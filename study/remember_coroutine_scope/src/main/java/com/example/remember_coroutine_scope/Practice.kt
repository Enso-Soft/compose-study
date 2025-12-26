package com.example.remember_coroutine_scope

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 연습 문제 1: Snackbar 표시하기
 *
 * 요구사항:
 * - 버튼을 클릭하면 Snackbar로 메시지를 표시
 * - rememberCoroutineScope를 사용하여 showSnackbar 호출
 *
 * 난이도: 기본
 */
@Composable
fun Practice1_SnackbarScreen() {
    val snackbarHostState = remember { SnackbarHostState() }

    // TODO: rememberCoroutineScope()를 사용하여 스코프를 생성하세요
    // 힌트: val scope = rememberCoroutineScope()

    /*
    val scope = rememberCoroutineScope()
    */

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "연습 1: Snackbar 표시",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "TODO: rememberCoroutineScope를 사용하여 Snackbar를 표시하세요",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // TODO: scope.launch 내에서 snackbarHostState.showSnackbar() 호출
                    // 힌트:
                    // scope.launch {
                    //     snackbarHostState.showSnackbar("메시지 표시!")
                    // }

                    /*
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "rememberCoroutineScope로 Snackbar 표시!",
                            duration = SnackbarDuration.Short
                        )
                    }
                    */
                }
            ) {
                Text("Snackbar 표시")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 힌트 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "힌트",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. rememberCoroutineScope()로 스코프 생성")
                    Text("2. onClick 내에서 scope.launch { } 사용")
                    Text("3. snackbarHostState.showSnackbar()는 suspend 함수")
                }
            }
        }
    }
}

/**
 * 연습 문제 2: 카운트다운 타이머
 *
 * 요구사항:
 * - 시작 버튼 클릭 시 10초 카운트다운 시작
 * - 정지 버튼으로 카운트다운 일시 정지
 * - 1초마다 숫자 감소
 * - 0에 도달하면 "완료!" 표시
 *
 * 난이도: 중급
 */
@Composable
fun Practice2_CountdownScreen() {
    var countdown by remember { mutableIntStateOf(10) }
    var isRunning by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    // TODO: rememberCoroutineScope()를 사용하여 스코프를 생성하세요

    /*
    val scope = rememberCoroutineScope()
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 2: 카운트다운 타이머",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: rememberCoroutineScope로 카운트다운을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = formatTime(countdown),
            style = MaterialTheme.typography.displayLarge
        )

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    // TODO: 카운트다운 시작
                    // 힌트:
                    // isRunning = true
                    // message = ""
                    // scope.launch {
                    //     while (countdown > 0 && isRunning) {
                    //         delay(1000)
                    //         countdown--
                    //     }
                    //     if (countdown == 0) {
                    //         message = "완료!"
                    //     }
                    //     isRunning = false
                    // }

                    /*
                    isRunning = true
                    message = ""
                    scope.launch {
                        while (countdown > 0 && isRunning) {
                            delay(1000)
                            countdown--
                        }
                        if (countdown == 0) {
                            message = "완료!"
                        }
                        isRunning = false
                    }
                    */
                },
                enabled = !isRunning && countdown > 0
            ) {
                Text("시작")
            }

            Button(
                onClick = {
                    isRunning = false
                },
                enabled = isRunning
            ) {
                Text("정지")
            }

            Button(onClick = {
                countdown = 10
                isRunning = false
                message = ""
            }) {
                Text("리셋")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. scope.launch 내에서 while 루프 사용")
                Text("2. delay(1000)으로 1초 대기")
                Text("3. isRunning 플래그로 정지 구현")
                Text("4. countdown == 0일 때 완료 메시지")
            }
        }
    }
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

/**
 * 연습 문제 3: 취소 가능한 장시간 작업
 *
 * 요구사항:
 * - 시작 버튼 클릭 시 5초 작업 시작
 * - 취소 버튼으로 작업 중단 가능
 * - Job을 저장하여 수동 취소 구현
 * - 작업 상태 표시 (대기중/진행중/완료/취소됨)
 *
 * 난이도: 심화
 */
@Composable
fun Practice3_CancellableTaskScreen() {
    var status by remember { mutableStateOf("대기중") }
    var progress by remember { mutableIntStateOf(0) }

    // TODO: rememberCoroutineScope()를 사용하여 스코프를 생성하세요
    // TODO: Job을 저장할 상태를 만드세요
    // 힌트: var job by remember { mutableStateOf<Job?>(null) }

    /*
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 3: 취소 가능한 작업",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: Job을 저장하여 취소 가능한 작업을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 진행 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (status) {
                    "진행중" -> MaterialTheme.colorScheme.primaryContainer
                    "완료" -> MaterialTheme.colorScheme.tertiaryContainer
                    "취소됨" -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.headlineMedium
                )

                if (status == "진행중") {
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${progress}%")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    // TODO: 장시간 작업 시작
                    // 힌트:
                    // job = scope.launch {
                    //     status = "진행중"
                    //     for (i in 1..100) {
                    //         if (!isActive) {
                    //             status = "취소됨"
                    //             return@launch
                    //         }
                    //         delay(50)
                    //         progress = i
                    //     }
                    //     status = "완료"
                    // }

                    /*
                    job = scope.launch {
                        status = "진행중"
                        progress = 0
                        for (i in 1..100) {
                            if (!isActive) {
                                status = "취소됨"
                                return@launch
                            }
                            delay(50)
                            progress = i
                        }
                        status = "완료"
                    }
                    */
                },
                enabled = status != "진행중"
            ) {
                Text("시작")
            }

            Button(
                onClick = {
                    // TODO: 작업 취소
                    // 힌트: job?.cancel()

                    /*
                    job?.cancel()
                    status = "취소됨"
                    */
                },
                enabled = status == "진행중"
            ) {
                Text("취소")
            }

            Button(onClick = {
                status = "대기중"
                progress = 0
            }) {
                Text("리셋")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. var job by remember { mutableStateOf<Job?>(null) }")
                Text("2. job = scope.launch { ... } 로 Job 저장")
                Text("3. job?.cancel()로 작업 취소")
                Text("4. isActive로 취소 여부 확인")
            }
        }
    }
}
