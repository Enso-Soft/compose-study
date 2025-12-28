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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

/**
 * 올바른 코드 - dragAndDropSource/Target modifier 사용
 *
 * dragAndDropSource/Target을 사용하면:
 * 1. 드롭 영역 진입/이탈 자동 감지 (onEntered/onExited 콜백)
 * 2. ClipData로 표준화된 데이터 전송
 * 3. 다른 앱과의 드래그 앤 드롭 지원
 * 4. 시스템 드래그 섀도우 자동 제공
 * 5. 접근성 서비스 통합
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }
    val demos = listOf("기본", "피드백", "다중 타겟")

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
            0 -> BasicDragAndDropDemo()
            1 -> VisualFeedbackDemo()
            2 -> MultipleTargetsDemo()
        }
    }
}

/**
 * 데모 1: 기본 Drag & Drop
 */
@Composable
private fun BasicDragAndDropDemo() {
    var droppedItems by remember { mutableStateOf(listOf<String>()) }

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
                    text = "기본 Drag & Drop",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 드래그 소스들
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "드래그 소스",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        listOf("Apple", "Banana", "Cherry").forEach { item ->
                            // dragAndDropSource modifier 사용!
                            val primaryColor = MaterialTheme.colorScheme.primary
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(80.dp, 50.dp)
                                    .background(primaryColor)
                                    // drawDragDecoration으로 드래그 시 시각적 피드백 제공
                                    .dragAndDropSource(
                                        drawDragDecoration = {
                                            drawRoundRect(
                                                color = primaryColor.copy(alpha = 0.8f),
                                                cornerRadius = CornerRadius(8.dp.toPx())
                                            )
                                        }
                                    ) { _ ->
                                        // ClipData로 데이터 전송!
                                        DragAndDropTransferData(
                                            ClipData.newPlainText("fruit", item)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item, color = Color.White)
                            }
                        }
                    }

                    // 드롭 타겟
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "드롭 타겟",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // callback은 remember로 캐싱!
                        val callback = remember {
                            object : DragAndDropTarget {
                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                    // ClipData에서 데이터 추출
                                    val text = event.toAndroidDragEvent()
                                        .clipData
                                        .getItemAt(0)
                                        .text
                                        .toString()
                                    droppedItems = droppedItems + text
                                    return true // 드롭 처리 완료
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .dragAndDropTarget(
                                    // 모든 드래그 앤 드롭 이벤트 수신
                                    shouldStartDragAndDrop = { true },
                                    target = callback
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("바구니")
                                droppedItems.takeLast(3).forEach {
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
                Text("- dragAndDropSource { DragAndDropTransferData(...) }")
                Text("- ClipData.newPlainText()로 텍스트 데이터 생성")
                Text("- dragAndDropTarget(shouldStart, callback)")
                Text("- callback은 반드시 remember로 캐싱!")
                Text("- onDrop에서 true 반환 시 처리 완료")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { droppedItems = emptyList() }) {
            Text("리셋")
        }
    }
}

/**
 * 데모 2: 시각적 피드백 (onEntered/onExited)
 */
@Composable
private fun VisualFeedbackDemo() {
    var isHovering by remember { mutableStateOf(false) }
    var droppedText by remember { mutableStateOf<String?>(null) }
    var dragState by remember { mutableStateOf("대기 중") }

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
                    text = "시각적 피드백",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "상태: $dragState",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
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
                                    ClipData.newPlainText("greeting", "Hello!")
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("드래그!", color = Color.White)
                    }

                    // 드롭 타겟 - 모든 콜백 활용
                    val callback = remember {
                        object : DragAndDropTarget {
                            override fun onStarted(event: DragAndDropEvent) {
                                dragState = "드래그 시작됨"
                            }

                            override fun onEntered(event: DragAndDropEvent) {
                                isHovering = true
                                dragState = "영역 진입!"
                            }

                            override fun onMoved(event: DragAndDropEvent) {
                                // 영역 내 이동 중 (필요시 위치 추적 가능)
                            }

                            override fun onExited(event: DragAndDropEvent) {
                                isHovering = false
                                dragState = "영역 이탈"
                            }

                            override fun onDrop(event: DragAndDropEvent): Boolean {
                                isHovering = false
                                val text = event.toAndroidDragEvent()
                                    .clipData
                                    .getItemAt(0)
                                    .text
                                    .toString()
                                droppedText = text
                                dragState = "드롭 완료: $text"
                                return true
                            }

                            override fun onEnded(event: DragAndDropEvent) {
                                isHovering = false
                                if (dragState.startsWith("드롭")) {
                                    // 드롭 성공 시 상태 유지
                                } else {
                                    dragState = "드래그 종료"
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            // 호버링 상태에 따른 배경색 변경!
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
                                text = if (isHovering) "놓으세요!" else "여기로 드롭",
                                color = if (isHovering) Color.Green else Color.Gray
                            )
                            droppedText?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 콜백 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DragAndDropTarget 콜백",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- onStarted: 드래그 세션 시작 (타겟 수신 가능)")
                Text("- onEntered: 타겟 영역 진입")
                Text("- onMoved: 타겟 영역 내 이동")
                Text("- onExited: 타겟 영역 이탈")
                Text("- onDrop: 드롭 발생 (true 반환 시 처리)")
                Text("- onEnded: 드래그 세션 종료")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            droppedText = null
            dragState = "대기 중"
        }) {
            Text("리셋")
        }
    }
}

