# Animate Bounds 학습

## 개념

**Modifier.animateBounds()**는 Compose 1.8 (BOM 2025.04.01)에서 도입된 새로운 애니메이션 API입니다. `LookaheadScope` 내에서 Composable의 **위치와 크기 변화를 자동으로 애니메이션**합니다.

기존 `animateContentSize`가 **크기만** 애니메이션하는 것과 달리, `animateBounds`는 **Rect(위치 + 크기)**를 함께 애니메이션하여 레이아웃 전환을 더 부드럽게 처리합니다.

### 실생활 비유

카드 게임에서 카드를 재배치할 때를 상상해보세요:
- **animateContentSize**: 카드 크기만 변하고, 위치는 순간이동
- **animateBounds**: 카드가 부드럽게 새 위치로 이동하면서 크기도 함께 변함

---

## 핵심 API

### 1. LookaheadScope

애니메이션이 발생할 **좌표 공간**을 정의합니다. Lookahead(미리보기) 패스를 통해 최종 레이아웃을 먼저 계산하고, Approach 패스에서 현재 위치에서 목적지까지 점진적으로 이동합니다.

```kotlin
LookaheadScope {
    // 이 스코프 내의 모든 레이아웃 변경이 감지됨
    Column {
        Box(
            Modifier
                .animateBounds(this@LookaheadScope)
                .size(if(expanded) 200.dp else 100.dp)
        )
    }
}
```

### 2. Modifier.animateBounds()

위치와 크기 변화를 자동으로 애니메이션합니다.

```kotlin
Modifier.animateBounds(
    lookaheadScope = this@LookaheadScope,
    boundsTransform = { initialBounds, targetBounds ->
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    }
)
```

**주요 파라미터:**
- `lookaheadScope`: 애니메이션 좌표 공간을 제공하는 LookaheadScope
- `boundsTransform`: 초기/목표 Rect에 따른 애니메이션 스펙 (BoundsTransform)
- `modifier`: 크기/위치 변경을 유발하는 modifier를 래핑 (선택사항)

### 3. BoundsTransform

애니메이션 스펙을 커스터마이징하는 함수형 인터페이스입니다.

```kotlin
val customBoundsTransform = BoundsTransform { initialBounds, targetBounds ->
    // 크기 변화량에 따라 다른 애니메이션 적용
    val sizeDelta = (targetBounds.width - initialBounds.width).absoluteValue
    if (sizeDelta > 100f) {
        spring(stiffness = Spring.StiffnessVeryLow)  // 큰 변화는 천천히
    } else {
        spring(stiffness = Spring.StiffnessMedium)   // 작은 변화는 빠르게
    }
}
```

---

## animateBounds vs animateContentSize 비교

| 특성 | animateContentSize | animateBounds |
|------|-------------------|---------------|
| **애니메이션 대상** | IntSize (크기만) | Rect (위치 + 크기) |
| **LookaheadScope 필수** | X | O |
| **위치 변경 애니메이션** | X | O |
| **사용 시점** | 콘텐츠 크기만 변할 때 | 레이아웃 재배치 시 |
| **Compose 버전** | 초기부터 | 1.8+ (2025.04) |
| **SharedElement 유사** | X | O (동적 전환 가능) |

### animateContentSize의 한계

```kotlin
// animateContentSize: 크기는 애니메이션, 위치는 즉시 변경!
FlowRow {
    items.forEach { item ->
        Box(
            Modifier
                .animateContentSize()  // 크기만 부드럽게
                .size(if(item.expanded) 150.dp else 80.dp)
        )
    }
}
// 결과: 아이템이 확장되면 다른 아이템들이 갑자기 점프함
```

### animateBounds의 해결책

