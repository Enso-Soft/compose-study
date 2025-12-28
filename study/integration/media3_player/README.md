# Media3 ExoPlayer + Compose 통합 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 |
|----------|------|
| `remember` | Composable 내에서 객체를 Recomposition에도 유지 |
| `DisposableEffect` | Composable이 Composition을 떠날 때 정리 작업 수행 |
| `LaunchedEffect` | Composable 내에서 suspend 함수 실행 |
| `AndroidView` | Compose에서 기존 Android View 사용 |

## 개념

**Media3**는 Android의 미디어 재생을 위한 최신 Jetpack 라이브러리입니다. **ExoPlayer**를 포함하며, Compose와 통합하는 방법은 두 가지가 있습니다:

1. **AndroidView + PlayerView** (전통적 방식): 기존 View 기반 PlayerView를 Compose에 임베드
2. **media3-ui-compose** (최신 방식, 1.6.0+): 네이티브 Compose UI 컴포넌트 사용

이 모듈에서는 **AndroidView 방식**을 중심으로 학습하며, 최신 Compose UI도 소개합니다.

```kotlin
// Media3 ExoPlayer 기본 구조
val player = remember {
    ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(videoUrl))
        prepare()
    }
}

AndroidView(
    factory = { ctx ->
        PlayerView(ctx).apply { this.player = player }
    }
)
```

## 핵심 특징

| 구성요소 | 역할 | 설명 |
|---------|------|------|
| **ExoPlayer.Builder** | 플레이어 생성 | Context를 받아 ExoPlayer 인스턴스 생성 |
| **MediaItem** | 미디어 소스 | 로컬 파일, 원격 URL, HLS/DASH 스트림 지원 |
| **PlayerView** | UI 표시 | 비디오 렌더링 + 기본 재생 컨트롤 제공 |
| **AndroidView** | Compose 브릿지 | View 기반 컴포넌트를 Compose에 임베드 |
| **DisposableEffect** | 생명주기 관리 | Composition 종료 시 player.release() 호출 |

---

## 문제 상황: 플레이어 생명주기 미관리

### 잘못된 코드 예시

