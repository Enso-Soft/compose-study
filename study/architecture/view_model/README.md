# ViewModel + Compose 완벽 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `state_hoisting` | 상태 호이스팅과 단방향 데이터 흐름 패턴 | [📚 학습하기](../../state/state_hoisting/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

`ViewModel`은 Android Architecture Components의 핵심 요소로, **화면 수준의 상태를 관리**하고 **비즈니스 로직을 UI에서 분리**하는 역할을 합니다. Compose와 결합하여 단방향 데이터 흐름(UDF)을 구현하고, 테스트 가능한 앱 구조를 만들 수 있습니다.

```kotlin
@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    val count by viewModel.count.collectAsStateWithLifecycle()

    Button(onClick = { viewModel.increment() }) {
        Text("Count: $count")
    }
}
```

---

## 왜 ViewModel이 필요한가?

### 문제 상황

#### 1. Configuration Change 시 상태 손실

```kotlin
@Composable
fun BrokenCounter() {
    var count by remember { mutableStateOf(0) }  // 화면 회전 시 0으로 초기화!

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

**실제 피해 사례**:
- 사용자가 긴 폼을 작성하다가 화면을 회전하면 모든 입력이 사라짐
- 장바구니에 상품을 담다가 설정을 변경하면 장바구니가 비어짐

#### 2. UI와 비즈니스 로직 혼재

```kotlin
@Composable
fun ProductListBroken() {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    // 문제: API 호출 로직이 Composable 안에!
    LaunchedEffect(Unit) {
        products = api.fetchProducts()  // 비즈니스 로직
    }
    // ...
}
```

**문제점**:
- 테스트하려면 Compose Test Rule 필요 (무겁고 느림)
- 동일한 로직을 다른 화면에서 재사용 불가
- 관심사 분리 원칙(SoC) 위반

### 해결 목표

| 목표 | ViewModel이 제공하는 해결책 |
|------|---------------------------|
| 상태 유지 | Configuration Change에서 생존 |
| 관심사 분리 | UI는 표시만, 로직은 ViewModel에서 |
| 테스트 용이성 | JUnit으로 단위 테스트 가능 |
| 코드 재사용 | 여러 화면에서 ViewModel 공유 가능 |

---

## 핵심 원칙

### 원칙 1: 상태는 ViewModel에서 관리

ViewModel은 `MutableStateFlow`로 내부 상태를 관리하고, 외부에는 읽기 전용 `StateFlow`를 노출합니다.

```kotlin
class CounterViewModel : ViewModel() {
    // 내부: 수정 가능한 상태
    private val _count = MutableStateFlow(0)

    // 외부: 읽기 전용 상태
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() {
        _count.value++
    }

    fun decrement() {
        _count.value--
    }
}
```

**왜 이 패턴을 사용하나요?**
- `_count`(밑줄 접두사): ViewModel 내부에서만 수정
- `count`: UI에서는 읽기만 가능
- 캡슐화를 통해 상태 변경의 단일 진입점 보장

### 원칙 2: UI는 상태를 관찰만

Composable은 `collectAsStateWithLifecycle()`을 사용하여 StateFlow를 관찰합니다.

```kotlin
@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    // StateFlow를 Compose State로 변환 (Lifecycle-aware)
    val count by viewModel.count.collectAsStateWithLifecycle()

    Column {
        Text("Count: $count")
        Button(onClick = { viewModel.increment() }) {
            Text("+1")
        }
    }
}
```

> **collectAsState vs collectAsStateWithLifecycle**
>
> | 함수 | 특징 | 권장 여부 |
> |-----|------|----------|
> | `collectAsState()` | 항상 Flow 수집, 플랫폼 독립적 | 멀티플랫폼용 |
> | `collectAsStateWithLifecycle()` | Lifecycle 인식, 백그라운드에서 수집 중지 | **Android 권장** |
>
> Android에서는 `collectAsStateWithLifecycle()`을 사용하세요. 앱이 백그라운드에 있을 때 불필요한 리소스 사용을 방지합니다.

### 원칙 3: 이벤트는 ViewModel로 전달 (UDF)

**UDF(Unidirectional Data Flow, 단방향 데이터 흐름)** 패턴:

```
+-----------------------------------------------------------+
|                                                           |
|   +-----------+         상태          +-----------+       |
|   |           |  ----------------->  |           |       |
|   | ViewModel |                       |    UI     |       |
|   |           |  <-----------------  |           |       |
|   +-----------+        이벤트         +-----------+       |
|                                                           |
+-----------------------------------------------------------+

