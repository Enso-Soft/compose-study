package com.example.progress_indicator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Problem: 로딩 중 빈 화면 문제
 *
 * 데이터를 불러오는 동안 사용자에게 아무런 피드백이 없으면
 * 사용자는 앱이 멈춘 것으로 오해할 수 있습니다.
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
        // 문제 설명 카드
        ProblemExplanationCard()

        // 문제 상황 데모
        ProblemDemo()

        // 문제점 요약
        ProblemSummaryCard()
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "문제 상황",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = "아래 버튼을 눌러보세요. 데이터를 불러오는 3초 동안 화면에 아무것도 표시되지 않습니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = "사용자는 앱이 멈춘 것인지, 정상 동작 중인지 알 수 없습니다!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ProblemDemo() {
    var newsList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "뉴스 목록 (문제 있는 버전)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    newsList = emptyList()
                    isLoading = true
                    scope.launch {
                        // 서버에서 데이터를 가져오는 것처럼 3초 대기
                        delay(3000)
                        newsList = listOf(
                            "속보: 오늘의 주요 뉴스",
                            "경제: 주식 시장 동향",
                            "스포츠: 축구 경기 결과",
                            "연예: 신작 영화 개봉"
                        )
                        isLoading = false
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("뉴스 불러오기")
            }

            // 문제: 로딩 중에 아무것도 표시하지 않음!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                if (newsList.isEmpty() && !isLoading) {
                    Text(
                        text = "버튼을 눌러 뉴스를 불러오세요",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (newsList.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        newsList.forEach { news ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = news,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
                // isLoading == true 일 때 이 영역은 완전히 비어있음!
                // 사용자에게 아무런 피드백이 없음!
            }

            // 현재 상태 표시 (디버그용)
            HorizontalDivider()
            Text(
                text = "현재 상태: ${if (isLoading) "로딩 중... (화면이 비어있음!)" else "대기 중"}",
                style = MaterialTheme.typography.bodySmall,
                color = if (isLoading) MaterialTheme.colorScheme.error
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProblemSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "이런 문제가 발생합니다",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            ProblemItem("1. 사용자 혼란: \"앱이 멈춘 건가?\"")
            ProblemItem("2. 앱 종료: 답답해서 강제 종료")
            ProblemItem("3. 나쁜 UX: 전문적이지 않은 앱처럼 보임")

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "해결책: Solution 탭에서 Progress Indicator를 사용한 해결 방법을 확인하세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProblemItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
