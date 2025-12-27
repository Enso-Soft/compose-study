package com.example.adaptive_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: WindowSizeClass 표시하기
 *
 * 목표: currentWindowAdaptiveInfo()를 사용하여 현재 화면 크기 정보를 표시
 *
 * 힌트:
 * 1. currentWindowAdaptiveInfo() 호출
 * 2. windowSizeClass 속성에서 windowWidthSizeClass 추출
 * 3. when 문으로 각 크기별 텍스트 표시
 */
@Composable
fun Practice1_DisplayWindowSize() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 연습 문제 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: WindowSizeClass 표시하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("TODO:")
                        appendLine("1. currentWindowAdaptiveInfo() 호출")
                        appendLine("2. windowSizeClass.windowWidthSizeClass 추출")
                        append("3. 화면 크기별 다른 텍스트 표시")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // TODO 1: currentWindowAdaptiveInfo()를 호출하여 windowSizeClass 가져오기
        // val windowAdaptiveInfo = ???
        // val windowSizeClass = ???

        // TODO 2: windowWidthSizeClass에 따라 다른 텍스트 표시
        // when (windowSizeClass.windowWidthSizeClass) {
        //     WindowWidthSizeClass.COMPACT -> { ... }
        //     WindowWidthSizeClass.MEDIUM -> { ... }
        //     WindowWidthSizeClass.EXPANDED -> { ... }
        // }

        Text(
            text = "여기에 현재 WindowSizeClass를 표시하세요",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.outline
        )

        // ============ 정답 (주석 해제하여 확인) ============
        /*
        val windowAdaptiveInfo = currentWindowAdaptiveInfo()
        val windowSizeClass = windowAdaptiveInfo.windowSizeClass

        val (sizeText, sizeColor) = when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> "COMPACT" to MaterialTheme.colorScheme.error
            WindowWidthSizeClass.MEDIUM -> "MEDIUM" to MaterialTheme.colorScheme.tertiary
            WindowWidthSizeClass.EXPANDED -> "EXPANDED" to MaterialTheme.colorScheme.primary
            else -> "UNKNOWN" to MaterialTheme.colorScheme.outline
        }

        Text(
            text = sizeText,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = sizeColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.COMPACT -> "작은 화면 (폰)"
                WindowWidthSizeClass.MEDIUM -> "중간 화면 (폴더블/작은 태블릿)"
                WindowWidthSizeClass.EXPANDED -> "큰 화면 (태블릿/데스크톱)"
                else -> "알 수 없는 크기"
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        */
    }
}

/**
 * 연습 문제 2: 화면 크기별 레이아웃 분기
 *
 * 목표: Compact에서는 Column, Medium/Expanded에서는 Row 사용
 *
 * 힌트:
 * 1. WindowSizeClass 가져오기
 * 2. when 문으로 레이아웃 분기
 * 3. Modifier.weight()로 균등 분배
 */
@Composable
fun Practice2_AdaptiveLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 연습 문제 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 화면 크기별 레이아웃 분기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("TODO:")
                        appendLine("1. WindowSizeClass 가져오기")
                        appendLine("2. Compact: Column으로 세로 배치")
                        append("3. Medium/Expanded: Row로 가로 배치")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 화면 크기에 따라 Column 또는 Row 사용
        // Compact → Column (세로 배치)
        // Medium/Expanded → Row (가로 배치)

        // 샘플 컨텐츠 (레이아웃만 변경하면 됨)
        Column(modifier = Modifier.fillMaxSize()) {
            ColorBox(color = "빨강", modifier = Modifier.weight(1f))
            ColorBox(color = "초록", modifier = Modifier.weight(1f))
            ColorBox(color = "파랑", modifier = Modifier.weight(1f))
        }

        // ============ 정답 (주석 해제하여 확인) ============
        /*
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> {
                // 폰: 세로 배치
                Column(modifier = Modifier.fillMaxSize()) {
                    ColorBox(color = "빨강", modifier = Modifier.weight(1f))
                    ColorBox(color = "초록", modifier = Modifier.weight(1f))
                    ColorBox(color = "파랑", modifier = Modifier.weight(1f))
                }
            }
            else -> {
                // 태블릿: 가로 배치
                Row(modifier = Modifier.fillMaxSize()) {
                    ColorBox(color = "빨강", modifier = Modifier.weight(1f))
                    ColorBox(color = "초록", modifier = Modifier.weight(1f))
                    ColorBox(color = "파랑", modifier = Modifier.weight(1f))
                }
            }
        }
        */
    }
}

@Composable
private fun ColorBox(
    color: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (color) {
        "빨강" -> MaterialTheme.colorScheme.errorContainer
        "초록" -> MaterialTheme.colorScheme.tertiaryContainer
        "파랑" -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(backgroundColor, MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = color,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(32.dp)
        )
    }
}

/**
 * 연습 문제 3: NavigationSuiteScaffold 구현
 *
 * 목표: 화면 크기에 따라 BottomNav ↔ NavigationRail 자동 전환
 *
 * 힌트:
 * 1. NavigationSuiteScaffold 사용
 * 2. navigationSuiteItems 블록에서 item() 함수로 아이템 추가
 * 3. selected, onClick, icon, label 속성 설정
 */
@Composable
fun Practice3_AdaptiveNavigation() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        NavItem("홈", Icons.Default.Home),
        NavItem("검색", Icons.Default.Search),
        NavItem("프로필", Icons.Default.Person),
        NavItem("설정", Icons.Default.Settings)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // 연습 문제 안내
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: NavigationSuiteScaffold 구현",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("TODO:")
                        appendLine("1. NavigationSuiteScaffold 사용")
                        appendLine("2. navigationSuiteItems에 4개 아이템 추가")
                        append("3. 화면 크기 변경 시 네비게이션 UI 자동 변환 확인")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // TODO: NavigationSuiteScaffold로 적응형 네비게이션 구현
        // NavigationSuiteScaffold(
        //     navigationSuiteItems = {
        //         navItems.forEachIndexed { index, item ->
        //             item(
        //                 selected = ???,
        //                 onClick = { ??? },
        //                 icon = { Icon(???, contentDescription = null) },
        //                 label = { Text(???) }
        //             )
        //         }
        //     }
        // ) {
        //     // 컨텐츠
        // }

        // 임시 컨텐츠 (NavigationSuiteScaffold로 교체하세요)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NavigationSuiteScaffold를 구현하세요",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // ============ 정답 (주석 해제하여 확인) ============
        /*
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                navItems.forEachIndexed { index, navItem ->
                    item(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(navItem.icon, contentDescription = null) },
                        label = { Text(navItem.label) }
                    )
                }
            }
        ) {
            // 선택된 탭에 따른 컨텐츠
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = navItems[selectedIndex].icon,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = navItems[selectedIndex].label,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "화면 크기를 변경하면 네비게이션 UI가 바뀝니다",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Compact → BottomNav\nMedium/Expanded → NavigationRail",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
        */
    }
}

private data class NavItem(
    val label: String,
    val icon: ImageVector
)
