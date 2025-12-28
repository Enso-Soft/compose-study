package com.example.custom_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Practice.kt - Custom Modifier 연습 문제
 *
 * 3가지 연습 문제를 통해 Custom Modifier를 직접 구현해봅니다.
 */

// ============================================================
// 연습 1: 조건부 Modifier 확장 함수 (기초)
// ============================================================

/**
 * [연습 1] Modifier.applyIf() 확장 함수 구현하기
 *
 * 목표: 조건이 true일 때만 modifier를 적용하는 확장 함수 만들기
 *
 * TODO: 아래 함수를 완성하세요
 *
 * 힌트:
 * - inline 키워드로 람다 오버헤드 제거
 * - condition이 true면 this.block() 반환
 * - condition이 false면 this 그대로 반환
 */
inline fun Modifier.applyIf(
    condition: Boolean,
    block: Modifier.() -> Modifier
): Modifier {
    // TODO: 구현하세요

    // 정답 (주석 해제하세요):
    // return if (condition) this.block() else this

    return this // 임시 반환값 - 정답으로 교체하세요
}

@Composable
fun Practice1_ConditionalModifier() {
    var showBorder by remember { mutableStateOf(false) }
    var showBackground by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 연습 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: applyIf 확장 함수 구현",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "조건부로 Modifier를 적용하는 확장 함수를 구현하세요.\n\n" +
                            "힌트: inline fun Modifier.applyIf(\n" +
                            "    condition: Boolean,\n" +
                            "    block: Modifier.() -> Modifier\n" +
                            "): Modifier",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 컨트롤 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { showBorder = !showBorder }) {
                Text(if (showBorder) "Hide Border" else "Show Border")
            }
            Button(onClick = { showBackground = !showBackground }) {
                Text(if (showBackground) "Hide BG" else "Show BG")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .applyIf(showBackground) {
                    background(Color.Yellow.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                }
                .applyIf(showBorder) {
                    background(Color.Transparent, RoundedCornerShape(16.dp))
                        .then(
                            Modifier.background(
                                brush = Brush.linearGradient(listOf(Color.Red, Color.Blue)),
                                shape = RoundedCornerShape(16.dp)
                            )
                        )
                }
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("테스트 박스", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Border: ${if (showBorder) "ON" else "OFF"} | " +
                            "Background: ${if (showBackground) "ON" else "OFF"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        inline fun Modifier.applyIf(
                            condition: Boolean,
                            block: Modifier.() -> Modifier
                        ): Modifier {
                            return if (condition) this.block() else this
                        }

                        * inline: 람다 오버헤드 제거
                        * this.block(): Modifier에 block 적용
                        * this: 조건이 false면 원본 그대로
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ============================================================
// 연습 2: DrawModifierNode로 그라데이션 원 (중급)
// ============================================================

/**
 * [연습 2] DrawModifierNode로 그라데이션 원 Modifier 구현하기
 *
 * 목표: 콘텐츠 뒤에 그라데이션 원을 그리는 Modifier 만들기
 *
 * TODO:
 * 1. GradientCircleNode 클래스의 draw() 함수 구현
 * 2. GradientCircleElement의 update() 함수 구현
 */

// 연습용 Node 클래스
private class GradientCircleNode(
    var startColor: Color,
    var endColor: Color,
    var radiusFraction: Float, // 0.0 ~ 1.0, 크기의 비율
) : DrawModifierNode, Modifier.Node() {

    override fun ContentDrawScope.draw() {
        // TODO: 그라데이션 원을 그리세요
        //
        // 힌트:
        // 1. Brush.radialGradient로 그라데이션 생성
        // 2. drawCircle로 원 그리기
        // 3. drawContent()로 자식 콘텐츠 그리기
        //
        // val radius = minOf(size.width, size.height) / 2 * radiusFraction
        // val brush = Brush.radialGradient(
        //     colors = listOf(startColor, endColor),
        //     center = Offset(size.width / 2, size.height / 2),
        //     radius = radius
        // )
        // drawCircle(brush = brush, radius = radius)
        // drawContent()

        // 정답 (주석 해제하세요):
        /*
        val radius = minOf(size.width, size.height) / 2 * radiusFraction
        val brush = Brush.radialGradient(
            colors = listOf(startColor, endColor),
            center = Offset(size.width / 2, size.height / 2),
            radius = radius
        )
        drawCircle(brush = brush, radius = radius)
        drawContent()
        */

        // 임시: 콘텐츠만 그리기 (정답으로 교체하세요)
        drawContent()
    }
}

// 연습용 Element 클래스
private data class GradientCircleElement(
    val startColor: Color,
    val endColor: Color,
    val radiusFraction: Float,
) : ModifierNodeElement<GradientCircleNode>() {

    override fun create(): GradientCircleNode {
        return GradientCircleNode(startColor, endColor, radiusFraction)
    }

    override fun update(node: GradientCircleNode) {
        // TODO: node의 속성들을 업데이트하세요
        //
        // 힌트:
        // node.startColor = startColor
        // node.endColor = endColor
        // node.radiusFraction = radiusFraction

        // 정답 (주석 해제하세요):
        /*
        node.startColor = startColor
        node.endColor = endColor
        node.radiusFraction = radiusFraction
        */
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "gradientCircle"
        properties["startColor"] = startColor
        properties["endColor"] = endColor
        properties["radiusFraction"] = radiusFraction
    }
}

// Factory 함수
fun Modifier.gradientCircle(
    startColor: Color = Color.Magenta,
    endColor: Color = Color.Transparent,
    radiusFraction: Float = 0.8f,
): Modifier = this then GradientCircleElement(startColor, endColor, radiusFraction)

@Composable
fun Practice2_DrawModifierNode() {
    var radiusFraction by remember { mutableFloatStateOf(0.8f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 연습 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: DrawModifierNode로 그라데이션 원",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "콘텐츠 뒤에 그라데이션 원을 그리는 Modifier를 구현하세요.\n\n" +
                            "구현할 것:\n" +
                            "1. GradientCircleNode의 draw() 함수\n" +
                            "2. GradientCircleElement의 update() 함수",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 슬라이더로 반지름 조절
        Text("원 크기: ${(radiusFraction * 100).toInt()}%")
        Slider(
            value = radiusFraction,
            onValueChange = { radiusFraction = it },
            valueRange = 0.1f..1.5f
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .gradientCircle(
                        startColor = Color.Red.copy(alpha = 0.5f),
                        endColor = Color.Transparent,
                        radiusFraction = radiusFraction
                    )
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Red")
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .gradientCircle(
                        startColor = Color.Blue.copy(alpha = 0.5f),
                        endColor = Color.Transparent,
                        radiusFraction = radiusFraction
                    )
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Blue")
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .gradientCircle(
                        startColor = Color.Green.copy(alpha = 0.5f),
                        endColor = Color.Transparent,
                        radiusFraction = radiusFraction
                    )
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Green")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        draw() 함수:
                        1. Brush.radialGradient 생성
                        2. drawCircle로 원 그리기
                        3. drawContent()로 자식 그리기

                        update() 함수:
                        - node의 각 속성에 새 값 할당
                        - 노드를 재생성하지 않음!

                        순서 주의:
                        - 원을 먼저 그리고 drawContent()
                        - drawContent() 먼저면 원이 위에 그려짐
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ============================================================
// 연습 3: PointerInputModifierNode로 Long Press 효과 (고급)
// ============================================================

/**
 * [연습 3] PointerInputModifierNode로 Long Press 효과 구현하기
 *
 * 목표: 길게 누르면 색이 변하는 Modifier 만들기
 *
 * TODO:
 * 1. LongPressNode 클래스의 onPointerEvent() 함수 구현
 * 2. 길게 누르는 동안 배경색 변경
 */

private class LongPressHighlightNode(
    var highlightColor: Color,
) : PointerInputModifierNode, DrawModifierNode, Modifier.Node() {

    private var isPressed = false

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        // TODO: 터치 이벤트를 처리하세요
        //
        // 힌트:
        // - pass == PointerEventPass.Main 일 때만 처리
        // - PointerEventType.Press: isPressed = true
        // - PointerEventType.Release, Exit: isPressed = false
        // - invalidateDraw() 호출하여 다시 그리기

        // 정답 (주석 해제하세요):
        /*
        if (pass == PointerEventPass.Main) {
            when (pointerEvent.type) {
                PointerEventType.Press -> {
                    isPressed = true
                    invalidateDraw()
                }
                PointerEventType.Release,
                PointerEventType.Exit -> {
                    isPressed = false
                    invalidateDraw()
                }
            }
        }
        */
    }

    override fun onCancelPointerInput() {
        isPressed = false
        invalidateDraw()
    }

    override fun ContentDrawScope.draw() {
        // 눌려있을 때 하이라이트 배경
        if (isPressed) {
            drawRoundRect(
                color = highlightColor,
                cornerRadius = CornerRadius(16f)
            )
        }
        drawContent()
    }
}

private data class LongPressHighlightElement(
    val highlightColor: Color,
) : ModifierNodeElement<LongPressHighlightNode>() {

    override fun create(): LongPressHighlightNode {
        return LongPressHighlightNode(highlightColor)
    }

    override fun update(node: LongPressHighlightNode) {
        node.highlightColor = highlightColor
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "longPressHighlight"
        properties["highlightColor"] = highlightColor
    }
}

fun Modifier.longPressHighlight(
    highlightColor: Color = Color.Yellow.copy(alpha = 0.3f)
): Modifier = this then LongPressHighlightElement(highlightColor)

@Composable
fun Practice3_PointerInputNode() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 연습 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: PointerInputModifierNode로 터치 효과",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "누르고 있는 동안 배경색이 변하는 Modifier를 구현하세요.\n\n" +
                            "구현할 것:\n" +
                            "LongPressHighlightNode의 onPointerEvent() 함수",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트 영역
        Text(
            text = "아래 박스들을 누르고 있어보세요:",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .longPressHighlight(Color.Yellow.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Yellow")
                }
            }

            Card(
                modifier = Modifier
                    .size(100.dp)
                    .longPressHighlight(Color.Cyan.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cyan")
                }
            }

            Card(
                modifier = Modifier
                    .size(100.dp)
                    .longPressHighlight(Color.Magenta.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Magenta")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        onPointerEvent() 함수:
                        1. pass == PointerEventPass.Main 확인
                        2. pointerEvent.type으로 이벤트 종류 확인
                        3. PointerEventType.Press → isPressed = true
                        4. Release/Exit → isPressed = false
                        5. invalidateDraw() 호출

                        핵심 포인트:
                        - invalidateDraw()를 호출해야 화면 갱신
                        - onCancelPointerInput()도 처리 필요
                        - draw()에서 isPressed 상태에 따라 그리기
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Modifier.Node 구조 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Modifier.Node 구조 정리",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        1. Node 클래스 (로직/상태)
                           - Modifier.Node() 상속
                           - 필요한 *ModifierNode 인터페이스 구현
                           - 상태 변수 보관

                        2. Element 클래스 (생성/업데이트)
                           - ModifierNodeElement<T> 상속
                           - create(): 새 노드 생성
                           - update(): 기존 노드 업데이트
                           - data class 권장 (equals/hashCode)

                        3. Factory 함수 (API)
                           - fun Modifier.xxx() = this then Element
                           - 사용자가 호출하는 공개 API
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
