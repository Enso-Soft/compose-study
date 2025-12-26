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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 연습 문제: Kotlin 핵심 문법 활용하기
 */

// ============================================================
// 연습 1: 람다 표현식
// ============================================================

/**
 * 연습 1: 람다를 사용한 리스트 처리
 *
 * 목표:
 * - filter, map 람다 사용
 * - onClick 람다로 상태 변경
 *
 * TODO:
 * 1. numbers에서 짝수만 필터링 (filter 사용)
 * 2. 필터링된 숫자를 2배로 변환 (map 사용)
 * 3. 버튼 클릭 시 결과 표시 토글
 */
@Composable
fun Practice1_LambdaScreen() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    var showResult by remember { mutableStateOf(false) }

    // TODO: 람다를 사용하여 짝수만 필터링하고 2배로 변환
    // 힌트: numbers.filter { 조건 }.map { 변환 }
    val result: List<Int> = listOf() // 이 줄을 수정하세요

    // 정답:
    // val result = numbers.filter { it % 2 == 0 }.map { it * 2 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 1: 람다 표현식",
            description = "filter와 map 람다를 사용하여 리스트 처리하기"
        ) {
            Text("원본 리스트: $numbers")

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: onClick 람다로 showResult 토글
            // 힌트: onClick = { showResult = !showResult }
            Button(
                onClick = { /* TODO */ }
            ) {
                Text(if (showResult) "숨기기" else "결과 보기")
            }

            // 정답:
            // Button(onClick = { showResult = !showResult }) { ... }

            if (showResult) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("짝수만 2배: $result")

                if (result.isEmpty()) {
                    Text(
                        text = "아직 구현되지 않았습니다!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        HintCard(
            hints = listOf(
                "filter { } - 조건이 true인 항목만 남김",
                "map { } - 각 항목을 변환",
                "it - 람다의 단일 파라미터 자동 참조",
                "it % 2 == 0 - 짝수 판별"
            )
        )
    }
}

// ============================================================
// 연습 2: 확장 함수 + Modifier
// ============================================================

/**
 * 연습 2: 커스텀 Modifier 확장 함수 만들기
 *
 * 목표:
 * - 확장 함수 문법 이해
 * - Modifier 체이닝 구현
 *
 * TODO:
 * 1. debugBorder() 확장 함수 구현 - 빨간 테두리 추가
 * 2. highlightBackground() 확장 함수 구현 - 노란 배경 추가
 * 3. 두 함수를 체이닝하여 사용
 */

// TODO: Modifier에 빨간 테두리를 추가하는 확장 함수
// 힌트: fun Modifier.함수명() = this.border(...)
fun Modifier.debugBorder(): Modifier = this // 이 줄을 수정하세요

// 정답:
// fun Modifier.debugBorder(): Modifier = this.border(2.dp, Color.Red)

// TODO: Modifier에 노란 배경을 추가하는 확장 함수
fun Modifier.highlightBackground(): Modifier = this // 이 줄을 수정하세요

// 정답:
// fun Modifier.highlightBackground(): Modifier = this.background(Color.Yellow)

// 보너스: 파라미터를 받는 확장 함수
// TODO: 테두리 두께를 파라미터로 받는 확장 함수
fun Modifier.customBorder(width: Dp = 1.dp, color: Color = Color.Gray): Modifier = this

// 정답:
// fun Modifier.customBorder(width: Dp = 1.dp, color: Color = Color.Gray): Modifier =
//     this.border(width, color)

