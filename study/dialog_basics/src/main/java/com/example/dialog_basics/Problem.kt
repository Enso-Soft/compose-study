package com.example.dialog_basics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: Dialog 없이 발생하는 문제
 *
 * 시나리오: 연락처 삭제 시 확인 없이 바로 삭제
 * - 사용자가 실수로 삭제 버튼 클릭
 * - 확인 없이 바로 삭제됨
 * - 복구할 방법 없음!
 *
 * 또는 인라인 확인 UI 시도
 * - 레이아웃이 변경됨
 * - 사용자 경험 저하
 */
@Composable
fun ProblemScreen() {
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
                label = { Text("확인 없음") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("인라인") }
            )
        }

        when (selectedDemo) {
            0 -> NoConfirmationDemo()
            1 -> InlineConfirmationDemo()
        }
    }
}

/**
 * 문제 1: 확인 없이 바로 삭제
 */
@Composable
fun NoConfirmationDemo() {
    // 샘플 연락처 목록
    var contacts by remember {
        mutableStateOf(
            listOf("홍길동", "김철수", "이영희", "박민수", "최지우")
        )
    }
    var deletedCount by remember { mutableIntStateOf(0) }

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
                        text = "문제 상황: 확인 없이 바로 삭제",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        삭제 버튼을 누르면 확인 없이 바로 삭제됩니다.

                        - 실수로 누르면 복구 불가능
                        - 사용자에게 경고 없음
                        - 중요한 데이터 손실 위험

                        아래 연락처를 삭제해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 삭제 통계
        if (deletedCount > 0) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "실수로 삭제된 연락처: ${deletedCount}개",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        // 연락처 목록
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연락처 목록",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (contacts.isEmpty()) {
                    Text(
                        text = "모든 연락처가 삭제되었습니다!",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Button(
                        onClick = {
                            contacts = listOf("홍길동", "김철수", "이영희", "박민수", "최지우")
                            deletedCount = 0
                        }
                    ) {
                        Text("연락처 복원")
                    }
                } else {
                    contacts.forEach { contact ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(contact)
                            // 문제: 확인 없이 바로 삭제!
                            IconButton(
                                onClick = {
                                    contacts = contacts.filter { it != contact }
                                    deletedCount++
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "삭제",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        if (contact != contacts.last()) {
                            HorizontalDivider()
                        }
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
IconButton(
    onClick = {
        // 확인 없이 바로 삭제!
        contacts = contacts.filter {
            it != contact
        }
    }
) {
    Icon(Icons.Default.Delete, "삭제")
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

/**
 * 문제 2: 인라인 확인 UI
 * - 레이아웃이 변경됨
 * - 사용자 경험 저하
 */
@Composable
fun InlineConfirmationDemo() {
    var items by remember {
        mutableStateOf(listOf("항목 1", "항목 2", "항목 3"))
    }
    var confirmingItem by remember { mutableStateOf<String?>(null) }

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
                        text = "문제 상황: 인라인 확인 UI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        삭제 버튼 클릭 시 인라인으로 확인 UI가 표시됩니다.

                        문제점:
                        - 레이아웃이 변경되어 다른 항목이 밀림
                        - 확인 UI가 눈에 잘 띄지 않음
                        - 여러 항목 동시 확인 시 혼란

                        삭제 버튼을 눌러 확인해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 항목 목록
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "항목 목록",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (items.isEmpty()) {
                    Text(
                        text = "모든 항목이 삭제되었습니다!",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Button(
                        onClick = {
                            items = listOf("항목 1", "항목 2", "항목 3")
                            confirmingItem = null
                        }
                    ) {
                        Text("항목 복원")
                    }
                } else {
                    items.forEach { item ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item)
                                Button(
                                    onClick = { confirmingItem = item },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("삭제")
                                }
                            }

                            // 문제: 인라인 확인 UI가 레이아웃을 변경함
                            if (confirmingItem == item) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Text(
                                            text = "정말 삭제하시겠습니까?",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    items = items.filter { it != item }
                                                    confirmingItem = null
                                                }
                                            ) {
                                                Text("예, 삭제")
                                            }
                                            OutlinedButton(
                                                onClick = { confirmingItem = null }
                                            ) {
                                                Text("취소")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (item != items.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "인라인 확인 UI의 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        - 확인 UI가 나타나면 다른 항목들이 밀립니다
                        - 사용자 주의를 명확히 끌지 못합니다
                        - 중요한 결정에 적합하지 않습니다

                        Dialog를 사용하면 이 문제가 해결됩니다!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
