package com.example.tooltip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: PlainTooltip 기본 사용 (쉬움)
 *
 * 하트 아이콘에 "즐겨찾기에 추가" Tooltip을 추가하세요.
 */
@Composable
fun Practice1_Screen() {
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
                    text = "연습 1: PlainTooltip 추가 (쉬움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "하트 아이콘 버튼에 PlainTooltip을 추가하세요.\n" +
                            "길게 누르면 \"즐겨찾기에 추가\"라는 설명이 나타나야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
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
                Spacer(modifier = Modifier.height(8.dp))
                HintItem("TooltipBox로 IconButton을 감싸세요")
                HintItem("positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()")
                HintItem("tooltip = { PlainTooltip { Text(\"...\") } }")
                HintItem("state = rememberTooltipState()")
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
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice1_Exercise()

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "아이콘을 길게 눌러 Tooltip이 나타나는지 확인하세요",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 요구사항 체크리스트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항 체크리스트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChecklistItem("TooltipBox 사용")
                ChecklistItem("PlainTooltip 사용")
                ChecklistItem("\"즐겨찾기에 추가\" 텍스트 표시")
                ChecklistItem("길게 누르면 Tooltip 표시")
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 아래 IconButton에 PlainTooltip을 추가하세요
    //
    // 요구사항:
    // 1. TooltipBox로 IconButton을 감싸세요
    // 2. positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
    // 3. tooltip = { PlainTooltip { Text("즐겨찾기에 추가") } }
    // 4. state = rememberTooltipState()
    //
    // 현재 코드 (수정 필요):
    IconButton(onClick = { }) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "즐겨찾기",
            tint = MaterialTheme.colorScheme.error
        )
    }
}

/**
 * 연습 문제 2: RichTooltip 구현 (중간)
 *
 * 정보 아이콘에 제목, 본문, 액션 버튼이 있는 RichTooltip을 구현하세요.
 */
