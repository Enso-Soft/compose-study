# App Bar (TopAppBar) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `scaffold` | Scaffold의 기본 슬롯 구조와 paddingValues 사용법 | [📚 학습하기](../../structure/scaffold/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**App Bar**는 화면 상단(또는 하단)에 위치하여 **제목, 네비게이션, 주요 액션 버튼**을 담는 UI 컴포넌트입니다.
마치 '앱의 명함'처럼 사용자가 처음 보는 영역이며, 일관된 네비게이션 경험을 제공합니다.

Material 3에서는 **4가지 TopAppBar 유형**과 **BottomAppBar**를 제공합니다.

---

## 문제 상황: Row로 직접 구현하면?

### 시나리오
"새 앱을 만들면서 화면 상단에 뒤로가기 버튼, 제목, 메뉴 아이콘을 넣고 싶다."

### 직접 구현 시 코드
```kotlin
@Composable
fun ManualAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF6200EE))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* 뒤로가기 */ }) {
            Icon(Icons.Default.ArrowBack, "뒤로가기", tint = Color.White)
        }
        Text(
            text = "화면 제목",
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontSize = 20.sp
        )
        IconButton(onClick = { /* 메뉴 */ }) {
            Icon(Icons.Default.Menu, "메뉴", tint = Color.White)
        }
    }
}
```

### 발생하는 문제점
1. **높이/패딩 직접 지정** - Material Design 가이드라인 위반 가능
2. **색상 하드코딩** - 다크모드 대응이 어려움
3. **스크롤 연동 없음** - 축소/확장 효과를 직접 구현해야 함
4. **StatusBar 처리 복잡** - 시스템 UI 영역과의 충돌
5. **일관성 부족** - 아이콘 간격, 터치 영역 등을 일일이 맞춰야 함

---

## 해결책: TopAppBar 사용

