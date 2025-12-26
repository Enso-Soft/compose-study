package com.example.remember_coroutine_scope

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*

/**
 * 문제가 있는 코드 - 이벤트 핸들러에서 코루틴 실행 시 문제점
 *
 * 이 화면에서 다루는 문제들:
 * 1. onClick에서 suspend 함수를 직접 호출할 수 없음
 * 2. GlobalScope 사용 시 메모리 누수 발생
 * 3. 직접 생성한 CoroutineScope는 자동 취소되지 않음
 */
@Composable
fun ProblemScreen() {
    var countdown by remember { mutableIntStateOf(10) }
    var isRunning by remember { mutableStateOf(false) }
    var showComponent by remember { mutableStateOf(true) }
    var leakWarning by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 1: onClick에서 suspend 함수 직접 호출 불가
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 1: suspend 함수 직접 호출 불가",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Button(onClick = {
    delay(1000)  // 컴파일 에러!
    // onClick은 suspend 함수가 아님
})
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 2: GlobalScope 사용 시연
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 2: GlobalScope 사용 시 메모리 누수",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
GlobalScope.launch {
    // 화면을 떠나도 계속 실행됨!
    // Composable 생명주기와 무관
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 위험한 코드 시연 (GlobalScope 사용)
        if (showComponent) {
            DangerousCountdownComponent(
                countdown = countdown,
                isRunning = isRunning,
                onStart = {
                    // 위험! GlobalScope 사용 - 화면 이탈 후에도 계속 실행
                    // 실제 앱에서는 절대 이렇게 하면 안 됩니다!
                    isRunning = true
                    GlobalScope.launch {
                        while (countdown > 0) {
                            delay(1000)
                            countdown--
                            // 화면이 사라져도 이 코루틴은 계속 실행됨
                        }
                        isRunning = false
                        leakWarning = "GlobalScope 코루틴이 끝까지 실행됨! (메모리 누수)"
                    }
                },
                onReset = {
                    countdown = 10
                    isRunning = false
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 컴포넌트 토글 버튼 (메모리 누수 시연용)
        Button(
            onClick = { showComponent = !showComponent },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(if (showComponent) "컴포넌트 제거 (화면 이탈 시뮬레이션)" else "컴포넌트 다시 표시")
        }

        if (leakWarning.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = leakWarning,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
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
                Text("1. GlobalScope는 앱 전체 생명주기를 가짐")
                Text("2. Composable이 제거되어도 코루틴이 계속 실행됨")
                Text("3. 더 이상 필요 없는 작업에 리소스 낭비")
                Text("4. 상태 업데이트 시 크래시 가능성")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "해결책: rememberCoroutineScope 사용!",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun DangerousCountdownComponent(
    countdown: Int,
    isRunning: Boolean,
    onStart: () -> Unit,
    onReset: () -> Unit
) {
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
                text = "카운트다운 (GlobalScope 사용)",
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
                    Text("시작 (위험!)")
                }

                Button(onClick = onReset) {
                    Text("리셋")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "시작 후 '컴포넌트 제거' 버튼을 눌러보세요",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
