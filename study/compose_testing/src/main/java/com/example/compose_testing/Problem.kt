package com.example.compose_testing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * 문제 상황: Semantics가 없는 테스트 불가능한 UI
 *
 * 이 화면은 로그인 폼을 구현했지만, 테스트하기 매우 어렵습니다:
 *
 * 1. Icon에 contentDescription이 없음 → 접근성 도구/테스트가 인식 못함
 * 2. 두 TextField를 구분할 방법 없음 → 어느 것이 이메일? 비밀번호?
 * 3. Button에 testTag 없음 → 텍스트로만 찾아야 함 (다국어 시 깨짐)
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
                    text = "문제: 테스트 불가능한 UI",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 로그인 폼은 Semantics 정보가 없어 테스트하기 어렵습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 테스트 불가능한 로그인 폼
        UntestableLoginForm()

        Spacer(modifier = Modifier.height(24.dp))

        // 문제점 설명
        ProblemExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 코드 예시
        TestCodeExampleCard()
    }
}

/**
 * 테스트하기 어려운 로그인 폼
 * - contentDescription 없는 아이콘
 * - 구분 불가능한 TextField
 * - testTag 없는 버튼
 */
@Composable
private fun UntestableLoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "로그인",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 문제 1: contentDescription이 null
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,  // ❌ 테스트/접근성 불가
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 문제 2: 두 TextField 구분 불가 (testTag 없음)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null  // ❌
                    )
                },
                placeholder = { Text("이메일") },
                modifier = Modifier.fillMaxWidth()
                // testTag 없음!
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null  // ❌
                    )
                },
                placeholder = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
                // testTag 없음!
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 문제 3: testTag 없음, 텍스트로만 찾아야 함
            Button(
                onClick = {
                    loginResult = if (email.isNotEmpty() && password.isNotEmpty()) {
                        "로그인 성공!"
                    } else {
                        "이메일과 비밀번호를 입력하세요"
                    }
                },
                modifier = Modifier.fillMaxWidth()
                // testTag 없음! 다국어 지원 시 테스트 깨짐
            ) {
                Text("로그인")
            }

            if (loginResult.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = loginResult,
                    color = if (loginResult.contains("성공"))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "왜 테스트가 어려운가?",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            ProblemItem(
                number = "1",
                title = "Icon contentDescription = null",
                description = "접근성 서비스와 테스트가 이 아이콘을 인식하지 못합니다."
            )

            ProblemItem(
                number = "2",
                title = "TextField 구분 불가",
                description = "두 TextField를 구분할 수 있는 testTag나 label이 없습니다. 테스트에서 어떤 필드가 이메일인지 알 수 없습니다."
            )

            ProblemItem(
                number = "3",
                title = "Button 텍스트 의존",
                description = "\"로그인\" 텍스트로만 버튼을 찾습니다. 다국어 지원 시 영어로 바뀌면 테스트가 모두 실패합니다."
            )
        }
    }
}

@Composable
private fun ProblemItem(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TestCodeExampleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "테스트 코드 (실패 예상)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
@Test
fun loginTest() {
    composeTestRule.setContent {
        UntestableLoginForm()
    }

    // 어떤 TextField가 이메일인지 알 수 없음!
    composeTestRule
        .onNodeWithText("이메일")  // placeholder로 찾기?
        .performTextInput("test@email.com")
        // ❌ placeholder는 입력 후 사라짐!

    // 다국어 시 깨짐
    composeTestRule
        .onNodeWithText("로그인")  // 영어면?
        .performClick()
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
