# Compose UI Testing 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `semantics_accessibility` | Semantics와 접근성 기초 | [📚 학습하기](../../testing/semantics_accessibility/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Compose UI Testing**은 Jetpack Compose로 작성된 UI를 자동화된 방식으로 검증하는 테스트 방법입니다.

핵심 특징:
- **Semantics 기반**: View ID 대신 **의미론적 정보**를 사용해 UI 요소를 찾습니다
- **접근성과 동일한 정보 사용**: 테스트와 접근성이 같은 Semantics Tree를 공유합니다
- **선언적 테스트**: Compose UI처럼 테스트도 선언적으로 작성합니다

```kotlin
// 기본 테스트 구조
@Test
fun myTest() {
    composeTestRule.setContent {
        MyScreen()
    }

    composeTestRule
        .onNodeWithText("Submit")   // Finder: 요소 찾기
        .performClick()              // Action: 동작 수행

    composeTestRule
        .onNodeWithText("Success")
        .assertIsDisplayed()         // Assertion: 검증
}
```

---

## 문제 상황: Semantics가 없는 UI

### 시나리오

로그인 화면을 개발했습니다. 이제 UI 테스트를 작성해야 하는데, 어떻게 테스트해야 할지 막막합니다.

### 잘못된 코드 예시

```kotlin
// 테스트하기 어려운 로그인 폼
@Composable
fun LoginScreen() {
    Column {
        Icon(Icons.Default.Person, contentDescription = null)  // 설명 없음
        TextField(value = email, onValueChange = {})           // 구분 불가
        TextField(value = password, onValueChange = {})        // 구분 불가
        Button(onClick = {}) { Text("로그인") }                // 다국어 시 깨짐
    }
}
```

### 발생하는 문제점

1. **Icon에 contentDescription이 없음**
   - 접근성 서비스가 이 아이콘을 인식하지 못합니다
   - 테스트에서도 이 요소를 찾을 수 없습니다

2. **두 TextField를 구분할 방법이 없음**
   - testTag나 label이 없어서 어느 것이 이메일 필드인지 알 수 없습니다
   - 테스트에서 "이메일 입력"과 "비밀번호 입력"을 구분할 수 없습니다

3. **Button이 텍스트에 의존**
   - "로그인" 텍스트로만 버튼을 찾습니다
   - 다국어 지원 시 영어("Login")로 바뀌면 테스트가 모두 실패합니다

### 테스트 코드 (실패 예상)

```kotlin
@Test
fun loginTest() {
    composeTestRule.setContent {
        LoginScreen()
    }

    // 어떤 TextField가 이메일인지 알 수 없음!
    composeTestRule
        .onNodeWithText("이메일")  // placeholder로 찾기?
        .performTextInput("test@email.com")
        // placeholder는 입력 후 사라짐!

    // 다국어 시 깨짐
    composeTestRule
        .onNodeWithText("로그인")  // 영어면?
        .performClick()
}
```

---

## 해결책: Semantics 추가

### Semantics Tree 이해

Compose는 UI 트리와 별도로 **Semantics Tree**를 유지합니다. 이 트리는 접근성 서비스와 UI 테스트가 모두 사용합니다.

```
┌─────────────────────────────────────────────────┐
│              Composition Tree                    │
│   (실제 UI 렌더링)                                │
└─────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────┐
│              Semantics Tree                      │
│   - 접근성 서비스 사용                            │
│   - UI 테스트 사용                               │
│   - 자동완성 사용                                │
└─────────────────────────────────────────────────┘
```

### Merged vs Unmerged Tree

```kotlin
// Button 내부 요소는 병합됨
Button(onClick = {}) {
    Icon(Icons.Default.Send, contentDescription = null)
    Text("Send")
}

// Merged Tree: 단일 노드 "Send"
// Unmerged Tree: Button → Icon + Text 개별 노드

// 병합된 트리 (기본값)
onNodeWithText("Send").performClick() // OK

// 병합되지 않은 트리 접근
onNodeWithText("Send", useUnmergedTree = true)
```

### 올바른 코드

```kotlin
// 테스트 가능한 로그인 폼
@Composable
fun LoginScreen() {
    Column {
        Icon(
            Icons.Default.Person,
            contentDescription = "User avatar"  // 접근성 + 테스트
        )
        TextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.testTag("email_field")  // testTag로 구분
        )
        TextField(
            value = password,
            onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier.testTag("password_field")
        )
        Button(
            onClick = {},
            modifier = Modifier.testTag("login_button")
        ) {
            Text("로그인")
        }
    }
}
```

### 해결되는 이유

1. **contentDescription**: 접근성 서비스가 "User avatar"라고 읽어주고, 테스트에서 `onNodeWithContentDescription("User avatar")`로 찾을 수 있습니다.

2. **testTag**: UI 텍스트와 무관한 고유 식별자입니다. 다국어 지원에도 테스트가 깨지지 않습니다.

3. **label**: TextField에 의미 있는 레이블을 제공합니다. 접근성과 UX 모두 개선됩니다.

---

## Testing API 활용

### 테스트 패턴: Finder -> Action -> Assertion

```kotlin
composeTestRule
    .onNodeWithTag("login_button")  // Finder: 요소 찾기
    .performClick()                  // Action: 동작 수행
    .assertExists()                  // Assertion: 검증 (체이닝 가능)
```

### Finders (요소 찾기)

