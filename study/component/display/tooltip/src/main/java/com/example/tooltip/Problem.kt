package com.example.tooltip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * Tooltip을 사용하지 않았을 때 발생하는 문제를 시연합니다.
 * 아이콘 버튼만 있고 설명이 없어서 사용자가 기능을 알 수 없습니다.
 */
@Composable
fun ProblemScreen() {
    var clickedButton by remember { mutableStateOf("") }
    var clickCount by remember { mutableIntStateOf(0) }

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
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 툴바의 아이콘들이 무슨 기능인지 알 수 있나요?\n" +
                            "길게 눌러도 아무런 설명이 나타나지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제가 있는 툴바 (Tooltip 없음)
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "문서 편집기 툴바",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tooltip 없는 아이콘 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 저장 버튼 - 설명 없음!
                    IconButton(onClick = {
                        clickedButton = "저장"
                        clickCount++
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null  // 접근성 설명도 없음
                        )
                    }

                    // 공유 버튼 - 설명 없음!
                    IconButton(onClick = {
                        clickedButton = "공유"
                        clickCount++
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null
                        )
                    }

                    // 서식 버튼 - 설명 없음!
                    IconButton(onClick = {
                        clickedButton = "서식 설정"
                        clickCount++
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null
                        )
                    }

                    // 설정 버튼 - 설명 없음!
                    IconButton(onClick = {
                        clickedButton = "설정"
                        clickCount++
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        // 클릭 결과 표시
        if (clickedButton.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "클릭한 버튼: $clickedButton",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "총 클릭 횟수: ${clickCount}회",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "클릭하기 전까지는 어떤 버튼인지 몰랐을 것입니다!\n" +
                                "Tooltip이 있었다면 미리 알 수 있었을 텐데요...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProblemItem(
                    number = 1,
                    text = "사용자 혼란: 아이콘만으로는 기능 파악이 어려움"
                )
                ProblemItem(
                    number = 2,
                    text = "학습 곡선 증가: 모든 버튼을 일일이 눌러봐야 함"
                )
                ProblemItem(
                    number = 3,
                    text = "접근성 문제: 스크린 리더가 읽을 내용이 없음"
                )
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// Tooltip 없이 아이콘만 있는 버튼
IconButton(onClick = { /* 저장 */ }) {
    Icon(
        imageVector = Icons.Default.Save,
        contentDescription = null  // 설명 없음!
    )
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 해결 방법 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결 방법",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solution 탭에서 Tooltip을 사용하여\n" +
                            "이 문제를 해결하는 방법을 확인하세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProblemItem(number: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number. ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
