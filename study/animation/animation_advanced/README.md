# Animation Advanced 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `animation` | 기본 애니메이션 API (animate*AsState, AnimatedVisibility 등) | [📚 학습하기](../../animation/animation_basics/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Animation Advanced는 Jetpack Compose에서 **고급 애니메이션 제어**를 위한 API들입니다:

- **updateTransition**: 여러 속성을 동시에 애니메이션화하고 상태 전환을 동기화
- **Animatable**: 코루틴 기반 명령형 애니메이션 제어
- **AnimationSpec 커스터마이징**: spring, keyframes 등으로 정밀한 타이밍 제어

## 핵심 특징

### 1. updateTransition
```kotlin
val transition = updateTransition(targetState = state, label = "myTransition")

val color by transition.animateColor(label = "color") { state ->
    when (state) {
        State.Collapsed -> Color.Gray
        State.Expanded -> Color.Blue
    }
}

val size by transition.animateDp(label = "size") { state ->
    when (state) {
        State.Collapsed -> 100.dp
        State.Expanded -> 200.dp
    }
}
```

- 모든 애니메이션이 **같은 상태 전환에 동기화**
- animateColor, animateDp, animateFloat 등 다양한 확장 함수
- transitionSpec으로 각 속성별 AnimationSpec 커스터마이징 가능

### 2. Animatable
```kotlin
val offset = remember { Animatable(0f) }
val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch {
        offset.animateTo(100f, spring(stiffness = Spring.StiffnessHigh))
        offset.animateTo(0f)
    }
})
```

- **Composable 밖에서도 사용 가능** (코루틴 기반)
- 이벤트 핸들러에서 애니메이션 직접 제어
- animateTo(), snapTo(), animateDecay() 메서드

### 3. AnimationSpec 종류
| 종류 | 설명 | 사용 시점 |
|------|------|----------|
| spring | 물리 기반, 바운스 효과 | 자연스러운 움직임 |
| tween | 시간 기반, 이징 적용 | 정해진 시간 내 완료 필요 |
| keyframes | 특정 시점에 특정 값 | 복잡한 경로 애니메이션 |
| snap | 즉시 전환 | 애니메이션 없이 변경 |

## 문제 상황: animate*AsState만으로는 부족한 경우

### 잘못된 코드 예시
```kotlin
// 각각 독립적인 애니메이션 → 동기화 문제 가능
val color by animateColorAsState(if (expanded) Color.Blue else Color.Gray)
val size by animateDpAsState(if (expanded) 200.dp else 100.dp)
val alpha by animateFloatAsState(if (expanded) 1f else 0.5f)

// 빠르게 상태를 토글하면 각 애니메이션이 다른 타이밍에 있을 수 있음
```

### 발생하는 문제점
1. **동기화 문제**: 여러 속성이 독립적으로 애니메이션 → 불일치 발생 가능
2. **이벤트 기반 제어 불가**: 버튼 클릭 시 특정 애니메이션 시퀀스 실행 어려움
3. **복잡한 타이밍 제어 불가**: 바운스, 키프레임 경로 등 정밀 제어 한계

## 해결책

### 해결책 1: updateTransition으로 동기화
```kotlin
enum class CardState { Collapsed, Expanded }

val transition = updateTransition(cardState, label = "card")

val color by transition.animateColor(label = "color") { ... }
val size by transition.animateDp(label = "size") { ... }
val alpha by transition.animateFloat(label = "alpha") { ... }

// 모든 애니메이션이 같은 전환에 바인딩 → 항상 동기화
```

### 해결책 2: Animatable로 명령형 제어
```kotlin
val offsetX = remember { Animatable(0f) }
val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch {
        // Shake 애니메이션: 좌우로 흔들기
        repeat(3) {
            offsetX.animateTo(-10f, spring(stiffness = Spring.StiffnessHigh))
            offsetX.animateTo(10f, spring(stiffness = Spring.StiffnessHigh))
        }
        offsetX.animateTo(0f)
    }
})
```

### 해결책 3: AnimationSpec 커스터마이징
```kotlin
// Spring: 물리적으로 자연스러운 바운스
spring(
    dampingRatio = Spring.DampingRatioHighBouncy, // 0.2f
    stiffness = Spring.StiffnessLow // 200f
)

// Keyframes: 정밀한 타이밍
keyframes {
    durationMillis = 1000
    0f at 0 using LinearEasing
    0.5f at 300 using FastOutSlowInEasing
    1f at 1000
}
```

---

## API 선택 가이드

### updateTransition vs Animatable 비교

