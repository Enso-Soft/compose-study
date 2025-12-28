# Focus Management 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `gesture` | 터치 입력 처리와 Gesture Modifier | [📚 학습하기](../../interaction/gesture/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Focus Management**는 사용자가 키보드, 게임 컨트롤러, D-Pad 등 비터치 입력 방식으로 앱과 상호작용할 때 **어떤 UI 요소가 현재 활성화되어 있는지** 관리하는 메커니즘입니다.

### 왜 Focus Management가 중요한가?

```
사용자가 TextField를 터치하면...
    → 포커스가 해당 필드로 이동
    → 키보드가 자동으로 열림
    → 입력 가능 상태가 됨

하지만 Focus Management 없이는...
    → 화면 진입 시 자동 포커스 불가
    → 키보드 "다음" 버튼으로 필드 이동 불가
    → 폼 제출 후 키보드가 열린 상태로 유지
```

### 실제 사용 예시

| 시나리오 | Focus Management 활용 |
|---------|---------------------|
| 로그인 화면 | 화면 진입 시 이메일 필드 자동 포커스 |
| 검색 화면 | 화면 열리면 검색창에 바로 입력 가능 |
| 회원가입 폼 | "다음" 버튼으로 필드 간 이동 |
| 폼 제출 후 | 키보드 숨기고 포커스 해제 |
| TV/Wear OS | D-Pad/Rotary Bezel로 네비게이션 |

---

## 핵심 API

### 1. FocusRequester

**역할**: 특정 Composable에 프로그래밍 방식으로 포커스를 요청

```kotlin
// 1. FocusRequester 생성
val focusRequester = remember { FocusRequester() }

// 2. Composable에 연결
TextField(
    modifier = Modifier.focusRequester(focusRequester)
)

// 3. 포커스 요청
focusRequester.requestFocus()
```

**주요 메서드**:
- `requestFocus()`: 포커스 요청
- `captureFocus()`: 포커스 잠금 (다른 요소로 이동 불가)
- `freeFocus()`: 포커스 잠금 해제

**여러 FocusRequester 한번에 생성**:
```kotlin
val (first, second, third) = remember { FocusRequester.createRefs() }
```

### 2. FocusManager

**역할**: 현재 화면의 포커스 상태를 관리

```kotlin
val focusManager = LocalFocusManager.current

// 방향별 포커스 이동
focusManager.moveFocus(FocusDirection.Down)
focusManager.moveFocus(FocusDirection.Next)
focusManager.moveFocus(FocusDirection.Previous)

// 포커스 해제 (키보드도 숨겨짐)
focusManager.clearFocus()
```

**FocusDirection 종류**:
- `Next` / `Previous`: 순차적 이동 (TAB 키 동작)
- `Up` / `Down` / `Left` / `Right`: 방향별 이동 (화살표 키 동작)
- `Enter` / `Exit`: 포커스 그룹 진입/이탈

### 3. focusProperties Modifier

**역할**: 포커스 동작을 세밀하게 커스터마이징

```kotlin
TextField(
    modifier = Modifier
        .focusRequester(emailFocusRequester)
        .focusProperties {
            // 다음/이전 포커스 대상 지정
            next = passwordFocusRequester
            previous = nameFocusRequester

            // 방향별 포커스 대상 지정
            up = FocusRequester.Cancel  // 위로 이동 차단
            down = submitButtonFocusRequester

            // 포커스 가능 여부
            canFocus = true
        }
)
```

**주요 속성**:
- `canFocus`: 포커스 가능 여부 (false면 포커스 불가)
- `next` / `previous`: 순차 이동 대상
- `up` / `down` / `left` / `right`: 방향별 이동 대상
- `enter` / `exit`: 그룹 진입/이탈 시 동작

**특수 값**:
- `FocusRequester.Cancel`: 해당 방향 이동 차단
- `FocusRequester.Default`: 기본 동작 사용

### 4. onFocusChanged Modifier

**역할**: 포커스 상태 변화 감지

```kotlin
var isFocused by remember { mutableStateOf(false) }

TextField(
    modifier = Modifier.onFocusChanged { focusState ->
        isFocused = focusState.isFocused

        // 다른 상태 정보
        // focusState.hasFocus - 자신 또는 자식이 포커스
        // focusState.isCaptured - 포커스가 잠겨있는지
    }
)
```

