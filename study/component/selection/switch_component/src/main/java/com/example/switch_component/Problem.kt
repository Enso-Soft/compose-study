package com.example.switch_component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * 설정 화면에서 Checkbox를 사용했을 때 발생하는 UX 문제를 시연합니다.
 * Switch 대신 Checkbox를 사용하면 사용자에게 혼란을 줄 수 있습니다.
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 상황 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "설정 화면에서 ON/OFF 토글을 Checkbox로 구현했습니다.\n" +
                            "사용자가 체크해도 '저장' 버튼이 없어서 혼란스러워합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제가 되는 UI 시연
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "앱 설정 (Checkbox 사용)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                ProblemDemo()
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. UX 혼란: 사용자가 '저장' 버튼을 찾게 됩니다")
                Text("2. 즉시 적용 불명확: 체크해도 바로 적용되는지 알 수 없습니다")
                Text("3. 시각적 피드백 부족: ON/OFF 상태가 직관적이지 않습니다")
                Text("4. 모바일 터치 어려움: Checkbox는 터치 영역이 작습니다")
            }
        }

        // Switch vs Checkbox 비교 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Switch vs Checkbox 차이점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 비교 테이블
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "기준",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Switch",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Checkbox",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                ComparisonRow("효과 시점", "즉시 적용", "Submit 후")
                ComparisonRow("사용 예시", "다크모드", "약관 동의")
                ComparisonRow("비유", "조명 스위치", "투표 용지")
            }
        }

        // 잘못된 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "잘못된 코드 (설정에 Checkbox 사용)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 설정 화면에서 Checkbox 사용 (비권장)
Row(verticalAlignment = CenterVertically) {
    Checkbox(
        checked = darkMode,
        onCheckedChange = { darkMode = it }
    )
    Text("다크모드")
}

// 문제점:
// - "저장" 버튼이 없어서 사용자 혼란
// - Checkbox는 폼 제출용 컴포넌트
// - 설정 화면에는 Switch가 적합!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ProblemDemo() {
    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var autoUpdate by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Checkbox로 구현한 설정들 (잘못된 방식)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = darkMode,
                onCheckedChange = { darkMode = it }
            )
            Text("다크모드")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = notifications,
                onCheckedChange = { notifications = it }
            )
            Text("알림 받기")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = autoUpdate,
                onCheckedChange = { autoUpdate = it }
            )
            Text("자동 업데이트")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 사용자의 혼란을 표현
        Text(
            text = "체크했는데... 저장 버튼은 어디있지?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = "이게 적용된 건가요?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun ComparisonRow(
    criteria: String,
    switchValue: String,
    checkboxValue: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = criteria,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = switchValue,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = checkboxValue,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}
