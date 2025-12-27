package com.example.launched_effect

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 문제가 있는 코드 - LaunchedEffect 없이 코루틴 실행
 *
 * 이 코드를 실행하면 다음 문제가 발생합니다:
 * 1. 무한 루프: API 호출 -> state 변경 -> Recomposition -> 다시 API 호출
 * 2. 불필요한 네트워크 요청 반복
 * 3. 카운터가 무한히 증가 (Recomposition 횟수 표시)
 */
@Composable
fun ProblemScreen(userId: String = "user123") {
    val scope = rememberCoroutineScope()
    var userData by remember { mutableStateOf<String?>(null) }

    // Recomposition 횟수 추적 (참조 객체 패턴 - 무한 루프 방지)
    val recompositionRef = remember { object { var count = 0 } }
    var displayRecompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionRef.count++
    }
    LaunchedEffect(Unit) {
        displayRecompositionCount = recompositionRef.count
    }

    // 문제: Recomposition마다 매번 코루틴 실행!
    // 주석을 해제하면 무한 루프 발생
    /*
    scope.launch {
        println("Fetching data... (count: $displayRecompositionCount)")
        delay(500) // 네트워크 요청 시뮬레이션
        userData = "User: $userId (loaded at ${System.currentTimeMillis()})"
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
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
                Text("Recomposition 횟수: $displayRecompositionCount")
                Spacer(modifier = Modifier.height(8.dp))
                Text(userData ?: "데이터 로딩 중...")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Problem.kt 파일의 주석을 해제하면\n무한 루프가 발생합니다!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
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
                    text = "왜 문제인가?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. scope.launch가 Composable 본문에서 직접 호출됨")
                Text("2. API 호출 -> state 변경 -> Recomposition 발생")
                Text("3. Recomposition -> 다시 scope.launch 실행")
                Text("4. 무한 반복!")
            }
        }
    }
}

/**
 * 가짜 API 호출 함수
 */
suspend fun fetchUserDataFake(userId: String): String {
    delay(1000)
    return "User Data for $userId"
}
