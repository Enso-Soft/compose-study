package com.example.navigation_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: Row + IconButton으로 하단 탐색바 직접 구현
 *
 * Row와 IconButton을 사용해서 직접 하단 탐색바를 만들려고 하면
 * 여러 문제가 발생합니다.
 */
@Composable
fun ProblemScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 상황 설명 카드
        ProblemExplanationCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 직접 구현한 하단 탐색바 시연
        Text(
            text = "직접 구현한 하단 탐색바:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 문제가 있는 직접 구현 버전
        ManualBottomNavigation(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 발생하는 문제점 목록
        ProblemsList()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 상황",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "앱에 하단 탐색바가 필요해서 Row와 IconButton으로 직접 만들어보려고 합니다. " +
                        "하지만 이 방식은 여러 문제를 일으킵니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

/**
 * Row + IconButton으로 직접 구현한 하단 탐색바
 *
 * 이 방식의 문제점:
 * 1. 선택 상태를 직접 관리해야 함 (배경색, 아이콘 색상)
 * 2. 애니메이션이 없음
 * 3. Material Design 가이드라인을 따르지 않음
 * 4. 접근성(Accessibility)이 부족함
 * 5. 코드 중복이 많음
 */
@Composable
private fun ManualBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        ManualNavItem("홈", Icons.Filled.Home),
        ManualNavItem("검색", Icons.Filled.Search),
        ManualNavItem("프로필", Icons.Filled.Person)
    )

    // 문제점이 드러나는 직접 구현
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            // 문제 1: 선택 상태에 따른 스타일을 모두 직접 관리해야 함
            val isSelected = selectedTab == index
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                Color.Transparent
            }
            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(index) }
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 문제 2: 아이콘 색상도 직접 관리
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                // 문제 3: 텍스트 색상도 직접 관리
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor
                )
            }
        }
    }

    // 코드 주석: 문제점을 보여주기 위해 단순화된 구현
    // 실제로는 더 많은 코드가 필요함:
    // - 리플 효과 직접 구현
    // - 선택/비선택 아이콘 전환
    // - 애니메이션 추가
    // - 접근성 속성 추가
}

private data class ManualNavItem(
    val label: String,
    val icon: ImageVector
)

@Composable
private fun ProblemsList() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "발생하는 문제점",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            ProblemItem(
                number = 1,
                title = "선택 상태 직접 관리",
                description = "배경색, 아이콘 색상, 텍스트 색상을 모두 조건문으로 직접 관리해야 합니다."
            )
            ProblemItem(
                number = 2,
                title = "애니메이션 없음",
                description = "탭 전환 시 부드러운 애니메이션이 없어 어색합니다."
            )
            ProblemItem(
                number = 3,
                title = "Material 가이드라인 미준수",
                description = "정확한 높이(80dp), 아이콘 크기, 리플 효과 등이 누락됩니다."
            )
            ProblemItem(
                number = 4,
                title = "접근성 미지원",
                description = "스크린 리더를 위한 역할(Role) 설정이 없습니다."
            )
            ProblemItem(
                number = 5,
                title = "유지보수 어려움",
                description = "탭이 추가될 때마다 같은 스타일 코드를 반복해야 합니다."
            )
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
