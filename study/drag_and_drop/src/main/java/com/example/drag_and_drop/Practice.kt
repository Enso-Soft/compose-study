package com.example.drag_and_drop

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 Drag & Drop 구현
 *
 * 목표: 이모지 카드를 드래그하여 상자에 넣기
 *
 * TODO 구현 사항:
 * 1. 각 이모지 카드에 dragAndDropSource modifier 추가
 * 2. 상자에 dragAndDropTarget modifier 추가
 * 3. onDrop에서 이모지 데이터 추출 및 상태 업데이트
 */
@Composable
fun Practice1_BasicDragAndDropScreen() {
    val emojis = listOf("Apple", "Banana", "Orange", "Grape")
    var collectedItems by remember { mutableStateOf(listOf<String>()) }

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
                    text = "연습 1: 기본 Drag & Drop",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "과일 카드를 바구니로 드래그하세요",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 드래그 소스 - 이모지 카드들
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("과일들", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        val primaryColor = MaterialTheme.colorScheme.primary
                        emojis.forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(80.dp, 50.dp)
                                    .background(primaryColor)
                                    // TODO 1: dragAndDropSource modifier 추가
                                    // 힌트: drawDragDecoration으로 드래그 시각화, trailing lambda로 데이터 전송
                                    .dragAndDropSource(
                                        drawDragDecoration = {
                                            drawRoundRect(
                                                color = primaryColor.copy(alpha = 0.8f),
                                                cornerRadius = CornerRadius(8.dp.toPx())
                                            )
                                        }
                                    ) { _ ->
                                        DragAndDropTransferData(
                                            ClipData.newPlainText("fruit", emoji)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, color = Color.White)
                            }
                        }
                    }

                    // 드롭 타겟 - 바구니
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("바구니", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        // TODO 2: callback 정의 (remember 사용!)
                        // 힌트: object : DragAndDropTarget { override fun onDrop(...) }
                        val callback = remember {
                            object : DragAndDropTarget {
                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                    // TODO 3: ClipData에서 텍스트 추출
                                    // 힌트: event.toAndroidDragEvent().clipData.getItemAt(0).text
                                    val text = event.toAndroidDragEvent()
                                        .clipData
                                        .getItemAt(0)
                                        .text
                                        .toString()
                                    collectedItems = collectedItems + text
                                    return true
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                // TODO 4: dragAndDropTarget modifier 추가
                                // 힌트: dragAndDropTarget(shouldStartDragAndDrop = { true }, target = callback)
                                .dragAndDropTarget(
                                    shouldStartDragAndDrop = { true },
                                    target = callback
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (collectedItems.isEmpty()) {
                                    Text("여기로 드래그")
                                } else {
                                    collectedItems.takeLast(4).forEach {
                                        Text(
                                            text = it,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. dragAndDropSource { DragAndDropTransferData(...) }")
                Text("2. ClipData.newPlainText(label, text)")
                Text("3. dragAndDropTarget(shouldStartDragAndDrop, target)")
                Text("4. callback은 remember로 감싸기!")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { collectedItems = emptyList() }) {
            Text("리셋")
        }
    }
}

/**
 * 연습 문제 2: 시각적 피드백 추가
 *
 * 목표: 드래그 진입 시 타겟 영역 하이라이트
 *
 * TODO 구현 사항:
 * 1. isHovering 상태 추가
 * 2. onEntered/onExited 콜백에서 상태 변경
 * 3. 배경색을 호버 상태에 따라 변경
 */
@Composable
fun Practice2_VisualFeedbackScreen() {
    // TODO 1: isHovering 상태 추가
    var isHovering by remember { mutableStateOf(false) }
    var droppedItem by remember { mutableStateOf<String?>(null) }

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
                    text = "연습 2: 시각적 피드백",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "드래그 진입 시 초록색으로 변합니다",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 드래그 소스
                    val primaryColor = MaterialTheme.colorScheme.primary
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(primaryColor)
                            .dragAndDropSource(
                                drawDragDecoration = {
                                    drawRoundRect(
                                        color = primaryColor.copy(alpha = 0.8f),
                                        cornerRadius = CornerRadius(8.dp.toPx())
                                    )
                                }
                            ) { _ ->
                                DragAndDropTransferData(
                                    ClipData.newPlainText("item", "Special Item")
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("드래그!", color = Color.White)
                    }

                    // 드롭 타겟 with 시각적 피드백
                    // TODO 2: onEntered/onExited 콜백 구현
                    val callback = remember {
                        object : DragAndDropTarget {
                            override fun onEntered(event: DragAndDropEvent) {
                                // TODO: isHovering = true
                                isHovering = true
                            }

                            override fun onExited(event: DragAndDropEvent) {
                                // TODO: isHovering = false
                                isHovering = false
                            }

                            override fun onDrop(event: DragAndDropEvent): Boolean {
                                isHovering = false
                                val text = event.toAndroidDragEvent()
                                    .clipData
                                    .getItemAt(0)
                                    .text
                                    .toString()
                                droppedItem = text
                                return true
                            }

                            override fun onEnded(event: DragAndDropEvent) {
                                isHovering = false
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            // TODO 3: 호버 상태에 따른 배경색 변경
                            // 힌트: if (isHovering) Color.Green.copy(alpha = 0.3f) else ...
                            .background(
                                if (isHovering)
                                    Color.Green.copy(alpha = 0.3f)
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { true },
                                target = callback
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isHovering) "놓으세요!" else "드롭 영역",
                                color = if (isHovering) Color.Green else Color.Gray
                            )
                            droppedItem?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
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
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. var isHovering by remember { mutableStateOf(false) }")
                Text("2. onEntered { isHovering = true }")
                Text("3. onExited { isHovering = false }")
                Text("4. background(if (isHovering) Color.Green... else ...)")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { droppedItem = null }) {
            Text("리셋")
        }
    }
}

/**
 * 연습 문제 3: 칸반 보드 만들기
 *
 * 목표: 태스크를 열 간에 이동
 *
 * TODO 구현 사항:
 * 1. 각 태스크에 dragAndDropSource 추가
 * 2. 각 열에 dragAndDropTarget 추가
 * 3. onDrop에서 태스크 이동 로직 구현
 */
@Composable
fun Practice3_KanbanBoardScreen() {
    data class Task(val id: String, val title: String)

    val columns = listOf("Todo", "InProgress", "Done")

    // 각 열의 태스크들
    val tasksByColumn = remember {
        mutableStateMapOf(
            "Todo" to mutableStateListOf(
                Task("1", "Task A"),
                Task("2", "Task B"),
                Task("3", "Task C")
            ),
            "InProgress" to mutableStateListOf<Task>(),
            "Done" to mutableStateListOf<Task>()
        )
    }

    val hoveringColumn = remember { mutableStateMapOf<String, Boolean>() }

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
                    text = "연습 3: 칸반 보드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "태스크를 다른 열로 드래그하세요",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    columns.forEach { column ->
                        val isHovering = hoveringColumn[column] ?: false
                        val tasks = tasksByColumn[column] ?: mutableStateListOf()

                        // TODO: 각 열에 대한 callback 정의
                        val callback = remember(column) {
                            object : DragAndDropTarget {
                                override fun onEntered(event: DragAndDropEvent) {
                                    hoveringColumn[column] = true
                                }

                                override fun onExited(event: DragAndDropEvent) {
                                    hoveringColumn[column] = false
                                }

                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                    hoveringColumn[column] = false

                                    // TODO: ClipData에서 taskId 추출
                                    val clipData = event.toAndroidDragEvent().clipData
                                    val sourceColumn = clipData.description.label.toString()
                                    val taskId = clipData.getItemAt(0).text.toString()

                                    // 같은 열이면 무시
                                    if (sourceColumn == column) return false

                                    // TODO: 소스 열에서 태스크 찾아서 제거
                                    val sourceTasks = tasksByColumn[sourceColumn]
                                    val task = sourceTasks?.find { it.id == taskId }

                                    if (task != null) {
                                        sourceTasks?.remove(task)
                                        // TODO: 대상 열에 태스크 추가
                                        tasksByColumn[column]?.add(task)
                                        return true
                                    }

                                    return false
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.width(100.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = column,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(
                                        if (isHovering)
                                            Color.Green.copy(alpha = 0.2f)
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .dragAndDropTarget(
                                        shouldStartDragAndDrop = { event ->
                                            event.mimeTypes().contains(
                                                ClipDescription.MIMETYPE_TEXT_PLAIN
                                            )
                                        },
                                        target = callback
                                    )
                            ) {
                                Column(
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    val taskColor = MaterialTheme.colorScheme.primary
                                    tasks.forEach { task ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(2.dp)
                                                .background(taskColor)
                                                // TODO: dragAndDropSource 추가
                                                // 힌트: drawDragDecoration으로 드래그 시각화
                                                .dragAndDropSource(
                                                    drawDragDecoration = {
                                                        drawRoundRect(
                                                            color = taskColor.copy(alpha = 0.8f),
                                                            cornerRadius = CornerRadius(4.dp.toPx())
                                                        )
                                                    }
                                                ) { _ ->
                                                    DragAndDropTransferData(
                                                        ClipData.newPlainText(column, task.id)
                                                    )
                                                }
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = task.title,
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. ClipData label에 소스 열 이름 저장")
                Text("2. ClipData text에 task ID 저장")
                Text("3. onDrop에서 소스 열에서 제거, 대상 열에 추가")
                Text("4. remember(column)으로 열별 callback 생성")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            tasksByColumn["Todo"]?.clear()
            tasksByColumn["Todo"]?.addAll(
                listOf(
                    Task("1", "Task A"),
                    Task("2", "Task B"),
                    Task("3", "Task C")
                )
            )
            tasksByColumn["InProgress"]?.clear()
            tasksByColumn["Done"]?.clear()
        }) {
            Text("리셋")
        }
    }
}
