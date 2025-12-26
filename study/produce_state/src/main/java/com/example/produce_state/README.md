# produceState 학습

## 개념

`produceState`는 **비-Compose 상태를 Compose 상태로 변환**하는 Side Effect API입니다.

suspend 함수, Flow, LiveData, RxJava 등 외부 데이터 소스의 결과를 Compose `State<T>`로 변환하여 UI에서 직접 사용할 수 있게 합니다.

```kotlin
val userState by produceState<User?>(initialValue = null, key1 = userId) {
    value = api.fetchUser(userId)  // suspend 함수 결과를 State로
}
```

## 핵심 특징

1. **상태 선언과 비동기 로직 통합**: `remember` + `LaunchedEffect` 조합을 단일 API로 대체
2. **코루틴 스코프 자동 관리**: Composition 진입 시 시작, 이탈 시 자동 취소
3. **State Conflation**: 동일한 값을 설정해도 불필요한 Recomposition 트리거 안 함
4. **Key 기반 재시작**: key가 변경되면 기존 코루틴 취소 후 새로 시작

## 내부 동작 원리

`produceState`는 내부적으로 다음과 같이 구현됩니다:

```kotlin
@Composable
fun <T> produceState(
    initialValue: T,
    vararg keys: Any?,
    producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = remember { mutableStateOf(initialValue) }

    LaunchedEffect(keys = keys) {
        ProduceStateScopeImpl(result, coroutineContext).producer()
    }

    return result
}
```

즉, `remember { mutableStateOf() }` + `LaunchedEffect`의 조합입니다!

## 문제 상황: LaunchedEffect + 다중 상태 관리의 복잡함

### 잘못된 코드 예시

```kotlin
@Composable
fun UserProfile(userId: String) {
    // 문제: 3개의 상태 변수를 개별 관리해야 함
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        isLoading = true
        errorMessage = null
        try {
            user = api.fetchUser(userId)
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.message
            isLoading = false
        }
    }

    // 상태 조합으로 UI 표시
    when {
        isLoading -> LoadingIndicator()
        errorMessage != null -> ErrorMessage(errorMessage!!)
        user != null -> UserCard(user!!)
    }
}
```

### 발생하는 문제점

1. **상태 변수 폭발**: Loading, Success, Error 각각 별도 변수 필요
2. **동기화 실수 가능**: `isLoading = false`를 빠뜨리면 영원히 로딩 상태
3. **null 처리 복잡**: `!!` 연산자 남발 또는 복잡한 null 체크
4. **코드 중복**: 비슷한 패턴이 화면마다 반복

## 해결책: produceState 사용

### 올바른 코드

```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

@Composable
fun UserProfile(userId: String) {
    val userState by produceState<UiState<User>>(
        initialValue = UiState.Loading,
        key1 = userId
    ) {
        value = try {
            UiState.Success(api.fetchUser(userId))
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Unknown error")
        }
    }

    when (userState) {
        is UiState.Loading -> LoadingIndicator()
        is UiState.Success -> UserCard((userState as UiState.Success).data)
        is UiState.Error -> ErrorMessage((userState as UiState.Error).message)
    }
}
```

### 해결되는 이유

1. **단일 상태**: sealed class로 모든 상태를 하나로 표현
2. **exhaustive 검사**: when 표현식으로 컴파일 타임 검증
3. **자동 재시작**: key(userId) 변경 시 자동으로 새 요청
4. **선언적 표현**: "이 상태는 비동기로 생성됨"을 코드로 명확히 표현

## 사용 시나리오

### 1. 네트워크 요청 결과를 State로

```kotlin
val userState by produceState<UiState<User>>(UiState.Loading, userId) {
    value = try {
        UiState.Success(api.fetchUser(userId))
    } catch (e: Exception) {
        UiState.Error(e.message ?: "Error")
    }
}
```

### 2. 타이머/스톱워치

```kotlin
val seconds by produceState(initialValue = 0) {
    while (true) {
        delay(1000)
        value += 1
    }
}
```

### 3. Flow 수집

```kotlin
val stockPrice by produceState(initialValue = 0.0, ticker) {
    stockRepository.observePrice(ticker).collect { price ->
        value = price
    }
}
```

### 4. 비-suspend 데이터 소스 (awaitDispose 활용)

```kotlin
val location by produceState<Location?>(initialValue = null) {
    val callback = LocationCallback { result ->
        value = result.lastLocation
    }

    locationClient.requestLocationUpdates(request, callback, looper)

    awaitDispose {
        locationClient.removeLocationUpdates(callback)
    }
}
```

## produceState vs LaunchedEffect

| 특성 | produceState | LaunchedEffect |
|------|-------------|----------------|
| **목적** | 비동기 결과를 State로 반환 | 부수 효과 실행 |
| **반환값** | `State<T>` | 없음 (Unit) |
| **사용 시점** | UI에 표시할 데이터 로딩 | 로깅, 애널리틱스, 네비게이션 등 |
| **상태 관리** | 자동 (ProduceStateScope.value) | 수동 (별도 remember 필요) |

### 선택 기준

- **UI에 표시할 비동기 데이터** → `produceState`
- **부수 효과만 필요** (상태 반환 불필요) → `LaunchedEffect`

## ProduceStateScope API

```kotlin
interface ProduceStateScope<T> : MutableState<T>, CoroutineScope {
    // value: 현재 상태 값 (읽기/쓰기)
    override var value: T

    // awaitDispose: 비-suspend 리소스 정리용
    suspend fun awaitDispose(onDispose: () -> Unit): Nothing
}
```

### awaitDispose 사용법

비-코루틴 데이터 소스(콜백 기반 API)를 사용할 때 정리가 필요한 경우:

```kotlin
produceState<T>(initialValue) {
    val callback = SomeCallback { newValue ->
        value = newValue
    }

    someApi.registerCallback(callback)

    awaitDispose {
        someApi.unregisterCallback(callback)
    }
}
```

## 주의사항

1. **초기값 필수**: 비동기 완료 전 표시할 값을 반드시 제공
2. **key 선택**: 재시작이 필요한 조건을 정확히 key로 지정
3. **Dispatcher 주의**: 필요 시 `withContext(Dispatchers.IO)`로 스레드 전환
4. **메모리 누수**: awaitDispose로 리소스 정리 필수 (콜백 기반 API 사용 시)

## 연습 문제

### 연습 1: 기초 타이머
`produceState`를 사용해 1초마다 증가하는 타이머를 구현하세요.

### 연습 2: 데이터 로딩 with Result
sealed class `UiState`와 함께 상품 목록을 로딩하고, 카테고리 변경 시 재로딩되도록 구현하세요.

### 연습 3: 카운트다운 with awaitDispose
10초 카운트다운 타이머를 구현하고, Composable 이탈 시 로그를 출력하세요.

## 다음 학습

- **snapshotFlow**: Compose 상태를 Flow로 변환 (produceState의 반대)
- **rememberUpdatedState**: 콜백에서 최신 값 참조
- **derivedStateOf**: 다른 상태로부터 파생 상태 생성
