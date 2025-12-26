# Layout & Modifier 학습

## 개념

Compose에서 모든 UI 배치는 **Column, Row, Box** 세 가지 레이아웃의 조합입니다. **Modifier**는 이들의 크기, 패딩, 배경 등을 꾸며줍니다.

```
모든 화면 = Column + Row + Box 조합
```

---

## 핵심 레이아웃

### 1. Column - 세로 배치

```kotlin
Column {
    Text("위")
    Text("아래")
}

// ↓ 결과
// 위
// 아래
```

**파라미터:**
- `verticalArrangement`: 세로 방향 배치/간격
- `horizontalAlignment`: 가로 방향 정렬

```kotlin
Column(
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally
) { ... }
```

### 2. Row - 가로 배치

```kotlin
Row {
    Text("왼쪽")
    Text("오른쪽")
}

// → 결과: 왼쪽 오른쪽
```

**파라미터:**
- `horizontalArrangement`: 가로 방향 배치/간격
- `verticalAlignment`: 세로 방향 정렬

```kotlin
Row(
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
) { ... }
```

### 3. Box - 겹치기

```kotlin
Box {
    Image(...)     // 배경
    Text("위에")   // 오버레이
}
```

**파라미터:**
- `contentAlignment`: 전체 자식 정렬
- `Modifier.align()`: 개별 자식 정렬

```kotlin
Box(contentAlignment = Alignment.Center) {
    Image(...)
    Text(
        "우측 하단",
        modifier = Modifier.align(Alignment.BottomEnd)
    )
}
```

---

## Arrangement (배치)

**주축 방향**으로 자식들을 어떻게 배치할지 결정합니다.

| Arrangement | 설명 |
|-------------|------|
| `Start` | 시작 부분에 모음 |
| `End` | 끝 부분에 모음 |
| `Center` | 중앙에 모음 |
| `SpaceBetween` | 양 끝 고정, 사이 균등 |
| `SpaceEvenly` | 모든 간격 균등 |
| `SpaceAround` | 양 끝 반만큼, 사이 균등 |
| `spacedBy(8.dp)` | 고정 간격 |

## Alignment (정렬)

**교차축 방향**으로 자식들을 어떻게 정렬할지 결정합니다.

| Column의 horizontalAlignment | Row의 verticalAlignment |
|------------------------------|-------------------------|
| `Start` | `Top` |
| `CenterHorizontally` | `CenterVertically` |
| `End` | `Bottom` |

---

## Modifier

Modifier는 Composable을 **꾸미고 동작을 추가**합니다.

### 주요 Modifier

```kotlin
Modifier
    .size(100.dp)           // 크기 고정
    .fillMaxWidth()         // 가로 꽉 채움
    .fillMaxHeight()        // 세로 꽉 채움
    .fillMaxSize()          // 전체 꽉 채움
    .padding(16.dp)         // 안쪽 여백
    .background(Color.Red)  // 배경색
    .border(1.dp, Color.Black) // 테두리
    .clip(RoundedCornerShape(8.dp)) // 모서리 둥글게
    .clickable { }          // 클릭 가능
```

### ⚠️ Modifier 순서가 중요!

```kotlin
// 배경 → 패딩: 패딩 영역에 배경 없음
Modifier.background(Color.Red).padding(16.dp)

// 패딩 → 배경: 패딩 포함 전체에 배경
Modifier.padding(16.dp).background(Color.Red)
```

**규칙:** Modifier는 **바깥쪽부터 안쪽으로** 적용됩니다.

---

## 문제 상황: 레이아웃이 예상대로 안 됨

### 잘못된 코드 예시

```kotlin
// 문제 1: fillMaxWidth 없이 Card
Card {
    Text("좁은 카드")
}
// 결과: 텍스트 크기만큼만 카드가 생성됨

// 문제 2: Arrangement 없이 Column
Column {
    Text("A")
    Text("B")
}
// 결과: 모두 위에 붙어서 배치됨

// 문제 3: Modifier 순서 실수
Modifier.padding(16.dp).clickable { }
// 결과: 패딩 영역은 클릭 안 됨
```

---

## 해결책: 올바른 Layout & Modifier 사용

```kotlin
// 해결 1: fillMaxWidth로 전체 너비
Card(modifier = Modifier.fillMaxWidth()) {
    Text("넓은 카드")
}

// 해결 2: Arrangement로 간격 조절
Column(
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text("A")
    Text("B")
}

// 해결 3: clickable을 먼저 적용
Modifier.clickable { }.padding(16.dp)
// 결과: 패딩 포함 전체가 클릭 영역
```

---

## 사용 시나리오

### 프로필 카드 레이아웃

```kotlin
Card(modifier = Modifier.fillMaxWidth()) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필 이미지
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.Gray, CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 정보
        Column {
            Text("홍길동", fontWeight = FontWeight.Bold)
            Text("hong@email.com")
        }
    }
}
```

### 뱃지가 있는 아이콘

```kotlin
Box {
    Icon(Icons.Default.Notifications, null, modifier = Modifier.size(32.dp))
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(Color.Red, CircleShape)
            .align(Alignment.TopEnd)
    ) {
        Text("3", color = Color.White, fontSize = 10.sp)
    }
}
```

---

## 주의사항

1. **Modifier 순서**
   - 바깥쪽부터 안쪽으로 적용
   - clickable은 원하는 영역에 맞게 배치

2. **Arrangement vs Alignment 구분**
   - Arrangement: 주축 (Column=세로, Row=가로)
   - Alignment: 교차축 (Column=가로, Row=세로)

3. **fillMaxXXX 사용**
   - 레이아웃이 작게 나오면 fillMaxWidth/Height 확인

4. **weight 사용**
   - Row/Column 안에서 비율로 공간 분배
   - `Modifier.weight(1f)`

---

## 연습 문제

### 연습 1: Column과 Row 조합
프로필 카드 레이아웃을 구현해보세요.

### 연습 2: Box와 오버레이
뱃지가 있는 알림 아이콘을 구현해보세요.

### 연습 3: Modifier 활용
Modifier 순서에 따른 차이를 확인해보세요.

---

## 다음 학습

Level 1 완료! 다음 단계:
- **Level 2: 상태 관리** - remember, rememberSaveable, State Hoisting
- **Level 3: Side Effects** - LaunchedEffect, DisposableEffect 등
