package com.example.compose_preview_testing

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_preview_testing.ui.theme.ComposePreviewTestingTheme

// ============================================
// 연습 문제 1: 기본 PreviewParameterProvider 만들기 (쉬움)
// ============================================

/**
 * # 연습 1: 버튼 상태 Provider 만들기
 *
 * ## 목표
 * 버튼의 3가지 상태(Enabled, Disabled, Loading)를 테스트하는
 * PreviewParameterProvider를 구현하세요.
 *
 * ## 요구사항
 * 1. ButtonState sealed class는 이미 정의되어 있습니다.
 * 2. ButtonStateProvider를 구현하세요.
 * 3. @Preview 함수에서 @PreviewParameter를 사용하세요.
 */

/**
 * 버튼 상태를 나타내는 sealed class
 */
sealed class ButtonState {
    data object Enabled : ButtonState()
    data object Disabled : ButtonState()
    data object Loading : ButtonState()
}

/**
 * 버튼 컴포넌트
 */
@Composable
fun ActionButton(
    state: ButtonState,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        enabled = state == ButtonState.Enabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        when (state) {
            ButtonState.Enabled -> Text("클릭하세요")
            ButtonState.Disabled -> Text("비활성화됨")
            ButtonState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("로딩 중...")
            }
        }
    }
}

// TODO: ButtonStateProvider를 구현하세요!
// class ButtonStateProvider : PreviewParameterProvider<ButtonState> {
//     override val values: Sequence<ButtonState> = sequenceOf(
//         // 여기에 3가지 상태를 추가하세요
//     )
// }

// TODO: Preview 함수를 작성하세요!
// @Preview(showBackground = true)
// @Composable
// private fun PreviewActionButton(
//     @PreviewParameter(ButtonStateProvider::class) state: ButtonState
// ) {
//     ComposePreviewTestingTheme {
//         ActionButton(state = state)
//     }
// }

/* 정답:

class ButtonStateProvider : PreviewParameterProvider<ButtonState> {
    override val values: Sequence<ButtonState> = sequenceOf(
        ButtonState.Enabled,
        ButtonState.Disabled,
        ButtonState.Loading
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewActionButton(
    @PreviewParameter(ButtonStateProvider::class) state: ButtonState
) {
    ComposePreviewTestingTheme {
        ActionButton(state = state)
    }
}

*/

@Composable
fun Practice1_BasicProvider() {
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
                    text = "연습 1: 기본 PreviewParameterProvider 만들기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "버튼의 3가지 상태(Enabled, Disabled, Loading)를 테스트하는 Provider를 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("1. PreviewParameterProvider<ButtonState> 인터페이스를 구현하세요")
                Text("2. values 프로퍼티에서 sequenceOf()로 3가지 상태를 반환하세요")
                Text("3. @Preview 함수에 @PreviewParameter 어노테이션을 추가하세요")
            }
        }

        // 현재 상태 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "버튼 상태 데모",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))

                Text("Enabled 상태:")
                ActionButton(state = ButtonState.Enabled)

                Spacer(Modifier.height(8.dp))
                Text("Disabled 상태:")
                ActionButton(state = ButtonState.Disabled)

                Spacer(Modifier.height(8.dp))
                Text("Loading 상태:")
                ActionButton(state = ButtonState.Loading)
            }
        }

        // 코드 템플릿
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 템플릿",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = """
// Practice.kt 파일에서 아래 코드를 완성하세요

