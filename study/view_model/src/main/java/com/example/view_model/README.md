# ViewModel + Compose 학습

## 개념

`ViewModel`은 Android Architecture Components의 핵심 요소로, **화면 수준의 상태를 관리**하고 **비즈니스 로직을 UI에서 분리**하는 역할을 합니다.

Compose에서는 `viewModel()` 함수를 통해 ViewModel 인스턴스를 가져오고, `StateFlow`를 `collectAsState()`로 변환하여 상태를 관찰합니다.

```kotlin
@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    val count by viewModel.count.collectAsState()

    Button(onClick = { viewModel.increment() }) {
        Text("Count: $count")
    }
}
```

## 핵심 특징

1. **Configuration Change 생존**: 화면 회전, 언어 변경 등에서도 상태 유지
2. **Lifecycle-aware**: Activity/Fragment 수명주기와 자동 연동
3. **비즈니스 로직 분리**: UI는 상태만 표시, 로직은 ViewModel에서 처리
4. **viewModelScope**: ViewModel 전용 코루틴 스코프 제공

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

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = repository.fetchProducts()
        }
    }
}
```

## 문제 상황: remember만으로 앱 상태 관리

### 문제 1: Configuration Change 시 상태 손실

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

### 문제 2: Composable 내부 비즈니스 로직 혼재

```kotlin
@Composable
fun ProductListBroken() {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // 문제: API 호출 로직이 Composable 안에!
    LaunchedEffect(Unit) {
        try {
            products = api.fetchProducts()  // 비즈니스 로직
        } finally {
            isLoading = false
        }
    }

    // UI 렌더링...
}
```

**문제점**:
- 테스트하려면 Compose Test Rule 필요 (무겁고 느림)
- 동일한 로직을 다른 화면에서 재사용 불가
- 관심사 분리 원칙(SoC) 위반

## 해결책: ViewModel + StateFlow + collectAsState()

### 기본 패턴

```kotlin
// 1. ViewModel 정의
class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() {
        _count.value++
    }

    fun decrement() {
        _count.value--
    }
}

// 2. Composable에서 사용
@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    val count by viewModel.count.collectAsState()

    Column {
        Text("Count: $count")
        Row {
            Button(onClick = { viewModel.decrement() }) { Text("-") }
            Button(onClick = { viewModel.increment() }) { Text("+") }
        }
    }
}
```

### 비동기 작업 처리

```kotlin
class ProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {  // ViewModel 생명주기와 연동된 코루틴
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

sealed class UiState {
    object Loading : UiState()
    data class Success(val products: List<Product>) : UiState()
    data class Error(val message: String) : UiState()
}
```

## UDF (Unidirectional Data Flow) 패턴

ViewModel과 Compose의 이상적인 데이터 흐름:

```
+---------------------------------------------------------+
|                                                         |
|   +-----------+         상태          +-----------+     |
|   |           |  ------------------> |           |     |
|   | ViewModel |                       |    UI     |     |
|   |           |  <------------------ |           |     |
|   +-----------+        이벤트         +-----------+     |
|                                                         |
+---------------------------------------------------------+

- 상태: ViewModel -> UI (StateFlow -> collectAsState)
- 이벤트: UI -> ViewModel (Button onClick -> viewModel.function())
```

**장점**:
- 데이터 흐름이 예측 가능
- 디버깅이 쉬움
- 상태 변경의 단일 진실 공급원(Single Source of Truth)

## 사용 시나리오

### 1. 화면 회전에서 상태 유지

```kotlin
class FormViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    private val _email = MutableStateFlow("")

    val name = _name.asStateFlow()
    val email = _email.asStateFlow()

    fun updateName(value: String) { _name.value = value }
    fun updateEmail(value: String) { _email.value = value }
}
```

### 2. 로딩 상태 관리

```kotlin
@Composable
fun ProductListScreen(viewModel: ProductViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> ProductList(state.products)
        is UiState.Error -> ErrorMessage(state.message)
    }
}
```

### 3. 사용자 입력 유효성 검사

```kotlin
class LoginViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    private val _emailError = MutableStateFlow<String?>(null)

    val email = _email.asStateFlow()
    val emailError = _emailError.asStateFlow()

    fun updateEmail(value: String) {
        _email.value = value
        _emailError.value = when {
            value.isEmpty() -> "이메일을 입력해주세요"
            !value.contains("@") -> "올바른 이메일 형식이 아닙니다"
            else -> null
        }
    }

    val isValid: Boolean
        get() = _emailError.value == null && _email.value.isNotEmpty()
}
```

## 주의사항

### 1. ViewModel에 Context 전달 금지

```kotlin
// 잘못된 코드: Activity Context 전달 -> 메모리 누수!
class BadViewModel(private val context: Context) : ViewModel()

// 올바른 방법 1: Application Context 사용
class GoodViewModel(application: Application) : AndroidViewModel(application) {
    fun getString() = getApplication<Application>().getString(R.string.hello)
}

// 올바른 방법 2: Repository에서 Context 처리
class BetterViewModel(private val repository: Repository) : ViewModel()
```

### 2. ViewModel은 Composable 스코프 한정 불가

```kotlin
// ViewModel은 Activity/Fragment 수명주기에 바인딩됨
// 개별 Composable에 스코프 지정 불가

@Composable
fun ParentScreen() {
    // 이 ViewModel은 Activity에 바인딩됨
    val viewModel: SharedViewModel = viewModel()

    ChildA(viewModel)  // 같은 인스턴스 공유
    ChildB(viewModel)  // 같은 인스턴스 공유
}
```

### 3. collectAsState() 수명주기 주의

```kotlin
@Composable
fun Screen(viewModel: MyViewModel = viewModel()) {
    // collectAsState()는 Composable이 활성화된 동안만 수집
    // Lifecycle.State.STARTED 이상에서만 수집됨
    val state by viewModel.uiState.collectAsStateWithLifecycle()  // 권장
}
```

## 다음 학습

- **Hilt + ViewModel**: `@HiltViewModel`과 `hiltViewModel()`로 의존성 주입
- **SavedStateHandle**: 프로세스 종료 후에도 상태 복원
- **Navigation + ViewModel**: NavBackStackEntry 스코프의 ViewModel

## 연습 문제

1. **카운터 ViewModel 구현**: MutableStateFlow로 카운터 상태 관리
2. **입력 폼 유효성 검사**: 이름 입력 시 3자 이상인지 검증
3. **로딩 상태 UI**: sealed class로 Loading/Success/Error 상태 구현

## 참고 자료

- [ViewModel overview - Android Developers](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Compose and other libraries - Android Developers](https://developer.android.com/develop/ui/compose/libraries)
- [State and Jetpack Compose - Android Developers](https://developer.android.com/develop/ui/compose/state)
- [viewModel vs hiltViewModel - Medium](https://kimmandoo.medium.com/viewmodel-hiltviewmodel-7be6aa6e0891)
