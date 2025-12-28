package com.example.auto_sizing_text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * 연습 문제 1: 적응형 상품 카드 제목 (쉬움)
 *
 * 기본 autoSize 사용법을 익힙니다.
 *
 * 과제:
 * - 상품명이 길어도 한 줄에 전체가 표시되어야 합니다.
 * - 카드 크기에 따라 폰트 크기가 자동 조절되어야 합니다.
 * - 최소 폰트: 10.sp, 최대 폰트: 16.sp
 */
@Composable
fun Practice1_ProductCardScreen() {
    var cardWidth by remember { mutableFloatStateOf(280f) }
    val productTitle = "삼성 갤럭시 S25 울트라 512GB 티타늄 블랙 자급제"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 적응형 상품 카드 제목 (쉬움)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "온라인 쇼핑몰의 상품 카드를 만들어보세요.\n" +
                            "상품명이 길어도 한 줄에 표시되어야 하고,\n" +
                            "카드 크기에 따라 폰트 크기가 자동 조절되어야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. AutoSizeText 컴포저블을 사용하세요")
                Text("2. minFontSize, maxFontSize를 설정하세요")
                Text("3. modifier = Modifier.fillMaxWidth()로 너비를 채우세요")
            }
        }

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

        // 연습 영역
        Card(
            modifier = Modifier.width(cardWidth.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                key(cardWidth) {
                    Practice1_Exercise(productTitle)
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

        // 정답 확인 (접힌 상태)
        var showAnswer by remember { mutableStateOf(false) }
        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
AutoSizeText(
    text = productTitle,
    minFontSize = 10.sp,
    maxFontSize = 16.sp,
    style = TextStyle(fontWeight = FontWeight.Bold),
    modifier = Modifier.fillMaxWidth()
)
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise(productTitle: String) {
    // TODO: 여기에 AutoSizeText를 사용하여 구현하세요
    // 요구사항:
    // - minFontSize = 10.sp
    // - maxFontSize = 16.sp
    // - fontWeight = FontWeight.Bold
    // - modifier = Modifier.fillMaxWidth()

    // 아래 Text를 PracticeAutoSizeText로 교체하세요
    Text(
        text = productTitle,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * 연습 문제 2: 세일 가격 배너 (중간)
 *
 * step과 스타일링을 조합합니다.
 *
 * 과제:
 * - 가격이 화면 너비에 맞게 자동 조절되어야 합니다.
 * - step을 사용하여 부드러운 크기 변화를 구현하세요.
 * - 스타일: 굵은 글씨, 흰색, 중앙 정렬
 */
@Composable
fun Practice2_PriceBannerScreen() {
    var bannerWidth by remember { mutableFloatStateOf(250f) }
    val price = "99,900원"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 세일 가격 배너 (중간)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "세일 배너를 만들어보세요.\n" +
                            "가격이 배너 너비에 맞게 자동 조절되어야 합니다.\n" +
                            "step을 사용하여 부드러운 크기 변화를 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "요구사항:\n" +
                            "- 폰트 범위: 20.sp ~ 60.sp\n" +
                            "- 조절 단위: 2.sp\n" +
                            "- 스타일: 굵은 글씨, 흰색, 중앙 정렬",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. step 파라미터를 2.sp로 설정하세요")
                Text("2. textAlign = TextAlign.Center로 중앙 정렬하세요")
            }
        }

        // 배너 너비 조절 슬라이더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("배너 너비:", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = bannerWidth,
                onValueChange = { bannerWidth = it },
                valueRange = 120f..350f,
                modifier = Modifier.weight(1f)
            )
            Text("${bannerWidth.toInt()}dp", style = MaterialTheme.typography.bodySmall)
        }

        // 연습 영역
        Box(
            modifier = Modifier
                .width(bannerWidth.dp)
                .background(Color(0xFFE53935))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            key(bannerWidth) {
                Practice2_Exercise(price)
            }
        }

        // 정답 확인
        var showAnswer by remember { mutableStateOf(false) }
        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
PracticeAutoSizeText(
    text = price,
    minFontSize = 20.sp,
    maxFontSize = 60.sp,
    step = 2.sp,
    style = TextStyle(
        fontWeight = FontWeight.Bold,
        color = Color.White
    ),
    textAlign = TextAlign.Center,
    modifier = Modifier.fillMaxWidth()
)
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise(price: String) {
    // TODO: 여기에 구현하세요
    // 요구사항:
    // - minFontSize = 20.sp
    // - maxFontSize = 60.sp
    // - step = 2.sp
    // - fontWeight = FontWeight.Bold
    // - color = Color.White
    // - textAlign = TextAlign.Center
    // - modifier = Modifier.fillMaxWidth()

    // 아래 Text를 PracticeAutoSizeText로 교체하세요
    Text(
        text = price,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 30.sp
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * 연습 문제 3: 반응형 대시보드 카드 (어려움)
 *
 * 실제 앱 시나리오를 구현합니다.
 *
 * 과제:
 * - 3개의 통계 카드를 가로로 배치하세요.
 * - 각 카드의 숫자가 자동으로 크기 조절되어야 합니다.
 * - Row 너비가 변해도 레이아웃이 유지되어야 합니다.
 */
@Composable
fun Practice3_DashboardScreen() {
    var rowWidth by remember { mutableFloatStateOf(350f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 반응형 대시보드 카드 (어려움)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "대시보드 통계 카드 3개를 가로로 배치하세요.\n" +
                            "각 카드의 숫자가 자동으로 크기 조절되어야 합니다.\n" +
                            "Row 너비가 변해도 레이아웃이 유지되어야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "요구사항:\n" +
                            "- 3개의 통계 카드 (방문자, 주문, 매출)\n" +
                            "- 숫자 폰트: 12.sp ~ 28.sp\n" +
                            "- Row로 균등 배치 (weight)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. Row와 Modifier.weight(1f)로 균등 배치")
                Text("2. 각 카드 컴포저블을 분리하여 재사용")
            }
        }

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

        // 연습 영역
        key(rowWidth) {
            Practice3_Exercise(rowWidth)
        }

        // 정답 확인
        var showAnswer by remember { mutableStateOf(false) }
        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
// StatCard 컴포저블
@Composable
fun StatCard(modifier: Modifier, title: String, value: String) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PracticeAutoSizeText(
                text = value,
                minFontSize = 12.sp,
                maxFontSize = 28.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 12.sp)
        }
    }
}

// 사용
Row(
    modifier = Modifier.width(rowWidth.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    StatCard(Modifier.weight(1f), "방문자", "1,234")
    StatCard(Modifier.weight(1f), "주문", "56,789")
    StatCard(Modifier.weight(1f), "매출", "9억원")
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise(rowWidth: Float) {
    // TODO: 여기에 대시보드 카드 3개를 구현하세요
    // 요구사항:
    // - Row로 균등 배치 (Modifier.weight(1f))
    // - 각 카드: 방문자(1,234), 주문(56,789), 매출(9억원)
    // - 숫자 폰트: minFontSize = 12.sp, maxFontSize = 28.sp
    // - Practice3_StatCard 컴포저블을 활용하세요

    // 아래 Row 안에 Practice3_StatCard를 3개 배치하세요
    Row(
        modifier = Modifier.width(rowWidth.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TODO: 첫 번째 카드 - 방문자

        // TODO: 두 번째 카드 - 주문

        // TODO: 세 번째 카드 - 매출
    }
}

@Composable
private fun Practice3_StatCard(
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
            PracticeAutoSizeText(
                text = value,
                minFontSize = 12.sp,
                maxFontSize = 28.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
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

/**
 * 연습용 Auto-sizing Text 컴포저블
 *
 * 텍스트 크기를 컨테이너에 맞게 자동 조절합니다.
 */
@Composable
private fun PracticeAutoSizeText(
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
