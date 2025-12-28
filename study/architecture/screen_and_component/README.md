# Screen과 Component 분리 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `state_hoisting` | 상태 호이스팅과 단방향 데이터 흐름 패턴 | [📚 학습하기](../../state/state_hoisting/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Compose에서 UI를 구성할 때 **Screen**과 **Component**를 명확히 구분하는 것이 중요합니다. 이 구분은 코드의 재사용성, 테스트 용이성, 유지보수성에 직접적인 영향을 미칩니다.

### Screen vs Component

| 구분 | Screen | Component |
|------|--------|-----------|
| 역할 | 전체 화면 담당 | 재사용 가능한 UI 조각 |
| 네이밍 | `XxxScreen` | 기능을 설명하는 명사 |
| 상태 | Stateful (상태 소유) | Stateless (상태 주입) |
| ViewModel | 직접 연결 | 연결하지 않음 |
| Preview | 어려움 (ViewModel 의존) | 쉬움 (상태 주입) |
| 예시 | `HomeScreen`, `ProfileScreen` | `UserCard`, `SearchBar` |

```kotlin
// Screen: 전체 화면
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) { ... }

// Component: 재사용 가능한 UI 조각
@Composable
fun UserCard(user: User, onClick: () -> Unit) { ... }
```

---

## 왜 분리해야 하는가?

### Unidirectional Data Flow (UDF)

Compose는 **단방향 데이터 흐름** 패턴을 따릅니다:
- **State flows down**: 상태는 위에서 아래로 흐릅니다
- **Events flow up**: 이벤트는 아래에서 위로 전달됩니다

```
┌─────────────────────────────────────────────┐
│                  ViewModel                   │
│  ┌─────────────────────────────────────┐    │
│  │     State (StateFlow<UiState>)      │    │
│  └──────────────────┬──────────────────┘    │
└─────────────────────┼───────────────────────┘
                      │ State Down
                      ▼
┌─────────────────────────────────────────────┐
│              Screen (Stateful)               │
│  collectAsStateWithLifecycle()               │
└──────────────────┬──────────────────────────┘
                   │ State Down
                   ▼
┌─────────────────────────────────────────────┐
│             Content (Stateless)              │
│  state: UiState, onAction: (Action) -> Unit  │
└──────────────────┬──────────────────────────┘
                   │ State Down
                   ▼
┌─────────────────────────────────────────────┐
│               Components                     │
│  필요한 데이터만, 콜백으로 이벤트 전달         │
└─────────────────────────────────────────────┘
                   │
                   │ Events Up (onClick, onChange, ...)
                   ▲
```

### 분리하지 않으면 발생하는 문제

**문제 1: Preview 불가능**
```kotlin
// ViewModel을 직접 사용하면 Preview에서 오류 발생!
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = viewModel()  // Preview 실패
    val state by viewModel.state.collectAsState()
    // ...
}

@Preview  // 오류: ViewModelStore should be set before setContentView()
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
```

**문제 2: 테스트 어려움**
- ViewModel Mock 필요
- 복잡한 의존성 설정
- UI 단위 테스트 불가

**문제 3: 재사용 불가능**
- 같은 UI를 다른 화면에서 사용할 수 없음
- 코드 중복 발생

---

## 핵심 원칙

### 원칙 1: Screen은 상태를 소유하고, Content는 상태를 표시한다

```kotlin
// Screen: 상태 소유 (Stateful)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileContent(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onNavigateBack
    )
}

// Content: 상태 표시 (Stateless)
@Composable
fun ProfileContent(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 순수 UI 렌더링
}
```

### 원칙 2: Component는 최소한의 데이터만 받는다

```kotlin
// Bad: 불필요한 데이터까지 받음
@Composable
fun UserAvatar(user: User) {  // User의 모든 필드가 필요한가?
    Image(user.avatarUrl)
}

// Good: 필요한 것만 받음
@Composable
fun UserAvatar(
    avatarUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = avatarUrl,
        contentDescription = contentDescription,
        modifier = modifier
    )
}
```

### 원칙 3: 이벤트는 콜백 람다로 위로 전달한다

```kotlin
@Composable
fun ToggleButton(
    isChecked: Boolean,                    // State down
    onCheckedChange: (Boolean) -> Unit,    // Event up
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onCheckedChange(!isChecked) },
        modifier = modifier
    ) {
        Icon(if (isChecked) Icons.Default.Check else Icons.Default.Close)
    }
}
```

---

## 화면의 일반적인 구조

```
Screen (Stateful) ─────────────────────────────────
│
├── ViewModel 연결
│   └── collectAsStateWithLifecycle()
│
└── Content (Stateless) ───────────────────────────
    │
    └── Scaffold
        ├── TopAppBar
        │
        ├── Content Area
        │   ├── Component A (예: Header)
        │   ├── Component B (예: List)
        │   └── Component C (예: Card)
        │
        └── BottomBar / FAB (선택)
```

---

## 구현 방법

