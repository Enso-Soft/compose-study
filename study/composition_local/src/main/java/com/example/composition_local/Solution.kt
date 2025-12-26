package com.example.composition_local

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 올바른 코드 - CompositionLocal 사용
 *
 * CompositionLocal을 사용하면:
 * 1. 중간 계층이 매개변수를 전달할 필요 없음
 * 2. 필요한 곳에서 .current로 직접 접근
 * 3. 특정 서브트리에만 다른 값 제공 가능 (중첩 Provider)
 * 4. 코드가 깔끔하고 유지보수 용이
 */

/**
 * 앱 설정 데이터 클래스
 */
data class AppSettings(
    val primaryColor: Color = Color(0xFF6200EE),
    val secondaryColor: Color = Color(0xFF03DAC5),
    val spacing: Dp = 8.dp,
    val fontSize: Int = 16
)

/**
 * CompositionLocal 정의
 * - compositionLocalOf: 값이 변경될 때 읽는 부분만 recompose
 * - 기본값을 제공하여 Provider 없이도 동작 가능
 */
val LocalAppSettings = compositionLocalOf { AppSettings() }

@Composable
fun SolutionScreen() {
    val scrollState = rememberScrollState()

    // 동적으로 변경 가능한 설정
    var isDarkMode by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16f) }
    var spacing by remember { mutableStateOf(12f) }

    // 설정 값 계산
    val settings = AppSettings(
        primaryColor = if (isDarkMode) Color(0xFF03DAC5) else Color(0xFF6200EE),
        secondaryColor = if (isDarkMode) Color(0xFF6200EE) else Color(0xFF03DAC5),
        spacing = spacing.dp,
        fontSize = fontSize.toInt()
    )

    // CompositionLocalProvider로 값 제공
    CompositionLocalProvider(LocalAppSettings provides settings) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = "Solution: CompositionLocal",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 핵심 포인트 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "핵심 포인트",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. CompositionLocalProvider로 값 제공")
                    Text("2. 중간 계층은 매개변수 전달 불필요")
                    Text("3. 필요한 곳에서 LocalAppSettings.current로 접근")
                    Text("4. 중첩 Provider로 특정 영역만 오버라이드 가능")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 설정 컨트롤 패널
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "설정 컨트롤 (실시간 변경)",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 다크모드 토글
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("다크모드")
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { isDarkMode = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 폰트 크기 슬라이더
                    Text("폰트 크기: ${fontSize.toInt()}sp")
                    Slider(
                        value = fontSize,
                        onValueChange = { fontSize = it },
                        valueRange = 12f..24f
                    )

                    // 스페이싱 슬라이더
                    Text("스페이싱: ${spacing.toInt()}dp")
                    Slider(
                        value = spacing,
                        onValueChange = { spacing = it },
                        valueRange = 4f..24f
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 실제 구현 시연
            Text(
                text = "실제 코드 시연 (5단계 깊이)",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CompositionLocal 사용 시연 - 매개변수 없음!
            Level1_MainScreen_Solution()

            Spacer(modifier = Modifier.height(24.dp))

            // 중첩 Provider 시연
            Text(
                text = "중첩 Provider 시연",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            NestedProviderDemo()
        }
    }
}

/**
 * Level 1: 메인 화면
 * 매개변수 없음! CompositionLocal을 통해 설정에 접근
 */
@Composable
fun Level1_MainScreen_Solution() {
    val settings = LocalAppSettings.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8E8E8)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 1: MainScreen",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = "매개변수 없음! LocalAppSettings.current 사용",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            // 매개변수 없이 호출!
            Level2_ContentSection_Solution()
        }
    }
}

/**
 * Level 2: 콘텐츠 섹션
 * 매개변수 없음!
 */
@Composable
fun Level2_ContentSection_Solution() {
    val settings = LocalAppSettings.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0D0D0)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 2: ContentSection",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = "매개변수 없음!",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            Level3_ContentCard_Solution()
        }
    }
}

/**
 * Level 3: 콘텐츠 카드
 * 매개변수 없음!
 */
@Composable
fun Level3_ContentCard_Solution() {
    val settings = LocalAppSettings.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB8B8B8)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 3: ContentCard",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = "매개변수 없음!",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            Level4_ContentBody_Solution()
        }
    }
}

/**
 * Level 4: 콘텐츠 본문
 * 매개변수 없음!
 */
@Composable
fun Level4_ContentBody_Solution() {
    val settings = LocalAppSettings.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFA0A0A0)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 4: ContentBody",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = "매개변수 없음!",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            Level5_ThemedText_Solution()
        }
    }
}

/**
 * Level 5: 테마가 적용된 텍스트
 * LocalAppSettings.current로 직접 접근!
 */
@Composable
fun Level5_ThemedText_Solution() {
    val settings = LocalAppSettings.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = settings.primaryColor
        )
    ) {
        Column(
            modifier = Modifier.padding(settings.spacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Level 5: ThemedText",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = "LocalAppSettings.current로 직접 접근!",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            Text(
                text = "이 텍스트가 settings를 사용합니다",
                fontSize = settings.fontSize.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .background(settings.secondaryColor)
                    .padding(8.dp)
            ) {
                Text(
                    text = "보조 색상 배경",
                    color = Color.Black
                )
            }
        }
    }
}

/**
 * 중첩 Provider 데모
 * 특정 영역에서만 다른 값을 제공
 */
@Composable
fun NestedProviderDemo() {
    val settings = LocalAppSettings.current

    Column(modifier = Modifier.fillMaxWidth()) {
        // 기본 설정 사용 영역
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = settings.primaryColor
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "기본 설정 영역",
                    color = Color.White,
                    fontSize = settings.fontSize.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 오버라이드된 설정 사용 영역
        val overriddenSettings = AppSettings(
            primaryColor = Color(0xFFFF5722),
            secondaryColor = Color(0xFFFFEB3B),
            spacing = 16.dp,
            fontSize = 20
        )

        CompositionLocalProvider(LocalAppSettings provides overriddenSettings) {
            OverriddenContent()
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 다시 기본 설정 사용
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = settings.primaryColor
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "다시 기본 설정 영역",
                    color = Color.White,
                    fontSize = settings.fontSize.sp
                )
            }
        }
    }
}

@Composable
fun OverriddenContent() {
    val settings = LocalAppSettings.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = settings.primaryColor
        )
    ) {
        Column(
            modifier = Modifier.padding(settings.spacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "오버라이드된 설정 영역",
                color = Color.White,
                fontSize = settings.fontSize.sp
            )
            Text(
                text = "(주황색 + 폰트 20sp)",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
