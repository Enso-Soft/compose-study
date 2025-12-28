package com.example.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * 문제 상황: Surface + Text로 태그 UI를 직접 구현
 *
 * 쇼핑몰 앱에서 필터 태그를 만들어야 합니다.
 * Material 3 Chip을 모른다면 어떻게 구현할까요?
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
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
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Surface + Text로 선택 가능한 태그 UI를 직접 만들어봅시다.\n" +
                            "얼마나 많은 코드가 필요한지 확인해보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 1: 선택 가능한 필터 태그 직접 구현
        ProblemSection(
            title = "문제 1: 선택 가능한 필터 태그",
            description = "배경색, 아이콘, 클릭 처리를 모두 직접 구현해야 합니다."
        ) {
            CustomFilterTagDemo()
        }

        // 문제 2: 삭제 가능한 입력 태그 직접 구현
        ProblemSection(
            title = "문제 2: 삭제 가능한 입력 태그",
            description = "X 버튼, 아바타, 삭제 로직을 모두 직접 구현해야 합니다."
        ) {
            CustomInputTagDemo()
        }

        // 문제점 정리
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 구현의 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProblemPoint("코드가 길고 복잡함 (각 태그마다 30줄+)")
                ProblemPoint("배경색, 아이콘 크기, 패딩 직접 계산")
                ProblemPoint("Material 3 표준 ripple 효과 없음")
                ProblemPoint("접근성(contentDescription) 누락 가능")
                ProblemPoint("여러 화면에서 같은 코드 반복")
            }
        }

        Text(
            text = "Solution 탭에서 Chip을 사용한 깔끔한 해결책을 확인하세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ProblemSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}

@Composable
private fun ProblemPoint(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("X", color = MaterialTheme.colorScheme.error)
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

// ============================================================
// 문제 1: 선택 가능한 필터 태그 직접 구현
// ============================================================

/**
 * CustomFilterTag: Surface + Text로 직접 구현한 필터 태그
 *
 * 이 코드가 얼마나 길고 복잡한지 확인하세요!
 * Chip을 사용하면 5줄로 동일한 기능을 구현할 수 있습니다.
 */
@Composable
fun CustomFilterTag(
    text: String,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. 배경색 직접 계산
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    // 2. 텍스트 색상 직접 계산
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    // 3. Surface + Row + 조건부 아이콘 + 텍스트 조합
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelectionChange(!isSelected) },
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 4. 선택 시 체크 아이콘 표시 (직접 구현)
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "선택됨",
                    modifier = Modifier.size(18.dp),
                    tint = contentColor
                )
            }

            // 5. 텍스트
            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun CustomFilterTagDemo() {
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }
    val filters = listOf("할인중", "무료배송", "당일배송")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                CustomFilterTag(
                    text = filter,
                    isSelected = filter in selectedFilters,
                    onSelectionChange = { selected ->
                        selectedFilters = if (selected) {
                            selectedFilters + filter
                        } else {
                            selectedFilters - filter
                        }
                    }
                )
            }
        }

        Text(
            text = "선택된 필터: ${selectedFilters.joinToString(", ").ifEmpty { "없음" }}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 코드 줄 수 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = "CustomFilterTag 구현: 약 35줄",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

// ============================================================
// 문제 2: 삭제 가능한 입력 태그 직접 구현
// ============================================================

/**
 * CustomInputTag: 삭제 버튼이 있는 입력 태그
 *
 * X 버튼, 아바타, 삭제 로직을 모두 직접 구현해야 합니다.
 */
@Composable
fun CustomInputTag(
    text: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 1. 아바타/아이콘 (직접 스타일링)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text.first().uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            // 2. 텍스트
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            // 3. 삭제 버튼 (직접 구현)
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "삭제",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun CustomInputTagDemo() {
    var tags by remember { mutableStateOf(listOf("Kotlin", "Android", "Compose")) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                CustomInputTag(
                    text = tag,
                    onDelete = { tags = tags - tag }
                )
            }
        }

        if (tags.isEmpty()) {
            Text(
                text = "모든 태그가 삭제되었습니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 초기화 버튼
        TextButton(
            onClick = { tags = listOf("Kotlin", "Android", "Compose") }
        ) {
            Text("태그 초기화")
        }

        // 코드 줄 수 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = "CustomInputTag 구현: 약 40줄",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
