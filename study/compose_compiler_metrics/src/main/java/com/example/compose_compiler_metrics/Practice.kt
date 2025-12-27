package com.example.compose_compiler_metrics

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

/**
 * 연습 문제 1: Unstable 클래스를 Stable로 만들기
 *
 * 요구사항:
 * - ShoppingCart 클래스가 Unstable로 분류됨
 * - @Immutable 어노테이션을 추가하여 Stable로 만드세요
 * - Recomposition 카운트가 증가하지 않는지 확인하세요
 *
 * TODO: @Immutable 어노테이션을 추가하세요!
 */

// TODO: 이 클래스를 Stable로 만드세요
// 힌트: @Immutable 어노테이션 사용
data class ShoppingCart(
    val id: String,
    val items: List<CartItem>,  // Unstable!
    val totalPrice: Double
)

data class CartItem(
    val productId: String,
    val name: String,
    val quantity: Int
)

/*
// 정답:
@Immutable
data class ShoppingCart(
    val id: String,
    val items: List<CartItem>,
    val totalPrice: Double
)

@Immutable
data class CartItem(
    val productId: String,
    val name: String,
    val quantity: Int
)
*/

@Composable
fun Practice1_StableShoppingCart() {
    var triggerRecomposition by remember { mutableIntStateOf(0) }

    val cart = remember {
        ShoppingCart(
            id = "cart-001",
            items = listOf(
                CartItem("p1", "상품 A", 2),
                CartItem("p2", "상품 B", 1),
                CartItem("p3", "상품 C", 3)
            ),
            totalPrice = 45000.0
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: Unstable -> Stable 변환",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("ShoppingCart 클래스가 List<CartItem>을 포함하여 Unstable입니다.")
                Text("@Immutable 어노테이션을 추가하여 Stable로 만드세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: CartItem 클래스에도 @Immutable이 필요할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { triggerRecomposition++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recomposition 트리거 ($triggerRecomposition)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 Unstable - 매번 recompose됨
        ShoppingCartCard(cart = cart)

        Spacer(modifier = Modifier.height(16.dp))

        // 예상 Report
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 예상 Report",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
unstable class ShoppingCart {
    stable val id: String
    unstable val items: List<CartItem>
    stable val totalPrice: Double
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun ShoppingCartCard(cart: ShoppingCart) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "장바구니 (${cart.id})",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Recompose: ${recomposeCount}회",
                    color = if (recomposeCount > 1)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            cart.items.forEach { item ->
                Text("${item.name} x ${item.quantity}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "총액: ${cart.totalPrice.toInt()}원",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

/**
 * 연습 문제 2: Lambda Memoization 이해하기
 *
 * 요구사항:
 * - onClick lambda가 매번 새로 생성되어 불필요한 recomposition 발생
 * - remember를 사용하여 lambda를 memoize하세요
 * - 또는 Strong Skipping Mode의 동작을 이해하세요
 *
 * TODO: lambda를 memoize하거나 왜 필요없는지 이해하세요!
 */
@Composable
fun Practice2_LambdaMemoization() {
    var count by remember { mutableIntStateOf(0) }
    var triggerRecomposition by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: Lambda Memoization",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("onClick lambda가 매번 새로 생성됩니다.")
                Text("Strong Skipping Mode가 없다면 문제가 됩니다.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: Kotlin 2.0.20+에서는 자동 memoization됩니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 부모 상태 변경 버튼
        Button(
            onClick = { triggerRecomposition++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("부모 Recomposition 트리거 ($triggerRecomposition)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("현재 카운트: $count")

        Spacer(modifier = Modifier.height(16.dp))

        // 문제가 되는 코드: lambda가 매번 새로 생성됨
        // Strong Skipping Mode 없이는 CounterButton이 매번 recompose됨
        CounterButton(
            label = "증가",
            onClick = { count++ }  // 매번 새 lambda 인스턴스!
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 해결책: remember로 memoize
        val memoizedOnClick: () -> Unit = remember { { count++ } }
        CounterButton(
            label = "증가 (Memoized)",
            onClick = memoizedOnClick  // 같은 lambda 인스턴스
        )

        Spacer(modifier = Modifier.height(16.dp))

        /*
        // 더 나은 해결책 (count를 캡처하는 경우):
        val onIncrement = remember(count) {
            { count++ }
        }

        // 또는 rememberUpdatedState 사용:
        val currentCount by rememberUpdatedState(count)
        val onIncrement = remember {
            { /* currentCount 사용 */ }
        }
        */

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Strong Skipping Mode (Kotlin 2.0.20+)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
Strong Skipping이 활성화되면:
- Lambda가 자동으로 memoize됨
- 캡처된 값을 key로 사용
- 수동 remember 불필요!

→ 위 두 버튼의 동작이 동일해집니다.
                    """.trimIndent(),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun CounterButton(
    label: String,
    onClick: () -> Unit
) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++
    }

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("$label (Recompose: ${recomposeCount}회)")
    }
}

/**
 * 연습 문제 3: Compiler Report 해석 및 문제 진단
 *
 * 요구사항:
 * - 주어진 Compiler Report를 분석하세요
 * - 문제가 되는 클래스와 Composable을 찾으세요
 * - 해결책을 적용하세요
 *
 * TODO: Report를 분석하고 문제를 해결하세요!
 */

// 아래 Compiler Report를 분석하세요:
/*
unstable class User {
    stable val id: Long
    stable val name: String
    unstable val permissions: Set<Permission>
    <runtime stability> = Unstable
}

restartable scheme("[...]") fun UserDashboard(
    unstable user: User
    stable modifier: Modifier
)
// 'skippable' 없음!
*/

// TODO: User 클래스를 Stable로 만드세요
enum class Permission { READ, WRITE, DELETE, ADMIN }

data class User(
    val id: Long,
    val name: String,
    val permissions: Set<Permission>  // 문제!
)

/*
// 정답:
@Immutable
data class User(
    val id: Long,
    val name: String,
    val permissions: Set<Permission>
)
*/

@Composable
fun Practice3_ReportAnalysis() {
    var triggerRecomposition by remember { mutableIntStateOf(0) }

    val user = remember {
        User(
            id = 1L,
            name = "관리자",
            permissions = setOf(Permission.READ, Permission.WRITE, Permission.ADMIN)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: Report 분석",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("아래 Compiler Report를 분석하세요:")
                Text("1. 어떤 필드가 Unstable인가요?")
                Text("2. 왜 UserDashboard가 skippable이 아닌가요?")
                Text("3. 어떻게 해결할 수 있나요?")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 분석할 Report
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Compiler Report (classes.txt)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
unstable class User {
    stable val id: Long
    stable val name: String
    unstable val permissions: Set<Permission>
    <runtime stability> = Unstable
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Compiler Report (composables.txt)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
restartable scheme("[...]")
fun UserDashboard(
    unstable user: User
    stable modifier: Modifier
)
// 주목: 'skippable' 키워드 없음!
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { triggerRecomposition++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recomposition 트리거 ($triggerRecomposition)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        UserDashboard(user = user)

        Spacer(modifier = Modifier.height(16.dp))

        // 정답 힌트
        var showAnswer by remember { mutableStateOf(false) }

        Button(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. permissions: Set<Permission>이 Unstable")
                    Text("2. User가 Unstable -> UserDashboard가 non-skippable")
                    Text("3. 해결책:")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = """
@Immutable
data class User(
    val id: Long,
    val name: String,
    val permissions: Set<Permission>
)
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun UserDashboard(
    user: User,
    modifier: Modifier = Modifier
) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++
    }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Recompose: ${recomposeCount}회",
                    color = if (recomposeCount > 1)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("ID: ${user.id}")
            Text("권한: ${user.permissions.joinToString(", ")}")
        }
    }
}
