package com.example.animate_bounds

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Animate Bounds - 문제 상황
 *
 * 이 예제는 animateBounds 없이 레이아웃이 변경될 때
 * 발생하는 **갑작스러운 점프 문제**를 보여줍니다.
 *
 * 문제점:
 * 1. 카드 크기가 변할 때 다른 카드들이 순간이동하듯 점프
 * 2. 레이아웃(Row <-> Column) 전환 시 요소들이 갑자기 재배치
 * 3. animateContentSize는 크기만 애니메이션하고 위치는 즉시 변경
 * 4. 사용자가 어떤 요소가 변경되었는지 시각적으로 추적하기 어려움
 *
 * 아래 앱을 실행하고 카드를 클릭하거나 레이아웃을 전환해보세요.
 */

// 샘플 데이터
data class ColorCard(
    val id: Int,
    val title: String,
    val color: Color
)

val sampleCards = listOf(
    ColorCard(1, "Red", Color(0xFFE53935)),
    ColorCard(2, "Blue", Color(0xFF1E88E5)),
    ColorCard(3, "Green", Color(0xFF43A047)),
    ColorCard(4, "Orange", Color(0xFFFF9800)),
    ColorCard(5, "Purple", Color(0xFF8E24AA)),
    ColorCard(6, "Teal", Color(0xFF00897B)),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemScreen() {
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var isHorizontal by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: 레이아웃 변경 시 갑작스러운 점프",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |아래 토글로 Row ↔ Column을 전환하거나 카드를 클릭해보세요.
                        |
                        |발생하는 문제:
                        |1. Row <-> Column 전환 시 갑작스러운 재배치
                        |2. 카드 크기 변경 시 다른 카드가 순간이동
                        |3. animateContentSize는 크기만 애니메이션함
                        |4. 위치 변경에는 애니메이션이 없음
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
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
                    text = "위의 토글로 레이아웃을 전환하면 요소들이 갑자기 재배치됩니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 문제: 레이아웃 전환 시 위치가 즉시 변경됨!
                ProblemLayoutTransition(
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
                    text = "예제 2: 카드 확장 (animateContentSize 사용)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "카드를 클릭하면 크기는 부드럽게 변하지만, 다른 카드의 위치는 즉시 점프합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 문제: animateContentSize는 크기만 애니메이션
                // 다른 카드들의 위치는 즉시 변경됨!
                ProblemCardGrid(
                    cards = sampleCards.take(4),
                    selectedId = selectedId,
                    onCardClick = { id ->
                        selectedId = if (selectedId == id) null else id
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 기술적 설명
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 이런 문제가 발생할까요?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |animateContentSize는 IntSize만 애니메이션합니다:
                        |- FiniteAnimationSpec<IntSize>
                        |
                        |하지만 레이아웃 재배치에는 위치(x, y)도 필요합니다:
                        |- 필요한 것: FiniteAnimationSpec<Rect>
                        |
                        |Compose는 기본적으로 레이아웃 변경을 즉시 적용합니다.
                        |위치 변경을 애니메이션하려면 별도의 메커니즘이 필요합니다.
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProblemCardGrid(
    cards: List<ColorCard>,
    selectedId: Int?,
    onCardClick: (Int) -> Unit
) {
    // animateContentSize: 부모 크기만 애니메이션
    // 자식들의 위치 변경은 즉시 적용됨!
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),  // 크기만 부드럽게, 위치는 점프!
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        cards.forEach { card ->
            val isSelected = card.id == selectedId
            val size = if (isSelected) 120.dp else 70.dp

            Box(
                modifier = Modifier
                    // 문제: 개별 카드의 크기 변경은 애니메이션되지만,
                    // 다른 카드들이 밀려나는 위치 변경은 즉시 적용됨!
                    .animateContentSize()
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProblemLayoutTransition(
    isHorizontal: Boolean
) {
    val items = listOf(
        Color(0xFF5C6BC0) to "A",
        Color(0xFF26A69A) to "B",
        Color(0xFFEF5350) to "C",
    )

    // 문제: FlowRow의 maxItemsInEachRow 변경 시 위치가 즉시 변경됨
    // 애니메이션 없이 갑자기 재배치됨!
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        maxItemsInEachRow = if (isHorizontal) Int.MAX_VALUE else 1,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (color, label) ->
            Box(
                modifier = Modifier
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
