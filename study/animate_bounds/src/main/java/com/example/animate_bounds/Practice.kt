package com.example.animate_bounds

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Animate Bounds - 연습 문제
 *
 * 각 연습 문제는 TODO 주석을 따라 구현해보세요.
 * 힌트가 제공되며, 주석 처리된 정답을 참고할 수 있습니다.
 */

// ============================================================
// 연습 1: 기본 animateBounds 적용하기 (난이도: 하)
// ============================================================

/**
 * 연습 1: 기본 LookaheadScope + animateBounds 적용
 *
 * 목표: 버튼 클릭 시 박스의 크기와 위치가 부드럽게 변경되도록 구현
 *
 * TODO:
 * 1. Column을 LookaheadScope { } 으로 감싸세요
 * 2. Box에 Modifier.animateBounds(this@LookaheadScope) 추가
 *
 * 힌트:
 * - LookaheadScope는 애니메이션 좌표 공간을 정의
 * - animateBounds는 위치와 크기를 모두 애니메이션
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Practice1_BasicAnimateBounds() {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 animateBounds 적용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. 아래 Column을 LookaheadScope로 감싸세요
                        |2. Box에 animateBounds modifier를 추가하세요
                        |3. 버튼을 클릭하여 애니메이션을 확인하세요
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: 이 Column을 LookaheadScope { } 으로 감싸세요

        /* 정답:
        LookaheadScope {
            Column(
                horizontalAlignment = if(expanded) Alignment.End else Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .animateBounds(this@LookaheadScope)
                        .size(if(expanded) 150.dp else 80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF42A5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if(expanded) "확장됨" else "축소됨",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { expanded = !expanded }) {
                    Text(if(expanded) "축소하기" else "확장하기")
                }
            }
        }
        */

        // 현재 구현 (animateBounds 없음 - 점프!)
        Column(
            horizontalAlignment = if(expanded) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    // TODO: .animateBounds(this@LookaheadScope) 추가
                    .size(if(expanded) 150.dp else 80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF42A5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if(expanded) "확장됨" else "축소됨",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { expanded = !expanded }) {
                Text(if(expanded) "축소하기" else "확장하기")
            }
        }
    }
}

// ============================================================
// 연습 2: BoundsTransform 커스터마이징 (난이도: 중)
// ============================================================

