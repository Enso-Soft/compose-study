# Unidirectional Data Flow (UDF) - 단방향 데이터 흐름

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `ViewModel` | 화면의 상태와 로직을 관리하는 컴포넌트 | [📚 학습하기](../view_model/README.md) |
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**단방향 데이터 흐름(Unidirectional Data Flow, UDF)**은 Compose 아키텍처의 핵심 패턴입니다.
데이터가 한 방향으로만 흐르도록 설계하여 UI를 예측 가능하고, 테스트 가능하며, 디버깅하기 쉽게 만듭니다.

```
User Event --> ViewModel --> State --> UI
     ^                                  |
     |__________________________________|
              (이벤트만 위로)
```

> **비유**: 교통 신호 시스템을 생각해보세요.
> - 버튼을 누르면(Event) 제어기(ViewModel)가 처리합니다
> - 제어기가 신호등 색상(State)을 변경합니다
> - 신호등(UI)은 색상만 표시합니다
> - 신호등이 직접 색상을 바꾸지 않습니다 (단방향!)

---

## 왜 UDF가 필요한가?

### 양방향 데이터 흐름의 문제점

```kotlin
// 문제가 있는 코드 - 여러 곳에서 상태 수정
@Composable
fun ProblematicCounter() {
    var count by remember { mutableStateOf(0) }

    // 문제 1: 버튼에서 직접 수정
    Button(onClick = { count++ }) { Text("+1") }

    // 문제 2: Effect에서도 수정
    LaunchedEffect(Unit) {
        delay(1000)
        count = fetchFromServer()  // 어디서 바뀌었지?
    }

    // 문제 3: 조건에 따라 수정
    if (count > 10) {
        count = 0  // 버그 추적 어려움!
    }
}
```

**발생하는 문제:**
1. **추적 어려움**: 상태가 어디서 변경되었는지 파악 어려움
2. **버그 재현 어려움**: 동일한 버그를 재현하기 힘듦
3. **테스트 어려움**: UI와 로직이 섞여 단위 테스트 불가
4. **유지보수 어려움**: 코드가 복잡해질수록 관리 힘듦

### UDF의 해결책

```kotlin
// UDF 패턴 - 상태는 ViewModel에서만 관리
class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() { _count.value++ }
    fun reset() { _count.value = 0 }
}

@Composable
fun UDFCounter(viewModel: CounterViewModel = viewModel()) {
    val count by viewModel.count.collectAsStateWithLifecycle()

    // UI는 상태만 표시, 이벤트만 전달
    Text("Count: $count")
    Button(onClick = { viewModel.increment() }) { Text("+1") }
}
```

---

## 핵심 원칙

### 원칙 1: 상태는 아래로, 이벤트는 위로 (State Down, Event Up)

```
ViewModel
    |
    v (State 전달)
Composable Screen
    |
    v (State 전달)
Child Composable
    |
    ^ (Event 전달)
    |
ViewModel.onEvent()
```

```kotlin
// ViewModel에서 상태 관리
class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged ->
                _state.update { it.copy(email = event.email) }
            is LoginEvent.PasswordChanged ->
                _state.update { it.copy(password = event.password) }
            LoginEvent.LoginClicked -> login()
        }
    }
}

// UI는 상태 표시 + 이벤트 전달만
@Composable
fun LoginScreen(
    state: LoginState,           // State Down
    onEvent: (LoginEvent) -> Unit // Event Up
) {
    TextField(
        value = state.email,
        onValueChange = { onEvent(LoginEvent.EmailChanged(it)) }
    )
}
```

### 원칙 2: 단일 진실 공급원 (Single Source of Truth, SSOT)

상태는 **한 곳**에서만 관리합니다. 여러 Composable이 같은 상태를 공유하더라도,
상태의 원본은 ViewModel의 StateFlow **하나**입니다.

```kotlin
// SSOT - ViewModel이 유일한 상태 소유자
class CartViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val totalPrice: StateFlow<Int> = _items.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}

// 여러 Composable이 같은 상태 공유
@Composable
fun ShoppingScreen(viewModel: CartViewModel = viewModel()) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val totalPrice by viewModel.totalPrice.collectAsStateWithLifecycle()

    Column {
        CartItemList(items = items)  // 같은 상태 사용
        CartBadge(itemCount = items.size)  // 같은 상태 사용
        TotalPrice(price = totalPrice)  // 파생 상태 사용
    }
}
```

### 원칙 3: 불변 상태 (Immutable State)

상태는 **data class**로 정의하고, **copy()**로 새 상태를 생성합니다.
직접 수정하지 않습니다.

```kotlin
// 불변 상태 정의
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

// 상태 변경 시 copy() 사용
fun onEmailChanged(email: String) {
    _state.update { currentState ->
        currentState.copy(email = email)  // 새 객체 생성
    }
}

// 이렇게 하면 안 됨!
fun badUpdate(email: String) {
    _state.value.email = email  // 컴파일 에러 (val이므로)
}
```