**FocusState 속성**:
- `isFocused`: 직접 포커스 상태
- `hasFocus`: 자신 또는 자식이 포커스 상태
- `isCaptured`: captureFocus()로 잠긴 상태

### 5. IME Actions (KeyboardActions)

**역할**: 소프트 키보드의 액션 버튼 처리

```kotlin
TextField(
    keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next  // "다음" 버튼 표시
    ),
    keyboardActions = KeyboardActions(
        onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }
    )
)
```

**ImeAction 종류**:
| ImeAction | 키보드 버튼 | 용도 |
|-----------|-----------|------|
| `Done` | 완료 | 입력 완료, 폼 제출 |
| `Next` | 다음 | 다음 필드로 이동 |
| `Previous` | 이전 | 이전 필드로 이동 |
| `Search` | 검색 | 검색 실행 |
| `Go` | 이동 | URL 이동 등 |
| `Send` | 전송 | 메시지 전송 등 |

**KeyboardActions 콜백**:
- `onDone`, `onNext`, `onPrevious`, `onSearch`, `onGo`, `onSend`

### 6. SoftwareKeyboardController

**역할**: 키보드 표시/숨김 제어

```kotlin
val keyboardController = LocalSoftwareKeyboardController.current

// 키보드 숨기기
keyboardController?.hide()

// 키보드 표시
keyboardController?.show()
```

**FocusManager.clearFocus()와의 차이**:
- `clearFocus()`: 포커스 해제 + 키보드 숨김
- `keyboardController?.hide()`: 키보드만 숨김 (포커스 유지)

---

## 문제 상황: Focus Management 없이 구현된 로그인 폼

### 잘못된 코드

```kotlin
@Composable
fun BadLoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        // 문제 1: 자동 포커스 없음
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            // 문제 2: imeAction 없음
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
            // 문제 3: keyboardActions 없음
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        Button(onClick = {
            // 문제 4: 키보드 숨기기 없음
            // 문제 5: 포커스 해제 없음
            performLogin()
        }) {
            Text("로그인")
        }
    }
}
```

### 발생하는 문제점

1. **화면 진입 시 자동 포커스 없음**: 사용자가 직접 이메일 필드를 터치해야 함
2. **키보드 "다음" 버튼 미동작**: 누르면 아무 일도 안 일어남
3. **키보드 "완료" 버튼 미동작**: 로그인 실행 안 됨
4. **로그인 후 키보드 열림 유지**: 수동으로 키보드를 닫아야 함
5. **포커스 상태 피드백 없음**: 어떤 필드가 활성화인지 구분 어려움

---

## 해결책: Focus Management 완벽 적용

### 올바른 코드

```kotlin
@Composable
fun GoodLoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 1. FocusRequester 생성
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    // 2. FocusManager와 KeyboardController
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // 3. 화면 진입 시 자동 포커스
    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    // 4. 로그인 처리
    val performLogin: () -> Unit = {
        keyboardController?.hide()
        focusManager.clearFocus()
        // 로그인 로직...
    }

    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier
                // 5. FocusRequester 연결
                .focusRequester(emailFocusRequester)
                // 6. 다음 포커스 지정
                .focusProperties { next = passwordFocusRequester },
            // 7. IME Action 설정
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            // 8. IME Action 핸들러
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            modifier = Modifier
                .focusRequester(passwordFocusRequester)
                .focusProperties { previous = emailFocusRequester },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { performLogin() }
            )
        )

        Button(onClick = performLogin) {
            Text("로그인")
        }
    }
}
```

### 해결되는 점

1. **자동 포커스**: `LaunchedEffect(Unit)`로 화면 진입 시 이메일 필드에 자동 포커스
2. **"다음" 버튼 동작**: `ImeAction.Next` + `onNext` 콜백으로 비밀번호 필드로 이동
3. **"완료" 버튼 동작**: `ImeAction.Done` + `onDone` 콜백으로 로그인 실행
4. **키보드/포커스 정리**: `keyboardController?.hide()` + `focusManager.clearFocus()`
5. **명시적 포커스 순서**: `focusProperties`로 next/previous 지정

