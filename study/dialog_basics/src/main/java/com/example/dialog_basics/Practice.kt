package com.example.dialog_basics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * 연습 1: 기본 확인/취소 다이얼로그
 *
 * 요구사항:
 * - 카운터 값이 0보다 클 때 "리셋" 버튼 활성화
 * - 리셋 버튼 클릭 시 확인 다이얼로그 표시
 * - "확인" 클릭 시 카운터 0으로 리셋
 * - "취소" 클릭 시 다이얼로그만 닫기
 *
 * 힌트:
 * - showDialog 상태 변수 선언
 * - Button onClick에서 showDialog = true
 * - if (showDialog) { AlertDialog(...) }
 */
@Composable
fun Practice1_BasicDialog() {
    var count by remember { mutableIntStateOf(0) }

    // TODO: 다이얼로그 표시 상태 선언
    // 힌트: var showResetDialog by remember { mutableStateOf(false) }

    // ========== 정답 ==========
    // var showResetDialog by remember { mutableStateOf(false) }
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
                        text = "연습 1: 기본 확인/취소 다이얼로그",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        - 리셋 버튼 클릭 시 확인 다이얼로그 표시
                        - 확인 → 카운터 리셋, 취소 → 다이얼로그 닫기

                        구현 단계:
                        1. showResetDialog 상태 선언
                        2. 리셋 버튼에서 showResetDialog = true
                        3. if (showResetDialog) { AlertDialog(...) }
                        4. confirmButton에서 count = 0 후 닫기
                        5. dismissButton에서 다이얼로그 닫기
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

                    // TODO: 리셋 버튼 - 클릭 시 showResetDialog = true
                    OutlinedButton(
                        onClick = {
                            // TODO: 다이얼로그 표시
                            // 힌트: showResetDialog = true

                            // ========== 정답 ==========
                            // showResetDialog = true
                            // ==========================

                            // 임시: 확인 없이 바로 리셋 (문제 상황)
                            count = 0
                        },
                        enabled = count > 0
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("리셋")
                    }
                }
            }
        }

        // 예시 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 구조",
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
var showResetDialog by remember {
    mutableStateOf(false)
}

OutlinedButton(
    onClick = { showResetDialog = true }
) { Text("리셋") }

if (showResetDialog) {
    AlertDialog(
        onDismissRequest = {
            showResetDialog = false
        },
        title = { Text("리셋 확인") },
        text = { Text("카운터를 리셋?") },
        confirmButton = {
            TextButton(onClick = {
                count = 0
                showResetDialog = false
            }) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = {
                showResetDialog = false
            }) { Text("취소") }
        }
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

    // TODO: AlertDialog 구현
    // if (showResetDialog) {
    //     AlertDialog(
    //         onDismissRequest = { showResetDialog = false },
    //         title = { Text("리셋 확인") },
    //         text = { Text("카운터를 리셋하시겠습니까?\n현재 값: $count") },
    //         confirmButton = {
    //             TextButton(
    //                 onClick = {
    //                     count = 0
    //                     showResetDialog = false
    //                 }
    //             ) { Text("확인") }
    //         },
    //         dismissButton = {
    //             TextButton(onClick = { showResetDialog = false }) {
    //                 Text("취소")
    //             }
    //         }
    //     )
    // }
}

/**
 * 연습 2: 입력 다이얼로그
 *
 * 요구사항:
 * - 현재 이름 표시
 * - "이름 변경" 버튼 클릭 시 입력 다이얼로그 표시
 * - TextField에 새 이름 입력
 * - 확인 시 이름 업데이트
 *
 * 힌트:
 * - 다이얼로그용 입력 상태: editText
 * - 다이얼로그 열 때 현재 값으로 초기화
 * - AlertDialog의 text에 OutlinedTextField 배치
 */
