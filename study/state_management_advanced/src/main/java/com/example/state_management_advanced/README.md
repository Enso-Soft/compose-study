# 상태 관리 심화 - StateFlow vs SharedFlow vs Channel

## 개념

Android Compose에서 **ViewModel과 UI 간의 데이터 통신**을 위해 Kotlin Flow의 세 가지 핵심 도구를 사용합니다:

| 도구 | 특징 | 주요 용도 |
|------|------|----------|
| **StateFlow** | Hot Stream, 항상 최신 값 보유 | UI 상태 관리 |
| **SharedFlow** | Hot Stream, 값 보유 X, replay 설정 가능 | 여러 구독자에게 이벤트 전달 |
| **Channel** | 일회성 소비 보장 | Snackbar, Navigation 등 일회성 이벤트 |

---

## 핵심 특징

### 1. StateFlow - UI 상태의 단일 진실 공급원

```kotlin
class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    fun loadProducts() {
        _uiState.update { it.copy(isLoading = true) }
        // ...
    }
}
```

**특징:**
- **Hot Stream**: 구독자 없어도 값을 유지
- **최신 값 보장**: 새 구독자는 즉시 현재 값을 받음
- **distinctUntilChanged**: 같은 값은 방출하지 않음 (중복 방지)
- **Configuration Change 생존**: 화면 회전에도 상태 유지

### 2. SharedFlow - 이벤트 스트림

```kotlin
class EventViewModel : ViewModel() {
    private val _events = MutableSharedFlow<UiEvent>(
        replay = 0,                          // 새 구독자에게 이전 값 전달 X
        extraBufferCapacity = 1,             // 버퍼 크기
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()
}
```

**특징:**
- **replay**: 새 구독자에게 최근 N개 값 전달
- **extraBufferCapacity**: 추가 버퍼 크기
- **onBufferOverflow**: 버퍼 초과 시 정책 (SUSPEND, DROP_OLDEST, DROP_LATEST)
- **여러 구독자**: 모든 구독자가 같은 이벤트 수신

### 3. Channel - 일회성 이벤트

```kotlin
class SnackbarViewModel : ViewModel() {
    private val _events = Channel<String>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun showMessage(message: String) {
        viewModelScope.launch {
            _events.send(message)
        }
    }
}
```

**특징:**
- **일회성 소비**: 각 이벤트는 단 한 번만 소비됨
- **소비 보장**: 구독자 없어도 버퍼에 저장되어 나중에 전달
- **단일 소비자**: 여러 구독자가 있어도 한 명만 이벤트 수신

---

## 문제 상황: StateFlow로 일회성 이벤트 처리

### 잘못된 코드 예시

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

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
```

### 발생하는 문제점

1. **화면 회전 시 중복 표시**: StateFlow는 최신 값을 보유하므로, 화면 회전 후 재구독 시 Snackbar가 다시 표시됨
2. **수동 초기화 필요**: `clearSnackbar()` 호출을 잊으면 이벤트가 계속 남아 있음
3. **Race Condition**: 연속 클릭 시 이벤트가 덮어씌워질 수 있음

---

## 해결책: 상태와 이벤트 분리

### 올바른 코드

```kotlin
// UI 상태 (지속적인 데이터)
data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
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

### Composable에서 수집

```kotlin
@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Channel 이벤트 수집
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

## collectAsState vs collectAsStateWithLifecycle

### collectAsState

```kotlin
val state by viewModel.uiState.collectAsState()
```

- **Composition 수명주기 따름**: Composable이 Composition에 있는 동안 수집
- **백그라운드에서도 수집**: 앱이 백그라운드여도 Flow 수집 계속
- **크로스 플랫폼용**: Compose Multiplatform에서 사용

### collectAsStateWithLifecycle (권장)

```kotlin
val state by viewModel.uiState.collectAsStateWithLifecycle()
```

- **Android Lifecycle 인식**: STARTED 상태 이상에서만 수집
- **리소스 절약**: 백그라운드에서는 수집 중단
- **공식 권장**: Android 앱에서는 이것을 사용

```kotlin
// 필요한 의존성
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.x")
```

---

## WhileSubscribed(5000)의 의미와 활용

### SharingStarted 정책

```kotlin
val uiState: StateFlow<UiState> = repository.getDataFlow()
    .map { data -> UiState(data = data) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState()
    )
