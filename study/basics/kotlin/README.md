# Kotlin - Compose를 위한 필수 문법

## 사전 지식

> **시작 모듈**: 이 모듈은 Compose 학습의 첫 단계입니다. 선행 학습이 필요 없습니다.
>
> 💡 Kotlin 프로그래밍 언어의 기본 문법(변수, 함수, 클래스)만 알고 있으면 됩니다.

---

## 개념

Jetpack Compose는 **Kotlin DSL**로 구축되어 있습니다. Compose 코드를 읽고 쓰려면 반드시 알아야 할 Kotlin 문법이 있습니다:

### 필수 문법 (반드시 알아야 함)
1. **람다 표현식 (Lambda Expression)**
2. **후행 람다 (Trailing Lambda)** - 람다의 확장 문법
3. **확장 함수 (Extension Function)**
4. **널 안전성 (Null Safety)**

### 심화 문법 (Compose 숙달을 위해)
5. **수신 객체 지정 람다 (Lambda with Receiver)** - Compose DSL의 비밀

이 문법들을 모르면 Compose 코드가 **외계어**처럼 보입니다.

---

## 핵심 특징

### 1. 람다 표현식 (Lambda Expression)

익명 함수를 간결하게 표현하는 방법입니다.

```kotlin
// 기본 문법
val sum: (Int, Int) -> Int = { a, b -> a + b }

// Compose에서의 활용
Button(onClick = { count++ }) { ... }
```

**특징:**
- `{ parameters -> body }` 형태
- 파라미터가 하나면 `it`으로 자동 참조
- 마지막 표현식이 반환값이 됨

### 2. 후행 람다 (Trailing Lambda)

마지막 파라미터가 람다일 때, 괄호 밖으로 뺄 수 있습니다.

```kotlin
// 일반 호출
Column(content = { Text("Hello") })

// 후행 람다 적용
Column { Text("Hello") }

// 모든 Compose 컨테이너에서 사용
Row { ... }
Box { ... }
Card { ... }
```

**특징:**
- DSL 스타일 코드의 핵심
- 가독성 대폭 향상
- Compose UI 구조를 선언적으로 표현

### 3. 확장 함수 (Extension Function)

기존 클래스에 새로운 함수를 추가합니다.

```kotlin
// 정의
fun String.addExclamation(): String = "$this!"

// 사용
"Hello".addExclamation()  // "Hello!"

// Compose에서의 활용 - Modifier 체이닝
Modifier.padding(16.dp).background(Color.Red)
```

**특징:**
- `fun 타입.함수명()` 형태
- 원본 클래스 수정 없이 기능 추가
- 체이닝 패턴의 핵심

### 4. 널 안전성 (Null Safety)

Kotlin의 타입 시스템으로 NPE를 방지합니다.

```kotlin
val name: String? = null  // nullable

// 안전 호출 (Safe Call)
name?.length  // null 반환 (크래시 X)

// Elvis 연산자
name ?: "Unknown"  // null이면 기본값

// let 스코프 함수와 안전 호출 조합
name?.let { Text(it) }  // null 아닐 때만 실행

// Compose에서의 활용 - 조건부 렌더링 패턴
user?.let { userData ->
    Text("Hello, ${userData.name}")
} ?: Text("로그인 해주세요")
```

**특징:**
- `?` 타입으로 nullable 명시
- `?.`으로 안전하게 속성/메서드 접근
- `?:`으로 null일 때 기본값 제공
- `?.let { }`으로 null 아닐 때만 코드 실행

### 5. 수신 객체 지정 람다 (Lambda with Receiver) - 심화

Compose DSL의 핵심 원리입니다. 람다 내부에서 특정 객체의 멤버에 직접 접근할 수 있습니다.

```kotlin
// 일반 람다
val greet: (String) -> String = { name -> "Hello, $name!" }

// 수신 객체 지정 람다 (String이 수신 객체)
val greetWithReceiver: String.() -> String = { "Hello, $this!" }

// 사용
"World".greetWithReceiver()  // "Hello, World!"
```

**Compose에서의 활용:**

```kotlin
// Row의 content 파라미터 시그니처
content: @Composable RowScope.() -> Unit

// RowScope가 수신 객체이므로 Row 내부에서 weight() 사용 가능
Row {
    Text("Left", modifier = Modifier.weight(1f))  // weight()는 RowScope의 멤버!
    Text("Right", modifier = Modifier.weight(1f))
}
```

