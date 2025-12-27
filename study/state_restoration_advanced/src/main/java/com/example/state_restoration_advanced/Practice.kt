package com.example.state_restoration_advanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * # Practice: 상태 복원 심화 연습
 *
 * 3개의 연습 문제를 통해 Saver 구현을 직접 연습합니다.
 */

// ========== 연습 1: mapSaver로 SearchQuery 저장 ==========

data class SearchQuery(
    val query: String = "",
    val category: String = "all",
    val maxResults: Int = 10,
    val includeArchived: Boolean = false
)

// TODO: SearchQuerySaver를 mapSaver로 구현하세요
// 힌트: mapSaver(save = { ... }, restore = { ... })
val SearchQuerySaver: Saver<SearchQuery, Any> = run {
    // TODO: 여기에 키 상수들을 정의하세요
    // val queryKey = "query"
    // ...

    mapSaver(
        save = { searchQuery ->
            // TODO: SearchQuery를 Map으로 변환하세요
            // 힌트: mapOf("key" to value, ...)
            mapOf<String, Any?>() // 이 줄을 교체하세요
        },
        restore = { map ->
            // TODO: Map에서 SearchQuery로 복원하세요
            // 힌트: map["key"] as Type
            SearchQuery() // 이 줄을 교체하세요
        }
    )
}

/* 정답 (주석 해제하여 확인):
val SearchQuerySaverAnswer: Saver<SearchQuery, Any> = run {
    val queryKey = "query"
    val categoryKey = "category"
    val maxResultsKey = "maxResults"
    val includeArchivedKey = "includeArchived"

    mapSaver(
        save = { searchQuery ->
            mapOf(
                queryKey to searchQuery.query,
                categoryKey to searchQuery.category,
                maxResultsKey to searchQuery.maxResults,
                includeArchivedKey to searchQuery.includeArchived
            )
        },
        restore = { map ->
            SearchQuery(
                query = map[queryKey] as String,
                category = map[categoryKey] as String,
                maxResults = map[maxResultsKey] as Int,
                includeArchived = map[includeArchivedKey] as Boolean
            )
        }
    )
}
*/

