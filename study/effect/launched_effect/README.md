# LaunchedEffect 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기                                                                        |
|----------|------|-----------------------------------------------------------------------------|
| `side_effect` | Side Effect 개념과 Compose에서의 의미 | [📚 학습하기](.../../effect/side_effect/README.md) |
| `remember` | Recomposition에도 상태 유지 | [📚 학습하기](../../state/remember/README.md)            |
| `recomposition` | 상태 변경 시 UI가 다시 그려지는 과정 | [📚 학습하기](../../state/recomposition/README.md)  |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 핵심 용어 해설

| 용어 | 설명 |
|------|------|
| **Side Effect** | Composable 함수의 범위 밖에서 발생하는 앱 상태의 변경 (예: 네트워크 요청, 로깅) |
| **Composition** | Compose가 UI 트리를 구성하는 과정. Composable이 "화면에 표시됨" |
| **Recomposition** | 상태가 변경되어 Composable이 다시 실행되는 것. "화면이 다시 그려짐" |

---

## 개념

`LaunchedEffect`는 **Composable 함수 내에서 코루틴을 안전하게 실행**하기 위한 Side Effect API입니다.

> "LaunchedEffect는 마치 **자동 정지 기능이 있는 타이머**와 같습니다.
> 화면에 들어오면 시작하고, 화면을 나가면 자동으로 멈춥니다."

```kotlin
LaunchedEffect(key1, key2, ...) {
    // 코루틴 스코프 내에서 실행됨
    // suspend 함수 호출 가능
}
```

## 핵심 특징

1. **Composition에 진입할 때** 코루틴이 시작됨
2. **key가 변경되면** 기존 코루틴을 취소하고 새로 시작
3. **Composition을 떠나면** 자동으로 코루틴 취소
4. **Composable 본문에서 suspend 함수**를 안전하게 호출할 수 있음

---

## 문제 상황: LaunchedEffect 없이 코루틴 실행하기

### 시나리오

> 당신은 사용자 프로필 화면을 개발하고 있습니다.
> 화면에 진입하면 서버에서 사용자 정보를 불러와 표시해야 합니다.
> Compose 함수 안에서 API를 호출하려면 어떻게 해야 할까요?

### 잘못된 코드 예시

```kotlin
@Composable
fun BadExample(userId: String) {
    val scope = rememberCoroutineScope()
    var userData by remember { mutableStateOf<User?>(null) }

    // ❌ 문제: Recomposition마다 매번 API 호출!
    scope.launch {
        userData = fetchUserData(userId)
    }

    Text(userData?.name ?: "Loading...")
}
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 무한 루프 | API 호출 → state 변경 → Recomposition → 다시 API 호출 → 반복 |
| 리소스 낭비 | 불필요한 네트워크 요청 반복 |
| 앱 크래시 | 메모리 누수, ANR 발생 위험 |

---

## 해결책: LaunchedEffect 사용

### 올바른 코드

```kotlin
@Composable
fun GoodExample(userId: String) {
    var userData by remember { mutableStateOf<User?>(null) }

    // ✅ userId가 변경될 때만 실행
    LaunchedEffect(userId) {
        userData = fetchUserData(userId)
    }

    Text(userData?.name ?: "Loading...")
}
```

### 해결되는 이유

| 해결 | 설명 |
|------|------|
| 무한 루프 방지 | key(userId)가 같으면 재실행하지 않음 |
| 최신 데이터만 로드 | key가 변경되면 기존 작업 취소 후 새로 실행 |
| 메모리 누수 방지 | Composable이 사라지면 자동 취소 |

---

## 사용 시나리오

### 1. 화면 진입 시 한 번만 실행
```kotlin
LaunchedEffect(Unit) {
    analytics.logScreenView("HomeScreen")
}
```

### 2. 특정 값 변경 시 실행 (debounce)
```kotlin
LaunchedEffect(searchQuery) {
    delay(300) // debounce
    searchResults = searchApi.search(searchQuery)
}
```

### 3. 여러 key 조합
```kotlin
LaunchedEffect(userId, category) {
    items = fetchItems(userId, category)
}
```

---

## 주의사항

### 1. key 선택이 중요!

```kotlin
// ❌ 너무 자주 변경되는 key
LaunchedEffect(someFrequentlyChangingValue) { ... }

