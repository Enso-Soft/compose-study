package com.example.animation_advanced

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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

/**
 * 문제가 있는 코드 - animate*AsState만으로는 한계가 있는 상황
 *
 * 이 코드의 문제점:
 * 1. 여러 속성이 독립적으로 애니메이션 → 동기화 문제 가능
 * 2. 이벤트 핸들러에서 애니메이션 직접 제어 불가
 * 3. 복잡한 타이밍(바운스, 키프레임) 제어 어려움
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Problem: animate*AsState의 한계",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        // 문제 1: 독립적인 애니메이션 동기화 문제
        ProblemIndependentAnimations()

        // 문제 2: 이벤트 기반 애니메이션 제어 불가
        ProblemNoEventControl()

        // 문제 3: 복잡한 타이밍 제어 불가
        ProblemNoComplexTiming()

        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 문제인가?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 각 animate*AsState가 독립적 → 빠르게 토글하면 불일치")
                Text("2. 버튼 클릭 시 '흔들기' 같은 시퀀스 구현 어려움")
                Text("3. 바운스, 키프레임 경로 등 정밀 제어 한계")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solution 탭에서 해결책을 확인하세요!",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 문제 1: 독립적인 애니메이션들 - 동기화 문제
 *
 * 빠르게 클릭하면 각 애니메이션이 다른 타이밍에 있을 수 있음
 */
@Composable
private fun ProblemIndependentAnimations() {
    var isExpanded by remember { mutableStateOf(false) }

    // 각각 독립적인 애니메이션
    val backgroundColor by animateColorAsState(
        targetValue = if (isExpanded) Color(0xFF2196F3) else Color(0xFFE0E0E0),
        label = "background"
    )
    val size by animateDpAsState(
        targetValue = if (isExpanded) 150.dp else 100.dp,
        label = "size"
    )
    val cornerRadius by animateDpAsState(
        targetValue = if (isExpanded) 8.dp else 24.dp,
        label = "corner"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0.7f,
        label = "alpha"
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
                text = "문제 1: 독립적인 애니메이션들",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "빠르게 클릭해보세요 - 속성들이 다른 타이밍에 변할 수 있음",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(backgroundColor)
                    .graphicsLayer { this.alpha = alpha }
                    .clickable { isExpanded = !isExpanded },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isExpanded) "Expanded" else "Collapsed",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "4개의 animate*AsState가 독립적으로 동작",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 2: 이벤트 기반 애니메이션 제어 불가
 *
 * 버튼 클릭 시 "흔들기" 같은 애니메이션 시퀀스 구현 어려움
 */
@Composable
private fun ProblemNoEventControl() {
    var shakeCount by remember { mutableIntStateOf(0) }

    // animate*AsState는 상태 변경에만 반응
    // 클릭할 때마다 "왼쪽 → 오른쪽 → 중앙" 시퀀스를 어떻게 구현?

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
                text = "문제 2: 이벤트 기반 제어 불가",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "클릭 시 '흔들기' 효과를 어떻게 구현할까?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    shakeCount++
                    // animate*AsState로는 "왼쪽 → 오른쪽 → 중앙" 시퀀스 구현 어려움
                    // 클릭할 때마다 특정 애니메이션 시퀀스를 실행하고 싶은데...
                }
            ) {
                Text("Shake Me! (클릭 ${shakeCount}회)")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "animate*AsState는 상태 기반 → 시퀀스 제어 불가",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 3: 복잡한 타이밍 제어 불가
 *
 * 바운스 효과, 키프레임 경로 등 정밀한 타이밍 제어 어려움
 */
@Composable
private fun ProblemNoComplexTiming() {
    var isBouncing by remember { mutableStateOf(false) }

    // 기본 tween 애니메이션 - 바운스 없음
    val offsetY by animateDpAsState(
        targetValue = if (isBouncing) (-30).dp else 0.dp,
        label = "offset"
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
                text = "문제 3: 복잡한 타이밍 제어",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "바운스 효과나 키프레임 경로를 적용하고 싶다면?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(y = offsetY)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { isBouncing = !isBouncing },
                contentAlignment = Alignment.Center
            ) {
                Text("Tap", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "기본 tween → 바운스 없이 단조로운 움직임",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "spring, keyframes로 더 풍부한 효과 필요!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
