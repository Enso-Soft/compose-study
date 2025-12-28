# Text & Typography 심화 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | Text, Button, TextField, Icon 기본 사용법 | [📚 학습하기](../../layout/basic_ui_components/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**Text & Typography 심화**는 Jetpack Compose에서 단순한 텍스트 표시를 넘어, 복잡한 스타일링과 인터랙션을 구현하는 기술입니다.

일반적인 `Text` 컴포저블로는 구현하기 어려운 다음과 같은 요구사항을 해결합니다:
- 텍스트 일부분만 다른 스타일 적용 (색상, 굵기, 크기 등)
- 텍스트 흐름 내에 아이콘이나 이미지 삽입
- 텍스트 특정 부분만 클릭 가능하게 만들기
- 텍스트 크기를 측정하여 배경 그리기
- 앱 전체에 일관된 폰트 스타일 적용

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

## 문제 상황: Row로 스타일링할 때의 한계

### 왜 이 기술이 필요한가?

직관적으로 "텍스트 일부분만 스타일을 다르게 하고 싶다"면, 여러 `Text`를 `Row`로 묶는 방법을 떠올릴 수 있습니다. 하지만 이 방식에는 심각한 한계가 있습니다.

### 문제 1: 자연스러운 줄바꿈 불가

```kotlin
// 잘못된 접근법
Row {
    Text("검색 결과 ")
    Text("15건", color = Color.Blue, fontWeight = FontWeight.Bold)
    Text("이 발견되었습니다.")
}
// 문제: Row는 한 줄로만 배치! 텍스트가 길면 잘리거나 overflow 발생
```

### 문제 2: Baseline 정렬 문제

```kotlin
// 잘못된 접근법
Row(verticalAlignment = CenterVertically) {
    Text("평점: ")
    Icon(Icons.Default.Star, ...)
    Text(" 4.5점")
}
// 문제: 아이콘과 텍스트의 baseline이 맞지 않음, 줄바꿈 시 분리됨
```

### 문제 3: 부분 클릭 불가

```kotlin
// 잘못된 접근법
Text(
    text = "이용약관에 동의합니다",
    modifier = Modifier.clickable { }
)
// 문제: 전체가 클릭됨! '이용약관'만 클릭하고 싶음
```

### 발생하는 문제 요약

| 문제 | 설명 | 해결 기술 |
|------|------|----------|
| 줄바꿈 불가 | Row는 가로 정렬이라 텍스트가 잘림 | **buildAnnotatedString** |
| Baseline 불일치 | 아이콘과 텍스트의 baseline이 맞지 않음 | **InlineTextContent** |
| 부분 클릭 불가 | 전체 또는 분리된 영역만 클릭 가능 | **LinkAnnotation** |
| 성능 오버헤드 | 여러 Composable 사용으로 불필요한 오버헤드 | 단일 Text 사용 |

---

## 핵심 기술 6가지

| 기술 | 설명 | 해결하는 문제 | 사용 시점 |
|------|------|-------------|----------|
| **AnnotatedString** | 부분별 스타일이 적용된 문자열 | 줄바꿈 문제 | 여러 스타일 혼합 |
| **buildAnnotatedString** | 타입 안전한 AnnotatedString 빌더 | 줄바꿈 문제 | AnnotatedString 생성 |
| **InlineTextContent** | 텍스트 내 Composable 삽입 | Baseline 정렬 | 아이콘/이미지 삽입 |
| **LinkAnnotation** | 텍스트 부분 클릭 처리 (2024+ 권장) | 부분 클릭 | 약관 링크, 멘션 클릭 |
| **TextMeasurer** | 텍스트 크기 측정 | (추가 기법) | Canvas에서 배경 그리기 |
| **FontFamily/Typography** | 폰트 스타일 설정 | (추가 기법) | 커스텀 폰트 적용 |

---

## 기술 1: buildAnnotatedString (줄바꿈 문제 해결)

단일 `Text` 내에서 `SpanStyle`로 부분별 스타일을 적용합니다. 자연스러운 줄바꿈이 가능합니다.

### 기본 사용법

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

### SpanStyle 주요 속성

