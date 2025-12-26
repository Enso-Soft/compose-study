package com.example.kotlin_basics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
 * Solution: 4가지 핵심 Kotlin 문법 이해하기
 *
 * 1. 람다 표현식 - 익명 함수를 간결하게
 * 2. 확장 함수 - 기존 클래스에 함수 추가
 * 3. 후행 람다 - DSL 스타일 코드
 * 4. 널 안전성 - NPE 방지
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
                    text = "해결책: 4가지 핵심 문법 마스터",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "이 4가지만 알면 모든 Compose 코드를 읽을 수 있습니다!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 1. 람다 표현식
        LambdaExplanation()

        Spacer(modifier = Modifier.height(16.dp))

        // 2. 확장 함수
        ExtensionFunctionExplanation()

        Spacer(modifier = Modifier.height(16.dp))

        // 3. 후행 람다
        TrailingLambdaExplanation()

        Spacer(modifier = Modifier.height(16.dp))

        // 4. 널 안전성
        NullSafetyExplanation()

        Spacer(modifier = Modifier.height(24.dp))

        // 종합 인터랙티브 예제
        ComprehensiveDemo()
    }
}

@Composable
private fun LambdaExplanation() {
    var count by remember { mutableIntStateOf(0) }

    SolutionCard(
        title = "1. 람다 표현식 (Lambda)",
        explanation = "익명 함수를 { } 안에 간결하게 표현"
    ) {
        // 코드 예시
        CodeBlock(
            code = """
// 기본 형태
val add = { a: Int, b: Int -> a + b }

// 파라미터가 하나면 'it' 사용
val double = { it * 2 }

// Compose에서
Button(onClick = { count++ })
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 인터랙티브 예제
        Text(
            text = "직접 해보기:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { count++ }  // 람다!
            ) {
                Text("count++")
            }

            Text(
                text = "count = $count",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = { count = 0 }  // 람다!
            ) {
                Text("리셋")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "onClick = { count++ } 에서 { count++ }가 람다입니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ExtensionFunctionExplanation() {
    SolutionCard(
        title = "2. 확장 함수 (Extension Function)",
        explanation = "기존 클래스에 새로운 함수를 추가"
    ) {
        CodeBlock(
            code = """
// 정의: fun 타입.함수명()
fun String.exclaim() = this + "!"

"Hello".exclaim()  // "Hello!"

// Modifier 체이닝의 비밀
Modifier
    .padding(16.dp)     // 확장 함수
    .background(Red)    // 확장 함수
    .fillMaxWidth()     // 확장 함수
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "직접 해보기:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Modifier 체이닝 시각화
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Modifier 없음",
                modifier = Modifier
            )

            Text(
                text = ".padding(8.dp)",
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.LightGray)
            )

            Text(
                text = ".padding(8.dp).background(Blue)",
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(0xFF64B5F6))
            )

            Text(
                text = ".padding(16.dp).background(Green).border(2.dp, Red)",
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFF81C784))
                    .border(2.dp, Color.Red)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "각 함수가 새 Modifier를 반환하여 체이닝이 가능!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TrailingLambdaExplanation() {
    SolutionCard(
        title = "3. 후행 람다 (Trailing Lambda)",
        explanation = "마지막 파라미터가 람다면 괄호 밖으로"
    ) {
        CodeBlock(
            code = """
// 원래 형태
Column(content = { Text("Hello") })

// 후행 람다 적용
Column { Text("Hello") }

// 다른 파라미터가 있을 때
Button(
    onClick = { /* 클릭 */ }
) {
    Text("버튼")  // 이것도 후행 람다!
}
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "직접 해보기:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 실제 후행 람다 예제
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Column 후행 람다
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            ) {
                Column(  // 후행 람다!
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text("Column {")
                    Text("  Text(1)")
                    Text("  Text(2)")
                    Text("}")
                }
            }

            // Row 후행 람다
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFCE4EC)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Row {")
                    Row {
                        Text(" A ")
                        Text(" B ")
                    }
                    Text("}")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Column { }, Row { }, Box { } 모두 후행 람다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun NullSafetyExplanation() {
    var showUser by remember { mutableStateOf(true) }

    // 시뮬레이션: nullable user
    val userName: String? = if (showUser) "홍길동" else null

    SolutionCard(
        title = "4. 널 안전성 (Null Safety)",
        explanation = "NPE 없이 안전하게 null 처리"
    ) {
        CodeBlock(
            code = """
val name: String? = null  // nullable

// 안전 호출
name?.length     // null 반환

// Elvis 연산자
name ?: "기본값"  // null이면 기본값

// let 스코프 함수
name?.let {
    Text(it)     // null 아닐 때만 실행
} ?: Text("없음")
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "직접 해보기:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Switch(
                checked = showUser,
                onCheckedChange = { showUser = it }
            )
            Text(if (showUser) "user = 홍길동" else "user = null")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 조건부 렌더링 (let + Elvis)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (userName != null)
                    Color(0xFFE8F5E9)
                else
                    Color(0xFFFFEBEE)
            )
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                // 핵심: ?.let { } ?: 패턴
                userName?.let { name ->
                    Text("안녕하세요, ${name}님!")
                } ?: Text("로그인이 필요합니다")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "user?.let { } ?: Text() 패턴으로 조건부 UI!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ComprehensiveDemo() {
    var items by remember {
        mutableStateOf(
            listOf(
                Item("사과", true),
                Item("바나나", false),
                Item("오렌지", true),
                Item("포도", false)
            )
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "종합 예제: 4가지 문법 모두 사용",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            CodeBlock(
                code = """
// 1. 확장 함수로 커스텀 Modifier
fun Modifier.activeStyle() =
    this.background(Green).padding(8.dp)

// 2. 람다로 필터링
val activeItems = items.filter { it.isActive }

// 3. 후행 람다로 레이아웃
Column {
    activeItems.forEach { item ->
        // 4. 람다 + 후행 람다
        Text(
            text = item.name,
            modifier = Modifier.activeStyle()
        )
    }
}
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("결과:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // 실제 구현
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items
                    .filter { it.isActive }  // 람다
                    .forEach { item ->       // 람다
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .background(Color(0xFF81C784))  // 확장 함수
                                .padding(8.dp)                   // 확장 함수
                        )
                    }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 토글 버튼들
            Text("항목 활성화 토글:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEachIndexed { index, item ->
                    FilterChip(
                        selected = item.isActive,
                        onClick = {  // 람다!
                            items = items.toMutableList().apply {
                                this[index] = item.copy(isActive = !item.isActive)
                            }
                        },
                        label = { Text(item.name) }  // 후행 람다!
                    )
                }
            }
        }
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

@Composable
private fun CodeBlock(code: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF2D2D2D),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = code,
            modifier = Modifier.padding(12.dp),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = Color(0xFFE0E0E0),
            lineHeight = 18.sp
        )
    }
}

private data class Item(
    val name: String,
    val isActive: Boolean
)
