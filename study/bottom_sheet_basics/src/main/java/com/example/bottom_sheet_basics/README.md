# BottomSheet 기초 학습

## 개념

`ModalBottomSheet`는 화면 하단에서 올라오는 **보조 콘텐츠 영역**입니다. 모바일 앱에서 가장 자연스러운 UI 패턴 중 하나로, 여러 옵션을 선택하거나 추가 정보를 표시할 때 사용합니다.

```kotlin
var showSheet by remember { mutableStateOf(false) }

Button(onClick = { showSheet = true }) {
    Text("옵션 보기")
}

if (showSheet) {
    ModalBottomSheet(
        onDismissRequest = { showSheet = false }
    ) {
        // 시트 내용
        Text("Hello, BottomSheet!")
    }
}
```

---

## BottomSheet vs Dialog 선택 기준

| 상황 | 권장 UI | 이유 |
|------|---------|------|
| 간단한 확인/경고 | Dialog | "확인/취소" 두 옵션만 필요 |
| 중요한 결정 | Dialog | 사용자 주의 집중 필요 |
| 여러 옵션 선택 | **BottomSheet** | 목록 형태로 자연스럽게 표시 |
| 상세 정보 표시 | **BottomSheet** | 더 많은 콘텐츠 공간 활용 |
| 액션 시트 (공유, 삭제 등) | **BottomSheet** | 모바일 UX 표준 패턴 |
| 필터/정렬 옵션 | **BottomSheet** | 빠른 선택 후 닫기 |

### 왜 BottomSheet인가?

1. **엄지 접근성**: 모바일에서 하단은 엄지로 쉽게 닿는 위치
2. **자연스러운 제스처**: 아래로 드래그하여 닫는 것이 직관적
3. **화면 가림 최소화**: 상단 콘텐츠는 보이므로 맥락 유지
4. **유연한 높이**: 콘텐츠에 따라 자동으로 높이 조절

---

## 핵심 특징

### 1. Boolean 상태로 열기/닫기

```kotlin
// 가장 단순한 형태
var showSheet by remember { mutableStateOf(false) }

if (showSheet) {
    ModalBottomSheet(
        onDismissRequest = { showSheet = false }
    ) {
        Text("시트 내용")
    }
}
```

- `showSheet = true`: 시트 열기
- `showSheet = false`: 시트 닫기 (composition에서 제거)
- `onDismissRequest`: 드래그로 닫거나 scrim 터치 시 호출

### 2. onDismissRequest 콜백

```kotlin
ModalBottomSheet(
    onDismissRequest = {
        // 다음 상황에서 호출됨:
        // 1. 아래로 드래그해서 닫을 때
        // 2. 어두운 배경(scrim) 터치 시
        // 3. 시스템 뒤로가기 버튼
        showSheet = false
    }
) { /* 내용 */ }
```

### 3. ColumnScope 콘텐츠

시트 내부는 `ColumnScope`입니다. 세로로 항목을 배치합니다.

```kotlin
ModalBottomSheet(onDismissRequest = { showSheet = false }) {
    // Column처럼 배치됨
    Text("첫 번째 항목")
    Text("두 번째 항목")
    Text("세 번째 항목")
}
```

### 4. 기본 드래그 동작

- **아래로 드래그**: 시트 닫힘
- **위로 드래그**: 시트 확장 (콘텐츠가 많을 경우)
- **드래그 핸들**: 상단에 기본 핸들 표시

---

## 문제 상황: Dialog로 옵션 메뉴 구현

### 시나리오: 게시물 "더보기" 버튼

게시물에서 더보기 버튼을 누르면 공유, 복사, 삭제 등의 옵션을 보여줘야 합니다.

### 잘못된 코드 예시 (Dialog 사용)

```kotlin
@Composable
fun PostOptionsDialog() {
    var showOptions by remember { mutableStateOf(false) }

    IconButton(onClick = { showOptions = true }) {
        Icon(Icons.Default.MoreVert, "더보기")
    }

    if (showOptions) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            title = { Text("옵션") },
            text = {
                Column {
                    TextButton(onClick = { /* 공유 */ }) {
                        Text("공유하기")
                    }
                    TextButton(onClick = { /* 복사 */ }) {
                        Text("링크 복사")
                    }
                    TextButton(onClick = { /* 삭제 */ }) {
                        Text("삭제")
                    }
                }
            },
            confirmButton = { },  // 불필요한 파라미터
            dismissButton = { }   // 불필요한 파라미터
        )
    }
}
```

### 발생하는 문제점

1. **어색한 구조**: AlertDialog는 확인/취소용이지 옵션 목록용이 아님
2. **빈 버튼 필요**: confirmButton/dismissButton을 비워둬야 함
3. **드래그 불가**: 드래그로 닫을 수 없어 모바일 UX에 맞지 않음
4. **화면 중앙 표시**: 엄지로 닿기 어려운 위치
5. **옵션 많으면 복잡**: 옵션이 늘어나면 Dialog가 커져서 어색함

---

## 해결책: ModalBottomSheet 사용

### 올바른 코드

