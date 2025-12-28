# Rich Text (AnnotatedString) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `Text` | Compose의 기본 텍스트 컴포넌트 | [📚 학습하기](../text_typography/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Rich Text**는 Jetpack Compose에서 `AnnotatedString`을 활용하여 단일 텍스트 내에서 여러 스타일을 혼합하고, 클릭 가능한 영역을 만들고, 인라인 이미지/아이콘을 삽입하는 기술입니다.

일반적인 `Text` 컴포저블로는 구현하기 어려운 복잡한 텍스트 스타일링을 우아하게 해결합니다.

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

## 핵심 특징

| 기술 | 설명 | 해결하는 문제 |
|------|------|-------------|
| **buildAnnotatedString** | 타입 안전한 AnnotatedString 빌더 | 줄바꿈 문제 |
| **SpanStyle** | 인라인 스타일 (색상, 굵기, 크기) | 부분 스타일링 |
| **ParagraphStyle** | 단락 스타일 (정렬, 줄 높이) | 단락 레이아웃 |
| **InlineTextContent** | 텍스트 내 Composable 삽입 | Baseline 정렬 |
| **LinkAnnotation** | 텍스트 부분 클릭 처리 | 부분 클릭 |
| **withAnnotation** | 텍스트에 메타데이터 추가 | 데이터 연결 |

---

## 문제 상황: Row로 여러 Text를 조합할 때

### 왜 이 기술이 필요한가?

직관적으로 "텍스트 일부분만 스타일을 다르게 하고 싶다"면, 여러 `Text`를 `Row`로 묶는 방법을 떠올릴 수 있습니다. 하지만 이 방식에는 심각한 한계가 있습니다.

### 문제 1: 줄바꿈 불가

```kotlin
// 잘못된 방법
Row {
    Text("검색 결과 ")
    Text("15건", color = Color.Blue, fontWeight = FontWeight.Bold)
    Text("이 발견되었습니다. 원하시는 항목을 선택해주세요.")
}
// 문제: Row는 한 줄로만 배치! 화면이 좁으면 텍스트가 잘림
```

Row는 자식 요소를 가로로 나열합니다. 텍스트가 화면 너비를 초과해도 자동 줄바꿈이 되지 않아 텍스트가 잘리거나 overflow가 발생합니다.

### 문제 2: Baseline 정렬 불일치

```kotlin
// 잘못된 방법
Row(verticalAlignment = Alignment.CenterVertically) {
    Text("평점: ")
    Icon(Icons.Default.Star, contentDescription = null)
    Text(" 4.5점")
}
// 문제: 아이콘과 텍스트의 baseline이 맞지 않아 어색함
```

Row에서 Icon과 Text를 함께 사용하면 기준선(baseline)이 맞지 않아 시각적으로 어색해 보입니다.

### 문제 3: 부분 클릭 불가

```kotlin
// 잘못된 방법 1: 전체가 클릭됨
Text(
    text = "이용약관에 동의합니다",
    modifier = Modifier.clickable { }
)

// 잘못된 방법 2: Row로 분리하면 줄바꿈 문제
Row {
    Text("이용약관", modifier = Modifier.clickable { })
    Text("에 동의합니다")
}
```

"이용약관"만 클릭하고 싶은데, 방법 1은 전체가 클릭되고 방법 2는 줄바꿈 문제가 발생합니다.

### 발생하는 문제 요약

| 문제 | 설명 | 해결 기술 |
|------|------|----------|
| 줄바꿈 불가 | Row는 가로 정렬이라 텍스트가 잘림 | **buildAnnotatedString** |
| Baseline 불일치 | 아이콘과 텍스트의 기준선이 맞지 않음 | **InlineTextContent** |
| 부분 클릭 불가 | 전체가 클릭되거나 분리 시 줄바꿈 문제 | **LinkAnnotation** |

---

## 해결책 1: buildAnnotatedString + SpanStyle

줄바꿈 문제를 해결합니다. 단일 Text 내에서 여러 스타일을 혼합하고 자연스러운 줄바꿈이 가능합니다.

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

### SpanStyle 주요 속성

```kotlin
withStyle(SpanStyle(
    color = Color.Red,              // 텍스트 색상
    fontSize = 20.sp,               // 글자 크기
    fontWeight = FontWeight.Bold,   // 굵기
    fontStyle = FontStyle.Italic,   // 기울임
    textDecoration = TextDecoration.Underline,  // 밑줄
    background = Color.Yellow,      // 배경색
    letterSpacing = 2.sp            // 자간
)) {
    append("스타일 적용 텍스트")
}
```

---

## 해결책 2: InlineTextContent

