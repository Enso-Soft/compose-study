# Custom Modifier 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `custom_layout` | Layout Composable을 통한 커스텀 레이아웃 구현 | [📚 학습하기](../../layout/custom_layout/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Custom Modifier**는 Jetpack Compose에서 재사용 가능한 UI 수정자를 직접 만드는 방법입니다.
기본 제공 Modifier(padding, background 등) 외에 프로젝트에 특화된 동작이나 스타일을 캡슐화할 수 있습니다.

---

## 왜 Custom Modifier가 필요한가?

### 1. 반복되는 Modifier 체인 캡슐화

```kotlin
// Before: 여러 곳에서 동일한 Modifier 체인 반복
Box(
    modifier = Modifier
        .padding(16.dp)
        .background(Color.White, RoundedCornerShape(8.dp))
        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
        .shadow(4.dp, RoundedCornerShape(8.dp))
)

// After: Custom Modifier로 캡슐화
Box(modifier = Modifier.cardStyle())

fun Modifier.cardStyle() = this
    .padding(16.dp)
    .background(Color.White, RoundedCornerShape(8.dp))
    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    .shadow(4.dp, RoundedCornerShape(8.dp))
```

### 2. 기본 Modifier로 불가능한 동작 구현

- 커스텀 터치 효과 (Material ripple 대신 scale/opacity)
- 특수한 그리기 동작 (그라데이션 테두리, 오버레이)
- 복잡한 제스처 처리 (멀티터치, 커스텀 드래그)

### 3. 프로젝트 일관성 유지

- 디자인 시스템에 맞는 공통 스타일 정의
- 브랜드 고유의 UI 동작 표준화
- 팀 전체가 동일한 방식으로 UI 구현

---

## Custom Modifier 생성 방법

Compose에서 Custom Modifier를 만드는 3가지 방법이 있습니다:

```
Modifier.then()          -> 기본 체이닝 (가장 단순)
Modifier.composed {}     -> 레거시 API (비권장, 성능 문제)
Modifier.Node            -> 최신 권장 API (Compose 1.3.0+)
```

### 1. Modifier.then() - 기본 체이닝

가장 간단한 방식으로, 기존 Modifier들을 연결합니다:

```kotlin
fun Modifier.debugBorder() = this.then(
    Modifier.border(1.dp, Color.Red)
)

// 더 간단하게
fun Modifier.debugBorder() = this.border(1.dp, Color.Red)
```

**적합한 상황:**
- 기존 Modifier를 단순 조합할 때
- 상태나 애니메이션이 필요 없을 때
- 성능이 크게 중요하지 않을 때

### 2. Modifier.composed {} - 레거시 (비권장)

**주의: 2025년 현재 더 이상 권장되지 않습니다!**

```kotlin
// 비권장 - 성능 문제 발생
fun Modifier.scaleOnPress() = composed {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f)

    this.graphicsLayer { scaleX = scale; scaleY = scale }
        .pointerInput(Unit) { /* 터치 처리 */ }
}
```

**문제점:**
- 각 인스턴스마다 새로운 subcomposition 생성
- 매 recomposition마다 새 인스턴스 할당
- LazyColumn에서 사용 시 심각한 성능 저하
- Composable 함수가 skip 되지 않음

### 3. Modifier.Node - 최신 권장 API

Compose 1.3.0부터 도입된 현대적 접근법입니다.
**Node 기반 Modifier가 현재 권장되는 표준입니다.**

```kotlin
// 1. Node 구현 (로직과 상태 보관)
private class CircleNode(var color: Color) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        drawCircle(color = color)
        drawContent()
    }
}

// 2. Element 구현 (Node 생성/업데이트 담당)
private data class CircleElement(val color: Color) : ModifierNodeElement<CircleNode>() {
    override fun create() = CircleNode(color)
    override fun update(node: CircleNode) { node.color = color }
}

// 3. Factory 함수 (API 제공)
fun Modifier.circle(color: Color) = this then CircleElement(color)
```

**장점:**
- 노드 재사용으로 성능 최대 80% 개선 (clickable 마이그레이션 사례)
- 생명주기 관리 용이
- 테스트 용이성 향상
- Subcomposition 오버헤드 없음

---

## 핵심 포인트: composed에서 Node로

