package com.example.canvas_drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * 연습 문제 1: 태양 아이콘 그리기 (기초)
 *
 * 요구사항:
 * - 중앙에 노란색 원 그리기 (태양 본체)
 * - 8개의 광선을 45도 간격으로 그리기
 * - 광선은 원 바깥에서 시작하여 바깥쪽으로 뻗어나감
 *
 * TODO: drawCircle과 drawLine을 사용해서 태양 아이콘을 완성하세요!
 */
@Composable
fun Practice1_SunIconScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 태양 아이콘",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: drawCircle과 drawLine을 사용해서 태양을 그려보세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .size(200.dp)
                .background(Color(0xFF87CEEB)) // 하늘색 배경
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val sunRadius = size.minDimension / 4  // 태양 반지름
            val rayInnerRadius = sunRadius + 15f    // 광선 시작점 (원 바깥)
            val rayOuterRadius = sunRadius + 50f    // 광선 끝점

            // TODO 1: 노란색 원 그리기 (태양 본체)
            // 힌트: drawCircle(color = Color.Yellow, radius = sunRadius, center = center)

            // 정답 (주석 해제하여 확인):
            /*
            drawCircle(
                color = Color(0xFFFFD700),  // 금색
                radius = sunRadius,
                center = center
            )
            */

            // TODO 2: 8개의 광선 그리기 (45도 간격)
            // 힌트:
            // - for 루프로 0, 45, 90, 135, 180, 225, 270, 315도 순회
            // - 각도를 라디안으로 변환: Math.toRadians(angle.toDouble())
            // - 시작점 x = center.x + rayInnerRadius * cos(radian)
            // - 시작점 y = center.y + rayInnerRadius * sin(radian)
            // - 끝점도 같은 방식으로 rayOuterRadius 사용

            // 정답 (주석 해제하여 확인):
            /*
            for (angle in 0 until 360 step 45) {
                val radian = Math.toRadians(angle.toDouble())
                val startX = center.x + rayInnerRadius * cos(radian).toFloat()
                val startY = center.y + rayInnerRadius * sin(radian).toFloat()
                val endX = center.x + rayOuterRadius * cos(radian).toFloat()
                val endY = center.y + rayOuterRadius * sin(radian).toFloat()

                drawLine(
                    color = Color(0xFFFFD700),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            }
            */
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. drawCircle(color, radius, center) 사용")
                Text("2. for (angle in 0 until 360 step 45) 로 8방향")
                Text("3. Math.toRadians()로 도 -> 라디안 변환")
                Text("4. cos, sin으로 x, y 좌표 계산")
                Text("5. drawLine(color, start, end, strokeWidth)")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "좌표 공식:\n" +
                    "x = center.x + radius * cos(radian)\n" +
                    "y = center.y + radius * sin(radian)",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 연습 문제 2: 별 모양 그리기 (중급)
 *
 * 요구사항:
 * - Path를 사용하여 5개 꼭지점을 가진 별 그리기
 * - 외부 반지름(꼭지점)과 내부 반지름(오목한 부분) 사용
 * - 금색으로 채우기
 *
 * TODO: Path의 moveTo, lineTo를 사용해서 별을 그려보세요!
 */
@Composable
fun Practice2_StarScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: 별 모양",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: Path를 사용해서 5각 별을 그려보세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .size(200.dp)
                .background(Color(0xFF1A237E)) // 진한 남색 배경
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val outerRadius = size.minDimension / 2.5f  // 꼭지점 반지름
            val innerRadius = outerRadius / 2.5f         // 오목한 부분 반지름
            val points = 5  // 별 꼭지점 개수

            // TODO: 별 모양 Path 생성 및 그리기
            // 힌트:
            // 1. Path() 객체 생성
            // 2. 10개의 점 (외부 5개 + 내부 5개)을 번갈아 연결
            // 3. 각도 간격: 360도 / 10 = 36도
            // 4. 시작 각도: -90도 (12시 방향)
            // 5. 짝수 인덱스는 outerRadius, 홀수 인덱스는 innerRadius

            val starPath = Path()

            // 정답 (주석 해제하여 확인):
            /*
            val totalPoints = points * 2  // 외부 + 내부 = 10개 점
            val angleStep = 360f / totalPoints

            for (i in 0 until totalPoints) {
                val radius = if (i % 2 == 0) outerRadius else innerRadius
                val angle = Math.toRadians((i * angleStep - 90).toDouble())

                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()

                if (i == 0) {
                    starPath.moveTo(x, y)
                } else {
                    starPath.lineTo(x, y)
                }
            }
            starPath.close()

            drawPath(
                path = starPath,
                color = Color(0xFFFFD700),  // 금색
                style = androidx.compose.ui.graphics.drawscope.Fill
            )
            */

            // 별이 완성되면 이 코드로 그려집니다:
            // drawPath(path = starPath, color = Color(0xFFFFD700))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 5각 별 = 10개의 점 (외부 5 + 내부 5)")
                Text("2. 각도 간격 = 360 / 10 = 36도")
                Text("3. -90도에서 시작 (12시 방향)")
                Text("4. 짝수 인덱스: outerRadius (꼭지점)")
                Text("5. 홀수 인덱스: innerRadius (오목)")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Path 사용법:\n" +
                    "path.moveTo(x, y)  // 시작점\n" +
                    "path.lineTo(x, y)  // 선 그리기\n" +
                    "path.close()       // 경로 닫기",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 별 구조 설명 이미지 대체
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("별의 구조:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("     0 (외부, -90도)")
                Text("    / \\")
                Text("   9   1 (내부)")
                Text("  /     \\")
                Text(" 8       2 (외부, -18도)")
                Text("  \\     /")
                Text("   7   3")
                Text("    \\ /")
                Text("  6   4")
                Text("    5 (외부, 90도)")
            }
        }
    }
}

