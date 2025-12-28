package com.example.snackbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 해결책 화면
 *
 * Snackbar를 사용하면 Context 없이 피드백을 제공하고,
 * 액션 버튼으로 "실행 취소" 기능을 구현할 수 있습니다.
 */
@Composable
fun SolutionScreen() {
    // Step 1: 코루틴 스코프와 SnackbarHostState 준비
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // 할일 목록 상태
    var items by remember {
        mutableStateOf(listOf("장보기", "운동하기", "책 읽기"))
    }
    var lastDeletedItem by remember { mutableStateOf<String?>(null) }
    var lastDeletedIndex by remember { mutableIntStateOf(-1) }

    // Step 2: Scaffold에 SnackbarHost 배치
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                        text = "해결책: Snackbar 사용",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Snackbar는 '실행 취소' 버튼을 제공하여 " +
                                "사용자가 실수로 삭제한 항목을 복구할 수 있습니다!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // 할일 목록 카드
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "할일 목록 (Snackbar 사용)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (items.isEmpty()) {
                        Text(
                            text = "모든 항목이 삭제되었습니다.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        items.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = item)
                                IconButton(
                                    onClick = {
                                        // 삭제 전 저장 (복구를 위해)
                                        lastDeletedItem = item
                                        lastDeletedIndex = index
                                        items = items - item

                                        // Step 3: Snackbar 표시 + 액션 처리
                                        scope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "$item 삭제됨",
                                                actionLabel = "실행 취소",
                                                duration = SnackbarDuration.Short
                                            )

                                            // Step 4: SnackbarResult 처리
                                            when (result) {
                                                SnackbarResult.ActionPerformed -> {
                                                    // 실행 취소! 원래 위치에 복구
                                                    lastDeletedItem?.let { deletedItem ->
                                                        val newList = items.toMutableList()
                                                        val insertIndex = lastDeletedIndex
                                                            .coerceIn(0, newList.size)
                                                        newList.add(insertIndex, deletedItem)
                                                        items = newList
                                                    }
                                                }
                                                SnackbarResult.Dismissed -> {
                                                    // 완전 삭제 (아무것도 하지 않음)
                                                }
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "삭제",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 리셋 버튼
                    OutlinedButton(
                        onClick = {
                            items = listOf("장보기", "운동하기", "책 읽기")
                            lastDeletedItem = null
                            lastDeletedIndex = -1
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("목록 초기화")
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
                    Text("1. rememberCoroutineScope()로 스코프 획득")
                    Text("2. remember { SnackbarHostState() }로 상태 생성")
                    Text("3. Scaffold의 snackbarHost에 SnackbarHost 배치")
                    Text("4. scope.launch { showSnackbar() }로 표시")
                    Text("5. SnackbarResult로 사용자 응답 처리")
                }
            }

            // 올바른 코드 예시
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "사용된 코드 (Snackbar)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
// Step 1: 준비
val scope = rememberCoroutineScope()
val snackbarHostState = remember {
    SnackbarHostState()
}

// Step 2: Scaffold에 배치
Scaffold(
    snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }
) {
    // Step 3: 버튼 클릭 시 표시
    Button(onClick = {
        scope.launch {
            val result = snackbarHostState
                .showSnackbar(
                    message = "삭제됨",
                    actionLabel = "실행 취소",
                    duration = SnackbarDuration.Short
                )

            // Step 4: 결과 처리
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    // 복구 로직
                }
                SnackbarResult.Dismissed -> {
                    // 완전 삭제
                }
            }
        }
    })
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Duration 설명 카드
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SnackbarDuration 옵션",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Short: 약 4초 (기본값)")
                    Text("Long: 약 10초")
                    Text("Indefinite: 수동으로 닫을 때까지")
                }
            }
        }
    }
}
