package com.example.stability

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - Unstable 타입으로 인한 불필요한 Recomposition
 *
 * 이 코드의 문제점:
 * 1. User data class가 @Immutable 없이 정의됨 -> Unstable
 * 2. List<User>는 기본적으로 Unstable
 * 3. 카운터만 변경해도 UserList 전체가 recompose됨
 * 4. 각 UserItem도 불필요하게 다시 그려짐
 */

// Unstable data class - @Immutable 어노테이션 없음
data class UnstableUser(
    val id: Int,
    val name: String,
    val email: String
)

@Composable
fun ProblemScreen() {
    var counter by remember { mutableIntStateOf(0) }
    var parentRecomposeCount by remember { mutableIntStateOf(0) }

    // 사용자 목록 - List<UnstableUser>는 Unstable
    val users = remember {
        listOf(
            UnstableUser(1, "Alice", "alice@example.com"),
            UnstableUser(2, "Bob", "bob@example.com"),
            UnstableUser(3, "Charlie", "charlie@example.com"),
            UnstableUser(4, "Diana", "diana@example.com"),
            UnstableUser(5, "Eve", "eve@example.com")
        )
    }

    // 부모 Recomposition 추적
    SideEffect {
        parentRecomposeCount++
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상태 표시 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Problem: Unstable 타입 사용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("부모 Recomposition: $parentRecomposeCount")
                Text("카운터 값: $counter")
            }
        }

        // 문제 설명
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 문제인가?",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "1. data class UnstableUser는 Unstable",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "2. List<UnstableUser>도 Unstable",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "3. 카운터만 변경해도 전체 리스트 recompose",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "4. 버튼을 누르고 각 아이템의 카운터 확인!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 카운터 버튼
        Button(
            onClick = { counter++ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("카운터 증가 (현재: $counter)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 문제: 카운터만 변경해도 이 리스트 전체가 recompose됨
        UnstableUserList(users = users)
    }
}

@Composable
private fun UnstableUserList(users: List<UnstableUser>) {
    var listRecomposeCount by remember { mutableIntStateOf(0) }

    // 리스트 Recomposition 추적
    SideEffect {
        listRecomposeCount++
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "UserList Recomposition: $listRecomposeCount",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(4.dp))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users, key = { it.id }) { user ->
            UnstableUserItem(user = user)
        }
    }
}

@Composable
private fun UnstableUserItem(user: UnstableUser) {
    var itemRecomposeCount by remember { mutableIntStateOf(0) }

    // 아이템 Recomposition 추적
    SideEffect {
        itemRecomposeCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (itemRecomposeCount > 1)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Recompose",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "$itemRecomposeCount",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
