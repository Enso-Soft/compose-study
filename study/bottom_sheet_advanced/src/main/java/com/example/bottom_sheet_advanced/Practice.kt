package com.example.bottom_sheet_advanced

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Practice: BottomSheet 고급 활용 연습
 *
 * 3개의 연습 문제를 통해 BottomSheet 사용법을 익힙니다.
 */

// ==================== 연습 1: 기본 ModalBottomSheet 구현 ====================

/**
 * 연습 1: 기본 ModalBottomSheet 구현
 *
 * 요구사항:
 * - 버튼 클릭 시 BottomSheet 열기
 * - 3개의 옵션 표시 (공유, 링크 복사, 삭제)
 * - 옵션 클릭 시 선택된 옵션 표시 후 BottomSheet 닫기
 * - 드래그나 외부 클릭으로도 닫기 가능
 *
 * 힌트:
 * 1. rememberModalBottomSheetState() 사용
 * 2. showSheet 변수로 composition 제어
 * 3. scope.launch { sheetState.hide() } 로 부드럽게 닫기
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicModalBottomSheet() {
    // TODO 1: sheetState 생성
    // val sheetState = rememberModalBottomSheetState()

    // TODO 2: CoroutineScope 생성
    // val scope = rememberCoroutineScope()

    // TODO 3: 시트 표시 상태 변수
    // var showSheet by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "연습 1: 기본 ModalBottomSheet",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "공유, 링크 복사, 삭제 옵션이 있는 액션 시트를 만드세요.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "힌트:\n" +
                            "1. rememberModalBottomSheetState()\n" +
                            "2. showSheet 변수로 조건부 렌더링\n" +
                            "3. sheetState.hide() 후 showSheet = false",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedOption.isNotEmpty()) {
            Text(
                "선택된 옵션: $selectedOption",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // TODO 4: 시트 열기 버튼 구현
        Button(
            onClick = {
                // TODO: showSheet = true
            }
        ) {
            Text("액션 시트 열기")
        }

        // TODO 5: ModalBottomSheet 구현
        // if (showSheet) {
        //     ModalBottomSheet(
        //         onDismissRequest = { showSheet = false },
        //         sheetState = sheetState
        //     ) {
        //         Column(
        //             modifier = Modifier
        //                 .fillMaxWidth()
        //                 .padding(16.dp)
        //         ) {
        //             Text("액션 선택", style = MaterialTheme.typography.titleMedium)
        //             Spacer(modifier = Modifier.height(16.dp))
        //
        //             // 공유 옵션
        //             ListItem(
        //                 headlineContent = { Text("공유") },
        //                 leadingContent = { Icon(Icons.Default.Share, null) },
        //                 modifier = Modifier.clickable {
        //                     scope.launch {
        //                         selectedOption = "공유"
        //                         sheetState.hide()
        //                         showSheet = false
        //                     }
        //                 }
        //             )
        //
        //             // 링크 복사 옵션
        //             // TODO: 구현
        //
        //             // 삭제 옵션
        //             // TODO: 구현
        //
        //             Spacer(modifier = Modifier.height(24.dp))
        //         }
        //     }
        // }
    }

    /* ========== 정답 ==========

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Column(...) {
        Button(onClick = { showSheet = true }) {
            Text("액션 시트 열기")
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("액션 선택", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                listOf(
                    Triple("공유", Icons.Default.Share, "share"),
                    Triple("링크 복사", Icons.Default.Link, "copy"),
                    Triple("삭제", Icons.Default.Delete, "delete")
                ).forEach { (title, icon, action) ->
                    ListItem(
                        headlineContent = { Text(title) },
                        leadingContent = { Icon(icon, null) },
                        modifier = Modifier.clickable {
                            scope.launch {
                                selectedOption = title
                                sheetState.hide()
                                showSheet = false
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    ========================== */
}

// ==================== 연습 2: 3단계 높이 조절 ====================

