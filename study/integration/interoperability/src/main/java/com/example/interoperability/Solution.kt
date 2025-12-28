package com.example.interoperability

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.width

/**
 * 올바른 코드 - View-Compose 상호운용 해결책
 *
 * Part 1: AndroidView (Compose에서 View 사용)
 * - factory: View 생성 (한 번만 호출)
 * - update: Compose 상태 -> View 업데이트 (recomposition마다 호출)
 * - 양방향 통신: 리스너(View->Compose) + update(Compose->View)
 *
 * Part 2: ComposeView (View에서 Compose 사용)
 * - setViewCompositionStrategy: Composition 해제 시점 제어
 * - Fragment에서는 DisposeOnViewTreeLifecycleDestroyed 사용
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solution: AndroidView 활용",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "AndroidView 예제",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 예제 1: EditText 양방향 바인딩
        EditTextExample()

        Spacer(modifier = Modifier.height(24.dp))

        // 예제 2: SeekBar 양방향 바인딩
        SeekBarExample()

        Spacer(modifier = Modifier.height(24.dp))

        // Part 1 핵심 포인트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Part 1: AndroidView 핵심 포인트",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                KeyPoint("factory", "View 생성 (한 번만 호출), 이벤트 리스너 설정")
                KeyPoint("update", "Compose 상태 변경 시 View 업데이트")
                KeyPoint("무한 루프 방지", "update에서 조건부 업데이트 필수")
                KeyPoint("생명주기 관리", "Compose가 View 생명주기를 자동 관리")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        // Part 2: ComposeView
        Text(
            text = "Part 2: ComposeView",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ComposeView 예제 카드
        ComposeViewExampleCard()
    }
}

/**
 * ComposeView 사용 예제 (Fragment에서 Compose 사용)
 */
@Composable
private fun ComposeViewExampleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Fragment에서 ComposeView 올바른 사용법",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 올바른 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "올바른 코드:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
class MyFragment : Fragment() {
    override fun onCreateView(...): View {
        return ComposeView(context).apply {
            // Fragment에 적합한 전략 설정
            setViewCompositionStrategy(
                ViewCompositionStrategy
                    .DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                MaterialTheme {
                    MyComposeScreen()
                }
            }
        }
    }
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ViewCompositionStrategy 선택 가이드
            Text(
                text = "ViewCompositionStrategy 선택 가이드",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            StrategyItem(
                strategy = "DisposeOnViewTreeLifecycleDestroyed",
                useCase = "Fragment (권장)",
                isRecommended = true
            )

            StrategyItem(
                strategy = "DisposeOnDetachedFromWindowOrReleasedFromPool",
                useCase = "RecyclerView 아이템, 일반 View",
                isRecommended = false
            )

            StrategyItem(
                strategy = "DisposeOnLifecycleDestroyed",
                useCase = "커스텀 Lifecycle 관리",
                isRecommended = false
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Part 2 핵심 포인트
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Part 2: ComposeView 핵심 포인트",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            KeyPoint("setViewCompositionStrategy", "Fragment 사용 시 필수 설정")
            KeyPoint("DisposeOnViewTreeLifecycleDestroyed", "Fragment에서 권장하는 전략")
            KeyPoint("상태 보존", "백스택 복귀 시에도 Compose 상태 유지")
            KeyPoint("rememberSaveable", "프로세스 종료에도 대비 가능")
        }
    }
}

@Composable
private fun StrategyItem(
    strategy: String,
    useCase: String,
    isRecommended: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isRecommended) "[권장]" else "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(48.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = strategy,
                style = MaterialTheme.typography.bodySmall,
                color = if (isRecommended) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = useCase,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * EditText를 Compose에서 사용하는 예제
 * 양방향 데이터 바인딩 구현
 */
@Composable
private fun EditTextExample() {
    var text by remember { mutableStateOf("Hello AndroidView!") }
    var syncCount by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "예제 1: EditText 양방향 바인딩",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Compose 상태 표시
            Text("Compose 상태: $text")
            Text("동기화 횟수: $syncCount", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(12.dp))

            // AndroidView로 EditText 통합
            AndroidView(
                factory = { context ->
                    EditText(context).apply {
                        hint = "여기에 입력하세요"
                        setText(text)

                        // View → Compose: TextWatcher로 이벤트 전달
                        addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                            override fun afterTextChanged(s: Editable?) {
                                val newText = s?.toString() ?: ""
                                if (newText != text) {
                                    text = newText
                                    syncCount++
                                }
                            }
                        })
                    }
                },
                update = { editText ->
                    // Compose → View: 상태 변경 시 View 업데이트
                    // 무한 루프 방지: 값이 다를 때만 업데이트
                    if (editText.text.toString() != text) {
                        editText.setText(text)
                        editText.setSelection(text.length) // 커서를 끝으로
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Compose에서 텍스트 변경
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { text = "Compose에서 변경!" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Compose 변경", style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = { text = "" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("초기화", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

/**
 * SeekBar를 Compose에서 사용하는 예제
 * 진행률 표시와 함께 양방향 바인딩
 */
@Composable
private fun SeekBarExample() {
    var progress by remember { mutableIntStateOf(50) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "예제 2: SeekBar 양방향 바인딩",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 진행률 표시
            Text("현재 값: $progress / 100")

            // 진행률 바 (Compose로 시각화)
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // AndroidView로 SeekBar 통합
            AndroidView(
                factory = { context ->
                    SeekBar(context).apply {
                        max = 100
                        this.progress = progress

                        // View → Compose: 리스너로 이벤트 전달
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                                if (fromUser) {
                                    progress = value
                                }
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })
                    }
                },
                update = { seekBar ->
                    // Compose → View
                    if (seekBar.progress != progress) {
                        seekBar.progress = progress
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Compose에서 값 변경
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { progress = 0 },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("0%")
                }
                Button(
                    onClick = { progress = 50 },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("50%")
                }
                Button(
                    onClick = { progress = 100 },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("100%")
                }
            }
        }
    }
}

@Composable
private fun KeyPoint(title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "• $title: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}
