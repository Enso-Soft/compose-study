package com.example.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 해결책: Carousel로 자연스러운 브라우징 경험 구현
 *
 * Carousel 장점:
 * 1. 아이템 크기가 스크롤 위치에 따라 자동 조절
 * 2. 가운데 아이템이 자연스럽게 강조됨
 * 3. 양쪽에 부분적으로 보이는 아이템이 "더 있다"는 힌트 제공
 * 4. Material 3 디자인 가이드라인 준수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    var carouselType by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                    text = "해결: Carousel 사용",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Carousel을 사용하면 아이템 크기가 자동으로 조절됩니다.\n" +
                            "가운데 아이템이 강조되고, Material 3 디자인을 준수합니다.",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Carousel 유형 선택
        Text(
            text = "Carousel 유형 선택",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = carouselType == 0,
                onClick = { carouselType = 0 },
                label = { Text("MultiBrowse") }
            )
            FilterChip(
                selected = carouselType == 1,
                onClick = { carouselType = 1 },
                label = { Text("Uncontained") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 선택된 Carousel 표시
        when (carouselType) {
            0 -> MultiBrowseCarouselDemo()
            1 -> UncontainedCarouselDemo()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 비교 정보
        CarouselComparisonInfo()

        Spacer(modifier = Modifier.height(16.dp))

        // Carousel vs Pager 비교
        CarouselVsPagerComparison()
    }
}

/**
 * HorizontalMultiBrowseCarousel 데모
 *
 * 특징:
 * - 여러 크기의 아이템이 섞여 보임 (large, medium, small)
 * - preferredItemWidth로 "이 정도 크기였으면 좋겠어" 요청
 * - 공간에 맞게 아이템 크기가 자동 조절됨
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MultiBrowseCarouselDemo() {
    val items = remember { sampleGalleryItems() }

    Column {
        Text(
            text = "HorizontalMultiBrowseCarousel",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = "여러 크기 아이템이 섞여 보입니다 (서점 진열대처럼)",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { items.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(221.dp),
            preferredItemWidth = 186.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { index ->
            val item = items[index]
            CarouselCard(item)
        }
    }
}

/**
 * HorizontalUncontainedCarousel 데모
 *
 * 특징:
 * - 모든 아이템이 고정 크기
 * - itemWidth로 "정확히 이 크기로" 지정
 * - 화면 끝에서 잘리지 않고 자연스럽게 넘어감
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UncontainedCarouselDemo() {
    val items = remember { sampleGalleryItems() }

    Column {
        Text(
            text = "HorizontalUncontainedCarousel",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = "고정 크기 아이템 (영화관 포스터 벽처럼)",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalUncontainedCarousel(
            state = rememberCarouselState { items.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(221.dp),
            itemWidth = 186.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { index ->
            val item = items[index]
            CarouselCard(item)
        }
    }
}

/**
 * Carousel 아이템 카드
 * CarouselItemScope 외부에서 사용할 수 있는 버전 (maskClip 없이)
 */
@Composable
private fun CarouselCard(item: GalleryItem) {
    Card(
        modifier = Modifier.height(205.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.color
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "크기 자동 조절!",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * MultiBrowse vs Uncontained 비교 정보
 */
@Composable
private fun CarouselComparisonInfo() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "MultiBrowse vs Uncontained",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))

            ComparisonRow(
                label = "아이템 크기",
                value1 = "다양 (자동 조절)",
                value2 = "고정"
            )
            ComparisonRow(
                label = "preferredItemWidth",
                value1 = "O (제안)",
                value2 = "-"
            )
            ComparisonRow(
                label = "itemWidth",
                value1 = "-",
                value2 = "O (고정)"
            )
            ComparisonRow(
                label = "적합한 용도",
                value1 = "빠른 브라우징",
                value2 = "균일한 표시"
            )
        }
    }
}

/**
 * Carousel vs HorizontalPager 비교 정보
 */
@Composable
private fun CarouselVsPagerComparison() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Carousel vs HorizontalPager",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))

            ComparisonRow(
                label = "보이는 아이템",
                value1 = "여러 개",
                value2 = "1개 (전체)"
            )
            ComparisonRow(
                label = "크기 변화",
                value1 = "자동 조절",
                value2 = "고정"
            )
            ComparisonRow(
                label = "용도",
                value1 = "썸네일 브라우징",
                value2 = "전체 화면 콘텐츠"
            )
            ComparisonRow(
                label = "예시",
                value1 = "앨범 아트, 추천 상품",
                value2 = "온보딩, 이미지 뷰어"
            )
        }
    }
}

@Composable
private fun ComparisonRow(
    label: String,
    value1: String,
    value2: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value1,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value2,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 샘플 갤러리 아이템 생성
 */
private fun sampleGalleryItems(): List<GalleryItem> = listOf(
    GalleryItem(0, "여행", Color(0xFF6200EE)),
    GalleryItem(1, "음식", Color(0xFF03DAC5)),
    GalleryItem(2, "자연", Color(0xFFFF6D00)),
    GalleryItem(3, "도시", Color(0xFF3700B3)),
    GalleryItem(4, "인물", Color(0xFF018786)),
)
