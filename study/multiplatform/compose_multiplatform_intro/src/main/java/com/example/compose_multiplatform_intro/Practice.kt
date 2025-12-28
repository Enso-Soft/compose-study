package com.example.compose_multiplatform_intro

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// 연습 1: 플랫폼 정보 표시 (쉬움)
// ============================================================

/**
 * 연습 1: expect/actual 패턴 이해하기
 *
 * 목표: expect/actual 패턴을 시뮬레이션하여 플랫폼 정보를 표시합니다.
 *
 * 과제:
 * 1. getPlatformInfo() 함수를 완성하세요
 * 2. 플랫폼 이름과 버전을 PlatformInfo로 반환하세요
 * 3. 이것이 expect/actual 패턴의 "actual" 구현입니다
 */
@Composable
fun Practice1_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 플랫폼 정보 표시",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "expect/actual 패턴을 시뮬레이션하여\n" +
                            "현재 플랫폼의 정보를 표시하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // expect 선언 설명
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "commonMain에서의 expect 선언",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
// commonMain/Platform.kt
data class PlatformInfo(
    val name: String,
    val version: String
)

// "이런 기능이 필요해요"
expect fun getPlatformInfo(): PlatformInfo
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFD4D4D4),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Android에서는 Build.VERSION.SDK_INT로 버전을 얻습니다")
                Text("2. PlatformInfo(name, version)을 반환하세요")
                Text("3. 이것이 androidMain에 작성되는 actual 구현입니다")
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFCE4EC)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "실습 영역",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Practice1_Exercise()
            }
        }

        // 정답
        AnswerCard(
            answer = """
// androidMain/Platform.android.kt
actual fun getPlatformInfo(): PlatformInfo {
    return PlatformInfo(
        name = "Android",
        version = Build.VERSION.SDK_INT.toString()
    )
}

// iosMain/Platform.ios.kt
actual fun getPlatformInfo(): PlatformInfo {
    return PlatformInfo(
        name = "iOS",
        version = UIDevice.currentDevice.systemVersion
    )
}

// desktopMain/Platform.jvm.kt
actual fun getPlatformInfo(): PlatformInfo {
    return PlatformInfo(
        name = "Desktop",
        version = System.getProperty("java.version")
    )
}
            """.trimIndent()
        )
    }
}

/**
 * 연습 1 실습 영역
 */
@Composable
private fun Practice1_Exercise() {
    // TODO: getPlatformInfo() 함수를 완성하세요
    // 현재는 빈 값을 반환하고 있습니다.

    val platformInfo = getPlatformInfo()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "플랫폼: ${platformInfo.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "버전: ${platformInfo.version}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (platformInfo.name.isEmpty()) {
        Text(
            text = "getPlatformInfo() 함수를 완성하세요!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    } else {
        Text(
            text = "정상적으로 플랫폼 정보가 표시됩니다!",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF4CAF50)
        )
    }
}

/**
 * 플랫폼 정보를 담는 데이터 클래스
 * (실제로는 commonMain에 정의됩니다)
 */
data class PlatformInfo(
    val name: String,
    val version: String
)

/**
 * TODO: 이 함수를 완성하세요!
 *
 * 이 함수는 실제로는 expect/actual 패턴으로 구현됩니다.
 * - expect: commonMain에서 선언
 * - actual: 각 플랫폼(android, ios, desktop)에서 구현
 *
 * 현재는 Android 환경이므로 Android용 actual 구현을 작성하세요.
 *
 * 요구사항:
 * - name: "Android"
 * - version: Android API 레벨 (Build.VERSION.SDK_INT)
 */
fun getPlatformInfo(): PlatformInfo {
    // TODO: 아래 코드를 수정하세요
    // 힌트: Build.VERSION.SDK_INT를 사용합니다

    return PlatformInfo(
        name = "Android",
        version = Build.VERSION.SDK_INT.toString()
    )
}

// ============================================================
// 연습 2: 공유 프로필 카드 (중간)
// ============================================================

