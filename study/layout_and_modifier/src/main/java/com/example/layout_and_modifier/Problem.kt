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
 * Problem: Layout & Modifier 사용 시 흔한 실수
 *
 * 1. Modifier 순서에 따른 결과 차이
 * 2. fillMaxWidth 없이 레이아웃
 * 3. Arrangement/Alignment 미지정
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
                    text = "문제: Layout & Modifier 실수",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Modifier 순서, fillMax, Arrangement를 이해하지 못하면 원하는 레이아웃이 안 나옵니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 1: Modifier 순서
        ModifierOrderProblem()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 2: fillMaxWidth 미사용
        FillMaxProblem()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 3: Arrangement 미지정
        ArrangementProblem()
    }
}

@Composable
private fun ModifierOrderProblem() {
    var clickCount1 by remember { mutableIntStateOf(0) }
    var clickCount2 by remember { mutableIntStateOf(0) }

    ProblemCard(
        title = "문제 1: Modifier 순서가 결과를 바꾼다",
        errorCode = """
// 순서 A: background → padding
Modifier.background(Red).padding(16.dp)

// 순서 B: padding → background
Modifier.padding(16.dp).background(Red)

// 둘의 결과가 다릅니다!
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
                    color = MaterialTheme.colorScheme.error
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
private fun FillMaxProblem() {
    ProblemCard(
        title = "문제 2: fillMaxWidth 없으면 컨텐츠 크기만큼",
        errorCode = """
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
        explanation = "레이아웃이 예상보다 작다면 fillMaxWidth/Height를 확인하세요."
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
private fun ArrangementProblem() {
    ProblemCard(
        title = "문제 3: Arrangement 없으면 시작 부분에 붙음",
        errorCode = """
// Arrangement 없음
Column {
    Text("A")
    Text("B")
    Text("C")
}
// 결과: 모두 위에 붙어있음

// Arrangement 있음
Column(
    verticalArrangement = Arrangement.SpaceEvenly
) {
    Text("A")
    Text("B")
    Text("C")
}
// 결과: 균등하게 분포
        """.trimIndent(),
        explanation = "자식들 사이에 간격이 필요하면 Arrangement를 지정하세요."
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
            Text("❌ 위에 붙음", color = MaterialTheme.colorScheme.error)
            Text("✅ 균등 분포", color = Color(0xFF2E7D32))
        }
    }
}

@Composable
private fun ProblemCard(
    title: String,
    errorCode: String,
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
                    fontSize = 11.sp,
                    color = Color(0xFFFF6B6B),
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
