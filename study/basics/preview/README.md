# @Preview 어노테이션 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `composable_function` | @Composable 함수와 기본 사용법 | [📚 학습하기](../../basics/composable_function/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`@Preview`는 Jetpack Compose에서 **앱을 실행하지 않고 UI를 미리보기**할 수 있게 해주는 어노테이션입니다.
Android Studio의 Design 탭에서 실시간으로 Composable의 렌더링 결과를 확인할 수 있습니다.

```kotlin
@Preview(
    name = "사용자 카드",
    showBackground = true,
    widthDp = 320
)
@Composable
fun UserCardPreview() {
    UserCard(User("Alice", 25))
}
```

---

## 핵심 특징

### 1. @Preview 파라미터

| 파라미터 | 설명 | 예시 |
|---------|------|------|
| `name` | Preview 이름 | `"Light Mode"` |
| `group` | Preview 그룹 | `"Theme"` |
| `showBackground` | 배경 표시 | `true` |
| `backgroundColor` | 배경 색상 (Long) | `0xFFFFFFFF` |
| `widthDp` | 너비 (dp) | `360` |
| `heightDp` | 높이 (dp) | `640` |
| `showSystemUi` | 시스템 UI 표시 | `true` |
| `uiMode` | 다크/라이트 모드 | `Configuration.UI_MODE_NIGHT_YES` |
| `device` | 디바이스 프리셋 | `Devices.PIXEL_4` |
| `locale` | 언어 설정 | `"ko"` |
| `fontScale` | 폰트 크기 배율 | `1.5f` |

### 2. @PreviewParameter

**동적 데이터**로 여러 Preview를 자동 생성합니다.

```kotlin
class UserProvider : PreviewParameterProvider<User> {
    override val values = sequenceOf(
        User("Alice", 25),
        User("Bob", 30),
        User("Charlie", 35)
    )
}

@Preview
@Composable
fun UserCardPreview(
    @PreviewParameter(UserProvider::class) user: User
) {
    UserCard(user)
}
// 3개의 Preview 자동 생성!
```

### 3. Multipreview

**커스텀 어노테이션**으로 여러 Preview를 묶습니다.

```kotlin
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class LightDarkPreview

@LightDarkPreview
@Composable
fun MyCardPreview() {
    MyCard()
}
// Light + Dark 두 버전 자동 생성!
```

### 4. 내장 Multipreview 템플릿 (1.6.0+)

**androidx.compose.ui:ui-tooling-preview 1.6.0** 부터 제공되는 **내장 Multipreview 템플릿**입니다.

| 템플릿 | 설명 |
|--------|------|
| `@PreviewScreenSizes` | 다양한 화면 크기 (Phone, Tablet 등) |
| `@PreviewFontScales` | 폰트 크기 배율 (1.0f, 1.5f, 2.0f) |
| `@PreviewLightDark` | Light/Dark 모드 |
| `@PreviewDynamicColors` | Dynamic Colors (Material You) |

```kotlin
@PreviewScreenSizes
@PreviewFontScales
@PreviewLightDark
@Composable
fun MyScreenPreview() {
    MyTheme {
        MyScreen()
    }
}
// 화면 크기, 폰트 크기, 테마 조합의 여러 Preview 자동 생성!
```

---

## 문제 상황: Preview 없이 개발

### 반복적인 빌드/실행

```kotlin
@Composable
fun UserCard(user: User) {
    Card {
        Text(user.name)
        Text("${user.age}세")
    }
}

// UI를 확인하려면?
// 1. 앱 빌드 (1-2분)
// 2. 에뮬레이터/기기에서 실행
// 3. 해당 화면까지 네비게이션
// 4. 결과 확인
// 5. 수정 후 1번으로...
```

### 코드 중복

```kotlin
// 여러 상태를 테스트하려면 Preview 함수 반복
@Preview @Composable
fun UserCardPreview1() = UserCard(User("Alice", 25))

@Preview @Composable
fun UserCardPreview2() = UserCard(User("Bob", 30))

@Preview @Composable
fun UserCardPreview3() = UserCard(User("Charlie", 35))

// 10개 상태가 있다면 10개 함수...
```

### 다양한 환경 테스트 어려움

```kotlin
// Light/Dark, Phone/Tablet 조합 = 4개 함수 필요
@Preview @Composable
fun LightPhone() { ... }

@Preview @Composable
fun DarkPhone() { ... }

@Preview @Composable
fun LightTablet() { ... }

@Preview @Composable
fun DarkTablet() { ... }
```

---

