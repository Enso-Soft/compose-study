# Navigation 3 (Nav3) 학습

> 2025년 Google I/O에서 발표되고 11월에 안정화된 Compose 전용 네비게이션 라이브러리

## 개념

**Navigation 3 (Nav3)**는 Jetpack Compose를 위해 처음부터 새로 설계된 네비게이션 라이브러리입니다. 기존 Navigation 2가 XML View 시스템 기반이었다면, Nav3는 Compose의 선언적 패러다임을 완전히 수용합니다.

### 핵심 철학: "You Own the Back Stack"

```kotlin
// Nav3 - 개발자가 백스택을 직접 소유하고 제어
val backStack = remember { mutableStateListOf<Any>(Home) }

// 네비게이션 = 단순한 리스트 조작
backStack.add(ProductDetail(id = 123))  // 화면 이동
backStack.removeLastOrNull()             // 뒤로가기
backStack.clear(); backStack.add(Home)   // 홈으로 초기화
```

## Navigation 2 vs Navigation 3 비교

| 항목 | Navigation 2 | Navigation 3 |
|------|-------------|--------------|
| **상태 관리** | NavController (불투명) | 개발자 소유 SnapshotStateList |
| **라우트 정의** | 문자열 기반 | Kotlin 타입 (data class/object) |
| **타입 안전성** | 런타임 에러 가능 | 컴파일 타임 체크 |
| **멀티 화면** | 단일 목적지만 표시 | Scenes API로 다중 목적지 |
| **ViewModel 스코핑** | 네비게이션 그래프 전체 | NavEntry별 세밀한 스코핑 |
| **설계 철학** | 블랙박스 | 오픈, 확장 가능 |
| **Multiplatform** | Android 전용 | Android, iOS, Desktop, Web 지원 |

## 핵심 구성 요소

### 1. NavKey (화면 정의)

```kotlin
import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

// 단순 화면 - data object 사용
@Serializable
data object Home : NavKey

@Serializable
data object Settings : NavKey

// 인자가 있는 화면 - data class 사용
@Serializable
data class ProductDetail(val id: Int, val name: String) : NavKey

@Serializable
data class UserProfile(val userId: String) : NavKey
```

### 2. 백스택 (SnapshotStateList)

```kotlin
// 기본 백스택 생성 (Configuration Change 시 초기화됨)
val backStack = remember { mutableStateListOf<Any>(Home) }

// 영속적 백스택 생성 (프로세스 종료 후에도 유지)
val backStack = rememberNavBackStack(Home)
```

### 3. NavDisplay (화면 렌더링)

```kotlin
NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryDecorators = listOf(
        rememberSavedStateNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    ),
    entryProvider = { key ->
        when (key) {
            is Home -> NavEntry(key) { HomeScreen() }
            is ProductDetail -> NavEntry(key) {
                ProductDetailScreen(id = key.id, name = key.name)
            }
            else -> NavEntry(Unit) { Text("Unknown") }
        }
    }
)
```

### 4. entryProvider DSL (권장)

```kotlin
NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
        entry<Home> { HomeScreen() }
        entry<ProductDetail> { key ->
            ProductDetailScreen(id = key.id, name = key.name)
        }
        entry<Settings> { SettingsScreen() }
    }
)
```

## Entry Decorators (상태 관리)

Nav3에서는 **Entry Decorators**를 통해 상태 저장, ViewModel 스코핑 등을 처리합니다.

```kotlin
NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryDecorators = listOf(
        // 1. SavedState: rememberSaveable 상태 유지
        rememberSavedStateNavEntryDecorator(),
        // 2. ViewModel: NavEntry별 ViewModel 스코핑
        rememberViewModelStoreNavEntryDecorator()
    ),
    entryProvider = entryProvider { /* ... */ }
)
```

### ViewModel 스코핑

```kotlin
entry<ProductDetail> { key ->
    // 이 ViewModel은 ProductDetail이 백스택에 있는 동안만 유지
    // 화면이 백스택에서 제거되면 자동으로 클리어됨
    val viewModel: ProductViewModel = viewModel()

    ProductDetailScreen(
        product = viewModel.product,
        onAction = viewModel::onAction
    )
}
```

