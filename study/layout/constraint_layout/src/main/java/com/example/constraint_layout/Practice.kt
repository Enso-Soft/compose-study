package com.example.constraint_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

/**
 * 연습 1: 기본 ConstraintLayout - 로그인 폼
 *
 * 난이도: ★☆☆ (초급)
 *
 * 목표:
 * - createRefs()로 참조 생성
 * - constrainAs()로 제약 조건 적용
 * - linkTo()로 요소 간 연결
 *
 * 구현할 UI:
 * ┌─────────────────────────────┐
 * │         [App Logo]          │
 * │                             │
 * │    ┌─────────────────┐     │
 * │    │ 이메일           │     │
 * │    └─────────────────┘     │
 * │    ┌─────────────────┐     │
 * │    │ 비밀번호         │     │
 * │    └─────────────────┘     │
 * │                             │
 * │      [ 로그인 버튼 ]        │
 * └─────────────────────────────┘
 */
@Composable
fun Practice1_LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 1: 로그인 폼",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
힌트:
1. createRefs()로 (logo, emailField, passwordField, loginBtn) 생성
2. logo: parent.top에 연결, 수평 중앙 정렬
3. emailField: logo.bottom에 연결
4. passwordField: emailField.bottom에 연결
5. loginBtn: passwordField.bottom에 연결
6. centerHorizontallyTo(parent)로 수평 중앙 정렬
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            // TODO: ConstraintLayout으로 로그인 폼 구현
            // 아래 Column을 ConstraintLayout으로 교체하세요

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("여기에 ConstraintLayout으로 구현하세요")
            }

            // ========== 정답 코드 (주석 해제하여 확인) ==========
            /*
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                val (logo, emailField, passwordField, loginBtn) = createRefs()

                // 로고
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .constrainAs(logo) {
                            top.linkTo(parent.top, margin = 32.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    tint = MaterialTheme.colorScheme.primary
                )

                // 이메일 입력
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("이메일") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    modifier = Modifier.constrainAs(emailField) {
                        top.linkTo(logo.bottom, margin = 32.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                )

                // 비밀번호 입력
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("비밀번호") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    modifier = Modifier.constrainAs(passwordField) {
                        top.linkTo(emailField.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                )

                // 로그인 버튼
                Button(
                    onClick = { },
                    modifier = Modifier.constrainAs(loginBtn) {
                        top.linkTo(passwordField.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                ) {
                    Text("로그인")
                }
            }
            */
        }
    }
}

/**
 * 연습 2: Barrier 활용 - 상품 정보 카드
 *
 * 난이도: ★★☆ (중급)
 *
 * 목표:
 * - createEndBarrier()로 레이블 끝에 배리어 생성
 * - 값들을 배리어 기준으로 정렬
 *
 * 구현할 UI:
 * ┌─────────────────────────────┐
 * │ 상품명         맥북 프로 16  │
 * │ 가격           3,490,000원   │
 * │ 재고           23개          │
 * │ 배송비         무료          │
 * └─────────────────────────────┘
 */
@Composable
fun Practice2_BarrierSettings() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 2: Barrier로 정렬",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
힌트:
1. 레이블 4개와 값 4개의 참조 생성
2. 레이블들을 세로로 배치 (top.linkTo로 연결)
3. createEndBarrier(label1, label2, label3, label4)로 배리어 생성
4. 각 값을 해당 레이블의 top과 배리어의 start에 연결
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            // TODO: Barrier를 사용하여 레이블/값 정렬 구현
            // 아래 Column을 ConstraintLayout으로 교체하세요

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("여기에 Barrier를 사용하여 구현하세요")
            }

            // ========== 정답 코드 (주석 해제하여 확인) ==========
            /*
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val (label1, label2, label3, label4) = createRefs()
                val (value1, value2, value3, value4) = createRefs()

                // 레이블들
                Text(
                    text = "상품명",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "가격",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label2) {
                        top.linkTo(label1.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "재고",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label3) {
                        top.linkTo(label2.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )
                Text(
                    text = "배송비",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.constrainAs(label4) {
                        top.linkTo(label3.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                    }
                )

                // 배리어 생성
                val endBarrier = createEndBarrier(label1, label2, label3, label4, margin = 24.dp)

                // 값들
                Text(
                    text = "맥북 프로 16",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(value1) {
                        top.linkTo(label1.top)
                        start.linkTo(endBarrier)
                    }
                )
                Text(
                    text = "3,490,000원",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.constrainAs(value2) {
                        top.linkTo(label2.top)
                        start.linkTo(endBarrier)
                    }
                )
                Text(
                    text = "23개",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(value3) {
                        top.linkTo(label3.top)
                        start.linkTo(endBarrier)
                    }
                )
                Text(
                    text = "무료",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.constrainAs(value4) {
                        top.linkTo(label4.top)
                        start.linkTo(endBarrier)
                    }
                )
            }
            */
        }
    }
}

