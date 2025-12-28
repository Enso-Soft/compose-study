package com.example.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 게시글 더보기 메뉴 (쉬움)
 *
 * IconButton과 DropdownMenu를 사용하여 기본 더보기 메뉴를 구현합니다.
 */
@Composable
fun Practice1_BasicMenu() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 요구사항 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 게시글 더보기 메뉴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        게시글의 더보기 버튼을 클릭하면 옵션 메뉴가 표시되도록 구현하세요.

                        요구사항:
                        1. MoreVert 아이콘의 IconButton
                        2. 메뉴 항목: 수정, 삭제, 공유
                        3. 각 항목 클릭 시 선택된 액션 표시
                    """.trimIndent(),
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
                Spacer(modifier = Modifier.height(4.dp))
                Text("- var expanded by remember { mutableStateOf(false) }")
                Text("- Box로 IconButton과 DropdownMenu 감싸기")
                Text("- onDismissRequest에서 expanded = false")
                Text("- onClick에서 액션 처리 후 expanded = false")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 게시글 더보기 메뉴를 구현하세요

    // 구현해야 할 내용:
    // 1. expanded 상태 변수 생성
    // 2. selectedAction 상태 변수 생성 (선택된 액션 표시용)
    // 3. 게시글 카드 UI 구현
    //    - Row: 게시글 제목 + 더보기 버튼
    //    - Box로 IconButton과 DropdownMenu 감싸기
    // 4. DropdownMenu 구현
    //    - expanded와 onDismissRequest 연결
    //    - 3개의 DropdownMenuItem (수정, 삭제, 공유)
    // 5. 선택된 액션 표시

    // 아래는 기본 UI 틀입니다. 더보기 메뉴를 추가하세요!
    var selectedAction by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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

                // TODO: 여기에 Box로 감싼 IconButton과 DropdownMenu를 추가하세요
                // Box {
                //     IconButton(onClick = { /* expanded = true */ }) {
                //         Icon(Icons.Default.MoreVert, "더보기")
                //     }
                //     DropdownMenu(
                //         expanded = expanded,
                //         onDismissRequest = { /* expanded = false */ }
                //     ) {
                //         DropdownMenuItem(
                //             text = { Text("수정") },
                //             onClick = { /* 액션 처리 */ }
                //         )
                //         // 삭제, 공유 항목 추가
                //     }
                // }

                Text(
                    text = "[더보기 버튼 추가]",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "게시글 본문 내용입니다.",
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
}

/**
 * 연습 문제 2: 설정 메뉴 with 아이콘 (중간)
 *
 * 아이콘과 Divider가 있는 설정 메뉴를 구현합니다.
 */
@Composable
fun Practice2_IconMenu() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 요구사항 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 설정 메뉴 with 아이콘",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        아이콘과 구분선이 있는 설정 메뉴를 구현하세요.

                        요구사항:
                        1. leadingIcon으로 각 항목에 아이콘 추가
                        2. HorizontalDivider로 그룹 구분
                        3. 그룹 구성:
                           - 그룹 1: 프로필, 설정
                           - 그룹 2: 도움말, 정보
                    """.trimIndent(),
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
                Spacer(modifier = Modifier.height(4.dp))
                Text("- leadingIcon = { Icon(Icons.Outlined.Person, null) }")
                Text("- HorizontalDivider()로 그룹 사이 구분")
                Text("- trailingIcon도 추가 가능 (선택)")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice2_Exercise()
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 아이콘과 Divider가 있는 설정 메뉴를 구현하세요

    // 구현해야 할 내용:
    // 1. 메뉴 항목에 leadingIcon 추가
    //    - 프로필: Icons.Outlined.Person
    //    - 설정: Icons.Outlined.Settings
    //    - 도움말: Icons.AutoMirrored.Outlined.Help
    //    - 정보: Icons.Outlined.Info
    // 2. HorizontalDivider()로 그룹 구분
    // 3. (선택) 도움말에 trailingIcon으로 외부 링크 아이콘 추가

    var selectedAction by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "설정 메뉴",
            style = MaterialTheme.typography.titleSmall
        )

        // TODO: 여기에 아이콘 메뉴를 구현하세요
        // Box {
        //     IconButton(onClick = { expanded = true }) {
        //         Icon(Icons.Default.MoreVert, "더보기")
        //     }
        //     DropdownMenu(...) {
        //         DropdownMenuItem(
        //             text = { Text("프로필") },
        //             leadingIcon = { Icon(Icons.Outlined.Person, null) },
        //             onClick = { }
        //         )
        //         DropdownMenuItem(
        //             text = { Text("설정") },
        //             leadingIcon = { Icon(Icons.Outlined.Settings, null) },
        //             onClick = { }
        //         )
        //         HorizontalDivider()
        //         // 도움말, 정보 항목 추가
        //     }
        // }

        Text(
            text = "[설정 버튼 추가]",
            color = MaterialTheme.colorScheme.primary
        )
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

