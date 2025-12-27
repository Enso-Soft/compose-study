package com.example.baseline_profiles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 해결책: Baseline Profiles 적용 방법
 *
 * 이 화면에서는 Baseline Profiles를 적용하는 전체 과정을
 * 단계별로 안내합니다.
 */
@Composable
fun SolutionScreen() {
    var currentStep by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 제목
        Text(
            text = "Solution: Baseline Profiles 적용",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "30%+ 성능 향상을 위한 단계별 가이드",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 적용 후 효과 요약
        PerformanceImprovementCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 단계별 가이드
        StepNavigator(
            currentStep = currentStep,
            onStepChange = { currentStep = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 단계 내용
        when (currentStep) {
            0 -> Step1_ModuleSetup()
            1 -> Step2_AppModuleConfig()
            2 -> Step3_ProfileGenerator()
            3 -> Step4_ComposeReportDrawn()
            4 -> Step5_GenerateAndDeploy()
            5 -> Step6_CICDIntegration()
        }
    }
}

@Composable
private fun PerformanceImprovementCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Baseline Profiles 적용 효과",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "첫 실행부터 30%+ 성능 향상, Meta 사례: 최대 40%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun StepNavigator(
    currentStep: Int,
    onStepChange: (Int) -> Unit
) {
    val steps = listOf(
        "1. 모듈 설정",
        "2. App 설정",
        "3. Generator",
        "4. ReportDrawn",
        "5. 생성/배포",
        "6. CI/CD"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        steps.forEachIndexed { index, step ->
            FilterChip(
                selected = currentStep == index,
                onClick = { onStepChange(index) },
                label = { Text(step, maxLines = 1) }
            )
        }
    }
}

@Composable
private fun rememberScrollState() = androidx.compose.foundation.rememberScrollState()

@Composable
private fun Step1_ModuleSetup() {
    StepCard(
        title = "Step 1: Baseline Profile Generator 모듈 생성",
        description = "Android Studio Iguana 이상에서 Baseline Profile Generator 템플릿 사용"
    ) {
        Text(
            text = "1. File > New > New Module 선택",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "2. Baseline Profile Generator 템플릿 선택",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "3. Target application에서 앱 모듈 지정",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "또는 수동으로 build.gradle.kts 설정:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
plugins {
    id("com.android.test")
    id("androidx.baselineprofile")
}

android {
    namespace = "com.example.baselineprofile"
    targetProjectPath = ":app"

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6api31") {
            device = "Pixel 6"
            apiLevel = 31
            systemImageSource = "aosp"
        }
    }
}

baselineProfile {
    managedDevices += "pixel6api31"
    useConnectedDevices = false
}

dependencies {
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.benchmark:benchmark-macro-junit4:1.4.1")
}
            """.trimIndent()
        )
    }
}

@Composable
private fun Step2_AppModuleConfig() {
    StepCard(
        title = "Step 2: App 모듈 설정",
        description = "ProfileInstaller 의존성 추가 및 baselineProfile 플러그인 적용"
    ) {
        Text(
            text = "app/build.gradle.kts:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
plugins {
    id("com.android.application")
    id("androidx.baselineprofile")
}

dependencies {
    // ProfileInstaller: Cloud Profiles 없는 환경에서 필수
    implementation("androidx.profileinstaller:profileinstaller:1.4.1")

    // Baseline Profile 모듈 연결
    baselineProfile(project(":baselineprofile"))
}

baselineProfile {
    // 빌드 시 자동 생성 (선택사항)
    automaticGenerationDuringBuild = true
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard(
            title = "ProfileInstaller가 하는 일",
            content = "Google Play가 아닌 곳(사이드로드, 사내 배포 등)에서 설치해도 " +
                    "Baseline Profile을 적용할 수 있게 해줍니다. " +
                    "설치 후 백그라운드에서 프로필을 컴파일합니다."
        )
    }
}

@Composable
private fun Step3_ProfileGenerator() {
    StepCard(
        title = "Step 3: BaselineProfileGenerator 작성",
        description = "Critical User Journey를 정의하여 프로필 생성"
    ) {
        Text(
            text = "BaselineProfileGenerator.kt:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateProfile() {
        rule.collect(
            packageName = "com.example.app",
            // Startup Profile에도 포함 (DEX 레이아웃 최적화)
            includeInStartupProfile = true,
            profileBlock = {
                // 1. 앱 시작
                startActivityAndWait()

                // 2. 핵심 사용자 여정 (Critical User Journeys)

                // 로그인 화면
                device.findObject(By.text("Login")).click()
                device.waitForIdle()

                // 메인 화면에서 스크롤
                device.findObject(By.res("content_list"))
                    .scroll(Direction.DOWN, 2f)
                device.waitForIdle()

                // 상세 화면으로 이동
                device.findObject(By.res("item_0")).click()
                device.waitForIdle()
            }
        )
    }
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard(
            title = "Critical User Journey란?",
            content = "사용자가 앱에서 가장 자주 수행하는 핵심 경로입니다. " +
                    "앱 시작, 로그인, 메인 피드 스크롤, 검색 등 " +
                    "첫 5분 내에 대부분의 사용자가 거치는 경로를 포함하세요."
        )
    }
}

@Composable
private fun Step4_ComposeReportDrawn() {
    StepCard(
        title = "Step 4: Compose에서 ReportDrawn 사용",
        description = "비동기 데이터 로드 완료 시점을 정확히 보고"
    ) {
        Text(
            text = "데이터 로드 후 완료 보고:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
@Composable
fun MainScreen(data: List<Item>) {
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            isReady = true
        }
    }

    // 조건이 true가 되면 완료 보고
    ReportDrawnWhen { isReady }

    LazyColumn {
        items(data) { item ->
            ItemCard(item)
        }
    }
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "suspend 함수 완료 후 보고:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
@Composable
fun AsyncDataScreen() {
    // suspend 함수가 완료되면 자동으로 완료 보고
    ReportDrawnAfter {
        repository.loadInitialData()
    }

    // UI 렌더링
    ContentView()
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard(
            title = "왜 ReportDrawn이 중요한가?",
            content = "Baseline Profile Generator가 '앱이 완전히 로드됨'을 " +
                    "정확히 인식하려면 ReportDrawn이 필요합니다. " +
                    "이를 통해 시작 시간(TTFD)을 정확히 측정할 수 있습니다."
        )
    }
}

@Composable
private fun Step5_GenerateAndDeploy() {
    StepCard(
        title = "Step 5: 프로필 생성 및 배포",
        description = "Gradle 명령으로 프로필 생성 및 확인"
    ) {
        Text(
            text = "프로필 생성 명령어:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
# Baseline Profile 생성
./gradlew :app:generateBaselineProfile

# 특정 빌드 변형
./gradlew :app:generateReleaseBaselineProfile

# 벤치마크 실행 (성능 측정)
./gradlew :benchmark:connectedAndroidTest
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "생성된 파일 위치:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
app/src/main/baseline-prof.txt
app/src/release/generated/baselineProfiles/baseline-prof.txt
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "baseline-prof.txt 예시:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
# H = Hot (자주 호출)
# S = Startup (시작 시 필요)
# P = Post-startup (시작 직후)
# L = 클래스 타입

HSPLcom/example/app/MainActivity;->onCreate(Landroid/os/Bundle;)V
HSPLcom/example/app/ui/HomeScreen;->HomeScreen(Landroidx/compose/runtime/Composer;I)V
PLcom/example/app/data/Repository;->loadData()Ljava/util/List;

# 패키지 전체 포함 (와일드카드)
Lcom/example/app/ui/**;
            """.trimIndent()
        )
    }
}