class ButtonStateProvider : PreviewParameterProvider<ButtonState> {
    override val values: Sequence<ButtonState> = sequenceOf(
        // TODO: 3가지 상태 추가
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewActionButton(
    @PreviewParameter(ButtonStateProvider::class) state: ButtonState
) {
    ComposePreviewTestingTheme {
        ActionButton(state = state)
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFD4D4D4),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// ============================================
// 연습 문제 2: CollectionPreviewParameterProvider 사용 (중간)
// ============================================

/**
 * # 연습 2: 제품 카드 Provider 만들기
 *
 * ## 목표
 * CollectionPreviewParameterProvider를 사용하여
 * 다양한 제품 데이터로 제품 카드를 테스트하세요.
 *
 * ## 요구사항
 * 1. Product data class는 이미 정의되어 있습니다.
 * 2. CollectionPreviewParameterProvider를 상속하여 ProductProvider를 구현하세요.
 * 3. 5가지 이상의 다양한 제품 데이터를 제공하세요:
 *    - 기본 제품
 *    - 할인 제품
 *    - 긴 이름 제품
 *    - 무료 제품
 *    - 품절 제품
 */

/**
 * 제품 정보를 담는 데이터 클래스
 */
data class Product(
    val name: String,
    val price: Int,
    val discount: Int? = null,
    val isSoldOut: Boolean = false
)

/**
 * 제품 카드 컴포넌트
 */
@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 제품명
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            // 가격
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (product.discount != null) {
                    Text(
                        text = "${product.price}원",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                    )
                    Text(
                        text = "${product.price * (100 - product.discount) / 100}원",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${product.discount}%",
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                } else {
                    Text(
                        text = if (product.price == 0) "무료" else "${product.price}원",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 품절 표시
            if (product.isSoldOut) {
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "품절",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// TODO: ProductProvider를 구현하세요!
// class ProductProvider : CollectionPreviewParameterProvider<Product>(
//     listOf(
//         // 여기에 5가지 이상의 제품 데이터를 추가하세요
//     )
// )

// TODO: Preview 함수를 작성하세요!

/* 정답:

class ProductProvider : CollectionPreviewParameterProvider<Product>(
    listOf(
        Product("기본 상품", 10000),
        Product("할인 상품", 50000, discount = 30),
        Product("아주아주아주긴이름을가진특별한상품입니다", 99999),
        Product("무료 상품", 0),
        Product("품절 상품", 25000, isSoldOut = true)
    )
)

@Preview(showBackground = true)
@Composable
private fun PreviewProductCard(
    @PreviewParameter(ProductProvider::class) product: Product
) {
    ComposePreviewTestingTheme {
        ProductCard(product = product)
    }
}

*/

@Composable
fun Practice2_CollectionProvider() {
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
                    text = "연습 2: CollectionPreviewParameterProvider 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "CollectionPreviewParameterProvider를 사용하여 다양한 제품 데이터로 제품 카드를 테스트하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("1. CollectionPreviewParameterProvider<Product>(listOf(...))를 상속하세요")
                Text("2. 다양한 엣지 케이스를 포함하세요: 할인, 긴 이름, 무료, 품절 등")
                Text("3. sequenceOf() 대신 listOf()를 직접 사용할 수 있습니다")
            }
        }

        // 현재 상태 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "제품 카드 데모",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))

                ProductCard(Product("기본 상품", 10000))
                Spacer(Modifier.height(8.dp))
                ProductCard(Product("할인 상품", 50000, discount = 30))
                Spacer(Modifier.height(8.dp))
                ProductCard(Product("품절 상품", 25000, isSoldOut = true))
            }
        }

        // 코드 템플릿
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 템플릿",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = """
class ProductProvider : CollectionPreviewParameterProvider<Product>(
    listOf(
        // TODO: 5가지 이상의 제품 데이터 추가
        // 기본 제품, 할인 제품, 긴 이름, 무료, 품절 등
    )
)

@Preview(showBackground = true)
@Composable
private fun PreviewProductCard(
    @PreviewParameter(ProductProvider::class) product: Product
) {
    ComposePreviewTestingTheme {
        ProductCard(product = product)
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFD4D4D4),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// ============================================
// 연습 문제 3: Multipreview + PreviewParameter 조합 (어려움)
// ============================================

/**
 * # 연습 3: 주문 상태 카드 종합 테스트
 *
 * ## 목표
 * Multipreview 어노테이션과 PreviewParameter를 조합하여
 * 다크모드, 폰트 스케일, 여러 상태를 한 번에 테스트하세요.
 *
 * ## 요구사항
 * 1. OrderStatus sealed class는 이미 정의되어 있습니다.
 * 2. 커스텀 Multipreview 어노테이션을 정의하세요:
 *    - Light 모드
 *    - Dark 모드
 *    - Large Font (1.3f)
 * 3. OrderStatusProvider를 구현하세요.
 * 4. Multipreview + PreviewParameter를 조합한 Preview를 작성하세요.
 */

/**
 * 주문 상태를 나타내는 sealed class
 */
sealed class OrderStatus(val label: String, val color: Color) {
    data object Pending : OrderStatus("주문 대기", Color(0xFFFF9800))
    data object Confirmed : OrderStatus("주문 확인", Color(0xFF2196F3))
    data object Shipped : OrderStatus("배송 중", Color(0xFF9C27B0))
    data object Delivered : OrderStatus("배송 완료", Color(0xFF4CAF50))
    data object Cancelled : OrderStatus("주문 취소", Color(0xFFF44336))
}

/**
 * 주문 상태 카드 컴포넌트
 */
@Composable
fun OrderStatusCard(status: OrderStatus) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 상태 아이콘
            Surface(
                color = status.color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = when (status) {
                        OrderStatus.Pending -> Icons.Default.Schedule
                        OrderStatus.Confirmed -> Icons.Default.CheckCircle
                        OrderStatus.Shipped -> Icons.Default.LocalShipping
                        OrderStatus.Delivered -> Icons.Default.Done
                        OrderStatus.Cancelled -> Icons.Default.Cancel
                    },
                    contentDescription = null,
                    tint = status.color,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp)
                )
            }

            // 상태 정보
            Column {
                Text(
                    text = status.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "주문번호: 2024-12345",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.weight(1f))

            // 상태 뱃지
            Surface(
                color = status.color,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = status.label,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }
        }
    }
}

// TODO: 커스텀 Multipreview 어노테이션을 정의하세요!
// @Preview(name = "Light")
// @Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
// @Preview(name = "Large Font", fontScale = 1.3f)
// annotation class OrderCardPreviews

// TODO: OrderStatusProvider를 구현하세요!
// class OrderStatusProvider : PreviewParameterProvider<OrderStatus> {
//     override val values: Sequence<OrderStatus> = sequenceOf(
//         // 5가지 주문 상태 추가
//     )
// }

// TODO: Multipreview + PreviewParameter 조합 Preview를 작성하세요!

/* 정답:

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "Large Font", showBackground = true, fontScale = 1.3f)
annotation class OrderCardPreviews

class OrderStatusProvider : PreviewParameterProvider<OrderStatus> {
    override val values: Sequence<OrderStatus> = sequenceOf(
        OrderStatus.Pending,
        OrderStatus.Confirmed,
        OrderStatus.Shipped,
        OrderStatus.Delivered,
        OrderStatus.Cancelled
    )
}

@OrderCardPreviews
@Composable
private fun PreviewOrderStatusCard(
    @PreviewParameter(OrderStatusProvider::class) status: OrderStatus
) {
    ComposePreviewTestingTheme {
        OrderStatusCard(status = status)
    }
}

// 결과: 5가지 상태 x 3가지 설정 = 15개 Preview 자동 생성!

*/

@Composable
fun Practice3_MultipreviewCombination() {
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
                    text = "연습 3: Multipreview + PreviewParameter 조합",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "커스텀 Multipreview 어노테이션과 PreviewParameter를 조합하여 다크모드, 폰트 스케일, 여러 상태를 한 번에 테스트하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("1. @Preview를 여러 개 붙인 annotation class를 정의하세요")
                Text("2. uiMode = Configuration.UI_MODE_NIGHT_YES로 다크모드 설정")
                Text("3. fontScale = 1.3f로 큰 폰트 설정")
                Text("4. 정의한 어노테이션을 @Composable 함수에 적용하세요")
            }
        }

        // 예상 결과
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예상 결과",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("5가지 상태 x 3가지 설정 = 15개 Preview 자동 생성!")
                Text("코드는 단 1개의 함수!")
            }
        }

        // 현재 상태 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "주문 상태 카드 데모",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))

                OrderStatusCard(OrderStatus.Pending)
                Spacer(Modifier.height(8.dp))
                OrderStatusCard(OrderStatus.Confirmed)
                Spacer(Modifier.height(8.dp))
                OrderStatusCard(OrderStatus.Shipped)
                Spacer(Modifier.height(8.dp))
                OrderStatusCard(OrderStatus.Delivered)
                Spacer(Modifier.height(8.dp))
                OrderStatusCard(OrderStatus.Cancelled)
            }
        }

        // 코드 템플릿
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 템플릿",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = """
import android.content.res.Configuration

// Step 1: 커스텀 Multipreview 어노테이션 정의
@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "Large Font", showBackground = true, fontScale = 1.3f)
annotation class OrderCardPreviews

// Step 2: Provider 구현
class OrderStatusProvider : PreviewParameterProvider<OrderStatus> {
    override val values: Sequence<OrderStatus> = sequenceOf(
        // TODO: 5가지 주문 상태 추가
    )
}

// Step 3: Multipreview + PreviewParameter 조합
@OrderCardPreviews
@Composable
private fun PreviewOrderStatusCard(
    @PreviewParameter(OrderStatusProvider::class) status: OrderStatus
) {
    ComposePreviewTestingTheme {
        OrderStatusCard(status = status)
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFD4D4D4),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
