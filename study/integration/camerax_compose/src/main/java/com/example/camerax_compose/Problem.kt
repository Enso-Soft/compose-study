package com.example.camerax_compose

import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Problem: CameraX 없이 직접 카메라/SurfaceView 관리
 *
 * 이 화면에서는 CameraX를 사용하지 않고 직접 카메라와 Surface를 관리할 때
 * 발생하는 문제점들을 보여줍니다.
 *
 * 주요 문제점:
 * 1. SurfaceHolder.Callback 수동 구현 필요
 * 2. 생명주기에 따른 카메라 open/release 수동 관리
 * 3. 권한 처리 boilerplate 코드
 * 4. 디바이스별 호환성 이슈
 * 5. Preview 크기 계산 복잡성
 */

// ============================================================
// 문제 상황 1: SurfaceView 직접 관리의 복잡성
// ============================================================

/**
 * 전통적인 방식의 SurfaceHolder.Callback 구현
 *
 * 문제점:
 * - 3개의 콜백 메서드를 모두 구현해야 함
 * - Surface 상태 변화 시점을 정확히 파악해야 함
 * - 카메라 초기화/해제 타이밍이 복잡함
 */
private class ProblematicSurfaceCallback : SurfaceHolder.Callback {
    // Surface가 생성될 때 - 이 시점에 카메라를 열어야 함
    override fun surfaceCreated(holder: SurfaceHolder) {
        // 문제: 여기서 카메라를 열어도, Activity가 pause 상태일 수 있음
        // try {
        //     camera = Camera.open()  // deprecated API
        //     camera.setPreviewDisplay(holder)
        //     camera.startPreview()
        // } catch (e: Exception) {
        //     // 다른 앱이 카메라를 사용 중이면 실패
        // }
    }

    // Surface 크기가 변경될 때
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // 문제: Preview 크기와 Surface 크기를 맞춰야 함
        // 화면 회전 시 복잡한 계산 필요
        // val supportedSizes = camera.parameters.supportedPreviewSizes
        // val optimalSize = getOptimalPreviewSize(supportedSizes, width, height)
    }

    // Surface가 파괴될 때
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // 문제: 여기서 카메라를 release해도
        // Activity의 onPause보다 늦게 호출될 수 있음
        // camera?.release()
    }
}

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        ProblemDescriptionCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 1: SurfaceView 직접 관리
        Problem1_SurfaceViewManagement()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 2: 생명주기 미연동
        Problem2_LifecycleLeak()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 3: 권한 처리 복잡성
        Problem3_PermissionBoilerplate()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 4: 디바이스 호환성
        Problem4_DeviceCompatibility()
    }
}

@Composable
private fun ProblemDescriptionCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "CameraX 없이 카메라 관리의 문제점",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                """
                직접 Camera API와 SurfaceView를 관리하면:

                1. SurfaceHolder.Callback 수동 구현 필요
                2. 생명주기에 따른 리소스 관리 복잡
                3. 권한 처리 boilerplate 코드 증가
                4. 디바이스별 호환성 문제
                5. Preview 크기 계산 어려움

                아래에서 각 문제를 자세히 살펴봅니다.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// ============================================================
// 문제 1: SurfaceView 직접 관리
// ============================================================

@Composable
private fun Problem1_SurfaceViewManagement() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "문제 1: SurfaceView 직접 관리",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 실제로 동작하지 않는 예시 - 시각화만 함
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "[SurfaceView 영역]\n카메라 미연결",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 문제가 되는 코드 패턴 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "잘못된 패턴:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        """
                        // SurfaceHolder.Callback 직접 구현
                        surfaceHolder.addCallback(object : Callback {
                            override fun surfaceCreated(holder: SurfaceHolder) {
                                camera = Camera.open() // 언제 호출될지 불확실
                            }
                            override fun surfaceDestroyed(holder: SurfaceHolder) {
                                camera?.release() // 타이밍 문제 발생 가능
                            }
                        })
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

// ============================================================
// 문제 2: 생명주기 미연동으로 인한 리소스 누수
// ============================================================

@Composable
private fun Problem2_LifecycleLeak() {
    var leakWarningCount by remember { mutableIntStateOf(0) }

    // 이 SideEffect는 Recomposition마다 실행되어 문제를 시각화
    SideEffect {
        // 실제로는 카메라를 열지 않음 - 시뮬레이션만
        leakWarningCount++
    }

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "문제 2: 생명주기 미연동",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Recomposition 횟수: ${leakWarningCount}회",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 문제 시각화
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "위험한 패턴:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        """
                        @Composable
                        fun BadCameraScreen() {
                            // 생명주기 무시 - 메모리 누수!
                            LaunchedEffect(Unit) {
                                val camera = Camera.open()
                                // onDispose 없음 - 해제 안 됨!
                            }
                        }
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        """
                        발생하는 문제:
                        - 화면 전환 시 카메라 해제 안 됨
                        - 다른 앱이 카메라 사용 불가
                        - 배터리 과소모
                        - ANR(Application Not Responding) 발생 가능
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// ============================================================
// 문제 3: 권한 처리 복잡성
// ============================================================

@Composable
private fun Problem3_PermissionBoilerplate() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "문제 3: 권한 처리 Boilerplate",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "전통적인 권한 처리 (Activity 기반):",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        """
                        // Activity에서
                        private val requestPermissionLauncher =
                            registerForActivityResult(
                                ActivityResultContracts.RequestPermission()
                            ) { isGranted ->
                                if (isGranted) {
                                    startCamera()
                                } else {
                                    showPermissionDeniedDialog()
                                }
                            }

                        override fun onCreate(...) {
                            if (ContextCompat.checkSelfPermission(
                                this, Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED) {
                                startCamera()
                            } else if (shouldShowRequestPermissionRationale(...)) {
                                showRationaleDialog()
                            } else {
                                requestPermissionLauncher.launch(...)
                            }
                        }
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                """
                문제점:
                - 많은 boilerplate 코드
                - Activity/Fragment에 종속
                - 상태 관리 복잡
                - Compose의 선언적 패러다임과 맞지 않음
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

// ============================================================
// 문제 4: 디바이스 호환성
// ============================================================

@Composable
private fun Problem4_DeviceCompatibility() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "문제 4: 디바이스 호환성",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Camera1 vs Camera2 API:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        """
                        // Camera1 (deprecated)
                        val camera = Camera.open()

                        // Camera2 (복잡함)
                        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                            override fun onOpened(camera: CameraDevice) {
                                // CaptureSession 생성...
                                // CaptureRequest 생성...
                                // Surface 설정...
                            }
                            override fun onDisconnected(camera: CameraDevice) { }
                            override fun onError(camera: CameraDevice, error: Int) { }
                        }, handler)
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                """
                호환성 문제:
                - Camera1: deprecated, 기능 제한적
                - Camera2: 복잡한 API, 디바이스별 동작 차이
                - 프리뷰 크기/비율 계산 필요
                - 전면/후면 카메라 전환 로직 수동 구현
                - 플래시, 줌 등 기능별 지원 여부 확인 필요
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
