package com.example.gesture

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - clickable만 사용
 *
 * 이 코드의 문제점:
 * 1. 더블탭 감지 불가 - clickable은 단일 탭만 지원
 * 2. 탭 위치 정보 없음 - 어디를 탭했는지 알 수 없음
 * 3. 드래그 불가 - 요소를 이동시킬 수 없음
 * 4. 멀티터치 불가 - 핀치 줌, 회전 불가능
 */
@Composable
fun ProblemScreen() {
    var tapCount by remember { mutableIntStateOf(0) }
    var zoomed by remember { mutableStateOf(false) }
    var recompositionCount by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적
    SideEffect {
        recompositionCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem Screen",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 1: 더블탭으로 줌하고 싶지만 불가능
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 1: 더블탭 줌 불가",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // clickable은 더블탭을 감지할 수 없음!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(
                            if (zoomed) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable {
                            // 문제: 더블탭을 감지할 수 없음
                            // 단일 탭만 감지됨
                            tapCount++
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "탭 횟수: $tapCount\n(더블탭 감지 불가!)",
                        color = if (zoomed) Color.White else Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "clickable은 단일 탭만 감지합니다",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 2: 탭 위치를 알 수 없음
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 2: 탭 위치 정보 없음",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                var lastTapPosition by remember { mutableStateOf("알 수 없음") }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            // 문제: 탭 위치(Offset)를 알 수 없음!
                            lastTapPosition = "위치 정보 없음 (clickable 한계)"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("탭 위치: $lastTapPosition")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "clickable은 탭 위치(Offset)를 제공하지 않습니다",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 3: 드래그 불가
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 3: 드래그로 이동 불가",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                // 문제: clickable로는 드래그 불가!
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "드래그\n불가",
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "clickable은 드래그 제스처를 지원하지 않습니다",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 왜 문제인가?
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
                Text("1. clickable은 단일 탭만 감지 (더블탭, 롱프레스 미지원)")
                Text("2. 탭 위치(Offset) 정보를 제공하지 않음")
                Text("3. 드래그, 스와이프 제스처 미지원")
                Text("4. 멀티터치(핀치 줌, 회전) 불가능")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "→ pointerInput + detectGestures 필요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recomposition 횟수: $recompositionCount",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
