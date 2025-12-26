package com.example.state_hoisting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Solution: State Hoisting으로 올바른 상태 관리
 *
 * 이 화면에서는 State Hoisting 패턴을 사용하여
 * 재사용 가능하고 테스트하기 쉬운 컴포넌트를 만드는 방법을 보여줍니다.
 */

@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("기본 패턴") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("상태 공유") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("상태 홀더") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> Solution1_BasicPattern()
            1 -> Solution2_SharedState()
            2 -> Solution3_StateHolder()
        }
    }
}

/**
 * 솔루션 1: 기본 State Hoisting 패턴
 *
 * Stateless 컴포넌트: value + onValueChange 매개변수
 */
@Composable
fun Solution1_BasicPattern() {
    // 상태 소유자 (State Owner)
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: State Hoisting 패턴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 상태는 상위 컴포넌트에서 관리\n" +
                            "2. 값은 매개변수로 전달 (State flows down)\n" +
                            "3. 이벤트는 콜백으로 위임 (Events flow up)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Stateless Counter 사용
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "StatelessCounter 사용",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Stateless 컴포넌트 사용
                StatelessCounter(
                    count = count,
                    onIncrement = { count++ },
                    onDecrement = { count-- }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 부모에서 제어 가능
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { count = 0 }) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("리셋")
                    }
                    OutlinedButton(onClick = { count = 100 }) {
                        Text("100으로 설정")
                    }
                }
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
                    text = "Stateless 컴포넌트 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun StatelessCounter(
    count: Int,              // State down
    onIncrement: () -> Unit, // Event up
    onDecrement: () -> Unit, // Event up
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Button(onClick = onDecrement) { Text("-") }
        Text("${'$'}count")
        Button(onClick = onIncrement) { Text("+") }
    }
}

// 사용법 (부모에서 상태 관리)
var count by remember { mutableIntStateOf(0) }
StatelessCounter(
    count = count,
    onIncrement = { count++ },
    onDecrement = { count-- }
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 장점 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "장점",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 재사용 가능: 어디서든 다른 상태와 함께 사용\n" +
                            "2. 제어 가능: 부모에서 리셋, 특정 값 설정 가능\n" +
                            "3. 테스트 용이: 입력과 콜백만으로 동작 검증\n" +
                            "4. 예측 가능: 단방향 데이터 흐름으로 디버깅 쉬움",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Stateless Counter 컴포넌트
 * 상태를 외부에서 받고, 이벤트를 외부로 위임
 */
@Composable
fun StatelessCounter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledTonalButton(onClick = onDecrement) {
            Text("-", style = MaterialTheme.typography.titleLarge)
        }
        Text(
            text = "$count",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(100.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = onIncrement) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}

/**
 * 솔루션 2: 상태 공유
 *
 * 여러 컴포넌트가 동일한 상태를 공유하도록 State Hoisting 적용
 */
@Composable
fun Solution2_SharedState() {
    // 공유 상태: 모든 하위 컴포넌트가 이 상태를 사용
    var sharedCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: 상태 공유",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "State Hoisting으로 여러 컴포넌트가\n" +
                            "동일한 상태를 공유할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Display 컴포넌트 (읽기 전용)
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
                    text = "SharedCountDisplay (같은 상태 사용)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                SharedCountDisplay(count = sharedCount)
            }
        }

        // Control 컴포넌트 (읽기 + 쓰기)
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
                    text = "SharedCountControl (같은 상태 변경)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                SharedCountControl(
                    count = sharedCount,
                    onIncrement = { sharedCount++ },
                    onDecrement = { sharedCount-- }
                )
            }
        }

        // 슬라이더 컨트롤 (또 다른 방식의 조작)
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
                    text = "슬라이더로도 같은 상태 변경 가능!",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = sharedCount.toFloat(),
                    onValueChange = { sharedCount = it.toInt() },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth()
                )
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
                    text = "상태 공유 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 부모에서 상태 소유
var sharedCount by remember { mutableIntStateOf(0) }

// 여러 컴포넌트에 동일한 상태 전달
SharedCountDisplay(count = sharedCount)  // 읽기
SharedCountControl(
    count = sharedCount,
    onIncrement = { sharedCount++ },
    onDecrement = { sharedCount-- }
)
Slider(
    value = sharedCount.toFloat(),
    onValueChange = { sharedCount = it.toInt() }
)

// 모두 동기화됨!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun SharedCountDisplay(count: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = "현재 값: $count",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun SharedCountControl(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Control 표시: $count",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onDecrement) {
                Text("-10")
            }
            FilledTonalButton(onClick = onDecrement) {
                Text("-1")
            }
            FilledTonalButton(onClick = onIncrement) {
                Text("+1")
            }
            Button(onClick = onIncrement) {
                Text("+10")
            }
        }
    }
}

/**
 * 솔루션 3: 상태 홀더 클래스
 *
 * 복잡한 상태를 클래스로 캡슐화하여 관리
 */
@Composable
fun Solution3_StateHolder() {
    // 상태 홀더 사용
    val formState = rememberFormState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: 상태 홀더 클래스",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "복잡한 상태는 클래스로 캡슐화하여\n" +
                            "관련 상태와 로직을 함께 관리합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 폼 입력
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "회원가입 폼",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = formState.name,
                    onValueChange = { formState.name = it },
                    label = { Text("이름") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = formState.email,
                    onValueChange = { formState.email = it },
                    label = { Text("이메일") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = formState.password,
                    onValueChange = { formState.password = it },
                    label = { Text("비밀번호") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { formState.reset() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("초기화")
                    }
                    Button(
                        onClick = { /* 제출 로직 */ },
                        modifier = Modifier.weight(1f),
                        enabled = formState.isValid
                    ) {
                        Text("가입하기")
                    }
                }
            }
        }

        // 유효성 상태 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (formState.isValid)
                    Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (formState.isValid) "폼이 유효합니다!" else "폼을 완성해주세요",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "이름: ${if (formState.name.isNotBlank()) "O" else "X"}\n" +
                            "이메일 (@포함): ${if (formState.email.contains("@")) "O" else "X"}\n" +
                            "비밀번호 (6자 이상): ${if (formState.password.length >= 6) "O" else "X"}",
                    style = MaterialTheme.typography.bodySmall
                )
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
                    text = "상태 홀더 클래스 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
class FormState {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    
    val isValid: Boolean
        get() = name.isNotBlank() 
            && email.contains("@")
            && password.length >= 6
    
    fun reset() {
        name = ""
        email = ""
        password = ""
    }
}

@Composable
fun rememberFormState() = remember { FormState() }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 장점 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "상태 홀더의 장점",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 관련 상태를 하나의 클래스로 그룹화\n" +
                            "2. 유효성 검사 등 로직을 함께 캡슐화\n" +
                            "3. reset() 같은 편의 메서드 제공\n" +
                            "4. 단위 테스트 용이 (UI 없이 테스트 가능)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 상태 홀더 클래스
 * 복잡한 폼 상태를 캡슐화
 */
class FormState {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    val isValid: Boolean
        get() = name.isNotBlank() &&
                email.contains("@") &&
                password.length >= 6

    fun reset() {
        name = ""
        email = ""
        password = ""
    }
}

@Composable
fun rememberFormState(): FormState = remember { FormState() }
