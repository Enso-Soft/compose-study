package com.example.flow_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 올바른 코드 - FlowRow/FlowColumn 사용
 *
 * FlowRow/FlowColumn을 사용하면:
 * 1. 공간이 부족하면 자동으로 다음 줄/열로 래핑
 * 2. Arrangement 옵션으로 간격과 배치 제어
 * 3. maxItemsInEachRow로 행당 아이템 수 제한
 * 4. Modifier.weight()로 남은 공간 분배
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SolutionScreen() {
    val tags = listOf(
        "Kotlin", "Jetpack Compose", "Android", "Material Design",
        "Coroutines", "Flow", "ViewModel", "Navigation",
        "Room", "Retrofit", "Hilt", "DataStore"
    )

    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    var selectedArrangement by remember { mutableStateOf(0) }
    var maxItemsPerRow by remember { mutableIntStateOf(Int.MAX_VALUE) }

    val arrangements = listOf(
        "spacedBy(8.dp)" to Arrangement.spacedBy(8.dp),
        "Start" to Arrangement.Start,
        "Center" to Arrangement.Center,
        "End" to Arrangement.End,
        "SpaceEvenly" to Arrangement.SpaceEvenly,
        "SpaceBetween" to Arrangement.SpaceBetween,
        "SpaceAround" to Arrangement.SpaceAround
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. 기본 FlowRow 사용
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. FlowRow 기본 사용 (태그 선택)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "태그를 선택/해제해보세요. 자동으로 래핑됩니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        FilterChip(
                            selected = selectedTags.contains(tag),
                            onClick = {
                                selectedTags = if (selectedTags.contains(tag)) {
                                    selectedTags - tag
                                } else {
                                    selectedTags + tag
                                }
                            },
                            label = { Text(tag) }
                        )
                    }
                }

                if (selectedTags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "선택된 태그: ${selectedTags.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Arrangement 옵션 비교
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. Arrangement 옵션 비교",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Arrangement 선택
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    arrangements.forEachIndexed { index, (name, _) ->
                        FilterChip(
                            selected = selectedArrangement == index,
                            onClick = { selectedArrangement = index },
                            label = { Text(name, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 선택된 Arrangement 적용
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = arrangements[selectedArrangement].second,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(6) { index ->
                            Box(
                                modifier = Modifier
                                    .size(width = (50 + index * 10).dp, height = 40.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${index + 1}",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "현재: ${arrangements[selectedArrangement].first}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. maxItemsInEachRow 사용
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. maxItemsInEachRow + weight",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 슬라이더로 maxItems 조절
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "행당 최대: ${if (maxItemsPerRow == Int.MAX_VALUE) "무제한" else maxItemsPerRow}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = if (maxItemsPerRow == Int.MAX_VALUE) 6f else maxItemsPerRow.toFloat(),
                        onValueChange = {
                            maxItemsPerRow = if (it >= 6f) Int.MAX_VALUE else it.toInt()
                        },
                        valueRange = 2f..6f,
                        steps = 3,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // maxItemsInEachRow 적용된 FlowRow
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = maxItemsPerRow
                ) {
                    repeat(9) { index ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Item ${index + 1}",
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "weight(1f)로 각 행에서 균등하게 공간 분배",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. FlowColumn 사용
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "4. FlowColumn (세로 래핑)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "세로 공간이 부족하면 다음 열로 래핑됩니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // FlowColumn: 고정 높이 필요
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    FlowColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachColumn = 4
                    ) {
                        val colors = listOf(
                            Color(0xFF6750A4), Color(0xFF7D5260), Color(0xFF625B71),
                            Color(0xFF3DDC84), Color(0xFF0D47A1), Color(0xFFE91E63),
                            Color(0xFFFF5722), Color(0xFF795548), Color(0xFF607D8B),
                            Color(0xFF9C27B0), Color(0xFF00BCD4), Color(0xFF8BC34A)
                        )
                        colors.forEachIndexed { index, color ->
                            Box(
                                modifier = Modifier
                                    .size(width = 60.dp, height = 35.dp)
                                    .background(color, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${index + 1}",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "maxItemsInEachColumn = 4 (열당 최대 4개)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 핵심 포인트 요약
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- FlowRow: 가로로 배치, 부족하면 아래로 래핑")
                Text("- FlowColumn: 세로로 배치, 부족하면 오른쪽으로 래핑")
                Text("- Arrangement: 간격과 배치 방식 제어")
                Text("- maxItemsInEachRow/Column: 줄당 최대 아이템 수")
                Text("- weight: 해당 줄의 남은 공간을 분배")
            }
        }
    }
}
