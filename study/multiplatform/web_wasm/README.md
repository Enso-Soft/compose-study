# Compose for Web (Wasm) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `compose_multiplatform_intro` | Compose Multiplatform 기초 개념 | [📚 학습하기](../compose_multiplatform_intro/README.md) |
| `composable_function` | @Composable 함수 작성법 | [📚 학습하기](../../basics/composable_function/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

> Kotlin/Wasm으로 브라우저에서 Compose UI 실행하기

---

## 개념

**Compose for Web**은 Jetpack Compose UI를 웹 브라우저에서 실행할 수 있게 해주는 기술입니다.
Kotlin 코드를 WebAssembly(Wasm)로 컴파일하여 네이티브 수준의 성능으로 웹 앱을 만들 수 있습니다.

### 비유로 이해하기

Compose for Web은 **'통역사'**와 같습니다:
- Kotlin 코드를 브라우저가 이해할 수 있는 WebAssembly로 번역해줍니다
- 마치 한국어를 영어로 통역하듯, Compose UI를 웹 UI로 변환합니다

---

## 핵심 특징

### 1. Kotlin/Wasm 기반
- **WebAssembly**: 브라우저에서 네이티브 수준 성능으로 실행되는 바이너리 형식
- **wasmJs 타겟**: Kotlin을 WebAssembly로 컴파일하는 Gradle 타겟
- **2025년 현황**: Beta 상태, 모든 모던 브라우저 지원 (Chrome, Firefox, Safari, Edge)

### 2. Canvas 기반 렌더링
- **Skiko 라이브러리**: Skia 그래픽 엔진의 Kotlin 래퍼
- **직접 렌더링**: HTML DOM 대신 Canvas에 직접 UI를 그림
- **일관된 UI**: 모든 플랫폼에서 동일한 렌더링 결과

### 3. 크로스플랫폼 코드 공유
```
┌─────────────────────────────────────────┐
│           공유 Kotlin 코드              │
│    (UI, 비즈니스 로직, 데이터 모델)     │
└─────────────────────────────────────────┘
         │         │         │         │
         ▼         ▼         ▼         ▼
     ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐
     │Android│ │  iOS  │ │Desktop│ │  Web  │
     └───────┘ └───────┘ └───────┘ └───────┘
```

---

## 문제 상황: 전통적 웹 개발의 복잡성

### 문제 1: 기술 스택 파편화

전통적인 웹 개발에서는 여러 언어와 기술을 배워야 합니다:

```
┌──────────────────────────────────────────────────┐
│ 전통적 웹 개발 스택                               │
├──────────────────────────────────────────────────┤
│ HTML     - 구조 정의                             │
│ CSS      - 스타일링                              │
│ JavaScript/TypeScript - 동작 로직                │
│ React/Vue/Angular - 프레임워크                   │
│ Redux/Zustand - 상태 관리                        │
│ Webpack/Vite - 빌드 도구                         │
└──────────────────────────────────────────────────┘
```

### 문제 2: 플랫폼별 코드 중복

모바일과 웹을 함께 개발할 때:

```
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│   Android   │  │     iOS     │  │     Web     │
├─────────────┤  ├─────────────┤  ├─────────────┤
│   Kotlin    │  │    Swift    │  │ JavaScript  │
│   Compose   │  │   SwiftUI   │  │    React    │
│  Coroutines │  │ async/await │  │   Promises  │
└─────────────┘  └─────────────┘  └─────────────┘
      │                │                │
      ▼                ▼                ▼
   3배의 개발 시간, 3배의 버그 수정
```

### 문제 3: 타입 안전성 부족

JavaScript의 동적 타이핑으로 인한 런타임 에러:

```javascript
// JavaScript - 런타임에서야 에러 발견
function greet(user) {
    return "Hello, " + user.name;  // user가 null이면?
}

greet(null);  // TypeError: Cannot read property 'name' of null
```

---

## 해결책: Compose for Web 사용

### 해결책 1: 단일 언어 (Kotlin)

```kotlin
// Kotlin - 컴파일 타임에 에러 검출
fun greet(user: User): String {
    return "Hello, ${user.name}"
}

greet(null)  // 컴파일 에러! User 타입에 null 불가
```

### 해결책 2: 코드 공유

```kotlin
// commonMain - 모든 플랫폼에서 공유
@Composable
fun Greeting(name: String) {
    Text("Hello, $name!")
}

// Android, iOS, Desktop, Web 모두에서 동일하게 동작!
```

### 해결책 3: 선언적 UI

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: $count")
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}
// 상태가 변경되면 UI가 자동으로 업데이트됨
```

---

## 프로젝트 설정 방법

### Step 1: 프로젝트 생성

IntelliJ IDEA에서:
1. File > New > Project
2. Kotlin Multiplatform 선택
3. Web 타겟 선택 + "Share UI" 체크

또는 웹 위저드 사용:
- https://kmp.jetbrains.com/

### Step 2: build.gradle.kts 설정

```kotlin
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    // wasmJs 타겟 설정
    wasmJs {
        browser {
            binaries.executable()
        }
    }

    sourceSets {
        val wasmJsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
            }
        }
    }
}
```

### Step 3: 메인 진입점 작성

```kotlin
// src/wasmJsMain/kotlin/Main.kt
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App()
    }
}

