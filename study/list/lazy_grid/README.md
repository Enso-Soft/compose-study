# LazyGrid 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `LazyColumn` | 효율적인 스크롤 리스트 구현 | [📚 학습하기](../lazy_column/README.md) |
| `Modifier` | Composable의 레이아웃과 동작 수정 | [📚 학습하기](../../layout/layout_and_modifier/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

LazyGrid는 **화면에 보이는 아이템만 렌더링**하는 그리드 컴포넌트입니다. 마치 똑똑한 사진첩처럼, 현재 보고 있는 페이지만 꺼내서 보여주기 때문에 1000장의 사진이 있어도 빠르게 스크롤할 수 있습니다.

## 핵심 특징

1. **Lazy Loading**: 화면에 보이는 아이템만 생성하여 메모리 절약
2. **유연한 열 설정**: Fixed(고정 개수) 또는 Adaptive(반응형)로 열 구성
3. **Span 지원**: 특정 아이템이 여러 열을 차지하도록 설정 가능
4. **Staggered Grid**: Pinterest처럼 다양한 높이의 아이템 지원

---

## 문제 상황: Column/Row로 그리드 구현 시

### 시나리오
사진 갤러리 앱에서 100장의 사진을 2열 그리드로 표시하려고 합니다.

### 잘못된 코드
```kotlin
// Column과 Row를 중첩하여 그리드 구현
Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
    photos.chunked(2).forEach { rowPhotos ->
        Row {
            rowPhotos.forEach { photo ->
                PhotoItem(photo)
            }
        }
    }
}
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 메모리 과다 사용 | 100개 아이템이 모두 한 번에 메모리에 로드됨 |
| 느린 초기 로딩 | 모든 아이템을 생성한 후에야 화면 표시 |
| 비효율적 Recomposition | 하나 변경 시 전체 그리드 다시 그림 |
| 스크롤 버벅거림 | 많은 아이템에서 60fps 유지 불가 |

```
Column/Row 방식:              LazyGrid 방식:
┌───┬───┐                     ┌───┬───┐
│ 1 │ 2 │ ← 렌더링            │ 1 │ 2 │ ← 렌더링
├───┼───┤                     ├───┼───┤
│ 3 │ 4 │ ← 렌더링            │ 3 │ 4 │ ← 렌더링
├───┼───┤                     ├───┼───┤
│...│...│                     │ 5 │ 6 │ ← 렌더링
├───┼───┤ ← 100개 전부!       └───┴───┘
│99 │100│                     (화면 밖은 생략)
└───┴───┘
```

---

## 해결책: LazyGrid 사용

### 1. LazyVerticalGrid 기본 사용법

```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),  // 정확히 2열
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(16.dp)
) {
    items(100, key = { it }) { index ->
        PhotoItem(photos[index])
    }
}
```

### 2. GridCells 옵션

#### GridCells.Fixed(count)
정확히 지정된 개수의 열을 생성합니다.

```kotlin
// 항상 2열
columns = GridCells.Fixed(2)
```
- 화면 크기와 관계없이 항상 같은 열 개수
- 설정 화면의 아이콘 그리드에 적합

#### GridCells.Adaptive(minSize)
최소 크기 기준으로 열 개수가 자동 조정됩니다.

```kotlin
// 아이템 최소 너비 100dp
columns = GridCells.Adaptive(minSize = 100.dp)
```
- 화면이 넓으면 열이 늘어남
- 태블릿/폰 대응에 적합

### 3. Span으로 헤더 만들기

특정 아이템이 여러 열을 차지하도록 설정합니다.

```kotlin
LazyVerticalGrid(columns = GridCells.Fixed(3)) {
    // 전체 너비를 차지하는 헤더
    item(span = { GridItemSpan(maxLineSpan) }) {
        Text("카테고리 헤더", style = MaterialTheme.typography.headlineSmall)
    }

    // 일반 아이템들
    items(products) { product ->
        ProductCard(product)
    }
}
```

### 4. LazyVerticalStaggeredGrid (Pinterest 스타일)

아이템 높이가 다양한 그리드를 만듭니다.

```kotlin
LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Fixed(2),
    verticalItemSpacing = 8.dp,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(photos) { photo ->
        // 각 아이템의 높이가 다를 수 있음
        AsyncImage(
            model = photo.url,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        )
    }
}
```

### 5. LazyGridState로 스크롤 제어

```kotlin
val gridState = rememberLazyGridState()
val coroutineScope = rememberCoroutineScope()

// 현재 첫 번째 보이는 아이템 확인
val firstVisibleItem = gridState.firstVisibleItemIndex

// 프로그래밍 방식으로 스크롤
Button(onClick = {
    coroutineScope.launch {
        gridState.animateScrollToItem(0)  // 맨 위로 이동
    }
}) {
    Text("맨 위로")
}

LazyVerticalGrid(
    state = gridState,
    columns = GridCells.Fixed(2)
) { ... }
```

---

## 사용 시나리오

### 언제 Fixed를 사용하나요?
- 화면 너비에 관계없이 정확한 열 개수가 필요할 때
- 예: 설정 화면 아이콘, 이모지 선택기

### 언제 Adaptive를 사용하나요?
- 다양한 화면 크기에 대응해야 할 때
- 예: 사진 갤러리, 상품 목록

### 언제 Staggered를 사용하나요?
- 아이템 높이가 다양할 때
- 예: Pinterest 스타일 피드, 이미지 콜라주

---

## 주의사항

### key 사용하기
아이템에 고유 key를 지정하면 상태가 유지되고 성능이 향상됩니다.

```kotlin
items(photos, key = { it.id }) { photo ->
    PhotoItem(photo)
}
```

### 한 item에 여러 요소 넣지 않기
```kotlin
// 나쁜 예: 하나의 item에 여러 Composable
item {
    Text("제목")
    Image(...)
    Text("설명")
}

// 좋은 예: 각각 별도의 item으로
item { Text("제목") }
item { Image(...) }
item { Text("설명") }
```

### LazyHorizontalGrid도 있습니다
가로 스크롤 그리드가 필요하면 LazyHorizontalGrid를 사용하세요.

```kotlin
LazyHorizontalGrid(
    rows = GridCells.Fixed(2),  // 2행
    modifier = Modifier.height(200.dp)
) { ... }
```

---

## 연습 문제

### 연습 1: 기본 이미지 그리드 (쉬움)
- 20개의 색상 카드를 2열 그리드로 표시
- GridCells.Fixed(2) 사용
- 아이템 간 8dp 간격

### 연습 2: 반응형 갤러리 (중간)
- 50개의 아이템을 반응형 그리드로 표시
- GridCells.Adaptive(100.dp) 사용
- 화면 회전 시 열 개수 변화 확인

### 연습 3: Pinterest 스타일 (어려움)
- LazyVerticalStaggeredGrid 사용
- 3개 카테고리 섹션 (최근, 인기, 추천)
- 각 카테고리에 헤더 (전체 너비)
- 다양한 높이의 아이템

---

## 다음 학습

- [pager](../pager/README.md): 페이지 스와이프 구현
- [pull_to_refresh](../pull_to_refresh/README.md): 당겨서 새로고침
- [paging_compose](../paging_compose/README.md): 무한 스크롤 구현

---

## 참고 자료

- [Lists and grids | Android Developers](https://developer.android.com/develop/ui/compose/lists)
- [Create a scrollable grid | Android Developers](https://developer.android.com/develop/ui/compose/quick-guides/content/create-scrollable-grid)
