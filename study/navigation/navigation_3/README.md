# Navigation 3 (Nav3) 학습

> 2025년 Google I/O에서 발표되고 11월에 안정화된 Compose 전용 네비게이션 라이브러리

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `navigation` | Jetpack Navigation Compose 기본 개념과 NavHost 사용법 | [📚 학습하기](../../navigation/navigation_basics/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 학습 목표

이 모듈을 완료하면 다음을 할 수 있습니다:

1. Navigation 2와 Navigation 3의 차이점을 이해하고 적절한 선택을 할 수 있다
2. Nav3의 핵심 개념(NavKey, 백스택, NavDisplay)을 이해하고 구현할 수 있다
3. 타입 안전한 네비게이션을 구현할 수 있다
4. SceneStrategy를 활용하여 다양한 화면 레이아웃에 대응할 수 있다
5. 기존 Navigation 2 프로젝트를 Navigation 3로 마이그레이션할 수 있다

---

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

---

## 시작하기

### 의존성 추가

```kotlin
// build.gradle.kts (앱 모듈)
dependencies {
    // Navigation 3 런타임 (필수)
    implementation("androidx.navigation3:navigation3-runtime:1.0.0")

    // Navigation 3 UI (NavDisplay 등)
    implementation("androidx.navigation3:navigation3-ui:1.0.0")

    // Kotlin Serialization (NavKey 직렬화용)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

// build.gradle.kts (프로젝트 루트)
plugins {
    kotlin("plugin.serialization") version "2.0.21" apply false
}
```

> **Note**: 버전은 2025년 11월 기준입니다. 최신 버전은 [Maven Repository](https://maven.google.com) 또는 [Navigation 3 릴리즈 노트](https://developer.android.com/jetpack/androidx/releases/navigation3)에서 확인하세요.

### 최소 요구 사항

| 항목 | 요구 버전 |
|------|----------|
| Kotlin | 2.0.0 이상 |
| Compose BOM | 2024.12.01 이상 |
| minSdk | 21 이상 |
| compileSdk | 35 이상 |

---

## 상황별 선택 가이드

Nav3를 언제 사용해야 할까요?

### 의사결정 플로우차트

```
새 프로젝트인가?
    |
    +--Yes--> Nav3 사용 (권장)
    |
    +--No--> 기존 Navigation 2 프로젝트인가?
                |
                +--Yes--> 복잡도와 일정 고려
                |           |
                |           +-- 여유 있음 --> 점진적 마이그레이션
                |           |
                |           +-- 긴급함 --> 현재 Nav2 유지
                |
                +--No--> Compose Multiplatform 프로젝트인가?
                            |
                            +--Yes--> Nav3 필수 (유일한 선택지)
                            |
                            +--No--> Nav3 권장
```

### 상황별 권장 사항

| 상황 | 권장 | 이유 |
|------|------|------|
| **새 Android 프로젝트** | Nav3 | Compose 친화적, 타입 안전, 최신 표준 |
| **기존 Nav2 프로젝트** | 점진적 마이그레이션 | 기능별로 전환 가능 |
| **Compose Multiplatform** | Nav3 필수 | Android, iOS, Desktop, Web 지원 |
| **복잡한 태블릿 레이아웃** | Nav3 | SceneStrategy로 List-Detail 쉽게 구현 |

---

## Navigation 2 vs Navigation 3 비교

| 항목 | Navigation 2 | Navigation 3 |
|------|-------------|--------------|
| **상태 관리** | NavController (불투명) | 개발자 소유 SnapshotStateList |
| **라우트 정의** | 문자열 기반 | Kotlin 타입 (data class/object) |
| **타입 안전성** | 런타임 에러 가능 | 컴파일 타임 체크 |
| **멀티 화면** | 단일 목적지만 표시 | SceneStrategy로 다중 목적지 |
| **ViewModel 스코핑** | 네비게이션 그래프 전체 | NavEntry별 세밀한 스코핑 |
| **설계 철학** | 블랙박스 | 오픈, 확장 가능 |
| **Multiplatform** | Android 전용 | Android, iOS, Desktop, Web 지원 |

---

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

> **Note**: `rememberNavBackStack()`을 사용하려면 모든 NavKey가 `@Serializable`이고 `NavKey` 인터페이스를 구현해야 합니다.

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

> **API 변경**: `EntryProviderBuilder`가 `EntryProviderScope`로 이름이 변경되었습니다 (2025년 11월).

---

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

---

## SceneStrategy (다중 화면 레이아웃)

Nav3의 강력한 기능 중 하나는 **SceneStrategy**입니다. 이를 통해 태블릿, 폴더블 등 다양한 화면 크기에 대응할 수 있습니다.

### 기본 전략

```kotlin
// 기본값: 단일 화면
NavDisplay(
    backStack = backStack,
    sceneStrategy = SinglePaneSceneStrategy(), // 기본값
    // ...
)
```

### DialogSceneStrategy (다이얼로그)

```kotlin
NavDisplay(
    backStack = backStack,
    sceneStrategy = remember { DialogSceneStrategy() },
    entryProvider = entryProvider {
        entry<Home> { HomeScreen() }

        // 다이얼로그로 표시
        entry<ConfirmDialog>(
            metadata = DialogSceneStrategy.dialog()
        ) {
            ConfirmDialogContent()
        }
    }
)
```

### TwoPaneSceneStrategy (List-Detail 레이아웃)

```kotlin
NavDisplay(
    backStack = backStack,
    sceneStrategy = remember { TwoPaneSceneStrategy<NavKey>() },
    entryProvider = entryProvider {
        entry<ProductList> {
            ProductListScreen()
        }

        // 두 번째 패널로 표시
        entry<ProductDetail>(
            metadata = TwoPaneSceneStrategy.twoPane()
        ) { key ->
            ProductDetailScreen(key.id)
        }
    }
)
```

### 전략 체이닝

여러 전략을 `then`으로 연결할 수 있습니다:

```kotlin
val sceneStrategy = remember {
    DialogSceneStrategy<NavKey>() then TwoPaneSceneStrategy()
}

NavDisplay(
    backStack = backStack,
    sceneStrategy = sceneStrategy,
    // ...
)
```

---

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

---

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

---

## 마이그레이션 가이드 (Nav2 -> Nav3)

기존 Navigation 2 프로젝트에서 마이그레이션하는 단계:

### Step 1: NavKey 인터페이스 구현

```kotlin
// Before (Nav2)
@Serializable
data class ProductRoute(val id: String)

// After (Nav3)
@Serializable
data class ProductRoute(val id: String) : NavKey
```

### Step 2: NavController를 백스택으로 교체

```kotlin
// Before (Nav2)
val navController = rememberNavController()
navController.navigate("product/123")

// After (Nav3)
val backStack = rememberNavBackStack(Home)
backStack.add(ProductRoute(id = "123"))
```

### Step 3: NavHost를 NavDisplay로 교체

```kotlin
// Before (Nav2)
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen() }
    composable("product/{id}") { entry ->
        ProductScreen(entry.toRoute<ProductRoute>().id)
    }
}

// After (Nav3)
NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
        entry<Home> { HomeScreen() }
        entry<ProductRoute> { key ->
            ProductScreen(key.id)
        }
    }
)
```

---

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

### 4. Polymorphic Serialization 주의
```kotlin
// rememberNavBackStack() 사용 시 모든 NavKey 서브타입이
// SerializersModule에 등록되어야 합니다
val module = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(Home::class)
        subclass(ProductDetail::class)
        // ...
    }
}
```

---

## 연습 문제

1. **기본 네비게이션**: Home, Settings 두 화면 간 이동 구현
2. **인자 전달**: 상품 목록에서 상품 상세로 ID, 이름 전달
3. **백스택 조작**: popUpTo, replaceTop, clearStack 구현

---

## 다음 학습

- Navigation 2 (기존 방식 이해 및 비교)
- Deep Link 심화
- ViewModel + Navigation 패턴
- Compose Multiplatform Navigation

---

## 학습 모듈 안내

이 모듈에서는 Navigation 3를 직접 체험할 수 있습니다:

| 탭 | 내용 |
|---|------|
| **Problem** | Navigation 2의 5가지 핵심 문제점을 코드와 함께 확인 |
| **Solution** | Navigation 3의 백스택 기반 네비게이션을 직접 조작하며 체험 |
| **Practice** | 3단계 난이도별 연습 문제로 실력 향상 |

> **Note**: Solution 탭에서는 학습 목적으로 `mutableStateListOf`를 사용하여 Nav3의 백스택 개념을 시뮬레이션합니다. 실제 프로덕션에서는 `rememberNavBackStack()`과 `NavDisplay`를 사용하세요.

---

## 참고 자료

### 공식 문서
- [Navigation 3 공식 문서](https://developer.android.com/guide/navigation/navigation-3)
- [Navigation 3 릴리즈 노트](https://developer.android.com/jetpack/androidx/releases/navigation3)
- [Nav3 Recipes (공식 예제)](https://github.com/android/nav3-recipes)

### 블로그 및 발표
- [Navigation 3 발표 블로그 (2025.05)](https://android-developers.googleblog.com/2025/05/announcing-jetpack-navigation-3-for-compose.html)
- [Navigation 3 안정화 발표 (2025.11)](https://android-developers.googleblog.com/2025/11/jetpack-navigation-3-is-stable.html)

### 커뮤니티 자료
- [Production-Ready Navigation 3](https://proandroiddev.com/production-ready-navigation-3-in-jetpack-compose-0ff709d527e4)
- [Mastering Navigation 3 - Android Poet](https://androidpoet.medium.com/mastering-navigation-3-in-jetpack-compose-the-complete-guide-to-modern-android-navigation-ea93276385d1)
