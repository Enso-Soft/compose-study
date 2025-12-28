# Layout & Modifier 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `composable_function` | @Composable 함수와 기본 사용법 | [📚 학습하기](../../basics/composable_function/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

Compose에서 모든 UI는 **Column, Row, Box** 세 가지 기본 레이아웃의 조합으로 구성됩니다.
**Modifier**는 이들의 크기, 여백, 배경, 클릭 동작 등을 정의합니다.

> **핵심 공식**: 모든 화면 = Column + Row + Box 조합 + Modifier

---

## Part 1: 기본 레이아웃

### 언제 어떤 Layout을 선택할까?

레이아웃을 선택할 때 가장 먼저 "요소들을 어떻게 배치할 것인가?"를 생각합니다.

```
레이아웃 선택
    |
    +-- 요소들을 세로로 나열? -------> Column
    |   (리스트, 폼, 카드 내용)
    |
    +-- 요소들을 가로로 나열? -------> Row
    |   (버튼 그룹, 탭바, 프로필 행)
    |
    +-- 요소들을 겹쳐서 표시? -------> Box
        (오버레이, 뱃지, 로딩 인디케이터)
```

| 기준 | Column | Row | Box |
|------|--------|-----|-----|
| **배치 방향** | 세로 (위에서 아래) | 가로 (왼쪽에서 오른쪽) | 겹침 (z-index) |
| **주축** | 세로 (Vertical) | 가로 (Horizontal) | 없음 |
| **Arrangement** | `verticalArrangement` | `horizontalArrangement` | 해당 없음 |
| **Alignment** | `horizontalAlignment` | `verticalAlignment` | `contentAlignment` |
| **개별 정렬** | - | - | `Modifier.align()` |
| **대표 사용처** | 리스트, 폼 | 버튼 그룹, 헤더 | 오버레이, 뱃지 |

---

### 1. Column - 세로 배치

자식 요소들을 **위에서 아래로** 배치합니다.

```kotlin
Column {
    Text("첫 번째")
    Text("두 번째")
    Text("세 번째")
}

// 결과:
// 첫 번째
// 두 번째
// 세 번째
```

**주요 파라미터:**
- `verticalArrangement`: 세로 방향 배치 방식 (주축)
- `horizontalAlignment`: 가로 방향 정렬 (교차축)

```kotlin
Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text("위")
    Text("중간")
    Text("아래")
}
```

---

### 2. Row - 가로 배치

자식 요소들을 **왼쪽에서 오른쪽으로** 배치합니다.

```kotlin
Row {
    Text("왼쪽")
    Text("중앙")
    Text("오른쪽")
}

// 결과: 왼쪽 중앙 오른쪽
```

**주요 파라미터:**
- `horizontalArrangement`: 가로 방향 배치 방식 (주축)
- `verticalAlignment`: 세로 방향 정렬 (교차축)

```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(Icons.Default.Home, null)
    Icon(Icons.Default.Search, null)
    Icon(Icons.Default.Settings, null)
}
```

---

### 3. Box - 겹치기 (Stack)

자식 요소들을 **같은 위치에 겹쳐서** 배치합니다. 나중에 선언된 요소가 위에 표시됩니다.

```kotlin
Box {
    Image(...)           // 배경 (아래)
    Text("오버레이")      // 전면 (위)
}
```

**주요 파라미터:**
- `contentAlignment`: 모든 자식의 기본 정렬 위치
- `Modifier.align()`: 개별 자식의 정렬 위치 (Box 내부에서만 사용)

```kotlin
Box(
    modifier = Modifier.size(200.dp),
    contentAlignment = Alignment.Center
) {
    // 중앙에 배치
    CircularProgressIndicator()

    // 개별 정렬로 우측 하단에 배치
    Text(
        "로딩 중...",
        modifier = Modifier.align(Alignment.BottomEnd)
    )
}
```

---

## Part 2: Arrangement & Alignment

### Arrangement (배치) - 주축 방향

**주축 방향**으로 자식들을 어떻게 배치할지 결정합니다.

| Arrangement | 설명 | 시각화 |
|-------------|------|--------|
| `Start` | 시작 부분에 모음 | `[A][B][C]........` |
| `End` | 끝 부분에 모음 | `........[A][B][C]` |
| `Center` | 중앙에 모음 | `....[A][B][C]....` |
| `SpaceBetween` | 양 끝 고정, 사이 균등 | `[A]....[B]....[C]` |
| `SpaceEvenly` | 모든 간격 균등 | `..[A]..[B]..[C]..` |
| `SpaceAround` | 양 끝 반, 사이 균등 | `.[A]...[B]...[C].` |
| `spacedBy(8.dp)` | 고정 간격 | `[A]-8dp-[B]-8dp-[C]` |

