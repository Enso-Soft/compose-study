package com.example.stability

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: @Immutable 적용하기
 *
 * 요구사항:
 * - Product data class에 @Immutable 추가
 * - 카운터 변경 시 ProductList가 recompose되지 않도록 최적화
 *
 * TODO: @Immutable 어노테이션을 추가하세요!
 */

// TODO: @Immutable 어노테이션을 추가하세요
// @Immutable
data class Product(
    val id: Int,
    val name: String,
    val price: Int
)

@Composable
fun Practice1_ImmutableScreen() {
    var counter by remember { mutableIntStateOf(0) }
    var recomposeCount by remember { mutableIntStateOf(0) }

    val products = remember {
        listOf(
            Product(1, "노트북", 1500000),
            Product(2, "마우스", 50000),
            Product(3, "키보드", 100000)
        )
    }

    SideEffect {
        recomposeCount++
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 연습 문제 설명
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: @Immutable 적용",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TODO: Product class에 @Immutable 추가\n카운터 변경 시 리스트 recompose 방지",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 상태 표시
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Parent Recompose: $recomposeCount")
            Text("Counter: $counter")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { counter++ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("카운터 증가")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Practice1ProductList(products = products)

        // 힌트
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("정답 코드:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
@Immutable
data class Product(
    val id: Int,
    val name: String,
    val price: Int
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Practice1ProductList(products: List<Product>) {
    var listRecomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        listRecomposeCount++
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "ProductList Recompose: $listRecomposeCount",
            style = MaterialTheme.typography.bodySmall,
            color = if (listRecomposeCount > 1)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary
        )
    }

    LazyColumn(
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products, key = { it.id }) { product ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(product.name)
                    Text("${"%,d".format(product.price)}원")
                }
            }
        }
    }
}

/**
 * 연습 문제 2: Wrapper 클래스 만들기
 *
 * 요구사항:
 * - List<Message>를 감싸는 MessageListWrapper 생성
 * - Wrapper에 @Immutable 적용
 * - 타이머가 돌아도 메시지 리스트는 recompose되지 않도록
 *
 * TODO: MessageListWrapper 클래스를 만들고 @Immutable 적용하세요!
 */

data class Message(
    val id: Int,
    val sender: String,
    val content: String
)

// TODO: MessageListWrapper 클래스를 만들고 @Immutable을 적용하세요
/*
@Immutable
data class MessageListWrapper(
    val messages: List<Message>
)
*/

@Composable
fun Practice2_WrapperScreen() {
    var seconds by remember { mutableIntStateOf(0) }
    var recomposeCount by remember { mutableIntStateOf(0) }

    // 1초마다 카운터 증가 (타이머 시뮬레이션)
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            seconds++
        }
    }

    val messages = remember {
        listOf(
            Message(1, "Alice", "안녕하세요!"),
            Message(2, "Bob", "반갑습니다."),
            Message(3, "Charlie", "오늘 날씨가 좋네요.")
        )
    }

    SideEffect {
        recomposeCount++
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 연습 문제 설명
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: Wrapper 클래스",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TODO: MessageListWrapper 생성\n@Immutable 적용하여 리스트 최적화",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 상태 표시
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("경과 시간: ${seconds}초")
                    Text("Parent Recompose: $recomposeCount")
                }
                Text(
                    text = "타이머 동작 중...",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 현재: List<Message>를 직접 전달 (Unstable)
        Practice2MessageList(messages = messages)

        // TODO: Wrapper를 사용하도록 변경
        // Practice2MessageListOptimized(wrapper = MessageListWrapper(messages))

        // 힌트
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("정답 코드:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
@Immutable
data class MessageListWrapper(
    val messages: List<Message>
)

// 사용:
Practice2MessageListOptimized(
    wrapper = remember {
        MessageListWrapper(messages)
    }
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Practice2MessageList(messages: List<Message>) {
    var listRecomposeCount by remember { mutableIntStateOf(0) }

    SideEffect {
        listRecomposeCount++
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "MessageList Recompose: $listRecomposeCount",
            style = MaterialTheme.typography.bodySmall,
            color = if (listRecomposeCount > 3)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary
        )
    }

    LazyColumn(
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = message.sender,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 3: @Stable 상태 클래스
 *
 * 요구사항:
 * - FormState 클래스에 @Stable 적용
 * - 내부 프로퍼티는 MutableState 사용
 * - 입력 변경 시 Compose가 변경을 감지하도록
 *
 * TODO: @Stable과 mutableStateOf를 올바르게 적용하세요!
 */

// TODO: @Stable을 추가하고 프로퍼티를 mutableStateOf로 변경하세요
// @Stable
class FormState {
    // TODO: mutableStateOf 사용
    var username: String = ""
    var email: String = ""
    var password: String = ""

    /*
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    */

    val isValid: Boolean
        get() = username.isNotBlank() &&
                email.contains("@") &&
                password.length >= 8
}

@Composable
fun Practice3_StableClassScreen() {
    // TODO: remember로 FormState 인스턴스 유지
    val formState = remember { FormState() }
    var recomposeCount by remember { mutableIntStateOf(0) }

    // 현재 상태를 강제로 관찰하기 위한 트릭
    var forceUpdate by remember { mutableIntStateOf(0) }

    SideEffect {
        recomposeCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 연습 문제 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: @Stable 상태 클래스",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "TODO: FormState에 @Stable 추가\n프로퍼티에 mutableStateOf 사용",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 입력 필드들
        OutlinedTextField(
            value = formState.username,
            onValueChange = {
                formState.username = it
                forceUpdate++  // 현재는 수동으로 업데이트 트리거
            },
            label = { Text("사용자명") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.email,
            onValueChange = {
                formState.email = it
                forceUpdate++
            },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.password,
            onValueChange = {
                formState.password = it
                forceUpdate++
            },
            label = { Text("비밀번호 (8자 이상)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 상태 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recomposition: $recomposeCount")
                Text("폼 유효: ${formState.isValid}")
                Text(
                    text = "현재 상태: forceUpdate로 수동 트리거 중",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* 제출 */ },
            enabled = formState.isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (formState.isValid) "가입하기" else "입력을 완료하세요")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("정답 코드:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = """
@Stable
class FormState {
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    val isValid: Boolean
        get() = username.isNotBlank() &&
                email.contains("@") &&
                password.length >= 8
}

// 사용 시 forceUpdate 트릭 불필요!
// mutableStateOf가 자동으로 변경 감지
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun PracticeScreen() {
    PracticeNavigator()
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
                label = { Text("@Immutable") }
            )
            FilterChip(
                selected = selectedPractice == 1,
                onClick = { selectedPractice = 1 },
                label = { Text("Wrapper") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("@Stable") }
            )
        }

        when (selectedPractice) {
            0 -> Practice1_ImmutableScreen()
            1 -> Practice2_WrapperScreen()
            2 -> Practice3_StableClassScreen()
        }
    }
}
