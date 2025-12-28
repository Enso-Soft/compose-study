# Scaffold 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `layout_and_modifier` | Row, Column, Box 레이아웃과 Modifier 기본 사용법 | [📚 학습하기](../../layout/layout_and_modifier/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Scaffold**는 Material Design 화면의 기본 뼈대(골격)를 제공하는 컨테이너입니다.

> 비유: 건물을 지을 때 먼저 뼈대(Scaffold)를 세우면 벽, 지붕, 창문이 어디에 들어갈지 명확해집니다.
> Compose의 Scaffold도 마찬가지로, TopBar, FAB, 콘텐츠가 들어갈 자리를 미리 정해줍니다.

```kotlin
Scaffold(
    topBar = { /* 상단 앱바 */ },
    bottomBar = { /* 하단 바 */ },
    floatingActionButton = { /* FAB */ },
    snackbarHost = { /* Snackbar 표시 영역 */ }
) { paddingValues ->
    // 메인 콘텐츠 (paddingValues 필수 적용!)
}
```

## 핵심 특징

1. **슬롯 기반 API**: 각 UI 요소가 들어갈 "자리(슬롯)"가 정해져 있음
2. **자동 레이아웃 관리**: TopBar, FAB, Snackbar의 위치와 간격을 자동 계산
3. **paddingValues 제공**: 콘텐츠가 다른 요소와 겹치지 않도록 여백 정보 전달

---

## Scaffold 슬롯 구조

| 슬롯 | 역할 | 예시 컴포넌트 |
|------|------|--------------|
| `topBar` | 화면 상단 앱바 | TopAppBar, CenterAlignedTopAppBar |
| `bottomBar` | 화면 하단 바 | BottomAppBar, NavigationBar |
| `floatingActionButton` | 플로팅 액션 버튼 | FloatingActionButton |
| `snackbarHost` | Snackbar 표시 영역 | SnackbarHost |
| `content` | 메인 콘텐츠 영역 | 화면의 주요 내용 |

### floatingActionButtonPosition 옵션

| 값 | 위치 |
|----|------|
| `FabPosition.End` | 오른쪽 하단 (기본값) |
| `FabPosition.Center` | 가운데 하단 |

---

## 문제 상황: Scaffold 없이 직접 배치

### 시나리오

간단한 메모 앱 화면을 만든다고 가정해봅시다:
- 상단에 "메모" 제목의 앱바
- 중앙에 메모 목록
- 우하단에 새 메모 추가 FAB

### 잘못된 코드 예시 (Box로 직접 배치)

```kotlin
@Composable
fun BrokenMemoScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // 상단 앱바 역할
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.TopCenter),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text("메모", modifier = Modifier.padding(16.dp))
        }

        // 콘텐츠 - 여백 없이 배치하면 앱바 뒤로 들어감!
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(memoList) { memo ->
                MemoCard(memo)
            }
        }

        // FAB - 위치 직접 계산 필요
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { }
        ) {
            Icon(Icons.Default.Add, "추가")
        }
    }
}
```

### 발생하는 문제점

1. **콘텐츠 겹침**: LazyColumn이 상단 Surface 뒤로 들어가 첫 번째 아이템이 가려짐
2. **수동 여백 계산**: 앱바 높이(64dp)를 직접 계산해서 콘텐츠에 padding 적용 필요
3. **시스템 바 미처리**: 상태바, 네비게이션 바와의 간격을 직접 관리해야 함
4. **Snackbar 충돌**: Snackbar 표시 시 FAB와 겹침 문제 발생

---

## 해결책: Scaffold 사용

