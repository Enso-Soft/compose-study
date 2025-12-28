# Carousel 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `pager` | HorizontalPager/VerticalPager를 이용한 페이지 전환 | [📚 학습하기](../../list/pager/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Carousel**은 Material 3에서 새로 도입된 가로 스크롤 콘텐츠 브라우저입니다.
"회전목마"처럼 옆으로 넘기며 여러 카드를 탐색할 수 있으며, 한 화면에 여러 아이템이 부분적으로 함께 보입니다.

기존 `LazyRow`나 `HorizontalPager`와 달리, **아이템 크기가 자동으로 조절**되어 더 자연스러운 브라우징 경험을 제공합니다.

## 핵심 특징

### 1. 여러 아이템이 동시에 보임
```kotlin
// 가운데 아이템이 가장 크고, 양옆 아이템은 작게 보임
HorizontalMultiBrowseCarousel(
    state = rememberCarouselState { items.size },
    preferredItemWidth = 186.dp
) { index -> ... }
```

### 2. 아이템 크기 자동 조절
- **MultiBrowse**: 공간에 맞게 large/medium/small 아이템 혼합
- **Uncontained**: 고정 크기, 화면 밖으로 자연스럽게 확장

### 3. Material 3 디자인 준수
- `maskClip()`으로 둥근 모서리 자동 적용
- 스크롤 시 부드러운 크기 전환 애니메이션

---

## 선행 학습
- **필수**: Compose 기본 (Composable 함수, Modifier)
- **권장**: LazyRow/LazyColumn (Carousel과 비교하기 위해)
- **선택**: HorizontalPager (선택 가이드 이해를 위해)

---

## 문제 상황: LazyRow로 이미지 갤러리 구현 시 한계

### 시나리오
쇼핑 앱에서 추천 상품 갤러리를 만들고 싶습니다.
현재 보고 있는 아이템을 크게 보여주고, 양쪽의 다른 아이템은 작게 보여주는 효과가 필요합니다.

### 잘못된 코드 예시
```kotlin
LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(products.size) { index ->
        // 모든 아이템이 동일한 크기!
        ProductCard(
            modifier = Modifier.size(186.dp, 205.dp)
        )
    }
}
```

### 발생하는 문제점
1. **아이템 크기 전환 효과 없음**: 모든 아이템이 동일한 크기로 표시됨
2. **포커스/강조 효과 직접 구현 필요**: 현재 아이템 강조가 어려움
3. **"더 있다" 힌트 부족**: 사용자가 스크롤 가능함을 인지하기 어려움
4. **Material Design 가이드라인 미준수**: 수동으로 디자인 맞춰야 함

---

## 해결책: Carousel 사용

### HorizontalMultiBrowseCarousel
여러 크기의 아이템이 섞여 보이는 캐러셀입니다. "서점 진열대"처럼 공간에 맞게 아이템 크기가 자동 조절됩니다.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiBrowseCarouselExample() {
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { items.size },
        modifier = Modifier
            .fillMaxWidth()
            .height(221.dp),
        preferredItemWidth = 186.dp,  // "이 정도 크기였으면 좋겠어" 요청
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { index ->
        val item = items[index]
        Image(
            modifier = Modifier
                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),  // 둥근 모서리
            painter = painterResource(id = item.imageResId),
            contentDescription = item.description,
            contentScale = ContentScale.Crop
        )
    }
}
```

### HorizontalUncontainedCarousel
모든 아이템이 고정 크기로 표시되며, 화면 끝에서 잘리지 않고 자연스럽게 넘어갑니다.
"영화관 포스터 벽"처럼 일정한 크기의 아이템이 나열됩니다.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UncontainedCarouselExample() {
    HorizontalUncontainedCarousel(
        state = rememberCarouselState { items.size },
        modifier = Modifier
            .fillMaxWidth()
            .height(221.dp),
        itemWidth = 186.dp,  // "정확히 이 크기로" 지정
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { index ->
        val item = items[index]
        Image(
            modifier = Modifier
                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),
            painter = painterResource(id = item.imageResId),
            contentDescription = item.description,
            contentScale = ContentScale.Crop
        )
    }
}
```

### 해결되는 이유
1. **자동 크기 조절**: 스크롤 위치에 따라 아이템 크기가 부드럽게 변화
2. **포커스 효과 내장**: 가운데 아이템이 자연스럽게 강조됨
3. **"더 있다" 힌트**: 양쪽에 부분적으로 보이는 아이템이 스크롤 가능함을 암시
4. **Material 3 준수**: 디자인 가이드라인에 맞는 UX 자동 제공

---

## 주요 파라미터

### CarouselState
```kotlin
// 현재 위치와 스크롤 상태를 관리
val state = rememberCarouselState { itemCount }
```
- 현재 어디를 보고 있는지 기억하는 객체
- `itemCount`는 람다로 전달 (동적 변경 지원)

