@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Practice.kt - 탭 연습 문제
 *
 * 이 파일에는 3개의 연습 문제가 있습니다.
 * 각 연습 문제의 TODO 부분을 직접 구현해보세요!
 *
 * - 연습 1: 기본 탭 만들기 (쉬움)
 * - 연습 2: 아이콘 + 텍스트 탭 (중간)
 * - 연습 3: HorizontalPager 연동 (어려움)
 */

@Composable
fun PracticeScreen() {
    var selectedPractice by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 연습 문제 선택
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedPractice == 0,
                onClick = { selectedPractice = 0 },
                label = { Text("연습 1") },
                leadingIcon = if (selectedPractice == 0) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("연습 2") },
                leadingIcon = if (selectedPractice == 1) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("연습 3") },
                leadingIcon = if (selectedPractice == 2) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }

        when (selectedPractice) {
            0 -> Practice1_BasicTabs()
            1 -> Practice2_IconTextTabs()
            2 -> Practice3_TabsWithPager()
        }
    }
}

// =============================================================================
// 연습 1: 기본 탭 만들기 (쉬움)
// =============================================================================

/**
 * 연습 1: 기본 탭 만들기
 *
 * 목표: "홈", "검색", "프로필" 3개 탭을 만들고,
 *       선택된 탭에 따라 다른 텍스트를 표시하세요.
 *
 * 요구사항:
 * 1. PrimaryTabRow와 Tab 사용
 * 2. selectedTab 상태 변수로 현재 선택된 탭 관리
 * 3. 탭 선택 시 해당 콘텐츠 표시
 *
 * 힌트:
 * - rememberSaveable { mutableIntStateOf(0) } 로 상태 선언
 * - PrimaryTabRow(selectedTabIndex = selectedTab) { ... }
 * - Tab(selected = ..., onClick = ..., text = ...)
 */
@Composable
fun Practice1_BasicTabs() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PracticeHeader(
            title = "연습 1: 기본 탭 만들기",
            difficulty = "쉬움",
            description = "\"홈\", \"검색\", \"프로필\" 3개 탭을 만들고, 선택된 탭에 따라 다른 텍스트를 표시하세요."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ===== 여기에 구현하세요! =====

        // TODO 1: 상태 변수 선언
        // var selectedTab by ...

        val tabs = listOf("홈", "검색", "프로필")

        // TODO 2: PrimaryTabRow 구현
        // PrimaryTabRow(selectedTabIndex = ...) {
        //     tabs.forEachIndexed { index, title ->
        //         Tab(
        //             selected = ...,
        //             onClick = { ... },
        //             text = { Text(...) }
        //         )
        //     }
        // }

        // TODO 3: 선택된 탭에 따른 콘텐츠 표시
        // when (selectedTab) {
        //     0 -> Text("홈 화면입니다")
        //     1 -> Text("검색 화면입니다")
        //     2 -> Text("프로필 화면입니다")
        // }

        // ===== 구현 영역 끝 =====

        Spacer(modifier = Modifier.weight(1f))

        HintCard(
            hints = listOf(
                "rememberSaveable { mutableIntStateOf(0) } 로 상태 선언",
                "PrimaryTabRow의 selectedTabIndex에 현재 선택된 탭 인덱스 전달",
                "Tab의 selected는 Boolean, onClick은 람다"
            )
        )
    }
}

// =============================================================================
// 연습 2: 아이콘 + 텍스트 탭 (중간)
// =============================================================================

/**
 * 연습 2: 아이콘 + 텍스트 탭
 *
 * 목표: "음악", "동영상", "사진" 탭에 각각 아이콘을 추가하세요.
 *
 * 요구사항:
 * 1. 음악: Icons.Default.MusicNote
 * 2. 동영상: Icons.Default.VideoLibrary
 * 3. 사진: Icons.Default.Photo
 * 4. 선택된 탭에 따른 콘텐츠 표시 (아이콘 크게 + 텍스트)
 *
 * 힌트:
 * - Tab의 icon 파라미터 사용: icon = { Icon(...) }
 * - Pair 또는 data class로 탭 데이터 정의
 */
