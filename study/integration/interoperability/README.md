# View-Compose 상호운용성 (Interoperability) 완벽 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `lifecycle_integration` | Compose와 Android Lifecycle 통합 | [📚 학습하기](../../integration/lifecycle_integration/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**상호운용성(Interoperability)**은 기존 View 시스템과 Jetpack Compose 간의 **양방향 통합**을 가능하게 하는 API들입니다.

```
┌─────────────────────────────────────────────────────────────────┐
│                          Android 앱                              │
├───────────────────────────┬─────────────────────────────────────┤
│     기존 View 시스템       │         Jetpack Compose             │
│     (XML, Fragment)       │         (@Composable)               │
├───────────────────────────┼─────────────────────────────────────┤
│                           │                                     │
│  ┌─────────────────────┐  │  ┌───────────────────────────────┐  │
│  │    ComposeView      │──┼──│  Compose UI를 View에 삽입     │  │
│  │  (View → Compose)   │  │  │  Fragment, Activity에서 사용  │  │
│  └─────────────────────┘  │  └───────────────────────────────┘  │
│                           │                                     │
│  ┌─────────────────────┐  │  ┌───────────────────────────────┐  │
│  │    AndroidView      │──┼──│  View를 Compose에 삽입        │  │
│  │  (Compose → View)   │  │  │  MapView, WebView, AdView 등  │  │
│  └─────────────────────┘  │  └───────────────────────────────┘  │
│                           │                                     │
└───────────────────────────┴─────────────────────────────────────┘
```

### 왜 상호운용성이 필요한가?

1. **점진적 마이그레이션**: 기존 View 앱을 한 번에 Compose로 전환하기 어려움
2. **레거시 View 활용**: MapView, WebView, AdView 등 아직 Compose로 제공되지 않는 View 사용
3. **기존 커스텀 View 재사용**: 이미 만들어진 복잡한 커스텀 View 활용
4. **팀 내 점진적 도입**: 일부 화면부터 Compose 도입 가능

---

## 핵심 API 비교표

| 기준 | AndroidView | ComposeView |
|------|-------------|-------------|
| **방향** | Compose → View | View → Compose |
| **용도** | Compose 내에서 View 사용 | View 내에서 Compose 사용 |
| **주요 파라미터** | factory, update, onReset | setContent, setViewCompositionStrategy |
| **사용 사례** | MapView, WebView, 커스텀 View | Fragment에 Compose 추가, 점진적 마이그레이션 |
| **생명주기** | Compose가 View 관리 | ViewCompositionStrategy로 제어 |

---

## 의사결정 플로우차트

```
시작: 어떤 상황인가?
        │
        ├── Compose 앱에서 레거시 View를 사용하고 싶다
        │         │
        │         └──► AndroidView 사용
        │                   │
        │                   ├── Lazy 리스트 내부? ──Yes──► onReset 파라미터 추가
        │                   │
        │                   └── 양방향 동기화 필요? ──Yes──► factory(리스너) + update(조건부)
        │
        └── View 앱에서 Compose를 추가하고 싶다
                  │
                  └──► ComposeView 사용
                            │
                            └── 어디서 사용하나?
                                  │
                                  ├── Fragment ──► DisposeOnViewTreeLifecycleDestroyed
                                  │
                                  ├── RecyclerView ──► DisposeOnDetachedFromWindowOrReleasedFromPool
                                  │
                                  └── 커스텀 Lifecycle ──► DisposeOnLifecycleDestroyed
```

---

## Part 1: Compose에서 View 사용하기 (AndroidView)

### 문제 상황: View를 Compose에서 직접 사용할 수 없음

#### 시나리오
Compose 앱에서 EditText, MapView, WebView 같은 기존 View를 사용하고 싶습니다.
View를 직접 생성해서 사용하려고 하면 여러 문제가 발생합니다.

#### 잘못된 코드 예시

