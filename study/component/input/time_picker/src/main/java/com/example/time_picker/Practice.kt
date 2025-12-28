package com.example.time_picker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 1: 기본 TimePicker 표시
 *
 * 요구사항:
 * - 현재 시간으로 초기화된 TimePicker 표시
 * - 선택된 시간을 Text로 보여주기
 * - 24시간 형식 사용
 *
 * 힌트:
 * - Calendar.getInstance()로 현재 시간 가져오기
 * - rememberTimePickerState 사용
 * - state.hour, state.minute로 선택된 시간 읽기
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicTimePicker() {
    // TODO: Calendar.getInstance()로 현재 시간 가져오기
    // val currentTime = Calendar.getInstance()

    // TODO: rememberTimePickerState로 상태 생성
    // val timePickerState = rememberTimePickerState(
    //     initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
    //     initialMinute = currentTime.get(Calendar.MINUTE),
    //     is24Hour = true,
    // )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "연습 1: 기본 TimePicker 표시",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        - 현재 시간으로 초기화된 TimePicker 표시
                        - 선택된 시간을 Text로 보여주기

                        구현 단계:
                        1. Calendar.getInstance() 호출
                        2. rememberTimePickerState로 상태 생성
                        3. TimePicker 컴포넌트 표시
                        4. state.hour, state.minute로 결과 표시
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 구현 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "알람 시간 설정",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // TODO: TimePicker 추가
                // TimePicker(state = timePickerState)

                // 임시 안내 메시지
                Text(
                    text = "여기에 TimePicker를 추가하세요",
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // TODO: 선택된 시간 표시
                // Text("선택된 시간: ${String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)}")

                Text(
                    text = "선택된 시간: --:--",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // 정답 코드 구조
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 구조",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicTimePicker() {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Column(...) {
        TimePicker(state = timePickerState)

        Text(
            text = String.format(
                "선택된 시간: %02d:%02d",
                timePickerState.hour,
                timePickerState.minute
            )
        )
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

/**
 * 연습 2: TimePickerDialog 구현
 *
 * 요구사항:
 * - "시간 선택" 버튼 클릭 시 다이얼로그 표시
 * - AlertDialog 안에 TimePicker 배치
 * - 확인 클릭 시 선택된 시간 저장
 * - 취소 클릭 시 다이얼로그만 닫기
 *
 * 힌트:
 * - showDialog 상태 변수 필요
 * - AlertDialog의 text 파라미터에 TimePicker 배치
 * - confirmButton에서 state.hour, state.minute 읽기
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_TimePickerDialog() {
    // TODO: 다이얼로그 표시 상태
    // var showDialog by remember { mutableStateOf(false) }

    // TODO: 선택된 시간 저장
    // var selectedHour by remember { mutableIntStateOf(9) }
    // var selectedMinute by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "연습 2: TimePickerDialog 구현",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        - "시간 선택" 버튼 클릭 시 다이얼로그 표시
                        - 확인 → 시간 저장, 취소 → 닫기만

                        구현 단계:
                        1. showDialog 상태 선언
                        2. selectedHour, selectedMinute 상태 선언
                        3. Button onClick에서 showDialog = true
                        4. if (showDialog) { AlertDialog(...) }
                        5. confirmButton에서 시간 저장 후 닫기
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 현재 시간 표시 및 버튼
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "알람 설정",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "현재 알람 시간",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // TODO: 선택된 시간 표시
                        // Text(String.format("%02d:%02d", selectedHour, selectedMinute))
                        Text(
                            text = "--:--",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    // TODO: 버튼 클릭 시 showDialog = true
                    Button(
                        onClick = {
                            // showDialog = true
                        }
                    ) {
                        Text("시간 선택")
                    }
                }
            }
        }

        // 정답 코드 구조
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 구조",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
var showDialog by remember { mutableStateOf(false) }
var selectedHour by remember { mutableIntStateOf(9) }
var selectedMinute by remember { mutableIntStateOf(0) }

Button(onClick = { showDialog = true }) {
    Text("시간 선택")
}

if (showDialog) {
    val timePickerState = rememberTimePickerState(
        initialHour = selectedHour,
        initialMinute = selectedMinute,
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text("시간 선택") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(onClick = {
                selectedHour = timePickerState.hour
                selectedMinute = timePickerState.minute
                showDialog = false
            }) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = { showDialog = false }) {
                Text("취소")
            }
        }
    )
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }

    // TODO: AlertDialog 구현
    // if (showDialog) {
    //     val timePickerState = rememberTimePickerState(...)
    //     AlertDialog(...)
    // }
}

/**
 * 연습 3: 알람 시간 범위 설정
 *
 * 요구사항:
 * - "시작 시간"과 "종료 시간" 두 개의 시간 설정
 * - 각각 버튼 클릭 시 TimePickerDialog 표시
 * - TimePicker와 TimeInput 간 전환 가능
 * - 설정된 시간 범위 표시 (예: "09:00 - 18:00")
 *
 * 힌트:
 * - 시작/종료 시간 각각의 상태 필요
 * - showDial 상태로 TimePicker/TimeInput 전환
 * - 어떤 시간을 편집 중인지 추적하는 상태 필요 (editingStart: Boolean)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_AlarmTimeRange() {
    // TODO: 시작/종료 시간 상태
    // var startHour by remember { mutableIntStateOf(9) }
    // var startMinute by remember { mutableIntStateOf(0) }
    // var endHour by remember { mutableIntStateOf(18) }
    // var endMinute by remember { mutableIntStateOf(0) }

    // TODO: 다이얼로그 상태
    // var showDialog by remember { mutableStateOf(false) }
    // var editingStart by remember { mutableStateOf(true) }  // true면 시작시간, false면 종료시간

    // TODO: 다이얼/입력 전환 상태
    // var showDial by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "연습 3: 알람 시간 범위 설정",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        요구사항:
                        - 시작/종료 시간 각각 설정
                        - TimePicker/TimeInput 전환 기능
                        - 시간 범위 표시 (09:00 - 18:00)

                        구현 단계:
                        1. 시작/종료 시간 상태 선언
                        2. 다이얼로그 표시 + 편집 대상 상태
                        3. 다이얼/입력 전환 상태
                        4. 시간 카드 2개 (시작/종료)
                        5. 다이얼로그에서 전환 아이콘 + TimePicker/TimeInput
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 시간 범위 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "활동 시간",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                // TODO: 시간 범위 표시
                // Text("${String.format("%02d:%02d", startHour, startMinute)} - ${String.format("%02d:%02d", endHour, endMinute)}")
                Text(
                    text = "--:-- - --:--",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // 시작 시간 카드
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "시작 시간",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // TODO: 시작 시간 표시
                    Text(
                        text = "--:--",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                // TODO: 버튼 클릭 시 editingStart = true, showDialog = true
                Button(onClick = { }) {
                    Text("변경")
                }
            }
        }

        // 종료 시간 카드
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "종료 시간",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // TODO: 종료 시간 표시
                    Text(
                        text = "--:--",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                // TODO: 버튼 클릭 시 editingStart = false, showDialog = true
                Button(onClick = { }) {
                    Text("변경")
                }
            }
        }

        // 정답 코드 구조
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드 구조",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// 시작/종료 시간 상태
var startHour by remember { mutableIntStateOf(9) }
var startMinute by remember { mutableIntStateOf(0) }
var endHour by remember { mutableIntStateOf(18) }
var endMinute by remember { mutableIntStateOf(0) }

// 다이얼로그 상태
var showDialog by remember { mutableStateOf(false) }
var editingStart by remember { mutableStateOf(true) }
var showDial by remember { mutableStateOf(true) }

// 다이얼로그
if (showDialog) {
    val initialHour = if (editingStart) startHour else endHour
    val initialMinute = if (editingStart) startMinute else endMinute

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = {
            Row(...) {
                Text(if (editingStart) "시작 시간" else "종료 시간")
                IconButton(onClick = { showDial = !showDial }) {
                    Icon(
                        if (showDial) Icons.Default.Edit
                        else Icons.Default.AccessTime
                    )
                }
            }
        },
        text = {
            if (showDial) {
                TimePicker(state = timePickerState)
            } else {
                TimeInput(state = timePickerState)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (editingStart) {
                    startHour = timePickerState.hour
                    startMinute = timePickerState.minute
                } else {
                    endHour = timePickerState.hour
                    endMinute = timePickerState.minute
                }
                showDialog = false
            }) { Text("확인") }
        },
        dismissButton = { ... }
    )
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }

    // TODO: 시간 범위 선택 다이얼로그
    // if (showDialog) {
    //     ...
    // }
}
