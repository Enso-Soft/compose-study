package com.example.secure_textfield

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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

/**
 * Solution: SecureTextField 개념 학습
 *
 * SecureTextField는 Material3 1.4.0+ / Compose BOM 2025.04.01+에서 사용 가능합니다.
 * 현재 프로젝트 BOM (2024.09.00)에서는 기존 API로 동일한 패턴을 구현합니다.
 *
 * SecureTextField의 장점:
 * 1. 간결한 API: 핵심 기능이 몇 줄로 구현됨
 * 2. 보안 기능 내장: 복사/잘라내기 자동 차단
 * 3. TextFieldState 기반: 안정적인 상태 관리
 * 4. TextObfuscationMode: 표시/숨김 전환이 간단
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SecureTextField - 새로운 비밀번호 입력 API",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |버전 요구사항:
                        |- Material3 1.4.0+ (alpha14 이상)
                        |- Compose Foundation 1.8.0+
                        |- Compose BOM 2025.04.01+
                        |
                        |현재 프로젝트: BOM 2024.09.00
                        |→ 기존 API로 동일한 패턴을 구현합니다.
                        |
                        |새 API의 장점:
                        |1. 복사/잘라내기 자동 차단
                        |2. TextObfuscationMode로 간편한 표시/숨김
                        |3. TextFieldState 기반 안정적 상태 관리
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // 해결책 1: 새 API 코드 예시
        Solution1_NewApiExample()

        HorizontalDivider()

        // 해결책 2: 현재 버전에서 구현 (기본)
        Solution2_CurrentVersionBasic()

        HorizontalDivider()

        // 해결책 3: 현재 버전에서 구현 (토글 포함)
        Solution3_CurrentVersionWithToggle()

        HorizontalDivider()

        // 해결책 4: Outlined 변형
        Solution4_OutlinedVariant()

        HorizontalDivider()

        // 해결책 5: 장점 요약
        Solution5_BenefitsSummary()
    }
}

/**
 * 해결책 1: 새로운 SecureTextField API 코드 예시
 *
 * Material3 1.4.0+ / Compose BOM 2025.04.01+ 환경에서 사용 가능합니다.
 */
@Composable
fun Solution1_NewApiExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 1: SecureTextField API (BOM 2025.04.01+)",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "새로운 API 사용 예시 (코드 미리보기)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |// 기본 사용법 - 단 2줄!
                        |val passwordState = rememberTextFieldState()
                        |SecureTextField(state = passwordState)
                        |
                        |// 비밀번호 토글 추가
                        |var passwordHidden by rememberSaveable { mutableStateOf(true) }
                        |
                        |SecureTextField(
                        |    state = rememberTextFieldState(),
                        |    label = { Text("비밀번호") },
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
                        |                contentDescription = "토글"
                        |            )
                        |        }
                        |    }
                        |)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "TextObfuscationMode 옵션",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
                        |RevealLastTyped: 마지막 글자만 잠시 보여줌 (기본값)
                        |Hidden: 모든 글자를 즉시 숨김
                        |Visible: 모든 글자를 표시
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

/**
 * 해결책 2: 현재 버전에서 기본 비밀번호 필드 구현
 */
@Composable
fun Solution2_CurrentVersionBasic() {
    var password by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 2: 현재 버전 - 기본 비밀번호 필드",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "PasswordVisualTransformation 사용",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "현재 BOM에서 사용 가능한 방식입니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // 현재 버전에서 동작하는 코드
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            placeholder = { Text("비밀번호를 입력하세요") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "입력된 비밀번호: ${password.length}자",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * 해결책 3: 현재 버전에서 토글 포함 비밀번호 필드 구현
 */
@Composable
fun Solution3_CurrentVersionWithToggle() {
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 3: 현재 버전 - 토글 기능 추가",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "표시/숨김 토글 구현",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = """
                        |1. passwordVisible 상태 관리
                        |2. visualTransformation 조건부 적용
                        |3. trailingIcon에 토글 버튼 추가
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            placeholder = { Text("비밀번호를 입력하세요") },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
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
                text = "비밀번호: ${if (passwordVisible) "표시" else "숨김"} 상태",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${password.length}자 입력됨",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 해결책 4: Outlined 변형 + leadingIcon
 */
@Composable
fun Solution4_OutlinedVariant() {
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 4: 완성된 비밀번호 필드",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "leadingIcon + trailingIcon 조합",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "자물쇠 아이콘과 토글 아이콘을 함께 사용",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            placeholder = { Text("비밀번호를 입력하세요") },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
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

        Text(
            text = "새 API(SecureTextField)에서는 OutlinedSecureTextField로 동일하게 구현 가능",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * 해결책 5: 장점 요약
 */
@Composable
fun Solution5_BenefitsSummary() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 5: SecureTextField 장점 요약",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 방식 vs SecureTextField",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ComparisonRow("코드 양", "15줄 이상", "5줄 이하")
                    ComparisonRow("보안 기능", "수동 구현 필요", "자동 내장")
                    ComparisonRow("복사 차단", "추가 처리 필요", "기본 제공")
                    ComparisonRow("상태 관리", "value/onChange", "TextFieldState")
                    ComparisonRow("마스킹 모드", "VisualTransformation", "TextObfuscationMode")
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "업그레이드 준비",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |프로젝트를 다음 버전으로 업그레이드하면
                        |SecureTextField를 사용할 수 있습니다:
                        |
                        |Compose BOM: 2024.09.00 → 2025.04.01+
                        |Material3: 1.2.x → 1.4.0+
                        |
                        |업그레이드 후 Practice 탭의 코드를
                        |SecureTextField로 변경해보세요!
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * 비교 행 컴포넌트
 */
@Composable
private fun ComparisonRow(
    feature: String,
    oldWay: String,
    newWay: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = oldWay,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = newWay,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
    }
}
