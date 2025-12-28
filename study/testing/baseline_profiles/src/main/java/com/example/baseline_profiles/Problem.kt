package com.example.baseline_profiles

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 문제 상황: Baseline Profiles 없이 앱을 배포할 때 발생하는 성능 문제
 *
 * 이 화면에서는 Baseline Profiles 없이 앱을 배포했을 때
 * 사용자가 경험하는 성능 문제를 시뮬레이션하고 설명합니다.
 *
 * 실제 문제:
 * 1. 콜드 스타트 시 JIT 컴파일로 인한 느린 시작
 * 2. 첫 상호작용 시 프레임 드롭 (Jank)
 * 3. Compose 라이브러리 로드 오버헤드
 * 4. Cloud Profiles 없는 환경에서 성능 저하
 */
@Composable
fun ProblemScreen() {
    var showSimulation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 제목
        Text(
            text = "Problem: Baseline Profiles 없는 앱",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 설명 카드
        ProblemExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        // ART 동작 방식 시각화
        ARTCompilationFlowCard()

        Spacer(modifier = Modifier.height(16.dp))

        // JIT 컴파일 시뮬레이션
        Button(
            onClick = { showSimulation = !showSimulation },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showSimulation) "시뮬레이션 숨기기" else "JIT 컴파일 시뮬레이션 보기")
        }

        if (showSimulation) {
            Spacer(modifier = Modifier.height(16.dp))
            JITCompilationSimulation()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compose 특유의 문제
        ComposeSpecificProblemCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 성능 비교 표
        PerformanceComparisonCard()
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Baseline Profiles 없이 배포하면?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            val problems = listOf(
                "1. 앱 첫 실행 시 해석(Interpretation) 모드로 코드 실행" to
                        "바이트코드를 한 줄씩 해석 -> 매우 느림",
                "2. JIT(Just-in-Time) 컴파일러가 동시에 동작" to
                        "CPU 리소스 경쟁 -> UI 버벅거림",
                "3. 최적화까지 며칠이 걸림" to
                        "사용자의 첫 경험이 최악의 성능!",
                "4. 새 버전 업데이트마다 반복" to
                        "매번 처음부터 프로필 수집 필요"
            )

            problems.forEach { (title, description) ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ARTCompilationFlowCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ART 컴파일 과정 (Baseline Profiles 없을 때)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            val steps = listOf(
                "앱 설치" to Color(0xFF4CAF50),
                "첫 번째 실행 (해석 모드 - 느림)" to Color(0xFFF44336),
                "JIT 컴파일 시작 (CPU 경쟁)" to Color(0xFFFF9800),
                "프로필 데이터 수집 (며칠간)" to Color(0xFFFF9800),
                "백그라운드 AOT 컴파일" to Color(0xFF2196F3),
                "최적화 완료 (며칠 후)" to Color(0xFF4CAF50)
            )

            steps.forEachIndexed { index, (step, color) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color, shape = MaterialTheme.shapes.small),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 11.dp)
                            .width(2.dp)
                            .height(16.dp)
                            .background(MaterialTheme.colorScheme.outline)
                    )
                }
            }
        }
    }
}

@Composable
private fun JITCompilationSimulation() {
    var simulationPhase by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            // 시뮬레이션 단계 진행
            for (phase in 1..5) {
                delay(1500)
                simulationPhase = phase
            }
            isRunning = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "JIT 컴파일 시뮬레이션",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 진행 상태 표시
            val phases = listOf(
                "대기 중...",
                "앱 시작 중... (해석 모드)",
                "JIT 컴파일러 동작 중... (CPU 경쟁!)",
                "자주 사용되는 메서드 컴파일 중...",
                "프로필 수집 중...",
                "완료 (하지만 사용자는 이미 떠났습니다)"
            )

            val currentPhase = phases.getOrElse(simulationPhase) { phases.last() }
            val progress by animateFloatAsState(
                targetValue = simulationPhase / 5f,
                animationSpec = tween(500),
                label = "progress"
            )

            Text(
                text = currentPhase,
                style = MaterialTheme.typography.bodyMedium,
                color = when (simulationPhase) {
                    1, 2 -> MaterialTheme.colorScheme.error
                    3, 4 -> MaterialTheme.colorScheme.tertiary
                    5 -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSecondaryContainer
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = when (simulationPhase) {
                    1, 2 -> MaterialTheme.colorScheme.error
                    3, 4 -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.primary
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    simulationPhase = 0
                    isRunning = true
                },
                enabled = !isRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isRunning) "시뮬레이션 진행 중..." else "시뮬레이션 시작")
            }

            if (simulationPhase == 5) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "사용자는 느린 앱에 실망하여 다른 앱을 사용합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ComposeSpecificProblemCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Compose 앱의 특별한 문제",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Compose는 플랫폼이 아닌 라이브러리로 배포됩니다:",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 비교 표
            Column {
                ComparisonRow(
                    type = "Android Platform Code",
                    status = "이미 AOT 컴파일됨",
                    isOptimized = true
                )
                ComparisonRow(
                    type = "Compose Library",
                    status = "JIT 컴파일 필요",
                    isOptimized = false
                )
                ComparisonRow(
                    type = "앱 고유 코드",
                    status = "JIT 컴파일 필요",
                    isOptimized = false
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Compose 1.6.0+는 기본 프로필을 포함하지만, 앱 고유 코드 경로는 직접 생성 필요!",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun ComparisonRow(
    type: String,
    status: String,
    isOptimized: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = if (isOptimized) {
                Color(0xFF4CAF50)
            } else {
                MaterialTheme.colorScheme.error
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PerformanceComparisonCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "실제 성능 측정 결과 (Macrobenchmark)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 벤치마크 결과 시뮬레이션
            Column {
                BenchmarkResultRow(
                    label = "Baseline Profile 없음",
                    time = "324ms",
                    isBaseline = false
                )
                BenchmarkResultRow(
                    label = "Baseline Profile 적용",
                    time = "229ms",
                    isBaseline = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "약 30% 성능 향상!",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "* 실제 결과는 앱 복잡도와 기기에 따라 다를 수 있습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun BenchmarkResultRow(
    label: String,
    time: String,
    isBaseline: Boolean
) {
    val progress by animateFloatAsState(
        targetValue = if (isBaseline) 0.71f else 1f,
        animationSpec = tween(1000),
        label = "benchmark_progress"
    )

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isBaseline) {
                    Color(0xFF4CAF50)
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = if (isBaseline) {
                Color(0xFF4CAF50)
            } else {
                MaterialTheme.colorScheme.error
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
