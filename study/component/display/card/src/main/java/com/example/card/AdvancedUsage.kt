package com.example.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * AdvancedUsage: Card 고급 활용법
 *
 * 다루는 내용:
 * 1. 클릭 가능한 Card (onClick)
 * 2. Card 내부 레이아웃 패턴
 * 3. 이미지 + 텍스트 Card
 * 4. 버튼 포함 Card
 * 5. CardDefaults 커스터마이징
 */
@Composable
fun AdvancedUsageScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. 클릭 가능한 Card
        item {
            ClickableCardSection()
        }

        // 2. 확장/축소 Card
        item {
            ExpandableCardSection()
        }

        // 3. 이미지 + 텍스트 레이아웃
        item {
            ImageTextCardSection()
        }

        // 4. 버튼 포함 Card
        item {
            CardWithButtonSection()
        }

        // 5. CardDefaults 커스터마이징
        item {
            CustomCardSection()
        }

        // 6. 다양한 Card 모양
        item {
            CardShapeSection()
        }
    }
}

@Composable
private fun ClickableCardSection() {
    var clickCount by remember { mutableIntStateOf(0) }

    AdvancedCard(
        title = "1. 클릭 가능한 Card",
        description = "Card에 onClick 파라미터를 추가하면 클릭할 수 있습니다. 클릭하면 ripple 효과가 나타납니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 클릭 가능한 Card
            Card(
                onClick = { clickCount++ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "클릭해보세요!",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "클릭 횟수: $clickCount",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.TouchApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            CodeSnippet(
                """
Card(
    onClick = { clickCount++ },
    modifier = Modifier.fillMaxWidth()
) {
    Text("클릭 횟수: ${'$'}clickCount")
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun ExpandableCardSection() {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    AdvancedCard(
        title = "2. 확장/축소 Card",
        description = "클릭하면 상세 정보가 나타나는 Card입니다. FAQ나 상세보기에 활용됩니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ElevatedCard(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Card는 무엇인가요?",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Icon(
                            imageVector = Icons.Filled.ExpandMore,
                            contentDescription = if (isExpanded) "접기" else "펼치기",
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Card는 Material Design의 컨테이너 컴포넌트입니다. " +
                                    "관련된 정보를 하나의 영역에 묶어서 보여줄 때 사용합니다. " +
                                    "쇼핑 앱의 상품 카드, 뉴스 앱의 기사 카드 등이 대표적인 예입니다.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            CodeSnippet(
                """
var isExpanded by remember { mutableStateOf(false) }

ElevatedCard(
    onClick = { isExpanded = !isExpanded },
    modifier = Modifier.animateContentSize()
) {
    Text("제목")
    if (isExpanded) {
        Text("상세 내용...")
    }
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun ImageTextCardSection() {
    AdvancedCard(
        title = "3. 이미지 + 텍스트 레이아웃",
        description = "Card 안에 Row를 사용하여 이미지와 텍스트를 나란히 배치합니다. 리스트 아이템에 많이 사용됩니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 가로형 Card (이미지 왼쪽)
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 이미지 영역 (실제 앱에서는 AsyncImage 등 사용)
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 텍스트 영역
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "상품명",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "상품에 대한 간단한 설명입니다.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "10,000원",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 세로형 Card (이미지 위)
            Card(
                modifier = Modifier.width(200.dp)
            ) {
                Column {
                    // 이미지 영역
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // 텍스트 영역
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "세로형 카드",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "이미지가 위에 배치됩니다",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            CodeSnippet(
                """
Card {
    Row(modifier = Modifier.padding(16.dp)) {
        // 이미지 영역
        Box(modifier = Modifier.size(64.dp))
        Spacer(Modifier.width(16.dp))
        // 텍스트 영역
        Column {
            Text("제목")
            Text("설명")
        }
    }
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun CardWithButtonSection() {
    AdvancedCard(
        title = "4. 버튼 포함 Card",
        description = "Card 하단에 액션 버튼을 배치합니다. 상품 카드, 알림 카드 등에 활용됩니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 헤더
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "프리미엄 상품",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700) // 골드 색상
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 본문
                    Text(
                        text = "최고급 품질의 프리미엄 상품입니다. 지금 구매하시면 20% 할인!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "80,000원",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 버튼 영역
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("찜하기")
                        }

                        Button(
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("구매하기")
                        }
                    }
                }
            }

            CodeSnippet(
                """
Card {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("상품명")
        Text("가격")
        Row {
            OutlinedButton(...) { Text("찜하기") }
            Button(...) { Text("구매하기") }
        }
    }
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun CustomCardSection() {
    AdvancedCard(
        title = "5. CardDefaults 커스터마이징",
        description = "CardDefaults로 색상, 그림자(elevation), 테두리를 커스터마이징할 수 있습니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 커스텀 색상 Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0), // 연한 오렌지
                    contentColor = Color(0xFFE65100)    // 진한 오렌지
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "주의: 커스텀 색상 Card입니다",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 커스텀 elevation Card
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    hoveredElevation = 10.dp
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "높은 Elevation (8dp)",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // 커스텀 테두리 Card
            OutlinedCard(
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "두꺼운 Primary 색상 테두리",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CodeSnippet(
                """
// 색상 커스터마이징
Card(
    colors = CardDefaults.cardColors(
        containerColor = Color(0xFFFFF3E0),
        contentColor = Color(0xFFE65100)
    )
)

// Elevation 커스터마이징
ElevatedCard(
    elevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp,
        pressedElevation = 12.dp
    )
)

// 테두리 커스터마이징
OutlinedCard(
    border = BorderStroke(2.dp, Color.Blue)
)
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun CardShapeSection() {
    AdvancedCard(
        title = "6. Card 모양 변경",
        description = "shape 파라미터로 Card의 모서리 모양을 변경할 수 있습니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 둥근 모서리
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("둥근 16dp")
                    }
                }

                // 아주 둥근 모서리
                Card(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Pill 모양")
                    }
                }

                // 각진 모서리
                Card(
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("각진 0dp")
                    }
                }
            }

            CodeSnippet(
                """
Card(shape = RoundedCornerShape(16.dp)) { }
Card(shape = RoundedCornerShape(50)) { } // Pill
Card(shape = RoundedCornerShape(0.dp)) { } // 각진
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun AdvancedCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun CodeSnippet(code: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = code,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
    }
}
