# State Restoration 심화 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `state_management_advanced` | StateFlow, SharedFlow, Channel을 활용한 상태 관리 | [📚 학습하기](../../state/state_management_advanced/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**State Restoration(상태 복원)**은 앱의 UI 상태가 다음 상황에서도 유지되도록 하는 기법입니다:

1. **Configuration Change** - 화면 회전, 다크모드 전환, 언어 변경 등
2. **Process Death** - 시스템이 백그라운드 앱을 메모리 부족으로 종료

Compose에서는 `rememberSaveable`이 기본 상태 저장 메커니즘이지만, **복잡한 객체**를 저장하려면 **커스텀 Saver**가 필요합니다.

---

## 왜 State Restoration이 필요한가?

### 사용자 경험 관점
사용자가 폼을 입력하다가 전화를 받고 돌아왔을 때, 입력했던 데이터가 모두 사라져 있다면 어떨까요?

| 시나리오 | State Restoration 없음 | State Restoration 있음 |
|---------|----------------------|----------------------|
| 화면 회전 | 모든 입력 데이터 손실 | 데이터 유지 |
| 다른 앱 사용 후 복귀 | 프로세스 종료 시 손실 | 데이터 복원 |
| 언어 변경 | 모든 입력 데이터 손실 | 데이터 유지 |

### Android 생명주기 관점
```
앱 실행 중
    ↓ (화면 회전 or 다크모드 전환)
Activity 재생성 (Configuration Change)
    ↓
remember 상태 손실!
    ↓
rememberSaveable은 Bundle에서 복원
```

---

## 문제 상황: remember만 사용할 때

### 잘못된 코드 예시

```kotlin
@Composable
fun ProfileForm() {
    // 화면 회전 시 모든 데이터 손실!
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var hobbies by remember { mutableStateOf(listOf<String>()) }

    // ...
}
```

### 발생하는 문제점

1. **화면 회전 시** 입력한 데이터가 모두 사라짐
2. **앱이 백그라운드에서 종료되면** 복구 불가
3. **사용자 경험 저하** (다시 입력해야 함)

> Problem.kt에서 직접 테스트해보세요!
> 화면 회전 시 데이터가 사라지는 것을 확인할 수 있습니다.

---

## 해결 전략 개요

### 상태 저장 계층 구조

```
remember
  ↓ (Configuration Change에서 손실)
rememberSaveable (기본 타입만)
  ↓ (커스텀 객체는 Saver 필요)
rememberSaveable + Saver (커스텀 객체)
  ↓ (ViewModel과 분리됨)
ViewModel + SavedStateHandle (프로덕션 권장)
```

### Bundle 저장 가능한 타입

| 타입 | 저장 가능 | 비고 |
|------|:--------:|------|
| Int, Long, Float, Double | O | 기본 타입 |
| String, Boolean | O | 기본 타입 |
| IntArray, LongArray 등 | O | 기본 배열 |
| Parcelable | O | @Parcelize 사용 |
| Serializable | O | 성능상 비권장 |
| List, Map | X | ArrayList, HashMap으로 변환 필요 |
| 커스텀 data class | X | Saver 필요 |

---

## Saver 방법 선택 가이드

### 의사결정 플로우차트

```
시작: 커스텀 객체를 저장해야 한다
    │
    ├── Android 전용 앱인가?
    │     │
    │     ├── Yes ─► @Parcelize가 가능한가?
    │     │           │
    │     │           ├── Yes ─► 방법 1: @Parcelize 사용
    │     │           │
    │     │           └── No (3rd party 라이브러리 클래스 등)
    │     │                 ↓
    │     │                 필드가 5개 이상인가?
    │     │                   ├── Yes ─► 방법 2: mapSaver
    │     │                   └── No ─► 방법 3: listSaver
    │     │
    │     └── No (KMP, 테스트 용이성 필요)
    │           ↓
    │           방법 2: mapSaver 또는 방법 3: listSaver
    │
    └── ViewModel에서 관리해야 하는가?
          │
          ├── Yes ─► 방법 4: ViewModel + SavedStateHandle
          │
          └── No ─► 위 방법들 중 선택
```

