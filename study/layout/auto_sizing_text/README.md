# Auto-sizing Text 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `Text` | Compose의 기본 텍스트 컴포넌트 | [📚 학습하기](../../basics/text_typography/README.md) |
| `Modifier` | Composable의 레이아웃과 동작 수정 | [📚 학습하기](../../basics/modifier_basics/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Auto-sizing Text**는 텍스트 크기를 컨테이너 크기에 맞게 자동으로 조절하는 기능입니다.
Compose 1.8부터 `BasicText`에서 `autoSize` 파라미터를 통해 사용할 수 있습니다.

마치 **자동 줌 기능**과 같습니다:
- 컨테이너가 크면 텍스트가 크게 표시됨
- 컨테이너가 작으면 텍스트가 작게 표시됨
- 하지만 텍스트 내용은 **항상 전체가 보임!**

## 핵심 특징

1. **자동 크기 조절**: 컨테이너 크기에 맞게 폰트 크기 자동 계산
2. **범위 제한**: minFontSize/maxFontSize로 가독성과 시각적 임팩트 보장
3. **조절 단위 설정**: stepGranularity로 크기 변화의 정밀도 조절

---

## 문제 상황: 고정 크기 텍스트의 한계

### 시나리오

쇼핑몰 앱에서 상품 카드를 만들고 있습니다.
상품명을 표시해야 하는데, 고정된 `fontSize = 16.sp`를 사용했습니다.

```kotlin
// 문제가 발생하는 코드
Text(
    text = "삼성 갤럭시 S25 울트라 512GB 티타늄 블랙 자급제",
    fontSize = 16.sp,  // 고정 크기!
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)
```

### 발생하는 문제점

1. **텍스트 오버플로우**: 컨테이너보다 텍스트가 길면 잘림
2. **정보 손실**: Ellipsis(...)로 중요 정보 누락 (용량, 색상 등)
3. **레이아웃 불균형**: 다양한 화면 크기에서 일관성 없음

### 수동 해결의 어려움

```kotlin
// 복잡한 조건문으로 수동 조절?
val fontSize = when {
    screenWidth < 300.dp -> 12.sp
    screenWidth < 400.dp -> 14.sp
    else -> 16.sp
}
```

이 방법은:
- 모든 경우를 예측해야 함
- 코드가 복잡해짐
- 유지보수가 어려움

---

## 해결책: TextAutoSize.StepBased() 사용

### 기본 사용법

```kotlin
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.text.TextAutoSize

BasicText(
    text = "삼성 갤럭시 S25 울트라 512GB 티타늄 블랙 자급제",
    maxLines = 1,
    autoSize = TextAutoSize.StepBased(
        minFontSize = 10.sp,   // 최소 크기 (가독성 보장)
        maxFontSize = 16.sp,   // 최대 크기
        stepGranularity = 1.sp // 조절 단위
    ),
    modifier = Modifier.fillMaxWidth()  // 컨테이너 크기 필요!
)
```

### 동작 원리

1. **maxFontSize에서 시작**
2. 텍스트가 컨테이너에 맞는지 확인
3. 맞지 않으면 **stepGranularity만큼 감소**
4. **minFontSize까지 반복**
5. 최적의 크기 결정!

### 해결되는 이유

- 컨테이너 크기에 **자동으로 적응**
- minFontSize로 **최소 가독성 보장**
- 수동 조건문 **불필요**
- 모든 화면 크기에서 **일관된 UX**

---

## API 상세

### TextAutoSize.StepBased

```kotlin
TextAutoSize.StepBased(
    minFontSize: TextUnit = TextUnit.Unspecified,
    maxFontSize: TextUnit = TextUnit.Unspecified,
    stepGranularity: TextUnit = 1.sp
)
```

| 파라미터 | 타입 | 설명 | 기본값 |
|----------|------|------|--------|
| minFontSize | TextUnit | 최소 폰트 크기 | Unspecified |
| maxFontSize | TextUnit | 최대 폰트 크기 | Unspecified |
| stepGranularity | TextUnit | 크기 조절 단위 | 1.sp |

### 파라미터 가이드

**minFontSize**
- 가독성을 보장하는 최소 크기
- 일반 텍스트: 10-12sp
- 중요 정보(가격 등): 14-16sp

**maxFontSize**
- 시각적 임팩트를 위한 최대 크기
- 카드 제목: 16-20sp
- 배너: 32-64sp

**stepGranularity**
- 작을수록 정밀하지만 계산 많음
- 클수록 빠르지만 덜 정밀함
- 일반: 1sp, 빠른 조절: 2-4sp

---

## 사용 시나리오

### 1. 상품 카드 제목

```kotlin
BasicText(
    text = productTitle,
    maxLines = 1,
    autoSize = TextAutoSize.StepBased(
        minFontSize = 10.sp,
        maxFontSize = 16.sp,
        stepGranularity = 0.5.sp
    ),
    style = TextStyle(fontWeight = FontWeight.Bold),
    modifier = Modifier.fillMaxWidth()
)
```

### 2. 가격 배너

```kotlin
BasicText(
    text = "99,900원",
    maxLines = 1,
    autoSize = TextAutoSize.StepBased(
        minFontSize = 16.sp,
        maxFontSize = 48.sp,
        stepGranularity = 2.sp
    ),
    style = TextStyle(
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center
    ),
    modifier = Modifier.fillMaxWidth()
)
```

### 3. 대시보드 통계

```kotlin
BasicText(
    text = "1,234,567",
    maxLines = 1,
    autoSize = TextAutoSize.StepBased(
        minFontSize = 12.sp,
        maxFontSize = 28.sp
    ),
    style = TextStyle(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
    modifier = Modifier.fillMaxWidth()
)
```

---

## 주의사항

1. **컨테이너 크기 필수**
   - `Modifier.fillMaxWidth()` 또는 고정 크기 필요
   - 크기가 없으면 자동 조절 불가

2. **멀티라인 제한**
   - `maxLines > 1`일 때 줄 간격이 텍스트 크기와 비례하지 않을 수 있음
   - 멀티라인에서는 시각적으로 어색할 수 있음

3. **버전 요구사항**
   - Compose BOM 2025.04.01 이상
   - Foundation 1.8.0 이상
   - Material3 1.4.0 이상 (Text autoSize)

4. **BasicText vs Text**
   - 현재: `BasicText`에서 autoSize 지원
   - Material3 1.4.0+: `Text`에서도 autoSize 지원

---

## 연습 문제

### 연습 1: 적응형 상품 카드 제목 (쉬움)

기본 autoSize 사용법을 익힙니다.

**과제**: 상품명이 길어도 한 줄에 전체가 표시되도록 구현하세요.

**힌트**:
- BasicText 사용
- minFontSize = 10.sp, maxFontSize = 16.sp

### 연습 2: 세일 가격 배너 (중간)

stepGranularity와 스타일링을 조합합니다.

**과제**: 가격이 배너 크기에 맞게 자동 조절되도록 구현하세요.

**힌트**:
- stepGranularity = 2.sp로 빠른 조절
- TextStyle로 중앙 정렬

### 연습 3: 반응형 대시보드 카드 (어려움)

실제 앱 시나리오를 구현합니다.

**과제**: 3개의 통계 카드를 균등 배치하고, 각 숫자가 자동 조절되도록 구현하세요.

**힌트**:
- Row와 weight로 균등 배치
- 재사용 가능한 StatCard 컴포저블 분리

---

## 다음 학습

- **Text & Typography**: AnnotatedString, InlineContent 등 텍스트 심화
- **Modifier 심화**: 커스텀 Modifier 작성
- **Layout**: 반응형 레이아웃 구성
