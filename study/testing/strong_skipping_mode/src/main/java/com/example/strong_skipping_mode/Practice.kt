package com.example.strong_skipping_mode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ============================================================
// 연습 1: Stable/Unstable 타입 구분하기 (쉬움)
// ============================================================

/**
 * 연습 1: Stable/Unstable 타입 구분하기
 *
 * 목표: 어떤 타입이 Stable이고 어떤 타입이 Unstable인지 구분할 수 있다.
 *
 * Stable 타입:
 * - primitive (Int, Float, Boolean 등)
 * - String
 * - Enum
 * - @Stable/@Immutable 어노테이션된 클래스
 *
 * Unstable 타입:
 * - List, Map, Set (표준 컬렉션)
 * - var 프로퍼티를 가진 클래스
 * - 외부 모듈의 클래스 (Compose가 확인 불가)
 * - 함수 타입 (Lambda)
 */
@Composable
fun Practice1_StableUnstableScreen() {
    var showAnswers by remember { mutableStateOf(false) }
    var selectedAnswers by remember { mutableStateOf(mapOf<Int, Boolean>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: Stable/Unstable 타입 구분하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 타입들이 Stable인지 Unstable인지 구분해보세요. " +
                           "Strong Skipping에서 Stable은 equals()로, Unstable은 ===로 비교됩니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- Primitive 타입과 String은 Stable")
                Text("- 표준 컬렉션(List, Map, Set)은 Unstable")
                Text("- val만 있는 data class도 외부 모듈이면 Unstable")
                Text("- Lambda/함수 타입은 Unstable")
            }
        }

        // 퀴즈 문제들
        val questions = listOf(
            TypeQuestion(0, "String", true, "불변 타입, equals()로 비교 가능"),
            TypeQuestion(1, "Int", true, "primitive 타입은 항상 Stable"),
            TypeQuestion(2, "List<String>", false, "표준 컬렉션은 Unstable"),
            TypeQuestion(3, "Map<String, Int>", false, "표준 컬렉션은 Unstable"),
            TypeQuestion(4, "data class User(val name: String)", true, "val만 있으면 Stable"),
            TypeQuestion(5, "class Counter(var count: Int)", false, "var 프로퍼티 -> Unstable"),
            TypeQuestion(6, "() -> Unit", false, "Lambda/함수 타입은 Unstable"),
            TypeQuestion(7, "enum class State { A, B }", true, "Enum은 Stable"),
        )

        questions.forEach { question ->
            TypeQuestionCard(
                question = question,
                selectedAnswer = selectedAnswers[question.id],
                showAnswer = showAnswers,
                onAnswerSelected = { isStable ->
                    selectedAnswers = selectedAnswers + (question.id to isStable)
                }
            )
        }

        // 정답 확인 버튼
        Button(
            onClick = { showAnswers = !showAnswers },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswers) "정답 숨기기" else "정답 확인하기")
        }

        // 채점 결과
        if (showAnswers) {
            val correctCount = questions.count { q ->
                selectedAnswers[q.id] == q.isStable
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (correctCount == questions.size)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$correctCount / ${questions.size} 정답!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class TypeQuestion(
    val id: Int,
    val typeName: String,
    val isStable: Boolean,
    val explanation: String
)

@Composable
private fun TypeQuestionCard(
    question: TypeQuestion,
    selectedAnswer: Boolean?,
    showAnswer: Boolean,
    onAnswerSelected: (Boolean) -> Unit
) {
    val isCorrect = selectedAnswer == question.isStable

    Card(
        colors = CardDefaults.cardColors(
            containerColor = when {
                showAnswer && selectedAnswer != null && isCorrect ->
                    MaterialTheme.colorScheme.primaryContainer
                showAnswer && selectedAnswer != null && !isCorrect ->
                    MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = question.typeName,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedAnswer == true,
                    onClick = { onAnswerSelected(true) },
                    label = { Text("Stable") },
                    leadingIcon = if (selectedAnswer == true) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
                FilterChip(
                    selected = selectedAnswer == false,
                    onClick = { onAnswerSelected(false) },
                    label = { Text("Unstable") },
                    leadingIcon = if (selectedAnswer == false) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
            }

            if (showAnswer) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCorrect || selectedAnswer == null)
                            Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCorrect || selectedAnswer == null)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "정답: ${if (question.isStable) "Stable" else "Unstable"} - ${question.explanation}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// ============================================================
// 연습 2: Recomposition 스킵 확인하기 (중간)
// ============================================================

/**
 * 연습 2: Recomposition 스킵 확인하기
 *
 * 목표: SideEffect를 사용하여 Recomposition 횟수를 카운트하고,
 *       Strong Skipping이 실제로 스킵하는지 확인한다.
 *
 * TODO: 아래 코드를 완성하여 Strong Skipping의 효과를 직접 확인해보세요.
 */
@Composable
fun Practice2_SkipVerificationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: Recomposition 스킵 확인하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SideEffect를 사용하여 Composable이 실제로 Recomposition되는지 확인해보세요. " +
                           "Strong Skipping이 동작하면 같은 인스턴스의 List는 스킵됩니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- SideEffect { count++ }로 Recomposition 추적 가능")
                Text("- remember { listOf(...) }로 인스턴스 유지")
                Text("- 상위 상태가 변해도 하위가 스킵되는지 확인")
            }
        }

        // TODO 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "TODO: 아래 코드를 완성하세요",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice2_Exercise()
            }
        }

        // 정답 카드
        Practice2_AnswerCard()
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 여기에 코드를 완성하세요
    // 1. 상위에 counter 상태 추가
    // 2. remember로 items 리스트 유지
    // 3. 하위 Composable에 SideEffect로 Recomposition 카운트
    // 4. 버튼으로 counter 증가 시 하위가 스킵되는지 확인

    var counter by remember { mutableIntStateOf(0) }
    // TODO: items 리스트를 remember로 생성하세요
    // val items = ...

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { counter++ }) {
            Text("카운터 증가: $counter")
        }

        Text(
            text = "TODO: 아래에 ItemList Composable을 추가하고\n" +
                   "SideEffect로 Recomposition 횟수를 표시하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // TODO: ItemList(items = items) 호출하세요
    }
}

