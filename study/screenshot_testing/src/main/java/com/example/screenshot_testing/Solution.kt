package com.example.screenshot_testing

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * # Solution: Screenshot Testing으로 UI 자동 검증
 *
 * ## 핵심 개념
 *
 * Screenshot Testing(스냅샷 테스트)은 UI의 시각적 정확성을 자동으로 검증하는 방법입니다.
 *
 * 1. **골든 이미지 (Reference Image)**: 올바른 UI를 캡처한 기준 이미지
 * 2. **비교 테스트**: 현재 UI를 골든 이미지와 비교
 * 3. **자동화**: CI/CD에서 자동으로 모든 화면 검증
 *
 * ## 주요 라이브러리
 *
 * | 라이브러리 | 특징 | 장점 |
 * |-----------|------|------|
 * | Paparazzi | JVM 기반, 에뮬레이터 불필요 | 빠른 속도, 간단한 설정 |
 * | Roborazzi | Robolectric 통합 | Activity/Fragment 테스트, 상호작용 가능 |
 * | Google Preview | @Preview 기반 | 가장 간단한 설정 |
 */
@Composable
fun SolutionScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        SolutionExplanationCard()

        HorizontalDivider()

        // Paparazzi 설정 가이드
        PaparazziSetupGuide()

        // Roborazzi 설정 가이드
        RoborazziSetupGuide()

        // 다크/라이트 모드 테스트
        DarkLightModeTestGuide()

        // 디바이스 크기 테스트
        DeviceSizeTestGuide()

        // CI/CD 통합
        CICDIntegrationGuide()

        // 베스트 프랙티스
        BestPracticesGuide()
    }
}

@Composable
private fun SolutionExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Screenshot Testing으로 UI 자동 검증",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = """
                    Screenshot Testing의 장점:

                    1. 자동화: 수십 개의 화면을 몇 초 만에 검증
                    2. 회귀 감지: 1px 단위의 변화도 자동 감지
                    3. 문서화: 골든 이미지가 UI 명세서 역할
                    4. 다양한 설정: 다크모드, 디바이스 크기 자동 테스트

                    아래에서 설정 방법을 확인하세요!
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun PaparazziSetupGuide() {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Camera, contentDescription = null)
                    Text(
                        text = "Paparazzi 설정",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                // Step 1: Gradle 설정
                StepCard(
                    stepNumber = 1,
                    title = "build.gradle.kts 설정",
                    code = """
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
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Step 2: 테스트 작성
                StepCard(
                    stepNumber = 2,
                    title = "테스트 코드 작성 (src/test/)",
                    code = """
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
    fun sampleCard() {
        paparazzi.snapshot {
            SampleCard(
                title = "제목",
                description = "설명 텍스트"
            )
        }
    }
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Step 3: 명령어
                StepCard(
                    stepNumber = 3,
                    title = "Gradle 명령어",
                    code = """
# 골든 이미지 생성/업데이트
./gradlew recordPaparazziDebug

# 현재 UI와 골든 이미지 비교
./gradlew verifyPaparazziDebug

# 모든 스냅샷 삭제
./gradlew deletePaparazziSnapshots
                    """.trimIndent()
                )
            }
        }
    }
}

@Composable
private fun RoborazziSetupGuide() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Android, contentDescription = null)
                    Text(
                        text = "Roborazzi 설정",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Roborazzi는 Robolectric과 통합되어 Activity/Fragment 테스트와 상호작용이 가능합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))

                // Step 1: Gradle 설정
                StepCard(
                    stepNumber = 1,
                    title = "build.gradle.kts 설정",
                    code = """
plugins {
    id("io.github.takahirom.roborazzi") version "1.32.2"
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
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Step 2: 테스트 작성
                StepCard(
                    stepNumber = 2,
                    title = "테스트 코드 작성",
                    code = """
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
    @Config(qualifiers = "+night") // 다크모드
    fun captureScreenDarkMode() {
        // ...
    }
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Step 3: 명령어
                StepCard(
                    stepNumber = 3,
                    title = "Gradle 명령어",
                    code = """
# 골든 이미지 생성
./gradlew recordRoborazziDebug

# 비교 검증
./gradlew verifyRoborazziDebug

# 비교 및 리포트 생성
./gradlew compareRoborazziDebug
                    """.trimIndent()
                )
            }
        }
    }
}

