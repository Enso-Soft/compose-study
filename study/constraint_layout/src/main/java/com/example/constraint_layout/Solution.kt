package com.example.constraint_layout

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

/**
 * 해결책: ConstraintLayout으로 플랫한 구조 구현
 *
 * ConstraintLayout을 사용하면:
 * 1. 모든 요소가 직접 자식으로 플랫한 구조
 * 2. 제약 조건으로 관계를 명확하게 표현
 * 3. Barrier로 동적 정렬 가능
 * 4. Chain으로 균등 배치 가능
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

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
                    text = "해결책: ConstraintLayout 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        • 플랫한 구조로 모든 요소가 직접 자식
                        • 제약 조건으로 관계를 명확하게 표현
                        • Barrier로 동적 정렬 가능
                        • Chain으로 균등 배치 가능
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 데모 선택 칩
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("프로필 카드") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("Barrier") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("Chain") }
            )
            FilterChip(
                selected = selectedDemo == 3,
                onClick = { selectedDemo = 3 },
                label = { Text("Guideline") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> ConstraintProfileCard()
            1 -> BarrierDemo()
            2 -> ChainDemo()
            3 -> GuidelineDemo()
        }
    }
}

/**
 * ConstraintLayout으로 구현한 프로필 카드
 * 플랫한 구조로 모든 요소가 ConstraintLayout의 직접 자식
 */
