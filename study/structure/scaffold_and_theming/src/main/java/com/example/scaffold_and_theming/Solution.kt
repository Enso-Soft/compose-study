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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 올바른 구현: MaterialTheme과 Scaffold를 활용한 UI 구성
 *
 * 이 화면은 Material Design 3의 올바른 사용법을 보여줍니다.
 * Problem.kt(안티패턴)와 비교하여 차이점을 확인하세요.
 *
 * 핵심 포인트:
 * 1. MaterialTheme으로 색상 스킴 정의 → 자동 다크모드 대응
 * 2. Scaffold로 표준 레이아웃 슬롯 활용 → 시스템 바 자동 처리
 * 3. MaterialTheme.colorScheme.xxx로 일관된 색상 사용
 * 4. PaddingValues를 content에 반드시 적용 → 콘텐츠 겹침 방지
 * 5. Dynamic Color로 Android 12+ 배경화면 기반 색상 지원
 *
 * @see ProblemScreen 피해야 할 안티패턴 예시
 * @see CustomAppTheme 커스텀 테마 정의 방법
 */

// 커스텀 Light/Dark 색상 스킴 정의
private val CustomLightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F)
)

private val CustomDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0)
)

@Composable
fun CustomAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Dynamic Color: Android 12+ 에서 배경화면 기반 색상
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    var isDarkMode by remember { mutableStateOf(false) }
    var useDynamicColor by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ✅ 커스텀 테마로 감싸기
    CustomAppTheme(
        darkTheme = isDarkMode,
        dynamicColor = useDynamicColor
    ) {
        // ✅ Scaffold로 표준 레이아웃 구성
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Scaffold & MaterialTheme") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("메뉴 버튼 클릭!")
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "메뉴")
                        }
                    },
                    actions = {
                        // 다크모드 토글 버튼
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "테마 전환"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        // ✅ 테마 색상 사용
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            floatingActionButton = {
                // ✅ FAB 자동 배치
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("FAB 클릭!")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "추가")
                }
            },
            snackbarHost = {
                // ✅ SnackbarHost - FAB와 겹치지 않게 자동 조정
                SnackbarHost(hostState = snackbarHostState)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            // ✅ paddingValues 반드시 적용!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // 중요!
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // 핵심 포인트 카드
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        // ✅ 테마 색상 사용
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "해결책: MaterialTheme + Scaffold",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "✅ colorScheme으로 일관된 색상\n" +
                                    "✅ Scaffold 슬롯으로 표준 레이아웃\n" +
                                    "✅ 다크모드 자동 대응\n" +
                                    "✅ PaddingValues로 겹침 방지",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dynamic Color 토글
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "테마 설정",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "다크 모드",
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { isDarkMode = it }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Dynamic Color (Android 12+)",
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Switch(
                                checked = useDynamicColor,
                                onCheckedChange = { useDynamicColor = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 메모 카드 1 - 테마 색상 활용
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "첫 번째 메모",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "MaterialTheme.colorScheme을 사용하면\n다크모드에서 자동으로 색상이 전환됩니다!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 메모 카드 2
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "두 번째 메모",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "테마 시스템으로 일관된 디자인을 유지합니다.\n위 토글로 다크모드를 전환해보세요!",
                            color = MaterialTheme.colorScheme.onTertiaryContainer
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
                            text = "올바른 코드 예시",
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
                                text = """
// ✅ 테마 색상 사용
Text(
    text = "Hello",
    color = MaterialTheme.colorScheme.primary
)

// ✅ Scaffold 슬롯 활용
Scaffold(
    topBar = { TopAppBar(...) },
    floatingActionButton = { FAB(...) },
    snackbarHost = { SnackbarHost(...) }
) { paddingValues ->
    // paddingValues 반드시 적용!
    Column(Modifier.padding(paddingValues)) {
        // content
    }
}
                                """.trimIndent(),
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp)) // FAB 공간
            }
        }
    }
}
