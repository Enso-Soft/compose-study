# Canvas & Drawing 학습

## 개념

**Canvas**는 Jetpack Compose에서 커스텀 그래픽을 그리기 위한 핵심 Composable입니다. `DrawScope` 내에서 선, 원, 사각형, 호, 경로 등을 자유롭게 그릴 수 있으며, 기존 View 시스템의 `Canvas`와 유사하지만 Compose의 선언적 패러다임에 맞게 설계되었습니다.

Canvas는 `Modifier.drawBehind`의 편리한 래퍼로, UI 레이아웃에 쉽게 통합할 수 있습니다.

## 핵심 특징

1. **Canvas Composable**: `Modifier.drawBehind`의 편리한 래퍼, 반드시 크기 지정 필요
2. **기본 도형 함수**: `drawLine`, `drawCircle`, `drawRect`, `drawArc`, `drawOval` 등
3. **Path**: `moveTo`, `lineTo`, `cubicTo` 등으로 커스텀 복잡한 도형 생성
4. **Brush**: 단색(`Color`) 또는 그라디언트(linear, radial, sweep)
5. **Style**: `Fill`(채움) vs `Stroke`(테두리)
6. **터치 통합**: `Modifier.pointerInput`으로 인터랙티브 드로잉

## 기본 도형 함수들

### drawLine - 선 그리기
```kotlin
drawLine(
    color = Color.Black,
    start = Offset(0f, 0f),
    end = Offset(100f, 100f),
    strokeWidth = 4f,
    cap = StrokeCap.Round  // Butt, Round, Square
)
```

### drawCircle - 원 그리기
```kotlin
drawCircle(
    color = Color.Blue,
    radius = 50f,
    center = Offset(100f, 100f),
    style = Fill  // 또는 Stroke(width = 2f)
)
```

### drawRect - 사각형 그리기
```kotlin
drawRect(
    color = Color.Green,
    topLeft = Offset(10f, 10f),
    size = Size(100f, 80f),
    style = Stroke(width = 3f)
)
```

### drawArc - 호/부채꼴 그리기
```kotlin
drawArc(
    color = Color.Red,
    startAngle = -90f,      // 12시 방향에서 시작
    sweepAngle = 270f,      // 270도 그리기
    useCenter = false,      // 중심 연결 여부
    style = Stroke(width = 8f, cap = StrokeCap.Round)
)
```

## Path 사용법

복잡한 커스텀 도형을 그릴 때 `Path`를 사용합니다:

```kotlin
val path = Path().apply {
    moveTo(100f, 0f)       // 시작점으로 이동
    lineTo(200f, 100f)     // 선 그리기
    lineTo(150f, 200f)     // 다음 점으로 선
    quadraticBezierTo(     // 2차 베지어 곡선
        100f, 150f,        // 제어점
        50f, 200f          // 끝점
    )
    cubicTo(               // 3차 베지어 곡선
        0f, 150f,          // 제어점 1
        0f, 50f,           // 제어점 2
        100f, 0f           // 끝점
    )
    close()                // 경로 닫기
}

drawPath(path, Color.Purple, style = Fill)
```

## Brush & 그라디언트

### 선형 그라디언트
```kotlin
val linearBrush = Brush.linearGradient(
    colors = listOf(Color.Red, Color.Yellow, Color.Green),
    start = Offset.Zero,
    end = Offset(size.width, size.height)
)
```

### 방사형 그라디언트
```kotlin
val radialBrush = Brush.radialGradient(
    colors = listOf(Color.White, Color.Blue),
    center = Offset(size.width / 2, size.height / 2),
    radius = size.minDimension / 2
)
```

### 스윕 그라디언트 (원형)
```kotlin
val sweepBrush = Brush.sweepGradient(
    colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Red),
    center = Offset(size.width / 2, size.height / 2)
)
```

## 터치 이벤트와 결합

Canvas와 `Modifier.pointerInput`을 결합하여 인터랙티브 드로잉 앱을 만들 수 있습니다:

