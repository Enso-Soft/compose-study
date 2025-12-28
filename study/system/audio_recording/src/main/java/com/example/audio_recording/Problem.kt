package com.example.audio_recording

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

/**
 * 문제가 있는 코드 - MediaRecorder 생명주기 미관리 + 권한 미처리
 *
 * 이 코드의 문제점:
 * 1. 권한 확인 없이 MediaRecorder 사용 시도 -> SecurityException 발생
 * 2. MediaRecorder를 Composable 내부에서 직접 생성 -> Recomposition마다 새로 생성
 * 3. release() 미호출 -> 마이크 리소스 점유 지속, 메모리 누수
 * 4. 화면 이탈 시 정리 없음 -> 녹음 중 화면 전환 시 파일 손상
 */
@Composable
fun ProblemScreen() {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 문제 1: 권한 확인 없음
    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    // 문제 2: MediaRecorder가 Composable 밖에서 관리되지 않음
    // 실제로 여기서 생성하면 매 recomposition마다 새 인스턴스 생성됨
    // val recorder = MediaRecorder() // 이렇게 하면 안 됨!

    // 문제 3: DisposableEffect 없음 -> release() 호출 보장 없음
    // DisposableEffect(Unit) {
    //     onDispose {
    //         recorder.release()  // 이게 없으면 리소스 누수!
    //     }
    // }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "이 코드에는 심각한 문제가 있습니다!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 상태 표시 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("RECORD_AUDIO 권한: ${if (hasPermission) "허용됨" else "거부됨"}")
                Text("녹음 상태: ${if (isRecording) "녹음 중 (시뮬레이션)" else "대기 중"}")

                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "오류: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 위험한 녹음 버튼 (시뮬레이션)
        Button(
            onClick = {
                // 실제 코드에서 이렇게 하면 크래시 발생!
                // 여기서는 시뮬레이션만 함
                if (!hasPermission) {
                    errorMessage = "SecurityException: RECORD_AUDIO 권한이 없습니다!"
                } else {
                    isRecording = !isRecording
                    if (isRecording) {
                        errorMessage = "녹음 시작됨 (시뮬레이션)\n" +
                            "실제로는 release() 없이 화면 나가면\n" +
                            "마이크 리소스가 해제되지 않습니다!"
                    } else {
                        errorMessage = null
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(if (isRecording) "녹음 중지 (시뮬레이션)" else "녹음 시작 (시뮬레이션)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제점 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제점 분석",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 1,
                    title = "권한 미처리",
                    description = "권한 확인 없이 MediaRecorder.setAudioSource() 호출 시 SecurityException 발생"
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 2,
                    title = "생명주기 미관리",
                    description = "MediaRecorder를 Composable 내부에서 직접 생성하면 Recomposition마다 새 인스턴스 생성"
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 3,
                    title = "리소스 누수",
                    description = "release() 미호출로 마이크 점유 지속, 다른 앱에서 마이크 사용 불가"
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 4,
                    title = "상태 불일치",
                    description = "화면 회전/이탈 시 녹음이 중단되지 않아 파일 손상 가능"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 잘못된 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 패턴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                    @Composable
                    fun BadRecorder() {
                        // X: 권한 확인 없음
                        val recorder = MediaRecorder()

                        Button(onClick = {
                            // X: SecurityException 발생!
                            recorder.setAudioSource(MIC)
                            recorder.start()
                        })

                        // X: release() 호출 없음
                        // → 메모리 누수!
                    }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