/**
 * 연습 2: BoundsTransform으로 커스텀 애니메이션
 *
 * 목표: 카드 선택 시 bouncy spring 애니메이션 적용
 *
 * TODO:
 * 1. boundsTransform 변수에 BoundsTransform 구현
 * 2. spring 애니메이션 적용:
 *    - dampingRatio: Spring.DampingRatioMediumBouncy
 *    - stiffness: Spring.StiffnessLow
 * 3. animateBounds에 boundsTransform 파라미터 추가
 *
 * 힌트:
 * - BoundsTransform { initialBounds, targetBounds -> ... }
 * - spring(dampingRatio, stiffness) 사용
 */
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class)
@Composable
fun Practice2_CustomBoundsTransform() {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val colors = listOf(
        Color(0xFFE57373),
        Color(0xFF81C784),
        Color(0xFF64B5F6),
        Color(0xFFFFD54F),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: BoundsTransform 커스터마이징",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. boundsTransform에 BoundsTransform 구현
                        |2. spring 애니메이션 적용 (bouncy + low stiffness)
                        |3. 카드를 클릭하여 bouncy 효과 확인
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: BoundsTransform 구현
        // val boundsTransform: BoundsTransform = BoundsTransform { _, _ ->
        //     spring(
        //         dampingRatio = Spring.DampingRatioMediumBouncy,
        //         stiffness = Spring.StiffnessLow
        //     )
        // }

        /* 정답:
        val boundsTransform: BoundsTransform = BoundsTransform { _, _ ->
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        }

        LookaheadScope {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                colors.forEachIndexed { index, color ->
                    val isSelected = index == selectedIndex
                    val size = if (isSelected) 100.dp else 60.dp

                    key(index) {
                        Box(
                            modifier = Modifier
                                .animateBounds(
                                    lookaheadScope = this@LookaheadScope,
                                    boundsTransform = boundsTransform  // 여기!
                                )
                                .size(size)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color)
                                .clickable {
                                    selectedIndex = if (isSelected) null else index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        */

        // 현재 구현 (기본 애니메이션)
        LookaheadScope {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                colors.forEachIndexed { index, color ->
                    val isSelected = index == selectedIndex
                    val size = if (isSelected) 100.dp else 60.dp

                    key(index) {
                        Box(
                            modifier = Modifier
                                .animateBounds(
                                    lookaheadScope = this@LookaheadScope
                                    // TODO: boundsTransform 파라미터 추가
                                )
                                .size(size)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color)
                                .clickable {
                                    selectedIndex = if (isSelected) null else index
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// 연습 3: 리스트 아이템 재정렬 애니메이션 (난이도: 상)
// ============================================================

/**
 * 연습 3: 리스트 아이템 재정렬 애니메이션
 *
 * 목표: 아이템 순서 변경 시 부드러운 재정렬 애니메이션 구현
 *
 * TODO:
 * 1. Column을 LookaheadScope로 감싸기
 * 2. 각 아이템에 key(item)로 안정적인 식별자 제공
 * 3. 아이템 Row에 animateBounds 적용
 * 4. "섞기" 버튼 클릭 시 아이템들이 부드럽게 재배치되도록
 *
 * 힌트:
 * - key()는 재정렬 애니메이션에 필수!
 * - key가 없으면 Compose가 아이템을 추적하지 못함
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Practice3_ListReorder() {
    var items by remember { mutableStateOf(listOf("Apple", "Banana", "Cherry", "Date", "Elderberry")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 리스트 아이템 재정렬",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        |1. 아이템 목록을 LookaheadScope로 감싸세요
                        |2. 각 아이템에 key(item)를 추가하세요
                        |3. 아이템 Row에 animateBounds를 적용하세요
                        |4. "섞기" 버튼으로 애니메이션 확인
                    """.trimMargin(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 버튼들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { items = items.shuffled() }) {
                Text("섞기")
            }
            OutlinedButton(onClick = { items = items.sorted() }) {
                Text("정렬")
            }
            OutlinedButton(onClick = { items = items.reversed() }) {
                Text("역순")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 아래 Column을 LookaheadScope로 감싸고
        // 각 아이템에 key와 animateBounds를 적용하세요

        /* 정답:
        LookaheadScope {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { item ->
                    key(item) {  // 필수! 이게 없으면 재정렬 시 새 아이템으로 인식
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateBounds(this@LookaheadScope)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Row {
                                IconButton(onClick = {
                                    val currentIndex = items.indexOf(item)
                                    if (currentIndex > 0) {
                                        items = items.toMutableList().apply {
                                            val temp = this[currentIndex - 1]
                                            this[currentIndex - 1] = this[currentIndex]
                                            this[currentIndex] = temp
                                        }
                                    }
                                }) {
                                    Icon(Icons.Default.KeyboardArrowUp, "위로")
                                }
                                IconButton(onClick = {
                                    val currentIndex = items.indexOf(item)
                                    if (currentIndex < items.size - 1) {
                                        items = items.toMutableList().apply {
                                            val temp = this[currentIndex + 1]
                                            this[currentIndex + 1] = this[currentIndex]
                                            this[currentIndex] = temp
                                        }
                                    }
                                }) {
                                    Icon(Icons.Default.KeyboardArrowDown, "아래로")
                                }
                            }
                        }
                    }
                }
            }
        }
        */

        // 현재 구현 (애니메이션 없음)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                // TODO: key(item) { } 로 감싸기
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        // TODO: .animateBounds(this@LookaheadScope) 추가
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row {
                        IconButton(onClick = {
                            val currentIndex = items.indexOf(item)
                            if (currentIndex > 0) {
                                items = items.toMutableList().apply {
                                    val temp = this[currentIndex - 1]
                                    this[currentIndex - 1] = this[currentIndex]
                                    this[currentIndex] = temp
                                }
                            }
                        }) {
                            Icon(Icons.Default.KeyboardArrowUp, "위로")
                        }
                        IconButton(onClick = {
                            val currentIndex = items.indexOf(item)
                            if (currentIndex < items.size - 1) {
                                items = items.toMutableList().apply {
                                    val temp = this[currentIndex + 1]
                                    this[currentIndex + 1] = this[currentIndex]
                                    this[currentIndex] = temp
                                }
                            }
                        }) {
                            Icon(Icons.Default.KeyboardArrowDown, "아래로")
                        }
                    }
                }
            }
        }
    }
}
