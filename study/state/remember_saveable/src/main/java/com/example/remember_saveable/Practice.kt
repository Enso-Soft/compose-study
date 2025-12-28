package com.example.remember_saveable

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize

/**
 * Practice: rememberSaveable 연습 문제
 *
 * 각 연습 문제에서 remember를 rememberSaveable로 변경하거나,
 * 커스텀 객체를 저장 가능하게 만드는 연습을 합니다.
 */

@Composable
fun PracticeScreen() {
    var selectedPractice by rememberSaveable { mutableIntStateOf(0) }

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
                label = { Text("폼") }
            )
            FilterChip(
                selected = selectedPractice == 2,
                onClick = { selectedPractice = 2 },
                label = { Text("Parcelize") }
            )
            FilterChip(
                selected = selectedPractice == 3,
                onClick = { selectedPractice = 3 },
                label = { Text("Saver") }
            )
        }

        when (selectedPractice) {
            0 -> Practice1_BasicSaveable()
            1 -> Practice2_FormData()
            2 -> Practice3_Parcelize()
            3 -> Practice4_CustomSaver()
        }
    }
}

/**
 * 연습 문제 1: 기본 rememberSaveable 적용
 *
 * 목표: remember를 rememberSaveable로 변경하여 화면 회전에도 상태 유지
 *
 * TODO: 아래 코드에서 remember를 rememberSaveable로 변경하세요.
 */
@Composable
fun Practice1_BasicSaveable() {
    // TODO: remember를 rememberSaveable로 변경
    // 힌트: var count by rememberSaveable { mutableStateOf(0) }

    // === 정답 ===
    var count by rememberSaveable { mutableIntStateOf(0) }
    var isLiked by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 기본 rememberSaveable",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("remember를 rememberSaveable로 변경하여 화면 회전 시에도 상태가 유지되도록 하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "힌트: remember { } -> rememberSaveable { }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 카운터
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "카운터: $count",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = { count-- }) { Text("-1") }
                    Button(onClick = { count++ }) { Text("+1") }
                    OutlinedButton(onClick = { count = 0 }) { Text("초기화") }
                }
            }
        }

        // 좋아요 버튼
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isLiked) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { isLiked = !isLiked },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "좋아요",
                        modifier = Modifier.size(48.dp),
                        tint = if (isLiked) Color.Red else Color.Gray
                    )
                }
                Text(
                    text = if (isLiked) "좋아요!" else "좋아요를 눌러주세요"
                )
            }
        }

        // 상태 확인
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 상태 (화면 회전 후 확인)",
                    fontWeight = FontWeight.Bold
                )
                Text("count = $count")
                Text("isLiked = $isLiked")
            }
        }
    }
}

/**
 * 연습 문제 2: 폼 데이터 유지
 *
 * 목표: 여러 필드가 있는 폼에서 모든 입력을 화면 회전에도 유지
 *
 * TODO: 모든 remember를 rememberSaveable로 변경하세요.
 */
@Composable
fun Practice2_FormData() {
    // TODO: 모든 remember를 rememberSaveable로 변경
    // 힌트: var name by rememberSaveable { mutableStateOf("") }

    // === 정답 ===
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var agreeTerms by rememberSaveable { mutableStateOf(false) }
    var agreeMarketing by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 폼 데이터 유지",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("회원가입 폼의 모든 필드가 화면 회전 후에도 유지되도록 하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "팁: 모든 입력 필드와 체크박스 상태를 rememberSaveable로!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 폼
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "회원가입",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("이름") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("이메일") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("전화번호") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                HorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = agreeTerms,
                        onCheckedChange = { agreeTerms = it }
                    )
                    Text("이용약관에 동의합니다 (필수)")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = agreeMarketing,
                        onCheckedChange = { agreeMarketing = it }
                    )
                    Text("마케팅 정보 수신에 동의합니다 (선택)")
                }

                Button(
                    onClick = { /* 가입 처리 */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotEmpty() && email.isNotEmpty() && agreeTerms
                ) {
                    Text("가입하기")
                }
            }
        }

        // 입력 상태 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "현재 입력 상태",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("이름: $name")
                Text("이메일: $email")
                Text("전화번호: $phone")
                Text("이용약관 동의: $agreeTerms")
                Text("마케팅 동의: $agreeMarketing")
            }
        }
    }
}

/**
 * 연습용 data class
 */
@Parcelize
data class TodoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
) : Parcelable

