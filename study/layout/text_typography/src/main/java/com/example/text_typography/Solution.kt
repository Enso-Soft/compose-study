package com.example.text_typography

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Text & Typography 해결책
 *
 * Row를 사용한 부분 스타일링의 한계를 해결하는 5가지 기술을 소개합니다.
 *
 * ## 문제-해결 매핑
 * | 문제 | 해결책 |
 * |------|--------|
 * | 줄바꿈 불가 | buildAnnotatedString |
 * | Baseline 정렬 | InlineTextContent |
 * | 부분 클릭 불가 | LinkAnnotation |
 * | 텍스트 크기 측정 | TextMeasurer (추가 기법) |
 * | 폰트 통일 | Typography (추가 기법) |
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
        // 해결책 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: AnnotatedString & Typography 심화",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. buildAnnotatedString으로 부분 스타일링\n" +
                            "2. InlineTextContent로 텍스트 내 아이콘\n" +
                            "3. LinkAnnotation으로 부분 클릭\n" +
                            "4. TextMeasurer로 텍스트 측정\n" +
                            "5. 커스텀 폰트 및 Typography 설정",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        HorizontalDivider()

        // 해결책 1: buildAnnotatedString
        Solution1_BuildAnnotatedString()

        HorizontalDivider()

        // 해결책 2: InlineTextContent
        Solution2_InlineContent()

        HorizontalDivider()

        // 해결책 3: LinkAnnotation
        Solution3_LinkAnnotation()

        HorizontalDivider()

        // 해결책 4: TextMeasurer
        Solution4_TextMeasurer()

        HorizontalDivider()

        // 해결책 5: 커스텀 폰트
        Solution5_CustomFonts()
    }
}

/**
 * 해결책 1: buildAnnotatedString으로 부분 스타일링
 *
 * [해결하는 문제] Row 사용 시 자연스럽지 않은 줄바꿈 문제
 * [핵심 기술] SpanStyle, ParagraphStyle, withStyle
 *
 * 단일 Text 컴포저블 내에서 여러 스타일을 혼합합니다.
 * 자연스러운 줄바꿈이 가능합니다.
 */
@Composable
private fun Solution1_BuildAnnotatedString() {
    var searchCount by remember { mutableIntStateOf(15) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 1: buildAnnotatedString",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // 올바른 방법: buildAnnotatedString 사용
                val annotatedText = buildAnnotatedString {
                    append("검색 결과 ")

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("${searchCount}건")
                    }

                    append("이 발견되었습니다. 원하시는 항목을 선택하세요.")
                }

                Text(
                    text = annotatedText,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 복잡한 스타일 예제
                Text(
                    text = "복잡한 스타일 혼합:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                val complexText = buildAnnotatedString {
                    append("안녕하세요, ")

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("홍길동")
                    }

                    append("님! 오늘 ")

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                            background = Color.Yellow.copy(alpha = 0.3f)
                        )
                    ) {
                        append("3개")
                    }

                    append("의 새로운 알림이 있습니다. ")

                    withStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("지금 확인해보세요!")
                    }
                }

                Text(
                    text = complexText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { searchCount += 10 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("증가")
                    }
                    OutlinedButton(
                        onClick = { searchCount = 15 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("초기화")
                    }
                }
            }
        }

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    text = "핵심: withStyle(SpanStyle(...)) { append(...) } 로 " +
                            "부분별 스타일 적용. 단일 Text로 자연스러운 줄바꿈!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 해결책 2: InlineTextContent로 텍스트 내 아이콘 삽입
 *
 * [해결하는 문제] Row로 아이콘 삽입 시 Baseline 정렬 불일치 문제
 * [핵심 기술] InlineTextContent, Placeholder, PlaceholderVerticalAlign
 *
 * appendInlineContent와 InlineTextContent를 사용하여
 * 텍스트 흐름 내에 아이콘을 자연스럽게 삽입합니다.
 */
