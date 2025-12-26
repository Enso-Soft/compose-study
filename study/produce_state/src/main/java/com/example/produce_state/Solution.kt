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
 * UI 상태를 표현하는 sealed class
 * - Loading, Success, Error 3가지 상태를 하나로 통합
 * - exhaustive when 검사 가능
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

/**
 * 올바른 코드 - produceState 사용
 *
 * produceState의 장점:
 * 1. 단일 sealed class로 모든 상태 표현
 * 2. 상태 선언과 비동기 로직이 하나로 통합
 * 3. key 변경 시 자동 재시작
 * 4. 코루틴 자동 취소 (메모리 누수 방지)
 */
@Composable
fun SolutionScreen() {
    var userId by remember { mutableStateOf("user123") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution: produceState",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 데모
        SolutionUserProfile(userId = userId)

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

        // 해결책 설명 카드
        SolutionExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 추가 예제: 타이머
        TimerExample()
    }
}

/**
 * produceState를 사용한 사용자 프로필 화면
 * - 단일 UiState로 모든 상태 관리
 * - 상태 선언과 로딩 로직이 하나로 통합
 */
@Composable
fun SolutionUserProfile(userId: String) {
    var recompositionCount by remember { mutableIntStateOf(0) }

    // produceState: 비동기 결과를 State로 변환
    val userState by produceState<UiState<User>>(
        initialValue = UiState.Loading,
        key1 = userId  // userId 변경 시 자동 재시작
    ) {
        // 초기 상태는 이미 Loading으로 설정됨
        delay(1500) // 네트워크 시뮬레이션

        value = if (userId == "error") {
            UiState.Error("사용자를 찾을 수 없습니다")
        } else {
            UiState.Success(
                User(
                    id = userId,
                    name = "사용자 ${userId.takeLast(3)}",
                    email = "$userId@example.com"
                )
            )
        }
    }

    // Recomposition 추적
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "produceState + sealed class",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Recomposition: ${recompositionCount}회")
            Text("현재 userId: $userId")

            Spacer(modifier = Modifier.height(16.dp))

            // 깔끔한 when 표현식
            when (val state = userState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("로딩 중...")
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,  // 스마트 캐스트!
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is UiState.Success -> {
                    Text("이름: ${state.data.name}")  // 스마트 캐스트!
                    Text("이메일: ${state.data.email}")
                }
            }
        }
    }
}

@Composable
fun SolutionExplanationCard() {
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

            Text("1. sealed class로 Loading/Success/Error 하나로 통합")
            Text("2. produceState가 상태 선언 + 비동기 로직을 하나로")
            Text("3. when 표현식으로 스마트 캐스트 (!! 불필요)")
            Text("4. key1 = userId로 자동 재시작")

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
val userState by produceState<UiState<User>>(
    initialValue = UiState.Loading,
    key1 = userId
) {
    value = try {
        UiState.Success(api.fetchUser(userId))
    } catch (e: Exception) {
        UiState.Error(e.message ?: "Error")
    }
}

when (val state = userState) {
    is UiState.Loading -> LoadingIndicator()
    is UiState.Success -> UserCard(state.data)
    is UiState.Error -> ErrorMessage(state.message)
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
 * 타이머 예제 - produceState의 또 다른 활용
 */
@Composable
fun TimerExample() {
    val seconds by produceState(initialValue = 0) {
        while (true) {
            delay(1000)
            value += 1
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "타이머 예제",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${seconds}초",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "produceState로 1초마다 자동 증가",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
