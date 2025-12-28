package com.example.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 1: 기본 액션 시트
 *
 * TODO: 버튼 클릭 시 BottomSheet를 표시하세요.
 * - 3가지 옵션: 편집, 공유, 삭제
 * - 각 옵션에 아이콘 포함
 * - 옵션 클릭 시 시트 닫고 선택된 옵션 표시
 *
 * 힌트:
 * - var showSheet by remember { mutableStateOf(false) }
 * - if (showSheet) { ModalBottomSheet(...) { } }
 * - ListItem(headlineContent = {...}, leadingContent = {...})
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BasicActionSheet() {
    // TODO: 시트 표시 상태 선언
    // var showSheet by remember { mutableStateOf(false) }

    // TODO: 선택된 옵션 상태 선언
    // var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 액션 시트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "버튼 클릭 시 편집, 공유, 삭제 옵션이 있는 BottomSheet를 표시하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: showSheet 상태, ModalBottomSheet, ListItem",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: 버튼 추가
        // Button(onClick = { showSheet = true }) { Text("옵션 보기") }
        Text(
            text = "TODO: 여기에 버튼을 추가하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        // TODO: 선택된 옵션 표시
        // selectedOption?.let { Text("선택: $it") }

        // TODO: BottomSheet 추가
        // if (showSheet) {
        //     ModalBottomSheet(onDismissRequest = { showSheet = false }) {
        //         ListItem(
        //             headlineContent = { Text("편집") },
        //             leadingContent = { Icon(Icons.Default.Edit, null) },
        //             modifier = Modifier.clickable {
        //                 selectedOption = "편집"
        //                 showSheet = false
        //             }
        //         )
        //         // ... 공유, 삭제 옵션 추가
        //         Spacer(Modifier.height(32.dp))
        //     }
        // }
    }
}