```kotlin
// animateBounds: 크기와 위치 모두 애니메이션!
LookaheadScope {
    FlowRow(Modifier.animateContentSize()) {  // 부모 크기도 애니메이션
        items.forEach { item ->
            Box(
                Modifier
                    .animateBounds(this@LookaheadScope)  // 개별 아이템 위치+크기
                    .size(if(item.expanded) 150.dp else 80.dp)
            )
        }
    }
}
// 결과: 모든 아이템이 부드럽게 새 위치로 이동
```

---

## 문제 상황: animateBounds 없이 레이아웃 변경

### 잘못된 코드 예시

```kotlin
@Composable
fun CardGrid() {
    var selectedId by remember { mutableStateOf<Int?>(null) }

    FlowRow {
        cards.forEach { card ->
            Card(
                modifier = Modifier
                    .size(if(card.id == selectedId) 200.dp else 100.dp)
                    // 크기 변경 시 모든 카드가 즉시 재배치됨!
            ) {
                Text(card.title)
            }
        }
    }
}
```

### 발생하는 문제점

1. **갑작스러운 점프**: 카드 크기가 변하면 다른 카드들이 순간이동
2. **시각적 혼란**: 어떤 카드가 선택되었는지 추적하기 어려움
3. **레이아웃 변경 추적 불가**: Row<->Column 전환 시 요소 위치가 갑자기 변경
4. **사용자 경험 저하**: 자연스러운 흐름 없이 딱딱한 전환

---

## 해결책: LookaheadScope + animateBounds 사용

### 올바른 코드

```kotlin
@Composable
fun CardGrid() {
    var selectedId by remember { mutableStateOf<Int?>(null) }

    LookaheadScope {
        FlowRow(
            modifier = Modifier.animateContentSize()  // 부모 크기 애니메이션
        ) {
            cards.forEach { card ->
                key(card.id) {  // key는 필수!
                    Card(
                        modifier = Modifier
                            .animateBounds(this@LookaheadScope)  // 위치+크기 애니메이션
                            .size(if(card.id == selectedId) 200.dp else 100.dp)
                            .clickable { selectedId = card.id }
                    ) {
                        Text(card.title)
                    }
                }
            }
        }
    }
}
```

### 해결되는 이유

1. **Lookahead Pass**: 최종 레이아웃을 미리 계산 (목적지 파악)
2. **Approach Pass**: 현재 위치에서 목적지까지 점진적 이동
3. **Rect 애니메이션**: x, y, width, height가 모두 함께 애니메이션
4. **key() 활용**: 아이템 식별로 올바른 애니메이션 대상 지정

---

## 사용 시나리오

### 1. 카드 확장/축소
```kotlin
LookaheadScope {
    FlowRow {
        cards.forEach { card ->
            key(card.id) {
                ExpandableCard(
                    modifier = Modifier.animateBounds(this@LookaheadScope)
                )
            }
        }
    }
}
```

### 2. 레이아웃 전환 (Row <-> Column)
```kotlin
LookaheadScope {
    val layout: @Composable () -> Unit = {
        items.forEach { item ->
            key(item.id) {
                ItemBox(Modifier.animateBounds(this@LookaheadScope))
            }
        }
    }

    if (isHorizontal) {
        Row { layout() }
    } else {
        Column { layout() }
    }
}
```

### 3. 리스트 아이템 재정렬
```kotlin
LookaheadScope {
    Column {
        items.forEach { item ->
            key(item.id) {  // key가 있어야 재정렬 시 애니메이션 동작
                ListItem(
                    modifier = Modifier.animateBounds(this@LookaheadScope)
                )
            }
        }
    }
}
```

### 4. 동적 그리드 변경
```kotlin
LookaheadScope {
    val columns = if (isTablet) 4 else 2

    LazyVerticalGrid(columns = GridCells.Fixed(columns)) {
        items(data, key = { it.id }) { item ->
            GridItem(
                modifier = Modifier.animateBounds(this@LookaheadScope)
            )
        }
    }
}
```

---

## BoundsTransform 커스터마이징

