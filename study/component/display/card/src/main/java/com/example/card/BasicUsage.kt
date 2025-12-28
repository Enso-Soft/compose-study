package com.example.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * BasicUsage: Material 3 Card 기본 사용법
 *
 * Card는 관련된 정보를 하나의 컨테이너에 담는 Material Design 컴포넌트입니다.
 * '명함'이나 '카드 게임의 카드'처럼 한 장에 하나의 일관된 정보를 담습니다.
 *
 * Material 3에서 제공하는 3가지 Card 유형:
 * 1. Card (Filled) - 배경색이 채워진 기본 카드
 * 2. ElevatedCard - 그림자(elevation)가 있어 떠있는 느낌의 카드
 * 3. OutlinedCard - 테두리만 있는 심플한 카드
 */
@Composable
fun BasicUsageScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. 가장 기본적인 Card
        item {
            CardSection(
                title = "1. 가장 기본적인 Card",
                description = "Card는 내용물을 담는 그릇입니다. 텍스트, 이미지, 버튼 등을 넣을 수 있습니다."
            ) {
                Card {
                    Text(
                        text = "안녕하세요, Card입니다!",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                CodeSnippet(
                    """
Card {
    Text(
        text = "안녕하세요, Card입니다!",
        modifier = Modifier.padding(16.dp)
    )
}
                    """.trimIndent()
                )
            }
        }

        // 2. Card (Filled)
        item {
            CardSection(
                title = "2. Card (Filled) - 배경색이 채워진 카드",
                description = "기본 Card에 배경색을 지정할 수 있습니다. surfaceVariant 색상을 많이 사용합니다."
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Filled Card",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CodeSnippet(
                    """
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    modifier = Modifier.fillMaxWidth().height(100.dp)
) {
    Text("Filled Card")
}
                    """.trimIndent()
                )
            }
        }

        // 3. ElevatedCard
        item {
            CardSection(
                title = "3. ElevatedCard - 그림자가 있는 카드",
                description = "ElevatedCard는 그림자 효과로 배경에서 떠있는 느낌을 줍니다. 두꺼운 종이로 만든 명함처럼 입체감이 있습니다."
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Elevated Card",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CodeSnippet(
                    """
ElevatedCard(
    elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp
    ),
    modifier = Modifier.fillMaxWidth().height(100.dp)
) {
    Text("Elevated Card")
}
                    """.trimIndent()
                )
            }
        }

        // 4. OutlinedCard
        item {
            CardSection(
                title = "4. OutlinedCard - 테두리가 있는 카드",
                description = "OutlinedCard는 테두리로 영역을 구분합니다. 배경색 대신 테두리로 강조할 때 사용합니다."
            ) {
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Outlined Card",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CodeSnippet(
                    """
OutlinedCard(
    border = BorderStroke(1.dp, Color.Black),
    modifier = Modifier.fillMaxWidth().height(100.dp)
) {
    Text("Outlined Card")
}
                    """.trimIndent()
                )
            }
        }

        // 5. 3가지 유형 비교
        item {
            CardSection(
                title = "5. Card 유형 비교",
                description = "3가지 Card를 나란히 놓고 시각적 차이를 비교해보세요."
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Filled Card
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Filled",
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Elevated Card
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Elevated",
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Outlined Card
                    OutlinedCard(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Outlined",
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // 6. Card 유형 선택 가이드
        item {
            CardComparisonTable()
        }
    }
}

@Composable
private fun CardSection(
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
private fun CardComparisonTable() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Card 유형 선택 가이드",
                style = MaterialTheme.typography.titleMedium
            )

            HorizontalDivider()

            // 헤더
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "유형",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "특징",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "사용 시나리오",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1.2f)
                )
            }

            HorizontalDivider()

            // Card (Filled)
            ComparisonRow(
                type = "Card (Filled)",
                feature = "배경색 있음",
                useCase = "일반 콘텐츠 컨테이너"
            )

            // ElevatedCard
            ComparisonRow(
                type = "ElevatedCard",
                feature = "그림자 효과",
                useCase = "강조 필요, 떠있는 느낌"
            )

            // OutlinedCard
            ComparisonRow(
                type = "OutlinedCard",
                feature = "테두리만 있음",
                useCase = "심플한 구분, 선택 가능 항목"
            )
        }
    }
}

@Composable
private fun ComparisonRow(
    type: String,
    feature: String,
    useCase: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = feature,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = useCase,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1.2f)
        )
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
