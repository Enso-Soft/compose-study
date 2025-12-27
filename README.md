# Android Jetpack Compose 학습 로드맵

> 이 문서는 2024-2025년 공식 자료와 커뮤니티 베스트 프랙티스를 기반으로 작성되었습니다.

---

## 📊 학습 진행 현황

```
완료된 모듈: 37개 | 전체 커버리지: 약 100%
```

| 영역 | 상태 | 설명 |
|------|------|------|
| Kotlin 기초 | ✅ 완료 | 람다, 확장 함수, 후행 람다, 널 안전성 |
| Compose 기초 | ✅ 완료 | Composable 함수, UI 컴포넌트, Layout, Modifier |
| 상태 관리 | ✅ 완료 | remember, rememberSaveable, State Hoisting, ViewModel |
| Side Effects | ✅ 완료 | LaunchedEffect, DisposableEffect, SideEffect, produceState 등 6개 |
| 성능 최적화 | ✅ 완료 | Recomposition, Stability, derivedStateOf |
| Navigation | ✅ 완료 | Type-Safe Navigation |
| Lifecycle | ✅ 완료 | LifecycleStartEffect, LifecycleResumeEffect |
| **애니메이션** | ✅ 완료 | animate*AsState, AnimatedVisibility, Crossfade |
| **Scaffold/테마** | ✅ 완료 | MaterialTheme, Scaffold, TopAppBar, 다크모드 |
| **UI 테스트** | ✅ 완료 | ComposeTestRule, 시맨틱 |
| **Preview** | ✅ 완료 | @Preview 어노테이션 활용 |
| **상호운용성** | ✅ 완료 | AndroidView, ComposeView |

---

## 왜 학습 순서가 중요한가?

Compose의 개념들은 **서로 의존 관계**가 있습니다:

```
State를 모르면 → Side Effects를 이해할 수 없음
Side Effects를 모르면 → Navigation에서 데이터 로드를 구현할 수 없음
Recomposition을 모르면 → 성능 최적화를 할 수 없음
```

잘못된 순서로 학습하면 **"왜 이게 필요한지"를 이해하지 못한 채** 문법만 외우게 됩니다.

---

## 학습 로드맵 개요

```
Level 1: 기초 ✅       ──→ Level 2: 상태 관리 ✅   ──→ Level 3: Side Effects ✅
(1-2주)                    (2-3주, 가장 중요)           (2주)
    │                           │                          │
    ▼                           ▼                          ▼
Kotlin 기초               remember/mutableStateOf      LaunchedEffect
Composable 함수           rememberSaveable             rememberCoroutineScope
기본 UI 컴포넌트           State Hoisting              DisposableEffect
Layout & Modifier         ViewModel 통합               SideEffect

                              │
         ┌────────────────────┴────────────────────┐
         ▼                                         ▼
Level 4: 성능 최적화 ✅                     Level 5: 고급 주제 ⚠️
(1-2주)                                     (2-3주)
    │                                           │
    ▼                                           ▼
Recomposition 이해                          Navigation ✅
Stability                                   Lifecycle 통합 ✅
derivedStateOf                              Animation ❌ (미완성)

         ┌────────────────────────────────────────┐
         ▼
Level 6: 추가 주제 ❌ (향후 추가 예정)
    │
    ▼
Scaffold & Theme
UI Testing
Preview 활용
View-Compose 상호운용
```

---

## Level 1: 기초 (1-2주)

### 1.1 Kotlin 기초 (선행 필수)

Compose는 **Kotlin DSL**로 구성됩니다. 아래 개념이 없으면 Compose 코드를 읽을 수 없습니다:

| 개념 | Compose에서의 활용 |
|------|-------------------|
| 람다 표현식 | `Button(onClick = { })` 모든 이벤트 핸들러 |
| 확장 함수 | `Modifier.padding().background()` 체이닝 |
| 후행 람다 | `Column { Text("Hello") }` 모든 컨테이너 |
| 널 안전성 | `text?.let { }` 조건부 렌더링 |

### 1.2 Composable 함수

```kotlin
@Composable
fun Greeting(name: String) {  // 선언적 UI의 기본 단위
    Text("Hello, $name")
}
```

