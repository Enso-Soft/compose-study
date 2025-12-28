package com.example.disposable_effect

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 문제가 있는 코드 - 정리(cleanup) 없이 리소스 등록
 *
 * 이 코드를 실행하면 다음 문제가 발생합니다:
 * 1. 메모리 누수: 등록된 콜백/리스너가 해제되지 않음
 * 2. 중복 등록: Recomposition마다 새로운 콜백 등록
 * 3. 의도치 않은 동작: 화면을 떠나도 콜백이 계속 실행됨
 */

// 가상의 이벤트 시스템 (실제 앱에서는 리스너, BroadcastReceiver 등)
object FakeEventSystem {
    private val callbacks = mutableListOf<(String) -> Unit>()

    fun registerCallback(callback: (String) -> Unit) {
        callbacks.add(callback)
        println("콜백 등록됨. 현재 등록된 콜백 수: ${callbacks.size}")
    }

    fun unregisterCallback(callback: (String) -> Unit) {
        callbacks.remove(callback)
        println("콜백 해제됨. 현재 등록된 콜백 수: ${callbacks.size}")
    }

    fun emit(event: String) {
        callbacks.forEach { it(event) }
    }

    fun getCallbackCount() = callbacks.size

    fun clear() {
        callbacks.clear()
    }
}

@Composable
fun ProblemScreen() {
    var showChild by remember { mutableStateOf(true) }
    var eventLog by remember { mutableStateOf<List<String>>(emptyList()) }
    var callbackCount by remember { mutableIntStateOf(0) }

    // 콜백 수 업데이트
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            callbackCount = FakeEventSystem.getCallbackCount()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 등록된 콜백 수 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 등록된 콜백 수: $callbackCount",
                    style = MaterialTheme.typography.titleMedium
                )
                if (callbackCount > 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚠️ 중복 등록 발생! (정상: 0 또는 1)",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 토글 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { showChild = !showChild }) {
                Text(if (showChild) "컴포넌트 숨기기" else "컴포넌트 보이기")
            }
            Button(
                onClick = { FakeEventSystem.emit("테스트 이벤트 ${System.currentTimeMillis()}") }
            ) {
                Text("이벤트 발생")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                FakeEventSystem.clear()
                eventLog = emptyList()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("초기화")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제있는 자식 컴포넌트
        if (showChild) {
            ProblematicChildComponent(
                onEvent = { event ->
                    eventLog = eventLog + event
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 설명
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
                Text("1. 컴포넌트를 숨겼다 보이면 콜백이 중복 등록됨")
                Text("2. 화면을 떠나도 콜백이 해제되지 않음")
                Text("3. '이벤트 발생' 버튼을 누르면 중복 호출됨")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 이벤트 로그
        Text("이벤트 로그 (최근 5개):", style = MaterialTheme.typography.labelMedium)
        eventLog.takeLast(5).forEach { log ->
            Text(log, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/**
 * 문제가 있는 자식 컴포넌트
 *
 * 콜백을 등록하지만 해제하지 않음!
 */
@Composable
fun ProblematicChildComponent(onEvent: (String) -> Unit) {
    var recompositionCount by remember { mutableIntStateOf(0) }

    // 문제: 콜백을 등록만 하고 해제하지 않음
    // 주석을 해제하면 문제 발생
    /*
    val callback: (String) -> Unit = { event ->
        onEvent("받은 이벤트: $event")
    }
    FakeEventSystem.registerCallback(callback)
    */

    // Recomposition 횟수 추적
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "자식 컴포넌트",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Recomposition: ${recompositionCount}회")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Problem.kt의 주석을 해제하면\n콜백 중복 등록 문제 발생!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
