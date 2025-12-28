package com.example.progress_indicator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Practice: Progress Indicator 연습 문제
 *
 * 3가지 난이도의 연습 문제를 통해 Progress Indicator 사용법을 익힙니다.
 */

// ========================================
// 연습 1: 간단한 로딩 표시 (쉬움)
// ========================================

@Composable
fun Practice1_BasicLoading() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExerciseHeader(
                title = "연습 1: 간단한 로딩 표시",
                difficulty = "쉬움"
            )

            Text(
                text = "버튼을 누르면 3초간 로딩 후 완료 메시지를 표시하세요",
                style = MaterialTheme.typography.bodyMedium
            )

            RequirementsList(
                requirements = listOf(
                    "isLoading 상태를 remember { mutableStateOf(false) }로 관리",
                    "버튼 클릭 시 isLoading = true",
                    "LaunchedEffect로 3초 후 isLoading = false",
                    "isLoading이 true면 CircularProgressIndicator 표시",
                    "로딩 완료 후 \"로딩 완료!\" 텍스트 표시"
                )
            )

            HintCard(
                hints = listOf(
                    "LaunchedEffect(isLoading) { if (isLoading) { delay(3000); isLoading = false } }",
                    "when { isLoading -> CircularProgressIndicator() }",
                    "Button(enabled = !isLoading)"
                )
            )

            // TODO: 여기에 로딩 표시를 구현하세요
            // 1. isLoading, hasLoaded 상태 선언
            // 2. LaunchedEffect로 3초 딜레이
            // 3. Button 클릭 시 isLoading = true
            // 4. 조건부로 CircularProgressIndicator 또는 텍스트 표시

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO: 버튼 + CircularProgressIndicator + 완료 메시지 구현",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ========================================
// 연습 2: 진행률 표시 (중간)
// ========================================

@Composable
fun Practice2_ProgressDisplay() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExerciseHeader(
                title = "연습 2: 진행률 표시",
                difficulty = "중간"
            )

            Text(
                text = "버튼을 누르면 0%에서 100%까지 진행률을 표시하세요",
                style = MaterialTheme.typography.bodyMedium
            )

            RequirementsList(
                requirements = listOf(
                    "progress 상태를 remember { mutableFloatStateOf(0f) }로 관리",
                    "rememberCoroutineScope()로 코루틴 스코프 획득",
                    "버튼 클릭 시 코루틴에서 progress를 0.01씩 증가 (delay 30ms)",
                    "LinearProgressIndicator(progress = { progress }) 사용",
                    "퍼센트 텍스트 표시: \"\${(progress * 100).toInt()}%\""
                )
            )

            HintCard(
                hints = listOf(
                    "scope.launch { while (progress < 1f) { progress += 0.01f; delay(30) } }",
                    "LinearProgressIndicator(progress = { progress })",
                    "Text(\"\${(progress * 100).toInt()}%\")"
                )
            )

            // TODO: 여기에 진행률 표시를 구현하세요
            // 1. progress, isRunning 상태 선언
            // 2. rememberCoroutineScope() 사용
            // 3. 버튼 클릭 시 scope.launch로 progress 증가
            // 4. LinearProgressIndicator + 퍼센트 텍스트 표시

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TODO: 버튼 + LinearProgressIndicator + 퍼센트 텍스트 구현",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ========================================
// 연습 3: 파일 다운로드 시뮬레이션 (어려움)
// ========================================

/**
 * 다운로드 상태 enum (이 enum을 사용하세요)
 */
enum class DownloadState {
    Idle,
    Downloading,
    Completed
}

@Composable
fun Practice3_FileDownload() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ExerciseHeader(
                title = "연습 3: 파일 다운로드 시뮬레이션",
                difficulty = "어려움"
            )

            Text(
                text = "다운로드 버튼 → 진행률 표시 → 완료 메시지",
                style = MaterialTheme.typography.bodyMedium
            )

            RequirementsList(
                requirements = listOf(
                    "DownloadState enum 사용: Idle, Downloading, Completed",
                    "state, progress 상태 관리",
                    "Idle 상태: \"다운로드\" 버튼 표시",
                    "Downloading 상태: LinearProgressIndicator + 파일 크기 표시",
                    "Completed 상태: 체크 아이콘 + \"다운로드 완료!\" 메시지"
                )
            )

            HintCard(
                hints = listOf(
                    "var state by remember { mutableStateOf(DownloadState.Idle) }",
                    "when (state) { Idle -> 버튼; Downloading -> 진행률; Completed -> 완료 }",
                    "다운로드 완료 시 state = DownloadState.Completed",
                    "\"다시 다운로드\" 버튼으로 state = Idle"
                )
            )

            // TODO: 여기에 파일 다운로드 UI를 구현하세요
            // 1. state (DownloadState), progress 상태 선언
            // 2. 파일 정보 Card (파일명, 크기)
            // 3. when(state)로 상태별 UI 분기
            // 4. Idle: 다운로드 버튼
            // 5. Downloading: LinearProgressIndicator + 진행률 텍스트
            // 6. Completed: 체크 아이콘 + 다시 다운로드 버튼

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TODO: 상태 기반 다운로드 UI 구현",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "DownloadState enum 활용 (Idle → Downloading → Completed)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ========================================
// Practice 메인 화면
// ========================================

@Composable
fun PracticeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 문제",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "각 연습을 완료하며 Progress Indicator 사용법을 익혀보세요!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Practice1_BasicLoading()
        Practice2_ProgressDisplay()
        Practice3_FileDownload()
    }
}

// ========================================
// 공통 컴포넌트
// ========================================

@Composable
private fun ExerciseHeader(
    title: String,
    difficulty: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        AssistChip(
            onClick = { },
            label = { Text(difficulty) }
        )
    }
}

@Composable
private fun RequirementsList(requirements: List<String>) {
    Column {
        Text(
            text = "요구사항:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        requirements.forEach { req ->
            Text(
                text = "• $req",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun HintCard(hints: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "숨기기" else "보기")
                }
            }

            if (expanded) {
                hints.forEach { hint ->
                    Text(
                        text = "• $hint",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}
