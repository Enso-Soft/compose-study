package com.example.media3_player

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.text.SimpleDateFormat
import java.util.*

// 샘플 비디오 URL (공개 도메인)
private const val SAMPLE_VIDEO_URL =
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

/**
 * 올바른 코드 - remember + DisposableEffect로 생명주기 관리
 *
 * 핵심 포인트:
 * 1. remember로 플레이어 인스턴스 유지
 * 2. DisposableEffect의 onDispose에서 release() 호출
 * 3. LifecycleEventObserver로 앱 생명주기 연동 (pause/resume)
 */
@Composable
fun SolutionScreen() {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current

    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var playbackState by remember { mutableIntStateOf(Player.STATE_IDLE) }

    // 생명주기 이벤트 로그
    val lifecycleEvents = remember { mutableStateListOf<LifecycleLog>() }

    // remember로 플레이어 인스턴스 유지 (Recomposition에도 재생성 안됨)
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(SAMPLE_VIDEO_URL))
            prepare()
            playWhenReady = true
        }
    }

    // 플레이어 상태 리스너
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(state: Int) {
                playbackState = state
                if (state == Player.STATE_READY) {
                    duration = player.duration
                }
            }
        }
        player.addListener(listener)

        onDispose {
            player.removeListener(listener)
        }
    }

    // 재생 위치 업데이트 (1초마다)
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition
            kotlinx.coroutines.delay(1000)
        }
    }

    // 생명주기 관리 (앱 백그라운드/포그라운드)
    DisposableEffect(lifecycleOwner, player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    player.pause()
                    lifecycleEvents.add(0, LifecycleLog("ON_PAUSE", "player.pause() 호출"))
                }
                Lifecycle.Event.ON_RESUME -> {
                    lifecycleEvents.add(0, LifecycleLog("ON_RESUME", "재생 준비됨"))
                    if (player.playbackState == Player.STATE_READY) {
                        player.play()
                    }
                }
                Lifecycle.Event.ON_START -> {
                    lifecycleEvents.add(0, LifecycleLog("ON_START", "화면 표시됨"))
                }
                Lifecycle.Event.ON_STOP -> {
                    lifecycleEvents.add(0, LifecycleLog("ON_STOP", "화면 숨겨짐"))
                }
                else -> {}
            }
            // 로그 5개만 유지
            while (lifecycleEvents.size > 5) {
                lifecycleEvents.removeAt(lifecycleEvents.lastIndex)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        lifecycleEvents.add(0, LifecycleLog("INIT", "Observer 등록됨"))

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            lifecycleEvents.add(0, LifecycleLog("DISPOSE", "player.release() 호출!"))
            player.release() // 핵심: 여기서 플레이어 해제!
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 헤더
        item {
            Text(
                text = "Solution: 올바른 플레이어 관리",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // 비디오 플레이어
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player
                            useController = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }

        // 재생 상태 표시
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "재생 상태",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("상태: ${getPlaybackStateText(playbackState)}")
                            Text("재생 중: ${if (isPlaying) "Yes" else "No"}")
                        }
                        Text(
                            text = "${formatTime(currentPosition)} / ${formatTime(duration)}",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }

        // Compose 기반 재생 컨트롤
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 10초 뒤로
                IconButton(onClick = {
                    player.seekTo(maxOf(0, player.currentPosition - 10000))
                }) {
                    Icon(Icons.Default.Replay10, contentDescription = "10초 뒤로")
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Play/Pause
                FilledIconButton(
                    onClick = {
                        if (isPlaying) player.pause() else player.play()
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "일시정지" else "재생",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 10초 앞으로
                IconButton(onClick = {
                    player.seekTo(minOf(player.duration, player.currentPosition + 10000))
                }) {
                    Icon(Icons.Default.Forward10, contentDescription = "10초 앞으로")
                }
            }
        }

        // 생명주기 이벤트 로그
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "생명주기 이벤트 로그",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "실시간",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "앱을 백그라운드로 보내거나 탭을 전환해보세요!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (lifecycleEvents.isEmpty()) {
                        Text(
                            text = "(아직 이벤트 없음)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            lifecycleEvents.forEach { log ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = log.time,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = log.event,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = getEventColor(log.event)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = log.message,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Problem vs Solution 비교
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Problem vs Solution 비교",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ComparisonRow("인스턴스 생성", "매번 새로 생성", "remember로 유지")
                    ComparisonRow("리소스 정리", "release() 없음", "onDispose에서 release()")
                    ComparisonRow("생명주기", "무시", "Observer로 연동")
                    ComparisonRow("백그라운드", "계속 재생", "자동 pause")
                    ComparisonRow("결과", "OOM 크래시", "안정적 동작")
                }
            }
        }

        // 핵심 코드 설명
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "핵심 코드 패턴",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CodeBlock(
                        title = "1. remember로 플레이어 유지",
                        code = """
val player = remember {
    ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(url))
        prepare()
    }
}
                        """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CodeBlock(
                        title = "2. DisposableEffect로 정리",
                        code = """
DisposableEffect(player) {
    onDispose {
        player.release()  // 핵심!
    }
}
                        """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CodeBlock(
                        title = "3. 생명주기 연동",
                        code = """
val observer = LifecycleEventObserver { _, event ->
    when (event) {
        ON_PAUSE -> player.pause()
        ON_RESUME -> player.play()
    }
}
lifecycleOwner.lifecycle.addObserver(observer)
                        """.trimIndent()
                    )
                }
            }
        }

        // 학습 완료 체크리스트
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "학습 체크리스트",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ChecklistItem("remember가 Recomposition에도 인스턴스를 유지함을 이해")
                    ChecklistItem("DisposableEffect의 onDispose가 언제 호출되는지 확인")
                    ChecklistItem("생명주기 로그에서 이벤트 순서 확인")
                    ChecklistItem("Problem 탭과 비교하여 차이점 이해")
                    ChecklistItem("Practice 탭에서 직접 구현 시도!")
                }
            }
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 생명주기 로그 데이터 클래스
 */
private data class LifecycleLog(
    val event: String,
    val message: String,
    val time: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
)

/**
 * 비교 테이블 행
 */
@Composable
private fun ComparisonRow(label: String, problem: String, solution: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = problem,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = solution,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 코드 블록 컴포넌트
 */
@Composable
private fun CodeBlock(title: String, code: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = code,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

/**
 * 체크리스트 아이템
 */
@Composable
private fun ChecklistItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun getEventColor(event: String) = when (event) {
    "ON_PAUSE", "ON_STOP" -> MaterialTheme.colorScheme.error
    "ON_RESUME", "ON_START" -> MaterialTheme.colorScheme.primary
    "DISPOSE" -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.onSurface
}

private fun getPlaybackStateText(state: Int): String {
    return when (state) {
        Player.STATE_IDLE -> "IDLE (초기화 전)"
        Player.STATE_BUFFERING -> "BUFFERING (버퍼링 중)"
        Player.STATE_READY -> "READY (재생 준비 완료)"
        Player.STATE_ENDED -> "ENDED (재생 종료)"
        else -> "UNKNOWN"
    }
}

private fun formatTime(millis: Long): String {
    if (millis < 0) return "00:00"
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
