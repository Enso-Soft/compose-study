package com.example.strong_skipping_mode

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 해결책 화면
 *
 * Strong Skipping Mode로 문제를 해결하는 방법을 시연합니다:
 * - Unstable 파라미터도 인스턴스 비교(===)로 스킵 결정
 * - Lambda도 자동 메모이제이션
 * - @Stable/@Immutable 수동 추가 불필요
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "해결책: Strong Skipping Mode",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Strong Skipping이 활성화되면, Unstable 파라미터도 인스턴스 비교(===)로 " +
                           "스킵 여부를 결정합니다. 같은 객체면 다시 그리지 않습니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 해결책 데모
        StrongSkippingDemo()

        // 핵심 포인트 카드
        KeyPointsCard()

        // 설정 방법
        ConfigurationCard()

        // Lambda 메모이제이션 설명
        LambdaMemoizationCard()

        // Opt-out 방법
        OptOutCard()
    }
}

/**
 * Strong Skipping 효과 데모
 *
 * Strong Skipping 덕분에 같은 인스턴스의 List는 스킵됩니다.
 */
@Composable
private fun StrongSkippingDemo() {
    var counter by remember { mutableIntStateOf(0) }

    // 사용자 목록 (같은 인스턴스 유지)
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
                text = "Strong Skipping 효과 데모",
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
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Strong Skipping 덕분에 counter가 변경되어도\n" +
                           "users 인스턴스가 같으면 아래 목록은 스킵됩니다!",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 사용자 목록 (Strong Skipping 적용)
            OptimizedUserList(users = users)
        }
    }
}

/**
 * Strong Skipping으로 최적화된 사용자 목록
 *
 * users 인스턴스가 같으면 이 Composable은 스킵됩니다.
 */
@Composable
private fun OptimizedUserList(users: List<User>) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    // Recomposition 추적
    SideEffect {
        recomposeCount++
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Recomposition 카운터 표시
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
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
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 스킵 상태 표시
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "같은 users 인스턴스 -> 스킵됨!",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 사용자 목록
        users.forEach { user ->
            OptimizedUserItemCard(user = user)
        }
    }
}

@Composable
private fun OptimizedUserItemCard(user: User) {
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
private fun KeyPointsCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "핵심 포인트",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            KeyPoint(
                number = "1",
                title = "인스턴스 비교 (===)",
                description = "Unstable 파라미터는 '같은 객체인지' 확인합니다. " +
                              "내용이 같아도 새 객체면 Recomposition됩니다."
            )

            KeyPoint(
                number = "2",
                title = "객체 비교 (equals())",
                description = "Stable 파라미터는 '내용이 같은지' 확인합니다. " +
                              "String, Int, @Stable 클래스 등이 해당됩니다."
            )

            KeyPoint(
                number = "3",
                title = "remember로 인스턴스 유지",
                description = "List, Map 등은 remember로 인스턴스를 유지해야 " +
                              "Strong Skipping이 효과적으로 동작합니다."
            )
        }
    }
}

@Composable
private fun KeyPoint(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
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
private fun ConfigurationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "활성화 방법",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Kotlin 2.0.20+
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Kotlin 2.0.20 이상",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "기본으로 활성화됨 - 설정 불필요!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Kotlin 2.0.20 미만
            Text(
                text = "Kotlin 2.0.20 미만 (Compose Compiler 1.5.4+)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// build.gradle.kts
android {
    // ...
}

composeCompiler {
    enableStrongSkippingMode = true
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

@Composable
private fun LambdaMemoizationCard() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Lambda 자동 메모이제이션",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Strong Skipping은 Lambda도 자동으로 remember 처리합니다. " +
                       "onClick 같은 콜백을 직접 remember할 필요가 없습니다.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 이전 방식
            Text(
                text = "이전 방식 (수동 remember)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// 이전에는 Lambda를 remember해야 했음
val onClick = remember(item) { { onItemClick(item) } }
Button(onClick = onClick) { ... }
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Strong Skipping
            Text(
                text = "Strong Skipping (자동 처리)",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// Lambda가 자동으로 메모이제이션됨!
Button(onClick = { onItemClick(item) }) { ... }
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun OptOutCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Opt-out 방법 (필요한 경우)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // @NonSkippableComposable
            Text(
                text = "@NonSkippableComposable",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "특정 Composable의 스킵을 비활성화합니다.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
@NonSkippableComposable
@Composable
fun AlwaysRecompose() {
    // 이 Composable은 항상 Recomposition됨
}
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // @DontMemoize
            Text(
                text = "@DontMemoize",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lambda 메모이제이션을 비활성화합니다.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
val lambda = @DontMemoize {
    // 이 Lambda는 메모이제이션되지 않음
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
