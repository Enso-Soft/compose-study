# Haptic Feedback 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `gesture_basics` | 터치 제스처 처리 기본 | [📚 학습하기](../gesture_basics/README.md) |
| `Modifier` | Composable의 레이아웃과 동작 수정 | [📚 학습하기](../../layout/layout_and_modifier/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Haptic Feedback(햅틱 피드백)**은 터치 인터랙션에 진동 피드백을 추가하여 사용자가 손가락으로 "느낄 수 있는" 반응을 제공하는 기능입니다. 버튼을 누르거나 슬라이더를 조작할 때 적절한 진동 피드백을 제공하면 사용자 경험(UX)이 크게 향상됩니다.

> **비유**: 햅틱 피드백은 스마트폰의 '손가락 언어'입니다. 시각적 변화만으로는 전달하기 어려운 정보를 촉각으로 전달합니다.

## 핵심 특징

1. **LocalHapticFeedback**: Compose가 제공하는 표준 햅틱 피드백 API
2. **HapticFeedbackType**: LongPress, TextHandleMove 등 햅틱 타입
3. **LocalView**: 더 다양한 HapticFeedbackConstants 접근 가능
4. **VIBRATE 권한 불필요**: HapticFeedbackConstants 사용 시 별도 권한 없이 동작

---

## 문제 상황: 피드백 없는 터치 인터랙션

### 시나리오

사용자가 앱에서 버튼을 누르거나 슬라이더를 조작할 때, 화면의 시각적 변화만 있고 촉각적 반응이 없다면 어떤 문제가 발생할까요?

### 발생하는 문제점

1. **확신 부족**: 사용자가 액션이 수행되었는지 확신하기 어려움
2. **시각 의존**: 화면을 보지 않으면 인터랙션 상태를 파악할 수 없음
3. **접근성 문제**: 시각 장애 사용자에게 정보 전달이 어려움
4. **만족도 저하**: 인터랙션의 "느낌"이 부족해 사용 만족도가 떨어짐

### 잘못된 코드 예시

```kotlin
// 햅틱 피드백이 없는 버튼
Button(onClick = {
    isLiked = !isLiked
    // 촉각 피드백 없음!
}) {
    Icon(
        imageVector = if (isLiked) Icons.Filled.Favorite
                      else Icons.Outlined.FavoriteBorder
    )
}
```

---

## 해결책: Haptic Feedback 사용

### 방법 1: LocalHapticFeedback (Compose 표준)

Compose가 제공하는 표준 방식으로, `LongPress`와 `TextHandleMove` 타입을 지원합니다.

```kotlin
val haptics = LocalHapticFeedback.current

Button(onClick = {
    // 햅틱 피드백 발생!
    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
    isLiked = !isLiked
}) {
    Icon(...)
}
```

### 방법 2: LocalView (더 다양한 타입)

View의 `performHapticFeedback`을 사용하면 더 다양한 햅틱 타입에 접근할 수 있습니다.

```kotlin
val view = LocalView.current

Button(onClick = {
    // CLOCK_TICK, CONFIRM, REJECT 등 다양한 타입 사용 가능
    view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
}) {
    Text("성공")
}
```

### 방법 3: 슬라이더 틱 햅틱

`snapshotFlow`를 사용하여 값이 특정 단계를 넘을 때마다 틱 햅틱을 발생시킵니다.

```kotlin
val view = LocalView.current
var volume by remember { mutableFloatStateOf(50f) }

LaunchedEffect(Unit) {
    snapshotFlow { volume }
        .map { (it / 10).toInt() }  // 10단위로 변환
        .distinctUntilChanged()      // 변경 시에만
        .drop(1)                     // 초기값 스킵
        .collect {
            view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
        }
}

Slider(
    value = volume,
    onValueChange = { volume = it },
    valueRange = 0f..100f,
    steps = 9  // 10단계
)
```

### 방법 4: 롱프레스 햅틱

`combinedClickable`의 `onLongClick`에서 햅틱 피드백을 발생시킵니다.

```kotlin
val haptics = LocalHapticFeedback.current

Card(
    modifier = Modifier.combinedClickable(
        onClick = { /* 일반 클릭 */ },
        onLongClick = {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            isSelected = true
        }
    )
) {
    // 카드 내용
}
```

---

## HapticFeedbackType 비교

| 타입 | 용도 | 제공 방식 | 느낌 |
|------|------|----------|------|
| `LongPress` | 롱프레스 확인 | LocalHapticFeedback | 중간 강도 진동 |
| `TextHandleMove` | 텍스트 선택 | LocalHapticFeedback | 미세한 진동 |
| `CLOCK_TICK` | 틱/스크롤 | LocalView | 시계 초침 느낌 |
| `CONFIRM` | 성공 피드백 | LocalView (API 30+) | 부드러운 '톡' |
| `REJECT` | 실패 피드백 | LocalView (API 30+) | 강한 '덜덜' |
| `KEYBOARD_PRESS` | 키보드 입력 | LocalView | 키 누르는 느낌 |

---

## 사용 시나리오

### 1. 중요한 버튼 클릭
결제 버튼, 삭제 확인 등 중요한 액션에 `LongPress` 또는 `CONFIRM` 햅틱 추가

### 2. 슬라이더/피커 조작
볼륨, 밝기, 시간 선택 등에서 단계마다 `CLOCK_TICK` 햅틱 추가

### 3. 롱프레스 인식 확인
컨텍스트 메뉴, 다중 선택 등 롱프레스 인식 시 `LongPress` 햅틱으로 피드백

### 4. 드래그 앤 드롭
드래그 시작, 이동, 드롭 성공/실패에 각각 다른 햅틱 적용

---

## 주의사항

### 1. 에뮬레이터 제한
- 햅틱 피드백은 **실제 디바이스에서만 동작**합니다
- 에뮬레이터에서는 테스트할 수 없으니 반드시 실기기에서 확인하세요

### 2. API 레벨 체크
- `CONFIRM`, `REJECT`는 **Android 11 (API 30)** 이상에서만 지원
- 하위 버전에서는 `LONG_PRESS`로 폴백 처리 필요

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
} else {
    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
}
```

### 3. 과도한 사용 자제
- 모든 인터랙션에 햅틱을 추가하면 오히려 사용자를 피로하게 함
- 의미 있는 순간에만 적절히 사용

### 4. 시스템 설정 존중
- 사용자가 시스템에서 햅틱을 비활성화한 경우 자동으로 동작하지 않음
- 별도의 확인 로직 불필요

### 5. 접근성 고려
- 햅틱 피드백은 접근성 기능을 **보완**하는 것이지 대체하는 것이 아님
- 시각적, 청각적 피드백과 함께 사용

---

## 선수 학습

이 모듈을 학습하기 전에 다음 개념을 먼저 익히면 좋습니다:

- [state/remember](../../state/remember/) - 상태 관리 기본
- [effect/launched_effect](../../effect/launched_effect/) - LaunchedEffect 사용법
- [interaction/gesture](../gesture/) - 제스처 처리 기본

---

## 연습 문제

### 연습 1: 좋아요 버튼 햅틱 (쉬움)
`LocalHapticFeedback`을 사용하여 좋아요 버튼에 햅틱 피드백을 추가합니다.

### 연습 2: 볼륨 슬라이더 틱 햅틱 (중간)
`snapshotFlow`와 `LocalView`를 사용하여 슬라이더 10단계마다 틱 햅틱을 발생시킵니다.

### 연습 3: 드래그 앤 드롭 햅틱 (어려움)
드래그 시작, 이동, 드롭 성공/실패에 각각 다른 햅틱 타입을 적용합니다.

---

## 다음 학습

- [interaction/drag_and_drop](../drag_and_drop/) - 드래그 앤 드롭 심화
- [testing/semantics_accessibility](../../testing/semantics_accessibility/) - 접근성 테스트

---

## 참고 자료

- [Android Developers: Haptic Feedback](https://developer.android.com/develop/ui/views/haptics/haptic-feedback)
- [Compose UI: HapticFeedbackType](https://developer.android.com/reference/kotlin/androidx/compose/ui/hapticfeedback/HapticFeedbackType)
- [HapticFeedbackConstants](https://developer.android.com/reference/android/view/HapticFeedbackConstants)
