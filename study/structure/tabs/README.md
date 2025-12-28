# Tabs 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `scaffold` | Scaffold의 기본 슬롯 구조와 paddingValues 사용법 | [📚 학습하기](../../structure/scaffold/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**TabRow**와 **Tab**을 사용하여 Material Design 규격의 탭 UI를 구현하는 방법을 학습합니다.
탭은 같은 화면 내에서 관련된 콘텐츠 그룹 간 전환을 가능하게 하는 UI 패턴입니다.

> TV 리모컨을 생각해보세요. 채널 버튼(Tab)을 누르면 해당 채널(콘텐츠)로 전환되고,
> 현재 채널에 표시등(indicator)이 켜지며, 버튼들이 한 줄로 정렬(TabRow)되어 있습니다.

## 핵심 특징

1. **PrimaryTabRow** - 앱의 메인 콘텐츠 목적지용 탭 (상단 앱바 아래 배치)
2. **SecondaryTabRow** - 콘텐츠 내부의 서브 네비게이션용 탭
3. **PrimaryScrollableTabRow** - 탭이 많을 때 스크롤 가능한 탭
4. **Tab** - 개별 탭으로, 텍스트, 아이콘, 또는 둘 다 표시 가능
5. **HorizontalPager 연동** - 스와이프로 탭 전환 가능

## 사전 지식

- `remember`와 `mutableStateOf`로 상태 관리하기
- 기본 레이아웃 (Column, Row)
- [연습 3] `LaunchedEffect` 기초

---

## 문제 상황: Row + Button으로 탭을 직접 만들면?

### 시나리오

음악 스트리밍 앱을 만들고 있습니다. "음악", "동영상", "사진" 3개 카테고리를 탭으로 전환하는 UI가 필요합니다.
"Row에 Button 3개 넣으면 되지 않을까?" 하고 직접 구현을 시도합니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun ManualTabsScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("음악", "동영상", "사진")

    Column {
        // Row + Button으로 직접 만든 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, title ->
                Button(
                    onClick = { selectedTab = index },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(title)
                }
            }
        }

        // 콘텐츠 영역
        when (selectedTab) {
            0 -> Text("음악 콘텐츠")
            1 -> Text("동영상 콘텐츠")
            2 -> Text("사진 콘텐츠")
        }
    }
}
```

### 발생하는 문제점

1. **인디케이터 애니메이션 부재**
   - Material Design 탭은 선택 시 부드러운 인디케이터 슬라이딩 애니메이션이 있음
   - 직접 구현하려면 `animateDpAsState`, `offset` 계산 등 복잡한 코드 필요

2. **Material Design 규격 위반**
   - 탭 높이, 간격, 색상 등 Material 가이드라인 수동 적용 필요
   - 잘못된 디자인으로 앱 전체 일관성 저하

3. **접근성(a11y) 처리 누락**
   - 탭 역할(role="tab"), 선택 상태(selected) 등 시맨틱 정보 없음
   - 스크린 리더 사용자가 탭으로 인식하지 못함

4. **일관성 문제**
   - 탭 너비 균등 분배 수동 관리
   - 텍스트 오버플로우 처리 누락

---

## 해결책: TabRow 사용

### 1. 기본 PrimaryTabRow

```kotlin
@Composable
fun BasicTabsScreen() {
    // 1. 상태 변수 선언 - 현재 선택된 탭 번호를 저장
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("음악", "동영상", "사진")

    Column(modifier = Modifier.fillMaxSize()) {
        // 2. PrimaryTabRow로 탭 행 생성
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            // 3. 각 탭 생성
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,  // 현재 탭이 선택되었는지
                    onClick = { selectedTab = index }, // 탭 클릭 시 상태 업데이트
                    text = { Text(title) }
                )
            }
        }

        // 4. 선택된 탭에 따른 콘텐츠 표시
        when (selectedTab) {
            0 -> MusicContent()
            1 -> VideoContent()
            2 -> PhotoContent()
        }
    }
}
```

**해결되는 점:**
- 인디케이터 애니메이션 자동 적용
- Material Design 규격 자동 준수
- 접근성 시맨틱 자동 처리
- 탭 너비 균등 분배 자동

### 2. 아이콘 + 텍스트 조합

```kotlin
@Composable
fun IconTextTabsScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        "음악" to Icons.Default.MusicNote,
        "동영상" to Icons.Default.VideoLibrary,
        "사진" to Icons.Default.Photo
    )

    PrimaryTabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, (title, icon) ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(title) },
                icon = { Icon(icon, contentDescription = null) }
            )
        }
    }
}
```

### 3. PrimaryTabRow vs SecondaryTabRow

| 특성 | PrimaryTabRow | SecondaryTabRow |
|------|--------------|-----------------|
| 위치 | 상단 앱바 바로 아래 | 콘텐츠 영역 내부 |
| 용도 | 메인 콘텐츠 네비게이션 | 서브 콘텐츠 구분 |
| 시각적 강조 | 강함 (주요 인디케이터) | 약함 (보조 인디케이터) |
| 예시 | 뉴스/스포츠/영화 | 국내뉴스/국제뉴스/경제 |

```kotlin
// Primary: 메인 네비게이션
PrimaryTabRow(selectedTabIndex = selectedTab) {
    // 앱의 주요 섹션 탭
}

