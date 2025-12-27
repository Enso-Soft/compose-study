# Slot API Pattern & Compound Component

## 개념

**Slot API**는 Composable 함수가 다른 Composable Lambda를 파라미터로 받아 "슬롯(빈 공간)"을 만들어,
호출자가 원하는 UI 콘텐츠를 자유롭게 주입할 수 있게 하는 디자인 패턴입니다.

```kotlin
@Composable
fun Button(
    onClick: () -> Unit,
    content: @Composable () -> Unit  // <- 이것이 Slot!
) {
    Row(onClick = onClick) {
        content()  // 호출자가 제공한 UI가 여기에 렌더링됨
    }
}

// 사용
Button(onClick = { }) {
    Icon(Icons.Default.Add)
    Text("Add Item")
}
```

---

## 핵심 특징

### 1. 완전한 유연성
- Props(속성)와 달리 **어떤 Composable이든** 주입 가능
- 스타일, 레이아웃, 동작 모두 커스터마이징 가능

### 2. API 단순화
- 수십 개의 파라미터 대신 슬롯 몇 개로 해결
- 새로운 요구사항에도 API 변경 불필요

### 3. 관심사 분리
- 컨테이너(레이아웃/배치)와 콘텐츠(실제 UI)의 역할 분리
- 재사용성 극대화

---

## Material 컴포넌트의 Slot 사용 예시

### Scaffold
```kotlin
Scaffold(
    topBar = { TopAppBar(...) },           // Slot
    bottomBar = { BottomAppBar(...) },     // Slot
    floatingActionButton = { FAB(...) },   // Slot
    content = { padding -> ... }           // Slot
)
```

### TopAppBar
```kotlin
TopAppBar(
    title = { Text("제목") },                    // Slot
    navigationIcon = { IconButton(...) },        // Slot
    actions = { IconButton(...); IconButton(...) }  // Slot
)
```

### Button
```kotlin
Button(onClick = { }) {
    Icon(...)   // 자유로운 구성
    Text(...)
}
```

---

## Scoped Slots (범위 지정 슬롯)

### 개념
`RowScope`, `ColumnScope`, `BoxScope` 처럼 특정 레이아웃에서만 사용 가능한
Modifier를 제공하는 패턴입니다.

### 예시: RowScope
```kotlin
@Composable
fun Button(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit  // RowScope 제공
) {
    Row(modifier = Modifier.clickable(onClick = onClick)) {
        content()  // 내부에서 weight, align 등 사용 가능
    }
}

// 사용
Button(onClick = { }) {
    Text("왼쪽", modifier = Modifier.weight(1f))  // weight 사용 가능!
    Icon(Icons.Default.ArrowForward)
}
```

### 커스텀 Scope 만들기
```kotlin
interface CardScope {
    fun Modifier.highlight(): Modifier
}

@Composable
fun CustomCard(
    content: @Composable CardScope.() -> Unit
) {
    val scope = object : CardScope {
        override fun Modifier.highlight() = this
            .background(Color.Yellow.copy(alpha = 0.3f))
            .padding(4.dp)
    }

    Card {
        scope.content()
    }
}
```

---

## Compound Component 패턴

### 개념
하나의 컴포넌트를 여러 조각으로 분해하고, 부모가 상태를 관리하면서
자식 컴포넌트들에게 상태를 공유하는 패턴입니다.

### 구현 방식
```kotlin
// 1. Scope 클래스 정의
class AccordionScope(
    val isExpanded: Boolean,
    val onToggle: () -> Unit
)

// 2. 부모 컴포넌트
@Composable
fun Accordion(
    content: @Composable AccordionScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = AccordionScope(expanded) { expanded = !expanded }

    Column {
        scope.content()
    }
}

// 3. 자식 컴포넌트 (확장 함수)
@Composable
fun AccordionScope.Header(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.clickable(onClick = onToggle)
    ) {
        content()
        Icon(if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore)
    }
}

@Composable
fun AccordionScope.Content(content: @Composable () -> Unit) {
    AnimatedVisibility(visible = isExpanded) {
        content()
    }
}

// 4. 사용
Accordion {
    Header { Text("Click to expand") }
    Content { Text("Hidden content here!") }
}
```

---

## layoutId를 활용한 Slot 타입 제약

커스텀 레이아웃에서 특정 슬롯을 식별하고 배치할 때 사용합니다.

