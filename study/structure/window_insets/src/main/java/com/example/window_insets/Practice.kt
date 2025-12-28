package com.example.window_insets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 시스템 바 처리 (난이도: 기본)
 *
 * 과제: Box에 safeDrawingPadding()을 적용하여
 * 콘텐츠가 시스템 바에 가려지지 않도록 하세요.
 *
 * 힌트: Modifier.safeDrawingPadding() 사용
 */
@Composable
fun Practice1_SafeDrawingPadding() {
    Column(modifier = Modifier.fillMaxSize()) {
        // 힌트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: safeDrawingPadding() 적용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        Box에 배경색을 적용하고
                        safeDrawingPadding()을 추가하세요.

                        힌트: Modifier.safeDrawingPadding()
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // TODO: 아래 Box에 safeDrawingPadding()을 추가하세요
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                // TODO: 여기에 .safeDrawingPadding() 을 추가하세요
            ,
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "안전 영역 내 콘텐츠",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "이 카드는 시스템 바에 가려지지 않습니다",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    /* 정답 코드:
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .safeDrawingPadding(),  // 추가!
        contentAlignment = Alignment.Center
    ) {
        // 콘텐츠
    }
    */
}

/**
 * 연습 문제 2: 키보드 대응 TextField (난이도: 중급)
 *
 * 과제: 화면 하단에 TextField를 배치하고
 * 키보드가 올라올 때 TextField가 가려지지 않도록 하세요.
 *
 * 힌트:
 * - Modifier.safeDrawingPadding() - 시스템 바 처리
 * - Modifier.imePadding() - 키보드 처리
 * - 순서가 중요합니다!
 */
@Composable
fun Practice2_ImePadding() {
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // 힌트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 키보드 대응 메시지 입력",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        하단 TextField를 클릭하여 키보드를 열면
                        TextField가 키보드 위로 올라와야 합니다.

                        힌트:
                        1. Column에 imePadding() 적용
                        2. Spacer(weight(1f))로 TextField를 하단에 배치
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // TODO: 이 Column에 imePadding()을 추가하세요
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                // TODO: 여기에 .imePadding() 을 추가하세요
        ) {
            // 메시지 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "채팅 메시지 영역",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "입력된 메시지: ${message.ifEmpty { "(없음)" }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 입력 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("메시지를 입력하세요...") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { /* 전송 */ }) {
                    Icon(Icons.Default.Send, contentDescription = "보내기")
                }
            }
        }
    }

    /* 정답 코드:
    Column(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .imePadding()  // 키보드가 올라오면 패딩 추가
    ) {
        // 내용
    }
    */
}

/**
 * 연습 문제 3: Scaffold로 리스트 화면 만들기 (난이도: 고급)
 *
 * 과제: Scaffold를 사용하여 TopAppBar가 있는 리스트 화면을 만드세요.
 * 리스트의 첫 번째와 마지막 아이템이 시스템 바에 가려지지 않도록 하세요.
 *
 * 힌트:
 * - Scaffold의 paddingValues를 LazyColumn의 contentPadding으로 사용
 * - 또는 Modifier.padding(paddingValues) 사용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_ScaffoldWithList() {
    Column(modifier = Modifier.fillMaxSize()) {
        // 힌트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: Scaffold + LazyColumn",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        TopAppBar가 있는 리스트 화면을 만드세요.

                        힌트:
                        1. Scaffold { paddingValues -> }
                        2. LazyColumn(contentPadding = paddingValues)
                        3. 또는 Modifier.padding(paddingValues)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // TODO: Scaffold를 완성하세요
        Scaffold(
            topBar = {
                // TODO: TopAppBar 또는 CenterAlignedTopAppBar 추가
                // CenterAlignedTopAppBar(title = { Text("연습 3 리스트") })
            }
        ) { paddingValues ->
            // TODO: paddingValues를 사용하여 LazyColumn 구성
            // LazyColumn(contentPadding = paddingValues) { ... }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                // TODO: contentPadding = paddingValues 추가
            ) {
                items(20) { index ->
                    ListItem(
                        headlineContent = { Text("아이템 ${index + 1}") },
                        supportingContent = {
                            if (index == 0) Text("첫 번째 아이템 - 상단 패딩이 필요합니다")
                            else if (index == 19) Text("마지막 아이템 - 하단 패딩이 필요합니다")
                            else null
                        }
                    )
                    if (index < 19) HorizontalDivider()
                }
            }
        }
    }

    /* 정답 코드:
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("연습 3 리스트") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues
            // 또는: modifier = Modifier.padding(paddingValues)
        ) {
            items(20) { index ->
                ListItem(
                    headlineContent = { Text("아이템 ${index + 1}") }
                )
            }
        }
    }
    */
}
