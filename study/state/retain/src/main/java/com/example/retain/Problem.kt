package com.example.retain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.UUID

/**
 * Problem: remember와 rememberSaveable로는 해결할 수 없는 상황
 *
 * 이 화면에서는 직렬화가 불가능한 객체를 Configuration Change에서
 * 유지해야 할 때 발생하는 문제를 보여줍니다:
 *
 * 1. remember 사용 시: 화면 회전마다 객체가 재생성됨
 * 2. rememberSaveable 시도: 직렬화 불가능한 객체는 저장 불가
 *
 * 실습: 각 예시에서 값을 변경한 후 화면을 회전해보세요!
 */

// ============================================================
// 시뮬레이션용 클래스들 (실제 ExoPlayer 대신 사용)
// ============================================================

/**
 * 미디어 플레이어를 시뮬레이션하는 클래스
 * 실제 ExoPlayer처럼 직렬화가 불가능한 복잡한 객체를 표현합니다.
 */
class MockMediaPlayer {
    // 각 인스턴스를 구별하기 위한 고유 ID
    val instanceId: String = UUID.randomUUID().toString().take(8)

    // 재생 상태
    var isPlaying: Boolean = false
        private set

    // 현재 재생 위치 (초)
    var currentPosition: Int = 0
        private set

    // 총 재생 시간 (초)
    val duration: Int = 180  // 3분 영상

    // 생성 시간 (인스턴스 재생성 여부 확인용)
    val createdAt: Long = System.currentTimeMillis()

    fun play() {
        isPlaying = true
    }

    fun pause() {
        isPlaying = false
    }

    fun seekTo(position: Int) {
        currentPosition = position.coerceIn(0, duration)
    }

    fun advancePosition(seconds: Int = 5) {
        currentPosition = (currentPosition + seconds).coerceAtMost(duration)
    }

    companion object {
        // 전역 생성 카운터 (인스턴스가 몇 번 생성되었는지 추적)
        var creationCount: Int = 0
            private set

        fun create(): MockMediaPlayer {
            creationCount++
            return MockMediaPlayer()
        }

        fun resetCounter() {
            creationCount = 0
        }
    }
}

/**
 * 이미지 캐시를 시뮬레이션하는 클래스
 * 실제 Bitmap 캐시처럼 직렬화가 불가능한 객체를 표현합니다.
 */
class MockImageCache {
    val instanceId: String = UUID.randomUUID().toString().take(8)

    private val cache = mutableMapOf<String, String>()
    var loadCount: Int = 0
        private set

    fun getOrLoad(url: String): String {
        return cache.getOrPut(url) {
            loadCount++
            "Image_${url.hashCode()}"  // 실제로는 Bitmap
        }
    }

    fun getCacheSize(): Int = cache.size

    companion object {
        var creationCount: Int = 0
            private set

        fun create(): MockImageCache {
            creationCount++
            return MockImageCache()
        }

        fun resetCounter() {
            creationCount = 0
        }
    }
}

// ============================================================
// Problem Screen
// ============================================================

