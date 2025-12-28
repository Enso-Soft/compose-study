package com.example.produce_state

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// ============================================================
// 연습 1: 기초 타이머
// ============================================================

/**
 * 연습 1: 기초 타이머
 *
 * TODO: produceState를 사용해 1초마다 증가하는 타이머를 구현하세요.
 *
 * 힌트:
 * - produceState(initialValue = 0) 사용
 * - while (true) { delay(1000); value += 1 } 패턴
 */
@Composable
fun Practice1_TimerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 1: 기초 타이머",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: 여기에 타이머를 구현하세요
        // 현재는 임시로 0초를 표시합니다
        val seconds = 0  // <- 이 부분을 produceState로 변경!

        Text(
            text = "${seconds}초",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        HintCard(
            hints = listOf(
                "produceState(initialValue = 0) 사용",
                "while (true) 무한 루프",
                "delay(1000)으로 1초 대기",
                "value += 1로 값 증가"
            )
        )

        /*
        // ===== 정답 코드 =====
        val seconds by produceState(initialValue = 0) {
            while (true) {
                delay(1000)
                value += 1
            }
        }

        Text(
            text = "${seconds}초",
            style = MaterialTheme.typography.displayLarge
        )
        // ====================
        */
    }
}

// ============================================================
// 연습 2: 데이터 로딩 with Result
// ============================================================

/**
 * 상품 데이터 클래스
 */
data class Product(
    val id: String,
    val name: String,
    val price: Int
)

/**
 * 가짜 상품 API
 */
object FakeProductApi {
    suspend fun getProducts(category: String): List<Product> {
        delay(1500) // 네트워크 시뮬레이션

        if (category == "error") {
            throw Exception("상품을 불러올 수 없습니다")
        }

        return when (category) {
            "electronics" -> listOf(
                Product("1", "스마트폰", 1000000),
                Product("2", "노트북", 1500000),
                Product("3", "태블릿", 800000)
            )
            "clothing" -> listOf(
                Product("4", "티셔츠", 30000),
                Product("5", "청바지", 80000),
                Product("6", "자켓", 150000)
            )
            else -> listOf(
                Product("7", "기타 상품 1", 10000),
                Product("8", "기타 상품 2", 20000)
            )
        }
    }
}

/**
 * 연습 2: 상품 목록 로딩
 *
 * TODO: produceState와 UiState를 사용해 상품 목록을 로딩하세요.
 *       카테고리 변경 시 재로딩되어야 합니다.
 *
 * 힌트:
 * - produceState<UiState<List<Product>>>(UiState.Loading, key1 = category)
 * - try-catch로 에러 처리
 * - FakeProductApi.getProducts(category) 사용
 */
@Composable
fun Practice2_ProductListScreen() {
    var category by remember { mutableStateOf("electronics") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: 상품 목록 로딩",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 카테고리 선택 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = category == "electronics",
                onClick = { category = "electronics" },
                label = { Text("전자제품") }
            )
            FilterChip(
                selected = category == "clothing",
                onClick = { category = "clothing" },
                label = { Text("의류") }
            )
            FilterChip(
                selected = category == "error",
                onClick = { category = "error" },
                label = { Text("에러") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: produceState로 상품 목록 로딩
        // 현재는 임시로 빈 목록을 표시합니다
        val productsState: UiState<List<Product>> = UiState.Loading  // <- 변경 필요!

        when (productsState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Text(
                    text = productsState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is UiState.Success -> {
                LazyColumn {
                    items(productsState.data) { product ->
                        ProductItem(product)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        HintCard(
            hints = listOf(
                "produceState<UiState<List<Product>>>(...)",
                "initialValue = UiState.Loading",
                "key1 = category로 재시작 조건",
                "try-catch로 Success/Error 분기"
            )
        )

        /*
        // ===== 정답 코드 =====
        val productsState by produceState<UiState<List<Product>>>(
            initialValue = UiState.Loading,
            key1 = category
        ) {
            value = try {
                UiState.Success(FakeProductApi.getProducts(category))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Unknown error")
            }
        }
        // ====================
        */
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(product.name)
            Text(
                text = "${product.price}원",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ============================================================
// 연습 3: 카운트다운 with awaitDispose
// ============================================================

/**
 * 연습 3: 카운트다운 타이머
 *
 * TODO: 10초 카운트다운을 구현하고, Composable 이탈 시 로그를 출력하세요.
 *
 * 학습 목표:
 * - awaitDispose의 목적 이해: Composable이 Composition에서 벗어날 때 리소스 정리
 * - 예: 콜백 해제, 리스너 제거, 로깅 등
 *
 * 힌트:
 * - produceState(initialValue = 10)
 * - while (value > 0) 조건부 루프
 * - awaitDispose { Log.d(...) } 로 정리
 *   - awaitDispose는 Composable이 화면에서 사라질 때 호출됩니다
 *   - "카운트다운 숨기기" 버튼을 눌러 Logcat에서 정리 로그를 확인하세요
 */
@Composable
fun Practice3_CountdownScreen() {
    var isVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: 카운트다운",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 카운트다운 보이기/숨기기 토글
        Button(onClick = { isVisible = !isVisible }) {
            Text(if (isVisible) "카운트다운 숨기기" else "카운트다운 보이기")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isVisible) {
            CountdownDisplay()
        } else {
            Text("카운트다운이 숨겨졌습니다. Logcat에서 정리 로그를 확인하세요!")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        HintCard(
            hints = listOf(
                "produceState(initialValue = 10)",
                "while (value > 0) 조건부 루프",
                "delay(1000) 후 value -= 1",
                "awaitDispose { Log.d(\"Practice3\", \"정리됨\") }"
            )
        )
    }
}

@Composable
fun CountdownDisplay() {
    // TODO: produceState로 카운트다운 구현
    val countdown = 10  // <- produceState로 변경!

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (countdown <= 3 && countdown > 0) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (countdown > 0) "$countdown" else "완료!",
                style = MaterialTheme.typography.displayLarge,
                color = if (countdown <= 3 && countdown > 0) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )

            if (countdown > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("카운트다운 중...")
            }
        }
    }

    /*
    // ===== 정답 코드 =====
    val countdown by produceState(initialValue = 10) {
        while (value > 0) {
            delay(1000)
            value -= 1
        }

        awaitDispose {
            Log.d("Practice3", "카운트다운이 정리되었습니다")
        }
    }
    // ====================
    */
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
fun HintCard(hints: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "힌트",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            hints.forEach { hint ->
                Text("- $hint", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