@Composable
private fun DarkLightModeTestGuide() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DarkMode, contentDescription = null)
                    Text(
                        text = "다크/라이트 모드 테스트",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                // Paparazzi 방식
                StepCard(
                    stepNumber = 1,
                    title = "Paparazzi - 테마 변경",
                    code = """
class ThemeTest {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun lightTheme() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Light"
        )
        paparazzi.snapshot(name = "light") {
            MyComponent()
        }
    }

    @Test
    fun darkTheme() {
        paparazzi.unsafeUpdateConfig(
            theme = "android:Theme.Material3.Dark"
        )
        paparazzi.snapshot(name = "dark") {
            MyComponent()
        }
    }
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Roborazzi 방식
                StepCard(
                    stepNumber = 2,
                    title = "Roborazzi - @Config 사용",
                    code = """
@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ThemeParameterizedTest(
    private val darkMode: Boolean
) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun params() = listOf(false, true)
    }

    @Test
    @Config(qualifiers = "+night") // 다크모드 테스트
    fun darkModeTest() {
        // 다크모드에서 테스트
    }

    @Test
    @Config(qualifiers = "-night") // 라이트모드 테스트
    fun lightModeTest() {
        // 라이트모드에서 테스트
    }
}
                    """.trimIndent()
                )
            }
        }
    }
}

@Composable
private fun DeviceSizeTestGuide() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Devices, contentDescription = null)
                    Text(
                        text = "다양한 디바이스 크기 테스트",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                // Paparazzi DeviceConfig
                StepCard(
                    stepNumber = 1,
                    title = "Paparazzi - DeviceConfig",
                    code = """
class DeviceTest {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun phoneScreen() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DeviceConfig.PIXEL_5
        )
        paparazzi.snapshot { MyScreen() }
    }

    @Test
    fun tabletScreen() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DeviceConfig.PIXEL_TABLET
        )
        paparazzi.snapshot { MyScreen() }
    }

    @Test
    fun customDevice() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DeviceConfig(
                screenHeight = 2400,
                screenWidth = 1080,
                density = Density.XXHIGH
            )
        )
        paparazzi.snapshot { MyScreen() }
    }
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Roborazzi 디바이스 설정
                StepCard(
                    stepNumber = 2,
                    title = "Roborazzi - RobolectricDeviceQualifiers",
                    code = """
// 폰 테스트
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class PhoneTest { ... }

// 태블릿 테스트
@Config(qualifiers = RobolectricDeviceQualifiers.MediumTablet)
class TabletTest { ... }

// 폴더블 테스트
@Config(qualifiers = "w600dp-h800dp")
class FoldableTest { ... }
                    """.trimIndent()
                )
            }
        }
    }
}

@Composable
private fun CICDIntegrationGuide() {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CloudSync, contentDescription = null)
                    Text(
                        text = "CI/CD 통합",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                // GitHub Actions 예시
                StepCard(
                    stepNumber = 1,
                    title = "GitHub Actions 설정",
                    code = """
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
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Git LFS 설정
                StepCard(
                    stepNumber = 2,
                    title = "Git LFS 설정 (권장)",
                    code = """
# Git LFS 설치 및 설정
brew install git-lfs
git lfs install --local

# 스냅샷 이미지 추적
git lfs track "**/snapshots/**/*.png"
git add .gitattributes

# 성능 최적화
git config lfs.setlockablereadonly false
                    """.trimIndent()
                )
            }
        }
    }
}

@Composable
private fun BestPracticesGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "베스트 프랙티스",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            BestPracticeItem(
                number = 1,
                title = "작은 컴포넌트 단위로 테스트",
                description = "전체 화면보다 개별 컴포넌트 테스트가 유지보수에 좋습니다."
            )

            BestPracticeItem(
                number = 2,
                title = "의미 있는 조합만 테스트",
                description = "모든 조합이 아닌, 실제로 영향을 주는 설정만 테스트하세요."
            )

            BestPracticeItem(
                number = 3,
                title = "CI에서만 record 실행",
                description = "로컬과 CI 간 렌더링 차이를 방지하려면 CI에서 골든 이미지를 생성하세요."
            )

            BestPracticeItem(
                number = 4,
                title = "이미지 차이 임계값 설정",
                description = "미세한 안티앨리어싱 차이를 무시하도록 0.1% 정도의 임계값을 설정하세요."
            )

            BestPracticeItem(
                number = 5,
                title = "테스트 이름을 명확하게",
                description = "스냅샷 파일명이 테스트 이름이 되므로 의미 있는 이름을 사용하세요."
            )
        }
    }
}

@Composable
private fun StepCard(
    stepNumber: Int,
    title: String,
    code: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = stepNumber.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(8.dp))

            Surface(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(4.dp)
            ) {
                val scrollState = rememberScrollState()
                Text(
                    text = code,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(12.dp),
                    color = Color(0xFFD4D4D4),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun BestPracticeItem(
    number: Int,
    title: String,
    description: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

// ============================================
// 스크린샷 테스트 대상 샘플 컴포넌트들
// ============================================

/**
 * 스크린샷 테스트용 로그인 폼 컴포넌트
 */
@Composable
fun LoginForm(
    email: String = "",
    password: String = "",
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "로그인",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("이메일") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("비밀번호") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(if (isLoading) "로그인 중..." else "로그인")
            }
        }
    }
}

/**
 * 스크린샷 테스트용 사용자 프로필 카드
 */
@Composable
fun UserProfileCard(
    name: String,
    email: String,
    avatarUrl: String? = null,
    isVerified: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (isVerified) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "인증됨",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
