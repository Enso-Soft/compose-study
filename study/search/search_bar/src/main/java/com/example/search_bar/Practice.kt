package com.example.search_bar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Practice: SearchBar 연습 문제
 *
 * 이 파일에는 3개의 연습 문제가 있습니다.
 * TODO 주석을 읽고 직접 구현해보세요!
 *
 * 난이도:
 * - 연습 1: 쉬움 (기본 SearchBar 구조)
 * - 연습 2: 중간 (trailingIcon + 상태 초기화)
 * - 연습 3: 어려움 (onSearch + 검색 결과 상태)
 */

// ============================================================
// 연습 1: 과일 검색 (쉬움)
// ============================================================

/**
 * 연습 1: 기본 SearchBar로 과일 목록 검색하기
 *
 * TODO: 아래 요구사항을 만족하는 SearchBar를 구현하세요.
 *
 * 요구사항:
 * 1. 검색 아이콘(Icons.Default.Search)이 있는 SearchBar 구현
 * 2. "과일 검색" placeholder 표시
 * 3. 입력한 텍스트로 fruits 목록 필터링
 * 4. 필터링된 결과를 LazyColumn으로 표시
 * 5. 결과 항목 클릭 시 해당 과일을 검색어로 설정하고 닫기
 *
 * 힌트:
 * - var query by remember { mutableStateOf("") }
 * - var expanded by rememberSaveable { mutableStateOf(false) }
 * - SearchBarDefaults.InputField(...) 사용
 * - fruits.filter { it.contains(query) } 로 필터링
 *
 * 참고: @OptIn(ExperimentalMaterial3Api::class) 어노테이션 필요!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_FruitSearch() {
    val fruits = listOf("사과", "바나나", "오렌지", "포도", "딸기", "수박", "참외", "복숭아")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 과일 검색 (쉬움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기본 SearchBar를 구현하고 과일 목록을 필터링하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 여기에 SearchBar를 구현하세요!
        // ========================================
        //
        // 1. query와 expanded 상태를 선언하세요
        //    var query by remember { mutableStateOf("") }
        //    var expanded by rememberSaveable { mutableStateOf(false) }
        //
        // 2. 필터링된 과일 목록을 만드세요
        //    val filteredFruits = if (query.isEmpty()) fruits
        //        else fruits.filter { it.contains(query) }
        //
        // 3. Box로 감싸고 SearchBar를 구현하세요
        //    Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
        //        SearchBar(
        //            inputField = {
        //                SearchBarDefaults.InputField(
        //                    query = query,
        //                    onQueryChange = { query = it },
        //                    onSearch = { expanded = false },
        //                    expanded = expanded,
        //                    onExpandedChange = { expanded = it },
        //                    placeholder = { Text("과일 검색") },
        //                    leadingIcon = { Icon(Icons.Default.Search, "검색") }
        //                )
        //            },
        //            expanded = expanded,
        //            onExpandedChange = { expanded = it }
        //        ) {
        //            LazyColumn {
        //                items(filteredFruits) { fruit ->
        //                    ListItem(
        //                        headlineContent = { Text(fruit) },
        //                        modifier = Modifier.clickable {
        //                            query = fruit
        //                            expanded = false
        //                        }
        //                    )
        //                }
        //            }
        //        }
        //    }
        //
        // ========================================

        Text(
            text = "위 주석의 힌트를 참고하여 구현해보세요!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

// ============================================================
// 연습 2: 연락처 검색 + 지우기 버튼 (중간)
// ============================================================

/**
 * 연습 2: 연락처 검색 + 지우기 버튼
 *
 * TODO: 아래 요구사항을 만족하는 SearchBar를 구현하세요.
 *
 * 요구사항:
 * 1. 연락처(이름) 목록에서 검색
 * 2. 검색어가 있으면 X 버튼(trailingIcon) 표시
 * 3. X 버튼 클릭 시 검색어 초기화 (query = "")
 * 4. 결과 항목 클릭 시 해당 이름을 검색어로 설정하고 닫기
 * 5. "연락처 검색" placeholder 표시
 *
 * 힌트:
 * - trailingIcon은 query.isNotEmpty()일 때만 표시
 * - IconButton과 Icons.Default.Close 사용
 * - 연습 1과 비슷하지만 trailingIcon 추가!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_ContactSearch() {
    val contacts = listOf(
        "김철수", "이영희", "박민수", "최지은", "정다운",
        "강민호", "윤서연", "임재현", "한소희", "오준혁"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 연락처 검색 + 지우기 버튼 (중간)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "trailingIcon으로 지우기 버튼을 추가하세요.\n" +
                            "검색어가 있을 때만 X 버튼이 표시되어야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 여기에 SearchBar를 구현하세요!
        // ========================================
        //
        // 연습 1과 동일하게 시작하되, trailingIcon을 추가하세요:
        //
        // trailingIcon = {
        //     if (query.isNotEmpty()) {
        //         IconButton(onClick = { query = "" }) {
        //             Icon(Icons.Default.Close, "지우기")
        //         }
        //     }
        // }
        //
        // 연락처 목록에서 사람 아이콘을 표시하려면:
        // leadingContent = { Icon(Icons.Default.Person, null) }
        //
        // ========================================

        Text(
            text = "trailingIcon에 조건부로 지우기 버튼을 추가하세요!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

// ============================================================
// 연습 3: 도시 검색 + 검색 결과 표시 (어려움)
// ============================================================

/**
 * 연습 3: 도시 검색 + 검색 결과 표시
 *
 * TODO: 아래 요구사항을 만족하는 SearchBar를 구현하세요.
 *
 * 요구사항:
 * 1. 도시 목록에서 검색
 * 2. 입력 중에는 실시간 필터링 표시 (expanded 상태)
 * 3. 키보드 검색 버튼 클릭 시:
 *    - 검색창 닫기 (expanded = false)
 *    - 검색된 도시를 별도 상태에 저장 (searchedCity)
 *    - SearchBar 아래에 "검색 결과: {도시명}" 텍스트 표시
 * 4. 검색 결과가 없으면 "결과 없음" 표시
 * 5. "도시 검색" placeholder + leadingIcon + trailingIcon 모두 포함
 *
 * 힌트:
 * - var searchedCity by remember { mutableStateOf<String?>(null) }
 * - onSearch에서 searchedCity = query 또는 첫 번째 결과 설정
 * - SearchBar 아래에 searchedCity가 null이 아닐 때 결과 표시
 *
 * 추가 도전:
 * - 검색 결과가 정확히 일치하는 도시만 표시
 * - 결과가 없을 때 "'{query}'에 해당하는 도시가 없습니다" 표시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_CitySearch() {
    val cities = listOf(
        "서울", "부산", "대구", "인천", "광주",
        "대전", "울산", "세종", "수원", "용인"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 도시 검색 + 검색 결과 표시 (어려움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "onSearch 콜백을 활용하여 검색 버튼 클릭 시\n" +
                            "검색 결과를 별도로 표시하세요.\n\n" +
                            "검색창 아래에 \"검색 결과: {도시명}\" 형태로 표시합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 여기에 SearchBar를 구현하세요!
        // ========================================
        //
        // 1. 추가 상태 선언
        //    var searchedCity by remember { mutableStateOf<String?>(null) }
        //
        // 2. onSearch에서 검색 결과 저장
        //    onSearch = { searchQuery ->
        //        expanded = false
        //        val exactMatch = cities.find {
        //            it.equals(searchQuery, ignoreCase = true)
        //        }
        //        searchedCity = exactMatch ?: filteredCities.firstOrNull()
        //    }
        //
        // 3. SearchBar 아래에 검색 결과 표시
        //    if (searchedCity != null) {
        //        Card(...) {
        //            Text("검색 결과: $searchedCity")
        //        }
        //    } else if (query.isNotEmpty() && !expanded) {
        //        Text("결과 없음")
        //    }
        //
        // ========================================

        Text(
            text = "onSearch 콜백에서 검색 결과를 저장하고 표시하세요!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 구현 체크리스트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "구현 체크리스트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChecklistItem("query, expanded, searchedCity 세 개의 상태가 있나요?")
                ChecklistItem("leadingIcon, trailingIcon이 모두 있나요?")
                ChecklistItem("onSearch에서 expanded = false로 설정했나요?")
                ChecklistItem("onSearch에서 searchedCity를 설정했나요?")
                ChecklistItem("SearchBar 아래에 검색 결과를 표시했나요?")
            }
        }
    }
}

@Composable
private fun ChecklistItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = "[ ]",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
