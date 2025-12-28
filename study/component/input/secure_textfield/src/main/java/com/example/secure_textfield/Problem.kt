package com.example.secure_textfield

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Problem: 기존 TextField로 비밀번호 필드를 구현할 때의 복잡성
 *
 * 기존 방식의 문제점:
 * 1. 여러 설정을 조합해야 함 (visualTransformation, keyboardOptions 등)
 * 2. 비밀번호 표시/숨김 토글을 직접 구현해야 함
 * 3. 보안 기능(복사/붙여넣기 차단)이 기본 제공되지 않음
 * 4. 코드가 길어지고 반복적인 보일러플레이트 발생
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
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
                    text = "기존 TextField로 비밀번호 필드 구현 시 문제점",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. PasswordVisualTransformation 수동 적용 필요
                        |2. KeyboardType.Password 별도 설정 필요
                        |3. 비밀번호 표시/숨김 토글 직접 구현
                        |4. 보안 기능(복사 차단) 기본 제공 안 됨
                        |5. 코드가 20줄 이상으로 길어짐
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: 기본 비밀번호 필드의 복잡성
        Problem1_BasicPasswordField()

        HorizontalDivider()

        // 문제 2: 토글 기능 추가 시 더 복잡해짐
        Problem2_PasswordWithToggle()

        HorizontalDivider()

        // 문제 3: 보안 기능 부재
        Problem3_SecurityConcerns()
    }
}

/**
 * 문제 1: 기본 비밀번호 필드 구현
 *
 * 비밀번호 필드 하나를 구현하는 데도 여러 설정이 필요합니다.
 */
@Composable
fun Problem1_BasicPasswordField() {
    // 상태 관리: value/onValueChange 패턴 사용
    var password by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 1: 기본 비밀번호 필드의 복잡성",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "필요한 설정들",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |- visualTransformation = PasswordVisualTransformation()
                        |- keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        |- singleLine = true
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 기존 방식: 여러 설정을 조합해야 함
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호 (기존 방식)") },
            placeholder = { Text("비밀번호를 입력하세요") },
            // 1. 비밀번호 마스킹
            visualTransformation = PasswordVisualTransformation(),
            // 2. 비밀번호 키보드
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            // 3. 단일 줄
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "입력된 비밀번호: ${password.length}자",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "문제점: 3가지 설정을 모두 기억해야 하고, 하나라도 빠지면 보안 취약점 발생",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

/**
 * 문제 2: 비밀번호 표시/숨김 토글 추가
 *
 * 토글 기능을 추가하면 코드가 더욱 복잡해집니다.
 * 상태 관리, 아이콘 전환, contentDescription 등을 모두 직접 처리해야 합니다.
 */
@Composable
fun Problem2_PasswordWithToggle() {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 2: 토글 기능 추가 시 복잡성 증가",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "추가로 필요한 코드",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |- passwordVisible 상태 변수 추가
                        |- visualTransformation 조건부 적용
                        |- trailingIcon에 IconButton 추가
                        |- 아이콘 조건부 전환
                        |- contentDescription 설정
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 기존 방식: 토글 기능 직접 구현 (약 20줄)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호 (토글 있음)") },
            placeholder = { Text("비밀번호를 입력하세요") },
            // 조건부 마스킹
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            // 토글 아이콘
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (passwordVisible) {
                            "비밀번호 숨기기"
                        } else {
                            "비밀번호 보기"
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "비밀번호 표시: ${if (passwordVisible) "ON" else "OFF"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${password.length}자 입력됨",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "문제점: 토글 기능 추가에 약 10줄 이상의 코드가 필요하고, 매번 동일한 패턴을 반복해야 함",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

/**
 * 문제 3: 보안 기능 부재
 *
 * 기존 TextField는 복사/붙여넣기, 드래그 등을 자동으로 차단하지 않습니다.
 * 이를 구현하려면 추가적인 처리가 필요합니다.
 */
@Composable
fun Problem3_SecurityConcerns() {
    var password by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 3: 보안 기능 부재",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "기존 TextField의 보안 취약점",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |- 복사/잘라내기 가능 (비밀번호 유출 위험)
                        |- 드래그로 텍스트 선택 가능
                        |- 컨텍스트 메뉴에서 공유 가능
                        |- 보안 키보드 연동 보장 안 됨
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호 (보안 취약)") },
            placeholder = { Text("텍스트를 선택하고 길게 눌러보세요") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            // 텍스트 선택 및 복사 가능!
            // 보안을 위해 이를 비활성화하려면 추가 처리 필요
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "위 필드에 비밀번호를 입력하고 텍스트를 길게 눌러보세요!",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "복사/잘라내기/공유 메뉴가 나타납니다. " +
                            "비밀번호가 클립보드에 복사될 수 있어 보안에 취약합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 요약 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 요약",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. 코드 복잡성: 비밀번호 필드 하나에 많은 설정 필요
                        |2. 보일러플레이트: 토글 기능을 매번 직접 구현
                        |3. 보안 취약점: 복사/붙여넣기 차단이 기본 제공 안 됨
                        |4. 실수 가능성: 설정을 하나라도 빠뜨리면 문제 발생
                        |
                        |해결책: Solution 탭에서 SecureTextField를 확인하세요!
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}
