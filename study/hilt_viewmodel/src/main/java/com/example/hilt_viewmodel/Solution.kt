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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Solution: Hilt를 사용한 깔끔한 의존성 주입
 *
 * @HiltViewModel과 hiltViewModel()을 사용하면
 * Factory 코드 없이 깔끔하게 의존성을 주입받을 수 있습니다.
 */

// ========================================
// 해결책 코드 구조
// ========================================

/**
 * 데이터 클래스
 */
data class User(
    val id: String,
    val name: String,
    val email: String
)

sealed interface UiState {
    data object Loading : UiState
    data class Success(val user: User) : UiState
    data class Error(val message: String) : UiState
}

/**
 * Repository 인터페이스
 * - 인터페이스를 사용하면 테스트 시 Mock으로 쉽게 교체 가능
 */
interface UserRepository {
    suspend fun getUser(userId: String): User
}

/**
 * Repository 구현체
 * - @Inject constructor()로 Hilt가 자동 생성
 * - @Singleton으로 앱 전체에서 하나의 인스턴스 사용
 */
@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getUser(userId: String): User {
        delay(1000) // 네트워크 시뮬레이션
        return User(
            id = userId,
            name = "Hilt 사용자 $userId",
            email = "hilt_$userId@example.com"
        )
    }
}

/**
 * @HiltViewModel을 사용한 ViewModel
 *
 * 핵심 포인트:
 * 1. @HiltViewModel 어노테이션 추가
 * 2. @Inject constructor로 의존성 주입
 * 3. SavedStateHandle도 자동 주입 가능
 * 4. Factory 클래스 불필요!
 */
@HiltViewModel
class HiltUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // SavedStateHandle에서 Navigation 인자 자동 추출
    // Navigation Compose에서 전달된 인자가 자동으로 들어옴
    private val userId: String = savedStateHandle.get<String>("userId") ?: "default_user"

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _loadCount = MutableStateFlow(0)
    val loadCount: StateFlow<Int> = _loadCount.asStateFlow()

    fun loadUser() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val user = userRepository.getUser(userId)
                _uiState.value = UiState.Success(user)
                _loadCount.value++
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateUserId(newUserId: String) {
        savedStateHandle["userId"] = newUserId
    }
}

// ========================================
// Solution UI
// ========================================

@Composable
fun SolutionScreen() {
    // hiltViewModel()로 간단하게 ViewModel 획득!
    // Factory 코드가 전혀 필요 없음!
    val viewModel: HiltUserViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val loadCount by viewModel.loadCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: @HiltViewModel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hilt를 사용하면 Factory 코드 없이 " +
                            "의존성을 자동으로 주입받을 수 있습니다.\n\n" +
                            "아래에서 얼마나 코드가 간단해지는지 확인하세요!",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // 해결책 1: @HiltViewModel
        Text(
            text = "해결책 1: @HiltViewModel",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        SolutionDemo_HiltViewModel(viewModel, uiState, loadCount)

        // 해결책 2: SavedStateHandle 자동 주입
        HorizontalDivider()

        Text(
            text = "해결책 2: SavedStateHandle 자동 주입",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        SolutionDemo_SavedStateHandle()

        // 해결책 3: Module로 의존성 제공
        HorizontalDivider()

        Text(
            text = "해결책 3: Hilt Module",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        SolutionDemo_HiltModule()
    }
}

@Composable
private fun SolutionDemo_HiltViewModel(
    viewModel: HiltUserViewModel,
    uiState: UiState,
    loadCount: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hilt 코드 (간단!)",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// 1. ViewModel에 @HiltViewModel 추가
@HiltViewModel
class HiltUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    // ... ViewModel 로직
}

// 2. Composable에서 hiltViewModel() 호출
@Composable
fun UserScreen() {
    val viewModel: HiltUserViewModel = hiltViewModel()
    // 끝! Factory 코드 필요 없음!
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "비교: Factory 코드가 완전히 사라졌습니다!",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.loadUser() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("사용자 로드 (Hilt)")
            }

            Text(
                text = "로드 횟수: ${loadCount}회",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                is UiState.Success -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "사용자 정보",
                                fontWeight = FontWeight.Bold
                            )
                            Text("이름: ${uiState.user.name}")
                            Text("이메일: ${uiState.user.email}")
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = "에러: ${uiState.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SolutionDemo_SavedStateHandle() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Navigation 인자 자동 주입",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// Navigation 정의
composable("user/{userId}") {
    UserScreen()  // hiltViewModel()이 자동으로 처리!
}

// ViewModel에서 자동으로 인자 접근
@HiltViewModel
class UserViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Navigation 인자가 자동으로 SavedStateHandle에!
    val userId: String = checkNotNull(
        savedStateHandle["userId"]
    )
}

// 장점:
// - Factory 없이 Navigation 인자 전달
// - 프로세스 사망 후에도 데이터 복원
// - 타입 안전한 접근 가능
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Navigation 인자가 SavedStateHandle에 자동 주입됩니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SolutionDemo_HiltModule() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hilt Module로 의존성 제공",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// 방법 1: @Binds (인터페이스 바인딩)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}

// 방법 2: @Provides (직접 인스턴스 생성)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com")
            .build()
    }
}

// 이점:
// - 의존성 그래프 자동 관리
// - 테스트 시 @TestInstallIn으로 Mock 교체
// - Scope 관리 자동화 (@Singleton 등)
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "핵심 Scope 정리:",
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ScopeItem("@Singleton", "앱 전체에서 하나의 인스턴스")
                ScopeItem("@ActivityRetainedScoped", "Activity 수명 동안 유지")
                ScopeItem("@ViewModelScoped", "ViewModel 수명 동안 유지")
                ScopeItem("@ActivityScoped", "Activity 인스턴스에 바인딩")
            }
        }
    }
}

@Composable
private fun ScopeItem(scope: String, description: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = scope,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(160.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