```kotlin
// 예시: Row에서 Arrangement 사용
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text("왼쪽")
    Text("오른쪽")  // 양 끝에 배치됨
}
```

---

### Alignment (정렬) - 교차축 방향

**교차축 방향**으로 자식들을 어떻게 정렬할지 결정합니다.

| Column (세로 레이아웃) | Row (가로 레이아웃) |
|----------------------|-------------------|
| `horizontalAlignment` | `verticalAlignment` |
| `Alignment.Start` (왼쪽) | `Alignment.Top` (위) |
| `Alignment.CenterHorizontally` (중앙) | `Alignment.CenterVertically` (중앙) |
| `Alignment.End` (오른쪽) | `Alignment.Bottom` (아래) |

```kotlin
// 예시: Column에서 가로 중앙 정렬
Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text("중앙 정렬된 텍스트")
    Button(onClick = {}) { Text("중앙 버튼") }
}
```

> **기억법**: Arrangement는 **주축**, Alignment는 **교차축**을 제어합니다.
> - Column의 주축 = 세로 → `verticalArrangement`
> - Row의 주축 = 가로 → `horizontalArrangement`

---

## Part 3: Modifier

### Modifier란?

Modifier는 Composable의 **외형과 동작을 정의**하는 체인입니다.
크기, 여백, 배경, 클릭 등 거의 모든 UI 속성을 Modifier로 설정합니다.

### 핵심 Modifier 목록

```kotlin
Modifier
    // 크기 관련
    .size(100.dp)           // 가로×세로 고정
    .width(100.dp)          // 가로만 고정
    .height(50.dp)          // 세로만 고정
    .fillMaxWidth()         // 가로 꽉 채움
    .fillMaxHeight()        // 세로 꽉 채움
    .fillMaxSize()          // 전체 꽉 채움
    .fillMaxWidth(0.5f)     // 가로 50% 채움

    // 여백 관련
    .padding(16.dp)         // 안쪽 여백 (전체)
    .padding(horizontal = 16.dp, vertical = 8.dp)
    .padding(start = 8.dp, top = 16.dp)

    // 배경/테두리
    .background(Color.Red)
    .background(Color.Blue, RoundedCornerShape(8.dp))
    .border(1.dp, Color.Black)
    .border(2.dp, Color.Gray, CircleShape)

    // 모양
    .clip(RoundedCornerShape(8.dp))  // 모서리 둥글게
    .clip(CircleShape)               // 원형

    // 동작
    .clickable { }          // 클릭 가능
    .scrollable(...)        // 스크롤 가능
```

---

### Modifier 순서의 중요성

Modifier는 **체인 순서대로 바깥쪽부터 안쪽으로** 적용됩니다.
순서에 따라 결과가 완전히 달라집니다.

```kotlin
// 순서 A: background → padding
// 배경이 먼저 적용되고, 그 안에 padding이 생김
// → padding 영역에는 배경이 없음
Box(
    modifier = Modifier
        .background(Color.Red)
        .padding(16.dp)
) {
    Text("Hello")
}

// 순서 B: padding → background
// padding이 먼저 적용되고, 그 안에 background가 생김
// → padding을 포함한 전체에 배경이 있음
Box(
    modifier = Modifier
        .padding(16.dp)
        .background(Color.Red)
) {
    Text("Hello")
}
```

**시각화:**

```
순서 A: background → padding
┌──────────────────┐
│    (배경 없음)    │ ← padding 영역
│  ┌────────────┐  │
│  │  빨간 배경  │  │ ← background 적용 영역
│  │   Hello    │  │
│  └────────────┘  │
└──────────────────┘

순서 B: padding → background
┌──────────────────┐
│ 빨간 배경         │ ← background가 전체 적용
│  ┌────────────┐  │
│  │            │  │ ← padding 영역
│  │   Hello    │  │
│  └────────────┘  │
└──────────────────┘
```

**clickable과 padding 순서:**

```kotlin
// clickable → padding: 전체 영역 클릭 가능 (권장)
Modifier.clickable { }.padding(16.dp)

// padding → clickable: padding 제외한 내부만 클릭 가능
Modifier.padding(16.dp).clickable { }
```

