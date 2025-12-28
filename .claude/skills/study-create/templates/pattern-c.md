# 패턴 C: 비교-선택 (Compare-Choose) 템플릿

이 템플릿은 **"여러 옵션 중 선택"**이 필요한 주제에 적용합니다.

## 적용 대상

- Row vs Column vs Box (레이아웃 선택)
- LazyColumn vs LazyRow vs LazyGrid (리스트 선택)
- Navigation 방식들
- Animation 종류들 (animateXAsState vs Animatable vs Transition)

---

## 파일 구조

```
study/{category}/{module_name}/
├── README.md                      # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt
│   ├── OptionsComparison.kt   # 패턴 C 전용
│   ├── SelectionGuide.kt      # 패턴 C 전용
│   ├── Practice.kt
│   ├── README.md                  # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/list/lazy_layouts/`

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
    val tabs = listOf("비교", "선택 가이드", "Practice")

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
                0 -> OptionsComparisonScreen()
                1 -> SelectionGuideScreen()
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
                label = { Text("{Option1}") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("{Option2}") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("복합") }
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

## OptionsComparison.kt 템플릿

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
 * 옵션 비교 화면
 *
 * 각 옵션의 특징과 차이점을 비교합니다.
 */
@Composable
fun OptionsComparisonScreen() {
    var selectedOption by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 개요 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🔍 {주제}: 무엇을 선택해야 할까?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "{왜 선택이 중요한지 설명}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 옵션 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedOption == 0,
                onClick = { selectedOption = 0 },
                label = { Text("{Option1}") }
            )
            FilterChip(
                selected = selectedOption == 1,
                onClick = { selectedOption = 1 },
                label = { Text("{Option2}") }
            )
            FilterChip(
                selected = selectedOption == 2,
                onClick = { selectedOption = 2 },
                label = { Text("{Option3}") }
            )
        }

        // 선택된 옵션 상세
        when (selectedOption) {
            0 -> Option1Detail()
            1 -> Option2Detail()
            2 -> Option3Detail()
        }

        // 비교 표
        ComparisonTable()
    }
}

@Composable
private fun Option1Detail() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📦 {Option1}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "특징",
                style = MaterialTheme.typography.titleSmall
            )
            Text("• {특징 1}")
            Text("• {특징 2}")
            Text("• {특징 3}")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "적합한 상황",
                style = MaterialTheme.typography.titleSmall
            )
            Text("• {상황 1}")
            Text("• {상황 2}")

            Spacer(modifier = Modifier.height(12.dp))

            // 데모
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Option1Demo()
                }
            }
        }
    }
}

@Composable
private fun Option1Demo() {
    // {Option1} 데모
    Text("{Option1} 데모")
}

@Composable
private fun Option2Detail() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📦 {Option2}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "특징",
                style = MaterialTheme.typography.titleSmall
            )
            Text("• {특징 1}")
            Text("• {특징 2}")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "적합한 상황",
                style = MaterialTheme.typography.titleSmall
            )
            Text("• {상황 1}")
            Text("• {상황 2}")

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Option2Demo()
                }
            }
        }
    }
}

@Composable
private fun Option2Demo() {
    // {Option2} 데모
    Text("{Option2} 데모")
}

@Composable
private fun Option3Detail() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📦 {Option3}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "특징",
                style = MaterialTheme.typography.titleSmall
            )
            Text("• {특징 1}")
            Text("• {특징 2}")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "적합한 상황",
                style = MaterialTheme.typography.titleSmall
            )
            Text("• {상황 1}")

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Option3Demo()
                }
            }
        }
    }
}

@Composable
private fun Option3Demo() {
    // {Option3} 데모
    Text("{Option3} 데모")
}

@Composable
private fun ComparisonTable() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📊 비교 표",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 테이블 헤더
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "기준",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "{Option1}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "{Option2}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "{Option3}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // 테이블 행들
            ComparisonRow("{기준1}", "{값}", "{값}", "{값}")
            ComparisonRow("{기준2}", "{값}", "{값}", "{값}")
            ComparisonRow("{기준3}", "{값}", "{값}", "{값}")
        }
    }
}

