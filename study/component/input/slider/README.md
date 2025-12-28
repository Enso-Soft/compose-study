# Slider 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Slider는 **연속적인 값을 선택할 수 있는 UI 컴포넌트**입니다.
볼륨 조절, 밝기 조절처럼 범위 내에서 원하는 값을 드래그로 선택할 때 사용합니다.

> 비유: Slider는 **오디오 믹서의 페이더**와 같습니다. 손잡이(thumb)를 위아래로 움직여 음량을 조절하듯이, Slider의 thumb을 좌우로 드래그하여 값을 조절합니다.

## 핵심 특징

1. **연속적 값 선택**: 드래그로 부드럽게 값 조절
2. **불연속 값 선택**: `steps` 파라미터로 특정 단계만 선택 가능
3. **범위 선택**: `RangeSlider`로 최소/최대 두 값을 동시에 선택
4. **완전한 커스터마이징**: 색상, 활성화 상태 등 모든 것을 조절 가능

## 사전 지식

- Compose 기본 상태 관리 (`remember`, `mutableStateOf`)
- 기본 레이아웃 (`Column`, `Row`)

---

## 문제 상황: Slider 없이 볼륨 조절 UI 구현하기

### 시나리오

음악 앱을 만들고 있습니다. 볼륨을 조절하는 슬라이더가 필요해서 직접 구현하려고 합니다.

### 직접 구현하면 필요한 것들

```kotlin
// 이렇게 복잡한 코드가 필요합니다!
Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .onSizeChanged { boxWidth = it.width }
        .pointerInput(Unit) {
            detectDragGestures { change, _ ->
                // 1. 드래그 위치 계산
                val newValue = (change.position.x / boxWidth).coerceIn(0f, 1f)
                // 2. 값 범위 제한
                onVolumeChange(newValue)
            }
        }
) {
    // 3. 트랙 배경 그리기
    Box(Modifier.fillMaxWidth().height(4.dp).background(Color.Gray))
    // 4. 활성 트랙 그리기
    Box(Modifier.fillMaxWidth(volume).height(4.dp).background(Color.Blue))
    // 5. Thumb 그리기
    Box(
        Modifier
            .offset(x = (volume * boxWidth / density).dp)
            .size(24.dp)
            .clip(CircleShape)
            .background(Color.Blue)
    )
}
```

### 발생하는 문제점

1. **코드가 30줄 이상**: 단순한 슬라이더 하나에 많은 코드 필요
2. **에지 케이스 처리 누락**: 터치 영역, 빠른 드래그 등
3. **접근성 미지원**: 시각 장애인을 위한 TalkBack 지원 없음
4. **애니메이션 없음**: 자연스러운 움직임 구현 어려움
5. **Material Design 미준수**: 디자인 가이드라인 직접 구현 필요

---

## 해결책: Slider 사용

### 기본 사용법

```kotlin
var volume by remember { mutableFloatStateOf(0.5f) }

Slider(
    value = volume,
    onValueChange = { volume = it }
)
Text("볼륨: ${(volume * 100).toInt()}%")
```

**단 3줄로 완벽한 슬라이더 완성!**

### 해결되는 이유

- Material3 Slider는 **모든 것을 내장**하고 있습니다:
  - 드래그 제스처 처리
  - 트랙과 Thumb 그리기
  - 접근성 지원 (TalkBack)
  - 부드러운 애니메이션
  - Material Design 3 스타일

---

## Slider 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `value` | Float | 현재 선택된 값 |
| `onValueChange` | (Float) -> Unit | 값이 변경될 때 호출 |
| `valueRange` | ClosedFloatingPointRange | 값의 범위 (기본: 0f..1f) |
| `steps` | Int | 중간 단계 수 (0=연속, 양수=불연속) |
| `enabled` | Boolean | 활성화 상태 (기본: true) |
| `colors` | SliderColors | 색상 커스터마이징 |
| `onValueChangeFinished` | (() -> Unit)? | 드래그 완료 시 호출 |

---

## 사용 시나리오

### 1. valueRange로 범위 지정

```kotlin
var brightness by remember { mutableFloatStateOf(50f) }

Slider(
    value = brightness,
    onValueChange = { brightness = it },
    valueRange = 0f..100f  // 0~100 범위
)
Text("밝기: ${brightness.toInt()}%")
```

### 2. steps로 단계별 선택

```kotlin
var rating by remember { mutableFloatStateOf(3f) }

Slider(
    value = rating,
    onValueChange = { rating = it },
    valueRange = 1f..5f,
    steps = 3  // 중간에 3개 단계 = 총 5개 선택점 (1, 2, 3, 4, 5)
)
Text("평점: ${rating.toInt()}점")
```

> **steps 이해하기**: steps는 시작과 끝 사이의 "중간 단계" 수입니다.
> - steps = 0: 연속적인 값 (무한대 선택)
> - steps = 3: 1, 2, 3, 4, 5 (5개 선택점)
> - steps = 4: 1, 2, 3, 4, 5, 6 (6개 선택점)

### 3. RangeSlider로 범위 선택

```kotlin
var priceRange by remember { mutableStateOf(10f..50f) }

RangeSlider(
    value = priceRange,
    onValueChange = { priceRange = it },
    valueRange = 0f..100f
)
Text("가격: ${priceRange.start.toInt()}만원 ~ ${priceRange.endInclusive.toInt()}만원")
```

### 4. 색상 커스터마이징

```kotlin
Slider(
    value = value,
    onValueChange = { value = it },
    colors = SliderDefaults.colors(
        thumbColor = Color.Red,                    // Thumb 색상
        activeTrackColor = Color.Red.copy(alpha = 0.7f),  // 활성 트랙
        inactiveTrackColor = Color.Gray.copy(alpha = 0.3f) // 비활성 트랙
    )
)
```

### 5. enabled/disabled 상태

```kotlin
var isEnabled by remember { mutableStateOf(true) }

Slider(
    value = value,
    onValueChange = { value = it },
    enabled = isEnabled  // false면 회색으로 비활성화
)
```

### 6. onValueChangeFinished로 드래그 완료 감지

```kotlin
Slider(
    value = volume,
    onValueChange = { volume = it },
    onValueChangeFinished = {
        // 드래그가 끝났을 때만 서버에 저장
        viewModel.saveVolume(volume)
    }
)
```

---

## 주의사항

- **remember 필수**: Slider의 value는 반드시 `remember`로 상태를 유지해야 합니다
- **steps 계산**: steps = n이면 선택 가능한 점 = n + 2 (시작점 + 끝점 + 중간점)
- **RangeSlider value 타입**: `ClosedFloatingPointRange<Float>` 사용 (예: `0f..100f`)
- **onValueChangeFinished**: 서버 저장 등 비용이 큰 작업은 이 콜백에서 처리

---

## 연습 문제

### 연습 1: 온도 조절기 (쉬움)
에어컨 온도를 16도~30도 범위에서 조절하는 Slider를 만드세요.

### 연습 2: 별점 선택기 (중간)
1~5점을 선택하는 Slider를 만들고, 선택된 점수만큼 별 아이콘을 표시하세요.

### 연습 3: 가격 범위 필터 (어려움)
쇼핑앱의 가격 필터를 만드세요. RangeSlider로 0~100만원 범위를 10만원 단위로 선택합니다.

---

## 다음 학습

- [ProgressIndicator](../../../component/display/progress_indicator/README.md) - 진행 상태 표시
- [Switch](../../../component/selection/switch_component/README.md) - 켜기/끄기 토글
