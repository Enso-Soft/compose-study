package com.example.pausable_composition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pausable_composition.ui.theme.PerformanceBad
import com.example.pausable_composition.ui.theme.PerformanceGood

/**
 * 해결책 화면
 *
 * Pausable Composition이 어떻게 jank 문제를 해결하는지 설명합니다.
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
        // 해결책 소개 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Pausable Composition",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        Compose 1.10 (2025년 12월)부터 기본 활성화된 성능 최적화 기술입니다.

                        컴포지션(UI 구성 작업)을 일시정지하고 여러 프레임에 분산하여 jank를 방지합니다.

                        별도 설정 없이 자동으로 동작합니다!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 비교 시각화
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Before vs After",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Before
                Text(
                    text = "이전 (Non-pausable):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .background(PerformanceBad, RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = "50ms - 모든 아이템을 한 번에",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 8.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Text(
                    text = "JANK 발생!",
                    color = PerformanceBad,
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                // After
                Text(
                    text = "이후 (Pausable):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .background(PerformanceGood, RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = "15ms",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .background(PerformanceGood, RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = "15ms",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .background(PerformanceGood, RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = "15ms",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Text(
                    text = "각 프레임 16ms 이내, 부드러운 스크롤!",
                    color = PerformanceGood,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // 핵심 메커니즘 3가지
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 메커니즘 3가지",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 1. 사전 준비
                MechanismItem(
                    number = "1",
                    title = "사전 준비 (Pre-work)",
                    description = "화면에 보이기 전에 미리 아이템을 구성합니다 (prefetch)"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2. 협력적 일시중지
                MechanismItem(
                    number = "2",
                    title = "협력적 일시중지 (Cooperative Pause)",
                    description = "\"지금 멈춰야 할까요?\" 라고 묻는 shouldPause 콜백으로 프레임 보호"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 3. 배치 처리
                MechanismItem(
                    number = "3",
                    title = "배치 처리 (Batch Apply)",
                    description = "여러 작은 변경사항을 모아서 한 번에 적용"
                )
            }
        }

        // 상태 머신 다이어그램
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "내부 상태 흐름",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                StateMachineDiagram()
            }
        }

        // API 구조 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "API 구조 (내부 동작 이해용)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pausable Composition은 내부 API이므로 직접 호출하지 않습니다.\n원리를 이해하면 왜 성능이 개선되는지 알 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 코드 블록
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = """
// PausableComposition 인터페이스
sealed interface PausableComposition
    : ReusableComposition {

    fun setPausableContent(
        content: @Composable () -> Unit
    ): PausedComposition
}

// PausedComposition 인터페이스
sealed interface PausedComposition {
    val isComplete: Boolean

    fun resume(
        shouldPause: ShouldPauseCallback
    ): Boolean

    fun apply()
    fun cancel()
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 비유 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "비유: 택배 배송 센터",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        Pausable Composition 방식:
                        "시간이 되면 실린 만큼만 먼저 출발하고,
                        나머지는 다음 트럭에 싣습니다."

                        정시 출발 보장 (= no jank)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 활성화 방법
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "활성화 방법",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Compose 1.10 이상 (2025.12+)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PerformanceGood
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = """
// 별도 설정 불필요! 자동 활성화

// build.gradle.kts
dependencies {
    implementation(platform(
        "androidx.compose:compose-bom:2025.12.00"
    ))
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Compose 1.9 (수동 활성화 필요)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = """
// Application 클래스에서 설정
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ComposeFoundationFlags
          .isPausableCompositionInPrefetchEnabled
          = true
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // 핵심 포인트 정리
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 컴포지션을 여러 프레임에 분산하여 jank 방지")
                Text("2. shouldPause 콜백으로 현재 프레임 렌더링 보호")
                Text("3. Compose 1.10부터 자동 활성화, 설정 불필요")
                Text("4. Lazy 레이아웃 prefetch에서 가장 효과적")
            }
        }
    }
}

@Composable
private fun MechanismItem(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(24.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number,
                    color = MaterialTheme.colorScheme.onPrimary,
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
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun StateMachineDiagram() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StateBox(
            text = "InitialPending",
            description = "작업 시작 준비"
        )

        Arrow()

        StateBox(
            text = "RecomposePending",
            description = "구성 진행 중 (일시정지 가능)"
        )

        Arrow()

        StateBox(
            text = "ApplyPending",
            description = "변경사항 적용 준비"
        )

        Arrow()

        StateBox(
            text = "Applied",
            description = "완료!",
            isTerminal = true
        )
    }
}

@Composable
private fun StateBox(
    text: String,
    description: String,
    isTerminal: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .border(
                width = 2.dp,
                color = if (isTerminal)
                    PerformanceGood
                else
                    MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        shape = RoundedCornerShape(8.dp),
        color = if (isTerminal)
            PerformanceGood.copy(alpha = 0.1f)
        else
            MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isTerminal) PerformanceGood else Color.Unspecified
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Arrow() {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .size(24.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
