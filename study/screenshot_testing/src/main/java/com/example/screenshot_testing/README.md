# Screenshot Testing 학습

> 스크린샷 테스트로 UI 회귀 버그를 자동으로 감지하고, 다양한 설정에서 일관된 UI를 보장하세요.

---

## 개념

**Screenshot Testing**(스크린샷 테스트 또는 스냅샷 테스트)은 UI의 시각적 정확성을 자동으로 검증하는 테스트 방법입니다.

### 작동 원리

1. **골든 이미지 (Reference Image)**: 올바른 UI 상태를 캡처한 기준 이미지
2. **비교 테스트**: 현재 UI를 골든 이미지와 픽셀 단위로 비교
3. **차이 감지**: 변경 사항이 있으면 테스트 실패 및 차이점 리포트 생성

```
코드 변경
    ↓
Screenshot 테스트 실행
    ↓
현재 UI 렌더링 → 골든 이미지와 비교
    ↓
    ├── 동일 → 테스트 통과
    └── 차이 발생 → 테스트 실패 + diff 이미지 생성
```

---

## 왜 필요한가?

### 1. 수동 UI 검증의 한계

| 수동 테스트 | Screenshot 테스트 |
|------------|------------------|
| 매번 빌드 후 직접 확인 | 자동으로 수십 개 화면 검증 |
| 3-5분/화면 | 1초/화면 |
| 휴먼 에러 발생 가능 | 1px 단위 차이도 감지 |
| 일부 상태만 확인 | 모든 상태 자동 확인 |

### 2. UI 회귀 버그 자동 감지

```kotlin
// padding이 16dp에서 12dp로 실수로 변경되면?
// 색상이 #6650a4에서 #5D4B9E로 바뀌면?
// Screenshot 테스트가 즉시 감지!
```

### 3. 다양한 설정 자동 테스트

- 다크모드 / 라이트모드
- 폰 / 태블릿 / 폴더블
- 일반 폰트 / 큰 폰트 (접근성)
- LTR / RTL 언어

모든 조합 = 36가지 이상 → 수동으로는 불가능!

---

## 주요 라이브러리 비교

### 비교 표

| 특성 | Paparazzi | Roborazzi | Google Preview |
|------|-----------|-----------|----------------|
| **기반** | LayoutLib (JVM) | Robolectric | @Preview |
| **속도** | 매우 빠름 | 빠름 | 빠름 |
| **에뮬레이터** | 불필요 | 불필요 | 불필요 |
| **Activity/Fragment** | 불가 | 가능 | 불가 |
| **상호작용** | 불가 | 가능 (Espresso) | 불가 |
| **Hilt 통합** | 불가 | 가능 | 불가 |
| **설정 난이도** | 쉬움 | 보통 | 매우 쉬움 |

### Paparazzi

