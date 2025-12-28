package com.example.material_symbols

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Solution 화면
 *
 * Material Symbols를 사용하여 기존 문제들을 해결하는 방법을 보여줍니다.
 * - Vector Drawable 사용법
 * - 스타일 비교 (Outlined, Rounded, Sharp)
 * - Tint 적용
 * - 크기 조절
 * - 실제 활용 예시
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
        // 개요 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Material Symbols로 해결하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Google Fonts에서 필요한 아이콘만 Vector Drawable로 다운로드하여 사용합니다. " +
                            "빌드 시간 감소, 최신 아이콘 사용, 커스터마이징이 모두 가능합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 다운로드 방법
        DownloadGuide()

        // 기본 사용법
        BasicUsageDemo()

        // 스타일 비교
        StyleComparisonDemo()

        // Tint 적용
        TintDemo()

        // 크기 조절
        SizeDemo()

        // 실제 활용 예시
        RealWorldExamples()
    }
}

@Composable
private fun DownloadGuide() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "다운로드 방법",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            val steps = listOf(
                "1. fonts.google.com/icons 접속",
                "2. 원하는 아이콘 검색 (예: home)",
                "3. 스타일 선택 (Outlined, Rounded, Sharp)",
                "4. Android 탭 클릭",
                "5. XML 파일 다운로드",
                "6. res/drawable/ 폴더에 추가"
            )

            steps.forEach { step ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = step, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun BasicUsageDemo() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "기본 사용법",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = """
// ImageVector.vectorResource 사용
Icon(
    imageVector = ImageVector.vectorResource(
        id = R.drawable.ic_home
    ),
    contentDescription = "홈"
)

// 또는 painterResource 사용
Icon(
    painter = painterResource(
        id = R.drawable.ic_settings
    ),
    contentDescription = "설정"
)
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "실제 결과:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_home),
                        contentDescription = "홈",
                        modifier = Modifier.size(32.dp)
                    )
                    Text("home", style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                        contentDescription = "설정",
                        modifier = Modifier.size(32.dp)
                    )
                    Text("settings", style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
                        contentDescription = "좋아요",
                        modifier = Modifier.size(32.dp)
                    )
                    Text("favorite", style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                        contentDescription = "검색",
                        modifier = Modifier.size(32.dp)
                    )
                    Text("search", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun StyleComparisonDemo() {
    var selectedStyle by remember { mutableStateOf("outlined") }

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "스타일 비교",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Material Symbols는 3가지 스타일을 제공합니다. 앱의 디자인에 맞는 스타일을 선택하세요.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StyleOption(
                    title = "Outlined",
                    iconRes = R.drawable.ic_home,
                    description = "깔끔하고 미니멀",
                    isSelected = selectedStyle == "outlined",
                    onClick = { selectedStyle = "outlined" }
                )
                StyleOption(
                    title = "Rounded",
                    iconRes = R.drawable.ic_home_rounded,
                    description = "부드럽고 친근",
                    isSelected = selectedStyle == "rounded",
                    onClick = { selectedStyle = "rounded" }
                )
                StyleOption(
                    title = "Sharp",
                    iconRes = R.drawable.ic_home_sharp,
                    description = "날카롭고 현대적",
                    isSelected = selectedStyle == "sharp",
                    onClick = { selectedStyle = "sharp" }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "선택된 스타일: ${selectedStyle.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun StyleOption(
    title: String,
    iconRes: Int,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(8.dp)
                    )
                } else {
                    Modifier
                }
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = title,
            modifier = Modifier.size(40.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TintDemo() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tint (색상) 적용",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "tint 파라미터로 아이콘 색상을 변경합니다. MaterialTheme.colorScheme을 사용하면 테마에 맞게 자동 적용됩니다.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TintExample("primary", MaterialTheme.colorScheme.primary)
                TintExample("secondary", MaterialTheme.colorScheme.secondary)
                TintExample("tertiary", MaterialTheme.colorScheme.tertiary)
                TintExample("error", MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = """
Icon(
    imageVector = ImageVector.vectorResource(
        R.drawable.ic_favorite
    ),
    contentDescription = "좋아요",
    tint = MaterialTheme.colorScheme.primary
)
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun TintExample(
    name: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = color
        )
        Text(name, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun SizeDemo() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "크기 조절",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Modifier.size()로 아이콘 크기를 조절합니다. Material Design 가이드라인에 따라 20dp, 24dp, 40dp, 48dp를 권장합니다.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                SizeExample(20)
                SizeExample(24)
                SizeExample(36)
                SizeExample(48)
            }
        }
    }
}

@Composable
private fun SizeExample(sizeDp: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_home),
            contentDescription = null,
            modifier = Modifier.size(sizeDp.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("${sizeDp}dp", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun RealWorldExamples() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "실제 활용 예시",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 네비게이션 바 예시
            Text(
                text = "NavigationBar",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            NavigationBarExample()

            Spacer(modifier = Modifier.height(16.dp))

            // 설정 메뉴 예시
            Text(
                text = "설정 메뉴",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsMenuExample()
        }
    }
}

@Composable
private fun NavigationBarExample() {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (selectedItem == 0) R.drawable.ic_home_filled
                        else R.drawable.ic_home
                    ),
                    contentDescription = "홈"
                )
            },
            label = { Text("홈") }
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (selectedItem == 1) R.drawable.ic_favorite_filled
                        else R.drawable.ic_favorite
                    ),
                    contentDescription = "좋아요"
                )
            },
            label = { Text("좋아요") }
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                    contentDescription = "설정"
                )
            },
            label = { Text("설정") }
        )
    }
}

@Composable
private fun SettingsMenuExample() {
    Column {
        SettingsMenuItem(
            iconRes = R.drawable.ic_notifications,
            title = "알림 설정"
        )
        HorizontalDivider()
        SettingsMenuItem(
            iconRes = R.drawable.ic_person,
            title = "계정 관리"
        )
        HorizontalDivider()
        SettingsMenuItem(
            iconRes = R.drawable.ic_info,
            title = "앱 정보"
        )
    }
}

@Composable
private fun SettingsMenuItem(
    iconRes: Int,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 클릭 처리 */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