// ❌ key 없이 Unit만 사용 (업데이트 안됨)
LaunchedEffect(Unit) {
    val data = fetchData(userId) // userId 변경 시 다시 안 불림!
}

// ✅ 적절한 key 선택
LaunchedEffect(userId) {
    val data = fetchData(userId)
}
```

### 2. 무한 루프에서 isActive 사용

`while` 루프를 사용할 때는 반드시 `isActive`를 체크해야 합니다.
코루틴이 취소되어도 `while(true)`는 계속 실행되기 때문입니다.

```kotlin
// ❌ 코루틴 취소 시 무한 루프 위험
LaunchedEffect(key) {
    while (true) {
        delay(1000)
        doSomething()
    }
}

// ✅ 코루틴 취소 시 안전하게 종료
LaunchedEffect(key) {
    while (isActive) {  // CoroutineScope에서 제공
        delay(1000)
        doSomething()
    }
}
```

### 3. rememberCoroutineScope와의 차이

| 상황 | 사용할 API |
|------|-----------|
| Composition 진입 시 자동 실행 | `LaunchedEffect` |
| 사용자 이벤트(클릭 등)에 의한 실행 | `rememberCoroutineScope` |

```kotlin
// LaunchedEffect: 화면 진입 시 자동 실행
LaunchedEffect(Unit) {
    loadInitialData()
}

// rememberCoroutineScope: 버튼 클릭 시 실행
val scope = rememberCoroutineScope()
Button(onClick = {
    scope.launch { saveData() }
}) { Text("저장") }
```

---

## 고급: rememberUpdatedState 패턴

`LaunchedEffect` 내에서 변경될 수 있는 값을 사용할 때, effect를 재시작하지 않고 최신 값을 참조하고 싶다면 `rememberUpdatedState`를 사용합니다.

```kotlin
@Composable
fun LandingScreen(onTimeout: () -> Unit) {
    // onTimeout이 변경되어도 LaunchedEffect는 재시작하지 않음
    // 하지만 항상 최신 onTimeout을 참조
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(Unit) {  // Unit이므로 한 번만 실행
        delay(3000)
        currentOnTimeout()  // 3초 후 최신 콜백 호출
    }
}
```

**언제 사용하나요?**
- key 변경 시 effect를 재시작하고 싶지 않을 때
- 콜백 함수가 자주 변경되지만 effect 내에서 최신 값이 필요할 때

---

## 실행 흐름 다이어그램

```
[Composable 진입]
       |
       v
+------------------+
| LaunchedEffect   |
| key = userId     |
+------------------+
       |
       v
[코루틴 시작: fetchData(userId)]
       |
       +--------> [userId 변경됨?]
       |                |
       |           Yes  |  No
       |                v    v
       |          [기존 코루틴 취소]  [계속 실행]
       |                |
       |                v
       |          [새 코루틴 시작]
       |
       v
[Composable 이탈] --> [코루틴 자동 취소]
```

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | 잘못된 코드 - 무한 루프 문제 재현 |
| `Solution.kt` | 올바른 코드 - LaunchedEffect 사용 |
| `Practice.kt` | 연습 문제 4개 (검색, 타이머, 탭, Splash) |

---

## 연습 문제

| 난이도 | 문제 | 설명 |
|--------|------|------|
| 쉬움 | **검색 기능** | debounce를 적용한 검색 구현 |
| 쉬움 | **타이머** | 1초마다 증가하는 타이머 + 리셋 기능 |
| 중간 | **탭 데이터 로드** | 탭 전환 시 데이터 로드 |
| 고급 | **Splash Screen** | rememberUpdatedState로 콜백 관리 |

---

## 다음 학습

- `DisposableEffect`: 정리(cleanup)가 필요한 side effect
- `SideEffect`: 매 Recomposition마다 실행해야 할 작업
- `rememberCoroutineScope`: 사용자 이벤트에 의한 코루틴 실행
