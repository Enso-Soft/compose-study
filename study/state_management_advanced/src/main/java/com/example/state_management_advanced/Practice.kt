package com.example.state_management_advanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Practice: 상태 관리 심화 연습 문제
 *
 * 연습 1: StateFlow와 Channel 분리하기 (기초)
 * 연습 2: collectAsStateWithLifecycle 적용 (중급)
 * 연습 3: MVI 패턴 구현 (심화)
 */

// ==================== 연습 문제 1: StateFlow와 Channel 분리 ====================

/**
 * 연습 1: 잘못된 코드를 수정하여 상태와 이벤트를 분리하세요
 *
 * 현재 문제:
 * - successMessage가 StateFlow 상태에 포함되어 있음
 * - 화면 회전 시 Snackbar가 다시 표시됨
 *
 * 수정 요구사항:
 * 1. successMessage를 Channel로 분리
 * 2. ViewModel에서 Channel 생성
 * 3. Composable에서 LaunchedEffect로 Channel 수집
 */

// 잘못된 UiState (수정 필요)
data class TodoUiState(
    val todos: List<TodoItem> = emptyList(),
    val isLoading: Boolean = false,
    val successMessage: String? = null  // TODO: 이 필드를 제거하고 Channel로 분리
)

data class TodoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)

// TODO: Channel 이벤트 sealed class 정의
// sealed class TodoEvent {
//     data class ShowMessage(val message: String) : TodoEvent()
// }

class Practice1ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    // TODO: Channel 추가
    // private val _events = Channel<TodoEvent>(Channel.BUFFERED)
    // val events = _events.receiveAsFlow()

    private var nextId = 1

    fun addTodo(title: String) {
        if (title.isBlank()) return

        val newTodo = TodoItem(
            id = nextId++,
            title = title
        )

        _uiState.update {
            it.copy(
                todos = it.todos + newTodo,
                // TODO: 아래 줄을 제거하고 Channel로 이벤트 전송
                successMessage = "할 일이 추가되었습니다"
            )
        }

        // TODO: Channel로 이벤트 전송
        // viewModelScope.launch {
        //     _events.send(TodoEvent.ShowMessage("할 일이 추가되었습니다"))
        // }
    }

    fun toggleTodo(todoId: Int) {
        _uiState.update { state ->
            state.copy(
                todos = state.todos.map { todo ->
                    if (todo.id == todoId) todo.copy(isCompleted = !todo.isCompleted)
                    else todo
                }
            )
        }
    }

    fun deleteTodo(todoId: Int) {
        _uiState.update { state ->
            state.copy(
                todos = state.todos.filter { it.id != todoId },
                // TODO: 아래 줄을 제거하고 Channel로 이벤트 전송
                successMessage = "할 일이 삭제되었습니다"
            )
        }
    }

    // TODO: 이 함수 제거 (Channel 사용 시 불필요)
    fun clearMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}

