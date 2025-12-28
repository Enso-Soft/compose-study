# Date Picker 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

DatePicker는 사용자가 **캘린더 UI를 통해 날짜를 선택**할 수 있게 해주는 Material 3 컴포넌트입니다. 마치 종이 캘린더에서 날짜를 눈으로 보고 선택하는 것처럼, 직관적인 날짜 선택 경험을 제공합니다.

Compose에서는 `DatePicker`, `DatePickerDialog`, `DateRangePicker` 세 가지 컴포넌트로 날짜 선택 기능을 구현합니다.

---

## 핵심 특징

### 1. DatePicker - 기본 날짜 선택기

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDatePicker() {
    val datePickerState = rememberDatePickerState()

    DatePicker(
        state = datePickerState,
        showModeToggle = true  // 캘린더/입력 모드 전환 가능
    )
}
```

### 2. DatePickerDialog - 다이얼로그 형태

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogExample() {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
```

### 3. DateRangePicker - 날짜 범위 선택

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerExample() {
    val dateRangePickerState = rememberDateRangePickerState()

    DateRangePicker(
        state = dateRangePickerState,
        title = { Text("기간을 선택하세요") },
        modifier = Modifier.height(500.dp)
    )

    // 선택된 범위
    val startDate = dateRangePickerState.selectedStartDateMillis
    val endDate = dateRangePickerState.selectedEndDateMillis
}
```

---

## 문제 상황: TextField로 날짜 직접 입력

### 시나리오

회원가입 화면에서 생년월일을 입력받아야 합니다. TextField에 "YYYY/MM/DD" 형식으로 직접 입력받도록 구현했습니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun ProblemDemo() {
    var dateText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    OutlinedTextField(
        value = dateText,
        onValueChange = { input ->
            dateText = input
            // 형식 검증 시도...
            errorMessage = try {
                validateDateFormat(input)
                null
            } catch (e: Exception) {
                "올바른 형식으로 입력하세요 (YYYY/MM/DD)"
            }
        },
        label = { Text("생년월일 (YYYY/MM/DD)") },
        isError = errorMessage != null,
        supportingText = { errorMessage?.let { Text(it) } }
    )
}

// 복잡한 검증 로직 필요
fun validateDateFormat(input: String): Boolean {
    // 형식 확인
    // 유효한 날짜인지 확인
    // 월별 일수 확인
    // ...매우 복잡!
    return true
}
```

### 발생하는 문제점

1. **다양한 형식 입력 가능**
   - "2024-01-15", "01/15/2024", "20240115" 등 사용자마다 다른 형식
   - 모든 형식을 처리하는 복잡한 파싱 로직 필요

2. **유효하지 않은 날짜 입력 가능**
   - 2월 30일, 13월 1일 같은 존재하지 않는 날짜
   - 월별 일수, 윤년 등 복잡한 검증 필요

3. **UX 불편**
   - 캘린더 없이 날짜를 기억해서 입력해야 함
   - 숫자 키보드 타이핑 번거로움
   - 오늘 날짜가 몇 일인지 모르는 경우

---

## 해결책: DatePickerDialog 사용

### 올바른 코드

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionDemo() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // 선택된 날짜를 문자열로 변환
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: "날짜를 선택하세요"

    // 읽기 전용 TextField로 표시
    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { Text("생년월일") },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, "날짜 선택")
            }
        },
        modifier = Modifier.clickable { showDatePicker = true }
    )

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

// 밀리초를 날짜 문자열로 변환
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    return formatter.format(Date(millis))
}
```

### 해결되는 이유

1. **형식 오류 없음**: 캘린더에서 선택하므로 형식 검증 불필요
2. **유효한 날짜만 선택 가능**: 존재하지 않는 날짜는 표시되지 않음
3. **UX 향상**: 시각적 캘린더로 직관적인 날짜 선택

---

## selectedDateMillis 변환

DatePicker의 선택된 날짜는 **밀리초(milliseconds)** 형태로 저장됩니다. 밀리초는 1970년 1월 1일부터 경과한 시간을 1/1000초 단위로 나타낸 값입니다.

사람이 읽을 수 있는 날짜로 변환하려면:

```kotlin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 밀리초 -> 날짜 문자열
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

// 사용 예
val datePickerState = rememberDatePickerState()
val dateString = datePickerState.selectedDateMillis?.let {
    convertMillisToDate(it)
} ?: "선택 안 됨"
```

### 다양한 형식

```kotlin
// 한국어 형식
SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
// 결과: "2024년 01월 15일"

// ISO 형식
SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
// 결과: "2024-01-15"

