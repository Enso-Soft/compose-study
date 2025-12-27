package com.example.audio_recording

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
 * 연습 문제 1: 기본 녹음 시작/중지 구현
 *
 * 요구사항:
 * - MediaRecorder 인스턴스를 remember로 생성
 * - DisposableEffect로 리소스 해제 구현
 * - 녹음 시작/중지 버튼 구현
 * - 권한 체크 포함
 *
 * TODO: 주석 처리된 코드를 참고하여 구현하세요!
 */
@Composable
fun Practice1_BasicRecording() {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("대기 중") }

    // 권한 체크
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    // TODO 1: MediaRecorder 인스턴스 생성
    // 힌트: remember 사용, API 31 분기 처리
    /*
    val recorder = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }
    */

    // TODO 2: DisposableEffect로 리소스 해제
    // 힌트: onDispose 블록에서 release() 호출
    /*
    DisposableEffect(Unit) {
        onDispose {
            try {
                if (isRecording) {
                    recorder.stop()
                }
            } catch (e: Exception) { }
            recorder.release()
        }
    }
    */

    // TODO 3: 녹음 시작 함수
    fun startRecording() {
        // 힌트:
        // 1. 파일 경로 생성
        // 2. recorder.reset()
        // 3. setAudioSource(MediaRecorder.AudioSource.MIC)
        // 4. setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        // 5. setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        // 6. setOutputFile(filePath)
        // 7. prepare()
        // 8. start()

        /*
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val filePath = File(context.filesDir, "practice1_$timestamp.m4a").absolutePath

            recorder.apply {
                reset()
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(filePath)
                prepare()
                start()
            }
            isRecording = true
            statusMessage = "녹음 중..."
        } catch (e: Exception) {
            statusMessage = "오류: ${e.message}"
        }
        */
        statusMessage = "TODO: startRecording() 구현 필요"
    }

    // TODO 4: 녹음 중지 함수
    fun stopRecording() {
        // 힌트: recorder.stop() 호출

        /*
        try {
            recorder.stop()
            isRecording = false
            statusMessage = "녹음 완료!"
        } catch (e: Exception) {
            statusMessage = "오류: ${e.message}"
        }
        */
        statusMessage = "TODO: stopRecording() 구현 필요"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 1: 기본 녹음",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: MediaRecorder 생명주기를 관리하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isRecording)
                    RecordingRed.copy(alpha = 0.1f)
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.FiberManualRecord else Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = if (isRecording) RecordingRed else MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "권한: ${if (hasPermission) "허용됨" else "필요함"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 권한 요청 버튼 (필요시)
        if (!hasPermission) {
            Button(
                onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }
            ) {
                Icon(Icons.Default.Security, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("권한 요청")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 녹음 버튼
        Button(
            onClick = {
                if (!hasPermission) {
                    statusMessage = "권한이 필요합니다!"
                    return@Button
                }
                if (isRecording) stopRecording() else startRecording()
            },
            enabled = hasPermission,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isRecording) "녹음 중지" else "녹음 시작")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. remember로 MediaRecorder 인스턴스 생성")
                Text("2. DisposableEffect.onDispose에서 release() 호출")
                Text("3. startRecording에서 설정 + prepare() + start()")
                Text("4. stopRecording에서 stop() 호출")
            }
        }
    }
}

/**
 * 연습 문제 2: 녹음 시간 타이머 표시
 *
 * 요구사항:
 * - LaunchedEffect를 사용하여 1초마다 타이머 증가
 * - MM:SS 형식으로 시간 표시
 * - 녹음 중일 때만 타이머 동작
 *
 * TODO: LaunchedEffect를 사용해서 타이머를 구현하세요!
 */
@Composable
fun Practice2_RecordingTimer() {
    var isRecording by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    // TODO: LaunchedEffect로 타이머 구현
    // 힌트: key는 isRecording, while(true) + delay(1000)

    /*
    LaunchedEffect(isRecording) {
        if (isRecording) {
            elapsedSeconds = 0
            while (true) {
                delay(1000L)
                elapsedSeconds++
            }
        }
    }
    */

    // 시간 포맷팅 함수
    fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return "%02d:%02d".format(min, sec)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 2: 녹음 타이머",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: LaunchedEffect로 타이머를 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 타이머 표시
        Card(
            modifier = Modifier.size(200.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isRecording)
                    RecordingRed.copy(alpha = 0.1f)
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isRecording) {
                        Icon(
                            Icons.Default.FiberManualRecord,
                            contentDescription = null,
                            tint = RecordingRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = formatTime(elapsedSeconds),
                        style = MaterialTheme.typography.displayLarge,
                        color = if (isRecording) RecordingRed else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 시작/중지 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    if (!isRecording) {
                        isRecording = true
                    }
                },
                enabled = !isRecording,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmplitudeGreen
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("시작")
            }

            Button(
                onClick = {
                    isRecording = false
                    // elapsedSeconds는 유지 (마지막 녹음 시간 확인용)
                },
                enabled = isRecording,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RecordingRed
                )
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("중지")
            }

            OutlinedButton(
                onClick = {
                    isRecording = false
                    elapsedSeconds = 0
                }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("리셋")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. LaunchedEffect(isRecording) 사용")
                Text("2. if (isRecording) 조건 체크")
                Text("3. while(true) { delay(1000); elapsedSeconds++ }")
                Text("4. isRecording이 false가 되면 코루틴 자동 취소")
            }
        }
    }
}