### 비교표

| 방법 | 장점 | 단점 | 적합한 상황 |
|------|------|------|------------|
| **@Parcelize** | 가장 간단함, 별도 코드 불필요 | Android 전용 | 빠른 구현, Android 전용 앱 |
| **mapSaver** | 명시적 키, 플랫폼 독립적, 디버깅 용이 | 코드가 길어짐 | 필드가 많거나 키가 중요할 때, KMP |
| **listSaver** | 간결한 코드 | 인덱스 관리 필요, 필드 순서에 민감 | 필드가 적을 때 (3-4개 이하) |
| **ViewModel + SavedStateHandle** | 비즈니스 로직 통합, 프로세스 종료 복원 | Experimental API, 설정 복잡 | 프로덕션 앱, 비즈니스 로직과 통합 필요 |

---

## 해결책 상세

### 방법 1: @Parcelize (가장 간단)

```kotlin
@Parcelize
data class UserProfile(
    val name: String = "",
    val email: String = "",
    val hobbies: List<String> = emptyList()
) : Parcelable

// 별도 Saver 없이 바로 사용 가능
var profile by rememberSaveable {
    mutableStateOf(UserProfile())
}
```

**장점**: 가장 적은 코드로 구현 가능
**단점**: Android 플랫폼 전용

---

### 방법 2: mapSaver (명시적 키)

```kotlin
val UserProfileSaver = mapSaver(
    save = { profile ->
        mapOf(
            "name" to profile.name,
            "email" to profile.email,
            "hobbies" to ArrayList(profile.hobbies)
        )
    },
    restore = { map ->
        UserProfile(
            name = map["name"] as String,
            email = map["email"] as String,
            hobbies = map["hobbies"] as List<String>
        )
    }
)

var profile by rememberSaveable(stateSaver = UserProfileSaver) {
    mutableStateOf(UserProfile())
}
```

**장점**: 키가 명시적이어서 디버깅 용이, 플랫폼 독립적
**단점**: 코드량이 많음

---

### 방법 3: listSaver (간결함)

```kotlin
val UserProfileSaver = listSaver(
    save = { listOf(it.name, it.email, ArrayList(it.hobbies)) },
    restore = {
        UserProfile(
            name = it[0] as String,
            email = it[1] as String,
            hobbies = it[2] as List<String>
        )
    }
)
```

**장점**: 간결한 코드
**단점**: 인덱스 기반이라 필드 순서 변경 시 호환성 문제

---

### 방법 4: ViewModel + SavedStateHandle (프로덕션 권장)

```kotlin
class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 주의: 이 API는 Experimental입니다
    @OptIn(SavedStateHandleSaveableApi::class)
    var profile by savedStateHandle.saveable(
        stateSaver = UserProfileSaver
    ) {
        mutableStateOf(UserProfile())
    }
        private set

    fun updateName(name: String) {
        profile = profile.copy(name = name)
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val profile = viewModel.profile
    // ...
}
```

**장점**: 비즈니스 로직과 통합, 프로세스 종료 시에도 복원
**단점**: Experimental API, 추가 설정 필요

> **주의**: `SavedStateHandle.saveable()`은 **Experimental API**입니다.
> `@OptIn(SavedStateHandleSaveableApi::class)` 어노테이션이 필요합니다.

---

## SaveableStateHolder 사용법

여러 화면/탭의 상태를 독립적으로 관리할 때 사용합니다.

```kotlin
@Composable
fun MultiTabScreen(currentTab: Int) {
    val stateHolder = rememberSaveableStateHolder()

    stateHolder.SaveableStateProvider(key = "tab_$currentTab") {
        when (currentTab) {
            0 -> FirstTabContent()
            1 -> SecondTabContent()
            2 -> ThirdTabContent()
        }
    }
}
```

