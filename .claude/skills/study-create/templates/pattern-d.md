# 패턴 D: 아키텍처 가이드 (Architecture Guide) 템플릿

이 템플릿은 **"설계/아키텍처"** 주제에 적용합니다.

## 적용 대상

- ViewModel + Compose
- State hoisting
- Unidirectional Data Flow (UDF)
- MVVM with Compose
- Repository 패턴
- Clean Architecture

---

## 파일 구조

```
study/{category}/{module_name}/
├── README.md                      # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt
│   ├── Principles.kt       # 패턴 D 전용
│   ├── Implementation.kt   # 패턴 D 전용
│   ├── Practice.kt
│   ├── README.md                  # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/architecture/state_hoisting/`

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
    val tabs = listOf("원칙", "구현", "Practice")

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
                0 -> PrinciplesScreen()
                1 -> ImplementationScreen()
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
                label = { Text("기본") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("응용") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("실전") }
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

## Principles.kt 템플릿

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
 * 핵심 원칙 화면
 *
 * {아키텍처}의 핵심 원칙과 필요성을 학습합니다.
 */
@Composable
fun PrinciplesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 왜 필요한가?
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "❓ 왜 {아키텍처}가 필요한가?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "{아키텍처 없이 발생하는 문제들}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• {문제 1}")
                Text("• {문제 2}")
                Text("• {문제 3}")
            }
        }

        // 해결 목표
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🎯 {아키텍처}의 목표",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• {목표 1}")
                Text("• {목표 2}")
                Text("• {목표 3}")
            }
        }

        // 핵심 원칙들
        PrincipleCard(
            number = 1,
            title = "{원칙 1 이름}",
            description = "{원칙 1 설명}",
            codeExample = """
                // 원칙 1 예시 코드
            """.trimIndent()
        ) {
            Principle1Demo()
        }

        PrincipleCard(
            number = 2,
            title = "{원칙 2 이름}",
            description = "{원칙 2 설명}",
            codeExample = """
                // 원칙 2 예시 코드
            """.trimIndent()
        ) {
            Principle2Demo()
        }

        PrincipleCard(
            number = 3,
            title = "{원칙 3 이름}",
            description = "{원칙 3 설명}",
            codeExample = """
                // 원칙 3 예시 코드
            """.trimIndent()
        ) {
            Principle3Demo()
        }
    }
}

@Composable
private fun PrincipleCard(
    number: Int,
    title: String,
    description: String,
    codeExample: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "원칙 $number: $title",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = codeExample,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 데모
            demo()
        }
    }
}

@Composable
private fun Principle1Demo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 원칙 1 데모
        Text("원칙 1 데모")
    }
}

@Composable
private fun Principle2Demo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 원칙 2 데모
        Text("원칙 2 데모")
    }
}

