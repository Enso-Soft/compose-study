# TextFieldState 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

> Compose Foundation 1.8+ / Material 3 1.4.0+ 에서 도입된 새로운 상태 기반 TextField API

---

## 개념

**TextFieldState**는 기존의 `value`/`onValueChange` 패턴을 대체하는 새로운 텍스트 필드 상태 관리 방식입니다.
2025년 8월 기준 Stable API로 승격되었으며, Google I/O 2025에서 공식 권장 방식으로 발표되었습니다.

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

---

## 핵심 특징

1. **동기적 입력 처리**: 비동기 콜백이 아닌 동기적으로 입력을 처리하여 입력 누락 없음
2. **자동 offset 매핑**: OutputTransformation이 커서 위치를 자동으로 계산
3. **ViewModel 친화적**: UI 의존성 없이 ViewModel에서 직접 상태 관리 가능

---

## 문제 상황: 기존 TextField의 한계

### 시나리오

전화번호 입력 필드를 구현하려고 합니다. 숫자만 입력받고, 화면에는 `(010)1234-5678` 형식으로 표시해야 합니다.

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

| 문제 | 기존 방식 | TextFieldState |
|------|----------|----------------|
| 입력 처리 | 비동기 콜백 | 동기적 처리 |
| 필터링 시점 | 저장 후 | 저장 전 (InputTransformation) |
| offset 매핑 | 수동 구현 | 자동 처리 (OutputTransformation) |
| 데이터/표시 분리 | 혼재 | 명확하게 분리 |

---

## 핵심 API 상세

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

**Best Practice**: 가장 선택적인 필터부터 적용하세요. 불필요한 변환을 피할 수 있습니다.

### 4. OutputTransformation

저장된 텍스트를 **표시할 때만** 변환합니다. (실제 데이터는 변경되지 않음)

```kotlin
// 전화번호 포맷팅: 1234567890 -> (123)456-7890
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
이는 "ViewModel에 UI 의존성 금지" 원칙을 재고하게 만드는 중요한 변화입니다.

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

**주의**: ViewModel에서 TextFieldState를 사용할 경우, `rememberSaveable`의 저장/복원 기능이 자동으로 적용되지 않습니다. 필요하다면 별도로 `SavedStateHandle`을 사용하여 상태를 저장해야 합니다.

---

## 마이그레이션 가이드

| 기존 (Value-based) | 신규 (State-based) |
|-------------------|-------------------|
| `value`, `onValueChange` | `state = rememberTextFieldState()` |
| `singleLine = true` | `lineLimits = TextFieldLineLimits.SingleLine` |
| `maxLines = 5` | `lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5)` |
| `VisualTransformation` | `OutputTransformation` |
| `onValueChange` 필터링 | `InputTransformation` |
| `PasswordVisualTransformation` | `SecureTextField` |

---

## 주의사항

1. **버전 요구사항**
   - Compose Foundation 1.8+
   - Material 3 버전 1.4.0-alpha14 이상
   - Compose BOM 2025.04.01 이상 권장 (Autofill 지원)

2. **API 안정성**: 2025년 8월 기준 Stable API로 승격됨 (BasicTextField2 -> BasicTextField 이름 변경)

3. **혼용 금지**: 같은 필드에서 value/onValueChange와 state를 함께 사용하지 마세요

4. **ViewModel 저장**: ViewModel에서 사용 시 상태 저장/복원을 별도로 처리해야 합니다

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

- [focus_management](../../../interaction/focus_management/README.md) - FocusRequester와 TextField 포커스 관리
- [text_typography](../../../layout/text_typography/README.md) - AnnotatedString, 텍스트 스타일링

---

## 참고 자료

- [Configure text fields - Android Developers](https://developer.android.com/develop/ui/compose/text/user-input)
- [Migrate to state-based text fields](https://developer.android.com/develop/ui/compose/text/migrate-state-based)
- [Autofill in Compose](https://developer.android.com/develop/ui/compose/text/autofill)
- [Effective state management for TextField - Medium](https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5)
- [TextFieldState API Reference](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/input/TextFieldState)
