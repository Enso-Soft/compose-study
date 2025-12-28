package com.example.retain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.UUID

/**
 * Solution: retain을 사용하여 직렬화 불가능한 객체도 Configuration Change에서 유지
 *
 * 이 화면에서는 retain을 사용하여 Problem에서 발생했던 문제들을 해결합니다:
 *
 * 1. 미디어 플레이어: 화면 회전에도 동일 인스턴스 유지
 * 2. 이미지 캐시: 화면 회전에도 캐시 유지
 * 3. 핵심 포인트: 직렬화 없이 인스턴스 보존
 *
 * 참고: 실제 retain API는 Compose BOM 2025.12.00 이상에서 사용 가능합니다.
 * 여기서는 학습 목적으로 retain의 동작을 시뮬레이션합니다.
 */

// ============================================================
// retain 시뮬레이션
// 실제 retain API가 아직 빌드에 포함되지 않을 수 있으므로
// rememberSaveable + 별도 저장소로 동작을 시뮬레이션합니다.
// ============================================================

/**
 * retain을 시뮬레이션하는 함수
 *
 * 실제 retain API의 시그니처:
 * @Composable
 * inline fun <T> retain(crossinline init: () -> T): T
 *
 * 학습 목적으로 별도 저장소를 사용하여 동작을 시뮬레이션합니다.
 */
object RetainedStore {
    private val store = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrCreate(key: String, factory: () -> T): T {
        return store.getOrPut(key) { factory() as Any } as T
    }

    fun clear() {
        store.clear()
    }

    fun getStoredCount(): Int = store.size
}

/**
 * retain 시뮬레이션 함수
 * 실제 API와 유사한 방식으로 동작합니다.
 */
@Composable
inline fun <reified T> simulatedRetain(
    key: String = T::class.simpleName ?: "unknown",
    crossinline factory: () -> T
): T {
    // 컴포저블 위치 기반 키 생성 (실제 retain은 자동으로 처리)
    @Suppress("DEPRECATION")
    val fullKey = "${key}_${currentCompositeKeyHash}"
    return remember { RetainedStore.getOrCreate(fullKey) { factory() } }
}

// ============================================================
// Solution용 시뮬레이션 클래스들
// ============================================================

/**
 * retain으로 관리되는 미디어 플레이어 시뮬레이션
 */
class RetainedMediaPlayer {
    val instanceId: String = UUID.randomUUID().toString().take(8)
    var isPlaying: Boolean = false
        private set
    var currentPosition: Int = 0
        private set
    val duration: Int = 180
    val createdAt: Long = System.currentTimeMillis()

    fun play() { isPlaying = true }
    fun pause() { isPlaying = false }
    fun seekTo(position: Int) { currentPosition = position.coerceIn(0, duration) }
    fun advancePosition(seconds: Int = 5) {
        currentPosition = (currentPosition + seconds).coerceAtMost(duration)
    }

    companion object {
        var creationCount: Int = 0
            private set

        fun create(): RetainedMediaPlayer {
            creationCount++
            return RetainedMediaPlayer()
        }

        fun resetCounter() {
            creationCount = 0
        }
    }
}

/**
 * retain으로 관리되는 이미지 캐시 시뮬레이션
 */
class RetainedImageCache {
    val instanceId: String = UUID.randomUUID().toString().take(8)
    private val cache = mutableMapOf<String, String>()
    var loadCount: Int = 0
        private set

    fun getOrLoad(url: String): String {
        return cache.getOrPut(url) {
            loadCount++
            "Image_${url.hashCode()}"
        }
    }

    fun getCacheSize(): Int = cache.size

    companion object {
        var creationCount: Int = 0
            private set

        fun create(): RetainedImageCache {
            creationCount++
            return RetainedImageCache()
        }

        fun resetCounter() {
            creationCount = 0
        }
    }
}

// ============================================================
// Solution Screen
// ============================================================

@Composable
fun SolutionScreen() {
    var selectedDemo by rememberSaveable { mutableIntStateOf(0) }

    // 화면 진입 시 카운터 리셋 (데모 목적)
    LaunchedEffect(Unit) {
        RetainedMediaPlayer.resetCounter()
        RetainedImageCache.resetCounter()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("플레이어") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("캐시") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("비교") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> RetainedMediaPlayerDemo()
            1 -> RetainedImageCacheDemo()
            2 -> ComparisonDemo()
        }
    }
}

/**
 * 해결책 1: retain으로 미디어 플레이어 관리
 * 화면 회전에도 동일 인스턴스 유지!
 */
