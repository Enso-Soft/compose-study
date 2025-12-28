# Compose Compiler Metrics & Reports 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `stability` | Compose의 Stability 개념과 @Stable, @Immutable | [📚 학습하기](../../state/stability/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Compose Compiler Metrics**는 Compose 컴파일러가 생성하는 **성능 분석 리포트**입니다.
Composable 함수와 클래스의 **Stability(안정성)** 상태를 분석하여
불필요한 Recomposition의 원인을 찾고 최적화할 수 있게 도와줍니다.

```kotlin
// build.gradle.kts에서 활성화
composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}
```

## 핵심 특징

### 1. 생성되는 파일

| 파일 | 설명 |
|------|------|
| `<module>-classes.txt` | 클래스의 Stability 분석 결과 |
| `<module>-composables.txt` | Composable 함수의 restartable/skippable 상태 |
| `<module>-composables.csv` | CSV 형식 (스프레드시트 분석용) |

### 2. Stability 분류

| 분류 | 의미 | 예시 |
|------|------|------|
| **Stable** | 불변 타입, Compose가 변경 감지 가능 | `Int`, `String`, `@Immutable data class` |
| **Unstable** | 가변 가능 타입, 항상 recomposition | `List<T>`, `Map<K,V>`, 외부 라이브러리 클래스 |
| **Runtime** | 런타임에 결정 (제네릭 등) | `T: Any` |

### 3. Skippable vs Restartable

```
restartable skippable scheme("[...]") fun MyComposable(...)
   ^            ^
   |            └── 파라미터 변경 없으면 건너뜀 (이상적!)
   └── Recomposition 가능한 함수
```

- **Restartable만 있음**: 파라미터가 변경되지 않아도 매번 recompose
- **Skippable도 있음**: 파라미터가 같으면 skip (성능 최적화)

## 문제 상황: Unstable 클래스로 인한 불필요한 Recomposition

### 잘못된 코드 예시

```kotlin
// List<String>은 인터페이스 → Unstable!
data class UserProfile(
    val id: Long,
    val name: String,
    val interests: List<String>  // 문제!
)

@Composable
fun ProfileCard(user: UserProfile) {
    // 이 Composable은 절대 skip되지 않음
    // 부모가 recompose될 때마다 함께 recompose
    Card {
        Text(user.name)
        user.interests.forEach { Text(it) }
    }
}
```

### Compiler Report 예시

```
unstable class UserProfile {
    stable val id: Long
    stable val name: String
    unstable val interests: List<String>
    <runtime stability> = Unstable
}

restartable scheme("[...]") fun ProfileCard(
    unstable user: UserProfile  // skippable 아님!
)
```

### 발생하는 문제점

1. `List<String>`은 인터페이스이므로 **Unstable**로 분류
2. 하나라도 unstable 필드가 있으면 **전체 클래스가 Unstable**
3. Unstable 파라미터를 받는 Composable은 **Skippable이 아님**
4. 부모 Composable이 recompose될 때마다 **자식도 불필요하게 recompose**
5. 개발자는 **원인을 파악하기 어려움** (Compiler Metrics 없이는!)

## 해결책: Compiler Metrics 분석 & Stability 수정

### 해결책 1: Compiler Metrics 활성화

```kotlin
// build.gradle.kts
composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
}
```

**중요**: 반드시 **release 빌드**로 실행하세요!

```bash
./gradlew :app:assembleRelease
# 리포트 위치: app/build/compose_compiler/
```

### 해결책 2: 리포트 파일 분석

**classes.txt 확인**:
```
unstable class UserProfile {
    stable val id: Long
    stable val name: String
    unstable val interests: List<String>  // 원인 발견!
    <runtime stability> = Unstable
}
```

**composables.txt 확인**:
```
restartable scheme("[...]") fun ProfileCard(
    unstable user: UserProfile  // skippable 없음 = 문제!
)
```

### 해결책 3: Stability 수정 방법

**Option A - @Immutable 어노테이션**:
```kotlin
@Immutable
data class UserProfile(
    val id: Long,
    val name: String,
    val interests: List<String>  // 컴파일러에게 "이건 안 바뀐다" 약속
)
```

**Option B - ImmutableList 사용 (권장)**:
```kotlin
import kotlinx.collections.immutable.ImmutableList

data class UserProfile(
    val id: Long,
    val name: String,
    val interests: ImmutableList<String>  // 타입 자체가 Stable
)
```

**Option C - stability-config.txt 파일**:
```
// stability-config.txt
com.example.UserProfile
java.time.LocalDate
```

```kotlin
// build.gradle.kts
composeCompiler {
    stabilityConfigurationFile = file("stability-config.txt")
}
```

