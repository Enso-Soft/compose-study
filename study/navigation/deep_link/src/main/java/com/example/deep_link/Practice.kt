package com.example.deep_link

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

// ========================================
// 연습 문제용 Route 정의
// ========================================

// 연습용 홈
@Serializable
object PracticeHome

// ========================================
// 연습 1: 기본 Deep Link 정의
// ========================================

/**
 * 연습 1: UserProfile Route
 * TODO: 이미 정의되어 있음. 코드를 분석하세요.
 */
@Serializable
data class UserProfile(val userId: String)

// ========================================
// 연습 2: 다중 파라미터 Deep Link
// ========================================

/**
 * 연습 2: OrderDetail Route
 * - orderId: 필수 파라미터
 * - status: 선택 파라미터 (기본값 "pending")
 *
 * TODO: 아래 주석을 해제하고 코드를 완성하세요.
 * 힌트: 선택 파라미터는 기본값을 설정하면 query parameter로 변환됩니다.
 */

// 정답 (학습용으로 미리 정의됨)
@Serializable
data class OrderDetail(
    val orderId: String,
    val status: String = "pending"
)

/*
// TODO: 직접 완성해보세요!
@Serializable
data class OrderDetail(
    val orderId: String,
    // TODO: status 파라미터 추가 (기본값: "pending")
)
*/

// ========================================
// 연습 3: PendingIntent로 알림 Deep Link
// ========================================

@Serializable
data class NotificationTarget(val notificationId: String)

// ========================================
// 연습 화면 구성
// ========================================

@Composable
fun PracticeScreen() {
    var selectedPractice by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedPractice == 0,
                onClick = { selectedPractice = 0 },
                label = { Text("연습 1") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("연습 2") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("연습 3") }
            )
        }

        when (selectedPractice) {
            0 -> Practice1Screen()
            1 -> Practice2Screen()
            2 -> Practice3Screen()
        }
    }
}

// ========================================
// 연습 1: 기본 Deep Link 정의
// ========================================

