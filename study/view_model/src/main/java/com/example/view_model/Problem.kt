package com.example.view_model

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: ViewModel 없이 상태 관리의 문제점
 *
 * 이 화면에서는 remember만 사용했을 때 발생하는 문제를 보여줍니다:
 * 1. Configuration Change(화면 회전) 시 상태 손실
 * 2. 비즈니스 로직과 UI의 혼재
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
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "remember만 사용하면 화면 회전(Configuration Change) 시 상태가 손실됩니다.\n" +
                            "아래 예제에서 카운터를 증가시킨 후 화면을 회전해보세요!",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: Configuration Change 시 상태 손실
        Text(
            text = "문제 1: 화면 회전 시 상태 손실",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        BrokenCounterWithRemember()

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
                Text(
                    text = "remember는 Composable의 수명주기에 바인딩됩니다. " +
                            "화면 회전 시 Activity가 재생성되면서 모든 Composable도 재생성되어 " +
                            "remember 상태가 초기화됩니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // 문제 2: 비즈니스 로직 혼재
        Text(
            text = "문제 2: UI에 비즈니스 로직 혼재",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        BrokenFormWithBusinessLogic()

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
                Text(
                    text = "유효성 검사, 데이터 변환 등의 비즈니스 로직이 Composable 안에 있으면:\n" +
                            "- 단위 테스트가 어려움 (Compose Test Rule 필요)\n" +
                            "- 코드 재사용 불가\n" +
                            "- 관심사 분리(SoC) 원칙 위반",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // 해결책 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ViewModel을 사용하면:\n" +
                            "1. Configuration Change에서도 상태 유지\n" +
                            "2. 비즈니스 로직을 UI에서 분리\n" +
                            "3. 테스트 용이성 향상\n\n" +
                            "Solution 탭에서 해결 방법을 확인하세요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

/**
 * 문제 1: remember만 사용한 카운터
 * 화면 회전 시 카운터가 0으로 초기화됩니다.
 */
@Composable
fun BrokenCounterWithRemember() {
    // remember만 사용 - 화면 회전 시 손실!
    var count by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "remember 카운터",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledTonalButton(onClick = { count-- }) {
                    Text("-1")
                }
                Button(onClick = { count++ }) {
                    Text("+1")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "var count by remember { mutableStateOf(0) }",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "카운터를 증가시킨 후 화면을 회전해보세요!",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

/**
 * 문제 2: Composable 내부에 비즈니스 로직이 있는 폼
 * 유효성 검사 로직이 UI 코드에 섞여 있습니다.
 */
@Composable
fun BrokenFormWithBusinessLogic() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // 비즈니스 로직이 Composable 안에 있음 - 테스트 어려움!
    val nameError = if (name.isNotEmpty() && name.length < 2) {
        "이름은 2자 이상이어야 합니다"
    } else null

    val emailError = if (email.isNotEmpty() && !email.contains("@")) {
        "올바른 이메일 형식이 아닙니다"
    } else null

    val isValid = name.length >= 2 && email.contains("@")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "회원가입 폼 (문제 있는 코드)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("이름") },
                isError = nameError != null,
                supportingText = {
                    nameError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일") },
                isError = emailError != null,
                supportingText = {
                    emailError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { /* 가입 처리 */ },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("가입하기")
            }

            // 문제점 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFCDD2)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "이 코드의 문제점:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "- 유효성 검사 로직이 Composable 안에 있음\n" +
                                "- 화면 회전 시 입력값 손실\n" +
                                "- 단위 테스트 불가능",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
