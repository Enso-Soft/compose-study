package com.example.badge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 해결책 화면
 *
 * Badge와 BadgedBox를 사용하여 문제를 해결하는 방법을 시연합니다.
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Badge & BadgedBox",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Material 3의 Badge와 BadgedBox를 사용하면 자동으로 정렬되고, " +
                            "크기가 조절되며, 접근성도 처리됩니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 인터랙티브 데모
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BadgedBox로 구현한 Badge",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                ProperBadgeDemo()
            }
        }

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. BadgedBox는 Badge를 자동으로 우상단에 배치합니다")
                Spacer(modifier = Modifier.height(4.dp))
                Text("2. Badge()만 사용하면 점(Dot) Badge, Badge { Text(...) }는 숫자 Badge")
                Spacer(modifier = Modifier.height(4.dp))
                Text("3. 99보다 큰 숫자는 \"99+\"로 표시하는 것이 좋습니다")
                Spacer(modifier = Modifier.height(4.dp))
                Text("4. containerColor와 contentColor로 색상을 커스터마이징할 수 있습니다")
            }
        }

        // 다양한 Badge 유형 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "다양한 Badge 유형",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                BadgeTypesDemo()
            }
        }

        // 올바른 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// BadgedBox 사용!
BadgedBox(
    badge = {
        if (count > 0) {
            Badge {
                val text = if (count > 99) "99+"
                           else count.toString()
                Text(text)
            }
        }
    }
) {
    Icon(
        imageVector = Icons.Filled.Mail,
        contentDescription = "메일"
    )
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 비유 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "비유: 스티커 케이스",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "BadgedBox는 '스티커를 붙일 수 있는 케이스'입니다.\n\n" +
                            "- content: 스티커를 붙일 물건 (아이콘)\n" +
                            "- badge: 붙일 스티커 (Badge)\n\n" +
                            "케이스가 알아서 스티커를 올바른 위치에 붙여줍니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/**
 * BadgedBox를 사용한 올바른 Badge 구현 데모
 */
@Composable
private fun ProperBadgeDemo() {
    var count by remember { mutableIntStateOf(5) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // BadgedBox 사용 - 올바른 구현
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 현재 카운트
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BadgedBox(
                    badge = {
                        if (count > 0) {
                            Badge {
                                Text(if (count > 99) "99+" else count.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "메일",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("현재: $count", style = MaterialTheme.typography.bodySmall)
            }

            // 99 (자동 처리)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BadgedBox(
                    badge = {
                        Badge { Text("99") }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "메일",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("99 (정상)", style = MaterialTheme.typography.bodySmall)
            }

            // 150 -> 99+ 처리
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BadgedBox(
                    badge = {
                        Badge { Text("99+") }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "메일",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("150 -> 99+", style = MaterialTheme.typography.bodySmall)
            }
        }

        // 카운트 조절 버튼
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { if (count > 0) count-- }) {
                Text("-")
            }
            Button(onClick = { count++ }) {
                Text("+")
            }
            OutlinedButton(onClick = { count = 150 }) {
                Text("150으로")
            }
        }

        Text(
            text = "숫자가 커져도 Badge가 깨지지 않습니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 다양한 Badge 유형 데모
 */
@Composable
private fun BadgeTypesDemo() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 1. Dot Badge (점 표시)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BadgedBox(
                badge = { Badge() }  // 내용 없으면 점 Badge
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "알림",
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("점 Badge", style = MaterialTheme.typography.bodySmall)
        }

        // 2. Number Badge (숫자 표시)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BadgedBox(
                badge = {
                    Badge { Text("5") }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "이메일",
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("숫자 Badge", style = MaterialTheme.typography.bodySmall)
        }

        // 3. Overflow Badge (99+)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BadgedBox(
                badge = {
                    Badge { Text("99+") }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "이메일",
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("99+ Badge", style = MaterialTheme.typography.bodySmall)
        }

        // 4. Custom Color Badge
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ) {
                        Text("3")
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "장바구니",
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("커스텀 색상", style = MaterialTheme.typography.bodySmall)
        }
    }
}
