# Desktop Extensions 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `compose_multiplatform_intro` | Compose Multiplatform 기초 개념 | [📚 학습하기](../compose_multiplatform_intro/README.md) |
| `composable_function` | @Composable 함수 작성법 | [📚 학습하기](../../basics/composable_function/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Desktop Extensions는 **Compose for Desktop(Compose Multiplatform)에서 데스크톱 플랫폼 전용 기능을 제공하는 API 모음**입니다.
모바일 UI와 달리 데스크톱 사용자가 기대하는 네이티브 경험(메뉴바, 시스템 트레이, 키보드 단축키 등)을 구현할 수 있습니다.

> **참고**: 이 모듈은 Android 프로젝트이지만, Desktop Extensions의 개념과 코드를 학습하기 위한 교육용 모듈입니다.
> 실제 Desktop 앱을 만들려면 Compose Multiplatform Desktop 프로젝트를 생성해야 합니다.

## 핵심 특징

1. **Window 관리**: 창 크기, 위치, 상태(최대화/최소화) 제어
2. **MenuBar**: 네이티브 메뉴바 (File, Edit, View 등)
3. **Tray**: 시스템 트레이 아이콘 및 알림
4. **KeyShortcut**: 키보드 단축키 (Ctrl+C, Ctrl+S 등)
5. **추가 기능**: 파일 다이얼로그, 컨텍스트 메뉴, 드래그 앤 드롭

---

## 문제 상황: 모바일 UI를 Desktop에 그대로 사용하면?

### 시나리오

Android 개발자 A씨는 Compose로 멋진 앱을 만들었습니다.
Compose Multiplatform을 사용해 Desktop 버전도 만들기로 했지만,
모바일 UI를 그대로 가져왔더니 사용자들의 불만이 쏟아졌습니다.

### 발생하는 문제점

#### 1. 메뉴 접근성 문제

| 모바일 방식 | 데스크톱 기대 |
|------------|--------------|
| 햄버거 아이콘 클릭 | 상단 메뉴바에서 바로 선택 |
| 드로어 열기 | File > Open |
| 메뉴 선택 (3단계) | 1단계로 접근 |

```
모바일: [햄버거] -> [드로어 열림] -> [메뉴 선택]
Desktop: [File 클릭] -> [Open 선택]
```

#### 2. 키보드 단축키 부재

| 작업 | 모바일 | 데스크톱 기대 |
|------|--------|--------------|
| 복사 | 길게 누르기 -> 복사 선택 | Ctrl+C |
| 붙여넣기 | 길게 누르기 -> 붙여넣기 | Ctrl+V |
| 저장 | 버튼 클릭 | Ctrl+S |
| 실행 취소 | 없거나 버튼 | Ctrl+Z |

데스크톱 사용자는 키보드로 빠르게 작업하길 원합니다!

#### 3. 백그라운드 실행 불가

- **모바일**: 앱을 닫아도 백그라운드에서 서비스 실행
- **Desktop**: 창을 닫으면 앱 완전 종료
- **필요한 것**: 시스템 트레이에서 대기하다 필요시 복귀

```
예: 메신저 앱
- 창을 닫아도 메시지 알림 받기
- 트레이 아이콘 클릭으로 다시 열기
- 완전 종료는 트레이 메뉴에서 선택
```

#### 4. 창 관리 불가

- **모바일**: 항상 전체 화면
- **Desktop**: 다양한 창 크기, 다중 모니터, 창 위치 기억

```
데스크톱 사용자 기대:
- 원하는 크기로 창 조절
- 마지막 위치 기억
- 다중 창 지원
```

---

## 해결책: Desktop Extensions 사용

### 1. Window 관리

#### 기본 Window
```kotlin
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
        // 앱 콘텐츠
        App()
    }
}
```

#### 간단한 단일 창 앱
```kotlin
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Simple App",
    state = WindowState(size = DpSize(400.dp, 300.dp))
) {
    Text("Hello Desktop!")
}
```

### 2. MenuBar (메뉴바)

데스크톱 앱의 상단 메뉴바를 구성합니다.

```kotlin
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

        // View 메뉴
        Menu("View", mnemonic = 'V') {
            CheckboxItem(
                "Show Toolbar",
                checked = showToolbar,
                onCheckedChange = { showToolbar = it }
            )
            Menu("Theme") {
                Item("Light", onClick = { theme = "light" })
                Item("Dark", onClick = { theme = "dark" })
            }
        }
    }

    // 앱 콘텐츠
    Content()
}
```

#### MenuBar 구성 요소

| 요소 | 설명 | 예시 |
|------|------|------|
| `Menu` | 메뉴 그룹 | File, Edit, View |
| `Item` | 메뉴 항목 | Open, Save, Exit |
| `CheckboxItem` | 체크박스 항목 | Show Toolbar |
| `Separator` | 구분선 | --- |
| `mnemonic` | 단축 문자 | Alt+F로 File 메뉴 |
| `shortcut` | 키보드 단축키 | Ctrl+S |

### 3. Tray (시스템 트레이)

시스템 트레이(작업 표시줄 알림 영역)에 아이콘을 추가합니다.

```kotlin
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
            Item("Show/Hide", onClick = { isVisible = !isVisible })
            Item("Send Notification", onClick = {
                trayState.sendNotification(notification)
            })
            Separator()
            Item("Exit", onClick = ::exitApplication)
        }
    )

    // 창 (숨길 수 있음)
    if (isVisible) {
        Window(
            onCloseRequest = { isVisible = false }, // 닫으면 숨김
            title = "My App"
        ) {
            Content()
        }
    }
}
```

#### 알림 유형

```kotlin
// 일반 알림
rememberNotification("제목", "내용", Notification.Type.None)

// 경고 알림
rememberNotification("경고", "주의가 필요합니다", Notification.Type.Warning)

// 오류 알림
rememberNotification("오류", "문제가 발생했습니다", Notification.Type.Error)
```

#### 트레이만 있는 앱 (창 없이)

```kotlin
fun main() = application {
    Tray(
        icon = TrayIcon,
        menu = {
            Item("Do Something", onClick = { /* 작업 */ })
            Item("Exit", onClick = ::exitApplication)
        }
    )
    // 창 없이 트레이만 실행!
}
```

### 4. KeyShortcut (키보드 단축키)

```kotlin
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut

// 기본 형식
KeyShortcut(Key.S, ctrl = true)           // Ctrl+S
KeyShortcut(Key.S, ctrl = true, shift = true)  // Ctrl+Shift+S
KeyShortcut(Key.Z, ctrl = true, alt = true)    // Ctrl+Alt+Z
KeyShortcut(Key.Escape)                    // Esc

// macOS에서는 ctrl이 자동으로 Cmd로 변환됩니다
```

#### 주요 단축키 예시

| 기능 | Windows/Linux | macOS |
|------|---------------|-------|
| 저장 | Ctrl+S | Cmd+S |
| 열기 | Ctrl+O | Cmd+O |
| 복사 | Ctrl+C | Cmd+C |
| 붙여넣기 | Ctrl+V | Cmd+V |
| 실행취소 | Ctrl+Z | Cmd+Z |
| 닫기 | Ctrl+W | Cmd+W |

### 5. 추가 기능

#### Context Menu (우클릭 메뉴)

```kotlin
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
```

#### Tooltip (도움말)

```kotlin
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement

TooltipArea(
    tooltip = {
        Surface(
            modifier = Modifier.shadow(4.dp),
            color = Color.LightGray,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("이것은 도움말입니다", modifier = Modifier.padding(8.dp))
        }
    },
    delayMillis = 600
) {
    Button(onClick = {}) {
        Text("Hover me")
    }
}
```

---

## 사용 시나리오

### 시나리오 1: 텍스트 편집기

```kotlin
fun main() = application {
    var text by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("Untitled") }

    Window(
        onCloseRequest = ::exitApplication,
        title = "$fileName - Text Editor"
    ) {
        MenuBar {
            Menu("File") {
                Item("New", shortcut = KeyShortcut(Key.N, ctrl = true)) {
                    text = ""
                    fileName = "Untitled"
                }
                Item("Save", shortcut = KeyShortcut(Key.S, ctrl = true)) {
                    // 저장 로직
                }
            }
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxSize()
        )
    }
}
```

### 시나리오 2: 메신저 앱

```kotlin
fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    var unreadCount by remember { mutableStateOf(0) }
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = if (unreadCount > 0) UnreadIcon else NormalIcon,
        tooltip = if (unreadCount > 0) "$unreadCount new messages" else "Messenger",
        menu = {
            Item("Open", onClick = { isVisible = true })
            if (unreadCount > 0) {
                Item("Mark all as read", onClick = { unreadCount = 0 })
            }
            Separator()
            Item("Exit", onClick = ::exitApplication)
        }
    )

    if (isVisible) {
        Window(onCloseRequest = { isVisible = false }) {
            MessengerContent()
        }
    }
}
```

### 시나리오 3: 다중 창 앱

```kotlin
fun main() = application {
    val windows = remember { mutableStateListOf<WindowInfo>() }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Main Window"
    ) {
        MenuBar {
            Menu("Window") {
                Item("New Window", shortcut = KeyShortcut(Key.N, ctrl = true)) {
                    windows.add(WindowInfo("Window ${windows.size + 1}"))
                }
            }
        }
        MainContent()
    }

    // 추가 창들
    windows.forEachIndexed { index, info ->
        Window(
            onCloseRequest = { windows.removeAt(index) },
            title = info.title
        ) {
            SecondaryContent()
        }
    }
}
```

---

## 주의사항

1. **플랫폼 전용 API**: Desktop Extensions는 Desktop 타겟에서만 사용 가능합니다
2. **expect/actual**: Multiplatform에서는 expect/actual 패턴으로 플랫폼별 구현 분리
3. **테스트**: Desktop UI 테스트는 별도 설정 필요
4. **배포**: native distribution 패키징 필요 (`./gradlew packageDistributionForCurrentOS`)

---

## 연습 문제

### 연습 1: MenuBar 구성하기 - 쉬움

File 메뉴에 New, Open, Save, Exit 항목을 추가하고 각각에 단축키를 연결하세요.

### 연습 2: Tray + 알림 구현하기 - 중간

시스템 트레이 아이콘을 추가하고, 버튼 클릭 시 알림을 보내는 기능을 구현하세요.

### 연습 3: 완전한 Desktop 앱 설계 - 어려움

Window + MenuBar + Tray를 조합한 완전한 Desktop 앱을 설계하세요.
창을 닫으면 트레이로 숨기고, 트레이 메뉴에서 다시 열거나 완전 종료할 수 있어야 합니다.

---

## 다음 학습

- [Compose Multiplatform 기초](../compose_multiplatform_basics/README.md)
- [Platform-specific APIs](../platform_specific/README.md)
- [Desktop 배포](../desktop_distribution/README.md)

---

## 참고 자료

- [Compose Multiplatform Desktop-only API](https://kotlinlang.org/docs/multiplatform/compose-desktop-components.html)
- [JetBrains Compose Multiplatform Tutorials](https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials)
- [Tray, Notifications, MenuBar Tutorial](https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Tray_Notifications_MenuBar_new)
