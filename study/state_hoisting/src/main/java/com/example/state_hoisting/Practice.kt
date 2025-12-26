package com.example.state_hoisting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: Stateful을 Stateless로 변환
 *
 * 목표: 내부 상태를 가진 컴포넌트를 State Hoisting 적용
 * 시나리오: 좋아요 토글 버튼을 Stateless로 변환
 *
 * TODO: StatelessLikeButton을 완성하세요.
 */
@Composable
fun Practice1_ConvertToStateless() {
    // 부모에서 상태 관리
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(42) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: Stateful을 Stateless로 변환",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "StatelessLikeButton 컴포넌트를 완성하세요.\n" +
                            "내부 상태 대신 매개변수와 콜백을 사용합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Stateless Like Button 사용
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isLiked) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "좋아요 수: $likeCount",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Stateless Like Button 사용
                StatelessLikeButton(
                    isLiked = isLiked,
                    onLikeToggle = { newState ->
                        isLiked = newState
                        likeCount += if (newState) 1 else -1
                    }
                )
            }
        }

        // 부모에서 제어 가능함을 보여주는 버튼들
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "부모에서 제어 가능!",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = {
                        isLiked = true
                        likeCount = 100
                    }) {
                        Text("100 좋아요로 설정")
                    }
                    OutlinedButton(onClick = {
                        isLiked = false
                        likeCount = 0
                    }) {
                        Text("리셋")
                    }
                }
            }
        }

        // 코드 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트: Stateless 컴포넌트 구조",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun StatelessLikeButton(
    isLiked: Boolean,           // State down
    onLikeToggle: (Boolean) -> Unit  // Event up
) {
    IconButton(onClick = { onLikeToggle(!isLiked) }) {
        Icon(
            imageVector = if (isLiked) 
                Icons.Default.Favorite 
            else 
                Icons.Default.FavoriteBorder,
            // ...
        )
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Stateless Like Button
 * TODO: 이 컴포넌트를 완성하세요
 */
@Composable
fun StatelessLikeButton(
    isLiked: Boolean,
    onLikeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // === 정답 ===
    IconButton(
        onClick = { onLikeToggle(!isLiked) },
        modifier = modifier.size(64.dp)
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isLiked) "좋아요 취소" else "좋아요",
            modifier = Modifier.size(48.dp),
            tint = if (isLiked) Color.Red else Color.Gray
        )
    }
}

/**
 * 연습 문제 2: 상태 공유하기
 *
 * 목표: 여러 컴포넌트가 동일한 상태를 공유
 * 시나리오: 이름 입력 + 인사 메시지 + 글자 수 표시
 *
 * TODO: NameInput, Greeting, CharCount가 같은 상태를 사용하도록 구현
 */
@Composable
fun Practice2_ShareState() {
    // 공유할 상태
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 상태 공유하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "세 개의 컴포넌트가 동일한 'name' 상태를 공유합니다.\n" +
                            "State Hoisting으로 모두 동기화됩니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 입력 컴포넌트
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("입력", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                NameInput(
                    name = name,
                    onNameChange = { name = it }
                )
            }
        }

        // 인사 메시지 컴포넌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("인사 메시지", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Greeting(name = name)
            }
        }

        // 글자 수 표시 컴포넌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("글자 수", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                CharCount(name = name)
            }
        }

        // 부모에서 제어
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "부모에서 제어",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { name = "Compose" }) {
                        Text("'Compose'로 설정")
                    }
                    OutlinedButton(onClick = { name = "" }) {
                        Text("초기화")
                    }
                }
            }
        }
    }
}

