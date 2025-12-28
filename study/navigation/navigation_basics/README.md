# Navigation Compose 학습 (Type-Safe Navigation)

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `state_hoisting` | 상태 끌어올리기, Stateless 컴포넌트 설계 | [📚 학습하기](../../state/state_hoisting/README.md) |
| `remember` | Recomposition에도 상태 유지 | [📚 학습하기](../../state/remember/README.md) |
| `composable_function` | @Composable 함수의 동작 원리 | [📚 학습하기](../../basics/composable_function/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

| 항목 | 내용 |
|------|------|
| **주제** | Type-Safe Navigation |
| **지원 버전** | Navigation Compose 2.8.0+ (현재 최신: 2.9.6) |
| **필수 의존성** | Kotlin Serialization 플러그인 |
| **난이도** | 중급 |

---

## 개념

**Navigation Compose**는 Jetpack Compose에서 화면 간 이동을 처리하는 공식 라이브러리입니다.
**Type-Safe Navigation**은 Navigation 2.8.0부터 도입된 기능으로, **컴파일 타임에 타입 안전성**을 보장합니다.

### 핵심 특징

| 특징 | 설명 |
|------|------|
| **컴파일 타임 검증** | 오타나 타입 오류가 빌드 시 발견됨 |
| **IDE 자동완성** | Route 클래스의 프로퍼티 자동완성 지원 |
| **타입 강제** | String, Int, Boolean 등 타입이 강제됨 |
| **리팩토링 안전성** | 이름 변경 시 모든 참조 자동 업데이트 |

### 기본 사용법

```kotlin
// Route 정의 (인자 없음)
@Serializable
object Home

// Route 정의 (인자 있음)
@Serializable
data class Profile(val userId: String)

// NavHost 구성
NavHost(navController, startDestination = Home) {
    composable<Home> { HomeScreen() }
    composable<Profile> { backStackEntry ->
        val profile: Profile = backStackEntry.toRoute()
        ProfileScreen(profile.userId)
    }
}

// 화면 이동
navController.navigate(Profile(userId = "user123"))
```

---

## 문제 상황: 문자열 기반 네비게이션

### 시나리오

당신은 쇼핑 앱을 개발하고 있습니다. 사용자가 상품을 클릭하면 상세 페이지로 이동해야 하고,
상품 ID를 전달해야 합니다. 팀에서는 Navigation Compose를 사용하기로 했습니다.

### 잘못된 코드 예시

```kotlin
// 문자열로 Route 정의
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen() }
    composable("profile/{userId}") { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId")
        ProfileScreen(userId)  // nullable!
    }
}

// 문자열로 네비게이션
navController.navigate("profile/user123")
```

### 발생하는 문제점

| 문제 | 설명 | 발생 시점 |
|------|------|----------|
| **오타** | `"proflie/{userId}"` 같은 오타 | 런타임 |
| **타입 안전성 부재** | Int를 전달해야 하는데 String 전달해도 컴파일됨 | 런타임 |
| **인자명 불일치** | `userId` vs `user_id` | 런타임 |
| **유지보수 어려움** | Route 문자열이 여러 곳에 흩어져 있음 | 개발 시 |
| **nullable 인자** | 인자가 항상 `String?`으로 추출됨 | 개발 시 |

---

## 해결책: Type-Safe Navigation 사용

### 핵심 원리

Type-Safe Navigation은 Kotlin Serialization을 활용하여 Route를 클래스로 정의합니다.
이를 통해 문자열 기반 네비게이션의 모든 문제를 해결합니다.

### Route 정의 패턴

```kotlin
// 1. 인자 없는 Route: object 사용
@Serializable
object Home

// 2. 인자 있는 Route: data class 사용
@Serializable
data class Profile(val userId: String)

// 3. 선택적 인자: 기본값 사용
@Serializable
data class Settings(val darkMode: Boolean = false)

// 4. 여러 인자: 다양한 타입 조합
@Serializable
data class Product(
    val productId: String,
    val quantity: Int,
    val isInCart: Boolean = false
)
```

### NavHost 구성

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home  // 타입으로 지정 (문자열 아님!)
    ) {
        composable<Home> {
            HomeScreen(
                onNavigateToProfile = { userId ->
                    navController.navigate(Profile(userId))  // 타입 안전!
                }
            )
        }

        composable<Profile> { backStackEntry ->
            val profile: Profile = backStackEntry.toRoute()  // 타입 안전 추출
            ProfileScreen(profile.userId)  // Non-nullable String!
        }
    }
}
```

### 해결되는 이유

| 문제 | 해결 방법 |
|------|----------|
| 오타 | 컴파일 오류로 즉시 발견 |
| 타입 불일치 | `Profile(userId: String)`에 Int 전달 시 컴파일 오류 |
| 인자명 불일치 | 클래스 프로퍼티로 정의되어 불일치 불가능 |
| nullable 인자 | `toRoute()`가 Non-nullable 타입 반환 |
| 리팩토링 | IDE의 Rename 기능으로 모든 참조 자동 업데이트 |

---

## 사용 시나리오

### 1. 단순 화면 이동 (인자 없음)

```kotlin
@Serializable
object Settings

navController.navigate(Settings)
```

### 2. 데이터 전달 (인자 있음)

```kotlin
@Serializable
data class ProductDetail(val productId: String)

navController.navigate(ProductDetail(productId = "prod123"))
```

### 3. 여러 인자 전달

```kotlin
@Serializable
data class OrderConfirm(
    val orderId: String,
    val totalPrice: Int,
    val isExpress: Boolean = false
)

