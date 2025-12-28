# Screenshot Testing: 올바른 라이브러리 선택 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `compose_testing` | Compose UI Testing과 Semantics 기반 테스트 | [📚 학습하기](../../testing/compose_testing/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

> UI 회귀 버그를 자동으로 감지하고, 상황에 맞는 최적의 스크린샷 테스트 라이브러리를 선택하세요.

---

## 개요

**Screenshot Testing**(스크린샷 테스트 또는 스냅샷 테스트)은 UI의 시각적 정확성을 자동으로 검증하는 테스트 방법입니다.

### 작동 원리

```
코드 변경
    |
Screenshot 테스트 실행
    |
현재 UI 렌더링 -> 골든 이미지와 비교
    |
    +-- 동일 -> 테스트 통과
    +-- 차이 발생 -> 테스트 실패 + diff 이미지 생성
```

### 핵심 가치

| 수동 테스트 | Screenshot 테스트 |
|------------|------------------|
| 매번 빌드 후 직접 확인 | 자동으로 수십 개 화면 검증 |
| 3-5분/화면 | 1초/화면 |
| 휴먼 에러 발생 가능 | 1px 단위 차이도 감지 |
| 일부 상태만 확인 | 모든 상태 자동 확인 |

---

## 라이브러리 옵션

Android에서 Screenshot Testing을 위한 3가지 주요 라이브러리가 있습니다. 각 라이브러리의 특징을 이해하고 프로젝트에 맞는 것을 선택하세요.

---

### 옵션 1: Paparazzi

