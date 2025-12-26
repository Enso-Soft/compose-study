# ConstraintLayout 학습

## 개념

ConstraintLayout은 **제약 조건(Constraints)**을 사용하여 UI 요소들을 상대적으로 배치하는 레이아웃입니다.
각 요소가 다른 요소나 부모에 대해 어떤 관계를 가지는지 선언적으로 정의할 수 있습니다.

```kotlin
ConstraintLayout {
    val (image, title, button) = createRefs()

    Image(
        modifier = Modifier.constrainAs(image) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }
    )

    Text(
        modifier = Modifier.constrainAs(title) {
            top.linkTo(image.top)
            start.linkTo(image.end, margin = 16.dp)
        }
    )
}
```

---

## 핵심 특징

### 1. DSL 기반 제약 조건

```kotlin
// 참조 생성
val (button, text) = createRefs()

// 제약 조건 적용
Modifier.constrainAs(button) {
    top.linkTo(parent.top, margin = 16.dp)
    start.linkTo(parent.start, margin = 16.dp)
    end.linkTo(parent.end, margin = 16.dp)
}
```

### 2. Guideline (가이드라인)

화면의 특정 비율 또는 dp 위치에 보이지 않는 선을 만들어 기준점으로 활용합니다.

```kotlin
val startGuideline = createGuidelineFromStart(0.3f)  // 30% 지점
val topGuideline = createGuidelineFromTop(100.dp)    // 상단에서 100dp

Text(
    modifier = Modifier.constrainAs(text) {
        start.linkTo(startGuideline)
        top.linkTo(topGuideline)
    }
)
```

### 3. Barrier (배리어)

여러 요소의 경계를 기준으로 동적 가이드라인을 생성합니다.

```kotlin
val endBarrier = createEndBarrier(label1, label2, label3)

Text(
    text = value,
    modifier = Modifier.constrainAs(valueRef) {
        start.linkTo(endBarrier, margin = 8.dp)  // 가장 긴 레이블 끝에 정렬
    }
)
```

### 4. Chain (체인)

여러 요소를 체인으로 연결하여 균등 배치합니다.

```kotlin
createHorizontalChain(btn1, btn2, btn3, chainStyle = ChainStyle.SpreadInside)
```

| ChainStyle | 설명 |
|------------|------|
| `Spread` | 균등 분배 (기본값) |
| `SpreadInside` | 첫/끝 요소는 가장자리에, 나머지 균등 분배 |
| `Packed` | 중앙에 모아서 배치 |

---

## 문제 상황: Row/Column 중첩으로 복잡해진 레이아웃

### 잘못된 코드 예시

프로필 카드를 Row와 Column만으로 구현하면:

```kotlin
@Composable
fun ProfileCard() {
    Row {
        Image(avatar)
        Column {
            Row {
                Text(name)
                Spacer(Modifier.weight(1f))
                Text(time)
            }
            Text(description)
            Row {
                Button("Like")
                Spacer(Modifier.weight(1f))
                Button("Share")
            }
        }
    }
}
```

### 발생하는 문제점

1. **3단계 이상 중첩**으로 코드 구조 파악이 어려움
2. 요소 간 **상대적 정렬**이 복잡해짐
3. 레이블 길이에 따른 **동적 정렬**이 어려움
4. Spacer와 weight를 과도하게 사용해야 함

---

## 해결책: ConstraintLayout 사용

### 올바른 코드

```kotlin
@Composable
fun ProfileCard() {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (avatar, name, time, description, likeBtn, shareBtn) = createRefs()

        Image(
            modifier = Modifier.constrainAs(avatar) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
            }
        )

        Text(
            text = userName,
            modifier = Modifier.constrainAs(name) {
                top.linkTo(avatar.top)
                start.linkTo(avatar.end, 12.dp)
            }
        )

        Text(
            text = timeText,
            modifier = Modifier.constrainAs(time) {
                top.linkTo(avatar.top)
                end.linkTo(parent.end, 16.dp)
            }
        )
        // ... 나머지 요소들
    }
}
```

### 해결되는 이유

1. **플랫한 구조**: 모든 요소가 ConstraintLayout의 직접 자식
2. **명확한 관계**: 각 요소가 어디에 배치되는지 한눈에 파악
3. **유연한 정렬**: Barrier로 동적 정렬 가능
4. **가독성 향상**: 제약 조건이 의도를 명확히 표현

---

## 사용 시나리오

### 1. 복잡한 폼 레이아웃
레이블과 입력 필드가 정렬되어야 할 때 Barrier 활용

### 2. 프로필/카드 UI
이미지, 텍스트, 버튼이 상대적으로 배치될 때

### 3. 반응형 레이아웃
Guideline 비율로 다양한 화면 크기 대응

### 4. 균등 배치 UI
Chain으로 버튼이나 아이콘 균등 배치

---

## Row/Column vs ConstraintLayout 선택 기준

| 상황 | 권장 레이아웃 |
|------|-------------|
| 단순한 수직/수평 배치 | Row, Column |
| 1-2단계 중첩 | Row, Column |
| 3단계 이상 중첩 | ConstraintLayout 고려 |
| 요소 간 상대적 배치 필요 | ConstraintLayout |
| 동적 경계 기반 정렬 | ConstraintLayout (Barrier) |
| 균등 분배 | ConstraintLayout (Chain) 또는 Row + weight |

---

## 주의사항

1. **성능**: Compose에서는 중첩 레이아웃이 성능 문제가 아님 (View와 다름)
2. **RTL 지원**: left/right 대신 start/end 사용
3. **의존성 추가 필요**: `implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")`
4. **단순한 레이아웃에는 과도함**: Row/Column으로 충분하면 굳이 사용하지 않음

---

## 연습 문제

1. **기본 제약 조건**: 로그인 폼 구현 (createRefs, constrainAs)
2. **Barrier 활용**: 설정 화면 레이블/값 정렬
3. **Chain 활용**: 하단 네비게이션 균등 배치

---

## 다음 학습

- **animation_advanced**: 고급 애니메이션 (updateTransition, Animatable)
