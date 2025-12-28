# Intrinsic Measurements 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `Modifier` | Composable의 레이아웃과 동작 수정 | [📚 학습하기](../layout_and_modifier/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Intrinsic Measurements**는 자식 컴포저블의 "고유 크기(intrinsic size)"를 실제 측정 전에 미리 쿼리하는 기능입니다.

"이 컴포저블이 적절히 표시되려면 최소/최대 얼마나 필요한가?"를 미리 알 수 있어, Row나 Column 내 아이템들의 크기를 동기화할 때 유용합니다.

---

## 핵심 특징

### 1. 4가지 IntrinsicSize 옵션

| Modifier | 설명 |
|----------|------|
| `width(IntrinsicSize.Min)` | 콘텐츠를 적절히 표시하는 **최소 너비** |
| `width(IntrinsicSize.Max)` | 콘텐츠를 적절히 표시하는 **최대 너비** |
| `height(IntrinsicSize.Min)` | 콘텐츠를 적절히 표시하는 **최소 높이** |
| `height(IntrinsicSize.Max)` | 콘텐츠를 적절히 표시하는 **최대 높이** |

### 2. Min vs Max 차이 (텍스트 예시)

| 옵션 | 텍스트에서의 의미 |
|------|-----------------|
| **Min** | 가장 긴 단어의 너비 (여러 줄로 나뉠 수 있음) |
| **Max** | 모든 텍스트를 한 줄에 표시하는 너비 |

### 3. 두 번 측정이 아닙니다!

Intrinsic 쿼리는 실제 측정(measure)과 다른 가벼운 연산입니다:
1. **사전 쿼리**: 자식들의 고유 크기를 물어봄
2. **제약 계산**: 그 정보를 바탕으로 제약(constraints) 결정
3. **실제 측정**: 계산된 제약으로 자식들을 측정

---

## 문제 상황: IntrinsicSize 없이 발생하는 문제

### 시나리오

Row 안에 서로 다른 높이의 텍스트와 VerticalDivider가 있을 때:

```kotlin
// 문제가 발생하는 코드
Row(modifier = Modifier.fillMaxWidth()) {
    Text("여러 줄\n텍스트\n입니다", modifier = Modifier.weight(1f))
    VerticalDivider(modifier = Modifier.fillMaxHeight().width(1.dp))
    Text("짧음", modifier = Modifier.weight(1f))
}
```

### 발생하는 문제점

1. **VerticalDivider가 보이지 않음**: Divider의 `minIntrinsicHeight`가 0이므로 높이 제약이 없으면 0이 됨
2. **카드 높이 불일치**: 각 카드가 자신의 콘텐츠 높이에만 맞춰짐
3. **UI 불균형**: 정돈되지 않은 레이아웃

### 왜 문제가 발생하는가?

```
VerticalDivider의 minIntrinsicHeight = 0
                ↓
"나는 0 높이도 괜찮아!"라고 말함
                ↓
Row에 높이 제약이 없으면 fillMaxHeight()가 동작 안 함
                ↓
Divider가 0 높이가 됨
```

---

## 해결책: IntrinsicSize 사용

### 기본 사용법

```kotlin
Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)  // 핵심!
) {
    Text("여러 줄\n텍스트\n입니다", modifier = Modifier.weight(1f))
    VerticalDivider(
        modifier = Modifier
            .fillMaxHeight()  // IntrinsicSize.Min 덕분에 동작!
            .width(1.dp)
    )
    Text("짧음", modifier = Modifier.weight(1f))
}
```

### 해결되는 이유

```
height(IntrinsicSize.Min) 적용
                ↓
Row가 자식들의 minIntrinsicHeight를 쿼리
                ↓
Text1 = 3줄 높이, Text2 = 1줄 높이, Divider = 0
                ↓
Row의 높이 = max(3줄, 1줄, 0) = 3줄 높이
                ↓
Divider의 fillMaxHeight()가 3줄 높이에 맞춤!
```

---

## 사용 시나리오

### 1. Row 내 Divider 높이 동기화

```kotlin
Row(modifier = Modifier.height(IntrinsicSize.Min)) {
    Column(modifier = Modifier.weight(1f)) { /* 왼쪽 콘텐츠 */ }
    VerticalDivider(modifier = Modifier.fillMaxHeight())
    Column(modifier = Modifier.weight(1f)) { /* 오른쪽 콘텐츠 */ }
}
```

