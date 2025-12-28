package com.example.media3_player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

// 샘플 비디오 URL (공개 도메인)
private const val SAMPLE_VIDEO_URL =
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

/**
 * 문제 시연 화면 - ExoPlayer 생명주기 미관리
 *
 * 이 화면에서는 잘못된 방식으로 플레이어를 생성하면 어떤 문제가 발생하는지
 * 실제로 체험해볼 수 있습니다.
 *
 * 문제점:
 * 1. 메모리 누수: 플레이어가 release()되지 않음
 * 2. 플레이어 누적: 버튼 클릭마다 새 인스턴스 생성
 * 3. 다중 오디오 재생: 여러 플레이어가 동시에 재생됨
 */
@Composable
fun ProblemScreen() {
    val context = LocalContext.current.applicationContext

    // 생성된 플레이어 목록 (안전을 위해 최대 5개 제한)
    val players = remember { mutableStateListOf<PlayerInfo>() }
    var totalCreatedCount by remember { mutableIntStateOf(0) }

    // 화면 이탈 시 모든 플레이어 해제 (안전장치)
    DisposableEffect(Unit) {
        onDispose {
            players.forEach { it.player.release() }
            players.clear()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 헤더
        item {
            Text(
                text = "Problem: 플레이어 누수 시연",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        // 경고 카드
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "생성된 플레이어: ${players.size}개",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "총 생성 횟수: ${totalCreatedCount}회",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (players.size >= 2) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "다중 오디오가 동시에 재생됩니다!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        // 플레이어 생성 버튼
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (players.size < 5) {
                            // 잘못된 방법: release() 없이 새 플레이어 생성
                            val newPlayer = ExoPlayer.Builder(context).build().apply {
                                setMediaItem(MediaItem.fromUri(SAMPLE_VIDEO_URL))
                                prepare()
                                playWhenReady = true
                            }
                            players.add(
                                PlayerInfo(
                                    player = newPlayer,
                                    id = totalCreatedCount + 1
                                )
                            )
                            totalCreatedCount++
                        }
                    },
                    enabled = players.size < 5,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("플레이어 생성 (잘못된 방법)")
                }

                OutlinedButton(
                    onClick = {
                        players.forEach { it.player.release() }
                        players.clear()
                    },
                    enabled = players.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("모두 해제")
                }
            }
        }

        // 5개 제한 메시지
        if (players.size >= 5) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(
                        text = "안전을 위해 5개로 제한됨.\n" +
                                "실제로는 무한히 생성되어 OOM 크래시 발생!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // 생성된 플레이어 목록
        itemsIndexed(players) { index, playerInfo ->
            PlayerCard(
                playerInfo = playerInfo,
                onRemove = {
                    playerInfo.player.release()
                    players.removeAt(index)
                }
            )
        }

        // 문제 설명
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "왜 문제인가?",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. remember 없이 생성 → Recomposition마다 새 인스턴스")
                    Text("2. release() 호출 안함 → 메모리 누수")
                    Text("3. 이전 플레이어 참조 상실 → 정리 불가능")
                    Text("4. 다중 오디오 동시 재생 → 사용자 경험 저하")
                    Text("5. 누적되어 결국 OOM 크래시!")
                }
            }
        }

        // 잘못된 코드 예시
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "잘못된 코드 예시",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
@Composable
fun BadVideoPlayer() {
    val context = LocalContext.current

    // 문제: remember 없음!
    val player = ExoPlayer.Builder(context).build()

    // 문제: release() 호출 안함!
    player.setMediaItem(...)
    player.prepare()

    AndroidView(factory = { ... })
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 학습 포인트
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "학습 포인트",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• 플레이어 생성 버튼을 여러 번 눌러보세요")
                    Text("• 다중 오디오가 동시에 재생되는지 확인")
                    Text("• '모두 해제' 버튼으로 정상 해제 과정 이해")
                    Text("• Solution 탭에서 올바른 방법 확인!")
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
 * 플레이어 정보를 담는 데이터 클래스
 */
private data class PlayerInfo(
    val player: ExoPlayer,
    val id: Int
)

/**
 * 개별 플레이어 카드
 */
@Composable
private fun PlayerCard(
    playerInfo: PlayerInfo,
    onRemove: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(true) }
    var playbackState by remember { mutableIntStateOf(Player.STATE_IDLE) }

    // 플레이어 상태 리스너
    DisposableEffect(playerInfo.player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(state: Int) {
                playbackState = state
            }
        }
        playerInfo.player.addListener(listener)

        onDispose {
            playerInfo.player.removeListener(listener)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "플레이어 #${playerInfo.id}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "상태: ${getPlaybackStateText(playbackState)} | " +
                                "재생 중: ${if (isPlaying) "Yes" else "No"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row {
                    // 재생/일시정지 버튼
                    IconButton(onClick = {
                        if (isPlaying) {
                            playerInfo.player.pause()
                        } else {
                            playerInfo.player.play()
                        }
                    }) {
                        Text(if (isPlaying) "⏸" else "▶")
                    }
                    // 삭제 버튼
                    IconButton(onClick = onRemove) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "삭제",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // 미니 플레이어 뷰
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        this.player = playerInfo.player
                        useController = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}

private fun getPlaybackStateText(state: Int): String {
    return when (state) {
        Player.STATE_IDLE -> "IDLE"
        Player.STATE_BUFFERING -> "BUFFERING"
        Player.STATE_READY -> "READY"
        Player.STATE_ENDED -> "ENDED"
        else -> "UNKNOWN"
    }
}
