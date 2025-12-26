package com.example.custom_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Problem: Column, Row, Box로는 원형 레이아웃을 구현할 수 없습니다!
 *
 * 원형 메뉴를 만들고 싶지만 기본 레이아웃으로는 불가능합니다:
 * - Column: 세로로 일렬 배치
 * - Row: 가로로 일렬 배치
 * - Box: 모두 같은 위치에 겹침
 */

@Composable
fun ProblemScreen() {
    var selectedLayout by remember { mutableStateOf("column") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 상황 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Column/Row/Box의 한계",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "원형 메뉴를 만들고 싶지만 기본 레이아웃으로는 " +
                            "아이템들을 원 형태로 배치할 수 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 레이아웃 선택 버튼
        Text(
            text = "레이아웃 선택:",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedLayout == "column",
                onClick = { selectedLayout = "column" },
                label = { Text("Column") }
            )
            FilterChip(
                selected = selectedLayout == "row",
                onClick = { selectedLayout = "row" },
                label = { Text("Row") }
            )
            FilterChip(
                selected = selectedLayout == "box",
                onClick = { selectedLayout = "box" },
                label = { Text("Box") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 결과 표시 영역
        Text(
            text = "결과:",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 원형 배치 시도 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (selectedLayout) {
                "column" -> ColumnAttempt()
                "row" -> RowAttempt()
                "box" -> BoxAttempt()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = when (selectedLayout) {
                        "column" -> "Column 결과"
                        "row" -> "Row 결과"
                        else -> "Box 결과"
                    },
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (selectedLayout) {
                        "column" -> "아이콘들이 위에서 아래로 세로 일렬로 배치됩니다. " +
                                "원형 배치가 불가능합니다."
                        "row" -> "아이콘들이 왼쪽에서 오른쪽으로 가로 일렬로 배치됩니다. " +
                                "원형 배치가 불가능합니다."
                        else -> "모든 아이콘이 같은 위치에 겹쳐서 하나만 보입니다. " +
                                "최악의 결과입니다!"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 필요성
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Custom Layout",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Layout composable을 사용하면 삼각함수로 원형 좌표를 계산하고 " +
                            "각 아이템을 원하는 위치에 배치할 수 있습니다. " +
                            "Solution 탭에서 확인하세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ColumnAttempt() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MenuIcon(Icons.Default.Home, "Home", Color(0xFF4CAF50))
        MenuIcon(Icons.Default.Search, "Search", Color(0xFF2196F3))
        MenuIcon(Icons.Default.Favorite, "Favorite", Color(0xFFE91E63))
        MenuIcon(Icons.Default.Settings, "Settings", Color(0xFF9C27B0))
        MenuIcon(Icons.Default.Person, "Profile", Color(0xFFFF9800))
    }
}

@Composable
private fun RowAttempt() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MenuIcon(Icons.Default.Home, "Home", Color(0xFF4CAF50))
        MenuIcon(Icons.Default.Search, "Search", Color(0xFF2196F3))
        MenuIcon(Icons.Default.Favorite, "Favorite", Color(0xFFE91E63))
        MenuIcon(Icons.Default.Settings, "Settings", Color(0xFF9C27B0))
        MenuIcon(Icons.Default.Person, "Profile", Color(0xFFFF9800))
    }
}

@Composable
private fun BoxAttempt() {
    Box(contentAlignment = Alignment.Center) {
        // 모든 아이콘이 같은 위치에 배치됨 - 마지막 것만 보임!
        MenuIcon(Icons.Default.Home, "Home", Color(0xFF4CAF50))
        MenuIcon(Icons.Default.Search, "Search", Color(0xFF2196F3))
        MenuIcon(Icons.Default.Favorite, "Favorite", Color(0xFFE91E63))
        MenuIcon(Icons.Default.Settings, "Settings", Color(0xFF9C27B0))
        MenuIcon(Icons.Default.Person, "Profile", Color(0xFFFF9800))
    }
}

@Composable
private fun MenuIcon(
    icon: ImageVector,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
