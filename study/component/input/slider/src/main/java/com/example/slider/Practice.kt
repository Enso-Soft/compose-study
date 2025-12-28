package com.example.slider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 온도 조절기 (쉬움)
 *
 * 에어컨 온도를 조절하는 Slider를 만드세요.
 */
@Composable
fun Practice1_TemperatureControl() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 온도 조절기",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "에어컨 앱의 온도 조절 슬라이더를 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 온도 범위: 16도 ~ 30도")
                Text("2. 현재 온도를 \"현재 온도: XX도\" 형태로 표시")
                Text("3. 소수점 없이 정수로 표시 (roundToInt 사용)")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- valueRange = 16f..30f 사용\n" +
                            "- temperature.roundToInt() 로 정수 변환",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
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
                Text(
                    text = "아래에 구현하세요",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 온도 조절 Slider를 구현하세요
    //
    // 요구사항:
    // 1. remember로 온도 상태 관리 (초기값: 24f)
    // 2. Slider의 valueRange를 16f..30f로 설정
    // 3. 현재 온도를 Text로 표시 (정수로 반올림)
    //
    // 예시 출력: "현재 온도: 24도"

    Text(
        text = "TODO: 온도 조절 Slider를 구현하세요",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}

/**
 * 연습 문제 2: 별점 선택기 (중간)
 *
 * 1~5점을 선택하는 Slider와 별 아이콘을 표시하세요.
 */
@Composable
fun Practice2_StarRating() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 별점 선택기",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "리뷰 앱의 별점 선택 UI를 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 1점 ~ 5점 선택 가능 (1, 2, 3, 4, 5만 선택)")
                Text("2. steps 파라미터를 사용하여 불연속 값으로 설정")
                Text("3. 선택된 점수만큼 채워진 별 아이콘 표시")
                Text("4. \"평점: X점\" 형태로 텍스트 표시")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- valueRange = 1f..5f, steps = 3 (시작+끝+중간3 = 5개)\n" +
                            "- Icons.Default.Star (채워진 별)\n" +
                            "- Icons.Outlined.Star (빈 별)\n" +
                            "- repeat(rating.roundToInt()) { ... } 로 별 반복",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
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
                Text(
                    text = "아래에 구현하세요",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice2_Exercise()
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 별점 선택 Slider를 구현하세요
    //
    // 요구사항:
    // 1. remember로 평점 상태 관리 (초기값: 3f)
    // 2. Slider의 valueRange를 1f..5f로 설정
    // 3. steps = 3 으로 설정 (1, 2, 3, 4, 5 총 5개 선택점)
    // 4. Row로 별 아이콘 5개 표시
    //    - rating 이하: 채워진 별 (Icons.Default.Star)
    //    - rating 초과: 빈 별 (Icons.Outlined.Star)
    // 5. "평점: X점" 텍스트 표시
    //
    // 예시 출력:
    // ★★★☆☆
    // 평점: 3점

    Text(
        text = "TODO: 별점 선택 Slider를 구현하세요",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}

/**
 * 연습 문제 3: 가격 범위 필터 (어려움)
 *
 * RangeSlider로 쇼핑앱의 가격 필터를 구현하세요.
 */
@Composable
fun Practice3_PriceRangeFilter() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 가격 범위 필터",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "쇼핑앱의 가격 필터 UI를 구현하세요.\n" +
                            "RangeSlider로 최소/최대 가격을 동시에 선택합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // 요구사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. RangeSlider 사용")
                Text("2. 가격 범위: 0 ~ 100만원")
                Text("3. 10만원 단위로만 선택 가능 (steps 사용)")
                Text("4. \"XX만원 ~ YY만원\" 형태로 표시")
                Text("5. 선택 범위 초기값: 20만원 ~ 80만원")
            }
        }

        // 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- RangeSlider의 value는 ClosedFloatingPointRange 타입\n" +
                            "- 초기값: mutableStateOf(20f..80f)\n" +
                            "- valueRange = 0f..100f\n" +
                            "- steps = 9 (0, 10, 20, ..., 100 = 11개 선택점)\n" +
                            "- priceRange.start.roundToInt() 로 시작값 접근",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
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
                Text(
                    text = "아래에 구현하세요",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice3_Exercise()
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 가격 범위 필터를 구현하세요
    //
    // 요구사항:
    // 1. remember로 가격 범위 상태 관리 (초기값: 20f..80f)
    //    - var priceRange by remember { mutableStateOf(20f..80f) }
    // 2. RangeSlider 사용
    //    - value = priceRange
    //    - onValueChange = { priceRange = it }
    //    - valueRange = 0f..100f
    //    - steps = 9 (10만원 단위로 총 11개 선택점)
    // 3. "XX만원 ~ YY만원" 형태로 Text 표시
    //    - priceRange.start.roundToInt()
    //    - priceRange.endInclusive.roundToInt()
    //
    // 예시 출력: "20만원 ~ 80만원"

    Text(
        text = "TODO: 가격 범위 필터를 구현하세요",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}
