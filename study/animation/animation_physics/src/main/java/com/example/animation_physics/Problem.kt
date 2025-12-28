package com.example.animation_physics

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * tween 애니메이션의 한계를 보여줍니다:
 * 1. 기계적이고 딱딱한 움직임
 * 2. 중단 시 속도 불연속
 * 3. 자연스럽지 않은 느낌
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
                    text = "문제 상황: tween 애니메이션의 한계",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "시간 기반 애니메이션(tween)을 사용하면 움직임이 기계적이고 딱딱합니다. " +
                            "아래 버튼을 빠르게 여러 번 클릭해보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 데모 1: tween 버튼의 딱딱함
        TweenButtonDemo()

        // 데모 2: 중단 시 속도 불연속
        InterruptionDemo()

        // 문제점 정리 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProblemPoint("1. 기계적인 움직임: 항상 정확히 같은 시간 동안 움직임")
                ProblemPoint("2. 중단 불연속: 애니메이션 중간에 방향을 바꾸면 속도가 0으로 리셋")
                ProblemPoint("3. 자연스럽지 않음: 현실 세계의 물리 법칙과 어긋남")
                ProblemPoint("4. 제스처 연결 불가: 드래그 속도를 애니메이션에 반영할 수 없음")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = """
// tween - 시간 기반 애니메이션
val scale by animateFloatAsState(
    targetValue = if (pressed) 0.9f else 1f,
    animationSpec = tween(
        durationMillis = 150,
        easing = LinearEasing  // 선형 이징
    )
)

// 문제점:
// - 항상 150ms 고정
// - 중단 시 속도 0으로 리셋
// - 로봇처럼 딱딱한 느낌
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProblemPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * 데모 1: tween 버튼의 딱딱한 움직임
 */
@Composable
private fun TweenButtonDemo() {
    var isPressed by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    // tween 애니메이션 - 딱딱한 움직임
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        ),
        label = "tween_scale"
    )

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demo 1: tween 버튼",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "빠르게 연타해보세요 - 딱딱하고 끊기는 느낌",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "클릭 횟수: $clickCount",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .scale(scale)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                tryAwaitRelease()
                                isPressed = false
                                clickCount++
                            }
                        )
                    }
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TWEEN 버튼",
                    color = MaterialTheme.colorScheme.onError,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "animationSpec = tween(150ms, LinearEasing)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 데모 2: 중단 시 속도 불연속
 */
@Composable
private fun InterruptionDemo() {
    var targetPosition by remember { mutableFloatStateOf(0f) }

    // tween 애니메이션 - 중단 시 속도 불연속
    val position by animateFloatAsState(
        targetValue = targetPosition,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "tween_position"
    )

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demo 2: 중단 시 속도 불연속",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "움직이는 중간에 방향을 바꿔보세요 - 속도가 갑자기 리셋됨",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 애니메이션 시각화
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = (position * 250).dp)
                        .padding(8.dp)
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { targetPosition = 0f },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("왼쪽")
                }
                Button(
                    onClick = { targetPosition = 1f },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("오른쪽")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "tween(1000ms) - 중간에 바꾸면 급정지 후 재시작",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
