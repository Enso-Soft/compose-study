package com.example.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
 * 연습 1: 기본 캐러셀 만들기 (쉬움)
 *
 * 목표: HorizontalMultiBrowseCarousel 기본 사용법 익히기
 *
 * 요구사항:
 * - 5개의 컬러 카드를 표시하는 캐러셀 만들기
 * - preferredItemWidth: 200.dp
 * - itemSpacing: 12.dp
 * - 각 카드에 인덱스 번호 표시 ("카드 1", "카드 2", ...)
 *
 * 힌트:
 * 1. rememberCarouselState { 5 }로 상태 생성
 * 2. HorizontalMultiBrowseCarousel 사용
 * 3. content 람다에서 index로 카드 생성
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicCarousel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 연습 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 캐러셀 (쉬움)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "5개의 컬러 카드를 표시하는 HorizontalMultiBrowseCarousel을 만드세요.",
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "- preferredItemWidth: 200.dp\n" +
                            "- itemSpacing: 12.dp\n" +
                            "- 각 카드에 '카드 N' 텍스트 표시",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ========================================
        // TODO: 여기에 코드를 작성하세요!
        // ========================================

        // TODO 1: 5개 아이템의 CarouselState 생성
        // 힌트: val state = rememberCarouselState { 5 }

        // TODO 2: 5가지 색상 리스트 정의
        // 힌트: val colors = listOf(Color(0xFF...), ...)

        // TODO 3: HorizontalMultiBrowseCarousel 구현
        // 힌트:
        // HorizontalMultiBrowseCarousel(
        //     state = state,
        //     modifier = Modifier.fillMaxWidth().height(221.dp),
        //     preferredItemWidth = 200.dp,
        //     itemSpacing = 12.dp,
        //     contentPadding = PaddingValues(horizontal = 16.dp)
        // ) { index ->
        //     // TODO 4: 카드 생성
        // }

        // 플레이스홀더 (구현 후 삭제)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(221.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "여기에 HorizontalMultiBrowseCarousel을\n구현하세요!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ========================================
    }
}

