package com.example.screen_and_component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Problem: Screen과 Component를 구분하지 않았을 때 발생하는 문제
 *
 * 이 화면에서는 모든 코드를 하나의 Composable에 넣었을 때 발생하는 문제를 보여줍니다:
 * 1. Preview 불가능 (ViewModel 의존성)
 * 2. 테스트 어려움
 * 3. 재사용 불가능
 * 4. 코드 가독성 저하
 */

@Composable
fun ProblemScreen() {
    var selectedProblem by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 문제 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedProblem == 0,
                onClick = { selectedProblem = 0 },
                label = { Text("모노리식") }
            )
            FilterChip(
                selected = selectedProblem == 1,
                onClick = { selectedProblem = 1 },
                label = { Text("Preview 불가") }
            )
            FilterChip(
                selected = selectedProblem == 2,
                onClick = { selectedProblem = 2 },
                label = { Text("재사용 불가") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedProblem) {
            0 -> Problem1_Monolithic()
            1 -> Problem2_NoPreview()
            2 -> Problem3_NotReusable()
        }
    }
}

/**
 * 문제 1: 모든 것이 하나의 거대한 Composable에 뒤섞임
 *
 * 이 코드는 실제로 동작하지만 많은 문제가 있습니다:
 * - 상태 관리와 UI가 뒤섞임
 * - 어디서 무엇이 일어나는지 파악하기 어려움
 * - 부분적인 수정이 어려움
 */
@Composable
fun Problem1_Monolithic() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                    text = "문제 1: 모노리식 Composable",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "모든 코드가 하나의 거대한 함수에 뒤섞여 있습니다.\n" +
                            "상태 관리, UI 렌더링, 비즈니스 로직이 분리되지 않았습니다.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제가 있는 코드 시뮬레이션
        MonolithicProfileScreen()

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제가 있는 코드 구조",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun ProfileScreen() {
    // 상태 관리 (ViewModel 역할)
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(user?.name ?: "") }

    // UI 코드가 바로 여기에 섞여있음
    Column {
        // 프로필 이미지 - 재사용 불가
        Box(modifier = Modifier.size(100.dp)) {
            Image(...)
            IconButton(...) // 편집 버튼
        }

        // 사용자 정보 - 재사용 불가
        if (isEditing) {
            TextField(value = name, ...)
        } else {
            Text(text = user?.name ?: "")
        }

        // 버튼들 - 재사용 불가
        Button(onClick = { viewModel.logout() }) {
            Text("로그아웃")
        }
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 발생하는 문제 목록
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 코드가 200줄 이상으로 길어짐\n" +
                            "2. 어디를 수정해야 할지 찾기 어려움\n" +
                            "3. 프로필 이미지를 다른 화면에서 쓸 수 없음\n" +
                            "4. UI 테스트 외에 테스트 방법이 없음\n" +
                            "5. 새 개발자가 코드 이해하기 어려움",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제가 있는 모노리식 프로필 화면
 * (실제로는 ViewModel 없이 시뮬레이션)
 */
@Composable
fun MonolithicProfileScreen() {
    // 모든 상태가 여기에 선언됨
    var userName by remember { mutableStateOf("홍길동") }
    var userEmail by remember { mutableStateOf("hong@example.com") }
    var isEditing by remember { mutableStateOf(false) }
    var followerCount by remember { mutableIntStateOf(1234) }
    var followingCount by remember { mutableIntStateOf(567) }
    var postCount by remember { mutableIntStateOf(89) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // 모든 UI가 하나의 함수에...
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "모노리식 프로필 화면",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 프로필 이미지 (인라인으로 작성됨 - 재사용 불가)
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 이름 표시/편집 (조건부 렌더링이 복잡해짐)
            if (isEditing) {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("이름") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 통계 (같은 패턴이 3번 반복 - DRY 위반)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$postCount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "게시물", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$followerCount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "팔로워", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$followingCount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "팔로잉", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { isEditing = !isEditing },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isEditing) "저장" else "편집")
                }
                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("로그아웃")
                }
            }

            // 다이얼로그 (같은 파일에 모든 것이...)
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("로그아웃") },
                    text = { Text("정말 로그아웃 하시겠습니까?") },
                    confirmButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("확인")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("취소")
                        }
                    }
                )
            }
        }
    }
}

/**
 * 문제 2: Preview 불가능
 *
 * ViewModel을 직접 사용하면 Preview에서 오류가 발생합니다.
 */
@Composable
fun Problem2_NoPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                    text = "문제 2: Preview 불가능",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ViewModel을 직접 사용하는 Composable은\n" +
                            "@Preview에서 오류가 발생합니다.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Preview 실패 시뮬레이션
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFCDD2)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Preview 실패!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "java.lang.IllegalStateException:\n" +
                            "ViewModelStore should be set before\n" +
                            "setContentView()",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF8B0000)
                )
            }
        }

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Preview 실패 원인",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 이 코드는 Preview에서 오류 발생
@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()  // ViewModel 생성 시도 -> 실패!
}

// ProfileScreen 내부:
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = viewModel()  // 여기서 실패
    // ...
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 해결 방향
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "왜 문제인가?",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 개발 중 UI 확인이 어려움\n" +
                            "2. 매번 앱을 실행해야 함\n" +
                            "3. 디자이너와 협업 시 UI 공유 어려움\n" +
                            "4. 다양한 상태(로딩, 에러 등) 확인 불가\n\n" +
                            "해결: Screen + Content 분리\n" +
                            "Content는 상태를 파라미터로 받아 Preview 가능!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 문제 3: 재사용 불가능
 *
 * 컴포넌트가 분리되지 않아 다른 화면에서 재사용할 수 없습니다.
 */
@Composable
fun Problem3_NotReusable() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                    text = "문제 3: 재사용 불가능",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "UI 요소가 분리되지 않아 같은 코드를\n" +
                            "다른 화면에서 복사-붙여넣기 해야 합니다.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 중복 코드 예시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "화면 A: 프로필 화면",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 통계 표시 (중복 코드 1)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("89", fontWeight = FontWeight.Bold)
                        Text("게시물", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1,234", fontWeight = FontWeight.Bold)
                        Text("팔로워", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("567", fontWeight = FontWeight.Bold)
                        Text("팔로잉", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "화면 B: 다른 사용자 프로필",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                // 통계 표시 (중복 코드 2 - 거의 동일!)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("156", fontWeight = FontWeight.Bold)
                        Text("게시물", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("5,678", fontWeight = FontWeight.Bold)
                        Text("팔로워", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("234", fontWeight = FontWeight.Bold)
                        Text("팔로잉", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "중복되는 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// ProfileScreen.kt 에서
Row {
    Column {
        Text("${'$'}postCount")
        Text("게시물")
    }
    Column {
        Text("${'$'}followerCount")
        Text("팔로워")
    }
    // ...
}

// OtherUserScreen.kt 에서 (복사-붙여넣기!)
Row {
    Column {
        Text("${'$'}postCount")
        Text("게시물")
    }
    Column {
        Text("${'$'}followerCount")
        Text("팔로워")
    }
    // ...
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 문제점
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "발생하는 문제",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. 디자인 변경 시 여러 곳 수정 필요\n" +
                            "2. 수정 누락으로 UI 불일치 발생\n" +
                            "3. 코드 중복으로 유지보수 비용 증가\n" +
                            "4. 버그 수정 시 여러 곳에서 동일 작업\n\n" +
                            "해결: StatsRow Component로 추출!\n" +
                            "StatsRow(posts = 89, followers = 1234, ...)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
