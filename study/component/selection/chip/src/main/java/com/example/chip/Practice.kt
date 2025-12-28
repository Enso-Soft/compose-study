package com.example.chip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 문제: Chip 컴포넌트 활용
 *
 * 각 연습문제의 TODO를 완성하세요.
 * 정답 코드는 제공되지 않습니다 - 직접 구현해보세요!
 */
@Composable
fun PracticeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "연습 문제",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Solution 탭에서 배운 내용을 바탕으로 아래 연습문제를 해결하세요.\n" +
                    "TODO 주석을 따라 직접 코드를 작성해보세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 연습 1: 기본 FilterChip
        PracticeSection(
            number = 1,
            title = "기본 FilterChip",
            difficulty = "쉬움",
            description = "\"알림 받기\" FilterChip을 구현하세요.\n" +
                    "- 선택 시 체크마크(Done 아이콘) 표시\n" +
                    "- 클릭하면 선택 상태 토글"
        ) {
            Practice1_FilterChipBasic()
        }

        // 연습 2: InputChip 태그 삭제
        PracticeSection(
            number = 2,
            title = "InputChip 태그 삭제",
            difficulty = "중간",
            description = "태그 목록을 InputChip으로 표시하고 삭제 기능을 구현하세요.\n" +
                    "- 초기 태그: [\"Kotlin\", \"Android\", \"Compose\"]\n" +
                    "- 각 태그 오른쪽에 X 버튼(Close 아이콘)\n" +
                    "- X 버튼 클릭 시 해당 태그 삭제"
        ) {
            Practice2_InputChipDelete()
        }

        // 연습 3: 카테고리 필터 시스템
        PracticeSection(
            number = 3,
            title = "카테고리 필터 시스템",
            difficulty = "어려움",
            description = "다중 선택 가능한 카테고리 필터를 구현하세요.\n" +
                    "- 카테고리: [\"전자기기\", \"의류\", \"식품\", \"가구\", \"도서\", \"스포츠\"]\n" +
                    "- FlowRow로 자동 줄바꿈 배치\n" +
                    "- 선택된 카테고리 개수 표시\n" +
                    "- \"선택 초기화\" 버튼으로 모든 선택 해제"
        ) {
            Practice3_CategoryFilter()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PracticeSection(
    number: Int,
    title: String,
    difficulty: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "연습 $number: $title",
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    color = when (difficulty) {
                        "쉬움" -> MaterialTheme.colorScheme.primaryContainer
                        "중간" -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.tertiaryContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = difficulty,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            content()
        }
    }
}

// ============================================================
// 연습 1: 기본 FilterChip (쉬움)
// ============================================================

