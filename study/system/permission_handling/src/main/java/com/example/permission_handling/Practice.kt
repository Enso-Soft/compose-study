package com.example.permission_handling

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * 연습 문제 1: 위치 권한 요청 구현 (초급)
 *
 * 요구사항:
 * - ACCESS_FINE_LOCATION 권한 요청 구현
 * - 권한 상태에 따른 UI 분기 (승인/설명필요/미승인)
 * - 권한 요청 버튼 구현
 *
 * TODO: rememberPermissionState를 사용해서 구현하세요!
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Practice1_LocationPermission() {
    val context = LocalContext.current
    var showHint by remember { mutableStateOf(false) }

    // TODO 1: rememberPermissionState로 위치 권한 상태 생성
    // 힌트: android.Manifest.permission.ACCESS_FINE_LOCATION 사용

    /*
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 1: 위치 권한 요청",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "TODO: rememberPermissionState를 사용해서 위치 권한 처리를 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("현재 상태:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                // TODO 2: 권한 상태에 따른 UI 분기
                // - isGranted: "위치 권한 승인됨!" + 녹색 체크 아이콘
                // - shouldShowRationale: 설명 텍스트 + 재요청 버튼
                // - else: 초기 요청 UI + 요청 버튼

                Text(
                    text = "(구현 필요) - 권한 상태를 표시하세요",
                    color = MaterialTheme.colorScheme.error
                )

                /*
                when {
                    locationPermissionState.status.isGranted -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "위치 권한이 승인되었습니다!",
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                    locationPermissionState.status.shouldShowRationale -> {
                        Column {
                            Text("위치 권한이 필요합니다.")
                            Text("주변 맛집을 찾기 위해 사용됩니다.")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                                Text("다시 요청")
                            }
                        }
                    }
                    else -> {
                        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                            Text("위치 권한 요청")
                        }
                    }
                }
                */
            }
        }

        // 힌트 토글
        HintCard(
            showHint = showHint,
            onToggle = { showHint = !showHint },
            hints = listOf(
                "rememberPermissionState(permission) 사용",
                "permissionState.status.isGranted 로 승인 여부 확인",
                "permissionState.status.shouldShowRationale 로 설명 필요 여부 확인",
                "버튼 onClick에서 permissionState.launchPermissionRequest() 호출"
            )
        )
    }
}

/**
 * 연습 문제 2: 알림 권한 + 설정 화면 안내 (중급)
 *
 * 요구사항:
 * - POST_NOTIFICATIONS 권한 처리 (Android 13+ 전용)
 * - 영구 거부 상태 감지 후 설정 화면으로 유도
 * - Android 버전에 따른 분기 처리
 *
 * TODO: 권한 영구 거부 시 설정 화면 안내를 구현하세요!
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Practice2_NotificationWithSettings() {
    val context = LocalContext.current
    var showHint by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 2: 알림 권한 + 설정 안내",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "TODO: 영구 거부 시 설정 화면으로 이동하는 기능을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        // TODO 1: Android 버전 체크
        // 힌트: Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU (API 33)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // TODO 2: POST_NOTIFICATIONS 권한 상태 생성

                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notificationPermissionState = rememberPermissionState(
                        permission = Manifest.permission.POST_NOTIFICATIONS
                    )

                    when {
                        notificationPermissionState.status.isGranted -> {
                            Text("알림 권한 승인됨!", color = Color(0xFF4CAF50))
                        }
                        notificationPermissionState.status.shouldShowRationale -> {
                            Column {
                                Text("알림을 받으려면 권한이 필요합니다.")
                                Button(onClick = { notificationPermissionState.launchPermissionRequest() }) {
                                    Text("권한 요청")
                                }
                            }
                        }
                        else -> {
                            // TODO 3: 영구 거부 상태 - 설정 화면으로 이동
                            Column {
                                Text("알림 권한이 거부되었습니다.")
                                Text("설정에서 직접 권한을 허용해주세요.")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { notificationPermissionState.launchPermissionRequest() }) {
                                    Text("권한 요청")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(onClick = { openAppSettingsPractice(context) }) {
                                    Text("설정으로 이동")
                                }
                            }
                        }
                    }
                } else {
                    Text("Android 12 이하에서는 별도의 알림 권한 요청이 필요 없습니다.")
                }
                */

                Text(
                    text = "(구현 필요) - Android 버전에 따른 알림 권한 처리",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        // 힌트 토글
        HintCard(
            showHint = showHint,
            onToggle = { showHint = !showHint },
            hints = listOf(
                "Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU 로 버전 체크",
                "!isGranted && !shouldShowRationale 는 첫 요청 또는 영구 거부",
                "영구 거부와 첫 요청 구분이 어려우므로 둘 다 설정 버튼 제공 권장",
                "Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS) 사용"
            )
        )
    }
}

