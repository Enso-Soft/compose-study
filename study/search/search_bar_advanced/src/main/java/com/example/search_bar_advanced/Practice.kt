package com.example.search_bar_advanced

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * 연습 문제 1: 기본 SearchBar + 검색 결과 표시
 *
 * 요구사항:
 * - SearchBar 컴포넌트 구현
 * - 더미 데이터에서 검색어로 필터링
 * - 결과를 LazyColumn으로 표시
 * - 검색어가 비어있을 때는 전체 목록 표시
 *
 * TODO: SearchBar를 사용해서 구현하세요!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicSearchBar() {
    val items = listOf(
        "Apple", "Apricot", "Avocado",
        "Banana", "Blueberry", "Blackberry",
        "Cherry", "Coconut", "Cranberry",
        "Date", "Dragonfruit", "Durian",
        "Elderberry", "Fig", "Grape"
    )

    // TODO 1: query 상태 선언
    // 힌트: var query by remember { mutableStateOf("") }

    // TODO 2: expanded 상태 선언 (rememberSaveable 사용)
    // 힌트: var expanded by rememberSaveable { mutableStateOf(false) }

    // TODO 3: 필터링된 아이템 계산
    // 힌트: remember(query) { items.filter { ... } }

    /*
    // 정답:
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val filteredItems = remember(query) {
        if (query.isEmpty()) items
        else items.filter { it.lowercase().contains(query.lowercase()) }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 기본 SearchBar",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: SearchBar를 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 4: SearchBar 구현
        // 힌트:
        // SearchBar(
        //     inputField = {
        //         SearchBarDefaults.InputField(
        //             query = query,
        //             onQueryChange = { query = it },
        //             onSearch = { expanded = false },
        //             expanded = expanded,
        //             onExpandedChange = { expanded = it },
        //             placeholder = { Text("과일 검색...") },
        //             leadingIcon = { Icon(Icons.Default.Search, "검색") },
        //             trailingIcon = { if (query.isNotEmpty()) ... }
        //         )
        //     },
        //     expanded = expanded,
        //     onExpandedChange = { expanded = it }
        // ) {
        //     // content: LazyColumn으로 필터링된 결과 표시
        // }

        /*
        // 정답:
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("과일 검색...") },
                    leadingIcon = { Icon(Icons.Default.Search, "검색") },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Close, "지우기")
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(filteredItems) { item ->
                    ListItem(
                        headlineContent = { Text(item) },
                        modifier = Modifier.clickable {
                            query = item
                            expanded = false
                        }
                    )
                }
            }
        }
        */

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. SearchBar의 inputField에 SearchBarDefaults.InputField 사용")
                Text("2. expanded와 onExpandedChange로 확장 상태 관리")
                Text("3. content 람다에서 LazyColumn으로 결과 표시")
                Text("4. ListItem의 modifier.clickable로 선택 처리")
            }
        }
    }
}

/**
 * 연습 문제 2: 검색 기록 저장 및 표시
 *
 * 요구사항:
 * - 검색 실행 시 기록 저장
 * - 검색 기록 최대 5개 유지
 * - 기록 항목 클릭 시 해당 검색어로 자동 입력
 * - 기록 삭제 기능 (X 버튼)
 *
 * TODO: 검색 기록 기능을 구현하세요!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_SearchHistory() {
    // TODO 1: 검색 기록 상태 선언
    // 힌트: val searchHistory = remember { mutableStateListOf<String>() }

    // TODO 2: query와 expanded 상태 선언

    /*
    // 정답:
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf("Kotlin", "Compose", "Android") }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: 검색 기록",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 검색 기록 기능을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 3: SearchBar 구현
        // 힌트:
        // - onSearch에서 기록 추가 (중복 제거, 최대 5개)
        // - query가 비어있을 때 검색 기록 표시
        // - History 아이콘 사용
        // - 삭제 버튼 (trailing에 X 아이콘)

        /*
        // 정답:
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {
                        if (query.isNotBlank()) {
                            searchHistory.remove(query)
                            searchHistory.add(0, query)
                            while (searchHistory.size > 5) {
                                searchHistory.removeLast()
                            }
                        }
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("검색어 입력 후 Enter") },
                    leadingIcon = { Icon(Icons.Default.Search, "검색") },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Close, "지우기")
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (query.isEmpty() && searchHistory.isNotEmpty()) {
                Column {
                    Text(
                        text = "최근 검색",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                    searchHistory.forEach { history ->
                        ListItem(
                            headlineContent = { Text(history) },
                            leadingContent = {
                                Icon(Icons.Default.Refresh, null)
                            },
                            trailingContent = {
                                IconButton(onClick = { searchHistory.remove(history) }) {
                                    Icon(Icons.Default.Close, "삭제", Modifier.size(20.dp))
                                }
                            },
                            modifier = Modifier.clickable {
                                query = history
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        */

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. mutableStateListOf<String>()으로 기록 관리")
                Text("2. onSearch에서:")
                Text("   - searchHistory.remove(query) // 중복 제거")
                Text("   - searchHistory.add(0, query) // 맨 앞에 추가")
                Text("   - while (size > 5) removeLast() // 최대 5개")
                Text("3. Icons.Default.Refresh로 기록 아이콘")
                Text("4. trailingContent에 삭제 버튼")
            }
        }
    }
}

