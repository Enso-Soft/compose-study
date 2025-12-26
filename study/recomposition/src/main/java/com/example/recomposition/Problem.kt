package com.example.recomposition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * Problem: Recomposition을 이해하지 못했을 때 발생하는 문제들
 *
 * 이 화면에서는 Recomposition 최적화가 되지 않았을 때 발생하는 문제를 보여줍니다:
 * 1. 불필요한 Recomposition: 상태와 무관한 컴포넌트도 다시 실행
 * 2. Unstable 파라미터: 동일한 데이터인데도 매번 Recomposition
 * 3. Lambda 문제: 매번 새 람다가 생성되어 자식 컴포넌트가 Recomposition
 */

@Composable
fun ProblemScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Recomposition을 이해하지 못하면 성능 문제가 발생합니다.\n" +
                            "아래 데모에서 Recomposition 횟수를 확인해보세요.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("불필요한 Recomposition") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("Scope 문제") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> UnnecessaryRecompositionDemo()
            1 -> RecompositionScopeDemo()
        }
    }
}

/**
 * 데모 1: 불필요한 Recomposition
 *
 * count 상태가 변경되면 관련 없는 ExpensiveComponent도 매번 다시 실행됩니다.
 */
@Composable
fun UnnecessaryRecompositionDemo() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 카운터 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "카운터: $count",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { count++ }) {
                    Text("+1")
                }
            }
        }

        // 문제 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "문제 코드:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
Column {
    Text("Count: ${'$'}count")
    ExpensiveComponent() // count와 무관!
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 비용이 큰 컴포넌트들 (문제 상황)
        Text(
            text = "아래 컴포넌트들은 count와 무관한데도 매번 Recompose됩니다:",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        // 비용이 큰 컴포넌트 1
        ExpensiveComponentBad("ExpensiveComponent 1")

        // 비용이 큰 컴포넌트 2
        ExpensiveComponentBad("ExpensiveComponent 2")

        // 정적 컴포넌트
        StaticComponentBad()

        // 결과 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "관찰해보세요:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. +1 버튼을 누를 때마다 모든 Recomposition 카운터가 증가합니다.\n" +
                            "2. ExpensiveComponent는 count와 무관한데도 매번 다시 실행됩니다.\n" +
                            "3. 랜덤 색상이 매번 바뀌는 것으로 Recomposition을 시각적으로 확인할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 비용이 큰 컴포넌트 (최적화 안 됨)
 * 매번 Recomposition될 때마다 랜덤 색상으로 변경되어 시각적으로 확인 가능
 */
@Composable
fun ExpensiveComponentBad(name: String) {
    // Recomposition 횟수 추적
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    // 매 Recomposition마다 새로운 랜덤 색상
    val randomColor = remember(recompositionCount) {
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 0.3f
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = randomColor
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Recomposition: $recompositionCount",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "(count와 무관한데 매번 Recompose됨)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

/**
 * 정적 컴포넌트 (최적화 안 됨)
 */
@Composable
fun StaticComponentBad() {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "StaticComponent",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Recomposition: $recompositionCount",
                    color = Color(0xFFE65100),
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "완전히 정적인 내용인데도 Recompose됨",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

/**
 * 데모 2: Recomposition Scope 문제
 *
 * 상태를 읽는 위치에 따라 Recomposition 범위가 달라집니다.
 */
@Composable
fun RecompositionScopeDemo() {
    var parentRecomposeCount by remember { mutableIntStateOf(0) }
    var count by remember { mutableIntStateOf(0) }

    // 부모의 Recomposition 추적
    SideEffect {
        parentRecomposeCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 부모 Recomposition 카운터
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
                    text = "Parent Recomposition: $parentRecomposeCount",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "현재 count: $count", // 여기서 count를 읽음!
                    style = MaterialTheme.typography.headlineSmall
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

        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "문제:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "부모 Composable에서 count를 읽으면 전체 부모가 Recomposition 범위가 됩니다.\n" +
                            "아래 모든 자식 컴포넌트들도 함께 Recompose됩니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 자식 컴포넌트들 (모두 불필요하게 Recompose됨)
        Text(
            text = "자식 컴포넌트들 (모두 불필요하게 Recompose):",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        ChildComponentBad("Child 1")
        ChildComponentBad("Child 2")
        ChildComponentBad("Child 3")

        // 코드 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "문제 코드:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
@Composable
fun Parent() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: ${'$'}count") // 부모에서 읽음
        Child1() // 전부 recompose
        Child2() // 전부 recompose
        Child3() // 전부 recompose
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun ChildComponentBad(name: String) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name)
            Text(
                text = "Recomposition: $recompositionCount",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
