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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: MaterialExpressiveTheme 적용하기 (쉬움)
 *
 * 기본 MaterialTheme을 MaterialExpressiveTheme으로 교체하는 연습입니다.
 * Theme.kt에서 이미 구현되어 있으니 참고하세요.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Practice1_ExpressiveTheme() {
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
                    text = "연습 1: MaterialExpressiveTheme 적용하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("기존 MaterialTheme을 MaterialExpressiveTheme으로")
                        appendLine("교체하는 방법을 학습합니다.")
                        appendLine()
                        appendLine("난이도: 쉬움")
                    },
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        appendLine("1. @OptIn(ExperimentalMaterial3ExpressiveApi::class) 추가")
                        appendLine("2. MaterialTheme 대신 MaterialExpressiveTheme 사용")
                        appendLine("3. motionScheme = MotionScheme.expressive() 전달")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 연습 영역
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "연습 영역",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Practice1_Exercise()
            }
        }

        // 정답
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드",
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
fun MyExpressiveTheme(
    content: @Composable () -> Unit
) {
    MaterialExpressiveTheme(
        colorScheme = lightColorScheme(),
        motionScheme = MotionScheme.expressive(),
        typography = Typography,
        content = content
    )
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 이 앱이 MaterialExpressiveTheme으로 감싸져 있는지 확인하세요.
    // Theme.kt의 MaterialExpressiveStudyTheme을 참고하세요.

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "이 앱은 이미 MaterialExpressiveTheme을 사용합니다!",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "MainActivity.kt와 Theme.kt를 확인해보세요.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 연습 문제 2: Spatial Spec으로 버튼 애니메이션 (중간)
 *
 * defaultSpatialSpec을 사용하여 버튼 클릭 시
 * 스프링 스케일 애니메이션을 구현합니다.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Practice2_SpatialAnimation() {
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
                    text = "연습 2: Spatial Spec 버튼 애니메이션",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("defaultSpatialSpec을 사용하여")
                        appendLine("버튼 클릭 시 스프링 스케일 애니메이션을 구현하세요.")
                        appendLine()
                        appendLine("난이도: 중간")
                    },
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        appendLine("1. MaterialTheme.motionScheme.defaultSpatialSpec<Float>() 사용")
                        appendLine("2. animateFloatAsState의 animationSpec에 전달")
                        appendLine("3. Modifier.scale(scale)로 버튼에 적용")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 연습 영역
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "연습 영역 - 버튼을 눌러보세요!",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Practice2_Exercise()
            }
        }

        // 정답
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드",
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
fun SpringScaleButton() {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource
        .collectIsPressedAsState()

    val animationSpec = MaterialTheme
        .motionScheme
        .defaultSpatialSpec<Float>()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = animationSpec,
        label = "scale"
    )

    Button(
        onClick = { },
        modifier = Modifier.scale(scale),
        interactionSource = interactionSource
    ) {
        Text("Spring Button")
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Practice2_Exercise() {
    // TODO: defaultSpatialSpec을 사용하여 스프링 스케일 버튼을 구현하세요.
    // 요구사항:
    // 1. MutableInteractionSource를 생성하세요
    // 2. collectIsPressedAsState()로 눌림 상태를 감지하세요
    // 3. MaterialTheme.motionScheme.defaultSpatialSpec<Float>()를 사용하세요
    // 4. animateFloatAsState로 scale 값을 애니메이션하세요 (눌림: 0.9f, 기본: 1f)
    // 5. Button에 Modifier.scale(scale)과 interactionSource를 적용하세요

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // TODO: spatialSpec을 정의하세요
    // val animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()

    // TODO: scale 애니메이션을 정의하세요
    // val scale by animateFloatAsState(...)

    Button(
        onClick = { },
        // TODO: Modifier.scale(scale)을 추가하세요
        interactionSource = interactionSource
    ) {
        Icon(Icons.Filled.TouchApp, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("눌러서 스프링 확인!")
    }
}

/**
 * 연습 문제 3: FAB 메뉴 구현 (어려움)
 *
 * FloatingActionButtonMenu와 ToggleFloatingActionButton을 조합하여
 * 확장 가능한 FAB 메뉴를 구현합니다.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Practice3_FabMenu() {
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
                    text = "연습 3: FAB 메뉴 구현",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("FloatingActionButtonMenu와")
                        appendLine("ToggleFloatingActionButton을 조합하여")
                        appendLine("확장 가능한 FAB 메뉴를 구현하세요.")
                        appendLine()
                        appendLine("난이도: 어려움")
                    },
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        appendLine("1. rememberSaveable로 expanded 상태 관리")
                        appendLine("2. ToggleFloatingActionButton의 checked 상태 연결")
                        appendLine("3. FloatingActionButtonMenu에 button과 content 제공")
                        appendLine("4. FloatingActionButtonMenuItem으로 메뉴 아이템 추가")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 연습 영역
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "연습 영역 - FAB 메뉴 구현",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Practice3_Exercise()
            }
        }

        // 정답
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "정답 코드",
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
fun ExpandableFabMenu() {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    // BackHandler(expanded) { expanded = false }

    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            ToggleFloatingActionButton(
                checked = expanded,
                onCheckedChange = { expanded = it }
            ) {
                val icon = if (checkedProgress > 0.5f)
                    Icons.Filled.Close
                else
                    Icons.Filled.Add
                Icon(icon, contentDescription = null)
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = { expanded = false },
            icon = { Icon(Icons.Filled.Edit, null) },
            text = { Text("Edit") }
        )
        FloatingActionButtonMenuItem(
            onClick = { expanded = false },
            icon = { Icon(Icons.Filled.Share, null) },
            text = { Text("Share") }
        )
    }
}
                        """.trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        // 주의사항
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "주의사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        appendLine("FloatingActionButtonMenu는 1.4.0-alpha17 이상에서만 사용 가능합니다.")
                        appendLine("아래 데모는 컴포넌트의 개념을 보여주는 간략화된 버전입니다.")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Practice3_Exercise() {
    // TODO: 확장 가능한 FAB 메뉴를 구현하세요
    // 요구사항:
    // 1. expanded 상태 변수를 생성하세요
    // 2. expanded가 true일 때 메뉴 아이템들(Edit, Share, Delete)을 표시하세요
    // 3. 메인 FAB 클릭 시 expanded를 토글하세요
    // 4. expanded 상태에 따라 아이콘을 변경하세요 (Add ↔ Close)
    // 5. 메뉴 아이템 클릭 시 expanded를 false로 설정하세요

    // TODO: expanded 상태 변수를 생성하세요
    // var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TODO: expanded가 true일 때 메뉴 아이템들을 표시하세요
        // SmallFloatingActionButton을 사용하세요

        // 메인 FAB
        FloatingActionButton(
            onClick = { /* TODO: expanded 토글 */ }
        ) {
            Icon(
                imageVector = Icons.Filled.Add, // TODO: expanded에 따라 아이콘 변경
                contentDescription = "Expand"
            )
        }

        Text(
            text = "FAB를 눌러 메뉴 열기",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
