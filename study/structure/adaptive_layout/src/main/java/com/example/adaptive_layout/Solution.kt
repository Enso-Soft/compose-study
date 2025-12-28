package com.example.adaptive_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Solution: WindowSizeClass를 활용한 적응형 레이아웃
 *
 * 해결 방법:
 * 1. currentWindowAdaptiveInfo()로 현재 화면 크기 정보 가져오기
 * 2. WindowWidthSizeClass에 따라 다른 레이아웃 표시
 * 3. Compact: 기존 단일 화면 방식 (폰에 최적화)
 * 4. Medium/Expanded: 리스트-디테일 동시 표시 (태블릿에 최적화)
 *
 * 테스트 방법:
 * - Resizable Emulator에서 화면 크기 조절하며 레이아웃 변화 확인
 * - 태블릿 에뮬레이터에서 리스트와 디테일 동시 표시 확인
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolutionScreen() {
    // 핵심: currentWindowAdaptiveInfo()로 현재 윈도우 정보 가져오기
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowAdaptiveInfo.windowSizeClass

    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 현재 화면 크기 정보 표시 (학습 목적)
        WindowSizeIndicator(windowSizeClass.windowWidthSizeClass)

        // 핵심 포인트 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결: WindowSizeClass 기반 적응형 레이아웃",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("1. currentWindowAdaptiveInfo()로 화면 크기 감지")
                        appendLine("2. Compact: 단일 화면 (폰)")
                        append("3. Medium/Expanded: 리스트-디테일 동시 표시")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 핵심: 화면 크기에 따라 다른 레이아웃 표시
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> {
                // 폰: 기존 단일 화면 방식
                CompactLayout(
                    selectedEmail = selectedEmail,
                    onEmailSelect = { selectedEmail = it },
                    onBack = { selectedEmail = null }
                )
            }
            else -> {
                // 태블릿/폴더블: 리스트-디테일 동시 표시
                ExpandedLayout(
                    selectedEmail = selectedEmail,
                    onEmailSelect = { selectedEmail = it }
                )
            }
        }
    }
}

@Composable
private fun WindowSizeIndicator(widthSizeClass: WindowWidthSizeClass) {
    val (label, color) = when (widthSizeClass) {
        WindowWidthSizeClass.COMPACT -> "COMPACT (폰)" to MaterialTheme.colorScheme.tertiary
        WindowWidthSizeClass.MEDIUM -> "MEDIUM (폴더블/작은 태블릿)" to MaterialTheme.colorScheme.secondary
        WindowWidthSizeClass.EXPANDED -> "EXPANDED (태블릿/데스크톱)" to MaterialTheme.colorScheme.primary
        else -> "UNKNOWN" to MaterialTheme.colorScheme.outline
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = "현재 WindowWidthSizeClass: $label",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.labelLarge,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompactLayout(
    selectedEmail: Email?,
    onEmailSelect: (Email) -> Unit,
    onBack: () -> Unit
) {
    // Compact: 폰에서는 기존 단일 화면 방식 유지
    if (selectedEmail == null) {
        SolutionEmailList(
            emails = sampleEmails,
            selectedEmail = null,
            onEmailClick = onEmailSelect,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("이메일 상세") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
            SolutionEmailDetail(
                email = selectedEmail,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ExpandedLayout(
    selectedEmail: Email?,
    onEmailSelect: (Email) -> Unit
) {
    // Expanded: 태블릿에서는 리스트와 디테일 동시 표시
    Row(modifier = Modifier.fillMaxSize()) {
        // 리스트 패널 (40%)
        SolutionEmailList(
            emails = sampleEmails,
            selectedEmail = selectedEmail,
            onEmailClick = onEmailSelect,
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
        )

        // 구분선
        VerticalDivider()

        // 디테일 패널 (60%)
        if (selectedEmail != null) {
            SolutionEmailDetail(
                email = selectedEmail,
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
            )
        } else {
            // 선택된 이메일이 없을 때 안내 메시지
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "이메일을 선택하세요",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "왼쪽 목록에서 이메일을 클릭하면\n여기에 내용이 표시됩니다",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun SolutionEmailList(
    emails: List<Email>,
    selectedEmail: Email?,
    onEmailClick: (Email) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(emails) { email ->
            SolutionEmailItem(
                email = email,
                isSelected = email.id == selectedEmail?.id,
                onClick = { onEmailClick(email) }
            )
        }
    }
}

@Composable
private fun SolutionEmailItem(
    email: Email,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder()
        } else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = email.sender,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Text(
                    text = email.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email.subject,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = email.preview,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SolutionEmailDetail(
    email: Email,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = email.subject,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = email.sender.first().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = email.sender,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = email.content,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )
    }
}