@Composable
fun Practice1Screen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PracticeHome
    ) {
        composable<PracticeHome> {
            Practice1HomeScreen(navController)
        }

        // TODO: UserProfile에 Deep Link를 연결하세요
        // 힌트: navDeepLink<UserProfile>(basePath = "https://myapp.com/user")
        composable<UserProfile>(
            deepLinks = listOf(
                // 정답:
                navDeepLink<UserProfile>(basePath = "https://myapp.com/user")
            )
        ) { backStackEntry ->
            val profile: UserProfile = backStackEntry.toRoute()
            Practice1ProfileScreen(
                userId = profile.userId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun Practice1HomeScreen(navController: NavHostController) {
    var inputUserId by remember { mutableStateOf("user123") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 기본 Deep Link",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 시나리오 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "시나리오",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("UserProfile 화면에 Deep Link를 연결하세요.")
                Text("URI: https://myapp.com/user/{userId}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputUserId,
            onValueChange = { inputUserId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(UserProfile(userId = inputUserId))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("프로필로 이동 (Type-Safe)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                // Deep Link 시뮬레이션
                val uri = "https://myapp.com/user/$inputUserId".toUri()
                val request = androidx.navigation.NavDeepLinkRequest.Builder
                    .fromUri(uri)
                    .build()
                navController.navigate(request)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Deep Link 시뮬레이션")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
composable<UserProfile>(
    deepLinks = listOf(
        navDeepLink<UserProfile>(
            basePath = "https://myapp.com/user"
        )
    )
) { ... }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 정답 Card (접이식)
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
@Serializable
data class UserProfile(val userId: String)

composable<UserProfile>(
    deepLinks = listOf(
        navDeepLink<UserProfile>(
            basePath = "https://myapp.com/user"
        )
    )
) { backStackEntry ->
    val profile = backStackEntry.toRoute<UserProfile>()
    ProfileScreen(userId = profile.userId)
}

// 생성되는 URI 패턴:
// https://myapp.com/user/{userId}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1ProfileScreen(
    userId: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "프로필 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("User ID: $userId")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deep Link로 접근 성공!",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ========================================
// 연습 2: 다중 파라미터 Deep Link
// ========================================

@Composable
fun Practice2Screen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PracticeHome
    ) {
        composable<PracticeHome> {
            Practice2HomeScreen(navController)
        }

        // TODO: OrderDetail에 Deep Link를 연결하세요
        // URI: myapp://order/{orderId}?status={status}
        composable<OrderDetail>(
            deepLinks = listOf(
                // 정답:
                navDeepLink<OrderDetail>(basePath = "myapp://order")
            )
        ) { backStackEntry ->
            val order: OrderDetail = backStackEntry.toRoute()
            Practice2OrderScreen(
                orderId = order.orderId,
                status = order.status,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun Practice2HomeScreen(navController: NavHostController) {
    var inputOrderId by remember { mutableStateOf("order456") }
    var inputStatus by remember { mutableStateOf("shipped") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: 다중 파라미터 Deep Link",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 시나리오 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "시나리오",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("OrderDetail 화면에 필수/선택 파라미터 Deep Link를 연결하세요.")
                Text("- orderId: 필수 (path)")
                Text("- status: 선택, 기본값 pending (query)")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputOrderId,
            onValueChange = { inputOrderId = it },
            label = { Text("Order ID (필수)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputStatus,
            onValueChange = { inputStatus = it },
            label = { Text("Status (선택)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(
                    OrderDetail(orderId = inputOrderId, status = inputStatus)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("주문 상세로 이동")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    // status 포함
                    val uri = "myapp://order/$inputOrderId?status=$inputStatus".toUri()
                    val request = androidx.navigation.NavDeepLinkRequest.Builder
                        .fromUri(uri)
                        .build()
                    navController.navigate(request)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("status 포함")
            }

            OutlinedButton(
                onClick = {
                    // status 없이 (기본값 사용)
                    val uri = "myapp://order/$inputOrderId".toUri()
                    val request = androidx.navigation.NavDeepLinkRequest.Builder
                        .fromUri(uri)
                        .build()
                    navController.navigate(request)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("기본값 사용")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("필수 파라미터: path parameter /{orderId}")
                Text("선택 파라미터: query parameter ?status={status}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Serializable
data class OrderDetail(
    val orderId: String,        // 필수 → path
    val status: String = "pending"  // 선택 → query
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2OrderScreen(
    orderId: String,
    status: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("주문 상세") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "주문 상세 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Order ID: $orderId (필수)")
                    Text("Status: $status (선택)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "다중 파라미터 Deep Link 성공!",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ========================================
// 연습 3: PendingIntent로 알림 Deep Link
// ========================================

@Composable
fun Practice3Screen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PracticeHome
    ) {
        composable<PracticeHome> {
            Practice3HomeScreen(navController)
        }

        composable<NotificationTarget>(
            deepLinks = listOf(
                navDeepLink<NotificationTarget>(basePath = "myapp://notification")
            )
        ) { backStackEntry ->
            val target: NotificationTarget = backStackEntry.toRoute()
            Practice3NotificationScreen(
                notificationId = target.notificationId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun Practice3HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    var inputNotificationId by remember { mutableStateOf("notif001") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: PendingIntent Deep Link",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 시나리오 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "시나리오",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("알림 클릭 시 특정 화면으로 이동하는")
                Text("PendingIntent를 생성하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("(실제 알림 대신 시뮬레이션으로 테스트)")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputNotificationId,
            onValueChange = { inputNotificationId = it },
            label = { Text("Notification ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Deep Link 시뮬레이션 (PendingIntent 대신)
                val uri = "myapp://notification/$inputNotificationId".toUri()
                val request = androidx.navigation.NavDeepLinkRequest.Builder
                    .fromUri(uri)
                    .build()
                navController.navigate(request)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("알림 탭 시뮬레이션")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // PendingIntent 생성 코드 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "PendingIntent 생성 방법",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
fun createDeepLinkPendingIntent(
    context: Context,
    notificationId: String
): PendingIntent? {
    val deepLinkIntent = Intent(
        Intent.ACTION_VIEW,
        "myapp://notification/${"$"}notificationId".toUri(),
        context,
        MainActivity::class.java
    )

    return TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(deepLinkIntent)
        getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or
                PendingIntent.FLAG_IMMUTABLE
        )
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Intent.ACTION_VIEW 사용")
                Text("2. URI를 Intent의 data로 설정")
                Text("3. TaskStackBuilder로 백스택 구성")
                Text("4. FLAG_IMMUTABLE 필수 (API 31+)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 정답
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "상세 설명 숨기기" else "상세 설명 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "알림에 PendingIntent 적용",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
val pendingIntent = createDeepLinkPendingIntent(
    context,
    "notif001"
)

val notification = NotificationCompat.Builder(
    context,
    CHANNEL_ID
)
    .setContentTitle("새 알림")
    .setContentText("탭하여 상세 보기")
    .setContentIntent(pendingIntent)
    .setAutoCancel(true)
    .build()
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3NotificationScreen(
    notificationId: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("알림 상세") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "알림 상세 화면",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Notification ID: $notificationId")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("알림에서 Deep Link로 진입!")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PendingIntent Deep Link 성공!",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ========================================
// 헬퍼 함수: PendingIntent 생성 (참고용)
// ========================================

/**
 * Deep Link용 PendingIntent 생성
 * 알림 클릭 시 특정 화면으로 이동
 */
fun createDeepLinkPendingIntent(
    context: Context,
    notificationId: String
): PendingIntent? {
    val deepLinkIntent = Intent(
        Intent.ACTION_VIEW,
        "myapp://notification/$notificationId".toUri(),
        context,
        MainActivity::class.java
    )

    return TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(deepLinkIntent)
        getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
