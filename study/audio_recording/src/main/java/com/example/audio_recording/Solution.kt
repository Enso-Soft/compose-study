package com.example.audio_recording

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.audio_recording.ui.theme.AmplitudeGreen
import com.example.audio_recording.ui.theme.RecordingRed
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 녹음 상태를 나타내는 Sealed Class
 * 상태 기계 패턴으로 녹음 상태를 명확히 관리
 */
sealed class RecordingState {
    object Idle : RecordingState()
    data class Recording(val startTime: Long) : RecordingState()
    data class Paused(val elapsedTime: Long) : RecordingState()
    data class Completed(val filePath: String, val duration: Long) : RecordingState()
    data class Error(val message: String) : RecordingState()
}

/**
 * 올바른 코드 - DisposableEffect로 MediaRecorder 생명주기 관리
 * + 권한 처리 + 상태 기계 패턴
 */
@Composable
fun SolutionScreen() {
    val context = LocalContext.current

    // 권한 상태 관리
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    if (!hasPermission) {
        PermissionRequestScreen(
            onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        )
    } else {
        RecorderScreen()
    }
}

@Composable
private fun PermissionRequestScreen(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "마이크 권한이 필요합니다",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "오디오 녹음을 위해 마이크 접근 권한을\n허용해주세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRequestPermission) {
            Icon(Icons.Default.Security, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("권한 요청")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: 권한 처리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- rememberLauncherForActivityResult로 권한 요청")
                Text("- ActivityResultContracts.RequestPermission() 사용")
                Text("- 권한 결과에 따라 UI 분기 처리")
            }
        }
    }
}

