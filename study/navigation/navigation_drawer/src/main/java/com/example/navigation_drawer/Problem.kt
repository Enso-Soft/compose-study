package com.example.navigation_drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: Navigation Drawer를 직접 구현하려 할 때의 문제점
 *
 * Box + AnimatedVisibility로 사이드 메뉴를 직접 만들면 다음 문제가 발생합니다:
 * 1. 스와이프 제스처 미지원 - 손가락으로 끌어서 열기/닫기 불가
 * 2. 스크림(반투명 배경) 처리 복잡
 * 3. 애니메이션 타이밍 맞추기 어려움
 * 4. 접근성(TalkBack) 미지원
 * 5. Predictive back gesture 미지원
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemScreen() {
    var isDrawerOpen by remember { mutableStateOf(false) }

    // 문제점을 보여주기 위한 직접 구현
    Box(modifier = Modifier.fillMaxSize()) {

        // ===== 메인 콘텐츠 영역 =====
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Problem: 직접 구현") },
                    navigationIcon = {
                        IconButton(onClick = { isDrawerOpen = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "메뉴 열기")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // 문제 설명 카드
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "직접 구현의 문제점",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Box + AnimatedVisibility로 만든 이 메뉴는:",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        val problems = listOf(
                            "1. 스와이프로 열 수 없습니다 (왼쪽 가장자리 스와이프 시도해보세요)",
                            "2. 스크림(어두운 배경) 클릭 처리가 복잡합니다",
                            "3. 애니메이션 타이밍이 완벽하지 않습니다",
                            "4. 접근성(TalkBack)을 지원하지 않습니다",
                            "5. Android 13+ 뒤로가기 제스처 미리보기가 안 됩니다"
                        )

                        problems.forEach { problem ->
                            Text(
                                text = problem,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 안내 카드
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "테스트해보세요!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. 햄버거 메뉴 버튼을 눌러 메뉴를 여세요\n" +
                                    "2. 화면 왼쪽 가장자리에서 오른쪽으로 스와이프해보세요\n" +
                                    "   -> 스와이프가 동작하지 않습니다!\n" +
                                    "3. Solution 탭에서 ModalNavigationDrawer를 확인하세요",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // ===== 직접 구현한 Drawer (문제가 있는 코드) =====

        // 문제 1: 스크림 수동 구현 필요
        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable {
                        // 문제 2: 클릭 이벤트 처리가 복잡해질 수 있음
                        isDrawerOpen = false
                    }
            )
        }

        // 문제 3: Drawer 애니메이션과 스크림 애니메이션 동기화가 완벽하지 않음
        AnimatedVisibility(
            visible = isDrawerOpen,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it }
        ) {
            // 문제 4: 스와이프 제스처가 없음!
            // 문제 5: 접근성 지원 없음
            // 문제 6: Predictive back gesture 지원 없음
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "직접 만든 메뉴",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                HorizontalDivider()

                // 간단한 메뉴 항목들
                ManualMenuItem(
                    text = "홈",
                    onClick = { isDrawerOpen = false }
                )
                ManualMenuItem(
                    text = "설정",
                    onClick = { isDrawerOpen = false }
                )
                ManualMenuItem(
                    text = "프로필",
                    onClick = { isDrawerOpen = false }
                )

                Spacer(modifier = Modifier.weight(1f))

                // 경고 메시지
                Text(
                    text = "이 메뉴는 스와이프로\n열 수 없습니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ManualMenuItem(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = text)
    }
}
