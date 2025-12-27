# Drag & Drop 학습

## 개념

**Drag & Drop**은 사용자가 화면의 요소를 드래그하여 다른 위치나 다른 앱으로 이동시키는 인터랙션입니다.
Jetpack Compose에서는 2024년 1월부터 `Modifier.dragAndDropSource`와 `Modifier.dragAndDropTarget`을 통해
시스템 통합 드래그 앤 드롭 기능을 제공합니다.

### 기존 Gesture와의 차이점

| 항목 | pointerInput + detectDragGestures | dragAndDropSource/Target |
|------|-----------------------------------|--------------------------|
| 목적 | 요소 이동 (오프셋 변경) | 데이터 전송 |
| 범위 | 앱 내부만 | 앱 간 공유 가능 |
| 시각적 피드백 | 수동 구현 필요 | 시스템 드래그 섀도우 자동 제공 |
| 데이터 형식 | 없음 | ClipData (표준화) |
| 접근성 | 수동 구현 필요 | 시스템 통합 |

## 핵심 특징

### 1. dragAndDropSource Modifier

드래그 가능한 요소를 정의합니다. Offset 파라미터를 받아서 드래그 시작 위치 정보를 활용할 수 있습니다.

```kotlin
// 기본 사용 (Offset 무시)
Modifier.dragAndDropSource { _ ->
    DragAndDropTransferData(
        ClipData.newPlainText("label", "전송할 텍스트")
    )
}

// Offset 활용
Modifier.dragAndDropSource { offset ->
    Log.d("Drag", "드래그 시작 위치: $offset")
    DragAndDropTransferData(
        ClipData.newPlainText("label", "전송할 텍스트")
    )
}
```

### 2. dragAndDropTarget Modifier

드롭 대상 영역을 정의합니다.

```kotlin
val callback = remember {
    object : DragAndDropTarget {
        override fun onDrop(event: DragAndDropEvent): Boolean {
            val text = event.toAndroidDragEvent().clipData
                .getItemAt(0).text.toString()
            // 데이터 처리
            return true // 드롭 처리 완료
        }
    }
}

Modifier.dragAndDropTarget(
    shouldStartDragAndDrop = { true },
    target = callback
)
```

### 3. ClipData 타입

| 타입 | 생성 방법 | 용도 |
|------|----------|------|
| 텍스트 | `ClipData.newPlainText(label, text)` | 문자열 전송 |
| HTML | `ClipData.newHtmlText(label, text, html)` | 서식 있는 텍스트 |
| URI | `ClipData.newUri(resolver, label, uri)` | 파일, 이미지 |
| Intent | `ClipData.newIntent(label, intent)` | Intent 전달 |

### 4. DragAndDropTarget 콜백

```kotlin
object : DragAndDropTarget {
    override fun onStarted(event: DragAndDropEvent) {
        // 드래그 세션 시작 (이 타겟이 수신 가능할 때)
    }

    override fun onEntered(event: DragAndDropEvent) {
        // 드래그 아이템이 타겟 영역 진입
    }

    override fun onMoved(event: DragAndDropEvent) {
        // 타겟 영역 내에서 이동 중
    }

    override fun onExited(event: DragAndDropEvent) {
        // 드래그 아이템이 타겟 영역 이탈
    }

    override fun onDrop(event: DragAndDropEvent): Boolean {
        // 실제 드롭 발생! 데이터 처리
        return true // 처리 완료 시 true
    }

    override fun onEnded(event: DragAndDropEvent) {
        // 드래그 세션 종료
    }
}
```

## 문제 상황: 수동 드래그 구현의 한계

### 잘못된 코드 예시

```kotlin
// 문제: pointerInput으로 수동 구현
var offset by remember { mutableStateOf(Offset.Zero) }

Box(
    modifier = Modifier
        .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                offset += dragAmount
            }
        }
)
```

### 발생하는 문제점

1. **데이터 전송 불가**: 드래그 위치만 변경, 데이터 전달 메커니즘 없음
2. **드롭 대상 판별 수동**: 어느 영역에 드롭했는지 직접 계산 필요
3. **앱 간 공유 불가**: 다른 앱으로 드래그 불가능
4. **시스템 통합 부재**: 드래그 섀도우, 접근성 미지원
5. **복잡한 상태 관리**: 드래그 중, 호버링, 드롭 성공/실패 등

## 해결책: dragAndDropSource/Target 사용

### 올바른 코드

```kotlin
@Composable
fun DragAndDropDemo() {
    var droppedItems by remember { mutableStateOf(listOf<String>()) }

    Row {
        // 드래그 소스 (trailing lambda, Offset 파라미터 무시)
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Blue)
                .dragAndDropSource { _ ->
                    DragAndDropTransferData(
                        ClipData.newPlainText("item", "Blue Box")
                    )
                }
        )

        // 드롭 타겟
        val callback = remember {
            object : DragAndDropTarget {
                override fun onDrop(event: DragAndDropEvent): Boolean {
                    val text = event.toAndroidDragEvent().clipData
                        .getItemAt(0).text.toString()
                    droppedItems = droppedItems + text
                    return true
                }
            }
        }

        Box(
            modifier = Modifier
                .size(150.dp)
                .background(Color.LightGray)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { true },
                    target = callback
                )
        ) {
            droppedItems.forEach { Text(it) }
        }
    }
}
```

### 해결되는 이유

1. **표준화된 데이터 전송**: ClipData로 명확한 데이터 교환
2. **자동 영역 판별**: Modifier가 타겟 영역 진입/이탈 자동 감지
3. **앱 간 드래그 지원**: DRAG_FLAG_GLOBAL로 다른 앱과 공유
4. **시스템 드래그 섀도우**: 자동으로 드래그 시각화
5. **간결한 콜백 구조**: 이벤트별 명확한 콜백

