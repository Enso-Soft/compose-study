package com.example.gesture

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 올바른 코드 - pointerInput + detectGestures 사용
 *
 * pointerInput을 사용하면:
 * 1. 탭, 더블탭, 롱프레스 모두 감지 가능
 * 2. 탭 위치(Offset) 정보 제공
 * 3. 드래그 제스처 지원
 * 4. 멀티터치(핀치 줌, 회전) 지원
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }
    val demos = listOf("탭 제스처", "드래그", "멀티터치")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

        // 데모 선택
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            demos.forEachIndexed { index, title ->
                FilterChip(
                    selected = selectedDemo == index,
                    onClick = { selectedDemo = index },
                    label = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> TapGestureDemo()
            1 -> DragGestureDemo()
            2 -> TransformGestureDemo()
        }
    }
}

/**
 * 데모 1: detectTapGestures - 탭 제스처
 */
@Composable
private fun TapGestureDemo() {
    var tapCount by remember { mutableIntStateOf(0) }
    var doubleTapCount by remember { mutableIntStateOf(0) }
    var longPressCount by remember { mutableIntStateOf(0) }
    var lastTapPosition by remember { mutableStateOf(Offset.Zero) }
    var lastGesture by remember { mutableStateOf("없음") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "detectTapGestures",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // pointerInput으로 탭 제스처 감지
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset ->
                                    tapCount++
                                    lastTapPosition = offset
                                    lastGesture = "탭"
                                },
                                onDoubleTap = { offset ->
                                    doubleTapCount++
                                    lastTapPosition = offset
                                    lastGesture = "더블탭"
                                },
                                onLongPress = { offset ->
                                    longPressCount++
                                    lastTapPosition = offset
                                    lastGesture = "롱프레스"
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("이 영역을 탭/더블탭/롱프레스 해보세요")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("마지막 제스처: $lastGesture")
                        Text(
                            "위치: (${lastTapPosition.x.roundToInt()}, ${lastTapPosition.y.roundToInt()})"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("탭", style = MaterialTheme.typography.labelSmall)
                        Text("$tapCount", style = MaterialTheme.typography.headlineSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("더블탭", style = MaterialTheme.typography.labelSmall)
                        Text("$doubleTapCount", style = MaterialTheme.typography.headlineSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("롱프레스", style = MaterialTheme.typography.labelSmall)
                        Text("$longPressCount", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- detectTapGestures로 탭/더블탭/롱프레스 모두 감지")
                Text("- 각 콜백에서 Offset(탭 위치) 정보 제공")
                Text("- onPress도 있음 (누르는 순간 감지)")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            tapCount = 0
            doubleTapCount = 0
            longPressCount = 0
            lastGesture = "없음"
        }) {
            Text("리셋")
        }
    }
}

/**
 * 데모 2: detectDragGestures - 드래그 제스처
 */
@Composable
private fun DragGestureDemo() {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var dragDistance by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "detectDragGestures",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 드래그 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    // 드래그 가능한 박스
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                            .size(80.dp)
                            .background(
                                if (isDragging) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.secondary
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = {
                                        isDragging = true
                                        dragDistance = Offset.Zero
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()  // 이벤트 소비!
                                        offset += dragAmount
                                        dragDistance += dragAmount
                                    },
                                    onDragEnd = {
                                        isDragging = false
                                    },
                                    onDragCancel = {
                                        isDragging = false
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isDragging) "드래그 중" else "드래그!",
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("현재 위치: (${offset.x.roundToInt()}, ${offset.y.roundToInt()})")
                Text("드래그 거리: (${dragDistance.x.roundToInt()}, ${dragDistance.y.roundToInt()})")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- detectDragGestures로 2D 드래그 감지")
                Text("- onDrag에서 change.consume() 호출 필수!")
                Text("- dragAmount로 이동 거리 계산")
                Text("- offset modifier로 위치 반영")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            offset = Offset.Zero
            dragDistance = Offset.Zero
        }) {
            Text("위치 리셋")
        }
    }
}

/**
 * 데모 3: detectTransformGestures - 멀티터치 (줌, 회전)
 *
 * 핵심: 제스처 감지 영역을 넓게 잡아야 핀치/회전이 제대로 동작함!
 * - 내부 작은 Box에서 감지 → 두 손가락이 모두 작은 영역 안에 있어야 함 (어려움)
 * - 외부 큰 Box에서 감지 → 넓은 영역에서 제스처 인식 가능 (쉬움)
 */
@Composable
private fun TransformGestureDemo() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "detectTransformGestures",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 핵심 변경: 외부 Box에서 제스처 감지!
                // 넓은 영역(300dp)에서 핀치/회전을 감지해야 제대로 동작함
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clipToBounds()  // 변환된 콘텐츠가 경계를 벗어나지 않도록
                        .background(MaterialTheme.colorScheme.surface)
                        .pointerInput(Unit) {
                            detectTransformGestures { centroid, pan, zoom, rotationChange ->
                                // zoom: 핀치 확대/축소 비율
                                scale = (scale * zoom).coerceIn(0.5f, 3f)
                                // rotationChange: 회전 각도 변화량 (도 단위)
                                rotation += rotationChange
                                // pan: 이동 거리
                                offset += pan
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // 내부 Box는 시각적 변환만 담당 (제스처 감지 X)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                rotationZ = rotation
                                translationX = offset.x
                                translationY = offset.y
                            }
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "핀치 줌\n& 회전",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    // 안내 텍스트 (하단)
                    Text(
                        text = "이 영역 아무 곳에서나\n두 손가락으로 핀치/회전",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 현재 상태 표시
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("배율", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "${String.format("%.2f", scale)}x",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("회전", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "${rotation.roundToInt()}°",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("이동", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "(${offset.x.roundToInt()}, ${offset.y.roundToInt()})",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- 외부 Box에서 제스처 감지 (넓은 영역)")
                Text("- 내부 Box는 graphicsLayer로 변환만")
                Text("- zoom: 핀치 확대/축소 비율")
                Text("- rotation: 회전 각도 (도 단위)")
                Text("- pan: 이동 거리")
                Text("- clipToBounds()로 경계 처리")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            scale = 1f
            rotation = 0f
            offset = Offset.Zero
        }) {
            Text("리셋")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "에뮬레이터: Ctrl 누른 채 마우스 드래그로 핀치 테스트",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
