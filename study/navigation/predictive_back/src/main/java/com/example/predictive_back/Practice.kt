package com.example.predictive_back

import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

/**
 * 연습 문제 1: 기본 스케일 애니메이션 (쉬움)
 *
 * 카드 화면에서 뒤로가기 제스처 시 카드가 축소되는 애니메이션을 구현합니다.
 *
 * 학습 목표:
 * - PredictiveBackHandler 기본 사용법
 * - Flow<BackEventCompat> 수집
 * - try-catch-finally 패턴
 */
@Composable
fun Practice1_ScaleAnimation() {
    var showCard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 스케일 애니메이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "난이도: 쉬움",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "카드 화면에서 뒤로가기 제스처 시 카드가 1.0에서 0.8로 축소되는 애니메이션을 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. cardScale 상태를 mutableFloatStateOf(1f)로 선언")
                Text("2. graphicsLayer { scaleX = cardScale; scaleY = cardScale }")
                Text("3. scale = 1f - (event.progress * 0.2f)")
                Text("4. catch에서 CancellationException 처리 후 throw e")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!showCard) {
                    Button(onClick = { showCard = true }) {
                        Text("카드 화면 열기")
                    }
                } else {
                    Practice1_CardScreen(
                        onBack = { showCard = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice1_CardScreen(onBack: () -> Unit) {
    // TODO: 여기에 cardScale 상태를 선언하세요
    // var cardScale by remember { mutableFloatStateOf(1f) }

    // TODO: PredictiveBackHandler를 구현하세요
    // 요구사항:
    // 1. backEvent.collect로 progress 감지
    // 2. cardScale = 1f - (event.progress * 0.2f) 계산
    // 3. collect 완료 시 onBack() 호출
    // 4. CancellationException catch 후 throw e
    // 5. finally에서 cardScale = 1f로 복원

    // PredictiveBackHandler { backEvent: Flow<BackEventCompat> ->
    //     try {
    //         backEvent.collect { event ->
    //             cardScale = 1f - (event.progress * 0.2f)
    //         }
    //         onBack()
    //     } catch (e: CancellationException) {
    //         throw e
    //     } finally {
    //         cardScale = 1f
    //     }
    // }

    // 스케일 상태 (TODO 완성 전 기본값)
    val cardScale = 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    text = "카드 상세",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                text = "왼쪽 가장자리에서 스와이프하여\n카드가 축소되는지 확인하세요!",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Scale: ${String.format("%.2f", cardScale)}",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedButton(onClick = onBack) {
                Text("닫기")
            }
        }
    }
}

/**
 * 연습 문제 2: 스와이프 방향별 애니메이션 (중간)
 *
 * 왼쪽에서 스와이프하면 오른쪽으로 밀리고,
 * 오른쪽에서 스와이프하면 왼쪽으로 밀리는 애니메이션을 구현합니다.
 *
 * 학습 목표:
 * - BackEventCompat.swipeEdge 활용
 * - EDGE_LEFT, EDGE_RIGHT 구분
 * - translationX 애니메이션
 */
@Composable
fun Practice2_DirectionalAnimation() {
    var showPanel by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 스와이프 방향별 애니메이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "난이도: 중간",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "왼쪽에서 스와이프하면 화면이 오른쪽으로 이동하고,\n" +
                            "오른쪽에서 스와이프하면 화면이 왼쪽으로 이동하도록 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. event.swipeEdge로 방향 판단")
                Text("2. BackEventCompat.EDGE_LEFT / EDGE_RIGHT 상수 사용")
                Text("3. translationX = progress * maxOffset (또는 음수)")
                Text("4. maxOffset은 화면 너비의 30% 정도")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!showPanel) {
                    Button(onClick = { showPanel = true }) {
                        Text("패널 열기")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "패널을 연 후 왼쪽/오른쪽 가장자리에서\n스와이프해보세요",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Practice2_PanelScreen(
                        onBack = { showPanel = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice2_PanelScreen(onBack: () -> Unit) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val maxOffset = screenWidthPx * 0.3f  // 화면 너비의 30%

    // 상태
    var translationX by remember { mutableFloatStateOf(0f) }
    var currentEdge by remember { mutableStateOf("없음") }
    var progress by remember { mutableFloatStateOf(0f) }

    // TODO: PredictiveBackHandler를 구현하세요
    // 요구사항:
    // 1. event.swipeEdge로 스와이프 방향 감지
    // 2. BackEventCompat.EDGE_LEFT면 오른쪽으로 이동 (양수 offset)
    // 3. BackEventCompat.EDGE_RIGHT면 왼쪽으로 이동 (음수 offset)
    // 4. translationX = progress * maxOffset (또는 음수)

    // PredictiveBackHandler { backEvent: Flow<BackEventCompat> ->
    //     try {
    //         backEvent.collect { event ->
    //             progress = event.progress
    //             translationX = when (event.swipeEdge) {
    //                 BackEventCompat.EDGE_LEFT -> {
    //                     currentEdge = "왼쪽"
    //                     event.progress * maxOffset
    //                 }
    //                 BackEventCompat.EDGE_RIGHT -> {
    //                     currentEdge = "오른쪽"
    //                     -event.progress * maxOffset
    //                 }
    //                 else -> 0f
    //             }
    //         }
    //         onBack()
    //     } catch (e: CancellationException) {
    //         throw e
    //     } finally {
    //         translationX = 0f
    //         currentEdge = "없음"
    //         progress = 0f
    //     }
    // }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.translationX = translationX
            }
            .background(
                MaterialTheme.colorScheme.tertiaryContainer,
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
                    text = "방향별 패널",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                text = "화면 양쪽 가장자리에서 스와이프해보세요!\n" +
                        "스와이프 방향에 따라 다르게 이동합니다.",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("실시간 상태", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("스와이프 방향: $currentEdge")
                    Text("Progress: ${(progress * 100).toInt()}%")
                    Text("Translation X: ${translationX.toInt()}px")
                }
            }

            OutlinedButton(onClick = onBack) {
                Text("닫기")
            }
        }
    }
}

