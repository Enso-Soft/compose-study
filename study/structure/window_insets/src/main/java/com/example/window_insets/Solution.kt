package com.example.window_insets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Solution: WindowInsets를 올바르게 처리하는 방법들
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 데모 선택 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("Scaffold") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("키보드") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("Inset 값") }
            )
        }

        when (selectedDemo) {
            0 -> Solution1_Scaffold()
            1 -> Solution2_KeyboardHandling()
            2 -> Solution3_InsetValues()
        }
    }
}

/**
 * 해결책 1: Scaffold 사용
 *
 * Scaffold는 자동으로 WindowInsets를 처리합니다.
 * paddingValues를 LazyColumn의 contentPadding으로 전달하면
 * 첫 번째/마지막 아이템이 시스템 바에 가려지지 않습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Solution1_Scaffold() {
    // 핵심 포인트 카드
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "해결: Scaffold + contentPadding",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
                    Scaffold { paddingValues ->
                        LazyColumn(
                            contentPadding = paddingValues
                        ) { ... }
                    }
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    val items = (1..30).map { "아이템 $it" }

    // 실제 WindowInsets 값을 사용하여 contentPadding 계산
    val navigationBarsBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    // Scaffold가 자동으로 insets 처리
    // 여기서는 이미 상위 Scaffold 안에 있으므로 시뮬레이션
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // contentPadding으로 상하단 여백 추가 (실제 navigationBars inset 사용)
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = navigationBarsBottom + 16.dp  // 실제 네비게이션 바 높이 + 여유 공간
        )
    ) {
        item {
            ListItem(
                modifier = Modifier.background(Color.Green.copy(alpha = 0.3f)),
                headlineContent = {
                    Text("첫 번째 아이템 (안전하게 표시됨)", fontWeight = FontWeight.Bold)
                },
                supportingContent = {
                    Text("contentPadding으로 상단 여백 확보")
                }
            )
        }

        items(items.drop(1).dropLast(1)) { item ->
            ListItem(headlineContent = { Text(item) })
            HorizontalDivider()
        }

        item {
            ListItem(
                modifier = Modifier.background(Color.Green.copy(alpha = 0.3f)),
                headlineContent = {
                    Text("마지막 아이템 (안전하게 표시됨)", fontWeight = FontWeight.Bold)
                },
                supportingContent = {
                    Text("contentPadding으로 하단 여백 확보")
                }
            )
        }
    }
}

/**
 * 해결책 2: IME (키보드) 처리
 *
 * imePadding()을 사용하면 키보드가 올라올 때
 * 자동으로 패딩이 추가되어 TextField가 가려지지 않습니다.
 */
@Composable
private fun Solution2_KeyboardHandling() {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()  // 핵심! 키보드에 대응
    ) {
        // 핵심 포인트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결: imePadding() 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()  // 키보드 대응
                        ) {
                            TextField(...)
                        }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 메시지 영역
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "메시지 영역\n\nTextField를 클릭하면 키보드가 올라오고\n입력 영역이 키보드 위로 이동합니다!",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // 입력 영역 - 키보드가 올라오면 함께 올라감
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("메시지를 입력하세요...") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { text = "" }) {
                Icon(Icons.Default.Send, contentDescription = "보내기")
            }
        }
    }
}

/**
 * 해결책 3: Inset 값 실시간 확인
 *
 * WindowInsets의 각 값을 실시간으로 확인할 수 있습니다.
 * 키보드를 열면 ime 값이 변경됩니다.
 */
@Composable
private fun Solution3_InsetValues() {
    val density = LocalDensity.current

    // 각 inset 값 읽기
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val safeDrawingTop = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
    val safeDrawingBottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()

    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Inset 값 읽기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        val height = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 현재 Inset 값들 표시
        Text(
            text = "현재 Inset 값",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        InsetValueRow("statusBars (top)", statusBarTop.toString())
        InsetValueRow("navigationBars (bottom)", navBarBottom.toString())
        InsetValueRow("ime (bottom)", imeBottom.toString())
        InsetValueRow("safeDrawing (top)", safeDrawingTop.toString())
        InsetValueRow("safeDrawing (bottom)", safeDrawingBottom.toString())

        Spacer(modifier = Modifier.height(24.dp))

        // 키보드 테스트용 TextField
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "키보드를 열어 ime 값 변화를 확인하세요",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("여기를 클릭...") }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Inset 유형 설명
        Text(
            text = "주요 Inset 유형",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        val insetTypes = listOf(
            "safeDrawing" to "시각적 겹침 방지 (권장)",
            "safeGestures" to "제스처 충돌 방지",
            "safeContent" to "safeDrawing + safeGestures",
            "systemBars" to "상태바 + 네비게이션 바",
            "ime" to "소프트웨어 키보드",
            "displayCutout" to "노치/펀치홀"
        )

        insetTypes.forEach { (name, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = name,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = desc,
                    modifier = Modifier.weight(1.5f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InsetValueRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
    HorizontalDivider()
}