navController.navigate(OrderConfirm(
    orderId = "order456",
    totalPrice = 50000,
    isExpress = true
))
```

### 4. 뒤로 가기

```kotlin
// 이전 화면으로 돌아가기
navController.popBackStack()

// 특정 화면까지 돌아가기 (Type-Safe)
navController.popBackStack<Home>(inclusive = false)
```

---

## 빠른 참조 (Quick Reference)

### Route 정의 체크리스트

```kotlin
// 1. @Serializable 어노테이션 필수!
@Serializable
object MyRoute           // 인자 없는 화면

@Serializable
data class MyRoute(      // 인자 있는 화면
    val id: String,      // 필수 인자
    val count: Int = 0   // 선택적 인자 (기본값)
)
```

### NavHost 구성 체크리스트

```kotlin
NavHost(
    navController = navController,
    startDestination = StartRoute  // 객체 또는 클래스 (문자열 X)
) {
    composable<RouteType> { backStackEntry ->
        val route: RouteType = backStackEntry.toRoute()
        // route.property 로 인자 접근
    }
}
```

### 네비게이션 패턴

```kotlin
// 화면 이동
navController.navigate(Profile(userId = "123"))

// 이전 화면으로
navController.popBackStack()

// 특정 화면까지 돌아가기
navController.popBackStack<Home>(inclusive = false)
```

---

## 주의사항

### 1. Serialization 플러그인 필수!

```kotlin
// build.gradle.kts (Module)
plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
```

### 2. 복잡한 객체는 직접 전달 불가

```kotlin
// 잘못된 방법: 객체 전체 전달
@Serializable
data class UserDetail(val user: User)  // User가 복잡한 객체라면 문제

// 올바른 방법: ID만 전달하고 화면에서 조회
@Serializable
data class UserDetail(val userId: String)
```

### 3. 큰 데이터 전달 금지 (TransactionTooLargeException)

```kotlin
// 잘못된 방법: 긴 리스트 전달
@Serializable
data class ListScreen(val items: List<String>)  // 크기 제한 위험!

// 올바른 방법: ID만 전달하고 ViewModel에서 데이터 로드
@Serializable
data class ListScreen(val categoryId: String)
```

### 4. Enum 사용 시 @Keep 필요 (R8/ProGuard)

```kotlin
// Enum을 Route 인자로 사용할 때
@Keep  // ProGuard에서 제거되지 않도록
enum class Category { FOOD, DRINK, DESSERT }

@Serializable
data class CategoryScreen(val category: Category)
```

---

## 필수 의존성

```kotlin
// build.gradle.kts (Module)
plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation(libs.kotlinx.serialization.json)
}
```

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | 문자열 기반 네비게이션의 문제점 시연 |
| `Solution.kt` | Type-Safe Navigation 사용법 시연 |
| `Practice.kt` | 직접 구현해보는 연습 문제 |

---

## 연습 문제

### 연습 1: 기본 Type-Safe Navigation
- Home -> Detail 화면 이동 구현
- `@Serializable` Route 정의
- `composable<T>`와 `toRoute<T>()` 사용

### 연습 2: 다중 인자 전달
- 상품 ID(String), 수량(Int), 장바구니 여부(Boolean) 전달
- 기본값이 있는 선택적 인자 처리

### 연습 3: 뒤로 가기 처리
- `popBackStack()` 기본 사용
- `popBackStack<Route>(inclusive)` 활용
- 백스택 구조 이해

---

## Navigation 2 vs Navigation 3

### Navigation 3이란?

2025년 11월, **Navigation 3 (Nav3)**이 stable로 출시되었습니다.
Nav3은 Compose를 위해 처음부터 새로 설계된 네비게이션 라이브러리입니다.

### 주요 차이점

| 항목 | Navigation 2 (Type-Safe) | Navigation 3 |
|------|--------------------------|--------------|
| **설계** | XML 시대부터 진화 | Compose-first 설계 |
| **백스택 관리** | 라이브러리가 관리 | 개발자가 직접 관리 (`SnapshotStateList`) |
| **모델** | 이벤트 기반 | 상태 기반 |
| **유연성** | 제한적 | 높음 (커스텀 가능) |
| **학습 곡선** | 낮음 | 중간 |

### 어떤 것을 배워야 하나?

1. **입문자**: Navigation 2 Type-Safe → Navigation 3 순서로 학습
2. **새 프로젝트**: Navigation 3 검토 권장
3. **기존 프로젝트**: Navigation 2 Type-Safe 유지 또는 점진적 마이그레이션

---

## 다음 학습

- `navigation_3`: Navigation 3 학습 (Compose-first 설계)
- `deep_link`: 외부 링크로 특정 화면 열기
- `animation`: 화면 전환 애니메이션
- `lifecycle_effect`: Lifecycle과 Compose 연동

---

## 참고 자료

- [Type safety in Kotlin DSL and Navigation Compose](https://developer.android.com/guide/navigation/design/type-safety) - Android 공식 문서
- [Type safe navigation for Compose](https://medium.com/androiddevelopers/type-safe-navigation-for-compose-105325a97657) - Android Developers Medium
- [Navigation Compose meet Type Safety](https://medium.com/androiddevelopers/navigation-compose-meet-type-safety-e081fb3cf2f8) - Ian Lake
- [Jetpack Navigation 3 is stable](https://android-developers.googleblog.com/2025/11/jetpack-navigation-3-is-stable.html) - Android Developers Blog
