package com.example.pausable_composition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pausable_composition.ui.theme.PerformanceBad
import com.example.pausable_composition.ui.theme.PerformanceGood
import com.example.pausable_composition.ui.theme.PerformanceWarning
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 연습 문제 1: Jank 측정 도구 만들기 (쉬움)
 *
 * 프레임 렌더링 시간을 측정하고 표시하는 UI를 만들어봅니다.
 */
@Composable
fun Practice1_JankMeasurement() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: Jank 측정 도구 만들기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        프레임 렌더링 시간을 측정하고 표시하는 UI를 만들어보세요.

                        요구사항:
                        - 현재 프레임 렌더링 시간을 실시간으로 표시
                        - 16ms 이하면 녹색, 초과하면 빨간색으로 표시
                    """.trimIndent()
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- System.nanoTime()으로 시간 측정")
                Text("- SideEffect로 recomposition마다 실행")
                Text("- 나노초를 밀리초로 변환: / 1_000_000")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "구현 결과",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // TODO: 여기에 직접 구현해보세요!
                Practice1_Exercise()
            }
        }

        // 정답 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = """
@Composable
fun FrameTimeMonitor() {
    var frameTime by remember {
        mutableLongStateOf(0L)
    }
    var lastFrameTime by remember {
        mutableLongStateOf(System.nanoTime())
    }

    // 매 recomposition마다 실행
    SideEffect {
        val current = System.nanoTime()
        // 나노초 -> 밀리초 변환
        frameTime = (current - lastFrameTime)
            / 1_000_000
        lastFrameTime = current
    }

    Text(
        text = "Frame: ${'$'}{frameTime}ms",
        color = if (frameTime <= 16)
            Color.Green
        else
            Color.Red
    )
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

/**
 * 연습 1 연습 영역
 */
@Composable
private fun Practice1_Exercise() {
    // TODO: 프레임 렌더링 시간 측정 UI를 구현하세요
    // 요구사항:
    // 1. frameTime, lastFrameTime 상태 변수 선언
    // 2. SideEffect 블록에서 시간 측정
    // 3. System.nanoTime()으로 현재 시간 획득
    // 4. 나노초 -> 밀리초 변환: / 1_000_000
    // 5. 16ms 이하면 녹색, 초과하면 빨간색으로 표시

    // TODO: 상태 변수 선언
    // var frameTime by remember { mutableLongStateOf(0L) }
    // var lastFrameTime by remember { mutableLongStateOf(System.nanoTime()) }

    // TODO: SideEffect 블록 구현
    // SideEffect {
    //     val current = System.nanoTime()
    //     frameTime = (current - lastFrameTime) / 1_000_000
    //     lastFrameTime = current
    // }

    var counter by remember { mutableIntStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: 프레임 시간 표시
        Text(
            text = "프레임 시간을 측정해보세요!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 리컴포지션 유발 버튼
        Button(onClick = { counter++ }) {
            Text("Recomposition 유발 ($counter)")
        }
    }
}

/**
 * 연습 문제 2: Prefetch 상태 모니터링 (중간)
 *
 * LazyColumn의 현재 상태를 실시간으로 모니터링합니다.
 */
@Composable
fun Practice2_PrefetchMonitoring() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: Prefetch 상태 모니터링",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        LazyColumn의 현재 상태를 실시간으로 모니터링하세요.

                        요구사항:
                        - 현재 보이는 아이템 범위 표시 (첫 번째 ~ 마지막)
                        - 전체 아이템 개수 표시
                        - 스크롤하면서 값 변화 관찰
                    """.trimIndent()
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- rememberLazyListState() 사용")
                Text("- listState.layoutInfo.visibleItemsInfo 활용")
                Text("- listState.layoutInfo.totalItemsCount 활용")
            }
        }

        // 연습 영역
        Practice2_Exercise()
    }
}

/**
 * 연습 2 연습 영역
 */
@Composable
private fun Practice2_Exercise() {
    // TODO: LazyColumn의 상태를 실시간으로 모니터링하세요
    // 요구사항:
    // 1. rememberLazyListState()로 리스트 상태 관리
    // 2. listState.layoutInfo.visibleItemsInfo로 보이는 아이템 정보 획득
    // 3. 보이는 범위 (첫 번째 ~ 마지막) 표시
    // 4. 전체 아이템 개수 표시

    // TODO: 상태 선언
    // val listState = rememberLazyListState()
    // val visibleItems = listState.layoutInfo.visibleItemsInfo

    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        // 상태 정보 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "LazyColumn 상태",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // TODO: 보이는 범위, 보이는 개수, 전체 아이템 개수를 표시하세요
                Text(
                    text = "스크롤하면서 상태 변화를 관찰하세요!",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pausable Composition은 보이는 영역 바깥의 아이템을 미리 구성합니다 (prefetch)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 리스트
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(100) { index ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outline)
                        ) {
                            Text(
                                text = "$index",
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(text = "아이템 $index")
                    }
                }
            }
        }
    }
}

/**
 * 연습 문제 3: 복잡도에 따른 성능 비교 (어려움)
 *
 * 아이템 복잡도를 조절하며 프레임 시간 변화를 관찰합니다.
 */
@Composable
fun Practice3_ComplexityComparison() {
    var complexity by remember { mutableIntStateOf(1) } // 1: 간단, 2: 보통, 3: 복잡
    var frameTime by remember { mutableLongStateOf(0L) }
    var lastFrameTime by remember { mutableLongStateOf(System.nanoTime()) }
    var maxFrameTime by remember { mutableLongStateOf(0L) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isAutoScrolling by remember { mutableStateOf(false) }

    SideEffect {
        val current = System.nanoTime()
        val ft = (current - lastFrameTime) / 1_000_000
        if (ft > 0 && ft < 1000) {
            frameTime = ft
            if (ft > maxFrameTime) {
                maxFrameTime = ft
            }
        }
        lastFrameTime = current
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 복잡도에 따른 성능 비교",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        아이템 복잡도를 변경하며 성능 차이를 관찰하세요.

                        1. 복잡도 선택
                        2. "빠른 스크롤" 실행
                        3. 최대 프레임 시간 비교
                    """.trimIndent()
                )
            }
        }

        // 복잡도 선택
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = complexity == 1,
                onClick = {
                    complexity = 1
                    maxFrameTime = 0
                },
                label = { Text("간단") }
            )
            FilterChip(
                selected = complexity == 2,
                onClick = {
                    complexity = 2
                    maxFrameTime = 0
                },
                label = { Text("보통") }
            )
            FilterChip(
                selected = complexity == 3,
                onClick = {
                    complexity = 3
                    maxFrameTime = 0
                },
                label = { Text("복잡") }
            )
        }

        // 프레임 시간 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when {
                    maxFrameTime <= 16 -> PerformanceGood.copy(alpha = 0.2f)
                    maxFrameTime <= 32 -> PerformanceWarning.copy(alpha = 0.2f)
                    else -> PerformanceBad.copy(alpha = 0.2f)
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "현재: ${frameTime}ms",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "최대: ${maxFrameTime}ms",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            maxFrameTime <= 16 -> PerformanceGood
                            maxFrameTime <= 32 -> PerformanceWarning
                            else -> PerformanceBad
                        }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            isAutoScrolling = !isAutoScrolling
                            if (isAutoScrolling) {
                                scope.launch {
                                    while (isAutoScrolling) {
                                        listState.animateScrollToItem(
                                            (listState.firstVisibleItemIndex + 3)
                                                .coerceAtMost(99)
                                        )
                                        delay(50)
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAutoScrolling)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(if (isAutoScrolling) "중지" else "빠른 스크롤")
                    }

                    Button(
                        onClick = {
                            maxFrameTime = 0
                            scope.launch {
                                listState.scrollToItem(0)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("초기화")
                    }
                }
            }
        }

        // 복잡도 설명
        Text(
            text = when (complexity) {
                1 -> "간단: Text만 표시"
                2 -> "보통: Text + 아이콘 + 배경"
                else -> "복잡: Text + 아이콘 + 배경 + 다중 레이아웃 + 계산"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 리스트
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(100) { index ->
                when (complexity) {
                    1 -> SimpleItem(index)
                    2 -> MediumItem(index)
                    else -> ComplexItem(index)
                }
            }
        }
    }
}

@Composable
private fun SimpleItem(index: Int) {
    Text(
        text = "아이템 $index",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
private fun MediumItem(index: Int) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "$index",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "아이템 $index",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ComplexItem(index: Int) {
    // 의도적으로 복잡한 레이아웃
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "$index",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "복잡한 아이템 $index",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${index}분 전",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "이것은 복잡한 아이템의 본문입니다. " +
                        "여러 줄의 텍스트가 포함되어 있어 렌더링에 시간이 걸립니다. " +
                        "아이템 번호: $index"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    text = "미디어 영역 $index",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = { }) { Text("좋아요 ${index * 10}") }
                TextButton(onClick = { }) { Text("댓글 ${index * 2}") }
                TextButton(onClick = { }) { Text("공유") }
            }
        }
    }
}
