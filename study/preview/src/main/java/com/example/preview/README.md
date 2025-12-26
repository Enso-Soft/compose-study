# @Preview 어노테이션 학습

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
- [Dynamic Previews with @PreviewParameter - Medium](https://medium.com/@daniyalidrees/dynamic-previews-in-jetpack-compose-with-previewparameter-04a08f0f59cc)
- [Better Previews in Compose with Custom Annotations - Medium](https://medium.com/@mortitech/better-previews-in-compose-with-custom-annotations-dc49b94ff579)
- [Tips for working with Preview - Nimble](https://nimblehq.co/blog/tips-for-working-with-preview-in-jetpack-compose)
