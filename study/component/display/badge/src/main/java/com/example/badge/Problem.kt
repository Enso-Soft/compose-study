package com.example.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 문제 상황 화면
 *
 * Badge 컴포넌트를 사용하지 않고 Box로 직접 구현할 때 발생하는 문제를 시연합니다.
 */
@Composable
fun ProblemScreen() {
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
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "메일 아이콘 위에 읽지 않은 메일 개수를 표시하고 싶습니다.\n" +
                            "Badge 컴포넌트를 모르면 Box로 직접 구현하려고 시도하게 됩니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 데모 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Box로 직접 구현한 Badge",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                ManualBadgeDemo()
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 정렬 문제: offset 값을 하드코딩해야 하고, 아이콘 크기가 바뀌면 다시 조정 필요")
                Spacer(modifier = Modifier.height(4.dp))
                Text("2. 크기 조절: 숫자가 2자리, 3자리일 때 Badge 크기를 수동으로 조절해야 함")
                Spacer(modifier = Modifier.height(4.dp))
                Text("3. 접근성 누락: 스크린 리더가 Badge 정보를 읽을 수 없음")
                Spacer(modifier = Modifier.height(4.dp))
                Text("4. Material 불일치: 공식 디자인 가이드라인과 다른 모양새")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 이렇게 하면 문제가 발생합니다!
Box {
    Icon(Icons.Filled.Mail, ...)

    // 수동으로 Badge 배치
    Box(
        modifier = Modifier
            .size(16.dp)
            .offset(x = 12.dp, y = (-4).dp)  // 하드코딩!
            .background(Color.Red, CircleShape)
    ) {
        Text(
            text = count.toString(),
            fontSize = 10.sp  // 수동 조절!
        )
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 비유 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "비유: 스티커 붙이기",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "노트북에 스티커를 붙일 때, 직접 테이프로 고정하면 위치 잡기가 어렵죠.\n" +
                            "스티커 전용 케이스(BadgedBox)를 사용하면 딱 맞는 위치에 스티커(Badge)가 붙습니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

/**
 * 수동으로 Badge를 구현하는 데모
 *
 * Box를 사용해서 직접 Badge를 배치할 때 발생하는 문제를 보여줍니다.
 */
@Composable
private fun ManualBadgeDemo() {
    var count by remember { mutableIntStateOf(5) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 수동 구현 Badge - 문제가 있음
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 한 자리 숫자
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ManualBadgeIcon(count = count)
                Spacer(modifier = Modifier.height(4.dp))
                Text("현재: $count", style = MaterialTheme.typography.bodySmall)
            }

            // 두 자리 숫자 (문제 발생)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ManualBadgeIcon(count = 99)
                Spacer(modifier = Modifier.height(4.dp))
                Text("99 (깨짐)", style = MaterialTheme.typography.bodySmall)
            }

            // 세 자리 숫자 (심각한 문제)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ManualBadgeIcon(count = 150)
                Spacer(modifier = Modifier.height(4.dp))
                Text("150 (심각)", style = MaterialTheme.typography.bodySmall)
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
        }

        Text(
            text = "숫자가 커지면 Badge가 깨지는 것을 확인하세요!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * 수동으로 구현한 Badge 아이콘 (문제가 있는 코드)
 */
@Composable
private fun ManualBadgeIcon(count: Int) {
    Box {
        Icon(
            imageVector = Icons.Filled.Email,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )

        if (count > 0) {
            // 문제: 크기가 고정되어 있어서 숫자가 커지면 깨짐
            Box(
                modifier = Modifier
                    .size(16.dp)  // 고정 크기 - 문제!
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .background(Color.Red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    color = Color.White,
                    fontSize = 10.sp,  // 고정 크기 - 문제!
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}
