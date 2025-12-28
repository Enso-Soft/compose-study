package com.example.search_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Problem: TextField로 검색창을 직접 구현하면 복잡해집니다
 *
 * 이 화면은 TextField와 Card를 사용해서 검색 UI를 직접 만들었을 때
 * 발생하는 문제점들을 보여줍니다.
 *
 * 문제점:
 * 1. 확장/축소 애니메이션을 직접 구현해야 함
 * 2. 외부 클릭 시 닫기 처리가 없음
 * 3. Material Design 가이드라인과 다른 모양
 * 4. 키보드 검색 버튼 처리가 별도로 필요함
 * 5. 접근성(Accessibility) 지원이 부족함
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 상황 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TextField로 검색창 직접 구현",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래는 TextField로 검색 UI를 직접 만든 예시입니다.\n" +
                            "작동은 하지만 여러 문제점이 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TextField로 직접 구현한 검색창
        ManualSearchExample()

        Spacer(modifier = Modifier.height(24.dp))

        // 문제점 목록
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "이 방식의 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem(
                    number = 1,
                    title = "애니메이션 부재",
                    description = "확장/축소 시 부드러운 전환이 없어 어색합니다."
                )
                ProblemItem(
                    number = 2,
                    title = "외부 클릭 처리 없음",
                    description = "검색창 밖을 클릭해도 제안 목록이 닫히지 않습니다."
                )
                ProblemItem(
                    number = 3,
                    title = "Material Design 미준수",
                    description = "공식 디자인 가이드라인과 다른 모양입니다."
                )
                ProblemItem(
                    number = 4,
                    title = "키보드 처리 부족",
                    description = "검색 버튼 클릭 시 동작을 별도로 구현해야 합니다."
                )
                ProblemItem(
                    number = 5,
                    title = "접근성 미흡",
                    description = "스크린 리더 사용자를 위한 처리가 부족합니다."
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Solution 탭에서 SearchBar를 사용한 올바른 구현을 확인하세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * TextField로 직접 구현한 검색창 예시
 *
 * 이 코드는 "잘못된 예시"를 보여주기 위한 것입니다.
 * 실제 앱에서는 SearchBar를 사용하세요!
 */
@Composable
private fun ManualSearchExample() {
    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    // 샘플 데이터
    val fruits = listOf("사과", "바나나", "오렌지", "포도", "딸기", "수박", "참외", "복숭아")
    val filteredFruits = if (query.isEmpty()) {
        fruits
    } else {
        fruits.filter { it.contains(query, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "직접 구현한 검색창:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 문제: 기본 OutlinedTextField 사용
        OutlinedTextField(
            value = query,
            onValueChange = { newQuery ->
                query = newQuery
                isExpanded = true  // 입력하면 확장
            },
            placeholder = { Text("과일 검색") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색"
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "지우기"
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // 문제: AnimatedVisibility로 직접 확장/축소 처리
        // 외부 클릭 시 닫히지 않음!
        AnimatedVisibility(visible = isExpanded && filteredFruits.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    filteredFruits.forEach { fruit ->
                        Text(
                            text = fruit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    query = fruit
                                    isExpanded = false
                                }
                                .padding(16.dp)
                        )
                        if (fruit != filteredFruits.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

        // 결과 없음 표시
        AnimatedVisibility(visible = isExpanded && query.isNotEmpty() && filteredFruits.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = "검색 결과가 없습니다",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.width(24.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
