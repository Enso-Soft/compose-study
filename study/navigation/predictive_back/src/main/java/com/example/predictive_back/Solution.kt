package com.example.predictive_back

import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

/**
 * 해결책 화면
 *
 * PredictiveBackHandler를 사용하여 부드러운 뒤로가기 전환을 구현합니다.
 *
 * 해결 포인트:
 * 1. progress 값으로 실시간 애니메이션
 * 2. CancellationException으로 취소 처리
 * 3. 시스템이 이전 화면 미리보기 제공
 */
@Composable
fun SolutionScreen() {
    var showDetail by remember { mutableStateOf(false) }
    var smoothTransitionCount by remember { mutableIntStateOf(0) }
    var cancelCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: PredictiveBackHandler",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "PredictiveBackHandler는 제스처 진행 중 실시간으로 progress 값을 제공합니다.\n" +
                            "이를 활용하여 부드러운 애니메이션을 적용하고, 취소 시 원래 상태로 복원할 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 데모 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "부드러운 전환",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "${smoothTransitionCount}회",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "취소됨",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "${cancelCount}회",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!showDetail) {
                    SolutionListScreen(
                        onItemClick = { showDetail = true }
                    )
                } else {
                    SolutionDetailScreen(
                        onBack = {
                            smoothTransitionCount++
                            showDetail = false
                        },
                        onCancel = {
                            cancelCount++
                        }
                    )
                }
            }
        }

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Flow<BackEventCompat>으로 실시간 진행 상태 수신")
                Text("2. event.progress (0.0~1.0)로 애니메이션 적용")
                Text("3. CancellationException으로 취소 시 상태 복원")
            }
        }

        // 올바른 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드 (PredictiveBackHandler)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
PredictiveBackHandler { backEvent ->
    try {
        backEvent.collect { event ->
            // 제스처 진행에 따라 화면 축소
            scale = 1f - (event.progress * 0.2f)
        }
        // 제스처 완료: 뒤로가기 실행
        onBack()
    } catch (e: CancellationException) {
        // 제스처 취소: 원래 상태로 복원
        onCancel()
        throw e
    } finally {
        scale = 1f
    }
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // BackEventCompat 정보
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "BackEventCompat 속성",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("progress", fontWeight = FontWeight.Medium)
                    Text("0.0 ~ 1.0 (제스처 진행률)")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("touchX / touchY", fontWeight = FontWeight.Medium)
                    Text("터치 좌표")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("swipeEdge", fontWeight = FontWeight.Medium)
                    Text("EDGE_LEFT / EDGE_RIGHT")
                }
            }
        }

        // 사용 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "테스트 방법",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. '상세 화면으로 이동' 버튼을 클릭하세요\n" +
                            "2. 화면 왼쪽 가장자리에서 오른쪽으로 스와이프하세요\n" +
                            "3. 스와이프하는 동안 화면이 축소되는 것을 확인하세요\n" +
                            "4. 손을 떼지 않고 원래 위치로 돌리면 취소됩니다",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun SolutionListScreen(
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "메인 화면 (목록)",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "버튼을 눌러 상세 화면으로 이동하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onItemClick) {
            Text("상세 화면으로 이동")
        }
    }
}

@Composable
private fun SolutionDetailScreen(
    onBack: () -> Unit,
    onCancel: () -> Unit
) {
    // 애니메이션 상태
    var scale by remember { mutableFloatStateOf(1f) }
    var progress by remember { mutableFloatStateOf(0f) }
    var swipeEdge by remember { mutableStateOf("없음") }

    // PredictiveBackHandler - 부드러운 전환
    PredictiveBackHandler { backEvent: Flow<BackEventCompat> ->
        try {
            backEvent.collect { event ->
                // Step 1: 진행 상태 업데이트
                progress = event.progress

                // Step 2: 스와이프 방향 확인
                swipeEdge = when (event.swipeEdge) {
                    BackEventCompat.EDGE_LEFT -> "왼쪽"
                    BackEventCompat.EDGE_RIGHT -> "오른쪽"
                    else -> "없음"
                }

                // Step 3: 애니메이션 적용 (1.0 -> 0.8)
                scale = 1f - (event.progress * 0.2f)
            }

            // Step 4: 제스처 완료 - 뒤로가기 실행
            onBack()
        } catch (e: CancellationException) {
            // Step 5: 제스처 취소 - 상태 복원
            onCancel()
            throw e  // 반드시 다시 던져야 함
        } finally {
            // Step 6: 완료/취소 모두 - 초기화
            scale = 1f
            progress = 0f
            swipeEdge = "없음"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
                Text(
                    text = "상세 화면",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                text = "이 화면에서 왼쪽 가장자리를 스와이프해보세요.\n" +
                        "화면이 축소되면서 부드럽게 전환됩니다!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // 실시간 상태 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "실시간 상태",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Progress:")
                        Text("${(progress * 100).toInt()}%")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Scale:")
                        Text("${String.format("%.2f", scale)}")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Swipe Edge:")
                        Text(swipeEdge)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "스와이프 도중 손을 떼지 않고 원위치로 돌리면 취소됩니다",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(8.dp)
                )
            }

            OutlinedButton(onClick = onBack) {
                Text("버튼으로 뒤로가기")
            }
        }
    }
}