```kotlin
@Composable
fun BadVideoPlayer(url: String) {
    val context = LocalContext.current

    // 문제 1: Recomposition마다 새 플레이어 생성
    val player = ExoPlayer.Builder(context).build()

    // 문제 2: release() 호출 안함 - 메모리 누수
    player.setMediaItem(MediaItem.fromUri(url))
    player.prepare()

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply { this.player = player }
        }
    )
}
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| 메모리 누수 | 플레이어가 release되지 않아 리소스 계속 점유 |
| 플레이어 누적 | Recomposition마다 새 인스턴스 생성, 이전 것은 정리 안됨 |
| 다중 오디오 재생 | 여러 플레이어 인스턴스가 동시에 재생 |
| 백그라운드 재생 지속 | 화면 벗어나도 플레이어가 계속 재생 |
| OOM 크래시 | 결국 메모리 부족으로 앱 종료 |

---

## 해결책: remember + DisposableEffect

### 올바른 코드

```kotlin
@Composable
fun GoodVideoPlayer(url: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // remember로 플레이어 인스턴스 유지
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
    }

    // DisposableEffect로 생명주기 관리
    DisposableEffect(lifecycleOwner, player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_RESUME -> player.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply { this.player = player }
        }
    )
}
```

### 해결되는 이유

| 해결 | 설명 |
|------|------|
| 인스턴스 재사용 | remember로 Recomposition에도 플레이어 유지 |
| 자동 정리 | DisposableEffect의 onDispose에서 release() 호출 |
| 생명주기 연동 | 앱 백그라운드 시 pause, 포그라운드 시 resume |
| 리소스 효율 | 하나의 플레이어 인스턴스만 존재 |

---

## 사용 시나리오

### 1. 기본 비디오 재생
```kotlin
val player = remember {
    ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(videoUrl))
        prepare()
    }
}
```

### 2. 플레이리스트 설정
```kotlin
val mediaItems = listOf(
    MediaItem.fromUri("https://example.com/video1.mp4"),
    MediaItem.fromUri("https://example.com/video2.mp4"),
    MediaItem.fromUri("https://example.com/video3.mp4")
)
player.setMediaItems(mediaItems)
```

### 3. 재생 컨트롤
```kotlin
player.play()           // 재생
player.pause()          // 일시정지
player.seekTo(10000)    // 10초 위치로 이동
player.seekToNextMediaItem()      // 다음 트랙
player.seekToPreviousMediaItem()  // 이전 트랙
```

### 4. 재생 상태 감지
```kotlin
player.addListener(object : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        // 재생 상태 변경 시 호출
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> { /* 초기화 전 */ }
            Player.STATE_BUFFERING -> { /* 버퍼링 중 */ }
            Player.STATE_READY -> { /* 재생 준비 완료 */ }
            Player.STATE_ENDED -> { /* 재생 종료 */ }
        }
    }
})
```

---

## Media3 의존성

```kotlin
// build.gradle.kts
dependencies {
    // 핵심 의존성 (2025년 12월 기준 최신: 1.9.0)
    implementation("androidx.media3:media3-exoplayer:1.9.0")
    implementation("androidx.media3:media3-ui:1.9.0")
    implementation("androidx.media3:media3-common:1.9.0")

    // 선택: 네이티브 Compose UI (1.6.0+)
    implementation("androidx.media3:media3-ui-compose:1.9.0")
}
```

## AndroidManifest.xml 권한

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 주의사항

### 1. Context 선택
```kotlin
// Application Context 사용 권장 (Activity 누수 방지)
// Activity Context를 사용하면 플레이어가 Activity 참조를 유지하여
// Activity가 종료되어도 GC되지 않는 메모리 누수 발생
val context = LocalContext.current.applicationContext
```

### 2. 화면 회전 처리
```kotlin
// Activity configChanges 설정으로 재생성 방지
// 설정 안하면 화면 회전 시 Activity 재생성 → 플레이어도 재생성
android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"
```

### 3. URL 변경 시 처리
```kotlin
// videoUrl이 변경되면 새 미디어 로드
LaunchedEffect(videoUrl) {
    player.setMediaItem(MediaItem.fromUri(videoUrl))
    player.prepare()
}
```

### 4. 재생 위치 복원
```kotlin
// rememberSaveable로 Configuration Change에도 위치 유지
var playbackPosition by rememberSaveable { mutableLongStateOf(0L) }

DisposableEffect(player) {
    onDispose {
        playbackPosition = player.currentPosition
    }
}

