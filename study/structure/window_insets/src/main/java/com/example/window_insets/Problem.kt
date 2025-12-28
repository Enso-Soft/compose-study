package com.example.window_insets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: WindowInsets를 처리하지 않을 때 발생하는 문제들
 *
 * Android 15 이상에서는 edge-to-edge가 기본 적용되어
 * 콘텐츠가 시스템 바 뒤에 그려집니다.
 * WindowInsets를 처리하지 않으면 콘텐츠가 가려집니다!
 */
@Composable
fun ProblemScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 데모 선택 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("리스트 가려짐") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("키보드 가려짐") }
            )
        }

        when (selectedDemo) {
            0 -> Problem1_ListOverlap()
            1 -> Problem2_KeyboardOverlap()
        }
    }
}

/**
 * 문제 1: 리스트의 첫 번째/마지막 아이템이 시스템 바에 가려짐
 *
 * WindowInsets 처리 없이 LazyColumn을 사용하면
 * - 첫 번째 아이템: 상태바에 가려짐
 * - 마지막 아이템: 네비게이션 바에 가려짐
 */
@Composable
private fun Problem1_ListOverlap() {
    Column(modifier = Modifier.fillMaxSize()) {
        // 문제 설명 카드
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
                    text = "문제: Inset 미처리 시 콘텐츠 가려짐",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        • 첫 번째 아이템이 상태바에 가려질 수 있음
                        • 마지막 아이템이 네비게이션 바에 가려질 수 있음
                        • 스크롤해도 일부 콘텐츠에 접근 불가

                        아래 리스트를 스크롤하여 확인하세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 문제 상황 시뮬레이션
        // 실제로는 Scaffold 안에 있어서 상태바는 처리됨
        // 하지만 개념을 보여주기 위해 배경색으로 표시
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            val items = (1..30).map { "아이템 $it" }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                // 의도적으로 contentPadding 없음 - 문제 상황!
            ) {
                item {
                    // 첫 번째 아이템 - 시각적으로 문제 표시
                    ListItem(
                        modifier = Modifier.background(Color.Red.copy(alpha = 0.3f)),
                        headlineContent = {
                            Text(
                                "첫 번째 아이템 (가려질 수 있음)",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        supportingContent = {
                            Text("상태바 뒤에 그려질 수 있습니다")
                        }
                    )
                }

                items(items.drop(1).dropLast(1)) { item ->
                    ListItem(headlineContent = { Text(item) })
                    HorizontalDivider()
                }

                item {
                    // 마지막 아이템 - 시각적으로 문제 표시
                    ListItem(
                        modifier = Modifier.background(Color.Red.copy(alpha = 0.3f)),
                        headlineContent = {
                            Text(
                                "마지막 아이템 (가려질 수 있음)",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        supportingContent = {
                            Text("네비게이션 바에 가려질 수 있습니다")
                        }
                    )
                }
            }
        }
    }
}

/**
 * 문제 2: TextField가 키보드에 가려짐
 *
 * 화면 하단에 TextField가 있을 때
 * 키보드가 올라오면 TextField가 완전히 가려집니다.
 */
@Composable
private fun Problem2_KeyboardOverlap() {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // 문제 설명 카드
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
                    text = "문제: 키보드에 TextField 가려짐",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        • 하단의 TextField를 클릭해보세요
                        • 키보드가 올라오면 TextField가 가려집니다
                        • 사용자가 입력 내용을 볼 수 없음!

                        (참고: AndroidManifest의 adjustResize로 일부 완화됨)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 공간 채우기
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "이 영역은 메시지 목록이라고 가정합니다",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // 하단 입력 영역 - imePadding 없음!
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red.copy(alpha = 0.2f))
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("메시지 입력 (키보드에 가려질 수 있음)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("클릭하여 키보드를 열어보세요...") }
            )
        }
    }
}
