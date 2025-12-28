# Slot API Pattern 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `screen_and_component` | Screen과 Component 분리, 재사용 가능한 컴포넌트 설계 | [📚 학습하기](../../architecture/screen_and_component/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**Slot API**는 Composable 함수가 다른 Composable Lambda를 파라미터로 받아 "슬롯(빈 공간)"을 만들어,
호출자가 원하는 UI 콘텐츠를 자유롭게 주입할 수 있게 하는 디자인 패턴입니다.

### 왜 Slot API를 배워야 하는가?

1. **Material 컴포넌트의 핵심**: `Button`, `Scaffold`, `TopAppBar` 등 모든 Material 컴포넌트가 Slot API를 사용
2. **재사용 컴포넌트 설계 필수 지식**: 유연하고 확장 가능한 컴포넌트를 만들기 위한 표준 패턴
3. **Props의 한계 극복**: 파라미터 폭발(Parameter Explosion) 문제를 우아하게 해결

### 학습 목표

- Slot API의 기본 개념과 사용법 이해
- Scoped Slots와 Compound Component 패턴 활용
- 공식 가이드라인에 따른 베스트 프랙티스 적용

---

## 기본 사용법

가장 단순한 Slot API 예시입니다:

```kotlin
@Composable
fun Button(
    onClick: () -> Unit,
    content: @Composable () -> Unit  // <- 이것이 Slot!
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        content()  // 호출자가 제공한 UI가 여기에 렌더링됨
    }
}

// 사용 - Kotlin의 trailing lambda 문법 활용
Button(onClick = { }) {
    Icon(Icons.Default.Add, contentDescription = null)
    Text("Add Item")
}
```

**핵심 포인트:**
- `content: @Composable () -> Unit` 타입의 파라미터가 슬롯
- 호출자는 중괄호 `{ }` 안에 원하는 UI를 자유롭게 구성
- 아이콘만, 텍스트만, 둘 다, 또는 어떤 Composable이든 가능

---

## 핵심 기능

### 기능 1: 기본 Slot API

#### 단일 슬롯

가장 기본적인 형태로, 하나의 `content` 슬롯을 제공합니다:

```kotlin
@Composable
fun SimpleCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(modifier = modifier) {
        content()
    }
}
```

#### 다중 슬롯

여러 개의 슬롯을 통해 구조화된 레이아웃을 제공합니다:

```kotlin
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    action: @Composable () -> Unit = {}  // 선택적 슬롯
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
    // action 슬롯 생략 - 기본값 {} 사용
)
```

---

### 기능 2: Scoped Slots

`RowScope`, `ColumnScope`, `BoxScope` 처럼 특정 레이아웃에서만 사용 가능한
Modifier를 제공하는 패턴입니다.

#### 기본 사용법

```kotlin
@Composable
fun FlexibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit  // RowScope 제공
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        content()  // 내부에서 weight, align 등 사용 가능
    }
}

// 사용 - RowScope의 weight 활용
FlexibleButton(onClick = { }) {
    Text("왼쪽 정렬", modifier = Modifier.weight(1f))  // weight 사용 가능!
    Icon(Icons.Default.ArrowForward, contentDescription = null)
}
```

#### 커스텀 Scope 만들기

```kotlin
interface CardScope {
    fun Modifier.highlight(): Modifier
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable CardScope.() -> Unit
) {
    val scope = object : CardScope {
        override fun Modifier.highlight() = this
            .background(Color.Yellow.copy(alpha = 0.3f))
            .padding(4.dp)
    }

    Card(modifier = modifier) {
        scope.content()
    }
}

// 사용
CustomCard {
    Text(
        "강조된 텍스트",
        modifier = Modifier.highlight()  // 커스텀 Modifier 사용
    )
}
```

---

### 기능 3: Compound Component 패턴

하나의 컴포넌트를 여러 조각으로 분해하고, 부모가 상태를 관리하면서
자식 컴포넌트들에게 상태를 공유하는 패턴입니다.

#### 구현 방식

```kotlin
// 1. Scope 클래스 정의 - 공유할 상태와 콜백
class AccordionScope(
    val isExpanded: Boolean,
    val onToggle: () -> Unit
)

// 2. 부모 컴포넌트 - 상태 관리
@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    content: @Composable AccordionScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = AccordionScope(
        isExpanded = expanded,
        onToggle = { expanded = !expanded }
    )

    Card(modifier = modifier) {
        Column {
            scope.content()
        }
    }
}

// 3. 자식 컴포넌트 (확장 함수)
@Composable
fun AccordionScope.Header(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        Icon(
            imageVector = if (isExpanded)
                Icons.Default.KeyboardArrowUp
            else
                Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "접기" else "펼치기"
        )
    }
}

@Composable
fun AccordionScope.Content(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        content()
    }
}

// 4. 사용 - 선언적이고 직관적인 API
Accordion {
    Header { Text("Click to expand") }
    Content { Text("Hidden content here!") }
}
```