### 2. 동일 높이 카드 그리드

```kotlin
Row(modifier = Modifier.height(IntrinsicSize.Min)) {
    Card(modifier = Modifier.weight(1f).fillMaxHeight()) { /* 카드 1 */ }
    Card(modifier = Modifier.weight(1f).fillMaxHeight()) { /* 카드 2 */ }
    Card(modifier = Modifier.weight(1f).fillMaxHeight()) { /* 카드 3 */ }
}
```

### 3. 테이블 레이아웃

```kotlin
Column {
    // 각 행에 IntrinsicSize.Min 적용
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        TableCell("이름", Modifier.weight(1f))
        VerticalDivider()
        TableCell("점수", Modifier.weight(1f))
    }
    HorizontalDivider()
    // 데이터 행들...
}
```

### 4. 동일 너비 버튼 그룹

```kotlin
Column(modifier = Modifier.width(IntrinsicSize.Max)) {
    Button(modifier = Modifier.fillMaxWidth()) { Text("짧은") }
    Button(modifier = Modifier.fillMaxWidth()) { Text("중간 길이 버튼") }
    Button(modifier = Modifier.fillMaxWidth()) { Text("가장 긴 버튼 텍스트") }
}
```

---

## 비유로 이해하기

IntrinsicSize는 **"사전 조사원"**과 같습니다:

```
집을 지을 때 (측정 전에)
        ↓
각 방에 들어갈 가구 크기를 먼저 조사합니다 (intrinsic 쿼리)
        ↓
그 정보를 바탕으로 방 크기를 결정합니다 (제약 계산)
        ↓
그 다음 실제로 가구를 배치합니다 (측정)
```

"두 번 측정"이 아니라 **"조사 → 측정"** 순서입니다!

---

## 주의사항

### 1. 성능 고려

- Intrinsic 쿼리는 추가 연산이므로, 필요한 경우에만 사용
- 깊이 중첩된 레이아웃에서 과도하게 사용하면 성능 저하 가능

### 2. 커스텀 Layout에서의 Intrinsic

커스텀 `Layout`을 만들 때는 intrinsic 메서드를 오버라이드할 수 있습니다:

```kotlin
Layout(
    content = content,
    measurePolicy = object : MeasurePolicy {
        override fun MeasureScope.measure(...): MeasureResult { /* 측정 */ }

        override fun IntrinsicMeasureScope.minIntrinsicWidth(
            measurables: List<IntrinsicMeasurable>,
            height: Int
        ): Int {
            // 커스텀 로직
        }
        // maxIntrinsicWidth, minIntrinsicHeight, maxIntrinsicHeight도 동일
    }
)
```

### 3. fillMaxHeight/Width와 함께 사용

- `IntrinsicSize.Min`만으로는 부족하고, 자식에게 `fillMaxHeight()`도 필요
- Row의 높이를 제한해야 fillMaxHeight()가 동작함

---

## 연습 문제

### 연습 1: 프로필 비교 UI (쉬움)

두 사용자 프로필을 나란히 배치하고 VerticalDivider로 구분하세요.
Divider가 더 긴 프로필 높이에 맞춰야 합니다.

**힌트**: Row에 `height(IntrinsicSize.Min)` 적용

### 연습 2: 동일 높이 상품 카드 (중간)

3개의 상품 카드를 동일한 높이로 만들고, 구매 버튼을 각 카드 하단에 정렬하세요.

**힌트**: Row에 `IntrinsicSize.Min`, Card에 `fillMaxHeight()`, Column 내 `Spacer(weight(1f))`

### 연습 3: 성적표 테이블 (어려움)

학생 성적표를 테이블 형태로 구현하세요. 각 행의 셀들이 동일한 높이를 가져야 합니다.

**힌트**: 각 Row에 `height(IntrinsicSize.Min)` 적용

---

## 다음 학습

- [Custom Layout](../custom_layout/README.md): Intrinsic 메서드 오버라이드
- [Constraint Layout](../constraint_layout/README.md): 복잡한 레이아웃 제약
- [Flow Layout](../flow_layout/README.md): 자동 줄바꿈 레이아웃
