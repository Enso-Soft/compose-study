package com.example.view_model

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Practice: ViewModel 연습 문제
 *
 * 연습 1: 간단한 카운터 ViewModel 구현
 * 연습 2: 입력 폼 유효성 검사
 * 연습 3: 로딩 상태 UI 구현
 */

// ==================== 연습 1: 카운터 ViewModel ====================

/**
 * 연습 1 ViewModel
 *
 * TODO: MutableStateFlow로 카운터 상태 관리
 * - _count: 내부에서만 수정 가능한 MutableStateFlow
 * - count: 외부로 노출되는 읽기 전용 StateFlow
 * - increment(): 카운터 1 증가
 * - decrement(): 카운터 1 감소
 */
class Practice1ViewModel : ViewModel() {
    // 힌트: private val _count = MutableStateFlow(0)

    // === 정답 ===
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() {
        _count.value++
    }

    fun decrement() {
        _count.value--
    }
}

@Composable
fun Practice1_CounterViewModel() {
    // TODO: viewModel() 함수로 ViewModel 가져오기
    // 힌트: val viewModel: Practice1ViewModel = viewModel()

    // === 정답 ===
    val viewModel: Practice1ViewModel = viewModel()
    val count by viewModel.count.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 카운터 ViewModel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("MutableStateFlow로 카운터 상태를 관리하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트:\n" +
                            "1. private val _count = MutableStateFlow(0)\n" +
                            "2. val count: StateFlow<Int> = _count.asStateFlow()\n" +
                            "3. viewModel() 함수로 ViewModel 획득\n" +
                            "4. collectAsState()로 Flow를 State로 변환",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
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
                    text = "카운터",
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
                    FilledTonalButton(
                        onClick = {
                            // TODO: viewModel.decrement() 호출
                            viewModel.decrement()
                        }
                    ) {
                        Text("-1")
                    }
                    Button(
                        onClick = {
                            // TODO: viewModel.increment() 호출
                            viewModel.increment()
                        }
                    ) {
                        Text("+1")
                    }
                }
            }
        }

        // 확인 포인트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "확인해보세요",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. 카운터가 증가/감소하나요?\n" +
                            "2. 화면을 회전해도 값이 유지되나요?",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ==================== 연습 2: 입력 폼 유효성 검사 ====================

/**
 * 연습 2 ViewModel
 *
 * TODO: 이름 입력 유효성 검사
 * - 이름이 3자 이상이면 유효
 * - 에러 메시지를 StateFlow로 노출
 */
class Practice2ViewModel : ViewModel() {
    // 힌트: private val _name = MutableStateFlow("")

    // === 정답 ===
    private val _name = MutableStateFlow("")
    private val _nameError = MutableStateFlow<String?>(null)

    val name: StateFlow<String> = _name.asStateFlow()
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    fun updateName(value: String) {
        _name.value = value
        // 유효성 검사
        _nameError.value = when {
            value.isEmpty() -> null  // 아직 입력 안 함
            value.length < 3 -> "이름은 3자 이상이어야 합니다"
            else -> null
        }
    }

    val isValid: Boolean
        get() = _name.value.length >= 3 && _nameError.value == null
}

@Composable
fun Practice2_FormValidation() {
    // TODO: viewModel() 함수로 ViewModel 가져오기

    // === 정답 ===
    val viewModel: Practice2ViewModel = viewModel()
    val name by viewModel.name.collectAsState()
    val nameError by viewModel.nameError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 입력 폼 유효성 검사",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("이름이 3자 이상인지 검증하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트:\n" +
                            "1. _name과 _nameError 두 개의 MutableStateFlow 필요\n" +
                            "2. updateName()에서 유효성 검사 수행\n" +
                            "3. isValid 프로퍼티로 버튼 활성화 제어",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 폼 카드
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        // TODO: viewModel.updateName(it) 호출
                        viewModel.updateName(it)
                    },
                    label = { Text("이름") },
                    isError = nameError != null,
                    supportingText = {
                        nameError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        } ?: if (name.length >= 3) {
                            Text("유효한 이름입니다!", color = Color(0xFF2E7D32))
                        } else null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { /* 제출 처리 */ },
                    enabled = viewModel.isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("제출하기")
                }

                // 상태 표시
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "현재 상태",
                            fontWeight = FontWeight.Bold
                        )
                        Text("이름: \"$name\"")
                        Text("글자 수: ${name.length}")
                        Text("에러: ${nameError ?: "없음"}")
                        Text("유효: ${viewModel.isValid}")
                    }
                }
            }
        }
    }
}

