# Hilt + ViewModel 학습

## 개념

**Hilt**는 Android에서 의존성 주입(Dependency Injection, DI)을 간편하게 구현할 수 있게 해주는 Jetpack 라이브러리입니다. Dagger를 기반으로 하되, Android에 최적화된 설정과 생명주기 관리를 제공합니다.

**@HiltViewModel**은 Hilt와 ViewModel을 연결하여, ViewModel에 필요한 의존성을 자동으로 주입받을 수 있게 합니다.

```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel()
```

## 핵심 특징

### 1. @HiltViewModel 어노테이션
- ViewModel 클래스에 추가하면 Hilt가 자동으로 ViewModel을 제공
- 생성자에 `@Inject`를 사용하여 의존성 주입
- ViewModel Factory를 수동으로 만들 필요 없음

### 2. SavedStateHandle 자동 주입
- Navigation 인자가 자동으로 SavedStateHandle에 포함됨
- `savedStateHandle["key"]`로 간편하게 접근
- 프로세스 사망 후에도 데이터 복원 가능

### 3. hiltViewModel() Composable 함수
- Compose에서 ViewModel을 가져오는 표준 방법
- NavGraph 단위로 scope 가능
- 생명주기 자동 관리

### 4. Hilt Module로 의존성 제공
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepositoryImpl()
}
```

## 문제 상황: Hilt 없이 ViewModel에 의존성 전달

### 잘못된 코드 예시

```kotlin
// 수동으로 Factory 작성 필요
class UserViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository) as T
    }
}

// Composable에서 수동으로 Factory 사용
@Composable
fun UserScreen() {
    val factory = remember {
        UserViewModelFactory(UserRepositoryImpl())
    }
    val viewModel: UserViewModel = viewModel(factory = factory)
}
```

### 발생하는 문제점

1. **보일러플레이트 코드 폭발**: 모든 ViewModel마다 Factory 클래스 필요
2. **의존성 그래프 관리 어려움**: 의존성이 많아지면 수동 생성이 복잡
3. **테스트 어려움**: 의존성 교체가 힘들어 Mock 주입이 어려움
4. **Navigation 인자 전달 번거로움**: SavedStateHandle을 수동으로 처리해야 함

## 해결책: @HiltViewModel 사용

### 올바른 코드

```kotlin
// Step 1: Repository 인터페이스와 구현
interface UserRepository {
    suspend fun getUser(id: String): User
}

class UserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getUser(id: String): User = /* API 호출 */
}

// Step 2: Hilt Module로 의존성 제공
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}

// Step 3: @HiltViewModel로 ViewModel 정의
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _uiState.value = try {
                UiState.Success(userRepository.getUser(userId))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

// Step 4: Compose에서 hiltViewModel() 사용
@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // UI 렌더링
}
```

### 해결되는 이유

1. **Factory 코드 제거**: Hilt가 자동으로 ViewModel 생성 및 의존성 주입
2. **의존성 그래프 자동 관리**: Module에서 선언만 하면 Hilt가 알아서 연결
3. **테스트 용이**: `@TestInstallIn`으로 Mock 의존성 쉽게 교체
4. **Navigation 인자 자동 주입**: SavedStateHandle에 nav argument가 자동 포함

## 사용 시나리오

### 1. API 호출이 필요한 ViewModel
```kotlin
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getProducts()
        }
    }
}
```

### 2. Navigation 인자를 받는 ViewModel
```kotlin
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Navigation에서 전달된 productId 자동 추출
    val productId: String = checkNotNull(savedStateHandle["productId"])
}
```

### 3. 여러 화면에서 ViewModel 공유 (NavGraph Scope)
```kotlin
@Composable
fun CheckoutFlow(navController: NavHostController) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry("checkout_graph")
    }
    val sharedViewModel: CheckoutViewModel = hiltViewModel(parentEntry)

    NavHost(navController, startDestination = "address") {
        composable("address") { AddressScreen(sharedViewModel) }
        composable("payment") { PaymentScreen(sharedViewModel) }
        composable("confirm") { ConfirmScreen(sharedViewModel) }
    }
}
```

### 4. Assisted Injection (동적 파라미터)
```kotlin
@HiltViewModel(assistedFactory = ProductViewModel.Factory::class)
class ProductViewModel @AssistedInject constructor(
    @Assisted val productId: String,
    private val repository: ProductRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(productId: String): ProductViewModel
    }
}

@Composable
fun ProductScreen(productId: String) {
    val viewModel = hiltViewModel<ProductViewModel, ProductViewModel.Factory> { factory ->
        factory.create(productId)
    }
}
```

## Hilt 필수 설정

### 1. Application 클래스
```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

### 2. Activity
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity()
```

### 3. Gradle 의존성
```kotlin
// build.gradle.kts (project)
plugins {
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
}

// build.gradle.kts (module)
plugins {
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}
```

## 주의사항

1. **@HiltAndroidApp 필수**: Application 클래스에 반드시 추가
2. **@AndroidEntryPoint 필수**: Hilt를 사용하는 Activity에 추가
3. **SavedStateHandle vs Assisted Injection**:
   - SavedStateHandle: 프로세스 사망 시에도 데이터 복원 가능
   - Assisted Injection: Parcelable이 아닌 데이터 전달 가능하지만 복원 불가
4. **ViewModel Scope 이해**:
   - `@ViewModelScoped`: 해당 ViewModel 내에서만 단일 인스턴스
   - `@ActivityRetainedScoped`: Activity 수명 동안 공유
   - `@Singleton`: 앱 전체에서 공유

## 연습 문제

1. **기본 @HiltViewModel 구현**: TodoRepository를 주입받는 TodoViewModel 만들기
2. **SavedStateHandle 활용**: Navigation 인자로 productId를 받아 상품 로드하기
3. **NavGraph Scope 활용**: 주문 플로우에서 여러 화면이 OrderViewModel 공유하기

## 다음 학습

- [Deep Link](../deep_link) - Navigation + URI 처리
