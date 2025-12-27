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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Problem: 기존 Value-based TextField의 한계
 *
 * 기존 TextField(value, onValueChange) 패턴의 문제점:
 * 1. 비동기 콜백으로 인한 입력 누락 가능성
 * 2. VisualTransformation에서 복잡한 offset 매핑 필요
 * 3. 입력 필터링이 저장 후에 발생 (깜빡임)
 * 4. CJK(한국어 등) 입력 시 조합 문제
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
                    text = "기존 TextField 방식의 문제점",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. value/onValueChange 콜백 기반 처리
                        |2. VisualTransformation의 복잡한 offset 매핑
                        |3. 입력 후 필터링 (깜빡임 발생 가능)
                        |4. 빠른 입력 시 동기화 문제
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: 기본 필터링
        Problem1_BasicFiltering()

        HorizontalDivider()

        // 문제 2: VisualTransformation 복잡성
        Problem2_VisualTransformation()

        HorizontalDivider()

        // 문제 3: onValueChange 무한 루프 위험
        Problem3_OnValueChangeIssue()
    }
}

/**
 * 문제 1: onValueChange에서 필터링
 * - 입력 후에 필터링되므로 잘못된 입력이 순간적으로 보임
 * - 빠르게 타이핑하면 일부 입력이 누락될 수 있음
 */
@Composable
fun Problem1_BasicFiltering() {
    var phoneNumber by remember { mutableStateOf("") }
    var filterCount by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 1: onValueChange 필터링",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "문제점: 입력 후에 필터링됨",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |문자를 입력하면 잠깐 보였다가 사라짐
                        |빠른 타이핑 시 일부 입력 누락 가능
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 기존 방식: onValueChange에서 필터링
        TextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                filterCount++
                // 숫자만 허용하고 10자로 제한
                // 문제: 이미 입력된 후에 필터링됨
                phoneNumber = newValue.filter { it.isDigit() }.take(10)
            },
            label = { Text("전화번호 (숫자만)") },
            placeholder = { Text("1234567890") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "입력값: $phoneNumber",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "필터링 실행 횟수: ${filterCount}회",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * 문제 2: VisualTransformation의 복잡성
 * - offset 매핑을 직접 구현해야 함
 * - 커서 위치 계산 오류 발생 가능
 * - 유지보수가 어려움
 */
@Composable
fun Problem2_VisualTransformation() {
    var creditCard by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 2: VisualTransformation 복잡성",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "문제점: 복잡한 offset 매핑",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |VisualTransformation에서 originalToTransformed,
                        |transformedToOriginal 매핑을 직접 구현해야 함.
                        |오류 발생 시 커서가 엉뚱한 위치로 이동.
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 기존 방식: VisualTransformation 사용
        TextField(
            value = creditCard,
            onValueChange = { newValue ->
                creditCard = newValue.filter { it.isDigit() }.take(16)
            },
            label = { Text("신용카드 번호") },
            placeholder = { Text("1234 5678 9012 3456") },
            visualTransformation = ProblemCreditCardVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "실제 저장값: $creditCard",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "표시값: ${formatCreditCard(creditCard)}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 복잡한 VisualTransformation 구현 예시
 * - offset 매핑 로직이 복잡하고 오류 가능성 높음
 */
class ProblemCreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = buildString {
            text.forEachIndexed { index, char ->
                append(char)
                // 4자리마다 공백 추가 (마지막 제외)
                if ((index + 1) % 4 == 0 && index < text.length - 1) {
                    append(' ')
                }
            }
        }

        // 복잡한 offset 매핑 - 오류 발생 가능!
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // 원본 위치 -> 변환된 위치
                // 4자리마다 공백이 추가되므로 계산 필요
                if (offset <= 0) return 0
                val spaces = (offset - 1) / 4
                return (offset + spaces).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // 변환된 위치 -> 원본 위치
                if (offset <= 0) return 0
                // 공백 개수를 빼야 함
                var original = 0
                var transformed = 0
                while (transformed < offset && original < text.length) {
                    original++
                    transformed++
                    if (original % 4 == 0 && original < text.length) {
                        transformed++ // 공백 건너뛰기
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
 * 문제 3: onValueChange에서 상태 변경 시 주의사항
 * - 무한 루프나 예기치 않은 동작 발생 가능
 * - 비동기 처리와의 충돌
 */
@Composable
fun Problem3_OnValueChangeIssue() {
    var text by remember { mutableStateOf("") }
    var upperCaseText by remember { mutableStateOf("") }
    var changeCount by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 3: onValueChange 복잡한 상태 관리",
            style = MaterialTheme.typography.titleSmall
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "문제점: 여러 상태 동기화",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = """
                        |TextField 값을 다른 상태와 동기화할 때
                        |onValueChange에서 여러 상태를 업데이트하면
                        |예기치 않은 동작이 발생할 수 있음
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        TextField(
            value = text,
            onValueChange = { newValue ->
                changeCount++
                text = newValue
                // 다른 상태도 함께 업데이트
                // 이렇게 하면 recomposition이 여러 번 발생
                upperCaseText = newValue.uppercase()
            },
            label = { Text("텍스트 입력") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "원본: $text",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "대문자: $upperCaseText",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = "onValueChange 호출: ${changeCount}회",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
