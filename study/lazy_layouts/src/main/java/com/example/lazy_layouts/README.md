# Lazy Layouts 심화 학습

## 개념

**Lazy Layouts**는 대량의 데이터를 효율적으로 표시하기 위한 Compose의 핵심 컴포넌트입니다.
화면에 보이는 아이템만 컴포지션하여 메모리와 성능을 최적화합니다.

### 주요 컴포넌트

| 컴포넌트 | 설명 |
|---------|------|
| `LazyColumn` | 수직 스크롤 리스트 |
| `LazyRow` | 수평 스크롤 리스트 |
| `LazyVerticalGrid` | 수직 그리드 (고정/적응형 열) |
| `LazyHorizontalGrid` | 수평 그리드 |
| `LazyVerticalStaggeredGrid` | 가변 높이 그리드 (Pinterest 스타일) |

---

## 핵심 특징: 성능 최적화의 3요소

### 1. key 파라미터

아이템의 **고유 식별자**를 제공하여 Compose가 아이템을 추적할 수 있게 합니다.

```kotlin
LazyColumn {
    items(
        items = todos,
        key = { todo -> todo.id }  // 고유 ID 사용
    ) { todo ->
        TodoItem(todo)
    }
}
```

**효과:**
- 아이템 삭제/추가 시 불필요한 recomposition 방지
- 순서 변경 시 이동만 처리 (최대 40% 성능 향상)
- `animateItem()` 애니메이션 활성화
- 내부 상태(TextField 등) 보존

**key 요구사항:**
- Bundle에 저장 가능한 타입 (Int, String, Long, Parcelable 등)
- 고유하고 안정적이어야 함 (절대 인덱스 사용 금지!)

### 2. contentType 파라미터

여러 타입의 아이템이 섞인 리스트에서 **같은 타입끼리만 컴포지션을 재사용**하도록 합니다.

```kotlin
LazyColumn {
    items(
        items = feedItems,
        key = { it.id },
        contentType = { item ->
            when (item) {
                is Header -> "header"
                is Post -> "post"
                is Ad -> "ad"
            }
        }
    ) { item ->
        // ...
    }
}
```

**효과:**
- Header 컴포지션이 Post로 재사용되는 것 방지
- 타입별 레이아웃 최적화
- 스크롤 시 깜빡임 감소

### 3. derivedStateOf

**스크롤 상태 기반 UI 업데이트**를 최적화합니다.

```kotlin
val listState = rememberLazyListState()

// 값이 실제로 변할 때만 recomposition
val showScrollButton by remember {
    derivedStateOf {
        listState.firstVisibleItemIndex > 0
    }
}
```

---

## 문제 상황: key 없이 리스트 사용

### 잘못된 코드 예시

```kotlin
LazyColumn {
    items(todos) { todo ->  // key 없음!
        TodoItemWithNote(
            todo = todo,
            onDelete = { /* 삭제 */ }
        )
    }
}
```

### 발생하는 문제점

1. **전체 재구성**: 아이템 삭제 시 모든 아이템이 다시 그려짐
2. **상태 손실**: TextField에 입력 중이던 내용이 사라짐
3. **애니메이션 불가**: `animateItem()`이 작동하지 않음
4. **성능 저하**: 스크롤 시 불필요한 recomposition 발생

---

## 해결책: key, contentType, derivedStateOf 사용

### 올바른 코드

```kotlin
@Composable
fun OptimizedList() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // derivedStateOf로 스크롤 상태 최적화
    val showScrollButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 2 }
    }

    Box {
        LazyColumn(state = listState) {
            items(
                items = todos,
                key = { it.id },           // 고유 키 제공
                contentType = { "todo" }   // 타입 지정
            ) { todo ->
                TodoItem(
                    todo = todo,
                    modifier = Modifier.animateItem()  // 애니메이션 활성화
                )
            }
        }

        // 스크롤 버튼
        AnimatedVisibility(visible = showScrollButton) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, "맨 위로")
            }
        }
    }
}
```

### 해결되는 이유

1. **key**: Compose가 각 아이템을 고유하게 식별하여 변경된 부분만 업데이트
2. **contentType**: 같은 타입의 컴포지션끼리만 재사용
3. **derivedStateOf**: 불리언 값이 실제로 변할 때만 recomposition 발생

---

## 사용 시나리오

### 1. 할 일 목록 (삭제/완료 기능)
- key로 아이템 식별
- animateItem()으로 삭제 애니메이션

### 2. 채팅 앱 (텍스트/이미지 혼합)
- contentType으로 메시지 타입 구분
- 효율적인 컴포지션 재사용

### 3. 무한 스크롤 피드
- Paging3 + LazyColumn 통합
- `lazyPagingItems.itemKey { it.id }` 사용

### 4. 스크롤 위치 기반 UI
- derivedStateOf로 "맨 위로" 버튼 표시/숨김
- snapshotFlow로 스크롤 분석 이벤트 전송

---

## 주의사항

1. **인덱스를 key로 사용하지 마세요**
   ```kotlin
   // 잘못된 예
   items(todos, key = { index, _ -> index })

   // 올바른 예
   items(todos, key = { it.id })
   ```

2. **0픽셀 크기 아이템 피하기**
   - 이미지 로딩 시 `Modifier.size()` 지정 필수
   - Lazy Layout이 뷰포트 용량을 올바르게 계산하도록 함

3. **같은 방향 스크롤 중첩 금지**
   ```kotlin
   // 잘못된 예: LazyColumn 안에 세로 스크롤
   LazyColumn {
       item { Column(Modifier.verticalScroll()) { } }
   }
   ```

4. **무거운 작업은 Composition 밖에서**
   - 이미지 디코딩, JSON 파싱 등은 ViewModel에서 처리

---

## 2025년 최신 업데이트

- **Pausable Composition**: 복잡한 컴포지션도 프레임 드롭 없이 처리
- **LazyLayoutCacheWindow**: 프리페치 아이템 수 조절 가능
- **animateItem()**: 아이템 리오더링 애니메이션 지원

---

## 연습 문제

### 연습 1: key 파라미터 추가 (기초)
연락처 목록에 key를 추가하여 삭제 시 성능 최적화

### 연습 2: contentType으로 혼합 리스트 최적화 (중급)
채팅 메시지 리스트에서 텍스트/이미지 메시지 타입 구분

### 연습 3: derivedStateOf로 스크롤 감지 최적화 (고급)
스크롤 위치에 따른 앱바 표시/숨김 최적화

---

## 다음 학습

- **Gesture & Pointer Input**: 터치, 드래그, 스와이프 처리
- **Canvas & Graphics**: 커스텀 드로잉
- **CompositionLocal**: 암시적 데이터 전달