@Composable
private fun Practice2_AnswerCard() {
    var showAnswer by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAnswer = !showAnswer },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "정답 보기",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (showAnswer) "접기" else "펼치기",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (showAnswer) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
@Composable
fun Practice2_Answer() {
    var counter by remember { mutableIntStateOf(0) }

    // remember로 인스턴스 유지!
    val items = remember {
        listOf("아이템 1", "아이템 2", "아이템 3")
    }

    Column {
        Button(onClick = { counter++ }) {
            Text("카운터: ${"$"}counter")
        }

        // Strong Skipping: items가 같은 인스턴스면 스킵!
        ItemList(items = items)
    }
}

@Composable
fun ItemList(items: List<String>) {
    var recomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++  // Recomposition마다 증가
    }

    Column {
        Text("Recomposition: ${"$"}recomposeCount회")
        items.forEach { item ->
            Text(item)
        }
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

// ============================================================
// 연습 3: Lambda 메모이제이션 이해하기 (어려움)
// ============================================================

/**
 * 연습 3: Lambda 메모이제이션 이해하기
 *
 * 목표: Lambda가 외부 상태를 캡처할 때 Strong Skipping이 어떻게 처리하는지 이해한다.
 *
 * Strong Skipping은 Lambda를 자동으로 remember 처리합니다.
 * 하지만 Lambda가 캡처하는 값이 변경되면 새로운 Lambda가 생성됩니다.
 */
@Composable
fun Practice3_LambdaMemoizationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: Lambda 메모이제이션 이해하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lambda가 외부 값을 캡처하면 그 값이 변경될 때 Lambda도 새로 생성됩니다. " +
                           "Strong Skipping이 Lambda를 어떻게 처리하는지 분석해보세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- Lambda가 캡처한 값이 같으면 같은 Lambda로 취급")
                Text("- Unstable 값을 캡처하면 인스턴스(===)로 비교")
                Text("- Stable 값을 캡처하면 equals()로 비교")
                Text("- @DontMemoize로 메모이제이션 비활성화 가능")
            }
        }

        // 분석 문제
        Practice3_AnalysisCard()

        // TODO 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "TODO: Lambda 메모이제이션 분석",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice3_Exercise()
            }
        }

        // 정답 카드
        Practice3_AnswerCard()
    }
}