### preferredItemWidth vs itemWidth

| 파라미터 | 의미 | 동작 |
|---------|------|------|
| `preferredItemWidth` | "이 정도 크기였으면 좋겠어" | 공간에 맞게 자동 조절 |
| `itemWidth` | "정확히 이 크기로" | 고정 크기 유지 |

### 기타 파라미터
- `itemSpacing`: 아이템 사이의 간격
- `contentPadding`: 캐러셀 양쪽 끝의 여백
- `modifier`: 크기, 배경 등 설정

---

## MultiBrowse vs Uncontained 비교

| 특성 | MultiBrowse | Uncontained |
|------|-------------|-------------|
| 아이템 크기 | 다양 (large/medium/small) | 고정 |
| 공간 활용 | 화면에 맞게 최적화 | 일정한 아이템 표시 |
| 적합한 용도 | 많은 콘텐츠 빠르게 탐색 | 균일한 크기가 중요할 때 |
| 예시 | 앨범 아트, 제품 썸네일 | 영화 포스터, 카드 목록 |

---

## Carousel vs HorizontalPager: 언제 무엇을 쓸까?

| 기준 | Carousel | HorizontalPager |
|------|----------|-----------------|
| 한 번에 보이는 아이템 | 여러 개 (부분 포함) | 1개 (전체) |
| 아이템 크기 변화 | 자동 조절 | 고정 |
| 적합한 용도 | 썸네일 브라우징 | 전체 화면 콘텐츠 |
| 스냅 방식 | 아이템 단위 | 페이지 단위 |
| 예시 | 앨범 아트, 추천 상품 | 온보딩, 이미지 뷰어 |

### 선택 가이드
```
아이템을 전체 화면으로 보여줘야 하나요?
  ├── Yes → HorizontalPager
  └── No → 여러 아이템을 빠르게 탐색해야 하나요?
              ├── Yes → Carousel
              └── No → LazyRow도 고려
```

---

## 주의사항

### 1. ExperimentalMaterial3Api 어노테이션 필요
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselScreen() { ... }
```
Carousel API는 아직 실험적(Experimental) 상태입니다.

### 2. Material 3 의존성 필요
```kotlin
// build.gradle.kts
implementation("androidx.compose.material3:material3:1.4.0-alpha01")
// 또는 BOM 사용
implementation(platform("androidx.compose:compose-bom:2024.01.00"))
implementation("androidx.compose.material3:material3")
```

### 3. 이미지에 maskClip 적용
```kotlin
Image(
    modifier = Modifier
        .height(205.dp)
        .maskClip(MaterialTheme.shapes.extraLarge),  // 둥근 모서리
    ...
)
```
`maskClip`은 `CarouselItemScope` 내에서만 사용 가능합니다.

---

## 연습 문제

### 연습 1: 기본 캐러셀 만들기 (쉬움)
5개의 컬러 카드를 표시하는 `HorizontalMultiBrowseCarousel`을 만드세요.
각 카드에 인덱스 번호를 표시합니다.

### 연습 2: 영화 포스터 캐러셀 (중간)
`HorizontalUncontainedCarousel`을 사용하여 영화 포스터 캐러셀을 만드세요.
카드 클릭 시 Snackbar로 영화 제목을 표시합니다.

### 연습 3: 인디케이터가 있는 캐러셀 (어려움)
`HorizontalMultiBrowseCarousel` 하단에 현재 위치를 표시하는 인디케이터를 추가하세요.
인디케이터 클릭 시 해당 위치로 스크롤합니다.

---

## 참고: 다른 Carousel 유형

Material 3에서는 4가지 캐러셀 레이아웃을 제공합니다:

1. **Multi-browse** (이 모듈에서 학습): 여러 크기 아이템 혼합
2. **Uncontained** (이 모듈에서 학습): 고정 크기 아이템
3. **Hero**: 하나의 큰 이미지 강조 + 다음 미리보기
4. **Full-screen**: 한 번에 한 아이템 전체 화면

Hero와 Full-screen은 별도 학습 모듈에서 다룹니다.

---

## 다음 학습

- **Pager**: 전체 화면 페이지 전환 (HorizontalPager, VerticalPager)
- **LazyLayouts**: 리스트 성능 최적화 (LazyColumn, LazyRow)
- **Animation**: Carousel과 애니메이션 연동

---

## 참고 자료

- [Android Developers - Carousel](https://developer.android.com/develop/ui/compose/components/carousel)
- [Material 3 - Carousel Guidelines](https://m3.material.io/components/carousel/overview)
- [Composables - HorizontalMultiBrowseCarousel](https://composables.com/material3/horizontalmultibrowsecarousel)
