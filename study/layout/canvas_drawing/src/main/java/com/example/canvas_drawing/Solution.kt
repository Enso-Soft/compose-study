package com.example.canvas_drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * 올바른 코드 - Canvas를 사용한 그래픽 구현
 *
 * Canvas를 사용하면:
 * 1. 단일 Canvas에서 모든 그리기 처리 - 효율적
 * 2. Path로 점들을 연결한 부드러운 선
 * 3. 그라디언트, 스타일 자유롭게 적용
 * 4. 터치 이벤트와 자연스러운 통합
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }
    val demos = listOf("기본 도형", "그라디언트", "드로잉 패드")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )

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
            0 -> BasicShapesDemo()
            1 -> GradientDemo()
            2 -> DrawingPadDemo()
        }
    }
}

/**
 * 데모 1: 기본 도형 그리기
 *
 * drawLine, drawCircle, drawRect, drawArc, drawPath 사용법
 */
@Composable
private fun BasicShapesDemo() {
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
                    text = "기본 도형 그리기",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Canvas로 기본 도형들 그리기
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color.White)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // 1. drawLine - 선 그리기
                    drawLine(
                        color = Color.Red,
                        start = Offset(20f, 20f),
                        end = Offset(canvasWidth / 3 - 20f, 80f),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )

                    // 2. drawCircle - 원 그리기 (채움)
                    drawCircle(
                        color = Color.Blue,
                        radius = 40f,
                        center = Offset(canvasWidth / 2, 60f)
                    )

                    // 3. drawCircle - 원 그리기 (테두리)
                    drawCircle(
                        color = Color.Green,
                        radius = 40f,
                        center = Offset(canvasWidth * 5 / 6, 60f),
                        style = Stroke(width = 4f)
                    )

                    // 4. drawRect - 사각형 그리기
                    drawRect(
                        color = Color.Magenta,
                        topLeft = Offset(20f, 120f),
                        size = Size(80f, 60f)
                    )

                    // 5. drawRect - 사각형 (테두리)
                    drawRect(
                        color = Color.Cyan,
                        topLeft = Offset(canvasWidth / 3, 120f),
                        size = Size(80f, 60f),
                        style = Stroke(width = 4f)
                    )

                    // 6. drawArc - 호 그리기
                    drawArc(
                        color = Color(0xFFFF9800),
                        startAngle = 0f,
                        sweepAngle = 270f,
                        useCenter = true,  // 중심 연결 (부채꼴)
                        topLeft = Offset(canvasWidth * 2 / 3, 110f),
                        size = Size(80f, 80f)
                    )

                    // 7. drawPath - 커스텀 삼각형
                    val trianglePath = Path().apply {
                        moveTo(60f, canvasHeight - 20f)  // 하단 좌측
                        lineTo(140f, canvasHeight - 20f) // 하단 우측
                        lineTo(100f, canvasHeight - 80f) // 상단 중앙
                        close()
                    }
                    drawPath(
                        path = trianglePath,
                        color = Color(0xFF4CAF50),
                        style = Fill
                    )

                    // 8. drawPath - 별 모양
                    val starPath = createStarPath(
                        center = Offset(canvasWidth / 2, canvasHeight - 50f),
                        outerRadius = 40f,
                        innerRadius = 20f,
                        points = 5
                    )
                    drawPath(
                        path = starPath,
                        color = Color(0xFFFFD700)
                    )

                    // 9. 점선
                    drawLine(
                        color = Color.Gray,
                        start = Offset(canvasWidth * 2 / 3, canvasHeight - 20f),
                        end = Offset(canvasWidth - 20f, canvasHeight - 20f),
                        strokeWidth = 4f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "첫 번째 줄: 선, 원(채움), 원(테두리)",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "두 번째 줄: 사각형(채움), 사각형(테두리), 호(부채꼴)",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "세 번째 줄: 삼각형(Path), 별(Path), 점선",
                    style = MaterialTheme.typography.bodySmall
                )
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
                Text("- drawLine: start, end Offset으로 선 그리기")
                Text("- drawCircle: center, radius로 원 그리기")
                Text("- drawRect: topLeft, size로 사각형 그리기")
                Text("- drawArc: startAngle, sweepAngle로 호 그리기")
                Text("- drawPath: Path 객체로 커스텀 도형 그리기")
                Text("- style: Fill(채움) vs Stroke(테두리)")
                Text("- PathEffect: 점선, 대시 패턴 적용")
            }
        }
    }
}