/**
 * 연습 문제 3: 필터 칩과 연동된 고급 검색
 *
 * 요구사항:
 * - 카테고리 필터 칩 표시 (전체, 과일, 채소, 음료)
 * - 선택된 필터에 따라 검색 결과 필터링
 * - 디바운스 적용 (300ms)
 * - 검색어 하이라이팅
 *
 * TODO: 고급 검색 기능을 구현하세요!
 */
@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun Practice3_AdvancedSearch() {
    data class SearchItem(val name: String, val category: String)

    val items = listOf(
        SearchItem("Apple", "과일"),
        SearchItem("Banana", "과일"),
        SearchItem("Orange", "과일"),
        SearchItem("Carrot", "채소"),
        SearchItem("Broccoli", "채소"),
        SearchItem("Spinach", "채소"),
        SearchItem("Orange Juice", "음료"),
        SearchItem("Apple Juice", "음료"),
        SearchItem("Green Tea", "음료")
    )

    val categories = listOf("전체", "과일", "채소", "음료")

    // TODO 1: 상태 선언
    // - query, expanded, selectedCategory
    // - filteredItems (검색 + 카테고리 필터 적용)
    // - isSearching (디바운스 중 로딩 표시용)

    // TODO 2: 디바운스 구현 (LaunchedEffect + snapshotFlow)
    // 힌트:
    // LaunchedEffect(Unit) {
    //     snapshotFlow { query }
    //         .debounce(300)
    //         .filter { it.length >= 2 }
    //         .distinctUntilChanged()
    //         .collect { ... }
    // }

    /*
    // 정답:
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var debouncedQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300)
            .distinctUntilChanged()
            .collect { debouncedQuery = it }
    }

    val filteredItems = remember(debouncedQuery, selectedCategory) {
        items.filter { item ->
            val matchesQuery = debouncedQuery.isEmpty() ||
                    item.name.lowercase().contains(debouncedQuery.lowercase())
            val matchesCategory = selectedCategory == null ||
                    selectedCategory == "전체" ||
                    item.category == selectedCategory
            matchesQuery && matchesCategory
        }
    }
    */

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "연습 3: 고급 검색",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "TODO: 필터 + 디바운스 + 하이라이팅을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 3: SearchBar 구현

        // TODO 4: FilterChip Row 구현
        // 힌트:
        // LazyRow {
        //     items(categories) { category ->
        //         FilterChip(
        //             selected = selectedCategory == category,
        //             onClick = { selectedCategory = if (...) null else category },
        //             label = { Text(category) }
        //         )
        //     }
        // }

        // TODO 5: 결과 목록 (하이라이팅 적용)
        // 힌트: HighlightedText(text = item.name, query = debouncedQuery)

        /*
        // 정답 (SearchBar):
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("검색 (300ms 디바운스)") },
                    leadingIcon = { Icon(Icons.Default.Search, "검색") },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Close, "지우기")
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            LazyColumn {
                items(filteredItems) { item ->
                    ListItem(
                        headlineContent = {
                            HighlightedText(text = item.name, query = debouncedQuery)
                        },
                        supportingContent = { Text(item.category) },
                        modifier = Modifier.clickable {
                            query = item.name
                            expanded = false
                        }
                    )
                }
            }
        }

        // 정답 (FilterChip):
        LazyRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = if (selectedCategory == category) null else category
                    },
                    label = { Text(category) }
                )
            }
        }

        // 정답 (결과 목록):
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = {
                            HighlightedText(text = item.name, query = debouncedQuery)
                        },
                        supportingContent = { Text(item.category) }
                    )
                }
            }
        }
        */

        // 힌트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 디바운스:")
                Text("   snapshotFlow { query }.debounce(300)")
                Text("2. 필터링:")
                Text("   matchesQuery && matchesCategory")
                Text("3. FilterChip:")
                Text("   selected = (selectedCategory == category)")
                Text("4. 하이라이팅:")
                Text("   buildAnnotatedString + withStyle")
            }
        }
    }
}

/**
 * 하이라이팅 텍스트 컴포넌트 (연습용)
 */
@Composable
private fun HighlightedText(text: String, query: String) {
    val annotatedString = buildAnnotatedString {
        if (query.isEmpty()) {
            append(text)
            return@buildAnnotatedString
        }

        val lowercaseText = text.lowercase()
        val lowercaseQuery = query.lowercase()
        var startIndex = 0

        while (true) {
            val matchIndex = lowercaseText.indexOf(lowercaseQuery, startIndex)
            if (matchIndex == -1) {
                append(text.substring(startIndex))
                break
            }

            append(text.substring(startIndex, matchIndex))

            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    background = Color.Yellow.copy(alpha = 0.3f)
                )
            ) {
                append(text.substring(matchIndex, matchIndex + query.length))
            }

            startIndex = matchIndex + query.length
        }
    }
    Text(annotatedString)
}
