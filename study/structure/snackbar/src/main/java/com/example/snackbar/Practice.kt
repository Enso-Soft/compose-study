package com.example.snackbar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 Snackbar 표시 (쉬움)
 *
 * 버튼을 클릭하면 "안녕하세요!" 메시지의 Snackbar를 표시하세요.
 */
@Composable
fun Practice1_BasicScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 1: 기본 Snackbar 표시",
            difficulty = "쉬움",
            requirements = listOf(
                "버튼 클릭 시 Snackbar 표시",
                "메시지: \"안녕하세요!\"",
                "Scaffold + SnackbarHost 구조 사용"
            )
        )

        HintCard(
            hints = listOf(
                "val snackbarHostState = remember { SnackbarHostState() }",
                "val scope = rememberCoroutineScope()",
                "Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) })",
                "scope.launch { snackbarHostState.showSnackbar(message = \"안녕하세요!\") }"
            )
        )

        // TODO: 여기에 Scaffold + Snackbar를 구현하세요
        // 1. snackbarHostState 생성
        // 2. rememberCoroutineScope() 사용
        // 3. Scaffold의 snackbarHost 설정
        // 4. Button 클릭 시 scope.launch { showSnackbar(...) }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO: Scaffold + SnackbarHost + Button 구현하기",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 연습 문제 2: 액션 버튼 처리 (중간)
 *
 * "좋아요" 버튼을 구현하세요.
 * - 버튼 클릭 시 좋아요 수 증가 + Snackbar 표시
 * - "취소" 액션으로 좋아요 취소 가능
 */
@Composable
fun Practice2_ActionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 2: 액션 버튼 처리",
            difficulty = "중간",
            requirements = listOf(
                "좋아요 버튼 클릭 시 카운트 증가",
                "Snackbar에 \"취소\" 액션 버튼 표시",
                "취소 클릭 시 좋아요 카운트 감소",
                "SnackbarResult 활용"
            )
        )

        HintCard(
            hints = listOf(
                "var likeCount by remember { mutableIntStateOf(0) }",
                "actionLabel = \"취소\"",
                "val result = snackbarHostState.showSnackbar(...)",
                "if (result == SnackbarResult.ActionPerformed) { likeCount-- }"
            )
        )

        // TODO: 여기에 좋아요 + 취소 기능을 구현하세요
        // 1. likeCount 상태 관리
        // 2. 하트 아이콘 버튼 클릭 시 likeCount++
        // 3. showSnackbar(message, actionLabel = "취소")
        // 4. result가 ActionPerformed면 likeCount--

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TODO: 좋아요 버튼 + 취소 Snackbar 구현하기",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "SnackbarResult.ActionPerformed 체크",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 3: 삭제 + 실행 취소 구현 (어려움)
 *
 * 메모 목록에서 아이템을 삭제하고 "실행 취소"로 복구하세요.
 */
@Composable
fun Practice3_DeleteScreen() {
    // 초기 메모 데이터
    val initialMemos = listOf("회의 준비", "장보기", "운동하기", "책 읽기", "이메일 확인")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExerciseCard(
            title = "연습 3: 삭제 + 실행 취소",
            difficulty = "어려움",
            requirements = listOf(
                "메모 목록 표시 (LazyColumn)",
                "삭제 버튼 클릭 시 아이템 제거",
                "\"실행 취소\" 액션으로 복구 가능",
                "삭제 전 위치(인덱스) 저장 필요"
            )
        )

        HintCard(
            hints = listOf(
                "var memos by remember { mutableStateOf(initialMemos) }",
                "var lastDeleted by remember { mutableStateOf<Pair<Int, String>?>(null) }",
                "lastDeleted = Pair(index, memo) // 삭제 전 저장",
                "newList.add(index, item) // 원래 위치에 복구"
            )
        )

        // TODO: 여기에 삭제 + 복구 기능을 구현하세요
        // 1. memos 상태 (리스트)
        // 2. lastDeleted 상태 (Pair<Int, String>? - 인덱스와 값)
        // 3. 삭제 버튼 클릭 시:
        //    - lastDeleted에 (index, memo) 저장
        //    - memos에서 제거
        //    - showSnackbar(actionLabel = "실행 취소")
        // 4. ActionPerformed 시:
        //    - lastDeleted에서 복구
        //    - 원래 인덱스 위치에 삽입

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TODO: 메모 목록 + 삭제 + 복구 구현하기",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "초기 데이터: ${initialMemos.joinToString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
    requirements: List<String>
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
                text = "요구사항:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            requirements.forEach { req ->
                Text(
                    text = "• $req",
                    style = MaterialTheme.typography.bodySmall
                )
            }
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
