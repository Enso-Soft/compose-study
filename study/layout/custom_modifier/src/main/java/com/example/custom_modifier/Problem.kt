package com.example.custom_modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem.kt - Custom Modifier의 문제 상황
 *
 * 이 파일에서는 Modifier.composed를 사용했을 때 발생하는
 * 성능 문제를 시연합니다.
 */

// ============================================================
// 문제 1: Modifier.composed의 성능 오버헤드
// ============================================================

/**
 * [비권장] Modifier.composed 사용 - 성능 문제 발생!
 *
 * 문제점:
 * 1. 각 호출마다 새로운 subcomposition 생성
 * 2. remember가 매번 새로 호출됨
 * 3. Composable 함수가 skip 되지 않음
 * 4. LazyColumn에서 심각한 프레임 드롭
 */
@Suppress("DEPRECATION")
fun Modifier.badScaleOnPress(
    pressedScale: Float = 0.95f
) = composed {
    // 매번 새로운 subcomposition에서 remember 호출 - 오버헤드!
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        label = "scale"
    )

    // Recomposition 추적용 (실제로는 사용하지 않음)
    SideEffect {
        composedCallCount++
    }

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                }
            )
        }
}

// Subcomposition 횟수 추적 (UI에서 관찰 가능하도록 State로 변경)
var composedCallCount by mutableStateOf(0)
    private set

// ============================================================
// 문제 2: 조건부 Modifier의 잘못된 패턴
// ============================================================

/**
 * [잘못된 패턴] 분기마다 다른 Composable 트리 생성
 *
 * 이렇게 하면 상태가 유지되지 않고, 불필요한 recomposition 발생
 */
@Composable
fun BadConditionalModifierExample() {
    var isHighlighted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { isHighlighted = !isHighlighted }) {
            Text("Toggle Highlight")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 잘못된 패턴: 조건에 따라 다른 Box 생성
        // 상태 전환 시 Composable이 완전히 재생성됨
        if (isHighlighted) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Yellow, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Highlighted")
            }
        } else {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Normal")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "문제: if/else로 분기하면 Composable 트리가 달라져서\n" +
                    "상태가 유지되지 않고 매번 재생성됩니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

// ============================================================
// 메인 화면
// ============================================================

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Modifier.composed {}는 각 호출마다 새로운 " +
                            "subcomposition을 생성하여 심각한 성능 문제를 발생시킵니다.\n\n" +
                            "특히 LazyColumn에서 사용하면 스크롤 시 프레임 드롭이 발생합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recomposition 카운터
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Composed 블록 호출 횟수: $composedCallCount",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "↑ 스크롤하면 이 숫자가 증가합니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 1: composed 성능 테스트
        Text(
            text = "문제 1: Modifier.composed 성능",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "아래 리스트를 스크롤해보세요. " +
                    "각 아이템에 badScaleOnPress modifier가 적용되어 있습니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn with composed modifier
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(8.dp)
                ),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((1..50).toList()) { index ->
                ComposedModifierItem(index = index)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "* 각 아이템을 누르면 축소 효과가 적용됩니다.\n" +
                    "* 스크롤 시 새 아이템마다 subcomposition이 생성됩니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 2: 잘못된 조건부 Modifier
        Text(
            text = "문제 2: 잘못된 조건부 Modifier",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        BadConditionalModifierExample()

        Spacer(modifier = Modifier.height(24.dp))

        // 코드 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 문제인가?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        1. Modifier.composed의 문제:
                           - 각 호출마다 subcomposition 생성
                           - remember가 매번 새로 실행
                           - skip 최적화 불가능

                        2. if/else 분기의 문제:
                           - Composable 트리가 달라짐
                           - 상태가 유지되지 않음
                           - 불필요한 재생성
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ComposedModifierItem(index: Int) {
    var itemRecomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        itemRecomposeCount++
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .badScaleOnPress(), // composed modifier 사용 - 비권장!
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Item #$index",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Recompose: $itemRecomposeCount",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