@Composable
private fun ConstraintProfileCard() {
    Column {
        Text(
            text = "ConstraintLayout 프로필 카드",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 모든 참조를 한 번에 생성
                val (avatar, name, time, description, likeBtn, commentBtn, shareBtn) = createRefs()

                // 아바타
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .constrainAs(avatar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // 이름 - 아바타 오른쪽에 배치
                Text(
                    text = "홍길동",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(name) {
                        top.linkTo(avatar.top)
                        start.linkTo(avatar.end, margin = 12.dp)
                    }
                )

                // 시간 - 오른쪽 끝에 배치
                Text(
                    text = "오후 3:42",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(avatar.top)
                        end.linkTo(parent.end)
                    }
                )

                // 설명 - 아바타 아래, 아바타 오른쪽부터 시작
                Text(
                    text = "오늘 날씨가 정말 좋네요! 공원에서 산책하기 딱 좋은 날씨입니다. 다들 즐거운 하루 보내세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(description) {
                        top.linkTo(name.bottom, margin = 8.dp)
                        start.linkTo(avatar.end, margin = 12.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                )

                // 버튼들을 Chain으로 균등 배치
                createHorizontalChain(likeBtn, commentBtn, shareBtn, chainStyle = ChainStyle.SpreadInside)

                TextButton(
                    onClick = { },
                    modifier = Modifier.constrainAs(likeBtn) {
                        top.linkTo(description.bottom, margin = 12.dp)
                        start.linkTo(avatar.end)
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.ThumbUp, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("좋아요")
                }

                TextButton(
                    onClick = { },
                    modifier = Modifier.constrainAs(commentBtn) {
                        top.linkTo(description.bottom, margin = 12.dp)
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Comment, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("댓글")
                }

                TextButton(
                    onClick = { },
                    modifier = Modifier.constrainAs(shareBtn) {
                        top.linkTo(description.bottom, margin = 12.dp)
                        end.linkTo(parent.end)
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Share, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("공유")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 핵심 포인트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
• createRefs()로 참조 생성
• constrainAs()로 제약 조건 적용
• linkTo()로 요소 간 연결
• Dimension.fillToConstraints로 너비 제약
• createHorizontalChain()으로 버튼 균등 배치
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Barrier 데모: 레이블 끝을 기준으로 값 정렬
 */
@Composable
private fun BarrierDemo() {
    Column {
        Text(
            text = "Barrier: 동적 정렬",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (label1, label2, label3, label4) = createRefs()
                val (value1, value2, value3, value4) = createRefs()

                // 레이블들
                Text(
                    text = "이름",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "이메일 주소",  // 더 긴 레이블
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label2) {
                        top.linkTo(label1.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "전화번호",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label3) {
                        top.linkTo(label2.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "주소",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label4) {
                        top.linkTo(label3.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )

                // Barrier: 가장 긴 레이블의 끝에 생성
                val endBarrier = createEndBarrier(label1, label2, label3, label4, margin = 16.dp)

                // 값들: Barrier 기준으로 정렬
                Text(
                    text = "홍길동",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(value1) {
                        top.linkTo(label1.top)
                        start.linkTo(endBarrier)
                    }
                )
                Text(
                    text = "hong@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(value2) {
                        top.linkTo(label2.top)
                        start.linkTo(endBarrier)
                    }
                )
                Text(
                    text = "010-1234-5678",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(value3) {
                        top.linkTo(label3.top)
                        start.linkTo(endBarrier)
                    }
                )
                Text(
                    text = "서울시 강남구",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(value4) {
                        top.linkTo(label4.top)
                        start.linkTo(endBarrier)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Barrier 핵심",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
• createEndBarrier()로 여러 요소의 끝에 배리어 생성
• 가장 긴 요소 기준으로 자동 정렬
• 레이블 길이가 변해도 값들이 정렬 유지
• 고정 너비 하드코딩 불필요
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Chain 데모: 요소들을 균등하게 배치
 */
@Composable
private fun ChainDemo() {
    var chainStyle by remember { mutableStateOf(ChainStyle.Spread) }

    Column {
        Text(
            text = "Chain: 균등 배치",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ChainStyle 선택
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = chainStyle == ChainStyle.Spread,
                onClick = { chainStyle = ChainStyle.Spread },
                label = { Text("Spread") }
            )
            FilterChip(
                selected = chainStyle == ChainStyle.SpreadInside,
                onClick = { chainStyle = ChainStyle.SpreadInside },
                label = { Text("SpreadInside") }
            )
            FilterChip(
                selected = chainStyle == ChainStyle.Packed,
                onClick = { chainStyle = ChainStyle.Packed },
                label = { Text("Packed") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(80.dp)
            ) {
                val (btn1, btn2, btn3) = createRefs()

                // Chain 생성
                createHorizontalChain(btn1, btn2, btn3, chainStyle = chainStyle)

                Button(
                    onClick = { },
                    modifier = Modifier.constrainAs(btn1) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    Icon(Icons.Default.Home, null)
                    Spacer(Modifier.width(4.dp))
                    Text("홈")
                }

                Button(
                    onClick = { },
                    modifier = Modifier.constrainAs(btn2) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    Icon(Icons.Default.Search, null)
                    Spacer(Modifier.width(4.dp))
                    Text("검색")
                }

                Button(
                    onClick = { },
                    modifier = Modifier.constrainAs(btn3) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    Icon(Icons.Default.Person, null)
                    Spacer(Modifier.width(4.dp))
                    Text("프로필")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "ChainStyle 설명",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (chainStyle) {
                        ChainStyle.Spread -> "Spread: 모든 요소를 균등하게 분배 (여백 포함)"
                        ChainStyle.SpreadInside -> "SpreadInside: 첫/끝 요소는 가장자리에, 나머지 균등 분배"
                        ChainStyle.Packed -> "Packed: 모든 요소를 중앙에 모아서 배치"
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Guideline 데모: 비율 기반 배치
 */
@Composable
private fun GuidelineDemo() {
    Column {
        Text(
            text = "Guideline: 비율 기반 배치",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            ) {
                val (leftBox, rightBox, centerText) = createRefs()

                // 30% 지점에 수직 가이드라인
                val startGuideline = createGuidelineFromStart(0.3f)
                // 50% 지점에 수평 가이드라인
                val topGuideline = createGuidelineFromTop(0.5f)

                // 왼쪽 영역 (0% ~ 30%)
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .constrainAs(leftBox) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(startGuideline)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "30%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // 오른쪽 영역 (30% ~ 100%)
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .constrainAs(rightBox) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(startGuideline, margin = 8.dp)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "70%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                // 중앙 텍스트 (가이드라인 교차점)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            CircleShape
                        )
                        .constrainAs(centerText) {
                            top.linkTo(topGuideline)
                            bottom.linkTo(topGuideline)
                            start.linkTo(startGuideline)
                            end.linkTo(startGuideline)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "50%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Guideline 핵심",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
• createGuidelineFromStart(0.3f): 시작에서 30% 지점
• createGuidelineFromTop(0.5f): 상단에서 50% 지점
• createGuidelineFromEnd(16.dp): 끝에서 16dp 지점
• 화면 크기에 따라 자동 조정 (반응형)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
