package com.example.switch_component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 해결책 화면
 *
 * Switch 컴포넌트의 다양한 사용법을 단계별로 보여줍니다.
 * 1. 기본 Switch
 * 2. 레이블과 함께 사용
 * 3. thumbContent로 아이콘 추가 (Material 3)
 * 4. 색상 커스터마이징
 * 5. enabled/disabled 상태
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Switch 사용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Switch는 조명 스위치처럼 토글하면 즉시 적용됩니다.\n" +
                            "'저장' 버튼 없이도 사용자가 설정 변경을 직관적으로 이해합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 1. 기본 Switch
        SectionCard(
            title = "1. 기본 Switch",
            description = "가장 간단한 형태의 Switch입니다."
        ) {
            BasicSwitchDemo()
        }

        // 2. 레이블과 함께 사용
        SectionCard(
            title = "2. 레이블과 함께 사용",
            description = "Row로 Text와 Switch를 배치합니다."
        ) {
            LabeledSwitchDemo()
        }

        // 3. thumbContent로 아이콘 추가
        SectionCard(
            title = "3. thumbContent로 아이콘 추가",
            description = "Material 3에서 동그란 부분(thumb)에 아이콘을 표시할 수 있습니다."
        ) {
            SwitchWithIconDemo()
        }

        // 4. 색상 커스터마이징
        SectionCard(
            title = "4. 색상 커스터마이징",
            description = "SwitchDefaults.colors()로 색상을 변경합니다."
        ) {
            ColoredSwitchDemo()
        }

        // 5. enabled/disabled
        SectionCard(
            title = "5. enabled/disabled 상태",
            description = "enabled = false로 비활성화합니다."
        ) {
            EnabledDisabledDemo()
        }

        // 완성된 설정 화면 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "완성된 설정 화면",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                CompletedSettingsDemo()
            }
        }

        // 핵심 포인트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Switch는 즉시 적용되는 설정에 사용합니다")
                Text("2. thumbContent로 ON 상태를 더 명확히 표현하세요")
                Text("3. 관련 설정이 OFF면 하위 설정을 enabled = false로 비활성화하세요")
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

/**
 * 1. 기본 Switch
 */
@Composable
private fun BasicSwitchDemo() {
    var checked by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Switch(
            checked = checked,
            onCheckedChange = { checked = it }
        )
        Text(
            text = if (checked) "ON" else "OFF",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * 2. 레이블과 함께 사용
 */
@Composable
private fun LabeledSwitchDemo() {
    var darkMode by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "다크모드",
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = darkMode,
            onCheckedChange = { darkMode = it }
        )
    }
}

/**
 * 3. thumbContent로 아이콘 추가 (Material 3)
 */
@Composable
private fun SwitchWithIconDemo() {
    var checked by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // 체크 아이콘 버전
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("체크 아이콘")
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                thumbContent = if (checked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                } else null
            )
        }

        // 다크모드 아이콘 버전
        var darkMode by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("다크모드 (ON/OFF 아이콘)")
            Switch(
                checked = darkMode,
                onCheckedChange = { darkMode = it },
                thumbContent = {
                    Icon(
                        imageVector = if (darkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            )
        }
    }
}

/**
 * 4. 색상 커스터마이징
 */
@Composable
private fun ColoredSwitchDemo() {
    var checked by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("커스텀 색상")
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            } else null
        )
    }
}

/**
 * 5. enabled/disabled 상태
 */
@Composable
private fun EnabledDisabledDemo() {
    var masterSwitch by remember { mutableStateOf(true) }
    var subSwitch by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("마스터 스위치")
            Switch(
                checked = masterSwitch,
                onCheckedChange = { masterSwitch = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "하위 스위치 (마스터 OFF 시 비활성화)",
                color = if (masterSwitch) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
            Switch(
                checked = subSwitch,
                onCheckedChange = { subSwitch = it },
                enabled = masterSwitch
            )
        }
    }
}

/**
 * 완성된 설정 화면 예시
 */
@Composable
private fun CompletedSettingsDemo() {
    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var autoUpdate by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        SettingItem(
            title = "다크모드",
            description = "어두운 화면을 사용합니다",
            checked = darkMode,
            onCheckedChange = { darkMode = it }
        )

        HorizontalDivider()

        SettingItem(
            title = "알림",
            description = "푸시 알림을 받습니다",
            checked = notifications,
            onCheckedChange = { notifications = it }
        )

        HorizontalDivider()

        SettingItem(
            title = "자동 업데이트",
            description = "WiFi 연결 시 자동 업데이트",
            checked = autoUpdate,
            onCheckedChange = { autoUpdate = it }
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                }
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            } else null
        )
    }
}
