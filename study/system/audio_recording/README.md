# Audio Recording (오디오 녹음) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `permission_handling` | 런타임 권한 요청과 처리 방법 | [📚 학습하기](../../system/permission_handling/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Android에서 오디오 녹음을 구현하려면 `MediaRecorder` 클래스를 사용합니다. Jetpack Compose와 통합할 때는 다음 요소들을 고려해야 합니다:

1. **RECORD_AUDIO 권한 처리**: 런타임 권한 요청 필요
2. **MediaRecorder 생명주기 관리**: DisposableEffect를 사용한 리소스 해제
3. **상태 관리**: 녹음 상태(대기/녹음중/일시정지/완료)를 Compose 상태로 관리
4. **타이머**: LaunchedEffect를 사용한 녹음 시간 표시
5. **시각화**: 진폭(Amplitude) 데이터를 활용한 오디오 파형 표시

## 핵심 특징

### 1. MediaRecorder 생명주기

MediaRecorder는 엄격한 상태 머신을 따릅니다:

```
Initial → Initialized → DataSourceConfigured → Prepared → Recording → Released
                                                    ↓
                                                 Paused (API 24+)
```

### 2. RECORD_AUDIO 권한

- **Dangerous Permission**: 런타임 권한 요청 필수
- **Manifest 선언**: `<uses-permission android:name="android.permission.RECORD_AUDIO" />`
- **Compose 처리**: `rememberLauncherForActivityResult`와 `ActivityResultContracts.RequestPermission` 사용

### 3. MediaRecorder 주요 메서드

```kotlin
// 설정
recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
recorder.setOutputFile(filePath)

// 생명주기
recorder.prepare()  // 녹음 준비
recorder.start()    // 녹음 시작
recorder.pause()    // 일시정지 (API 24+)
recorder.resume()   // 재개
recorder.stop()     // 녹음 중지
recorder.release()  // 리소스 해제 (필수!)
```

### 4. 진폭(Amplitude) 조회

```kotlin
val amplitude = recorder.maxAmplitude  // 0 ~ 32767
val normalized = amplitude / 32767f    // 0.0 ~ 1.0
```

## 문제 상황: MediaRecorder 미관리

### 잘못된 코드 예시

```kotlin
@Composable
fun BadRecorderScreen() {
    var isRecording by remember { mutableStateOf(false) }

    // 문제 1: MediaRecorder가 Composable 내부에서 직접 생성
    val recorder = MediaRecorder()  // 매 recomposition마다 새로 생성!

    Button(onClick = {
        // 문제 2: 권한 확인 없이 녹음 시작
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)  // SecurityException!
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder.setOutputFile(filePath)
        recorder.prepare()
        recorder.start()
    }) { Text("Record") }

    // 문제 3: 화면 이탈 시 recorder.release() 호출 없음
    // → 마이크 리소스 점유 지속, 메모리 누수, 다른 앱 마이크 사용 불가
}
```

### 발생하는 문제점

1. **SecurityException**: 권한 없이 마이크 접근 시 앱 크래시
2. **리소스 누수**: `release()` 미호출로 마이크 점유 지속
3. **메모리 누수**: MediaRecorder 인스턴스 미해제
4. **상태 불일치**: 화면 회전 시 녹음 상태 손실

## 해결책: DisposableEffect + 권한 처리

### 올바른 코드

```kotlin
@Composable
fun GoodRecorderScreen() {
    val context = LocalContext.current
    var recordingState by remember { mutableStateOf<RecordingState>(RecordingState.Idle) }

    // 권한 처리
    var hasPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    // MediaRecorder 인스턴스 관리
    val recorder = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    // DisposableEffect로 리소스 해제 보장
    DisposableEffect(Unit) {
        onDispose {
            try {
                if (recordingState is RecordingState.Recording) {
                    recorder.stop()
                }
            } catch (e: Exception) { /* 무시 */ }
            recorder.release()
        }
    }

    if (!hasPermission) {
        // 권한 요청 UI
        PermissionRequestScreen(onRequestPermission = {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        })
    } else {
        // 녹음 UI
        RecorderScreen(recorder, recordingState, onStateChange = { recordingState = it })
    }
}
```

### 해결되는 이유

1. **권한 확인**: `rememberLauncherForActivityResult`로 안전하게 권한 요청
2. **리소스 해제 보장**: `DisposableEffect.onDispose`에서 `release()` 호출
3. **상태 관리**: Sealed class로 녹음 상태 명확히 관리
4. **생명주기 인식**: Composable 생명주기와 MediaRecorder 동기화

## 녹음 상태 관리 패턴

### Sealed Class 활용

```kotlin
sealed class RecordingState {
    object Idle : RecordingState()
    data class Recording(val startTime: Long) : RecordingState()
    data class Paused(val elapsedTime: Long) : RecordingState()
    data class Completed(val filePath: String, val duration: Long) : RecordingState()
    data class Error(val message: String) : RecordingState()
}
```

### 상태 전이 규칙

```
Idle ──(start)──> Recording
Recording ──(pause)──> Paused    (API 24+)
Recording ──(stop)──> Completed
Paused ──(resume)──> Recording
Paused ──(stop)──> Completed
Any ──(error)──> Error
```

## 사용 시나리오

1. **음성 메모 앱**: 간단한 녹음/재생 기능
2. **음성 메시지**: 채팅 앱에서 음성 메시지 녹음
3. **팟캐스트 녹음**: 긴 시간 녹음 + 일시정지 기능
4. **음성 일기**: 매일 음성 녹음 + 저장

## 주의사항

1. **API 레벨 처리**:
   - MediaRecorder(Context) 생성자는 API 31+ 필요
   - `pause()`/`resume()`는 API 24+ 필요

2. **파일 저장 위치**:
   - 내부 저장소: `context.filesDir` (앱 전용)
   - 캐시: `context.cacheDir` (임시 파일)
   - 외부 저장소: WRITE_EXTERNAL_STORAGE 권한 필요 (deprecated in API 30)

3. **출력 포맷**:
   - MPEG_4 (.m4a): 가장 호환성 좋음
   - THREE_GPP (.3gp): 작은 파일 크기
   - AAC_ADTS (.aac): 오디오 전용

4. **IllegalStateException 방지**:
   - MediaRecorder 상태 전이 규칙 준수 필수
   - try-catch로 예외 처리

## 연습 문제

### 연습 1: 기본 녹음 시작/중지
MediaRecorder를 사용하여 기본적인 녹음 시작/중지 기능을 구현하세요.

### 연습 2: 녹음 시간 타이머
LaunchedEffect를 사용하여 녹음 경과 시간을 MM:SS 형식으로 표시하세요.

### 연습 3: 녹음 파일 목록 + 재생
저장된 녹음 파일 목록을 표시하고 MediaPlayer로 재생하는 기능을 구현하세요.

## 다음 학습

- **권한 처리 심화**: [permission_handling](../../system/permission_handling/README.md)
- **DisposableEffect 기초**: [disposable_effect](../../effect/disposable_effect/README.md)
- **LaunchedEffect 기초**: [launched_effect](../../effect/launched_effect/README.md)
