package com.example.compose_introduction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Problem: 명령형 UI의 문제점
 *
 * 이 화면은 기존 View 시스템(명령형 UI)에서 발생하는 문제점을 보여줍니다.
 * Compose 모듈이지만, 명령형 사고방식의 문제를 시뮬레이션하여 설명합니다.
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
        // 문제 설명
        ProblemExplanationCard()

        // 명령형 UI 코드 예시
        ImperativeCodeExampleCard()

        // 문제점 시뮬레이션
        ProblemSimulationCard()

        // 핵심 문제 요약
        KeyProblemsCard()
    }
}

@Composable
private fun ProblemExplanationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "명령형 UI의 문제점",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = """
기존 Android View 시스템은 '명령형(Imperative)' 방식입니다.

명령형 UI에서는:
1. findViewById()로 뷰를 찾아야 합니다
2. setText(), setVisibility() 등으로 직접 업데이트해야 합니다
3. 상태가 변경될 때마다 모든 관련 UI를 수동으로 갱신해야 합니다

이 방식은 코드가 복잡해지고, 버그가 발생하기 쉽습니다.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ImperativeCodeExampleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "명령형 UI 코드 예시 (View 시스템)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // 코드 블록
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = """
// Activity에서의 명령형 UI 코드
class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 뷰 찾기
        val textViewCount = findViewById<TextView>(R.id.text_count)
        val textViewDouble = findViewById<TextView>(R.id.text_double)
        val buttonIncrement = findViewById<Button>(R.id.button_increment)

        // 2. 초기값 설정
        textViewCount.text = "Count: ${'$'}count"
        textViewDouble.text = "Double: ${'$'}{count * 2}"

        // 3. 버튼 클릭 시 수동 업데이트
        buttonIncrement.setOnClickListener {
            count++
            textViewCount.text = "Count: ${'$'}count"     // 수동!
            textViewDouble.text = "Double: ${'$'}{count * 2}"  // 수동!
            // 만약 여기서 하나를 빠뜨리면? UI 불일치!
        }
    }
}
                    """.trimIndent(),
                    modifier = Modifier.padding(12.dp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ProblemSimulationCard() {
    // 문제 시뮬레이션: 수동 동기화가 필요한 상황
    var count by remember { mutableIntStateOf(0) }
    var isOutOfSync by remember { mutableStateOf(false) }
    var manualDouble by remember { mutableIntStateOf(0) } // 의도적으로 분리

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "문제 시뮬레이션: 동기화 실패",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "아래는 명령형 방식에서 발생할 수 있는 '동기화 실패' 문제를 시뮬레이션합니다.",
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider()

            // 현재 상태 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Count",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Double (수동 업데이트)",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "$manualDouble",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isOutOfSync) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "올바른 값",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${count * 2}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (isOutOfSync) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "UI 불일치 발생! Double 값이 올바르지 않습니다.",
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 올바른 업데이트 (둘 다)
                Button(
                    onClick = {
                        count++
                        manualDouble = count * 2 // 올바르게 동기화
                        isOutOfSync = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("올바른 업데이트")
                }

                // 잘못된 업데이트 (하나만)
                Button(
                    onClick = {
                        count++
                        // manualDouble 업데이트를 깜빡함!
                        isOutOfSync = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("업데이트 누락")
                }
            }

            Button(
                onClick = {
                    count = 0
                    manualDouble = 0
                    isOutOfSync = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("초기화")
            }
        }
    }
}

@Composable
private fun KeyProblemsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "명령형 UI의 핵심 문제",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            ProblemItem(
                number = 1,
                title = "수동 동기화 필요",
                description = "상태가 변경될 때마다 관련된 모든 UI를 직접 업데이트해야 합니다."
            )

            ProblemItem(
                number = 2,
                title = "업데이트 누락 가능",
                description = "여러 곳에서 같은 데이터를 표시할 때 하나를 빠뜨리기 쉽습니다."
            )

            ProblemItem(
                number = 3,
                title = "코드량 증가",
                description = "findViewById, setText 등 보일러플레이트 코드가 많아집니다."
            )

            ProblemItem(
                number = 4,
                title = "복잡성 증가",
                description = "UI가 복잡해질수록 상태와 뷰의 관계를 추적하기 어려워집니다."
            )

            HorizontalDivider()

            Text(
                text = "Solution 탭에서 Compose가 이 문제들을 어떻게 해결하는지 확인하세요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProblemItem(
    number: Int,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.error
        ) {
            Text(
                text = "$number",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onError,
                fontWeight = FontWeight.Bold
            )
        }

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
