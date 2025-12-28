package com.example.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 문제 상황 화면
 *
 * Slider를 사용하지 않고 볼륨 조절 UI를 직접 구현하면
 * 얼마나 복잡한지 보여줍니다.
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 상황 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: Slider 없이 볼륨 조절 구현하기",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "음악 앱의 볼륨 조절 슬라이더를 직접 구현해봅시다.\n" +
                            "드래그 제스처, 위치 계산, UI 그리기를 모두 수동으로 해야 합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 직접 구현한 슬라이더 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 구현한 볼륨 슬라이더",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 슬라이더를 드래그해보세요. 동작은 하지만 코드가 매우 복잡합니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                ManualVolumeSlider()
            }
        }

        HorizontalDivider()

        // 코드 복잡성 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 구현에 필요한 코드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        1. 드래그 제스처 감지 (pointerInput, detectDragGestures)
                        2. 컴포넌트 크기 측정 (onSizeChanged)
                        3. 드래그 위치 -> 값 변환 계산
                        4. 값 범위 제한 (coerceIn)
                        5. 트랙 배경 그리기 (Box + background)
                        6. 활성 트랙 그리기 (fillMaxWidth fraction)
                        7. Thumb 위치 계산 및 그리기 (offset)
                        8. 현재 값 텍스트 표시
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 문제점 나열
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProblemItem("코드가 50줄 이상 필요")
                ProblemItem("에지 케이스 처리 누락 (터치 영역, 빠른 드래그)")
                ProblemItem("접근성 미지원 (TalkBack)")
                ProblemItem("애니메이션 없음")
                ProblemItem("Material Design 미준수")
                ProblemItem("유지보수 어려움")
            }
        }

        // 해결책 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책은?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solution 탭에서 Material3 Slider를 사용한 간단한 해결책을 확인하세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProblemItem(text: String) {
    Text(
        text = "- $text",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onErrorContainer
    )
}

/**
 * Slider 없이 직접 구현한 볼륨 슬라이더
 *
 * 이 코드가 얼마나 복잡한지 보여주기 위한 예제입니다.
 * 실제로 이렇게 구현하지 마세요!
 */
@Composable
private fun ManualVolumeSlider() {
    var volume by remember { mutableFloatStateOf(0.5f) }
    var sliderWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 현재 볼륨 표시
        Text(
            text = "볼륨: ${(volume * 100).roundToInt()}%",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 직접 구현한 슬라이더 (복잡한 코드!)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 12.dp)
                // 1. 크기 측정
                .onSizeChanged { size ->
                    sliderWidth = size.width
                }
                // 2. 드래그 제스처 처리
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            // 터치 시작 위치로 값 설정
                            if (sliderWidth > 0) {
                                volume = (offset.x / sliderWidth).coerceIn(0f, 1f)
                            }
                        },
                        onDrag = { change, _ ->
                            // 드래그 중 위치로 값 업데이트
                            if (sliderWidth > 0) {
                                volume = (change.position.x / sliderWidth).coerceIn(0f, 1f)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // 3. 트랙 배경 (비활성 영역)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            // 4. 활성 트랙 (선택된 영역)
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = volume)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )

            // 5. Thumb (드래그 손잡이)
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = ((sliderWidth * volume) - with(density) { 12.dp.toPx() }).roundToInt(),
                            y = 0
                        )
                    }
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "위 슬라이더는 약 50줄의 코드로 구현되었습니다",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
