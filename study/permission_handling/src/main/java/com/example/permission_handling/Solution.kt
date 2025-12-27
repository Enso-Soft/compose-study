package com.example.permission_handling

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * 올바른 권한 처리 - Accompanist Permissions 사용
 *
 * 핵심 포인트:
 * 1. rememberPermissionState로 단일 권한 상태 관리
 * 2. rememberMultiplePermissionsState로 다중 권한 상태 관리
 * 3. status.isGranted, status.shouldShowRationale로 상태 확인
 * 4. 영구 거부 시 설정 화면 안내
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Solution: 올바른 권한 처리",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("단일 권한") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("다중 권한") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("알림 권한") }
            )
        }

        when (selectedDemo) {
            0 -> SinglePermissionDemo()
            1 -> MultiplePermissionsDemo()
            2 -> NotificationPermissionDemo()
        }
    }
}

/**
 * 단일 권한 요청 데모 - 카메라 권한
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SinglePermissionDemo() {
    val context = LocalContext.current

    // Accompanist Permissions: 단일 권한 상태 관리
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "카메라 권한 (단일)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 권한 상태 표시
            PermissionStatusIndicator(
                permissionName = "CAMERA",
                isGranted = cameraPermissionState.status.isGranted,
                shouldShowRationale = cameraPermissionState.status.shouldShowRationale
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 상태별 UI 분기
            when {
                cameraPermissionState.status.isGranted -> {
                    // 권한 승인됨
                    GrantedContent("카메라를 사용할 준비가 되었습니다!")
                }
                cameraPermissionState.status.shouldShowRationale -> {
                    // 권한 거부됨, 설명 가능
                    RationaleContent(
                        message = "카메라 권한이 필요합니다.\n사진 촬영 기능을 사용하려면 권한을 허용해주세요.",
                        onRequestAgain = { cameraPermissionState.launchPermissionRequest() }
                    )
                }
                else -> {
                    // 첫 요청 또는 영구 거부
                    InitialOrDeniedContent(
                        onRequest = { cameraPermissionState.launchPermissionRequest() },
                        onOpenSettings = { openAppSettings(context) }
                    )
                }
            }
        }
    }

    // 핵심 코드 설명
    CodeExplanationCard(
        title = "단일 권한 요청 코드",
        code = """
val cameraPermissionState = rememberPermissionState(
    permission = Manifest.permission.CAMERA
)

when {
    cameraPermissionState.status.isGranted -> {
        // 권한 승인됨 -> 기능 사용 가능
    }
    cameraPermissionState.status.shouldShowRationale -> {
        // 거부됨 + 설명 가능 -> 다시 요청 버튼
        Button(onClick = {
            cameraPermissionState.launchPermissionRequest()
        })
    }
    else -> {
        // 첫 요청 또는 영구 거부 -> 설정 안내
    }
}
        """.trimIndent()
    )
}

/**
 * 다중 권한 요청 데모 - 카메라 + 마이크
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MultiplePermissionsDemo() {
    val context = LocalContext.current

    // Accompanist Permissions: 다중 권한 상태 관리
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "카메라 + 마이크 권한 (다중)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 각 권한별 상태 표시
            multiplePermissionsState.permissions.forEach { permissionState ->
                val permissionName = permissionState.permission.substringAfterLast(".")
                PermissionStatusIndicator(
                    permissionName = permissionName,
                    isGranted = permissionState.status.isGranted,
                    shouldShowRationale = permissionState.status.shouldShowRationale
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 상태별 UI 분기
            when {
                multiplePermissionsState.allPermissionsGranted -> {
                    GrantedContent("모든 권한이 승인되었습니다!\n영상 녹화 기능을 사용할 수 있습니다.")
                }
                multiplePermissionsState.shouldShowRationale -> {
                    RationaleContent(
                        message = "영상 녹화를 위해 카메라와 마이크 권한이 필요합니다.",
                        onRequestAgain = { multiplePermissionsState.launchMultiplePermissionRequest() }
                    )
                }
                else -> {
                    Column {
                        // 거부된 권한 목록 표시
                        val deniedPermissions = multiplePermissionsState.revokedPermissions
                            .map { it.permission.substringAfterLast(".") }
                            .joinToString(", ")

                        if (deniedPermissions.isNotEmpty()) {
                            Text(
                                text = "필요한 권한: $deniedPermissions",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        InitialOrDeniedContent(
                            onRequest = { multiplePermissionsState.launchMultiplePermissionRequest() },
                            onOpenSettings = { openAppSettings(context) }
                        )
                    }
                }
            }
        }
    }

    // 핵심 코드 설명
    CodeExplanationCard(
        title = "다중 권한 요청 코드",
        code = """
val multiplePermissionsState = rememberMultiplePermissionsState(
    permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
)

if (multiplePermissionsState.allPermissionsGranted) {
    // 모든 권한 승인됨
} else {
    // 거부된 권한 확인
    val denied = multiplePermissionsState.revokedPermissions

    Button(onClick = {
        multiplePermissionsState.launchMultiplePermissionRequest()
    })
}
        """.trimIndent()
    )
}

/**
 * 알림 권한 데모 - Android 13+ (API 33+)
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionDemo() {
    val context = LocalContext.current

    // Android 13+ 에서만 POST_NOTIFICATIONS 권한 필요
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "알림 권한 (Android 13+)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Android 13 (API 33)부터 알림을 보내려면\nPOST_NOTIFICATIONS 권한이 필요합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                PermissionStatusIndicator(
                    permissionName = "POST_NOTIFICATIONS",
                    isGranted = notificationPermissionState.status.isGranted,
                    shouldShowRationale = notificationPermissionState.status.shouldShowRationale
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    notificationPermissionState.status.isGranted -> {
                        GrantedContent("알림을 보낼 수 있습니다!")
                    }
                    notificationPermissionState.status.shouldShowRationale -> {
                        RationaleContent(
                            message = "중요한 알림을 받으시려면 권한을 허용해주세요.",
                            onRequestAgain = { notificationPermissionState.launchPermissionRequest() }
                        )
                    }
                    else -> {
                        InitialOrDeniedContent(
                            onRequest = { notificationPermissionState.launchPermissionRequest() },
                            onOpenSettings = { openAppSettings(context) }
                        )
                    }
                }
            }
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "알림 권한 (Android 12 이하)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Android 12 이하에서는 별도의 알림 권한 요청이 필요 없습니다.")
                Text("알림 채널 설정만 하면 됩니다.")
            }
        }
    }

    // Android 14+ 변경사항 안내
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Android 14/15 권한 변경사항",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("- targetSdk 23 미만 앱 설치 불가 (Android 14)")
            Text("- Foreground Service Type 필수 선언 (Android 14)")
            Text("- content:// URI 접근 시 더 엄격한 검사 (Android 15)")
            Text("- 백그라운드 Activity 시작 제한 강화 (Android 15)")
        }
    }
}

// ========== UI 컴포넌트 ==========

@Composable
private fun PermissionStatusIndicator(
    permissionName: String,
    isGranted: Boolean,
    shouldShowRationale: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (isGranted) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isGranted) Color(0xFF4CAF50) else Color(0xFFF44336),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = permissionName,
            style = MaterialTheme.typography.bodyMedium
        )
        if (shouldShowRationale) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "(설명 필요)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun GrantedContent(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF4CAF50)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message,
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
private fun RationaleContent(
    message: String,
    onRequestAgain: () -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onRequestAgain,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text("권한 다시 요청")
        }
    }
}

@Composable
private fun InitialOrDeniedContent(
    onRequest: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column {
        Button(onClick = onRequest) {
            Text("권한 요청")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onOpenSettings) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("설정에서 권한 허용")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "권한 요청이 나타나지 않으면 '설정에서 권한 허용' 버튼을 눌러주세요.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun CodeExplanationCard(title: String, code: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = code,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 앱 설정 화면으로 이동
 */
fun openAppSettings(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(this)
    }
}
