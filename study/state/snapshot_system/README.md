# Snapshot System 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `mutableStateOf` | Compose에서 관찰 가능한 상태 생성 | [📚 학습하기](../remember/README.md) |
| `Flow` | Kotlin 비동기 스트림 처리 | [📚 학습하기](../../basics/kotlin_flow/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Snapshot System은 Compose의 상태 관리를 가능하게 하는 **핵심 내부 메커니즘**입니다.
비디오 게임의 "세이브 포인트"처럼, 특정 시점의 모든 상태를 기록하고 변경을 추적합니다.

> **비유로 이해하기**
>
> Snapshot은 게임의 세이브 파일과 같습니다:
> - 특정 시점의 상태를 "사진 찍듯이" 저장합니다
> - 다른 플레이어의 게임에 영향을 주지 않습니다 (격리)
> - 여러 세이브 파일을 만들고 병합할 수 있습니다

## 핵심 특징

1. **자동 상태 추적**: Compose가 어떤 State를 읽었는지 자동으로 기록합니다
2. **변경 감지**: State가 바뀌면 해당 State를 사용하는 UI만 다시 그립니다
3. **상태 격리**: 한 곳에서의 변경이 다른 곳에 즉시 영향을 주지 않습니다 (트랜잭션)
4. **Flow 변환**: `snapshotFlow`를 통해 State를 Flow로 변환할 수 있습니다

---

## 문제 상황: State 변경을 어떻게 외부에서 관찰할까?

### 시나리오

검색 앱을 만들고 있습니다. 사용자가 검색어를 입력할 때마다 API를 호출해야 합니다.
하지만 글자 하나 입력할 때마다 API를 호출하면 너무 많은 요청이 발생합니다.

### 잘못된 접근 방식

```kotlin
// 방법 1: LaunchedEffect를 key로 사용
// 문제: 디바운스 적용이 어렵고, 매 변경마다 새 코루틴 시작
@Composable
fun BadSearchExample() {
    var query by remember { mutableStateOf("") }

    // query가 바뀔 때마다 새로운 LaunchedEffect 실행
    LaunchedEffect(query) {
        // 이 코드는 글자마다 실행됨!
        if (query.isNotEmpty()) {
            val results = searchApi(query)  // 과도한 API 호출
        }
    }

    TextField(
        value = query,
        onValueChange = { query = it }
    )
}
```

```kotlin
// 방법 2: 수동으로 이전 값과 비교
// 문제: 복잡하고, Flow 연산자(debounce, filter) 사용 불가
@Composable
fun ManualTrackingExample() {
    var query by remember { mutableStateOf("") }
    var lastQuery by remember { mutableStateOf("") }

    // Recomposition마다 체크해야 함
    if (query != lastQuery) {
        lastQuery = query
        // 여기서 어떻게 디바운스를 적용할까요?
        // Flow 연산자를 사용할 수 없습니다!
    }
}
```

### 발생하는 문제점

1. **과도한 호출**: 글자 하나마다 API 호출 발생
2. **디바운스 불가**: 일정 시간 대기 후 처리하는 로직 구현이 복잡
3. **Flow 연산자 활용 불가**: `debounce()`, `filter()`, `distinctUntilChanged()` 등 사용 불가
4. **복잡한 코드**: 수동 추적 로직으로 코드가 복잡해짐

---

## 해결책: snapshotFlow 사용

### snapshotFlow란?

`snapshotFlow`는 Compose State를 Kotlin Flow로 변환하는 함수입니다.
State가 변경될 때마다 새 값을 Flow로 전달합니다.

> **비유**: snapshotFlow는 "변경 알림 구독"과 같습니다
> - 신문 구독처럼 새 소식(State 변경)이 있을 때만 배달됩니다
> - 구독을 취소하면 더 이상 알림을 받지 않습니다
> - 다양한 필터(연산자)를 적용할 수 있습니다

### 기본 사용법

```kotlin
@Composable
fun SnapshotFlowBasicExample() {
    var counter by remember { mutableIntStateOf(0) }
    var logMessage by remember { mutableStateOf("아직 변경 없음") }

    // snapshotFlow로 counter 변경을 관찰
    LaunchedEffect(Unit) {
        snapshotFlow { counter }  // counter를 Flow로 변환
            .collect { value ->   // 값이 변경될 때마다 실행
                logMessage = "카운터가 $value로 변경됨"
            }
    }

    Column {
        Button(onClick = { counter++ }) {
            Text("카운트: $counter")
        }
        Text(logMessage)
    }
}
```

### Flow 연산자와 조합

snapshotFlow의 진정한 강점은 **모든 Flow 연산자를 사용할 수 있다**는 것입니다.

```kotlin
@Composable
fun SearchWithDebounceExample() {
    var query by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf("검색어를 입력하세요") }
    var apiCallCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(500)           // 500ms 동안 입력이 없을 때만 처리
            .filter { it.length >= 2 }  // 2글자 이상일 때만
            .distinctUntilChanged()  // 같은 값은 무시
            .collect { searchQuery ->
                apiCallCount++
                searchResult = "검색 중: $searchQuery (API 호출 #$apiCallCount)"
                // 실제로는 여기서 API 호출
            }
    }

    Column {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("검색어") }
        )
        Text(searchResult)
        Text("총 API 호출 횟수: $apiCallCount")
    }
}
```

### 해결되는 이유

1. **Flow 연산자 활용**: `debounce()`, `filter()`, `distinctUntilChanged()` 등 모든 연산자 사용 가능
2. **효율적인 처리**: 조건을 만족할 때만 collect 블록 실행
3. **깔끔한 코드**: 선언적으로 상태 변경 처리 로직 작성
4. **자동 취소**: LaunchedEffect가 종료되면 Flow 수집도 자동 취소

---

## 사용 시나리오

### 1. 검색 자동완성 (디바운스)

```kotlin
LaunchedEffect(Unit) {
    snapshotFlow { searchQuery }
        .debounce(300)           // 타이핑 멈춘 후 300ms 대기
        .filter { it.isNotBlank() }
        .mapLatest { query ->    // 새 검색어 입력 시 이전 검색 취소
            searchRepository.getSuggestions(query)
        }
        .collect { suggestions ->
            suggestionList = suggestions
        }
}
```

### 2. 스크롤 위치 기반 분석 이벤트

```kotlin
val listState = rememberLazyListState()

LaunchedEffect(listState) {
    snapshotFlow { listState.firstVisibleItemIndex }
        .map { index -> index > 10 }  // 10번째 아이템 이후인지
        .distinctUntilChanged()       // 상태 변경 시에만
        .filter { it }                // true일 때만
        .collect {
            analytics.logEvent("user_scrolled_deep")
        }
}
```

### 3. 폼 유효성 변경 추적

```kotlin
LaunchedEffect(Unit) {
    snapshotFlow {
        email.isNotBlank() && password.length >= 8
    }
        .distinctUntilChanged()
        .collect { isValid ->
            if (isValid) {
                submitButton.enable()
            } else {
                submitButton.disable()
            }
        }
}
```

### 4. 여러 State 조합 관찰

```kotlin
LaunchedEffect(Unit) {
    snapshotFlow {
        FormData(
            name = nameState,
            email = emailState,
            phone = phoneState
        )
    }
        .debounce(1000)  // 1초간 변경 없으면 자동 저장
        .collect { formData ->
            repository.saveDraft(formData)
        }
}
```

---

## 내부 동작 원리 (심화)

### Compose가 Recomposition을 트리거하는 방법

1. **읽기 추적**: Composable이 State를 읽을 때 Snapshot System이 기록합니다
2. **쓰기 감지**: State가 변경되면 Snapshot System이 감지합니다
3. **영향 범위 계산**: 변경된 State를 읽은 Composable만 찾아냅니다
4. **Recomposition 스케줄링**: 해당 Composable만 다시 실행합니다

```
State 변경 감지
    ↓
Snapshot System이 변경 알림
    ↓
해당 State를 읽은 Composable 찾기
    ↓
그 Composable만 Recomposition
```

### 고급 API (참고용)

일반적인 앱 개발에서는 `snapshotFlow`만으로 충분하지만,
라이브러리 개발이나 특수한 경우에는 다음 API들을 사용할 수 있습니다:

| API | 용도 |
|-----|------|
| `Snapshot.takeMutableSnapshot()` | 격리된 상태 스냅샷 생성 (테스트, 트랜잭션) |
| `Snapshot.observe()` | 읽기/쓰기 관찰 (커스텀 관찰 로직) |
| `Snapshot.registerApplyObserver()` | 전역 상태 변경 감지 (런타임 레벨) |
| `Snapshot.registerGlobalWriteObserver()` | 전역 쓰기 감지 (런타임 레벨) |

---

## 주의사항

### 1. LaunchedEffect 내에서 사용

```kotlin
// O 올바른 사용
LaunchedEffect(Unit) {
    snapshotFlow { state.value }
        .collect { ... }
}

// X 잘못된 사용 - Composable 외부에서 직접 호출 불가
val flow = snapshotFlow { state.value }  // 이것만으로는 동작 안 함
```

### 2. 무한 루프 주의

```kotlin
// X 위험한 코드 - collect에서 같은 State 수정
LaunchedEffect(Unit) {
    snapshotFlow { counter }
        .collect {
            counter++  // 무한 루프 발생!
        }
}
```

### 3. key 선택

```kotlin
// 특정 조건에서만 관찰하려면 적절한 key 사용
LaunchedEffect(userId) {  // userId가 바뀌면 재시작
    snapshotFlow { userState }
        .collect { ... }
}
```

### 4. debounce는 FlowPreview

```kotlin
import kotlinx.coroutines.FlowPreview  // 필요할 수 있음

@OptIn(FlowPreview::class)
@Composable
fun Example() {
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300)  // FlowPreview API
            .collect { ... }
    }
}
```

---

## 연습 문제

### 연습 1: 카운터 5의 배수 알림 (쉬움)

버튼을 클릭할 때마다 카운터가 증가합니다.
카운터가 5의 배수가 될 때마다 알림 메시지를 표시하세요.

**힌트:**
- `snapshotFlow { counter }`로 시작
- `.filter { it % 5 == 0 && it > 0 }` 사용
- `.collect`에서 알림 상태 업데이트

### 연습 2: 폼 자동저장 (중간)

이름과 이메일 입력 필드가 있습니다.
입력 후 1초 동안 변경이 없으면 자동으로 저장(로그 출력)합니다.

**힌트:**
- 두 필드를 data class로 묶어서 관찰
- `debounce(1000)` 사용
- `distinctUntilChanged()` 추가

### 연습 3: 스크롤 분석 추적기 (어려움)

LazyColumn의 아이템 중 사용자가 **처음으로 본 아이템**을 추적합니다.
이미 본 아이템은 중복 기록하지 않고, "분석 로그" 목록에 표시합니다.

**힌트:**
- `listState.layoutInfo.visibleItemsInfo` 활용
- Set이나 List로 이미 본 아이템 추적
- snapshotFlow로 visible items 변경 관찰

---

## 다음 학습

- [derivedStateOf](../derived_state_of/) - 파생 상태로 Recomposition 최적화
- [State Hoisting](../state_hoisting/) - 상태 끌어올리기 패턴
- [remember](../remember/) - 상태 기억하기 기초

---

## 참고 자료

- [공식 문서: Side-effects in Compose](https://developer.android.com/develop/ui/compose/side-effects)
- [Snapshot API Reference](https://developer.android.com/reference/kotlin/androidx/compose/runtime/snapshots/Snapshot)
- [Introduction to the Compose Snapshot system](https://blog.zachklipp.com/introduction-to-the-compose-snapshot-system/)
