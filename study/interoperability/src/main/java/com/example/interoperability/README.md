# View-Compose 상호운용성 (Interoperability)

## 개념

**상호운용성(Interoperability)**은 기존 View 시스템과 Jetpack Compose 간의 **양방향 통합**을 가능하게 하는 API들입니다.

```
┌─────────────────────────────────────────────────────┐
│                      Android 앱                      │
├─────────────────────┬───────────────────────────────┤
│   기존 View 시스템   │      Jetpack Compose            │
│   (XML, Fragment)   │      (@Composable)            │
├─────────────────────┼───────────────────────────────┤
│                     │                               │
│  ┌───────────────┐  │  ┌─────────────────────────┐  │
│  │  ComposeView  │──┼──│  Compose UI를 View에     │  │
│  │               │  │  │  삽입할 때 사용            │  │
│  └───────────────┘  │  └─────────────────────────┘  │
│                     │                               │
│  ┌───────────────┐  │  ┌─────────────────────────┐  │
│  │  AndroidView  │──┼──│  View를 Compose에        │  │
│  │               │  │  │  삽입할 때 사용            │  │
│  └───────────────┘  │  └─────────────────────────┘  │
│                     │                               │
└─────────────────────┴───────────────────────────────┘
```

## 핵심 특징

### 1. 두 가지 상호운용 방향

| 방향 | API | 사용 사례 |
|------|-----|----------|
| View → Compose | `ComposeView` | 기존 앱에 Compose 화면 추가 |
| Compose → View | `AndroidView` | Compose에서 레거시 View 사용 |

### 2. ComposeView

기존 View 레이아웃 내에 Compose UI를 삽입합니다.

```kotlin
// Fragment에서 ComposeView 사용
class MyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MyComposeUI()
            }
        }
    }
}
```

### 3. AndroidView

Compose UI 내에서 전통적인 Android View를 사용합니다.

```kotlin
@Composable
fun LegacyViewInCompose() {
    var sliderValue by remember { mutableFloatStateOf(50f) }

    AndroidView(
        factory = { context ->
            SeekBar(context).apply {
                max = 100
            }
        },
        update = { seekBar ->
            seekBar.progress = sliderValue.toInt()
        },
        modifier = Modifier.fillMaxWidth()
    )
}
```

### 4. ViewCompositionStrategy

ComposeView의 Composition 해제 시점을 제어합니다.

| 전략 | 설명 | 사용 시나리오 |
|------|------|-------------|
| `DisposeOnDetachedFromWindowOrReleasedFromPool` | 기본값. 윈도우 분리 또는 풀 해제 시 | RecyclerView 아이템 |
| `DisposeOnViewTreeLifecycleDestroyed` | Lifecycle 파괴 시 해제 | Fragment |
| `DisposeOnLifecycleDestroyed` | 지정된 Lifecycle 파괴 시 | 커스텀 생명주기 |

## 문제 상황: 상태 동기화 실패

### 잘못된 접근

```kotlin
@Composable
fun WrongApproach() {
    var text by remember { mutableStateOf("") }

    // ❌ EditText의 텍스트 변경이 Compose State와 동기화되지 않음
    val editText = remember { EditText(context) }

    // View 생명주기 관리 문제, 양방향 바인딩 불가
}
```

### 발생하는 문제점

1. **View 생명주기 미관리**: View가 Compose 밖에서 생성되어 정리되지 않음
2. **단방향 통신만 가능**: View → Compose 또는 Compose → View 중 하나만 동작
3. **무한 루프 위험**: 상태 변경 시 서로를 계속 업데이트
4. **메모리 누수**: View가 Composition 외부에서 참조되어 해제 안 됨

## 해결책: AndroidView 사용

### 올바른 패턴

```kotlin
@Composable
fun CorrectApproach() {
    var text by remember { mutableStateOf("") }

    AndroidView(
        factory = { context ->
            EditText(context).apply {
                // View → Compose: 이벤트 리스너로 상태 업데이트
                addTextChangedListener { editable ->
                    text = editable?.toString() ?: ""
                }
            }
        },
        update = { editText ->
            // Compose → View: 상태 변경 시 View 업데이트
            // 무한 루프 방지: 값이 다를 때만 업데이트
            if (editText.text.toString() != text) {
                editText.setText(text)
            }
        }
    )
}
```

### 해결되는 이유

1. **factory에서 View 생성**: Compose가 View 생명주기를 관리
2. **양방향 통신**: 리스너(View→Compose) + update(Compose→View)
3. **조건부 업데이트**: 무한 루프 방지
4. **자동 정리**: Composition 종료 시 View도 정리됨

## 사용 시나리오

### 1. 점진적 마이그레이션

```kotlin
// 기존 Fragment에 새 Compose 화면 추가
class LegacyFragment : Fragment() {
    override fun onCreateView(...): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent { NewComposeScreen() }
        }
    }
}
```

### 2. Compose에서 아직 지원 안 되는 View

```kotlin
// MapView, AdView, WebView 등
@Composable
fun MapScreen() {
    AndroidView(
        factory = { context -> MapView(context) },
        update = { mapView -> /* 지도 업데이트 */ }
    )
}
```

### 3. LazyColumn에서 View 재사용

```kotlin
LazyColumn {
    items(100) { index ->
        AndroidView(
            factory = { MyCustomView(it) },
            update = { it.setData(index) },
            onReset = { it.clear() }  // 재사용 활성화!
        )
    }
}
```

## 주의사항

### 1. ViewCompositionStrategy 필수 설정

```kotlin
// ❌ Fragment에서 기본 전략 사용 시 상태 손실 위험
composeView.setContent { ... }

// ✅ Fragment에 적합한 전략 명시
composeView.setViewCompositionStrategy(
    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
)
composeView.setContent { ... }
```

### 2. factory에서 View 생성

```kotlin
// ❌ remember로 View를 외부에서 보관
val myView = remember { MyView(context) }
AndroidView(factory = { myView })

// ✅ factory 람다 내에서 View 생성
AndroidView(
    factory = { context -> MyView(context) }
)
```

### 3. 무한 루프 방지

```kotlin
AndroidView(
    update = { view ->
        // ❌ 조건 없이 항상 업데이트
        view.setText(text)

        // ✅ 값이 다를 때만 업데이트
        if (view.text.toString() != text) {
            view.setText(text)
        }
    }
)
```

### 4. DisposableEffect로 정리

```kotlin
@Composable
fun WebViewWithCleanup(url: String) {
    val webView = remember { mutableStateOf<WebView?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            webView.value?.destroy()
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).also { webView.value = it }
        }
    )
}
```

## 연습 문제

1. **SeekBar 통합 (초급)**: AndroidView로 SeekBar를 Compose에 통합하고, 값 변경 시 Text로 표시
2. **CalendarView 통합 (중급)**: CalendarView와 Compose State 양방향 바인딩
3. **RatingBar 통합 (고급)**: RatingBar + 리뷰 텍스트 입력 폼을 Compose로 구현

## 다음 학습

- `custom_layout`: 커스텀 레이아웃
- `constraint_layout`: ConstraintLayout in Compose