@Composable
private fun Practice3_AnalysisCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "분석 문제: 아래 코드에서 Lambda가 어떻게 처리될까요?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
@Composable
fun Example() {
    var count by remember { mutableIntStateOf(0) }
    val items = remember { listOf("A", "B", "C") }

    // Lambda 1: count를 캡처
    val onClick1 = { println(count) }

    // Lambda 2: items를 캡처
    val onClick2 = { println(items.size) }

    Button(onClick = { count++ }) {
        Text("Count: ${"$"}count")
    }

    // count가 변경되면 onClick1은 어떻게 될까요?
    // items가 변경되지 않으면 onClick2는 어떻게 될까요?
}
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    var count by remember { mutableIntStateOf(0) }
    val items = remember { listOf("아이템 A", "아이템 B", "아이템 C") }

    // TODO: 아래 질문에 대한 답을 생각해보세요
    // 1. count가 변경되면 onClick1은 새로 생성될까요?
    // 2. items가 같은 인스턴스면 onClick2는 같은 인스턴스일까요?
    // 3. @DontMemoize를 사용하면 어떻게 달라질까요?

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { count++ }) {
            Text("Count 증가: $count")
        }

        Text(
            text = "TODO: 위 분석 문제의 답을 아래 정답에서 확인하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 현재 상태 표시
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.small
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("현재 count: $count", style = MaterialTheme.typography.bodySmall)
                Text("items 크기: ${items.size}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun Practice3_AnswerCard() {
    var showAnswer by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAnswer = !showAnswer },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "정답 및 해설",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (showAnswer) "접기" else "펼치기",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (showAnswer) {
                Spacer(modifier = Modifier.height(12.dp))

                // 답변 1
                AnswerItem(
                    question = "Q1: count가 변경되면 onClick1은 새로 생성될까요?",
                    answer = "예, 새로 생성됩니다!",
                    explanation = "onClick1은 count를 캡처합니다. count가 Stable 타입(Int)이지만 " +
                                  "값이 변경되면(0 -> 1) equals()가 false이므로 새 Lambda가 생성됩니다."
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 답변 2
                AnswerItem(
                    question = "Q2: items가 같은 인스턴스면 onClick2는 같은 인스턴스일까요?",
                    answer = "예, 같은 인스턴스입니다!",
                    explanation = "onClick2는 items를 캡처합니다. items가 Unstable 타입(List)이지만 " +
                                  "인스턴스가 같으면(===) Strong Skipping이 같은 Lambda를 재사용합니다."
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 답변 3
                AnswerItem(
                    question = "Q3: @DontMemoize를 사용하면 어떻게 달라질까요?",
                    answer = "매번 새 Lambda가 생성됩니다!",
                    explanation = "@DontMemoize를 붙이면 Strong Skipping의 Lambda 메모이제이션이 " +
                                  "비활성화되어, 매 Recomposition마다 새 Lambda가 생성됩니다. " +
                                  "특별한 이유가 없으면 사용하지 않는 것이 좋습니다."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 정리
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "정리",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Strong Skipping의 Lambda 메모이제이션:\n" +
                                   "- Stable 캡처: 값이 같으면(equals) 재사용\n" +
                                   "- Unstable 캡처: 인스턴스가 같으면(===) 재사용\n" +
                                   "- @DontMemoize: 메모이제이션 비활성화",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerItem(
    question: String,
    answer: String,
    explanation: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = question,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = answer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
