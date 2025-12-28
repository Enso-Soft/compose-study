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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Solution: IntrinsicSize로 자식 크기 동기화!
 *
 * IntrinsicSize.Min/Max를 사용하면:
 * 1. Row/Column이 자식들의 '고유 크기(intrinsic size)'를 먼저 쿼리합니다
 * 2. 그 중 가장 큰 값으로 높이/너비를 제한합니다
 * 3. 모든 자식이 동일한 크기 제약을 받게 됩니다
 *
 * 중요: 이것은 "두 번 측정"이 아닙니다!
 * intrinsic 쿼리는 실제 측정과 다른 가벼운 연산입니다.
 */

@Composable
fun SolutionScreen() {
    var showSolution by remember { mutableStateOf("divider") }
    var useIntrinsic by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 개념 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "IntrinsicSize 핵심 개념",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "IntrinsicSize는 '사전 조사원'과 같습니다:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. 집을 지을 때 (측정 전에)\n" +
                            "2. 각 방에 들어갈 가구 크기를 먼저 조사합니다 (intrinsic 쿼리)\n" +
                            "3. 그 정보를 바탕으로 방 크기를 결정합니다 (제약 계산)\n" +
                            "4. 그 다음 실제로 가구를 배치합니다 (측정)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 솔루션 선택 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = showSolution == "divider",
                onClick = { showSolution = "divider" },
                label = { Text("Divider 해결") }
            )
            FilterChip(
                selected = showSolution == "card",
                onClick = { showSolution = "card" },
                label = { Text("카드 해결") }
            )
            FilterChip(
                selected = showSolution == "compare",
                onClick = { showSolution = "compare" },
                label = { Text("Min vs Max") }
            )
        }

        // 토글: IntrinsicSize 적용/해제
        if (showSolution != "compare") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (useIntrinsic) "IntrinsicSize.Min 적용" else "IntrinsicSize 없음",
                    fontWeight = FontWeight.Medium,
                    color = if (useIntrinsic) Color(0xFF2E7D32) else Color.Red
                )
                Switch(
                    checked = useIntrinsic,
                    onCheckedChange = { useIntrinsic = it }
                )
            }
        }

        // 솔루션 시연
        when (showSolution) {
            "divider" -> Solution1_DividerHeight(useIntrinsic)
            "card" -> Solution2_CardHeight(useIntrinsic)
            "compare" -> Solution3_MinVsMax()
        }

        // 코드 설명
        CodeExplanationCard(showSolution)
    }
}

/**
 * 해결책 1: VerticalDivider 높이 동기화
 */
@Composable
private fun Solution1_DividerHeight(useIntrinsic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결: Divider 높이 동기화",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 핵심: height(IntrinsicSize.Min) 적용
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (useIntrinsic) {
                            Modifier.height(IntrinsicSize.Min)  // 핵심!
                        } else {
                            Modifier
                        }
                    )
                    .border(2.dp, if (useIntrinsic) Color(0xFF2E7D32) else Color.Red)
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

                // VerticalDivider - IntrinsicSize.Min 덕분에 정상 동작!
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

            Text(
                text = if (useIntrinsic) {
                    "IntrinsicSize.Min 적용: 파란 Divider가 왼쪽 텍스트 높이에 맞춰집니다!"
                } else {
                    "IntrinsicSize 없음: Divider가 올바르게 표시되지 않습니다."
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (useIntrinsic) Color(0xFF2E7D32) else Color.Red
            )
        }
    }
}

/**
 * 해결책 2: 카드 높이 동기화
 */
