package com.example.animation_physics

import androidx.compose.animation.core.*
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 연습 문제 1: 바운스 좋아요 버튼 (쉬움)
 *
 * 목표: spring() 기본 사용법 이해
 *
 * 요구사항:
 * - 좋아요 버튼을 누르면 하트가 커졌다가 바운스하며 원래 크기로 돌아옴
 * - 좋아요 상태일 때 빨간 하트, 아닐 때 회색 테두리 하트
 */
@Composable
fun Practice1_BounceButton() {
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
                    text = "연습 1: 바운스 좋아요 버튼",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "난이도: 쉬움",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "좋아요 버튼을 누르면 하트가 커졌다가(1.3배) 바운스하며 원래 크기로 돌아오게 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. animateFloatAsState + spring() 사용")
                Text("2. 클릭 시 잠시 scale을 1.3f로 변경")
                Text("3. dampingRatio: MediumBouncy 권장")
                Text("4. LaunchedEffect로 자동 복귀")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice1_Exercise()
            }
        }

        // 정답 보기 카드
        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 코드",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = """
var isLiked by remember { mutableStateOf(false) }
var animateTrigger by remember { mutableIntStateOf(0) }

val scale by animateFloatAsState(
    targetValue = if (animateTrigger > 0) 1.3f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
)

LaunchedEffect(animateTrigger) {
    if (animateTrigger > 0) {
        delay(100)
        animateTrigger = 0
    }
}

IconButton(
    onClick = {
        isLiked = !isLiked
        animateTrigger++
    }
) {
    Icon(
        modifier = Modifier.scale(scale),
        imageVector = if (isLiked) Icons.Filled.Favorite
                      else Icons.Outlined.FavoriteBorder,
        tint = if (isLiked) Color.Red else Color.Gray
    )
}
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: spring 애니메이션을 사용한 바운스 좋아요 버튼을 구현하세요
    // 요구사항:
    // 1. isLiked, animateTrigger 상태 변수 선언
    // 2. animateFloatAsState + spring() 사용
    // 3. dampingRatio = Spring.DampingRatioMediumBouncy
    // 4. stiffness = Spring.StiffnessMedium
    // 5. LaunchedEffect로 animateTrigger 변경 후 100ms 뒤 자동 복귀

    // TODO: 상태 변수 선언
    // var isLiked by remember { mutableStateOf(false) }
    // var animateTrigger by remember { mutableIntStateOf(0) }

    // TODO: spring 애니메이션 정의
    // val scale by animateFloatAsState(
    //     targetValue = if (animateTrigger > 0) 1.3f else 1f,
    //     animationSpec = spring(
    //         dampingRatio = Spring.DampingRatioMediumBouncy,
    //         stiffness = Spring.StiffnessMedium
    //     ),
    //     label = "like_scale"
    // )

    // TODO: 자동 복귀를 위한 LaunchedEffect
    // LaunchedEffect(animateTrigger) {
    //     if (animateTrigger > 0) {
    //         delay(100)
    //         animateTrigger = 0
    //     }
    // }

    var isLiked by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (isLiked) "좋아요!" else "좋아요를 눌러주세요",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            onClick = {
                isLiked = !isLiked
                // TODO: animateTrigger++ 추가
            },
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) Color.Red else Color.Gray,
                modifier = Modifier.size(48.dp)
                // TODO: .scale(scale) 추가
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "버튼을 클릭해보세요!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 연습 문제 2: 드래그 스냅백 카드 (중간)
 *
 * 목표: Animatable + spring 조합 이해
 *
 * 요구사항:
 * - 카드를 좌우로 드래그하면 따라옴
 * - 손을 떼면 스프링처럼 원위치로 돌아옴
 */
@Composable
fun Practice2_DragSnapCard() {
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
                    text = "연습 2: 드래그 스냅백 카드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "난이도: 중간",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "카드를 좌우로 드래그하면 따라오고, 손을 떼면 스프링처럼 원위치로 돌아오게 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. Animatable(0f)로 offsetX 생성")
                Text("2. detectDragGestures 사용")
                Text("3. onDrag: snapTo로 즉시 이동")
                Text("4. onDragEnd: animateTo(0f, spring())로 복귀")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice2_Exercise()
            }
        }

        // 정답 보기 카드
        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 코드",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = """
val offsetX = remember { Animatable(0f) }
val scope = rememberCoroutineScope()