```kotlin
@Composable
fun WrongApproach() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    // ❌ View를 remember로 외부에서 생성
    val editText = remember {
        EditText(context).apply {
            hint = "입력하세요"
        }
    }

    // View가 화면에 표시되지 않음!
    // View ↔ Compose 상태 동기화 불가!
}
```

#### 발생하는 문제점

1. **View가 화면에 표시되지 않음**: AndroidView 없이는 View가 렌더링되지 않음
2. **View 생명주기 미관리**: Compose가 View의 생성/해제를 관리하지 않음
3. **단방향 통신만 가능**: View → Compose 또는 Compose → View 중 하나만 동작
4. **무한 루프 위험**: 양방향 동기화 시 서로를 계속 업데이트
5. **메모리 누수**: View가 Composition 해제 후에도 메모리에 남음

---

### 해결책: AndroidView 사용

#### AndroidView 핵심 파라미터

| 파라미터 | 역할 | 호출 시점 |
|----------|------|----------|
| `factory` | View 생성, 이벤트 리스너 설정 | 최초 1회 |
| `update` | Compose 상태 → View 반영 | recomposition마다 |
| `modifier` | Compose Modifier 적용 | - |
| `onReset` | View 재사용 시 상태 초기화 (Lazy용) | View 재사용 시 |
| `onRelease` | View가 Composition에서 제거될 때 | 제거 시 |

#### 올바른 코드 예시

```kotlin
@Composable
fun CorrectApproach() {
    var text by remember { mutableStateOf("") }

    AndroidView(
        factory = { context ->
            // ✅ factory 내부에서 View 생성
            EditText(context).apply {
                hint = "여기에 입력하세요"

                // View → Compose: 이벤트 리스너로 상태 업데이트
                addTextChangedListener { editable ->
                    val newText = editable?.toString() ?: ""
                    if (newText != text) {  // 무한 루프 방지
                        text = newText
                    }
                }
            }
        },
        update = { editText ->
            // Compose → View: 상태 변경 시 View 업데이트
            // ✅ 무한 루프 방지: 값이 다를 때만 업데이트
            if (editText.text.toString() != text) {
                editText.setText(text)
                editText.setSelection(text.length)  // 커서 위치 유지
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
```

#### 해결되는 이유

1. **factory에서 View 생성**: Compose가 View 생명주기를 자동 관리
2. **양방향 통신 구현**: 리스너(View→Compose) + update(Compose→View)
3. **조건부 업데이트**: 값 비교로 무한 루프 방지
4. **자동 정리**: Composition 종료 시 View도 정리됨

---

### 양방향 데이터 바인딩 패턴

