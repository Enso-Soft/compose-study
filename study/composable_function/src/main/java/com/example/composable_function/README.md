# @Composable 함수 학습

## 개념

`@Composable` 함수는 Jetpack Compose의 **핵심 빌딩 블록**입니다. UI를 선언하는 특별한 함수로, 일반 함수와는 다른 규칙을 따릅니다.

```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hello, $name!")
}
```

---

## 핵심 특징

### 1. @Composable 어노테이션

```kotlin
@Composable  // 이 어노테이션이 필수!
fun MyComponent() {
    Text("Hello")
}
```

**역할:**
- 컴파일러에게 "이 함수는 UI 트리의 노드가 됩니다"라고 알림
- Composer라는 숨겨진 파라미터가 자동 주입됨
- @Composable 컨텍스트에서만 호출 가능

### 2. 네이밍 컨벤션

```kotlin
// ✅ 올바른 네이밍 (PascalCase)
@Composable
fun UserProfile() { ... }

@Composable
fun MessageCard() { ... }

// ❌ 잘못된 네이밍 (camelCase)
@Composable
fun userProfile() { ... }
```

**규칙:**
- **PascalCase** 사용 (첫 글자 대문자)
- 명사 또는 명사구 사용
- 동작이 아닌 "무엇"을 나타내는지 표현

### 3. 반환 타입

```kotlin
@Composable
fun Greeting(): Unit {  // Unit 반환 (생략 가능)
    Text("Hello")
}

// 잘못된 생각: View를 반환한다?
// Composable은 View 객체를 반환하지 않습니다!
```

**특징:**
- 대부분 `Unit` 반환 (암시적)
- UI를 "반환"하지 않고 "선언"함
- 함수 호출 = UI 트리에 노드 추가

---

## Composable 라이프사이클

```
┌─────────────────────────────────────────┐
│           Composable 라이프사이클          │
├─────────────────────────────────────────┤
│                                         │
│   1. Initial Composition (최초 생성)     │
│          ↓                              │
│   2. Recomposition (0회 이상)            │
│      - State 변경 시 자동 실행            │
│      - 변경된 부분만 업데이트              │
│          ↓                              │
│   3. Leave Composition (제거)           │
│      - 화면에서 사라질 때                 │
│                                         │
└─────────────────────────────────────────┘
```

### Recomposition이란?

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: $count")  // count 변경 시 이 부분만 업데이트!
    }
}
```

**핵심:**
- State가 변경되면 Compose가 자동으로 함수를 다시 실행
- 전체가 아닌 **변경된 부분만** 효율적으로 업데이트
- `remember`로 상태를 Recomposition 간에 보존

---

## 문제 상황: Composable 호출 규칙 위반

### 잘못된 코드 예시

```kotlin
// ❌ 에러 1: 일반 함수에서 Composable 호출
fun createGreeting() {
    Text("Hello")  // 컴파일 에러!
}

// ❌ 에러 2: onClick에서 직접 Composable 호출
Button(onClick = {
    Text("Hello")  // 컴파일 에러!
}) { ... }

// ❌ 에러 3: remember 없이 상태 관리
@Composable
fun BrokenCounter() {
    var count = 0  // Recomposition마다 0으로 초기화됨!
    Button(onClick = { count++ }) {
        Text("Count: $count")  // 항상 0
    }
}
```

### 발생하는 문제점

- "@Composable invocations can only happen from the context of a @Composable function"
- 상태가 보존되지 않아 UI가 업데이트 안 됨
- 클릭해도 카운터가 증가하지 않음

---

## 해결책: 올바른 Composable 사용

### 올바른 코드

```kotlin
// ✅ 해결 1: @Composable 어노테이션 추가
@Composable
fun Greeting() {
    Text("Hello")
}

// ✅ 해결 2: onClick에서는 상태 변경, UI는 자동 업데이트
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {  // 상태만 변경
        Text("Count: $count")         // 자동으로 업데이트됨
    }
}

// ✅ 해결 3: remember로 상태 보존
@Composable
fun WorkingCounter() {
    var count by remember { mutableStateOf(0) }  // Recomposition 간 보존!

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### 해결되는 이유

1. `@Composable` 컨텍스트 안에서만 다른 Composable 호출
2. `onClick`에서는 **상태만 변경**, UI 업데이트는 Compose가 자동 처리
3. `remember`가 상태를 Recomposition 간에 보존

---

## 사용 시나리오

### 1. 재사용 가능한 컴포넌트

```kotlin
@Composable
fun UserCard(
    name: String,
    email: String,
    onEdit: () -> Unit
) {
    Card {
        Column {
            Text(name)
            Text(email)
            Button(onClick = onEdit) {
                Text("Edit")
            }
        }
    }
}
```

### 2. 조건부 렌더링

```kotlin
@Composable
fun ConditionalContent(isLoggedIn: Boolean) {
    if (isLoggedIn) {
        WelcomeScreen()   // Composition에 포함
    } else {
        LoginScreen()     // 이게 표시될 때 WelcomeScreen은 Composition에서 제거됨
    }
}
```

### 3. 리스트 렌더링

```kotlin
@Composable
fun ItemList(items: List<String>) {
    Column {
        items.forEach { item ->
            Text(item)  // 각 항목이 별도의 Composable
        }
    }
}
```

---

## 주의사항

1. **@Composable 컨텍스트 필수**
   - 일반 함수, 일반 람다에서 Composable 호출 불가
   - `setContent { }`, 다른 Composable 내부에서만 호출

2. **Side Effect 주의**
   - Composable은 여러 번 실행될 수 있음
   - 네트워크 호출 등은 LaunchedEffect 사용

3. **remember 필수**
   - 상태는 반드시 `remember`로 감싸기
   - 안 그러면 Recomposition마다 초기화

---

## 연습 문제

### 연습 1: 첫 번째 Composable 작성
@Composable 함수를 직접 작성해보세요.

### 연습 2: 상태와 Recomposition
remember와 mutableStateOf로 카운터를 구현해보세요.

### 연습 3: 조건부 렌더링
상태에 따라 다른 Composable을 표시해보세요.

---

## 다음 학습

- **기본 UI 컴포넌트**: Text, Button, Image 등
- **Layout & Modifier**: Column, Row, Box와 Modifier
- **상태 관리**: remember, rememberSaveable, State Hoisting
