package com.example.button

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Practice: Button 연습 문제
 *
 * 3단계 연습을 통해 Button 사용법을 익힙니다:
 * 1. 쉬움: 기본 버튼 5종 만들기
 * 2. 중간: 로딩 상태가 있는 제출 버튼 구현
 * 3. 어려움: 재사용 가능한 커스텀 버튼 시스템 구축
 */
@Composable
fun PracticeScreen() {
    var selectedPractice by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedPractice == 0,
                onClick = { selectedPractice = 0 },
                label = { Text("쉬움") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("중간") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("어려움") }
            )
        }

        when (selectedPractice) {
            0 -> Exercise1_BasicButtons()
            1 -> Exercise2_LoadingButton()
            2 -> Exercise3_CustomButtonSystem()
        }
    }
}

// ============================================================
// 연습 1: 기본 버튼 5종 만들기 (쉬움)
// ============================================================
@Composable
fun Exercise1_BasicButtons() {
    val snackbarHostState = remember { SnackbarHostState() }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            message = ""
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PracticeCard(
                    title = "연습 1: 기본 버튼 5종 만들기",
                    difficulty = "쉬움",
                    description = """
                        Material 3의 5가지 기본 버튼을 만들어보세요.
                        각 버튼 클릭 시 Snackbar로 메시지를 표시합니다.
                    """.trimIndent(),
                    requirements = listOf(
                        "Button: '확인' 텍스트",
                        "FilledTonalButton: '보류' 텍스트",
                        "ElevatedButton: '공유' 텍스트",
                        "OutlinedButton: '취소' 텍스트",
                        "TextButton: '건너뛰기' 텍스트"
                    )
                )
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "구현 결과",
                            style = MaterialTheme.typography.titleSmall
                        )

                        // TODO: 여기에 5가지 버튼을 구현하세요
                        // 힌트: Button(onClick = { message = "확인 클릭" }) { Text("확인") }

                        // === 예시 답안 ===
                        Button(onClick = { message = "확인 버튼 클릭됨" }) {
                            Text("확인")
                        }

                        FilledTonalButton(onClick = { message = "보류 버튼 클릭됨" }) {
                            Text("보류")
                        }

                        ElevatedButton(onClick = { message = "공유 버튼 클릭됨" }) {
                            Text("공유")
                        }

                        OutlinedButton(onClick = { message = "취소 버튼 클릭됨" }) {
                            Text("취소")
                        }

                        TextButton(onClick = { message = "건너뛰기 클릭됨" }) {
                            Text("건너뛰기")
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// 연습 2: 로딩 버튼 구현 (중간)
// ============================================================
@Composable
fun Exercise2_LoadingButton() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PracticeCard(
                title = "연습 2: 로딩 버튼 구현",
                difficulty = "중간",
                description = """
                    클릭 시 3초간 로딩 상태를 보여주는 버튼을 만들어보세요.
                    로딩 중에는 버튼이 비활성화되어야 합니다.
                """.trimIndent(),
                requirements = listOf(
                    "기본 상태: '제출하기' 텍스트",
                    "로딩 상태: CircularProgressIndicator + '처리 중...' 텍스트",
                    "로딩 중 버튼 비활성화 (enabled = false)",
                    "3초 후 기본 상태로 복귀"
                )
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "구현 결과",
                        style = MaterialTheme.typography.titleSmall
                    )

                    // TODO: 여기에 로딩 버튼을 구현하세요
                    // 힌트 1: var isLoading by remember { mutableStateOf(false) }
                    // 힌트 2: LaunchedEffect(isLoading) { if (isLoading) { delay(3000); isLoading = false } }
                    // 힌트 3: Button(onClick = { isLoading = true }, enabled = !isLoading)

                    // === 예시 답안 ===
                    var isLoading by remember { mutableStateOf(false) }

                    LaunchedEffect(isLoading) {
                        if (isLoading) {
                            delay(3000)
                            isLoading = false
                        }
                    }

                    Button(
                        onClick = { isLoading = true },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("처리 중...")
                        } else {
                            Text("제출하기")
                        }
                    }

                    Text(
                        text = if (isLoading) "3초 후 완료됩니다..." else "버튼을 클릭해보세요",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ============================================================
// 연습 3: 커스텀 버튼 시스템 (어려움)
// ============================================================
@Composable
fun Exercise3_CustomButtonSystem() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PracticeCard(
                title = "연습 3: 커스텀 버튼 시스템",
                difficulty = "어려움",
                description = """
                    재사용 가능한 앱 전용 버튼 컴포넌트를 만들어보세요.
                    variant, size, icon 등 다양한 옵션을 지원해야 합니다.
                """.trimIndent(),
                requirements = listOf(
                    "AppButton 컴포저블 생성",
                    "variant: Primary, Secondary, Danger",
                    "size: Small, Medium, Large",
                    "leadingIcon 지원",
                    "loading 상태 지원"
                )
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "구현 결과",
                        style = MaterialTheme.typography.titleSmall
                    )

                    // === 예시 답안: 커스텀 버튼 시스템 사용 예 ===

                    // Primary 버튼들
                    Text(
                        text = "Primary Variant",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppButton(
                            text = "Small",
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Small,
                            onClick = { }
                        )
                        AppButton(
                            text = "Medium",
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Medium,
                            onClick = { }
                        )
                        AppButton(
                            text = "Large",
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Large,
                            onClick = { }
                        )
                    }

                    // Secondary 버튼들
                    Text(
                        text = "Secondary Variant",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            text = "취소",
                            variant = ButtonVariant.Secondary,
                            onClick = { }
                        )
                        AppButton(
                            text = "건너뛰기",
                            variant = ButtonVariant.Secondary,
                            leadingIcon = Icons.Filled.SkipNext,
                            onClick = { }
                        )
                    }

                    // Danger 버튼들
                    Text(
                        text = "Danger Variant",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFF44336)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            text = "삭제",
                            variant = ButtonVariant.Danger,
                            leadingIcon = Icons.Filled.Delete,
                            onClick = { }
                        )
                    }

                    // 로딩 상태
                    Text(
                        text = "Loading State",
                        style = MaterialTheme.typography.labelMedium
                    )
                    var isLoading by remember { mutableStateOf(false) }
                    LaunchedEffect(isLoading) {
                        if (isLoading) {
                            delay(2000)
                            isLoading = false
                        }
                    }
                    AppButton(
                        text = "저장",
                        variant = ButtonVariant.Primary,
                        leadingIcon = Icons.Filled.Save,
                        isLoading = isLoading,
                        onClick = { isLoading = true }
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "AppButton 구현 코드",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = """
enum class ButtonVariant { Primary, Secondary, Danger }
enum class ButtonSize { Small, Medium, Large }

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Medium,
    leadingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    // 구현 내용은 아래 코드 참조
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

// ============================================================
// 커스텀 버튼 시스템 구현
// ============================================================

enum class ButtonVariant { Primary, Secondary, Danger }
enum class ButtonSize { Small, Medium, Large }

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Medium,
    leadingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    val colors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors()
        ButtonVariant.Secondary -> ButtonDefaults.outlinedButtonColors()
        ButtonVariant.Danger -> ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF44336),
            contentColor = Color.White
        )
    }

    val contentPadding = when (size) {
        ButtonSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ButtonSize.Medium -> ButtonDefaults.ContentPadding
        ButtonSize.Large -> PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    }

    val textStyle = when (size) {
        ButtonSize.Small -> MaterialTheme.typography.labelSmall
        ButtonSize.Medium -> MaterialTheme.typography.labelLarge
        ButtonSize.Large -> MaterialTheme.typography.titleMedium
    }

    when (variant) {
        ButtonVariant.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && !isLoading,
                contentPadding = contentPadding
            ) {
                ButtonContent(
                    text = text,
                    textStyle = textStyle,
                    leadingIcon = leadingIcon,
                    isLoading = isLoading,
                    size = size
                )
            }
        }
        else -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && !isLoading,
                colors = colors,
                contentPadding = contentPadding
            ) {
                ButtonContent(
                    text = text,
                    textStyle = textStyle,
                    leadingIcon = leadingIcon,
                    isLoading = isLoading,
                    size = size
                )
            }
        }
    }
}

@Composable
private fun RowScope.ButtonContent(
    text: String,
    textStyle: androidx.compose.ui.text.TextStyle,
    leadingIcon: ImageVector?,
    isLoading: Boolean,
    size: ButtonSize
) {
    val iconSize = when (size) {
        ButtonSize.Small -> 14.dp
        ButtonSize.Medium -> 18.dp
        ButtonSize.Large -> 24.dp
    }

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(iconSize),
            strokeWidth = 2.dp
        )
        Spacer(Modifier.width(8.dp))
    } else if (leadingIcon != null) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )
        Spacer(Modifier.width(8.dp))
    }
    Text(text = text, style = textStyle)
}

// ============================================================
// 공통 컴포넌트
// ============================================================

@Composable
private fun PracticeCard(
    title: String,
    difficulty: String,
    description: String,
    requirements: List<String>
) {
    val difficultyColor = when (difficulty) {
        "쉬움" -> Color(0xFF4CAF50)
        "중간" -> Color(0xFFFF9800)
        "어려움" -> Color(0xFFF44336)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    color = difficultyColor,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = difficulty,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "요구사항:",
                style = MaterialTheme.typography.labelMedium
            )

            requirements.forEach { requirement ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("• ", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = requirement,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
