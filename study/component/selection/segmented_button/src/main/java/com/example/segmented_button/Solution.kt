package com.example.segmented_button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 해결책: SegmentedButton 사용
 *
 * Material 3의 SegmentedButton을 사용하면:
 * 1. 모서리 처리가 자동으로 됨 (itemShape)
 * 2. 선택 상태 스타일링이 자동으로 적용됨
 * 3. 테두리 겹침 문제가 없음
 * 4. 접근성(Accessibility)이 자동으로 지원됨
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Solution: SegmentedButton",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 해결책 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SegmentedButton의 장점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- 모서리 처리 자동 (itemShape)")
                Text("- 선택 상태 스타일링 자동")
                Text("- 테두리 겹침 문제 없음")
                Text("- 접근성 자동 지원")
                Text("- Material Design 3 가이드라인 준수")
            }
        }

        HorizontalDivider()

        // 예제 1: SingleChoice - 레이블만
        ExampleSection(
            title = "예제 1: 단일 선택 (레이블만)",
            description = "SingleChoiceSegmentedButtonRow를 사용하여 하나만 선택할 수 있는 UI를 만듭니다."
        ) {
            SingleChoiceLabelOnlyExample()
        }

        HorizontalDivider()

        // 예제 2: SingleChoice - 아이콘만
        ExampleSection(
            title = "예제 2: 단일 선택 (아이콘만)",
            description = "아이콘만 사용하여 보기 모드를 선택합니다. 공간이 좁을 때 유용합니다."
        ) {
            SingleChoiceIconOnlyExample()
        }

        HorizontalDivider()

        // 예제 3: MultiChoice
        ExampleSection(
            title = "예제 3: 다중 선택",
            description = "MultiChoiceSegmentedButtonRow를 사용하여 여러 개를 동시에 선택할 수 있습니다."
        ) {
            MultiChoiceExample()
        }

        HorizontalDivider()

        // 예제 4: 아이콘 + 레이블
        ExampleSection(
            title = "예제 4: 아이콘 + 레이블",
            description = "아이콘과 레이블을 함께 사용하여 더 명확한 UI를 만듭니다."
        ) {
            IconWithLabelExample()
        }

        // 핵심 코드 비교
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 코드 (단 10줄!)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
SingleChoiceSegmentedButtonRow {
    options.forEachIndexed { index, label ->
        SegmentedButton(
            selected = index == selectedIndex,
            onClick = { selectedIndex = index },
            shape = SegmentedButtonDefaults.itemShape(
                index = index,
                count = options.size
            )
        ) {
            Text(label)
        }
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun ExampleSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}

/**
 * 예제 1: 단일 선택 - 레이블만
 *
 * 가장 기본적인 형태의 Segmented Button입니다.
 * 정렬 방식 선택 같은 상호 배타적 옵션에 사용합니다.
 */
@Composable
fun SingleChoiceLabelOnlyExample() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("날짜순", "이름순", "크기순")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    )
                ) {
                    Text(label)
                }
            }
        }

        Text(
            text = "선택됨: ${options[selectedIndex]}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 예제 2: 단일 선택 - 아이콘만
 *
 * 공간이 좁거나 시각적 표현이 더 적합할 때 사용합니다.
 * 보기 모드(리스트, 그리드) 선택에 자주 사용됩니다.
 */
@Composable
fun SingleChoiceIconOnlyExample() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(
        Icons.AutoMirrored.Filled.ViewList to "리스트",
        Icons.Default.GridView to "그리드",
        Icons.Default.ViewModule to "모듈"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, (icon, description) ->
                SegmentedButton(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = description
                    )
                }
            }
        }

        Text(
            text = "보기 모드: ${options[selectedIndex].second}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 예제 3: 다중 선택
 *
 * 여러 옵션을 동시에 선택할 수 있습니다.
 * 필터 선택 같은 조합 가능한 옵션에 사용합니다.
 *
 * checked/onCheckedChange를 사용하는 것이 SingleChoice와의 차이점입니다.
 */
@Composable
fun MultiChoiceExample() {
    // 각 옵션의 선택 상태를 개별적으로 관리
    val selectedOptions = remember { mutableStateListOf(false, false, false) }
    val options = listOf("인기", "최신", "할인")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MultiChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    checked = selectedOptions[index],
                    onCheckedChange = { selectedOptions[index] = it },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    icon = {
                        // 선택 상태에 따라 체크 아이콘 표시
                        SegmentedButtonDefaults.Icon(active = selectedOptions[index])
                    }
                ) {
                    Text(label)
                }
            }
        }

        // 선택된 필터 표시
        val selectedFilters = options.filterIndexed { index, _ -> selectedOptions[index] }
        Text(
            text = if (selectedFilters.isEmpty()) {
                "선택된 필터 없음"
            } else {
                "적용된 필터: ${selectedFilters.joinToString(", ")}"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 예제 4: 아이콘 + 레이블
 *
 * 아이콘과 텍스트를 함께 사용하여 더 명확한 UI를 만듭니다.
 * Row로 아이콘과 텍스트를 배치합니다.
 */
@Composable
fun IconWithLabelExample() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(
        Triple(Icons.Default.Home, "홈", "홈 화면"),
        Triple(Icons.Default.Search, "검색", "검색 화면"),
        Triple(Icons.Default.Person, "프로필", "프로필 화면")
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, (icon, label, _) ->
                SegmentedButton(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(label)
                    }
                }
            }
        }

        Text(
            text = "현재 화면: ${options[selectedIndex].third}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
