package com.example.search_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
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
 * 해결책 - Material3 SearchBar 사용
 *
 * SearchBar 사용의 장점:
 * 1. 내장 애니메이션으로 부드러운 확장/축소
 * 2. Material Design 3 가이드라인 자동 준수
 * 3. 접근성 기본 지원
 * 4. 외부 클릭 시 자동으로 닫힘
 * 5. 상태 관리 단순화
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }
    val demos = listOf("기본 사용", "검색 기록", "디바운스", "필터 칩", "하이라이팅")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Solution: Material3 SearchBar",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

        // 데모 선택 탭
        ScrollableTabRow(
            selectedTabIndex = selectedDemo,
            modifier = Modifier.fillMaxWidth()
        ) {
            demos.forEachIndexed { index, title ->
                Tab(
                    selected = selectedDemo == index,
                    onClick = { selectedDemo = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedDemo) {
            0 -> BasicSearchBarDemo()
            1 -> SearchHistoryDemo()
            2 -> DebounceSearchDemo()
            3 -> FilterChipSearchDemo()
            4 -> HighlightingSearchDemo()
        }
    }
}

/**
 * 기본 SearchBar 사용 예시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicSearchBarDemo() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val allItems = listOf(
        "Apple", "Apricot", "Avocado",
        "Banana", "Blueberry", "Blackberry",
        "Cherry", "Coconut", "Cranberry",
        "Date", "Dragonfruit", "Durian"
    )

    val filteredItems = remember(query) {
        if (query.isEmpty()) allItems
        else allItems.filter { it.lowercase().contains(query.lowercase()) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("과일 검색...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "검색") },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "지우기")
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
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- SearchBarDefaults.InputField로 입력 필드 커스터마이징")
                Text("- expanded/onExpandedChange로 확장 상태 관리")
                Text("- content 람다에서 검색 결과 표시")
                Text("- rememberSaveable로 화면 회전 시 상태 유지")
            }
        }
    }
}

/**
 * 검색 기록 관리 예시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchHistoryDemo() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf("Kotlin", "Compose", "Android", "Material") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {
                        if (query.isNotBlank()) {
                            // 중복 제거 후 맨 앞에 추가
                            searchHistory.remove(query)
                            searchHistory.add(0, query)
                            // 최대 10개 유지
                            while (searchHistory.size > 10) {
                                searchHistory.removeLast()
                            }
                        }
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("검색어를 입력하세요") },
                    leadingIcon = { Icon(Icons.Default.Search, "검색") },
                    trailingIcon = {
                        Row {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(Icons.Default.Close, "지우기")
                                }
                            }
                            // 음성 검색 버튼 (데모)
                            IconButton(onClick = { /* 음성 검색 */ }) {
                                Icon(Icons.Default.MoreVert, "더보기")
                            }
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            // 검색 기록 표시
            if (query.isEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
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
                                IconButton(
                                    onClick = { searchHistory.remove(history) }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "삭제",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            modifier = Modifier.clickable {
                                query = history
                                expanded = false
                            }
                        )
                    }
                }
            } else {
                // 검색 제안
                val suggestions = searchHistory.filter {
                    it.lowercase().contains(query.lowercase())
                }
                LazyColumn {
                    items(suggestions) { suggestion ->
                        ListItem(
                            headlineContent = { Text(suggestion) },
                            leadingContent = { Icon(Icons.Default.Search, null) },
                            modifier = Modifier.clickable {
                                query = suggestion
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "검색 기록 관리",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- mutableStateListOf로 기록 관리")
                Text("- 중복 제거 후 맨 앞에 추가")
                Text("- 최대 개수 제한 (10개)")
                Text("- History 아이콘으로 기록 표시")
                Text("- 개별 기록 삭제 기능")
            }
        }
    }
}

/**
 * 디바운스 검색 예시
 */
@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
private fun DebounceSearchDemo() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchCount by remember { mutableIntStateOf(0) }

    // 디바운스 처리
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300) // 300ms 대기
            .filter { it.length >= 2 } // 2글자 이상
            .distinctUntilChanged() // 중복 방지
            .collect { searchQuery ->
                isSearching = true
                searchCount++
                // 실제로는 API 호출
                kotlinx.coroutines.delay(500) // 네트워크 시뮬레이션
                searchResults = listOf(
                    "$searchQuery 관련 결과 1",
                    "$searchQuery 관련 결과 2",
                    "$searchQuery 관련 결과 3"
                )
                isSearching = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("2글자 이상 입력 (디바운스 300ms)") },
                    leadingIcon = { Icon(Icons.Default.Search, "검색") },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = {
                                query = ""
                                searchResults = emptyList()
                            }) {
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
            if (isSearching) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(searchResults) { result ->
                        ListItem(
                            headlineContent = { Text(result) },
                            modifier = Modifier.clickable {
                                query = result
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 검색 횟수 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "API 호출 횟수: $searchCount",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("디바운스 없이는 매 글자마다 API 호출!")
                Text("디바운스로 입력 완료 후 한 번만 호출")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "디바운스 구현 패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("snapshotFlow { query }")
                Text("  .debounce(300)")
                Text("  .filter { it.length >= 2 }")
                Text("  .distinctUntilChanged()")
                Text("  .collect { ... }")
            }
        }
    }
}

