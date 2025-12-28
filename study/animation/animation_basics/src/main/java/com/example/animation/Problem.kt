package com.example.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - 애니메이션 없이 즉각적인 UI 변화
 *
 * 이 코드의 문제점:
 * 1. 크기 변화가 즉각적으로 발생 → 어색한 UX
 * 2. 색상 변화가 즉각적 → 무엇이 변했는지 인지하기 어려움
 * 3. 요소 등장/퇴장이 갑자기 발생 → 시각적 연속성 없음
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Problem: 애니메이션 없는 UI",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        // 문제 1: 즉각적인 크기 변화
        ProblemSizeChange()

        // 문제 2: 즉각적인 색상 변화
        ProblemColorChange()

        // 문제 3: 즉각적인 등장/퇴장
        ProblemVisibility()

        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 문제인가?",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 크기가 갑자기 변경 → 사용자가 무엇이 변했는지 모름")
                Text("2. 색상이 즉시 전환 → 시각적 피드백 부족")
                Text("3. 요소가 갑자기 나타남/사라짐 → 혼란스러운 UX")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solution 탭에서 해결책을 확인하세요!",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 문제 1: 애니메이션 없는 크기 변화
 */
@Composable
private fun ProblemSizeChange() {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "문제 1: 크기 변화",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 문제: 크기가 즉각적으로 변경됨
            val boxSize = if (isExpanded) 150.dp else 80.dp

            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { isExpanded = !isExpanded },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "클릭!",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "크기가 즉시 변경됩니다 (어색함)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 2: 애니메이션 없는 색상 변화
 */
@Composable
private fun ProblemColorChange() {
    var isLiked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "문제 2: 색상 변화",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 문제: 색상이 즉각적으로 변경됨
            val iconColor = if (isLiked) Color.Red else Color.Gray

            IconButton(onClick = { isLiked = !isLiked }) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    tint = iconColor,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = "색상이 즉시 변경됩니다 (시각적 피드백 부족)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 문제 3: 애니메이션 없는 등장/퇴장
 */
@Composable
private fun ProblemVisibility() {
    var isVisible by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "문제 3: 등장/퇴장",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { isVisible = !isVisible }) {
                Text(if (isVisible) "숨기기" else "보이기")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 문제: 즉각적으로 나타나거나 사라짐
            if (isVisible) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "갑자기 나타나고 사라지는 카드",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "요소가 갑자기 나타나거나 사라집니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
