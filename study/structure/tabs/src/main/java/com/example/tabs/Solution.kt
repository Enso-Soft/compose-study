@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Solution.kt - TabRow를 사용한 Material Design 규격 탭 구현
 *
 * 이 화면은 PrimaryTabRow, SecondaryTabRow, ScrollableTabRow, HorizontalPager 연동 등
 * 다양한 탭 사용법을 보여줍니다.
 */
@Composable
fun SolutionScreen() {
    var selectedExample by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 예제 선택 칩
        Text(
            text = "예제 선택",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedExample == 0,
                onClick = { selectedExample = 0 },
                label = { Text("기본") }
            )
            FilterChip(
                selected = selectedExample == 1,
                onClick = { selectedExample = 1 },
                label = { Text("아이콘+텍스트") }
            )
            FilterChip(
                selected = selectedExample == 2,
                onClick = { selectedExample = 2 },
                label = { Text("Primary vs Secondary") }
            )
            FilterChip(
                selected = selectedExample == 3,
                onClick = { selectedExample = 3 },
                label = { Text("Scrollable") }
            )
            FilterChip(
                selected = selectedExample == 4,
                onClick = { selectedExample = 4 },
                label = { Text("Pager 연동") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 선택된 예제 표시
        when (selectedExample) {
            0 -> BasicTabsExample()
            1 -> IconTextTabsExample()
            2 -> TabTypeComparisonExample()
            3 -> ScrollableTabsExample()
            4 -> TabsWithPagerExample()
        }
    }
}

/**
 * 예제 1: 기본 PrimaryTabRow 사용법
 */
@Composable
private fun BasicTabsExample() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("음악", "동영상", "사진")

    Column(modifier = Modifier.fillMaxWidth()) {
        ExampleHeader(
            title = "기본 PrimaryTabRow",
            description = "가장 기본적인 탭 사용법입니다. 인디케이터 애니메이션과 Material Design 규격이 자동으로 적용됩니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // PrimaryTabRow 사용
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // 콘텐츠 영역
        ContentBox(selectedTab, tabs)

        Spacer(modifier = Modifier.height(16.dp))

        // 설명
        SolutionPointCard(
            points = listOf(
                "인디케이터 슬라이딩 애니메이션 자동 적용",
                "Material Design 규격 (높이, 색상, ripple) 자동 준수",
                "접근성(a11y) 시맨틱 자동 처리",
                "탭 너비 균등 분배"
            )
        )
    }
}

/**
 * 예제 2: 아이콘 + 텍스트 조합
 */
@Composable
private fun IconTextTabsExample() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        Triple("음악", Icons.Default.PlayArrow, "음악 콘텐츠"),
        Triple("동영상", Icons.Default.Star, "동영상 콘텐츠"),
        Triple("사진", Icons.Default.Face, "사진 콘텐츠")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        ExampleHeader(
            title = "아이콘 + 텍스트 탭",
            description = "Tab의 icon과 text 파라미터를 모두 사용하면 아이콘과 텍스트가 함께 표시됩니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, (title, icon, _) ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                    icon = { Icon(icon, contentDescription = null) }
                )
            }
        }

        // 콘텐츠 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = tabs[selectedTab].second,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tabs[selectedTab].third,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CodeSnippetCard(
            code = """
Tab(
    selected = selectedTab == index,
    onClick = { selectedTab = index },
    text = { Text(title) },
    icon = { Icon(icon, contentDescription = null) }
)
            """.trimIndent()
        )
    }
}

/**
 * 예제 3: PrimaryTabRow vs SecondaryTabRow 비교
 */
@Composable
private fun TabTypeComparisonExample() {
    var primaryTab by rememberSaveable { mutableIntStateOf(0) }
    var secondaryTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("탭 1", "탭 2", "탭 3")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ExampleHeader(
            title = "PrimaryTabRow vs SecondaryTabRow",
            description = "Primary는 메인 네비게이션용, Secondary는 콘텐츠 내 서브 네비게이션용입니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Primary 예제
        Text(
            text = "PrimaryTabRow",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "상단 앱바 아래에 배치, 앱의 주요 섹션 네비게이션",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryTabRow(selectedTabIndex = primaryTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = primaryTab == index,
                    onClick = { primaryTab = index },
                    text = { Text(title) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            Text("Primary 콘텐츠: ${tabs[primaryTab]}")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Secondary 예제
        Text(
            text = "SecondaryTabRow",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "콘텐츠 영역 내부에 배치, 세부 분류용",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        SecondaryTabRow(selectedTabIndex = secondaryTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = secondaryTab == index,
                    onClick = { secondaryTab = index },
                    text = { Text(title) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            Text("Secondary 콘텐츠: ${tabs[secondaryTab]}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 비교 표
        ComparisonCard()
    }
}

/**
 * 예제 4: ScrollableTabRow - 탭이 많을 때
 */
@Composable
private fun ScrollableTabsExample() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("탭1", "탭2", "탭3", "탭4", "탭5", "탭6", "탭7", "탭8")

    Column(modifier = Modifier.fillMaxWidth()) {
        ExampleHeader(
            title = "PrimaryScrollableTabRow",
            description = "탭이 5개 이상으로 많아지면 화면에 다 표시되지 않습니다. ScrollableTabRow를 사용하면 스크롤이 가능합니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // 콘텐츠 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "선택된 탭: ${tabs[selectedTab]}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "언제 사용할까요?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- 탭이 5개 이상일 때", style = MaterialTheme.typography.bodySmall)
                Text("- 탭 이름이 길어서 화면에 다 안 들어갈 때", style = MaterialTheme.typography.bodySmall)
                Text("- 동적으로 탭이 추가/삭제될 때", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/**
 * 예제 5: HorizontalPager 연동
 */
@Composable
private fun TabsWithPagerExample() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("음악", "동영상", "사진")
    val pagerState = rememberPagerState { tabs.size }

    // 탭 클릭 -> 페이저 이동
    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    // 페이저 스와이프 -> 탭 업데이트
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTab = pagerState.currentPage
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        ExampleHeader(
            title = "HorizontalPager 연동",
            description = "스와이프로 탭을 전환하고 싶다면 HorizontalPager와 연동합니다. LaunchedEffect로 양방향 동기화를 합니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when (page) {
                            0 -> MaterialTheme.colorScheme.primaryContainer
                            1 -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = when (page) {
                            0 -> Icons.Default.PlayArrow
                            1 -> Icons.Default.Star
                            else -> Icons.Default.Face
                        },
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${tabs[page]} 페이지",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "좌우로 스와이프해보세요!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CodeSnippetCard(
            code = """
// 탭 클릭 -> 페이저 이동
LaunchedEffect(selectedTab) {
    pagerState.animateScrollToPage(selectedTab)
}

// 페이저 스와이프 -> 탭 업데이트
LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
    if (!pagerState.isScrollInProgress) {
        selectedTab = pagerState.currentPage
    }
}
            """.trimIndent()
        )
    }
}

// ========== Helper Composables ==========

@Composable
private fun ExampleHeader(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ContentBox(selectedTab: Int, tabs: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${tabs[selectedTab]} 콘텐츠 영역",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun SolutionPointCard(points: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "해결된 점",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            points.forEach { point ->
                Text(
                    text = "- $point",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun CodeSnippetCard(code: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "코드",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = code,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}

@Composable
private fun ComparisonCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "비교",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("특성", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Text("Primary", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Text("Secondary", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("위치", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text("앱바 아래", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text("콘텐츠 내부", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("용도", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text("메인 네비게이션", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text("서브 분류", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("강조", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text("강함", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text("약함", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

