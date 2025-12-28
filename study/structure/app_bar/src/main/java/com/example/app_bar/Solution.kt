package com.example.app_bar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 해결책: TopAppBar를 사용한 올바른 구현
 *
 * 이 화면은 Material 3의 TopAppBar 4가지 유형과
 * scrollBehavior, BottomAppBar 사용법을 보여줍니다.
 *
 * 핵심 포인트:
 * 1. TopAppBar - 기본 앱바
 * 2. CenterAlignedTopAppBar - 제목 중앙 정렬
 * 3. MediumTopAppBar - 중간 크기 제목
 * 4. LargeTopAppBar - 큰 제목
 * 5. scrollBehavior - 스크롤 연동
 * 6. BottomAppBar - 하단 앱바
 *
 * @see ProblemScreen Row로 직접 구현 시 발생하는 문제
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 데모 선택 탭
        ScrollableTabRow(
            selectedTabIndex = selectedDemo,
            edgePadding = 8.dp
        ) {
            Tab(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                text = { Text("Small") }
            )
            Tab(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                text = { Text("Center") }
            )
            Tab(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                text = { Text("Medium") }
            )
            Tab(
                selected = selectedDemo == 3,
                onClick = { selectedDemo = 3 },
                text = { Text("Large") }
            )
            Tab(
                selected = selectedDemo == 4,
                onClick = { selectedDemo = 4 },
                text = { Text("Scroll") }
            )
            Tab(
                selected = selectedDemo == 5,
                onClick = { selectedDemo = 5 },
                text = { Text("Bottom") }
            )
        }

        // 선택된 데모 표시
        when (selectedDemo) {
            0 -> SmallTopAppBarDemo()
            1 -> CenterAlignedTopAppBarDemo()
            2 -> MediumTopAppBarDemo()
            3 -> LargeTopAppBarDemo()
            4 -> ScrollBehaviorDemo()
            5 -> BottomAppBarDemo()
        }
    }
}

/**
 * 기본 TopAppBar (Small) 데모
 *
 * 가장 기본적인 형태의 앱바입니다.
 * 네비게이션과 액션이 적은 화면에 적합합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarDemo() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Small TopAppBar") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("뒤로가기 클릭") }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("검색 클릭") }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "검색")
                    }
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("더보기 클릭") }
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "더보기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        DemoContent(
            padding = padding,
            title = "TopAppBar (Small)",
            description = "가장 기본적인 형태의 앱바입니다.\n" +
                    "네비게이션과 액션이 적은 화면에 적합합니다.\n\n" +
                    "특징:\n" +
                    "- 제목이 왼쪽에 위치\n" +
                    "- 작은 제목 크기\n" +
                    "- 설정, 리스트 화면에 적합",
            codeExample = """
TopAppBar(
    title = { Text("Small TopAppBar") },
    navigationIcon = {
        IconButton(onClick = { /* ... */ }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
        }
    },
    actions = {
        IconButton(onClick = { /* ... */ }) {
            Icon(Icons.Default.Search, "검색")
        }
    }
)
            """.trimIndent()
        )
    }
}

/**
 * CenterAlignedTopAppBar 데모
 *
 * 제목이 중앙에 정렬되는 앱바입니다.
 * 브랜드 강조나 단일 주요 액션 화면에 적합합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopAppBarDemo() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Center Aligned",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("메뉴 클릭") }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("설정 클릭") }
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "설정")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        DemoContent(
            padding = padding,
            title = "CenterAlignedTopAppBar",
            description = "제목이 중앙에 정렬되는 앱바입니다.\n" +
                    "브랜드 강조나 단일 주요 액션 화면에 적합합니다.\n\n" +
                    "특징:\n" +
                    "- 제목이 중앙에 위치\n" +
                    "- 브랜드 아이덴티티 강조\n" +
                    "- 랜딩 페이지, 프로필 화면에 적합",
            codeExample = """
