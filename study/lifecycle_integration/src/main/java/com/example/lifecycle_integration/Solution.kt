package com.example.lifecycle_integration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.currentStateAsState
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

/**
 * 해결책 - 새로운 Lifecycle API (2.7.0+)
 *
 * 새 API의 장점:
 * 1. 보일러플레이트 대폭 감소
 * 2. 정리 로직이 구조적으로 강제됨
 * 3. 의도가 명확한 함수명
 * 4. key 변경 시 자동 재시작
 */

@Composable
fun SolutionScreen() {
    var eventHistory by remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution: 새로운 Lifecycle API",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 장점 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "새 API의 장점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. LifecycleStartEffect - START/STOP 쌍 처리")
                Text("2. LifecycleResumeEffect - RESUME/PAUSE 쌍 처리")
                Text("3. LifecycleEventEffect - 단일 이벤트 처리")
                Text("4. currentStateAsState - 상태 기반 조건부 렌더링")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 이벤트 기록 초기화 버튼
        Button(
            onClick = { eventHistory = emptyList() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("기록 초기화")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. LifecycleStartEffect 데모
        LifecycleStartEffectDemo(
            onEvent = { eventHistory = eventHistory + it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. LifecycleResumeEffect 데모
        LifecycleResumeEffectDemo(
            onEvent = { eventHistory = eventHistory + it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. LifecycleEventEffect 데모
        LifecycleEventEffectDemo(
            onEvent = { eventHistory = eventHistory + it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. currentStateAsState 데모
        CurrentStateAsStateDemo()

        Spacer(modifier = Modifier.height(16.dp))

        // 이벤트 기록
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "이벤트 기록 (최근 10개)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (eventHistory.isEmpty()) {
                    Text("아직 이벤트가 없습니다.")
                } else {
                    eventHistory.takeLast(10).forEach { event ->
                        Text(event, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 비교 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "새 코드 (간결함)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
LifecycleStartEffect(Unit) {
    camera.start()
    onStopOrDispose {
        camera.stop()
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 1. LifecycleStartEffect 데모
 *
 * ON_START에서 시작, ON_STOP에서 정리
 */
@Composable
fun LifecycleStartEffectDemo(onEvent: (String) -> Unit) {
    var isStarted by remember { mutableStateOf(false) }
    var startCount by remember { mutableIntStateOf(0) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    // 새 API: 간결한 LifecycleStartEffect
    LifecycleStartEffect(Unit) {
        isStarted = true
        startCount++
        onEvent("[StartEffect] ON_START - 시작됨")

        onStopOrDispose {
            isStarted = false
            onEvent("[StartEffect] ON_STOP/Dispose - 중지됨")
        }
    }

    // 시작됐을 때만 타이머 실행
    LaunchedEffect(isStarted) {
        if (isStarted) {
            while (true) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isStarted) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "1. LifecycleStartEffect",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "START/STOP 쌍",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isStarted) "STARTED" else "STOPPED")
                    Text(
                        text = "${startCount}번",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("활성 시간")
                    Text(
                        text = "${elapsedSeconds}초",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

/**
 * 2. LifecycleResumeEffect 데모
 *
 * ON_RESUME에서 시작, ON_PAUSE에서 정리
 * 더 정밀한 "화면이 보이는" 시간 측정
 */
@Composable
fun LifecycleResumeEffectDemo(onEvent: (String) -> Unit) {
    var isResumed by remember { mutableStateOf(false) }
    var resumeCount by remember { mutableIntStateOf(0) }
    var totalTimeMs by remember { mutableLongStateOf(0L) }
    var startTimeMs by remember { mutableLongStateOf(0L) }

    // 새 API: LifecycleResumeEffect
    LifecycleResumeEffect(Unit) {
        isResumed = true
        resumeCount++
        startTimeMs = System.currentTimeMillis()
        onEvent("[ResumeEffect] ON_RESUME - 포커스 획득")

        onPauseOrDispose {
            isResumed = false
            totalTimeMs += System.currentTimeMillis() - startTimeMs
            onEvent("[ResumeEffect] ON_PAUSE/Dispose - 포커스 상실")
        }
    }

    // 현재 세션 시간 표시용
    var currentSessionMs by remember { mutableLongStateOf(0L) }
    LaunchedEffect(isResumed) {
        if (isResumed) {
            while (true) {
                delay(100)
                currentSessionMs = System.currentTimeMillis() - startTimeMs
            }
        } else {
            currentSessionMs = 0L
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isResumed) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "2. LifecycleResumeEffect",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "RESUME/PAUSE 쌍 (더 정밀)",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isResumed) "RESUMED" else "PAUSED")
                    Text(
                        text = "${resumeCount}번",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("현재 세션")
                    Text(
                        text = "${currentSessionMs / 1000}.${(currentSessionMs % 1000) / 100}초",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("누적 시간")
                    Text(
                        text = "${(totalTimeMs + currentSessionMs) / 1000}초",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

/**
 * 3. LifecycleEventEffect 데모
 *
 * 단일 이벤트에만 반응 (정리 불필요)
 * 분석 로그 전송 등에 적합
 */
@Composable
fun LifecycleEventEffectDemo(onEvent: (String) -> Unit) {
    var createCount by remember { mutableIntStateOf(0) }
    var resumeCount by remember { mutableIntStateOf(0) }

    // ON_CREATE 이벤트
    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        createCount++
        onEvent("[EventEffect] ON_CREATE - 생성됨")
    }

    // ON_RESUME 이벤트
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        resumeCount++
        onEvent("[EventEffect] ON_RESUME - 화면 분석 로그 전송")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "3. LifecycleEventEffect",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "단일 이벤트 (정리 불필요)",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ON_CREATE")
                    Text(
                        text = "${createCount}번",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ON_RESUME")
                    Text(
                        text = "${resumeCount}번",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

/**
 * 4. currentStateAsState 데모
 *
 * Lifecycle 상태를 State로 관찰
 * 상태 기반 조건부 렌더링에 적합
 */
@Composable
fun CurrentStateAsStateDemo() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                lifecycleState.isAtLeast(Lifecycle.State.RESUMED) ->
                    MaterialTheme.colorScheme.primaryContainer
                lifecycleState.isAtLeast(Lifecycle.State.STARTED) ->
                    MaterialTheme.colorScheme.tertiaryContainer
                else ->
                    MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "4. currentStateAsState",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "상태 기반 조건부 렌더링",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "현재 상태: $lifecycleState",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 상태에 따른 조건부 렌더링
            when {
                lifecycleState.isAtLeast(Lifecycle.State.RESUMED) -> {
                    Text(
                        text = "화면이 완전히 보입니다!",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                lifecycleState.isAtLeast(Lifecycle.State.STARTED) -> {
                    Text(
                        text = "화면이 부분적으로 보입니다.",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                else -> {
                    Text(
                        text = "화면이 보이지 않습니다.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
