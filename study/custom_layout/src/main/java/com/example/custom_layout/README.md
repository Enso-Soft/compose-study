# Custom Layout 학습

## 개념

Compose UI는 세 단계로 동작합니다:
1. **Composition** - UI를 선언
2. **Measurement** - 각 컴포넌트 크기 결정
3. **Layout** - 컴포넌트 위치 배치

**Custom Layout**은 이 중 Measurement와 Layout 단계를 직접 제어하여 **Column, Row, Box로는 표현할 수 없는 레이아웃**을 구현하는 기술입니다.

---

## 핵심 특징

### 1. Single-pass Measurement 규칙
```kotlin
// Compose의 핵심 제약
자식을 한 번만 측정할 수 있습니다.
두 번 측정하면 런타임 예외가 발생합니다!
```

### 2. 세 가지 구현 방법

| 방법 | 용도 | 특징 |
|------|------|------|
| `layout` modifier | 단일 요소 커스터마이징 | 간단, 기존 Composable에 적용 |
| `Layout` composable | 다중 자식 레이아웃 | Column, Row처럼 완전한 커스텀 레이아웃 |
| `SubcomposeLayout` | 동적 컨텐츠 | 측정 중 composition 가능 (LazyColumn 등) |

### 3. MeasurePolicy 구성요소

```kotlin
Layout(content = content) { measurables, constraints ->
    // measurables: 측정할 자식들 (List<Measurable>)
    // constraints: 부모로부터 받은 크기 제약 (min/max width/height)

    // 1. 자식 측정
    val placeables = measurables.map { it.measure(constraints) }

    // 2. 레이아웃 크기 결정 및 자식 배치
    layout(width, height) {
        placeables.forEach { placeable ->
            placeable.placeRelative(x, y)
        }
    }
}
```

---

## 문제 상황: Column/Row/Box로 표현 불가능한 레이아웃

### 잘못된 시도들

```kotlin
// 시도 1: Column 사용 → 세로로 쌓임
Column {
    repeat(5) { Icon(Icons.Default.Star, null) }
}
// 결과: 아이콘들이 위에서 아래로 일렬로 배치

// 시도 2: Row 사용 → 가로로 쌓임
Row {
    repeat(5) { Icon(Icons.Default.Star, null) }
}
// 결과: 아이콘들이 왼쪽에서 오른쪽으로 일렬로 배치

// 시도 3: Box 사용 → 모두 겹침
Box {
    repeat(5) { Icon(Icons.Default.Star, null) }
}
// 결과: 아이콘들이 같은 위치에 겹쳐서 하나만 보임
```

### 발생하는 문제점

1. **원형 배치 불가** - 아이템들을 원 형태로 배치할 수 없음
2. **계단식 배치 불가** - 각 아이템을 점진적으로 들여쓰기할 수 없음
3. **커스텀 정렬 불가** - baseline 외 다른 기준으로 정렬 불가

---

## 해결책: Custom Layout 사용

### 1. Layout composable로 원형 배치

```kotlin
@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    radius: Dp = 100.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // 1. 모든 자식 측정
        val placeables = measurables.map { it.measure(constraints) }

        // 2. 중심점과 각도 계산
        val centerX = constraints.maxWidth / 2
        val centerY = constraints.maxHeight / 2
        val radiusPx = radius.roundToPx()
        val angleStep = 2 * PI / placeables.size

        // 3. 레이아웃 크기 결정 및 배치
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val angle = angleStep * index - PI / 2
                val x = (centerX + radiusPx * cos(angle) - placeable.width / 2).toInt()
                val y = (centerY + radiusPx * sin(angle) - placeable.height / 2).toInt()
                placeable.placeRelative(x, y)
            }
        }
    }
}
```

### 2. layout modifier로 커스텀 패딩

```kotlin
fun Modifier.baselinePadding(top: Dp) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val baseline = placeable[FirstBaseline]
    val placeableY = top.roundToPx() - baseline

    layout(placeable.width, placeable.height + placeableY) {
        placeable.placeRelative(0, placeableY)
    }
}
```

