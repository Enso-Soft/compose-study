# Notification Integration 학습

## 개념

**Android 알림(Notification)**은 앱이 포그라운드에 있지 않을 때 사용자에게 정보를 전달하는 핵심 메커니즘입니다. Jetpack Compose에서는 권한 요청, 채널 관리, 알림 생성을 효과적으로 통합할 수 있습니다.

### 알림의 핵심 구성 요소

```
┌─────────────────────────────────────────────┐
│           Android 알림 시스템                 │
├─────────────────────────────────────────────┤
│                                             │
│  1. NotificationChannel (API 26+)           │
│     └─ 알림 그룹화 및 사용자 제어            │
│                                             │
│  2. NotificationCompat.Builder              │
│     └─ 알림 생성의 핵심 클래스               │
│                                             │
│  3. POST_NOTIFICATIONS 권한 (API 33+)       │
│     └─ 런타임 권한 요청 필요                 │
│                                             │
│  4. NotificationManagerCompat               │
│     └─ 알림 표시 및 관리                     │
│                                             │
└─────────────────────────────────────────────┘
```

---

## 핵심 특징

### 1. NotificationChannel (Android 8.0+)

```kotlin
// 채널 생성 - 앱 시작 시 한 번만 실행
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        "channel_id",           // 고유 ID
        "채널 이름",             // 사용자에게 보이는 이름
        NotificationManager.IMPORTANCE_DEFAULT  // 중요도
    ).apply {
        description = "채널 설명"
    }

    val manager = getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)
}
```

**중요도 레벨:**

| 중요도 | 상수 | 동작 |
|--------|------|------|
| 높음 | IMPORTANCE_HIGH | 소리, 헤드업 알림 |
| 기본 | IMPORTANCE_DEFAULT | 소리, 상태바에 표시 |
| 낮음 | IMPORTANCE_LOW | 소리 없음, 상태바에 표시 |
| 최소 | IMPORTANCE_MIN | 소리 없음, 숨겨진 영역에만 |

### 2. POST_NOTIFICATIONS 권한 (Android 13+)

```kotlin
// AndroidManifest.xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

// Compose에서 권한 요청
@Composable
fun NotificationPermission() {
    var hasPermission by remember {
        mutableStateOf(checkPermission(context))
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasPermission) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
```

### 3. 기본 알림 생성

```kotlin
val notification = NotificationCompat.Builder(context, CHANNEL_ID)
    .setSmallIcon(R.drawable.ic_notification)  // 필수!
    .setContentTitle("알림 제목")               // 필수!
    .setContentText("알림 내용")                // 필수!
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setAutoCancel(true)  // 클릭 시 자동 제거
    .build()

// 권한 확인 후 표시
if (checkPermission(context)) {
    NotificationManagerCompat.from(context).notify(notificationId, notification)
}
```

---

## 문제 상황: 채널/권한 없이 알림 생성

### 잘못된 코드 예시

```kotlin
// 문제 1: 채널 생성 없이 바로 알림
val notification = NotificationCompat.Builder(
    context,
    "nonexistent_channel"  // 존재하지 않는 채널!
)
.setSmallIcon(R.drawable.ic_notification)
.setContentTitle("알림")
.build()

// 문제 2: 권한 확인 없이 바로 notify
val manager = context.getSystemService(NotificationManager::class.java)
manager.notify(1, notification)
```

### 발생하는 문제점

1. **Android 8.0+**: 채널 없으면 알림이 표시되지 않음
2. **Android 13+**: 권한 없으면 알림이 조용히 실패
3. **디버깅 어려움**: 에러 메시지 없이 실패
4. **사용자 혼란**: 왜 알림이 안 오는지 알 수 없음

```
[알림 표시 시도]
      ↓
[채널 존재 확인] ── 없음 ──→ 조용히 실패
      ↓ 있음
[권한 확인] ── 없음 ──→ 조용히 실패
      ↓ 있음
[알림 표시] ✓
```

---

## 해결책: 올바른 알림 구현

