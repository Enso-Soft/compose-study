package com.example.composable_function

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
 * 연습 문제: @Composable 함수 작성하기
 */

// ============================================================
// 연습 1: 첫 번째 Composable 함수 작성
// ============================================================

/**
 * 연습 1: 첫 번째 Composable 함수 작성하기
 *
 * 목표:
 * - @Composable 어노테이션 사용법 이해
 * - Composable 함수 네이밍 규칙 (PascalCase)
 * - 파라미터 받아서 UI에 표시
 *
 * TODO:
 * 1. WelcomeMessage Composable 함수 작성
 * 2. name 파라미터를 받아서 "환영합니다, {name}님!" 표시
 */

// TODO: @Composable 어노테이션을 추가하고 함수를 완성하세요
// 힌트: @Composable fun 함수명(파라미터) { Text(...) }

fun WelcomeMessage(name: String) {
    // TODO: Text 컴포넌트로 메시지 표시
    // 힌트: Text("환영합니다, ${name}님!")
}

// 정답:
// @Composable
// fun WelcomeMessage(name: String) {
//     Text("환영합니다, ${name}님!")
// }

@Composable
fun Practice1_ComposableScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 1: 첫 번째 Composable",
            description = "@Composable 함수를 직접 작성해보세요"
        ) {
            Text(
                text = "WelcomeMessage 함수를 @Composable로 만들어보세요:",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 테스트 영역
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "테스트 결과:",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // WelcomeMessage 호출 시도
                    // 함수가 @Composable이 아니면 여기서 에러 발생
                    // WelcomeMessage("홍길동")

                    Text(
                        text = "⚠️ WelcomeMessage에 @Composable을 추가하고\n" +
                                "위 주석을 해제하면 결과가 표시됩니다.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "@Composable 어노테이션을 함수 앞에 추가",
                "함수명은 PascalCase (첫 글자 대문자)",
                "Text() 컴포넌트로 문자열 표시",
                "문자열 템플릿: \"환영합니다, \${name}님!\""
            )
        )
    }
}

// ============================================================
// 연습 2: 상태와 Recomposition
// ============================================================

/**
 * 연습 2: 카운터 구현하기
 *
 * 목표:
 * - remember { mutableStateOf() } 사용법
 * - 상태 변경과 UI 자동 업데이트 이해
 * - by 위임 패턴
 *
 * TODO:
 * 1. count 상태를 remember로 감싸기
 * 2. 버튼 클릭 시 count 증가
 * 3. count 값 표시
 */
@Composable
fun Practice2_StateScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 2: 상태와 Recomposition",
            description = "remember로 상태를 관리하는 카운터를 구현하세요"
        ) {
            // TODO: count 상태를 remember로 감싸세요
            // 힌트: var count by remember { mutableIntStateOf(0) }
            var count = 0  // 이 줄을 수정하세요

            // 정답:
            // var count by remember { mutableIntStateOf(0) }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // TODO: onClick에서 count를 증가시키세요
                Button(
                    onClick = {
                        // TODO: count 증가
                        // 힌트: count++
                    }
                ) {
                    Text("+1")
                }

                // 정답:
                // Button(onClick = { count++ }) { Text("+1") }

                Text(
                    text = "Count: $count",
                    style = MaterialTheme.typography.headlineMedium
                )

                Button(
                    onClick = {
                        // TODO: count를 0으로 리셋
                    }
                ) {
                    Text("리셋")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (count == 0) {
                Text(
                    text = "⚠️ 버튼을 눌러도 숫자가 안 변한다면 remember를 추가하세요!",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    text = "✅ 잘 작동합니다! count = $count",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "var count by remember { mutableIntStateOf(0) }",
                "by 키워드로 위임하면 .value 없이 직접 접근 가능",
                "onClick = { count++ }",
                "리셋: count = 0"
            )
        )
    }
}

// ============================================================
// 연습 3: 조건부 렌더링
// ============================================================

/**
 * 연습 3: 조건부 렌더링
 *
 * 목표:
 * - if/else로 Composable 조건부 표시
 * - 상태에 따른 UI 변경
 * - Composable의 Enter/Leave Composition 이해
 *
 * TODO:
 * 1. isLoggedIn 상태 관리
 * 2. 로그인 상태에 따라 다른 화면 표시
 * 3. 로그인/로그아웃 버튼 구현
 */
@Composable
fun Practice3_ConditionalScreen() {
    // TODO: isLoggedIn 상태를 remember로 관리하세요
    // 힌트: var isLoggedIn by remember { mutableStateOf(false) }
    var isLoggedIn = false  // 이 줄을 수정하세요

    // 정답:
    // var isLoggedIn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 3: 조건부 렌더링",
            description = "로그인 상태에 따라 다른 화면을 표시하세요"
        ) {
            // TODO: isLoggedIn에 따라 다른 UI 표시
            // 힌트: if (isLoggedIn) { ... } else { ... }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isLoggedIn)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO: 조건부 렌더링 구현
                    // if (isLoggedIn) {
                    //     LoggedInContent()
                    // } else {
                    //     LoggedOutContent()
                    // }

                    Text("조건부 렌더링을 구현해주세요")

                    // 정답:
                    // if (isLoggedIn) {
                    //     Text("환영합니다!")
                    //     Text("로그인 되었습니다.")
                    // } else {
                    //     Text("로그인이 필요합니다")
                    //     Text("아래 버튼을 눌러 로그인하세요.")
                    // }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 로그인/로그아웃 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        // TODO: isLoggedIn = true
                    },
                    enabled = !isLoggedIn,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("로그인")
                }

                Button(
                    onClick = {
                        // TODO: isLoggedIn = false
                    },
                    enabled = isLoggedIn,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("로그아웃")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "현재 상태: ${if (isLoggedIn) "로그인됨" else "로그아웃됨"}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "var isLoggedIn by remember { mutableStateOf(false) }",
                "if (isLoggedIn) { 로그인UI } else { 로그아웃UI }",
                "버튼 onClick에서 상태 변경: isLoggedIn = true/false",
                "조건이 바뀌면 해당 Composable이 Composition에서 추가/제거됨"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 추가 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Composition 라이프사이클",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• if 조건이 true가 되면: Composable이 Composition에 진입\n" +
                            "• if 조건이 false가 되면: Composable이 Composition에서 이탈\n" +
                            "• 이탈 시 remember로 저장한 상태도 사라집니다!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
private fun PracticeCard(
    title: String,
    description: String,
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
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
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
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            hints.forEach { hint ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