/**
 * 연습 문제 3: 녹음된 파일 목록 + 재생 기능
 *
 * 요구사항:
 * - 저장된 녹음 파일 목록 표시
 * - MediaPlayer로 파일 재생
 * - DisposableEffect로 MediaPlayer 리소스 관리
 * - 파일 삭제 기능
 *
 * TODO: 파일 목록 로드 + MediaPlayer 재생 기능을 구현하세요!
 */
@Composable
fun Practice3_FileListPlayback() {
    val context = LocalContext.current
    var recordings by remember { mutableStateOf<List<File>>(emptyList()) }
    var currentlyPlaying by remember { mutableStateOf<String?>(null) }
    var playerStatus by remember { mutableStateOf("재생 대기") }

    // TODO 1: MediaPlayer 인스턴스 생성 + DisposableEffect로 관리
    // 힌트: remember로 생성, onDispose에서 release()

    /*
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
    */

    // TODO 2: 파일 목록 로드 (LaunchedEffect 사용)
    // 힌트: context.filesDir에서 .m4a 파일 필터링

    /*
    LaunchedEffect(Unit) {
        recordings = context.filesDir.listFiles()
            ?.filter { it.name.endsWith(".m4a") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }
    */

    // TODO 3: 재생 함수
    fun playFile(file: File) {
        // 힌트:
        // 1. 이미 재생 중이면 stop()
        // 2. reset() -> setDataSource() -> prepare() -> start()
        // 3. setOnCompletionListener로 완료 처리

        /*
        try {
            if (currentlyPlaying == file.absolutePath) {
                mediaPlayer.stop()
                currentlyPlaying = null
                playerStatus = "재생 대기"
            } else {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(file.absolutePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                currentlyPlaying = file.absolutePath
                playerStatus = "재생 중: ${file.name}"

                mediaPlayer.setOnCompletionListener {
                    currentlyPlaying = null
                    playerStatus = "재생 완료"
                }
            }
        } catch (e: Exception) {
            playerStatus = "오류: ${e.message}"
        }
        */
        playerStatus = "TODO: playFile() 구현 필요"
    }

    // 파일 삭제 함수
    fun deleteFile(file: File) {
        file.delete()
        // 목록 갱신
        recordings = context.filesDir.listFiles()
            ?.filter { it.name.endsWith(".m4a") }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: 파일 목록 + 재생",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: MediaPlayer로 녹음 파일을 재생하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (currentlyPlaying != null)
                    AmplitudeGreen.copy(alpha = 0.1f)
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (currentlyPlaying != null)
                        Icons.Default.VolumeUp
                    else
                        Icons.Default.VolumeOff,
                    contentDescription = null,
                    tint = if (currentlyPlaying != null)
                        AmplitudeGreen
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = playerStatus,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 파일 목록
        Text(
            text = "녹음 파일 (${recordings.size}개)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (recordings.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.FolderOff,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "녹음 파일이 없습니다",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Solution 탭에서 녹음을 먼저 해보세요!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recordings) { file ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
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
                                        text = formatFileSizeForPractice(file.length()),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row {
                                IconButton(onClick = { playFile(file) }) {
                                    Icon(
                                        if (currentlyPlaying == file.absolutePath)
                                            Icons.Default.Stop
                                        else
                                            Icons.Default.PlayArrow,
                                        contentDescription = if (currentlyPlaying == file.absolutePath)
                                            "정지"
                                        else
                                            "재생",
                                        tint = if (currentlyPlaying == file.absolutePath)
                                            RecordingRed
                                        else
                                            AmplitudeGreen
                                    )
                                }
                                IconButton(onClick = { deleteFile(file) }) {
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
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. remember { MediaPlayer() }로 인스턴스 생성")
                Text("2. DisposableEffect.onDispose에서 release()")
                Text("3. LaunchedEffect(Unit)에서 파일 목록 로드")
                Text("4. playFile에서 reset() -> setDataSource() -> prepare() -> start()")
            }
        }
    }
}

private fun formatFileSizeForPractice(size: Long): String {
    return when {
        size < 1024 -> "$size B"
        size < 1024 * 1024 -> "${size / 1024} KB"
        else -> "%.1f MB".format(size / (1024.0 * 1024.0))
    }
}
