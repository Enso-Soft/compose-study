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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Practice: Hilt + ViewModel 연습 문제
 *
 * 각 연습 문제에서 TODO를 완성하세요.
 * 힌트와 정답이 제공됩니다.
 */

// ========================================
// 연습 1: 기본 @HiltViewModel 구현
// ========================================

/**
 * 연습 문제 1: TodoViewModel 만들기
 *
 * 목표: @HiltViewModel을 사용하여 TodoRepository를 주입받는 ViewModel 구현
 *
 * TODO:
 * 1. @HiltViewModel 어노테이션 추가
 * 2. @Inject constructor로 TodoRepository 주입받기
 * 3. viewModelScope.launch로 할 일 목록 로드
 */

// Repository 인터페이스
interface TodoRepository {
    suspend fun getTodos(): List<Todo>
    suspend fun addTodo(title: String)
}

data class Todo(val id: Int, val title: String, val completed: Boolean)

// Repository 구현체 (이미 제공됨)
class TodoRepositoryImpl @Inject constructor() : TodoRepository {
    private val todos = mutableListOf(
        Todo(1, "Hilt 배우기", false),
        Todo(2, "ViewModel 연습", false),
        Todo(3, "Compose UI 만들기", true)
    )

    override suspend fun getTodos(): List<Todo> {
        delay(500)
        return todos.toList()
    }

    override suspend fun addTodo(title: String) {
        delay(300)
        todos.add(Todo(todos.size + 1, title, false))
    }
}

// TODO: 아래 ViewModel을 완성하세요!
// 힌트: @HiltViewModel과 @Inject constructor 사용

// 연습용 ViewModel (미완성 버전)
/*
class Practice1_TodoViewModel(
    private val todoRepository: TodoRepository  // 어떻게 주입받을까요?
) : ViewModel() {
    // ...
}
*/

// 정답 (주석 해제하여 확인)
@HiltViewModel
class Practice1_TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTodos() {
        viewModelScope.launch {
            _isLoading.value = true
            _todos.value = todoRepository.getTodos()
            _isLoading.value = false
        }
    }

    fun addTodo(title: String) {
        viewModelScope.launch {
            todoRepository.addTodo(title)
            loadTodos()
        }
    }
}

@Composable
fun Practice1_BasicHiltViewModel() {
    val viewModel: Practice1_TodoViewModel = hiltViewModel()
    // collectAsStateWithLifecycle(): 앱이 백그라운드일 때 collection 자동 중지 (권장)
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var newTodoText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 @HiltViewModel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TodoRepository를 주입받는 @HiltViewModel을 구현하세요.\n" +
                            "할 일 목록을 로드하고 새 할 일을 추가할 수 있어야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "힌트", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    // StateFlow로 상태 관리
    // viewModelScope.launch로 비동기 작업
}
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 인터랙션 영역
        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                placeholder = { Text("새 할 일") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Button(
                onClick = {
                    if (newTodoText.isNotBlank()) {
                        viewModel.addTodo(newTodoText)
                        newTodoText = ""
                    }
                }
            ) {
                Text("추가")
            }
        }

        Button(
            onClick = { viewModel.loadTodos() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("할 일 목록 로드")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        todos.forEach { todo ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (todo.completed)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.completed,
                        onCheckedChange = null
                    )
                    Text(
                        text = todo.title,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

// ========================================
// 연습 2: SavedStateHandle 활용
// ========================================

/**
 * 연습 문제 2: SavedStateHandle로 상품 ID 받기
 *
 * 목표: SavedStateHandle에서 Navigation 인자를 추출하여 상품 로드
 *
 * TODO:
 * 1. SavedStateHandle을 생성자에 주입받기
 * 2. savedStateHandle["productId"]로 인자 추출
 * 3. 추출한 ID로 상품 데이터 로드
 */

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val description: String
)

// TODO: 아래 ViewModel을 완성하세요!
// 힌트: SavedStateHandle에서 productId를 추출

// 정답
@HiltViewModel
class Practice2_ProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // SavedStateHandle에서 productId 추출
    // Navigation에서 "product/{productId}"로 전달되면 자동으로 들어옴
    private val productId: String = savedStateHandle.get<String>("productId") ?: "prod_001"

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 상품 데이터 (시뮬레이션)
    private val products = mapOf(
        "prod_001" to Product("prod_001", "Kotlin 입문서", 25000, "코틀린 기초를 배우는 책"),
        "prod_002" to Product("prod_002", "Android 개발 가이드", 35000, "안드로이드 앱 개발 완전 정복"),
        "prod_003" to Product("prod_003", "Compose UI 마스터", 42000, "Jetpack Compose로 UI 만들기")
    )

    fun loadProduct() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(800) // 네트워크 시뮬레이션
            _product.value = products[productId]
            _isLoading.value = false
        }
    }

    fun changeProduct(newId: String) {
        // SavedStateHandle 값 업데이트
        savedStateHandle["productId"] = newId
        loadProduct()
    }

    fun getCurrentProductId(): String = productId
}

