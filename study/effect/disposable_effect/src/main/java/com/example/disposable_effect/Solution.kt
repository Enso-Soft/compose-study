package com.example.disposable_effect

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 올바른 코드 - DisposableEffect로 리소스 정리
 *
 * DisposableEffect를 사용하면:
 * 1. Composition 진입 시 리소스 등록
 * 2. Composition 떠날 때 onDispose에서 리소스 해제
 * 3. key가 변경되면 기존 onDispose 실행 후 재등록
 */

// 가상의 이벤트 시스템 (Solution용 별도 인스턴스)
object SolutionEventSystem {
    private val callbacks = mutableListOf<(String) -> Unit>()

    fun registerCallback(callback: (String) -> Unit) {
        callbacks.add(callback)
        println("[Solution] 콜백 등록됨. 현재 등록된 콜백 수: ${callbacks.size}")
    }

    fun unregisterCallback(callback: (String) -> Unit) {
        callbacks.remove(callback)
        println("[Solution] 콜백 해제됨. 현재 등록된 콜백 수: ${callbacks.size}")
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
fun SolutionScreen() {
    var showChild by remember { mutableStateOf(true) }
    var eventLog by remember { mutableStateOf<List<String>>(emptyList()) }
    var callbackCount by remember { mutableIntStateOf(0) }

    // 콜백 수 업데이트
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            callbackCount = SolutionEventSystem.getCallbackCount()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 등록된 콜백 수 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 등록된 콜백 수: $callbackCount",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (showChild) "✅ 컴포넌트 표시 중 (콜백 1개)" else "✅ 컴포넌트 숨김 (콜백 0개)",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 토글 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { showChild = !showChild }) {
                Text(if (showChild) "컴포넌트 숨기기" else "컴포넌트 보이기")
            }
            Button(
                onClick = { SolutionEventSystem.emit("테스트 이벤트 ${System.currentTimeMillis()}") }
            ) {
                Text("이벤트 발생")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                SolutionEventSystem.clear()
                eventLog = emptyList()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("초기화")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 올바른 자식 컴포넌트
        if (showChild) {
            CorrectChildComponent(
                onEvent = { event ->
                    eventLog = eventLog + event
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- 컴포넌트 숨기면 콜백 자동 해제 (0개)")
                Text("- 다시 보이면 새로 등록 (1개)")
                Text("- 중복 등록 문제 없음!")
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
 * 올바른 자식 컴포넌트
 *
 * DisposableEffect를 사용하여 콜백 등록/해제 관리
 */
@Composable
fun CorrectChildComponent(onEvent: (String) -> Unit) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    var registrationCount by remember { mutableIntStateOf(0) }
    var disposalCount by remember { mutableIntStateOf(0) }

    // DisposableEffect: 콜백 등록 및 해제
    DisposableEffect(Unit) {
        registrationCount++

        val callback: (String) -> Unit = { event ->
            onEvent("받은 이벤트: $event")
        }
        SolutionEventSystem.registerCallback(callback)

        onDispose {
            disposalCount++
            SolutionEventSystem.unregisterCallback(callback)
            println("onDispose 호출됨 - 콜백 해제!")
        }
    }

    // Recomposition 횟수 추적
    SideEffect {
        recompositionCount++
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
                text = "자식 컴포넌트 (DisposableEffect 사용)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Recomposition: ${recompositionCount}회")
            Text("등록 횟수: ${registrationCount}회")
            Text("해제 횟수: ${disposalCount}회")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "✅ 컴포넌트 숨기면 onDispose 호출!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
