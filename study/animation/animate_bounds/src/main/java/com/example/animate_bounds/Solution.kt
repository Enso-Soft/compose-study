package com.example.animate_bounds

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Animate Bounds - 해결책
 *
 * LookaheadScope와 Modifier.animateBounds()를 사용하여
 * 레이아웃 변경 시 위치와 크기를 모두 부드럽게 애니메이션합니다.
 *
 * 핵심 변경사항:
 * 1. LookaheadScope로 전체 영역 감싸기
 * 2. 각 요소에 Modifier.animateBounds() 적용
 * 3. key()를 사용하여 안정적인 아이템 식별
 * 4. BoundsTransform으로 애니메이션 커스터마이징
 */

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var isHorizontal by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 해결책 설명 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: LookaheadScope + animateBounds",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |아래 토글로 Row ↔ Column을 전환하거나 카드를 클릭해보세요.
                        |
                        |개선된 점:
                        |1. Row <-> Column 전환 시 자연스러운 애니메이션
                        |2. 카드 크기 변경 시 모든 카드가 부드럽게 이동
                        |3. 위치와 크기가 함께 Rect로 애니메이션됨
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Row/Column 토글 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.width(200.dp)
            ) {
                SegmentedButton(
                    selected = isHorizontal,
                    onClick = { isHorizontal = true },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("가로")
                }
                SegmentedButton(
                    selected = !isHorizontal,
                    onClick = { isHorizontal = false },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("세로")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(onClick = { selectedId = null }) {
                Text("초기화")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 예제 1: 레이아웃 전환 (Row <-> Column) - 위로 이동
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예제 1: Row <-> Column 전환",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "위의 토글로 레이아웃을 전환하면 요소들이 부드럽게 새 위치로 이동합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 해결책: LookaheadScope + animateBounds로 레이아웃 전환
                SolutionLayoutTransition(
                    isHorizontal = isHorizontal
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 예제 2: 카드 확장/축소 - 아래로 이동
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예제 2: 카드 확장 (animateBounds 사용)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "카드를 클릭하면 해당 카드와 다른 카드들 모두 부드럽게 이동합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 해결책: LookaheadScope + animateBounds
                SolutionCardGrid(
                    cards = sampleCards.take(4),
                    selectedId = selectedId,
                    onCardClick = { id ->
                        selectedId = if (selectedId == id) null else id
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 코드 설명
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 코드",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |LookaheadScope {
                        |    FlowRow(
                        |        maxItemsInEachRow = if (isHorizontal) Int.MAX_VALUE else 1
                        |    ) {
                        |        items.forEach { item ->
                        |            key(item.id) {  // 필수!
                        |                Box(
                        |                    Modifier
                        |                        .animateBounds(this@LookaheadScope)
                        |                        .size(...)
                        |                )
                        |            }
                        |        }
                        |    }
                        |}
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SolutionCardGrid(
    cards: List<ColorCard>,
    selectedId: Int?,
    onCardClick: (Int) -> Unit
) {
    // 핵심 1: LookaheadScope로 감싸기
    LookaheadScope {
        // 부모의 크기 변화도 애니메이션
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cards.forEach { card ->
                val isSelected = card.id == selectedId
                val size = if (isSelected) 120.dp else 70.dp

                // 핵심 2: key()로 아이템 식별 (재정렬 시 필수)
                key(card.id) {
                    Box(
                        modifier = Modifier
                            // 핵심 3: animateBounds로 위치+크기 애니메이션
                            .animateBounds(
                                lookaheadScope = this@LookaheadScope,
                                // 핵심 4: BoundsTransform으로 애니메이션 커스터마이징
                                boundsTransform = { _, _ ->
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                }
                            )
                            .size(size)
                            .clip(RoundedCornerShape(12.dp))
                            .background(card.color)
                            .clickable { onCardClick(card.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = card.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
private fun SolutionLayoutTransition(
    isHorizontal: Boolean
) {
    val items = listOf(
        Color(0xFF5C6BC0) to "A",
        Color(0xFF26A69A) to "B",
        Color(0xFFEF5350) to "C",
    )

    // 커스텀 BoundsTransform
    val boundsTransform: BoundsTransform = BoundsTransform { _, _ ->
        spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    }

    // 핵심: LookaheadScope로 감싸고 FlowRow의 maxItemsInEachRow로 방향 변경
    // if/else로 Row/Column을 분기하지 않음으로써 animateBounds가 제대로 동작!
    LookaheadScope {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            // Row 모드: 모든 아이템을 한 줄에 / Column 모드: 한 줄에 1개씩
            maxItemsInEachRow = if (isHorizontal) Int.MAX_VALUE else 1,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { (color, label) ->
                // key()로 안정적인 식별자 제공
                key(label) {
                    Box(
                        modifier = Modifier
                            // animateBounds로 위치+크기 애니메이션
                            .animateBounds(
                                lookaheadScope = this@LookaheadScope,
                                boundsTransform = boundsTransform
                            )
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
