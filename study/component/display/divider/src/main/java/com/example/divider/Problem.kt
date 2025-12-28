package com.example.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - Box로 직접 구분선 그리기
 *
 * 이 코드의 문제점:
 * 1. 코드 중복: 구분선마다 같은 Box 코드 반복
 * 2. 두께 변경 어려움: height(1.dp)를 모든 곳에서 수정해야 함
 * 3. 색상 관리 어려움: 테마 색상이 아닌 직접 지정
 * 4. 수직 구분선 구현 복잡: fillMaxHeight()가 제대로 동작하지 않음
 * 5. Material Design 미준수: 표준 divider 스타일과 다름
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Problem: Box로 직접 구분선 그리기",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "HorizontalDivider/VerticalDivider 없이 Box로 직접 구분선을 그리면 어떤 문제가 발생하는지 확인해보세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 1: 수평 구분선 직접 구현
        ProblemCard(
            title = "문제 1: 수평 구분선 직접 구현",
            description = "Box로 구분선을 만들면 코드가 반복됩니다."
        ) {
            SettingsWithManualHorizontalDividers()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 2: 수직 구분선 직접 구현
        ProblemCard(
            title = "문제 2: 수직 구분선 직접 구현",
            description = "수직 구분선은 더 복잡합니다. fillMaxHeight()가 제대로 동작하지 않아요!"
        ) {
            StatsWithManualVerticalDividers()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 비교 카드
        CodeComparisonCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 정리
        ProblemSummaryCard()
    }
}

/**
 * Box로 직접 구현한 수평 구분선
 * 문제: 코드가 길고 반복적임
 */
@Composable
private fun ManualHorizontalDivider(
    thickness: Int = 1,
    color: Color = Color.LightGray
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness.dp)
            .background(color)
    )
}

/**
 * Box로 직접 구현한 수직 구분선
 * 문제: fillMaxHeight()가 부모의 높이에 의존함
 */
@Composable
private fun ManualVerticalDivider(
    thickness: Int = 1,
    color: Color = Color.LightGray
) {
    Box(
        modifier = Modifier
            .width(thickness.dp)
            .fillMaxHeight()  // 부모가 높이를 정해줘야 동작!
            .background(color)
    )
}

/**
 * 수평 구분선을 직접 구현한 설정 화면
 */
@Composable
private fun SettingsWithManualHorizontalDividers() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 설정 아이템들 사이에 Box로 직접 구분선 추가
        SettingItemRow(Icons.Default.AccountCircle, "계정 설정")
        ManualHorizontalDivider()  // 반복!
        SettingItemRow(Icons.Default.Notifications, "알림 설정")
        ManualHorizontalDivider()  // 또 반복!
        SettingItemRow(Icons.Default.Security, "보안 설정")
        ManualHorizontalDivider()  // 또 반복!
        SettingItemRow(Icons.Default.Settings, "일반 설정")
    }
}

/**
 * 수직 구분선을 직접 구현한 통계 대시보드
 */
@Composable
private fun StatsWithManualVerticalDividers() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "수직 구분선이 보이지 않는 문제:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 높이가 지정되지 않은 Row - 수직 구분선이 안 보임!
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem("조회수", "1,234")
            ManualVerticalDivider()  // 안 보임! (높이가 0)
            StatItem("좋아요", "567")
            ManualVerticalDivider()  // 안 보임!
            StatItem("댓글", "89")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "고정 높이로 해결하면?",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 높이를 고정하면 보이지만... 유연성이 없음
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)  // 높이 하드코딩 필요
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem("조회수", "1,234")
            ManualVerticalDivider()  // 이제 보이지만...
            StatItem("좋아요", "567")
            ManualVerticalDivider()
            StatItem("댓글", "89")
        }

        Text(
            text = "높이를 하드코딩하면 내용이 바뀔 때 문제가 생깁니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun SettingItemRow(
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProblemCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun CodeComparisonCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Box로 직접 구현한 구분선 코드",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// 수평 구분선 - 5줄!
Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Color.LightGray)
)

// 수직 구분선 - 5줄 + 부모 높이 설정 필요!
Box(
    modifier = Modifier
        .width(1.dp)
        .fillMaxHeight()
        .background(Color.LightGray)
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun ProblemSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제점 정리",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            val problems = listOf(
                "1. 코드 중복: 구분선마다 같은 Box 코드 반복",
                "2. 두께 변경 어려움: height()를 모든 곳에서 수정해야 함",
                "3. 색상 관리 어려움: 테마 색상이 아닌 직접 지정",
                "4. 수직 구분선 복잡: fillMaxHeight()가 제대로 동작하지 않음",
                "5. Material Design 미준수: 표준 divider 스타일과 다름"
            )

            problems.forEach { problem ->
                Text(
                    text = problem,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