### 문제 상황

```kotlin
// 매 호출마다 subcomposition 생성 - 성능 저하!
fun Modifier.badScaleEffect() = composed {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.95f else 1f)

    this.graphicsLayer { scaleX = scale; scaleY = scale }
}

// LazyColumn에서 사용 시 심각한 프레임 드롭
LazyColumn {
    items(100) { index ->
        Box(modifier = Modifier.badScaleEffect()) // 매 아이템마다 오버헤드
    }
}
```

**발생하는 문제점:**
1. **Subcomposition 오버헤드**: 각 composed 호출마다 별도의 composition 생성
2. **Skip 불가**: Composable 함수가 반환값을 가지므로 skip 최적화 불가
3. **메모리 압박**: 리스트에서 수많은 remember 인스턴스 생성
4. **프레임 드롭**: 스크롤 시 버벅임 현상

### 해결책: Modifier.Node

```kotlin
// Node는 recomposition을 걸쳐 재사용됨
private class ScaleOnPressNode :
    PointerInputModifierNode,
    DrawModifierNode,
    Modifier.Node() {

    private var scale = 1f

    override fun onPointerEvent(...) {
        // 터치 처리 로직
        scale = if (pressed) 0.95f else 1f
        invalidateDraw() // 필요한 부분만 다시 그리기
    }

    override fun ContentDrawScope.draw() {
        // scale 적용하여 그리기
        drawContext.transform.scale(scale, scale)
        drawContent()
    }
}
```

**해결되는 이유:**
1. **노드 재사용**: 같은 Element면 기존 Node의 update만 호출
2. **Subcomposition 없음**: 일반 클래스로 구현되어 오버헤드 없음
3. **Skip 최적화**: Element의 equals가 true면 update도 skip
4. **효율적 무효화**: invalidate* 함수로 필요한 부분만 갱신

---

## Modifier.Node 심화

### 3요소 구현 패턴

Custom Modifier를 Modifier.Node로 구현할 때는 **항상 3가지 요소**가 필요합니다:

```
+------------------+
|  Factory 함수    |  -> 사용자가 호출하는 API
|  Modifier.xxx() |
+--------+---------+
         |
         v
+------------------+
|    Element      |  -> Node 생성/업데이트 담당
| ModifierNode    |     (data class 권장)
|    Element      |
+--------+---------+
         |
         v
+------------------+
|     Node        |  -> 실제 로직과 상태 보관
|  Modifier.Node  |     (*ModifierNode 인터페이스 구현)
+------------------+
```

### 주요 Node 타입

| Node 타입 | 용도 | 주요 메서드 |
|----------|------|------------|
| `DrawModifierNode` | 커스텀 그리기 | `ContentDrawScope.draw()` |
| `LayoutModifierNode` | 레이아웃 측정/배치 | `measure()`, `minIntrinsicWidth()` |
| `PointerInputModifierNode` | 터치/제스처 처리 | `onPointerEvent()` |
| `SemanticsModifierNode` | 접근성 정보 | `SemanticsPropertyReceiver.applySemantics()` |
| `DelegatingNode` | 여러 노드 조합 | `delegate()` |
| `GraphicsLayerModifierNode` | 그래픽 레이어 수정 | `applyGraphicsLayer()` |

### Node 타입별 사용 예시

#### DrawModifierNode - 커스텀 그리기

```kotlin
class GradientBorderNode(var colors: List<Color>) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        drawContent() // 먼저 콘텐츠 그리기

        // 그라데이션 테두리 그리기
        val brush = Brush.linearGradient(colors)
        drawRoundRect(brush = brush, style = Stroke(width = 4f))
    }
}
```

#### PointerInputModifierNode - 터치 처리

```kotlin
class TapCountNode : PointerInputModifierNode, Modifier.Node() {
    var tapCount = 0

    override fun onPointerEvent(event: PointerEvent, pass: PointerEventPass, bounds: IntSize) {
        if (pass == PointerEventPass.Main && event.type == PointerEventType.Press) {
            tapCount++
            // 상태 변경 처리
        }
    }
}
```

#### DelegatingNode - 여러 노드 조합

```kotlin
class ClickableNode : DelegatingNode() {
    val focusNode = delegate(FocusableNode())
    val rippleNode = delegate(RippleNode())
    val semanticsNode = delegate(ClickSemanticsNode())
}
```

