package com.example.date_picker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * TextField로 날짜를 직접 입력받을 때 발생하는 문제를 시연합니다.
 */
@Composable
fun ProblemScreen() {
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
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        회원가입 화면에서 생년월일을 입력받아야 합니다.

                        TextField에 "YYYY/MM/DD" 형식으로 직접 입력받도록 구현했습니다.

                        아래에서 직접 날짜를 입력해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 데모
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "날짜 입력 시도해보기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                ProblemDemo()
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 다양한 형식 입력 가능 (2024-01-15, 01/15/2024, 20240115...)")
                Text("2. 유효하지 않은 날짜 입력 가능 (2월 30일, 13월 1일...)")
                Text("3. 캘린더 없이 날짜를 기억해서 입력해야 함")
                Text("4. 형식 검증 로직이 매우 복잡해짐")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// TextField로 날짜 직접 입력
OutlinedTextField(
    value = dateText,
    onValueChange = { input ->
        dateText = input
        // 형식 검증 필요...
        // 유효성 검증 필요...
        // 월별 일수 확인 필요...
        // 윤년 확인 필요...
        // -> 매우 복잡!
    },
    label = { Text("YYYY/MM/DD") }
)
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun ProblemDemo() {
    var dateText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var attempts by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = dateText,
            onValueChange = { input ->
                dateText = input
                attempts++
                errorMessage = validateDateInput(input)
            },
            label = { Text("생년월일 (YYYY/MM/DD)") },
            placeholder = { Text("예: 1990/05/15") },
            isError = errorMessage != null,
            supportingText = {
                if (errorMessage != null) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                } else if (dateText.isNotEmpty()) {
                    Text("형식이 올바릅니다", color = MaterialTheme.colorScheme.primary)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 입력 시도 횟수 및 안내
        if (attempts > 0) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "입력 시도: ${attempts}회",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "이 입력 방식의 문제점:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "- 형식을 외워야 합니다\n- 오타가 발생하기 쉽습니다\n- 캘린더를 볼 수 없습니다",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 테스트 케이스 버튼들
        Text(
            text = "문제를 체험해보세요:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    dateText = "1990-05-15"  // 다른 형식
                    attempts++
                    errorMessage = validateDateInput(dateText)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("1990-05-15", style = MaterialTheme.typography.labelSmall)
            }
            OutlinedButton(
                onClick = {
                    dateText = "05/15/1990"  // 미국식 형식
                    attempts++
                    errorMessage = validateDateInput(dateText)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("05/15/1990", style = MaterialTheme.typography.labelSmall)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    dateText = "2024/02/30"  // 존재하지 않는 날짜
                    attempts++
                    errorMessage = validateDateInput(dateText)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("2024/02/30", style = MaterialTheme.typography.labelSmall)
            }
            OutlinedButton(
                onClick = {
                    dateText = "2024/13/01"  // 존재하지 않는 월
                    attempts++
                    errorMessage = validateDateInput(dateText)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("2024/13/01", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/**
 * 날짜 입력 검증 함수
 * 이 함수가 얼마나 복잡한지 보여주기 위한 예시
 */
private fun validateDateInput(input: String): String? {
    if (input.isEmpty()) return null

    // 형식 검증: YYYY/MM/DD
    val regex = Regex("""^\d{4}/\d{2}/\d{2}$""")
    if (!input.matches(regex)) {
        return "형식이 올바르지 않습니다 (YYYY/MM/DD)"
    }

    // 날짜 파싱
    val parts = input.split("/")
    val year = parts[0].toIntOrNull() ?: return "연도가 올바르지 않습니다"
    val month = parts[1].toIntOrNull() ?: return "월이 올바르지 않습니다"
    val day = parts[2].toIntOrNull() ?: return "일이 올바르지 않습니다"

    // 월 범위 검증
    if (month < 1 || month > 12) {
        return "월은 1~12 사이여야 합니다"
    }

    // 월별 일수 검증
    val daysInMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 0
    }

    if (day < 1 || day > daysInMonth) {
        return "${month}월은 ${daysInMonth}일까지입니다"
    }

    // 미래 날짜 검증 (생년월일이므로)
    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    if (year > currentYear) {
        return "미래 날짜는 입력할 수 없습니다"
    }

    return null  // 검증 통과
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
