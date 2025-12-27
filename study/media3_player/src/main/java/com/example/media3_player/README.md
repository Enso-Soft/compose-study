# Media3 ExoPlayer + Compose 통합 학습

## 개념

**Media3**는 Android의 미디어 재생을 위한 최신 Jetpack 라이브러리입니다. **ExoPlayer**를 포함하며, Compose와 통합할 때 `AndroidView`를 사용하여 `PlayerView`를 임베드합니다.

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

1. **ExoPlayer.Builder**: 플레이어 인스턴스 생성
2. **MediaItem**: 미디어 소스 설정 (로컬/원격 URL)
3. **PlayerView**: 비디오 UI 표시 (재생 컨트롤 포함)
4. **AndroidView**: Compose에서 View 기반 컴포넌트 사용
5. **DisposableEffect**: 플레이어 생명주기 관리 (release)

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
    implementation("androidx.media3:media3-exoplayer:1.5.1")
    implementation("androidx.media3:media3-ui:1.5.1")
    implementation("androidx.media3:media3-common:1.5.1")
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
val context = LocalContext.current.applicationContext
```

### 2. 화면 회전 처리
```kotlin
// Activity configChanges 설정으로 재생성 방지
android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"
```

### 3. URL 변경 시 처리
```kotlin
LaunchedEffect(videoUrl) {
    player.setMediaItem(MediaItem.fromUri(videoUrl))
    player.prepare()
}
```

### 4. 재생 위치 복원
```kotlin
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

## 다음 학습

- `media3-ui-compose`: Media3 1.6.0+의 네이티브 Compose UI (PlayerSurface)
- `Picture-in-Picture`: PiP 모드 구현
- `Background Playback`: MediaSession + Foreground Service
