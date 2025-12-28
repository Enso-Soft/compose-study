# Recomposition 이해

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | remember, mutableStateOf로 상태 저장 | [📚 학습하기](../../state/remember/README.md) |
| `composable_function` | @Composable 함수의 동작 원리 | [📚 학습하기](../../basics/composable_function/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Recomposition**은 Compose에서 **상태가 변경될 때 UI를 업데이트하는 과정**입니다. Compose는 상태 변경을 감지하면 해당 상태를 읽는 Composable 함수들만 **선택적으로 다시 호출**합니다.

> **왜 중요한가?**
> Recomposition을 이해하지 못하면 불필요한 UI 재구성이 발생하여 성능 문제, UI 버벅임, 배터리 소모가 증가합니다.
> 반대로 Recomposition을 잘 이해하고 최적화하면 부드럽고 효율적인 앱을 만들 수 있습니다.

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

Compose가 Composable을 스킵하려면 **모든 파라미터가 안정(Stable)**해야 합니다.

> **Stable(안정)이란?**
> Compose 컴파일러가 "이 값이 변경되지 않았다"고 확신할 수 있는 타입입니다.
> 안정적인 타입만 사용하면 Compose는 불필요한 recomposition을 건너뛸 수 있습니다.

| 타입 | 안정성 | 이유 |
|------|--------|------|
| `Int`, `String`, `Boolean` | Stable | 불변 원시 타입 |
| `data class` (val만) | Stable | 불변으로 추론 |
| `List`, `Map`, `Set` | **Unstable** | Kotlin 표준 컬렉션은 불변 보장 불가 |
| 외부 라이브러리 클래스 | **Unstable** | Compose가 분석 불가 |

> **왜 List는 Unstable인가?**
> `kotlin.collections.List`는 인터페이스일 뿐, 실제 구현체는 `MutableList`일 수 있습니다.
> Compose 컴파일러는 런타임에 내용이 변경될 가능성을 배제할 수 없어 Unstable로 판정합니다.

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

---

## 2025년 주요 업데이트

### Strong Skipping Mode

**Kotlin 2.0.20부터 기본 활성화**되는 새로운 컴파일러 모드입니다.

```kotlin
// 이전: Unstable 파라미터가 있으면 무조건 recompose
@Composable
fun UserCard(user: User) { ... }  // User가 Unstable이면 항상 recompose

// Strong Skipping: 참조 동등성(===)으로 체크
// 같은 인스턴스면 스킵 가능!
```

> **주의**: Strong Skipping은 **참조 동등성**만 체크합니다.
> 리스트를 필터링하거나 정렬하면 새 인스턴스가 생성되므로 recompose됩니다.
> 이런 경우에는 여전히 `@Immutable`/`@Stable` 어노테이션이 필요합니다.

### Pausable Composition (2025년 12월 릴리즈)

무거운 UI 작업을 여러 프레임에 나눠 실행하여 jank(버벅임)를 줄입니다.
LazyLayout prefetch에서 기본 활성화됩니다.

### @FrequentlyChangingValue

자주 변경되는 값(스크롤 위치, 애니메이션 값 등)을 Composition 단계에서 읽을 때
경고를 표시하는 새로운 lint 체크입니다.

---

## 상황별 최적화 기법 선택

어떤 최적화 기법을 사용해야 할지 모르겠다면, 아래 표를 참고하세요:

| 상황 | 권장 기법 |
|------|----------|
| 부모에서 상태를 읽어 자식까지 recompose되는 경우 | **상태 읽기 격리** - 별도 Composable로 분리 |
| 데이터 클래스가 Unstable로 판정되는 경우 | **@Immutable / @Stable** 어노테이션 |
| 애니메이션/스크롤처럼 자주 변경되는 상태 | **상태 읽기 지연** - 람다 버전 Modifier 사용 |
| 외부에서 전달받는 람다가 매번 새로 생성되는 경우 | **Lambda 캐싱** - remember 사용 |
| LazyColumn 아이템 순서가 변경되는 경우 | **key 파라미터** - 아이템 ID 사용 |

### 의사결정 플로우차트

```
Recomposition 문제 발생
        │
        ├── 부모 상태 변경 시 자식도 recompose?
        │         │
        │         └──Yes──► 상태 읽기 격리 (별도 Composable 분리)
        │
        ├── 데이터 클래스가 Unstable?
        │         │
        │         └──Yes──► @Immutable/@Stable 또는 Strong Skipping 활용
        │
        ├── 애니메이션/스크롤 값 읽기?
        │         │
        │         └──Yes──► 람다 버전 Modifier로 읽기 지연
        │
        └── LazyColumn 아이템 문제?
                  │
                  └──Yes──► key 파라미터 추가
```

---

## 최적화 체크리스트

1. [ ] 상태를 읽는 곳을 최소 범위로 격리했는가?
2. [ ] 데이터 클래스에 @Immutable 또는 @Stable을 적용했는가?
3. [ ] 자주 변경되는 상태를 람다로 래핑하여 읽기를 지연했는가?
4. [ ] LazyColumn에 key 파라미터를 사용했는가?
5. [ ] remember로 비용이 큰 계산을 캐싱했는가?
6. [ ] Release 모드에서 성능을 테스트했는가? (Debug 모드는 느림)
7. [ ] Strong Skipping Mode가 활성화되어 있는가? (Kotlin 2.0.20+)

## 다음 학습

- **Stability**: @Stable, @Immutable 어노테이션 심화
- **derivedStateOf**: 파생 상태로 recomposition 최소화
- **LazyList 최적화**: key, contentType 활용

## 참고 자료

### 공식 문서
- [Jetpack Compose Performance - Android Developers](https://developer.android.com/develop/ui/compose/performance)
- [Performance Best Practices - Android Developers](https://developer.android.com/develop/ui/compose/performance/bestpractices)
- [Thinking in Compose - Android Developers](https://developer.android.com/develop/ui/compose/mental-model)
- [Lifecycle of Composables - Android Developers](https://developer.android.com/develop/ui/compose/lifecycle)
- [Stability in Compose - Android Developers](https://developer.android.com/develop/ui/compose/performance/stability)

### 심화 자료
- [Jetpack Compose Stability Explained - Android Developers Blog](https://medium.com/androiddevelopers/jetpack-compose-stability-explained-79c10db270c8)
- [What's New in Jetpack Compose - Google I/O 2025](https://android-developers.googleblog.com/2025/05/whats-new-in-jetpack-compose.html)
- [Jetpack Compose December '25 Release](https://android-developers.googleblog.com/2025/12/whats-new-in-jetpack-compose-december.html)
