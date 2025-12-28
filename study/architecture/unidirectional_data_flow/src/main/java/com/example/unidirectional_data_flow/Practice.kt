package com.example.unidirectional_data_flow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * 연습 문제 1: 기본 UDF 적용 (쉬움)
 *
 * remember로 구현된 카운터를 UDF 패턴(ViewModel + StateFlow)으로 리팩토링합니다.
 */
@Composable
fun Practice1_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 UDF 적용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래의 remember 기반 카운터를 UDF 패턴으로 리팩토링하세요.\n\n" +
                            "목표:\n" +
                            "1. CounterState data class 정의\n" +
                            "2. CounterEvent sealed interface 정의\n" +
                            "3. CounterViewModel 구현\n" +
                            "4. UI에서 state 수집 및 event 전달"
                )
            }
        }

        // 문제가 있는 코드 (remember 기반)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "리팩토링 대상 코드 (문제 있음):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = """
@Composable
fun BrokenCounter() {
    // 문제: remember만 사용 - 화면 회전 시 손실!
    var count by remember { mutableStateOf(0) }
    var step by remember { mutableStateOf(1) }

    Column {
        Text("Count: ${'$'}count (Step: ${'$'}step)")
        Row {
            Button(onClick = { count -= step }) { Text("-") }
            Button(onClick = { count += step }) { Text("+") }
        }
        Button(onClick = { step++ }) { Text("Step +1") }
        Button(onClick = { count = 0; step = 1 }) { Text("Reset") }
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- State는 data class로 정의 (count, step 포함)\n" +
                            "- Event는 sealed interface로 정의 (Increment, Decrement, IncreaseStep, Reset)\n" +
                            "- ViewModel에서 onEvent() 함수로 이벤트 처리\n" +
                            "- UI에서 collectAsStateWithLifecycle() 사용",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "정답 예시 (UDF 패턴 적용)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Practice1_Solution()
            }
        }
    }
}

// ==================== 연습 1 정답 ====================

data class CounterState(
    val count: Int = 0,
    val step: Int = 1
)

sealed interface CounterEvent {
    data object Increment : CounterEvent
    data object Decrement : CounterEvent
    data object IncreaseStep : CounterEvent
    data object Reset : CounterEvent
}

class CounterViewModel : ViewModel() {
    private val _state = MutableStateFlow(CounterState())
    val state: StateFlow<CounterState> = _state.asStateFlow()

    fun onEvent(event: CounterEvent) {
        when (event) {
            CounterEvent.Increment -> {
                _state.value = _state.value.copy(
                    count = _state.value.count + _state.value.step
                )
            }
            CounterEvent.Decrement -> {
                _state.value = _state.value.copy(
                    count = _state.value.count - _state.value.step
                )
            }
            CounterEvent.IncreaseStep -> {
                _state.value = _state.value.copy(
                    step = _state.value.step + 1
                )
            }
            CounterEvent.Reset -> {
                _state.value = CounterState()  // 초기 상태로
            }
        }
    }
}

@Composable
private fun Practice1_Solution(viewModel: CounterViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "${state.count}",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Step: ${state.step}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(
                onClick = { viewModel.onEvent(CounterEvent.Decrement) }
            ) {
                Icon(Icons.Default.Remove, contentDescription = "감소")
                Text(" ${state.step}")
            }
            Button(
                onClick = { viewModel.onEvent(CounterEvent.Increment) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "증가")
                Text(" ${state.step}")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { viewModel.onEvent(CounterEvent.IncreaseStep) }
            ) {
                Text("Step +1")
            }
            OutlinedButton(
                onClick = { viewModel.onEvent(CounterEvent.Reset) }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Text(" Reset")
            }
        }

        Text(
            text = "화면을 회전해도 상태가 유지됩니다!",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

/**
 * 연습 문제 2: 상태 공유 (중간)
 *
 * 장바구니와 상품 목록이 같은 상태를 공유하도록 UDF로 구현합니다.
 */
@Composable
fun Practice2_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 상태 공유 (SSOT)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "여러 Composable이 같은 장바구니 상태를 공유하도록 구현하세요.\n\n" +
                            "요구사항:\n" +
                            "- ProductList: 상품 목록 표시, 장바구니 추가 버튼\n" +
                            "- CartBadge: 장바구니 아이템 수 표시\n" +
                            "- CartSummary: 총 금액 표시\n" +
                            "- 모든 컴포넌트가 같은 상태를 공유 (SSOT)"
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- CartState: items (Map<Product, Int> - 상품과 수량)\n" +
                            "- CartEvent: AddItem, RemoveItem, ClearCart\n" +
                            "- 상위 Composable에서 state와 onEvent를 하위에 전달\n" +
                            "- ViewModel 인스턴스를 전달하지 않음 (안티패턴)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "정답 예시 (SSOT 적용)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Practice2_Solution()
            }
        }
    }
}