@Composable
fun Practice2_IconTextTabs() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PracticeHeader(
            title = "연습 2: 아이콘 + 텍스트 탭",
            difficulty = "중간",
            description = "\"음악\", \"동영상\", \"사진\" 탭에 각각 아이콘을 추가하세요."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ===== 여기에 구현하세요! =====

        var selectedTab by rememberSaveable { mutableIntStateOf(0) }

        // TODO 1: 탭 데이터 정의 (제목과 아이콘 쌍)
        // val tabs = listOf(
        //     "음악" to Icons.Default.MusicNote,
        //     "동영상" to Icons.Default.VideoLibrary,
        //     "사진" to Icons.Default.Photo
        // )

        // TODO 2: PrimaryTabRow 구현 (text와 icon 파라미터 모두 사용)
        // PrimaryTabRow(selectedTabIndex = selectedTab) {
        //     tabs.forEachIndexed { index, (title, icon) ->
        //         Tab(
        //             selected = ...,
        //             onClick = { ... },
        //             text = { Text(...) },
        //             icon = { Icon(..., contentDescription = null) }
        //         )
        //     }
        // }

        // TODO 3: 선택된 탭에 따른 콘텐츠 (카드 형태로)
        // Box(
        //     modifier = Modifier
        //         .fillMaxWidth()
        //         .height(150.dp)
        //         .background(MaterialTheme.colorScheme.surfaceContainerLow),
        //     contentAlignment = Alignment.Center
        // ) {
        //     Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //         Icon(선택된 탭의 아이콘, size = 64.dp)
        //         Text(선택된 탭의 제목 + " 콘텐츠")
        //     }
        // }

        // ===== 구현 영역 끝 =====

        Spacer(modifier = Modifier.weight(1f))

        HintCard(
            hints = listOf(
                "Pair<String, ImageVector> 로 탭 데이터 정의",
                "Tab의 icon = { Icon(imageVector, contentDescription = null) }",
                "tabs[selectedTab].second 로 선택된 아이콘 접근"
            )
        )
    }
}

// =============================================================================
// 연습 3: HorizontalPager 연동 (어려움)
// =============================================================================

/**
 * 연습 3: HorizontalPager 연동
 *
 * 목표: 탭과 HorizontalPager를 연동하여 스와이프로 탭을 전환할 수 있게 만드세요.
 *
 * 요구사항:
 * 1. "뉴스", "스포츠", "엔터테인먼트" 3개 탭
 * 2. 탭 클릭 시 해당 페이지로 애니메이션 이동
 * 3. 페이지 스와이프 시 탭 인디케이터 자동 이동
 * 4. LaunchedEffect로 양방향 동기화
 *
 * 힌트:
 * - rememberPagerState { pageCount } 사용
 * - LaunchedEffect(selectedTab) { pagerState.animateScrollToPage(selectedTab) }
 * - LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) { ... }
 */
@Composable
fun Practice3_TabsWithPager() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PracticeHeader(
            title = "연습 3: HorizontalPager 연동",
            difficulty = "어려움",
            description = "탭과 HorizontalPager를 연동하여 스와이프로 탭을 전환할 수 있게 만드세요."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ===== 여기에 구현하세요! =====

        var selectedTab by rememberSaveable { mutableIntStateOf(0) }
        val tabs = listOf("뉴스", "스포츠", "엔터테인먼트")

        // TODO 1: PagerState 생성
        // val pagerState = rememberPagerState { tabs.size }

        // TODO 2: 탭 클릭 -> 페이저 이동 동기화
        // LaunchedEffect(selectedTab) {
        //     pagerState.animateScrollToPage(selectedTab)
        // }

        // TODO 3: 페이저 스와이프 -> 탭 업데이트 동기화
        // LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        //     if (!pagerState.isScrollInProgress) {
        //         selectedTab = pagerState.currentPage
        //     }
        // }

        // TODO 4: PrimaryTabRow 구현
        // PrimaryTabRow(selectedTabIndex = selectedTab) {
        //     tabs.forEachIndexed { index, title ->
        //         Tab(...)
        //     }
        // }

        // TODO 5: HorizontalPager 구현
        // HorizontalPager(
        //     state = pagerState,
        //     modifier = Modifier
        //         .fillMaxWidth()
        //         .height(200.dp)
        // ) { page ->
        //     // 페이지별 콘텐츠
        //     when (page) {
        //         0 -> NewsContent()
        //         1 -> SportsContent()
        //         2 -> EntertainmentContent()
        //     }
        // }

        // ===== 구현 영역 끝 =====

        Spacer(modifier = Modifier.weight(1f))

        HintCard(
            hints = listOf(
                "rememberPagerState { pageCount } - 람다로 페이지 수 전달",
                "LaunchedEffect(key)는 key가 변경될 때마다 실행",
                "pagerState.isScrollInProgress로 스크롤 중인지 확인",
                "스크롤이 끝났을 때만 selectedTab 업데이트"
            )
        )
    }
}

// ===== 페이지 콘텐츠 (연습 3용) =====

@Composable
fun NewsContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "뉴스 페이지",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "좌우로 스와이프해보세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun SportsContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "스포츠 페이지",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "좌우로 스와이프해보세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun EntertainmentContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "엔터테인먼트 페이지",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "좌우로 스와이프해보세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

// ===== Helper Composables =====

@Composable
private fun PracticeHeader(
    title: String,
    difficulty: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Surface(
                    color = when (difficulty) {
                        "쉬움" -> MaterialTheme.colorScheme.primary
                        "중간" -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.error
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = difficulty,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            hints.forEach { hint ->
                Text(
                    text = "- $hint",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
