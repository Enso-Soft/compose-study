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
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

/**
 * ====================================================
 * Practice 1: PagingSource 구현
 * ====================================================
 *
 * 과제: 아래 ProductPagingSource를 완성하세요.
 *
 * 힌트:
 * - load() 메서드에서 LoadResult.Page 반환
 * - prevKey: 첫 페이지면 null, 아니면 page - 1
 * - nextKey: 마지막 페이지면 null, 아니면 page + 1
 */

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String
)

class ProductPagingSource : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 0

        // TODO: PagingSource 구현하기
        // 1. delay(500)으로 네트워크 시뮬레이션
        // 2. 상품 데이터 생성 (page * pageSize부터 시작)
        // 3. LoadResult.Page 반환
        //    - data: 생성된 상품 목록
        //    - prevKey: 첫 페이지면 null
        //    - nextKey: 다음 페이지 (최대 5페이지까지만)

        // ===== 정답 (주석 해제하여 확인) =====
        /*
        return try {
            delay(500)

            val pageSize = params.loadSize
            val startIndex = page * pageSize

            val products = (startIndex until startIndex + pageSize).map { index ->
                Product(
                    id = index,
                    name = "상품 #${index + 1}",
                    price = (1000..50000).random(),
                    category = listOf("전자기기", "의류", "식품", "도서")[index % 4]
                )
            }

            LoadResult.Page(
                data = products,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page >= 4) null else page + 1  // 5페이지까지만
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
        */

        // 임시 반환 (위 TODO 완성 후 삭제)
        return LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}

@Composable
fun Practice1_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Practice 1: PagingSource 구현",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "ProductPagingSource의 load() 메서드를 완성하세요.\n\n" +
                    "힌트:\n" +
                    "• LoadResult.Page(data, prevKey, nextKey) 반환\n" +
                    "• 첫 페이지면 prevKey = null\n" +
                    "• 마지막 페이지면 nextKey = null",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 테스트용 UI (PagingSource 구현 완료 후 동작)
        Text(
            "PagingSource를 완성하면 아래에 상품 목록이 표시됩니다.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * ====================================================
 * Practice 2: LoadState 처리
 * ====================================================
 *
 * 과제: 로딩/에러 상태에 따른 UI를 구현하세요.
 *
 * 힌트:
 * - loadState.refresh: 첫 로드 상태
 * - loadState.append: 다음 페이지 로드 상태
 * - LoadState.Loading, LoadState.Error, LoadState.NotLoading
 */

class MessagePagingSource(
    private val shouldFail: Boolean = false
) : PagingSource<Int, String>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = params.key ?: 0
        delay(1000)

        // 에러 시뮬레이션 (3페이지에서 실패)
        if (shouldFail && page == 2) {
            return LoadResult.Error(Exception("네트워크 오류 발생!"))
        }

        val messages = (0 until params.loadSize).map { i ->
            "메시지 #${page * params.loadSize + i + 1}"
        }

        return LoadResult.Page(
            data = messages,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (page >= 4) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? = null
}

class Practice2ViewModel : ViewModel() {
    val messagesFlow: Flow<PagingData<String>> = Pager(
        config = PagingConfig(pageSize = 10, prefetchDistance = 3),
        pagingSourceFactory = { MessagePagingSource(shouldFail = true) }
    ).flow.cachedIn(viewModelScope)
}

@Composable
fun Practice2_Screen(viewModel: Practice2ViewModel = viewModel()) {
    val lazyPagingItems = viewModel.messagesFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Practice 2: LoadState 처리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "3페이지에서 의도적으로 에러가 발생합니다.\n" +
                    "에러 UI와 retry 버튼을 구현하세요.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: LoadState에 따른 UI 구현
        // 1. loadState.refresh가 Loading이면 → CircularProgressIndicator
        // 2. loadState.refresh가 Error이면 → 에러 메시지 + retry 버튼
        // 3. loadState.append가 Loading이면 → 목록 하단에 로딩 표시
        // 4. loadState.append가 Error이면 → 목록 하단에 retry 버튼

        // ===== 정답 (주석 해제하여 확인) =====
        /*
        when (val refreshState = lazyPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("에러: ${refreshState.error.message}")
                        Button(onClick = { lazyPagingItems.retry() }) {
                            Text("다시 시도")
                        }
                    }
                }
            }
            is LoadState.NotLoading -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lazyPagingItems.itemCount) { index ->
                        lazyPagingItems[index]?.let { message ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(message, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }

                    when (val appendState = lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                        is LoadState.Error -> {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
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
        */

        // 임시 UI (위 TODO 완성 후 삭제)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { message ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(message, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

/**
 * ====================================================
 * Practice 3: cachedIn 효과 확인
 * ====================================================
 *
 * 과제: cachedIn 사용 전후의 차이를 확인하세요.
 *
 * 힌트:
 * - cachedIn(viewModelScope) 없이 → 화면 회전 시 데이터 재로드
 * - cachedIn(viewModelScope) 사용 → 데이터 유지
 */

// cachedIn 미사용 (문제 상황)
class Practice3BadViewModel : ViewModel() {
    // 주의: cachedIn 없음!
    val itemsFlow: Flow<PagingData<String>> = Pager(
        config = PagingConfig(pageSize = 15),
        pagingSourceFactory = {
            object : PagingSource<Int, String>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
                    val page = params.key ?: 0
                    delay(800)

                    val items = (0 until params.loadSize).map { i ->
                        "아이템 #${page * params.loadSize + i + 1} (${System.currentTimeMillis() % 10000})"
                    }

                    return LoadResult.Page(
                        data = items,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (page >= 3) null else page + 1
                    )
                }

                override fun getRefreshKey(state: PagingState<Int, String>): Int? = null
            }
        }
    ).flow
    // TODO: .cachedIn(viewModelScope) 추가하기
}