@Composable
private fun ComparisonRow(
    criterion: String,
    value1: String,
    value2: String,
    value3: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = criterion,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value1,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value2,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value3,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
```

---

## SelectionGuide.kt 템플릿

```kotlin
package com.example.{module_name}

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 선택 가이드 화면
 *
 * 상황별로 어떤 옵션을 선택해야 하는지 안내합니다.
 */
@Composable
fun SelectionGuideScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 의사결정 가이드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🎯 선택 가이드",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "상황에 맞는 최적의 옵션을 선택하세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 상황별 추천
        ScenarioCard(
            scenario = "상황 1: {상황 설명}",
            recommendation = "{Option1}",
            reason = "{추천 이유}"
        )

        ScenarioCard(
            scenario = "상황 2: {상황 설명}",
            recommendation = "{Option2}",
            reason = "{추천 이유}"
        )

        ScenarioCard(
            scenario = "상황 3: {상황 설명}",
            recommendation = "{Option3}",
            reason = "{추천 이유}"
        )

        // 의사결정 플로우차트
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📋 의사결정 플로우",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                DecisionFlowChart()
            }
        }

        // 주의사항
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚠️ 주의사항",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• {주의사항 1}")
                Text("• {주의사항 2}")
                Text("• {주의사항 3}")
            }
        }
    }
}

@Composable
private fun ScenarioCard(
    scenario: String,
    recommendation: String,
    reason: String
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = scenario,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "→ ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = recommendation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = " 사용",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "이유: $reason",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DecisionFlowChart() {
    Column {
        Text("시작", style = MaterialTheme.typography.labelLarge)
        Text("  │")
        Text("  ├── {조건 1}? ──Yes──► {Option1}")
        Text("  │")
        Text("  ├── {조건 2}? ──Yes──► {Option2}")
        Text("  │")
        Text("  └── 그 외 ──────────► {Option3}")
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
 * 연습 문제 1: {Option1} 선택 상황 (쉬움)
 *
 * {Option1}이 적합한 상황을 구현합니다.
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
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📝 연습 1: {Option1} 활용",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{Option1}이 적합한 상황: {상황 설명}")
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
                Practice1_Exercise()
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: {Option1}을 사용하여 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 2: {Option2} 선택 상황 (중간)
 *
 * {Option2}가 적합한 상황을 구현합니다.
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
                    text = "📝 연습 2: {Option2} 활용",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{Option2}가 적합한 상황: {상황 설명}")
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
    // TODO: {Option2}를 사용하여 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 3: 복합 선택 상황 (어려움)
 *
 * 상황에 맞는 옵션을 선택하고 구현합니다.
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
                    text = "📝 연습 3: 복합 선택",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{복합 상황 설명 - 어떤 옵션을 선택할지도 결정해야 함}")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• 먼저 어떤 옵션이 적합한지 결정하세요")
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
    // TODO: 상황을 분석하고 적절한 옵션을 선택하여 구현하세요

    /* 정답:
    // 정답 코드 (선택 이유와 함께)
    */

    Text("TODO: 직접 구현해보세요!")
}
```

---

## 플레이스홀더

| 플레이스홀더 | 설명 | 예시 |
|-------------|------|------|
| `{module_name}` | 모듈명 (snake_case) | `lazy_layouts` |
| `{TechnologyName}` | 기술명 (PascalCase) | `LazyLayouts` |
| `{주제}` | 비교 주제 | 레이아웃 선택 |
| `{Option1/2/3}` | 비교 옵션들 | LazyColumn, LazyRow, LazyGrid |
| `{특징}` | 각 옵션의 특징 | 세로 스크롤, 가로 스크롤 |
| `{상황}` | 적합한 사용 상황 | 채팅 목록, 갤러리 |
| `{기준}` | 비교 기준 | 스크롤 방향, 성능 |
| `{조건}` | 의사결정 조건 | 세로 스크롤이 필요한가? |
