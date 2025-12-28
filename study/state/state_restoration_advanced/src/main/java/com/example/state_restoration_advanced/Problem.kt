package com.example.state_restoration_advanced

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * # Problem: 복잡한 폼 데이터가 구성 변경/프로세스 종료 시 손실됨
 *
 * ## 문제 시나리오
 * 사용자가 프로필 정보를 입력하는 도중:
 * 1. 화면을 회전하면 모든 입력 데이터가 사라짐
 * 2. 앱이 백그라운드에서 종료되면 입력 데이터가 손실됨
 * 3. 복잡한 객체(커스텀 데이터 클래스)는 rememberSaveable로도 저장 불가
 *
 * ## 원인
 * - remember는 Recomposition에서만 상태 유지 (Configuration Change 시 손실)
 * - rememberSaveable은 Bundle 직렬화 가능한 타입만 지원
 * - 커스텀 객체는 Saver 없이는 저장 불가
 */

// 저장하려는 커스텀 데이터 클래스 (Bundle 직렬화 불가)
data class UserProfileData(
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val hobbies: List<String> = emptyList(),
    val bio: String = ""
)

@Composable
fun ProblemScreen() {
    // 문제 1: remember만 사용 - 화면 회전 시 상태 손실
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var hobbies by remember { mutableStateOf(listOf<String>()) }
    var newHobby by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 경고 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "문제: remember만 사용한 폼",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = "이 화면은 remember만 사용합니다.\n" +
                           "화면을 회전하면 모든 입력 데이터가 사라집니다!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 테스트 방법 안내
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "테스트 방법",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1. 아래 폼에 데이터를 입력하세요\n" +
                           "2. 디바이스를 회전하세요 (Ctrl+Left/Right in Emulator)\n" +
                           "3. 모든 데이터가 사라진 것을 확인하세요",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider()

        Text(
            text = "프로필 편집",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // 기본 입력 필드들
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
            value = age,
            onValueChange = { age = it },
            label = { Text("나이") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("자기소개") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        // 취미 목록 (List<String> - 복잡한 상태)
        Text(
            text = "취미 목록",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newHobby,
                onValueChange = { newHobby = it },
                label = { Text("새 취미") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Button(
                onClick = {
                    if (newHobby.isNotBlank()) {
                        hobbies = hobbies + newHobby
                        newHobby = ""
                    }
                }
            ) {
                Text("추가")
            }
        }

        // 취미 표시
        if (hobbies.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    hobbies.forEachIndexed { index, hobby ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${index + 1}. $hobby")
                            TextButton(
                                onClick = {
                                    hobbies = hobbies.filterIndexed { i, _ -> i != index }
                                }
                            ) {
                                Text("삭제", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        } else {
            Text(
                text = "등록된 취미가 없습니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider()

        // 현재 상태 요약
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "현재 입력된 데이터",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("이름: ${name.ifBlank { "(미입력)" }}")
                Text("이메일: ${email.ifBlank { "(미입력)" }}")
                Text("나이: ${age.ifBlank { "(미입력)" }}")
                Text("자기소개: ${bio.ifBlank { "(미입력)" }}")
                Text("취미: ${if (hobbies.isEmpty()) "(없음)" else hobbies.joinToString(", ")}")
            }
        }

        // 문제 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "문제가 되는 코드",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = """
// remember는 Configuration Change에서 상태 손실
var name by remember { mutableStateOf("") }
var hobbies by remember { mutableStateOf(listOf<String>()) }

// rememberSaveable 사용해도 커스텀 객체는 저장 불가
// (Saver 없이는 컴파일 에러 또는 런타임 에러)
// var profile by rememberSaveable {
//     mutableStateOf(UserProfileData())
// }
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
