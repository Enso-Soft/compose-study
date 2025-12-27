# Text & Typography 심화 학습

## 개념

**Text & Typography 심화**는 Jetpack Compose에서 복잡한 텍스트 스타일링을 구현하는 기술입니다.
단일 `Text` 컴포저블 내에서 여러 스타일을 혼합하고, 텍스트 내에 아이콘을 삽입하며, 특정 부분만 클릭 가능하게 만들 수 있습니다.

```kotlin
// 부분 스타일링 예제
val styledText = buildAnnotatedString {
    append("안녕하세요, ")
    withStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
        append("홍길동")
    }
    append("님!")
}
Text(text = styledText)
```

---

## 핵심 기술 6가지

| 기술 | 설명 | 사용 시점 |
|------|------|----------|
| **AnnotatedString** | 부분별 스타일이 적용된 문자열 | 여러 스타일 혼합 |
| **buildAnnotatedString** | 타입 안전한 AnnotatedString 빌더 | AnnotatedString 생성 |
| **InlineTextContent** | 텍스트 내 Composable 삽입 | 아이콘/이미지 삽입 |
| **LinkAnnotation** | 텍스트 부분 클릭 처리 | 약관 링크, 멘션 클릭 |
| **TextMeasurer** | 텍스트 크기 측정 | Canvas에서 배경 그리기 |
| **FontFamily/Typography** | 폰트 스타일 설정 | 커스텀 폰트 적용 |

---

## 문제 상황: Row로 스타일링의 한계

### 잘못된 접근법

```kotlin
// 문제 1: Row로 부분 스타일링
Row {
    Text("검색 결과 ")
    Text("15건", color = Color.Blue, fontWeight = FontWeight.Bold)
    Text("이 발견되었습니다.")
}
// 문제: 줄바꿈이 자연스럽지 않음!
```

```kotlin
// 문제 2: Row로 아이콘 삽입
Row(verticalAlignment = CenterVertically) {
    Text("평점: ")
    Icon(Icons.Default.Star, ...)
    Text(" 4.5점")
}
// 문제: Baseline 정렬이 맞지 않음, 줄바꿈 시 분리됨!
```

```kotlin
// 문제 3: 전체 클릭
Text(
    text = "이용약관에 동의합니다",
    modifier = Modifier.clickable { }
)
// 문제: 전체가 클릭됨! '이용약관'만 클릭하고 싶음
```

### 발생하는 문제점

1. **줄바꿈 문제**: Row는 한 줄로만 배치되어 텍스트가 잘림
2. **Baseline 정렬**: 아이콘과 텍스트의 baseline이 맞지 않음
3. **클릭 영역**: 전체 또는 분리된 영역만 클릭 가능
4. **성능**: 여러 Composable 사용으로 오버헤드 발생

---

## 해결책 1: buildAnnotatedString

단일 `Text` 내에서 `SpanStyle`로 부분별 스타일을 적용합니다.

```kotlin
val annotatedText = buildAnnotatedString {
    append("검색 결과 ")

    withStyle(SpanStyle(
        color = Color.Blue,
        fontWeight = FontWeight.Bold
    )) {
        append("15건")
    }

    append("이 발견되었습니다.")
}

Text(text = annotatedText)
```

### SpanStyle vs ParagraphStyle

| SpanStyle | ParagraphStyle |
|-----------|----------------|
| 인라인 스타일 (색상, 굵기, 크기) | 단락 스타일 (정렬, 들여쓰기) |
| `withStyle(SpanStyle(...))` | `withStyle(ParagraphStyle(...))` |
| 단어/문자 단위 | 문단 단위 |

```kotlin
// SpanStyle 예제
withStyle(SpanStyle(
    color = Color.Red,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    fontStyle = FontStyle.Italic,
    textDecoration = TextDecoration.Underline,
    background = Color.Yellow
)) {
    append("강조 텍스트")
}
```

---

## 해결책 2: InlineTextContent

텍스트 흐름 내에 아이콘이나 이미지를 삽입합니다.

```kotlin
// 1. InlineContent 맵 정의
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
            tint = Color.Yellow
        )
    }
)

// 2. buildAnnotatedString에서 placeholder 삽입
val ratingText = buildAnnotatedString {
    append("평점: ")
    repeat(5) {
        appendInlineContent("star", "[star]")
    }
    append(" 4.5점")
}

// 3. Text에 inlineContent 전달
Text(
    text = ratingText,
    inlineContent = inlineContent
)
```