### 기본 Spring 애니메이션
```kotlin
val bouncyTransform = BoundsTransform { _, _ ->
    spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,  // 0.5f
        stiffness = Spring.StiffnessLow                   // 200f
    )
}
```

### 조건부 애니메이션
```kotlin
val smartTransform = BoundsTransform { initial, target ->
    val distance = (target.center - initial.center).getDistance()

    when {
        distance > 300f -> tween(durationMillis = 500, easing = FastOutSlowInEasing)
        distance > 100f -> spring(stiffness = Spring.StiffnessLow)
        else -> spring(stiffness = Spring.StiffnessMedium)
    }
}
```

### Keyframes 사용
```kotlin
val keyframeTransform = BoundsTransform { _, _ ->
    keyframes {
        durationMillis = 500
        // 중간에 오버슈트 효과
    }
}
```

---

## modifier 파라미터 활용

특정 Layout Modifier가 animateBounds와 체인될 때 예상치 못한 동작이 발생할 수 있습니다. 이 경우 `modifier` 파라미터를 사용합니다.

### 문제 상황
```kotlin
// size()가 체인에 있어서 부모/자식에 다르게 전파될 수 있음
Modifier
    .animateBounds(lookahead)
    .size(if(expanded) 200.dp else 100.dp)  // 문제 발생 가능
```

### 해결책
```kotlin
// modifier 파라미터로 래핑
Modifier.animateBounds(
    lookaheadScope = lookahead,
    modifier = Modifier.size(if(expanded) 200.dp else 100.dp)  // 내부에서 래핑
)
```

---

## 성능 고려사항

1. **key() 필수 사용**: 리스트 아이템에는 반드시 `key()`를 사용해 안정적인 identity 제공
2. **LookaheadScope 범위**: 너무 넓은 범위는 불필요한 계산 유발
3. **LazyLayout 지원**: LazyColumn/LazyRow/LazyGrid에서도 사용 가능 (Compose 1.8+)
4. **Pager 지원**: HorizontalPager/VerticalPager에서 Lookahead 지원

---

## 주의사항

1. **LookaheadScope 필수**: animateBounds는 LookaheadScope 내에서만 동작
2. **key() 사용**: 재정렬 애니메이션에는 key()가 필수
3. **Compose 1.8+ 필요**: BOM 2025.04.01 이상 사용
4. **중첩 LookaheadScope**: 여러 LookaheadScope를 중첩하면 각각의 좌표 공간이 분리됨
5. **스크롤과의 상호작용**: LookaheadScope가 스크롤 영역과 함께 움직이면 스크롤은 애니메이션 트리거하지 않음

---

## 연습 문제

1. **연습 1**: 기본 animateBounds 적용하기 (난이도: 하)
2. **연습 2**: BoundsTransform으로 커스텀 애니메이션 (난이도: 중)
3. **연습 3**: 리스트 아이템 재정렬 애니메이션 (난이도: 상)

---

## 다음 학습

- [Shared Element Transition](../shared_element_transition/src/main/java/com/example/shared_element_transition/README.md) - 화면 간 공유 요소 전환
- [Animation Advanced](../animation_advanced/src/main/java/com/example/animation_advanced/README.md) - 고급 애니메이션
- [Flow Layout](../flow_layout/src/main/java/com/example/flow_layout/README.md) - FlowRow/FlowColumn

---

## 참고 자료

- [What's new in Jetpack Compose April '25 release](https://android-developers.googleblog.com/2025/04/whats-new-in-jetpack-compose-april-25.html)
- [animateBounds - Composables.com](https://composables.com/animation/animatebounds)
- [3 neat animations you can create with Modifier.animateBounds](https://www.tunjid.com/articles/3-neat-animations-you-can-create-with-modifieranimatebounds-67e474130e9ba862fe18b5e5)
- [Compose Animation Release Notes](https://developer.android.com/jetpack/androidx/releases/compose-animation)
