package com.example.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 올바른 코드 - HorizontalDivider / VerticalDivider 사용
 *
 * Material 3의 Divider 컴포넌트를 사용하면:
 * 1. 한 줄 코드로 구분선 완성
 * 2. 파라미터로 두께, 색상 쉽게 조절
 * 3. 테마 색상 자동 적용
 * 4. VerticalDivider + IntrinsicSize.Min으로 수직 구분선도 간편
 * 5. Material Design 가이드라인 준수
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Solution: HorizontalDivider / VerticalDivider",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Material 3의 전용 컴포넌트를 사용하면 간단하고 일관된 구분선을 만들 수 있습니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 해결책 1: HorizontalDivider 기본 사용
        SolutionCard(
            title = "해결책 1: HorizontalDivider 기본",
            description = "한 줄 코드로 깔끔한 수평 구분선"
        ) {
            BasicHorizontalDividerDemo()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 2: 파라미터 커스터마이징
        SolutionCard(
            title = "해결책 2: thickness와 color 커스터마이징",
            description = "파라미터로 두께와 색상을 쉽게 변경"
        ) {
            CustomizedDividerDemo()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 3: Inset Divider
        SolutionCard(
            title = "해결책 3: Inset Divider (들여쓰기)",
            description = "Modifier.padding으로 여백 추가"
        ) {
            InsetDividerDemo()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 4: VerticalDivider + IntrinsicSize.Min
        SolutionCard(
            title = "해결책 4: VerticalDivider + IntrinsicSize.Min",
            description = "수직 구분선의 핵심 - Row에 height(IntrinsicSize.Min) 필수!"
        ) {
            VerticalDividerDemo()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 5: 리스트에서 Divider
        SolutionCard(
            title = "해결책 5: LazyColumn에서 Divider",
            description = "리스트 아이템 사이에 구분선 추가 (마지막 아이템 제외)"
        ) {
            ListDividerDemo()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 완성된 설정 화면 예시
        SolutionCard(
            title = "완성된 예시: 설정 화면",
            description = "HorizontalDivider를 활용한 깔끔한 설정 화면"
        ) {
            CompleteSettingsDemo()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 비교 카드
        CodeComparisonCard()
    }
}

/**
 * 기본 HorizontalDivider 데모
 */
@Composable
private fun BasicHorizontalDividerDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = "위 콘텐츠",
            modifier = Modifier.padding(16.dp)
        )

        // 한 줄로 구분선 완성!
        HorizontalDivider()

        Text(
            text = "아래 콘텐츠",
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * 두께와 색상 커스터마이징 데모
 */
@Composable
private fun CustomizedDividerDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = "기본 구분선 (1dp, 기본 색상)",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider()

        Text(
            text = "두꺼운 구분선 (2dp)",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(thickness = 2.dp)

        Text(
            text = "색상 변경 (primary 색상)",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

        Text(
            text = "두께 + 색상 조합",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

/**
 * Inset Divider 데모
 */
@Composable
private fun InsetDividerDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SettingRow(Icons.Default.Person, "프로필")

        // Full-width divider
        HorizontalDivider()

        SettingRow(Icons.Default.Email, "이메일")

        // Inset divider (왼쪽 여백)
        HorizontalDivider(
            modifier = Modifier.padding(start = 56.dp)  // 아이콘 너비만큼 들여쓰기
        )

        SettingRow(Icons.Default.Phone, "전화번호")

        // Inset divider (양쪽 여백)
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        SettingRow(Icons.Default.LocationOn, "주소")
    }
}

/**
 * VerticalDivider 데모 (핵심!)
 */
@Composable
private fun VerticalDividerDemo() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "핵심: Row에 height(IntrinsicSize.Min) 필수!",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // IntrinsicSize.Min 사용 - 구분선이 제대로 보임!
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)  // 핵심!
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatColumn("조회수", "1,234")

            VerticalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )

            StatColumn("좋아요", "567")

            VerticalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )

            StatColumn("댓글", "89")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "IntrinsicSize.Min이 뭔가요?",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "Row에게 '자식들의 최소 필요 높이를 확인해서 그에 맞춰 높이를 정해!'라고 알려주는 것입니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 리스트에서 Divider 사용 데모
 */
@Composable
private fun ListDividerDemo() {
    val items = listOf(
        "첫 번째 아이템",
        "두 번째 아이템",
        "세 번째 아이템",
        "네 번째 아이템"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        LazyColumn {
            itemsIndexed(items) { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // 마지막 아이템이 아닐 때만 구분선 추가
                if (index < items.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

/**
 * 완성된 설정 화면 예시
 */
@Composable
private fun CompleteSettingsDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 계정 섹션
        SectionHeader("계정")
        SettingRow(Icons.Default.Person, "프로필")
        HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
        SettingRow(Icons.Default.Lock, "비밀번호")

        // 섹션 구분 (두꺼운 구분선)
        HorizontalDivider(
            thickness = 8.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        // 알림 섹션
        SectionHeader("알림")
        SettingRow(Icons.Default.Notifications, "알림 소리")
        HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
        SettingRow(Icons.Default.Vibration, "진동")

        // 섹션 구분
        HorizontalDivider(
            thickness = 8.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        // 일반 섹션
        SectionHeader("일반")
        SettingRow(Icons.Default.Language, "언어")
        HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
        SettingRow(Icons.Default.Info, "앱 정보")
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun StatColumn(
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
private fun SolutionCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
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
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "코드 비교",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Before
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Before (Box)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = """
Box(
  Modifier
    .fillMaxWidth()
    .height(1.dp)
    .background(
      Color.Gray
    )
)
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }

                // After
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "After (Divider)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = """
HorizontalDivider()

// 또는 커스텀:
HorizontalDivider(
  thickness = 2.dp,
  color = Color.Gray
)
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
