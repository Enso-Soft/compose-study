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

// 알림 채널 상수
object NotificationConstants {
    const val CHANNEL_ID = "notification_study_channel"
    const val CHANNEL_NAME = "학습 알림"
    const val CHANNEL_DESCRIPTION = "Notification Integration 학습용 알림 채널"

    const val PROGRESS_CHANNEL_ID = "progress_channel"
    const val PROGRESS_CHANNEL_NAME = "진행 상태 알림"
}

/**
 * 올바른 코드 - 채널 생성 + 권한 처리 + 알림 표시
 *
 * 핵심 포인트:
 * 1. 앱 시작 시 채널 생성 (Android 8.0+)
 * 2. POST_NOTIFICATIONS 권한 요청 (Android 13+)
 * 3. 권한 확인 후 알림 표시
 * 4. Rich 알림 스타일 지원
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 권한 상태 관리
    var hasPermission by remember {
        mutableStateOf(checkNotificationPermission(context))
    }

    // 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    // 알림 채널 생성 (앱 시작 시 한 번만)
    LaunchedEffect(Unit) {
        createNotificationChannels(context)
    }

    // 알림 카운터
    var notificationId by remember { mutableIntStateOf(100) }

    // 진행 상태 알림용 상태
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 권한 상태 카드
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
                Text(
                    text = "권한 상태",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (hasPermission) "허용됨" else "거부됨",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Button(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        ) {
                            Text("권한 요청")
                        }
                    }
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    Text(
                        text = "(Android 12 이하는 권한 불필요)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 기본 알림 섹션
        Text(
            text = "1. 기본 알림",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    showBasicNotification(context, notificationId++)
                }
            },
            enabled = hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            Text("기본 알림 보내기")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Rich 알림 섹션
        Text(
            text = "2. Rich 알림 스타일",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        showBigTextNotification(context, notificationId++)
                    }
                },
                enabled = hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU,
                modifier = Modifier.weight(1f)
            ) {
                Text("BigText")
            }

            Button(
                onClick = {
                    if (hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        showBigPictureNotification(context, notificationId++)
                    }
                },
                enabled = hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU,
                modifier = Modifier.weight(1f)
            ) {
                Text("BigPicture")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    showInboxNotification(context, notificationId++)
                }
            },
            enabled = hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("InboxStyle 알림")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 진행 상태 알림 섹션
        Text(
            text = "3. 진행 상태 알림",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isDownloading) {
            LinearProgressIndicator(
                progress = { downloadProgress / 100f },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("다운로드 중... ${downloadProgress}%")
        }

        Button(
            onClick = {
                if ((hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                    && !isDownloading
                ) {
                    scope.launch {
                        isDownloading = true
                        downloadProgress = 0

                        val progressNotificationId = 999
                        for (progress in 0..100 step 10) {
                            downloadProgress = progress
                            showProgressNotification(
                                context,
                                progressNotificationId,
                                progress,
                                isComplete = false
                            )
                            delay(500)
                        }

                        // 완료 알림
                        showProgressNotification(
                            context,
                            progressNotificationId,
                            100,
                            isComplete = true
                        )
                        isDownloading = false
                    }
                }
            },
            enabled = (hasPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                    && !isDownloading
        ) {
            Text(if (isDownloading) "다운로드 중..." else "다운로드 시작")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 핵심 포인트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 채널은 앱 시작 시 한 번만 생성")
                Text("2. Android 13+는 POST_NOTIFICATIONS 권한 필수")
                Text("3. 권한 확인 후 알림 표시")
                Text("4. setStyle()로 Rich 알림 생성")
                Text("5. 진행 알림은 같은 ID로 업데이트")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 올바른 코드 패턴
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드 패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 1. 채널 생성 (앱 시작 시)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        CHANNEL_ID,
        "채널 이름",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    notificationManager.createNotificationChannel(channel)
}

// 2. 권한 확인 (Android 13+)
if (ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.POST_NOTIFICATIONS
) == PackageManager.PERMISSION_GRANTED) {
    // 3. 알림 표시
    NotificationManagerCompat.from(context)
        .notify(id, notification)
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 권한 확인 함수
 */
fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Android 12 이하는 권한 불필요
    }
}

/**
 * 알림 채널 생성
 */
fun createNotificationChannels(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

        // 기본 채널
        val defaultChannel = NotificationChannel(
            NotificationConstants.CHANNEL_ID,
            NotificationConstants.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = NotificationConstants.CHANNEL_DESCRIPTION
        }

        // 진행 상태 채널
        val progressChannel = NotificationChannel(
            NotificationConstants.PROGRESS_CHANNEL_ID,
            NotificationConstants.PROGRESS_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "다운로드 등 진행 상태 알림"
        }

        notificationManager.createNotificationChannel(defaultChannel)
        notificationManager.createNotificationChannel(progressChannel)
    }
}

/**
 * 기본 알림 표시
 */
private fun showBasicNotification(context: Context, id: Int) {
    val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("기본 알림")
        .setContentText("이것은 기본 알림입니다. (ID: $id)")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    if (checkNotificationPermission(context)) {
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

/**
 * BigTextStyle 알림 표시
 */
private fun showBigTextNotification(context: Context, id: Int) {
    val longText = """
        BigTextStyle은 긴 텍스트를 표시할 때 사용합니다.

        사용자가 알림을 확장하면 전체 텍스트가 보입니다.
        이메일 미리보기나 긴 메시지 표시에 적합합니다.

        setStyle(NotificationCompat.BigTextStyle())을 사용하세요.
    """.trimIndent()

    val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("BigTextStyle 알림")
        .setContentText("확장하면 긴 텍스트가 보입니다")
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(longText)
                .setBigContentTitle("확장된 제목")
                .setSummaryText("요약 텍스트")
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    if (checkNotificationPermission(context)) {
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

/**
 * BigPictureStyle 알림 표시
 */
private fun showBigPictureNotification(context: Context, id: Int) {
    // 샘플 비트맵 생성 (실제로는 네트워크에서 로드하거나 PNG 리소스 사용)
    val bitmap = android.graphics.Bitmap.createBitmap(200, 200, android.graphics.Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.parseColor("#3DDC84")) // Android Green
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 40f
        textAlign = android.graphics.Paint.Align.CENTER
    }
    canvas.drawText("Compose", 100f, 110f, paint)

    val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("BigPictureStyle 알림")
        .setContentText("확장하면 큰 이미지가 보입니다")
        .setLargeIcon(bitmap)
        .setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null as android.graphics.Bitmap?) // 확장 시 largeIcon 숨김
                .setBigContentTitle("확장된 제목")
                .setSummaryText("이미지 알림 예시")
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    if (checkNotificationPermission(context)) {
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

/**
 * InboxStyle 알림 표시
 */
private fun showInboxNotification(context: Context, id: Int) {
    val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("새 메시지 5개")
        .setContentText("받은 메시지가 있습니다")
        .setStyle(
            NotificationCompat.InboxStyle()
                .addLine("홍길동: 안녕하세요!")
                .addLine("김철수: 회의 일정 변경되었습니다")
                .addLine("이영희: 자료 확인 부탁드립니다")
                .addLine("박민수: 점심 같이 하실래요?")
                .addLine("최지은: 프로젝트 진행상황 공유")
                .setBigContentTitle("새 메시지 5개")
                .setSummaryText("example@email.com")
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    if (checkNotificationPermission(context)) {
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}

/**
 * 진행 상태 알림 표시
 */
private fun showProgressNotification(
    context: Context,
    id: Int,
    progress: Int,
    isComplete: Boolean
) {
    val builder = NotificationCompat.Builder(context, NotificationConstants.PROGRESS_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(!isComplete) // 완료 전까지는 스와이프로 제거 불가

    if (isComplete) {
        builder
            .setContentTitle("다운로드 완료")
            .setContentText("파일 다운로드가 완료되었습니다")
            .setProgress(0, 0, false) // 진행바 제거
            .setAutoCancel(true)
    } else {
        builder
            .setContentTitle("다운로드 중...")
            .setContentText("${progress}% 완료")
            .setProgress(100, progress, false)
    }

    if (checkNotificationPermission(context)) {
        NotificationManagerCompat.from(context).notify(id, builder.build())
    }
}
