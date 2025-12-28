package com.example.haptic_feedback

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt

/**
 * 연습 문제 1: 좋아요 버튼 햅틱 (쉬움)
 *
 * LocalHapticFeedback을 사용하여 좋아요 버튼에 햅틱 피드백을 추가합니다.
 */
@Composable
fun Practice1_LikeButtonScreen() {
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
                    text = "연습 1: 좋아요 버튼 햅틱 (쉬움)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "하트 버튼을 클릭하면 햅틱 피드백이 발생하고 좋아요 상태가 토글되도록 구현하세요.",
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
                Text("1. LocalHapticFeedback.current로 햅틱 객체 가져오기")
                Text("2. onClick에서 performHapticFeedback(HapticFeedbackType.LongPress) 호출")
                Text("3. isLiked 상태 토글하기")
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
                Practice1_Exercise()
            }
        }

        // 정답 보기 (접을 수 있음)
        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 코드",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = """
val haptics = LocalHapticFeedback.current
var isLiked by remember { mutableStateOf(false) }

IconButton(onClick = {
    haptics.performHapticFeedback(
        HapticFeedbackType.LongPress
    )
    isLiked = !isLiked
}) {
    Icon(
        imageVector = if (isLiked)
            Icons.Filled.Favorite
        else Icons.Outlined.FavoriteBorder,
        tint = if (isLiked) Color.Red
            else Color.Gray
    )
}
                            """.trimIndent(),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: LocalHapticFeedback을 사용하여 구현하세요
    // 요구사항:
    // 1. LocalHapticFeedback.current로 햅틱 객체 가져오기
    // 2. onClick에서 performHapticFeedback(HapticFeedbackType.LongPress) 호출
    // 3. isLiked 상태 토글하기

    // TODO: 햅틱 피드백 객체 가져오기
    // val haptics = LocalHapticFeedback.current

    var isLiked by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                // TODO: 여기에 햅틱 피드백 추가
                // haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                isLiked = !isLiked
            },
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    if (isLiked) Color.Red.copy(alpha = 0.1f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "좋아요",
                tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isLiked) "좋아요!" else "좋아요 없음",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * 연습 문제 2: 볼륨 슬라이더 틱 햅틱 (중간)
 *
 * snapshotFlow와 LocalView를 사용하여 슬라이더 10단계마다 틱 햅틱을 발생시킵니다.
 */
@Composable
fun Practice2_VolumeSliderScreen() {
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
                    text = "연습 2: 볼륨 슬라이더 틱 햅틱 (중간)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "볼륨 슬라이더를 움직일 때 10단계(0, 10, 20, ..., 100)마다 " +
                            "CLOCK_TICK 햅틱이 발생하도록 구현하세요.",
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
                Text("1. LocalView.current로 View 객체 가져오기")
                Text("2. LaunchedEffect + snapshotFlow로 volume 변화 감지")
                Text("3. map { (it / 10).toInt() }로 10단위로 변환")
                Text("4. distinctUntilChanged()로 중복 방지")
                Text("5. HapticFeedbackConstants.CLOCK_TICK 사용")
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
                Practice2_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 코드",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = """
val view = LocalView.current
var volume by remember { mutableFloatStateOf(50f) }

LaunchedEffect(Unit) {
    snapshotFlow { volume }
        .map { (it / 10).toInt() }
        .distinctUntilChanged()
        .drop(1) // 초기값 스킵
        .collect {
            view.performHapticFeedback(
                HapticFeedbackConstants.CLOCK_TICK
            )
        }
}