// ==================== 연습 2 정답 ====================

data class Product(
    val id: Int,
    val name: String,
    val price: Int
)

data class CartState(
    val items: Map<Product, Int> = emptyMap()  // 상품 -> 수량
) {
    val totalItems: Int get() = items.values.sum()
    val totalPrice: Int get() = items.entries.sumOf { (product, quantity) ->
        product.price * quantity
    }
}

sealed interface CartEvent {
    data class AddItem(val product: Product) : CartEvent
    data class RemoveItem(val product: Product) : CartEvent
    data object ClearCart : CartEvent
}

class CartViewModel : ViewModel() {
    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state.asStateFlow()

    val products = listOf(
        Product(1, "커피", 4500),
        Product(2, "케이크", 6000),
        Product(3, "샌드위치", 5500)
    )

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.AddItem -> {
                val currentItems = _state.value.items.toMutableMap()
                val currentQty = currentItems[event.product] ?: 0
                currentItems[event.product] = currentQty + 1
                _state.value = _state.value.copy(items = currentItems)
            }
            is CartEvent.RemoveItem -> {
                val currentItems = _state.value.items.toMutableMap()
                val currentQty = currentItems[event.product] ?: 0
                if (currentQty > 1) {
                    currentItems[event.product] = currentQty - 1
                } else {
                    currentItems.remove(event.product)
                }
                _state.value = _state.value.copy(items = currentItems)
            }
            CartEvent.ClearCart -> {
                _state.value = CartState()
            }
        }
    }
}

@Composable
private fun Practice2_Solution(viewModel: CartViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // 장바구니 배지 (상태 공유 1)
        CartBadge(totalItems = state.totalItems)

        // 상품 목록 (상태 공유 2)
        ProductList(
            products = viewModel.products,
            cartItems = state.items,
            onAddItem = { viewModel.onEvent(CartEvent.AddItem(it)) },
            onRemoveItem = { viewModel.onEvent(CartEvent.RemoveItem(it)) }
        )

        // 장바구니 요약 (상태 공유 3)
        CartSummary(
            totalItems = state.totalItems,
            totalPrice = state.totalPrice,
            onClearCart = { viewModel.onEvent(CartEvent.ClearCart) }
        )

        Text(
            text = "세 컴포넌트가 같은 상태를 공유합니다 (SSOT)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun CartBadge(totalItems: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "장바구니: ${totalItems}개",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProductList(
    products: List<Product>,
    cartItems: Map<Product, Int>,
    onAddItem: (Product) -> Unit,
    onRemoveItem: (Product) -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "상품 목록",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            products.forEach { product ->
                val quantity = cartItems[product] ?: 0
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(product.name)
                        Text(
                            text = "${product.price}원",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (quantity > 0) {
                            IconButton(onClick = { onRemoveItem(product) }) {
                                Icon(Icons.Default.Remove, contentDescription = "제거")
                            }
                            Text("$quantity", fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { onAddItem(product) }) {
                            Icon(Icons.Default.Add, contentDescription = "추가")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartSummary(
    totalItems: Int,
    totalPrice: Int,
    onClearCart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "총 ${totalItems}개",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${totalPrice}원",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            if (totalItems > 0) {
                OutlinedButton(onClick = onClearCart) {
                    Text("비우기")
                }
            }
        }
    }
}

/**
 * 연습 문제 3: 전체 MVI 구현 (어려움)
 *
 * 검색 기능을 완전한 MVI 패턴으로 구현합니다 (Event, State, Effect 모두 포함).
 */
@Composable
fun Practice3_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 전체 MVI 구현",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "검색 기능을 완전한 MVI 패턴으로 구현하세요.\n\n" +
                            "요구사항:\n" +
                            "- SearchState: query, isLoading, results, error\n" +
                            "- SearchEvent: QueryChanged, Search, ClearError\n" +
                            "- SearchEffect: ShowError (Snackbar)\n" +
                            "- 검색 API 호출 시뮬레이션 (delay 사용)\n" +
                            "- Channel로 Effect 처리\n" +
                            "- LaunchedEffect에서 Effect 수집"
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- Effect는 Channel 사용 (일회성 이벤트)\n" +
                            "- 빈 쿼리로 검색 시 에러 Effect 발생\n" +
                            "- LaunchedEffect(Unit)에서 effect.collect\n" +
                            "- when (effect)로 타입별 처리",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "정답 예시 (완전한 MVI)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Practice3_Solution()
            }
        }
    }
}

