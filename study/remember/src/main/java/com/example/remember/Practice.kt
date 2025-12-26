package com.example.remember

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 토글 버튼 구현
 *
 * 목표: remember + mutableStateOf로 Boolean 상태 관리
 * 시나리오: "좋아요" 버튼 - 클릭하면 하트가 채워지고/비워지고
 *
 * TODO: isLiked 상태를 remember + mutableStateOf로 선언하고,
 *       버튼 클릭 시 토글되도록 구현하세요.
 */
@Composable
fun Practice1_ToggleButton() {
    // TODO: Boolean 상태 선언
    // 힌트: var isLiked by remember { mutableStateOf(false) }

    // === 정답 (주석 해제하여 확인) ===
    var isLiked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 좋아요 토글 버튼",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("버튼을 클릭하면 하트 아이콘이 토글됩니다.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: var isLiked by remember { mutableStateOf(false) }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 좋아요 버튼
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isLiked) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        // TODO: isLiked 토글
                        isLiked = !isLiked
                    },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "좋아요",
                        modifier = Modifier.size(48.dp),
                        tint = if (isLiked) Color.Red else Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isLiked) "좋아요!" else "좋아요를 눌러주세요",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // 상태 확인
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태",
                    fontWeight = FontWeight.Bold
                )
                Text("isLiked = $isLiked")
            }
        }
    }
}

/**
 * 연습 문제 2: 텍스트 입력 폼
 *
 * 목표: TextField와 함께 문자열 상태 관리
 * 시나리오: 이름을 입력받아 인사 메시지 표시
 *
 * TODO: name 상태를 선언하고 TextField와 연결하세요.
 */
@Composable
fun Practice2_TextInput() {
    // TODO: 문자열 상태 선언
    // 힌트: var name by remember { mutableStateOf("") }

    // === 정답 (주석 해제하여 확인) ===
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 텍스트 입력 폼",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("이름을 입력하면 인사 메시지가 표시됩니다.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: TextField의 value와 onValueChange 연결",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 입력 폼
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,  // TODO: name 상태 연결
                    onValueChange = { name = it },  // TODO: 상태 업데이트
                    label = { Text("이름을 입력하세요") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 인사 메시지
                if (name.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E9)
                        )
                    ) {
                        Text(
                            text = "안녕하세요, ${name}님!",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // 글자 수 표시
                Text(
                    text = "입력된 글자 수: ${name.length}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        // 추가 과제
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "추가 과제",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "초기화 버튼을 추가해보세요!\n" +
                            "힌트: Button의 onClick에서 name = \"\"",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 초기화 버튼 예시
                OutlinedButton(onClick = { name = "" }) {
                    Text("초기화")
                }
            }
        }
    }
}

/**
 * 연습 문제 3: remember key 활용
 *
 * 목표: key 매개변수로 의존성 기반 재계산
 * 시나리오: 숫자 목록에서 합계 계산, 목록 변경 시에만 재계산
 *
 * TODO: remember(key) 패턴을 사용하여 합계를 캐싱하세요.
 */
@Composable
fun Practice3_RememberKey() {
    var numbers by remember { mutableStateOf(listOf(10, 20, 30)) }
    var calculationCount by remember { mutableIntStateOf(0) }

    // TODO: remember(key)를 사용하여 합계 캐싱
    // 힌트: val sum = remember(numbers) { ... }

    // === 정답 ===
    val sum = remember(numbers) {
        calculationCount++
        println("Calculating sum... (count: $calculationCount)")
        numbers.sum()
    }

    // 평균 계산 (추가 연습)
    val average = remember(numbers) {
        if (numbers.isEmpty()) 0.0 else numbers.average()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: remember key 활용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("숫자 목록의 합계를 캐싱합니다. 목록이 변경될 때만 재계산됩니다.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: remember(numbers) { numbers.sum() }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 숫자 목록 및 결과
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "숫자 목록",
                    fontWeight = FontWeight.Bold
                )

                // 현재 목록 표시
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = numbers.joinToString(", "),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                HorizontalDivider()

                // 계산 결과
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("합계", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "$sum",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("평균", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = String.format("%.1f", average),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("계산 횟수", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "$calculationCount",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                HorizontalDivider()

                // 조작 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        val newNumber = (1..100).random()
                        numbers = numbers + newNumber
                    }) {
                        Text("랜덤 추가")
                    }

                    OutlinedButton(onClick = {
                        if (numbers.isNotEmpty()) {
                            numbers = numbers.dropLast(1)
                        }
                    }) {
                        Text("마지막 제거")
                    }

                    FilledTonalButton(onClick = {
                        numbers = listOf(10, 20, 30)
                        calculationCount = 0
                    }) {
                        Text("초기화")
                    }
                }
            }
        }

        // 핵심 포인트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "관찰해보세요",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 숫자를 추가/제거하면 계산 횟수가 증가합니다.\n" +
                            "2. numbers가 변경되지 않으면 캐시된 값을 사용합니다.\n" +
                            "3. 비용이 큰 계산(정렬, 필터링 등)에 유용합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
