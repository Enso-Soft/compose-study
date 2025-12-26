package com.example.composable_function

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Solution: @Composable 함수 올바르게 사용하기
 *
 * 1. @Composable 어노테이션으로 Composable 함수 선언
 * 2. onClick에서는 상태 변경, UI는 Compose가 자동 업데이트
 * 3. remember로 상태를 Recomposition 간에 보존
 */

@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 해결책 소개
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: @Composable 올바르게 사용하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "세 가지 핵심 규칙을 이해하면 Composable 함수를 자유롭게 사용할 수 있습니다!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 해결책 1: @Composable 어노테이션
        ComposableAnnotationSection()

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 2: 상태 변경과 UI 업데이트 분리
        StateAndRecompositionSection()

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 3: remember로 상태 보존
        RememberSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Recomposition 시각화
        RecompositionVisualization()
    }
}

@Composable
private fun ComposableAnnotationSection() {
    SolutionCard(
        title = "1. @Composable 어노테이션 사용",
        explanation = "UI를 선언하는 함수에는 @Composable 어노테이션을 붙입니다"
    ) {
        CodeBlock(
            code = """
// ✅ 올바른 Composable 함수
@Composable
fun Greeting(name: String) {
    Text("Hello, ${'$'}name!")
}

// ✅ 다른 Composable 내에서 호출
@Composable
fun WelcomeScreen() {
    Column {
        Greeting("Android")
        Greeting("Compose")
    }
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "직접 확인:",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 실제 Composable 호출 예제
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                SimpleGreeting("Android")
                SimpleGreeting("Compose")
            }
        }
    }
}

// ✅ 올바른 Composable 함수
@Composable
private fun SimpleGreeting(name: String) {
    Text(
        text = "Hello, $name!",
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun StateAndRecompositionSection() {
    var count by remember { mutableIntStateOf(0) }

    SolutionCard(
        title = "2. 상태 변경 → 자동 UI 업데이트",
        explanation = "onClick에서는 상태만 변경하고, UI 업데이트는 Compose가 처리합니다"
    ) {
        CodeBlock(
            code = """
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Button(
        onClick = { count++ }  // 상태만 변경!
    ) {
        Text("Count: ${'$'}count")  // 자동으로 업데이트됨
    }
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "직접 해보기:",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { count++ }  // 상태만 변경
            ) {
                Text("+1")
            }

            Text(
                text = "Count: $count",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = { count = 0 }
            ) {
                Text("리셋")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "onClick에서 count++만 하면 Compose가 자동으로 Text를 업데이트합니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun RememberSection() {
    var correctCount by remember { mutableIntStateOf(0) }
    var trigger by remember { mutableIntStateOf(0) }

    SolutionCard(
        title = "3. remember로 상태 보존",
        explanation = "remember { }로 감싸면 Recomposition 간에 값이 보존됩니다"
    ) {
        CodeBlock(
            code = """
// ✅ 올바른 상태 관리
@Composable
fun WorkingCounter() {
    // remember로 감싸서 Recomposition 간 보존!
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: ${'$'}count")
    }
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "비교 테스트:",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // remember 있는 카운터
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "✅ remember 있는 카운터",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { correctCount++ }) {
                        Text("+1")
                    }
                    Text(
                        text = "Count: $correctCount",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Recomposition 트리거
        Button(
            onClick = { trigger++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recomposition 발생 (trigger: $trigger)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Recomposition이 발생해도 remember로 감싼 count는 보존됩니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun RecompositionVisualization() {
    var parentCount by remember { mutableIntStateOf(0) }
    var childACount by remember { mutableIntStateOf(0) }
    var childBCount by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적
    var parentRecomposeCount by remember { mutableIntStateOf(0) }
    var childARecomposeCount by remember { mutableIntStateOf(0) }
    var childBRecomposeCount by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recomposition 시각화",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "State가 변경되면 해당 State를 읽는 Composable만 Recompose됩니다.",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Parent
            SideEffect { parentRecomposeCount++ }
            RecomposeCard(
                name = "Parent",
                recomposeCount = parentRecomposeCount,
                backgroundColor = Color(0xFFE3F2FD)
            ) {
                Text("parentCount: $parentCount")
                Button(
                    onClick = { parentCount++ },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Parent +1")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Child A
                key(childACount) {
                    SideEffect { childARecomposeCount++ }
                }
                RecomposeCard(
                    name = "Child A",
                    recomposeCount = childARecomposeCount,
                    backgroundColor = Color(0xFFE8F5E9)
                ) {
                    Text("childACount: $childACount")
                    Button(onClick = { childACount++ }) {
                        Text("Child A +1")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Child B
                key(childBCount) {
                    SideEffect { childBRecomposeCount++ }
                }
                RecomposeCard(
                    name = "Child B",
                    recomposeCount = childBRecomposeCount,
                    backgroundColor = Color(0xFFFCE4EC)
                ) {
                    Text("childBCount: $childBCount")
                    Button(onClick = { childBCount++ }) {
                        Text("Child B +1")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "각 버튼을 클릭하면 해당 State를 읽는 Composable의 Recompose 횟수가 증가합니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun RecomposeCard(
    name: String,
    recomposeCount: Int,
    backgroundColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Recompose: ${recomposeCount}회",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE65100)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun SolutionCard(
    title: String,
    explanation: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
private fun CodeBlock(code: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF2D2D2D),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = code,
            modifier = Modifier.padding(12.dp),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = Color(0xFFE0E0E0),
            lineHeight = 18.sp
        )
    }
}
