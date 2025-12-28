package com.example.canvas_drawing

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * 문제가 있는 코드 - Canvas 없이 그래픽 구현 시도
 *
 * 이 코드의 문제점:
 * 1. 성능 저하: 각 터치 포인트마다 새로운 Box Composable 생성
 * 2. 선 연결 불가: 점만 표시되고 부드러운 선이 그려지지 않음
 * 3. 재구성 폭발: 점이 추가될 때마다 전체 리스트가 재구성됨
 * 4. 스타일링 제한: 그라디언트, 굵기 변경 등 세밀한 제어 불가
 */
@Composable
fun ProblemScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }
    val demos = listOf("Box로 점 찍기", "리소스 의존")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            demos.forEachIndexed { index, title ->
                FilterChip(
                    selected = selectedDemo == index,
                    onClick = { selectedDemo = index },
                    label = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> BoxDrawingProblem()
            1 -> ResourceDependencyProblem()
        }
    }
}

/**
 * 문제 1: Box로 점 찍기 시도
 *
 * 터치한 위치에 작은 Box를 생성하여 그림을 그리려는 시도
 * 결과: 성능 저하, 선 연결 불가, 재구성 폭발
 */
@Composable
private fun BoxDrawingProblem() {
    // 터치 포인트 저장 (최대 100개로 제한 - 성능 문제 방지)
    var points by remember { mutableStateOf<List<Offset>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: Box로 그림 그리기",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 문제가 있는 드로잉 영역
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.White)
                        .pointerInput(Unit) {
                            detectDragGestures { change, _ ->
                                // 포인트 추가 (최대 100개 제한)
                                if (points.size < 100) {
                                    points = points + change.position
                                }
                            }
                        }
                ) {
                    // 문제: 각 포인트마다 새로운 Box Composable 생성!
                    // 이로 인해:
                    // 1. 수십, 수백 개의 Composable이 생성됨
                    // 2. 각 포인트 추가 시 전체 리스트 재구성
                    // 3. 점만 표시되고 선으로 연결되지 않음
                    points.forEach { point ->
                        Box(
                            modifier = Modifier
                                .offset { IntOffset(point.x.roundToInt() - 4, point.y.roundToInt() - 4) }
                                .size(8.dp)
                                .background(Color.Black, CircleShape)
                        )
                    }

                    if (points.isEmpty()) {
                        Text(
                            text = "여기를 드래그해보세요\n(100개 제한)",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 문제점 표시
                Text(
                    text = "점 개수: ${points.size}개",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { points = emptyList() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("지우기")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 설명
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
                Text("1. 각 점마다 Box Composable 생성 -> 메모리 낭비")
                Text("2. 점이 추가될 때마다 전체 리스트 재구성")
                Text("3. 점만 표시되고 선으로 연결되지 않음")
                Text("4. 그라디언트, 스타일 변경 등 세밀한 제어 불가")
                Text("5. 수백 개 이상 시 심각한 성능 저하")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "-> Canvas + Path 사용 필요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 문제 2: 리소스 파일 의존
 *
 * 모든 아이콘/그래픽을 drawable 리소스로 관리하려는 시도
 * 결과: 동적 변경 불가, 다양한 크기/색상마다 리소스 필요
 */
@Composable
private fun ResourceDependencyProblem() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: 리소스 파일 의존",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 가상의 시나리오 설명
                Text(
                    text = "시나리오: 다양한 크기/색상의 별 아이콘 필요",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 문제 상황 시각화
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // 가상의 리소스 아이콘 (실제로는 Box로 대체)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.Yellow)
                        )
                        Text("ic_star_small", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.Yellow)
                        )
                        Text("ic_star_medium", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.Yellow)
                        )
                        Text("ic_star_large", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.Red)
                        )
                        Text("ic_star_red", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.Blue)
                        )
                        Text("ic_star_blue", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.Green)
                        )
                        Text("ic_star_green", style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "각 크기/색상 조합마다 별도 리소스 파일 필요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "리소스 의존의 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 크기 변경: 각 크기마다 별도 파일 필요")
                Text("2. 색상 변경: 각 색상마다 별도 파일 필요")
                Text("3. 조합 폭발: 크기 x 색상 = 수많은 파일")
                Text("4. 동적 변경 불가: 런타임에 모양 변경 어려움")
                Text("5. 애니메이션 제한: 도형 변형 애니메이션 불가")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "-> Canvas로 동적 그리기 필요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Canvas 해결책 미리보기
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Canvas로 해결하면?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- 단일 Canvas에서 모든 크기/색상 동적 처리")
                Text("- 코드로 도형 정의 -> 유지보수 용이")
                Text("- 런타임에 크기, 색상, 스타일 자유롭게 변경")
                Text("- 그라디언트, 애니메이션 쉽게 적용")
            }
        }
    }
}