LaunchedEffect(player) {
    player.seekTo(playbackPosition)
}
```

### 5. Wake Lock (Media3 1.9.0+)
```kotlin
// 1.9.0부터 wake lock이 기본 활성화 (opt-out)
// 백그라운드 재생 시 기기가 슬립 모드로 전환되지 않음
// 비활성화하려면:
player.setWakeMode(C.WAKE_MODE_NONE)
```

---

## 학습 파일

| 파일 | 설명 |
|------|------|
| `Problem.kt` | 잘못된 코드 - 생명주기 미관리로 메모리 누수 |
| `Solution.kt` | 올바른 코드 - remember + DisposableEffect 사용 |
| `Practice.kt` | 연습 문제 3개 (기본 플레이어, 커스텀 컨트롤, 플레이리스트) |

---

## 연습 문제

1. **기본 비디오 플레이어**: ExoPlayer 생성 + AndroidView + 생명주기 관리
2. **커스텀 재생 컨트롤**: Play/Pause 버튼, Seek 버튼을 Compose로 구현
3. **플레이리스트**: 여러 비디오 + 다음/이전 트랙 기능

---

## 최신 Compose UI: media3-ui-compose (1.6.0+)

Media3 1.6.0 (2025년 3월)부터 네이티브 Compose UI 모듈이 추가되었습니다. AndroidView 없이 순수 Compose로 비디오 재생이 가능합니다.

### PlayerSurface

```kotlin
// AndroidView 대신 PlayerSurface 사용
PlayerSurface(
    player = player,
    surfaceType = SurfaceType.SURFACE_VIEW  // 또는 TEXTURE_VIEW
)
```

### ContentFrame (1.8.0+)

```kotlin
// aspect-ratio 자동 처리 + shutter overlay 포함
ContentFrame(
    player = player,
    modifier = Modifier.fillMaxWidth()
)
```

### 상태 홀더

Media3는 Compose용 상태 홀더를 제공합니다:

| 상태 홀더 | 용도 |
|----------|------|
| `rememberPresentationState` | 비디오 표시 상태 관리 |
| `rememberPlayPauseButtonState` | 재생/일시정지 버튼 상태 |
| `rememberSeekBackButtonState` | 뒤로 이동 버튼 상태 |
| `rememberSeekForwardButtonState` | 앞으로 이동 버튼 상태 |
| `rememberProgressStateWithTickInterval` | 재생 위치/시간 표시 (1.9.0+) |

---

## Material3 Compose UI: media3-ui-compose-material3 (1.9.0+)

Media3 1.9.0 (2025년 12월)에서 **Material3 테마 Compose UI 모듈**이 새로 추가되었습니다. 기존 media3-ui-compose는 UI를 직접 구현해야 했지만, 이 모듈은 **Material3 스타일의 미리 만들어진 버튼**을 제공합니다.

### 의존성 추가

```kotlin
// build.gradle.kts
implementation("androidx.media3:media3-ui-compose-material3:1.9.0")
```

### 제공되는 Material3 Composable

| Composable | 설명 |
|------------|------|
| `PlayPauseButton` | 재생/일시정지 토글 버튼 |
| `NextButton` | 다음 트랙 버튼 |
| `PreviousButton` | 이전 트랙 버튼 |
| `SeekBackButton` | 뒤로 이동 버튼 |
| `SeekForwardButton` | 앞으로 이동 버튼 |
| `RepeatButton` | 반복 모드 버튼 |
| `ShuffleButton` | 셔플 버튼 |
| `MuteButton` | 음소거 버튼 |
| `TimeText` | 재생 시간 텍스트 (현재 위치/남은 시간/전체 시간) |

### 사용 예시

```kotlin
@Composable
fun Material3PlayerControls(player: Player) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이전 트랙
        PreviousButton(player = player)

        // 10초 뒤로
        SeekBackButton(player = player)

        // 재생/일시정지
        PlayPauseButton(player = player)

        // 10초 앞으로
        SeekForwardButton(player = player)

        // 다음 트랙
        NextButton(player = player)
    }

    // 재생 시간 표시
    TimeText(
        player = player,
        showDuration = true  // "01:23 / 05:00" 형식
    )
}
```

### AndroidView vs media3-ui-compose vs material3

| 기준 | AndroidView + PlayerView | media3-ui-compose | media3-ui-compose-material3 |
|------|--------------------------|-------------------|------------------------------|
| 최소 버전 | Media3 모든 버전 | 1.6.0+ | 1.9.0+ |
| 컨트롤 UI | 기본 제공 | 직접 구현 필요 | Material3 버튼 제공 |
| 커스터마이징 | 제한적 | 완전한 자유도 | Material3 테마 기반 |
| 성능 | View 브릿징 오버헤드 | 네이티브 Compose | 네이티브 Compose |
| 권장 상황 | 빠른 구현, 레거시 | 완전 커스텀 UI | Material3 앱, 표준 UI |

---

## 다음 학습

- `Picture-in-Picture`: PiP 모드 구현
- `Background Playback`: MediaSession + Foreground Service
- `Offline Playback`: 다운로드 및 오프라인 재생
