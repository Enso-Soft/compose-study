package com.example.compose_preview_testing

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * # Problem: @PreviewParameter 없이 발생하는 문제
 *
 * ## 문제 상황들
 *
 * 1. **코드 중복**
 *    - 각 상태마다 별도의 @Preview 함수를 작성해야 함
 *    - DRY(Don't Repeat Yourself) 원칙 위반
 *    - 유지보수가 어려워짐
 *
 * 2. **Preview 함수 폭증**
 *    - 상태 4개 x 테마 2개 x 폰트 3개 = 24개 Preview 필요
 *    - 새 상태 추가 시 모든 조합에 대해 추가 작업 필요
 *    - 파일이 매우 길어짐
 *
 * 3. **일관성 유지 어려움**
 *    - 모든 Preview에 동일한 변경 적용 시 실수 가능
 *    - 일부 Preview만 수정하고 나머지 누락 가능
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

        // 문제 1: 코드 중복
        Problem1_CodeDuplication()

        // 문제 2: Preview 폭증
        Problem2_PreviewExplosion()

        // 문제 3: 잘못된 코드 예시
        Problem3_BadCodeExample()
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
                    text = "@PreviewParameter 없이 발생하는 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Text(
                text = """
                    Compose에서 여러 상태의 UI를 Preview로 확인하려면,
                    각 상태마다 별도의 @Preview 함수를 작성해야 합니다.

                    이 방식의 문제점:
                    - 코드 중복이 심해집니다
                    - Preview 함수 개수가 폭발적으로 증가합니다
                    - 유지보수가 매우 어려워집니다

                    아래에서 각 문제를 직접 확인해보세요.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

/**
 * 문제 1: 코드 중복
 *
 * 사용자 프로필 카드를 4가지 상태로 Preview하려면,
 * 4개의 거의 동일한 Preview 함수를 작성해야 합니다.
 */
@Composable
private fun Problem1_CodeDuplication() {
    var stateCount by remember { mutableIntStateOf(4) }

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
                text = "문제 1: 코드 중복",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "사용자 프로필 카드의 다양한 상태를 Preview로 확인하려면?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 상태 추가 시뮬레이션
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("테스트할 상태 개수:")

                FilledTonalIconButton(
                    onClick = { if (stateCount > 1) stateCount-- }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "감소")
                }

                Text(
                    text = "$stateCount",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                FilledTonalIconButton(
                    onClick = { stateCount++ }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "증가")
                }
            }

            // 결과 표시
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "필요한 Preview 함수 개수",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("상태 ${stateCount}개")
                        Text(
                            "${stateCount}개 Preview 함수 필요",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    if (stateCount >= 4) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "각 Preview 함수에 거의 동일한 코드가 반복됩니다!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // 상태 목록 예시
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "테스트해야 할 상태 예시",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    val states = listOf(
                        "일반 사용자",
                        "인증된 사용자",
                        "긴 이름 사용자",
                        "이메일 없는 사용자",
                        "프로필 이미지 있는 사용자",
                        "VIP 사용자",
                        "비활성화된 사용자"
                    )

                    states.take(stateCount).forEachIndexed { index, state ->
                        Text(
                            "${index + 1}. $state",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

/**
 * 문제 2: Preview 함수 폭증
 *
 * 다크모드, 폰트 스케일 등 설정까지 조합하면
 * Preview 함수 개수가 기하급수적으로 증가합니다.
 */
@Composable
private fun Problem2_PreviewExplosion() {
    var stateCount by remember { mutableIntStateOf(4) }
    var themeCount by remember { mutableIntStateOf(2) }
    var fontScaleCount by remember { mutableIntStateOf(3) }

    val totalPreviews = stateCount * themeCount * fontScaleCount

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
                text = "문제 2: Preview 함수 폭증",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "다크모드, 폰트 크기 등 설정까지 테스트하려면?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 조합 계산
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Preview 조합 계산",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(12.dp))

                    // 상태
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text("UI 상태")
                        }
                        Text("${stateCount}가지", fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(8.dp))

                    // 테마
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.DarkMode,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text("테마 (라이트/다크)")
                        }
                        Text("${themeCount}가지", fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(8.dp))

                    // 폰트 스케일
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.FormatSize,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text("폰트 스케일")
                        }
                        Text("${fontScaleCount}가지", fontWeight = FontWeight.Medium)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // 총 개수
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "$stateCount x $themeCount x $fontScaleCount =",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            "${totalPreviews}개 Preview 함수!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // 경고 메시지
            if (totalPreviews >= 10) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "${totalPreviews}개의 거의 동일한 Preview 함수를 작성하고 유지보수해야 합니다!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

/**
 * 문제 3: 잘못된 코드 예시
 *
 * @PreviewParameter 없이 여러 상태를 Preview하는 코드입니다.
 * 코드 중복이 얼마나 심각한지 보여줍니다.
 */
@Composable
private fun Problem3_BadCodeExample() {
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
                text = "문제 3: 잘못된 코드 예시",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "아래 코드를 보세요. 얼마나 많은 중복이 있는지 확인해보세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 잘못된 코드 예시
            Surface(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "이렇게 하지 마세요!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = """
// 각 상태마다 별도 Preview 함수 작성 - 코드 중복!

@Preview(name = "일반 사용자")
@Composable
fun PreviewNormalUser() {
    MyTheme {
        UserProfileCard(
            user = User("홍길동", "hong@example.com", false)
        )
    }
}

@Preview(name = "인증된 사용자")
@Composable
fun PreviewVerifiedUser() {
    MyTheme {
        UserProfileCard(
            user = User("김철수", "kim@example.com", true)
        )
    }
}

@Preview(name = "긴 이름 사용자")
@Composable
fun PreviewLongNameUser() {
    MyTheme {
        UserProfileCard(
            user = User("아주긴이름", "long@example.com", false)
        )
    }
}

@Preview(name = "이메일 없는 사용자")
@Composable
fun PreviewNoEmailUser() {
    MyTheme {
        UserProfileCard(
            user = User("테스트", null, false)
        )
    }
}

// 다크모드도 테스트하려면? 또 4개 추가...
// 폰트 스케일도? 또 4개 추가...
// 총 12개 이상의 Preview 함수가 필요합니다!
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            // 문제점 요약
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "이 방식의 문제점",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))

                    val problems = listOf(
                        "MyTheme { UserProfileCard(...) } 구조가 모든 함수에서 반복",
                        "새 상태 추가 시 복사-붙여넣기 필요",
                        "테마/폰트 변경 시 모든 함수 수정 필요",
                        "실수로 일부 함수만 수정하고 나머지 누락 가능"
                    )

                    problems.forEach { problem ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("*", color = MaterialTheme.colorScheme.error)
                            Text(
                                problem,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================
// Preview 테스트용 샘플 데이터 클래스
// ============================================

/**
 * 사용자 정보를 담는 데이터 클래스
 */
data class UserProfile(
    val name: String,
    val email: String?,
    val isVerified: Boolean,
    val avatarUrl: String? = null
)

/**
 * UI 상태를 나타내는 sealed interface
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
    data object Empty : UiState<Nothing>
}
