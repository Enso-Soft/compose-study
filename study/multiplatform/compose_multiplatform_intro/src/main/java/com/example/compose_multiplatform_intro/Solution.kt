package com.example.compose_multiplatform_intro

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Solution: Compose Multiplatform으로 해결
 *
 * 이 화면은 Compose Multiplatform이 어떻게 플랫폼별 코드 중복 문제를 해결하는지 보여줍니다.
 * 공유 코드 작성법, 프로젝트 구조, expect/actual 패턴을 설명합니다.
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
        // 해결책 소개
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Compose Multiplatform",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "한 번 작성한 UI 코드를 Android, iOS, Desktop, Web에서 모두 실행할 수 있습니다.\n" +
                            "JetBrains가 개발했으며, Jetpack Compose를 기반으로 합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 2025년 플랫폼 지원 현황
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2025년 플랫폼 지원 현황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                PlatformStatusRow("Android", "Stable", Color(0xFF3DDC84))
                Spacer(modifier = Modifier.height(8.dp))
                PlatformStatusRow("iOS", "Stable", Color(0xFF3DDC84))
                Spacer(modifier = Modifier.height(8.dp))
                PlatformStatusRow("Desktop", "Stable", Color(0xFF3DDC84))
                Spacer(modifier = Modifier.height(8.dp))
                PlatformStatusRow("Web", "Beta", Color(0xFFFFA000))

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "iOS는 2025년 5월 1.8.0 버전에서 Stable 달성",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 공유 코드 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "공유 코드 예시",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 코드는 commonMain에 작성되며,\n모든 플랫폼에서 동일하게 동작합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 코드 블록
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
// commonMain/components/TodoList.kt
// 이 코드는 Android, iOS, Desktop, Web에서 동작합니다!

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
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.done,
            onCheckedChange = { }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(todo.title)
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFD4D4D4),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 실제 동작 데모
                Text(
                    text = "실제 동작 결과:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                SharedTodoListDemo()
            }
        }

        // 프로젝트 구조
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "프로젝트 구조 (소스셋)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "소스셋(Source Set): 특정 플랫폼이나 목적에 맞는 코드를 담는 폴더",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProjectStructureDiagram()
            }
        }

        // expect/actual 패턴
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "expect/actual 패턴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "플랫폼마다 다르게 구현해야 하는 기능이 있을 때 사용합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                ExpectActualExplanation()

                Spacer(modifier = Modifier.height(16.dp))

                // expect/actual 데모
                Text(
                    text = "실제 동작 데모:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExpectActualDemo()
            }
        }

        // 코드 공유율
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "실제 앱의 코드 공유율",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                CodeShareRateBar(appName = "Respawn (iOS)", shareRate = 96)
                Spacer(modifier = Modifier.height(8.dp))
                CodeShareRateBar(appName = "JetBrains Toolbox", shareRate = 100)

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "대부분의 UI 코드를 공유할 수 있습니다.\n플랫폼 특정 기능만 expect/actual로 분리합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 이점 요약
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Compose Multiplatform의 이점",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))

                BenefitItem("한 번 작성, 모든 곳에서 실행")
                BenefitItem("유지보수 비용 대폭 감소")
                BenefitItem("일관된 UI/UX 보장")
                BenefitItem("Kotlin 하나로 모든 플랫폼 개발")
                BenefitItem("네이티브 성능 유지")
            }
        }

        // 다음 단계 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Practice 탭에서 직접 실습해보세요!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun PlatformStatusRow(
    platform: String,
    status: String,
    statusColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = platform,
            style = MaterialTheme.typography.bodyLarge
        )
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = statusColor
        ) {
            Text(
                text = status,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SharedTodoListDemo() {
    val todos = remember {
        listOf(
            SimulatedTodo(1, "Compose Multiplatform 학습하기", true),
            SimulatedTodo(2, "expect/actual 패턴 이해하기", true),
            SimulatedTodo(3, "공유 UI 컴포넌트 만들기", false),
            SimulatedTodo(4, "실제 프로젝트에 적용하기", false)
        )
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            todos.forEach { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.done,
                        onCheckedChange = { },
                        enabled = false
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (todo.id < 4) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                }
            }
        }
    }
}

private data class SimulatedTodo(
    val id: Int,
    val title: String,
    val done: Boolean
)

@Composable
private fun ProjectStructureDiagram() {
    Surface(
        color = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = """
shared/
├── commonMain/         # 공유 UI 코드
│   └── kotlin/
│       ├── App.kt
│       └── components/
│
├── androidMain/        # Android 전용
│   └── kotlin/
│       └── Platform.android.kt
│
├── iosMain/            # iOS 전용
│   └── kotlin/
│       └── Platform.ios.kt
│
├── desktopMain/        # Desktop 전용
│   └── kotlin/
│       └── Platform.jvm.kt
│
└── wasmJsMain/         # Web 전용
    └── kotlin/
        └── Platform.wasm.kt
            """.trimIndent(),
            modifier = Modifier.padding(12.dp),
            color = Color(0xFF98C379),
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun ExpectActualExplanation() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // expect 설명
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2196F3).copy(alpha = 0.1f))
                .border(1.dp, Color(0xFF2196F3), RoundedCornerShape(8.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "expect",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3),
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "\"이런 기능이 필요해요\" (선언/약속)",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // actual 설명
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF4CAF50).copy(alpha = 0.1f))
                .border(1.dp, Color(0xFF4CAF50), RoundedCornerShape(8.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "actual",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50),
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "\"이렇게 구현했어요\" (구현/이행)",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 코드 예시
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            color = Color(0xFF1E1E1E),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = """
// commonMain/Platform.kt
expect fun getPlatformName(): String

// androidMain/Platform.android.kt
actual fun getPlatformName(): String = "Android"

// iosMain/Platform.ios.kt
actual fun getPlatformName(): String = "iOS"

// desktopMain/Platform.jvm.kt
actual fun getPlatformName(): String = "Desktop"
                """.trimIndent(),
                modifier = Modifier.padding(12.dp),
                color = Color(0xFFD4D4D4),
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun ExpectActualDemo() {
    // 시뮬레이션: 현재 Android 환경에서 실행
    val platformName = "Android" // actual 구현 결과
    val platformVersion = Build.VERSION.SDK_INT.toString()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "현재 플랫폼: $platformName",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "API Level: $platformVersion",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "이 앱을 iOS에서 실행하면 \"iOS\"가,\nDesktop에서 실행하면 \"Desktop\"이 표시됩니다.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun CodeShareRateBar(
    appName: String,
    shareRate: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = appName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$shareRate%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { shareRate / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
        )
    }
}

@Composable
private fun BenefitItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}
