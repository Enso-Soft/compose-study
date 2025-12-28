package com.example.navigation_drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Solution: ModalNavigationDrawer를 사용한 올바른 구현
 *
 * ModalNavigationDrawer는 Material 3에서 제공하는 완전한 구현체로:
 * 1. 스와이프 제스처 자동 지원
 * 2. 스크림 자동 처리
 * 3. 부드러운 애니메이션 내장
 * 4. 접근성 자동 지원
 * 5. Predictive back gesture 지원
 * 6. DrawerState로 깔끔한 상태 관리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    // 1. DrawerState 생성 - Drawer의 열림/닫힘 상태를 관리합니다
    // DrawerValue.Closed: 처음에는 닫힌 상태로 시작
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // 2. 코루틴 스코프 - open()/close()는 suspend 함수라서 필요합니다
    val scope = rememberCoroutineScope()

    // 3. 현재 선택된 메뉴 인덱스
    var selectedItem by remember { mutableIntStateOf(0) }

    // 4. gesturesEnabled 상태 - 스와이프 제스처 활성화/비활성화
    var gesturesEnabled by remember { mutableStateOf(true) }

    // 메뉴 항목 정의
    val menuItems = listOf(
        MenuItem("홈", Icons.Default.Home),
        MenuItem("즐겨찾기", Icons.Default.Favorite),
        MenuItem("알림", Icons.Default.Notifications),
        MenuItem("메시지", Icons.Default.Email),
        MenuItem("프로필", Icons.Default.AccountCircle),
        MenuItem("설정", Icons.Default.Settings)
    )

    // 5. ModalNavigationDrawer - 핵심 컴포넌트
    ModalNavigationDrawer(
        drawerState = drawerState,  // 상태 연결
        gesturesEnabled = gesturesEnabled,  // 스와이프 제스처 허용 여부

        // 6. Drawer 내용 정의
        drawerContent = {
            // ModalDrawerSheet: Material 3 스타일이 적용된 Drawer 컨테이너
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Drawer 헤더
                    Text(
                        text = "Navigation Drawer",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // 메뉴 항목들
                    menuItems.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(item.title) },
                            selected = selectedItem == index,  // 선택 상태 표시
                            onClick = {
                                selectedItem = index
                                // 메뉴 선택 시 Drawer 닫기
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            // 알림 메뉴에는 badge 추가
                            badge = if (index == 2) {
                                { Text("3") }  // 알림 개수 표시
                            } else null
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    ) {
        // 7. 메인 화면 콘텐츠
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Solution: ModalNavigationDrawer") },
                    navigationIcon = {
                        // 햄버거 메뉴 버튼
                        IconButton(onClick = {
                            // Drawer 열기 (suspend 함수이므로 scope.launch 필요)
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "메뉴 열기"
                            )
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
            ) {
                // 성공 안내 카드
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ModalNavigationDrawer의 장점",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val benefits = listOf(
                            "1. 화면 왼쪽 가장자리에서 스와이프하면 열립니다!",
                            "2. 스크림(어두운 배경)이 자동으로 처리됩니다",
                            "3. 애니메이션이 부드럽고 완벽합니다",
                            "4. 접근성(TalkBack)이 자동 지원됩니다",
                            "5. Android 13+ 뒤로가기 제스처 미리보기 지원"
                        )

                        benefits.forEach { benefit ->
                            Text(
                                text = benefit,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 현재 선택된 메뉴 표시
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "현재 선택된 메뉴",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = menuItems[selectedItem].title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // gesturesEnabled 토글
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "스와이프 제스처",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (gesturesEnabled) {
                                    "활성화됨 - 왼쪽 가장자리에서 스와이프 가능"
                                } else {
                                    "비활성화됨 - 버튼으로만 열 수 있음"
                                },
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Switch(
                            checked = gesturesEnabled,
                            onCheckedChange = { gesturesEnabled = it },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 테스트 안내
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "테스트해보세요!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. 화면 왼쪽 가장자리에서 오른쪽으로 스와이프\n" +
                                    "2. 햄버거 메뉴 버튼 클릭\n" +
                                    "3. 메뉴 항목 선택 (자동으로 닫힘)\n" +
                                    "4. 스크림(어두운 영역) 클릭\n" +
                                    "5. 스와이프 제스처 토글 후 다시 시도",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

/**
 * 메뉴 항목 데이터 클래스
 */
private data class MenuItem(
    val title: String,
    val icon: ImageVector
)