| 기준 | updateTransition | Animatable |
|------|------------------|------------|
| **제어 방식** | 선언적 (상태 기반) | 명령형 (코루틴 기반) |
| **사용 위치** | Composable 내부만 | 어디서든 (이벤트 핸들러 포함) |
| **다중 속성 동기화** | 자동 동기화 | 수동 제어 필요 |
| **애니메이션 시퀀스** | 어려움 | 순차 실행 용이 |
| **적합한 경우** | 상태 전환 (확장/축소, ON/OFF) | 이벤트 기반 (클릭 시 흔들기) |

### AnimationSpec 종류 비교 (확장)

| 종류 | 특징 | 적합한 상황 | 예시 |
|------|------|------------|------|
| **spring** | 물리 기반, 바운스 | 자연스러운 움직임 | 카드 확장, 드래그 복귀 |
| **tween** | 시간 기반, 이징 | 정해진 시간 내 완료 | 페이드 인/아웃, 슬라이드 |
| **keyframes** | 특정 시점에 특정 값 | 복잡한 경로 | 로딩 도트, 알림 벨 |
| **snap** | 즉시 전환 | 애니메이션 건너뛰기 | 초기화, 리셋 |
| **repeatable** | 유한 반복 | N회 반복 필요 | 주의 끌기 효과 |
| **infiniteRepeatable** | 무한 반복 | 계속 반복 | 로딩 인디케이터 |

### 의사결정 플로우차트

```
애니메이션이 필요한 상황
    |
    +-- 여러 속성을 동시에 동기화해야 하는가?
    |       |
    |       +-- Yes --> updateTransition 사용
    |       |           (예: 카드 확장 시 크기+색상+코너 동시 변경)
    |       |
    |       +-- No --> 다음 질문으로
    |
    +-- 이벤트 발생 시 애니메이션 시퀀스를 실행해야 하는가?
    |       |
    |       +-- Yes --> Animatable + coroutineScope 사용
    |       |           (예: 버튼 클릭 시 좌->우->중앙 흔들기)
    |       |
    |       +-- No --> 다음 질문으로
    |
    +-- 바운스/오버슈트 효과가 필요한가?
    |       |
    |       +-- Yes --> spring(dampingRatio = HighBouncy) 사용
    |       |
    |       +-- No --> 다음 질문으로
    |
    +-- 정확한 시간 내에 완료되어야 하는가?
    |       |
    |       +-- Yes --> tween(durationMillis = N) 사용
    |       |
    |       +-- No --> 다음 질문으로
    |
    +-- 특정 시점에 특정 값을 지나야 하는가?
            |
            +-- Yes --> keyframes 사용
            |
            +-- No --> 기본 spring 사용 (가장 자연스러움)
```

---

## 사용 시나리오

### 1. updateTransition
- 카드 확장/축소 시 크기, 색상, 코너 라운드 동시 변경
- 토글 버튼 ON/OFF 전환
- 탭 전환 시 여러 요소 동시 애니메이션

### 2. Animatable
- 버튼 클릭 시 흔들림(Shake) 효과
- 드래그 후 원래 위치로 복귀
- 성공/실패 시 특정 애니메이션 시퀀스 실행

### 3. Spring AnimationSpec
- 카드 확장 시 바운스 효과
- 리스트 아이템 스와이프 후 복귀
- 자연스러운 물리 기반 움직임

### 4. Keyframes AnimationSpec
- 로딩 인디케이터 (도트 순차 점프)
- 알림 벨 흔들림
- 복잡한 경로를 따르는 애니메이션

## 주의사항

1. **updateTransition은 상태 기반**: 상태 객체(enum, sealed class)로 관리
2. **Animatable은 remember로 저장**: Recomposition에도 값 유지
3. **Spring은 duration이 없음**: 물리 시뮬레이션이므로 끝나는 시점 예측 어려움
4. **Keyframes의 at 순서**: 반드시 시간순으로 정의

## 연습 문제

### 연습 1: updateTransition 토글 버튼 (초급)
- ON/OFF 토글 버튼 구현
- 배경색과 핸들 위치 동시 애니메이션

### 연습 2: Animatable 탄성 카운터 (중급)
- 숫자 변경 시 scale 바운스 효과
- LaunchedEffect와 Animatable 연동

### 연습 3: Keyframes 로딩 도트 (고급)
- 3개 도트가 순차적으로 점프
- infiniteRepeatable + keyframes 조합

## 다음 학습

- `rememberInfiniteTransition`: 무한 반복 애니메이션
- `AnimatedContent`: 컨텐츠 교체 시 커스텀 전환
- `Modifier.animateContentSize()`: 크기 변화 자동 애니메이션
