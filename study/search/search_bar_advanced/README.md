# Material3 SearchBar 심화 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `search_bar` | Material3 SearchBar의 기본 사용법과 확장/축소 상태 관리 | [📚 학습하기](../../search/search_bar/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`SearchBar`는 Material Design 3에서 제공하는 **검색 기능 전용 컴포넌트**입니다.
사용자가 검색어를 입력하고, 검색 기록/제안을 확인하며, 검색 결과를 탐색할 수 있는
완전한 검색 경험을 제공합니다.

> **핵심 메시지**: TextField로 검색 UI를 직접 구현하면 보일러플레이트 코드가 증가하고,
> Material Design 가이드라인 준수가 어렵습니다. SearchBar를 사용하면 이 모든 문제가 해결됩니다.

## 핵심 특징

### 1. 내장 애니메이션
- 확장/축소 시 부드러운 전환 애니메이션 자동 적용
- leading icon 이동, 입력 필드 확장 등 자동 처리

### 2. 상태 관리
```kotlin
// 필수 상태
var query by remember { mutableStateOf("") }  // 검색어
var expanded by rememberSaveable { mutableStateOf(false) }  // 확장 상태

// rememberSaveable로 화면 회전 시에도 확장 상태 유지
```

### 3. 접근성 기본 지원
- 시맨틱 정보 자동 제공
- 스크린 리더 호환
- 키보드 네비게이션 지원

---

## 문제 상황: TextField로 직접 구현

