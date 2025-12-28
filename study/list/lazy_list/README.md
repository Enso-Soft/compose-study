# LazyColumn/LazyRow 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**LazyColumn**과 **LazyRow**는 화면에 보이는 항목만 그려서 대량의 리스트를 효율적으로 표시하는 Compose 컴포넌트입니다.

> "Lazy"는 "게으른"이라는 뜻입니다.
> 필요할 때까지 일을 미루고, 화면에 보이는 항목만 그립니다.

### 선수 지식
- Column, Row 레이아웃 기본 사용법
- Modifier.padding(), Modifier.fillMaxWidth() 등 기본 Modifier
- @Composable 함수 작성법

## 핵심 특징

### 1. 화면에 보이는 항목만 그림 (Lazy Composition)
```
100명이 줄 서서 기다리는 상황을 상상해보세요:

Column (일반 레이아웃):
  - 창구 10개인데 100명 모두 앞에 서 있음
  - 공간 부족! 메모리 낭비!

LazyColumn (지연 레이아웃):
  - 순번표 받고 앉아 있다가 차례되면 옴
  - 효율적! 필요한 만큼만 메모리 사용!
```

### 2. RecyclerView와 동일한 원리
Android View 시스템의 RecyclerView처럼 항목을 재활용하여 성능을 최적화합니다.

### 3. DSL 기반의 간결한 항목 정의
`items()`, `item()` 함수로 직관적으로 항목을 추가합니다.

---

## 문제 상황: Column으로 많은 아이템 표시

### 시나리오
100명의 사용자 목록을 표시하는 화면을 만들어야 합니다.
Column으로 구현하면 어떤 문제가 발생할까요?

### 잘못된 코드 예시

```kotlin
@Composable
fun UserListScreen() {
    val users = (1..100).map { User(it, "사용자 $it") }

    // Column은 모든 자식을 한 번에 그립니다!
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        users.forEach { user ->
            UserCard(user)  // 100개 모두 즉시 그려짐
        }
    }
}
```

### 발생하는 문제점

1. **느린 초기 로딩**: 앱 시작 시 100개 아이템 모두 그림
2. **메모리 낭비**: 화면에 10개만 보여도 100개 모두 메모리에 유지
3. **스크롤 버벅거림**: 많은 아이템 처리로 인한 성능 저하
4. **대용량 데이터 불가**: 1000개, 10000개는 OOM(메모리 부족) 발생

---

## 해결책: LazyColumn 사용

### 올바른 코드

```kotlin
@Composable
fun UserListScreen() {
    val users = (1..100).map { User(it, "사용자 $it") }

    // LazyColumn은 화면에 보이는 항목만 그립니다!
    LazyColumn {
        items(users) { user ->
            UserCard(user)  // 필요할 때만 그려짐
        }
    }
}
```

### 해결되는 이유

1. **빠른 초기 로딩**: 화면에 보이는 10개만 그림
2. **효율적 메모리**: 보이는 항목 + 약간의 버퍼만 메모리에 유지
3. **부드러운 스크롤**: 필요한 항목만 처리하여 60fps 유지
4. **무한 스크롤 가능**: 수천 개 데이터도 문제없이 표시

---

## 기본 사용법

### 1. items() - 리스트 기반 항목 추가

가장 많이 사용하는 방식입니다.

```kotlin
val fruits = listOf("사과", "바나나", "체리", "포도")

LazyColumn {
    items(fruits) { fruit ->
        Text(text = fruit)
    }
}
```

### 2. item() - 단일 항목 추가

헤더, 푸터 등 개별 항목을 추가할 때 사용합니다.

```kotlin
LazyColumn {
    // 헤더
    item {
        Text(
            text = "과일 목록",
            style = MaterialTheme.typography.headlineMedium
        )
    }

    // 리스트
    items(fruits) { fruit ->
        Text(text = fruit)
    }

    // 푸터
    item {
        Text(text = "--- 목록 끝 ---")
    }
}
```

### 3. items(count) - 숫자 기반 항목 추가

단순히 개수만 지정할 때 사용합니다.

```kotlin
LazyColumn {
    items(50) { index ->
        Text(text = "아이템 ${index + 1}")
    }
}
```

