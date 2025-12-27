package com.example.bottom_sheet_advanced

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch

/**
 * Solution: BottomSheet 올바른 상태 관리
 *
 * 이 화면에서는 BottomSheet를 올바르게 사용하는 방법을 보여줍니다.
 *
 * 핵심 해결책:
 * 1. rememberModalBottomSheetState() 사용
 * 2. LaunchedEffect로 상태 동기화
 * 3. BackHandler로 중첩 시트 제어
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "해결책",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "rememberModalBottomSheetState()와 올바른 상태 관리 패턴을 사용합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        HorizontalDivider()

        // 해결책 1: 올바른 상태 관리
        Solution1_ProperStateManagement()

        HorizontalDivider()

        // 해결책 2: 다양한 커스터마이징
        Solution2_Customization()

        HorizontalDivider()

        // 해결책 3: 중첩 시트 + BackHandler
        Solution3_NestedSheetWithBackHandler()

        HorizontalDivider()

        // 해결책 4: 스크롤 콘텐츠
        Solution4_ScrollableContent()
    }
}

/**
 * 해결책 1: 올바른 상태 관리
 *
 * - rememberModalBottomSheetState() 사용
 * - showSheet 변수로 composition 제어
 * - sheetState로 UI 상태 제어
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Solution1_ProperStateManagement() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("") }
    var currentState by remember { mutableStateOf("Hidden") }

    // 상태 변화 추적
    LaunchedEffect(sheetState.currentValue) {
        currentState = sheetState.currentValue.name
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "해결책 1: 올바른 상태 관리",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "rememberModalBottomSheetState()를 사용하여 " +
                        "프로그래밍 방식으로 시트를 제어합니다.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "현재 상태: $currentState",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            if (selectedAction.isNotEmpty()) {
                Text(
                    "선택된 액션: $selectedAction",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { showSheet = true }) {
                Text("시트 열기 (올바른 방식)")
            }
        }
    }

    // 올바른 구현
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "액션 시트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 액션 옵션들
                val actions = listOf(
                    Triple("공유", Icons.Default.Share, "share"),
                    Triple("링크 복사", Icons.Default.Create, "copy"),
                    Triple("삭제", Icons.Default.Delete, "delete")
                )

                actions.forEach { (title, icon, action) ->
                    ListItem(
                        headlineContent = { Text(title) },
                        leadingContent = { Icon(icon, null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 프로그래밍 방식 닫기 (애니메이션 포함)
                Button(
                    onClick = {
                        scope.launch {
                            sheetState.hide()  // 애니메이션 완료까지 대기
                            showSheet = false   // composition에서 제거
                            selectedAction = "시트가 부드럽게 닫혔습니다"
                        }
                    }
                ) {
                    Text("닫기 (애니메이션 포함)")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 상태 전환 버튼들
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                if (sheetState.currentValue == SheetValue.Expanded) {
                                    sheetState.partialExpand()
                                } else {
                                    sheetState.expand()
                                }
                            }
                        }
                    ) {
                        Text(
                            if (sheetState.currentValue == SheetValue.Expanded)
                                "부분 확장"
                            else
                                "전체 확장"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        "핵심: sheetState.hide()는 suspend 함수로 " +
                                "애니메이션 완료까지 대기합니다.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * 해결책 2: 다양한 커스터마이징
 *
 * - 드래그 핸들 커스터마이징
 * - Scrim 색상 변경
 * - 시트 모양 변경
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Solution2_Customization() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var customizationType by remember { mutableStateOf("default") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "해결책 2: 커스터마이징",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "드래그 핸들, Scrim, 시트 모양 등을 커스터마이징합니다.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = customizationType == "default",
                    onClick = { customizationType = "default" },
                    label = { Text("기본") }
                )
                FilterChip(
                    selected = customizationType == "custom",
                    onClick = { customizationType = "custom" },
                    label = { Text("커스텀") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { showSheet = true }) {
                Text("시트 열기")
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            // 커스텀 드래그 핸들
            dragHandle = if (customizationType == "custom") {
                {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .width(48.dp)
                            .height(6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(3.dp)
                            )
                    )
                }
            } else null,
            // 커스텀 Scrim 색상
            scrimColor = if (customizationType == "custom") {
                Color.Black.copy(alpha = 0.7f)
            } else {
                BottomSheetDefaults.ScrimColor
            },
            // 커스텀 시트 모양
            shape = if (customizationType == "custom") {
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            } else {
                BottomSheetDefaults.ExpandedShape
            },
            // 커스텀 색상
            containerColor = if (customizationType == "custom") {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                BottomSheetDefaults.ContainerColor
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    if (customizationType == "custom") "커스텀 시트" else "기본 시트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "현재 스타일: $customizationType",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (customizationType == "custom") {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("적용된 커스터마이징:", fontWeight = FontWeight.Bold)
                            Text("- 드래그 핸들: 파란색 바")
                            Text("- Scrim: 70% 불투명")
                            Text("- 모서리: 24dp 둥글게")
                            Text("- 배경색: primaryContainer")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            showSheet = false
                        }
                    }
                ) {
                    Text("닫기")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * 해결책 3: 중첩 시트 + BackHandler
 *
 * - shouldDismissOnBackPress = false로 자체 BackHandler 비활성화
 * - BackHandler 우선순위 관리
 * - 부모 닫힘 시 자식 상태 정리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Solution3_NestedSheetWithBackHandler() {
    var showParentSheet by remember { mutableStateOf(false) }
    var showChildSheet by remember { mutableStateOf(false) }
    val parentSheetState = rememberModalBottomSheetState()
    val childSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var statusMessage by remember { mutableStateOf("") }

    // 부모 시트가 닫히면 자식 상태도 정리
    LaunchedEffect(showParentSheet) {
        if (!showParentSheet) {
            showChildSheet = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "해결책 3: 중첩 시트 + BackHandler",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "shouldDismissOnBackPress와 BackHandler를 조합하여 " +
                        "중첩 시트를 올바르게 제어합니다.",
                style = MaterialTheme.typography.bodySmall
            )

            if (statusMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    statusMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "부모: ${if (showParentSheet) "열림" else "닫힘"} / " +
                        "자식: ${if (showChildSheet) "열림" else "닫힘"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    showParentSheet = true
                    statusMessage = "부모 시트 열림 - 뒤로가기를 테스트해보세요"
                }
            ) {
                Text("부모 시트 열기")
            }
        }
    }

    if (showParentSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showParentSheet = false
                statusMessage = "부모 시트 닫힘"
            },
            sheetState = parentSheetState,
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false  // 핵심: 자체 BackHandler 비활성화
            )
        ) {
            // 자식 시트가 열려있을 때의 BackHandler
            BackHandler(enabled = showChildSheet) {
                scope.launch {
                    childSheetState.hide()
                    showChildSheet = false
                    statusMessage = "자식 시트 닫힘 (BackHandler)"
                }
            }

            // 자식 시트가 없을 때의 BackHandler
            BackHandler(enabled = !showChildSheet) {
                scope.launch {
                    parentSheetState.hide()
                    showParentSheet = false
                    statusMessage = "부모 시트 닫힘 (BackHandler)"
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "부모 시트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("핵심 포인트:", fontWeight = FontWeight.Bold)
                        Text("1. shouldDismissOnBackPress = false")
                        Text("2. BackHandler enabled 조건으로 우선순위 제어")
                        Text("3. 자식이 열려있으면 자식 먼저 닫기")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showChildSheet = true
                        statusMessage = "자식 시트 열림 - 뒤로가기를 테스트해보세요"
                    }
                ) {
                    Text("자식 시트 열기")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "뒤로가기를 누르면 자식 -> 부모 순서로 닫힙니다",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 자식 시트
                if (showChildSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showChildSheet = false
                            statusMessage = "자식 시트 닫힘 (드래그/외부 클릭)"
                        },
                        sheetState = childSheetState
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "자식 시트",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "뒤로가기를 눌러보세요!",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "이 시트만 닫히고 부모 시트는 유지됩니다.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        childSheetState.hide()
                                        showChildSheet = false
                                        statusMessage = "자식 시트 닫힘 (버튼)"
                                    }
                                }
                            ) {
                                Text("닫기")
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * 해결책 4: 스크롤 콘텐츠
 *
 * - LazyColumn을 시트 안에 배치
 * - 최대 높이 제한
 * - 중첩 스크롤 처리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Solution4_ScrollableContent() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "해결책 4: 스크롤 콘텐츠",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "LazyColumn을 시트 안에 배치하여 " +
                        "많은 콘텐츠를 스크롤할 수 있게 합니다.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { showSheet = true }) {
                Text("스크롤 시트 열기")
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 헤더 (고정)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "아이템 목록",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                showSheet = false
                            }
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                }

                HorizontalDivider()

                // 스크롤 가능한 콘텐츠
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f),  // 최대 높이 70%
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((1..50).toList()) { index ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            ListItem(
                                headlineContent = { Text("아이템 $index") },
                                supportingContent = { Text("스크롤하여 더 많은 아이템을 확인하세요") },
                                leadingContent = {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
