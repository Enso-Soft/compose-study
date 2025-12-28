# 패턴 A: 문제-해결 (Problem-Solution) 템플릿

이 템플릿은 **"없으면 문제가 발생하는"** 기술에 적용합니다.

## 적용 대상

- LaunchedEffect, DisposableEffect, SideEffect
- rememberCoroutineScope, rememberSaveable
- derivedStateOf, snapshotFlow, produceState

---

## 파일 구조

```
study/{category}/{module_name}/
├── README.md                    # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt
│   ├── Problem.kt
│   ├── Solution.kt
│   ├── Practice.kt
│   ├── README.md                # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/effect/launched_effect/`

---

## MainActivity.kt 템플릿

```kotlin
package com.example.{module_name}

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.{module_name}.ui.theme.{TechnologyName}Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            {TechnologyName}Theme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Problem", "Solution", "Practice")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("{TechnologyName} 학습") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> ProblemScreen()
                1 -> SolutionScreen()
                2 -> PracticeNavigator()
            }
        }
    }
}

@Composable
fun PracticeNavigator() {
    var selectedPractice by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedPractice == 0,
                onClick = { selectedPractice = 0 },
                label = { Text("{Practice1Label}") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("{Practice2Label}") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("{Practice3Label}") }
            )
        }

        when (selectedPractice) {
            0 -> Practice1_Screen()
            1 -> Practice2_Screen()
            2 -> Practice3_Screen()
        }
    }
}
```

---

## Problem.kt 템플릿

```kotlin
package com.example.{module_name}

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 문제 상황 화면
 *
 * {기술}을 사용하지 않았을 때 발생하는 문제를 시연합니다.
 */
@Composable
fun ProblemScreen() {
    var recompositionCount by remember { mutableIntStateOf(0) }

    // Recomposition 추적
    SideEffect {
        recompositionCount++
    }

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
                    text = "⚠️ 문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "{문제 상황 설명}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제 코드 시연 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recomposition 횟수: ${recompositionCount}회",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (recompositionCount > 5) Color.Red else Color.Unspecified
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 문제가 발생하는 인터랙티브 데모
                ProblemDemo()
            }
        }

        // 문제점 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🔍 발생하는 문제",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. {문제점 1}")
                Text("2. {문제점 2}")
                Text("3. {문제점 3}")
            }
        }

        // 잘못된 코드 예시 (주석 처리)
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "❌ 잘못된 코드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                    // 이렇게 하면 문제가 발생합니다!
                    // {잘못된 코드 예시}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ProblemDemo() {
    var count by remember { mutableIntStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { count++ }) {
            Text("카운트: $count")
        }

        // 문제가 발생하는 코드 (주석 처리하여 안전하게)
        // 실제 문제 코드는 주석으로 설명만
        /*
        // ❌ 위험한 코드 - 주석 해제하지 마세요!
        // {위험한 코드 예시}
        */

        Text(
            text = "버튼을 클릭해보세요",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
```

---

## Solution.kt 템플릿

```kotlin
package com.example.{module_name}

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 해결책 화면
 *
 * {기술}을 사용하여 문제를 해결하는 방법을 시연합니다.
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
        // 해결책 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "✅ 해결책: {기술명}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "{해결책 설명}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 해결책 데모 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SolutionDemo()
            }
        }

        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. {핵심 포인트 1}")
                Text("2. {핵심 포인트 2}")
                Text("3. {핵심 포인트 3}")
            }
        }

        // 올바른 코드 예시
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "✅ 올바른 코드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                    // {올바른 코드 예시}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SolutionDemo() {
    var count by remember { mutableIntStateOf(0) }
    var result by remember { mutableStateOf("") }

    // ✅ 올바른 사용법
    // {기술} 사용 예시
    /*
    LaunchedEffect(count) {
        // 비동기 작업
        result = fetchData(count)
    }
    */

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { count++ }) {
            Text("카운트: $count")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "결과: $result",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "정상적으로 동작합니다!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
```

---

## Practice.kt 템플릿

```kotlin
package com.example.{module_name}

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: {제목} (쉬움)
 *
 * {연습 목표 설명}
 */
@Composable
fun Practice1_Screen() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📝 연습 1: {제목}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{연습 문제 설명}")
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• {힌트 1}")
                Text("• {힌트 2}")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 여기에 {기술}을 사용하여 구현하세요

    /* 정답:
    var data by remember { mutableStateOf("") }

    {기술}({key}) {
        // 구현
    }
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 2: {제목} (중간)
 *
 * {연습 목표 설명}
 */
@Composable
fun Practice2_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📝 연습 2: {제목}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{연습 문제 설명}")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• {힌트}")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice2_Exercise()
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 여기에 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 3: {제목} (어려움)
 *
 * {연습 목표 설명}
 */
@Composable
fun Practice3_Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📝 연습 3: {제목}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{연습 문제 설명}")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• {최소한의 힌트}")
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Practice3_Exercise()
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 실제 앱 시나리오를 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}
```

---

## 플레이스홀더

| 플레이스홀더 | 설명 | 예시 |
|-------------|------|------|
| `{module_name}` | 모듈명 (snake_case) | `launched_effect` |
| `{TechnologyName}` | 기술명 (PascalCase) | `LaunchedEffect` |
| `{기술}` / `{기술명}` | 기술명 (한글 문서용) | `LaunchedEffect` |
| `{문제 상황 설명}` | 이 기술 없이 발생하는 문제 | API 호출이 무한 반복됩니다 |
| `{문제점 1~3}` | 구체적인 문제점들 | 메모리 누수, 무한 루프 등 |
| `{해결책 설명}` | 기술이 문제를 해결하는 방법 | key가 변경될 때만 실행됩니다 |
| `{핵심 포인트 1~3}` | 기억해야 할 핵심 사항 | key 선택이 중요합니다 |
| `{Practice1Label}` | 연습 1 라벨 | 검색 |
| `{Practice2Label}` | 연습 2 라벨 | 타이머 |
| `{Practice3Label}` | 연습 3 라벨 | 페이징 |
