package com.example.basic_ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Problem: 기본 UI 컴포넌트 사용 시 흔한 실수
 *
 * 1. TextField에 상태 연결 안 함 → 입력이 안 됨
 * 2. Button onClick 누락 → 컴파일 에러
 * 3. Icon contentDescription 누락 → 접근성 문제
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
                    text = "문제: 컴포넌트 사용 실수",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기본 UI 컴포넌트를 잘못 사용하면 예상대로 동작하지 않습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 문제 1: TextField 상태 연결 안 함
        TextFieldProblem()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 2: Button 사용 설명
        ButtonProblem()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 3: Text 스타일링 문제
        TextProblem()
    }
}

@Composable
private fun TextFieldProblem() {
    ProblemCard(
        title = "문제 1: TextField에 상태 연결 안 함",
        errorCode = """
// ❌ 잘못된 코드
TextField(
    value = "",           // 항상 빈 문자열!
    onValueChange = { }   // 아무것도 안 함!
)

// 타이핑해도 아무 글자도 안 보입니다!
        """.trimIndent(),
        explanation = "TextField는 value와 onValueChange로 상태를 연결해야 합니다."
    ) {
        Text(
            text = "직접 확인해보세요:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 잘못된 TextField - 상태 연결 없음
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "⚠️ 상태 연결 없는 TextField:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 이 TextField는 입력이 안됩니다!
                TextField(
                    value = "",  // 항상 빈 문자열
                    onValueChange = { },  // 아무것도 안 함
                    label = { Text("여기에 타이핑해보세요") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아무리 타이핑해도 글자가 나타나지 않습니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ButtonProblem() {
    ProblemCard(
        title = "문제 2: Button onClick 누락",
        errorCode = """
// ❌ 컴파일 에러!
Button {
    Text("클릭")
}
// onClick 파라미터가 필수입니다!

// 또한 흔한 실수:
Button(onClick = { }) {
    "버튼 텍스트"  // Text() 없이 문자열만!
}
// 버튼에 아무것도 표시되지 않음
        """.trimIndent(),
        explanation = "Button은 onClick이 필수이고, 내부 컨텐츠는 Composable이어야 합니다."
    ) {
        Text(
            text = "Button 필수 구조:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // 올바른 버튼 구조 시각화
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF2D2D2D),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
Button(
    onClick = { /* 필수! */ }
) {
    Text("버튼 텍스트")  // Composable 필수!
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = Color(0xFF81C784)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { }) {
                        Text("올바른 Button")
                    }

                    OutlinedButton(onClick = { }) {
                        Text("OutlinedButton")
                    }
                }
            }
        }
    }
}

@Composable
private fun TextProblem() {
    ProblemCard(
        title = "문제 3: Text 스타일링 미적용",
        errorCode = """
// 스타일 없는 밋밋한 텍스트
Text("제목")
Text("본문")
Text("캡션")

// 모두 같은 크기와 스타일로 표시됨
// UI가 계층 구조 없이 평평해 보임
        """.trimIndent(),
        explanation = "Text에 스타일을 적용하여 시각적 계층 구조를 만들어야 합니다."
    ) {
        Text(
            text = "스타일 없는 텍스트들:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // 스타일 없는 텍스트들
                Text("제목")
                Text("본문")
                Text("캡션")

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⚠️ 위 텍스트들은 모두 같은 스타일입니다. " +
                            "시각적 계층이 없어서 어떤 것이 제목인지 알기 어렵습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun ProblemCard(
    title: String,
    errorCode: String,
    explanation: String,
    content: @Composable ColumnScope.() -> Unit
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

            // 에러 코드 블록
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2D2D2D),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = errorCode,
                    modifier = Modifier.padding(12.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B6B),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}
