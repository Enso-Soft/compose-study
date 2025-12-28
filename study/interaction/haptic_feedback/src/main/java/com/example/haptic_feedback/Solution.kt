package com.example.haptic_feedback

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map

/**
 * 해결책 화면
 *
 * Haptic Feedback을 사용하여 터치 인터랙션에 촉각 피드백을 추가하는 방법을 시연합니다.
 * 사용자가 버튼을 누르거나 슬라이더를 조작할 때 진동으로 반응을 느낄 수 있습니다.
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Haptic Feedback 적용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Compose에서 제공하는 LocalHapticFeedback과 View의 performHapticFeedback을 " +
                            "사용하여 터치 시 진동 피드백을 제공합니다. 실제 디바이스에서 테스트해보세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 안내 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "햅틱 피드백은 에뮬레이터에서 동작하지 않습니다. 반드시 실제 디바이스에서 테스트하세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 방법 1: LocalHapticFeedback
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "방법 1: LocalHapticFeedback (Compose 표준)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Compose가 제공하는 표준 방식입니다. LongPress와 TextHandleMove 타입을 지원합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                LocalHapticFeedbackDemo()
            }
        }

        // 방법 2: LocalView + HapticFeedbackConstants
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "방법 2: LocalView (더 다양한 타입)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "View의 performHapticFeedback을 사용하면 CLOCK_TICK, CONFIRM, REJECT 등 " +
                            "더 다양한 햅틱 타입을 사용할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                ViewHapticFeedbackDemo()
            }
        }

        // 방법 3: 슬라이더 틱 햅틱
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "방법 3: 슬라이더 틱 햅틱",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "snapshotFlow를 사용하여 값이 특정 단계를 넘을 때마다 틱 햅틱을 발생시킵니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                SliderTickHapticDemo()
            }
        }

        // 방법 4: 롱프레스 햅틱
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "방법 4: 롱프레스 햅틱",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "combinedClickable의 onLongClick에서 햅틱 피드백을 발생시켜 " +
                            "롱프레스 인식을 사용자에게 알립니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                LongPressHapticDemo()
            }
        }

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                KeyPointItem("LocalHapticFeedback: Compose 표준 방식, 제한된 타입")
                KeyPointItem("LocalView: 더 다양한 HapticFeedbackConstants 접근 가능")
                KeyPointItem("CLOCK_TICK: 미세한 틱 느낌 (스크롤, 슬라이더용)")
                KeyPointItem("CONFIRM/REJECT: 성공/실패 피드백 (API 30+)")
                KeyPointItem("상황에 맞는 적절한 타입 선택이 UX 품질 결정!")
            }
        }

        // HapticFeedbackType 비교 표
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "HapticFeedbackType 비교",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                HapticTypeComparisonTable()
            }
        }

        // 올바른 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드",
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
// 방법 1: LocalHapticFeedback
val haptics = LocalHapticFeedback.current
Button(onClick = {
    haptics.performHapticFeedback(
        HapticFeedbackType.LongPress
    )
    isLiked = !isLiked
}) { ... }

// 방법 2: LocalView (더 많은 타입)
val view = LocalView.current
Button(onClick = {
    view.performHapticFeedback(
        HapticFeedbackConstants.CONFIRM
    )
}) { ... }
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
private fun KeyPointItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "V ",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LocalHapticFeedbackDemo() {
    val haptics = LocalHapticFeedback.current
    var isLiked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LongPress 타입
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
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
                Text(
                    text = "LongPress",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // TextHandleMove 타입
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                ) {
                    Text("TextHandleMove")
                }
                Text(
                    text = "텍스트 선택용",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ViewHapticFeedbackDemo() {
    val view = LocalView.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CLOCK_TICK
            OutlinedButton(
                onClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                }
            ) {
                Text("CLOCK_TICK")
            }

            // LONG_PRESS
            OutlinedButton(
                onClick = {
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                }
            ) {
                Text("LONG_PRESS")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // CONFIRM (API 30+)
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                    } else {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("CONFIRM")
            }

            // REJECT (API 30+)
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        view.performHapticFeedback(HapticFeedbackConstants.REJECT)
                    } else {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("REJECT")
            }
        }

        Text(
            text = "CONFIRM/REJECT는 Android 11(API 30) 이상에서 지원",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SliderTickHapticDemo() {
    val view = LocalView.current
    var volume by remember { mutableFloatStateOf(50f) }

    // 10단계마다 틱 햅틱 발생
    LaunchedEffect(Unit) {
        snapshotFlow { volume }
            .map { (it / 10).toInt() }
            .distinctUntilChanged()
            .drop(1) // 초기값 스킵
            .collect {
                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
            }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "볼륨: ${volume.toInt()}%",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = volume,
            onValueChange = { volume = it },
            valueRange = 0f..100f,
            steps = 9, // 10단계
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "10단계마다 틱 햅틱이 발생합니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LongPressHapticDemo() {
    val haptics = LocalHapticFeedback.current
    var isSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        // 일반 클릭
                    },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        isSelected = !isSelected
                    }
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
                    text = if (isSelected) "선택됨! (햅틱 느껴졌나요?)" else "롱프레스 시 햅틱 피드백 발생",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HapticTypeComparisonTable() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("타입", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("용도", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
            Text("제공 방식", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        HorizontalDivider()

        // 데이터 행들
        TableRow("LongPress", "롱프레스 확인", "Compose")
        TableRow("TextHandleMove", "텍스트 선택", "Compose")
        TableRow("CLOCK_TICK", "틱/스크롤", "View")
        TableRow("CONFIRM", "성공 피드백", "View (API 30+)")
        TableRow("REJECT", "실패 피드백", "View (API 30+)")
        TableRow("KEYBOARD_PRESS", "키보드 입력", "View")
    }
}

@Composable
private fun TableRow(type: String, usage: String, provider: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(type, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
        Text(usage, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1.5f))
        Text(provider, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
    }
}
