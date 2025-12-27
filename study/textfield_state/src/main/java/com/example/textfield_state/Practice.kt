package com.example.textfield_state

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Practice: TextFieldState 연습 문제
 *
 * 3가지 연습 문제를 통해 TextField 고급 사용법을 익힙니다.
 * 각 연습에는 TODO 힌트와 정답(주석)이 포함되어 있습니다.
 *
 * 참고: TextFieldState API는 Compose BOM 2025.04.01+ 에서 사용 가능합니다.
 * 현재는 기존 API로 동일한 개념을 연습합니다.
 */

// ============================================================
// 연습 1: 이메일 입력 필드 (기본)
// ============================================================
/**
 * 연습 1: 이메일 입력 필드 구현
 *
 * 목표:
 * - 이메일 형식 키보드 사용
 * - 입력값 상태 관리
 * - 기본적인 TextField 구성
 *
 * 새 API (BOM 2025.04.01+) 참고:
 * - rememberTextFieldState()
 * - ContentType.EmailAddress로 Autofill 지원
 */
@Composable
fun Practice1_EmailField() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 이메일 입력 필드",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |요구사항:
                        |1. 이메일 형식 키보드 표시
                        |2. 입력값을 화면에 표시
                        |3. SingleLine 적용
                        |
                        |새 API (BOM 2025.04.01+):
                        |rememberTextFieldState() +
                        |Modifier.semantics { contentType = EmailAddress }
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트 (현재 API)",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |var email by remember { mutableStateOf("") }
                        |TextField(
                        |    value = email,
                        |    onValueChange = { email = it },
                        |    keyboardOptions = KeyboardOptions(
                        |        keyboardType = KeyboardType.Email
                        |    ),
                        |    singleLine = true
                        |)
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // ========== 정답 ==========
        var email by remember { mutableStateOf("") }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일 주소") },
            placeholder = { Text("example@email.com") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "입력된 이메일: $email",
            style = MaterialTheme.typography.bodyMedium
        )
        // ========== 정답 끝 ==========
    }
}

// ============================================================
// 연습 2: 신용카드 번호 입력 (중급)
// ============================================================
/**
 * 연습 2: 신용카드 번호 입력 구현
 *
 * 목표:
 * - 16자리 숫자만 허용
 * - 4자리마다 하이픈 표시 (VisualTransformation)
 * - 예: 1234-5678-9012-3456
 *
 * 새 API (BOM 2025.04.01+) 참고:
 * - InputTransformation.maxLength(16).then { ... }
 * - OutputTransformation { insert(4, "-"); ... }
 */
@Composable
fun Practice2_CreditCardField() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 신용카드 번호 입력",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |요구사항:
                        |1. 16자리 숫자만 입력 가능
                        |2. 숫자가 아닌 문자 입력 시 무시
                        |3. 화면에는 4자리마다 하이픈 표시
                        |   예: 1234-5678-9012-3456
                        |4. 실제 저장되는 값은 숫자만
                        |
                        |새 API에서는 OutputTransformation으로
                        |자동 offset 매핑이 가능합니다.
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트 (현재 API)",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |onValueChange = { newValue ->
                        |    cardNumber = newValue
                        |        .filter { it.isDigit() }
                        |        .take(16)
                        |}
                        |
                        |visualTransformation =
                        |    CreditCardVisualTransformation()
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // ========== 정답 ==========
        var cardNumber by remember { mutableStateOf("") }

        TextField(
            value = cardNumber,
            onValueChange = { newValue ->
                cardNumber = newValue.filter { it.isDigit() }.take(16)
            },
            label = { Text("카드 번호") },
            placeholder = { Text("1234-5678-9012-3456") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            visualTransformation = PracticeCreditCardTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "저장된 값: $cardNumber",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${cardNumber.length}/16자리",
                style = MaterialTheme.typography.bodySmall,
                color = if (cardNumber.length == 16)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline
            )
        }
        // ========== 정답 끝 ==========
    }
}

/**
 * 신용카드 VisualTransformation (연습용)
 */
class PracticeCreditCardTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = buildString {
            text.forEachIndexed { index, char ->
                append(char)
                if ((index + 1) % 4 == 0 && index < text.length - 1) {
                    append('-')
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val hyphens = (offset - 1) / 4
                return (offset + hyphens).coerceAtMost(formatted.length)
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

// ============================================================
// 연습 3: 마크다운 Bold 기능 (고급)
// ============================================================
/**
 * 연습 3: 마크다운 Bold 기능 구현
 *
 * 목표:
 * - 선택된 텍스트를 **텍스트** 형식으로 변환
 * - TextFieldValue로 selection 조작
 * - 변환 후 선택 상태 유지
 *
 * 새 API (BOM 2025.04.01+) 참고:
 * - state.edit { insert(), selection = TextRange(...) }
 */
@Composable
fun Practice3_MarkdownEditor() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 마크다운 Bold 기능",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |요구사항:
                        |1. 텍스트를 선택한 후 Bold 버튼 클릭
                        |2. 선택된 텍스트를 **텍스트** 형식으로 변환
                        |3. 선택 영역이 없으면 아무 동작 안함
                        |4. 변환 후에도 선택 상태 유지
                        |
                        |새 API에서는 state.edit { } 블록으로
                        |더 간결하게 구현 가능합니다.
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |val selection = textFieldValue.selection
                        |if (selection.length > 0) {
                        |    val newText = buildString {
                        |        append(text.substring(0, selection.min))
                        |        append("**")
                        |        append(text.substring(selection.min, selection.max))
                        |        append("**")
                        |        append(text.substring(selection.max))
                        |    }
                        |    textFieldValue = TextFieldValue(
                        |        text = newText,
                        |        selection = TextRange(
                        |            selection.min + 2,
                        |            selection.max + 2
                        |        )
                        |    )
                        |}
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // ========== 정답 ==========
        var textFieldValue by remember {
            mutableStateOf(TextFieldValue("Hello World - 텍스트를 선택하고 Bold 버튼을 눌러보세요"))
        }

        // 툴바
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bold 버튼
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
                }
            ) {
                Text("Bold")
            }

            Spacer(modifier = Modifier.weight(1f))

            // 상태 정보
            Text(
                text = "선택: ${textFieldValue.selection.min}-${textFieldValue.selection.max}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // 에디터
        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("마크다운 에디터") },
            minLines = 5,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth()
        )

        // 미리보기
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "원본 텍스트:",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = textFieldValue.text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        // ========== 정답 끝 ==========

        // 추가 도전 과제
        HorizontalDivider()

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "추가 도전 과제",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. Italic 버튼 추가 (_, _)
                        |2. 코드 블록 버튼 추가 (`, `)
                        |3. 링크 버튼 추가 ([text](url))
                        |4. 실제 마크다운 렌더링 구현
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
