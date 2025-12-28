package com.example.checkbox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

/**
 * 해결책 - Material Checkbox 사용
 *
 * 장점:
 * 1. mutableStateListOf로 효율적인 상태 관리
 * 2. TriStateCheckbox로 전체 선택 간편 구현
 * 3. Material Design 자동 준수
 * 4. 접근성 자동 지원
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

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
                    text = "Solution: Material Checkbox 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Checkbox와 TriStateCheckbox로 깔끔하게 해결!",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 데모 선택
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 4)
            ) { Text("기본", maxLines = 1) }
            SegmentedButton(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 4)
            ) { Text("전체선택", maxLines = 1) }
            SegmentedButton(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 4)
            ) { Text("색상", maxLines = 1) }
            SegmentedButton(
                selected = selectedDemo == 3,
                onClick = { selectedDemo = 3 },
                shape = SegmentedButtonDefaults.itemShape(index = 3, count = 4)
            ) { Text("접근성", maxLines = 1) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> BasicCheckboxDemo()
            1 -> TriStateCheckboxDemo()
            2 -> ColorCustomizationDemo()
            3 -> AccessibilityDemo()
        }
    }
}

/**
 * 데모 1: 기본 Checkbox 사용법
 */
@Composable
private fun BasicCheckboxDemo() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "1. 기본 Checkbox",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "checked와 onCheckedChange 두 가지만 있으면 됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 기본 체크박스
            var isChecked by remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("기본 체크박스")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isChecked) "체크됨!" else "체크 안 됨",
                color = if (isChecked) Color(0xFF4CAF50) else Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // enabled/disabled 상태
            Text(
                text = "enabled 속성",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false  // 비활성화
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("비활성화된 체크박스", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
Checkbox(
    checked = isChecked,
    onCheckedChange = { isChecked = it }
)
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 데모 2: TriStateCheckbox로 전체 선택
 */
@Composable
private fun TriStateCheckboxDemo() {
    // 상품 데이터
    data class Product(val name: String, val price: Int)
    val products = listOf(
        Product("상품 A", 10000),
        Product("상품 B", 20000),
        Product("상품 C", 30000)
    )

    // 체크 상태 관리 - mutableStateListOf 사용!
    val checkedStates = remember { mutableStateListOf(false, false, false) }

    // 부모 상태 자동 계산
    val parentState = when {
        checkedStates.all { it } -> ToggleableState.On
        checkedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    // 선택된 상품 가격 합계
    val totalPrice = products.filterIndexed { index, _ ->
        checkedStates[index]
    }.sumOf { it.price }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "2. TriStateCheckbox - 전체 선택",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "모두 선택 / 일부 선택 / 모두 해제 3가지 상태 표현",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 전체 선택 TriStateCheckbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TriStateCheckbox(
                    state = parentState,
                    onClick = {
                        // On이 아니면 모두 선택, On이면 모두 해제
                        val newState = parentState != ToggleableState.On
                        checkedStates.indices.forEach { checkedStates[it] = newState }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "전체 선택",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = when (parentState) {
                        ToggleableState.On -> "모두 선택됨"
                        ToggleableState.Off -> "선택 없음"
                        ToggleableState.Indeterminate -> "일부 선택됨"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // 개별 상품
            products.forEachIndexed { index, product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checkedStates[index],
                        onCheckedChange = { checkedStates[index] = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = product.name,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${product.price}원",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 총액 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "선택 상품 합계:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${totalPrice}원",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
// 부모 상태 계산
val parentState = when {
    checkedStates.all { it } -> ToggleableState.On
    checkedStates.none { it } -> ToggleableState.Off
    else -> ToggleableState.Indeterminate
}

TriStateCheckbox(
    state = parentState,
    onClick = { /* 토글 로직 */ }
)
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 데모 3: 색상 커스터마이징
 */
@Composable
private fun ColorCustomizationDemo() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(false) }
    var checked3 by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "3. 색상 커스터마이징",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "CheckboxDefaults.colors()로 색상을 변경할 수 있습니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 초록색 체크박스
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked1,
                    onCheckedChange = { checked1 = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4CAF50),
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "완료됨",
                    textDecoration = if (checked1) TextDecoration.LineThrough else null
                )
            }

            // 빨간색 체크박스
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked2,
                    onCheckedChange = { checked2 = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFF44336),
                        uncheckedColor = Color(0xFFF44336),
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("중요 항목")
            }

            // 파란색 체크박스
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked3,
                    onCheckedChange = { checked3 = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF2196F3),
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("정보 항목")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
Checkbox(
    checked = isChecked,
    onCheckedChange = { isChecked = it },
    colors = CheckboxDefaults.colors(
        checkedColor = Color.Green,
        uncheckedColor = Color.Gray,
        checkmarkColor = Color.White
    )
)
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 데모 4: 접근성 패턴
 */
@Composable
private fun AccessibilityDemo() {
    var isAgreed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "4. 접근성 패턴",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "텍스트 클릭으로도 체크되게 하기 (toggleable 사용)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 권장 패턴: Row 전체를 클릭 가능하게
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = isAgreed,
                        onValueChange = { isAgreed = it },
                        role = Role.Checkbox  // 스크린 리더용
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAgreed,
                    onCheckedChange = null  // Row에서 처리하므로 null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("이용약관에 동의합니다 (텍스트 클릭해보세요)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* 가입 처리 */ },
                enabled = isAgreed,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("가입하기")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
Row(
    modifier = Modifier.toggleable(
        value = isAgreed,
        onValueChange = { isAgreed = it },
        role = Role.Checkbox  // 접근성
    )
) {
    Checkbox(
        checked = isAgreed,
        onCheckedChange = null  // Row에서 처리
    )
    Text("이용약관에 동의합니다")
}
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