### 올바른 코드

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("메모") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* 새 메모 */ }) {
                Icon(Icons.Default.Add, "추가")
            }
        }
    ) { paddingValues ->
        // paddingValues 적용으로 자동 여백!
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)  // 핵심!
        ) {
            items(memoList) { memo ->
                MemoCard(memo)
            }
        }
    }
}
```

### 해결되는 이유

1. **topBar 슬롯**: TopAppBar가 화면 최상단에 자동 배치
2. **paddingValues**: 콘텐츠가 TopBar 아래에서 시작하도록 여백 자동 계산
3. **FAB 슬롯**: FAB가 시스템 바를 고려하여 적절한 위치에 배치
4. **Snackbar 조화**: snackbarHost 사용 시 Snackbar가 FAB 위로 자동 이동

---

## 사용 시나리오

### 1. 기본 화면 구조

```kotlin
Scaffold(
    topBar = { TopAppBar(title = { Text("앱 이름") }) }
) { padding ->
    Content(modifier = Modifier.padding(padding))
}
```

### 2. FAB가 있는 목록 화면

```kotlin
Scaffold(
    topBar = { TopAppBar(title = { Text("목록") }) },
    floatingActionButton = {
        FloatingActionButton(onClick = { }) {
            Icon(Icons.Default.Add, "추가")
        }
    }
) { padding ->
    LazyColumn(modifier = Modifier.padding(padding)) {
        // 목록 아이템
    }
}
```

### 3. Snackbar 표시

```kotlin
val snackbarHostState = remember { SnackbarHostState() }
val scope = rememberCoroutineScope()

Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    floatingActionButton = {
        FloatingActionButton(onClick = {
            scope.launch {
                snackbarHostState.showSnackbar("메시지")
            }
        }) {
            Icon(Icons.Default.Notifications, "알림")
        }
    }
) { padding ->
    Content(modifier = Modifier.padding(padding))
}
```

---

## 주의사항

### 1. paddingValues 필수 적용!

```kotlin
// 잘못된 예 - paddingValues 무시
Scaffold(topBar = { TopAppBar(...) }) { _ ->  // 무시!
    Column {  // TopBar 뒤로 콘텐츠가 들어감
        Text("가려지는 텍스트")
    }
}

// 올바른 예
Scaffold(topBar = { TopAppBar(...) }) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues)) {
        Text("정상적으로 표시됨")
    }
}
```

### 2. @OptIn(ExperimentalMaterial3Api::class)

TopAppBar 사용 시 `@OptIn(ExperimentalMaterial3Api::class)` 어노테이션이 필요합니다.

### 3. FabPosition 선택

```kotlin
Scaffold(
    floatingActionButton = { FAB() },
    floatingActionButtonPosition = FabPosition.Center  // 가운데 배치
) { ... }
```

---

## 연습 문제

### 연습 1: 기본 Scaffold 구현 (쉬움)

TopAppBar와 FAB가 있는 기본 Scaffold를 구현하세요.

**요구사항:**
- TopAppBar에 "연습 1" 타이틀 표시
- FAB에 '+' 아이콘 표시
- content에 "Hello Scaffold!" 텍스트 중앙 배치

### 연습 2: Snackbar 통합 (중간)

FAB 클릭 시 Snackbar를 표시하는 Scaffold를 구현하세요.

**요구사항:**
- SnackbarHostState 생성
- FAB 클릭 시 "메모가 추가되었습니다" 메시지 표시
- Snackbar가 FAB와 겹치지 않는지 확인

### 연습 3: 전체 슬롯 활용 (어려움)

모든 슬롯을 활용한 완전한 화면 구조를 구현하세요.

**요구사항:**
- TopAppBar: 제목 + 설정 아이콘 버튼
- BottomBar: 3개 탭 네비게이션
- FAB: 가운데 하단 배치
- SnackbarHost: FAB 클릭 시 메시지 표시
- Content: 선택된 탭에 따른 콘텐츠

---

## 다음 학습

- **scaffold_and_theming**: MaterialTheme과 Scaffold 조합
- **navigation**: Navigation과 Scaffold 통합
- **bottom_sheet**: BottomSheet 슬롯 활용

---

## 참고 자료

- [Scaffold - Android Developers](https://developer.android.com/develop/ui/compose/components/scaffold)
- [App bars - Material Design 3](https://m3.material.io/components/top-app-bar)
