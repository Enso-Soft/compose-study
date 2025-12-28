# NavigationBar 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `scaffold` | Scaffold의 기본 슬롯 구조와 paddingValues 사용법 | [📚 학습하기](../../structure/scaffold/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 핵심 용어 해설

| 용어 | 설명 |
|------|------|
| **NavigationBar** | 앱 하단에 위치하는 주요 탐색 UI (Material 3) |
| **NavigationBarItem** | NavigationBar 안의 개별 탐색 항목 (탭) |
| **Badge** | 알림 개수나 상태를 표시하는 작은 표시기 |
| **BadgedBox** | Badge를 다른 컴포넌트 위에 표시하는 컨테이너 |
| **Scaffold** | 앱의 기본 레이아웃 구조(TopBar, BottomBar 등)를 제공하는 컨테이너 |
| **Material 3** | 구글의 최신 디자인 시스템 (Material Design 3) |

---

## 개념

`NavigationBar`는 **앱 하단에 위치하는 주요 탐색 UI**입니다. 3~5개의 최상위 목적지(화면)를 표시하며, Material 3 디자인 가이드라인을 따릅니다.

> "NavigationBar는 '건물의 엘리베이터 버튼 패널'과 같습니다.
> - 각 층(화면)으로 이동하는 버튼이 있습니다
> - 현재 층(선택된 탭)은 불이 켜져 있습니다
> - 버튼을 누르면 해당 층으로 이동합니다"

```kotlin
NavigationBar {
    NavigationBarItem(
        selected = true,
        onClick = { /* 탭 클릭 시 동작 */ },
        icon = { Icon(Icons.Filled.Home, contentDescription = "홈") },
        label = { Text("홈") }
    )
    // 추가 탭들...
}
```

## 핵심 특징

1. **Material 3 디자인 준수**: 자동으로 적용되는 선택 효과, 애니메이션, 색상
2. **간편한 상태 관리**: `selected` 파라미터로 선택 상태를 쉽게 표시
3. **Badge 지원**: `BadgedBox`로 알림 개수를 간단하게 표시

---

## 문제 상황: Row + IconButton으로 직접 구현하기

### 시나리오

"하단 탐색바를 직접 만들어보자"라고 생각하고 Row + IconButton으로 구현을 시도합니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun ManualBottomNavigation() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 문제 1: 선택 상태 직접 관리해야 함
        IconButton(
            onClick = { selectedTab = 0 },
            modifier = Modifier.background(
                if (selectedTab == 0) Color.LightGray else Color.Transparent
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "홈",
                    // 문제 2: 색상도 직접 관리
                    tint = if (selectedTab == 0) Color.Blue else Color.Gray
                )
                Text(
                    "홈",
                    color = if (selectedTab == 0) Color.Blue else Color.Gray
                )
            }
        }
        // 다른 탭들도 반복...
    }
}
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 선택 효과 직접 구현 | 배경색, 아이콘 색상을 조건문으로 직접 관리해야 함 |
| 애니메이션 없음 | 탭 전환 시 부드러운 애니메이션이 없음 |
| Material 가이드라인 미준수 | 정확한 높이(80dp), 아이콘 크기(24dp), 리플 효과 등이 누락 |
| 접근성 미지원 | 스크린 리더를 위한 역할(Role) 설정이 없음 |
| 유지보수 어려움 | 탭이 추가될 때마다 코드 중복 |

---

## 해결책: NavigationBar 사용

### 올바른 코드

```kotlin
@Composable
fun MaterialBottomNavigation() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val items = listOf(
        NavigationItem("홈", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItem("검색", Icons.Filled.Search, Icons.Outlined.Search),
        NavigationItem("프로필", Icons.Filled.Person, Icons.Outlined.Person)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { selectedTab = index },
                icon = {
                    Icon(
                        if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}

data class NavigationItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
```

### 해결되는 이유

| 해결 | 설명 |
|------|------|
| 자동 선택 효과 | `selected = true`만 전달하면 색상, 배경이 자동 적용 |
| 부드러운 애니메이션 | Material 3 표준 애니메이션이 내장되어 있음 |
| 가이드라인 준수 | 높이, 간격, 리플 효과 등이 자동으로 올바르게 적용 |
| 접근성 지원 | 스크린 리더를 위한 역할이 자동 설정됨 |

---

## NavigationBarItem 핵심 파라미터

| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| `selected` | Boolean | O | 이 탭이 현재 선택되어 있는지 |
| `onClick` | () -> Unit | O | 탭 클릭 시 실행할 동작 |
| `icon` | @Composable () -> Unit | O | 표시할 아이콘 |
| `label` | @Composable (() -> Unit)? | X | 아이콘 아래 표시할 텍스트 |
| `enabled` | Boolean | X | 탭 활성화 여부 (기본: true) |
| `alwaysShowLabel` | Boolean | X | 항상 레이블 표시 여부 (기본: true) |
| `colors` | NavigationBarItemColors | X | 색상 커스터마이징 |