[CashApp](https://github.com/cashapp/paparazzi)에서 개발한 JVM 기반 스크린샷 테스트 라이브러리입니다.

**장점:**
- 빠른 속도 (에뮬레이터 불필요)
- 간단한 설정
- Compose와 View 모두 지원
- 스크롤 가능한 컨텐츠 스냅샷 지원

**단점:**
- Activity/Fragment 테스트 불가
- Robolectric과 호환 불가
- 상호작용 테스트 불가

### Roborazzi

[takahirom](https://github.com/takahirom/roborazzi)이 개발한 Robolectric 기반 스크린샷 테스트 라이브러리입니다. Google의 [Now in Android](https://github.com/android/nowinandroid)에서 사용됩니다.

**장점:**
- Activity/Fragment 테스트 가능
- Espresso 상호작용 지원
- Dagger Hilt 통합 가능
- Compose Preview 자동 스캔 지원

**단점:**
- Paparazzi보다 설정이 복잡
- 일부 하드웨어 렌더링 이슈

### Google Compose Preview Screenshot Testing

@Preview 기반의 가장 간단한 스크린샷 테스트 방법입니다.

**장점:**
- 가장 간단한 설정
- @Preview 재사용

**단점:**
- screenshotTest 소스셋에 Preview 필요
- Preview가 실제 코드와 분리됨

---

## Paparazzi 설정

### 1. Gradle 설정

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

### 2. 테스트 작성

```kotlin
// src/test/java/.../MyScreenshotTest.kt

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

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

    @Test
    fun sampleButtonDisabled() {
        paparazzi.snapshot {
            SampleButton(text = "비활성화", enabled = false)
        }
    }
}
```

### 3. 명령어

```bash
# 골든 이미지 생성/업데이트
./gradlew recordPaparazziDebug

# 현재 UI와 골든 이미지 비교
./gradlew verifyPaparazziDebug

# 스냅샷 삭제
./gradlew deletePaparazziSnapshots
```

---

## Roborazzi 설정

### 1. Gradle 설정

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

### 2. 테스트 작성

```kotlin
// src/test/java/.../MyRoborazziTest.kt

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
}
```

### 3. 명령어

```bash
# 골든 이미지 생성
./gradlew recordRoborazziDebug

# 비교 검증
./gradlew verifyRoborazziDebug

# 비교 리포트 생성
./gradlew compareRoborazziDebug
```

---

## 다크/라이트 모드 테스트

### Paparazzi

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

### Roborazzi

```kotlin
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ThemeTest {

    @Test
    @Config(qualifiers = "-night")  // 라이트 모드
    fun componentLightMode() {
        // ...
    }

    @Test
    @Config(qualifiers = "+night")  // 다크 모드
    fun componentDarkMode() {
        // ...
    }
}
```

---

## 다양한 디바이스 크기 테스트

### Paparazzi DeviceConfig

```kotlin
class DeviceTest {
    @get:Rule
    val paparazzi = Paparazzi()

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

    @Test
    fun customDevice() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DeviceConfig(
                screenHeight = 2400,
                screenWidth = 1080,
                density = Density.XXHIGH,
                fontScale = 1.5f  // 큰 폰트
            )
        )
        paparazzi.snapshot { MyScreen() }
    }
}
```

### Roborazzi RobolectricDeviceQualifiers

```kotlin
// 폰
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class PhoneTest { ... }

// 태블릿
@Config(qualifiers = RobolectricDeviceQualifiers.MediumTablet)
class TabletTest { ... }

// 폴더블
@Config(qualifiers = "w600dp-h800dp")
class FoldableTest { ... }
```

---

## CI/CD 통합

### GitHub Actions 예시

```yaml
# .github/workflows/screenshot-test.yml
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
          lfs: true  # Git LFS 사용 시

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

### Git LFS 설정 (권장)

스크린샷 이미지(PNG)가 많아지면 Git 저장소가 느려질 수 있습니다.

```bash
# Git LFS 설치
brew install git-lfs

# 프로젝트에 설정
git lfs install --local
git lfs track "**/snapshots/**/*.png"
git add .gitattributes

# 성능 최적화
git config lfs.setlockablereadonly false
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
@Test fun buttonLightPixelTablet() { ... }  // 차이 없음
```

### 3. CI에서만 record 실행

로컬과 CI 간 렌더링 차이를 방지하려면:

```yaml
# CI에서만 골든 이미지 업데이트
- name: Record Screenshots
  if: github.event.inputs.update_snapshots == 'true'
  run: ./gradlew recordPaparazziDebug
```

### 4. 이미지 차이 임계값 설정

```kotlin
val paparazzi = Paparazzi(
    maxPercentDifference = 0.1  // 0.1% 이하 차이는 무시
)
```

### 5. 테스트 이름을 명확하게

```kotlin
// Good: 스냅샷 파일명이 의미 있음
@Test fun loginForm_emptyState() { ... }
@Test fun loginForm_withError() { ... }
@Test fun loginForm_loading() { ... }

// Bad: 스냅샷 파일명이 모호함
@Test fun test1() { ... }
@Test fun test2() { ... }
```

---

## 연습 문제

### 연습 1: 기본 스냅샷 테스트

Paparazzi를 사용하여 `SampleButton` 컴포넌트의 스냅샷 테스트를 작성해보세요.

### 연습 2: 다크/라이트 모드 테스트

동일한 `SampleCard` 컴포넌트를 다크 모드와 라이트 모드에서 각각 스냅샷으로 캡처해보세요.

### 연습 3: 다양한 디바이스 크기 테스트

`ResponsiveLayout` 컴포넌트를 폰과 태블릿 크기에서 테스트하여 반응형 레이아웃이 올바르게 동작하는지 확인해보세요.

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
- [Roborazzi: Elevating Android Visual Testing](https://medium.com/@takahirom/roborazzi-elevating-android-visual-testing-to-the-next-level-46ec56b24055)
- [Master Screenshot Testing on Android](https://academy.droidcon.com/course/master-screenshot-testing-on-android-comparing-paparazzi-roborazzi-and-compose-preview-tools)

---

## 다음 학습

- [Compose UI Testing](../compose_testing/src/main/java/com/example/compose_testing/README.md) - 시맨틱 기반 UI 테스트
- [Preview](../preview/src/main/java/com/example/preview/README.md) - @Preview 어노테이션 활용
