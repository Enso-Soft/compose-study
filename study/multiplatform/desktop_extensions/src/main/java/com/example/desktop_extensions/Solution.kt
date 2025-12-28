package com.example.desktop_extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
 * Solution 화면
 *
 * Desktop Extensions API를 코드 예제와 함께 설명합니다.
 * 실제 Desktop 프로젝트에서 사용할 수 있는 코드를 제공합니다.
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 소개 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "해결책: Desktop Extensions 사용",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Compose for Desktop은 데스크톱 플랫폼 전용 API를 제공합니다. " +
                            "Window, MenuBar, Tray, KeyShortcut 등을 사용하여 네이티브 데스크톱 경험을 구현할 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 참고 사항
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "이 코드는 Compose Desktop 프로젝트에서 실행해야 합니다. " +
                            "Android 프로젝트에서는 동작하지 않습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        // 1. Window 관리
        SolutionSection(
            number = 1,
            title = "Window 관리",
            description = "데스크톱 창의 크기, 위치, 상태를 제어합니다."
        ) {
            CodeBlock(
                title = "기본 Window",
                code = """
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            size = DpSize(800.dp, 600.dp)
        ),
        title = "My Desktop App",
        icon = painterResource("app-icon.png")
    ) {
        App()  // 앱 콘텐츠
    }
}
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CodeBlock(
                title = "간단한 단일 창 앱",
                code = """
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Simple App",
    state = WindowState(size = DpSize(400.dp, 300.dp))
) {
    Text("Hello Desktop!")
}
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // WindowState 설명
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "WindowState 주요 속성",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PropertyRow("size", "DpSize(width, height)", "창 크기")
                    PropertyRow("position", "WindowPosition", "창 위치")
                    PropertyRow("isMinimized", "Boolean", "최소화 상태")
                    PropertyRow("placement", "WindowPlacement", "전체화면/최대화")
                }
            }
        }

        // 2. MenuBar
        SolutionSection(
            number = 2,
            title = "MenuBar (메뉴바)",
            description = "데스크톱 앱의 상단 메뉴바를 구성합니다."
        ) {
            CodeBlock(
                title = "MenuBar 구성",
                code = """
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut

Window(onCloseRequest = ::exitApplication) {
    MenuBar {
        // File 메뉴
        Menu("File", mnemonic = 'F') {
            Item(
                "Open",
                onClick = { /* 파일 열기 */ },
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

        // Edit 메뉴
        Menu("Edit", mnemonic = 'E') {
            Item("Cut", shortcut = KeyShortcut(Key.X, ctrl = true))
            Item("Copy", shortcut = KeyShortcut(Key.C, ctrl = true))
            Item("Paste", shortcut = KeyShortcut(Key.V, ctrl = true))
        }

        // View 메뉴 (체크박스 항목)
        Menu("View", mnemonic = 'V') {
            CheckboxItem(
                "Show Toolbar",
                checked = showToolbar,
                onCheckedChange = { showToolbar = it }
            )
            Menu("Theme") {  // 서브메뉴
                Item("Light", onClick = { theme = "light" })
                Item("Dark", onClick = { theme = "dark" })
            }
        }
    }

    // 앱 콘텐츠
    Content()
}
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // MenuBar 구성 요소 표
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "MenuBar 구성 요소",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MenuElementRow("Menu", "메뉴 그룹", "File, Edit, View")
                    MenuElementRow("Item", "메뉴 항목", "Open, Save, Exit")
                    MenuElementRow("CheckboxItem", "체크박스", "Show Toolbar")
                    MenuElementRow("Separator", "구분선", "---")
                    MenuElementRow("mnemonic", "단축 문자", "Alt+F")
                    MenuElementRow("shortcut", "키보드 단축키", "Ctrl+S")
                }
            }
        }

        // 3. Tray
        SolutionSection(
            number = 3,
            title = "Tray (시스템 트레이)",
            description = "시스템 트레이(작업 표시줄 알림 영역)에 아이콘을 추가합니다."
        ) {
            CodeBlock(
                title = "Tray 구현",
                code = """
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberNotification

fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    val trayState = rememberTrayState()
    val notification = rememberNotification(
        "알림 제목",
        "새 메시지가 도착했습니다!"
    )

    // 시스템 트레이
    Tray(
        state = trayState,
        icon = painterResource("tray-icon.png"),
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

    // 창 (숨길 수 있음)
    if (isVisible) {
        Window(
            onCloseRequest = { isVisible = false },
            title = "My App"
        ) {
            Content()
        }
    }
}
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 알림 유형
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "알림 유형 (Notification.Type)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NotificationTypeRow("None", "일반 알림", Color.Gray)
                    NotificationTypeRow("Warning", "경고 알림", Color(0xFFFF9800))
                    NotificationTypeRow("Error", "오류 알림", Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            CodeBlock(
                title = "트레이만 있는 앱 (창 없이)",
                code = """
fun main() = application {
    Tray(
        icon = TrayIcon,
        menu = {
            Item("Do Something") { /* 작업 */ }
            Item("Exit", onClick = ::exitApplication)
        }
    )
    // 창 없이 트레이만 실행!
}
                """.trimIndent()
            )
        }

        // 4. KeyShortcut
        SolutionSection(
            number = 4,
            title = "KeyShortcut (키보드 단축키)",
            description = "메뉴 항목에 키보드 단축키를 연결합니다."
        ) {
            CodeBlock(
                title = "KeyShortcut 사용법",
                code = """
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut

// 기본 형식
KeyShortcut(Key.S, ctrl = true)           // Ctrl+S
KeyShortcut(Key.S, ctrl = true, shift = true)  // Ctrl+Shift+S
KeyShortcut(Key.Z, ctrl = true, alt = true)    // Ctrl+Alt+Z
KeyShortcut(Key.Escape)                    // Esc

// macOS에서는 ctrl이 자동으로 Cmd로 변환됩니다
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 단축키 표
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "주요 단축키 예시",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ShortcutRow("저장", "Ctrl+S", "Cmd+S")
                    ShortcutRow("열기", "Ctrl+O", "Cmd+O")
                    ShortcutRow("복사", "Ctrl+C", "Cmd+C")
                    ShortcutRow("붙여넣기", "Ctrl+V", "Cmd+V")
                    ShortcutRow("실행취소", "Ctrl+Z", "Cmd+Z")
                    ShortcutRow("닫기", "Ctrl+W", "Cmd+W")
                }
            }
        }

        // 5. 추가 기능
        SolutionSection(
            number = 5,
            title = "추가 기능",
            description = "Context Menu, Tooltip 등 데스크톱 전용 기능입니다."
        ) {
            CodeBlock(
                title = "Context Menu (우클릭 메뉴)",
                code = """
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem

ContextMenuArea(items = {
    listOf(
        ContextMenuItem("Cut") { /* 잘라내기 */ },
        ContextMenuItem("Copy") { /* 복사 */ },
        ContextMenuItem("Paste") { /* 붙여넣기 */ }
    )
}) {
    // 우클릭할 영역
    Text("Right-click me!")
}
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CodeBlock(
                title = "Tooltip (도움말)",
                code = """
import androidx.compose.foundation.TooltipArea

TooltipArea(
    tooltip = {
        Surface(
            color = Color.LightGray,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                "이것은 도움말입니다",
                modifier = Modifier.padding(8.dp)
            )
        }
    },
    delayMillis = 600  // 600ms 후 표시
) {
    Button(onClick = {}) {
        Text("Hover me")
    }
}
                """.trimIndent()
            )
        }

        // 통합 예제
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "통합 예제: 메신저 앱",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))

                CodeBlock(
                    title = "",
                    code = """
fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    var unreadCount by remember { mutableStateOf(0) }
    val trayState = rememberTrayState()

    // 시스템 트레이
    Tray(
        state = trayState,
        icon = if (unreadCount > 0) UnreadIcon else NormalIcon,
        tooltip = if (unreadCount > 0)
            "${'$'}unreadCount new messages" else "Messenger",
        menu = {
            Item("Open") { isVisible = true }
            if (unreadCount > 0) {
                Item("Mark all as read") { unreadCount = 0 }
            }
            Separator()
            Item("Exit", onClick = ::exitApplication)
        }
    )

    // 메인 창
    if (isVisible) {
        Window(
            onCloseRequest = { isVisible = false },  // 닫으면 트레이로
            title = "Messenger"
        ) {
            MenuBar {
                Menu("File") {
                    Item("Settings") { /* 설정 */ }
                    Separator()
                    Item("Exit", onClick = ::exitApplication)
                }
            }
            MessengerContent()
        }
    }
}
                    """.trimIndent()
                )
            }
        }

        // 다음 단계
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "다음 단계",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Practice 탭에서 직접 코드를 작성해보세요!\n" +
                            "실제로 Desktop 앱을 만들려면 Compose Multiplatform 프로젝트를 생성해야 합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun SolutionSection(
    number: Int,
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$number",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun CodeBlock(
    title: String,
    code: String
) {
    Column {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
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
                    fontSize = 11.sp,
                    color = Color(0xFFA9B7C6),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun PropertyRow(
    name: String,
    type: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            name,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            type,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.35f)
        )
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f)
        )
    }
}

@Composable
private fun MenuElementRow(
    element: String,
    description: String,
    example: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            element,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.35f)
        )
        Text(
            example,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.35f)
        )
    }
}

@Composable
private fun NotificationTypeRow(
    type: String,
    description: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            type,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Composable
private fun ShortcutRow(
    action: String,
    windows: String,
    mac: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            action,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.34f)
        )
        Text(
            windows,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.33f)
        )
        Text(
            mac,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(0.33f)
        )
    }
}