CenterAlignedTopAppBar(
    title = {
        Text(
            "Center Aligned",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    },
    navigationIcon = { /* ... */ },
    actions = { /* ... */ }
)
            """.trimIndent()
        )
    }
}

/**
 * MediumTopAppBar 데모
 *
 * 중간 크기 제목을 가진 앱바입니다.
 * 상세 페이지나 프로필 화면에 적합합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumTopAppBarDemo() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        "Medium TopAppBar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("뒤로가기 클릭") }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("공유 클릭") }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "공유")
                    }
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("더보기 클릭") }
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "더보기")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        DemoContent(
            padding = padding,
            title = "MediumTopAppBar",
            description = "중간 크기 제목을 가진 앱바입니다.\n" +
                    "상세 페이지나 프로필 화면에 적합합니다.\n\n" +
                    "특징:\n" +
                    "- 제목이 두 줄 영역에 표시\n" +
                    "- 중간 크기 제목\n" +
                    "- scrollBehavior와 함께 사용 시 축소 가능",
            codeExample = """
MediumTopAppBar(
    title = {
        Text(
            "Medium TopAppBar",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    },
    navigationIcon = { /* ... */ },
    actions = { /* ... */ },
    scrollBehavior = scrollBehavior  // 선택사항
)
            """.trimIndent()
        )
    }
}

/**
 * LargeTopAppBar 데모
 *
 * 큰 제목을 가진 앱바입니다.
 * 메인 대시보드, 갤러리 화면 등에 적합합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarDemo() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "Large TopAppBar",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("메뉴 클릭") }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "메뉴")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("검색 클릭") }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "검색")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        DemoContent(
            padding = padding,
            title = "LargeTopAppBar",
            description = "큰 제목을 가진 앱바입니다.\n" +
                    "메인 대시보드, 갤러리 화면 등에 적합합니다.\n\n" +
                    "특징:\n" +
                    "- 큰 제목 영역\n" +
                    "- 시각적 임팩트가 강함\n" +
                    "- scrollBehavior와 함께 사용 시 크게 축소됨",
            codeExample = """
LargeTopAppBar(
    title = {
        Text(
            "Large TopAppBar",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    },
    navigationIcon = { /* ... */ },
    actions = { /* ... */ },
    scrollBehavior = scrollBehavior  // 선택사항
)
            """.trimIndent()
        )
    }
}

/**
 * scrollBehavior 데모
 *
 * 스크롤 시 앱바가 축소/확장되는 동작을 보여줍니다.
 * LazyColumn과 함께 사용하여 실제 동작을 확인할 수 있습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollBehaviorDemo() {
    var behaviorType by remember { mutableIntStateOf(0) }

    // scrollBehavior 유형 선택
    val scrollBehavior = when (behaviorType) {
        0 -> TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        1 -> TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        else -> TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        when (behaviorType) {
                            0 -> "Pinned (고정)"
                            1 -> "Enter Always"
                            else -> "Exit Until Collapsed"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            // scrollBehavior 선택 버튼
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "scrollBehavior 선택",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = behaviorType == 0,
                                onClick = { behaviorType = 0 },
                                label = { Text("Pinned") }
                            )
                            FilterChip(
                                selected = behaviorType == 1,
                                onClick = { behaviorType = 1 },
                                label = { Text("EnterAlways") }
                            )
                            FilterChip(
                                selected = behaviorType == 2,
                                onClick = { behaviorType = 2 },
                                label = { Text("ExitUntil") }
                            )
                        }
                    }
                }
            }

            // 설명
            item {
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
                            when (behaviorType) {
                                0 -> "pinnedScrollBehavior"
                                1 -> "enterAlwaysScrollBehavior"
                                else -> "exitUntilCollapsedScrollBehavior"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            when (behaviorType) {
                                0 -> "앱바가 항상 고정되어 있습니다.\n스크롤해도 움직이지 않습니다."
                                1 -> "위로 스크롤하면 앱바가 숨겨지고,\n아래로 스크롤하면 다시 나타납니다."
                                else -> "위로 스크롤하면 앱바가 축소되고,\n콘텐츠 끝에서 다시 확장됩니다."
                            },
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // 스크롤 테스트용 아이템
            items(30) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "스크롤 테스트 아이템 ${index + 1}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * BottomAppBar 데모
 *
 * 화면 하단에 위치하는 앱바입니다.
 * 자주 쓰는 액션을 엄지손가락이 닿기 쉬운 곳에 배치할 때 사용합니다.
 */
@Composable
fun BottomAppBarDemo() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("체크 클릭") }
                    }) {
                        Icon(Icons.Filled.Check, contentDescription = "체크")
                    }
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("편집 클릭") }
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "편집")
                    }
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("마이크 클릭") }
                    }) {
                        Icon(Icons.Filled.Mic, contentDescription = "마이크")
                    }
                    IconButton(onClick = {
                        scope.launch { snackbarHostState.showSnackbar("이미지 클릭") }
                    }) {
                        Icon(Icons.Filled.Image, contentDescription = "이미지")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("FAB 클릭") }
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "추가")
                    }
                },
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        DemoContent(
            padding = padding,
            title = "BottomAppBar",
            description = "화면 하단에 위치하는 앱바입니다.\n" +
                    "자주 쓰는 액션을 엄지손가락이 닿기 쉬운 곳에 배치합니다.\n\n" +
                    "특징:\n" +
                    "- 주요 액션을 하단에 배치\n" +
                    "- FAB과 함께 사용 가능\n" +
                    "- 이메일, 메모 앱 등에 적합",
            codeExample = """
BottomAppBar(
    actions = {
        IconButton(onClick = { /* ... */ }) {
            Icon(Icons.Filled.Check, "체크")
        }
        IconButton(onClick = { /* ... */ }) {
            Icon(Icons.Filled.Edit, "편집")
        }
    },
    floatingActionButton = {
        FloatingActionButton(onClick = { /* ... */ }) {
            Icon(Icons.Filled.Add, "추가")
        }
    }
)
            """.trimIndent()
        )
    }
}

/**
 * 데모 콘텐츠 공통 컴포넌트
 */
@Composable
private fun DemoContent(
    padding: PaddingValues,
    title: String,
    description: String,
    codeExample: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        // 제목
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "설명",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 예시",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = codeExample,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "위의 앱바 아이콘들을 클릭해보세요!",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
