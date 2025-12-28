package com.example.strong_skipping_mode

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * Strong Skipping Mode가 없을 때 발생하는 문제를 시연합니다:
 * - Unstable 파라미터(List, Map 등)가 있으면 Composable이 항상 Recomposition됨
 * - 같은 데이터여도 스킵되지 않아 성능 저하 발생
 */
@Composable
fun ProblemScreen() {
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
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: Unstable 파라미터로 인한 불필요한 Recomposition",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Strong Skipping이 없으면, List나 Map 같은 '불안정한' 타입의 파라미터가 있을 때 " +
                           "Composable이 매번 다시 그려집니다. 같은 데이터여도 스킵되지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 데모
        UnstableParameterDemo()

        // 문제점 설명 카드
        ProblemExplanationCard()

        // 잘못된 코드 예시
        ProblematicCodeCard()
    }
}

/**
 * 불안정한 파라미터로 인한 문제 데모
 *
 * 상위에서 counter가 변경될 때마다 UserList가 불필요하게 Recomposition됩니다.
 * (Strong Skipping이 없는 상황을 시뮬레이션)
 */
@Composable
private fun UnstableParameterDemo() {
    var counter by remember { mutableIntStateOf(0) }

    // 사용자 목록은 변하지 않지만, counter 변경 시 영향을 받는 상황 시뮬레이션
    val users = remember {
        listOf(
            User(1, "김철수", 25),
            User(2, "이영희", 30),
            User(3, "박민수", 28)
        )
    }

    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "불필요한 Recomposition 데모",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 카운터 버튼
            Button(
                onClick = { counter++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("카운터: $counter")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 설명 텍스트
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "버튼을 누르면 counter만 변경되지만,\n" +
                           "Strong Skipping 없이는 아래 목록도 다시 그려집니다.",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 사용자 목록 (문제 상황 시뮬레이션)
            SimulatedUnstableUserList(users = users, parentCounter = counter)
        }
    }
}

/**
 * 불안정한 상황을 시뮬레이션하는 사용자 목록
 *
 * 실제로는 Strong Skipping 덕분에 스킵되지만,
 * 이 컴포저블에서는 문제 상황을 설명하기 위해 counter를 참조합니다.
 */
@Composable
private fun SimulatedUnstableUserList(
    users: List<User>,
    parentCounter: Int  // 상위 상태를 의도적으로 참조하여 Recomposition 유발
) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    // Recomposition 추적 (이 Composable이 실행될 때마다 증가)
    SideEffect {
        recomposeCount++
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Recomposition 카운터 표시
        Surface(
            color = if (recomposeCount > 3)
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.tertiaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "UserList Recomposition 횟수",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${recomposeCount}회",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (recomposeCount > 3)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }

        // 사용자 목록
        users.forEach { user ->
            UserItemCard(user = user)
        }

        // parentCounter를 사용하여 의도적으로 Recomposition 유발
        // 실제 앱에서는 이런 패턴을 피해야 합니다!
        if (parentCounter > 0) {
            // 이 조건문이 있으면 parentCounter가 변경될 때마다 재평가됨
        }
    }
}

@Composable
private fun UserItemCard(user: User) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${user.age}세",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "왜 이런 문제가 발생할까요?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            ProblemItem(
                number = "1",
                title = "List<User>는 Unstable 타입",
                description = "Compose는 List, Map, Set 등을 '불안정한' 타입으로 판단합니다. " +
                              "내부 데이터가 바뀔 수 있기 때문입니다."
            )

            ProblemItem(
                number = "2",
                title = "Unstable 파라미터 = 항상 Recomposition",
                description = "Strong Skipping 없이는 Unstable 파라미터가 있으면 " +
                              "Composable이 절대 스킵되지 않습니다."
            )

            ProblemItem(
                number = "3",
                title = "불필요한 렌더링 = 성능 저하",
                description = "같은 데이터인데도 매번 다시 그리면 프레임 드랍, 배터리 소모가 증가합니다."
            )
        }
    }
}

@Composable
private fun ProblemItem(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(24.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProblematicCodeCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제가 되는 코드 패턴",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// Strong Skipping 없이는 이 Composable이 절대 스킵되지 않음!
@Composable
fun UserListScreen(
    users: List<User>  // List = Unstable 타입
) {
    Column {
        users.forEach { user ->
            UserItem(user)  // 매번 전체가 다시 그려짐
        }
    }
}

// 상위 Composable
@Composable
fun ParentScreen() {
    var counter by remember { mutableIntStateOf(0) }
    val users = remember { listOf(...) }  // 같은 인스턴스인데도...

    Column {
        Button(onClick = { counter++ }) {
            Text("카운터: ${"$"}counter")
        }

        // counter가 변경되면 UserListScreen도 다시 그려짐!
        UserListScreen(users = users)
    }
}
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 사용자 데이터 클래스
 */
data class User(
    val id: Int,
    val name: String,
    val age: Int
)