---

## 포커스 상태에 따른 스타일링

```kotlin
var isFocused by remember { mutableStateOf(false) }

// 포커스 상태에 따른 색상 애니메이션
val borderColor by animateColorAsState(
    targetValue = if (isFocused)
        MaterialTheme.colorScheme.primary
    else
        Color.Transparent
)

OutlinedTextField(
    modifier = Modifier
        .onFocusChanged { isFocused = it.isFocused }
        .border(
            width = 2.dp,
            color = borderColor,
            shape = RoundedCornerShape(4.dp)
        )
)
```

---

## 외부 터치 시 포커스 해제

```kotlin
Box(
    modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }
) {
    // 컨텐츠...
}
```

---

## 주의사항

### 1. Modifier 순서 중요

```kotlin
// 올바른 순서: focusRequester → focusProperties → onFocusChanged → focusable
Modifier
    .focusRequester(focusRequester)
    .focusProperties { next = other }
    .onFocusChanged { ... }
    .focusable()  // 또는 TextField 등 focusable 컴포넌트
```

### 2. 부모 focusProperties가 자식보다 우선

```kotlin
// 부모가 canFocus = false로 설정하면 자식도 포커스 불가
Box(modifier = Modifier.focusProperties { canFocus = false }) {
    TextField(...)  // 포커스 불가!
}
```

### 3. requestFocus()는 Composable 컨텍스트 외부에서 호출

```kotlin
// 올바름: LaunchedEffect 내에서 호출
LaunchedEffect(Unit) {
    focusRequester.requestFocus()
}

// 잘못됨: Composable 본문에서 직접 호출
@Composable
fun MyScreen() {
    focusRequester.requestFocus()  // 매 Recomposition마다 호출됨!
}
```

### 4. captureFocus()는 이미 포커스된 상태에서만 동작

```kotlin
// captureFocus는 해당 요소가 이미 포커스를 갖고 있어야 함
if (someCondition) {
    focusRequester.captureFocus()  // 포커스가 없으면 실패
}
```

### 5. FocusRequester는 접근성(Accessibility) 포커스에 동작하지 않음

```kotlin
// 주의: FocusRequester는 키보드 포커스용
// 스크린 리더(TalkBack)의 접근성 포커스는 별도 처리 필요

// 접근성 포커스 이동에는 semantics + LiveRegion 또는
// View.sendAccessibilityEvent 등의 대안 필요
```

> **참고**: 키보드/D-Pad 포커스와 접근성 포커스는 별개의 시스템입니다.
> TalkBack 등 스크린 리더를 위한 포커스 관리는 `semantics` 모디파이어를 참고하세요.

---

## 연습 문제

### 연습 1: 검색창 자동 포커스 (난이도: 쉬움)
- FocusRequester 생성
- LaunchedEffect로 초기 포커스 요청
- TextField에 focusRequester 연결

### 연습 2: 회원가입 폼 네비게이션 (난이도: 중간)
- ImeAction.Next/Done 설정
- KeyboardActions 콜백 처리
- FocusManager.moveFocus() 사용

### 연습 3: 설정 화면 포커스 관리 (난이도: 어려움)
- FocusRequester.createRefs() 사용
- focusProperties로 커스텀 순서 지정
- onFocusChanged로 스타일 변경
- canFocus = false로 포커스 불가 설정

---

## 다음 학습

- **Focus Group**: `focusGroup()` 모디파이어로 포커스 그룹 구성
- **Hardware Keyboard 이벤트**: `onKeyEvent`, `onPreviewKeyEvent`
- **Accessibility Traversal**: `semantics`로 접근성 순서 제어

---

## 참고 자료

- [Focus in Compose - Android Developers](https://developer.android.com/develop/ui/compose/touch-input/focus)
- [Change focus behavior - Android Developers](https://developer.android.com/develop/ui/compose/touch-input/focus/change-focus-behavior)
- [React to focus - Android Developers](https://developer.android.com/develop/ui/compose/touch-input/focus/react-to-focus)
- [Handle keyboard actions - Android Developers](https://developer.android.com/develop/ui/compose/touch-input/keyboard-input/commands)
