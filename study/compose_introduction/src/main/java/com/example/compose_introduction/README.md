# Jetpack Compose 소개

## Compose란 무엇인가?

**Jetpack Compose**는 Android를 위한 **현대적인 선언적 UI 툴킷**입니다.

Google은 2019년 Google I/O에서 Compose를 처음 발표했으며, 2025년 현재 Play Store 상위 1,000개 앱 중 **60% 이상**이 Compose를 사용하고 있습니다.

### Google이 왜 Compose를 만들었을까?

기존 Android View 시스템에는 여러 한계가 있었습니다:

```
문제 1: 수동 UI 업데이트
- findViewById()로 뷰를 찾아야 함
- setText(), setVisibility() 등으로 직접 업데이트
- 상태가 변경될 때마다 모든 관련 UI를 수동으로 갱신

문제 2: 상태 동기화 어려움
- 같은 데이터를 여러 곳에서 표시할 때 업데이트 누락 가능
- 예상치 못한 충돌로 불법 상태 발생 가능

문제 3: 복잡성 증가
- 코드가 길어지고 버그 발생 확률 증가
- 유지보수가 어려워짐
```

이러한 문제들을 해결하기 위해 **전체 업계가 선언적 UI 모델로 이동**하고 있습니다:
- Web: React
- iOS: SwiftUI
- Flutter: Dart Widgets
- Android: **Jetpack Compose**

---

## 선언적 UI vs 명령형 UI

### 비유: 레스토랑 주문

**명령형 UI (Imperative)**는 요리사에게 레시피를 알려주는 것과 같습니다:
```
"양파를 썰고, 팬을 달구고, 기름을 두르고, 양파를 볶고..."
```
모든 단계를 **어떻게(How)** 해야 하는지 지시합니다.

**선언적 UI (Declarative)**는 완성된 요리 사진을 보여주는 것과 같습니다:
```
"이 사진처럼 만들어 주세요."
```
**무엇을(What)** 원하는지만 선언하면 됩니다.

### 코드로 비교하기

#### 명령형 UI (기존 View 시스템)
```kotlin
// 1. 뷰 찾기
val textView = findViewById<TextView>(R.id.text_count)
val button = findViewById<Button>(R.id.button_increment)

// 2. 초기값 설정
var count = 0
textView.text = "Count: $count"

// 3. 이벤트 처리 + 수동 UI 업데이트
button.setOnClickListener {
    count++
    textView.text = "Count: $count"  // 직접 업데이트해야 함!
}
```

#### 선언적 UI (Jetpack Compose)
```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: $count")              // 상태를 선언적으로 표현
        Button(onClick = { count++ }) {    // 상태만 변경하면 끝!
            Text("Increment")
        }
    }
}
```

### 핵심 차이점

| 구분 | 명령형 UI | 선언적 UI |
|------|----------|----------|
| 관점 | "어떻게" 업데이트할까 | "무엇을" 보여줄까 |
| 상태 관리 | 수동 동기화 | 자동 동기화 |
| UI 업데이트 | setText(), setVisibility() 호출 | 상태 변경만 하면 자동 |
| 버그 위험 | 업데이트 누락 가능 | 구조적으로 방지 |
| 코드량 | 많음 | 적음 |

---

## 핵심 원칙: UI = f(State)

Compose의 가장 중요한 원칙은 다음과 같습니다:

```
UI = f(State)

UI는 상태의 함수다.
상태가 변하면, UI가 자동으로 변한다.
```

### 동작 원리

```
[상태 변경]
     |
     v
[Recomposition]  <-- Compose가 자동으로 감지
     |
     v
[UI 업데이트]    <-- 변경된 부분만 다시 그림
```

### 예시

```kotlin
@Composable
fun Greeting() {
    var name by remember { mutableStateOf("World") }

    // UI는 name의 함수
    // name이 변경되면 자동으로 UI도 변경됨
    Text("Hello, $name!")

    Button(onClick = { name = "Compose" }) {
        Text("Change Name")
    }
}
```

위 코드에서:
1. 버튼을 클릭하면 `name`이 "Compose"로 변경됨
2. Compose가 자동으로 변경 감지 (Recomposition)
3. `Text`가 "Hello, Compose!"로 자동 업데이트

