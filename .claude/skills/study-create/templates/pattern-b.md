# 패턴 B: 구성요소 탐구 (Component Exploration) 템플릿

이 템플릿은 **"Compose의 필수 구성 요소"**에 적용합니다.

## 적용 대상

- Modifier
- Text, Button, Image (기본 Composable)
- Slot API
- CompositionLocal
- Theme / Material Design

---

## 파일 구조

```
study/{category}/{module_name}/
├── README.md                    # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt
│   ├── BasicUsage.kt      # 패턴 B 전용
│   ├── AdvancedUsage.kt   # 패턴 B 전용
│   ├── Practice.kt
│   ├── README.md                # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/component/action/button/`

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
    val tabs = listOf("기본", "고급", "Practice")

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
                0 -> BasicUsageScreen()
                1 -> AdvancedUsageScreen()
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

## BasicUsage.kt 템플릿

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
 * 기본 사용법 화면
 *
 * {구성요소}의 기본적인 사용 방법을 학습합니다.
 */
@Composable
fun BasicUsageScreen() {
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
                    text = "📖 {구성요소}란?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "{구성요소에 대한 간단한 설명}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 기능 1
        FeatureCard(
            title = "기능 1: {기능명}",
            description = "{기능 설명}"
        ) {
            Feature1Demo()
        }

        // 기능 2
        FeatureCard(
            title = "기능 2: {기능명}",
            description = "{기능 설명}"
        ) {
            Feature2Demo()
        }

        // 기능 3
        FeatureCard(
            title = "기능 3: {기능명}",
            description = "{기능 설명}"
        ) {
            Feature3Demo()
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    description: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            demo()
        }
    }
}

@Composable
private fun Feature1Demo() {
    // 기능 1 데모
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // {기능 1 예제 코드}
        Text("기능 1 데모")
    }
}

@Composable
private fun Feature2Demo() {
    // 기능 2 데모
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // {기능 2 예제 코드}
        Text("기능 2 데모")
    }
}

@Composable
private fun Feature3Demo() {
    // 기능 3 데모
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // {기능 3 예제 코드}
        Text("기능 3 데모")
    }
}
```

---

## AdvancedUsage.kt 템플릿

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
 * 고급 활용 화면
 *
 * {구성요소}의 고급 사용법과 조합 패턴을 학습합니다.
 */
@Composable
fun AdvancedUsageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 고급 활용 소개
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🚀 고급 활용",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "기본 기능을 조합하여 더 강력한 패턴을 만들어봅시다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 조합 패턴 1
        PatternCard(
            title = "패턴 1: {패턴명}",
            description = "{이 패턴을 언제 사용하는지}",
            useCase = "{사용 사례}"
        ) {
            Pattern1Demo()
        }

        // 조합 패턴 2
        PatternCard(
            title = "패턴 2: {패턴명}",
            description = "{이 패턴을 언제 사용하는지}",
            useCase = "{사용 사례}"
        ) {
            Pattern2Demo()
        }

        // 베스트 프랙티스 vs 안티패턴
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "✅ 베스트 프랙티스",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• {베스트 프랙티스 1}")
                Text("• {베스트 프랙티스 2}")
                Text("• {베스트 프랙티스 3}")

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "❌ 안티패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• {안티패턴 1}")
                Text("• {안티패턴 2}")
            }
        }

        // 안티패턴 데모
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "❌ 이렇게 하지 마세요",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                    // 잘못된 예시
                    // {안티패턴 코드}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "✅ 대신 이렇게 하세요",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                    // 올바른 예시
                    // {올바른 코드}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun PatternCard(
    title: String,
    description: String,
    useCase: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "📌 사용 사례: $useCase",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            demo()
        }
    }
}

@Composable
private fun Pattern1Demo() {
    // 패턴 1 데모
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // {패턴 1 예제 코드}
        Text("패턴 1 데모")
    }
}

@Composable
private fun Pattern2Demo() {
    // 패턴 2 데모
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // {패턴 2 예제 코드}
        Text("패턴 2 데모")
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
 * 기본 기능을 사용해봅니다.
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
                    text = "📝 연습 1: {제목}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{기본 기능을 사용하는 연습 문제}")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• {구체적인 힌트 1}")
                Text("• {구체적인 힌트 2}")
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
    // TODO: {구성요소}의 기본 기능을 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 2: {제목} (중간)
 *
 * 여러 기능을 조합해봅니다.
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
                Text("{기능 조합 연습 문제}")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• {방향을 제시하는 힌트}")
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
    // TODO: 여러 기능을 조합하여 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 3: {제목} (어려움)
 *
 * 고급 패턴을 적용해봅니다.
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
                Text("{고급 패턴 적용 연습 문제}")
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
    // TODO: 고급 패턴을 적용하여 구현하세요

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
| `{module_name}` | 모듈명 (snake_case) | `custom_modifier` |
| `{TechnologyName}` | 기술명 (PascalCase) | `CustomModifier` |
| `{구성요소}` | 구성요소명 (한글) | `Modifier` |
| `{기능명}` | 핵심 기능 이름 | `padding`, `size` |
| `{기능 설명}` | 기능에 대한 설명 | 요소 주변에 여백 추가 |
| `{패턴명}` | 조합 패턴 이름 | `체이닝 패턴` |
| `{사용 사례}` | 실제 사용 예시 | 카드 레이아웃 구성 |
| `{베스트 프랙티스}` | 권장 사항 | 순서를 고려하여 체이닝 |
| `{안티패턴}` | 피해야 할 것 | 동일 Modifier 중복 적용 |