@Composable
private fun Solution2_InlineContent() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 2: InlineTextContent",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // InlineContent 맵 정의
                val inlineContent = mapOf(
                    "star" to InlineTextContent(
                        placeholder = Placeholder(
                            width = 16.sp,
                            height = 16.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    "heart" to InlineTextContent(
                        placeholder = Placeholder(
                            width = 14.sp,
                            height = 14.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    "warning" to InlineTextContent(
                        placeholder = Placeholder(
                            width = 16.sp,
                            height = 16.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                )

                // 별점 표시
                val ratingText = buildAnnotatedString {
                    append("평점: ")
                    repeat(5) {
                        appendInlineContent("star", "[star]")
                    }
                    append(" ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("4.5점")
                    }
                    append(" (1,234명 평가)")
                }

                Text(
                    text = "별점 표시 예제:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = ratingText,
                    fontSize = 16.sp,
                    inlineContent = inlineContent
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 혼합 아이콘 예제
                val mixedText = buildAnnotatedString {
                    appendInlineContent("warning", "[warning]")
                    append(" 주의: 이 상품은 ")
                    appendInlineContent("heart", "[heart]")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
                        append("1,234명")
                    }
                    append("이 찜했습니다! ")
                    repeat(3) {
                        appendInlineContent("star", "[star]")
                    }
                    append(" 인기 상품")
                }

                Text(
                    text = "복합 아이콘 예제:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = mixedText,
                    fontSize = 14.sp,
                    inlineContent = inlineContent
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 좁은 화면에서도 자연스러운 줄바꿈
                Text(
                    text = "좁은 화면 시뮬레이션 (width = 200dp):",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Text(
                        text = mixedText,
                        fontSize = 14.sp,
                        inlineContent = inlineContent
                    )
                }
            }
        }

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    text = "핵심: appendInlineContent(\"id\")로 placeholder 삽입, " +
                            "inlineContent 맵에서 Composable 정의. " +
                            "PlaceholderVerticalAlign으로 정렬!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 해결책 3: LinkAnnotation으로 부분 클릭
 *
 * [해결하는 문제] 텍스트 특정 부분만 클릭하고 싶은데 전체가 클릭되는 문제
 * [핵심 기술] LinkAnnotation.Url, LinkAnnotation.Clickable, TextLinkStyles
 *
 * 2024년부터 권장되는 방식으로, ClickableText(deprecated) 대신
 * LinkAnnotation을 사용합니다.
 */
@Composable
private fun Solution3_LinkAnnotation() {
    val context = LocalContext.current
    var clickedItem by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 3: LinkAnnotation (2024+ 권장)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // URL 링크 예제
                Text(
                    text = "1. URL 링크:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                val urlText = buildAnnotatedString {
                    append("자세한 내용은 ")

                    withLink(
                        LinkAnnotation.Url(
                            url = "https://developer.android.com",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        )
                    ) {
                        append("Android 공식 문서")
                    }

                    append("를 참고하세요.")
                }

                Text(
                    text = urlText,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Clickable 링크 예제
                Text(
                    text = "2. Clickable 링크 (커스텀 동작):",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                val clickableText = buildAnnotatedString {
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "terms",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ),
                            linkInteractionListener = {
                                clickedItem = "이용약관"
                                Toast.makeText(context, "이용약관 클릭!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    ) {
                        append("이용약관")
                    }

                    append(" 및 ")

                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "privacy",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ),
                            linkInteractionListener = {
                                clickedItem = "개인정보처리방침"
                                Toast.makeText(context, "개인정보처리방침 클릭!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    ) {
                        append("개인정보처리방침")
                    }

                    append("에 동의합니다.")
                }

                Text(
                    text = clickableText,
                    fontSize = 16.sp
                )

                if (clickedItem.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "마지막 클릭: $clickedItem",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 멘션 예제
                Text(
                    text = "3. 멘션 클릭:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                val mentionText = buildAnnotatedString {
                    append("좋은 글이네요! ")

                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "user_kim",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = Color(0xFF1DA1F2),
                                    fontWeight = FontWeight.Bold
                                )
                            ),
                            linkInteractionListener = {
                                Toast.makeText(context, "김철수 프로필로 이동", Toast.LENGTH_SHORT).show()
                            }
                        )
                    ) {
                        append("@김철수")
                    }

                    append("님 ")

                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "user_lee",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = Color(0xFF1DA1F2),
                                    fontWeight = FontWeight.Bold
                                )
                            ),
                            linkInteractionListener = {
                                Toast.makeText(context, "이영희 프로필로 이동", Toast.LENGTH_SHORT).show()
                            }
                        )
                    ) {
                        append("@이영희")
                    }

                    append("님도 확인해보세요!")
                }

                Text(
                    text = mentionText,
                    fontSize = 14.sp
                )
            }
        }

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    text = "핵심: withLink(LinkAnnotation.Clickable/Url) 사용. " +
                            "TextLinkStyles로 스타일 정의, linkInteractionListener로 클릭 처리!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 해결책 4: TextMeasurer로 텍스트 크기 측정 (추가 기법)
 *
 * [용도] Canvas에서 텍스트 배경 그리기, 레이아웃 계산
 * [핵심 기술] rememberTextMeasurer, TextLayoutResult, drawText
 *
 * rememberTextMeasurer()로 텍스트 크기를 측정하고
 * Canvas에서 배경 그리기 등에 활용합니다.
 */