// ==================== 연습 3 정답 ====================

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<String> = emptyList(),
    val error: String? = null
)

sealed interface SearchEvent {
    data class QueryChanged(val query: String) : SearchEvent
    data object Search : SearchEvent
    data object ClearError : SearchEvent
    data object ClearResults : SearchEvent
}

sealed interface SearchEffect {
    data class ShowSnackbar(val message: String) : SearchEffect
}

class SearchViewModel : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = Channel<SearchEffect>()
    val effect = _effect.receiveAsFlow()

    // 검색 가능한 데이터 (시뮬레이션)
    private val database = listOf(
        "Kotlin", "Compose", "Android", "Jetpack",
        "ViewModel", "StateFlow", "MVI", "UDF",
        "Coroutines", "Flow", "Channel", "Navigation"
    )

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> {
                _state.value = _state.value.copy(
                    query = event.query,
                    error = null
                )
            }
            SearchEvent.Search -> search()
            SearchEvent.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }
            SearchEvent.ClearResults -> {
                _state.value = _state.value.copy(
                    query = "",
                    results = emptyList()
                )
            }
        }
    }

    private fun search() {
        val query = _state.value.query

        if (query.isBlank()) {
            viewModelScope.launch {
                _effect.send(SearchEffect.ShowSnackbar("검색어를 입력하세요"))
            }
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            // API 호출 시뮬레이션
            delay(1000)

            val results = database.filter {
                it.contains(query, ignoreCase = true)
            }

            if (results.isEmpty()) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    results = emptyList(),
                    error = "'$query'에 대한 결과가 없습니다"
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    results = results,
                    error = null
                )
            }
        }
    }
}

@Composable
private fun Practice3_Solution(viewModel: SearchViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect 처리
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Snackbar 표시
        SnackbarHost(hostState = snackbarHostState)

        // 검색 입력
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.onEvent(SearchEvent.QueryChanged(it)) },
                label = { Text("검색어") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onEvent(SearchEvent.ClearResults) }
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "지우기")
                        }
                    }
                }
            )
            Button(
                onClick = { viewModel.onEvent(SearchEvent.Search) },
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Search, contentDescription = "검색")
                }
            }
        }

        // 로딩 상태
        if (state.isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // 에러 메시지
        if (state.error != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = state.error!!,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 검색 결과
        if (state.results.isNotEmpty()) {
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "검색 결과 (${state.results.size}개)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    state.results.forEach { result ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(result)
                        }
                    }
                }
            }
        }

        // 현재 상태 표시 (디버깅용)
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
                    text = "query: \"${state.query}\"\n" +
                            "isLoading: ${state.isLoading}\n" +
                            "results: ${state.results.size}개\n" +
                            "error: ${state.error ?: "null"}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Text(
            text = "Event → ViewModel → State → UI + Effect",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
