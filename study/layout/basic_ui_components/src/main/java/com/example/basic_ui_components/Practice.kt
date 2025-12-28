package com.example.basic_ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 연습 문제: 기본 UI 컴포넌트 활용하기
 */

// ============================================================
// 연습 1: Text 스타일링
// ============================================================

@Composable
fun Practice1_TextScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 1: Text 스타일링",
            description = "다양한 Text 스타일을 적용해보세요"
        ) {
            Text("아래 Text 컴포넌트들을 스타일링해보세요:")

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 제목 스타일 적용
            // 힌트: style = MaterialTheme.typography.headlineLarge
            Text("제목 텍스트")  // 이 줄을 수정하세요

            // 정답:
            // Text(
            //     text = "제목 텍스트",
            //     style = MaterialTheme.typography.headlineLarge,
            //     fontWeight = FontWeight.Bold
            // )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: 본문 스타일 + 색상 적용
            // 힌트: color = MaterialTheme.colorScheme.primary
            Text("본문 텍스트")  // 이 줄을 수정하세요

            // 정답:
            // Text(
            //     text = "본문 텍스트",
            //     style = MaterialTheme.typography.bodyLarge,
            //     color = MaterialTheme.colorScheme.primary
            // )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: 캡션 스타일 + 커스텀 폰트 크기
            // 힌트: fontSize = 12.sp, style = MaterialTheme.typography.labelSmall
            Text("캡션 텍스트")  // 이 줄을 수정하세요

            // 정답:
            // Text(
            //     text = "캡션 텍스트",
            //     style = MaterialTheme.typography.labelSmall,
            //     color = MaterialTheme.colorScheme.onSurfaceVariant
            // )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "MaterialTheme.typography.headlineLarge/Medium/Small",
                "MaterialTheme.typography.bodyLarge/Medium/Small",
                "MaterialTheme.typography.labelLarge/Medium/Small",
                "fontWeight = FontWeight.Bold",
                "color = MaterialTheme.colorScheme.primary",
                "fontSize = 24.sp"
            )
        )
    }
}

// ============================================================
// 연습 2: Button과 카운터
// ============================================================

@Composable
fun Practice2_ButtonScreen() {
    // TODO: count 상태를 remember로 관리하세요
    // 힌트: var count by remember { mutableIntStateOf(0) }
    var count = 0  // 이 줄을 수정하세요

    // 정답:
    // var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 2: Button과 카운터",
            description = "Button으로 카운터를 구현해보세요"
        ) {
            // 카운트 표시
            Text(
                text = "카운트: $count",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 다양한 버튼 구현
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO: +1 버튼
                Button(
                    onClick = { /* TODO: count++ */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+1")
                }

                // 정답:
                // Button(
                //     onClick = { count++ },
                //     modifier = Modifier.weight(1f)
                // ) {
                //     Text("+1")
                // }

                // TODO: -1 버튼 (OutlinedButton 사용)
                OutlinedButton(
                    onClick = { /* TODO: count-- */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("-1")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO: +10 버튼 (FilledTonalButton 사용)
                FilledTonalButton(
                    onClick = { /* TODO: count += 10 */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+10")
                }

                // TODO: 리셋 버튼 (TextButton 사용)
                TextButton(
                    onClick = { /* TODO: count = 0 */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(Modifier.width(4.dp))
                    Text("리셋")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 추가 도전: 조건부 버튼
            Text("추가 도전:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // TODO: count가 음수면 비활성화되는 버튼
            // 힌트: enabled = count >= 0
            Button(
                onClick = { /* TODO */ },
                enabled = true,  // 이 줄을 수정하세요
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("count가 음수면 비활성")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "var count by remember { mutableIntStateOf(0) }",
                "Button(onClick = { count++ }) { Text(\"+1\") }",
                "OutlinedButton, FilledTonalButton, TextButton",
                "enabled = count >= 0 으로 조건부 활성화"
            )
        )
    }
}

// ============================================================
// 연습 3: TextField 입력 폼
// ============================================================

@Composable
fun Practice3_TextFieldScreen() {
    // TODO: 각 TextField를 위한 상태 선언
    // 힌트: var name by remember { mutableStateOf("") }
    var name = ""  // 이 줄을 수정하세요
    var email = ""  // 이 줄을 수정하세요
    var message = ""  // 이 줄을 수정하세요

    // 정답:
    // var name by remember { mutableStateOf("") }
    // var email by remember { mutableStateOf("") }
    // var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 3: TextField 입력 폼",
            description = "입력한 텍스트를 실시간으로 표시해보세요"
        ) {
            // TODO: 이름 입력 TextField
            OutlinedTextField(
                value = name,
                onValueChange = { /* TODO: name = it */ },
                label = { Text("이름") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth()
            )

            // 정답:
            // OutlinedTextField(
            //     value = name,
            //     onValueChange = { name = it },
            //     label = { Text("이름") },
            //     leadingIcon = { Icon(Icons.Default.Person, null) },
            //     modifier = Modifier.fillMaxWidth()
            // )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: 이메일 입력 TextField
            OutlinedTextField(
                value = email,
                onValueChange = { /* TODO: email = it */ },
                label = { Text("이메일") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                placeholder = { Text("example@email.com") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: 메시지 입력 TextField (여러 줄)
            OutlinedTextField(
                value = message,
                onValueChange = { /* TODO: message = it */ },
                label = { Text("메시지") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 입력 결과 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "입력 결과:",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("이름: $name")
                    Text("이메일: $email")
                    Text("메시지: $message")

                    Spacer(modifier = Modifier.height(8.dp))

                    // TODO: 모든 필드가 채워졌을 때만 활성화되는 전송 버튼
                    // 힌트: enabled = name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()
                    Button(
                        onClick = { /* 전송 로직 */ },
                        enabled = false,  // 이 줄을 수정하세요
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null)
                        Spacer(Modifier.width(8.dp))
                        Text("전송")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "var name by remember { mutableStateOf(\"\") }",
                "OutlinedTextField(value = name, onValueChange = { name = it })",
                "leadingIcon = { Icon(Icons.Default.Person, null) }",
                "singleLine = true 로 한 줄 입력 제한",
                "minLines = 3 으로 여러 줄 입력",
                "enabled = name.isNotEmpty() && ..."
            )
        )
    }
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
private fun PracticeCard(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "힌트",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            hints.forEach { hint ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