### 1. Application에서 채널 생성

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "일반 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
```

### 2. Compose에서 권한 요청

```kotlin
@Composable
fun NotificationScreen() {
    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    // 권한 요청 UI
    if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }) {
            Text("알림 권한 허용")
        }
    }
}
```

### 3. 안전한 알림 표시

```kotlin
fun showNotificationSafely(context: Context, title: String, content: String) {
    // 권한 확인
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return  // 권한 없으면 중단
        }
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setContentText(content)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(
        System.currentTimeMillis().toInt(),
        notification
    )
}
```

---

## Rich 알림 스타일

### BigTextStyle (긴 텍스트)

```kotlin
.setStyle(
    NotificationCompat.BigTextStyle()
        .bigText("매우 긴 텍스트 내용...")
        .setBigContentTitle("확장된 제목")
        .setSummaryText("요약")
)
```

### BigPictureStyle (큰 이미지)

```kotlin
.setStyle(
    NotificationCompat.BigPictureStyle()
        .bigPicture(bitmap)
        .bigLargeIcon(null as Bitmap?)  // 확장 시 아이콘 숨김
        .setBigContentTitle("확장된 제목")
)
```

### InboxStyle (여러 줄)

```kotlin
.setStyle(
    NotificationCompat.InboxStyle()
        .addLine("첫 번째 줄")
        .addLine("두 번째 줄")
        .addLine("세 번째 줄")  // 최대 6줄
        .setSummaryText("+3개 더")
)
```

---

## 진행 상태 알림

```kotlin
@Composable
fun DownloadWithProgress() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDownloading by remember { mutableStateOf(false) }

    Button(onClick = {
        scope.launch {
            isDownloading = true
            val notificationId = 999

            for (progress in 0..100 step 10) {
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_download)
                    .setContentTitle("다운로드 중...")
                    .setContentText("${progress}% 완료")
                    .setProgress(100, progress, false)
                    .setOngoing(true)  // 스와이프로 제거 방지
                    .build()

                NotificationManagerCompat.from(context)
                    .notify(notificationId, notification)

                delay(500)
            }

            // 완료 알림
            val doneNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_done)
                .setContentTitle("다운로드 완료")
                .setProgress(0, 0, false)  // 진행바 제거
                .setOngoing(false)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context)
                .notify(notificationId, doneNotification)

            isDownloading = false
        }
    }) {
        Text(if (isDownloading) "다운로드 중..." else "다운로드 시작")
    }
}
```

**핵심 포인트:**
- 같은 `notificationId`로 `notify()`하면 기존 알림이 업데이트됨
- `setOngoing(true)`로 진행 중 스와이프 방지
- 완료 시 `setProgress(0, 0, false)`로 진행바 제거

---

## 주의사항

### 1. 채널 생성 시점
- **앱 시작 시 한 번만** 생성
- 같은 ID로 재생성해도 기존 설정 유지 (사용자가 변경한 설정)
- Application.onCreate()에서 생성 권장

### 2. 권한 요청 타이밍
- 앱 첫 실행 시 바로 요청하지 말 것
- **알림이 필요한 맥락에서** 요청 (예: 설정 화면, 기능 사용 시)
- 거부 시 대안 제공

### 3. Android 버전 대응

| 버전 | 채널 | 권한 |
|------|------|------|
| 7.1 이하 (API 25-) | 불필요 | 불필요 |
| 8.0-12 (API 26-32) | **필수** | 불필요 |
| 13+ (API 33+) | **필수** | **필수** |

### 4. 알림 아이콘
- `setSmallIcon()` 필수
- 24x24dp 벡터 드로어블 권장
- 투명 배경 + 단색 아이콘

---

## 연습 문제

### 연습 1: 권한 요청 및 기본 알림 (초급)
- POST_NOTIFICATIONS 권한 상태 확인
- 권한 요청 런처 구현
- 기본 알림 표시

### 연습 2: BigTextStyle 알림 (중급)
- 사용자 입력을 받아 알림 생성
- BigTextStyle 적용
- 채널 생성 확인

### 연습 3: 진행 상태 알림 (고급)
- 코루틴으로 진행 상태 시뮬레이션
- 같은 ID로 알림 업데이트
- 완료 시 알림 변경

---

## 다음 학습

- **Foreground Service**: 장기 실행 작업과 알림
- **알림 액션**: 알림에서 바로 동작 수행
- **알림 그룹화**: 여러 알림을 그룹으로 묶기
- **알림 클릭 처리**: PendingIntent로 화면 이동