## 백스택 조작 패턴

### 기본 네비게이션

```kotlin
// 화면 이동
backStack.add(ProductDetail(id = 123, name = "Kotlin Book"))

// 뒤로가기
backStack.removeLastOrNull()

// 홈으로 초기화
backStack.clear()
backStack.add(Home)
```

### 고급 백스택 조작

```kotlin
// popUpTo: 특정 화면까지 모든 화면 제거
inline fun <reified T> MutableList<Any>.popUpTo(inclusive: Boolean = false) {
    val index = indexOfLast { it is T }
    if (index >= 0) {
        val removeFrom = if (inclusive) index else index + 1
        while (size > removeFrom) {
            removeAt(lastIndex)
        }
    }
}

// replaceTop: 현재 화면을 새 화면으로 교체
fun MutableList<Any>.replaceTop(newKey: Any) {
    if (isNotEmpty()) {
        removeAt(lastIndex)
    }
    add(newKey)
}

// 사용 예시
backStack.popUpTo<Home>(inclusive = false)  // Home 위의 모든 화면 제거
backStack.replaceTop(Settings)               // 현재 화면을 Settings로 교체
```

## Deep Link 처리

```kotlin
@Composable
fun AppNavigation(intent: Intent?) {
    val backStack = rememberNavBackStack(Home)

    // Deep Link 처리
    LaunchedEffect(intent) {
        intent?.data?.let { uri ->
            when {
                uri.path?.startsWith("/product/") == true -> {
                    val id = uri.lastPathSegment?.toIntOrNull() ?: return@let
                    backStack.add(ProductDetail(id = id, name = ""))
                }
                uri.path == "/settings" -> {
                    backStack.add(Settings)
                }
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        // ...
    )
}
```

### AndroidManifest.xml 설정

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data
        android:scheme="myapp"
        android:host="app" />
</intent-filter>
```

## 주의사항

### 1. 반드시 Entry Decorators 설정
```kotlin
// 필수! 상태 저장과 ViewModel 스코핑을 위해
entryDecorators = listOf(
    rememberSavedStateNavEntryDecorator(),
    rememberViewModelStoreNavEntryDecorator()
)
```

### 2. NavKey 직렬화
```kotlin
// NavKey를 구현하면 rememberNavBackStack 사용 가능
@Serializable
data class Product(val id: Int) : NavKey  // O

// NavKey 없이도 가능하지만 영속성 없음
data class Product(val id: Int)  // remember { mutableStateListOf }만 사용 가능
```

### 3. 백스택 빈 상태 처리
```kotlin
onBack = {
    if (backStack.size > 1) {
        backStack.removeLastOrNull()
    } else {
        // 마지막 화면이면 앱 종료 등 처리
        activity.finish()
    }
}
```

## 사용 시나리오

1. **새 프로젝트**: Nav3를 기본으로 사용
2. **기존 Nav2 프로젝트**: 점진적 마이그레이션 권장
3. **Compose Multiplatform**: Nav3가 유일한 선택지
4. **복잡한 네비게이션**: Scenes API로 다중 화면 지원

## 연습 문제

1. **기본 네비게이션**: Home, Settings 두 화면 간 이동 구현
2. **인자 전달**: 상품 목록에서 상품 상세로 ID, 이름 전달
3. **백스택 조작**: popUpTo, replaceTop, clearStack 구현

## 다음 학습

- Navigation 2 (기존 방식 이해)
- Deep Link 심화
- ViewModel + Navigation 패턴
- Compose Multiplatform Navigation

## 참고 자료

- [Navigation 3 공식 문서](https://developer.android.com/guide/navigation/navigation-3)
- [Nav3 Recipes](https://github.com/android/nav3-recipes)
- [Navigation 3 발표 블로그](https://android-developers.googleblog.com/2025/05/announcing-jetpack-navigation-3-for-compose.html)
- [Navigation 3 안정화 발표](https://android-developers.googleblog.com/2025/11/jetpack-navigation-3-is-stable.html)
