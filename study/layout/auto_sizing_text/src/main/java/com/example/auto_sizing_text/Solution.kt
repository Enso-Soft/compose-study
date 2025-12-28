package com.example.auto_sizing_text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 해결책 화면
 *
 * Auto-sizing Text를 사용하여 고정 fontSize의 문제를 해결합니다.
 *
 * ## 구현 방식
 * Compose 1.8+ 에서는 TextAutoSize.StepBased()를 사용할 수 있지만,
 * 현재 프로젝트 BOM 버전에서는 커스텀 AutoSizeText 컴포저블로 구현합니다.
 *
 * ## 핵심 원리
 * - 텍스트가 컨테이너에 맞을 때까지 폰트 크기를 조절
 * - drawWithContent로 텍스트 오버플로우 감지
 * - minFontSize ~ maxFontSize 범위 내에서 자동 조절
 *
 * ## 비유
 * Auto-sizing Text는 '자동 줌 기능'과 같습니다.
 * - 컨테이너가 크면 텍스트가 크게 표시됨
 * - 컨테이너가 작으면 텍스트가 작게 표시됨
 * - 하지만 텍스트 내용은 항상 전체가 보임!
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 해결책 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Auto-sizing Text",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "텍스트 크기를 컨테이너에 맞게 자동 조절하여\n" +
                            "오버플로우 없이 전체 텍스트를 표시합니다.\n\n" +
                            "마치 '자동 줌'처럼 컨테이너 크기에 따라\n" +
                            "텍스트 크기가 자동으로 조절됩니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider()

        // 해결책 1: 상품 카드 제목
        Solution1_AutoSizeProductTitle()

        HorizontalDivider()

        // 해결책 2: 가격 배너
        Solution2_AutoSizePriceBanner()

        HorizontalDivider()

        // 해결책 3: 통계 카드
        Solution3_AutoSizeStatistics()

        HorizontalDivider()

        // API 요약
        ApiSummaryCard()
    }
}

/**
 * Auto-sizing Text 컴포저블
 *
 * 텍스트 크기를 컨테이너에 맞게 자동 조절합니다.
 * Compose 1.8 이전 버전에서 사용할 수 있는 커스텀 구현입니다.
 *
 * @param text 표시할 텍스트
 * @param minFontSize 최소 폰트 크기
 * @param maxFontSize 최대 폰트 크기
 * @param step 크기 조절 단위
 * @param style 텍스트 스타일
 * @param textAlign 텍스트 정렬
 * @param maxLines 최대 줄 수
 * @param modifier Modifier
 */
@Composable
fun AutoSizeText(
    text: String,
    minFontSize: TextUnit = 10.sp,
    maxFontSize: TextUnit = 24.sp,
    step: TextUnit = 1.sp,
    style: TextStyle = TextStyle.Default,
    textAlign: TextAlign? = null,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    var fontSize by remember { mutableStateOf(maxFontSize) }
    var readyToDraw by remember { mutableStateOf(false) }

    val mergedStyle = if (textAlign != null) {
        style.copy(fontSize = fontSize, textAlign = textAlign)
    } else {
        style.copy(fontSize = fontSize)
    }

    Text(
        text = text,
        style = mergedStyle,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        softWrap = false,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth || textLayoutResult.didOverflowHeight) {
                val nextFontSizeValue = fontSize.value - step.value
                if (nextFontSizeValue >= minFontSize.value) {
                    fontSize = nextFontSizeValue.sp
                } else {
                    fontSize = minFontSize
                    readyToDraw = true
                }
            } else {
                readyToDraw = true
            }
        }
    )
}

/**
 * 해결책 1: Auto-sizing 상품 카드 제목
 *
 * 카드 너비에 맞게 상품 제목 크기를 자동 조절합니다.
 */
