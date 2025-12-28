# Compose Multiplatform 입문

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `kotlin` | Kotlin 기본 문법과 핵심 개념 | [📚 학습하기](../../basics/kotlin/README.md) |
| `compose_introduction` | Jetpack Compose 기초 | [📚 학습하기](../../basics/compose_introduction/README.md) |
| `composable_function` | @Composable 함수 작성법 | [📚 학습하기](../../basics/composable_function/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## Compose Multiplatform이란?

**Compose Multiplatform**은 JetBrains가 개발한 **크로스플랫폼 UI 프레임워크**입니다.

Jetpack Compose를 기반으로 하며, 한 번 작성한 UI 코드를 Android, iOS, Desktop, Web에서 모두 실행할 수 있습니다.

### 2025년 현재 상태

| 플랫폼 | 상태 | 비고 |
|--------|------|------|
| Android | **Stable** | Jetpack Compose와 동일한 경험 |
| iOS | **Stable** | 2025년 5월 1.8.0에서 Stable 달성 |
| Desktop | **Stable** | Windows, macOS, Linux 지원 |
| Web | **Beta** | 2025년 9월 1.9.0에서 Beta |

### Jetpack Compose와의 관계

```
Jetpack Compose (Android 전용)
        |
        v
Compose Multiplatform (크로스플랫폼 확장)
        |
        +-- Android (Jetpack Compose 그대로)
        +-- iOS (Compose for iOS)
        +-- Desktop (Compose for Desktop)
        +-- Web (Compose for Web)
```

**핵심 포인트**:
- Compose Multiplatform = Jetpack Compose + 멀티플랫폼 지원
- Android에서는 Jetpack Compose와 100% 호환
- 같은 Compose API (Column, Row, Box, Text 등) 사용

---

## 핵심 특징

### 1. 한 번 작성, 모든 곳에서 실행

```kotlin
// 이 코드는 Android, iOS, Desktop, Web에서 동일하게 동작합니다
@Composable
fun Greeting(name: String) {
    Text("Hello, $name!")
}
```

### 2. Kotlin 100%

- 모든 플랫폼에서 Kotlin 사용
- Swift, JavaScript 학습 불필요 (iOS, Web용)
- Kotlin Multiplatform (KMP) 기반

### 3. 네이티브 성능

- 각 플랫폼의 네이티브 렌더링 엔진 사용
- 크로스플랫폼이지만 네이티브 성능 유지

### 4. 점진적 도입 가능

- 기존 앱에 부분적으로 도입 가능
- 새 화면만 Compose Multiplatform으로 작성 가능

---

## 문제 상황: 플랫폼별 UI 코드 중복

### 시나리오

"여러분이 회사에서 할 일 목록(Todo) 앱을 만들어야 합니다. 처음에는 Android용으로 만들었는데, iOS 버전도 필요하다고 합니다. 나중에는 데스크톱과 웹 버전도 요청받습니다."

### 기존 방식의 문제점

```
[Android]           [iOS]              [Web]
Kotlin + Compose    Swift + SwiftUI    JavaScript + React
     |                  |                   |
     v                  v                   v
@Composable         struct View        function Component
fun TodoList()      TodoList           TodoList()
{ ... }             { ... }            { return ... }
```

**문제 1: 코드 중복**
- 같은 UI를 플랫폼마다 다시 작성
- 3개 플랫폼 = 3배의 코드량

**문제 2: 유지보수 비용 증가**
- 버튼 색상 변경 -> 3곳 수정
- 새 기능 추가 -> 3번 구현

**문제 3: 일관성 유지 어려움**
- 플랫폼별로 UI가 조금씩 달라짐
- 버그도 플랫폼별로 다르게 발생

**문제 4: 팀 구성 복잡**
- Android 개발자, iOS 개발자, Web 개발자 필요
- 커뮤니케이션 비용 증가

---

## 해결책: Compose Multiplatform

### 공유 코드로 해결

```
[Compose Multiplatform]

         commonMain (공유 코드)
         @Composable
         fun TodoList() { ... }
              |
    +---------+---------+---------+
    |         |         |         |
Android    iOS     Desktop     Web
```

### 프로젝트 구조

Compose Multiplatform 프로젝트는 다음과 같은 **소스셋(Source Set)** 구조를 가집니다:

```
shared/
├── commonMain/         # 공유 UI 코드 (모든 플랫폼에서 사용)
│   └── kotlin/
│       └── App.kt      # @Composable 함수들
│
├── androidMain/        # Android 전용 코드
│   └── kotlin/
│       └── Platform.android.kt
│
├── iosMain/            # iOS 전용 코드
│   └── kotlin/
│       └── Platform.ios.kt
│
├── desktopMain/        # Desktop 전용 코드
│   └── kotlin/
│       └── Platform.jvm.kt
│
└── wasmJsMain/         # Web 전용 코드
    └── kotlin/
        └── Platform.wasm.kt
```

**소스셋(Source Set)**이란?
- 특정 플랫폼이나 목적에 맞는 코드를 담는 폴더
- commonMain: 모든 플랫폼에서 공유하는 코드
- androidMain, iosMain 등: 해당 플랫폼에서만 컴파일되는 코드

---

## expect/actual 패턴

### 개념

플랫폼마다 다르게 구현해야 하는 기능이 있을 때 사용하는 패턴입니다.

- **expect**: "이런 기능이 필요해요" (선언)
- **actual**: "이렇게 구현했어요" (구현)

### 비유

```
expect = 약속 (계약서)
actual = 이행 (실제 이행)

"플랫폼 이름을 알려주세요" (expect)
  ├── Android: "Android" (actual)
  ├── iOS: "iOS" (actual)
  └── Desktop: "Desktop" (actual)
```

### 코드 예시

```kotlin
// commonMain/Platform.kt
// "이런 기능이 필요해요"
expect fun getPlatformName(): String

// androidMain/Platform.android.kt
// Android에서는 이렇게 구현해요
actual fun getPlatformName(): String = "Android"

// iosMain/Platform.ios.kt
// iOS에서는 이렇게 구현해요
actual fun getPlatformName(): String = "iOS"

// desktopMain/Platform.jvm.kt
// Desktop에서는 이렇게 구현해요
actual fun getPlatformName(): String = "Desktop"
```

### 사용

```kotlin
// commonMain/App.kt
// 공유 코드에서 expect 함수 사용
@Composable
fun App() {
    Text("현재 플랫폼: ${getPlatformName()}")
}
```

실행 결과:
- Android에서 실행: "현재 플랫폼: Android"
- iOS에서 실행: "현재 플랫폼: iOS"
- Desktop에서 실행: "현재 플랫폼: Desktop"

---

## 공유 UI 코드 작성법

### 기본 원칙

**대부분의 UI 코드는 commonMain에 작성합니다.**

```kotlin
// commonMain/components/ProfileCard.kt
@Composable
fun ProfileCard(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```

이 코드는 **Android, iOS, Desktop, Web에서 동일하게 동작**합니다.

### 플랫폼별 분기가 필요할 때

1. **간단한 경우**: expect/actual 사용

```kotlin
// commonMain
expect val isIOS: Boolean

// iosMain
actual val isIOS = true

// androidMain
actual val isIOS = false
```

2. **복잡한 경우**: 인터페이스 + 의존성 주입

```kotlin
// commonMain
interface PlatformFeatures {
    fun shareContent(text: String)
    fun openBrowser(url: String)
}

// 각 플랫폼에서 구현 후 주입
```

---

## 코드 공유율

실제 앱에서의 코드 공유 사례:

| 앱 | 코드 공유율 | 비고 |
|----|------------|------|
| Respawn (iOS) | **96%** | Android와 96% 코드 공유 |
| JetBrains Toolbox | **100%** | 모든 UI가 Compose |

### 공유할 수 있는 것

- UI 컴포넌트 (Button, Card, List 등)
- 화면 레이아웃 (Screen, Dialog 등)
- 테마 및 스타일
- 비즈니스 로직

### 공유하기 어려운 것 (expect/actual 필요)

- 카메라, GPS 등 하드웨어 접근
- 파일 시스템 접근
- 플랫폼 특정 API (Android Intent, iOS URL Scheme 등)

---

## 이 모듈의 특수성

**중요**: 이 모듈은 Compose Multiplatform의 **개념 학습**을 위한 것입니다.

- 실제 멀티플랫폼 빌드 환경이 아닌 Android 단독 프로젝트
- expect/actual 패턴을 **시뮬레이션** 형태로 학습
- 핵심 개념과 원리를 이해하는 것이 목표

**실제 Compose Multiplatform 프로젝트를 시작하려면**:
- [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com/) 사용
- JetBrains 공식 템플릿으로 프로젝트 생성

---

## 연습 문제

### 연습 1: 플랫폼 정보 표시 (쉬움)
expect/actual 패턴을 시뮬레이션하여 플랫폼 정보를 표시하세요.

### 연습 2: 공유 프로필 카드 (중간)
commonMain에 배치될 공유 UI 컴포넌트를 작성하세요.

### 연습 3: 플랫폼별 네비게이션 (어려움)
플랫폼마다 다른 스타일의 네비게이션을 구현하세요.

---

## 다음 학습

이 모듈에서 Compose Multiplatform의 기본 개념을 이해했다면:

1. **Kotlin Multiplatform 기초** - expect/actual 심화
2. **리소스 공유** - 이미지, 문자열 공유 방법
3. **네비게이션** - Compose Multiplatform에서의 화면 전환
4. **실제 프로젝트 설정** - Gradle 설정, 빌드 방법

---

## 참고 자료

- [Compose Multiplatform 공식 문서](https://www.jetbrains.com/compose-multiplatform/)
- [Kotlin Multiplatform 시작하기](https://kotlinlang.org/docs/multiplatform-get-started.html)
- [expect/actual 공식 문서](https://kotlinlang.org/docs/multiplatform-expect-actual.html)
- [JetBrains Blog - Compose Multiplatform 1.8.0 (iOS Stable)](https://blog.jetbrains.com/kotlin/2025/05/compose-multiplatform-1-8-0-released-compose-multiplatform-for-ios-is-stable-and-production-ready/)