@Composable
fun Practice2_ExtensionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 2: 확장 함수",
            description = "커스텀 Modifier 확장 함수 만들기"
        ) {
            Text("확장 함수를 구현한 후 아래 결과를 확인하세요:")

            Spacer(modifier = Modifier.height(16.dp))

            // 테스트 1: debugBorder
            Text(
                text = "debugBorder() 테스트",
                modifier = Modifier
                    .debugBorder()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 테스트 2: highlightBackground
            Text(
                text = "highlightBackground() 테스트",
                modifier = Modifier
                    .highlightBackground()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 테스트 3: 체이닝
            Text(
                text = "두 함수 체이닝 테스트",
                modifier = Modifier
                    .debugBorder()
                    .highlightBackground()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 테스트 4: 파라미터 있는 확장 함수
            Text(
                text = "customBorder(4.dp, Blue) 테스트",
                modifier = Modifier
                    .customBorder(4.dp, Color.Blue)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "fun Modifier.함수명() = this.기존함수()",
                "this는 현재 Modifier를 참조",
                "border(width, color)로 테두리 추가",
                "background(color)로 배경 추가",
                "체이닝: Modifier.a().b().c()"
            )
        )
    }
}

// ============================================================
// 연습 3: 널 안전성
// ============================================================

/**
 * 연습 3: 널 안전성으로 조건부 UI 구현
 *
 * 목표:
 * - ?. (안전 호출) 사용
 * - ?: (Elvis 연산자) 사용
 * - ?.let { } 패턴 활용
 *
 * TODO:
 * 1. nullable한 User 데이터로 조건부 렌더링
 * 2. 로그인/로그아웃 버튼 구현
 * 3. 사용자 정보 또는 로그인 안내 표시
 */

private data class User(
    val name: String,
    val email: String,
    val level: Int
)

@Composable
fun Practice3_NullSafetyScreen() {
    var currentUser by remember { mutableStateOf<User?>(null) }

    // 샘플 사용자
    val sampleUser = User("홍길동", "hong@example.com", 5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        PracticeCard(
            title = "연습 3: 널 안전성",
            description = "nullable 데이터로 조건부 UI 구현하기"
        ) {
            // 로그인/로그아웃 버튼
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { currentUser = sampleUser },
                    enabled = currentUser == null
                ) {
                    Text("로그인")
                }

                Button(
                    onClick = { currentUser = null },
                    enabled = currentUser != null
                ) {
                    Text("로그아웃")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 널 안전성을 활용한 조건부 UI
            // 힌트: currentUser?.let { } ?: 대체UI
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // TODO: 이 부분을 ?.let { } ?: 패턴으로 구현하세요

                    // 현재 구현 (if-else 방식) - 이것을 변경하세요
                    if (currentUser != null) {
                        Text("환영합니다, ${currentUser!!.name}님!")
                        Text("이메일: ${currentUser!!.email}")
                        Text("레벨: ${currentUser!!.level}")
                    } else {
                        Text("로그인이 필요합니다")
                    }

                    // 정답 (?.let { } ?: 패턴):
                    // currentUser?.let { user ->
                    //     Text("환영합니다, ${user.name}님!")
                    //     Text("이메일: ${user.email}")
                    //     Text("레벨: ${user.level}")
                    // } ?: Text("로그인이 필요합니다")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 추가 연습: Elvis 연산자
            Text("추가 연습: Elvis 연산자 (?:)")
            Spacer(modifier = Modifier.height(8.dp))

            // TODO: Elvis 연산자로 기본값 제공
            // 힌트: currentUser?.name ?: "게스트"
            val displayName = "이름 없음" // 이 줄을 수정하세요

            // 정답:
            // val displayName = currentUser?.name ?: "게스트"

            Text("표시 이름: $displayName")

            // TODO: 안전 호출로 레벨 표시
            // null이면 0 표시
            val displayLevel = 0 // 이 줄을 수정하세요

            // 정답:
            // val displayLevel = currentUser?.level ?: 0

            Text("레벨: $displayLevel")
        }

        Spacer(modifier = Modifier.height(16.dp))

        HintCard(
            hints = listOf(
                "?. - null이면 null 반환, 아니면 속성 접근",
                "?: - null이면 오른쪽 값 사용",
                "?.let { } - null 아닐 때만 블록 실행",
                "?.let { } ?: 대체값 - 조건부 렌더링 패턴",
                "!! 사용은 피하기 (NPE 발생 가능)"
            )
        )
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
