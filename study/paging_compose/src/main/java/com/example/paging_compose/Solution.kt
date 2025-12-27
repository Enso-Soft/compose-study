package com.example.paging_compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.*
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

/**
 * Solution: Paging 3 + Compose를 사용한 효율적인 데이터 로드
 *
 * Paging 3를 사용하면:
 * 1. 필요한 페이지만 로드 → 메모리 효율적
 * 2. 첫 페이지만 로드 → 빠른 초기 표시
 * 3. 스크롤할 때 다음 페이지 자동 로드 → 부드러운 UX
 * 4. 로딩/에러 상태 자동 관리 → 간편한 UI 처리
 */

// ========================================
// 1. PagingSource 정의
// ========================================
class NewsPagingSource : PagingSource<Int, NewsItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        val page = params.key ?: 0

        return try {
            // 네트워크 지연 시뮬레이션 (페이지당 500ms)
            delay(500)

            val pageSize = params.loadSize
            val startIndex = page * pageSize

            // 가짜 데이터 생성
            val news = (startIndex until startIndex + pageSize).map { index ->
                NewsItem(
                    id = index,
                    title = "뉴스 제목 #${index + 1}",
                    summary = "이것은 뉴스 ${index + 1}번의 요약입니다. Paging으로 효율적으로 로드되었습니다.",
                    category = listOf("정치", "경제", "사회", "문화", "스포츠")[index % 5],
                    timestamp = System.currentTimeMillis() - (index * 60000)
                )
            }

            LoadResult.Page(
                data = news,
                prevKey = if (page == 0) null else page - 1,
                nextKey = page + 1 // 무한 스크롤 시뮬레이션
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

// ========================================
// 2. ViewModel
// ========================================
class NewsViewModel : ViewModel() {

    // Pager 생성 및 Flow 캐싱
    val newsFlow: Flow<PagingData<NewsItem>> = Pager(
        config = PagingConfig(
            pageSize = 20,          // 한 페이지에 20개
            prefetchDistance = 5,   // 끝에서 5개 전에 다음 페이지 로드
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NewsPagingSource() }
    ).flow.cachedIn(viewModelScope) // Configuration Change 시 데이터 유지
}

// ========================================
// 3. Compose UI
// ========================================
@Composable
fun SolutionScreen(viewModel: NewsViewModel = viewModel()) {
    // PagingData Flow를 LazyPagingItems로 수집
    val lazyPagingItems = viewModel.newsFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 솔루션 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "해결책: Paging 3 + Compose",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "• 페이지 단위로 20개씩 로드\n" +
                    "• 스크롤할 때 자동으로 다음 페이지 로드\n" +
                    "• 로딩/에러 상태 자동 관리",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
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
                        Text("로드된 아이템: ${lazyPagingItems.itemCount}개")
                        Text("로드 상태: ${getLoadStateText(lazyPagingItems.loadState.refresh)}")
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Append: ${getLoadStateText(lazyPagingItems.loadState.append)}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 콘텐츠 영역
        when (val refreshState = lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                // 첫 로드 중
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("첫 페이지 로딩 중...")
                        Text(
                            "20개만 먼저 로드합니다",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            is LoadState.Error -> {
                // 에러 발생
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("에러 발생: ${refreshState.error.message}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { lazyPagingItems.retry() }) {
                            Text("다시 시도")
                        }
                    }
                }
            }

            is LoadState.NotLoading -> {
                // 뉴스 목록
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = { index -> lazyPagingItems.peek(index)?.id ?: index }
                    ) { index ->
                        lazyPagingItems[index]?.let { news ->
                            SolutionNewsCard(news)
                        }
                    }

                    // 다음 페이지 로딩 인디케이터
                    when (lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Text(
                                            "다음 페이지 로딩 중...",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextButton(onClick = { lazyPagingItems.retry() }) {
                                        Text("로드 실패. 다시 시도")
                                    }
                                }
                            }
                        }

                        is LoadState.NotLoading -> { }
                    }
                }
            }
        }

        // Refresh 버튼
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { lazyPagingItems.refresh() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("새로고침")
        }
    }
}

@Composable
fun SolutionNewsCard(news: NewsItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    news.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            news.category,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
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

private fun getLoadStateText(loadState: LoadState): String {
    return when (loadState) {
        is LoadState.Loading -> "로딩 중"
        is LoadState.Error -> "에러"
        is LoadState.NotLoading -> if (loadState.endOfPaginationReached) "완료" else "대기"
    }
}
