package com.example.state_management_advanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
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
 * Solution: StateFlow + Channel을 사용한 올바른 상태/이벤트 분리
 *
 * 핵심:
 * 1. UI 상태는 StateFlow로 관리
 * 2. 일회성 이벤트는 Channel로 분리
 * 3. collectAsStateWithLifecycle 사용
 * 4. WhileSubscribed(5000) 패턴 적용
 */

// ==================== 올바른 상태/이벤트 정의 ====================

/**
 * UI 상태 (지속적인 데이터만 포함)
 * 일회성 이벤트는 여기에 포함하지 않음!
 */
data class GoodProductUiState(
    val products: List<Product> = sampleProducts,
    val isLoading: Boolean = false,
    val cartCount: Int = 0,
    val selectedProductId: Int? = null
)

/**
 * 일회성 이벤트 (sealed class로 타입 안전하게 정의)
 */
sealed class ProductEvent {
    data class ShowSnackbar(val message: String) : ProductEvent()
    data class NavigateToDetail(val productId: Int) : ProductEvent()
    data object ShowCartDialog : ProductEvent()
}

/**
 * 올바른 ViewModel - StateFlow + Channel 분리
 */
class GoodProductViewModel : ViewModel() {
    // 상태: StateFlow
    private val _uiState = MutableStateFlow(GoodProductUiState())
    val uiState: StateFlow<GoodProductUiState> = _uiState.asStateFlow()

    // 이벤트: Channel (일회성 소비 보장)
    private val _events = Channel<ProductEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var addToCartCount = 0

    fun addToCart(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500) // API 호출 시뮬레이션

            addToCartCount++
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cartCount = it.cartCount + 1
                )
            }

            // Channel로 이벤트 전송 - 한 번만 소비됨!
            _events.send(
                ProductEvent.ShowSnackbar(
                    "${product.name}이(가) 장바구니에 추가됨 (총 ${addToCartCount}회 클릭)"
                )
            )
        }
    }

    fun navigateToDetail(productId: Int) {
        viewModelScope.launch {
            _events.send(ProductEvent.NavigateToDetail(productId))
        }
    }

    fun showCart() {
        viewModelScope.launch {
            _events.send(ProductEvent.ShowCartDialog)
        }
    }

    fun selectProduct(productId: Int?) {
        _uiState.update { it.copy(selectedProductId = productId) }
    }
}

/**
 * WhileSubscribed 데모용 ViewModel
 * Repository에서 Flow를 구독하는 패턴
 */
class LocationViewModel : ViewModel() {
    // 가상의 위치 데이터 스트림
    private val locationFlow = flow {
        var lat = 37.5665
        while (true) {
            emit(LocationData(lat, 126.9780))
            delay(2000)
            lat += 0.0001
        }
    }

    // WhileSubscribed(5000) 패턴 적용
    val location: StateFlow<LocationData?> = locationFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000), // 5초 대기
            initialValue = null
        )

    // 구독 상태 추적 (데모용) - 위치 데이터 존재 여부로 판단
    private val _isSubscribed = MutableStateFlow(true)
    val isSubscribed: StateFlow<Boolean> = _isSubscribed.asStateFlow()
}

data class LocationData(val latitude: Double, val longitude: Double)

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
                label = { Text("Channel") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("WhileSubscribed") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("비교") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> ChannelSolutionDemo()
            1 -> WhileSubscribedDemo()
            2 -> ComparisonDemo()
        }
    }
}

/**
 * 데모 1: Channel을 사용한 올바른 이벤트 처리
 */
