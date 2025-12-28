package com.example.side_effect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 외부 Analytics 시스템 (Compose가 관리하지 않음)
 *
 * 이 객체는 Compose 외부에 존재하며,
 * Compose 상태와 동기화가 필요합니다.
 */
object ExternalAnalytics {
    var currentUserType: String = ""
    var updateCount: Int = 0
    var lastUpdateTime: Long = 0

    fun reset() {
        currentUserType = ""
        updateCount = 0
        lastUpdateTime = 0
    }
}

/**
 * 문제가 있는 코드 - SideEffect 없이 외부 상태 동기화
 *
 * 이 코드의 문제점:
 * 1. Composable 본문에서 직접 외부 객체 수정
 * 2. Composition 중간에 실행되어 불안정한 상태 가능
 * 3. 실패한 Recomposition에서도 side effect 발생
 * 4. 예측 불가능한 실행 횟수
 */
@Composable
fun ProblemScreen() {
    var userType by remember { mutableStateOf("Free") }
    var triggerRecompose by remember { mutableIntStateOf(0) }
    var problemUpdateCount by remember { mutableIntStateOf(0) }

    // 문제: Composable 본문에서 직접 외부 상태 수정!
    // 주석을 해제하면 문제 상황을 확인할 수 있습니다.
    /*
    ExternalAnalytics.currentUserType = userType
    ExternalAnalytics.updateCount++
    ExternalAnalytics.lastUpdateTime = System.currentTimeMillis()
    problemUpdateCount = ExternalAnalytics.updateCount
    */

    // Recomposition 추적용 (참조 객체 패턴 - 무한 루프 방지)
    val recompositionRef = remember { object { var count = 0 } }
    var displayRecompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionRef.count++
    }
    LaunchedEffect(Unit) {
        displayRecompositionCount = recompositionRef.count
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("사용자 타입: $userType")
                Text("Recomposition 횟수: $displayRecompositionCount")
                Text("Analytics 업데이트 횟수: ${ExternalAnalytics.updateCount}")
                Text("Analytics에 저장된 타입: ${ExternalAnalytics.currentUserType}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 사용자 타입 변경 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { userType = "Free" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (userType == "Free")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline
                )
            ) {
                Text("Free")
            }
            Button(
                onClick = { userType = "Premium" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (userType == "Premium")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline
                )
            ) {
                Text("Premium")
            }
            Button(
                onClick = { userType = "Enterprise" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (userType == "Enterprise")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline
                )
            ) {
                Text("Enterprise")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 강제 Recomposition 버튼
        OutlinedButton(onClick = { triggerRecompose++ }) {
            Text("강제 Recomposition (테스트용)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 리셋 버튼
        TextButton(onClick = {
            ExternalAnalytics.reset()
            userType = "Free"
            triggerRecompose = 0
        }) {
            Text("초기화")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 설명 카드
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
                Text("1. Composable 본문에서 직접 외부 객체 수정")
                Text("2. Composition 중간에 실행되어 불안정")
                Text("3. 실패한 Recomposition에서도 실행됨")
                Text("4. 예측 불가능한 실행 횟수")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun UserProfile(user: User) {
    // Composable 본문에서 직접 호출
    analytics.setUserProperty("type", user.type)

    Text("Hello, ${'$'}{user.name}")
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Problem.kt 파일의 주석을 해제하면\n문제 상황을 확인할 수 있습니다!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )

        // triggerRecompose 사용 (unused warning 방지)
        if (triggerRecompose > 0) {
            Text(
                text = "(테스트 트리거: $triggerRecompose)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