## 해결책: @Preview 활용

### 1. 기본 Preview

```kotlin
@Preview(
    name = "사용자 카드",
    showBackground = true,
    widthDp = 320
)
@Composable
fun UserCardPreview() {
    PreviewTheme {
        UserCard(User("Alice", 25))
    }
}
```

### 2. PreviewParameter로 코드 중복 제거

```kotlin
class UserProvider : PreviewParameterProvider<User> {
    override val values = sequenceOf(
        User("Alice", 25),
        User("Bob", 30),
        User("Charlie", 35)
    )
}

@Preview(showBackground = true)
@Composable
fun UserCardPreview(
    @PreviewParameter(UserProvider::class) user: User
) {
    PreviewTheme {
        UserCard(user)
    }
}
// 하나의 함수로 3개 Preview!
```

### 3. Multipreview로 환경 테스트

```kotlin
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class LightDarkPreview

@Preview(name = "Phone", widthDp = 360, heightDp = 640)
@Preview(name = "Tablet", widthDp = 800, heightDp = 1280)
annotation class DevicePreview

@LightDarkPreview
@DevicePreview
@Composable
fun MyScreenPreview() {
    PreviewTheme {
        MyScreen()
    }
}
// Light Phone, Dark Phone, Light Tablet, Dark Tablet 4개 자동 생성!
```

---

## 사용 시나리오

### 1. 상태별 UI 테스트

```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: String) : UiState()
    data class Error(val message: String) : UiState()
}

class UiStateProvider : PreviewParameterProvider<UiState> {
    override val values = sequenceOf(
        UiState.Loading,
        UiState.Success("데이터 로드 완료"),
        UiState.Error("네트워크 오류")
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview(
    @PreviewParameter(UiStateProvider::class) state: UiState
) {
    MyScreen(state)
}
```

### 2. 폰트 크기 접근성 테스트

```kotlin
@Preview(fontScale = 1.0f, name = "기본")
@Preview(fontScale = 1.5f, name = "큰 글꼴")
@Preview(fontScale = 2.0f, name = "매우 큰 글꼴")
annotation class FontScalePreview

@FontScalePreview
@Composable
fun TextPreview() {
    Text("접근성 테스트")
}
```

### 3. 다국어 테스트

```kotlin
@Preview(locale = "ko", name = "한국어")
@Preview(locale = "en", name = "English")
@Preview(locale = "ja", name = "日本語")
annotation class LocalePreview
```

---

## 주의사항

### 1. Preview 함수는 파라미터 없이

```kotlin
// ❌ 잘못됨 - 일반 파라미터 사용 불가
@Preview
@Composable
fun UserCardPreview(user: User) { ... }

// ✅ 올바름 - @PreviewParameter 사용
@Preview
@Composable
fun UserCardPreview(
    @PreviewParameter(UserProvider::class) user: User
) { ... }

// ✅ 올바름 - 내부에서 데이터 생성
@Preview
@Composable
fun UserCardPreview() {
    UserCard(User("Sample", 25))
}
```

### 2. Preview는 Release 빌드에서 제외

```kotlin
// Preview 함수는 앱에 포함되지 않음
// debugImplementation(libs.androidx.compose.ui.tooling)
// → debug 빌드에서만 사용
```

### 3. Context 접근 제한

```kotlin
// Preview에서는 실제 Context가 없을 수 있음
// LocalContext.current 사용 시 주의
@Preview
@Composable
fun MyPreview() {
    // 실제 리소스 접근은 제한적
}
```

### 4. limit 파라미터로 Preview 수 제한

```kotlin
@Preview
@Composable
fun UserCardPreview(
    @PreviewParameter(UserProvider::class, limit = 2) user: User
) {
    UserCard(user)
}
// 처음 2개만 Preview 생성
```

---

## Android Studio 2025 Preview 도구

Android Studio의 최신 도구들을 활용하면 Preview 작업이 더욱 효율적입니다.

### 1. Gemini를 통한 Preview 자동 생성

Composable 함수에서 우클릭 > **Gemini > Generate Preview**를 선택하면 AI가 자동으로 적절한 Preview 함수를 생성합니다.

```kotlin
// Gemini가 자동 생성한 Preview 예시
@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    MyTheme {
        UserProfile(
            user = User("Sample User", "sample@email.com")
        )
    }
}
```

### 2. Resizable Preview

Preview 패널에서 **Focus 모드**로 진입 후, 가장자리를 드래그하여 실시간으로 크기를 조절할 수 있습니다.