/**
 * 연습 2: 상품 상세 정보
 *
 * TODO: 상품 카드 클릭 시 해당 상품의 상세 정보를 BottomSheet로 표시하세요.
 * - 상품 목록에서 카드 클릭
 * - 상세 정보: 이름, 가격, 설명
 * - "장바구니 담기" 버튼
 *
 * 힌트:
 * - var selectedProduct by remember { mutableStateOf<Product?>(null) }
 * - selectedProduct?.let { product -> ModalBottomSheet(...) }
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_ProductDetail() {
    // 샘플 상품 데이터
    val products = remember {
        listOf(
            Product("무선 이어폰", 199000, "고음질 노이즈 캔슬링 이어폰"),
            Product("스마트 워치", 349000, "건강 모니터링과 알림 기능"),
            Product("보조 배터리", 49000, "20000mAh 대용량 휴대용 충전기")
        )
    }

    // TODO: 선택된 상품 상태 선언
    // var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 상품 상세 정보",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "상품 카드 클릭 시 해당 상품의 상세 정보를 BottomSheet로 표시하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: selectedProduct 상태, selectedProduct?.let { }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 상품 목록
        products.forEach { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                // TODO: .clickable { selectedProduct = product }
            ) {
                ListItem(
                    headlineContent = { Text(product.name) },
                    supportingContent = { Text("${product.price}원") },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                    }
                )
            }
        }

        // TODO: BottomSheet 추가
        // selectedProduct?.let { product ->
        //     ModalBottomSheet(onDismissRequest = { selectedProduct = null }) {
        //         Column(modifier = Modifier.padding(16.dp)) {
        //             Text(product.name, style = ...)
        //             Text("${product.price}원", style = ...)
        //             Text(product.description)
        //             Button(onClick = { selectedProduct = null }) {
        //                 Text("장바구니 담기")
        //             }
        //         }
        //         Spacer(Modifier.height(32.dp))
        //     }
        // }
    }
}

data class Product(
    val name: String,
    val price: Int,
    val description: String
)

/**
 * 연습 3: 정렬 옵션 선택기
 *
 * TODO: 목록 화면에서 정렬 버튼 클릭 시 정렬 옵션 BottomSheet를 표시하세요.
 * - 정렬 옵션: 최신순, 인기순, 이름순, 가격순
 * - 현재 선택된 옵션을 RadioButton으로 표시
 * - 옵션 선택 시 시트 닫고 선택 반영
 *
 * 힌트:
 * - var showSortSheet by remember { mutableStateOf(false) }
 * - var currentSort by remember { mutableStateOf(SortType.LATEST) }
 * - RadioButton(selected = currentSort == option, onClick = null)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_SortOptions() {
    // 샘플 아이템 데이터
    val items = remember {
        listOf("항목 1", "항목 2", "항목 3", "항목 4", "항목 5")
    }

    // TODO: 시트 표시 상태 선언
    // var showSortSheet by remember { mutableStateOf(false) }

    // TODO: 현재 정렬 옵션 상태 선언
    // var currentSort by remember { mutableStateOf(SortType.LATEST) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 정렬 옵션 선택기",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "정렬 버튼 클릭 시 RadioButton으로 옵션을 선택하는 BottomSheet를 표시하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: SortType enum, RadioButton, ListItem",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 헤더 (정렬 버튼 포함)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "아이템 목록",
                style = MaterialTheme.typography.titleMedium
            )

            // TODO: 정렬 버튼
            // TextButton(onClick = { showSortSheet = true }) {
            //     Icon(Icons.Default.Sort, null)
            //     Spacer(Modifier.width(4.dp))
            //     Text(currentSort.displayName)
            // }
            Text(
                text = "TODO: 정렬 버튼",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 아이템 목록
        items.forEach { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                ListItem(
                    headlineContent = { Text(item) }
                )
            }
        }

        // TODO: 정렬 옵션 BottomSheet 추가
        // if (showSortSheet) {
        //     ModalBottomSheet(onDismissRequest = { showSortSheet = false }) {
        //         Text("정렬 기준", ...)
        //
        //         SortType.entries.forEach { sortType ->
        //             ListItem(
        //                 headlineContent = { Text(sortType.displayName) },
        //                 leadingContent = {
        //                     RadioButton(
        //                         selected = currentSort == sortType,
        //                         onClick = null
        //                     )
        //                 },
        //                 modifier = Modifier.clickable {
        //                     currentSort = sortType
        //                     showSortSheet = false
        //                 }
        //             )
        //         }
        //         Spacer(Modifier.height(32.dp))
        //     }
        // }
    }
}

enum class SortType(val displayName: String) {
    LATEST("최신순"),
    POPULAR("인기순"),
    NAME("이름순"),
    PRICE("가격순")
}

// ============================================================
// 정답 코드 (주석 처리됨 - 연습 후 참고용)
// ============================================================

/*
// 연습 1 정답
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_Answer() {
    var showSheet by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { showSheet = true }) {
            Text("옵션 보기")
        }

        selectedOption?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text("선택: $it", color = MaterialTheme.colorScheme.primary)
        }

        if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }) {
                ListItem(
                    headlineContent = { Text("편집") },
                    leadingContent = { Icon(Icons.Default.Edit, null) },
                    modifier = Modifier.clickable {
                        selectedOption = "편집"
                        showSheet = false
                    }
                )
                ListItem(
                    headlineContent = { Text("공유") },
                    leadingContent = { Icon(Icons.Default.Share, null) },
                    modifier = Modifier.clickable {
                        selectedOption = "공유"
                        showSheet = false
                    }
                )
                ListItem(
                    headlineContent = { Text("삭제") },
                    leadingContent = { Icon(Icons.Default.Delete, null) },
                    modifier = Modifier.clickable {
                        selectedOption = "삭제"
                        showSheet = false
                    }
                )
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// 연습 2 정답
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_Answer() {
    val products = remember {
        listOf(
            Product("무선 이어폰", 199000, "고음질 노이즈 캔슬링 이어폰"),
            Product("스마트 워치", 349000, "건강 모니터링과 알림 기능"),
            Product("보조 배터리", 49000, "20000mAh 대용량 휴대용 충전기")
        )
    }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        products.forEach { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedProduct = product }
            ) {
                ListItem(
                    headlineContent = { Text(product.name) },
                    supportingContent = { Text("${product.price}원") },
                    trailingContent = { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) }
                )
            }
        }

        selectedProduct?.let { product ->
            ModalBottomSheet(onDismissRequest = { selectedProduct = null }) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(product.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${product.price}원",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(product.description)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { selectedProduct = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ShoppingCart, null)
                        Spacer(Modifier.width(8.dp))
                        Text("장바구니 담기")
                    }
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// 연습 3 정답
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_Answer() {
    val items = remember { listOf("항목 1", "항목 2", "항목 3", "항목 4", "항목 5") }
    var showSortSheet by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf(SortType.LATEST) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("아이템 목록", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { showSortSheet = true }) {
                Icon(Icons.Default.Sort, null)
                Spacer(Modifier.width(4.dp))
                Text(currentSort.displayName)
            }
        }

        items.forEach { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                ListItem(headlineContent = { Text(item) })
            }
        }

        if (showSortSheet) {
            ModalBottomSheet(onDismissRequest = { showSortSheet = false }) {
                Text(
                    "정렬 기준",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                SortType.entries.forEach { sortType ->
                    ListItem(
                        headlineContent = { Text(sortType.displayName) },
                        leadingContent = {
                            RadioButton(
                                selected = currentSort == sortType,
                                onClick = null
                            )
                        },
                        modifier = Modifier.clickable {
                            currentSort = sortType
                            showSortSheet = false
                        }
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
*/
