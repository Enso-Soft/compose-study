package com.example.rich_text

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Rich Text 연습 문제
 *
 * 3개의 연습 문제를 통해 AnnotatedString, InlineTextContent, LinkAnnotation을 직접 구현합니다.
 *
 * 학습자가 직접 TODO 부분을 채워 넣으며 학습합니다.
 * 정답 보기 버튼으로 정답을 확인할 수 있습니다.
 */

// ============================================================
// 연습 1: 강조 텍스트 만들기 (쉬움)
// ============================================================

/**
 * 연습 1: 강조 텍스트 만들기
 *
 * 목표: 문장에서 특정 키워드를 파란색 + 굵게 표시하기
 *
 * 시나리오:
 * "Jetpack Compose는 안드로이드의 최신 UI 프레임워크입니다."
 * → "Jetpack Compose"를 파란색 + 굵게 표시
 *
 * TODO:
 * 1. text에서 keyword 위치 찾기: text.indexOf(keyword)
 * 2. keyword 이전 텍스트 append
 * 3. withStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold))로 keyword append
 * 4. keyword 이후 텍스트 append
 *
 * 힌트:
 * - indexOf로 위치 찾기
 * - substring으로 분리
 * - withStyle(SpanStyle(...)) 사용
 */
@Composable
fun Practice1_HighlightScreen() {
    val text = "Jetpack Compose는 안드로이드의 최신 UI 프레임워크입니다."
    val keyword = "Jetpack Compose"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 1: 강조 텍스트 만들기 (쉬움)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "목표: \"$keyword\"를 파란색 + 굵게 표시\n\n" +
                            "힌트:\n" +
                            "- text.indexOf(keyword)로 위치 찾기\n" +
                            "- text.substring(start, end)로 분리\n" +
                            "- withStyle(SpanStyle(...)) { append(...) }",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 원본 텍스트
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "원본 텍스트:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = text, fontSize = 16.sp)
            }
        }

        // TODO: 아래 코드를 수정하세요
        // -----------------------------------------------

        val styledText = buildAnnotatedString {
            // TODO: keyword 부분만 스타일 적용하기
            //
            // 구현 단계:
            // 1. val keywordIndex = text.indexOf(keyword)
            // 2. append(text.substring(0, keywordIndex))  // keyword 이전
            // 3. withStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
            //        append(keyword)
            //    }
            // 4. append(text.substring(keywordIndex + keyword.length))  // keyword 이후

            // 임시 코드 (삭제하고 위 단계대로 구현하세요!)
            append(text)
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "결과:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = styledText, fontSize = 16.sp)
            }
        }

        // -----------------------------------------------

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice1_Answer(text, keyword)
        }
    }
}

@Composable
private fun Practice1_Answer(text: String, keyword: String) {
    val keywordIndex = text.indexOf(keyword)

    val styledText = buildAnnotatedString {
        // keyword 이전 텍스트
        append(text.substring(0, keywordIndex))

        // keyword에 스타일 적용
        withStyle(
            SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(keyword)
        }

        // keyword 이후 텍스트
        append(text.substring(keywordIndex + keyword.length))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "정답:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = styledText, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
                    val keywordIndex = text.indexOf(keyword)

                    buildAnnotatedString {
                        append(text.substring(0, keywordIndex))
                        withStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                            append(keyword)
                        }
                        append(text.substring(keywordIndex + keyword.length))
                    }
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}


// ============================================================
// 연습 2: 인라인 아이콘 삽입 (중간)
// ============================================================

/**
 * 연습 2: 인라인 아이콘 삽입
 *
 * 목표: 텍스트 내에 경고 아이콘과 별 아이콘을 삽입하기
 *
 * 시나리오:
 * "[경고] 주의: 이 상품은 [별][별][별] 인기 상품입니다!"
 *
 * TODO:
 * 1. inlineContent 맵 정의 (warning, star)
 * 2. buildAnnotatedString에서 appendInlineContent 사용
 * 3. Text에 inlineContent 전달
 *
 * 힌트:
 * - InlineTextContent(placeholder = Placeholder(...)) { Icon(...) }
 * - PlaceholderVerticalAlign.TextCenter
 * - appendInlineContent("id", "[alt]")
 */
@Composable
fun Practice2_InlineIconScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 2: 인라인 아이콘 삽입 (중간)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "목표: 텍스트 내에 경고 아이콘과 별 아이콘 삽입\n\n" +
                            "힌트:\n" +
                            "- InlineTextContent(placeholder = Placeholder(...))\n" +
                            "- PlaceholderVerticalAlign.TextCenter\n" +
                            "- appendInlineContent(\"id\", \"[alt]\")",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // TODO: 아래 코드를 수정하세요
        // -----------------------------------------------

        // TODO: inlineContent 맵 정의
        val inlineContent = mapOf<String, InlineTextContent>(
            // TODO: "warning" 키로 경고 아이콘 정의
            // "warning" to InlineTextContent(
            //     placeholder = Placeholder(
            //         width = 16.sp,
            //         height = 16.sp,
            //         placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            //     )
            // ) {
            //     Icon(
            //         imageVector = Icons.Default.Warning,
            //         contentDescription = null,
            //         tint = Color(0xFFFF9800),
            //         modifier = Modifier.fillMaxSize()
            //     )
            // },

            // TODO: "star" 키로 별 아이콘 정의
            // "star" to InlineTextContent(...) { Icon(Icons.Default.Star, ...) }
        )

        // TODO: buildAnnotatedString으로 텍스트 구성
        val annotatedText = buildAnnotatedString {
            // TODO: 다음 순서로 구성하세요
            // 1. appendInlineContent("warning", "[warning]")
            // 2. append(" 주의: 이 상품은 ")
            // 3. repeat(3) { appendInlineContent("star", "[star]") }
            // 4. append(" 인기 상품입니다!")

            // 임시 코드 (삭제하고 위 단계대로 구현하세요!)
            append("주의: 이 상품은 *** 인기 상품입니다!")
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "결과:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = annotatedText,
                    fontSize = 16.sp,
                    inlineContent = inlineContent
                )
            }
        }

        // -----------------------------------------------

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice2_Answer()
        }
    }
}

