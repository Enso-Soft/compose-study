package com.example.scaffold_and_theming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Problem: 테마와 Scaffold 없이 UI를 구성할 때의 문제점
 *
 * 문제 상황:
 * 1. 색상을 하드코딩하면 다크모드 대응 불가
 * 2. 레이아웃을 수동 구성하면 시스템 바와 충돌
 * 3. 각 컴포넌트마다 다른 스타일 → 일관성 없음
 * 4. FAB, Snackbar 위치를 수동 계산해야 함
 */

@Composable
fun ProblemScreen() {
    var isDarkMode by remember { mutableStateOf(false) }
    var recomposeCount by remember { mutableIntStateOf(0) }

    // Recomposition 추적
    SideEffect {
        recomposeCount++
    }

    // ❌ 문제: 테마 없이 색상 하드코딩
    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val textColor = Color(0xFF000000) // ❌ 다크모드에서 안 보임!
    val appBarColor = Color(0xFF6200EE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 설명 카드
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
                    text = "문제 상황: 테마와 Scaffold 없이 UI 구성",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• 색상 하드코딩 → 다크모드 대응 불가\n" +
                            "• 수동 레이아웃 → 시스템 바 충돌\n" +
                            "• 일관성 없는 스타일링\n" +
                            "• FAB/Snackbar 위치 수동 계산",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // ❌ 수동으로 구현한 "앱바" - 하드코딩된 높이
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(appBarColor)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "메뉴",
                    tint = Color.White // ❌ 하드코딩
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "수동 구현 앱바",
                    color = Color.White, // ❌ 하드코딩
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 다크모드 토글
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "다크 모드 시뮬레이션",
                color = textColor, // ❌ 다크모드에서 검정 글씨 → 안 보임!
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ❌ 하드코딩된 색상의 카드들
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD) // ❌ 하드코딩된 색상
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "첫 번째 메모",
                    color = Color(0xFF1565C0), // ❌ 하드코딩
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "이 카드는 하드코딩된 색상을 사용합니다.\n다크모드로 전환해도 색상이 변경되지 않습니다.",
                    color = Color(0xFF424242) // ❌ 하드코딩
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0) // ❌ 또 다른 하드코딩 색상
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "두 번째 메모",
                    color = Color(0xFFE65100), // ❌ 하드코딩
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "색상이 일관성 없이 각각 다르게 지정되어 있습니다.\n디자인 시스템 없이 파편화된 UI입니다.",
                    color = Color(0xFF424242) // ❌ 하드코딩
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 비교
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
                    text = "잘못된 코드 예시",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
// ❌ 하드코딩된 색상
Text(
    text = "Hello",
    color = Color(0xFF000000)  // 매직 넘버!
)

// ❌ 수동 레이아웃
Box(
    modifier = Modifier
        .height(56.dp)  // 하드코딩!
        .background(Color(0xFF6200EE))
) { ... }
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recomposition 카운터
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = "Recomposition 횟수: ${recomposeCount}회",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // FAB 공간 확보

        // ❌ 수동 FAB 배치 - Box 밖에 있어서 위치가 이상함
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF6200EE),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "추가",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
