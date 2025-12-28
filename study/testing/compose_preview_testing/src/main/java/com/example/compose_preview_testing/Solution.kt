package com.example.compose_preview_testing

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_preview_testing.ui.theme.ComposePreviewTestingTheme

/**
 * # Solution: @PreviewParameter로 효율적인 Preview 관리
 *
 * ## 핵심 개념
 *
 * @PreviewParameter와 PreviewParameterProvider를 사용하면
 * 하나의 Preview 함수로 여러 상태를 자동으로 테스트할 수 있습니다.
 *
 * ## 주요 기능
 *
 * 1. **PreviewParameterProvider**: 테스트 데이터를 Sequence로 제공
 * 2. **CollectionPreviewParameterProvider**: 더 간단한 List 기반 Provider
 * 3. **Multipreview 어노테이션**: 다크모드, 폰트 스케일 등 설정 조합
 * 4. **limit 파라미터**: Preview 개수 제한
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

        // 기본 사용법: PreviewParameterProvider
        BasicPreviewParameterGuide()

        // CollectionPreviewParameterProvider
        CollectionProviderGuide()

        // Multipreview 어노테이션
        MultipreviewGuide()

        // 고급 활용: limit와 getDisplayName
        AdvancedUsageGuide()

        // 베스트 프랙티스
        BestPracticesCard()
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
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "@PreviewParameter로 효율적인 Preview 관리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = """
                    @PreviewParameter를 사용하면:

                    1. 하나의 Preview 함수로 여러 상태 자동 생성
                    2. 데이터와 UI 로직 분리
                    3. Multipreview와 조합하여 설정별 테스트 자동화
                    4. 유지보수가 훨씬 쉬워짐

                    아래에서 사용 방법을 확인하세요!
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun BasicPreviewParameterGuide() {
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
                    Icon(Icons.Default.Code, contentDescription = null)
                    Text(
                        text = "1. PreviewParameterProvider 기본 사용법",
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
                    text = "PreviewParameterProvider 인터페이스를 구현하여 테스트 데이터를 제공합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))

                // Step 1: Provider 구현
                StepCard(
                    stepNumber = 1,
                    title = "PreviewParameterProvider 구현",
                    code = """
// 테스트 데이터를 제공하는 Provider 클래스
class UserProfileProvider : PreviewParameterProvider<UserProfile> {

    // values 프로퍼티를 오버라이드하여 Sequence 반환
    override val values: Sequence<UserProfile> = sequenceOf(
        UserProfile("홍길동", "hong@example.com", false),
        UserProfile("김철수", "kim@example.com", true),
        UserProfile("아주긴이름을가진사용자입니다", "long@example.com", false),
        UserProfile("테스트", null, false)
    )
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // Step 2: Preview에서 사용
                StepCard(
                    stepNumber = 2,
                    title = "@PreviewParameter로 Preview 작성",
                    code = """
// 하나의 Preview 함수로 모든 상태 테스트!
@Preview(showBackground = true)
@Composable
fun PreviewUserProfileCard(
    @PreviewParameter(UserProfileProvider::class)
    user: UserProfile
) {
    ComposePreviewTestingTheme {
        UserProfileCard(user = user)
    }
}

// 결과: Android Studio에서 4개의 Preview가 자동 생성됨!
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // 결과 설명
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Provider의 각 값마다 Preview가 자동으로 생성됩니다. 4개의 상태 = 4개의 Preview!",
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
private fun CollectionProviderGuide() {
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
                    Icon(Icons.Default.List, contentDescription = null)
                    Text(
                        text = "2. CollectionPreviewParameterProvider",
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
                    text = "더 간단한 방법! List를 직접 전달하면 자동으로 Sequence로 변환됩니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))

                StepCard(
                    stepNumber = 1,
                    title = "CollectionPreviewParameterProvider 사용",
                    code = """
// 더 간단한 문법!
class UserProfileCollectionProvider :
    CollectionPreviewParameterProvider<UserProfile>(
        listOf(
            UserProfile("홍길동", "hong@example.com", false),
            UserProfile("김철수", "kim@example.com", true),
            UserProfile("아주긴이름", "long@example.com", false),
            UserProfile("테스트", null, false)
        )
    )

// 사용법은 동일
@Preview
@Composable
fun PreviewUserCard(
    @PreviewParameter(UserProfileCollectionProvider::class)
    user: UserProfile
) {
    UserProfileCard(user = user)
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // 비교 표
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "PreviewParameterProvider vs CollectionPreviewParameterProvider",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("PreviewParameterProvider", style = MaterialTheme.typography.bodySmall)
                            Text("sequenceOf() 직접 구현", style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("CollectionPreviewParameterProvider", style = MaterialTheme.typography.bodySmall)
                            Text("List 직접 전달 (더 간단)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MultipreviewGuide() {
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
                        text = "3. Multipreview 어노테이션 조합",
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
                    text = "@PreviewParameter와 Multipreview 어노테이션을 조합하면 상태 x 설정 조합을 자동으로 생성합니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))

                // 커스텀 Multipreview 정의
                StepCard(
                    stepNumber = 1,
                    title = "커스텀 Multipreview 어노테이션 정의",
                    code = """
import android.content.res.Configuration

// 라이트/다크 + 여러 폰트 스케일을 한 번에!
@Preview(name = "Light")
@Preview(
    name = "Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(name = "Large Font", fontScale = 1.3f)
@Preview(
    name = "Dark + Large Font",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    fontScale = 1.3f
)
annotation class ThemeAndFontPreviews
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // PreviewParameter와 조합
                StepCard(
                    stepNumber = 2,
                    title = "PreviewParameter와 조합",
                    code = """
// Multipreview + PreviewParameter 조합
@ThemeAndFontPreviews
@Composable
fun PreviewUserCard(
    @PreviewParameter(UserProfileProvider::class)
    user: UserProfile
) {
    ComposePreviewTestingTheme {
        UserProfileCard(user = user)
    }
}

// 결과: 4가지 상태 x 4가지 설정 = 16개 Preview 자동 생성!
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // 계산 결과
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Preview 자동 생성 계산",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "4가지 상태 (Provider) x 4가지 설정 (Multipreview) = 16개 Preview",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "코드는 단 1개의 함수! 중복 없이 모든 조합 테스트!",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdvancedUsageGuide() {
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
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Text(
                        text = "4. 고급 활용: limit와 getDisplayName",
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

                // limit 파라미터
                StepCard(
                    stepNumber = 1,
                    title = "limit 파라미터로 Preview 개수 제한",
                    code = """
// 너무 많은 Preview가 생성되면 IDE 성능 저하
// limit로 개수 제한 가능!

@Preview
@Composable
fun PreviewUserCard(
    @PreviewParameter(
        UserProfileProvider::class,
        limit = 2  // 처음 2개만 Preview 생성
    )
    user: UserProfile
) {
    UserProfileCard(user = user)
}
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // getDisplayName
                StepCard(
                    stepNumber = 2,
                    title = "getDisplayName으로 커스텀 Preview 이름",
                    code = """
class UserProfileProvider : PreviewParameterProvider<UserProfile> {

    private val users = listOf(
        UserProfile("홍길동", "hong@example.com", false),
        UserProfile("김철수", "kim@example.com", true)
    )

    override val values = users.asSequence()

    // Preview 이름 커스터마이징 (2025 신기능!)
    override fun getDisplayName(index: Int): String? {
        return when (index) {
            0 -> "일반 사용자"
            1 -> "인증된 사용자"
            else -> null
        }
    }
}

// 결과: Preview 이름이 "user 0" 대신 "일반 사용자"로 표시!
                    """.trimIndent()
                )

                Spacer(Modifier.height(12.dp))

                // LoremIpsum
                StepCard(
                    stepNumber = 3,
                    title = "내장 Provider: LoremIpsum",
                    code = """
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

// 텍스트 길이 테스트에 유용한 내장 Provider
@Preview
@Composable
fun PreviewTextCard(
    @PreviewParameter(LoremIpsum::class)
    text: String
) {
    Card {
        Text(
            text = text,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Lorem Ipsum 텍스트가 자동으로 제공됨
                    """.trimIndent()
                )
            }
        }
    }
}

@Composable
private fun BestPracticesCard() {
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
                title = "Provider 재사용",
                description = "여러 Preview에서 동일한 Provider를 재사용하여 일관성 유지"
            )

            BestPracticeItem(
                number = 2,
                title = "limit 활용",
                description = "개발 중에는 limit=2로 빠른 Preview, CI에서는 전체 테스트"
            )

            BestPracticeItem(
                number = 3,
                title = "의미 있는 데이터",
                description = "엣지 케이스(긴 텍스트, null 값 등)를 포함한 테스트 데이터 구성"
            )

            BestPracticeItem(
                number = 4,
                title = "Multipreview 조합",
                description = "다크모드, 폰트 스케일 등 중요한 설정은 Multipreview로 자동 테스트"
            )

            BestPracticeItem(
                number = 5,
                title = "getDisplayName 활용",
                description = "Preview 이름을 명확하게 하여 디버깅 용이하게"
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
// 실제 Preview 예제 (Android Studio에서 확인 가능)
// ============================================

/**
 * 사용자 프로필 카드 컴포넌트
 */