@Composable
fun Practice1_TodoScreen(viewModel: Practice1ViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var inputText by remember { mutableStateOf("") }

    // TODO: 현재 잘못된 패턴 (StateFlow에서 이벤트 감지)
    // 아래 코드를 LaunchedEffect(Unit) { viewModel.events.collect { } }로 변경
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()  // 수동 초기화 필요 (Channel 사용 시 불필요)
        }
    }

    // TODO: 올바른 패턴으로 변경
    // LaunchedEffect(Unit) {
    //     viewModel.events.collect { event ->
    //         when (event) {
    //             is TodoEvent.ShowMessage -> {
    //                 snackbarHostState.showSnackbar(event.message)
    //             }
    //         }
    //     }
    // }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 힌트 카드
            PracticeHintCard(
                title = "연습 1: StateFlow와 Channel 분리",
                hints = listOf(
                    "1. sealed class TodoEvent 정의",
                    "2. Channel<TodoEvent>(Channel.BUFFERED) 생성",
                    "3. receiveAsFlow()로 Flow 변환",
                    "4. LaunchedEffect(Unit) { events.collect { } }"
                )
            )

            // 입력 필드
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("새 할 일") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.addTodo(inputText)
                        inputText = ""
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }

            // 할 일 목록
            uiState.todos.forEach { todo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (todo.isCompleted)
                            MaterialTheme.colorScheme.surfaceVariant
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = todo.isCompleted,
                                onCheckedChange = { viewModel.toggleTodo(todo.id) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = todo.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        IconButton(onClick = { viewModel.deleteTodo(todo.id) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "삭제",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            if (uiState.todos.isEmpty()) {
                Text(
                    text = "할 일을 추가해보세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ==================== 연습 문제 2: collectAsStateWithLifecycle 적용 ====================

/**
 * 연습 2: collectAsState를 collectAsStateWithLifecycle로 변경하고
 * WhileSubscribed(5000) 패턴을 적용하세요
 *
 * 수정 요구사항:
 * 1. SharingStarted.Eagerly를 WhileSubscribed(5000)으로 변경
 * 2. collectAsState()를 collectAsStateWithLifecycle()로 변경
 * 3. 필요한 import 추가
 */

data class TimerState(
    val seconds: Int = 0,
    val isRunning: Boolean = false
)

class Practice2ViewModel : ViewModel() {
    private val timerFlow = flow {
        var seconds = 0
        while (true) {
            emit(seconds)
            delay(1000)
            seconds++
        }
    }

    // TODO: SharingStarted.Eagerly를 WhileSubscribed(5000)으로 변경
    val timerState: StateFlow<TimerState> = timerFlow
        .map { seconds -> TimerState(seconds = seconds, isRunning = true) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,  // TODO: WhileSubscribed(5_000)으로 변경
            initialValue = TimerState()
        )

    // 구독 상태 추적 (데모용) - 단순화
    private val _isSubscribed = MutableStateFlow(true)
    val isSubscribed: StateFlow<Boolean> = _isSubscribed.asStateFlow()
}

@Composable
fun Practice2_TimerScreen(viewModel: Practice2ViewModel = viewModel()) {
    // TODO: collectAsState()를 collectAsStateWithLifecycle()로 변경
    val timerState by viewModel.timerState.collectAsState()  // TODO: collectAsStateWithLifecycle()
    val isSubscribed by viewModel.isSubscribed.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 힌트 카드
        PracticeHintCard(
            title = "연습 2: Lifecycle-aware 수집",
            hints = listOf(
                "1. import androidx.lifecycle.compose.collectAsStateWithLifecycle",
                "2. collectAsState() -> collectAsStateWithLifecycle()",
                "3. SharingStarted.WhileSubscribed(5_000)",
                "4. 백그라운드에서 Flow 자동 중단 확인"
            )
        )

        // 타이머 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${timerState.seconds}초",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (timerState.isRunning) "실행 중" else "정지",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 구독 상태
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isSubscribed)
                    Color(0xFFC8E6C9)
                else
                    Color(0xFFFFCDD2)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isSubscribed) "Flow 구독 중" else "Flow 미구독",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isSubscribed)
                        "타이머가 동작하고 있습니다"
                    else
                        "WhileSubscribed로 인해 타이머가 중단됩니다",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 테스트 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "테스트 방법",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "수정 전:\n" +
                            "- 백그라운드에서도 타이머 계속 동작\n\n" +
                            "수정 후:\n" +
                            "- 백그라운드로 가면 5초 후 타이머 중단\n" +
                            "- 다시 돌아오면 즉시 재시작",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 현재 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 코드 (수정 필요)",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// ViewModel
.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = TimerState()
)

// Composable
val timerState by viewModel.timerState.collectAsState()
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

// ==================== 연습 문제 3: MVI 패턴 구현 ====================

/**
 * 연습 3: 완전한 MVI 패턴을 구현하세요
 *
 * 요구사항:
 * 1. CounterState data class 정의 (count, isLoading)
 * 2. CounterIntent sealed class 정의 (Increment, Decrement, Reset)
 * 3. CounterEvent sealed class 정의 (ShowMessage)
 * 4. ViewModel에서 Intent 처리 및 Event 발생
 * 5. Composable에서 State 수집 및 Event 처리
 */

// TODO: State 정의
data class CounterState(
    val count: Int = 0,
    val isLoading: Boolean = false
)

// TODO: Intent 정의 (사용자 의도)
sealed class CounterIntent {
    data object Increment : CounterIntent()
    data object Decrement : CounterIntent()
    data object Reset : CounterIntent()
    data class SetValue(val value: Int) : CounterIntent()
}

// TODO: Event 정의 (일회성 이벤트)
sealed class CounterEvent {
    data class ShowMessage(val message: String) : CounterEvent()
}

class Practice3ViewModel : ViewModel() {
    // TODO: State - StateFlow
    private val _state = MutableStateFlow(CounterState())
    val state: StateFlow<CounterState> = _state.asStateFlow()

    // TODO: Events - Channel
    private val _events = Channel<CounterEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // TODO: Intent 처리 함수 구현
    fun onIntent(intent: CounterIntent) {
        when (intent) {
            is CounterIntent.Increment -> {
                _state.update { it.copy(count = it.count + 1) }
            }
            is CounterIntent.Decrement -> {
                _state.update { it.copy(count = it.count - 1) }
            }
            is CounterIntent.Reset -> {
                _state.update { it.copy(count = 0) }
                // TODO: 이벤트 전송
                viewModelScope.launch {
                    _events.send(CounterEvent.ShowMessage("카운터가 초기화되었습니다"))
                }
            }
            is CounterIntent.SetValue -> {
                _state.update { it.copy(count = intent.value) }
                viewModelScope.launch {
                    _events.send(CounterEvent.ShowMessage("값이 ${intent.value}로 설정되었습니다"))
                }
            }
        }
    }

    // 비동기 작업 예시
    fun loadRemoteCount() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(1500) // API 호출 시뮬레이션
            val remoteCount = (1..100).random()
            _state.update { it.copy(count = remoteCount, isLoading = false) }
            _events.send(CounterEvent.ShowMessage("서버에서 값을 불러왔습니다: $remoteCount"))
        }
    }
}

@Composable
fun Practice3_MVIScreen(viewModel: Practice3ViewModel = viewModel()) {
    // TODO: State 수집
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // TODO: Event 수집
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CounterEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 힌트 카드
            PracticeHintCard(
                title = "연습 3: MVI 패턴 구현",
                hints = listOf(
                    "1. State: data class (불변 상태)",
                    "2. Intent: sealed class (사용자 의도)",
                    "3. Event: sealed class (일회성 효과)",
                    "4. ViewModel.onIntent(intent) 패턴"
                )
            )

            // MVI 다이어그램
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "MVI 패턴 흐름",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
View ──Intent──> ViewModel
  ^                  │
  │                  │
  └──State/Event────┘
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 카운터 표시
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("로딩 중...")
                    } else {
                        Text(
                            text = "${state.count}",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Intent 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // TODO: Intent 전송
                Button(
                    onClick = { viewModel.onIntent(CounterIntent.Decrement) },
                    enabled = !state.isLoading
                ) {
                    Text("-1")
                }

                Button(
                    onClick = { viewModel.onIntent(CounterIntent.Increment) },
                    enabled = !state.isLoading
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+1")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { viewModel.onIntent(CounterIntent.Reset) },
                    enabled = !state.isLoading
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("초기화")
                }

                FilledTonalButton(
                    onClick = { viewModel.loadRemoteCount() },
                    enabled = !state.isLoading
                ) {
                    Text("서버에서 불러오기")
                }
            }

            // 코드 구조 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFC8E6C9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "MVI 코드 구조",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
// State
data class CounterState(
    val count: Int = 0,
    val isLoading: Boolean = false
)

// Intent
sealed class CounterIntent {
    object Increment : CounterIntent()
    object Decrement : CounterIntent()
    object Reset : CounterIntent()
}

// Event
sealed class CounterEvent {
    data class ShowMessage(val message: String) : CounterEvent()
}

// ViewModel
fun onIntent(intent: CounterIntent) {
    when (intent) {
        is Increment -> _state.update { ... }
        is Reset -> {
            _state.update { ... }
            _events.send(ShowMessage(...))
        }
    }
}

// Composable
Button(onClick = { viewModel.onIntent(Increment) })
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

// ==================== 공통 컴포넌트 ====================

@Composable
fun PracticeHintCard(
    title: String,
    hints: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            hints.forEach { hint ->
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/*
// ==================== 정답 코드 ====================

// 연습 1 정답: Channel로 이벤트 분리

sealed class TodoEvent {
    data class ShowMessage(val message: String) : TodoEvent()
}

class Practice1ViewModelSolution : ViewModel() {
    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    private val _events = Channel<TodoEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var nextId = 1

    fun addTodo(title: String) {
        if (title.isBlank()) return

        val newTodo = TodoItem(
            id = nextId++,
            title = title
        )

        _uiState.update {
            it.copy(todos = it.todos + newTodo)
        }

        viewModelScope.launch {
            _events.send(TodoEvent.ShowMessage("할 일이 추가되었습니다"))
        }
    }

    fun deleteTodo(todoId: Int) {
        _uiState.update { state ->
            state.copy(todos = state.todos.filter { it.id != todoId })
        }
        viewModelScope.launch {
            _events.send(TodoEvent.ShowMessage("할 일이 삭제되었습니다"))
        }
    }
}

@Composable
fun Practice1Solution(viewModel: Practice1ViewModelSolution = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TodoEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    // ... UI 코드
}

// 연습 2 정답: WhileSubscribed + collectAsStateWithLifecycle

class Practice2ViewModelSolution : ViewModel() {
    private val timerFlow = flow {
        var seconds = 0
        while (true) {
            emit(seconds)
            delay(1000)
            seconds++
        }
    }

    val timerState: StateFlow<TimerState> = timerFlow
        .map { seconds -> TimerState(seconds = seconds, isRunning = true) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),  // 수정됨!
            initialValue = TimerState()
        )
}

@Composable
fun Practice2Solution(viewModel: Practice2ViewModelSolution = viewModel()) {
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()  // 수정됨!
    // ... UI 코드
}

// 연습 3은 이미 정답 형태로 작성되어 있음 (MVI 패턴 데모)

*/
