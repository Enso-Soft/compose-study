package com.example.pull_to_refresh

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 해결책: PullToRefreshBox를 사용한 당겨서 새로고침
 *
 * PullToRefreshBox를 사용하면:
 * 1. 모바일 표준 UX 제공 (위에서 아래로 당기기)
 * 2. 자동 인디케이터 애니메이션
 * 3. Nested Scroll 자동 처리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    // Recomposition 카운트
    class Ref(var value: Int)
    val recompositionRef = remember { Ref(0) }
    var displayCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recompositionRef.value++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결: PullToRefreshBox 사용",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "위에서 아래로 화면을 당겨보세요!\n자연스럽게 새로고침됩니다.",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 올바른 구현: PullToRefreshBox 사용
        SolutionNewsFeed()

        Spacer(modifier = Modifier.height(16.dp))

        // Recomposition 카운터
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Recomposition: ${displayCount}회",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
            TextButton(onClick = { displayCount = recompositionRef.value }) {
                Text("갱신", fontSize = 12.sp)
            }
        }

        // 핵심 포인트 카드
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "핵심 포인트",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• isRefreshing: 새로고침 상태 (직접 관리)\n" +
                            "• onRefresh: 새로고침 트리거 콜백\n" +
                            "• 내부에 스크롤 가능한 콘텐츠 배치",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SolutionNewsFeed() {
    var articles by remember { mutableStateOf(generateSolutionArticles()) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "뉴스 피드",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "당겨서 새로고침",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider()

        // PullToRefreshBox로 LazyColumn 래핑
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(1500) // 네트워크 시뮬레이션
                    articles = generateNewSolutionArticles() + articles
                    isRefreshing = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            state = state,
            indicator = {
                // 기본 인디케이터 (색상 커스터마이징 가능)
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = state,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(articles, key = { it.id }) { article ->
                    SolutionArticleItem(article)
                }
            }
        }
    }
}

@Composable
private fun SolutionArticleItem(article: SolutionArticle) {
    ListItem(
        headlineContent = {
            Text(
                text = article.title,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                text = article.summary,
                maxLines = 2,
                fontSize = 14.sp
            )
        },
        trailingContent = {
            Text(
                text = article.timestamp,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    )
}

// 데이터 모델 (Solution용)
private data class SolutionArticle(
    val id: Int,
    val title: String,
    val summary: String,
    val timestamp: String
)

private var solutionArticleIdCounter = 100

private fun generateSolutionArticles(): List<SolutionArticle> {
    return listOf(
        SolutionArticle(
            id = solutionArticleIdCounter++,
            title = "PullToRefresh로 UX 개선",
            summary = "당겨서 새로고침 패턴을 적용하여 사용자 경험을 크게 개선했습니다.",
            timestamp = "10분 전"
        ),
        SolutionArticle(
            id = solutionArticleIdCounter++,
            title = "Material3 컴포넌트 가이드",
            summary = "최신 Material3 디자인 시스템의 컴포넌트를 활용하는 방법을 알아봅니다.",
            timestamp = "1시간 전"
        ),
        SolutionArticle(
            id = solutionArticleIdCounter++,
            title = "Compose 상태 관리 베스트 프랙티스",
            summary = "효율적인 상태 관리를 위한 패턴과 안티패턴을 정리했습니다.",
            timestamp = "2시간 전"
        ),
        SolutionArticle(
            id = solutionArticleIdCounter++,
            title = "비동기 처리와 코루틴",
            summary = "Compose에서 코루틴을 활용한 비동기 처리 방법을 소개합니다.",
            timestamp = "3시간 전"
        ),
        SolutionArticle(
            id = solutionArticleIdCounter++,
            title = "애니메이션 적용하기",
            summary = "사용자 인터랙션에 애니메이션을 추가하여 앱을 더 생동감 있게 만들어보세요.",
            timestamp = "5시간 전"
        )
    )
}

private fun generateNewSolutionArticles(): List<SolutionArticle> {
    val headlines = listOf(
        "새 소식: 최신 업데이트",
        "핫토픽: 트렌드 분석",
        "가이드: 개발 팁 공유",
        "리뷰: 신규 기능 체험기"
    )
    val summaries = listOf(
        "방금 업데이트된 최신 소식입니다.",
        "개발자 커뮤니티에서 화제가 되고 있습니다.",
        "실무에 바로 적용할 수 있는 팁입니다.",
        "새로운 기능을 직접 사용해 보았습니다."
    )

    return listOf(
        SolutionArticle(
            id = solutionArticleIdCounter++,
            title = headlines.random(),
            summary = summaries.random(),
            timestamp = "방금 전"
        )
    )
}
