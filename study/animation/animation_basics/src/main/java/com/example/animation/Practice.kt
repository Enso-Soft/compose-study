package com.example.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 좋아요 버튼 애니메이션
 *
 * 요구사항:
 * - 버튼을 누르면 아이콘의 크기가 커졌다가 원래대로 돌아옴 (1f → 1.3f → 1f 효과)
 * - 색상이 Gray에서 Red로 애니메이션
 *
 * TODO: animateFloatAsState와 animateColorAsState를 사용하세요!
 */
@Composable
fun Practice1_LikeButton() {
    var isLiked by remember { mutableStateOf(false) }

    // TODO 1: animateFloatAsState로 scale 애니메이션
    // 힌트: targetValue = if (isLiked) 1.3f else 1f
    // 힌트: animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    val scale = 1f  // 이 줄을 수정하세요!

    // TODO 2: animateColorAsState로 색상 애니메이션
    // 힌트: targetValue = if (isLiked) Color.Red else Color.Gray
    val iconColor = if (isLiked) Color.Red else Color.Gray  // 이 줄을 수정하세요!

    /*
    // 정답 코드
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.3f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "like scale"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300),
        label = "like color"
    )
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 1: 좋아요 버튼",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: animateFloatAsState와 animateColorAsState를 사용하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 좋아요 버튼
        IconButton(
            onClick = { isLiked = !isLiked },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = iconColor,
                modifier = Modifier
                    .size(64.dp)
                    .scale(scale)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isLiked) "좋아요!" else "좋아요를 눌러주세요",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. animateFloatAsState로 scale 값 애니메이션")
                Text("2. animateColorAsState로 색상 값 애니메이션")
                Text("3. spring()으로 탄성 효과 적용")
                Text("4. Modifier.scale(scale)로 크기 적용")
            }
        }
    }
}

/**
 * 연습 문제 2: 확장 가능한 카드
 *
 * 요구사항:
 * - 카드를 클릭하면 추가 정보가 애니메이션과 함께 나타남
 * - AnimatedVisibility 사용
 * - enter에 fadeIn + expandVertically 조합
 * - exit에 fadeOut + shrinkVertically 조합
 *
 * TODO: AnimatedVisibility를 사용하세요!
 */
@Composable
fun Practice2_ExpandableCard() {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "연습 2: 확장 카드",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: AnimatedVisibility를 사용하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 확장 가능한 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jetpack Compose Animation",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }

                // TODO: AnimatedVisibility로 감싸세요!
                // 힌트: enter = fadeIn() + expandVertically()
                // 힌트: exit = fadeOut() + shrinkVertically()

                // 현재는 즉각적으로 나타나거나 사라짐
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Compose 애니메이션은 상태 변화를 시각적으로 부드럽게 " +
                                "표현할 수 있게 해줍니다. animate*AsState, AnimatedVisibility, " +
                                "Crossfade 등 다양한 API를 제공합니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "주요 API: animateDpAsState, animateColorAsState, " +
                                "animateFloatAsState, AnimatedVisibility, Crossfade",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                /*
                // 정답 코드
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(300)) +
                            expandVertically(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) +
                            shrinkVertically(animationSpec = tween(300))
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Compose 애니메이션은 상태 변화를 시각적으로 부드럽게 " +
                                    "표현할 수 있게 해줍니다. animate*AsState, AnimatedVisibility, " +
                                    "Crossfade 등 다양한 API를 제공합니다.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "주요 API: animateDpAsState, animateColorAsState, " +
                                    "animateFloatAsState, AnimatedVisibility, Crossfade",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                */
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. if (isExpanded) 부분을 AnimatedVisibility로 감싸기")
                Text("2. enter에 fadeIn() + expandVertically() 조합")
                Text("3. exit에 fadeOut() + shrinkVertically() 조합")
                Text("4. + 연산자로 여러 transition 조합 가능")
            }
        }
    }
}

/**
 * 연습 문제 3: 탭 전환 애니메이션
 *
 * 요구사항:
 * - 두 개의 탭 사이를 부드럽게 전환
 * - Crossfade 사용
 *
 * TODO: Crossfade를 사용하세요!
 */
@Composable
fun Practice3_TabContent() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: 탭 전환",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: Crossfade를 사용하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 탭 바
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("홈") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("프로필") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Crossfade로 감싸세요!
        // 힌트: Crossfade(targetState = selectedTab, label = "tab") { ... }
        // 힌트: when (it) 으로 탭별 콘텐츠 분기

        // 현재는 즉각적으로 전환됨
        when (selectedTab) {
            0 -> HomeContent()
            1 -> ProfileContent()
        }

        /*
        // 정답 코드
        Crossfade(
            targetState = selectedTab,
            animationSpec = tween(500),
            label = "tab crossfade"
        ) { tab ->
            when (tab) {
                0 -> HomeContent()
                1 -> ProfileContent()
            }
        }
        */

        Spacer(modifier = Modifier.height(32.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. when 블록을 Crossfade로 감싸기")
                Text("2. targetState에 selectedTab 전달")
                Text("3. 람다 파라미터(it 또는 tab)로 분기")
                Text("4. animationSpec으로 전환 속도 조절")
            }
        }
    }
}

@Composable
private fun HomeContent() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "홈 화면",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "여기는 홈 콘텐츠입니다",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ProfileContent() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "프로필 화면",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "여기는 프로필 콘텐츠입니다",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
