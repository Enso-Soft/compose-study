package com.example.navigation_3

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

/**
 * Solution Screen: Navigation 3 실제 구현
 *
 * Navigation 3의 핵심 기능을 인터랙티브하게 시연합니다:
 * 1. 타입 안전 네비게이션 (NavKey)
 * 2. 개발자 소유 백스택 (SnapshotStateList)
 * 3. NavDisplay와 entryProvider
 * 4. 백스택 시각화 및 조작
 */

// ========================================
// NavKey 정의 (Type-Safe Routes)
// ========================================

@Serializable
sealed interface SolutionNavKey

@Serializable
data object HomeKey : SolutionNavKey

@Serializable
data class ProductDetailKey(
    val id: Int,
    val name: String,
    val price: Int
) : SolutionNavKey

@Serializable
data object CartKey : SolutionNavKey

@Serializable
data object SettingsKey : SolutionNavKey

@Serializable
data class CategoryKey(val category: String) : SolutionNavKey

// ========================================
// Solution Screen (메인 컴포저블)
// ========================================

@Composable
fun SolutionScreen() {
    // 참조 객체로 카운트 (State가 아니므로 Recomposition 트리거 안 함)
    val recompositionRef = remember { object { var count = 0 } }

    // 화면에 표시할 State는 별도로 관리
    var displayCount by remember { mutableIntStateOf(0) }

    // 핵심: 개발자가 백스택을 직접 소유!
    // Nav3에서는 rememberNavBackStack을 사용하지만,
    // 학습 목적으로 mutableStateListOf로 직접 구현
    val backStack = remember { mutableStateListOf<SolutionNavKey>(HomeKey) }

    // Recomposition 횟수 추적 (State 변경 없이)
    SideEffect {
        recompositionRef.count++
    }

    // 초기 1회만 displayCount 동기화
    LaunchedEffect(Unit) {
        displayCount = recompositionRef.count
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단: 백스택 시각화
        BackStackVisualizer(
            backStack = backStack,
            recompositionCount = displayCount
        )

        HorizontalDivider()

        // 메인: 현재 화면 렌더링 (NavDisplay 역할)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // 현재 화면 표시 (백스택의 마지막 항목)
            val currentKey = backStack.lastOrNull() ?: HomeKey

            AnimatedContent(
                targetState = currentKey,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                },
                label = "screen_transition"
            ) { key ->
                when (key) {
                    is HomeKey -> HomeScreen(
                        onNavigateToProduct = { id, name, price ->
                            backStack.add(ProductDetailKey(id, name, price))
                        },
                        onNavigateToCart = { backStack.add(CartKey) },
                        onNavigateToSettings = { backStack.add(SettingsKey) },
                        onNavigateToCategory = { category ->
                            backStack.add(CategoryKey(category))
                        }
                    )

                    is ProductDetailKey -> ProductDetailScreen(
                        product = key,
                        onBack = { backStack.removeLastOrNull() },
                        onAddToCart = {
                            // popUpTo Home 후 Cart로 이동
                            backStack.popUpTo<HomeKey>(inclusive = false)
                            backStack.add(CartKey)
                        }
                    )

                    is CartKey -> CartScreen(
                        onBack = { backStack.removeLastOrNull() },
                        onContinueShopping = {
                            // Home으로 초기화
                            backStack.clear()
                            backStack.add(HomeKey)
                        }
                    )

                    is SettingsKey -> SettingsScreen(
                        onBack = { backStack.removeLastOrNull() }
                    )

                    is CategoryKey -> CategoryScreen(
                        category = key.category,
                        onBack = { backStack.removeLastOrNull() },
                        onProductClick = { id, name, price ->
                            backStack.add(ProductDetailKey(id, name, price))
                        }
                    )
                }
            }
        }

        HorizontalDivider()

        // 하단: 백스택 조작 버튼들
        BackStackControls(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() }
        )
    }
}

// ========================================
// 백스택 시각화
// ========================================

@Composable
private fun BackStackVisualizer(
    backStack: List<SolutionNavKey>,
    recompositionCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Navigation 3 Demo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Recomposition: $recompositionCount",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "백스택 (${backStack.size}개):",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(backStack) { key ->
                BackStackItem(key = key, isTop = key == backStack.lastOrNull())
            }
        }
    }
}

@Composable
private fun BackStackItem(key: SolutionNavKey, isTop: Boolean) {
    val (name, color) = when (key) {
        is HomeKey -> "Home" to MaterialTheme.colorScheme.primary
        is ProductDetailKey -> "Product(${key.id})" to MaterialTheme.colorScheme.secondary
        is CartKey -> "Cart" to MaterialTheme.colorScheme.tertiary
        is SettingsKey -> "Settings" to MaterialTheme.colorScheme.error
        is CategoryKey -> key.category to MaterialTheme.colorScheme.outline
    }

    Surface(
        color = if (isTop) color else color.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            if (isTop) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.White
                )
            }
        }
    }
}