@Composable
fun NameInput(
    name: String,
    onNameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("이름을 입력하세요") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun Greeting(name: String) {
    Text(
        text = if (name.isNotBlank()) "안녕하세요, ${name}님!" else "이름을 입력해주세요",
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CharCount(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${name.length}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text("글자 수", style = MaterialTheme.typography.bodySmall)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${name.filter { !it.isWhitespace() }.length}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text("공백 제외", style = MaterialTheme.typography.bodySmall)
        }
    }
}

/**
 * 연습 문제 3: 상태 홀더 만들기
 *
 * 목표: 복잡한 상태를 클래스로 캡슐화
 * 시나리오: 쇼핑 카트 상태 관리
 *
 * TODO: CartState 클래스와 CartItem, CartSummary 컴포넌트를 구현
 */
@Composable
fun Practice3_StateHolder() {
    val cartState = rememberCartState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 상태 홀더 만들기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "쇼핑 카트 상태를 CartState 클래스로 관리합니다.\n" +
                            "상품 수량, 총 가격, 할인 등을 캡슐화합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 상품 목록
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "상품 목록",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                CartItemRow(
                    name = "맥북 프로",
                    price = 2500000,
                    quantity = cartState.macbookQuantity,
                    onIncrease = { cartState.macbookQuantity++ },
                    onDecrease = { if (cartState.macbookQuantity > 0) cartState.macbookQuantity-- }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                CartItemRow(
                    name = "아이패드",
                    price = 1200000,
                    quantity = cartState.ipadQuantity,
                    onIncrease = { cartState.ipadQuantity++ },
                    onDecrease = { if (cartState.ipadQuantity > 0) cartState.ipadQuantity-- }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                CartItemRow(
                    name = "에어팟",
                    price = 350000,
                    quantity = cartState.airpodsQuantity,
                    onIncrease = { cartState.airpodsQuantity++ },
                    onDecrease = { if (cartState.airpodsQuantity > 0) cartState.airpodsQuantity-- }
                )
            }
        }

        // 장바구니 요약
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "주문 요약",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("총 상품 수")
                    Text("${cartState.totalItems}개")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("상품 금액")
                    Text("${String.format("%,d", cartState.subtotal)}원")
                }

                if (cartState.hasDiscount) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("할인 (10%)", color = Color.Red)
                        Text("-${String.format("%,d", cartState.discount)}원", color = Color.Red)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "총 결제 금액",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${String.format("%,d", cartState.totalPrice)}원",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (cartState.hasDiscount) {
                    Text(
                        text = "300만원 이상 구매로 10% 할인이 적용되었습니다!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                } else if (cartState.subtotal > 0) {
                    val remaining = 3000000 - cartState.subtotal
                    Text(
                        text = "${String.format("%,d", remaining)}원 더 구매하시면 10% 할인!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { cartState.clear() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("장바구니 비우기")
                    }
                    Button(
                        onClick = { /* 주문하기 */ },
                        modifier = Modifier.weight(1f),
                        enabled = cartState.totalItems > 0
                    ) {
                        Text("주문하기")
                    }
                }
            }
        }

        // 코드 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CartState 클래스 구조",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
class CartState {
    var macbookQuantity by mutableIntStateOf(0)
    var ipadQuantity by mutableIntStateOf(0)
    var airpodsQuantity by mutableIntStateOf(0)
    
    val totalItems: Int
        get() = macbookQuantity + ipadQuantity + airpodsQuantity
    
    val subtotal: Long
        get() = (macbookQuantity * 2500000L) + 
                (ipadQuantity * 1200000L) + 
                (airpodsQuantity * 350000L)
    
    val hasDiscount: Boolean
        get() = subtotal >= 3000000
    
    val discount: Long
        get() = if (hasDiscount) (subtotal * 0.1).toLong() else 0
    
    val totalPrice: Long
        get() = subtotal - discount
    
    fun clear() { /* 수량 초기화 */ }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun CartItemRow(
    name: String,
    price: Int,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(
                "${String.format("%,d", price)}원",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = onDecrease,
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-")
            }
            Text(
                text = "$quantity",
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            FilledTonalButton(
                onClick = onIncrease,
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+")
            }
        }
    }
}

/**
 * 장바구니 상태 홀더
 */
class CartState {
    var macbookQuantity by mutableIntStateOf(0)
    var ipadQuantity by mutableIntStateOf(0)
    var airpodsQuantity by mutableIntStateOf(0)

    val totalItems: Int
        get() = macbookQuantity + ipadQuantity + airpodsQuantity

    val subtotal: Long
        get() = (macbookQuantity * 2500000L) +
                (ipadQuantity * 1200000L) +
                (airpodsQuantity * 350000L)

    val hasDiscount: Boolean
        get() = subtotal >= 3000000

    val discount: Long
        get() = if (hasDiscount) (subtotal * 0.1).toLong() else 0

    val totalPrice: Long
        get() = subtotal - discount

    fun clear() {
        macbookQuantity = 0
        ipadQuantity = 0
        airpodsQuantity = 0
    }
}

@Composable
fun rememberCartState(): CartState = remember { CartState() }
