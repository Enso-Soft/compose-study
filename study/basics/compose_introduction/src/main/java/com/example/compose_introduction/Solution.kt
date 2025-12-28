package com.example.compose_introduction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Solution: Compose의 선언적 UI
 *
 * Compose는 선언적(Declarative) 방식으로 UI를 구성합니다.
 * 상태가 변경되면 UI가 자동으로 업데이트됩니다.
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명
        SolutionExplanationCard()

        // 선언적 UI 코드 예시
        DeclarativeCodeExampleCard()

        // 인터랙티브 데모
        InteractiveDemoCard()

        // 코드 비교
        CodeComparisonCard()

        // 핵심 포인트
        KeyPointsCard()
    }
}

@Composable
private fun SolutionExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Compose의 선언적 UI",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = """
Jetpack Compose는 '선언적(Declarative)' 방식입니다.

선언적 UI의 핵심 원칙:
UI = f(State)

즉, UI는 상태의 함수입니다.
상태가 변경되면 UI가 자동으로 업데이트됩니다!

개발자는 "UI가 어떻게 보여야 하는지"만 선언하면 됩니다.
"어떻게 업데이트할지"는 Compose가 알아서 처리합니다.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DeclarativeCodeExampleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "선언적 UI 코드 예시 (Compose)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 코드 블록
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
@Composable
fun Counter() {
    // 상태 선언 - 단 한 줄!
    var count by remember { mutableStateOf(0) }

    Column {
        // UI는 상태의 함수
        Text("Count: ${'$'}count")
        Text("Double: ${'$'}{count * 2}")

        // 상태만 변경하면 UI는 자동 업데이트!
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    lineHeight = 18.sp
                )
            }

            Text(
                text = "위 코드에서 count가 변경되면 Count와 Double 모두 자동으로 업데이트됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun InteractiveDemoCard() {
    // 선언적 UI의 장점을 보여주는 인터랙티브 데모
    var count by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "인터랙티브 데모: 자동 동기화",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "아래 버튼을 눌러보세요. 모든 값이 자동으로 동기화됩니다!",
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider()

            // 여러 개의 파생 값을 표시 - 모두 자동 동기화!
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ValueDisplay(label = "Count", value = "$count")
                ValueDisplay(label = "x2", value = "${count * 2}")
                ValueDisplay(label = "x3", value = "${count * 3}")
                ValueDisplay(label = "^2", value = "${count * count}")
            }

            // 상태만 변경하면 모든 UI가 자동 업데이트!
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { count++ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+1")
                }

                Button(
                    onClick = { count += 5 },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+5")
                }

                Button(
                    onClick = { count = 0 },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = "count 상태 하나만 변경했는데, 4개의 값이 모두 자동으로 업데이트되었습니다!\n\n명령형 방식이었다면 4개의 setText()를 호출해야 했을 것입니다.",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ValueDisplay(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun CodeComparisonCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "코드 비교",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 명령형
            Text(
                text = "명령형 UI (기존 View)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
button.setOnClickListener {
    count++
    textViewCount.text = "Count: ${'$'}count"
    textViewDouble.text = "Double: ${'$'}{count * 2}"
    textViewTriple.text = "Triple: ${'$'}{count * 3}"
    textViewSquare.text = "Square: ${'$'}{count * count}"
}
                    """.trimIndent(),
                    modifier = Modifier.padding(8.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }

            // 선언적
            Text(
                text = "선언적 UI (Compose)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
Button(onClick = { count++ }) {
    Text("Increment")
}
// 끝! 모든 UI는 상태에 따라 자동 업데이트
                    """.trimIndent(),
                    modifier = Modifier.padding(8.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
private fun KeyPointsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "핵심 포인트",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            KeyPoint(
                number = 1,
                title = "UI = f(State)",
                description = "UI는 상태의 함수입니다. 상태가 변하면 UI가 자동으로 변합니다."
            )

            KeyPoint(
                number = 2,
                title = "수동 업데이트 불필요",
                description = "setText, setVisibility 같은 메서드를 호출할 필요가 없습니다."
            )

            KeyPoint(
                number = 3,
                title = "동기화 문제 해결",
                description = "같은 상태를 참조하는 모든 UI가 항상 일관성을 유지합니다."
            )

            KeyPoint(
                number = 4,
                title = "코드 간결성",
                description = "보일러플레이트 코드가 크게 줄어 가독성이 향상됩니다."
            )

            HorizontalDivider()

            Text(
                text = "Practice 탭에서 직접 Composable 함수를 작성해보세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun KeyPoint(
    number: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = "$number",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
