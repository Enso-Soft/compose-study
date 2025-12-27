# Effect Handlers 심화 학습

## 개요

이 모듈에서는 Compose의 고급 Effect Handler 패턴을 학습합니다:
- **snapshotFlow**: State를 Flow로 변환하여 side effect 처리
- **rememberUpdatedState**: 장기 실행 effect 내에서 최신 값 유지
- **currentRecomposeScope**: Recomposition 디버깅
- **derivedStateOf vs snapshotFlow**: 상황에 맞는 도구 선택

---

## 1. snapshotFlow

### 개념

`snapshotFlow`는 Compose State를 Kotlin Flow로 변환하는 함수입니다.

```kotlin
snapshotFlow { someState.value }
    .collect { value ->
        // 상태 변경 시 실행되는 side effect
    }
```

### 비유

`snapshotFlow`는 **CCTV 녹화 시스템**과 같습니다:
- CCTV(snapshotFlow)가 특정 장소(State)를 모니터링
- 변화가 감지되면 녹화(Flow emit)
- 녹화된 영상은 나중에 분석(collect)

### 언제 사용하나?

| 상황 | snapshotFlow 사용 |
|------|------------------|
| 상태 변경 시 분석 이벤트 전송 | O |
| 상태 변경 시 로그 기록 | O |
| 상태 변경 시 토스트 표시 | O |
| 단순 UI 상태 파생 | X (derivedStateOf 사용) |

### 핵심 포인트

```kotlin
LaunchedEffect(Unit) {
    snapshotFlow { scrollState.firstVisibleItemIndex }
        .distinctUntilChanged()  // 중복 방지
        .collect { index ->
            analytics.logScrollPosition(index)
        }
}
```

**장점:**
- LaunchedEffect가 재시작되지 않음
- 상태 변경이 Flow로 안전하게 변환
- Flow 연산자(map, filter, debounce 등) 활용 가능

---

## 2. rememberUpdatedState

### 개념

`rememberUpdatedState`는 장기 실행 effect 내에서 항상 최신 값을 참조할 수 있게 해주는 함수입니다.

```kotlin
val currentCallback by rememberUpdatedState(callback)

LaunchedEffect(Unit) {
    delay(5000)
    currentCallback()  // 항상 최신 콜백!
}
```

### 비유

`rememberUpdatedState`는 **라이브 스트리밍 링크**와 같습니다:
- 녹화된 영상(일반 캡처)은 촬영 당시 모습만 보여줌
- 라이브 링크(rememberUpdatedState)는 항상 현재 방송 내용을 보여줌

### 언제 사용하나?

| 상황 | rememberUpdatedState 사용 |
|------|--------------------------|
| 타이머 완료 후 콜백 실행 | O |
| 일정 시간 후 알림 표시 | O |
| LaunchedEffect(Unit) + 긴 delay | O |
| 짧은 작업, 즉시 완료 | X |

### 핵심 패턴

```kotlin
@Composable
fun TimerScreen(onTimeout: () -> Unit) {
    // 항상 최신 onTimeout 참조
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(Unit) {  // Unit = 재시작 안 함
        delay(5000)
        currentOnTimeout()  // 5초 후 최신 콜백 호출
    }
}
```

---

## 3. derivedStateOf vs snapshotFlow

### 핵심 차이

| 특성 | derivedStateOf | snapshotFlow |
|------|---------------|--------------|
| 용도 | UI 상태 파생 | Side Effect 처리 |
| 반환 타입 | State<T> | Flow<T> |
| Recomposition | 결과 변경 시만 트리거 | 트리거하지 않음 |
| 비용 | 약간 비쌈 | 일반적 |

### 황금률

> **UI 상태 파생 -> derivedStateOf**
> **Side Effect 처리 -> snapshotFlow**

### 예시: 스크롤 위치 기반 기능

```kotlin
// UI 상태: "맨 위로" 버튼 표시 여부
val showScrollToTop by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 5 }
}

// Side Effect: 스크롤 위치 분석
LaunchedEffect(Unit) {
    snapshotFlow { listState.firstVisibleItemIndex }
        .distinctUntilChanged()
        .collect { index ->
            analytics.logScrollDepth(index)
        }
}
```

### 잘못된 사용 예

```kotlin
// BAD: derivedStateOf로 side effect 처리 불가
val showBanner by remember {
    derivedStateOf { scrollIndex > 10 }
}
// 분석 이벤트는 어떻게 보내지? -> derivedStateOf로는 불가능!

// BAD: 단순 파생에 snapshotFlow 사용 (오버헤드)
LaunchedEffect(Unit) {
    snapshotFlow { scrollIndex > 10 }
        .collect { showBanner = it }  // 불필요한 복잡성
}
```

---

## 4. currentRecomposeScope 활용

### 개념

`currentRecomposeScope`는 현재 Recomposition scope 정보를 제공합니다. 디버깅에 유용합니다.

```kotlin
@Composable
inline fun LogRecomposition(tag: String) {
    val scope = currentRecomposeScope
    SideEffect {
        Log.d("Recomposition", "$tag: $scope")
    }
}
```

### 디버깅 활용

