package com.example.text_typography

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Text & Typography 문제 상황
 *
 * 이 파일에서는 AnnotatedString, InlineContent, ClickableText 등을 사용하지 않고
 * Row와 여러 Text를 조합했을 때 발생하는 문제점들을 보여줍니다.
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
        // 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: Row로 부분 스타일링의 한계",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Row와 여러 Text를 조합하면:\n" +
                            "1. 자연스러운 줄바꿈이 불가능\n" +
                            "2. Baseline 정렬 문제\n" +
                            "3. 특정 부분만 클릭 불가\n" +
                            "4. 성능 오버헤드 (여러 Composable)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        HorizontalDivider()

        // 문제 1: Row로 부분 스타일링
        Problem1_RowStyling()

        HorizontalDivider()

        // 문제 2: Row로 아이콘 삽입
        Problem2_RowWithIcons()

        HorizontalDivider()

        // 문제 3: 전체 클릭 vs 부분 클릭
        Problem3_FullClickable()

        HorizontalDivider()

        // 문제 4: 긴 텍스트 줄바꿈 문제
        Problem4_LineBreakIssue()
    }
}

/**
 * 문제 1: Row로 부분 스타일링 시도
 *
 * Row를 사용해 "검색 결과 15건" 중 숫자만 강조하려고 합니다.
 * 문제: 텍스트가 길어지면 줄바꿈이 자연스럽지 않습니다.
 */
@Composable
private fun Problem1_RowStyling() {
    var searchCount by remember { mutableIntStateOf(15) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 1: Row로 부분 스타일링",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "시도한 방법:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 잘못된 방법: Row로 여러 Text 조합
                Row {
                    Text(
                        text = "검색 결과 ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${searchCount}건",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "이 발견되었습니다. 원하시는 항목을 선택하세요.",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "문제점: Row는 가로 정렬이라 텍스트가 잘립니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Button(onClick = { searchCount += 10 }) {
            Text("검색 결과 증가")
        }
    }
}

/**
 * 문제 2: Row로 아이콘 삽입 시도
 *
 * "평점: 별별별별별 4.5점" 처럼 텍스트 흐름 내에 아이콘을 넣으려 합니다.
 * 문제: Baseline 정렬이 맞지 않고, 줄바꿈 시 분리됩니다.
 */
@Composable
private fun Problem2_RowWithIcons() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 2: Row로 아이콘 삽입",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "시도한 방법:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 잘못된 방법: Row로 Text와 Icon 조합
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "평점: ",
                        fontSize = 16.sp
                    )
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = " 4.5점 (1,234명 평가)",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "문제점:\n" +
                            "- CenterVertically를 써도 baseline이 맞지 않음\n" +
                            "- 화면이 좁으면 아이콘과 텍스트가 분리되어 줄바꿈됨",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 좁은 화면 시뮬레이션
                Text(
                    text = "좁은 화면 시뮬레이션 (width = 200dp):",
                    style = MaterialTheme.typography.labelSmall
                )
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "평점: ", fontSize = 16.sp)
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(text = " 4.5점 (1,234명 평가)", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

/**
 * 문제 3: 전체 클릭 vs 부분 클릭
 *
 * "이용약관에 동의합니다" 중 '이용약관'만 클릭 가능하게 하고 싶습니다.
 * 문제: Text에 clickable을 붙이면 전체가 클릭됩니다.
 */
@Composable
private fun Problem3_FullClickable() {
    var clickedText by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 3: 전체 클릭 vs 부분 클릭",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "시도한 방법 1: 전체 Text에 clickable",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 잘못된 방법 1: 전체에 clickable
                Text(
                    text = "이용약관에 동의합니다",
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { clickedText = "전체 텍스트 클릭됨!" }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "시도한 방법 2: Row로 분리",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 잘못된 방법 2: Row로 분리
                Row {
                    Text(
                        text = "이용약관",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { clickedText = "이용약관 클릭!" }
                    )
                    Text(
                        text = "에 동의합니다",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "문제점:\n" +
                            "- 방법 1: 전체가 클릭됨 (원하는 동작 아님)\n" +
                            "- 방법 2: 줄바꿈 시 부자연스러움\n" +
                            "- 밑줄 스타일 적용도 따로 해야 함",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )

                if (clickedText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "클릭 결과: $clickedText",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

/**
 * 문제 4: 긴 텍스트 줄바꿈 문제
 *
 * 여러 스타일이 섞인 긴 문장에서 Row를 사용하면
 * 자연스러운 줄바꿈이 불가능합니다.
 */
@Composable
private fun Problem4_LineBreakIssue() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "문제 4: 긴 텍스트 줄바꿈 문제",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "원하는 결과: 자연스러운 줄바꿈 + 부분 스타일링",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 잘못된 방법: Row로 여러 스타일 조합
                Row {
                    Text(text = "안녕하세요, ", fontSize = 14.sp)
                    Text(
                        text = "홍길동",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "님! 오늘 ", fontSize = 14.sp)
                    Text(
                        text = "3개",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(text = "의 새로운 알림이 있습니다. 지금 확인해보세요!", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "문제점:\n" +
                            "- Row는 한 줄로만 배치됨\n" +
                            "- 화면 너비가 좁으면 텍스트가 잘림\n" +
                            "- FlowRow를 써도 단어 단위 줄바꿈이 부자연스러움",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "해결책: AnnotatedString + buildAnnotatedString 사용",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}