/**
 * 연습 2: 영화 포스터 캐러셀 (중간)
 *
 * 목표: 실제 앱에서 사용할 수 있는 영화 포스터 캐러셀 구현
 *
 * 요구사항:
 * - Movie 데이터 클래스 사용 (title, year, color)
 * - HorizontalUncontainedCarousel 사용
 * - 카드 하단에 영화 제목과 연도 표시
 * - 카드 클릭 시 Snackbar로 영화 제목 표시
 *
 * 힌트:
 * 1. data class Movie(val title: String, val year: Int, val color: Color)
 * 2. itemWidth = 150.dp 권장
 * 3. Card에 onClick 파라미터 사용
 * 4. Scaffold + SnackbarHost 구성
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_MovieCarousel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 연습 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 영화 포스터 캐러셀 (중간)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "HorizontalUncontainedCarousel로 영화 포스터를 표시하세요.\n" +
                            "카드 클릭 시 Snackbar로 영화 제목을 표시합니다.",
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 샘플 영화 데이터 (사용하세요!)
        // val movies = remember {
        //     listOf(
        //         Movie("인셉션", 2010, Color(0xFF1A237E)),
        //         Movie("인터스텔라", 2014, Color(0xFF0D47A1)),
        //         Movie("테넷", 2020, Color(0xFF1565C0)),
        //         Movie("오펜하이머", 2023, Color(0xFF42A5F5)),
        //         Movie("덩케르크", 2017, Color(0xFF64B5F6)),
        //     )
        // }

        // ========================================
        // TODO: 여기에 코드를 작성하세요!
        // ========================================

        // TODO 1: Movie 데이터 클래스 정의 (또는 위 주석의 데이터 사용)
        // data class Movie(val title: String, val year: Int, val color: Color)

        // TODO 2: SnackbarHostState 생성
        // val snackbarHostState = remember { SnackbarHostState() }

        // TODO 3: rememberCoroutineScope 생성 (Snackbar 표시용)
        // val scope = rememberCoroutineScope()

        // TODO 4: CarouselState 생성
        // val state = rememberCarouselState { movies.size }

        // TODO 5: Scaffold + SnackbarHost 구성

        // TODO 6: HorizontalUncontainedCarousel 구현
        // - itemWidth = 150.dp
        // - 각 아이템은 클릭 가능한 MovieCard

        // TODO 7: MovieCard - 포스터 색상, 제목, 연도 표시
        // Card(onClick = { scope.launch { snackbarHostState.showSnackbar(...) } })

        // 플레이스홀더 (구현 후 삭제)
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
                text = "영화 포스터 캐러셀을 구현하세요!\n\n" +
                        "- HorizontalUncontainedCarousel 사용\n" +
                        "- 클릭 시 Snackbar 표시",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }

        // ========================================
    }
}

/**
 * 연습 3: 인디케이터가 있는 캐러셀 (어려움)
 *
 * 목표: CarouselState를 활용한 커스텀 인디케이터 구현
 *
 * 요구사항:
 * - HorizontalMultiBrowseCarousel + 하단 인디케이터
 * - 현재 위치에 해당하는 인디케이터 강조 (크고 진한 색)
 * - 인디케이터 클릭 시 해당 위치로 스크롤
 *
 * 힌트:
 * 1. CarouselState를 사용하여 현재 위치 파악
 * 2. rememberCoroutineScope()로 스크롤 제어
 * 3. 인디케이터는 Row + Box로 구현
 * 4. Box에 clickable 수정자 추가
 *
 * 주의: CarouselState의 프로퍼티를 확인하세요!
 * (공식 문서 참조: developer.android.com/develop/ui/compose/components/carousel)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_CarouselWithIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 연습 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 인디케이터 캐러셀 (어려움)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "캐러셀 하단에 현재 위치를 표시하는 인디케이터를 추가하세요.\n" +
                            "인디케이터 클릭 시 해당 위치로 스크롤합니다.",
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ========================================
        // TODO: 여기에 코드를 작성하세요!
        // ========================================

        // TODO 1: 아이템 데이터 및 상태 정의
        // val items = listOf(...) // 5개 이상의 아이템
        // val state = rememberCarouselState { items.size }
        // val scope = rememberCoroutineScope()

        // TODO 2: 현재 위치 추적
        // CarouselState에서 현재 위치를 어떻게 가져올 수 있을까요?
        // 힌트: 공식 문서를 참조하세요

        // TODO 3: Column으로 Carousel + Indicator 배치
        // Column {
        //     HorizontalMultiBrowseCarousel(...) { ... }
        //     Spacer(...)
        //     Row(인디케이터들) { ... }
        // }

        // TODO 4: 인디케이터 Row 구현
        // Row(
        //     modifier = Modifier.fillMaxWidth(),
        //     horizontalArrangement = Arrangement.Center
        // ) {
        //     items.forEachIndexed { index, _ ->
        //         val isSelected = (현재 위치 == index)
        //         Box(
        //             modifier = Modifier
        //                 .size(if (isSelected) 12.dp else 8.dp)
        //                 .background(
        //                     color = if (isSelected) 진한색 else 연한색,
        //                     shape = CircleShape
        //                 )
        //                 .clickable {
        //                     scope.launch {
        //                         // TODO: 해당 위치로 스크롤
        //                     }
        //                 }
        //         )
        //     }
        // }

        // 플레이스홀더 (구현 후 삭제)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "캐러셀 + 인디케이터를 구현하세요!",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "1. HorizontalMultiBrowseCarousel 구현\n" +
                            "2. 현재 위치 표시 인디케이터 추가\n" +
                            "3. 인디케이터 클릭 시 스크롤",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 인디케이터 예시
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == 0) 12.dp else 8.dp)
                                .background(
                                    color = if (index == 0)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outline,
                                    shape = CircleShape
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "(인디케이터 예시)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }

        // ========================================
    }
}