```kotlin
@Composable
fun MyScreen() {
    LogRecomposition("MyScreen")  // Recomposition 추적

    var count by remember { mutableIntStateOf(0) }

    Column {
        LogRecomposition("Column")
        Text("Count: $count")
        Button(onClick = { count++ }) {
            LogRecomposition("Button")
            Text("Increment")
        }
    }
}
```

### 출력 예시

```
Recomposition: MyScreen: RecomposeScopeImpl@894fab8
Recomposition: Column: RecomposeScopeImpl@894fab8  <- 같은 scope!
Recomposition: Button: RecomposeScopeImpl@12ab3cd  <- 다른 scope
```

---

## 5. 복잡한 Effect 조합 패턴

### 패턴 1: snapshotFlow + debounce (검색 최적화)

```kotlin
@Composable
fun SearchScreen(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300)  // 300ms 대기
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collect { searchQuery ->
                onSearch(searchQuery)
            }
    }

    TextField(value = query, onValueChange = { query = it })
}
```

### 패턴 2: rememberUpdatedState + snapshotFlow

```kotlin
@Composable
fun NotificationScreen(
    message: String,
    onNotify: (String) -> Unit
) {
    val currentMessage by rememberUpdatedState(message)
    val currentOnNotify by rememberUpdatedState(onNotify)

    var triggerCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        snapshotFlow { triggerCount }
            .filter { it > 0 }
            .collect {
                currentOnNotify(currentMessage)  // 최신 메시지, 최신 콜백
            }
    }

    Button(onClick = { triggerCount++ }) {
        Text("Notify")
    }
}
```

### 패턴 3: derivedStateOf + snapshotFlow 조합

```kotlin
@Composable
fun SmartListScreen() {
    val listState = rememberLazyListState()

    // UI 상태 (derivedStateOf)
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 5 }
    }

    // Side Effect (snapshotFlow)
    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { it / 10 }  // 10개 단위로 그룹화
            .distinctUntilChanged()
            .collect { section ->
                analytics.logSection(section)
            }
    }

    // UI
    Box {
        LazyColumn(state = listState) { /* items */ }
        AnimatedVisibility(visible = showFab) {
            FloatingActionButton(onClick = { /* scroll to top */ }) {
                Icon(Icons.Default.KeyboardArrowUp, "Top")
            }
        }
    }
}
```

---

## 6. Effect 디버깅 및 트러블슈팅

### 문제 1: LaunchedEffect가 계속 재시작됨

**증상:** Effect가 예상보다 자주 실행됨

**원인:** key가 너무 자주 변경됨

```kotlin
// BAD: 매 상태 변경마다 재시작
LaunchedEffect(scrollIndex) {
    // scrollIndex가 바뀔 때마다 취소 후 재실행
}

// GOOD: snapshotFlow 사용
LaunchedEffect(Unit) {
    snapshotFlow { scrollIndex }
        .collect { /* 재시작 없이 변경 감지 */ }
}
```

### 문제 2: 오래된 콜백/값 참조 (Stale Closure)

**증상:** 5초 전의 값이 사용됨

```kotlin
// BAD: 오래된 값 캡처
LaunchedEffect(Unit) {
    delay(5000)
    onTimeout()  // 5초 전의 onTimeout!
}

// GOOD: rememberUpdatedState 사용
val currentOnTimeout by rememberUpdatedState(onTimeout)
LaunchedEffect(Unit) {
    delay(5000)
    currentOnTimeout()  // 최신 onTimeout!
}
```

### 문제 3: 불필요한 Recomposition

**증상:** 성능 저하, 버벅임

```kotlin
// BAD: 매 스크롤마다 Recomposition
val showButton = listState.firstVisibleItemIndex > 0

// GOOD: derivedStateOf로 최적화
val showButton by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 0 }
}
```

### 디버깅 체크리스트

1. **LaunchedEffect key 확인**: 너무 자주 변경되는가?
2. **rememberUpdatedState 필요성**: 긴 delay 후 값 사용하는가?
3. **derivedStateOf 적절성**: 입력보다 출력 변경이 적은가?
4. **snapshotFlow 사용처**: Side effect인가, UI 상태인가?

---

## 연습 문제

### 연습 1: 검색어 디바운싱
snapshotFlow와 debounce를 사용하여 검색어 입력 최적화를 구현하세요.

### 연습 2: 동적 메시지 타이머
rememberUpdatedState를 사용하여 5초 후 최신 메시지로 알림을 표시하세요.

### 연습 3: UI 상태 vs Side Effect 분리
derivedStateOf와 snapshotFlow를 적절히 조합하여 스크롤 기반 UI/분석을 구현하세요.

---

## 다음 학습

- [produceState](../produce_state/src/main/java/com/example/produce_state/README.md): 외부 데이터 → State 변환
- [stability](../stability/src/main/java/com/example/stability/README.md): Recomposition 최적화
- [recomposition](../recomposition/src/main/java/com/example/recomposition/README.md): Recomposition 심층 이해

---

## 참고 자료

- [Side-effects in Compose - Android Developers](https://developer.android.com/develop/ui/compose/side-effects)
- [snapshotFlow - droidcon 2025](https://www.droidcon.com/2025/07/22/snapshotflow-or-collectasstate-how-to-pick-the-right-tool-for-jetpack-compose/)
- [Jetpack Compose Debugging Recomposition](https://medium.com/androiddevelopers/jetpack-compose-debugging-recomposition-bfcf4a6f8d37)
