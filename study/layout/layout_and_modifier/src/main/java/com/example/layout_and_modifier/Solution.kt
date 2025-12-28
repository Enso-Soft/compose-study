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
 * Solution: Layout & Modifier 올바르게 사용하기
 *
 * 1. Column, Row, Box 사용법
 * 2. Arrangement와 Alignment
 * 3. Modifier 활용
 */

@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 해결책 소개
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Layout & Modifier 마스터하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Column, Row, Box와 Modifier를 자유롭게 조합하여 어떤 레이아웃도 만들 수 있습니다!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 1. Column/Row/Box 갤러리
        LayoutGallery()

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Arrangement 갤러리
        ArrangementGallery()

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Box와 정렬
        BoxAlignmentDemo()

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Modifier 활용
        ModifierDemo()

        Spacer(modifier = Modifier.height(16.dp))

        // 5. 실전 예제
        RealWorldExample()
    }
}

@Composable
private fun LayoutGallery() {
    SolutionCard(
        title = "1. Column / Row / Box",
        explanation = "세로, 가로, 겹치기 - 세 가지 기본 레이아웃"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Column", fontWeight = FontWeight.Bold)
                Text("(세로)", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        ColorBox(Color(0xFF2196F3), "1")
                        ColorBox(Color(0xFF64B5F6), "2")
                        ColorBox(Color(0xFFBBDEFB), "3")
                    }
                }
            }

            // Row
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Row", fontWeight = FontWeight.Bold)
                Text("(가로)", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFCE4EC)
                    )
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        ColorBox(Color(0xFFE91E63), "1")
                        ColorBox(Color(0xFFF06292), "2")
                        ColorBox(Color(0xFFF8BBD9), "3")
                    }
                }
            }

            // Box
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Box", fontWeight = FontWeight.Bold)
                Text("(겹침)", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Box(modifier = Modifier.padding(8.dp)) {
                        ColorBox(Color(0xFF4CAF50), "1", size = 60)
                        ColorBox(Color(0xFF81C784), "2", size = 45)
                        ColorBox(Color(0xFFC8E6C9), "3", size = 30)
                    }
                }
            }
        }
    }
}

@Composable
private fun ArrangementGallery() {
    var selectedArrangement by remember { mutableIntStateOf(0) }
    val arrangements = listOf("Start", "Center", "End", "SpaceBetween", "SpaceEvenly", "spacedBy")

    SolutionCard(
        title = "2. Arrangement (배치)",
        explanation = "Row의 horizontalArrangement로 가로 배치를 제어합니다"
    ) {
        // 선택 칩
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            arrangements.forEachIndexed { index, name ->
                FilterChip(
                    selected = selectedArrangement == index,
                    onClick = { selectedArrangement = index },
                    label = { Text(name, fontSize = 10.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 결과 표시
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = when (selectedArrangement) {
                    0 -> Arrangement.Start
                    1 -> Arrangement.Center
                    2 -> Arrangement.End
                    3 -> Arrangement.SpaceBetween
                    4 -> Arrangement.SpaceEvenly
                    else -> Arrangement.spacedBy(16.dp)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColorBox(Color(0xFF2196F3), "A")
                ColorBox(Color(0xFF4CAF50), "B")
                ColorBox(Color(0xFFF44336), "C")
            }
        }
    }
}

@Composable
private fun BoxAlignmentDemo() {
    SolutionCard(
        title = "3. Box Alignment (정렬)",
        explanation = "Box 안에서 개별 자식의 위치를 Modifier.align()으로 지정합니다"
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF8E1)
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "TopStart",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    fontSize = 12.sp
                )
                Text(
                    "TopEnd",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    fontSize = 12.sp
                )
                Text(
                    "Center",
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "BottomStart",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    fontSize = 12.sp
                )
                Text(
                    "BottomEnd",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ModifierDemo() {
    SolutionCard(
        title = "4. Modifier 활용",
        explanation = "size, padding, background, border, clip 등을 조합합니다"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 기본
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("기본", style = MaterialTheme.typography.labelSmall)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF2196F3))
                )
            }

            // padding
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("padding", style = MaterialTheme.typography.labelSmall)
                Box(
                    modifier = Modifier
                        .background(Color(0xFFBBDEFB))
                        .padding(8.dp)
                        .size(44.dp)
                        .background(Color(0xFF2196F3))
                )
            }

            // border
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("border", style = MaterialTheme.typography.labelSmall)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .border(3.dp, Color(0xFF1565C0))
                        .background(Color(0xFF2196F3))
                )
            }

            // clip
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("clip", style = MaterialTheme.typography.labelSmall)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF2196F3))
                )
            }

            // circle
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("circle", style = MaterialTheme.typography.labelSmall)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3))
                )
            }
        }
    }
}

@Composable
private fun RealWorldExample() {
    SolutionCard(
        title = "5. 실전 예제: 프로필 카드",
        explanation = "Column, Row, Box, Modifier를 조합한 실제 UI"
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 프로필 이미지 + 뱃지
                Box {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF9C27B0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    // 온라인 뱃지
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50))
                            .border(2.dp, Color.White, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 정보
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "홍길동",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "hong@email.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text("개발자", fontSize = 10.sp) },
                            modifier = Modifier.height(24.dp)
                        )
                        AssistChip(
                            onClick = { },
                            label = { Text("안드로이드", fontSize = 10.sp) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }

                // 액션 버튼
                IconButton(onClick = { }) {
                    Icon(Icons.Default.MoreVert, "더보기")
                }
            }
        }
    }
}

@Composable
private fun ColorBox(color: Color, text: String, size: Int = 30) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SolutionCard(
    title: String,
    explanation: String,
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
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}
