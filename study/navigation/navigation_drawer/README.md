# Navigation Drawer 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `navigation` | Jetpack Navigation Compose 기본 개념과 NavHost 사용법 | [📚 학습하기](../../navigation/navigation_basics/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Navigation Drawer는 화면 측면에서 슬라이드되어 나오는 탐색 메뉴 패널입니다.
마치 "서랍(Drawer)"처럼 필요할 때 꺼내서 사용하고, 다 쓰면 다시 닫을 수 있습니다.

## 핵심 특징

1. **ModalNavigationDrawer**: 콘텐츠 위에 오버레이로 나타남 (모바일용, 가장 많이 사용)
2. **PermanentNavigationDrawer**: 항상 화면에 고정됨 (태블릿/데스크톱용)
3. **DismissibleNavigationDrawer**: 닫을 수 있지만 콘텐츠 옆에 표시됨 (중간 크기 화면용)
4. **DrawerState**: 프로그래밍 방식으로 열기/닫기 제어 가능

---

## 문제 상황: Navigation Drawer를 직접 구현하려 할 때

### 시나리오

앱에 사이드 메뉴가 필요해서 Box + AnimatedVisibility로 직접 만들어보려 합니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun ManualDrawerAttempt() {
    var isOpen by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        // 메인 콘텐츠
        MainContent(onMenuClick = { isOpen = true })

        // 스크림(반투명 배경) - 직접 구현해야 함
        AnimatedVisibility(visible = isOpen) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { isOpen = false }
            )
        }

        // Drawer - 직접 구현해야 함
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it }
        ) {
            Column(
                Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // 메뉴 항목들...
            }
        }
    }
}
```

### 발생하는 문제점

1. **스와이프 제스처 미지원**: 손가락으로 끌어서 열기/닫기가 안 됨
2. **스크림 처리 복잡**: 반투명 배경 클릭 시 이벤트 처리가 까다로움
3. **애니메이션 타이밍 불일치**: 스크림 fade와 Drawer slide 동기화가 어려움
4. **접근성 미지원**: 스크린 리더(TalkBack) 사용자를 위한 처리 없음
5. **Predictive back gesture 미지원**: Android 13+ 뒤로가기 제스처 미리보기 불가
6. **상태 관리 복잡**: 여러 곳에서 열기/닫기 제어 시 코드 중복

---

## 해결책: ModalNavigationDrawer 사용

### 기본 구조

```kotlin
ModalNavigationDrawer(
    drawerContent = {
        // Drawer 안에 들어갈 내용
        ModalDrawerSheet {
            // 메뉴 항목들
        }
    }
) {
    // 메인 화면 콘텐츠
}
```

### 올바른 코드

```kotlin
@Composable
fun NavigationDrawerExample() {
    // 1. DrawerState 생성 - Drawer의 열림/닫힘 상태를 관리
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // 2. 코루틴 스코프 - open()/close()는 suspend 함수라서 필요
    val scope = rememberCoroutineScope()

    // 3. 현재 선택된 메뉴 인덱스
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("홈", "설정", "프로필")

    ModalNavigationDrawer(
        drawerState = drawerState,  // 상태 연결
        drawerContent = {
            ModalDrawerSheet {  // Material 3 스타일 적용
                Text("메뉴", modifier = Modifier.padding(16.dp))
                HorizontalDivider()

                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(item) },
                        selected = selectedItem == index,  // 선택 상태 표시
                        onClick = {
                            selectedItem = index
                            scope.launch { drawerState.close() }  // 메뉴 선택 후 닫기
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("앱 제목") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }  // 햄버거 메뉴로 열기
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
                        }
                    }
                )
            }
        ) { padding ->
            // 메인 콘텐츠
        }
    }
}
```

### 해결되는 이유

ModalNavigationDrawer는 Material 3에서 제공하는 완전한 구현체로:

- **스와이프 제스처**: 화면 왼쪽 가장자리에서 스와이프하면 Drawer가 열림
- **스크림 자동 처리**: 반투명 배경이 자동으로 나타나고, 클릭하면 닫힘
- **부드러운 애니메이션**: 스크림과 Drawer 애니메이션이 완벽히 동기화
- **접근성 지원**: TalkBack 사용자를 위한 처리가 내장됨
- **Predictive back gesture**: Android 13+에서 뒤로가기 제스처 시 미리보기 지원
- **DrawerState로 깔끔한 상태 관리**: 어디서든 drawerState.open()/close() 호출

---

## 주요 컴포넌트 설명

### 1. ModalNavigationDrawer

```kotlin
ModalNavigationDrawer(
    drawerState = drawerState,           // 상태 관리 (필수)
    drawerContent = { ... },              // Drawer 내용 (필수)
    gesturesEnabled = true,               // 스와이프 제스처 허용 여부
    scrimColor = DrawerDefaults.scrimColor, // 스크림 색상
    content = { ... }                     // 메인 화면 (필수)
)
```

### 2. DrawerState

```kotlin
// 생성
val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

