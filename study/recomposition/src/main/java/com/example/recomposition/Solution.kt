package com.example.recomposition

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * Solution: Recomposition 최적화 기법
 *
 * 이 화면에서는 Recomposition을 최적화하는 다양한 기법을 보여줍니다:
 * 1. 상태 읽기 격리: 상태를 읽는 부분을 별도 Composable로 분리
 * 2. Stable 파라미터: @Immutable, @Stable 어노테이션 활용
 * 3. Lambda 캐싱: remember로 람다 캐싱
 * 4. 상태 읽기 지연: Layout 단계로 읽기 지연
 */

@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("상태 격리") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("Lambda 캐싱") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("읽기 지연") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> StateIsolationDemo()
            1 -> LambdaCachingDemo()
            2 -> DeferredReadDemo()
        }
    }
}

/**
 * 데모 1: 상태 읽기 격리
 *
 * 상태를 읽는 부분을 별도 Composable로 분리하여
 * 다른 컴포넌트들이 불필요하게 Recompose되지 않도록 합니다.
 */
@Composable
fun StateIsolationDemo() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: 상태 읽기 격리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "상태를 읽는 부분을 별도 Composable로 분리하면,\n" +
                            "해당 Composable만 Recompose됩니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 카운터 컨트롤 (상태 변경만)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "카운터 컨트롤",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { count++ }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+1")
                    }
                    OutlinedButton(onClick = { count = 0 }) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset")
                    }
                }
            }
        }

        // 상태를 읽는 부분 (격리됨)
        IsolatedCountDisplay(count)

        // 솔루션 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "솔루션 코드:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
@Composable
fun Parent() {
    var count by remember { mutableStateOf(0) }

    Column {
        CountDisplay(count) // 상태 읽기 격리!
        ExpensiveComponent() // 스킵됨
    }
}

@Composable
fun CountDisplay(count: Int) {
    Text("Count: ${'$'}count") // 여기만 recompose
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 최적화된 컴포넌트들
        Text(
            text = "아래 컴포넌트들은 count가 변해도 스킵됩니다:",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        ExpensiveComponentGood("ExpensiveComponent 1")
        ExpensiveComponentGood("ExpensiveComponent 2")
        StaticComponentGood()

        // 결과 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "결과:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. CountDisplay만 Recompose됩니다.\n" +
                            "2. ExpensiveComponent들은 초기 렌더링 후 스킵됩니다.\n" +
                            "3. Recomposition 횟수가 증가하지 않는 것을 확인하세요!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 상태 읽기가 격리된 카운터 디스플레이
 */
@Composable
fun IsolatedCountDisplay(count: Int) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

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
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "이 컴포넌트만 Recompose: $recompositionCount",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF1565C0)
            )
        }
    }
}

/**
 * 최적화된 비용이 큰 컴포넌트
 * 부모의 상태 변경에 영향받지 않음
 */
@Composable
fun ExpensiveComponentGood(name: String) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
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
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 최적화된 정적 컴포넌트
 */
@Composable
fun StaticComponentGood() {
    var recompositionCount by remember { mutableIntStateOf(0) }
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
                text = "StaticComponent",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Recomposition: $recompositionCount",
                color = Color(0xFF7B1FA2),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 데모 2: Lambda 캐싱
 *
 * remember를 사용하여 람다를 캐싱하면
 * 자식 컴포넌트가 불필요하게 Recompose되지 않습니다.
 */
@Composable
fun LambdaCachingDemo() {
    var count by remember { mutableIntStateOf(0) }

    // 캐싱된 람다
    val onClickCached = remember { { println("Cached click!") } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: Lambda 캐싱",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "람다를 remember로 캐싱하면 동일한 참조가 유지되어\n" +
                            "자식 컴포넌트가 스킵될 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 카운터
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
                Button(onClick = { count++ }) {
                    Text("+1 (부모 상태 변경)")
                }
            }
        }

        // 비교: 캐싱 안 된 람다 vs 캐싱된 람다
        Text(
            text = "캐싱 안 된 람다:",
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        // 매번 새 람다 생성 (문제)
        ChildWithCallback(
            name = "UncachedChild",
            onClick = { println("Uncached click!") }, // 매번 새 람다!
            isOptimized = false
        )

        Text(
            text = "캐싱된 람다:",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        // 캐싱된 람다 사용 (최적화)
        ChildWithCallback(
            name = "CachedChild",
            onClick = onClickCached, // 캐싱된 람다
            isOptimized = true
        )

        // 코드 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "코드 비교:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "// 문제: 매번 새 람다\nChild(onClick = { doSomething() })",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "// 해결: 람다 캐싱\nval onClick = remember { { doSomething() } }\nChild(onClick = onClick)",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF2E7D32)
                )
            }
        }

        // 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "참고:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Compose 컴파일러가 자동으로 람다를 캐싱하는 경우도 있습니다.\n" +
                            "하지만 외부 상태를 캡처하는 람다는 수동 캐싱이 필요할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun ChildWithCallback(
    name: String,
    onClick: () -> Unit,
    isOptimized: Boolean
) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isOptimized) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isOptimized) "캐싱된 람다" else "매번 새 람다 생성",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = "Recomposition: $recompositionCount",
                color = if (isOptimized) Color(0xFF2E7D32) else Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 데모 3: 상태 읽기 지연 (Defer State Reads)
 *
 * 자주 변경되는 상태를 Layout 단계로 지연하여 읽으면
 * Composition 단계의 Recomposition을 피할 수 있습니다.
 */
@Composable
fun DeferredReadDemo() {
    var sliderValue by remember { mutableFloatStateOf(0f) }
    val animatedOffset by animateFloatAsState(
        targetValue = sliderValue * 200,
        label = "offset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: 상태 읽기 지연",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "자주 변경되는 상태(애니메이션 등)는\n" +
                            "람다를 사용하여 Layout 단계로 읽기를 지연할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 슬라이더
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("오프셋 조절: ${(sliderValue * 100).toInt()}%")
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 나쁜 예시
        Text(
            text = "나쁜 예: Composition 단계에서 읽기",
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        DeferredReadBadExample(animatedOffset)

        // 좋은 예시
        Text(
            text = "좋은 예: Layout 단계로 읽기 지연",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        DeferredReadGoodExample(animatedOffset)

        // 코드 비교
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "코드 비교:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "// 나쁜 예: 값으로 직접 읽기\nModifier.offset(x = offset.dp)",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "// 좋은 예: 람다로 읽기 지연\nModifier.offset { IntOffset(offset.toInt(), 0) }",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF2E7D32)
                )
            }
        }

        // 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "왜 다른가요?",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "람다 버전은 상태 읽기를 Layout 단계로 지연합니다.\n" +
                            "Composition이 아닌 Layout만 다시 실행되어 더 효율적입니다.\n\n" +
                            "슬라이더를 움직여보세요. 람다 버전의 Recomposition 횟수가\n" +
                            "더 적게 증가하는 것을 확인할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun DeferredReadBadExample(offset: Float) {
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
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recomposition: $recompositionCount", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .offset(x = offset.dp) // 값으로 직접 읽기!
                    .size(50.dp)
                    .background(Color.Red)
            )
        }
    }
}

@Composable
fun DeferredReadGoodExample(offset: Float) {
    var recompositionCount by remember { mutableIntStateOf(0) }
    SideEffect {
        recompositionCount++
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Recomposition: $recompositionCount", color = Color(0xFF2E7D32))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .offset { IntOffset(offset.toInt(), 0) } // 람다로 읽기 지연!
                    .size(50.dp)
                    .background(Color(0xFF4CAF50))
            )
        }
    }
}
