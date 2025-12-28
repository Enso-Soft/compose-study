package com.example.image_loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
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
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

/**
 * Solution.kt - Coil을 사용한 이미지 로딩 해결책
 *
 * Coil의 AsyncImage와 SubcomposeAsyncImage를 사용하여
 * 네트워크 이미지 로딩의 모든 문제를 해결합니다.
 */

@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        SolutionExplanationCard()

        HorizontalDivider()

        // 해결책 1: 기본 AsyncImage
        Solution1_BasicAsyncImage()

        HorizontalDivider()

        // 해결책 2: Placeholder와 Error 처리
        Solution2_PlaceholderAndError()

        HorizontalDivider()

        // 해결책 3: 이미지 변환 (원형, 둥근 모서리)
        Solution3_ImageTransformations()

        HorizontalDivider()

        // 해결책 4: SubcomposeAsyncImage 활용
        Solution4_SubcomposeAsyncImage()

        HorizontalDivider()

        // 해결책 5: 캐싱 전략
        Solution5_CachingStrategy()
    }
}

@Composable
private fun SolutionExplanationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Coil로 해결!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
                    |Coil의 AsyncImage는 모든 문제를 해결합니다:
                    |
                    |1. URL에서 직접 이미지 로드
                    |2. 비동기 로딩 (UI 블로킹 없음)
                    |3. 자동 메모리/디스크 캐싱
                    |4. 화면 크기에 맞게 다운샘플링
                    |5. 로딩/에러 상태 처리 내장
                """.trimMargin(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 해결책 1: 기본 AsyncImage 사용
 *
 * 가장 간단한 형태의 이미지 로딩.
 * URL만 전달하면 자동으로 비동기 로딩, 캐싱, 다운샘플링이 적용됩니다.
 */
@Composable
private fun Solution1_BasicAsyncImage() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결책 1: 기본 AsyncImage",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 가장 간단한 AsyncImage 사용법
            AsyncImage(
                model = "https://picsum.photos/seed/basic/400/200",
                contentDescription = "기본 AsyncImage 예시",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 코드 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
                        AsyncImage(
                            model = "https://picsum.photos/400/200",
                            contentDescription = "이미지 설명",
                            contentScale = ContentScale.Crop
                        )

                        // 이것만으로:
                        // - 비동기 네트워크 로딩
                        // - 자동 캐싱 (메모리 + 디스크)
                        // - 화면 크기에 맞게 다운샘플링
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 해결책 2: Placeholder와 Error 처리
 *
 * 로딩 중에는 placeholder를, 실패 시에는 error 이미지를 표시합니다.
 * crossfade 애니메이션으로 자연스러운 전환을 제공합니다.
 */
@Composable
private fun Solution2_PlaceholderAndError() {
    val context = LocalContext.current

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결책 2: Placeholder & Error 처리",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 정상 이미지
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("https://picsum.photos/seed/placeholder/200/150")
                            .crossfade(true)
                            .build(),
                        placeholder = ColorPainter(Color.LightGray),
                        error = ColorPainter(Color.Red.copy(alpha = 0.3f)),
                        contentDescription = "정상 이미지",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text("정상 로드", style = MaterialTheme.typography.bodySmall)
                }

                // 에러 이미지 (잘못된 URL)
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("https://invalid-url-that-will-fail.com/image.jpg")
                            .crossfade(true)
                            .build(),
                        placeholder = ColorPainter(Color.LightGray),
                        error = ColorPainter(Color.Red.copy(alpha = 0.3f)),
                        contentDescription = "에러 이미지",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text("에러 발생", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 코드 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(imageUrl)
                                .crossfade(true)  // 페이드 인 애니메이션
                                .build(),
                            placeholder = ColorPainter(Color.LightGray),
                            error = ColorPainter(Color.Red),
                            contentDescription = null
                        )
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 해결책 3: 이미지 변환 (Compose 권장 방식)
 *
 * Compose에서는 Coil의 Transformation 대신
 * Modifier.clip()을 사용하는 것이 더 효율적입니다.
 */
@Composable
private fun Solution3_ImageTransformations() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결책 3: 이미지 변환",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 원형 크롭
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = "https://picsum.photos/seed/circle/200/200",
                        contentDescription = "원형 이미지",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),  // 원형으로 클리핑
                        contentScale = ContentScale.Crop
                    )
                    Text("CircleShape", style = MaterialTheme.typography.bodySmall)
                }

                // 둥근 모서리
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = "https://picsum.photos/seed/rounded/200/200",
                        contentDescription = "둥근 모서리 이미지",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(16.dp)),  // 둥근 모서리
                        contentScale = ContentScale.Crop
                    )
                    Text("Rounded(16.dp)", style = MaterialTheme.typography.bodySmall)
                }

                // 사각형
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = "https://picsum.photos/seed/square/200/200",
                        contentDescription = "사각형 이미지",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text("기본", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 코드 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
                        // 원형 (권장: Modifier.clip 사용)
                        AsyncImage(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        // 둥근 모서리
                        Modifier.clip(RoundedCornerShape(16.dp))

                        // Coil Transformation 대신 Modifier.clip 권장!
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 해결책 4: SubcomposeAsyncImage 활용
 *
 * 로딩/성공/에러 상태별로 완전히 다른 UI를 표시할 수 있습니다.
 * 단, LazyList에서는 성능상 비권장.
 */
@Composable
private fun Solution4_SubcomposeAsyncImage() {
    val context = LocalContext.current
    var imageKey by remember { mutableIntStateOf(0) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결책 4: SubcomposeAsyncImage",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "상태별 완전 커스텀 UI",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data("https://picsum.photos/seed/$imageKey/400/200")
                    .crossfade(true)
                    .build(),
                contentDescription = "SubcomposeAsyncImage 예시",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
                                Text("이미지 로딩 중...")
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
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { imageKey++ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("새 이미지 로드")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 주의사항
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Text(
                    text = """
                        주의: SubcomposeAsyncImage는 서브컴포지션을 사용하므로
                        LazyColumn/LazyGrid에서는 성능상 AsyncImage + placeholder 권장!
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 해결책 5: 캐싱 전략
 *
 * Coil의 자동 캐싱을 보여줍니다.
 * 같은 이미지는 메모리/디스크 캐시에서 즉시 로드됩니다.
 */
@Composable
private fun Solution5_CachingStrategy() {
    val context = LocalContext.current
    val sampleImages = remember {
        listOf(
            "https://picsum.photos/seed/cache1/200/150",
            "https://picsum.photos/seed/cache2/200/150",
            "https://picsum.photos/seed/cache3/200/150",
            "https://picsum.photos/seed/cache4/200/150",
            "https://picsum.photos/seed/cache5/200/150",
            "https://picsum.photos/seed/cache6/200/150"
        )
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결책 5: 자동 캐싱",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "스크롤 후 다시 보면 캐시에서 즉시 로드",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 이미지 그리드
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(220.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(sampleImages) { url ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(url)
                            .crossfade(true)
                            // 캐시 정책 (기본값 - 모두 ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        placeholder = ColorPainter(Color.LightGray),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 캐시 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
                        Coil 캐시 계층:
                        1. 메모리 캐시 (앱 메모리의 25%)
                           → 디코딩된 Bitmap 저장
                           → 즉시 표시

                        2. 디스크 캐시 (디스크의 2%)
                           → 원본 이미지 파일 저장
                           → 디코딩만 필요

                        3. 네트워크
                           → 캐시 미스 시에만 요청
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
