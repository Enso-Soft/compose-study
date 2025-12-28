# Menu 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Menu는 사용자가 버튼이나 텍스트 필드를 클릭했을 때 **임시 표면에 옵션 목록을 표시**하는 UI 컴포넌트입니다. Compose에서는 `DropdownMenu`, `DropdownMenuItem`, `ExposedDropdownMenuBox`를 사용하여 다양한 메뉴를 구현할 수 있습니다.

---

## 핵심 특징

### 1. DropdownMenu - 팝업 메뉴

```kotlin
var expanded by remember { mutableStateOf(false) }

Box {
    IconButton(onClick = { expanded = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = "더보기")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("수정") },
            onClick = { /* 액션 */ }
        )
        DropdownMenuItem(
            text = { Text("삭제") },
            onClick = { /* 액션 */ }
        )
    }
}
```

### 2. DropdownMenuItem - 메뉴 항목

```kotlin
DropdownMenuItem(
    text = { Text("설정") },
    onClick = { /* 액션 */ },
    leadingIcon = { Icon(Icons.Outlined.Settings, null) },
    trailingIcon = { Icon(Icons.AutoMirrored.Outlined.OpenInNew, null) }
)
```

### 3. ExposedDropdownMenuBox - 선택 드롭다운

```kotlin
ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = it }
) {
    TextField(
        value = selectedOption,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
    )
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = {
                    selectedOption = option
                    expanded = false
                }
            )
        }
    }
}
```

---

## 문제 상황: Menu 없이 팝업 구현

### 시나리오

게시글의 "더보기" 버튼을 눌렀을 때 수정/삭제/공유 옵션을 보여주는 메뉴를 구현하려고 합니다. DropdownMenu를 모르는 상태에서 직접 Box와 Column으로 구현하려고 시도합니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun ManualMenuAttempt() {
    var showMenu by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("게시글 제목")
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, "더보기")
            }
        }

        // 문제: 레이아웃에 영향을 주고 오버레이되지 않음
        if (showMenu) {
            Card(
                modifier = Modifier.padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "수정",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(12.dp)
                    )
                    Text(
                        text = "삭제",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(12.dp)
                    )
                }
            }
        }

        // 다른 콘텐츠가 밀려남!
        Text("게시글 내용...")
    }
}
```

### 발생하는 문제점

1. **레이아웃 밀림**: 메뉴가 나타나면 아래 콘텐츠가 밀림
2. **위치 문제**: 버튼 바로 아래에 메뉴를 정확히 위치시키기 어려움
3. **외부 클릭**: 메뉴 외부를 클릭해도 닫히지 않음
4. **오버레이 부재**: 다른 UI 위에 떠 있는 효과가 없음
5. **포커스/접근성**: 키보드 탐색, 스크린 리더 지원 누락

---

## 해결책: DropdownMenu 사용

### 올바른 코드

```kotlin
@Composable
fun ProperMenuImplementation() {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("게시글 제목")

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, "더보기")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("수정") },
                        onClick = {
                            // 수정 액션
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Edit, null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("삭제") },
                        onClick = {
                            // 삭제 액션
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Delete, null)
                        }
                    )
                }
            }
        }

        // 다른 콘텐츠는 영향받지 않음
        Text("게시글 내용...")
    }
}
```

### 해결되는 이유

1. **오버레이**: 메뉴가 다른 UI 위에 팝업으로 표시됨
2. **자동 위치 계산**: 앵커(IconButton) 기준으로 자동 배치
3. **onDismissRequest**: 외부 클릭, 뒤로가기 시 자동으로 닫힘
4. **접근성**: 키보드 탐색, 스크린 리더 자동 지원
5. **애니메이션**: 표시/숨김 애니메이션 내장

---

## DropdownMenu 파라미터

| 파라미터 | 설명 | 필수 |
|---------|------|------|
| `expanded` | 메뉴 표시 여부 | O |
| `onDismissRequest` | 메뉴 닫힘 요청 시 콜백 | O |
| `modifier` | 레이아웃 수정자 | X |
| `offset` | 위치 오프셋 (DpOffset) | X |
| `scrollState` | 스크롤 상태 | X |
| `shape` | 컨테이너 모양 | X |
| `containerColor` | 배경색 | X |

## DropdownMenuItem 파라미터

| 파라미터 | 설명 | 필수 |
|---------|------|------|
| `text` | 메뉴 아이템 라벨 | O |
| `onClick` | 선택 시 콜백 | O |
| `leadingIcon` | 시작 아이콘 | X |
| `trailingIcon` | 끝 아이콘 | X |
| `enabled` | 활성화 여부 | X |
| `colors` | 색상 설정 | X |

---

## 사용 시나리오

### 1. 더보기 메뉴 (More Options)

앱바나 리스트 아이템에서 추가 옵션을 제공할 때 사용합니다.

```kotlin
IconButton(onClick = { expanded = true }) {
    Icon(Icons.Default.MoreVert, "더보기")
}
DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
    DropdownMenuItem(text = { Text("수정") }, onClick = { })
    DropdownMenuItem(text = { Text("삭제") }, onClick = { })
    DropdownMenuItem(text = { Text("공유") }, onClick = { })
}
```

### 2. 설정 메뉴 (아이콘 + 구분선)

설정이나 프로필 메뉴에서 그룹별로 구분할 때 사용합니다.

```kotlin
DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
    DropdownMenuItem(
        text = { Text("프로필") },
        leadingIcon = { Icon(Icons.Outlined.Person, null) },
        onClick = { }
    )
    DropdownMenuItem(
        text = { Text("설정") },
        leadingIcon = { Icon(Icons.Outlined.Settings, null) },
        onClick = { }
    )

    HorizontalDivider()

    DropdownMenuItem(
        text = { Text("로그아웃") },
        leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Logout, null) },
        onClick = { }
    )
}
```

### 3. 정렬 옵션 선택

```kotlin
ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = it }
) {
    TextField(
        value = selectedSort,
        onValueChange = {},
        readOnly = true,
        label = { Text("정렬") },
        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
    )
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        listOf("최신순", "인기순", "가격순").forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = {
                    selectedSort = option
                    expanded = false
                }
            )
        }
    }
}
```

### 4. 국가/지역 선택

```kotlin
val countries = listOf("한국", "미국", "일본", "중국", "영국")
var selectedCountry by remember { mutableStateOf(countries[0]) }

ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = it }
) {
    OutlinedTextField(
        value = selectedCountry,
        onValueChange = {},
        readOnly = true,
        label = { Text("국가") },
        modifier = Modifier
            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            .fillMaxWidth(),
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
    )
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        countries.forEach { country ->
            DropdownMenuItem(
                text = { Text(country) },
                onClick = {
                    selectedCountry = country
                    expanded = false
                }
            )
        }
    }
}
```

---

## DropdownMenu vs ExposedDropdownMenuBox

| 항목 | DropdownMenu | ExposedDropdownMenuBox |
|------|-------------|------------------------|
| **용도** | 액션 메뉴 (더보기) | 값 선택 (드롭다운) |
| **트리거** | IconButton, TextButton | TextField |
| **선택 표시** | 없음 (액션 실행) | TextField에 선택값 표시 |
| **사용 사례** | 수정/삭제/공유 | 국가 선택, 정렬 옵션 |

### 언제 무엇을 사용할까?

```
액션을 실행하고 싶다 (수정, 삭제)
    → DropdownMenu + IconButton

