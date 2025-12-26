package com.example.state_hoisting

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
 * Problem: State Hoisting 없이 상태 관리의 문제점
 *
 * 이 화면에서는 State Hoisting을 사용하지 않았을 때 발생하는 문제를 보여줍니다:
 * 1. 재사용 불가능한 컴포넌트
 * 2. 상태 동기화 불가
 * 3. 외부에서 제어 불가
 */

@Composable
fun ProblemScreen() {
    var selectedProblem by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedProblem == 0,
                onClick = { selectedProblem = 0 },
                label = { Text("재사용 불가") }
            )
            FilterChip(
                selected = selectedProblem == 1,
                onClick = { selectedProblem = 1 },
                label = { Text("동기화 불가") }
            )
            FilterChip(
                selected = selectedProblem == 2,
                onClick = { selectedProblem = 2 },
                label = { Text("제어 불가") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedProblem) {
            0 -> Problem1_NotReusable()
            1 -> Problem2_NotSynced()
            2 -> Problem3_NotControllable()
        }
    }
}

/**
 * 문제 1: 재사용 불가능한 Stateful 컴포넌트
 *
 * StatefulCounter는 내부에서 상태를 관리하므로:
 * - 초기값을 외부에서 설정할 수 없음
 * - 다른 화면에서 다른 용도로 사용 불가
 */
@Composable
fun Problem1_NotReusable() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                    text = "문제 1: 재사용 불가능한 컴포넌트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "내부에서 상태를 관리하면 외부에서 초기값을 설정하거나 " +
                            "상태를 제어할 수 없습니다.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Stateful Counter (문제가 있는 컴포넌트)
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
                    text = "StatefulCounter (문제 있음)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatefulCounter()
            }
        }

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun StatefulCounter() {
    // 내부에서 상태 관리 - 외부 제어 불가!
    var count by remember { mutableIntStateOf(0) }
    
    Button(onClick = { count++ }) {
        Text("Count: ${'$'}count")
    }
}

// 사용 시 문제점:
StatefulCounter()  // 초기값 설정 불가
StatefulCounter()  // 리셋 불가
StatefulCounter()  // 현재 값 가져오기 불가
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 시도해보기
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "생각해보기",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "- 위 카운터를 10에서 시작하게 하려면?\n" +
                            "- 부모에서 카운터를 0으로 리셋하려면?\n" +
                            "- 카운터 값을 다른 UI에 표시하려면?\n\n" +
                            "현재 구조로는 모두 불가능합니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제 있는 Stateful 컴포넌트
 * 내부에서 상태를 관리하여 재사용이 어려움
 */
@Composable
fun StatefulCounter() {
    var count by remember { mutableIntStateOf(0) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { count-- }) {
            Text("-")
        }
        Text(
            text = "$count",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.width(60.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Button(onClick = { count++ }) {
            Text("+")
        }
    }
}

/**
 * 문제 2: 상태 동기화 불가
 *
 * 각 컴포넌트가 자체 상태를 가지면 서로 동기화되지 않음
 */
@Composable
fun Problem2_NotSynced() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                    text = "문제 2: 상태 동기화 불가",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Display와 Control이 각자 상태를 가지면 " +
                            "서로 동기화되지 않습니다. 버튼을 눌러도 위의 표시가 변하지 않습니다!",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Display 컴포넌트 (자체 상태)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CountDisplay (자체 상태)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                CountDisplay()
            }
        }

        // Control 컴포넌트 (별도의 자체 상태)
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
                    text = "CountControl (별도의 자체 상태)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                CountControl()
            }
        }

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun CountDisplay() {
    var count by remember { mutableIntStateOf(0) }
    Text("Display: ${'$'}count")  // 항상 0
}

@Composable
fun CountControl() {
    var count by remember { mutableIntStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Control: ${'$'}count")  // 증가하지만...
    }
}

// Display와 Control은 서로 다른 상태!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 핵심 메시지
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "두 컴포넌트가 동일한 상태를 공유하려면,\n" +
                            "상태를 공통 부모로 끌어올려야 합니다! (State Hoisting)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun CountDisplay() {
    // 자체 상태 - CountControl과 동기화되지 않음
    var count by remember { mutableIntStateOf(0) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = "표시된 값: $count",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun CountControl() {
    // 별도의 자체 상태 - CountDisplay와 동기화되지 않음
    var count by remember { mutableIntStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Control 내부 값: $count",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { count-- }) {
                Text("-1")
            }
            Button(onClick = { count++ }) {
                Text("+1")
            }
        }
    }
}

/**
 * 문제 3: 외부에서 제어 불가
 *
 * Stateful 컴포넌트는 부모에서 리셋하거나 특정 값으로 설정할 수 없음
 */
@Composable
fun Problem3_NotControllable() {
    // 부모에서 리셋하고 싶지만 방법이 없음
    var resetKey by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                    text = "문제 3: 외부에서 제어 불가",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Stateful 컴포넌트는 부모에서 상태를 리셋하거나 " +
                            "특정 값으로 설정할 수 없습니다.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Stateful 컴포넌트들
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "각각 카운터를 증가시켜보세요",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // key를 사용해 강제 재생성 (불완전한 해결책)
                key(resetKey) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("카운터 A")
                            StatefulCounter()
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("카운터 B")
                            StatefulCounter()
                        }
                    }
                }
            }
        }

        // 리셋 시도
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "부모에서 리셋하려면?",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Stateful 컴포넌트는 리셋 방법이 없어서\n" +
                            "key를 변경해 컴포넌트를 재생성하는 Hack이 필요합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { resetKey++ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("강제 리셋 (Hack!)")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "resetKey: $resetKey",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Hack 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 불완전한 해결책: key 변경으로 강제 재생성
var resetKey by remember { mutableIntStateOf(0) }

key(resetKey) {
    StatefulCounter()  // resetKey 변경 시 재생성
}

Button(onClick = { resetKey++ }) {
    Text("리셋")
}

// 문제점:
// - 상태뿐만 아니라 컴포넌트 전체가 재생성됨
// - 성능 저하 가능
// - 깔끔하지 않은 패턴
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
