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
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

/**
 * 문제 상황 - 기존 방식: DisposableEffect + LifecycleEventObserver
 *
 * 이 방식의 문제점:
 * 1. 보일러플레이트 코드가 많음
 * 2. Observer 해제를 잊기 쉬움
 * 3. rememberUpdatedState 미사용 시 stale closure 문제
 * 4. 코드 가독성이 떨어짐
 */

@Composable
fun ProblemScreen() {
    var showDemo by remember { mutableStateOf(true) }
    var eventHistory by remember { mutableStateOf<List<String>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem: 기존 방식의 복잡성",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "기존 방식의 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. DisposableEffect + LifecycleEventObserver 조합")
                Text("2. Observer 생성, 등록, 해제 코드 필요")
                Text("3. when 문으로 이벤트 분기 처리")
                Text("4. 실제 로직보다 설정 코드가 더 많음")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 컨트롤 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { showDemo = !showDemo }) {
                Text(if (showDemo) "데모 숨기기" else "데모 보이기")
            }
            Button(
                onClick = { eventHistory = emptyList() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("기록 초기화")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 기존 방식 데모
        if (showDemo) {
            OldStyleLifecycleDemo(
                onEvent = { event ->
                    eventHistory = eventHistory + event
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 복잡한 코드 예시 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "기존 코드 (장황함)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            ON_START -> camera.start()
            ON_STOP -> camera.stop()
            else -> {}
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

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
                    text = "Lifecycle 이벤트 기록 (최근 10개)",
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

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
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
                Text("3. 이벤트 기록에서 START/STOP 확인")
                Text("4. '데모 숨기기' 버튼으로 onDispose 확인")
            }
        }
    }
}

/**
 * 기존 방식: DisposableEffect + LifecycleEventObserver
 *
 * 이 코드의 문제점:
 * - 보일러플레이트가 많음
 * - Observer 등록/해제 패턴이 반복적
 * - 실제 로직(start/stop)보다 설정 코드가 더 많음
 */
@Composable
fun OldStyleLifecycleDemo(onEvent: (String) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var isStarted by remember { mutableStateOf(false) }
    var startCount by remember { mutableIntStateOf(0) }
    var stopCount by remember { mutableIntStateOf(0) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    // 기존 방식: 장황한 DisposableEffect 코드
    DisposableEffect(lifecycleOwner) {
        // 1. Observer 생성 (보일러플레이트)
        val observer = LifecycleEventObserver { _, event ->
            // 2. 이벤트별 분기 처리 (when 문)
            when (event) {
                Lifecycle.Event.ON_START -> {
                    isStarted = true
                    startCount++
                    onEvent("[OLD] ON_START - 시작됨 (${startCount}번째)")
                }
                Lifecycle.Event.ON_STOP -> {
                    isStarted = false
                    stopCount++
                    onEvent("[OLD] ON_STOP - 중지됨 (${stopCount}번째)")
                }
                else -> {
                    // 다른 이벤트도 처리 가능하지만 복잡해짐
                }
            }
        }

        // 3. Observer 등록 (보일러플레이트)
        lifecycleOwner.lifecycle.addObserver(observer)
        onEvent("[OLD] Observer 등록됨")

        // 4. 정리 코드 (보일러플레이트)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            onEvent("[OLD] Observer 해제됨 (onDispose)")
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
                text = "기존 방식 데모",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isStarted) "STARTED" else "STOPPED",
                style = MaterialTheme.typography.headlineMedium,
                color = if (isStarted) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("START 횟수")
                    Text(
                        text = "${startCount}회",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("STOP 횟수")
                    Text(
                        text = "${stopCount}회",
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
