package com.example.navigation_rail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Solution: NavigationRail로 측면 네비게이션 구현
 *
 * 태블릿이나 대형 화면에서는 NavigationRail을 사용하여
 * 측면에 세로형 네비게이션을 배치합니다.
 */

// 앱의 목적지 정의
enum class Destination(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    HOME("홈", Icons.Outlined.Home, Icons.Default.Home),
    SEARCH("검색", Icons.Outlined.Search, Icons.Default.Search),
    LIBRARY("라이브러리", Icons.Outlined.LibraryMusic, Icons.Default.LibraryMusic),
    SETTINGS("설정", Icons.Outlined.Settings, Icons.Default.Settings)
}

@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: NavigationRail 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "네비게이션을 화면 측면에 세로로 배치합니다. " +
                            "태블릿을 잡은 손가락으로 쉽게 접근할 수 있습니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // NavigationRail 데모
        Text(
            text = "NavigationRail 데모",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        NavigationRailDemo()

        // 핵심 코드 설명
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 코드",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = """
Row {
    NavigationRail(
        header = {
            // FAB 또는 로고를 배치할 수 있는 공간
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, "추가")
            }
        }
    ) {
        destinations.forEach { dest ->
            NavigationRailItem(
                selected = currentDest == dest,
                onClick = { currentDest = dest },
                icon = { Icon(dest.icon, dest.label) },
                label = { Text(dest.label) }
            )
        }
    }

    // 콘텐츠 영역
    ContentScreen(currentDest)
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        // NavigationRailItem 파라미터 설명
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "NavigationRailItem 주요 파라미터",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                ParameterItem(
                    name = "selected",
                    type = "Boolean",
                    description = "현재 선택된 상태인지 (필수)"
                )
                ParameterItem(
                    name = "onClick",
                    type = "() -> Unit",
                    description = "클릭했을 때 실행할 코드 (필수)"
                )
                ParameterItem(
                    name = "icon",
                    type = "@Composable",
                    description = "표시할 아이콘 (필수)"
                )
                ParameterItem(
                    name = "label",
                    type = "@Composable?",
                    description = "표시할 텍스트 (선택)"
                )
                ParameterItem(
                    name = "alwaysShowLabel",
                    type = "Boolean",
                    description = "레이블 항상 표시 여부 (기본: true)"
                )
            }
        }

        // 비교표
        ComparisonCard()

        // 선택 가이드
        SelectionGuideCard()

        // header 슬롯 설명
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "header 슬롯 활용",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "NavigationRail의 header 파라미터를 사용하면 " +
                            "상단에 FAB나 앱 로고를 배치할 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // FAB 예시
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SmallFloatingActionButton(onClick = { }) {
                            Icon(Icons.Default.Add, "추가")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("FAB", style = MaterialTheme.typography.labelSmall)
                    }

                    // 로고 예시
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.MusicNote,
                            contentDescription = "로고",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("로고", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationRailDemo() {
    var selectedDestination by remember { mutableStateOf(Destination.HOME) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // NavigationRail
            NavigationRail(
                header = {
                    FloatingActionButton(
                        onClick = { },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "새 플레이리스트")
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Destination.entries.forEach { destination ->
                    NavigationRailItem(
                        selected = selectedDestination == destination,
                        onClick = { selectedDestination = destination },
                        icon = {
                            Icon(
                                if (selectedDestination == destination) {
                                    destination.selectedIcon
                                } else {
                                    destination.icon
                                },
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }

            // 콘텐츠 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        selectedDestination.selectedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${selectedDestination.label} 화면",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "왼쪽 Rail에서 다른 메뉴를 선택해보세요",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 성공 표시
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 100.dp, top = 12.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "손가락으로 쉽게 접근!",
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ParameterItem(
    name: String,
    type: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = type,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ComparisonCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "NavigationBar vs NavigationRail 비교",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 비교표
            Column {
                ComparisonRow("기준", "NavigationBar", "NavigationRail", isHeader = true)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                ComparisonRow("방향", "가로 (하단)", "세로 (측면)")
                ComparisonRow("대상 화면", "휴대폰", "태블릿/데스크톱")
                ComparisonRow("항목 수", "3-5개", "3-7개")
                ComparisonRow("FAB 포함", "불가", "header 슬롯")
                ComparisonRow("접근성", "엄지로 닿음", "손가락 측면")
            }
        }
    }
}

@Composable
private fun ComparisonRow(
    criterion: String,
    value1: String,
    value2: String,
    isHeader: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = criterion,
            modifier = Modifier.weight(1f),
            style = if (isHeader) MaterialTheme.typography.labelMedium
            else MaterialTheme.typography.bodySmall,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value1,
            modifier = Modifier.weight(1f),
            style = if (isHeader) MaterialTheme.typography.labelMedium
            else MaterialTheme.typography.bodySmall,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Text(
            text = value2,
            modifier = Modifier.weight(1f),
            style = if (isHeader) MaterialTheme.typography.labelMedium
            else MaterialTheme.typography.bodySmall,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SelectionGuideCard() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "언제 무엇을 선택할까?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 선택 가이드 플로우차트
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "화면 크기 확인",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("  |")
                    Text("  +-- Compact (< 600dp) --> NavigationBar")
                    Text("  |")
                    Text("  +-- Medium (600-840dp) --> NavigationRail")
                    Text("  |")
                    Text("  +-- Expanded (> 840dp) --> NavigationRail")
                    Text("                           (+ Drawer 가능)")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tip: WindowSizeClass를 사용하면 화면 크기를 쉽게 판단할 수 있습니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
