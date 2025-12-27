package com.example.screenshot_testing

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.screenshot_testing.ui.theme.ScreenshotTestingTheme
import org.junit.Rule
import org.junit.Test

/**
 * Screenshot Testing 예제
 *
 * 이 테스트 클래스는 Paparazzi를 사용한 스크린샷 테스트의 기본 패턴을 보여줍니다.
 *
 * ## 실행 방법
 *
 * ```bash
 * # 골든 이미지 생성/업데이트
 * ./gradlew :study:screenshot_testing:recordPaparazziDebug
 *
 * # 현재 UI와 골든 이미지 비교
 * ./gradlew :study:screenshot_testing:verifyPaparazziDebug
 * ```
 *
 * ## 골든 이미지 위치
 *
 * 생성된 스냅샷은 다음 경로에 저장됩니다:
 * `src/test/snapshots/images/`
 */
class ScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material3.Light"
    )

    // ====================================
    // 기본 컴포넌트 테스트
    // ====================================

    @Test
    fun sampleButton() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                SampleButton(text = "확인")
            }
        }
    }

    @Test
    fun sampleButtonDisabled() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                SampleButton(text = "비활성화", enabled = false)
            }
        }
    }

    @Test
    fun sampleCard() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                SampleCard(
                    title = "제목",
                    description = "이것은 설명 텍스트입니다."
                )
            }
        }
    }

    // ====================================
    // 다크/라이트 모드 테스트
    // ====================================

    @Test
    fun loginFormLight() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Light"
        )
        paparazzi.snapshot(name = "login_form_light") {
            ScreenshotTestingTheme(darkTheme = false, dynamicColor = false) {
                LoginForm(
                    email = "user@example.com",
                    password = ""
                )
            }
        }
    }

    @Test
    fun loginFormDark() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Dark"
        )
        paparazzi.snapshot(name = "login_form_dark") {
            ScreenshotTestingTheme(darkTheme = true, dynamicColor = false) {
                LoginForm(
                    email = "user@example.com",
                    password = ""
                )
            }
        }
    }

    // ====================================
    // 다양한 상태 테스트
    // ====================================

    @Test
    fun loginFormLoading() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                LoginForm(
                    email = "user@example.com",
                    password = "password",
                    isLoading = true
                )
            }
        }
    }

    @Test
    fun loginFormWithError() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                LoginForm(
                    email = "user@example.com",
                    password = "wrong",
                    errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다."
                )
            }
        }
    }

    // ====================================
    // 사용자 프로필 테스트
    // ====================================

    @Test
    fun userProfileVerified() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                UserProfileCard(
                    name = "홍길동",
                    email = "hong@example.com",
                    isVerified = true
                )
            }
        }
    }

    @Test
    fun userProfileNotVerified() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                UserProfileCard(
                    name = "김철수",
                    email = "kim@example.com",
                    isVerified = false
                )
            }
        }
    }

    // ====================================
    // 연습 문제 대상 컴포넌트 테스트
    // ====================================

    @Test
    fun practiceTargetButton() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                PracticeTargetButton()
            }
        }
    }

    @Test
    fun practiceTargetCard() {
        paparazzi.snapshot {
            ScreenshotTestingTheme {
                PracticeTargetCard()
            }
        }
    }

    @Test
    fun practiceTargetResponsiveLayoutPhone() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DeviceConfig.PIXEL_5
        )
        paparazzi.snapshot(name = "responsive_phone") {
            ScreenshotTestingTheme {
                PracticeTargetResponsiveLayout()
            }
        }
    }

    @Test
    fun practiceTargetResponsiveLayoutTablet() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DeviceConfig.PIXEL_TABLET
        )
        paparazzi.snapshot(name = "responsive_tablet") {
            ScreenshotTestingTheme {
                PracticeTargetResponsiveLayout()
            }
        }
    }

    // ====================================
    // 테마 인식 컴포넌트 테스트
    // ====================================

    @Test
    fun themeAwareComponentLight() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Light"
        )
        paparazzi.snapshot(name = "theme_aware_light") {
            ScreenshotTestingTheme(darkTheme = false, dynamicColor = false) {
                ThemeAwareComponent()
            }
        }
    }

    @Test
    fun themeAwareComponentDark() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Dark"
        )
        paparazzi.snapshot(name = "theme_aware_dark") {
            ScreenshotTestingTheme(darkTheme = true, dynamicColor = false) {
                ThemeAwareComponent()
            }
        }
    }
}