[CashApp](https://github.com/cashapp/paparazzi)에서 개발한 JVM 기반 스크린샷 테스트 라이브러리입니다.

#### 특징

- **JVM 기반**: 에뮬레이터/디바이스 없이 테스트 실행
- **LayoutLib 사용**: Android Studio Preview와 동일한 렌더링 엔진
- **빠른 속도**: 가장 빠른 테스트 실행 속도

#### 적합한 상황

- Compose UI 컴포넌트만 테스트
- 간단하고 빠른 설정 원함
- Activity/Fragment 테스트가 불필요
- Robolectric을 사용하지 않는 프로젝트

#### 제약 사항

- Activity/Fragment 테스트 불가
- **Robolectric과 호환 불가** (중요!)
- 상호작용(클릭, 스크롤) 테스트 불가
- NavHost 등 일부 Composable 렌더링 제한

#### 설정 방법

```kotlin
// 프로젝트 레벨 build.gradle.kts
plugins {
    id("app.cash.paparazzi") version "1.3.5" apply false
}

// 모듈 레벨 build.gradle.kts
plugins {
    id("app.cash.paparazzi")
}

android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}
```

#### 테스트 코드 예시

```kotlin
class MyScreenshotTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material3.Light"
    )

    @Test
    fun sampleButton() {
        paparazzi.snapshot {
            SampleButton(text = "클릭하세요")
        }
    }
}
```

#### 명령어

```bash
# 골든 이미지 생성/업데이트
./gradlew recordPaparazziDebug

# 현재 UI와 골든 이미지 비교
./gradlew verifyPaparazziDebug
```

---

### 옵션 2: Roborazzi

[takahirom](https://github.com/takahirom/roborazzi)이 개발한 Robolectric 기반 스크린샷 테스트 라이브러리입니다. Google의 [Now in Android](https://github.com/android/nowinandroid) 프로젝트에서 사용됩니다.

#### 특징

- **Robolectric 통합**: Activity, Fragment, Hilt와 함께 사용 가능
- **상호작용 지원**: Espresso를 통한 클릭, 스크롤 등 테스트 가능
- **@Preview 스캔**: ComposablePreviewScanner와 통합하여 Preview 자동 테스트

#### 적합한 상황

- Activity/Fragment 레벨 테스트 필요
- Dagger Hilt를 사용하는 프로젝트
- 상호작용 후 UI 상태 테스트 필요
- 대규모 프로젝트 (Now in Android 참고)

#### 제약 사항

- Paparazzi보다 설정이 복잡
- 일부 하드웨어 렌더링 이슈 가능

#### 설정 방법

```kotlin
// 프로젝트 레벨 build.gradle.kts
plugins {
    id("io.github.takahirom.roborazzi") version "1.32.2" apply false
}

// 모듈 레벨 build.gradle.kts
plugins {
    id("io.github.takahirom.roborazzi")
}

android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
}

dependencies {
    testImplementation("io.github.takahirom.roborazzi:roborazzi:1.32.2")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:1.32.2")
    testImplementation("org.robolectric:robolectric:4.11.1")
}
```

#### 테스트 코드 예시

```kotlin
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class MyRoborazziTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun captureScreen() {
        composeTestRule.setContent {
            MyTheme {
                MyScreen()
            }
        }

        composeTestRule
            .onRoot()
            .captureRoboImage()
    }

    @Test
    fun captureAfterInteraction() {
        composeTestRule.setContent {
            MyTheme { LoginScreen() }
        }

        // 상호작용 후 스크린샷
        composeTestRule
            .onNodeWithText("로그인")
            .performClick()

        composeTestRule.onRoot().captureRoboImage()
    }
}
```

#### 명령어

```bash
# 골든 이미지 생성
./gradlew recordRoborazziDebug

# 비교 검증
./gradlew verifyRoborazziDebug

# 비교 리포트 생성
./gradlew compareRoborazziDebug
```

---

### 옵션 3: Google Compose Preview Screenshot Testing

@Preview 어노테이션을 활용한 Google 공식 스크린샷 테스트 도구입니다.

#### 특징

- **@Preview 재사용**: 기존 Preview를 그대로 테스트에 활용
- **가장 간단한 설정**: 최소한의 설정으로 시작 가능
- **공식 지원**: Android 공식 도구

#### 적합한 상황

- @Preview를 이미 많이 작성한 프로젝트
- 가장 간단한 설정 원함
- Compose만 사용하는 프로젝트

#### 제약 사항

- screenshotTest 소스셋에 Preview 필요 (코드와 분리됨)
- Activity/Fragment 테스트 불가
- 상호작용 테스트 불가

#### 설정 방법

```kotlin
// 모듈 레벨 build.gradle.kts
android {
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    screenshotTestImplementation("androidx.compose.ui:ui-tooling")
}
```

#### 테스트 코드 예시

```kotlin
// src/screenshotTest/.../MyPreviewScreenshots.kt

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    MyTheme {
        MyButton(text = "클릭")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ButtonDarkPreview() {
    MyTheme {
        MyButton(text = "클릭")
    }
}
```

#### 명령어

```bash
# 스크린샷 생성
./gradlew updateDebugScreenshotTest

# 스크린샷 검증
./gradlew validateDebugScreenshotTest
```

---

## 상세 비교표

| 특성 | Paparazzi | Roborazzi | Google Preview |
|------|-----------|-----------|----------------|
| **기반 기술** | LayoutLib (JVM) | Robolectric | @Preview |
| **에뮬레이터** | 불필요 | 불필요 | 불필요 |
| **실행 속도** | 매우 빠름 | 빠름 | 빠름 |
| **Activity/Fragment** | 불가 | **가능** | 불가 |
| **상호작용 테스트** | 불가 | **가능 (Espresso)** | 불가 |
| **Hilt 통합** | 불가 | **가능** | 불가 |
| **Robolectric 호환** | **불가** | 가능 | 가능 |
| **설정 난이도** | 쉬움 | 보통 | 매우 쉬움 |
| **@Preview 스캔** | 별도 설정 | 지원 | 네이티브 |
| **대표 사용처** | CashApp | Now in Android | 공식 예제 |

---

## 상황별 선택 가이드

### 상황 1: Compose만 사용, 빠르게 시작하고 싶음
**-> Paparazzi 추천**

```
이유:
- 가장 빠른 설정
- 빠른 테스트 실행 속도
- Compose 컴포넌트 테스트에 충분
```

### 상황 2: Activity/Fragment도 테스트해야 함
**-> Roborazzi 추천**

```
이유:
- Activity/Fragment 스크린샷 지원
- 전체 화면 통합 테스트 가능
```

### 상황 3: Dagger Hilt를 사용하는 프로젝트
**-> Roborazzi 추천**

```
이유:
- Hilt와 완벽히 통합
- DI가 포함된 화면 테스트 가능
```

### 상황 4: 클릭 후 UI 변화를 테스트해야 함
**-> Roborazzi 추천**

```
이유:
- Espresso 상호작용 지원
- 상태 변화 후 스크린샷 캡처 가능
```

### 상황 5: @Preview를 최대한 활용하고 싶음
**-> Google Preview 또는 Roborazzi + ComposablePreviewScanner**

```
이유:
- 기존 Preview 재사용
- 중복 코드 최소화
```

### 상황 6: 대규모 프로젝트 (Now in Android 스타일)
**-> Roborazzi 추천**

```
이유:
- Google 공식 샘플에서 사용
- 검증된 확장성
- 다양한 테스트 옵션
```

---

## 의사결정 플로우차트

```
시작: Screenshot Testing 도입
    |
    +-- Robolectric 이미 사용 중? ----Yes----> Roborazzi
    |
    +-- Activity/Fragment 테스트 필요? --Yes--> Roborazzi
    |
    +-- Hilt 사용 중? ---------------Yes----> Roborazzi
    |
    +-- 상호작용 테스트 필요? --------Yes----> Roborazzi
    |
    +-- @Preview만으로 충분? --------Yes----> Google Preview
    |
    +-- 그 외 (Compose 컴포넌트만) ----------> Paparazzi
```

---

## 공통 패턴

### 다크/라이트 모드 테스트

#### Paparazzi

```kotlin
class ThemeTest {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun componentLightTheme() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Light"
        )
        paparazzi.snapshot(name = "light") {
            MyComponent()
        }
    }

    @Test
    fun componentDarkTheme() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Dark"
        )
        paparazzi.snapshot(name = "dark") {
            MyComponent()
        }
    }
}
```

#### Roborazzi

```kotlin
@Test
@Config(qualifiers = "-night")  // 라이트 모드
fun componentLightMode() { ... }

@Test
@Config(qualifiers = "+night")  // 다크 모드
fun componentDarkMode() { ... }
```

---

### 다양한 디바이스 크기 테스트

#### Paparazzi DeviceConfig

```kotlin
@Test
fun onPhone() {
    paparazzi.unsafeUpdateConfig(deviceConfig = DeviceConfig.PIXEL_5)
    paparazzi.snapshot { MyScreen() }
}

@Test
fun onTablet() {
    paparazzi.unsafeUpdateConfig(deviceConfig = DeviceConfig.PIXEL_TABLET)
    paparazzi.snapshot { MyScreen() }
}

@Test
fun landscape() {
    paparazzi.unsafeUpdateConfig(
        deviceConfig = DeviceConfig.PIXEL_5.copy(
            orientation = ScreenOrientation.LANDSCAPE
        )
    )
    paparazzi.snapshot { MyScreen() }
}
```

#### Roborazzi RobolectricDeviceQualifiers

```kotlin
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class PhoneTest { ... }

@Config(qualifiers = RobolectricDeviceQualifiers.MediumTablet)
class TabletTest { ... }
```

---

## CI/CD 통합

### GitHub Actions 예시

```yaml
name: Screenshot Tests

on:
  pull_request:
    branches: [ main ]

jobs:
  screenshot-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          lfs: true

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Verify Screenshots
        run: ./gradlew verifyPaparazziDebug

      - name: Upload Diff on Failure
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: screenshot-diffs
          path: '**/build/paparazzi/failures/'
```

### CI 환경 주의사항

로컬과 CI 간 렌더링 차이가 발생할 수 있습니다:
- **해결책 1**: CI에서만 골든 이미지 생성
- **해결책 2**: 이미지 차이 임계값 설정 (0.1% 정도)

```kotlin
val paparazzi = Paparazzi(
    maxPercentDifference = 0.1  // 0.1% 이하 차이는 무시
)
```

---

## 베스트 프랙티스

### 1. 작은 컴포넌트 단위로 테스트

```kotlin
// Good: 개별 컴포넌트 테스트
@Test fun button() { ... }
@Test fun card() { ... }
@Test fun loginForm() { ... }

// Avoid: 전체 화면 테스트 (변경에 취약)
@Test fun entireHomeScreen() { ... }
```

### 2. 의미 있는 조합만 테스트

```kotlin
// Good: 실제로 영향을 주는 설정만
@Test fun buttonLight() { ... }
@Test fun buttonDark() { ... }

// Avoid: 무의미한 조합
@Test fun buttonLightPixel5() { ... }
@Test fun buttonLightPixel6() { ... }  // 차이 없음
```

### 3. CI에서만 record 실행

로컬과 CI 간 렌더링 차이를 방지합니다.

### 4. 테스트 이름을 명확하게

```kotlin
// Good: 스냅샷 파일명이 의미 있음
@Test fun loginForm_emptyState() { ... }
@Test fun loginForm_withError() { ... }
@Test fun loginForm_loading() { ... }

// Bad: 스냅샷 파일명이 모호함
@Test fun test1() { ... }
```

### 5. Git LFS 설정 (권장)

스크린샷 이미지가 많아지면 Git 저장소가 느려집니다.

```bash
git lfs install --local
git lfs track "**/snapshots/**/*.png"
```

---

## 연습 문제

### 연습 1: 기본 스냅샷 테스트 (초급)

Paparazzi를 사용하여 `SampleButton` 컴포넌트의 스냅샷 테스트를 작성해보세요.

### 연습 2: 다크/라이트 모드 테스트 (중급)

동일한 `SampleCard` 컴포넌트를 다크 모드와 라이트 모드에서 각각 스냅샷으로 캡처해보세요.

### 연습 3: 다양한 디바이스 크기 테스트 (중급)

`ResponsiveLayout` 컴포넌트를 폰과 태블릿 크기에서 테스트해보세요.

### 연습 4: 라이브러리 선택 (고급)

다음 상황에서 어떤 라이브러리를 선택해야 할지 결정하고 이유를 설명하세요:
- 상황 A: 새 프로젝트, Compose만 사용, 빠른 설정 원함
- 상황 B: 기존 프로젝트, Hilt 사용 중, Activity 테스트 필요
- 상황 C: @Preview가 100개 이상, 이를 테스트로 활용하고 싶음

---

## 참고 자료

### 공식 문서
- [Android Screenshot Testing](https://developer.android.com/training/testing/ui-tests/screenshot)
- [Compose Preview Screenshot Testing](https://developer.android.com/studio/preview/compose-screenshot-testing)

### 라이브러리
- [Paparazzi GitHub](https://github.com/cashapp/paparazzi)
- [Roborazzi GitHub](https://github.com/takahirom/roborazzi)
- [ComposablePreviewScanner](https://github.com/sergio-sastre/ComposablePreviewScanner)

### 관련 글
- [Comparing Snapshot Testing Libraries](https://medium.com/@natalia.kulbaka/comparing-snapshot-testing-libraries-paparazzi-roborazzi-compose-previews-screenshot-testing-b7c3b47f7f59)
- [Master Screenshot Testing on Android](https://academy.droidcon.com/course/master-screenshot-testing-on-android-comparing-paparazzi-roborazzi-and-compose-preview-tools)

---

## 다음 학습

- [Compose UI Testing](../../testing/compose_testing/README.md) - 시맨틱 기반 UI 테스트
- [Preview](../../basics/preview/README.md) - @Preview 어노테이션 활용