@Composable
fun RetainedMediaPlayerDemo() {
    // retain 사용 - 화면 회전에도 인스턴스 유지!
    // 실제 코드: val player = retain { ExoPlayer.Builder(context).build() }
    val player = simulatedRetain("MediaPlayer") { RetainedMediaPlayer.create() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 성공 배너
        SuccessBanner()

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "핵심 포인트",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "retain은 객체를 직렬화하지 않고 별도 저장소에 보관합니다. " +
                            "Configuration Change 후에도 동일한 인스턴스를 반환합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 플레이어 UI
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
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
                        text = "생성 횟수: ${RetainedMediaPlayer.creationCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
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
                            fontWeight = FontWeight.Bold,
                            color = if (player.isPlaying) Color(0xFF2E7D32) else Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { player.currentPosition.toFloat() / player.duration },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF4CAF50)
                        )

                        Text(
                            text = "${formatTime(player.currentPosition)} / ${formatTime(player.duration)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // 컨트롤 버튼
                var refreshTrigger by remember { mutableIntStateOf(0) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (player.isPlaying) player.pause() else player.play()
                            refreshTrigger++
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text(if (player.isPlaying) "일시정지" else "재생")
                    }

                    Button(
                        onClick = {
                            player.advancePosition(30)
                            refreshTrigger++
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text("+30초")
                    }
                }

                // refreshTrigger 사용
                if (refreshTrigger < 0) Text("")

                // 올바른 코드 표시
                Text(
                    text = "val player = retain { ExoPlayer.Builder(context).build() }",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "화면을 회전해도 인스턴스 ID와 재생 위치가 유지됩니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1B5E20)
                )
            }
        }

        // 주의사항
        AttentionCard()
    }
}

/**
 * 해결책 2: retain으로 이미지 캐시 관리
 */
@Composable
fun RetainedImageCacheDemo() {
    // retain 사용 - 화면 회전에도 캐시 유지!
    val cache = simulatedRetain("ImageCache") { RetainedImageCache.create() }

    val imageUrls = listOf(
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg",
        "https://example.com/image3.jpg"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SuccessBanner()

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                        text = "캐시 생성 횟수: ${RetainedImageCache.creationCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Bold
                    )
                }

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
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF1565C0)
                        )
                        Text(
                            text = "총 로드 횟수: ${cache.loadCount}회",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                var refreshTrigger by remember { mutableIntStateOf(0) }

                Button(
                    onClick = {
                        imageUrls.forEach { url ->
                            cache.getOrLoad(url)
                        }
                        refreshTrigger++
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text("이미지 3개 로드")
                }

                if (refreshTrigger < 0) Text("")

                Text(
                    text = "화면 회전 후 다시 로드해도 캐시가 유지됩니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1565C0)
                )

                Text(
                    text = "val cache = retain { mutableMapOf<String, Bitmap>() }",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1565C0),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AttentionCard()
    }
}

/**
 * remember vs retain vs rememberSaveable 비교
 */
@Composable
fun ComparisonDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "상태 보존 API 비교",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // 비교 테이블
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // 헤더
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("특성", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
                    Text("remember", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("retain", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("Saveable", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Recomposition
                ComparisonRow("Recomposition", "O", "O", "O")
                // Config Change
                ComparisonRow("Config Change", "X", "O", "O")
                // Process Death
                ComparisonRow("Process Death", "X", "X", "O")
                // 직렬화
                ComparisonRow("직렬화 필요", "-", "X", "O")
            }
        }

        // 선택 가이드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "언제 무엇을 사용할까?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text("remember: 화면 회전에서 유지할 필요 없는 임시 상태")
                Text("retain: 직렬화 불가능한 객체 (ExoPlayer, Bitmap, Flow)")
                Text("rememberSaveable: 사용자 입력, 스크롤 위치 등 중요한 UI 상태")
            }
        }

        // 실제 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "실제 사용 예시",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = """
// ExoPlayer는 retain으로
val player = retain {
    ExoPlayer.Builder(applicationContext).build()
}

// 사용자 입력은 rememberSaveable로
var searchQuery by rememberSaveable {
    mutableStateOf("")
}

// 임시 UI 상태는 remember로
var isExpanded by remember {
    mutableStateOf(false)
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun ComparisonRow(label: String, remember: String, retain: String, saveable: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, modifier = Modifier.weight(1.5f))
        Text(
            remember,
            modifier = Modifier.weight(1f),
            color = if (remember == "X") Color.Red else Color(0xFF2E7D32)
        )
        Text(
            retain,
            modifier = Modifier.weight(1f),
            color = if (retain == "X") Color.Red else Color(0xFF2E7D32)
        )
        Text(
            saveable,
            modifier = Modifier.weight(1f),
            color = if (saveable == "X") Color.Red else Color(0xFF2E7D32)
        )
    }
}

@Composable
fun SuccessBanner() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50)
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
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White
            )
            Column {
                Text(
                    text = "retain으로 해결!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "화면을 회전해도 상태가 유지됩니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun AttentionCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "주의사항",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF57C00)
            )

            Text(
                text = "1. applicationContext 사용 (Activity Context 참조 금지)",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "2. Process Death에서는 유지되지 않음",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "3. remember/rememberSaveable과 동일 객체에 혼용 금지",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}