/**
 * 연습 2: 공유 UI 컴포넌트 작성하기
 *
 * 목표: commonMain에 배치될 공유 UI 컴포넌트를 작성합니다.
 *
 * 과제:
 * 1. SharedProfileCard 컴포넌트를 완성하세요
 * 2. 이름, 이메일, 프로필 아이콘을 표시하세요
 * 3. 이 컴포넌트는 모든 플랫폼에서 동일하게 동작합니다
 */
@Composable
fun Practice2_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 공유 프로필 카드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "commonMain에 배치될 공유 UI 컴포넌트를 작성하세요.\n" +
                            "이 컴포넌트는 Android, iOS, Desktop, Web에서\n" +
                            "동일하게 동작합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Card로 컨테이너를 만드세요")
                Text("2. Row로 아이콘과 텍스트를 배치하세요")
                Text("3. Column으로 이름과 이메일을 세로 배치하세요")
                Text("4. Material3 스타일을 활용하세요")
            }
        }

        // 목표 결과
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "목표 결과 (이렇게 보여야 합니다)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 목표 결과 예시
                TargetProfileCard(
                    name = "홍길동",
                    email = "hong@example.com"
                )
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "실습 영역",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // TODO: SharedProfileCard를 완성하세요
                SharedProfileCard(
                    name = "김철수",
                    email = "kim@example.com"
                )
            }
        }

        // 정답
        AnswerCard(
            answer = """
// commonMain/components/SharedProfileCard.kt
@Composable
fun SharedProfileCard(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 아이콘
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 이름과 이메일
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
            """.trimIndent()
        )
    }
}

/**
 * 목표 결과 (정답 예시)
 */
@Composable
private fun TargetProfileCard(
    name: String,
    email: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * TODO: 이 컴포넌트를 완성하세요!
 *
 * 이 컴포넌트는 commonMain에 배치되어
 * 모든 플랫폼에서 동일하게 동작합니다.
 *
 * 요구사항:
 * - Card로 감싸기
 * - 왼쪽에 프로필 아이콘 (Icons.Default.Person)
 * - 오른쪽에 이름(굵게)과 이메일 표시
 */
@Composable
fun SharedProfileCard(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    // TODO: 아래 코드를 수정하세요
    // 힌트: Card, Row, Column, Surface, Icon, Text 사용

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: 프로필 아이콘 추가
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // TODO: 이름과 이메일 표시
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ============================================================
// 연습 3: 플랫폼별 네비게이션 (어려움)
// ============================================================

/**
 * 연습 3: expect/actual Composable 패턴
 *
 * 목표: 플랫폼마다 다른 스타일의 네비게이션을 구현합니다.
 *
 * 과제:
 * 1. PlatformNavigationBar를 작성하세요
 * 2. Android: Material3 하단 네비게이션 스타일
 * 3. iOS: iOS 스타일 탭바 (시뮬레이션)
 * 4. Desktop: 세로 네비게이션 레일 스타일
 */
@Composable
fun Practice3_Screen() {
    var selectedPlatform by remember { mutableStateOf("android") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 플랫폼별 네비게이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "expect/actual 패턴으로 플랫폼마다\n" +
                            "다른 스타일의 네비게이션을 구현하세요.\n" +
                            "대부분의 UI는 공유하지만, 플랫폼 특화 UI가\n" +
                            "필요할 때 이 패턴을 사용합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. expect @Composable fun으로 선언합니다")
                Text("2. 각 actual에서 플랫폼에 맞는 스타일 구현")
                Text("3. Android: NavigationBar (가로)")
                Text("4. iOS: iOS 스타일 탭바 (가로, 파란색)")
                Text("5. Desktop: NavigationRail (세로)")
            }
        }

        // 플랫폼 선택
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "플랫폼 시뮬레이션",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "버튼을 눌러 다른 플랫폼의 네비게이션을 확인하세요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = selectedPlatform == "android",
                        onClick = { selectedPlatform = "android" },
                        label = { Text("Android") }
                    )
                    FilterChip(
                        selected = selectedPlatform == "ios",
                        onClick = { selectedPlatform = "ios" },
                        label = { Text("iOS") }
                    )
                    FilterChip(
                        selected = selectedPlatform == "desktop",
                        onClick = { selectedPlatform = "desktop" },
                        label = { Text("Desktop") }
                    )
                }
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "실습 영역 - ${selectedPlatform.uppercase()} 스타일",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // TODO: PlatformNavigationBar를 완성하세요
                PlatformNavigationBar(platform = selectedPlatform)
            }
        }

        // expect/actual 코드 구조
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "expect/actual 코드 구조",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
// commonMain/navigation/PlatformNavigationBar.kt
@Composable
expect fun PlatformNavigationBar(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
)

