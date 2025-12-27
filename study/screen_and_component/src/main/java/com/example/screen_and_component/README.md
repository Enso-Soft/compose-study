# Screen과 Component 학습

## 개념

Compose에서 UI를 구성할 때 **Screen**과 **Component**를 명확히 구분하는 것이 중요합니다. 이 구분은 코드의 재사용성, 테스트 용이성, 유지보수성에 직접적인 영향을 미칩니다.

### Screen vs Component

| 구분 | Screen | Component |
|------|--------|-----------|
| 역할 | 전체 화면 담당 | 재사용 가능한 UI 조각 |
| 네이밍 | `XxxScreen` | 기능을 설명하는 명사 |
| 상태 | Stateful (상태 소유) | Stateless (상태 주입) |
| ViewModel | 직접 연결 | 연결하지 않음 |
| 예시 | `HomeScreen`, `ProfileScreen` | `UserCard`, `SearchBar` |

```kotlin
// Screen: 전체 화면
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) { ... }

// Component: 재사용 가능한 UI 조각
@Composable
fun UserCard(user: User, onClick: () -> Unit) { ... }
```

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

## 핵심 패턴: Stateful Screen + Stateless Content

### 왜 분리해야 하는가?

**문제: 모든 것을 하나의 Composable에 넣으면**
```kotlin
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // ViewModel 의존성 때문에:
    // - Preview 불가능
    // - 테스트 어려움
    // - 재사용 불가능

    Scaffold { ... }
}
```

**해결: Screen + Content 분리**
```kotlin
// Stateful Screen: ViewModel과 연결
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileContent(
        state = state,
        onEditClick = viewModel::onEditClick,
        onLogoutClick = viewModel::onLogout,
        onBackClick = onNavigateBack
    )
}

// Stateless Content: Preview 가능, 테스트 용이
@Composable
fun ProfileContent(
    state: ProfileState,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { ProfileTopBar(onBackClick = onBackClick) }
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {
            ProfileHeader(user = state.user)
            ProfileStats(stats = state.stats)
            ProfileActions(
                onEditClick = onEditClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}
```

### 패턴의 장점

| 장점 | 설명 |
|------|------|
| **Preview 가능** | Content에 mock 데이터를 전달하여 Preview 확인 |
| **테스트 용이** | Content는 순수 함수처럼 입력/출력만으로 테스트 |
| **재사용 가능** | 같은 Content를 다른 데이터로 재사용 |
| **관심사 분리** | Screen=상태 관리, Content=UI 렌더링 |

## 일반적인 화면 코드 구조

```kotlin
// ===== 1. Data Classes =====
data class HomeState(
    val user: User? = null,
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface HomeAction {
    data class ItemClick(val id: String) : HomeAction
    object RefreshClick : HomeAction
    object SettingsClick : HomeAction
}

// ===== 2. ViewModel =====
class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.ItemClick -> { /* 처리 */ }
            HomeAction.RefreshClick -> { /* 처리 */ }
            HomeAction.SettingsClick -> { /* 처리 */ }
        }
    }
}

// ===== 3. Screen (Stateful) =====
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

// ===== 4. Content (Stateless) =====
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
        if (state.isLoading) {
            LoadingIndicator()
        } else {
            ItemList(
                items = state.items,
                onItemClick = { id -> onAction(HomeAction.ItemClick(id)) },
                modifier = modifier.padding(padding)
            )
        }
    }
}

// ===== 5. Components =====
@Composable
fun HomeTopBar(userName: String?, onSettingsClick: () -> Unit) { ... }

@Composable
fun ItemList(items: List<Item>, onItemClick: (String) -> Unit, modifier: Modifier) { ... }

@Composable
fun LoadingIndicator() { ... }
```

## Component 설계 원칙

### 1. 최소한의 파라미터만 받기

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

### 2. Modifier는 항상 첫 번째 선택적 파라미터

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

### 3. 네이밍 규칙

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

## 사용 시나리오

### 새 화면 만들 때
1. `XxxScreen` Composable 생성 (ViewModel 연결)
2. `XxxContent` Composable 생성 (UI 구현)
3. 반복되는 UI를 Component로 추출

### 기존 화면 리팩토링할 때
1. ViewModel 의존 부분을 Screen으로 분리
2. 순수 UI 부분을 Content로 분리
3. 재사용 가능한 부분을 Component로 추출

### Preview 추가할 때
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
```

## 주의사항

### 1. Content에 ViewModel 직접 전달 금지

```kotlin
// Bad
@Composable
fun ProfileContent(viewModel: ProfileViewModel) { ... }

// Good
@Composable
fun ProfileContent(state: ProfileState, onAction: (ProfileAction) -> Unit) { ... }
```

### 2. Component에 과도한 로직 넣지 않기

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

### 3. 상태 호이스팅 원칙 준수

```kotlin
// Component 내부에서 상태를 관리하지 않음
@Composable
fun ToggleButton(
    isChecked: Boolean,           // 상태는 외부에서
    onCheckedChange: (Boolean) -> Unit,  // 이벤트는 위로
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

## 연습 문제

1. **기본 분리**: 모든 것이 하나로 된 화면을 Screen + Content로 분리
2. **Component 추출**: 반복되는 UI를 재사용 가능한 Component로 추출
3. **프로필 화면 구성**: ProfileScreen + ProfileContent + 여러 Component로 화면 구성

## 다음 학습

- **state_hoisting**: 상태 끌어올리기 패턴 심화
- **view_model**: ViewModel과 Compose 통합
- **scaffold_and_theming**: Scaffold 구조 활용

## 참고 자료

- [Compose UI Architecture - Android Developers](https://developer.android.com/develop/ui/compose/architecture)
- [State hoisting - Android Developers](https://developer.android.com/develop/ui/compose/state-hoisting)
- [Style guidelines for Compose APIs - Android Developers](https://developer.android.com/develop/ui/compose/api-guidelines)
