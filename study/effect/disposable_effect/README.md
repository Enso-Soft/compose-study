# DisposableEffect 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `launched_effect` | Composable 내에서 비동기 작업 실행 | [📚 학습하기](../../effect/launched_effect/README.md) |
| `side_effect` | Side Effect 개념과 Compose에서의 의미 | [📚 학습하기](../../effect/side_effect/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`DisposableEffect`는 **Composable이 Composition을 떠날 때 정리(cleanup)가 필요한 Side Effect**를 위한 API입니다.

```kotlin
DisposableEffect(key1, key2, ...) {
    // 설정 코드 (Composition 진입 시)

    onDispose {
        // 정리 코드 (Composition 떠날 때)
    }
}
```

## 핵심 특징

1. **onDispose 블록 필수**: 반드시 정리 코드를 제공해야 함
2. **key 변경 시**: 기존 onDispose 실행 → 새로운 effect 실행
3. **Composition 떠날 때**: onDispose 자동 실행
4. **리소스 관리**: 리스너, 콜백, 구독 등의 등록/해제에 적합

---

## Side Effect API 비교

| 특성 | LaunchedEffect | DisposableEffect | LifecycleEventEffect |
|------|---------------|------------------|---------------------|
| 용도 | 코루틴 실행 | 리소스 정리 | 라이프사이클 이벤트 처리 |
| 정리 방식 | 자동 (코루틴 취소) | 수동 (onDispose) | 자동 (내부 처리) |
| 실행 타이밍 | 비동기 (코루틴) | 동기 | 동기 |
| 예시 | API 호출, delay | 리스너, 콜백 등록 | ON_RESUME, ON_PAUSE 감지 |
| 요구 버전 | Compose 1.0+ | Compose 1.0+ | Lifecycle 2.7.0+ |

> **참고**: `LifecycleEventEffect`는 `DisposableEffect` + `LifecycleEventObserver` 패턴을 간소화한 API입니다.

---

## 문제 상황: 정리 없이 리스너 등록

### 잘못된 코드 예시

```kotlin
@Composable
fun BadExample() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ❌ 문제: 등록만 하고 해제하지 않음!
    val observer = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> println("Resumed")
            Lifecycle.Event.ON_PAUSE -> println("Paused")
            else -> {}
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)

    Text("Observer registered but never removed!")
}
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 메모리 누수 | 해제되지 않은 리스너가 메모리 점유 |
| 중복 등록 | Recomposition마다 새 리스너 추가 |
| 의도치 않은 동작 | 화면 떠나도 콜백 계속 실행 |

---

## 해결책: DisposableEffect 사용

### 올바른 코드

```kotlin
@Composable
fun GoodExample() {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> println("Resumed")
                Lifecycle.Event.ON_PAUSE -> println("Paused")
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // ✅ Composition 떠날 때 자동으로 호출됨
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Text("Observer properly managed!")
}
```

### 해결되는 이유

| 해결 | 설명 |
|------|------|
| 메모리 누수 방지 | onDispose에서 리스너 해제 |
| 중복 등록 방지 | key 기반으로 등록 관리 |
| 생명주기 동기화 | Composable 생명주기에 맞춰 정리 |

---

## 사용 시나리오

### 1. Lifecycle Observer

```kotlin
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event -> ... }
    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
    }
}
```

### 2. BroadcastReceiver

```kotlin
DisposableEffect(Unit) {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) { ... }
    }
    context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    onDispose {
        context.unregisterReceiver(receiver)
    }
}
```

### 3. 콜백 등록/해제

```kotlin
DisposableEffect(callback) {
    someService.registerCallback(callback)

    onDispose {
        someService.unregisterCallback(callback)
    }
}
```

---

## rememberUpdatedState와 함께 사용하기

콜백 람다가 외부에서 전달될 때, `rememberUpdatedState`를 사용하여 항상 최신 콜백을 참조해야 합니다.

### 왜 필요한가?

DisposableEffect는 key가 변경되지 않으면 재실행되지 않습니다. 하지만 콜백 람다가 변경될 수 있다면, 오래된 콜백이 호출될 위험이 있습니다.

### 올바른 패턴

```kotlin
@Composable
fun HomeScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit,  // 외부에서 전달되는 콜백
    onStop: () -> Unit
) {
    // 항상 최신 콜백을 참조하도록 보장
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnStop by rememberUpdatedState(onStop)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> currentOnStart()
                Lifecycle.Event.ON_STOP -> currentOnStop()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
```

---

## 최신 대안 API (Lifecycle 2.7.0+)

Lifecycle 2.7.0부터 라이프사이클 이벤트 처리를 위한 간소화된 API가 추가되었습니다.

### LifecycleEventEffect

특정 라이프사이클 이벤트에 반응:

```kotlin
LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
    // ON_RESUME 시 실행할 코드
    println("화면이 활성화됨")
}
```

### LifecycleStartEffect / LifecycleResumeEffect

시작-정지 쌍을 자동으로 처리:

```kotlin
// ON_START와 ON_STOP 쌍 처리
LifecycleStartEffect(Unit) {
    // ON_START 시 실행
    startSomething()

    onStopOrDispose {
        // ON_STOP 또는 Composition 떠날 때 실행
        stopSomething()
    }
}

