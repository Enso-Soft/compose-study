package com.example.animation_advanced

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 올바른 코드 - 고급 애니메이션 API 활용
 *
 * 핵심 해결책:
 * 1. updateTransition: 여러 속성 동기화
 * 2. Animatable: 명령형 애니메이션 제어
 * 3. AnimationSpec 커스터마이징: spring, keyframes
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Solution: 고급 애니메이션 API",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 해결책 1: updateTransition
        SolutionUpdateTransition()

        // 해결책 2: Animatable
        SolutionAnimatable()

        // 해결책 3: AnimationSpec 비교
        SolutionAnimationSpec()

        // 핵심 포인트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- updateTransition: 여러 속성을 같은 전환에 바인딩")
                Text("- Animatable: 코루틴으로 명령형 제어")
                Text("- spring: 물리 기반, 자연스러운 바운스")
                Text("- keyframes: 특정 시점에 특정 값 지정")
                Text("- transitionSpec: 각 속성별 다른 AnimationSpec")
            }
        }
    }
}

/**
 * 해결책 1: updateTransition으로 동기화된 다중 속성 애니메이션
 */
private enum class CardState { Collapsed, Expanded }

@Composable
private fun SolutionUpdateTransition() {
    var cardState by remember { mutableStateOf(CardState.Collapsed) }

    // updateTransition: 모든 애니메이션이 같은 전환에 동기화
    val transition = updateTransition(
        targetState = cardState,
        label = "card transition"
    )

    // 각 속성은 같은 transition에 바인딩
    val backgroundColor by transition.animateColor(
        label = "background",
        transitionSpec = { tween(durationMillis = 500) }
    ) { state ->
        when (state) {
            CardState.Collapsed -> Color(0xFFE0E0E0)
            CardState.Expanded -> Color(0xFF2196F3)
        }
    }

    val size by transition.animateDp(
        label = "size",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        }
    ) { state ->
        when (state) {
            CardState.Collapsed -> 100.dp
            CardState.Expanded -> 150.dp
        }
    }

    val cornerRadius by transition.animateDp(
        label = "corner",
        transitionSpec = { tween(durationMillis = 500) }
    ) { state ->
        when (state) {
            CardState.Collapsed -> 24.dp
            CardState.Expanded -> 8.dp
        }
    }

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 300) }
    ) { state ->
        when (state) {
            CardState.Collapsed -> 0.7f
            CardState.Expanded -> 1f
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "해결 1: updateTransition",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "빠르게 클릭해도 모든 속성이 동기화됨!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(backgroundColor)
                    .graphicsLayer { this.alpha = alpha }
                    .clickable {
                        cardState = if (cardState == CardState.Collapsed)
                            CardState.Expanded else CardState.Collapsed
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (cardState) {
                        CardState.Collapsed -> "Collapsed"
                        CardState.Expanded -> "Expanded"
                    },
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "transition.animateColor, animateDp, animateFloat 사용",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 해결책 2: Animatable로 명령형 애니메이션 제어
 */
@Composable
private fun SolutionAnimatable() {
    // Animatable: 코루틴으로 직접 제어
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "해결 2: Animatable",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "클릭하면 '흔들기' 애니메이션 시퀀스 실행!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        // Shake 애니메이션: 좌우로 흔들기 시퀀스
                        repeat(3) {
                            offsetX.animateTo(
                                targetValue = -15f,
                                animationSpec = spring(stiffness = Spring.StiffnessHigh)
                            )
                            offsetX.animateTo(
                                targetValue = 15f,
                                animationSpec = spring(stiffness = Spring.StiffnessHigh)
                            )
                        }
                        offsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium)
                        )
                    }
                },
                modifier = Modifier.offset(x = offsetX.value.dp)
            ) {
                Text("Shake Me!")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Animatable + coroutineScope로 시퀀스 제어",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "animateTo()를 순차 호출하여 애니메이션 체이닝",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * 해결책 3: AnimationSpec 비교 (spring vs tween vs keyframes)
 */
@Composable
private fun SolutionAnimationSpec() {
    var isAnimating by remember { mutableStateOf(false) }

    // Spring: 물리 기반 바운스
    val springOffset by animateDpAsState(
        targetValue = if (isAnimating) (-40).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "spring"
    )

    // Tween: 시간 기반, 이징 적용
    val tweenOffset by animateDpAsState(
        targetValue = if (isAnimating) (-40).dp else 0.dp,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "tween"
    )

    // Keyframes: 특정 시점에 특정 값
    val keyframesOffset by animateDpAsState(
        targetValue = if (isAnimating) (-40).dp else 0.dp,
        animationSpec = keyframes {
            durationMillis = 600
            0.dp at 0 using LinearEasing
            (-50).dp at 200 using FastOutSlowInEasing
            (-30).dp at 400 using LinearEasing
            (-40).dp at 600
        },
        label = "keyframes"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "해결 3: AnimationSpec 비교",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Spring
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .offset(y = springOffset)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("S", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Spring", style = MaterialTheme.typography.bodySmall)
                    Text("바운스", style = MaterialTheme.typography.labelSmall)
                }

                // Tween
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .offset(y = tweenOffset)
                            .clip(CircleShape)
                            .background(Color(0xFF2196F3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("T", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tween", style = MaterialTheme.typography.bodySmall)
                    Text("이징", style = MaterialTheme.typography.labelSmall)
                }

                // Keyframes
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .offset(y = keyframesOffset)
                            .clip(CircleShape)
                            .background(Color(0xFFFF9800)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("K", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Keyframes", style = MaterialTheme.typography.bodySmall)
                    Text("경로", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { isAnimating = !isAnimating }) {
                Text(if (isAnimating) "Reset" else "Animate!")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "같은 움직임에 다른 AnimationSpec 적용",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
