package com.example.view_model

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Solution: ViewModel을 사용한 올바른 상태 관리
 *
 * ViewModel을 사용하면:
 * 1. Configuration Change에서도 상태 유지
 * 2. 비즈니스 로직을 UI에서 분리
 * 3. 테스트 용이성 향상
 */

// ==================== ViewModel 정의 ====================

/**
 * 카운터 ViewModel
 * MutableStateFlow로 상태를 관리하고, 외부에는 읽기 전용 StateFlow 노출
 */
class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() {
        _count.value++
    }

    fun decrement() {
        _count.value--
    }

    fun reset() {
        _count.value = 0
    }
}

/**
 * 폼 ViewModel
 * 비즈니스 로직(유효성 검사)을 ViewModel에서 처리
 */
class FormViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _nameError = MutableStateFlow<String?>(null)
    private val _emailError = MutableStateFlow<String?>(null)

    val name: StateFlow<String> = _name.asStateFlow()
    val email: StateFlow<String> = _email.asStateFlow()
    val nameError: StateFlow<String?> = _nameError.asStateFlow()
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    // 유효성 검사 로직이 ViewModel에! (테스트 가능)
    fun updateName(value: String) {
        _name.value = value
        _nameError.value = validateName(value)
    }

    fun updateEmail(value: String) {
        _email.value = value
        _emailError.value = validateEmail(value)
    }

    // 순수 함수로 분리 - 단위 테스트 가능!
    private fun validateName(name: String): String? {
        return when {
            name.isEmpty() -> null  // 아직 입력 안 함
            name.length < 2 -> "이름은 2자 이상이어야 합니다"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> null
            !email.contains("@") -> "올바른 이메일 형식이 아닙니다"
            !email.contains(".") -> "도메인을 포함해주세요"
            else -> null
        }
    }

    val isValid: Boolean
        get() = _name.value.length >= 2 &&
                _email.value.contains("@") &&
                _email.value.contains(".") &&
                _nameError.value == null &&
                _emailError.value == null
}

/**
 * 비동기 작업 ViewModel
 * sealed class로 UI 상태 관리
 */
sealed class DataUiState {
    data object Loading : DataUiState()
    data class Success(val data: List<String>) : DataUiState()
    data class Error(val message: String) : DataUiState()
}

class DataViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<DataUiState>(DataUiState.Loading)
    val uiState: StateFlow<DataUiState> = _uiState.asStateFlow()

    private val _loadCount = MutableStateFlow(0)
    val loadCount: StateFlow<Int> = _loadCount.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _loadCount.value++
        viewModelScope.launch {
            _uiState.value = DataUiState.Loading
            delay(1500)  // 네트워크 요청 시뮬레이션

            // 랜덤하게 성공/실패
            if ((0..10).random() > 2) {
                _uiState.value = DataUiState.Success(
                    listOf(
                        "데이터 항목 1",
                        "데이터 항목 2",
                        "데이터 항목 3",
                        "로드 횟수: ${_loadCount.value}"
                    )
                )
            } else {
                _uiState.value = DataUiState.Error("네트워크 오류가 발생했습니다")
            }
        }
    }
}

// ==================== UI 컴포저블 ====================

@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("카운터") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("폼 검증") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("비동기") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> CounterDemo()
            1 -> FormDemo()
            2 -> AsyncDataDemo()
        }
    }
}

/**
 * 데모 1: ViewModel 카운터
 * Configuration Change에서도 상태 유지
 *
 * collectAsStateWithLifecycle vs collectAsState:
 * - collectAsStateWithLifecycle: Lifecycle 인식, 백그라운드에서 수집 중지 (Android 권장)
 * - collectAsState: 항상 수집, 플랫폼 독립적 (멀티플랫폼용)
 */
@Composable
fun CounterDemo(viewModel: CounterViewModel = viewModel()) {
    // StateFlow를 Compose State로 변환 (Lifecycle-aware - Android 권장!)
    val count by viewModel.count.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("viewModel: CounterViewModel = viewModel()")
                Text("val count by viewModel.count.collectAsStateWithLifecycle()")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ViewModel은 Activity/Fragment 수명주기에 바인딩되어\n" +
                            "화면 회전에도 상태가 유지됩니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 카운터 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ViewModel 카운터",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(onClick = { viewModel.decrement() }) {
                        Text("-1")
                    }
                    Button(onClick = { viewModel.increment() }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+1")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { viewModel.reset() }) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("초기화")
                }
            }
        }

        // 비교 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "화면을 회전해보세요!",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Problem 탭의 카운터와 달리 값이 유지됩니다.\n" +
                            "ViewModel은 Activity 재생성 시에도 살아남습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 데모 2: 폼 유효성 검사
 * 비즈니스 로직이 ViewModel에 분리됨
 */
@Composable
fun FormDemo(viewModel: FormViewModel = viewModel()) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val nameError by viewModel.nameError.collectAsStateWithLifecycle()
    val emailError by viewModel.emailError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "비즈니스 로직 분리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "유효성 검사 로직이 ViewModel에 있어서:\n" +
                            "- JUnit으로 쉽게 테스트 가능\n" +
                            "- 다른 화면에서 재사용 가능\n" +
                            "- UI와 로직이 명확히 분리됨",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 폼 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "회원가입 폼 (ViewModel 사용)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.updateName(it) },  // ViewModel에 위임
                    label = { Text("이름") },
                    isError = nameError != null,
                    supportingText = {
                        nameError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.updateEmail(it) },  // ViewModel에 위임
                    label = { Text("이메일") },
                    isError = emailError != null,
                    supportingText = {
                        emailError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { /* 가입 처리 */ },
                    enabled = viewModel.isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("가입하기")
                }

                // 장점 표시
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFC8E6C9)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "이 코드의 장점:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "- validateName(), validateEmail() 순수 함수 테스트 가능\n" +
                                    "- 화면 회전 시 입력값 유지\n" +
                                    "- UI는 상태만 표시, 로직은 ViewModel에서 처리",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

/**
 * 데모 3: 비동기 데이터 로딩
 * sealed class로 Loading/Success/Error 상태 관리
 */
@Composable
fun AsyncDataDemo(viewModel: DataViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loadCount by viewModel.loadCount.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "sealed class로 UI 상태 관리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Loading, Success, Error 상태를 명시적으로 표현하여\n" +
                            "when으로 UI를 분기합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 로드 횟수 표시
        Text(
            text = "총 로드 시도: $loadCount 회",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        // 상태에 따른 UI
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val state = uiState) {
                    is DataUiState.Loading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("데이터 로딩 중...")
                    }

                    is DataUiState.Success -> {
                        Text(
                            text = "성공!",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        state.data.forEach { item ->
                            Text(
                                text = "- $item",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    is DataUiState.Error -> {
                        Text(
                            text = "오류 발생",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.loadData() },
                    enabled = uiState !is DataUiState.Loading
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("다시 로드")
                }
            }
        }

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "sealed class 패턴",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
sealed class DataUiState {
    object Loading : DataUiState()
    data class Success(val data: List<String>) : DataUiState()
    data class Error(val message: String) : DataUiState()
}

// UI에서:
when (val state = uiState) {
    is DataUiState.Loading -> CircularProgressIndicator()
    is DataUiState.Success -> ShowData(state.data)
    is DataUiState.Error -> ShowError(state.message)
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
