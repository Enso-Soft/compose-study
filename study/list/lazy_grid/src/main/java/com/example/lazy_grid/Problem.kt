package com.example.lazy_grid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Problem 화면: Column/Row로 그리드 구현 시 발생하는 문제
 *
 * 이 화면에서는 LazyGrid 없이 Column과 Row만으로 그리드를 구현할 때
 * 어떤 성능 문제가 발생하는지 보여줍니다.
 *
 * 핵심 문제:
 * 1. 모든 아이템이 한 번에 생성됨 (메모리 낭비)
 * 2. 하나가 변경되면 전체가 다시 그려짐 (Recomposition 비효율)
 * 3. 많은 아이템에서 스크롤이 버벅거림
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
        // 문제 상황 설명
        ProblemExplanationCard()

        // 비효율적인 그리드 데모
        InefficientGridDemo()

        // 문제점 정리
        ProblemSummaryCard()
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Column/Row로 그리드 구현 시 문제",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
                    |시나리오: 사진 갤러리 앱에서 100장의 사진을 2열 그리드로 표시하려고 합니다.
                    |
                    |아래 코드처럼 Column과 Row를 중첩하면:
                    |
                    |Column {
                    |    photos.chunked(2).forEach { rowPhotos ->
                    |        Row {
                    |            rowPhotos.forEach { PhotoItem(it) }
                    |        }
                    |    }
                    |}
                    |
                    |100개 아이템이 모두 한 번에 생성되어 메모리를 낭비합니다!
                """.trimMargin(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun InefficientGridDemo() {
    // 실제 앱에서는 위험하므로 50개로 제한
    val items = remember { (1..50).toList() }

    // Recomposition 횟수 추적
    var recompositionCount by remember { mutableIntStateOf(0) }

    // 상태 변경용
    var triggerRecomposition by remember { mutableIntStateOf(0) }

    // SideEffect로 Recomposition 감지
    SideEffect {
        recompositionCount++
    }

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "비효율적인 그리드 (Column/Row)",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Recomposition 카운터 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recomposition 횟수: $recompositionCount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Button(
                    onClick = { triggerRecomposition++ }
                ) {
                    Text("Recomposition 유발")
                }
            }

            // 더미 상태 (recomposition 유발용)
            val dummy = triggerRecomposition

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "아이템 개수: ${items.size}개 (전부 한 번에 생성됨)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 비효율적인 그리드 구현
            // 주의: 실제로는 이렇게 하면 안 됩니다!
            Column(
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        rowItems.forEach { item ->
                            // 각 아이템이 즉시 생성됨
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(
                                        Color(0xFF000000 + item * 0x050505)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$item",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProblemSummaryCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "발생하는 문제점",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            ProblemItem(
                number = 1,
                title = "메모리 과다 사용",
                description = "100개 아이템이 화면에 보이지 않아도 모두 메모리에 로드됩니다."
            )

            ProblemItem(
                number = 2,
                title = "느린 초기 로딩",
                description = "모든 아이템을 한 번에 생성하므로 화면 표시가 지연됩니다."
            )

            ProblemItem(
                number = 3,
                title = "비효율적인 Recomposition",
                description = "하나의 아이템이 변경되어도 전체 그리드가 다시 그려집니다."
            )

            ProblemItem(
                number = 4,
                title = "스크롤 버벅거림",
                description = "많은 아이템에서 부드러운 60fps 스크롤이 불가능합니다."
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "해결책: LazyGrid를 사용하면 화면에 보이는 아이템만 렌더링합니다!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
