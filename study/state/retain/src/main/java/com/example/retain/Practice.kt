package com.example.retain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * 연습 문제 1: 이미지 캐시 유지 (쉬움)
 *
 * 목표: remember로 구현된 이미지 캐시를 retain으로 변경하여
 * 화면 회전에도 캐시가 유지되도록 하세요.
 */
@Composable
fun Practice1_CacheScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 이미지 캐시 유지",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "아래 코드에서 remember를 retain으로 변경하여 " +
                            "화면 회전 후에도 캐시가 유지되도록 수정하세요."
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("remember { ... } 를 retain { ... } 로 변경")
                Text("retain은 직렬화 없이 객체를 유지합니다")
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFCE4EC)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Practice1_Exercise()
            }
        }

        // 정답 카드 (접이식)
        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
// 변경 전:
val cache = remember { PracticeImageCache.create() }

// 변경 후:
val cache = retain { PracticeImageCache.create() }

// 또는 simulatedRetain 사용 (학습용):
val cache = simulatedRetain("PracticeCache") {
    PracticeImageCache.create()
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

/**
 * 연습 1 실습 영역
 */
@Composable
private fun Practice1_Exercise() {
    // TODO: 아래 remember를 retain으로 변경하세요
    // 힌트: simulatedRetain("키") { ... } 사용
    val cache = remember { PracticeImageCache.create() }

    val imageUrls = listOf("url1", "url2", "url3", "url4", "url5")

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "캐시 ID: ${cache.instanceId}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = "생성 횟수: ${PracticeImageCache.creationCount}회",
            style = MaterialTheme.typography.bodyMedium,
            color = if (PracticeImageCache.creationCount > 1) Color.Red else Color.Gray,
            fontWeight = if (PracticeImageCache.creationCount > 1) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "캐시된 이미지: ${cache.getCacheSize()}개 / 로드 횟수: ${cache.loadCount}회",
            style = MaterialTheme.typography.bodyMedium
        )

        var refreshTrigger by remember { mutableIntStateOf(0) }

        Button(
            onClick = {
                imageUrls.forEach { cache.getOrLoad(it) }
                refreshTrigger++
            }
        ) {
            Text("이미지 5개 로드")
        }

        if (refreshTrigger < 0) Text("")

        if (PracticeImageCache.creationCount > 1) {
            Text(
                text = "화면 회전 시 캐시가 초기화되었습니다! retain으로 수정하세요.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        } else if (cache.getCacheSize() > 0) {
            Text(
                text = "화면을 회전해보세요. 캐시가 유지되나요?",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

// ============================================================

/**
 * 연습 문제 2: StateFlow 구독 관리 (중간)
 *
 * 목표: 네트워크 상태를 모니터링하는 StateFlow를 retain으로 관리하고
 * collectAsState로 구독하세요.
 */
@Composable
fun Practice2_FlowScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: StateFlow 구독 관리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "NetworkMonitor를 retain으로 관리하고, " +
                            "state를 collectAsState로 구독하세요. " +
                            "화면 회전 후에도 구독 카운트가 증가하지 않아야 합니다."
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. NetworkMonitor를 retain으로 생성")
                Text("2. monitor.state.collectAsState()로 구독")
                Text("3. 화면 회전 후 구독 횟수 확인")
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Practice2_Exercise()
            }
        }

        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
// NetworkMonitor를 retain으로 생성
val monitor = simulatedRetain("NetworkMonitor") {
    PracticeNetworkMonitor.create()
}

// StateFlow 구독
val networkState by monitor.state.collectAsState()

// UI에서 networkState 사용
Text("현재 상태: ${'$'}networkState")
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: remember를 retain으로 변경하세요
    val monitor = remember { PracticeNetworkMonitor.create() }

    // TODO: StateFlow 구독 추가
    // val networkState by monitor.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "모니터 ID: ${monitor.instanceId}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = "생성 횟수: ${PracticeNetworkMonitor.creationCount}회",
            style = MaterialTheme.typography.bodyMedium,
            color = if (PracticeNetworkMonitor.creationCount > 1) Color.Red else Color.Gray
        )
        Text(
            text = "현재 상태: ${monitor.currentState}",
            style = MaterialTheme.typography.titleMedium
        )

        var refreshTrigger by remember { mutableIntStateOf(0) }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                monitor.setConnected()
                refreshTrigger++
            }) {
                Text("연결")
            }
            FilledTonalButton(onClick = {
                monitor.setDisconnected()
                refreshTrigger++
            }) {
                Text("연결 해제")
            }
        }

        if (refreshTrigger < 0) Text("")

        if (PracticeNetworkMonitor.creationCount > 1) {
            Text(
                text = "화면 회전 시 모니터가 재생성되었습니다! retain으로 수정하세요.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

// ============================================================

/**
 * 연습 문제 3: 복합 매니저 객체 (어려움)
 *
 * 목표: 분석 트래커, 캐시 매니저, 로거가 통합된 AppManager를 retain으로 관리하고
 * 화면 회전에도 모든 상태가 유지되도록 하세요.
 */
@Composable
fun Practice3_ManagerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 복합 매니저 객체",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "AppManager는 분석, 캐시, 로깅 기능을 통합한 객체입니다. " +
                            "retain으로 관리하여 화면 회전에도 모든 상태를 유지하세요."
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. AppManager를 retain으로 생성")
                Text("2. 각 기능(분석, 캐시, 로그) 사용해보기")
                Text("3. 화면 회전 후 상태 유지 확인")
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Practice3_Exercise()
            }
        }

        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