### PlaceholderVerticalAlign 옵션

| 옵션 | 설명 |
|------|------|
| `TextTop` | 텍스트 상단에 정렬 |
| `TextBottom` | 텍스트 하단에 정렬 |
| `TextCenter` | 텍스트 중앙에 정렬 |
| `AboveBaseline` | Baseline 위에 정렬 |
| `Top` | 라인 상단에 정렬 |
| `Bottom` | 라인 하단에 정렬 |
| `Center` | 라인 중앙에 정렬 |

---

## 해결책 3: LinkAnnotation (2024년 권장)

`ClickableText` 대신 `LinkAnnotation`을 사용하여 부분 클릭을 구현합니다.

### URL 링크

```kotlin
val urlText = buildAnnotatedString {
    append("자세한 내용은 ")

    withLink(
        LinkAnnotation.Url(
            url = "https://developer.android.com",
            styles = TextLinkStyles(
                style = SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            )
        )
    ) {
        append("공식 문서")
    }

    append("를 참고하세요.")
}

Text(text = urlText)  // 클릭 시 자동으로 URL 열림
```

### Clickable 링크 (커스텀 동작)

```kotlin
val clickableText = buildAnnotatedString {
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
                // 커스텀 동작
                showTermsDialog()
            }
        )
    ) {
        append("이용약관")
    }

    append("에 동의합니다.")
}

Text(text = clickableText)
```

### ClickableText vs LinkAnnotation

| ClickableText (Deprecated) | LinkAnnotation (권장) |
|---------------------------|----------------------|
| `onClick: (Int) -> Unit` | `linkInteractionListener` |
| 수동으로 annotation 확인 | 자동으로 링크 처리 |
| 별도 Composable 필요 | 일반 Text 사용 |

---

## 해결책 4: TextMeasurer

텍스트 크기를 측정하여 Canvas에서 배경을 그리거나 레이아웃을 계산합니다.

```kotlin
val textMeasurer = rememberTextMeasurer()
val density = LocalDensity.current

val textLayoutResult = remember(text) {
    textMeasurer.measure(
        text = AnnotatedString(text),
        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        constraints = Constraints.fixedWidth(maxWidth)
    )
}

// 측정된 크기 사용
val textWidthDp = with(density) { textLayoutResult.size.width.toDp() }
val textHeightDp = with(density) { textLayoutResult.size.height.toDp() }

// Canvas에서 배경 + 텍스트 그리기
Canvas(modifier = Modifier.size(textWidthDp + 16.dp, textHeightDp + 8.dp)) {
    drawRect(color = Color.Yellow)  // 배경
    drawText(textLayoutResult, topLeft = Offset(8.dp.toPx(), 4.dp.toPx()))  // 텍스트
}
```

### TextLayoutResult 주요 속성

| 속성 | 설명 |
|------|------|
| `size` | 텍스트 전체 크기 (IntSize) |
| `lineCount` | 줄 수 |
| `getLineTop(line)` | 특정 줄 상단 Y 좌표 |
| `getLineBottom(line)` | 특정 줄 하단 Y 좌표 |
| `getOffsetForPosition(position)` | 위치에서 문자 인덱스 |

---

## 해결책 5: 커스텀 폰트 & Typography

### 기본 FontFamily

```kotlin
Text("Default 폰트", fontFamily = FontFamily.Default)
Text("Serif 폰트", fontFamily = FontFamily.Serif)
Text("SansSerif 폰트", fontFamily = FontFamily.SansSerif)
Text("Monospace 폰트", fontFamily = FontFamily.Monospace)
Text("Cursive 폰트", fontFamily = FontFamily.Cursive)
```

### 커스텀 폰트 사용

```kotlin
// 1. res/font 폴더에 폰트 파일 추가
// 2. FontFamily 정의
val customFontFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_light, FontWeight.Light)
)

// 3. Typography 정의
val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = customFontFamily,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)

// 4. Theme에서 적용
MaterialTheme(
    typography = CustomTypography
) {
    // 앱 전체에서 커스텀 폰트 사용
}
```

### MaterialTheme.typography 사용

```kotlin
Text("displayLarge", style = MaterialTheme.typography.displayLarge)
Text("headlineMedium", style = MaterialTheme.typography.headlineMedium)
Text("titleLarge", style = MaterialTheme.typography.titleLarge)
Text("bodyLarge", style = MaterialTheme.typography.bodyLarge)
Text("labelSmall", style = MaterialTheme.typography.labelSmall)
```

