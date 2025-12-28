package com.example.layout_and_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
 * Modifier 실험실: Layout & Modifier 동작 원리 탐구
 *
 * 이 화면에서는 Modifier와 Layout의 동작 원리를 실험을 통해 이해합니다.
 * 1. Modifier 순서에 따른 결과 차이
 * 2. fillMaxWidth 유무에 따른 크기 변화
 * 3. Arrangement 적용에 따른 배치 변화
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 실험실 소개 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Modifier 실험실",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Modifier 순서, fillMaxWidth, Arrangement의 동작 원리를 직접 실험하며 이해해보세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 실험 1: Modifier 순서
        ModifierOrderExperiment()

        Spacer(modifier = Modifier.height(16.dp))

        // 실험 2: fillMaxWidth 동작
        FillMaxExperiment()

        Spacer(modifier = Modifier.height(16.dp))

        // 실험 3: Arrangement 동작
        ArrangementExperiment()
    }
}

@Composable
private fun ModifierOrderExperiment() {
    var clickCount1 by remember { mutableIntStateOf(0) }
    var clickCount2 by remember { mutableIntStateOf(0) }

    ExperimentCard(
        title = "실험 1: Modifier 순서에 따른 결과 비교",
        codeSnippet = """
// 순서 A: background → padding
Modifier.background(Red).padding(16.dp)

// 순서 B: padding → background
Modifier.padding(16.dp).background(Red)

// 두 결과를 비교해보세요!
        """.trimIndent(),
        explanation = "Modifier는 바깥쪽부터 안쪽으로 적용됩니다."
    ) {
        Text(
            text = "직접 비교해보세요:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 순서 A: background → padding
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("background → padding", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE57373))
                        .padding(16.dp)
                        .clickable { clickCount1++ }
                ) {
                    Text("클릭: $clickCount1", color = Color.White)
                }
                Text(
                    "패딩 영역에\n배경 없음",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            // 순서 B: padding → background
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("padding → background", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFF81C784))
                        .clickable { clickCount2++ }
                ) {
                    Text("클릭: $clickCount2")
                }
                Text(
                    "패딩 포함\n전체 배경",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }
}

@Composable
private fun FillMaxExperiment() {
    ExperimentCard(
        title = "실험 2: fillMaxWidth 유무에 따른 크기 변화",
        codeSnippet = """
// fillMaxWidth 없음
Card {
    Text("좁은 카드")
}
// 결과: 텍스트만큼만 카드가 생성됨

// fillMaxWidth 있음
Card(modifier = Modifier.fillMaxWidth()) {
    Text("넓은 카드")
}
// 결과: 화면 너비만큼 카드 생성
        """.trimIndent(),
        explanation = "Composable은 기본적으로 컨텐츠 크기만큼 차지합니다."
    ) {
        Text(
            text = "비교:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // fillMaxWidth 없음
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFCDD2)
                )
            ) {
                Text(
                    "fillMaxWidth 없음 - 좁음!",
                    modifier = Modifier.padding(8.dp)
                )
            }

            // fillMaxWidth 있음
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFC8E6C9)
                )
            ) {
                Text(
                    "fillMaxWidth 있음 - 넓음!",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun ArrangementExperiment() {
    ExperimentCard(
        title = "실험 3: Arrangement 적용에 따른 배치 변화",
        codeSnippet = """
// Arrangement 없음 (기본값: Top/Start)
Column {
    Text("A")
    Text("B")
    Text("C")
}
// 결과: 모두 시작 부분에 배치됨

// Arrangement 적용
Column(
    verticalArrangement = Arrangement.SpaceEvenly
) {
    Text("A")
    Text("B")
    Text("C")
}
// 결과: 균등하게 분포
        """.trimIndent(),
        explanation = "Arrangement로 자식 요소들의 배치 방식을 제어할 수 있습니다."
    ) {
        Text(
            text = "비교 (높이 고정 150dp):",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Arrangement 없음
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFCDD2)
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                    // verticalArrangement 없음!
                ) {
                    Text("A")
                    Text("B")
                    Text("C")
                }
            }

            // Arrangement 있음
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFC8E6C9)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("A")
                    Text("B")
                    Text("C")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("기본값 (Top)", color = MaterialTheme.colorScheme.tertiary)
            Text("SpaceEvenly", color = Color(0xFF2E7D32))
        }
    }
}

@Composable
private fun ExperimentCard(
    title: String,
    codeSnippet: String,
    explanation: String,
    content: @Composable ColumnScope.() -> Unit
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
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 코드 블록
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2D2D2D),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = codeSnippet,
                    modifier = Modifier.padding(12.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFF90CAF9),
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}
