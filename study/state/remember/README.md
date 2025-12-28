# remember & mutableStateOf 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | Text, Button, TextField 등 기본 UI 사용법 | [📚 학습하기](../../layout/basic_ui_components/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`remember`와 `mutableStateOf`는 Jetpack Compose에서 **상태 관리의 가장 기본적인 빌딩 블록**입니다.

- **mutableStateOf**: 관찰 가능한(observable) 상태 홀더를 생성. 값이 변경되면 해당 값을 읽는 모든 Composable이 자동으로 Recomposition됨
- **remember**: Recomposition 간에 값을 메모리에 유지. 없으면 매 Recomposition마다 상태가 초기화됨

```kotlin
// 기본 패턴
var count by remember { mutableStateOf(0) }

// 원시 타입 최적화 패턴 (권장)
var count by remember { mutableIntStateOf(0) }
```

## 핵심 특징

1. **반응형 UI의 핵심**: 상태가 변하면 UI가 자동으로 업데이트
2. **Recomposition 생존**: remember로 감싸야 Recomposition 간 값 유지
3. **Composition 수명 한정**: 화면 회전 등 Configuration Change 시 손실됨 (rememberSaveable 사용 필요)
4. **key 매개변수 지원**: 특정 값이 변경될 때만 재계산 가능
5. **원시 타입 최적화**: Int, Long, Float, Double 등은 전용 State 함수 사용 권장

## 문제 상황: remember 없이 상태 관리

### 시나리오

쇼핑 앱에서 장바구니에 담긴 상품 수량을 표시하는 카운터를 구현한다고 가정해봅시다. 사용자가 "+" 버튼을 누르면 수량이 증가해야 합니다. 하지만 remember를 사용하지 않으면 예상치 못한 문제가 발생합니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun BrokenCounter() {
    // 문제 1: 일반 변수 - 상태 변경이 UI에 반영되지 않음
    var count = 0

    Button(onClick = { count++ }) {
        Text("Count: $count")  // 항상 0 표시
    }
}

@Composable
fun AnotherBrokenCounter() {
    // 문제 2: mutableStateOf만 사용 - Recomposition마다 초기화
    var count by mutableStateOf(0)  // remember 없음!

    Button(onClick = { count++ }) {
        Text("Count: $count")  // 클릭하면 1이 되지만 다시 0으로 돌아감
    }
}
```

### 발생하는 문제점

1. **일반 변수**: 값을 변경해도 Compose가 알지 못함 → UI 업데이트 안 됨
2. **remember 없는 mutableStateOf**: Recomposition될 때마다 0으로 재초기화
3. **상태 유실**: 사용자 입력이 저장되지 않음

## 해결책: remember + mutableStateOf

### 올바른 코드

```kotlin
@Composable
fun Counter() {
    // 방법 1: = 연산자 (State 객체 직접 접근)
    val count = remember { mutableStateOf(0) }
    Button(onClick = { count.value++ }) {
        Text("Count: ${count.value}")
    }

    // 방법 2: by 위임 (권장 - 더 간결)
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### 해결되는 이유

1. **mutableStateOf**: Compose에게 "이 값이 변경되면 알려줘"라고 등록
2. **remember**: "Recomposition이 일어나도 이 값을 유지해줘"
3. 두 가지가 결합되어 **안정적인 상태 관리** 가능

## 사용 시나리오

### 1. 카운터 / 토글
```kotlin
var isExpanded by remember { mutableStateOf(false) }
var count by remember { mutableIntStateOf(0) }
```

### 2. 텍스트 입력
```kotlin
var text by remember { mutableStateOf("") }
TextField(
    value = text,
    onValueChange = { text = it }
)
```

### 3. key를 활용한 캐싱
```kotlin
// userId가 변경될 때만 재계산
val userData = remember(userId) {
    computeUserData(userId)
}
```

### 4. 비용이 큰 계산
```kotlin
val sortedList = remember(items) {
    items.sortedBy { it.name }  // items가 변경될 때만 정렬
}
```

## = vs by 비교

| 방식 | 문법 | 접근 방식 | 권장 상황 |
|------|------|----------|----------|
| `=` | `val count = remember { mutableStateOf(0) }` | `count.value` | State 객체 자체를 전달할 때 |
| `by` | `var count by remember { mutableStateOf(0) }` | `count` | 일반적인 상황 (권장) |

```kotlin
// = 방식: State 객체를 전달할 때 유용
val countState = remember { mutableStateOf(0) }
ChildComposable(countState)  // State<Int> 타입 전달

// by 방식: 간결한 접근 (권장)
var count by remember { mutableStateOf(0) }
Text("Count: $count")  // .value 불필요
```

## 원시 타입 최적화

Compose 1.4부터 원시 타입(Int, Long, Float, Double)에 대한 전용 State 함수가 추가되었습니다. 오토박싱(autoboxing)을 피해 성능을 최적화합니다.

```kotlin
// Int
var count by remember { mutableIntStateOf(0) }

// Long
var timestamp by remember { mutableLongStateOf(0L) }

// Float
var progress by remember { mutableFloatStateOf(0f) }

// Double
var value by remember { mutableDoubleStateOf(0.0) }
```

**왜 사용해야 하나요?**
- `mutableStateOf(0)`은 내부적으로 `Int`를 `Integer` 객체로 박싱
- `mutableIntStateOf(0)`은 원시 `int`를 직접 저장
- 빈번한 상태 변경 시 메모리 할당 및 GC 부담 감소

## 주의사항

1. **remember는 Composition 수명 동안만 유지**: 화면 회전 시 상태 손실 → `rememberSaveable` 학습 필요
2. **key 매개변수 활용**: 의존성 기반 재계산이 필요할 때 `remember(key) { ... }` 사용
3. **by 위임 시 var 사용**: `val`이 아닌 `var`로 선언해야 값 변경 가능
4. **원시 타입 최적화**: `mutableIntStateOf`, `mutableFloatStateOf` 등 사용 권장
5. **리스트/객체 변경 시 주의**: 리스트 수정 시 `list = list + item` 형태로 새 인스턴스 할당 필요

## 다음 학습

- **rememberSaveable**: Configuration Change(화면 회전)에서도 상태 유지
- **State Hoisting**: 상태를 상위로 끌어올려 재사용 가능한 Composable 만들기
- **ViewModel**: 화면 수준의 상태 관리, 비즈니스 로직 분리

## 연습 문제

1. **토글 버튼 구현**: 클릭하면 "좋아요" 상태가 토글되는 버튼
2. **텍스트 입력 폼**: 이름을 입력받아 인사 메시지 표시
3. **remember key 활용**: 숫자 목록의 합계를 캐싱하여 계산

## 참고 자료

- [State and Jetpack Compose - Android Developers](https://developer.android.com/develop/ui/compose/state)
- [remember { mutableStateOf() } - A cheat sheet](https://dev.to/zachklipp/remember-mutablestateof-a-cheat-sheet-10ma)
- [Jetpack Compose State Management - Bugfender](https://bugfender.com/blog/jetpack-compose-state-management/)
