package com.example.back_handler

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: BackHandler 없이 발생하는 문제
 *
 * 시나리오: 메모 작성 중 실수로 뒤로가기
 * - 사용자가 긴 메모를 작성 중
 * - 실수로 뒤로가기 버튼/제스처 사용
 * - 확인 없이 바로 화면 이탈
 * - 작성 내용 전부 손실!
 */
@Composable
fun ProblemScreen() {
    var memoText by remember { mutableStateOf("") }
    var backPressCount by remember { mutableIntStateOf(0) }

    // 문제: BackHandler가 없음!
    // 뒤로가기 시 확인 없이 바로 이전 화면으로 이동
    // (실제 앱에서는 화면이 닫히고 데이터가 손실됨)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "문제 상황",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        이 화면에는 BackHandler가 없습니다.

                        메모를 작성한 후 뒤로가기 버튼을 누르면:
                        • 확인 없이 바로 화면 이탈
                        • 작성한 내용 전부 손실
                        • 사용자는 복구할 방법 없음

                        아래에서 메모를 작성하고 뒤로가기를 눌러보세요.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 메모 작성 영역
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "메모 작성",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = memoText,
                    onValueChange = { memoText = it },
                    label = { Text("메모 내용") },
                    placeholder = { Text("여기에 메모를 작성하세요...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 글자 수 표시
                Text(
                    text = "글자 수: ${memoText.length}자",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (memoText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = "지금 뒤로가기를 누르면 이 내용이 모두 사라집니다!",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // 뒤로가기 시뮬레이션 버튼
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "뒤로가기 시뮬레이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "실제 앱에서는 시스템 뒤로가기 버튼을 사용하지만,\n여기서는 버튼으로 시뮬레이션합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        backPressCount++
                        // 실제로는 여기서 화면이 닫히고 데이터가 손실됨
                        // 데모에서는 카운트만 증가
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("뒤로가기 누르기 (시뮬레이션)")
                }

                if (backPressCount > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "뒤로가기 ${backPressCount}회 눌림\n" +
                                    "실제 앱이었다면 확인 없이 바로 나갔을 것입니다!",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // 문제가 있는 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
@Composable
fun MemoScreen() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it }
    )

    // BackHandler 없음!
    // 뒤로가기 시 확인 없이 바로 이탈
    // 작성 내용 손실됨
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}