Card(
    modifier = Modifier
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { _, dragAmount ->
                    scope.launch {
                        offsetX.snapTo(offsetX.value + dragAmount.x)
                    }
                },
                onDragEnd = {
                    scope.launch {
                        offsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    }
                }
            )
        }
)
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: Animatable + spring 조합으로 드래그 스냅백 카드를 구현하세요
    // 요구사항:
    // 1. Animatable(0f)로 offsetX 생성
    // 2. detectDragGestures 사용
    // 3. onDrag: scope.launch { offsetX.snapTo(offsetX.value + dragAmount.x) }
    // 4. onDragEnd: offsetX.animateTo(0f, spring())로 복귀

    // TODO: Animatable 생성
    // val offsetX = remember { Animatable(0f) }
    // val scope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "카드를 좌우로 드래그해보세요!",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "현재 offset: 0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                // TODO: .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                // TODO: .pointerInput(Unit) { detectDragGestures(...) }
                .width(200.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "드래그 카드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "놓으면 바운스 복귀!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 연습 문제 3: 스와이프 삭제 (어려움)
 *
 * 목표: VelocityTracker + animateDecay 조합 이해
 *
 * 요구사항:
 * - 아이템을 빠르게 스와이프하면 관성으로 미끄러짐
 * - 화면 절반 이상 밀리면 삭제, 아니면 스프링 복귀
 */
@Composable
fun Practice3_SwipeToDelete() {
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
                    text = "연습 3: 스와이프 삭제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "난이도: 어려움",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아이템을 빠르게 스와이프하면 관성으로 미끄러지다가, " +
                            "화면 절반 이상 이동하면 삭제되고, 아니면 원위치로 돌아오게 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. splineBasedDecay<Float>(density) 사용")
                Text("2. VelocityTracker로 드래그 속도 추적")
                Text("3. decay.calculateTargetValue()로 도착 예측")
                Text("4. 화면 절반 기준으로 삭제/복귀 분기")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice3_Exercise()
            }
        }

        // 정답 보기 카드
        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 코드",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = """
val offsetX = remember { Animatable(0f) }
val density = LocalDensity.current

Box(
    modifier = Modifier
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .pointerInput(Unit) {
            val decay = splineBasedDecay<Float>(density)

            coroutineScope {
                while (true) {
                    val velocityTracker = VelocityTracker()
                    offsetX.stop()

                    awaitPointerEventScope {
                        val pointerId = awaitFirstDown().id

                        horizontalDrag(pointerId) { change ->
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position
                            )
                            launch {
                                offsetX.snapTo(
                                    offsetX.value + change.positionChange().x
                                )
                            }
                        }
                    }

                    val velocity = velocityTracker.calculateVelocity().x
                    val targetX = decay.calculateTargetValue(
                        offsetX.value, velocity
                    )

                    launch {
                        if (abs(targetX) > size.width / 2) {
                            // 삭제
                            offsetX.animateTo(
                                if (targetX > 0) size.width.toFloat()
                                else -size.width.toFloat()
                            )
                            onDelete()
                        } else {
                            // 복귀
                            offsetX.animateTo(0f, spring())
                        }
                    }
                }
            }
        }
)
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: VelocityTracker + splineBasedDecay 조합으로 스와이프 삭제를 구현하세요
    // 요구사항:
    // 1. splineBasedDecay<Float>(density) 사용
    // 2. VelocityTracker로 드래그 속도 추적
    // 3. decay.calculateTargetValue()로 도착 예측
    // 4. 화면 절반 기준으로 삭제/복귀 분기

    // 아래 SwipeableItem은 정답 구현입니다.
    // TODO 주석을 참고하여 직접 구현해보세요!

    var items by remember { mutableStateOf(listOf("Item 1", "Item 2", "Item 3")) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "아이템을 빠르게 스와이프해보세요!",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (items.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.height(200.dp)
            ) {
                Text(
                    text = "모든 아이템이 삭제되었습니다",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    items = listOf("Item 1", "Item 2", "Item 3")
                }) {
                    Text("초기화")
                }
            }
        } else {
            items.forEach { item ->
                key(item) {
                    // TODO: 직접 SwipeableItem을 구현해보세요
                    // 현재는 정답 구현이 적용되어 있습니다
                    SwipeableItem(
                        text = item,
                        onDelete = {
                            items = items - item
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SwipeableItem(
    text: String,
    onDelete: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var isDeleted by remember { mutableStateOf(false) }

    if (isDeleted) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        // 삭제 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        // 스와이프 가능한 카드
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    val decay = splineBasedDecay<Float>(density)

                    coroutineScope {
                        while (true) {
                            val velocityTracker = VelocityTracker()
                            offsetX.stop()

                            awaitPointerEventScope {
                                val pointerId = awaitFirstDown().id

                                horizontalDrag(pointerId) { change ->
                                    velocityTracker.addPosition(
                                        change.uptimeMillis,
                                        change.position
                                    )
                                    launch {
                                        offsetX.snapTo(offsetX.value + change.positionChange().x)
                                    }
                                }
                            }

                            val velocity = velocityTracker.calculateVelocity().x
                            val targetX = decay.calculateTargetValue(offsetX.value, velocity)

                            launch {
                                if (abs(targetX) > size.width / 2) {
                                    // 삭제 애니메이션
                                    offsetX.animateTo(
                                        targetValue = if (targetX > 0) size.width.toFloat() else -size.width.toFloat(),
                                        animationSpec = spring()
                                    )
                                    isDeleted = true
                                    onDelete()
                                } else {
                                    // 원위치 복귀
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy
                                        )
                                    )
                                }
                            }
                        }
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
