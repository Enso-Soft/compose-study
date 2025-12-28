# SearchBar 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composition 간 값을 유지하는 상태 관리 기본 | [📚 학습하기](../../state/remember/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`SearchBar`는 Material Design 3에서 제공하는 **검색 UI 컴포넌트**입니다.
사용자가 검색어를 입력하고, 검색 제안이나 결과를 확인할 수 있는 완전한 검색 경험을 제공합니다.

> **핵심 메시지**: TextField로 검색창을 직접 만들면 확장/축소 애니메이션, 제안 표시 등 복잡한 구현이 필요합니다.
> SearchBar를 사용하면 이 모든 것이 자동으로 처리됩니다!

## 핵심 특징

### 1. 확장/축소 애니메이션 내장
- 검색창을 클릭하면 자동으로 부드럽게 확장됩니다
- 외부를 클릭하거나 검색을 실행하면 자동으로 축소됩니다

### 2. 간단한 상태 관리
```kotlin
// 필요한 상태는 단 2개!
var query by remember { mutableStateOf("") }      // 검색어
var expanded by rememberSaveable { mutableStateOf(false) }  // 확장 상태
```

### 3. Material Design 3 스타일 자동 적용
- 가이드라인에 맞는 모양, 색상, 애니메이션이 자동으로 적용됩니다
- 접근성(Accessibility)도 자동으로 지원됩니다

---

## 문제 상황: TextField로 검색창 직접 구현

