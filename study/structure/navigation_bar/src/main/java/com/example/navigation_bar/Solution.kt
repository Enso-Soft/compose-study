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
 * Solution: NavigationBar 컴포넌트 사용
 *
 * Material 3의 NavigationBar와 NavigationBarItem을 사용하면
 * 모든 문제가 해결됩니다.
 */
@Composable
fun SolutionScreen() {
    var currentDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 데모 선택 탭
        ScrollableTabRow(
            selectedTabIndex = currentDemo,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("기본", "Badge", "Scaffold 통합").forEachIndexed { index, title ->
                Tab(
                    selected = currentDemo == index,
                    onClick = { currentDemo = index },
                    text = { Text(title) }
                )
            }
        }

        when (currentDemo) {
            0 -> BasicNavigationBarDemo()
            1 -> BadgeNavigationBarDemo()
            2 -> ScaffoldIntegrationDemo()
        }
    }
}

// ============================================================
// 데모 1: 기본 NavigationBar
// ============================================================

@Composable
private fun BasicNavigationBarDemo() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 해결책 설명
        SolutionExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "NavigationBar 컴포넌트:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 기본 NavigationBar
        BasicNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 핵심 코드 설명
        CodeExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 해결된 문제점 목록
        SolutionBenefitsCard()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SolutionExplanationCard() {
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
                text = "해결책: NavigationBar 사용",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Material 3의 NavigationBar와 NavigationBarItem을 사용하면 " +
                        "선택 효과, 애니메이션, 접근성이 자동으로 적용됩니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * 기본 NavigationBar 구현
 *
 * NavigationBar와 NavigationBarItem만 사용하면 됩니다.
 * - selected: 현재 선택된 탭인지 여부
 * - onClick: 탭 클릭 시 동작
 * - icon: 표시할 아이콘
 * - label: 아이콘 아래 표시할 텍스트
 */
@Composable
private fun BasicNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        NavItem("홈", Icons.Filled.Home, Icons.Outlined.Home),
        NavItem("검색", Icons.Filled.Search, Icons.Outlined.Search),
        NavItem("프로필", Icons.Filled.Person, Icons.Outlined.Person)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}

private data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null  // null: 배지 없음, 0: 점 배지, 1+: 숫자 배지
)

@Composable
private fun CodeExplanationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "핵심 코드",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
NavigationBar {
    NavigationBarItem(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        icon = { Icon(...) },
        label = { Text("홈") }
    )
    // 추가 탭들...
}
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}

@Composable
private fun SolutionBenefitsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결된 문제점",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            BenefitItem("자동 선택 효과", "selected = true만 전달하면 색상, 배경이 자동 적용")
            BenefitItem("부드러운 애니메이션", "Material 3 표준 애니메이션 내장")
            BenefitItem("가이드라인 준수", "높이, 간격, 리플 효과가 자동으로 올바르게 적용")
            BenefitItem("접근성 지원", "스크린 리더를 위한 역할이 자동 설정됨")
            BenefitItem("간결한 코드", "탭 추가 시 NavigationBarItem만 추가하면 됨")
        }
    }
}

@Composable
private fun BenefitItem(title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ============================================================
// 데모 2: Badge가 있는 NavigationBar
// ============================================================

@Composable
private fun BadgeNavigationBarDemo() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var notificationCount by remember { mutableIntStateOf(5) }
    var hasNewMessage by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Badge 설명 카드
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
                    text = "Badge 추가하기",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "BadgedBox를 사용하면 아이콘 위에 알림 개수나 상태를 표시할 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Badge 조절 컨트롤
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Badge 조절",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 알림 개수 조절
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("알림 개수: $notificationCount")
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { if (notificationCount > 0) notificationCount-- }) {
                        Text("-")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { notificationCount++ }) {
                        Text("+")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 새 메시지 토글
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("새 메시지 알림:")
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = hasNewMessage,
                        onCheckedChange = { hasNewMessage = it }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Badge가 있는 NavigationBar:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Badge가 있는 NavigationBar
        BadgedNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            notificationCount = notificationCount,
            hasNewMessage = hasNewMessage
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Badge 코드 설명
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Badge 사용 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 숫자 Badge
BadgedBox(
    badge = { Badge { Text("5") } }
) {
    Icon(...)
}

// 점 Badge (숫자 없이)
BadgedBox(
    badge = { Badge() }
) {
    Icon(...)
}

// 99+ 표시
Badge {
    Text(if (count > 99) "99+" else "${"$"}count")
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Badge가 있는 NavigationBar 구현
 *
 * BadgedBox를 사용하여 아이콘 위에 Badge를 표시합니다.
 * - 숫자 Badge: Badge { Text("5") }
 * - 점 Badge: Badge() (내용 없이)
 * - 99+ 표시: count > 99일 때 "99+" 텍스트
 */
@Composable
private fun BadgedNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    notificationCount: Int,
    hasNewMessage: Boolean
) {
    NavigationBar {
        // 홈 탭 (Badge 없음)
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "홈"
                )
            },
            label = { Text("홈") }
        )

        // 채팅 탭 (점 Badge - 새 메시지 있음 표시)
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                if (hasNewMessage) {
                    BadgedBox(
                        badge = { Badge() }  // 점 Badge
                    ) {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.MailOutline else Icons.Outlined.MailOutline,
                            contentDescription = "메시지"
                        )
                    }
                } else {
                    Icon(
                        imageVector = if (selectedTab == 1) Icons.Filled.MailOutline else Icons.Outlined.MailOutline,
                        contentDescription = "메시지"
                    )
                }
            },
            label = { Text("채팅") }
        )

        // 알림 탭 (숫자 Badge)
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                if (notificationCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text(
                                    text = if (notificationCount > 99) "99+" else "$notificationCount"
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                            contentDescription = "알림"
                        )
                    }
                } else {
                    Icon(
                        imageVector = if (selectedTab == 2) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "알림"
                    )
                }
            },
            label = { Text("알림") }
        )

        // 프로필 탭 (Badge 없음)
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "프로필"
                )
            },
            label = { Text("프로필") }
        )
    }
}

// ============================================================
// 데모 3: Scaffold 통합
// ============================================================

@Composable
private fun ScaffoldIntegrationDemo() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scaffold 설명 카드
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
                    text = "Scaffold와 함께 사용하기",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "실제 앱에서는 Scaffold의 bottomBar에 NavigationBar를 배치합니다. " +
                            "innerPadding을 적용해야 콘텐츠와 NavigationBar가 겹치지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Scaffold 통합 예제
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "홈"
                            )
                        },
                        label = { Text("홈") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 1) Icons.Filled.Explore else Icons.Outlined.Explore,
                                contentDescription = "탐색"
                            )
                        },
                        label = { Text("탐색") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 2) Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "프로필"
                            )
                        },
                        label = { Text("프로필") }
                    )
                }
            }
        ) { innerPadding ->
            // innerPadding을 적용해야 NavigationBar와 콘텐츠가 겹치지 않음
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTab) {
                    0 -> ScreenContent("홈", "홈 화면입니다")
                    1 -> ScreenContent("탐색", "탐색 화면입니다")
                    2 -> ScreenContent("프로필", "프로필 화면입니다")
                }
            }
        }
    }
}

@Composable
private fun ScreenContent(title: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
