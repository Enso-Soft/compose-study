package com.example.navigation_rail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 NavigationRail 만들기 (쉬움)
 *
 * NavigationRail의 기본 구조를 이해하고 구현합니다.
 */
@Composable
fun Practice1Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 NavigationRail 만들기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "3개의 목적지(홈, 검색, 프로필)가 있는 기본 NavigationRail을 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. NavigationRail을 Row 안에 배치")
                Text("2. 3개의 NavigationRailItem 추가:")
                Text("   - 홈 (Icons.Default.Home)")
                Text("   - 검색 (Icons.Default.Search)")
                Text("   - 프로필 (Icons.Default.Person)")
                Text("3. 각 항목에 아이콘과 레이블 표시")
                Text("4. 선택 상태 관리 (remember 사용)")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- var selectedIndex by remember { mutableIntStateOf(0) }")
                Text("- NavigationRailItem의 selected 파라미터 사용")
                Text("- onClick에서 selectedIndex 업데이트")
            }
        }

        // 구현 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "구현 영역",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Practice1Exercise()
                }
            }
        }
    }
}

@Composable
private fun Practice1Exercise() {
    // TODO: 3개의 목적지가 있는 기본 NavigationRail을 구현하세요
    //
    // 1. var selectedIndex by remember { mutableIntStateOf(0) } 로 상태 선언
    // 2. Row { NavigationRail { ... } Content { ... } } 구조로 배치
    // 3. NavigationRailItem에 selected, onClick, icon, label 설정
    //
    // 구현할 코드:
    // Row {
    //     NavigationRail {
    //         NavigationRailItem(
    //             selected = ...,
    //             onClick = { ... },
    //             icon = { Icon(...) },
    //             label = { Text(...) }
    //         )
    //         // ... 더 많은 항목
    //     }
    //     // 콘텐츠 영역
    // }

    Text(
        text = "TODO: 기본 NavigationRail을 구현하세요",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * 연습 문제 2: FAB가 있는 NavigationRail (중간)
 *
 * header 슬롯을 활용하여 FAB가 포함된 NavigationRail을 구현합니다.
 */
@Composable
fun Practice2Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: FAB가 있는 NavigationRail",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "header 슬롯에 FloatingActionButton이 있는 NavigationRail을 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. NavigationRail의 header 파라미터 사용")
                Text("2. header에 FloatingActionButton 배치")
                Text("   - 아이콘: Icons.Default.Edit (글쓰기)")
                Text("3. FAB 클릭 시 메시지 표시")
                Text("   - SnackbarHost 사용 권장")
                Text("4. 4개의 목적지 (메일, 채팅, 미팅, 연락처)")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- NavigationRail(header = { ... }) { ... }")
                Text("- FloatingActionButton(onClick = { }) { Icon(...) }")
                Text("- val snackbarHostState = remember { SnackbarHostState() }")
                Text("- LaunchedEffect로 Snackbar 표시")
            }
        }

        // 구현 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "구현 영역",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Practice2Exercise()
                }
            }
        }
    }
}

@Composable
private fun Practice2Exercise() {
    // TODO: header 슬롯에 FAB가 있는 NavigationRail을 구현하세요
    //
    // 1. SnackbarHostState를 remember로 선언
    // 2. NavigationRail의 header 파라미터에 FAB 배치
    // 3. FAB 클릭 시 Snackbar 표시 (rememberCoroutineScope 활용)
    // 4. 4개의 NavigationRailItem 추가
    //
    // 구현할 코드:
    // val snackbarHostState = remember { SnackbarHostState() }
    // val scope = rememberCoroutineScope()
    //
    // Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
    //     Row {
    //         NavigationRail(
    //             header = {
    //                 FloatingActionButton(
    //                     onClick = {
    //                         scope.launch {
    //                             snackbarHostState.showSnackbar("글쓰기 클릭!")
    //                         }
    //                     }
    //                 ) { Icon(Icons.Default.Edit, "글쓰기") }
    //             }
    //         ) {
    //             // NavigationRailItems...
    //         }
    //         // 콘텐츠 영역
    //     }
    // }

    Text(
        text = "TODO: FAB가 있는 NavigationRail을 구현하세요",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * 연습 문제 3: 적응형 네비게이션 (어려움)
 *
 * 화면 크기에 따라 NavigationBar 또는 NavigationRail을 표시합니다.
 */
@Composable
fun Practice3Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 적응형 네비게이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "화면 크기에 따라 NavigationBar 또는 NavigationRail을 자동으로 전환하는 " +
                            "적응형 네비게이션을 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. BoxWithConstraints로 화면 너비 감지")
                Text("2. 화면 너비 기준:")
                Text("   - 600dp 미만: NavigationBar (하단)")
                Text("   - 600dp 이상: NavigationRail (측면)")
                Text("3. 동일한 목적지와 선택 상태 공유")
                Text("4. 전환 시 상태 유지")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- BoxWithConstraints { maxWidth -> ... }")
                Text("- if (maxWidth < 600.dp) { ... } else { ... }")
                Text("- 상태는 BoxWithConstraints 밖에서 선언")
                Text("- 공통 목적지 리스트를 사용하여 중복 제거")
            }
        }

        // 추가 도전
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "추가 도전 (선택)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "WindowSizeClass를 사용하여 더 정교하게 구현해보세요. " +
                            "(material3-window-size-class 의존성 필요)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 구현 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "구현 영역",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Practice3Exercise()
                }
            }
        }
    }
}

@Composable
private fun Practice3Exercise() {
    // TODO: 화면 크기에 따라 NavigationBar 또는 NavigationRail을 표시하세요
    //
    // 1. 상위에서 선택 상태 관리
    //    var selectedIndex by remember { mutableIntStateOf(0) }
    //
    // 2. BoxWithConstraints로 화면 크기 감지
    //
    // 3. 조건에 따라 다른 레이아웃 표시
    //
    // 구현할 코드:
    // var selectedIndex by remember { mutableIntStateOf(0) }
    // val destinations = listOf(
    //     "홈" to Icons.Default.Home,
    //     "검색" to Icons.Default.Search,
    //     "프로필" to Icons.Default.Person
    // )
    //
    // BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    //     if (maxWidth < 600.dp) {
    //         // Compact: NavigationBar 사용
    //         Scaffold(
    //             bottomBar = {
    //                 NavigationBar {
    //                     destinations.forEachIndexed { index, (label, icon) ->
    //                         NavigationBarItem(...)
    //                     }
    //                 }
    //             }
    //         ) { padding ->
    //             // 콘텐츠
    //         }
    //     } else {
    //         // Medium/Expanded: NavigationRail 사용
    //         Row {
    //             NavigationRail {
    //                 destinations.forEachIndexed { index, (label, icon) ->
    //                     NavigationRailItem(...)
    //                 }
    //             }
    //             // 콘텐츠
    //         }
    //     }
    // }

    Text(
        text = "TODO: 적응형 네비게이션을 구현하세요",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
