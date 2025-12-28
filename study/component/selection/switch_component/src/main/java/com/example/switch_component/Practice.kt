package com.example.switch_component

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
 * 연습 문제 1: 기본 Switch (쉬움)
 *
 * 비행기 모드 스위치를 구현하세요.
 * - 상태에 따라 "비행기 모드: ON" / "비행기 모드: OFF" 표시
 */
@Composable
fun Practice1Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 Switch (쉬움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "비행기 모드 스위치를 구현하세요.\n" +
                            "상태에 따라 '비행기 모드: ON' / '비행기 모드: OFF'를 표시합니다."
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
                Text("- var isAirplaneMode by remember { mutableStateOf(false) }")
                Text("- Switch(checked = ..., onCheckedChange = ...)")
                Text("- if (isAirplaneMode) \"ON\" else \"OFF\"")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Practice1Exercise()
            }
        }

        // 정답 카드 (접을 수 있게)
        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice1Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice1Exercise() {
    // TODO: 여기에 비행기 모드 스위치를 구현하세요
    // 1. var isAirplaneMode by remember { mutableStateOf(false) }
    // 2. Row로 Switch와 Text 배치
    // 3. 상태에 따라 "비행기 모드: ON" / "비행기 모드: OFF" 표시

    Text(
        text = "TODO: 위 힌트를 참고하여 구현해보세요!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline
    )
}

@Composable
private fun Practice1Answer() {
    var isAirplaneMode by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "비행기 모드: ${if (isAirplaneMode) "ON" else "OFF"}",
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = isAirplaneMode,
            onCheckedChange = { isAirplaneMode = it }
        )
    }
}

/**
 * 연습 문제 2: 아이콘이 있는 Switch (중간)
 *
 * 다크모드 스위치를 구현하세요.
 * - ON: 달 아이콘 (Icons.Filled.DarkMode) 표시
 * - OFF: 해 아이콘 (Icons.Filled.LightMode) 표시
 */
@Composable
fun Practice2Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 아이콘이 있는 Switch (중간)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "다크모드 스위치를 구현하세요.\n" +
                            "- ON: 달 아이콘 표시\n" +
                            "- OFF: 해 아이콘 표시"
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- thumbContent = { Icon(...) }")
                Text("- Icons.Filled.DarkMode / Icons.Filled.LightMode")
                Text("- Modifier.size(SwitchDefaults.IconSize)")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Practice2Exercise()
            }
        }

        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice2Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice2Exercise() {
    // TODO: 여기에 다크모드 스위치를 구현하세요
    // 1. var isDarkMode by remember { mutableStateOf(false) }
    // 2. thumbContent에서 조건에 따라 다른 아이콘 표시
    // 3. Icons.Filled.DarkMode (달) / Icons.Filled.LightMode (해)

    Text(
        text = "TODO: thumbContent를 사용하여 구현해보세요!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline
    )
}

@Composable
private fun Practice2Answer() {
    var isDarkMode by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                contentDescription = null
            )
            Text(
                text = if (isDarkMode) "다크모드" else "라이트모드",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Switch(
            checked = isDarkMode,
            onCheckedChange = { isDarkMode = it },
            thumbContent = {
                Icon(
                    imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        )
    }
}

/**
 * 연습 문제 3: 알림 설정 화면 (어려움)
 *
 * 알림 설정 화면을 구현하세요:
 * 1. 푸시 알림
 * 2. 이메일 알림
 * 3. 마케팅 수신 동의
 *
 * 추가 요구사항:
 * - 마케팅 동의가 OFF면 "프로모션 알림" 스위치는 비활성화
 * - 각 스위치에 아이콘과 설명 텍스트 추가
 */
@Composable
fun Practice3Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 알림 설정 화면 (어려움)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "알림 설정 화면을 구현하세요:\n" +
                            "1. 푸시 알림\n" +
                            "2. 이메일 알림\n" +
                            "3. 마케팅 수신 동의\n" +
                            "4. 프로모션 알림 (마케팅 동의 OFF 시 비활성화)"
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- enabled = marketingConsent 사용")
                Text("- Card와 Column으로 설정 아이템 구성")
                Text("- Icons: Notifications, Email, Campaign, LocalOffer")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Practice3Exercise()
            }
        }

        var showAnswer by remember { mutableStateOf(false) }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "보기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice3Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice3Exercise() {
    // TODO: 여기에 알림 설정 화면을 구현하세요
    // 1. 푸시 알림, 이메일 알림, 마케팅 동의, 프로모션 알림 스위치
    // 2. 각 아이템에 아이콘, 제목, 설명, 스위치 배치
    // 3. 마케팅 동의가 false면 프로모션 알림은 enabled = false

    Text(
        text = "TODO: 4개의 설정 스위치를 구현해보세요!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline
    )
}

@Composable
private fun Practice3Answer() {
    var pushNotification by remember { mutableStateOf(true) }
    var emailNotification by remember { mutableStateOf(true) }
    var marketingConsent by remember { mutableStateOf(false) }
    var promotionNotification by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        NotificationSettingItem(
            icon = Icons.Filled.Notifications,
            title = "푸시 알림",
            description = "앱 알림을 받습니다",
            checked = pushNotification,
            onCheckedChange = { pushNotification = it }
        )

        HorizontalDivider()

        NotificationSettingItem(
            icon = Icons.Filled.Email,
            title = "이메일 알림",
            description = "이메일로 소식을 받습니다",
            checked = emailNotification,
            onCheckedChange = { emailNotification = it }
        )

        HorizontalDivider()

        NotificationSettingItem(
            icon = Icons.Filled.Campaign,
            title = "마케팅 수신 동의",
            description = "마케팅 정보를 받습니다",
            checked = marketingConsent,
            onCheckedChange = { marketingConsent = it }
        )

        HorizontalDivider()

        NotificationSettingItem(
            icon = Icons.Filled.LocalOffer,
            title = "프로모션 알림",
            description = "할인 및 이벤트 소식을 받습니다",
            checked = promotionNotification,
            onCheckedChange = { promotionNotification = it },
            enabled = marketingConsent  // 마케팅 동의가 OFF면 비활성화
        )

        if (!marketingConsent) {
            Text(
                text = "* 마케팅 수신 동의를 켜면 프로모션 알림을 설정할 수 있습니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun NotificationSettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            },
            modifier = Modifier.padding(end = 16.dp)
        )

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
