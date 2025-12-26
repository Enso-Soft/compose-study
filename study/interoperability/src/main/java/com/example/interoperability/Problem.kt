package com.example.interoperability

import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * 문제가 있는 코드 - AndroidView 없이 View를 Compose에서 사용하려는 시도
 *
 * 이 코드의 문제점:
 * 1. View를 remember로 외부에서 생성 → View 생명주기 미관리
 * 2. View → Compose 상태 동기화 불가
 * 3. Compose → View 상태 동기화 불가
 * 4. 양방향 데이터 바인딩 구현 불가
 */
@Composable
fun ProblemScreen() {
    val context = LocalContext.current
    var recompositionCount by remember { mutableIntStateOf(0) }
    var composeText by remember { mutableStateOf("Compose 상태") }

    // 문제: View를 remember로 외부에서 생성
    // 이렇게 하면 View 생명주기가 Compose에 의해 관리되지 않음
    val editText = remember {
        EditText(context).apply {
            hint = "여기에 입력하세요"
            setText("초기 텍스트")
        }
    }

    // Recomposition 횟수 추적
    SideEffect {
        recompositionCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Problem: View-Compose 상태 동기화 실패",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 정보 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Recomposition 횟수: $recompositionCount")
                Text("Compose 상태: $composeText")
                Text("EditText 값: ${editText.text}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 상황 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 이게 문제인가?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProblemItem("1. View 생명주기 미관리", "remember로 View를 보관하면 Compose가 View를 정리하지 않음")
                ProblemItem("2. 단방향 통신만 가능", "EditText 값이 변해도 Compose가 알 수 없음")
                ProblemItem("3. 화면에 View가 표시되지 않음", "AndroidView 없이는 View가 실제로 렌더링되지 않음")
                ProblemItem("4. 메모리 누수 위험", "View가 Composition 해제 후에도 메모리에 남음")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 잘못된 시도 버튼들
        Text(
            text = "아래 버튼을 눌러도 동기화가 안 됩니다:",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    // Compose 상태 변경 → EditText에 반영 안 됨!
                    composeText = "변경됨 ${System.currentTimeMillis() % 1000}"
                }
            ) {
                Text("Compose 변경")
            }

            Button(
                onClick = {
                    // EditText 직접 변경 → Compose가 모름!
                    editText.setText("직접 변경 ${System.currentTimeMillis() % 1000}")
                }
            ) {
                Text("EditText 변경")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 결과 확인
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 확인 방법",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 'Compose 변경' 버튼 → Compose 상태만 변경됨")
                Text("2. 'EditText 변경' 버튼 → EditText 값이 변해도 UI에 반영 안 됨")
                Text("3. EditText가 화면에 보이지 않음!")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "→ Solution 탭에서 AndroidView로 해결!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 잘못된 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "❌ 잘못된 코드",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// View를 remember로 외부에서 생성
val editText = remember {
    EditText(context).apply {
        hint = "입력하세요"
    }
}

// View가 화면에 표시되지 않음!
// View ↔ Compose 상태 동기화 불가!
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ProblemItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
