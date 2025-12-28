package com.example.desktop_extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 연습 문제 1: MenuBar 구성하기 (쉬움)
 *
 * 목표: Desktop 앱의 기본 메뉴바 구조 이해하기
 */
@Composable
fun Practice1_MenuBarScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: MenuBar 구성하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "File 메뉴에 New, Open, Save, Exit 항목을 추가하고 " +
                            "각각에 키보드 단축키를 연결하세요."
                )
            }
        }

        // 요구사항 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                RequirementItem("\"File\" 메뉴 생성 (mnemonic: 'F')")
                RequirementItem("New 항목 (Ctrl+N)")
                RequirementItem("Open 항목 (Ctrl+O)")
                RequirementItem("Save 항목 (Ctrl+S)")
                RequirementItem("Separator (구분선)")
                RequirementItem("Exit 항목 (Esc)")
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- MenuBar { Menu(...) { Item(...) } } 구조 사용")
                Text("- shortcut = KeyShortcut(Key.S, ctrl = true)")
                Text("- mnemonic = 'F'로 Alt+F 단축 문자 지정")
                Text("- Separator()로 구분선 추가")
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFCE4EC)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "아래 TODO를 완성하세요",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                PracticeCodeBlock(
                    code = """
// TODO: MenuBar 구성하기
// 아래 코드를 Desktop 프로젝트의 Window 안에 작성하세요

MenuBar {
    // TODO: File 메뉴 추가
    // Menu("File", mnemonic = ???) {
    //
    //     // TODO: New 항목 추가 (Ctrl+N)
    //     // Item(...)
    //
    //     // TODO: Open 항목 추가 (Ctrl+O)
    //     // Item(...)
    //
    //     // TODO: Save 항목 추가 (Ctrl+S)
    //     // Item(...)
    //
    //     // TODO: 구분선 추가
    //     // Separator()
    //
    //     // TODO: Exit 항목 추가 (Esc)
    //     // Item(...)
    // }
}
                    """.trimIndent()
                )
            }
        }

        // 정답 카드
        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    PracticeCodeBlock(
                        code = """
MenuBar {
    Menu("File", mnemonic = 'F') {
        Item(
            "New",
            onClick = { /* 새 문서 */ },
            shortcut = KeyShortcut(Key.N, ctrl = true)
        )
        Item(
            "Open",
            onClick = { /* 열기 */ },
            shortcut = KeyShortcut(Key.O, ctrl = true)
        )
        Item(
            "Save",
            onClick = { /* 저장 */ },
            shortcut = KeyShortcut(Key.S, ctrl = true)
        )
        Separator()
        Item(
            "Exit",
            onClick = ::exitApplication,
            shortcut = KeyShortcut(Key.Escape)
        )
    }
}
                        """.trimIndent()
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 2: Tray + 알림 구현하기 (중간)
 *
 * 목표: 시스템 트레이 아이콘과 알림 기능 이해하기
 */
@Composable
fun Practice2_TrayScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: Tray + 알림 구현하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "시스템 트레이 아이콘을 추가하고, 버튼 클릭 시 " +
                            "알림을 보내는 기능을 구현하세요."
                )
            }
        }

        // 요구사항 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                RequirementItem("rememberTrayState()로 트레이 상태 관리")
                RequirementItem("rememberNotification()으로 알림 생성")
                RequirementItem("Tray() composable로 트레이 아이콘 추가")
                RequirementItem("트레이 메뉴: Show/Hide, Send Notification, Exit")
                RequirementItem("trayState.sendNotification()으로 알림 전송")
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. val trayState = rememberTrayState()")
                Text("2. val notification = rememberNotification(\"제목\", \"내용\")")
                Text("3. Tray(state = trayState, icon = ..., menu = { ... })")
                Text("4. trayState.sendNotification(notification)")
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "아래 TODO를 완성하세요",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                PracticeCodeBlock(
                    code = """
fun main() = application {
    var isVisible by remember { mutableStateOf(true) }

    // TODO: 트레이 상태 생성
    // val trayState = ???

    // TODO: 알림 생성 (제목: "알림", 내용: "새 메시지!")
    // val notification = ???

    // TODO: 시스템 트레이 구현
    // Tray(
    //     state = ???,
    //     icon = TrayIcon,
    //     tooltip = "My App",
    //     menu = {
    //         // TODO: Show/Hide 메뉴
    //         // Item(...)
    //
    //         // TODO: Send Notification 메뉴
    //         // Item(...)
    //
    //         // TODO: 구분선
    //         // Separator()
    //
    //         // TODO: Exit 메뉴
    //         // Item(...)
    //     }
    // )

    if (isVisible) {
        Window(
            onCloseRequest = { isVisible = false },
            title = "My App"
        ) {
            Text("Main Content")
        }
    }
}
                    """.trimIndent()
                )
            }
        }

        // 정답 카드
        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    PracticeCodeBlock(
                        code = """
fun main() = application {
    var isVisible by remember { mutableStateOf(true) }

    val trayState = rememberTrayState()
    val notification = rememberNotification("알림", "새 메시지!")

    Tray(
        state = trayState,
        icon = TrayIcon,
        tooltip = "My App",
        menu = {
            Item("Show/Hide") {
                isVisible = !isVisible
            }
            Item("Send Notification") {
                trayState.sendNotification(notification)
            }
            Separator()
            Item("Exit", onClick = ::exitApplication)
        }
    )

    if (isVisible) {
        Window(
            onCloseRequest = { isVisible = false },
            title = "My App"
        ) {
            Text("Main Content")
        }
    }
}
                        """.trimIndent()
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 3: 완전한 Desktop 앱 설계 (어려움)
 *
 * 목표: Window + MenuBar + Tray + 키보드 단축키 통합
 */
@Composable
fun Practice3_IntegrationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 완전한 Desktop 앱 설계",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Window + MenuBar + Tray를 조합한 완전한 Desktop 앱을 설계하세요. " +
                            "창을 닫으면 트레이로 숨기고, 트레이 메뉴에서 다시 열거나 " +
                            "완전 종료할 수 있어야 합니다."
                )
            }
        }

        // 요구사항 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "요구사항",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                RequirementItem("창 크기: 800x600dp, 제목: \"My Desktop App\"")
                RequirementItem("MenuBar: File(New/Open/Save/Exit), Edit(Cut/Copy/Paste)")
                RequirementItem("모든 메뉴 항목에 적절한 단축키 연결")
                RequirementItem("시스템 트레이: 창 최소화 시 트레이로 숨기기")
                RequirementItem("트레이 메뉴: Open, Hide, Exit")
                RequirementItem("창 닫기(X 버튼) 시 완전 종료가 아닌 트레이로 숨김")
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. var isVisible by remember { mutableStateOf(true) }")
                Text("2. Window의 onCloseRequest에서 isVisible = false")
                Text("3. if (isVisible) { Window { MenuBar { ... } } }")
                Text("4. Tray는 isVisible과 관계없이 항상 표시")
                Text("5. 각 기능을 별도 함수로 분리하면 관리 용이")
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "아래 TODO를 완성하세요",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                PracticeCodeBlock(
                    code = """
fun main() = application {
    // TODO: 창 표시 상태
    // var isVisible by remember { ??? }

    // TODO: 트레이 상태 및 알림
    // val trayState = ???
    // val notification = ???

    // TODO: 시스템 트레이 구현
    // Tray(
    //     state = ???,
    //     icon = AppIcon,
    //     tooltip = "My Desktop App",
    //     menu = {
    //         // TODO: Open 메뉴 (창 열기)
    //         // TODO: Hide 메뉴 (창 숨기기)
    //         // TODO: 구분선
    //         // TODO: Exit 메뉴 (완전 종료)
    //     }
    // )

    // TODO: 메인 창 (isVisible일 때만 표시)
    // if (???) {
    //     Window(
    //         onCloseRequest = { ??? },  // 닫으면 숨김
    //         state = rememberWindowState(size = ???),
    //         title = "My Desktop App"
    //     ) {
    //         // TODO: MenuBar 구현
    //         // MenuBar {
    //         //     Menu("File") { ... }
    //         //     Menu("Edit") { ... }
    //         // }
    //
    //         // 앱 콘텐츠
    //         AppContent()
    //     }
    // }
}

@Composable
fun AppContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Welcome to My Desktop App!")
    }
}
                    """.trimIndent()
                )
            }
        }

        // 정답 카드
        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    PracticeCodeBlock(
                        code = """
fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    val trayState = rememberTrayState()
    val notification = rememberNotification(
        "My Desktop App",
        "앱이 트레이로 최소화되었습니다."
    )

    // 시스템 트레이 (항상 표시)
    Tray(
        state = trayState,
        icon = AppIcon,
        tooltip = "My Desktop App",
        menu = {
            Item("Open") { isVisible = true }
            Item("Hide") { isVisible = false }
            Separator()
            Item("Exit", onClick = ::exitApplication)
        }
    )

    // 메인 창
    if (isVisible) {
        Window(
            onCloseRequest = {
                isVisible = false
                trayState.sendNotification(notification)
            },
            state = rememberWindowState(
                size = DpSize(800.dp, 600.dp)
            ),
            title = "My Desktop App"
        ) {
            MenuBar {
                Menu("File", mnemonic = 'F') {
                    Item("New",
                        onClick = { /* 새 문서 */ },
                        shortcut = KeyShortcut(Key.N, ctrl = true))
                    Item("Open",
                        onClick = { /* 열기 */ },
                        shortcut = KeyShortcut(Key.O, ctrl = true))
                    Item("Save",
                        onClick = { /* 저장 */ },
                        shortcut = KeyShortcut(Key.S, ctrl = true))
                    Separator()
                    Item("Exit",
                        onClick = ::exitApplication,
                        shortcut = KeyShortcut(Key.Q, ctrl = true))
                }
                Menu("Edit", mnemonic = 'E') {
                    Item("Cut",
                        onClick = { /* 잘라내기 */ },
                        shortcut = KeyShortcut(Key.X, ctrl = true))
                    Item("Copy",
                        onClick = { /* 복사 */ },
                        shortcut = KeyShortcut(Key.C, ctrl = true))
                    Item("Paste",
                        onClick = { /* 붙여넣기 */ },
                        shortcut = KeyShortcut(Key.V, ctrl = true))
                }
            }
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Welcome to My Desktop App!")
    }
}
                        """.trimIndent()
                    )
                }
            }
        }

        // 추가 도전 과제
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "추가 도전 과제",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "- View 메뉴에 CheckboxItem으로 'Dark Mode' 토글 추가\n" +
                            "- Help 메뉴에 'About' 항목 추가 (다이얼로그 표시)\n" +
                            "- 다중 창 지원 (Window 메뉴에서 'New Window' 클릭 시 새 창)\n" +
                            "- 창 위치/크기 기억하기 (rememberWindowState)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

@Composable
private fun RequirementItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            "- ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PracticeCodeBlock(code: String) {
    Surface(
        color = Color(0xFF2B2B2B),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            Text(
                text = code,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = Color(0xFFA9B7C6),
                lineHeight = 14.sp
            )
        }
    }
}