@Composable
fun App() {
    MaterialTheme {
        var count by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Count: $count", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { count++ }) {
                Text("Click me!")
            }
        }
    }
}
```

### Step 4: HTML 파일 설정

```html
<!-- src/wasmJsMain/resources/index.html -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Compose for Web</title>
    <style>
        html, body {
            width: 100%;
            height: 100%;
            margin: 0;
            padding: 0;
            overflow: hidden;
        }
        #ComposeTarget {
            width: 100%;
            height: 100%;
        }
    </style>
</head>
<body>
    <canvas id="ComposeTarget"></canvas>
    <script src="composeApp.js"></script>
</body>
</html>
```

### Step 5: 실행

```bash
# 개발 서버 실행
./gradlew wasmJsBrowserRun

# 프로덕션 빌드
./gradlew wasmJsBrowserDistribution
# 결과물: build/dist/wasmJs/productionExecutable/
```

---

## 브라우저 API 연동

### JavaScript Interop

```kotlin
// external 선언으로 JavaScript 함수 호출
external fun alert(message: String)

// 사용
Button(onClick = { alert("Hello from Kotlin!") }) {
    Text("Show Alert")
}
```

### localStorage 사용

```kotlin
external object localStorage {
    fun getItem(key: String): String?
    fun setItem(key: String, value: String)
    fun removeItem(key: String)
}

// 사용
fun saveData(key: String, value: String) {
    localStorage.setItem(key, value)
}

fun loadData(key: String): String? {
    return localStorage.getItem(key)
}
```

### window 객체 접근

```kotlin
external val window: Window

external class Window {
    val innerWidth: Int
    val innerHeight: Int
    fun open(url: String, target: String): Window?
}

// 사용
val width = window.innerWidth
window.open("https://kotlinlang.org", "_blank")
```

---

## 사용 시나리오

### 1. 기존 앱 웹 확장
- Android/iOS 앱을 웹으로 확장
- 코드 재사용으로 개발 시간 단축

### 2. 내부 도구 개발
- 관리자 대시보드
- 데이터 시각화 도구

### 3. 프로토타입 제작
- 빠른 아이디어 검증
- 브라우저에서 바로 공유

---

## 성능 비교

| 항목 | JavaScript | Kotlin/Wasm |
|------|------------|-------------|
| UI 렌더링 속도 | 기준 | ~3배 빠름 |
| 초기 로드 시간 | 빠름 | 0.25-0.5초 추가 |
| 번들 크기 | 작음 | 더 큼 (Wasm 런타임 포함) |
| 타입 안전성 | 제한적 | 완벽 (Kotlin) |

---

## 주의사항

### 1. 레거시 브라우저 미지원
- IE11, 구형 Safari 미지원
- WasmGC 지원 브라우저 필요 (2024년 이후 브라우저)

### 2. SEO 제한
- Canvas 렌더링은 검색 엔진이 콘텐츠를 읽지 못함
- 공개 웹사이트보다 앱 형태에 적합

### 3. 번들 크기
- Wasm 런타임으로 초기 번들 크기 증가
- 캐싱으로 재방문 시 빠름

### 4. Android 전용 API 사용 불가
- Toast, Context 등 Android 전용 API
- expect/actual 패턴으로 플랫폼별 구현 필요

---

## 연습 문제

### 연습 1: 프로젝트 구조 이해 (쉬움)
wasmJs 타겟 설정을 위한 build.gradle.kts 코드를 완성하세요.

### 연습 2: 코드 변환 (중간)
Android Compose 코드에서 웹에서 사용할 수 없는 API를 식별하고 대안을 제시하세요.

### 연습 3: JavaScript Interop 설계 (어려움)
localStorage를 사용하는 데이터 저장 유틸리티를 설계하세요.

---

## 다음 학습

- [Compose Multiplatform 공식 문서](https://www.jetbrains.com/compose-multiplatform/)
- [Kotlin/Wasm 시작하기](https://kotlinlang.org/docs/wasm-get-started.html)
- [KMP 웹 위저드](https://kmp.jetbrains.com/)

---

## 참고 자료

- [Kotlin/Wasm & Compose Web 2025 가이드](https://www.kmpship.app/blog/kotlin-wasm-and-compose-web-2025)
- [JetBrains Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- [Kotlin/Wasm 예제](https://github.com/Kotlin/kotlin-wasm-examples)
