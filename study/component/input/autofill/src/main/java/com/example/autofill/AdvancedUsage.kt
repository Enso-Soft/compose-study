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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * 고급 활용 화면
 *
 * Autofill의 고급 사용법과 조합 패턴을 학습합니다.
 * - 다중 ContentType 조합
 * - 자격 증명 저장 (commit) - Compose 1.8+ 필요
 * - 하이라이트 색상 커스터마이징 - Compose 1.8+ 필요
 *
 * 참고: LocalAutofillManager와 LocalAutofillHighlightColor는
 * Compose BOM 2025.04.01 이상에서 사용 가능합니다.
 */
@Composable
fun AdvancedUsageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 고급 활용 소개
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "고급 활용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기본 기능을 조합하여 더 강력한 Autofill 패턴을 만들어봅시다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 패턴 1: 다중 ContentType 조합
        PatternCard(
            title = "패턴 1: 다중 ContentType 조합",
            description = "여러 ContentType을 조합하여 유연한 입력 필드를 만듭니다.",
            useCase = "이메일 또는 전화번호로 로그인"
        ) {
            MultipleContentTypeDemo()
        }

        // 패턴 2: 자격 증명 저장
        PatternCard(
            title = "패턴 2: 자격 증명 저장 (commit)",
            description = "LocalAutofillManager를 사용하여 새 자격 증명을 저장합니다. (Compose 1.8+ 필요)",
            useCase = "회원가입 완료 시 비밀번호 저장 제안"
        ) {
            CredentialSaveDemo()
        }

        // 패턴 3: 하이라이트 색상 커스터마이징
        PatternCard(
            title = "패턴 3: 하이라이트 색상",
            description = "LocalAutofillHighlightColor로 강조 색상을 브랜드에 맞게 변경합니다. (Compose 1.8+ 필요)",
            useCase = "브랜드 색상 적용"
        ) {
            HighlightColorDemo()
        }

        // 베스트 프랙티스 vs 안티패턴
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "베스트 프랙티스",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                BestPracticeItem("로그인: Username + Password 사용")
                BestPracticeItem("회원가입: NewUsername + NewPassword 사용")
                BestPracticeItem("KeyboardType과 함께 설정 (이메일 -> KeyboardType.Email)")
                BestPracticeItem("폼 제출 시 commit() 호출")

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "안티패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                AntiPatternItem("비밀번호 필드에 Username ContentType 사용")
                AntiPatternItem("회원가입에서 commit() 누락")
                AntiPatternItem("로그인 필드에 New* ContentType 사용")
            }
        }

        // 안티패턴 상세 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "이렇게 하지 마세요",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 잘못된 예: 비밀번호 필드에 Username 타입
TextField(
    visualTransformation = PasswordVisualTransformation(),
    modifier = Modifier.semantics {
        contentType = ContentType.Username  // 비밀번호가 아님!
    }
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "대신 이렇게 하세요",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 올바른 예
TextField(
    visualTransformation = PasswordVisualTransformation(),
    modifier = Modifier.semantics {
        contentType = ContentType.Password  // 올바른 타입
    }
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun PatternCard(
    title: String,
    description: String,
    useCase: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "사용 사례: $useCase",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            demo()
        }
    }
}

@Composable
private fun BestPracticeItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun AntiPatternItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.Cancel,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(16.dp)
        )
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

/**
 * 다중 ContentType 조합 데모
 */
@Composable
private fun MultipleContentTypeDemo() {
    var loginId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "이메일 또는 전화번호로 로그인할 수 있는 필드:",
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = loginId,
            onValueChange = { loginId = it },
            label = { Text("이메일 또는 전화번호") },
            leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    // 두 ContentType을 + 연산자로 조합
                    contentType = ContentType.EmailAddress + ContentType.PhoneNumber
                },
            singleLine = true
        )

        Text(
            text = "코드: contentType = ContentType.EmailAddress + ContentType.PhoneNumber",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * 자격 증명 저장 데모
 *
 * 참고: LocalAutofillManager는 Compose BOM 2025.04.01 이상에서 사용 가능합니다.
 * 아래는 API 사용법을 보여주는 시뮬레이션입니다.
 */
@Composable
private fun CredentialSaveDemo() {
    // Compose 1.8+ 에서는 다음과 같이 사용합니다:
    // val autofillManager = LocalAutofillManager.current

    var newUsername by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var commitStatus by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // BOM 버전 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
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

        Text(
            text = "회원가입 폼 (NewUsername + NewPassword 사용):",
            style = MaterialTheme.typography.bodySmall
        )

        // 새 사용자 이름
        OutlinedTextField(
            value = newUsername,
            onValueChange = { newUsername = it },
            label = { Text("새 사용자 이름") },
            leadingIcon = { Icon(Icons.Default.PersonAdd, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.NewUsername
                },
            singleLine = true
        )

        // 새 비밀번호
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("새 비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.NewPassword
                },
            singleLine = true,
            supportingText = {
                Text("NewPassword 사용 시 '강력한 비밀번호 제안' 버튼이 표시됩니다")
            }
        )

        // 회원가입 버튼
        Button(
            onClick = {
                if (newUsername.isNotEmpty() && newPassword.isNotEmpty()) {
                    // Compose 1.8+ 에서는:
                    // autofillManager?.commit()
                    commitStatus = "commit() 호출됨! 자격 증명 저장 다이얼로그가 표시됩니다."
                } else {
                    commitStatus = "사용자 이름과 비밀번호를 입력하세요"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입 (commit 호출)")
        }

        // 상태 표시
        if (commitStatus.isNotEmpty()) {
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
                    Text(commitStatus, style = MaterialTheme.typography.bodySmall)
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
                    text = "사용법 (Compose 1.8+):",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
val autofillManager = LocalAutofillManager.current

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

/**
 * 하이라이트 색상 커스터마이징 데모
 *
 * 참고: LocalAutofillHighlightColor는 Compose BOM 2025.04.01 이상에서 사용 가능합니다.
 */
@Composable
private fun HighlightColorDemo() {
    var selectedColor by remember { mutableIntStateOf(0) }
    var username by remember { mutableStateOf("") }

    val colors = listOf(
        "기본 (노란색)" to Color(0x4dffeb3b),
        "빨간색" to Color.Red.copy(alpha = 0.3f),
        "파란색" to Color.Blue.copy(alpha = 0.3f),
        "초록색" to Color.Green.copy(alpha = 0.3f)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // BOM 버전 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Text(
                    text = "LocalAutofillHighlightColor는 Compose BOM 2025.04.01 이상 필요",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text(
            text = "하이라이트 색상 선택 (시뮬레이션):",
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            colors.forEachIndexed { index, (name, _) ->
                FilterChip(
                    selected = selectedColor == index,
                    onClick = { selectedColor = index },
                    label = { Text(name.substringBefore(" ")) }
                )
            }
        }

        // TextField 표시
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("사용자 이름") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.Username
                },
            singleLine = true
        )

        // 색상 미리보기
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colors[selectedColor].second
            )
        ) {
            Text(
                text = "현재 선택된 하이라이트 색상 미리보기",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "사용법 (Compose 1.8+):",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
CompositionLocalProvider(
    LocalAutofillHighlightColor provides ${colors[selectedColor].first}
) {
    TextField(...)
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
