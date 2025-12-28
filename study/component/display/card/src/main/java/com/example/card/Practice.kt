package com.example.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Practice: Card 연습 문제
 *
 * 3단계 연습을 통해 Card 사용법을 익힙니다:
 * 1. 쉬움: 기본 Card 3가지 유형 만들기
 * 2. 중간: 클릭하면 확장되는 Card 구현
 * 3. 어려움: 상품 정보 카드 만들기
 */
@Composable
fun PracticeScreen() {
    var selectedPractice by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedPractice == 0,
                onClick = { selectedPractice = 0 },
                label = { Text("쉬움") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("중간") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("어려움") }
            )
        }

        when (selectedPractice) {
            0 -> Exercise1_BasicCards()
            1 -> Exercise2_ExpandableCard()
            2 -> Exercise3_ProductCard()
        }
    }
}

// ============================================================
// 연습 1: 기본 Card 3가지 유형 만들기 (쉬움)
// ============================================================
@Composable
fun Exercise1_BasicCards() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ExerciseCard(
                title = "연습 1: 기본 Card 3가지 유형",
                difficulty = "쉬움",
                requirements = listOf(
                    "Card (Filled): '일반 카드' 텍스트, surfaceVariant 배경색",
                    "ElevatedCard: '떠있는 카드' 텍스트, 6dp elevation",
                    "OutlinedCard: '테두리 카드' 텍스트, 1dp 검은색 테두리"
                )
            )
        }

        item {
            HintCard(
                hints = listOf(
                    "Card(colors = CardDefaults.cardColors(containerColor = ...))",
                    "ElevatedCard(elevation = CardDefaults.cardElevation(defaultElevation = 6.dp))",
                    "OutlinedCard(border = BorderStroke(1.dp, Color.Black))"
                )
            )
        }

        item {
            // TODO: 여기에 3가지 Card를 구현하세요
            // 1. Card(Filled): surfaceVariant 배경색, "일반 카드" 텍스트
            // 2. ElevatedCard: 6dp elevation, "떠있는 카드" 텍스트
            // 3. OutlinedCard: 1dp 검은색 테두리, "테두리 카드" 텍스트

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TODO: Card, ElevatedCard, OutlinedCard 구현하기",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ============================================================
// 연습 2: 클릭하면 확장되는 Card (중간)
// ============================================================
@Composable
fun Exercise2_ExpandableCard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ExerciseCard(
                title = "연습 2: 확장되는 Card",
                difficulty = "중간",
                requirements = listOf(
                    "기본 상태: 제목만 표시",
                    "확장 상태: 제목 + 상세 설명 표시",
                    "화살표 아이콘이 확장 시 180도 회전",
                    "확장/축소 시 애니메이션 적용"
                )
            )
        }

        item {
            HintCard(
                hints = listOf(
                    "var isExpanded by remember { mutableStateOf(false) }",
                    "ElevatedCard(onClick = { isExpanded = !isExpanded })",
                    "AnimatedVisibility(visible = isExpanded) { ... }",
                    "Modifier.rotate(if (isExpanded) 180f else 0f)"
                )
            )
        }

        item {
            // TODO: 여기에 확장 가능한 Card를 구현하세요
            // 1. isExpanded 상태 관리
            // 2. ElevatedCard(onClick = ...) 사용
            // 3. Row에 제목 + ExpandMore 아이콘 배치
            // 4. AnimatedVisibility로 상세 내용 표시/숨김
            // 5. 아이콘 회전 애니메이션

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TODO: 클릭 시 확장되는 Card 구현하기\n애니메이션 포함",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ============================================================
// 연습 3: 상품 정보 카드 만들기 (어려움)
// ============================================================
@Composable
fun Exercise3_ProductCard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ExerciseCard(
                title = "연습 3: 상품 정보 카드",
                difficulty = "어려움",
                requirements = listOf(
                    "상단: 상품 이미지 영역 (160dp 높이)",
                    "중단: 상품명 (titleMedium), 가격 (bodyLarge + 굵게)",
                    "하단: '찜하기' OutlinedButton + '구매하기' Button",
                    "ElevatedCard 사용 + Modifier.fillMaxWidth()",
                    "Card 클릭 시 Snackbar로 상품명 표시"
                )
            )
        }

        item {
            HintCard(
                hints = listOf(
                    "Column { 이미지영역; 텍스트영역; 버튼영역 }",
                    "Box(modifier = Modifier.height(160.dp).fillMaxWidth().background(...))",
                    "Row { OutlinedButton(Modifier.weight(1f)); Button(Modifier.weight(1f)) }",
                    "Text(fontWeight = FontWeight.Bold)"
                )
            )
        }

        item {
            // TODO: 여기에 상품 정보 카드를 구현하세요
            // 1. ElevatedCard(onClick = ...) 사용
            // 2. 상단: 160dp 높이의 이미지 영역 (Box + Icon)
            // 3. 중단: 상품명, 설명, 가격 텍스트
            // 4. 하단: 찜하기 + 구매하기 버튼
            // 5. Scaffold + SnackbarHost로 클릭 시 메시지 표시

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TODO: 상품 정보 카드 구현하기",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "이미지 + 상품명 + 가격 + 버튼",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
private fun ExerciseCard(
    title: String,
    difficulty: String,
    requirements: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                AssistChip(
                    onClick = { },
                    label = { Text(difficulty) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "요구사항:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            requirements.forEach { req ->
                Text(
                    text = "• $req",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "숨기기" else "보기")
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                hints.forEach { hint ->
                    Text(
                        text = "• $hint",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}
