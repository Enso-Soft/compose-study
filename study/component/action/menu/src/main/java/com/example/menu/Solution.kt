package com.example.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Solution: DropdownMenu와 ExposedDropdownMenuBox 사용
 *
 * 핵심 포인트:
 * 1. expanded 상태로 메뉴 표시/숨김 제어
 * 2. onDismissRequest로 외부 클릭 처리
 * 3. Box로 감싸서 위치 계산
 * 4. ExposedDropdownMenuBox로 선택 UI 구현
 */
@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 데모 선택
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("기본") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("아이콘") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("선택") }
            )
            FilterChip(
                selected = selectedDemo == 3,
                onClick = { selectedDemo = 3 },
                label = { Text("필터") }
            )
        }

        when (selectedDemo) {
            0 -> BasicDropdownMenuDemo()
            1 -> IconDropdownMenuDemo()
            2 -> ExposedDropdownDemo()
            3 -> FilterableDropdownDemo()
        }
    }
}

/**
 * 데모 1: 기본 DropdownMenu
 */
@Composable
fun BasicDropdownMenuDemo() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "해결책: DropdownMenu 사용",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        DropdownMenu를 사용하면 메뉴가 오버레이로 표시되어
                        레이아웃에 영향을 주지 않습니다.

                        - expanded: 메뉴 표시 여부
                        - onDismissRequest: 외부 클릭 시 호출

                        더보기 버튼을 눌러보세요!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "데모: 기본 DropdownMenu",
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

                    // 핵심: Box로 감싸기
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "더보기"
                            )
                        }

                        // DropdownMenu - 오버레이로 표시됨
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("수정") },
                                onClick = {
                                    selectedAction = "수정"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("삭제") },
                                onClick = {
                                    selectedAction = "삭제"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("공유") },
                                onClick = {
                                    selectedAction = "공유"
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // 이 콘텐츠는 밀리지 않음!
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "게시글 본문 내용입니다. DropdownMenu를 사용하면 이 콘텐츠가 밀리지 않고 " +
                            "메뉴가 오버레이로 표시됩니다. 외부를 탭하면 메뉴가 자동으로 닫힙니다.",
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
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "올바른 코드",
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
var expanded by remember {
    mutableStateOf(false)
}

Box {  // Box로 감싸기
    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(Icons.Default.MoreVert, "더보기")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("수정") },
            onClick = {
                // 액션 수행
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("삭제") },
            onClick = { expanded = false }
        )
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
    }
}

/**
 * 데모 2: 아이콘과 Divider가 있는 메뉴
 */
@Composable
fun IconDropdownMenuDemo() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "아이콘과 Divider 메뉴",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        DropdownMenuItem에 아이콘을 추가하고
                        HorizontalDivider로 그룹을 구분할 수 있습니다.

                        - leadingIcon: 시작 아이콘
                        - trailingIcon: 끝 아이콘
                        - HorizontalDivider: 그룹 구분
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "설정 메뉴",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, "더보기")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            // 그룹 1: 계정
                            DropdownMenuItem(
                                text = { Text("프로필") },
                                leadingIcon = {
                                    Icon(Icons.Filled.Person, null)
                                },
                                onClick = {
                                    selectedAction = "프로필"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("설정") },
                                leadingIcon = {
                                    Icon(Icons.Filled.Settings, null)
                                },
                                onClick = {
                                    selectedAction = "설정"
                                    expanded = false
                                }
                            )

                            HorizontalDivider()

                            // 그룹 2: 도움말
                            DropdownMenuItem(
                                text = { Text("도움말") },
                                leadingIcon = {
                                    Icon(Icons.Filled.Info, null)
                                },
                                trailingIcon = {
                                    Icon(Icons.Filled.Share, null)
                                },
                                onClick = {
                                    selectedAction = "도움말"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("정보") },
                                leadingIcon = {
                                    Icon(Icons.Filled.Info, null)
                                },
                                onClick = {
                                    selectedAction = "정보"
                                    expanded = false
                                }
                            )

                            HorizontalDivider()

                            // 그룹 3: 로그아웃
                            DropdownMenuItem(
                                text = { Text("로그아웃") },
                                leadingIcon = {
                                    Icon(Icons.Filled.ExitToApp, null)
                                },
                                onClick = {
                                    selectedAction = "로그아웃"
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (selectedAction.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "선택된 항목: $selectedAction",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "아이콘 메뉴 코드",
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
DropdownMenuItem(
    text = { Text("설정") },
    leadingIcon = {
        Icon(Icons.Filled.Settings, null)
    },
    onClick = { /* 액션 */ }
)

HorizontalDivider()

DropdownMenuItem(
    text = { Text("도움말") },
    leadingIcon = {
        Icon(Icons.Filled.Info, null)
    },
    trailingIcon = {
        Icon(Icons.Filled.Share, null)
    },
    onClick = { /* 액션 */ }
)
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

/**
 * 데모 3: ExposedDropdownMenuBox (선택 드롭다운)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownDemo() {
    val countries = listOf("한국", "미국", "일본", "중국", "영국")
    var selectedCountry by remember { mutableStateOf(countries[0]) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ExposedDropdownMenuBox",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        TextField와 결합된 선택 드롭다운입니다.
                        선택된 값이 TextField에 표시됩니다.

                        핵심:
                        - menuAnchor() 수정자 필수
                        - readOnly = true로 직접 입력 방지
                        - ExposedDropdownMenu 사용
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "국가 선택",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCountry,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("국가") },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text(country) },
                                onClick = {
                                    selectedCountry = country
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "선택된 국가: $selectedCountry",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ExposedDropdownMenuBox 코드",
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
@OptIn(ExperimentalMaterial3Api::class)
ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = it }
) {
    OutlinedTextField(
        value = selectedOption,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.menuAnchor(
            MenuAnchorType
                .PrimaryNotEditable
        ),
        trailingIcon = {
            ExposedDropdownMenuDefaults
                .TrailingIcon(expanded)
        }
    )

    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = { Text(option) },
                onClick = {
                    selectedOption = option
                    expanded = false
                }
            )
        }
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
    }
}

/**
 * 데모 4: 필터링 가능한 드롭다운
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterableDropdownDemo() {
    val allCities = listOf(
        "서울", "부산", "대구", "인천", "광주",
        "대전", "울산", "세종", "수원", "성남",
        "고양", "용인", "창원", "청주", "전주"
    )
    var searchText by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val filteredCities = remember(searchText) {
        if (searchText.isEmpty()) allCities
        else allCities.filter { it.contains(searchText) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "검색 가능한 드롭다운",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        많은 옵션이 있을 때 검색으로 필터링할 수 있습니다.
                        TextField에 직접 입력하여 옵션을 필터링합니다.

                        핵심:
                        - readOnly 제거
                        - PrimaryEditable 사용
                        - 입력값으로 필터링
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 데모
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "도시 검색",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            expanded = true
                        },
                        label = { Text("도시 검색") },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryEditable)
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    if (filteredCities.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            filteredCities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        searchText = city
                                        selectedCity = city
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }

                if (selectedCity.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "선택된 도시: $selectedCity",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // 주의사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DropdownMenu vs ExposedDropdownMenuBox",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "DropdownMenu",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "- 액션 메뉴\n- IconButton 트리거\n- 수정/삭제/공유",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "ExposedDropdown",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "- 값 선택\n- TextField 트리거\n- 국가/정렬 선택",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