@Composable
fun UserProfileCard(user: UserProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아바타
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // 정보
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (user.isVerified) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "인증됨",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                user.email?.let { email ->
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } ?: Text(
                    text = "이메일 없음",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// ============================================
// PreviewParameterProvider 구현 예제
// ============================================

/**
 * 사용자 프로필 테스트 데이터 Provider
 */
class UserProfilePreviewProvider : PreviewParameterProvider<UserProfile> {
    override val values: Sequence<UserProfile> = sequenceOf(
        UserProfile("홍길동", "hong@example.com", isVerified = false),
        UserProfile("김철수", "kim@example.com", isVerified = true),
        UserProfile("아주긴이름을가진사용자입니다", "longname@example.com", isVerified = false),
        UserProfile("테스트", null, isVerified = false)
    )
}

/**
 * CollectionPreviewParameterProvider 사용 예제
 */
class UserProfileCollectionProvider : CollectionPreviewParameterProvider<UserProfile>(
    listOf(
        UserProfile("홍길동", "hong@example.com", isVerified = false),
        UserProfile("김철수", "kim@example.com", isVerified = true)
    )
)

// ============================================
// 실제 동작하는 Preview 예제
// ============================================

/**
 * PreviewParameter를 사용한 Preview
 */
@Preview(showBackground = true)
@Composable
private fun PreviewUserProfileCard(
    @PreviewParameter(UserProfilePreviewProvider::class) user: UserProfile
) {
    ComposePreviewTestingTheme {
        UserProfileCard(user = user)
    }
}

/**
 * Multipreview + PreviewParameter 조합 예제
 */
@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class LightDarkPreview

@LightDarkPreview
@Composable
private fun PreviewUserProfileCardMulti(
    @PreviewParameter(UserProfileCollectionProvider::class) user: UserProfile
) {
    ComposePreviewTestingTheme {
        UserProfileCard(user = user)
    }
}
