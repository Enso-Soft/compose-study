# WindowInsets 학습

## 개념

**WindowInsets**는 시스템 UI(상태바, 네비게이션 바, 키보드 등)가 화면에서 차지하는 영역 정보를 제공하는 API입니다. 앱이 시스템 UI 뒤로 콘텐츠를 그릴 때(edge-to-edge) 콘텐츠가 가려지지 않도록 **안전한 영역**을 알려줍니다.

## Android 15 (SDK 35) 중요 변화

| 버전 | 기본 동작 |
|------|----------|
| Android 14 이하 | 앱이 시스템 바 영역 밖에 그려짐 |
| **Android 15+** | **edge-to-edge 강제 적용** - 앱이 자동으로 시스템 바 뒤에 그려짐 |

> **중요**: Android 15 이상에서는 WindowInsets 처리가 **필수**입니다!

## 핵심 Inset 유형

### "Safe" Inset 유형 (권장)

| Inset | 용도 |
|-------|------|
| `safeDrawing` | 시스템 UI와 **시각적 겹침 방지** (가장 많이 사용) |
| `safeGestures` | **제스처 충돌 방지** (시스템 스와이프 영역) |
| `safeContent` | safeDrawing + safeGestures (가장 안전) |

### 개별 Inset 유형

| Inset | 용도 |
|-------|------|
| `statusBars` | 상태바 영역 (상단) |
| `navigationBars` | 네비게이션 바 영역 (하단/측면) |
| `systemBars` | statusBars + navigationBars |
| `ime` | 소프트웨어 키보드 영역 |
| `displayCutout` | 노치/펀치홀 영역 |

## 문제 상황: Inset을 처리하지 않으면?

### 1. 콘텐츠가 상태바에 가려짐
```kotlin
// 잘못된 코드
Column {
    Text("이 텍스트는 상태바 뒤에 숨겨집니다!")
}
```

### 2. 버튼이 네비게이션 바에 가려짐
```kotlin
// 잘못된 코드
Box(Modifier.fillMaxSize()) {
    Button(
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Text("클릭할 수 없음!")  // 네비게이션 바에 가려짐
    }
}
```

### 3. TextField가 키보드에 가려짐
```kotlin
// 잘못된 코드
Column {
    Spacer(Modifier.weight(1f))
    TextField(...)  // 키보드가 올라오면 보이지 않음!
}
```

## 해결책: WindowInsets 활용

### 방법 1: Scaffold 사용 (가장 권장)

```kotlin
Scaffold { paddingValues ->
    LazyColumn(
        contentPadding = paddingValues  // 자동으로 insets 처리
    ) {
        items(100) { Text("아이템 $it") }
    }
}
```

### 방법 2: safeDrawingPadding() 사용

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()  // 시스템 UI 피함
) {
    Text("안전하게 표시됨")
}
```

### 방법 3: windowInsetsPadding() 직접 사용

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.systemBars)
) {
    Text("상태바와 네비게이션 바 피함")
}
```

### 방법 4: 키보드(IME) 처리

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .imePadding()  // 키보드가 올라오면 자동 패딩
) {
    Spacer(Modifier.weight(1f))
    TextField(...)  // 키보드 위에 표시됨!
}
```

## 핵심 Modifier 정리

| Modifier | 역할 |
|----------|------|
| `safeDrawingPadding()` | 모든 시스템 UI 피함 (권장) |
| `safeContentPadding()` | 시스템 UI + 제스처 영역 피함 |
| `systemBarsPadding()` | 상태바 + 네비게이션 바만 |
| `statusBarsPadding()` | 상태바만 |
| `navigationBarsPadding()` | 네비게이션 바만 |
| `imePadding()` | 키보드만 |
| `windowInsetsPadding(insets)` | 커스텀 inset 지정 |

## Inset 값 읽기

```kotlin
val statusBarHeight = WindowInsets.statusBars
    .asPaddingValues()
    .calculateTopPadding()

val navBarHeight = WindowInsets.navigationBars
    .asPaddingValues()
    .calculateBottomPadding()

val imeHeight = WindowInsets.ime
    .asPaddingValues()
    .calculateBottomPadding()
```

## 주의사항

1. **Modifier 순서가 중요**
   ```kotlin
   // 올바른 순서
   Modifier
       .safeDrawingPadding()  // 먼저 시스템 바 처리
       .imePadding()          // 그 다음 키보드 처리
   ```

2. **Scaffold와 중복 사용 주의**
   - Scaffold 내부에서 `safeDrawingPadding()` 사용 시 패딩이 두 번 적용됨
   - Scaffold 사용 시 `paddingValues`만 사용

3. **enableEdgeToEdge() 호출 필수**
   ```kotlin
   class MainActivity : ComponentActivity() {
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           enableEdgeToEdge()  // 이 호출이 있어야 insets가 제대로 동작
           setContent { ... }
       }
   }
   ```

## 연습 문제

1. **기본**: Box에 `safeDrawingPadding()` 적용하기
2. **중급**: 하단 TextField에 `imePadding()` 적용하여 키보드 대응
3. **고급**: Scaffold로 TopAppBar가 있는 리스트 화면 만들기

## 다음 학습

- Gesture 처리 (pointerInput, draggable)
- Canvas & Graphics
