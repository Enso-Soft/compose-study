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
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.currentStateAsState
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * 연습 문제 1: 화면 체류 시간 측정기
 *
 * 목표: LifecycleResumeEffect를 사용하여 정확한 화면 체류 시간을 측정합니다.
 *
 * 요구사항:
 * 1. ON_RESUME에서 시간 측정 시작
 * 2. ON_PAUSE에서 시간 측정 중지 및 누적
 * 3. 현재 세션 시간과 총 누적 시간 표시
 * 4. 리셋 버튼으로 누적 시간 초기화
 *
 * 힌트: System.currentTimeMillis()를 사용하세요
 */
@Composable
fun Practice1_ScreenTimeTracker() {
    var totalTimeMs by remember { mutableLongStateOf(0L) }
    var sessionStartMs by remember { mutableLongStateOf(0L) }
    var isResumed by remember { mutableStateOf(false) }
    var currentSessionMs by remember { mutableLongStateOf(0L) }

    // TODO: LifecycleResumeEffect를 사용하여 체류 시간을 측정하세요
    // 힌트:
    // - onPauseOrDispose에서 totalTimeMs에 경과 시간을 더하세요
    // - sessionStartMs에 현재 시간을 저장하세요

    // === 정답 ===
    LifecycleResumeEffect(Unit) {
        isResumed = true
        sessionStartMs = System.currentTimeMillis()

        onPauseOrDispose {
            isResumed = false
            totalTimeMs += System.currentTimeMillis() - sessionStartMs
        }
    }
    // === 정답 끝 ===

    // 현재 세션 시간 업데이트
    LaunchedEffect(isResumed) {
        if (isResumed) {
            while (true) {
                delay(100)
                currentSessionMs = System.currentTimeMillis() - sessionStartMs
            }
        } else {
            currentSessionMs = 0L
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 화면 체류 시간 측정기",
            style = MaterialTheme.typography.headlineSmall
        )

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
                Text("1. LifecycleResumeEffect(Unit) { } 사용")
                Text("2. 내부에서 isResumed = true, sessionStartMs 설정")
                Text("3. onPauseOrDispose에서 totalTimeMs 누적")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 결과 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isResumed) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isResumed) "측정 중..." else "일시정지",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("현재 세션")
                        Text(
                            text = formatTime(currentSessionMs),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("총 누적 시간")
                        Text(
                            text = formatTime(totalTimeMs + currentSessionMs),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                totalTimeMs = 0L
                currentSessionMs = 0L
                sessionStartMs = System.currentTimeMillis()
            }
        ) {
            Text("시간 초기화")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "테스트 방법",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 앱을 백그라운드로 보내기 (홈 버튼)")
                Text("2. 다시 앱으로 돌아오기")
                Text("3. 누적 시간이 유지되는지 확인")
            }
        }
    }
}

/**
 * 연습 문제 2: 센서 시뮬레이터
 *
 * 목표: LifecycleStartEffect를 사용하여 센서 연결/해제를 관리합니다.
 *
 * 요구사항:
 * 1. ON_START에서 센서 "연결" (isConnected = true)
 * 2. ON_STOP에서 센서 "해제" (isConnected = false)
 * 3. 연결 상태에서만 센서 데이터 업데이트 시뮬레이션
 * 4. 연결/해제 횟수 표시
 */
