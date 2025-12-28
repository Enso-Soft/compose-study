package com.example.launched_effect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 연습 문제 1: 검색 기능 구현
 *
 * 요구사항:
 * - 사용자가 검색어를 입력하면 API를 호출해서 검색 결과를 표시
 * - 검색어가 변경될 때마다 API 호출 (debounce 300ms)
 * - 로딩 상태 표시
 *
 * TODO: LaunchedEffect를 사용해서 구현하세요!
 */
@Composable
fun Practice1_SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // TODO: 여기에 LaunchedEffect를 추가하세요
    // 힌트: key는 searchQuery를 사용
    // 힌트: delay(300)으로 debounce 구현
    // 힌트: searchQuery가 비어있으면 빈 리스트 반환

    /*
    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) {
            searchResults = emptyList()
            return@LaunchedEffect
        }
        delay(300) // debounce
        isLoading = true
        searchResults = searchApiFake(searchQuery)
        isLoading = false
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 검색 기능",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: LaunchedEffect를 사용해서 검색 기능을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("검색어 입력") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
            Text("검색 결과가 없습니다 (LaunchedEffect 구현 필요)")
        } else {
            LazyColumn {
                items(searchResults) { result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = result,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

suspend fun searchApiFake(query: String): List<String> {
    delay(500)
    if (query.isEmpty()) return emptyList()
    return listOf(
        "$query 관련 결과 1",
        "$query 관련 결과 2",
        "$query 관련 결과 3",
        "$query 관련 결과 4"
    )
}

/**
 * 연습 문제 2: 타이머 구현
 *
 * 요구사항:
 * - 화면에 진입하면 0부터 시작하는 타이머 표시
 * - 1초마다 숫자 증가
 * - 타이머 리셋 버튼 (버튼 누르면 0부터 다시 시작)
 *
 * TODO: LaunchedEffect를 사용해서 구현하세요!
 */
@Composable
fun Practice2_TimerScreen() {
    var seconds by remember { mutableIntStateOf(0) }
    var timerKey by remember { mutableIntStateOf(0) }

    // TODO: 여기에 LaunchedEffect를 추가하세요
    // 힌트: key는 timerKey를 사용 (리셋할 때 key 변경)
    // 힌트: while(isActive) 루프 안에서 delay(1000)과 seconds++ 실행
    // 중요: while(true) 대신 while(isActive)를 사용해야 코루틴 취소 시 안전합니다!

    /*
    LaunchedEffect(timerKey) {
        seconds = 0
        while (isActive) {  // isActive: 코루틴이 활성 상태인지 체크
            delay(1000)
            seconds++
        }
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 2: 타이머",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: LaunchedEffect를 사용해서 타이머를 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = formatTime(seconds),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            seconds = 0
            timerKey++
        }) {
            Text("리셋")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "힌트: timerKey가 변경되면 LaunchedEffect 재시작",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}

/**
 * 연습 문제 3: 탭 전환 시 데이터 로드
 *
 * 요구사항:
 * - 3개의 탭이 있음 (전체, 인기, 최신)
 * - 탭을 선택하면 해당 탭의 데이터를 API에서 로드
 * - 같은 탭을 다시 선택하면 API 호출 안 함
 *
 * TODO: LaunchedEffect를 사용해서 구현하세요!
 */
@Composable
fun Practice3_TabScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var tabData by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val tabs = listOf("전체", "인기", "최신")

    // TODO: 여기에 LaunchedEffect를 추가하세요
    // 힌트: key는 selectedTab을 사용

    /*
    LaunchedEffect(selectedTab) {
        isLoading = true
        tabData = fetchTabDataFake(selectedTab)
        isLoading = false
    }
    */

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "연습 3: 탭 데이터 로드",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "TODO: LaunchedEffect를 사용해서 탭 데이터 로드를 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (tabData.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("데이터 없음 (LaunchedEffect 구현 필요)")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                items(tabData) { item ->
                    Text(
                        text = item,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

suspend fun fetchTabDataFake(tabIndex: Int): List<String> {
    delay(800)
    val tabName = when (tabIndex) {
        0 -> "전체"
        1 -> "인기"
        2 -> "최신"
        else -> "Unknown"
    }
    return (1..10).map { "$tabName 아이템 $it" }
}

/**
 * 연습 문제 4: 고급 - Splash Screen with rememberUpdatedState
 *
 * 요구사항:
 * - 3초 후에 onTimeout 콜백 호출
 * - 하지만 3초 안에 onTimeout이 변경되어도 LaunchedEffect는 재시작하지 않음
 * - 3초 후에는 최신 onTimeout을 호출해야 함
 *
 * TODO: rememberUpdatedState를 사용해서 구현하세요!
 *
 * 힌트:
 * - rememberUpdatedState(onTimeout)으로 최신 콜백 참조
 * - LaunchedEffect(Unit)으로 한 번만 실행
 */
@Composable
fun Practice4_SplashScreen(
    onTimeout: () -> Unit = {}
) {
    var countdown by remember { mutableIntStateOf(3) }
    var callbackVersion by remember { mutableIntStateOf(1) }

    // TODO: 여기에 rememberUpdatedState를 추가하세요
    // val currentOnTimeout by rememberUpdatedState(onTimeout)

    // TODO: 여기에 LaunchedEffect를 추가하세요
    // 힌트: key는 Unit 사용 (한 번만 실행)
    // 힌트: 1초마다 countdown 감소
    // 힌트: countdown이 0이 되면 currentOnTimeout() 호출

    /*
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        currentOnTimeout()
    }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "연습 4: Splash Screen (고급)",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: rememberUpdatedState를 사용해서 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = countdown.toString(),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "현재 콜백 버전: $callbackVersion",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { callbackVersion++ }) {
            Text("콜백 버전 변경 (재시작 없이 최신 값 사용)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- rememberUpdatedState: 값이 변경되어도 effect 재시작 안 함")
                Text("- 하지만 effect 내에서 항상 최신 값을 참조할 수 있음")
                Text("- Splash, Timeout 등 한 번만 실행되어야 하는 effect에 유용")
            }
        }
    }
}
