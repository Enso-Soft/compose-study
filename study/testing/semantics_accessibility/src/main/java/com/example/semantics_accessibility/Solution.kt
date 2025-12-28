package com.example.semantics_accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 올바른 코드 - 접근성을 고려한 UI
 *
 * TalkBack으로 이 화면을 탐색하면:
 * 1. 모든 요소에 의미 있는 설명이 제공됨
 * 2. 관련 요소들이 논리적으로 그룹화됨
 * 3. 버튼의 동작이 명확하게 전달됨
 * 4. 색상 외에 텍스트/아이콘으로도 상태 표시
 * 5. 동적 콘텐츠 변경 시 자동 알림
 */
@Composable
fun SolutionScreen() {
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
            text = "Solution: 접근성 적용",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 해결책 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TalkBack으로 비교해보세요!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Problem 탭과 비교하여 어떻게 개선되었는지 확인하세요.")
                Text("각 요소가 더 명확하고 효율적으로 읽힙니다.")
            }
        }

        HorizontalDivider()

        // 해결책 1: contentDescription 제공
        Text(
            text = "해결책 1: 의미 있는 설명 제공",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        GoodProductCard()

        // 해결책 2: 요소 병합
        Text(
            text = "해결책 2: mergeDescendants로 그룹화",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        GoodProfileCard()

        // 해결책 3: 버튼 동작 명확화
        Text(
            text = "해결책 3: 명확한 버튼 설명",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // 해결책: 명확한 contentDescription
            IconButton(
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier.semantics {
                    contentDescription = if (isFavorite) "좋아요 취소" else "좋아요 추가"
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,  // 부모에서 설정
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }

            IconButton(
                onClick = { /* 공유 */ },
                modifier = Modifier.semantics {
                    contentDescription = "공유하기"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null  // 부모에서 설정
                )
            }

            IconButton(
                onClick = { /* 삭제 */ },
                modifier = Modifier.semantics {
                    contentDescription = "삭제하기"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null  // 부모에서 설정
                )
            }
        }

        // 해결책 4: 색상 + 텍스트/아이콘
        Text(
            text = "해결책 4: 색상 + 텍스트/아이콘",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        GoodStatusIndicators()

        // 해결책 5: liveRegion 적용
        Text(
            text = "해결책 5: liveRegion으로 동적 알림",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        GoodNotificationCounter(
            count = notificationCount,
            onIncrement = { notificationCount++ },
            onDecrement = { if (notificationCount > 0) notificationCount-- }
        )

        // 추가: traversalIndex 예시
        Text(
            text = "보너스: traversalIndex로 탐색 순서 조정",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        TraversalOrderExample()

        // 해결책 요약
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. contentDescription으로 의미 있는 설명 제공")
                Text("2. mergeDescendants로 논리적 그룹화")
                Text("3. clearAndSetSemantics로 커스텀 설명")
                Text("4. liveRegion으로 동적 변경 알림")
                Text("5. 색상 외 텍스트/아이콘으로 정보 전달")
                Text("6. traversalIndex로 탐색 순서 최적화")
            }
        }
    }
}

/**
 * 올바른 상품 카드
 * - 모든 요소에 적절한 설명
 * - 카드 전체가 하나의 단위로 병합
 */
@Composable
private fun GoodProductCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                // 카드 정보를 하나로 병합
                .semantics(mergeDescendants = true) {
                    contentDescription = "무선 이어폰, 89,000원, 30% 할인"
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "무선 이어폰 상품 이미지",  // 해결!
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

            // 해결: 명확한 버튼 설명
            IconButton(
                onClick = { /* 장바구니 추가 */ },
                modifier = Modifier.semantics {
                    contentDescription = "장바구니에 추가"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null  // 부모에서 설정
                )
            }
        }
    }
    // TalkBack 결과: "무선 이어폰, 89,000원, 30% 할인" → "장바구니에 추가, 버튼"
}

/**
 * 올바른 프로필 카드
 * - 모든 요소가 하나로 병합
 * - 팔로우 버튼만 개별 인터랙션
 */
@Composable
private fun GoodProfileCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable { /* 프로필 상세 이동 */ }
                // clickable이 자동으로 mergeDescendants = true 적용
                .clearAndSetSemantics {
                    contentDescription = "홍길동, Android 개발자 프로필로 이동"
                    role = Role.Button
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,  // 부모에서 처리
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "홍길동",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Android 개발자",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 팔로우 버튼은 별도 인터랙션이므로 개별 포커스
        Button(
            onClick = { /* 팔로우 */ },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.End)
        ) {
            Text("팔로우")
        }
    }
    // TalkBack 결과: "홍길동, Android 개발자 프로필로 이동, 버튼" → "팔로우, 버튼"
    // 2번의 스와이프로 모든 정보 파악 + 인터랙션 가능
}

/**
 * 올바른 상태 표시
 * - 색상 + 텍스트 + 아이콘
 * - 색맹 사용자도 구분 가능
 */
@Composable
private fun GoodStatusIndicators() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatusWithLabel(
            status = "정상",
            color = Color(0xFF4CAF50),
            icon = Icons.Default.CheckCircle
        )
        StatusWithLabel(
            status = "경고",
            color = Color(0xFFFFC107),
            icon = Icons.Default.Warning
        )
        StatusWithLabel(
            status = "오류",
            color = Color(0xFFF44336),
            icon = Icons.Default.Close
        )
    }
}

@Composable
private fun StatusWithLabel(
    status: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.semantics(mergeDescendants = true) {
            contentDescription = "$status 상태"
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,  // 부모에서 처리
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = status,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}

/**
 * 올바른 알림 카운터
 * - liveRegion 적용으로 변경 시 자동 알림
 * - 버튼 동작도 명확
 */
@Composable
private fun GoodNotificationCounter(
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
                IconButton(
                    onClick = onDecrement,
                    modifier = Modifier.semantics {
                        contentDescription = "알림 개수 감소"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null  // 부모에서 설정
                    )
                }

                // 해결: liveRegion으로 변경 시 자동 알림
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.semantics {
                        contentDescription = "현재 알림 ${count}개"
                        liveRegion = LiveRegionMode.Polite
                    }
                )

                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier.semantics {
                        contentDescription = "알림 개수 증가"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null  // 부모에서 설정
                    )
                }
            }
        }
    }

    Text(
        text = "버튼을 누르면 TalkBack이 변경된 값을 자동으로 알려줍니다!",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

/**
 * traversalIndex 예시
 * 시각적 배치와 다른 탐색 순서 설정
 */
@Composable
private fun TraversalOrderExample() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .semantics { isTraversalGroup = true }
        ) {
            Text(
                text = "탐색 순서 조정 예시",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.semantics { traversalIndex = 0f }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 시각적으로 왼쪽이지만 나중에 탐색
                Text(
                    text = "도움말 (나중에 읽힘)",
                    modifier = Modifier.semantics { traversalIndex = 3f },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 시각적으로 오른쪽이지만 먼저 탐색
                Button(
                    onClick = { },
                    modifier = Modifier.semantics { traversalIndex = 1f }
                ) {
                    Text("확인 (먼저 읽힘)")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "traversalIndex가 낮을수록 먼저 탐색됩니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.semantics { traversalIndex = 2f }
            )
        }
    }
}
