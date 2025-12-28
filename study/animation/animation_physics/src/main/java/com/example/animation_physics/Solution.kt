package com.example.animation_physics

import androidx.compose.animation.core.*
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 해결책 화면
 *
 * spring()과 animateDecay()를 사용하여 자연스러운 물리 기반 애니메이션을 구현합니다.
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
                    text = "해결책: 물리 기반 애니메이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "spring()과 animateDecay()를 사용하면 현실 세계의 물리 법칙을 시뮬레이션하여 " +
                            "자연스럽고 생동감 있는 애니메이션을 만들 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Demo 1: Spring 버튼
        SpringButtonDemo()

        // Demo 2: Spring 탐험기
        SpringExplorer()

        // Demo 3: 중단 시 속도 연속성
        SpringInterruptionDemo()

        // Demo 4: 드래그 스냅백
        DragSnapbackDemo()

        // Demo 5: Decay Fling
        DecayFlingDemo()

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                KeyPoint("1. spring()은 물리 법칙을 시뮬레이션하여 자연스러운 움직임 제공")
                KeyPoint("2. dampingRatio로 바운스 정도, stiffness로 속도 조절")
                KeyPoint("3. 중단해도 속도가 연속적으로 유지됨 (tween과 다름)")
                KeyPoint("4. animateDecay()로 드래그 속도를 관성 애니메이션으로 연결")
                KeyPoint("5. Compose의 기본 애니메이션은 spring() (권장)")
            }
        }

        // 올바른 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = """
// spring - 물리 기반 애니메이션
val scale by animateFloatAsState(
    targetValue = if (pressed) 0.9f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
)

// decay - 관성 기반 감속
val decay = splineBasedDecay<Float>(density)
scope.launch {
    animatable.animateDecay(velocity, decay)
}
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
private fun KeyPoint(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

/**
 * Demo 1: Spring 버튼 - 바운스 효과
 */
@Composable
private fun SpringButtonDemo() {
    var isPressed by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    // spring 애니메이션 - 탄성 있는 움직임
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "spring_scale"
    )

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demo 1: Spring 버튼",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "빠르게 연타해도 자연스러운 바운스!",
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
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SPRING 버튼",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "spring(MediumBouncy, StiffnessMedium)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * Demo 2: Spring 파라미터 탐험기
 */
@Composable
private fun SpringExplorer() {
    var dampingIndex by remember { mutableIntStateOf(1) }
    var stiffnessIndex by remember { mutableIntStateOf(2) }
    var trigger by remember { mutableIntStateOf(0) }

    val dampingOptions = listOf(
        "HighBouncy (0.2)" to Spring.DampingRatioHighBouncy,
        "MediumBouncy (0.5)" to Spring.DampingRatioMediumBouncy,
        "LowBouncy (0.75)" to Spring.DampingRatioLowBouncy,
        "NoBouncy (1.0)" to Spring.DampingRatioNoBouncy
    )

    val stiffnessOptions = listOf(
        "VeryLow (50)" to Spring.StiffnessVeryLow,
        "Low (200)" to Spring.StiffnessLow,
        "MediumLow (400)" to Spring.StiffnessMediumLow,
        "Medium (1500)" to Spring.StiffnessMedium,
        "High (10000)" to Spring.StiffnessHigh
    )

    val animatedOffset by animateFloatAsState(
        targetValue = if (trigger % 2 == 0) 0f else 1f,
        animationSpec = spring(
            dampingRatio = dampingOptions[dampingIndex].second,
            stiffness = stiffnessOptions[stiffnessIndex].second
        ),
        label = "explorer_offset"
    )

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Demo 2: Spring 파라미터 탐험기",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "다양한 조합을 시험해보세요!",
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
                        .offset(x = (animatedOffset * 250).dp)
                        .padding(8.dp)
                        .size(44.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DampingRatio 선택
            Text(
                text = "DampingRatio (바운스): ${dampingOptions[dampingIndex].first}",
                style = MaterialTheme.typography.labelMedium
            )
            Slider(
                value = dampingIndex.toFloat(),
                onValueChange = { dampingIndex = it.toInt() },
                valueRange = 0f..3f,
                steps = 2
            )

            // Stiffness 선택
            Text(
                text = "Stiffness (강성): ${stiffnessOptions[stiffnessIndex].first}",
                style = MaterialTheme.typography.labelMedium
            )
            Slider(
                value = stiffnessIndex.toFloat(),
                onValueChange = { stiffnessIndex = it.toInt() },
                valueRange = 0f..4f,
                steps = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { trigger++ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("애니메이션 실행")
            }
        }
    }
}

/**
 * Demo 3: Spring 중단 시 속도 연속성
 */
@Composable
private fun SpringInterruptionDemo() {
    var targetPosition by remember { mutableFloatStateOf(0f) }

    // spring 애니메이션 - 중단해도 속도 연속
    val position by animateFloatAsState(
        targetValue = targetPosition,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "spring_position"
    )

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demo 3: 속도 연속성",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "중간에 방향을 바꿔도 자연스럽게 전환!",
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
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = { targetPosition = 0f }) {
                    Text("왼쪽")
                }
                Button(onClick = { targetPosition = 1f }) {
                    Text("오른쪽")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "spring() - 중간에 바꿔도 속도가 연속적!",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * Demo 4: 드래그 + 스프링 스냅백
 */
@Composable
private fun DragSnapbackDemo() {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demo 4: 드래그 스냅백",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "카드를 좌우로 드래그하고 놓아보세요!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { _, dragAmount ->
                                scope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            }
                        )
                    }
                    .width(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "드래그 카드",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "놓으면 바운스 복귀",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "animateTo(0f, spring(MediumBouncy, Low))",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * Demo 5: Decay Fling
 */
@Composable
private fun DecayFlingDemo() {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demo 5: Decay Fling",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "빠르게 밀어보세요 - 관성으로 미끄러집니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 트랙
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                // 슬라이더
                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .padding(8.dp)
                        .size(54.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .pointerInput(Unit) {
                            val decay = splineBasedDecay<Float>(density)
                            val velocityTracker = VelocityTracker()

                            detectDragGestures(
                                onDragStart = {
                                    scope.launch { offsetX.stop() }
                                },
                                onDrag = { change, dragAmount ->
                                    velocityTracker.addPosition(
                                        change.uptimeMillis,
                                        change.position
                                    )
                                    scope.launch {
                                        offsetX.snapTo(
                                            (offsetX.value + dragAmount.x)
                                                .coerceIn(0f, 250f)
                                        )
                                    }
                                },
                                onDragEnd = {
                                    val velocity = velocityTracker.calculateVelocity().x
                                    scope.launch {
                                        offsetX.updateBounds(0f, 250f)
                                        offsetX.animateDecay(velocity, decay)
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Fling",
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        offsetX.animateTo(0f, spring())
                    }
                }
            ) {
                Text("리셋")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "animateDecay(velocity, splineBasedDecay)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