- 상태: ViewModel -> UI (StateFlow -> collectAsStateWithLifecycle)
- 이벤트: UI -> ViewModel (Button onClick -> viewModel.function())
```

**장점**:
- 데이터 흐름이 예측 가능
- 디버깅이 쉬움
- 상태 변경의 단일 진실 공급원(Single Source of Truth)

---

## 구현 방법

### Step 1: 의존성 추가

```kotlin
// build.gradle.kts
dependencies {
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // collectAsStateWithLifecycle (권장)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
}
```

### Step 2: ViewModel 정의

```kotlin
class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val products = repository.fetchProducts()
                _uiState.value = UiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

### Step 3: UI 상태 sealed class 정의

```kotlin
sealed class UiState {
    data object Loading : UiState()
    data class Success(val products: List<Product>) : UiState()
    data class Error(val message: String) : UiState()
}
```

### Step 4: Composable에서 사용

```kotlin
@Composable
fun ProductListScreen(viewModel: ProductViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> ProductList(state.products)
        is UiState.Error -> ErrorMessage(
            message = state.message,
            onRetry = { viewModel.loadProducts() }
        )
    }
}
```

---

## 실제 앱 예제

### 예제: 로그인 폼

```kotlin
// ViewModel
class LoginViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)

    val email: StateFlow<String> = _email.asStateFlow()
    val password: StateFlow<String> = _password.asStateFlow()
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    val isValid: Boolean
        get() = _email.value.contains("@") && _password.value.length >= 6

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun login() {
        if (!isValid) return

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                authRepository.login(_email.value, _password.value)
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

// Composable
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = viewModel::updateEmail,
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = viewModel::updatePassword,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = viewModel::login,
            enabled = viewModel.isValid && loginState !is LoginState.Loading
        ) {
            if (loginState is LoginState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else {
                Text("Login")
            }
        }

        if (loginState is LoginState.Error) {
            Text(
                text = (loginState as LoginState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
```

---

## 안티패턴

### 1. Context를 ViewModel에 전달

```kotlin
// 잘못된 코드: Activity Context 전달 -> 메모리 누수!
class BadViewModel(private val context: Context) : ViewModel()
```

```kotlin
// 올바른 방법 1: Application Context 사용
class GoodViewModel(application: Application) : AndroidViewModel(application) {
    fun getString() = getApplication<Application>().getString(R.string.hello)
}

// 올바른 방법 2: Repository에서 Context 처리
class BetterViewModel(private val repository: Repository) : ViewModel()
```

### 2. Composable 내부에 비즈니스 로직

```kotlin
// 잘못된 코드
@Composable
fun BadScreen() {
    var email by remember { mutableStateOf("") }

    // 비즈니스 로직이 Composable에!
    val isValid = email.contains("@") && email.contains(".")

    OutlinedTextField(value = email, onValueChange = { email = it })
    Button(enabled = isValid) { /* ... */ }
}
```

```kotlin
// 올바른 코드
@Composable
fun GoodScreen(viewModel: FormViewModel = viewModel()) {
    val email by viewModel.email.collectAsStateWithLifecycle()

    OutlinedTextField(
        value = email,
        onValueChange = viewModel::updateEmail  // ViewModel에 위임
    )
    Button(enabled = viewModel.isValid) { /* ... */ }
}
```

