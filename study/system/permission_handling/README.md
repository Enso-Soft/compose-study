# Permission Handling 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `launched_effect` | LaunchedEffect를 통한 Side Effect 처리 | [📚 학습하기](../../effect/launched_effect/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

Android Jetpack Compose에서 런타임 권한을 올바르게 처리하는 방법을 학습합니다.

## 개념

**런타임 권한(Runtime Permission)**은 Android 6.0 (API 23)부터 도입된 개념으로, 앱이 민감한 데이터나 시스템 리소스에 접근하려면 사용자의 명시적인 동의가 필요합니다.

Compose에서 권한 처리는 특별한 도전 과제입니다:
- **선언적 UI**: Compose는 상태에 따라 UI를 선언적으로 표현
- **명령형 권한 API**: 권한 요청은 시스템에 "요청"을 보내는 명령형 작업
- **상태 동기화**: 권한 상태 변화를 UI에 즉시 반영해야 함

이 모듈에서는 Accompanist Permissions 라이브러리를 사용하여 Compose의 선언적 패러다임에 맞게 권한을 처리하는 방법을 학습합니다.

## 핵심 특징

### 1. 상태 기반 권한 관리
권한 상태를 Compose State로 관리하여 UI와 자동 동기화

### 2. 선언적 API
`rememberPermissionState`, `rememberMultiplePermissionsState`로 선언적 권한 처리

### 3. 자동 UI 업데이트
권한 상태 변경 시 Recomposition을 통한 자동 UI 갱신

---

## 문제 상황: 권한 처리를 잘못하면?

### 시나리오
카메라 앱을 만들고 있습니다. 사용자가 "사진 촬영" 버튼을 누르면 카메라 권한을 요청해야 합니다.

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

// 문제 2: 현재 상태 확인 없이 바로 요청
Button(onClick = {
    launcher.launch(permission)  // 이미 승인된 권한도 다시 요청?
})

// 문제 3: 영구 거부 처리 없음
if (!isGranted) {
    Text("권한이 필요합니다")  // 사용자가 할 수 있는 일이 없음
}
```

### 발생하는 문제점

| 문제 | 설명 |
|------|------|
| **사용자 혼란** | 권한 상태가 UI에 반영되지 않아 현재 상태를 알 수 없음 |
| **불필요한 요청** | 이미 승인된 권한을 다시 요청하여 사용자 경험 저하 |
| **설명 부재** | `shouldShowRationale`을 무시하여 권한이 왜 필요한지 설명 안 함 |
| **막다른 길** | 영구 거부 시 설정 화면 안내가 없어 사용자가 권한을 부여할 방법이 없음 |

---

## 해결책: Accompanist Permissions 사용

### 왜 Accompanist인가?

| 방식 | 설명 | 장점 | 단점 |
|------|------|------|------|
| **Activity Result API** | `rememberLauncherForActivityResult` 사용 | 별도 의존성 없음, 공식 API | 상태 관리 수동, shouldShowRationale 별도 확인 필요 |
| **Accompanist Permissions** | Google 제공 Compose 확장 라이브러리 | 상태 자동 관리, 선언적 API, shouldShowRationale 내장 | 외부 의존성 필요, @ExperimentalPermissionsApi |

> **Accompanist Permissions 상태 (2025년 기준)**
> - Google에서 제공하는 공식 라이브러리이지만, Jetpack Compose에 아직 통합되지 않음
> - `@ExperimentalPermissionsApi` 어노테이션이 필요하며, API가 변경될 수 있음
> - 그러나 production 환경에서 안전하게 사용 가능하며, 현재 권한 처리의 de facto 표준
> - 공식 Compose API가 출시되면 마이그레이션 가이드가 제공될 예정

### Step 1: 의존성 추가

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.google.accompanist:accompanist-permissions:0.37.0") // 2025년 최신
}
```

> 최신 버전은 [Maven Repository](https://mvnrepository.com/artifact/com.google.accompanist/accompanist-permissions)에서 확인하세요.

### Step 2: 단일 권한 요청

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraFeature() {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { isGranted ->
            // 권한 결과 콜백 (로깅, 분석 등에 활용)
            if (isGranted) {
                Log.d("Permissions", "Camera permission granted")
            }
        }
    )

    // 상태에 따른 UI 표시
    when {
        cameraPermissionState.status.isGranted -> {
            Text("카메라를 사용할 수 있습니다!")
        }
        else -> {
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("카메라 권한 요청")
            }
        }
    }
}
```

### Step 3: 상태별 UI 분기

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraFeatureComplete() {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

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
```

### Step 4: 설정 화면으로 이동

```kotlin
fun openAppSettings(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(this)
    }
}
```