// 요일 포함
SimpleDateFormat("yyyy.MM.dd (E)", Locale.KOREA)
// 결과: "2024.01.15 (월)"
```

---

## selectableDates: 선택 가능 날짜 제한

특정 날짜만 선택할 수 있도록 제한할 수 있습니다.

### 기본 구조

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
val datePickerState = rememberDatePickerState(
    selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            // true: 선택 가능, false: 선택 불가
            return true
        }

        override fun isSelectableYear(year: Int): Boolean {
            // 특정 연도만 허용
            return true
        }
    }
)
```

### 실용적인 예제

```kotlin
// 1. 미래 날짜만 선택 가능 (예약용)
object FutureDatesOnly : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= System.currentTimeMillis()
    }
}

// 2. 과거 날짜만 선택 가능 (생년월일용)
object PastDatesOnly : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }
}

// 3. 주말 제외 (평일만)
object WeekdaysOnly : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = utcTimeMillis
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
    }
}

// 사용
val datePickerState = rememberDatePickerState(
    selectableDates = FutureDatesOnly
)
```

---

## DateRangePicker 사용법

시작일과 종료일을 함께 선택해야 할 때 사용합니다.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeExample() {
    var showDialog by remember { mutableStateOf(false) }
    val dateRangeState = rememberDateRangePickerState()

    Button(onClick = { showDialog = true }) {
        Text("기간 선택")
    }

    // 선택된 범위 표시
    dateRangeState.selectedStartDateMillis?.let { start ->
        dateRangeState.selectedEndDateMillis?.let { end ->
            val startDate = convertMillisToDate(start)
            val endDate = convertMillisToDate(end)
            val days = (end - start) / (1000 * 60 * 60 * 24)

            Text("$startDate ~ $endDate ($days박)")
        }
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("확인")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangeState,
                title = { Text("체크인/체크아웃 선택") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            )
        }
    }
}
```

---

## 사용 시나리오

### 1. 생년월일 입력

```kotlin
val datePickerState = rememberDatePickerState(
    selectableDates = PastDatesOnly  // 과거 날짜만
)
```

### 2. 예약 날짜 선택

```kotlin
val datePickerState = rememberDatePickerState(
    selectableDates = FutureDatesOnly  // 미래 날짜만
)
```

### 3. 호텔 체크인/체크아웃

```kotlin
val dateRangeState = rememberDateRangePickerState(
    selectableDates = FutureDatesOnly
)
```

### 4. 근무 가능일 선택

```kotlin
val datePickerState = rememberDatePickerState(
    selectableDates = WeekdaysOnly  // 평일만
)
```

---

## 주의사항

1. **ExperimentalMaterial3Api**
   - DatePicker 관련 API는 실험적이므로 `@OptIn(ExperimentalMaterial3Api::class)` 필요
   - 향후 API 변경 가능성 있음

2. **밀리초 타임존**
   - `selectedDateMillis`는 UTC 기준
   - 표시할 때 로컬 타임존 고려 필요

3. **DateRangePicker 높이**
   - DateRangePicker는 두 달을 표시하므로 충분한 높이 필요
   - 최소 `height(500.dp)` 권장

4. **Dialog 상태 관리**
   - `showDialog` 상태로 다이얼로그 표시/숨김 제어
   - 확인/취소 버튼에서 상태 업데이트 필수

---

## 연습 문제

### 연습 1: 생년월일 선택기 (쉬움)

TextField를 클릭하면 DatePickerDialog가 표시되고, 날짜를 선택하면 "YYYY년 MM월 DD일" 형식으로 표시되는 생년월일 선택기를 구현하세요.

**요구사항:**
- 미래 날짜는 선택 불가
- 선택 후 TextField에 날짜 표시

### 연습 2: 호텔 예약 날짜 선택 (중간)

체크인/체크아웃 날짜를 선택하는 DateRangePicker를 구현하세요.

**요구사항:**
- 오늘 이전 날짜 선택 불가
- 선택된 기간과 숙박 일수 표시 (예: "1월 15일 ~ 1월 17일 (2박)")

### 연습 3: 근무 가능일 선택 (어려움)

주말과 공휴일을 제외한 평일만 선택 가능한 DatePicker를 구현하세요.

**요구사항:**
- 주말(토, 일) 선택 불가
- 제공된 공휴일 목록도 선택 불가
- 오늘부터 30일 이내만 선택 가능

---

## 다음 학습

- **TimePicker**: 시간 선택 컴포넌트
- **BottomSheet**: DatePicker를 BottomSheet에 표시
- **Dialog**: 다양한 다이얼로그 패턴
