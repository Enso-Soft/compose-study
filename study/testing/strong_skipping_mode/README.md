# Strong Skipping Mode 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `Recomposition` | Compose의 UI 재구성 과정 | [📚 학습하기](../../state/recomposition/README.md) |
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Strong Skipping Mode는 Compose 컴파일러의 성능 최적화 옵션으로, **불안정한(unstable) 파라미터를 가진 Composable도 자동으로 스킵**할 수 있게 해주는 기능입니다.

> Kotlin 2.0.20+에서는 **기본으로 활성화**되어 있습니다.

---

## 핵심 특징

### 1. Unstable 파라미터도 스킵 가능

이전에는 `List`, `Map`, `var`를 포함한 클래스 등 "불안정한" 타입의 파라미터가 있으면 Composable이 **항상 Recomposition**되었습니다. Strong Skipping이 활성화되면, **같은 인스턴스(===)인지 확인**하여 스킵 여부를 결정합니다.

### 2. Lambda 자동 메모이제이션

Button의 `onClick`처럼 Lambda를 전달할 때, Strong Skipping은 **자동으로 `remember`로 감싸줍니다**. 개발자가 직접 `remember`를 추가할 필요가 없어집니다.

### 3. @Stable/@Immutable 수동 추가 불필요

이전에는 성능 최적화를 위해 data class에 `@Stable`이나 `@Immutable` 어노테이션을 추가해야 했지만, Strong Skipping이 활성화되면 **대부분의 경우 불필요**합니다.

---

## 비유로 이해하기

Strong Skipping은 **"똑똑한 비서"**와 같습니다:

- **이전 방식**: 서류가 오면 내용과 상관없이 무조건 다시 처리
- **Strong Skipping**: "이거 아까 본 그 서류 아니에요? 그럼 안 봐도 돼요!"

### Stable vs Unstable 비유

| 구분 | 비유 | 설명 |
|------|------|------|
| **Stable** | 봉인된 봉투 | 내용이 바뀌지 않음 → `equals()`로 비교 |
| **Unstable** | 열린 봉투 | 내용이 바뀔 수 있음 → Strong Skipping이 `===`로 비교 |

---

## 문제 상황: Unstable 파라미터로 인한 불필요한 Recomposition

### 시나리오

사용자 목록을 표시하는 화면이 있습니다. 상단에 카운터 버튼이 있고, 아래에 사용자 목록이 있습니다.

```kotlin
@Composable
fun UserListScreen(users: List<User>) {
    Column {
        users.forEach { user ->
            UserItem(user)
        }
    }
}
```

### 발생하는 문제 (Strong Skipping 없이)

1. `List<User>`는 Compose가 **unstable**로 판단
2. 상위에서 **어떤 상태라도 변경**되면 `UserListScreen`이 **매번 Recomposition**
3. `users` 인스턴스가 **같더라도** 스킵되지 않음
4. 불필요한 렌더링으로 **성능 저하**

---

## 해결책: Strong Skipping Mode

### Strong Skipping이 활성화되면

```kotlin
@Composable
fun UserListScreen(users: List<User>) {
    // users가 같은 인스턴스(===)면 스킵!
    Column {
        users.forEach { user ->
            UserItem(user)
        }
    }
}
```

### 동작 원리

| 파라미터 타입 | 비교 방식 | 설명 |
|--------------|----------|------|
| **Stable** (String, Int, @Stable 클래스) | `equals()` | 내용이 같으면 스킵 |
| **Unstable** (List, Map, 일반 클래스) | `===` | 같은 인스턴스면 스킵 |

---

## 활성화 방법

### Kotlin 2.0.20 이상

**기본으로 활성화**되어 있습니다. 별도 설정이 필요 없습니다.

### Kotlin 2.0.20 미만 (Compose Compiler 1.5.4+)

`build.gradle.kts`에 다음을 추가합니다:

```kotlin
android {
    // ...
}

composeCompiler {
    enableStrongSkippingMode = true
}
```

