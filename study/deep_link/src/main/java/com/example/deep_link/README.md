# Deep Link 학습

## 개념

**Deep Link**는 외부 URL, 앱 링크, 알림 등에서 앱의 특정 화면으로 직접 이동하는 기능입니다.

예를 들어 카카오톡에서 공유받은 상품 링크를 클릭하면, 웹 브라우저 대신 쇼핑 앱이 열리고 해당 상품 페이지로 바로 이동합니다.

```kotlin
// Deep Link URI 예시
"https://myshop.com/product/12345"  → 앱의 상품 상세 화면(productId=12345)
"myshop://cart"                     → 앱의 장바구니 화면
```

## 핵심 특징

1. **외부 진입점**: 웹, 알림, 다른 앱에서 앱의 특정 화면 접근
2. **Type-Safe 정의**: `navDeepLink<T>()`로 타입 안전하게 정의
3. **자동 파라미터 파싱**: URI에서 파라미터 자동 추출
4. **Manifest 설정 필수**: 외부 앱에서 접근하려면 intent-filter 필요

## Deep Link 유형

| 유형 | 스킴 예시 | 설명 |
|------|----------|------|
| 커스텀 스킴 | `myshop://product/123` | 앱 전용 스킴 |
| HTTP/HTTPS | `https://myshop.com/product/123` | 웹 URL과 동일 |
| App Links | 위와 같지만 `autoVerify="true"` | Google 인증된 링크 |

## 문제 상황: 하드코딩된 문자열 Deep Link

### 잘못된 코드 예시

```kotlin
// 문제 1: 문자열 하드코딩 - 오타 발생 가능
composable(
    route = "product/{productId}",
    deepLinks = listOf(
        navDeepLink { uriPattern = "myshop://product/{productId}" }
    )
) { backStackEntry ->
    // 문제 2: Nullable 타입 추출
    val productId = backStackEntry.arguments?.getString("productId")
    // productId가 null일 수 있음!
    ProductScreen(productId ?: "unknown")
}
```

### 발생하는 문제점

1. **타입 불안전**: `getString()` 반환값이 `String?`
2. **오타 위험**: `"productId"` 문자열 오타 시 런타임 에러
3. **리팩토링 어려움**: 파라미터명 변경 시 모든 곳 수동 수정
4. **IDE 지원 부족**: 자동완성, 타입 체크 불가

## 해결책: Type-Safe Deep Link

### 올바른 코드

```kotlin
// Step 1: Serializable Route 정의
@Serializable
data class Product(val productId: String)

// Step 2: navDeepLink<T>()로 Type-Safe 정의
composable<Product>(
    deepLinks = listOf(
        navDeepLink<Product>(basePath = "myshop://product")
    )
) { backStackEntry ->
    // Step 3: toRoute<T>()로 타입 안전하게 추출
    val product: Product = backStackEntry.toRoute()
    ProductScreen(productId = product.productId)  // Non-nullable!
}
```

### 해결되는 이유

1. **Non-nullable 타입**: `productId`가 항상 `String` (null 아님)
2. **컴파일 타임 검증**: 오타나 타입 오류를 빌드 시 발견
3. **IDE 지원**: 자동완성, 참조 찾기, 리팩토링 지원
4. **일관성**: Navigation Route와 Deep Link가 같은 타입 사용

## AndroidManifest 설정

외부 앱에서 Deep Link로 접근하려면 `intent-filter` 필요:

```xml
<activity
    android:name=".MainActivity"
    android:launchMode="singleTop"
    android:exported="true">

    <!-- 커스텀 스킴 Deep Link -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data android:scheme="myshop" android:host="product"/>
    </intent-filter>

    <!-- HTTPS App Link -->
    <intent-filter android:autoVerify="true">
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data
            android:scheme="https"
            android:host="myshop.example.com"
            android:pathPrefix="/product"/>
    </intent-filter>
</activity>
```

### 핵심 속성

| 속성 | 설명 |
|------|------|
| `launchMode="singleTop"` | 같은 화면이 스택에 있으면 재사용 |
| `android:autoVerify="true"` | App Links 검증 (HTTPS만) |
| `android:scheme` | URI 스킴 (myshop, https 등) |
| `android:host` | 도메인/호스트 |
| `android:pathPrefix` | 경로 접두사 |

## URI 패턴 매칭 규칙

```kotlin
@Serializable
data class Order(
    val orderId: String,              // 필수 파라미터 → path
    val status: String = "pending"    // 선택 파라미터 (기본값) → query
)

navDeepLink<Order>(basePath = "myshop://order")
// 생성되는 패턴: myshop://order/{orderId}?status={status}
```

| 파라미터 타입 | URI 위치 | 예시 |
|--------------|----------|------|
| 필수 (기본값 없음) | Path | `/{orderId}` |
| 선택 (기본값 있음) | Query | `?status={status}` |

## Activity 재생성 방지

Deep Link로 앱 진입 시 Activity 재생성을 방지하려면:

```kotlin
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            // NavHost 설정...
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // singleTop 모드에서 새 Deep Link 처리
        intent.data?.let { uri ->
            navController.navigate(
                NavDeepLinkRequest.Builder.fromUri(uri).build(),
                NavOptions.Builder().setLaunchSingleTop(true).build()
            )
        }
    }
}
```

## 사용 시나리오

1. **마케팅 링크**: 이메일/SMS 캠페인에서 특정 상품 페이지로 유도
2. **알림 탭**: 푸시 알림 클릭 시 관련 화면으로 이동
3. **앱 간 연동**: 다른 앱에서 특정 기능 호출
4. **QR 코드**: 오프라인에서 온라인 콘텐츠로 연결
5. **웹 → 앱 전환**: 웹 페이지에서 앱 설치 유도 및 컨텐츠 연결

## Deep Link 테스트

### ADB 명령어

```bash
# 커스텀 스킴 테스트
adb shell am start -W -a android.intent.action.VIEW \
    -d "myshop://product/12345" \
    com.example.deep_link

# HTTPS 테스트
adb shell am start -W -a android.intent.action.VIEW \
    -d "https://myshop.example.com/product/12345" \
    com.example.deep_link
```

### 코드 내 테스트

```kotlin
// URI로 직접 네비게이션
val uri = "myshop://product/12345".toUri()
navController.navigate(
    NavDeepLinkRequest.Builder.fromUri(uri).build()
)
```

## 주의사항

1. **Manifest 설정 필수**: intent-filter 없으면 외부 접근 불가
2. **파라미터 타입 제한**: String, Int, Boolean, Long, Float만 지원
3. **singleTop 사용**: 중복 화면 생성 방지
4. **앱 미설치 처리**: 웹 폴백 또는 Play Store 연결 고려
5. **보안**: 민감한 데이터를 Deep Link로 전달하지 않기

## 연습 문제

### 연습 1: 기본 Deep Link 정의 (난이도: 하)
- UserProfile 화면에 Deep Link를 연결하세요
- URI: `https://myapp.com/user/{userId}`

### 연습 2: 다중 파라미터 Deep Link (난이도: 중)
- 주문 상세 화면에 필수/선택 파라미터 Deep Link 정의
- 필수: orderId, 선택: status (기본값 "pending")

### 연습 3: 알림에서 Deep Link (난이도: 상)
- PendingIntent를 사용해 알림 클릭 시 특정 화면으로 이동

## 다음 학습

Deep Link를 마스터했다면 다음 주제를 학습하세요:
- **Nested Navigation**: 중첩된 NavGraph 구성
- **Bottom Navigation + Deep Link**: 탭 기반 앱에서 Deep Link 처리
