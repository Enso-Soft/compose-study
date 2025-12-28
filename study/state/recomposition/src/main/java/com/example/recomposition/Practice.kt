package com.example.recomposition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * 연습 문제: Recomposition 최적화 실습
 *
 * 1. Recomposition 추적기: SideEffect로 Recomposition 횟수 추적
 * 2. 컴포넌트 분리 최적화: 상태 읽기를 격리하여 최적화
 * 3. LazyList 최적화: key를 사용하여 리스트 최적화
 */

/**
 * 연습 문제 1: Recomposition 추적기 만들기
 *
 * 목표: SideEffect를 사용하여 Recomposition 횟수를 추적하는 컴포넌트 만들기
 * 시나리오: 각 컴포넌트가 몇 번 Recompose되었는지 시각화
 *
 * TODO: RecompositionTracker Composable을 완성하세요
 */
@Composable
fun Practice1_RecompositionTracker() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: Recomposition 추적기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("SideEffect를 사용하여 Recomposition 횟수를 추적합니다.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: SideEffect { recompositionCount++ }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 카운터 컨트롤
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { count++ }) {
                        Text("+1")
                    }
                    OutlinedButton(onClick = { count = 0 }) {
                        Text("Reset")
                    }
                }
            }
        }

        // TODO: 아래 RecompositionTracker를 완성하세요
        RecompositionTracker("Component A")
        RecompositionTracker("Component B")
        RecompositionTracker("Component C")

        // 완성 확인
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "확인해보세요:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. +1 버튼을 누르면 모든 Tracker의 카운트가 증가합니다.\n" +
                            "2. 이것이 바로 '불필요한 Recomposition' 문제입니다!\n" +
                            "3. 다음 연습에서 이를 최적화해봅니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Recomposition 추적 컴포넌트
 *
 * TODO: SideEffect를 사용하여 recompositionCount를 증가시키세요
 */
@Composable
fun RecompositionTracker(name: String) {
    // === 정답 ===
    var recompositionCount by remember { mutableIntStateOf(0) }

    // TODO: SideEffect 블록을 추가하여 recompositionCount를 증가시키세요
    // 힌트: SideEffect { recompositionCount++ }
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3E5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Recomposition: $recompositionCount",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 연습 문제 2: 컴포넌트 분리로 최적화
 *
 * 목표: 상태 읽기를 별도 Composable로 분리하여 최적화
 * 시나리오: 부모의 상태 변경이 자식에게 영향을 주지 않도록 수정
 *
 * TODO: OptimizedParent와 CounterDisplay를 분리하세요
 */
@Composable
fun Practice2_OptimizeParent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 컴포넌트 분리 최적화",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("상태 읽기를 별도 Composable로 분리하여 다른 컴포넌트가 스킵되도록 합니다.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: CountDisplay(count) 형태로 분리",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 최적화된 부모 컴포넌트
        OptimizedParent()

        // 코드 힌트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "최적화 전:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Text(
                    text = """
@Composable
fun Parent() {
    var count by remember { mutableStateOf(0) }
    Column {
        Text("Count: ${'$'}count") // 부모에서 읽음
        Child() // 불필요하게 recompose
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "최적화 후:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = """
@Composable
fun Parent() {
    var count by remember { mutableStateOf(0) }
    Column {
        CountDisplay(count) // 격리!
        Child() // 스킵됨
    }
}

@Composable
fun CountDisplay(count: Int) {
    Text("Count: ${'$'}count")
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

/**
 * 최적화된 부모 컴포넌트
 */
@Composable
fun OptimizedParent() {
    var count by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "최적화된 구조",
                fontWeight = FontWeight.Bold
            )

            // 상태를 읽는 부분 (격리됨)
            IsolatedCounter(count = count, onIncrement = { count++ })

            // 자식 컴포넌트들 (스킵됨)
            OptimizedChild("Child A")
            OptimizedChild("Child B")
            OptimizedChild("Child C")
        }
    }
}

@Composable
fun IsolatedCounter(count: Int, onIncrement: () -> Unit) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Count: $count",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "이 부분만 Recompose: $recompositionCount",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onIncrement) {
                Text("+1")
            }
        }
    }
}

@Composable
fun OptimizedChild(name: String) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name)
        Text(
            text = "Recomposition: $recompositionCount (스킵됨!)",
            color = Color(0xFF2E7D32),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * 연습 문제 3: LazyList 최적화
 *
 * 목표: key를 사용하여 LazyColumn을 최적화
 * 시나리오: 아이템 순서가 변경되어도 기존 아이템이 재사용되도록
 *
 * TODO: key 파라미터를 추가하여 최적화하세요
 */
@Composable
fun Practice3_LazyListOptimization() {
    var items by remember {
        mutableStateOf(
            listOf(
                ListItem("1", "Apple"),
                ListItem("2", "Banana"),
                ListItem("3", "Cherry"),
                ListItem("4", "Date"),
                ListItem("5", "Elderberry")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: LazyList 최적화",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("key 파라미터를 사용하여 아이템 순서가 변경되어도 기존 아이템이 재사용되도록 합니다.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: items(items, key = { it.id }) { ... }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 컨트롤 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                items = items.shuffled()
            }) {
                Text("Shuffle")
            }
            Button(onClick = {
                val newId = (items.size + 1).toString()
                val fruits = listOf("Fig", "Grape", "Honeydew", "Kiwi", "Lemon", "Mango")
                items = items + ListItem(newId, fruits.random())
            }) {
                Text("Add")
            }
            OutlinedButton(onClick = {
                items = listOf(
                    ListItem("1", "Apple"),
                    ListItem("2", "Banana"),
                    ListItem("3", "Cherry"),
                    ListItem("4", "Date"),
                    ListItem("5", "Elderberry")
                )
            }) {
                Text("Reset")
            }
        }

        // 리스트
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // TODO: key 파라미터 추가
            // 힌트: items(items, key = { it.id }) { item -> ... }
            items(items, key = { it.id }) { item ->
                ListItemCard(
                    item = item,
                    onDelete = { items = items.filter { it.id != item.id } }
                )
            }
        }

        // 결과 확인
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "확인해보세요:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. Shuffle 버튼을 눌러보세요.\n" +
                            "2. key가 있으면: 아이템 순서만 바뀌고, 각 아이템의 Recomposition 카운터는 유지됩니다.\n" +
                            "3. key가 없으면: 모든 아이템이 새로 그려집니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

data class ListItem(
    val id: String,
    val name: String
)

@Composable
fun ListItemCard(item: ListItem, onDelete: () -> Unit) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    // 랜덤 색상 (최초 composition 시에만 생성)
    val backgroundColor = remember {
        Color(
            red = Random.nextFloat() * 0.3f + 0.7f,
            green = Random.nextFloat() * 0.3f + 0.7f,
            blue = Random.nextFloat() * 0.3f + 0.7f
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${item.name} (ID: ${item.id})",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Recomposition: $recompositionCount",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