@Composable
private fun Solution1_AutoSizeProductTitle() {
    var cardWidth by remember { mutableFloatStateOf(300f) }
    val productTitle = "삼성 갤럭시 S25 울트라 512GB 티타늄 블랙 자급제"

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "해결책 1: Auto-sizing 상품 카드 제목",
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

        // Auto-sizing이 적용된 카드
        Card(
            modifier = Modifier.width(cardWidth.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "AutoSizeText 사용",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Auto-sizing 적용!
                // key로 cardWidth를 사용하여 크기 변경 시 재계산
                key(cardWidth) {
                    AutoSizeText(
                        text = productTitle,
                        minFontSize = 10.sp,
                        maxFontSize = 16.sp,
                        step = 0.5.sp,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "1,650,000원",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 핵심 포인트
        KeyPointCard(
            text = "카드 너비가 변해도 제목 전체가 항상 보입니다!\n" +
                    "minFontSize = 10.sp로 최소 가독성을 보장합니다."
        )
    }
}

/**
 * 해결책 2: Auto-sizing 가격 배너
 *
 * 배너 크기에 맞게 가격 크기를 자동 조절합니다.
 */
@Composable
private fun Solution2_AutoSizePriceBanner() {
    var bannerWidth by remember { mutableFloatStateOf(280f) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "해결책 2: Auto-sizing 가격 배너",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        // 배너 너비 조절 슬라이더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("배너 너비:", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = bannerWidth,
                onValueChange = { bannerWidth = it },
                valueRange = 120f..320f,
                modifier = Modifier.weight(1f)
            )
            Text("${bannerWidth.toInt()}dp", style = MaterialTheme.typography.bodySmall)
        }

        // Auto-sizing이 적용된 배너
        Box(
            modifier = Modifier
                .width(bannerWidth.dp)
                .background(Color(0xFFE53935))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            key(bannerWidth) {
                AutoSizeText(
                    text = "99,900원",
                    minFontSize = 16.sp,
                    maxFontSize = 48.sp,
                    step = 2.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 핵심 포인트
        KeyPointCard(
            text = "step = 2.sp로 빠르게 조절됩니다.\n" +
                    "배너 크기에 관계없이 가격이 항상 완전히 표시됩니다!"
        )
    }
}

/**
 * 해결책 3: Auto-sizing 통계 카드
 *
 * 다양한 길이의 숫자도 균형 잡힌 레이아웃으로 표시합니다.
 */
@Composable
private fun Solution3_AutoSizeStatistics() {
    var rowWidth by remember { mutableFloatStateOf(350f) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "해결책 3: Auto-sizing 통계 카드",
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

        // Auto-sizing이 적용된 통계 카드들
        Row(
            modifier = Modifier.width(rowWidth.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            key(rowWidth) {
                SolutionStatCard(
                    modifier = Modifier.weight(1f),
                    title = "방문자",
                    value = "1,234",
                    rowWidth = rowWidth
                )
                SolutionStatCard(
                    modifier = Modifier.weight(1f),
                    title = "주문",
                    value = "56,789,012",
                    rowWidth = rowWidth
                )
                SolutionStatCard(
                    modifier = Modifier.weight(1f),
                    title = "매출",
                    value = "9억원",
                    rowWidth = rowWidth
                )
            }
        }

        // 핵심 포인트
        KeyPointCard(
            text = "긴 숫자도 잘리지 않고 완전히 표시됩니다!\n" +
                    "각 카드가 균형 잡힌 레이아웃을 유지합니다."
        )
    }
}

/**
 * Auto-sizing이 적용된 통계 카드
 */
@Composable
private fun SolutionStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    rowWidth: Float
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Auto-sizing 적용!
            key(rowWidth) {
                AutoSizeText(
                    text = value,
                    minFontSize = 12.sp,
                    maxFontSize = 24.sp,
                    step = 1.sp,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 핵심 포인트 카드
 */
@Composable
private fun KeyPointCard(text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

/**
 * API 요약 카드
 */
@Composable
private fun ApiSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "API 요약",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "커스텀 AutoSizeText 사용법:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = """
AutoSizeText(
    text = "텍스트",
    minFontSize = 10.sp,   // 최소 크기
    maxFontSize = 24.sp,   // 최대 크기
    step = 1.sp,           // 조절 단위
    style = TextStyle(...),
    modifier = Modifier.fillMaxWidth()
)
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Compose 1.8+ (BOM 2025.04.01+) 사용법:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = """
BasicText(
    text = "텍스트",
    maxLines = 1,
    autoSize = TextAutoSize.StepBased(
        minFontSize = 10.sp,
        maxFontSize = 24.sp,
        stepGranularity = 1.sp
    ),
    modifier = Modifier.fillMaxWidth()
)
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "파라미터 가이드:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "- minFontSize: 가독성 보장을 위한 최소 크기\n" +
                        "- maxFontSize: 시각적 임팩트를 위한 최대 크기\n" +
                        "- step: 작을수록 정밀, 클수록 빠름",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "주의사항:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "- 컨테이너 크기가 정해져 있어야 함 (fillMaxWidth 등)\n" +
                        "- 크기 변경 시 key()로 재계산 트리거 권장\n" +
                        "- Compose 1.8+에서는 공식 TextAutoSize API 사용 권장",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
