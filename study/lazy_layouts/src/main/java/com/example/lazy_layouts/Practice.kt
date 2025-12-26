package com.example.lazy_layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// ============================================================
// 연습 1: key 파라미터 추가하기 (기초)
// ============================================================

data class Contact(
    val id: String,
    val name: String,
    val phone: String
)

private val sampleContacts = listOf(
    Contact("1", "김철수", "010-1234-5678"),
    Contact("2", "이영희", "010-2345-6789"),
    Contact("3", "박민수", "010-3456-7890"),
    Contact("4", "정수진", "010-4567-8901"),
    Contact("5", "홍길동", "010-5678-9012")
)

/**
 * 연습 1: 연락처 목록에 key 파라미터 추가하기
 *
 * 목표: 연락처 삭제 시 성능 최적화
 *
 * 힌트: items() 함수의 key 파라미터에 고유 식별자를 제공하세요
 */
@Composable
fun Practice1_KeyScreen() {
    var contacts by remember { mutableStateOf(sampleContacts) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: key 파라미터 추가",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("목표: items()에 key 파라미터를 추가하여")
                Text("삭제 시 불필요한 recomposition 방지")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: key = { it.id }",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // TODO: key 파라미터를 추가하세요
            // 힌트: items(items = contacts, key = { ??? })
            items(contacts) { contact ->

            // 정답:
            // items(
            //     items = contacts,
            //     key = { it.id }
            // ) { contact ->

                ContactCard(
                    contact = contact,
                    onDelete = { contacts = contacts.filter { it.id != contact.id } }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newId = ((contacts.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1).toString()
                contacts = contacts + Contact(newId, "새 연락처 $newId", "010-0000-0000")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("연락처 추가")
        }
    }
}

@Composable
private fun ContactCard(
    contact: Contact,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = contact.phone,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "삭제",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// ============================================================
// 연습 2: contentType으로 혼합 리스트 최적화 (중급)
// ============================================================

sealed class Message(val id: String, val timestamp: Long) {
    data class TextMessage(
        val messageId: String,
        val text: String,
        val time: Long
    ) : Message(messageId, time)

    data class ImageMessage(
        val messageId: String,
        val imageUrl: String,
        val time: Long
    ) : Message(messageId, time)
}

private val sampleMessages = listOf(
    Message.TextMessage("1", "안녕하세요!", System.currentTimeMillis()),
    Message.ImageMessage("2", "https://example.com/image1.jpg", System.currentTimeMillis()),
    Message.TextMessage("3", "오늘 날씨가 좋네요", System.currentTimeMillis()),
    Message.TextMessage("4", "점심 뭐 먹을까요?", System.currentTimeMillis()),
    Message.ImageMessage("5", "https://example.com/image2.jpg", System.currentTimeMillis()),
    Message.TextMessage("6", "저녁에 만나요!", System.currentTimeMillis())
)

/**
 * 연습 2: 채팅 메시지에 key와 contentType 추가하기
 *
 * 목표: 텍스트/이미지 메시지 타입을 구분하여 최적화
 *
 * 힌트: contentType에서 when 표현식으로 타입 구분
 */
@Composable
fun Practice2_ContentTypeScreen() {
    val messages = remember { sampleMessages }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: contentType 추가",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("목표: key와 contentType을 모두 추가하여")
                Text("텍스트/이미지 메시지 타입별 최적화")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: contentType = { when(it) { ... } }",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // TODO: key와 contentType을 모두 추가하세요
            items(messages) { message ->

            // 정답:
            // items(
            //     items = messages,
            //     key = { it.id },
            //     contentType = { message ->
            //         when (message) {
            //             is Message.TextMessage -> "text"
            //             is Message.ImageMessage -> "image"
            //         }
            //     }
            // ) { message ->

                when (message) {
                    is Message.TextMessage -> TextBubble(message)
                    is Message.ImageMessage -> ImageBubble(message)
                }
            }
        }
    }
}

@Composable
private fun TextBubble(message: Message.TextMessage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "텍스트 메시지 | ID: ${message.id}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ImageBubble(message: Message.ImageMessage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 실제로는 AsyncImage 등을 사용
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("이미지 플레이스홀더")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "이미지 메시지 | ID: ${message.id}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ============================================================
// 연습 3: derivedStateOf로 스크롤 감지 최적화 (고급)
// ============================================================

/**
 * 연습 3: derivedStateOf를 사용하여 스크롤 상태 최적화
 *
 * 목표: 스크롤 위치에 따라 TopAppBar 표시/숨김을 최적화
 *
 * 힌트: val isAtTop by remember { derivedStateOf { ... } }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_DerivedStateScreen() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // TODO: derivedStateOf를 사용하여 최적화하세요
    // 현재 코드는 매 프레임마다 recomposition을 발생시킵니다!
    val isAtTop = listState.firstVisibleItemIndex == 0

    // 정답:
    // val isAtTop by remember {
    //     derivedStateOf {
    //         listState.firstVisibleItemIndex == 0 &&
    //         listState.firstVisibleItemScrollOffset == 0
    //     }
    // }

    Column(modifier = Modifier.fillMaxSize()) {
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
                    text = "연습 3: derivedStateOf 최적화",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("목표: derivedStateOf를 사용하여")
                Text("스크롤 상태 기반 UI 업데이트 최적화")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: val isAtTop by remember { derivedStateOf { ... } }",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 스크롤 상태에 따라 앱바 표시/숨김
        AnimatedVisibility(visible = isAtTop) {
            TopAppBar(
                title = { Text("피드") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(50) { index ->
                    ListItem(
                        headlineContent = { Text("아이템 ${index + 1}") },
                        supportingContent = { Text("스크롤하여 앱바 변화를 확인하세요") }
                    )
                }
            }

            // 스크롤 상태 표시
            Card(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "현재 상태",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text("isAtTop: $isAtTop")
                    Text("firstVisibleItemIndex: ${listState.firstVisibleItemIndex}")
                    Text("firstVisibleItemScrollOffset: ${listState.firstVisibleItemScrollOffset}")
                }
            }

            // 맨 위로 버튼
            if (!isAtTop) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text("Top")
                }
            }
        }
    }
}
