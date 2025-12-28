package com.example.state_management_advanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Problem: StateFlow로 일회성 이벤트를 처리할 때 발생하는 문제
 *
 * 문제점:
 * 1. 화면 회전 시 Snackbar가 다시 표시됨
 * 2. 수동으로 이벤트를 초기화해야 함
 * 3. 연속 이벤트 발생 시 덮어씌워질 수 있음
 */

// ==================== 잘못된 방식의 UiState ====================

/**
 * 문제: 일회성 이벤트(snackbarMessage, navigateTo)가 상태에 포함됨
 * StateFlow는 최신 값을 유지하므로, 화면 회전 시 이벤트가 재실행됨
 */
data class BadProductUiState(
    val products: List<Product> = sampleProducts,
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,  // 문제: 상태로 관리되는 이벤트
    val navigateTo: String? = null,        // 문제: 상태로 관리되는 이벤트
    val cartCount: Int = 0
)

data class Product(
    val id: Int,
    val name: String,
    val price: Int
)

val sampleProducts = listOf(
    Product(1, "노트북", 1200000),
    Product(2, "마우스", 50000),
    Product(3, "키보드", 80000),
    Product(4, "모니터", 350000)
)

/**
 * 잘못된 ViewModel - StateFlow로 일회성 이벤트 처리
 */
class BadProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BadProductUiState())
    val uiState: StateFlow<BadProductUiState> = _uiState.asStateFlow()

    private var addToCartCount = 0

    fun addToCart(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500) // API 호출 시뮬레이션

            addToCartCount++
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cartCount = it.cartCount + 1,
                    // 문제: 일회성 이벤트가 상태로 저장됨
                    snackbarMessage = "${product.name}이(가) 장바구니에 추가됨 (총 ${addToCartCount}회 클릭)"
                )
            }
        }
    }

    fun navigateToDetail(productId: Int) {
        // 문제: Navigation도 상태로 저장됨
        _uiState.update {
            it.copy(navigateTo = "detail/$productId")
        }
    }

    // 수동으로 이벤트를 초기화해야 함 (잊기 쉬움!)
    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun clearNavigation() {
        _uiState.update { it.copy(navigateTo = null) }
    }
}

// ==================== UI 컴포저블 ====================

@Composable
fun ProblemScreen() {
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
                label = { Text("Snackbar 문제") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("collectAsState") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> SnackbarProblemDemo()
            1 -> CollectAsStateProblemDemo()
        }
    }
}

/**
 * 데모 1: StateFlow로 Snackbar 처리 시 발생하는 문제
 */
@Composable
fun SnackbarProblemDemo(viewModel: BadProductViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 문제 시연: snackbarMessage가 상태에 있으므로 화면 회전 시 재실행됨
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()  // 수동 초기화 필요!
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
            // 문제 설명 카드
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "잘못된 패턴: StateFlow로 Snackbar 처리",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "문제점:\n" +
                                "1. 화면 회전 시 Snackbar가 다시 표시됨\n" +
                                "2. clearSnackbar() 호출을 잊으면 이벤트가 남아 있음\n" +
                                "3. 빠른 연속 클릭 시 이전 이벤트가 무시될 수 있음",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 잘못된 코드 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "잘못된 코드",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
data class UiState(
    val products: List<Product>,
    val snackbarMessage: String? = null,
    // 일회성 이벤트가 상태에 포함됨
)

// LaunchedEffect에서 상태 변경 감지
LaunchedEffect(uiState.snackbarMessage) {
    uiState.snackbarMessage?.let {
        snackbarHostState.showSnackbar(it)
        viewModel.clearSnackbar()
        // 수동 초기화 필요!
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
                    containerColor = Color(0xFFFFF3E0)
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
                                "4. Snackbar가 다시 표시되는 것을 확인하세요!",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 장바구니 카운트
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
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
                            text = "장바구니",
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
                ProductCard(
                    product = product,
                    isLoading = uiState.isLoading,
                    onAddToCart = { viewModel.addToCart(product) }
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    isLoading: Boolean,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                onClick = onAddToCart,
                enabled = !isLoading
            ) {
                if (isLoading) {
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

/**
 * 데모 2: collectAsState vs collectAsStateWithLifecycle 문제
 */
@Composable
fun CollectAsStateProblemDemo() {
    var recomposeCount by remember { mutableIntStateOf(0) }

    // Recomposition 카운터
    SideEffect {
        recomposeCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFC62828)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "collectAsState의 문제점",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "collectAsState는 앱이 백그라운드에 있어도 Flow 수집을 계속합니다.\n\n" +
                            "문제가 되는 상황:\n" +
                            "- 위치 업데이트 구독\n" +
                            "- Firebase 실시간 데이터베이스\n" +
                            "- 센서 데이터 수집\n\n" +
                            "결과: 배터리 소모, 메모리 누수 가능성",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 비교 표
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "collectAsState vs collectAsStateWithLifecycle",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // collectAsState
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFCDD2)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "collectAsState()",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                        Text(
                            text = "val state by flow.collectAsState()",
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "- Composition 수명주기만 따름\n" +
                                    "- 백그라운드에서도 수집 계속\n" +
                                    "- 리소스 낭비 가능성",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // collectAsStateWithLifecycle
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFC8E6C9)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "collectAsStateWithLifecycle() [권장]",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "val state by flow.collectAsStateWithLifecycle()",
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "- Android Lifecycle 인식\n" +
                                    "- STARTED 상태 이상에서만 수집\n" +
                                    "- 백그라운드에서 자동 중단",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // SharingStarted 문제
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SharingStarted.Eagerly 문제",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 잘못된 예시
val uiState = repository.getDataFlow()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        // 항상 활성화 - 리소스 낭비!
        initialValue = UiState()
    )
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SharingStarted.Eagerly는 ViewModel이 생성되자마자 " +
                            "업스트림 Flow를 시작하고, 구독자가 없어도 계속 유지합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 해결책 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Solution 탭 참조",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. collectAsStateWithLifecycle() 사용\n" +
                            "2. SharingStarted.WhileSubscribed(5000) 사용\n" +
                            "3. 일회성 이벤트는 Channel로 분리",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Recomposition 카운터
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Recomposition 횟수:")
                Text(
                    text = "${recomposeCount}회",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
