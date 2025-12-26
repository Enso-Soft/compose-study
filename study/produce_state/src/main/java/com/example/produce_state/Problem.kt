package com.example.produce_state

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 문제가 있는 코드 - LaunchedEffect + 다중 상태 관리
 *
 * 이 코드의 문제점:
 * 1. 3개의 상태 변수(user, isLoading, errorMessage)를 개별 관리해야 함
 * 2. 상태 동기화 실수 가능 (isLoading = false 빠뜨리면?)
 * 3. null 처리가 복잡함 (!!연산자 또는 복잡한 when 조건)
 * 4. 비슷한 패턴이 화면마다 반복됨 (보일러플레이트)
 */
@Composable
fun ProblemScreen() {
    var userId by remember { mutableStateOf("user123") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem: 다중 상태 관리",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 데모
        ProblemUserProfile(userId = userId)

        Spacer(modifier = Modifier.height(16.dp))

        // userId 변경 버튼
        Text("userId 변경하기:")
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { userId = "user123" }) {
                Text("User 123")
            }
            Button(onClick = { userId = "user456" }) {
                Text("User 456")
            }
            Button(onClick = { userId = "error" }) {
                Text("Error Case")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 설명 카드
        ProblemExplanationCard()
    }
}

/**
 * 문제가 있는 사용자 프로필 화면
 * - 3개의 개별 상태 변수 사용
 * - LaunchedEffect 내에서 모든 상태 수동 관리
 */
@Composable
fun ProblemUserProfile(userId: String) {
    // 문제: 3개의 상태를 개별 관리해야 함
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var recompositionCount by remember { mutableIntStateOf(0) }

    // LaunchedEffect로 데이터 로딩
    LaunchedEffect(userId) {
        // 모든 상태를 수동으로 초기화해야 함
        isLoading = true
        errorMessage = null
        user = null

        try {
            delay(1500) // 네트워크 시뮬레이션

            if (userId == "error") {
                throw Exception("사용자를 찾을 수 없습니다")
            }

            user = User(
                id = userId,
                name = "사용자 ${userId.takeLast(3)}",
                email = "$userId@example.com"
            )
            isLoading = false  // 이걸 빠뜨리면 영원히 로딩!
        } catch (e: Exception) {
            errorMessage = e.message
            isLoading = false  // 여기도 빠뜨리면 안 됨!
        }
    }

    // Recomposition 추적
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LaunchedEffect + 다중 상태",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Recomposition: ${recompositionCount}회")
            Text("현재 userId: $userId")

            Spacer(modifier = Modifier.height(16.dp))

            // 복잡한 상태 조합으로 UI 결정
            when {
                isLoading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("로딩 중...")
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,  // !! 사용 필요
                        color = MaterialTheme.colorScheme.error
                    )
                }
                user != null -> {
                    Text("이름: ${user!!.name}")  // !! 사용 필요
                    Text("이메일: ${user!!.email}")
                }
            }
        }
    }
}

@Composable
fun ProblemExplanationCard() {
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

            Text("1. 3개의 상태 변수 (user, isLoading, errorMessage) 개별 관리")
            Text("2. isLoading = false를 빠뜨리면 영원히 로딩 상태")
            Text("3. null 처리를 위해 !! 연산자 사용 필요")
            Text("4. 비슷한 보일러플레이트가 모든 화면에서 반복")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "코드 예시:",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
var user by remember { mutableStateOf<User?>(null) }
var isLoading by remember { mutableStateOf(true) }
var errorMessage by remember { mutableStateOf<String?>(null) }

LaunchedEffect(userId) {
    isLoading = true
    errorMessage = null
    try {
        user = api.fetchUser(userId)
        isLoading = false  // 빠뜨리면 버그!
    } catch (e: Exception) {
        errorMessage = e.message
        isLoading = false  // 여기도!
    }
}
                    """.trimIndent(),
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 사용자 데이터 클래스
 */
data class User(
    val id: String,
    val name: String,
    val email: String
)
