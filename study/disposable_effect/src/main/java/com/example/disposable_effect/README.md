# DisposableEffect 학습

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

## LaunchedEffect vs DisposableEffect

| 특성 | LaunchedEffect | DisposableEffect |
|------|---------------|------------------|
| 용도 | 코루틴 실행 | 리소스 정리 |
| 정리 | 자동 (코루틴 취소) | 수동 (onDispose) |
| 예시 | API 호출, delay | 리스너, 콜백 등록 |

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
