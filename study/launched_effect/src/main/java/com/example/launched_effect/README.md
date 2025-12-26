# LaunchedEffect 학습

## 개념

`LaunchedEffect`는 **Composable 함수 내에서 코루틴을 안전하게 실행**하기 위한 Side Effect API입니다.

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
4. **suspend 함수**를 호출할 수 있는 유일한 방법 (Composable 내에서)

---

## 문제 상황: LaunchedEffect 없이 코루틴 실행하기

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

### key 선택이 중요!

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

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | 잘못된 코드 - 무한 루프 문제 재현 |
| `Solution.kt` | 올바른 코드 - LaunchedEffect 사용 |
| `Practice.kt` | 연습 문제 3개 (검색, 타이머, 탭) |

---

## 연습 문제

1. **검색 기능**: debounce를 적용한 검색 구현
2. **타이머**: 1초마다 증가하는 타이머 + 리셋 기능
3. **탭 데이터 로드**: 탭 전환 시 데이터 로드

---

## 다음 학습

- `DisposableEffect`: 정리(cleanup)가 필요한 side effect
- `SideEffect`: 매 Recomposition마다 실행해야 할 작업
- `rememberCoroutineScope`: 사용자 이벤트에 의한 코루틴 실행
