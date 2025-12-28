package com.example.remember

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: remember 없이 상태 관리의 문제점
 *
 * 이 화면에서는 remember를 사용하지 않았을 때 발생하는 문제를 보여줍니다:
 * 1. 일반 변수: 상태 변경이 UI에 반영되지 않음
 * 2. mutableStateOf만 사용: Recomposition마다 상태 초기화
 */

// Recomposition 횟수 추적용 (전역 변수로 시뮬레이션)
private var globalRecompositionCount = 0

@Composable
fun ProblemScreen() {
    var forceRecomposition by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적
    SideEffect {
        globalRecompositionCount++
    }

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
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "remember 없이 상태를 관리하면 어떤 문제가 발생하는지 확인해보세요.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: 일반 변수
        Text(
            text = "문제 1: 일반 변수 사용",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        BrokenCounter1()

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "왜 안 될까요?",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "일반 변수(var count = 0)는 Compose가 관찰하지 않습니다. " +
                            "값이 변경되어도 UI가 업데이트되지 않습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // 문제 2: remember 없는 mutableStateOf
        Text(
            text = "문제 2: remember 없는 mutableStateOf",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        BrokenCounter2(forceRecomposition)

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "왜 안 될까요?",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "mutableStateOf는 상태 변경을 감지하지만, remember 없이는 " +
                            "매 Recomposition마다 새로운 State 객체가 생성되어 0으로 초기화됩니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        // Recomposition 강제 트리거 버튼
        Text(
            text = "Recomposition 테스트",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "현재 Recomposition 횟수: $globalRecompositionCount",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { forceRecomposition++ }
                ) {
                    Text("Recomposition 트리거")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "이 버튼을 누르면 전체 화면이 Recomposition됩니다.\n" +
                            "위의 '문제 2' 카운터가 어떻게 되는지 확인해보세요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

/**
 * 문제 1: 일반 변수 사용
 * 버튼을 클릭해도 카운터가 증가하지 않습니다.
 */
@Composable
fun BrokenCounter1() {
    // 일반 변수 - Compose가 관찰하지 않음
    var count = 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "카운터: $count",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        count++
                        // 값은 증가하지만 UI에 반영되지 않음
                        println("BrokenCounter1: count = $count (UI에 반영 안 됨)")
                    }
                ) {
                    Text("+1")
                }
                Button(
                    onClick = { count-- }
                ) {
                    Text("-1")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "var count = 0  // 일반 변수",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
    }
}

/**
 * 문제 2: remember 없는 mutableStateOf
 * 클릭하면 증가하지만 Recomposition 시 0으로 초기화됩니다.
 */
@Composable
fun BrokenCounter2(externalTrigger: Int) {
    // remember 없이 mutableStateOf만 사용
    // 매 Recomposition마다 새로운 State가 생성됨
    var count by mutableStateOf(0)

    // externalTrigger를 읽어서 이 Composable이 recompose 되도록 함
    val trigger = externalTrigger

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "카운터: $count",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { count++ }
                ) {
                    Text("+1")
                }
                Button(
                    onClick = { count-- }
                ) {
                    Text("-1")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "var count by mutableStateOf(0)  // remember 없음!",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE65100)
            )
            Text(
                text = "(Recomposition 트리거: $trigger)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