/**
 * 연습 문제 3: 국가 선택 드롭다운 (어려움)
 *
 * ExposedDropdownMenuBox를 사용하여 국가 선택 UI를 구현합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_ExposedDropdown() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 요구사항 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 국가 선택 드롭다운",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        ExposedDropdownMenuBox를 사용하여
                        국가 선택 UI를 구현하세요.

                        요구사항:
                        1. 5개 국가 목록 (한국, 미국, 일본, 중국, 영국)
                        2. 선택된 국가가 TextField에 표시
                        3. 확장/축소 아이콘 표시
                    """.trimIndent(),
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
                Spacer(modifier = Modifier.height(4.dp))
                Text("- @OptIn(ExperimentalMaterial3Api::class) 필요")
                Text("- menuAnchor(MenuAnchorType.PrimaryNotEditable)")
                Text("- readOnly = true로 직접 입력 방지")
                Text("- ExposedDropdownMenuDefaults.TrailingIcon(expanded)")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice3_Exercise()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Practice3_Exercise() {
    // TODO: ExposedDropdownMenuBox로 국가 선택 UI를 구현하세요

    // 구현해야 할 내용:
    // 1. countries 리스트: listOf("한국", "미국", "일본", "중국", "영국")
    // 2. selectedCountry 상태: 선택된 국가 저장
    // 3. expanded 상태: 드롭다운 열림/닫힘
    // 4. ExposedDropdownMenuBox 구현
    //    - OutlinedTextField with menuAnchor
    //    - readOnly = true
    //    - trailingIcon with ExposedDropdownMenuDefaults.TrailingIcon
    // 5. ExposedDropdownMenu with DropdownMenuItem for each country

    val countries = listOf("한국", "미국", "일본", "중국", "영국")
    var selectedCountry by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: ExposedDropdownMenuBox 구현
        // ExposedDropdownMenuBox(
        //     expanded = expanded,
        //     onExpandedChange = { expanded = it }
        // ) {
        //     OutlinedTextField(
        //         value = selectedCountry,
        //         onValueChange = {},
        //         readOnly = true,
        //         label = { Text("국가") },
        //         modifier = Modifier
        //             .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        //             .fillMaxWidth(),
        //         trailingIcon = {
        //             ExposedDropdownMenuDefaults.TrailingIcon(expanded)
        //         }
        //     )
        //     ExposedDropdownMenu(
        //         expanded = expanded,
        //         onDismissRequest = { expanded = false }
        //     ) {
        //         countries.forEach { country ->
        //             DropdownMenuItem(
        //                 text = { Text(country) },
        //                 onClick = {
        //                     selectedCountry = country
        //                     expanded = false
        //                 }
        //             )
        //         }
        //     }
        // }

        OutlinedTextField(
            value = selectedCountry.ifEmpty { "국가를 선택하세요" },
            onValueChange = {},
            readOnly = true,
            label = { Text("국가") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "[ExposedDropdownMenuBox로 변경하세요]",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodySmall
        )

        if (selectedCountry.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "선택된 국가: $selectedCountry",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
