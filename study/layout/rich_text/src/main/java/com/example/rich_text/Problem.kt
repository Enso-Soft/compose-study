package com.example.rich_text

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Rich Text 문제 상황
 *
 * Row로 여러 Text를 조합하면 발생하는 3가지 핵심 문제를 시연합니다.
 *
 * ## 문제 목록
 * 1. 줄바꿈 불가 - Row는 한 줄로만 배치되어 텍스트가 잘림
 * 2. Baseline 불일치 - 아이콘과 텍스트의 기준선이 맞지 않음
 * 3. 부분 클릭 불가 - 전체 텍스트가 클릭되거나 별도 분리 시 줄바꿈 문제
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 전체 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: Row로 여러 Text를 조합할 때",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "텍스트 일부분만 스타일을 다르게 하고 싶을 때, " +
                            "여러 Text를 Row로 묶는 방법을 생각할 수 있습니다.\n\n" +
                            "하지만 이 방식에는 심각한 한계가 있습니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: 줄바꿈 불가
        Problem1_LineBreakIssue()

        HorizontalDivider()

        // 문제 2: Baseline 정렬 문제
        Problem2_BaselineIssue()

        HorizontalDivider()

        // 문제 3: 부분 클릭 불가
        Problem3_PartialClickIssue()
    }
}

/**
 * 문제 1: 줄바꿈 불가
 *
 * Row로 여러 Text를 조합하면 한 줄로만 배치됩니다.
 * 화면이 좁거나 텍스트가 길면 잘리거나 overflow가 발생합니다.
 */
@Composable
private fun Problem1_LineBreakIssue() {
    var searchCount by remember { mutableIntStateOf(15) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 1: 줄바꿈이 안 됨",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "시나리오: 검색 결과 메시지에서 숫자만 강조하기",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 잘못된 방법: Row 사용
                Text(
                    text = "잘못된 방법 (Row 사용):",
                    style = MaterialTheme.typography.labelSmall
                )

                // 전체 화면 너비에서는 문제가 안 보일 수 있음
                Row {
                    Text("검색 결과 ")
                    Text(
                        text = "${searchCount}건",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("이 발견되었습니다. 원하시는 항목을 선택해주세요.")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 좁은 화면 시뮬레이션
                Text(
                    text = "좁은 화면에서 (200dp):",
                    style = MaterialTheme.typography.labelSmall
                )

                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Row {
                        Text("검색 결과 ")
                        Text(
                            text = "${searchCount}건",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("이 발견되었습니다. 원하시는 항목을 선택해주세요.")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { searchCount += 100 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("숫자 증가")
                    }
                    OutlinedButton(
                        onClick = { searchCount = 15 },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("초기화")
                    }
                }
            }
        }

        // 문제점 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Row는 자식 요소를 한 줄로 배치합니다. " +
                            "텍스트가 화면을 넘어가면 잘리거나 overflow가 발생합니다. " +
                            "자연스러운 줄바꿈이 불가능합니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제 2: Baseline 정렬 불일치
 *
 * Row + Icon + Text 조합에서 아이콘과 텍스트의 baseline이 맞지 않습니다.
 * 아이콘이 텍스트보다 위나 아래로 튀어나와 어색해 보입니다.
 */
@Composable
private fun Problem2_BaselineIssue() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 2: Baseline 정렬 불일치",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "시나리오: 상품 평점에 별 아이콘 표시하기",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Alignment.Top
                Text(
                    text = "Alignment.Top:",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text("평점: ")
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = " 4.5점",
                            fontWeight = FontWeight.Bold
                        )
                        Text(" (1,234명)")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Alignment.CenterVertically
                Text(
                    text = "Alignment.CenterVertically:",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("평점: ")
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = " 4.5점",
                            fontWeight = FontWeight.Bold
                        )
                        Text(" (1,234명)")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Alignment.Bottom
                Text(
                    text = "Alignment.Bottom:",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("평점: ")
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = " 4.5점",
                            fontWeight = FontWeight.Bold
                        )
                        Text(" (1,234명)")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 좁은 화면에서 줄바꿈 문제도 발생
                Text(
                    text = "좁은 화면에서 (200dp) - 줄바꿈 문제도 발생:",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("평점: ")
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = " 4.5점",
                            fontWeight = FontWeight.Bold
                        )
                        Text(" (1,234명 평가)")
                    }
                }
            }
        }

        // 문제점 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Row에서 아이콘은 텍스트의 baseline(기준선)과 맞지 않습니다. " +
                            "Top/Center/Bottom 정렬 모두 완벽하지 않으며, " +
                            "좁은 화면에서는 줄바꿈 문제도 함께 발생합니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제 3: 부분 클릭 불가
 *
 * 전체 Text에 clickable을 적용하면 전체가 클릭됩니다.
 * "이용약관"만 클릭하고 싶은데 전체 문장이 클릭됩니다.
 */
@Composable
private fun Problem3_PartialClickIssue() {
    var clickCount by remember { mutableIntStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 3: 부분 클릭 불가",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "시나리오: '이용약관'과 '개인정보처리방침' 링크 만들기",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 방법 1: 전체 Text에 clickable
                Text(
                    text = "방법 1: 전체 Text에 clickable (잘못됨)",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Text(
                        text = "서비스 이용약관 및 개인정보처리방침에 동의합니다.",
                        modifier = Modifier.clickable { clickCount++ },
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 방법 2: Row로 분리 (줄바꿈 문제)
                Text(
                    text = "방법 2: Row로 분리 (줄바꿈 문제 발생)",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Row {
                        Text(
                            text = "이용약관",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { clickCount++ }
                        )
                        Text(" 및 ")
                        Text(
                            text = "개인정보처리방침",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { clickCount++ }
                        )
                        Text("에 동의합니다.")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "클릭 횟수: $clickCount",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Button(onClick = { clickCount = 0 }) {
                    Text("초기화")
                }
            }
        }

        // 문제점 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "방법 1은 전체 문장이 클릭됩니다. " +
                            "방법 2는 Row 사용으로 줄바꿈 문제가 발생합니다. " +
                            "두 방법 모두 완벽하지 않습니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
