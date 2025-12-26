package com.example.derived_state_of

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 올바른 코드 - derivedStateOf 사용
 *
 * derivedStateOf를 사용하면:
 * 1. firstVisibleItemIndex가 계속 변해도 showButton 결과가 같으면 Recomposition 스킵
 * 2. showButton이 false→true 또는 true→false로 실제 변경될 때만 Recomposition
 * 3. 성능 최적화 및 배터리 절약
 */
@Composable
fun SolutionScreen() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var recompositionCount by remember { mutableIntStateOf(0) }
    var buttonStateChangeCount by remember { mutableIntStateOf(0) }

    // 해결책: derivedStateOf 사용!
    // showButton 값이 실제로 변경될 때만 Recomposition 트리거
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    // showButton 변경 횟수 추적
    LaunchedEffect(showButton) {
        buttonStateChangeCount++
    }

    // Recomposition 횟수 추적
    SideEffect {
        recompositionCount++
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 상태 표시 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Solution: derivedStateOf 사용",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Recomposition 횟수: $recompositionCount")
                    Text("showButton 변경 횟수: $buttonStateChangeCount")
                    Text("firstVisibleItemIndex: ${listState.firstVisibleItemIndex}")
                    Text("showButton: $showButton")
                }
            }

            // 핵심 포인트
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "핵심 포인트",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "스크롤해도 Recomposition 횟수가\n거의 증가하지 않습니다!",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "showButton 변경은 맨 위↔아래 이동 시만",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 코드 예시
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "사용된 코드",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = """
val showButton by remember {
    derivedStateOf {
        listState.firstVisibleItemIndex > 0
    }
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            // 스크롤 가능한 리스트
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(100) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "아이템 ${index + 1}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        // "맨 위로" 버튼
        AnimatedVisibility(
            visible = showButton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "맨 위로"
                )
            }
        }
    }
}
