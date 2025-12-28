package com.example.slider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 해결책 화면
 *
 * Material3 Slider를 사용한 간단하고 완벽한 해결책을 보여줍니다.
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
        // 해결책 소개
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Material3 Slider 사용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Slider 컴포넌트 하나로 모든 문제가 해결됩니다!\n" +
                            "드래그 제스처, 접근성, 애니메이션이 모두 내장되어 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 1. 기본 Slider
        SolutionCard(
            title = "1. 기본 Slider",
            description = "가장 간단한 사용법. value와 onValueChange만 있으면 됩니다."
        ) {
            BasicSliderDemo()
        }

        // 2. valueRange로 범위 지정
        SolutionCard(
            title = "2. valueRange로 범위 지정",
            description = "0~100 범위에서 밝기를 조절합니다."
        ) {
            ValueRangeSliderDemo()
        }

        // 3. steps로 불연속 값
        SolutionCard(
            title = "3. steps로 단계별 선택",
            description = "1~5점 평점을 선택합니다. steps=3은 중간에 3개 단계를 만듭니다."
        ) {
            StepsSliderDemo()
        }

        HorizontalDivider()

        // 4. RangeSlider
        SolutionCard(
            title = "4. RangeSlider로 범위 선택",
            description = "최소/최대 두 값을 동시에 선택합니다. 가격 필터에 적합합니다."
        ) {
            RangeSliderDemo()
        }

        // 5. 색상 커스터마이징
        SolutionCard(
            title = "5. 색상 커스터마이징",
            description = "SliderDefaults.colors()로 Thumb과 Track 색상을 변경합니다."
        ) {
            ColorCustomSliderDemo()
        }

        // 6. enabled/disabled
        SolutionCard(
            title = "6. enabled/disabled 상태",
            description = "enabled=false로 슬라이더를 비활성화합니다."
        ) {
            EnabledSliderDemo()
        }

        // 코드 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "코드 비교",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "직접 구현: 약 50줄\nSlider 사용: 단 3줄!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        var volume by remember { mutableFloatStateOf(0.5f) }
                        Slider(value = volume, onValueChange = { volume = it })
                        Text("볼륨: ${"$"}{(volume * 100).toInt()}%")
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
private fun SolutionCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

/**
 * 기본 Slider 데모
 */
@Composable
private fun BasicSliderDemo() {
    var volume by remember { mutableFloatStateOf(0.5f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "볼륨",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "볼륨: ${(volume * 100).roundToInt()}%",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 단 2줄로 완벽한 슬라이더!
        Slider(
            value = volume,
            onValueChange = { volume = it }
        )
    }
}

/**
 * valueRange 데모
 */
@Composable
private fun ValueRangeSliderDemo() {
    var brightness by remember { mutableFloatStateOf(50f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "밝기: ${brightness.roundToInt()}%",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = brightness,
            onValueChange = { brightness = it },
            valueRange = 0f..100f  // 0~100 범위
        )
    }
}

/**
 * steps 데모
 */
@Composable
private fun StepsSliderDemo() {
    var rating by remember { mutableFloatStateOf(3f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "평점: ${rating.roundToInt()}점",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = rating,
            onValueChange = { rating = it },
            valueRange = 1f..5f,
            steps = 3  // 중간에 3개 단계 = 1, 2, 3, 4, 5 선택 가능
        )

        Text(
            text = "steps=3: 시작(1) + 중간(3개) + 끝(5) = 5개 선택점",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * RangeSlider 데모
 */
@Composable
private fun RangeSliderDemo() {
    var priceRange by remember { mutableStateOf(20f..80f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "가격: ${priceRange.start.roundToInt()}만원 ~ ${priceRange.endInclusive.roundToInt()}만원",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = priceRange,
            onValueChange = { priceRange = it },
            valueRange = 0f..100f
        )

        Text(
            text = "두 개의 Thumb을 드래그하여 범위를 선택하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 색상 커스터마이징 데모
 */
@Composable
private fun ColorCustomSliderDemo() {
    var value by remember { mutableFloatStateOf(0.7f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "커스텀 색상: ${(value * 100).roundToInt()}%",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = value,
            onValueChange = { value = it },
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFE91E63),                    // 핑크 Thumb
                activeTrackColor = Color(0xFFE91E63).copy(alpha = 0.7f),  // 핑크 활성 트랙
                inactiveTrackColor = Color.Gray.copy(alpha = 0.3f) // 회색 비활성 트랙
            )
        )

        Text(
            text = "SliderDefaults.colors()로 색상 변경",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * enabled/disabled 데모
 */
@Composable
private fun EnabledSliderDemo() {
    var isEnabled by remember { mutableStateOf(true) }
    var value by remember { mutableFloatStateOf(0.5f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("활성화:")
            Switch(
                checked = isEnabled,
                onCheckedChange = { isEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = value,
            onValueChange = { value = it },
            enabled = isEnabled  // 비활성화 시 회색으로 표시
        )

        Text(
            text = if (isEnabled) "드래그 가능" else "드래그 불가 (비활성화)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