@Composable
fun ProblemScreen() {
    // 화면 진입 시 카운터 리셋
    LaunchedEffect(Unit) {
        MockMediaPlayer.resetCounter()
        MockImageCache.resetCounter()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 경고 배너
        RotationWarningBanner()

        HorizontalDivider()

        // 문제 1: remember로 미디어 플레이어 관리
        Text(
            text = "문제 1: remember로 미디어 플레이어 관리",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        BrokenMediaPlayer()
        ProblemExplanationCard(
            explanation = "remember는 Recomposition 간에만 값을 유지합니다. " +
                    "화면 회전 시 Activity가 재생성되면서 Composition이 새로 시작되고, " +
                    "MockMediaPlayer 인스턴스가 새로 생성됩니다. " +
                    "재생 위치와 상태가 모두 초기화됩니다!"
        )

        HorizontalDivider()

        // 문제 2: remember로 이미지 캐시 관리
        Text(
            text = "문제 2: remember로 이미지 캐시 관리",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        BrokenImageCache()
        ProblemExplanationCard(
            explanation = "이미지 캐시도 마찬가지로 화면 회전 시 초기화됩니다. " +
                    "이미 로드했던 이미지들을 다시 로드해야 하므로 " +
                    "네트워크 트래픽과 로딩 시간이 증가합니다."
        )

        HorizontalDivider()

        // 문제 3: rememberSaveable 시도 (실패)
        Text(
            text = "문제 3: rememberSaveable은 해결책이 아닌 이유",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        RememberSaveableLimitation()

        HorizontalDivider()

        // 핵심 문제 정리
        CoreProblemSummary()
    }
}

@Composable
fun RotationWarningBanner() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Column {
                Text(
                    text = "화면을 회전해보세요!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "아래 예시들에서 값을 변경한 후 화면을 회전하면 " +
                            "모든 상태가 초기화되는 것을 확인할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

/**
 * 문제 1: remember로 미디어 플레이어 관리
 * 화면 회전 시 플레이어가 재생성되어 재생 상태가 초기화됨
 */
@Composable
fun BrokenMediaPlayer() {
    // remember만 사용 - 화면 회전 시 재생성됨!
    val player = remember { MockMediaPlayer.create() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 인스턴스 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "인스턴스 ID: ${player.instanceId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "생성 횟수: ${MockMediaPlayer.creationCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (MockMediaPlayer.creationCount > 1) Color.Red else Color.Gray,
                    fontWeight = if (MockMediaPlayer.creationCount > 1) FontWeight.Bold else FontWeight.Normal
                )
            }

            // 재생 상태 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (player.isPlaying) "재생 중" else "일시정지",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 진행 바
                    LinearProgressIndicator(
                        progress = { player.currentPosition.toFloat() / player.duration },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Text(
                        text = "${formatTime(player.currentPosition)} / ${formatTime(player.duration)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 컨트롤 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 상태 변경을 위해 recomposition 트리거
                var refreshTrigger by remember { mutableIntStateOf(0) }

                FilledTonalButton(
                    onClick = {
                        if (player.isPlaying) player.pause() else player.play()
                        refreshTrigger++
                    }
                ) {
                    Text(if (player.isPlaying) "일시정지" else "재생")
                }

                FilledTonalButton(
                    onClick = {
                        player.advancePosition(30)
                        refreshTrigger++
                    }
                ) {
                    Text("+30초")
                }

                // refreshTrigger 사용 (경고 방지)
                if (refreshTrigger < 0) Text("")
            }

            // 문제 코드 표시
            Text(
                text = "val player = remember { MockMediaPlayer.create() }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

/**
 * 문제 2: remember로 이미지 캐시 관리
 * 화면 회전 시 캐시가 초기화되어 이미지 재로드 필요
 */
@Composable
fun BrokenImageCache() {
    // remember만 사용 - 화면 회전 시 캐시 초기화!
    val cache = remember { MockImageCache.create() }

    // 이미지 로드 시뮬레이션
    val imageUrls = listOf(
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg",
        "https://example.com/image3.jpg"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 캐시 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "캐시 ID: ${cache.instanceId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "캐시 생성 횟수: ${MockImageCache.creationCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (MockImageCache.creationCount > 1) Color.Red else Color.Gray,
                    fontWeight = if (MockImageCache.creationCount > 1) FontWeight.Bold else FontWeight.Normal
                )
            }

            // 캐시 상태
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "캐시된 이미지: ${cache.getCacheSize()}개",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "총 로드 횟수: ${cache.loadCount}회",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (cache.loadCount > imageUrls.size) Color.Red else Color.Gray
                    )
                }
            }

            // 이미지 로드 버튼
            var refreshTrigger by remember { mutableIntStateOf(0) }

            Button(
                onClick = {
                    imageUrls.forEach { url ->
                        cache.getOrLoad(url)
                    }
                    refreshTrigger++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("이미지 3개 로드")
            }

            // refreshTrigger 사용 (경고 방지)
            if (refreshTrigger < 0) Text("")

            Text(
                text = "화면 회전 후 다시 로드하면 로드 횟수가 증가합니다!",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE65100)
            )

            // 문제 코드 표시
            Text(
                text = "val cache = remember { MockImageCache.create() }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

/**
 * 문제 3: rememberSaveable의 한계
 * 직렬화 불가능한 객체에는 사용할 수 없음
 */
@Composable
fun RememberSaveableLimitation() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3E5F5)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "rememberSaveable을 쓰면 되지 않나요?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "rememberSaveable은 Bundle에 저장하므로 직렬화가 필요합니다. " +
                        "하지만 다음 객체들은 직렬화가 불가능합니다:",
                style = MaterialTheme.typography.bodyMedium
            )

            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("- ExoPlayer (미디어 플레이어)")
                Text("- Bitmap (이미지)")
                Text("- Flow / StateFlow (데이터 스트림)")
                Text("- Lambda / Callback (함수 참조)")
                Text("- Socket / Connection (네트워크 연결)")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 실패 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "시도하면 실패하는 코드:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = """
// ExoPlayer는 Parcelable이 아님!
val player = rememberSaveable {
    ExoPlayer.Builder(context).build()
}
// 컴파일 에러 또는 런타임 에러!
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                }
            }

            Text(
                text = "이런 객체들을 Configuration Change에서 유지하려면 " +
                        "새로운 API가 필요합니다...",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 핵심 문제 정리
 */
@Composable
fun CoreProblemSummary() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "핵심 문제 정리",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "1. remember: 화면 회전 시 객체가 재생성됨",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "2. rememberSaveable: 직렬화 불가능한 객체는 저장 불가",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "3. 필요한 것: 직렬화 없이 Configuration Change를 견디는 API",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Solution 탭에서 retain을 사용한 해결책을 확인하세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ProblemExplanationCard(explanation: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "왜 문제인가요?",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// 시간 포맷 유틸리티
private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}