```kotlin
withStyle(SpanStyle(
    color = Color.Red,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    fontStyle = FontStyle.Italic,
    textDecoration = TextDecoration.Underline,
    background = Color.Yellow,
    letterSpacing = 2.sp
)) {
    append("강조 텍스트")
}
```

---

## 기술 2: InlineTextContent (Baseline 정렬 해결)

텍스트 흐름 내에 아이콘이나 이미지를 삽입합니다. `PlaceholderVerticalAlign`으로 정렬을 제어합니다.

### 기본 사용법

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

| 옵션 | 설명 | 사용 시점 |
|------|------|----------|
| `TextTop` | 텍스트 상단에 정렬 | 위첨자 스타일 |
| `TextBottom` | 텍스트 하단에 정렬 | 아래첨자 스타일 |
| `TextCenter` | 텍스트 중앙에 정렬 | 가장 일반적 |
| `AboveBaseline` | Baseline 위에 정렬 | 정교한 정렬 필요 시 |
| `Center` | 라인 중앙에 정렬 | 전체 라인 기준 |

---

## 기술 3: LinkAnnotation (부분 클릭 해결)

`ClickableText`는 **deprecated**되었습니다 (Compose Foundation 1.7.0-alpha07+).
2024년부터는 `LinkAnnotation`을 사용하세요.

### URL 링크 (자동으로 브라우저 열기)

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

### TextLinkStyles 고급 옵션 (2025)

```kotlin
TextLinkStyles(
    style = SpanStyle(color = Color.Blue),           // 기본 스타일
    hoveredStyle = SpanStyle(color = Color.Cyan),    // 호버 시 (Desktop/Web)
    focusedStyle = SpanStyle(color = Color.Green),   // 포커스 시
    pressedStyle = SpanStyle(color = Color.DarkBlue) // 누를 때
)
```

### ClickableText vs LinkAnnotation 비교

| ClickableText (Deprecated) | LinkAnnotation (권장) |
|---------------------------|----------------------|
| `onClick: (Int) -> Unit` | `linkInteractionListener` |
| 수동으로 annotation 확인 | 자동으로 링크 처리 |
| 별도 Composable 필요 | 일반 Text 사용 |
| Compose 1.7.0에서 제거 예정 | 현재 및 미래 표준 |

---

## 추가 기법 1: TextMeasurer

텍스트 크기를 측정하여 Canvas에서 배경을 그리거나 레이아웃을 계산합니다.

### 사용법

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

## 추가 기법 2: 커스텀 폰트 & Typography

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

## 실전 사용 시나리오

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

## 주의사항 & 베스트 프랙티스

### 1. 성능 최적화 (remember 사용)

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
// Placeholder 크기는 sp 단위 사용 (폰트 크기에 비례)
Placeholder(
    width = 16.sp,   // dp가 아닌 sp!
    height = 16.sp,
    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
)
```

### 3. LinkAnnotation 접근성

```kotlin
// 의미 있는 링크 텍스트 사용
withLink(LinkAnnotation.Url("https://example.com")) {
    append("여기를 클릭하세요")  // X - 스크린 리더가 이해하기 어려움
    append("공식 문서 보기")     // O - 명확한 의미
}
```

### 4. TextMeasurer 캐싱

```kotlin
// textMeasurer.measure()는 비용이 높으므로 remember 사용
val textLayoutResult = remember(text, style) {
    textMeasurer.measure(AnnotatedString(text), style)
}
```

### 5. AnnotatedString 재사용

```kotlin
// 동일한 스타일을 여러 곳에서 사용할 때
val highlightStyle = SpanStyle(
    color = MaterialTheme.colorScheme.primary,
    fontWeight = FontWeight.Bold
)

// 재사용
withStyle(highlightStyle) { append("강조1") }
withStyle(highlightStyle) { append("강조2") }
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
- [User interactions - Android Developers](https://developer.android.com/develop/ui/compose/text/user-interactions)
- [InlineTextContent API Reference](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/InlineTextContent)
- [AnnotatedString API Reference](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString)
- [Mastering LinkAnnotation - ProAndroidDev](https://proandroiddev.com/mastering-linkannotation-the-modern-approach-to-text-links-26fbde09b158)