### 기본 구조
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopAppBarExample() {
    TopAppBar(
        title = { Text("화면 제목") },
        navigationIcon = {
            IconButton(onClick = { /* 뒤로가기 */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
            }
        },
        actions = {
            IconButton(onClick = { /* 검색 */ }) {
                Icon(Icons.Default.Search, "검색")
            }
            IconButton(onClick = { /* 더보기 */ }) {
                Icon(Icons.Default.MoreVert, "더보기")
            }
        }
    )
}
```

### 핵심 파라미터
| 파라미터 | 설명 |
|---------|------|
| `title` | 앱바에 표시할 제목 (Composable) |
| `navigationIcon` | 왼쪽 네비게이션 아이콘 (뒤로가기, 햄버거 메뉴 등) |
| `actions` | 오른쪽 액션 아이콘들 (검색, 더보기 등) |
| `colors` | 앱바 색상 커스터마이즈 |
| `scrollBehavior` | 스크롤 시 앱바 동작 방식 |

---

## 4가지 TopAppBar 유형 비교

### 1. TopAppBar (Small)
가장 기본적인 형태. 네비게이션과 액션이 적은 화면에 적합.

```kotlin
TopAppBar(
    title = { Text("Small TopAppBar") },
    navigationIcon = { /* ... */ },
    actions = { /* ... */ }
)
```

### 2. CenterAlignedTopAppBar
제목이 중앙에 정렬됨. 브랜드 강조나 단일 주요 액션 화면에 적합.

```kotlin
CenterAlignedTopAppBar(
    title = { Text("Center Aligned") },
    navigationIcon = { /* ... */ },
    actions = { /* ... */ }
)
```

### 3. MediumTopAppBar
중간 크기의 제목. 상세 페이지나 프로필 화면에 적합.
스크롤 시 Small 크기로 축소 가능.

```kotlin
MediumTopAppBar(
    title = { Text("Medium TopAppBar") },
    navigationIcon = { /* ... */ },
    actions = { /* ... */ },
    scrollBehavior = scrollBehavior
)
```

### 4. LargeTopAppBar
큰 제목 영역. 메인 대시보드, 앨범 화면 등에 적합.
스크롤 시 크게 축소되어 공간 활용도가 높음.

```kotlin
LargeTopAppBar(
    title = { Text("Large TopAppBar") },
    navigationIcon = { /* ... */ },
    actions = { /* ... */ },
    scrollBehavior = scrollBehavior
)
```

---

## 비교 표

| 유형 | 제목 위치 | 제목 크기 | 적합한 화면 |
|------|----------|----------|-------------|
| **TopAppBar** | 왼쪽 | 작음 | 일반 리스트, 설정 |
| **CenterAlignedTopAppBar** | 중앙 | 작음 | 랜딩 페이지, 브랜드 강조 |
| **MediumTopAppBar** | 왼쪽 (2줄) | 중간 | 상세 페이지, 프로필 |
| **LargeTopAppBar** | 왼쪽 (2줄) | 큼 | 대시보드, 갤러리 |

---

## scrollBehavior: 스크롤 연동

스크롤할 때 앱바가 어떻게 반응할지 결정합니다. 마치 '창문 블라인드'처럼 동작합니다.

### 3가지 유형

| 유형 | 동작 | 비유 |
|------|------|------|
| `pinnedScrollBehavior()` | 항상 고정, 스크롤에 반응 없음 | 블라인드가 항상 열려 있음 |
| `enterAlwaysScrollBehavior()` | 위로 스크롤하면 숨김, 아래로 스크롤하면 나타남 | 손으로 올리면 닫히고, 내리면 열림 |
| `exitUntilCollapsedScrollBehavior()` | 콘텐츠 끝에서만 완전히 나타남 | 완전히 닫힌 후에야 열림 |

### 사용법
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableTopAppBar() {
    // 1. scrollBehavior 생성
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        // 2. nestedScroll 연결 (필수!)
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text("스크롤 테스트") },
                // 3. scrollBehavior 전달
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(50) { index ->
                Text("Item $index", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
```

**핵심 포인트:**
1. `TopAppBarDefaults.xxxScrollBehavior()` 로 생성
2. `Modifier.nestedScroll()` 로 Scaffold에 연결 (필수!)
3. TopAppBar의 `scrollBehavior` 파라미터에 전달

---

## BottomAppBar: 하단 앱바

화면 하단에 위치하는 앱바. 자주 쓰는 액션을 엄지손가락이 닿기 쉬운 곳에 배치할 때 사용합니다.

### 기본 사용법
```kotlin
@Composable
fun BottomAppBarExample() {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { /* ... */ }) {
                        Icon(Icons.Filled.Check, "확인")
                    }
                    IconButton(onClick = { /* ... */ }) {
                        Icon(Icons.Filled.Edit, "편집")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { /* ... */ }) {
                        Icon(Icons.Filled.Add, "추가")
                    }
                }
            )
        }
    ) { padding ->
        // 콘텐츠
    }
}
```

### 핵심 파라미터
| 파라미터 | 설명 |
|---------|------|
| `actions` | 왼쪽에 위치하는 액션 아이콘들 |
| `floatingActionButton` | 오른쪽에 위치하는 FAB (선택사항) |

---

## 상황별 선택 가이드

### 의사결정 플로우차트
```
시작
  |
  ├── 제목을 중앙에 두고 싶다?
  |     └── Yes ──► CenterAlignedTopAppBar
  |
  ├── 큰 제목이 필요하다?
  |     ├── 매우 큰 제목 ──► LargeTopAppBar
  |     └── 중간 크기 ──► MediumTopAppBar
  |
  ├── 스크롤 시 숨기고 싶다?
  |     ├── 빠르게 숨기고 나타나기 ──► enterAlwaysScrollBehavior
  |     └── 끝에서만 완전히 나타나기 ──► exitUntilCollapsedScrollBehavior
  |
  ├── 항상 보여야 한다?
  |     └── Yes ──► pinnedScrollBehavior (또는 생략)
  |
  └── 그 외 기본 ──► TopAppBar
```

### 실제 앱 예시
| 앱 유형 | 추천 TopAppBar | 이유 |
|---------|---------------|------|
| 설정 화면 | TopAppBar (Small) | 단순한 네비게이션 |
| 이메일 앱 메인 | LargeTopAppBar | 많은 정보, 스크롤 연동 |
| 채팅 앱 | CenterAlignedTopAppBar | 상대방 이름 강조 |
| 뉴스 상세 | MediumTopAppBar + exitUntilCollapsed | 제목 확인 후 콘텐츠 집중 |

---

## 베스트 프랙티스

1. **Scaffold와 함께 사용**
   ```kotlin
   Scaffold(topBar = { TopAppBar(...) }) { padding ->
       // padding 반드시 적용!
   }
   ```

2. **RTL 지원 아이콘 사용**
   ```kotlin
   // 권장
   Icon(Icons.AutoMirrored.Filled.ArrowBack, ...)
   // 비권장
   Icon(Icons.Default.ArrowBack, ...)
   ```

3. **긴 제목 처리**
   ```kotlin
   title = {
       Text(
           "매우 긴 제목이 있는 경우",
           maxLines = 1,
           overflow = TextOverflow.Ellipsis
       )
   }
   ```

4. **테마 색상 활용**
   ```kotlin
   colors = TopAppBarDefaults.topAppBarColors(
       containerColor = MaterialTheme.colorScheme.primaryContainer,
       titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
   )
   ```

---

## 연습 문제

### 연습 1: 기본 TopAppBar 만들기 (쉬움)
**목표**: TopAppBar의 기본 구조 익히기

**요구사항**:
- 제목: "My App"
- navigationIcon: 햄버거 메뉴 아이콘 (`Icons.Default.Menu`)
- 클릭 이벤트는 주석으로 TODO 표시

**힌트**: TopAppBar의 `title`, `navigationIcon` 파라미터 사용

---

### 연습 2: scrollBehavior 적용하기 (중간)
**목표**: 스크롤 시 축소되는 TopAppBar 만들기

**요구사항**:
- `MediumTopAppBar` 사용
- `enterAlwaysScrollBehavior` 적용
- `LazyColumn`과 함께 동작하도록 설정
- `Modifier.nestedScroll()` 연결

**힌트**:
1. `TopAppBarDefaults.enterAlwaysScrollBehavior()` 사용
2. Scaffold의 modifier에 `nestedScroll` 연결
3. `scrollBehavior`를 MediumTopAppBar에 전달

---

### 연습 3: 검색 + 메뉴 아이콘 TopAppBar (어려움)
**목표**: 실제 앱처럼 여러 액션 아이콘이 있는 TopAppBar 구현

**요구사항**:
- 제목: "연습문제"
- navigationIcon: 뒤로가기 (`Icons.AutoMirrored.Filled.ArrowBack`)
- actions에 2개 아이콘:
  - 검색 아이콘 (`Icons.Default.Search`) - 클릭 시 "검색" 스낵바 표시
  - 더보기 아이콘 (`Icons.Default.MoreVert`) - 클릭 시 "메뉴" 스낵바 표시
- `SnackbarHostState`와 연동

**힌트**:
1. `actions`는 `@Composable RowScope.() -> Unit` 타입
2. `rememberCoroutineScope()`로 스낵바 표시
3. `Icons.AutoMirrored.Filled.ArrowBack` 사용 (RTL 지원)

---

## 다음 학습

- [Scaffold와 테마](../../structure/scaffold_and_theming/README.md): TopAppBar를 Scaffold와 함께 활용하는 방법
- [Navigation](../../navigation/navigation_basics/README.md): TopAppBar와 Navigation 연동
- [NavigationBar](../navigation_bar/README.md): BottomAppBar와 함께 사용하는 하단 네비게이션
