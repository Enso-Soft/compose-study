package com.example.app_bar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * TopAppBar 연습 문제
 *
 * 3가지 난이도의 연습 문제를 통해 TopAppBar 사용법을 익힙니다.
 *
 * 연습 1: 기본 TopAppBar 만들기 (쉬움)
 * 연습 2: scrollBehavior 적용하기 (중간)
 * 연습 3: 검색 + 메뉴 아이콘 TopAppBar (어려움)
 */

@Composable
fun PracticeNavigator() {
    var selectedPractice by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedPractice == 0,
                onClick = { selectedPractice = 0 },
                label = { Text("기본") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("스크롤") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("액션") }
            )
        }

        when (selectedPractice) {
            0 -> Practice1_BasicTopAppBar()
            1 -> Practice2_ScrollBehavior()
            2 -> Practice3_ActionsTopAppBar()
        }
    }
}

/**
 * 연습 1: 기본 TopAppBar 만들기 (쉬움)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicTopAppBar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 1: 기본 TopAppBar",
            difficulty = "쉬움",
            requirements = listOf(
                "제목: \"My App\"",
                "navigationIcon: 햄버거 메뉴 (Icons.Default.Menu)",
                "클릭 이벤트는 TODO 주석으로"
            )
        )

        HintCard(
            hints = listOf(
                "TopAppBar(title = { Text(...) })",
                "navigationIcon = { IconButton(...) { Icon(...) } }",
                "Icons.Default.Menu 사용"
            )
        )

        // TODO: 여기에 Scaffold + TopAppBar를 구현하세요
        // 1. Scaffold의 topBar에 TopAppBar 배치
        // 2. title = { Text("My App") }
        // 3. navigationIcon에 IconButton + Menu 아이콘

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO: Scaffold + TopAppBar 구현하기",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 연습 2: scrollBehavior 적용하기 (중간)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_ScrollBehavior() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 2: scrollBehavior 적용",
            difficulty = "중간",
            requirements = listOf(
                "MediumTopAppBar 사용",
                "enterAlwaysScrollBehavior 적용",
                "LazyColumn과 함께 동작",
                "Modifier.nestedScroll() 연결"
            )
        )

        HintCard(
            hints = listOf(
                "val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()",
                "Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection))",
                "MediumTopAppBar(scrollBehavior = scrollBehavior)"
            )
        )

        // TODO: 여기에 스크롤 가능한 TopAppBar를 구현하세요
        // 1. scrollBehavior 생성
        // 2. Scaffold에 nestedScroll 연결
        // 3. MediumTopAppBar에 scrollBehavior 전달
        // 4. LazyColumn으로 20개 아이템 표시

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO: MediumTopAppBar + scrollBehavior 구현하기\n스크롤 시 앱바가 축소/확장됨",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 연습 3: 검색 + 메뉴 아이콘 TopAppBar (어려움)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_ActionsTopAppBar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 3: 액션 아이콘 TopAppBar",
            difficulty = "어려움",
            requirements = listOf(
                "제목: \"연습문제\"",
                "navigationIcon: 뒤로가기 (AutoMirrored)",
                "actions: 검색 + 더보기 아이콘",
                "클릭 시 Snackbar로 메시지 표시"
            )
        )

        HintCard(
            hints = listOf(
                "Icons.AutoMirrored.Filled.ArrowBack (RTL 지원)",
                "actions = { IconButton(...) IconButton(...) }",
                "val scope = rememberCoroutineScope()",
                "scope.launch { snackbarHostState.showSnackbar(...) }"
            )
        )

        // TODO: 여기에 액션 아이콘이 있는 TopAppBar를 구현하세요
        // 1. SnackbarHostState 생성
        // 2. Scaffold에 snackbarHost 설정
        // 3. TopAppBar에 navigationIcon + actions 추가
        // 4. 검색 클릭 시 "검색" Snackbar
        // 5. 더보기 클릭 시 "메뉴" Snackbar

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO: TopAppBar + actions + Snackbar 구현하기\n뒤로가기 + 검색 + 더보기 아이콘",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// =============================================================================
// 공통 컴포넌트
// =============================================================================

@Composable
private fun ExerciseCard(
    title: String,
    difficulty: String,
    requirements: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                    fontWeight = FontWeight.Bold
                )
                AssistChip(
                    onClick = { },
                    label = { Text(difficulty) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "요구사항:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            requirements.forEach { req ->
                Text(
                    text = "• $req",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    var expanded by remember { mutableStateOf(false) }

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
                    text = "힌트",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "숨기기" else "보기")
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                hints.forEach { hint ->
                    Text(
                        text = "• $hint",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}
