package com.example.bottom_sheet_advanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: BottomSheet 상태 관리 복잡성
 *
 * 이 화면에서는 BottomSheet를 잘못 사용할 때 발생하는 문제들을 보여줍니다.
 *
 * 문제 상황:
 * 1. Boolean 상태만으로 시트 제어 시 상태 불일치
 * 2. 중첩 시트에서의 BackHandler 충돌
 * 3. 프로그래밍 방식 제어의 어려움
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemScreen() {
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
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "문제 상황",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "아래 예제들은 BottomSheet를 잘못 사용할 때 발생하는 문제들을 보여줍니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        HorizontalDivider()

        // 문제 1: 단순 Boolean 상태 관리
        Problem1_SimpleBooleanState()

        HorizontalDivider()

        // 문제 2: 중첩 시트 상태 충돌
        Problem2_NestedSheetConflict()
    }
}

/**
 * 문제 1: 단순 Boolean 상태 관리
 *
 * Boolean만으로 BottomSheet를 제어할 때의 한계:
 * - 프로그래밍 방식 show/hide 제어 불가
 * - 애니메이션 완료 감지 불가
 * - 중간 상태(PartiallyExpanded) 활용 불가
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Problem1_SimpleBooleanState() {
    // 문제: Boolean만으로 상태 관리
    var isSheetOpen by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }
    var closeMethod by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "문제 1: 단순 Boolean 상태 관리",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Boolean만으로 시트를 제어합니다. " +
                        "프로그래밍 방식 제어나 상태 동기화가 어렵습니다.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "시트 열기 횟수: ${clickCount}회",
                style = MaterialTheme.typography.bodySmall
            )
            if (closeMethod.isNotEmpty()) {
                Text(
                    "마지막 닫기 방법: $closeMethod",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    isSheetOpen = true
                    clickCount++
                }
            ) {
                Text("시트 열기 (Boolean만 사용)")
            }
        }
    }

    // 문제가 있는 구현: sheetState를 사용하지 않음
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                // 드래그로 닫을 때만 호출됨
                closeMethod = "드래그/외부 클릭"
                isSheetOpen = false
            }
            // sheetState 미사용 -> 기본 sheetState 생성됨
            // 문제: 프로그래밍 방식으로 hide() 호출 불가
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

                ListItem(
                    headlineContent = { Text("공유") },
                    leadingContent = { Icon(Icons.Default.Share, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                ListItem(
                    headlineContent = { Text("삭제") },
                    leadingContent = { Icon(Icons.Default.Delete, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 문제: sheetState가 없어서 프로그래밍 방식 닫기 불가
                Button(
                    onClick = {
                        // 이렇게 하면 애니메이션 없이 바로 사라짐
                        closeMethod = "버튼 클릭 (애니메이션 없음)"
                        isSheetOpen = false
                    }
                ) {
                    Text("닫기 (애니메이션 없이)")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "문제: sheetState.hide()를 호출할 수 없어 " +
                            "부드러운 닫기 애니메이션 구현 불가",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * 문제 2: 중첩 시트 상태 충돌
 *
 * 중첩된 BottomSheet에서 발생하는 문제:
 * - BackHandler 우선순위 혼란
 * - 부모/자식 시트 상태 동기화 어려움
 * - 뒤로가기 시 어떤 시트가 닫혀야 하는지 불명확
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Problem2_NestedSheetConflict() {
    // 문제: 독립적인 Boolean 상태들
    var showParentSheet by remember { mutableStateOf(false) }
    var showChildSheet by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "문제 2: 중첩 시트 상태 충돌",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "중첩된 BottomSheet에서 BackHandler와 상태 관리가 혼란스럽습니다. " +
                        "뒤로가기를 누르면 어떤 시트가 닫힐지 예측하기 어렵습니다.",
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
                "부모 시트: ${if (showParentSheet) "열림" else "닫힘"} / " +
                        "자식 시트: ${if (showChildSheet) "열림" else "닫힘"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    showParentSheet = true
                    statusMessage = "부모 시트 열림"
                }
            ) {
                Text("부모 시트 열기")
            }
        }
    }

    // 문제가 있는 중첩 시트 구현
    if (showParentSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                // 문제: 자식 시트가 열려있어도 부모가 닫힐 수 있음
                showParentSheet = false
                // 자식 상태가 남아있을 수 있음!
                statusMessage = "부모 시트 닫힘 (자식 상태: ${if (showChildSheet) "열림!" else "닫힘"})"
            }
            // 문제: 자체 BackHandler가 있어 제어 어려움
        ) {
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

                Text(
                    "이 시트 안에서 다른 시트를 열 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showChildSheet = true
                        statusMessage = "자식 시트 열림"
                    }
                ) {
                    Text("자식 시트 열기")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "문제: 뒤로가기를 누르면 부모가 먼저 닫히거나, " +
                            "자식이 열려있는데 부모가 닫힐 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 자식 시트 (문제 있는 구현)
                if (showChildSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showChildSheet = false
                            statusMessage = "자식 시트 닫힘"
                        }
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
                                "뒤로가기를 눌러보세요. " +
                                        "어떤 시트가 닫히나요?",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "문제: BackHandler 우선순위가 불명확합니다. " +
                                        "Material3의 자체 BackHandler와 충돌할 수 있습니다.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}
