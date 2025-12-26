package com.example.composition_local

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ============================================================
// 연습 문제 1: LocalSpacing 만들기 (기초)
// ============================================================

/**
 * 연습 문제 1: LocalSpacing 만들기
 *
 * 요구사항:
 * - Spacing 데이터 클래스 정의 (small, medium, large)
 * - compositionLocalOf로 LocalSpacing 생성
 * - CompositionLocalProvider로 값 제공
 * - LocalSpacing.current로 패딩 적용
 *
 * TODO: 아래 주석을 해제하고 완성하세요!
 */

// TODO 1: Spacing 데이터 클래스 정의
/*
data class Spacing(
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 16.dp
)
*/

// TODO 2: LocalSpacing CompositionLocal 정의
/*
val LocalSpacing = compositionLocalOf { Spacing() }
*/

@Composable
fun Practice1_SpacingScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: LocalSpacing 만들기",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Spacing(small, medium, large) 데이터 클래스 정의")
                Text("2. compositionLocalOf { Spacing() } 로 LocalSpacing 생성")
                Text("3. CompositionLocalProvider로 커스텀 값 제공")
                Text("4. LocalSpacing.current.medium 등으로 접근")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 3: CompositionLocalProvider로 커스텀 Spacing 제공
        // 힌트: CompositionLocalProvider(LocalSpacing provides Spacing(8.dp, 16.dp, 32.dp))

        /*
        CompositionLocalProvider(
            LocalSpacing provides Spacing(
                small = 8.dp,
                medium = 16.dp,
                large = 32.dp
            )
        ) {
            SpacingDemoContent()
        }
        */

        // 임시 표시 (TODO 완성 후 삭제)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("위 주석을 해제하고 완성하세요!")
                Text("Practice.kt 파일을 수정하세요.")
            }
        }
    }
}

// TODO 4: LocalSpacing.current로 패딩 적용
/*
@Composable
fun SpacingDemoContent() {
    val spacing = LocalSpacing.current

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.small)
        ) {
            Text(
                text = "Small 패딩: ${spacing.small}",
                modifier = Modifier.padding(spacing.small)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
        ) {
            Text(
                text = "Medium 패딩: ${spacing.medium}",
                modifier = Modifier.padding(spacing.medium)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.large)
        ) {
            Text(
                text = "Large 패딩: ${spacing.large}",
                modifier = Modifier.padding(spacing.large)
            )
        }
    }
}
*/

// ============================================================
// 연습 문제 2: static vs dynamic 비교 (중급)
// ============================================================

/**
 * 연습 문제 2: static vs dynamic 비교
 *
 * 요구사항:
 * - compositionLocalOf로 LocalDynamicCounter 생성
 * - staticCompositionLocalOf로 LocalStaticCounter 생성
 * - 버튼 클릭으로 값 변경 시 recomposition 범위 비교
 *
 * TODO: 아래 주석을 해제하고 완성하세요!
 */

// TODO 1: Dynamic CompositionLocal (값 변경 시 읽는 부분만 recompose)
/*
val LocalDynamicCounter = compositionLocalOf { 0 }
*/

// TODO 2: Static CompositionLocal (값 변경 시 전체 content lambda recompose)
/*
val LocalStaticCounter = staticCompositionLocalOf { 0 }
*/

@Composable
fun Practice2_StaticVsDynamicScreen() {
    val scrollState = rememberScrollState()

    // TODO 3: 카운터 상태
    /*
    var dynamicCount by remember { mutableIntStateOf(0) }
    var staticCount by remember { mutableIntStateOf(0) }
    */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: static vs dynamic",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. compositionLocalOf: 읽는 부분만 recompose")
                Text("2. staticCompositionLocalOf: 전체 content lambda recompose")
                Text("3. SideEffect로 recomposition 횟수 추적")
                Text("4. 로그를 확인하여 차이점 관찰")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 4: 두 Provider를 설정하고 비교
        /*
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { dynamicCount++ }) {
                Text("Dynamic +1")
            }
            Button(onClick = { staticCount++ }) {
                Text("Static +1")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // Dynamic Provider
            CompositionLocalProvider(LocalDynamicCounter provides dynamicCount) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Dynamic", style = MaterialTheme.typography.titleSmall)
                    DynamicReaderCard()
                    NonReaderCard("Dynamic")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Static Provider
            CompositionLocalProvider(LocalStaticCounter provides staticCount) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Static", style = MaterialTheme.typography.titleSmall)
                    StaticReaderCard()
                    NonReaderCard("Static")
                }
            }
        }
        */

        // 임시 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("위 주석을 해제하고 완성하세요!")
                Text("Logcat에서 recomposition 로그를 확인하세요.")
            }
        }
    }
}

