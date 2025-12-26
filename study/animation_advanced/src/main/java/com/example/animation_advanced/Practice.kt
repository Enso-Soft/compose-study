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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ============================================================
// 연습 1: updateTransition으로 토글 버튼 만들기 (초급)
// ============================================================

/**
 * 연습 1: ON/OFF 토글 버튼을 updateTransition으로 구현
 *
 * 목표:
 * - 토글 상태에 따라 배경색 변경 (Gray ↔ Green)
 * - 핸들이 좌우로 이동
 * - 두 애니메이션이 동기화되어야 함
 *
 * TODO: 아래 코드를 완성하세요
 *
 * 힌트:
 * 1. enum class ToggleState { Off, On } 정의 (이미 되어있음)
 * 2. updateTransition(toggleState, label = "toggle")
 * 3. transition.animateColor로 배경색 애니메이션
 * 4. transition.animateDp로 핸들 오프셋 애니메이션
 */
private enum class ToggleState { Off, On }

@Composable
fun Practice1_ToggleButton() {
    var toggleState by remember { mutableStateOf(ToggleState.Off) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 1: updateTransition 토글 버튼",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Text("1. updateTransition(toggleState, label = \"toggle\")")
                Text("2. transition.animateColor { state -> ... }")
                Text("3. transition.animateDp { state -> ... }")
                Text("4. Off: Gray, 0.dp / On: Green, 36.dp")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: updateTransition을 사용하여 토글 버튼 구현
        // 현재는 애니메이션 없는 즉각적 변경

        val backgroundColor = if (toggleState == ToggleState.On) Color(0xFF4CAF50) else Color.Gray
        val handleOffset = if (toggleState == ToggleState.On) 36.dp else 0.dp

        // ========== 정답 ==========
        // val transition = updateTransition(toggleState, label = "toggle")
        //
        // val backgroundColor by transition.animateColor(
        //     label = "background",
        //     transitionSpec = { tween(300) }
        // ) { state ->
        //     when (state) {
        //         ToggleState.Off -> Color.Gray
        //         ToggleState.On -> Color(0xFF4CAF50)
        //     }
        // }
        //
        // val handleOffset by transition.animateDp(
        //     label = "offset",
        //     transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) }
        // ) { state ->
        //     when (state) {
        //         ToggleState.Off -> 0.dp
        //         ToggleState.On -> 36.dp
        //     }
        // }
        // ==========================

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(backgroundColor)
                .clickable {
                    toggleState = if (toggleState == ToggleState.Off) ToggleState.On else ToggleState.Off
                }
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = handleOffset)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }

        Text(
            text = "현재 상태: ${toggleState}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// ============================================================
// 연습 2: Animatable로 탄성 카운터 구현 (중급)
// ============================================================

/**
 * 연습 2: 숫자가 바뀔 때 spring 효과로 튀어오르는 카운터
 *
 * 목표:
 * - + 버튼 클릭 시 카운터 증가
 * - 숫자가 바뀔 때 scale이 1.0 → 1.3 → 1.0으로 변화
 * - spring(dampingRatio = 0.5f) 사용
 *
 * TODO: 아래 코드를 완성하세요
 *
 * 힌트:
 * 1. val scale = remember { Animatable(1f) }
 * 2. LaunchedEffect(count) 내에서 애니메이션 실행
 * 3. scale.animateTo(1.3f, spring(...))
 * 4. scale.animateTo(1f, spring(...))
 * 5. Modifier.graphicsLayer { scaleX = scale.value; scaleY = scale.value }
 */
@Composable
fun Practice2_BouncyCounter() {
    var count by remember { mutableIntStateOf(0) }

    // TODO: Animatable 정의
    // val scale = remember { Animatable(1f) }

    // TODO: LaunchedEffect로 count 변경 시 애니메이션
    // LaunchedEffect(count) {
    //     if (count > 0) {
    //         scale.animateTo(1.3f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium))
    //         scale.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium))
    //     }
    // }

    // ========== 정답 ==========
    val scale = remember { Animatable(1f) }

    LaunchedEffect(count) {
        if (count > 0) {
            scale.animateTo(
                targetValue = 1.3f,
                animationSpec = spring(
                    dampingRatio = 0.5f,
                    stiffness = Spring.StiffnessMedium
                )
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.5f,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }
    // ==========================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 2: Animatable 탄성 카운터",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Text("1. remember { Animatable(1f) }")
                Text("2. LaunchedEffect(count) { ... }")
                Text("3. scale.animateTo(1.3f, spring(...))")
                Text("4. graphicsLayer { scaleX = ...; scaleY = ... }")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // TODO: graphicsLayer 적용
        // 현재는 scale 변화 없음
        Text(
            text = "$count",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.graphicsLayer {
                // TODO: scale.value 적용
                scaleX = scale.value
                scaleY = scale.value
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { if (count > 0) count-- }) {
                Text("-", fontSize = 24.sp)
            }
            Button(onClick = { count++ }) {
                Text("+", fontSize = 24.sp)
            }
        }
    }
}

// ============================================================
// 연습 3: Keyframes로 로딩 도트 애니메이션 (고급)
// ============================================================

/**
 * 연습 3: 3개 도트가 순서대로 위아래로 움직이는 로딩 인디케이터
 *
 * 목표:
 * - 3개의 원이 가로로 배치
 * - 각 원이 0ms, 150ms, 300ms 딜레이로 애니메이션 시작
 * - infiniteRepeatable + keyframes 조합
 *
 * TODO: 아래 코드를 완성하세요
 *
 * 힌트:
 * 1. rememberInfiniteTransition() 사용
 * 2. infiniteTransition.animateFloat(
 *        initialValue = 0f,
 *        targetValue = 0f,
 *        animationSpec = infiniteRepeatable(
 *            animation = keyframes { ... },
 *            repeatMode = RepeatMode.Restart
 *        )
 *    )
 * 3. keyframes 내에서: 0f at 0, -20f at 300, 0f at 600
 * 4. initialStartOffset으로 각 도트 딜레이 적용
 */
@Composable
fun Practice3_LoadingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    // TODO: 3개 도트의 오프셋 애니메이션 정의
    // 각각 0ms, 150ms, 300ms 딜레이

    // ========== 정답 ==========
    val dot1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0f at 0
                -20f at 150 using FastOutSlowInEasing
                0f at 300
                0f at 600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "dot1"
    )

    val dot2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0f at 0
                0f at 150
                -20f at 300 using FastOutSlowInEasing
                0f at 450
                0f at 600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "dot2"
    )

    val dot3Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0f at 0
                0f at 300
                -20f at 450 using FastOutSlowInEasing
                0f at 600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "dot3"
    )
    // ==========================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 3: Keyframes 로딩 도트",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Text("1. rememberInfiniteTransition()")
                Text("2. infiniteRepeatable(keyframes { ... })")
                Text("3. keyframes: 0f at 0, -20f at 150, 0f at 300")
                Text("4. 각 도트마다 다른 타이밍으로 점프")
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 로딩 도트 UI
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dot 1
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .offset(y = dot1Offset.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // Dot 2
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .offset(y = dot2Offset.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // Dot 3
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .offset(y = dot3Offset.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Loading...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
