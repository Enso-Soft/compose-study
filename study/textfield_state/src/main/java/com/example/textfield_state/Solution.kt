package com.example.textfield_state

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Solution: TextFieldState API 개념 학습
 *
 * 참고: TextFieldState는 Compose BOM 2025.04.01+ 에서 완전히 지원됩니다.
 * 현재 프로젝트의 BOM 버전에서는 기존 API로 개념을 설명합니다.
 *
 * TextFieldState의 핵심 개념:
 * 1. rememberTextFieldState() - 상태 생성 및 기억
 * 2. InputTransformation - 입력 시점 필터링 (저장 전)
 * 3. OutputTransformation - 출력 포맷팅 (자동 offset 매핑)
 * 4. TextFieldBuffer - 프로그래밍 방식 편집
 * 5. SecureTextField - 비밀번호 입력
 * 6. Autofill - 자동 완성 지원
 */

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
                    text = "TextFieldState 기반 새로운 API",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |Compose BOM 2025.04.01+ 에서 사용 가능:
                        |
                        |1. rememberTextFieldState()로 상태 생성
                        |2. InputTransformation으로 입력 필터링
                        |3. OutputTransformation으로 표시 변환 (자동 offset)
                        |4. TextFieldBuffer.edit {}로 프로그래밍 편집
                        |5. SecureTextField로 비밀번호 입력
                        |
                        |현재는 기존 API로 개념을 설명합니다.
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

        // 해결책 2: 카드번호 포맷팅 (가장 복잡한 예시)
        Solution2_CreditCardField()

        HorizontalDivider()

        // 해결책 3: 전화번호 포맷팅 (기존 API로 구현)
        Solution3_PhoneNumberField()

        HorizontalDivider()

        // 해결책 4: TextFieldValue로 프로그래밍 편집
        Solution4_ProgrammaticEditing()

        HorizontalDivider()

        // 해결책 5: 비밀번호 입력
        Solution5_PasswordField()

        HorizontalDivider()

        // 해결책 6: 마이그레이션 가이드
        Solution6_MigrationGuide()
    }
}

/**
 * 해결책 1: 새로운 TextFieldState API 코드 예시
 */
@Composable
fun Solution1_NewApiExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 1: TextFieldState API (BOM 2025.04.01+)",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "새로운 API 사용 예시 (코드만)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |// 기본 사용법
                        |val state = rememberTextFieldState()
                        |TextField(state = state)
                        |
                        |// 입력 필터링 + 출력 포맷팅
                        |TextField(
                        |    state = rememberTextFieldState(),
                        |    inputTransformation = InputTransformation
                        |        .maxLength(10)
                        |        .then {
                        |            if (!asCharSequence().isDigitsOnly()) {
                        |                revertAllChanges()
                        |            }
                        |        },
                        |    outputTransformation = OutputTransformation {
                        |        if (length > 0) insert(0, "(")
                        |        if (length > 4) insert(4, ")")
                        |        if (length > 8) insert(8, "-")
                        |    }
                        |)
                        |
                        |// 프로그래밍 편집
                        |state.edit {
                        |    insert(0, "Hello ")
                        |    append("!")
                        |    selectAll()
                        |}
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * 해결책 2: 카드번호 포맷팅 (가장 복잡한 예시)
 * - 16자리 숫자를 4-4-4-4 형식으로 표시
 * - VisualTransformation으로 포맷팅
 * - 새 API에서는 OutputTransformation으로 간단하게 구현 가능
 */
