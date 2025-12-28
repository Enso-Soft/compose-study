package com.example.material_expressive

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
 * 해결책: Material 3 Expressive로 생동감 있는 UI
 *
 * Material 3 Expressive의 핵심 기능들을 활용한 화면입니다.
 * 스프링 물리 기반 애니메이션으로 자연스러운 인터랙션을 제공합니다.
 *
 * 핵심 기능:
 * 1. MotionScheme.expressive() - 스프링 물리 기반 모션
 * 2. defaultSpatialSpec - 공간적 변화용 애니메이션 스펙
 * 3. 새로운 컴포넌트들 - LargeExtendedFAB, WavyProgress 등
 * 4. 확장된 Shape 시스템
 *
 * @see ProblemScreen 기존 방식과 비교
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: Material 3 Expressive",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("Material Expressive 장점:")
                        appendLine("- 스프링 물리 기반 자연스러운 모션")
                        appendLine("- 탄력있게 튕기는 바운스 효과")
                        appendLine("- 35개의 새로운 Shape")
                        append("- 15개의 새로운/업데이트된 컴포넌트")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 비교 포인트 1: 스프링 버튼 애니메이션
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. 스프링 버튼 애니메이션",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "버튼을 누르면 defaultSpatialSpec으로 탄력있게 축소됩니다.\n실제 스프링처럼 자연스럽게 튕깁니다!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 스프링 물리 애니메이션 버튼
                SpringAnimatedButton()
            }
        }

        // 비교 포인트 2: 새로운 FAB
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. 새로운 FAB 컴포넌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "LargeExtendedFloatingActionButton으로\n더 크고 눈에 띄는 FAB를 만들 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Large Extended FAB
                    LargeExtendedFloatingActionButton(
                        onClick = { },
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(FloatingActionButtonDefaults.LargeIconSize)
                            )
                        },
                        text = { Text("Large Extended FAB") }
                    )

                    Text(
                        text = "기본 FAB보다 더 크고 표현력 있음",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 비교 포인트 3: Wavy Progress Indicator
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. Wavy Progress Indicator",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "물결 모양의 프로그레스 인디케이터로\n더 생동감 있는 로딩 표시를 제공합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Wavy Circular Progress
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularWavyProgressIndicator()
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Wavy", style = MaterialTheme.typography.labelSmall)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularWavyProgressIndicator(
                                progress = { 0.7f }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("70%", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    // Wavy Linear Progress
                    Column(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearWavyProgressIndicator()
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Linear Wavy Progress", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // 비교 포인트 4: 확장된 Shape 시스템
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "4. 확장된 Shape 시스템",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "MaterialShapes로 35개의 새로운 Shape에 접근할 수 있습니다.\n(실제 사용 시 graphics-shapes 라이브러리 필요)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 데모용 다양한 Shape
                    ShapeDemo(
                        name = "Pill",
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primary
                    )
                    ShapeDemo(
                        name = "Squircle",
                        shape = RoundedCornerShape(30),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    ShapeDemo(
                        name = "Diamond",
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 30.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 30.dp
                        ),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "MaterialShapes.Cookie9Sided, Clover4Leaf 등\n더 많은 Shape는 graphics-shapes 라이브러리에서 제공",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
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
                    text = "Expressive 코드 예시 (스프링 애니메이션)",
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
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SpringButton() {
    // 스프링 물리 기반 애니메이션 스펙
    val animationSpec = MaterialTheme
        .motionScheme
        .defaultSpatialSpec<Float>()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = animationSpec // 스프링!
    )

    // 장점:
    // - 탄력있는 바운스 효과
    // - 자연스러운 움직임
    // - 생동감 있는 인터랙션
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // MotionScheme 비교 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Spatial vs Effects Spec",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.OpenWith,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Spatial",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "위치, 크기\n회전",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "Effects",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = "색상, 투명도\n시각 효과",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * 스프링 물리 애니메이션을 사용한 버튼
 *
 * defaultSpatialSpec으로 탄력있게 튕기는 애니메이션을 적용합니다.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SpringAnimatedButton() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 스프링 물리 기반 애니메이션!
    val animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = animationSpec,
        label = "spring_scale"
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
            Text("Spring - 탄력있음!")
        }
    }
}

/**
 * Shape 데모 컴포넌트
 */
@Composable
private fun ShapeDemo(
    name: String,
    shape: RoundedCornerShape,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(shape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            // Empty - just showing shape
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