@Composable
fun Practice2_SavedStateHandle() {
    val viewModel: Practice2_ProductViewModel = hiltViewModel()
    // collectAsStateWithLifecycle(): lifecycle-aware collection (권장)
    val product by viewModel.product.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var selectedProductId by remember { mutableStateOf(viewModel.getCurrentProductId()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: SavedStateHandle 활용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SavedStateHandle에서 productId를 추출하여 상품 정보를 로드하세요.\n" +
                            "Navigation Compose에서 자동으로 인자가 주입됩니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "힌트", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Navigation 인자 추출
    val productId: String = checkNotNull(
        savedStateHandle["productId"]
    )
    // 또는 기본값 제공
    val productId = savedStateHandle.get<String>("productId")
        ?: "default"
}
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 상품 선택
        HorizontalDivider()

        Text(
            text = "상품 선택 (Navigation 시뮬레이션)",
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("prod_001", "prod_002", "prod_003").forEach { id ->
                FilterChip(
                    selected = selectedProductId == id,
                    onClick = {
                        selectedProductId = id
                        viewModel.changeProduct(id)
                    },
                    label = { Text(id.takeLast(3)) }
                )
            }
        }

        Button(
            onClick = { viewModel.loadProduct() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("상품 로드")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        product?.let { p ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = p.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ID: ${p.id}")
                    Text(text = "가격: ${p.price}원")
                    Text(text = p.description)
                }
            }
        }
    }
}

// ========================================
// 연습 3: NavGraph Scope 활용
// ========================================

/**
 * 연습 문제 3: 여러 화면에서 ViewModel 공유
 *
 * 목표: 주문 플로우(배송지 → 결제 → 확인)에서 OrderViewModel 공유
 *
 * TODO:
 * 1. NavGraph 수준으로 ViewModel scope 설정
 * 2. 여러 화면에서 동일한 ViewModel 인스턴스 사용
 * 3. 화면 간 데이터 공유 확인
 */

data class OrderData(
    val address: String = "",
    val paymentMethod: String = "",
    val items: List<String> = listOf("Kotlin 입문서", "Android 가이드"),
    val totalPrice: Int = 60000
)

@HiltViewModel
class Practice3_OrderViewModel @Inject constructor() : ViewModel() {

    private val _orderData = MutableStateFlow(OrderData())
    val orderData: StateFlow<OrderData> = _orderData.asStateFlow()

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    // 인스턴스 ID (공유 확인용)
    val instanceId = System.identityHashCode(this)

    fun updateAddress(address: String) {
        _orderData.value = _orderData.value.copy(address = address)
    }

    fun updatePaymentMethod(method: String) {
        _orderData.value = _orderData.value.copy(paymentMethod = method)
    }

    fun nextStep() {
        if (_currentStep.value < 2) {
            _currentStep.value++
        }
    }

    fun prevStep() {
        if (_currentStep.value > 0) {
            _currentStep.value--
        }
    }