/**
 * 연습 2: 3단계 높이 조절 (BottomSheetScaffold)
 *
 * 요구사항:
 * - BottomSheetScaffold 사용
 * - peek 상태: 100.dp 높이
 * - half 상태: 화면 50% (partialExpand)
 * - full 상태: 전체 확장 (expand)
 * - 버튼으로 각 상태 전환
 * - 현재 상태 표시
 *
 * 힌트:
 * 1. rememberBottomSheetScaffoldState() 사용
 * 2. sheetPeekHeight로 최소 높이 설정
 * 3. partialExpand(), expand() 함수 활용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_ThreeStageHeight() {
    // TODO 1: scaffoldState 생성
    // val scaffoldState = rememberBottomSheetScaffoldState()

    // TODO 2: CoroutineScope 생성
    // val scope = rememberCoroutineScope()

    var currentStateText by remember { mutableStateOf("Peek") }

    // 힌트 카드
    Card(
        modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "연습 2: 3단계 높이 조절",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "BottomSheetScaffold로 peek/half/full 3단계를 구현하세요.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "힌트:\n" +
                        "1. rememberBottomSheetScaffoldState()\n" +
                        "2. sheetPeekHeight = 100.dp\n" +
                        "3. partialExpand(), expand()",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

    // TODO 3: BottomSheetScaffold 구현
    // BottomSheetScaffold(
    //     scaffoldState = scaffoldState,
    //     sheetPeekHeight = 100.dp,
    //     sheetContent = {
    //         Column(
    //             modifier = Modifier
    //                 .fillMaxWidth()
    //                 .padding(16.dp),
    //             horizontalAlignment = Alignment.CenterHorizontally
    //         ) {
    //             // 드래그 핸들
    //             Box(
    //                 modifier = Modifier
    //                     .width(40.dp)
    //                     .height(4.dp)
    //                     .background(
    //                         MaterialTheme.colorScheme.outline,
    //                         RoundedCornerShape(2.dp)
    //                     )
    //             )
    //
    //             Spacer(modifier = Modifier.height(16.dp))
    //
    //             Text(
    //                 "현재 상태: $currentStateText",
    //                 style = MaterialTheme.typography.titleMedium
    //             )
    //
    //             Spacer(modifier = Modifier.height(16.dp))
    //
    //             // 상태 전환 버튼들
    //             Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    //                 // TODO: Peek 버튼
    //                 // TODO: Half 버튼
    //                 // TODO: Full 버튼
    //             }
    //
    //             // 콘텐츠
    //             Spacer(modifier = Modifier.height(24.dp))
    //             Text("시트 콘텐츠 영역")
    //             Spacer(modifier = Modifier.height(200.dp))
    //         }
    //     }
    // ) { innerPadding ->
    //     // 메인 콘텐츠
    //     Column(
    //         modifier = Modifier
    //             .fillMaxSize()
    //             .padding(innerPadding)
    //             .padding(16.dp)
    //     ) {
    //         Text("메인 콘텐츠 영역")
    //     }
    // }

    /* ========== 정답 ==========

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        currentStateText = when (scaffoldState.bottomSheetState.currentValue) {
            SheetValue.Hidden -> "Hidden"
            SheetValue.PartiallyExpanded -> "Half"
            SheetValue.Expanded -> "Full"
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("현재 상태: $currentStateText", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                    }) { Text("Peek/Half") }

                    Button(onClick = {
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    }) { Text("Full") }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("시트 콘텐츠 영역")
                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("메인 콘텐츠 영역")
            Text("현재 시트 상태: $currentStateText")
        }
    }

    ========================== */
}

// ==================== 연습 3: 중첩 BottomSheet + 뒤로가기 처리 ====================