@Composable
private fun RecorderScreen() {
    val context = LocalContext.current

    // 녹음 상태
    var recordingState by remember { mutableStateOf<RecordingState>(RecordingState.Idle) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var amplitude by remember { mutableFloatStateOf(0f) }
    var recordings by remember { mutableStateOf<List<File>>(emptyList()) }

    // 현재 재생 중인 파일
    var playingFile by remember { mutableStateOf<String?>(null) }

    // MediaPlayer 관리
    val mediaPlayer = remember { MediaPlayer() }

    // 녹음 파일 경로 생성
    val outputFilePath = remember {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        File(context.filesDir, "recording_$timestamp.m4a").absolutePath
    }

    // MediaRecorder 인스턴스 (remember로 유지)
    val recorder = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    // DisposableEffect: 생명주기 관리의 핵심!
    DisposableEffect(Unit) {
        // 초기 녹음 파일 목록 로드
        recordings = loadRecordings(context.filesDir)

        onDispose {
            // 화면 이탈 시 안전하게 리소스 해제
            try {
                if (recordingState is RecordingState.Recording ||
                    recordingState is RecordingState.Paused) {
                    recorder.stop()
                }
            } catch (e: Exception) {
                // IllegalStateException 무시
            }
            recorder.release()
            mediaPlayer.release()
        }
    }

    // 녹음 중 타이머 + 진폭 측정
    LaunchedEffect(recordingState) {
        if (recordingState is RecordingState.Recording) {
            elapsedSeconds = 0
            while (recordingState is RecordingState.Recording) {
                delay(100L)
                // 진폭 측정 (0 ~ 32767)
                try {
                    val maxAmplitude = recorder.maxAmplitude
                    amplitude = maxAmplitude / 32767f
                } catch (e: Exception) {
                    amplitude = 0f
                }

                // 1초마다 시간 증가
                if ((elapsedSeconds * 10 + 1) % 10 == 0) {
                    elapsedSeconds++
                }
            }
        } else {
            amplitude = 0f
        }
    }

    // 1초 단위 타이머 (더 정확한 타이머)
    LaunchedEffect(recordingState) {
        if (recordingState is RecordingState.Recording) {
            elapsedSeconds = 0
            while (recordingState is RecordingState.Recording) {
                delay(1000L)
                elapsedSeconds++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "DisposableEffect + 권한 처리 + 상태 기계",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 녹음 상태 표시
        RecordingStateCard(
            recordingState = recordingState,
            elapsedSeconds = elapsedSeconds
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 진폭 시각화
        if (recordingState is RecordingState.Recording) {
            AmplitudeVisualizer(amplitude = amplitude)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 타이머 표시
        Text(
            text = formatTime(elapsedSeconds),
            style = MaterialTheme.typography.displayLarge,
            color = if (recordingState is RecordingState.Recording)
                RecordingRed else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 녹음 컨트롤 버튼들
        RecordingControls(
            recordingState = recordingState,
            onStart = {
                try {
                    // 새 파일 경로 생성
                    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    val filePath = File(context.filesDir, "recording_$timestamp.m4a").absolutePath

                    recorder.apply {
                        reset()
                        setAudioSource(MediaRecorder.AudioSource.MIC)
                        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        setOutputFile(filePath)
                        prepare()
                        start()
                    }
                    recordingState = RecordingState.Recording(System.currentTimeMillis())
                } catch (e: Exception) {
                    recordingState = RecordingState.Error(e.message ?: "녹음 시작 실패")
                }
            },
            onPause = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        recorder.pause()
                        recordingState = RecordingState.Paused(elapsedSeconds.toLong())
                    } catch (e: Exception) {
                        recordingState = RecordingState.Error(e.message ?: "일시정지 실패")
                    }
                }
            },
            onResume = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        recorder.resume()
                        recordingState = RecordingState.Recording(System.currentTimeMillis())
                    } catch (e: Exception) {
                        recordingState = RecordingState.Error(e.message ?: "재개 실패")
                    }
                }
            },
            onStop = {
                try {
                    recorder.stop()
                    // 파일 목록 갱신
                    recordings = loadRecordings(context.filesDir)
                    recordingState = RecordingState.Completed(
                        filePath = outputFilePath,
                        duration = elapsedSeconds.toLong()
                    )
                } catch (e: Exception) {
                    recordingState = RecordingState.Error(e.message ?: "녹음 중지 실패")
                }
            },
            onReset = {
                elapsedSeconds = 0
                recordingState = RecordingState.Idle
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 녹음 파일 목록
        if (recordings.isNotEmpty()) {
            Text(
                text = "녹음 파일 목록",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(recordings) { file ->
                    RecordingFileItem(
                        file = file,
                        isPlaying = playingFile == file.absolutePath,
                        onPlay = {
                            try {
                                if (playingFile == file.absolutePath) {
                                    mediaPlayer.stop()
                                    playingFile = null
                                } else {
                                    mediaPlayer.reset()
                                    mediaPlayer.setDataSource(file.absolutePath)
                                    mediaPlayer.prepare()
                                    mediaPlayer.start()
                                    playingFile = file.absolutePath
                                    mediaPlayer.setOnCompletionListener {
                                        playingFile = null
                                    }
                                }
                            } catch (e: Exception) {
                                // 재생 오류 처리
                            }
                        },
                        onDelete = {
                            file.delete()
                            recordings = loadRecordings(context.filesDir)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- DisposableEffect.onDispose에서 recorder.release() 호출")
                Text("- rememberLauncherForActivityResult로 권한 처리")
                Text("- Sealed class로 녹음 상태 명확히 관리")
                Text("- LaunchedEffect로 타이머 및 진폭 측정")
            }
        }
    }
}

@Composable
private fun RecordingStateCard(
    recordingState: RecordingState,
    elapsedSeconds: Int
) {
    val (stateText, stateColor) = when (recordingState) {
        is RecordingState.Idle -> "대기 중" to MaterialTheme.colorScheme.onSurfaceVariant
        is RecordingState.Recording -> "녹음 중" to RecordingRed
        is RecordingState.Paused -> "일시정지" to MaterialTheme.colorScheme.tertiary
        is RecordingState.Completed -> "녹음 완료" to AmplitudeGreen
        is RecordingState.Error -> "오류 발생" to MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = stateColor.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (recordingState) {
                        is RecordingState.Idle -> Icons.Default.RadioButtonUnchecked
                        is RecordingState.Recording -> Icons.Default.FiberManualRecord
                        is RecordingState.Paused -> Icons.Default.Pause
                        is RecordingState.Completed -> Icons.Default.CheckCircle
                        is RecordingState.Error -> Icons.Default.Error
                    },
                    contentDescription = null,
                    tint = stateColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stateText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = stateColor
                )
            }

            if (recordingState is RecordingState.Error) {
                Text(
                    text = recordingState.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun AmplitudeVisualizer(amplitude: Float) {
    val animatedAmplitude by animateFloatAsState(
        targetValue = amplitude,
        label = "amplitude"
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val barCount = 20
        val barWidth = size.width / (barCount * 2)
        val maxBarHeight = size.height * 0.8f

        for (i in 0 until barCount) {
            // 중앙에서 멀어질수록 높이 감소 효과
            val distanceFromCenter = kotlin.math.abs(i - barCount / 2f) / (barCount / 2f)
            val heightFactor = 1f - distanceFromCenter * 0.5f
            val barHeight = maxBarHeight * animatedAmplitude * heightFactor *
                (0.5f + kotlin.random.Random.nextFloat() * 0.5f)

            drawLine(
                color = AmplitudeGreen,
                start = Offset(
                    x = barWidth + i * barWidth * 2,
                    y = size.height / 2 + barHeight / 2
                ),
                end = Offset(
                    x = barWidth + i * barWidth * 2,
                    y = size.height / 2 - barHeight / 2
                ),
                strokeWidth = barWidth * 0.8f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun RecordingControls(
    recordingState: RecordingState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (recordingState) {
            is RecordingState.Idle -> {
                FilledIconButton(
                    onClick = onStart,
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = RecordingRed
                    )
                ) {
                    Icon(
                        Icons.Default.FiberManualRecord,
                        contentDescription = "녹음 시작",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            is RecordingState.Recording -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FilledIconButton(
                        onClick = onPause,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = "일시정지")
                    }
                }

                FilledIconButton(
                    onClick = onStop,
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Stop,
                        contentDescription = "녹음 중지",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            is RecordingState.Paused -> {
                FilledIconButton(
                    onClick = onResume,
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = AmplitudeGreen
                    )
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "재개")
                }

                FilledIconButton(
                    onClick = onStop,
                    modifier = Modifier.size(64.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Stop,
                        contentDescription = "녹음 중지",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            is RecordingState.Completed, is RecordingState.Error -> {
                FilledIconButton(
                    onClick = onReset,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "다시 녹음",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordingFileItem(
    file: File,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.AudioFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatFileSize(file.length()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row {
                IconButton(onClick = onPlay) {
                    Icon(
                        if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "정지" else "재생",
                        tint = if (isPlaying) RecordingRed else AmplitudeGreen
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun loadRecordings(directory: File): List<File> {
    return directory.listFiles()
        ?.filter { it.name.startsWith("recording_") && it.name.endsWith(".m4a") }
        ?.sortedByDescending { it.lastModified() }
        ?: emptyList()
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

private fun formatFileSize(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${size / 1024} KB"
        else -> "%.1f MB".format(size / (1024.0 * 1024.0))
    }
}
