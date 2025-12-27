package com.example.launched_effect

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 올바른 코드 - LaunchedEffect 사용
 *
 * LaunchedEffect를 사용하면:
 * 1. key(userId)가 같으면 재실행하지 않음 -> 무한 루프 방지
 * 2. key가 변경되면 기존 코루틴 취소 후 새로 실행 -> 최신 데이터만 로드
 * 3. Composable이 사라지면 자동 취소 -> 메모리 누수 방지
 */
@Composable
fun SolutionScreen(initialUserId: String = "user123") {
    var userId by remember { mutableStateOf(initialUserId) }
    var userData by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var fetchCount by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적 (참조 객체 패턴 - 무한 루프 방지)
    val recompositionRef = remember { object { var count = 0 } }
    var displayRecompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionRef.count++
    }

    // LaunchedEffect: userId가 변경될 때만 실행
    LaunchedEffect(userId) {
        fetchCount++
        displayRecompositionCount = recompositionRef.count
        println("Fetching data for $userId (fetch count: $fetchCount)")
        isLoading = true
        delay(1000)
        userData = "User: $userId (loaded at ${System.currentTimeMillis()})"
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recomposition 횟수: $displayRecompositionCount")
                Text("API 호출 횟수: $fetchCount")
                Text("현재 userId: $userId")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 로딩 상태 또는 데이터 표시
        if (isLoading) {
            CircularProgressIndicator()
            Text("데이터 로딩 중...")
        } else {
            Text(
                text = userData ?: "데이터 없음",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // userId 변경 버튼들
        Text("userId를 변경하면 LaunchedEffect가 다시 실행됩니다:")

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { userId = "user123" }) {
                Text("User 123")
            }
            Button(onClick = { userId = "user456" }) {
                Text("User 456")
            }
            Button(onClick = { userId = "user789" }) {
                Text("User 789")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                Text("- 같은 userId 버튼을 눌러도 API 재호출 없음")
                Text("- 다른 userId로 변경 시에만 API 호출")
                Text("- Recomposition은 여러번 일어나도 OK")
            }
        }
    }
}