/**
 * 연습 문제 3: 다중 권한 처리 + 상태 리스트 표시 (고급)
 *
 * 요구사항:
 * - Camera, Microphone, Fine Location 3개 권한 한번에 처리
 * - 각 권한별 상태를 리스트로 표시
 * - 거부된 권한만 필터링하여 표시
 * - 전체 권한 요청 버튼 구현
 *
 * TODO: rememberMultiplePermissionsState를 사용해서 구현하세요!
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Practice3_MultiplePermissions() {
    val context = LocalContext.current
    var showHint by remember { mutableStateOf(false) }

    // TODO 1: Camera, Microphone, Fine Location 3개 권한 상태 생성
    // 힌트: rememberMultiplePermissionsState(listOf(...)) 사용

    /*
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 3: 다중 권한 처리",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "TODO: rememberMultiplePermissionsState를 사용해서 다중 권한 처리를 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("권한 상태 목록:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))

                // TODO 2: 각 권한별 상태를 리스트로 표시
                // - 권한 이름, 승인/거부 상태, 아이콘

                /*
                multiplePermissionsState.permissions.forEach { permissionState ->
                    val permissionName = permissionState.permission.substringAfterLast(".")
                    val isGranted = permissionState.status.isGranted

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
                        Text(permissionName)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = if (isGranted) "승인" else "거부",
                            color = if (isGranted) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
                */

                Text(
                    text = "(구현 필요) - 각 권한 상태를 리스트로 표시하세요",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        // TODO 3: allPermissionsGranted 확인 후 전체 기능 활성화 표시

        /*
        if (multiplePermissionsState.allPermissionsGranted) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("모든 권한 승인됨! 전체 기능 사용 가능")
                }
            }
        }
        */

        // TODO 4: 거부된 권한만 필터링하여 표시
        // 힌트: revokedPermissions 속성 사용

        /*
        val revokedPermissions = multiplePermissionsState.revokedPermissions
        if (revokedPermissions.isNotEmpty()) {
            Text(
                text = "거부된 권한: ${revokedPermissions.map {
                    it.permission.substringAfterLast(".")
                }.joinToString(", ")}",
                color = MaterialTheme.colorScheme.error
            )
        }
        */

        // TODO 5: 전체 권한 요청 버튼 구현
        // 힌트: launchMultiplePermissionRequest() 사용

        Button(
            onClick = {
                // TODO: multiplePermissionsState.launchMultiplePermissionRequest()
            },
            enabled = false // 구현 후 true로 변경
        ) {
            Text("전체 권한 요청 (구현 필요)")
        }

        OutlinedButton(onClick = { openAppSettingsPractice(context) }) {
            Text("설정으로 이동")
        }

        // 힌트 토글
        HintCard(
            showHint = showHint,
            onToggle = { showHint = !showHint },
            hints = listOf(
                "rememberMultiplePermissionsState(permissions = listOf(...)) 사용",
                "multiplePermissionsState.permissions 로 개별 권한 상태 접근",
                "multiplePermissionsState.allPermissionsGranted 로 전체 승인 확인",
                "multiplePermissionsState.revokedPermissions 로 거부된 권한만 필터링",
                "permission.substringAfterLast(\".\") 로 권한 이름 간단히 표시",
                "launchMultiplePermissionRequest() 로 전체 요청"
            )
        )
    }
}

// ========== 공통 컴포넌트 ==========

@Composable
private fun HintCard(
    showHint: Boolean,
    onToggle: () -> Unit,
    hints: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "힌트 보기",
                    style = MaterialTheme.typography.titleSmall
                )
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (showHint) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (showHint) "힌트 숨기기" else "힌트 보기"
                    )
                }
            }

            AnimatedVisibility(visible = showHint) {
                Column {
                    hints.forEachIndexed { index, hint ->
                        Text(
                            text = "${index + 1}. $hint",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 앱 설정 화면으로 이동 (연습용)
 */
private fun openAppSettingsPractice(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(this)
    }
}
