package com.example.material_expressive

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 문제 상황: 기존 Material 3의 딱딱한 애니메이션
 *
 * 이 화면은 기존 Material 3에서 사용하던 방식을 보여줍니다.
 * tween 기반의 선형 애니메이션은 딱딱하고 기계적입니다.
 *
 * 문제점:
 * 1. tween() 애니메이션 - 로봇처럼 딱딱한 움직임
 * 2. 기본 RoundedCornerShape만 사용 가능
 * 3. 기본 컴포넌트의 제한된 표현력
 * 4. 사용자에게 감성적 반응 전달 어려움
 *
 * @see SolutionScreen Material Expressive로 개선된 버전
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
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: 기존 Material 3의 딱딱한 애니메이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("기존 방식의 한계:")
                        appendLine("- tween() 기반 선형 애니메이션")
                        appendLine("- 로봇처럼 딱딱한 움직임")
                        appendLine("- 자연스러운 바운스 효과 없음")
                        append("- 제한된 Shape 옵션")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 비교 포인트 1: 딱딱한 버튼 애니메이션
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. 딱딱한 버튼 애니메이션",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "버튼을 누르면 tween(300)으로 선형 축소됩니다.\n스프링 효과 없이 딱딱하게 움직입니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 딱딱한 tween 애니메이션 버튼
                TweenAnimatedButton()
            }
        }

        // 비교 포인트 2: 기본 FAB
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. 기본 FAB (제한된 표현력)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기존 ExtendedFloatingActionButton은 크기와\n스타일 옵션이 제한적입니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // 기본 FAB
                    ExtendedFloatingActionButton(
                        onClick = { },
                        icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                        text = { Text("기본 FAB") }
                    )
                }
            }
        }

        // 비교 포인트 3: 기본 프로그레스 인디케이터
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. 기본 Progress Indicator",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기존 프로그레스 인디케이터는 단순한 형태로\n시각적 재미가 부족합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 기본 Circular
                    CircularProgressIndicator()

                    // 기본 Linear
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
        }

        // 비교 포인트 4: 기본 Shape
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "4. 제한된 Shape 옵션",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기존에는 RoundedCornerShape, CircleShape 등\n기본적인 Shape만 사용 가능했습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 기본 둥근 모서리
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("8dp", color = Color.White, fontSize = 12.sp)
                    }

                    // 더 둥근 모서리
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("16dp", color = Color.White, fontSize = 12.sp)
                    }

                    // 원형
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Circle", color = Color.White, fontSize = 10.sp)
                    }
                }
            }
        }

        // 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "기존 코드 예시 (tween 애니메이션)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
// 기존 방식: tween 기반 애니메이션
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.9f else 1f,
    animationSpec = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
)

// 문제점:
// - 선형적인 움직임
// - 자연스러운 바운스 없음
// - 기계적인 느낌
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * tween 애니메이션을 사용한 버튼
 *
 * 클릭 시 선형적으로 축소되어 딱딱한 느낌을 줍니다.
 */
@Composable
private fun TweenAnimatedButton() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // tween 기반 딱딱한 애니메이션
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 300), // 선형 애니메이션
        label = "tween_scale"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { },
            modifier = Modifier.scale(scale),
            interactionSource = interactionSource
        ) {
            Icon(Icons.Filled.Favorite, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("tween(300) - 딱딱함")
        }
    }
}
