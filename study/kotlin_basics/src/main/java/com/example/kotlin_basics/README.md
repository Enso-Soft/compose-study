# Kotlin 기초 - Compose를 위한 필수 문법

## 개념

Jetpack Compose는 **Kotlin DSL**로 구축되어 있습니다. Compose 코드를 읽고 쓰려면 반드시 알아야 할 Kotlin 문법 4가지가 있습니다:

1. **람다 표현식 (Lambda Expression)**
2. **확장 함수 (Extension Function)**
3. **후행 람다 (Trailing Lambda)**
4. **널 안전성 (Null Safety)**

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
- 반환 타입 추론 가능

### 2. 확장 함수 (Extension Function)

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

### 3. 후행 람다 (Trailing Lambda)

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

### 4. 널 안전성 (Null Safety)

Kotlin의 타입 시스템으로 NPE를 방지합니다.

```kotlin
val name: String? = null  // nullable

// 안전 호출
name?.length  // null 반환 (크래시 X)

// Elvis 연산자
name ?: "Unknown"  // null이면 기본값

// let 스코프 함수
name?.let { Text(it) }  // null 아닐 때만 실행

// Compose에서의 활용 - 조건부 렌더링
user?.let {
    Text("Hello, ${it.name}")
} ?: Text("로그인 해주세요")
```

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
   - 람다에서 `return`은 바깥 함수를 종료
   - 람다만 종료하려면 `return@람다이름` 사용

2. **확장 함수 스코프**
   - 같은 시그니처의 멤버 함수가 있으면 멤버 함수 우선

3. **널 안전성 과용 주의**
   - `!!` 연산자는 NPE 발생 가능, 가급적 피하기

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
