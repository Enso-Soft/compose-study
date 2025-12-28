# CompositionLocal 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `slot_api_pattern` | Slot API 패턴과 Composable Lambda를 통한 컴포넌트 설계 | [📚 학습하기](../../architecture/slot_api_pattern/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**CompositionLocal**은 Compose에서 UI 트리를 통해 데이터를 **암묵적으로(implicitly)** 전달하는 도구입니다.

일반적으로 Compose에서 데이터는 매개변수를 통해 명시적으로 전달됩니다. 하지만 테마 색상, 폰트 스타일, Context처럼 **여러 컴포넌트에서 공통으로 사용**되는 데이터를 매번 매개변수로 전달하는 것은 번거롭습니다.

```kotlin
// CompositionLocal 사용
val primaryColor = LocalContentColor.current  // 어디서든 접근 가능!
```

## 핵심 특징

1. **암묵적 전달**: 중간 계층이 매개변수를 전달할 필요 없음
2. **트리 범위 적용**: Composition의 특정 부분에 값을 제공
3. **가장 가까운 Provider**: `.current`는 가장 가까운 ancestor의 값 반환
4. **기본값 제공**: Provider 없어도 기본값으로 동작

## compositionLocalOf vs staticCompositionLocalOf

| 특성 | compositionLocalOf | staticCompositionLocalOf |
|------|-------------------|------------------------|
| **변경 추적** | O (읽는 부분만 recompose) | X (전체 content lambda recompose) |
| **사용 시기** | 값이 변할 가능성 있음 | 값이 거의 변경 안 됨 |
| **성능** | writes 빈번 + reads 적음 | reads 많음 + writes 적음 |
| **예시** | 테마 색상, 애니메이션 값 | Context, Configuration |

```kotlin
// 값이 변경될 수 있음 - 읽는 부분만 recompose
val LocalAppTheme = compositionLocalOf { AppTheme() }

// 값이 거의 변경 안 됨 - 성능 최적화
val LocalAppContext = staticCompositionLocalOf<Context> {
    error("No Context provided")
}
```

## 문제 상황: 매개변수 드릴링(Parameter Drilling)

### 잘못된 코드 예시

CompositionLocal 없이 앱 설정을 전달하면:

```kotlin
@Composable
fun App() {
    val settings = AppSettings(primaryColor = Color.Blue, spacing = 16.dp)
    MainScreen(settings = settings)  // 전달
}

@Composable
fun MainScreen(settings: AppSettings) {
    Column {
        Header(settings = settings)   // 전달만 함
        Content(settings = settings)  // 전달만 함
    }
}

@Composable
fun Header(settings: AppSettings) {
    TopBar(settings = settings)  // 또 전달
}

@Composable
fun TopBar(settings: AppSettings) {
    // 여기서야 실제 사용!
    Text("Title", color = settings.primaryColor)
}
```

### 발생하는 문제점

1. **코드 중복**: 중간 컴포넌트가 사용하지 않는 매개변수를 전달만 함
2. **유지보수 어려움**: 새 설정 추가 시 모든 중간 계층 수정 필요
3. **가독성 저하**: 함수 시그니처가 불필요하게 길어짐
4. **결합도 증가**: 중간 계층이 불필요한 의존성 가짐

## 해결책: CompositionLocal 사용

### 올바른 코드

```kotlin
// 1. CompositionLocal 정의
data class AppSettings(
    val primaryColor: Color = Color.Blue,
    val spacing: Dp = 8.dp
)

val LocalAppSettings = compositionLocalOf { AppSettings() }

// 2. Provider로 값 제공
@Composable
fun App() {
    val settings = AppSettings(primaryColor = Color.Magenta, spacing = 12.dp)

    CompositionLocalProvider(LocalAppSettings provides settings) {
        MainScreen()  // 매개변수 없음!
    }
}

// 3. 중간 계층 - 매개변수 전달 불필요
@Composable
fun MainScreen() {
    Column {
        Header()   // 깔끔!
        Content()  // 깔끔!
    }
}

// 4. 필요한 곳에서 .current로 접근
@Composable
fun TopBar() {
    val settings = LocalAppSettings.current
    Text("Title", color = settings.primaryColor)
}
```

### 해결되는 이유

1. **암묵적 전달**: 중간 계층이 매개변수를 알 필요 없음
2. **트리 범위 적용**: 특정 서브트리에만 다른 값 제공 가능
3. **기본값 제공**: Provider 없어도 기본값으로 동작
4. **타입 안전성**: 컴파일 타임에 타입 체크

## 사용 시나리오