### 시나리오
앱에 검색 기능을 추가해야 합니다. TextField와 LazyColumn을 조합해서 검색 UI를 만들면 될 것 같습니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun ManualSearchUI() {
    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf(listOf<String>()) }

    Column {
        // 1. 기본 TextField - 스타일링 직접 처리 필요
        TextField(
            value = query,
            onValueChange = {
                query = it
                isExpanded = true
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 2. 제안 목록 - 위치, 크기, 그림자 직접 처리
        if (isExpanded && suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp)
            ) {
                LazyColumn {
                    items(suggestions) { suggestion ->
                        Text(
                            text = suggestion,
                            modifier = Modifier
                                .clickable { query = suggestion }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
```

### 발생하는 문제점

1. **보일러플레이트 코드**: 확장/축소, 애니메이션, 포커스 관리 직접 구현 (약 100줄 이상)
2. **Material Design 미준수**: 가이드라인에 맞는 스타일링 어려움
3. **접근성 누락**: 시맨틱, 스크린 리더 지원 직접 처리
4. **외부 클릭 처리**: 검색창 외부 클릭 시 닫기 로직 필요
5. **키보드 처리**: IME 액션, 포커스 이동 직접 구현

---

## 해결책: Material3 SearchBar 사용

### 올바른 코드

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicSearchBarExample() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("검색어를 입력하세요") },
                leadingIcon = { Icon(Icons.Default.Search, "검색") }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        // 검색 결과 또는 제안 표시
    }
}
```

### 해결되는 이유

1. **내장 애니메이션**: 확장/축소 시 자동 애니메이션
2. **Material Design 3 준수**: 가이드라인에 맞는 스타일 자동 적용
3. **접근성 지원**: 시맨틱 정보 자동 제공
4. **상태 관리 단순화**: expanded/onExpandedChange로 간단히 처리
5. **외부 클릭 자동 처리**: 검색창 외부 클릭 시 자동으로 닫힘

---

## SearchBar vs DockedSearchBar

| 특성 | SearchBar | DockedSearchBar |
|------|-----------|-----------------|
| **확장 동작** | 전체 화면으로 확장 | 검색바 아래 제한된 영역으로 확장 |
| **권장 기기** | 모바일 (소형 화면) | 태블릿, 데스크톱 (대형 화면) |
| **사용 사례** | 일반적인 검색 UI | 검색과 다른 콘텐츠가 함께 표시되어야 할 때 |

```kotlin
// 화면 크기에 따라 적절한 컴포넌트 선택
@Composable
fun AdaptiveSearchBar(windowSizeClass: WindowSizeClass) {
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        SearchBar(...)  // 모바일
    } else {
        DockedSearchBar(...)  // 태블릿/데스크톱
    }
}
```

## 핵심 특징

### 1. 내장 애니메이션
- 확장/축소 시 부드러운 전환 애니메이션 자동 적용
- leading icon 이동, 입력 필드 확장 등 자동 처리

### 2. 상태 관리
```kotlin
// 필수 상태
var query by remember { mutableStateOf("") }  // 검색어
var expanded by rememberSaveable { mutableStateOf(false) }  // 확장 상태

// rememberSaveable로 화면 회전 시에도 확장 상태 유지
```

### 3. 접근성 기본 지원
- 시맨틱 정보 자동 제공
- 스크린 리더 호환
- 키보드 네비게이션 지원

## 핵심 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `inputField` | `@Composable () -> Unit` | 입력 필드 정의 (SearchBarDefaults.InputField 사용) |
| `expanded` | `Boolean` | 검색바 확장 상태 |
| `onExpandedChange` | `(Boolean) -> Unit` | 확장 상태 변경 콜백 |
| `content` | `@Composable ColumnScope.() -> Unit` | 검색 결과/제안 콘텐츠 |

### SearchBarDefaults.InputField 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `query` | `String` | 현재 검색어 |
| `onQueryChange` | `(String) -> Unit` | 검색어 변경 콜백 |
| `onSearch` | `(String) -> Unit` | 검색 실행 콜백 (키보드 검색 버튼) |
| `placeholder` | `@Composable () -> Unit` | 플레이스홀더 텍스트 |
| `leadingIcon` | `@Composable () -> Unit` | 앞쪽 아이콘 (보통 검색 아이콘) |
| `trailingIcon` | `@Composable () -> Unit` | 뒤쪽 아이콘 (지우기, 음성 검색 등) |

---

## 검색 기록 관리 예제

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithHistory() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf("이전 검색어1", "이전 검색어2") }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    expanded = false
                    if (query.isNotBlank() && !searchHistory.contains(query)) {
                        searchHistory.add(0, query)
                    }
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("검색어를 입력하세요") },
                leadingIcon = { Icon(Icons.Default.Search, "검색") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, "지우기")
                        }
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        LazyColumn {
            items(searchHistory) { history ->
                ListItem(
                    headlineContent = { Text(history) },
                    leadingContent = { Icon(Icons.Default.Refresh, null) },
                    modifier = Modifier.clickable {
                        query = history
                        expanded = false
                    }
                )
            }
        }
    }
}
```

## 디바운스 처리

검색어 입력 시 매 글자마다 API를 호출하면 비효율적입니다.
디바운스를 적용하여 입력이 멈춘 후 일정 시간(보통 300ms) 후에 검색을 실행합니다.

```kotlin
@Composable
fun SearchWithDebounce() {
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }

    // snapshotFlow + debounce 패턴
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300)  // 300ms 대기
            .filter { it.length >= 2 }  // 2글자 이상
            .distinctUntilChanged()  // 중복 방지
            .collect { searchQuery ->
                searchResults = performSearch(searchQuery)
            }
    }

    SearchBar(...)
}
```

## 검색어 하이라이팅

검색 결과에서 검색어와 일치하는 부분을 강조 표시합니다.

```kotlin
@Composable
fun HighlightedText(text: String, query: String) {
    val annotatedString = buildAnnotatedString {
        val startIndex = text.lowercase().indexOf(query.lowercase())
        if (startIndex >= 0 && query.isNotEmpty()) {
            append(text.substring(0, startIndex))
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    background = Color.Yellow.copy(alpha = 0.3f)
                )
            ) {
                append(text.substring(startIndex, startIndex + query.length))
            }
            append(text.substring(startIndex + query.length))
        } else {
            append(text)
        }
    }
    Text(annotatedString)
}
```

## 필터 칩 연동

검색과 함께 필터를 제공하여 결과를 좁힐 수 있습니다.

```kotlin
@Composable
fun SearchWithFilters() {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = listOf("전체", "과일", "채소", "음료")

    Column {
        SearchBar(...)

        // 필터 칩
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = if (selectedCategory == category) null else category
                    },
                    label = { Text(category) }
                )
            }
        }
    }
}
```

## 사용 시나리오

1. **앱 내 콘텐츠 검색**: 메모, 연락처, 파일 검색
2. **E-commerce 검색**: 상품 검색 + 카테고리 필터
3. **지도 앱**: 장소 검색 + 검색 기록
4. **설정 검색**: 앱 설정 항목 검색

## Material3 1.4.0 신규 API (2025년 9월)

Material3 1.4.0에서 SearchBar API가 크게 개선되었습니다.

### 주요 변경사항

| 구분 | 기존 API | 신규 API (1.4.0+) |
|------|---------|------------------|
| 상태 관리 | `expanded`, `onExpandedChange` 개별 관리 | `SearchBarState`로 통합 |
| 확장 화면 | 동일 컴포저블에서 처리 | 별도 컴포저블 분리 |

### SearchBarState 도입

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSearchBarExample() {
    val searchBarState = rememberSearchBarState()
    var query by remember { mutableStateOf("") }

    SearchBar(
        state = searchBarState,
        inputField = {
            SearchBarDefaults.InputField(
                state = searchBarState,
                query = query,
                onQueryChange = { query = it },
                onSearch = { searchBarState.collapse() }
            )
        }
    ) {
        // 검색 결과
    }
}
```

