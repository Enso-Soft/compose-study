# retain 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Recomposition 간 상태 유지 | [📚 학습하기](../remember/README.md) |
| `rememberSaveable` | Configuration Change와 Process Death에서 상태 유지 | [📚 학습하기](../remember_saveable/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`retain`은 **Configuration Change(화면 회전 등)에서 직렬화 없이 상태를 유지**할 수 있게 해주는 Compose 1.10+의 새로운 API입니다.

`remember`와 `rememberSaveable` 사이에 위치하며, **직렬화가 불가능한 객체**(ExoPlayer, Bitmap, Flow, Lambda 등)를 Configuration Change에서도 유지할 수 있습니다.

```kotlin
// 기본 사용법
val player = retain { ExoPlayer.Builder(context).build() }
// 화면 회전해도 동일한 player 인스턴스 유지!
```

### 비유로 이해하기

> **retain은 '창고'와 같습니다.**
> - 물건(객체)을 창고에 보관합니다
> - 집(Activity)이 리모델링(Configuration Change)되어도 창고는 그대로입니다
> - 하지만 이사(Process Death)하면 창고도 비워집니다

---

## remember vs retain vs rememberSaveable 비교

| 특성 | remember | retain | rememberSaveable |
|------|----------|--------|------------------|
| Recomposition 유지 | O | O | O |
| Configuration Change 유지 | X | **O** | O |
| Process Death 유지 | X | X | O |
| 직렬화 필요 | 불필요 | **불필요** | 필요 |
| 생명주기 | 가장 짧음 | 중간 | 가장 김 |

### 언제 무엇을 사용할까?

```
시작
  │
  ├── 화면 회전에서 유지 필요 없음? ──Yes──► remember
  │
  ├── 직렬화 가능한 타입? ──Yes──► rememberSaveable
  │     (Int, String, Parcelable 등)
  │
  └── 직렬화 불가능한 타입? ──Yes──► retain
        (ExoPlayer, Bitmap, Flow, Lambda)
```

---

## 핵심 특징

1. **직렬화 불필요**: Bundle에 저장하지 않고 별도 저장소(RetainedValuesStore)에 객체 보관
2. **동일 인스턴스 반환**: Configuration Change 후에도 `===`로 비교하면 true
3. **자동 정리**: Composition 계층에서 제거되면 자동으로 retire(정리)됨
4. **RetainObserver 지원**: 정리 시점에 콜백 받아 리소스 해제 가능

---

## 문제 상황: 직렬화 불가능한 객체 관리

### 시나리오: 미디어 플레이어

사용자가 영상을 재생하는 중에 화면을 회전하면 어떻게 될까요?

### remember 사용 시

```kotlin
@Composable
fun MediaPlayerScreen() {
    val context = LocalContext.current

    // remember 사용 - 화면 회전 시 재생성됨!
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("https://example.com/video.mp4"))
            prepare()
            play()
        }
    }

    // 화면 회전 → player 새로 생성 → 재생 처음부터!
}
```

**발생하는 문제:**
1. 화면 회전마다 ExoPlayer 인스턴스가 새로 생성됨
2. 재생 위치가 0으로 초기화됨
3. 버퍼링을 처음부터 다시 해야 함
4. 사용자 경험 저하

### rememberSaveable 시도 (실패!)

```kotlin
// ExoPlayer는 Parcelable이 아니므로 저장 불가!
val player = rememberSaveable {
    ExoPlayer.Builder(context).build()  // 컴파일 에러 또는 런타임 에러!
}
```

**왜 안 되는가:**
- `rememberSaveable`은 Bundle에 저장하므로 직렬화 필요
- ExoPlayer, Bitmap, Flow 등은 직렬화 불가능
- Saver를 만들어도 인스턴스 자체를 직렬화할 수 없음

---

## 해결책: retain 사용

```kotlin
@Composable
fun MediaPlayerScreen() {
    val context = LocalContext.current.applicationContext  // applicationContext 사용!

    // retain 사용 - 화면 회전에도 동일 인스턴스 유지!
    val player = retain {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("https://example.com/video.mp4"))
            prepare()
            play()
        }
    }

    // 화면 회전해도 player 인스턴스 유지 → 재생 계속!

    DisposableEffect(Unit) {
        onDispose {
            player.release()  // Composable 제거 시 정리
        }
    }
}
```

**해결되는 이유:**
- retain은 객체를 직렬화하지 않고 별도 저장소에 보관
- Configuration Change 후에도 동일한 인스턴스 반환
- 재생 상태, 버퍼 등 모든 상태 유지

---

## 사용 시나리오

### 1. 미디어 플레이어 (ExoPlayer/Media3)
```kotlin
val player = retain { ExoPlayer.Builder(applicationContext).build() }
```

### 2. 이미지 캐시 (Bitmap)
```kotlin
val imageCache = retain { mutableMapOf<String, Bitmap>() }
```

### 3. Flow/StateFlow 관리
```kotlin
val networkMonitor = retain { NetworkMonitor() }
val state by networkMonitor.state.collectAsState()
```

### 4. Lambda/Callback 보존
```kotlin
val analyticsCallback = retain {
    AnalyticsCallback { event ->
        analytics.track(event)
    }
}
```

### 5. 제3자 라이브러리 객체
```kotlin
val paymentProcessor = retain { PaymentProcessor.create() }
val adManager = retain { AdManager.getInstance(applicationContext) }
```

---

## 주의사항

### 1. 짧은 생명주기 객체 참조 금지

```kotlin
// Activity, Fragment, ViewModel, Context, Lifecycle 참조 금지!
val player = retain {
    // applicationContext 사용 (O)
    ExoPlayer.Builder(applicationContext).build()
}

// 일반 context 사용 (X) - Activity Context는 메모리 누수 위험!
val player = retain {
    ExoPlayer.Builder(context).build()  // 위험!
}
```

### 2. @DoNotRetain 어노테이션

특정 타입이 retain에 사용되는 것을 방지할 수 있습니다:

```kotlin
@DoNotRetain
class DangerousObject {
    // 이 클래스는 retain에서 사용할 수 없음
}
```

### 3. remember/rememberSaveable와 혼용 금지

```kotlin
// 같은 객체에 remember와 retain 혼용 금지!
val obj = retain { MyObject() }
val wrapped = remember { obj }  // 위험!
```

### 4. Process Death에서는 유지 안 됨

```kotlin
// retain은 Process Death에서 유지되지 않음
// 중요한 사용자 입력은 rememberSaveable 사용
var userName by rememberSaveable { mutableStateOf("") }  // Process Death 대응
val cache = retain { mutableMapOf<String, Any>() }  // 캐시는 잃어도 됨
```

### 5. 의존성 추가 필요

```kotlin
// build.gradle.kts
dependencies {
    implementation("androidx.compose.runtime:runtime-retain")
    // 또는 Compose BOM 2025.12.00 이상 사용
}
```

---

## RetainObserver로 정리 작업

retain된 객체가 retire(정리)될 때 콜백을 받을 수 있습니다:

```kotlin
class MediaPlayerManager(
    context: Context
) : RetainObserver {
    val player = ExoPlayer.Builder(context).build()

    override fun onRetire() {
        // retain이 정리될 때 호출됨
        player.release()
    }
}

@Composable
fun MediaScreen() {
    val context = LocalContext.current.applicationContext
    val manager = retain { MediaPlayerManager(context) }
    // manager.player 사용...
}
```

---

## 연습 문제

### 연습 1: 이미지 캐시 유지 (쉬움)

`remember`로 구현된 이미지 캐시를 `retain`으로 변경하여 화면 회전에도 캐시가 유지되도록 하세요.

### 연습 2: StateFlow 구독 관리 (중간)

네트워크 상태를 모니터링하는 `StateFlow`를 `retain`으로 관리하고, `collectAsState`로 구독하세요.

### 연습 3: 복합 매니저 객체 (어려움)

분석 트래커, 캐시 매니저, 로거가 통합된 `AppManager`를 `retain`으로 관리하고, `RetainObserver`를 구현하여 정리 작업을 추가하세요.

---

## 다음 학습

- **ViewModel**: retain보다 더 긴 생명주기가 필요할 때
- **SavedStateHandle**: ViewModel에서 Process Death 대응
- **Media3 + Compose**: 실제 미디어 플레이어 구현

---

## 참고 자료

- [State lifespans in Compose - Android Developers](https://developer.android.com/develop/ui/compose/state-lifespans)
- [State and Jetpack Compose - Android Developers](https://developer.android.com/develop/ui/compose/state)
- [What's new in Jetpack Compose December '25](https://android-developers.googleblog.com/2025/12/whats-new-in-jetpack-compose-december.html)

---

*Last reviewed: 2025-12-28 | Compose BOM 2025.12.00+ required*