```kotlin
var paths by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }

Canvas(
    modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    currentPath = listOf(offset)
                },
                onDrag = { change, _ ->
                    currentPath = currentPath + change.position
                },
                onDragEnd = {
                    paths = paths + listOf(currentPath)
                    currentPath = emptyList()
                }
            )
        }
) {
    // 저장된 경로들 그리기
    paths.forEach { points ->
        if (points.size > 1) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(path, Color.Black, style = Stroke(5f))
        }
    }

    // 현재 그리는 중인 경로
    if (currentPath.size > 1) {
        val path = Path().apply {
            moveTo(currentPath.first().x, currentPath.first().y)
            currentPath.drop(1).forEach { lineTo(it.x, it.y) }
        }
        drawPath(path, Color.Blue, style = Stroke(5f))
    }
}
```

## 문제 상황: Canvas 없이 그래픽 구현 시도

### 잘못된 접근법
```kotlin
// 각 터치 포인트마다 Box를 생성하려는 시도
points.forEach { point ->
    Box(
        modifier = Modifier
            .offset { IntOffset(point.x.toInt(), point.y.toInt()) }
            .size(5.dp)
            .background(Color.Black, CircleShape)
    )
}
```

### 발생하는 문제점
1. **성능 저하**: 수백 개의 Composable 생성으로 재구성 폭발
2. **선 연결 불가**: 점만 표시되고 부드러운 선이 그려지지 않음
3. **스타일링 제한**: 그라디언트, 스타일 변경 등 세밀한 제어 불가
4. **메모리 낭비**: 각 점마다 별도의 Composable 인스턴스 생성

## 해결책: Canvas 사용

### 올바른 접근법
- **단일 Canvas**에서 모든 그리기 처리
- **Path 객체**로 점들을 연결한 부드러운 선
- **detectDragGestures**로 터치 시작, 이동, 종료 모두 처리
- **상태 분리**: 완료된 paths와 진행 중인 currentPath 분리

### 장점
1. **효율성**: 단일 Canvas 내에서 모든 드로잉 처리, 재구성 최소화
2. **유연성**: 색상, 굵기, 스타일, 그라디언트 자유롭게 적용
3. **성능**: Canvas 내부 그리기는 재구성 없이 효율적
4. **정밀 제어**: 픽셀 단위의 정확한 그래픽 제어 가능

## 사용 시나리오

1. **차트/그래프 그리기**: 막대 차트, 선 차트, 원형 차트
2. **서명 패드**: 사용자 서명 캡처
3. **커스텀 아이콘/일러스트**: 벡터 기반 동적 아이콘
4. **게임 그래픽**: 2D 게임 요소 렌더링
5. **프로그레스 인디케이터**: 원형 프로그레스 바, 커스텀 로딩
6. **데이터 시각화**: 히트맵, 분산형 차트

## 주의사항

1. **크기 지정 필수**: Canvas는 반드시 `Modifier.size()` 또는 `fillMaxSize()` 등으로 크기 지정
2. **좌표계**: 좌상단이 원점 (0, 0), 오른쪽이 +X, 아래쪽이 +Y
3. **픽셀 밀도**: 다양한 화면 밀도를 고려하여 dp 단위 사용 권장
4. **성능 최적화**: `drawWithCache` 사용으로 Brush, Path 등 캐싱 가능
5. **각도 단위**: `drawArc`의 각도는 도(degree) 단위, 3시 방향이 0도

## 연습 문제

1. **태양 아이콘 그리기 (기초)**: `drawCircle` + `drawLine`으로 광선 표현
2. **별 모양 그리기 (중급)**: `Path`의 `moveTo`, `lineTo`로 오각별 구현
3. **원형 프로그레스 바 (심화)**: `drawArc` + `Brush.sweepGradient`로 그라디언트 프로그레스

## 다음 학습

- `Modifier.drawBehind` / `Modifier.drawWithContent`: 컨텐츠 뒤/앞에 그리기
- 애니메이션과 Canvas 결합: `animateFloatAsState`와 함께 사용
- `drawWithCache`: 성능 최적화를 위한 캐싱
- `BlendMode`: 색상 블렌딩 효과

## 참고 자료

- [Graphics in Compose | Android Developers](https://developer.android.com/develop/ui/compose/graphics/draw/overview)
- [Brush: gradients and shaders | Android Developers](https://developer.android.com/develop/ui/compose/graphics/draw/brush)
- [Pointer input in Compose | Android Developers](https://developer.android.com/develop/ui/compose/touch-input/pointer-input)