---

## Lambda 메모이제이션

Strong Skipping은 Lambda도 자동으로 메모이제이션합니다:

### 이전 (수동 remember 필요)

```kotlin
@Composable
fun MyComposable(onClick: () -> Unit) {
    val rememberedOnClick = remember { onClick }
    Button(onClick = rememberedOnClick) { ... }
}
```

### Strong Skipping 활성화 후 (자동 처리)

```kotlin
@Composable
fun MyComposable(onClick: () -> Unit) {
    // Lambda가 자동으로 remember됨!
    Button(onClick = onClick) { ... }
}
```

---

## Opt-out 방법

### @NonSkippableComposable

특정 Composable의 스킵을 비활성화하려면:

```kotlin
@NonSkippableComposable
@Composable
fun AlwaysRecompose() {
    // 이 Composable은 항상 Recomposition됨
}
```

### @DontMemoize

Lambda 메모이제이션을 비활성화하려면:

```kotlin
@Composable
fun MyComposable() {
    val lambda = @DontMemoize {
        // 이 Lambda는 메모이제이션되지 않음
    }
}
```

---

## 사용 시나리오

### 1. List/Map을 파라미터로 받는 경우

```kotlin
@Composable
fun ArticleList(articles: List<Article>) {
    // Strong Skipping: articles 인스턴스가 같으면 스킵
}
```

### 2. 콜백 Lambda를 전달하는 경우

```kotlin
@Composable
fun ItemRow(
    item: Item,
    onItemClick: (Item) -> Unit  // 자동 메모이제이션
) {
    // ...
}
```

### 3. ViewModel의 State를 사용하는 경우

```kotlin
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    // uiState가 같은 인스턴스면 스킵
}
```

---

## 주의사항

### 1. 인스턴스가 바뀌면 Recomposition됨

```kotlin
// 매번 새 List 생성 → 매번 Recomposition
val users = listOf(User(...), User(...))  // Bad!

// remember로 인스턴스 유지 → 스킵 가능
val users = remember { listOf(User(...), User(...)) }  // Good!
```

### 2. 객체 내부 변경은 감지 안 됨

```kotlin
class MutableUser(var name: String)

val user = remember { MutableUser("Kim") }
user.name = "Lee"  // UI 업데이트 안 됨! (같은 인스턴스)
```

### 3. 성능 이슈가 있을 때만 추가 최적화

Strong Skipping이 대부분의 상황을 처리합니다. **측정된 성능 이슈**가 있을 때만 `@Stable`, `@Immutable`을 고려하세요.

---

## Stable vs Unstable 타입 구분

| Stable (스킵 가능) | Unstable (Strong Skipping 필요) |
|-------------------|-------------------------------|
| `String`, `Int`, `Float` 등 primitive | `List`, `Map`, `Set` |
| `@Stable` 어노테이션된 클래스 | `var` 프로퍼티를 가진 클래스 |
| `@Immutable` 어노테이션된 클래스 | 외부 모듈의 클래스 |
| `Enum` | 함수 타입 (Lambda) |

---

## 연습 문제

### 연습 1: Stable/Unstable 구분하기 - 쉬움

다양한 타입이 주어졌을 때, 어떤 것이 Stable이고 어떤 것이 Unstable인지 구분해보세요.

### 연습 2: Recomposition 스킵 확인하기 - 중간

`SideEffect`를 사용하여 Recomposition 횟수를 카운트하고, Strong Skipping이 실제로 스킵하는지 확인해보세요.

### 연습 3: Lambda 메모이제이션 이해하기 - 어려움

Lambda가 외부 상태를 캡처할 때 Strong Skipping이 어떻게 처리하는지 분석해보세요.

---

## 다음 학습

- [Compose Compiler Metrics](../compose_compiler_metrics/README.md) - 컴파일러 리포트로 stability 분석
- [Stability](../../state/stability/README.md) - @Stable, @Immutable 심화 학습