### 해결되는 이유

1. **자유로운 좌표 계산** - 삼각함수로 원형 좌표 계산 가능
2. **모든 자식 개별 배치** - 각 아이템마다 다른 위치 지정 가능
3. **RTL 자동 지원** - `placeRelative` 사용으로 RTL 레이아웃 대응

---

## SubcomposeLayout

### 언제 사용하나?

**측정 결과를 바탕으로 다른 컴포저블을 구성해야 할 때**

```kotlin
@Composable
fun EqualWidthColumn(content: @Composable () -> Unit) {
    SubcomposeLayout { constraints ->
        // 1단계: 자연스러운 크기로 먼저 측정
        val measureables = subcompose("content", content)
        val maxWidth = measureables.maxOf {
            it.measure(constraints).width
        }

        // 2단계: 모든 자식을 maxWidth로 재측정
        val placeables = measureables.map {
            it.measure(constraints.copy(minWidth = maxWidth, maxWidth = maxWidth))
        }

        layout(maxWidth, placeables.sumOf { it.height }) {
            var y = 0
            placeables.forEach {
                it.placeRelative(0, y)
                y += it.height
            }
        }
    }
}
```

### 주의사항

1. **Intrinsic size 손실** - SubcomposeLayout은 고유 크기를 제공할 수 없음
2. **성능 비용** - Composition과 Measurement가 결합되어 오버헤드 발생
3. **사용 사례** - LazyColumn, TabRow, BoxWithConstraints가 이를 사용

---

## Intrinsic Measurements

### 개념

실제 측정 전에 컴포저블의 크기 정보를 쿼리하는 방법

```kotlin
Modifier.width(IntrinsicSize.Min)   // 최소 너비
Modifier.width(IntrinsicSize.Max)   // 최대 너비
Modifier.height(IntrinsicSize.Min)  // 최소 높이
Modifier.height(IntrinsicSize.Max)  // 최대 높이
```

### 커스텀 레이아웃에서 구현

```kotlin
Layout(
    content = content,
    measurePolicy = object : MeasurePolicy {
        override fun MeasureScope.measure(
            measurables: List<Measurable>,
            constraints: Constraints
        ): MeasureResult { /* ... */ }

        override fun IntrinsicMeasureScope.minIntrinsicWidth(
            measurables: List<IntrinsicMeasurable>,
            height: Int
        ): Int {
            return measurables.maxOf { it.minIntrinsicWidth(height) }
        }
        // maxIntrinsicWidth, minIntrinsicHeight, maxIntrinsicHeight도 구현
    }
)
```

---

## 사용 시나리오

1. **원형 메뉴** - CircularLayout으로 아이콘 원형 배치
2. **Staggered Grid** - Pinterest 스타일 불규칙 그리드
3. **Flow Layout** - 태그 목록 (자동 줄바꿈)
4. **커스텀 정렬** - baseline 외 다른 기준으로 정렬
5. **동적 크기 조정** - 가장 큰 자식에 맞춰 다른 자식 크기 조정

---

## 주의사항

1. **Single-pass 규칙** - 자식을 두 번 측정하면 예외 발생
2. **SubcomposeLayout 비용** - 꼭 필요할 때만 사용
3. **placeRelative 사용** - RTL 지원을 위해 `place` 대신 `placeRelative` 사용
4. **Intrinsic 구현** - `IntrinsicSize` 지원이 필요하면 반드시 오버라이드

---

## 연습 문제

### 연습 1: StaggeredColumn (계단식 배치)
각 아이템을 index만큼 들여쓰기하는 레이아웃 구현

### 연습 2: ReverseFlowRow (역방향 Flow)
오른쪽에서 왼쪽으로 배치, 넘치면 다음 줄로

### 연습 3: EqualWidthColumn (동일 너비 컬럼)
가장 넓은 자식에 맞춰 모든 자식 너비 통일 (SubcomposeLayout)

---

## 다음 학습

- **ConstraintLayout Compose** - 제약 조건 기반 복잡한 레이아웃
- **Animation Advanced** - Transition, Animatable 고급 애니메이션