@Composable
fun Practice2_SensorSimulator() {
    var isConnected by remember { mutableStateOf(false) }
    var sensorValue by remember { mutableIntStateOf(0) }
    var connectCount by remember { mutableIntStateOf(0) }
    var disconnectCount by remember { mutableIntStateOf(0) }
    var updateCount by remember { mutableIntStateOf(0) }

    // TODO: LifecycleStartEffect를 사용하여 센서 연결을 관리하세요
    // 힌트:
    // - onStopOrDispose에서 isConnected = false
    // - connectCount, disconnectCount 업데이트

    // === 정답 ===
    LifecycleStartEffect(Unit) {
        isConnected = true
        connectCount++

        onStopOrDispose {
            isConnected = false
            disconnectCount++
        }
    }
    // === 정답 끝 ===

    // 센서 데이터 시뮬레이션 (연결됐을 때만)
    LaunchedEffect(isConnected) {
        if (isConnected) {
            while (true) {
                delay(500)
                sensorValue = Random.nextInt(0, 100)
                updateCount++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: 센서 시뮬레이터",
            style = MaterialTheme.typography.headlineSmall
        )

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
                Text("1. LifecycleStartEffect(Unit) { } 사용")
                Text("2. 내부에서 isConnected = true, connectCount++")
                Text("3. onStopOrDispose에서 disconnectCount++")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 센서 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isConnected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isConnected) "센서 연결됨" else "센서 해제됨",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isConnected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 센서 값 게이지
                if (isConnected) {
                    LinearProgressIndicator(
                        progress = { sensorValue / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "센서 값: $sensorValue",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("연결 횟수")
                        Text(
                            text = "${connectCount}회",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("해제 횟수")
                        Text(
                            text = "${disconnectCount}회",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("업데이트")
                        Text(
                            text = "${updateCount}회",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "테스트 방법",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 앱을 백그라운드로 보내면 센서 해제")
                Text("2. 다시 앱으로 돌아오면 센서 재연결")
                Text("3. 연결/해제 횟수 확인")
            }
        }
    }
}

/**
 * 연습 문제 3: 상태 기반 UI 최적화
 *
 * 목표: currentStateAsState를 사용하여 Lifecycle 상태에 따른 조건부 렌더링
 *
 * 요구사항:
 * 1. 현재 Lifecycle 상태를 텍스트로 표시
 * 2. RESUMED 상태에서만 애니메이션(카운터) 실행
 * 3. STARTED 상태에서는 정적 컨텐츠 표시
 * 4. 상태 변화 히스토리 로그 표시
 */
@Composable
fun Practice3_StateBasedUI() {
    val lifecycleOwner = LocalLifecycleOwner.current

    // TODO: currentStateAsState()를 사용하여 현재 상태를 관찰하세요
    // 힌트: val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()

    // === 정답 ===
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()
    // === 정답 끝 ===

    var animationCounter by remember { mutableIntStateOf(0) }
    var stateHistory by remember { mutableStateOf<List<String>>(emptyList()) }

    // RESUMED 상태에서만 애니메이션 실행
    LaunchedEffect(lifecycleState) {
        stateHistory = stateHistory + "상태 변경: $lifecycleState"
        if (lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {
            while (true) {
                delay(100)
                animationCounter++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: 상태 기반 UI",
            style = MaterialTheme.typography.headlineSmall
        )

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
                Text("1. lifecycle.currentStateAsState() 사용")
                Text("2. lifecycleState.isAtLeast(State.RESUMED) 조건 활용")
                Text("3. 상태에 따라 다른 UI 표시")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 현재 상태 표시
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
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "현재 상태",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = lifecycleState.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 상태에 따른 조건부 렌더링
                when {
                    lifecycleState.isAtLeast(Lifecycle.State.RESUMED) -> {
                        Text("애니메이션 실행 중!")
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (animationCounter % 100) / 100f },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = "Counter: $animationCounter",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    lifecycleState.isAtLeast(Lifecycle.State.STARTED) -> {
                        Text(
                            text = "정적 컨텐츠 (애니메이션 중지)",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "마지막 Counter: $animationCounter",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    else -> {
                        Text(
                            text = "화면이 보이지 않음",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 히스토리
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
                        text = "상태 변화 기록",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = { stateHistory = emptyList() }) {
                        Text("초기화")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (stateHistory.isEmpty()) {
                    Text("아직 기록이 없습니다.")
                } else {
                    stateHistory.takeLast(10).forEach { log ->
                        Text(log, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "테스트 방법",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 앱을 백그라운드로 보내면 애니메이션 중지")
                Text("2. 다시 앱으로 돌아오면 애니메이션 재개")
                Text("3. 상태 변화 기록 확인")
            }
        }
    }
}

// 시간 포맷팅 유틸리티
private fun formatTime(ms: Long): String {
    val seconds = ms / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val tenths = (ms % 1000) / 100
    return if (minutes > 0) {
        "${minutes}분 ${remainingSeconds}.${tenths}초"
    } else {
        "${remainingSeconds}.${tenths}초"
    }
}
