package com.example.segmented_button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

/**
 * 문제 상황: Row + Button으로 탭 UI를 직접 구현하려는 경우
 *
 * 이 코드는 SegmentedButton 없이 직접 구현할 때 발생하는
 * 여러 문제점들을 보여줍니다:
 *
 * 1. 모서리 처리가 복잡함 (첫 번째, 마지막, 중간 버튼 각각 다르게)
 * 2. 선택 상태에 따른 색상 처리를 수동으로 해야 함
 * 3. 테두리가 겹치는 문제 발생
 * 4. 접근성(Accessibility) 처리가 누락됨
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Problem: 직접 구현의 어려움",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        // 문제 상황 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "시나리오",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "정렬 방식을 선택하는 탭 UI를 만들려고 합니다.\n" +
                            "Row와 Button을 조합해서 직접 구현해봅시다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 직접 구현한 탭 UI (문제가 있는 버전)
        Text(
            text = "직접 구현한 버전:",
            style = MaterialTheme.typography.titleSmall
        )

        ManualSegmentedButton()

        // 문제점 나열
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점들",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem(
                    number = 1,
                    title = "모서리 처리가 복잡함",
                    description = "첫 번째 버튼은 왼쪽만 둥글게, " +
                            "마지막 버튼은 오른쪽만 둥글게, " +
                            "중간 버튼은 직각으로 처리해야 합니다."
                )

                ProblemItem(
                    number = 2,
                    title = "선택 상태 색상 수동 처리",
                    description = "선택된 버튼과 선택되지 않은 버튼의 " +
                            "배경색, 글자색을 모두 직접 지정해야 합니다."
                )

                ProblemItem(
                    number = 3,
                    title = "테두리 겹침 문제",
                    description = "버튼 사이의 테두리가 2중으로 보이는 문제가 발생합니다. " +
                            "offset이나 zIndex로 수동 처리가 필요합니다."
                )

                ProblemItem(
                    number = 4,
                    title = "접근성 미지원",
                    description = "스크린 리더가 '라디오 버튼 그룹'으로 인식하지 못합니다. " +
                            "semantics를 수동으로 설정해야 합니다."
                )
            }
        }

        // 코드 미리보기
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.1f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 구현 시 필요한 코드량:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 모서리 처리
val shape = when (index) {
    0 -> RoundedCornerShape(
        topStart = 50.percent,
        bottomStart = 50.percent
    )
    options.lastIndex -> RoundedCornerShape(
        topEnd = 50.percent,
        bottomEnd = 50.percent
    )
    else -> RectangleShape
}

// 색상 처리
val containerColor = if (isSelected)
    MaterialTheme.colorScheme.secondaryContainer
else Color.Transparent

// 테두리 겹침 해결
val offsetX = if (index > 0) (-1).dp else 0.dp
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        Text(
            text = "Solution 탭에서 더 쉬운 방법을 확인하세요!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 직접 구현한 Segmented Button (문제점이 있는 버전)
 *
 * 실제로 동작하지만, 코드가 복잡하고 유지보수가 어렵습니다.
 */
@Composable
fun ManualSegmentedButton() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("날짜순", "이름순", "크기순")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        options.forEachIndexed { index, label ->
            val isSelected = selectedIndex == index

            // 문제 1: 모서리 처리가 복잡함
            val shape = when (index) {
                0 -> RoundedCornerShape(
                    topStart = 50.dp,
                    bottomStart = 50.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
                options.lastIndex -> RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 50.dp,
                    bottomEnd = 50.dp
                )
                else -> RectangleShape
            }

            // 문제 2: 선택 상태 색상을 수동으로 처리
            val containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                Color.Transparent
            }

            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            }

            // 문제 3: 테두리 겹침 - offset으로 해결 시도
            val offsetX = if (index > 0) (-1).dp else 0.dp

            OutlinedButton(
                onClick = { selectedIndex = index },
                shape = shape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .offset(x = offsetX)
            ) {
                Text(label)
            }
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number. ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