### Step 5: 다중 권한 처리

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VideoRecordingScreen() {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ),
        onPermissionsResult = { permissionResults ->
            permissionResults.forEach { (permission, isGranted) ->
                Log.d("Permissions", "$permission: $isGranted")
            }
        }
    )

    if (multiplePermissionsState.allPermissionsGranted) {
        // 모든 권한 승인됨
        VideoRecordingContent()
    } else {
        // 거부된 권한 표시
        val deniedPermissions = multiplePermissionsState.revokedPermissions
            .map { it.permission.substringAfterLast(".") }
            .joinToString(", ")

        Column {
            Text("필요한 권한: $deniedPermissions")

            if (multiplePermissionsState.shouldShowRationale) {
                Text("영상 녹화를 위해 카메라와 마이크 권한이 필요합니다.")
            }

            Button(onClick = {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }) {
                Text("전체 권한 요청")
            }
        }
    }
}
```

---

## 심화: 영구 거부 감지 패턴

시스템 API만으로는 **"첫 요청"**과 **"영구 거부"**를 구분할 수 없습니다.
둘 다 `!isGranted && !shouldShowRationale` 상태이기 때문입니다.

### SharedPreferences를 활용한 우회 패턴

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmartPermissionRequest(permission: String) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("permissions", Context.MODE_PRIVATE)
    }

    val permissionState = rememberPermissionState(permission)
    val hasAskedBefore = prefs.getBoolean("asked_$permission", false)

    // 권한 요청 시 기록
    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            prefs.edit().putBoolean("asked_$permission", true).apply()
        }
    }

    when {
        permissionState.status.isGranted -> {
            // 권한 승인됨
            GrantedContent()
        }
        permissionState.status.shouldShowRationale -> {
            // 거부됨, 설명 가능 -> 재요청 버튼
            RationaleContent(onRequest = { permissionState.launchPermissionRequest() })
        }
        hasAskedBefore -> {
            // 이전에 요청했고, shouldShowRationale이 false -> 영구 거부
            // 설정 화면으로 안내
            PermanentlyDeniedContent(onOpenSettings = { openAppSettings(context) })
        }
        else -> {
            // 첫 요청 -> 권한 요청 버튼
            InitialRequestContent(onRequest = { permissionState.launchPermissionRequest() })
        }
    }
}
```

### 핵심 로직

```
                        hasAskedBefore?
                              │
                    ┌─────────┴─────────┐
                    │                   │
                   No                  Yes
                    │                   │
              첫 번째 요청        이전에 요청함
                    │                   │
                    ▼                   ▼
            권한 요청 버튼      shouldShowRationale?
                                        │
                              ┌─────────┴─────────┐
                              │                   │
                            True                False
                              │                   │
                         설명 후 재요청       영구 거부
                                                  │
                                                  ▼
                                          설정 화면 안내
```

---

## 사용 시나리오

### 1. 카메라 앱
```kotlin
val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
```

### 2. 영상 통화 앱
```kotlin
val permissions = rememberMultiplePermissionsState(
    listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
)
```

### 3. 위치 기반 서비스
```kotlin
val locationPermissionState = rememberPermissionState(
    Manifest.permission.ACCESS_FINE_LOCATION
)
```

### 4. 푸시 알림 (Android 13+)
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )
}
```

---

## Android 14/15 권한 변경사항 참고

### Android 14 (API 34)
- **targetSdk 23 미만 앱 설치 불가**: 런타임 권한 모델 회피 방지
- **Foreground Service Type 필수**: 포그라운드 서비스에 타입 선언 필수
- **BLUETOOTH_CONNECT 강제 적용**: Bluetooth 관련 API 호출 시 권한 필수

### Android 15 (API 35)
- **content:// URI 접근 강화**: 다른 앱의 콘텐츠 접근 시 더 엄격한 권한 검사
- **사이드로드 앱 제한**: 민감한 권한(접근성, 기기 관리자 등) 제한 강화
- **백그라운드 Activity 제한**: 특정 조건 충족 없이 백그라운드에서 Activity 시작 불가
- **최소 targetSdk 24 필수**: targetSdkVersion 24 미만 앱 설치 불가

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
- `LaunchedEffect` 내부에서 직접 요청하지 말 것 (Side Effect로 권한 요청은 지양)

### 3. shouldShowRationale의 동작

| 상태 | shouldShowRationale |
|------|---------------------|
| 첫 요청 | `false` |
| 첫 거부 후 | `true` |
| 두 번째 거부 후 (영구 거부) | `false` |

> **주의**: 첫 요청과 영구 거부를 구분할 수 없음 -> 둘 다 설정 버튼 제공 권장

### 4. 적절한 타이밍에 권한 요청
- 앱 시작 시 바로 요청하지 말 것
- 기능 사용 직전에 요청하여 왜 필요한지 명확히
- 예: 카메라 버튼 클릭 시 카메라 권한 요청

### 5. ExperimentalPermissionsApi 어노테이션
```kotlin
@OptIn(ExperimentalPermissionsApi::class)
```
Accompanist Permissions는 아직 실험적 API이므로 `@OptIn` 필요

---

## 연습 문제

### 연습 1: 위치 권한 요청 (초급)
`rememberPermissionState`를 사용하여 위치 권한 요청 구현
- `ACCESS_FINE_LOCATION` 권한 요청
- 권한 상태에 따른 UI 분기

### 연습 2: 알림 권한 + 설정 안내 (중급)
Android 버전 체크 + 영구 거부 시 설정 화면 이동
- `Build.VERSION.SDK_INT >= TIRAMISU` 체크
- `POST_NOTIFICATIONS` 권한 처리
- 설정 화면 이동 버튼 구현

### 연습 3: 다중 권한 처리 (고급)
`rememberMultiplePermissionsState`로 3개 권한 동시 처리
- Camera, Microphone, Location 권한
- 각 권한별 상태 리스트 표시
- 거부된 권한만 필터링

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
- [Android 15 Behavior Changes](https://developer.android.com/about/versions/15/behavior-changes-15)
- [Accompanist GitHub Repository](https://github.com/google/accompanist)
- [Maven Repository - accompanist-permissions](https://mvnrepository.com/artifact/com.google.accompanist/accompanist-permissions)
