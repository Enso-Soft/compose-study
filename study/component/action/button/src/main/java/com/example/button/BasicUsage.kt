package com.example.button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * BasicUsage: Material 3 Button 유형별 기본 사용법
 *
 * Material 3에서 제공하는 7가지 Button 유형을 학습합니다:
 * 1. Button (Filled) - 가장 높은 강조, 주요 액션
 * 2. FilledTonalButton - 중간 강조, 보조 액션
 * 3. ElevatedButton - 그림자 효과, 컨테이너 위에 떠있는 느낌
 * 4. OutlinedButton - 테두리만, 낮은 강조
 * 5. TextButton - 텍스트만, 가장 낮은 강조
 * 6. IconButton - 아이콘만 있는 버튼
 * 7. FloatingActionButton - 화면의 주요 액션
 */
@Composable
fun BasicUsageScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    var lastClickedButton by remember { mutableStateOf("") }

    LaunchedEffect(lastClickedButton) {
        if (lastClickedButton.isNotEmpty()) {
            snackbarHostState.showSnackbar("$lastClickedButton 클릭됨")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Button (Filled)
            item {
                ButtonSection(
                    title = "1. Button (Filled)",
                    description = "가장 높은 강조를 가진 버튼입니다. 화면에서 가장 중요한 액션에 사용합니다.",
                    useCase = "예: 저장, 확인, 제출"
                ) {
                    Button(onClick = { lastClickedButton = "저장" }) {
                        Text("저장")
                    }
                }
            }

            // 2. FilledTonalButton
            item {
                ButtonSection(
                    title = "2. FilledTonalButton",
                    description = "중간 강조를 가진 버튼입니다. 주요 버튼보다 덜 중요한 보조 액션에 사용합니다.",
                    useCase = "예: 초기화, 다음, 이전"
                ) {
                    FilledTonalButton(onClick = { lastClickedButton = "초기화" }) {
                        Text("초기화")
                    }
                }
            }

            // 3. ElevatedButton
            item {
                ButtonSection(
                    title = "3. ElevatedButton",
                    description = "그림자 효과가 있는 버튼입니다. 배경과 분리되어 떠있는 느낌을 줍니다.",
                    useCase = "예: 내보내기, 공유, 추가 기능"
                ) {
                    ElevatedButton(onClick = { lastClickedButton = "내보내기" }) {
                        Text("내보내기")
                    }
                }
            }

            // 4. OutlinedButton
            item {
                ButtonSection(
                    title = "4. OutlinedButton",
                    description = "테두리만 있는 버튼입니다. 낮은 강조가 필요할 때 사용합니다.",
                    useCase = "예: 취소, 건너뛰기, 나중에"
                ) {
                    OutlinedButton(onClick = { lastClickedButton = "취소" }) {
                        Text("취소")
                    }
                }
            }

            // 5. TextButton
            item {
                ButtonSection(
                    title = "5. TextButton",
                    description = "텍스트만 있는 버튼입니다. 가장 낮은 강조를 가지며, 부가적인 액션에 사용합니다.",
                    useCase = "예: 더보기, 자세히, 모두 보기"
                ) {
                    TextButton(onClick = { lastClickedButton = "더보기" }) {
                        Text("더보기")
                    }
                }
            }

            // 6. IconButton
            item {
                ButtonSection(
                    title = "6. IconButton",
                    description = "아이콘만 있는 버튼입니다. 공간이 제한되거나 아이콘만으로 의미 전달이 가능할 때 사용합니다.",
                    useCase = "예: 설정, 공유, 좋아요, 삭제"
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { lastClickedButton = "설정" }) {
                            Icon(Icons.Filled.Settings, contentDescription = "설정")
                        }
                        IconButton(onClick = { lastClickedButton = "공유" }) {
                            Icon(Icons.Filled.Share, contentDescription = "공유")
                        }
                        IconButton(onClick = { lastClickedButton = "좋아요" }) {
                            Icon(Icons.Filled.Favorite, contentDescription = "좋아요")
                        }
                        IconButton(onClick = { lastClickedButton = "삭제" }) {
                            Icon(Icons.Filled.Delete, contentDescription = "삭제")
                        }
                    }
                }
            }

            // 7. FloatingActionButton
            item {
                ButtonSection(
                    title = "7. FloatingActionButton (FAB)",
                    description = "화면에서 가장 중요한 단일 액션을 나타냅니다. 보통 화면 우하단에 떠있는 형태로 배치합니다.",
                    useCase = "예: 새 항목 추가, 작성, 새 메시지"
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 작은 FAB
                        SmallFloatingActionButton(
                            onClick = { lastClickedButton = "작은 FAB" }
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "추가")
                        }

                        // 기본 FAB
                        FloatingActionButton(
                            onClick = { lastClickedButton = "FAB" }
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "추가")
                        }

                        // 확장 FAB
                        ExtendedFloatingActionButton(
                            onClick = { lastClickedButton = "확장 FAB" },
                            icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                            text = { Text("작성") }
                        )
                    }
                }
            }

            // 버튼 비교표
            item {
                ButtonComparisonCard()
            }
        }
    }
}

@Composable
private fun ButtonSection(
    title: String,
    description: String,
    useCase: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = useCase,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun ButtonComparisonCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Button 선택 가이드",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            ButtonGuideRow("Button (Filled)", "주요 액션", "높음")
            ButtonGuideRow("FilledTonalButton", "보조 액션", "중간")
            ButtonGuideRow("ElevatedButton", "분리된 액션", "중간")
            ButtonGuideRow("OutlinedButton", "취소/건너뛰기", "낮음")
            ButtonGuideRow("TextButton", "부가 액션", "최소")
            ButtonGuideRow("IconButton", "공간 절약", "아이콘")
            ButtonGuideRow("FAB", "화면 대표 액션", "최고")
        }
    }
}

@Composable
private fun ButtonGuideRow(name: String, useCase: String, emphasis: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = useCase,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = emphasis,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