### Step 1: State와 Action 정의

```kotlin
// UI 상태를 담는 데이터 클래스
data class HomeState(
    val user: User? = null,
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// UI에서 발생하는 이벤트
sealed interface HomeAction {
    data class ItemClick(val id: String) : HomeAction
    data object RefreshClick : HomeAction
    data object SettingsClick : HomeAction
}
```

### Step 2: ViewModel 작성

```kotlin
class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.ItemClick -> handleItemClick(action.id)
            HomeAction.RefreshClick -> refresh()
            HomeAction.SettingsClick -> { /* Navigation에서 처리 */ }
        }
    }

    private fun handleItemClick(id: String) { /* ... */ }
    private fun refresh() { /* ... */ }
}
```

### Step 3: Screen 작성 (Stateful)

```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onAction = { action ->
            when (action) {
                is HomeAction.ItemClick -> onNavigateToDetail(action.id)
                HomeAction.SettingsClick -> onNavigateToSettings()
                else -> viewModel.onAction(action)
            }
        }
    )
}
```

### Step 4: Content 작성 (Stateless)

```kotlin
@Composable
fun HomeContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                userName = state.user?.name,
                onSettingsClick = { onAction(HomeAction.SettingsClick) }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorMessage(state.error)
            else -> ItemList(
                items = state.items,
                onItemClick = { id -> onAction(HomeAction.ItemClick(id)) },
                modifier = modifier.padding(padding)
            )
        }
    }
}
```

### Step 5: Component 추출

```kotlin
// 재사용 가능한 Component들
@Composable
fun HomeTopBar(
    userName: String?,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) { ... }

@Composable
fun ItemList(
    items: List<Item>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) { ... }

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) { ... }

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) { ... }
```

---

## Component 설계 원칙

### 1. Modifier는 항상 첫 번째 선택적 파라미터

```kotlin
@Composable
fun ProfileCard(
    user: User,                     // 필수 파라미터
    onClick: () -> Unit,            // 필수 콜백
    modifier: Modifier = Modifier,  // 첫 번째 선택적 파라미터
    isSelected: Boolean = false     // 기타 선택적 파라미터
) {
    Card(modifier = modifier) { ... }
}
```

### 2. 네이밍 규칙

| 패턴 | 예시 | 설명 |
|------|------|------|
| `XxxScreen` | `HomeScreen` | 전체 화면 |
| `XxxContent` | `HomeContent` | Screen의 Stateless 버전 |
| `XxxCard` | `ProductCard` | 카드 형태 UI |
| `XxxItem` | `ListItem` | 리스트 아이템 |
| `XxxButton` | `SubmitButton` | 버튼 컴포넌트 |
| `XxxBar` | `SearchBar` | 바 형태 UI |

**피해야 할 네이밍:**
- `ProfileScreenUI()` - "UI" 중복
- `LoginButtonComponent()` - "Component" 중복

### 3. 적절한 분리 수준

```kotlin
// Too fine-grained: 너무 작은 단위
@Composable
fun BoldText(text: String) {
    Text(text, fontWeight = FontWeight.Bold)
}

// Just right: 의미있는 단위
@Composable
fun ProfileHeader(
    name: String,
    email: String,
    avatarUrl: String,
    modifier: Modifier = Modifier
) { ... }

// Too coarse: 너무 큰 단위 (Content 수준)
@Composable
fun ProfileEverything(...) { ... }
```

---

## 안티패턴

### 1. Content에 ViewModel 직접 전달

```kotlin
// Bad: Preview 불가, 테스트 어려움
@Composable
fun ProfileContent(viewModel: ProfileViewModel) { ... }

// Good: 상태와 콜백만 전달
@Composable
fun ProfileContent(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) { ... }
```

### 2. Component에 과도한 비즈니스 로직

```kotlin
// Bad: Component가 비즈니스 로직 수행
@Composable
fun PriceCard(product: Product) {
    val discountedPrice = product.price * (1 - product.discountRate)  // 계산 로직
    val formattedPrice = NumberFormat.getCurrencyInstance().format(discountedPrice)
    Text(formattedPrice)
}

// Good: 계산된 값을 받기
@Composable
fun PriceCard(
    formattedPrice: String,
    hasDiscount: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = formattedPrice,
        color = if (hasDiscount) Color.Red else Color.Black
    )
}
```

### 3. 상태 호이스팅 미준수

```kotlin
// Bad: Component 내부에서 상태 관리
@Composable
fun ToggleButton() {
    var isChecked by remember { mutableStateOf(false) }  // 내부 상태
    IconButton(onClick = { isChecked = !isChecked }) { ... }
}

// Good: 상태를 외부에서 주입
@Composable
fun ToggleButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    IconButton(onClick = { onCheckedChange(!isChecked) }) { ... }
}
```

---

## 테스트 가이드

### Content 테스트

Content는 Stateless이므로 mock 상태를 주입하여 쉽게 테스트할 수 있습니다:

```kotlin
@Test
fun homeContent_showsLoadingIndicator_whenLoading() {
    composeTestRule.setContent {
        HomeContent(
            state = HomeState(isLoading = true),
            onAction = {}
        )
    }

    composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
}

@Test
fun homeContent_showsItems_whenLoaded() {
    val items = listOf(Item("1", "Item 1"), Item("2", "Item 2"))

    composeTestRule.setContent {
        HomeContent(
            state = HomeState(items = items, isLoading = false),
            onAction = {}
        )
    }

    composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()
    composeTestRule.onNodeWithText("Item 2").assertIsDisplayed()
}

@Test
fun homeContent_callsOnAction_whenItemClicked() {
    var clickedId: String? = null
    val items = listOf(Item("1", "Item 1"))

    composeTestRule.setContent {
        HomeContent(
            state = HomeState(items = items),
            onAction = { action ->
                if (action is HomeAction.ItemClick) {
                    clickedId = action.id
                }
            }
        )
    }

    composeTestRule.onNodeWithText("Item 1").performClick()
    assertEquals("1", clickedId)
}
```

### Preview 활용

```kotlin
@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    ProfileContent(
        state = ProfileState(
            user = User("홍길동", "hong@example.com"),
            isEditing = false
        ),
        onEditClick = {},
        onLogoutClick = {},
        onBackClick = {}
    )
}

// 다양한 상태 Preview
@Preview(name = "Loading")
@Composable
fun ProfileContentLoadingPreview() {
    ProfileContent(
        state = ProfileState(isLoading = true),
        onEditClick = {},
        onLogoutClick = {},
        onBackClick = {}
    )
}

@Preview(name = "Error")
@Composable
fun ProfileContentErrorPreview() {
    ProfileContent(
        state = ProfileState(error = "네트워크 오류가 발생했습니다"),
        onEditClick = {},
        onLogoutClick = {},
        onBackClick = {}
    )
}
```

---

## 사용 시나리오

### 새 화면 만들 때
1. State와 Action 정의
2. ViewModel 작성
3. `XxxScreen` Composable 생성 (ViewModel 연결)
4. `XxxContent` Composable 생성 (UI 구현)
5. 반복되는 UI를 Component로 추출
6. Preview 추가

### 기존 화면 리팩토링할 때
1. 현재 상태를 State 데이터 클래스로 추출
2. ViewModel 의존 부분을 Screen으로 분리
3. 순수 UI 부분을 Content로 분리
4. 재사용 가능한 부분을 Component로 추출
5. 테스트 및 Preview 추가

---

## 연습 문제

### 연습 1: Screen + Content 분리하기

**목표**: 모든 것이 섞인 Composable을 Screen + Content로 분리

**시나리오**: 간단한 카운터 화면을 Screen + Content 패턴으로 분리

**힌트**:
- CounterScreen: 상태(`count`) 관리
- CounterContent: UI 렌더링, 상태와 콜백을 파라미터로 받음

### 연습 2: Component 추출하기

**목표**: 반복되는 UI 요소를 재사용 가능한 Component로 추출

**시나리오**: 팀원 목록에서 UserInfoCard를 추출하여 재사용

**힌트**:
- UserInfoCard는 name, role, email, onClick을 파라미터로 받음
- Modifier는 첫 번째 선택적 파라미터

### 연습 3: 프로필 화면 완성하기

**목표**: Screen + Content + Components로 완전한 화면 구성

**시나리오**: ProfileScreen을 체계적으로 분리
- ProfilePracticeContent
- ProfilePracticeHeader
- ProfilePracticeBio
- ProfilePracticeStats
- FollowPracticeButton

---

## 파일 구조 권장

```
feature/profile/
   │
   ├── ProfileScreen.kt      // Screen + Content
   │
   ├── ProfileState.kt       // State, Action 정의
   │
   ├── ProfileViewModel.kt   // ViewModel
   │
   └── components/
          ├── ProfileAvatar.kt
          ├── ProfileTopBar.kt
          ├── StatsRow.kt
          └── ActionButtons.kt

// 공통 Component는 ui/components/에 배치
ui/components/
   ├── Avatar.kt
   ├── StatsRow.kt
   └── LoadingIndicator.kt
```

---

## 다음 학습

- **state_hoisting**: 상태 끌어올리기 패턴 심화
- **view_model**: ViewModel과 Compose 통합
- **scaffold_and_theming**: Scaffold 구조 활용
- **navigation_3**: Jetpack Navigation 3 활용

---

## 참고 자료

- [Compose UI Architecture - Android Developers](https://developer.android.com/develop/ui/compose/architecture)
- [State hoisting - Android Developers](https://developer.android.com/develop/ui/compose/state-hoisting)
- [Style guidelines for Compose APIs - Android Developers](https://developer.android.com/develop/ui/compose/api-guidelines)
- [Follow best practices - Android Developers](https://developer.android.com/develop/ui/compose/performance/bestpractices)
