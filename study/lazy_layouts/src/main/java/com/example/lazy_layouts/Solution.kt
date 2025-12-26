package com.example.lazy_layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 올바른 코드 - key, contentType, derivedStateOf 사용
 *
 * 개선점:
 * 1. key로 각 아이템을 고유하게 식별
 * 2. contentType으로 아이템 타입별 최적화
 * 3. derivedStateOf로 스크롤 상태 기반 UI 최적화
 * 4. animateItem()으로 삭제 애니메이션
 */

sealed class FeedItem(val id: String) {
    data class Header(val title: String) : FeedItem("header_$title")
    data class Todo(val todoId: Int, val title: String) : FeedItem("todo_$todoId")
}

@Composable
fun SolutionScreen() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // derivedStateOf: 실제 값이 변할 때만 recomposition
    val showScrollButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 2
        }
    }

    var feedItems by remember {
        mutableStateOf(
            listOf<FeedItem>(
                FeedItem.Header("오늘의 할 일"),
                FeedItem.Todo(1, "장보기"),
                FeedItem.Todo(2, "운동하기"),
                FeedItem.Todo(3, "책 읽기"),
                FeedItem.Header("내일의 할 일"),
                FeedItem.Todo(4, "코딩 공부"),
                FeedItem.Todo(5, "친구 만나기"),
                FeedItem.Todo(6, "영화 보기"),
                FeedItem.Header("이번 주 목표"),
                FeedItem.Todo(7, "프로젝트 완성"),
                FeedItem.Todo(8, "블로그 글쓰기"),
                FeedItem.Todo(9, "새 기술 학습")
            )
        )
    }
    var totalRecompositions by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Solution: key + contentType + derivedStateOf",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "개선점",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. key: 아이템별 고유 식별자 제공")
                    Text("2. contentType: Header/Todo 타입 구분")
                    Text("3. derivedStateOf: 스크롤 버튼 최적화")
                    Text("4. animateItem(): 삭제 애니메이션")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "총 Recomposition: $totalRecompositions",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = feedItems,
                    key = { item -> item.id },  // 고유 키 제공
                    contentType = { item ->     // 타입별 구분
                        when (item) {
                            is FeedItem.Header -> "header"
                            is FeedItem.Todo -> "todo"
                        }
                    }
                ) { item ->
                    when (item) {
                        is FeedItem.Header -> {
                            SolutionHeaderCard(
                                header = item,
                                modifier = Modifier.animateItem()
                            )
                        }
                        is FeedItem.Todo -> {
                            SolutionTodoCard(
                                todo = item,
                                onDelete = {
                                    feedItems = feedItems.filter { it.id != item.id }
                                },
                                onRecomposition = { totalRecompositions++ },
                                modifier = Modifier.animateItem()  // 삭제 애니메이션
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val maxId = feedItems
                        .filterIsInstance<FeedItem.Todo>()
                        .maxOfOrNull { it.todoId } ?: 0
                    feedItems = feedItems + FeedItem.Todo(maxId + 1, "새 할 일 ${maxId + 1}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("할 일 추가")
            }
        }

        // derivedStateOf로 최적화된 스크롤 버튼
        AnimatedVisibility(
            visible = showScrollButton,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "맨 위로")
            }
        }
    }
}

@Composable
fun SolutionHeaderCard(
    header: FeedItem.Header,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = header.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SolutionTodoCard(
    todo: FeedItem.Todo,
    onDelete: () -> Unit,
    onRecomposition: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Recomposition 추적
    SideEffect {
        onRecomposition()
    }

    // key가 있으므로 이 상태는 올바르게 보존됨
    var memo by remember { mutableStateOf("") }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                label = { Text("메모 (삭제해도 다른 아이템 유지)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "ID: ${todo.todoId} | Key: ${todo.id}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
