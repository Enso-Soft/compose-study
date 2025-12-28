package com.example.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 Scaffold 구현 (쉬움)
 *
 * TopAppBar와 FAB가 있는 기본 Scaffold를 구현합니다.
 */
@Composable
fun Practice1_BasicScaffold() {
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
                    text = "연습 1: 기본 Scaffold",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TopAppBar와 FAB가 있는 기본 Scaffold를 구현하세요.",
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
                Text("1. TopAppBar에 \"연습 1\" 타이틀 표시")
                Text("2. FloatingActionButton에 '+' 아이콘(Icons.Default.Add) 표시")
                Text("3. content 영역에 \"Hello Scaffold!\" 텍스트를 중앙에 배치")
                Text("4. paddingValues를 content에 반드시 적용")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- @OptIn(ExperimentalMaterial3Api::class) 필요")
                Text("- Scaffold의 topBar, floatingActionButton, content 슬롯 사용")
                Text("- Box + contentAlignment = Alignment.Center로 중앙 배치")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 다음 요구사항을 만족하는 Scaffold를 구현하세요
    //
    // 1. TopAppBar에 "연습 1" 타이틀 표시
    // 2. FloatingActionButton에 '+' 아이콘 표시
    // 3. content 영역에 "Hello Scaffold!" 텍스트 중앙 배치
    // 4. paddingValues를 content에 적용
    //
    // 필요한 import:
    // - androidx.compose.material3.Scaffold
    // - androidx.compose.material3.TopAppBar
    // - androidx.compose.material3.FloatingActionButton
    // - androidx.compose.material.icons.Icons
    // - androidx.compose.material.icons.filled.Add

    Text(
        text = "TODO: 여기에 Scaffold를 구현하세요",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.outline
    )
}

/**
 * 연습 문제 2: Snackbar 통합 (중간)
 *
 * FAB 클릭 시 Snackbar를 표시하는 Scaffold를 구현합니다.
 */
@Composable
fun Practice2_WithSnackbar() {
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
                    text = "연습 2: Snackbar 통합",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "FAB 클릭 시 Snackbar를 표시하는 Scaffold를 구현하세요.",
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
                Text("1. TopAppBar에 \"Snackbar 연습\" 타이틀 표시")
                Text("2. snackbarHost 슬롯에 SnackbarHost 연결")
                Text("3. FAB 클릭 시 \"메모가 추가되었습니다\" 메시지 표시")
                Text("4. Snackbar가 FAB와 겹치지 않는지 확인")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- val snackbarHostState = remember { SnackbarHostState() }")
                Text("- val scope = rememberCoroutineScope()")
                Text("- scope.launch { snackbarHostState.showSnackbar(\"메시지\") }")
                Text("- Scaffold의 snackbarHost 슬롯 사용")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice2_Exercise()
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: FAB 클릭 시 Snackbar를 표시하는 Scaffold를 구현하세요
    //
    // 1. SnackbarHostState를 remember로 생성
    // 2. rememberCoroutineScope로 코루틴 스코프 획득
    // 3. Scaffold의 snackbarHost에 SnackbarHost 연결
    // 4. FAB 클릭 시 "메모가 추가되었습니다" 메시지 표시
    //
    // 필요한 import:
    // - androidx.compose.material3.SnackbarHost
    // - androidx.compose.material3.SnackbarHostState
    // - androidx.compose.runtime.rememberCoroutineScope
    // - kotlinx.coroutines.launch

    Text(
        text = "TODO: 여기에 Snackbar가 있는 Scaffold를 구현하세요",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.outline
    )
}

/**
 * 연습 문제 3: 전체 슬롯 활용 (어려움)
 *
 * 모든 슬롯을 활용한 완전한 화면 구조를 구현합니다.
 */
@Composable
fun Practice3_FullScaffold() {
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
                    text = "연습 3: 전체 슬롯 활용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "모든 슬롯을 활용한 완전한 메모 앱 화면을 구현하세요.",
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
                Text("1. TopAppBar: \"나의 메모\" 제목 + 우측에 설정 아이콘 버튼")
                Text("2. BottomBar: 홈/검색/프로필 3개 탭 (NavigationBar 사용)")
                Text("3. FAB: 가운데 하단에 배치 (FabPosition.Center)")
                Text("4. SnackbarHost: FAB 클릭 시 \"새 메모 작성\" 메시지 표시")
                Text("5. Content: 선택된 탭에 따라 다른 텍스트 표시")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- floatingActionButtonPosition = FabPosition.Center")
                Text("- NavigationBar { NavigationBarItem(...) } 사용")
                Text("- var selectedTab by remember { mutableIntStateOf(0) }")
                Text("- TopAppBar의 actions 파라미터로 아이콘 버튼 추가")
                Text("- Icons.Default.Home, Icons.Default.Search, Icons.Default.Person")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice3_Exercise()
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 모든 슬롯을 활용한 완전한 화면 구조를 구현하세요
    //
    // 요구사항:
    // 1. TopAppBar: "나의 메모" 제목 + 우측 설정 아이콘 버튼 (actions 파라미터)
    // 2. BottomBar: NavigationBar로 홈/검색/프로필 3개 탭
    // 3. FAB: FabPosition.Center로 가운데 하단 배치
    // 4. SnackbarHost: FAB 클릭 시 메시지 표시
    // 5. Content: when(selectedTab) { 0 -> "홈", 1 -> "검색", 2 -> "프로필" }
    //
    // 필요한 import:
    // - androidx.compose.material3.NavigationBar
    // - androidx.compose.material3.NavigationBarItem
    // - androidx.compose.material3.FabPosition
    // - androidx.compose.material.icons.filled.Home
    // - androidx.compose.material.icons.filled.Search
    // - androidx.compose.material.icons.filled.Person
    // - androidx.compose.material.icons.filled.Settings

    Text(
        text = "TODO: 여기에 전체 슬롯을 활용한 Scaffold를 구현하세요",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.outline
    )
}
