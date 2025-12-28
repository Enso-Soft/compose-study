package com.example.screenshot_testing

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
 * # Problem: Screenshot Testing 없이 발생하는 문제
 *
 * ## 문제 상황들
 *
 * 1. **수동 UI 검증의 한계**
 *    - 앱을 직접 빌드하고 화면을 일일이 확인해야 함
 *    - 시간이 많이 소요되고 휴먼 에러 발생
 *    - 모든 화면 상태(로딩, 에러, 빈 상태 등)를 수동으로 확인하기 어려움
 *
 * 2. **UI 회귀 버그 미감지**
 *    - 코드 변경 후 의도치 않은 UI 변경 발생
 *    - padding, color, font 등 미묘한 변화를 놓침
 *    - QA 단계에서야 발견되는 버그
 *
 * 3. **다양한 설정 테스트 부족**
 *    - 다크/라이트 모드 모두 확인하기 어려움
 *    - 다양한 디바이스 크기 테스트 누락
 *    - 폰트 스케일, RTL 등 접근성 설정 확인 어려움
 */
@Composable
fun ProblemScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        ProblemExplanationCard()

        HorizontalDivider()

        // 문제 1: 수동 UI 검증
        Problem1_ManualVerification()

        // 문제 2: UI 회귀 버그
        Problem2_UIRegressionBug()

        // 문제 3: 다양한 설정 테스트
        Problem3_MultiConfigurationTesting()
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
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
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Screenshot Testing 없이 발생하는 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Text(
                text = """
                    UI 테스트 없이 개발하면 다음과 같은 문제가 발생합니다:

                    1. 매번 앱을 빌드하고 직접 눈으로 확인해야 함
                    2. 작은 UI 변경을 놓쳐서 배포 후 문제 발견
                    3. 다크모드, 태블릿 등 모든 설정을 수동 테스트해야 함

                    이 화면에서 각 문제 상황을 직접 확인해보세요.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

/**
 * 문제 1: 수동 UI 검증의 한계
 *
 * 개발자가 UI 변경사항을 확인하려면:
 * 1. 코드 수정
 * 2. 빌드 (2-5분 소요)
 * 3. 에뮬레이터/디바이스에서 확인
 * 4. 문제 발견 시 1번으로 돌아감
 *
 * 이 과정을 수십 번 반복해야 합니다.
 */
@Composable
private fun Problem1_ManualVerification() {
    var buildCount by remember { mutableIntStateOf(0) }
    var totalMinutes by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "문제 1: 수동 UI 검증의 한계",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "UI 변경을 확인하려면 앱을 빌드하고 직접 확인해야 합니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 수동 테스트 시뮬레이션
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        buildCount++
                        totalMinutes += 3 // 평균 빌드 시간 3분
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Build, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("빌드 & 확인")
                }

                OutlinedButton(
                    onClick = {
                        buildCount = 0
                        totalMinutes = 0
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("리셋")
                }
            }

            // 통계
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "수동 테스트 통계",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("빌드 횟수: ${buildCount}회")
                    Text("소요 시간: 약 ${totalMinutes}분")

                    if (buildCount >= 5) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "이미 ${totalMinutes}분이나 소요되었습니다! Screenshot Test는 몇 초만에 수십 개의 화면을 확인합니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * 문제 2: UI 회귀 버그
 *
 * 코드 변경 시 의도치 않게 UI가 변경될 수 있습니다.
 * 아래 예시에서 버튼 색상이 미묘하게 바뀌었는데, 눈으로 확인하기 어렵습니다.
 */
@Composable
private fun Problem2_UIRegressionBug() {
    var showRegression by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "문제 2: UI 회귀 버그",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "아래 버튼을 눌러 '코드 변경'을 시뮬레이션해보세요. 미묘한 UI 변경을 발견할 수 있나요?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 회귀 버그 시뮬레이션
            Button(
                onClick = { showRegression = !showRegression }
            ) {
                Text(if (showRegression) "원래대로 되돌리기" else "코드 변경 시뮬레이션")
            }

            // 회귀 버그가 있는 컴포넌트
            SampleComponentWithPotentialRegression(hasRegression = showRegression)

            // 힌트
            if (showRegression) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "힌트: padding이 16dp에서 12dp로, 버튼 색상이 미묘하게 변경됨, 텍스트 크기도 달라짐",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SampleComponentWithPotentialRegression(hasRegression: Boolean) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(if (hasRegression) 12.dp else 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "사용자 프로필 카드",
                style = MaterialTheme.typography.titleSmall,
                fontSize = if (hasRegression) 13.sp else 14.sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (hasRegression)
                        Color(0xFF5D4B9E) // 미묘하게 다른 보라색
                    else
                        MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Column {
                    Text("홍길동", fontWeight = FontWeight.Medium)
                    Text(
                        "hong@example.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasRegression)
                        Color(0xFF6B5BAD) // 원래 색상과 미묘하게 다름
                    else
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Text("프로필 편집")
            }
        }
    }
}

/**
 * 문제 3: 다양한 설정 테스트 부족
 *
 * 실제 사용자들은 다양한 환경에서 앱을 사용합니다:
 * - 다크모드 / 라이트모드
 * - 폰 / 태블릿
 * - 일반 폰트 / 큰 폰트 (접근성)
 * - LTR / RTL 언어
 *
 * 이 모든 조합을 수동으로 테스트하기는 거의 불가능합니다.
 */
@Composable
private fun Problem3_MultiConfigurationTesting() {
    val isDarkTheme = isSystemInDarkTheme()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "문제 3: 다양한 설정 테스트 부족",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "사용자들은 다양한 환경에서 앱을 사용합니다. 모든 조합을 수동으로 테스트하려면?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 테스트해야 할 조합 목록
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "테스트해야 할 설정 조합",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    ConfigurationItem(
                        icon = Icons.Default.DarkMode,
                        label = "테마",
                        value = "라이트 + 다크 = 2가지"
                    )
                    ConfigurationItem(
                        icon = Icons.Default.PhoneAndroid,
                        label = "디바이스",
                        value = "폰 + 태블릿 + 폴더블 = 3가지"
                    )
                    ConfigurationItem(
                        icon = Icons.Default.FormatSize,
                        label = "폰트 크기",
                        value = "작게/보통/크게 = 3가지"
                    )
                    ConfigurationItem(
                        icon = Icons.Default.Language,
                        label = "언어 방향",
                        value = "LTR + RTL = 2가지"
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "총 조합: 2 x 3 x 3 x 2 = 36가지",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = "각 화면마다 36번씩 수동 테스트?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // 현재 테마 표시
            Surface(
                color = if (isDarkTheme)
                    MaterialTheme.colorScheme.inverseSurface
                else
                    MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "현재 테마",
                        color = if (isDarkTheme)
                            MaterialTheme.colorScheme.inverseOnSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        if (isDarkTheme) "다크 모드" else "라이트 모드",
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme)
                            MaterialTheme.colorScheme.inverseOnSurface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "시스템 설정을 변경하여 다른 테마를 확인해보세요. 매번 이렇게 수동으로 확인하기는 비효율적입니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ConfigurationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
        Text(label, modifier = Modifier.weight(1f))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ============================================
// 스크린샷 테스트 대상이 될 샘플 컴포넌트들
// ============================================

/**
 * 스크린샷 테스트에 사용할 샘플 버튼 컴포넌트
 */
@Composable
fun SampleButton(
    text: String,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text)
    }
}

/**
 * 스크린샷 테스트에 사용할 샘플 카드 컴포넌트
 */
@Composable
fun SampleCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 다크/라이트 모드에서 다르게 보이는 컴포넌트
 */
@Composable
fun ThemeAwareComponent() {
    val isDark = isSystemInDarkTheme()

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (isDark) "다크 모드" else "라이트 모드",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
