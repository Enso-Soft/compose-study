package com.example.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * 해결책: HorizontalPager 사용
 *
 * HorizontalPager를 사용하면:
 * 1. 스와이프 제스처 자동 지원
 * 2. 페이지 스냅 동작
 * 3. Lazy 페이지 생성으로 성능 최적화
 * 4. 풍부한 상태 정보 제공
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: HorizontalPager 사용",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "이제 좌우로 스와이프해보세요!\n" +
                            "자연스러운 페이지 전환을 경험할 수 있습니다.",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 올바른 구현: HorizontalPager 사용
        SolutionOnboarding()
    }
}

@Composable
private fun SolutionOnboarding() {
    val pages = listOf(
        SolutionPageData(
            title = "환영합니다!",
            description = "앱의 주요 기능을 소개합니다.\n좌우로 스와이프해보세요!",
            backgroundColor = Color(0xFF6200EE)
        ),
        SolutionPageData(
            title = "쉬운 사용법",
            description = "직관적인 인터페이스로 쉽게 사용하세요.",
            backgroundColor = Color(0xFF03DAC5)
        ),
        SolutionPageData(
            title = "시작하기",
            description = "지금 바로 시작해보세요!",
            backgroundColor = Color(0xFFFF6D00)
        )
    )

    // PagerState 생성
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HorizontalPager - 스와이프로 페이지 전환!
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            pageSpacing = 16.dp
        ) { page ->
            SolutionPageContent(pages[page])
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 페이지 인디케이터 (currentPage 연동)
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 정보 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "PagerState 정보",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "currentPage: ${pagerState.currentPage}",
                    fontSize = 12.sp
                )
                Text(
                    text = "currentPageOffsetFraction: ${
                        String.format("%.2f", pagerState.currentPageOffsetFraction)
                    }",
                    fontSize = 12.sp
                )
                Text(
                    text = "settledPage: ${pagerState.settledPage}",
                    fontSize = 12.sp
                )
                Text(
                    text = "targetPage: ${pagerState.targetPage}",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 프로그래밍적 페이지 전환 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            ) {
                Text("처음으로")
            }

            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pages.size - 1)
                    }
                }
            ) {
                Text("건너뛰기")
            }
        }
    }
}

@Composable
private fun SolutionPageContent(data: SolutionPageData) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = data.backgroundColor,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = data.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = data.description,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class SolutionPageData(
    val title: String,
    val description: String,
    val backgroundColor: Color
)
