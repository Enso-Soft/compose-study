package com.example.haptic_feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * Haptic Feedback을 사용하지 않았을 때 발생하는 UX 문제를 시연합니다.
 * 터치 인터랙션에 촉각적 피드백이 없으면 사용자가 액션 수행 여부를 확신하기 어렵습니다.
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: 피드백 없는 터치 인터랙션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 버튼과 슬라이더를 조작해보세요. 시각적 변화는 있지만 촉각적 피드백이 없어서 " +
                            "사용자가 액션이 수행되었는지 확신하기 어렵습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 데모 1: 피드백 없는 버튼
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "1. 피드백 없는 좋아요 버튼",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "버튼을 눌러보세요. 색상은 바뀌지만 손가락으로 '느껴지는' 반응이 없습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                NoHapticLikeButtonDemo()
            }
        }

        // 데모 2: 피드백 없는 슬라이더
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "2. 피드백 없는 볼륨 슬라이더",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "슬라이더를 움직여보세요. 현재 볼륨이 어느 단계인지 손가락으로 느낄 수 없습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                NoHapticSliderDemo()
            }
        }

        // 데모 3: 피드백 없는 롱프레스
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "3. 피드백 없는 롱프레스",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "카드를 길게 눌러보세요. 롱프레스가 인식되었는지 촉각으로 알 수 없습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                NoHapticLongPressDemo()
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProblemItem("1. 사용자가 액션 수행 여부를 확신하기 어려움")
                ProblemItem("2. 화면을 보지 않으면 인터랙션 상태 파악 불가")
                ProblemItem("3. 시각 장애 사용자에게 접근성 문제 발생")
                ProblemItem("4. 인터랙션의 '느낌'이 부족해 사용 만족도 저하")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 코드 (햅틱 없음)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// 햅틱 피드백이 없는 버튼
Button(onClick = {
    isLiked = !isLiked
    // 촉각 피드백 없음!
}) {
    Icon(...)
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun ProblemItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "X ",
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NoHapticLikeButtonDemo() {
    var isLiked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 햅틱 피드백 없는 좋아요 버튼
        IconButton(
            onClick = {
                // 햅틱 피드백 없음!
                isLiked = !isLiked
            },
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isLiked) Color.Red.copy(alpha = 0.1f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "좋아요",
                tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isLiked) "좋아요!" else "좋아요 없음",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NoHapticSliderDemo() {
    var volume by remember { mutableFloatStateOf(50f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "볼륨: ${volume.toInt()}%",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 햅틱 피드백 없는 슬라이더
        Slider(
            value = volume,
            onValueChange = {
                // 햅틱 피드백 없음!
                volume = it
            },
            valueRange = 0f..100f,
            steps = 9, // 10단계 (0, 10, 20, ..., 100)
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "10단계마다 틱 느낌이 있으면 좋겠지만...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NoHapticLongPressDemo() {
    var isSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { /* 일반 클릭 */ }
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "이 카드를 길게 눌러보세요",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "(롱프레스가 인식되어도 촉각 피드백 없음)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "롱프레스 인식 여부를 손가락으로 알 수 없음",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}
