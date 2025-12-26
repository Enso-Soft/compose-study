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
 * 올바른 코드 - SideEffect를 사용한 외부 상태 동기화
 *
 * SideEffect를 사용하면:
 * 1. 성공적인 Recomposition 후에만 실행 -> 안정적인 상태 보장
 * 2. Composition이 완료된 후 실행 -> 일관된 상태
 * 3. 명시적인 side effect 선언 -> 코드 가독성 향상
 * 4. 예측 가능한 동작 -> 디버깅 용이
 */
@Composable
fun SolutionScreen() {
    var userType by remember { mutableStateOf("Free") }
    var triggerRecompose by remember { mutableIntStateOf(0) }
    var recompositionCount by remember { mutableIntStateOf(0) }
    var syncCount by remember { mutableIntStateOf(0) }

    // 올바른 방식: SideEffect 사용
    // 성공적인 Recomposition 후에만 실행됩니다
    SideEffect {
        recompositionCount++
        ExternalAnalytics.currentUserType = userType
        ExternalAnalytics.updateCount++
        ExternalAnalytics.lastUpdateTime = System.currentTimeMillis()
        syncCount = ExternalAnalytics.updateCount
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("사용자 타입: $userType")
                Text("Recomposition 횟수: $recompositionCount")
                Text("Analytics 동기화 횟수: $syncCount")
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

        // 핵심 포인트 카드
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
                Text("- 성공적인 Recomposition 후에만 실행")
                Text("- Compose 상태를 외부 객체와 동기화")
                Text("- 동기적으로 실행 (코루틴 아님)")
                Text("- Key가 없음 (매번 실행)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 올바른 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드 패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun UserProfile(user: User) {
    val analytics = remember { Analytics() }

    SideEffect {
        // 성공적인 Recomposition 후 실행
        analytics.setUserProperty(
            "type", user.type
        )
    }

    Text("Hello, ${'$'}{user.name}")
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SideEffect vs 다른 API 비교
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "언제 SideEffect를 사용하나요?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- 외부 객체에 Compose 상태 동기화")
                Text("- Analytics/Logging")
                Text("- 3rd party SDK 상태 업데이트")
                Text("- 코루틴이 필요 없는 동기 작업")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "비동기 작업은 LaunchedEffect,\n정리가 필요하면 DisposableEffect 사용!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // triggerRecompose 사용 (unused warning 방지)
        if (triggerRecompose > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "(테스트 트리거: $triggerRecompose)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
