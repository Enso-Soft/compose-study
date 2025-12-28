package com.example.badge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 Badge 표시 (쉬움)
 *
 * 알림 아이콘 위에 빨간 점(Dot Badge)을 표시합니다.
 */
@Composable
fun Practice1_BasicBadgeScreen() {
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
                    text = "연습 1: 기본 Badge 표시",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "알림 아이콘 위에 빨간 점(Dot Badge)을 표시하세요.\n\n" +
                            "요구사항:\n" +
                            "- Icons.Filled.Notifications 아이콘 사용\n" +
                            "- 아이콘 위에 빨간 점 Badge 표시 (숫자 없음)"
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- BadgedBox의 badge 파라미터에 Badge()를 전달하세요")
                Text("- Badge()에 content를 전달하지 않으면 점 Badge가 됩니다")
                Text("- content에 Icon을 배치하세요")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "결과 미리보기",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: BadgedBox를 사용하여 알림 아이콘에 빨간 점 Badge를 표시하세요
    //
    // 구현 단계:
    // 1. BadgedBox로 감싸기
    // 2. badge 파라미터에 Badge() 전달 (내용 없이)
    // 3. content에 Icons.Filled.Notifications 아이콘 배치
    //
    // 코드 구조:
    // BadgedBox(
    //     badge = { ??? }
    // ) {
    //     Icon(???)
    // }

    // 아래 Text를 지우고 직접 구현해보세요!
    Text("TODO: BadgedBox와 Badge를 사용하여 구현하세요!")
}

/**
 * 연습 문제 2: 동적 카운트 Badge (중간)
 *
 * 버튼을 눌러 메시지 개수를 증가/감소하고, Badge에 표시합니다.
 */
@Composable
fun Practice2_DynamicBadgeScreen() {
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
                    text = "연습 2: 동적 카운트 Badge",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "버튼을 눌러 메시지 개수를 증가/감소하고, Badge에 표시하세요.\n\n" +
                            "요구사항:\n" +
                            "- +/- 버튼으로 카운트 조절\n" +
                            "- 카운트가 0이면 Badge 숨기기\n" +
                            "- 카운트가 99보다 크면 \"99+\" 표시\n" +
                            "- Icons.Filled.Email 아이콘 사용"
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- remember { mutableIntStateOf(0) }로 상태 관리")
                Text("- if (count > 0) 조건으로 Badge 표시 여부 결정")
                Text("- if (count > 99) \"99+\" else count.toString()")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "결과 미리보기",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Practice2_Exercise()
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 메시지 개수를 표시하는 동적 Badge를 구현하세요
    //
    // 구현 단계:
    // 1. var count by remember { mutableIntStateOf(0) } 선언
    // 2. BadgedBox로 Email 아이콘 감싸기
    // 3. count > 0 일 때만 Badge 표시
    // 4. count > 99 이면 "99+" 표시, 아니면 count.toString()
    // 5. +/- 버튼 추가
    //
    // 코드 구조:
    // Column {
    //     BadgedBox(
    //         badge = {
    //             if (count > 0) {
    //                 Badge { Text(???) }
    //             }
    //         }
    //     ) {
    //         Icon(???)
    //     }
    //
    //     Row {
    //         Button(onClick = { ??? }) { Text("-") }
    //         Button(onClick = { ??? }) { Text("+") }
    //     }
    // }

    // 아래 Text를 지우고 직접 구현해보세요!
    Text("TODO: 동적 카운트 Badge를 구현하세요!")
}

/**
 * 연습 문제 3: 장바구니 아이콘 Badge (어려움)
 *
 * 장바구니 아이콘에 상품 개수를 표시하는 완전한 UI를 구현합니다.
 */
@Composable
fun Practice3_ShoppingCartScreen() {
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
                    text = "연습 3: 장바구니 아이콘 Badge",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "장바구니 아이콘에 상품 개수를 표시하는 완전한 UI를 구현하세요.\n\n" +
                            "요구사항:\n" +
                            "- Icons.Filled.ShoppingCart 아이콘 사용\n" +
                            "- 커스텀 색상 적용 (MaterialTheme.colorScheme.primary)\n" +
                            "- \"장바구니에 담기\" 버튼으로 개수 증가\n" +
                            "- \"비우기\" 버튼으로 개수를 0으로 리셋\n" +
                            "- 0일 때 Badge 숨기기, 99보다 크면 \"99+\" 표시"
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- Badge(containerColor = ..., contentColor = ...) 사용")
                Text("- MaterialTheme.colorScheme.primary로 배경색")
                Text("- MaterialTheme.colorScheme.onPrimary로 글자색")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "결과 미리보기",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Practice3_Exercise()
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 장바구니 Badge를 구현하세요
    //
    // 구현 단계:
    // 1. var cartCount by remember { mutableIntStateOf(0) } 선언
    // 2. BadgedBox로 ShoppingCart 아이콘 감싸기
    // 3. Badge에 커스텀 색상 적용:
    //    - containerColor = MaterialTheme.colorScheme.primary
    //    - contentColor = MaterialTheme.colorScheme.onPrimary
    // 4. cartCount > 0 일 때만 Badge 표시
    // 5. cartCount > 99 이면 "99+" 표시
    // 6. "장바구니에 담기" 버튼 (cartCount++)
    // 7. "비우기" 버튼 (cartCount = 0)
    //
    // 코드 구조:
    // Column {
    //     BadgedBox(
    //         badge = {
    //             if (cartCount > 0) {
    //                 Badge(
    //                     containerColor = ???,
    //                     contentColor = ???
    //                 ) {
    //                     Text(???)
    //                 }
    //             }
    //         }
    //     ) {
    //         Icon(???)
    //     }
    //
    //     Button(onClick = { cartCount++ }) {
    //         Text("장바구니에 담기")
    //     }
    //
    //     OutlinedButton(onClick = { cartCount = 0 }) {
    //         Text("비우기")
    //     }
    // }

    // 아래 Text를 지우고 직접 구현해보세요!
    Text("TODO: 장바구니 Badge를 구현하세요!")
}