@Composable
private fun Step6_CICDIntegration() {
    StepCard(
        title = "Step 6: CI/CD 통합",
        description = "GitHub Actions 등에서 자동으로 프로필 생성"
    ) {
        Text(
            text = "GitHub Actions 워크플로우:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeSnippet(
            code = """
name: Generate Baseline Profile

on:
  push:
    branches: [main]
  workflow_dispatch:

jobs:
  baseline-profile:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Accept licenses
        run: yes | sdkmanager --licenses

      - name: Generate Baseline Profile
        run: ./gradlew :app:generateBaselineProfile

      - name: Commit Profile
        run: |
          git config user.name github-actions
          git config user.email github-actions@github.com
          git add app/src/main/baseline-prof.txt
          git commit -m "chore: update baseline profile"
          git push
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard(
            title = "CI/CD 자동화의 장점",
            content = "매 릴리즈마다 최신 Critical User Journey를 반영한 " +
                    "Baseline Profile이 자동으로 생성됩니다. " +
                    "새로운 기능이 추가되어도 성능 최적화가 유지됩니다."
        )
    }
}

@Composable
private fun StepCard(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun CodeSnippet(code: String) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    LaunchedEffect(copied) {
        if (copied) {
            kotlinx.coroutines.delay(2000)
            copied = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(code))
                        copied = true
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = "Copy",
                        tint = if (copied) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = code,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
