package com.example.intrinsic_measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: IntrinsicSize 없이 Row에 Divider를 넣으면 높이가 동기화되지 않습니다!
 *
 * Row 안에 서로 다른 높이의 컴포저블과 VerticalDivider가 있을 때:
 * - VerticalDivider의 minIntrinsicHeight는 0입니다 (제약 없으면 공간 차지 안 함)
 * - fillMaxHeight()를 사용해도 Row에 높이 제약이 없으면 제대로 동작하지 않습니다
 * - 결과적으로 Divider가 보이지 않거나, 예상과 다른 높이가 됩니다
 */

@Composable
fun ProblemScreen() {
    var showProblem1 by remember { mutableStateOf(true) }
    var showProblem2 by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 상황 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Row + VerticalDivider 높이 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Row 안에 서로 다른 높이의 텍스트와 VerticalDivider가 있을 때, " +
                            "IntrinsicSize 없이는 Divider가 제대로 표시되지 않습니다. " +
                            "왜냐하면 Divider의 '최소 고유 높이(minIntrinsicHeight)'가 0이기 때문입니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 선택 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = showProblem1,
                onClick = { showProblem1 = true; showProblem2 = false },
                label = { Text("문제 1: Divider 높이") }
            )
            FilterChip(
                selected = showProblem2,
                onClick = { showProblem1 = false; showProblem2 = true },
                label = { Text("문제 2: 카드 높이") }
            )
        }

        // 문제 시연
        if (showProblem1) {
            Problem1_DividerHeight()
        }
        if (showProblem2) {
            Problem2_CardHeight()
        }

        // 문제점 요약
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. VerticalDivider가 텍스트 높이에 맞춰지지 않음")
                Text("2. 카드들이 서로 다른 높이를 가짐")
                Text("3. UI가 불균형하고 정돈되지 않아 보임")
            }
        }

        // 해결책 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: IntrinsicSize",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Modifier.height(IntrinsicSize.Min)을 사용하면 " +
                            "Row가 자식들의 '최소 고유 높이'를 먼저 확인하고, " +
                            "가장 큰 값에 맞춰 높이를 제한합니다. " +
                            "Solution 탭에서 확인하세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * 문제 1: VerticalDivider 높이가 텍스트에 맞춰지지 않음
 */
@Composable
private fun Problem1_DividerHeight() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 1: VerticalDivider 높이 문제",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "IntrinsicSize 없이 Row 구성:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 문제 상황: IntrinsicSize 없는 Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Red)
                    .padding(8.dp)
            ) {
                // 왼쪽: 여러 줄 텍스트
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFE3F2FD))
                        .padding(8.dp)
                ) {
                    Text("프로필 A", fontWeight = FontWeight.Bold)
                    Text("이름: 김철수")
                    Text("나이: 25세")
                    Text("직업: 개발자")
                }

                // VerticalDivider - IntrinsicSize 없으면 문제 발생!
                // fillMaxHeight()가 있어도 Row에 높이 제약이 없으면 동작 안 함
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp),
                    color = Color.Blue
                )

                // 오른쪽: 짧은 텍스트
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFFCE4EC))
                        .padding(8.dp)
                ) {
                    Text("프로필 B", fontWeight = FontWeight.Bold)
                    Text("이름: 이영희")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 문제 설명
            Text(
                text = "빨간 테두리 안에서 파란 Divider가 왼쪽 텍스트 높이에 맞춰지지 않습니다. " +
                        "Row에 높이 제약이 없어서 fillMaxHeight()가 0이 되거나 " +
                        "예상과 다르게 동작합니다.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

/**
 * 문제 2: 카드들의 높이가 동기화되지 않음
 */
@Composable
private fun Problem2_CardHeight() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 2: 카드 높이 불일치",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "IntrinsicSize 없이 카드 배치:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 문제 상황: 높이가 다른 카드들
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Red),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 짧은 카드
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("상품 A", fontWeight = FontWeight.Bold)
                        Text("10,000원")
                    }
                }

                // 긴 카드
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("상품 B", fontWeight = FontWeight.Bold)
                        Text("25,000원")
                        Text("할인율: 20%")
                        Text("배송: 무료")
                    }
                }

                // 중간 카드
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE1F5FE)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("상품 C", fontWeight = FontWeight.Bold)
                        Text("15,000원")
                        Text("할인율: 10%")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 문제 설명
            Text(
                text = "세 카드의 높이가 모두 다릅니다. 내용 길이에 따라 각자 다른 높이를 가지므로 " +
                        "UI가 불균형해 보입니다. 모든 카드가 가장 긴 카드와 같은 높이를 갖게 하려면?",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}
