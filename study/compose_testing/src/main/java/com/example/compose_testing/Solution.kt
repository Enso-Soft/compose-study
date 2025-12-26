package com.example.compose_testing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * 해결책: Semantics를 활용한 테스트 가능한 UI
 *
 * 주요 변경:
 * 1. Icon에 contentDescription 추가 → 접근성 + 테스트 가능
 * 2. TextField에 testTag 추가 → 고유 식별 가능
 * 3. Button에 testTag 추가 → 다국어 상관없이 테스트 가능
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "해결책: 테스트 가능한 UI",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Semantics 정보를 추가하여 테스트와 접근성을 모두 개선했습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 테스트 가능한 로그인 폼
        TestableLoginForm()

        Spacer(modifier = Modifier.height(24.dp))

        // 해결 포인트 설명
        SolutionExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 코드 예시
        TestableCodeExampleCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 베스트 프랙티스
        BestPracticesCard()
    }
}

/**
 * 테스트 가능한 로그인 폼
 * - contentDescription이 있는 아이콘
 * - testTag로 구분 가능한 TextField
 * - testTag가 있는 버튼
 */
@Composable
fun TestableLoginForm() {
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

            // 해결 1: contentDescription 추가
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User avatar",  // ✅ 접근성 + 테스트 가능
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 해결 2: testTag로 TextField 구분
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },  // ✅ 레이블 추가
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = "Email icon"  // ✅
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("email_field")  // ✅ testTag 추가
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },  // ✅ 레이블 추가
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Password icon"  // ✅
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("password_field")  // ✅ testTag 추가
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 해결 3: testTag로 다국어 대응
            Button(
                onClick = {
                    loginResult = if (email.isNotEmpty() && password.isNotEmpty()) {
                        "Login successful!"
                    } else {
                        "Please enter email and password"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_button")  // ✅ testTag 추가
            ) {
                Text("로그인")
            }

            if (loginResult.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = loginResult,
                    color = if (loginResult.contains("successful"))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .testTag("login_result")  // ✅ 결과도 태그 추가
                        .semantics {
                            contentDescription = loginResult
                        }
                )
            }
        }
    }
}

@Composable
private fun SolutionExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Semantics 추가 포인트",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            SolutionItem(
                number = "1",
                title = "contentDescription 추가",
                code = "contentDescription = \"User avatar\"",
                description = "접근성 서비스가 \"User avatar\"라고 읽어줍니다. 테스트에서도 이 값으로 찾을 수 있습니다."
            )

            SolutionItem(
                number = "2",
                title = "testTag로 구분",
                code = "Modifier.testTag(\"email_field\")",
                description = "UI 텍스트와 무관하게 고유 식별자로 요소를 찾습니다. 다국어 지원에도 안전합니다."
            )

            SolutionItem(
                number = "3",
                title = "label 추가",
                code = "label = { Text(\"Email\") }",
                description = "TextField에 의미 있는 레이블을 제공합니다. 접근성과 UX 모두 개선됩니다."
            )
        }
    }
}

@Composable
private fun SolutionItem(
    number: String,
    title: String,
    code: String,
    description: String
) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = code,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TestableCodeExampleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "테스트 코드 (성공!)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
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
fun login_withValidCredentials_showsSuccess() {
    composeTestRule.setContent {
        TestableLoginForm()
    }

    // testTag로 정확히 찾기
    composeTestRule
        .onNodeWithTag("email_field")
        .performTextInput("test@email.com")

    composeTestRule
        .onNodeWithTag("password_field")
        .performTextInput("password123")

    // 다국어 상관없이 동작
    composeTestRule
        .onNodeWithTag("login_button")
        .performClick()

    // 결과 확인
    composeTestRule
        .onNodeWithTag("login_result")
        .assertTextContains("successful")
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

@Composable
private fun BestPracticesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Best Practices",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))

            BestPracticeItem(
                title = "Semantics 우선순위",
                items = listOf(
                    "1. contentDescription (접근성 + 테스트)",
                    "2. text (자연스러운 매칭)",
                    "3. testTag (필요한 경우만)"
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            BestPracticeItem(
                title = "testTag 사용 시기",
                items = listOf(
                    "- 동일한 요소가 여러 개일 때 (리스트 아이템)",
                    "- 다국어 지원이 필요할 때",
                    "- contentDescription이 적절하지 않을 때"
                )
            )
        }
    }
}

@Composable
private fun BestPracticeItem(title: String, items: List<String>) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        items.forEach { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f),
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}
