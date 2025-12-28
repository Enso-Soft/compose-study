package com.example.remember

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
import androidx.compose.ui.unit.dp

/**
 * Solution: remember + mutableStateOf로 올바른 상태 관리
 *
 * 이 화면에서는 remember와 mutableStateOf를 함께 사용하여
 * 안정적인 상태 관리를 구현하는 방법을 보여줍니다.
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
                label = { Text("기본 카운터") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("= vs by") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("key 활용") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> BasicCounterDemo()
            1 -> SyntaxComparisonDemo()
            2 -> RememberKeyDemo()
        }
    }
}

/**
 * 데모 1: 기본 카운터
 * remember + mutableStateOf로 올바르게 구현된 카운터
 */
@Composable
fun BasicCounterDemo() {
    // 올바른 상태 관리: remember + mutableStateOf
    var count by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적 (참조 객체 패턴 - 무한 루프 방지)
    val recompositionRef = remember { object { var count = 0 } }
    var displayRecompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionRef.count++
    }
    LaunchedEffect(Unit) {
        displayRecompositionCount = recompositionRef.count
    }

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
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("var count by remember { mutableStateOf(0) }")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "remember: Recomposition 간 값 유지\n" +
                            "mutableStateOf: 값 변경 시 UI 자동 업데이트",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 카운터 카드
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
                    text = "카운터",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(onClick = { count-- }) {
                        Text("-1")
                    }
                    Button(onClick = { count++ }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+1")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { count = 0 }) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("초기화")
                }
            }
        }

        // Recomposition 정보
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Recomposition 횟수: $displayRecompositionCount",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "버튼을 클릭할 때마다 Recomposition이 발생하지만,\n" +
                            "remember 덕분에 count 값이 유지됩니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 데모 2: = vs by 문법 비교
 */
@Composable
fun SyntaxComparisonDemo() {
    // 방법 1: = 연산자 (State 객체)
    val countState = remember { mutableStateOf(0) }

    // 방법 2: by 위임 (직접 값 접근)
    var countDelegated by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "두 가지 문법 비교",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "= 방식: State<T> 객체를 직접 다룸\n" +
                            "by 방식: 위임을 통해 값에 직접 접근 (권장)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 방법 1: = 연산자
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
                    text = "방법 1: = 연산자",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "val countState = remember { mutableStateOf(0) }",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1565C0)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "값: ${countState.value}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "접근: countState.value",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { countState.value++ }) {
                    Text("+1 (countState.value++)")
                }
            }
        }

        // 방법 2: by 위임
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
                    text = "방법 2: by 위임 (권장)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "var count by remember { mutableStateOf(0) }",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7B1FA2)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "값: $countDelegated",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "접근: count (직접)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { countDelegated++ }) {
                    Text("+1 (count++)")
                }
            }
        }

        // 비교 표
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "언제 어떤 방식을 사용할까?",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "by 위임: 대부분의 경우 (더 간결)\n" +
                            "= 연산자: State 객체 자체를 다른 Composable에 전달할 때",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 데모 3: remember key 활용
 */
@Composable
fun RememberKeyDemo() {
    var userId by remember { mutableStateOf("user_1") }
    var items by remember { mutableStateOf(listOf(1, 2, 3, 4, 5)) }

    // key를 활용한 계산 캐싱
    // userId가 변경될 때만 재계산
    val userData = remember(userId) {
        println("Computing user data for: $userId")
        "User Data for $userId (computed at ${System.currentTimeMillis() % 10000})"
    }

    // items가 변경될 때만 재계산
    val sum = remember(items) {
        println("Computing sum...")
        items.sum()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "remember(key) 활용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "key가 변경될 때만 람다가 재실행됩니다.\n" +
                            "비용이 큰 계산을 캐싱할 때 유용합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // User ID 변경 데모
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예시 1: 사용자 ID 기반 캐싱",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "remember(userId) { computeUserData(userId) }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "현재 userId: $userId")
                Text(text = "캐시된 데이터: $userData")
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { userId = "user_1" }) {
                        Text("user_1")
                    }
                    Button(onClick = { userId = "user_2" }) {
                        Text("user_2")
                    }
                    Button(onClick = { userId = "user_3" }) {
                        Text("user_3")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "같은 버튼을 여러 번 눌러보세요. 타임스탬프가 변하지 않습니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        // 합계 계산 데모
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예시 2: 목록 합계 캐싱",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "remember(items) { items.sum() }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "현재 목록: $items")
                Text(
                    text = "합계: $sum",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        items = items + (items.size + 1)
                    }) {
                        Text("항목 추가")
                    }
                    OutlinedButton(onClick = {
                        if (items.isNotEmpty()) {
                            items = items.dropLast(1)
                        }
                    }) {
                        Text("항목 제거")
                    }
                }
            }
        }

        // 핵심 포인트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "remember(key1, key2, ...) { computation }\n\n" +
                            "key가 변경되면: 람다 재실행, 새 값 저장\n" +
                            "key가 동일하면: 캐시된 값 반환 (재계산 안 함)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
