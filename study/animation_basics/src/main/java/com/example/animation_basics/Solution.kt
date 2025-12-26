package com.example.animation_basics

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 올바른 코드 - 애니메이션 API 활용
 *
 * 핵심 해결책:
 * 1. animateDpAsState: 크기 애니메이션
 * 2. animateColorAsState: 색상 애니메이션
 * 3. AnimatedVisibility: 등장/퇴장 애니메이션
 * 4. Crossfade: 컨텐츠 전환 애니메이션
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
            text = "Solution: 애니메이션 적용",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 해결책 1: animateDpAsState로 크기 애니메이션
        SolutionSizeAnimation()

        // 해결책 2: animateColorAsState + animateFloatAsState
        SolutionColorAnimation()

        // 해결책 3: AnimatedVisibility
        SolutionAnimatedVisibility()

        // 해결책 4: Crossfade
        SolutionCrossfade()

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
                Text("- animateDpAsState: Dp 값 변화를 부드럽게")
                Text("- animateColorAsState: Color 값 변화를 부드럽게")
                Text("- animateFloatAsState: Float 값 (scale, alpha 등)")
                Text("- AnimatedVisibility: 등장/퇴장 애니메이션")
                Text("- Crossfade: 컨텐츠 교체 시 페이드 효과")
            }
        }
    }
}

/**
 * 해결책 1: animateDpAsState로 부드러운 크기 변화
 */
@Composable
private fun SolutionSizeAnimation() {
    var isExpanded by remember { mutableStateOf(false) }

    // animateDpAsState로 크기 애니메이션
    val boxSize by animateDpAsState(
        targetValue = if (isExpanded) 150.dp else 80.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "box size"
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
                text = "해결 1: animateDpAsState",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { isExpanded = !isExpanded },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "클릭!",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "spring 애니메이션으로 자연스러운 전환",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 해결책 2: animateColorAsState + animateFloatAsState
 */
@Composable
private fun SolutionColorAnimation() {
    var isLiked by remember { mutableStateOf(false) }

    // animateColorAsState로 색상 애니메이션
    val iconColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300),
        label = "icon color"
    )

    // animateFloatAsState로 크기(scale) 애니메이션
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "icon scale"
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
                text = "해결 2: animateColorAsState + animateFloatAsState",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = { isLiked = !isLiked }) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = iconColor,
                    modifier = Modifier
                        .size(48.dp)
                        .scale(scale)
                )
            }

            Text(
                text = "색상 + 크기가 함께 애니메이션",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 해결책 3: AnimatedVisibility로 등장/퇴장 애니메이션
 */
@Composable
private fun SolutionAnimatedVisibility() {
    var isVisible by remember { mutableStateOf(true) }

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
                text = "해결 3: AnimatedVisibility",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { isVisible = !isVisible }) {
                Text(if (isVisible) "숨기기" else "보이기")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AnimatedVisibility로 부드러운 등장/퇴장
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(300)) +
                        expandVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) +
                        shrinkVertically(animationSpec = tween(300))
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "부드럽게 나타나고 사라지는 카드",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "fadeIn + expandVertically / fadeOut + shrinkVertically",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 해결책 4: Crossfade로 컨텐츠 전환
 */
@Composable
private fun SolutionCrossfade() {
    var currentScreen by remember { mutableStateOf("A") }

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
                text = "해결 4: Crossfade",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { currentScreen = "A" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentScreen == "A")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                ) {
                    Text("화면 A")
                }
                Button(
                    onClick = { currentScreen = "B" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentScreen == "B")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                ) {
                    Text("화면 B")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Crossfade로 부드러운 화면 전환
            Crossfade(
                targetState = currentScreen,
                animationSpec = tween(500),
                label = "screen crossfade"
            ) { screen ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (screen) {
                            "A" -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (screen) {
                                "A" -> "화면 A 콘텐츠"
                                else -> "화면 B 콘텐츠"
                            },
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crossfade로 부드러운 화면 전환",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