### 4. itemsIndexed() - 인덱스와 함께 항목 추가

인덱스가 필요할 때 사용합니다.

```kotlin
LazyColumn {
    itemsIndexed(fruits) { index, fruit ->
        Text(text = "${index + 1}. $fruit")
    }
}
```

### 5. LazyRow - 수평 스크롤

LazyColumn과 동일하지만 가로 방향입니다.

```kotlin
val categories = listOf("전체", "음식", "카페", "쇼핑", "문화")

LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)
) {
    items(categories) { category ->
        FilterChip(
            onClick = { },
            label = { Text(category) },
            selected = category == "전체"
        )
    }
}
```

### 6. contentPadding, Arrangement - 레이아웃 설정

```kotlin
LazyColumn(
    // 콘텐츠 주변 여백 (LazyColumn 자체가 아닌 콘텐츠에 적용)
    contentPadding = PaddingValues(16.dp),

    // 항목 간 간격
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(users) { user ->
        UserCard(user)
    }
}
```

### 7. LazyVerticalGrid - 그리드 레이아웃

사진 갤러리, 상품 목록 등에 사용합니다.

```kotlin
LazyVerticalGrid(
    // 2열 고정
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(photos) { photo ->
        PhotoCard(photo)
    }
}
```

---

## 사용 시나리오

### 1. 채팅 앱 메시지 목록
```kotlin
LazyColumn(
    reverseLayout = true  // 최신 메시지가 아래에
) {
    items(messages) { message ->
        MessageBubble(message)
    }
}
```

### 2. 쇼핑 앱 상품 목록
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2)
) {
    items(products) { product ->
        ProductCard(product)
    }
}
```

### 3. 설정 화면
```kotlin
LazyColumn {
    item { SectionHeader("일반") }
    items(generalSettings) { SettingItem(it) }

    item { SectionHeader("알림") }
    items(notificationSettings) { SettingItem(it) }
}
```

---

## 주의사항

### 1. 스크롤이 필요 없으면 Column 사용
```kotlin
// 아이템이 5개 이하면 Column으로 충분
Column {
    items.forEach { item ->
        ItemCard(item)
    }
}
```

### 2. 같은 방향 스크롤 중첩 금지
```kotlin
// 잘못된 예: LazyColumn 안에 세로 스크롤
LazyColumn {
    item {
        Column(Modifier.verticalScroll()) { }  // 오류 발생!
    }
}
```

### 3. 0픽셀 크기 아이템 피하기
```kotlin
// 이미지 로딩 시 크기 지정 필수
AsyncImage(
    model = imageUrl,
    modifier = Modifier.size(100.dp)  // 크기 지정!
)
```

---

## 연습 문제

### 연습 1: 기본 텍스트 리스트 (쉬움)
50개의 "아이템 N" 텍스트를 LazyColumn으로 표시하세요.
- contentPadding: 16.dp
- 아이템 간격: 8.dp

### 연습 2: 연락처 목록 만들기 (중간)
연락처 목록을 만드세요.
- 헤더: "연락처" 제목
- 각 연락처: 프로필 아이콘 + 이름 + 전화번호
- Card로 감싸기

### 연습 3: 사진 갤러리 그리드 (어려움)
선택 가능한 사진 갤러리를 만드세요.
- 2열 그리드 (GridCells.Fixed(2))
- 정사각형 카드 (aspectRatio(1f))
- 탭하면 선택 상태 토글 (체크 표시)

---

## 다음 학습

이 모듈에서 LazyColumn/LazyRow의 **기본 사용법**을 배웠습니다.

다음 단계로 **성능 최적화**를 학습하세요:
- **[lazy_layouts](../../list/lazy_layouts/README.md)**: key, contentType, animateItem

```
학습 경로:
lazy_list (기초) → lazy_layouts (최적화)
```

### 성능 최적화가 필요한 이유
- 아이템 삭제/추가 시 전체 리스트가 다시 그려지는 문제
- TextField 등 내부 상태가 손실되는 문제
- 리스트 애니메이션이 동작하지 않는 문제

이러한 문제는 `key`, `contentType`, `animateItem()`으로 해결할 수 있습니다!
