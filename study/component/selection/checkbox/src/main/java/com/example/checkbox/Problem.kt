package com.example.checkbox

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - Checkbox 없이 직접 구현
 *
 * 이 코드의 문제점:
 * 1. 각 항목마다 개별 상태 변수가 필요 - 확장성 없음
 * 2. 전체 선택 로직을 직접 구현해야 함 - 복잡함
 * 3. Material Design 가이드라인 미준수
 * 4. 접근성 미지원 - 스크린 리더가 인식 못함
 */
@Composable
fun ProblemScreen() {
    // 문제 1: 각 항목마다 개별 상태 변수 필요!
    // 항목이 10개면 10개의 변수가 필요...
    var item1Checked by remember { mutableStateOf(false) }
    var item2Checked by remember { mutableStateOf(false) }
    var item3Checked by remember { mutableStateOf(false) }

    // 문제 2: 전체 선택 로직을 직접 구현해야 함
    val allChecked = item1Checked && item2Checked && item3Checked
    val noneChecked = !item1Checked && !item2Checked && !item3Checked

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
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
                    text = "Problem: 직접 체크박스 구현",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Box와 Icon으로 체크박스를 직접 구현하면 어떤 문제가 생길까요?",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 직접 구현한 체크박스 데모
        Text(
            text = "직접 만든 체크박스 (문제 있음)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 전체 선택 - 직접 구현
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val newState = !allChecked
                    item1Checked = newState
                    item2Checked = newState
                    item3Checked = newState
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(
                checked = allChecked,
                indeterminate = !allChecked && !noneChecked,
                onCheckedChange = {
                    val newState = !allChecked
                    item1Checked = newState
                    item2Checked = newState
                    item3Checked = newState
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "전체 선택",
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // 개별 항목들
        CustomCheckboxItem(
            label = "상품 A - 10,000원",
            checked = item1Checked,
            onCheckedChange = { item1Checked = it }
        )
        CustomCheckboxItem(
            label = "상품 B - 20,000원",
            checked = item2Checked,
            onCheckedChange = { item2Checked = it }
        )
        CustomCheckboxItem(
            label = "상품 C - 30,000원",
            checked = item3Checked,
            onCheckedChange = { item3Checked = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 문제점 설명
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
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem(number = 1, text = "상태 관리 복잡: 항목마다 개별 변수 필요")
                ProblemItem(number = 2, text = "전체 선택 로직: 모든 상태 일일이 확인")
                ProblemItem(number = 3, text = "Material Design 미준수: 표준과 다른 모양")
                ProblemItem(number = 4, text = "접근성 문제: 스크린 리더 미지원")
                ProblemItem(number = 5, text = "코드 중복: 유사한 코드 반복")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드의 문제점",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 항목이 늘어날수록 변수도 늘어남
var item1Checked by remember { mutableStateOf(false) }
var item2Checked by remember { mutableStateOf(false) }
var item3Checked by remember { mutableStateOf(false) }
// item4, item5, item6... 계속 추가?

// 전체 선택 확인도 복잡
val allChecked = item1Checked &&
                 item2Checked &&
                 item3Checked
// && item4Checked && item5Checked...
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Solution 탭에서 Checkbox로 깔끔하게 해결하는 방법을 확인하세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 직접 구현한 체크박스 - 문제가 많음!
 */
@Composable
private fun CustomCheckbox(
    checked: Boolean,
    indeterminate: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 2.dp,
                color = if (checked || indeterminate) Color(0xFF6200EE) else Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        when {
            checked -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(18.dp)
                )
            }
            indeterminate -> {
                Box(
                    modifier = Modifier
                        .size(12.dp, 2.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = Color(0xFF6200EE)
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomCheckboxItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label)
    }
}

@Composable
private fun ProblemItem(number: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number.",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}