// cachedIn 사용 (올바른 방법)
class Practice3GoodViewModel : ViewModel() {
    val itemsFlow: Flow<PagingData<String>> = Pager(
        config = PagingConfig(pageSize = 15),
        pagingSourceFactory = {
            object : PagingSource<Int, String>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
                    val page = params.key ?: 0
                    delay(800)

                    val items = (0 until params.loadSize).map { i ->
                        "아이템 #${page * params.loadSize + i + 1} (${System.currentTimeMillis() % 10000})"
                    }

                    return LoadResult.Page(
                        data = items,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (page >= 3) null else page + 1
                    )
                }

                override fun getRefreshKey(state: PagingState<Int, String>): Int? = null
            }
        }
    ).flow.cachedIn(viewModelScope) // cachedIn 적용!
}

@Composable
fun Practice3_Screen() {
    var useCachedIn by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Practice 3: cachedIn 효과",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "화면을 회전하거나 다른 탭으로 이동 후 돌아와보세요.\n\n" +
                    "• cachedIn 없음: 데이터 재로드 (타임스탬프 변경)\n" +
                    "• cachedIn 있음: 데이터 유지 (타임스탬프 동일)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 토글
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = !useCachedIn,
                onClick = { useCachedIn = false },
                label = { Text("cachedIn 없음") }
            )
            FilterChip(
                selected = useCachedIn,
                onClick = { useCachedIn = true },
                label = { Text("cachedIn 있음") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 콘텐츠
        if (useCachedIn) {
            Practice3GoodContent()
        } else {
            Practice3BadContent()
        }
    }
}

@Composable
private fun Practice3BadContent(viewModel: Practice3BadViewModel = viewModel()) {
    val lazyPagingItems = viewModel.itemsFlow.collectAsLazyPagingItems()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "cachedIn 미사용 - 탭 이동 시 재로드됨",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    when (lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(lazyPagingItems.itemCount) { index ->
                    lazyPagingItems[index]?.let { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(item, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice3GoodContent(viewModel: Practice3GoodViewModel = viewModel()) {
    val lazyPagingItems = viewModel.itemsFlow.collectAsLazyPagingItems()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "cachedIn 사용 - 탭 이동해도 데이터 유지",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    when (lazyPagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(lazyPagingItems.itemCount) { index ->
                    lazyPagingItems[index]?.let { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(item, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}
