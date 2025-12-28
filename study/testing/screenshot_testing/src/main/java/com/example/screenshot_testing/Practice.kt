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
 * # Practice: Screenshot Testing 연습
 *
 * 이 연습에서는 Paparazzi를 사용한 스크린샷 테스트 작성을 학습합니다.
 *
 * ## 연습 1: 기본 스냅샷 테스트 작성
 * - 간단한 Composable에 대한 스냅샷 테스트 작성
 *
 * ## 연습 2: 다크/라이트 모드 테스트
 * - 동일한 컴포넌트를 두 가지 테마로 테스트
 *
 * ## 연습 3: 다양한 디바이스 크기 테스트
 * - DeviceConfig를 활용한 반응형 UI 테스트
 */
@Composable
fun Practice1_BasicSnapshot() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PracticeCard(
            number = 1,
            title = "기본 스냅샷 테스트 작성",
            difficulty = "초급",
            description = """
                Paparazzi를 사용하여 간단한 Composable의 스냅샷 테스트를 작성해보세요.

                목표:
                - Paparazzi Rule 설정
                - snapshot() 함수 호출
                - 테스트 실행 및 골든 이미지 생성
            """.trimIndent()
        )

        // 테스트 대상 컴포넌트 미리보기
        Text(
            text = "테스트 대상 컴포넌트:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        PracticeTargetButton()

        Spacer(Modifier.height(8.dp))

        // 힌트
        HintCard(
            hints = listOf(
                "src/test/java 폴더에 테스트 클래스를 생성하세요",
                "@get:Rule로 Paparazzi 인스턴스를 선언하세요",
                "paparazzi.snapshot { } 블록 안에 테스트할 Composable을 배치하세요"
            )
        )

        // 코드 템플릿
        CodeTemplate(
            title = "테스트 코드 템플릿",
            code = """
// src/test/java/com/example/screenshot_testing/Practice1Test.kt

class Practice1Test {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5
    )

    @Test
    fun practiceButton() {
        // TODO: paparazzi.snapshot을 사용하여
        //       PracticeTargetButton()을 스냅샷으로 캡처하세요

        // 정답:
        // paparazzi.snapshot {
        //     PracticeTargetButton()
        // }
    }
}
            """.trimIndent()
        )

        // 실행 명령어
        CommandCard(
            title = "테스트 실행",
            commands = listOf(
                "./gradlew :study:screenshot_testing:recordPaparazziDebug" to "골든 이미지 생성",
                "./gradlew :study:screenshot_testing:verifyPaparazziDebug" to "스냅샷 검증"
            )
        )
    }
}

@Composable
fun Practice2_DarkLightMode() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PracticeCard(
            number = 2,
            title = "다크/라이트 모드 테스트",
            difficulty = "중급",
            description = """
                동일한 컴포넌트를 다크 모드와 라이트 모드에서 각각 테스트해보세요.

                목표:
                - unsafeUpdateConfig()로 테마 변경
                - 각 테마별 스냅샷 캡처
                - 이름이 다른 골든 이미지 생성
            """.trimIndent()
        )

        // 테스트 대상 컴포넌트
        Text(
            text = "테스트 대상 컴포넌트:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        PracticeTargetCard()

        Spacer(Modifier.height(8.dp))

        // 힌트
        HintCard(
            hints = listOf(
                "paparazzi.unsafeUpdateConfig(theme = ...)로 테마를 변경합니다",
                "라이트: \"android:Theme.Material3.Light\"",
                "다크: \"android:Theme.Material3.Dark\"",
                "snapshot(name = \"...\")로 이름을 지정하여 구분하세요"
            )
        )

        // 코드 템플릿
        CodeTemplate(
            title = "테스트 코드 템플릿",
            code = """
// src/test/java/com/example/screenshot_testing/Practice2Test.kt

class Practice2Test {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun cardLightMode() {
        // TODO: 라이트 모드로 테마 설정 후 스냅샷 캡처

        // 정답:
        // paparazzi.unsafeUpdateConfig(
        //     theme = "android:Theme.Material3.Light"
        // )
        // paparazzi.snapshot(name = "card_light") {
        //     PracticeTargetCard()
        // }
    }

    @Test
    fun cardDarkMode() {
        // TODO: 다크 모드로 테마 설정 후 스냅샷 캡처

        // 정답:
        // paparazzi.unsafeUpdateConfig(
        //     theme = "android:Theme.Material3.Dark"
        // )
        // paparazzi.snapshot(name = "card_dark") {
        //     PracticeTargetCard()
        // }
    }
}
            """.trimIndent()
        )

        // 예상 결과
        ExpectedResultCard(
            description = "두 개의 골든 이미지가 생성됩니다:",
            items = listOf(
                "snapshots/images/Practice2Test_cardLightMode_card_light.png",
                "snapshots/images/Practice2Test_cardDarkMode_card_dark.png"
            )
        )
    }
}

