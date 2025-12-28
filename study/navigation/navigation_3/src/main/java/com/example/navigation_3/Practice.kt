package com.example.navigation_3

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

/**
 * 연습 문제 1: 기본 네비게이션 구현 (난이도: 초급)
 *
 * 요구사항:
 * - Home과 Settings 두 화면 정의
 * - 백스택 생성 (시작: Home)
 * - Home에서 Settings로 이동하는 버튼
 * - Settings에서 뒤로가기 버튼
 *
 * TODO: 주석 처리된 코드를 참고하여 구현하세요!
 */
@Composable
fun Practice1_BasicNavigation() {
    // TODO 1: NavKey 정의 (이미 아래에 정의되어 있음)
    // Hint: data object를 사용하세요

    // TODO 2: 백스택 생성
    // Hint: remember { mutableStateListOf<Any>(P1Home) }
    val backStack = remember { mutableStateListOf<Any>(/* TODO: 시작 화면 */) }

    // TODO 3: 현재 화면에 따른 UI 렌더링
    // Hint: when (backStack.lastOrNull()) { ... }

    Column(modifier = Modifier.fillMaxSize()) {
        // 힌트 카드
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
                    text = "연습 1: 기본 네비게이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. P1Home, P1Settings NavKey는 이미 정의됨")
                Text("2. backStack을 mutableStateListOf로 생성")
                Text("3. backStack.add(P1Settings)로 화면 이동")
                Text("4. backStack.removeLastOrNull()로 뒤로가기")
            }
        }

        // 백스택 상태 표시
        Text(
            text = "현재 백스택: ${backStack.map { it::class.simpleName }}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: 현재 화면 렌더링
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("TODO: when (backStack.lastOrNull())로 화면 분기")
        }

        /*
        // 정답 코드:
        when (backStack.lastOrNull()) {
            is P1Home -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Home 화면", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { backStack.add(P1Settings) }) {
                        Text("Settings로 이동")
                    }
                }
            }
            is P1Settings -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Settings 화면", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { backStack.removeLastOrNull() }) {
                        Text("뒤로가기")
                    }
                }
            }
        }
        */
    }
}

// Practice 1용 NavKey 정의
@Serializable
data object P1Home

@Serializable
data object P1Settings

// ========================================

/**
 * 연습 문제 2: 인자가 있는 네비게이션 (난이도: 중급)
 *
 * 요구사항:
 * - 상품 목록 화면과 상품 상세 화면 구현
 * - 상품 클릭 시 ID와 이름을 함께 전달
 * - 상세 화면에서 전달받은 데이터 표시
 *
 * TODO: 타입 안전 인자 전달을 구현하세요!
 */
@Composable
fun Practice2_NavigationWithArgs() {
    // TODO 1: NavKey 정의 (이미 아래에 정의되어 있음)
    // - P2ProductList: data object
    // - P2ProductDetail: data class with id: Int, name: String

    // TODO 2: 백스택 생성
    val backStack = remember { mutableStateListOf<Any>(P2ProductList) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 힌트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 인자가 있는 네비게이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. P2ProductDetail(id: Int, name: String) 사용")
                Text("2. 상품 클릭 시 backStack.add(P2ProductDetail(id, name))")
                Text("3. 상세 화면에서 key.id, key.name으로 접근")
                Text("4. 타입 캐스팅 없이 바로 사용!")
            }
        }

        // 백스택 상태 표시
        Text(
            text = "현재 백스택: ${backStack.map {
                when (it) {
                    is P2ProductList -> "ProductList"
                    is P2ProductDetail -> "ProductDetail(${it.id})"
                    else -> it::class.simpleName
                }
            }}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: 화면 렌더링
        when (val current = backStack.lastOrNull()) {
            is P2ProductList -> {
                // 상품 목록 화면
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val products = listOf(
                        1 to "Kotlin in Action",
                        2 to "Effective Java",
                        3 to "Clean Architecture"
                    )

                    items(products) { (id, name) ->
                        Card(
                            onClick = {
                                // TODO: 상품 상세로 이동 (id, name 전달)
                                // backStack.add(P2ProductDetail(id, name))
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#$id",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            is P2ProductDetail -> {
                // TODO: 상세 화면 구현
                // current.id, current.name으로 접근 가능!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("TODO: 상품 상세 화면 구현")
                    Text("ID: ${current.id}")
                    Text("Name: ${current.name}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { backStack.removeLastOrNull() }) {
                        Text("뒤로가기")
                    }
                }
            }
        }

        /*
        // 정답 코드 (P2ProductDetail 화면):
        is P2ProductDetail -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Book,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "상품 ID: ${current.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = current.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { backStack.removeLastOrNull() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("뒤로가기")
                }
            }
        }
        */
    }
}

// Practice 2용 NavKey 정의
@Serializable
data object P2ProductList