## 사용 시나리오

### 1. 앱 내 카드 정렬
```kotlin
// 카드를 다른 영역으로 드래그하여 분류
items.forEach { item ->
    DraggableCard(
        item = item,
        modifier = Modifier.dragAndDropSource { _ ->
            DragAndDropTransferData(
                ClipData.newPlainText("item_id", item.id)
            )
        }
    )
}
```

### 2. 시각적 피드백 추가
```kotlin
var isHovering by remember { mutableStateOf(false) }

val callback = remember {
    object : DragAndDropTarget {
        override fun onEntered(event: DragAndDropEvent) {
            isHovering = true
        }
        override fun onExited(event: DragAndDropEvent) {
            isHovering = false
        }
        override fun onDrop(event: DragAndDropEvent): Boolean {
            isHovering = false
            // 드롭 처리
            return true
        }
    }
}

Box(
    modifier = Modifier
        .background(
            if (isHovering) Color.Green.copy(alpha = 0.3f)
            else Color.Gray
        )
        .dragAndDropTarget(...)
)
```

### 3. 앱 간 드래그 앤 드롭
```kotlin
// 다른 앱으로 이미지 공유
Modifier.dragAndDropSource { _ ->
    DragAndDropTransferData(
        ClipData.newUri(contentResolver, "Image", imageUri),
        flags = View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ
    )
}

// 다른 앱에서 이미지 수신
val callback = remember {
    object : DragAndDropTarget {
        override fun onDrop(event: DragAndDropEvent): Boolean {
            val dragEvent = event.toAndroidDragEvent()
            // 권한 요청 필요 시
            // activity.requestDragAndDropPermissions(dragEvent)
            val uri = dragEvent.clipData.getItemAt(0).uri
            // URI로 이미지 로드
            return true
        }
    }
}
```

## 고급 기능

### 1. 드래그 섀도우 커스터마이징

기본 드래그 섀도우 대신 커스텀 시각 효과를 적용할 수 있습니다.

```kotlin
// drawDragDecoration 파라미터로 커스텀 드래그 섀도우 정의
Box(
    modifier = Modifier
        .size(80.dp)
        .background(color)
        .dragAndDropSource(
            drawDragDecoration = {
                // 드래그 중 표시될 커스텀 그래픽
                drawRoundRect(
                    color = color.copy(alpha = 0.8f),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
        ) { _ ->
            DragAndDropTransferData(
                ClipData.newPlainText("color", color.toString())
            )
        }
)
```

### 2. 스크롤 영역 내 드래그 처리

`verticalScroll`이나 `LazyColumn` 내에서 드래그할 때 제스처 충돌이 발생할 수 있습니다.

```kotlin
// 해결책 1: 드래그 소스를 스크롤 영역 외부에 배치
Column {
    // 스크롤되지 않는 드래그 소스 영역
    Row(modifier = Modifier.padding(16.dp)) {
        DraggableItems()  // dragAndDropSource 사용
    }

    // 스크롤되는 드롭 타겟 영역
    LazyColumn {
        items(dropTargets) { target ->
            DropTargetItem(target)  // dragAndDropTarget 사용
        }
    }
}

// 해결책 2: Long press로 드래그 시작 (deprecated API 사용 시)
// 최신 API는 자동으로 long press 감지하므로 일반적으로 문제없음
```

**팁**: 스크롤과 드래그가 충돌하면, 드래그 소스를 스크롤 컨테이너 외부에 배치하는 것이 가장 확실한 해결책입니다.

## 주의사항

1. **callback은 remember로 캐싱**: 매 recomposition마다 새 객체 생성 방지
   ```kotlin
   val callback = remember { object : DragAndDropTarget { ... } }
   ```

2. **앱 간 드래그 시 권한 처리**: URI 수신 시 `requestDragAndDropPermissions()` 필요

3. **shouldStartDragAndDrop 필터링**: 원하는 MIME 타입만 수신
   ```kotlin
   shouldStartDragAndDrop = { event ->
       event.toAndroidDragEvent().clipDescription
           .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
   }
   ```

4. **리스트 리오더링은 별도 라이브러리 권장**:
   - dragAndDropSource/Target은 "데이터 전송"용
   - 리스트 아이템 순서 변경은 [Reorderable](https://github.com/Calvin-LL/Reorderable) 라이브러리 사용

5. **최소 API 레벨**: dragAndDropSource/Target은 API 24+ 지원

## 연습 문제

1. **Practice 1: 기본 Drag & Drop** - 이모지 카드를 상자로 드래그
2. **Practice 2: 시각적 피드백** - 드래그 진입 시 타겟 하이라이트
3. **Practice 3: 칸반 보드** - 태스크를 열 간에 이동

## 다음 학습

- **LazyLayout 리오더링**: Reorderable 라이브러리를 사용한 드래그 정렬
- **멀티 윈도우 드래그**: 분할 화면에서의 앱 간 드래그
- **접근성 고려사항**: 드래그 앤 드롭의 접근성 대안

## 참고 자료

- [Drag and drop - Android Developers](https://developer.android.com/develop/ui/compose/touch-input/user-interactions/drag-and-drop)
- [Drag and Drop in Compose Codelab](https://developer.android.com/codelabs/codelab-dnd-compose)
- [dragAndDropSource - Composables.com](https://composables.com/foundation/draganddropsource)
- [dragAndDropTarget - Composables.com](https://composables.com/foundation/draganddroptarget)
