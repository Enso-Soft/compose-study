# BottomSheet 고급 활용 (Material3)

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `bottom_sheet` | ModalBottomSheet의 기본 사용법과 열기/닫기 제어 | [📚 학습하기](../../structure/bottom_sheet/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

BottomSheet는 화면 하단에서 올라오는 **보조 콘텐츠 영역**으로, 사용자가 메인 콘텐츠를 벗어나지 않고 추가 작업을 수행할 수 있게 해줍니다. Material Design 3에서는 두 가지 유형의 BottomSheet를 제공합니다:

| 유형 | 설명 | 사용 시점 |
|------|------|----------|
| **ModalBottomSheet** | 독립적인 오버레이로 표시, 백드롭(scrim)으로 뒤 콘텐츠를 어둡게 함 | 임시 액션 선택, 확인 다이얼로그 대용 |
| **BottomSheetScaffold** | 화면 구조의 일부로 통합, 항상 일부가 보임 (peek 상태) | 지속적인 보조 정보, 지도 앱의 상세 패널 |

> **왜 "고급" 활용인가?**
>
> BottomSheet는 단순히 열고 닫는 것 이상의 복잡한 상태 관리가 필요합니다.
> 이 모듈에서는 **SheetState를 활용한 프로그래밍 방식 제어**, **중첩 시트 처리**,
> **BackHandler 통합** 등 실무에서 자주 마주치는 고급 시나리오를 다룹니다.

## 핵심 특징

### 1. SheetState 상태 관리
```kotlin
val sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = false,  // true면 Expanded 상태만 가능
    confirmValueChange = { newValue -> true }  // 상태 변경 승인/거부
)

// 주요 속성
sheetState.isVisible          // 시트가 보이는지
sheetState.currentValue       // Hidden, PartiallyExpanded, Expanded
sheetState.targetValue        // 애니메이션 목표 상태

// 주요 함수 (suspend)
sheetState.show()             // 시트 표시
sheetState.hide()             // 시트 숨김
sheetState.expand()           // 완전 확장
sheetState.partialExpand()    // 부분 확장
```

### 2. ModalBottomSheet vs BottomSheetScaffold

| 특성 | ModalBottomSheet | BottomSheetScaffold |
|------|------------------|---------------------|
| 표시 방식 | 오버레이 | 화면 구조 일부 |
| 백드롭 | 있음 (scrim) | 없음 |
| 기본 상태 | Hidden | PartiallyExpanded |
| 사용 용도 | 임시 액션 | 지속적 보조 콘텐츠 |

### 3. SheetValue 상태

시트는 세 가지 상태를 가질 수 있으며, 각 상태는 사용자 경험에 중요한 역할을 합니다:

| 상태 | 설명 | 사용 시나리오 |
|------|------|--------------|
| **Hidden** | 완전히 숨김 (ModalBottomSheet만) | 시트가 필요 없을 때 |
| **PartiallyExpanded** | 부분 확장 (peek 상태) | 미리보기 제공, 드래그로 확장 유도 |
| **Expanded** | 완전 확장 | 전체 콘텐츠 표시 |

```kotlin
// 상태에 따른 UI 변화 감지
LaunchedEffect(sheetState.currentValue) {
    when (sheetState.currentValue) {
        SheetValue.Hidden -> { /* 시트 완전히 숨겨짐 */ }
        SheetValue.PartiallyExpanded -> { /* 일부만 보임 */ }
        SheetValue.Expanded -> { /* 전체 확장됨 */ }
    }
}
```

---

## 문제 상황: 상태 관리 복잡성

많은 개발자들이 BottomSheet를 처음 사용할 때 단순히 `Boolean` 변수만으로 열림/닫힘을 관리하려 합니다.
이는 간단해 보이지만 실제로 여러 문제를 야기합니다.

### 잘못된 코드 예시
```kotlin
@Composable
fun BadBottomSheetExample() {
    // 문제: Boolean만으로 상태 관리
    var isSheetOpen by remember { mutableStateOf(false) }

    Button(onClick = { isSheetOpen = true }) {
        Text("Open Sheet")
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                // 드래그로 닫을 때 호출됨
                isSheetOpen = false
            }
        ) {
            // 시트 내용
        }
    }
}
```

### 발생하는 문제점
1. **상태 동기화 실패**: 드래그로 닫을 때 내부 애니메이션과 isSheetOpen 불일치
2. **프로그래밍 방식 제어 불가**: show(), hide() 같은 세밀한 제어 불가능
3. **중간 상태 접근 불가**: PartiallyExpanded 상태 활용 불가
4. **애니메이션 완료 감지 불가**: 시트가 완전히 닫힌 후 작업 수행 어려움

## 해결책: rememberModalBottomSheetState 사용

### 올바른 코드
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoodBottomSheetExample() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    Button(onClick = { showSheet = true }) {
        Text("Open Sheet")
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Button(onClick = {
                scope.launch {
                    sheetState.hide()  // 애니메이션 완료 대기
                    showSheet = false   // composition에서 제거
                }
            }) {
                Text("Close")
            }
        }
    }
}
```

### 해결되는 이유

1. **이중 상태 관리**:
   - `showSheet`: Composition 제어 (시트가 화면에 존재하는지)
   - `sheetState`: UI 상태 제어 (애니메이션, 확장 정도)

2. **애니메이션 동기화**:
   - `sheetState.hide()`는 suspend 함수로, 애니메이션이 완료될 때까지 대기
   - 애니메이션 완료 후 `showSheet = false`로 composition에서 제거

3. **세밀한 제어**:
   - `expand()`, `partialExpand()` 등 다양한 상태 전환 가능
   - `currentValue`, `targetValue`로 현재/목표 상태 확인

---

## 고급 활용 시나리오

### 1. 기본 액션 시트
```kotlin
ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState
) {
    ListItem(
        headlineContent = { Text("공유") },
        leadingContent = { Icon(Icons.Default.Share, null) },
        modifier = Modifier.clickable { /* 공유 로직 */ }
    )
    ListItem(
        headlineContent = { Text("삭제") },
        leadingContent = { Icon(Icons.Default.Delete, null) },
        modifier = Modifier.clickable { /* 삭제 로직 */ }
    )
}
```

### 2. 드래그 핸들 커스터마이징
```kotlin
ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    dragHandle = {
        // 커스텀 드래그 핸들
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .width(40.dp)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
) { /* 내용 */ }
```

### 3. Scrim 색상 변경
```kotlin
ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    scrimColor = Color.Black.copy(alpha = 0.7f)  // 더 어두운 백드롭
) { /* 내용 */ }
```

### 4. 시트 내 스크롤 콘텐츠
```kotlin
ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState
) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(0.8f)  // 최대 높이 제한
    ) {
        items(100) { index ->
            ListItem(headlineContent = { Text("Item $index") })
        }
    }
}
```

### 5. 중첩 BottomSheet + BackHandler

중첩 시트에서 가장 중요한 것은 **뒤로가기 버튼 처리**입니다. 자식 시트가 열려있을 때 뒤로가기를 누르면 자식 시트만 닫혀야 합니다.

```kotlin
@Composable
fun NestedBottomSheets() {
    var showParent by remember { mutableStateOf(false) }
    var showChild by remember { mutableStateOf(false) }
    val parentState = rememberModalBottomSheetState()
    val childState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // 부모 시트가 닫히면 자식 상태도 정리
    LaunchedEffect(showParent) {
        if (!showParent) showChild = false
    }

    if (showParent) {
        ModalBottomSheet(
            onDismissRequest = { showParent = false },
            sheetState = parentState,
            // Material3 1.4.0+: ModalBottomSheetProperties를 통해 설정
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false  // 자체 BackHandler 비활성화
            )
        ) {
            // 자식 시트가 열려있을 때: 자식만 닫기
            BackHandler(enabled = showChild) {
                scope.launch {
                    childState.hide()
                    showChild = false
                }
            }

            // 자식 시트가 없을 때: 부모 닫기
            BackHandler(enabled = !showChild) {
                scope.launch {
                    parentState.hide()
                    showParent = false
                }
            }

            if (showChild) {
                ModalBottomSheet(
                    onDismissRequest = { showChild = false },
                    sheetState = childState
                ) { /* 자식 시트 내용 */ }
            }
        }
    }
}
```

> **핵심 포인트**:
> 1. `shouldDismissOnBackPress = false`로 기본 BackHandler 비활성화
> 2. `BackHandler`의 `enabled` 조건으로 우선순위 제어
> 3. `LaunchedEffect`로 부모 닫힘 시 자식 상태 정리

---

## 주의사항

| 항목 | 설명 |
|------|------|
| composition 제거 필수 | `hide()` 후 반드시 `showSheet = false`로 composition에서 제거 |
| CoroutineScope 필요 | `show()`, `hide()` 등은 suspend 함수이므로 `rememberCoroutineScope()` 필요 |
| BackHandler 우선순위 | Material3 ModalBottomSheet는 자체 BackHandler가 있어 충돌 주의 |
| shouldDismissOnBackPress | 중첩 시트에서는 `false`로 설정하고 직접 BackHandler 처리 |
| skipPartiallyExpanded | `true`로 설정하면 PartiallyExpanded 상태를 건너뛰고 바로 Expanded로 |

### 2025년 API 업데이트 참고

Material3 Compose 1.4.0+ 버전에서 일부 API가 변경되었습니다:

```kotlin
// ModalBottomSheetProperties를 통한 설정
ModalBottomSheet(
    onDismissRequest = { /* ... */ },
    properties = ModalBottomSheetDefaults.properties(
        shouldDismissOnBackPress = false,
        // 1.4.0-alpha18+: scrim 클릭으로 닫기 비활성화 옵션 추가
    )
)
```

---

## 연습 문제

| 연습 | 난이도 | 주제 |
|------|--------|------|
| 연습 1 | 기본 | 기본 ModalBottomSheet 구현 |
| 연습 2 | 중급 | BottomSheetScaffold로 3단계 높이 조절 |
| 연습 3 | 고급 | 중첩 BottomSheet + 뒤로가기 처리 |

### 연습 1: 기본 ModalBottomSheet 구현

공유, 링크 복사, 삭제 옵션이 있는 액션 시트를 만들어보세요.

**요구사항**:
- `rememberModalBottomSheetState()` 사용
- 옵션 클릭 시 애니메이션과 함께 닫기
- 선택된 옵션을 화면에 표시

### 연습 2: 3단계 높이 조절

BottomSheetScaffold를 사용하여 peek(100dp) / half(50%) / full 3단계로 조절되는 시트를 만들어보세요.

**요구사항**:
- `rememberBottomSheetScaffoldState()` 사용
- 각 상태로 전환하는 버튼 구현
- 현재 상태 표시

### 연습 3: 중첩 BottomSheet + 뒤로가기 처리

메인 시트 안에서 상세 시트를 열 수 있고, 뒤로가기 시 올바른 순서로 닫히도록 구현해보세요.

**요구사항**:
- `shouldDismissOnBackPress = false` 설정
- `BackHandler`로 우선순위 제어
- 부모 닫힘 시 자식 상태 정리

---

## 다음 학습

- **Scaffold와 Theming**: Material3 디자인 시스템 통합
- **Navigation과 BottomSheet 연동**: 네비게이션 상태와 시트 동기화
- **복잡한 UI 상태 관리**: 여러 시트와 다이얼로그 조합

---

## 참고 자료

- [Material Design 3 - Bottom sheets](https://m3.material.io/components/bottom-sheets)
- [Android Developers - Bottom sheets](https://developer.android.com/develop/ui/compose/components/bottom-sheets)
- [Compose Material3 Release Notes](https://developer.android.com/jetpack/androidx/releases/compose-material3)
