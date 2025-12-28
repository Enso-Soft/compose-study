package com.example.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * Scaffold를 사용하지 않고 Box로 직접 레이아웃을 구성할 때 발생하는 문제를 시연합니다.
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
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Scaffold 없이 Box로 TopBar, 콘텐츠, FAB를 직접 배치하면 " +
                            "요소들이 서로 겹치는 문제가 발생합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 데모: 겹침 발생
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Box로 직접 배치한 화면",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 예시에서 첫 번째 메모가 상단 바에 가려지는 것을 확인하세요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 문제가 발생하는 레이아웃 데모
                ProblemDemo()
            }
        }

        // 문제점 목록
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
                Text("1. 콘텐츠가 TopBar 뒤로 들어감 (첫 번째 아이템 가려짐)")
                Text("2. 수동으로 padding 계산 필요 (앱바 높이 직접 지정)")
                Text("3. FAB 위치를 수동으로 계산해야 함")
                Text("4. Snackbar 표시 시 FAB와 겹침")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Box(modifier = Modifier.fillMaxSize()) {
    // 상단 바
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .align(Alignment.TopCenter),
        color = primaryContainer
    ) { Text("메모") }

    // 콘텐츠 - padding 없음!
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(memos) { MemoCard(it) }
    }

    // FAB - 수동 위치 지정
    FloatingActionButton(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp),
        onClick = { }
    ) { Icon(Add) }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun ProblemDemo() {
    // 문제 상황을 시각적으로 보여주는 데모
    val memos = listOf(
        "첫 번째 메모 - 이 텍스트가 가려집니다!",
        "두 번째 메모",
        "세 번째 메모",
        "네 번째 메모",
        "다섯 번째 메모"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 콘텐츠 - padding 없이 배치 (문제!)
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(memos) { memo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = memo,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // 상단 바 역할 - 콘텐츠 위에 겹쳐서 표시됨
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.TopCenter),
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "메모 (Box 사용)",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // FAB - 수동 위치 지정
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "추가",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        // 문제 표시
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 60.dp, end = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Red.copy(alpha = 0.9f)
            )
        ) {
            Text(
                text = "첫 번째 메모가 가려짐!",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}
