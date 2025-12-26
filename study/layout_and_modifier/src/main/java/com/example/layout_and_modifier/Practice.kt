package com.example.layout_and_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
 * 연습 문제: Layout & Modifier 활용하기
 */

// ============================================================
// 연습 1: Column과 Row 조합
// ============================================================

@Composable
fun Practice1_LayoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 1: 프로필 카드 레이아웃",
            description = "Column과 Row를 조합하여 프로필 카드를 만들어보세요"
        ) {
            Text("아래 구조를 구현하세요:")
            Spacer(modifier = Modifier.height(8.dp))

            // 목표 구조 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        """
Row {
    [프로필 이미지 Box]
    Column {
        Text("이름")
        Text("이메일")
    }
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 프로필 카드 구현
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                // TODO: Row 안에 프로필 이미지와 정보를 배치하세요
                // 힌트:
                // Row(
                //     modifier = Modifier.padding(16.dp),
                //     verticalAlignment = Alignment.CenterVertically
                // ) {
                //     Box(...) // 프로필 이미지
                //     Spacer(modifier = Modifier.width(16.dp))
                //     Column { Text(...) Text(...) }
                // }

                Text(
                    "여기에 Row와 Column을 구현하세요",
                    modifier = Modifier.padding(16.dp)
                )

                // 정답:
                // Row(
                //     modifier = Modifier.padding(16.dp),
                //     verticalAlignment = Alignment.CenterVertically
                // ) {
                //     Box(
                //         modifier = Modifier
                //             .size(60.dp)
                //             .clip(CircleShape)
                //             .background(Color(0xFF6200EE)),
                //         contentAlignment = Alignment.Center
                //     ) {
                //         Text("홍", color = Color.White, fontSize = 24.sp)
                //     }
                //     Spacer(modifier = Modifier.width(16.dp))
                //     Column {
                //         Text("홍길동", fontWeight = FontWeight.Bold)
                //         Text("hong@email.com")
                //     }
                // }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "Row(..., verticalAlignment = Alignment.CenterVertically)",
                "Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(...))",
                "Spacer(modifier = Modifier.width(16.dp))",
                "Column { Text(...) Text(...) }"
            )
        )
    }
}

// ============================================================
// 연습 2: Box와 오버레이
// ============================================================

@Composable
fun Practice2_BoxScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 2: 뱃지가 있는 알림 아이콘",
            description = "Box를 사용하여 아이콘 위에 뱃지를 오버레이하세요"
        ) {
            Text("목표: 알림 아이콘 우측 상단에 빨간 뱃지")
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 예시
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("예시", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("5", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                // TODO
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("구현", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(8.dp))

                    // TODO: Box 안에 Icon과 뱃지를 배치하세요
                    // 힌트:
                    // Box {
                    //     Icon(..., modifier = Modifier.size(48.dp))
                    //     Box(
                    //         modifier = Modifier
                    //             .size(20.dp)
                    //             .clip(CircleShape)
                    //             .background(Color.Red)
                    //             .align(Alignment.TopEnd),
                    //         contentAlignment = Alignment.Center
                    //     ) {
                    //         Text("3", ...)
                    //     }
                    // }

                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        "(뱃지를 추가하세요)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("추가 도전: 쇼핑 카트 아이콘에 뱃지 달기")
            Spacer(modifier = Modifier.height(8.dp))

            // TODO: 쇼핑 카트 + 뱃지
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                // TODO: Box로 감싸고 뱃지 추가
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "Box { Icon(...) Box(뱃지) }",
                "Modifier.align(Alignment.TopEnd)",
                "Modifier.clip(CircleShape).background(Color.Red)",
                "contentAlignment = Alignment.Center"
            )
        )
    }
}

// ============================================================
// 연습 3: Modifier 활용
// ============================================================

@Composable
fun Practice3_ModifierScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 3: Modifier 순서 실험",
            description = "Modifier 순서에 따른 결과 차이를 확인해보세요"
        ) {
            Text("Modifier 순서를 바꿔보고 결과를 비교하세요:")
            Spacer(modifier = Modifier.height(16.dp))

            // 실험 1: padding과 background 순서
            Text("실험 1: padding ↔ background")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("A", style = MaterialTheme.typography.labelSmall)
                    // TODO: background 먼저, padding 나중
                    // 힌트: Modifier.background(Color.Red).padding(16.dp)
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE57373))
                            // TODO: padding(16.dp) 추가
                    ) {
                        Text("Hello")
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("B", style = MaterialTheme.typography.labelSmall)
                    // TODO: padding 먼저, background 나중
                    // 힌트: Modifier.padding(16.dp).background(Color.Green)
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            // TODO: background(Color.Green) 추가
                    ) {
                        Text("Hello")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 실험 2: size와 fillMaxWidth
            Text("실험 2: size vs fillMaxWidth")
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // TODO: size(100.dp) 적용
                Card(
                    modifier = Modifier
                    // TODO: .size(100.dp) 추가
                ) {
                    Text("size(100.dp)", modifier = Modifier.padding(8.dp))
                }

                // TODO: fillMaxWidth() 적용
                Card(
                    modifier = Modifier
                    // TODO: .fillMaxWidth() 추가
                ) {
                    Text("fillMaxWidth()", modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 실험 3: clip과 background
            Text("실험 3: clip + background")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("둥근 모서리", style = MaterialTheme.typography.labelSmall)
                    // TODO: clip(RoundedCornerShape(16.dp)).background(Color.Blue)
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            // TODO: clip과 background 추가
                            .background(Color.Gray)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("원형", style = MaterialTheme.typography.labelSmall)
                    // TODO: clip(CircleShape).background(Color.Blue)
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            // TODO: clip과 background 추가
                            .background(Color.Gray)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "Modifier.background(color).padding(dp) - 패딩 영역 배경 없음",
                "Modifier.padding(dp).background(color) - 패딩 포함 배경",
                "Modifier.size(dp) - 고정 크기",
                "Modifier.fillMaxWidth() - 가로 꽉 채움",
                "Modifier.clip(RoundedCornerShape(dp)).background(color)",
                "Modifier.clip(CircleShape).background(color)"
            )
        )
    }
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
private fun PracticeCard(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "힌트",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            hints.forEach { hint ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