// androidMain
@Composable
actual fun PlatformNavigationBar(...) {
    NavigationBar { /* Material3 스타일 */ }
}

// iosMain
@Composable
actual fun PlatformNavigationBar(...) {
    /* iOS 스타일 탭바 */
}

// desktopMain
@Composable
actual fun PlatformNavigationBar(...) {
    NavigationRail { /* 세로 레일 */ }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFD4D4D4),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 정답
        AnswerCard(
            answer = """
// 각 플랫폼별 actual 구현

// Android: NavigationBar (가로)
NavigationBar {
    items.forEachIndexed { index, item ->
        NavigationBarItem(
            selected = selectedIndex == index,
            onClick = { onItemSelected(index) },
            icon = { Icon(item.icon, contentDescription = null) },
            label = { Text(item.label) }
        )
    }
}

// iOS: iOS 스타일 (파란색 강조)
Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFF8F8F8))
        .padding(8.dp)
) {
    items.forEachIndexed { index, item ->
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icon,
                tint = if (selected) Color(0xFF007AFF) else Color.Gray
            )
            Text(
                text = item.label,
                color = if (selected) Color(0xFF007AFF) else Color.Gray
            )
        }
    }
}

// Desktop: NavigationRail (세로)
NavigationRail {
    items.forEachIndexed { index, item ->
        NavigationRailItem(
            selected = selectedIndex == index,
            onClick = { onItemSelected(index) },
            icon = { Icon(item.icon, contentDescription = null) },
            label = { Text(item.label) }
        )
    }
}
            """.trimIndent()
        )
    }
}

/**
 * 네비게이션 아이템 데이터
 */
data class NavItem(
    val icon: ImageVector,
    val label: String
)

/**
 * TODO: 이 컴포넌트를 완성하세요!
 *
 * 플랫폼에 따라 다른 스타일의 네비게이션을 표시합니다.
 * - android: Material3 NavigationBar (가로)
 * - ios: iOS 스타일 탭바 (가로, 파란색 강조)
 * - desktop: NavigationRail (세로)
 *
 * 이것은 expect/actual 패턴의 시뮬레이션입니다.
 * 실제로는 각 플랫폼별 소스셋에서 actual을 구현합니다.
 */
@Composable
fun PlatformNavigationBar(platform: String) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val items = listOf(
        NavItem(Icons.Default.Home, "Home"),
        NavItem(Icons.Default.Search, "Search"),
        NavItem(Icons.Default.Person, "Profile")
    )

    // TODO: platform 값에 따라 다른 네비게이션 스타일을 구현하세요
    when (platform) {
        "android" -> {
            // TODO: Android 스타일 (NavigationBar)
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) }
                    )
                }
            }
        }

        "ios" -> {
            // TODO: iOS 스타일 (파란색 강조, 밝은 배경)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8F8F8))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedIndex == index
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFF007AFF) else Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.label,
                            fontSize = 12.sp,
                            color = if (isSelected) Color(0xFF007AFF) else Color.Gray
                        )
                    }
                }
            }
        }

        "desktop" -> {
            // TODO: Desktop 스타일 (NavigationRail - 세로)
            NavigationRail {
                items.forEachIndexed { index, item ->
                    NavigationRailItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    }
}

// ============================================================
// 공통 UI 컴포넌트
// ============================================================

@Composable
private fun AnswerCard(answer: String) {
    var showAnswer by remember { mutableStateOf(false) }

    Card(
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
                Text(
                    text = "정답 보기",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = showAnswer,
                    onCheckedChange = { showAnswer = it }
                )
            }

            if (showAnswer) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0xFF1E1E1E),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = answer,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFD4D4D4),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}
