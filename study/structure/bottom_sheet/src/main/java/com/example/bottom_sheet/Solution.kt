package com.example.bottom_sheet

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
import androidx.compose.ui.unit.dp

/**
 * Solution: ModalBottomSheet로 옵션 메뉴 구현
 *
 * ModalBottomSheet는 화면 하단에서 올라오는 보조 콘텐츠 영역입니다.
 * - Boolean 상태로 간단하게 열기/닫기
 * - 드래그로 닫기 지원
 * - 모바일 UX에 최적화
 * - 엄지로 쉽게 접근 가능
 */

private enum class DemoType {
    ACTION_SHEET,
    PRODUCT_INFO,
    SORT_OPTIONS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    var currentDemo by remember { mutableStateOf<DemoType?>(null) }
    var selectedAction by remember { mutableStateOf<String?>(null) }
    var selectedSort by remember { mutableStateOf(SortOption.LATEST) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: ModalBottomSheet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "화면 하단에서 올라오는 시트로 옵션을 표시합니다. " +
                            "드래그로 닫을 수 있고, 엄지로 쉽게 접근할 수 있어 모바일 UX에 최적화되어 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 핵심 포인트
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 코드 패턴",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
var showSheet by remember { mutableStateOf(false) }

if (showSheet) {
    ModalBottomSheet(
        onDismissRequest = { showSheet = false }
    ) {
        // 시트 내용 (ColumnScope)
        ListItem(...)
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 데모 버튼들
        Text(
            text = "직접 체험해보기",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 1. 액션 시트 데모
        DemoCard(
            title = "1. 기본 액션 시트",
            description = "공유, 편집, 삭제 옵션이 있는 액션 시트",
            buttonText = "액션 시트 열기",
            result = selectedAction?.let { "선택: $it" },
            onClick = { currentDemo = DemoType.ACTION_SHEET }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2. 상품 정보 데모
        DemoCard(
            title = "2. 상품 정보 표시",
            description = "상품 상세 정보를 BottomSheet로 표시",
            buttonText = "상품 정보 보기",
            result = null,
            onClick = { currentDemo = DemoType.PRODUCT_INFO }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. 정렬 옵션 데모
        DemoCard(
            title = "3. 정렬 옵션 선택",
            description = "RadioButton으로 정렬 옵션 선택",
            buttonText = "정렬 옵션",
            result = "현재: ${selectedSort.displayName}",
            onClick = { currentDemo = DemoType.SORT_OPTIONS }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 장점 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Dialog 대비 장점",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                AdvantageItem("드래그로 닫기 가능")
                AdvantageItem("화면 하단에서 올라와 엄지로 쉽게 접근")
                AdvantageItem("옵션 목록에 최적화된 구조")
                AdvantageItem("자연스러운 모바일 UX")
            }
        }
    }

    // 각 데모별 BottomSheet
    when (currentDemo) {
        DemoType.ACTION_SHEET -> {
            ActionSheetDemo(
                onDismiss = { currentDemo = null },
                onActionSelected = { action ->
                    selectedAction = action
                    currentDemo = null
                }
            )
        }
        DemoType.PRODUCT_INFO -> {
            ProductInfoDemo(
                onDismiss = { currentDemo = null }
            )
        }
        DemoType.SORT_OPTIONS -> {
            SortOptionsDemo(
                currentSort = selectedSort,
                onSortSelected = { sort ->
                    selectedSort = sort
                    currentDemo = null
                },
                onDismiss = { currentDemo = null }
            )
        }
        null -> { /* 아무것도 표시하지 않음 */ }
    }
}

@Composable
private fun DemoCard(
    title: String,
    description: String,
    buttonText: String,
    result: String?,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onClick) {
                    Text(buttonText)
                }
                result?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvantageItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

// 데모 1: 기본 액션 시트
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionSheetDemo(
    onDismiss: () -> Unit,
    onActionSelected: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Text(
            text = "옵션 선택",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        ListItem(
            headlineContent = { Text("공유하기") },
            supportingContent = { Text("다른 앱으로 공유") },
            leadingContent = {
                Icon(Icons.Default.Share, contentDescription = null)
            },
            modifier = Modifier.clickable { onActionSelected("공유") }
        )
        ListItem(
            headlineContent = { Text("편집하기") },
            supportingContent = { Text("게시물 수정") },
            leadingContent = {
                Icon(Icons.Default.Edit, contentDescription = null)
            },
            modifier = Modifier.clickable { onActionSelected("편집") }
        )
        ListItem(
            headlineContent = { Text("삭제하기") },
            supportingContent = { Text("게시물 삭제") },
            leadingContent = {
                Icon(Icons.Default.Delete, contentDescription = null)
            },
            colors = ListItemDefaults.colors(
                headlineColor = MaterialTheme.colorScheme.error,
                supportingColor = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                leadingIconColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.clickable { onActionSelected("삭제") }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// 데모 2: 상품 정보 표시
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductInfoDemo(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "프리미엄 무선 이어폰",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "199,000원",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "고음질 오디오와 강력한 노이즈 캔슬링 기능을 갖춘 프리미엄 무선 이어폰입니다. " +
                        "최대 24시간 재생 가능하며, 빠른 충전을 지원합니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.FavoriteBorder, null)
                    Spacer(Modifier.width(4.dp))
                    Text("찜하기")
                }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ShoppingCart, null)
                    Spacer(Modifier.width(4.dp))
                    Text("장바구니")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// 데모 3: 정렬 옵션 선택
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortOptionsDemo(
    currentSort: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Text(
            text = "정렬 기준",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        SortOption.entries.forEach { option ->
            ListItem(
                headlineContent = { Text(option.displayName) },
                leadingContent = {
                    RadioButton(
                        selected = currentSort == option,
                        onClick = null  // ListItem 전체 클릭으로 처리
                    )
                },
                modifier = Modifier.clickable { onSortSelected(option) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

enum class SortOption(val displayName: String) {
    LATEST("최신순"),
    POPULAR("인기순"),
    PRICE_LOW("가격 낮은순"),
    PRICE_HIGH("가격 높은순")
}