```

### 동작 방식

| 정책 | 설명 |
|------|------|
| `Eagerly` | 즉시 시작, 영원히 유지 (리소스 낭비 가능) |
| `Lazily` | 첫 구독자 등장 시 시작, 영원히 유지 |
| `WhileSubscribed(5000)` | 구독자 있을 때만 활성, 5초 대기 후 중단 |

### 왜 5000ms인가?

```kotlin
SharingStarted.WhileSubscribed(
    stopTimeoutMillis = 5_000,  // 마지막 구독자 사라진 후 대기 시간
    replayExpirationMillis = 0  // replay 캐시 만료 시간
)
```

- **화면 회전 대응**: Configuration Change 시 Activity 재생성에 약 1-2초 소요
- **5초 대기**: 재구독할 충분한 시간 확보
- **이후 중단**: 5초 내 재구독 없으면 업스트림 중단하여 리소스 절약

---

## produceState vs StateFlow 비교

### produceState

```kotlin
@Composable
fun DetailScreen(itemId: String) {
    val item by produceState<Item?>(initialValue = null, key1 = itemId) {
        value = repository.getItem(itemId)  // suspend 함수
    }

    item?.let { ItemDetails(it) }
}
```

**특징:**
- Composable 내에서 suspend 함수를 State로 변환
- Composition 수명주기에 바인딩
- 단순한 일회성 데이터 로드에 적합

### StateFlow

```kotlin
class DetailViewModel(itemId: String) : ViewModel() {
    val item: StateFlow<Item?> = repository.observeItem(itemId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}
```

**특징:**
- ViewModel에서 장기적인 상태 관리
- Configuration Change에서도 상태 유지
- 여러 Composable에서 공유 가능

### 선택 기준

| 상황 | 권장 |
|------|------|
| 단순 일회성 데이터 로드 | `produceState` |
| 화면 수준 상태 관리 | `StateFlow` in ViewModel |
| 여러 화면에서 공유 | `StateFlow` in ViewModel |
| 실시간 업데이트 구독 | `StateFlow` with `stateIn` |

---

## MVI 패턴에서의 활용

### 구조

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

### 완전한 MVI 예시

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

## 사용 시나리오 정리

| 시나리오 | 도구 | 이유 |
|---------|------|------|
| 화면 UI 상태 | `StateFlow` | 최신 값 보유, 구독자에게 즉시 전달 |
| 로딩/에러 상태 | `StateFlow` | 지속적인 UI 표시 필요 |
| Snackbar 표시 | `Channel` | 일회성, 중복 표시 방지 |
| Navigation | `Channel` | 일회성, 한 번만 실행되어야 함 |
| 여러 화면에 알림 | `SharedFlow(replay=1)` | 모든 구독자에게 전달 |
| 실시간 위치 업데이트 | `StateFlow` + `WhileSubscribed` | 백그라운드에서 중단 |

---

## 주의사항

### 1. Channel은 LaunchedEffect에서 수집

```kotlin
// 올바른 방법
LaunchedEffect(Unit) {
    viewModel.events.collect { event -> /* 처리 */ }
}

// 잘못된 방법: Composable body에서 직접 수집
// viewModel.events.collect { } // 컴파일 에러
```

### 2. SharedFlow의 replay 설정 주의

```kotlin
// replay = 0이면 구독 전 이벤트 손실
private val _events = MutableSharedFlow<Event>(replay = 0)

// 안전한 설정: 최소 1개 버퍼
private val _events = MutableSharedFlow<Event>(
    replay = 0,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
```

### 3. collectAsStateWithLifecycle 사용 시 의존성 확인

```kotlin
// build.gradle.kts
dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.x")
}
```

---

## 연습 문제

1. **기초**: 잘못된 StateFlow 이벤트 처리 코드를 Channel로 분리하기
2. **중급**: collectAsState를 collectAsStateWithLifecycle로 변경하고 WhileSubscribed 적용
3. **심화**: 완전한 MVI 패턴으로 카운터 앱 구현

---

## 다음 학습

- [hilt_viewmodel](../hilt_viewmodel): Hilt와 ViewModel 통합
- [navigation](../navigation): Type-Safe Navigation과 이벤트 처리
- [effect_handlers_advanced](../effect_handlers_advanced): 고급 Side Effect 핸들러
