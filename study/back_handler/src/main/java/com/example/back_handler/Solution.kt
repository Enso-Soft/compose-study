package com.example.back_handler

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Solution: BackHandler를 사용한 해결책
 *
 * 핵심 포인트:
 * 1. enabled 파라미터로 조건부 활성화
 * 2. 확인 다이얼로그로 사용자에게 선택권 제공
 * 3. 중첩 시 innermost wins 규칙 활용
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 데모 선택
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("기본") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("중첩") }
            )
        }

        when (selectedDemo) {
            0 -> BasicBackHandlerDemo()
            1 -> NestedBackHandlerDemo()
        }
    }
}

/**
 * 데모 1: 기본 BackHandler 사용
 * - 변경사항 있을 때만 활성화
 * - 확인 다이얼로그 표시
 */
@Composable
fun BasicBackHandlerDemo() {
    var memoText by remember { mutableStateOf("") }
    var showExitDialog by remember { mutableStateOf(false) }
    var backHandlerTriggered by remember { mutableIntStateOf(0) }

    // 변경사항이 있는지 확인
    val hasChanges = memoText.isNotEmpty()

    // ✅ 핵심: BackHandler로 뒤로가기 가로채기
    BackHandler(enabled = hasChanges) {
        // 뒤로가기 시 다이얼로그 표시
        showExitDialog = true
        backHandlerTriggered++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "해결책: BackHandler 사용",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        BackHandler를 사용하여 뒤로가기를 가로챕니다.

                        • enabled: 변경사항 있을 때만 활성화
                        • 람다: 뒤로가기 시 다이얼로그 표시
                        • 결과: 사용자에게 선택권 제공

                        메모를 작성하고 뒤로가기를 눌러보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 상태 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (hasChanges)
                    MaterialTheme.colorScheme.tertiaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "BackHandler 상태",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (hasChanges)
                        "활성화됨 - 뒤로가기 시 다이얼로그 표시"
                    else
                        "비활성화됨 - 기본 뒤로가기 동작",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (backHandlerTriggered > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "BackHandler 트리거됨: ${backHandlerTriggered}회",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // 메모 작성
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
                        .height(150.dp),
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "글자 수: ${memoText.length}자",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 올바른 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드",
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
val hasChanges = text.isNotEmpty()

BackHandler(enabled = hasChanges) {
    showExitDialog = true
}

if (showExitDialog) {
    AlertDialog(
        title = { Text("저장하지 않고 나가시겠습니까?") },
        confirmButton = { /* 나가기 */ },
        dismissButton = { /* 계속 작성 */ }
    )
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

    // 확인 다이얼로그
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("저장하지 않고 나가시겠습니까?") },
            text = { Text("작성 중인 메모가 삭제됩니다.\n\n글자 수: ${memoText.length}자") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // 실제로는 여기서 화면 종료
                        memoText = ""
                        showExitDialog = false
                    }
                ) {
                    Text("나가기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("계속 작성")
                }
            }
        )
    }
}

/**
 * 데모 2: 중첩 BackHandler (Innermost Wins)
 * - 모달이 열려있으면 모달 BackHandler 우선
 * - 모달이 닫혀있으면 외부 BackHandler 동작
 */
@Composable
fun NestedBackHandlerDemo() {
    var showModal by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var outerHandlerCount by remember { mutableIntStateOf(0) }
    var innerHandlerCount by remember { mutableIntStateOf(0) }

    // 외부 BackHandler - 종료 확인
    BackHandler {
        outerHandlerCount++
        showExitDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "중첩 BackHandler: Innermost Wins",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        BackHandler가 중첩되면 가장 안쪽(innermost)이 우선됩니다.

                        • 모달 닫힘: 외부 BackHandler → 종료 확인
                        • 모달 열림: 내부 BackHandler → 모달 닫기

                        모달을 열고 뒤로가기를 눌러보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 상태 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "활성 BackHandler",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (showModal)
                        "내부 (모달 닫기) - innermost 우선"
                    else
                        "외부 (종료 확인)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "외부 트리거: ${outerHandlerCount}회 | 내부 트리거: ${innerHandlerCount}회",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
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

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "중첩 패턴 코드",
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
// 외부 BackHandler
BackHandler {
    showExitDialog = true
}

if (showModal) {
    // 내부 BackHandler - 이게 우선!
    BackHandler {
        showModal = false
    }
    ModalContent()
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

    // 모달 (내부 BackHandler 포함)
    if (showModal) {
        // ✅ 핵심: 중첩된 BackHandler - innermost wins!
        BackHandler {
            innerHandlerCount++
            showModal = false
        }

        AlertDialog(
            onDismissRequest = { showModal = false },
            title = { Text("모달 열림") },
            text = {
                Text(
                    "뒤로가기를 누르면 이 모달만 닫힙니다.\n" +
                            "(외부 BackHandler는 동작하지 않음)"
                )
            },
            confirmButton = {
                TextButton(onClick = { showModal = false }) {
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
