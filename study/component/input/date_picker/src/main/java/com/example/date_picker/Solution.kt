package com.example.date_picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

/**
 * 해결책 화면
 *
 * DatePicker, DatePickerDialog, DateRangePicker를 사용하여 문제를 해결합니다.
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 데모 선택
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("기본") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("범위") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("제한") }
            )
        }

        when (selectedDemo) {
            0 -> BasicDatePickerDemo()
            1 -> DateRangePickerDemo()
            2 -> SelectableDatesDemo()
        }
    }
}

/**
 * 데모 1: 기본 DatePickerDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDatePickerDemo() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: "날짜를 선택하세요"

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "해결책: DatePickerDialog",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        DatePickerDialog를 사용하면 캘린더 UI로 날짜를 선택합니다.

                        - 형식 오류 없음 (캘린더에서 선택)
                        - 유효한 날짜만 표시됨
                        - 직관적인 UX

                        아래 TextField를 클릭해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 날짜 선택 데모
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "생년월일 선택",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { },
                    label = { Text("생년월일") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "날짜 선택")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드",
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
val datePickerState = rememberDatePickerState()

// 밀리초 -> 날짜 문자열 변환
val selectedDate = datePickerState
    .selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: "날짜를 선택하세요"

if (showDatePicker) {
    DatePickerDialog(
        onDismissRequest = { showDatePicker = false },
        confirmButton = {
            TextButton(onClick = {
                showDatePicker = false
            }) {
                Text("확인")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
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

    // DatePickerDialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * 데모 2: DateRangePicker (날짜 범위 선택)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDemo() {
    var showDateRangePicker by remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()

    val startDate = dateRangePickerState.selectedStartDateMillis?.let {
        convertMillisToDate(it)
    }
    val endDate = dateRangePickerState.selectedEndDateMillis?.let {
        convertMillisToDate(it)
    }

    // 숙박 일수 계산
    val nights = if (dateRangePickerState.selectedStartDateMillis != null &&
        dateRangePickerState.selectedEndDateMillis != null
    ) {
        val diff = dateRangePickerState.selectedEndDateMillis!! -
                dateRangePickerState.selectedStartDateMillis!!
        (diff / (1000 * 60 * 60 * 24)).toInt()
    } else {
        0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "DateRangePicker",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        시작일과 종료일을 함께 선택할 때 사용합니다.

                        - 호텔 체크인/체크아웃
                        - 휴가 기간 선택
                        - 프로젝트 기간 설정
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 날짜 범위 선택 데모
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "호텔 예약",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDateRangePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("체크인/체크아웃 선택")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 선택 결과 표시
                if (startDate != null && endDate != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "체크인",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Text(
                                        text = startDate,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "체크아웃",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Text(
                                        text = endDate,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${nights}박 ${nights + 1}일",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    Text(
                        text = "날짜를 선택해주세요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DateRangePicker 코드",
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
val state = rememberDateRangePickerState()

// 시작일, 종료일 추출
val startDate = state.selectedStartDateMillis
val endDate = state.selectedEndDateMillis

// 숙박 일수 계산
val nights = if (startDate != null && endDate != null) {
    (endDate - startDate) / (1000 * 60 * 60 * 24)
} else 0

DatePickerDialog(...) {
    DateRangePicker(
        state = state,
        title = { Text("기간 선택") },
        modifier = Modifier.height(500.dp)
    )
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

    // DateRangePicker Dialog
    if (showDateRangePicker) {
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(onClick = { showDateRangePicker = false }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = {
                    Text(
                        text = "체크인/체크아웃 선택",
                        modifier = Modifier.padding(16.dp)
                    )
                },
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            )
        }
    }
}

/**
 * 데모 3: selectableDates (날짜 제한)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableDatesDemo() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedRestriction by remember { mutableIntStateOf(0) }

    // 제한 유형에 따른 selectableDates
    val selectableDates = when (selectedRestriction) {
        0 -> FutureDatesOnly
        1 -> PastDatesOnly
        2 -> WeekdaysOnly
        else -> object : SelectableDates {}
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDateWithDay(it)
    } ?: "날짜를 선택하세요"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "selectableDates",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        특정 날짜만 선택 가능하도록 제한할 수 있습니다.

                        - 미래 날짜만 (예약용)
                        - 과거 날짜만 (생년월일)
                        - 평일만 (근무일 선택)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 제한 유형 선택
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "제한 유형 선택",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedRestriction = 0 }
                    ) {
                        RadioButton(
                            selected = selectedRestriction == 0,
                            onClick = { selectedRestriction = 0 }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("미래 날짜만", fontWeight = FontWeight.Bold)
                            Text(
                                "예약 날짜 선택에 사용",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedRestriction = 1 }
                    ) {
                        RadioButton(
                            selected = selectedRestriction == 1,
                            onClick = { selectedRestriction = 1 }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("과거 날짜만", fontWeight = FontWeight.Bold)
                            Text(
                                "생년월일 선택에 사용",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedRestriction = 2 }
                    ) {
                        RadioButton(
                            selected = selectedRestriction == 2,
                            onClick = { selectedRestriction = 2 }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("평일만 (주말 제외)", fontWeight = FontWeight.Bold)
                            Text(
                                "근무일 선택에 사용",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { },
                    label = { Text("선택된 날짜") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "날짜 선택")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SelectableDates 구현",
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
// 미래 날짜만
object FutureDatesOnly : SelectableDates {
    override fun isSelectableDate(
        utcTimeMillis: Long
    ): Boolean {
        return utcTimeMillis >=
            System.currentTimeMillis()
    }
}

// 평일만 (주말 제외)
object WeekdaysOnly : SelectableDates {
    override fun isSelectableDate(
        utcTimeMillis: Long
    ): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = utcTimeMillis
        val dayOfWeek = calendar.get(
            Calendar.DAY_OF_WEEK
        )
        return dayOfWeek != Calendar.SATURDAY
            && dayOfWeek != Calendar.SUNDAY
    }
}

// 사용
val state = rememberDatePickerState(
    selectableDates = FutureDatesOnly
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

    // DatePickerDialog (제한 적용된)
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    val title = when (selectedRestriction) {
                        0 -> "미래 날짜만 선택 가능"
                        1 -> "과거 날짜만 선택 가능"
                        2 -> "평일만 선택 가능"
                        else -> "날짜 선택"
                    }
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                    )
                }
            )
        }
    }
}

// === 유틸리티 함수 및 객체 ===

/**
 * 밀리초를 날짜 문자열로 변환
 */
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    return formatter.format(Date(millis))
}

/**
 * 밀리초를 날짜 문자열로 변환 (요일 포함)
 */
fun convertMillisToDateWithDay(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREA)
    return formatter.format(Date(millis))
}

/**
 * 미래 날짜만 선택 가능
 */
@OptIn(ExperimentalMaterial3Api::class)
object FutureDatesOnly : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= System.currentTimeMillis()
    }
}

/**
 * 과거 날짜만 선택 가능
 */
@OptIn(ExperimentalMaterial3Api::class)
object PastDatesOnly : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }
}

/**
 * 평일만 선택 가능 (주말 제외)
 */
@OptIn(ExperimentalMaterial3Api::class)
object WeekdaysOnly : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = utcTimeMillis
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
    }
}
