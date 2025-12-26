package com.example.pull_to_refresh

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 연습 1: 기본 PullToRefreshBox 구현 (초급)
 *
 * 목표: 기본적인 PullToRefreshBox 사용법 익히기
 *
 * 요구사항:
 * 1. 숫자 목록을 표시하는 LazyColumn
 * 2. 당겨서 새로고침 시 랜덤 숫자 추가
 * 3. isRefreshing 상태 올바르게 관리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicPullToRefresh() {
    var numbers by remember { mutableStateOf((1..5).toList()) }

    // TODO: isRefreshing 상태를 추가하세요
    // var isRefreshing by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. PullToRefreshBox로 LazyColumn을 감싸세요\n" +
                            "2. isRefreshing과 onRefresh 파라미터 설정\n" +
                            "3. onRefresh에서 코루틴으로 데이터 추가",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "숫자 목록 (당겨서 새로고침)",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: PullToRefreshBox로 감싸세요
        // PullToRefreshBox(
        //     isRefreshing = isRefreshing,
        //     onRefresh = {
        //         scope.launch {
        //             isRefreshing = true
        //             delay(1000)
        //             numbers = listOf((1..100).random()) + numbers
        //             isRefreshing = false
        //         }
        //     }
        // ) {
        //     LazyColumn { ... }
        // }

        // 정답:
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(1000)
                    numbers = listOf((1..100).random()) + numbers
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(numbers) { number ->
                    ListItem(
                        headlineContent = { Text("숫자: $number") },
                        leadingContent = {
                            Text(
                                text = "${numbers.indexOf(number) + 1}",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }
        }
    }
}

/**
 * 연습 2: 인디케이터 색상 커스터마이징 (중급)
 *
 * 목표: rememberPullToRefreshState()를 사용해 인디케이터 스타일 변경
 *
 * 요구사항:
 * 1. rememberPullToRefreshState() 사용
 * 2. indicator 파라미터로 Indicator 컴포저블 제공
 * 3. containerColor와 color 속성으로 색상 변경
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_CustomColor() {
    var items by remember { mutableStateOf(listOf("항목 A", "항목 B", "항목 C", "항목 D", "항목 E")) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // TODO: rememberPullToRefreshState() 추가
    // val state = rememberPullToRefreshState()
    val state = rememberPullToRefreshState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. rememberPullToRefreshState()로 state 생성\n" +
                            "2. indicator 파라미터에 Indicator 컴포저블 제공\n" +
                            "3. containerColor: 배경색, color: 인디케이터 색",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "커스텀 색상 인디케이터",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: indicator 파라미터 추가하기
        // PullToRefreshBox(
        //     ...,
        //     state = state,
        //     indicator = {
        //         Indicator(
        //             modifier = Modifier.align(Alignment.TopCenter),
        //             isRefreshing = isRefreshing,
        //             state = state,
        //             containerColor = Color(0xFF4CAF50),  // 초록색 배경
        //             color = Color.White                  // 흰색 인디케이터
        //         )
        //     }
        // )

        // 정답:
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(1500)
                    items = listOf("새 항목 ${items.size + 1}") + items
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize(),
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = state,
                    containerColor = Color(0xFF4CAF50),  // 초록색 배경
                    color = Color.White                   // 흰색 인디케이터
                )
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items) { item ->
                    ListItem(
                        headlineContent = { Text(item) }
                    )
                }
            }
        }
    }
}

/**
 * 연습 3: 커스텀 인디케이터 만들기 (고급)
 *
 * 목표: distanceFraction을 활용한 완전 커스텀 인디케이터
 *
 * 요구사항:
 * 1. 드래그 거리에 따라 아이콘 크기/투명도 변화
 * 2. 새로고침 중에는 회전 애니메이션
 * 3. Crossfade로 상태 전환
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_CustomIndicator() {
    var items by remember { mutableStateOf((1..10).map { "아이템 $it" }) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()

    // 회전 애니메이션 (무한 반복)
    val infiniteTransition = rememberInfiniteTransition(label = "refresh")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. state.distanceFraction: 드래그 진행률 (0~1)\n" +
                            "2. graphicsLayer { scaleX, scaleY, alpha } 사용\n" +
                            "3. Crossfade로 드래그/로딩 상태 전환",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "커스텀 인디케이터 (드래그하면 커지고, 로딩 시 회전)",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: 커스텀 인디케이터 구현
        // indicator = {
        //     val progress = state.distanceFraction.coerceIn(0f, 1f)
        //
        //     Box(modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp)) {
        //         Crossfade(targetState = isRefreshing) { refreshing ->
        //             if (refreshing) {
        //                 // 회전하는 아이콘
        //                 Icon(
        //                     Icons.Default.Refresh,
        //                     contentDescription = null,
        //                     modifier = Modifier
        //                         .size(32.dp)
        //                         .rotate(rotation)
        //                 )
        //             } else {
        //                 // 크기가 변하는 아이콘
        //                 Icon(
        //                     Icons.Default.Refresh,
        //                     contentDescription = null,
        //                     modifier = Modifier
        //                         .size(32.dp)
        //                         .graphicsLayer {
        //                             scaleX = progress
        //                             scaleY = progress
        //                             alpha = progress
        //                         }
        //                 )
        //             }
        //         }
        //     }
        // }

        // 정답:
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(2000)
                    items = listOf("새로운 아이템!") + items
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize(),
            state = state,
            indicator = {
                val progress = state.distanceFraction.coerceIn(0f, 1f)

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                ) {
                    Crossfade(
                        targetState = isRefreshing,
                        label = "indicator"
                    ) { refreshing ->
                        if (refreshing) {
                            // 로딩 중: 회전하는 아이콘
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "로딩 중",
                                modifier = Modifier
                                    .size(32.dp)
                                    .rotate(rotation),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            // 드래그 중: 크기와 투명도가 변하는 아이콘
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "당겨서 새로고침",
                                modifier = Modifier
                                    .size(32.dp)
                                    .graphicsLayer {
                                        scaleX = progress
                                        scaleY = progress
                                        alpha = progress
                                    },
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items) { item ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = item,
                                fontWeight = if (item == "새로운 아이템!") FontWeight.Bold else FontWeight.Normal,
                                color = if (item == "새로운 아이템!") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }
        }
    }
}
