package com.example.compose_testing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 Finder와 Assertion 사용하기
 *
 * 요구사항:
 * - 인사 메시지가 표시되는지 확인하는 테스트 작성
 * - 버튼이 존재하는지 확인하는 테스트 작성
 *
 * 학습 포인트:
 * - onNodeWithText() 사용법
 * - assertIsDisplayed(), assertExists() 차이
 */
@Composable
fun Practice1_FinderBasics() {
    var showGreeting by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: Finder 기초",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "목표",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "1. \"Hello, Compose!\" 텍스트가 표시되는지 확인\n" +
                            "2. \"Toggle\" 버튼이 존재하는지 확인\n" +
                            "3. 토글 후 메시지가 사라지는지 확인",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 테스트 대상 UI
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showGreeting) {
                    Text(
                        text = "Hello, Compose!",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.testTag("greeting_text")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showGreeting = !showGreeting },
                    modifier = Modifier.testTag("toggle_button")
                ) {
                    Text("Toggle")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 힌트 카드
        HintCard(
            hints = listOf(
                "onNodeWithText(\"Hello, Compose!\").assertIsDisplayed()",
                "onNodeWithTag(\"toggle_button\").assertExists()",
                "assertDoesNotExist()로 요소 없음 확인"
            ),
            testCode = """
@Test
fun greeting_isDisplayed() {
    composeTestRule.setContent {
        Practice1_FinderBasics()
    }

    // TODO: 테스트 코드 작성
    // composeTestRule
    //     .onNodeWithText("Hello, Compose!")
    //     .assertIsDisplayed()
}

@Test
fun toggleButton_hidesGreeting() {
    composeTestRule.setContent {
        Practice1_FinderBasics()
    }

    // TODO: 버튼 클릭 후 텍스트 사라짐 확인
    // composeTestRule.onNodeWithTag("toggle_button").performClick()
    // composeTestRule.onNodeWithText("Hello, Compose!").assertDoesNotExist()
}
            """.trimIndent()
        )
    }
}

/**
 * 연습 문제 2: Actions과 상태 변화 테스트
 *
 * 요구사항:
 * - 버튼 클릭으로 카운터 증가 테스트
 * - 여러 번 클릭 후 올바른 값 확인
 *
 * 학습 포인트:
 * - performClick() 사용법
 * - 상태 변화 후 UI 검증
 */
@Composable
fun Practice2_ActionsAndState() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: Actions과 상태 변화",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "목표",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "1. 초기 상태 \"Count: 0\" 확인\n" +
                            "2. + 버튼 클릭 후 \"Count: 1\" 확인\n" +
                            "3. - 버튼으로 감소 테스트\n" +
                            "4. Reset 버튼 동작 확인",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 테스트 대상 UI
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.testTag("count_display")
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilledTonalButton(
                        onClick = { count-- },
                        modifier = Modifier.testTag("decrement_button")
                    ) {
                        Text("-")
                    }

                    Button(
                        onClick = { count++ },
                        modifier = Modifier.testTag("increment_button")
                    ) {
                        Text("+")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { count = 0 },
                    modifier = Modifier.testTag("reset_button")
                ) {
                    Text("Reset")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HintCard(
            hints = listOf(
                "performClick()으로 버튼 클릭",
                "assertTextContains()로 부분 텍스트 매칭",
                "여러 번 클릭: repeat(3) { performClick() }"
            ),
            testCode = """
@Test
fun counter_incrementsOnClick() {
    composeTestRule.setContent {
        Practice2_ActionsAndState()
    }

    // 초기값 확인
    composeTestRule
        .onNodeWithTag("count_display")
        .assertTextEquals("Count: 0")

    // TODO: 버튼 클릭 후 값 확인
    // composeTestRule
    //     .onNodeWithTag("increment_button")
    //     .performClick()
    //
    // composeTestRule
    //     .onNodeWithTag("count_display")
    //     .assertTextEquals("Count: 1")
}

@Test
fun reset_setsCountToZero() {
    // TODO: 값 증가 후 리셋 테스트
}
            """.trimIndent()
        )
    }
}

/**
 * 연습 문제 3: 복합 테스트 - 리스트와 입력
 *
 * 요구사항:
 * - Todo 아이템 추가 테스트
 * - 리스트 아이템 개수 확인
 * - 아이템 삭제 테스트
 *
 * 학습 포인트:
 * - performTextInput() 사용법
 * - onAllNodesWithTag() 사용법
 * - assertCountEquals() 사용법
 */
@Composable
fun Practice3_ListAndInput() {
    var todos by remember { mutableStateOf(listOf("Learn Compose", "Write Tests")) }
    var newTodo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: 리스트와 입력",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "목표",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "1. 초기 Todo 개수 확인 (2개)\n" +
                            "2. 새 Todo 입력 및 추가\n" +
                            "3. 추가 후 개수 확인 (3개)\n" +
                            "4. 삭제 기능 테스트",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 입력 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTodo,
                onValueChange = { newTodo = it },
                label = { Text("New Todo") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("todo_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (newTodo.isNotBlank()) {
                        todos = todos + newTodo
                        newTodo = ""
                    }
                },
                modifier = Modifier.testTag("add_button")
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add todo"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Todo 리스트
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .testTag("todo_list")
        ) {
            items(todos, key = { it }) { todo ->
                TodoItem(
                    text = todo,
                    onDelete = { todos = todos - todo }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "테스트 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = """
• performTextInput("새 할일")으로 입력
• onAllNodesWithTag("todo_item").assertCountEquals(2)
• onNodeWithText("Learn Compose").assertExists()
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun TodoItem(
    text: String,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("todo_item")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.testTag("delete_$text")
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete $text"
                )
            }
        }
    }
}

@Composable
private fun HintCard(hints: List<String>, testCode: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "힌트",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            hints.forEach { hint ->
                Text(
                    text = "• $hint",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "테스트 코드 예시",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = testCode,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
