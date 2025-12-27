package com.example.notification_integration

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 연습 문제 1: 권한 요청 및 기본 알림 (초급)
 *
 * 요구사항:
 * - POST_NOTIFICATIONS 권한 상태 확인
 * - 권한이 없으면 요청 버튼 표시
 * - 권한이 있으면 알림 발송 버튼 표시
 *
 * TODO: 아래 코드를 완성하세요!
 */
@Composable
fun Practice1_PermissionAndBasicNotification() {
    val context = LocalContext.current

    // TODO 1: 권한 상태를 저장할 State 생성
    // 힌트: mutableStateOf와 checkPracticePermission() 사용
    var hasPermission by remember { mutableStateOf(false) }

    // TODO 2: 권한 요청 런처 생성
    // 힌트: rememberLauncherForActivityResult와
    //       ActivityResultContracts.RequestPermission() 사용
    /*
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }
    */

    // TODO 3: LaunchedEffect로 초기 권한 상태 확인
    // 힌트: checkPracticePermission(context) 호출
    /*
    LaunchedEffect(Unit) {
        hasPermission = checkPracticePermission(context)
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 권한 처리 및 기본 알림",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 권한 요청과 알림 발송을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 권한 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (hasPermission)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("권한 상태: ${if (hasPermission) "허용됨" else "거부됨"}")

                Spacer(modifier = Modifier.height(8.dp))

                // TODO 4: 권한 없으면 요청 버튼, 있으면 알림 버튼 표시
                // 힌트: if (!hasPermission) { 권한 요청 버튼 } else { 알림 버튼 }

                if (!hasPermission) {
                    Button(onClick = {
                        // TODO: permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }) {
                        Text("권한 요청 (구현 필요)")
                    }
                } else {
                    Button(onClick = {
                        // TODO: showPractice1Notification(context)
                    }) {
                        Text("알림 보내기 (구현 필요)")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. rememberLauncherForActivityResult로 권한 결과 수신")
                Text("2. Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU 확인")
                Text("3. ContextCompat.checkSelfPermission()으로 권한 확인")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 정답 (주석 처리)
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 (참고용)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 권한 상태
var hasPermission by remember {
    mutableStateOf(checkPracticePermission(context))
}

// 권한 런처
val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { granted ->
    hasPermission = granted
}

// 권한 요청
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 연습 문제 2: BigTextStyle 알림 (중급)
 *
 * 요구사항:
 * - 사용자가 입력한 제목과 내용으로 알림 생성
 * - BigTextStyle 적용
 * - 채널 생성 확인
 *
 * TODO: 아래 코드를 완성하세요!
 */
@Composable
fun Practice2_BigTextNotification() {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var hasPermission by remember { mutableStateOf(checkPracticePermission(context)) }

    // 채널 생성
    LaunchedEffect(Unit) {
        createPracticeChannel(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: BigTextStyle 알림",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 입력한 내용으로 BigTextStyle 알림을 보내세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("알림 제목") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("알림 내용 (긴 텍스트)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // TODO: BigTextStyle 알림 생성 및 표시
                // 힌트: NotificationCompat.Builder 생성
                // 힌트: .setStyle(NotificationCompat.BigTextStyle().bigText(...))
                // 힌트: NotificationManagerCompat.from(context).notify(...)

                /*
                if (hasPermission && title.isNotEmpty() && content.isNotEmpty()) {
                    val notification = NotificationCompat.Builder(context, PRACTICE_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(content.take(30) + "...")
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText(content)
                                .setBigContentTitle(title)
                        )
                        .setAutoCancel(true)
                        .build()

                    NotificationManagerCompat.from(context)
                        .notify(System.currentTimeMillis().toInt(), notification)
                }
                */
            },
            enabled = hasPermission && title.isNotEmpty() && content.isNotEmpty()
        ) {
            Text("BigTextStyle 알림 보내기")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. NotificationCompat.Builder(context, CHANNEL_ID)")
                Text("2. .setStyle(NotificationCompat.BigTextStyle())")
                Text("3. .bigText(longText)로 긴 텍스트 설정")
                Text("4. .setBigContentTitle()로 확장 시 제목 설정")
            }
        }
    }
}

/**
 * 연습 문제 3: 진행 상태 알림 (고급)
 *
 * 요구사항:
 * - 시작 버튼 클릭 시 진행 상태 알림 표시
 * - 코루틴으로 0% → 100% 진행
 * - 완료 시 "완료" 알림으로 변경
 *
 * TODO: 아래 코드를 완성하세요!
 */
