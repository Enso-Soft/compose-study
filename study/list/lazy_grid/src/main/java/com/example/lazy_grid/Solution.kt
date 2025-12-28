package com.example.lazy_grid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Solution 화면: LazyGrid로 효율적인 그리드 구현
 *
 * LazyGrid는 화면에 보이는 아이템만 렌더링하여 성능을 최적화합니다.
 *
 * 주요 컴포넌트:
 * - LazyVerticalGrid: 세로 스크롤 그리드
 * - LazyHorizontalGrid: 가로 스크롤 그리드
 * - LazyVerticalStaggeredGrid: Pinterest 스타일 그리드
 *
 * GridCells 옵션:
 * - Fixed(n): 정확히 n개의 열
 * - Adaptive(minSize): 최소 크기 기준 자동 조정
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 데모 선택 탭
        ScrollableTabRow(
            selectedTabIndex = selectedDemo,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Fixed", "Adaptive", "Span", "Staggered", "State").forEachIndexed { index, title ->
                Tab(
                    selected = selectedDemo == index,
                    onClick = { selectedDemo = index },
                    text = { Text(title) }
                )
            }
        }

        // 선택된 데모 표시
        when (selectedDemo) {
            0 -> FixedGridDemo()
            1 -> AdaptiveGridDemo()
            2 -> SpanGridDemo()
            3 -> StaggeredGridDemo()
            4 -> StateGridDemo()
        }
    }
}

/**
 * GridCells.Fixed 데모
 *
 * 정확히 지정된 개수의 열을 생성합니다.
 * 화면 너비와 관계없이 항상 같은 열 개수를 유지합니다.
 */
@Composable
private fun FixedGridDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "GridCells.Fixed(2)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "정확히 2개의 열로 고정됩니다. 화면을 회전해도 2열을 유지합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyVerticalGrid with Fixed columns
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(100, key = { it }) { index ->
                GridItemCard(
                    index = index,
                    color = Color(0xFF6200EE + index * 0x010101)
                )
            }
        }
    }
}

/**
 * GridCells.Adaptive 데모
 *
 * 지정된 최소 크기를 기준으로 열 개수가 자동 조정됩니다.
 * 화면이 넓으면 열이 늘어나고, 좁으면 줄어듭니다.
 */
@Composable
private fun AdaptiveGridDemo() {
    var minSize by remember { mutableIntStateOf(100) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "GridCells.Adaptive(${minSize}.dp)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "아이템의 최소 너비를 ${minSize}dp로 설정합니다. 화면 크기에 따라 열 개수가 자동 조정됩니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 최소 크기 조절 슬라이더
                Text(
                    text = "최소 크기: ${minSize}dp",
                    style = MaterialTheme.typography.bodySmall
                )
                Slider(
                    value = minSize.toFloat(),
                    onValueChange = { minSize = it.toInt() },
                    valueRange = 50f..200f,
                    steps = 5
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyVerticalGrid with Adaptive columns
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = minSize.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(100, key = { it }) { index ->
                GridItemCard(
                    index = index,
                    color = Color(0xFF03DAC5 + index * 0x010101)
                )
            }
        }
    }
}

/**
 * GridItemSpan 데모
 *
 * span을 사용하면 특정 아이템이 여러 열을 차지할 수 있습니다.
 * 헤더나 배너에 유용합니다.
 */
@Composable
private fun SpanGridDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "GridItemSpan으로 헤더 만들기",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "span = { GridItemSpan(maxLineSpan) }을 사용하면 아이템이 전체 너비를 차지합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val categories = listOf("과일", "채소", "음료")

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEachIndexed { categoryIndex, category ->
                // 카테고리 헤더 (전체 너비 차지)
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // 카테고리별 아이템들
                items(6) { itemIndex ->
                    val globalIndex = categoryIndex * 6 + itemIndex
                    GridItemCard(
                        index = globalIndex,
                        label = "${category} ${itemIndex + 1}",
                        color = when (categoryIndex) {
                            0 -> Color(0xFFFF6B6B)
                            1 -> Color(0xFF4ECDC4)
                            else -> Color(0xFF45B7D1)
                        }
                    )
                }
            }
        }
    }
}

/**
 * LazyVerticalStaggeredGrid 데모
 *
 * Pinterest처럼 아이템 높이가 다양한 그리드를 만듭니다.
 * 각 아이템이 자신의 높이에 맞게 배치됩니다.
 */
@Composable
private fun StaggeredGridDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "LazyVerticalStaggeredGrid",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pinterest 스타일! 각 아이템이 자신의 높이에 맞게 배치됩니다. 빈 공간 없이 효율적으로 채워집니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(50) { index ->
                // 랜덤 높이 시뮬레이션 (인덱스 기반)
                val height = (100 + (index % 5) * 40).dp
                StaggeredGridItemCard(
                    index = index,
                    height = height,
                    color = Color(
                        red = (100 + index * 3) % 256,
                        green = (150 + index * 5) % 256,
                        blue = (200 + index * 7) % 256
                    )
                )
            }
        }
    }
}

/**
 * LazyGridState 데모
 *
 * 스크롤 상태를 관리하고 프로그래밍 방식으로 스크롤을 제어합니다.
 */
@Composable
private fun StateGridDemo() {
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    // 첫 번째 보이는 아이템 인덱스
    val firstVisibleItemIndex by remember {
        derivedStateOf { gridState.firstVisibleItemIndex }
    }

    // 스크롤 위치에 따라 FAB 표시 여부 결정
    val showScrollToTop by remember {
        derivedStateOf { firstVisibleItemIndex > 10 }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "LazyGridState로 스크롤 제어",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "현재 첫 번째 보이는 아이템: $firstVisibleItemIndex",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "10번 이상 스크롤하면 '맨 위로' 버튼이 나타납니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(100, key = { it }) { index ->
                    GridItemCard(
                        index = index,
                        color = Color(0xFF9C27B0 + index * 0x010101)
                    )
                }
            }

            // 맨 위로 스크롤 버튼
            if (showScrollToTop) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "맨 위로"
                    )
                }
            }
        }
    }
}

/**
 * 기본 그리드 아이템 카드
 */
@Composable
private fun GridItemCard(
    index: Int,
    color: Color,
    label: String? = null
) {
    Card(
        modifier = Modifier.aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label ?: "$index",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 스태거드 그리드용 아이템 카드 (다양한 높이)
 */
@Composable
private fun StaggeredGridItemCard(
    index: Int,
    height: Dp,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$index",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${height.value.toInt()}dp",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