@Composable
fun Practice3_DeviceSizes() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PracticeCard(
            number = 3,
            title = "다양한 디바이스 크기 테스트",
            difficulty = "중급",
            description = """
                DeviceConfig를 활용하여 폰, 태블릿 등 다양한 화면 크기에서 UI를 테스트해보세요.

                목표:
                - DeviceConfig.PIXEL_5 (폰)
                - DeviceConfig.PIXEL_TABLET (태블릿)
                - 커스텀 디바이스 설정
            """.trimIndent()
        )

        // 테스트 대상 컴포넌트
        Text(
            text = "테스트 대상 컴포넌트:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        PracticeTargetResponsiveLayout()

        Spacer(Modifier.height(8.dp))

        // 힌트
        HintCard(
            hints = listOf(
                "DeviceConfig.PIXEL_5, PIXEL_TABLET 등 사전 정의된 디바이스 사용",
                "DeviceConfig(...) 생성자로 커스텀 디바이스 정의 가능",
                "screenWidth, screenHeight, density 등 설정",
                "ScreenOrientation.PORTRAIT / LANDSCAPE로 방향 설정"
            )
        )

        // 코드 템플릿
        CodeTemplate(
            title = "테스트 코드 템플릿",
            code = """
// src/test/java/com/example/screenshot_testing/Practice3Test.kt

class Practice3Test {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun layoutOnPhone() {
        // TODO: PIXEL_5 디바이스로 설정 후 스냅샷 캡처

        // 정답:
        // paparazzi.unsafeUpdateConfig(
        //     deviceConfig = DeviceConfig.PIXEL_5
        // )
        // paparazzi.snapshot {
        //     PracticeTargetResponsiveLayout()
        // }
    }

    @Test
    fun layoutOnTablet() {
        // TODO: PIXEL_TABLET 디바이스로 설정 후 스냅샷 캡처

        // 정답:
        // paparazzi.unsafeUpdateConfig(
        //     deviceConfig = DeviceConfig.PIXEL_TABLET
        // )
        // paparazzi.snapshot {
        //     PracticeTargetResponsiveLayout()
        // }
    }

    @Test
    fun layoutLandscape() {
        // TODO: 가로 방향으로 설정 후 스냅샷 캡처

        // 정답:
        // paparazzi.unsafeUpdateConfig(
        //     deviceConfig = DeviceConfig.PIXEL_5.copy(
        //         orientation = ScreenOrientation.LANDSCAPE
        //     )
        // )
        // paparazzi.snapshot {
        //     PracticeTargetResponsiveLayout()
        // }
    }
}
            """.trimIndent()
        )

        // 추가 팁
        TipCard(
            title = "파라미터화 테스트로 간소화",
            tip = """
JUnit의 @ParameterizedTest를 사용하면 여러 디바이스를
하나의 테스트 메서드로 처리할 수 있습니다:

@ParameterizedTest
@MethodSource("devices")
fun testOnVariousDevices(config: DeviceConfig) {
    paparazzi.unsafeUpdateConfig(deviceConfig = config)
    paparazzi.snapshot {
        MyComponent()
    }
}

companion object {
    @JvmStatic
    fun devices() = listOf(
        DeviceConfig.PIXEL_5,
        DeviceConfig.PIXEL_TABLET,
        DeviceConfig.WEAR_OS_SMALL_ROUND
    )
}
            """.trimIndent()
        )
    }
}

/**
 * Practice 4: 라이브러리 선택 연습
 *
 * 상황에 맞는 스크린샷 테스트 라이브러리를 선택하는 연습입니다.
 */
@Composable
fun Practice4_LibrarySelection() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PracticeCard(
            number = 4,
            title = "라이브러리 선택 연습",
            difficulty = "고급",
            description = """
                프로젝트 상황에 맞는 스크린샷 테스트 라이브러리를 선택해보세요.

                목표:
                - 각 라이브러리의 장단점 이해
                - 상황별 최적의 선택 판단
                - 선택 이유 설명 능력
            """.trimIndent()
        )

        // 시나리오들
        LibrarySelectionScenario(
            scenarioLetter = "A",
            title = "새 프로젝트, Compose Only",
            description = "새로운 프로젝트를 시작합니다. Compose만 사용하고, 빠르게 스크린샷 테스트를 도입하고 싶습니다. Activity/Fragment 테스트는 필요 없습니다.",
            answer = "Paparazzi",
            reason = "빠른 설정, Compose 컴포넌트 테스트에 충분, 가장 빠른 실행 속도"
        )

        LibrarySelectionScenario(
            scenarioLetter = "B",
            title = "Hilt 사용 프로젝트",
            description = "기존 프로젝트에 스크린샷 테스트를 추가합니다. Dagger Hilt를 사용 중이고, Activity 레벨에서 DI가 주입된 화면을 테스트해야 합니다.",
            answer = "Roborazzi",
            reason = "Hilt와 통합 가능, Activity/Fragment 테스트 지원, Robolectric 기반"
        )

        LibrarySelectionScenario(
            scenarioLetter = "C",
            title = "@Preview 활용",
            description = "@Preview가 100개 이상 작성되어 있습니다. 이 Preview들을 그대로 스크린샷 테스트로 활용하고 싶습니다.",
            answer = "Google Preview 또는 Roborazzi + ComposablePreviewScanner",
            reason = "기존 Preview 재사용, 중복 코드 최소화"
        )

        LibrarySelectionScenario(
            scenarioLetter = "D",
            title = "상호작용 테스트 필요",
            description = "버튼 클릭 후 UI 상태 변화를 스크린샷으로 캡처해야 합니다. 로딩 -> 성공 -> 에러 등 다양한 상태 전이를 테스트하고 싶습니다.",
            answer = "Roborazzi",
            reason = "Espresso 상호작용 지원, 상태 변화 후 스크린샷 가능"
        )

        LibrarySelectionScenario(
            scenarioLetter = "E",
            title = "Robolectric 이미 사용 중",
            description = "프로젝트에서 이미 Robolectric을 사용한 테스트가 많습니다. 스크린샷 테스트를 추가하려 합니다.",
            answer = "Roborazzi",
            reason = "Robolectric과 통합, Paparazzi는 Robolectric과 호환 불가"
        )

        // 의사결정 플로우차트 카드
        DecisionFlowchartCard()
    }
}

