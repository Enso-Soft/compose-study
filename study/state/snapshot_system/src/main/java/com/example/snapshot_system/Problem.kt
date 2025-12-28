package com.example.snapshot_system

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 문제 상황 화면
 *
 * snapshotFlow를 사용하지 않았을 때 발생하는 문제를 시연합니다.
 * - State 변경을 효율적으로 추적하기 어려움
 * - Flow 연산자(debounce, filter)를 활용할 수 없음
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
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "검색 기능을 구현할 때, 사용자가 글자를 입력할 때마다 API를 호출하면 " +
                            "너무 많은 요청이 발생합니다. 디바운스(일정 시간 대기 후 처리)를 " +
                            "적용하고 싶지만, 기존 방식으로는 어렵습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 데모 1: LaunchedEffect with key
        ProblemDemo1_LaunchedEffectWithKey()

        // 문제 데모 2: 수동 상태 추적
        ProblemDemo2_ManualTracking()

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 글자 하나마다 API 호출 발생 (비효율적)")
                Text("2. debounce, filter 등 Flow 연산자 사용 불가")
                Text("3. 수동 추적 코드가 복잡해짐")
                Text("4. 여러 State를 조합하기 어려움")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 예시",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 방법 1: LaunchedEffect(query)
// query가 바뀔 때마다 새 코루틴 시작
LaunchedEffect(query) {
    // 디바운스 적용이 어려움!
    searchApi(query)
}

// 방법 2: 수동 비교
if (query != lastQuery) {
    lastQuery = query
    // Flow 연산자 사용 불가!
}
                    """.trimIndent(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

/**
 * 문제 데모 1: LaunchedEffect를 key로 사용할 때의 문제
 *
 * query가 변경될 때마다 LaunchedEffect가 재시작되어
 * 글자 하나마다 "API 호출"이 발생합니다.
 */
@Composable
private fun ProblemDemo1_LaunchedEffectWithKey() {
    var query by remember { mutableStateOf("") }
    var apiCallCount by remember { mutableIntStateOf(0) }
    var lastCallTime by remember { mutableStateOf("") }

    // 문제: query가 바뀔 때마다 새 LaunchedEffect 실행
    // 디바운스를 적용하려고 delay를 넣어도, 새 글자 입력 시 기존 effect가 취소됨
    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            // delay(500)을 넣어도 새 글자 입력 시 취소되어 의미 없음
            apiCallCount++
            lastCallTime = "호출 시간: ${System.currentTimeMillis() % 100000}"
        }
    }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "데모 1: LaunchedEffect(query) 방식",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("검색어 입력") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // API 호출 횟수 표시 - 빨간색으로 과도한 호출 강조
            Text(
                text = "API 호출 횟수: $apiCallCount",
                color = if (apiCallCount > 3) Color.Red else Color.Unspecified,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = lastCallTime,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "글자를 입력할 때마다 호출 횟수가 증가합니다!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 데모 2: 수동으로 상태 변경을 추적할 때의 문제
 *
 * 이전 값과 비교하여 변경을 감지하는 방식은:
 * - 복잡한 코드가 필요
 * - Flow 연산자를 사용할 수 없음
 */
@Composable
private fun ProblemDemo2_ManualTracking() {
    var counter by remember { mutableIntStateOf(0) }
    var lastLoggedValue by remember { mutableIntStateOf(-1) }
    var changeLog by remember { mutableStateOf("변경 로그가 여기에 표시됩니다") }
    var recompositionCount by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적
    SideEffect {
        recompositionCount++
    }

    // 문제: Recomposition마다 비교해야 함
    // 이 방식은 동작하지만 여러 문제가 있음:
    // 1. Composition 중에 상태 변경 (권장되지 않음)
    // 2. Flow 연산자 사용 불가
    // 3. 복잡한 조건 처리가 어려움
    if (counter != lastLoggedValue) {
        // SideEffect 없이 직접 상태 변경은 권장되지 않음!
        // 하지만 데모를 위해 LaunchedEffect로 우회
        LaunchedEffect(counter) {
            lastLoggedValue = counter
            changeLog = "카운터가 ${counter}로 변경됨 (Recomp: ${recompositionCount})"
        }
    }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "데모 2: 수동 상태 추적 방식",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { counter++ }) {
                Text("카운트: $counter")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(changeLog)
            Text(
                text = "Recomposition 횟수: $recompositionCount",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "이 방식으로는 debounce나 filter를 적용할 수 없습니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
