package com.example.lazy_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: Column으로 많은 아이템을 표시할 때 발생하는 문제
 *
 * 이 화면은 100개의 사용자를 Column으로 표시합니다.
 * Column은 모든 자식을 한 번에 그리기 때문에,
 * 화면에 보이지 않는 아이템도 모두 메모리에 생성됩니다.
 */
@Composable
fun ProblemScreen() {
    // 화면에 그려진 아이템 수를 추적
    var composedCount by remember { mutableIntStateOf(0) }

    // 100명의 사용자 데이터
    val users = remember {
        (1..100).map { User(id = it, name = "사용자 $it") }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        ProblemExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 통계 카드
        StatisticsCard(composedCount = composedCount, totalCount = users.size)

        Spacer(modifier = Modifier.height(16.dp))

        // 문제가 있는 Column 리스트
        // Column은 모든 자식을 한 번에 그립니다!
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            users.forEach { user ->
                // 각 아이템이 그려질 때 카운터 증가
                LaunchedEffect(user.id) {
                    composedCount++
                }

                SimpleUserCard(user = user)
            }
        }
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "문제: Column으로 많은 아이템 표시",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
                    Column은 모든 자식을 한 번에 그립니다.

                    - 화면에 10개만 보여도 100개 모두 메모리에 생성
                    - 앱 시작 시 느린 로딩
                    - 대용량 데이터에서는 앱이 멈출 수 있음
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun StatisticsCard(composedCount: Int, totalCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (composedCount == totalCount)
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "화면에 그려진 아이템 수",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$composedCount / $totalCount",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (composedCount == totalCount)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (composedCount == totalCount) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "모든 아이템이 한 번에 그려졌습니다! (비효율적)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun SimpleUserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.size(40.dp),
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
                    text = "ID: ${user.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
    val name: String
)
