# Navigation Compose 학습 (Type-Safe Navigation)

## 개념

**Navigation Compose**는 Jetpack Compose에서 화면 간 이동을 처리하는 공식 라이브러리입니다.
**Type-Safe Navigation**은 Navigation 2.8.0부터 도입된 기능으로, **컴파일 타임에 타입 안전성**을 보장합니다.

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

## 핵심 특징

1. **컴파일 타임 타입 체크**: 잘못된 타입은 컴파일 오류 발생
2. **IDE 자동완성 지원**: Route 클래스의 프로퍼티 자동완성
3. **리팩토링 안전성**: Route 이름 변경 시 모든 참조 자동 업데이트
4. **코드 가독성**: 문자열 대신 명확한 클래스 사용

---

## 문제 상황: 문자열 기반 네비게이션

### 잘못된 코드 예시

```kotlin
// 문자열로 Route 정의
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen() }
    composable("profile/{userId}") { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId")
        ProfileScreen(userId)
    }
}

// 문자열로 네비게이션
navController.navigate("profile/user123")
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 오타 | `"proflie/{userId}"` 같은 오타가 런타임에만 발견됨 |
| 타입 안전성 부재 | Int를 전달해야 하는데 String 전달해도 컴파일됨 |
| 인자명 불일치 | `userId` vs `user_id` 런타임 오류 |
| 유지보수 어려움 | Route 문자열이 여러 곳에 흩어져 있음 |

---

## 해결책: Type-Safe Navigation 사용

### Route 정의

```kotlin
// 인자 없는 Route: object 사용
@Serializable
object Home

// 인자 있는 Route: data class 사용
@Serializable
data class Profile(val userId: String)

// 선택적 인자: 기본값 사용
@Serializable
data class Settings(val darkMode: Boolean = false)
```

### NavHost 구성

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home  // 타입으로 지정
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
            ProfileScreen(profile.userId)
        }
    }
}
```

### 해결되는 이유

| 해결 | 설명 |
|------|------|
| 컴파일 타임 검증 | 오타나 타입 오류가 빌드 시 발견됨 |
| 자동완성 | IDE에서 Profile( 입력 시 userId 힌트 제공 |
| 리팩토링 | userId를 id로 변경하면 모든 참조 자동 업데이트 |
| 타입 강제 | Profile(userId: String)에 Int 전달 불가 |

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

// 특정 화면까지 돌아가기
navController.popBackStack<Home>(inclusive = false)
```

---

## 주의사항

### Serialization 플러그인 필수!

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
```

### 복잡한 객체는 직접 전달 불가

```kotlin
// 잘못된 방법: 객체 전체 전달
@Serializable
data class UserDetail(val user: User)  // User가 복잡한 객체라면 문제

// 올바른 방법: ID만 전달하고 화면에서 조회
@Serializable
data class UserDetail(val userId: String)
```

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | 문자열 기반 네비게이션의 문제점 |
| `Solution.kt` | Type-Safe Navigation 사용법 |
| `Practice.kt` | 직접 구현해보는 연습 문제 |

---

## 연습 문제

1. **기본 네비게이션**: Home -> Detail 화면 이동 구현
2. **다중 인자 전달**: 상품 ID, 수량, 장바구니 여부 전달
3. **뒤로 가기 처리**: popBackStack 활용

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

## 다음 학습

- `LifecycleEffect`: Lifecycle과 Compose 연동
- `Animation`: 화면 전환 애니메이션
- `Deep Link`: 외부 링크로 특정 화면 열기
