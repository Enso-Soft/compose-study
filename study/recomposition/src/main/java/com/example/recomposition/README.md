# Recomposition 이해

## 개념

**Recomposition**은 Compose에서 **상태가 변경될 때 UI를 업데이트하는 과정**입니다. Compose는 상태 변경을 감지하면 해당 상태를 읽는 Composable 함수들만 **선택적으로 다시 호출**합니다.

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    // count가 변경되면 이 Composable이 "recompose"됨
    Text("Count: $count")

    Button(onClick = { count++ }) {
        Text("+1")
    }
}
```

## Compose의 3단계

```
1. Composition (구성)
   - Composable 함수 실행
   - UI 트리 구축
   - 상태 읽기 발생

2. Layout (레이아웃)
   - 각 요소의 크기 측정
   - 위치 배치

3. Drawing (그리기)
   - 실제 픽셀 렌더링
```

**Recomposition은 1단계(Composition)에서 발생합니다.**

## 핵심 특징

### 1. Smart Recomposition

Compose는 **변경된 부분만** 다시 compose합니다:

```kotlin
@Composable
fun Parent() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: $count")   // count 읽음 -> recompose됨
        StaticText()             // count 안 읽음 -> 스킵 가능
    }
}

@Composable
fun StaticText() {
    Text("I never change")  // 파라미터가 동일하면 스킵됨
}
```

### 2. Skippable Composable

Compose가 Composable을 스킵하려면 **모든 파라미터가 안정(Stable)**해야 합니다:

| 타입 | 안정성 | 이유 |
|------|--------|------|
| `Int`, `String`, `Boolean` | Stable | 불변 원시 타입 |
| `data class` (val만) | Stable | 불변으로 추론 |
| `List`, `Map`, `Set` | **Unstable** | Kotlin 표준 컬렉션은 불변 보장 불가 |
| 외부 라이브러리 클래스 | **Unstable** | Compose가 분석 불가 |

### 3. Recomposition Scope

상태를 읽는 **가장 가까운 Composable 함수**가 recomposition 범위가 됩니다:

```kotlin
@Composable
fun Parent() {
    var count by remember { mutableStateOf(0) }

    Column {  // 이 Column 전체가 recomposition scope
        Text("Count: $count")  // count 읽음
        Text("Static")
        Button(onClick = { count++ }) {
            Text("+1")
        }
    }
}
```

## 문제 상황

### 문제 1: 불필요한 Recomposition

```kotlin
@Composable
fun BadExample() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: $count")

        // 문제: count와 무관한데 매번 다시 실행됨!
        ExpensiveCalculation()
    }
}
```

**발생하는 문제:**
- 성능 저하 (비용이 큰 계산이 매번 실행)
- UI 버벅임
- 배터리 소모 증가

### 문제 2: Unstable 파라미터

```kotlin
@Composable
fun UserList(users: List<User>) {  // List는 Unstable!
    users.forEach { user ->
        UserItem(user)  // users가 같아도 매번 recompose
    }
}
```

### 문제 3: Lambda로 인한 Recomposition

```kotlin
@Composable
fun ParentWithLambda() {
    var count by remember { mutableStateOf(0) }

    // 매 recomposition마다 새 람다 생성!
    ChildComponent(onClick = { doSomething() })
}
```

## 해결책

### 해결책 1: 상태 읽기 격리

```kotlin
@Composable
fun GoodExample() {
    var count by remember { mutableStateOf(0) }

    Column {
        // count 읽기를 별도 Composable로 격리
        CountDisplay(count)

        // 이제 count가 변해도 스킵됨
        ExpensiveCalculation()
    }
}

@Composable
fun CountDisplay(count: Int) {
    Text("Count: $count")
}
```

### 해결책 2: @Immutable / @Stable 사용

```kotlin
@Immutable
data class User(
    val id: String,
    val name: String
)

// 또는 kotlinx-collections-immutable 사용
fun UserList(users: ImmutableList<User>) { ... }
```

### 해결책 3: Lambda 캐싱

```kotlin
@Composable
fun OptimizedLambda() {
    // remember로 람다 캐싱
    val onClick = remember { { doSomething() } }

    ChildComponent(onClick = onClick)
}
```

### 해결책 4: 상태 읽기 지연 (Defer State Reads)

```kotlin
@Composable
fun AnimatedBox() {
    val offset by animateFloatAsState(targetValue = 100f)

    // 나쁜 예: Composition 단계에서 읽기
    // Box(modifier = Modifier.offset(x = offset.dp))

    // 좋은 예: Layout 단계로 읽기 지연
    Box(
        modifier = Modifier.offset {
            IntOffset(offset.toInt(), 0)  // 람다 사용
        }
    )
}
```

## 디버깅 도구

### 1. Layout Inspector

Android Studio의 Layout Inspector에서:
- **Recomposition counts** 확인 가능
- 어떤 Composable이 자주 recompose되는지 시각화

### 2. SideEffect로 로깅

```kotlin
@Composable
fun DebugComposable() {
    SideEffect {
        println("Recomposed!")  // 매 성공적인 composition 후 실행
    }
}
```

### 3. Compose Compiler 리포트

```kotlin
// build.gradle.kts
composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}
```

## 최적화 체크리스트

1. [ ] 상태를 읽는 곳을 최소 범위로 격리했는가?
2. [ ] 데이터 클래스에 @Immutable 또는 @Stable을 적용했는가?
3. [ ] 자주 변경되는 상태를 람다로 래핑하여 읽기를 지연했는가?
4. [ ] LazyColumn에 key 파라미터를 사용했는가?
5. [ ] remember로 비용이 큰 계산을 캐싱했는가?

## 다음 학습

- **Stability**: @Stable, @Immutable 어노테이션 심화
- **derivedStateOf**: 파생 상태로 recomposition 최소화
- **LazyList 최적화**: key, contentType 활용

## 참고 자료

- [Jetpack Compose Performance - Android Developers](https://developer.android.com/develop/ui/compose/performance)
- [Performance Best Practices - Android Developers](https://developer.android.com/develop/ui/compose/performance/bestpractices)
- [Thinking in Compose - Android Developers](https://developer.android.com/develop/ui/compose/mental-model)
- [Jetpack Compose Stability Explained](https://medium.com/androiddevelopers/jetpack-compose-stability-explained-79c10db270c8)