Slider(
    value = volume,
    onValueChange = { volume = it },
    valueRange = 0f..100f,
    steps = 9
)
                            """.trimIndent(),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: snapshotFlow와 LocalView를 사용하여 구현하세요
    // 요구사항:
    // 1. LocalView.current로 View 객체 가져오기
    // 2. LaunchedEffect + snapshotFlow로 volume 변화 감지
    // 3. map { (it / 10).toInt() }로 10단위로 변환
    // 4. distinctUntilChanged()로 중복 방지
    // 5. HapticFeedbackConstants.CLOCK_TICK 사용

    // TODO: View 객체 가져오기
    // val view = LocalView.current

    var volume by remember { mutableFloatStateOf(50f) }

    // TODO: LaunchedEffect로 volume 변화 감지 및 햅틱 발생
    // LaunchedEffect(Unit) {
    //     snapshotFlow { volume }
    //         .map { (it / 10).toInt() }
    //         .distinctUntilChanged()
    //         .drop(1)
    //         .collect {
    //             view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    //         }
    // }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "볼륨: ${volume.toInt()}%",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = volume,
            onValueChange = { volume = it },
            valueRange = 0f..100f,
            steps = 9,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "10단계마다 틱 햅틱이 발생합니다",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 연습 문제 3: 드래그 앤 드롭 햅틱 (어려움)
 *
 * 드래그 인터랙션에 다양한 햅틱 타입을 적용합니다.
 * - 드래그 시작: LONG_PRESS
 * - 드래그 중 특정 거리마다: CLOCK_TICK
 * - 드롭 영역 진입/성공: CONFIRM
 * - 드롭 영역 밖: REJECT
 */
@Composable
fun Practice3_DragDropScreen() {
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
                    text = "연습 3: 드래그 앤 드롭 햅틱 (어려움)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "카드를 드래그하여 드롭 영역에 놓으세요. " +
                            "드래그 시작, 이동, 드롭 성공/실패에 각각 다른 햅틱이 발생합니다.",
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
                Text("1. pointerInput + detectDragGestures 사용")
                Text("2. onDragStart에서 LONG_PRESS 햅틱")
                Text("3. onDrag에서 일정 거리마다 CLOCK_TICK")
                Text("4. onDragEnd에서 드롭 위치 확인 후 CONFIRM/REJECT")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice3_Exercise()
            }
        }

        // 정답 코드 (복잡하므로 별도 표시)
        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 코드 (핵심 부분)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = """
.pointerInput(Unit) {
    detectDragGestures(
        onDragStart = {
            view.performHapticFeedback(
                HapticFeedbackConstants.LONG_PRESS
            )
            isDragging = true
        },
        onDrag = { _, dragAmount ->
            offset += dragAmount
            // 50dp마다 틱 햅틱
            val newTick = (offset.getDistance() / 50).toInt()
            if (newTick != lastTick) {
                view.performHapticFeedback(
                    HapticFeedbackConstants.CLOCK_TICK
                )
                lastTick = newTick
            }
        },
        onDragEnd = {
            if (isInDropZone) {
                // CONFIRM 햅틱 (API 30+)
                performConfirmHaptic(view)
            } else {
                // REJECT 햅틱 (API 30+)
                performRejectHaptic(view)
            }
        }
    )
}
                            """.trimIndent(),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 드래그 앤 드롭에 다양한 햅틱 타입을 적용하세요
    // 요구사항:
    // 1. onDragStart에서 LONG_PRESS 햅틱
    // 2. onDrag에서 일정 거리마다 CLOCK_TICK
    // 3. onDragEnd에서 드롭 위치 확인 후 CONFIRM/REJECT

    // TODO: View 객체 가져오기
    // val view = LocalView.current

    var offset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var dropResult by remember { mutableStateOf<Boolean?>(null) }

    // 드롭 영역 정의 (상단 중앙)
    val dropZoneCenter = Offset(150f, 50f)
    val dropZoneRadius = 80f

    fun isInDropZone(): Boolean {
        val distance = (offset - dropZoneCenter).getDistance()
        return distance < dropZoneRadius
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 드롭 영역
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    when {
                        dropResult == true -> Color.Green.copy(alpha = 0.3f)
                        dropResult == false -> Color.Red.copy(alpha = 0.3f)
                        isDragging && isInDropZone() -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when {
                    dropResult == true -> "성공!"
                    dropResult == false -> "다시 시도"
                    else -> "드롭 영역"
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 드래그 가능한 카드
        Card(
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .align(Alignment.Center)
                .size(80.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            // TODO: LONG_PRESS 햅틱 추가
                            // view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            isDragging = true
                            dropResult = null
                        },
                        onDrag = { _, dragAmount ->
                            offset += dragAmount
                            // TODO: 일정 거리마다 CLOCK_TICK 햅틱
                        },
                        onDragEnd = {
                            isDragging = false
                            if (isInDropZone()) {
                                // TODO: CONFIRM 햅틱 (API 30+)
                                dropResult = true
                            } else {
                                // TODO: REJECT 햅틱 (API 30+)
                                dropResult = false
                            }
                            // 위치 리셋
                            offset = Offset.Zero
                        },
                        onDragCancel = {
                            isDragging = false
                            offset = Offset.Zero
                        }
                    )
                },
            colors = CardDefaults.cardColors(
                containerColor = if (isDragging)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.primary
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isDragging) "드래그 중" else "드래그",
                    color = if (isDragging)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 안내 텍스트
        Text(
            text = "카드를 드래그하여 상단 드롭 영역에 놓으세요",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Offset 확장 함수
private fun Offset.getDistance(): Float {
    return kotlin.math.sqrt(x * x + y * y)
}
