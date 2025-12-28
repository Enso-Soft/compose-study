# 고급 제스처 처리 (Gesture Advanced)

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `gesture` | 기본 제스처 처리 (clickable, draggable 등) | [📚 학습하기](../gesture/README.md) |
| `Modifier` | Composable의 레이아웃과 동작 수정 | [📚 학습하기](../../layout/layout_and_modifier/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**고급 제스처 처리**는 `pointerInput`과 저수준 제스처 API를 활용하여 복잡한 터치 인터랙션을 구현하는 기술입니다.

단순한 `clickable`, `draggable`, `transformable` Modifier만으로는 구현할 수 없는 **멀티터치 제스처**, **커스텀 제스처**, **플링 애니메이션** 등을 처리할 수 있습니다.

---

## 핵심 특징

1. **pointerInput**: 터치 이벤트를 직접 처리할 수 있는 저수준 Modifier
2. **detectTransformGestures**: 핀치 줌, 회전, 드래그를 한 번에 감지
3. **awaitPointerEventScope**: 커스텀 제스처 감지기를 만들 수 있는 코루틴 공간
4. **VelocityTracker**: 손가락 이동 속도를 추적하여 플링 애니메이션 구현

---

## 문제 상황: 단순 제스처의 한계

### 시나리오: 이미지 뷰어 앱

갤러리 앱처럼 이미지를 핀치 줌하고, 드래그하고, 더블탭으로 확대하는 기능을 구현하려고 합니다.

### 단순 제스처만으로는...

```kotlin
// transformable만 사용하면?
var scale by remember { mutableFloatStateOf(1f) }
var offset by remember { mutableStateOf(Offset.Zero) }

val state = rememberTransformableState { zoomChange, panChange, _ ->
    scale *= zoomChange
    offset += panChange
}

Box(
    modifier = Modifier
        .transformable(state = state)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
)
```

### 발생하는 문제점

1. **줌 범위 제한 불가**: `scale`이 0.001이나 1000까지 무한히 커지거나 작아질 수 있음
2. **화면 경계 제한 불가**: 이미지가 화면 밖으로 완전히 사라질 수 있음
3. **더블탭 줌 불가**: `transformable`은 탭 제스처를 감지할 수 없음
4. **플링 불가**: 빠르게 스와이프해도 관성 이동이 없음
5. **제스처 충돌**: 여러 제스처를 조합할 때 의도치 않은 동작 발생

---

## 해결책: 고급 제스처 API

### 1. detectTransformGestures - 멀티터치 통합 처리

```kotlin
var scale by remember { mutableFloatStateOf(1f) }
var offset by remember { mutableStateOf(Offset.Zero) }

Box(
    modifier = Modifier
        .pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, rotation ->
                // 줌 범위를 0.5배 ~ 3배로 제한
                scale = (scale * zoom).coerceIn(0.5f, 3f)

                // 오프셋 적용
                offset += pan
            }
        }
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y
        )
)
```

**핵심 포인트**:
- `centroid`: 제스처의 중심점 (두 손가락 사이의 중간 지점)
- `pan`: 이동 거리 (Offset)
- `zoom`: 확대/축소 비율 (1.0 = 변화 없음, 1.1 = 10% 확대)
- `rotation`: 회전 각도 (라디안)

### 2. awaitPointerEventScope - 저수준 이벤트 처리

```kotlin
Modifier.pointerInput(Unit) {
    awaitPointerEventScope {
        // 첫 번째 터치 이벤트 대기
        val down = awaitFirstDown()

        // 터치가 끝날 때까지 드래그 추적
        drag(down.id) { change ->
            // 터치 이동 처리
            offset += change.positionChange()
            change.consume()  // 이벤트 소비 (다른 제스처로 전파 방지)
        }
    }
}
```

### 3. VelocityTracker - 플링 구현

```kotlin
val velocityTracker = remember { VelocityTracker() }
val offsetX = remember { Animatable(0f) }

Modifier.pointerInput(Unit) {
    val decay = splineBasedDecay<Float>(this)

    awaitPointerEventScope {
        while (true) {
            val pointerId = awaitFirstDown().id
            velocityTracker.resetTracking()

            drag(pointerId) { change ->
                velocityTracker.addPosition(
                    change.uptimeMillis,
                    change.position
                )
                launch { offsetX.snapTo(offsetX.value + change.positionChange().x) }
            }

            // 드래그 종료 -> 플링 시작
            val velocity = velocityTracker.calculateVelocity().x
            launch {
                offsetX.animateDecay(velocity, decay)
            }
        }
    }
}
```

### 4. 더블탭 줌 구현

```kotlin
Modifier.pointerInput(Unit) {
    detectTapGestures(
        onDoubleTap = { tapOffset ->
            // 현재 1배면 2배로, 아니면 1배로
            scale = if (scale > 1f) 1f else 2f
        }
    )
}
```

### 5. 여러 제스처 조합

```kotlin
// 방법 1: 별도의 pointerInput 사용
Modifier
    .pointerInput(Unit) {
        detectTapGestures(onDoubleTap = { scale = if (scale > 1f) 1f else 2f })
    }
    .pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            scale = (scale * zoom).coerceIn(0.5f, 3f)
            offset += pan
        }
    }

// 방법 2: awaitEachGesture로 직접 구현
Modifier.pointerInput(Unit) {
    awaitEachGesture {
        val down = awaitFirstDown()
        val up = waitForUpOrCancellation() ?: return@awaitEachGesture

        // 더블탭 감지
        val secondDown = withTimeoutOrNull(viewConfiguration.doubleTapTimeoutMillis) {
            awaitFirstDown()
        }

        if (secondDown != null) {
            waitForUpOrCancellation()
            // 더블탭 처리
            scale = if (scale > 1f) 1f else 2f
        }
    }
}
```

---

## 사용 시나리오

1. **이미지 뷰어**: 핀치 줌 + 드래그 + 더블탭 줌
2. **지도 앱**: 핀치 줌 + 회전 + 플링 이동
3. **드로잉 앱**: 한 손가락 그리기 + 두 손가락 캔버스 이동
4. **게임**: 멀티터치 조작 + 커스텀 제스처 인식

---

## 주의사항

### 1. pointerInput의 key 파라미터

```kotlin
// key가 변경되면 제스처 처리기가 재생성됨
Modifier.pointerInput(userId) {  // userId가 변경되면 재시작
    detectTapGestures { ... }
}

// Unit을 사용하면 한 번만 생성
Modifier.pointerInput(Unit) {
    detectTapGestures { ... }
}
```

### 2. 이벤트 소비 (consume)

```kotlin
// consume()을 호출하면 다른 제스처가 이 이벤트를 받지 않음
drag(pointerId) { change ->
    offset += change.positionChange()
    change.consume()  // 부모 스크롤 등에게 전파 방지
}
```

### 3. 제스처 충돌 해결

```kotlin
// detectTransformGestures와 detectDragGestures를 동시에 사용하면 충돌!
// 해결책: 별도의 pointerInput으로 분리하거나, awaitEachGesture로 직접 구현
```

---

## 연습 문제

### 연습 1: 핀치 줌 이미지 뷰어 (쉬움)

`detectTransformGestures`를 사용하여 이미지를 핀치 줌으로 확대/축소하는 기능을 구현하세요.

**요구사항**:
- 줌 범위: 0.5배 ~ 3배
- `graphicsLayer`로 스케일 적용

### 연습 2: 이동 가능한 줌 뷰어 (중간)

핀치 줌과 함께 드래그로 이미지를 이동할 수 있는 뷰어를 구현하세요.

**요구사항**:
- 핀치 줌 (0.5배 ~ 3배)
- 드래그로 이미지 이동
- (보너스) 화면 경계 밖으로 나가지 않도록 제한

### 연습 3: 플링 가능한 이미지 뷰어 (어려움)

완전한 이미지 뷰어를 구현하세요.

**요구사항**:
- 핀치 줌 + 드래그
- 빠르게 스와이프하면 관성 이동 (플링)
- 더블탭으로 2배 줌/원래 크기 토글

---

## 다음 학습

- [Drag and Drop](../drag_and_drop/) - 드래그 앤 드롭 구현
- [Haptic Feedback](../haptic_feedback/) - 햅틱 피드백 추가
- [Focus Management](../focus_management/) - 포커스 관리
