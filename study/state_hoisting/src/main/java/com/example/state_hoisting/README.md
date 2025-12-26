# State Hoisting (상태 끌어올리기) 학습

## 개념

**State Hoisting**은 Composable 함수 내부의 상태를 **호출자(caller)에게 이동**시켜 해당 Composable을 **무상태(stateless)**로 만드는 패턴입니다.

상태를 내부에서 관리하는 대신, **상태를 매개변수로 받고** 상태 변경은 **콜백 함수로 위임**합니다.

```kotlin
// State Hoisting 적용 전 (Stateful)
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }  // 내부에서 상태 관리
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}

// State Hoisting 적용 후 (Stateless)
@Composable
fun Counter(
    count: Int,              // 상태는 외부에서 받음
    onIncrement: () -> Unit  // 이벤트는 외부로 위임
) {
    Button(onClick = onIncrement) {
        Text("Count: $count")
    }
}
```

## 핵심 원칙: 단방향 데이터 흐름 (Unidirectional Data Flow)

```
┌─────────────────────────────────────────────────┐
│           상위 Composable (State Owner)          │
│  var count by remember { mutableStateOf(0) }     │
└──────────────────┬────────────────▲─────────────┘
                   │                │
            State  │                │ Event
           (count) ▼                │ (onIncrement)
                   │                │
┌──────────────────▼────────────────┴─────────────┐
│          하위 Composable (Stateless)             │
│  Counter(count = count, onIncrement = { })      │
└─────────────────────────────────────────────────┘
```

- **상태는 아래로 흐름 (State flows down)**: 상위에서 하위로 값 전달
- **이벤트는 위로 흐름 (Events flow up)**: 하위에서 상위로 콜백 호출

## 문제 상황: State Hoisting 없이 상태 관리

### 문제 1: 재사용 불가능한 컴포넌트

```kotlin
@Composable
fun StatefulCounter() {
    var count by remember { mutableIntStateOf(0) }
    // ... UI 렌더링
}
```

**문제점:**
- 초기값을 외부에서 설정 불가
- 부모에서 카운터를 리셋 불가
- 다른 컴포넌트와 상태 공유 불가

### 문제 2: 상태 중복 및 동기화 문제

```kotlin
@Composable
fun DisplayCount() {
    var count by remember { mutableIntStateOf(0) }  // 자체 상태
    Text("Count: $count")
}

@Composable
fun ControlCount() {
    var count by remember { mutableIntStateOf(0) }  // 별도의 상태!
    Button(onClick = { count++ }) { Text("+1") }
}

// 두 컴포넌트의 상태가 동기화되지 않음!
```

### 문제 3: 테스트 어려움

```kotlin
// Stateful 컴포넌트는 UI 테스트로만 검증 가능
@Test
fun testCounter() {
    // Compose Test Rule 필요
    // 실제 UI를 렌더링해야 함
}
```

## 해결책: State Hoisting 패턴

### 1. 기본 패턴: value + onValueChange

TextField가 이미 사용하는 패턴입니다:

```kotlin
@Composable
fun MyTextField(
    value: String,                    // 현재 값 (State down)
    onValueChange: (String) -> Unit,  // 변경 콜백 (Event up)
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
}
```

### 2. 상태 소유자 (State Owner)

```kotlin
@Composable
fun CounterScreen() {
    // 상태를 "소유"하는 컴포넌트
    var count by remember { mutableIntStateOf(0) }
    
    Column {
        // 동일한 상태를 여러 컴포넌트에서 사용 가능
        CounterDisplay(count = count)
        CounterControls(
            onIncrement = { count++ },
            onDecrement = { count-- },
            onReset = { count = 0 }
        )
    }
}
```

### 3. 상태 홀더 클래스 (State Holder)

복잡한 상태는 클래스로 캡슐화:

```kotlin
class FormState(
    initialName: String = "",
    initialEmail: String = ""
) {
    var name by mutableStateOf(initialName)
    var email by mutableStateOf(initialEmail)
    
    val isValid: Boolean
        get() = name.isNotBlank() && email.contains("@")
    
    fun reset() {
        name = ""
        email = ""
    }
}

@Composable
fun rememberFormState() = remember { FormState() }
```

## 언제 상태를 끌어올려야 할까?

### 끌어올려야 할 때

1. **상태를 읽는 모든 컴포넌트의 최소 공통 조상**까지 끌어올림
2. **상태를 변경할 수 있는 가장 높은 수준**까지 끌어올림
3. **동일한 이벤트에 반응하는 두 상태**는 함께 끌어올림

### 끌어올리지 않아도 될 때

1. 상태가 **하나의 컴포넌트에서만 사용**될 때
2. 순수 UI 상태 (스크롤 위치, 포커스 상태 등)
3. 상태를 공유하거나 제어할 필요가 없을 때

## State Hoisting의 장점

| 장점 | 설명 |
|------|------|
| **단일 진실 공급원** | 상태를 복제하지 않고 이동하여 버그 방지 |
| **캡슐화** | 상태를 소유한 컴포넌트만 수정 가능 |
| **공유 가능** | 여러 컴포넌트에서 동일한 상태 사용 |
| **재사용성** | Stateless 컴포넌트는 다양한 맥락에서 재사용 |
| **테스트 용이성** | 입력값만으로 동작 검증 가능 |

## 실무 패턴

### 1. Stateful + Stateless 쌍 만들기

```kotlin
// Stateless 버전 (재사용 가능)
@Composable
fun Counter(
    count: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    // UI만 담당
}

// Stateful 편의 버전 (간편 사용)
@Composable
fun Counter(
    modifier: Modifier = Modifier
) {
    var count by remember { mutableIntStateOf(0) }
    Counter(
        count = count,
        onIncrement = { count++ },
        modifier = modifier
    )
}
```

### 2. ViewModel과 연동

```kotlin
@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel()
) {
    val count by viewModel.count.collectAsState()
    
    Counter(
        count = count,
        onIncrement = viewModel::increment
    )
}
```

## 다음 학습

- **rememberSaveable**: Configuration Change에서도 상태 유지
- **ViewModel**: 화면 수준의 상태 관리
- **Stability**: Recomposition 최적화를 위한 안정성 이해

## 연습 문제

1. **Stateful을 Stateless로 변환**: 내부 상태를 가진 컴포넌트를 State Hoisting 적용
2. **상태 공유 구현**: 두 컴포넌트가 동일한 상태를 사용하도록 구현
3. **폼 상태 관리**: 여러 입력 필드의 상태를 하나의 상위 컴포넌트에서 관리

## 참고 자료

- [Where to hoist state - Android Developers](https://developer.android.com/develop/ui/compose/state-hoisting)
- [State and Jetpack Compose - Android Developers](https://developer.android.com/develop/ui/compose/state)
- [State Hoisting Best Practices - Medium](https://medium.com/@jecky999/state-hoisting-in-jetpack-compose-best-practices-examples-f4099f3d5592)
