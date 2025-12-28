package com.example.pull_to_refresh

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 문제 상황: PullToRefresh 없이 새로고침 구현
 *
 * 별도의 새로고침 버튼을 사용하면:
 * 1. 사용자가 버튼을 찾아야 하는 불편함
 * 2. 화면 공간 낭비
 * 3. 모바일 표준 UX 미준수
 * 4. 접근성 관점에서 제스처 기반 사용자에게 불편
 */
@Composable
fun ProblemScreen() {
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
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: 버튼으로만 새로고침 가능",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "위에서 아래로 화면을 당겨보세요.\n아무 반응이 없습니다! 버튼을 눌러야만 새로고침됩니다.",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 잘못된 구현: 버튼으로만 새로고침
        ProblemNewsFeed()

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
    }
}

@Composable
private fun ProblemNewsFeed() {
    var articles by remember { mutableStateOf(generateInitialArticles()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        // 문제: 별도의 새로고침 버튼이 필요
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

            // 새로고침 버튼 (사용자가 찾아야 함!)
            IconButton(
                onClick = {
                    scope.launch {
                        isLoading = true
                        delay(1500) // 네트워크 시뮬레이션
                        articles = generateNewArticles() + articles
                        isLoading = false
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "새로고침"
                    )
                }
            }
        }

        HorizontalDivider()

        // 기사 목록 (당겨도 새로고침 안 됨!)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            items(articles, key = { it.id }) { article ->
                ArticleItem(article)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "당겨서 새로고침이 동작하지 않습니다!",
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun ArticleItem(article: Article) {
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

// 데이터 모델
data class Article(
    val id: Int,
    val title: String,
    val summary: String,
    val timestamp: String
)

private var articleIdCounter = 0

private fun generateInitialArticles(): List<Article> {
    return listOf(
        Article(
            id = articleIdCounter++,
            title = "Compose 2.0 정식 출시",
            summary = "Google이 Jetpack Compose 2.0을 정식으로 출시했습니다. 새로운 기능과 성능 개선이 포함되어 있습니다.",
            timestamp = "10분 전"
        ),
        Article(
            id = articleIdCounter++,
            title = "Android 15 베타 공개",
            summary = "Android 15 개발자 프리뷰가 공개되었습니다. 새로운 보안 기능과 UI 변경사항을 확인하세요.",
            timestamp = "1시간 전"
        ),
        Article(
            id = articleIdCounter++,
            title = "Kotlin 2.1 발표",
            summary = "JetBrains가 Kotlin 2.1을 발표했습니다. 컴파일 속도 개선과 새로운 언어 기능이 추가되었습니다.",
            timestamp = "2시간 전"
        ),
        Article(
            id = articleIdCounter++,
            title = "Material Design 4 공개",
            summary = "Google이 Material Design의 새로운 버전을 공개했습니다. 더 유연한 커스터마이징이 가능해졌습니다.",
            timestamp = "3시간 전"
        ),
        Article(
            id = articleIdCounter++,
            title = "Flutter vs Compose 비교",
            summary = "크로스 플랫폼 개발의 두 가지 접근법을 비교 분석합니다.",
            timestamp = "5시간 전"
        )
    )
}

private fun generateNewArticles(): List<Article> {
    val headlines = listOf(
        "속보: 새로운 기술 발표",
        "업데이트: 최신 트렌드 분석",
        "특집: 개발자를 위한 팁",
        "분석: 시장 동향 리포트"
    )
    val summaries = listOf(
        "자세한 내용은 본문을 확인하세요.",
        "개발자들의 많은 관심을 받고 있습니다.",
        "이번 업데이트의 핵심 내용을 정리했습니다.",
        "전문가들의 의견을 종합했습니다."
    )

    return listOf(
        Article(
            id = articleIdCounter++,
            title = headlines.random(),
            summary = summaries.random(),
            timestamp = "방금 전"
        )
    )
}
