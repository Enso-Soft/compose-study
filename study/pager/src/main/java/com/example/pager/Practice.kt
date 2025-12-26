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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * 연습 문제 1: 기본 이미지 갤러리 (기초)
 *
 * 목표: HorizontalPager 기본 사용법 익히기
 *
 * 요구사항:
 * 1. 5개의 컬러 박스를 가로로 스와이프
 * 2. 현재 페이지 번호 표시 (예: "3 / 5")
 * 3. 각 페이지에 다른 배경색 적용
 */
@Composable
fun Practice1_ImageGallery() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 1: 기본 이미지 갤러리",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "힌트:\n" +
                            "• rememberPagerState(pageCount = { 5 })\n" +
                            "• HorizontalPager(state = pagerState) { page -> ... }\n" +
                            "• pagerState.currentPage로 현재 페이지 접근",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 아래 코드를 완성하세요!

        val colors = listOf(
            Color(0xFFE57373),
            Color(0xFF81C784),
            Color(0xFF64B5F6),
            Color(0xFFFFD54F),
            Color(0xFFBA68C8)
        )

        // TODO: rememberPagerState를 사용하여 pagerState 생성
        // val pagerState = ...

        // TODO: HorizontalPager 구현
        // HorizontalPager(...) { page ->
        //     Box(배경색: colors[page]) { Text("Page ${page + 1}") }
        // }

        // TODO: 현재 페이지 표시 (예: "3 / 5")
        // Text("${pagerState.currentPage + 1} / ${colors.size}")

        // ========== 정답 (주석 해제하여 확인) ==========
        /*
        val pagerState = rememberPagerState(pageCount = { colors.size })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colors[page],
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Page ${page + 1}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${pagerState.currentPage + 1} / ${colors.size}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        */

        // 임시 플레이스홀더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "여기에 HorizontalPager를 구현하세요",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 연습 문제 2: 탭 + Pager 연동 (중급)
 *
 * 목표: TabRow와 HorizontalPager 양방향 연동
 *
 * 요구사항:
 * 1. 3개 탭 (홈, 검색, 설정)과 3개 페이지
 * 2. 탭 클릭 시 해당 페이지로 애니메이션 전환
 * 3. 페이지 스와이프 시 탭 선택 상태 변경
 */
@Composable
fun Practice2_TabPager() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 2: 탭 + Pager 연동",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "힌트:\n" +
                            "• TabRow(selectedTabIndex = pagerState.currentPage)\n" +
                            "• Tab(onClick = { scope.launch { pagerState.animateScrollToPage(index) } })\n" +
                            "• rememberCoroutineScope() 필요",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 아래 코드를 완성하세요!

        val tabs = listOf("홈", "검색", "설정")

        // TODO: pagerState와 coroutineScope 생성

        // TODO: TabRow 구현 (탭 클릭 시 animateScrollToPage)

        // TODO: HorizontalPager 구현 (각 페이지에 탭 이름 표시)

        // ========== 정답 (주석 해제하여 확인) ==========
        /*
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        val scope = rememberCoroutineScope()

        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${tabs[page]} 화면",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        */

        // 임시 플레이스홀더
        TabRow(selectedTabIndex = 0) {
            tabs.forEach { title ->
                Tab(
                    selected = false,
                    onClick = { },
                    text = { Text(title) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "TabRow와 HorizontalPager를 연동하세요",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 연습 문제 3: 커스텀 페이지 인디케이터 (고급)
 *
 * 목표: 드래그 진행률을 반영하는 애니메이션 인디케이터
 *
 * 요구사항:
 * 1. 5개 페이지의 점 인디케이터
 * 2. 스와이프 중 점이 부드럽게 크기 변화
 * 3. currentPageOffsetFraction 활용
 */
@Composable
fun Practice3_CustomIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 3: 커스텀 페이지 인디케이터",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "힌트:\n" +
                            "• pagerState.currentPageOffsetFraction: -1.0 ~ 1.0\n" +
                            "• 현재 페이지와 다음/이전 페이지 크기 보간\n" +
                            "• Modifier.scale() 또는 graphicsLayer 사용",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 아래 코드를 완성하세요!

        val pageCount = 5
        val colors = listOf(
            Color(0xFFE57373),
            Color(0xFF81C784),
            Color(0xFF64B5F6),
            Color(0xFFFFD54F),
            Color(0xFFBA68C8)
        )

        // TODO: pagerState 생성 및 HorizontalPager 구현

        // TODO: 커스텀 인디케이터 구현
        // - 현재 페이지: 크게 (scale 1.5)
        // - 드래그 중: 현재 페이지는 작아지고, 다음 페이지는 커짐
        // - currentPageOffsetFraction을 사용하여 보간

        // ========== 정답 (주석 해제하여 확인) ==========
        /*
        val pagerState = rememberPagerState(pageCount = { pageCount })

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colors[page],
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Page ${page + 1}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 커스텀 인디케이터
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { index ->
                val offsetFraction = pagerState.currentPageOffsetFraction
                val currentPage = pagerState.currentPage

                // 각 인디케이터의 스케일 계산
                val scale = when (index) {
                    currentPage -> {
                        // 현재 페이지: 드래그하면 작아짐
                        1.5f - offsetFraction.absoluteValue * 0.5f
                    }
                    currentPage + 1 -> {
                        // 다음 페이지: 오른쪽으로 드래그하면 커짐
                        if (offsetFraction > 0) 1f + offsetFraction * 0.5f else 1f
                    }
                    currentPage - 1 -> {
                        // 이전 페이지: 왼쪽으로 드래그하면 커짐
                        if (offsetFraction < 0) 1f + (-offsetFraction) * 0.5f else 1f
                    }
                    else -> 1f
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(10.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(CircleShape)
                        .background(
                            if (index == currentPage)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 정보 표시
        Text(
            text = "offsetFraction: ${
                String.format("%.2f", pagerState.currentPageOffsetFraction)
            }",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )
        */

        // 임시 플레이스홀더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "커스텀 인디케이터를 구현하세요",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outline)
                )
            }
        }
    }
}
