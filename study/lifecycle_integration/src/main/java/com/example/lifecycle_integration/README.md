# Lifecycle Integration 학습

## 개념

**Lifecycle Integration**은 Compose에서 Android Activity/Fragment의 **Lifecycle 이벤트**에 반응하는 방법입니다.

Lifecycle 2.7.0부터 도입된 새로운 API들로 기존의 복잡한 패턴을 간결하게 대체할 수 있습니다.

```kotlin
// 새로운 API (Lifecycle 2.7.0+)
LifecycleStartEffect(key) {
    // ON_START에서 실행
    onStopOrDispose {
        // ON_STOP 또는 Composable 제거 시 실행
    }
}
```

## 핵심 API

| API | 트리거 이벤트 | 정리 이벤트 | 사용 사례 |
|-----|-------------|------------|----------|
| `LifecycleStartEffect` | ON_START | ON_STOP | 카메라, 센서, 화면 분석 |
| `LifecycleResumeEffect` | ON_RESUME | ON_PAUSE | 정밀한 체류 시간, 포커스 관리 |
| `LifecycleEventEffect` | 지정한 이벤트 | 없음 | 단방향 이벤트 (로깅 등) |
| `currentStateAsState()` | - | - | 상태 기반 조건부 렌더링 |

---

## 문제 상황: 기존 방식의 복잡성

### 잘못된(복잡한) 코드 예시

```kotlin
@Composable
fun OldWay() {
    val lifecycleOwner = LocalLifecycleOwner.current

    // 많은 보일러플레이트 코드!
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> camera.start()
                Lifecycle.Event.ON_STOP -> camera.stop()
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

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 보일러플레이트 | Observer 생성, 등록, 해제 코드가 장황함 |
| 누락 위험 | Observer 해제를 잊기 쉬움 |
| Stale Closure | rememberUpdatedState 미사용 시 오래된 값 참조 |
| 가독성 저하 | 실제 로직보다 설정 코드가 더 많음 |

---

## 해결책: 새로운 Lifecycle API 사용

### 1. LifecycleStartEffect (START/STOP 쌍)

```kotlin
@Composable
fun CameraPreview() {
    LifecycleStartEffect(Unit) {
        camera.start()  // ON_START

        onStopOrDispose {
            camera.stop()  // ON_STOP 또는 Composable 제거
        }
    }
}
```

### 2. LifecycleResumeEffect (RESUME/PAUSE 쌍)

```kotlin
@Composable
fun TimeTracker() {
    var startTime by remember { mutableLongStateOf(0L) }
    var totalTime by remember { mutableLongStateOf(0L) }

    LifecycleResumeEffect(Unit) {
        startTime = System.currentTimeMillis()

        onPauseOrDispose {
            totalTime += System.currentTimeMillis() - startTime
        }
    }
}
```

### 3. LifecycleEventEffect (단일 이벤트)

```kotlin
@Composable
fun AnalyticsScreen(screenName: String) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        analytics.logScreenView(screenName)
    }
}
```

### 4. currentStateAsState (상태 관찰)

```kotlin
@Composable
fun AdaptiveContent() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()

    if (lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {
        ExpensiveAnimation()  // 화면이 보일 때만
    } else {
        StaticPlaceholder()   // 백그라운드에서는 간단한 UI
    }
}
```

### 해결되는 이유

| 해결 | 설명 |
|------|------|
| 보일러플레이트 제거 | Observer 수동 관리 불필요 |
| 정리 로직 강제 | `onStopOrDispose`/`onPauseOrDispose` 블록 필수 |
| 가독성 향상 | 의도가 명확한 함수명 |
| Key 기반 재시작 | key 변경 시 자동 재시작/정리 |

---

## 사용 시나리오

### 1. 카메라 프리뷰
```kotlin
LifecycleStartEffect(cameraId) {
    cameraController.bind(cameraId)
    onStopOrDispose { cameraController.unbind() }
}
```

### 2. 화면 체류 시간 측정
```kotlin
LifecycleResumeEffect(screenName) {
    val startMs = System.currentTimeMillis()
    onPauseOrDispose {
        analytics.logDuration(screenName, System.currentTimeMillis() - startMs)
    }
}
```

### 3. 센서 리스너
```kotlin
LifecycleResumeEffect(sensorType) {
    sensorManager.registerListener(listener, sensor, delay)
    onPauseOrDispose { sensorManager.unregisterListener(listener) }
}
```

### 4. 화면 분석 로그
```kotlin
LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
    analytics.logScreenView(screenName)
}
```

---

## 주의사항

### key가 필수!

```kotlin
// key 없이 호출하면 컴파일 에러
LifecycleStartEffect { ... }  // 에러!

// 최소 하나의 key 필요
LifecycleStartEffect(Unit) { ... }  // OK
LifecycleStartEffect(userId) { ... }  // OK - userId 변경 시 재시작
```

### Lifecycle 2.7.0 이상 필요

```kotlin
// build.gradle.kts
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
```

### currentStateAsState vs LifecycleEffect

```kotlin
// 상태 기반 조건부 렌더링 → currentStateAsState
val state by lifecycle.currentStateAsState()
if (state.isAtLeast(RESUMED)) { ... }

// 이벤트 기반 Side Effect → LifecycleEffect
LifecycleResumeEffect(Unit) {
    doSomething()
    onPauseOrDispose { cleanup() }
}
```

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | 기존 방식 - DisposableEffect + LifecycleEventObserver |
| `Solution.kt` | 새 API - LifecycleStartEffect, LifecycleResumeEffect 등 |
| `Practice.kt` | 연습 문제 3개 |

---

## 연습 문제

1. **화면 체류 시간 측정기**: LifecycleResumeEffect로 정확한 체류 시간 측정
2. **센서 시뮬레이터**: LifecycleStartEffect로 센서 연결/해제 관리
3. **상태 기반 UI**: currentStateAsState로 조건부 렌더링

---

## 다음 학습

- `Animation`: Compose 애니메이션 시스템
- `Custom Effects`: 자신만의 Effect 만들기
