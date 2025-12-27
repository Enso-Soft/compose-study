package com.example.shared_element_transition

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Shared Element Transition - 문제 상황
 *
 * 이 예제는 SharedTransitionLayout 없이 일반 Navigation만 사용할 때
 * 발생하는 **시각적 단절 문제**를 보여줍니다.
 *
 * 문제점:
 * 1. 리스트에서 아이템을 클릭하면 화면이 갑자기 바뀜
 * 2. 색상 박스와 텍스트가 "점프"하듯이 갑자기 나타남
 * 3. 어떤 아이템을 선택했는지 시각적 연결이 없음
 * 4. 사용자가 맥락을 잃어버리기 쉬움
 *
 * 아래 앱을 실행하고 아이템을 클릭해보세요.
 * 화면 전환이 얼마나 부자연스러운지 체험할 수 있습니다.
 */

// 스낵 데이터 모델
data class Snack(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val color: Color
)

// 샘플 데이터
val sampleSnacks = listOf(
    Snack("1", "컵케이크", "달콤한 바닐라 크림이 올라간 컵케이크", "$3.50", Color(0xFFE91E63)),
    Snack("2", "도넛", "초콜릿 글레이즈가 입혀진 도넛", "$2.00", Color(0xFF9C27B0)),
    Snack("3", "에클레어", "프렌치 스타일 에클레어", "$4.00", Color(0xFF673AB7)),
    Snack("4", "프로요", "신선한 과일 프로즌 요거트", "$5.00", Color(0xFF3F51B5)),
    Snack("5", "진저브레드", "계피향 진저브레드 쿠키", "$2.50", Color(0xFF2196F3)),
    Snack("6", "허니콤", "달콤한 벌집 모양 과자", "$3.00", Color(0xFF00BCD4)),
    Snack("7", "아이스크림 샌드위치", "쿠키 사이에 아이스크림", "$4.50", Color(0xFF009688)),
    Snack("8", "젤리빈", "다양한 맛의 젤리빈", "$1.50", Color(0xFF4CAF50)),
)

@Composable
fun ProblemScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // 문제 설명 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황: 시각적 단절",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |아래 리스트에서 아이템을 클릭해보세요.
                        |
                        |발생하는 문제:
                        |1. 화면이 갑자기 바뀜 (fade만 있고 연결이 없음)
                        |2. 색상 박스가 사라졌다가 다시 나타남
                        |3. 어떤 아이템을 선택했는지 맥락이 끊김
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 일반 Navigation (SharedTransitionLayout 없음!)
        ProblemSnackGallery()
    }
}

@Composable
private fun ProblemSnackGallery() {
    val navController = rememberNavController()

    // 문제: SharedTransitionLayout으로 감싸지 않음!
    // 결과: 화면 전환 시 요소들이 연결되지 않고 갑자기 바뀜
    NavHost(
        navController = navController,
        startDestination = "list",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("list") {
            ProblemListScreen(
                onSnackClick = { snackId ->
                    navController.navigate("detail/$snackId")
                }
            )
        }
        composable("detail/{snackId}") { backStackEntry ->
            val snackId = backStackEntry.arguments?.getString("snackId") ?: ""
            ProblemDetailScreen(
                snackId = snackId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun ProblemListScreen(
    onSnackClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sampleSnacks) { snack ->
            // 문제: 일반 카드, sharedElement modifier 없음
            ProblemSnackCard(
                snack = snack,
                onClick = { onSnackClick(snack.id) }
            )
        }
    }
}

@Composable
private fun ProblemSnackCard(
    snack: Snack,
    onClick: () -> Unit
) {
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
            // 문제: 일반 Box, sharedElement 없음
            // 상세 화면으로 전환 시 이 박스가 "사라졌다가 나타남"
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(snack.color)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // 문제: 일반 Text, sharedBounds 없음
                Text(
                    text = snack.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = snack.price,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProblemDetailScreen(
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
            // 문제: 일반 Box, sharedElement 없음
            // 리스트의 작은 박스와 연결되지 않고 갑자기 나타남
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(snack.color)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // 문제: 일반 Text, sharedBounds 없음
                Text(
                    text = snack.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = snack.price,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
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
