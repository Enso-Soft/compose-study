package com.example.pager

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 문제 상황: Pager 없이 온보딩 화면 구현
 *
 * AnimatedContent를 사용하면:
 * 1. 스와이프 제스처가 동작하지 않음
 * 2. 드래그 피드백이 없음
 * 3. 모바일 사용자에게 익숙하지 않은 UX
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: Pager 없이 페이지 전환",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "AnimatedContent를 사용하면 버튼으로만 전환 가능합니다.\n" +
                            "화면을 좌우로 스와이프해보세요 - 아무 반응이 없습니다!",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 잘못된 구현: AnimatedContent 사용
        ProblemOnboarding()
    }
}

@Composable
private fun ProblemOnboarding() {
    var currentPage by remember { mutableIntStateOf(0) }
    val pageCount = 3

    val pages = listOf(
        OnboardingPageData(
            title = "환영합니다!",
            description = "앱의 주요 기능을 소개합니다.",
            backgroundColor = Color(0xFF6200EE)
        ),
        OnboardingPageData(
            title = "쉬운 사용법",
            description = "직관적인 인터페이스로 쉽게 사용하세요.",
            backgroundColor = Color(0xFF03DAC5)
        ),
        OnboardingPageData(
            title = "시작하기",
            description = "지금 바로 시작해보세요!",
            backgroundColor = Color(0xFFFF6D00)
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // AnimatedContent로 페이지 전환 (스와이프 불가!)
        AnimatedContent(
            targetState = currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            label = "page"
        ) { page ->
            OnboardingPageContent(pages[page])
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 페이지 인디케이터
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pageCount) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (currentPage == index) 10.dp else 8.dp)
                        .background(
                            color = if (currentPage == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 버튼으로만 전환 가능
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text("이전")
            }

            Text(
                text = "${currentPage + 1} / $pageCount",
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Button(
                onClick = { if (currentPage < pageCount - 1) currentPage++ },
                enabled = currentPage < pageCount - 1
            ) {
                Text("다음")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "스와이프로 전환할 수 없습니다!",
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun OnboardingPageContent(data: OnboardingPageData) {
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

private data class OnboardingPageData(
    val title: String,
    val description: String,
    val backgroundColor: Color
)
