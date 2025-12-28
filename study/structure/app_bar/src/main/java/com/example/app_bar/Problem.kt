package com.example.app_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 문제 상황: Row로 앱바를 직접 구현할 때의 어려움
 *
 * 이 화면은 TopAppBar 없이 Row를 사용하여 앱바를 직접 구현했을 때
 * 발생하는 문제점들을 보여줍니다.
 *
 * 문제점:
 * 1. 높이, 패딩을 직접 지정해야 함 (Material Design 가이드라인 위반 가능)
 * 2. 색상을 하드코딩 → 다크모드 대응이 어려움
 * 3. 스크롤 시 축소/확장을 직접 구현해야 함
 * 4. StatusBar 영역 처리가 복잡함
 * 5. 아이콘 간격, 터치 영역 등을 일일이 맞춰야 함
 *
 * @see SolutionScreen TopAppBar를 사용한 올바른 해결책
 */

@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 문제 상황 설명 카드
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
                    text = "문제 상황: Row로 앱바 직접 구현",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TopAppBar 없이 Row를 사용하면\n" +
                            "많은 것들을 직접 구현해야 합니다.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 직접 구현한 앱바 (문제 시연)
        Text(
            text = "직접 구현한 앱바:",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        ManualAppBar()

        Spacer(modifier = Modifier.height(16.dp))

        // 문제점 목록
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
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem(
                    number = 1,
                    title = "높이/패딩 직접 지정",
                    description = "56.dp, 16.dp 등 수치를 직접 지정해야 함.\n" +
                            "Material Design 가이드라인을 위반할 수 있음."
                )

                ProblemItem(
                    number = 2,
                    title = "색상 하드코딩",
                    description = "Color(0xFF6200EE)처럼 직접 색상 지정.\n" +
                            "다크모드에서 자동 전환되지 않음."
                )

                ProblemItem(
                    number = 3,
                    title = "스크롤 연동 없음",
                    description = "스크롤 시 축소/확장 효과를 원하면\n" +
                            "직접 구현해야 함."
                )

                ProblemItem(
                    number = 4,
                    title = "StatusBar 처리 복잡",
                    description = "시스템 UI 영역과의 충돌을 직접 처리해야 함.\n" +
                            "WindowInsets 계산 필요."
                )

                ProblemItem(
                    number = 5,
                    title = "일관성 부족",
                    description = "아이콘 간격, 터치 영역, 리플 효과 등을\n" +
                            "일일이 맞춰야 함."
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 예시
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드 예시",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
// 문제가 있는 직접 구현
Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)  // 하드코딩
        .background(Color(0xFF6200EE))  // 하드코딩
        .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    IconButton(onClick = { }) {
        Icon(
            Icons.Default.ArrowBack,
            "뒤로가기",
            tint = Color.White  // 다크모드 대응 안됨
        )
    }
    Text(
        text = "제목",
        modifier = Modifier.weight(1f),
        color = Color.White,  // 다크모드 대응 안됨
        fontSize = 20.sp  // Typography 미사용
    )
    // ... 더 많은 수동 설정 필요
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 해결책 안내
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solution 탭에서 TopAppBar를 사용한\n" +
                            "올바른 구현 방법을 확인하세요!",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

/**
 * 직접 구현한 앱바 (안티패턴 예시)
 *
 * TopAppBar 없이 Row를 사용하여 앱바를 만든 예시입니다.
 * 색상, 높이, 패딩 등을 모두 하드코딩해야 하는 문제가 있습니다.
 */
@Composable
fun ManualAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)  // 문제 1: 높이 하드코딩
            .background(Color(0xFF6200EE))  // 문제 2: 색상 하드코딩
            .padding(horizontal = 4.dp),  // 문제 1: 패딩 직접 지정
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* 뒤로가기 */ }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color.White  // 문제 2: 색상 하드코딩
            )
        }

        Text(
            text = "직접 만든 앱바",
            modifier = Modifier.weight(1f),
            color = Color.White,  // 문제 2: 색상 하드코딩
            fontSize = 20.sp,  // 문제: Typography 미사용
            fontWeight = FontWeight.Medium
        )

        IconButton(onClick = { /* 검색 */ }) {
            Icon(
                Icons.Default.Search,
                contentDescription = "검색",
                tint = Color.White
            )
        }

        IconButton(onClick = { /* 더보기 */ }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "더보기",
                tint = Color.White
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.error,
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number.toString(),
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