@Composable
fun Practice3_ProgressNotification() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // TODO 1: 상태 변수 생성
    var isRunning by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }
    var hasPermission by remember { mutableStateOf(checkPracticePermission(context)) }

    // 채널 생성
    LaunchedEffect(Unit) {
        createPracticeProgressChannel(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: 진행 상태 알림",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 코루틴으로 진행 상태 알림을 업데이트하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 진행 상태 표시
        if (isRunning) {
            CircularProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier.size(120.dp),
                strokeWidth = 8.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${progress}%",
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            Text(
                text = if (progress == 100) "완료!" else "대기 중",
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // TODO 2: 코루틴으로 진행 상태 업데이트
                // 힌트: scope.launch 사용
                // 힌트: for 루프로 0..100 진행
                // 힌트: delay(100)로 시뮬레이션
                // 힌트: 각 단계에서 알림 업데이트

                /*
                scope.launch {
                    isRunning = true
                    progress = 0
                    val notificationId = 888

                    for (p in 0..100 step 5) {
                        progress = p
                        showPracticeProgressNotification(
                            context,
                            notificationId,
                            p,
                            isComplete = false
                        )
                        delay(100)
                    }

                    // 완료 알림
                    showPracticeProgressNotification(
                        context,
                        notificationId,
                        100,
                        isComplete = true
                    )
                    isRunning = false
                }
                */
            },
            enabled = hasPermission && !isRunning
        ) {
            Text(if (isRunning) "진행 중..." else "시작")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. scope.launch { } 안에서 진행")
                Text("2. setProgress(max, progress, indeterminate)")
                Text("3. 같은 notification ID로 notify()하면 업데이트")
                Text("4. setOngoing(true)로 진행 중 스와이프 방지")
                Text("5. 완료 시 setProgress(0, 0, false)로 진행바 제거")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 정답 코드
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 (참고용)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
scope.launch {
    isRunning = true
    val id = 888

    for (p in 0..100 step 5) {
        progress = p
        val notification = NotificationCompat.Builder(
            context, PROGRESS_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("진행 중...")
            .setContentText("${'$'}p% 완료")
            .setProgress(100, p, false)
            .setOngoing(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(id, notification)
        delay(100)
    }

    // 완료 알림
    val done = NotificationCompat.Builder(
        context, PROGRESS_CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("완료!")
        .setProgress(0, 0, false)
        .setOngoing(false)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context)
        .notify(id, done)
    isRunning = false
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// 연습용 상수
private const val PRACTICE_CHANNEL_ID = "practice_channel"
private const val PRACTICE_PROGRESS_CHANNEL_ID = "practice_progress_channel"

/**
 * 연습용 권한 확인 함수
 */
private fun checkPracticePermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

/**
 * 연습용 채널 생성
 */
private fun createPracticeChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            PRACTICE_CHANNEL_ID,
            "연습용 알림",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

/**
 * 연습용 진행 상태 채널 생성
 */
private fun createPracticeProgressChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            PRACTICE_PROGRESS_CHANNEL_ID,
            "연습용 진행 알림",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

/**
 * 연습 1용 알림 표시
 */
private fun showPractice1Notification(context: Context) {
    if (!checkPracticePermission(context)) return

    val notification = NotificationCompat.Builder(context, PRACTICE_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("연습 1 성공!")
        .setContentText("권한 처리와 기본 알림을 구현했습니다.")
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context)
        .notify(System.currentTimeMillis().toInt(), notification)
}

/**
 * 연습 3용 진행 상태 알림 표시
 */
private fun showPracticeProgressNotification(
    context: Context,
    id: Int,
    progress: Int,
    isComplete: Boolean
) {
    if (!checkPracticePermission(context)) return

    val builder = NotificationCompat.Builder(context, PRACTICE_PROGRESS_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)

    if (isComplete) {
        builder
            .setContentTitle("작업 완료!")
            .setContentText("진행 상태 알림 연습을 완료했습니다.")
            .setProgress(0, 0, false)
            .setOngoing(false)
            .setAutoCancel(true)
    } else {
        builder
            .setContentTitle("작업 진행 중...")
            .setContentText("${progress}% 완료")
            .setProgress(100, progress, false)
            .setOngoing(true)
    }

    NotificationManagerCompat.from(context).notify(id, builder.build())
}
