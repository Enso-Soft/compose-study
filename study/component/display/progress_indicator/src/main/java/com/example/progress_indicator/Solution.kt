package com.example.progress_indicator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Solution: Progress Indicator로 로딩 상태 표시
 *
 * CircularProgressIndicator와 LinearProgressIndicator를 사용하여
 * 사용자에게 로딩 중임을 알려줍니다.
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명
        SolutionExplanationCard()

        // 1. Indeterminate (무한 로딩) 예제
        IndeterminateDemo()

        // 2. Determinate (진행률 표시) 예제
        DeterminateDemo()

        // 3. 커스터마이징 예제
        CustomizationDemo()

        // 4. 조건부 표시 패턴 (실제 사용 예)
        ConditionalDisplayDemo()
    }
}

@Composable
private fun SolutionExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "해결책: Progress Indicator",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "로딩 중에 Progress Indicator를 표시하면 사용자가 \"아, 지금 데이터를 불러오고 있구나\"라고 인식합니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun IndeterminateDemo() {
    var showCircular by remember { mutableStateOf(false) }
    var showLinear by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "1. Indeterminate (무한 로딩)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "진행률을 알 수 없을 때 사용합니다. (예: API 호출)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 원형 Progress Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CircularProgressIndicator()",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Button(
                        onClick = {
                            showCircular = true
                            scope.launch {
                                delay(3000)
                                showCircular = false
                            }
                        },
                        enabled = !showCircular
                    ) {
                        Text("3초간 표시")
                    }
                }

                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (showCircular) {
                        CircularProgressIndicator()
                    }
                }
            }

            HorizontalDivider()

            // 선형 Progress Indicator
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "LinearProgressIndicator()",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )

                Button(
                    onClick = {
                        showLinear = true
                        scope.launch {
                            delay(3000)
                            showLinear = false
                        }
                    },
                    enabled = !showLinear
                ) {
                    Text("3초간 표시")
                }

                if (showLinear) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // 높이 유지를 위한 Spacer
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun DeterminateDemo() {
    var progress by remember { mutableFloatStateOf(0f) }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "2. Determinate (진행률 표시)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "진행률을 알 때 사용합니다. (예: 파일 다운로드)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
                        // progress 값: 0.0 ~ 1.0
                        CircularProgressIndicator(
                            progress = { 0.7f }  // 70%
                        )
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 원형 진행률
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        progress = { progress }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (!isRunning) {
                                isRunning = true
                                progress = 0f
                                scope.launch {
                                    while (progress < 1f) {
                                        progress += 0.01f
                                        delay(30)
                                    }
                                    progress = 1f
                                    isRunning = false
                                }
                            }
                        },
                        enabled = !isRunning
                    ) {
                        Text("진행 시작")
                    }

                    Button(
                        onClick = { progress = 0f },
                        enabled = !isRunning && progress > 0f
                    ) {
                        Text("리셋")
                    }
                }
            }

            HorizontalDivider()

            // 선형 진행률
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "진행률: ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun CustomizationDemo() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "3. 커스터마이징",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "색상, 크기, 두께를 변경할 수 있습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 기본
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("기본", style = MaterialTheme.typography.bodySmall)
                }

                // 색상 변경
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF4CAF50), // 초록색
                        trackColor = Color(0xFFE8F5E9)
                    )
                    Text("색상", style = MaterialTheme.typography.bodySmall)
                }

                // 크기 변경
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 6.dp
                    )
                    Text("큰 크기", style = MaterialTheme.typography.bodySmall)
                }

                // 작은 크기 (버튼 내부용)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Text("작은 크기", style = MaterialTheme.typography.bodySmall)
                }
            }

            HorizontalDivider()

            // LinearProgressIndicator 커스터마이징
            Text(
                text = "LinearProgressIndicator 커스터마이징",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            // 기본
            Column {
                Text("기본:", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { 0.6f },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 색상 변경
            Column {
                Text("색상 변경:", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { 0.6f },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFF9800), // 주황색
                    trackColor = Color(0xFFFFF3E0)
                )
            }

            // 둥근 끝
            Column {
                Text("둥근 끝 (StrokeCap.Round):", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { 0.6f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun ConditionalDisplayDemo() {
    var newsList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "4. 조건부 표시 패턴 (실제 사용 예)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Problem 탭과 동일한 시나리오이지만, 이번에는 Progress Indicator를 표시합니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
                        when {
                            isLoading -> CircularProgressIndicator()
                            data.isNotEmpty() -> ContentList(data)
                            else -> EmptyState()
                        }
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    newsList = emptyList()
                    isLoading = true
                    scope.launch {
                        delay(3000)
                        newsList = listOf(
                            "속보: 오늘의 주요 뉴스",
                            "경제: 주식 시장 동향",
                            "스포츠: 축구 경기 결과",
                            "연예: 신작 영화 개봉"
                        )
                        isLoading = false
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("뉴스 불러오기")
            }

            // 해결: 조건부 표시!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        // 로딩 중: Progress Indicator 표시!
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "뉴스를 불러오는 중...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    newsList.isNotEmpty() -> {
                        // 로딩 완료: 콘텐츠 표시
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            newsList.forEach { news ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Text(
                                        text = news,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // 초기 상태
                        Text(
                            text = "버튼을 눌러 뉴스를 불러오세요",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 성공 메시지
            if (!isLoading && newsList.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(
                        text = "로딩 중에도 사용자가 앱이 정상 동작 중임을 알 수 있습니다!",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}
