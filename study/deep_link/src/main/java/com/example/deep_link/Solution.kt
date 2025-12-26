package com.example.deep_link

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

// ========================================
// 해결책: Type-Safe Deep Link
// ========================================

/**
 * Type-Safe Route 정의
 * @Serializable 어노테이션으로 직렬화 가능하게 만듦
 */

// 홈 화면 (인자 없음)
@Serializable
object SolutionHome

// 상품 상세 화면 (필수 인자)
@Serializable
data class SolutionProduct(val productId: String)

// 주문 상세 화면 (필수 + 선택 인자)
@Serializable
data class SolutionOrder(
    val orderId: String,           // 필수: path parameter로 변환
    val status: String = "pending" // 선택: query parameter로 변환
)

// 장바구니 화면 (인자 없음)
@Serializable
object SolutionCart

// ========================================
// Deep Link URI 상수
// ========================================
private const val BASE_URI_CUSTOM = "myshop://"
private const val BASE_URI_HTTPS = "https://myshop.example.com"

@Composable
fun SolutionScreen() {
    val navController = rememberNavController()

    // Type-Safe NavHost 구성
    NavHost(
        navController = navController,
        startDestination = SolutionHome  // 타입으로 지정!
    ) {
        // 홈 화면
        composable<SolutionHome> {
            SolutionHomeScreen(navController)
        }

        // 상품 상세 화면 + Deep Link
        composable<SolutionProduct>(
            deepLinks = listOf(
                // 커스텀 스킴 Deep Link
                navDeepLink<SolutionProduct>(basePath = "${BASE_URI_CUSTOM}product"),
                // HTTPS Deep Link
                navDeepLink<SolutionProduct>(basePath = "${BASE_URI_HTTPS}/product")
            )
        ) { backStackEntry ->
            // toRoute<T>()로 타입 안전하게 추출
            val product: SolutionProduct = backStackEntry.toRoute()
            SolutionProductScreen(
                productId = product.productId,  // Non-nullable String!
                onBack = { navController.popBackStack() }
            )
        }

        // 주문 상세 화면 + Deep Link (필수 + 선택 인자)
        composable<SolutionOrder>(
            deepLinks = listOf(
                navDeepLink<SolutionOrder>(basePath = "${BASE_URI_CUSTOM}order")
            )
        ) { backStackEntry ->
            val order: SolutionOrder = backStackEntry.toRoute()
            SolutionOrderScreen(
                orderId = order.orderId,
                status = order.status,
                onBack = { navController.popBackStack() }
            )
        }

        // 장바구니 화면 + Deep Link
        composable<SolutionCart>(
            deepLinks = listOf(
                navDeepLink<SolutionCart>(basePath = "${BASE_URI_CUSTOM}cart")
            )
        ) {
            SolutionCartScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun SolutionHomeScreen(navController: NavHostController) {
    var inputProductId by remember { mutableStateOf("prod001") }
    var inputOrderId by remember { mutableStateOf("order123") }
    var inputStatus by remember { mutableStateOf("shipped") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution: Type-Safe Deep Link",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. @Serializable로 Route 정의")
                Text("2. navDeepLink<T>()로 Deep Link 정의")
                Text("3. toRoute<T>()로 타입 안전하게 추출")
                Text("4. Non-nullable 파라미터!")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 코드 예시 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드 예시",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Serializable
data class Product(val productId: String)

composable<Product>(
    deepLinks = listOf(
        navDeepLink<Product>(
            basePath = "myshop://product"
        )
    )
) { backStackEntry ->
    val product = backStackEntry.toRoute<Product>()
    // product.productId는 Non-nullable!
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 상품 상세 테스트
        Text(
            text = "Type-Safe 네비게이션",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputProductId,
            onValueChange = { inputProductId = it },
            label = { Text("Product ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Type-Safe 네비게이션!
                navController.navigate(SolutionProduct(productId = inputProductId))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("상품 상세로 이동 (Type-Safe)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Deep Link 시뮬레이션 버튼
        OutlinedButton(
            onClick = {
                // Deep Link URI로 네비게이션 시뮬레이션
                val uri = "myshop://product/$inputProductId".toUri()
                val request = androidx.navigation.NavDeepLinkRequest.Builder
                    .fromUri(uri)
                    .build()
                navController.navigate(request)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Deep Link 시뮬레이션")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 주문 상세 테스트 (다중 파라미터)
        Text(
            text = "다중 파라미터 Deep Link",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputOrderId,
            onValueChange = { inputOrderId = it },
            label = { Text("Order ID (필수)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputStatus,
            onValueChange = { inputStatus = it },
            label = { Text("Status (선택, 기본값: pending)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(
                        SolutionOrder(
                            orderId = inputOrderId,
                            status = inputStatus
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Type-Safe")
            }

            OutlinedButton(
                onClick = {
                    // Deep Link 시뮬레이션 (query parameter 포함)
                    val uri = "myshop://order/$inputOrderId?status=$inputStatus".toUri()
                    val request = androidx.navigation.NavDeepLinkRequest.Builder
                        .fromUri(uri)
                        .build()
                    navController.navigate(request)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Deep Link")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 장바구니 (인자 없음)
        Text(
            text = "인자 없는 Deep Link",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(SolutionCart)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("장바구니")
            }

            OutlinedButton(
                onClick = {
                    val uri = "myshop://cart".toUri()
                    val request = androidx.navigation.NavDeepLinkRequest.Builder
                        .fromUri(uri)
                        .build()
                    navController.navigate(request)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Deep Link")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // URI 패턴 설명 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "URI 패턴 자동 생성 규칙",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("필수 파라미터 → Path: /{orderId}")
                Text("선택 파라미터 → Query: ?status={status}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "결과: myshop://order/{orderId}?status={status}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ADB 테스트 명령어
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ADB 테스트 명령어",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
adb shell am start -W \
  -a android.intent.action.VIEW \
  -d "myshop://product/test123" \
  com.example.deep_link
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionProductScreen(
    productId: String,  // Non-nullable! 타입 안전
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상품 상세 (Type-Safe)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
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
            Text(
                text = "상품 상세 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Product ID: $productId",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Type-Safe 장점:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("- productId가 Non-nullable String")
                    Text("- 컴파일 타임에 타입 검증")
                    Text("- IDE 자동완성 지원")
                    Text("- null 체크 불필요!")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deep Link로 진입해도 동일하게 작동!",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionOrderScreen(
    orderId: String,
    status: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주문 상세 (Type-Safe)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
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
            Text(
                text = "주문 상세 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "전달된 파라미터:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Order ID: $orderId (필수)")
                    Text("Status: $status (선택, 기본값: pending)")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "URI 패턴:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("myshop://order/{orderId}?status={status}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "필수 → path, 선택 → query",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionCartScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("장바구니 (Type-Safe)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
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
            Text(
                text = "장바구니 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "인자 없는 Deep Link:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("URI: myshop://cart")
                    Text("Route: @Serializable object Cart")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deep Link로 직접 장바구니 접근 가능!",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