**Compound Component의 장점:**
- 부모(Accordion)가 상태(isExpanded) 관리
- 자식(Header, Content)이 상태에 자동 접근
- 깔끔한 선언적 API

---

## Material 컴포넌트의 Slot 활용

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

## 베스트 프랙티스

Google의 [Compose Component API Guidelines](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-component-api-guidelines.md)에 기반한 권장사항입니다.

### 1. 적절한 Layout Scope 제공

```kotlin
// 좋은 예: RowScope 제공으로 weight 사용 가능
@Composable
fun Button(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(modifier = Modifier.clickable(onClick = onClick)) {
        content()
    }
}

// 사용자가 편리하게 레이아웃 구성
Button(onClick = { }) {
    Icon(...)
    Text(...)
    Spacer(modifier = Modifier.weight(1f))  // weight 사용 가능!
}
```

### 2. content 파라미터는 마지막에 배치

```kotlin
// 좋은 예: trailing lambda 문법 활용 가능
@Composable
fun SimpleRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit  // 마지막 파라미터
)

// 사용 시 깔끔한 문법
SimpleRow(modifier = Modifier.padding(8.dp)) {
    Text("Hello")
}
```

### 3. 선택적 슬롯에 기본값 제공

```kotlin
@Composable
fun InfoCard(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    action: @Composable () -> Unit = {}  // 기본값으로 빈 블록
)
```

### 4. Layout을 사용한 슬롯 생명주기 보존

```kotlin
// 좋은 예: Layout으로 상태 변경 시 슬롯 콘텐츠 유지
@Composable
fun PreferenceItem(
    checked: Boolean,
    content: @Composable () -> Unit
) {
    Layout({
        Text("Preference item")
        content()
    }) { measurables, constraints ->
        // checked 변경 시에도 content 인스턴스 유지
    }
}
```

---

## 안티패턴

### 1. DSL 슬롯 사용 지양

```kotlin
// 나쁜 예: DSL 기반 슬롯 - 복잡하고 유연성 떨어짐
@Composable
fun TabRow(
    tabs: TabRowScope.() -> Unit
)

interface TabRowScope {
    fun tab(string: String)
    fun tab(tabContent: @Composable () -> Unit)
}

// 좋은 예: 일반 Composable 슬롯 - 단순하고 유연함
@Composable
fun TabRow(
    tabs: @Composable () -> Unit
)

@Composable
fun Tab(...)

// 사용
TabRow {
    tabsData.forEach { data ->
        Tab(...)
    }
}
```

### 2. 조건부 렌더링에서 슬롯 생명주기 파괴

```kotlin
// 나쁜 예: checked 변경 시 content가 dispose되고 다시 compose됨
@Composable
fun PreferenceItem(
    checked: Boolean,
    content: @Composable () -> Unit
) {
    if (checked) {
        Row {
            Text("Checked")
            content()  // checked 변경 시 재생성됨!
        }
    } else {
        Column {
            Text("Unchecked")
            content()  // 내부 상태 손실!
        }
    }
}
```

### 3. 너무 많은 슬롯

```kotlin
// 나쁜 예: 슬롯이 너무 많아 API가 복잡해짐
@Composable
fun ComplexCard(
    header: @Composable () -> Unit,
    subheader: @Composable () -> Unit,
    leadingIcon: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    content: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    badge: @Composable () -> Unit
)
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

## 연습 문제

### 연습 1: 기본 Slot API

**AlertBanner 컴포넌트 만들기**
- icon, message, action 세 개의 슬롯
- action은 선택적 슬롯 (기본값 = {})
- Row 레이아웃으로 배치

### 연습 2: Scoped Slot

**ButtonBar 컴포넌트에 ButtonBarScope 제공하기**
- `Modifier.spacer()` 확장 함수 제공 (weight(1f) 적용)
- RowScope를 상속받아 기존 Row Modifier도 사용 가능

### 연습 3: Compound Component

**CollapsibleSection 컴포넌트 구현하기**
- CollapsibleSectionScope로 상태 공유
- Title, Body 확장 함수 구현
- Title 클릭 시 Body 토글

---

## 다음 학습

- [Custom Layout](../../layout/custom_layout/README.md) - Layout Composable, MeasurePolicy
- [Composition Local](../../architecture/composition_local/README.md) - 컴포지션 트리를 통한 값 전달

---

## 참고 자료

- [Compose layout basics - Android Developers](https://developer.android.com/develop/ui/compose/layouts/basics)
- [API Guidelines for components in Jetpack Compose](https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-component-api-guidelines.md)
- [Best Practices for Composition Patterns in Jetpack Compose - Droidcon](https://www.droidcon.com/2025/01/10/best-practices-for-composition-patterns-in-jetpack-compose/)
- [Slotting in with Compose UI - Chris Banes](https://chrisbanes.me/posts/slotting-in-with-compose-ui/)
- [Practical Compose Slot API example - Mobile Dev Notes](https://www.valueof.io/blog/compose-slot-api-example-composable-content-lambda)