// ========================================
// 백스택 조작 버튼
// ========================================

@Composable
private fun BackStackControls(
    backStack: MutableList<SolutionNavKey>,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilledTonalButton(
            onClick = onBack,
            enabled = backStack.size > 1
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Back")
        }

        FilledTonalButton(
            onClick = {
                backStack.clear()
                backStack.add(HomeKey)
            }
        ) {
            Icon(Icons.Default.Home, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Home")
        }

        FilledTonalButton(
            onClick = {
                backStack.popUpTo<HomeKey>(inclusive = false)
            },
            enabled = backStack.size > 1
        ) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("PopUp")
        }
    }
}

// ========================================
// 개별 화면들
// ========================================

@Composable
private fun HomeScreen(
    onNavigateToProduct: (Int, String, Int) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCategory: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 카테고리
        Text(
            text = "카테고리",
            style = MaterialTheme.typography.titleMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Electronics", "Books", "Fashion").forEach { category ->
                FilterChip(
                    selected = false,
                    onClick = { onNavigateToCategory(category) },
                    label = { Text(category) }
                )
            }
        }

        HorizontalDivider()

        // 인기 상품
        Text(
            text = "인기 상품",
            style = MaterialTheme.typography.titleMedium
        )

        // 상품 목록 - 타입 안전 인자 전달!
        val products = listOf(
            Triple(1, "Kotlin in Action", 45000),
            Triple(2, "Android Development", 55000),
            Triple(3, "Compose Essentials", 38000)
        )

        products.forEach { (id, name, price) ->
            ProductCard(
                id = id,
                name = name,
                price = price,
                onClick = { onNavigateToProduct(id, name, price) }
            )
        }

        HorizontalDivider()

        // 빠른 메뉴
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickMenuButton(
                icon = Icons.Default.ShoppingCart,
                label = "Cart",
                onClick = onNavigateToCart
            )
            QuickMenuButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = onNavigateToSettings
            )
        }

        // 핵심 포인트 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Navigation 3 핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 백스택이 상단에 실시간으로 표시됨")
                Text("2. 화면 클릭 시 backStack.add(Key)로 이동")
                Text("3. Back 버튼 시 backStack.removeLastOrNull()")
                Text("4. 타입 안전: ProductDetailKey(id, name, price)")
            }
        }
    }
}

@Composable
private fun ProductCard(
    id: Int,
    name: String,
    price: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$id",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${price}원",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun QuickMenuButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledIconButton(onClick = onClick) {
            Icon(icon, contentDescription = label)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailScreen(
    product: ProductDetailKey,
    onBack: () -> Unit,
    onAddToCart: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상품 상세") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 타입 안전하게 인자 접근!
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "상품 ID: ${product.id}",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${product.price}원",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // 타입 안전성 강조
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "타입 안전 인자 전달",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
data class ProductDetailKey(
    val id: Int,        // Int 타입 보장
    val name: String,   // String 타입 보장
    val price: Int      // Int 타입 보장
)

// 접근: product.id, product.name, product.price
// 타입 캐스팅 불필요! 컴파일 타임 안전!
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("장바구니에 추가 (Home으로 Pop 후 Cart 이동)")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartScreen(
    onBack: () -> Unit,
    onContinueShopping: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("장바구니") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "장바구니",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "장바구니가 비어있습니다",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(onClick = onContinueShopping) {
                Text("쇼핑 계속하기 (백스택 초기화)")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "설정 화면",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryScreen(
    category: String,
    onBack: () -> Unit,
    onProductClick: (Int, String, Int) -> Unit
) {
    val products = when (category) {
        "Electronics" -> listOf(
            Triple(101, "Laptop Pro", 1500000),
            Triple(102, "Smartphone X", 890000)
        )
        "Books" -> listOf(
            Triple(201, "Clean Code", 35000),
            Triple(202, "Design Patterns", 42000)
        )
        "Fashion" -> listOf(
            Triple(301, "Winter Jacket", 89000),
            Triple(302, "Sneakers", 129000)
        )
        else -> emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { (id, name, price) ->
                ProductCard(
                    id = id,
                    name = name,
                    price = price,
                    onClick = { onProductClick(id, name, price) }
                )
            }
        }
    }
}

// ========================================
// 유틸리티 함수
// ========================================

/**
 * popUpTo: 특정 타입의 NavKey까지 모든 항목 제거
 *
 * @param inclusive true면 해당 화면도 제거, false면 해당 화면까지만 유지
 */
inline fun <reified T : SolutionNavKey> MutableList<SolutionNavKey>.popUpTo(
    inclusive: Boolean = false
) {
    val index = indexOfLast { it is T }
    if (index >= 0) {
        val removeFrom = if (inclusive) index else index + 1
        while (size > removeFrom) {
            removeAt(lastIndex)
        }
    }
}
