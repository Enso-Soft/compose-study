package com.example.gesture_advanced

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp

// ============================================================================
// 연습 문제 1: 핀치 줌 이미지 뷰어 (쉬움)
// ============================================================================

/**
 * 연습 1: 핀치 줌 이미지 뷰어
 *
 * detectTransformGestures를 사용하여 핀치 줌을 구현하세요.
 *
 * 요구사항:
 * - 핀치 제스처로 확대/축소
 * - 줌 범위: 0.5배 ~ 3배로 제한
 * - graphicsLayer로 스케일 적용
 */
@Composable
fun Practice1_PinchZoomScreen() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 핀치 줌 (쉬움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "detectTransformGestures를 사용하여\n" +
                            "핀치 줌으로 상자를 확대/축소하세요.\n\n" +
                            "요구사항:\n" +
                            "- 줌 범위: 0.5배 ~ 3배\n" +
                            "- graphicsLayer로 scaleX, scaleY 적용",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- Modifier.pointerInput(Unit) { detectTransformGestures { ... } }")
                Text("- detectTransformGestures { centroid, pan, zoom, rotation -> }")
                Text("- scale = (scale * zoom).coerceIn(0.5f, 3f)")
                Text("- graphicsLayer(scaleX = scale, scaleY = scale)")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 상태 변수를 선언하세요
    // var scale by remember { mutableFloatStateOf(1f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // TODO: 현재 스케일 값을 표시하세요
        Text(
            text = "Scale: ??? (구현 필요)",
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
                    // TODO: pointerInput과 detectTransformGestures를 추가하세요
                    // .pointerInput(Unit) {
                    //     detectTransformGestures { centroid, pan, zoom, rotation ->
                    //         // 줌 적용 (범위 제한)
                    //         scale = (scale * zoom).coerceIn(0.5f, 3f)
                    //     }
                    // }
                    // TODO: graphicsLayer로 스케일을 적용하세요
                    // .graphicsLayer(
                    //     scaleX = scale,
                    //     scaleY = scale
                    // )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pinch\nZoom",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 핀치 줌을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }

    /* 정답:
    var scale by remember { mutableFloatStateOf(1f) }

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
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 3f)
                        }
                    }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pinch\nZoom",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
    */
}

// ============================================================================
// 연습 문제 2: 이동 가능한 줌 뷰어 (중간)
// ============================================================================

/**
 * 연습 2: 이동 가능한 줌 뷰어
 *
 * 핀치 줌과 드래그를 함께 구현하세요.
 *
 * 요구사항:
 * - 핀치 줌 (0.5배 ~ 3배)
 * - 드래그로 이동
 * - (보너스) 경계 밖으로 나가지 않도록 제한
 */
@Composable
fun Practice2_ZoomPanScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 줌 + 이동 (중간)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "detectTransformGestures의 pan 파라미터를 사용하여\n" +
                            "줌과 함께 드래그 이동을 구현하세요.\n\n" +
                            "요구사항:\n" +
                            "- 핀치 줌 (0.5배 ~ 3배)\n" +
                            "- 드래그로 이동\n" +
                            "- (보너스) 경계 제한",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- var offset by remember { mutableStateOf(Offset.Zero) }")
                Text("- detectTransformGestures의 pan 파라미터 사용")
                Text("- offset += pan")
                Text("- graphicsLayer(translationX, translationY)")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice2_Exercise()
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: scale과 offset 상태 변수를 선언하세요
    // var scale by remember { mutableFloatStateOf(1f) }
    // var offset by remember { mutableStateOf(Offset.Zero) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Scale: ??? / Offset: (???, ???)",
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
                    .size(80.dp)
                    // TODO: pointerInput + detectTransformGestures 추가
                    // zoom과 pan 모두 처리
                    // .pointerInput(Unit) {
                    //     detectTransformGestures { _, pan, zoom, _ ->
                    //         scale = (scale * zoom).coerceIn(0.5f, 3f)
                    //         offset += pan
                    //         // 보너스: 경계 제한
                    //         // val maxOffset = 85f
                    //         // offset = Offset(
                    //         //     offset.x.coerceIn(-maxOffset, maxOffset),
                    //         //     offset.y.coerceIn(-maxOffset, maxOffset)
                    //         // )
                    //     }
                    // }
                    // TODO: graphicsLayer로 scale과 offset 적용
                    // .graphicsLayer(
                    //     scaleX = scale,
                    //     scaleY = scale,
                    //     translationX = offset.x,
                    //     translationY = offset.y
                    // )
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Zoom\n+ Pan",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 줌 + 이동을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }

    /* 정답:
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Scale: %.2f / Offset: (%.1f, %.1f)".format(scale, offset.x, offset.y),
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
                    .size(80.dp)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 3f)
                            offset += pan

                            // 경계 제한
                            val maxOffset = 85f
                            offset = Offset(
                                offset.x.coerceIn(-maxOffset, maxOffset),
                                offset.y.coerceIn(-maxOffset, maxOffset)
                            )
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
                    text = "Zoom\n+ Pan",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
    */
}

