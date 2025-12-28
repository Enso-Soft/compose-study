package com.example.time_picker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar

/**
 * Solution: TimePicker와 TimeInput을 사용한 해결책
 *
 * 핵심 포인트:
 * 1. rememberTimePickerState로 상태 관리
 * 2. TimePicker (다이얼) vs TimeInput (키보드)
 * 3. is24Hour 옵션
 * 4. AlertDialog와 결합
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("다이얼") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("입력") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("형식") }
            )
            FilterChip(
                selected = selectedDemo == 3,
                onClick = { selectedDemo = 3 },
                label = { Text("Dialog") }
            )
        }

        when (selectedDemo) {
            0 -> BasicTimePickerDemo()
            1 -> TimeInputDemo()
            2 -> TimeFormatDemo()
            3 -> TimePickerDialogDemo()
        }
    }
}

/**
 * 데모 1: 기본 TimePicker (다이얼 형태)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTimePickerDemo() {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "해결책: TimePicker 사용",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        TimePicker는 다이얼(시계 모양)을 돌려 시간을 선택합니다.

                        장점:
                        - 입력 검증이 필요 없음 (올바른 값만 선택 가능)
                        - AM/PM 자동 처리
                        - 직관적인 터치 UX
                        - 코드가 매우 간결함

                        아래 다이얼을 돌려 시간을 선택해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // TimePicker
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

                TimePicker(state = timePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                // 선택된 시간 표시
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "선택된 시간: ${String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "간결한 코드",
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
val currentTime = Calendar.getInstance()
val timePickerState = rememberTimePickerState(
    initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
    initialMinute = currentTime.get(Calendar.MINUTE),
    is24Hour = true,
)

TimePicker(state = timePickerState)

// 선택된 시간 읽기
val hour = timePickerState.hour      // 0-23
val minute = timePickerState.minute  // 0-59
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
 * 데모 2: TimeInput (키보드 입력 형태)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputDemo() {
    val timePickerState = rememberTimePickerState(
        initialHour = 9,
        initialMinute = 30,
        is24Hour = true,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "TimeInput: 키보드 입력",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        TimeInput은 숫자를 직접 입력해서 시간을 설정합니다.

                        특징:
                        - TimePicker와 같은 상태(TimePickerState) 사용
                        - 키보드 입력이 편한 상황에 적합
                        - 자동 범위 검증 (올바른 값만 입력됨)
                        - 컴팩트한 UI

                        시간을 직접 입력해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // TimeInput
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "시간 직접 입력",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                TimeInput(state = timePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "선택된 시간: ${String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // TimePicker vs TimeInput 비교
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TimePicker vs TimeInput",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "TimePicker",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "- 다이얼 UI\n- 터치로 선택\n- 시각적\n- 넓은 공간 필요",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "TimeInput",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "- 입력 필드 UI\n- 키보드로 입력\n- 컴팩트\n- 정확한 값 입력",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TimeInput 코드",
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
// TimePickerState는 동일하게 사용!
val timePickerState = rememberTimePickerState(
    initialHour = 9,
    initialMinute = 30,
    is24Hour = true,
)

// TimePicker 대신 TimeInput 사용
TimeInput(state = timePickerState)

// 선택된 시간 읽기도 동일
val hour = timePickerState.hour
val minute = timePickerState.minute
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
 * 데모 3: 24시간 vs 12시간 형식
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeFormatDemo() {
    var is24Hour by remember { mutableStateOf(true) }

    // key를 사용해서 형식 변경 시 상태 재생성
    val timePickerState = rememberTimePickerState(
        initialHour = 14,
        initialMinute = 30,
        is24Hour = is24Hour,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "24시간 vs 12시간 형식",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        is24Hour 파라미터로 시간 형식을 설정합니다.

                        - is24Hour = true: 24시간 형식 (14:30)
                        - is24Hour = false: 12시간 형식 (2:30 PM)

                        토글을 눌러 형식 변화를 확인해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 형식 토글
        Card {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "24시간 형식",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = is24Hour,
                    onCheckedChange = { is24Hour = it }
                )
            }
        }

        // TimePicker (형식에 따라 변경)
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (is24Hour) "24시간 형식" else "12시간 형식 (AM/PM)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                key(is24Hour) {
                    TimePicker(
                        state = rememberTimePickerState(
                            initialHour = 14,
                            initialMinute = 30,
                            is24Hour = is24Hour,
                        )
                    )
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "형식 설정 코드",
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
// 24시간 형식
val state24 = rememberTimePickerState(
    initialHour = 14,
    initialMinute = 30,
    is24Hour = true,  // 14:30으로 표시
)

// 12시간 형식 (AM/PM 토글 표시)
val state12 = rememberTimePickerState(
    initialHour = 14,
    initialMinute = 30,
    is24Hour = false, // 2:30 PM으로 표시
)

// 주의: 내부적으로 hour는 항상 0-23
// state.hour == 14 (두 경우 모두)
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
 * 데모 4: TimePickerDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogDemo() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableIntStateOf(9) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "TimePickerDialog 패턴",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        실제 앱에서 가장 일반적인 패턴입니다.

                        흐름:
                        1. 버튼 클릭 → 다이얼로그 열기
                        2. TimePicker로 시간 선택
                        3. 확인 → 선택된 시간 저장
                        4. 취소 → 다이얼로그만 닫기

                        "시간 변경" 버튼을 눌러보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 현재 알람 시간
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
                        Text(
                            text = String.format("%02d:%02d", selectedHour, selectedMinute),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("시간 변경")
                    }
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TimePickerDialog 코드",
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
        text = { TimePicker(state = timePickerState) },
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

    // TimePickerDialog
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
                TextButton(
                    onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        showDialog = false
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}
