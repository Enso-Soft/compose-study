# 물리 기반 애니메이션 (Physics-Based Animation) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `animation_basics` | Compose 애니메이션 기본 개념 | [📚 학습하기](../animation_basics/README.md) |
| `gesture_basics` | 터치 제스처 처리 기본 | [📚 학습하기](../../interaction/gesture_basics/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

물리 기반 애니메이션은 **현실 세계의 물리 법칙을 시뮬레이션**하여 자연스럽고 생동감 있는 UI 움직임을 만드는 방법입니다.

Compose에서는 두 가지 핵심 물리 애니메이션을 제공합니다:
- **spring()**: 스프링(용수철)의 탄성을 이용한 바운스 애니메이션
- **animateDecay()**: 관성을 이용한 감속 애니메이션

## 핵심 특징

1. **자연스러운 움직임**: 실제 물리 법칙을 따르므로 사용자에게 익숙하고 자연스럽게 느껴집니다
2. **중단 가능(Interruptible)**: 애니메이션 중간에 새 타겟으로 변경해도 속도가 연속적으로 유지됩니다
3. **제스처 연결**: 드래그 속도를 자연스럽게 애니메이션으로 전환할 수 있습니다

---

## 문제 상황: tween 애니메이션의 한계

### 시나리오

버튼을 클릭할 때 크기가 줄어들었다 커지는 피드백 효과를 구현하려고 합니다.
시간 기반 애니메이션(tween)을 사용하면 어떤 문제가 발생할까요?

### 잘못된 코드 예시

```kotlin
// tween 애니메이션 사용
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.9f else 1f,
    animationSpec = tween(durationMillis = 150, easing = LinearEasing),
    label = "scale"
)
```

### 발생하는 문제점

1. **기계적인 움직임**: 항상 정확히 150ms 동안 움직여서 로봇 같은 느낌
2. **중단 시 속도 불연속**: 빠르게 연타하면 애니메이션이 끊기고 새로 시작
3. **제스처 연결 불가**: 드래그 속도를 애니메이션에 반영할 방법이 없음
4. **사용자 경험 저하**: 앱이 "딱딱하다", "반응이 느리다"는 인상

---

## 해결책: spring/decay 사용

### Spring 애니메이션

스프링 물리를 이용해 **탄성 있는 바운스 효과**를 만듭니다.

```kotlin
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.9f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,  // 바운스 정도
        stiffness = Spring.StiffnessMedium               // 속도/강성
    ),
    label = "scale"
)
```

#### dampingRatio (바운스 정도)

값이 작을수록 더 많이 튀어오릅니다 (공이 바닥에 부딪히는 것처럼):

| 상수 | 값 | 효과 |
|------|-----|------|
| `DampingRatioHighBouncy` | 0.2f | 많이 튀어오름 |
| `DampingRatioMediumBouncy` | 0.5f | 적당히 튀어오름 |
| `DampingRatioLowBouncy` | 0.75f | 조금 튀어오름 |
| `DampingRatioNoBouncy` | 1.0f | 튀지 않음 (기본값) |

#### stiffness (강성/속도)

값이 클수록 더 빨리 목표에 도달합니다 (뻣뻣한 스프링 vs 느슨한 스프링):

| 상수 | 값 | 효과 |
|------|-----|------|
| `StiffnessHigh` | 10000f | 매우 빠름 |
| `StiffnessMedium` | 1500f | 중간 (기본값) |
| `StiffnessMediumLow` | 400f | 약간 느림 |
| `StiffnessLow` | 200f | 느림 |
| `StiffnessVeryLow` | 50f | 매우 느림 |

### Decay 애니메이션

관성을 이용해 **자연스럽게 감속하는 fling 효과**를 만듭니다.

```kotlin
val decay = splineBasedDecay<Float>(LocalDensity.current)

// 드래그 종료 시 현재 속도로 감속 시작
scope.launch {
    offsetAnimatable.animateDecay(
        initialVelocity = velocity,  // 드래그 속도
        animationSpec = decay
    )
}
```

### 해결되는 이유

1. **자연스러운 물리**: 스프링 탄성과 관성 감속이 현실 세계와 유사
2. **속도 연속성 보장**: 애니메이션 중단 시에도 현재 속도를 유지하며 새 타겟으로 이동
3. **제스처 완벽 연결**: VelocityTracker로 측정한 속도를 animateDecay에 그대로 전달
4. **Material Design 권장**: Google이 spring을 기본 애니메이션으로 권장

---

## Spring 조합 가이드

| 사용 사례 | dampingRatio | stiffness | 비고 |
|----------|--------------|-----------|------|
| 버튼 클릭 피드백 | MediumBouncy | Medium | 생동감 있는 반응 |
| 카드 스냅백 | MediumBouncy | Low | 부드러운 복귀 |
| 빠른 화면 전환 | NoBouncy | High | 즉각적 전환 |
| 귀여운 캐릭터 | HighBouncy | VeryLow | 통통 튀는 효과 |
| 모달 등장 | LowBouncy | MediumLow | 우아한 등장 |

---

## Decay 종류

### splineBasedDecay

플랫폼 표준 fling 동작입니다. Android의 스크롤과 동일한 감속 곡선을 사용합니다.

```kotlin
val decay = splineBasedDecay<Float>(LocalDensity.current)
```

### exponentialDecay

마찰 계수를 직접 조절할 수 있는 지수 감속입니다.

```kotlin
val decay = exponentialDecay<Float>(
    frictionMultiplier = 1f,     // 마찰 (클수록 빨리 멈춤)
    absVelocityThreshold = 0.1f  // 멈춤 판정 속도
)
```

---

## 사용 시나리오

1. **버튼 클릭 피드백**: 클릭 시 크기가 줄었다 바운스하며 복귀
2. **드래그 스냅백**: 카드를 드래그하고 놓으면 원위치로 스프링 복귀
3. **스와이프 삭제**: 빠르게 스와이프하면 관성으로 화면 밖으로 나감
4. **리스트 아이템 추가/제거**: 새 아이템이 바운스하며 등장
5. **Pull-to-Refresh**: 당겼다 놓으면 스프링처럼 복귀

---

## 주의사항

- **spring()은 무한 시간이 아닙니다**: visibilityThreshold에 도달하면 종료됩니다
- **animateDecay는 경계를 넘을 수 있습니다**: updateBounds()로 제한하세요
- **VelocityTracker는 재사용하지 마세요**: 매 제스처마다 새로 생성하세요

---

## 연습 문제

### 연습 1: 바운스 좋아요 버튼 (쉬움)

좋아요 버튼을 누르면 하트가 커졌다 바운스하며 원래 크기로 돌아오게 하세요.

**힌트**:
- animateFloatAsState + spring() 사용
- dampingRatio: MediumBouncy 권장
- 크기 변화: 1f -> 1.3f -> 1f

### 연습 2: 드래그 스냅백 카드 (중간)

카드를 좌우로 드래그하면 따라오고, 손을 떼면 스프링처럼 원위치로 돌아오게 하세요.

**힌트**:
- Animatable(0f)로 offsetX 생성
- detectDragGestures 사용
- onDrag: snapTo, onDragEnd: animateTo(0f, spring())

### 연습 3: 스와이프 삭제 (어려움)

아이템을 빠르게 스와이프하면 관성으로 미끄러지다가 화면 밖으로 나가면 삭제되게 하세요.

**힌트**:
- VelocityTracker + splineBasedDecay 사용
- decay.calculateTargetValue()로 도착 예측
- 화면 절반 이상이면 삭제, 아니면 스프링 복귀

---

## 다음 학습

- **SharedElementTransition**: 화면 간 요소 전환 애니메이션
- **AnimatedContent**: 콘텐츠 전환 애니메이션
- **MotionScheme**: Material Expressive의 새로운 모션 시스템