```kotlin
enum class SlotId { Header, Content, Footer }

@Composable
fun ThreePartLayout(
    content: @Composable () -> Unit
) {
    Layout(content = content) { measurables, constraints ->
        val header = measurables.find { it.layoutId == SlotId.Header }
        val body = measurables.find { it.layoutId == SlotId.Content }
        val footer = measurables.find { it.layoutId == SlotId.Footer }

        // 각 슬롯을 원하는 위치에 배치
        layout(constraints.maxWidth, constraints.maxHeight) {
            // ...배치 로직
        }
    }
}

// 사용
ThreePartLayout {
    Text("Header", modifier = Modifier.layoutId(SlotId.Header))
    Text("Content", modifier = Modifier.layoutId(SlotId.Content))
    Text("Footer", modifier = Modifier.layoutId(SlotId.Footer))
}
```

---

## Slot vs Props 비교

| 측면 | Props (속성) | Slot (슬롯) |
|------|-------------|------------|
| **유연성** | 제한적 (정해진 타입만) | 완전한 자유 (모든 Composable) |
| **사용 복잡도** | 간단 (값만 전달) | 약간 복잡 (Lambda 정의) |
| **API 크기** | 많은 파라미터 필요 | 적은 파라미터로 해결 |
| **스타일링** | 제한적 | 완전한 제어 가능 |
| **적합한 경우** | 단순한 텍스트, 색상 | 복잡한 커스텀 UI |

### 언제 Props를 사용할까?
```kotlin
// 단순한 값: Props가 적합
@Composable
fun Badge(
    text: String,        // Props
    color: Color = Red   // Props
)
```

### 언제 Slot을 사용할까?
```kotlin
// 복잡한 커스터마이징: Slot이 적합
@Composable
fun Card(
    header: @Composable () -> Unit,   // Slot - 어떤 UI든 가능
    content: @Composable () -> Unit,  // Slot
    footer: @Composable () -> Unit = {}  // Optional Slot
)
```

---

## 실제 재사용 컴포넌트 설계 예시

### 정보 카드 컴포넌트
```kotlin
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    action: @Composable () -> Unit = {}
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                title()
                description()
            }
            action()
        }
    }
}

// 다양한 사용 예시
InfoCard(
    icon = { Icon(Icons.Default.Warning, tint = Color.Red) },
    title = { Text("경고", fontWeight = FontWeight.Bold) },
    description = { Text("시스템에 문제가 발생했습니다") },
    action = { TextButton(onClick = {}) { Text("자세히") } }
)

InfoCard(
    icon = { CircularProgressIndicator(modifier = Modifier.size(24.dp)) },
    title = { Text("로딩 중...") },
    description = { Text("데이터를 불러오고 있습니다") }
)
```

---

## 주의사항

1. **너무 많은 슬롯 피하기**: 슬롯이 많아지면 API가 복잡해짐
2. **DSL 슬롯 지양**: `tabs: TabRowScope.() -> Unit` 보다 일반 Lambda 선호
3. **선택적 슬롯은 기본값 제공**: `action: @Composable () -> Unit = {}`
4. **적절한 Scope 제공**: 필요할 때만 Scope 인터페이스 사용

---

## 연습 문제

1. **기본 Slot API**: AlertBanner 컴포넌트 만들기 (icon, message, action 슬롯)
2. **Scoped Slot**: ButtonBar 컴포넌트에 Modifier.spacer() 제공하기
3. **Compound Component**: Accordion 컴포넌트로 Header/Content 상태 공유하기

---

## 다음 학습

- [Custom Layout](../custom_layout/src/main/java/com/example/custom_layout/README.md) - Layout Composable, MeasurePolicy
- [Composition Local](../composition_local/src/main/java/com/example/composition_local/README.md) - 컴포지션 트리를 통한 값 전달

---

## 참고 자료

- [Compose layout basics - Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics)
- [Best Practices for Composition Patterns in Jetpack Compose - Droidcon](https://www.droidcon.com/2025/01/10/best-practices-for-composition-patterns-in-jetpack-compose/)
- [API Guidelines for components in Jetpack Compose](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-component-api-guidelines.md)
- [Slotting in with Compose UI - Chris Banes](https://chrisbanes.me/posts/slotting-in-with-compose-ui/)
