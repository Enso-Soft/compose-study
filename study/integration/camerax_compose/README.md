# CameraX + Compose 통합 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `interoperability` | View-Compose 상호운용성 (AndroidView, ComposeView) | [📚 학습하기](../../integration/interoperability/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**CameraX**는 Android Jetpack 라이브러리로, 카메라 앱 개발을 크게 단순화합니다. 기존 Camera2 API의 복잡성을 추상화하고, 생명주기 인식(Lifecycle-aware) 기능을 제공하여 안정적인 카메라 앱 개발이 가능합니다.

Jetpack Compose에서 CameraX를 사용하는 방법은 두 가지가 있습니다:

| 방식 | 라이브러리 | 권장 여부 |
|------|-----------|----------|
| **CameraXViewfinder** (권장) | `camera-compose` 1.5+ | 2025년~ 권장 |
| AndroidView + PreviewView | `camera-view` | 레거시/호환성 |

---

## 권장 방식: CameraXViewfinder (2025년~)

CameraX 1.5부터 **Compose-native 카메라 API**가 안정화되었습니다. `CameraXViewfinder` Composable을 사용하면 `AndroidView` 래핑 없이 순수 Compose로 카메라 프리뷰를 구현할 수 있습니다.

### 의존성 추가

```kotlin
// build.gradle.kts
val cameraxVersion = "1.5.0"
implementation("androidx.camera:camera-core:$cameraxVersion")
implementation("androidx.camera:camera-camera2:$cameraxVersion")
implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
implementation("androidx.camera:camera-compose:$cameraxVersion")  // Compose 통합!
```

### CameraXViewfinder 사용법

```kotlin
@Composable
fun ModernCameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // SurfaceRequest를 State로 관리
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }

    val preview = remember {
        Preview.Builder().build().apply {
            setSurfaceProvider { request ->
                surfaceRequest = request
            }
        }
    }

    // 카메라 바인딩
    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview
        )
    }

    // Compose-native 카메라 뷰파인더
    surfaceRequest?.let { request ->
        CameraXViewfinder(
            surfaceRequest = request,
            modifier = Modifier.fillMaxSize()
        )
    }
}
```

### CameraXViewfinder의 장점

1. **순수 Compose**: `AndroidView` 래핑 불필요
2. **자동 좌표 변환**: 탭-투-포커스가 자동으로 작동
3. **Surface 생명주기 관리**: Compose와 자연스럽게 통합
4. **회전/스케일링 처리**: 내부적으로 자동 처리

---

## 레거시 방식: AndroidView + PreviewView

기존 프로젝트나 CameraX 1.4 이하 버전에서는 `PreviewView`를 `AndroidView`로 래핑하여 사용합니다. 이 방식은 여전히 동작하며, 많은 기존 코드베이스에서 사용됩니다.

```kotlin
// 레거시 방식 (CameraX 1.4 이하)
@Composable
fun LegacyCameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { previewView ->
            // CameraX 바인딩
        }
    )
}
```

> 이 모듈의 Solution.kt는 레거시 방식을 사용하여 구현되어 있습니다.
> 새 프로젝트에서는 위의 CameraXViewfinder 방식을 권장합니다.

## 핵심 특징

### 1. UseCase 기반 아키텍처
CameraX는 4가지 주요 UseCase를 제공합니다:
- **Preview**: 카메라 프리뷰 표시
- **ImageCapture**: 사진 촬영
- **VideoCapture**: 비디오 녹화
- **ImageAnalysis**: 실시간 이미지 분석 (ML 등)

### 2. 생명주기 자동 관리
```kotlin
cameraProvider.bindToLifecycle(
    lifecycleOwner,  // Activity/Fragment의 Lifecycle
    cameraSelector,
    preview,
    imageCapture
)
// onStart/onStop에서 자동으로 카메라 시작/중지
```

### 3. 디바이스 호환성
- 다양한 Android 기기에서 일관된 동작 보장
- 하드웨어 차이를 자동으로 처리
- minSdk 21 지원

