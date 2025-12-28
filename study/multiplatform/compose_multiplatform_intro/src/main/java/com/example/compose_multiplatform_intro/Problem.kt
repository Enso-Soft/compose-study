package com.example.compose_multiplatform_intro

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Problem: 플랫폼별 UI 코드 중복 문제
 *
 * 이 화면은 Compose Multiplatform이 없을 때 발생하는 문제를 시각화합니다.
 * 같은 기능의 UI를 여러 플랫폼에서 각각 다르게 구현해야 하는 상황을 보여줍니다.
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
        // 문제 상황 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: 플랫폼별 UI 코드 중복",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "여러분이 '할 일 목록' 앱을 만들어야 합니다.\n" +
                            "처음에는 Android용으로 만들었는데, iOS 버전도 필요하다고 합니다.\n" +
                            "나중에는 Desktop과 Web 버전도 요청받습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 플랫폼별 코드 비교
        Text(
            text = "같은 기능, 다른 코드",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // 플랫폼별 코드 카드들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlatformCodeCard(
                platform = "Android",
                language = "Kotlin + Compose",
                color = Color(0xFF3DDC84),
                code = """
@Composable
fun TodoList(items: List<Todo>) {
    LazyColumn {
        items(items) { todo ->
            TodoItem(todo)
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Row {
        Checkbox(
            checked = todo.done,
            onCheckedChange = { }
        )
        Text(todo.title)
    }
}
                """.trimIndent()
            )

            PlatformCodeCard(
                platform = "iOS",
                language = "Swift + SwiftUI",
                color = Color(0xFFFA7343),
                code = """
struct TodoList: View {
    var items: [Todo]

    var body: some View {
        List(items) { todo in
            TodoItem(todo: todo)
        }
    }
}

struct TodoItem: View {
    var todo: Todo

    var body: some View {
        HStack {
            Toggle("", isOn: .constant(todo.done))
            Text(todo.title)
        }
    }
}
                """.trimIndent()
            )

            PlatformCodeCard(
                platform = "Web",
                language = "JavaScript + React",
                color = Color(0xFF61DAFB),
                code = """
function TodoList({ items }) {
    return (
        <ul>
            {items.map(todo => (
                <TodoItem
                    key={todo.id}
                    todo={todo}
                />
            ))}
        </ul>
    );
}

function TodoItem({ todo }) {
    return (
        <li>
            <input
                type="checkbox"
                checked={todo.done}
            />
            <span>{todo.title}</span>
        </li>
    );
}
                """.trimIndent()
            )
        }

        // 문제점 요약
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem(
                    number = 1,
                    title = "코드 중복",
                    description = "같은 UI를 3번 작성해야 합니다.\n3개 플랫폼 = 3배의 코드량"
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 2,
                    title = "유지보수 비용 증가",
                    description = "버튼 색상 변경 -> 3곳 수정\n새 기능 추가 -> 3번 구현"
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 3,
                    title = "일관성 유지 어려움",
                    description = "플랫폼별로 UI가 조금씩 달라집니다.\n버그도 플랫폼마다 다르게 발생합니다."
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 4,
                    title = "팀 구성 복잡",
                    description = "Android, iOS, Web 개발자가 각각 필요합니다.\n커뮤니케이션 비용이 증가합니다."
                )
            }
        }

        // 비용 시뮬레이션
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "비용 시뮬레이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                var platformCount by remember { mutableIntStateOf(1) }

                Text("지원 플랫폼 수: $platformCount")
                Slider(
                    value = platformCount.toFloat(),
                    onValueChange = { platformCount = it.toInt() },
                    valueRange = 1f..4f,
                    steps = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                val platforms = listOf("Android", "iOS", "Desktop", "Web")
                Text(
                    text = "지원 플랫폼: ${platforms.take(platformCount).joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CostIndicator(
                        label = "코드량",
                        value = "${platformCount}x"
                    )
                    CostIndicator(
                        label = "유지보수",
                        value = "${platformCount}x"
                    )
                    CostIndicator(
                        label = "개발자 수",
                        value = "${platformCount}+"
                    )
                }
            }
        }

        // 다음 단계 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Solution 탭에서 이 문제의 해결책을 확인하세요!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PlatformCodeCard(
    platform: String,
    language: String,
    color: Color,
    code: String
) {
    Card(
        modifier = Modifier.width(300.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // 헤더
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = platform,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = language,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // 코드
            Surface(
                color = Color(0xFF1E1E1E),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = code,
                    modifier = Modifier.padding(12.dp),
                    color = Color(0xFFD4D4D4),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.error
        ) {
            Text(
                text = "$number",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onError,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
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
private fun CostIndicator(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
