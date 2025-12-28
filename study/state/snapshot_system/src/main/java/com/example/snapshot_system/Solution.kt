package com.example.snapshot_system

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * 해결책 화면
 *
 * snapshotFlow를 사용하여 State 변경을 효율적으로 관찰하는 방법을 시연합니다.
 * - State를 Flow로 변환
 * - debounce, filter 등 Flow 연산자 활용
 * - 여러 State 조합 관찰
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: snapshotFlow",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "snapshotFlow는 Compose State를 Kotlin Flow로 변환합니다. " +
                            "이를 통해 debounce, filter 등 모든 Flow 연산자를 활용할 수 있습니다. " +
                            "State가 변경될 때마다 새 값이 Flow로 전달됩니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 솔루션 데모 1: snapshotFlow 기본 사용
        SolutionDemo1_BasicSnapshotFlow()

        // 솔루션 데모 2: Flow 연산자 조합
        SolutionDemo2_WithDebounce()

        // 솔루션 데모 3: 여러 State 조합
        SolutionDemo3_CombinedStates()

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. snapshotFlow는 State를 Flow로 변환합니다")
                Text("2. 모든 Flow 연산자(debounce, filter 등) 사용 가능")
                Text("3. LaunchedEffect 내에서 collect해야 합니다")
                Text("4. 여러 State를 블록 내에서 조합할 수 있습니다")
            }
        }

        // 올바른 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// snapshotFlow로 State를 Flow로 변환
LaunchedEffect(Unit) {
    snapshotFlow { query }
        .debounce(500)           // 500ms 대기
        .filter { it.length >= 2 }  // 2글자 이상
        .distinctUntilChanged()  // 중복 제거
        .collect { searchQuery ->
            searchApi(searchQuery)
        }
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
 * 솔루션 데모 1: snapshotFlow 기본 사용법
 *
 * snapshotFlow를 사용하여 카운터 변경을 관찰합니다.
 * 변경 시마다 로그 메시지를 업데이트합니다.
 */
@Composable
private fun SolutionDemo1_BasicSnapshotFlow() {
    var counter by remember { mutableIntStateOf(0) }
    var logMessage by remember { mutableStateOf("아직 변경 없음") }
    var collectCount by remember { mutableIntStateOf(0) }

    // snapshotFlow로 counter 변경을 관찰
    // counter가 변경될 때마다 collect 블록이 실행됨
    LaunchedEffect(Unit) {
        snapshotFlow { counter }  // counter를 Flow로 변환
            .collect { value ->   // 값이 변경될 때마다 실행
                collectCount++
                logMessage = "카운터가 ${value}로 변경됨 (collect #${collectCount})"
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
                text = "데모 1: snapshotFlow 기본 사용",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { counter++ }) {
                Text("카운트: $counter")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = logMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "snapshotFlow가 변경을 자동으로 감지합니다",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 솔루션 데모 2: snapshotFlow + Flow 연산자
 *
 * debounce와 filter를 사용하여 효율적인 검색을 구현합니다.
 * 500ms 동안 입력이 없을 때만 처리하고, 2글자 이상일 때만 검색합니다.
 */
@OptIn(FlowPreview::class)
@Composable
private fun SolutionDemo2_WithDebounce() {
    var query by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf("검색어를 입력하세요 (2글자 이상)") }
    var apiCallCount by remember { mutableIntStateOf(0) }
    var isSearching by remember { mutableStateOf(false) }

    // snapshotFlow + Flow 연산자 조합
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(500)              // 500ms 동안 추가 입력이 없을 때만 처리
            .filter { it.length >= 2 }  // 2글자 이상일 때만
            .distinctUntilChanged()     // 같은 값은 무시
            .collect { searchQuery ->
                isSearching = true
                apiCallCount++
                // 실제로는 여기서 API 호출
                // val results = searchRepository.search(searchQuery)
                searchResult = "검색 결과: '$searchQuery' (API 호출 #$apiCallCount)"
                isSearching = false
            }
    }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "데모 2: debounce + filter 적용",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("검색어 입력 (빠르게 타이핑해보세요)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isSearching) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }

            Text(
                text = searchResult,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "총 API 호출 횟수: $apiCallCount (입력한 글자 수보다 훨씬 적음!)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "500ms 대기 + 2글자 이상 + 중복 제거",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 솔루션 데모 3: 여러 State 조합 관찰
 *
 * snapshotFlow 블록 내에서 여러 State를 읽으면
 * 어느 하나라도 변경될 때 collect가 실행됩니다.
 */
@OptIn(FlowPreview::class)
@Composable
private fun SolutionDemo3_CombinedStates() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var combinedResult by remember { mutableStateOf("이름을 입력하세요") }
    var combineCount by remember { mutableIntStateOf(0) }

    // 여러 State를 조합하여 관찰
    // firstName 또는 lastName 중 하나라도 변경되면 collect 실행
    LaunchedEffect(Unit) {
        snapshotFlow {
            // 블록 내에서 여러 State를 읽음
            val fullName = "$firstName $lastName".trim()
            fullName  // 조합된 값을 반환
        }
            .debounce(300)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collect { fullName ->
                combineCount++
                combinedResult = "전체 이름: $fullName (조합 #$combineCount)"
            }
    }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "데모 3: 여러 State 조합",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("이름") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("성") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = combinedResult,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "두 필드 중 하나라도 변경되면 조합이 업데이트됩니다",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
