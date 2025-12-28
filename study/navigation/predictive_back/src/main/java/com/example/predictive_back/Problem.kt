package com.example.predictive_back

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * 기존 BackHandler를 사용했을 때 발생하는 갑작스러운 화면 전환 문제를 시연합니다.
 *
 * 문제점:
 * 1. 뒤로가기 시 애니메이션 없이 즉시 전환
 * 2. 제스처 도중 취소할 방법 없음
 * 3. 어디로 돌아가는지 미리 알 수 없음
 */
@Composable
fun ProblemScreen() {
    var showDetail by remember { mutableStateOf(false) }
    var transitionCount by remember { mutableIntStateOf(0) }

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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기존 BackHandler는 뒤로가기 완료 후에만 콜백이 호출됩니다.\n" +
                            "이로 인해 갑작스러운 화면 전환이 발생하고, 사용자는 제스처를 취소할 수 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 데모 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "갑작스러운 전환 횟수: ${transitionCount}회",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (transitionCount > 0)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!showDetail) {
                    // 목록 화면 (메인)
                    ProblemListScreen(
                        onItemClick = { showDetail = true }
                    )
                } else {
                    // 상세 화면
                    ProblemDetailScreen(
                        onBack = {
                            transitionCount++
                            showDetail = false
                        }
                    )
                }
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 애니메이션 없이 즉시 전환 - 사용자가 당황")
                Text("2. 제스처 도중 취소 불가능 - 실수로 뒤로가기해도 복구 불가")
                Text("3. 목적지 불확실 - 어디로 돌아가는지 모름")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 (현재 구현)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
// 기존 BackHandler - 즉시 전환
BackHandler(enabled = showDetail) {
    showDetail = false  // 애니메이션 없음!
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // 사용 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "테스트 방법",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. '상세 화면으로 이동' 버튼을 클릭하세요\n" +
                            "2. 화면 왼쪽 가장자리에서 오른쪽으로 스와이프하세요\n" +
                            "3. 화면이 갑자기 전환되는 것을 확인하세요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProblemListScreen(
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "메인 화면 (목록)",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "버튼을 눌러 상세 화면으로 이동하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onItemClick) {
            Text("상세 화면으로 이동")
        }
    }
}

@Composable
private fun ProblemDetailScreen(
    onBack: () -> Unit
) {
    // 기존 BackHandler - 애니메이션 없이 즉시 전환
    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
            Text(
                text = "상세 화면",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Text(
            text = "이 화면에서 왼쪽 가장자리를 스와이프해보세요.\n" +
                    "갑자기 이전 화면으로 전환됩니다!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = "제스처 도중 취소할 수 없습니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = onBack) {
            Text("버튼으로 뒤로가기")
        }
    }
}