@Composable
fun Practice2_InputDialog() {
    var userName by remember { mutableStateOf("홍길동") }

    // TODO: 다이얼로그 관련 상태 선언
    // 힌트:
    // var showEditDialog by remember { mutableStateOf(false) }
    // var editText by remember { mutableStateOf("") }

    // ========== 정답 ==========
    // var showEditDialog by remember { mutableStateOf(false) }
    // var editText by remember { mutableStateOf("") }
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
                        text = "연습 2: 입력 다이얼로그",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        - 이름 변경 버튼 클릭 시 입력 다이얼로그
                        - TextField에 새 이름 입력
                        - 확인 시 이름 업데이트

                        구현 단계:
                        1. showEditDialog, editText 상태 선언
                        2. 버튼에서 editText = userName 후 다이얼로그 열기
                        3. AlertDialog text에 OutlinedTextField 배치
                        4. confirmButton에서 userName = editText
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 현재 이름 표시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "프로필",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "현재 이름",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // TODO: 이름 변경 버튼
                Button(
                    onClick = {
                        // TODO: editText 초기화 후 다이얼로그 열기
                        // 힌트:
                        // editText = userName
                        // showEditDialog = true

                        // ========== 정답 ==========
                        // editText = userName
                        // showEditDialog = true
                        // ==========================
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("이름 변경")
                }
            }
        }

        // 예시 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 구조",
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
if (showEditDialog) {
    AlertDialog(
        onDismissRequest = {
            showEditDialog = false
        },
        title = { Text("이름 변경") },
        text = {
            OutlinedTextField(
                value = editText,
                onValueChange = {
                    editText = it
                },
                label = { Text("새 이름") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    userName = editText
                    showEditDialog = false
                },
                enabled = editText.isNotBlank()
            ) { Text("변경") }
        },
        dismissButton = {
            TextButton(onClick = {
                showEditDialog = false
            }) { Text("취소") }
        }
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

    // TODO: 입력 다이얼로그 구현
    // if (showEditDialog) {
    //     AlertDialog(
    //         onDismissRequest = { showEditDialog = false },
    //         title = { Text("이름 변경") },
    //         text = {
    //             OutlinedTextField(
    //                 value = editText,
    //                 onValueChange = { editText = it },
    //                 label = { Text("새 이름") },
    //                 singleLine = true,
    //                 modifier = Modifier.fillMaxWidth()
    //             )
    //         },
    //         confirmButton = {
    //             TextButton(
    //                 onClick = {
    //                     userName = editText
    //                     showEditDialog = false
    //                 },
    //                 enabled = editText.isNotBlank()
    //             ) { Text("변경") }
    //         },
    //         dismissButton = {
    //             TextButton(onClick = { showEditDialog = false }) {
    //                 Text("취소")
    //             }
    //         }
    //     )
    // }
}

/**
 * 연습 3: Custom Dialog
 *
 * 요구사항:
 * - "프로필 보기" 버튼 클릭 시 커스텀 다이얼로그 표시
 * - Dialog composable 사용 (AlertDialog 아님)
 * - Surface로 둥근 모서리 카드 형태
 * - 프로필 아이콘, 이름, 이메일 표시
 * - 닫기 버튼
 *
 * 힌트:
 * - Dialog(onDismissRequest = {...}) { ... }
 * - Surface(shape = RoundedCornerShape(16.dp)) { ... }
 */
@Composable
fun Practice3_CustomDialog() {
    // TODO: 다이얼로그 표시 상태
    // var showProfileDialog by remember { mutableStateOf(false) }

    // ========== 정답 ==========
    // var showProfileDialog by remember { mutableStateOf(false) }
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
                        text = "연습 3: Custom Dialog",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        - Dialog composable 사용
                        - 프로필 카드 형태의 커스텀 UI
                        - 아이콘, 이름, 이메일, 닫기 버튼

                        구현 단계:
                        1. showProfileDialog 상태 선언
                        2. Dialog(onDismissRequest) { ... }
                        3. Surface(shape = RoundedCornerShape)
                        4. Column으로 내용 배치
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 프로필 보기 버튼
        Button(
            onClick = {
                // TODO: 다이얼로그 열기
                // showProfileDialog = true

                // ========== 정답 ==========
                // showProfileDialog = true
                // ==========================
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Person, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("프로필 보기")
        }

        // 예시 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 구조",
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
if (showProfileDialog) {
    Dialog(
        onDismissRequest = {
            showProfileDialog = false
        }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme
                .colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Person,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text("홍길동")
                Text("hong@example.com")
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    showProfileDialog = false
                }) { Text("닫기") }
            }
        }
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        // AlertDialog vs Dialog 비교
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "AlertDialog vs Dialog",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        AlertDialog:
                        - title, text, confirmButton 등 정해진 구조
                        - Material Design 규격 준수
                        - 간단한 확인/취소에 적합

                        Dialog:
                        - 완전히 자유로운 UI 구성
                        - Surface로 직접 스타일링
                        - 복잡한 커스텀 UI에 적합
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // TODO: Custom Dialog 구현
    // if (showProfileDialog) {
    //     Dialog(
    //         onDismissRequest = { showProfileDialog = false }
    //     ) {
    //         Surface(
    //             shape = RoundedCornerShape(16.dp),
    //             color = MaterialTheme.colorScheme.surface,
    //             tonalElevation = 8.dp
    //         ) {
    //             Column(
    //                 modifier = Modifier.padding(24.dp),
    //                 horizontalAlignment = Alignment.CenterHorizontally
    //             ) {
    //                 Surface(
    //                     shape = RoundedCornerShape(50),
    //                     color = MaterialTheme.colorScheme.primaryContainer,
    //                     modifier = Modifier.size(80.dp)
    //                 ) {
    //                     Icon(
    //                         Icons.Default.Person,
    //                         contentDescription = null,
    //                         modifier = Modifier.padding(20.dp).size(40.dp),
    //                         tint = MaterialTheme.colorScheme.primary
    //                     )
    //                 }
    //                 Spacer(modifier = Modifier.height(16.dp))
    //                 Text(
    //                     text = "홍길동",
    //                     style = MaterialTheme.typography.headlineSmall,
    //                     fontWeight = FontWeight.Bold
    //                 )
    //                 Text(
    //                     text = "hong@example.com",
    //                     style = MaterialTheme.typography.bodyMedium,
    //                     color = MaterialTheme.colorScheme.onSurfaceVariant
    //                 )
    //                 Spacer(modifier = Modifier.height(24.dp))
    //                 Button(
    //                     onClick = { showProfileDialog = false },
    //                     modifier = Modifier.fillMaxWidth()
    //                 ) {
    //                     Text("닫기")
    //                 }
    //             }
    //         }
    //     }
    // }
}