**불변 상태의 장점:**
- Recomposition 최적화 (값 비교로 변경 감지)
- 예측 가능한 상태 변화
- 디버깅 용이 (이전 상태 추적 가능)

---

## MVI 패턴 구현

MVI(Model-View-Intent)는 UDF를 구현하는 대표적인 패턴입니다.

### Step 1: Event, State, Effect 정의

```kotlin
// Event (Intent) - 사용자 또는 시스템 입력
sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object LoginClicked : LoginEvent
    data object ClearError : LoginEvent
}

// State - UI가 표시해야 할 불변 상태
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

// Effect - 일회성 이벤트 (네비게이션, 토스트 등)
sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data class ShowSnackbar(val message: String) : LoginEffect
}
```

### Step 2: ViewModel 구현

```kotlin
class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // Effect는 Channel 사용 (일회성 이벤트)
    private val _effect = Channel<LoginEffect>()
    val effect: Flow<LoginEffect> = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged ->
                _state.update { it.copy(email = event.email) }
            is LoginEvent.PasswordChanged ->
                _state.update { it.copy(password = event.password) }
            LoginEvent.LoginClicked -> login()
            LoginEvent.ClearError ->
                _state.update { it.copy(error = null) }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = repository.login(
                _state.value.email,
                _state.value.password
            )

            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(LoginEffect.NavigateToHome)
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
            )
        }
    }
}
```

### Step 3: Compose UI 연동

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect 처리 (일회성 이벤트)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> onNavigateToHome()
                is LoginEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        LoginContent(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun LoginContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit
) {
    Column {
        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (state.error != null) {
            Text(state.error, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { onEvent(LoginEvent.LoginClicked) },
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text("Login")
            }
        }
    }
}
```

---

## 안티패턴

### 1. ViewModel을 하위 Composable에 전달

```kotlin
// 잘못된 예
@Composable
fun ParentScreen(viewModel: MyViewModel = viewModel()) {
    ChildComponent(viewModel = viewModel)  // ViewModel 전달 X
}

// 올바른 예
@Composable
fun ParentScreen(viewModel: MyViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ChildComponent(
        state = state,
        onEvent = viewModel::onEvent
    )
}
```

### 2. Composable에서 직접 상태 수정

```kotlin
// 잘못된 예
@Composable
fun BadExample(viewModel: MyViewModel) {
    Button(onClick = {
        viewModel._state.value = newState  // 직접 수정 X
    })
}

// 올바른 예
@Composable
fun GoodExample(onEvent: (MyEvent) -> Unit) {
    Button(onClick = {
        onEvent(MyEvent.ButtonClicked)  // 이벤트만 전달
    })
}
```

### 3. 상태에 람다나 코루틴 포함

```kotlin
// 잘못된 예
data class BadState(
    val onClick: () -> Unit,  // 람다 X
    val scope: CoroutineScope  // 코루틴 스코프 X
)

// 올바른 예
data class GoodState(
    val isButtonEnabled: Boolean,
    val buttonText: String
)
```

---

## State vs Effect 구분

| 구분 | State | Effect |
|------|-------|--------|
| 지속성 | 명시적 업데이트까지 유지 | 한 번 발생하고 사라짐 |
| 예시 | 입력값, 로딩 상태, 에러 메시지 | 네비게이션, 토스트, Snackbar |
| 구현 | StateFlow | Channel |
| 수집 | collectAsStateWithLifecycle | LaunchedEffect + collect |

---

## 테스트 가이드

UDF 패턴은 테스트를 용이하게 합니다.

```kotlin
@Test
fun `email 입력 시 state가 업데이트된다`() = runTest {
    // Given
    val viewModel = LoginViewModel(FakeRepository())

    // When
    viewModel.onEvent(LoginEvent.EmailChanged("test@email.com"))

    // Then
    assertThat(viewModel.state.value.email).isEqualTo("test@email.com")
}

@Test
fun `로그인 성공 시 NavigateToHome Effect가 발생한다`() = runTest {
    // Given
    val viewModel = LoginViewModel(FakeSuccessRepository())

    // When
    viewModel.onEvent(LoginEvent.LoginClicked)

    // Then
    val effect = viewModel.effect.first()
    assertThat(effect).isEqualTo(LoginEffect.NavigateToHome)
}
```

---

## 연습 문제

### 연습 1: 기본 UDF 적용 (쉬움)
remember로 구현된 카운터를 UDF 패턴으로 리팩토링하세요.

### 연습 2: 상태 공유 (중간)
장바구니와 상품 목록이 같은 상태를 공유하도록 UDF로 구현하세요.

### 연습 3: 전체 MVI 구현 (어려움)
검색 기능을 완전한 MVI 패턴으로 구현하세요 (Event, State, Effect 모두 포함).

---

## 다음 학습

- **ViewModel**: UDF의 상태 관리자
- **State Hoisting**: 상태 끌어올리기 패턴
- **Hilt + ViewModel**: 의존성 주입과 ViewModel