/**
 * 데모 2: 그라디언트 적용
 *
 * Brush를 사용한 다양한 그라디언트 효과
 */
@Composable
private fun GradientDemo() {
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
                    text = "그라디언트 효과",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .background(Color.White)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val rectWidth = (canvasWidth - 60f) / 2
                    val rectHeight = (canvasHeight - 80f) / 3

                    // 1. 수평 그라디언트
                    val horizontalGradient = Brush.horizontalGradient(
                        colors = listOf(Color.Red, Color.Yellow, Color.Green)
                    )
                    drawRect(
                        brush = horizontalGradient,
                        topLeft = Offset(20f, 20f),
                        size = Size(rectWidth, rectHeight)
                    )

                    // 2. 수직 그라디언트
                    val verticalGradient = Brush.verticalGradient(
                        colors = listOf(Color.Blue, Color.Cyan, Color.White)
                    )
                    drawRect(
                        brush = verticalGradient,
                        topLeft = Offset(rectWidth + 40f, 20f),
                        size = Size(rectWidth, rectHeight)
                    )

                    // 3. 선형 그라디언트 (대각선)
                    val linearGradient = Brush.linearGradient(
                        colors = listOf(Color.Magenta, Color.Blue),
                        start = Offset(20f, rectHeight + 40f),
                        end = Offset(rectWidth + 20f, rectHeight * 2 + 40f)
                    )
                    drawRect(
                        brush = linearGradient,
                        topLeft = Offset(20f, rectHeight + 40f),
                        size = Size(rectWidth, rectHeight)
                    )

                    // 4. 방사형 그라디언트
                    val radialGradient = Brush.radialGradient(
                        colors = listOf(Color.Yellow, Color.Red, Color.Black),
                        center = Offset(
                            rectWidth + 40f + rectWidth / 2,
                            rectHeight + 40f + rectHeight / 2
                        ),
                        radius = rectHeight / 2
                    )
                    drawRect(
                        brush = radialGradient,
                        topLeft = Offset(rectWidth + 40f, rectHeight + 40f),
                        size = Size(rectWidth, rectHeight)
                    )

                    // 5. 스윕 그라디언트 (원형)
                    val sweepGradient = Brush.sweepGradient(
                        colors = listOf(
                            Color.Red, Color.Yellow, Color.Green,
                            Color.Cyan, Color.Blue, Color.Magenta, Color.Red
                        ),
                        center = Offset(20f + rectWidth / 2, rectHeight * 2 + 60f + rectHeight / 2)
                    )
                    drawCircle(
                        brush = sweepGradient,
                        radius = rectHeight / 2 - 10f,
                        center = Offset(20f + rectWidth / 2, rectHeight * 2 + 60f + rectHeight / 2)
                    )

                    // 6. 그라디언트가 적용된 호 (프로그레스 바)
                    val progressGradient = Brush.sweepGradient(
                        colors = listOf(Color(0xFF4CAF50), Color(0xFFCDDC39), Color(0xFFFFEB3B)),
                        center = Offset(
                            rectWidth + 40f + rectWidth / 2,
                            rectHeight * 2 + 60f + rectHeight / 2
                        )
                    )
                    // 배경 원
                    drawArc(
                        color = Color.LightGray,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(rectWidth + 50f, rectHeight * 2 + 70f),
                        size = Size(rectWidth - 20f, rectHeight - 20f),
                        style = Stroke(width = 16f)
                    )
                    // 프로그레스 호
                    drawArc(
                        brush = progressGradient,
                        startAngle = -90f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(rectWidth + 50f, rectHeight * 2 + 70f),
                        size = Size(rectWidth - 20f, rectHeight - 20f),
                        style = Stroke(width = 16f, cap = StrokeCap.Round)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("1행: 수평 그라디언트, 수직 그라디언트", style = MaterialTheme.typography.bodySmall)
                Text("2행: 선형 그라디언트(대각), 방사형 그라디언트", style = MaterialTheme.typography.bodySmall)
                Text("3행: 스윕 그라디언트(원), 프로그레스 바", style = MaterialTheme.typography.bodySmall)
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
                    text = "Brush 종류",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- Brush.horizontalGradient: 좌 -> 우")
                Text("- Brush.verticalGradient: 상 -> 하")
                Text("- Brush.linearGradient: 시작점 -> 끝점")
                Text("- Brush.radialGradient: 중심 -> 외곽")
                Text("- Brush.sweepGradient: 원형 회전")
            }
        }
    }
}

