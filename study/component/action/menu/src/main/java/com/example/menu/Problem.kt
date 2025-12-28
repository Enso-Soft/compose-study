package com.example.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: Menu 없이 팝업 메뉴 구현 시도
 *
 * 이 화면에서는 DropdownMenu를 사용하지 않고
 * 직접 Box/Column으로 메뉴를 구현할 때 발생하는 문제를 보여줍니다.
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
        // 문제 설명 Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "문제 상황: 직접 메뉴 구현",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        게시글의 "더보기" 메뉴를 구현하려고 합니다.
                        DropdownMenu를 모르는 상태에서 Box/Column으로
                        직접 구현하면 다음과 같은 문제가 발생합니다:

                        1. 레이아웃 밀림 - 메뉴가 나타나면 다른 콘텐츠가 밀림
                        2. 위치 계산 어려움 - 버튼 아래 정확한 위치 배치
                        3. 외부 클릭 처리 - 메뉴 외부 탭 시 닫히지 않음
                        4. 오버레이 부재 - 다른 UI 위에 떠 있지 않음

                        아래 데모에서 직접 확인해보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 문제 데모 1: 레이아웃 밀림
        ManualMenuDemo()

        // 잘못된 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 예시",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = """
@Composable
fun ManualMenuAttempt() {
    var showMenu by remember {
        mutableStateOf(false)
    }

    Column {
        Row {
            Text("게시글 제목")
            IconButton(
                onClick = { showMenu = !showMenu }
            ) {
                Icon(Icons.Default.MoreVert, "더보기")
            }
        }

        // 문제: 레이아웃에 영향을 줌
        if (showMenu) {
            Card {
                Column {
                    Text("수정")
                    Text("삭제")
                }
            }
        }

        // 이 콘텐츠가 밀려남!
        Text("게시글 내용...")
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // 문제점 요약
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제점",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                ProblemItem(
                    number = 1,
                    title = "레이아웃 밀림",
                    description = "메뉴가 나타나면 아래 콘텐츠가 밀려남"
                )
                ProblemItem(
                    number = 2,
                    title = "오버레이 부재",
                    description = "다른 UI 위에 팝업으로 떠 있지 않음"
                )
                ProblemItem(
                    number = 3,
                    title = "외부 클릭 미처리",
                    description = "메뉴 외부를 탭해도 닫히지 않음"
                )
                ProblemItem(
                    number = 4,
                    title = "접근성 누락",
                    description = "키보드 탐색, 스크린 리더 지원 없음"
                )
            }
        }
    }
}

/**
 * 직접 구현한 메뉴 데모 - 문제점 시연
 */
@Composable
private fun ManualMenuDemo() {
    var showMenu by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("") }

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "데모: 직접 구현한 메뉴",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 게시글 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "게시글 제목입니다",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "작성자 | 2025.01.15",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "더보기"
                    )
                }
            }

            // 문제: 이 메뉴가 레이아웃에 영향을 줌
            if (showMenu) {
                Card(
                    modifier = Modifier.padding(top = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "수정",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAction = "수정"
                                    showMenu = false
                                }
                                .padding(12.dp)
                        )
                        Text(
                            text = "삭제",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAction = "삭제"
                                    showMenu = false
                                }
                                .padding(12.dp)
                        )
                        Text(
                            text = "공유",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAction = "공유"
                                    showMenu = false
                                }
                                .padding(12.dp)
                        )
                    }
                }
            }

            // 이 콘텐츠가 밀려남!
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "게시글 본문 내용입니다. 이 텍스트는 메뉴가 열리면 아래로 밀려나게 됩니다. " +
                        "DropdownMenu를 사용하면 이 콘텐츠가 밀리지 않고 메뉴가 오버레이로 표시됩니다.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (selectedAction.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "선택된 액션: $selectedAction",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 문제점 강조
            Surface(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "더보기 버튼을 눌러보세요! 본문이 밀리는 것을 확인할 수 있습니다.",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
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
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.error
        ) {
            Text(
                text = "$number",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onError
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
