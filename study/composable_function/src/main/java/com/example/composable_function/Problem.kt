package com.example.composable_function

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Problem: @Composable 규칙을 모르면 발생하는 문제들
 *
 * 1. 일반 함수에서 Composable 호출 시 컴파일 에러
 * 2. onClick에서 Composable 직접 호출 시 컴파일 에러
 * 3. remember 없이 상태 관리 시 상태가 초기화됨
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: @Composable 규칙 위반",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "@Composable 함수의 규칙을 모르면 컴파일 에러가 발생하거나 예상대로 동작하지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 1: 일반 함수에서 Composable 호출
        ProblemSection(
            title = "문제 1: 일반 함수에서 Composable 호출",
            errorCode = """
// ❌ 컴파일 에러!
fun createGreeting(): Unit {
    Text("Hello, World!")
}

// 에러 메시지:
// "@Composable invocations can only happen
// from the context of a @Composable function"
            """.trimIndent(),
            explanation = "@Composable 함수는 @Composable 컨텍스트에서만 호출할 수 있습니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 2: onClick에서 Composable 호출
        ProblemSection(
            title = "문제 2: onClick에서 Composable 호출",
            errorCode = """
// ❌ 컴파일 에러!
Button(onClick = {
    Text("Clicked!")  // 일반 람다에서 호출 불가!
}) {
    Text("Click me")
}

// onClick은 일반 람다 () -> Unit
// Composable 람다가 아닙니다!
            """.trimIndent(),
            explanation = "onClick은 일반 람다이므로 그 안에서 Composable을 호출할 수 없습니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 3: remember 없이 상태 관리
        ProblemSection(
            title = "문제 3: remember 없이 상태 관리",
            errorCode = """
@Composable
fun BrokenCounter() {
    var count = 0  // ❌ remember 없음!

    Button(onClick = { count++ }) {
        Text("Count: ${'$'}count")
    }
}
// Recomposition마다 count가 0으로 초기화됨!
            """.trimIndent(),
            explanation = "remember 없이 선언된 변수는 매 Recomposition마다 초기화됩니다."
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 인터랙티브 데모: remember 없는 카운터
        BrokenCounterDemo()
    }
}

@Composable
private fun ProblemSection(
    title: String,
    errorCode: String,
    explanation: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 에러 코드 블록
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2D2D2D),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = errorCode,
                    modifier = Modifier.padding(12.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B6B),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 문제 시연: remember 없는 카운터
 * 버튼을 클릭해도 카운터가 증가하지 않는 것처럼 보입니다.
 */
@Composable
private fun BrokenCounterDemo() {
    // Recomposition을 강제로 발생시키기 위한 트리거
    var trigger by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "인터랙티브 데모: remember 없는 카운터",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "아래 카운터는 remember 없이 구현되어 있습니다. " +
                        "버튼을 클릭하면 내부적으로 count가 증가하지만, " +
                        "Recomposition이 발생하면 다시 0으로 초기화됩니다.",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // remember 없는 카운터 (문제 시연)
            BrokenCounter(trigger)

            Spacer(modifier = Modifier.height(16.dp))

            // Recomposition 트리거 버튼
            Button(
                onClick = { trigger++ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Recomposition 강제 발생 (trigger: $trigger)")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "위 버튼을 누르면 Recomposition이 발생하고, " +
                        "remember 없는 count는 0으로 초기화됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * ❌ 잘못된 구현: remember 없는 카운터
 * 이 코드는 의도적으로 잘못 작성되었습니다.
 */
@Composable
private fun BrokenCounter(trigger: Int) {
    // ❌ remember 없음! Recomposition마다 0으로 초기화됨
    var count = 0

    // 참고: trigger를 사용하여 이 Composable이 recompose되도록 함
    // 실제로는 trigger 값을 사용하지 않지만, Compose는 이 파라미터가
    // 변경되면 이 Composable을 다시 실행합니다.
    @Suppress("UNUSED_EXPRESSION")
    trigger

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        count++  // 증가는 되지만...
                        println("count increased to: $count")  // 로그로 확인 가능
                    }
                ) {
                    Text("+1")
                }

                Text(
                    text = "Count: $count",  // 항상 0으로 표시됨
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⚠️ 버튼을 눌러도 화면의 숫자는 변하지 않습니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "(Logcat에서 'count increased to: N'을 확인해보세요)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
