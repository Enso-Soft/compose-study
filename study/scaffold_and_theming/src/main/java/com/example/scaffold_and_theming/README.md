# Scaffold와 MaterialTheme 학습

## 개념

**Scaffold**는 Material Design의 기본 레이아웃 구조를 제공하는 컨테이너입니다. TopAppBar, BottomBar, FAB, Snackbar 등의 표준 UI 요소를 **슬롯(slot)** 형태로 쉽게 배치할 수 있습니다.

**MaterialTheme**은 앱 전체에 일관된 디자인 시스템을 적용하는 래퍼입니다. Color Scheme, Typography, Shapes를 정의하면 모든 Material 컴포넌트에 자동으로 반영됩니다.

## 핵심 특징

### Scaffold

1. **표준 레이아웃 슬롯**: topBar, bottomBar, floatingActionButton, snackbarHost
2. **자동 Insets 처리**: 시스템 바(상태바, 네비게이션 바)와 충돌 방지
3. **PaddingValues 제공**: content 람다에 padding 정보 전달
4. **FAB-Snackbar 조정**: Snackbar가 FAB와 겹치지 않도록 자동 조정

### MaterialTheme

1. **Color Scheme**: 29개의 색상 역할 (primary, secondary, surface, background 등)
2. **Typography**: 15개의 텍스트 스타일 (displayLarge ~ labelSmall)
3. **Shapes**: 컴포넌트 모서리 형태 (small, medium, large)
4. **Dynamic Color (Android 12+)**: 배경화면 기반 색상 자동 생성

## 문제 상황: 테마와 Scaffold 없이 UI 구성

### 잘못된 코드 예시

```kotlin
// ❌ 하드코딩된 색상
Text(
    text = "Hello",
    color = Color(0xFF000000)  // 다크모드에서 안 보임!
)

// ❌ 수동 레이아웃 구성
Column {
    Box(
        modifier = Modifier
            .height(56.dp)  // 하드코딩!
            .background(Color(0xFF6200EE))
    ) {
        Text("앱바", color = Color.White)
    }
    // 콘텐츠... 시스템 바와 겹칠 수 있음
}

// ❌ FAB 위치 수동 계산
Box(
    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
) {
    FloatingActionButton(onClick = {}) { ... }
}
```

### 발생하는 문제점

1. **다크모드 미대응**: 하드코딩된 색상은 시스템 테마와 동기화되지 않음
2. **시스템 바 충돌**: WindowInsets를 수동 처리해야 함
3. **일관성 없는 UI**: 각 컴포넌트마다 다른 색상/스타일
4. **FAB-Snackbar 충돌**: 위치를 수동 계산해야 함
5. **유지보수 어려움**: 색상 변경 시 모든 곳에서 수정 필요

## 해결책: MaterialTheme + Scaffold 사용

### 올바른 코드

```kotlin
@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("앱 제목") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("메시지!")
                    }
                }
            ) {
                Icon(Icons.Default.Add, "추가")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // ✅ paddingValues 반드시 적용!
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "내용",
                color = MaterialTheme.colorScheme.onBackground  // ✅ 테마 색상
            )
        }
    }
}
```

### 해결되는 이유

1. **자동 다크모드 대응**: `isSystemInDarkTheme()`으로 시스템 설정 감지
2. **Dynamic Color**: Android 12+에서 배경화면 기반 색상 자동 생성
3. **표준 레이아웃**: Scaffold가 TopAppBar, FAB, Snackbar 자동 배치
4. **Insets 자동 처리**: `paddingValues`에 시스템 바 정보 포함
5. **일관된 디자인**: `MaterialTheme.colorScheme.xxx`로 통일된 색상

## 사용 시나리오

### 1. 기본 앱 구조

```kotlin
setContent {
    MyAppTheme {  // 테마 적용
        Scaffold(
            topBar = { ... },
            floatingActionButton = { ... }
        ) { padding ->
            NavHost(modifier = Modifier.padding(padding)) { ... }
        }
    }
}
```

### 2. 다크모드 수동 토글

```kotlin
var isDarkMode by remember { mutableStateOf(false) }

MyAppTheme(darkTheme = isDarkMode) {
    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = { isDarkMode = !isDarkMode }) {
                        Icon(
                            if (isDarkMode) Icons.Default.LightMode
                            else Icons.Default.DarkMode,
                            "테마 전환"
                        )
                    }
                }
            )
        }
    ) { ... }
}
```

### 3. 커스텀 색상 스킴

```kotlin
private val BrandLightColors = lightColorScheme(
    primary = Color(0xFF006D40),  // 브랜드 색상
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8FF8B9),
    // ... 나머지 색상 정의
)

private val BrandDarkColors = darkColorScheme(
    primary = Color(0xFF73DB9E),
    onPrimary = Color(0xFF00391E),
    // ... 나머지 색상 정의
)
```

## 주의사항

### 1. paddingValues 반드시 적용

```kotlin
Scaffold { paddingValues ->
    // ❌ paddingValues 무시하면 콘텐츠가 앱바 뒤로 들어감
    Column { ... }

    // ✅ paddingValues 적용
    Column(modifier = Modifier.padding(paddingValues)) { ... }
}
```

### 2. Dynamic Color 버전 체크

```kotlin
val colorScheme = when {
    // Android 12 (API 31) 이상에서만 사용 가능
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    }
    // ...
}
```

### 3. 테마 색상 사용

```kotlin
// ❌ 하드코딩
Text(color = Color(0xFF000000))

// ✅ 테마 색상 (자동 다크모드 대응)
Text(color = MaterialTheme.colorScheme.onBackground)
```

### 4. TopAppBar 타입 선택

| 타입 | 설명 | 스크롤 동작 |
|------|------|------------|
| `TopAppBar` | 기본 앱바 | 고정 |
| `CenterAlignedTopAppBar` | 제목 가운데 정렬 | 고정 |
| `MediumTopAppBar` | 확장 앱바 (중간) | 스크롤 시 축소 |
| `LargeTopAppBar` | 확장 앱바 (대형) | 스크롤 시 축소 |

## 연습 문제

### 연습 1: 커스텀 색상 스킴 만들기 (기초)
`lightColorScheme()`과 `darkColorScheme()`을 사용하여 나만의 색상 테마를 정의하세요.

### 연습 2: Scaffold 구성하기 (중급)
TopAppBar, FAB, SnackbarHost가 있는 완전한 Scaffold를 구성하고, FAB 클릭 시 Snackbar를 표시하세요.

### 연습 3: 다크모드 토글 구현 (심화)
앱 내에서 다크모드를 수동 전환하는 기능을 구현하세요. TopAppBar에 토글 버튼을 배치합니다.

## 다음 학습

- **compose_testing**: Compose UI 테스트
- **interoperability**: View-Compose 상호운용

## 참고 자료

- [Scaffold - Android Developers](https://developer.android.com/develop/ui/compose/components/scaffold)
- [Material Design 3 in Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Theming in Compose with Material 3](https://developer.android.com/codelabs/jetpack-compose-theming)
- [Material Theme Builder](https://m3.material.io/theme-builder)
