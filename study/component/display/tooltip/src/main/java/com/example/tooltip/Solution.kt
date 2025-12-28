package com.example.tooltip

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
import kotlinx.coroutines.launch

/**
 * 해결책 화면
 *
 * Tooltip을 사용하여 아이콘 버튼에 설명을 추가하는 방법을 시연합니다.
 * PlainTooltip, RichTooltip, TooltipState 사용법을 배웁니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Tooltip 사용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "각 아이콘을 길게 누르면 설명이 나타납니다!\n" +
                            "이제 클릭하지 않아도 기능을 알 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 1. PlainTooltip 예제
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. PlainTooltip - 간단한 설명",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "아이콘을 길게 누르면 짧은 텍스트 설명이 나타납니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 저장 버튼 + PlainTooltip
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("저장")
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "저장"
                            )
                        }
                    }

                    // 공유 버튼 + PlainTooltip
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("공유")
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "공유"
                            )
                        }
                    }

                    // 서식 버튼 + PlainTooltip
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("서식 설정")
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "서식 설정"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아이콘을 길게 눌러보세요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        // PlainTooltip 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "PlainTooltip 코드",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
TooltipBox(
    positionProvider = TooltipDefaults
        .rememberPlainTooltipPositionProvider(),
    tooltip = {
        PlainTooltip { Text("저장") }
    },
    state = rememberTooltipState()
) {
    IconButton(onClick = { }) {
        Icon(Icons.Default.Save, "저장")
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 2. RichTooltip 예제
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. RichTooltip - 상세 설명",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "제목, 본문, 액션 버튼을 포함한 상세 Tooltip입니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 설정 버튼 + RichTooltip
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                        tooltip = {
                            RichTooltip(
                                title = { Text("설정") },
                                action = {
                                    TextButton(onClick = { }) {
                                        Text("설정 열기")
                                    }
                                }
                            ) {
                                Text("글꼴, 여백, 테마 등을 변경할 수 있습니다")
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "설정"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "설정 아이콘을 길게 눌러보세요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        // RichTooltip 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "RichTooltip 코드",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
TooltipBox(
    positionProvider = TooltipDefaults
        .rememberRichTooltipPositionProvider(),
    tooltip = {
        RichTooltip(
            title = { Text("설정") },
            action = {
                TextButton(onClick = { }) {
                    Text("설정 열기")
                }
            }
        ) {
            Text("글꼴, 여백, 테마 등을...")
        }
    },
    state = rememberTooltipState()
) {
    IconButton(onClick = { }) {
        Icon(Icons.Default.Settings, "설정")
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 3. TooltipState 예제
        val manualTooltipState = rememberTooltipState()

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. TooltipState - 프로그래밍 방식 제어",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "버튼 클릭으로 Tooltip을 표시하거나 숨길 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tooltip이 적용된 아이콘
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("버튼으로 열림!")
                            }
                        },
                        state = manualTooltipState
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "정보",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // 표시 버튼
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                manualTooltipState.show()
                            }
                        }
                    ) {
                        Text("Tooltip 표시")
                    }

                    // 숨김 버튼
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                manualTooltipState.dismiss()
                            }
                        }
                    ) {
                        Text("숨기기")
                    }
                }
            }
        }

        // TooltipState 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TooltipState 코드",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
val tooltipState = rememberTooltipState()
val scope = rememberCoroutineScope()

// Tooltip 표시
Button(onClick = {
    scope.launch { tooltipState.show() }
}) {
    Text("Tooltip 표시")
}

// Tooltip 숨기기
Button(onClick = {
    scope.launch { tooltipState.dismiss() }
}) {
    Text("숨기기")
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                KeyPointItem(
                    number = 1,
                    text = "PlainTooltip은 간단한 텍스트 설명에 사용"
                )
                KeyPointItem(
                    number = 2,
                    text = "RichTooltip은 제목, 본문, 액션이 필요할 때 사용"
                )
                KeyPointItem(
                    number = 3,
                    text = "TooltipState로 프로그래밍 방식 제어 가능"
                )
                KeyPointItem(
                    number = 4,
                    text = "contentDescription도 함께 설정하여 접근성 향상"
                )
            }
        }
    }
}

@Composable
private fun KeyPointItem(number: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number. ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