/**
 * 필터 칩 연동 예시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChipSearchDemo() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val categories = listOf("전체", "과일", "채소", "음료")
    val items = listOf(
        SearchItem("Apple", "과일"),
        SearchItem("Banana", "과일"),
        SearchItem("Carrot", "채소"),
        SearchItem("Broccoli", "채소"),
        SearchItem("Orange Juice", "음료"),
        SearchItem("Apple Juice", "음료"),
        SearchItem("Grape", "과일"),
        SearchItem("Spinach", "채소")
    )

    val filteredItems = remember(query, selectedCategory) {
        items.filter { item ->
            val matchesQuery = query.isEmpty() ||
                    item.name.lowercase().contains(query.lowercase())
            val matchesCategory = selectedCategory == null ||
                    selectedCategory == "전체" ||
                    item.category == selectedCategory
            matchesQuery && matchesCategory
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("검색...") },
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LazyColumn {
                items(filteredItems) { item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text(item.category) },
                        modifier = Modifier.clickable {
                            query = item.name
                            expanded = false
                        }
                    )
                }
            }
        }

        // 필터 칩
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        // 결과 표시
        Text(
            text = "검색 결과: ${filteredItems.size}개",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text(item.category) }
                    )
                }
            }
        }
    }
}

/**
 * 검색어 하이라이팅 예시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HighlightingSearchDemo() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val items = listOf(
        "Apple iPhone 15 Pro",
        "Samsung Galaxy S24",
        "Google Pixel 8 Pro",
        "Apple MacBook Pro",
        "Microsoft Surface Pro",
        "Apple AirPods Pro"
    )

    val filteredItems = remember(query) {
        if (query.isEmpty()) items
        else items.filter { it.lowercase().contains(query.lowercase()) }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("제품 검색...") },
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LazyColumn {
                items(filteredItems) { item ->
                    ListItem(
                        headlineContent = {
                            HighlightedText(text = item, query = query)
                        },
                        modifier = Modifier.clickable {
                            query = item
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 결과 목록 (하이라이팅 적용)
        Text(
            text = "검색 결과 (하이라이팅 적용)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = {
                            HighlightedText(text = item, query = query)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                    text = "하이라이팅 구현",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("buildAnnotatedString {")
                Text("  withStyle(SpanStyle(fontWeight = Bold)) {")
                Text("    append(matchedPart)")
                Text("  }")
                Text("}")
            }
        }
    }
}

/**
 * 검색어 하이라이팅 컴포넌트
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

            // 매칭 전 텍스트
            append(text.substring(startIndex, matchIndex))

            // 매칭된 텍스트 (하이라이팅)
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

/**
 * 검색 아이템 데이터 클래스
 */
private data class SearchItem(
    val name: String,
    val category: String
)
