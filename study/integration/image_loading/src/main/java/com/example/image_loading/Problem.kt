package com.example.image_loading

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

/**
 * Problem.kt - 이미지 로딩 라이브러리 없이 발생하는 문제들
 *
 * 이 파일은 Coil 같은 이미지 로딩 라이브러리 없이
 * 네트워크 이미지를 로드하려 할 때 발생하는 문제들을 보여줍니다.
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        ProblemExplanationCard()

        HorizontalDivider()

        // 문제 1: 기본 Image 컴포저블의 한계
        Problem1_NoDirectUrlSupport()

        HorizontalDivider()

        // 문제 2: 수동 비트맵 로딩 시도 (위험한 코드 - 교육 목적)
        Problem2_ManualBitmapLoading()

        HorizontalDivider()

        // 문제 3: 캐싱 없는 이미지 로딩
        Problem3_NoCaching()
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "이미지 로딩 라이브러리 없이 발생하는 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
                    |네트워크 이미지를 직접 로드하면:
                    |
                    |1. Image 컴포저블은 URL을 지원하지 않음
                    |2. 수동 로딩 시 메인 스레드 블로킹 (ANR)
                    |3. 캐싱 없이 매번 네트워크 요청
                    |4. 메모리 관리 없이 OOM 위험
                    |5. 로딩/에러 상태 처리 필요
                """.trimMargin(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 문제 1: 기본 Image 컴포저블은 URL을 직접 지원하지 않음
 *
 * Compose의 Image는 Painter(painterResource, BitmapPainter 등)만 지원하며,
 * URL에서 이미지를 직접 로드하는 기능이 없습니다.
 */
