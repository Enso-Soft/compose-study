# Custom Modifier 학습

## 개념

**Custom Modifier**는 Jetpack Compose에서 재사용 가능한 UI 수정자를 직접 만드는 방법입니다.
기본 제공 Modifier(padding, background 등) 외에 프로젝트에 특화된 동작이나 스타일을 캡슐화할 수 있습니다.

### Modifier 생성 방식의 변천사

```
Modifier.then()          → 기본 체이닝 (가장 단순)
Modifier.composed {}     → 레거시 API (비권장, 성능 문제)
Modifier.Node            → 최신 권장 API (Compose 1.3.0+)
```

## 핵심 특징

### 1. Modifier.then() - 기본 체이닝

가장 간단한 방식으로, 기존 Modifier들을 연결합니다:

```kotlin
fun Modifier.debugBorder() = this.then(
    Modifier.border(1.dp, Color.Red)
)
```

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

Compose 1.3.0부터 도입된 현대적 접근법입니다:

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

## 주요 Node 타입

| Node 타입 | 용도 |
|----------|------|
| `DrawModifierNode` | 커스텀 그리기 (원, 선, 그라데이션 등) |
| `LayoutModifierNode` | 레이아웃 측정/배치 수정 |
| `PointerInputModifierNode` | 터치/제스처 입력 처리 |
| `SemanticsModifierNode` | 접근성 정보 제공 |
| `DelegatingNode` | 여러 노드 조합 |
| `GraphicsLayerModifierNode` | 그래픽 레이어 수정 (scale, alpha 등) |

## 문제 상황: composed의 성능 문제

### 잘못된 코드 (Modifier.composed 사용)

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

### 발생하는 문제점

1. **Subcomposition 오버헤드**: 각 composed 호출마다 별도의 composition 생성
2. **Skip 불가**: Composable 함수가 반환값을 가지므로 skip 최적화 불가
3. **메모리 압박**: 리스트에서 수많은 remember 인스턴스 생성
4. **프레임 드롭**: 스크롤 시 버벅임 현상

## 해결책: Modifier.Node 사용

### 올바른 구현

```kotlin
// Node는 recomposition을 걸쳐 재사용됨
private class ScaleOnPressNode :
    PointerInputModifierNode,
    GraphicsLayerModifierNode,
    Modifier.Node() {

    private var pressed = false
    private var scale = 1f

    override fun onPointerEvent(...) {
        // 터치 처리 로직
        pressed = event.type == PointerEventType.Press
        scale = if (pressed) 0.95f else 1f
        invalidateGraphicsLayer() // 다시 그리기 요청
    }

    override fun applyGraphicsLayer(scope: GraphicsLayerScope) {
        scope.scaleX = scale
        scope.scaleY = scale
    }
}
```

### 해결되는 이유

1. **노드 재사용**: 같은 Element면 기존 Node의 update만 호출
2. **Subcomposition 없음**: 일반 클래스로 구현되어 오버헤드 없음
3. **Skip 최적화**: Element의 equals가 true면 update도 skip
4. **효율적 무효화**: invalidate* 함수로 필요한 부분만 갱신

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

## 연습 문제

1. **연습 1 (기초)**: `Modifier.thenIf()` 확장 함수 구현하기
2. **연습 2 (중급)**: DrawModifierNode로 그라데이션 테두리 Modifier 만들기
3. **연습 3 (고급)**: PointerInputModifierNode로 누르면 축소되는 효과 구현하기

## 다음 학습

- [Gesture](../gesture) - 제스처 처리 심화
- [Animation Advanced](../animation_advanced) - 고급 애니메이션
- [Custom Layout](../custom_layout) - 커스텀 레이아웃

## 참고 자료

- [Create custom modifiers - Android Developers](https://developer.android.com/develop/ui/compose/custom-modifiers)
- [Level Up Your Compose Modifiers: Mastering the Node API - droidcon](https://www.droidcon.com/2025/04/02/level-up-your-compose-modifiers-mastering-the-node-api/)
- [Exploring Modifier.Node - RevenueCat](https://www.revenuecat.com/blog/engineering/compose-custom-modifier/)
