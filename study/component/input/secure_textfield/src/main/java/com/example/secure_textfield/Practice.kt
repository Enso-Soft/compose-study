package com.example.secure_textfield

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// =====================================================================
// Practice.kt - SecureTextField 연습 문제
// =====================================================================
//
// 참고: SecureTextField는 Material3 1.4.0+ (Compose BOM 2025.04.01+)에서 사용 가능합니다.
// 현재 프로젝트는 BOM 2024.09.00을 사용하므로,
// 연습 문제는 현재 API로 구현하되, 새 API 코드를 함께 학습합니다.
// =====================================================================

/**
 * 연습 문제 1: 기본 비밀번호 입력 필드 구현 (쉬움)
 *
 * 목표: PasswordVisualTransformation을 사용하여 기본 비밀번호 필드를 구현하세요.
 *
 * 요구사항:
 * 1. remember { mutableStateOf("") }로 상태 생성
 * 2. OutlinedTextField에 PasswordVisualTransformation 적용
 * 3. label을 "비밀번호"로 설정
 *
 * SecureTextField API (BOM 2025.04.01+):
 * ```
 * val passwordState = rememberTextFieldState()
 * SecureTextField(
 *     state = passwordState,
 *     label = { Text("비밀번호") }
 * )
 * ```
 */
