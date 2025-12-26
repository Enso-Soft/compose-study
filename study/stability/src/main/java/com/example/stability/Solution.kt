package com.example.stability

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * 올바른 코드 - @Immutable로 Stable 타입 만들기
 *
 * @Immutable 어노테이션을 사용하면:
 * 1. Compose 컴파일러에게 "이 클래스는 불변"임을 알림
 * 2. 파라미터가 같으면 Composable을 skip 가능
 * 3. 카운터 변경 시 UserList가 recompose되지 않음
 * 4. 각 UserItem도 불필요한 recompose 없음
 */

// Stable data class - @Immutable 어노테이션 추가
@Immutable
data class StableUser(
    val id: Int,
    val name: String,
    val email: String
)

// List를 감싸는 Wrapper - 이것도 @Immutable
@Immutable
data class StableUserListWrapper(
    val users: List<StableUser>
)

@Composable
fun SolutionScreen() {
    var counter by remember { mutableIntStateOf(0) }
    var parentRecomposeCount by remember { mutableIntStateOf(0) }

    // 사용자 목록 - Wrapper로 감싸서 Stable하게 만듦
    val usersWrapper = remember {
        StableUserListWrapper(
            listOf(
                StableUser(1, "Alice", "alice@example.com"),
                StableUser(2, "Bob", "bob@example.com"),
                StableUser(3, "Charlie", "charlie@example.com"),
                StableUser(4, "Diana", "diana@example.com"),
                StableUser(5, "Eve", "eve@example.com")
            )
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
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Solution: @Immutable 사용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("부모 Recomposition: $parentRecomposeCount")
                Text("카운터 값: $counter")
            }
        }

        // 핵심 포인트
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
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "1. @Immutable로 StableUser 정의",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "2. List도 Wrapper로 감싸서 @Immutable",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "3. 카운터 변경해도 리스트 skip!",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "4. 버튼을 눌러도 아이템 카운터 유지!",
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
                .padding(horizontal = 16.dp)
        ) {
            Text("카운터 증가 (현재: $counter)")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 코드 예시
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "사용된 코드",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
@Immutable
data class StableUser(
    val id: Int,
    val name: String,
    val email: String
)

@Immutable
data class StableUserListWrapper(
    val users: List<StableUser>
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 해결책: Wrapper를 사용하여 리스트 전달
        StableUserList(wrapper = usersWrapper)
    }
}

@Composable
private fun StableUserList(wrapper: StableUserListWrapper) {
    var listRecomposeCount by remember { mutableIntStateOf(0) }

    // 리스트 Recomposition 추적
    SideEffect {
        listRecomposeCount++
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "UserList Recomposition: $listRecomposeCount",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(wrapper.users, key = { it.id }) { user ->
            StableUserItem(user = user)
        }
    }
}

@Composable
private fun StableUserItem(user: StableUser) {
    var itemRecomposeCount by remember { mutableIntStateOf(0) }

    // 아이템 Recomposition 추적
    SideEffect {
        itemRecomposeCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (itemRecomposeCount > 1)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
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
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "$itemRecomposeCount",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