@Composable
fun Practice1_FilterChipBasic() {
    // =========================================================
    // TODO 1: 선택 상태를 관리할 변수를 선언하세요
    // 힌트: var selected by remember { mutableStateOf(???) }
    // =========================================================


    // =========================================================
    // TODO 2: FilterChip을 구현하세요
    // - onClick: 선택 상태를 토글 (true -> false, false -> true)
    // - label: "알림 받기" 텍스트
    // - selected: 위에서 선언한 상태 변수
    // - leadingIcon: 선택 시 Icons.Default.Done 아이콘 표시
    //               (미선택 시 null)
    //
    // 힌트:
    // FilterChip(
    //     onClick = { ... },
    //     label = { Text("...") },
    //     selected = ...,
    //     leadingIcon = if (...) { { Icon(...) } } else null
    // )
    // =========================================================

    // 임시 플레이스홀더 (TODO 완료 후 삭제하세요)
    Text(
        text = "TODO: 여기에 FilterChip을 구현하세요",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}

// ============================================================
// 연습 2: InputChip 태그 삭제 (중간)
// ============================================================

@Composable
fun Practice2_InputChipDelete() {
    // =========================================================
    // TODO 1: 태그 목록을 관리할 상태를 선언하세요
    // 힌트: var tags by remember { mutableStateOf(listOf("Kotlin", "Android", "Compose")) }
    // =========================================================


    // =========================================================
    // TODO 2: Row 또는 FlowRow로 태그들을 가로로 배치하세요
    // 힌트: Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { ... }
    // =========================================================


    // =========================================================
    // TODO 3: 각 태그를 InputChip으로 구현하세요 (tags.forEach 사용)
    // - onClick: 빈 람다 { } (또는 태그 상세보기 기능)
    // - label: 태그 텍스트
    // - selected: true
    // - trailingIcon: Icons.Default.Close 아이콘
    //   크기는 Modifier.size(InputChipDefaults.AvatarSize)
    // - onDismiss: 태그 목록에서 해당 태그 제거
    //   힌트: tags = tags.filter { it != tag } 또는 tags = tags - tag
    //
    // InputChip(
    //     onClick = { },
    //     label = { Text(tag) },
    //     selected = true,
    //     trailingIcon = { Icon(...) },
    //     onDismiss = { ... }
    // )
    // =========================================================


    // =========================================================
    // TODO 4: 모든 태그가 삭제되었을 때 안내 메시지 표시
    // 힌트: if (tags.isEmpty()) { Text("...") }
    // =========================================================


    // =========================================================
    // TODO 5: 태그 초기화 버튼 구현
    // 힌트: TextButton(onClick = { tags = listOf(...) }) { Text("태그 초기화") }
    // =========================================================


    // 임시 플레이스홀더 (TODO 완료 후 삭제하세요)
    Text(
        text = "TODO: 여기에 InputChip 목록을 구현하세요",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}

// ============================================================
// 연습 3: 카테고리 필터 시스템 (어려움)
// ============================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Practice3_CategoryFilter() {
    // =========================================================
    // TODO 1: 카테고리 목록을 정의하세요
    // val categories = listOf("전자기기", "의류", "식품", "가구", "도서", "스포츠")
    // =========================================================


    // =========================================================
    // TODO 2: 선택된 카테고리를 관리할 상태를 선언하세요
    // 힌트: Set<String> 타입을 사용하면 중복을 방지할 수 있습니다
    // var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    // =========================================================


    // =========================================================
    // TODO 3: 선택된 카테고리 개수를 표시하세요
    // 힌트: Text("선택된 카테고리: ${selectedCategories.size}개")
    // =========================================================


    // =========================================================
    // TODO 4: FlowRow로 FilterChip 목록을 구현하세요
    //
    // FlowRow(
    //     modifier = Modifier.fillMaxWidth(),
    //     horizontalArrangement = Arrangement.spacedBy(8.dp),
    //     verticalArrangement = Arrangement.spacedBy(8.dp)
    // ) {
    //     categories.forEach { category ->
    //         val isSelected = category in selectedCategories
    //
    //         FilterChip(
    //             onClick = {
    //                 // 선택 상태 토글
    //                 selectedCategories = if (isSelected) {
    //                     selectedCategories - category  // Set에서 제거
    //                 } else {
    //                     selectedCategories + category  // Set에 추가
    //                 }
    //             },
    //             label = { Text(category) },
    //             selected = isSelected,
    //             leadingIcon = if (isSelected) { { Icon(...) } } else null
    //         )
    //     }
    // }
    // =========================================================


    // =========================================================
    // TODO 5: "선택 초기화" 버튼을 구현하세요
    // 힌트: 클릭 시 selectedCategories = emptySet()
    // =========================================================


    // =========================================================
    // TODO 6 (선택사항): 선택된 카테고리 목록을 텍스트로 표시
    // 힌트: if (selectedCategories.isNotEmpty()) {
    //          Text(selectedCategories.joinToString(", "))
    //       }
    // =========================================================


    // 임시 플레이스홀더 (TODO 완료 후 삭제하세요)
    Text(
        text = "TODO: 여기에 카테고리 필터 시스템을 구현하세요",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}