@Composable
private fun Practice2_Answer() {
    val inlineContent = mapOf(
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
        },
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
        }
    )

    val annotatedText = buildAnnotatedString {
        appendInlineContent("warning", "[warning]")
        append(" 주의: 이 상품은 ")
        repeat(3) {
            appendInlineContent("star", "[star]")
        }
        append(" 인기 상품입니다!")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "정답:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = annotatedText,
                fontSize = 16.sp,
                inlineContent = inlineContent
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
                    val inlineContent = mapOf(
                        "warning" to InlineTextContent(
                            placeholder = Placeholder(16.sp, 16.sp, TextCenter)
                        ) { Icon(Icons.Default.Warning, tint = Orange) },
                        "star" to InlineTextContent(
                            placeholder = Placeholder(16.sp, 16.sp, TextCenter)
                        ) { Icon(Icons.Default.Star, tint = Yellow) }
                    )

                    buildAnnotatedString {
                        appendInlineContent("warning", "[warning]")
                        append(" 주의: 이 상품은 ")
                        repeat(3) { appendInlineContent("star", "[star]") }
                        append(" 인기 상품입니다!")
                    }
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}


// ============================================================
// 연습 3: 클릭 가능한 링크 텍스트 (어려움)
// ============================================================

/**
 * 연습 3: 클릭 가능한 링크 텍스트
 *
 * 목표: "이용약관"과 "개인정보처리방침" 클릭 시 각각 다른 Toast 표시
 *
 * 시나리오:
 * "서비스 [이용약관] 및 [개인정보처리방침]에 동의합니다."
 *
 * TODO:
 * 1. buildAnnotatedString에서 withLink 사용
 * 2. LinkAnnotation.Clickable로 클릭 처리
 * 3. TextLinkStyles로 스타일 정의
 * 4. linkInteractionListener로 Toast 표시
 *
 * 힌트:
 * - withLink(LinkAnnotation.Clickable(tag, styles, listener)) { append(...) }
 * - TextLinkStyles(style = SpanStyle(color, textDecoration))
 * - Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
 */
@Composable
fun Practice3_ClickableLinkScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 3: 클릭 가능한 링크 텍스트 (어려움)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "목표: \"이용약관\"과 \"개인정보처리방침\" 클릭 시 Toast 표시\n\n" +
                            "힌트:\n" +
                            "- withLink(LinkAnnotation.Clickable(...))\n" +
                            "- TextLinkStyles(style = SpanStyle(...))\n" +
                            "- linkInteractionListener = { Toast... }",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // TODO: 아래 코드를 수정하세요
        // -----------------------------------------------

        val termsText = buildAnnotatedString {
            // TODO: 다음 순서로 구성하세요

            // 1. append("서비스 ")

            // 2. "이용약관"에 LinkAnnotation.Clickable 적용
            // withLink(
            //     LinkAnnotation.Clickable(
            //         tag = "terms",
            //         styles = TextLinkStyles(
            //             style = SpanStyle(
            //                 color = Color.Blue,
            //                 textDecoration = TextDecoration.Underline
            //             )
            //         ),
            //         linkInteractionListener = {
            //             Toast.makeText(context, "이용약관 클릭!", Toast.LENGTH_SHORT).show()
            //         }
            //     )
            // ) {
            //     append("이용약관")
            // }

            // 3. append(" 및 ")

            // 4. "개인정보처리방침"에 LinkAnnotation.Clickable 적용
            // (위와 동일한 패턴, tag와 메시지만 변경)

            // 5. append("에 동의합니다.")

            // 임시 코드 (삭제하고 위 단계대로 구현하세요!)
            append("서비스 이용약관 및 개인정보처리방침에 동의합니다.")
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "결과 (링크를 클릭해보세요):",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = termsText, fontSize = 16.sp)
            }
        }

        // -----------------------------------------------

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice3_Answer(context)
        }
    }
}

@Composable
private fun Practice3_Answer(context: android.content.Context) {
    val termsText = buildAnnotatedString {
        append("서비스 ")

        withLink(
            LinkAnnotation.Clickable(
                tag = "terms",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
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
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
                    Toast.makeText(context, "개인정보처리방침 클릭!", Toast.LENGTH_SHORT).show()
                }
            )
        ) {
            append("개인정보처리방침")
        }

        append("에 동의합니다.")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "정답 (클릭 가능):",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = termsText, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
                    buildAnnotatedString {
                        append("서비스 ")
                        withLink(
                            LinkAnnotation.Clickable(
                                tag = "terms",
                                styles = TextLinkStyles(
                                    style = SpanStyle(color = Blue, textDecoration = Underline)
                                ),
                                linkInteractionListener = {
                                    Toast.makeText(context, "이용약관 클릭!", SHORT).show()
                                }
                            )
                        ) { append("이용약관") }
                        append(" 및 ")
                        withLink(...) { append("개인정보처리방침") }
                        append("에 동의합니다.")
                    }
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}
