# Notification Integration

Android 알림 시스템과 Jetpack Compose 통합 학습 모듈입니다.

## 학습 목표

1. **알림 채널(Notification Channel)** 생성 및 관리
2. **권한 처리** - Android 13+ POST_NOTIFICATIONS 권한
3. **Rich 알림 스타일** - BigText, BigPicture, Inbox
4. **진행 상태 알림** - Progress 업데이트

---

## 핵심 개념

### 1. 알림 채널 (Android 8.0+)

Android 8.0(API 26)부터 모든 알림은 채널에 할당되어야 합니다.

```kotlin
val channel = NotificationChannel(
    CHANNEL_ID,
    "채널 이름",
    NotificationManager.IMPORTANCE_DEFAULT
)
notificationManager.createNotificationChannel(channel)
```

**중요도 레벨:**
- `IMPORTANCE_HIGH`: 소리와 헤드업 알림
- `IMPORTANCE_DEFAULT`: 소리, 알림창에 표시
- `IMPORTANCE_LOW`: 소리 없음, 알림창에 표시
- `IMPORTANCE_MIN`: 소리 없음, 상태바에 표시 안됨

### 2. 권한 처리 (Android 13+)

Android 13(API 33)부터 알림 권한을 런타임에 요청해야 합니다.

```kotlin
// AndroidManifest.xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

// 권한 확인
if (ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.POST_NOTIFICATIONS
) == PackageManager.PERMISSION_GRANTED) {
    // 알림 표시
}
```

### 3. Rich 알림 스타일

#### BigTextStyle
긴 텍스트를 확장 가능한 형태로 표시합니다.

```kotlin
NotificationCompat.Builder(context, CHANNEL_ID)
    .setStyle(
        NotificationCompat.BigTextStyle()
            .bigText("긴 본문 텍스트...")
            .setBigContentTitle("확장 시 제목")
            .setSummaryText("요약")
    )
```

#### BigPictureStyle
큰 이미지를 알림에 표시합니다.

```kotlin
NotificationCompat.Builder(context, CHANNEL_ID)
    .setStyle(
        NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null as Bitmap?)
    )
```

#### InboxStyle
여러 줄의 텍스트를 목록 형태로 표시합니다.

```kotlin
NotificationCompat.Builder(context, CHANNEL_ID)
    .setStyle(
        NotificationCompat.InboxStyle()
            .addLine("첫 번째 줄")
            .addLine("두 번째 줄")
            .addLine("세 번째 줄")
            .setBigContentTitle("받은 메시지")
            .setSummaryText("+3개 더")
    )
```

### 4. 진행 상태 알림

다운로드나 업로드 등 진행 상황을 표시합니다.

```kotlin
// 확정 진행률 (determinate)
NotificationCompat.Builder(context, CHANNEL_ID)
    .setProgress(100, currentProgress, false)

// 불확정 진행률 (indeterminate)
NotificationCompat.Builder(context, CHANNEL_ID)
    .setProgress(0, 0, true)

// 완료 시 진행 바 제거
NotificationCompat.Builder(context, CHANNEL_ID)
    .setProgress(0, 0, false)
```

---

## 주의사항

1. **채널 ID 일관성**: 동일한 채널 ID로 여러 번 생성해도 안전 (기존 설정 유지)
2. **사용자 닫기 존중**: 사용자가 닫은 알림은 불필요하게 다시 표시하지 않기
3. **권한 거부 처리**: 권한이 거부된 경우 적절한 fallback 제공
4. **알림 ID 관리**: 동일 ID로 notify() 시 기존 알림 업데이트

---

## 연습 문제

1. **Practice1**: 권한 요청 및 기본 알림
2. **Practice2**: BigTextStyle 알림
3. **Practice3**: 진행 상태 알림

---

## 참고 자료

- [Android 알림 공식 문서](https://developer.android.com/develop/ui/views/notifications)
- [알림 채널 만들기](https://developer.android.com/develop/ui/views/notifications/channels)