### 시나리오
앱에 과일 검색 기능을 추가해야 합니다.
"TextField를 사용해서 검색창을 만들면 되겠지?" 라고 생각할 수 있습니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun ManualSearchUI() {
    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val fruits = listOf("사과", "바나나", "오렌지", "포도", "딸기")
    val filteredFruits = fruits.filter { it.contains(query) }

    Column {
        // 1. TextField로 직접 구현 - 스타일링 필요
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                isExpanded = true
            },
            placeholder = { Text("과일 검색") },
            leadingIcon = { Icon(Icons.Default.Search, "검색") },
            modifier = Modifier.fillMaxWidth()
        )

        // 2. 제안 목록 표시 - 위치, 애니메이션 직접 처리
        AnimatedVisibility(visible = isExpanded && filteredFruits.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    filteredFruits.forEach { fruit ->
                        Text(
                            text = fruit,
                            modifier = Modifier
                                .clickable {
                                    query = fruit
                                    isExpanded = false
                                }
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

1. **확장/축소 애니메이션 직접 구현 필요**: AnimatedVisibility만으로는 부드러운 전환이 어려움
2. **외부 클릭 처리 없음**: 검색창 밖을 클릭해도 닫히지 않음
3. **Material Design 미준수**: 가이드라인에 맞는 모양이 아님
4. **접근성 미흡**: 스크린 리더 지원을 위한 추가 작업 필요
5. **코드량 증가**: 완전한 검색 UI를 만들려면 100줄 이상 필요

---

## 해결책: SearchBar 사용

### 올바른 코드

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProperSearchUI() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val fruits = listOf("사과", "바나나", "오렌지", "포도", "딸기")
    val filteredFruits = if (query.isEmpty()) fruits else fruits.filter { it.contains(query) }

    Box(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("과일 검색") },
                    leadingIcon = { Icon(Icons.Default.Search, "검색") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            // 검색 결과 표시 영역
            LazyColumn {
                items(filteredFruits) { fruit ->
                    ListItem(
                        headlineContent = { Text(fruit) },
                        modifier = Modifier.clickable {
                            query = fruit
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
```

### 해결되는 이유

| 문제점 | SearchBar로 해결 |
|--------|-----------------|
| 확장/축소 애니메이션 | 내장되어 자동 처리 |
| 외부 클릭 닫기 | 자동으로 닫힘 |
| Material Design 스타일 | 자동 적용 |
| 접근성 | 자동 지원 |
| 코드량 | 약 30줄로 완성 |

---

## 핵심 파라미터

### SearchBar 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `inputField` | `@Composable () -> Unit` | 입력 필드 정의 (SearchBarDefaults.InputField 사용) |
| `expanded` | `Boolean` | 검색창이 확장된 상태인지 (true: 제안 목록 표시) |
| `onExpandedChange` | `(Boolean) -> Unit` | 확장 상태가 변경될 때 호출되는 콜백 |
| `content` | `@Composable ColumnScope.() -> Unit` | 검색 결과/제안을 표시하는 영역 |

### SearchBarDefaults.InputField 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `query` | `String` | 현재 입력된 검색어 |
| `onQueryChange` | `(String) -> Unit` | 사용자가 텍스트를 입력할 때마다 호출 |
| `onSearch` | `(String) -> Unit` | 키보드의 검색 버튼을 눌렀을 때 호출 |
| `placeholder` | `@Composable () -> Unit` | 검색창이 비어있을 때 보여주는 안내 텍스트 |
| `leadingIcon` | `@Composable () -> Unit` | 앞쪽 아이콘 (보통 검색 아이콘) |
| `trailingIcon` | `@Composable () -> Unit` | 뒤쪽 아이콘 (지우기 버튼 등) |

---

## 파라미터 이해하기

### 1. query와 onQueryChange
```kotlin
var query by remember { mutableStateOf("") }

// 사용자가 "사과"를 입력하면...
// onQueryChange가 "사", "사과" 순서로 호출됩니다
onQueryChange = { newText ->
    query = newText  // 상태 업데이트
}
```

### 2. expanded와 onExpandedChange
```kotlin
var expanded by rememberSaveable { mutableStateOf(false) }

// expanded = true  -> 검색창이 확장되어 제안 목록이 보임
// expanded = false -> 검색창이 축소되어 제안 목록이 숨겨짐

// rememberSaveable을 사용하면 화면 회전 시에도 상태 유지!
```

### 3. onSearch
```kotlin
onSearch = { searchQuery ->
    // 사용자가 키보드의 검색 버튼(돋보기)을 눌렀을 때 실행
    expanded = false  // 검색창 닫기
    // 여기서 실제 검색 로직 실행
}
```

---

## 사용 시나리오

1. **앱 내 목록 검색**: 연락처, 메모, 파일 검색
2. **상품 검색**: 쇼핑 앱에서 상품 찾기
3. **설정 검색**: 앱 설정 항목 빠르게 찾기

---

## 주의사항

### 1. 실험적 API 어노테이션 필요
SearchBar는 아직 실험적 API입니다. 사용하려면 `@OptIn` 어노테이션이 필요합니다.
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchScreen() {
    // SearchBar 사용
}
```

### 2. rememberSaveable 사용 권장
화면 회전 시에도 확장 상태를 유지하려면 `rememberSaveable`을 사용하세요.
```kotlin
var expanded by rememberSaveable { mutableStateOf(false) }
```

### 3. Box로 감싸기
SearchBar는 확장될 때 전체 화면을 덮을 수 있으므로, 적절한 레이아웃으로 감싸는 것이 좋습니다.
```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    SearchBar(
        modifier = Modifier.align(Alignment.TopCenter),
        // ...
    )
}
```

---

## 연습 문제

### 연습 1: 과일 검색 - 쉬움
기본 SearchBar를 구현하고 과일 목록을 필터링하여 표시합니다.

### 연습 2: 연락처 검색 + 지우기 버튼 - 중간
trailingIcon을 추가하여 검색어를 한 번에 지울 수 있게 합니다.

### 연습 3: 도시 검색 + 검색 결과 표시 - 어려움
onSearch를 활용하여 검색 실행 후 결과를 별도로 표시합니다.

---

## 다음 학습

- [search_bar_advanced](../../search/search_bar_advanced/README.md) - 디바운스, 검색 기록, 필터 칩 등 고급 기능
- [DockedSearchBar](../../search/search_bar_advanced/README.md) - 태블릿에서 사용하는 고정형 검색바
- [Focus Management](../../interaction/focus_management/README.md) - 검색 필드 포커스 관리

---

## 참고 자료

- [Android Developers - Search bar](https://developer.android.com/develop/ui/compose/components/search-bar) - 공식 가이드
- [Material Design 3 - Search](https://m3.material.io/components/search/overview) - 디자인 가이드라인
