package com.example.side_effect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 문제에서 사용할 외부 객체들
 */
object SharedCounter {
    var value: Int = 0

    fun reset() {
        value = 0
    }
}

object CompositionLogger {
    val logs = mutableListOf<String>()

    fun log(message: String) {
        logs.add("[${System.currentTimeMillis() % 100000}] $message")
        // 최대 20개 로그 유지
        if (logs.size > 20) {
            logs.removeAt(0)
        }
    }

    fun clear() {
        logs.clear()
    }
}

object ExternalState {
    var syncedValue: String = ""
    var lastSyncTime: Long = 0
    var syncCount: Int = 0

    fun reset() {
        syncedValue = ""
        lastSyncTime = 0
        syncCount = 0
    }
}

/**
 * 연습 문제 1: 기본 - 외부 카운터 동기화
 *
 * 요구사항:
 * - 화면에 내부 카운터가 있음
 * - SharedCounter.value에 내부 카운터 값을 동기화해야 함
 * - SideEffect를 사용하여 동기화 구현
 *
 * TODO: SideEffect를 사용해서 구현하세요!
 */
@Composable
fun Practice1_CounterSync() {
    var count by remember { mutableIntStateOf(0) }
    var recomposeCount by remember { mutableIntStateOf(0) }

    // TODO: 여기에 SideEffect를 추가하세요
    // 힌트: SideEffect { SharedCounter.value = count }

    /*
    SideEffect {
        SharedCounter.value = count
        recomposeCount++
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 외부 카운터 동기화",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: SideEffect를 사용해서\nSharedCounter와 동기화하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("내부 카운터: $count")
                Text("외부 카운터 (SharedCounter): ${SharedCounter.value}")
                Text("Recomposition 횟수: $recomposeCount")

                Spacer(modifier = Modifier.height(8.dp))

                val isSynced = count == SharedCounter.value
                Text(
                    text = if (isSynced) "동기화됨" else "동기화 필요 (SideEffect 구현 필요)",
                    color = if (isSynced)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 카운터 조작 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { count++ }) {
                Text("+1")
            }
            Button(onClick = { count += 10 }) {
                Text("+10")
            }
            OutlinedButton(onClick = {
                count = 0
                SharedCounter.reset()
            }) {
                Text("초기화")
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
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
SideEffect {
    SharedCounter.value = count
    recomposeCount++
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 연습 문제 2: 중급 - Recomposition 로깅
 *
 * 요구사항:
 * - 외부 로깅 시스템(CompositionLogger)에 Recomposition 정보를 기록
 * - 화면 이름과 현재 카운터 값을 로깅
 * - SideEffect를 사용하여 매 Recomposition마다 로그 기록
 *
 * TODO: SideEffect를 사용해서 구현하세요!
 */
@Composable
fun Practice2_Logging(screenName: String = "Practice2") {
    var counter by remember { mutableIntStateOf(0) }
    var logCount by remember { mutableIntStateOf(0) }

    // TODO: 여기에 SideEffect를 추가하세요
    // 힌트: 형식 "[$screenName] counter = $counter"

    /*
    SideEffect {
        CompositionLogger.log("[$screenName] counter = $counter")
        logCount = CompositionLogger.logs.size
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: Recomposition 로깅",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: SideEffect를 사용해서\nCompositionLogger에 로그를 기록하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("화면: $screenName")
                Text("카운터: $counter")
                Text("로그 수: $logCount")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { counter++ }) {
                Text("카운터 증가")
            }
            OutlinedButton(onClick = {
                counter = 0
                CompositionLogger.clear()
                logCount = 0
            }) {
                Text("초기화")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 로그 목록 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "로그 기록",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (CompositionLogger.logs.isEmpty()) {
                    Text(
                        text = "로그 없음 (SideEffect 구현 필요)",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    CompositionLogger.logs.takeLast(5).forEach { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.bodySmall
                        )
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
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
SideEffect {
    CompositionLogger.log(
        "[${'$'}screenName] counter = ${'$'}counter"
    )
    logCount = CompositionLogger.logs.size
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 연습 문제 3: 고급 - 조건부 외부 상태 동기화
 *
 * 요구사항:
 * - 사용자가 "동기화 활성화" 토글을 켜면 외부 상태와 동기화
 * - 토글이 꺼지면 동기화하지 않음
 * - SideEffect의 실행 여부를 조건문으로 제어
 *
 * 포인트: 조건부 SideEffect 배치의 두 가지 패턴 학습
 * 1. SideEffect 안에서 조건 체크
 * 2. 조건부로 SideEffect 컴포저블 배치 (주의 필요)
 *
 * TODO: SideEffect를 사용해서 구현하세요!
 */
@Composable
fun Practice3_ConditionalSync() {
    var inputText by remember { mutableStateOf("") }
    var syncEnabled by remember { mutableStateOf(false) }
    var syncCountDisplay by remember { mutableIntStateOf(0) }

    // TODO: 여기에 조건부 SideEffect를 추가하세요
    // 방법 1: SideEffect 안에서 조건 체크
    // 방법 2: if (syncEnabled) { SideEffect { ... } }

    /*
    // 방법 1: SideEffect 안에서 조건 체크 (권장)
    SideEffect {
        if (syncEnabled) {
            ExternalState.syncedValue = inputText
            ExternalState.lastSyncTime = System.currentTimeMillis()
            ExternalState.syncCount++
            syncCountDisplay = ExternalState.syncCount
        }
    }
    */

    /*
    // 방법 2: 조건부 SideEffect 배치
    // 주의: Composable의 조건부 배치는 Composition 구조에 영향을 줌
    if (syncEnabled) {
        SideEffect {
            ExternalState.syncedValue = inputText
            ExternalState.lastSyncTime = System.currentTimeMillis()
            ExternalState.syncCount++
            syncCountDisplay = ExternalState.syncCount
        }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: 조건부 동기화",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 토글이 켜져 있을 때만\nSideEffect로 외부 상태와 동기화하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 입력 필드
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("입력") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 동기화 토글
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("동기화 활성화")
            Switch(
                checked = syncEnabled,
                onCheckedChange = { syncEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (syncEnabled)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (syncEnabled) "동기화 활성화" else "동기화 비활성화",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("입력 값: $inputText")
                Text("동기화된 값: ${ExternalState.syncedValue}")
                Text("동기화 횟수: $syncCountDisplay")

                if (ExternalState.lastSyncTime > 0) {
                    Text("마지막 동기화: ${ExternalState.lastSyncTime % 100000}ms")
                }

                Spacer(modifier = Modifier.height(8.dp))

                val isSynced = inputText == ExternalState.syncedValue
                if (syncEnabled) {
                    Text(
                        text = if (isSynced) "동기화됨" else "동기화 필요 (SideEffect 구현 필요)",
                        color = if (isSynced)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 초기화 버튼
        TextButton(onClick = {
            inputText = ""
            syncEnabled = false
            ExternalState.reset()
            syncCountDisplay = 0
        }) {
            Text("초기화")
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
                    text = "힌트 (두 가지 방법)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 방법 1: SideEffect 안에서 조건 체크 (권장)
SideEffect {
    if (syncEnabled) {
        ExternalState.syncedValue = inputText
        ExternalState.syncCount++
    }
}

// 방법 2: 조건부 SideEffect 배치
if (syncEnabled) {
    SideEffect {
        ExternalState.syncedValue = inputText
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 추가 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "주의: 방법 2의 특성",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "조건부로 Composable을 배치하면\nComposition 구조가 변경됩니다.\n대부분의 경우 방법 1이 안전합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
