# FlowLayout 학습

## 개념

`FlowRow`와 `FlowColumn`은 **공간이 부족하면 자동으로 다음 줄/열로 래핑**되는 레이아웃 컴포저블입니다.

```kotlin
FlowRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    tags.forEach { tag ->
        FilterChip(...)
    }
}
```

## Row/Column vs FlowRow/FlowColumn

| 특성 | Row/Column | FlowRow/FlowColumn |
|------|------------|-------------------|
| 공간 부족 시 | 잘리거나 오버플로우 | 다음 줄/열로 래핑 |
| 사용 사례 | 고정된 아이템 수 | 동적/가변 아이템 수 |
| 스크롤 필요 | 별도 설정 필요 | 래핑으로 해결 |

---

## 핵심 특징

### 1. 자동 래핑
공간이 부족하면 자동으로 다음 줄로 넘어갑니다.

### 2. Arrangement 옵션
아이템 간 간격과 배치 방식을 세밀하게 제어할 수 있습니다.

### 3. maxItemsInEachRow/Column
한 줄에 들어갈 최대 아이템 수를 제한할 수 있습니다.

### 4. Modifier.weight() 지원
FlowRow 내에서 weight를 사용하면 해당 줄의 남은 공간을 분배받습니다.

---

## FlowRow vs FlowColumn

| 속성 | FlowRow | FlowColumn |
|------|---------|------------|
| 주 축 | 가로 (Horizontal) | 세로 (Vertical) |
| 래핑 방향 | 아래로 (다음 행) | 오른쪽으로 (다음 열) |
| 주 축 정렬 | `horizontalArrangement` | `verticalArrangement` |
| 교차 축 정렬 | `verticalArrangement` | `horizontalArrangement` |
| 아이템 제한 | `maxItemsInEachRow` | `maxItemsInEachColumn` |

---

## Arrangement 옵션

### horizontalArrangement (FlowRow) / verticalArrangement (FlowColumn)

```
Arrangement.Start (기본)
[A][B][C]              |

Arrangement.Center
     [A][B][C]         |

Arrangement.End
              [A][B][C]|

Arrangement.SpaceEvenly (양 끝 포함 균등)
|  [A]  [B]  [C]  |

Arrangement.SpaceBetween (양 끝 제외 균등)
[A]    [B]    [C]|

Arrangement.SpaceAround (양 끝 절반)
| [A]   [B]   [C] |

Arrangement.spacedBy(8.dp)
[A] 8dp [B] 8dp [C]
```

---

## 문제 상황: Row로 태그 구현 시 오버플로우

### 잘못된 코드 예시

```kotlin
@Composable
fun BadTagList(tags: List<String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        tags.forEach { tag ->
            FilterChip(
                selected = false,
                onClick = { },
                label = { Text(tag) }
            )
        }
    }
}
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 아이템 잘림 | 화면 너비를 초과하면 보이지 않음 |
| 스크롤 추가 필요 | `horizontalScroll` 추가해도 래핑은 안 됨 |
| UX 저하 | 사용자가 모든 태그를 볼 수 없음 |

---

## 해결책: FlowRow 사용

### 올바른 코드

```kotlin
@Composable
fun GoodTagList(tags: List<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tags.forEach { tag ->
            FilterChip(
                selected = false,
                onClick = { },
                label = { Text(tag) }
            )
        }
    }
}
```

### 해결되는 이유

| 해결 | 설명 |
|------|------|
| 자동 래핑 | 공간 부족 시 다음 줄로 자연스럽게 이동 |
| 반응형 | 화면 크기에 따라 자동 조절 |
| 모든 태그 표시 | 스크롤 없이 모든 아이템 표시 가능 |

---

## 실전 활용 사례

### 1. 태그/칩 필터 UI
```kotlin
FlowRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    categories.forEach { category ->
        FilterChip(
            selected = selectedCategories.contains(category),
            onClick = { toggleCategory(category) },
            label = { Text(category) }
        )
    }
}
```

### 2. 행당 고정 개수 그리드 (maxItemsInEachRow)
```kotlin
FlowRow(
    maxItemsInEachRow = 3,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items.forEach { item ->
        Card(modifier = Modifier.weight(1f)) {
            Text(item)
        }
    }
}
```

### 3. FlowColumn으로 세로 래핑
```kotlin
FlowColumn(
    modifier = Modifier.height(200.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    items.forEach { item ->
        Chip(item)
    }
}
```

---

## 주의사항

### 1. 대량 아이템은 Lazy 레이아웃 사용
FlowRow는 모든 아이템을 한 번에 compose하므로, 대량의 아이템은 성능 문제가 발생할 수 있습니다.

```kotlin
// 대량 데이터 -> LazyVerticalGrid 사용
LazyVerticalGrid(columns = GridCells.Fixed(3)) {
    items(largeList) { item -> ... }
}
```

### 2. FlowColumn에는 고정 높이 필요
FlowColumn이 래핑되려면 컨테이너에 고정 높이가 필요합니다.

```kotlin
FlowColumn(
    modifier = Modifier.height(200.dp)  // 필수!
) { ... }
```

### 3. weight는 해당 줄 내에서만 적용
`Modifier.weight()`는 아이템이 배치된 줄의 남은 공간만 분배받습니다.

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | Row 오버플로우 문제 시연 |
| `Solution.kt` | FlowRow/FlowColumn 사용법, Arrangement 비교 |
| `Practice.kt` | 연습 문제 3개 (태그, 그리드, FlowColumn) |

---

## 연습 문제

1. **기본 태그 클라우드**: FlowRow로 선택 가능한 태그 UI 구현
2. **그리드 레이아웃**: maxItemsInEachRow와 weight로 균등 그리드 구현
3. **FlowColumn 사용**: 세로 방향 래핑 레이아웃 구현

---

## 다음 학습

- `LazyVerticalGrid` / `LazyHorizontalGrid`: 대량 아이템용 그리드
- `ContextualFlowRow`: 대량 데이터 + 오버플로우 처리용 (Compose Foundation 1.7+)
