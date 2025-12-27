package com.example.effect_handlers_advanced

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * 연습 문제 1: snapshotFlow로 검색어 디바운싱
 *
 * 목표: TextField 입력 시 300ms 디바운스 후 검색 실행
 *
 * 구현 요구사항:
 * 1. snapshotFlow로 searchQuery 상태를 Flow로 변환
 * 2. 300ms debounce 적용
 * 3. 빈 문자열 필터링
 * 4. distinctUntilChanged로 중복 방지
 * 5. 검색 결과를 searchResults에 저장
 */
@OptIn(FlowPreview::class)
@Composable
fun Practice1_SearchWithDebounce() {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    var searchCount by remember { mutableIntStateOf(0) }
    var isSearching by remember { mutableStateOf(false) }

    // 시뮬레이션용 데이터
    val allItems = remember {
        listOf(
            "Apple", "Apricot", "Avocado",
            "Banana", "Blueberry", "Blackberry",
            "Cherry", "Coconut", "Cranberry",
            "Date", "Dragonfruit", "Durian",
            "Elderberry", "Fig", "Grape",
            "Honeydew", "Kiwi", "Lemon",
            "Mango", "Melon", "Nectarine",
            "Orange", "Papaya", "Peach",
            "Pear", "Pineapple", "Plum",
            "Raspberry", "Strawberry", "Watermelon"
        )
    }

    // TODO: snapshotFlow를 사용하여 검색어 디바운싱 구현
    // 힌트:
    // 1. LaunchedEffect(Unit) 사용
    // 2. snapshotFlow { searchQuery }
    // 3. .debounce(300)
    // 4. .filter { it.isNotBlank() }
    // 5. .distinctUntilChanged()
    // 6. .collect { query -> ... }

    // === 여기에 구현하세요 ===

    // 정답 (주석 해제하여 확인):
    /*
    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }
            .debounce(300)
            .filter { it.isNotBlank() }
            .distinctUntilChanged()
            .collect { query ->
                isSearching = true
                searchCount++
                // 검색 시뮬레이션 (실제로는 API 호출)
                kotlinx.coroutines.delay(200)
                searchResults = allItems.filter {
                    it.contains(query, ignoreCase = true)
                }
                isSearching = false
            }
    }
    */

    // === 구현 끝 ===

    // 빈 검색어일 때 결과 초기화
    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            searchResults = emptyList()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: snapshotFlow 검색 디바운싱",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "검색어 입력 시 300ms 대기 후 검색을 실행하세요. 빠르게 타이핑해도 마지막 입력만 검색됩니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
snapshotFlow { state }
    .debounce(300)
    .filter { it.isNotBlank() }
    .distinctUntilChanged()
    .collect { ... }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 검색 입력
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("검색어 입력") },
            placeholder = { Text("과일 이름 검색...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 상태 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("검색 실행 횟수: $searchCount")
            if (isSearching) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            }
        }

        // 검색 결과
        Text(
            text = "검색 결과 (${searchResults.size}개):",
            style = MaterialTheme.typography.titleSmall
        )

        if (searchResults.isEmpty() && searchQuery.isNotBlank()) {
            Text(
                text = "결과 없음 (snapshotFlow 구현 필요)",
                color = MaterialTheme.colorScheme.error
            )
        }

        searchResults.forEach { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = item,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

/**
 * 연습 문제 2: rememberUpdatedState로 동적 메시지 타이머
 *
 * 목표: 5초 후 최신 메시지로 알림 표시
 *
 * 구현 요구사항:
 * 1. rememberUpdatedState로 message 래핑
 * 2. rememberUpdatedState로 onNotify 콜백 래핑
 * 3. LaunchedEffect(Unit)에서 5초 후 최신 값 사용
 */
@Composable
fun Practice2_DynamicMessageTimer() {
    var message by remember { mutableStateOf("안녕하세요!") }
    var notificationHistory by remember { mutableStateOf(listOf<String>()) }
    var isTimerActive by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(0) }

    val onNotify: (String) -> Unit = { msg ->
        notificationHistory = (notificationHistory + msg).takeLast(5)
    }

    // TODO: rememberUpdatedState를 사용하여 최신 값 유지
    // 힌트:
    // 1. val currentMessage by rememberUpdatedState(message)
    // 2. val currentOnNotify by rememberUpdatedState(onNotify)
    // 3. LaunchedEffect(isTimerActive)에서 5초 카운트다운 후 currentOnNotify(currentMessage) 호출

    // === 여기에 구현하세요 ===

    // 정답 (주석 해제하여 확인):
    /*
    val currentMessage by rememberUpdatedState(message)
    val currentOnNotify by rememberUpdatedState(onNotify)

    LaunchedEffect(isTimerActive) {
        if (isTimerActive) {
            for (i in 5 downTo 1) {
                remainingSeconds = i
                kotlinx.coroutines.delay(1000)
            }
            remainingSeconds = 0
            currentOnNotify("알림: $currentMessage")
            isTimerActive = false
        }
    }
    */

    // === 구현 끝 ===

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: rememberUpdatedState 타이머",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "타이머 시작 후 메시지를 변경해보세요. 5초 후 최신 메시지가 알림으로 표시되어야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
val currentMessage by rememberUpdatedState(message)

LaunchedEffect(trigger) {
    delay(5000)
    useLatestValue(currentMessage)
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 메시지 입력
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("메시지") },
            modifier = Modifier.fillMaxWidth()
        )

        // 타이머 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { isTimerActive = true },
                enabled = !isTimerActive
            ) {
                Text(
                    if (isTimerActive) "대기 중... ${remainingSeconds}초"
                    else "5초 타이머 시작"
                )
            }

            if (isTimerActive) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }

        // 알림 기록
        Text(
            text = "알림 기록:",
            style = MaterialTheme.typography.titleSmall
        )

        if (notificationHistory.isEmpty()) {
            Text(
                text = "아직 알림이 없습니다 (rememberUpdatedState 구현 필요)",
                color = MaterialTheme.colorScheme.error
            )
        }

        notificationHistory.reversed().forEach { notification ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = notification,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

/**
 * 연습 문제 3: derivedStateOf vs snapshotFlow 분리
 *
 * 목표: 스크롤 기반 UI 상태와 분석 로그를 올바르게 분리
 *
 * 구현 요구사항:
 * 1. derivedStateOf로 FAB 표시 여부 결정 (index > 5)
 * 2. snapshotFlow로 스크롤 분석 로그 기록
 */
@OptIn(FlowPreview::class)
@Composable
fun Practice3_SeparateUIAndSideEffect() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var analyticsLog by remember { mutableStateOf(listOf<String>()) }
    var fabStateChanges by remember { mutableIntStateOf(0) }

    // TODO 1: derivedStateOf로 FAB 표시 여부 결정
    // 힌트: val showFab by remember { derivedStateOf { listState.firstVisibleItemIndex > 5 } }

    // === 여기에 구현하세요 ===
    val showFab = false  // TODO: derivedStateOf로 변경

    // 정답 (주석 해제하여 확인):
    /*
    val showFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 5 }
    }
    */
    // === 구현 끝 ===

    // TODO 2: snapshotFlow로 분석 로그 기록
    // 힌트:
    // LaunchedEffect(Unit) {
    //     snapshotFlow { listState.firstVisibleItemIndex }
    //         .distinctUntilChanged()
    //         .collect { index -> ... }
    // }

    // === 여기에 구현하세요 ===

    // 정답 (주석 해제하여 확인):
    /*
    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                val log = "스크롤: 인덱스 $index"
                analyticsLog = (analyticsLog + log).takeLast(5)
            }
    }
    */
    // === 구현 끝 ===

    // FAB 상태 변경 추적
    LaunchedEffect(showFab) {
        fabStateChanges++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: UI 상태 vs Side Effect 분리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "derivedStateOf로 FAB 표시를, snapshotFlow로 분석 로그를 구현하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "황금률",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
UI 상태 파생 → derivedStateOf
Side Effect → snapshotFlow
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 상태 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("현재 인덱스: ${listState.firstVisibleItemIndex}")
                Text("showFab: $showFab")
                Text("FAB 상태 변경: ${fabStateChanges}회")
            }
        }

        // 분석 로그
        Text(
            text = "분석 로그:",
            style = MaterialTheme.typography.labelMedium
        )
        if (analyticsLog.isEmpty()) {
            Text(
                text = "(snapshotFlow 구현 필요)",
                color = MaterialTheme.colorScheme.error
            )
        }
        analyticsLog.forEach { log ->
            Text(
                text = log,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 스크롤 리스트
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(50) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (index <= 5)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "아이템 ${index + 1}" +
                                    if (index == 5) " (여기서부터 FAB 표시)" else "",
                            modifier = Modifier.padding(16.dp),
                            fontWeight = if (index == 5) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // FAB
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "맨 위로")
                }
            }
        }

        // 구현 확인
        if (!showFab && listState.firstVisibleItemIndex > 5) {
            Text(
                text = "FAB가 표시되어야 합니다! derivedStateOf를 구현하세요.",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
