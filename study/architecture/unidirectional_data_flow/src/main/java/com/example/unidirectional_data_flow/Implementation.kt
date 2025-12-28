package com.example.unidirectional_data_flow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * 구현 화면
 *
 * MVI 패턴을 사용하여 UDF를 실제로 구현하는 방법을 학습합니다.
 */
@Composable
fun ImplementationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 구현 단계 개요
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MVI 패턴으로 UDF 구현하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "MVI(Model-View-Intent)는 UDF를 구현하는 대표적인 패턴입니다.\n\n" +
                            "- Event (Intent): 사용자 입력\n" +
                            "- State (Model): UI 상태\n" +
                            "- Effect: 일회성 이벤트 (네비게이션, 토스트)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Step 1: Event, State, Effect 정의
        StepCard(
            step = 1,
            title = "Event, State, Effect 정의",
            description = "sealed interface로 타입 안전한 이벤트와 상태를 정의합니다.",
            code = """
// Event - 사용자 또는 시스템 입력
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

// Effect - 일회성 이벤트
sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data class ShowSnackbar(val message: String) : LoginEffect
}
            """.trimIndent()
        )

        // Step 2: ViewModel 구현
        StepCard(
            step = 2,
            title = "ViewModel에서 상태 관리",
            description = "onEvent() 함수로 모든 이벤트를 처리하고, State와 Effect를 관리합니다.",
            code = """
class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // Effect는 Channel 사용 (일회성)
    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

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
            // API 호출...
            _effect.send(LoginEffect.NavigateToHome)
        }
    }
}
            """.trimIndent()
        )

        // Step 3: Compose UI 연동
        StepCard(
            step = 3,
            title = "Compose에서 State 수집 + Event 전달",
            description = "collectAsStateWithLifecycle()로 상태를 수집하고, " +
                    "LaunchedEffect로 Effect를 처리합니다.",
            code = """
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> { /* 네비게이션 */ }
                is LoginEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    // UI는 State만 표시, Event만 전달
    LoginContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}
            """.trimIndent()
        )

        // 실제 앱 예제
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "실제 앱 예제: 로그인 폼",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래는 완전한 MVI 패턴으로 구현된 로그인 폼입니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                RealWorldLoginExample()
            }
        }

        // 안티패턴
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "안티패턴 - 피해야 할 것들",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))

                AntiPatternCard(
                    title = "안티패턴 1: ViewModel을 하위 Composable에 전달",
                    why = "재사용성 저하, 테스트 어려움, SSOT 원칙 위반",
                    wrongCode = """
// 잘못된 예
@Composable
fun ParentScreen(viewModel: MyViewModel = viewModel()) {
    ChildComponent(viewModel = viewModel)  // X
}
                    """.trimIndent(),
                    correctCode = """
// 올바른 예
@Composable
fun ParentScreen(viewModel: MyViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ChildComponent(
        state = state,
        onEvent = viewModel::onEvent
    )
}
                    """.trimIndent()
                )

                Spacer(modifier = Modifier.height(12.dp))

                AntiPatternCard(
                    title = "안티패턴 2: 상태에 람다나 코루틴 포함",
                    why = "Recomposition 최적화 불가, 메모리 누수 위험",
                    wrongCode = """
// 잘못된 예
data class BadState(
    val onClick: () -> Unit,    // X
    val scope: CoroutineScope   // X
)
                    """.trimIndent(),
                    correctCode = """
// 올바른 예
data class GoodState(
    val isButtonEnabled: Boolean,
    val buttonText: String
)
                    """.trimIndent()
                )
            }
        }

        // State vs Effect
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "State vs Effect 구분",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "State",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "- 지속적\n- 입력값, 로딩\n- StateFlow\n- collectAsState",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Effect",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "- 일회성\n- 네비게이션, 토스트\n- Channel\n- LaunchedEffect",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // 테스트 가이드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "테스트 가이드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "UDF 패턴은 ViewModel 테스트를 쉽게 만듭니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = """
@Test
fun `email 입력 시 state가 업데이트된다`() = runTest {
    val viewModel = LoginViewModel(FakeRepository())

    viewModel.onEvent(LoginEvent.EmailChanged("test@email.com"))

    assertThat(viewModel.state.value.email)
        .isEqualTo("test@email.com")
}

@Test
fun `로그인 성공 시 NavigateToHome Effect 발생`() = runTest {
    val viewModel = LoginViewModel(FakeSuccessRepository())

    viewModel.onEvent(LoginEvent.LoginClicked)

    val effect = viewModel.effect.first()
    assertThat(effect).isEqualTo(LoginEffect.NavigateToHome)
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun StepCard(
    step: Int,
    title: String,
    description: String,
    code: String
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Step $step",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = code,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun AntiPatternCard(
    title: String,
    why: String,
    wrongCode: String,
    correctCode: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = why,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "잘못된 코드:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = wrongCode,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "올바른 코드:",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF2E7D32)
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Text(
                text = correctCode,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// ==================== 실제 앱 예제: 로그인 폼 MVI ====================

/**
 * 로그인 이벤트
 */
sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object TogglePasswordVisibility : LoginEvent
    data object LoginClicked : LoginEvent
    data object ClearError : LoginEvent
}

/**
 * 로그인 상태
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.length >= 4 && !isLoading
}

/**
 * 로그인 이펙트
 */
sealed interface LoginEffect {
    data object LoginSuccess : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}

/**
 * 로그인 ViewModel - MVI 패턴 구현
 */
class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.value = _state.value.copy(email = event.email, error = null)
            }
            is LoginEvent.PasswordChanged -> {
                _state.value = _state.value.copy(password = event.password, error = null)
            }
            LoginEvent.TogglePasswordVisibility -> {
                _state.value = _state.value.copy(
                    isPasswordVisible = !_state.value.isPasswordVisible
                )
            }
            LoginEvent.LoginClicked -> login()
            LoginEvent.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            // 로그인 API 시뮬레이션
            delay(1500)

            // 간단한 유효성 검사
            val email = _state.value.email
            val password = _state.value.password

            if (!email.contains("@")) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "올바른 이메일 형식이 아닙니다"
                )
                return@launch
            }

            if (password.length < 4) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "비밀번호는 4자 이상이어야 합니다"
                )
                return@launch
            }

            // 로그인 성공
            _state.value = _state.value.copy(isLoading = false)
            _effect.send(LoginEffect.LoginSuccess)
        }
    }
}

@Composable
private fun RealWorldLoginExample(viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.LoginSuccess -> {
                    showSuccessDialog = true
                }
                is LoginEffect.ShowError -> {
                    // Snackbar 표시
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("로그인 성공!") },
            text = { Text("MVI 패턴으로 구현된 로그인입니다.") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("확인")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 이메일 입력
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                label = { Text("이메일") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = state.error != null && state.email.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 비밀번호 입력
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = { Text("비밀번호") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.onEvent(LoginEvent.TogglePasswordVisibility) }
                    ) {
                        Icon(
                            imageVector = if (state.isPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = "비밀번호 표시/숨김"
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                isError = state.error != null && state.password.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // 에러 메시지
            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 로그인 버튼
            Button(
                onClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
                enabled = state.isLoginEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("로그인 중...")
                } else {
                    Text("로그인")
                }
            }

            // 상태 표시 (디버깅용)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "현재 State:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "email: ${state.email}\n" +
                                "password: ${"*".repeat(state.password.length)}\n" +
                                "isLoading: ${state.isLoading}\n" +
                                "error: ${state.error ?: "null"}",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
