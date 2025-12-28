package com.example.shared_element_transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Shared Element Transition - 해결책
 *
 * SharedTransitionLayout을 사용하여 화면 전환 시 요소들이
 * 시각적으로 연결되어 자연스럽게 전환됩니다.
 *
 * 핵심 변경사항:
 * 1. NavHost를 SharedTransitionLayout으로 감쌈
 * 2. 각 화면에 sharedTransitionScope, animatedVisibilityScope 전달
 * 3. 색상 박스에 sharedElement modifier 적용
 * 4. 텍스트에 sharedBounds modifier 적용 (폰트 크기 변화 지원)
 *
 * 아래 앱을 실행하고 아이템을 클릭해보세요.
 * 요소들이 부드럽게 전환되는 것을 확인할 수 있습니다!
 */

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SolutionScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // 해결책 설명 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: SharedTransitionLayout",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |아래 리스트에서 아이템을 클릭해보세요.
                        |
                        |개선된 점:
                        |1. 색상 박스가 부드럽게 확대되며 이동
                        |2. 텍스트도 자연스럽게 전환
                        |3. 선택한 아이템이 어떤 것인지 시각적으로 명확
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // SharedTransitionLayout 적용
        SolutionSnackGallery()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SolutionSnackGallery() {
    // 핵심 1: SharedTransitionLayout으로 NavHost 감싸기
    SharedTransitionLayout {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("list") {
                SolutionListScreen(
                    // 핵심 2: SharedTransitionScope 전달
                    sharedTransitionScope = this@SharedTransitionLayout,
                    // 핵심 3: AnimatedVisibilityScope 전달
                    animatedVisibilityScope = this@composable,
                    onSnackClick = { snackId ->
                        navController.navigate("detail/$snackId")
                    }
                )
            }
            composable("detail/{snackId}") { backStackEntry ->
                val snackId = backStackEntry.arguments?.getString("snackId") ?: ""
                SolutionDetailScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    snackId = snackId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SolutionListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSnackClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sampleSnacks) { snack ->
            SolutionSnackCard(
                snack = snack,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onClick = { onSnackClick(snack.id) }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SolutionSnackCard(
    snack: Snack,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit
) {
    // with(sharedTransitionScope)를 사용하여 scope 내에서 sharedElement 사용
    with(sharedTransitionScope) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 핵심 4: 색상 박스에 sharedElement 적용
                // 동일한 key ("image-${snack.id}")를 사용하여 상세 화면과 연결
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "image-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .background(snack.color)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // 핵심 5: 텍스트에 sharedBounds 적용
                    // sharedBounds는 폰트 크기가 달라질 때 부드럽게 스케일링
                    Text(
                        text = snack.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "text-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = snack.price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "price-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SolutionDetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    snackId: String,
    onBack: () -> Unit
) {
    val snack = sampleSnacks.find { it.id == snackId } ?: return

    with(sharedTransitionScope) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(snack.name) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "뒤로가기"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 핵심 6: 상세 화면에서도 동일한 key로 sharedElement 적용
                // 리스트의 작은 박스와 연결되어 부드럽게 확대됨
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "image-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .background(snack.color)
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    // 핵심 7: 상세 화면 텍스트에도 sharedBounds 적용
                    Text(
                        text = snack.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "text-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = snack.price,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "price-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // 설명은 공유 요소가 아니므로 일반 Text
                    Text(
                        text = snack.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
