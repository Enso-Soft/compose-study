# Stability (안정성) 학습

## 개념

Compose의 **Stability(안정성)**는 Recomposition 최적화의 핵심 원리입니다.
Compose 컴파일러는 모든 타입을 **Stable(안정)** 또는 **Unstable(불안정)**으로 분류하고,
이 분류에 따라 Composable의 **skip 여부**를 결정합니다.

```kotlin
// Stable 타입: Compose가 변경 여부를 확실히 알 수 있음
@Stable
class Counter {
    var count by mutableStateOf(0)
}

// Immutable 타입: 생성 후 절대 변경되지 않음
@Immutable
data class User(val id: Int, val name: String)
```

## 핵심 특징

1. **Skip 메커니즘**: Stable 파라미터가 변경되지 않았으면 Composable 실행 생략
2. **컴파일 시점 결정**: Compose 컴파일러가 빌드 시 안정성 판단
3. **성능 최적화**: 불필요한 Recomposition 방지로 성능 향상
4. **계약 기반**: @Stable/@Immutable은 개발자와 컴파일러 간의 약속

## Stable vs Unstable

| 특성 | Stable 타입 | Unstable 타입 |
|------|------------|--------------|
| Skip 가능 | O (변경 없으면 skip) | X (항상 recompose) |
| 변경 감지 | 가능 | 불가능 |
| 예시 | Int, String, @Immutable class | List, Map, 외부 모듈 클래스 |

### 기본 Stable 타입

```kotlin
// 원시 타입 - 자동으로 Stable
val number: Int = 42
val text: String = "Hello"
val flag: Boolean = true

// Compose State 타입 - Stable (변경을 Compose가 추적)
val count = mutableStateOf(0)
val list = mutableStateListOf<String>()
val map = mutableStateMapOf<String, Int>()

// Lambda - Stable
val onClick: () -> Unit = { }
```

### Unstable로 판단되는 경우

```kotlin
// 1. Collection 타입 (List, Set, Map)
val users: List<User>  // Unstable - 내부가 변경될 수 있음

// 2. var 프로퍼티가 있는 data class
data class MutableUser(
    val name: String,
    var age: Int  // var가 있으면 Unstable
)

// 3. 외부 모듈/라이브러리의 클래스
// network 모듈의 ApiResponse -> Compose 모듈에서 Unstable

// 4. interface나 abstract class
interface Animal  // 구현체가 불변인지 알 수 없음
```

## @Immutable vs @Stable

### @Immutable

**생성 후 절대 변경되지 않음**을 보장합니다.

```kotlin
@Immutable
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// 사용 조건:
// 1. 모든 public 프로퍼티가 val
// 2. 모든 프로퍼티 타입이 불변
// 3. Collection 사용 시 ImmutableList 등 사용 권장
```

### @Stable

**변경될 수 있지만, Compose가 그 변경을 인지**함을 보장합니다.

```kotlin
@Stable
class UserState {
    var name by mutableStateOf("")  // MutableState로 변경 추적
    var age by mutableStateOf(0)
}

// 사용 조건:
// 1. 변경 가능한 프로퍼티는 MutableState 사용
// 2. equals가 호출될 때마다 동일한 결과 반환
// 3. 예상치 못한 변경이 없음
```

### 비교 표

| 특성 | @Immutable | @Stable |
|------|-----------|---------|
| 변경 가능 | X (절대 불가) | O (MutableState로) |
| 사용 시나리오 | 정적 데이터 | 반응형 상태 |
| 주의점 | 실제로 변경하면 UI 업데이트 안됨 | MutableState 없이 변경하면 감지 안됨 |

## 문제 상황: Unstable 타입으로 인한 불필요한 Recomposition

### 잘못된 코드 예시

```kotlin
// Unstable data class
data class User(val id: Int, val name: String)

@Composable
fun UserListScreen() {
    var counter by remember { mutableIntStateOf(0) }
    val users = remember { listOf(User(1, "Alice"), User(2, "Bob")) }

    Column {
        // 카운터 버튼
        Button(onClick = { counter++ }) {
            Text("Count: $counter")
        }

        // 문제: counter만 변경해도 UserList가 recompose됨!
        UserList(users = users)
    }
}

@Composable
fun UserList(users: List<User>) {  // List<User>는 Unstable
    users.forEach { user ->
        UserItem(user)  // 전부 다시 그려짐
    }
}
```

### 발생하는 문제점

1. `List<User>`가 Unstable이므로 Compose가 변경 여부를 판단 못함
2. 부모(UserListScreen)가 recompose될 때 UserList도 항상 recompose
3. counter만 변경해도 UserList와 모든 UserItem이 다시 실행
4. 리스트가 크면 심각한 성능 저하

## 해결책: @Immutable/@Stable 적용

### 방법 1: @Immutable 어노테이션

```kotlin
@Immutable
data class User(val id: Int, val name: String)

// 이제 User는 Stable로 취급됨
// UserItem(user)가 같은 User면 skip!
```

