# Pager 학습

## 개념

Jetpack Compose의 **Pager**는 스와이프로 페이지를 전환하는 UI 컴포넌트입니다. 기존 View 시스템의 ViewPager와 유사하며, `HorizontalPager`(가로)와 `VerticalPager`(세로) 두 가지를 제공합니다.

Foundation 라이브러리에 포함되어 있으며, **LazyColumn/LazyRow처럼 Lazy하게 페이지를 생성**하여 성능을 최적화합니다.

## 핵심 특징

### 1. Lazy 페이지 생성
```kotlin
HorizontalPager(state = pagerState) { page ->
    // 현재 페이지 + 인접 페이지만 메모리에 유지
    PageContent(page)
}
```
- 모든 페이지를 동시에 렌더링하지 않음
- `beyondBoundsPageCount`로 미리 로드할 페이지 수 조절 가능

### 2. PagerState - 상태 관리
```kotlin
val pagerState = rememberPagerState(pageCount = { 5 })
```

| 속성 | 설명 |
|------|------|
| `currentPage` | 현재 표시 중인 페이지 인덱스 |
| `currentPageOffsetFraction` | 드래그 진행률 (-1.0 ~ 1.0) |
| `settledPage` | 애니메이션 완료 후 페이지 |
| `targetPage` | 애니메이션 목표 페이지 |
| `pageCount` | 전체 페이지 수 |

### 3. 프로그래밍적 페이지 전환
```kotlin
val scope = rememberCoroutineScope()

// 애니메이션과 함께 이동
scope.launch {
    pagerState.animateScrollToPage(targetPage)
}

// 즉시 이동 (애니메이션 없음)
scope.launch {
    pagerState.scrollToPage(targetPage)
}
```

---

## 문제 상황: Pager 없이 페이지 전환 구현

### 잘못된 코드 예시 1: AnimatedContent 사용
```kotlin
var currentPage by remember { mutableIntStateOf(0) }

AnimatedContent(targetState = currentPage) { page ->
    OnboardingPage(page)
}

Row {
    Button(onClick = { if (currentPage > 0) currentPage-- }) {
        Text("이전")
    }
    Button(onClick = { if (currentPage < 2) currentPage++ }) {
        Text("다음")
    }
}
```

### 발생하는 문제점

1. **스와이프 제스처 미지원**: 버튼으로만 전환 가능
2. **드래그 피드백 없음**: 사용자가 어디까지 스와이프했는지 알 수 없음
3. **자연스러운 UX 부재**: 모바일 사용자에게 익숙하지 않은 경험

### 잘못된 코드 예시 2: Row + horizontalScroll 사용
```kotlin
Row(
    modifier = Modifier.horizontalScroll(rememberScrollState())
) {
    repeat(5) { page ->
        Box(modifier = Modifier.fillMaxWidth()) { /* 내용 */ }
    }
}
```

### 발생하는 문제점

1. **스냅 동작 없음**: 페이지 중간에 멈춤
2. **모든 페이지 동시 생성**: 메모리 낭비
3. **현재 페이지 추적 어려움**: 스크롤 위치 → 페이지 번호 변환 필요

---

## 해결책: HorizontalPager / VerticalPager 사용

### 올바른 코드
```kotlin
val pagerState = rememberPagerState(pageCount = { 3 })
val scope = rememberCoroutineScope()

Column {
    // 스와이프 가능한 페이저
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.weight(1f)
    ) { page ->
        OnboardingPage(page)
    }

    // 페이지 인디케이터
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(3) { index ->
            val isSelected = pagerState.currentPage == index
            Box(
                modifier = Modifier
                    .size(if (isSelected) 10.dp else 8.dp)
                    .background(
                        color = if (isSelected) Color.Blue else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }

    // Skip 버튼
    Button(onClick = {
        scope.launch { pagerState.animateScrollToPage(2) }
    }) {
        Text("건너뛰기")
    }
}
```