// AppManager를 retain으로 생성
val manager = simulatedRetain("AppManager") {
    PracticeAppManager.create()
}

// 각 기능 사용
manager.trackEvent("button_click")
manager.cacheData("key", "value")
manager.log("User action performed")

// 모든 상태가 화면 회전에도 유지됨!
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: remember를 retain으로 변경하세요
    val manager = remember { PracticeAppManager.create() }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "매니저 ID: ${manager.instanceId}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = "생성 횟수: ${PracticeAppManager.creationCount}회",
            style = MaterialTheme.typography.bodyMedium,
            color = if (PracticeAppManager.creationCount > 1) Color.Red else Color.Gray
        )

        HorizontalDivider()

        // 분석 트래커
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("분석 트래커", fontWeight = FontWeight.Bold)
                Text("추적된 이벤트: ${manager.getEventCount()}개")
                var refreshTrigger1 by remember { mutableIntStateOf(0) }
                Button(
                    onClick = {
                        manager.trackEvent("button_click_${System.currentTimeMillis()}")
                        refreshTrigger1++
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("이벤트 추적")
                }
                if (refreshTrigger1 < 0) Text("")
            }
        }

        // 캐시 매니저
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("캐시 매니저", fontWeight = FontWeight.Bold)
                Text("캐시된 항목: ${manager.getCacheSize()}개")
                var refreshTrigger2 by remember { mutableIntStateOf(0) }
                Button(
                    onClick = {
                        manager.cacheData("key_${System.currentTimeMillis()}", "value")
                        refreshTrigger2++
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("데이터 캐시")
                }
                if (refreshTrigger2 < 0) Text("")
            }
        }

        // 로거
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("로거", fontWeight = FontWeight.Bold)
                Text("로그 항목: ${manager.getLogCount()}개")
                var refreshTrigger3 by remember { mutableIntStateOf(0) }
                Button(
                    onClick = {
                        manager.log("User action at ${System.currentTimeMillis()}")
                        refreshTrigger3++
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("로그 추가")
                }
                if (refreshTrigger3 < 0) Text("")
            }
        }

        if (PracticeAppManager.creationCount > 1) {
            Text(
                text = "화면 회전 시 모든 상태가 초기화되었습니다! retain으로 수정하세요.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

// ============================================================
// 연습용 클래스들
// ============================================================

class PracticeImageCache {
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

        fun create(): PracticeImageCache {
            creationCount++
            return PracticeImageCache()
        }
    }
}

class PracticeNetworkMonitor {
    val instanceId: String = UUID.randomUUID().toString().take(8)
    private val _state = MutableStateFlow("DISCONNECTED")
    val state: StateFlow<String> = _state.asStateFlow()

    val currentState: String get() = _state.value

    fun setConnected() {
        _state.value = "CONNECTED"
    }

    fun setDisconnected() {
        _state.value = "DISCONNECTED"
    }

    companion object {
        var creationCount: Int = 0
            private set

        fun create(): PracticeNetworkMonitor {
            creationCount++
            return PracticeNetworkMonitor()
        }
    }
}

class PracticeAppManager {
    val instanceId: String = UUID.randomUUID().toString().take(8)

    // 분석 트래커
    private val events = mutableListOf<String>()
    fun trackEvent(event: String) { events.add(event) }
    fun getEventCount(): Int = events.size

    // 캐시 매니저
    private val cache = mutableMapOf<String, Any>()
    fun cacheData(key: String, value: Any) { cache[key] = value }
    fun getCacheSize(): Int = cache.size

    // 로거
    private val logs = mutableListOf<String>()
    fun log(message: String) { logs.add(message) }
    fun getLogCount(): Int = logs.size

    companion object {
        var creationCount: Int = 0
            private set

        fun create(): PracticeAppManager {
            creationCount++
            return PracticeAppManager()
        }
    }
}
