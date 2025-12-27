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
 * 올바른 코드 - Stable 클래스로 최적화된 Composable
 *
 * 해결 방법:
 * 1. @Immutable 어노테이션 사용
 * 2. Strong Skipping Mode 활용 (Kotlin 2.0.20+)
 * 3. Compiler Metrics로 결과 확인
 */

// 해결책 1: @Immutable 어노테이션 사용
// 컴파일러에게 "이 클래스는 불변이다"라고 약속
@Immutable
data class StableUserProfile(
    val id: Long,
    val name: String,
    val email: String,
    val interests: List<String>  // 이제 문제없음!
)

@Immutable
data class StableProduct(
    val id: String,
    val name: String,
    val price: Double,
    val tags: Set<String>  // 이제 문제없음!
)

@Composable
fun SolutionScreen() {
    var triggerRecomposition by remember { mutableIntStateOf(0) }
    var parentRecomposeCount by remember { mutableIntStateOf(0) }

    // 부모 recomposition 추적
    SideEffect {
        parentRecomposeCount++
    }

    // 샘플 데이터 (Stable 클래스들)
    val user = remember {
        StableUserProfile(
            id = 1L,
            name = "홍길동",
            email = "hong@example.com",
            interests = listOf("Kotlin", "Compose", "Android")
        )
    }

    val products = remember {
        listOf(
            StableProduct("1", "상품 A", 10000.0, setOf("전자기기", "할인")),
            StableProduct("2", "상품 B", 20000.0, setOf("의류", "신상품")),
            StableProduct("3", "상품 C", 15000.0, setOf("식품", "베스트"))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Solution: Stable 클래스",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 해결책 설명 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결 방법",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. @Immutable 어노테이션 추가")
                Text("2. 컴파일러가 클래스를 Stable로 인식")
                Text("3. Composable이 Skippable로 변경됨")
                Text("4. 불필요한 Recomposition 방지!")
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
                    text = "아래 자식 Composable들은 1회만 recompose됩니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stable 파라미터를 받는 Composable들
        // 부모가 recompose되어도 파라미터가 같으면 skip됨!
        StableUserCard(user = user)

        Spacer(modifier = Modifier.height(8.dp))

        products.forEach { product ->
            StableProductCard(product = product)
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gradle 설정 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Compiler Metrics 활성화 (build.gradle.kts)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
composeCompiler {
    reportsDestination = layout.buildDirectory
        .dir("compose_compiler")
    metricsDestination = layout.buildDirectory
        .dir("compose_compiler")
}

// Strong Skipping (Kotlin < 2.0.20)
composeCompiler {
    enableStrongSkippingMode = true
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 수정된 Compiler Report 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "수정 후 Compiler Report (classes.txt)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
stable class StableUserProfile {
    stable val id: Long
    stable val name: String
    stable val email: String
    stable val interests: List<String>
    <runtime stability> = Stable
}

stable class StableProduct {
    stable val id: String
    stable val name: String
    stable val price: Double
    stable val tags: Set<String>
    <runtime stability> = Stable
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
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "수정 후 Compiler Report (composables.txt)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
restartable skippable scheme("[...]")
fun StableUserCard(
    stable user: StableUserProfile
)
// 'skippable' 키워드 추가됨!

restartable skippable scheme("[...]")
fun StableProductCard(
    stable product: StableProduct
)
// 이제 불필요한 recompose 방지
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Strong Skipping Mode 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Strong Skipping Mode (Kotlin 2.0.20+)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Kotlin 2.0.20부터 기본 활성화:
- Unstable 파라미터도 참조 동등성(===)으로 비교
- 같은 인스턴스면 skip 가능
- Lambda 자동 memoization

주의: 같은 참조여도 내용이 바뀌면
UI 업데이트가 안 될 수 있음!
→ 이 경우 @Immutable 또는 ImmutableList 사용
                    """.trimIndent(),
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Android Studio Stability Analyzer 설명
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Android Studio Stability Analyzer",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("설치: Settings > Plugins > Marketplace")
                Text("검색: 'Compose Stability Analyzer'")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Gutter Icon 색상:",
                    style = MaterialTheme.typography.labelMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("  ● ", color = androidx.compose.ui.graphics.Color.Green)
                    Text("녹색: Skippable (최적화됨)")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("  ● ", color = androidx.compose.ui.graphics.Color.Yellow)
                    Text("노란색: Restartable만 (개선 필요)")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("  ● ", color = androidx.compose.ui.graphics.Color.Red)
                    Text("빨간색: Unstable (문제)")
                }
            }
        }
    }
}

/**
 * Stable 파라미터를 받는 Composable
 * - StableUserProfile이 @Immutable이므로 이 함수는 Skippable
 * - 부모가 recompose되어도 파라미터가 같으면 skip됨!
 */
@Composable
fun StableUserCard(user: StableUserProfile) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                    color = MaterialTheme.colorScheme.primary,
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
 * Stable 파라미터를 받는 Composable
 * - StableProduct가 @Immutable이므로 이 함수는 Skippable
 */
@Composable
fun StableProductCard(product: StableProduct) {
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
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
