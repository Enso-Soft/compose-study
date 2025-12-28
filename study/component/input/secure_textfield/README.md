# SecureTextField 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `TextField` | Compose의 기본 텍스트 입력 컴포넌트 | [📚 학습하기](../textfield/README.md) |
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**SecureTextField**는 비밀번호 입력을 위해 특별히 설계된 Compose Material3 컴포넌트입니다.
기존 TextField + PasswordVisualTransformation 조합을 대체하여, 보안 기능이 내장된 간결한 API를 제공합니다.

> SecureTextField는 '보안 금고'와 같습니다.
> - 기존 TextField + 설정들 = 여러 자물쇠를 직접 달아야 함
> - SecureTextField = 이미 보안 시스템이 갖춰진 금고
>
> 금고를 사면 자물쇠, CCTV, 지문인식이 이미 설치되어 있듯이,
> SecureTextField를 사용하면 복사 차단, 보안 키보드, 마스킹이 자동으로 적용됩니다.

---

## 핵심 특징

### 1. TextFieldState 기반 상태 관리

**TextFieldState**는 텍스트 필드의 '기억 창고'입니다.
- 입력된 텍스트
- 커서 위치
- 선택 범위

이 모든 것을 안전하게 보관하여, 화면이 다시 그려져도(Recomposition) 입력 내용이 사라지지 않습니다.

```kotlin
val passwordState = rememberTextFieldState()
SecureTextField(state = passwordState)
```

### 2. TextObfuscationMode - 비밀번호 가림막 모드

**TextObfuscationMode**는 비밀번호 '가림막' 모드입니다.

| 모드 | 설명 | 표시 예시 |
|------|------|----------|
| Hidden | 모든 글자를 가림 (가장 안전) | ● ● ● ● ● |
| Visible | 모든 글자를 보여줌 (확인용) | password |
| RevealLastTyped | 마지막 입력 글자만 잠시 보여주고 숨김 (기본값) | ● ● ● ● d |

> RevealLastTyped는 스마트폰에서 타이핑할 때 어떤 글자를 눌렀는지 확인하기 좋습니다!

### 3. 내장 보안 기능

SecureTextField를 사용하면 자동으로 적용되는 보안 기능:

- **복사/잘라내기 비활성화**: 컨텍스트 메뉴에서 복사/잘라내기 차단
- **드래그 비활성화**: 텍스트 드래그 불가
- **보안 키보드 연동**: 키보드에 보안 모드 알림
- **단일 줄 입력**: 비밀번호는 한 줄만 입력 가능

### 4. 두 가지 스타일 변형

| 컴포넌트 | 스타일 | 사용 시나리오 |
|----------|--------|--------------|
| SecureTextField | Filled (채워진) | 시각적 강조가 필요할 때 |
| OutlinedSecureTextField | Outlined (테두리) | 폼에서 여러 필드와 함께 사용할 때 |

---

## 문제 상황: 기존 방식의 복잡성

### 시나리오

로그인 화면에 비밀번호 입력 필드를 구현해야 합니다.

### 기존 방식 (TextField + visualTransformation)

```kotlin
// 20줄 이상의 코드가 필요합니다!
var password by remember { mutableStateOf("") }
var passwordVisible by remember { mutableStateOf(false) }

TextField(
    value = password,
    onValueChange = { password = it },
    label = { Text("비밀번호") },
    visualTransformation = if (passwordVisible)
        VisualTransformation.None
    else
        PasswordVisualTransformation(),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    trailingIcon = {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
                imageVector = if (passwordVisible)
                    Icons.Default.VisibilityOff
                else
                    Icons.Default.Visibility,
                contentDescription = null
            )
        }
    }
)
```

### 발생하는 문제점

1. **코드 복잡성**: 비밀번호 필드 하나에 여러 설정이 필요
2. **보안 기능 누락 위험**: 복사/붙여넣기 차단을 잊기 쉬움
3. **상태 관리 불편**: value/onValueChange 콜백 패턴 사용
4. **화면 회전 시 상태 손실**: rememberSaveable 사용 필요

---

## 해결책: SecureTextField 사용

### 간결한 새로운 방식