### 새로운 확장 컴포저블

| 컴포저블 | 설명 | 권장 사용 |
|---------|------|----------|
| `ExpandedFullScreenSearchBar` | 전체 화면으로 확장 | 모바일 (compact) |
| `ExpandedDockedSearchBar` | 화면 일부에서 확장 | 태블릿/데스크톱 (medium/expanded) |

### TopSearchBar

스크롤 동작과 인셋(insets) 처리가 내장된 새로운 컴포저블입니다.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBarExample() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    TopSearchBar(
        scrollBehavior = scrollBehavior,
        inputField = { /* ... */ }
    ) {
        // 검색 결과
    }
}
```

> **참고**: 기존 API(`expanded`/`onExpandedChange` 방식)도 여전히 작동하지만,
> 새 프로젝트에서는 `SearchBarState` 사용을 권장합니다.

---

## 주의사항

### 1. 실험적 API
SearchBar는 아직 실험적 API입니다. 향후 API가 변경될 수 있습니다.
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
```

### 2. deprecated 경고 (Material3 1.4.0+)
기존 `DockedSearchBar` 오버로드는 deprecated되었습니다:
```kotlin
// deprecated - 사용 자제
DockedSearchBar(query, onQueryChange, ...)

// 권장 - inputField 파라미터 사용
DockedSearchBar(
    inputField = { SearchBarDefaults.InputField(...) },
    expanded = expanded,
    onExpandedChange = { ... }
) { ... }
```

### 3. 아이콘 의존성 추가 필요 (Material3 1.4.0+)
Material3 1.4.0부터 `material-icons-core`가 자동으로 포함되지 않습니다:
```kotlin
// build.gradle.kts
dependencies {
    implementation("androidx.compose.material:material-icons-core")
    // 확장 아이콘이 필요한 경우
    implementation("androidx.compose.material:material-icons-extended")
}
```

### 4. rememberSaveable 사용
화면 회전 시 확장 상태를 유지하려면 `rememberSaveable`을 사용합니다:
```kotlin
var expanded by rememberSaveable { mutableStateOf(false) }
```

### 5. 검색 기록 제한
무한히 쌓이지 않도록 최대 개수를 제한합니다:
```kotlin
if (searchHistory.size > 10) {
    searchHistory.removeLast()
}
```

### 6. 빈 검색어 처리
빈 문자열로 검색 실행을 방지합니다:
```kotlin
onSearch = {
    if (query.isNotBlank()) {
        // 검색 실행
    }
}
```

## 연습 문제

### 연습 1: 기본 SearchBar + 검색 결과 표시
기본 SearchBar를 구현하고, 더미 데이터에서 검색 결과를 필터링하여 표시합니다.

### 연습 2: 검색 기록 저장 및 표시
검색 실행 시 기록을 저장하고, 검색 기록을 표시 및 삭제하는 기능을 구현합니다.

### 연습 3: 필터 칩과 연동된 고급 검색
카테고리 필터 칩을 추가하고, 디바운스와 검색어 하이라이팅을 적용합니다.

## 다음 학습

- [Adaptive Layout](../../structure/adaptive_layout/README.md) - 화면 크기에 따른 SearchBar/DockedSearchBar 선택
- [Focus Management](../../interaction/focus_management/README.md) - 검색 필드 포커스 관리
- [LaunchedEffect](../../effect/launched_effect/README.md) - 디바운스 구현에 활용

## 참고 자료

- [Android Developers - Search bar](https://developer.android.com/develop/ui/compose/components/search-bar) - 공식 가이드
- [Material Design 3 - Search](https://m3.material.io/components/search/overview) - 디자인 가이드라인
- [Compose Material 3 Release Notes](https://developer.android.com/jetpack/androidx/releases/compose-material3) - 버전별 변경사항
- [SearchBarDefaults API Reference](https://developer.android.com/reference/kotlin/androidx/compose/material3/SearchBarDefaults) - API 레퍼런스
- [Composables.com - SearchBar](https://composables.com/material3/searchbar) - 상세 문서 및 예제

> **최종 업데이트**: 2025년 12월 - Material3 1.4.0 API 변경사항 반영