### 4. Accompanist Permissions
런타임 권한을 Compose 친화적으로 처리:
```kotlin
val cameraPermissionState = rememberPermissionState(
    android.Manifest.permission.CAMERA
)

if (cameraPermissionState.status.isGranted) {
    CameraPreview()
} else {
    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
        Text("카메라 권한 요청")
    }
}
```

---

## 문제 상황: CameraX 없이 직접 카메라 관리

### 잘못된 코드 예시

```kotlin
// 문제 1: SurfaceView 직접 관리
class OldCameraActivity : Activity() {
    private var camera: Camera? = null
    private lateinit var surfaceHolder: SurfaceHolder

    override fun onResume() {
        super.onResume()
        // 매번 카메라 열기 시도 - 예외 처리 복잡
        try {
            camera = Camera.open()
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        } catch (e: Exception) {
            // 다른 앱이 카메라 사용 중이면 실패
        }
    }

    override fun onPause() {
        super.onPause()
        // 해제를 잊으면 다른 앱이 카메라 사용 불가!
        camera?.stopPreview()
        camera?.release()
        camera = null
    }
}

// 문제 2: Compose에서 수동 Surface 관리
@Composable
fun BadCameraScreen() {
    val context = LocalContext.current

    // 생명주기 연동 없음 - 메모리 누수 위험
    LaunchedEffect(Unit) {
        val camera = Camera.open()
        // onDispose 없이 카메라 열기만 함
    }

    // Surface 콜백 직접 관리 필요
    AndroidView(
        factory = { SurfaceView(it) }
    )
}
```

### 발생하는 문제점

1. **생명주기 미연동**: onPause에서 카메라 해제를 잊으면 리소스 누수
2. **디바이스 호환성**: Camera1 vs Camera2 API 차이 처리 필요
3. **복잡한 예외 처리**: 카메라 접근 실패 시 다양한 예외 상황
4. **Preview 크기 계산**: 화면 비율과 카메라 비율 맞추기 어려움
5. **권한 처리 boilerplate**: ActivityResultContracts 직접 관리

---

## 해결책: CameraX + Compose 통합

### 올바른 코드

```kotlin
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {
        CameraContent()
    } else {
        PermissionRequest(cameraPermissionState)
    }
}

@Composable
fun CameraContent() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = remember { Preview.Builder().build() }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var isFrontCamera by remember { mutableStateOf(false) }

    val cameraSelector = if (isFrontCamera) {
        CameraSelector.DEFAULT_FRONT_CAMERA
    } else {
        CameraSelector.DEFAULT_BACK_CAMERA
    }

    // 생명주기에 자동으로 바인딩
    LaunchedEffect(cameraSelector) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    preview.surfaceProvider = surfaceProvider
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 카메라 전환 버튼
        IconButton(
            onClick = { isFrontCamera = !isFrontCamera },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Cameraswitch, "전환")
        }
    }
}
```

### 해결되는 이유

1. **ProcessCameraProvider**: 생명주기에 자동으로 바인딩되어 리소스 관리 자동화
2. **UseCase 추상화**: Preview, ImageCapture 등 간단한 API
3. **디바이스 호환성**: CameraX가 내부적으로 처리
4. **Accompanist Permissions**: 선언적 권한 처리
5. **AndroidView 통합**: Compose와 자연스럽게 통합

---

## 사용 시나리오

### 1. 사진 촬영

```kotlin
val imageCapture = remember {
    ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
        .build()
}

fun takePhoto(context: Context, onImageSaved: (Uri) -> Unit) {
    val photoFile = File(
        context.cacheDir,
        "photo_${System.currentTimeMillis()}.jpg"
    )
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onImageSaved(Uri.fromFile(photoFile))
            }
            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Photo capture failed", exception)
            }
        }
    )
}
```

### 2. 카메라 전환

```kotlin
var isFrontCamera by remember { mutableStateOf(false) }

val cameraSelector = if (isFrontCamera) {
    CameraSelector.DEFAULT_FRONT_CAMERA
} else {
    CameraSelector.DEFAULT_BACK_CAMERA
}

// cameraSelector가 변경되면 LaunchedEffect가 재실행
LaunchedEffect(cameraSelector) {
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(...)
}

Button(onClick = { isFrontCamera = !isFrontCamera }) {
    Text("카메라 전환")
}
```

