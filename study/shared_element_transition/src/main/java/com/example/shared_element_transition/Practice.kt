package com.example.shared_element_transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
 * Shared Element Transition - 연습 문제
 *
 * 각 연습 문제는 TODO 주석을 따라 구현해보세요.
 * 힌트가 제공되며, 주석 처리된 정답을 참고할 수 있습니다.
 */

// ============================================================
// 연습 1: 기본 SharedTransitionLayout 설정하기 (난이도: 하)
// ============================================================

/**
 * 연습 1: SharedTransitionLayout 기본 설정
 *
 * 목표: NavHost를 SharedTransitionLayout으로 감싸고,
 *       각 화면에 sharedTransitionScope와 animatedVisibilityScope를 전달하세요.
 *
 * TODO:
 * 1. NavHost를 SharedTransitionLayout { } 으로 감싸세요
 * 2. 리스트 화면에 this@SharedTransitionLayout을 sharedTransitionScope로 전달
 * 3. 리스트 화면에 this@composable을 animatedVisibilityScope로 전달
 * 4. 상세 화면에도 동일하게 전달
 *
 * 힌트:
 * - SharedTransitionLayout { NavHost(...) { ... } }
 * - this@SharedTransitionLayout으로 SharedTransitionScope 접근
 * - this@composable로 AnimatedVisibilityScope 접근
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Practice1_BasicSharedTransition() {
    Column(modifier = Modifier.fillMaxSize()) {
        // 힌트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: SharedTransitionLayout 설정",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. NavHost를 SharedTransitionLayout으로 감싸세요
                        |2. 각 화면에 scope를 전달하세요
                        |3. 이미지 Box에 sharedElement modifier를 추가하세요
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // TODO: 아래 NavHost를 SharedTransitionLayout으로 감싸세요
        // 현재는 일반 Navigation으로 동작합니다

        /* 정답:
        SharedTransitionLayout {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "list",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("list") {
                    Practice1ListScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        onSnackClick = { snackId ->
                            navController.navigate("detail/$snackId")
                        }
                    )
                }
                composable("detail/{snackId}") { backStackEntry ->
                    val snackId = backStackEntry.arguments?.getString("snackId") ?: ""
                    Practice1DetailScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        snackId = snackId,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
        */

        // 임시 구현 (SharedTransitionLayout 없음)
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("list") {
                // TODO: sharedTransitionScope와 animatedVisibilityScope 전달
                Practice1ListScreenBasic(
                    onSnackClick = { snackId ->
                        navController.navigate("detail/$snackId")
                    }
                )
            }
            composable("detail/{snackId}") { backStackEntry ->
                val snackId = backStackEntry.arguments?.getString("snackId") ?: ""
                Practice1DetailScreenBasic(
                    snackId = snackId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun Practice1ListScreenBasic(
    onSnackClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sampleSnacks.take(4)) { snack ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSnackClick(snack.id) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(snack.color)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = snack.name, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Practice1DetailScreenBasic(
    snackId: String,
    onBack: () -> Unit
) {
    val snack = sampleSnacks.find { it.id == snackId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(snack.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(snack.color)
            )
            Text(
                text = snack.name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

// SharedTransitionLayout 적용 버전 (정답 참조용)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun Practice1ListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSnackClick: (String) -> Unit
) {
    with(sharedTransitionScope) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleSnacks.take(4)) { snack ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSnackClick(snack.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "p1-image-${snack.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .background(snack.color)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = snack.name, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Practice1DetailScreen(
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "p1-image-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .background(snack.color)
                )
                Text(
                    text = snack.name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

// ============================================================
// 연습 2: 텍스트에 sharedBounds 적용하기 (난이도: 중)
// ============================================================

/**
 * 연습 2: sharedBounds를 사용한 텍스트 전환
 *
 * 목표: 이미지에 sharedElement가 적용된 상태에서,
 *       텍스트에도 sharedBounds를 적용하여 폰트 크기 변화를 부드럽게 처리하세요.
 *
 * TODO:
 * 1. 리스트의 스낵 이름에 sharedBounds modifier 추가
 * 2. 상세 화면의 스낵 이름에 동일한 key로 sharedBounds 추가
 * 3. 가격 텍스트에도 sharedBounds 적용
 *
 * 힌트:
 * - sharedBounds는 폰트 크기가 다를 때 부드럽게 스케일링
 * - sharedElement는 동일한 콘텐츠일 때만 적합
 * - 텍스트는 fontSize가 달라지므로 bounds가 적합
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Practice2_SharedBoundsText() {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: sharedBounds로 텍스트 전환",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |이미지는 이미 sharedElement가 적용되어 있습니다.
                        |텍스트에 sharedBounds를 추가해보세요:
                        |
                        |1. 스낵 이름에 sharedBounds 적용
                        |2. 가격에도 sharedBounds 적용
                        |3. 리스트와 상세에서 동일한 key 사용
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        SharedTransitionLayout {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "list",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("list") {
                    Practice2ListScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        onSnackClick = { snackId ->
                            navController.navigate("detail/$snackId")
                        }
                    )
                }
                composable("detail/{snackId}") { backStackEntry ->
                    val snackId = backStackEntry.arguments?.getString("snackId") ?: ""
                    Practice2DetailScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        snackId = snackId,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun Practice2ListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSnackClick: (String) -> Unit
) {
    with(sharedTransitionScope) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleSnacks.take(4)) { snack ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSnackClick(snack.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 이미지는 이미 sharedElement 적용됨
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "p2-image-${snack.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .background(snack.color)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            // TODO: 스낵 이름에 sharedBounds 추가
                            // 힌트: .sharedBounds(
                            //     sharedContentState = rememberSharedContentState(key = "p2-name-${snack.id}"),
                            //     animatedVisibilityScope = animatedVisibilityScope
                            // )
                            Text(
                                text = snack.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                // TODO: 여기에 sharedBounds modifier 추가

                                /* 정답:
                                modifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "p2-name-${snack.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                */
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // TODO: 가격에도 sharedBounds 추가
                            Text(
                                text = snack.price,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                // TODO: 여기에 sharedBounds modifier 추가

                                /* 정답:
                                modifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "p2-price-${snack.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                */
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Practice2DetailScreen(
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "p2-image-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .background(snack.color)
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    // TODO: 상세 화면의 스낵 이름에도 동일한 key로 sharedBounds 추가
                    Text(
                        text = snack.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        // TODO: 여기에 sharedBounds modifier 추가

                        /* 정답:
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "p2-name-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        */
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // TODO: 상세 화면의 가격에도 sharedBounds 추가
                    Text(
                        text = snack.price,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        // TODO: 여기에 sharedBounds modifier 추가

                        /* 정답:
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "p2-price-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        */
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = snack.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

// ============================================================
// 연습 3: boundsTransform으로 애니메이션 커스터마이징 (난이도: 상)
// ============================================================

/**
 * 연습 3: 애니메이션 커스터마이징
 *
 * 목표: boundsTransform 파라미터를 사용하여
 *       공유 요소 전환의 애니메이션 특성을 변경해보세요.
 *
 * TODO:
 * 1. spring 애니메이션 적용 (dampingRatio, stiffness 조정)
 * 2. 다양한 값으로 실험해보기
 *
 * 힌트:
 * - Spring.DampingRatioNoBouncy (1.0f) ~ DampingRatioHighBouncy (0.2f)
 * - Spring.StiffnessVeryLow (50f) ~ StiffnessHigh (10000f)
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Practice3_CustomAnimation() {
    // 애니메이션 설정을 위한 상태
    var dampingRatio by remember { mutableFloatStateOf(Spring.DampingRatioMediumBouncy) }
    var stiffness by remember { mutableFloatStateOf(Spring.StiffnessLow) }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: boundsTransform 커스터마이징",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "슬라이더로 애니메이션 특성을 조정해보세요:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Damping Ratio 슬라이더
                Text(
                    text = "Damping Ratio: ${String.format("%.2f", dampingRatio)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Slider(
                    value = dampingRatio,
                    onValueChange = { dampingRatio = it },
                    valueRange = 0.2f..1.0f
                )

                // Stiffness 슬라이더
                Text(
                    text = "Stiffness: ${stiffness.toInt()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Slider(
                    value = stiffness,
                    onValueChange = { stiffness = it },
                    valueRange = 50f..500f
                )
            }
        }

        SharedTransitionLayout {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "list",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("list") {
                    Practice3ListScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        dampingRatio = dampingRatio,
                        stiffness = stiffness,
                        onSnackClick = { snackId ->
                            navController.navigate("detail/$snackId")
                        }
                    )
                }
                composable("detail/{snackId}") { backStackEntry ->
                    val snackId = backStackEntry.arguments?.getString("snackId") ?: ""
                    Practice3DetailScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        dampingRatio = dampingRatio,
                        stiffness = stiffness,
                        snackId = snackId,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun Practice3ListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    dampingRatio: Float,
    stiffness: Float,
    onSnackClick: (String) -> Unit
) {
    with(sharedTransitionScope) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleSnacks.take(4)) { snack ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSnackClick(snack.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // TODO: boundsTransform 파라미터를 추가하여 spring 애니메이션 적용
                        // 힌트: boundsTransform = { _, _ -> spring(dampingRatio, stiffness) }
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "p3-image-${snack.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    // TODO: boundsTransform 추가
                                    boundsTransform = { _, _ ->
                                        spring(
                                            dampingRatio = dampingRatio,
                                            stiffness = stiffness
                                        )
                                    }
                                )
                                .background(snack.color)
                        )

                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = snack.name,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Practice3DetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    dampingRatio: Float,
    stiffness: Float,
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
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "p3-image-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            // 동일한 boundsTransform 적용
                            boundsTransform = { _, _ ->
                                spring(
                                    dampingRatio = dampingRatio,
                                    stiffness = stiffness
                                )
                            }
                        )
                        .background(snack.color)
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = snack.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = snack.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