@Composable
fun Practice2_Screen() {
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
                    text = "연습 2: RichTooltip 구현 (중간)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "정보(Info) 아이콘에 RichTooltip을 추가하세요.\n\n" +
                            "다음 내용을 포함해야 합니다:\n" +
                            "- 제목: \"앱 정보\"\n" +
                            "- 본문: \"이 앱은 문서 편집을 위한 앱입니다.\"\n" +
                            "- 액션: \"자세히 보기\" TextButton",
                    style = MaterialTheme.typography.bodyMedium
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
                Spacer(modifier = Modifier.height(8.dp))
                HintItem("positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider()")
                HintItem("RichTooltip(title = { ... }, action = { ... }) { 본문 }")
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
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice2_Exercise()

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "아이콘을 길게 눌러 RichTooltip이 나타나는지 확인하세요",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 요구사항 체크리스트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항 체크리스트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChecklistItem("TooltipBox 사용")
                ChecklistItem("RichTooltip 사용")
                ChecklistItem("title 파라미터로 \"앱 정보\" 표시")
                ChecklistItem("본문에 앱 설명 표시")
                ChecklistItem("action 파라미터로 TextButton 추가")
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 아래 IconButton에 RichTooltip을 추가하세요
    //
    // 요구사항:
    // 1. TooltipBox로 IconButton을 감싸세요
    // 2. positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider()
    // 3. RichTooltip 구성:
    //    - title = { Text("앱 정보") }
    //    - action = { TextButton(onClick = { }) { Text("자세히 보기") } }
    //    - 본문: Text("이 앱은 문서 편집을 위한 앱입니다.")
    // 4. state = rememberTooltipState()
    //
    // 현재 코드 (수정 필요):
    IconButton(onClick = { }) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "정보",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 연습 문제 3: 이미지 편집 앱 툴바 구현 (어려움)
 *
 * 5개의 도구 버튼에 적절한 Tooltip을 적용하세요.
 */
@Composable
fun Practice3_Screen() {
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
                    text = "연습 3: 이미지 편집 앱 툴바 (어려움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "이미지 편집 앱의 툴바를 완성하세요.\n\n" +
                            "5개의 버튼에 적절한 Tooltip을 적용합니다:\n\n" +
                            "1. 자르기(Crop) - PlainTooltip: \"이미지 자르기\"\n" +
                            "2. 회전(Rotate) - PlainTooltip: \"90도 회전\"\n" +
                            "3. 필터(Filter) - RichTooltip:\n" +
                            "   - 제목: \"필터 적용\"\n" +
                            "   - 본문: \"세피아, 흑백, 비비드 등 다양한 필터\"\n" +
                            "4. 조정(Tune) - RichTooltip + 프로그래밍 방식 표시:\n" +
                            "   - 제목: \"세부 조정\"\n" +
                            "   - 본문: \"밝기, 대비, 채도를 조절합니다\"\n" +
                            "   - LaunchedEffect로 처음 한 번 자동 표시\n" +
                            "5. 저장(Save) - PlainTooltip: \"저장\"",
                    style = MaterialTheme.typography.bodyMedium
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
                Spacer(modifier = Modifier.height(8.dp))
                HintItem("rememberTooltipState()로 상태를 저장하세요")
                HintItem("LaunchedEffect(Unit) { tooltipState.show() }로 자동 표시")
                HintItem("각 버튼마다 적절한 Tooltip 타입을 선택하세요")
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
                    text = "이미지 편집기 툴바",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice3_Exercise()

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "각 아이콘을 길게 눌러 Tooltip을 확인하세요\n" +
                            "(조정 버튼은 자동으로 Tooltip이 표시되어야 합니다)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 요구사항 체크리스트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항 체크리스트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChecklistItem("5개 버튼 모두 Tooltip 적용")
                ChecklistItem("단순 도구(자르기, 회전, 저장)에 PlainTooltip")
                ChecklistItem("복잡한 도구(필터, 조정)에 RichTooltip")
                ChecklistItem("조정 버튼 Tooltip이 화면 진입 시 자동 표시")
                ChecklistItem("각 Tooltip 텍스트가 요구사항과 일치")
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 이미지 편집 앱 툴바를 완성하세요
    //
    // 요구사항:
    // 1. 자르기(Close 아이콘) - PlainTooltip: "이미지 자르기"
    // 2. 회전(Refresh 아이콘) - PlainTooltip: "90도 회전"
    // 3. 필터(Star 아이콘) - RichTooltip:
    //    - 제목: "필터 적용"
    //    - 본문: "세피아, 흑백, 비비드 등 다양한 필터"
    // 4. 조정(Build 아이콘) - RichTooltip + LaunchedEffect로 자동 표시:
    //    - 제목: "세부 조정"
    //    - 본문: "밝기, 대비, 채도를 조절합니다"
    //    - LaunchedEffect(Unit) { tooltipState.show() }
    // 5. 저장(Check 아이콘) - PlainTooltip: "저장"
    //
    // 힌트:
    // - 조정 버튼의 TooltipState는 별도로 선언: val tuneTooltipState = rememberTooltipState()
    // - LaunchedEffect(Unit)은 Composable 함수 내에서 사용
    //
    // 현재 코드 (수정 필요):
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 1. 자르기 버튼
        IconButton(onClick = { }) {
            Icon(Icons.Default.Close, contentDescription = "자르기")
        }

        // 2. 회전 버튼
        IconButton(onClick = { }) {
            Icon(Icons.Default.Refresh, contentDescription = "회전")
        }

        // 3. 필터 버튼
        IconButton(onClick = { }) {
            Icon(Icons.Default.Star, contentDescription = "필터")
        }

        // 4. 조정 버튼
        IconButton(onClick = { }) {
            Icon(Icons.Default.Build, contentDescription = "조정")
        }

        // 5. 저장 버튼
        IconButton(onClick = { }) {
            Icon(Icons.Default.Check, contentDescription = "저장")
        }
    }
}

@Composable
private fun HintItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "- ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ChecklistItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "[ ]",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
