# Compose Preview Testing 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `@Preview` | Compose에서 UI 미리보기 설정 | [📚 학습하기](../../basics/preview/README.md) |
| `Composable` | 기본 Composable 함수 작성 | [📚 학습하기](../../basics/composable_function/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**PreviewParameter**는 하나의 Preview 함수로 여러 데이터를 자동으로 테스트할 수 있게 해주는 Compose 도구입니다.

마치 **"샘플 데이터 공장"**과 같습니다:
- Provider가 여러 데이터를 제공하면
- Android Studio가 각 데이터마다 Preview를 자동 생성합니다

## 핵심 특징

1. **코드 중복 제거**: 하나의 Preview 함수로 모든 상태 테스트
2. **자동 Preview 생성**: Provider의 각 값마다 Preview 자동 생성
3. **Multipreview 조합**: 다크모드, 폰트 스케일 등 설정과 조합 가능
4. **유지보수 용이**: 데이터 추가 시 Provider만 수정

---

## 문제 상황: 수동 Preview 관리의 비효율

### 시나리오

사용자 프로필 카드를 4가지 상태로 Preview 해야 합니다:
- 일반 사용자
- 인증된 사용자
- 긴 이름 사용자
- 이메일 없는 사용자

### 잘못된 코드 예시

```kotlin
// 각 상태마다 별도 Preview 함수 작성 - 코드 중복!

@Preview(name = "일반 사용자")
@Composable
fun PreviewNormalUser() {
    MyTheme {
        UserProfileCard(
            user = User("홍길동", "hong@example.com", false)
        )
    }
}

@Preview(name = "인증된 사용자")
@Composable
fun PreviewVerifiedUser() {
    MyTheme {
        UserProfileCard(
            user = User("김철수", "kim@example.com", true)
        )
    }
}

@Preview(name = "긴 이름 사용자")
@Composable
fun PreviewLongNameUser() {
    MyTheme {
        UserProfileCard(
            user = User("아주긴이름", "long@example.com", false)
        )
    }
}

@Preview(name = "이메일 없는 사용자")
@Composable
fun PreviewNoEmailUser() {
    MyTheme {
        UserProfileCard(
            user = User("테스트", null, false)
        )
    }
}

// 다크모드도 테스트하려면? 또 4개 추가...
// 폰트 스케일도? 또 4개 추가...
// 총 12개 이상의 Preview 함수가 필요!
```

### 발생하는 문제점

1. **코드 중복**: `MyTheme { UserProfileCard(...) }` 구조가 모든 함수에서 반복
2. **Preview 폭증**: 상태 4개 x 테마 2개 x 폰트 3개 = 24개 Preview 필요
3. **유지보수 어려움**: 새 상태 추가 시 모든 조합에 대해 추가 작업 필요
4. **실수 가능성**: 일부 Preview만 수정하고 나머지 누락 가능

---

## 해결책: @PreviewParameter 사용

### 올바른 코드

```kotlin
// 1. PreviewParameterProvider 구현
class UserProfileProvider : PreviewParameterProvider<UserProfile> {
    override val values: Sequence<UserProfile> = sequenceOf(
        UserProfile("홍길동", "hong@example.com", false),
        UserProfile("김철수", "kim@example.com", true),
        UserProfile("아주긴이름", "long@example.com", false),
        UserProfile("테스트", null, false)
    )
}

// 2. 하나의 Preview로 모든 상태 테스트!
@Preview(showBackground = true)
@Composable
fun PreviewUserProfileCard(
    @PreviewParameter(UserProfileProvider::class) user: UserProfile
) {
    MyTheme {
        UserProfileCard(user = user)
    }
}

// 결과: 4개의 Preview가 자동 생성됨!
```

### 해결되는 이유

1. **데이터와 UI 로직 분리**: Provider에서 데이터만 정의
2. **자동 Preview 생성**: Provider의 각 값마다 별도 Preview 생성
3. **유지보수 용이**: 새 상태 추가 시 Provider만 수정
4. **일관성 보장**: 모든 Preview가 동일한 UI 코드 사용

---

## 사용 시나리오

### 1. PreviewParameterProvider 기본 사용

```kotlin
class UserProfileProvider : PreviewParameterProvider<UserProfile> {
    override val values: Sequence<UserProfile> = sequenceOf(
        UserProfile("홍길동", "hong@example.com", false),
        UserProfile("김철수", "kim@example.com", true)
    )
}

@Preview
@Composable
fun PreviewUserCard(
    @PreviewParameter(UserProfileProvider::class) user: UserProfile
) {
    UserProfileCard(user = user)
}
```

### 2. CollectionPreviewParameterProvider (더 간단한 문법)

```kotlin
// List를 직접 전달하면 자동으로 Sequence로 변환!
class UserProfileCollectionProvider : CollectionPreviewParameterProvider<UserProfile>(
    listOf(
        UserProfile("홍길동", "hong@example.com", false),
        UserProfile("김철수", "kim@example.com", true)
    )
)
```

### 3. Multipreview 어노테이션과 조합

```kotlin
// 커스텀 Multipreview 정의
@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Large Font", fontScale = 1.3f)
annotation class ThemePreviews

// PreviewParameter와 조합
@ThemePreviews
@Composable
fun PreviewUserCard(
    @PreviewParameter(UserProfileProvider::class) user: UserProfile
) {
    MyTheme {
        UserProfileCard(user = user)
    }
}

// 결과: 2가지 상태 x 3가지 설정 = 6개 Preview 자동 생성!
```

### 4. limit 파라미터로 Preview 개수 제한

```kotlin
@Preview
@Composable
fun PreviewUserCard(
    @PreviewParameter(
        UserProfileProvider::class,
        limit = 2  // 처음 2개만 Preview 생성
    )
    user: UserProfile
) {
    UserProfileCard(user = user)
}
```

### 5. getDisplayName으로 커스텀 Preview 이름

```kotlin
class UserProfileProvider : PreviewParameterProvider<UserProfile> {
    private val users = listOf(
        UserProfile("홍길동", "hong@example.com", false),
        UserProfile("김철수", "kim@example.com", true)
    )

    override val values = users.asSequence()

    // 2025 신기능: 커스텀 Preview 이름
    override fun getDisplayName(index: Int): String? {
        return when (index) {
            0 -> "일반 사용자"
            1 -> "인증된 사용자"
            else -> null
        }
    }
}
```

### 6. 내장 Provider: LoremIpsum

```kotlin
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

@Preview
@Composable
fun PreviewTextCard(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    Card {
        Text(text = text, maxLines = 3)
    }
}
```

---

## 주의사항

1. **Provider 클래스는 인자 없는 생성자 필요**: 리플렉션으로 인스턴스화됨
2. **너무 많은 Preview는 IDE 성능 저하**: 필요시 `limit` 파라미터 사용
3. **Multipreview 남용 주의**: 의미 있는 조합만 테스트
4. **Preview는 런타임 동작을 보장하지 않음**: 실제 기기 테스트도 필요

---

## 연습 문제

### 연습 1: 버튼 상태 Provider 만들기 (쉬움)

`ButtonState` sealed class (Enabled, Disabled, Loading)를 위한 `PreviewParameterProvider`를 구현하세요.

**힌트**:
- `sequenceOf()`로 3가지 상태 제공
- `@PreviewParameter`로 Preview 함수에 연결

### 연습 2: 제품 카드 Collection Provider (중간)

`CollectionPreviewParameterProvider`를 사용하여 5가지 이상의 제품 데이터를 제공하세요.

**힌트**:
- 기본 제품, 할인 제품, 긴 이름, 무료, 품절 등 다양한 케이스 포함
- `listOf()`를 직접 생성자에 전달

### 연습 3: Multipreview + PreviewParameter 조합 (어려움)

주문 상태 카드를 다크모드, 라이트모드, 큰 폰트로 테스트하는 종합 Preview를 만드세요.

**힌트**:
- `@Preview`를 여러 개 붙인 annotation class 정의
- `uiMode = Configuration.UI_MODE_NIGHT_YES`로 다크모드
- `fontScale = 1.3f`로 큰 폰트

---

## 다음 학습

- [Screenshot Testing](../screenshot_testing/): Paparazzi/Roborazzi로 UI 스냅샷 테스트
- [Compose Testing](../compose_testing/): UI 동작 테스트

---

## 참고 자료

- [Android Developers - Composable Previews](https://developer.android.com/develop/ui/compose/tooling/previews)
- [PreviewParameter API Reference](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameter)
- [CollectionPreviewParameterProvider 활용](https://dladukedev.com/articles/038_compose_collectionpreviewparameterprovider/)