---

## 조건부 Modifier 패턴

### 기본 패턴 (Modifier.then 활용)

```kotlin
Modifier
    .fillMaxWidth()
    .then(if (isEnabled) Modifier.clickable { onClick() } else Modifier)
    .padding(16.dp)
```

### 확장 함수 패턴 (권장)

```kotlin
inline fun Modifier.thenIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
): Modifier = if (condition) this.modifier() else this

// 사용 예시
Modifier
    .fillMaxWidth()
    .thenIf(isHighlighted) { background(Color.Yellow) }
    .thenIf(isClickable) { clickable { onClick() } }
    .padding(16.dp)
```

### 잘못된 패턴 (피해야 함)

```kotlin
// 분기에 따라 다른 Composable 트리 생성 - 잘못됨!
if (isEnabled) {
    Box(modifier = Modifier.clickable { onClick() })
} else {
    Box(modifier = Modifier)
}
```

**문제점:**
- 조건 변경 시 Composable이 완전히 재생성됨
- 상태가 유지되지 않음
- 불필요한 recomposition 발생

---

## 사용 시나리오

### 1. 커스텀 클릭 효과
- Material ripple 대신 scale/opacity 애니메이션
- 브랜드 고유의 터치 피드백

### 2. 커스텀 그리기
- 그라데이션 테두리
- 커스텀 배경 패턴
- 오버레이 효과

### 3. 조건부 스타일링
- 상태에 따른 동적 스타일
- 테마 변형

### 4. 제스처 처리
- 커스텀 드래그 동작
- 멀티터치 처리

---

## 주의사항

### 1. equals/hashCode 구현 필수

```kotlin
// data class 사용 권장 - 자동 구현
private data class CircleElement(val color: Color) : ModifierNodeElement<CircleNode>()

// 일반 class면 직접 구현 필요
override fun equals(other: Any?): Boolean { ... }
override fun hashCode(): Int { ... }
```

### 2. update에서 노드 재생성 금지

```kotlin
override fun update(node: CircleNode) {
    // 좋음: 기존 노드의 속성만 변경
    node.color = color

    // 나쁨: 새 노드 생성 (성능 저하)
    // return CircleNode(color)  // 하지 마세요!
}
```

### 3. invalidate 함수 적절히 호출

```kotlin
// 그래픽 변경 시
invalidateGraphicsLayer()

// 레이아웃 변경 시
invalidateMeasurement()

// 시맨틱 변경 시
invalidateSemantics()

// 전체 그리기 변경 시
invalidateDraw()
```

### 4. DelegatingNode로 조합 시 주의

```kotlin
class CompositeNode : DelegatingNode() {
    // delegate()로 다른 노드에 위임
    val focusNode = delegate(FocusableNode())
    val drawNode = delegate(CustomDrawNode())
}
```

---

## 연습 문제

### 연습 1 (기초): `Modifier.applyIf()` 확장 함수 구현하기

조건이 true일 때만 Modifier를 적용하는 확장 함수를 만들어보세요.

### 연습 2 (중급): DrawModifierNode로 그라데이션 원 Modifier 만들기

콘텐츠 뒤에 그라데이션 원을 그리는 Custom Modifier를 구현하세요.

### 연습 3 (고급): PointerInputModifierNode로 누르면 하이라이트 효과 구현하기

터치하는 동안 배경색이 변하는 Custom Modifier를 구현하세요.

---

## 다음 학습

- [Gesture](../../interaction/gesture/README.md) - 제스처 처리 심화
- [Animation Advanced](../../animation/animation_advanced/README.md) - 고급 애니메이션
- [Custom Layout](../../layout/custom_layout/README.md) - 커스텀 레이아웃

---

## 참고 자료

- [Create custom modifiers - Android Developers](https://developer.android.com/develop/ui/compose/custom-modifiers)
- [Level Up Your Compose Modifiers: Mastering the Node API - droidcon](https://www.droidcon.com/2025/04/02/level-up-your-compose-modifiers-mastering-the-node-api/)
- [Exploring Modifier.Node - RevenueCat](https://www.revenuecat.com/blog/engineering/compose-custom-modifier/)
