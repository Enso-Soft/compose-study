# rememberSaveable 학습

## 개념

`rememberSaveable`은 `remember`의 확장 버전으로, **화면 회전(Configuration Change)** 이나 **프로세스 종료(Process Death)** 시에도 상태를 유지할 수 있게 해주는 Compose API입니다.

Android의 `onSaveInstanceState` 메커니즘을 활용하여 상태를 Bundle에 저장하고 복원합니다.

```kotlin
// 기본 사용법
var count by rememberSaveable { mutableStateOf(0) }
var text by rememberSaveable { mutableStateOf("") }
```

## remember vs rememberSaveable 비교

| 상황 | remember | rememberSaveable |
|------|----------|------------------|
| Recomposition | 유지 | 유지 |
| Configuration Change (화면 회전) | **초기화** | 유지 |
| Process Death (시스템에 의한 종료) | **초기화** | 유지 |
| 사용자가 앱 종료 (최근 앱에서 스와이프) | 초기화 | **초기화** |

## 핵심 특징

1. **Bundle 기반 저장**: Android의 saved instance state 메커니즘 사용
2. **기본 타입 자동 저장**: `Int`, `String`, `Boolean`, `Float`, `Long` 등
3. **복잡한 타입은 Saver 필요**: `@Parcelize`, `listSaver`, `mapSaver`, 커스텀 `Saver`
4. **Bundle 크기 제한**: 약 500KB~1MB (너무 큰 데이터는 저장 불가)

## 문제 상황: remember만 사용할 때

### 잘못된 코드 예시

```kotlin
@Composable
fun BrokenCounter() {
    // remember만 사용 - 화면 회전 시 상태 손실!
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}

@Composable
fun BrokenForm() {
    // 긴 텍스트 입력 후 화면 회전 → 모두 사라짐
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column {
        TextField(value = name, onValueChange = { name = it })
        TextField(value = email, onValueChange = { email = it })
    }
}
```

### 발생하는 문제점

1. **화면 회전 시 데이터 손실**: 사용자가 입력한 모든 내용이 사라짐
2. **사용자 경험 저하**: 긴 폼을 작성하던 중 실수로 회전하면 처음부터 다시 작성
3. **프로세스 종료 시 복구 불가**: 다른 앱 사용 후 돌아오면 상태 초기화

## 해결책: rememberSaveable

### 기본 타입 저장

```kotlin
@Composable
fun SaveableCounter() {
    // rememberSaveable 사용 - 화면 회전에도 상태 유지!
    var count by rememberSaveable { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Count: $count")  // 화면 회전 후에도 값 유지
    }
}
```

### 복잡한 타입 저장 방법

#### 1. @Parcelize 사용 (권장)

```kotlin
@Parcelize
data class User(
    val name: String,
    val age: Int
) : Parcelable

@Composable
fun UserScreen() {
    var user by rememberSaveable { mutableStateOf(User("Kim", 25)) }
    // 화면 회전에도 user 객체 유지
}
```

#### 2. listSaver 사용

```kotlin
data class User(val name: String, val age: Int)

val UserSaver = listSaver<User, Any>(
    save = { listOf(it.name, it.age) },
    restore = { User(it[0] as String, it[1] as Int) }
)

@Composable
fun UserScreen() {
    var user by rememberSaveable(stateSaver = UserSaver) {
        mutableStateOf(User("Kim", 25))
    }
}
```

#### 3. mapSaver 사용

```kotlin
val UserMapSaver = mapSaver(
    save = { mapOf("name" to it.name, "age" to it.age) },
    restore = { User(it["name"] as String, it["age"] as Int) }
)
```

## 사용 시나리오

### 1. 텍스트 입력 폼
```kotlin
var name by rememberSaveable { mutableStateOf("") }
var email by rememberSaveable { mutableStateOf("") }
```

### 2. 토글/체크박스 상태
```kotlin
var isAgree by rememberSaveable { mutableStateOf(false) }
var selectedOption by rememberSaveable { mutableStateOf(0) }
```

### 3. 스크롤 위치 (LazyListState)
```kotlin
// Compose가 자동으로 Saver를 제공
val listState = rememberLazyListState()
// 내부적으로 rememberSaveable 사용
```

### 4. 탭/네비게이션 상태
```kotlin
var selectedTab by rememberSaveable { mutableIntStateOf(0) }
```

## 주의사항

1. **Bundle 크기 제한**: 대용량 데이터는 Room, DataStore 등 사용
2. **UI 상태에만 사용**: 비즈니스 데이터는 ViewModel + Repository 패턴
3. **리스트 전체 저장 주의**: 긴 리스트는 저장하지 말 것
4. **사용자 명시적 종료 시 손실**: 최근 앱에서 스와이프하면 상태 손실

```kotlin
// 권장하지 않음 - 대용량 리스트
var items by rememberSaveable { mutableStateOf(hugeList) }  // X

// 권장 - 선택된 항목 ID만 저장
var selectedId by rememberSaveable { mutableStateOf<Int?>(null) }  // O
```

## 다음 학습

- **State Hoisting**: 상태를 상위로 끌어올려 재사용 가능한 Composable 만들기
- **ViewModel**: 화면 수준의 상태 관리, 비즈니스 로직 분리
- **SavedStateHandle**: ViewModel에서 Configuration Change 대응

## 연습 문제

1. **카운터 변환**: remember로 구현된 카운터를 rememberSaveable로 변환
2. **폼 데이터 유지**: 이름, 이메일 입력 폼에서 화면 회전 후에도 데이터 유지
3. **Parcelize 적용**: 커스텀 data class를 Parcelable로 만들어 저장

## 참고 자료

- [Save UI state in Compose - Android Developers](https://developer.android.com/develop/ui/compose/state-saving)
- [State and Jetpack Compose - Android Developers](https://developer.android.com/develop/ui/compose/state)
- [remember vs rememberSaveable - RevenueCat](https://www.revenuecat.com/blog/engineering/remember-vs-remembersaveable/)