    fun reset() {
        _orderData.value = OrderData()
        _currentStep.value = 0
    }
}

@Composable
fun Practice3_NavGraphScope() {
    // 실제 앱에서는 NavGraph의 backStackEntry를 사용
    // 여기서는 데모를 위해 일반 hiltViewModel() 사용
    val sharedViewModel: Practice3_OrderViewModel = hiltViewModel()
    // collectAsStateWithLifecycle(): lifecycle-aware collection (권장)
    val orderData by sharedViewModel.orderData.collectAsStateWithLifecycle()
    val currentStep by sharedViewModel.currentStep.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: NavGraph Scope",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "주문 플로우에서 여러 화면이 동일한 ViewModel을 공유합니다.\n" +
                            "배송지 → 결제 → 확인 단계를 거치면서 데이터가 유지되는지 확인하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "힌트: NavGraph Scope 사용법", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// NavGraph에 scope된 ViewModel 사용
@Composable
fun CheckoutFlow(navController: NavHostController) {
    // 부모 NavGraph의 backStackEntry 획득
    val parentEntry = remember(navController) {
        navController.getBackStackEntry("checkout_graph")
    }

    // 해당 entry로 ViewModel scope
    val sharedViewModel: OrderViewModel = hiltViewModel(
        viewModelStoreOwner = parentEntry
    )

    // 모든 하위 화면에서 동일한 인스턴스 사용!
}
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        HorizontalDivider()

        // ViewModel 인스턴스 ID 표시
        Text(
            text = "ViewModel 인스턴스 ID: ${sharedViewModel.instanceId}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        // Step Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("배송지", "결제", "확인").forEachIndexed { index, title ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (currentStep >= index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${index + 1}",
                                color = if (currentStep >= index)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        HorizontalDivider()

        // 현재 단계에 따른 화면
        when (currentStep) {
            0 -> AddressStep(sharedViewModel, orderData)
            1 -> PaymentStep(sharedViewModel, orderData)
            2 -> ConfirmStep(sharedViewModel, orderData)
        }

        // Navigation 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (currentStep > 0) {
                OutlinedButton(
                    onClick = { sharedViewModel.prevStep() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("이전")
                }
            }
            if (currentStep < 2) {
                Button(
                    onClick = { sharedViewModel.nextStep() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("다음")
                }
            } else {
                Button(
                    onClick = { sharedViewModel.reset() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("처음으로")
                }
            }
        }
    }
}

@Composable
private fun AddressStep(viewModel: Practice3_OrderViewModel, orderData: OrderData) {
    var address by remember { mutableStateOf(orderData.address) }

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "1단계: 배송지 입력",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    viewModel.updateAddress(it)
                },
                label = { Text("배송 주소") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "* 이 데이터는 다음 단계에서도 유지됩니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun PaymentStep(viewModel: Practice3_OrderViewModel, orderData: OrderData) {
    var selectedMethod by remember { mutableStateOf(orderData.paymentMethod) }

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "2단계: 결제 방법",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (orderData.address.isNotBlank()) {
                Text(
                    text = "배송지: ${orderData.address}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            listOf("신용카드", "계좌이체", "간편결제").forEach { method ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedMethod == method,
                        onClick = {
                            selectedMethod = method
                            viewModel.updatePaymentMethod(method)
                        }
                    )
                    Text(text = method)
                }
            }
        }
    }
}

@Composable
private fun ConfirmStep(viewModel: Practice3_OrderViewModel, orderData: OrderData) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "3단계: 주문 확인",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "주문 상품:", fontWeight = FontWeight.Bold)
            orderData.items.forEach { item ->
                Text(text = "  - $item")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "배송지: ${orderData.address.ifBlank { "(미입력)" }}")
            Text(text = "결제 방법: ${orderData.paymentMethod.ifBlank { "(미선택)" }}")

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "총 금액: ${orderData.totalPrice}원",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "같은 ViewModel 인스턴스를 공유하기 때문에 " +
                        "모든 단계에서 입력한 데이터가 유지됩니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