@Composable
fun Solution2_CreditCardField() {
    var creditCard by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 2: 카드번호 포맷팅",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "VisualTransformation으로 4-4-4-4 포맷",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = """
                        |현재 API: VisualTransformation + 복잡한 offset 매핑
                        |새 API: OutputTransformation {
                        |    if (length > 4) insert(4, " ")
                        |    if (length > 9) insert(9, " ")
                        |    if (length > 14) insert(14, " ")
                        |}
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        OutlinedTextField(
            value = creditCard,
            onValueChange = { newValue ->
                creditCard = newValue.filter { it.isDigit() }.take(16)
            },
            label = { Text("카드 번호") },
            placeholder = { Text("1234 5678 9012 3456") },
            visualTransformation = CreditCardVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "저장된 값: $creditCard",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "길이: ${creditCard.length}/16",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = "표시 형식: ${formatCreditCard(creditCard)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 카드번호 VisualTransformation
 * - 4자리마다 공백 삽입
 * - offset 매핑 자동 처리
 */
class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = buildString {
            text.forEachIndexed { index, char ->
                append(char)
                if ((index + 1) % 4 == 0 && index < text.length - 1) {
                    append(' ')
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val spaces = (offset - 1) / 4
                return (offset + spaces).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                var original = 0
                var transformed = 0
                while (transformed < offset && original < text.length) {
                    original++
                    transformed++
                    if (original % 4 == 0 && original < text.length) {
                        transformed++
                    }
                }
                return original.coerceAtMost(text.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

private fun formatCreditCard(text: String): String {
    return text.chunked(4).joinToString(" ")
}

/**
 * 해결책 3: 전화번호 포맷팅 (기존 API로 구현)
 */
@Composable
fun Solution3_PhoneNumberField() {
    var phoneNumber by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 3: 전화번호 포맷팅",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "VisualTransformation으로 (010)1234-5678 포맷",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = """
                        |현재 API: VisualTransformation + offset 매핑
                        |새 API: OutputTransformation (자동 offset)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        TextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                phoneNumber = newValue.filter { it.isDigit() }.take(10)
            },
            label = { Text("전화번호") },
            placeholder = { Text("(010)1234-5678") },
            visualTransformation = PhoneNumberVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "저장된 값: $phoneNumber",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "길이: ${phoneNumber.length}/10",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 전화번호 VisualTransformation
 */
class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(10)
        val formatted = buildString {
            if (trimmed.isNotEmpty()) append("(")
            append(trimmed.take(3))
            if (trimmed.length > 3) {
                append(")")
                append(trimmed.substring(3, minOf(7, trimmed.length)))
            }
            if (trimmed.length > 7) {
                append("-")
                append(trimmed.substring(7))
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                return when {
                    offset <= 3 -> offset + 1  // ( 추가
                    offset <= 7 -> offset + 2  // () 추가
                    else -> offset + 3         // ()- 추가
                }.coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                return when {
                    offset <= 1 -> 0
                    offset <= 4 -> offset - 1
                    offset <= 5 -> 3
                    offset <= 9 -> offset - 2
                    offset <= 10 -> 7
                    else -> offset - 3
                }.coerceAtMost(text.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

/**
 * 해결책 4: TextFieldValue로 프로그래밍 편집
 * 새 API의 TextFieldBuffer.edit {} 개념을 기존 API로 시뮬레이션
 */
@Composable
fun Solution4_ProgrammaticEditing() {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue("Hello World"))
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 4: 프로그래밍 편집",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "TextFieldValue로 selection 조작",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = """
                        |현재: TextFieldValue.copy(text, selection)
                        |새 API: state.edit { insert(), append() }
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("텍스트 편집기") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Bold 추가 (마크다운)
            FilledTonalButton(
                onClick = {
                    val selection = textFieldValue.selection
                    if (selection.length > 0) {
                        val newText = buildString {
                            append(textFieldValue.text.substring(0, selection.min))
                            append("**")
                            append(textFieldValue.text.substring(selection.min, selection.max))
                            append("**")
                            append(textFieldValue.text.substring(selection.max))
                        }
                        textFieldValue = TextFieldValue(
                            text = newText,
                            selection = TextRange(selection.min + 2, selection.max + 2)
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Bold")
            }

            // 전체 선택
            TextButton(
                onClick = {
                    textFieldValue = textFieldValue.copy(
                        selection = TextRange(0, textFieldValue.text.length)
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("전체 선택")
            }

            // 지우기
            TextButton(
                onClick = {
                    textFieldValue = TextFieldValue("")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("지우기")
            }
        }

        Text(
            text = "선택 범위: ${textFieldValue.selection.min}-${textFieldValue.selection.max}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * 해결책 5: 비밀번호 입력
 */
@Composable
fun Solution5_PasswordField() {
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 5: 비밀번호 입력",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "PasswordVisualTransformation",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = """
                        |현재: PasswordVisualTransformation()
                        |새 API: SecureTextField(state, textObfuscationMode)
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
            singleLine = true,
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        if (showPassword) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = if (showPassword) "숨기기" else "보기"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "길이: ${password.length}자",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 해결책 6: 마이그레이션 가이드
 */
@Composable
fun Solution6_MigrationGuide() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 6: 마이그레이션 가이드",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "API 변경 사항",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |기존 API           ->  새 API
                        |────────────────────────────────
                        |value, onValueChange  state = rememberTextFieldState()
                        |singleLine = true     lineLimits = SingleLine
                        |maxLines = 5          lineLimits = MultiLine(max=5)
                        |VisualTransformation  OutputTransformation
                        |onValueChange 필터링  InputTransformation
                        |PasswordVisualT..     SecureTextField
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "업그레이드 방법",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. Compose BOM을 2025.04.01 이상으로 업그레이드
                        |2. Material 3 버전 1.4.0-alpha14 이상 확인
                        |3. value/onValueChange -> state 변경
                        |4. VisualTransformation -> OutputTransformation
                        |5. 필터링 로직 -> InputTransformation
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "새 API의 장점",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. 동기적 입력 처리 - 입력 누락 없음
                        |2. 자동 offset 매핑 - 커서 위치 오류 없음
                        |3. ViewModel 친화적 - UI 의존성 없음
                        |4. 간결한 코드 - 보일러플레이트 감소
                        |5. Autofill 기본 지원
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