```kotlin
val passwordState = rememberTextFieldState()
var passwordHidden by rememberSaveable { mutableStateOf(true) }

SecureTextField(
    state = passwordState,
    label = { Text("비밀번호") },
    textObfuscationMode = if (passwordHidden)
        TextObfuscationMode.RevealLastTyped
    else
        TextObfuscationMode.Visible,
    trailingIcon = {
        IconButton(onClick = { passwordHidden = !passwordHidden }) {
            Icon(
                imageVector = if (passwordHidden)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff,
                contentDescription = if (passwordHidden)
                    "비밀번호 보기"
                else
                    "비밀번호 숨기기"
            )
        }
    }
)
```

### 해결되는 이유

1. **간결한 API**: 핵심 기능이 몇 줄로 구현됨
2. **보안 기능 자동 내장**: 복사/잘라내기 자동 차단
3. **TextFieldState 기반**: 안정적인 상태 관리, 화면 회전에도 유지
4. **TextObfuscationMode**: 표시/숨김 전환이 간단

---

## 사용 시나리오

### 1. 로그인 화면

가장 일반적인 사용 사례입니다.

```kotlin
SecureTextField(
    state = rememberTextFieldState(),
    label = { Text("비밀번호") }
)
```

### 2. 회원가입 - 비밀번호 확인

비밀번호와 비밀번호 확인 필드를 함께 사용합니다.

```kotlin
val passwordState = rememberTextFieldState()
val confirmState = rememberTextFieldState()

// 비밀번호 일치 여부 확인
val passwordsMatch by remember {
    derivedStateOf {
        passwordState.text.toString() == confirmState.text.toString()
    }
}
```

### 3. PIN 입력

숫자만 입력받는 보안 필드입니다.

```kotlin
SecureTextField(
    state = rememberTextFieldState(),
    label = { Text("PIN 코드") },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
)
```

---

## API 레퍼런스

### SecureTextField

```kotlin
@Composable
fun SecureTextField(
    state: TextFieldState,                                        // 필수
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    textObfuscationCharacter: Char = '\u2022',                    // 기본: ●
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    // ... 기타 TextField 공통 파라미터
)
```

### TextObfuscationMode

| 값 | 설명 |
|----|------|
| `Hidden` | 모든 문자를 마스킹 문자로 대체 |
| `Visible` | 모든 문자를 그대로 표시 |
| `RevealLastTyped` | 마지막 입력 문자만 잠시 표시 후 마스킹 (기본값) |

---

## 주의사항

1. **버전 요구사항**
   - Material3 1.4.0-alpha14 이상
   - Compose BOM 2024.11.00+

2. **단일 줄 입력**
   - SecureTextField는 항상 단일 줄로 동작합니다
   - 여러 줄 비밀번호 입력은 불가능합니다

3. **접근성**
   - trailingIcon의 contentDescription을 반드시 설정하세요
   - 시각 장애인 사용자를 위해 "비밀번호 보기/숨기기" 명시

---

## 연습 문제

### 연습 1: 기본 SecureTextField 구현 (쉬움)

`rememberTextFieldState()`를 사용하여 기본 SecureTextField를 구현하세요.
라벨은 '비밀번호'로 설정합니다.

### 연습 2: 비밀번호 표시/숨김 토글 (중간)

trailingIcon에 토글 아이콘을 추가하여 비밀번호 표시/숨김 기능을 구현하세요.
- 숨김 상태: Visibility 아이콘 (눈 모양)
- 표시 상태: VisibilityOff 아이콘 (눈에 선 그어진 모양)

### 연습 3: 완전한 로그인 폼 구현 (어려움)

다음 조건을 만족하는 로그인 폼을 구현하세요:
1. 이메일 입력 필드 (TextField)
2. 비밀번호 입력 필드 (SecureTextField + 토글)
3. 유효성 검사:
   - 이메일: '@' 포함 필수
   - 비밀번호: 8자 이상
4. 두 조건 모두 만족 시에만 '로그인' 버튼 활성화

---

## 다음 학습

- [TextFieldState](../textfield_state/README.md): 새로운 TextField 상태 관리 API
- [폼 유효성 검사](../../form_validation/README.md): 입력값 검증 패턴
- [Autofill](../../autofill/README.md): 자동 완성 기능

---

## 참고 자료

- [Android Developers - SecureTextField](https://developer.android.com/develop/ui/compose/text/user-input)
- [Material3 SecureTextField](https://composables.com/material3/securetextfield)
- [Show or Hide Password Guide](https://developer.android.com/develop/ui/compose/quick-guides/content/show-hide-password)