1. **커스텀 디자인 시스템**: 앱 전체에 적용할 스페이싱, 색상, 타이포그래피
2. **사용자 설정 전파**: 다크모드, 언어, 접근성 설정
3. **Context 접근**: `LocalContext.current`로 어디서든 Context 사용
4. **테마 오버라이드**: 특정 영역에서만 다른 테마 적용

## 주의사항

### 1. CompositionLocal이 적합한 경우
- 좋은 기본값이 있어야 함
- 트리 범위 개념이어야 함 (모든 descendants가 사용 가능)
- Cross-cutting concerns (테마, 설정 등)

### 2. CompositionLocal이 부적합한 경우
- 특정 화면의 ViewModel 전달 (명시적 의존성 필요)
- 소수의 컴포넌트만 필요한 데이터
- 비즈니스 로직 전달

```kotlin
// 안티패턴: ViewModel을 CompositionLocal로 전달
val LocalScreenViewModel = compositionLocalOf<ScreenViewModel> {
    error("No ViewModel provided")
}

// 왜 안 좋은가?
// 1. ViewModel은 특정 화면에 종속적 - 트리 범위 개념이 아님
// 2. 모든 하위 컴포넌트가 ViewModel을 알 필요 없음
// 3. 테스트와 재사용성이 떨어짐
```

### 3. 테스트와 Preview에서의 활용

CompositionLocal은 테스트와 Preview에서 의존성을 쉽게 주입할 수 있게 해줍니다.

```kotlin
// Preview에서 커스텀 값 제공
@Preview
@Composable
fun ThemePreview() {
    CompositionLocalProvider(
        LocalAppSettings provides AppSettings(
            primaryColor = Color.Red,
            spacing = 16.dp
        )
    ) {
        MyComponent()
    }
}

// 테스트에서 Mock 값 제공
@Test
fun testWithCustomSettings() {
    composeTestRule.setContent {
        CompositionLocalProvider(
            LocalAppSettings provides testSettings
        ) {
            ComponentUnderTest()
        }
    }
    // 테스트 assertions...
}
```

### 4. 명명 규칙
```kotlin
// Local 접두사 사용 (Google API 가이드라인)
val LocalAppSettings = compositionLocalOf { ... }
val LocalContentColor = ...  // Material 제공
val LocalContext = ...       // Compose 제공

// CompositionLocal/Local을 명사 접미사로 사용하지 않음
val LocalAppSettings = ...   // O (Local 접두사)
val AppSettingsLocal = ...   // X (Local 접미사 - 잘못된 패턴)
```

## Compose 내장 CompositionLocal

Compose와 Material은 자주 사용되는 데이터를 위한 내장 CompositionLocal을 제공합니다.

| CompositionLocal | 용도 |
|------------------|------|
| `LocalContext` | Android Context 접근 |
| `LocalConfiguration` | Configuration (화면 방향 등) |
| `LocalDensity` | 화면 밀도 (dp ↔ px 변환) |
| `LocalContentColor` | 현재 콘텐츠 색상 |
| `LocalLifecycleOwner` | Lifecycle Owner |
| `LocalFocusManager` | 포커스 관리 |

### 활용 예시

```kotlin
// LocalContext - Toast, SharedPreferences, 리소스 접근
@Composable
fun ContextExample() {
    val context = LocalContext.current

    Button(onClick = {
        Toast.makeText(context, "클릭!", Toast.LENGTH_SHORT).show()
    }) {
        Text("토스트 표시")
    }
}

// LocalDensity - dp와 px 간 변환
@Composable
fun DensityExample() {
    val density = LocalDensity.current

    // dp를 px로 변환
    val pxValue = with(density) { 16.dp.toPx() }

    // px를 dp로 변환
    val dpValue = with(density) { 100f.toDp() }
}

// LocalConfiguration - 화면 방향, 크기 감지
@Composable
fun ConfigurationExample() {
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeLayout()
    } else {
        PortraitLayout()
    }
}
```

## 연습 문제

### 연습 1: LocalSpacing 만들기 (기초)
`compositionLocalOf`로 `LocalSpacing`을 생성하고, `CompositionLocalProvider`로 값을 제공한 뒤 `.current`로 접근하여 패딩을 적용하세요.

### 연습 2: static vs dynamic 비교 (중급)
`compositionLocalOf`와 `staticCompositionLocalOf`를 각각 만들고, 값 변경 시 recomposition 범위의 차이를 관찰하세요.

### 연습 3: 중첩 Provider (고급)
기본 테마를 제공하고, 특정 카드 영역에서만 강조 테마로 오버라이드하세요.

## 다음 학습

- `produceState`: 비-Compose 상태를 Compose State로 변환
- `snapshotFlow`: Compose 상태를 Flow로 변환
