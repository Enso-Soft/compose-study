package com.example.focus_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * # Problem: Focus Management 없이 구현된 로그인 폼
 *
 * ## 발생하는 문제점들:
 *
 * 1. **자동 포커스 없음**
 *    - 화면 진입 시 이메일 필드에 자동으로 포커스가 가지 않음
 *    - 사용자가 직접 터치해야 입력 시작 가능
 *
 * 2. **IME Action 미연결**
 *    - 키보드의 "다음" 버튼을 눌러도 다음 필드로 이동하지 않음
 *    - "완료" 버튼을 눌러도 로그인이 실행되지 않음
 *    - 매번 터치로 필드 이동해야 함
 *
 * 3. **포커스 스타일링 부재**
 *    - 현재 어떤 필드가 활성화되어 있는지 시각적 피드백 부족
 *    - 기본 스타일만 적용됨
 *
 * 4. **키보드 제어 불가**
 *    - 로그인 버튼 클릭 후에도 키보드가 숨겨지지 않음
 *    - 외부 영역 터치로 키보드 숨김 처리 누락
 *
 * 5. **포커스 순서 미지정**
 *    - Tab 키 네비게이션 시 순서가 명시적이지 않음
 *    - 접근성 문제 발생 가능
 */
@Composable
fun ProblemScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf("") }

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
                    text = "Focus Management 없이 구현된 로그인 폼",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |발생하는 문제점들:
                        |
                        |1. 화면 진입 시 자동 포커스 없음
                        |   - 직접 터치해야 입력 가능
                        |
                        |2. 키보드 "다음" 버튼 미동작
                        |   - 다음 필드로 이동하지 않음
                        |
                        |3. 키보드 "완료" 버튼 미동작
                        |   - 로그인 실행되지 않음
                        |
                        |4. 포커스 상태 스타일링 부재
                        |   - 어떤 필드가 활성화인지 구분 어려움
                        |
                        |5. 로그인 후 키보드 미숨김
                        |   - 수동으로 키보드 닫아야 함
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 로그인 폼 (문제 있는 버전)
        Text(
            text = "로그인",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 이메일 필드 - 문제: 자동 포커스 없음, IME Action 미설정
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            // 문제: keyboardOptions와 keyboardActions가 설정되지 않음
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
                // imeAction = ImeAction.Next 가 없음!
            )
            // 문제: keyboardActions가 없음!
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 비밀번호 필드 - 문제: IME Action 미설정
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보기"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
                // imeAction = ImeAction.Done 가 없음!
            )
            // 문제: keyboardActions가 없음!
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 로그인 버튼 - 문제: 클릭 시 키보드 숨기지 않음
        Button(
            onClick = {
                // 문제: 키보드를 숨기지 않음
                // 문제: 포커스를 해제하지 않음
                if (email.isNotBlank() && password.isNotBlank()) {
                    loginMessage = "로그인 성공! (이메일: $email)"
                } else {
                    loginMessage = "이메일과 비밀번호를 입력해주세요."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotBlank() && password.isNotBlank()
        ) {
            Text("로그인")
        }

        // 로그인 결과 표시
        if (loginMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (loginMessage.contains("성공"))
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = loginMessage,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 테스트 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 테스트해보세요!",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. 화면을 새로 열어도 커서가 자동으로 가지 않음
                        |2. 이메일 입력 후 키보드의 "다음" 버튼 확인
                        |   → 아무 일도 안 일어남!
                        |3. 비밀번호 입력 후 키보드의 "완료" 버튼 확인
                        |   → 로그인이 실행되지 않음!
                        |4. 로그인 버튼 클릭 후 키보드 상태 확인
                        |   → 키보드가 계속 열려있음!
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 문제점 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드에서 누락된 것들:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |// 1. FocusRequester 없음
                        |val focusRequester = remember { FocusRequester() }
                        |
                        |// 2. LaunchedEffect로 자동 포커스 없음
                        |LaunchedEffect(Unit) {
                        |    focusRequester.requestFocus()
                        |}
                        |
                        |// 3. keyboardActions 없음
                        |keyboardActions = KeyboardActions(
                        |    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        |)
                        |
                        |// 4. FocusManager로 키보드 제어 없음
                        |focusManager.clearFocus()
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
