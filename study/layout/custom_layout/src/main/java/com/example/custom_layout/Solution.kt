package com.example.custom_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Solution: Custom Layout으로 원형 배치 구현!
 *
 * Layout composable을 사용하면:
 * 1. 모든 자식을 측정하고
 * 2. 원하는 좌표를 계산하여
 * 3. 자유롭게 배치할 수 있습니다
 */

@Composable
fun SolutionScreen() {
    var radius by remember { mutableFloatStateOf(80f) }
    var itemCount by remember { mutableIntStateOf(5) }
    var showAdvanced by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Custom Layout 핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Layout composable로 커스텀 레이아웃 생성\n" +
                            "2. measurables.map { it.measure() }로 자식 측정\n" +
                            "3. layout(width, height) { }에서 크기 결정 및 배치\n" +
                            "4. placeable.placeRelative(x, y)로 위치 지정",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 반경 조절 슬라이더
        Text(text = "반경: ${radius.roundToInt()}dp")
        Slider(
            value = radius,
            onValueChange = { radius = it },
            valueRange = 40f..120f,
            modifier = Modifier.fillMaxWidth()
        )

        // 아이템 개수 조절
        Text(text = "아이템 개수: $itemCount")
        Slider(
            value = itemCount.toFloat(),
            onValueChange = { itemCount = it.roundToInt() },
            valueRange = 3f..8f,
            steps = 4,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 원형 레이아웃 결과
        Text(
            text = "CircularLayout 결과:",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularLayout(
                radius = radius.dp,
                modifier = Modifier.size(260.dp)
            ) {
                val icons = listOf(
                    Icons.Default.Home to Color(0xFF4CAF50),
                    Icons.Default.Search to Color(0xFF2196F3),
                    Icons.Default.Favorite to Color(0xFFE91E63),
                    Icons.Default.Settings to Color(0xFF9C27B0),
                    Icons.Default.Person to Color(0xFFFF9800),
                    Icons.Default.Star to Color(0xFFFFEB3B),
                    Icons.Default.Notifications to Color(0xFF00BCD4),
                    Icons.Default.Email to Color(0xFF795548)
                )
                repeat(itemCount) { index ->
                    val (icon, color) = icons[index % icons.size]
                    CircleIcon(icon = icon, color = color)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 고급 예제 토글
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "고급 예제 보기",
                style = MaterialTheme.typography.titleSmall
            )
            Switch(
                checked = showAdvanced,
                onCheckedChange = { showAdvanced = it }
            )
        }

        if (showAdvanced) {
            Spacer(modifier = Modifier.height(16.dp))
            AdvancedExamples()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 설명
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CircularLayout 구현 코드",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Layout(content, modifier) { measurables, constraints ->
    val placeables = measurables.map {
        it.measure(constraints)
    }
    val angleStep = 2 * PI / placeables.size

    layout(constraints.maxWidth, constraints.maxHeight) {
        placeables.forEachIndexed { i, placeable ->
            val angle = angleStep * i - PI / 2
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            placeable.placeRelative(x, y)
        }
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 핵심: CircularLayout 구현
 * 자식들을 원형으로 배치하는 커스텀 레이아웃
 */
@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    radius: Dp = 100.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // 1. 모든 자식 측정
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        // 2. 중심점과 각도 계산
        val centerX = constraints.maxWidth / 2
        val centerY = constraints.maxHeight / 2
        val radiusPx = radius.roundToPx()
        val angleStep = 2 * PI / placeables.size

        // 3. 레이아웃 크기 결정 및 배치
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                // 각 아이템의 각도 (12시 방향에서 시작)
                val angle = angleStep * index - PI / 2

                // 원형 좌표 계산
                val x = (centerX + radiusPx * cos(angle) - placeable.width / 2).toInt()
                val y = (centerY + radiusPx * sin(angle) - placeable.height / 2).toInt()

                // RTL 지원을 위해 placeRelative 사용
                placeable.placeRelative(x, y)
            }
        }
    }
}

/**
 * layout modifier 예제: 단일 요소의 측정/배치 커스터마이징
 * 커스텀 수직 오프셋 적용
 */
fun Modifier.verticalOffset(offset: Dp) = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val offsetPx = offset.roundToPx()

        layout(placeable.width, placeable.height + offsetPx.coerceAtLeast(0)) {
            placeable.placeRelative(0, offsetPx.coerceAtLeast(0))
        }
    }
)

@Composable
private fun AdvancedExamples() {
    Column {
        // 예제 0: layout modifier (단일 요소 커스터마이징)
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "layout modifier (단일 요소)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "수직 오프셋 40dp 적용:",
                    style = MaterialTheme.typography.bodySmall
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.shapes.small
                        )
                        .padding(8.dp)
                ) {
                    Text("일반 텍스트")
                    Text(
                        text = "오프셋 적용",
                        modifier = Modifier.verticalOffset(offset = 40.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 예제 1: StaggeredColumn
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "StaggeredColumn (계단식 배치)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                StaggeredColumn(indent = 24.dp) {
                    Text("첫 번째 아이템")
                    Text("두 번째 아이템")
                    Text("세 번째 아이템")
                    Text("네 번째 아이템")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 예제 2: EqualWidthColumn (SubcomposeLayout)
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "EqualWidthColumn (SubcomposeLayout)",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "가장 넓은 자식에 맞춰 모든 자식 너비 통일:",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                EqualWidthColumn {
                    Button(onClick = {}) { Text("Short") }
                    Button(onClick = {}) { Text("Medium Button") }
                    Button(onClick = {}) { Text("Very Long Button Text") }
                }
            }
        }
    }
}

/**
 * StaggeredColumn: 각 아이템을 계단식으로 들여쓰기
 */
@Composable
fun StaggeredColumn(
    modifier: Modifier = Modifier,
    indent: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val indentPx = indent.roundToPx()
        val placeables = measurables.map { it.measure(constraints) }

        val height = placeables.sumOf { it.height }
        val width = placeables.mapIndexed { index, placeable ->
            placeable.width + index * indentPx
        }.maxOrNull() ?: 0

        layout(width, height) {
            var yPosition = 0
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = index * indentPx,
                    y = yPosition
                )
                yPosition += placeable.height
            }
        }
    }
}

/**
 * EqualWidthColumn: SubcomposeLayout을 사용하여
 * 가장 넓은 자식에 맞춰 모든 자식의 너비를 통일
 */
@Composable
fun EqualWidthColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        // 1단계: 먼저 자연스러운 크기로 측정하여 최대 너비 찾기
        val measureables = subcompose("measure", content)
        val maxWidth = measureables.maxOfOrNull { measurable ->
            measurable.measure(constraints).width
        } ?: 0

        // 2단계: 모든 자식을 동일한 너비로 재측정
        val placeables = subcompose("content", content).map { measurable ->
            measurable.measure(
                constraints.copy(
                    minWidth = maxWidth,
                    maxWidth = maxWidth
                )
            )
        }

        // 3단계: 배치
        val height = placeables.sumOf { it.height }
        layout(maxWidth, height) {
            var yPosition = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(0, yPosition)
                yPosition += placeable.height
            }
        }
    }
}

@Composable
private fun CircleIcon(
    icon: ImageVector,
    color: Color
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
