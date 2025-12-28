# Autofill 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `TextField` | Compose의 기본 텍스트 입력 컴포넌트 | [📚 학습하기](../textfield/README.md) |
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**Autofill(자동완성)**은 시스템이 저장된 사용자 정보(이메일, 비밀번호, 주소 등)를 TextField에 자동으로 채워주는 기능입니다. 2025년 4월 Compose 1.8에서 네이티브 지원이 추가되어, 이제 간단한 설정만으로 시스템 자동완성 서비스와 통합할 수 있습니다.

> **비유**: Autofill은 **비서**와 같습니다.
> - 당신이 어떤 정보가 필요한지 미리 알려주면 (ContentType 설정)
> - 비서가 저장해둔 정보를 자동으로 채워줍니다 (시스템 자동완성)
> - 새로운 정보를 입력하면 비서에게 저장해달라고 요청합니다 (commit)

### 왜 Autofill이 중요한가?

1. **사용자 편의성**: 긴 이메일이나 복잡한 비밀번호를 매번 입력할 필요 없음
2. **접근성 향상**: WCAG Success Criterion 1.3.5 (Identify Input Purpose) 충족
3. **보안 강화**: 비밀번호 관리자와 통합하여 강력한 비밀번호 사용 장려
4. **전환율 향상**: 회원가입/로그인 과정 간소화

---

## 기본 사용법

TextField에 `contentType`을 설정하면 시스템 자동완성이 활성화됩니다.

```kotlin
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.autofill.ContentType

TextField(
    value = username,
    onValueChange = { username = it },
    label = { Text("사용자 이름") },
    modifier = Modifier.semantics {
        contentType = ContentType.Username
    }
)
```

**동작 원리**:
1. 사용자가 TextField를 탭하면
2. 시스템이 저장된 사용자 이름 목록을 표시
3. 사용자가 선택하면 자동으로 입력됨

---

## 핵심 기능

### 기능 1: ContentType 종류

| ContentType | 용도 | 설명 |
|-------------|------|------|
| `Username` | 로그인 | 기존 계정 사용자 이름 |
| `Password` | 로그인 | 기존 계정 비밀번호 |
| `NewUsername` | 회원가입 | 새 계정 사용자 이름 |
| `NewPassword` | 회원가입 | 새 계정 비밀번호 (강력한 비밀번호 제안) |
| `EmailAddress` | 이메일 입력 | 이메일 주소 |
| `PhoneNumber` | 전화번호 입력 | 전화번호 |
| `PostalAddress` | 주소 입력 | 전체 주소 |
| `AddressStreet` | 주소 입력 | 도로명 주소 |
| `AddressCity` | 주소 입력 | 시/군/구 |
| `CreditCardNumber` | 결제 | 카드 번호 |

### 기능 2: State-based TextField 지원

최신 TextField API (rememberTextFieldState)에서도 동일하게 사용 가능합니다.

```kotlin
TextField(
    state = rememberTextFieldState(),
    label = { Text("이메일") },
    modifier = Modifier.semantics {
        contentType = ContentType.EmailAddress
    }
)
```

### 기능 3: LocalAutofillManager

시스템 자동완성 서비스와 상호작용하는 매니저입니다.

```kotlin
val autofillManager = LocalAutofillManager.current

// 자격 증명 저장 요청
autofillManager?.commit()

// 자동완성 취소
autofillManager?.cancel()
```

---

## 고급 활용

### 패턴 1: 다중 ContentType 조합

이메일 또는 전화번호로 로그인을 허용하는 경우:

```kotlin
TextField(
    value = loginId,
    onValueChange = { loginId = it },
    label = { Text("이메일 또는 전화번호") },
    modifier = Modifier.semantics {
        contentType = ContentType.EmailAddress + ContentType.PhoneNumber
    }
)
```

### 패턴 2: 자격 증명 저장

회원가입 완료 시 새 자격 증명을 저장하도록 시스템에 요청:

