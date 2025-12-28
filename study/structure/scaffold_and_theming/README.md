# Scaffold와 MaterialTheme 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `scaffold` | Scaffold의 기본 슬롯 구조와 paddingValues 사용법 | [📚 학습하기](../../structure/scaffold/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**Scaffold**와 **MaterialTheme**은 Material Design 3 기반 앱을 구축하기 위한 핵심 구성 요소입니다.

| 구성 요소 | 역할 |
|----------|------|
| **Scaffold** | 화면의 기본 레이아웃 구조를 제공하는 컨테이너. TopAppBar, BottomBar, FAB, Snackbar 등을 슬롯 형태로 배치 |
| **MaterialTheme** | 앱 전체에 일관된 디자인 시스템(색상, 타이포그래피, 모양)을 적용하는 래퍼 |

```kotlin
// 기본 구조
MaterialTheme(colorScheme = ..., typography = ...) {
    Scaffold(
        topBar = { TopAppBar(...) },
        floatingActionButton = { FAB(...) }
    ) { paddingValues ->
        // 콘텐츠
    }
}
```

---

## MaterialTheme 핵심 기능

### 1. Color Scheme (색상 스킴)

Material Design 3는 29개의 색상 역할을 정의합니다. 가장 자주 사용하는 색상:

| 색상 역할 | 용도 | 대응 색상 |
|----------|------|----------|
| `primary` | 주요 강조 색상 | `onPrimary` |
| `primaryContainer` | 주요 컨테이너 배경 | `onPrimaryContainer` |
| `secondary` | 보조 강조 색상 | `onSecondary` |
| `surface` | 카드, 시트 등 표면 | `onSurface` |
| `background` | 앱 배경 | `onBackground` |
| `error` | 오류 표시 | `onError` |

```kotlin
// 커스텀 색상 스킴 정의
private val LightColors = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFFCCC2DC),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F)
)
```

### 2. Dynamic Color (동적 색상) - Android 12+

Android 12 이상에서는 사용자의 배경화면을 기반으로 색상을 자동 생성합니다.

```kotlin
val colorScheme = when {
    // Android 12+ Dynamic Color
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColors
    else -> LightColors
}
```

### 3. Typography (타이포그래피)

Material Design 3는 15개의 텍스트 스타일을 정의합니다:

| 카테고리 | 크기 | 용도 |
|---------|------|------|
| **Display** | Large / Medium / Small | 히어로 텍스트, 대형 제목 |
| **Headline** | Large / Medium / Small | 섹션 제목 |
| **Title** | Large / Medium / Small | 카드 제목, 다이얼로그 제목 |
| **Body** | Large / Medium / Small | 본문 텍스트 |
| **Label** | Large / Medium / Small | 버튼, 탭, 캡션 |

```kotlin
// Typography 사용
Text(
    text = "큰 제목",
    style = MaterialTheme.typography.headlineLarge
)

Text(
    text = "본문 내용",
    style = MaterialTheme.typography.bodyMedium
)

Text(
    text = "버튼 라벨",
    style = MaterialTheme.typography.labelLarge
)
```

### 4. Shapes (모양)

컴포넌트의 모서리 형태를 정의합니다:

```kotlin
val shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // Chip, 작은 버튼
    small = RoundedCornerShape(8.dp),        // Card, TextField
    medium = RoundedCornerShape(12.dp),      // Dialog
    large = RoundedCornerShape(16.dp),       // BottomSheet
    extraLarge = RoundedCornerShape(28.dp)   // 대형 컴포넌트
)
```

---

## Scaffold 핵심 기능

### 1. 레이아웃 슬롯

Scaffold는 다양한 UI 요소를 위한 슬롯을 제공합니다:

```kotlin
Scaffold(
    topBar = { /* TopAppBar */ },
    bottomBar = { /* BottomAppBar 또는 NavigationBar */ },
    floatingActionButton = { /* FAB */ },
    floatingActionButtonPosition = FabPosition.End,
    snackbarHost = { /* SnackbarHost */ },
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
) { paddingValues ->
    // 메인 콘텐츠
}
```

### 2. PaddingValues 처리 (중요!)

Scaffold는 `paddingValues`를 content 람다에 전달합니다. **반드시 적용해야 합니다!**

```kotlin
Scaffold(
    topBar = { TopAppBar(title = { Text("제목") }) }
) { paddingValues ->
    // paddingValues 적용
    Column(
        modifier = Modifier
            .padding(paddingValues)  // 필수!
            .fillMaxSize()
    ) {
        // 콘텐츠가 TopAppBar 뒤로 들어가지 않음
    }
}
```

### 3. TopAppBar 타입 선택