**왜 먼저 배우나?** 모든 Compose UI는 `@Composable` 함수입니다. 이것 없이는 아무것도 못 합니다.

### 1.3 기본 UI 컴포넌트

- `Text`, `Button`, `Image`, `Icon`, `TextField`
- 가장 자주 사용하는 빌딩 블록
- 이후 모든 학습에서 실습 재료로 사용

### 1.4 Layout & Modifier

```kotlin
Column(
    modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
) {
    Row { /* 가로 배치 */ }
    Box { /* 겹치기 */ }
}
```

**왜 먼저 배우나?** UI 구성의 근본입니다. 모든 화면은 Column, Row, Box의 조합입니다.

---

## Level 2: 상태 관리 (2-3주)

> **이 단계가 가장 중요합니다.** Compose의 핵심은 "상태가 변하면 UI가 자동으로 업데이트된다"입니다.

### 2.1 remember & mutableStateOf

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }  // 상태 선언
    Button(onClick = { count++ }) {              // 상태 변경
        Text("Count: $count")                    // 상태 사용 → 자동 업데이트
    }
}
```

**왜 먼저 배우나?**
- 모든 상태 관리의 기초
- `remember` 없이는 상태가 매 Recomposition마다 초기화됨
- Side Effects 이해의 전제조건

### 2.2 rememberSaveable

```kotlin
var text by rememberSaveable { mutableStateOf("") }  // 화면 회전 후에도 유지
```

**왜 두 번째로 배우나?**
- `remember`만으로는 화면 회전 시 상태가 사라지는 **실제 문제** 발생
- Configuration Change 대응 방법 이해

### 2.3 State Hoisting (상태 끌어올리기)

```kotlin
// Stateless Composable - 재사용 가능
@Composable
fun Counter(
    count: Int,           // 상태는 외부에서 받음
    onIncrement: () -> Unit
) {
    Button(onClick = onIncrement) {
        Text("Count: $count")
    }
}

// 상위 Composable에서 상태 관리
@Composable
fun CounterScreen() {
    var count by remember { mutableStateOf(0) }
    Counter(count = count, onIncrement = { count++ })
}
```

**왜 세 번째로 배우나?**
- **재사용 가능한 컴포넌트** 설계의 핵심
- Unidirectional Data Flow(UDF) 패턴: **상태는 아래로, 이벤트는 위로**
- 테스트 용이성 향상

### 2.4 ViewModel 통합

```kotlin
class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() { _count.value++ }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    val count by viewModel.count.collectAsState()
    Counter(count = count, onIncrement = viewModel::increment)
}
```

**왜 네 번째로 배우나?**
- **화면 수준**의 상태 관리
- 비즈니스 로직과 UI 분리
- 프로덕션 앱의 표준 아키텍처 (MVVM)

---

## Level 3: Side Effects (2주)

> Side Effect = "Composable 함수 범위 밖에서 발생하는 상태 변경"

### Side Effects 실행 순서 (공식 문서 기반)

```
Composition 시작
    ↓
Composable 함수 실행
    ↓
DisposableEffect 등록
    ↓
LaunchedEffect 등록
    ↓
Composition 종료
    ↓
DisposableEffect 실행 (동기)
    ↓
