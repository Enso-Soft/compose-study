package com.example.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
 * 문제 상황: LazyRow로 이미지 갤러리 구현 시 한계
 *
 * LazyRow를 사용하면:
 * 1. 모든 아이템이 동일한 크기로 표시됨
 * 2. 포커스/강조 효과를 직접 구현해야 함
 * 3. "더 있다"는 힌트가 부족함
 * 4. Material Design 가이드라인 미준수
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
                    text = "문제: LazyRow로 이미지 갤러리 구현",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "LazyRow를 사용하면 모든 아이템이 동일한 크기입니다.\n" +
                            "아이템 크기 전환 효과나 포커스 강조가 없습니다.\n" +
                            "스크롤해도 아이템 크기가 변하지 않습니다!",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제가 있는 구현: LazyRow 사용
        Text(
            text = "LazyRow 갤러리 (문제점 확인)",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProblemGallery()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 강조
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProblemPoint("1. 모든 카드가 같은 크기 - 어떤 것이 현재 아이템인지 모름")
                ProblemPoint("2. 스크롤 위치에 따른 크기 변화 없음")
                ProblemPoint("3. 양쪽 끝이 잘려 보여서 '더 있다'는 힌트 부족")
                ProblemPoint("4. Material 3 디자인 가이드라인 미준수")
            }
        }
    }
}

@Composable
private fun ProblemGallery() {
    val items = remember {
        listOf(
            GalleryItem(0, "여행", Color(0xFF6200EE)),
            GalleryItem(1, "음식", Color(0xFF03DAC5)),
            GalleryItem(2, "자연", Color(0xFFFF6D00)),
            GalleryItem(3, "도시", Color(0xFF3700B3)),
            GalleryItem(4, "인물", Color(0xFF018786)),
        )
    }

    // 문제가 있는 구현: 모든 아이템이 동일한 크기!
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items.size) { index ->
            val item = items[index]
            // 모든 아이템이 동일한 크기 - 문제점!
            Card(
                modifier = Modifier.size(186.dp, 205.dp),
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
                            text = "동일한 크기",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProblemPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * 갤러리 아이템 데이터 클래스
 */
data class GalleryItem(
    val id: Int,
    val title: String,
    val color: Color
)
