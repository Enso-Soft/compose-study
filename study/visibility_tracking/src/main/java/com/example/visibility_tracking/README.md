# Visibility Tracking API 학습

## 개념

**Visibility Tracking API**는 Compose 1.8+(BOM 2025.05.01)에서 도입된 고성능 가시성 추적 API입니다. Composable이 화면에 보이는지, 얼마나 보이는지, 얼마나 오래 보였는지를 효율적으로 추적할 수 있습니다.

### 왜 필요한가?

기존에는 `onGloballyPositioned`를 사용해 수동으로 가시성을 계산해야 했습니다. 이 방식은:
- 매 레이아웃 패스마다 콜백 발생 (성능 저하)
- 디바운스/스로틀 미지원
- 가시성 계산 로직을 직접 구현해야 함
- LazyColumn에서 특히 문제가 심각

새로운 Visibility Tracking API는 이 모든 문제를 해결합니다.

## 핵심 API

### 1. onVisibilityChanged (High-level)

가시성 변화를 추적하는 고수준 API입니다.

```kotlin
Modifier.onVisibilityChanged(
    minFractionVisible = 0.5f,  // 50% 이상 보일 때
    minDurationMs = 1000         // 1초 이상 보일 때
) { isVisible ->
    if (isVisible) {
        // 가시성 조건 충족
    }
}
```

### 2. onLayoutRectChanged (Low-level)

`onVisibilityChanged`의 기반이 되는 저수준 API입니다.

```kotlin
Modifier.onLayoutRectChanged { layoutInfo ->
    val boundsInRoot = layoutInfo.boundsInRoot
    val boundsInWindow = layoutInfo.boundsInWindow
    val boundsInScreen = layoutInfo.boundsInScreen
    // 직접 가시성 로직 구현
}
```

## 주요 파라미터

| 파라미터 | 타입 | 기본값 | 설명 |
|---------|------|--------|------|
| `minFractionVisible` | Float | 1.0f | 가시성 판정에 필요한 최소 노출 비율 (0.0f ~ 1.0f) |
| `minDurationMs` | Long | 0 | 콜백 트리거까지 필요한 최소 가시 시간 (밀리초) |

### minFractionVisible 예시

- `1.0f`: 100% 완전히 보일 때만 visible
- `0.5f`: 50% 이상 보일 때 visible
- `0.0f`: 조금이라도 보이면 visible

### minDurationMs 예시

- `0`: 즉시 콜백 (기본값)
- `500`: 0.5초 이상 보일 때 콜백
- `1000`: 1초 이상 보일 때 콜백

## 문제 상황: 기존 방식의 한계

### 잘못된 코드 예시 1: onGloballyPositioned 사용

```kotlin
// 성능 문제 발생!
LazyColumn {
    items(1000) { index ->
        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    // 매 레이아웃 패스마다 1000개 아이템 모두에서 호출!
                    val bounds = coordinates.boundsInWindow()
                    val isVisible = bounds.top < screenHeight && bounds.bottom > 0
                    if (isVisible) {
                        analytics.trackImpression(index)
                    }
                }
        )
    }
}
```

### 발생하는 문제점
1. 스크롤마다 모든 아이템에서 콜백 발생
2. 디바운스 없이 과도한 호출
3. 수동 가시성 계산 필요
4. 중복 impression 추적 가능

### 잘못된 코드 예시 2: LaunchedEffect 오용

```kotlin
// 광고 노출 추적에 LaunchedEffect 사용 - 잘못된 방법!
LazyColumn {
    items(ads) { ad ->
        AdCard(ad)
        LaunchedEffect(ad.id) {
            // Prefetch로 인해 화면 표시 전에 실행될 수 있음!
            analytics.trackAdImpression(ad.id)
        }
    }
}
```

### 발생하는 문제점
1. Lazy 레이아웃의 prefetch로 실제 화면 표시 전 실행
2. 사용자가 보지 않은 광고도 impression으로 카운트
3. 부정확한 분석 데이터

## 해결책: Visibility Tracking API 사용

### 올바른 코드 1: 기본 가시성 추적

```kotlin
Text(
    text = "Tracked Content",
    modifier = Modifier
        .onVisibilityChanged { isVisible ->
            if (isVisible) {
                Log.d("Visibility", "Content is now visible")
            }
        }
        .padding(8.dp)
)
```

### 올바른 코드 2: 광고 노출 추적

```kotlin
@Composable
fun AdFeed(ads: List<Ad>, analytics: Analytics) {
    val trackedImpressions = remember { mutableSetOf<String>() }

    LazyColumn {
        items(ads, key = { it.id }) { ad ->
            AdCard(
                ad = ad,
                modifier = Modifier
                    .onVisibilityChanged(
                        minFractionVisible = 0.5f,  // 50% 이상 보일 때
                        minDurationMs = 1000        // 1초 이상 보일 때
                    ) { visible ->
                        if (visible && ad.id !in trackedImpressions) {
                            trackedImpressions.add(ad.id)
                            analytics.trackImpression(ad.id)
                        }
                    }
            )
        }
    }
}
```