/**
 * 연습 문제 3: 커스텀 BottomSheet 통합 (어려움)
 *
 * 커스텀 BottomSheet 컴포넌트에 Predictive Back을 적용하여
 * 부드럽게 닫히도록 구현합니다.
 *
 * 학습 목표:
 * - 컴포넌트 상태와 Predictive Back 연동
 * - offset 기반 애니메이션
 * - 조건부 PredictiveBackHandler 활성화
 */
@Composable
fun Practice3_BottomSheetIntegration() {
    var showSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 커스텀 BottomSheet 통합",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "난이도: 어려움",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "커스텀 BottomSheet에 Predictive Back을 적용하여\n" +
                            "뒤로가기 제스처 시 시트가 아래로 슬라이드되며 닫히도록 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. sheetOffset 상태로 시트 위치 제어")
                Text("2. PredictiveBackHandler(enabled = showSheet)")
                Text("3. offset = progress * sheetHeight")
                Text("4. translationY로 아래로 이동")
            }
        }

        // 참고 사항
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "참고",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "실제 Material3 ModalBottomSheet는 이미 Predictive Back을 지원합니다.\n" +
                            "이 연습은 커스텀 컴포넌트 구현 학습을 위한 것입니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { showSheet = true }) {
                    Text("커스텀 시트 열기")
                }
            }
        }

        // 커스텀 BottomSheet
        if (showSheet) {
            Practice3_CustomBottomSheet(
                onDismiss = { showSheet = false }
            )
        }
    }
}

@Composable
private fun Practice3_CustomBottomSheet(onDismiss: () -> Unit) {
    val sheetHeight = 300.dp
    val density = LocalDensity.current
    val sheetHeightPx = with(density) { sheetHeight.toPx() }

    // 상태
    var offsetY by remember { mutableFloatStateOf(0f) }
    var progress by remember { mutableFloatStateOf(0f) }

    // TODO: PredictiveBackHandler를 구현하세요
    // 요구사항:
    // 1. enabled = true로 설정
    // 2. progress에 따라 offsetY 계산 (offsetY = event.progress * sheetHeightPx)
    // 3. collect 완료 시 onDismiss() 호출
    // 4. CancellationException 처리
    // 5. finally에서 offsetY와 progress 초기화

    // PredictiveBackHandler(enabled = true) { backEvent: Flow<BackEventCompat> ->
    //     try {
    //         backEvent.collect { event ->
    //             progress = event.progress
    //             offsetY = event.progress * sheetHeightPx
    //         }
    //         onDismiss()
    //     } catch (e: CancellationException) {
    //         throw e
    //     } finally {
    //         offsetY = 0f
    //         progress = 0f
    //     }
    // }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f * (1f - progress)))
    ) {
        // Scrim 클릭 시 닫기
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = sheetHeight)
        ) {
            // 빈 영역 (터치 가능)
        }

        // BottomSheet
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(sheetHeight)
                .graphicsLayer {
                    translationY = offsetY
                }
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 핸들
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(4.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "커스텀 BottomSheet",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "뒤로가기 제스처로 시트를 닫아보세요!\n" +
                            "시트가 아래로 슬라이드됩니다.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Progress: ${(progress * 100).toInt()}%")
                        Text("Offset Y: ${offsetY.toInt()}px")
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("닫기")
                }
            }
        }
    }
}
