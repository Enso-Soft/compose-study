# Card 컴포넌트 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `button` | 버튼 컴포넌트 기본 사용법 (연습 3에서 사용) | [📚 학습하기](../../../component/action/button/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

Card는 **관련된 정보를 하나의 컨테이너에 담는** Material Design 컴포넌트입니다.

실제 명함이나 카드 게임의 카드처럼, 한 장에 하나의 일관된 정보를 담습니다:
- 쇼핑 앱의 **상품 카드**
- 뉴스 앱의 **기사 카드**
- SNS 앱의 **프로필 카드**

```kotlin
// 가장 기본적인 Card
Card {
    Text(
        text = "안녕하세요!",
        modifier = Modifier.padding(16.dp)
    )
}
```

---

## 사전 지식

이 모듈을 학습하기 전에 다음 내용을 알아야 합니다:
- `Modifier` 기본 사용법 (padding, fillMaxWidth)
- `Row`, `Column` 레이아웃
- `Button` 기본 사용법 (연습 3에서 사용)

---

## 핵심 특징

1. **컨테이너 역할**: 내용물을 담는 그릇처럼 동작
2. **자동 정렬**: Card 안의 콘텐츠는 자동으로 위에서 아래로 정렬 (Column처럼)
3. **3가지 유형**: Filled, Elevated, Outlined

---

## 3가지 Card 유형

Material 3에서는 3가지 Card 유형을 제공합니다:

### 1. Card (Filled) - 배경색이 채워진 카드

```kotlin
Card(
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = "Filled Card",
        modifier = Modifier.padding(16.dp)
    )
}
```

**사용 시나리오**: 일반적인 콘텐츠 컨테이너

### 2. ElevatedCard - 그림자가 있는 카드

```kotlin
ElevatedCard(
    elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp
    ),
    modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = "Elevated Card",
        modifier = Modifier.padding(16.dp)
    )
}
```

**사용 시나리오**: 배경에서 떠있는 느낌이 필요할 때, 강조가 필요할 때

### 3. OutlinedCard - 테두리가 있는 카드

```kotlin
OutlinedCard(
    border = BorderStroke(1.dp, Color.Black),
    modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = "Outlined Card",
        modifier = Modifier.padding(16.dp)
    )
}
```

**사용 시나리오**: 심플한 구분이 필요할 때, 선택 가능한 항목

---

## 비교 표

| 유형 | 특징 | 시각적 효과 | 사용 시나리오 |
|------|------|------------|--------------|
| Card (Filled) | 배경색 있음 | 평평함 | 일반 컨테이너 |
| ElevatedCard | 그림자 효과 | 떠있는 느낌 | 강조 필요 시 |
| OutlinedCard | 테두리만 있음 | 심플함 | 선택 항목, 구분 |

---

## 클릭 가능한 Card

Card에 `onClick` 파라미터를 추가하면 클릭할 수 있습니다:

```kotlin
var clickCount by remember { mutableIntStateOf(0) }

Card(
    onClick = { clickCount++ },
    modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = "클릭 횟수: $clickCount",
        modifier = Modifier.padding(16.dp)
    )
}
```

클릭하면 ripple 효과가 자동으로 나타납니다.

---

## Card 내부 레이아웃 패턴

### 가로형 (이미지 + 텍스트)

```kotlin
Card {
    Row(modifier = Modifier.padding(16.dp)) {
        // 이미지 영역
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.LightGray)
        )

        Spacer(Modifier.width(16.dp))

        // 텍스트 영역
        Column {
            Text("상품명", style = MaterialTheme.typography.titleMedium)
            Text("설명", style = MaterialTheme.typography.bodyMedium)
            Text("10,000원", fontWeight = FontWeight.Bold)
        }
    }
}
```

### 세로형 (이미지 위에 텍스트)

```kotlin
Card {
    Column {
        // 이미지 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.LightGray)
        )

        // 텍스트 영역
        Column(modifier = Modifier.padding(16.dp)) {
            Text("제목")
            Text("설명")
        }
    }
}
```

### 버튼 포함

```kotlin
Card {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("상품명")
        Text("가격")

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            OutlinedButton(onClick = { }) {
                Text("찜하기")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { }) {
                Text("구매하기")
            }
        }
    }
}
```

---

## CardDefaults 커스터마이징

### 색상 변경

```kotlin
Card(
    colors = CardDefaults.cardColors(
        containerColor = Color(0xFFFFF3E0),  // 배경색
        contentColor = Color(0xFFE65100)     // 텍스트/아이콘 색상
    )
) {
    Text("커스텀 색상")
}
```

### 그림자(Elevation) 변경

```kotlin
ElevatedCard(
    elevation = CardDefaults.cardElevation(
        defaultElevation = 8.dp,     // 기본 그림자
        pressedElevation = 12.dp,    // 누를 때 그림자
        hoveredElevation = 10.dp     // 호버 시 그림자
    )
) {
    Text("높은 그림자")
}
```

### 테두리 변경

```kotlin
OutlinedCard(
    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
) {
    Text("두꺼운 테두리")
}
```

### 모양 변경

```kotlin
Card(
    shape = RoundedCornerShape(16.dp)  // 둥근 모서리
) {
    Text("둥근 카드")
}

Card(
    shape = RoundedCornerShape(0.dp)   // 각진 모서리
) {
    Text("각진 카드")
}
```

---

## 베스트 프랙티스

1. **일관된 padding 사용**: Card 내부에 16.dp padding을 권장
2. **적절한 유형 선택**: 강조 정도에 따라 유형 선택
3. **클릭 영역 명확히**: 클릭 가능한 Card는 전체 영역이 클릭 가능하도록
4. **접근성 고려**: 중요한 Card에는 contentDescription 제공

---

## 안티패턴

### 피해야 할 것 1: 너무 많은 중첩

```kotlin
// 피하세요
Card {
    Card {
        Card {
            Text("너무 많은 중첩")
        }
    }
}
```

### 피해야 할 것 2: 과도한 그림자

```kotlin
// 피하세요
ElevatedCard(
    elevation = CardDefaults.cardElevation(
        defaultElevation = 24.dp  // 너무 높은 elevation
    )
)
```

### 피해야 할 것 3: 일관성 없는 스타일

같은 화면의 Card들은 일관된 스타일을 유지하세요.

---

## 연습 문제

### 연습 1: 기본 Card 3가지 유형 만들기 - 쉬움

Material 3의 3가지 Card 유형(Filled, Elevated, Outlined)을 각각 만들어보세요.

### 연습 2: 클릭하면 확장되는 Card - 중간

FAQ처럼 클릭하면 상세 내용이 나타나는 Card를 구현해보세요.
- 기본: 제목만 표시
- 확장: 제목 + 상세 내용
- 화살표 아이콘 회전 애니메이션

### 연습 3: 상품 정보 카드 만들기 - 어려움

쇼핑 앱의 상품 카드를 완성해보세요:
- 상단: 상품 이미지
- 중단: 상품명, 가격
- 하단: 찜하기, 구매하기 버튼

---

## 다음 학습

- **Scaffold**: 전체 화면 레이아웃 구성
- **Surface**: Card의 기반이 되는 더 유연한 컨테이너
- **LazyColumn + Card**: 스크롤 가능한 Card 리스트
- **SwipeToDismiss + Card**: 밀어서 삭제 기능

---

## 참고 자료

- [Android Developers: Card](https://developer.android.com/develop/ui/compose/components/card)
- [Material Design 3: Cards](https://m3.material.io/components/cards/overview)
