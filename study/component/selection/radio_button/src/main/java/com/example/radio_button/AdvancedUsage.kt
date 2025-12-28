package com.example.radio_button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 고급 활용 화면
 *
 * RadioButton의 고급 사용법과 조합 패턴을 학습합니다.
 * - 접근성(Accessibility) 최적화 패턴
 * - Card 스타일 RadioButton
 * - RadioButton vs Checkbox 비교
 */
@Composable
fun AdvancedUsageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 고급 활용 소개
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "고급 활용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기본 기능을 조합하여 더 강력하고 접근성 좋은 RadioButton 패턴을 만들어봅시다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 패턴 1: 접근성 최적화
        PatternCard(
            title = "패턴 1: 접근성 최적화 (권장)",
            description = "스크린 리더(TalkBack)가 RadioButton 그룹을 올바르게 인식하도록 합니다.",
            useCase = "모든 RadioButton 그룹에 권장되는 표준 패턴"
        ) {
            AccessibleRadioGroupDemo()
        }

        // 패턴 2: 카드 스타일
        PatternCard(
            title = "패턴 2: 카드 스타일 RadioButton",
            description = "선택 옵션을 Card로 감싸 시각적으로 강조합니다. 선택된 카드에 테두리를 추가합니다.",
            useCase = "요금제 선택, 구독 플랜 선택 UI"
        ) {
            CardStyleRadioDemo()
        }

        // RadioButton vs Checkbox 비교
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "RadioButton vs Checkbox 비교",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 비교표
                ComparisonTable()

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // 피자 주문 비유
                PizzaOrderExample()
            }
        }

        // 베스트 프랙티스 & 안티패턴
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "베스트 프랙티스",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                BulletPoint("Modifier.selectableGroup()을 Column에 적용하세요 (접근성)")
                BulletPoint("RadioButton의 onClick = null로 설정하고, Row가 클릭을 처리하게 하세요")
                BulletPoint("Row 높이를 최소 48-56dp로 유지하세요 (터치 영역)")
                BulletPoint("항상 Text 레이블과 함께 사용하세요")

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "안티패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                BulletPoint("다중 선택이 필요한 곳에 RadioButton 사용 (Checkbox 사용하세요)", isWarning = true)
                BulletPoint("레이블 없이 RadioButton만 사용 (의미 전달 불가)", isWarning = true)
                BulletPoint("각 RadioButton에 독립적인 onClick 처리 (그룹으로 관리하세요)", isWarning = true)
            }
        }
    }
}

@Composable
private fun PatternCard(
    title: String,
    description: String,
    useCase: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "사용 사례: $useCase",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            demo()
        }
    }
}

/**
 * 패턴 1: 접근성 최적화 (공식 권장 패턴)
 *
 * 핵심 포인트:
 * 1. Column에 Modifier.selectableGroup() 적용
 *    → 스크린 리더에게 "이 버튼들은 한 그룹"임을 알림
 *
 * 2. Row에 Modifier.selectable() 적용
 *    → 전체 Row를 클릭 가능하게 만듦
 *    → role = Role.RadioButton으로 역할 명시
 *
 * 3. RadioButton의 onClick = null
 *    → Row가 클릭을 처리하므로 중복 방지
 *    → 접근성 서비스와의 충돌 방지
 */
@Composable
private fun AccessibleRadioGroupDemo() {
    data class PaymentOption(
        val name: String,
        val icon: ImageVector
    )

    val options = listOf(
        PaymentOption("신용카드", Icons.Default.Star),
        PaymentOption("계좌이체", Icons.Default.Home),
        PaymentOption("간편결제", Icons.Default.Favorite)
    )
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column(Modifier.selectableGroup()) {  // 1. selectableGroup
        options.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)  // 터치 영역 확보
                    .selectable(  // 2. selectable
                        selected = (option == selectedOption),
                        onClick = { selectedOption = option },
                        role = Role.RadioButton  // 역할 명시
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null  // 3. onClick = null
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = option.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = selectedOption.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${selectedOption.name}(으)로 결제합니다",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * 패턴 2: 카드 스타일 RadioButton
 *
 * 시각적으로 눈에 띄는 선택 UI를 만듭니다.
 * - 선택된 카드에 BorderStroke 추가
 * - 가격, 설명 등 추가 정보 표시 가능
 */
@Composable
private fun CardStyleRadioDemo() {
    data class Plan(
        val name: String,
        val price: String,
        val description: String
    )

    val plans = listOf(
        Plan("Basic", "9,000원/월", "기본 기능"),
        Plan("Pro", "19,000원/월", "고급 기능 + 우선 지원"),
        Plan("Enterprise", "49,000원/월", "모든 기능 + 전담 매니저")
    )
    var selectedPlan by remember { mutableStateOf(plans[0]) }

    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        plans.forEach { plan ->
            val isSelected = plan == selectedPlan

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = { selectedPlan = plan },
                        role = Role.RadioButton
                    ),
                border = if (isSelected)
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                else
                    BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plan.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = plan.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = plan.price,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * RadioButton vs Checkbox 비교표
 */
@Composable
private fun ComparisonTable() {
    Column {
        // 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "기준",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "RadioButton",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Checkbox",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider()

        // 선택 방식
        ComparisonRow("선택 방식", "단일 선택", "다중 선택")
        ComparisonRow("사용 사례", "결제 수단, 크기", "토핑, 필터")
        ComparisonRow("상태", "상호 배타적", "독립적 ON/OFF")
    }
}

@Composable
private fun ComparisonRow(criterion: String, radio: String, checkbox: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = criterion,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = radio,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = checkbox,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

/**
 * 피자 주문 예제: RadioButton vs Checkbox 직관적 비교
 */
@Composable
private fun PizzaOrderExample() {
    Column {
        Text(
            text = "피자 주문으로 이해하기",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // RadioButton 예시: 크러스트
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "크러스트 (하나만!)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                val crusts = listOf("씬", "오리지널", "치즈크러스트")
                var selectedCrust by remember { mutableStateOf(crusts[1]) }

                crusts.forEach { crust ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(32.dp)
                    ) {
                        RadioButton(
                            selected = crust == selectedCrust,
                            onClick = { selectedCrust = crust },
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(crust, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Checkbox 예시: 토핑
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "토핑 (여러 개 OK)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(4.dp))

                val toppings = listOf("버섯", "피망", "페퍼로니")
                val selectedToppings = remember { mutableStateListOf<String>() }

                toppings.forEach { topping ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Checkbox(
                            checked = topping in selectedToppings,
                            onCheckedChange = { checked ->
                                if (checked) selectedToppings.add(topping)
                                else selectedToppings.remove(topping)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(topping, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun BulletPoint(text: String, isWarning: Boolean = false) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = if (isWarning) "X " else "O ",
            color = if (isWarning) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
