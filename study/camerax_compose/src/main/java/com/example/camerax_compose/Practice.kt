package com.example.camerax_compose

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.concurrent.Executor

/**
 * Practice: CameraX + Compose 연습 문제
 *
 * 3가지 연습 문제를 통해 CameraX 사용법을 익힙니다.
 */

// ============================================================
// 연습 문제 1: 기본 카메라 프리뷰 구현
// ============================================================

/**
 * 연습 문제 1: 기본 카메라 프리뷰 구현
 *
 * TODO:
 * 1. ProcessCameraProvider.awaitInstance()로 카메라 프로바이더 획득
 * 2. Preview.Builder().build()로 프리뷰 UseCase 생성
 * 3. AndroidView로 PreviewView 생성
 * 4. preview.surfaceProvider에 PreviewView의 surfaceProvider 연결
 * 5. cameraProvider.bindToLifecycle()로 생명주기 바인딩
 *
 * 힌트:
 * - LaunchedEffect 내에서 카메라 초기화
 * - CameraSelector.DEFAULT_BACK_CAMERA 사용
 * - Preview를 remember로 생성
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Practice1_BasicPreview() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        PracticeHintCard(
            title = "연습 1: 기본 카메라 프리뷰",
            hints = listOf(
                "ProcessCameraProvider.awaitInstance(context) 사용",
                "Preview.Builder().build()로 UseCase 생성",
                "AndroidView { PreviewView(it) }로 뷰 생성",
                "preview.surfaceProvider = previewView.surfaceProvider",
                "bindToLifecycle(lifecycleOwner, cameraSelector, preview)"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cameraPermissionState.status.isGranted) {
            // TODO: 여기에 카메라 프리뷰를 구현하세요
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "여기에 카메라 프리뷰를 구현하세요",
                    color = Color.White
                )
            }

            /*
            // 정답:
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current

            val preview = remember { Preview.Builder().build() }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            LaunchedEffect(Unit) {
                val cameraProvider = ProcessCameraProvider.awaitInstance(context)
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            }

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
            */
        } else {
            // 권한 요청 UI
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("카메라 권한이 필요합니다")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("권한 요청")
                }
            }
        }
    }
}

// ============================================================
// 연습 문제 2: 사진 촬영 후 미리보기 표시
// ============================================================

/**
 * 연습 문제 2: 사진 촬영 후 미리보기 표시
 *
 * TODO:
 * 1. ImageCapture.Builder().build()로 UseCase 생성
 * 2. bindToLifecycle에 imageCapture 추가
 * 3. 촬영 버튼 추가
 * 4. imageCapture.takePicture() 호출
 * 5. 촬영된 이미지 URI를 State로 관리
 * 6. AsyncImage로 촬영 결과 표시
 *
 * 힌트:
 * - ImageCapture.OutputFileOptions.Builder(file).build()
 * - OnImageSavedCallback으로 결과 처리
 * - capturedUri 상태 변수로 이미지 URI 관리
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Practice2_PhotoCapture() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        PracticeHintCard(
            title = "연습 2: 사진 촬영 후 미리보기",
            hints = listOf(
                "ImageCapture.Builder().build() 사용",
                "bindToLifecycle에 preview와 imageCapture 모두 전달",
                "ImageCapture.OutputFileOptions로 저장 위치 지정",
                "takePicture(options, executor, callback) 호출",
                "AsyncImage(model = uri)로 결과 표시"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cameraPermissionState.status.isGranted) {
            // TODO: 여기에 사진 촬영 기능을 구현하세요
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "여기에 카메라 프리뷰와\n촬영 버튼을 구현하세요",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* TODO: 촬영 */ }) {
                        Text("촬영")
                    }
                }
            }

            /*
            // 정답:
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current
            val executor = remember { ContextCompat.getMainExecutor(context) }

            val preview = remember { Preview.Builder().build() }
            val imageCapture = remember {
                ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()
            }

            var capturedUri by remember { mutableStateOf<Uri?>(null) }
            var showPreview by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                val cameraProvider = ProcessCameraProvider.awaitInstance(context)
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (showPreview) {
                    // 카메라 프리뷰
                    AndroidView(
                        factory = { ctx ->
                            PreviewView(ctx).apply {
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                                preview.surfaceProvider = surfaceProvider
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // 촬영 버튼
                    Button(
                        onClick = {
                            val photoFile = File(
                                context.cacheDir,
                                "photo_${System.currentTimeMillis()}.jpg"
                            )
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            imageCapture.takePicture(
                                outputOptions,
                                executor,
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                        capturedUri = Uri.fromFile(photoFile)
                                        showPreview = false
                                    }
                                    override fun onError(exception: ImageCaptureException) {
                                        Log.e("Practice", "촬영 실패", exception)
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(32.dp)
                    ) {
                        Text("촬영")
                    }
                } else {
                    // 촬영된 이미지 표시
                    capturedUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "촬영된 사진",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Button(
                        onClick = { showPreview = true },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(32.dp)
                    ) {
                        Text("다시 촬영")
                    }
                }
            }
            */
        } else {
            // 권한 요청 UI
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("카메라 권한이 필요합니다")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("권한 요청")
                }
            }
        }
    }
}

