package com.example.snapshot_system

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * 연습 문제 1: 카운터 5의 배수 알림 (쉬움)
 *
 * 버튼을 클릭할 때마다 카운터가 증가합니다.
 * snapshotFlow를 사용하여 카운터가 5의 배수가 될 때마다 알림을 표시하세요.
 */
@Composable
fun Practice1_CounterNotificationScreen() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 5의 배수 알림",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("버튼을 클릭할 때마다 카운터가 증가합니다.")
                Text("snapshotFlow를 사용하여 카운터가 5의 배수가 될 때마다 알림 메시지를 표시하세요.")
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- snapshotFlow { counter }로 시작하세요")
                Text("- .filter { it % 5 == 0 && it > 0 } 를 사용하세요")
                Text("- collect에서 notification 상태를 업데이트하세요")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    var counter by remember { mutableIntStateOf(0) }
    var notification by remember { mutableStateOf("아직 알림 없음") }
    var notificationCount by remember { mutableIntStateOf(0) }

    // TODO: snapshotFlow를 사용하여 5의 배수일 때 알림을 표시하세요
    // 요구사항:
    // 1. LaunchedEffect(Unit) 블록 안에서 snapshotFlow 사용
    // 2. snapshotFlow { counter }로 시작
    // 3. .filter { it % 5 == 0 && it > 0 } 적용
    // 4. .collect에서 notificationCount++, notification 업데이트

    // LaunchedEffect(Unit) {
    //     snapshotFlow { counter }
    //         .filter { it % 5 == 0 && it > 0 }
    //         .collect { value ->
    //             notificationCount++
    //             notification = "${value}는 5의 배수입니다! (알림 #${notificationCount})"
    //         }
    // }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { counter++ }) {
            Text("카운트: $counter")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (notification.contains("배수"))
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = notification,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "5, 10, 15, 20... 에서 알림이 표시됩니다",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 연습 문제 2: 폼 자동저장 (중간)
 *
 * 이름과 이메일 입력 필드가 있습니다.
 * snapshotFlow + debounce를 사용하여 입력 후 1초 동안 변경이 없으면
 * 자동으로 "저장"(로그 출력)하세요.
 */
@OptIn(FlowPreview::class)
@Composable
fun Practice2_AutoSaveFormScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 폼 자동저장",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("이름과 이메일 입력 필드가 있습니다.")
                Text("입력 후 1초 동안 변경이 없으면 자동으로 저장합니다.")
                Text("저장 횟수와 마지막 저장 내용을 표시하세요.")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- 두 필드를 snapshotFlow 블록에서 조합하세요")
                Text("- debounce(1000)을 사용하세요")
                Text("- distinctUntilChanged()를 추가하세요")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Practice2_Exercise()
            }
        }
    }
}

// 폼 데이터를 담는 data class
private data class FormData(val name: String, val email: String)

@OptIn(FlowPreview::class)
@Composable
private fun Practice2_Exercise() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var saveCount by remember { mutableIntStateOf(0) }
    var lastSaved by remember { mutableStateOf("아직 저장되지 않음") }
    var isSaving by remember { mutableStateOf(false) }

    // TODO: snapshotFlow + debounce로 자동저장 구현
    // 요구사항:
    // 1. snapshotFlow { FormData(name, email) }로 시작
    // 2. .debounce(1000) - 1초 대기
    // 3. .distinctUntilChanged() - 중복 제거
    // 4. .filter { it.name.isNotBlank() || it.email.isNotBlank() } - 빈 값 무시
    // 5. .collect에서 saveCount++, lastSaved 업데이트

    // LaunchedEffect(Unit) {
    //     snapshotFlow { FormData(name, email) }
    //         .debounce(1000)
    //         .distinctUntilChanged()
    //         .filter { it.name.isNotBlank() || it.email.isNotBlank() }
    //         .collect { data ->
    //             isSaving = true
    //             saveCount++
    //             lastSaved = "저장됨: 이름='${data.name}', 이메일='${data.email}'"
    //             isSaving = false
    //         }
    // }

    Column {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            }
            Text(
                text = "저장 횟수: $saveCount",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = lastSaved,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "1초 동안 입력이 없으면 자동 저장됩니다",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 연습 문제 3: 스크롤 분석 추적기 (어려움)
 *
 * LazyColumn의 아이템 중 사용자가 처음으로 본 아이템을 추적합니다.
 * 이미 본 아이템은 중복 기록하지 않고, "분석 로그" 목록에 표시합니다.
 */
@Composable
fun Practice3_ScrollAnalyticsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 스크롤 분석 추적",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("LazyColumn을 스크롤하면서 아이템 노출을 추적합니다.")
                Text("각 아이템이 처음 화면에 보일 때만 로그에 기록됩니다.")
                Text("이미 본 아이템은 중복 기록되지 않습니다.")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- listState.layoutInfo.visibleItemsInfo를 활용하세요")
                Text("- 이미 본 아이템을 Set이나 List로 추적하세요")
            }
        }

        Practice3_Exercise()
    }
}

@Composable
private fun Practice3_Exercise() {
    val listState = rememberLazyListState()
    val items = remember { (1..50).map { "아이템 $it" } }

    // 이미 본 아이템 인덱스 추적
    val seenItems = remember { mutableStateListOf<Int>() }
    // 분석 로그
    val analyticsLog = remember { mutableStateListOf<String>() }

    // TODO: snapshotFlow로 visible items 변경을 관찰하고
    // 처음 보는 아이템만 로그에 기록하세요
    // 요구사항:
    // 1. LaunchedEffect(listState) 사용
    // 2. snapshotFlow { listState.layoutInfo.visibleItemsInfo.map { it.index } }
    // 3. .collect에서 visibleIndices 순회
    // 4. seenItems에 없는 인덱스만 추가

    // LaunchedEffect(listState) {
    //     snapshotFlow {
    //         listState.layoutInfo.visibleItemsInfo.map { it.index }
    //     }
    //     .collect { visibleIndices ->
    //         visibleIndices.forEach { index ->
    //             if (index !in seenItems) {
    //                 seenItems.add(index)
    //                 analyticsLog.add(0, "아이템 ${index + 1} 최초 노출")
    //             }
    //         }
    //     }
    // }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 왼쪽: 스크롤 가능한 리스트
        Card(modifier = Modifier.weight(1f)) {
            Column {
                Text(
                    text = "스크롤하세요",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleSmall
                )
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = item,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 오른쪽: 분석 로그
        Card(modifier = Modifier.weight(1f)) {
            Column {
                Text(
                    text = "분석 로그 (${seenItems.size}개 추적)",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleSmall
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(analyticsLog) { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
