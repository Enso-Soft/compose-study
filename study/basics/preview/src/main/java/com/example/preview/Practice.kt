package com.example.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.preview.ui.theme.PreviewTheme

/**
 * 연습 문제: @Preview 활용하기
 *
 * 3가지 연습을 통해 Preview를 마스터합니다.
 */

// ===== 연습 1: 기본 Preview 작성 =====

/**
 * 연습 1: 상품 카드의 기본 Preview 작성하기
 *
 * TODO: ProductCard 컴포넌트의 Preview를 작성하세요.
 *
 * 요구사항:
 * - name: "상품 카드"
 * - showBackground: true
 * - widthDp: 320
 * - PreviewTheme 으로 감싸기
 */
@Composable
fun Practice1Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 Preview 작성",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("ProductCard 컴포넌트의 기본 Preview를 작성하세요.")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Preview(
    name = "...",
    showBackground = ...,
    widthDp = ...
)
@Composable
fun ProductCardPreview() {
    PreviewTheme {
        ProductCard(...)
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 대상 컴포넌트
        Text(
            text = "Preview 대상 컴포넌트",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        ProductCard(
            product = Product(
                name = "MacBook Pro 16\"",
                price = 3490000,
                discountPercent = 10
            )
        )

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val requirements = listOf(
                    "1. @Preview 어노테이션 추가",
                    "2. name = \"상품 카드\"",
                    "3. showBackground = true",
                    "4. widthDp = 320",
                    "5. PreviewTheme으로 감싸기"
                )

                requirements.forEach { req ->
                    Text(
                        text = req,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ===== 연습 1 정답 (주석 해제하여 확인) =====

/*
@Preview(
    name = "상품 카드",
    showBackground = true,
    widthDp = 320
)
@Composable
private fun ProductCardPreview() {
    PreviewTheme {
        ProductCard(
            product = Product(
                name = "MacBook Pro 16\"",
                price = 3490000,
                discountPercent = 10
            )
        )
    }
}
*/

// ===== 연습 2: PreviewParameter 사용하기 =====

/**
 * 연습 2: 주문 상태별 Preview 작성하기
 *
 * TODO: OrderStatusProvider를 구현하고 Preview에 적용하세요.
 *
 * 요구사항:
 * - PreviewParameterProvider<OrderStatus> 구현
 * - 3가지 상태 제공: Pending, Shipped, Delivered
 * - @PreviewParameter 로 Preview 함수에 주입
 */
@Composable
fun Practice2Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: PreviewParameter 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("OrderStatusProvider를 구현하여 여러 주문 상태를 한 번에 테스트하세요.")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
class OrderStatusProvider : PreviewParameterProvider<OrderStatus> {
    override val values = sequenceOf(
        // 여기에 상태들 추가
    )
}

@Preview(showBackground = true)
@Composable
fun OrderCardPreview(
    @PreviewParameter(...) status: OrderStatus
) {
    // ...
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 대상 컴포넌트들
        Text(
            text = "3가지 주문 상태",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        OrderStatusCard(status = OrderStatus.Pending)
        OrderStatusCard(status = OrderStatus.Shipped("1234567890"))
        OrderStatusCard(status = OrderStatus.Delivered)

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val requirements = listOf(
                    "1. OrderStatusProvider 클래스 구현",
                    "2. PreviewParameterProvider<OrderStatus> 상속",
                    "3. values에 3가지 상태 제공",
                    "4. @PreviewParameter로 Preview에 적용"
                )

                requirements.forEach { req ->
                    Text(
                        text = req,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ===== 연습 2 정답 (주석 해제하여 확인) =====

/*
class OrderStatusProvider : PreviewParameterProvider<OrderStatus> {
    override val values: Sequence<OrderStatus> = sequenceOf(
        OrderStatus.Pending,
        OrderStatus.Shipped("1234567890"),
        OrderStatus.Delivered
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun OrderCardPreview(
    @PreviewParameter(OrderStatusProvider::class) status: OrderStatus
) {
    PreviewTheme {
        OrderStatusCard(status = status)
    }
}
*/

// ===== 연습 3: Multipreview 만들기 =====

/**
 * 연습 3: Phone/Tablet × Light/Dark 조합의 Multipreview 만들기
 *
 * TODO: 4가지 조합을 테스트하는 Multipreview 어노테이션을 만드세요.
 *
 * 요구사항:
 * - @PhoneTabletPreview 어노테이션 정의 (Phone 360dp, Tablet 800dp)
 * - @LightDarkPreview 어노테이션 정의
 * - 두 어노테이션을 조합하여 사용
 */
@Composable
fun Practice3Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: Multipreview 만들기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Phone/Tablet × Light/Dark = 4가지 조합을 테스트하는 Multipreview를 만드세요.")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 디바이스 크기 Multipreview
@Preview(name = "Phone", widthDp = 360)
@Preview(name = "Tablet", widthDp = 800)
annotation class PhoneTabletPreview

// Light/Dark Multipreview
@Preview(name = "Light", uiMode = ...)
@Preview(name = "Dark", uiMode = ...)
annotation class ThemeModePreview

// 조합 사용
@PhoneTabletPreview
@ThemeModePreview
@Composable
fun MyPreview() { ... }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 결과 시뮬레이션
        Text(
            text = "생성되는 4가지 Preview",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        val combinations = listOf(
            "Light Phone (360dp)",
            "Dark Phone (360dp)",
            "Light Tablet (800dp)",
            "Dark Tablet (800dp)"
        )

        combinations.forEachIndexed { index, name ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (name.contains("Dark"))
                        MaterialTheme.colorScheme.surfaceVariant
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Preview ${index + 1}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val requirements = listOf(
                    "1. @PhoneTabletPreview 어노테이션 정의",
                    "   - Phone: widthDp = 360",
                    "   - Tablet: widthDp = 800",
                    "2. @ThemeModePreview 어노테이션 정의",
                    "   - Light: UI_MODE_NIGHT_NO",
                    "   - Dark: UI_MODE_NIGHT_YES",
                    "3. 두 어노테이션을 함께 사용하여 4개 Preview 생성"
                )

                requirements.forEach { req ->
                    Text(
                        text = req,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ===== 연습 3 정답 (주석 해제하여 확인) =====

/*
@Preview(name = "Phone", widthDp = 360, showBackground = true)
@Preview(name = "Tablet", widthDp = 800, showBackground = true)
annotation class PhoneTabletPreview

@Preview(
    name = "Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
annotation class ThemeModePreview

@PhoneTabletPreview
@ThemeModePreview
@Composable
private fun ResponsiveCardPreview() {
    PreviewTheme {
        ProductCard(
            product = Product(
                name = "테스트 상품",
                price = 50000,
                discountPercent = 20
            )
        )
    }
}
*/

// ===== 연습용 데이터 및 컴포넌트 =====

/**
 * 상품 데이터
 */
data class Product(
    val name: String,
    val price: Int,
    val discountPercent: Int
)

/**
 * 상품 카드 컴포넌트
 */
@Composable
fun ProductCard(product: Product) {
    val discountedPrice = product.price * (100 - product.discountPercent) / 100

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${product.discountPercent}% 할인",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text(
                        text = "₩${"%,d".format(product.price)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "₩${"%,d".format(discountedPrice)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * 주문 상태 sealed class
 */
sealed class OrderStatus {
    object Pending : OrderStatus()
    data class Shipped(val trackingNumber: String) : OrderStatus()
    object Delivered : OrderStatus()
}

/**
 * 주문 상태 카드 컴포넌트
 */
@Composable
fun OrderStatusCard(status: OrderStatus) {
    val (title, description, containerColor) = when (status) {
        is OrderStatus.Pending -> Triple(
            "주문 처리 중",
            "상품을 준비하고 있습니다",
            MaterialTheme.colorScheme.tertiaryContainer
        )
        is OrderStatus.Shipped -> Triple(
            "배송 중",
            "운송장 번호: ${status.trackingNumber}",
            MaterialTheme.colorScheme.secondaryContainer
        )
        is OrderStatus.Delivered -> Triple(
            "배송 완료",
            "상품이 도착했습니다",
            MaterialTheme.colorScheme.primaryContainer
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
