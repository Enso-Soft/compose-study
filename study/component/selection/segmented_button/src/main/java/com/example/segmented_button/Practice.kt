package com.example.segmented_button

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
 * 연습문제 1: 보기 모드 선택 (쉬움)
 *
 * SingleChoiceSegmentedButtonRow를 사용하여
 * 리스트/그리드/갤러리 보기 모드를 선택하는 UI를 만드세요.
 *
 * 요구사항:
 * - 3가지 보기 모드: 리스트, 그리드, 갤러리
 * - 아이콘만 사용 (텍스트 없이)
 *   - 리스트: Icons.Default.ViewList
 *   - 그리드: Icons.Default.GridView
 *   - 갤러리: Icons.Default.PhotoLibrary
 * - 선택된 모드에 따라 하단에 "현재 모드: {모드명}" 표시
 *
 * 힌트:
 * - mutableIntStateOf로 선택된 인덱스 관리
 * - SegmentedButtonDefaults.itemShape(index, count) 사용
 * - SegmentedButton의 content에 Icon 컴포저블 배치
 */
@Composable
fun Exercise1_ViewModeSelector() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 1: 보기 모드 선택 (쉬움)",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 리스트/그리드/갤러리 3가지 모드")
                Text("2. 아이콘만 사용 (ViewList, GridView, PhotoLibrary)")
                Text("3. 선택된 모드명을 하단에 표시")
            }
        }

        HorizontalDivider()

        // TODO: 여기에 SingleChoiceSegmentedButtonRow를 구현하세요
        // 힌트:
        // val options = listOf(
        //     Icons.Default.ViewList to "리스트",
        //     Icons.Default.GridView to "그리드",
        //     Icons.Default.PhotoLibrary to "갤러리"
        // )
        // var selectedIndex by remember { mutableIntStateOf(0) }

        Text(
            text = "여기에 SegmentedButton을 구현하세요",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )

        // TODO: 선택된 모드명 표시
        // Text("현재 모드: ${options[selectedIndex].second}")
    }
}

/**
 * 연습문제 2: 다중 필터 선택 (중간)
 *
 * MultiChoiceSegmentedButtonRow를 사용하여
 * 여러 필터를 동시에 선택할 수 있는 UI를 만드세요.
 *
 * 요구사항:
 * - 3가지 필터: "인기", "최신", "할인"
 * - 여러 필터를 동시에 선택 가능
 * - 선택된 필터들을 하단에 표시
 *   - 예: "적용된 필터: 인기, 최신"
 *   - 아무것도 선택 안 됨: "필터 없음"
 * - 선택 시 체크 아이콘 표시 (SegmentedButtonDefaults.Icon 사용)
 *
 * 힌트:
 * - remember { mutableStateListOf(false, false, false) }로 각 필터 상태 관리
 * - checked와 onCheckedChange 파라미터 사용
 * - filterIndexed와 joinToString으로 선택된 필터 문자열 생성
 */
@Composable
fun Exercise2_MultiFilter() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 2: 다중 필터 선택 (중간)",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 인기/최신/할인 3가지 필터")
                Text("2. 여러 필터 동시 선택 가능")
                Text("3. 선택된 필터들을 쉼표로 구분하여 표시")
                Text("4. 선택 시 체크 아이콘 표시")
            }
        }

        HorizontalDivider()

        // TODO: 여기에 MultiChoiceSegmentedButtonRow를 구현하세요
        // 힌트:
        // val options = listOf("인기", "최신", "할인")
        // val selectedOptions = remember { mutableStateListOf(false, false, false) }
        //
        // MultiChoiceSegmentedButtonRow {
        //     options.forEachIndexed { index, label ->
        //         SegmentedButton(
        //             checked = selectedOptions[index],
        //             onCheckedChange = { selectedOptions[index] = it },
        //             shape = SegmentedButtonDefaults.itemShape(index, options.size),
        //             icon = { SegmentedButtonDefaults.Icon(active = selectedOptions[index]) }
        //         ) {
        //             Text(label)
        //         }
        //     }
        // }

        Text(
            text = "여기에 MultiChoiceSegmentedButtonRow를 구현하세요",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )

        // TODO: 선택된 필터 표시
        // val selectedFilters = options.filterIndexed { index, _ -> selectedOptions[index] }
        // Text(
        //     text = if (selectedFilters.isEmpty()) "필터 없음"
        //            else "적용된 필터: ${selectedFilters.joinToString(", ")}"
        // )
    }
}