### 해결되는 이유

1. **자동 스냅**: 스와이프 후 가장 가까운 페이지에 정렬
2. **Lazy 생성**: 현재 페이지 주변만 메모리에 유지
3. **풍부한 상태 정보**: currentPage, currentPageOffsetFraction 등 제공
4. **자연스러운 UX**: 모바일 사용자에게 익숙한 스와이프 경험

---

## 주요 파라미터

### HorizontalPager / VerticalPager
```kotlin
HorizontalPager(
    state = pagerState,                    // 필수: PagerState
    modifier = Modifier,                   // Modifier
    contentPadding = PaddingValues(),      // 페이지 패딩 (미리보기 효과)
    pageSize = PageSize.Fill,              // 페이지 크기
    beyondBoundsPageCount = 0,             // 미리 로드할 페이지 수
    pageSpacing = 0.dp,                    // 페이지 간 간격
    verticalAlignment = Alignment.CenterVertically,  // 세로 정렬
    flingBehavior = PagerDefaults.flingBehavior(pagerState),  // 플링 동작
    key = { it },                          // 페이지 key
    pageContent = { page -> ... }          // 페이지 내용
)
```

### contentPadding으로 미리보기 효과
```kotlin
HorizontalPager(
    state = pagerState,
    contentPadding = PaddingValues(horizontal = 32.dp)
) { page -> ... }
```

---

## 페이지 변경 감지

```kotlin
LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
        // 페이지 변경 시 실행
        analytics.logPageView(page)
    }
}
```

---

## 사용 시나리오

1. **온보딩 화면**: 앱 첫 실행 시 소개 화면
2. **이미지 갤러리**: 사진 뷰어
3. **탭 콘텐츠**: TabRow와 연동하여 탭별 콘텐츠 표시
4. **카드 캐러셀**: 카드 형태의 콘텐츠 슬라이더
5. **날짜 선택**: 캘린더의 월/주 전환

---

## 주의사항

### 1. pageCount는 람다로 전달
```kotlin
// 올바른 사용
val pagerState = rememberPagerState(pageCount = { items.size })

// 잘못된 사용 - 컴파일 에러
val pagerState = rememberPagerState(pageCount = items.size)
```

### 2. 프로그래밍적 스크롤은 코루틴에서
```kotlin
val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch {
        pagerState.animateScrollToPage(0)  // suspend 함수!
    }
}) { ... }
```

### 3. TabRow 연동 시 양방향 동기화 주의
```kotlin
// 탭 → 페이저 (탭 클릭 시)
Tab(onClick = {
    scope.launch { pagerState.animateScrollToPage(index) }
})

// 페이저 → 탭 (자동 - currentPage로 연동)
TabRow(selectedTabIndex = pagerState.currentPage)
```

---

## 연습 문제

### 연습 1: 기본 이미지 갤러리 (기초)
5개의 컬러 박스를 가로로 스와이프하고, 현재 페이지 번호를 표시하세요.

### 연습 2: 탭 + Pager 연동 (중급)
3개 탭과 3개 페이지를 양방향으로 연동하세요. 탭 클릭과 스와이프 모두 동작해야 합니다.

### 연습 3: 커스텀 페이지 인디케이터 (고급)
드래그 진행률(currentPageOffsetFraction)을 반영하여 부드럽게 이동하는 인디케이터를 구현하세요.

---

## 다음 학습

- Gesture: 커스텀 스와이프 제스처
- Animation: Pager와 애니메이션 연동
- LazyLayouts: LazyColumn/LazyRow와의 차이점

---

## 참고 자료

- [Android Developers - Pager in Compose](https://developer.android.com/develop/ui/compose/layouts/pager)
- [Android Developers - Add a custom page indicator](https://developer.android.com/develop/ui/compose/quick-guides/content/custom-page-indicator)
- [Composables - HorizontalPager](https://composables.com/foundation/horizontalpager)
