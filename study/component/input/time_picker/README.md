# TimePicker 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

TimePicker와 TimeInput은 사용자가 **시간을 선택**할 수 있게 해주는 Material 3 컴포넌트입니다.

- **TimePicker**: 다이얼(시계 모양)을 돌려 시간을 선택하는 UI
- **TimeInput**: 키보드로 시간을 직접 입력하는 UI

---

## 핵심 특징

### 1. TimePicker (다이얼 형태)

```kotlin
val timePickerState = rememberTimePickerState(
    initialHour = 9,
    initialMinute = 30,
    is24Hour = true,
)

TimePicker(state = timePickerState)
```

아날로그 시계처럼 시/분 바늘을 돌려 시간을 설정합니다.

### 2. TimeInput (키보드 입력 형태)

```kotlin
TimeInput(state = timePickerState)
```

숫자를 직접 입력해서 시간을 설정합니다. 키보드 입력이 편한 상황에 적합합니다.

### 3. rememberTimePickerState

```kotlin
val timePickerState = rememberTimePickerState(
    initialHour = 14,      // 초기 시간 (0-23)
    initialMinute = 30,    // 초기 분 (0-59)
    is24Hour = true,       // true: 24시간, false: 12시간(AM/PM)
)

// 선택된 시간 읽기
val hour = timePickerState.hour      // 0-23
val minute = timePickerState.minute  // 0-59
```

---

## 문제 상황: TextField로 시간 입력

### 시나리오

알람 앱에서 사용자가 알람 시간을 설정해야 합니다.
TextField 2개로 시/분을 입력받으려고 하면 여러 문제가 발생합니다.

### 잘못된 코드 예시

```kotlin
// 문제가 많은 코드!
@Composable
fun ManualTimeInput() {
    var hourText by remember { mutableStateOf("") }
    var minuteText by remember { mutableStateOf("") }

    Row {
        TextField(
            value = hourText,
            onValueChange = {
                // 숫자만 입력되도록 필터링 필요
                // 0-23 범위 검증 필요
                // 빈 값 처리 필요
                hourText = it.filter { c -> c.isDigit() }
            },
            label = { Text("시") }
        )
        Text(":")
        TextField(
            value = minuteText,
            onValueChange = {
                // 숫자만 입력되도록 필터링 필요
                // 0-59 범위 검증 필요
                minuteText = it.filter { c -> c.isDigit() }
            },
            label = { Text("분") }
        )
    }
}
```

### 발생하는 문제점

1. **입력 검증 복잡**: 숫자만 입력, 범위 검증 로직 필요
2. **AM/PM 처리 어려움**: 12시간 형식 지원 시 변환 로직 복잡
3. **UX 저하**: 키보드 팝업 필요, 직관적이지 않음
4. **에러 처리 부담**: 잘못된 입력에 대한 피드백 구현 필요
5. **코드량 증가**: 단순한 시간 입력에 많은 코드 필요

---

## 해결책: TimePicker 사용

### 올바른 코드

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerExample() {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Column {
        TimePicker(state = timePickerState)

        Text(
            text = "선택된 시간: ${String.format("%02d:%02d",
                timePickerState.hour,
                timePickerState.minute)}"
        )
    }
}
```

### 해결되는 이유

1. **입력 검증 내장**: 올바른 범위만 선택 가능
2. **AM/PM 자동 처리**: is24Hour 설정으로 간단히 전환
3. **직관적 UX**: 터치로 쉽게 시간 선택
4. **에러 없음**: 잘못된 값 입력 불가능
5. **코드 간결**: 몇 줄로 완성

---

## 사용 시나리오

### 1. 기본 TimePicker

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTimePicker() {
    val timePickerState = rememberTimePickerState(
        initialHour = 9,
        initialMinute = 0,
        is24Hour = true,
    )

    TimePicker(state = timePickerState)
}
```

### 2. TimeInput (키보드 입력)

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyboardTimeInput() {
    val timePickerState = rememberTimePickerState(
        initialHour = 14,
        initialMinute = 30,
        is24Hour = true,
    )

    TimeInput(state = timePickerState)
}
```

### 3. 24시간 vs 12시간 형식

```kotlin
// 24시간 형식 (오후 2시 = 14:00)
rememberTimePickerState(is24Hour = true)

// 12시간 형식 (오후 2시 = 2:00 PM)
rememberTimePickerState(is24Hour = false)
```

### 4. TimePickerDialog (가장 일반적)

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("시간 선택") },
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
```

---

## 주의사항

### 1. 실험적 API

TimePicker와 TimeInput은 아직 실험적(Experimental) API입니다.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePicker() {
    // ...
}
```

### 2. 초기 시간 설정

Calendar를 사용해 현재 시간으로 초기화하는 것이 일반적입니다.

```kotlin
val currentTime = Calendar.getInstance()
val timePickerState = rememberTimePickerState(
    initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
    initialMinute = currentTime.get(Calendar.MINUTE),
    is24Hour = true,
)
```

### 3. 선택된 시간 읽기

TimePickerState에서 직접 읽을 수 있습니다.

```kotlin
val hour = timePickerState.hour      // Int (0-23)
val minute = timePickerState.minute  // Int (0-59)
```

### 4. 다이얼로그와 함께 사용

실제 앱에서는 버튼을 누르면 다이얼로그가 열리고, 그 안에서 시간을 선택하는 패턴이 가장 일반적입니다.

---

## 연습 문제

### 연습 1: 기본 TimePicker 표시 - 쉬움

현재 시간으로 초기화된 TimePicker를 표시하고, 선택된 시간을 Text로 보여주세요.

### 연습 2: TimePickerDialog 구현 - 중간

"시간 선택" 버튼을 누르면 AlertDialog가 열리고, 확인/취소 버튼이 있는 TimePickerDialog를 구현하세요.

### 연습 3: 알람 시간 범위 설정 - 어려움

"시작 시간"과 "종료 시간"을 각각 설정할 수 있는 알람 UI를 구현하세요.
TimePicker와 TimeInput 간 전환도 가능하게 만드세요.

---

## 다음 학습

- **DatePicker**: 날짜 선택 컴포넌트
- **DatePickerDialog**: 다이얼로그로 날짜 선택
- **Dialog Basics**: AlertDialog 기본 사용법