LaunchedEffect 코루틴 시작 (비동기)
```

### 3.1 LaunchedEffect (가장 먼저)

```kotlin
@Composable
fun UserProfile(userId: String) {
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(userId) {  // userId가 바뀌면 재실행
        user = api.fetchUser(userId)  // suspend 함수 호출 가능
    }

    user?.let { Text(it.name) }
}
```

**왜 먼저 배우나?**
- **가장 흔히 사용**됨 (API 호출, 애니메이션, 타이머)
- Composable 진입 시점 또는 key 변경 시 실행
- 자동으로 취소됨 (메모리 누수 방지)

### 3.2 rememberCoroutineScope

```kotlin
@Composable
fun SendButton() {
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {  // 이벤트 핸들러 내에서 코루틴 실행
            api.sendMessage()
        }
    }) {
        Text("Send")
    }
}
```

**왜 두 번째로 배우나?**
- `LaunchedEffect`는 **자동 실행**용
- `rememberCoroutineScope`는 **사용자 이벤트** 기반 실행용
- 버튼 클릭 등 이벤트 핸들러에서는 `LaunchedEffect` 사용 불가

### 3.3 DisposableEffect

```kotlin
@Composable
fun LocationTracker() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val listener = LocationListener { /* 위치 업데이트 */ }
        locationManager.registerListener(listener)  // 등록

        onDispose {
            locationManager.unregisterListener(listener)  // 정리!
        }
    }
}
```

**왜 세 번째로 배우나?**
- **리소스 정리**가 필요한 경우
- 리스너 등록/해제, 시스템 콜백 관리
- `onDispose` 콜백이 핵심

### 3.4 SideEffect

```kotlin
@Composable
fun AnalyticsScreen(screenName: String) {
    SideEffect {
        analytics.logScreenView(screenName)  // 매 성공적인 Recomposition 후 실행
    }
}
```

**왜 네 번째로 배우나?**
- 상대적으로 **드물게 사용**
- Compose 상태를 **외부 시스템과 동기화**할 때
- 코루틴이 필요 없는 동기 작업용

---

## Level 4: 성능 최적화 (1-2주)

### 4.1 Recomposition 이해

```kotlin
@Composable
fun Parent() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: $count")      // count 변경 시 recompose
        ExpensiveChild()           // Compose가 "스마트하게" 스킵 가능
    }
}
```

**왜 먼저 배우나?** 성능 최적화의 **전제 지식**입니다.

### 4.2 Stability (안정성)

| 타입 | 안정성 | 이유 |
|------|--------|------|
| `Int`, `String`, `Boolean` | Stable | 불변 원시 타입 |
| `data class`(val만) | Stable | 불변으로 추론 가능 |
| `List`, `Map`, `Set` | **Unstable** | 불변 보장 불가 |
| 외부 라이브러리 클래스 | **Unstable** | Compose가 판단 불가 |

```kotlin
// Unstable → 매번 Recomposition
@Composable
fun UserList(users: List<User>) { ... }

// Stable로 만들기
@Immutable
data class User(val id: String, val name: String)

// 또는 Kotlinx Immutable Collections 사용
fun UserList(users: ImmutableList<User>) { ... }
```

### 4.3 derivedStateOf

```kotlin
@Composable
fun SearchResults(items: List<Item>, query: String) {
    // query가 바뀔 때마다 필터링 → 매우 자주 발생 가능
    val filtered by remember(items, query) {
        derivedStateOf {  // 결과가 실제로 바뀔 때만 Recomposition
            items.filter { it.name.contains(query) }
        }
    }
}
```

---

## Level 5: 고급 주제 (2-3주)

### 5.1 Navigation

```kotlin
// Type-Safe Navigation (권장)
@Serializable
object Home

