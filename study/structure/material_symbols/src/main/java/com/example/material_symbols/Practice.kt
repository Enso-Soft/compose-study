package com.example.material_symbols

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 기본 아이콘 표시 (쉬움)
 *
 * 설정 화면의 메뉴 아이템을 만들어봅니다.
 */
@Composable
fun Practice1_BasicIcon() {
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
                    text = "연습 1: 기본 아이콘 표시",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "설정 화면의 메뉴 아이템을 만들어보세요. " +
                            "각 메뉴는 아이콘과 텍스트로 구성됩니다.",
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
                Text("- R.drawable.ic_settings 리소스 사용", style = MaterialTheme.typography.bodySmall)
                Text("- ImageVector.vectorResource()로 아이콘 로드", style = MaterialTheme.typography.bodySmall)
                Text("- Row로 아이콘과 텍스트 가로 배치", style = MaterialTheme.typography.bodySmall)
                Text("- Spacer로 간격 추가", style = MaterialTheme.typography.bodySmall)
            }
        }

        // 연습 영역
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 구현해보세요",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Practice1_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice1_Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 여기에 메뉴 아이템을 구현하세요
    // 1. Row 안에 Icon과 Text 배치
    // 2. ImageVector.vectorResource(R.drawable.ic_settings) 사용
    // 3. contentDescription 제공
    // 4. Modifier.size(24.dp)로 아이콘 크기 설정

    Text(
        text = "TODO: 여기에 설정 메뉴를 구현하세요",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun Practice1_Answer() {
    // 정답: 설정 메뉴 아이템
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
            contentDescription = "설정",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "설정",
            style = MaterialTheme.typography.bodyLarge
        )
    }

    HorizontalDivider()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
            contentDescription = "알림",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "알림",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * 연습 문제 2: 스타일별 아이콘 갤러리 (중간)
 *
 * 여러 스타일의 아이콘을 비교하는 갤러리를 만들어봅니다.
 */
@Composable
fun Practice2_StyleGallery() {
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
                    text = "연습 2: 스타일별 아이콘 갤러리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Outlined, Rounded, Sharp 세 가지 스타일의 아이콘을 나란히 보여주고, " +
                            "클릭하면 선택 표시가 되는 갤러리를 만들어보세요.",
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
                Text("- remember { mutableStateOf(0) }로 선택 상태 관리", style = MaterialTheme.typography.bodySmall)
                Text("- Row로 가로 배치, Column으로 아이콘+라벨 배치", style = MaterialTheme.typography.bodySmall)
                Text("- Modifier.border()로 선택 효과 표현", style = MaterialTheme.typography.bodySmall)
                Text("- Modifier.clickable()로 클릭 처리", style = MaterialTheme.typography.bodySmall)
            }
        }

        // 연습 영역
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 구현해보세요",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Practice2_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice2_Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 여기에 스타일 갤러리를 구현하세요
    // 1. 선택 상태를 관리하는 state 생성
    // 2. Row로 3개 스타일 가로 배치
    // 3. 각 스타일은 Column(아이콘 + 라벨)
    // 4. 선택된 스타일에 테두리 표시
    // 5. 클릭하면 선택 변경

    Text(
        text = "TODO: 여기에 스타일 갤러리를 구현하세요",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun Practice2_Answer() {
    var selectedStyle by remember { mutableIntStateOf(0) }

    val styles = listOf(
        Triple("Outlined", R.drawable.ic_home, "깔끔"),
        Triple("Rounded", R.drawable.ic_home_rounded, "부드럽"),
        Triple("Sharp", R.drawable.ic_home_sharp, "날카롭")
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        styles.forEachIndexed { index, (name, iconRes, desc) ->
            val isSelected = selectedStyle == index

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                2.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(12.dp)
                            )
                        } else Modifier
                    )
                    .clickable { selectedStyle = index }
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(iconRes),
                    contentDescription = name,
                    modifier = Modifier.size(40.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "선택된 스타일: ${styles[selectedStyle].first}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * 연습 문제 3: 동적 테마 아이콘 (어려움)
 *
 * 테마에 따라 동적으로 변하는 아이콘 시스템을 구현합니다.
 */
@Composable
fun Practice3_DynamicTheme() {
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
                    text = "연습 3: 동적 테마 아이콘",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Slider로 아이콘 크기를 조절하고, 테마 색상과 연동되는 아이콘 시스템을 만들어보세요. " +
                            "크기 변경 시 부드러운 애니메이션이 적용되어야 합니다.",
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
                Text("- animateDpAsState로 부드러운 크기 전환", style = MaterialTheme.typography.bodySmall)
                Text("- Slider의 value는 0f~1f, 20~48dp로 변환", style = MaterialTheme.typography.bodySmall)
                Text("- MaterialTheme.colorScheme.primary 등 테마 색상 사용", style = MaterialTheme.typography.bodySmall)
                Text("- 여러 아이콘을 LazyRow 또는 Row로 표시", style = MaterialTheme.typography.bodySmall)
            }
        }

        // 연습 영역
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "직접 구현해보세요",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Practice3_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "정답:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Practice3_Answer()
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 여기에 동적 테마 아이콘을 구현하세요
    // 1. Slider로 아이콘 크기 조절 (20dp ~ 48dp)
    // 2. animateDpAsState로 부드러운 애니메이션
    // 3. 여러 아이콘을 테마 색상으로 표시
    // 4. 현재 크기 텍스트로 표시

    Text(
        text = "TODO: 여기에 동적 테마 아이콘을 구현하세요",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun Practice3_Answer() {
    // Slider 값 (0f ~ 1f)
    var sliderValue by remember { mutableFloatStateOf(0.14f) } // 24dp 초기값

    // 20dp ~ 48dp 범위로 변환
    val targetSize = (20 + (sliderValue * 28)).dp

    // 애니메이션 적용
    val animatedSize: Dp by animateDpAsState(
        targetValue = targetSize,
        label = "iconSize"
    )

    val icons = listOf(
        Pair(R.drawable.ic_home, MaterialTheme.colorScheme.primary),
        Pair(R.drawable.ic_favorite, MaterialTheme.colorScheme.error),
        Pair(R.drawable.ic_settings, MaterialTheme.colorScheme.secondary),
        Pair(R.drawable.ic_notifications, MaterialTheme.colorScheme.tertiary)
    )

    Column {
        // 크기 표시
        Text(
            text = "아이콘 크기: ${animatedSize.value.toInt()}dp",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 슬라이더
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("20dp", style = MaterialTheme.typography.labelSmall)
            Text("48dp", style = MaterialTheme.typography.labelSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 아이콘 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            icons.forEach { (iconRes, color) ->
                Icon(
                    imageVector = ImageVector.vectorResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(animatedSize),
                    tint = color
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 테마 정보
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "현재 테마 색상:",
                    style = MaterialTheme.typography.labelMedium
                )
                Text("Primary, Error, Secondary, Tertiary", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = if (isSystemInDarkTheme()) "다크 모드" else "라이트 모드",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