/**
 * 연습문제 3: 완전한 설정 화면 (어려움)
 *
 * 여러 SegmentedButton을 조합하여 실제 앱의 설정 화면을 만드세요.
 *
 * 요구사항:
 * 1. 테마 설정 (단일 선택)
 *    - 옵션: "라이트", "다크", "시스템"
 *
 * 2. 알림 설정 (다중 선택)
 *    - 옵션: "푸시", "이메일", "SMS"
 *
 * 3. 언어 설정 (단일 선택)
 *    - 옵션: "한국어", "English", "日本語"
 *
 * 4. 각 설정 그룹에 제목 텍스트 추가
 *
 * 5. 하단에 "설정 저장" 버튼 추가
 *    - 클릭 시 Snackbar로 현재 설정 상태 표시
 *    - 예: "테마: 다크, 알림: 푸시, 이메일, 언어: 한국어"
 *
 * 힌트:
 * - 각 설정 그룹을 별도 Composable로 분리하면 깔끔합니다
 * - Scaffold + SnackbarHost 활용
 * - rememberCoroutineScope()로 Snackbar 표시
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise3_SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "연습 3: 설정 화면 (어려움)",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 테마 설정: 라이트/다크/시스템 (단일)")
                Text("2. 알림 설정: 푸시/이메일/SMS (다중)")
                Text("3. 언어 설정: 한국어/English/日本語 (단일)")
                Text("4. 각 그룹에 제목 추가")
                Text("5. 설정 저장 버튼 + Snackbar")
            }
        }

        HorizontalDivider()

        // TODO: 테마 설정 구현
        // 섹션 제목과 SingleChoiceSegmentedButtonRow

        Text(
            text = "테마 설정",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "여기에 테마 선택 SegmentedButton 구현",
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: 알림 설정 구현
        // 섹션 제목과 MultiChoiceSegmentedButtonRow

        Text(
            text = "알림 설정",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "여기에 알림 선택 SegmentedButton 구현",
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(8.dp))

        // TODO: 언어 설정 구현
        // 섹션 제목과 SingleChoiceSegmentedButtonRow

        Text(
            text = "언어 설정",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "여기에 언어 선택 SegmentedButton 구현",
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 설정 저장 버튼
        // Button + Snackbar

        Button(
            onClick = {
                // TODO: Snackbar 표시
                // 현재 설정 상태를 문자열로 조합하여 표시
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("설정 저장")
        }

        // 힌트: Scaffold와 SnackbarHost를 사용하거나,
        // 간단하게 Text로 상태를 표시할 수도 있습니다.

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "구현 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 테마 설정 상태
var themeIndex by remember { mutableIntStateOf(2) }
val themeOptions = listOf("라이트", "다크", "시스템")

// 알림 설정 상태
val notificationOptions = remember {
    mutableStateListOf(true, false, false)
}
val notificationLabels = listOf("푸시", "이메일", "SMS")

// 언어 설정 상태
var languageIndex by remember { mutableIntStateOf(0) }
val languageOptions = listOf("한국어", "English", "日本語")

// Snackbar
val snackbarHostState = remember { SnackbarHostState() }
val scope = rememberCoroutineScope()

// 저장 버튼 클릭 시
scope.launch {
    val message = buildString {
        append("테마: ${"$"}{themeOptions[themeIndex]}, ")
        append("알림: ...")
        append("언어: ${"$"}{languageOptions[languageIndex]}")
    }
    snackbarHostState.showSnackbar(message)
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}
