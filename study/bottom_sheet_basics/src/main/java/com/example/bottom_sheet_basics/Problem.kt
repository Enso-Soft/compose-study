package com.example.bottom_sheet_basics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Problem: Dialog로 옵션 메뉴를 구현할 때의 문제점
 *
 * 게시물에서 "더보기" 버튼을 누르면 여러 옵션을 보여줘야 합니다.
 * Dialog로 구현하면 다음과 같은 문제가 발생합니다:
 *
 * 1. AlertDialog는 확인/취소용이지 옵션 목록용이 아님
 * 2. confirmButton/dismissButton을 비워둬야 함
 * 3. 드래그로 닫을 수 없어 모바일 UX에 맞지 않음
 * 4. 화면 중앙에 표시되어 엄지로 닿기 어려움
 * 5. 옵션이 많아지면 Dialog가 복잡해짐
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "게시물의 '더보기' 버튼을 눌렀을 때 여러 옵션(공유, 편집, 삭제)을 보여줘야 합니다. " +
                            "Dialog로 구현하면 구조적으로 어색하고 모바일 UX에 맞지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제가 있는 코드 시연
        Text(
            text = "Dialog로 옵션 메뉴 구현 (문제 있음)",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 샘플 게시물 카드
        PostCardWithDialogOptions()

        Spacer(modifier = Modifier.height(24.dp))

        // 문제점 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem("1. AlertDialog는 확인/취소 용도이지 옵션 목록용이 아님")
                ProblemItem("2. confirmButton/dismissButton을 비워둬야 함 (어색한 구조)")
                ProblemItem("3. 드래그로 닫을 수 없음 (모바일 UX 위반)")
                ProblemItem("4. 화면 중앙에 표시됨 (엄지로 닿기 어려움)")
                ProblemItem("5. 옵션이 많아지면 Dialog가 복잡해짐")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 코드 예시 카드
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
AlertDialog(
    onDismissRequest = { ... },
    title = { Text("옵션") },
    text = {
        Column {
            TextButton(...) { Text("공유") }
            TextButton(...) { Text("편집") }
            TextButton(...) { Text("삭제") }
        }
    },
    confirmButton = { },  // 불필요
    dismissButton = { }   // 불필요
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun PostCardWithDialogOptions() {
    var showOptionsDialog by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "샘플 게시물",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "게시물 내용입니다. 더보기 버튼을 눌러보세요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 더보기 버튼
                IconButton(onClick = { showOptionsDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "더보기"
                    )
                }
            }

            // 선택된 옵션 표시
            selectedOption?.let { option ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "선택된 옵션: $option",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // 문제가 있는 Dialog 구현
    if (showOptionsDialog) {
        AlertDialog(
            onDismissRequest = { showOptionsDialog = false },
            title = { Text("옵션") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            selectedOption = "공유"
                            showOptionsDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, null)
                        Spacer(Modifier.width(8.dp))
                        Text("공유하기")
                    }
                    TextButton(
                        onClick = {
                            selectedOption = "편집"
                            showOptionsDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, null)
                        Spacer(Modifier.width(8.dp))
                        Text("편집하기")
                    }
                    TextButton(
                        onClick = {
                            selectedOption = "삭제"
                            showOptionsDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Delete, null)
                        Spacer(Modifier.width(8.dp))
                        Text("삭제하기")
                    }
                }
            },
            // 불필요하지만 필수인 파라미터들
            confirmButton = { },
            dismissButton = { }
        )
    }
}

@Composable
private fun ProblemItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