@Serializable
data class P2ProductDetail(val id: Int, val name: String)

// ========================================

/**
 * 연습 문제 3: 백스택 조작 심화 (난이도: 고급)
 *
 * 요구사항:
 * - 4개 화면 (Home, Category, Product, Cart) 구현
 * - popUpTo: 특정 화면까지 모두 제거
 * - replaceTop: 현재 화면 교체
 * - clearStack: 백스택 초기화
 * - 현재 백스택 상태를 UI에 표시
 *
 * TODO: 고급 백스택 조작 함수를 구현하세요!
 */
@Composable
fun Practice3_AdvancedBackStack() {
    // NavKey들은 이미 정의되어 있음
    val backStack = remember { mutableStateListOf<Any>(P3Home) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 힌트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 백스택 조작 심화",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. popUpTo<T>(): T까지의 화면 제거")
                Text("2. replaceTop(): 현재 화면을 새 화면으로 교체")
                Text("3. clearAndNavigate(): 초기화 후 이동")
                Text("4. 백스택 시각화로 동작 확인!")
            }
        }

        // 백스택 시각화
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "백스택 (${backStack.size}개):",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(backStack.toList()) { key ->
                        Surface(
                            color = when (key) {
                                is P3Home -> MaterialTheme.colorScheme.primary
                                is P3Category -> MaterialTheme.colorScheme.secondary
                                is P3Product -> MaterialTheme.colorScheme.tertiary
                                is P3Cart -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.outline
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = when (key) {
                                    is P3Home -> "Home"
                                    is P3Category -> key.name
                                    is P3Product -> "Product(${key.id})"
                                    is P3Cart -> "Cart"
                                    else -> "?"
                                },
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 네비게이션 버튼들
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "기본 네비게이션",
                style = MaterialTheme.typography.titleSmall
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { backStack.add(P3Category("Electronics")) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Category")
                }
                Button(
                    onClick = { backStack.add(P3Product(id = (1..100).random())) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Product")
                }
                Button(
                    onClick = { backStack.add(P3Cart) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cart")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "고급 백스택 조작",
                style = MaterialTheme.typography.titleSmall
            )

            // Back
            OutlinedButton(
                onClick = { backStack.removeLastOrNull() },
                modifier = Modifier.fillMaxWidth(),
                enabled = backStack.size > 1
            ) {
                Text("Back (removeLastOrNull)")
            }

            // TODO: popUpTo 구현
            OutlinedButton(
                onClick = {
                    // TODO: Home까지 모든 화면 제거 (Home은 유지)
                    // Hint: backStack.popUpToP3<P3Home>(inclusive = false)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = backStack.size > 1
            ) {
                Text("PopUpTo Home (Home 위의 모든 화면 제거)")
            }

            // TODO: replaceTop 구현
            OutlinedButton(
                onClick = {
                    // TODO: 현재 화면을 Cart로 교체
                    // Hint: 마지막 제거 후 Cart 추가
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ReplaceTop with Cart")
            }

            // Clear and Navigate
            OutlinedButton(
                onClick = {
                    // TODO: 백스택 초기화 후 Home으로
                    // Hint: backStack.clear(); backStack.add(P3Home)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Stack & Go Home")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 정답 힌트
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "popUpTo 구현 힌트:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = """
inline fun <reified T> MutableList<Any>.popUpToP3(
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
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }

    /*
    // 정답: popUpTo 함수
    inline fun <reified T> MutableList<Any>.popUpToP3(inclusive: Boolean = false) {
        val index = indexOfLast { it is T }
        if (index >= 0) {
            val removeFrom = if (inclusive) index else index + 1
            while (size > removeFrom) {
                removeAt(lastIndex)
            }
        }
    }

    // 정답: replaceTop 함수
    fun MutableList<Any>.replaceTop(newKey: Any) {
        if (isNotEmpty()) {
            removeAt(lastIndex)
        }
        add(newKey)
    }

    // 정답: 버튼 onClick 구현
    // popUpTo: backStack.popUpToP3<P3Home>(inclusive = false)
    // replaceTop: backStack.replaceTop(P3Cart)
    // clearStack: backStack.clear(); backStack.add(P3Home)
    */
}

// Practice 3용 NavKey 정의
@Serializable
data object P3Home

@Serializable
data class P3Category(val name: String)

@Serializable
data class P3Product(val id: Int)

@Serializable
data object P3Cart

// Helper extension for Practice 3
inline fun <reified T> MutableList<Any>.popUpToP3(inclusive: Boolean = false) {
    val index = indexOfLast { it is T }
    if (index >= 0) {
        val removeFrom = if (inclusive) index else index + 1
        while (size > removeFrom) {
            removeAt(lastIndex)
        }
    }
}

fun MutableList<Any>.replaceTop(newKey: Any) {
    if (isNotEmpty()) {
        removeAt(lastIndex)
    }
    add(newKey)
}
