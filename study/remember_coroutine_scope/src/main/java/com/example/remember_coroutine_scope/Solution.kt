package com.example.remember_coroutine_scope

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 올바른 코드 - rememberCoroutineScope 사용
 *
 * rememberCoroutineScope를 사용하면:
 * 1. Composable 생명주기에 바인딩된 스코프 획득
 * 2. 이벤트 핸들러에서 안전하게 코루틴 실행
 * 3. Composable 제거 시 자동으로 코루틴 취소
 */
@Composable
fun SolutionScreen() {
    val scope = rememberCoroutineScope()  // 핵심! Composition에 바인딩된 스코프

    var countdown by remember { mutableIntStateOf(10) }
    var isRunning by remember { mutableStateOf(false) }
    var showComponent by remember { mutableStateOf(true) }
    var statusMessage by remember { mutableStateOf("") }
    var recompositionCount by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적
    SideEffect {
        recompositionCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recomposition 횟수: $recompositionCount")
                Text("컴포넌트 표시: $showComponent")
                Text("카운트다운 상태: ${if (isRunning) "실행 중" else "대기"}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 안전한 카운트다운 컴포넌트
        if (showComponent) {
            SafeCountdownComponent(
                countdown = countdown,
                isRunning = isRunning,
                onStart = {
                    // rememberCoroutineScope 사용 - 안전!
                    isRunning = true
                    statusMessage = "카운트다운 시작됨"
                    scope.launch {
                        while (countdown > 0) {
                            delay(1000)
                            countdown--
                        }
                        isRunning = false
                        statusMessage = "카운트다운 완료!"
                    }
                },
                onReset = {
                    countdown = 10
                    isRunning = false
                    statusMessage = "리셋됨"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 컴포넌트 토글 버튼
        Button(
            onClick = {
                showComponent = !showComponent
                if (!showComponent) {
                    statusMessage = "컴포넌트 제거됨 - 코루틴도 자동 취소!"
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(if (showComponent) "컴포넌트 제거" else "컴포넌트 다시 표시")
        }

        if (statusMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = statusMessage,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 핵심 포인트 설명
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
                Text("1. rememberCoroutineScope()로 스코프 획득")
                Text("2. onClick 내에서 scope.launch { } 사용")
                Text("3. 컴포넌트 제거 시 코루틴 자동 취소")
                Text("4. 메모리 누수 없음!")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "'컴포넌트 제거' 버튼으로 확인해보세요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "사용된 코드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
    }
})
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SafeCountdownComponent(
    countdown: Int,
    isRunning: Boolean,
    onStart: () -> Unit,
    onReset: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "카운트다운 (rememberCoroutineScope 사용)",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$countdown",
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onStart,
                    enabled = !isRunning && countdown > 0
                ) {
                    Text("시작 (안전!)")
                }

                Button(onClick = onReset) {
                    Text("리셋")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "시작 후 '컴포넌트 제거'를 눌러도 안전합니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