### 올바른 코드 3: 비디오 자동재생

```kotlin
@Composable
fun VideoFeed(videos: List<Video>) {
    LazyColumn {
        items(videos, key = { it.id }) { video ->
            VideoPlayer(
                video = video,
                modifier = Modifier
                    .onVisibilityChanged(
                        minDurationMs = 500,       // 0.5초 후 재생
                        minFractionVisible = 1f    // 완전히 보일 때만
                    ) { visible ->
                        if (visible) video.play() else video.pause()
                    }
            )
        }
    }
}
```

## 사용 시나리오

### 1. Analytics & Impression Tracking
- 광고 노출 추적 (50% 이상, 1초 이상)
- 콘텐츠 조회수 측정
- 사용자 행동 분석

### 2. Media Playback Control
- 비디오 자동 재생/일시정지
- 오디오 플레이어 제어
- 애니메이션 시작/중지

### 3. Performance Optimization
- 화면에 보이는 아이템만 데이터 로드
- 네트워크 요청 최적화
- 리소스 사용량 감소

### 4. Lazy Loading & Prefetching
- 다음 페이지 프리페치 트리거
- 이미지 지연 로딩
- 무한 스크롤 구현

## 주의사항

### 1. Modifier 순서가 중요합니다
```kotlin
// 올바른 순서: onVisibilityChanged 먼저
Modifier
    .onVisibilityChanged { ... }  // 먼저!
    .padding(8.dp)
    .background(Color.White)

// 잘못된 순서: padding 후에 추적하면 padding 영역 제외
Modifier
    .padding(8.dp)
    .onVisibilityChanged { ... }  // padding 제외한 영역만 추적
```

### 2. onFirstVisible은 Deprecated 예정
- Compose 1.11에서 deprecated 예정
- Lazy 레이아웃에서 아이템 재구성 시 재호출 문제
- 대신 `onVisibilityChanged` + 중복 방지 로직 사용

### 3. LaunchedEffect를 visibility 신호로 사용하지 마세요
- Prefetch로 인해 실제 가시성과 무관하게 실행될 수 있음
- 반드시 `onVisibilityChanged` 사용

### 4. 콜백에서 무거운 작업 피하기
```kotlin
// 피해야 할 패턴: 콜백에서 직접 무거운 작업
.onVisibilityChanged { visible ->
    if (visible) {
        // 무거운 네트워크 요청 X
        heavyNetworkCall()
    }
}

// 권장 패턴: 상태 변경 후 LaunchedEffect에서 처리
.onVisibilityChanged { visible ->
    isVisible = visible  // 상태만 변경
}

LaunchedEffect(isVisible) {
    if (isVisible) {
        // 여기서 무거운 작업 수행
    }
}
```

## BOM 버전 요구사항

```kotlin
// build.gradle.kts
implementation(platform("androidx.compose:compose-bom:2025.05.01"))
// 또는 최신 버전
implementation(platform("androidx.compose:compose-bom:2025.08.00"))
```

- `onLayoutRectChanged`: Compose UI 1.8+ (BOM 2025.04.00+)
- `onVisibilityChanged`: Compose UI 1.9+ (BOM 2025.08.00+)

## 연습 문제

### 연습 1: 기본 가시성 로깅 (초급)
LazyColumn의 아이템이 50% 이상 보일 때 로그를 출력하세요.

### 연습 2: 광고 노출 분석 (중급)
광고가 50% 이상, 1초 이상 보일 때 노출을 기록하세요. 중복 기록을 방지해야 합니다.

### 연습 3: 비디오 자동재생 (고급)
비디오가 완전히 보이고 0.5초 후 자동재생, 화면에서 벗어나면 일시정지하세요.

## 다음 학습

- `snapshotFlow`: 상태 변화를 Flow로 변환
- `LazyListState`: 스크롤 위치 기반 로직
- `Modifier.Node`: 커스텀 고성능 Modifier 구현

## 참고 자료

- [Visibility tracking in Compose - Android Developers](https://developer.android.com/develop/ui/compose/layouts/visibility-modifiers)
- [What's new in Jetpack Compose August '25 release](https://android-developers.googleblog.com/2025/08/whats-new-in-jetpack-compose-august-25-release.html)
- [Visibility APIs in Jetpack Compose 1.9 - ProAndroidDev](https://proandroiddev.com/visibility-apis-in-jetpack-compose-1-9-easier-cleaner-but-not-quite-there-yet-9bbfdb60bd6b)
