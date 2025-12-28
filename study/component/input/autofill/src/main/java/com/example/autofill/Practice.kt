package com.example.autofill

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 로그인 폼 (쉬움)
 *
 * 사용자 이름과 비밀번호 필드에 적절한 ContentType을 설정하세요.
 */
@Composable
fun Practice1_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 로그인 폼",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("사용자 이름과 비밀번호 필드에 적절한 ContentType을 설정하세요.")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "목표: ContentType.Username과 ContentType.Password 사용법 익히기",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- Modifier.semantics { contentType = ... } 사용")
                Text("- 비밀번호 필드는 PasswordVisualTransformation도 적용")
                Text("- 사용자 이름: ContentType.Username")
                Text("- 비밀번호: ContentType.Password")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "직접 구현해보세요:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Practice1_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice1_Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 아래 TextField들에 적절한 ContentType을 설정하세요

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // TODO: 사용자 이름 필드 - ContentType.Username 설정 필요
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("사용자 이름") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            // TODO: 여기에 .semantics { contentType = ContentType.Username } 추가
            singleLine = true
        )

        // TODO: 비밀번호 필드 - ContentType.Password 설정 필요
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            // TODO: 여기에 .semantics { contentType = ContentType.Password } 추가
            singleLine = true
        )

        Button(
            onClick = { /* 로그인 처리 */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인")
        }
    }
}

@Composable
private fun Practice1_Answer() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("사용자 이름") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.Username  // 정답!
                },
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.Password  // 정답!
                },
            singleLine = true
        )

        Text(
            text = "ContentType.Username과 ContentType.Password가 적용되었습니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 연습 문제 2: 유연한 로그인 필드 (중간)
 *
 * 이메일 또는 전화번호로 로그인할 수 있는 필드를 만드세요.
 */
@Composable
fun Practice2_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 유연한 로그인 필드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("이메일 또는 전화번호로 로그인할 수 있는 필드를 만드세요.")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "목표: 다중 ContentType 조합 (+ 연산자) 익히기",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- + 연산자로 ContentType 조합")
                Text("- ContentType.EmailAddress + ContentType.PhoneNumber")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "직접 구현해보세요:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Practice2_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice2_Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 이메일 또는 전화번호 모두 자동완성되는 필드를 만드세요

    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // TODO: 다중 ContentType 조합 필요
        OutlinedTextField(
            value = loginId,
            onValueChange = { loginId = it },
            label = { Text("이메일 또는 전화번호") },
            leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            // TODO: 여기에 두 ContentType을 조합하세요
            // .semantics { contentType = ContentType.EmailAddress + ContentType.PhoneNumber }
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.Password
                },
            singleLine = true
        )

        Button(
            onClick = { /* 로그인 처리 */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인")
        }
    }
}

@Composable
private fun Practice2_Answer() {
    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = loginId,
            onValueChange = { loginId = it },
            label = { Text("이메일 또는 전화번호") },
            leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    // 정답: 두 ContentType을 + 연산자로 조합!
                    contentType = ContentType.EmailAddress + ContentType.PhoneNumber
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.Password
                },
            singleLine = true
        )

        Text(
            text = "ContentType.EmailAddress + ContentType.PhoneNumber로 조합되었습니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 연습 문제 3: 회원가입 폼 + 자동 저장 (어려움)
 *
 * 이메일, 비밀번호, 전화번호를 입력받고, 회원가입 완료 시 자격 증명을 저장하세요.
 *
 * 참고: LocalAutofillManager는 Compose BOM 2025.04.01 이상에서 사용 가능합니다.
 */
@Composable
fun Practice3_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 회원가입 폼 + 자동 저장",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("이메일, 비밀번호, 전화번호를 입력받고, 회원가입 완료 시 자격 증명을 저장하세요.")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "목표: LocalAutofillManager.current와 commit() 사용법 익히기",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // BOM 버전 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Text(
                    text = "LocalAutofillManager는 Compose BOM 2025.04.01 이상 필요",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- LocalAutofillManager.current로 매니저 획득")
                Text("- 회원가입이므로 NewPassword ContentType 사용")
                Text("- 버튼 클릭 시 autofillManager?.commit() 호출")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "직접 구현해보세요:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Practice3_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice3_Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: LocalAutofillManager를 사용하여 자격 증명 저장 기능을 구현하세요
    // Compose 1.8+ 에서는:
    // val autofillManager = LocalAutofillManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isComplete by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.EmailAddress
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        // TODO: 비밀번호 필드에 ContentType.NewPassword 설정
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            // TODO: .semantics { contentType = ContentType.NewPassword }
            singleLine = true
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("전화번호") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.PhoneNumber
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        Button(
            onClick = {
                // TODO: autofillManager?.commit() 호출
                isComplete = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입")
        }

        if (isComplete) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Text("회원가입 완료!")
                }
            }
        }
    }
}

@Composable
private fun Practice3_Answer() {
    // 정답: LocalAutofillManager 사용 (Compose 1.8+ 필요)
    // val autofillManager = LocalAutofillManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isComplete by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.EmailAddress
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        // 정답: NewPassword ContentType 사용
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.NewPassword  // 정답!
                },
            singleLine = true
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("전화번호") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.PhoneNumber
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        Button(
            onClick = {
                // 정답: commit() 호출로 자격 증명 저장 요청
                // autofillManager?.commit()
                isComplete = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입")
        }

        if (isComplete) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Text("회원가입 완료!")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Compose 1.8+ 에서는 autofillManager?.commit()을 호출하여 자격 증명 저장을 요청합니다.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "전체 코드 (Compose 1.8+):",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
val autofillManager = LocalAutofillManager.current

TextField(
    modifier = Modifier.semantics {
        contentType = ContentType.NewPassword
    }
)

Button(onClick = {
    autofillManager?.commit()
}) {
    Text("회원가입")
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