// ==================== 연습 3: 로딩 상태 UI ====================

/**
 * 연습 3 UI 상태
 *
 * TODO: sealed class로 Loading/Success/Error 정의
 */
sealed class Practice3UiState {
    // 힌트: object Loading, data class Success(val items: List<String>), data class Error(val message: String)

    // === 정답 ===
    data object Loading : Practice3UiState()
    data class Success(val items: List<String>) : Practice3UiState()
    data class Error(val message: String) : Practice3UiState()
}

/**
 * 연습 3 ViewModel
 *
 * TODO: viewModelScope로 비동기 작업 수행
 */
class Practice3ViewModel : ViewModel() {
    // 힌트: private val _uiState = MutableStateFlow<Practice3UiState>(Practice3UiState.Loading)

    // === 정답 ===
    private val _uiState = MutableStateFlow<Practice3UiState>(Practice3UiState.Loading)
    val uiState: StateFlow<Practice3UiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = Practice3UiState.Loading
            delay(2000)  // 네트워크 요청 시뮬레이션

            // 50% 확률로 성공/실패
            if ((0..1).random() == 0) {
                _uiState.value = Practice3UiState.Success(
                    listOf(
                        "Apple",
                        "Banana",
                        "Cherry",
                        "Date",
                        "Elderberry"
                    )
                )
            } else {
                _uiState.value = Practice3UiState.Error("데이터를 불러오는데 실패했습니다")
            }
        }
    }
}

@Composable
fun Practice3_LoadingState() {
    // TODO: viewModel()과 collectAsState() 사용

    // === 정답 ===
    val viewModel: Practice3ViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 로딩 상태 UI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("sealed class로 Loading/Success/Error 상태를 관리하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트:\n" +
                            "1. sealed class Practice3UiState 정의\n" +
                            "2. when으로 각 상태에 맞는 UI 표시\n" +
                            "3. viewModelScope.launch로 비동기 작업",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 상태에 따른 UI
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TODO: when으로 uiState에 따라 다른 UI 표시

                // === 정답 ===
                when (val state = uiState) {
                    is Practice3UiState.Loading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("데이터 로딩 중...")
                    }

                    is Practice3UiState.Success -> {
                        Text(
                            text = "로드 성공!",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                state.items.forEachIndexed { index, item ->
                                    Text("${index + 1}. $item")
                                }
                            }
                        }
                    }

                    is Practice3UiState.Error -> {
                        Text(
                            text = "오류 발생!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.loadData() },
                    enabled = uiState !is Practice3UiState.Loading
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("다시 로드")
                }
            }
        }

        // sealed class 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "sealed class 장점",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "- when에서 모든 상태를 강제로 처리\n" +
                            "- 각 상태에 필요한 데이터만 포함\n" +
                            "- 타입 안전성 보장",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ==================== Practice Navigator ====================

@Composable
fun PracticeScreen() {
    var selectedPractice by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedPractice == 0,
                onClick = { selectedPractice = 0 },
                label = { Text("카운터") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("폼 검증") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("로딩") }
            )
        }

        when (selectedPractice) {
            0 -> Practice1_CounterViewModel()
            1 -> Practice2_FormValidation()
            2 -> Practice3_LoadingState()
        }
    }
}