/**
 * 데모 3: 다중 드롭 타겟
 */
@Composable
private fun MultipleTargetsDemo() {
    val categories = listOf("과일", "채소", "육류")
    val itemsPerCategory = remember {
        mutableStateMapOf(
            "과일" to mutableListOf<String>(),
            "채소" to mutableListOf<String>(),
            "육류" to mutableListOf<String>()
        )
    }
    val hoveringCategory = remember { mutableStateMapOf<String, Boolean>() }

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
                    text = "다중 드롭 타겟",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 드래그 소스들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val primaryColor = MaterialTheme.colorScheme.primary
                    listOf(
                        "Apple" to "fruit",
                        "Carrot" to "vegetable",
                        "Beef" to "meat"
                    ).forEach { (name, type) ->
                        Box(
                            modifier = Modifier
                                .size(70.dp, 45.dp)
                                .background(primaryColor)
                                .dragAndDropSource(
                                    drawDragDecoration = {
                                        drawRoundRect(
                                            color = primaryColor.copy(alpha = 0.8f),
                                            cornerRadius = CornerRadius(4.dp.toPx())
                                        )
                                    }
                                ) { _ ->
                                    DragAndDropTransferData(
                                        // 타입 정보도 함께 전송
                                        ClipData.newPlainText(type, name)
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(name, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 드롭 타겟들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    categories.forEach { category ->
                        val isHovering = hoveringCategory[category] ?: false

                        val callback = remember(category) {
                            object : DragAndDropTarget {
                                override fun onEntered(event: DragAndDropEvent) {
                                    hoveringCategory[category] = true
                                }

                                override fun onExited(event: DragAndDropEvent) {
                                    hoveringCategory[category] = false
                                }

                                override fun onDrop(event: DragAndDropEvent): Boolean {
                                    hoveringCategory[category] = false
                                    val dragEvent = event.toAndroidDragEvent()
                                    val clipData = dragEvent.clipData
                                    val label = clipData.description.label.toString()
                                    val name = clipData.getItemAt(0).text.toString()

                                    // 카테고리 매칭 확인
                                    val validDrop = when (category) {
                                        "과일" -> label == "fruit"
                                        "채소" -> label == "vegetable"
                                        "육류" -> label == "meat"
                                        else -> false
                                    }

                                    if (validDrop) {
                                        itemsPerCategory[category]?.add(name)
                                    }

                                    return validDrop
                                }
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp, 120.dp)
                                    .background(
                                        if (isHovering)
                                            Color.Green.copy(alpha = 0.3f)
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .dragAndDropTarget(
                                        shouldStartDragAndDrop = { event ->
                                            // MIME 타입 필터링 가능
                                            event.mimeTypes().contains(
                                                ClipDescription.MIMETYPE_TEXT_PLAIN
                                            )
                                        },
                                        target = callback
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(category)
                                    itemsPerCategory[category]?.forEach {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.bodySmall,
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
                Text("- ClipData label로 타입 정보 전달")
                Text("- 각 타겟별 별도 callback 인스턴스")
                Text("- onDrop에서 조건부 처리 (true/false 반환)")
                Text("- shouldStartDragAndDrop으로 MIME 필터링")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            categories.forEach { itemsPerCategory[it]?.clear() }
        }) {
            Text("리셋")
        }
    }
}