```kotlin
@Composable
fun PostOptionsBottomSheet() {
    var showSheet by remember { mutableStateOf(false) }

    IconButton(onClick = { showSheet = true }) {
        Icon(Icons.Default.MoreVert, "더보기")
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }
        ) {
            ListItem(
                headlineContent = { Text("공유하기") },
                leadingContent = {
                    Icon(Icons.Default.Share, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    // 공유 로직
                    showSheet = false
                }
            )
            ListItem(
                headlineContent = { Text("링크 복사") },
                leadingContent = {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    // 복사 로직
                    showSheet = false
                }
            )
            ListItem(
                headlineContent = { Text("삭제") },
                leadingContent = {
                    Icon(Icons.Default.Delete, contentDescription = null)
                },
                modifier = Modifier.clickable {
                    // 삭제 로직
                    showSheet = false
                }
            )

            Spacer(modifier = Modifier.height(32.dp))  // 하단 여백
        }
    }
}
```

### 해결되는 이유

1. **자연스러운 구조**: 옵션 목록을 위한 최적의 UI
2. **드래그 지원**: 아래로 드래그해서 닫기 가능
3. **엄지 접근성**: 화면 하단에서 올라와 쉽게 터치
4. **확장 가능**: 옵션이 많아져도 스크롤로 대응

---

## 사용 시나리오

### 1. 기본 액션 시트

```kotlin
@Composable
fun ActionSheet(
    onDismiss: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        ListItem(
            headlineContent = { Text("공유") },
            leadingContent = { Icon(Icons.Default.Share, null) },
            modifier = Modifier.clickable { onShare(); onDismiss() }
        )
        ListItem(
            headlineContent = { Text("복사") },
            leadingContent = { Icon(Icons.Default.ContentCopy, null) },
            modifier = Modifier.clickable { onCopy(); onDismiss() }
        )
        ListItem(
            headlineContent = { Text("삭제") },
            leadingContent = { Icon(Icons.Default.Delete, null) },
            modifier = Modifier.clickable { onDelete(); onDismiss() }
        )
        Spacer(Modifier.height(32.dp))
    }
}
```

### 2. 상품 정보 표시

```kotlin
@Composable
fun ProductInfoSheet(
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${product.price}원",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Text(text = product.description)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onAddToCart(); onDismiss() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("장바구니 담기")
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}
```

### 3. 정렬 옵션 선택

```kotlin
@Composable
fun SortOptionsSheet(
    currentSort: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Text(
            text = "정렬 기준",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        SortOption.entries.forEach { option ->
            ListItem(
                headlineContent = { Text(option.displayName) },
                leadingContent = {
                    RadioButton(
                        selected = currentSort == option,
                        onClick = null  // ListItem 전체 클릭으로 처리
                    )
                },
                modifier = Modifier.clickable {
                    onSortSelected(option)
                    onDismiss()
                }
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}

enum class SortOption(val displayName: String) {
    LATEST("최신순"),
    POPULAR("인기순"),
    PRICE_LOW("가격 낮은순"),
    PRICE_HIGH("가격 높은순")
}
```

---

## 주의사항

### 1. 하단 여백 추가

시스템 네비게이션 바와 겹치지 않도록 하단에 Spacer를 추가하세요.

```kotlin
ModalBottomSheet(onDismissRequest = { }) {
    // 내용...

    Spacer(Modifier.height(32.dp))  // 하단 여백
}
```

### 2. 옵션 클릭 후 시트 닫기

옵션을 선택한 후 반드시 시트를 닫아야 합니다.

```kotlin
modifier = Modifier.clickable {
    onShare()           // 1. 액션 수행
    showSheet = false   // 2. 시트 닫기
}
```

### 3. ExperimentalMaterial3Api 필요

`ModalBottomSheet`는 현재 실험적 API입니다.

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen() {
    ModalBottomSheet(...) { }
}
```

### 4. Composition에서 제거

시트가 닫히면 composition에서 완전히 제거됩니다.

```kotlin
if (showSheet) {  // false가 되면 ModalBottomSheet가 composition에서 제거됨
    ModalBottomSheet(...) { }
}
```

---

## 연습 문제

### 연습 1: 기본 액션 시트 (쉬움)
버튼 클릭 시 편집, 공유, 삭제 옵션이 있는 BottomSheet를 표시하세요.
각 옵션 클릭 시 시트를 닫고 선택한 옵션을 표시하세요.

### 연습 2: 상품 상세 정보 (중간)
상품 목록에서 상품 클릭 시 해당 상품의 상세 정보를 BottomSheet로 표시하세요.
상세 정보에는 이름, 가격, 설명, 장바구니 담기 버튼이 있어야 합니다.

### 연습 3: 정렬 옵션 선택기 (중간-어려움)
목록 화면에서 정렬 버튼 클릭 시 정렬 옵션 BottomSheet를 표시하세요.
현재 선택된 옵션을 RadioButton으로 표시하고, 옵션 선택 시 반영되어야 합니다.

---

## 다음 학습

**bottom_sheet_advanced** - SheetState로 세밀한 상태 제어
- `rememberModalBottomSheetState` 사용법
- `sheetState.show()`, `sheetState.hide()` 프로그래밍 방식 제어
- `skipPartiallyExpanded` 옵션
- 중첩 BottomSheet + BackHandler 처리
- 커스텀 드래그 핸들, Scrim 색상 변경
