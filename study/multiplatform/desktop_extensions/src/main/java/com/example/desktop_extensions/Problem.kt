package com.example.desktop_extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Problem 화면
 *
 * 모바일 UI를 Desktop에 그대로 사용할 때 발생하는 문제점을 시각적으로 보여줍니다.
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 상황 소개 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "문제 상황: 모바일 UI를 Desktop에 그대로 사용하면?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Android 앱의 UI를 Compose Multiplatform으로 Desktop에 포팅했을 때, " +
                            "모바일 패턴을 그대로 사용하면 데스크톱 사용자가 기대하는 경험을 제공하지 못합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 1: 메뉴 접근성
        ProblemCard(
            title = "문제 1: 메뉴 접근성",
            description = "데스크톱 사용자는 상단 메뉴바를 기대합니다"
        ) {
            ComparisonRow(
                mobileTitle = "모바일 방식",
                mobileContent = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                        Text("1. 햄버거 클릭", style = MaterialTheme.typography.bodySmall)
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                        Text("2. 드로어 열림", style = MaterialTheme.typography.bodySmall)
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                        Text("3. 메뉴 선택", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "= 3단계 필요",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                desktopTitle = "데스크톱 기대",
                desktopContent = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("File", style = MaterialTheme.typography.bodySmall)
                            Text("Edit", style = MaterialTheme.typography.bodySmall)
                            Text("View", style = MaterialTheme.typography.bodySmall)
                        }
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                        Text("바로 선택!", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "= 1단계로 완료",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        // 문제 2: 키보드 단축키 부재
        ProblemCard(
            title = "문제 2: 키보드 단축키 부재",
            description = "데스크톱 사용자는 키보드로 빠르게 작업하길 원합니다"
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ShortcutComparisonRow("복사", "길게 누르기 > 복사", "Ctrl+C")
                ShortcutComparisonRow("붙여넣기", "길게 누르기 > 붙여넣기", "Ctrl+V")
                ShortcutComparisonRow("저장", "버튼 클릭", "Ctrl+S")
                ShortcutComparisonRow("실행취소", "없거나 버튼", "Ctrl+Z")

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = "모바일에는 키보드 단축키 개념이 없음 -> 생산성 저하!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // 문제 3: 백그라운드 실행 불가
        ProblemCard(
            title = "문제 3: 백그라운드 실행 불가",
            description = "시스템 트레이 없이는 앱을 최소화 상태로 유지할 수 없습니다"
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 모바일
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("모바일", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null)
                                Text("앱 닫기", style = MaterialTheme.typography.bodySmall)
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                Icon(Icons.Default.Settings, contentDescription = null)
                                Text("백그라운드 서비스", style = MaterialTheme.typography.bodySmall)
                                Text("계속 실행!", color = Color(0xFF4CAF50))
                            }
                        }
                    }

                    // Desktop
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Desktop (트레이 없음)", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null)
                                Text("창 닫기", style = MaterialTheme.typography.bodySmall)
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red)
                                Text("앱 완전 종료!", style = MaterialTheme.typography.bodySmall)
                                Text("메시지 알림 못 받음", color = Color.Red)
                            }
                        }
                    }
                }

                Text(
                    text = "예: 메신저 앱 - 창을 닫아도 메시지 알림을 받아야 함",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        // 문제 4: 창 관리 불가
        ProblemCard(
            title = "문제 4: 창 관리 불가",
            description = "데스크톱에서는 창 크기와 위치를 자유롭게 조절해야 합니다"
        ) {
            ComparisonRow(
                mobileTitle = "모바일",
                mobileContent = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp, 100.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "전체\n화면",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                        Text("항상 전체 화면", style = MaterialTheme.typography.bodySmall)
                    }
                },
                desktopTitle = "데스크톱",
                desktopContent = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp, 30.dp)
                                    .background(
                                        MaterialTheme.colorScheme.secondary,
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .size(30.dp, 40.dp)
                                    .background(
                                        MaterialTheme.colorScheme.tertiary,
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                        Text("다양한 크기", style = MaterialTheme.typography.bodySmall)
                        Text("+ 위치 기억", style = MaterialTheme.typography.bodySmall)
                        Text("+ 다중 창", style = MaterialTheme.typography.bodySmall)
                    }
                }
            )
        }

        // 결론
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "결론",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "모바일 UI를 Desktop에 그대로 사용하면 데스크톱 사용자가 기대하는 " +
                            "네이티브 경험을 제공하지 못합니다.\n\n" +
                            "해결책: Desktop Extensions를 사용하여 MenuBar, Tray, 키보드 단축키 등을 구현!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "-> Solution 탭에서 Desktop Extensions 사용법을 알아보세요!",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ProblemCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun ComparisonRow(
    mobileTitle: String,
    mobileContent: @Composable () -> Unit,
    desktopTitle: String,
    desktopContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 모바일
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                mobileTitle,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                ),
                modifier = Modifier.padding(4.dp)
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    mobileContent()
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 데스크톱
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                desktopTitle,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                ),
                modifier = Modifier.padding(4.dp)
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    desktopContent()
                }
            }
        }
    }
}

@Composable
private fun ShortcutComparisonRow(
    action: String,
    mobileWay: String,
    desktopWay: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(4.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            action,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.3f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            mobileWay,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(0.35f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            desktopWay,
            color = Color(0xFF4CAF50),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.35f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
