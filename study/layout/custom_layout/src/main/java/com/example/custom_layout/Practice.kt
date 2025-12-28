package com.example.custom_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: StaggeredColumn (계단식 배치)
 *
 * 목표: Layout composable 기본 사용법 익히기
 * 각 아이템을 index * indent 만큼 오른쪽으로 들여쓰기
 */
@Composable
fun Practice1_StaggeredColumn() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: StaggeredColumn",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "각 아이템을 index * indent 만큼 오른쪽으로 들여쓰기하세요.\n\n" +
                            "힌트:\n" +
                            "1. measurables.mapIndexed 사용\n" +
                            "2. x 좌표: index * indentPx\n" +
                            "3. y 좌표: 이전 아이템들의 높이 합",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 결과 표시 영역
        Text("결과:", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .padding(16.dp)
        ) {
            MyStaggeredColumn(indent = 32.dp) {
                for (i in 1..5) {
                    Card(
                        modifier = Modifier.padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = "아이템 $i",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 정답 확인
        var showAnswer by remember { mutableStateOf(false) }
        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = """
Layout(content, modifier) { measurables, constraints ->
    val indentPx = indent.roundToPx()
    val placeables = measurables.map {
        it.measure(constraints)
    }

    val height = placeables.sumOf { it.height }
    val width = placeables.mapIndexed { i, p ->
        p.width + i * indentPx
    }.maxOrNull() ?: 0

    layout(width, height) {
        var y = 0
        placeables.forEachIndexed { i, p ->
            p.placeRelative(i * indentPx, y)
            y += p.height
        }
    }
}
                    """.trimIndent(),
                    modifier = Modifier.padding(16.dp),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun MyStaggeredColumn(
    modifier: Modifier = Modifier,
    indent: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    // TODO: Layout composable을 사용하여 구현하세요
    // 각 아이템을 index * indent 만큼 오른쪽으로 배치
    //
    // 힌트:
    // 1. Layout(content, modifier) { measurables, constraints -> ... }
    // 2. measurables.map { it.measure(constraints) }로 자식 측정
    // 3. layout(width, height) { ... }에서 placeRelative 사용

    // 아래 Column은 임시 구현입니다. Layout으로 교체하세요!
    Column(modifier = modifier) {
        content()
    }
}

/**
 * 연습 문제 2: ReverseFlowRow (역방향 Flow)
 *
 * 목표: 제약조건 활용 및 줄바꿈 로직 이해
 * 오른쪽에서 왼쪽으로 배치, 공간이 부족하면 다음 줄로
 */
@Composable
fun Practice2_ReverseFlowRow() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: ReverseFlowRow",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "오른쪽에서 왼쪽으로 배치하고, 공간이 부족하면 다음 줄로.\n\n" +
                            "힌트:\n" +
                            "1. currentX = maxWidth에서 시작\n" +
                            "2. 각 아이템: currentX -= placeable.width\n" +
                            "3. currentX < 0이면 다음 줄로",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 결과 표시 영역
        Text("결과:", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .padding(16.dp)
        ) {
            MyReverseFlowRow {
                listOf("Kotlin", "Compose", "Android", "Jetpack", "UI", "Layout", "Custom").forEach { tag ->
                    AssistChip(
                        onClick = {},
                        label = { Text(tag) },
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 정답 확인
        var showAnswer by remember { mutableStateOf(false) }
        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = """
Layout(content, modifier) { measurables, constraints ->
    val placeables = measurables.map {
        it.measure(constraints)
    }

    val rows = mutableListOf<List<Placeable>>()
    var currentRow = mutableListOf<Placeable>()
    var currentWidth = 0

    placeables.forEach { p ->
        if (currentWidth + p.width > constraints.maxWidth) {
            rows.add(currentRow.toList())
            currentRow = mutableListOf(p)
            currentWidth = p.width
        } else {
            currentRow.add(p)
            currentWidth += p.width
        }
    }
    if (currentRow.isNotEmpty()) rows.add(currentRow)

    val height = rows.sumOf { row ->
        row.maxOf { it.height }
    }

    layout(constraints.maxWidth, height) {
        var y = 0
        rows.forEach { row ->
            var x = constraints.maxWidth
            row.forEach { p ->
                x -= p.width
                p.placeRelative(x, y)
            }
            y += row.maxOf { it.height }
        }
    }
}
                    """.trimIndent(),
                    modifier = Modifier.padding(16.dp),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun MyReverseFlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // TODO: Layout composable을 사용하여 구현하세요
    // 오른쪽에서 왼쪽으로 배치, 공간 부족시 다음 줄
    //
    // 힌트:
    // 1. currentX = constraints.maxWidth에서 시작
    // 2. 각 아이템: currentX -= placeable.width
    // 3. currentX < 0이면 다음 줄로 (currentY += rowHeight)
    // 4. 각 아이템 위치를 저장하고 마지막에 placeRelative

    // 아래 Row는 임시 구현입니다. Layout으로 교체하세요!
    androidx.compose.foundation.layout.FlowRow(modifier = modifier) {
        content()
    }
}

/**
 * 연습 문제 3: EqualWidthColumn (SubcomposeLayout)
 *
 * 목표: SubcomposeLayout 사용법 이해
 * 가장 넓은 자식에 맞춰 모든 자식의 너비를 통일
 */
@Composable
fun Practice3_EqualWidthColumn() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: EqualWidthColumn (SubcomposeLayout)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "가장 넓은 자식에 맞춰 모든 자식 너비 통일.\n\n" +
                            "힌트:\n" +
                            "1. subcompose()로 먼저 측정\n" +
                            "2. maxWidth 찾기\n" +
                            "3. constraints.copy(minWidth=maxWidth)로 재측정\n\n" +
                            "주의: 일반 Layout은 자식을 두 번 측정할 수 없습니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 비교: 일반 Column vs EqualWidthColumn
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("일반 Column", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Button(onClick = {}) { Text("Short") }
                    Button(onClick = {}) { Text("Medium") }
                    Button(onClick = {}) { Text("Very Long") }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("EqualWidthColumn", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                MyEqualWidthColumn {
                    Button(onClick = {}) { Text("Short") }
                    Button(onClick = {}) { Text("Medium") }
                    Button(onClick = {}) { Text("Very Long") }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 정답 확인
        var showAnswer by remember { mutableStateOf(false) }
        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = """
SubcomposeLayout(modifier) { constraints ->
    // 1단계: 먼저 자연 크기로 측정
    val measureables = subcompose("measure", content)
    val maxWidth = measureables.maxOfOrNull {
        it.measure(constraints).width
    } ?: 0

    // 2단계: 동일 너비로 재측정
    val placeables = subcompose("content", content).map {
        it.measure(constraints.copy(
            minWidth = maxWidth,
            maxWidth = maxWidth
        ))
    }

    // 3단계: 배치
    val height = placeables.sumOf { it.height }
    layout(maxWidth, height) {
        var y = 0
        placeables.forEach { p ->
            p.placeRelative(0, y)
            y += p.height
        }
    }
}
                    """.trimIndent(),
                    modifier = Modifier.padding(16.dp),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SubcomposeLayout 주의사항
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SubcomposeLayout 주의사항",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Intrinsic size 지원 안됨\n" +
                            "2. Composition과 Measurement가 결합되어 성능 비용\n" +
                            "3. 꼭 필요한 경우에만 사용 (LazyColumn, TabRow 등)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Composable
private fun MyEqualWidthColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // TODO: SubcomposeLayout을 사용하여 구현하세요
    // 가장 넓은 자식에 맞춰 모든 자식의 너비를 통일
    //
    // 힌트:
    // 1. SubcomposeLayout(modifier) { constraints -> ... }
    // 2. subcompose("measure", content)로 먼저 측정하여 maxWidth 찾기
    // 3. subcompose("content", content)로 재측정 (constraints.copy 사용)
    // 4. layout(maxWidth, height) { ... }에서 배치

    // 아래 Column은 임시 구현입니다. SubcomposeLayout으로 교체하세요!
    Column(modifier = modifier) {
        content()
    }
}