@Composable
fun ChannelSolutionDemo(viewModel: GoodProductViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Channel 이벤트 수집 - 일회성 소비 보장!
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProductEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                    // clearSnackbar() 불필요! Channel이 자동으로 소비 처리
                }
                is ProductEvent.NavigateToDetail -> {
                    snackbarHostState.showSnackbar("상품 ${event.productId} 상세 페이지로 이동")
                }
                is ProductEvent.ShowCartDialog -> {
                    snackbarHostState.showSnackbar("장바구니 다이얼로그 표시")
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 해결책 설명 카드
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "올바른 패턴: Channel로 이벤트 분리",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "장점:\n" +
                                "1. 화면 회전 시 이벤트가 재실행되지 않음\n" +
                                "2. 수동 초기화 불필요 (자동 소비)\n" +
                                "3. 이벤트 손실 방지 (버퍼 사용)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 올바른 코드 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFC8E6C9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "올바른 코드",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
// ViewModel
private val _events = Channel<Event>(Channel.BUFFERED)
val events = _events.receiveAsFlow()

fun addToCart(product: Product) {
    viewModelScope.launch {
        // 상태 업데이트
        _uiState.update { it.copy(cartCount = it.cartCount + 1) }
        // 이벤트 전송
        _events.send(Event.ShowSnackbar("추가됨"))
    }
}

// Composable
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when (event) {
            is Event.ShowSnackbar ->
                snackbarHostState.showSnackbar(event.message)
        }
    }
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // 테스트 지침
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
                        text = "1. 아래 상품의 '장바구니 추가' 버튼을 클릭하세요\n" +
                                "2. Snackbar가 표시됩니다\n" +
                                "3. 화면을 회전해보세요\n" +
                                "4. Snackbar가 다시 표시되지 않는 것을 확인하세요!",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 장바구니 카운트
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = { viewModel.showCart() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "장바구니 (클릭하여 테스트)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "${uiState.cartCount}개",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 상품 목록
            uiState.products.forEach { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.navigateToDetail(product.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${"%,d".format(product.price)}원",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                            onClick = { viewModel.addToCart(product) },
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("추가")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 데모 2: WhileSubscribed(5000) 패턴
 */
@Composable
fun WhileSubscribedDemo(viewModel: LocationViewModel = viewModel()) {
    val location by viewModel.location.collectAsStateWithLifecycle()
    val isSubscribed by viewModel.isSubscribed.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "WhileSubscribed(5000) 패턴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "마지막 구독자가 사라진 후 5초간 대기합니다.\n" +
                            "5초 내 재구독 시: 업스트림 유지\n" +
                            "5초 후: 업스트림 중단하여 리소스 절약\n\n" +
                            "화면 회전(Configuration Change)에 대응하기 위해 5초 대기 시간을 둡니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFC8E6C9)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 패턴",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
val uiState: StateFlow<UiState> = repository
    .getDataFlow()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5_000,
            replayExpirationMillis = 0
        ),
        initialValue = UiState()
    )
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 구독 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isSubscribed) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSubscribed) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isSubscribed)
                        "현재 화면이 위치 업데이트를 수집하고 있습니다.\n백그라운드로 가면 5초 후 중단됩니다."
                    else
                        "화면이 백그라운드 상태입니다.\n5초 후 업스트림이 중단됩니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSubscribed) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
        }

        // 현재 위치 표시
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "현재 위치 (시뮬레이션)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                location?.let { loc ->
                    Text(
                        text = "위도: ${String.format("%.4f", loc.latitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "경도: ${String.format("%.4f", loc.longitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } ?: run {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "위치 데이터 대기 중...",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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
                    text = "1. 위치가 2초마다 업데이트되는 것을 확인하세요\n" +
                            "2. 앱을 백그라운드로 보내보세요 (홈 버튼)\n" +
                            "3. 5초 후 업스트림이 중단됩니다\n" +
                            "4. 다시 앱으로 돌아오면 즉시 재시작됩니다",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // SharingStarted 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SharingStarted 정책 비교",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                SharingStartedRow("Eagerly", "즉시 시작, 영원히 유지", Color(0xFFFFCDD2))
                Spacer(modifier = Modifier.height(8.dp))
                SharingStartedRow("Lazily", "첫 구독자 등장 시 시작, 영원히 유지", Color(0xFFFFF9C4))
                Spacer(modifier = Modifier.height(8.dp))
                SharingStartedRow(
                    "WhileSubscribed",
                    "구독자 있을 때만 활성, 타임아웃 후 중단",
                    Color(0xFFC8E6C9)
                )
            }
        }
    }
}

@Composable
private fun SharingStartedRow(name: String, description: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 데모 3: StateFlow vs SharedFlow vs Channel 비교
 */
@Composable
fun ComparisonDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 비교 표
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "StateFlow vs SharedFlow vs Channel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // StateFlow
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFBBDEFB)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "StateFlow",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "- Hot Stream, 항상 최신 값 보유\n" +
                                    "- .value로 현재 값 직접 접근\n" +
                                    "- 새 구독자는 즉시 최신 값 수신\n" +
                                    "- distinctUntilChanged 자동 적용",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "용도: UI 상태, 설정, 현재 값이 중요한 경우",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // SharedFlow
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE1BEE7)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "SharedFlow",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "- Hot Stream, 값 보유 옵션 (replay)\n" +
                                    "- 여러 구독자에게 동시 전달\n" +
                                    "- replay, buffer 설정 가능\n" +
                                    "- 구독자 없으면 이벤트 손실 가능",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "용도: 여러 화면에 이벤트 브로드캐스트",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7B1FA2)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Channel
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFC8E6C9)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Channel",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "- Hot, 일회성 소비 보장\n" +
                                    "- 각 이벤트는 단 한 번만 소비\n" +
                                    "- 구독자 없어도 버퍼에 저장\n" +
                                    "- 여러 구독자 중 한 명만 수신",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "용도: Snackbar, Navigation, 일회성 이벤트",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        // 선택 가이드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "선택 가이드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Q: 현재 상태가 중요한가요?
   -> StateFlow (UI 상태, 설정 등)

Q: 여러 구독자에게 같은 이벤트를 전달해야 하나요?
   -> SharedFlow (브로드캐스트)

Q: 이벤트가 딱 한 번만 처리되어야 하나요?
   -> Channel (Snackbar, Navigation)

Q: 구독자 없을 때 이벤트가 손실되면 안 되나요?
   -> Channel (버퍼 보장)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // collectAsState 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "collectAsState vs collectAsStateWithLifecycle",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "collectAsState",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "- 크로스 플랫폼\n- 백그라운드에서도 수집",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "collectAsStateWithLifecycle",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "- Android 전용\n- Lifecycle 인식\n- 공식 권장",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // MVI 패턴 요약
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "MVI 패턴에서의 권장 사용",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
State (UI 상태): StateFlow
- data class UiState(...)
- collectAsStateWithLifecycle()

Intent (사용자 의도): sealed class
- sealed class Intent { ... }
- viewModel.onIntent(intent)

Event (일회성 효과): Channel
- sealed class Event { ... }
- LaunchedEffect { events.collect { } }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
