package com.example.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * 문제가 있는 코드 - 문자열 기반 네비게이션
 *
 * 이 코드는 다음 문제점들을 보여줍니다:
 * 1. Route 문자열 오타 - 컴파일은 되지만 런타임에 오류
 * 2. 인자명 불일치 - "userId" vs "user_id"
 * 3. 타입 안전성 없음 - Int/String 혼용 가능
 * 4. 유지보수 어려움 - 문자열이 여러 곳에 흩어짐
 */
@Composable
fun ProblemScreen() {
    val navController = rememberNavController()

    // 문제: 문자열 기반 Route 정의
    NavHost(
        navController = navController,
        startDestination = "problem_home"  // 문자열로 정의
    ) {
        composable("problem_home") {
            ProblemHomeScreen(navController)
        }

        // 문제: Route 문자열과 인자 정의가 분리됨
        composable("problem_profile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProblemProfileScreen(
                userId = userId,
                onBack = { navController.popBackStack() }
            )
        }

        composable("problem_settings") {
            ProblemSettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun ProblemHomeScreen(navController: NavHostController) {
    var inputUserId by remember { mutableStateOf("user123") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem: 문자열 기반 Navigation",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 오타가 컴파일 시 발견되지 않음")
                Text("2. 인자명 불일치 시 런타임 오류")
                Text("3. 타입 안전성이 없음")
                Text("4. 리팩토링이 어려움")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputUserId,
            onValueChange = { inputUserId = it },
            label = { Text("User ID 입력") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 시연: 문자열 기반 네비게이션
        Button(
            onClick = {
                // 문자열로 Route 구성 - 오타 가능성!
                navController.navigate("problem_profile/$inputUserId")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("프로필로 이동 (문자열 기반)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 문제 시연: 오타가 있는 Route
        OutlinedButton(
            onClick = {
                // 오타! "proflie" vs "profile"
                // 컴파일은 되지만 런타임에 화면이 안 보임
                navController.navigate("problem_proflie/$inputUserId")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("오타 있는 Route 테스트 (작동 안 함)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate("problem_settings")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("설정으로 이동")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 코드 예시 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드 예시",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 문자열 기반 Route
navController.navigate("profile/${'$'}userId")

// 오타가 있어도 컴파일됨!
navController.navigate("proflie/${'$'}userId")

// 타입이 달라도 컴파일됨!
navController.navigate("profile/${'$'}{userId.toInt()}")
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemProfileScreen(
    userId: String?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로필 (문자열 기반)") },
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
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 문제: nullable String - 인자가 제대로 전달되지 않을 수 있음
            Text(
                text = "User ID: ${userId ?: "없음 (오류!)"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("문제점:")
                    Text("- userId가 nullable (String?)")
                    Text("- 인자명이 틀리면 null")
                    Text("- 타입 검증 없음")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemSettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정 (문자열 기반)") },
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
                text = "설정 화면",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("인자 없는 화면도 문자열로 정의해야 함")
        }
    }
}