| 타입 | 설명 | 스크롤 동작 |
|------|------|------------|
| `TopAppBar` | 기본 앱바 | 고정 |
| `CenterAlignedTopAppBar` | 제목 가운데 정렬 | 고정 |
| `MediumTopAppBar` | 확장 앱바 (중간) | 스크롤 시 축소 |
| `LargeTopAppBar` | 확장 앱바 (대형) | 스크롤 시 축소 |

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableAppBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("큰 제목") },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            // 스크롤 콘텐츠
        }
    }
}
```

### 4. Snackbar와 FAB 조합

Scaffold는 Snackbar와 FAB가 겹치지 않도록 자동으로 조정합니다:

```kotlin
@Composable
fun SnackbarFabDemo() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "작업 완료!",
                            actionLabel = "실행취소"
                        )
                    }
                }
            ) {
                Icon(Icons.Default.Add, "추가")
            }
        }
    ) { paddingValues ->
        // Snackbar가 FAB 위로 올라감 (자동 조정)
    }
}
```

---

## 조합 패턴

### 패턴 1: 기본 앱 구조

```kotlin
@Composable
fun MyApp() {
    MyAppTheme {  // 테마 적용
        Scaffold(
            topBar = { TopAppBar(title = { Text("앱 이름") }) },
            floatingActionButton = { FAB(...) }
        ) { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = "home"
            ) {
                // 네비게이션 그래프
            }
        }
    }
}
```

### 패턴 2: 다크모드 수동 토글

```kotlin
@Composable
fun ThemeToggleApp() {
    var isDarkMode by remember { mutableStateOf(false) }

    MyAppTheme(darkTheme = isDarkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("설정") },
                    actions = {
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(
                                if (isDarkMode) Icons.Default.LightMode
                                else Icons.Default.DarkMode,
                                contentDescription = "테마 전환"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            // 콘텐츠
        }
    }
}
```

### 패턴 3: 브랜드 색상 커스터마이징

```kotlin
// Material Theme Builder (https://m3.material.io/theme-builder) 활용
private val BrandLightColors = lightColorScheme(
    primary = Color(0xFF006D40),      // 브랜드 그린
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8FF8B9),
    // ... 나머지 색상
)

@Composable
fun BrandedApp() {
    MaterialTheme(
        colorScheme = BrandLightColors,
        typography = BrandTypography,
        shapes = BrandShapes
    ) {
        // 브랜드 테마가 적용된 앱
    }
}
```

---

## 베스트 프랙티스

### 1. 항상 테마 색상 사용

```kotlin
// 좋은 예시
Text(color = MaterialTheme.colorScheme.primary)
Card(colors = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.surfaceVariant
))

// 나쁜 예시 (하드코딩)
Text(color = Color(0xFF6200EE))
Card(colors = CardDefaults.cardColors(containerColor = Color.White))
```

### 2. paddingValues 반드시 적용

```kotlin
Scaffold { paddingValues ->
    // 무조건 적용!
    Column(modifier = Modifier.padding(paddingValues)) { ... }
}
```

### 3. Dynamic Color 옵트아웃 옵션 제공

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,  // 사용자가 끌 수 있도록
    content: @Composable () -> Unit
)
```

### 4. 일관된 Typography 사용

```kotlin
// 직접 스타일 정의 대신 MaterialTheme.typography 사용
Text(style = MaterialTheme.typography.titleMedium)  // 좋음
Text(fontSize = 16.sp, fontWeight = FontWeight.Bold)  // 피하기
```

---

## 안티패턴 (피해야 할 사례)

### 1. 색상 하드코딩

```kotlin
// 다크모드에서 텍스트가 보이지 않음!
Text(color = Color(0xFF000000))

// 배경색과 텍스트 색상 불일치
Box(background = Color.White) {
    Text(color = Color.White)  // 안 보임!
}
```

### 2. paddingValues 무시

```kotlin
Scaffold(topBar = { TopAppBar(...) }) { _ ->  // paddingValues 무시!
    Column {  // TopAppBar 뒤로 콘텐츠가 들어감
        Text("내용이 앱바에 가려집니다")
    }
}
```

### 3. 시스템 바 수동 처리

```kotlin
// Scaffold가 자동 처리하는데 수동으로 하면 중복 padding
Scaffold { paddingValues ->
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .statusBarsPadding()  // 중복! 불필요!
    )
}
```

---

## 연습 문제

### 연습 1: 커스텀 색상 스킴 만들기 (기초)

`lightColorScheme()`과 `darkColorScheme()`을 사용하여 나만의 색상 테마를 정의하세요.

**힌트:**
- primary, secondary, background, surface 정의
- isSystemInDarkTheme()으로 시스템 설정 감지
- MaterialTheme에 colorScheme 적용

### 연습 2: Scaffold 구성하기 (중급)

TopAppBar, FAB, SnackbarHost가 있는 완전한 Scaffold를 구성하고, FAB 클릭 시 Snackbar를 표시하세요.

**힌트:**
- remember { SnackbarHostState() }
- rememberCoroutineScope()로 코루틴 스코프 획득
- paddingValues 반드시 적용

### 연습 3: 다크모드 토글 구현 (심화)

앱 내에서 다크모드를 수동 전환하는 기능을 구현하세요. TopAppBar에 토글 버튼을 배치합니다.

**힌트:**
- var isDarkMode by remember { mutableStateOf(false) }
- isDarkMode에 따라 colorScheme 선택
- Icons.Default.LightMode / DarkMode 활용

---

## 다음 학습

- **Navigation 3**: 최신 Compose 네비게이션
- **Compose Testing**: UI 테스트 작성법

---

## 참고 자료

- [Scaffold - Android Developers](https://developer.android.com/develop/ui/compose/components/scaffold)
- [Material Design 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Theming in Compose Codelab](https://developer.android.com/codelabs/jetpack-compose-theming)
- [Material Theme Builder](https://m3.material.io/theme-builder)
- [M2 to M3 Migration Guide](https://developer.android.com/develop/ui/compose/designsystems/material2-material3)