---

## 사용 시나리오

### 1. SNS 게시글 (해시태그 + 멘션)

```kotlin
val postText = buildAnnotatedString {
    append("오늘 #Compose 공부! ")
    withLink(LinkAnnotation.Clickable("user") { openProfile("kim") }) {
        withStyle(SpanStyle(color = Color.Blue)) {
            append("@김철수")
        }
    }
    append("님 감사합니다 #개발자일상")
}
```

### 2. 약관 동의

```kotlin
val termsText = buildAnnotatedString {
    withLink(LinkAnnotation.Clickable("terms") { showTerms() }) {
        withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("이용약관")
        }
    }
    append(" 및 ")
    withLink(LinkAnnotation.Clickable("privacy") { showPrivacy() }) {
        withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("개인정보처리방침")
        }
    }
    append("에 동의합니다.")
}
```

### 3. 제품 평점

```kotlin
val inlineContent = mapOf(
    "star" to InlineTextContent(
        Placeholder(16.sp, 16.sp, PlaceholderVerticalAlign.TextCenter)
    ) {
        Icon(Icons.Default.Star, null, tint = Color.Yellow)
    }
)

val ratingText = buildAnnotatedString {
    repeat(5) { appendInlineContent("star") }
    append(" ")
    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
        append("4.5")
    }
    append(" (1,234)")
}

Text(ratingText, inlineContent = inlineContent)
```

### 4. 검색 결과 하이라이트

```kotlin
fun highlightSearchQuery(text: String, query: String): AnnotatedString {
    return buildAnnotatedString {
        var lastIndex = 0
        var matchIndex = text.indexOf(query, 0, ignoreCase = true)

        while (matchIndex >= 0) {
            append(text.substring(lastIndex, matchIndex))
            withStyle(SpanStyle(background = Color.Yellow, fontWeight = FontWeight.Bold)) {
                append(text.substring(matchIndex, matchIndex + query.length))
            }
            lastIndex = matchIndex + query.length
            matchIndex = text.indexOf(query, lastIndex, ignoreCase = true)
        }

        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
}
```

---

## 주의사항

### 1. 성능 최적화

```kotlin
// 나쁜 예: 매 composition마다 재생성
Text(buildAnnotatedString { ... })

// 좋은 예: remember로 캐싱
val annotatedText = remember(text, query) {
    buildAnnotatedString { ... }
}
Text(annotatedText)
```

### 2. InlineContent 크기 지정

```kotlin
// Placeholder 크기는 sp 단위 사용
Placeholder(
    width = 16.sp,   // dp가 아닌 sp!
    height = 16.sp,
    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
)
```

### 3. LinkAnnotation 접근성

```kotlin
// 링크에 의미 있는 텍스트 사용
withLink(LinkAnnotation.Url("https://example.com")) {
    append("여기를 클릭하세요")  // X
    append("공식 문서 보기")     // O
}
```

### 4. TextMeasurer 캐싱

```kotlin
// textMeasurer.measure()는 비용이 높으므로 remember 사용
val textLayoutResult = remember(text, style) {
    textMeasurer.measure(AnnotatedString(text), style)
}
```

---

## 연습 문제

### 연습 1: 해시태그 스타일링 (초급)

트윗에서 `#해시태그`만 파란색 + 굵게 표시하세요.

**힌트**: `Regex("#\\S+").findAll(text)`

### 연습 2: 멘션 클릭 (중급)

`@사용자명` 클릭 시 Toast로 사용자 정보를 표시하세요.

**힌트**: `LinkAnnotation.Clickable`

### 연습 3: 검색 하이라이트 (고급)

검색어와 일치하는 부분에 노란 배경을 적용하세요.

**힌트**: `indexOf(query, startIndex, ignoreCase = true)`

---

## 다음 학습

- **Brush로 그라디언트 텍스트**: `TextStyle(brush = Brush.linearGradient(...))`
- **SelectionContainer**: 텍스트 선택 가능하게 만들기
- **BasicTextField2**: 고급 텍스트 입력 커스터마이징
- **AutoResizing Text**: 컨테이너에 맞게 텍스트 크기 자동 조절

---

## 참고 자료

- [Style text - Android Developers](https://developer.android.com/develop/ui/compose/text/style-text)
- [Work with fonts - Android Developers](https://developer.android.com/develop/ui/compose/text/fonts)
- [InlineTextContent API Reference](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/InlineTextContent)
- [AnnotatedString API Reference](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString)
