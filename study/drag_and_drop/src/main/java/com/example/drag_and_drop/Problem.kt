package com.example.drag_and_drop

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 문제가 있는 코드 - pointerInput + detectDragGestures로 수동 구현
 *
 * 이 코드의 문제점:
 * 1. 드롭 대상 영역 판별을 수동으로 계산해야 함
 * 2. 데이터 전송 메커니즘이 없음 (위치만 변경)
 * 3. 다른 앱과 드래그 앤 드롭 불가능
 * 4. 시스템 드래그 섀도우 없음
 * 5. 호버링 상태 판별이 복잡함
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 1: 드롭 영역 수동 계산
        ManualDropZoneDemo()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 2: 데이터 전송 불가
        NoDataTransferDemo()

        Spacer(modifier = Modifier.height(16.dp))

        // 왜 문제인가?
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 문제인가?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 드롭 영역 진입/이탈을 수동 계산해야 함")
                Text("2. 드래그 시 데이터 전달 메커니즘 없음")
                Text("3. 다른 앱으로 드래그 불가능")
                Text("4. 시스템 드래그 섀도우 없음")
                Text("5. 접근성 서비스 통합 불가")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "-> dragAndDropSource/Target 사용!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "드래그 앤 드롭 예제",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 문제 1: 드롭 영역을 수동으로 계산해야 함
 */
@Composable
private fun ManualDropZoneDemo() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 1: 드롭 영역 수동 계산",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 드래그 가능한 박스의 오프셋
            var offset by remember { mutableStateOf(Offset.Zero) }
            var isDragging by remember { mutableStateOf(false) }

            // 드롭 영역의 경계 (수동 추적 필요!)
            var dropZoneBounds by remember { mutableStateOf(Rect.Zero) }
            var isOverDropZone by remember { mutableStateOf(false) }

            // 드래그 가능한 박스의 절대 위치 (수동 추적 필요!)
            var draggableBoxPosition by remember { mutableStateOf(Offset.Zero) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 드래그 영역
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    // 문제: 드래그 가능한 박스
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                            .size(60.dp)
                            .background(
                                when {
                                    isOverDropZone -> Color.Green
                                    isDragging -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.secondary
                                }
                            )
                            .onGloballyPositioned { coordinates ->
                                // 문제: 절대 위치를 수동으로 추적해야 함!
                                draggableBoxPosition = coordinates.boundsInRoot().center
                            }
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { isDragging = true },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        offset += dragAmount

                                        // 문제: 드롭 영역 진입 여부를 수동으로 계산!
                                        // 실시간으로 위치를 계산하고 경계와 비교해야 함
                                        val currentPos = draggableBoxPosition + dragAmount
                                        isOverDropZone = dropZoneBounds.contains(currentPos)
                                    },
                                    onDragEnd = {
                                        isDragging = false
                                        if (isOverDropZone) {
                                            // 드롭 성공? 하지만 데이터 전달이 없음!
                                        }
                                        offset = Offset.Zero
                                        isOverDropZone = false
                                    },
                                    onDragCancel = {
                                        isDragging = false
                                        offset = Offset.Zero
                                        isOverDropZone = false
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("드래그", color = Color.White)
                    }
                }

                // 드롭 대상 영역
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            if (isOverDropZone) Color.Green.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .onGloballyPositioned { coordinates ->
                            // 문제: 드롭 영역 경계를 수동으로 추적!
                            dropZoneBounds = coordinates.boundsInRoot()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isOverDropZone) "드롭 가능!" else "드롭 영역",
                        color = if (isOverDropZone)
                            Color.Green
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "문제: onGloballyPositioned + boundsInRoot로 영역 추적 필요",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Rect.contains()로 진입 여부 직접 계산해야 함",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 2: 데이터 전송 메커니즘 없음
 */
@Composable
private fun NoDataTransferDemo() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 2: 데이터 전송 불가",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            val items = listOf("Apple", "Banana", "Cherry")
            var droppedItem by remember { mutableStateOf<String?>(null) }

            // 문제: 어떤 아이템이 드래그되는지 외부 상태로 추적해야 함
            var currentlyDraggingItem by remember { mutableStateOf<String?>(null) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 드래그 가능한 아이템들
                Column {
                    items.forEach { item ->
                        var offset by remember { mutableStateOf(Offset.Zero) }

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .offset {
                                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                                }
                                .size(70.dp, 40.dp)
                                .background(MaterialTheme.colorScheme.primary)
                                .pointerInput(item) {
                                    detectDragGestures(
                                        onDragStart = {
                                            // 문제: 외부 상태로 현재 드래그 중인 아이템 추적
                                            currentlyDraggingItem = item
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            offset += dragAmount
                                        },
                                        onDragEnd = {
                                            // 문제: 어디에 드롭했는지 판별 후
                                            // 데이터를 "수동으로" 전달해야 함
                                            // 표준화된 데이터 전송 방법 없음!
                                            offset = Offset.Zero
                                            currentlyDraggingItem = null
                                        },
                                        onDragCancel = {
                                            offset = Offset.Zero
                                            currentlyDraggingItem = null
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(item, color = Color.White)
                        }
                    }
                }

                // 드롭 대상
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("바구니")
                        droppedItem?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "문제: 드래그 중인 아이템을 외부 상태로 추적해야 함",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "표준화된 데이터 전송 방법(ClipData) 없음",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = "다른 앱으로 데이터 전송 불가능",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
