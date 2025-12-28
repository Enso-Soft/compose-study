# LazyColumn 성능 최적화: key, contentType, animateItem

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `lazy_list` | LazyColumn/LazyRow의 기본 사용법 | [📚 학습하기](../../list/lazy_list/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Lazy Layouts**는 대량의 데이터를 효율적으로 표시하기 위한 Compose의 핵심 컴포넌트입니다.
화면에 보이는 아이템만 컴포지션(Composition: UI 요소를 메모리에 구성하는 과정)하여 메모리와 성능을 최적화합니다.

> **사전 지식**: 이 모듈은 LazyColumn/LazyRow의 기본 사용법을 알고 있다고 가정합니다.
> 기본 사용법은 [Compose 공식 문서](https://developer.android.com/develop/ui/compose/lists)를 참조하세요.

### Lazy Layout 종류

| 컴포넌트 | 설명 | 사용 시점 |
|---------|------|----------|
| `LazyColumn` | 수직 스크롤 리스트 | 일반적인 목록 UI |
| `LazyRow` | 수평 스크롤 리스트 | 가로 스크롤 카드 |
| `LazyVerticalGrid` | 수직 그리드 | 갤러리, 상품 목록 |
| `LazyHorizontalGrid` | 수평 그리드 | 가로 스크롤 그리드 |
| `LazyVerticalStaggeredGrid` | 가변 높이 그리드 | Pinterest 스타일 |

> **Note**: 이 모듈에서는 `LazyColumn`을 중심으로 설명하지만,
> 모든 최적화 기법(key, contentType, animateItem)은 다른 Lazy Layout에도 동일하게 적용됩니다.

---

## 문제 상황: 왜 성능 최적화가 필요한가?

### 시나리오: 할 일 목록 앱

사용자가 할 일 목록 앱을 사용하고 있습니다:

1. 사용자가 "장보기" 항목의 메모 필드에 "우유, 빵, 계란"을 입력 중
2. 이때 "운동하기" 항목을 삭제
3. **예상**: "장보기"의 메모는 그대로 유지되어야 함
4. **실제**: 메모가 사라지거나 다른 항목으로 이동!

### 왜 이런 일이 발생할까요?

LazyColumn을 **기본 설정으로만** 사용하면:

```kotlin
// 문제가 있는 코드
LazyColumn {
    items(todos) { todo ->  // key가 없음!
        TodoItem(todo)
    }
}
```

Compose는 아이템을 **인덱스(순서)**로만 구분합니다.

마치 학교에서 학생을 "학번" 대신 "자리 번호"로만 관리하는 것과 같습니다:
- 2번 자리 학생이 전학을 가면
- 3번 자리 학생이 2번 자리로 이동
- 하지만 Compose는 "2번 자리 = 같은 학생"이라고 착각!

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 전체 Recomposition | 아이템 하나 삭제 시 **모든 아이템**이 다시 그려짐 |
| 상태 손실 | TextField에 입력 중이던 **내용이 사라짐** |
| 애니메이션 불가 | 삭제/추가 시 **애니메이션이 작동하지 않음** |
| 성능 저하 | 스크롤 시 **불필요한 화면 갱신** 발생 |

> **Recomposition이란?** 상태가 변경되었을 때 Compose가 UI를 다시 그리는 과정입니다.
> 필요한 부분만 다시 그려야 효율적이지만, key가 없으면 전체를 다시 그리게 됩니다.

---

## 해결책: 성능 최적화의 핵심 요소

### 1. key 파라미터 - "학번"을 부여하세요

아이템의 **고유 식별자**를 제공하여 Compose가 아이템을 정확히 추적할 수 있게 합니다.

학생을 "자리 번호"가 아닌 "학번"으로 관리하는 것처럼,
각 아이템에 고유한 ID를 부여하면 Compose가 "이 아이템은 이동한 것"인지 "새로 추가된 것"인지 정확히 알 수 있습니다.

```kotlin
LazyColumn {
    items(
        items = todos,
        key = { todo -> todo.id }  // 고유 ID 사용 (학번 역할)
    ) { todo ->
        TodoItem(todo)
    }
}
```

**효과:**
- 아이템 삭제/추가 시 **변경된 부분만** Recomposition
- 순서 변경 시 이동만 처리 (최대 40% 성능 향상)
- `animateItem()` 애니메이션 활성화
- 내부 상태(TextField 등) 보존

**key 요구사항:**
- Bundle에 저장 가능한 타입 (Int, String, Long, Parcelable 등)
- 고유하고 안정적이어야 함 (**절대 인덱스 사용 금지!**)

### 2. contentType 파라미터 - "반"을 구분하세요

여러 타입의 아이템이 섞인 리스트에서 **같은 타입끼리만 UI 구성요소를 재사용**하도록 합니다.

학교에서 "1반 교실"은 1반 학생만, "2반 교실"은 2반 학생만 사용하는 것처럼,
Header UI는 Header 데이터에만, Post UI는 Post 데이터에만 재사용됩니다.

```kotlin
LazyColumn {
    items(
        items = feedItems,
        key = { it.id },
        contentType = { item ->
            when (item) {
                is Header -> "header"  // Header 타입
                is Post -> "post"      // Post 타입
                is Ad -> "ad"          // Ad 타입
            }
        }
    ) { item ->
        // ...
    }
}
```

**효과:**
- Header UI가 Post 데이터에 잘못 재사용되는 것 방지
- 타입별 레이아웃 최적화 (RecyclerView의 ViewType과 유사)
- 스크롤 시 깜빡임 감소

### 3. animateItem()

아이템의 **추가/삭제/이동 애니메이션**을 활성화합니다.

```kotlin
LazyColumn {
    items(
        items = todos,
        key = { it.id }
    ) { todo ->
        TodoItem(
            todo = todo,
            modifier = Modifier.animateItem()  // 애니메이션 활성화
        )
    }
}
```

**효과:**
- 아이템 추가 시 fadeIn 애니메이션
- 아이템 삭제 시 fadeOut 애니메이션
- 아이템 위치 변경 시 이동 애니메이션

**주의:** `animateItem()`이 동작하려면 **반드시 key를 제공**해야 합니다!

> **2025년 업데이트**: `animateItemPlacement()`는 deprecated되었으며,
> `animateItem()`으로 대체되었습니다.

---

## 완전한 해결책: 모든 최적화 기법 적용

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

### 왜 이 코드가 효과적인가?

| 기법 | 역할 | 비유 |
|------|------|------|
| **key** | 각 아이템을 고유하게 식별 | 학번으로 학생 구분 |
| **contentType** | 같은 타입끼리만 UI 재사용 | 반별로 교실 구분 |
| **derivedStateOf** | 값이 실제로 변할 때만 갱신 | 성적이 바뀔 때만 성적표 재발급 |
| **animateItem()** | 부드러운 추가/삭제 애니메이션 | 자리 이동 시 천천히 이동 |

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

## 고급 최적화: derivedStateOf

스크롤 상태에 따라 UI를 업데이트할 때, `derivedStateOf`를 사용하면 불필요한 Recomposition을 방지할 수 있습니다.

### 문제 상황: 불필요한 화면 갱신

```kotlin
val listState = rememberLazyListState()

// 매 프레임마다 Recomposition 발생!
val showScrollButton = listState.firstVisibleItemIndex > 0
```

사용자가 스크롤할 때마다 `firstVisibleItemIndex`가 0 -> 1 -> 2 -> 3... 으로 변경됩니다.
이 값을 직접 읽으면 **매번** 화면이 다시 그려집니다.

하지만 실제로 우리가 필요한 것은 "0보다 큰가?" (true/false)뿐입니다.
인덱스가 1에서 2로 바뀌든, 2에서 100으로 바뀌든 결과는 여전히 `true`입니다.

### 해결책: derivedStateOf

```kotlin
val listState = rememberLazyListState()

// 결과값(true/false)이 변할 때만 Recomposition
val showScrollButton by remember {
    derivedStateOf {
        listState.firstVisibleItemIndex > 0
    }
}
```

`derivedStateOf`는 **파생된 결과값이 실제로 변경될 때만** Recomposition을 트리거합니다.

| 인덱스 변화 | showScrollButton | Recomposition |
|------------|------------------|---------------|
| 0 -> 1 | false -> true | O (발생) |
| 1 -> 2 | true -> true | X (발생 안함) |
| 2 -> 100 | true -> true | X (발생 안함) |
| 1 -> 0 | true -> false | O (발생) |

---

## 주의사항

### 1. 인덱스를 key로 사용하지 마세요 (가장 중요!)

```kotlin
// 잘못된 예 - 절대 하지 마세요!
LazyColumn {
    itemsIndexed(todos) { index, todo ->
        TodoItem(
            todo = todo,
            // index를 key처럼 사용하면 문제 발생
        )
    }
}

// 올바른 예
LazyColumn {
    items(
        items = todos,
        key = { todo -> todo.id }  // 고유 ID 사용
    ) { todo ->
        TodoItem(todo)
    }
}
```

인덱스를 key로 사용하면:
- 아이템 삭제 시 뒤의 모든 아이템이 재구성됨
- 상태가 잘못된 아이템에 연결됨
- 애니메이션이 올바르게 동작하지 않음

### 2. 0픽셀 크기 아이템 피하기
- 이미지 로딩 시 `Modifier.size()` 지정 필수
- Lazy Layout이 뷰포트 용량을 올바르게 계산하도록 함

### 3. 같은 방향 스크롤 중첩 금지
```kotlin
// 잘못된 예: LazyColumn 안에 세로 스크롤
LazyColumn {
    item { Column(Modifier.verticalScroll()) { } }
}
```

### 4. 무거운 작업은 Composition 밖에서
- 리스트 정렬, 필터링은 `remember`로 캐싱
- 이미지 디코딩, JSON 파싱은 ViewModel에서 처리

```kotlin
// 잘못된 예 - 매번 정렬
LazyColumn {
    items(contacts.sortedBy { it.name }) { ... }
}

// 올바른 예 - 정렬 결과 캐싱
val sortedContacts = remember(contacts) {
    contacts.sortedBy { it.name }
}
LazyColumn {
    items(sortedContacts) { ... }
}
```

---

## 2025년 최신 업데이트

### animateItem() - animateItemPlacement() 대체

```kotlin
// 기존 (deprecated)
Modifier.animateItemPlacement()

// 신규 (권장)
Modifier.animateItem(
    fadeInSpec = tween(durationMillis = 250),   // 등장 애니메이션
    fadeOutSpec = tween(durationMillis = 250),  // 퇴장 애니메이션
    placementSpec = spring()                     // 이동 애니메이션
)
```

`animateItem()`은 아이템의 추가, 삭제, 이동 **세 가지 모두** 애니메이션을 지원합니다.

### Pausable Composition

복잡한 컴포지션도 프레임 드롭 없이 처리할 수 있게 되었습니다.
Compose 런타임이 자동으로 컴포지션을 여러 프레임에 나누어 처리합니다.

### LazyLayoutCacheWindow

프리페치할 아이템 수를 조절하여 메모리와 성능의 균형을 맞출 수 있습니다.

### 성능 테스트 시 주의

```kotlin
// Release 모드에서만 성능 측정!
// Debug 모드에서는 Lazy layout 스크롤이 느리게 보일 수 있습니다.
```

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
