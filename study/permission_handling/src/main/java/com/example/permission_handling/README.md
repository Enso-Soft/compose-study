# Permission Handling 학습

Android Jetpack Compose에서 런타임 권한을 올바르게 처리하는 방법을 학습합니다.

## 개념

**런타임 권한(Runtime Permission)**은 Android 6.0 (API 23)부터 도입된 개념으로, 앱이 민감한 데이터나 시스템 리소스에 접근하려면 사용자의 명시적인 동의가 필요합니다.

Compose에서 권한을 처리하는 두 가지 방법:
1. **Activity Result API** (`rememberLauncherForActivityResult`)
2. **Accompanist Permissions** 라이브러리 (권장)

---

## 핵심 특징

### 1. rememberLauncherForActivityResult (기본 API)

```kotlin
val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { isGranted: Boolean ->
    if (isGranted) {
        // 권한 승인됨
    } else {
        // 권한 거부됨
    }
}

// 권한 요청
Button(onClick = {
    permissionLauncher.launch(Manifest.permission.CAMERA)
})
```

### 2. Accompanist Permissions (권장)

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    when {
        cameraPermissionState.status.isGranted -> {
            // 권한 승인됨 -> 카메라 사용 가능
            CameraContent()
        }
        cameraPermissionState.status.shouldShowRationale -> {
            // 권한 거부됨, 설명 가능 -> 재요청 버튼
            RationaleContent(
                onRequestAgain = { cameraPermissionState.launchPermissionRequest() }
            )
        }
        else -> {
            // 첫 요청 또는 영구 거부 -> 설정 화면 안내
            PermissionRequestContent(
                onRequest = { cameraPermissionState.launchPermissionRequest() },
                onOpenSettings = { openAppSettings(context) }
            )
        }
    }
}
```

### 3. 다중 권한 처리

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VideoRecordingScreen() {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    if (multiplePermissionsState.allPermissionsGranted) {
        // 모든 권한 승인됨
        VideoRecordingContent()
    } else {
        // 거부된 권한 표시
        val deniedPermissions = multiplePermissionsState.revokedPermissions
            .map { it.permission.substringAfterLast(".") }
            .joinToString(", ")

        Text("필요한 권한: $deniedPermissions")

        Button(onClick = {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }) {
            Text("전체 권한 요청")
        }
    }
}
```

---

## 문제 상황: 권한 상태를 무시하는 코드

### 잘못된 코드 예시

```kotlin
// 문제 1: 결과만 단순 저장
val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    status = if (isGranted) "승인" else "거부"
    // shouldShowRationale 확인 없음!
    // 설정 화면 안내 없음!
}

// 문제 2: 현재 상태 확인 없이 요청
Button(onClick = {
    launcher.launch(permission)  // 이미 승인된 권한도 다시 요청?
})

// 문제 3: 영구 거부 처리 없음
if (!isGranted) {
    Text("권한이 필요합니다")  // 사용자가 할 수 있는 일이 없음
}
```

### 발생하는 문제점

1. **사용자 혼란**: 권한 상태가 UI에 반영되지 않아 현재 상태를 알 수 없음
2. **불필요한 요청**: 이미 승인된 권한을 다시 요청
3. **설명 부재**: `shouldShowRationale`을 무시하여 권한이 왜 필요한지 설명 안 함
4. **막다른 길**: 영구 거부 시 설정 화면 안내가 없어 사용자가 권한을 부여할 방법이 없음

---

## 해결책: Accompanist Permissions 사용

### 올바른 코드

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraFeature() {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    // 상태별 UI 분기
    when {
        cameraPermissionState.status.isGranted -> {
            // 1. 권한 승인됨 -> 기능 사용 가능
            CameraContent()
        }
        cameraPermissionState.status.shouldShowRationale -> {
            // 2. 거부됨 + 설명 가능 -> 왜 필요한지 설명 후 재요청
            Column {
                Text("카메라 권한이 필요합니다.")
                Text("사진 촬영 기능을 사용하려면 권한을 허용해주세요.")
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("권한 다시 요청")
                }
            }
        }
        else -> {
            // 3. 첫 요청 또는 영구 거부 -> 설정 화면 안내
            Column {
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("권한 요청")
                }
                OutlinedButton(onClick = { openAppSettings(context) }) {
                    Text("설정에서 권한 허용")
                }
            }
        }
    }
}

