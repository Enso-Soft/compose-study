package com.example.kotlin_basics

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
import androidx.compose.ui.unit.sp

/**
 * Problem: Kotlin 문법을 모르면 Compose 코드가 읽히지 않는다
 *
 * 아래 코드들은 Kotlin 문법을 이해하지 못했을 때
 * 어떻게 "이상하게" 보이는지 보여줍니다.
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: Compose 코드가 외계어로 보인다",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kotlin 핵심 문법을 모르면 Compose 코드를 읽을 수 없습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 1: 람다를 모르면
        ProblemSection(
            title = "문제 1: 람다 표현식을 모르면",
            confusingCode = """
Button(onClick = { count++ }) {
    Text("클릭")
}
            """.trimIndent(),
            confusion = "{ count++ }가 뭐지? 왜 중괄호 안에 코드가 있지?"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 2: 확장 함수를 모르면
        ProblemSection(
            title = "문제 2: 확장 함수를 모르면",
            confusingCode = """
Modifier
    .padding(16.dp)
    .background(Color.Red)
    .fillMaxWidth()
            """.trimIndent(),
            confusion = "Modifier에 padding이라는 메서드가 있나? 왜 점(.)으로 계속 연결되지?"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 3: 후행 람다를 모르면
        ProblemSection(
            title = "문제 3: 후행 람다를 모르면",
            confusingCode = """
Column {
    Text("Hello")
    Text("World")
}
            """.trimIndent(),
            confusion = "Column() 괄호는 어디 갔지? 왜 바로 중괄호가 나오지? 문법 오류 아닌가?"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 4: 널 안전성을 모르면
        ProblemSection(
            title = "문제 4: 널 안전성을 모르면",
            confusingCode = """
user?.let { userData ->
    Text(userData.name)
} ?: Text("로그인 필요")
            """.trimIndent(),
            confusion = "?.let이 뭐지? ?: 는 또 뭐지? 이게 어떻게 동작하는 거야?"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 인터랙티브 데모
        InteractiveConfusionDemo()
    }
}

@Composable
private fun ProblemSection(
    title: String,
    confusingCode: String,
    confusion: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 코드 블록
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2D2D2D),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = confusingCode,
                    modifier = Modifier.padding(12.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    color = Color(0xFFE0E0E0)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 혼란 포인트
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "???",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = confusion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InteractiveConfusionDemo() {
    var showAnswer by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "퀴즈: 이 코드의 의미는?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 퀴즈 코드
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2D2D2D),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
items.filter { it.isActive }
     .map { it.name }
     .forEach { println(it) }
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    color = Color(0xFFE0E0E0)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { showAnswer = !showAnswer },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showAnswer) "숨기기" else "정답 보기")
            }

            if (showAnswer) {
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "정답:",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("1. filter { it.isActive }")
                        Text("   → 람다: isActive가 true인 항목만 필터링")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("2. map { it.name }")
                        Text("   → 람다: 각 항목을 name으로 변환")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("3. forEach { println(it) }")
                        Text("   → 람다: 각 name을 출력")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "핵심: 'it'은 람다의 단일 파라미터를 자동 참조!",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
