package com.example.effect_handlers_advanced

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: Effect Handlers를 잘못 사용할 때 발생하는 문제들
 *
 * 이 화면에서는 다음 문제들을 보여줍니다:
 * 1. LaunchedEffect key로 상태를 직접 사용 → 잦은 재시작
 * 2. 콜백을 직접 캡처 → 오래된 값 참조 (Stale Closure)
 * 3. derivedStateOf 없이 매번 계산 → 불필요한 Recomposition
 * 4. 디버깅 정보 없음 → 문제 원인 파악 어려움
 */
@Composable
fun ProblemScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 개요 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Effect Handlers 잘못된 사용 패턴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "이 화면에서는 Effect Handlers를 잘못 사용할 때 발생하는 문제들을 보여줍니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 1: LaunchedEffect key 문제
        Problem1_LaunchedEffectKeyIssue()

        // 문제 2: Stale Closure 문제
        Problem2_StaleClosureIssue()

        // 문제 3: 불필요한 Recomposition
        Problem3_UnnecessaryRecomposition()

        // 문제 4: 디버깅 어려움
        Problem4_DebuggingDifficulty()
    }
}

/**
 * 문제 1: LaunchedEffect key로 상태를 직접 사용
 *
 * 잘못된 패턴:
 * - LaunchedEffect(scrollIndex) 처럼 자주 변하는 값을 key로 사용
 * - 매 상태 변경마다 effect가 취소되고 재시작됨
 * - 진행 중인 작업이 중단되어 예기치 않은 동작 발생
 */
@Composable
private fun Problem1_LaunchedEffectKeyIssue() {
    var scrollIndex by remember { mutableIntStateOf(0) }
    var effectRestartCount by remember { mutableIntStateOf(0) }
    var analyticsCallCount by remember { mutableIntStateOf(0) }

    // 문제: scrollIndex를 key로 사용하면 매번 effect가 재시작됨
    // 실제로는 주석 처리 - 시연을 위해 SideEffect로 대체
    /*
    LaunchedEffect(scrollIndex) {
        // 매 scrollIndex 변경마다 이 effect가 취소되고 재시작됨!
        effectRestartCount++
        delay(100) // 분석 이벤트 전송 시뮬레이션
        analyticsCallCount++
    }
    */

    // 시연용: 키 변경 시마다 카운트 증가
    LaunchedEffect(scrollIndex) {
        effectRestartCount++
        // 만약 여기서 delay가 있었다면 취소될 수 있음
        analyticsCallCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 1: LaunchedEffect Key 오용",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
LaunchedEffect(scrollIndex) {
    // 매번 취소 후 재시작됨!
    analytics.logScroll(scrollIndex)
}
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Scroll Index: $scrollIndex")
                    Text("Effect 재시작 횟수: $effectRestartCount")
                    Text("분석 호출 횟수: $analyticsCallCount")
                }
                Column {
                    Button(
                        onClick = { scrollIndex++ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("+1")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "버튼을 빠르게 클릭하면 effect가 계속 재시작됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 2: Stale Closure (오래된 값 참조)
 *
 * 잘못된 패턴:
 * - LaunchedEffect(Unit)에서 콜백을 직접 캡처
 * - 콜백이 변경되어도 원래 콜백이 호출됨
 * - 5초 전의 메시지가 표시되는 버그
 */
@Composable
private fun Problem2_StaleClosureIssue() {
    var message by remember { mutableStateOf("초기 메시지") }
    var displayedMessage by remember { mutableStateOf("") }
    var isTimerRunning by remember { mutableStateOf(false) }
    var countdown by remember { mutableIntStateOf(0) }

    // 문제: message를 직접 캡처하면 초기값만 사용됨
    // 올바른 방식: rememberUpdatedState 사용
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            // 시작 시점의 message를 캡처 (문제!)
            val capturedMessage = message
            for (i in 3 downTo 1) {
                countdown = i
                kotlinx.coroutines.delay(1000)
            }
            countdown = 0
            // 3초 전에 캡처된 메시지 사용 (문제!)
            displayedMessage = "표시: $capturedMessage"
            isTimerRunning = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 2: Stale Closure (오래된 값 참조)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
LaunchedEffect(Unit) {
    delay(3000)
    showMessage(message) // 3초 전 값!
}
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("메시지 입력") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isTimerRunning
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { isTimerRunning = true },
                    enabled = !isTimerRunning
                ) {
                    Text(if (isTimerRunning) "대기중... ${countdown}초" else "3초 후 표시")
                }
                Text(displayedMessage)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "타이머 시작 후 메시지를 변경해보세요. 변경된 메시지가 아닌 시작 시점 메시지가 표시됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 3: derivedStateOf 없이 매번 계산
 *
 * 잘못된 패턴:
 * - 자주 변하는 상태에서 단순 조건 계산을 매번 수행
 * - 결과가 같아도 Recomposition 발생
 * - 성능 저하 및 배터리 소모
 */
@Composable
private fun Problem3_UnnecessaryRecomposition() {
    val listState = rememberLazyListState()
    var recompositionCount by remember { mutableIntStateOf(0) }

    // 문제: derivedStateOf 없이 매번 계산
    // 결과가 같아도 listState 변경마다 Recomposition 발생
    val showButton = listState.firstVisibleItemIndex > 2

    // Recomposition 카운터
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 3: 불필요한 Recomposition",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
// derivedStateOf 없이 직접 계산
val showButton = listState.firstVisibleItemIndex > 2
// → 매 스크롤마다 Recomposition!
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("firstVisibleItemIndex: ${listState.firstVisibleItemIndex}")
                    Text("showButton: $showButton")
                    Text(
                        "Recomposition 횟수: $recompositionCount",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 스크롤 가능한 리스트
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(20) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "아이템 ${index + 1}",
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "스크롤하면 Recomposition 횟수가 급격히 증가합니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 4: 디버깅 정보 없음
 *
 * 문제:
 * - Recomposition이 언제 발생하는지 알 수 없음
 * - 어떤 Composable이 같은 scope를 공유하는지 모름
 * - 성능 문제 원인 파악 어려움
 */
@Composable
private fun Problem4_DebuggingDifficulty() {
    var counter by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 4: 디버깅 정보 없음",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
// 디버깅 정보 없이는...
// - 언제 Recomposition이 발생하는지?
// - 어떤 Composable이 같은 scope인지?
// - 왜 성능이 느린지?
// 알 수 없습니다!
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("카운터: $counter")
                Button(onClick = { counter++ }) {
                    Text("+1")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "버튼을 클릭해도 어떤 부분이 Recompose되는지 로그 없이는 알 수 없습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Solution 탭에서 currentRecomposeScope를 활용한 디버깅 방법을 확인하세요!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
