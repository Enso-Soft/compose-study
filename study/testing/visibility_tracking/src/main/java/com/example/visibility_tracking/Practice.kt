package com.example.visibility_tracking

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 가시성 로깅
 *
 * 요구사항:
 * - LazyColumn의 각 아이템이 화면에 50% 이상 보일 때 로그 출력
 * - 로그 형식: "Item X is visible"
 * - visibleCount를 증가시켜 화면에 표시
 *
 * TODO: onVisibilityChanged를 사용해서 구현하세요!
 *
 * 참고: 현재 BOM 버전에 따라 onVisibilityChanged가 없을 수 있습니다.
 * 그 경우 주석 처리된 정답을 참고하세요.
 */
@Composable
fun Practice1_BasicVisibilityLogging() {
    var visibleCount by remember { mutableIntStateOf(0) }
    val visibleItems = remember { mutableStateSetOf<Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 기본 가시성 로깅",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 아이템이 50% 이상 보일 때 로그를 출력하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "- Modifier.onVisibilityChanged(minFractionVisible = 0.5f) 사용",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "- 콜백에서 if (isVisible) Log.d(...) 호출",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "- visibleItems.add(index)로 중복 방지",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "보인 아이템 수: ${visibleItems.size}개",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(20) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 4.dp)
                        // TODO: 여기에 onVisibilityChanged 추가
                        // 힌트:
                        // .onVisibilityChanged(minFractionVisible = 0.5f) { visible ->
                        //     if (visible && index !in visibleItems) {
                        //         visibleItems.add(index)
                        //         Log.d("Practice1", "Item $index is visible")
                        //     }
                        // }
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = if (index in visibleItems)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Item #$index",
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (index in visibleItems) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Seen",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /*
    // 정답 코드 (onVisibilityChanged가 있는 BOM 버전에서):
    LazyColumn {
        items(20) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 4.dp)
                    .onVisibilityChanged(minFractionVisible = 0.5f) { visible ->
                        if (visible && index !in visibleItems) {
                            visibleItems.add(index)
                            Log.d("Practice1", "Item $index is visible")
                        }
                    }
            ) {
                // content
            }
        }
    }
    */
}

/**
 * 연습 문제 2: 광고 노출 분석
 *
 * 요구사항:
 * - 광고가 50% 이상 화면에 보일 때
 * - 1초(1000ms) 이상 지속될 때
 * - 같은 광고는 한 번만 추적 (중복 방지)
 * - 노출 횟수를 화면에 표시
 *
 * TODO: onVisibilityChanged를 사용해서 구현하세요!
 */
@Composable
fun Practice2_AdImpressionTracking() {
    data class Ad(val id: String, val title: String, val description: String)

    val ads = remember {
        listOf(
            Ad("ad-001", "Special Offer!", "50% 할인 이벤트"),
            Ad("ad-002", "New Product", "신제품 출시"),
            Ad("ad-003", "Limited Time", "한정 특가"),
            Ad("ad-004", "Free Shipping", "무료 배송"),
            Ad("ad-005", "Members Only", "회원 전용 혜택"),
            Ad("ad-006", "Flash Sale", "타임 세일"),
            Ad("ad-007", "Best Seller", "베스트셀러"),
            Ad("ad-008", "Exclusive", "단독 판매")
        )
    }

    val trackedImpressions = remember { mutableStateSetOf<String>() }
    var totalImpressions by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: 광고 노출 분석",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 광고가 50% 이상, 1초 이상 보일 때 노출을 기록하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "- minFractionVisible = 0.5f, minDurationMs = 1000 사용",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "- if (visible && ad.id !in trackedImpressions) 조건",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "- trackedImpressions.add(ad.id)로 중복 방지",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 통계 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "총 노출 수",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "$totalImpressions",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "고유 광고",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${trackedImpressions.size} / ${ads.size}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(ads, key = { it.id }) { ad ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(vertical = 4.dp)
                        // TODO: 여기에 onVisibilityChanged 추가
                        // 힌트:
                        // .onVisibilityChanged(
                        //     minFractionVisible = 0.5f,
                        //     minDurationMs = 1000
                        // ) { visible ->
                        //     if (visible && ad.id !in trackedImpressions) {
                        //         trackedImpressions.add(ad.id)
                        //         totalImpressions++
                        //         Log.d("Practice2", "Ad ${ad.id} impression tracked")
                        //     }
                        // }
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = if (ad.id in trackedImpressions)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ad.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = ad.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (ad.id in trackedImpressions)
                                    "Impression Recorded"
                                else
                                    "Waiting... (50%, 1sec)",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (ad.id in trackedImpressions)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (ad.id in trackedImpressions) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Tracked",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    /*
    // 정답 코드:
    .onVisibilityChanged(
        minFractionVisible = 0.5f,
        minDurationMs = 1000
    ) { visible ->
        if (visible && ad.id !in trackedImpressions) {
            trackedImpressions.add(ad.id)
            totalImpressions++
            Log.d("Practice2", "Ad ${ad.id} impression tracked")
        }
    }
    */
}