**주요 Compose Scope:**
| Scope | 사용 위치 | 제공하는 기능 |
|-------|----------|--------------|
| `RowScope` | Row { } | `weight()`, `align()` |
| `ColumnScope` | Column { } | `weight()`, `align()` |
| `BoxScope` | Box { } | `align()`, `matchParentSize()` |
| `LazyItemScope` | LazyColumn items { } | `animateItemPlacement()` |

**특징:**
- DSL(Domain Specific Language) 구현의 핵심
- 람다 내부에서 `this`가 수신 객체를 참조
- 특정 컨텍스트에서만 사용 가능한 함수 제공
- Compose의 선언적 UI 패턴을 가능하게 함

---

## 문제 상황: Kotlin 문법 없이 Compose 코드 읽기

### 잘못된 이해 예시

```kotlin
// 이 코드가 무슨 뜻인지 모름
Button(onClick = { viewModel.save() }) {
    Text("저장")
}
```

**발생하는 문제점:**
- `onClick = { }` - 람다가 뭔지 모름
- `{ Text("저장") }` - 왜 중괄호가 괄호 밖에 있는지 모름
- 전체 구조가 이해 안 됨

---

## 해결책: 4가지 핵심 문법 이해

### 분석된 코드

```kotlin
Button(
    onClick = { viewModel.save() }  // 람다: 클릭 시 실행할 코드
) {                                  // 후행 람다: content 파라미터
    Text("저장")
}
```

**이해 후:**
- `onClick`에 람다로 클릭 핸들러 전달
- 후행 람다로 버튼 내부 컨텐츠 정의
- 선언적이고 읽기 쉬운 구조

---

## 사용 시나리오

### 1. 이벤트 핸들러 (람다)
```kotlin
TextField(
    value = text,
    onValueChange = { newValue -> text = newValue }
)
```

### 2. Modifier 커스터마이징 (확장 함수)
```kotlin
fun Modifier.debugBorder() = this.border(1.dp, Color.Red)

// 사용
Text("Debug", modifier = Modifier.debugBorder())
```

### 3. 레이아웃 구성 (후행 람다)
```kotlin
Column {
    Row {
        Icon(...)
        Text(...)
    }
    Spacer(...)
    Button(...) { Text("확인") }
}
```

### 4. 조건부 UI (널 안전성)
```kotlin
@Composable
fun UserProfile(user: User?) {
    user?.let {
        Column {
            Text(it.name)
            Text(it.email)
        }
    } ?: Text("사용자 정보 없음")
}
```

---

## 주의사항

1. **람다 내부의 return**
   - 람다에서 `return`은 바깥 함수를 종료 (non-local return)
   - 람다만 종료하려면 `return@람다이름` 사용
   ```kotlin
   // 예시
   listOf(1, 2, 3).forEach {
       if (it == 2) return@forEach  // 이 람다만 종료
       println(it)
   }
   ```

2. **확장 함수 스코프**
   - 같은 시그니처의 멤버 함수가 있으면 멤버 함수 우선
   - 확장 함수는 원본 클래스를 수정하지 않음 (정적 해석)

3. **널 안전성 과용 주의**
   - `!!` 연산자는 NPE 발생 가능, 가급적 피하기
   - 대신 `?.let {}` 또는 `?: defaultValue` 패턴 사용 권장

---

## 연습 문제

### 연습 1: 람다 표현식
리스트 필터링과 변환에 람다를 활용해보세요.

### 연습 2: 확장 함수 + Modifier
커스텀 Modifier 확장 함수를 만들어보세요.

### 연습 3: 널 안전성
nullable 데이터로 안전한 조건부 UI를 구현해보세요.

---

## 다음 학습

- **Composable 함수**: @Composable 어노테이션의 의미
- **기본 UI 컴포넌트**: Text, Button, Image 등
- **Layout & Modifier**: Column, Row, Box와 Modifier 활용

---

## 참고 자료

### 공식 문서
- [Kotlin for Jetpack Compose](https://developer.android.com/develop/ui/compose/kotlin) - Android 공식 Kotlin Compose 가이드
- [Kotlin 람다 표현식](https://kotlinlang.org/docs/lambdas.html) - Kotlin 공식 문서
- [Kotlin 확장 함수](https://kotlinlang.org/docs/extensions.html) - Kotlin 공식 문서
- [Kotlin 널 안전성](https://kotlinlang.org/docs/null-safety.html) - Kotlin 공식 문서

### 추가 학습
- [Use function types and lambda expressions in Kotlin](https://developer.android.com/codelabs/basic-android-kotlin-compose-function-types-and-lambda) - Android Codelab
- [Use nullability in Kotlin](https://developer.android.com/codelabs/basic-android-kotlin-compose-nullability) - Android Codelab
