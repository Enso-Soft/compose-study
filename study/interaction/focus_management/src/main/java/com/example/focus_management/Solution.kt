package com.example.focus_management

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * # Solution: 완벽한 Focus Management가 적용된 로그인 폼
 *
 * ## 적용된 기술들:
 *
 * 1. **FocusRequester**
 *    - 프로그래밍 방식으로 포커스 요청
 *    - LaunchedEffect와 함께 화면 진입 시 자동 포커스
 *
 * 2. **FocusManager**
 *    - moveFocus(): 방향별 포커스 이동
 *    - clearFocus(): 포커스 해제 및 키보드 숨김
 *
 * 3. **IME Actions (KeyboardActions)**
 *    - onNext: 다음 필드로 이동
 *    - onDone: 폼 제출
 *
 * 4. **onFocusChanged**
 *    - 포커스 상태에 따른 동적 스타일링
 *
 * 5. **focusProperties**
 *    - 명시적 포커스 순서 지정
 *
 * 6. **SoftwareKeyboardController**
 *    - 키보드 표시/숨김 제어
 */
@Composable
fun SolutionScreen() {
    // 상태 변수들
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf("") }

    // 포커스 상태 추적
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    // FocusRequester들 생성
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    // FocusManager와 KeyboardController
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // 화면 진입 시 이메일 필드에 자동 포커스
    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    // 로그인 처리 함수
    val performLogin: () -> Unit = {
        keyboardController?.hide()
        focusManager.clearFocus()
        if (email.isNotBlank() && password.isNotBlank()) {
            loginMessage = "로그인 성공! (이메일: $email, 자동 로그인: $rememberMe)"
        } else {
            loginMessage = "이메일과 비밀번호를 입력해주세요."
        }
    }

    // 외부 터치 시 포커스 해제를 위한 Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 솔루션 설명 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Focus Management 완벽 적용!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
                            |적용된 기술들:
                            |
                            |1. FocusRequester + LaunchedEffect
                            |   → 화면 진입 시 이메일 필드 자동 포커스
                            |
                            |2. KeyboardOptions + KeyboardActions
                            |   → "다음" 버튼으로 필드 이동
                            |   → "완료" 버튼으로 로그인 실행
                            |
                            |3. onFocusChanged
                            |   → 포커스 상태에 따른 테두리 강조
                            |
                            |4. focusProperties
                            |   → 명시적 포커스 순서 지정
                            |
                            |5. FocusManager.clearFocus()
                            |   → 로그인 시 키보드 숨김
                            |
                            |6. pointerInput + detectTapGestures
                            |   → 외부 터치 시 포커스 해제
                        """.trimMargin(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 현재 포커스 상태 표시
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FocusStateIndicator(
                        label = "이메일",
                        isFocused = isEmailFocused
                    )
                    FocusStateIndicator(
                        label = "비밀번호",
                        isFocused = isPasswordFocused
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 로그인 폼 (해결된 버전)
            Text(
                text = "로그인",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 이메일 필드 - 완벽한 Focus Management 적용
            val emailBorderColor by animateColorAsState(
                targetValue = if (isEmailFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                label = "emailBorderColor"
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    // 1. FocusRequester 연결
                    .focusRequester(emailFocusRequester)
                    // 2. 포커스 순서 지정
                    .focusProperties {
                        next = passwordFocusRequester
                    }
                    // 3. 포커스 상태 추적
                    .onFocusChanged { focusState ->
                        isEmailFocused = focusState.isFocused
                    }
                    // 4. 포커스 상태에 따른 스타일링
                    .border(
                        width = 2.dp,
                        color = emailBorderColor,
                        shape = RoundedCornerShape(4.dp)
                    ),
                singleLine = true,
                // 5. IME Action 설정
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next  // "다음" 버튼
                ),
                // 6. IME Action 핸들러
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 비밀번호 필드
            val passwordBorderColor by animateColorAsState(
                targetValue = if (isPasswordFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                label = "passwordBorderColor"
            )

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
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester)
                    .focusProperties {
                        previous = emailFocusRequester
                    }
                    .onFocusChanged { focusState ->
                        isPasswordFocused = focusState.isFocused
                    }
                    .border(
                        width = 2.dp,
                        color = passwordBorderColor,
                        shape = RoundedCornerShape(4.dp)
                    ),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done  // "완료" 버튼
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        performLogin()
                    }
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 자동 로그인 체크박스
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Text("자동 로그인")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 로그인 버튼
            Button(
                onClick = performLogin,
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

            // 제어 버튼들
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "포커스 제어 버튼",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { emailFocusRequester.requestFocus() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("이메일 포커스", style = MaterialTheme.typography.bodySmall)
                        }
                        OutlinedButton(
                            onClick = { passwordFocusRequester.requestFocus() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("비밀번호 포커스", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { focusManager.clearFocus() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("포커스 해제", style = MaterialTheme.typography.bodySmall)
                        }
                        OutlinedButton(
                            onClick = { keyboardController?.hide() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("키보드 숨기기", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 핵심 코드 표시
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "핵심 코드:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
                            |// 1. FocusRequester 생성
                            |val emailFocusRequester = remember { FocusRequester() }
                            |val focusManager = LocalFocusManager.current
                            |
                            |// 2. 화면 진입 시 자동 포커스
                            |LaunchedEffect(Unit) {
                            |    emailFocusRequester.requestFocus()
                            |}
                            |
                            |// 3. TextField에 적용
                            |OutlinedTextField(
                            |    modifier = Modifier
                            |        .focusRequester(emailFocusRequester)
                            |        .focusProperties { next = passwordFocusRequester }
                            |        .onFocusChanged { isFocused = it.isFocused },
                            |    keyboardOptions = KeyboardOptions(
                            |        imeAction = ImeAction.Next
                            |    ),
                            |    keyboardActions = KeyboardActions(
                            |        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            |    )
                            |)
                        """.trimMargin(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun FocusStateIndicator(
    label: String,
    isFocused: Boolean
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isFocused)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant,
        label = "indicatorBackground"
    )

    val textColor by animateColorAsState(
        targetValue = if (isFocused)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = "indicatorText"
    )

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isFocused) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
        }
    }
}
