# TextFieldState 학습

> Compose 1.6+ 에서 도입된 새로운 상태 기반 TextField API

---

## 개념

**TextFieldState**는 기존의 `value`/`onValueChange` 패턴을 대체하는 새로운 텍스트 필드 상태 관리 방식입니다.

```kotlin
// 기존 방식 (Value-based)
var text by remember { mutableStateOf("") }
TextField(
    value = text,
    onValueChange = { text = it }
)

// 새로운 방식 (State-based)
val textFieldState = rememberTextFieldState()
TextField(state = textFieldState)
```

### 왜 새로운 API가 필요한가?

기존 `value`/`onValueChange` 패턴에는 여러 문제점이 있었습니다:

1. **비동기 동기화 문제**: 빠른 타이핑 시 키보드와 상태가 불일치할 수 있음
2. **복잡한 VisualTransformation**: offset 매핑을 직접 구현해야 함
3. **CJK 입력 문제**: 한국어, 중국어, 일본어 입력 시 조합 중 문제 발생

---

## 핵심 API

### 1. rememberTextFieldState

상태를 생성하고 기억합니다.

```kotlin
// 빈 상태
val state = rememberTextFieldState()

// 초기값 설정
val state = rememberTextFieldState(initialText = "Hello")
```

### 2. TextFieldLineLimits

줄 수 제한을 명확하게 설정합니다.

```kotlin
// 한 줄 (기존: singleLine = true)
TextField(
    state = state,
    lineLimits = TextFieldLineLimits.SingleLine
)

// 여러 줄 (기존: maxLines = 5)
TextField(
    state = state,
    lineLimits = TextFieldLineLimits.MultiLine(
        minHeightInLines = 1,
        maxHeightInLines = 5
    )
)
```

### 3. InputTransformation

사용자 입력을 **저장하기 전에** 필터링합니다.

```kotlin
// 최대 길이 제한
TextField(
    state = state,
    inputTransformation = InputTransformation.maxLength(10)
)

// 숫자만 허용
TextField(
    state = state,
    inputTransformation = InputTransformation.maxLength(10).then {
        if (!asCharSequence().isDigitsOnly()) {
            revertAllChanges()
        }
    }
)
```

### 4. OutputTransformation

저장된 텍스트를 **표시할 때만** 변환합니다. (실제 데이터는 변경되지 않음)

```kotlin
// 전화번호 포맷팅: 1234567890 → (123)456-7890
TextField(
    state = state,
    outputTransformation = OutputTransformation {
        if (length > 0) insert(0, "(")
        if (length > 4) insert(4, ")")
        if (length > 8) insert(8, "-")
    }
)
```

**핵심 장점**: offset 매핑이 자동으로 처리됩니다!

### 5. TextFieldBuffer 조작

`TextFieldState.edit { }` 블록 내에서 텍스트를 프로그래밍 방식으로 편집합니다.

```kotlin
val state = rememberTextFieldState("Hello")

// 텍스트 편집
state.edit {
    append(" World!")           // 끝에 추가
    insert(0, "- ")             // 위치에 삽입
    replace(0, 2, ">> ")        // 범위 교체
    delete(0, 3)                // 범위 삭제
    selectAll()                 // 전체 선택
    placeCursorAtEnd()          // 커서를 끝으로
}

// 간단한 메서드
state.setTextAndPlaceCursorAtEnd("New text")
state.clearText()
```

### 6. SecureTextField

비밀번호 입력을 위한 전용 컴포넌트입니다.

```kotlin
SecureTextField(
    state = passwordState,
    textObfuscationMode = TextObfuscationMode.Hidden
)
```

### 7. Autofill 지원

`semantics` modifier로 자동 완성을 활성화합니다.

```kotlin
TextField(
    state = emailState,
    modifier = Modifier.semantics {
        contentType = ContentType.EmailAddress
    }
)

// 여러 타입 지원
TextField(
    state = loginState,
    modifier = Modifier.semantics {
        contentType = ContentType.Username + ContentType.EmailAddress
    }
)
```

---

## 문제 상황: 기존 TextField의 한계

### 잘못된 코드 예시

```kotlin
@Composable
fun OldPhoneNumberField() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { newValue ->
            // 문제 1: 이미 입력된 후에 필터링됨
            text = newValue.filter { it.isDigit() }.take(10)
        },
        visualTransformation = PhoneNumberVisualTransformation()
    )
}

// 문제 2: offset 매핑을 직접 구현해야 함
class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = buildString { /* 복잡한 포맷팅 */ }

        // 커서 위치 계산 - 매우 복잡하고 오류 발생 가능
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // 복잡한 계산...
            }
            override fun transformedToOriginal(offset: Int): Int {
                // 복잡한 계산...
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
```

### 발생하는 문제점

1. **빠른 입력 시 글자 누락**: 비동기 콜백으로 인해 입력이 씹힐 수 있음
2. **깜빡임 현상**: 잘못된 입력이 순간적으로 보였다가 사라짐
3. **커서 위치 오류**: offset 매핑 실수로 커서가 엉뚱한 곳으로 이동
4. **한글 조합 문제**: 조합 중인 글자가 비정상적으로 처리됨

---

## 해결책: TextFieldState 사용

### 올바른 코드

