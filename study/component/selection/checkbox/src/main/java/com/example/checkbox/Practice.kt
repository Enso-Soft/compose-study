package com.example.checkbox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 이용약관 동의 (쉬움)
 */
@Composable
fun Practice1_AgreementScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 1: 이용약관 동의",
            difficulty = "쉬움",
            requirements = listOf(
                "\"이용약관에 동의합니다\" 체크박스 구현",
                "체크해야만 \"가입하기\" 버튼 활성화",
                "텍스트 클릭으로도 체크되게 하기 (toggleable 사용)"
            )
        )

        HintCard(
            hints = listOf(
                "var isAgreed by remember { mutableStateOf(false) }",
                "Modifier.toggleable(value = isAgreed, onValueChange = {...}, role = Role.Checkbox)",
                "Checkbox(checked = isAgreed, onCheckedChange = null) // Row에서 처리",
                "Button(enabled = isAgreed)"
            )
        )

        // TODO: 여기에 이용약관 동의 체크박스를 구현하세요
        // 1. isAgreed 상태 선언
        // 2. Row에 toggleable modifier 적용
        // 3. Checkbox + Text("이용약관에 동의합니다") 배치
        // 4. Button(enabled = isAgreed)로 조건부 활성화

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
                    text = "TODO: 체크박스 + 가입하기 버튼 구현",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 연습 문제 2: 할 일 목록 (중간)
 */
@Composable
fun Practice2_TodoListScreen() {
    // 할 일 목록 데이터 (이 데이터를 사용하세요)
    val todos = listOf(
        "장보기",
        "운동하기",
        "책 읽기",
        "코딩 공부",
        "친구 만나기"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 2: 할 일 목록",
            difficulty = "중간",
            requirements = listOf(
                "할 일 목록 5개 표시",
                "완료된 항목은 취소선 표시",
                "상단에 \"3/5 완료\" 형식으로 진행률 표시",
                "완료된 항목은 초록색 체크박스"
            )
        )

        HintCard(
            hints = listOf(
                "val checkedStates = remember { mutableStateListOf(false, false, false, false, false) }",
                "TextDecoration.LineThrough",
                "CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))",
                "val completedCount = checkedStates.count { it }"
            )
        )

        // TODO: 여기에 할 일 목록을 구현하세요
        // 1. checkedStates: MutableList<Boolean> 상태 선언
        // 2. completedCount 계산
        // 3. LinearProgressIndicator(progress = completedCount / todos.size)
        // 4. 각 항목에 Checkbox + Text (취소선 조건부 적용)

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TODO: 할 일 목록 + 진행률 구현",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "사용할 데이터: ${todos.joinToString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 3: 쇼핑 카트 전체 선택 (어려움)
 */
@Composable
fun Practice3_ShoppingCartScreen() {
    // 상품 데이터 (이 데이터를 사용하세요)
    data class CartItem(val name: String, val price: Int)
    val cartItems = listOf(
        CartItem("무선 이어폰", 150000),
        CartItem("스마트 워치", 300000),
        CartItem("블루투스 스피커", 80000),
        CartItem("USB 케이블", 15000)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 3: 쇼핑 카트 전체 선택",
            difficulty = "어려움",
            requirements = listOf(
                "상품 목록 (이름, 가격) 표시",
                "각 상품에 체크박스",
                "\"전체 선택\" TriStateCheckbox",
                "선택된 상품의 총 가격 표시",
                "선택된 항목 없으면 \"주문하기\" 버튼 비활성화"
            )
        )

        HintCard(
            hints = listOf(
                "val checkedStates = remember { mutableStateListOf(false, false, false, false) }",
                "ToggleableState.On, Off, Indeterminate",
                "when { all -> On; none -> Off; else -> Indeterminate }",
                "TriStateCheckbox(state = parentState, onClick = { 전체 토글 })",
                "cartItems.filterIndexed { i, _ -> checkedStates[i] }.sumOf { it.price }"
            )
        )

        // TODO: 여기에 쇼핑 카트를 구현하세요
        // 1. checkedStates: MutableList<Boolean> 상태 선언
        // 2. parentState: ToggleableState 계산
        // 3. TriStateCheckbox로 전체 선택/해제
        // 4. 각 상품에 Checkbox + 이름 + 가격
        // 5. 선택된 상품 합계 계산
        // 6. Button(enabled = selectedCount > 0)

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
                        text = "TODO: 전체 선택 + 상품 목록 + 합계 구현",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "TriStateCheckbox + 가격 합계 계산",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// =============================================================================
// 공통 컴포넌트
// =============================================================================

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
