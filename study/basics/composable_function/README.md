# @Composable 함수 완벽 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `compose_introduction` | Jetpack Compose 소개 및 선언적 UI 개념 | [📚 학습하기](../../basics/compose_introduction/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

`@Composable` 함수는 Jetpack Compose의 **가장 기본적인 빌딩 블록**입니다. Compose로 UI를 만들려면 반드시 이해해야 하는 핵심 개념입니다.

> **핵심 포인트**: Composable 함수는 UI를 "반환"하지 않고 "선언"합니다. 함수를 호출하면 UI 트리에 노드가 추가됩니다.

---

## 기본 사용법

```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hello, $name!")
}

// 사용
@Composable
fun WelcomeScreen() {
    Column {
        Greeting("Android")
        Greeting("Compose")
    }
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

## Composable 함수의 3가지 필수 특성

공식 문서에 따르면, Composable 함수는 다음 특성을 가져야 합니다:

| 특성 | 설명 |
|------|------|
| **Fast (빠름)** | 애니메이션의 모든 프레임에서 호출될 수 있으므로 빠르게 실행되어야 함 |
| **Idempotent (멱등성)** | 같은 인자로 여러 번 호출해도 동일한 결과 |
| **Side-effect Free** | 전역 변수 수정, 네트워크 호출 등의 부수 효과 없음 |

```kotlin
// ✅ 좋은 예: 순수한 Composable
@Composable
fun Greeting(name: String) {
    Text("Hello, $name!")  // 같은 name이면 항상 같은 결과
}

// ❌ 나쁜 예: Side Effect 포함
@Composable
fun BadGreeting(name: String) {
    println("Rendered!")  // Side Effect! Recomposition마다 출력됨
    Log.d("TAG", "Called")  // Side Effect!
    Text("Hello, $name!")
}
```

---

## Composable 사용 규칙

### 규칙 1: @Composable 컨텍스트 필수

```kotlin
// ❌ 컴파일 에러: 일반 함수에서 Composable 호출 불가
fun createGreeting() {
    Text("Hello")  // Error!
}

// ✅ 올바른 사용: @Composable 함수 내에서 호출
@Composable
fun Greeting() {
    Text("Hello")
}
```

### 규칙 2: onClick에서는 상태만 변경

```kotlin
// ❌ 컴파일 에러: onClick은 일반 람다
Button(onClick = {
    Text("Hello")  // Error! onClick은 @Composable 람다가 아님
}) { ... }

// ✅ 올바른 사용: 상태 변경만 수행
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {  // 상태만 변경
        Text("Count: $count")         // Compose가 자동 업데이트
    }
}
```

### 규칙 3: remember로 상태 보존

```kotlin
// ❌ 문제: Recomposition마다 0으로 초기화
@Composable
fun BrokenCounter() {
    var count = 0  // remember 없음!
    Button(onClick = { count++ }) {
        Text("Count: $count")  // 항상 0
    }
}

// ✅ 해결: remember로 상태 보존
@Composable
fun WorkingCounter() {
    var count by remember { mutableStateOf(0) }  // Recomposition 간 보존!
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

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

## 베스트 프랙티스

### 1. Modifier 파라미터 규칙

```kotlin
// ✅ 권장: modifier는 첫 번째 선택적 파라미터
@Composable
fun UserCard(
    name: String,                    // 필수 파라미터
    modifier: Modifier = Modifier,   // 첫 번째 선택적 파라미터
    onClick: () -> Unit = {}         // 기타 선택적 파라미터
) {
    Card(modifier = modifier) {      // modifier는 단일 루트에만 적용
        Text(name)
    }
}
```

### 2. 비용이 큰 연산은 remember로 캐싱

```kotlin
@Composable
fun ExpensiveCalculation(items: List<Item>) {
    // ✅ remember로 계산 결과 캐싱
    val sortedItems = remember(items) {
        items.sortedByDescending { it.priority }  // 한 번만 계산
    }

    LazyColumn {
        items(sortedItems) { item ->
            ItemRow(item)
        }
    }
}
```

### 3. Side Effect가 필요한 경우

```kotlin
@Composable
fun UserProfile(userId: String) {
    var user by remember { mutableStateOf<User?>(null) }

    // ✅ Side Effect는 LaunchedEffect 사용
    LaunchedEffect(userId) {
        user = fetchUser(userId)  // 네트워크 호출
    }

    user?.let { UserContent(it) }
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

4. **Recomposition 최적화**
   - Stable 타입 사용 권장 (불변 객체, data class)
   - 자주 변경되는 상태는 람다로 지연 읽기

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
