package com.example.derived_state_of

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 스크롤 기반 헤더 축소
 *
 * 요구사항:
 * - 스크롤 위치에 따라 헤더 크기 상태 결정
 * - firstVisibleItemScrollOffset > 100 이면 "축소" 상태
 * - derivedStateOf를 사용해서 최적화
 *
 * TODO: derivedStateOf를 사용해서 구현하세요!
 */
@Composable
fun Practice1_ScrollHeaderScreen() {
    val listState = rememberLazyListState()
    var recompositionCount by remember { mutableIntStateOf(0) }

    // TODO: derivedStateOf를 사용해서 isCompact 상태를 정의하세요
    // 힌트: listState.firstVisibleItemScrollOffset > 100 조건 사용
    val isCompact = false  // 이 줄을 수정하세요!

    /*
    val isCompact by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset > 100
        }
    }
    */

    SideEffect {
        recompositionCount++
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 연습 문제 설명
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 스크롤 헤더",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TODO: derivedStateOf를 사용해서\n스크롤 오프셋 > 100일 때 isCompact = true",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 헤더 (축소/확대)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(if (isCompact) 40.dp else 80.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isCompact)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCompact) "축소 헤더" else "확대 헤더",
                    style = if (isCompact)
                        MaterialTheme.typography.bodyMedium
                    else
                        MaterialTheme.typography.headlineSmall
                )
            }
        }

        // 상태 표시
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Recomposition: $recompositionCount")
            Text("Offset: ${listState.firstVisibleItemScrollOffset}")
            Text("Compact: $isCompact")
        }

        // 스크롤 리스트
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(50) { index ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "아이템 ${index + 1}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 2: 장바구니 총액 계산
 *
 * 요구사항:
 * - 각 상품의 수량을 변경할 수 있음
 * - 총액은 수량이 변해도 결과가 같을 수 있음 (예: +1, -1 연속)
 * - derivedStateOf로 총액 계산 최적화
 *
 * TODO: derivedStateOf를 사용해서 구현하세요!
 */
@Composable
fun Practice2_CartTotalScreen() {
    var quantity1 by remember { mutableIntStateOf(1) }
    var quantity2 by remember { mutableIntStateOf(1) }
    var quantity3 by remember { mutableIntStateOf(1) }
    var recompositionCount by remember { mutableIntStateOf(0) }

    val price1 = 10000
    val price2 = 25000
    val price3 = 15000

    // TODO: derivedStateOf를 사용해서 totalPrice를 계산하세요
    // 힌트: quantity1 * price1 + quantity2 * price2 + quantity3 * price3
    val totalPrice = 0  // 이 줄을 수정하세요!

    /*
    val totalPrice by remember {
        derivedStateOf {
            quantity1 * price1 + quantity2 * price2 + quantity3 * price3
        }
    }
    */

    SideEffect {
        recompositionCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 장바구니 총액",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TODO: derivedStateOf를 사용해서\n총액 계산을 최적화하세요",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상품 1
        CartItemRow(
            name = "상품 A",
            price = price1,
            quantity = quantity1,
            onQuantityChange = { quantity1 = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 상품 2
        CartItemRow(
            name = "상품 B",
            price = price2,
            quantity = quantity2,
            onQuantityChange = { quantity2 = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 상품 3
        CartItemRow(
            name = "상품 C",
            price = price3,
            quantity = quantity3,
            onQuantityChange = { quantity3 = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 총액 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "총액: ${"%,d".format(totalPrice)}원",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("Recomposition 횟수: $recompositionCount")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = "+1 후 -1 하면 총액은 같지만\nderivdStateOf 없으면 Recomposition 2번",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun CartItemRow(
    name: String,
    price: Int,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(name, style = MaterialTheme.typography.titleMedium)
                Text("${"%,d".format(price)}원", style = MaterialTheme.typography.bodySmall)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("-")
                }
                Text(
                    text = "$quantity",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    onClick = { onQuantityChange(quantity + 1) },
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+")
                }
            }
        }
    }
}

/**
 * 연습 문제 3: 폼 유효성 검사
 *
 * 요구사항:
 * - 이메일과 비밀번호 입력 필드
 * - 이메일: @ 포함, 비밀번호: 8자 이상
 * - 둘 다 유효할 때만 버튼 활성화
 * - derivedStateOf로 버튼 활성화 상태 최적화
 *
 * TODO: derivedStateOf를 사용해서 구현하세요!
 */
@Composable
fun Practice3_FormValidationScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var recompositionCount by remember { mutableIntStateOf(0) }

    // TODO: derivedStateOf를 사용해서 isFormValid를 계산하세요
    // 힌트: email.contains("@") && password.length >= 8
    val isFormValid = false  // 이 줄을 수정하세요!

    /*
    val isFormValid by remember {
        derivedStateOf {
            email.contains("@") && password.length >= 8
        }
    }
    */

    SideEffect {
        recompositionCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 연습 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 폼 유효성 검사",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TODO: derivedStateOf를 사용해서\n이메일(@포함) + 비밀번호(8자↑) 검증",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 이메일 입력
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth(),
            isError = email.isNotEmpty() && !email.contains("@"),
            supportingText = {
                if (email.isNotEmpty() && !email.contains("@")) {
                    Text("@를 포함해야 합니다")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 비밀번호 입력
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            modifier = Modifier.fillMaxWidth(),
            isError = password.isNotEmpty() && password.length < 8,
            supportingText = {
                if (password.isNotEmpty() && password.length < 8) {
                    Text("8자 이상 입력하세요 (현재 ${password.length}자)")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 제출 버튼
        Button(
            onClick = { /* 제출 로직 */ },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isFormValid) "제출하기" else "입력을 완료하세요")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recomposition 횟수: $recompositionCount")
                Text("isFormValid: $isFormValid")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: 타이핑 중에는 isFormValid가\nfalse로 계속 같을 수 있음",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("정답 코드:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
val isFormValid by remember {
    derivedStateOf {
        email.contains("@") &&
        password.length >= 8
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
