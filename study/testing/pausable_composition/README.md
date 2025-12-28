# Pausable Composition 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `LazyColumn` | 효율적인 스크롤 리스트 구현 | [📚 학습하기](../../list/lazy_column/README.md) |
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Pausable Composition**은 Compose 런타임이 컴포지션(UI 구성 작업)을 **일시정지**하고 **여러 프레임에 분산**하여 화면 끊김(jank)을 방지하는 성능 최적화 기술입니다.

2025년 12월 Compose 1.10 릴리스부터 **기본으로 활성화**되어, 별도 설정 없이 자동으로 적용됩니다.

## 핵심 특징

1. **자동 작업 분산**: 복잡한 UI 구성을 여러 프레임에 나누어 처리합니다
2. **프레임 보호**: 현재 화면 렌더링이 우선, 남은 작업은 다음 프레임에 처리합니다
3. **Lazy 레이아웃 최적화**: LazyColumn, LazyRow 등의 prefetch 과정에서 동작합니다

---

## 문제 상황: 복잡한 리스트 스크롤 시 화면 끊김

### 시나리오

소셜 미디어 피드 앱을 개발하고 있습니다. 각 피드 아이템에는:
- 프로필 이미지
- 사용자 이름 및 시간 정보
- 본문 텍스트
- 미디어 콘텐츠 (이미지/동영상)
- 좋아요/댓글/공유 버튼

빠르게 스크롤하면 **화면이 끊기는 현상(jank)**이 발생합니다.

### 프레임 예산이란?

화면이 부드럽게 보이려면 **초당 60프레임**을 유지해야 합니다.
```
1초 = 1000ms
1000ms / 60프레임 = 약 16ms

각 프레임마다 16ms 안에 모든 작업을 끝내야 합니다!
```

### 왜 문제가 발생하는가?

**기존 방식 (Pausable Composition 이전)**:
```
아이템 1 구성: 5ms
아이템 2 구성: 5ms
아이템 3 구성: 5ms
아이템 4 구성: 5ms
아이템 5 구성: 5ms
─────────────────
총 시간: 25ms  <- 16ms 초과! JANK 발생!
```

비유하자면, **모든 택배를 한 트럭에 다 실어야 출발**하는 것과 같습니다.
큰 짐이 있으면 출발이 늦어지죠.

### 발생하는 문제점

1. **프레임 드랍**: 16ms를 초과하면 해당 프레임을 건너뜀
2. **스크롤 끊김**: 사용자가 "버벅거림"을 느낌
3. **사용자 경험 저하**: 앱이 느리다는 인상

---

## 해결책: Pausable Composition

### 어떻게 해결하는가?

**Pausable Composition 방식**:
```
프레임 1: 아이템 1, 2, 3 구성 (15ms) <- OK!
프레임 2: 아이템 4, 5 구성 (10ms)    <- OK!
프레임 3: 아이템 6, 7, 8 구성 (15ms) <- OK!
```

비유하자면, **시간이 되면 실린 만큼만 먼저 출발**하고, 나머지는 다음 트럭에 싣는 방식입니다.

### 핵심 메커니즘 3가지

#### 1. 사전 준비 (Pre-work)
화면에 보이기 전에 미리 아이템을 구성합니다(prefetch).
```
[보이는 영역]    [미리 준비 중]
  아이템 1         아이템 4 <- prefetch
  아이템 2         아이템 5 <- prefetch
  아이템 3
```

#### 2. 협력적 일시중지 (Cooperative Pause)
"지금 멈춰야 할까요?"라고 묻는 함수(shouldPause)가 있습니다.
```kotlin
// 런타임 내부 동작 (개념 설명)
while (!isComplete) {
    composeNextPart()

    if (shouldPause()) {  // "멈춰야 하나요?"
        break  // "네, 다음 프레임에 계속할게요"
    }
}
```

#### 3. 배치 처리 (Batch Apply)
여러 작은 변경사항을 모아서 한 번에 적용합니다.
```
기록: 노드 A 추가
기록: 노드 B 추가
기록: 노드 C 수정
──────────────────
apply() -> 한 번에 적용!
```

### API 구조 (내부 동작 이해용)

Pausable Composition은 내부 API이므로 직접 호출하지 않습니다.
원리를 이해하면 왜 성능이 개선되는지 알 수 있습니다.

```kotlin
// PausableComposition 인터페이스 (Compose 내부)
sealed interface PausableComposition : ReusableComposition {
    // 일시정지 가능한 컨텐츠 설정
    fun setPausableContent(content: @Composable () -> Unit): PausedComposition
}

// PausedComposition 인터페이스 (일시정지된 상태 제어)
sealed interface PausedComposition {
    val isComplete: Boolean  // 완료 여부

    fun resume(shouldPause: ShouldPauseCallback): Boolean  // 계속 진행
    fun apply()   // 변경사항 적용
    fun cancel()  // 취소
}
```

