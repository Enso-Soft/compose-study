package com.example.semantics_accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 문제가 있는 코드 - 접근성을 고려하지 않은 UI
 *
 * TalkBack으로 이 화면을 탐색하면 다음 문제가 발생합니다:
 * 1. 아이콘/이미지의 contentDescription 누락 - 무슨 요소인지 알 수 없음
 * 2. 개별 요소가 따로따로 읽힘 - 카드 전체 맥락 파악 어려움
 * 3. 버튼의 동작이 불명확함 - "버튼"으로만 읽힘
 * 4. 색상만으로 상태 표시 - 색맹 사용자가 인식 불가
 * 5. 동적 콘텐츠 변경 알림 없음 - 카운터 변화를 인지 못함
 */
@Composable
fun ProblemScreen() {
    var notificationCount by remember { mutableIntStateOf(3) }
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Problem: 접근성 미적용",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TalkBack으로 테스트해보세요!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 설정 > 접근성 > TalkBack 활성화")
                Text("2. 아래 카드들을 스와이프로 탐색")
                Text("3. 어떤 문제가 있는지 확인")
            }
        }

        HorizontalDivider()

        // 문제 1: contentDescription 누락
        Text(
            text = "문제 1: contentDescription 누락",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        BadProductCard()

        // 문제 2: 개별 요소 분리
        Text(
            text = "문제 2: 개별 요소가 따로 읽힘",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        BadProfileCard()

        // 문제 3: 버튼 동작 불명확
        Text(
            text = "문제 3: 버튼 동작 불명확",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // 문제: contentDescription = null
            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,  // 문제! TalkBack: "버튼"만 읽힘
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }

            IconButton(onClick = { /* 공유 */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null  // 문제!
                )
            }

            IconButton(onClick = { /* 삭제 */ }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null  // 문제!
                )
            }
        }

        // 문제 4: 색상만으로 상태 표시
        Text(
            text = "문제 4: 색상만으로 상태 표시",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        BadStatusIndicators()

        // 문제 5: 동적 콘텐츠 알림 없음
        Text(
            text = "문제 5: 동적 콘텐츠 알림 없음",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )

        BadNotificationCounter(
            count = notificationCount,
            onIncrement = { notificationCount++ },
            onDecrement = { if (notificationCount > 0) notificationCount-- }
        )

        // 문제 요약
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- 스크린 리더 사용자가 UI 이해 불가")
                Text("- 탐색에 불필요하게 오랜 시간 소요")
                Text("- 색맹 사용자는 상태 파악 불가")
                Text("- 동적 변경 사항을 인지할 수 없음")
                Text("- 자동화 접근성 테스트 실패")
            }
        }
    }
}

/**
 * 문제가 있는 상품 카드
 * - 이미지에 contentDescription 없음
 * - 장바구니 버튼 설명 없음
 */
@Composable
private fun BadProductCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 문제: contentDescription = null
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,  // 문제! 이미지 내용 불명
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "무선 이어폰",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "89,000원",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "30% 할인",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE91E63)
                )
            }

            // 문제: 버튼 설명 없음
            IconButton(onClick = { /* 장바구니 추가 */ }) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null  // 문제! TalkBack: "버튼"만 읽힘
                )
            }
        }
    }
}

/**
 * 문제가 있는 프로필 카드
 * - 각 요소가 개별적으로 읽힘
 * - 전체 맥락 파악 어려움
 */
@Composable
private fun BadProfileCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 개별 포커스 1
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "프로필",  // 개별 읽힘
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // 개별 포커스 2
                Text(
                    text = "홍길동",
                    style = MaterialTheme.typography.titleMedium
                )
                // 개별 포커스 3
                Text(
                    text = "Android 개발자",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 개별 포커스 4
            Button(onClick = { /* 팔로우 */ }) {
                Text("팔로우")
            }
        }
    }
    // TalkBack 결과: "프로필" → "홍길동" → "Android 개발자" → "팔로우, 버튼"
    // 4번의 스와이프가 필요하고, 하나의 카드임을 인식하기 어려움
}

/**
 * 문제가 있는 상태 표시
 * - 색상만으로 상태 구분
 * - 색맹 사용자가 구분 불가
 */
@Composable
private fun BadStatusIndicators() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 색상만으로 상태 표시 - 색맹 사용자는 구분 불가!
        StatusDot(color = Color(0xFF4CAF50))  // 초록 = ?
        StatusDot(color = Color(0xFFFFC107))  // 노랑 = ?
        StatusDot(color = Color(0xFFF44336))  // 빨강 = ?
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "색맹 사용자는 위 세 점의 상태를 구분할 수 없습니다",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
private fun StatusDot(color: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
    )
    // contentDescription도 없고, 텍스트 레이블도 없음
}

/**
 * 문제가 있는 알림 카운터
 * - 값이 변해도 스크린 리더가 알려주지 않음
 * - liveRegion 미적용
 */
@Composable
private fun BadNotificationCounter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("알림 개수:")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onDecrement) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null  // 문제!
                    )
                }

                // 문제: 값이 변해도 스크린 리더가 알려주지 않음
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                // liveRegion 없음!

                IconButton(onClick = onIncrement) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null  // 문제!
                    )
                }
            }
        }
    }

    Text(
        text = "버튼을 눌러도 TalkBack이 변경된 값을 알려주지 않습니다",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(top = 8.dp)
    )
}
