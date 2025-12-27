package com.example.slot_api_pattern

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 Slot API 구현
 *
 * 요구사항:
 * - AlertBanner 컴포넌트를 만드세요
 * - icon, message, action 세 개의 슬롯을 받아야 합니다
 * - Row 레이아웃으로 배치합니다
 * - action은 선택적 슬롯입니다 (기본값 = {})
 *
 * 힌트:
 * - 슬롯은 @Composable () -> Unit 타입
 * - 선택적 슬롯은 기본값 = {} 제공
 */
@Composable
fun Practice1_AlertBanner() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 기본 Slot API",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: AlertBanner 컴포넌트를 완성하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. icon: @Composable () -> Unit")
                Text("2. message: @Composable () -> Unit")
                Text("3. action: @Composable () -> Unit = {}")
                Text("4. Row로 세 슬롯을 배치")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: 아래 AlertBanner 함수를 완성하고 주석을 해제하세요

        // 사용 예시 1: 경고 배너
        /*
        AlertBanner(
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF9800)
                )
            },
            message = {
                Text("저장 공간이 부족합니다")
            },
            action = {
                TextButton(onClick = { }) {
                    Text("정리")
                }
            }
        )
        */

        Spacer(modifier = Modifier.height(8.dp))

        // 사용 예시 2: 성공 배너 (action 없음)
        /*
        AlertBanner(
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            },
            message = {
                Text("저장되었습니다!")
            }
        )
        */

        Spacer(modifier = Modifier.height(24.dp))

        // 정답 보기 카드
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = """
@Composable
fun AlertBanner(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    message: @Composable () -> Unit,
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
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f)) {
                message()
            }
            action()
        }
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// TODO: AlertBanner 함수를 여기에 구현하세요
/*
@Composable
fun AlertBanner(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    message: @Composable () -> Unit,
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
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f)) {
                message()
            }
            action()
        }
    }
}
*/

// ============================================================

/**
 * 연습 문제 2: Scoped Slot 구현
 *
 * 요구사항:
 * - ButtonBarScope 인터페이스를 만드세요
 * - Modifier.spacer() 확장 함수를 제공하세요 (weight(1f) 적용)
 * - ButtonBar 컴포넌트가 ButtonBarScope를 content에 제공하세요
 *
 * 힌트:
 * - interface ButtonBarScope : RowScope
 * - object : ButtonBarScope, RowScope by this { ... }
 */
@Composable
fun Practice2_ScopedSlot() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: Scoped Slot",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: ButtonBar에 ButtonBarScope를 제공하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. interface ButtonBarScope : RowScope")
                Text("2. fun Modifier.spacer(): Modifier = weight(1f)")
                Text("3. Row 내부에서 scope 객체 생성")
                Text("4. with(scope) { content() }")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: 아래 ButtonBar 사용 예시의 주석을 해제하세요

        /*
        ButtonBar {
            Button(onClick = { }) { Text("취소") }
            Spacer(modifier = Modifier.spacer())  // spacer() 사용!
            Button(onClick = { }) { Text("확인") }
        }
        */

        Spacer(modifier = Modifier.height(8.dp))

        /*
        ButtonBar {
            TextButton(onClick = { }) { Text("이전") }
            Spacer(modifier = Modifier.spacer())
            Text("2 / 5")
            Spacer(modifier = Modifier.spacer())
            Button(onClick = { }) { Text("다음") }
        }
        */

        Spacer(modifier = Modifier.height(24.dp))

        // 정답 보기 카드
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = """
interface ButtonBarScope : RowScope {
    fun Modifier.spacer(): Modifier
}

@Composable
fun ButtonBar(
    modifier: Modifier = Modifier,
    content: @Composable ButtonBarScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scope = object : ButtonBarScope, RowScope by this {
            override fun Modifier.spacer(): Modifier = weight(1f)
        }
        scope.content()
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// TODO: ButtonBarScope와 ButtonBar를 여기에 구현하세요
/*
interface ButtonBarScope : RowScope {
    fun Modifier.spacer(): Modifier
}

@Composable
fun ButtonBar(
    modifier: Modifier = Modifier,
    content: @Composable ButtonBarScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scope = object : ButtonBarScope, RowScope by this {
            override fun Modifier.spacer(): Modifier = weight(1f)
        }
        scope.content()
    }
}
*/

// ============================================================

/**
 * 연습 문제 3: Compound Component 패턴
 *
 * 요구사항:
 * - CollapsibleSection 컴포넌트를 만드세요
 * - CollapsibleSectionScope 클래스로 상태 공유
 * - Title, Body 두 개의 확장 함수 구현
 * - Title 클릭 시 Body가 토글되어야 합니다
 *
 * 힌트:
 * - class CollapsibleSectionScope(val isExpanded: Boolean, val onToggle: () -> Unit)
 * - fun CollapsibleSectionScope.Title(...)
 * - AnimatedVisibility 사용
 */
@Composable
fun Practice3_CompoundComponent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: Compound Component",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "TODO: CollapsibleSection을 구현하세요",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. CollapsibleSectionScope 클래스 정의")
                Text("2. isExpanded, onToggle 프로퍼티")
                Text("3. Title: clickable(onClick = onToggle)")
                Text("4. Body: AnimatedVisibility(visible = isExpanded)")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: 아래 CollapsibleSection 사용 예시의 주석을 해제하세요

        /*
        CollapsibleSection {
            Title {
                Text(
                    "자주 묻는 질문",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Body {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Q: Slot API란 무엇인가요?")
                    Text("A: Composable Lambda를 파라미터로 받는 패턴입니다.")
                }
            }
        }
        */

        Spacer(modifier = Modifier.height(8.dp))

        /*
        CollapsibleSection {
            Title {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("고급 설정")
                }
            }
            Body {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("- 캐시 정리")
                    Text("- 데이터 초기화")
                    Text("- 개발자 옵션")
                }
            }
        }
        */

        Spacer(modifier = Modifier.height(24.dp))

        // 정답 보기 카드
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(onClick = { showAnswer = !showAnswer }) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = """
class CollapsibleSectionScope(
    val isExpanded: Boolean,
    val onToggle: () -> Unit
)

@Composable
fun CollapsibleSection(
    modifier: Modifier = Modifier,
    content: @Composable CollapsibleSectionScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = CollapsibleSectionScope(
        isExpanded = expanded,
        onToggle = { expanded = !expanded }
    )

    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            scope.content()
        }
    }
}

@Composable
fun CollapsibleSectionScope.Title(
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
            imageVector = if (isExpanded)
                Icons.Default.KeyboardArrowUp
            else
                Icons.Default.KeyboardArrowDown,
            contentDescription = null
        )
    }
}

@Composable
fun CollapsibleSectionScope.Body(
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
                .background(
                    MaterialTheme.colorScheme
                        .surfaceVariant.copy(alpha = 0.5f)
                )
        ) {
            content()
        }
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// TODO: CollapsibleSectionScope, CollapsibleSection, Title, Body를 구현하세요
/*
class CollapsibleSectionScope(
    val isExpanded: Boolean,
    val onToggle: () -> Unit
)

@Composable
fun CollapsibleSection(
    modifier: Modifier = Modifier,
    content: @Composable CollapsibleSectionScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = CollapsibleSectionScope(
        isExpanded = expanded,
        onToggle = { expanded = !expanded }
    )

    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            scope.content()
        }
    }
}

@Composable
fun CollapsibleSectionScope.Title(
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
            contentDescription = null
        )
    }
}

@Composable
fun CollapsibleSectionScope.Body(
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
*/