### 해결책 4: Strong Skipping Mode (Kotlin 2.0.20+)

**Kotlin 2.0.20부터 기본 활성화**:
- Unstable 파라미터도 **참조 동등성(===)** 으로 비교하여 skip 가능
- Lambda 자동 memoization (캡처된 값을 key로 사용)
- 모든 restartable composable이 자동으로 skippable

```kotlin
// Kotlin 2.0.20+ (기본 활성화, 별도 설정 불필요)
// Strong Skipping이 자동으로 적용됩니다

// Kotlin 2.0.20 미만 버전에서 수동 활성화
composeCompiler {
    enableStrongSkippingMode = true  // deprecated in 2.0.20+
}
```

**Strong Skipping 제외하기** (특정 Composable 최적화 제어):
```kotlin
// 이 Composable은 Strong Skipping에서 제외됨
@NonSkippableComposable
@Composable
fun AlwaysRecompose() {
    // 항상 recompose 필요한 경우
}
```

**주의사항**: 같은 인스턴스 참조를 유지해야 skip됨!
```kotlin
// Bad: 매번 새 List 생성 -> skip 안 됨
ProfileCard(user.copy(interests = listOf("A", "B")))

// Good: 참조 유지 -> skip됨
ProfileCard(user)
```

## Android Studio Stability Analyzer 활용

### 설치 방법

1. Android Studio > Settings > Plugins > Marketplace
2. "Compose Stability Analyzer" 검색 및 설치
3. IDE 재시작

### 기능

| 기능 | 설명 |
|------|------|
| **Gutter Icons** | 에디터 왼쪽에 색상 점 표시 |
| **Hover Tooltips** | 마우스 오버 시 상세 정보 |
| **Inline Hints** | 파라미터 옆에 stability 배지 |

### Gutter Icon 색상

| 색상 | 의미 |
|------|------|
| 녹색 | Skippable (최적화됨) |
| 노란색 | Restartable만 (개선 필요) |
| 빨간색 | Unstable 파라미터 (문제) |

## 사용 시나리오

### 1. 성능 문제 진단
- 앱이 느린데 원인을 모를 때
- Layout Inspector에서 recomposition이 많이 보일 때
- Compiler Metrics로 unstable 클래스/함수 식별

### 2. 코드 리뷰
- PR 머지 전 stability 영향 확인
- CI/CD에서 Metrics 리포트 생성 및 비교

### 3. 라이브러리 개발
- 외부에 제공하는 Composable의 stability 보장
- @Stable/@Immutable 어노테이션 적절히 사용

## 주의사항

1. **조기 최적화 금지**
   - 실제 성능 문제가 있을 때만 최적화
   - Macrobenchmark로 먼저 측정

2. **@Immutable 남용 주의**
   - 실제로 불변이 아닌데 @Immutable 붙이면 버그 발생
   - 컴파일러 신뢰를 깨뜨림

3. **Strong Skipping의 한계**
   - 같은 참조여도 **내용이 바뀌면** UI 업데이트 안 됨
   - 이 경우 @Immutable wrapper나 ImmutableList 사용

4. **Release 빌드 필수**
   - Debug 빌드에서는 최적화가 다르게 적용됨
   - 정확한 분석을 위해 release로 빌드

## 연습 문제

1. **Unstable 클래스를 Stable로 만들기**
   - List를 포함한 data class를 @Immutable로 수정
   - Recomposition 카운트 변화 확인

2. **Composable 함수 Skippable하게 만들기**
   - Lambda와 List 파라미터를 최적화
   - Compiler Report로 결과 확인

3. **Compiler Report 해석 및 문제 진단**
   - 주어진 Report를 분석하여 문제점 찾기
   - 수정 전후 비교

## 다음 학습

- **stability** 모듈: @Stable, @Immutable 심화
- **recomposition** 모듈: Recomposition 동작 원리
- **derived_state_of** 모듈: 파생 상태로 불필요한 recomposition 방지

## 참고 자료

- [Diagnose stability issues - Android Developers](https://developer.android.com/develop/ui/compose/performance/stability/diagnose)
- [Strong skipping mode - Android Developers](https://developer.android.com/develop/ui/compose/performance/stability/strongskipping)
- [Fix stability issues - Android Developers](https://developer.android.com/develop/ui/compose/performance/stability/fix)
- [Compose Compiler Metrics Design Doc](https://android.googlesource.com/platform/frameworks/support/+/93a27bf544adcea26384f6612da46fef0275f631/compose/compiler/design/compiler-metrics.md)
- [Compose Stability Inference - GitHub](https://github.com/skydoves/compose-stability-inference)
- [Compose Performance Guide - GitHub](https://github.com/skydoves/compose-performance)
