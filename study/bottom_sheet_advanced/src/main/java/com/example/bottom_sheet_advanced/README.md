# BottomSheet 고급 활용 (Material3)

## 개념

BottomSheet는 화면 하단에서 올라오는 보조 콘텐츠 영역입니다. Material3에서는 두 가지 유형의 BottomSheet를 제공합니다:

1. **ModalBottomSheet**: 독립적인 오버레이로 표시되며, 백드롭(scrim)이 있어 뒤의 콘텐츠를 어둡게 합니다.
2. **BottomSheetScaffold**: 화면 구조의 일부로 통합되어 항상 일부가 보입니다 (peek 상태).

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
- **Hidden**: 완전히 숨김 (ModalBottomSheet만)
- **PartiallyExpanded**: 부분 확장 (peek 상태)
- **Expanded**: 완전 확장

## 문제 상황: 상태 관리 복잡성

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
1. **이중 상태 관리**: showSheet(composition 제어) + sheetState(UI 상태) 분리
2. **애니메이션 동기화**: suspend 함수로 애니메이션 완료 후 작업 수행
3. **세밀한 제어**: expand(), partialExpand() 등 다양한 상태 전환

## 사용 시나리오

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
```kotlin
@Composable
fun NestedBottomSheets() {
    var showParent by remember { mutableStateOf(false) }
    var showChild by remember { mutableStateOf(false) }
    val parentState = rememberModalBottomSheetState()
    val childState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showParent) {
        ModalBottomSheet(
            onDismissRequest = { showParent = false },
            sheetState = parentState,
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false  // 자체 BackHandler 비활성화
            )
        ) {
            // 자식 시트 BackHandler
            BackHandler(enabled = showChild) {
                scope.launch {
                    childState.hide()
                    showChild = false
                }
            }

            // 부모 시트 BackHandler
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

## 주의사항

1. **composition 제거 필수**: hide() 후 반드시 composition에서 ModalBottomSheet 제거
2. **CoroutineScope 필요**: show(), hide() 등은 suspend 함수
3. **BackHandler 우선순위**: Material3 ModalBottomSheet는 자체 BackHandler가 있음
4. **shouldDismissOnBackPress**: 중첩 시트에서는 false로 설정하고 직접 처리
5. **skipPartiallyExpanded**: true로 설정하면 PartiallyExpanded 상태 건너뜀

## 연습 문제

### 연습 1: 기본 ModalBottomSheet 구현
공유, 링크 복사, 삭제 옵션이 있는 액션 시트를 만들어보세요.

### 연습 2: 3단계 높이 조절
BottomSheetScaffold를 사용하여 peek(100dp) / half(50%) / full 3단계로 조절되는 시트를 만들어보세요.

### 연습 3: 중첩 BottomSheet + 뒤로가기 처리
메인 시트 안에서 상세 시트를 열 수 있고, 뒤로가기 시 올바른 순서로 닫히도록 구현해보세요.

## 다음 학습

- Scaffold와 Theming
- Navigation과 BottomSheet 연동
- 복잡한 UI 상태 관리
