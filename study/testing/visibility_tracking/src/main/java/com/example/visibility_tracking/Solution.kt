package com.example.visibility_tracking

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
 * 올바른 코드 - Visibility Tracking API 사용
 *
 * onVisibilityChanged를 사용하면:
 * 1. 효율적인 가시성 추적 (내부 디바운스/스로틀)
 * 2. minFractionVisible로 최소 노출 비율 설정
 * 3. minDurationMs로 최소 노출 시간 설정
 * 4. 깔끔하고 선언적인 코드
 *
 * 버전 요구사항:
 * - onVisibilityChanged: Compose UI 1.9+ (BOM 2025.08.00+)
 * - 권장 버전: BOM 2025.12.00 (성능 최적화, 버그 수정 포함)
 * - 주의: BOM 2025.08.00 ~ 2025.09.00에서 이벤트 누락 버그 있었음 (2025.09.01+에서 수정됨)
 *
 * 현재 프로젝트의 BOM 버전에 따라 onVisibilityChanged가 없을 수 있습니다.
 * 그 경우 이 파일은 개념 설명용으로만 사용됩니다.
 */
@Composable
fun SolutionScreen() {
    // 추적된 노출 (중복 방지)
    val trackedImpressions = remember { mutableStateSetOf<Int>() }
    var totalImpressions by remember { mutableIntStateOf(0) }

    // 비디오 재생 상태 시뮬레이션
    var playingVideoId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Solution: Visibility Tracking API",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 상태 표시 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "총 노출 수: $totalImpressions",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "고유 노출 아이템: ${trackedImpressions.size}개",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "현재 재생 중: ${playingVideoId?.let { "Video #$it" } ?: "없음"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 핵심 포인트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "onVisibilityChanged 핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- minFractionVisible: 50% 이상 보일 때 visible")
                Text("- minDurationMs: 1초 이상 보여야 impression")
                Text("- Set으로 중복 노출 방지")
                Text("- 내부 최적화로 성능 우수")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "스크롤하면 조건 충족 시 노출이 기록됩니다:",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 올바른 방식의 LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(30) { index ->
                // 짝수는 광고 카드, 홀수는 비디오 카드
                if (index % 2 == 0) {
                    SolutionAdCard(
                        adId = index,
                        isTracked = index in trackedImpressions,
                        onImpression = {
                            if (index !in trackedImpressions) {
                                trackedImpressions.add(index)
                                totalImpressions++
                                Log.d("VisibilitySolution", "Ad #$index impression tracked")
                            }
                        }
                    )
                } else {
                    SolutionVideoCard(
                        videoId = index,
                        isPlaying = playingVideoId == index,
                        onVisibilityChange = { visible ->
                            playingVideoId = if (visible) index else {
                                if (playingVideoId == index) null else playingVideoId
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // API 사용법 안내
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Visibility Tracking API 사용법",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
Modifier.onVisibilityChanged(
    minFractionVisible = 0.5f,  // 50% 보일 때
    minDurationMs = 1000        // 1초 후
) { isVisible ->
    if (isVisible) trackImpression()
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 올바른 광고 카드 - Visibility Tracking 시뮬레이션
 *
 * 실제로는 Modifier.onVisibilityChanged를 사용하지만,
 * 이 예제에서는 BOM 버전 호환성을 위해 시뮬레이션합니다.
 *
 * 실제 사용 예:
 * Modifier.onVisibilityChanged(
 *     minFractionVisible = 0.5f,
 *     minDurationMs = 1000
 * ) { visible ->
 *     if (visible) onImpression()
 * }
 */
@Composable
private fun SolutionAdCard(
    adId: Int,
    isTracked: Boolean,
    onImpression: () -> Unit
) {
    // 실제로는 onVisibilityChanged로 구현
    // 이 예제에서는 LaunchedEffect로 시뮬레이션 (1초 후 자동 노출)
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // 실제로는 onVisibilityChanged 콜백에서 처리
        // 이 시뮬레이션에서는 1초 후 자동으로 visible 처리
        kotlinx.coroutines.delay(1000 + (adId * 100L))
        isVisible = true
        onImpression()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTracked)
                MaterialTheme.colorScheme.primaryContainer
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
                    text = "Ad #$adId",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isTracked) "Impression Tracked" else "Waiting for impression...",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isTracked)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "(minFraction: 0.5, minDuration: 1000ms)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isTracked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Tracked",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

/**
 * 비디오 카드 - 가시성 기반 재생 제어 시뮬레이션
 *
 * 실제 사용 예:
 * Modifier.onVisibilityChanged(
 *     minFractionVisible = 1f,
 *     minDurationMs = 500
 * ) { visible ->
 *     if (visible) video.play() else video.pause()
 * }
 */
@Composable
private fun SolutionVideoCard(
    videoId: Int,
    isPlaying: Boolean,
    onVisibilityChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 4.dp),
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
            // 재생 아이콘
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Playing" else "Paused",
                tint = if (isPlaying)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Video #$videoId",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (isPlaying) "Playing..." else "Paused",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPlaying)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "(100% visible, 500ms delay)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 재생 상태 토글 버튼 (데모용)
            Switch(
                checked = isPlaying,
                onCheckedChange = { onVisibilityChange(it) }
            )
        }
    }
}

/**
 * 실제 onVisibilityChanged API 사용 예시 (코드 참조용)
 *
 * 이 함수는 실제로 호출되지 않습니다.
 * Compose BOM 2025.08.00+ (권장: 2025.12.00)에서 동작하는 코드 예시입니다.
 *
 * 주의: onFirstVisible은 Compose 1.11 (BOM 2025.12.00+)에서 deprecated되었습니다.
 * 대신 onVisibilityChanged + 수동 중복 방지 로직을 사용하세요.
 */
/*
@Composable
fun RealVisibilityTrackingExample() {
    val trackedImpressions = remember { mutableSetOf<String>() }

    LazyColumn {
        items(ads, key = { it.id }) { ad ->
            Card(
                modifier = Modifier
                    .onVisibilityChanged(
                        minFractionVisible = 0.5f,
                        minDurationMs = 1000
                    ) { visible ->
                        if (visible && ad.id !in trackedImpressions) {
                            trackedImpressions.add(ad.id)
                            analytics.trackImpression(ad.id)
                        }
                    }
                    .padding(8.dp)
            ) {
                // Ad content
            }
        }
    }
}

@Composable
fun RealVideoAutoplayExample(videos: List<Video>) {
    LazyColumn {
        items(videos, key = { it.id }) { video ->
            VideoPlayer(
                modifier = Modifier
                    .onVisibilityChanged(
                        minFractionVisible = 1f,
                        minDurationMs = 500
                    ) { visible ->
                        if (visible) video.play() else video.pause()
                    }
            )
        }
    }
}

// onFirstVisible 대체 패턴 (deprecated API 마이그레이션)
@Composable
fun OnFirstVisibleMigrationExample() {
    val hasBeenVisible = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .onVisibilityChanged { visible ->
                if (visible && !hasBeenVisible.value) {
                    hasBeenVisible.value = true
                    // 첫 번째 visible 시 한 번만 실행되는 로직
                    analytics.trackFirstImpression()
                }
            }
    ) {
        // Content
    }
}
*/