// ON_RESUME과 ON_PAUSE 쌍 처리
LifecycleResumeEffect(Unit) {
    // ON_RESUME 시 실행
    resumeSomething()

    onPauseOrDispose {
        // ON_PAUSE 또는 Composition 떠날 때 실행
        pauseSomething()
    }
}
```

### 언제 무엇을 사용할까?

| 상황 | 권장 API |
|------|---------|
| 단순 라이프사이클 이벤트 감지 | LifecycleEventEffect |
| START/STOP 쌍 처리 | LifecycleStartEffect |
| RESUME/PAUSE 쌍 처리 | LifecycleResumeEffect |
| 커스텀 리스너/콜백 관리 | DisposableEffect |
| BroadcastReceiver 등록 | DisposableEffect |

---

## 피해야 할 상황

### 1. suspend 함수가 필요한 경우

```kotlin
// ❌ DisposableEffect 내부에서 suspend 함수 사용 불가
DisposableEffect(key) {
    fetchData() // 컴파일 에러!
    onDispose { }
}

// ✅ LaunchedEffect 사용
LaunchedEffect(key) {
    fetchData() // suspend 함수 사용 가능
}
```

### 2. key가 빈번하게 변경되는 경우

```kotlin
// ❌ 매 프레임마다 재등록/해제 발생 (성능 저하)
DisposableEffect(animationProgress) {
    registerListener()
    onDispose { unregisterListener() }
}

// ✅ 안정적인 key 사용
DisposableEffect(Unit) {
    registerListener()
    onDispose { unregisterListener() }
}
```

### 3. State 업데이트

```kotlin
// ❌ DisposableEffect 내에서 직접 State 업데이트 (불필요한 recomposition 유발)
DisposableEffect(key) {
    someState = newValue // 피해야 함
    onDispose { }
}

// ✅ 콜백을 통해 간접적으로 업데이트
DisposableEffect(key) {
    val callback = { newValue: String ->
        someState = newValue
    }
    registerCallback(callback)
    onDispose { unregisterCallback(callback) }
}
```

---

## 주의사항

### onDispose는 필수!

```kotlin
// ❌ 컴파일 에러: onDispose가 없음
DisposableEffect(key) {
    doSomething()
}

// ✅ 올바른 사용
DisposableEffect(key) {
    doSomething()
    onDispose { } // 정리할 게 없어도 필수
}
```

### key 선택이 중요!

```kotlin
// ❌ 불필요하게 자주 재등록
DisposableEffect(someFrequentlyChangingValue) { ... }

// ✅ 적절한 key 선택
DisposableEffect(stableKey) {
    val callback = { /* stableKey 사용 */ }
    register(callback)
    onDispose { unregister(callback) }
}
```

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | 잘못된 코드 - 정리 없이 등록 |
| `Solution.kt` | 올바른 코드 - DisposableEffect 사용 |
| `Practice.kt` | 연습 문제 3개 (라이프사이클, 타이머, 스크롤) |

---

## 연습 문제

1. **라이프사이클 관찰**: LifecycleEventObserver 등록/해제
2. **백그라운드 체크**: 앱이 백그라운드로 가면 타이머 일시정지
3. **스크롤 이벤트**: 스크롤 위치 변경 시 콜백 등록/해제

---

## 다음 학습

- `rememberCoroutineScope`: 사용자 이벤트에 의한 코루틴 실행
- `SideEffect`: 매 Recomposition마다 실행해야 할 작업