### 3. collectAsState 대신 직접 value 접근

```kotlin
// 잘못된 코드: Recomposition 트리거 안 됨!
@Composable
fun BadScreen(viewModel: MyViewModel = viewModel()) {
    val count = viewModel.count.value  // 직접 접근 - 반응성 없음!
    Text("Count: $count")
}
```

```kotlin
// 올바른 코드
@Composable
fun GoodScreen(viewModel: MyViewModel = viewModel()) {
    val count by viewModel.count.collectAsStateWithLifecycle()  // 반응성 있음
    Text("Count: $count")
}
```

---

## 테스트 가이드

ViewModel의 큰 장점은 **JUnit으로 단위 테스트가 가능**하다는 것입니다.

### ViewModel 단위 테스트

```kotlin
class CounterViewModelTest {

    @Test
    fun `increment increases count by 1`() {
        // Given
        val viewModel = CounterViewModel()

        // When
        viewModel.increment()

        // Then
        assertEquals(1, viewModel.count.value)
    }

    @Test
    fun `decrement decreases count by 1`() {
        // Given
        val viewModel = CounterViewModel()
        viewModel.increment()
        viewModel.increment()

        // When
        viewModel.decrement()

        // Then
        assertEquals(1, viewModel.count.value)
    }
}
```

### 비동기 작업 테스트

```kotlin
class ProductViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()  // Dispatchers.Main 대체

    @Test
    fun `loadProducts success updates state to Success`() = runTest {
        // Given
        val fakeRepository = FakeProductRepository(
            products = listOf(Product("1", "Test"))
        )
        val viewModel = ProductViewModel(fakeRepository)

        // When
        viewModel.loadProducts()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is UiState.Success)
    }
}
```

---

## remember vs ViewModel 비교

| 특성 | remember | ViewModel |
|------|----------|-----------|
| **수명** | Composition (Composable) | Activity/Fragment |
| **Configuration Change** | 상태 손실 | 상태 유지 |
| **프로세스 종료** | 상태 손실 | 상태 손실 (SavedStateHandle로 해결) |
| **비즈니스 로직** | 부적합 | 적합 |
| **테스트 용이성** | 어려움 | 쉬움 (JUnit 테스트 가능) |
| **사용 범위** | 간단한 UI 상태 | 화면 수준 상태 |

### 언제 무엇을 사용할까?

```kotlin
// remember: 단순한 UI 상태 (버튼 확장, 애니메이션 등)
var isExpanded by remember { mutableStateOf(false) }

// ViewModel: 비즈니스 로직이 필요한 상태
class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()
}
```

---

## 연습 문제

### 연습 1: 카운터 ViewModel 구현
MutableStateFlow로 카운터 상태를 관리하고, increment/decrement 함수를 구현하세요.

### 연습 2: 입력 폼 유효성 검사
이름이 3자 이상인지 검증하는 ViewModel을 구현하세요. 에러 메시지를 StateFlow로 노출하세요.

### 연습 3: 로딩 상태 UI
sealed class로 Loading/Success/Error 상태를 정의하고, viewModelScope로 비동기 작업을 처리하세요.

---

## 다음 학습

- **Hilt + ViewModel**: `@HiltViewModel`과 `hiltViewModel()`로 의존성 주입
- **SavedStateHandle**: 프로세스 종료 후에도 상태 복원
- **Navigation + ViewModel**: NavBackStackEntry 스코프의 ViewModel

---

## 참고 자료

- [ViewModel overview - Android Developers](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [State and Jetpack Compose - Android Developers](https://developer.android.com/develop/ui/compose/state)
- [Consuming flows safely in Jetpack Compose - Android Developers Medium](https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3)
- [Lifecycle Runtime Compose - Android Developers](https://developer.android.com/jetpack/androidx/releases/lifecycle)