/**
 * 연습 3: Chain 활용 - 하단 네비게이션
 *
 * 난이도: ★★★ (고급)
 *
 * 목표:
 * - createHorizontalChain()으로 버튼 균등 배치
 * - ChainStyle.SpreadInside 적용
 * - 선택 상태에 따라 UI 변경
 *
 * 구현할 UI:
 * ┌─────────────────────────────────────┐
 * │  [홈]     [검색]    [장바구니] [프로필]│
 * └─────────────────────────────────────┘
 */
@Composable
fun Practice3_ChainNavigation() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "연습 3: Chain으로 네비게이션",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
힌트:
1. 4개 버튼 참조 생성 (home, search, cart, profile)
2. createHorizontalChain()으로 체인 생성
3. chainStyle = ChainStyle.SpreadInside 적용
4. 각 버튼의 top/bottom을 parent에 연결
5. 선택된 버튼은 filled, 나머지는 outlined 스타일
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 선택된 탭 표시
        Text(
            text = "선택된 탭: ${listOf("홈", "검색", "장바구니", "프로필")[selectedIndex]}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            // TODO: Chain을 사용하여 네비게이션 바 구현
            // 아래 Row를 ConstraintLayout으로 교체하세요

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("여기에 Chain을 사용하여 구현하세요")
            }

            // ========== 정답 코드 (주석 해제하여 확인) ==========
            /*
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                val (home, search, cart, profile) = createRefs()

                // Chain 생성
                createHorizontalChain(home, search, cart, profile, chainStyle = ChainStyle.SpreadInside)

                // 홈 버튼
                if (selectedIndex == 0) {
                    FilledTonalButton(
                        onClick = { selectedIndex = 0 },
                        modifier = Modifier.constrainAs(home) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.Home, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("홈")
                    }
                } else {
                    OutlinedButton(
                        onClick = { selectedIndex = 0 },
                        modifier = Modifier.constrainAs(home) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.Home, null, Modifier.size(20.dp))
                    }
                }

                // 검색 버튼
                if (selectedIndex == 1) {
                    FilledTonalButton(
                        onClick = { selectedIndex = 1 },
                        modifier = Modifier.constrainAs(search) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.Search, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("검색")
                    }
                } else {
                    OutlinedButton(
                        onClick = { selectedIndex = 1 },
                        modifier = Modifier.constrainAs(search) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.Search, null, Modifier.size(20.dp))
                    }
                }

                // 장바구니 버튼
                if (selectedIndex == 2) {
                    FilledTonalButton(
                        onClick = { selectedIndex = 2 },
                        modifier = Modifier.constrainAs(cart) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("장바구니")
                    }
                } else {
                    OutlinedButton(
                        onClick = { selectedIndex = 2 },
                        modifier = Modifier.constrainAs(cart) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, null, Modifier.size(20.dp))
                    }
                }

                // 프로필 버튼
                if (selectedIndex == 3) {
                    FilledTonalButton(
                        onClick = { selectedIndex = 3 },
                        modifier = Modifier.constrainAs(profile) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.Person, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("프로필")
                    }
                } else {
                    OutlinedButton(
                        onClick = { selectedIndex = 3 },
                        modifier = Modifier.constrainAs(profile) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    ) {
                        Icon(Icons.Default.Person, null, Modifier.size(20.dp))
                    }
                }
            }
            */
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ChainStyle 비교 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "ChainStyle 비교",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
• Spread: [  A  B  C  D  ] - 균등 분배 (여백 포함)
• SpreadInside: [A  B  C  D] - 첫/끝 가장자리, 나머지 균등
• Packed: [    ABCD    ] - 중앙에 모아서 배치
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
