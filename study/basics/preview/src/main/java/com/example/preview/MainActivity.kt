package com.example.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.preview.ui.theme.PreviewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PreviewTheme {
                PreviewStudyApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewStudyApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedPractice by remember { mutableIntStateOf(-1) }

    val tabs = listOf("Problem", "Solution", "Practice")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedPractice >= 0) "연습 ${selectedPractice + 1}"
                        else "@Preview 학습"
                    )
                },
                navigationIcon = {
                    if (selectedPractice >= 0) {
                        IconButton(onClick = { selectedPractice = -1 }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "뒤로 가기"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Practice가 선택되지 않은 경우에만 탭 표시
            if (selectedPractice < 0) {
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }

            // 컨텐츠
            when {
                selectedPractice >= 0 -> {
                    // 개별 연습 문제
                    when (selectedPractice) {
                        0 -> Practice1Screen()
                        1 -> Practice2Screen()
                        2 -> Practice3Screen()
                    }
                }
                selectedTab == 0 -> ProblemScreen()
                selectedTab == 1 -> SolutionScreen()
                selectedTab == 2 -> PracticeNavigator(
                    onPracticeSelected = { selectedPractice = it }
                )
            }
        }
    }
}

@Composable
fun PracticeNavigator(onPracticeSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 문제 선택",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val practices = listOf(
            "연습 1: 기본 Preview 작성" to "ProductCard의 @Preview를 작성하세요",
            "연습 2: PreviewParameter 사용" to "OrderStatusProvider를 구현하세요",
            "연습 3: Multipreview 만들기" to "Phone/Tablet × Light/Dark 조합"
        )

        practices.forEachIndexed { index, (title, description) ->
            Card(
                onClick = { onPracticeSelected(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
