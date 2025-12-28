package com.example.time_picker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Problem: TimePicker 없이 TextField로 시간 입력
 *
 * 시나리오: 알람 시간 설정 화면
 * - TextField 2개로 시/분 입력
 * - 숫자 필터링, 범위 검증 필요
 * - AM/PM 처리 복잡
 */
@Composable
fun ProblemScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("기본 입력") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("AM/PM") }
            )
        }

        when (selectedDemo) {
            0 -> TextFieldTimeInputDemo()
            1 -> AmPmTimeInputDemo()
        }
    }
}

/**
 * 문제 1: TextField로 시간 입력
 * - 숫자 필터링 필요
 * - 범위 검증 필요
 * - 에러 처리 복잡
 */
@Composable
fun TextFieldTimeInputDemo() {
    var hourText by remember { mutableStateOf("") }
    var minuteText by remember { mutableStateOf("") }
    var hourError by remember { mutableStateOf<String?>(null) }
    var minuteError by remember { mutableStateOf<String?>(null) }

    // 복잡한 검증 함수들
    fun validateHour(text: String): String? {
        if (text.isEmpty()) return "시간을 입력하세요"
        val hour = text.toIntOrNull() ?: return "숫자만 입력하세요"
        if (hour < 0 || hour > 23) return "0-23 사이로 입력하세요"
        return null
    }

    fun validateMinute(text: String): String? {
        if (text.isEmpty()) return "분을 입력하세요"
        val minute = text.toIntOrNull() ?: return "숫자만 입력하세요"
        if (minute < 0 || minute > 59) return "0-59 사이로 입력하세요"
        return null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "문제 상황: TextField로 시간 입력",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        알람 시간을 설정하기 위해 TextField로 시/분을 입력받습니다.

                        발생하는 문제:
                        - 숫자만 입력되도록 필터링 필요
                        - 0-23, 0-59 범위 검증 필요
                        - 빈 값, 잘못된 값 에러 처리 필요
                        - 코드가 복잡해짐

                        아래에 시간을 입력해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 시간 입력 UI
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "알람 시간 설정",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 시간 입력
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = hourText,
                            onValueChange = { input ->
                                // 숫자만 필터링 (복잡!)
                                val filtered = input.filter { it.isDigit() }.take(2)
                                hourText = filtered
                                hourError = validateHour(filtered)
                            },
                            label = { Text("시") },
                            isError = hourError != null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(100.dp),
                            singleLine = true
                        )
                        if (hourError != null) {
                            Text(
                                text = hourError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // 분 입력
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedTextField(
                            value = minuteText,
                            onValueChange = { input ->
                                // 숫자만 필터링 (복잡!)
                                val filtered = input.filter { it.isDigit() }.take(2)
                                minuteText = filtered
                                minuteError = validateMinute(filtered)
                            },
                            label = { Text("분") },
                            isError = minuteError != null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(100.dp),
                            singleLine = true
                        )
                        if (minuteError != null) {
                            Text(
                                text = minuteError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 결과 표시
                val isValid = hourError == null && minuteError == null &&
                        hourText.isNotEmpty() && minuteText.isNotEmpty()

                if (isValid) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "설정된 알람: ${hourText.padStart(2, '0')}:${minuteText.padStart(2, '0')}",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // 문제가 있는 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// 복잡한 검증 함수 필요!
fun validateHour(text: String): String? {
    if (text.isEmpty())
        return "시간을 입력하세요"
    val hour = text.toIntOrNull()
        ?: return "숫자만 입력하세요"
    if (hour < 0 || hour > 23)
        return "0-23 사이로 입력하세요"
    return null
}

// 필터링도 필요!
onValueChange = { input ->
    val filtered = input
        .filter { it.isDigit() }
        .take(2)
    hourText = filtered
    hourError = validateHour(filtered)
}
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

/**
 * 문제 2: AM/PM 전환 복잡성
 * - 12시간 ↔ 24시간 변환 로직
 * - 12 AM = 0시, 12 PM = 12시 처리
 */
@Composable
fun AmPmTimeInputDemo() {
    var hourText by remember { mutableStateOf("12") }
    var minuteText by remember { mutableStateOf("00") }
    var isAm by remember { mutableStateOf(true) }

    // 복잡한 변환 함수!
    fun to24Hour(hour12: Int, isAm: Boolean): Int {
        return when {
            hour12 == 12 && isAm -> 0      // 12 AM = 0시
            hour12 == 12 && !isAm -> 12    // 12 PM = 12시
            isAm -> hour12                  // AM 그대로
            else -> hour12 + 12             // PM + 12
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "문제 상황: AM/PM 변환 로직",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        12시간 형식(AM/PM)을 지원하려면 변환 로직이 필요합니다.

                        혼란스러운 케이스:
                        - 12:00 AM = 자정(0시)
                        - 12:00 PM = 정오(12시)
                        - 1:00 AM = 새벽 1시
                        - 1:00 PM = 오후 1시(13시)

                        AM/PM 버튼을 눌러 24시간 변환을 확인해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // AM/PM 입력 UI
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "12시간 형식 알람 설정",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = hourText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }.take(2)
                            val hour = filtered.toIntOrNull()
                            if (hour == null || hour in 1..12) {
                                hourText = filtered
                            }
                        },
                        label = { Text("시 (1-12)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(100.dp),
                        singleLine = true
                    )

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    OutlinedTextField(
                        value = minuteText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }.take(2)
                            val minute = filtered.toIntOrNull()
                            if (minute == null || minute in 0..59) {
                                minuteText = filtered
                            }
                        },
                        label = { Text("분") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(100.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // AM/PM 토글
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = isAm,
                        onClick = { isAm = true },
                        label = { Text("AM") }
                    )
                    FilterChip(
                        selected = !isAm,
                        onClick = { isAm = false },
                        label = { Text("PM") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 변환 결과 표시
                val hour12 = hourText.toIntOrNull() ?: 12
                val minute = minuteText.toIntOrNull() ?: 0
                val hour24 = to24Hour(hour12, isAm)

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "12시간 형식",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "${hourText.padStart(2, '0')}:${minuteText.padStart(2, '0')} ${if (isAm) "AM" else "PM"}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "24시간 형식",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "${hour24.toString().padStart(2, '0')}:${minuteText.padStart(2, '0')}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 복잡한 변환 코드 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "복잡한 AM/PM 변환 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// 12시간 → 24시간 변환 (혼란스러움!)
fun to24Hour(hour12: Int, isAm: Boolean): Int {
    return when {
        // 12 AM = 자정(0시)
        hour12 == 12 && isAm -> 0
        // 12 PM = 정오(12시)
        hour12 == 12 && !isAm -> 12
        // AM 그대로
        isAm -> hour12
        // PM + 12
        else -> hour12 + 12
    }
}

// TimePicker를 사용하면 이 복잡한
// 로직이 필요 없습니다!
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