**개발자는 상태만 변경하면 됩니다. UI 업데이트는 Compose가 알아서 합니다!**

---

## 기존 View 시스템과의 차이

| 항목 | 기존 View 시스템 | Jetpack Compose |
|------|----------------|-----------------|
| UI 정의 | XML 파일 | Kotlin 코드 (@Composable) |
| 뷰 참조 | findViewById, ViewBinding | 필요 없음 |
| 상속 구조 | View, ViewGroup 상속 | 함수 조합 |
| UI 업데이트 | 수동 (setText 등) | 자동 (Recomposition) |
| 미리보기 | Layout Editor (느림) | @Preview (빠름) |
| 언어 | XML + Kotlin | **Kotlin only** |

### 함수 조합 vs 상속

**기존 View**: 상속 기반
```kotlin
class MyButton : Button() {  // 상속
    // ...
}
```

**Compose**: 함수 조합
```kotlin
@Composable
fun MyButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {  // 조합
        Text(text)
    }
}
```

함수 조합 방식은:
- 더 유연함
- 재사용이 쉬움
- 테스트하기 쉬움

---

## Compose의 장점

### 1. 코드 간결성
Compose를 사용하면 같은 UI를 **50% 이상 적은 코드**로 구현할 수 있습니다.

### 2. 빠른 개발 속도
`@Preview` 어노테이션으로 **빌드 없이** 즉시 UI를 확인할 수 있습니다.
```kotlin
@Preview
@Composable
fun GreetingPreview() {
    Greeting()
}
```

### 3. Kotlin과의 완벽한 통합
- if/else로 조건부 렌더링
- for 루프로 리스트 렌더링
- 람다로 이벤트 핸들링
- 확장 함수로 Modifier 체이닝

```kotlin
@Composable
fun UserList(users: List<User>) {
    Column {
        if (users.isEmpty()) {
            Text("No users")
        } else {
            users.forEach { user ->
                Text(user.name)
            }
        }
    }
}
```

### 4. 강력한 도구 지원
- Android Studio 통합 미리보기
- 인터랙티브 미리보기
- 레이아웃 인스펙터
- Recomposition 카운터

---

## 주요 개념 미리보기

이 학습 모듈 시리즈에서 다룰 핵심 개념들입니다:

### @Composable 함수
```kotlin
@Composable
fun Greeting(name: String) {
    Text("Hello, $name!")
}
```
UI의 기본 단위입니다. 일반 함수에 `@Composable` 어노테이션을 붙입니다.

### remember & mutableStateOf
```kotlin
var count by remember { mutableStateOf(0) }
```
상태를 저장하고 관리합니다. 상태가 변경되면 UI가 자동으로 업데이트됩니다.

### Recomposition
상태가 변경되면 Compose가 해당 Composable 함수를 다시 호출하여 UI를 업데이트합니다.
변경된 부분만 다시 그리므로 효율적입니다.

---

## 연습 문제

### 연습 1: Hello, Compose!
첫 번째 Composable 함수를 작성해보세요.
- "Hello, [이름]!"을 표시하는 Composable 만들기

### 연습 2: 버튼으로 텍스트 토글하기
버튼을 누르면 텍스트가 바뀌는 UI를 만들어보세요.
- remember와 mutableStateOf 사용하기

### 연습 3: 실시간 입력 반영
TextField에 입력한 내용이 실시간으로 반영되는 UI를 만들어보세요.
- 상태와 UI의 자동 동기화 체험하기

---

## 다음 학습

이 모듈에서 Compose의 기본 개념을 이해했다면, 다음 단계로 넘어가세요:

1. **Composable 함수** - @Composable 어노테이션과 함수 작성법
2. **기본 UI 컴포넌트** - Text, Button, TextField 등
3. **Layout & Modifier** - Column, Row, Box와 Modifier 체이닝
4. **상태 관리** - remember, mutableStateOf, State Hoisting

---

## 참고 자료

- [Thinking in Compose - Android Developers](https://developer.android.com/develop/ui/compose/mental-model)
- [Jetpack Compose Tutorial](https://developer.android.com/develop/ui/compose/tutorial)
- [Jetpack Compose Basics Codelab](https://developer.android.com/codelabs/jetpack-compose-basics)