@Composable
private fun Problem1_NoDirectUrlSupport() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 1: Image 컴포저블의 한계",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // URL을 직접 사용할 수 없음을 보여주는 예시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Image(painter = ???)",
                        color = Color.Gray
                    )
                    Text(
                        text = "URL을 Painter로 변환 불가",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
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
                        // 이렇게 하고 싶지만...
                        Image(
                            painter = "https://example.com/image.jpg", // 컴파일 에러!
                            contentDescription = null
                        )

                        // painter는 Painter 타입만 받음:
                        // - painterResource(R.drawable.xxx)
                        // - BitmapPainter(bitmap)
                        // - VectorPainter(vector)
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
 * 문제 2: 수동으로 비트맵을 로드하려는 시도
 *
 * 직접 URL에서 비트맵을 로드하면 여러 문제가 발생합니다:
 * - 메인 스레드에서 네트워크 호출 시 ANR/NetworkOnMainThreadException
 * - 비동기로 처리해도 상태 관리가 복잡
 * - 메모리 관리 없음 (OOM 위험)
 */
@Preview
@Composable
private fun Problem2_ManualBitmapLoading() {
    val imageUrl = "https://picsum.photos/400/300"

    // 수동 로딩 시도 (위험한 코드 - 실제로는 이렇게 하지 마세요!)
    var loadingState by remember { mutableStateOf("대기 중") }
    var isLoading by remember { mutableStateOf(false) }
    var loadedBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 2: 수동 비트맵 로딩의 복잡성",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 수동 로딩 상태 표시
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Text("직접 로딩 중... (복잡한 상태 관리 필요)")
                        }
                    }
                    errorMessage != null -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = errorMessage!!,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    loadedBitmap != null -> {
                        Image(
                            bitmap = loadedBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        Text("이미지가 로드되지 않음", color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 수동 로딩 버튼 (교육 목적)
            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    loadingState = "로딩 시작"
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text("수동으로 이미지 로드 시도")
            }

            // 비동기 로딩 처리 (복잡함을 보여주기 위함)
            LaunchedEffect(isLoading) {
                if (isLoading) {
                    try {
                        loadingState = "네트워크 요청 중..."
                        // 실제로는 이렇게 직접 로딩하면 안 됩니다!
                        val bitmap = withContext(Dispatchers.IO) {
                            // 문제점들:
                            // 1. 캐싱 없음 - 매번 다운로드
                            // 2. 메모리 관리 없음 - 큰 이미지 OOM
                            // 3. 취소 처리 복잡
                            // 4. 에러 처리 직접 해야 함
                            URL(imageUrl).openStream().use { stream ->
                                BitmapFactory.decodeStream(stream)
                            }
                        }
                        loadedBitmap = bitmap
                        loadingState = "완료"
                    } catch (e: Exception) {
                        errorMessage = "로드 실패: ${e.message}"
                        loadingState = "실패"
                    } finally {
                        isLoading = false
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "상태: $loadingState",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            // 문제점 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = """
                        수동 로딩의 문제점:
                        - 복잡한 상태 관리 (loading, error, success)
                        - LaunchedEffect로 비동기 처리 필요
                        - 캐싱 로직 직접 구현 필요
                        - 메모리 관리 직접 해야 함
                        - 이미지 다운샘플링 직접 구현
                        - Composable 생명주기 관리
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제 3: 캐싱 없는 이미지 로딩
 *
 * 캐싱이 없으면:
 * - 같은 이미지를 여러 번 다운로드
 * - 네트워크 대역폭 낭비
 * - 배터리 소모 증가
 * - 사용자 데이터 요금 낭비
 */
@Composable
private fun Problem3_NoCaching() {
    var loadCount by remember { mutableIntStateOf(0) }
    var totalLoadTimeMs by remember { mutableLongStateOf(0L) }

    val imageUrls = remember {
        listOf(
            "https://picsum.photos/seed/nocache1/200/200",
            "https://picsum.photos/seed/nocache2/200/200",
            "https://picsum.photos/seed/nocache3/200/200"
        )
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 3: 캐싱 없는 반복 로딩",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 캐싱 없이 로드하면 발생하는 문제 시뮬레이션
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                imageUrls.forEachIndexed { index, url ->
                    // key를 사용하여 loadCount 변경 시 강제 리컴포지션
                    key(loadCount, index) {
                        NoCacheImageItem(
                            url = url,
                            index = index,
                            onLoadComplete = { timeMs ->
                                totalLoadTimeMs += timeMs
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    loadCount++
                    // 새로운 로드 시작 시 시간 리셋하지 않음 - 누적 표시
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("다시 로드 (${loadCount}회 로드됨)")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 문제점 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = """
                        캐싱 없이 $loadCount 번 로드 시:
                        - 네트워크 요청: ${loadCount * 3}회 (3개 이미지 x $loadCount)
                        - 총 로딩 시간: ${totalLoadTimeMs}ms
                        - 동일 이미지 중복 다운로드!

                        Coil 캐싱 사용 시:
                        - 첫 번째만 네트워크 요청
                        - 이후는 메모리/디스크 캐시에서 즉시 로드
                        - 네트워크 비용 절약
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 캐싱 없이 이미지를 로드하는 아이템 (문제점 시연용)
 */
@Composable
private fun NoCacheImageItem(
    url: String,
    index: Int,
    onLoadComplete: (Long) -> Unit
) {
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadTimeMs by remember { mutableLongStateOf(0L) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        val startTime = System.currentTimeMillis()

        try {
            val loadedBitmap = withContext(Dispatchers.IO) {
                // 캐싱 없이 매번 네트워크에서 직접 로드
                URL(url).openStream().use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            }
            bitmap = loadedBitmap
            loadTimeMs = System.currentTimeMillis() - startTime
            onLoadComplete(loadTimeMs)
        } catch (e: Exception) {
            errorMessage = e.message?.take(20) ?: "Error"
        } finally {
            isLoading = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
                errorMessage != null -> {
                    Icon(
                        Icons.Default.BrokenImage,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(32.dp)
                    )
                }
                bitmap != null -> {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "이미지 ${index + 1}",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Text(
            text = if (loadTimeMs > 0) "${loadTimeMs}ms" else "...",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}