@Composable
fun Practice1_BasicSecureTextField() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 비밀번호 입력 필드 구현",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: PasswordVisualTransformation을 사용하여
                        |기본 비밀번호 필드를 구현하세요.
                        |
                        |요구사항:
                        |1. remember { mutableStateOf("") }로 상태 생성
                        |2. OutlinedTextField에 PasswordVisualTransformation 적용
                        |3. label을 "비밀번호"로 설정
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // SecureTextField API 참조
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SecureTextField API (BOM 2025.04.01+)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |val passwordState = rememberTextFieldState()
                        |
                        |SecureTextField(
                        |    state = passwordState,
                        |    label = { Text("비밀번호") },
                        |    modifier = Modifier.fillMaxWidth()
                        |)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 API 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |- var password by remember { mutableStateOf("") }
                        |- visualTransformation = PasswordVisualTransformation()
                        |- keyboardOptions에 KeyboardType.Password 설정
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 연습 공간
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "여기에 구현하세요",
                    style = MaterialTheme.typography.labelMedium
                )

                Practice1_Exercise()
            }
        }

        // 정답 보기 (접을 수 있는 카드)
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedCard(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (showAnswer) "정답 숨기기" else "정답 보기",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
                            |// 현재 API (BOM 2024.xx)
                            |var password by remember { mutableStateOf("") }
                            |
                            |OutlinedTextField(
                            |    value = password,
                            |    onValueChange = { password = it },
                            |    label = { Text("비밀번호") },
                            |    visualTransformation = PasswordVisualTransformation(),
                            |    keyboardOptions = KeyboardOptions(
                            |        keyboardType = KeyboardType.Password
                            |    ),
                            |    modifier = Modifier.fillMaxWidth()
                            |)
                        """.trimMargin(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 기본 비밀번호 입력 필드를 구현하세요
    // 요구사항:
    // 1. password 상태 변수를 생성하세요 (remember { mutableStateOf("") })
    // 2. OutlinedTextField에 PasswordVisualTransformation 적용
    // 3. keyboardOptions에 KeyboardType.Password 설정
    // 4. label을 "비밀번호"로 설정

    // TODO: password 상태 변수를 생성하세요
    // var password by remember { mutableStateOf("") }

    // TODO: OutlinedTextField를 구현하세요
    // OutlinedTextField(
    //     value = password,
    //     onValueChange = { password = it },
    //     label = { Text("비밀번호") },
    //     visualTransformation = ???,
    //     keyboardOptions = ???,
    //     ...
    // )
}

/**
 * 연습 문제 2: 비밀번호 표시/숨김 토글 (중간)
 *
 * 목표: trailingIcon에 토글 아이콘을 추가하여 비밀번호 표시/숨김 기능을 구현하세요.
 *
 * 요구사항:
 * 1. passwordVisible 상태 변수 추가 (rememberSaveable 사용)
 * 2. visualTransformation을 상태에 따라 전환
 * 3. trailingIcon에 IconButton 추가
 * 4. 숨김: Visibility 아이콘, 표시: VisibilityOff 아이콘
 * 5. contentDescription 설정
 *
 * SecureTextField API (BOM 2025.04.01+):
 * ```
 * SecureTextField(
 *     state = passwordState,
 *     textObfuscationMode = if (passwordHidden)
 *         TextObfuscationMode.RevealLastTyped
 *     else
 *         TextObfuscationMode.Visible,
 *     trailingIcon = { /* 토글 아이콘 */ }
 * )
 * ```
 */
@Composable
fun Practice2_PasswordToggle() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 비밀번호 표시/숨김 토글",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: trailingIcon에 토글 아이콘을 추가하여
                        |비밀번호 표시/숨김 기능을 구현하세요.
                        |
                        |요구사항:
                        |1. passwordVisible 상태 변수 추가
                        |2. visualTransformation 조건부 전환
                        |3. trailingIcon에 IconButton 추가
                        |4. 적절한 아이콘과 contentDescription 설정
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // SecureTextField API 참조
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SecureTextField API (BOM 2025.04.01+)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |var passwordHidden by rememberSaveable { mutableStateOf(true) }
                        |
                        |SecureTextField(
                        |    state = passwordState,
                        |    textObfuscationMode = if (passwordHidden)
                        |        TextObfuscationMode.RevealLastTyped
                        |    else
                        |        TextObfuscationMode.Visible,
                        |    trailingIcon = {
                        |        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        |            Icon(
                        |                imageVector = if (passwordHidden)
                        |                    Icons.Default.Visibility
                        |                else
                        |                    Icons.Default.VisibilityOff,
                        |                contentDescription = ...
                        |            )
                        |        }
                        |    }
                        |)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 API 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |- rememberSaveable { mutableStateOf(false) }로 토글 상태 관리
                        |- if (passwordVisible) VisualTransformation.None
                        |  else PasswordVisualTransformation()
                        |- Icons.Default.Visibility / VisibilityOff 사용
                        |- IconButton(onClick = { passwordVisible = !passwordVisible })
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 연습 공간
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "여기에 구현하세요",
                    style = MaterialTheme.typography.labelMedium
                )

                Practice2_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedCard(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (showAnswer) "정답 숨기기" else "정답 보기",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
                            |var password by remember { mutableStateOf("") }
                            |var passwordVisible by rememberSaveable { mutableStateOf(false) }
                            |
                            |OutlinedTextField(
                            |    value = password,
                            |    onValueChange = { password = it },
                            |    label = { Text("비밀번호") },
                            |    visualTransformation = if (passwordVisible)
                            |        VisualTransformation.None
                            |    else
                            |        PasswordVisualTransformation(),
                            |    trailingIcon = {
                            |        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            |            Icon(
                            |                imageVector = if (passwordVisible)
                            |                    Icons.Default.VisibilityOff
                            |                else
                            |                    Icons.Default.Visibility,
                            |                contentDescription = if (passwordVisible)
                            |                    "비밀번호 숨기기"
                            |                else
                            |                    "비밀번호 보기"
                            |            )
                            |        }
                            |    },
                            |    keyboardOptions = KeyboardOptions(
                            |        keyboardType = KeyboardType.Password
                            |    ),
                            |    modifier = Modifier.fillMaxWidth()
                            |)
                        """.trimMargin(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 비밀번호 표시/숨김 토글 기능을 구현하세요
    // 요구사항:
    // 1. password 상태 변수 생성 (remember)
    // 2. passwordVisible 상태 변수 생성 (rememberSaveable)
    // 3. visualTransformation을 passwordVisible에 따라 전환
    //    - true: VisualTransformation.None
    //    - false: PasswordVisualTransformation()
    // 4. trailingIcon에 IconButton + 토글 아이콘 추가
    //    - 숨김 상태: Icons.Default.Visibility
    //    - 표시 상태: Icons.Default.VisibilityOff
    // 5. contentDescription 설정

    // TODO: 상태 변수들을 생성하세요
    // var password by remember { mutableStateOf("") }
    // var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // TODO: OutlinedTextField를 구현하세요
    // 힌트: visualTransformation = if (passwordVisible) ??? else ???
    // 힌트: trailingIcon = { IconButton(onClick = { ??? }) { Icon(...) } }
}

/**
 * 연습 문제 3: 완전한 로그인 폼 구현 (어려움)
 *
 * 목표: 이메일 + 비밀번호 + 유효성 검사 + 버튼 활성화를 구현하세요.
 *
 * 요구사항:
 * 1. 이메일 입력 필드 (OutlinedTextField, 일반 텍스트)
 * 2. 비밀번호 입력 필드 (OutlinedTextField + 토글)
 * 3. 유효성 검사:
 *    - 이메일: '@' 포함 필수
 *    - 비밀번호: 8자 이상
 * 4. 두 조건 모두 만족 시에만 '로그인' 버튼 활성화
 *
 * SecureTextField API (BOM 2025.04.01+):
 * ```
 * val emailState = rememberTextFieldState()
 * val passwordState = rememberTextFieldState()
 *
 * val isFormValid by remember {
 *     derivedStateOf {
 *         emailState.text.contains("@") &&
 *         passwordState.text.length >= 8
 *     }
 * }
 *
 * TextField(state = emailState, ...)
 * SecureTextField(state = passwordState, ...)
 * Button(enabled = isFormValid) { ... }
 * ```
 */
@Composable
fun Practice3_LoginForm() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 완전한 로그인 폼 구현",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: 이메일 + 비밀번호 + 유효성 검사 +
                        |버튼 활성화를 구현하세요.
                        |
                        |요구사항:
                        |1. 이메일 입력 필드
                        |2. 비밀번호 입력 필드 + 토글
                        |3. 유효성 검사:
                        |   - 이메일: '@' 포함 필수
                        |   - 비밀번호: 8자 이상
                        |4. 조건 만족 시에만 '로그인' 버튼 활성화
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // SecureTextField API 참조
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SecureTextField API (BOM 2025.04.01+)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |val emailState = rememberTextFieldState()
                        |val passwordState = rememberTextFieldState()
                        |
                        |val isFormValid by remember {
                        |    derivedStateOf {
                        |        emailState.text.contains("@") &&
                        |        passwordState.text.length >= 8
                        |    }
                        |}
                        |
                        |TextField(state = emailState, ...)
                        |SecureTextField(state = passwordState, ...)
                        |Button(enabled = isFormValid) { Text("로그인") }
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 API 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |- var email by remember { mutableStateOf("") }
                        |- var password by remember { mutableStateOf("") }
                        |- derivedStateOf로 유효성 검사 결과 계산
                        |- email.contains("@")
                        |- password.length >= 8
                        |- Button(enabled = isFormValid)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 연습 공간
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "여기에 구현하세요",
                    style = MaterialTheme.typography.labelMedium
                )

                Practice3_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedCard(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (showAnswer) "정답 숨기기" else "정답 보기",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
                            |var email by remember { mutableStateOf("") }
                            |var password by remember { mutableStateOf("") }
                            |var passwordVisible by rememberSaveable { mutableStateOf(false) }
                            |
                            |val isEmailValid by remember {
                            |    derivedStateOf { email.contains("@") }
                            |}
                            |val isPasswordValid by remember {
                            |    derivedStateOf { password.length >= 8 }
                            |}
                            |val isFormValid by remember {
                            |    derivedStateOf { isEmailValid && isPasswordValid }
                            |}
                            |
                            |Column(...) {
                            |    OutlinedTextField(
                            |        value = email,
                            |        onValueChange = { email = it },
                            |        label = { Text("이메일") },
                            |        leadingIcon = { Icon(Icons.Default.Email, null) },
                            |        keyboardOptions = KeyboardOptions(
                            |            keyboardType = KeyboardType.Email
                            |        )
                            |    )
                            |
                            |    OutlinedTextField(
                            |        value = password,
                            |        onValueChange = { password = it },
                            |        label = { Text("비밀번호 (8자 이상)") },
                            |        visualTransformation = if (passwordVisible)
                            |            VisualTransformation.None
                            |        else
                            |            PasswordVisualTransformation(),
                            |        leadingIcon = { Icon(Icons.Default.Lock, null) },
                            |        trailingIcon = { /* 토글 아이콘 */ }
                            |    )
                            |
                            |    Button(
                            |        onClick = { /* 로그인 처리 */ },
                            |        enabled = isFormValid
                            |    ) {
                            |        Text("로그인")
                            |    }
                            |}
                        """.trimMargin(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 완전한 로그인 폼을 구현하세요
    // 요구사항:
    // 1. email, password 상태 변수 생성
    // 2. passwordVisible 상태 변수 생성 (rememberSaveable)
    // 3. 유효성 검사 (derivedStateOf 사용):
    //    - isEmailValid: email.contains("@")
    //    - isPasswordValid: password.length >= 8
    //    - isFormValid: isEmailValid && isPasswordValid
    // 4. 이메일 필드: OutlinedTextField + Email 아이콘 + 에러 표시
    // 5. 비밀번호 필드: OutlinedTextField + 토글 + Lock 아이콘 + 에러 표시
    // 6. 로그인 버튼: enabled = isFormValid

    // TODO: 상태 변수들을 생성하세요
    // var email by remember { mutableStateOf("") }
    // var password by remember { mutableStateOf("") }
    // var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // TODO: 유효성 검사를 정의하세요
    // val isEmailValid by remember { derivedStateOf { ??? } }
    // val isPasswordValid by remember { derivedStateOf { ??? } }
    // val isFormValid by remember { derivedStateOf { ??? } }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO: 이메일 필드를 구현하세요
        // OutlinedTextField(
        //     leadingIcon = { Icon(Icons.Default.Email, null) },
        //     isError = email.isNotEmpty() && !isEmailValid,
        //     ...
        // )

        // TODO: 비밀번호 필드를 구현하세요 (토글 포함)

        // TODO: 로그인 버튼을 구현하세요
        // Button(enabled = isFormValid) { ... }

        // 상태 표시 카드 (구현 후 활성화됨)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "폼 상태",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "이메일: 유효하지 않음",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "비밀번호: 유효하지 않음",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "로그인 버튼: 비활성화",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
