package com.example.lazy_layouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - key 없이 LazyColumn 사용
 *
 * 이 코드를 실행하면 다음 문제가 발생합니다:
 * 1. 아이템 삭제 시 전체 리스트가 재구성됨
 * 2. TextField에 입력 중이던 내용이 사라짐 (상태 손실)
 * 3. 삭제 애니메이션이 적용되지 않음
 * 4. 불필요한 recomposition 발생
 */

data class TodoItem(
    val id: Int,
    val title: String
)

@Composable
fun ProblemScreen() {
    var todos by remember {
        mutableStateOf(
            listOf(
                TodoItem(1, "장보기"),
                TodoItem(2, "운동하기"),
                TodoItem(3, "책 읽기"),
                TodoItem(4, "코딩 공부"),
                TodoItem(5, "친구 만나기")
            )
        )
    }
    var totalRecompositions by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Problem: key 없는 LazyColumn",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 아이템 삭제 시 모든 아이템 recomposition")
                Text("2. 메모 입력 중 삭제하면 입력 내용 손실")
                Text("3. 삭제 애니메이션 없음")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "총 Recomposition: $totalRecompositions",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "할 일 목록 (${todos.size}개)",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 문제: key가 없어서 인덱스 기반으로 아이템 추적
            items(todos) { todo ->
                ProblemTodoCard(
                    todo = todo,
                    onDelete = { todos = todos.filter { it.id != todo.id } },
                    onRecomposition = { totalRecompositions++ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newId = (todos.maxOfOrNull { it.id } ?: 0) + 1
                todos = todos + TodoItem(newId, "새 할 일 $newId")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("할 일 추가")
        }
    }
}

@Composable
fun ProblemTodoCard(
    todo: TodoItem,
    onDelete: () -> Unit,
    onRecomposition: () -> Unit
) {
    // Recomposition 추적
    SideEffect {
        onRecomposition()
    }

    // 메모 상태 - key가 없으면 다른 아이템의 상태와 섞일 수 있음
    var memo by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
                label = { Text("메모 (삭제 시 테스트)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "ID: ${todo.id}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