/**
 * 연습 문제 3: 비디오 자동재생
 *
 * 요구사항:
 * - 비디오가 100% 화면에 보일 때 (minFractionVisible = 1f)
 * - 0.5초(500ms) 후 자동 재생 시작
 * - 화면에서 벗어나면 즉시 일시정지
 * - 재생 상태를 UI에 표시
 *
 * TODO: onVisibilityChanged를 사용해서 구현하세요!
 */
@Composable
fun Practice3_VideoAutoplay() {
    data class Video(
        val id: Int,
        val title: String,
        val duration: String
    )

    val videos = remember {
        listOf(
            Video(1, "Cute Cat Video", "2:30"),
            Video(2, "Amazing Sunset", "1:45"),
            Video(3, "Cooking Tutorial", "5:20"),
            Video(4, "Travel Vlog", "8:15"),
            Video(5, "Music Performance", "3:40"),
            Video(6, "Tech Review", "12:00"),
            Video(7, "Fitness Workout", "10:30"),
            Video(8, "Art Tutorial", "6:45")
        )
    }

    // 현재 재생 중인 비디오 ID (하나만 재생 가능)
    var playingVideoId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: 비디오 자동재생",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: 비디오가 100% 보이고 0.5초 후 자동재생하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "- minFractionVisible = 1f (100% 보일 때만)",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "- minDurationMs = 500 (0.5초 후)",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "- visible = true면 재생, false면 일시정지",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 현재 상태 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (playingVideoId != null)
                    MaterialTheme.colorScheme.tertiaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = playingVideoId?.let {
                    "Now Playing: ${videos.find { v -> v.id == it }?.title}"
                } ?: "No video playing",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(videos, key = { _, v -> v.id }) { index, video ->
                val isPlaying = playingVideoId == video.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(vertical = 4.dp)
                        // TODO: 여기에 onVisibilityChanged 추가
                        // 힌트:
                        // .onVisibilityChanged(
                        //     minFractionVisible = 1f,
                        //     minDurationMs = 500
                        // ) { visible ->
                        //     if (visible) {
                        //         playingVideoId = video.id
                        //     } else if (playingVideoId == video.id) {
                        //         playingVideoId = null
                        //     }
                        // }
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPlaying)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 재생 버튼 (토글)
                        IconButton(
                            onClick = {
                                playingVideoId = if (isPlaying) null else video.id
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = if (isPlaying)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Duration: ${video.duration}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isPlaying) "Playing..." else "Tap to play",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isPlaying)
                                    MaterialTheme.colorScheme.tertiary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // 재생 상태 표시
                        if (isPlaying) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "LIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    /*
    // 정답 코드:
    .onVisibilityChanged(
        minFractionVisible = 1f,
        minDurationMs = 500
    ) { visible ->
        if (visible) {
            playingVideoId = video.id
            Log.d("Practice3", "Video ${video.id} started playing")
        } else if (playingVideoId == video.id) {
            playingVideoId = null
            Log.d("Practice3", "Video ${video.id} paused")
        }
    }
    */
}
