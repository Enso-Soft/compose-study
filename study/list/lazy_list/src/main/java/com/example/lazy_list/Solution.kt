package com.example.lazy_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Solution: LazyColumn/LazyRow로 효율적으로 리스트 표시
 *
 * 5개의 데모를 통해 Lazy 레이아웃의 다양한 사용법을 학습합니다.
 */
@Composable
fun SolutionScreen() {
    var currentDemo by remember { mutableStateOf(Demo.BASIC) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 데모 선택 탭
        ScrollableTabRow(
            selectedTabIndex = currentDemo.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            Demo.entries.forEach { demo ->
                Tab(
                    selected = currentDemo == demo,
                    onClick = { currentDemo = demo },
                    text = { Text(demo.title) }
                )
            }
        }

        // 선택된 데모 표시
        when (currentDemo) {
            Demo.BASIC -> BasicLazyColumnDemo()
            Demo.ITEMS_TYPES -> ItemTypesDemo()
            Demo.LAZY_ROW -> LazyRowDemo()
            Demo.ARRANGEMENT -> ArrangementDemo()
            Demo.GRID -> GridDemo()
        }
    }
}

private enum class Demo(val title: String) {
    BASIC("기본"),
    ITEMS_TYPES("items 종류"),
    LAZY_ROW("LazyRow"),
    ARRANGEMENT("간격 설정"),
    GRID("Grid")
}

// =============================================================================
// 데모 1: 기본 LazyColumn
// =============================================================================

@Composable
private fun BasicLazyColumnDemo() {
    var composedCount by remember { mutableIntStateOf(0) }
    val users = remember { (1..100).map { User(it, "사용자 $it") } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                        text = "해결: LazyColumn 사용",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "LazyColumn은 화면에 보이는 항목만 그립니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 통계 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "화면에 그려진 아이템 수",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "$composedCount / ${users.size}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "스크롤하면 필요한 만큼만 추가로 그려집니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn - 화면에 보이는 항목만 그림!
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                // 각 아이템이 그려질 때 카운터 증가
                LaunchedEffect(user.id) {
                    composedCount++
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "ID: ${user.id}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// 데모 2: items 종류 (item, items, itemsIndexed)
// =============================================================================

@Composable
private fun ItemTypesDemo() {
    val fruits = listOf("사과", "바나나", "체리", "포도", "오렌지")
    val vegetables = listOf("당근", "브로콜리", "시금치")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "item(), items(), itemsIndexed() 사용법",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // item() - 단일 항목 (헤더)
            item {
                Text(
                    text = "과일 목록",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp)
                )
            }

            // items(list) - 리스트 기반 항목
            items(fruits) { fruit ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = fruit,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // item() - 또 다른 헤더
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "채소 목록 (번호 포함)",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(12.dp)
                )
            }

            // itemsIndexed() - 인덱스와 함께 항목
            itemsIndexed(vegetables) { index, vegetable ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${index + 1}. $vegetable",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // items(count) - 숫자 기반 항목
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "숫자 기반 항목 (items(3))",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(12.dp)
                )
            }

            items(3) { index ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "인덱스 기반 아이템: $index",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // 푸터
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "--- 목록 끝 ---",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

// =============================================================================
// 데모 3: LazyRow (수평 스크롤)
// =============================================================================

@Composable
private fun LazyRowDemo() {
    val categories = listOf("전체", "음식", "카페", "쇼핑", "문화", "스포츠", "여행", "교육")
    var selectedCategory by remember { mutableStateOf("전체") }

    val items = remember(selectedCategory) {
        when (selectedCategory) {
            "전체" -> (1..20).map { "아이템 $it" }
            else -> (1..10).map { "$selectedCategory 아이템 $it" }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "LazyRow - 수평 스크롤",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "카테고리 필터 (좌우로 스크롤)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // LazyRow로 카테고리 필터 표시
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    selected = selectedCategory == category
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 선택된 카테고리의 아이템 표시
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

// =============================================================================
// 데모 4: contentPadding, Arrangement (간격 설정)
// =============================================================================

@Composable
private fun ArrangementDemo() {
    val items = (1..20).map { "카드 $it" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "contentPadding & Arrangement",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = """
                contentPadding: 콘텐츠 주변 여백
                Arrangement.spacedBy: 항목 간 간격
            """.trimIndent(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // contentPadding과 Arrangement 적용
        LazyColumn(
            modifier = Modifier.weight(1f),
            // 콘텐츠 주변에 16dp 여백 (LazyColumn 자체가 아닌 콘텐츠에 적용)
            contentPadding = PaddingValues(16.dp),
            // 항목 간 8dp 간격
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

// =============================================================================
// 데모 5: LazyVerticalGrid (그리드)
// =============================================================================

@Composable
private fun GridDemo() {
    val photos = (1..20).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "LazyVerticalGrid - 그리드 레이아웃",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "GridCells.Fixed(2): 2열 고정",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2열 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(photos) { index ->
                Card(
                    modifier = Modifier.aspectRatio(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                                text = "Photo",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "$index",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}