```
┌─────────────────────────────────────────────────────────┐
│                 양방향 데이터 바인딩                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│    Compose State              Android View              │
│    ┌─────────┐                ┌─────────┐              │
│    │  text   │ ──update()───► │ EditText│              │
│    │         │ ◄──listener─── │         │              │
│    └─────────┘                └─────────┘              │
│                                                         │
│    핵심: 조건부 업데이트로 무한 루프 방지!               │
│                                                         │
│    if (editText.text.toString() != text) {             │
│        editText.setText(text)                          │
│    }                                                    │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

### Lazy 리스트에서의 View 최적화 (onReset)

Lazy 리스트(LazyColumn, LazyRow, Pager 등)에서 View를 효율적으로 재사용하려면 `onReset` 파라미터를 **반드시 제공**해야 합니다.

> **중요**: `onReset`이 null이면 View 재사용이 비활성화되고, 스크롤할 때마다 새 View가 생성됩니다.

```kotlin
LazyColumn {
    items(100) { index ->
        AndroidView(
            factory = { context ->
                MyCustomView(context).apply {
                    // 초기 설정
                }
            },
            update = { view ->
                // 데이터 업데이트
                view.setData(items[index])
            },
            onReset = { view ->
                // ✅ View 재사용 전 상태 초기화
                view.clear()
                view.resetToDefault()
            },
            onRelease = { view ->
                // View가 완전히 제거될 때 정리
                view.cleanup()
            }
        )
    }
}
```

| 파라미터 | 호출 시점 | 용도 | 필수 여부 |
|----------|----------|------|----------|
| `onReset` | View 재사용 직전 | 이전 데이터 초기화 | **필수** (null이면 재사용 비활성화) |
| `onRelease` | View가 풀에서 제거될 때 | 리소스 해제 | 선택 |

---

## Part 2: View에서 Compose 사용하기 (ComposeView)

### 문제 상황: Fragment에 Compose 추가 시 상태 손실

#### 시나리오
기존 Fragment 기반 앱에 새로운 화면을 Compose로 만들고 싶습니다.
ComposeView를 사용했지만, 화면 전환 시 Compose 상태가 사라집니다.

#### 잘못된 코드 예시

```kotlin
class MyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ❌ ViewCompositionStrategy 미설정
        return ComposeView(requireContext()).apply {
            setContent {
                var count by remember { mutableIntStateOf(0) }

                Button(onClick = { count++ }) {
                    Text("Count: $count")
                }
            }
        }
    }
}
```

#### 발생하는 문제점

1. **상태 손실**: Fragment가 백스택에 들어갔다 나오면 count가 0으로 초기화
2. **Composition 해제 시점 문제**: 기본 전략이 Fragment 생명주기와 맞지 않음
3. **불필요한 Composition 재생성**: 화면 전환마다 새로운 Composition 생성

---

### 해결책: ComposeView + ViewCompositionStrategy

#### 올바른 코드 예시

```kotlin
class MyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // ✅ Fragment에 적합한 전략 설정
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    MyComposeScreen()
                }
            }
        }
    }
}

@Composable
fun MyComposeScreen() {
    // rememberSaveable로 프로세스 종료에도 대비
    var count by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Count: $count", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}
```

---

### ViewCompositionStrategy 선택 가이드

| 전략 | 설명 | 사용 시나리오 |
|------|------|--------------|
| `DisposeOnDetachedFromWindowOrReleasedFromPool` | **기본값**. 윈도우 분리 또는 풀 해제 시 | RecyclerView 아이템, 일반 View |
| `DisposeOnViewTreeLifecycleDestroyed` | ViewTree의 Lifecycle 파괴 시 해제 | **Fragment (권장)** |
| `DisposeOnLifecycleDestroyed` | 지정된 Lifecycle 파괴 시 해제 | 커스텀 생명주기 관리 |
| ~~`DisposeOnDetachedFromWindow`~~ | **Deprecated**. `DisposeOnDetachedFromWindowOrReleasedFromPool`로 대체됨 | 사용하지 마세요 |

#### 전략별 상세 설명

**1. DisposeOnDetachedFromWindowOrReleasedFromPool (기본값)**
```kotlin
// RecyclerView에서 ComposeView 사용 시
class MyViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView) {
    init {
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool
        )
    }

    fun bind(data: MyData) {
        composeView.setContent {
            MyItemComposable(data)
        }
    }
}
```

**2. DisposeOnViewTreeLifecycleDestroyed (Fragment 권장)**
```kotlin
// Fragment에서 사용
class MyFragment : Fragment() {
    override fun onCreateView(...): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent { /* ... */ }
        }
    }
}
```

**3. DisposeOnLifecycleDestroyed (커스텀)**
```kotlin
// 특정 Lifecycle에 연결
class MyCustomView(context: Context) : FrameLayout(context) {
    private val composeView = ComposeView(context).apply {
        setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                findViewTreeLifecycleOwner()!!.lifecycle
            )
        )
    }
}
```

---

### XML 레이아웃에서 ComposeView 사용

```xml
<!-- fragment_my.xml -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="기존 View" />

    <!-- Compose UI 삽입 위치 -->
    <androidx.compose.ui.platform.ComposeView
        android:id="@+id/compose_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <Button
        android:id="@+id/legacy_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="기존 버튼" />

