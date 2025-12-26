# Compose UI Testing 학습

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

## 핵심 특징

### 1. Semantics Tree

Compose는 UI 트리와 별도로 **Semantics Tree**를 유지합니다:

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

### 2. Merged vs Unmerged Tree

```kotlin
// Button 내부 요소는 병합됨
Button(onClick = {}) {
    Icon(Icons.Default.Send, contentDescription = null)
    Text("Send")
}

// Merged Tree: 단일 노드 "Send"
// Unmerged Tree: Button → Icon + Text 개별 노드

// 병합된 트리 (기본값)
onNodeWithText("Send").performClick() ✅

// 병합되지 않은 트리 접근
onNodeWithText("Send", useUnmergedTree = true)
```

### 3. 테스트 패턴: Finder → Action → Assertion

```kotlin
composeTestRule
    .onNodeWithTag("login_button")  // Finder
    .performClick()                  // Action
    .assertExists()                  // Assertion (체이닝 가능)
```

## 문제 상황: Semantics 없는 UI

Semantics 정보가 없으면 테스트가 어려워집니다:

```kotlin
// ❌ 문제: 어떻게 테스트할까?
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

**발생하는 문제**:
1. Icon에 contentDescription이 없어 접근성 도구/테스트가 인식 못함
2. 두 TextField를 구분할 방법 없음
3. 텍스트 기반 테스트는 다국어 시 모두 깨짐

## 해결책: Semantics 추가

```kotlin
// ✅ 해결: 테스트 가능한 UI
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

## 주요 Testing API

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
| `assertCountEquals()` | 노드 개수 확인 |

### Actions (동작)

| API | 설명 |
|-----|------|
| `performClick()` | 클릭 |
| `performTextInput()` | 텍스트 입력 |
| `performScrollTo()` | 스크롤 |
| `performTouchInput { swipeLeft() }` | 제스처 |

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

## 주의사항

### 1. testTag는 최후의 수단
```kotlin
// ✅ 좋음: contentDescription 우선
Icon(Icons.Default.Add, contentDescription = "Add item")

// ⚠️ testTag는 구분이 필요할 때만
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

### 3. 동기화 주의
```kotlin
// 비동기 작업 대기
composeTestRule.waitUntil(timeoutMillis = 5000) {
    composeTestRule
        .onAllNodesWithTag("loaded_item")
        .fetchSemanticsNodes().isNotEmpty()
}
```

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

## 연습 문제

1. **기초**: 텍스트와 버튼 찾기, assertIsDisplayed() 사용
2. **중급**: 버튼 클릭 후 상태 변화 검증
3. **고급**: 리스트 아이템 개수 확인, 텍스트 입력 테스트

## 다음 학습

- **interoperability**: View-Compose 상호 운용
- **custom_layout**: 커스텀 레이아웃 만들기
