package com.example.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.preview.ui.theme.PreviewTheme

/**
 * 해결책: @Preview를 활용한 효율적인 UI 개발
 *
 * 이 화면에서는 @Preview의 세 가지 핵심 기능을 보여줍니다:
 * 1. 기본 @Preview와 파라미터 활용
 * 2. @PreviewParameter로 동적 데이터 주입
 * 3. Multipreview 커스텀 어노테이션
 */
@Composable
fun SolutionScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("기본 Preview", "PreviewParameter", "Multipreview")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> BasicPreviewDemo()
            1 -> PreviewParameterDemo()
            2 -> MultipreviewDemo()
        }
    }
}

// ===== 1. 기본 Preview 데모 =====

@Composable
fun BasicPreviewDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "기본 @Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "@Preview 어노테이션을 사용하면 앱을 빌드하지 않고 " +
                            "Android Studio에서 바로 UI를 확인할 수 있습니다."
                )
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 예시",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Preview(
    name = "사용자 카드",
    showBackground = true,
    widthDp = 320,
    heightDp = 200
)
@Composable
fun UserCardPreview() {
    PreviewTheme {
        UserCard(User("Alice", 25))
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 파라미터 설명
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "주요 파라미터",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val params = listOf(
                    "name" to "Preview 이름 (Android Studio에 표시)",
                    "showBackground" to "배경 표시 여부",
                    "widthDp" to "미리보기 너비 (dp)",
                    "heightDp" to "미리보기 높이 (dp)",
                    "uiMode" to "다크/라이트 모드 설정",
                    "device" to "디바이스 프리셋 (Pixel 4 등)",
                    "locale" to "언어 설정 (ko, en 등)",
                    "fontScale" to "폰트 크기 배율"
                )

                params.forEach { (name, desc) ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(100.dp)
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // 실제 컴포넌트
        Text(
            text = "실제 Preview 대상 컴포넌트",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        PreviewableUserCard(
            user = PreviewUser("홍길동", 28, "hong@email.com")
        )
    }
}

// ===== 2. PreviewParameter 데모 =====

@Composable
fun PreviewParameterDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "@PreviewParameter",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "PreviewParameterProvider를 사용하면 하나의 Preview 함수로 " +
                            "여러 데이터 상태를 자동으로 테스트할 수 있습니다."
                )
            }
        }

        // Provider 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Provider 정의",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
class UserProvider : PreviewParameterProvider<User> {
    override val values = sequenceOf(
        User("Alice", 25, "alice@email.com"),
        User("Bob", 30, "bob@email.com"),
        User("Charlie", 35, "charlie@email.com")
    )
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // Preview 함수 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. Preview 함수",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Preview(showBackground = true)
@Composable
fun UserCardPreview(
    @PreviewParameter(UserProvider::class) user: User
) {
    PreviewTheme {
        UserCard(user)
    }
}
// → 3개의 Preview가 자동 생성됩니다!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 결과 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "결과: 자동 생성된 3개의 Preview",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 시뮬레이션된 여러 데이터
                val users = listOf(
                    PreviewUser("Alice", 25, "alice@email.com"),
                    PreviewUser("Bob", 30, "bob@email.com"),
                    PreviewUser("Charlie", 35, "charlie@email.com")
                )

                users.forEachIndexed { index, user ->
                    Text(
                        text = "Preview ${index + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    PreviewableUserCard(user = user)
                    if (index < users.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // limit 파라미터 설명
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tip: limit 파라미터",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@PreviewParameter(UserProvider::class, limit = 2)

→ 처음 2개의 데이터만 Preview를 생성합니다.
   많은 데이터가 있을 때 빌드 시간을 줄일 수 있습니다.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ===== 3. Multipreview 데모 =====

@Composable
fun MultipreviewDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Multipreview 커스텀 어노테이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "여러 @Preview를 하나의 커스텀 어노테이션으로 묶어서 " +
                            "재사용할 수 있습니다."
                )
            }
        }

