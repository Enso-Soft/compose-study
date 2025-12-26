# PullToRefresh 학습

## 개념

`PullToRefreshBox`는 Material3에서 제공하는 **당겨서 새로고침** UI 패턴 컴포넌트입니다. 사용자가 스크롤 가능한 콘텐츠의 상단에서 아래로 드래그하면 새로고침을 트리거합니다.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshableList() {
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            // 새로고침 로직
        }
    ) {
        LazyColumn {
            // 스크롤 가능한 콘텐츠
        }
    }
}
```

## 핵심 특징

### 1. ExperimentalMaterial3Api
```kotlin
@OptIn(ExperimentalMaterial3Api::class)  // 필수!
```
- 아직 실험적 API로 분류됨
- 향후 API 변경 가능성 있음

### 2. 사용자 제어 상태
```kotlin
// isRefreshing은 사용자가 직접 관리
var isRefreshing by remember { mutableStateOf(false) }

PullToRefreshBox(
    isRefreshing = isRefreshing,  // 현재 새로고침 중인지
    onRefresh = {
        isRefreshing = true       // 직접 true로 설정
        // 데이터 로드...
        isRefreshing = false      // 직접 false로 설정
    }
)
```

### 3. 기본 인디케이터
- `CircularProgressIndicator` 스타일
- Material3 디자인 가이드라인 준수
- 드래그 거리에 따른 스케일 애니메이션

## 문제 상황: 새로고침 버튼만 있는 화면

### 잘못된 코드 예시
```kotlin
@Composable
fun BadRefreshScreen() {
    Column {
        // 별도의 새로고침 버튼 필요
        Button(onClick = { loadData() }) {
            Text("새로고침")
        }

        LazyColumn {
            // 아래로 당겨도 아무 반응 없음!
        }
    }
}
```

### 발생하는 문제점
1. **UX 문제**: 사용자가 버튼을 찾아야 함
2. **표준 위반**: 모바일 앱의 표준 패턴 미준수
3. **공간 낭비**: 버튼이 화면 공간 차지
4. **접근성**: 제스처 기반 사용자에게 불편

## 해결책: PullToRefreshBox 사용

### 올바른 코드
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodRefreshScreen() {
    var items by remember { mutableStateOf(listOf<String>()) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                items = fetchNewData()  // 비동기 데이터 로드
                isRefreshing = false
            }
        }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(items) { item ->
                ListItem({ Text(item) })
            }
        }
    }
}
```

### 해결되는 이유
1. **직관적 UX**: 아래로 당기면 새로고침
2. **표준 패턴**: iOS/Android 모두에서 익숙한 UX
3. **자동 애니메이션**: 인디케이터 표시/숨김 자동 처리
4. **Nested Scroll 처리**: 스크롤과 새로고침 제스처 충돌 없음

## API 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `isRefreshing` | `Boolean` | 현재 새로고침 중인지 (필수) |
| `onRefresh` | `() -> Unit` | 새로고침 트리거 콜백 (필수) |
| `modifier` | `Modifier` | 레이아웃 수정자 |
| `state` | `PullToRefreshState` | 드래그 상태 관리 |
| `indicator` | `@Composable` | 커스텀 인디케이터 |

## 커스텀 인디케이터

### 색상 커스터마이징
```kotlin
val state = rememberPullToRefreshState()

PullToRefreshBox(
    isRefreshing = isRefreshing,
    onRefresh = { /* ... */ },
    state = state,
    indicator = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state,
            containerColor = Color.Blue,  // 배경색
            color = Color.White           // 인디케이터 색
        )
    }
)
```

### 완전 커스텀 인디케이터
```kotlin
indicator = {
    val progress = state.distanceFraction.coerceIn(0f, 1f)

    Box(modifier = Modifier.align(Alignment.TopCenter)) {
        if (isRefreshing) {
            CircularProgressIndicator()
        } else {
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.graphicsLayer {
                    scaleX = progress
                    scaleY = progress
                    alpha = progress
                }
            )
        }
    }
}
```

### distanceFraction 이해
- `0f`: 드래그 시작 전
- `0f ~ 1f`: 드래그 진행 중
- `1f`: 새로고침 threshold 도달

## 사용 시나리오

### 1. 뉴스/피드 앱
```kotlin
PullToRefreshBox(
    isRefreshing = uiState.isLoading,
    onRefresh = viewModel::loadLatestNews
) {
    NewsFeed(articles = uiState.articles)
}
```

### 2. 이메일 앱
```kotlin
PullToRefreshBox(
    isRefreshing = isRefreshing,
    onRefresh = { syncEmails() }
) {
    EmailList(emails)
}
```

### 3. 소셜 미디어
```kotlin
PullToRefreshBox(
    isRefreshing = isRefreshing,
    onRefresh = { refreshTimeline() }
) {
    Timeline(posts)
}
```

## 주의사항

### 1. ExperimentalMaterial3Api OptIn 필수
```kotlin
@OptIn(ExperimentalMaterial3Api::class)  // 이거 빠뜨리면 컴파일 에러!
@Composable
fun MyScreen() { ... }
```

### 2. isRefreshing 상태 관리
```kotlin
// ❌ 잘못된 예: 상태 업데이트 안 함
onRefresh = {
    loadData()  // isRefreshing 변경 없음 → 인디케이터 안 보임
}

// ✅ 올바른 예: 명시적 상태 관리
onRefresh = {
    isRefreshing = true
    loadData()
    isRefreshing = false
}
```

### 3. 코루틴 사용 시
```kotlin
val scope = rememberCoroutineScope()

onRefresh = {
    scope.launch {
        isRefreshing = true
        delay(1000)  // 네트워크 요청 시뮬레이션
        // 데이터 로드...
        isRefreshing = false
    }
}
```

### 4. 스크롤 가능한 콘텐츠 필요
```kotlin
// ❌ 스크롤 불가 콘텐츠
PullToRefreshBox(...) {
    Column { ... }  // 동작하지만 의미 없음
}

// ✅ 스크롤 가능한 콘텐츠
PullToRefreshBox(...) {
    LazyColumn { ... }  // 권장
}
```

## API 변화 히스토리

| 버전 | 변경사항 |
|------|---------|
| Material3 1.3.0 | `PullToRefreshBox` 공식 도입 |
| Material3 1.4.0 | `indicatorShape`, `indicatorContainerColor` 파라미터 추가 |
| Material3 1.5.0 | `indicatorMaxDistance` 추가 |

## 연습 문제

### 연습 1: 기본 PullToRefreshBox (초급)
숫자 목록을 표시하고, 당겨서 새로고침 시 랜덤 숫자를 추가하세요.

### 연습 2: 인디케이터 색상 변경 (중급)
`rememberPullToRefreshState()`를 사용해 인디케이터 색상을 앱 테마에 맞게 변경하세요.

### 연습 3: 커스텀 인디케이터 (고급)
`distanceFraction`을 활용해 드래그 거리에 따라 아이콘 크기가 변하고, 새로고침 중에는 회전하는 커스텀 인디케이터를 만드세요.

## 다음 학습

- **snapshotFlow**: Compose State를 Flow로 변환
- **produceState**: Flow를 Compose State로 변환
- **Bottom Sheet**: 하단에서 올라오는 시트 UI
