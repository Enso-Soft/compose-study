package com.example.text_typography

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * Text & Typography 연습 문제
 *
 * 3개의 연습 문제를 통해 AnnotatedString, LinkAnnotation, 검색 하이라이트를 직접 구현합니다.
 */

// ============================================================
// 연습 1: 해시태그 스타일링 (초급)
// ============================================================

/**
 * 연습 1: 해시태그 스타일링
 *
 * 목표: 트윗 텍스트에서 #해시태그만 파란색 + 굵게 표시하기
 *
 * TODO:
 * 1. Regex("#\\S+")로 해시태그 위치 찾기
 * 2. buildAnnotatedString으로 AnnotatedString 생성
 * 3. 해시태그 부분만 SpanStyle 적용 (color = Blue, fontWeight = Bold)
 *
 * 힌트:
 * - Regex("#\\S+").findAll(text)로 모든 해시태그 매칭
 * - matchResult.range.first, matchResult.range.last로 위치 확인
 * - withStyle(SpanStyle(...)) { append(...) }로 스타일 적용
 */
@Composable
fun Practice1_HashtagScreen() {
    val tweetText = "오늘 #안드로이드 개발 공부 중! #JetpackCompose 정말 재밌다. #개발자일상 #코틀린 화이팅!"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 1: 해시태그 스타일링 (초급)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "목표: #해시태그만 파란색 + 굵게 표시\n\n" +
                            "힌트:\n" +
                            "- Regex(\"#\\\\S+\").findAll(text)\n" +
                            "- buildAnnotatedString { }\n" +
                            "- withStyle(SpanStyle(color, fontWeight))",
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
                Text(text = tweetText, fontSize = 16.sp)
            }
        }

        // TODO: 아래 코드를 수정하세요
        // -----------------------------------------------

        // 현재는 일반 Text로 표시됩니다.
        // buildAnnotatedString을 사용하여 해시태그만 스타일링하세요.

        val styledText = buildAnnotatedString {
            // TODO: 해시태그를 찾아서 스타일 적용하기
            // 1. Regex로 해시태그 찾기
            // 2. 일반 텍스트와 해시태그 구분하여 append
            // 3. 해시태그에는 withStyle 적용

            // 임시로 전체 텍스트만 추가 (수정 필요!)
            append(tweetText)
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

        // 정답 보기 (주석 해제하여 확인)
        var showAnswer by remember { mutableStateOf(false) }

        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice1_Answer(tweetText)
        }
    }
}

@Composable
private fun Practice1_Answer(tweetText: String) {
    val hashtagRegex = Regex("#\\S+")

    val annotatedText = buildAnnotatedString {
        var lastIndex = 0

        hashtagRegex.findAll(tweetText).forEach { matchResult ->
            // 해시태그 이전 텍스트 추가
            append(tweetText.substring(lastIndex, matchResult.range.first))

            // 해시태그에 스타일 적용
            withStyle(
                SpanStyle(
                    color = Color(0xFF1DA1F2),
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(matchResult.value)
            }

            lastIndex = matchResult.range.last + 1
        }

        // 마지막 해시태그 이후 텍스트 추가
        if (lastIndex < tweetText.length) {
            append(tweetText.substring(lastIndex))
        }
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
            Text(text = annotatedText, fontSize = 16.sp)
        }
    }
}


// ============================================================
// 연습 2: 멘션 클릭 가능하게 만들기 (중급)
// ============================================================

/**
 * 연습 2: 멘션 클릭
 *
 * 목표: @사용자명 부분만 클릭 가능하게 만들고, 클릭 시 Toast 표시
 *
 * TODO:
 * 1. Regex("@\\S+")로 멘션 찾기
 * 2. LinkAnnotation.Clickable 사용
 * 3. 멘션은 파란색 + 굵게, 클릭 시 Toast 표시
 *
 * 힌트:
 * - withLink(LinkAnnotation.Clickable(tag, styles, linkInteractionListener)) { }
 * - TextLinkStyles(style = SpanStyle(...))
 * - Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
 */
@Composable
fun Practice2_MentionScreen() {
    val context = LocalContext.current
    val commentText = "와! @김철수님 코드 리뷰 감사합니다. @이영희님도 확인 부탁드려요! @박지성 선수 멋져요!"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 2: 멘션 클릭 (중급)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "목표: @멘션 클릭 시 Toast 표시\n\n" +
                            "힌트:\n" +
                            "- Regex(\"@\\\\S+\").findAll(text)\n" +
                            "- withLink(LinkAnnotation.Clickable(...))\n" +
                            "- TextLinkStyles(style = SpanStyle(...))",
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
                Text(text = commentText, fontSize = 16.sp)
            }
        }

        // TODO: 아래 코드를 수정하세요
        // -----------------------------------------------

        val clickableText = buildAnnotatedString {
            // TODO: 멘션을 찾아서 LinkAnnotation.Clickable 적용하기
            // 1. Regex로 멘션 찾기
            // 2. 일반 텍스트는 그냥 append
            // 3. 멘션에는 withLink(LinkAnnotation.Clickable(...)) 적용

            // 임시로 전체 텍스트만 추가 (수정 필요!)
            append(commentText)
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "결과 (멘션을 클릭해보세요):",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = clickableText, fontSize = 16.sp)
            }
        }

        // -----------------------------------------------

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice2_Answer(commentText, context)
        }
    }
}

