package com.example.pausable_composition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pausable_composition.ui.theme.PerformanceBad
import com.example.pausable_composition.ui.theme.PerformanceGood
import com.example.pausable_composition.ui.theme.PerformanceWarning
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 문제 상황 화면
 *
 * Pausable Composition이 없다면 발생할 수 있는 jank 현상을 시연합니다.
 * 복잡한 아이템을 가진 LazyColumn에서 빠른 스크롤 시 프레임 드랍을 관찰합니다.
 */
@Composable
fun ProblemScreen() {
    var showDemo by remember { mutableStateOf(false) }

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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        복잡한 아이템이 많은 리스트를 빠르게 스크롤하면 화면이 끊기는 현상(jank)이 발생합니다.

                        이유:
                        - 각 아이템을 구성하는 데 시간이 걸립니다
                        - 프레임 예산(16ms)을 초과하면 프레임을 건너뜁니다
                        - 사용자는 "버벅거림"을 느끼게 됩니다
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 프레임 예산 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "프레임 예산이란?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        화면이 부드럽게 보이려면 초당 60프레임을 유지해야 합니다.

                        1초 = 1000ms
                        1000ms / 60프레임 = 약 16ms

                        각 프레임마다 16ms 안에 모든 작업을 끝내야 합니다!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 시각적 타임라인
                Text(
                    text = "프레임 예산 초과 예시:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(20.dp)
                            .background(PerformanceBad)
                    )
                    Text(
                        text = " 50ms - JANK!",
                        style = MaterialTheme.typography.labelSmall,
                        color = PerformanceBad
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.32f)
                            .height(20.dp)
                            .background(PerformanceGood)
                    )
                    Spacer(modifier = Modifier.weight(0.68f))
                    Text(
                        text = " 16ms - OK",
                        style = MaterialTheme.typography.labelSmall,
                        color = PerformanceGood
                    )
                }
            }
        }

        // 데모 버튼
        Button(
            onClick = { showDemo = !showDemo },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showDemo) "데모 숨기기" else "복잡한 리스트 데모 보기")
        }

        // 데모 영역
        if (showDemo) {
            JankDemoList()
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 프레임 드랍: 16ms를 초과하면 해당 프레임을 건너뜀")
                Text("2. 스크롤 끊김: 사용자가 버벅거림을 느낌")
                Text("3. UX 저하: 앱이 느리다는 인상을 줌")
            }
        }

        // 비유 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "비유: 택배 배송 센터",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        기존 방식 (Pausable Composition 이전):
                        "모든 택배를 한 트럭에 다 실어야 출발할 수 있습니다."
                        큰 짐이 있으면 출발이 늦어지죠. (= jank)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * Jank를 시연하기 위한 복잡한 리스트
 */
@Composable
private fun JankDemoList() {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isAutoScrolling by remember { mutableStateOf(false) }
    var frameTime by remember { mutableLongStateOf(0L) }
    var lastFrameTime by remember { mutableLongStateOf(System.nanoTime()) }

    // 프레임 시간 측정
    SideEffect {
        val current = System.nanoTime()
        frameTime = (current - lastFrameTime) / 1_000_000
        lastFrameTime = current
    }

    // 샘플 데이터
    val feedItems = remember {
        (1..50).map { index ->
            FeedItem(
                id = index,
                userName = "사용자 $index",
                timeAgo = "${index}분 전",
                content = "이것은 피드 아이템 $index 의 내용입니다. " +
                        "복잡한 레이아웃을 시뮬레이션하기 위해 긴 텍스트를 포함합니다. " +
                        "여러 줄의 텍스트가 렌더링되면 더 많은 시간이 필요합니다.",
                likes = (10..1000).random(),
                comments = (0..100).random()
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // 프레임 시간 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when {
                    frameTime <= 16 -> PerformanceGood.copy(alpha = 0.2f)
                    frameTime <= 32 -> PerformanceWarning.copy(alpha = 0.2f)
                    else -> PerformanceBad.copy(alpha = 0.2f)
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "프레임 시간: ${frameTime}ms",
                    color = when {
                        frameTime <= 16 -> PerformanceGood
                        frameTime <= 32 -> PerformanceWarning
                        else -> PerformanceBad
                    },
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                        isAutoScrolling = !isAutoScrolling
                        if (isAutoScrolling) {
                            scope.launch {
                                // 빠른 자동 스크롤
                                while (isAutoScrolling) {
                                    listState.animateScrollToItem(
                                        (listState.firstVisibleItemIndex + 5)
                                            .coerceAtMost(feedItems.size - 1)
                                    )
                                    delay(100)
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAutoScrolling)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (isAutoScrolling) "스크롤 중지" else "빠른 스크롤")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 복잡한 아이템 리스트
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(feedItems, key = { it.id }) { item ->
                ComplexFeedItemCard(item)
            }
        }
    }
}

/**
 * 복잡한 피드 아이템 카드
 * 여러 UI 요소가 조합되어 렌더링 시간이 길어집니다.
 */
@Composable
private fun ComplexFeedItemCard(item: FeedItem) {
    var isLiked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 프로필 영역
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 프로필 이미지 플레이스홀더
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = item.userName.first().toString(),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.userName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = item.timeAgo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 본문 텍스트
            Text(
                text = item.content,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 미디어 플레이스홀더
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "이미지 영역",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 액션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 좋아요 버튼
                TextButton(
                    onClick = { isLiked = !isLiked }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "좋아요",
                        tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${item.likes + if (isLiked) 1 else 0}")
                }

                // 댓글 버튼
                TextButton(onClick = { }) {
                    Text("댓글 ${item.comments}")
                }

                // 공유 버튼
                TextButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "공유"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("공유")
                }
            }
        }
    }
}

/**
 * 피드 아이템 데이터 클래스
 */
data class FeedItem(
    val id: Int,
    val userName: String,
    val timeAgo: String,
    val content: String,
    val likes: Int,
    val comments: Int
)