- 다양한 화면 크기에서 UI 반응 확인
- 원하는 크기를 새로운 `@Preview` 어노테이션으로 **한 클릭 저장**

### 3. Transform UI

Preview에서 우클릭 > **Transform UI**를 선택하고, 자연어로 변경 사항을 설명합니다.

```
예시: "버튼을 더 크게 만들고 파란색으로 변경해줘"
```

### 4. Compose Preview Screenshot Testing

Preview를 기반으로 스크린샷 테스트를 자동화하여 UI 회귀를 방지합니다.

```kotlin
// Screenshot Testing 설정
@Preview(showBackground = true)
@Composable
fun UserCardScreenshot() {
    UserCard(User("Test", 25))
}

// 테스트 실행 시 HTML 리포트 생성
// UI 변경 사항을 시각적으로 비교 가능
```

### 5. Devices API 업데이트

최신 디바이스가 `Devices` 객체에 추가되었습니다.

```kotlin
@Preview(device = Devices.PIXEL_FOLD)      // 폴더블
@Preview(device = Devices.TABLET)          // 태블릿
@Preview(device = Devices.PIXEL_8_PRO)     // Pixel 8 Pro
```

`@PreviewScreenSizes`에 **Tablet Portrait**가 추가되어 큰 화면 지원 테스트가 더 쉬워졌습니다.

---

## 베스트 프랙티스

### 1. ViewModel과 Preview 분리

Preview는 ViewModel과 함께 동작하지 않습니다. **UI 로직과 비즈니스 로직을 분리**하세요.

```kotlin
// 좋은 예시: 상태를 파라미터로 받음
@Composable
fun UserScreen(
    user: User,
    onEditClick: () -> Unit
) { ... }

// 나쁜 예시: ViewModel을 직접 사용
@Composable
fun UserScreen(viewModel: UserViewModel) { ... }
```

### 2. 내장 템플릿 활용

커스텀 Multipreview를 만들기 전에 **내장 템플릿**을 먼저 고려하세요.

```kotlin
// 내장 템플릿으로 충분한 경우
@PreviewLightDark
@PreviewScreenSizes
@Composable
fun MyPreview() { ... }

// 필요시 커스텀 템플릿 추가
@Preview(name = "Korean", locale = "ko")
@Preview(name = "English", locale = "en")
annotation class LocalePreview
```

### 3. CollectionPreviewParameterProvider 활용

간단한 데이터는 **CollectionPreviewParameterProvider**를 사용하세요.

```kotlin
class SimpleUserProvider : CollectionPreviewParameterProvider<User>(
    listOf(
        User("Alice", 25),
        User("Bob", 30),
        User("Charlie", 35)
    )
)
// PreviewParameterProvider보다 간결!
```

### 4. Preview용 Default 데이터 정의

Preview에서 자주 사용하는 **기본 데이터**를 미리 정의해두세요.

```kotlin
object PreviewData {
    val sampleUser = User("Preview User", 30, "preview@email.com")
    val sampleProducts = listOf(
        Product("MacBook", 2000000),
        Product("iPhone", 1500000)
    )
}

@Preview
@Composable
fun UserCardPreview() {
    UserCard(PreviewData.sampleUser)
}
```

---

## 연습 문제

### 연습 1: 기본 Preview
상품 카드의 Preview를 작성하세요.

### 연습 2: PreviewParameter
주문 상태별 Preview를 PreviewParameterProvider로 구현하세요.

### 연습 3: Multipreview
Phone/Tablet × Light/Dark 조합의 Multipreview 어노테이션을 만드세요.

---

## 다음 학습

- **Animation**: Compose 애니메이션 시스템
- **Scaffold & Theme**: 앱 기본 골격과 테마
- **UI Testing**: Compose UI 테스트

## 참고 자료

- [Preview your UI with composable previews - Android Developers](https://developer.android.com/develop/ui/compose/tooling/previews)
- [What's new in Jetpack Compose December '25 release](https://android-developers.googleblog.com/2025/12/whats-new-in-jetpack-compose-december.html)
- [PreviewParameter API Reference - Android Developers](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameter)
- [5 Tips for Better Compose Previews - Medium](https://medium.com/@domen.lanisnik/5-tips-for-better-compose-previews-0fd51c9aa048)
- [Dynamic Previews with @PreviewParameter - Medium](https://medium.com/@daniyalidrees/dynamic-previews-in-jetpack-compose-with-previewparameter-04a08f0f59cc)
- [Tips for working with Preview - Nimble](https://nimblehq.co/blog/tips-for-working-with-preview-in-jetpack-compose)
