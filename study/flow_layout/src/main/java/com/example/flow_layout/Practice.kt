package com.example.flow_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 태그 클라우드 구현
 *
 * 요구사항:
 * - FlowRow를 사용해서 태그 목록을 표시
 * - 태그를 클릭하면 선택/해제
 * - horizontalArrangement와 verticalArrangement에 spacedBy(8.dp) 적용
 *
 * TODO: FlowRow를 사용해서 구현하세요!
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Practice1_TagCloud() {
    val interests = listOf(
        "여행", "음악", "영화", "독서", "운동", "요리",
        "사진", "게임", "캠핑", "그림", "드라마", "애니메이션"
    )

    var selectedInterests by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "연습 1: 태그 클라우드",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: FlowRow를 사용해서 관심사 태그를 표시하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "관심사를 선택하세요",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // TODO: 아래 Row를 FlowRow로 변경하세요!
                // 힌트: horizontalArrangement = Arrangement.spacedBy(8.dp)
                // 힌트: verticalArrangement = Arrangement.spacedBy(8.dp)

                // 현재 문제가 있는 코드 (Row 사용)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    interests.take(4).forEach { interest ->
                        FilterChip(
                            selected = selectedInterests.contains(interest),
                            onClick = {
                                selectedInterests = if (selectedInterests.contains(interest)) {
                                    selectedInterests - interest
                                } else {
                                    selectedInterests + interest
                                }
                            },
                            label = { Text(interest) }
                        )
                    }
                }

                /*
                // 정답: FlowRow 사용
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    interests.forEach { interest ->
                        FilterChip(
                            selected = selectedInterests.contains(interest),
                            onClick = {
                                selectedInterests = if (selectedInterests.contains(interest)) {
                                    selectedInterests - interest
                                } else {
                                    selectedInterests + interest
                                }
                            },
                            label = { Text(interest) }
                        )
                    }
                }
                */

                if (selectedInterests.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "선택된 관심사: ${selectedInterests.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Row를 FlowRow로 변경",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "2. interests.take(4)를 interests로 변경",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "3. verticalArrangement 추가",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 연습 문제 2: 그리드 레이아웃 (maxItemsInEachRow + weight)
 *
 * 요구사항:
 * - FlowRow로 3열 그리드 레이아웃 구현
 * - maxItemsInEachRow = 3 설정
 * - Modifier.weight(1f)로 균등 너비 적용
 * - 카드 형태의 아이템 표시
 *
 * TODO: FlowRow와 maxItemsInEachRow, weight를 조합해서 구현하세요!
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Practice2_GridLayout() {
    val menuItems = listOf(
        "홈", "검색", "알림", "메시지", "프로필", "설정",
        "도움말", "정보", "로그아웃"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "연습 2: 그리드 레이아웃",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: maxItemsInEachRow와 weight를 사용해서 3열 그리드를 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: FlowRow를 사용해서 3열 그리드 구현
        // 힌트: maxItemsInEachRow = 3
        // 힌트: 각 아이템에 Modifier.weight(1f) 적용

        // 현재 문제가 있는 코드 (Column + Row 조합)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            menuItems.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { item ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                    // 빈 공간 채우기 (마지막 행이 3개가 아닐 때)
                    repeat(3 - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        /*
        // 정답: FlowRow + maxItemsInEachRow + weight 사용
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 3
        ) {
            menuItems.forEach { item ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        */

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Column + chunked 대신 FlowRow 사용",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "2. maxItemsInEachRow = 3으로 행당 3개 제한",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "3. weight(1f)는 해당 행에서만 적용됨",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "4. FlowRow가 자동으로 빈 공간 처리",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 연습 문제 3: FlowColumn 사용
 *
 * 요구사항:
 * - FlowColumn으로 세로 방향 래핑 레이아웃 구현
 * - 고정 높이(200.dp) 컨테이너 사용
 * - maxItemsInEachColumn = 5 설정
 * - 색상 팔레트 표시
 *
 * TODO: FlowColumn을 사용해서 구현하세요!
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Practice3_FlowColumn() {
    val colorPalette = listOf(
        "Red" to Color(0xFFE53935),
        "Pink" to Color(0xFFD81B60),
        "Purple" to Color(0xFF8E24AA),
        "Deep Purple" to Color(0xFF5E35B1),
        "Indigo" to Color(0xFF3949AB),
        "Blue" to Color(0xFF1E88E5),
        "Light Blue" to Color(0xFF039BE5),
        "Cyan" to Color(0xFF00ACC1),
        "Teal" to Color(0xFF00897B),
        "Green" to Color(0xFF43A047),
        "Light Green" to Color(0xFF7CB342),
        "Lime" to Color(0xFFC0CA33),
        "Yellow" to Color(0xFFFDD835),
        "Amber" to Color(0xFFFFB300),
        "Orange" to Color(0xFFFB8C00)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "연습 3: FlowColumn",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: FlowColumn을 사용해서 색상 팔레트를 세로로 배치하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "색상 팔레트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // TODO: 아래 Column을 FlowColumn으로 변경하세요!
                // 힌트: modifier = Modifier.fillMaxWidth().height(200.dp)
                // 힌트: maxItemsInEachColumn = 5
                // 힌트: verticalArrangement = Arrangement.spacedBy(8.dp)
                // 힌트: horizontalArrangement = Arrangement.spacedBy(12.dp)

                // 현재 문제가 있는 코드 (Column 사용 - 세로로만 배치)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Column은 세로로만 배치되어 잘림
                        colorPalette.take(5).forEach { (name, color) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(color, RoundedCornerShape(4.dp))
                                )
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                /*
                // 정답: FlowColumn 사용
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    FlowColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        maxItemsInEachColumn = 5
                    ) {
                        colorPalette.forEach { (name, color) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(color, RoundedCornerShape(4.dp))
                                )
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
                */

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "5개씩 열을 나누어 표시 (현재는 5개만 보임)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Column을 FlowColumn으로 변경",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "2. FlowColumn에는 고정 높이(height) 필수",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "3. take(5) 제거하고 전체 colorPalette 사용",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "4. maxItemsInEachColumn으로 열당 개수 제한",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
