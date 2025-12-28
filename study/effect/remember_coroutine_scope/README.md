# rememberCoroutineScope 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `launched_effect` | Composable 내에서 suspend 함수를 실행하는 Side Effect API | [📚 학습하기](../../effect/launched_effect/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`rememberCoroutineScope`는 Composable 함수 내에서 **이벤트 핸들러**(버튼 클릭 등)에서 코루틴을 실행할 때 사용하는 Side Effect API입니다.

```kotlin
@Composable
fun Example() {
    val scope = rememberCoroutineScope()  // Composition에 바인딩된 스코프

    Button(onClick = {
        scope.launch {
            // suspend 함수 호출 가능
            api.sendData()
        }
    }) {
        Text("Send")
    }
}
```

## 핵심 특징

1. **Composition에 바인딩**: 스코프는 Composable의 생명주기에 연결됨
2. **자동 취소**: Composable이 Composition에서 떠나면 모든 코루틴 자동 취소
3. **이벤트 기반 실행**: `LaunchedEffect`와 달리 명시적으로 `launch`를 호출해야 실행
4. **메모리 누수 방지**: 화면 이탈 시 실행 중인 코루틴 자동 정리
5. **구조화된 동시성(Structured Concurrency)**: 부모-자식 관계로 코루틴을 관리하여, 부모가 취소되면 모든 자식도 취소됨

> **구조화된 동시성이란?**
> 코루틴이 무질서하게 실행되지 않고, 명확한 범위(scope) 안에서 관리되는 것을 의미합니다.
> `rememberCoroutineScope`는 Composable의 생명주기를 부모로 삼아, 해당 Composable이 사라지면
> 그 안에서 실행된 모든 코루틴도 함께 정리됩니다.

## LaunchedEffect vs rememberCoroutineScope

| 특성 | LaunchedEffect | rememberCoroutineScope |
|------|----------------|------------------------|
| 실행 시점 | **자동** (Composition 진입 또는 key 변경 시) | **수동** (launch 호출 시) |
| 사용 목적 | 초기 데이터 로드, key 기반 작업 | 버튼 클릭, 사용자 이벤트 처리 |
| 예시 | API에서 사용자 정보 로드 | 버튼 클릭 시 메시지 전송 |

```kotlin
// LaunchedEffect: 화면 진입 시 자동 실행
LaunchedEffect(userId) {
    userData = api.fetchUser(userId)
}

// rememberCoroutineScope: 버튼 클릭 시 수동 실행
val scope = rememberCoroutineScope()
Button(onClick = {
    scope.launch { api.sendMessage() }
})
```

## 문제 상황: rememberCoroutineScope 없이 코루틴 실행

### 잘못된 코드 예시

```kotlin
// 1. onClick에서 suspend 함수 직접 호출 불가
Button(onClick = {
    delay(1000)  // 컴파일 에러! onClick은 suspend 함수가 아님
})

// 2. GlobalScope 사용 - 메모리 누수!
Button(onClick = {
    GlobalScope.launch {
        api.sendData()  // 화면 이탈 후에도 계속 실행됨
    }
})

// 3. 직접 CoroutineScope 생성 - 취소 불가
val scope = CoroutineScope(Dispatchers.Main)
Button(onClick = {
    scope.launch { ... }  // Composable 제거 후에도 계속 실행
})
```

### 발생하는 문제점

1. **컴파일 에러**: 이벤트 핸들러는 일반 함수이므로 suspend 함수 호출 불가
2. **메모리 누수**: GlobalScope나 직접 생성한 스코프는 Composable 생명주기와 무관하게 실행
3. **불필요한 작업**: 화면을 떠난 후에도 네트워크 요청이 계속 진행됨

## 해결책: rememberCoroutineScope 사용

### 올바른 코드

```kotlin
@Composable
fun SafeCoroutineExample() {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Button(
        onClick = {
            scope.launch {
                isLoading = true
                delay(2000)  // 네트워크 요청 시뮬레이션
                isLoading = false
            }
        },
        enabled = !isLoading
    ) {
        Text(if (isLoading) "Loading..." else "Send")
    }
}
```

### 해결되는 이유

1. **Composition 바인딩**: 스코프가 Composable 생명주기에 연결됨
2. **자동 취소**: 화면 이탈 시 실행 중인 코루틴이 자동으로 취소됨
3. **구조화된 동시성**: 안전한 코루틴 관리

## 사용 시나리오

### 1. Snackbar 표시
```kotlin
@Composable
fun SnackbarExample(snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            snackbarHostState.showSnackbar("작업 완료!")
        }
    }) {
        Text("Show Message")
    }
}
```

### 2. 애니메이션 제어
```kotlin
@Composable
fun AnimationExample() {
    val scope = rememberCoroutineScope()
    val animatable = remember { Animatable(0f) }

    Button(onClick = {
        scope.launch {
            animatable.animateTo(1f, animationSpec = tween(500))
        }
    }) {
        Text("Animate")
    }
}
```

### 3. 취소 가능한 작업
```kotlin
@Composable
fun CancellableTask() {
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    Row {
        Button(onClick = {
            job = scope.launch {
                // 장시간 작업
            }
        }) { Text("Start") }

        Button(onClick = { job?.cancel() }) {
            Text("Cancel")
        }
    }
}
```

## 주의사항

1. **Composition 본문에서 launch 금지**
   ```kotlin
   // 금지! 무한 루프 발생
   val scope = rememberCoroutineScope()
   scope.launch { ... }  // Recomposition마다 실행됨
   ```

2. **비즈니스 로직은 ViewModel에서**
   ```kotlin
   // 권장: ViewModel에서 처리
   viewModel.sendMessage()  // ViewModel 내부에서 viewModelScope 사용

   // UI 연동만 rememberCoroutineScope로
   scope.launch {
       snackbarHostState.showSnackbar("전송 완료")
   }
   ```

3. **예외 처리**
   ```kotlin
   scope.launch {
       try {
           api.sendData()
       } catch (e: Exception) {
           snackbarHostState.showSnackbar("오류: ${e.message}")
       }
   }
   ```

## 연습 문제

1. **기본**: 버튼 클릭 시 Snackbar 표시하기
2. **중급**: 카운트다운 타이머 구현하기 (시작/정지)
3. **심화**: Job을 저장하여 취소 가능한 작업 구현하기

## 다음 학습

- **DisposableEffect**: 리소스 정리가 필요한 Side Effect
- **SideEffect**: Compose 상태를 외부와 동기화
