package com.example.auto_sizing_text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 문제 상황 화면
 *
 * Auto-sizing Text를 사용하지 않고 고정 fontSize를 사용했을 때
 * 발생하는 문제점들을 시연합니다.
 *
 * ## 문제점
 * 1. 텍스트 오버플로우 - 컨테이너보다 텍스트가 길면 잘림
 * 2. 정보 손실 - Ellipsis로 중요 정보 누락
 * 3. 레이아웃 불균형 - 다양한 화면 크기에서 일관성 없음
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
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
                    text = "문제 상황: 고정 크기 텍스트의 한계",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "고정된 fontSize를 사용하면:\n" +
                            "1. 컨테이너 크기에 맞지 않아 텍스트가 잘림\n" +
                            "2. Ellipsis(...)로 중요 정보 손실\n" +
                            "3. 다양한 화면 크기에서 일관성 없는 UI",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: 상품 카드 제목 오버플로우
        Problem1_ProductTitleOverflow()

        HorizontalDivider()

        // 문제 2: 가격 배너 잘림
        Problem2_PriceBannerClip()

        HorizontalDivider()

        // 문제 3: 통계 카드 레이아웃 깨짐
        Problem3_StatisticsLayoutBreak()
    }
}

/**
 * 문제 1: 상품 카드 제목 오버플로우
 *
 * 쇼핑몰 상품 카드에서 긴 제목을 고정 fontSize로 표시할 때
 * 발생하는 오버플로우 문제를 시연합니다.
 *
 * 슬라이더로 카드 너비를 조절하면 텍스트가 잘리는 것을 확인할 수 있습니다.
 */
@Composable
private fun Problem1_ProductTitleOverflow() {
    var cardWidth by remember { mutableFloatStateOf(300f) }
    val productTitle = "삼성 갤럭시 S25 울트라 512GB 티타늄 블랙 자급제"

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "문제 1: 상품 카드 제목 오버플로우",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        // 카드 너비 조절 슬라이더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("카드 너비:", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = cardWidth,
                onValueChange = { cardWidth = it },
                valueRange = 150f..350f,
                modifier = Modifier.weight(1f)
            )
            Text("${cardWidth.toInt()}dp", style = MaterialTheme.typography.bodySmall)
        }

        // 문제가 발생하는 카드
        Card(
            modifier = Modifier.width(cardWidth.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "고정 fontSize = 16.sp",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 고정 fontSize로 인한 문제
                Text(
                    text = productTitle,
                    fontSize = 16.sp,  // 고정 크기 - 문제 발생!
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis  // 정보 손실!
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "1,650,000원",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 문제점 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "발생하는 문제:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "- 카드 너비가 좁아지면 '...'으로 잘림\n" +
                            "- 상품명의 중요 정보(용량, 색상) 손실\n" +
                            "- 사용자가 전체 정보를 알 수 없음",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제 2: 가격 배너 잘림
 *
 * 세일 가격을 표시하는 배너에서 고정 fontSize를 사용할 때
 * 화면 크기에 따라 가격이 잘리는 문제를 시연합니다.
 */
@Composable
private fun Problem2_PriceBannerClip() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "문제 2: 가격 배너 잘림",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "다양한 배너 너비에서 동일한 fontSize = 32.sp 사용:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 다양한 너비에서 문제 시연
        val widths = listOf(
            280.dp to "넓은 배너",
            200.dp to "중간 배너",
            140.dp to "좁은 배너"
        )

        widths.forEach { (width, label) ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "$label (${width.value.toInt()}dp):",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .width(width)
                        .background(Color(0xFFE53935))  // 세일 빨간색
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "99,900원",
                        fontSize = 32.sp,  // 고정 크기 - 문제 발생!
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Clip  // 가격 잘림!
                    )
                }
            }
        }

        // 문제점 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "발생하는 문제:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "- 좁은 배너에서 가격이 완전히 잘림\n" +
                            "- 중요한 가격 정보 손실\n" +
                            "- 사용자가 정확한 가격을 알 수 없음",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제 3: 통계 카드 레이아웃 깨짐
 *
 * 대시보드 통계 카드에서 다양한 길이의 숫자를 고정 fontSize로 표시할 때
 * 레이아웃이 깨지는 문제를 시연합니다.
 */
@Composable
private fun Problem3_StatisticsLayoutBreak() {
    var rowWidth by remember { mutableFloatStateOf(350f) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "문제 3: 통계 카드 레이아웃 깨짐",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        // Row 너비 조절 슬라이더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Row 너비:", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = rowWidth,
                onValueChange = { rowWidth = it },
                valueRange = 200f..400f,
                modifier = Modifier.weight(1f)
            )
            Text("${rowWidth.toInt()}dp", style = MaterialTheme.typography.bodySmall)
        }

        // 문제가 발생하는 통계 카드들
        Row(
            modifier = Modifier.width(rowWidth.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 각 카드에 고정 fontSize 사용
            ProblemStatCard(
                modifier = Modifier.weight(1f),
                title = "방문자",
                value = "1,234"
            )
            ProblemStatCard(
                modifier = Modifier.weight(1f),
                title = "주문",
                value = "56,789,012"  // 긴 숫자!
            )
            ProblemStatCard(
                modifier = Modifier.weight(1f),
                title = "매출",
                value = "9억원"
            )
        }

        // 문제점 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "발생하는 문제:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "- '주문' 카드의 긴 숫자가 잘림\n" +
                            "- 카드 간 시각적 불균형\n" +
                            "- Row 너비가 좁아지면 모든 숫자가 잘림",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제가 있는 통계 카드 (고정 fontSize 사용)
 */
@Composable
private fun ProblemStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,  // 고정 크기 - 문제 발생!
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