### 탭 개수별 권장 사항 (Material Design)

| 탭 개수 | 레이블 표시 |
|--------|------------|
| 3개 | 모든 탭에 아이콘 + 레이블 |
| 4개 | 활성 탭만 레이블, 나머지는 아이콘만 |
| 5개 | 활성 탭만 레이블 |

---

## Badge 사용하기

"Badge는 '우편함의 새 편지 알림'과 같습니다.
- 숫자가 있으면 몇 개의 새 편지가 있는지 알려줍니다
- 빨간 점만 있으면 '확인할 것이 있다'는 표시입니다"

### 숫자 Badge

```kotlin
NavigationBarItem(
    selected = selectedTab == 1,
    onClick = { selectedTab = 1 },
    icon = {
        BadgedBox(
            badge = { Badge { Text("5") } }  // 숫자 표시
        ) {
            Icon(Icons.Filled.Notifications, contentDescription = "알림")
        }
    },
    label = { Text("알림") }
)
```

### 점 Badge (숫자 없이)

```kotlin
BadgedBox(
    badge = { Badge() }  // 빨간 점만 표시
) {
    Icon(Icons.Filled.Email, contentDescription = "메일")
}
```

### 99+ 표시

```kotlin
val notificationCount = 150

BadgedBox(
    badge = {
        Badge {
            Text(if (notificationCount > 99) "99+" else "$notificationCount")
        }
    }
) {
    Icon(Icons.Filled.Notifications, contentDescription = "알림")
}
```

---

## Scaffold와 함께 사용하기

실제 앱에서는 `Scaffold`의 `bottomBar`에 NavigationBar를 배치합니다.

```kotlin
@Composable
fun AppWithNavigationBar() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "홈") },
                    label = { Text("홈") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.Search, contentDescription = "검색") },
                    label = { Text("검색") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "프로필") },
                    label = { Text("프로필") }
                )
            }
        }
    ) { innerPadding ->
        // innerPadding을 적용해야 NavigationBar와 콘텐츠가 겹치지 않습니다
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> ProfileScreen()
            }
        }
    }
}
```

**주의**: `innerPadding`을 콘텐츠에 적용하지 않으면 NavigationBar와 콘텐츠가 겹칩니다!

---

## 사용 시나리오

### 1. 3~5개의 주요 화면이 있는 앱
```kotlin
// SNS 앱: 홈, 검색, 생성, 알림, 프로필
// 쇼핑 앱: 홈, 카테고리, 장바구니, 마이페이지
// 음악 앱: 홈, 검색, 라이브러리
```

### 2. 모든 화면에서 동일한 탐색이 필요할 때
```kotlin
// 어느 화면에서든 다른 주요 화면으로 바로 이동 가능
```

### 3. 탭별로 독립적인 백스택이 필요할 때
```kotlin
// 각 탭은 자신만의 화면 이력을 유지
```

---

## 주의사항

### 1. 3~5개 탭 유지
```kotlin
// O 적절한 탭 개수
NavigationBar {
    NavigationBarItem(...) // 홈
    NavigationBarItem(...) // 검색
    NavigationBarItem(...) // 프로필
}

// X 너무 많은 탭 (6개 이상)
// NavigationRail이나 NavigationDrawer 고려
```

### 2. innerPadding 적용 필수
```kotlin
Scaffold(
    bottomBar = { NavigationBar { ... } }
) { innerPadding ->
    // O innerPadding 적용
    Box(modifier = Modifier.padding(innerPadding)) {
        Content()
    }

    // X innerPadding 무시 - 콘텐츠가 NavigationBar에 가려짐
    // Content()
}
```

### 3. 선택 상태와 onClick 동기화
```kotlin
// O 올바른 패턴
NavigationBarItem(
    selected = selectedTab == 0,     // 상태 확인
    onClick = { selectedTab = 0 },   // 상태 변경
    ...
)

// X 잘못된 패턴 - selected가 항상 false
NavigationBarItem(
    selected = false,
    onClick = { selectedTab = 0 },
    ...
)
```

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | Row + IconButton으로 직접 구현한 문제 상황 |
| `Solution.kt` | NavigationBar로 올바르게 구현한 해결책 |
| `Practice.kt` | 연습 문제 3개 (기본, Badge, Scaffold 통합) |

---

## 연습 문제

| 난이도 | 문제 | 설명 |
|--------|------|------|
| 쉬움 | **기본 Navigation Bar** | 홈/검색/프로필 3탭 구현 |
| 중간 | **Badge가 있는 Navigation Bar** | 숫자 Badge, 점 Badge 추가 |
| 어려움 | **Scaffold 통합 + 화면 전환** | 5탭 + Badge + 화면 전환 |

---

## 다음 학습

- `Navigation`: Jetpack Navigation과 연동하여 실제 화면 전환 구현
- `NavigationRail`: 태블릿/대화면을 위한 세로 탐색 바
- `NavigationDrawer`: 더 많은 메뉴가 필요할 때 사용하는 서랍형 메뉴
