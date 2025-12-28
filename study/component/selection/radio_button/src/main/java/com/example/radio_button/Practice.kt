package com.example.radio_button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 성별 선택 (쉬움)
 *
 * 기본 RadioButton 사용법을 연습합니다.
 */
@Composable
fun Practice1_GenderScreen() {
    // 옵션 데이터 (이 데이터를 사용하세요)
    val options = listOf("남성", "여성", "선택 안 함")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 1: 성별 선택",
            difficulty = "쉬움",
            requirements = listOf(
                "회원가입 폼에서 성별 선택 RadioButton 그룹",
                "옵션: \"남성\", \"여성\", \"선택 안 함\"",
                "선택된 값 표시"
            )
        )

        HintCard(
            hints = listOf(
                "var selectedOption by remember { mutableStateOf(\"선택 안 함\") }",
                "options.forEach { option -> ... }",
                "RadioButton(selected = (option == selectedOption), onClick = { selectedOption = option })"
            )
        )

        // TODO: 여기에 성별 선택 RadioButton을 구현하세요
        // 1. selectedOption 상태 선언
        // 2. options.forEach로 RadioButton 목록 생성
        // 3. Row에 RadioButton + Text 배치
        // 4. 선택된 값 Card로 표시

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TODO: RadioButton 그룹 구현",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "옵션: ${options.joinToString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 2: 결제 수단 선택 (중간)
 *
 * 접근성 최적화 패턴을 적용합니다.
 */
@Composable
fun Practice2_PaymentScreen() {
    // 옵션 데이터 (이 데이터를 사용하세요)
    val options = listOf("신용카드", "계좌이체", "카카오페이", "네이버페이")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 2: 결제 수단 선택",
            difficulty = "중간",
            requirements = listOf(
                "결제 수단 RadioButton 그룹",
                "접근성 최적화 패턴 적용 (selectableGroup, selectable, role)",
                "선택된 결제 수단으로 \"결제하기\" 버튼 텍스트 변경"
            )
        )

        HintCard(
            hints = listOf(
                "Column(Modifier.selectableGroup()) { ... }",
                "Row(Modifier.selectable(selected = ..., onClick = ..., role = Role.RadioButton))",
                "RadioButton(selected = ..., onClick = null) // Row에서 처리",
                "Button { Text(\"\${selectedOption}(으)로 결제하기\") }"
            )
        )

        // TODO: 여기에 결제 수단 선택을 구현하세요
        // 1. selectedOption 상태 선언
        // 2. Column(Modifier.selectableGroup())로 감싸기
        // 3. Row에 selectable modifier + Role.RadioButton
        // 4. RadioButton(onClick = null) + Text
        // 5. 동적 버튼 텍스트

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TODO: 접근성 최적화 RadioButton 구현",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "selectableGroup + selectable + Role.RadioButton",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 3: 배송 옵션 선택 (어려움)
 *
 * 복잡한 요구사항을 종합적으로 구현합니다.
 */
@Composable
fun Practice3_DeliveryScreen() {
    // 배송 옵션 데이터 클래스와 데이터 (이 데이터를 사용하세요)
    data class DeliveryOption(
        val name: String,
        val price: Int,
        val description: String,
        val icon: ImageVector,
        val requiresMetroArea: Boolean = false
    )

    val options = listOf(
        DeliveryOption("일반배송", 0, "3-5일 소요", Icons.Default.ShoppingCart),
        DeliveryOption("빠른배송", 3000, "1-2일 소요", Icons.Default.Star),
        DeliveryOption("새벽배송", 5000, "다음날 오전 7시 전 도착", Icons.Default.Favorite, requiresMetroArea = true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 3: 배송 옵션 선택",
            difficulty = "어려움",
            requirements = listOf(
                "Card 스타일 RadioButton으로 배송 옵션 표시",
                "새벽배송은 \"수도권 거주\" 체크 시에만 활성화",
                "선택된 Card에 테두리 하이라이트",
                "선택된 옵션에 따라 배송비 표시"
            )
        )

        HintCard(
            hints = listOf(
                "var isMetroArea by remember { mutableStateOf(false) }",
                "Card(border = if (isSelected) BorderStroke(2.dp, primary) else ...)",
                "Modifier.selectable(enabled = !option.requiresMetroArea || isMetroArea)",
                "if (option.price == 0) \"무료\" else \"\${option.price}원\""
            )
        )

        // TODO: 여기에 배송 옵션 선택을 구현하세요
        // 1. isMetroArea, selectedOption 상태 선언
        // 2. "수도권 거주" Checkbox
        // 3. 각 옵션을 Card로 표시
        // 4. Card에 selectable modifier (enabled 조건 포함)
        // 5. 선택된 Card에 테두리 하이라이트
        // 6. 배송비 요약 Card

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
                        text = "TODO: Card 스타일 배송 옵션 구현",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "조건부 enabled + 테두리 하이라이트 + 배송비 표시",
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
