package com.example.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 - 직접 구현해보세요!
 *
 * 각 연습 문제의 TODO 부분을 완성하세요.
 * 정답은 제공되지 않습니다. 직접 구현하면서 학습하세요!
 */

// ============================================================
// 연습 1: 기본 구분선 추가하기 (쉬움)
// ============================================================

/**
 * 연습 1: 프로필 카드 아래에 수평 구분선 추가하기
 *
 * 요구사항:
 * 1. ProfileCard 아래에 HorizontalDivider 추가
 * 2. 기본 두께(1dp)와 기본 색상 사용
 *
 * 힌트: HorizontalDivider()를 사용하세요
 */
@Composable
fun Practice1_BasicDivider() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "연습 1: 기본 구분선 추가하기",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "ProfileCard 아래에 HorizontalDivider를 추가하세요",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 프로필 카드
            ProfileCard(
                name = "홍길동",
                email = "hong@example.com"
            )

            // TODO: 여기에 HorizontalDivider를 추가하세요
            // 힌트: HorizontalDivider()

            // 추가 정보
            Text(
                text = "가입일: 2024년 1월 1일",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun ProfileCard(
    name: String,
    email: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ============================================================
// 연습 2: Row에서 아이템 구분하기 (중간)
// ============================================================

/**
 * 연습 2: 통계 대시보드에서 수직 구분선 추가하기
 *
 * 요구사항:
 * 1. Row 안의 3개 통계 사이에 VerticalDivider 추가
 * 2. 구분선이 제대로 보이도록 Row에 적절한 Modifier 설정
 * 3. 구분선 색상: MaterialTheme.colorScheme.secondary
 *
 * 힌트:
 * - VerticalDivider가 Row에서 제대로 보이려면 특별한 height 설정이 필요합니다
 * - IntrinsicSize.Min을 찾아보세요
 */
@Composable
fun Practice2_VerticalDivider() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "연습 2: 수직 구분선 추가하기",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "통계 사이에 VerticalDivider를 추가하세요",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 이 Row를 수정하세요
            // 힌트 1: Row의 Modifier에 height(IntrinsicSize.Min)을 추가하세요
            // 힌트 2: 각 StatBox 사이에 VerticalDivider를 추가하세요
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                // TODO: 여기에 .height(???) 추가
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatBox(value = "128", label = "게시물")

                // TODO: 여기에 VerticalDivider 추가 (색상: secondary)

                StatBox(value = "1.2K", label = "팔로워")

                // TODO: 여기에 VerticalDivider 추가 (색상: secondary)

                StatBox(value = "456", label = "팔로잉")
            }
        }
    }
}

@Composable
private fun StatBox(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ============================================================
// 연습 3: 설정 화면 구분선 시스템 (어려움)
// ============================================================

/**
 * 연습 3: 설정 화면에서 섹션별로 다른 스타일의 구분선 적용하기
 *
 * 요구사항:
 * 1. 같은 섹션 내 아이템: Inset Divider (왼쪽 16dp 패딩)
 * 2. 섹션 간: Full-width Divider (두께 2dp, outline 색상)
 * 3. LazyColumn 사용
 * 4. 마지막 아이템 뒤에는 구분선 없음
 *
 * 힌트:
 * - itemsIndexed를 사용하세요
 * - 현재 아이템과 다음 아이템의 section을 비교하세요
 * - 섹션이 같으면 Inset Divider, 다르면 Section Divider
 */

data class SettingItem(
    val icon: ImageVector,
    val title: String,
    val section: String
)

val settingsData = listOf(
    SettingItem(Icons.Default.Person, "프로필", "계정"),
    SettingItem(Icons.Default.Lock, "비밀번호", "계정"),
    SettingItem(Icons.Default.Email, "이메일 변경", "계정"),
    SettingItem(Icons.Default.Notifications, "알림 소리", "알림"),
    SettingItem(Icons.Default.Vibration, "진동", "알림"),
    SettingItem(Icons.Default.Language, "언어", "일반"),
    SettingItem(Icons.Default.DarkMode, "다크 모드", "일반"),
    SettingItem(Icons.Default.Info, "앱 정보", "일반")
)

@Composable
fun Practice3_SectionDividers() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "연습 3: 섹션별 구분선 시스템",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "같은 섹션: Inset Divider / 다른 섹션: Section Divider",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: 이 LazyColumn을 완성하세요
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                itemsIndexed(settingsData) { index, item ->
                    // 설정 아이템 표시
                    SettingItemRow(
                        icon = item.icon,
                        title = item.title
                    )

                    // TODO: 구분선 로직을 구현하세요
                    // 조건 1: 마지막 아이템이 아닐 때만 구분선 추가
                    // 조건 2: 다음 아이템과 같은 섹션이면 Inset Divider
                    //         (Modifier.padding(start = 56.dp) 사용)
                    // 조건 3: 다음 아이템과 다른 섹션이면 Section Divider
                    //         (thickness = 2.dp, color = outline)

                    // 힌트:
                    // if (index < settingsData.lastIndex) {
                    //     val nextItem = settingsData[index + 1]
                    //     if (item.section == nextItem.section) {
                    //         // Inset Divider
                    //     } else {
                    //         // Section Divider
                    //     }
                    // }
                }
            }
        }
    }
}

@Composable
private fun SettingItemRow(
    icon: ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// ============================================================
// Practice 화면 (3개 연습 문제 네비게이션)
// ============================================================

@Composable
fun Practice1Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Practice: Divider 연습",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "각 연습 문제의 TODO 부분을 직접 구현해보세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Practice1_BasicDivider()
    }
}

@Composable
fun Practice2Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Practice: Divider 연습",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "각 연습 문제의 TODO 부분을 직접 구현해보세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Practice2_VerticalDivider()
    }
}

@Composable
fun Practice3Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Practice: Divider 연습",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "각 연습 문제의 TODO 부분을 직접 구현해보세요!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Practice3_SectionDividers()
    }
}