@Composable
private fun Principle3Demo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // 원칙 3 데모
        Text("원칙 3 데모")
    }
}
```

---

## Implementation.kt 템플릿

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
 * 구현 화면
 *
 * {아키텍처}를 실제로 구현하는 방법과 안티패턴을 학습합니다.
 */
@Composable
fun ImplementationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 구현 단계
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🔨 구현 방법",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "단계별로 {아키텍처}를 구현해봅시다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Step 1
        StepCard(
            step = 1,
            title = "{단계 1 제목}",
            description = "{단계 1 설명}",
            code = """
                // Step 1 코드
            """.trimIndent()
        )

        // Step 2
        StepCard(
            step = 2,
            title = "{단계 2 제목}",
            description = "{단계 2 설명}",
            code = """
                // Step 2 코드
            """.trimIndent()
        )

        // Step 3
        StepCard(
            step = 3,
            title = "{단계 3 제목}",
            description = "{단계 3 설명}",
            code = """
                // Step 3 코드
            """.trimIndent()
        )

        // 실제 앱 예제
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📱 실제 앱 예제: {앱 기능명}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                RealWorldExample()
            }
        }

        // 안티패턴
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "❌ 안티패턴",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                AntiPatternCard(
                    title = "안티패턴 1: {이름}",
                    why = "{왜 나쁜지}",
                    wrongCode = """
                        // 잘못된 코드
                    """.trimIndent(),
                    correctCode = """
                        // 올바른 코드
                    """.trimIndent()
                )

                Spacer(modifier = Modifier.height(12.dp))

                AntiPatternCard(
                    title = "안티패턴 2: {이름}",
                    why = "{왜 나쁜지}",
                    wrongCode = """
                        // 잘못된 코드
                    """.trimIndent(),
                    correctCode = """
                        // 올바른 코드
                    """.trimIndent()
                )
            }
        }

        // 테스트 가이드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🧪 테스트 가이드",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{아키텍처를 테스트하는 방법}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        // 테스트 코드 예시
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun StepCard(
    step: Int,
    title: String,
    description: String,
    code: String
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "Step $step",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = code,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun RealWorldExample() {
    Column {
        // 실제 앱 예제 구현
        Text("실제 앱 예제 데모")
    }
}

@Composable
private fun AntiPatternCard(
    title: String,
    why: String,
    wrongCode: String,
    correctCode: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = why,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "❌ 잘못된 코드:",
            style = MaterialTheme.typography.labelMedium
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = wrongCode,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "✅ 올바른 코드:",
            style = MaterialTheme.typography.labelMedium
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = correctCode,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
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
 * 연습 문제 1: 기본 적용 (쉬움)
 *
 * 핵심 원칙 하나를 적용해봅니다.
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
                    text = "📝 연습 1: 기본 적용",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{원칙 1}을 적용하여 구현해보세요.")
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 힌트",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• {구체적인 힌트}")
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
    // TODO: {원칙 1}을 적용하여 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 2: 원칙 조합 (중간)
 *
 * 여러 원칙을 조합해봅니다.
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
                    text = "📝 연습 2: 원칙 조합",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{원칙 1}과 {원칙 2}를 함께 적용해보세요.")
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
    // TODO: 여러 원칙을 조합하여 구현하세요

    /* 정답:
    // 정답 코드
    */

    Text("TODO: 직접 구현해보세요!")
}

/**
 * 연습 문제 3: 실전 구현 (어려움)
 *
 * 전체 아키텍처를 직접 구현해봅니다.
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
                    text = "📝 연습 3: 실전 구현",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("{실제 앱 기능}을 {아키텍처} 패턴으로 구현해보세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "요구사항:",
                    style = MaterialTheme.typography.titleSmall
                )
                Text("• {요구사항 1}")
                Text("• {요구사항 2}")
                Text("• {요구사항 3}")
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
    // TODO: 전체 아키텍처를 구현하세요

    /* 정답:
    // 정답 코드 (전체 구현)
    */

    Text("TODO: 직접 구현해보세요!")
}
```

---

## 플레이스홀더

| 플레이스홀더 | 설명 | 예시 |
|-------------|------|------|
| `{module_name}` | 모듈명 (snake_case) | `state_hoisting` |
| `{TechnologyName}` | 기술명 (PascalCase) | `StateHoisting` |
| `{아키텍처}` | 아키텍처명 (한글) | State Hoisting |
| `{문제 1~3}` | 아키텍처 없이 발생하는 문제 | 상태 공유 어려움 |
| `{목표 1~3}` | 아키텍처가 달성하는 목표 | 재사용성 향상 |
| `{원칙 이름}` | 핵심 원칙 이름 | 상태 끌어올리기 |
| `{원칙 설명}` | 원칙에 대한 설명 | 상태를 상위로 이동 |
| `{단계 제목}` | 구현 단계 제목 | ViewModel 생성 |
| `{앱 기능명}` | 실제 앱 예제 기능 | 카운터 앱 |
| `{안티패턴 이름}` | 피해야 할 패턴 | 상태 직접 수정 |
