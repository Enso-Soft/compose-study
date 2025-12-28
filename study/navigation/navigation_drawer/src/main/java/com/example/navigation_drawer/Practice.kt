package com.example.navigation_drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// ============================================================================
// 연습 1: 기본 Navigation Drawer 만들기 (쉬움)
// ============================================================================

/**
 * 연습 1: 기본 Navigation Drawer 만들기
 *
 * 목표: ModalNavigationDrawer의 기본 구조 이해
 *
 * 요구사항:
 * 1. "홈", "검색", "알림" 3개 메뉴 항목 만들기
 * 2. NavigationDrawerItem 사용
 * 3. 햄버거 메뉴 버튼으로 Drawer 열기
 *
 * 힌트:
 * - rememberDrawerState(DrawerValue.Closed)로 상태 생성
 * - rememberCoroutineScope()로 코루틴 스코프 획득
 * - drawerState.open()은 suspend 함수 -> scope.launch { } 안에서 호출
 * - NavigationDrawerItem에 label, selected, onClick 전달
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicDrawer() {
    // TODO 1: rememberDrawerState로 drawerState 만들기
    // val drawerState = ???

    // TODO 2: rememberCoroutineScope로 scope 만들기
    // val scope = ???

    // 아래 코드를 완성하세요
    // ModalNavigationDrawer(
    //     // TODO 3: drawerState 연결하기
    //     drawerContent = {
    //         ModalDrawerSheet {
    //             Text("메뉴", modifier = Modifier.padding(16.dp))
    //             HorizontalDivider()
    //
    //             // TODO 4: NavigationDrawerItem 3개 추가하기
    //             // - "홈", "검색", "알림"
    //             // NavigationDrawerItem(
    //             //     label = { Text("홈") },
    //             //     selected = false,  // 일단 false로 설정
    //             //     onClick = { }
    //             // )
    //         }
    //     }
    // ) {
    //     Scaffold(
    //         topBar = {
    //             TopAppBar(
    //                 title = { Text("연습 1") },
    //                 navigationIcon = {
    //                     // TODO 5: IconButton으로 메뉴 열기 버튼 만들기
    //                     // IconButton(onClick = { scope.launch { drawerState.open() } }) {
    //                     //     Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
    //                     // }
    //                 }
    //             )
    //         }
    //     ) { padding ->
    //         Box(Modifier.padding(padding).fillMaxSize()) {
    //             Text("메인 콘텐츠 영역", modifier = Modifier.align(Alignment.Center))
    //         }
    //     }
    // }

    // ===== 아래는 연습 전 안내 화면입니다 =====
    PracticeGuide(
        title = "연습 1: 기본 Navigation Drawer 만들기",
        difficulty = "쉬움",
        description = """
            요구사항:
            1. "홈", "검색", "알림" 3개 메뉴 항목 만들기
            2. NavigationDrawerItem 사용
            3. 햄버거 메뉴 버튼으로 Drawer 열기

            힌트:
            - rememberDrawerState(DrawerValue.Closed)로 상태 생성
            - rememberCoroutineScope()로 코루틴 스코프 획득
            - scope.launch { drawerState.open() }으로 열기
        """.trimIndent(),
        todos = listOf(
            "TODO 1: rememberDrawerState로 drawerState 만들기",
            "TODO 2: rememberCoroutineScope로 scope 만들기",
            "TODO 3: ModalNavigationDrawer에 drawerState 연결",
            "TODO 4: NavigationDrawerItem 3개 추가 (홈, 검색, 알림)",
            "TODO 5: IconButton으로 메뉴 열기 버튼 만들기"
        )
    )
}


// ============================================================================
// 연습 2: 아이콘과 선택 상태 관리 (중간)
// ============================================================================

/**
 * 연습 2: 아이콘과 선택 상태 관리
 *
 * 목표: NavigationDrawerItem의 icon, selected 활용 및 상태 관리
 *
 * 요구사항:
 * 1. 각 메뉴 항목에 적절한 아이콘 추가
 * 2. 현재 선택된 메뉴 하이라이트
 * 3. 메뉴 선택 시 Drawer 자동 닫기
 * 4. 선택된 메뉴 이름을 메인 화면에 표시
 *
 * 힌트:
 * - mutableIntStateOf(0)으로 선택된 인덱스 관리
 * - forEachIndexed를 사용하면 인덱스와 항목을 함께 받을 수 있음
 * - selected = (currentIndex == selectedIndex)
 * - onClick에서 selectedIndex 업데이트 + drawerState.close()
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_IconsAndSelection() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // TODO 1: 선택된 메뉴 인덱스 상태 만들기 (remember, mutableIntStateOf)
    // var selectedIndex by remember { mutableIntStateOf(0) }

    // 메뉴 항목 정의 (아이콘은 Icons.Default에서 선택)
    // val menuItems = listOf(
    //     "홈" to Icons.Default.Home,
    //     "즐겨찾기" to Icons.Default.Favorite,
    //     "설정" to Icons.Default.Settings
    // )

    // ModalNavigationDrawer(
    //     drawerState = drawerState,
    //     drawerContent = {
    //         ModalDrawerSheet {
    //             Text("메뉴", modifier = Modifier.padding(16.dp))
    //             HorizontalDivider()
    //
    //             // TODO 2: menuItems를 순회하며 NavigationDrawerItem 만들기
    //             // menuItems.forEachIndexed { index, (title, icon) ->
    //             //     NavigationDrawerItem(
    //             //         icon = { Icon(icon, contentDescription = null) },
    //             //         label = { Text(title) },
    //             //         selected = ???,  // 현재 인덱스와 selectedIndex 비교
    //             //         onClick = {
    //             //             // selectedIndex 업데이트
    //             //             // drawerState.close() 호출
    //             //         }
    //             //     )
    //             // }
    //         }
    //     }
    // ) {
    //     Scaffold(
    //         topBar = { TopAppBar(title = { Text("연습 2") }, ...) }
    //     ) { padding ->
    //         Box(Modifier.padding(padding).fillMaxSize()) {
    //             // TODO 3: 현재 선택된 메뉴 이름 표시하기
    //             // Text("선택됨: ${menuItems[selectedIndex].first}")
    //         }
    //     }
    // }

    // ===== 아래는 연습 전 안내 화면입니다 =====
    PracticeGuide(
        title = "연습 2: 아이콘과 선택 상태 관리",
        difficulty = "중간",
        description = """
            요구사항:
            1. 각 메뉴 항목에 적절한 아이콘 추가
            2. 현재 선택된 메뉴 하이라이트 (selected)
            3. 메뉴 선택 시 Drawer 자동 닫기
            4. 선택된 메뉴 이름을 메인 화면에 표시

            힌트:
            - Icons.Default.Home, Icons.Default.Favorite 등 사용
            - forEachIndexed로 인덱스와 함께 순회
            - selected = (index == selectedIndex)
        """.trimIndent(),
        todos = listOf(
            "TODO 1: selectedIndex 상태 만들기 (mutableIntStateOf)",
            "TODO 2: menuItems.forEachIndexed로 NavigationDrawerItem 생성",
            "TODO 3: 메인 화면에 선택된 메뉴 이름 표시"
        )
    )
}


// ============================================================================
// 연습 3: 완성형 Navigation Drawer (어려움)
// ============================================================================

/**
 * 연습 3: 완성형 Navigation Drawer
 *
 * 목표: 실제 앱 수준의 완성도 높은 Drawer 구현
 *
 * 요구사항:
 * 1. 상단에 사용자 프로필 영역 (아이콘, 이름, 이메일)
 * 2. "메인 메뉴" 섹션: 홈, 대시보드, 알림 (badge로 알림 개수 표시)
 * 3. "설정" 섹션: 설정, 도움말, 로그아웃
 * 4. 섹션 구분선(HorizontalDivider) 사용
 * 5. gesturesEnabled를 토글하는 스위치 추가
 *
 * 힌트:
 * - badge 파라미터: badge = { Text("5") }
 * - HorizontalDivider()로 섹션 구분
 * - 섹션 제목: Text에 Modifier.padding 적용
 * - mutableStateOf(true)로 gesturesEnabled 상태 관리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_CompleteDrawer() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

    // TODO 1: gesturesEnabled 상태 만들기 (기본값 true)
    // var gesturesEnabled by remember { mutableStateOf(true) }

    // ModalNavigationDrawer(
    //     drawerState = drawerState,
    //     // TODO 2: gesturesEnabled 연결하기
    //     // gesturesEnabled = gesturesEnabled,
    //     drawerContent = {
    //         ModalDrawerSheet {
    //             Column(
    //                 modifier = Modifier
    //                     .verticalScroll(rememberScrollState())
    //                     .padding(horizontal = 12.dp)
    //             ) {
    //                 Spacer(Modifier.height(12.dp))
    //
    //                 // TODO 3: 프로필 영역 만들기
    //                 // Row(modifier = Modifier.padding(16.dp)) {
    //                 //     Icon(
    //                 //         Icons.Default.AccountCircle,
    //                 //         contentDescription = null,
    //                 //         modifier = Modifier.size(64.dp)
    //                 //     )
    //                 //     Spacer(Modifier.width(16.dp))
    //                 //     Column {
    //                 //         Text("홍길동", style = MaterialTheme.typography.titleMedium)
    //                 //         Text("hong@example.com", style = MaterialTheme.typography.bodySmall)
    //                 //     }
    //                 // }
    //
    //                 HorizontalDivider()
    //
    //                 // TODO 4: "메인 메뉴" 섹션 제목 추가
    //                 // Text(
    //                 //     "메인 메뉴",
    //                 //     modifier = Modifier.padding(16.dp),
    //                 //     style = MaterialTheme.typography.titleSmall
    //                 // )
    //
    //                 // TODO 5: 홈, 대시보드, 알림 NavigationDrawerItem 추가
    //                 // - 알림에는 badge = { Text("5") } 추가
    //                 // NavigationDrawerItem(
    //                 //     label = { Text("홈") },
    //                 //     icon = { Icon(Icons.Default.Home, null) },
    //                 //     selected = selectedItem == 0,
    //                 //     onClick = { selectedItem = 0; scope.launch { drawerState.close() } }
    //                 // )
    //                 // ...
    //
    //                 HorizontalDivider()
    //
    //                 // TODO 6: "설정" 섹션 제목 추가
    //
    //                 // TODO 7: 설정, 도움말, 로그아웃 NavigationDrawerItem 추가
    //             }
    //         }
    //     }
    // ) {
    //     Scaffold(...) { padding ->
    //         Column(Modifier.padding(padding)) {
    //             // TODO 8: Switch로 gesturesEnabled 토글 UI 만들기
    //             // Row {
    //             //     Text("스와이프 제스처")
    //             //     Switch(checked = gesturesEnabled, onCheckedChange = { gesturesEnabled = it })
    //             // }
    //
    //             Text("현재 선택: $selectedItem")
    //         }
    //     }
    // }

    // ===== 아래는 연습 전 안내 화면입니다 =====
    PracticeGuide(
        title = "연습 3: 완성형 Navigation Drawer",
        difficulty = "어려움",
        description = """
            요구사항:
            1. 상단에 프로필 영역 (아이콘, 이름, 이메일)
            2. "메인 메뉴" 섹션: 홈, 대시보드, 알림 (badge)
            3. "설정" 섹션: 설정, 도움말, 로그아웃
            4. 섹션 구분선(HorizontalDivider) 사용
            5. gesturesEnabled 토글 스위치

            힌트:
            - badge = { Text("5") }로 배지 추가
            - HorizontalDivider()로 섹션 구분
            - Icon(Icons.Default.AccountCircle, ...)로 프로필 아이콘
        """.trimIndent(),
        todos = listOf(
            "TODO 1: gesturesEnabled 상태 만들기",
            "TODO 2: ModalNavigationDrawer에 gesturesEnabled 연결",
            "TODO 3: 프로필 영역 (Row + Icon + Column)",
            "TODO 4: '메인 메뉴' 섹션 제목",
            "TODO 5: 홈, 대시보드, 알림 항목 (알림에 badge)",
            "TODO 6: '설정' 섹션 제목",
            "TODO 7: 설정, 도움말, 로그아웃 항목",
            "TODO 8: gesturesEnabled 토글 Switch"
        )
    )
}


// ============================================================================
// 연습 안내 화면 컴포넌트
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PracticeGuide(
    title: String,
    difficulty: String,
    description: String,
    todos: List<String>
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("연습용 Drawer")
                    Text("직접 구현해보세요!", style = MaterialTheme.typography.bodySmall)
                }
                HorizontalDivider()
                Text("이 Drawer를 완성해보세요!", modifier = Modifier.padding(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 난이도 표시
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (difficulty) {
                            "쉬움" -> MaterialTheme.colorScheme.primaryContainer
                            "중간" -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    )
                ) {
                    Text(
                        text = "난이도: $difficulty",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 요구사항 카드
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "요구사항",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // TODO 목록 카드
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "TODO 목록",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        todos.forEach { todo ->
                            Text(
                                text = todo,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 안내
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "구현 방법",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "1. Practice.kt 파일을 열어주세요\n" +
                                    "2. 해당 연습 함수의 주석 처리된 코드를 찾으세요\n" +
                                    "3. TODO 주석을 따라 코드를 완성하세요\n" +
                                    "4. PracticeGuide(...) 호출을 제거하고 실제 구현으로 교체하세요",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