값을 선택하고 표시하고 싶다 (국가, 정렬)
    → ExposedDropdownMenuBox + TextField
```

---

## 주의사항

1. **Box로 감싸기**
   - DropdownMenu는 앵커(IconButton 등)와 함께 Box로 감싸야 위치가 올바르게 계산됨

2. **expanded 상태 관리**
   - `onClick`과 `onDismissRequest` 모두에서 `expanded = false` 설정 필요

3. **ExposedDropdownMenuBox 어노테이션**
   - `@OptIn(ExperimentalMaterial3Api::class)` 필요

4. **menuAnchor 필수**
   - ExposedDropdownMenuBox 내 TextField에 `menuAnchor()` 수정자 필수
   - 읽기 전용: `MenuAnchorType.PrimaryNotEditable`
   - 편집 가능: `MenuAnchorType.PrimaryEditable`

5. **HorizontalDivider 사용**
   - 메뉴 그룹 구분 시 `HorizontalDivider()` 사용 (Material 3)

---

## 연습 문제

### 연습 1: 게시글 더보기 메뉴 (쉬움)

IconButton과 DropdownMenu를 사용하여 게시글의 더보기 메뉴를 구현하세요.

**요구사항**:
- MoreVert 아이콘의 IconButton
- 메뉴 항목: 수정, 삭제, 공유
- 각 항목 클릭 시 선택된 액션 표시

### 연습 2: 설정 메뉴 with 아이콘 (중간)

아이콘과 구분선이 있는 설정 메뉴를 구현하세요.

**요구사항**:
- leadingIcon으로 각 항목에 아이콘 추가
- HorizontalDivider로 그룹 구분
- 그룹: [프로필, 설정] / [도움말, 정보]

### 연습 3: 국가 선택 드롭다운 (어려움)

ExposedDropdownMenuBox를 사용하여 국가 선택 UI를 구현하세요.

**요구사항**:
- 5개 국가 목록 (한국, 미국, 일본, 중국, 영국)
- 선택된 국가가 TextField에 표시
- 확장/축소 아이콘 표시

---

## 다음 학습

- **Dialog**: 모달 다이얼로그 구현
- **BottomSheet**: 많은 옵션 표시 시 활용
- **Snackbar**: 간단한 피드백 메시지
