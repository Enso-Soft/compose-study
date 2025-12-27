package com.example.visibility_tracking

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - 기존 방식의 가시성 추적
 *
 * 이 코드의 문제점:
 * 1. onGloballyPositioned는 매 레이아웃 패스마다 모든 아이템에서 호출됨
 * 2. 디바운스/스로틀이 없어서 스크롤 시 과도한 콜백 발생
 * 3. 가시성 계산 로직을 직접 구현해야 함
 * 4. 노출 시간 조건을 추가하려면 복잡한 타이머 로직 필요
 */
@Composable
fun ProblemScreen() {
    var callbackCount by remember { mutableIntStateOf(0) }
    var visibleItems by remember { mutableStateOf(setOf<Int>()) }
    var impressionLog by remember { mutableStateOf(listOf<String>()) }

    // 화면 높이를 가져오기 위한 설정
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    // Recomposition 추적
    SideEffect {
        Log.d("VisibilityProblem", "Recomposition occurred, callback count: $callbackCount")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Problem: 기존 방식의 가시성 추적",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 문제 상황 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "onGloballyPositioned 콜백 횟수: $callbackCount",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "현재 보이는 아이템: ${visibleItems.size}개",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "스크롤하면 콜백이 과도하게 발생합니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 문제점 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "기존 방식의 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 매 레이아웃 패스마다 모든 아이템 콜백")
                Text("2. 디바운스/스로틀 미지원")
                Text("3. 수동 가시성 계산 필요")
                Text("4. 노출 시간 조건 구현 복잡")
                Text("5. 중복 노출 추적 가능성")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 있는 LazyColumn
        Text(
            text = "아래 리스트를 스크롤해보세요:",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 문제 있는 코드 - onGloballyPositioned 사용
        // 주의: 실제로 실행하면 성능 문제가 발생할 수 있습니다
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(30) { index ->
                ProblematicAdCard(
                    adId = index,
                    screenHeightPx = screenHeightPx,
                    onPositionCheck = { isVisible ->
                        callbackCount++
                        if (isVisible) {
                            visibleItems = visibleItems + index
                            // 문제: 중복 로깅 가능
                            val log = "Ad $index impression at ${System.currentTimeMillis()}"
                            impressionLog = (impressionLog + log).takeLast(5)
                        } else {
                            visibleItems = visibleItems - index
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 최근 노출 로그
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "최근 노출 로그 (중복 발생 가능!):",
                    style = MaterialTheme.typography.labelMedium
                )
                if (impressionLog.isEmpty()) {
                    Text(
                        text = "스크롤하면 로그가 표시됩니다",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    impressionLog.forEach { log ->
                        Text(
                            text = log,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

/**
 * 문제 있는 광고 카드 - onGloballyPositioned 사용
 */
@Composable
private fun ProblematicAdCard(
    adId: Int,
    screenHeightPx: Float,
    onPositionCheck: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 4.dp)
            // 문제: onGloballyPositioned는 매 레이아웃 패스마다 호출됨
            .onGloballyPositioned { coordinates ->
                // 수동으로 가시성 계산
                val bounds = coordinates.boundsInWindow()
                val itemTop = bounds.top
                val itemBottom = bounds.bottom

                // 아이템이 화면에 보이는지 확인
                val isVisible = itemBottom > 0 && itemTop < screenHeightPx

                // 콜백 호출 - 성능 문제!
                onPositionCheck(isVisible)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Ad #$adId",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "(onGloballyPositioned 사용)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 또 다른 문제 - LaunchedEffect를 visibility 신호로 사용
 *
 * 이 코드는 주석 처리되어 있습니다.
 * 실제로 사용하면 prefetch로 인해 잘못된 impression이 발생합니다.
 */
/*
@Composable
fun ProblematicLaunchedEffectExample(ads: List<Ad>) {
    LazyColumn {
        items(ads, key = { it.id }) { ad ->
            AdCard(ad)

            // 문제: prefetch로 인해 화면 표시 전에 실행될 수 있음!
            LaunchedEffect(ad.id) {
                analytics.trackAdImpression(ad.id)
            }
        }
    }
}
*/
