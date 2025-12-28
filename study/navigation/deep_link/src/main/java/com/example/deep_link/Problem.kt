package com.example.deep_link

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

// ========================================
// 문제 상황: 하드코딩된 문자열 기반 Deep Link
// ========================================

/**
 * 문제점:
 * 1. 문자열 하드코딩 - 오타 시 런타임 에러
 * 2. Nullable 타입 추출 - null 체크 필요
 * 3. 타입 캐스팅 필요 - 런타임 에러 가능성
 * 4. IDE 지원 부족 - 자동완성, 리팩토링 어려움
 */

@Composable
fun ProblemScreen() {
    val navController = rememberNavController()

    // 문제: 문자열 하드코딩으로 Route 정의
    NavHost(
        navController = navController,
        startDestination = "problem_home"  // 문자열 하드코딩!
    ) {
        // 홈 화면
        composable("problem_home") {
            ProblemHomeScreen(navController)
        }

        // 문제: 문자열로 Deep Link 정의
        composable(
            route = "problem_product/{productId}",  // 문자열 하드코딩!
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    // 문제: URI 패턴도 문자열 하드코딩
                    uriPattern = "oldshop://product/{productId}"
                }
            )
        ) { backStackEntry ->
            // 문제: Nullable 타입으로 추출해야 함
            val productId = backStackEntry.arguments?.getString("productId")
            // productId가 null일 수 있어서 null 처리 필요!
            ProblemProductScreen(
                productId = productId ?: "unknown",
                onBack = { navController.popBackStack() }
            )
        }

        // 문제: 여러 인자를 가진 Deep Link
        composable(
            route = "problem_order/{orderId}?status={status}",  // 복잡한 문자열!
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("status") {
                    type = NavType.StringType
                    defaultValue = "pending"
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "oldshop://order/{orderId}?status={status}"
                }
            )
        ) { backStackEntry ->
            // 문제: 모든 인자를 수동으로 추출
            val orderId = backStackEntry.arguments?.getString("orderId")
            val status = backStackEntry.arguments?.getString("status")
            ProblemOrderScreen(
                orderId = orderId ?: "unknown",
                status = status ?: "unknown",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun ProblemHomeScreen(navController: NavHostController) {
    var inputProductId by remember { mutableStateOf("prod001") }
    var inputOrderId by remember { mutableStateOf("order123") }
    var inputStatus by remember { mutableStateOf("pending") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem: 문자열 기반 Deep Link",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 설명 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제점",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 문자열 하드코딩 - 오타 시 런타임 에러")
                Text("2. Nullable 타입 - null 체크 코드 필요")
                Text("3. 타입 안전성 없음 - 잘못된 타입 전달 가능")
                Text("4. IDE 지원 부족 - 리팩토링 어려움")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 코드 문제점 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 예시",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 문자열 하드코딩
route = "product/{productId}"

// Nullable 추출
val id = arguments?.getString("productId")

// null 처리 필요
ProductScreen(id ?: "unknown")
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 상품 상세 테스트
        Text(
            text = "단일 파라미터 테스트",
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
                // 문제: 문자열 조합으로 네비게이션
                navController.navigate("problem_product/$inputProductId")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("상품 상세로 이동 (문자열 기반)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 주문 상세 테스트
        Text(
            text = "다중 파라미터 테스트",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputOrderId,
            onValueChange = { inputOrderId = it },
            label = { Text("Order ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputStatus,
            onValueChange = { inputStatus = it },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // 문제: 복잡한 문자열 조합
                navController.navigate("problem_order/$inputOrderId?status=$inputStatus")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("주문 상세로 이동 (문자열 기반)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Deep Link 테스트 불가 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Deep Link 외부 테스트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("터미널에서 아래 명령어로 테스트:")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "adb shell am start -W -a android.intent.action.VIEW -d \"oldshop://product/test123\"",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemProductScreen(
    productId: String,  // 원래는 String?이어야 함
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상품 상세 (문제)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
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
                style = MaterialTheme.typography.headlineMedium
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
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "문제점:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("- productId가 원래 String?")
                    Text("- null 체크 코드 추가 필요")
                    Text("- 오타 발견이 런타임에만 가능")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemOrderScreen(
    orderId: String,
    status: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주문 상세 (문제)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
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
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Order ID: $orderId")
                    Text("Status: $status")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "문제점:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("- 복잡한 URI 패턴 수동 관리")
                    Text("- Query parameter 문자열 조합")
                    Text("- 여러 인자 추출 시 반복 코드")
                }
            }
        }
    }
}
