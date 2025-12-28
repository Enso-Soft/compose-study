package com.example.search_bar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Solution: SearchBar를 사용하면 모든 문제가 해결됩니다!
 *
 * SearchBar는 Material Design 3에서 제공하는 검색 전용 컴포넌트로,
 * 확장/축소 애니메이션, 외부 클릭 처리, 접근성 등을 모두 자동으로 처리합니다.
 *
 * 핵심 파라미터:
 * - inputField: 검색 입력 필드 정의 (SearchBarDefaults.InputField 사용)
 * - expanded: 검색창 확장 상태 (true면 제안 목록 표시)
 * - onExpandedChange: 확장 상태 변경 콜백
 * - content: 검색 결과/제안을 표시하는 영역
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    var showExample by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 해결책 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SearchBar로 해결!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SearchBar를 사용하면 확장/축소 애니메이션, " +
                            "외부 클릭 처리, Material Design 스타일이 모두 자동으로 적용됩니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 토글 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = showExample,
                onClick = { showExample = true },
                label = { Text("예제 보기") }
            )
            FilterChip(
                selected = !showExample,
                onClick = { showExample = false },
                label = { Text("해결 포인트") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showExample) {
            // SearchBar 예제
            SearchBarExample()
        } else {
            // 해결 포인트 설명
            SolutionPoints()
        }
    }
}

/**
 * SearchBar를 사용한 올바른 구현 예제
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarExample() {
    // 1단계: 필요한 상태 선언
    var query by remember { mutableStateOf("") }
    // rememberSaveable: 화면 회전 시에도 상태 유지
    var expanded by rememberSaveable { mutableStateOf(false) }

    // 샘플 데이터
    val fruits = listOf("사과", "바나나", "오렌지", "포도", "딸기", "수박", "참외", "복숭아")

    // 검색어로 필터링
    val filteredFruits = if (query.isEmpty()) {
        fruits
    } else {
        fruits.filter { it.contains(query, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "SearchBar 사용:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 2단계: SearchBar를 Box로 감싸기 (확장 시 레이아웃 처리)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)  // 예제용 고정 높이
        ) {
            // 3단계: SearchBar 구현
            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                inputField = {
                    // 4단계: 입력 필드 정의
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = { newQuery ->
                            // 사용자가 텍스트를 입력할 때마다 호출
                            query = newQuery
                        },
                        onSearch = { searchQuery ->
                            // 키보드의 검색 버튼을 눌렀을 때 호출
                            expanded = false  // 검색창 닫기
                        },
                        expanded = expanded,
                        onExpandedChange = { isExpanded ->
                            // 확장 상태 변경 (클릭하면 true, 외부 클릭하면 false)
                            expanded = isExpanded
                        },
                        placeholder = { Text("과일 검색") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "검색"
                            )
                        },
                        trailingIcon = {
                            // 검색어가 있을 때만 지우기 버튼 표시
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "지우기"
                                    )
                                }
                            }
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                // 5단계: 검색 결과 표시 (content 영역)
                if (filteredFruits.isEmpty()) {
                    // 결과 없음
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "검색 결과가 없습니다",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    // 결과 목록
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filteredFruits) { fruit ->
                            ListItem(
                                headlineContent = { Text(fruit) },
                                leadingContent = {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                modifier = Modifier.clickable {
                                    query = fruit
                                    expanded = false  // 선택 후 닫기
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 표시 (학습용)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "query = \"$query\"",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "expanded = $expanded",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "필터링 결과: ${filteredFruits.size}개",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * SearchBar로 해결되는 포인트 설명
 */
@Composable
private fun SolutionPoints() {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SolutionItem(
            number = 1,
            title = "확장/축소 애니메이션 내장",
            description = "검색창을 클릭하면 부드럽게 확장되고, " +
                    "외부를 클릭하거나 검색을 실행하면 자동으로 축소됩니다."
        )
        SolutionItem(
            number = 2,
            title = "외부 클릭 자동 처리",
            description = "검색창 밖을 클릭하면 자동으로 onExpandedChange(false)가 호출됩니다. " +
                    "별도 구현 필요 없음!"
        )
        SolutionItem(
            number = 3,
            title = "Material Design 3 스타일",
            description = "공식 디자인 가이드라인에 맞는 모양, 색상, 그림자가 자동으로 적용됩니다."
        )
        SolutionItem(
            number = 4,
            title = "키보드 처리 내장",
            description = "onSearch 콜백으로 키보드의 검색 버튼 클릭을 쉽게 처리할 수 있습니다."
        )
        SolutionItem(
            number = 5,
            title = "접근성 자동 지원",
            description = "스크린 리더를 위한 시맨틱 정보가 자동으로 제공됩니다."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 비교
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드량 비교",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TextField 직접 구현: 약 80줄",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "SearchBar 사용: 약 30줄",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "약 60% 코드 감소!",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun SolutionItem(
    number: Int,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "$number.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(32.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
