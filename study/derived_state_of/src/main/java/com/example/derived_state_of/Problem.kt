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
 * 문제가 있는 코드 - derivedStateOf 없이 스크롤 상태 처리
 *
 * 이 코드의 문제점:
 * 1. 스크롤할 때마다 firstVisibleItemIndex가 변경됨
 * 2. showButton 조건을 매번 다시 평가
 * 3. 값이 true로 동일해도 불필요한 Recomposition 발생
 * 4. Recomposition 카운터가 스크롤만 해도 계속 증가!
 */
@Composable
fun ProblemScreen() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Recomposition 횟수 추적 (참조 객체 패턴 - 무한 루프 방지)
    val recompositionRef = remember { object { var count = 0 } }
    var displayRecompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionRef.count++
        // 즉시 표시 업데이트 (derivedStateOf 학습에서는 비교가 중요하므로)
    }
    LaunchedEffect(listState.firstVisibleItemIndex) {
        displayRecompositionCount = recompositionRef.count
    }

    // 문제: derivedStateOf 없이 직접 계산
    // 스크롤할 때마다 이 조건이 다시 평가되고 Recomposition 발생!
    val showButton = listState.firstVisibleItemIndex > 0

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 상태 표시 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Problem: derivedStateOf 미사용",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Recomposition 횟수: $displayRecompositionCount")
                    Text("firstVisibleItemIndex: ${listState.firstVisibleItemIndex}")
                    Text("showButton: $showButton")
                }
            }

            // 문제 설명
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
                        text = "왜 문제인가?",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("1. 스크롤할 때마다 index 변경", style = MaterialTheme.typography.bodySmall)
                    Text("2. showButton은 true로 동일", style = MaterialTheme.typography.bodySmall)
                    Text("3. 그런데도 Recomposition 발생!", style = MaterialTheme.typography.bodySmall)
                    Text("4. 스크롤하면서 카운터 확인해보세요", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "맨 위로"
                )
            }
        }
    }
}
