package com.example.chip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 해결책: Material 3 Chip 컴포넌트 사용
 *
 * 4가지 Chip 유형으로 다양한 UI를 쉽고 깔끔하게 구현합니다.
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 해결책 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Chip 컴포넌트",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Material 3의 4가지 Chip 유형으로 깔끔하게 구현할 수 있습니다.\n" +
                            "상태 관리, 스타일, 접근성이 모두 내장되어 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 1. AssistChip
        SolutionSection(
            title = "1. AssistChip - 스마트 액션",
            description = "사용자를 특정 방향으로 안내하는 액션 버튼"
        ) {
            AssistChipDemo()
        }

        // 2. FilterChip
        SolutionSection(
            title = "2. FilterChip - 필터링 선택",
            description = "선택/해제 가능한 필터 태그 (selected 상태 관리)"
        ) {
            FilterChipDemo()
        }

        // 3. InputChip
        SolutionSection(
            title = "3. InputChip - 사용자 입력 표시",
            description = "삭제 가능한 태그 (avatar, trailingIcon 지원)"
        ) {
            InputChipDemo()
        }

        // 4. SuggestionChip
        SolutionSection(
            title = "4. SuggestionChip - 동적 제안",
            description = "AI 추천, 자동완성 등 제안 표시"
        ) {
            SuggestionChipDemo()
        }

        // 5. FlowRow + Chip 조합
        SolutionSection(
            title = "5. FlowRow + Chip 조합",
            description = "태그가 많을 때 자동 줄바꿈 레이아웃"
        ) {
            FlowRowChipDemo()
        }

        // 장점 정리
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Chip 사용의 장점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                BenefitPoint("코드가 간결함 (5-10줄로 구현)")
                BenefitPoint("Material 3 디자인 자동 적용")
                BenefitPoint("Ripple 효과, 애니메이션 내장")
                BenefitPoint("접근성(contentDescription) 자동 처리")
                BenefitPoint("일관된 스타일과 크기")
            }
        }
    }
}

@Composable
private fun SolutionSection(
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
private fun BenefitPoint(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

// ============================================================
// 1. AssistChip - 스마트 액션
// ============================================================

@Composable
private fun AssistChipDemo() {
    var actionResult by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 기본 AssistChip
            AssistChip(
                onClick = { actionResult = "캘린더에 일정을 추가했습니다!" },
                label = { Text("캘린더에 추가") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )

            // leadingIcon 없는 AssistChip
            AssistChip(
                onClick = { actionResult = "알람이 설정되었습니다!" },
                label = { Text("알람 설정") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )
        }

        // Elevated 버전
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedAssistChip(
                onClick = { actionResult = "공유 메뉴가 열렸습니다!" },
                label = { Text("공유하기") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )

            Text(
                text = "(ElevatedAssistChip)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (actionResult.isNotEmpty()) {
            Text(
                text = actionResult,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ============================================================
// 2. FilterChip - 필터링 선택
// ============================================================

@Composable
private fun FilterChipDemo() {
    var selectedFilters by remember { mutableStateOf(setOf<String>()) }
    val filters = listOf("할인중", "무료배송", "당일배송")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                val isSelected = filter in selectedFilters

                FilterChip(
                    onClick = {
                        // 선택 상태 토글
                        selectedFilters = if (isSelected) {
                            selectedFilters - filter
                        } else {
                            selectedFilters + filter
                        }
                    },
                    label = { Text(filter) },
                    selected = isSelected,
                    // 선택 시 체크마크 아이콘 표시
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "선택됨",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }

        // Elevated 버전
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var elevated by remember { mutableStateOf(false) }

            ElevatedFilterChip(
                onClick = { elevated = !elevated },
                label = { Text("Elevated") },
                selected = elevated,
                leadingIcon = if (elevated) {
                    {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )

            Text(
                text = "(ElevatedFilterChip)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "선택된 필터: ${selectedFilters.joinToString(", ").ifEmpty { "없음" }}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 코드 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = "FilterChip 구현: 단 10줄! (vs 직접 구현 35줄)",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

// ============================================================
// 3. InputChip - 사용자 입력 표시
// ============================================================

@Composable
private fun InputChipDemo() {
    var tags by remember { mutableStateOf(listOf("Kotlin", "Android", "Compose")) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                InputChip(
                    onClick = { tags = tags - tag },  // 클릭 시 삭제
                    label = { Text(tag) },
                    selected = true,
                    // 왼쪽 아바타 (프로필 이미지 대신 첫 글자)
                    avatar = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(InputChipDefaults.AvatarSize)
                        )
                    },
                    // 오른쪽 삭제 버튼
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "삭제",
                            modifier = Modifier.size(InputChipDefaults.AvatarSize)
                        )
                    }
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

        TextButton(
            onClick = { tags = listOf("Kotlin", "Android", "Compose") }
        ) {
            Text("태그 초기화")
        }

        // 코드 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = "InputChip 구현: 단 15줄! (vs 직접 구현 40줄)",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

// ============================================================
// 4. SuggestionChip - 동적 제안
// ============================================================

@Composable
private fun SuggestionChipDemo() {
    var selectedSuggestion by remember { mutableStateOf("") }
    val suggestions = listOf("오늘 날씨 알려줘", "내일 일정", "알람 설정해줘")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "AI 챗봇 추천 질문:",
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { suggestion ->
                SuggestionChip(
                    onClick = { selectedSuggestion = suggestion },
                    label = { Text(suggestion) }
                )
            }
        }

        // Elevated 버전
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedSuggestionChip(
                onClick = { selectedSuggestion = "Elevated 제안" },
                label = { Text("Elevated 제안") }
            )

            Text(
                text = "(ElevatedSuggestionChip)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (selectedSuggestion.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = "선택한 질문: \"$selectedSuggestion\"",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// ============================================================
// 5. FlowRow + Chip 조합
// ============================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowChipDemo() {
    val categories = listOf(
        "전자기기", "의류", "식품", "가구", "도서",
        "스포츠", "화장품", "주방용품", "완구", "반려동물"
    )
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "카테고리 필터 (다중 선택):",
            style = MaterialTheme.typography.labelMedium
        )

        // FlowRow: 자동 줄바꿈 레이아웃
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = category in selectedCategories

                FilterChip(
                    onClick = {
                        selectedCategories = if (isSelected) {
                            selectedCategories - category
                        } else {
                            selectedCategories + category
                        }
                    },
                    label = { Text(category) },
                    selected = isSelected,
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "선택된 카테고리: ${selectedCategories.size}개",
                style = MaterialTheme.typography.bodySmall
            )

            TextButton(
                onClick = { selectedCategories = emptySet() }
            ) {
                Text("선택 초기화")
            }
        }

        if (selectedCategories.isNotEmpty()) {
            Text(
                text = selectedCategories.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
