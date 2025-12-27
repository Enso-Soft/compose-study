package com.example.compose_compiler_metrics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
 * 문제가 있는 코드 - Unstable 클래스로 인한 불필요한 Recomposition
 *
 * 이 화면에서는:
 * 1. List<String>을 포함한 data class가 Unstable로 분류됨
 * 2. Unstable 파라미터를 받는 Composable은 Skippable이 아님
 * 3. 부모가 recompose될 때마다 자식도 불필요하게 recompose됨
 * 4. Compiler Metrics 없이는 원인을 파악하기 어려움
 */

// Unstable 클래스 - List<String>이 인터페이스이므로 Unstable!
data class UserProfile(
    val id: Long,
    val name: String,
    val email: String,
    val interests: List<String>  // 문제! List는 인터페이스 → Unstable
)

// Unstable 클래스 - Set을 포함
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val tags: Set<String>  // 문제! Set도 인터페이스 → Unstable
)

@Composable
fun ProblemScreen() {
    var triggerRecomposition by remember { mutableIntStateOf(0) }
    var parentRecomposeCount by remember { mutableIntStateOf(0) }

    // 부모 recomposition 추적
    SideEffect {
        parentRecomposeCount++
    }

    // 샘플 데이터 (Unstable 클래스들)
    val user = remember {
        UserProfile(
            id = 1L,
            name = "홍길동",
            email = "hong@example.com",
            interests = listOf("Kotlin", "Compose", "Android")
        )
    }

    val products = remember {
        listOf(
            Product("1", "상품 A", 10000.0, setOf("전자기기", "할인")),
            Product("2", "상품 B", 20000.0, setOf("의류", "신상품")),
            Product("3", "상품 C", 15000.0, setOf("식품", "베스트"))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Problem: Unstable 클래스",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 문제 설명 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. UserProfile에 List<String> 포함 -> Unstable")
                Text("2. Product에 Set<String> 포함 -> Unstable")
                Text("3. 모든 자식 Composable이 매번 Recompose됨")
                Text("4. 성능 저하의 원인을 찾기 어려움")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recomposition 트리거 버튼
        Button(
            onClick = { triggerRecomposition++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("부모 Recomposition 트리거 (현재: $triggerRecomposition)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 카운터 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Recomposition 카운터",
                    style = MaterialTheme.typography.titleSmall
                )
                Text("부모 화면: ${parentRecomposeCount}회")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 자식 Composable들의 카운터를 확인하세요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Unstable 파라미터를 받는 Composable들
        // 부모가 recompose될 때마다 함께 recompose됨!
        UnstableUserCard(user = user)

        Spacer(modifier = Modifier.height(8.dp))

        products.forEach { product ->
            UnstableProductCard(product = product)
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compiler Report 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예상 Compiler Report (classes.txt)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
unstable class UserProfile {
    stable val id: Long
    stable val name: String
    stable val email: String
    unstable val interests: List<String>
    <runtime stability> = Unstable
}

unstable class Product {
    stable val id: String
    stable val name: String
    stable val price: Double
    unstable val tags: Set<String>
    <runtime stability> = Unstable
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // composables.txt 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예상 Compiler Report (composables.txt)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
restartable scheme("[...]") fun UnstableUserCard(
    unstable user: UserProfile
)
// 주목: 'skippable' 키워드가 없음!

restartable scheme("[...]") fun UnstableProductCard(
    unstable product: Product
)
// 역시 'skippable' 없음 -> 매번 recompose
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

/**
 * Unstable 파라미터를 받는 Composable
 * - UserProfile이 Unstable이므로 이 함수는 Skippable이 아님
 * - 부모가 recompose될 때마다 함께 recompose됨
 */
@Composable
fun UnstableUserCard(user: UserProfile) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
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
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "관심사: ${user.interests.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * Unstable 파라미터를 받는 Composable
 * - Product가 Unstable이므로 이 함수는 Skippable이 아님
 */
@Composable
fun UnstableProductCard(product: Product) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${product.price.toInt()}원",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = product.tags.joinToString(" | "),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = "${recomposeCount}회",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