### 상태 흐름

```
InitialPending     "작업 시작 준비"
       |
       v
RecomposePending   "구성 진행 중 (일시정지 가능)"
       |
       v
ApplyPending       "변경사항 적용 준비"
       |
       v
Applied            "완료!"
```

---

## 성능 개선 효과

### Compose vs View 비교

Google 내부 벤치마크에서 Compose의 스크롤 jank가 **0.2%**까지 감소했습니다.
이제 Compose는 View 시스템과 **동등한 스크롤 성능**을 달성했습니다.

```
Before Pausable Composition:
[████████████████████████████] 50ms - JANK!

After Pausable Composition:
[████████] [████████] [████████] 각 15ms - OK!
```

---

## 활성화 방법

### Compose 1.10 이상 (2025.12+)
```kotlin
// 별도 설정 불필요! 자동으로 활성화됩니다.

// build.gradle.kts
dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.12.00"))
    // ...
}
```

### Compose 1.9 (수동 활성화 필요)
```kotlin
// Application 클래스에서 설정
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ComposeFoundationFlags.isPausableCompositionInPrefetchEnabled = true
    }
}
```

---

## 관련 API: CacheWindow

Compose 1.9에서 도입된 CacheWindow API와 함께 사용하면 더 효과적입니다.

```kotlin
// 화면 밖 아이템을 미리 준비하는 개수 설정
LazyColumn(
    // Compose 1.9+에서 사용 가능
    // 스크롤 방향으로 추가 아이템 prefetch
) {
    items(feedItems) { item ->
        FeedItemCard(item)
    }
}
```

---

## 개발자가 해야 할 것

Pausable Composition은 **자동으로 동작**하므로, 개발자는:

1. **최신 BOM 사용**: 2025.12.00 이상
2. **성능 측정**: 실제 jank 발생 여부 확인
3. **아이템 복잡도 관리**: 너무 복잡한 아이템은 분리 고려

### 하지 말아야 할 것

```kotlin
// 아이템 내에서 무거운 계산 피하기
@Composable
fun BadFeedItem(item: FeedItem) {
    // 매 recomposition마다 계산 <- 나쁨!
    val processedData = heavyProcessing(item.data)
    Text(processedData)
}

// 올바른 방법: remember 또는 ViewModel에서 처리
@Composable
fun GoodFeedItem(item: FeedItem) {
    val processedData = remember(item.data) {
        heavyProcessing(item.data)  // 한 번만 계산
    }
    Text(processedData)
}
```

---

## 연습 문제

### 연습 1: Jank 측정 도구 만들기 (쉬움)

프레임 렌더링 시간을 측정하고 표시하는 UI를 만들어보세요.

**요구사항**:
- 현재 프레임 렌더링 시간을 실시간으로 표시
- 16ms 이하면 녹색, 초과하면 빨간색으로 표시

**힌트**:
- `System.nanoTime()`으로 시간 측정
- `SideEffect`로 recomposition 추적
- 나노초를 밀리초로 변환: `/ 1_000_000`

---

### 연습 2: Prefetch 상태 모니터링 (중간)

LazyColumn의 현재 상태를 실시간으로 모니터링하는 UI를 만들어보세요.

**요구사항**:
- 현재 보이는 아이템 범위 표시 (첫 번째 ~ 마지막)
- 전체 아이템 개수 표시
- 스크롤하면서 값이 업데이트되는지 확인

**힌트**:
- `rememberLazyListState()` 사용
- `listState.layoutInfo.visibleItemsInfo` 활용

---

### 연습 3: 복잡도에 따른 성능 비교 (어려움)

아이템 복잡도를 조절하며 프레임 시간 변화를 관찰해보세요.

**요구사항**:
- 아이템 복잡도를 3단계로 조절 (간단/보통/복잡)
- 각 단계에서 빠른 스크롤 시 프레임 시간 측정
- 어떤 복잡도에서 jank가 발생하는지 관찰

**힌트**:
- 간단: Text만
- 보통: Text + Image placeholder
- 복잡: Text + Image + 다중 Row/Column

---

## 다음 학습

- **Baseline Profiles**: 앱 시작 성능 최적화
- **Compose Compiler Metrics**: 컴파일 시점 성능 분석
- **LazyListState 활용**: 리스트 상태 관리 심화

---

## 참고 자료

- [Android Developers Blog - December '25 Release](https://android-developers.googleblog.com/2025/12/whats-new-in-jetpack-compose-december.html)
- [Exploring PausableComposition Internals](https://blog.shreyaspatil.dev/exploring-pausablecomposition-internals-in-jetpack-compose)
- [Compose Performance](https://developer.android.com/develop/ui/compose/performance)
