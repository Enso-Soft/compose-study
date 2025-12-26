# SideEffect 학습

## 개념

`SideEffect`는 Compose 상태를 **Compose가 관리하지 않는 외부 객체**와 동기화할 때 사용하는 Side Effect API입니다.

```kotlin
@Composable
fun UserProfile(user: User) {
    val analytics = remember { FirebaseAnalytics.getInstance() }

    SideEffect {
        // 매 성공적인 Recomposition 후 실행
        analytics.setUserProperty("userType", user.type)
    }

    Text("Welcome, ${user.name}")
}
```

## 핵심 특징

1. **Post-commit hook**: 성공적인 Recomposition이 완료된 **후에** 실행됩니다
2. **동기적 실행**: 메인 스레드에서 동기적으로 실행됩니다 (코루틴 아님)
3. **Key 없음**: LaunchedEffect, DisposableEffect와 달리 key 파라미터가 없습니다
4. **정리 콜백 없음**: DisposableEffect의 onDispose 같은 정리 메커니즘이 없습니다
5. **매번 실행**: 성공적인 Recomposition마다 실행됩니다 (한 번만 실행 X)

## 다른 Side Effect API와의 비교

| API | 실행 시점 | 코루틴 | 정리 | 용도 |
|-----|----------|--------|------|------|
| **SideEffect** | 매 Recomposition 후 | X | X | 외부 상태 동기화 |
| LaunchedEffect | key 변경 시 | O | 자동 취소 | 비동기 작업 |
| DisposableEffect | key 변경 시 | X | onDispose | 리소스 등록/해제 |
| rememberCoroutineScope | 이벤트 시 | O | Composable 종료 시 | 사용자 이벤트 |

## 문제 상황: Composable 본문에서 직접 외부 API 호출

### 잘못된 코드 예시

```kotlin
@Composable
fun UserProfile(user: User) {
    // Composable 본문에서 직접 호출
    analytics.setUserProperty("userType", user.type)  // 문제!

    Text("Welcome, ${user.name}")
}
```

### 발생하는 문제점

1. **예측 불가능한 실행**: Composable은 레이아웃 계산 중 여러 번 실행될 수 있음
2. **실패한 Composition에서도 실행**: Recomposition이 취소/실패해도 side effect 실행됨
3. **불안정한 상태**: Composition 중간에 실행되어 일관성 없는 상태 발생 가능
4. **디버깅 어려움**: 언제 실행되는지 예측하기 어려움

## 해결책: SideEffect 사용

### 올바른 코드

```kotlin
@Composable
fun UserProfile(user: User) {
    val analytics = remember { FirebaseAnalytics.getInstance() }

    SideEffect {
        // 성공적인 Recomposition 후에만 실행
        analytics.setUserProperty("userType", user.type)
    }

    Text("Welcome, ${user.name}")
}
```

### 해결되는 이유

1. **보장된 실행 시점**: 성공적인 Recomposition **후에만** 실행
2. **일관된 상태**: Composition이 완료된 후 실행되므로 상태가 안정적
3. **명확한 의도**: "이 코드는 side effect다"라는 것을 명시적으로 선언
4. **예측 가능한 동작**: 실행 시점을 정확히 알 수 있음

## 사용 시나리오

### 1. Analytics/Logging
```kotlin
SideEffect {
    analytics.setUserProperty("userType", user.type)
    analytics.logScreenView(screenName)
}
```

### 2. 외부 라이브러리 상태 동기화
```kotlin
SideEffect {
    externalSdk.updateState(currentValue)
}
```

### 3. 디버깅/모니터링
```kotlin
SideEffect {
    Log.d("Recomposition", "Component recomposed with state: $state")
}
```

### 4. Recomposition 횟수 추적
```kotlin
var recomposeCount by remember { mutableIntStateOf(0) }
SideEffect {
    recomposeCount++
}
```

## 주의사항

### SideEffect 내부에서 하면 안 되는 것

1. **suspend 함수 호출** - SideEffect는 코루틴이 아님
   ```kotlin
   // LaunchedEffect 사용하세요
   SideEffect {
       api.fetchData()  // 컴파일 에러!
   }
   ```

2. **무거운 연산** - 메인 스레드에서 동기 실행됨
   ```kotlin
   SideEffect {
       heavyComputation()  // UI 버벅임 발생!
   }
   ```

3. **Compose 상태 변경** - 무한 Recomposition 위험
   ```kotlin
   SideEffect {
       count++  // 무한 루프 위험!
   }
   ```

4. **한 번만 실행해야 하는 로직** - LaunchedEffect(Unit) 사용
   ```kotlin
   // LaunchedEffect(Unit) { } 사용하세요
   SideEffect {
       oneTimeSetup()  // 매번 실행됨!
   }
   ```

### 올바른 선택 가이드

| 상황 | 올바른 API |
|------|-----------|
| 비동기 작업 (API 호출, 애니메이션) | `LaunchedEffect` |
| 리소스 등록/해제 | `DisposableEffect` |
| 사용자 이벤트 처리 | `rememberCoroutineScope` |
| 외부 상태 동기화 (동기) | **`SideEffect`** |

## 연습 문제

### 연습 1: 기본 - 외부 카운터 동기화
외부 객체(SharedCounter)에 내부 카운터 값을 SideEffect로 동기화

### 연습 2: 중급 - Recomposition 로깅
외부 로깅 시스템에 Recomposition 정보를 SideEffect로 기록

### 연습 3: 고급 - 조건부 외부 상태 동기화
토글 상태에 따라 조건부로 외부 상태와 동기화

## 다음 학습

- **remember / mutableStateOf**: 상태 관리의 기초
- **rememberSaveable**: 화면 회전 시 상태 유지
- **State Hoisting**: 재사용 가능한 컴포넌트 설계
