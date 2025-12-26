package com.example.remember_saveable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: remember만 사용했을 때 Configuration Change에서 상태 손실
 *
 * 이 화면에서는 remember만 사용했을 때 발생하는 문제를 보여줍니다:
 * 1. 화면 회전 시 카운터 값 초기화
 * 2. 화면 회전 시 텍스트 입력 손실
 * 3. 화면 회전 시 폼 데이터 손실
 *
 * 실습: 각 예시에서 값을 변경한 후 화면을 회전해보세요!
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 경고 배너
        RotationWarningBanner()

        HorizontalDivider()

        // 문제 1: 카운터 상태 손실
        Text(
            text = "문제 1: 카운터 상태 손실",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        BrokenCounter()
        ProblemExplanationCard(
            explanation = "remember는 Recomposition 간에만 값을 유지합니다. " +
                    "화면 회전 시 Activity가 재생성되면서 Composition이 새로 시작되고, " +
                    "모든 remember 값이 초기화됩니다."
        )

        HorizontalDivider()

        // 문제 2: 텍스트 입력 손실
        Text(
            text = "문제 2: 텍스트 입력 손실",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        BrokenTextField()
        ProblemExplanationCard(
            explanation = "긴 텍스트를 입력한 후 화면을 회전하면 입력 내용이 모두 사라집니다. " +
                    "사용자 경험에 큰 영향을 미치는 문제입니다."
        )

        HorizontalDivider()

        // 문제 3: 폼 데이터 손실
        Text(
            text = "문제 3: 폼 데이터 손실",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        BrokenForm()
        ProblemExplanationCard(
            explanation = "여러 필드가 있는 폼에서 화면 회전 시 모든 입력이 초기화됩니다. " +
                    "회원가입, 설문조사 등의 화면에서 치명적인 UX 문제가 됩니다."
        )

        HorizontalDivider()

        // 문제 4: 체크박스/토글 상태 손실
        Text(
            text = "문제 4: 선택 상태 손실",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        BrokenCheckboxes()
        ProblemExplanationCard(
            explanation = "체크박스나 라디오 버튼의 선택 상태도 화면 회전 시 초기화됩니다. " +
                    "사용자가 여러 항목을 선택한 후 회전하면 모두 해제됩니다."
        )
    }
}

@Composable
fun RotationWarningBanner() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Column {
                Text(
                    text = "화면을 회전해보세요!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "아래 예시들에서 값을 변경한 후 화면을 회전하면 " +
                            "모든 상태가 초기화되는 것을 확인할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

/**
 * 문제 1: remember만 사용한 카운터
 * 화면 회전 시 0으로 초기화됨
 */
@Composable
fun BrokenCounter() {
    // remember만 사용 - 화면 회전 시 초기화됨!
    var count by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "카운터",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "$count",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(onClick = { count-- }) {
                    Text("-1")
                }
                Button(onClick = { count++ }) {
                    Text("+1")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "var count by remember { mutableStateOf(0) }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

/**
 * 문제 2: remember만 사용한 TextField
 * 화면 회전 시 입력 내용 손실
 */
@Composable
fun BrokenTextField() {
    // remember만 사용 - 화면 회전 시 입력 내용 손실!
    var text by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("메모를 입력하세요") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
            Text(
                text = "입력된 글자 수: ${text.length}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "var text by remember { mutableStateOf(\"\") }",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE65100)
            )
        }
    }
}

/**
 * 문제 3: remember만 사용한 폼
 * 화면 회전 시 모든 입력 손실
 */
@Composable
fun BrokenForm() {
    // 모든 필드가 remember만 사용 - 화면 회전 시 전체 손실!
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3E5F5)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "회원가입 폼 (예시)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("이름") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("전화번호") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            // 입력 상태 표시
            if (name.isNotEmpty() || email.isNotEmpty() || phone.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "현재 입력된 데이터:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text("이름: $name", style = MaterialTheme.typography.bodySmall)
                        Text("이메일: $email", style = MaterialTheme.typography.bodySmall)
                        Text("전화번호: $phone", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Text(
                text = "모든 필드가 remember만 사용",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF7B1FA2)
            )
        }
    }
}

/**
 * 문제 4: remember만 사용한 체크박스
 * 화면 회전 시 선택 상태 초기화
 */
@Composable
fun BrokenCheckboxes() {
    // remember만 사용 - 화면 회전 시 선택 초기화!
    var option1 by remember { mutableStateOf(false) }
    var option2 by remember { mutableStateOf(false) }
    var option3 by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "관심 분야 선택",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = option1, onCheckedChange = { option1 = it })
                Text("Android 개발")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = option2, onCheckedChange = { option2 = it })
                Text("iOS 개발")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = option3, onCheckedChange = { option3 = it })
                Text("웹 개발")
            }
            Text(
                text = "선택된 항목: ${listOf(option1, option2, option3).count { it }}개",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "var optionX by remember { mutableStateOf(false) }",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF2E7D32)
            )
        }
    }
}

@Composable
fun ProblemExplanationCard(explanation: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "왜 문제인가요?",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
