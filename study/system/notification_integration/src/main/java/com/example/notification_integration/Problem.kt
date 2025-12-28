package com.example.notification_integration

import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat

/**
 * 문제가 있는 코드 - 채널 없이/권한 없이 알림 생성
 *
 * 이 코드를 실행하면 다음 문제가 발생합니다:
 * 1. 채널 미생성: Android 8.0+에서 알림이 표시되지 않음
 * 2. 권한 미확인: Android 13+에서 알림이 조용히 실패
 * 3. 에러 메시지 없음: 왜 알림이 안 뜨는지 디버깅 어려움
 */
@Composable
fun ProblemScreen() {
    val context = LocalContext.current
    var attemptCount by remember { mutableIntStateOf(0) }
    var lastAttemptResult by remember { mutableStateOf("아직 시도하지 않음") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: 채널/권한 없이 알림 생성",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "알림 시도 횟수: ${attemptCount}회",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "마지막 결과: $lastAttemptResult",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제가 있는 알림 버튼 (실제로 실행하면 실패)
        Button(
            onClick = {
                attemptCount++
                // 문제 1: 채널이 생성되지 않음
                // 문제 2: 권한 확인 없음
                // 문제 3: 존재하지 않는 채널 ID 사용
                val result = showBrokenNotification(context, attemptCount)
                lastAttemptResult = result
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("알림 보내기 (실패할 것임)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 문제인가?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. NotificationChannel을 생성하지 않음")
                Text("   - Android 8.0+에서 채널 없으면 알림 표시 안 됨")
                Spacer(modifier = Modifier.height(4.dp))
                Text("2. POST_NOTIFICATIONS 권한 확인 안 함")
                Text("   - Android 13+에서 권한 없으면 조용히 실패")
                Spacer(modifier = Modifier.height(4.dp))
                Text("3. 에러 처리 없음")
                Text("   - 사용자는 왜 알림이 안 뜨는지 모름")
            }
        }

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
// 문제 1: 채널 생성 없이 바로 알림
val notification = NotificationCompat.Builder(
    context,
    "nonexistent_channel"  // 존재하지 않는 채널!
)
.setSmallIcon(R.drawable.ic_notification)
.setContentTitle("알림")
.build()

// 문제 2: 권한 확인 없이 바로 notify
notificationManager.notify(1, notification)
// 결과: Android 8.0+에서 조용히 실패
// 결과: Android 13+에서 권한 없으면 실패
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Android 버전별 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Android 버전별 동작",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Android 7.1 이하:",
                    style = MaterialTheme.typography.labelLarge
                )
                Text("- 채널 필요 없음, 알림 표시됨")

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Android 8.0 ~ 12:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Text("- 채널 없으면 알림 표시 안 됨")
                Text("- 에러 없이 조용히 실패")

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Android 13+:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Text("- 채널 필수 + POST_NOTIFICATIONS 권한 필수")
                Text("- 둘 중 하나라도 없으면 알림 표시 안 됨")
            }
        }
    }
}

/**
 * 문제가 있는 알림 함수 - 실패를 보여주기 위한 코드
 */
private fun showBrokenNotification(context: Context, id: Int): String {
    return try {
        // 문제 1: 채널을 생성하지 않음
        // 문제 2: 존재하지 않는 채널 ID 사용
        val notification = NotificationCompat.Builder(context, "nonexistent_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("테스트 알림 #$id")
            .setContentText("이 알림은 표시되지 않을 것입니다")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // 문제 3: 권한 확인 없이 바로 notify
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
        notificationManager.notify(id, notification)

        // 실제로는 에러 없이 실패함
        "notify() 호출됨 - 하지만 알림이 표시되지 않을 수 있음"
    } catch (e: Exception) {
        "에러: ${e.message}"
    }
}