/**
 * 연습 문제 3: @Parcelize 사용
 *
 * 목표: 커스텀 data class에 @Parcelize를 적용하여 저장
 *
 * TODO: TodoItem data class가 이미 @Parcelize로 정의되어 있습니다.
 *       이를 rememberSaveable로 저장하세요.
 */
@Composable
fun Practice3_Parcelize() {
    // @Parcelize가 적용된 객체 저장
    // TODO: remember를 rememberSaveable로 변경
    // 힌트: var currentTodo by rememberSaveable { mutableStateOf(TodoItem(...)) }

    // === 정답 ===
    var currentTodo by rememberSaveable {
        mutableStateOf(TodoItem(1, "", false))
    }
    var todoList by rememberSaveable {
        mutableStateOf(listOf<TodoItem>())
    }
    var nextId by rememberSaveable { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: @Parcelize 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("@Parcelize가 적용된 TodoItem을 rememberSaveable로 저장하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "@Parcelize\ndata class TodoItem(...) : Parcelable",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 할 일 입력
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "할 일 추가",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = currentTodo.title,
                    onValueChange = {
                        currentTodo = currentTodo.copy(title = it)
                    },
                    label = { Text("할 일") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = {
                        if (currentTodo.title.isNotEmpty()) {
                            todoList = todoList + currentTodo.copy(id = nextId)
                            nextId++
                            currentTodo = TodoItem(nextId, "", false)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("추가")
                }
            }
        }

        // 할 일 목록
        if (todoList.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "할 일 목록 (${todoList.size}개)",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    todoList.forEach { todo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = todo.isCompleted,
                                onCheckedChange = { checked ->
                                    todoList = todoList.map {
                                        if (it.id == todo.id) it.copy(isCompleted = checked) else it
                                    }
                                }
                            )
                            Text(
                                text = todo.title,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = {
                                todoList = todoList.filter { it.id != todo.id }
                            }) {
                                Text("삭제")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "화면을 회전해도 목록이 유지됩니다!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}

/**
 * 연습용 data class (Parcelable 아님)
 */
data class Note(
    val id: Int,
    val title: String,
    val content: String
)

// TODO: NoteSaver 정의
// 힌트: listSaver 사용
val NoteSaver = listSaver<Note, Any>(
    save = { listOf(it.id, it.title, it.content) },
    restore = { Note(it[0] as Int, it[1] as String, it[2] as String) }
)

/**
 * 연습 문제 4: Custom Saver 사용
 *
 * 목표: listSaver를 사용하여 Parcelable이 아닌 객체 저장
 *
 * TODO: Note data class를 저장할 수 있는 NoteSaver를 정의하고 사용하세요.
 */
@Composable
fun Practice4_CustomSaver() {
    // TODO: NoteSaver를 사용하여 Note 저장
    // 힌트: var note by rememberSaveable(stateSaver = NoteSaver) { ... }

    // === 정답 ===
    var note by rememberSaveable(stateSaver = NoteSaver) {
        mutableStateOf(Note(1, "", ""))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 4: Custom Saver 사용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Parcelable이 아닌 Note 객체를 listSaver로 저장하세요.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "listSaver<Note, Any>(\n" +
                            "    save = { listOf(it.id, it.title, it.content) },\n" +
                            "    restore = { Note(it[0] as Int, ...) }\n" +
                            ")",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                )
            }
        }

        // 노트 입력
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "메모장",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = note.title,
                    onValueChange = { note = note.copy(title = it) },
                    label = { Text("제목") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = note.content,
                    onValueChange = { note = note.copy(content = it) },
                    label = { Text("내용") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5,
                    maxLines = 10
                )
            }
        }

        // 저장된 데이터 표시
        if (note.title.isNotEmpty() || note.content.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "저장된 Note 객체",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("id: ${note.id}")
                    Text("title: ${note.title}")
                    Text("content: ${note.content}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "listSaver로 직렬화되어 화면 회전에도 유지됩니다!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }

        // Saver 코드 예시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "완성된 Saver 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "val NoteSaver = listSaver<Note, Any>(\n" +
                            "    save = { listOf(it.id, it.title, it.content) },\n" +
                            "    restore = { Note(\n" +
                            "        it[0] as Int,\n" +
                            "        it[1] as String,\n" +
                            "        it[2] as String\n" +
                            "    ) }\n" +
                            ")\n\n" +
                            "var note by rememberSaveable(\n" +
                            "    stateSaver = NoteSaver\n" +
                            ") {\n" +
                            "    mutableStateOf(Note(1, \"\", \"\"))\n" +
                            "}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