// 앱 설정 화면으로 이동
fun openAppSettings(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(this)
    }
}
```

### 해결되는 이유

1. **상태 기반 UI**: `status.isGranted`, `status.shouldShowRationale`로 명확한 상태 분기
2. **자동 상태 관리**: Accompanist가 권한 상태를 자동으로 추적하고 UI 업데이트
3. **설명 제공**: `shouldShowRationale`이 true일 때 사용자에게 권한 필요 이유 설명
4. **설정 안내**: 영구 거부 상태에서 설정 화면으로 이동할 수 있는 옵션 제공

---

## 사용 시나리오

### 1. 카메라 앱
```kotlin
// 사진 촬영 전 카메라 권한 확인
val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
```

### 2. 영상 통화 앱
```kotlin
// 카메라 + 마이크 권한 동시 확인
val permissions = rememberMultiplePermissionsState(
    listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
)
```

### 3. 위치 기반 서비스
```kotlin
// 정확한 위치 권한 확인
val locationPermissionState = rememberPermissionState(
    Manifest.permission.ACCESS_FINE_LOCATION
)
```

### 4. 푸시 알림 (Android 13+)
```kotlin
// Android 13 이상에서만 알림 권한 필요
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )
}
```

---

## Android 14/15 권한 변경사항

### Android 14 (API 34)
- **targetSdk 23 미만 앱 설치 불가**: 런타임 권한 모델 회피 방지
- **Foreground Service Type 필수**: 포그라운드 서비스에 타입 선언 필수
- **BLUETOOTH_CONNECT 강제 적용**: Bluetooth 관련 API 호출 시 권한 필수
- **SCHEDULE_EXACT_ALARM 기본 거부**: 정확한 알람 권한 자동 부여 중단

### Android 15 (API 35)
- **content:// URI 접근 강화**: 다른 앱의 콘텐츠 접근 시 더 엄격한 권한 검사
- **사이드로드 앱 제한**: 접근성, 기기 관리자 등 민감한 권한 제한
- **백그라운드 Activity 제한**: 특정 조건 충족 없이 백그라운드에서 Activity 시작 불가
- **exported 속성 필수**: Intent Filter가 있는 컴포넌트에 exported 속성 명시적 선언 필수

---

## 주의사항

### 1. Manifest 권한 선언 필수
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

### 2. 권한 요청은 비동기
- 권한 요청은 비동기적으로 처리됨
- 결과는 콜백 또는 상태 변경으로 받음
- `LaunchedEffect` 내부에서 직접 요청하지 말 것

### 3. shouldShowRationale의 동작
- 첫 요청 시: `false`
- 첫 거부 후: `true`
- 두 번째 거부 후 (영구 거부): `false`
- **첫 요청과 영구 거부를 구분할 수 없음** -> 둘 다 설정 버튼 제공 권장

### 4. ExperimentalPermissionsApi 어노테이션
```kotlin
@OptIn(ExperimentalPermissionsApi::class)
```
Accompanist Permissions는 아직 실험적 API이므로 `@OptIn` 필요

### 5. Accompanist 버전 호환성
Compose 버전과 Accompanist 버전 호환성 확인 필요
```kotlin
implementation("com.google.accompanist:accompanist-permissions:0.36.0")
```

---

## 연습 문제

### 연습 1: 위치 권한 요청 (초급)
`rememberPermissionState`를 사용하여 위치 권한 요청 구현

### 연습 2: 알림 권한 + 설정 안내 (중급)
Android 버전 체크 + 영구 거부 시 설정 화면 이동

### 연습 3: 다중 권한 처리 (고급)
`rememberMultiplePermissionsState`로 3개 권한 동시 처리 + 상태 리스트 표시

---

## 다음 학습

이 모듈을 완료했다면 다음 주제를 학습하세요:
- **LaunchedEffect**: 권한 승인 후 비동기 작업 실행
- **LifecycleStartEffect**: 앱이 포그라운드로 돌아올 때 권한 상태 재확인

---

## 참고 자료

- [Accompanist Permissions 공식 문서](https://google.github.io/accompanist/permissions/)
- [Request runtime permissions - Android Developers](https://developer.android.com/training/permissions/requesting)
- [Android 14 Behavior Changes](https://developer.android.com/about/versions/14/behavior-changes-14)
- [Android 15 Permission Changes](https://www.creolestudios.com/android-15-app-permission-changes/)