// 열기/닫기 (suspend 함수 - 코루틴 필요)
scope.launch { drawerState.open() }
scope.launch { drawerState.close() }

// 상태 확인
if (drawerState.isClosed) { ... }
```

### 3. NavigationDrawerItem

```kotlin
NavigationDrawerItem(
    label = { Text("메뉴 이름") },    // 텍스트 (필수)
    selected = isSelected,            // 선택 상태 (필수)
    onClick = { ... },                // 클릭 핸들러 (필수)
    icon = { Icon(...) },             // 아이콘 (선택)
    badge = { Text("5") }             // 배지 (선택) - 알림 개수 등
)
```

### 4. gesturesEnabled

```kotlin
// 스와이프 제스처 비활성화 (버튼으로만 열기/닫기)
ModalNavigationDrawer(
    drawerState = drawerState,
    gesturesEnabled = false,  // 스와이프 안 됨
    drawerContent = { ... }
) { ... }
```

---

## 사용 시나리오

1. **앱의 주요 섹션 간 이동**: 홈, 대시보드, 설정 등
2. **계정 관리 메뉴**: 프로필, 알림 설정, 로그아웃
3. **필터/카테고리 선택**: 쇼핑앱의 카테고리, 뉴스앱의 섹션
4. **복잡한 앱의 기능 정리**: 기능이 많은 앱에서 메뉴 조직화

---

## 화면 크기별 Drawer 선택

| 화면 크기 | 추천 Drawer | 특징 |
|----------|------------|------|
| 스마트폰 | ModalNavigationDrawer | 오버레이, 스와이프로 열기 |
| 폴더블/태블릿 | DismissibleNavigationDrawer | 옆에 표시, 닫기 가능 |
| 데스크톱 | PermanentNavigationDrawer | 항상 표시, 닫기 불가 |

---

## 주의사항

- **open()/close()는 suspend 함수**: 반드시 `scope.launch { }` 안에서 호출
- **ModalDrawerSheet 사용 권장**: Material 3 디자인 가이드라인 준수
- **gesturesEnabled=false 주의**: 스와이프를 막으면 사용자 경험에 영향
- **접근성 고려**: contentDescription 설정 잊지 말기

---

## 연습 문제

### 연습 1: 기본 Navigation Drawer 만들기 (쉬움)

"홈", "검색", "알림" 3개 메뉴가 있는 기본 Drawer를 만들어보세요.

**요구사항**:
- ModalNavigationDrawer 사용
- NavigationDrawerItem 3개 추가
- 햄버거 메뉴 버튼으로 Drawer 열기

### 연습 2: 아이콘과 선택 상태 관리 (중간)

각 메뉴에 아이콘을 추가하고, 선택된 메뉴를 하이라이트하세요.

**요구사항**:
- 각 메뉴에 적절한 아이콘 추가
- 현재 선택된 메뉴 하이라이트
- 메뉴 선택 시 Drawer 자동 닫기
- 메인 화면에 선택된 메뉴 이름 표시

### 연습 3: 완성형 Navigation Drawer (어려움)

실제 앱 수준의 완성도 높은 Drawer를 구현하세요.

**요구사항**:
1. 상단에 사용자 프로필 영역 (아이콘, 이름, 이메일)
2. "메인 메뉴" 섹션: 홈, 대시보드, 알림 (badge로 알림 개수 표시)
3. "설정" 섹션: 설정, 도움말, 로그아웃
4. 섹션 구분선(HorizontalDivider) 사용
5. gesturesEnabled를 토글하는 스위치 추가

---

## 다음 학습

- **Navigation Bar**: 하단 탐색 바 (5개 이하 주요 화면)
- **Navigation Rail**: 태블릿용 측면 탐색 레일
- **Scaffold**: 앱 화면 전체 레이아웃 구조