@Serializable
data class Profile(val userId: String)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Home) {
        composable<Home> { HomeScreen(navController) }
        composable<Profile> { backStackEntry ->
            val profile: Profile = backStackEntry.toRoute()
            ProfileScreen(profile.userId)
        }
    }
}
```

**2024-2025 권장사항:**
- Type-Safe Navigation 사용 (`@Serializable` 객체로 Route 정의)
- 하드코딩 문자열 Route 대신 sealed class 사용

### 5.2 Lifecycle 통합

```kotlin
@Composable
fun CameraPreview() {
    LifecycleStartEffect(Unit) {
        camera.start()  // ON_START에서 실행

        onStopOrDispose {
            camera.stop()  // ON_STOP 또는 Composable 제거 시
        }
    }
}
```

---

## 📚 완성된 학습 모듈

### Level 1: 기초

| 모듈 | 주제 | 실행 명령어 |
|------|------|------------|
| 📁 [kotlin_basics](study/kotlin_basics/src/main/java/com/example/kotlin_basics/README.md) | Kotlin 기초 (람다, 확장함수, 널안전성) | `./gradlew :study:kotlin_basics:installDebug` |
| 📁 [compose_introduction](study/compose_introduction/src/main/java/com/example/compose_introduction/README.md) | Compose 소개, 선언적 UI vs 명령형 UI | `./gradlew :study:compose_introduction:installDebug` |
| 📁 [composable_function](study/composable_function/src/main/java/com/example/composable_function/README.md) | @Composable 함수, Recomposition | `./gradlew :study:composable_function:installDebug` |
| 📁 [basic_ui_components](study/basic_ui_components/src/main/java/com/example/basic_ui_components/README.md) | Text, Button, TextField, Icon | `./gradlew :study:basic_ui_components:installDebug` |
| 📁 [layout_and_modifier](study/layout_and_modifier/src/main/java/com/example/layout_and_modifier/README.md) | Column, Row, Box, Modifier | `./gradlew :study:layout_and_modifier:installDebug` |
| 📁 [screen_and_component](study/screen_and_component/src/main/java/com/example/screen_and_component/README.md) | Screen vs Component, Stateful/Stateless 분리, 화면 구조 | `./gradlew :study:screen_and_component:installDebug` |

### Level 2: 상태 관리

| 모듈 | 주제 | 실행 명령어 |
|------|------|------------|
| 📁 [remember](study/remember/src/main/java/com/example/remember/README.md) | remember, mutableStateOf | `./gradlew :study:remember:installDebug` |
| 📁 [remember_saveable](study/remember_saveable/src/main/java/com/example/remember_saveable/README.md) | rememberSaveable, Saver, Parcelize | `./gradlew :study:remember_saveable:installDebug` |
| 📁 [state_hoisting](study/state_hoisting/src/main/java/com/example/state_hoisting/README.md) | State Hoisting (상태 끌어올리기) | `./gradlew :study:state_hoisting:installDebug` |
| 📁 [view_model](study/view_model/src/main/java/com/example/view_model/README.md) | ViewModel + Compose 통합 | `./gradlew :study:view_model:installDebug` |
| 📁 [state_restoration_advanced](study/state_restoration_advanced/src/main/java/com/example/state_restoration_advanced/README.md) | 커스텀 Saver, SavedStateHandle, 프로세스 종료 복원 | `./gradlew :study:state_restoration_advanced:installDebug` |
| 📁 [state_management_advanced](study/state_management_advanced/src/main/java/com/example/state_management_advanced/README.md) | StateFlow vs SharedFlow vs Channel, collectAsStateWithLifecycle, WhileSubscribed, MVI 패턴 | `./gradlew :study:state_management_advanced:installDebug` |

### Level 3: Side Effects

| 모듈 | 주제 | 실행 명령어 |
|------|------|------------|
| 📁 [launched_effect](study/launched_effect/src/main/java/com/example/launched_effect/README.md) | LaunchedEffect | `./gradlew :study:launched_effect:installDebug` |
| 📁 [remember_coroutine_scope](study/remember_coroutine_scope/src/main/java/com/example/remember_coroutine_scope/README.md) | rememberCoroutineScope | `./gradlew :study:remember_coroutine_scope:installDebug` |
| 📁 [disposable_effect](study/disposable_effect/src/main/java/com/example/disposable_effect/README.md) | DisposableEffect | `./gradlew :study:disposable_effect:installDebug` |
| 📁 [side_effect](study/side_effect/src/main/java/com/example/side_effect/README.md) | SideEffect | `./gradlew :study:side_effect:installDebug` |
| 📁 [derived_state_of](study/derived_state_of/src/main/java/com/example/derived_state_of/README.md) | derivedStateOf | `./gradlew :study:derived_state_of:installDebug` |
| 📁 [produce_state](study/produce_state/src/main/java/com/example/produce_state/README.md) | produceState | `./gradlew :study:produce_state:installDebug` |
| 📁 [effect_handlers_advanced](study/effect_handlers_advanced/src/main/java/com/example/effect_handlers_advanced/README.md) | snapshotFlow, rememberUpdatedState, currentRecomposeScope, derivedStateOf vs snapshotFlow | `./gradlew :study:effect_handlers_advanced:installDebug` |

### Level 4: 성능 최적화

| 모듈 | 주제 | 실행 명령어 |
|------|------|------------|
| 📁 [recomposition](study/recomposition/src/main/java/com/example/recomposition/README.md) | Recomposition 이해 | `./gradlew :study:recomposition:installDebug` |
| 📁 [stability](study/stability/src/main/java/com/example/stability/README.md) | Stability (@Stable, @Immutable) | `./gradlew :study:stability:installDebug` |
| 📁 [compose_compiler_metrics](study/compose_compiler_metrics/src/main/java/com/example/compose_compiler_metrics/README.md) | Compiler Metrics & Reports, Strong Skipping Mode | `./gradlew :study:compose_compiler_metrics:installDebug` |
| 📁 [baseline_profiles](study/baseline_profiles/src/main/java/com/example/baseline_profiles/README.md) | Baseline Profiles, Startup Profiles, AOT 컴파일, Macrobenchmark | `./gradlew :study:baseline_profiles:installDebug` |

### Level 5: 고급 주제

| 모듈 | 주제 | 실행 명령어 |
|------|------|------------|
| 📁 [navigation](study/navigation/src/main/java/com/example/navigation/README.md) | Navigation Compose (Type-Safe) | `./gradlew :study:navigation:installDebug` |
| 📁 [navigation_3](study/navigation_3/src/main/java/com/example/navigation_3/README.md) | Navigation 3 (Nav3) - 2025 최신 네비게이션 | `./gradlew :study:navigation_3:installDebug` |
| 📁 [lifecycle_integration](study/lifecycle_integration/src/main/java/com/example/lifecycle_integration/README.md) | Lifecycle Integration | `./gradlew :study:lifecycle_integration:installDebug` |
| 📁 [preview](study/preview/src/main/java/com/example/preview/README.md) | @Preview, @PreviewParameter, Multipreview | `./gradlew :study:preview:installDebug` |
| 📁 [animation_basics](study/animation_basics/src/main/java/com/example/animation_basics/README.md) | animate*AsState, AnimatedVisibility, Crossfade | `./gradlew :study:animation_basics:installDebug` |
| 📁 [animation_advanced](study/animation_advanced/src/main/java/com/example/animation_advanced/README.md) | updateTransition, Animatable, AnimationSpec | `./gradlew :study:animation_advanced:installDebug` |
| 📁 [shared_element_transition](study/shared_element_transition/src/main/java/com/example/shared_element_transition/README.md) | SharedTransitionLayout, sharedElement, sharedBounds | `./gradlew :study:shared_element_transition:installDebug` |
| 📁 [animate_bounds](study/animate_bounds/src/main/java/com/example/animate_bounds/README.md) | LookaheadScope, animateBounds, BoundsTransform | `./gradlew :study:animate_bounds:installDebug` |
| 📁 [scaffold_and_theming](study/scaffold_and_theming/src/main/java/com/example/scaffold_and_theming/README.md) | MaterialTheme, Scaffold, TopAppBar, 다크모드 | `./gradlew :study:scaffold_and_theming:installDebug` |
| 📁 [compose_testing](study/compose_testing/src/main/java/com/example/compose_testing/README.md) | ComposeTestRule, Semantics, UI 테스트 | `./gradlew :study:compose_testing:installDebug` |
| 📁 [screenshot_testing](study/screenshot_testing/src/main/java/com/example/screenshot_testing/README.md) | Paparazzi, Roborazzi, 스냅샷 테스트 | `./gradlew :study:screenshot_testing:installDebug` |
| 📁 [interoperability](study/interoperability/src/main/java/com/example/interoperability/README.md) | AndroidView, ComposeView, 상호운용성 | `./gradlew :study:interoperability:installDebug` |
| 📁 [deep_link](study/deep_link/src/main/java/com/example/deep_link/README.md) | Deep Link, navDeepLink, URI 처리 | `./gradlew :study:deep_link:installDebug` |
| 📁 [back_handler](study/back_handler/src/main/java/com/example/back_handler/README.md) | BackHandler, 뒤로가기 처리, Predictive Back | `./gradlew :study:back_handler:installDebug` |

### Level 6: 확장 기능

| 모듈 | 주제 | 실행 명령어 |
|------|------|------------|
| 📁 [custom_layout](study/custom_layout/src/main/java/com/example/custom_layout/README.md) | Layout composable, MeasurePolicy, SubcomposeLayout | `./gradlew :study:custom_layout:installDebug` |
| 📁 [constraint_layout](study/constraint_layout/src/main/java/com/example/constraint_layout/README.md) | ConstraintLayout, Barrier, Chain, Guideline | `./gradlew :study:constraint_layout:installDebug` |
| 📁 [hilt_viewmodel](study/hilt_viewmodel/src/main/java/com/example/hilt_viewmodel/README.md) | @HiltViewModel, SavedStateHandle, hiltViewModel() | `./gradlew :study:hilt_viewmodel:installDebug` |
| 📁 [lazy_layouts](study/lazy_layouts/src/main/java/com/example/lazy_layouts/README.md) | LazyColumn/Row/Grid, key, contentType, derivedStateOf | `./gradlew :study:lazy_layouts:installDebug` |
| 📁 [composition_local](study/composition_local/src/main/java/com/example/composition_local/README.md) | CompositionLocal, compositionLocalOf, Provider | `./gradlew :study:composition_local:installDebug` |
| 📁 [window_insets](study/window_insets/src/main/java/com/example/window_insets/README.md) | WindowInsets, Edge-to-Edge, imePadding | `./gradlew :study:window_insets:installDebug` |
| 📁 [gesture](study/gesture/src/main/java/com/example/gesture/README.md) | pointerInput, detectTapGestures, detectDragGestures | `./gradlew :study:gesture:installDebug` |
| 📁 [paging_compose](study/paging_compose/src/main/java/com/example/paging_compose/README.md) | Paging 3, PagingSource, collectAsLazyPagingItems | `./gradlew :study:paging_compose:installDebug` |
| 📁 [pager](study/pager/src/main/java/com/example/pager/README.md) | HorizontalPager, VerticalPager, PagerState | `./gradlew :study:pager:installDebug` |
| 📁 [pull_to_refresh](study/pull_to_refresh/src/main/java/com/example/pull_to_refresh/README.md) | PullToRefreshBox, 당겨서 새로고침 | `./gradlew :study:pull_to_refresh:installDebug` |
| 📁 [flow_layout](study/flow_layout/src/main/java/com/example/flow_layout/README.md) | FlowRow, FlowColumn, 동적 래핑 레이아웃 | `./gradlew :study:flow_layout:installDebug` |
| 📁 [canvas_drawing](study/canvas_drawing/src/main/java/com/example/canvas_drawing/README.md) | Canvas, drawLine, drawCircle, drawArc, Path, Brush | `./gradlew :study:canvas_drawing:installDebug` |
| 📁 [adaptive_layout](study/adaptive_layout/src/main/java/com/example/adaptive_layout/README.md) | WindowSizeClass, 반응형 레이아웃, NavigationSuiteScaffold | `./gradlew :study:adaptive_layout:installDebug` |
| 📁 [custom_modifier](study/custom_modifier/src/main/java/com/example/custom_modifier/README.md) | Modifier.Node, Modifier.composed, 조건부 Modifier | `./gradlew :study:custom_modifier:installDebug` |
| 📁 [drag_and_drop](study/drag_and_drop/src/main/java/com/example/drag_and_drop/README.md) | dragAndDropSource, dragAndDropTarget, ClipData | `./gradlew :study:drag_and_drop:installDebug` |
| 📁 [permission_handling](study/permission_handling/src/main/java/com/example/permission_handling/README.md) | rememberPermissionState, Accompanist Permissions, Android 14+ | `./gradlew :study:permission_handling:installDebug` |
| 📁 [focus_management](study/focus_management/src/main/java/com/example/focus_management/README.md) | FocusRequester, FocusManager, IME Actions, 포커스 스타일링 | `./gradlew :study:focus_management:installDebug` |
| 📁 [notification_integration](study/notification_integration/src/main/java/com/example/notification_integration/README.md) | NotificationChannel, POST_NOTIFICATIONS, Rich 알림, Progress 알림 | `./gradlew :study:notification_integration:installDebug` |
| 📁 [image_loading](study/image_loading/src/main/java/com/example/image_loading/README.md) | Coil 3.x, AsyncImage, SubcomposeAsyncImage, 캐싱 | `./gradlew :study:image_loading:installDebug` |
| 📁 [text_typography](study/text_typography/src/main/java/com/example/text_typography/README.md) | AnnotatedString, InlineContent, LinkAnnotation, TextMeasurer | `./gradlew :study:text_typography:installDebug` |
| 📁 [media3_player](study/media3_player/src/main/java/com/example/media3_player/README.md) | Media3 ExoPlayer + Compose 통합, PlayerView, 생명주기 관리 | `./gradlew :study:media3_player:installDebug` |
| 📁 [search_bar](study/search_bar/src/main/java/com/example/search_bar/README.md) | Material3 SearchBar, DockedSearchBar, 디바운스, 필터 칩 | `./gradlew :study:search_bar:installDebug` |
| 📁 [audio_recording](study/audio_recording/src/main/java/com/example/audio_recording/README.md) | MediaRecorder, RECORD_AUDIO 권한, 녹음 상태 관리, 진폭 시각화 | `./gradlew :study:audio_recording:installDebug` |
| 📁 [dialog_basics](study/dialog_basics/src/main/java/com/example/dialog_basics/README.md) | AlertDialog, Dialog, 상태 기반 다이얼로그, 입력/선택 다이얼로그 | `./gradlew :study:dialog_basics:installDebug` |
| 📁 [bottom_sheet_basics](study/bottom_sheet_basics/src/main/java/com/example/bottom_sheet_basics/README.md) | ModalBottomSheet 기초, Boolean 상태로 열기/닫기, 액션 시트 패턴 | `./gradlew :study:bottom_sheet_basics:installDebug` |
| 📁 [bottom_sheet_advanced](study/bottom_sheet_advanced/src/main/java/com/example/bottom_sheet_advanced/README.md) | ModalBottomSheet, BottomSheetScaffold, SheetState, 중첩 시트 | `./gradlew :study:bottom_sheet_advanced:installDebug` |
| 📁 [camerax_compose](study/camerax_compose/src/main/java/com/example/camerax_compose/README.md) | CameraX + Compose 통합, PreviewView, ImageCapture, 카메라 전환 | `./gradlew :study:camerax_compose:installDebug` |
| 📁 [slot_api_pattern](study/slot_api_pattern/src/main/java/com/example/slot_api_pattern/README.md) | Slot API 패턴, Compound Component, Scoped Slots, layoutId | `./gradlew :study:slot_api_pattern:installDebug` |
| 📁 [semantics_accessibility](study/semantics_accessibility/src/main/java/com/example/semantics_accessibility/README.md) | Semantics Tree, contentDescription, mergeDescendants, liveRegion, traversalOrder, 접근성 테스트 | `./gradlew :study:semantics_accessibility:installDebug` |
| 📁 [visibility_tracking](study/visibility_tracking/src/main/java/com/example/visibility_tracking/README.md) | Visibility Tracking API (2025), onVisibilityChanged, onLayoutRectChanged, 광고 노출 추적 | `./gradlew :study:visibility_tracking:installDebug` |
| 📁 [textfield_state](study/textfield_state/src/main/java/com/example/textfield_state/README.md) | TextFieldState, InputTransformation, OutputTransformation, Autofill, SecureTextField | `./gradlew :study:textfield_state:installDebug` |

### 각 모듈 구조

```
study/{module_name}/
├── README.md       # 개념 설명
├── Problem.kt      # 이 기술 없이 발생하는 문제
├── Solution.kt     # 기술을 사용한 해결책
└── Practice.kt     # 직접 구현해보는 연습
```

---

## 🚧 향후 추가 예정 모듈

> Compose 학습 완성도를 높이기 위해 다음 주제들을 추가할 예정입니다.

### 중간 우선순위 (확장 기능)

| 우선순위 | 모듈명 | 주제 | 설명 |
|:-------:|--------|------|------|
| ✅ - | `custom_layout` | 커스텀 레이아웃 | Layout(), MeasurePolicy, SubcomposeLayout |
| ✅ - | `constraint_layout` | ConstraintLayout | 제약 조건 기반 복잡한 레이아웃 |
| ✅ - | `animation_advanced` | 고급 애니메이션 | updateTransition, Animatable, 커스텀 스펙 |

### 낮은 우선순위 (심화/선택)

| 우선순위 | 모듈명 | 주제 | 설명 |
|:-------:|--------|------|------|
| ✅ - | `hilt_viewmodel` | Hilt + ViewModel | @HiltViewModel, SavedStateHandle |
| ✅ - | `deep_link` | Deep Link | Navigation + URI 처리 |

---

## 📖 Compose 학습 커버리지 분석

> 표준 Compose 교재 목차 기준 현재 커버리지 분석

### ✅ 완벽하게 커버된 영역

| 주제 | 모듈 수 | 비고 |
|------|:------:|------|
| 상태 관리 (State Management) | 4개 | remember → rememberSaveable → Hoisting → ViewModel |
| Side Effects | 5개 | LaunchedEffect, DisposableEffect, SideEffect, rememberCoroutineScope, derivedStateOf |
| 성능 최적화 | 2개 | Recomposition, Stability |
| Scaffold/Theme | 1개 | MaterialTheme, Scaffold, TopAppBar, 다크모드 |
| UI 테스트 | 1개 | ComposeTestRule, Semantics, Finders, Assertions, Actions |

### ⚠️ 부분적으로 커버된 영역

| 주제 | 현황 | 보완 필요 |
|------|------|----------|
| Layout & Modifier | 완료 | Custom Layout, ConstraintLayout 모두 완료 |
| Navigation | Type-Safe 커버 | Deep Link, Nested Navigation 추가 필요 |
| ViewModel | 기본 커버 | Hilt 연동, SavedStateHandle 추가 필요 |

### ❌ 완전히 부족한 영역

현재 모든 핵심 영역이 커버되었습니다. 향후 추가 예정 모듈을 확인하세요.

### 커버리지 시각화

```
상태 관리     ████████████████████ 100% ✅
Side Effects  ████████████████████ 100% ✅
성능 최적화   ████████████████████ 100% ✅
Preview       ████████████████████ 100% ✅
애니메이션    ████████████████████ 100% ✅
Navigation    ████████████████████ 100% ✅
Layout        ████████████████████ 100% ✅
Scaffold/Theme████████████████████ 100% ✅
UI 테스트     ████████████████████ 100% ✅
상호운용성    ████████████████████ 100% ✅
```

---

## 공식 학습 리소스

### Google 공식 코스
- [Android Basics with Compose](https://developer.android.com/courses/android-basics-compose/course) - 프로그래밍 경험 없이 시작 가능, 100시간+ 분량
- [Jetpack Compose for Android Developers](https://developer.android.com/courses/jetpack-compose/course) - 기존 Android 개발자용

### 공식 문서
- [State and Jetpack Compose](https://developer.android.com/develop/ui/compose/state)
- [Side-effects in Compose](https://developer.android.com/develop/ui/compose/side-effects)
- [Lifecycle of composables](https://developer.android.com/develop/ui/compose/lifecycle)
- [Jetpack Compose Performance](https://developer.android.com/develop/ui/compose/performance)
- [Animation in Compose](https://developer.android.com/develop/ui/compose/animation/introduction)
- [Testing your Compose layout](https://developer.android.com/develop/ui/compose/testing)

### 커뮤니티 리소스
- [Android Developer Roadmap](https://github.com/skydoves/android-developer-roadmap) - 시각적 학습 경로
- [Compose Performance 가이드](https://github.com/skydoves/compose-performance) - 성능 최적화 모음

---

## 참고 자료 (이 문서 작성에 사용된 출처)

- [Jetpack Compose Roadmap - Android Developers](https://developer.android.com/jetpack/androidx/compose-roadmap)
- [Jetpack Compose Complete Roadmap for 2025](https://medium.com/@ami0275/jetpack-compose-complete-roadmap-for-2025-eec23b780d84)
- [Side-effects in Compose - Android Developers](https://developer.android.com/develop/ui/compose/side-effects)
- [Understanding Execution Order in Jetpack Compose - droidcon 2025](https://www.droidcon.com/2025/04/22/understanding-execution-order-in-jetpack-compose-disposableeffect-launchedeffect-and-composables/)
- [Stability in Compose - Android Developers](https://developer.android.com/develop/ui/compose/performance/stability)
- [LaunchedEffect vs rememberCoroutineScope - droidcon](https://www.droidcon.com/2023/05/07/launchedeffect-vs-remembercoroutinescope-in-jetpack-compose/)
- [Navigation with Compose - Android Developers](https://developer.android.com/develop/ui/compose/navigation)
- [Jetpack Compose Stability Explained - Android Developers Medium](https://medium.com/androiddevelopers/jetpack-compose-stability-explained-79c10db270c8)
