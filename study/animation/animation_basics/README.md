# Animation 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Compose에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../state/remember/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Jetpack Compose의 **애니메이션 API**는 상태 변화를 시각적으로 부드럽게 표현할 수 있게 해줍니다. 즉각적인 UI 변화 대신 자연스러운 전환 효과를 제공하여 사용자 경험을 크게 향상시킵니다.

## 핵심 Animation API

### 1. animate*AsState
상태 값이 변경될 때 **자동으로 애니메이션**되는 값을 반환합니다.

> **언제 사용하나요?** 크기, 색상, 위치 등 **단일 값이 변할 때** 사용합니다.

```kotlin
val size by animateDpAsState(
    targetValue = if (isExpanded) 200.dp else 100.dp,
    label = "size"
)
```

**지원 타입:**
- `animateDpAsState`: Dp 값 (크기, 패딩, 오프셋)
- `animateColorAsState`: Color 값 (배경색, 텍스트 색상)
- `animateFloatAsState`: Float 값 (알파, 회전, 스케일)
- `animateIntAsState`: Int 값
- `animateSizeAsState`: Size 값
- `animateOffsetAsState`: Offset 값

### 2. AnimatedVisibility
컴포저블의 **등장/퇴장**을 애니메이션으로 처리합니다.

> **언제 사용하나요?** 요소가 **나타나거나 사라질 때** 사용합니다.

```kotlin
AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn() + slideInVertically(),
    exit = fadeOut() + slideOutVertically()
) {
    Text("나타나거나 사라지는 콘텐츠")
}
```

**기본 동작:**
- Enter: fadeIn() + expandIn()
- Exit: fadeOut() + shrinkOut()

**커스텀 가능한 Transition:**
- `fadeIn()`, `fadeOut()`: 페이드 효과
- `slideInVertically()`, `slideOutVertically()`: 수직 슬라이드
- `slideInHorizontally()`, `slideOutHorizontally()`: 수평 슬라이드
- `expandIn()`, `shrinkOut()`: 확대/축소
- `+` 연산자로 조합 가능

### 3. Crossfade
두 컴포저블 간 **크로스페이드 전환**을 처리합니다.

> **언제 사용하나요?** **다른 컨텐츠로 교체**할 때 (탭 전환, 화면 전환) 사용합니다.

```kotlin
Crossfade(
    targetState = currentScreen,
    label = "screen crossfade"
) { screen ->
    when (screen) {
        "A" -> ScreenA()
        "B" -> ScreenB()
    }
}
```

---

## API 선택 가이드

### 비교 표

| 상황 | 추천 API | 예시 |
|------|----------|------|
| 값이 변할 때 (크기, 색상, 위치) | `animate*AsState` | 버튼 크기 변경, 배경색 전환 |
| 요소가 나타나거나 사라질 때 | `AnimatedVisibility` | 확장 메뉴, 토스트 메시지 |
| 다른 컨텐츠로 교체할 때 | `Crossfade` | 탭 전환, 로딩 상태 변경 |
| 복잡한 컨텐츠 전환 | `AnimatedContent` | 숫자 카운터, 상태별 UI 변경 |

### 의사결정 플로우차트

```
애니메이션이 필요한 상황
        │
        ├── 값이 변하나요? (크기, 색상, 위치 등)
        │       │
        │       └── Yes → animate*AsState
        │
        ├── 요소가 나타나거나 사라지나요?
        │       │
        │       └── Yes → AnimatedVisibility
        │
        └── 다른 컨텐츠로 바뀌나요?
                │
                ├── 단순 페이드 전환 → Crossfade
                │
                └── 복잡한 전환 (슬라이드, 확대 등) → AnimatedContent
```

### AnimatedContent (추가 학습)

`AnimatedContent`는 `AnimatedVisibility`와 `Crossfade`를 결합한 고급 API입니다:

```kotlin
AnimatedContent(
    targetState = count,
    transitionSpec = {
        // 숫자가 증가하면 위에서 아래로, 감소하면 아래에서 위로
        if (targetState > initialState) {
            slideInVertically { -it } togetherWith slideOutVertically { it }
        } else {
            slideInVertically { it } togetherWith slideOutVertically { -it }
        }
    },
    label = "count animation"
) { targetCount ->
    Text(text = "$targetCount")
}
```

