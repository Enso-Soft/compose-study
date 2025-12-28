package com.example.radio_button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 기본 사용법 화면
 *
 * RadioButton의 기본적인 사용 방법을 학습합니다.
 *
 * RadioButton은 옛날 라디오의 채널 버튼과 같습니다.
 * - 버튼 하나를 누르면 기존에 눌린 버튼이 튀어나옵니다
 * - 동시에 두 개를 누를 수 없습니다
 * - 이렇게 '하나만 선택 가능'한 UI가 RadioButton입니다
 */
@Composable
fun BasicUsageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 개요 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "RadioButton이란?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "RadioButton은 여러 옵션 중에서 '하나만' 선택할 때 사용하는 UI 컴포넌트입니다. " +
                            "옛날 라디오의 채널 버튼처럼, 하나를 선택하면 다른 것은 자동으로 해제됩니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 기능 1: 단일 RadioButton
        FeatureCard(
            title = "기능 1: 단일 RadioButton",
            description = "가장 기본적인 RadioButton 사용법입니다. selected와 onClick 파라미터를 사용합니다."
        ) {
            SingleRadioButtonDemo()
        }

        // 기능 2: RadioButton 그룹
        FeatureCard(
            title = "기능 2: RadioButton 그룹",
            description = "여러 옵션 중 하나를 선택하는 그룹입니다. 상태(state)를 사용하여 선택된 옵션을 관리합니다."
        ) {
            RadioGroupDemo()
        }

        // 기능 3: 레이블과 함께 사용
        FeatureCard(
            title = "기능 3: 레이블과 함께 사용",
            description = "RadioButton과 Text를 Row로 묶어 레이블을 표시합니다. 전체 Row를 클릭 가능하게 만들면 사용성이 향상됩니다."
        ) {
            LabeledRadioButtonDemo()
        }

        // 기능 4: enabled/disabled 상태
        FeatureCard(
            title = "기능 4: enabled/disabled 상태",
            description = "특정 옵션을 비활성화하여 선택할 수 없게 만들 수 있습니다. enabled 파라미터를 사용합니다."
        ) {
            EnabledDisabledDemo()
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    description: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            demo()
        }
    }
}

/**
 * 기능 1: 단일 RadioButton
 *
 * 가장 기본적인 형태입니다.
 * - selected: 이 버튼이 선택되었는지 여부 (true/false)
 * - onClick: 사용자가 클릭했을 때 실행할 동작
 */
@Composable
private fun SingleRadioButtonDemo() {
    var isAgreed by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = isAgreed,
                onClick = { isAgreed = true }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("이용약관에 동의합니다")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isAgreed) "동의함" else "동의하지 않음",
            style = MaterialTheme.typography.bodySmall,
            color = if (isAgreed) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

/**
 * 기능 2: RadioButton 그룹
 *
 * 여러 옵션 중 하나를 선택하는 패턴입니다.
 * - remember { mutableStateOf(...) }로 선택 상태를 저장
 * - forEach로 각 옵션에 대해 RadioButton 생성
 * - selected = (option == selectedOption)으로 현재 선택 여부 판단
 */
@Composable
private fun RadioGroupDemo() {
    val options = listOf("한국어", "English", "日本語")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { selectedOption = option }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(option)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = "선택된 언어: $selectedOption",
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/**
 * 기능 3: 레이블과 함께 사용 (클릭 영역 확장)
 *
 * RadioButton만 클릭 가능하면 작은 영역만 터치됩니다.
 * Row 전체에 clickable을 추가하면 레이블을 눌러도 선택됩니다.
 *
 * 핵심 포인트:
 * - RadioButton의 onClick = null로 설정 (Row가 클릭을 처리하므로)
 * - Row에 Modifier.clickable { ... } 추가
 */
@Composable
private fun LabeledRadioButtonDemo() {
    val options = listOf("소리", "진동", "무음")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column {
        Text(
            text = "알림 설정",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedOption = option }
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null  // Row가 클릭을 처리하므로 null
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            if (option != options.last()) {
                HorizontalDivider(modifier = Modifier.padding(start = 48.dp))
            }
        }
    }
}

/**
 * 기능 4: enabled/disabled 상태
 *
 * 특정 조건에서 옵션을 비활성화할 수 있습니다.
 * - enabled = false: 회색으로 표시되고 클릭 불가
 * - 프리미엄 기능, 권한이 없는 옵션 등에 사용
 */
@Composable
private fun EnabledDisabledDemo() {
    // Pair<옵션명, 활성화 여부>
    val options = listOf(
        "무료 플랜" to true,
        "베이직 플랜" to true,
        "프리미엄 플랜 (준비 중)" to false  // 비활성화
    )
    var selectedOption by remember { mutableStateOf("무료 플랜") }

    Column {
        Text(
            text = "요금제 선택",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        options.forEach { (option, enabled) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        if (enabled) selectedOption = option
                    },
                    enabled = enabled
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = option,
                    color = if (enabled) Color.Unspecified else Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "현재 선택: $selectedOption",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