**핵심 포인트:**
- 각 화면마다 고유한 `key` 사용
- 내부에서 `rememberSaveable` 사용하면 탭 전환 후에도 유지
- Navigation과 함께 사용하면 백스택 상태 관리 가능

> **2025년 업데이트**: Compose 1.9.0-alpha03부터 `SaveableStateProvider`가
> `LocalSavedStateRegistryOwner`를 자동으로 제공합니다.

---

## 프로세스 종료 테스트 방법

1. **개발자 옵션 활성화**
   - 설정 > 휴대전화 정보 > 빌드 번호 7회 탭

2. **"활동 유지 안함" 활성화**
   - 설정 > 개발자 옵션 > "활동 유지 안함" ON

3. **테스트**
   - 앱에서 데이터 입력
   - 홈 버튼으로 백그라운드 이동
   - 다시 앱 열기
   - 데이터가 복원되는지 확인

> 이 테스트로 Problem.kt와 Solution.kt의 차이를 명확히 확인할 수 있습니다.

---

## Best Practices

### 1. ID만 저장, 데이터는 로컬에서 로드

```kotlin
// 나쁜 예: 전체 객체 저장 (Bundle 크기 제한 초과 위험)
var selectedItem by rememberSaveable { mutableStateOf(largeItem) }

// 좋은 예: ID만 저장
var selectedItemId by rememberSaveable { mutableStateOf<Long?>(null) }
val selectedItem by produceState<LargeItem?>(null, selectedItemId) {
    value = selectedItemId?.let { repository.getItem(it) }
}
```

### 2. 가변 컬렉션 사용 금지

```kotlin
// 나쁜 예: mutableListOf
var items by rememberSaveable { mutableStateOf(mutableListOf<String>()) }

// 좋은 예: 불변 List
var items by rememberSaveable { mutableStateOf(listOf<String>()) }
```

### 3. 다중 인스턴스 키 지정

```kotlin
// 동일 컴포저블이 여러 번 호출될 때
items.forEach { item ->
    rememberSaveable(key = item.id) {
        mutableStateOf(initialValue)
    }
}
```

---

## 주의사항

### 1. Bundle 크기 제한
- Bundle 전체 크기는 약 **1MB 이하** 권장
- 초과 시 `TransactionTooLargeException` 발생
- 이미지, 긴 텍스트, 큰 리스트는 저장 금지

### 2. ArrayList/HashMap 사용
```kotlin
// List는 Bundle에 직접 저장 불가
hobbies = listOf("a", "b")  // X

// ArrayList로 변환 필요
hobbies = ArrayList(listOf("a", "b"))  // O
```

### 3. 타입 캐스팅 주의
```kotlin
restore = { map ->
    @Suppress("UNCHECKED_CAST")
    val hobbies = map["hobbies"] as? ArrayList<String> ?: emptyList()
    // null 안전 처리 필수
}
```

### 4. Experimental API 주의
```kotlin
// SavedStateHandle.saveable()는 실험적 API입니다
@OptIn(SavedStateHandleSaveableApi::class)
var profile by savedStateHandle.saveable(...) { ... }
```

---

## 연습 문제

### 연습 1 (초급): mapSaver로 SearchQuery 객체 저장
- 4개 필드 (query, category, maxResults, includeArchived)
- 키 상수 정의 및 타입 변환 연습

### 연습 2 (중급): listSaver로 중첩 객체 저장
- ShoppingCart 내부에 List<CartItem> 포함
- 중첩된 리스트 직렬화 연습

### 연습 3 (고급): SaveableStateHolder로 탭 상태 관리
- 3개 탭 간 전환 시 각각의 상태 유지
- SaveableStateProvider 사용법 연습

> Practice.kt에서 각 연습을 진행할 수 있습니다.
> 정답은 주석으로 제공되어 있습니다.

---

## 다음 학습

- **Navigation과 상태 복원**: `navigation` 모듈
- **ViewModel 기초**: `view_model` 모듈
- **Hilt + SavedStateHandle**: `hilt_viewmodel` 모듈