// ============================================================
// 연습 문제 3: 전면/후면 카메라 전환
// ============================================================

/**
 * 연습 문제 3: 전면/후면 카메라 전환
 *
 * TODO:
 * 1. isFrontCamera 상태 변수 추가 (Boolean)
 * 2. cameraSelector를 isFrontCamera에 따라 동적으로 생성
 * 3. 카메라 전환 버튼 추가
 * 4. 버튼 클릭 시 isFrontCamera 토글
 * 5. LaunchedEffect의 key에 cameraSelector 추가
 * 6. unbindAll() 후 새 cameraSelector로 재바인딩
 *
 * 힌트:
 * - CameraSelector.DEFAULT_FRONT_CAMERA
 * - CameraSelector.DEFAULT_BACK_CAMERA
 * - LaunchedEffect(cameraSelector)로 변경 감지
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Practice3_CameraSwitch() {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        PracticeHintCard(
            title = "연습 3: 카메라 전환",
            hints = listOf(
                "var isFrontCamera by remember { mutableStateOf(false) }",
                "CameraSelector.DEFAULT_FRONT_CAMERA / DEFAULT_BACK_CAMERA",
                "remember(isFrontCamera)로 cameraSelector 생성",
                "LaunchedEffect(cameraSelector)로 변경 감지",
                "cameraProvider.unbindAll() 후 재바인딩"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cameraPermissionState.status.isGranted) {
            // TODO: 여기에 카메라 전환 기능을 구현하세요
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "여기에 카메라 프리뷰와\n전환 버튼을 구현하세요",
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* TODO: 전환 */ }) {
                        Icon(Icons.Default.Cameraswitch, "전환")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("카메라 전환")
                    }
                }
            }

            /*
            // 정답:
            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current

            var isFrontCamera by remember { mutableStateOf(false) }
            val preview = remember { Preview.Builder().build() }

            val cameraSelector = remember(isFrontCamera) {
                if (isFrontCamera) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
            }

            LaunchedEffect(cameraSelector) {
                val cameraProvider = ProcessCameraProvider.awaitInstance(context)
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                // 카메라 프리뷰
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            preview.surfaceProvider = surfaceProvider
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // 현재 카메라 표시
                Text(
                    text = if (isFrontCamera) "전면 카메라" else "후면 카메라",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // 카메라 전환 버튼
                Button(
                    onClick = { isFrontCamera = !isFrontCamera },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(32.dp)
                ) {
                    Icon(Icons.Default.Cameraswitch, "전환")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isFrontCamera) "후면으로 전환" else "전면으로 전환")
                }
            }
            */
        } else {
            // 권한 요청 UI
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("카메라 권한이 필요합니다")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("권한 요청")
                }
            }
        }
    }
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
private fun PracticeHintCard(
    title: String,
    hints: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            hints.forEachIndexed { index, hint ->
                Text(
                    "${index + 1}. $hint",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
