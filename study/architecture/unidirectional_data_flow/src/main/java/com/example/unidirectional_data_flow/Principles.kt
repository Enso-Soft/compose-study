package com.example.unidirectional_data_flow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 핵심 원칙 화면
 *
 * 단방향 데이터 흐름(UDF)의 핵심 원칙과 필요성을 학습합니다.
 */
@Composable
fun PrinciplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 왜 필요한가?
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 UDF가 필요한가?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "양방향 데이터 흐름은 다음과 같은 문제를 야기합니다:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "- 상태가 어디서 변경되었는지 추적 어려움\n" +
                            "- 동일한 버그를 재현하기 힘듦\n" +
                            "- UI와 로직이 섞여 단위 테스트 불가\n" +
                            "- 코드가 복잡해질수록 유지보수 어려움",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // UDF 다이어그램
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "UDF 데이터 흐름",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = """
User Event --> ViewModel --> State --> UI
     ^                                  |
     |__________________________________|
              (이벤트만 위로)
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "상태(State)는 아래로, 이벤트(Event)는 위로 흐릅니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 핵심 원칙들
        PrincipleCard(
            number = 1,
            title = "상태는 아래로, 이벤트는 위로 (State Down, Event Up)",
            description = "UI는 상태를 표시만 하고, 사용자 입력은 이벤트로 ViewModel에 전달합니다. " +
                    "ViewModel만 상태를 변경할 수 있습니다.",
            codeExample = """
// ViewModel - 상태 관리
class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun increment() { _count.value++ }
}

// UI - 상태 표시 + 이벤트 전달만!
@Composable
fun CounterScreen(
    count: Int,              // State Down
    onIncrement: () -> Unit  // Event Up
) {
    Text("Count: ${'$'}count")
    Button(onClick = onIncrement) { Text("+1") }
}
            """.trimIndent()
        ) {
            Principle1Demo()
        }

        PrincipleCard(
            number = 2,
            title = "단일 진실 공급원 (Single Source of Truth)",
            description = "상태는 한 곳(ViewModel의 StateFlow)에서만 관리합니다. " +
                    "여러 Composable이 같은 상태를 공유하더라도 원본은 하나입니다.",
            codeExample = """
// SSOT - ViewModel이 유일한 상태 소유자
class CartViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    // 파생 상태도 같은 원본에서 계산
    val totalCount: StateFlow<Int> = _items.map { it.size }
        .stateIn(viewModelScope, ...)
}

// 여러 Composable이 같은 상태 공유
@Composable
fun Screen(viewModel: CartViewModel) {
    val items by viewModel.items.collectAsStateWithLifecycle()

    CartList(items = items)    // 같은 상태
    CartBadge(count = items.size)  // 같은 상태에서 파생
}
            """.trimIndent()
        ) {
            Principle2Demo()
        }

        PrincipleCard(
            number = 3,
            title = "불변 상태 (Immutable State)",
            description = "상태는 data class로 정의하고, copy()로 새 상태를 생성합니다. " +
                    "직접 수정하지 않아 Recomposition이 최적화되고 디버깅이 쉬워집니다.",
            codeExample = """
// 불변 상태 정의
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

// 상태 변경 시 copy() 사용 - 새 객체 생성
fun onEmailChanged(email: String) {
    _state.update { currentState ->
        currentState.copy(email = email)
    }
}

// 이렇게 하면 안 됨!
// _state.value.email = email  // 컴파일 에러 (val)
            """.trimIndent()
        ) {
            Principle3Demo()
        }

        // 비유 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "비유: 교통 신호 시스템",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "- 버튼을 누르면(Event) 제어기(ViewModel)가 처리합니다\n" +
                            "- 제어기가 신호등 색상(State)을 변경합니다\n" +
                            "- 신호등(UI)은 색상만 표시합니다\n" +
                            "- 신호등이 직접 색상을 바꾸지 않습니다 (단방향!)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
private fun PrincipleCard(
    number: Int,
    title: String,
    description: String,
    codeExample: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "원칙 $number: $title",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = codeExample,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "실제 동작 데모:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 데모
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    demo()
                }
            }
        }
    }
}

// ==================== 원칙 1 데모: State Down, Event Up ====================

/**
 * 원칙 1 데모용 ViewModel
 */
class Principle1ViewModel : ViewModel() {
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
private fun Principle1Demo(viewModel: Principle1ViewModel = viewModel()) {
    // State Down: ViewModel에서 상태를 가져옴
    val count by viewModel.count.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "UDF 카운터",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$count",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Event Up: 버튼 클릭 시 ViewModel에 이벤트 전달
            FilledTonalButton(onClick = { viewModel.decrement() }) {
                Icon(Icons.Default.Remove, contentDescription = "감소")
            }
            Button(onClick = { viewModel.increment() }) {
                Icon(Icons.Default.Add, contentDescription = "증가")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "UI는 상태 표시 + 이벤트 전달만!",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

// ==================== 원칙 2 데모: SSOT ====================

/**
 * 원칙 2 데모용 ViewModel - 단일 진실 공급원
 */
class Principle2ViewModel : ViewModel() {
    private val _items = MutableStateFlow(listOf("상품 A", "상품 B"))
    val items: StateFlow<List<String>> = _items.asStateFlow()

    fun addItem() {
        val newItem = "상품 ${('A'.code + _items.value.size).toChar()}"
        _items.value = _items.value + newItem
    }

    fun removeItem() {
        if (_items.value.isNotEmpty()) {
            _items.value = _items.value.dropLast(1)
        }
    }
}

@Composable
private fun Principle2Demo(viewModel: Principle2ViewModel = viewModel()) {
    // 하나의 StateFlow를 여러 곳에서 사용
    val items by viewModel.items.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 컴포넌트 1: 아이템 개수 배지
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "장바구니: ${items.size}개",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 컴포넌트 2: 아이템 목록
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                if (items.isEmpty()) {
                    Text("장바구니가 비어있습니다", color = Color.Gray)
                } else {
                    items.forEach { item ->
                        Text("- $item")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { viewModel.removeItem() }) {
                Text("제거")
            }
            Button(onClick = { viewModel.addItem() }) {
                Text("추가")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "배지와 목록이 같은 상태를 공유합니다 (SSOT)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

// ==================== 원칙 3 데모: 불변 상태 ====================

/**
 * 원칙 3 데모용 불변 상태
 */
data class FormState(
    val name: String = "",
    val email: String = "",
    val updateCount: Int = 0  // 상태 변경 횟수 추적
)

class Principle3ViewModel : ViewModel() {
    private val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state.asStateFlow()

    fun updateName(name: String) {
        // copy()로 새 객체 생성 - 불변성 유지
        _state.value = _state.value.copy(
            name = name,
            updateCount = _state.value.updateCount + 1
        )
    }

    fun updateEmail(email: String) {
        _state.value = _state.value.copy(
            email = email,
            updateCount = _state.value.updateCount + 1
        )
    }
}

@Composable
private fun Principle3Demo(viewModel: Principle3ViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "상태 변경 횟수: ${state.updateCount}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "매번 copy()로 새 객체 생성됨",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
