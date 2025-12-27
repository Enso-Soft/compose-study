package com.example.effect_handlers_advanced

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * Solution: Effect Handlers 올바른 사용법
 *
 * 이 화면에서는 다음 해결책들을 보여줍니다:
 * 1. snapshotFlow: State → Flow 변환으로 안전한 side effect 처리
 * 2. rememberUpdatedState: 장기 실행 effect에서 최신 값 유지
 * 3. derivedStateOf: 불필요한 Recomposition 방지
 * 4. currentRecomposeScope: Recomposition 디버깅
 */
@Composable
fun SolutionScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 개요 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Effect Handlers 올바른 사용 패턴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "snapshotFlow, rememberUpdatedState, derivedStateOf, currentRecomposeScope를 올바르게 활용하는 방법을 알아봅니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 해결책 1: snapshotFlow
        Solution1_SnapshotFlow()

        // 해결책 2: rememberUpdatedState
        Solution2_RememberUpdatedState()

        // 해결책 3: derivedStateOf
        Solution3_DerivedStateOf()

        // 해결책 4: currentRecomposeScope
        Solution4_CurrentRecomposeScope()

        // 보너스: 복합 패턴
        Solution5_CombinedPattern()
    }
}

/**
 * 해결책 1: snapshotFlow로 상태 변경 감지
 *
 * snapshotFlow를 사용하면:
 * - LaunchedEffect가 재시작되지 않음
 * - 상태 변경이 Flow로 안전하게 변환
 * - Flow 연산자(distinctUntilChanged, debounce 등) 활용 가능
 */