        // LightDarkPreview 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Light/Dark 모드 테스트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class LightDarkPreview

@LightDarkPreview
@Composable
fun MyCardPreview() {
    PreviewTheme {
        MyCard()
    }
}
// → Light + Dark 2개 Preview 자동 생성!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // DevicePreview 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. 다양한 디바이스 테스트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Preview(name = "Phone", widthDp = 360, heightDp = 640)
@Preview(name = "Tablet", widthDp = 800, heightDp = 1280)
@Preview(name = "Foldable", widthDp = 673, heightDp = 841)
annotation class DeviceSizePreview
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 조합 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. 조합 사용",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@LightDarkPreview
@DeviceSizePreview
@Composable
fun MyScreenPreview() {
    PreviewTheme {
        MyScreen()
    }
}
// → 2 × 3 = 6개 Preview 자동 생성!
// (Light Phone, Dark Phone, Light Tablet,
//  Dark Tablet, Light Foldable, Dark Foldable)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 접근성 테스트 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "4. 접근성 테스트 (폰트 크기)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Preview(fontScale = 1.0f, name = "기본")
@Preview(fontScale = 1.5f, name = "큰 글꼴")
@Preview(fontScale = 2.0f, name = "매우 큰 글꼴")
annotation class FontScalePreview
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 내장 템플릿 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "5. 내장 Multipreview 템플릿 (1.6.0+)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@PreviewScreenSizes  // 다양한 화면 크기
@PreviewFontScales   // 폰트 크기 배율
@PreviewLightDark    // Light/Dark 모드
@PreviewDynamicColors // Dynamic Colors
@Composable
fun MyPreview() { ... }

// 내장 템플릿만으로도 충분한 경우가 많습니다!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        // 핵심 포인트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                val points = listOf(
                    "✓ 내장 템플릿으로 빠르게 시작 (1.6.0+)",
                    "✓ 커스텀 어노테이션으로 Preview 조합 재사용",
                    "✓ 여러 어노테이션 중첩으로 조합 확장",
                    "✓ 팀 전체에서 일관된 테스트 환경 유지",
                    "✓ 코드 중복 없이 다양한 환경 테스트"
                )

                points.forEach { point ->
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ===== Preview용 데이터 및 컴포넌트 =====

/**
 * Preview용 사용자 데이터
 */
data class PreviewUser(
    val name: String,
    val age: Int,
    val email: String
)

/**
 * Preview 가능한 사용자 카드
 */
@Composable
fun PreviewableUserCard(user: PreviewUser) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${user.age}세",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

// ===== 실제 Preview 어노테이션 정의 (실습용) =====

/**
 * Light/Dark 모드 동시 테스트용 Multipreview
 */
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class LightDarkPreview

/**
 * 다양한 화면 크기 테스트용 Multipreview
 */
@Preview(name = "Phone", widthDp = 360, heightDp = 640, showBackground = true)
@Preview(name = "Tablet", widthDp = 800, heightDp = 1280, showBackground = true)
annotation class DeviceSizePreview

/**
 * 폰트 크기 접근성 테스트용 Multipreview
 */
@Preview(fontScale = 1.0f, name = "기본 폰트", showBackground = true)
@Preview(fontScale = 1.5f, name = "큰 폰트", showBackground = true)
@Preview(fontScale = 2.0f, name = "매우 큰 폰트", showBackground = true)
annotation class FontScalePreview

// ===== PreviewParameterProvider 정의 =====

/**
 * 사용자 데이터 Provider
 */
class PreviewUserProvider : PreviewParameterProvider<PreviewUser> {
    override val values: Sequence<PreviewUser> = sequenceOf(
        PreviewUser("Alice", 25, "alice@email.com"),
        PreviewUser("Bob", 30, "bob@email.com"),
        PreviewUser("Charlie", 35, "charlie@email.com")
    )
}

// ===== 실제 Preview 함수들 =====

/**
 * 기본 Preview
 */
@Preview(
    name = "사용자 카드",
    showBackground = true,
    widthDp = 320
)
@Composable
private fun UserCardBasicPreview() {
    PreviewTheme {
        PreviewableUserCard(
            user = PreviewUser("홍길동", 28, "hong@email.com")
        )
    }
}

/**
 * PreviewParameter를 사용한 Preview
 */
@Preview(showBackground = true, widthDp = 320)
@Composable
private fun UserCardWithParameterPreview(
    @PreviewParameter(PreviewUserProvider::class) user: PreviewUser
) {
    PreviewTheme {
        PreviewableUserCard(user = user)
    }
}

/**
 * Multipreview를 사용한 Preview
 */
@LightDarkPreview
@Composable
private fun UserCardLightDarkPreview() {
    PreviewTheme {
        PreviewableUserCard(
            user = PreviewUser("테스트", 30, "test@email.com")
        )
    }
}

/**
 * 폰트 크기 접근성 Preview
 */
@FontScalePreview
@Composable
private fun UserCardFontScalePreview() {
    PreviewTheme {
        PreviewableUserCard(
            user = PreviewUser("접근성 테스트", 25, "a11y@email.com")
        )
    }
}