```kotlin
@Composable
fun SignUpForm() {
    val autofillManager = LocalAutofillManager.current

    Column {
        // NewUsername/NewPassword 사용 (새 계정임을 명시)
        TextField(
            state = rememberTextFieldState(),
            modifier = Modifier.semantics {
                contentType = ContentType.NewUsername
            }
        )

        TextField(
            state = rememberTextFieldState(),
            modifier = Modifier.semantics {
                contentType = ContentType.NewPassword
            }
        )

        Button(onClick = {
            autofillManager?.commit()  // 저장 다이얼로그 표시
        }) {
            Text("회원가입")
        }
    }
}
```

### 패턴 3: 하이라이트 색상 커스터마이징

자동완성 필드의 강조 색상을 브랜드에 맞게 변경:

```kotlin
CompositionLocalProvider(
    LocalAutofillHighlightColor provides Color.Cyan.copy(alpha = 0.3f)
) {
    TextField(
        state = rememberTextFieldState(),
        modifier = Modifier.semantics {
            contentType = ContentType.Username
        }
    )
}
```

기본 하이라이트 색상: `Color(0x4dffeb3b)` (노란색)

---

## 베스트 프랙티스

1. **적절한 ContentType 선택**
   - 로그인: `Username`, `Password`
   - 회원가입: `NewUsername`, `NewPassword`
   - 비밀번호 변경: `NewPassword`

2. **KeyboardType과 함께 사용**
   ```kotlin
   TextField(
       modifier = Modifier.semantics {
           contentType = ContentType.EmailAddress
       },
       keyboardOptions = KeyboardOptions(
           keyboardType = KeyboardType.Email
       )
   )
   ```

3. **폼 제출 시 commit() 호출**
   - 단일 Activity 앱에서는 명시적 commit 필요
   - Navigation 사용 시 자동 commit됨

4. **VisualTransformation과 함께 사용**
   ```kotlin
   TextField(
       visualTransformation = PasswordVisualTransformation(),
       modifier = Modifier.semantics {
           contentType = ContentType.Password
       }
   )
   ```

---

## 안티패턴

### 1. 잘못된 ContentType 사용

```kotlin
// 잘못된 예: 비밀번호 필드에 Username 사용
TextField(
    visualTransformation = PasswordVisualTransformation(),
    modifier = Modifier.semantics {
        contentType = ContentType.Username  // 비밀번호가 아님!
    }
)

// 올바른 예
TextField(
    visualTransformation = PasswordVisualTransformation(),
    modifier = Modifier.semantics {
        contentType = ContentType.Password
    }
)
```

### 2. commit() 누락

```kotlin
// 잘못된 예: 회원가입 후 commit 없음
Button(onClick = {
    // 저장 요청 없음 - 자격 증명이 저장되지 않음
    navigateToHome()
}) {
    Text("회원가입")
}

// 올바른 예
Button(onClick = {
    autofillManager?.commit()  // 저장 요청
    navigateToHome()
}) {
    Text("회원가입")
}
```

---

## 주의사항

### 기기 설정 필요

Autofill을 사용하려면 기기에서 자동완성이 활성화되어 있어야 합니다:

1. **설정** > **앱** > **자동완성 앱** 이동
2. 자격 증명 저장소 선택 (예: Google Password Manager)

### 의존성

Compose BOM 2025.04.01 이상이 필요합니다:

```kotlin
implementation(platform("androidx.compose:compose-bom:2025.04.01"))
```

---

## 연습 문제

### 연습 1: 기본 로그인 폼 (쉬움)

사용자 이름과 비밀번호 필드에 적절한 ContentType을 설정하세요.

### 연습 2: 유연한 로그인 필드 (중간)

이메일 또는 전화번호로 로그인할 수 있는 필드를 만드세요.

### 연습 3: 회원가입 폼 + 자동 저장 (어려움)

이메일, 비밀번호, 전화번호를 입력받고, 회원가입 완료 시 자격 증명을 저장하는 폼을 구현하세요.

---

## 다음 학습

- [TextField State](/study/component/input/textfield_state/) - 최신 TextField API
- [Secure TextField](/study/component/input/secure_textfield/) - 보안 입력 필드
- [Focus Management](/study/interaction/focus_management/) - 포커스 관리
