package com.example.lazy_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Practice: LazyColumn/LazyRow 연습 문제
 *
 * 3개의 연습 문제를 통해 배운 내용을 직접 실습합니다.
 * - 연습 1: 기본 텍스트 리스트 (쉬움)
 * - 연습 2: 연락처 목록 만들기 (중간)
 * - 연습 3: 사진 갤러리 그리드 (어려움)
 */
@Composable
fun PracticeScreen() {
    var selectedExercise by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedExercise) {
            Tab(
                selected = selectedExercise == 0,
                onClick = { selectedExercise = 0 },
                text = { Text("연습 1") }
            )
            Tab(
                selected = selectedExercise == 1,
                onClick = { selectedExercise = 1 },
                text = { Text("연습 2") }
            )
            Tab(
                selected = selectedExercise == 2,
                onClick = { selectedExercise = 2 },
                text = { Text("연습 3") }
            )
        }

        when (selectedExercise) {
            0 -> Exercise1Screen()
            1 -> Exercise2Screen()
            2 -> Exercise3Screen()
        }
    }
}

// =============================================================================
// 연습 1: 기본 텍스트 리스트 (쉬움)
// =============================================================================

@Composable
private fun Exercise1Screen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ExerciseCard(
                title = "연습 1: 기본 텍스트 리스트",
                difficulty = "쉬움",
                description = """
                    50개의 텍스트 아이템을 LazyColumn으로 표시하세요.

                    요구사항:
                    - 각 아이템: "아이템 1", "아이템 2", ... 형식
                    - contentPadding: 16.dp
                    - 아이템 간격: 8.dp
                """.trimIndent()
            )
        }

        item {
            HintCard(
                hints = listOf(
                    "LazyColumn(contentPadding = PaddingValues(16.dp))",
                    "verticalArrangement = Arrangement.spacedBy(8.dp)",
                    "items(50) { index -> Text(\"아이템 \${index + 1}\") }"
                )
            )
        }

        item {
            // TODO: 여기에 LazyColumn을 구현하세요
            // 1. contentPadding = PaddingValues(16.dp)
            // 2. verticalArrangement = Arrangement.spacedBy(8.dp)
            // 3. items(50) { index -> ... }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TODO: LazyColumn으로 50개 아이템 표시하기",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// =============================================================================
// 연습 2: 연락처 목록 만들기 (중간)
// =============================================================================

/**
 * 연락처 데이터 클래스 (이 클래스를 사용하세요)
 */
data class Contact(
    val id: Int,
    val name: String,
    val phone: String
)

@Composable
private fun Exercise2Screen() {
    // 샘플 연락처 데이터
    val contacts = remember {
        listOf(
            Contact(1, "김철수", "010-1234-5678"),
            Contact(2, "이영희", "010-2345-6789"),
            Contact(3, "박민수", "010-3456-7890"),
            Contact(4, "최지현", "010-4567-8901"),
            Contact(5, "정하늘", "010-5678-9012"),
            Contact(6, "강다은", "010-6789-0123"),
            Contact(7, "윤서준", "010-7890-1234"),
            Contact(8, "장미래", "010-8901-2345")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ExerciseCard(
                title = "연습 2: 연락처 목록",
                difficulty = "중간",
                description = """
                    연락처 목록을 만드세요.

                    요구사항:
                    - 헤더: "연락처" 제목 (item 사용)
                    - 각 연락처: 프로필 아이콘 + 이름 + 전화번호
                    - Card로 감싸기
                    - items(contacts) 사용
                """.trimIndent()
            )
        }

        item {
            HintCard(
                hints = listOf(
                    "LazyColumn { item { 헤더 } items(contacts) { 아이템 } }",
                    "Icons.Default.Person 아이콘 사용",
                    "Row로 아이콘과 텍스트 배치"
                )
            )
        }

        item {
            // TODO: 여기에 연락처 목록을 구현하세요
            // 1. item { } 으로 "연락처" 헤더 추가
            // 2. items(contacts) { contact -> ... } 으로 목록 표시
            // 3. 각 아이템에 Icon, Text(이름), Text(전화번호) 포함

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TODO: 연락처 목록 구현하기",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "사용할 데이터: contacts (${contacts.size}명)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// 연습 3: 사진 갤러리 그리드 (어려움)
// =============================================================================

@Composable
private fun Exercise3Screen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ExerciseCard(
                title = "연습 3: 사진 갤러리 그리드",
                difficulty = "어려움",
                description = """
                    선택 가능한 사진 갤러리를 만드세요.

                    요구사항:
                    - 2열 그리드 (GridCells.Fixed(2))
                    - 정사각형 카드 (aspectRatio(1f))
                    - 탭하면 선택 상태 토글 (체크 표시)
                    - 간격과 패딩: 8.dp
                    - 12개 아이템 표시
                """.trimIndent()
            )
        }

        item {
            HintCard(
                hints = listOf(
                    "var selectedIds by remember { mutableStateOf(setOf<Int>()) }",
                    "LazyVerticalGrid(columns = GridCells.Fixed(2))",
                    "Modifier.aspectRatio(1f).clickable { }",
                    "Icons.Default.Check 아이콘으로 선택 표시"
                )
            )
        }

        item {
            // TODO: 여기에 사진 갤러리 그리드를 구현하세요
            // 1. selectedIds: Set<Int> 상태 관리
            // 2. LazyVerticalGrid 사용
            // 3. 클릭 시 selectedIds 토글
            // 4. 선택된 아이템에 체크 아이콘 표시

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TODO: LazyVerticalGrid로 갤러리 구현하기",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "선택 상태 관리 + 체크 아이콘 표시",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// 공통 컴포넌트
// =============================================================================

@Composable
private fun ExerciseCard(
    title: String,
    difficulty: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                AssistChip(
                    onClick = { },
                    label = { Text(difficulty) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "숨기기" else "보기")
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                hints.forEach { hint ->
                    Text(
                        text = "• $hint",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}