@Composable
private fun Practice2_Answer(commentText: String, context: android.content.Context) {
    val mentionRegex = Regex("@\\S+")

    val annotatedText = buildAnnotatedString {
        var lastIndex = 0

        mentionRegex.findAll(commentText).forEach { matchResult ->
            // 멘션 이전 텍스트 추가
            append(commentText.substring(lastIndex, matchResult.range.first))

            // 멘션에 LinkAnnotation 적용
            val mention = matchResult.value
            withLink(
                LinkAnnotation.Clickable(
                    tag = mention,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = Color(0xFF1DA1F2),
                            fontWeight = FontWeight.Bold
                        )
                    ),
                    linkInteractionListener = {
                        Toast.makeText(context, "${mention} 프로필로 이동!", Toast.LENGTH_SHORT).show()
                    }
                )
            ) {
                append(mention)
            }

            lastIndex = matchResult.range.last + 1
        }

        // 마지막 멘션 이후 텍스트 추가
        if (lastIndex < commentText.length) {
            append(commentText.substring(lastIndex))
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "정답 (멘션 클릭 가능):",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = annotatedText, fontSize = 16.sp)
        }
    }
}


// ============================================================
// 연습 3: 검색어 하이라이트 (고급)
// ============================================================

/**
 * 연습 3: 검색어 하이라이트
 *
 * 목표: 사용자가 입력한 검색어와 일치하는 부분에 노란 배경 적용
 *
 * TODO:
 * 1. 검색어 입력 TextField 구현
 * 2. 본문에서 검색어 위치 찾기 (대소문자 무시 가능)
 * 3. 일치하는 부분에 SpanStyle(background = Color.Yellow) 적용
 *
 * 힌트:
 * - text.indexOf(query, startIndex, ignoreCase = true)
 * - while 루프로 모든 일치 항목 찾기
 * - SpanStyle(background = Color.Yellow)
 */
@Composable
fun Practice3_SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val contentText = "Jetpack Compose는 Android의 최신 UI 툴킷입니다. " +
            "Compose를 사용하면 더 적은 코드로 UI를 작성할 수 있습니다. " +
            "Compose는 선언적 UI 패러다임을 따르며, Kotlin으로 작성됩니다. " +
            "Android 개발의 미래는 Compose입니다!"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 3: 검색어 하이라이트 (고급)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "목표: 검색어와 일치하는 부분 노란 배경\n\n" +
                            "힌트:\n" +
                            "- indexOf(query, startIndex, ignoreCase = true)\n" +
                            "- while 루프로 모든 일치 항목 찾기\n" +
                            "- SpanStyle(background = Color.Yellow)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 검색어 입력
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("검색어 입력") },
            placeholder = { Text("예: Compose, Android") },
            modifier = Modifier.fillMaxWidth()
        )

        // TODO: 아래 코드를 수정하세요
        // -----------------------------------------------

        val highlightedText = buildAnnotatedString {
            // TODO: searchQuery와 일치하는 부분에 노란 배경 적용
            // 1. searchQuery가 비어있으면 그냥 전체 텍스트 추가
            // 2. indexOf로 일치하는 위치 찾기
            // 3. 일치 부분에 SpanStyle(background = Color.Yellow) 적용

            // 임시로 전체 텍스트만 추가 (수정 필요!)
            append(contentText)
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "검색 결과:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = highlightedText, fontSize = 16.sp, lineHeight = 24.sp)
            }
        }

        // -----------------------------------------------

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        Button(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice3_Answer(contentText, searchQuery)
        }
    }
}

@Composable
private fun Practice3_Answer(contentText: String, searchQuery: String) {
    val highlightedText = buildAnnotatedString {
        if (searchQuery.isEmpty()) {
            append(contentText)
        } else {
            var startIndex = 0
            var matchIndex = contentText.indexOf(searchQuery, startIndex, ignoreCase = true)

            while (matchIndex >= 0) {
                // 매칭 이전 텍스트 추가
                append(contentText.substring(startIndex, matchIndex))

                // 매칭 부분에 하이라이트 적용
                withStyle(
                    SpanStyle(
                        background = Color.Yellow,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(contentText.substring(matchIndex, matchIndex + searchQuery.length))
                }

                startIndex = matchIndex + searchQuery.length
                matchIndex = contentText.indexOf(searchQuery, startIndex, ignoreCase = true)
            }

            // 마지막 매칭 이후 텍스트 추가
            if (startIndex < contentText.length) {
                append(contentText.substring(startIndex))
            }
        }
    }

    // 매칭 개수 계산
    val matchCount = if (searchQuery.isEmpty()) 0 else {
        var count = 0
        var index = contentText.indexOf(searchQuery, 0, ignoreCase = true)
        while (index >= 0) {
            count++
            index = contentText.indexOf(searchQuery, index + 1, ignoreCase = true)
        }
        count
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "정답 (${matchCount}개 발견):",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = highlightedText, fontSize = 16.sp, lineHeight = 24.sp)
        }
    }
}
