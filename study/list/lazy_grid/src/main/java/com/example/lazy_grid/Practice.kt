package com.example.lazy_grid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * 연습 문제 1: 기본 이미지 그리드 (쉬움)
 *
 * GridCells.Fixed를 사용하여 2열 그리드를 구현합니다.
 */
@Composable
fun Practice1_FixedGridScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 이미지 그리드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: 20개의 색상 카드를 2열 그리드로 표시하세요.
                        |
                        |요구사항:
                        |1. LazyVerticalGrid 사용
                        |2. GridCells.Fixed(2)로 2열 설정
                        |3. 아이템 간 8dp 간격
                        |4. 각 아이템은 정사각형 비율
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |LazyVerticalGrid(
                        |    columns = GridCells.Fixed(???),
                        |    verticalArrangement = Arrangement.spacedBy(???),
                        |    horizontalArrangement = Arrangement.spacedBy(???)
                        |) {
                        |    items(???) { index ->
                        |        // 아이템 구현
                        |    }
                        |}
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 연습 영역
        Card(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                // TODO: 여기에 LazyVerticalGrid를 구현하세요!
                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 아래 코드를 수정하여 2열 그리드를 완성하세요!

    /* 정답:
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(20, key = { it }) { index ->
            Card(
                modifier = Modifier.aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF6200EE + index * 0x0A0A0A)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
    */

    // 플레이스홀더 - 완성하면 이 부분을 삭제하세요
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TODO: LazyVerticalGrid를 구현하세요!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 연습 문제 2: 반응형 이미지 갤러리 (중간)
 *
 * GridCells.Adaptive로 화면 크기에 따라 열 개수가 자동 조정되는 갤러리를 구현합니다.
 */
@Composable
fun Practice2_AdaptiveGridScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 반응형 이미지 갤러리",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: 화면 크기에 따라 열 개수가 자동 조정되는 갤러리를 만드세요.
                        |
                        |요구사항:
                        |1. 최소 100dp 너비의 아이템
                        |2. 50개의 아이템 표시
                        |3. 각 아이템은 정사각형
                        |4. 아이템 간 4dp 간격
                        |5. 그리드 주변 8dp 패딩
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |GridCells.Adaptive(minSize = ???.dp)
                        |contentPadding = PaddingValues(???)
                        |Modifier.aspectRatio(1f)로 정사각형
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 연습 영역
        Card(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "연습 영역 (화면을 회전하여 열 개수 변화를 확인하세요)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                Practice2_Exercise()
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 반응형 그리드를 구현하세요!

    /* 정답:
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(50, key = { it }) { index ->
            Card(
                modifier = Modifier.aspectRatio(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(
                        red = (50 + index * 4) % 256,
                        green = (100 + index * 3) % 256,
                        blue = (150 + index * 2) % 256
                    )
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
    */

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TODO: GridCells.Adaptive를 사용한 그리드를 구현하세요!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 연습 문제 3: Pinterest 스타일 스태거드 그리드 (어려움)
 *
 * LazyVerticalStaggeredGrid로 다양한 높이의 아이템과 섹션 헤더를 구현합니다.
 */
@Composable
fun Practice3_StaggeredGridScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: Pinterest 스타일 스태거드 그리드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: Pinterest처럼 다양한 높이의 아이템을 표시하세요.
                        |
                        |요구사항:
                        |1. LazyVerticalStaggeredGrid 사용
                        |2. 2열 스태거드 그리드
                        |3. 3개의 카테고리 섹션 (최근, 인기, 추천)
                        |4. 각 카테고리에 헤더 (전체 너비)
                        |5. 각 카테고리에 8개 아이템 (랜덤 높이)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |LazyVerticalStaggeredGrid(
                        |    columns = StaggeredGridCells.Fixed(2)
                        |) { ... }
                        |
                        |// 전체 너비 헤더:
                        |item(span = StaggeredGridItemSpan.FullLine) { ... }
                        |
                        |// 랜덤 높이:
                        |val height = remember { (100 + Random.nextInt(100)).dp }
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 연습 영역
        Card(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                Practice3_Exercise()
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: Pinterest 스타일 스태거드 그리드를 구현하세요!

    /* 정답:
    val categories = listOf("최근", "인기", "추천")
    val categoryColors = listOf(
        Color(0xFFE91E63),
        Color(0xFF2196F3),
        Color(0xFF4CAF50)
    )

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEachIndexed { catIndex, category ->
            // 카테고리 헤더
            item(span = StaggeredGridItemSpan.FullLine) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = categoryColors[catIndex]
                    )
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }

            // 카테고리 아이템들
            items(8) { itemIndex ->
                val height = remember { (100 + Random.nextInt(100)).dp }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height),
                    colors = CardDefaults.cardColors(
                        containerColor = categoryColors[catIndex].copy(alpha = 0.3f)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$category ${itemIndex + 1}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
    */

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TODO: LazyVerticalStaggeredGrid를 구현하세요!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "StaggeredGridItemSpan.FullLine으로 헤더를 만드세요",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