@Composable
fun Practice1_SearchScreen() {
    // TODO: rememberSaveable과 SearchQuerySaver를 사용하여 상태를 선언하세요
    // var searchQuery by rememberSaveable(saver = SearchQuerySaver) {
    //     mutableStateOf(SearchQuery())
    // }

    // 임시 구현 (위 TODO 완성 후 삭제)
    var searchQuery by remember { mutableStateOf(SearchQuery()) }

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
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "연습 1: mapSaver 구현하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "SearchQuery 데이터 클래스에 대한 mapSaver를 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "난이도: 초급",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = """
1. mapSaver는 두 개의 람다가 필요합니다:
   - save: 객체 -> Map 변환
   - restore: Map -> 객체 복원

2. 키 상수를 정의하면 오타 방지에 도움됩니다

3. Boolean, Int도 Bundle에 저장 가능합니다
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // 검색 폼
        Text(
            text = "검색 설정",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = searchQuery.query,
            onValueChange = { searchQuery = searchQuery.copy(query = it) },
            label = { Text("검색어") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 카테고리 선택
        Text("카테고리", style = MaterialTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("all", "image", "video", "audio").forEach { category ->
                FilterChip(
                    selected = searchQuery.category == category,
                    onClick = { searchQuery = searchQuery.copy(category = category) },
                    label = { Text(category) }
                )
            }
        }

        // 최대 결과 수
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("최대 결과 수: ${searchQuery.maxResults}")
            Row {
                TextButton(
                    onClick = {
                        if (searchQuery.maxResults > 10) {
                            searchQuery = searchQuery.copy(maxResults = searchQuery.maxResults - 10)
                        }
                    }
                ) { Text("-10") }
                TextButton(
                    onClick = {
                        searchQuery = searchQuery.copy(maxResults = searchQuery.maxResults + 10)
                    }
                ) { Text("+10") }
            }
        }

        // 아카이브 포함
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("아카이브된 항목 포함")
            Switch(
                checked = searchQuery.includeArchived,
                onCheckedChange = { searchQuery = searchQuery.copy(includeArchived = it) }
            )
        }

        // 현재 상태
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "현재 검색 설정",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("검색어: ${searchQuery.query.ifBlank { "(없음)" }}")
                Text("카테고리: ${searchQuery.category}")
                Text("최대 결과: ${searchQuery.maxResults}")
                Text("아카이브 포함: ${if (searchQuery.includeArchived) "예" else "아니오"}")
            }
        }
    }
}

// ========== 연습 2: listSaver로 ShoppingCart 저장 ==========

data class CartItem(
    val name: String,
    val price: Double,
    val quantity: Int
)

data class ShoppingCart(
    val items: List<CartItem> = emptyList(),
    val couponCode: String = "",
    val usePoints: Boolean = false
)

// TODO: ShoppingCartSaver를 listSaver로 구현하세요
// 힌트: 중첩된 List는 각 아이템도 List로 변환해야 합니다
val ShoppingCartSaver: Saver<ShoppingCart, Any> = listSaver(
    save = { cart ->
        // TODO: ShoppingCart를 List로 변환하세요
        // 힌트: items는 List<List<Any?>>로 변환 필요
        listOf<Any?>() // 이 줄을 교체하세요
    },
    restore = { list ->
        // TODO: List에서 ShoppingCart로 복원하세요
        ShoppingCart() // 이 줄을 교체하세요
    }
)

/* 정답 (주석 해제하여 확인):
val ShoppingCartSaverAnswer: Saver<ShoppingCart, Any> = listSaver(
    save = { cart ->
        listOf(
            // items를 List<List<Any?>>로 변환
            cart.items.map { item ->
                listOf(item.name, item.price, item.quantity)
            },
            cart.couponCode,
            cart.usePoints
        )
    },
    restore = { list ->
        @Suppress("UNCHECKED_CAST")
        val itemsList = list[0] as List<List<Any?>>
        ShoppingCart(
            items = itemsList.map { itemData ->
                CartItem(
                    name = itemData[0] as String,
                    price = itemData[1] as Double,
                    quantity = itemData[2] as Int
                )
            },
            couponCode = list[1] as String,
            usePoints = list[2] as Boolean
        )
    }
)
*/

@Composable
fun Practice2_ShoppingCartScreen() {
    // TODO: rememberSaveable과 ShoppingCartSaver를 사용하여 상태를 선언하세요
    var cart by remember { mutableStateOf(ShoppingCart()) }
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }

    val totalPrice = cart.items.sumOf { it.price * it.quantity }

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
                containerColor = Color(0xFFFFF3E0)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "연습 2: listSaver로 중첩 객체 저장",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ShoppingCart 내부의 List<CartItem>도 함께 저장하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "난이도: 중급",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE65100)
                )
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = """
1. 중첩된 객체 리스트는 List<List<Any?>>로 변환

2. save에서:
   items.map { listOf(it.name, it.price, it.quantity) }

3. restore에서:
   (list[0] as List<List<Any?>>).map { ... }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // 상품 추가
        Text(
            text = "장바구니",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("상품명") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = itemPrice,
                onValueChange = { itemPrice = it },
                label = { Text("가격") },
                modifier = Modifier.weight(0.5f),
                singleLine = true
            )
        }

        Button(
            onClick = {
                val price = itemPrice.toDoubleOrNull()
                if (itemName.isNotBlank() && price != null) {
                    cart = cart.copy(
                        items = cart.items + CartItem(itemName, price, 1)
                    )
                    itemName = ""
                    itemPrice = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("장바구니에 추가")
        }

        // 장바구니 목록
        if (cart.items.isNotEmpty()) {
            Card {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cart.items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.name, fontWeight = FontWeight.Bold)
                                Text(
                                    "${item.price}원 x ${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (item.quantity > 1) {
                                            val newItems = cart.items.toMutableList()
                                            newItems[index] = item.copy(quantity = item.quantity - 1)
                                            cart = cart.copy(items = newItems)
                                        }
                                    }
                                ) { Text("-") }
                                Text("${item.quantity}")
                                IconButton(
                                    onClick = {
                                        val newItems = cart.items.toMutableList()
                                        newItems[index] = item.copy(quantity = item.quantity + 1)
                                        cart = cart.copy(items = newItems)
                                    }
                                ) { Text("+") }
                                TextButton(
                                    onClick = {
                                        cart = cart.copy(
                                            items = cart.items.filterIndexed { i, _ -> i != index }
                                        )
                                    }
                                ) {
                                    Text("삭제", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                        if (index < cart.items.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        } else {
            Text(
                text = "장바구니가 비어있습니다",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 쿠폰 코드
        OutlinedTextField(
            value = cart.couponCode,
            onValueChange = { cart = cart.copy(couponCode = it) },
            label = { Text("쿠폰 코드") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 포인트 사용
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("포인트 사용")
            Switch(
                checked = cart.usePoints,
                onCheckedChange = { cart = cart.copy(usePoints = it) }
            )
        }

        // 합계
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "총 ${cart.items.size}개 상품",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "합계: ${String.format("%,.0f", totalPrice)}원",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ========== 연습 3: SaveableStateHolder로 탭 상태 관리 ==========

@Composable
fun Practice3_TabStateScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    // TODO: rememberSaveableStateHolder()를 사용하여 각 탭의 상태를 독립적으로 유지하세요
    // val stateHolder = rememberSaveableStateHolder()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "연습 3: SaveableStateHolder 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "각 탭에서 입력한 값이 탭 전환 후에도 유지되도록 하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "난이도: 고급",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2E7D32)
                )
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = """
1. rememberSaveableStateHolder()로 stateHolder 생성

2. stateHolder.SaveableStateProvider(key) { ... }로 감싸기

3. 각 탭마다 고유한 key 사용 (예: "tab_0", "tab_1")

4. SaveableStateProvider 내부에서 rememberSaveable 사용
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 탭
        val tabs = listOf("개인정보", "주소", "결제")
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // 탭 콘텐츠
        // TODO: stateHolder.SaveableStateProvider를 사용하여 각 탭을 감싸세요
        when (selectedTab) {
            0 -> TabContent1()
            1 -> TabContent2()
            2 -> TabContent3()
        }

        /* 정답 예시:
        val stateHolder = rememberSaveableStateHolder()

        stateHolder.SaveableStateProvider(key = "tab_$selectedTab") {
            when (selectedTab) {
                0 -> TabContent1()
                1 -> TabContent2()
                2 -> TabContent3()
            }
        }
        */
    }
}

@Composable
private fun TabContent1() {
    // SaveableStateProvider 안에서 rememberSaveable 사용하면 탭 전환 후에도 유지됨
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "개인정보 입력",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("전화번호") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "탭을 전환했다가 돌아와보세요. 값이 유지되나요?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TabContent2() {
    var address by rememberSaveable { mutableStateOf("") }
    var zipCode by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "주소 입력",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("주소") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = zipCode,
            onValueChange = { zipCode = it },
            label = { Text("우편번호") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TabContent3() {
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "결제 정보",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("카드 번호") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = expiry,
            onValueChange = { expiry = it },
            label = { Text("유효기간 (MM/YY)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