// TODO 5: Dynamic 값을 읽는 카드
/*
@Composable
fun DynamicReaderCard() {
    val value = LocalDynamicCounter.current

    SideEffect {
        println("DynamicReaderCard recomposed: $value")
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Text(
            text = "읽는 카드: $value",
            modifier = Modifier.padding(8.dp)
        )
    }
}
*/

// TODO 6: Static 값을 읽는 카드
/*
@Composable
fun StaticReaderCard() {
    val value = LocalStaticCounter.current

    SideEffect {
        println("StaticReaderCard recomposed: $value")
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC))
    ) {
        Text(
            text = "읽는 카드: $value",
            modifier = Modifier.padding(8.dp)
        )
    }
}
*/

// TODO 7: 값을 읽지 않는 카드 (static에서는 이것도 recompose됨!)
/*
@Composable
fun NonReaderCard(type: String) {
    SideEffect {
        println("NonReaderCard ($type) recomposed")
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Text(
            text = "안 읽는 카드",
            modifier = Modifier.padding(8.dp)
        )
    }
}
*/

// ============================================================
// 연습 문제 3: 중첩 Provider 활용 (고급)
// ============================================================

/**
 * 연습 문제 3: 중첩 Provider 활용
 *
 * 요구사항:
 * - LocalTheme CompositionLocal 정의
 * - 최상위에서 기본 테마 제공
 * - 특정 카드 영역에서 강조 테마로 오버라이드
 *
 * TODO: 아래 주석을 해제하고 완성하세요!
 */

// TODO 1: Theme 데이터 클래스 정의
/*
data class Theme(
    val backgroundColor: Color = Color.White,
    val textColor: Color = Color.Black,
    val accentColor: Color = Color(0xFF6200EE),
    val name: String = "Default"
)
*/

// TODO 2: LocalTheme CompositionLocal 정의
/*
val LocalTheme = compositionLocalOf { Theme() }
*/

@Composable
fun Practice3_NestedProviderScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: 중첩 Provider",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("힌트", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Theme(backgroundColor, textColor, accentColor) 정의")
                Text("2. 최상위에서 기본 테마 제공")
                Text("3. CompositionLocalProvider 중첩으로 특정 영역 오버라이드")
                Text("4. LocalTheme.current로 현재 테마 접근")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO 3: 기본 테마와 강조 테마 정의
        /*
        val defaultTheme = Theme(
            backgroundColor = Color(0xFFF5F5F5),
            textColor = Color.Black,
            accentColor = Color(0xFF6200EE),
            name = "Default"
        )

        val highlightTheme = Theme(
            backgroundColor = Color(0xFFFF5722),
            textColor = Color.White,
            accentColor = Color(0xFFFFEB3B),
            name = "Highlight"
        )

        val darkTheme = Theme(
            backgroundColor = Color(0xFF121212),
            textColor = Color.White,
            accentColor = Color(0xFF03DAC5),
            name = "Dark"
        )
        */

        // TODO 4: 중첩 Provider로 영역별 테마 적용
        /*
        CompositionLocalProvider(LocalTheme provides defaultTheme) {
            Column {
                ThemedCard("기본 테마 영역 1")

                Spacer(modifier = Modifier.height(8.dp))

                // 강조 테마 영역
                CompositionLocalProvider(LocalTheme provides highlightTheme) {
                    ThemedCard("강조 테마 영역")

                    Spacer(modifier = Modifier.height(8.dp))

                    // 다크 테마 영역 (2중 중첩)
                    CompositionLocalProvider(LocalTheme provides darkTheme) {
                        ThemedCard("다크 테마 영역 (2중 중첩)")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ThemedCard("기본 테마 영역 2")
            }
        }
        */

        // 임시 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("위 주석을 해제하고 완성하세요!")
                Text("중첩 Provider로 영역별 테마가 적용됩니다.")
            }
        }
    }
}

// TODO 5: 테마가 적용된 카드
/*
@Composable
fun ThemedCard(title: String) {
    val theme = LocalTheme.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = theme.backgroundColor
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                color = theme.textColor,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "현재 테마: ${theme.name}",
                color = theme.textColor.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(theme.accentColor)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Accent Color",
                    color = if (theme.name == "Highlight") Color.Black else Color.White
                )
            }
        }
    }
}
*/
