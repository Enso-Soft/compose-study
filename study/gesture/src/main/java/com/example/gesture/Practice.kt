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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 연습 문제 1: 탭 카운터
 *
 * 요구사항:
 * - 박스를 탭하면 tapCount 증가
 * - 박스를 더블탭하면 doubleTapCount 증가
 * - 박스를 롱프레스하면 longPressCount 증가
 * - 각 제스처별로 다른 배경색 표시 (잠깐 동안)
 *
 * TODO: pointerInput + detectTapGestures를 사용해서 구현하세요!
 */
@Composable
fun Practice1_TapCounterScreen() {
    var tapCount by remember { mutableIntStateOf(0) }
    var doubleTapCount by remember { mutableIntStateOf(0) }
    var longPressCount by remember { mutableIntStateOf(0) }
    var lastGesture by remember { mutableStateOf("없음") }

    // TODO: 힌트 - 이 변수로 배경색 변경
    // var backgroundColor by remember { mutableStateOf(Color.LightGray) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 탭 카운터",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: detectTapGestures를 사용해서 탭/더블탭/롱프레스를 카운트하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: pointerInput을 추가하세요
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
                // TODO: 여기에 pointerInput 추가
                // .pointerInput(Unit) {
                //     detectTapGestures(
                //         onTap = { tapCount++; lastGesture = "탭" },
                //         onDoubleTap = { doubleTapCount++; lastGesture = "더블탭" },
                //         onLongPress = { longPressCount++; lastGesture = "롱프레스" }
                //     )
                // }
            contentAlignment = Alignment.Center
        ) {
            Text("이 영역을 탭해보세요\n마지막 제스처: $lastGesture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 카운터 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("탭")
                Text("$tapCount", style = MaterialTheme.typography.headlineMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("더블탭")
                Text("$doubleTapCount", style = MaterialTheme.typography.headlineMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("롱프레스")
                Text("$longPressCount", style = MaterialTheme.typography.headlineMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Text("1. Modifier.pointerInput(Unit) { ... } 사용")
                Text("2. detectTapGestures(onTap, onDoubleTap, onLongPress)")
                Text("3. 각 콜백에서 카운터 증가 및 lastGesture 업데이트")
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
 * 연습 문제 2: 드래그 가능한 카드
 *
 * 요구사항:
 * - 카드를 자유롭게 드래그하여 이동
 * - 드래그 중에는 카드 색상 변경 (시각적 피드백)
 * - 드래그 거리 표시
 * - change.consume() 호출 필수
 *
 * TODO: pointerInput + detectDragGestures를 사용해서 구현하세요!
 */
@Composable
fun Practice2_DraggableCardScreen() {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var totalDragDistance by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: 드래그 가능한 카드",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: detectDragGestures를 사용해서 카드를 드래그하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 드래그 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            // TODO: 드래그 가능한 카드를 구현하세요
            Card(
                modifier = Modifier
                    .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                    .size(100.dp),
                    // TODO: 여기에 pointerInput 추가
                    // .pointerInput(Unit) {
                    //     detectDragGestures(
                    //         onDragStart = { isDragging = true },
                    //         onDrag = { change, dragAmount ->
                    //             change.consume()
                    //             offset += dragAmount
                    //             totalDragDistance += dragAmount.getDistance()
                    //         },
                    //         onDragEnd = { isDragging = false },
                    //         onDragCancel = { isDragging = false }
                    //     )
                    // }
                colors = CardDefaults.cardColors(
                    containerColor = if (isDragging)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isDragging) "드래그 중!" else "드래그",
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("현재 위치: (${offset.x.roundToInt()}, ${offset.y.roundToInt()})")
        Text("총 드래그 거리: ${totalDragDistance.roundToInt()}px")

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Text("1. offset { IntOffset(...) } 으로 위치 적용")
                Text("2. detectDragGestures의 onDrag에서:")
                Text("   - change.consume() 호출")
                Text("   - offset += dragAmount")
                Text("3. onDragStart/End로 isDragging 상태 관리")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            offset = Offset.Zero
            totalDragDistance = 0f
        }) {
            Text("위치 리셋")
        }
    }
}

/**
 * 연습 문제 3: 이미지 뷰어 (핀치 줌 & 회전)
 *
 * 요구사항:
 * - 두 손가락으로 확대/축소 (핀치 줌)
 * - 두 손가락으로 회전
 * - scale 범위 제한 (0.5f ~ 3f)
 * - graphicsLayer로 변환 적용
 *
 * TODO: pointerInput + detectTransformGestures를 사용해서 구현하세요!
 */
@Composable
fun Practice3_ImageViewerScreen() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: 이미지 뷰어",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: detectTransformGestures로 핀치 줌 & 회전을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 변환 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            // TODO: 변환 가능한 박스를 구현하세요
            Box(
                modifier = Modifier
                    .size(150.dp)
                    // TODO: 여기에 pointerInput 추가
                    // .pointerInput(Unit) {
                    //     detectTransformGestures { _, pan, zoom, rotationChange ->
                    //         scale = (scale * zoom).coerceIn(0.5f, 3f)
                    //         rotation += rotationChange
                    //         offset += pan
                    //     }
                    // }
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "핀치 줌",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "& 회전",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("배율: ${String.format("%.2f", scale)}x (0.5 ~ 3.0)")
        Text("회전: ${rotation.roundToInt()}°")
        Text("이동: (${offset.x.roundToInt()}, ${offset.y.roundToInt()})")

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Text("1. detectTransformGestures { _, pan, zoom, rotation -> ... }")
                Text("2. scale = (scale * zoom).coerceIn(0.5f, 3f)")
                Text("3. graphicsLayer { scaleX, scaleY, rotationZ, translation }")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "에뮬레이터: Ctrl + 마우스로 핀치 제스처",
                    style = MaterialTheme.typography.bodySmall
                )
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
    }
}
