package com.example.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem.kt - Row + Button으로 탭을 직접 만드는 어려움 시연
 *
 * 이 화면은 TabRow를 사용하지 않고 Row + Button으로 탭 UI를 직접 만들었을 때
 * 발생하는 문제점들을 보여줍니다.
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 상황 설명
        ProblemExplanationCard()

        Spacer(modifier = Modifier.height(24.dp))

        // 직접 만든 탭 UI 데모
        ManualTabsDemo()

        Spacer(modifier = Modifier.height(24.dp))

        // 문제점 목록
        ProblemListCard()
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
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Row + Button으로 탭 UI를 직접 만들면 어떤 문제가 발생할까요?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "아래 예제를 보며 문제점을 확인해보세요.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Row + Button으로 직접 만든 탭 UI
 * TabRow를 사용하지 않았을 때 발생하는 문제들을 시연합니다.
 */
@Composable
private fun ManualTabsDemo() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("음악", "동영상", "사진")

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "직접 만든 탭 (Row + Button)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 문제가 있는 탭 UI - Row + Button으로 직접 구현
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, title ->
                    Button(
                        onClick = { selectedTab = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (selectedTab == index)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    ) {
                        Text(title)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 콘텐츠 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                when (selectedTab) {
                    0 -> Text("음악 콘텐츠 영역")
                    1 -> Text("동영상 콘텐츠 영역")
                    2 -> Text("사진 콘텐츠 영역")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 눈에 보이는 문제 표시
            Text(
                text = "확인해보세요:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "- 탭을 선택해도 아래 슬라이딩 인디케이터가 없음",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "- 선택 시 부드러운 애니메이션 없음",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "- Material Design 탭과 전혀 다른 모양",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ProblemListCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "발생하는 문제점",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProblemItem(
                number = "1",
                title = "인디케이터 애니메이션 부재",
                description = "Material Design 탭은 선택 시 부드러운 인디케이터 슬라이딩 애니메이션이 있습니다. 직접 구현하려면 animateDpAsState, offset 계산 등 복잡한 코드가 필요합니다."
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProblemItem(
                number = "2",
                title = "Material Design 규격 위반",
                description = "탭 높이(48dp), 간격, 색상, ripple 효과 등 Material 가이드라인을 모두 수동으로 적용해야 합니다. 잘못된 디자인으로 앱 전체 일관성이 저하됩니다."
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProblemItem(
                number = "3",
                title = "접근성(a11y) 처리 누락",
                description = "탭 역할(role=\"tab\"), 선택 상태(selected), 탭 패널 연결 등 시맨틱 정보가 없습니다. 스크린 리더 사용자가 탭으로 인식하지 못합니다."
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProblemItem(
                number = "4",
                title = "일관성 문제",
                description = "탭 너비 균등 분배, 텍스트 오버플로우 처리, 긴 텍스트 말줄임 등을 모두 수동으로 관리해야 합니다."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "해결책: Solution 탭에서 TabRow를 사용한 올바른 구현을 확인하세요!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProblemItem(
    number: String,
    title: String,
    description: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.width(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
