package com.example.disposable_effect

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay

/**
 * 연습 문제 1: 라이프사이클 관찰자
 *
 * 요구사항:
 * - LifecycleEventObserver를 등록하여 앱의 라이프사이클 이벤트를 추적
 * - ON_START, ON_STOP 이벤트를 로그에 기록
 * - 컴포넌트가 사라질 때 Observer 해제
 *
 * TODO: DisposableEffect를 사용해서 구현하세요!
 */
@Composable
fun Practice1_LifecycleScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current
    var lifecycleLog by remember { mutableStateOf<List<String>>(emptyList()) }

    // TODO: 여기에 DisposableEffect를 추가하세요
    // 힌트: lifecycleOwner를 key로 사용
    // 힌트: LifecycleEventObserver를 생성하고 등록
    // 힌트: onDispose에서 removeObserver 호출

    /*
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            val timestamp = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date())
            when (event) {
                Lifecycle.Event.ON_START -> {
                    lifecycleLog = lifecycleLog + "[$timestamp] ON_START - 화면 시작"
                }
                Lifecycle.Event.ON_STOP -> {
                    lifecycleLog = lifecycleLog + "[$timestamp] ON_STOP - 화면 정지"
                }
                Lifecycle.Event.ON_RESUME -> {
                    lifecycleLog = lifecycleLog + "[$timestamp] ON_RESUME - 화면 활성화"
                }
                Lifecycle.Event.ON_PAUSE -> {
                    lifecycleLog = lifecycleLog + "[$timestamp] ON_PAUSE - 화면 비활성화"
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 라이프사이클 관찰자",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: DisposableEffect로 LifecycleEventObserver 구현",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                Spacer(modifier = Modifier.height(4.dp))
                Text("• LifecycleEventObserver 생성")
                Text("• lifecycle.addObserver()로 등록")
                Text("• onDispose에서 removeObserver()")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("앱을 백그라운드로 보냈다가 다시 오면 이벤트 로그가 추가됩니다:")

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { lifecycleLog = emptyList() }) {
            Text("로그 초기화")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (lifecycleLog.isEmpty()) {
            Text(
                text = "로그 없음 (DisposableEffect 구현 필요)",
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            lifecycleLog.takeLast(10).forEach { log ->
                Text(log, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/**
 * 연습 문제 2: 백그라운드 감지 타이머
 *
 * 요구사항:
 * - 타이머가 1초마다 증가
 * - 앱이 백그라운드로 가면 타이머 일시정지
 * - 앱이 포그라운드로 돌아오면 타이머 재개
 *
 * TODO: DisposableEffect와 LaunchedEffect를 조합해서 구현하세요!
 */
@Composable
fun Practice2_BackgroundTimerScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current
    var seconds by remember { mutableIntStateOf(0) }
    var isInForeground by remember { mutableStateOf(true) }

    // TODO: DisposableEffect로 라이프사이클 감지
    // 힌트: ON_RESUME -> isInForeground = true
    // 힌트: ON_PAUSE -> isInForeground = false

    /*
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> isInForeground = true
                Lifecycle.Event.ON_PAUSE -> isInForeground = false
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    */

    // 포그라운드일 때만 타이머 실행
    LaunchedEffect(isInForeground) {
        while (isInForeground) {
            delay(1000)
            seconds++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 2: 백그라운드 감지 타이머",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: DisposableEffect로 라이프사이클 감지",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = formatTime(seconds),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isInForeground)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = if (isInForeground) "▶ 실행 중" else "⏸ 일시정지",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { seconds = 0 }) {
            Text("리셋")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "앱을 백그라운드로 보내면 타이머가 일시정지됩니다",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

/**
 * 연습 문제 3: 키 변경에 따른 리소스 관리
 *
 * 요구사항:
 * - userId가 변경될 때마다 해당 사용자의 이벤트 구독 시작
 * - 다른 사용자로 변경되면 기존 구독 해제 후 새 구독 시작
 * - 화면을 떠나면 구독 완전 해제
 *
 * TODO: DisposableEffect의 key 기능을 활용해서 구현하세요!
 */

// 가상의 사용자 이벤트 시스템
object UserEventSystem {
    private val subscriptions = mutableMapOf<String, (String) -> Unit>()

    fun subscribe(userId: String, callback: (String) -> Unit) {
        subscriptions[userId] = callback
        println("[$userId] 구독 시작. 현재 구독: ${subscriptions.keys}")
    }

    fun unsubscribe(userId: String) {
        subscriptions.remove(userId)
        println("[$userId] 구독 해제. 현재 구독: ${subscriptions.keys}")
    }

    fun emit(userId: String, event: String) {
        subscriptions[userId]?.invoke(event)
    }

    fun getSubscriptions() = subscriptions.keys.toList()
}

@Composable
fun Practice3_UserSubscriptionScreen() {
    var selectedUser by remember { mutableStateOf("user1") }
    var eventLog by remember { mutableStateOf<List<String>>(emptyList()) }
    var subscriptions by remember { mutableStateOf<List<String>>(emptyList()) }

    val users = listOf("user1", "user2", "user3")

    // 구독 상태 업데이트
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            subscriptions = UserEventSystem.getSubscriptions()
        }
    }

    // TODO: 여기에 DisposableEffect를 추가하세요
    // 힌트: selectedUser를 key로 사용
    // 힌트: 사용자가 변경되면 기존 구독 해제 -> 새 구독 시작

    /*
    DisposableEffect(selectedUser) {
        UserEventSystem.subscribe(selectedUser) { event ->
            eventLog = eventLog + "[$selectedUser] $event"
        }

        onDispose {
            UserEventSystem.unsubscribe(selectedUser)
        }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: 사용자별 이벤트 구독",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: DisposableEffect의 key 기능 활용",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 구독 상태
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("현재 구독 중: ${subscriptions.joinToString(", ").ifEmpty { "없음" }}")
                Text("선택된 사용자: $selectedUser")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 사용자 선택 버튼
        Text("사용자 선택:")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            users.forEach { user ->
                FilterChip(
                    selected = selectedUser == user,
                    onClick = { selectedUser = user },
                    label = { Text(user) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 이벤트 발생 버튼
        Button(
            onClick = {
                UserEventSystem.emit(selectedUser, "이벤트 ${System.currentTimeMillis()}")
            }
        ) {
            Text("$selectedUser 에게 이벤트 발생")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { eventLog = emptyList() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("로그 초기화")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("핵심 포인트", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• DisposableEffect(selectedUser) 사용")
                Text("• 사용자 변경 시: onDispose → 새 effect 실행")
                Text("• 한 번에 하나의 구독만 유지")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 이벤트 로그
        Text("이벤트 로그:", style = MaterialTheme.typography.labelMedium)
        if (eventLog.isEmpty()) {
            Text(
                text = "로그 없음 (DisposableEffect 구현 필요)",
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            eventLog.takeLast(5).forEach { log ->
                Text(log, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