@Composable
private fun Solution4_TextMeasurer() {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 4: TextMeasurer",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "텍스트 크기 측정 후 배경 그리기:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 하이라이트 텍스트
                val highlightText = "SALE"
                val textLayoutResult = remember(highlightText) {
                    textMeasurer.measure(
                        text = AnnotatedString(highlightText),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Canvas에서 배경 + 텍스트 그리기
                val textWidthDp = with(density) { textLayoutResult.size.width.toDp() }
                val textHeightDp = with(density) { textLayoutResult.size.height.toDp() }

                Canvas(
                    modifier = Modifier
                        .size(textWidthDp + 16.dp, textHeightDp + 8.dp)
                ) {
                    // 노란 배경 그리기
                    drawRect(
                        color = Color.Yellow,
                        size = size
                    )

                    // 텍스트 그리기
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(8.dp.toPx(), 4.dp.toPx())
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 동적 텍스트 측정
                var dynamicText by remember { mutableStateOf("동적 텍스트") }

                Text(
                    text = "동적 텍스트 측정:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                TextField(
                    value = dynamicText,
                    onValueChange = { dynamicText = it },
                    label = { Text("텍스트 입력") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                val dynamicLayoutResult = remember(dynamicText) {
                    textMeasurer.measure(
                        text = AnnotatedString(dynamicText),
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    )
                }

                val dynamicWidthDp = with(density) { dynamicLayoutResult.size.width.toDp() }
                val dynamicHeightDp = with(density) { dynamicLayoutResult.size.height.toDp() }

                Text(
                    text = "측정된 크기: ${dynamicLayoutResult.size.width}px x ${dynamicLayoutResult.size.height}px",
                    style = MaterialTheme.typography.bodySmall
                )

                if (dynamicText.isNotEmpty()) {
                    Canvas(
                        modifier = Modifier
                            .size(dynamicWidthDp + 16.dp, dynamicHeightDp + 8.dp)
                    ) {
                        drawRect(
                            color = Color.Cyan.copy(alpha = 0.5f),
                            size = size
                        )
                        drawText(
                            textLayoutResult = dynamicLayoutResult,
                            topLeft = Offset(8.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }
            }
        }

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    text = "핵심: rememberTextMeasurer()로 인스턴스 획득, " +
                            "measure()로 TextLayoutResult 반환, " +
                            "drawText()로 Canvas에 그리기!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 해결책 5: 커스텀 폰트 및 Typography (추가 기법)
 *
 * [용도] 앱 전체에 일관된 폰트 스타일 적용
 * [핵심 기술] FontFamily, Font, Typography, MaterialTheme.typography
 *
 * FontFamily를 활용한 다양한 폰트 스타일과
 * Typography 설정 방법을 보여줍니다.
 */
@Composable
private fun Solution5_CustomFonts() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "해결책 5: 커스텀 폰트 & Typography",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "기본 제공 FontFamily:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 다양한 FontFamily 예제
                Text(
                    text = "Default: 기본 폰트입니다",
                    fontFamily = FontFamily.Default,
                    fontSize = 16.sp
                )

                Text(
                    text = "Serif: 세리프 폰트입니다",
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp
                )

                Text(
                    text = "SansSerif: 산세리프 폰트입니다",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp
                )

                Text(
                    text = "Monospace: 고정폭 폰트입니다",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp
                )

                Text(
                    text = "Cursive: 필기체 폰트입니다",
                    fontFamily = FontFamily.Cursive,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "MaterialTheme.typography 활용:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "displayLarge",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "headlineMedium",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "titleLarge",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "bodyLarge - 본문에 주로 사용",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "labelSmall",
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                // buildAnnotatedString에서 폰트 혼합
                Text(
                    text = "폰트 혼합 예제:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                val mixedFontText = buildAnnotatedString {
                    withStyle(SpanStyle(fontFamily = FontFamily.Default)) {
                        append("기본 폰트와 ")
                    }
                    withStyle(SpanStyle(fontFamily = FontFamily.Monospace, color = Color.Blue)) {
                        append("코드 폰트(Monospace)")
                    }
                    withStyle(SpanStyle(fontFamily = FontFamily.Default)) {
                        append("를 혼합하여 ")
                    }
                    withStyle(SpanStyle(fontFamily = FontFamily.Cursive, fontStyle = FontStyle.Italic)) {
                        append("우아한 필기체")
                    }
                    append("도 사용할 수 있습니다.")
                }

                Text(
                    text = mixedFontText,
                    fontSize = 14.sp
                )
            }
        }

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    text = "핵심: FontFamily.Default/Serif/Monospace 등 사용. " +
                            "커스텀 폰트는 res/font에 추가 후 Font()로 로드. " +
                            "Typography로 앱 전체 스타일 통일!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