---

## 문제 상황: 애니메이션 없는 즉각적인 UI 변화

### 잘못된 코드 예시

```kotlin
@Composable
fun NoAnimationBox() {
    var isExpanded by remember { mutableStateOf(false) }

    // 즉시 변경됨 - 어색한 UX
    val boxSize = if (isExpanded) 200.dp else 100.dp

    Box(
        modifier = Modifier
            .size(boxSize)  // 크기가 갑자기 변함!
            .background(Color.Blue)
    )
}
```

### 발생하는 문제점

1. **시각적 단절**: 크기가 즉시 변경되어 무엇이 변했는지 인지하기 어려움
2. **UX 저하**: 사용자가 버그로 오해할 수 있음
3. **상태 변화 추적 불가**: 이전 상태에서 현재 상태로의 "전환 과정"이 없음

## 해결책: animate*AsState 사용

### 올바른 코드

```kotlin
@Composable
fun AnimatedBox() {
    var isExpanded by remember { mutableStateOf(false) }

    // animateDpAsState로 부드러운 전환
    val boxSize by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 100.dp,
        animationSpec = tween(durationMillis = 300),
        label = "box size"
    )

    Box(
        modifier = Modifier
            .size(boxSize)  // 부드럽게 크기 변경
            .background(Color.Blue)
    )
}
```

### 해결되는 이유

1. **자동 보간**: Compose가 현재 값에서 목표 값까지 자동으로 중간값 계산
2. **Recomposition 최적화**: 애니메이션 중에만 필요한 Recomposition 발생
3. **취소 가능**: 애니메이션 중 새 목표값이 설정되면 자연스럽게 전환

## AnimationSpec 종류

```kotlin
// 1. tween: 시간 기반 (가장 기본)
animateDpAsState(
    targetValue = size,
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
)

// 2. spring: 물리 기반 스프링 (자연스러움)
animateDpAsState(
    targetValue = size,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
)

// 3. keyframes: 프레임 단위 정밀 제어
animateDpAsState(
    targetValue = size,
    animationSpec = keyframes {
        durationMillis = 500
        100.dp at 0 using LinearEasing
        150.dp at 250 using FastOutSlowInEasing
        200.dp at 500
    }
)
```

## 사용 시나리오

1. **크기 변화**: 카드 확장/축소, 버튼 크기 변경
2. **색상 전환**: 선택 상태 표시, 테마 전환
3. **등장/퇴장**: 리스트 아이템 추가/삭제, 팝업/다이얼로그
4. **화면 전환**: 탭 콘텐츠 변경, 온보딩 화면

## 성능 최적화 팁

```kotlin
// 배경색 애니메이션 시 drawBehind 사용 (Recomposition 감소)
val animatedColor by animateColorAsState(
    targetValue = if (isSelected) Color.Blue else Color.Gray,
    label = "background"
)

Box(
    modifier = Modifier
        .size(100.dp)
        .drawBehind {
            drawRect(animatedColor)  // background() 대신 사용
        }
)
```

## 주의사항

1. **label 파라미터**: 디버깅과 Animation Inspector에서 식별을 위해 항상 제공
2. **과도한 애니메이션 지양**: 모든 것에 애니메이션을 적용하면 오히려 UX 저하
3. **접근성 고려**: 애니메이션 감소 설정(Reduce Motion)을 고려할 것

## 연습 문제

### 연습 1: 좋아요 버튼
- animateFloatAsState로 아이콘 크기(scale) 애니메이션
- animateColorAsState로 아이콘 색상 전환

### 연습 2: 확장 카드
- AnimatedVisibility로 추가 정보 표시/숨김
- enter/exit transition 조합 사용

### 연습 3: 탭 전환
- Crossfade로 탭 콘텐츠 부드럽게 전환

## 다음 학습

- **`AnimatedContent`**: 위에서 소개한 고급 컨텐츠 전환 API (심화 학습 권장)
- **`updateTransition`**: 여러 값을 동시에 애니메이션 (상태 머신 기반)
- **`Animatable`**: 저수준 애니메이션 제어 (코루틴 기반)
- **`rememberInfiniteTransition`**: 무한 반복 애니메이션
