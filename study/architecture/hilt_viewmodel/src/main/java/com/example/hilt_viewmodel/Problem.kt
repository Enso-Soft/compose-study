package com.example.hilt_viewmodel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Problem: Hilt 없이 ViewModel에 의존성을 전달하는 문제점
 *
 * 이 화면에서는 Hilt 없이 수동으로 ViewModel Factory를 만들어야 하는
 * 번거로움과 문제점을 보여줍니다.
 */

// ========================================
// 문제 상황의 코드 구조
// ========================================

/**
 * 문제 1: 수동 Repository 구현
 * - 실제 앱에서는 API, Database 등 여러 의존성이 필요
 * - 하드코딩된 의존성은 테스트가 어려움
 */
class ManualUserRepository {
    suspend fun getUser(userId: String): ManualUser {
        delay(1000) // 네트워크 시뮬레이션
        return ManualUser(
            id = userId,
            name = "사용자 $userId",
            email = "$userId@example.com"
        )
    }
}

data class ManualUser(
    val id: String,
    val name: String,
    val email: String
)

/**
 * 문제 2: ViewModel에 의존성을 전달하려면 생성자 주입이 필요
 * - 하지만 Compose의 viewModel()은 기본 생성자만 지원
 * - 따라서 별도의 Factory 클래스가 필요!
 */
class ManualUserViewModel(
    private val repository: ManualUserRepository,
    private val userId: String
) : ViewModel() {

    private val _user = MutableStateFlow<ManualUser?>(null)
    val user: StateFlow<ManualUser?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    suspend fun loadUser() {
        _isLoading.value = true
        _user.value = repository.getUser(userId)
        _isLoading.value = false
    }
}

/**
 * 문제 3: 매번 수동으로 Factory 클래스를 작성해야 함!
 *
 * ViewModel마다 Factory 클래스가 필요하고,
 * 의존성이 추가될 때마다 Factory도 수정해야 함
 */
class ManualUserViewModelFactory(
    private val repository: ManualUserRepository,
    private val userId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManualUserViewModel::class.java)) {
            return ManualUserViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

// ========================================
// 문제 상황을 보여주는 UI
// ========================================

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hilt 없이 ViewModel에 의존성을 주입하려면 " +
                            "매번 ViewModelProvider.Factory를 수동으로 작성해야 합니다.\n\n" +
                            "아래 코드를 보면 얼마나 번거로운지 알 수 있습니다!",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: Factory 보일러플레이트
        Text(
            text = "문제 1: Factory 보일러플레이트",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ProblemDemo_FactoryBoilerplate()

        // 문제 2: 의존성 그래프 관리 어려움
        HorizontalDivider()

        Text(
            text = "문제 2: 의존성 그래프 관리 어려움",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ProblemDemo_DependencyGraph()

        // 문제 3: Navigation 인자 전달 번거로움
        HorizontalDivider()

        Text(
            text = "문제 3: Navigation 인자 전달",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ProblemDemo_NavigationArgs()
    }
}

@Composable
private fun ProblemDemo_FactoryBoilerplate() {
    // 수동으로 Factory를 생성해야 함
    val factory = remember {
        ManualUserViewModelFactory(
            repository = ManualUserRepository(),
            userId = "user123"
        )
    }

    // 이렇게 Factory를 전달해야 함
    val viewModel: ManualUserViewModel = viewModel(factory = factory)
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "필요한 코드:",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 문제점 코드 표시
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// 1. Repository 클래스
class ManualUserRepository { ... }

// 2. ViewModel 클래스
class ManualUserViewModel(
    private val repository: ManualUserRepository,
    private val userId: String
) : ViewModel() { ... }

// 3. Factory 클래스 (매번 작성 필요!)
class ManualUserViewModelFactory(
    private val repository: ManualUserRepository,
    private val userId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return ManualUserViewModel(
            repository, userId
        ) as T
    }
}

// 4. Composable에서 사용
@Composable
fun UserScreen() {
    val factory = remember {
        ManualUserViewModelFactory(
            ManualUserRepository(),
            "user123"
        )
    }
    val viewModel = viewModel(factory = factory)
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 주의: MainScope() 사용은 권장되지 않습니다.
            // 실제 앱에서는 viewModelScope나 LaunchedEffect를 사용하세요.
            Button(
                onClick = {
                    kotlinx.coroutines.MainScope().launch {
                        viewModel.loadUser()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("사용자 로드 (수동 Factory)")
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            user?.let {
                Text(
                    text = "결과: ${it.name} (${it.email})",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ProblemDemo_DependencyGraph() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "의존성이 늘어나면?",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// 의존성이 많아지면 관리가 복잡해짐!
class ComplexViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val analyticsService: AnalyticsService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel()

// Factory도 복잡해짐!
class ComplexViewModelFactory(
    private val userRepo: UserRepository,
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository,
    private val analytics: AnalyticsService,
    private val savedState: SavedStateHandle
) : ViewModelProvider.Factory { ... }

// Composable에서 모든 의존성을 직접 생성!
val factory = ComplexViewModelFactory(
    UserRepositoryImpl(apiService, database),
    ProductRepositoryImpl(apiService),
    CartRepositoryImpl(database),
    AnalyticsServiceImpl(),
    savedStateHandle
)
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "이런 문제가 발생합니다:",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "- 의존성 추가할 때마다 Factory 수정\n" +
                        "- 의존성의 의존성도 직접 생성해야 함\n" +
                        "- 코드 중복 증가\n" +
                        "- 테스트 시 Mock 교체 어려움",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ProblemDemo_NavigationArgs() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Navigation 인자를 ViewModel에 전달하려면?",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// Navigation에서 인자 추출
composable("user/{userId}") { backStackEntry ->
    val userId = backStackEntry.arguments
        ?.getString("userId") ?: ""

    // ViewModel에 어떻게 전달하지?
    // → Factory에 전달해야 함!
    val factory = remember(userId) {
        UserViewModelFactory(
            UserRepositoryImpl(),
            userId  // 매번 새 Factory 생성
        )
    }
    val viewModel = viewModel(factory = factory)
}

// SavedStateHandle도 수동으로 처리해야 함
class SavedStateViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: UserRepository
) : AbstractSavedStateViewModelFactory(
    owner, null
) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return UserViewModel(repository, handle) as T
    }
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "결론: 너무 복잡하고 유지보수가 어렵습니다!",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Solution 탭에서 Hilt로 이 모든 문제를 해결하는 방법을 확인하세요.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * kotlinx.coroutines 확장 함수
 *
 * 주의: MainScope() 사용은 권장되지 않습니다.
 * 실제 앱에서는 LaunchedEffect나 viewModelScope를 사용하세요.
 * 이 예제에서는 Problem 상황을 시연하기 위해 사용합니다.
 */
private fun kotlinx.coroutines.CoroutineScope.launch(block: suspend () -> Unit): kotlinx.coroutines.Job {
    return kotlinx.coroutines.MainScope().launch { block() }
}
