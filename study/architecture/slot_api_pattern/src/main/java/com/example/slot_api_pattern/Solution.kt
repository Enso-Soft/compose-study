package com.example.slot_api_pattern

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 올바른 코드 - Slot API 패턴 사용
 *
 * Slot API를 사용하면:
 * 1. 파라미터 폭발 방지 - 슬롯으로 모든 커스터마이징 가능
 * 2. 완전한 유연성 - 어떤 Composable이든 주입 가능
 * 3. API 안정성 - 새 요구사항에도 API 변경 불필요
 * 4. 관심사 분리 - 컨테이너와 콘텐츠 역할 분리
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Solution: Slot API Pattern",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ============================================================
        // 1. 기본 Slot API
        // ============================================================
        SectionTitle("1. 기본 Slot API")

        // 다양한 사용 예시
        FlexibleInfoCard(
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(40.dp)
                )
            },
            title = { Text("시스템 경고", fontWeight = FontWeight.Bold) },
            description = { Text("디스크 용량이 부족합니다") },
            action = {
                TextButton(onClick = { }) {
                    Text("정리하기")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlexibleInfoCard(
            icon = {
                // 커스텀 아이콘 - 로딩 인디케이터도 가능!
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp
                )
            },
            title = { Text("동기화 중...", fontWeight = FontWeight.Bold) },
            description = {
                Column {
                    Text("클라우드와 데이터를 동기화하고 있습니다")
                    LinearProgressIndicator(
                        progress = { 0.7f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
            // action 슬롯 생략 - 기본값 {} 사용
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlexibleInfoCard(
            icon = {
                // 배지가 있는 아이콘
                Box {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text("5")
                    }
                }
            },
            title = { Text("새 알림", fontWeight = FontWeight.Bold) },
            description = { Text("읽지 않은 알림이 5개 있습니다") },
            action = {
                Row {
                    TextButton(onClick = { }) { Text("모두 읽음") }
                    TextButton(onClick = { }) { Text("보기") }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("핵심 포인트:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("- icon, title, description, action 모두 슬롯")
                Text("- 어떤 Composable이든 주입 가능")
                Text("- API 변경 없이 로딩, 배지 등 추가")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ============================================================
        // 2. Scoped Slots (RowScope, ColumnScope 처럼)
        // ============================================================
        SectionTitle("2. Scoped Slots")

        FlexibleButton(onClick = { }) {
            // RowScope가 제공되어 weight 사용 가능!
            Text("왼쪽 정렬", modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlexibleButton(onClick = { }) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("항목 추가")
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlexibleButton(onClick = { }) {
            Text("처리 중...")
            Spacer(modifier = Modifier.width(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Scoped Slot의 장점:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("- content: @Composable RowScope.() -> Unit")
                Text("- weight, align 등 Row 전용 Modifier 사용 가능")
                Text("- 타입 안전성과 IDE 자동완성 지원")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ============================================================
        // 3. Compound Component 패턴
        // ============================================================
        SectionTitle("3. Compound Component 패턴")

        var expanded1 by remember { mutableStateOf(false) }
        var expanded2 by remember { mutableStateOf(true) }

        // Accordion 사용 예시
        Accordion {
            Header {
                Text(
                    "프로젝트 설정",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Content {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("- 빌드 설정")
                    Text("- 린트 규칙")
                    Text("- 코드 스타일")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Accordion {
            Header {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "보안 설정",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Content {
                Column(modifier = Modifier.padding(16.dp)) {
                    SwitchItem("2단계 인증", true)
                    SwitchItem("생체 인식", false)
                    SwitchItem("자동 잠금", true)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Compound Component의 장점:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("- 부모(Accordion)가 상태(isExpanded) 관리")
                Text("- 자식(Header, Content)이 상태에 자동 접근")
                Text("- AccordionScope를 통한 상태 공유")
                Text("- 깔끔한 선언적 API")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ============================================================
        // 4. Material 컴포넌트의 Slot 활용
        // ============================================================
        SectionTitle("4. Material Slot 활용 예시")

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Scaffold의 슬롯들:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("topBar = { TopAppBar(...) }")
                Text("bottomBar = { BottomAppBar(...) }")
                Text("floatingActionButton = { FAB(...) }")
                Text("content = { padding -> ... }")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "TopAppBar의 슬롯들:",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("title = { Text(...) }")
                Text("navigationIcon = { IconButton(...) }")
                Text("actions = { IconButton(...) }")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ============================================================
// Slot API를 사용한 유연한 컴포넌트들
// ============================================================

/**
 * 유연한 정보 카드 - Slot API 사용
 *
 * 모든 UI 요소를 슬롯으로 받아 완전한 커스터마이징 가능
 */
@Composable
fun FlexibleInfoCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    action: @Composable () -> Unit = {}
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                title()
                description()
            }

            action()
        }
    }
}

/**
 * 유연한 버튼 - Scoped Slot 사용
 *
 * RowScope를 제공하여 weight, align 등 사용 가능
 */
@Composable
fun FlexibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        // RowScope를 그대로 전달
        content()
    }
}

// ============================================================
// Compound Component 패턴 구현
// ============================================================

/**
 * Accordion Scope - 상태를 자식에게 공유
 */
class AccordionScope(
    val isExpanded: Boolean,
    val onToggle: () -> Unit
)

/**
 * Accordion 컴포넌트 - 부모가 상태 관리
 */
@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    content: @Composable AccordionScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = AccordionScope(
        isExpanded = expanded,
        onToggle = { expanded = !expanded }
    )

    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            scope.content()
        }
    }
}

/**
 * Accordion Header - 클릭 시 토글
 */
@Composable
fun AccordionScope.Header(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "접기" else "펼치기"
        )
    }
}

/**
 * Accordion Content - 확장 시에만 표시
 */
@Composable
fun AccordionScope.Content(
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            content()
        }
    }
}

// ============================================================
// 헬퍼 컴포넌트들
// ============================================================

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SwitchItem(label: String, checked: Boolean) {
    var isChecked by remember { mutableStateOf(checked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
    }
}
