# 상태 관리 심화: StateFlow vs SharedFlow vs Channel

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `stability` | Compose의 Stability(안정성)와 Recomposition 최적화 원리 | [📚 학습하기](../../state/stability/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

Android Compose에서 **ViewModel과 UI 간의 데이터 통신**은 앱의 안정성과 사용자 경험에 직접적인 영향을 미칩니다. Kotlin Flow는 이를 위한 세 가지 핵심 도구를 제공합니다.

### 한눈에 보기

| 도구 | 스트림 타입 | 값 보유 | 소비 방식 | 주요 용도 |
|------|-------------|---------|-----------|-----------|
| **StateFlow** | Hot | 항상 최신 값 | 여러 구독자 동시 수신 | UI 상태 관리 |
| **SharedFlow** | Hot | replay 설정 | 여러 구독자 동시 수신 | 이벤트 브로드캐스트 |
| **Channel** | Hot | 버퍼 저장 | 단일 소비 (한 번만) | 일회성 이벤트 |

### 핵심 원칙

> **상태(State)는 StateFlow로, 일회성 이벤트(Event)는 Channel로 분리하세요.**

---

## 도구별 심층 분석

### 1. StateFlow - UI 상태의 단일 진실 공급원

StateFlow는 **항상 값을 가지는 Hot Stream**입니다. 새 구독자는 즉시 현재 값을 받습니다.

```kotlin
class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProducts() {
        _uiState.update { it.copy(isLoading = true) }
        // 데이터 로딩...
    }
}
```

**핵심 특징:**

| 특징 | 설명 |
|------|------|
| Hot Stream | 구독자 없어도 값 유지 |
| 최신 값 보장 | 새 구독자는 즉시 현재 값 수신 |
| distinctUntilChanged | 같은 값은 재방출하지 않음 (성능 최적화) |
| Configuration Change 생존 | 화면 회전에도 상태 유지 |

**Composable에서 수집:**

```kotlin
// Android 권장 방식 (2025)
val uiState by viewModel.uiState.collectAsStateWithLifecycle()

// 필요한 의존성
// implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
```

**collectAsState vs collectAsStateWithLifecycle:**

| 함수 | 특징 | 사용 환경 |
|------|------|-----------|
| `collectAsState()` | Composition 수명주기만 따름, 백그라운드에서도 수집 | Compose Multiplatform |
| `collectAsStateWithLifecycle()` | Android Lifecycle 인식, STARTED 이상에서만 수집 | Android 앱 (권장) |

---

### 2. SharedFlow - 이벤트 스트림

SharedFlow는 **여러 구독자에게 동시에 이벤트를 전달**하는 Hot Stream입니다. 기본적으로 값을 보유하지 않습니다.

```kotlin
class EventViewModel : ViewModel() {
    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,                          // 새 구독자에게 이전 값 전달 안 함
        extraBufferCapacity = 1,             // 버퍼 크기
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun broadcast(event: UiEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
```

**설정 옵션:**

| 옵션 | 설명 | 기본값 |
|------|------|--------|
| `replay` | 새 구독자에게 전달할 최근 이벤트 수 | 0 |
| `extraBufferCapacity` | 추가 버퍼 크기 | 0 |
| `onBufferOverflow` | 버퍼 초과 시 정책 | SUSPEND |

**버퍼 초과 정책:**
- `SUSPEND`: 버퍼가 빌 때까지 대기
- `DROP_OLDEST`: 가장 오래된 이벤트 삭제
- `DROP_LATEST`: 최신 이벤트 삭제

**주의:** SharedFlow는 구독자가 없을 때 이벤트가 손실될 수 있습니다. 일회성 이벤트에는 Channel이 더 적합합니다.

---

### 3. Channel - 일회성 이벤트

Channel은 **각 이벤트가 단 한 번만 소비되도록 보장**합니다. Snackbar, Navigation 등 일회성 이벤트에 최적입니다.

```kotlin
class SnackbarViewModel : ViewModel() {
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun showMessage(message: String) {
        viewModelScope.launch {
            _events.send(UiEvent.ShowSnackbar(message))
        }
    }
}
```

**핵심 특징:**

| 특징 | 설명 |
|------|------|
| 일회성 소비 | 각 이벤트는 한 번만 처리됨 |
| 버퍼 저장 | 구독자 없어도 버퍼에 보관되어 나중에 전달 |
| 단일 소비자 | 여러 구독자 중 한 명만 이벤트 수신 |

**Composable에서 수집:**

```kotlin
@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Channel은 LaunchedEffect에서 수집
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.NavigateTo -> {
                    // Navigation 처리
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        // UI 내용
    }
}
```

---

## 상황별 선택 가이드

### 의사결정 플로우차트

```
시작
  │
  ├── 현재 상태가 중요한가? ──Yes──► StateFlow
  │         (UI 상태, 설정 등)
  │
  ├── 여러 구독자에게 동시 전달? ──Yes──► SharedFlow
  │         (브로드캐스트)
  │
  ├── 이벤트가 한 번만 처리? ──Yes──► Channel
  │         (Snackbar, Navigation)
  │
  └── 구독자 없을 때 손실 방지? ──Yes──► Channel
            (버퍼 보장)
```

### 시나리오별 권장 도구

| 시나리오 | 권장 도구 | 이유 |
|---------|-----------|------|
| 화면 UI 상태 | StateFlow | 최신 값 보유, 구독자에게 즉시 전달 |
| 로딩/에러 상태 | StateFlow | 지속적인 UI 표시 필요 |
| Snackbar 표시 | Channel | 일회성, 중복 표시 방지 |
| Navigation | Channel | 일회성, 한 번만 실행되어야 함 |
| 여러 화면에 알림 | SharedFlow(replay=1) | 모든 구독자에게 전달 |
| 실시간 위치 업데이트 | StateFlow + WhileSubscribed | 백그라운드에서 자동 중단 |
| 다이얼로그 표시 | StateFlow | 상태로 관리하는 것이 권장됨 |

---

## 흔한 실수와 해결책

### 실수 1: StateFlow로 일회성 이벤트 처리

**잘못된 코드:**

```kotlin
// 모든 것을 StateFlow 하나로 처리하려는 잘못된 시도
data class UiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,  // 문제!
    val navigateTo: String? = null         // 문제!
)

class BadViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun addToCart() {
        _uiState.update {
            it.copy(snackbarMessage = "장바구니에 추가됨")
        }
    }

    // 수동으로 초기화해야 함 (잊기 쉬움!)
    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
```

**발생하는 문제:**

1. **화면 회전 시 중복 표시**: StateFlow는 최신 값을 보유하므로, 재구독 시 Snackbar가 다시 표시됨
2. **수동 초기화 필요**: `clearSnackbar()` 호출을 잊으면 이벤트가 계속 남아 있음
3. **Race Condition**: 연속 클릭 시 이벤트가 덮어씌워질 수 있음

**해결책: Channel로 분리**

```kotlin
// UI 상태 (지속적인 데이터만)
data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false
)

// 일회성 이벤트
sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class NavigateTo(val route: String) : UiEvent()
}

class GoodViewModel : ViewModel() {
    // 상태: StateFlow
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    // 이벤트: Channel
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun addToCart(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // API 호출...
            _uiState.update { it.copy(isLoading = false) }
            _events.send(UiEvent.ShowSnackbar("${product.name} 추가됨"))
        }
    }
}
```

---

### 실수 2: collectAsState 사용 (백그라운드 리소스 낭비)

**잘못된 코드:**

```kotlin
// 백그라운드에서도 Flow 수집 계속
val state by viewModel.uiState.collectAsState()
```

**문제점:**
- 앱이 백그라운드에 있어도 Flow 수집 계속
- 위치 업데이트, Firebase 구독 등에서 배터리 소모
- 메모리 누수 가능성

**해결책:**

```kotlin
// Android Lifecycle 인식 - STARTED 이상에서만 수집
val state by viewModel.uiState.collectAsStateWithLifecycle()
```

---

### 실수 3: SharingStarted.Eagerly 사용

**잘못된 코드:**

```kotlin
val uiState = repository.getDataFlow()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,  // 항상 활성화 - 리소스 낭비!
        initialValue = UiState()
    )
```

**해결책:**

```kotlin
val uiState = repository.getDataFlow()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),  // 권장
        initialValue = UiState()
    )
```

---

## 고급 패턴

### WhileSubscribed(5000) 이해하기

```kotlin
val uiState: StateFlow<UiState> = repository.getDataFlow()
    .map { data -> UiState(data = data) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5_000,  // 마지막 구독자 사라진 후 대기 시간
            replayExpirationMillis = 0  // replay 캐시 만료 시간
        ),
        initialValue = UiState()
    )
```

**SharingStarted 정책 비교:**

| 정책 | 시작 시점 | 종료 시점 | 사용 상황 |
|------|-----------|-----------|-----------|
| `Eagerly` | 즉시 | 없음 (영원히) | 앱 전역 상태 |
| `Lazily` | 첫 구독자 등장 | 없음 (영원히) | 지연 초기화 필요 시 |
| `WhileSubscribed(5000)` | 첫 구독자 등장 | 마지막 구독자 + 5초 | 대부분의 경우 (권장) |

**왜 5000ms인가?**
- 화면 회전(Configuration Change) 시 Activity 재생성에 약 1-2초 소요
- 5초 대기로 재구독할 충분한 시간 확보
- 5초 후에도 재구독 없으면 업스트림 중단하여 리소스 절약

---

### produceState vs StateFlow

| 상황 | 권장 | 이유 |
|------|------|------|
| 단순 일회성 데이터 로드 | `produceState` | Composition 수명주기에 바인딩 |
| 화면 수준 상태 관리 | StateFlow in ViewModel | Configuration Change 생존 |
| 여러 화면에서 공유 | StateFlow in ViewModel | 중앙 집중식 관리 |
| 실시간 업데이트 구독 | StateFlow with `stateIn` | 효율적인 스트림 관리 |

**produceState 예시:**

```kotlin
@Composable
fun DetailScreen(itemId: String) {
    val item by produceState<Item?>(initialValue = null, key1 = itemId) {
        value = repository.getItem(itemId)  // suspend 함수
    }

    item?.let { ItemDetails(it) }
}
```

---

### MVI 패턴 적용

MVI(Model-View-Intent) 패턴은 StateFlow와 Channel을 효과적으로 활용합니다.

```
┌──────────────────────────────────────────────┐
│                    View                       │
│  ┌─────────────────────────────────────────┐ │
│  │  collectAsStateWithLifecycle(uiState)   │ │
│  │  LaunchedEffect { events.collect {...}} │ │
│  └─────────────────────────────────────────┘ │
│                     │ Intent                  │
│                     ▼                         │
│             ┌─────────────┐                   │
│             │  ViewModel  │                   │
│             └─────────────┘                   │
│                     │                         │
│         ┌──────────┴──────────┐              │
│         ▼                     ▼              │
│   StateFlow<State>      Channel<Event>       │
│   (UI 상태)             (일회성 이벤트)        │
└──────────────────────────────────────────────┘
```

**완전한 MVI 예시:**

```kotlin
// State (불변)
data class CounterState(
    val count: Int = 0,
    val isLoading: Boolean = false
)

// Intent (사용자 의도)
sealed class CounterIntent {
    data object Increment : CounterIntent()
    data object Decrement : CounterIntent()
    data object Reset : CounterIntent()
}

// Event (일회성)
sealed class CounterEvent {
    data class ShowMessage(val text: String) : CounterEvent()
}

class CounterViewModel : ViewModel() {
    private val _state = MutableStateFlow(CounterState())
    val state: StateFlow<CounterState> = _state.asStateFlow()

    private val _events = Channel<CounterEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onIntent(intent: CounterIntent) {
        when (intent) {
            is CounterIntent.Increment -> {
                _state.update { it.copy(count = it.count + 1) }
            }
            is CounterIntent.Decrement -> {
                _state.update { it.copy(count = it.count - 1) }
            }
            is CounterIntent.Reset -> {
                _state.update { it.copy(count = 0) }
                viewModelScope.launch {
                    _events.send(CounterEvent.ShowMessage("카운터가 초기화되었습니다"))
                }
            }
        }
    }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CounterEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.text)
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Count: ${state.count}")
            Button(onClick = { viewModel.onIntent(CounterIntent.Increment) }) {
                Text("+1")
            }
            Button(onClick = { viewModel.onIntent(CounterIntent.Reset) }) {
                Text("Reset")
            }
        }
    }
}
```

---

## 2025년 최신 권장사항

Google의 공식 Android 아키텍처 문서에 따르면:

1. **상태 기반 접근법 권장**: 이벤트도 가능하면 UI 상태로 모델링
2. **collectAsStateWithLifecycle 필수**: Android 앱에서 Flow 수집 시 사용
3. **WhileSubscribed(5000) 기본 사용**: 리소스 효율적인 상태 공유
4. **Channel은 진정한 일회성 이벤트에만**: Snackbar, Navigation 등

**의존성 (2025년 기준):**

```kotlin
// build.gradle.kts
dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
}
```

---

## 연습 문제

### 연습 1: StateFlow와 Channel 분리 (기초)

**목표:** 잘못된 코드를 수정하여 상태와 이벤트를 분리

**현재 문제:** `successMessage`가 StateFlow 상태에 포함되어 화면 회전 시 Snackbar가 다시 표시됨

**해결 과정:**
1. `sealed class TodoEvent` 정의
2. `Channel<TodoEvent>(Channel.BUFFERED)` 생성
3. `receiveAsFlow()`로 Flow 변환
4. `LaunchedEffect(Unit) { events.collect { } }`로 수집

---

### 연습 2: collectAsStateWithLifecycle 적용 (중급)

**목표:** 백그라운드에서 리소스를 절약하도록 코드 수정

**현재 문제:**
- `SharingStarted.Eagerly` 사용으로 항상 활성화
- `collectAsState()` 사용으로 백그라운드에서도 수집

**해결 과정:**
1. `SharingStarted.Eagerly` → `WhileSubscribed(5_000)` 변경
2. `collectAsState()` → `collectAsStateWithLifecycle()` 변경
3. 필요한 import 추가

---

### 연습 3: MVI 패턴 구현 (심화)

**목표:** 완전한 MVI 패턴으로 카운터 앱 구현

**구현 요소:**
1. `CounterState` data class (count, isLoading)
2. `CounterIntent` sealed class (Increment, Decrement, Reset)
3. `CounterEvent` sealed class (ShowMessage)
4. ViewModel에서 `onIntent()` 함수 구현
5. Composable에서 State 수집 및 Event 처리

---

## 정리

| 항목 | 권장 사항 |
|------|-----------|
| UI 상태 | StateFlow + collectAsStateWithLifecycle |
| 일회성 이벤트 | Channel + LaunchedEffect |
| 상태 공유 정책 | WhileSubscribed(5000) |
| 다이얼로그/바텀시트 | StateFlow (상태로 관리) |

---

## 다음 학습

- [hilt_viewmodel](../../architecture/hilt_viewmodel/README.md): Hilt와 ViewModel 통합
- [navigation](../../navigation/navigation_basics/README.md): Type-Safe Navigation과 이벤트 처리
- [effect_handlers_advanced](../../effect/effect_handlers_advanced/README.md): 고급 Side Effect 핸들러
