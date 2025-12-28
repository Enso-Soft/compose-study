package com.example.permission_handling

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

/**
 * 문제가 있는 코드 - 권한 처리 없이 기능 사용
 *
 * 이 코드의 문제점:
 * 1. 권한 상태를 UI에 반영하지 않음 -> 사용자가 현재 상태를 알 수 없음
 * 2. shouldShowRationale 무시 -> 사용자에게 권한 필요 이유 설명 안 함
 * 3. 영구 거부 상태 미처리 -> 설정 화면 안내 없음
 * 4. 권한 결과에 따른 후속 처리 없음
 */
@Composable
fun ProblemScreen() {
    val context = LocalContext.current
    var cameraPermissionStatus by remember { mutableStateOf("확인 안 함") }

    // 문제 1: 권한 요청 후 결과를 제대로 처리하지 않음
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // 단순히 승인/거부만 표시 - shouldShowRationale, 영구 거부 처리 없음
        cameraPermissionStatus = if (isGranted) "승인됨" else "거부됨"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Problem: 잘못된 권한 처리",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        // 문제 상황 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "이 코드의 문제점",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem("1. 권한 상태를 UI에 실시간 반영하지 않음")
                ProblemItem("2. shouldShowRationale 무시 (권한 필요 이유 설명 안 함)")
                ProblemItem("3. 영구 거부 시 설정 화면 안내 없음")
                ProblemItem("4. 권한 요청 전 현재 상태 확인 안 함")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("카메라 권한 상태: $cameraPermissionStatus")
            }
        }

        // 문제 2: 권한 요청 전에 현재 상태를 확인하지 않음
        Button(
            onClick = {
                // 문제: 이미 권한이 있는지 확인하지 않고 바로 요청
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("카메라 권한 요청 (잘못된 방식)")
        }

        // 참고용 - 현재 실제 권한 상태
        val actualStatus = if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) "실제: 승인됨" else "실제: 미승인"

        Text(
            text = actualStatus,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 잘못된 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 문제 1: 결과만 단순 저장
val launcher = rememberLauncherForActivityResult(
    RequestPermission()
) { isGranted ->
    status = if (isGranted) "승인" else "거부"
    // shouldShowRationale 확인 없음!
    // 설정 화면 안내 없음!
}

// 문제 2: 현재 상태 확인 없이 요청
Button(onClick = {
    launcher.launch(permission)
})
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Solution 탭에서 해결책 확인",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- Accompanist Permissions 라이브러리 사용")
                Text("- 권한 상태별 UI 분기 처리")
                Text("- shouldShowRationale 활용")
                Text("- 설정 화면 이동 기능")
            }
        }
    }
}

@Composable
private fun ProblemItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