@Composable
private fun LibrarySelectionScenario(
    scenarioLetter: String,
    title: String,
    description: String,
    answer: String,
    reason: String
) {
    var showAnswer by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = scenarioLetter,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { showAnswer = !showAnswer },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showAnswer) "정답 숨기기" else "정답 보기")
            }

            if (showAnswer) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = answer,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = reason,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DecisionFlowchartCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccountTree,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "의사결정 플로우차트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Spacer(Modifier.height(12.dp))

            Surface(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(8.dp)
            ) {
                val scrollState = rememberScrollState()
                Text(
                    text = """
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
                    """.trimIndent(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .padding(12.dp),
                    color = Color(0xFFD4D4D4),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

// ============================================
// 연습용 대상 컴포넌트들
// ============================================

/**
 * Practice 1: 기본 스냅샷 테스트 대상
 */
@Composable
fun PracticeTargetButton() {
    Button(
        onClick = { },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(Icons.Default.Check, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text("확인")
    }
}

/**
 * Practice 2: 다크/라이트 모드 테스트 대상
 */
@Composable
fun PracticeTargetCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column {
                    Text(
                        text = "사용자 이름",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "user@example.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { }) {
                    Text("편집")
                }
                Button(onClick = { }) {
                    Text("프로필 보기")
                }
            }
        }
    }
}

/**
 * Practice 3: 반응형 레이아웃 테스트 대상
 */
@Composable
fun PracticeTargetResponsiveLayout() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val isTablet = maxWidth > 600.dp

        if (isTablet) {
            // 태블릿: 가로 배치
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ResponsiveCard(
                    title = "카드 1",
                    icon = Icons.Default.Home,
                    modifier = Modifier.weight(1f)
                )
                ResponsiveCard(
                    title = "카드 2",
                    icon = Icons.Default.Settings,
                    modifier = Modifier.weight(1f)
                )
                ResponsiveCard(
                    title = "카드 3",
                    icon = Icons.Default.Person,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // 폰: 세로 배치
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResponsiveCard(
                    title = "카드 1",
                    icon = Icons.Default.Home,
                    modifier = Modifier.fillMaxWidth()
                )
                ResponsiveCard(
                    title = "카드 2",
                    icon = Icons.Default.Settings,
                    modifier = Modifier.fillMaxWidth()
                )
                ResponsiveCard(
                    title = "카드 3",
                    icon = Icons.Default.Person,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ResponsiveCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

// ============================================
// UI 헬퍼 컴포넌트들
// ============================================

@Composable
private fun PracticeCard(
    number: Int,
    title: String,
    difficulty: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = number.toString(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                AssistChip(
                    onClick = { },
                    label = { Text(difficulty) }
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Spacer(Modifier.height(8.dp))

            hints.forEach { hint ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun CodeTemplate(
    title: String,
    code: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Surface(
            color = Color(0xFF1E1E1E),
            shape = RoundedCornerShape(8.dp)
        ) {
            val scrollState = rememberScrollState()
            Text(
                text = code,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(16.dp),
                color = Color(0xFFD4D4D4),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun CommandCard(
    title: String,
    commands: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Terminal, contentDescription = null)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(12.dp))

            commands.forEach { (command, description) ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Surface(
                        color = Color(0xFF1E1E1E),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        val scrollState = rememberScrollState()
                        Text(
                            text = command,
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState)
                                .padding(8.dp),
                            color = Color(0xFF4EC9B0),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpectedResultCard(
    description: String,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "예상 결과",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(Modifier.height(8.dp))

            items.forEach { item ->
                val scrollState = rememberScrollState()
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                            .padding(8.dp),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TipCard(
    title: String,
    tip: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.TipsAndUpdates,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Surface(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(8.dp)
            ) {
                val scrollState = rememberScrollState()
                Text(
                    text = tip,
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