---

### Modifier 조합 패턴

#### 패턴 1: 카드 스타일

```kotlin
Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp, vertical = 8.dp)  // 외부 여백
    .clip(RoundedCornerShape(12.dp))
    .background(MaterialTheme.colorScheme.surface)
    .padding(16.dp)  // 내부 여백
```

#### 패턴 2: 클릭 가능한 리스트 아이템

```kotlin
Modifier
    .fillMaxWidth()
    .clickable { onItemClick() }  // 클릭 먼저!
    .padding(16.dp)               // 그 다음 padding
```

#### 패턴 3: 원형 프로필 이미지

```kotlin
Modifier
    .size(60.dp)
    .clip(CircleShape)
    .background(Color.Gray)
```

---

## 사용 시나리오

### 시나리오 1: 프로필 카드

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
                .clip(CircleShape)
                .background(Color.Gray)
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

### 시나리오 2: 뱃지가 있는 아이콘

```kotlin
Box {
    Icon(
        Icons.Default.Notifications,
        contentDescription = null,
        modifier = Modifier.size(32.dp)
    )

    // 뱃지 오버레이
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(Color.Red)
            .align(Alignment.TopEnd),
        contentAlignment = Alignment.Center
    ) {
        Text("3", color = Color.White, fontSize = 10.sp)
    }
}
```

### 시나리오 3: 균등 분할 버튼 그룹

```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Button(
        onClick = {},
        modifier = Modifier.weight(1f)  // 균등 분할
    ) {
        Text("취소")
    }

    Button(
        onClick = {},
        modifier = Modifier.weight(1f)  // 균등 분할
    ) {
        Text("확인")
    }
}
```

---

## 베스트 프랙티스

### 권장 사항

1. **Modifier 파라미터 전달**
   - 재사용 가능한 Composable은 항상 `modifier` 파라미터를 받아야 합니다
   ```kotlin
   @Composable
   fun ProfileCard(
       user: User,
       modifier: Modifier = Modifier  // 기본값과 함께 선언
   ) {
       Card(modifier = modifier) { ... }
   }
   ```

2. **fillMaxWidth 습관화**
   - Card, TextField 등은 기본적으로 컨텐츠 크기만큼만 차지
   - 전체 너비가 필요하면 반드시 `fillMaxWidth()` 추가

3. **weight로 비율 분배**
   - Row/Column 안에서 공간을 비율로 분배할 때 사용
   ```kotlin
   Row {
       Text("라벨", modifier = Modifier.weight(1f))
       Text("값", modifier = Modifier.weight(2f))  // 2배 공간
   }
   ```

4. **Spacer 활용**
   - 요소 간 고정 간격이 필요할 때 사용
   ```kotlin
   Spacer(modifier = Modifier.width(16.dp))  // 가로 간격
   Spacer(modifier = Modifier.height(8.dp))  // 세로 간격
   ```

### 피해야 할 패턴

1. **고정 크기 남용**
   - `size()`보다 `fillMaxWidth()` + 적절한 `height()` 선호
   - 다양한 화면 크기 대응을 위해

2. **중첩 레이아웃 과다**
   - 불필요한 Column/Row 중첩은 성능에 영향
   - 가능하면 단순한 구조 유지

3. **Modifier 순서 무시**
   - 항상 의도한 결과가 나오는지 확인
   - `clickable`은 보통 `padding`보다 먼저

---

## 연습 문제

### 연습 1: Column과 Row 조합 (기초)
프로필 카드 레이아웃을 구현해보세요.
- Row 안에 이미지와 Column 배치
- Column 안에 이름과 이메일 배치

### 연습 2: Box와 오버레이 (중급)
뱃지가 있는 알림 아이콘을 구현해보세요.
- Box로 아이콘과 뱃지 겹치기
- `Modifier.align()`으로 뱃지 위치 조정

### 연습 3: Modifier 순서 실험 (심화)
Modifier 순서에 따른 차이를 직접 확인해보세요.
- `background` → `padding` vs `padding` → `background`
- `clickable` → `padding` vs `padding` → `clickable`

---

## 다음 학습

Layout & Modifier 기초를 마쳤습니다! 다음 단계:
- **상태 관리**: remember, rememberSaveable, State Hoisting
- **Side Effects**: LaunchedEffect, DisposableEffect
- **고급 레이아웃**: LazyColumn, LazyRow, ConstraintLayout
