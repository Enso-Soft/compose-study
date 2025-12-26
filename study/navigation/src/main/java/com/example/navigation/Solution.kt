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
// Type-Safe Route 정의
// ========================================

/**
 * 인자 없는 Route: object 사용
 */
@Serializable
object SolutionHome

/**
 * 인자 있는 Route: data class 사용
 * - 모든 인자가 타입 안전하게 정의됨
 */
@Serializable
data class SolutionProfile(val userId: String)

/**
 * 선택적 인자가 있는 Route: 기본값 사용
 */
@Serializable
data class SolutionSettings(val darkMode: Boolean = false)

/**
 * 여러 인자가 있는 Route
 */
@Serializable
data class SolutionProduct(
    val productId: String,
    val quantity: Int,
    val isInCart: Boolean = false
)

// ========================================
// Type-Safe Navigation 구현
// ========================================

@Composable
fun SolutionScreen() {
    val navController = rememberNavController()

    // Type-Safe NavHost 구성
    NavHost(
        navController = navController,
        startDestination = SolutionHome  // 타입으로 지정 (문자열 아님!)
    ) {
        // composable<T>로 목적지 정의
        composable<SolutionHome> {
            SolutionHomeScreen(navController)
        }

        composable<SolutionProfile> { backStackEntry ->
            // toRoute<T>()로 타입 안전하게 인자 추출
            val profile: SolutionProfile = backStackEntry.toRoute()
            SolutionProfileScreen(
                userId = profile.userId,  // String 타입 보장!
                onBack = { navController.popBackStack() }
            )
        }

        composable<SolutionSettings> { backStackEntry ->
            val settings: SolutionSettings = backStackEntry.toRoute()
            SolutionSettingsScreen(
                darkMode = settings.darkMode,  // Boolean 타입 보장!
                onBack = { navController.popBackStack() }
            )
        }

        composable<SolutionProduct> { backStackEntry ->
            val product: SolutionProduct = backStackEntry.toRoute()
            SolutionProductScreen(
                productId = product.productId,
                quantity = product.quantity,
                isInCart = product.isInCart,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun SolutionHomeScreen(navController: NavHostController) {
    var inputUserId by remember { mutableStateOf("user123") }
    var inputProductId by remember { mutableStateOf("prod001") }
    var inputQuantity by remember { mutableStateOf("2") }
    var inputIsInCart by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution: Type-Safe Navigation",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "장점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 컴파일 타임에 오류 발견")
                Text("2. IDE 자동완성 지원")
                Text("3. 타입 안전성 보장")
                Text("4. 리팩토링 용이")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 프로필 이동 섹션
        Text(
            text = "단일 인자 전달",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputUserId,
            onValueChange = { inputUserId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Type-Safe 네비게이션!
                // 컴파일 타임에 타입 검증
                navController.navigate(SolutionProfile(userId = inputUserId))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("프로필로 이동 (Type-Safe)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 설정 이동 섹션
        Text(
            text = "기본값 있는 인자",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    // 기본값 사용 (darkMode = false)
                    navController.navigate(SolutionSettings())
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("라이트 모드")
            }

            Button(
                onClick = {
                    navController.navigate(SolutionSettings(darkMode = true))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("다크 모드")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 상품 이동 섹션
        Text(
            text = "다중 인자 전달",
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

        OutlinedTextField(
            value = inputQuantity,
            onValueChange = { inputQuantity = it },
            label = { Text("Quantity (숫자)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = inputIsInCart,
                onCheckedChange = { inputIsInCart = it }
            )
            Text("장바구니에 추가됨")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val qty = inputQuantity.toIntOrNull() ?: 1
                // 여러 타입의 인자를 타입 안전하게 전달
                navController.navigate(
                    SolutionProduct(
                        productId = inputProductId,
                        quantity = qty,  // Int 타입!
                        isInCart = inputIsInCart  // Boolean 타입!
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("상품 상세로 이동")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 코드 예시
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
// Route 정의
@Serializable
data class Profile(val userId: String)

// 네비게이션 (타입 안전!)
navController.navigate(Profile(userId = "abc"))

// 인자 추출 (타입 안전!)
val profile: Profile = backStackEntry.toRoute()
val id: String = profile.userId
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionProfileScreen(
    userId: String,  // Non-nullable! 타입 안전
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필 (Type-Safe)") },
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
            Text(
                text = "프로필 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Non-nullable String - 항상 값이 있음!
            Text(
                text = "User ID: $userId",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Type-Safe 장점:")
                    Text("- userId가 Non-nullable (String)")
                    Text("- 컴파일 타임에 타입 검증")
                    Text("- IDE 자동완성 지원")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionSettingsScreen(
    darkMode: Boolean,  // Boolean 타입 보장!
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정 (Type-Safe)") },
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
            Text(
                text = "설정 화면",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (darkMode) "다크 모드 ON" else "라이트 모드",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("darkMode: $darkMode (Boolean 타입)")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionProductScreen(
    productId: String,
    quantity: Int,  // Int 타입 보장!
    isInCart: Boolean,
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
            Text(
                text = "상품 상세",
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
                        text = "전달된 인자들:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Product ID: $productId (String)")
                    Text("Quantity: $quantity (Int)")
                    Text("In Cart: $isInCart (Boolean)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "모든 인자가 타입 안전하게 전달됨!",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
