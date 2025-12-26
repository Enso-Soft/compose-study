# Paging Compose 학습

## 개념

**Paging 3**는 Android Jetpack 라이브러리로, 대량의 데이터를 **페이지 단위로 점진적으로 로드**하여 효율적으로 표시합니다.

**Paging Compose**는 Paging 3를 Jetpack Compose의 `LazyColumn`/`LazyRow`와 통합하는 확장 라이브러리입니다.

```kotlin
// Paging Compose의 핵심 API
val lazyPagingItems = pager.flow.collectAsLazyPagingItems()

LazyColumn {
    items(lazyPagingItems.itemCount) { index ->
        lazyPagingItems[index]?.let { item ->
            ItemCard(item)
        }
    }
}
```

---

## 핵심 컴포넌트

### 1. PagingSource
데이터를 페이지 단위로 가져오는 **데이터 소스**입니다.

```kotlin
class NewsPagingSource(
    private val api: NewsApi
) : PagingSource<Int, NewsItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        val page = params.key ?: 0

        return try {
            val news = api.getNews(page, params.loadSize)
            LoadResult.Page(
                data = news,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (news.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}
```

### 2. Pager & PagingConfig
`PagingSource`를 사용하여 `PagingData` 스트림을 생성합니다.

```kotlin
val pager = Pager(
    config = PagingConfig(
        pageSize = 20,           // 한 페이지에 20개 아이템
        prefetchDistance = 5,    // 끝에서 5개 전에 다음 페이지 로드
        enablePlaceholders = false
    ),
    pagingSourceFactory = { NewsPagingSource(api) }
)

val pagingDataFlow: Flow<PagingData<NewsItem>> = pager.flow
```

### 3. collectAsLazyPagingItems()
`Flow<PagingData<T>>`를 Compose에서 사용할 수 있는 `LazyPagingItems<T>`로 변환합니다.

```kotlin
@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val lazyPagingItems = viewModel.newsFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { news ->
                NewsCard(news)
            }
        }
    }
}
```

### 4. LoadState
로딩 상태를 나타내며, UI에서 로딩/에러 처리에 사용합니다.

```kotlin
when (lazyPagingItems.loadState.refresh) {
    is LoadState.Loading -> LoadingScreen()
    is LoadState.Error -> ErrorScreen(onRetry = { lazyPagingItems.retry() })
    is LoadState.NotLoading -> ContentScreen()
}
```

---

## 문제 상황: 모든 데이터를 한 번에 로드

### 잘못된 코드 예시
```kotlin
@Composable
fun BadNewsScreen() {
    var news by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        news = api.getAllNews() // 10,000개 뉴스를 한 번에!
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(news) { NewsCard(it) }
        }
    }
}
```

### 발생하는 문제점

1. **메모리 과부하 (OOM)**
   - 수천 개의 아이템을 메모리에 모두 로드
   - 저사양 기기에서 앱 크래시

2. **긴 초기 로딩 시간**
   - 사용자가 첫 화면을 보기까지 오래 대기
   - 사용자 이탈률 증가

3. **네트워크 비효율**
   - 사용자가 보지 않을 데이터까지 다운로드
   - 데이터 요금 낭비

4. **수동 페이징 구현의 복잡성**
   - offset/limit 관리
   - 에러 처리 및 재시도 로직
   - 중복/누락 데이터 처리

---

## 해결책: Paging 3 + Compose 사용

### 올바른 코드

**1. PagingSource 정의**
```kotlin
class NewsPagingSource : PagingSource<Int, NewsItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        val page = params.key ?: 0
        delay(1000) // 네트워크 시뮬레이션

        return LoadResult.Page(
            data = generateNews(page, params.loadSize),
            prevKey = if (page == 0) null else page - 1,
            nextKey = page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? = null
}
```

**2. ViewModel에서 Pager 생성**
```kotlin
class NewsViewModel : ViewModel() {
    val newsFlow = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 5),
        pagingSourceFactory = { NewsPagingSource() }
    ).flow.cachedIn(viewModelScope)
}
```

**3. Compose UI**
```kotlin
@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val lazyPagingItems = viewModel.newsFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { NewsCard(it) }
        }

        // 로딩 인디케이터
        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item { LoadingIndicator() }
        }
    }
}
```

### 해결되는 이유

| 문제 | Paging 3 해결책 |
|------|----------------|
| 메모리 과부하 | 필요한 페이지만 메모리에 유지 |
| 긴 로딩 시간 | 첫 20개만 로드 → 즉시 표시 |
| 네트워크 비효율 | 스크롤할 때만 다음 페이지 로드 |
| 복잡한 구현 | PagingSource 하나로 모든 로직 캡슐화 |

---

## 사용 시나리오

1. **소셜 미디어 피드** - 무한 스크롤 타임라인
2. **검색 결과 목록** - 수천 개의 검색 결과
3. **채팅 메시지 히스토리** - 과거 메시지 로드
4. **이커머스 상품 목록** - 카테고리별 상품 브라우징

---

## 주요 API 정리

| API | 설명 |
|-----|------|
| `PagingSource<Key, Value>` | 데이터 소스 정의 |
| `Pager(config, pagingSourceFactory)` | PagingData 스트림 생성 |
| `PagingConfig(pageSize, prefetchDistance)` | 페이징 설정 |
| `collectAsLazyPagingItems()` | Flow를 LazyPagingItems로 변환 |
| `lazyPagingItems.loadState` | 로딩 상태 확인 |
| `lazyPagingItems.retry()` | 에러 시 재시도 |
| `lazyPagingItems.refresh()` | 처음부터 다시 로드 |
| `cachedIn(viewModelScope)` | Configuration Change 시 데이터 유지 |

---

## 주의사항

1. **cachedIn() 필수**: ViewModel에서 `cachedIn(viewModelScope)`을 사용하지 않으면 화면 회전 시 데이터가 다시 로드됩니다.

2. **LazyPagingItems null 처리**: `lazyPagingItems[index]`는 null일 수 있습니다 (플레이스홀더).

3. **LoadState 처리**: `refresh`, `prepend`, `append` 세 가지 상태를 모두 처리해야 합니다.

4. **key 설정**: `items()` 호출 시 고유 키를 제공하면 성능이 향상됩니다.
   ```kotlin
   items(
       count = lazyPagingItems.itemCount,
       key = { lazyPagingItems.peek(it)?.id ?: it }
   ) { ... }
   ```

---

## 연습 문제

1. **Practice 1: PagingSource 구현** - 기본 PagingSource 완성하기
2. **Practice 2: LoadState 처리** - 로딩/에러 UI 구현하기
3. **Practice 3: cachedIn 적용** - Configuration Change 대응하기

---

## 다음 학습

- `RemoteMediator` - 네트워크 + 로컬 DB 동시 사용
- `PagingDataAdapter` - RecyclerView와 함께 사용 (Compose 외)
