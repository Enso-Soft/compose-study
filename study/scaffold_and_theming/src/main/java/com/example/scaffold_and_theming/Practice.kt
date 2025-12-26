package com.example.scaffold_and_theming

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 연습 1: 커스텀 색상 스킴 만들기 (기초)
 *
 * 목표: Light/Dark 색상 스킴을 직접 정의하고 테마에 적용하세요.
 *
 * 힌트:
 * - lightColorScheme() / darkColorScheme() 사용
 * - primary, secondary, background, surface 등 정의
 * - isSystemInDarkTheme()으로 시스템 설정 감지
 */
@Composable
fun Practice1_CustomColorScheme() {
    // TODO: 직접 구현해보세요!

    // 힌트 카드
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
                text = "연습 1: 커스텀 색상 스킴 만들기",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("목표: 나만의 Light/Dark 색상 스킴을 정의하고 테마에 적용하세요.")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "힌트:\n" +
                        "1. lightColorScheme()으로 Light 색상 정의\n" +
                        "2. darkColorScheme()으로 Dark 색상 정의\n" +
                        "3. isSystemInDarkTheme()으로 선택\n" +
                        "4. MaterialTheme에 colorScheme 적용",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    // 구현 영역
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        // 아래 주석을 해제하고 구현해보세요
        // Practice1_Solution()
    }
}

// 정답 예시 (주석 해제하여 확인)
@Composable
private fun Practice1_Solution() {
    // Step 1: 커스텀 색상 정의
    val myLightColors = lightColorScheme(
        primary = Color(0xFF006D40),           // 녹색 계열
        onPrimary = Color.White,
        primaryContainer = Color(0xFF8FF8B9),
        onPrimaryContainer = Color(0xFF002110),
        secondary = Color(0xFF4E6354),
        onSecondary = Color.White,
        background = Color(0xFFFBFDF7),
        onBackground = Color(0xFF1A1C19),
        surface = Color(0xFFFBFDF7),
        onSurface = Color(0xFF1A1C19)
    )

    val myDarkColors = darkColorScheme(
        primary = Color(0xFF73DB9E),
        onPrimary = Color(0xFF00391E),
        primaryContainer = Color(0xFF00522F),
        onPrimaryContainer = Color(0xFF8FF8B9),
        secondary = Color(0xFFB6CCB9),
        onSecondary = Color(0xFF223527),
        background = Color(0xFF1A1C19),
        onBackground = Color(0xFFE2E3DD),
        surface = Color(0xFF1A1C19),
        onSurface = Color(0xFFE2E3DD)
    )

    // Step 2: 시스템 다크모드 감지
    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = if (isDarkTheme) myDarkColors else myLightColors

    // Step 3: MaterialTheme 적용
    MaterialTheme(colorScheme = colorScheme) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "커스텀 테마 적용됨!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "녹색 계열 커스텀 색상 스킴입니다.\n시스템 다크모드에 따라 자동 전환됩니다.",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * 연습 2: Scaffold 구성하기 (중급)
 *
 * 목표: TopAppBar + FAB + SnackbarHost가 있는 Scaffold를 구성하세요.
 *
 * 힌트:
 * - remember { SnackbarHostState() }
 * - TopAppBar의 navigationIcon, actions 활용
 * - paddingValues를 content에 반드시 적용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_ScaffoldSetup() {
    // TODO: 직접 구현해보세요!

    // 힌트 카드
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
                text = "연습 2: Scaffold 구성하기",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("목표: TopAppBar + FAB + SnackbarHost가 있는 Scaffold를 구성하세요.")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "힌트:\n" +
                        "1. SnackbarHostState 생성\n" +
                        "2. Scaffold의 topBar, fab, snackbarHost 슬롯 사용\n" +
                        "3. FAB 클릭 시 Snackbar 표시\n" +
                        "4. paddingValues를 content에 적용!",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    // 아래 주석을 해제하고 확인해보세요
    // Practice2_Solution()
}

// 정답 예시
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Practice2_Solution() {
    // Step 1: SnackbarHostState 생성
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Step 2: Scaffold 구성
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("나의 메모 앱") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, "메뉴")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, "검색")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, "더보기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            // Step 3: FAB 클릭 시 Snackbar 표시
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "새 메모가 추가되었습니다!",
                            actionLabel = "실행취소"
                        )
                    }
                }
            ) {
                Icon(Icons.Default.Add, "추가")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        // Step 4: paddingValues 적용!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "FAB를 클릭해보세요!",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Scaffold가 TopAppBar, FAB, Snackbar를\n자동으로 배치합니다.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 연습 3: 다크모드 토글 구현 (심화)
 *
 * 목표: 앱 내에서 다크모드를 수동 전환하는 기능을 구현하세요.
 *
 * 힌트:
 * - remember로 다크모드 상태 관리
 * - 상태에 따라 테마 적용
 * - TopAppBar에 토글 버튼 배치
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_DarkModeToggle() {
    // TODO: 직접 구현해보세요!

    // 힌트 카드
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
                text = "연습 3: 다크모드 토글 구현",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("목표: 앱 내에서 다크모드를 수동 전환하는 기능을 구현하세요.")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "힌트:\n" +
                        "1. var isDarkMode by remember { mutableStateOf(false) }\n" +
                        "2. isDarkMode에 따라 colorScheme 선택\n" +
                        "3. MaterialTheme에 선택된 colorScheme 적용\n" +
                        "4. TopAppBar에 IconButton으로 토글",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 정답 확인 버튼
    var showSolution by remember { mutableStateOf(false) }
    Button(
        onClick = { showSolution = !showSolution },
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(if (showSolution) "정답 숨기기" else "정답 보기")
    }

    if (showSolution) {
        Spacer(modifier = Modifier.height(16.dp))
        Practice3_Solution()
    }
}

// 정답 예시
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Practice3_Solution() {
    // Step 1: 다크모드 상태 관리
    var isDarkMode by remember { mutableStateOf(false) }
    var useDynamicColor by remember { mutableStateOf(true) }

    // Step 2: 색상 스킴 선택
    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkMode -> darkColorScheme(
            primary = Color(0xFFD0BCFF),
            onPrimary = Color(0xFF381E72),
            primaryContainer = Color(0xFF4F378B),
            background = Color(0xFF1C1B1F),
            onBackground = Color(0xFFE6E1E5),
            surface = Color(0xFF1C1B1F),
            onSurface = Color(0xFFE6E1E5)
        )
        else -> lightColorScheme(
            primary = Color(0xFF6750A4),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFEADDFF),
            background = Color(0xFFFFFBFE),
            onBackground = Color(0xFF1C1B1F),
            surface = Color(0xFFFFFBFE),
            onSurface = Color(0xFF1C1B1F)
        )
    }

    // Step 3: MaterialTheme 적용
    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("테마 토글 예제") },
                    actions = {
                        // Step 4: 토글 버튼
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "테마 전환"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "현재 모드: ${if (isDarkMode) "다크" else "라이트"}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "TopAppBar의 아이콘을 클릭하여\n다크모드를 전환해보세요!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Dynamic Color",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Switch(
                                checked = useDynamicColor,
                                onCheckedChange = { useDynamicColor = it }
                            )
                        }
                    }
                }
            }
        }
    }
}