/**
 * 데모 3: 터치 드로잉 패드
 *
 * Canvas + pointerInput으로 인터랙티브 드로잉 구현
 */
@Composable
private fun DrawingPadDemo() {
    // 완료된 경로들
    var paths by remember { mutableStateOf<List<DrawingPath>>(emptyList()) }
    // 현재 그리는 중인 경로
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }
    // 현재 색상
    var selectedColor by remember { mutableStateOf(Color.Black) }
    // 선 굵기
    var strokeWidth by remember { mutableFloatStateOf(5f) }

    val colors = listOf(
        Color.Black, Color.Red, Color.Blue, Color.Green,
        Color.Magenta, Color.Cyan, Color(0xFFFF9800)
    )

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
                    text = "터치 드로잉 패드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 드로잉 Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.White)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    // 새로운 경로 시작
                                    currentPath = listOf(offset)
                                },
                                onDrag = { change, _ ->
                                    // 경로에 점 추가
                                    currentPath = currentPath + change.position
                                },
                                onDragEnd = {
                                    // 경로 완료 후 저장
                                    if (currentPath.size > 1) {
                                        paths = paths + DrawingPath(
                                            points = currentPath,
                                            color = selectedColor,
                                            strokeWidth = strokeWidth
                                        )
                                    }
                                    currentPath = emptyList()
                                }
                            )
                        }
                ) {
                    // 저장된 경로들 그리기
                    paths.forEach { drawingPath ->
                        if (drawingPath.points.size > 1) {
                            val path = Path().apply {
                                moveTo(drawingPath.points.first().x, drawingPath.points.first().y)
                                drawingPath.points.drop(1).forEach { point ->
                                    lineTo(point.x, point.y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = drawingPath.color,
                                style = Stroke(
                                    width = drawingPath.strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }

                    // 현재 그리는 중인 경로
                    if (currentPath.size > 1) {
                        val path = Path().apply {
                            moveTo(currentPath.first().x, currentPath.first().y)
                            currentPath.drop(1).forEach { point ->
                                lineTo(point.x, point.y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = selectedColor,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }

                    // 빈 상태 안내
                    if (paths.isEmpty() && currentPath.isEmpty()) {
                        drawContext.canvas.nativeCanvas.apply {
                            // 힌트 텍스트는 생략 (Canvas 내에서 텍스트 그리기는 복잡)
                        }
                    }
                }

                if (paths.isEmpty() && currentPath.isEmpty()) {
                    Text(
                        text = "화면을 드래그하여 그림을 그려보세요!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 색상 선택
                Text("색상 선택:", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(if (color == selectedColor) 36.dp else 30.dp)
                                .background(
                                    color = color,
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                                .then(
                                    if (color == selectedColor) {
                                        Modifier.padding(2.dp)
                                    } else Modifier
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 굵기 조절
                Text("선 굵기: ${strokeWidth.toInt()}px", style = MaterialTheme.typography.labelMedium)
                Slider(
                    value = strokeWidth,
                    onValueChange = { strokeWidth = it },
                    valueRange = 2f..20f,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 지우기 버튼
                Button(
                    onClick = { paths = emptyList() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("전체 지우기")
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
                Text("- Canvas + pointerInput 결합")
                Text("- detectDragGestures로 터치 경로 추적")
                Text("- onDragStart: 새 경로 시작")
                Text("- onDrag: 경로에 점 추가 (change.position)")
                Text("- onDragEnd: 경로 완료 후 저장")
                Text("- Path로 점들을 부드러운 선으로 연결")
                Text("- StrokeCap.Round, StrokeJoin.Round로 매끄러운 선")
            }
        }
    }
}

/**
 * 드로잉 경로 데이터 클래스
 */
private data class DrawingPath(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

/**
 * 별 모양 Path 생성 헬퍼 함수
 */
private fun createStarPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int
): Path {
    val path = Path()
    val angleStep = Math.PI / points

    for (i in 0 until points * 2) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - Math.PI / 2  // 시작점을 12시 방향으로

        val x = center.x + (radius * cos(angle)).toFloat()
        val y = center.y + (radius * sin(angle)).toFloat()

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    path.close()
    return path
}