### 3. 플래시 제어

```kotlin
val imageCapture = remember {
    ImageCapture.Builder().build()
}

var flashMode by remember { mutableStateOf(ImageCapture.FLASH_MODE_OFF) }

// 플래시 모드 변경
imageCapture.flashMode = flashMode

// 토글 버튼
IconButton(onClick = {
    flashMode = when (flashMode) {
        ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
        ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
        else -> ImageCapture.FLASH_MODE_OFF
    }
}) {
    Icon(
        when (flashMode) {
            ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
            ImageCapture.FLASH_MODE_AUTO -> Icons.Default.FlashAuto
            else -> Icons.Default.FlashOff
        },
        contentDescription = "플래시"
    )
}
```

### 4. 줌 제어

```kotlin
var camera: Camera? by remember { mutableStateOf(null) }

// 카메라 바인딩 시 Camera 객체 저장
LaunchedEffect(cameraSelector) {
    camera = cameraProvider.bindToLifecycle(...)
}

// 줌 슬라이더
var zoomRatio by remember { mutableFloatStateOf(1f) }
val zoomState = camera?.cameraInfo?.zoomState?.observeAsState()

Slider(
    value = zoomRatio,
    onValueChange = { ratio ->
        zoomRatio = ratio
        camera?.cameraControl?.setZoomRatio(ratio)
    },
    valueRange = 1f..(zoomState?.value?.maxZoomRatio ?: 4f)
)
```

### 5. Pinch-to-Zoom

```kotlin
Box(
    modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, _, zoom, _ ->
                val currentZoom = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
                val newZoom = (currentZoom * zoom).coerceIn(1f, 10f)
                camera?.cameraControl?.setZoomRatio(newZoom)
            }
        }
) {
    // CameraPreview
}
```

---

## 주의사항

### 1. 권한 선언
AndroidManifest.xml에 반드시 선언:
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" android:required="true" />
```

### 2. Accompanist Permissions는 Experimental
```kotlin
@OptIn(ExperimentalPermissionsApi::class)
```

### 3. 동시 UseCase 제한
- Preview + ImageCapture + VideoCapture: LIMITED 디바이스 이상
- ImageAnalysis 추가 시: LEVEL_3 디바이스 필요

### 4. PreviewView 구현 모드
```kotlin
// COMPATIBLE: 모든 디바이스 지원, 성능 다소 낮음
// PERFORMANCE: SurfaceView 사용, 성능 좋지만 z-order 문제 가능
previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
```

### 5. 프로세스 종료 시 카메라 상태
- CameraX가 자동으로 정리
- 별도 저장 로직 불필요

---

## 연습 문제

### 연습 1: 기본 카메라 프리뷰 구현
`ProcessCameraProvider`를 사용하여 카메라 프리뷰를 표시하세요.
- 힌트: `ProcessCameraProvider.awaitInstance(context)`
- 힌트: `Preview.Builder().build()`

### 연습 2: 사진 촬영 후 미리보기 표시
`ImageCapture` UseCase로 사진을 촬영하고 결과를 화면에 표시하세요.
- 힌트: `ImageCapture.OutputFileOptions`
- 힌트: `takePicture()` 콜백

### 연습 3: 전면/후면 카메라 전환
버튼을 눌러 전면/후면 카메라를 전환하세요.
- 힌트: `CameraSelector.DEFAULT_FRONT_CAMERA`
- 힌트: 상태 변경 시 `unbindAll()` 후 재바인딩

---

## 다음 학습

- **VideoCapture**: 비디오 녹화
- **ImageAnalysis**: ML Kit 연동

---

## 참고 자료

- [CameraX 공식 문서](https://developer.android.com/media/camera/camerax)
- [CameraX + Compose Codelab](https://developer.android.com/codelabs/camerax-getting-started)
- [Accompanist Permissions](https://google.github.io/accompanist/permissions/)
- [CameraX 1.5 릴리스 노트](https://developer.android.com/jetpack/androidx/releases/camera)
- [Compose-Native CameraX 가이드 (2025)](https://proandroiddev.com/goodbye-androidview-camerax-goes-full-compose-4d21ca234c4e)
