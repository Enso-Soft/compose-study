package com.example.navigation_bar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Practice: NavigationBar 연습 문제
 *
 * 세 가지 난이도의 연습 문제를 통해 NavigationBar 사용법을 익힙니다.
 * - 연습 1 (쉬움): 기본 Navigation Bar 만들기
 * - 연습 2 (중간): Badge가 있는 Navigation Bar
 * - 연습 3 (어려움): Scaffold 통합 + 화면 전환
 */
@Composable
fun PracticeScreen() {
    var currentPractice by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 연습문제 선택 탭
        ScrollableTabRow(
            selectedTabIndex = currentPractice,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("연습 1: 기본", "연습 2: Badge", "연습 3: Scaffold").forEachIndexed { index, title ->
                Tab(
                    selected = currentPractice == index,
                    onClick = { currentPractice = index },
                    text = { Text(title) }
                )
            }
        }

        when (currentPractice) {
            0 -> Practice1()
            1 -> Practice2()
            2 -> Practice3()
        }
    }
}

// ============================================================
// 연습 1: 기본 Navigation Bar 만들기 (쉬움)
// ============================================================

@Composable
private fun Practice1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 설명
        PracticeCard(
            title = "연습 1: 기본 Navigation Bar 만들기",
            difficulty = "쉬움",
            description = """
홈, 검색, 프로필 3개 탭이 있는 Navigation Bar를 만들어보세요.

요구사항:
1. mutableIntStateOf로 선택된 탭 인덱스 관리
2. NavigationBar 안에 NavigationBarItem 3개 생성
3. 각 탭에 적절한 아이콘과 레이블 표시
4. 탭 클릭 시 선택 상태 변경
            """.trimIndent()
        )

        HintCard(
            hints = listOf(
                "remember { mutableIntStateOf(0) }로 상태 관리",
                "forEachIndexed로 아이템을 반복 생성하면 코드가 간결해집니다",
                "selected = selectedTab == index",
                "onClick = { selectedTab = index }"
            )
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // TODO: 여기에 연습 1의 답을 구현하세요
        Text(
            text = "아래에 NavigationBar를 구현하세요:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ============================================================
        // TODO: 여기에 NavigationBar를 구현하세요
        // ============================================================
        //
        // 데이터 클래스와 아이템 리스트는 제공됩니다:
        //
        // data class Practice1NavItem(
        //     val label: String,
        //     val selectedIcon: ImageVector,
        //     val unselectedIcon: ImageVector
        // )
        //
        // val items = listOf(
        //     Practice1NavItem("홈", Icons.Filled.Home, Icons.Outlined.Home),
        //     Practice1NavItem("검색", Icons.Filled.Search, Icons.Outlined.Search),
        //     Practice1NavItem("프로필", Icons.Filled.Person, Icons.Outlined.Person)
        // )
        //
        // TODO 1: var selectedTab by remember { mutableIntStateOf(0) } 선언
        //
        // TODO 2: NavigationBar { ... } 작성
        //
        // TODO 3: items.forEachIndexed로 NavigationBarItem 생성
        //         - selected = selectedTab == index
        //         - onClick = { selectedTab = index }
        //         - icon = { Icon(...) }  // selectedTab에 따라 아이콘 변경
        //         - label = { Text(item.label) }
        //
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TODO: NavigationBar 구현",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ============================================================
// 연습 2: Badge가 있는 Navigation Bar (중간)
// ============================================================

@Composable
private fun Practice2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 설명
        PracticeCard(
            title = "연습 2: Badge가 있는 Navigation Bar",
            difficulty = "중간",
            description = """
Badge가 있는 4개 탭 Navigation Bar를 만들어보세요.

요구사항:
1. 4개 탭: 홈, 채팅(5), 알림(점), 프로필
2. 채팅 탭: 숫자 Badge (5)
3. 알림 탭: 점 Badge (알림 있음 표시)
4. badgeCount가 99 초과시 "99+" 표시
            """.trimIndent()
        )

        HintCard(
            hints = listOf(
                "BadgedBox(badge = { Badge { Text(\"5\") } }) { Icon(...) }",
                "점 Badge: Badge() (내용 없이)",
                "badgeCount가 null이면 Badge 없음",
                "badgeCount가 0이면 점 Badge",
                "badgeCount > 0이면 숫자 Badge",
                "when 문으로 조건 분기"
            )
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "아래에 Badge가 있는 NavigationBar를 구현하세요:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ============================================================
        // TODO: 여기에 Badge가 있는 NavigationBar를 구현하세요
        // ============================================================
        //
        // 데이터 클래스와 아이템 리스트는 제공됩니다:
        //
        // data class Practice2NavItem(
        //     val label: String,
        //     val selectedIcon: ImageVector,
        //     val unselectedIcon: ImageVector,
        //     val badgeCount: Int? = null  // null: 배지 없음, 0: 점 배지, 1+: 숫자 배지
        // )
        //
        // val items = listOf(
        //     Practice2NavItem("홈", Icons.Filled.Home, Icons.Outlined.Home, null),
        //     Practice2NavItem("채팅", Icons.Filled.Chat, Icons.Outlined.Chat, 5),
        //     Practice2NavItem("알림", Icons.Filled.Notifications, Icons.Outlined.Notifications, 0),
        //     Practice2NavItem("프로필", Icons.Filled.Person, Icons.Outlined.Person, null)
        // )
        //
        // TODO 1: 선택 상태 관리
        //
        // TODO 2: NavigationBar 작성
        //
        // TODO 3: NavigationBarItem의 icon에서 Badge 조건 처리
        //         when (item.badgeCount) {
        //             null -> Icon(...)  // Badge 없음
        //             0 -> BadgedBox(badge = { Badge() }) { Icon(...) }  // 점 Badge
        //             else -> BadgedBox(badge = { Badge { Text(...) } }) { Icon(...) }  // 숫자 Badge
        //         }
        //
        // TODO 4: 99+ 처리
        //         if (badgeCount > 99) "99+" else "$badgeCount"
        //
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TODO: Badge가 있는 NavigationBar 구현",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ============================================================
// 연습 3: Scaffold 통합 + 화면 전환 (어려움)
// ============================================================

@Composable
private fun Practice3() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 설명
        PracticeCard(
            title = "연습 3: Scaffold 통합 + 화면 전환",
            difficulty = "어려움",
            description = """
Scaffold에 NavigationBar를 통합하고 화면 전환을 구현하세요.

요구사항:
1. Scaffold의 bottomBar에 NavigationBar 배치
2. 5개 탭: 홈, 탐색, 생성(+), 알림(Badge), 프로필
3. 각 탭 선택 시 해당 화면 콘텐츠 표시
4. 알림 탭에 숫자 Badge 표시
5. innerPadding을 콘텐츠에 적용
            """.trimIndent()
        )

        HintCard(
            hints = listOf(
                "Scaffold(bottomBar = { NavigationBar { ... } }) { innerPadding -> ... }",
                "Box(modifier = Modifier.padding(innerPadding)) { ... }",
                "when (selectedTab) { 0 -> HomeScreen() ... }",
                "생성 탭은 특별한 아이콘: Icons.Filled.AddCircle"
            )
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "아래에 Scaffold + NavigationBar를 구현하세요:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ============================================================
        // TODO: 여기에 Scaffold + NavigationBar를 구현하세요
        // ============================================================
        //
        // 제공되는 화면 컴포저블들:
        //
        // @Composable
        // fun Practice3HomeScreen() { ... }
        //
        // @Composable
        // fun Practice3ExploreScreen() { ... }
        //
        // @Composable
        // fun Practice3CreateScreen() { ... }
        //
        // @Composable
        // fun Practice3NotificationsScreen() { ... }
        //
        // @Composable
        // fun Practice3ProfileScreen() { ... }
        //
        // 네비게이션 아이템:
        //
        // val items = listOf(
        //     Practice3NavItem("홈", Icons.Filled.Home, Icons.Outlined.Home, null),
        //     Practice3NavItem("탐색", Icons.Filled.Explore, Icons.Outlined.Explore, null),
        //     Practice3NavItem("생성", Icons.Filled.AddCircle, Icons.Outlined.AddCircle, null),
        //     Practice3NavItem("알림", Icons.Filled.Notifications, Icons.Outlined.Notifications, 3),
        //     Practice3NavItem("프로필", Icons.Filled.Person, Icons.Outlined.Person, null)
        // )
        //
        // TODO 1: var selectedTab by remember { mutableIntStateOf(0) }
        //
        // TODO 2: Scaffold(
        //             bottomBar = {
        //                 NavigationBar {
        //                     items.forEachIndexed { index, item ->
        //                         NavigationBarItem(...)
        //                     }
        //                 }
        //             }
        //         ) { innerPadding ->
        //             // TODO 3: innerPadding 적용
        //             Box(modifier = Modifier.padding(innerPadding)) {
        //                 // TODO 4: 화면 전환
        //                 when (selectedTab) {
        //                     0 -> Practice3HomeScreen()
        //                     1 -> Practice3ExploreScreen()
        //                     2 -> Practice3CreateScreen()
        //                     3 -> Practice3NotificationsScreen()
        //                     4 -> Practice3ProfileScreen()
        //                 }
        //             }
        //         }
        //
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TODO: Scaffold + NavigationBar + 화면 전환 구현",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
private fun PracticeCard(
    title: String,
    difficulty: String,
    description: String
) {
    val difficultyColor = when (difficulty) {
        "쉬움" -> MaterialTheme.colorScheme.primary
        "중간" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = difficultyColor.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = difficulty,
                        style = MaterialTheme.typography.labelMedium,
                        color = difficultyColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            hints.forEach { hint ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = "- ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ============================================================
// 연습 3에서 사용할 화면 컴포저블들
// (학습자가 직접 구현한 NavigationBar에서 이 화면들을 표시해야 함)
// ============================================================

@Composable
fun Practice3HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "홈 화면",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Practice3ExploreScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Explore,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "탐색 화면",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Practice3CreateScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "생성 화면",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Practice3NotificationsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "알림 화면",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Practice3ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "프로필 화면",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