</LinearLayout>
```

```kotlin
class MyFragment : Fragment(R.layout.fragment_my) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.compose_view).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    // Compose UI
                    Card(modifier = Modifier.padding(16.dp)) {
                        Text("이것은 Compose UI입니다!")
                    }
                }
            }
        }
    }
}
```

---

## AndroidViewBinding (XML 레이아웃 통합)

복잡한 XML 레이아웃을 Compose에서 사용할 때는 `AndroidViewBinding`을 활용합니다.

```kotlin
// build.gradle.kts에 추가
// implementation("androidx.compose.ui:ui-viewbinding")

@Composable
fun MyXmlLayoutInCompose() {
    AndroidViewBinding(MyLayoutBinding::inflate) { binding ->
        // ViewBinding을 통해 View에 접근
        binding.textView.text = "Updated from Compose"
        binding.button.setOnClickListener {
            // 클릭 이벤트 처리
        }
    }
}
```

---

## 사용 시나리오 요약

### 1. 점진적 마이그레이션 (View → Compose)

```kotlin
// 기존 Fragment에 새 Compose 화면 추가
class LegacyFragment : Fragment() {
    override fun onCreateView(...): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                NewComposeScreen()
            }
        }
    }
}
```

### 2. Compose에서 아직 지원 안 되는 View

```kotlin
// MapView, AdView, WebView 등
@Composable
fun MapScreen(location: LatLng) {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                onCreate(null)
                getMapAsync { googleMap ->
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                }
            }
        },
        update = { mapView ->
            // 위치 업데이트
        },
        modifier = Modifier.fillMaxSize()
    )

    // MapView 생명주기 관리
    DisposableEffect(Unit) {
        onDispose {
            // mapView.onDestroy() 호출
        }
    }
}
```

### 3. RecyclerView에서 Compose 아이템

```kotlin
class ComposeViewHolder(
    private val composeView: ComposeView
) : RecyclerView.ViewHolder(composeView) {

    init {
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool
        )
    }

    fun bind(item: MyItem) {
        composeView.setContent {
            MyItemComposable(item = item)
        }
    }
}
```

---

## 주의사항

### 1. ViewCompositionStrategy 필수 설정 (Fragment)

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

### 4. fromUser 파라미터 활용

```kotlin
// SeekBar 등에서 사용자 입력과 프로그래밍 변경 구분
seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
        // ✅ fromUser가 true일 때만 상태 업데이트 (무한 루프 방지)
        if (fromUser) {
            progress = value
        }
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
})
```

### 5. DisposableEffect로 정리

```kotlin
@Composable
fun WebViewWithCleanup(url: String) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // ✅ 생명주기 관리
    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).also {
                webViewRef = it
                it.loadUrl(url)
            }
        }
    )
}
```

---

## 연습 문제

### 연습 1: SeekBar 볼륨 조절기 (초급)
- AndroidView로 SeekBar 통합
- 볼륨 값(0~100)을 Compose State로 관리
- 현재 볼륨을 Text로 표시
- "음소거" 버튼으로 0으로 설정

### 연습 2: CalendarView 날짜 선택기 (중급)
- CalendarView와 Compose State 양방향 바인딩
- 선택된 날짜를 포맷팅하여 표시
- "오늘" 버튼으로 현재 날짜로 이동

### 연습 3: RatingBar 리뷰 작성기 (고급)
- RatingBar + 리뷰 텍스트 입력 폼 구현
- 별점에 따라 다른 메시지 표시
- "제출" 버튼으로 Snackbar 표시

---

## 다음 학습

- `custom_layout`: 커스텀 레이아웃
- `constraint_layout`: ConstraintLayout in Compose
- `navigation_compose`: Navigation Compose로 화면 전환
