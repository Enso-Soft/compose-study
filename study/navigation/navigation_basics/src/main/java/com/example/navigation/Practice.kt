package com.example.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

// ========================================
// 연습 문제 1: 기본 네비게이션
// TODO: Route 정의를 완성하세요
// ========================================

/**
 * 연습 1: 인자 없는 Route
 * TODO: @Serializable 어노테이션을 추가하세요
 */
@Serializable
object Practice1Home

/**
 * 연습 1: 인자 있는 Route
 * TODO: @Serializable 어노테이션을 추가하고 itemId 파라미터를 정의하세요
 */
@Serializable
data class Practice1Detail(val itemId: String)

// ========================================
// 연습 문제 2: 다중 인자 전달
// TODO: 여러 타입의 인자를 가진 Route를 정의하세요
// ========================================

/**
 * 연습 2: 쇼핑 앱의 상품 목록 화면
 */
@Serializable
object Practice2ProductList

/**
 * 연습 2: 상품 상세 화면
 * TODO: productId(String), quantity(Int), isInCart(Boolean) 인자를 추가하세요
 * 힌트: isInCart는 기본값 false로 설정
 */
// 아래 주석을 해제하고 완성하세요:
/*
@Serializable
data class Practice2ProductDetail(
    val productId: String,
    val quantity: Int,
    val isInCart: Boolean = false
)
*/

// 임시 정의 (연습용 - 위 코드를 완성하면 이것을 삭제하세요)
@Serializable
data class Practice2ProductDetail(
    val productId: String = "",
    val quantity: Int = 1,
    val isInCart: Boolean = false
)

// ========================================
// 연습 문제 3: 뒤로 가기 처리
// ========================================

@Serializable
object Practice3Start

@Serializable
object Practice3Middle

@Serializable
object Practice3End

// ========================================
// 연습 화면 구현
// ========================================

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
                label = { Text("연습 1") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("연습 2") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("연습 3") }
            )
        }

        when (selectedPractice) {
            0 -> Practice1Screen()
            1 -> Practice2Screen()
            2 -> Practice3Screen()
        }
    }
}

// ========================================
// 연습 1: 기본 Type-Safe Navigation
// ========================================

@Composable
fun Practice1Screen() {
    val navController = rememberNavController()

    // TODO: NavHost를 Type-Safe 방식으로 구성하세요
    // 힌트: startDestination = Practice1Home
    NavHost(
        navController = navController,
        startDestination = Practice1Home
    ) {
        composable<Practice1Home> {
            Practice1HomeScreen(navController)
        }

        // TODO: Practice1Detail 화면을 추가하세요
        // 힌트: composable<Practice1Detail> { ... }
        composable<Practice1Detail> { backStackEntry ->
            val detail: Practice1Detail = backStackEntry.toRoute()
            Practice1DetailScreen(
                itemId = detail.itemId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun Practice1HomeScreen(navController: NavHostController) {
    var inputItemId by remember { mutableStateOf("item001") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 기본 Type-Safe Navigation",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: Detail 화면으로 이동하는 코드를 완성하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputItemId,
            onValueChange = { inputItemId = it },
            label = { Text("Item ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Type-Safe 네비게이션으로 변경하세요
        // 현재 코드는 정답입니다. 이해하고 분석해보세요!
        Button(
            onClick = {
                // 정답: Type-Safe 네비게이션
                navController.navigate(Practice1Detail(itemId = inputItemId))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Detail로 이동")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("학습 포인트:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. @Serializable로 Route 정의")
                Text("2. composable<T>로 목적지 등록")
                Text("3. navController.navigate(RouteInstance)")
                Text("4. backStackEntry.toRoute<T>()로 인자 추출")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1DetailScreen(
    itemId: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Item ID: $itemId", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("성공! Type-Safe하게 인자가 전달되었습니다.")
        }
    }
}

// ========================================
// 연습 2: 다중 인자 전달
// ========================================

@Composable
fun Practice2Screen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Practice2ProductList
    ) {
        composable<Practice2ProductList> {
            Practice2ProductListScreen(navController)
        }

        composable<Practice2ProductDetail> { backStackEntry ->
            val product: Practice2ProductDetail = backStackEntry.toRoute()
            Practice2ProductDetailScreen(
                productId = product.productId,
                quantity = product.quantity,
                isInCart = product.isInCart,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun Practice2ProductListScreen(navController: NavHostController) {
    var productId by remember { mutableStateOf("prod001") }
    var quantity by remember { mutableStateOf("3") }
    var isInCart by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: 다중 인자 전달",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "여러 타입의 인자를 전달해보세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = productId,
            onValueChange = { productId = it },
            label = { Text("Product ID (String)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity (Int)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isInCart,
                onCheckedChange = { isInCart = it }
            )
            Text("장바구니에 추가 (Boolean)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val qty = quantity.toIntOrNull() ?: 1
                // TODO: 여러 인자를 전달하는 코드를 분석하세요
                navController.navigate(
                    Practice2ProductDetail(
                        productId = productId,
                        quantity = qty,
                        isInCart = isInCart
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("상품 상세로 이동")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("학습 포인트:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. data class에 여러 타입 인자 정의")
                Text("2. String, Int, Boolean 모두 타입 안전")
                Text("3. 기본값 사용으로 선택적 인자 처리")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2ProductDetailScreen(
    productId: String,
    quantity: Int,
    isInCart: Boolean,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상품 상세") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("전달된 인자들:", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Product ID: $productId (String)")
                    Text("Quantity: $quantity (Int)")
                    Text("In Cart: $isInCart (Boolean)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("모든 타입이 안전하게 전달됨!", color = MaterialTheme.colorScheme.primary)
        }
    }
}

// ========================================
// 연습 3: 뒤로 가기 처리
// ========================================

@Composable
fun Practice3Screen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Practice3Start
    ) {
        composable<Practice3Start> {
            Practice3StartScreen(navController)
        }

        composable<Practice3Middle> {
            Practice3MiddleScreen(navController)
        }

        composable<Practice3End> {
            Practice3EndScreen(navController)
        }
    }
}

@Composable
fun Practice3StartScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 3: 뒤로 가기 처리",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("시작 화면 (Start)")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(Practice3Middle)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Middle로 이동")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("학습 포인트:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. popBackStack(): 이전 화면으로")
                Text("2. popBackStack<Route>(): 특정 화면까지")
                Text("3. inclusive 파라미터 이해")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3MiddleScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Middle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("중간 화면 (Middle)")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate(Practice3End)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("End로 이동")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    // 이전 화면으로 돌아가기
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("popBackStack() - Start로")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3EndScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("End") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("마지막 화면 (End)")

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    // 바로 이전 화면(Middle)으로
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("popBackStack() - Middle로")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Start 화면까지 돌아가기 (Middle 제거)
                    navController.popBackStack<Practice3Start>(inclusive = false)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("popBackStack<Start>() - Start로 바로")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("백스택 현재 상태:")
                    Text("Start -> Middle -> End")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("inclusive=false: Start 화면 유지")
                    Text("inclusive=true: Start 화면도 제거")
                }
            }
        }
    }
}
