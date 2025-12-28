package com.example.compose_introduction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Practice: Compose 첫 걸음
 *
 * 3가지 연습 문제를 통해 Compose의 기본을 익힙니다.
 */

// ============================================================
// 연습 1: Hello, Compose!
// ============================================================

/**
 * 연습 1: 첫 번째 Composable 함수 작성하기
 *
 * 목표: "Hello, [이름]!" 텍스트를 표시하는 Composable 만들기
 *
 * 힌트:
 * 1. @Composable 어노테이션을 함수 위에 붙입니다
 * 2. Text() 컴포넌트를 사용합니다
 * 3. 문자열 인터폴레이션($name)을 사용합니다
 */
@Composable
fun Practice1_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        HintCard(
            title = "연습 1: Hello, Compose!",
            description = """
                첫 번째 Composable 함수를 작성해보세요.

                과제:
                - HelloCompose라는 함수를 완성하세요
                - name 파라미터를 받아 "Hello, [이름]!" 형식으로 표시합니다

                힌트:
                - @Composable 어노테이션 사용
                - Text() 컴포넌트 사용
                - ${'$'}name으로 문자열 인터폴레이션
            """.trimIndent()
        )

        // 연습 영역
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "결과 확인",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // 여기서 HelloCompose 함수 호출
                HelloCompose(name = "Compose")
                HelloCompose(name = "Android")
                HelloCompose(name = "World")
            }
        }

        // 정답 (주석 처리)
        AnswerCard(
            answer = """
@Composable
fun HelloCompose(name: String) {
    Text(text = "Hello, ${'$'}name!")
}
            """.trimIndent()
        )
    }
}

/**
 * TODO: 이 함수를 완성하세요!
 *
 * 요구사항:
 * - name 파라미터를 받아 "Hello, [이름]!" 형식으로 표시
 */
@Composable
fun HelloCompose(name: String) {
    // TODO: 아래 주석을 해제하고 TODO 부분을 완성하세요

    // 정답:
    Text(text = "Hello, $name!")
}

// ============================================================
// 연습 2: 버튼으로 텍스트 토글하기
// ============================================================

/**
 * 연습 2: 상태 기반 UI 변경
 *
 * 목표: 버튼을 누르면 텍스트가 토글되는 UI 만들기
 *
 * 힌트:
 * 1. remember { mutableStateOf(true) }로 상태 선언
 * 2. by 키워드로 상태 위임
 * 3. if 조건으로 텍스트 선택
 * 4. Button onClick에서 상태 반전 (!isHello)
 */
@Composable
fun Practice2_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        HintCard(
            title = "연습 2: 버튼으로 텍스트 토글하기",
            description = """
                버튼을 누르면 텍스트가 바뀌는 UI를 만들어보세요.

                과제:
                - "안녕하세요!" <-> "반갑습니다!" 토글
                - 버튼을 누를 때마다 텍스트가 바뀝니다

                힌트:
                - var isHello by remember { mutableStateOf(true) }
                - if (isHello) "안녕하세요!" else "반갑습니다!"
                - onClick = { isHello = !isHello }
            """.trimIndent()
        )

        // 연습 영역
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "결과 확인",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // 여기서 ToggleGreeting 함수 호출
                ToggleGreeting()
            }
        }

        // 정답 (주석 처리)
        AnswerCard(
            answer = """
@Composable
fun ToggleGreeting() {
    var isHello by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isHello) "안녕하세요!" else "반갑습니다!",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(onClick = { isHello = !isHello }) {
            Text("토글")
        }
    }
}
            """.trimIndent()
        )
    }
}

/**
 * TODO: 이 함수를 완성하세요!
 *
 * 요구사항:
 * - 버튼을 누르면 "안녕하세요!" <-> "반갑습니다!" 토글
 */
@Composable
fun ToggleGreeting() {
    // TODO: 상태 선언
    var isHello by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO: 조건부 텍스트 표시
        Text(
            text = if (isHello) "안녕하세요!" else "반갑습니다!",
            style = MaterialTheme.typography.headlineMedium
        )

        // TODO: 상태 토글 버튼
        Button(onClick = { isHello = !isHello }) {
            Text("토글")
        }
    }
}

// ============================================================
// 연습 3: 실시간 입력 반영
// ============================================================

/**
 * 연습 3: 상태와 UI의 자동 동기화
 *
 * 목표: TextField에 입력한 내용이 실시간으로 반영되는 UI 만들기
 *
 * 힌트:
 * 1. remember { mutableStateOf("") }로 문자열 상태 선언
 * 2. TextField의 value와 onValueChange 연결
 * 3. 아래 Text에서 상태 변수 사용
 */
@Composable
fun Practice3_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        HintCard(
            title = "연습 3: 실시간 입력 반영",
            description = """
                TextField에 입력한 내용이 아래 텍스트에 실시간으로 반영되는 UI를 만들어보세요.

                과제:
                - 이름을 입력하면 "환영합니다, [이름]님!" 표시
                - 입력할 때마다 즉시 반영

                힌트:
                - var name by remember { mutableStateOf("") }
                - TextField(value = name, onValueChange = { name = it })
                - Text("환영합니다, ${'$'}{name}님!")
            """.trimIndent()
        )

        // 연습 영역
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "결과 확인",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // 여기서 WelcomeInput 함수 호출
                WelcomeInput()
            }
        }

        // 핵심 포인트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = """
                        이 연습에서 경험한 것이 바로 Compose의 핵심입니다!

                        - name 상태를 변경하면 UI가 자동으로 업데이트됩니다
                        - setText()를 호출하지 않았습니다
                        - 상태와 UI가 항상 동기화됩니다

                        이것이 "UI = f(State)" 원칙입니다.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 정답 (주석 처리)
        AnswerCard(
            answer = """
@Composable
fun WelcomeInput() {
    var name by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름 입력") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = if (name.isEmpty()) "이름을 입력해주세요"
                   else "환영합니다, ${'$'}{name}님!",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
            """.trimIndent()
        )
    }
}

/**
 * TODO: 이 함수를 완성하세요!
 *
 * 요구사항:
 * - TextField에 이름 입력
 * - 아래에 "환영합니다, [이름]님!" 실시간 표시
 */
@Composable
fun WelcomeInput() {
    // TODO: 문자열 상태 선언
    var name by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TODO: TextField 구현
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름 입력") },
            modifier = Modifier.fillMaxWidth()
        )

        // TODO: 환영 메시지 표시
        Text(
            text = if (name.isEmpty()) "이름을 입력해주세요" else "환영합니다, ${name}님!",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

// ============================================================
// 공통 UI 컴포넌트
// ============================================================

@Composable
private fun HintCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AnswerCard(answer: String) {
    var showAnswer by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "정답 보기",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = showAnswer,
                    onCheckedChange = { showAnswer = it }
                )
            }

            if (showAnswer) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = answer,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