@OptIn(FlowPreview::class)
@Composable
private fun Solution1_SnapshotFlow() {
    var scrollIndex by remember { mutableIntStateOf(0) }
    var analyticsEvents by remember { mutableStateOf(listOf<String>()) }

    // 해결책: snapshotFlow 사용!
    // LaunchedEffect(Unit)으로 한 번만 시작, 재시작 없음
    LaunchedEffect(Unit) {
        snapshotFlow { scrollIndex }
            .distinctUntilChanged()  // 중복 방지
            .debounce(100)           // 100ms 디바운스
            .collect { index ->
                // 분석 이벤트 시뮬레이션
                val event = "스크롤 위치: $index (${System.currentTimeMillis() % 10000}ms)"
                analyticsEvents = (analyticsEvents + event).takeLast(5)
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
                text = "해결책 1: snapshotFlow",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
LaunchedEffect(Unit) {
    snapshotFlow { scrollIndex }
        .distinctUntilChanged()
        .debounce(100)
        .collect { index ->
            analytics.log(index)
        }
}
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
                Text("Scroll Index: $scrollIndex")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { scrollIndex++ }) { Text("+1") }
                    Button(onClick = { scrollIndex += 5 }) { Text("+5") }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "분석 이벤트 로그:",
                style = MaterialTheme.typography.labelMedium
            )
            analyticsEvents.forEach { event ->
                Text(
                    text = event,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Effect가 재시작되지 않고 debounce가 적용됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 해결책 2: rememberUpdatedState로 최신 값 유지
 *
 * rememberUpdatedState를 사용하면:
 * - 장기 실행 effect가 재시작되지 않음
 * - 항상 최신 콜백/값을 참조
 * - 타이머, 애니메이션 등에 필수
 */
@Composable
private fun Solution2_RememberUpdatedState() {
    var message by remember { mutableStateOf("초기 메시지") }
    var displayedMessage by remember { mutableStateOf("") }
    var isTimerRunning by remember { mutableStateOf(false) }
    var countdown by remember { mutableIntStateOf(0) }

    // 해결책: rememberUpdatedState 사용!
    val currentMessage by rememberUpdatedState(message)

    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            for (i in 3 downTo 1) {
                countdown = i
                kotlinx.coroutines.delay(1000)
            }
            countdown = 0
            // currentMessage는 항상 최신값!
            displayedMessage = "표시: $currentMessage"
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
                text = "해결책 2: rememberUpdatedState",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
val currentMessage by rememberUpdatedState(message)

LaunchedEffect(Unit) {
    delay(3000)
    showMessage(currentMessage) // 항상 최신값!
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
                text = "타이머 중 메시지를 변경하면 변경된 최신 메시지가 표시됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 해결책 3: derivedStateOf로 불필요한 Recomposition 방지
 *
 * derivedStateOf를 사용하면:
 * - 결과가 변경될 때만 Recomposition 트리거
 * - distinctUntilChanged()와 유사한 효과
 * - 성능 최적화 및 배터리 절약
 */
@Composable
private fun Solution3_DerivedStateOf() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var recompositionCount by remember { mutableIntStateOf(0) }
    var buttonStateChangeCount by remember { mutableIntStateOf(0) }

    // 해결책: derivedStateOf 사용!
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2
        }
    }

    // showButton 변경 횟수 추적
    LaunchedEffect(showButton) {
        buttonStateChangeCount++
    }

    // Recomposition 횟수 추적
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
                text = "해결책 3: derivedStateOf",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
val showButton by remember {
    derivedStateOf {
        listState.firstVisibleItemIndex > 2
    }
}
// → showButton 결과가 변경될 때만 Recomposition!
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
                    Text("showButton 변경 횟수: $buttonStateChangeCount")
                    Text(
                        "Recomposition 횟수: $recompositionCount",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
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

                // "맨 위로" 버튼
                if (showButton) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "맨 위로",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "스크롤해도 Recomposition이 최소화됩니다. Problem 탭과 비교해보세요!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 해결책 4: currentRecomposeScope로 디버깅
 *
 * currentRecomposeScope를 활용하면:
 * - Recomposition 발생 시점 파악
 * - 같은 scope를 공유하는 Composable 식별
 * - 성능 문제 원인 분석
 */
@Composable
private fun Solution4_CurrentRecomposeScope() {
    var counter by remember { mutableIntStateOf(0) }
    var logMessages by remember { mutableStateOf(listOf<String>()) }

    // 커스텀 로깅 함수
    @Composable
    fun LogRecomposition(tag: String) {
        val scope = currentRecomposeScope
        SideEffect {
            val message = "$tag: ${scope.hashCode().toString(16)}"
            Log.d("Recomposition", message)
            logMessages = (logMessages + message).takeLast(6)
        }
    }

    // 최상위 로깅
    LogRecomposition("Solution4_Root")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // Card 내부 로깅
        LogRecomposition("Solution4_Card")

        Column(modifier = Modifier.padding(16.dp)) {
            // Column 내부 로깅
            LogRecomposition("Solution4_Column")

            Text(
                text = "해결책 4: currentRecomposeScope 디버깅",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
@Composable
inline fun LogRecomposition(tag: String) {
    val scope = currentRecomposeScope
    SideEffect {
        Log.d("Recomposition",
            "${'$'}tag: ${'$'}scope")
    }
}
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
                    // Button 내부 로깅
                    LogRecomposition("Solution4_Button")
                    Text("+1")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recomposition 로그 (Logcat + 화면):",
                style = MaterialTheme.typography.labelMedium
            )
            logMessages.forEach { log ->
                Text(
                    text = log,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "같은 해시코드 = 같은 Recomposition scope. 버튼 클릭 시 어떤 부분이 recompose되는지 확인하세요!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 해결책 5: 복합 패턴 - derivedStateOf + snapshotFlow 조합
 *
 * 실제 앱에서는 여러 패턴을 조합하여 사용합니다:
 * - UI 상태 파생: derivedStateOf
 * - Side Effect: snapshotFlow
 */
@OptIn(FlowPreview::class)
@Composable
private fun Solution5_CombinedPattern() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var analyticsLog by remember { mutableStateOf(listOf<String>()) }

    // UI 상태 파생: derivedStateOf 사용
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 3 }
    }

    // Side Effect: snapshotFlow 사용
    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .filter { it > 0 }  // 첫 번째 아이템 제외
            .collect { index ->
                val section = (index / 5) + 1
                val log = "섹션 $section 진입 (인덱스: $index)"
                analyticsLog = (analyticsLog + log).takeLast(3)
            }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "복합 패턴: derivedStateOf + snapshotFlow",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
// UI 상태 → derivedStateOf
val showFab by remember {
    derivedStateOf { index > 3 }
}

// Side Effect → snapshotFlow
LaunchedEffect(Unit) {
    snapshotFlow { index }
        .collect { analytics.log(it) }
}
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "분석 로그:",
                style = MaterialTheme.typography.labelMedium
            )
            analyticsLog.forEach { log ->
                Text(
                    text = log,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(30) { index ->
                        val section = (index / 5) + 1
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (index % 5 == 0)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Text(
                                text = if (index % 5 == 0) "--- 섹션 $section ---" else "아이템 ${index + 1}",
                                modifier = Modifier.padding(12.dp),
                                fontWeight = if (index % 5 == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                if (showFab) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "맨 위로",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "스크롤하면 FAB는 derivedStateOf로, 분석 로그는 snapshotFlow로 처리됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