// ============================================================================
// 연습 문제 3: 플링 가능한 이미지 뷰어 (어려움)
// ============================================================================

/**
 * 연습 3: 플링 가능한 이미지 뷰어
 *
 * VelocityTracker와 Animatable을 사용하여 플링 애니메이션을 구현하세요.
 *
 * 요구사항:
 * - 드래그로 이동
 * - 빠르게 스와이프하면 관성 이동 (플링)
 * - 경계 제한
 */
@Composable
fun Practice3_FlingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 플링 애니메이션 (어려움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "VelocityTracker로 속도를 추적하고\n" +
                            "Animatable로 플링 애니메이션을 구현하세요.\n\n" +
                            "요구사항:\n" +
                            "- awaitEachGesture + drag로 드래그 처리\n" +
                            "- VelocityTracker로 속도 추적\n" +
                            "- animateDecay로 플링 구현\n" +
                            "- 경계 제한",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }")
                Text("- VelocityTracker().addPosition(uptimeMillis, position)")
                Text("- velocityTracker.calculateVelocity()")
                Text("- offset.animateDecay(velocity, exponentialDecay())")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice3_Exercise()
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 필요한 상태와 scope를 선언하세요
    // val scope = rememberCoroutineScope()
    // val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Offset: (???, ???)",
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
                    // TODO: graphicsLayer로 offset 적용
                    // .graphicsLayer(
                    //     translationX = offset.value.x,
                    //     translationY = offset.value.y
                    // )
                    // TODO: pointerInput으로 플링 구현
                    // .pointerInput(Unit) {
                    //     val velocityTracker = VelocityTracker()
                    //     val maxBound = 95f
                    //
                    //     awaitEachGesture {
                    //         val pointerId = awaitFirstDown().id
                    //         velocityTracker.resetTracking()
                    //
                    //         drag(pointerId) { change ->
                    //             velocityTracker.addPosition(change.uptimeMillis, change.position)
                    //
                    //             val newOffset = offset.value + change.positionChange()
                    //             val constrained = Offset(
                    //                 newOffset.x.coerceIn(-maxBound, maxBound),
                    //                 newOffset.y.coerceIn(-maxBound, maxBound)
                    //             )
                    //             scope.launch { offset.snapTo(constrained) }
                    //             change.consume()
                    //         }
                    //
                    //         // 플링
                    //         val velocity = velocityTracker.calculateVelocity()
                    //         scope.launch {
                    //             offset.animateDecay(
                    //                 Offset(velocity.x, velocity.y),
                    //                 exponentialDecay(2f)
                    //             )
                    //             // 경계 체크
                    //             val bounded = Offset(
                    //                 offset.value.x.coerceIn(-maxBound, maxBound),
                    //                 offset.value.y.coerceIn(-maxBound, maxBound)
                    //             )
                    //             offset.snapTo(bounded)
                    //         }
                    //     }
                    // }
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Fling",
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 플링 애니메이션을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }

    /* 정답:
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
                        val maxBound = 95f

                        awaitEachGesture {
                            val pointerId = awaitFirstDown().id
                            velocityTracker.resetTracking()

                            drag(pointerId) { change ->
                                velocityTracker.addPosition(
                                    change.uptimeMillis,
                                    change.position
                                )

                                val newOffset = offset.value + change.positionChange()
                                val constrained = Offset(
                                    newOffset.x.coerceIn(-maxBound, maxBound),
                                    newOffset.y.coerceIn(-maxBound, maxBound)
                                )
                                scope.launch { offset.snapTo(constrained) }
                                change.consume()
                            }

                            val velocity = velocityTracker.calculateVelocity()
                            scope.launch {
                                offset.animateDecay(
                                    Offset(velocity.x, velocity.y),
                                    exponentialDecay(2f)
                                )
                                val bounded = Offset(
                                    offset.value.x.coerceIn(-maxBound, maxBound),
                                    offset.value.y.coerceIn(-maxBound, maxBound)
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
                    text = "Fling",
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
    */
}