```kotlin
@Composable
fun NewPhoneNumberField() {
    val phoneState = rememberTextFieldState()

    TextField(
        state = phoneState,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        // 입력 시점에 필터링 (저장 전)
        inputTransformation = InputTransformation.maxLength(10).then {
            if (!asCharSequence().isDigitsOnly()) {
                revertAllChanges()
            }
        },
        // 표시만 변경 (자동 offset 매핑)
        outputTransformation = OutputTransformation {
            if (length > 0) insert(0, "(")
            if (length > 4) insert(4, ")")
            if (length > 8) insert(8, "-")
        }
    )

    // 실제 저장된 값 접근
    val rawNumber = phoneState.text.toString() // "1234567890"
}
```

### 해결되는 이유

1. **동기적 처리**: InputTransformation은 입력 시점에 동기적으로 실행
2. **자동 offset 매핑**: OutputTransformation이 커서 위치를 자동 계산
3. **깔끔한 분리**: 실제 데이터(숫자)와 표시 형식(포맷)이 분리됨
4. **ViewModel 친화적**: TextFieldState는 ViewModel에서 직접 생성 가능

---

## 사용 시나리오

### 1. 로그인 폼

```kotlin
@Composable
fun LoginForm() {
    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

    Column {
        TextField(
            state = emailState,
            label = { Text("이메일") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.semantics {
                contentType = ContentType.EmailAddress
            }
        )

        SecureTextField(
            state = passwordState,
            label = { Text("비밀번호") },
            modifier = Modifier.semantics {
                contentType = ContentType.Password
            }
        )
    }
}
```

### 2. 신용카드 입력

```kotlin
@Composable
fun CreditCardField() {
    val cardState = rememberTextFieldState()

    TextField(
        state = cardState,
        inputTransformation = InputTransformation.maxLength(16).then {
            if (!asCharSequence().isDigitsOnly()) {
                revertAllChanges()
            }
        },
        outputTransformation = OutputTransformation {
            // 1234-5678-9012-3456
            if (length > 4) insert(4, "-")
            if (length > 9) insert(9, "-")
            if (length > 14) insert(14, "-")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
```

### 3. 마크다운 에디터

```kotlin
@Composable
fun MarkdownEditor() {
    val state = rememberTextFieldState()

    Column {
        Row {
            IconButton(onClick = {
                state.edit {
                    val sel = selection
                    insert(sel.max, "**")
                    insert(sel.min, "**")
                    selection = TextRange(sel.min + 2, sel.max + 2)
                }
            }) {
                Icon(Icons.Default.FormatBold, "Bold")
            }
        }

        TextField(
            state = state,
            lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 5)
        )
    }
}
```

---

## ViewModel에서 사용

TextFieldState는 순수 데이터 구조이므로 ViewModel에서 직접 생성할 수 있습니다.

```kotlin
class LoginViewModel : ViewModel() {
    val emailState = TextFieldState()
    val passwordState = TextFieldState()

    fun login() {
        val email = emailState.text.toString()
        val password = passwordState.text.toString()
        // 로그인 로직...
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    Column {
        TextField(state = viewModel.emailState)
        SecureTextField(state = viewModel.passwordState)
        Button(onClick = { viewModel.login() }) {
            Text("로그인")
        }
    }
}
```

---

## 마이그레이션 가이드

| 기존 (Value-based) | 신규 (State-based) |
|-------------------|-------------------|
| `value`, `onValueChange` | `state = rememberTextFieldState()` |
| `singleLine = true` | `lineLimits = TextFieldLineLimits.SingleLine` |
| `maxLines = 5` | `lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5)` |
| `VisualTransformation` | `OutputTransformation` |
| `onValueChange` 필터링 | `InputTransformation` |

---

## 주의사항

1. **Material 3 버전**: 1.4.0-alpha14 이상 필요
2. **Compose BOM**: 2025.04.01 이상 권장 (Autofill 지원)
3. **혼용 금지**: 같은 필드에서 value/onValueChange와 state를 함께 사용하지 마세요
4. **API 안정성**: 2025년 8월 기준 Stable API로 승격됨

---

## 연습 문제

### 연습 1: 이메일 입력 필드 (기본)
- rememberTextFieldState()로 상태 생성
- KeyboardType.Email 설정
- Autofill ContentType.EmailAddress 추가

### 연습 2: 신용카드 번호 입력 (중급)
- 16자리 숫자만 허용 (InputTransformation)
- 4자리마다 하이픈 표시 (OutputTransformation)

### 연습 3: 마크다운 Bold 기능 (고급)
- 선택된 텍스트를 **텍스트** 형식으로 변환
- TextFieldBuffer.edit { } 사용

---

## 다음 학습

- [focus_management](../focus_management/src/main/java/com/example/focus_management/README.md) - FocusRequester와 TextField 포커스 관리
- [text_typography](../text_typography/src/main/java/com/example/text_typography/README.md) - AnnotatedString, 텍스트 스타일링

---

## 참고 자료

- [Configure text fields - Android Developers](https://developer.android.com/develop/ui/compose/text/user-input)
- [Migrate to state-based text fields](https://developer.android.com/develop/ui/compose/text/migrate-state-based)
- [Autofill in Compose](https://developer.android.com/develop/ui/compose/text/autofill)
