package com.example.paging_compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Problem: 모든 데이터를 한 번에 로드하는 방식
 *
 * 이 화면은 Paging 없이 모든 뉴스 데이터를 한 번에 로드합니다.
 * 실제 앱에서 이 방식을 사용하면 다음과 같은 문제가 발생합니다:
 *
 * 1. 메모리 과부하 (OOM) - 수천 개의 아이템을 메모리에 로드
 * 2. 긴 초기 로딩 시간 - 사용자가 오래 대기
 * 3. 네트워크 비효율 - 보지 않을 데이터까지 다운로드
 */

// 뉴스 아이템 데이터 클래스
data class NewsItem(
    val id: Int,
    val title: String,
    val summary: String,
    val category: String,
    val timestamp: Long
)

// 모든 뉴스를 한 번에 가져오는 가짜 API (문제 상황 시뮬레이션)
object FakeNewsApi {
    suspend fun getAllNews(count: Int = 100): List<NewsItem> {
        // 네트워크 지연 시뮬레이션 (데이터가 많을수록 오래 걸림)
        delay(count * 30L) // 100개 = 3초

        return (0 until count).map { index ->
            NewsItem(
                id = index,
                title = "뉴스 제목 #${index + 1}",
                summary = "이것은 뉴스 ${index + 1}번의 요약입니다. 중요한 내용이 담겨있습니다...",
                category = listOf("정치", "경제", "사회", "문화", "스포츠")[index % 5],
                timestamp = System.currentTimeMillis() - (index * 60000)
            )
        }
    }
}

@Composable
fun ProblemScreen() {
    // 문제: 모든 데이터를 상태로 관리
    var newsList by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadingTimeMs by remember { mutableLongStateOf(0L) }

    // 문제: 모든 데이터를 한 번에 로드
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        newsList = FakeNewsApi.getAllNews(100) // 100개 한 번에!
        loadingTimeMs = System.currentTimeMillis() - startTime
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "문제: 모든 데이터 한 번에 로드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "• 100개 뉴스를 한 번에 로드\n" +
                    "• 로딩 중 사용자는 빈 화면만 봄\n" +
                    "• 메모리에 모든 데이터 유지",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "현재 상태",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("로드된 아이템: ${newsList.size}개")
                        Text("로딩 시간: ${loadingTimeMs}ms")
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            if (isLoading) "로딩 중..." else "로드 완료",
                            color = if (isLoading)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 콘텐츠 영역
        if (isLoading) {
            // 문제: 로딩 중에는 아무것도 표시할 수 없음
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("100개 뉴스 로딩 중...")
                    Text(
                        "실제 앱에서는 더 많은 데이터 = 더 긴 대기시간",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // 뉴스 목록
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(newsList, key = { it.id }) { news ->
                    NewsCard(news)
                }
            }
        }
    }
}

@Composable
fun NewsCard(news: NewsItem) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    news.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                AssistChip(
                    onClick = { },
                    label = { Text(news.category, style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.height(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                news.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}
