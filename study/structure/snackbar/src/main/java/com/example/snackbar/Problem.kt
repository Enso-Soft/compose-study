package com.example.snackbar

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * Toast를 사용하면 Compose에서 Context 접근이 필요하고,
 * 액션 버튼이 없어서 "실행 취소" 같은 기능을 제공할 수 없습니다.
 */
@Composable
fun ProblemScreen() {
    // Toast를 사용하려면 Context가 필요합니다
    val context = LocalContext.current

    // 할일 목록 상태
    var items by remember {
        mutableStateOf(listOf("장보기", "운동하기", "책 읽기"))
    }
    var deletedItem by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: Toast의 한계",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toast로 메시지를 표시하려면 Context가 필요하고, " +
                            "액션 버튼이 없어서 '실행 취소' 같은 기능을 제공할 수 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 할일 목록 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "할일 목록 (Toast 사용)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (items.isEmpty()) {
                    Text(
                        text = "모든 항목이 삭제되었습니다. 복구할 수 없습니다!",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item)
                            IconButton(
                                onClick = {
                                    deletedItem = item
                                    items = items - item

                                    // Toast로 피드백 - 액션 버튼 없음!
                                    Toast.makeText(
                                        context,
                                        "$item 삭제됨",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "삭제",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 리셋 버튼
                OutlinedButton(
                    onClick = {
                        items = listOf("장보기", "운동하기", "책 읽기")
                        deletedItem = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("목록 초기화")
                }
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. LocalContext.current로 Context를 가져와야 함")
                Text("2. Toast에는 '실행 취소' 버튼이 없음")
                Text("3. 삭제 후 복구할 방법이 없음!")
                Text("4. Compose의 선언적 패턴과 맞지 않음")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "사용된 코드 (Toast)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// Context가 필요합니다
val context = LocalContext.current

Button(onClick = {
    items = items - item

    // Toast - 액션 버튼 없음!
    Toast.makeText(
        context,
        "${'$'}item 삭제됨",
        Toast.LENGTH_SHORT
    ).show()

    // 실행 취소 방법 없음...
})
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Toast vs Snackbar 비교 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Toast vs Snackbar",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Toast",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text("Context 필요", style = MaterialTheme.typography.bodySmall)
                        Text("액션 버튼 X", style = MaterialTheme.typography.bodySmall)
                        Text("스와이프 닫기 X", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Snackbar",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text("Context 불필요", style = MaterialTheme.typography.bodySmall)
                        Text("액션 버튼 O", style = MaterialTheme.typography.bodySmall)
                        Text("스와이프 닫기 O", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
