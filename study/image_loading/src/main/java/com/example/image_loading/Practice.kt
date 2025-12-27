package com.example.image_loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade

/**
 * Practice.kt - Coil 이미지 로딩 연습 문제
 *
 * 3개의 연습 문제를 통해 Coil 사용법을 직접 실습합니다.
 * 각 연습에는 TODO 주석과 힌트가 포함되어 있습니다.
 */

// ============================================================================
// 연습 1: 기본 AsyncImage 구현 (초급)
// ============================================================================

/**
 * 연습 1: 기본 AsyncImage 구현
 *
 * 목표: URL에서 이미지를 로드하는 기본 AsyncImage 구현
 *
 * 요구사항:
 * 1. 주어진 URL로 이미지 로드
 * 2. placeholder로 회색 배경 표시
 * 3. crossfade 애니메이션 적용
 * 4. contentScale을 Crop으로 설정
 *
 * 힌트: ImageRequest.Builder(context).data(url).crossfade(true).build()
 */
@Composable
fun Practice1_BasicAsyncImage() {
    val context = LocalContext.current
    val imageUrl = "https://picsum.photos/seed/practice1/400/300"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 AsyncImage",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        URL에서 이미지를 로드하세요:
                        1. AsyncImage 사용
                        2. placeholder 설정 (회색)
                        3. crossfade 애니메이션 적용
                        4. ContentScale.Crop 사용
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        HintCard(
            hint = """
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    placeholder = ColorPainter(Color.LightGray),
                    ...
                )
            """.trimIndent()
        )

        // TODO: 여기에 AsyncImage를 구현하세요
        // ----------------------------------------------------------------
        // 아래 Box를 AsyncImage로 교체하세요

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("여기에 AsyncImage 구현", color = Color.Gray)
        }

        // 정답 (주석 해제하여 확인)
        /*
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = ColorPainter(Color.LightGray),
            error = ColorPainter(Color.Red.copy(alpha = 0.3f)),
            contentDescription = "연습 1 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        */
        // ----------------------------------------------------------------
    }
}

// ============================================================================
// 연습 2: 이미지 갤러리 구현 (중급)
// ============================================================================

/**
 * 연습 2: 이미지 갤러리 구현
 *
 * 목표: LazyVerticalGrid로 이미지 갤러리 구현
 *
 * 요구사항:
 * 1. 2열 그리드로 이미지 표시
 * 2. 각 이미지에 둥근 모서리 적용 (8.dp)
 * 3. placeholder와 error 처리
 * 4. 정사각형 비율 유지 (aspectRatio(1f))
 *
 * 힌트: LazyVerticalGrid(columns = GridCells.Fixed(2))
 *       Modifier.clip(RoundedCornerShape(8.dp))
 */
@Composable
fun Practice2_ImageGallery() {
    val context = LocalContext.current
    val imageUrls = remember {
        (1..12).map { "https://picsum.photos/seed/gallery$it/300/300" }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 이미지 갤러리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        LazyVerticalGrid로 갤러리 구현:
                        1. 2열 그리드 레이아웃
                        2. 둥근 모서리 (8.dp)
                        3. placeholder/error 처리
                        4. 정사각형 비율
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        HintCard(
            hint = """
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(imageUrls) { url ->
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp)),
                            ...
                        )
                    }
                }
            """.trimIndent()
        )

        // TODO: 여기에 LazyVerticalGrid 이미지 갤러리를 구현하세요
        // ----------------------------------------------------------------
        // 아래 Box를 LazyVerticalGrid로 교체하세요

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text("여기에 LazyVerticalGrid 구현", color = Color.Gray)
        }

        // 정답 (주석 해제하여 확인)
        /*
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(imageUrls) { url ->
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    placeholder = ColorPainter(Color.LightGray),
                    error = ColorPainter(Color.Red.copy(alpha = 0.3f)),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        */
        // ----------------------------------------------------------------
    }
}

// ============================================================================
// 연습 3: 상태별 UI 처리 (고급)
// ============================================================================

/**
 * 연습 3: 상태별 UI 처리
 *
 * 목표: SubcomposeAsyncImage로 로딩/성공/에러 상태별 커스텀 UI 구현
 *
 * 요구사항:
 * 1. Loading: CircularProgressIndicator + "로딩 중..." 텍스트
 * 2. Success: 이미지 표시
 * 3. Error: 에러 아이콘 + "로드 실패" 텍스트 + 재시도 버튼
 * 4. 재시도 버튼 클릭 시 새 이미지 로드
 *
 * 힌트: painter.state.collectAsState()로 상태 관찰
 *       when (state) { is AsyncImagePainter.State.Loading -> ... }
 */
@Composable
fun Practice3_StateHandling() {
    val context = LocalContext.current
    var imageKey by remember { mutableIntStateOf(0) }
    // 가끔 에러를 발생시키기 위한 URL
    val imageUrl = if (imageKey % 3 == 2) {
        "https://invalid-url-error.com/image.jpg"  // 에러 발생용
    } else {
        "https://picsum.photos/seed/state$imageKey/400/300"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 상태별 UI 처리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        SubcomposeAsyncImage로 상태별 UI:
                        1. Loading: 프로그레스 + 텍스트
                        2. Success: 이미지 표시
                        3. Error: 에러 UI + 재시도 버튼

                        * 3번째 로드마다 에러 발생
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        HintCard(
            hint = """
                SubcomposeAsyncImage(model = ...) {
                    val state by painter.state.collectAsState()
                    when (state) {
                        is AsyncImagePainter.State.Loading -> { ... }
                        is AsyncImagePainter.State.Success -> {
                            SubcomposeAsyncImageContent()
                        }
                        is AsyncImagePainter.State.Error -> { ... }
                        else -> {}
                    }
                }
            """.trimIndent()
        )

        // TODO: 여기에 SubcomposeAsyncImage를 구현하세요
        // ----------------------------------------------------------------

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("여기에 SubcomposeAsyncImage 구현", color = Color.Gray)
        }

        // 정답 (주석 해제하여 확인)
        /*
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "상태별 이미지",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            val state by painter.state.collectAsState()

            when (state) {
                is AsyncImagePainter.State.Empty,
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("로딩 중...")
                        }
                    }
                }
                is AsyncImagePainter.State.Success -> {
                    SubcomposeAsyncImageContent()
                }
                is AsyncImagePainter.State.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Text("로드 실패", color = Color.Red)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { imageKey++ }) {
                                Icon(Icons.Default.Refresh, null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("재시도")
                            }
                        }
                    }
                }
            }
        }
        */
        // ----------------------------------------------------------------

        // 이미지 변경 버튼
        Button(
            onClick = { imageKey++ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("새 이미지 로드 (현재: $imageKey)")
        }

        // 상태 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Text(
                text = if (imageKey % 3 == 2) {
                    "다음 로드 시 에러 발생 예정!"
                } else {
                    "정상 이미지 로드 중..."
                },
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// ============================================================================
// 공통 컴포넌트
// ============================================================================

@Composable
private fun HintCard(hint: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        }
    }
}
