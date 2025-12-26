package com.example.back_handler

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 1: 기본 BackHandler
 *
 * 요구사항:
 * - 카운터가 0보다 크면 BackHandler 활성화
 * - 뒤로가기 시 "리셋하시겠습니까?" 다이얼로그 표시
 * - 확인 시 count = 0, 취소 시 유지
 *
 * 힌트:
 * - BackHandler(enabled = ???) { }
 * - enabled 조건: count > 0
 */
@Composable
fun Practice1_CounterBackHandler() {
    var count by remember { mutableIntStateOf(0) }
    var showResetDialog by remember { mutableStateOf(false) }

    // TODO: BackHandler 구현
    // 힌트: count > 0 일 때만 활성화
    // 뒤로가기 시 showResetDialog = true

    // ========== 정답 ==========
    // BackHandler(enabled = count > 0) {
    //     showResetDialog = true
    // }
    // ==========================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "연습 1: 기본 BackHandler",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        • count > 0 일 때만 BackHandler 활성화
                        • 뒤로가기 시 리셋 확인 다이얼로그

                        힌트:
                        BackHandler(enabled = count > 0) {
                            showResetDialog = true
                        }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 카운터 UI
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = { count++ }) {
                        Text("+1")
                    }
                    OutlinedButton(
                        onClick = { count = 0 },
                        enabled = count > 0
                    ) {
                        Text("리셋")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (count > 0)
                        "BackHandler 활성화됨 - 뒤로가기 시 확인"
                    else
                        "BackHandler 비활성화됨",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (count > 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    // 리셋 확인 다이얼로그
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("카운터를 리셋하시겠습니까?") },
            text = { Text("현재 값: $count") },
            confirmButton = {
                TextButton(
                    onClick = {
                        count = 0
                        showResetDialog = false
                    }
                ) {
                    Text("리셋")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}

/**
 * 연습 2: 폼 변경 감지
 *
 * 요구사항:
 * - 이름과 이메일 TextField
 * - 초기값과 현재값이 다르면 hasChanges = true
 * - hasChanges일 때만 BackHandler 활성화
 * - 뒤로가기 시 "저장하지 않고 나가시겠습니까?"
 *
 * 힌트:
 * - 초기값 저장: remember { "초기값" }
 * - 변경 감지: currentValue != initialValue
 */
@Composable
fun Practice2_FormBackHandler() {
    val initialName = remember { "홍길동" }
    val initialEmail = remember { "hong@example.com" }

    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var showExitDialog by remember { mutableStateOf(false) }

    // TODO: 변경사항 감지 및 BackHandler 구현
    // 힌트: name != initialName || email != initialEmail

    // ========== 정답 ==========
    // val hasChanges = name != initialName || email != initialEmail
    //
    // BackHandler(enabled = hasChanges) {
    //     showExitDialog = true
    // }
    // ==========================

    val hasChanges = name != initialName || email != initialEmail

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "연습 2: 폼 변경 감지",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        • 초기값과 다르면 BackHandler 활성화
                        • 뒤로가기 시 저장 확인 다이얼로그

                        힌트:
                        val hasChanges = name != initialName ||
                                         email != initialEmail

                        BackHandler(enabled = hasChanges) { ... }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 폼 UI
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "프로필 수정",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("이름") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("이메일") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 변경 상태 표시
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasChanges)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = if (hasChanges)
                            "변경사항 있음 - BackHandler 활성화"
                        else
                            "변경사항 없음 - 초기값과 동일",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            name = initialName
                            email = initialEmail
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("초기값 복원")
                    }
                    Button(
                        onClick = { /* 저장 로직 */ },
                        modifier = Modifier.weight(1f),
                        enabled = hasChanges
                    ) {
                        Text("저장")
                    }
                }
            }
        }
    }

    // 저장 확인 다이얼로그
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("저장하지 않고 나가시겠습니까?") },
            text = { Text("변경사항이 저장되지 않습니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // 변경사항 버리고 나가기
                        name = initialName
                        email = initialEmail
                        showExitDialog = false
                    }
                ) {
                    Text("나가기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("계속 수정")
                }
            }
        )
    }
}

/**
 * 연습 3: 중첩 BackHandler
 *
 * 요구사항:
 * - "모달 열기" 버튼 → showModal = true
 * - 모달이 열려있으면: 뒤로가기로 모달만 닫기 (innermost)
 * - 모달이 닫혀있으면: 뒤로가기로 종료 확인 다이얼로그
 *
 * 힌트:
 * - BackHandler는 if 문 안에 배치 가능
 * - innermost wins 규칙 활용
 */
@Composable
fun Practice3_NestedBackHandler() {
    var showModal by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var modalCloseCount by remember { mutableIntStateOf(0) }
    var exitDialogCount by remember { mutableIntStateOf(0) }

    // TODO: 외부 BackHandler (종료 확인)
    // 힌트: 항상 활성화, 뒤로가기 시 showExitDialog = true

    // ========== 정답 ==========
    // BackHandler {
    //     exitDialogCount++
    //     showExitDialog = true
    // }
    // ==========================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "연습 3: 중첩 BackHandler",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        • 모달 열림: 뒤로가기로 모달 닫기
                        • 모달 닫힘: 뒤로가기로 종료 확인

                        힌트:
                        BackHandler { showExitDialog = true }

                        if (showModal) {
                            BackHandler { showModal = false }
                        }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 상태 표시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("모달 상태:")
                    Text(
                        text = if (showModal) "열림" else "닫힘",
                        fontWeight = FontWeight.Bold,
                        color = if (showModal)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("활성 핸들러:")
                    Text(
                        text = if (showModal) "내부 (모달 닫기)" else "외부 (종료 확인)",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "모달 닫기: ${modalCloseCount}회 | 종료 확인: ${exitDialogCount}회",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 모달 열기 버튼
        Button(
            onClick = { showModal = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("모달 열기")
        }
    }

    // 모달 (내부 BackHandler 포함)
    if (showModal) {
        // TODO: 내부 BackHandler (모달 닫기)
        // 힌트: showModal이 true일 때만 존재
        // 이 핸들러가 innermost → 우선 처리

        // ========== 정답 ==========
        // BackHandler {
        //     modalCloseCount++
        //     showModal = false
        // }
        // ==========================

        AlertDialog(
            onDismissRequest = {
                modalCloseCount++
                showModal = false
            },
            title = { Text("모달") },
            text = {
                Text(
                    "뒤로가기를 누르면 이 모달만 닫힙니다.\n" +
                            "(외부 BackHandler는 동작 안 함)"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        modalCloseCount++
                        showModal = false
                    }
                ) {
                    Text("닫기")
                }
            }
        )
    }

    // 종료 확인 다이얼로그
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("앱을 종료하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("종료")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}