Baseline 정렬 문제를 해결합니다. 텍스트 흐름 내에 아이콘이나 이미지를 자연스럽게 삽입합니다.

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

---

## 해결책 3: LinkAnnotation

부분 클릭 문제를 해결합니다. `ClickableText`는 **deprecated**되었으며, 2024년부터는 `LinkAnnotation`을 사용합니다.

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

---

## 해결책 4: ParagraphStyle

단락 스타일(정렬, 줄 높이, 들여쓰기)을 적용합니다.

```kotlin
val paragraphText = buildAnnotatedString {
    withStyle(ParagraphStyle(lineHeight = 28.sp)) {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("제목\n")
        }
        append("본문 내용입니다.")
    }
}

Text(text = paragraphText)
```

### ParagraphStyle 주요 속성

| 속성 | 설명 |
|------|------|
| `lineHeight` | 줄 높이 |
| `textAlign` | 텍스트 정렬 (Start, Center, End, Justify) |
| `textIndent` | 첫 줄/나머지 줄 들여쓰기 |

---

## 실전 사용 시나리오

### 1. 해시태그 자동 스타일링

```kotlin
fun buildHashtagStyledText(text: String): AnnotatedString {
    val hashtagRegex = Regex("#\\S+")

    return buildAnnotatedString {
        var lastIndex = 0

        hashtagRegex.findAll(text).forEach { matchResult ->
            append(text.substring(lastIndex, matchResult.range.first))
            withStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                append(matchResult.value)
            }
            lastIndex = matchResult.range.last + 1
        }

        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
}
```

### 2. 검색어 하이라이트

```kotlin
fun buildHighlightedText(text: String, query: String): AnnotatedString {
    return buildAnnotatedString {
        var startIndex = 0
        var matchIndex = text.indexOf(query, 0, ignoreCase = true)

        while (matchIndex >= 0) {
            append(text.substring(startIndex, matchIndex))
            withStyle(SpanStyle(background = Color.Yellow, fontWeight = FontWeight.Bold)) {
                append(text.substring(matchIndex, matchIndex + query.length))
            }
            startIndex = matchIndex + query.length
            matchIndex = text.indexOf(query, startIndex, ignoreCase = true)
        }

        if (startIndex < text.length) {
            append(text.substring(startIndex))
        }
    }
}
```

### 3. 가격 표시 (할인)

```kotlin
val priceText = buildAnnotatedString {
    withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough, color = Color.Gray)) {
        append("50,000원")
    }
    append(" ")
    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red, fontSize = 18.sp)) {
        append("35,000원")
    }
    append(" ")
    withStyle(SpanStyle(color = Color.Red, fontSize = 12.sp)) {
        append("30% 할인")
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

### 2. InlineContent 크기는 sp 단위

```kotlin
// Placeholder 크기는 sp 단위 사용 (폰트 크기에 비례)
Placeholder(
    width = 16.sp,   // dp가 아닌 sp!
    height = 16.sp,
    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
)
```

### 3. 접근성 고려

```kotlin
// 의미 있는 링크 텍스트 사용
withLink(LinkAnnotation.Url("https://example.com")) {
    append("여기를 클릭하세요")  // X - 스크린 리더가 이해하기 어려움
    append("공식 문서 보기")     // O - 명확한 의미
}
```

---

## 연습 문제

### 연습 1: 강조 텍스트 만들기 (쉬움)

문장에서 특정 키워드를 파란색 + 굵게 표시하세요.

**힌트**: `indexOf`, `substring`, `withStyle(SpanStyle(...))`

### 연습 2: 인라인 아이콘 삽입 (중간)

텍스트 내에 경고 아이콘과 별 아이콘을 삽입하세요.

**힌트**: `InlineTextContent`, `Placeholder`, `appendInlineContent`

### 연습 3: 클릭 가능한 링크 텍스트 (어려움)

"이용약관"과 "개인정보처리방침" 클릭 시 각각 다른 Toast를 표시하세요.

**힌트**: `LinkAnnotation.Clickable`, `TextLinkStyles`, `linkInteractionListener`

---

## 다음 학습

- **Brush로 그라디언트 텍스트**: `TextStyle(brush = Brush.linearGradient(...))`
- **SelectionContainer**: 텍스트 선택 가능하게 만들기
- **BasicTextField2**: 고급 텍스트 입력 커스터마이징
- **AutoResizing Text**: 컨테이너에 맞게 텍스트 크기 자동 조절

---

## 참고 자료

- [Style text - Android Developers](https://developer.android.com/develop/ui/compose/text/style-text)
- [User interactions - Android Developers](https://developer.android.com/develop/ui/compose/text/user-interactions)
- [AnnotatedString API Reference](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString)
- [InlineTextContent API Reference](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/InlineTextContent)