/**
 * 연습 문제 3: 원형 프로그레스 바 (심화)
 *
 * 요구사항:
 * - 배경으로 회색 원형 테두리 그리기
 * - 그 위에 그라디언트가 적용된 프로그레스 호 그리기
 * - 슬라이더로 프로그레스 값 조절 가능
 * - 중앙에 퍼센트 표시
 *
 * TODO: drawArc과 Brush.sweepGradient를 사용해서 완성하세요!
 */
@Composable
fun Practice3_CircularProgressScreen() {
    var progress by remember { mutableFloatStateOf(0.75f) }  // 0.0 ~ 1.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 3: 원형 프로그레스 바",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: drawArc과 Brush.sweepGradient를 사용하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(200.dp)
            ) {
                val strokeWidth = 24f
                val arcSize = Size(
                    size.width - strokeWidth,
                    size.height - strokeWidth
                )
                val arcTopLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                // TODO 1: 배경 원 그리기 (회색, 360도)
                // 힌트: drawArc(color, startAngle, sweepAngle, useCenter, topLeft, size, style)
                // - startAngle: 0f
                // - sweepAngle: 360f
                // - useCenter: false (호만 그림)
                // - style: Stroke(width = strokeWidth)

                // 정답 (주석 해제하여 확인):
                /*
                drawArc(
                    color = Color.LightGray,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )
                */

                // TODO 2: 그라디언트 브러시 생성
                // 힌트: Brush.sweepGradient(colors, center)
                // 색상 예시: 녹색 -> 노란색 -> 빨간색

                // 정답 (주석 해제하여 확인):
                /*
                val progressGradient = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF4CAF50),  // 녹색
                        Color(0xFFCDDC39),  // 연두색
                        Color(0xFFFFEB3B),  // 노란색
                        Color(0xFFFF9800),  // 주황색
                        Color(0xFFF44336)   // 빨간색
                    ),
                    center = Offset(size.width / 2, size.height / 2)
                )
                */

                // TODO 3: 프로그레스 호 그리기
                // 힌트:
                // - startAngle: -90f (12시 방향에서 시작)
                // - sweepAngle: 360f * progress
                // - style: Stroke(width, cap = StrokeCap.Round)

                // 정답 (주석 해제하여 확인):
                /*
                drawArc(
                    brush = progressGradient,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = arcTopLeft,
                    size = arcSize,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    )
                )
                */
            }

            // 중앙 퍼센트 표시
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 프로그레스 조절 슬라이더
        Text("프로그레스: ${(progress * 100).toInt()}%")
        Slider(
            value = progress,
            onValueChange = { progress = it },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 배경 원: drawArc(360도, 회색)")
                Text("2. 브러시: Brush.sweepGradient(colors)")
                Text("3. 프로그레스 호: drawArc(360 * progress)")
                Text("4. startAngle = -90f (12시 방향)")
                Text("5. useCenter = false (호만 그림)")
                Text("6. StrokeCap.Round (둥근 끝)")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "drawArc 파라미터:\n" +
                    "- brush/color: 색상 또는 그라디언트\n" +
                    "- startAngle: 시작 각도 (3시=0, 12시=-90)\n" +
                    "- sweepAngle: 그릴 각도\n" +
                    "- useCenter: 중심 연결 여부\n" +
                    "- style: Fill or Stroke",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
