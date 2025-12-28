# NavigationRail 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `navigation` | Jetpack Navigation Compose 기본 개념과 NavHost 사용법 | [📚 학습하기](../../navigation/navigation_basics/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**NavigationRail**은 화면 측면에 세로로 배치되는 네비게이션 컴포넌트입니다.
태블릿, 폴더블, 데스크톱 등 대형 화면에서 NavigationBar 대신 사용합니다.

> 비유: NavigationRail은 책장의 책등 라벨과 같습니다.
> 책을 세로로 꽂아두면 옆에서 라벨을 보며 원하는 책을 찾듯이,
> 화면 옆에서 메뉴를 선택합니다.

## 핵심 특징

1. **세로형 네비게이션**: 화면 좌측에 세로로 배치
2. **대형 화면 최적화**: 태블릿을 양손으로 잡았을 때 손가락으로 쉽게 접근
3. **header 슬롯**: 상단에 FAB나 로고를 배치할 수 있는 공간 제공
4. **3-7개 목적지**: NavigationBar(3-5개)보다 더 많은 항목 지원

---

## 문제 상황: 태블릿에서 NavigationBar의 한계

### 시나리오

음악 앱을 태블릿에서 실행합니다.
하단에 NavigationBar가 있는데, 뭔가 어색합니다.

### 발생하는 문제점

1. **손이 닿기 어려움**
   - 태블릿을 양손으로 잡으면 엄지손가락이 화면 옆에 위치
   - 하단까지 손을 뻗어야 하므로 불편

2. **공간 낭비**
   - 넓은 태블릿 화면에서 하단 바만 사용
   - 양쪽 측면 공간이 비어있음

3. **콘텐츠 영역 감소**
   - 하단에 네비게이션이 있으면 세로 공간 감소
   - 더 많은 콘텐츠를 표시할 수 있는 기회 손실

---

## 해결책: NavigationRail 사용

### 기본 사용법

```kotlin
Row(modifier = Modifier.fillMaxSize()) {
    NavigationRail {
        NavigationRailItem(
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0 },
            icon = { Icon(Icons.Default.Home, "홈") },
            label = { Text("홈") }
        )
        NavigationRailItem(
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 },
            icon = { Icon(Icons.Default.Search, "검색") },
            label = { Text("검색") }
        )
        // 더 많은 항목...
    }

    // 콘텐츠 영역
    ContentScreen(selectedIndex)
}
```

### header 슬롯 활용 (FAB 배치)

```kotlin
NavigationRail(
    header = {
        FloatingActionButton(
            onClick = { /* 액션 */ },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.Add, "추가")
        }
    }
) {
    // NavigationRailItems...
}
```

---

## API 상세

### NavigationRail 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| modifier | Modifier | 수정자 |
| containerColor | Color | 배경색 |
| contentColor | Color | 콘텐츠 색상 |
| **header** | @Composable? | FAB/로고 배치 공간 |
| windowInsets | WindowInsets | 윈도우 인셋 |
| content | @Composable | NavigationRailItem들 |

### NavigationRailItem 파라미터

| 파라미터 | 타입 | 필수 | 설명 |
|---------|------|------|------|
| **selected** | Boolean | O | 선택 상태 |
| **onClick** | () -> Unit | O | 클릭 이벤트 |
| **icon** | @Composable | O | 아이콘 |
| label | @Composable? | X | 텍스트 레이블 |
| alwaysShowLabel | Boolean | X | 항상 레이블 표시 (기본: true) |
| enabled | Boolean | X | 활성화 여부 |
| colors | NavigationRailItemColors | X | 색상 설정 |

---

## NavigationBar vs NavigationRail 비교

| 기준 | NavigationBar | NavigationRail |
|------|---------------|----------------|
| **방향** | 가로 (하단) | 세로 (측면) |
| **위치** | 화면 하단 | 화면 좌측 |
| **대상 화면** | 휴대폰 (Compact) | 태블릿/데스크톱 (Medium/Expanded) |
| **최적 항목 수** | 3-5개 | 3-7개 |
| **FAB 포함** | 불가 | header 슬롯으로 가능 |
| **접근성** | 엄지손가락 (하단) | 손가락 (측면) |

---

## 화면 크기별 선택 가이드

### 선택 플로우차트

```
화면 크기 확인
  |
  +-- Compact (< 600dp)
  |     └── NavigationBar 사용
  |
  +-- Medium (600-840dp)
  |     └── NavigationRail 사용
  |
  +-- Expanded (> 840dp)
        └── NavigationRail 사용
            (+ NavigationDrawer 추가 가능)
```

### 적응형 네비게이션 예시

```kotlin
BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    if (maxWidth < 600.dp) {
        // Compact: 하단 네비게이션
        Scaffold(
            bottomBar = { NavigationBar { /* items */ } }
        ) { /* content */ }
    } else {
        // Medium/Expanded: 측면 네비게이션
        Row {
            NavigationRail { /* items */ }
            Content()
        }
    }
}
```

---

## 다양한 활용

### 1. 로고 배치

```kotlin
NavigationRail(
    header = {
        Icon(
            Icons.Default.MusicNote,
            contentDescription = "로고",
            modifier = Modifier.size(40.dp)
        )
    }
) { /* items */ }
```

### 2. 커스텀 색상

```kotlin
NavigationRail(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
) { /* items */ }
```

### 3. 선택 상태에 따른 아이콘 변경

```kotlin
NavigationRailItem(
    selected = isSelected,
    icon = {
        Icon(
            if (isSelected) Icons.Filled.Home else Icons.Outlined.Home,
            contentDescription = "홈"
        )
    },
    // ...
)
```

---

## 주의사항

1. **Row 레이아웃 필수**: NavigationRail은 콘텐츠와 함께 Row 안에 배치해야 함
2. **상태 관리**: 선택 상태를 직접 관리해야 함 (remember/rememberSaveable)
3. **접근성**: contentDescription을 꼭 제공하세요
4. **항목 수 제한**: 7개 이상은 NavigationDrawer 고려

---

## 연습 문제

### 연습 1: 기본 NavigationRail 만들기 - 쉬움

3개의 목적지(홈, 검색, 프로필)가 있는 기본 NavigationRail을 구현하세요.

**목표**:
- NavigationRail의 기본 구조 이해
- NavigationRailItem의 필수 파라미터 사용
- 선택 상태 관리

### 연습 2: FAB가 있는 NavigationRail - 중간

header 슬롯에 FloatingActionButton이 있고, 클릭 시 Snackbar를 표시하는 NavigationRail을 구현하세요.

**목표**:
- header 슬롯 활용법 학습
- FAB와 NavigationRail 조합
- Snackbar 연동

### 연습 3: 적응형 네비게이션 - 어려움

화면 크기에 따라 NavigationBar 또는 NavigationRail을 자동으로 전환하는 적응형 네비게이션을 구현하세요.

**목표**:
- BoxWithConstraints 활용
- 조건부 렌더링
- 상태 공유

---

## 다음 학습

- **NavigationDrawer**: 더 많은 목적지가 필요할 때
- **NavigationSuiteScaffold**: 자동으로 네비게이션 전환
- **WindowSizeClass**: 화면 크기 분류 API
- **Adaptive Layout**: 대형 화면 지원 전체 가이드