/**
 * 연습 3: 중첩 BottomSheet + 뒤로가기 처리
 *
 * 요구사항:
 * - 메인 시트 열기 버튼
 * - 메인 시트 안에 "상세 보기" 버튼 -> 중첩 시트 열기
 * - 뒤로가기: 중첩 시트 먼저 닫기 -> 메인 시트 닫기
 * - 메인 시트 닫히면 중첩 시트도 함께 닫힘
 *
 * 힌트:
 * 1. ModalBottomSheetDefaults.properties(shouldDismissOnBackPress = false)
 * 2. BackHandler의 enabled 조건 활용
 * 3. LaunchedEffect로 부모 닫힘 시 자식 상태 정리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_NestedSheetWithBackHandler() {
    // TODO 1: 부모/자식 시트 상태 변수들
    // var showMainSheet by remember { mutableStateOf(false) }
    // var showDetailSheet by remember { mutableStateOf(false) }
    // val mainSheetState = rememberModalBottomSheetState()
    // val detailSheetState = rememberModalBottomSheetState()
    // val scope = rememberCoroutineScope()

    var statusMessage by remember { mutableStateOf("") }

    // TODO 2: 부모 시트가 닫히면 자식 상태도 정리
    // LaunchedEffect(showMainSheet) {
    //     if (!showMainSheet) {
    //         showDetailSheet = false
    //     }
    // }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "연습 3: 중첩 BottomSheet + 뒤로가기",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "중첩 시트에서 뒤로가기가 올바른 순서로 작동하도록 구현하세요.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "힌트:\n" +
                            "1. shouldDismissOnBackPress = false\n" +
                            "2. BackHandler enabled 조건\n" +
                            "3. 자식이 열려있으면 자식 먼저 닫기",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (statusMessage.isNotEmpty()) {
            Text(
                statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // TODO 3: 메인 시트 열기 버튼
        Button(
            onClick = {
                // TODO: showMainSheet = true
                statusMessage = "메인 시트 열림"
            }
        ) {
            Text("메인 시트 열기")
        }

        // TODO 4: 메인 시트 구현
        // if (showMainSheet) {
        //     ModalBottomSheet(
        //         onDismissRequest = {
        //             showMainSheet = false
        //             statusMessage = "메인 시트 닫힘"
        //         },
        //         sheetState = mainSheetState,
        //         properties = ModalBottomSheetDefaults.properties(
        //             shouldDismissOnBackPress = false  // 핵심!
        //         )
        //     ) {
        //         // TODO 5: 자식 시트가 열려있을 때의 BackHandler
        //         // BackHandler(enabled = showDetailSheet) { ... }
        //
        //         // TODO 6: 자식 시트가 없을 때의 BackHandler
        //         // BackHandler(enabled = !showDetailSheet) { ... }
        //
        //         Column(
        //             modifier = Modifier
        //                 .fillMaxWidth()
        //                 .padding(16.dp),
        //             horizontalAlignment = Alignment.CenterHorizontally
        //         ) {
        //             Text("메인 시트", style = MaterialTheme.typography.titleMedium)
        //             Spacer(modifier = Modifier.height(16.dp))
        //
        //             Button(onClick = {
        //                 showDetailSheet = true
        //                 statusMessage = "상세 시트 열림"
        //             }) {
        //                 Text("상세 보기")
        //             }
        //
        //             Spacer(modifier = Modifier.height(24.dp))
        //
        //             // TODO 7: 자식 시트
        //             // if (showDetailSheet) { ... }
        //         }
        //     }
        // }
    }

    /* ========== 정답 ==========

    var showMainSheet by remember { mutableStateOf(false) }
    var showDetailSheet by remember { mutableStateOf(false) }
    val mainSheetState = rememberModalBottomSheetState()
    val detailSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(showMainSheet) {
        if (!showMainSheet) {
            showDetailSheet = false
        }
    }

    Column(...) {
        Button(onClick = {
            showMainSheet = true
            statusMessage = "메인 시트 열림"
        }) {
            Text("메인 시트 열기")
        }
    }

    if (showMainSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showMainSheet = false
                statusMessage = "메인 시트 닫힘"
            },
            sheetState = mainSheetState,
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false
            )
        ) {
            BackHandler(enabled = showDetailSheet) {
                scope.launch {
                    detailSheetState.hide()
                    showDetailSheet = false
                    statusMessage = "상세 시트 닫힘 (BackHandler)"
                }
            }

            BackHandler(enabled = !showDetailSheet) {
                scope.launch {
                    mainSheetState.hide()
                    showMainSheet = false
                    statusMessage = "메인 시트 닫힘 (BackHandler)"
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("메인 시트", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    showDetailSheet = true
                    statusMessage = "상세 시트 열림"
                }) {
                    Text("상세 보기")
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (showDetailSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showDetailSheet = false
                            statusMessage = "상세 시트 닫힘"
                        },
                        sheetState = detailSheetState
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("상세 시트", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("뒤로가기를 눌러보세요!")
                            Text("이 시트만 닫히고 메인 시트는 유지됩니다.")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                scope.launch {
                                    detailSheetState.hide()
                                    showDetailSheet = false
                                    statusMessage = "상세 시트 닫힘 (버튼)"
                                }
                            }) {
                                Text("닫기")
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }

    ========================== */
}