| API | 설명 | 예시 |
|-----|------|------|
| `onNodeWithText()` | 텍스트로 찾기 | `onNodeWithText("Submit")` |
| `onNodeWithContentDescription()` | 설명으로 찾기 | `onNodeWithContentDescription("Add")` |
| `onNodeWithTag()` | testTag로 찾기 | `onNodeWithTag("login_btn")` |
| `onAllNodesWithTag()` | 여러 노드 | `onAllNodesWithTag("item")` |

### Assertions (검증)

| API | 설명 |
|-----|------|
| `assertExists()` | 노드 존재 확인 |
| `assertDoesNotExist()` | 노드 없음 확인 |
| `assertIsDisplayed()` | 화면에 표시됨 |
| `assertIsEnabled()` | 활성화 상태 |
| `assertTextEquals()` | 텍스트 일치 |
| `assertTextContains()` | 텍스트 포함 |
| `assertCountEquals()` | 노드 개수 확인 |

### Actions (동작)

| API | 설명 |
|-----|------|
| `performClick()` | 클릭 |
| `performTextInput()` | 텍스트 입력 |
| `performScrollTo()` | 스크롤 |
| `performTouchInput { swipeLeft() }` | 제스처 |

---

## 사용 시나리오

### 1. 버튼 클릭 테스트

```kotlin
@Test
fun incrementButton_increasesCount() {
    composeTestRule.setContent { CounterScreen() }

    composeTestRule.onNodeWithTag("increment_btn").performClick()
    composeTestRule.onNodeWithText("Count: 1").assertIsDisplayed()
}
```

### 2. 입력 테스트

```kotlin
@Test
fun emailField_acceptsInput() {
    composeTestRule.setContent { LoginScreen() }

    composeTestRule
        .onNodeWithTag("email_field")
        .performTextInput("test@example.com")

    composeTestRule
        .onNodeWithTag("email_field")
        .assertTextContains("test@example.com")
}
```

### 3. 리스트 테스트

```kotlin
@Test
fun todoList_showsAllItems() {
    composeTestRule.setContent { TodoScreen() }

    composeTestRule
        .onAllNodesWithTag("todo_item")
        .assertCountEquals(3)
}
```

### 4. 비동기 작업 대기

```kotlin
@Test
fun dataLoading_showsContent() {
    composeTestRule.setContent { DataScreen() }

    // 비동기 작업 완료 대기
    composeTestRule.waitUntil(timeoutMillis = 5000) {
        composeTestRule
            .onAllNodesWithTag("loaded_item")
            .fetchSemanticsNodes().isNotEmpty()
    }

    composeTestRule
        .onNodeWithTag("loaded_item")
        .assertIsDisplayed()
}
```

---

## 주의사항

### 1. testTag는 최후의 수단

```kotlin
// 좋음: contentDescription 우선
Icon(Icons.Default.Add, contentDescription = "Add item")

// testTag는 구분이 필요할 때만
LazyColumn {
    items(users) { user ->
        UserCard(modifier = Modifier.testTag("user_${user.id}"))
    }
}
```

### 2. Semantics 우선순위

```
1. contentDescription (접근성 + 테스트)
2. text (자연스러운 매칭)
3. testTag (필요한 경우만)
```

### 3. UiAutomator 연동

testTag를 UiAutomator에서 사용하려면 추가 설정이 필요합니다:

```kotlin
Modifier.semantics {
    testTagsAsResourceId = true
}
```

---

## 설정

### build.gradle.kts

```kotlin
dependencies {
    // 테스트 규칙
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // 테스트 매니페스트 (디버그 빌드용)
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

---

## 연습 문제

### 연습 1: Finder 기초 (Practice1_FinderBasics)

**목표**: 텍스트와 버튼 찾기, assertIsDisplayed() 사용

- "Hello, Compose!" 텍스트가 표시되는지 확인
- "Toggle" 버튼이 존재하는지 확인
- 버튼 클릭 후 텍스트가 사라지는지 확인

**힌트**:
```kotlin
onNodeWithText("Hello, Compose!").assertIsDisplayed()
onNodeWithTag("toggle_button").assertExists()
onNodeWithText("Hello, Compose!").assertDoesNotExist()
```

### 연습 2: Actions과 상태 변화 (Practice2_ActionsAndState)

**목표**: 버튼 클릭으로 카운터 증가 테스트

- 초기 상태 "Count: 0" 확인
- + 버튼 클릭 후 "Count: 1" 확인
- - 버튼으로 감소 테스트
- Reset 버튼 동작 확인

**힌트**:
```kotlin
onNodeWithTag("increment_button").performClick()
onNodeWithTag("count_display").assertTextEquals("Count: 1")
repeat(3) { onNodeWithTag("increment_button").performClick() }
```

### 연습 3: 리스트와 입력 (Practice3_ListAndInput)

**목표**: Todo 아이템 추가/삭제 테스트

- 초기 Todo 개수 확인 (2개)
- 새 Todo 입력 및 추가
- 추가 후 개수 확인 (3개)
- 삭제 기능 테스트

**힌트**:
```kotlin
onNodeWithTag("todo_input").performTextInput("New Task")
onNodeWithTag("add_button").performClick()
onAllNodesWithTag("todo_item").assertCountEquals(3)
```

---

## 다음 학습

- **screenshot_testing**: 스크린샷 기반 UI 테스트
- **semantics_accessibility**: Semantics와 접근성 심화
