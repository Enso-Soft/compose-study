package com.example.navigation_rail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Problem: 태블릿에서 하단 NavigationBar 사용 시 문제점
 *
 * 태블릿이나 대형 화면에서 하단 NavigationBar를 사용하면
 * 손이 닿기 어렵고 화면 공간을 비효율적으로 사용하게 됩니다.
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
                    text = "문제 상황: 태블릿에서 하단 NavigationBar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "음악 앱을 태블릿에서 실행합니다. " +
                            "하단에 NavigationBar가 있는데... 뭔가 어색합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 태블릿 시뮬레이션
        Text(
            text = "태블릿 화면 시뮬레이션",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        TabletSimulation()

        // 문제점 목록
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem(
                    number = 1,
                    title = "손이 닿기 어려움",
                    description = "태블릿을 양손으로 잡으면 엄지손가락이 화면 옆에 위치합니다. " +
                            "하단까지 손을 뻗어야 하므로 불편합니다."
                )

                ProblemItem(
                    number = 2,
                    title = "공간 낭비",
                    description = "넓은 태블릿 화면에서 하단 바만 사용하면, " +
                            "양쪽 공간이 비어서 낭비됩니다."
                )

                ProblemItem(
                    number = 3,
                    title = "콘텐츠 영역 감소",
                    description = "하단에 네비게이션이 있으면 세로 공간이 줄어들어 " +
                            "콘텐츠를 표시할 영역이 좁아집니다."
                )
            }
        }

        // 해결 방향 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결 방향",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "네비게이션을 화면 옆(측면)에 세로로 배치하면 어떨까요? " +
                            "바로 그것이 NavigationRail입니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "-> Solution 탭에서 확인해보세요!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun TabletSimulation() {
    // 태블릿 모양 시뮬레이션
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f) // 태블릿 비율
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        var selectedItem by remember { mutableIntStateOf(0) }
        val items = listOf(
            "홈" to Icons.Default.Home,
            "검색" to Icons.Default.Search,
            "라이브러리" to Icons.Default.LibraryMusic,
            "설정" to Icons.Default.Settings
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // 콘텐츠 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "넓은 태블릿 화면",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "콘텐츠 영역이 있지만...\n하단까지 손을 뻗어야 합니다",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 하단 NavigationBar (문제 상황)
            NavigationBar {
                items.forEachIndexed { index, (label, icon) ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, maxLines = 1) }
                    )
                }
            }
        }

        // 문제 표시 오버레이
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp)
                .background(
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "하단에 손이 닿기 어려움!",
                color = MaterialTheme.colorScheme.onError,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = MaterialTheme.colorScheme.onError,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
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
