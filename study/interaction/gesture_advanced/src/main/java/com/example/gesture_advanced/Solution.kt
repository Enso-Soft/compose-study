package com.example.gesture_advanced

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 해결책 화면
 *
 * pointerInput과 고급 제스처 API를 사용하여 문제를 해결합니다.
 *
 * 해결책:
 * 1. detectTransformGestures: 핀치 줌 + 드래그 + 회전을 한 번에 처리
 * 2. coerceIn: 줌 범위 제한
 * 3. detectTapGestures: 더블탭 줌
 * 4. VelocityTracker + Animatable: 플링 애니메이션
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
                    text = "해결책: 고급 제스처 API",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "pointerInput과 detectTransformGestures를 사용하면\n" +
                            "줌 범위 제한, 경계 제한, 더블탭 줌, 플링까지 모두 구현할 수 있습니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 데모 1: 기본 핀치 줌 (제한 있음)
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "1. detectTransformGestures + 줌 제한",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                BasicTransformDemo()
            }
        }

        // 데모 2: 더블탭 줌
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "2. 더블탭 줌 (detectTapGestures)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                DoubleTapZoomDemo()
            }
        }

        // 데모 3: 플링 가능한 드래그
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "3. 플링 애니메이션 (VelocityTracker)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlingDemo()
            }
        }

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. detectTransformGestures: centroid, pan, zoom, rotation을 한 번에 제공")
                Text("2. coerceIn(min, max): 값의 범위를 제한")
                Text("3. 별도의 pointerInput: 서로 다른 제스처를 분리")
                Text("4. VelocityTracker: 손가락 속도를 추적하여 플링 구현")
                Text("5. consume(): 이벤트를 소비하여 다른 제스처로 전파 방지")
            }
        }

        // 올바른 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드 (줌 제한)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
var scale by remember { mutableFloatStateOf(1f) }
var offset by remember { mutableStateOf(Offset.Zero) }

Box(
    modifier = Modifier
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                // 줌 범위를 0.5 ~ 3배로 제한!
                scale = (scale * zoom).coerceIn(0.5f, 3f)
                offset += pan
            }
        }
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 기본 Transform 데모: 줌 범위 제한
 */
@Composable
private fun BasicTransformDemo() {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Scale: %.2f (0.5 ~ 3.0)".format(scale),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Offset: (%.1f, %.1f)".format(offset.x, offset.y),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, rotationChange ->
                            // 줌 범위 제한!
                            scale = (scale * zoom).coerceIn(0.5f, 3f)
                            offset += pan
                            rotation += rotationChange
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                        rotationZ = rotation
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pinch\n& Drag",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "0.5배 이하로 축소되거나 3배 이상 확대되지 않습니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 더블탭 줌 데모
 */
@Composable
private fun DoubleTapZoomDemo() {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Scale: %.2f".format(scale),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    // 더블탭 감지
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { tapOffset ->
                                // 현재 1배면 2배로, 아니면 1배로
                                scale = if (scale > 1f) 1f else 2f
                                offset = Offset.Zero
                            }
                        )
                    }
                    // 핀치 줌 + 드래그
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 3f)
                            offset += pan
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Double\nTap!",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "더블탭으로 2배 줌 / 원래 크기 토글!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

/**
 * 플링 데모: VelocityTracker 사용
 */
@Composable
private fun FlingDemo() {
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Offset: (%.1f, %.1f)".format(offset.value.x, offset.value.y),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .graphicsLayer(
                        translationX = offset.value.x,
                        translationY = offset.value.y
                    )
                    .pointerInput(Unit) {
                        val velocityTracker = VelocityTracker()
                        val maxBound = 95f  // 경계 제한

                        awaitEachGesture {
                            val pointerId = awaitFirstDown().id
                            velocityTracker.resetTracking()

                            drag(pointerId) { change ->
                                velocityTracker.addPosition(
                                    change.uptimeMillis,
                                    change.position
                                )

                                val newOffset = offset.value + change.positionChange()
                                // 경계 제한 적용
                                val constrainedOffset = Offset(
                                    x = newOffset.x.coerceIn(-maxBound, maxBound),
                                    y = newOffset.y.coerceIn(-maxBound, maxBound)
                                )

                                scope.launch {
                                    offset.snapTo(constrainedOffset)
                                }

                                change.consume()
                            }

                            // 드래그 종료 -> 플링 시작
                            val velocity = velocityTracker.calculateVelocity()
                            scope.launch {
                                offset.animateDecay(
                                    initialVelocity = Offset(velocity.x, velocity.y),
                                    animationSpec = exponentialDecay(frictionMultiplier = 2f)
                                )
                                // 플링 후 경계 체크
                                val bounded = Offset(
                                    x = offset.value.x.coerceIn(-maxBound, maxBound),
                                    y = offset.value.y.coerceIn(-maxBound, maxBound)
                                )
                                offset.snapTo(bounded)
                            }
                        }
                    }
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Fling!",
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "빠르게 스와이프하면 관성 이동합니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
