package com.example.compose_testing

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

/**
 * Compose UI Testing 예제 테스트
 *
 * 이 파일은 실제 동작하는 테스트 코드 예시입니다.
 * androidTest 폴더에 위치하여 실제 테스트로 실행할 수 있습니다.
 *
 * 실행 방법:
 * ./gradlew :study:compose_testing:connectedAndroidTest
 */
class ExampleTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ========================================
    // Practice 1: Finder 기초 테스트
    // ========================================

    @Test
    fun greeting_isDisplayedInitially() {
        composeTestRule.setContent {
            Practice1_FinderBasics()
        }

        // 텍스트로 요소 찾기 + 표시 확인
        composeTestRule
            .onNodeWithText("Hello, Compose!")
            .assertIsDisplayed()
    }

    @Test
    fun toggleButton_exists() {
        composeTestRule.setContent {
            Practice1_FinderBasics()
        }

        // testTag로 요소 찾기 + 존재 확인
        composeTestRule
            .onNodeWithTag("toggle_button")
            .assertExists()
    }

    @Test
    fun toggleButton_hidesGreeting() {
        composeTestRule.setContent {
            Practice1_FinderBasics()
        }

        // 초기: 텍스트 표시됨
        composeTestRule
            .onNodeWithText("Hello, Compose!")
            .assertIsDisplayed()

        // 버튼 클릭
        composeTestRule
            .onNodeWithTag("toggle_button")
            .performClick()

        // 클릭 후: 텍스트 사라짐
        composeTestRule
            .onNodeWithText("Hello, Compose!")
            .assertDoesNotExist()
    }

    // ========================================
    // Practice 2: Actions과 상태 변화 테스트
    // ========================================

    @Test
    fun counter_showsInitialValue() {
        composeTestRule.setContent {
            Practice2_ActionsAndState()
        }

        composeTestRule
            .onNodeWithTag("count_display")
            .assertTextEquals("Count: 0")
    }

    @Test
    fun incrementButton_increasesCount() {
        composeTestRule.setContent {
            Practice2_ActionsAndState()
        }

        // + 버튼 클릭
        composeTestRule
            .onNodeWithTag("increment_button")
            .performClick()

        // 값 확인
        composeTestRule
            .onNodeWithTag("count_display")
            .assertTextEquals("Count: 1")
    }

    @Test
    fun decrementButton_decreasesCount() {
        composeTestRule.setContent {
            Practice2_ActionsAndState()
        }

        // - 버튼 클릭
        composeTestRule
            .onNodeWithTag("decrement_button")
            .performClick()

        // 값 확인 (0 → -1)
        composeTestRule
            .onNodeWithTag("count_display")
            .assertTextEquals("Count: -1")
    }

    @Test
    fun multipleClicks_accumulate() {
        composeTestRule.setContent {
            Practice2_ActionsAndState()
        }

        // 3번 클릭
        repeat(3) {
            composeTestRule
                .onNodeWithTag("increment_button")
                .performClick()
        }

        // 값 확인
        composeTestRule
            .onNodeWithTag("count_display")
            .assertTextEquals("Count: 3")
    }

    @Test
    fun resetButton_setsCountToZero() {
        composeTestRule.setContent {
            Practice2_ActionsAndState()
        }

        // 먼저 증가
        repeat(5) {
            composeTestRule
                .onNodeWithTag("increment_button")
                .performClick()
        }

        // 리셋
        composeTestRule
            .onNodeWithTag("reset_button")
            .performClick()

        // 0으로 돌아옴
        composeTestRule
            .onNodeWithTag("count_display")
            .assertTextEquals("Count: 0")
    }

    // ========================================
    // Practice 3: 리스트와 입력 테스트
    // ========================================

    @Test
    fun todoList_showsInitialItems() {
        composeTestRule.setContent {
            Practice3_ListAndInput()
        }

        // 초기 아이템 2개 확인
        composeTestRule
            .onAllNodesWithTag("todo_item")
            .assertCountEquals(2)
    }

    @Test
    fun addTodo_increasesListCount() {
        composeTestRule.setContent {
            Practice3_ListAndInput()
        }

        // 새 할일 입력
        composeTestRule
            .onNodeWithTag("todo_input")
            .performTextInput("New Todo Item")

        // 추가 버튼 클릭
        composeTestRule
            .onNodeWithTag("add_button")
            .performClick()

        // 아이템 3개로 증가
        composeTestRule
            .onAllNodesWithTag("todo_item")
            .assertCountEquals(3)
    }

    @Test
    fun addedTodo_appearsInList() {
        composeTestRule.setContent {
            Practice3_ListAndInput()
        }

        val newTodoText = "Write more tests"

        // 입력 및 추가
        composeTestRule
            .onNodeWithTag("todo_input")
            .performTextInput(newTodoText)

        composeTestRule
            .onNodeWithTag("add_button")
            .performClick()

        // 새 아이템이 리스트에 있는지 확인
        composeTestRule
            .onNodeWithText(newTodoText)
            .assertExists()
    }

    @Test
    fun deleteTodo_removesFromList() {
        composeTestRule.setContent {
            Practice3_ListAndInput()
        }

        // 초기: 2개
        composeTestRule
            .onAllNodesWithTag("todo_item")
            .assertCountEquals(2)

        // 첫 번째 아이템 삭제
        composeTestRule
            .onNodeWithTag("delete_Learn Compose")
            .performClick()

        // 삭제 후: 1개
        composeTestRule
            .onAllNodesWithTag("todo_item")
            .assertCountEquals(1)

        // 삭제된 아이템 없음 확인
        composeTestRule
            .onNodeWithText("Learn Compose")
            .assertDoesNotExist()
    }

    // ========================================
    // Solution: 로그인 폼 테스트
    // ========================================

    @Test
    fun loginForm_hasAllFields() {
        composeTestRule.setContent {
            TestableLoginForm()
        }

        // 모든 필수 요소 존재 확인
        composeTestRule.onNodeWithTag("email_field").assertExists()
        composeTestRule.onNodeWithTag("password_field").assertExists()
        composeTestRule.onNodeWithTag("login_button").assertExists()
    }

    @Test
    fun loginForm_acceptsInput() {
        composeTestRule.setContent {
            TestableLoginForm()
        }

        // 이메일 입력
        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("test@example.com")

        // 비밀번호 입력
        composeTestRule
            .onNodeWithTag("password_field")
            .performTextInput("password123")

        // 입력값 확인
        composeTestRule
            .onNodeWithTag("email_field")
            .assertTextContains("test@example.com")
    }

    @Test
    fun loginButton_withValidInput_showsSuccess() {
        composeTestRule.setContent {
            TestableLoginForm()
        }

        // 입력
        composeTestRule
            .onNodeWithTag("email_field")
            .performTextInput("user@email.com")

        composeTestRule
            .onNodeWithTag("password_field")
            .performTextInput("securepass")

        // 로그인 버튼 클릭
        composeTestRule
            .onNodeWithTag("login_button")
            .performClick()

        // 성공 메시지 확인
        composeTestRule
            .onNodeWithTag("login_result")
            .assertTextContains("successful")
    }

    @Test
    fun loginButton_withEmptyInput_showsError() {
        composeTestRule.setContent {
            TestableLoginForm()
        }

        // 빈 상태로 로그인 시도
        composeTestRule
            .onNodeWithTag("login_button")
            .performClick()

        // 에러 메시지 확인
        composeTestRule
            .onNodeWithTag("login_result")
            .assertTextContains("Please enter")
    }
}
