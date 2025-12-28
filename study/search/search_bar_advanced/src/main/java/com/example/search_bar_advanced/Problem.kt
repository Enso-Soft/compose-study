package com.example.search_bar_advanced

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - TextField로 직접 검색 UI 구현
 *
 * 이 코드의 문제점:
 * 1. 검색 UI 스타일링을 직접 처리해야 함
 * 2. 확장/축소 애니메이션 직접 구현 필요
 * 3. 제안 목록 위치, 크기, 그림자 직접 처리
 * 4. Material Design 가이드라인 준수 어려움
 * 5. 접근성(시맨틱) 직접 처리 필요
 * 6. 외부 클릭 시 닫기 동작 미구현
 */
@Composable
fun ProblemScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Problem: TextField로 직접 구현",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 데모
        ManualSearchDemo()

        Spacer(modifier = Modifier.height(24.dp))

        // 문제점 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProblemItem("1. 스타일링 직접 처리: Material Design 가이드라인 맞추기 어려움")
                ProblemItem("2. 애니메이션 미적용: 확장/축소 시 갑작스러운 전환")
                ProblemItem("3. 제안 목록 위치 하드코딩: 다양한 화면에서 문제 발생 가능")
                ProblemItem("4. 외부 클릭 미처리: 검색창 외부 클릭해도 닫히지 않음")
                ProblemItem("5. 접근성 누락: 스크린 리더 지원 직접 구현 필요")
                ProblemItem("6. 키보드 네비게이션: Tab 이동 등 직접 처리 필요")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 복잡성 비교
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 복잡성 비교",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("직접 구현 시 필요한 코드:")
                Text("- TextField + 커스텀 스타일링")
                Text("- AnimatedVisibility 직접 처리")
                Text("- Card + LazyColumn으로 제안 목록")
                Text("- Modifier.clickable로 외부 클릭 감지")
                Text("- FocusManager로 포커스 처리")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "= 약 100줄 이상의 보일러플레이트 코드",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ManualSearchDemo() {
    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    val allItems = listOf(
        "Apple", "Apricot", "Avocado",
        "Banana", "Blueberry", "Blackberry",
        "Cherry", "Coconut", "Cranberry"
    )

    val suggestions = remember(query) {
        if (query.isEmpty()) {
            emptyList()
        } else {
            allItems.filter { it.lowercase().contains(query.lowercase()) }
        }
    }

    Column {
        Text(
            text = "수동 구현 예시 (문제점 확인)",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 직접 구현한 검색 필드 - 문제점이 있는 코드
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                isExpanded = true
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("검색어 입력...") },
            leadingIcon = { Icon(Icons.Default.Search, "검색") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, "지우기")
                    }
                }
            },
            singleLine = true
        )

        // 제안 목록 - 애니메이션 없이 갑자기 나타남
        // 외부 클릭 처리 없음
        AnimatedVisibility(visible = isExpanded && suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                LazyColumn {
                    items(suggestions) { suggestion ->
                        Text(
                            text = suggestion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    query = suggestion
                                    isExpanded = false
                                }
                                .padding(16.dp)
                        )
                        HorizontalDivider()
                    }
                }
            }
        }

        // 빈 결과 표시
        if (isExpanded && query.isNotEmpty() && suggestions.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "검색 결과가 없습니다",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 직접 확인
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 확인해보세요!",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 검색어를 입력해보세요")
                Text("2. 제안이 나타날 때 애니메이션이 없음")
                Text("3. 제안 영역 밖을 터치해도 닫히지 않음")
                Text("4. Material Design 검색바와 다른 모양")
            }
        }
    }
}

@Composable
private fun ProblemItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 검색 기록 직접 관리의 문제점 데모
 */
@Composable
fun SearchHistoryProblemDemo() {
    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf<String>() }

    Column {
        Text(
            text = "검색 기록 직접 관리 예시",
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                isExpanded = true
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("검색 후 Enter") },
            leadingIcon = { Icon(Icons.Default.Search, "검색") },
            singleLine = true
        )

        // 문제: 검색 기록 UI를 완전히 직접 구현해야 함
        if (isExpanded && query.isEmpty() && searchHistory.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    searchHistory.forEach { history ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { query = history }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(history)
                            IconButton(
                                onClick = { searchHistory.remove(history) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "삭제",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (query.isNotBlank() && !searchHistory.contains(query)) {
                    searchHistory.add(0, query)
                    if (searchHistory.size > 5) {
                        searchHistory.removeLast()
                    }
                }
                query = ""
                isExpanded = false
            }
        ) {
            Text("검색")
        }
    }
}
