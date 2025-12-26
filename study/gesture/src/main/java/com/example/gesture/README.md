# Gesture 처리 학습

## 개념

Jetpack Compose의 **Gesture 처리**는 사용자의 터치 입력(탭, 드래그, 핀치 줌 등)을 감지하고 처리하는 시스템입니다. Compose는 세 가지 추상화 레벨을 제공하며, 상황에 맞는 레벨을 선택하는 것이 중요합니다.

## 핵심 특징

### 1. 세 가지 추상화 레벨

```
Level 1: 컴포넌트 지원 (최상위) - Button, LazyColumn 등
         ↓
Level 2: Gesture Modifiers    - clickable, draggable 등
         ↓
Level 3: pointerInput (최하위) - detectTapGestures, detectDragGestures 등
```

**원칙: 항상 가능한 가장 높은 레벨을 사용하라!**
- 높은 레벨 = 접근성, 시맨틱스, 모범 사례 자동 적용
- 낮은 레벨 = 더 많은 제어권, 더 많은 책임

### 2. Gesture Modifiers (Level 2)

| Modifier | 용도 |
|----------|------|
| `clickable` | 탭 감지 + 리플 효과 + 접근성 |
| `combinedClickable` | 탭 + 롱프레스 + 더블탭 |
| `draggable` | 단일 방향 드래그 |
| `transformable` | 확대/축소/회전 (멀티터치) |

### 3. pointerInput + detectGestures (Level 3)

```kotlin
Modifier.pointerInput(key) {
    detectTapGestures(
        onTap = { offset -> /* 탭 위치 */ },
        onDoubleTap = { offset -> /* 더블탭 위치 */ },
        onLongPress = { offset -> /* 롱프레스 위치 */ }
    )
}
```

**사용 시점:**
- 탭 위치(Offset)가 필요할 때
- 커스텀 제스처 로직이 필요할 때
- 여러 제스처 조합이 필요할 때

---

## 문제 상황: clickable만으로는 부족할 때

### 잘못된 코드 예시

```kotlin
// 문제: 이미지 더블탭 시 해당 위치로 줌 인하고 싶은데...
var zoomed by remember { mutableStateOf(false) }

Image(
    modifier = Modifier.clickable {
        zoomed = !zoomed  // 더블탭 감지 불가, 탭 위치도 모름!
    }
)
```

### 발생하는 문제점

1. **더블탭 미지원**: `clickable`은 단일 탭만 감지
2. **위치 정보 없음**: 어디를 탭했는지 알 수 없음
3. **드래그 불가**: 요소를 이동시킬 수 없음
4. **멀티터치 불가**: 핀치 줌, 회전 불가능

---

## 해결책: pointerInput 사용

### 올바른 코드 - detectTapGestures

```kotlin
var zoomed by remember { mutableStateOf(false) }
var zoomCenter by remember { mutableStateOf(Offset.Zero) }

Image(
    modifier = Modifier
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { tapOffset ->
                    zoomCenter = tapOffset  // 탭 위치 저장!
                    zoomed = !zoomed
                }
            )
        }
        .graphicsLayer {
            scaleX = if (zoomed) 2f else 1f
            scaleY = if (zoomed) 2f else 1f
            transformOrigin = TransformOrigin(
                zoomCenter.x / size.width,
                zoomCenter.y / size.height
            )
        }
)
```

### 올바른 코드 - detectDragGestures

```kotlin
var offset by remember { mutableStateOf(Offset.Zero) }

Box(
    modifier = Modifier
        .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()  // 이벤트 소비!
                offset += dragAmount
            }
        }
)
```

### 올바른 코드 - detectTransformGestures

```kotlin
var scale by remember { mutableFloatStateOf(1f) }
var rotation by remember { mutableFloatStateOf(0f) }
var offset by remember { mutableStateOf(Offset.Zero) }

Image(
    modifier = Modifier
        .pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, rotationChange ->
                scale *= zoom
                rotation += rotationChange
                offset += pan
            }
        }
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            rotationZ = rotation
            translationX = offset.x
            translationY = offset.y
        }
)
```

### 해결되는 이유

1. **상세 정보 제공**: 탭 위치, 드래그 거리, 줌 비율 등
2. **모든 제스처 지원**: 탭, 더블탭, 롱프레스, 드래그, 핀치, 회전
3. **완전한 제어**: 제스처 시작/종료 콜백, 이벤트 소비 제어

---

## detect* 함수 종류

| 함수 | 용도 | 콜백 |
|------|------|------|
| `detectTapGestures` | 탭 관련 제스처 | onTap, onDoubleTap, onLongPress, onPress |
| `detectDragGestures` | 2D 드래그 | onDragStart, onDrag, onDragEnd, onDragCancel |
| `detectHorizontalDragGestures` | 가로 드래그 | 위와 동일 |
| `detectVerticalDragGestures` | 세로 드래그 | 위와 동일 |
| `detectTransformGestures` | 멀티터치 변환 | (centroid, pan, zoom, rotation) |
| `detectDragGesturesAfterLongPress` | 롱프레스 후 드래그 | 위와 동일 |

---

## 주의사항

### 1. 하나의 pointerInput에 하나의 detector만!

```kotlin
// 잘못된 예 - 두 번째 detector는 실행 안 됨!
Modifier.pointerInput(Unit) {
    detectTapGestures { /* ... */ }
    detectDragGestures { _, _ -> /* 이건 실행 안 됨! */ }
}

// 올바른 예 - 별도 pointerInput 사용
Modifier
    .pointerInput(Unit) { detectTapGestures { /* ... */ } }
    .pointerInput(Unit) { detectDragGestures { _, _ -> /* ... */ } }
```

### 2. key 파라미터 활용

```kotlin
// onClose가 변경되면 pointerInput 재설정
Modifier.pointerInput(onClose) {
    detectTapGestures { onClose() }
}
```

### 3. change.consume() 호출

```kotlin
detectDragGestures { change, dragAmount ->
    change.consume()  // 부모로 이벤트 전파 방지
    // ...
}
```

### 4. 접근성 고려

`pointerInput`은 접근성 정보를 자동 제공하지 않음:

```kotlin
Modifier
    .pointerInput(Unit) { detectTapGestures { onClose() } }
    .semantics {
        onClick { onClose(); true }  // 수동으로 접근성 추가
    }
```

---

## 연습 문제

### 연습 1: 탭 카운터 (기초)
탭/더블탭/롱프레스를 각각 카운트하는 UI 구현

### 연습 2: 드래그 가능한 카드 (중급)
화면에서 자유롭게 드래그할 수 있는 카드 구현

### 연습 3: 이미지 뷰어 (고급)
핀치 줌과 회전이 가능한 이미지 뷰어 구현

---

## 다음 학습

- Canvas / Drawing: 커스텀 그래픽 그리기
- Animation: 제스처와 애니메이션 연동
- Pager: 스와이프 기반 페이지 전환

---

## 참고 자료

- [Android Developers - Understand gestures](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/understand-gestures)
- [Android Developers - Tap and press](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/tap-and-press)
- [Android Developers - Drag, swipe, and fling](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/drag-swipe-fling)
