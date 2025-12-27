package com.example.focus_management

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * # 연습 문제 1: 검색창 자동 포커스
 *
 * ## 목표
 * - FocusRequester 생성 및 연결
 * - LaunchedEffect로 초기 포커스 요청
 *
 * ## TODO
 * 아래 주석의 지시에 따라 코드를 완성하세요.
 */
@Composable
fun Practice1_SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }

    // TODO 1: FocusRequester 생성
    // 힌트: remember { FocusRequester() }
    // val searchFocusRequester = ???

    // 정답:
    val searchFocusRequester = remember { FocusRequester() }

    // TODO 2: 화면 진입 시 자동 포커스
    // 힌트: LaunchedEffect(Unit) { focusRequester.requestFocus() }

    // 정답:
    LaunchedEffect(Unit) {
        searchFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 검색창 자동 포커스",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: 화면 진입 시 검색창에 자동으로 포커스
                        |
                        |TODO:
                        |1. FocusRequester 생성
                        |2. LaunchedEffect로 자동 포커스 요청
                        |3. TextField에 focusRequester modifier 연결
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 검색 TextField
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("검색어를 입력하세요") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "검색어 지우기")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                // TODO 3: focusRequester 연결
                // 힌트: .focusRequester(searchFocusRequester)
                .focusRequester(searchFocusRequester),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // 검색 실행
                    searchResults = listOf(
                        "$searchQuery 검색 결과 1",
                        "$searchQuery 검색 결과 2",
                        "$searchQuery 검색 결과 3"
                    )
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 검색 결과
        if (searchResults.isNotEmpty()) {
            Text(
                text = "검색 결과:",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            searchResults.forEach { result ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |val focusRequester = remember { FocusRequester() }
                        |
                        |LaunchedEffect(Unit) {
                        |    focusRequester.requestFocus()
                        |}
                        |
                        |TextField(
                        |    modifier = Modifier.focusRequester(focusRequester)
                        |)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * # 연습 문제 2: 회원가입 폼 네비게이션
 *
 * ## 목표
 * - ImeAction 설정 (Next, Done)
 * - KeyboardActions 콜백 처리
 * - FocusManager.moveFocus() 사용
 *
 * ## TODO
 * 아래 주석의 지시에 따라 코드를 완성하세요.
 */
@Composable
fun Practice2_SignUpForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var signUpMessage by remember { mutableStateOf("") }

    // TODO 1: FocusManager 가져오기
    // 힌트: LocalFocusManager.current
    // val focusManager = ???

    // 정답:
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // 회원가입 처리
    val performSignUp: () -> Unit = {
        keyboardController?.hide()
        focusManager.clearFocus()
        if (name.isNotBlank() && email.isNotBlank() &&
            password.isNotBlank() && password == confirmPassword) {
            signUpMessage = "회원가입 성공! 환영합니다, $name 님"
        } else if (password != confirmPassword) {
            signUpMessage = "비밀번호가 일치하지 않습니다."
        } else {
            signUpMessage = "모든 필드를 입력해주세요."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 회원가입 폼 네비게이션",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: 키보드 "다음"/"완료" 버튼으로 폼 네비게이션
                        |
                        |TODO:
                        |1. FocusManager 가져오기
                        |2. ImeAction.Next 설정 (처음 3개 필드)
                        |3. ImeAction.Done 설정 (마지막 필드)
                        |4. KeyboardActions으로 포커스 이동
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 이름 필드
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            // TODO 2: ImeAction.Next 설정
            // 힌트: keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            // TODO 3: onNext에서 다음 필드로 이동
            // 힌트: keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 이메일 필드
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 비밀번호 필드
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 비밀번호 확인 필드
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("비밀번호 확인") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            // TODO 4: ImeAction.Done 설정
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            // TODO 5: onDone에서 회원가입 실행
            keyboardActions = KeyboardActions(
                onDone = { performSignUp() }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = performSignUp,
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && email.isNotBlank() &&
                    password.isNotBlank() && confirmPassword.isNotBlank()
        ) {
            Text("회원가입")
        }

        // 결과 메시지
        if (signUpMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (signUpMessage.contains("성공"))
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = signUpMessage,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |val focusManager = LocalFocusManager.current
                        |
                        |TextField(
                        |    keyboardOptions = KeyboardOptions(
                        |        imeAction = ImeAction.Next
                        |    ),
                        |    keyboardActions = KeyboardActions(
                        |        onNext = {
                        |            focusManager.moveFocus(FocusDirection.Down)
                        |        }
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

/**
 * # 연습 문제 3: 설정 화면 포커스 관리
 *
 * ## 목표
 * - FocusRequester.createRefs() 사용
 * - focusProperties로 커스텀 순서 지정
 * - onFocusChanged로 스타일 변경
 * - canFocus = false로 포커스 불가 설정
 *
 * ## TODO
 * 아래 주석의 지시에 따라 코드를 완성하세요.
 */
@Composable
fun Practice3_SettingsScreen() {
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    // 포커스 상태 추적
    var isNameFocused by remember { mutableStateOf(false) }
    var isEmailFocused by remember { mutableStateOf(false) }

    // TODO 1: 여러 FocusRequester 한번에 생성
    // 힌트: val (first, second) = remember { FocusRequester.createRefs() }
    // val (nameFocusRequester, emailFocusRequester) = ???

    // 정답:
    val (nameFocusRequester, emailFocusRequester) = remember { FocusRequester.createRefs() }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 설정 화면 포커스 관리",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |목표: 커스텀 포커스 순서와 스타일링
                        |
                        |TODO:
                        |1. FocusRequester.createRefs()로 여러 FocusRequester 생성
                        |2. focusProperties로 next/previous 지정
                        |3. onFocusChanged로 포커스 상태 추적
                        |4. 포커스 상태에 따른 테두리 색상 변경
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 포커스 상태 표시
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
                Text(
                    text = "이름: ${if (isNameFocused) "포커스됨" else "포커스 없음"}",
                    color = if (isNameFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "이메일: ${if (isEmailFocused) "포커스됨" else "포커스 없음"}",
                    color = if (isEmailFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "계정 설정",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 이름 필드
        val nameBorderColor by animateColorAsState(
            targetValue = if (isNameFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
            label = "nameBorderColor"
        )

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("이름") },
            modifier = Modifier
                .fillMaxWidth()
                // TODO 2: focusRequester 연결
                .focusRequester(nameFocusRequester)
                // TODO 3: focusProperties로 다음 순서 지정
                // 힌트: .focusProperties { next = emailFocusRequester }
                .focusProperties { next = emailFocusRequester }
                // TODO 4: onFocusChanged로 포커스 상태 추적
                // 힌트: .onFocusChanged { isNameFocused = it.isFocused }
                .onFocusChanged { isNameFocused = it.isFocused }
                // TODO 5: 포커스 상태에 따른 테두리
                .border(2.dp, nameBorderColor, RoundedCornerShape(4.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 이메일 필드
        val emailBorderColor by animateColorAsState(
            targetValue = if (isEmailFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
            label = "emailBorderColor"
        )

        OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            label = { Text("이메일") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocusRequester)
                .focusProperties { previous = nameFocusRequester }
                .onFocusChanged { isEmailFocused = it.isFocused }
                .border(2.dp, emailBorderColor, RoundedCornerShape(4.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "앱 설정",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 알림 스위치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("알림 받기")
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
                // 스위치는 기본적으로 focusable
            )
        }

        // 다크모드 스위치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("다크 모드 (포커스 불가)")
            Switch(
                checked = darkModeEnabled,
                onCheckedChange = { darkModeEnabled = it },
                // TODO 6: 이 스위치는 포커스 불가로 설정
                // 힌트: modifier = Modifier.focusProperties { canFocus = false }
                modifier = Modifier.focusProperties { canFocus = false }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 포커스 제어 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { nameFocusRequester.requestFocus() },
                modifier = Modifier.weight(1f)
            ) {
                Text("이름으로 포커스")
            }
            OutlinedButton(
                onClick = { emailFocusRequester.requestFocus() },
                modifier = Modifier.weight(1f)
            ) {
                Text("이메일로 포커스")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |// 여러 FocusRequester 생성
                        |val (first, second) = remember {
                        |    FocusRequester.createRefs()
                        |}
                        |
                        |// focusProperties로 순서 지정
                        |Modifier
                        |    .focusRequester(first)
                        |    .focusProperties { next = second }
                        |    .onFocusChanged { isFocused = it.isFocused }
                        |    .border(2.dp, if (isFocused) Primary else Transparent)
                        |
                        |// 포커스 불가 설정
                        |Modifier.focusProperties { canFocus = false }
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