// Secondary: 서브 네비게이션
SecondaryTabRow(selectedTabIndex = selectedTab) {
    // 현재 섹션 내 세부 분류 탭
}
```

### 4. ScrollableTabRow (탭이 많을 때)

탭이 5개 이상으로 많아지면 화면에 다 표시되지 않을 수 있습니다.
이때 `PrimaryScrollableTabRow`를 사용하면 스크롤이 가능합니다.

```kotlin
@Composable
fun ScrollableTabsScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("탭1", "탭2", "탭3", "탭4", "탭5", "탭6", "탭7")

    PrimaryScrollableTabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(title) }
            )
        }
    }
}
```

### 5. HorizontalPager 연동

스와이프로 탭을 전환하고 싶다면 `HorizontalPager`와 연동합니다.

```kotlin
@Composable
fun TabsWithPagerScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("음악", "동영상", "사진")
    val pagerState = rememberPagerState { tabs.size }

    // 탭 클릭 -> 페이저 이동
    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    // 페이저 스와이프 -> 탭 업데이트
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTab = pagerState.currentPage
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> MusicContent()
                1 -> VideoContent()
                2 -> PhotoContent()
            }
        }
    }
}
```

---

## API 참조

### Tab

```kotlin
@Composable
fun Tab(
    selected: Boolean,                    // 선택 상태
    onClick: () -> Unit,                  // 클릭 핸들러
    text: @Composable (() -> Unit)? = null,  // 텍스트 (선택)
    icon: @Composable (() -> Unit)? = null,  // 아이콘 (선택)
    enabled: Boolean = true,              // 활성화 여부
    selectedContentColor: Color,          // 선택 시 색상
    unselectedContentColor: Color         // 미선택 시 색상
)
```

### PrimaryTabRow

```kotlin
@Composable
fun PrimaryTabRow(
    selectedTabIndex: Int,               // 현재 선택된 탭 인덱스 (0부터 시작)
    modifier: Modifier = Modifier,
    containerColor: Color,               // 배경색
    contentColor: Color,                 // 콘텐츠 색상
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit,  // 커스텀 인디케이터
    divider: @Composable () -> Unit,     // 하단 구분선
    tabs: @Composable () -> Unit         // 탭 콘텐츠
)
```

---

## 사용 시나리오

1. **미디어 앱**: 음악/동영상/팟캐스트 카테고리 전환
2. **뉴스 앱**: 뉴스/스포츠/엔터테인먼트/경제 섹션
3. **쇼핑 앱**: 상품정보/리뷰/문의 탭
4. **설정 화면**: 일반/알림/개인정보 설정 탭

---

## 주의사항

- **TabRow vs ScrollableTabRow 선택**: 탭이 3-4개면 `PrimaryTabRow`, 5개 이상이면 `PrimaryScrollableTabRow`
- **selectedTabIndex 범위**: 0부터 시작, 탭 개수 - 1까지
- **상태 저장**: 화면 회전 시에도 유지하려면 `rememberSaveable` 사용
- **Deprecated API 주의**: `TabRow`, `ScrollableTabRow`는 deprecated, `PrimaryTabRow`/`SecondaryTabRow` 사용

---

## 연습 문제

### 연습 1: 기본 탭 만들기 (쉬움)

"홈", "검색", "프로필" 3개 탭을 만들고, 선택된 탭에 따라 다른 텍스트를 표시하세요.

**요구사항:**
- `PrimaryTabRow`와 `Tab` 사용
- 탭 선택 시 해당 텍스트 표시

### 연습 2: 아이콘 + 텍스트 탭 (중간)

"음악", "동영상", "사진" 탭에 각각 아이콘을 추가하세요.

**요구사항:**
- 음악: `Icons.Default.MusicNote`
- 동영상: `Icons.Default.VideoLibrary`
- 사진: `Icons.Default.Photo`
- 선택된 탭에 따른 콘텐츠 표시

### 연습 3: HorizontalPager 연동 (어려움)

탭과 HorizontalPager를 연동하여 스와이프로 탭을 전환할 수 있게 만드세요.

**요구사항:**
- "뉴스", "스포츠", "엔터테인먼트" 3개 탭
- 탭 클릭 시 해당 페이지로 애니메이션 이동
- 페이지 스와이프 시 탭 인디케이터 자동 이동
- `LaunchedEffect`로 양방향 동기화

---

## 다음 학습

- **HorizontalPager**: 페이지 스와이프 UI 심화
- **Navigation**: 화면 간 네비게이션
- **NavigationBar**: 하단 네비게이션 바