### 방법 2: Wrapper 클래스

```kotlin
@Immutable
data class UserListWrapper(val users: List<User>)

@Composable
fun UserList(wrapper: UserListWrapper) {
    wrapper.users.forEach { user ->
        UserItem(user)
    }
}
```

### 방법 3: ImmutableList 사용

```kotlin
// build.gradle.kts에 추가
// implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")

import kotlinx.collections.immutable.ImmutableList

@Composable
fun UserList(users: ImmutableList<User>) {
    // ImmutableList는 Stable로 취급됨
}
```

### 방법 4: @Stable 상태 클래스

```kotlin
@Stable
class UserListState {
    var users by mutableStateOf<List<User>>(emptyList())

    fun loadUsers(newUsers: List<User>) {
        users = newUsers
    }
}

@Composable
fun UserList(state: UserListState) {
    state.users.forEach { user ->
        UserItem(user)
    }
}
```

## 사용 시나리오

### 1. API 응답 모델

```kotlin
// 네트워크 응답 데이터 - 변경될 일 없음
@Immutable
data class Article(
    val id: Long,
    val title: String,
    val content: String,
    val author: Author
)

@Immutable
data class Author(
    val name: String,
    val avatarUrl: String
)
```

### 2. UI 상태 클래스

```kotlin
// 화면 상태 - 변경 가능하지만 Compose가 추적
@Stable
class FormState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    val isValid: Boolean
        get() = email.contains("@") && password.length >= 8
}
```

### 3. 설정/구성 데이터

```kotlin
@Immutable
data class ThemeConfig(
    val primaryColor: Color,
    val isDarkMode: Boolean,
    val fontSize: TextUnit
)
```

### 4. 선택 상태 관리

```kotlin
@Stable
class SelectionState<T> {
    private val _selectedItems = mutableStateListOf<T>()
    val selectedItems: List<T> get() = _selectedItems

    fun select(item: T) {
        if (item !in _selectedItems) _selectedItems.add(item)
    }

    fun deselect(item: T) {
        _selectedItems.remove(item)
    }

    fun isSelected(item: T) = item in _selectedItems
}
```

## 주의사항

### 1. 잘못된 @Immutable 사용

```kotlin
// 위험! 실제로 변경 가능한 데이터에 @Immutable 적용
@Immutable
data class User(
    val id: Int,
    var name: String  // var인데 @Immutable?
)

// 결과: name이 변경되어도 UI가 업데이트되지 않음!
```

### 2. @Stable 없이 var 사용

```kotlin
// 위험! MutableState 없이 변경
@Stable
class Counter {
    var count = 0  // MutableState가 아님!

    fun increment() {
        count++  // Compose가 감지 못함
    }
}
```

### 3. Collection 내부 변경

```kotlin
@Immutable
data class UserList(val users: List<User>)

// 위험! MutableList로 내부 변경 가능
val mutableList = mutableListOf(User(1, "Alice"))
val wrapper = UserList(mutableList)
mutableList.add(User(2, "Bob"))  // UI에 반영 안됨!
```

### 4. 외부 모듈 클래스

```kotlin
// 다른 모듈의 클래스는 자동으로 Unstable
// 해결: 해당 모듈에 @Immutable 추가하거나 Wrapper 사용
```

## Strong Skipping Mode (Compose 1.5.4+)

Compose 1.5.4부터 **Strong Skipping Mode**가 도입되었습니다.
이 모드를 활성화하면 Unstable 파라미터가 있어도 skip이 가능합니다.

```kotlin
// build.gradle.kts
composeCompiler {
    enableStrongSkippingMode = true
}
```

**주의**: Strong Skipping Mode는 람다의 equals 비교 방식을 변경하므로
기존 코드 동작이 달라질 수 있습니다. 신규 프로젝트에서 권장됩니다.

## 연습 문제

1. **@Immutable 적용**: data class에 @Immutable 추가해서 Recomposition 줄이기
2. **Wrapper 클래스**: List를 Wrapper로 감싸서 안정화하기
3. **@Stable 상태 클래스**: MutableState를 사용한 상태 클래스 만들기

## 다음 학습

- **derivedStateOf**: State 기반 파생 상태 최적화
- **Recomposition Scope**: Recomposition 범위 이해
- **Compose Compiler Reports**: 안정성 분석 리포트 활용

## 참고 자료

- [Stability in Compose - Android Developers](https://developer.android.com/develop/ui/compose/performance/stability)
- [Fix stability issues - Android Developers](https://developer.android.com/develop/ui/compose/performance/stability/fix)
- [Jetpack Compose Stability Explained - Medium](https://medium.com/androiddevelopers/jetpack-compose-stability-explained-79c10db270c8)
- [Understanding @Immutable and @Stable - Medium](https://medium.com/@dobri.kostadinov/understanding-immutable-and-stable-annotations-in-jetpack-compose-c71488fc7cdf)
- [Optimize App Performance By Mastering Stability - GetStream](https://getstream.io/blog/jetpack-compose-stability/)
