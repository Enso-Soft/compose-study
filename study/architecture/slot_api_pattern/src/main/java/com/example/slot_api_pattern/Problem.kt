package com.example.slot_api_pattern

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - Props 기반 접근의 한계
 *
 * Props(속성)만으로 컴포넌트를 커스터마이징하려고 하면:
 * 1. 파라미터가 계속 늘어남 (Parameter Explosion)
 * 2. 새로운 요구사항마다 API 변경 필요
 * 3. 스타일링 자유도 제한
 * 4. 복잡한 조건부 로직 증가
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
            text = "Problem: Props 기반 접근의 한계",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Props만 사용하면 컴포넌트가 유연하지 않습니다.")
                Text("새로운 요구사항이 생길 때마다 파라미터를 추가해야 합니다.")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 1: 파라미터 폭발
        Text(
            text = "문제 1: 파라미터 폭발 (Parameter Explosion)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 경직된 카드 - 파라미터가 많음
        RigidInfoCard(
            title = "시스템 경고",
            subtitle = "디스크 용량이 부족합니다",
            iconType = IconType.WARNING,
            iconColor = Color(0xFFFF9800),
            showBorder = true,
            borderColor = Color(0xFFFF9800),
            showAction = true,
            actionText = "정리하기",
            onActionClick = { }
        )

        Spacer(modifier = Modifier.height(8.dp))

        RigidInfoCard(
            title = "연결 성공",
            subtitle = "서버와 연결되었습니다",
            iconType = IconType.SUCCESS,
            iconColor = Color(0xFF4CAF50),
            showBorder = false,
            borderColor = Color.Transparent,
            showAction = false,
            actionText = "",
            onActionClick = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("RigidInfoCard의 문제점:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("- 이미 10개 이상의 파라미터")
                Text("- 커스텀 아이콘 사용 불가 (IconType으로 제한)")
                Text("- 여러 액션 버튼 불가")
                Text("- 복잡한 레이아웃 불가")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 2: 유연하지 않은 구조
        Text(
            text = "문제 2: 유연하지 않은 구조",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 경직된 버튼 - 순서 변경 불가
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RigidButton(
                text = "저장하기",
                iconType = IconType.SUCCESS,
                onClick = { }
            )

            RigidButton(
                text = "취소",
                iconType = IconType.ERROR,
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("RigidButton의 문제점:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("- 아이콘은 항상 왼쪽에 고정")
                Text("- 아이콘과 텍스트 순서 변경 불가")
                Text("- 로딩 인디케이터 추가 불가")
                Text("- 복잡한 콘텐츠 구성 불가")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 3: 새로운 요구사항 대응 어려움
        Text(
            text = "문제 3: 새 요구사항마다 API 변경 필요",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("요청: '배지를 추가해주세요'", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    """
// 기존 함수에 파라미터 추가 필요
fun RigidInfoCard(
    title: String,
    subtitle: String,
    iconType: IconType,
    iconColor: Color,
    showBorder: Boolean,
    borderColor: Color,
    showAction: Boolean,
    actionText: String,
    onActionClick: () -> Unit,
    // 새로운 요구사항!
    showBadge: Boolean = false,      // 추가
    badgeText: String = "",          // 추가
    badgeColor: Color = Color.Red    // 추가
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Slot API 패턴 사용!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- Composable Lambda를 파라미터로 받음")
                Text("- 어떤 UI든 자유롭게 주입 가능")
                Text("- API 변경 없이 새로운 요구사항 대응")
                Text("- Solution 탭에서 확인하세요!")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ============================================================
// 문제가 있는 컴포넌트들 (Props 기반)
// ============================================================

enum class IconType {
    INFO, WARNING, SUCCESS, ERROR
}

/**
 * 경직된 정보 카드 - 파라미터가 너무 많음!
 *
 * 새로운 요구사항이 생길 때마다 파라미터를 추가해야 함
 */
@Composable
fun RigidInfoCard(
    title: String,
    subtitle: String,
    iconType: IconType,
    iconColor: Color,
    showBorder: Boolean,
    borderColor: Color,
    showAction: Boolean,
    actionText: String,
    onActionClick: () -> Unit
) {
    val icon: ImageVector = when (iconType) {
        IconType.INFO -> Icons.Default.Info
        IconType.WARNING -> Icons.Default.Warning
        IconType.SUCCESS -> Icons.Default.Check
        IconType.ERROR -> Icons.Default.Close
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (showBorder) {
                    Modifier.background(borderColor.copy(alpha = 0.1f))
                } else {
                    Modifier
                }
            )
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
                tint = iconColor,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showAction) {
                TextButton(onClick = onActionClick) {
                    Text(actionText)
                }
            }
        }
    }
}

/**
 * 경직된 버튼 - 레이아웃 변경 불가
 *
 * 아이콘은 항상 왼쪽, 텍스트는 항상 오른쪽
 * 순서 변경이나 복잡한 구성 불가
 */
@Composable
fun RigidButton(
    text: String,
    iconType: IconType,
    onClick: () -> Unit
) {
    val icon: ImageVector = when (iconType) {
        IconType.INFO -> Icons.Default.Info
        IconType.WARNING -> Icons.Default.Warning
        IconType.SUCCESS -> Icons.Default.Check
        IconType.ERROR -> Icons.Default.Close
    }

    Button(onClick = onClick) {
        // 항상 아이콘이 먼저, 텍스트가 나중
        // 이 순서를 바꿀 수 없음!
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