@Composable
private fun Solution2_CardHeight(useIntrinsic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결: 카드 높이 동기화",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 핵심: height(IntrinsicSize.Min) + fillMaxHeight()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (useIntrinsic) {
                            Modifier.height(IntrinsicSize.Min)  // 핵심!
                        } else {
                            Modifier
                        }
                    )
                    .border(2.dp, if (useIntrinsic) Color(0xFF2E7D32) else Color.Red),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 짧은 카드
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (useIntrinsic) Modifier.fillMaxHeight() else Modifier
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("상품 A", fontWeight = FontWeight.Bold)
                        Text("10,000원")
                        if (useIntrinsic) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("구매")
                        }
                    }
                }

                // 긴 카드
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (useIntrinsic) Modifier.fillMaxHeight() else Modifier
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("상품 B", fontWeight = FontWeight.Bold)
                        Text("25,000원")
                        Text("할인율: 20%")
                        Text("배송: 무료")
                        if (useIntrinsic) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("구매")
                        }
                    }
                }

                // 중간 카드
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (useIntrinsic) Modifier.fillMaxHeight() else Modifier
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE1F5FE)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("상품 C", fontWeight = FontWeight.Bold)
                        Text("15,000원")
                        Text("할인율: 10%")
                        if (useIntrinsic) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("구매")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (useIntrinsic) {
                    "모든 카드가 동일한 높이! Spacer(weight)로 버튼을 하단에 정렬했습니다."
                } else {
                    "카드마다 높이가 다르고, 버튼 위치도 제각각입니다."
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (useIntrinsic) Color(0xFF2E7D32) else Color.Red
            )
        }
    }
}

/**
 * 해결책 3: Min vs Max 비교
 */
@Composable
private fun Solution3_MinVsMax() {
    var useMin by remember { mutableStateOf(true) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Min vs Max 비교",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Min/Max 선택
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = useMin,
                    onClick = { useMin = true },
                    label = { Text("IntrinsicSize.Min") }
                )
                FilterChip(
                    selected = !useMin,
                    onClick = { useMin = false },
                    label = { Text("IntrinsicSize.Max") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (useMin) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (useMin) "IntrinsicSize.Min" else "IntrinsicSize.Max",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (useMin) {
                            "콘텐츠를 적절히 표시하는 최소 크기입니다.\n" +
                                    "텍스트의 경우: 가장 긴 단어의 너비 (여러 줄 가능)"
                        } else {
                            "콘텐츠를 적절히 표시하는 최대 크기입니다.\n" +
                                    "텍스트의 경우: 모든 텍스트를 한 줄에 표시하는 너비"
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 너비 예시
            Text(
                text = "width(IntrinsicSize) 예시:",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .width(
                        if (useMin) IntrinsicSize.Min else IntrinsicSize.Max
                    )
                    .border(2.dp, if (useMin) Color(0xFF2E7D32) else Color(0xFFFF9800))
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Hello World This Is A Long Text For Intrinsic Demo",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (useMin) {
                    "Min: 가장 긴 단어('Intrinsic') 너비에 맞춰 여러 줄로 표시"
                } else {
                    "Max: 전체 텍스트를 한 줄에 표시하는 너비"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 코드 설명 카드
 */
@Composable
private fun CodeExplanationCard(solutionType: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "핵심 코드",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val codeText = when (solutionType) {
                "divider" -> """
// Row에 height(IntrinsicSize.Min) 적용
Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)  // 핵심!
) {
    Text("여러 줄\n텍스트", Modifier.weight(1f))
    VerticalDivider(
        modifier = Modifier
            .fillMaxHeight()  // Min 덕분에 동작!
            .width(1.dp)
    )
    Text("짧은 텍스트", Modifier.weight(1f))
}
                """.trimIndent()

                "card" -> """
// Row + Card + fillMaxHeight 조합
Row(
    modifier = Modifier.height(IntrinsicSize.Min)
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()  // 동일 높이!
    ) {
        Column {
            Text("내용")
            Spacer(Modifier.weight(1f))  // 남은 공간
            Button({}) { Text("하단 버튼") }
        }
    }
    // 다른 카드들도 동일...
}
                """.trimIndent()

                else -> """
// Min: 최소 필요 크기
Modifier.width(IntrinsicSize.Min)
// → 텍스트가 여러 줄로 나뉠 수 있음

// Max: 최대 필요 크기
Modifier.width(IntrinsicSize.Max)
// → 텍스트가 한 줄로 표시됨

// 높이도 동일하게 적용
Modifier.height(IntrinsicSize.Min)
Modifier.height(IntrinsicSize.Max)
                """.trimIndent()
            }

            Text(
                text = codeText,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            )
        }
    }
}
