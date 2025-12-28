package com.example.camerax_compose

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.util.concurrent.Executor

/**
 * Solution: CameraX + Compose 통합
 *
 * CameraX를 사용하면:
 * 1. 생명주기 자동 관리
 * 2. 간단한 UseCase 기반 API
 * 3. 디바이스 호환성 자동 처리
 * 4. Accompanist로 선언적 권한 처리
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SolutionScreen() {
    // 카메라 권한 상태 관리
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 핵심 포인트 카드
        SolutionDescriptionCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 권한 상태에 따른 UI 분기
        // Box + weight(1f)로 Column 내에서 정확한 크기 할당
        when {
            cameraPermissionState.status.isGranted -> {
                // 권한이 있으면 카메라 화면 표시
                Box(modifier = Modifier.weight(1f)) {
                    CameraContent()
                }
            }
            cameraPermissionState.status.shouldShowRationale -> {
                // 권한 거부 후 재요청 시 설명 표시
                Box(modifier = Modifier.weight(1f)) {
                    PermissionRationale(
                        onRequestPermission = {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    )
                }
            }
            else -> {
                // 최초 권한 요청
                Box(modifier = Modifier.weight(1f)) {
                    PermissionRequest(
                        onRequestPermission = {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SolutionDescriptionCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "CameraX + Compose 해결책",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                """
                CameraX 사용의 장점:

                1. ProcessCameraProvider로 생명주기 자동 관리
                2. UseCase (Preview, ImageCapture) 간단한 API
                3. AndroidView로 PreviewView 통합
                4. Accompanist로 선언적 권한 처리
                5. 디바이스 호환성 자동 처리
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun PermissionRequest(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CameraAlt,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "카메라 권한이 필요합니다",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "이 기능을 사용하려면 카메라 접근 권한을 허용해주세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("권한 요청")
        }
    }
}

@Composable
private fun PermissionRationale(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "카메라 권한이 필요한 이유",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "사진 촬영과 카메라 프리뷰를 표시하기 위해\n" +
                    "카메라 접근 권한이 필요합니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("다시 요청")
        }
    }
}

// ============================================================
// 메인 카메라 컨텐츠
// ============================================================

@Composable
private fun CameraContent() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { ContextCompat.getMainExecutor(context) }

    // 상태 관리
    var isFrontCamera by remember { mutableStateOf(false) }
    var flashMode by remember { mutableIntStateOf(ImageCapture.FLASH_MODE_OFF) }
    var zoomRatio by remember { mutableFloatStateOf(1f) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showCapturedImage by remember { mutableStateOf(false) }

    // CameraX UseCase
    val preview = remember { Preview.Builder().build() }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    // Camera 객체 (줌 제어용)
    var camera by remember { mutableStateOf<Camera?>(null) }

    // 카메라 셀렉터
    val cameraSelector = remember(isFrontCamera) {
        if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    // 플래시 모드 적용
    LaunchedEffect(flashMode) {
        imageCapture.flashMode = flashMode
    }

    // 카메라 바인딩
    LaunchedEffect(cameraSelector) {
        try {
            val cameraProvider = ProcessCameraProvider.awaitInstance(context)
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            // 줌 초기화
            zoomRatio = 1f
        } catch (e: Exception) {
            Log.e("CameraX", "카메라 바인딩 실패", e)
        }
    }

    // 줌 적용
    LaunchedEffect(zoomRatio) {
        camera?.cameraControl?.setZoomRatio(zoomRatio)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 촬영된 이미지 표시 모드
        if (showCapturedImage && capturedImageUri != null) {
            CapturedImageView(
                imageUri = capturedImageUri!!,
                onDismiss = { showCapturedImage = false }
            )
        } else {
            // 카메라 프리뷰
            CameraPreviewWithControls(
                preview = preview,
                camera = camera,
                isFrontCamera = isFrontCamera,
                flashMode = flashMode,
                zoomRatio = zoomRatio,
                onToggleCamera = { isFrontCamera = !isFrontCamera },
                onToggleFlash = {
                    flashMode = when (flashMode) {
                        ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
                        ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
                        else -> ImageCapture.FLASH_MODE_OFF
                    }
                },
                onZoomChange = { zoomRatio = it },
                onCapture = {
                    takePhoto(
                        context = context,
                        imageCapture = imageCapture,
                        executor = executor,
                        onImageSaved = { uri ->
                            capturedImageUri = uri
                            showCapturedImage = true
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun CameraPreviewWithControls(
    preview: Preview,
    camera: Camera?,
    isFrontCamera: Boolean,
    flashMode: Int,
    zoomRatio: Float,
    onToggleCamera: () -> Unit,
    onToggleFlash: () -> Unit,
    onZoomChange: (Float) -> Unit,
    onCapture: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(camera) {
                // Pinch-to-zoom
                detectTransformGestures { _, _, zoom, _ ->
                    val currentZoom = zoomRatio
                    val maxZoom = camera?.cameraInfo?.zoomState?.value?.maxZoomRatio ?: 4f
                    val newZoom = (currentZoom * zoom).coerceIn(1f, maxZoom)
                    onZoomChange(newZoom)
                }
            }
    ) {
        // 카메라 프리뷰
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    preview.surfaceProvider = surfaceProvider
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 상단 컨트롤 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 플래시 버튼 (후면 카메라에서만 표시)
            if (!isFrontCamera) {
                IconButton(
                    onClick = onToggleFlash,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        when (flashMode) {
                            ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
                            ImageCapture.FLASH_MODE_AUTO -> Icons.Default.FlashAuto
                            else -> Icons.Default.FlashOff
                        },
                        contentDescription = "플래시",
                        tint = Color.White
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }

            // 카메라 전환 버튼
            IconButton(
                onClick = onToggleCamera,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Cameraswitch,
                    contentDescription = "카메라 전환",
                    tint = Color.White
                )
            }
        }

        // 줌 표시
        if (zoomRatio > 1.01f) {
            Text(
                text = "%.1fx".format(zoomRatio),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // 하단 촬영 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            // 촬영 버튼
            IconButton(
                onClick = onCapture,
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = "촬영",
                    tint = Color.Black,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // 줌 슬라이더
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            val maxZoom = camera?.cameraInfo?.zoomState?.value?.maxZoomRatio ?: 4f

            Slider(
                value = zoomRatio,
                onValueChange = onZoomChange,
                valueRange = 1f..maxZoom,
                modifier = Modifier
                    .height(200.dp)
                    .width(48.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun CapturedImageView(
    imageUri: Uri,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 촬영된 이미지
        AsyncImage(
            model = imageUri,
            contentDescription = "촬영된 사진",
            modifier = Modifier.fillMaxSize()
        )

        // 닫기 버튼
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "닫기",
                tint = Color.White
            )
        }

        // 안내 텍스트
        Text(
            text = "촬영 완료! 뒤로가기를 눌러 카메라로 돌아가세요.",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

// ============================================================
// 유틸리티 함수
// ============================================================

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    executor: Executor,
    onImageSaved: (Uri) -> Unit
) {
    val photoFile = File(
        context.cacheDir,
        "photo_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                Log.d("CameraX", "사진 저장 완료: $savedUri")
                onImageSaved(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "사진 촬영 실패", exception)
            }
        }
    )
}
