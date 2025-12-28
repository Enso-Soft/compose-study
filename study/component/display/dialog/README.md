# Dialog 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composition 간 값을 유지하는 상태 관리 기본 | [📚 학습하기](../../../state/remember/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Dialog(다이얼로그)는 사용자에게 **중요한 정보를 전달하거나 결정을 요청**할 때 사용하는 팝업 UI입니다. 현재 화면 위에 오버레이로 표시되어 사용자의 즉각적인 주의를 끌고 응답을 받습니다.

Compose에서는 `AlertDialog`와 `Dialog` 두 가지 방식으로 다이얼로그를 구현할 수 있습니다.

---

## 핵심 특징

### 1. AlertDialog - Material Design 규격

```kotlin
AlertDialog(
    onDismissRequest = { /* 외부 탭 또는 뒤로가기 시 */ },
    title = { Text("제목") },
    text = { Text("본문 내용") },
    confirmButton = {
        TextButton(onClick = { }) { Text("확인") }
    },
    dismissButton = {
        TextButton(onClick = { }) { Text("취소") }
    }
)
```

### 2. Dialog - 완전 커스텀 UI

```kotlin
Dialog(onDismissRequest = { }) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        // 완전히 자유로운 UI 구성
    }
}
```

### 3. 상태 기반 표시/숨김

```kotlin
var showDialog by remember { mutableStateOf(false) }

Button(onClick = { showDialog = true }) {
    Text("다이얼로그 열기")
}

if (showDialog) {
    AlertDialog(
        onDismissRequest = { showDialog = false },
        // ...
    )
}
```

---

## 문제 상황: Dialog 없이 확인 UI 구현

### 잘못된 코드 예시

```kotlin
// 문제 1: 인라인 확인 UI - 복잡하고 어색함
@Composable
fun DeleteButton() {
    var showConfirm by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { showConfirm = true }) {
            Text("삭제")
        }

        // 인라인 확인 UI - 레이아웃 변경됨
        if (showConfirm) {
            Row {
                Text("정말 삭제?")
                Button(onClick = { delete() }) { Text("예") }
                Button(onClick = { showConfirm = false }) { Text("아니오") }
            }
        }
    }
}

// 문제 2: 확인 없이 바로 삭제
Button(onClick = { deleteItem() }) {  // 실수로 삭제!
    Text("삭제")
}
```

### 발생하는 문제점

1. **레이아웃 변경**: 확인 UI가 나타나면 다른 요소들이 밀림
2. **사용자 경험 저하**: 중요한 작업에 대한 명확한 경고 부족
3. **실수 유발**: 확인 없이 바로 실행되어 되돌릴 수 없음

---

## 해결책: AlertDialog 사용

### 올바른 코드

```kotlin
@Composable
fun DeleteButton() {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDeleteDialog = true }) {
        Text("삭제")
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("삭제 확인") },
            text = { Text("이 항목을 삭제하시겠습니까?\n삭제 후 복구할 수 없습니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteItem()
                        showDeleteDialog = false
                    }
                ) {
                    Text("삭제", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}
```

### 해결되는 이유

1. **레이아웃 안정**: 오버레이로 표시되어 기존 UI 영향 없음
2. **명확한 경고**: 모달 형태로 사용자 주의 집중
3. **실수 방지**: 확인 과정을 통해 의도치 않은 실행 방지

---

## AlertDialog 파라미터

| 파라미터 | 설명 | 필수 |
|---------|------|------|
| `onDismissRequest` | 외부 탭/뒤로가기 시 호출 | O |
| `confirmButton` | 확인 버튼 컴포저블 | O |
| `dismissButton` | 취소 버튼 컴포저블 | X |
| `title` | 제목 컴포저블 | X |
| `text` | 본문 컴포저블 | X |
| `icon` | 아이콘 컴포저블 | X |
| `shape` | 다이얼로그 모양 | X |
| `containerColor` | 배경색 | X |
| `tonalElevation` | 표면 elevation | X |

---

## 사용 시나리오

### 1. 확인/취소 다이얼로그
```kotlin
AlertDialog(
    onDismissRequest = { showDialog = false },
    title = { Text("저장하시겠습니까?") },
    confirmButton = {
        TextButton(onClick = { save(); showDialog = false }) {
            Text("저장")
        }
    },
    dismissButton = {
        TextButton(onClick = { showDialog = false }) {
            Text("취소")
        }
    }
)
```

### 2. 경고 다이얼로그 (확인만)
```kotlin
AlertDialog(
    onDismissRequest = { showDialog = false },
    title = { Text("알림") },
    text = { Text("작업이 완료되었습니다.") },
    confirmButton = {
        TextButton(onClick = { showDialog = false }) {
            Text("확인")
        }
    }
)
```

### 3. 입력 다이얼로그
```kotlin
var inputText by remember { mutableStateOf("") }

AlertDialog(
    onDismissRequest = { showDialog = false },
    title = { Text("이름 변경") },
    text = {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("새 이름") }
        )
    },
    confirmButton = {
        TextButton(
            onClick = {
                updateName(inputText)
                showDialog = false
            }
        ) {
            Text("변경")
        }
    },
    dismissButton = {
        TextButton(onClick = { showDialog = false }) {
            Text("취소")
        }
    }
)
```

### 4. 선택 다이얼로그
```kotlin
val options = listOf("옵션 1", "옵션 2", "옵션 3")
var selectedOption by remember { mutableStateOf(options[0]) }

AlertDialog(
    onDismissRequest = { showDialog = false },
    title = { Text("옵션 선택") },
    text = {
        Column {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = option }
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = option == selectedOption,
                        onClick = { selectedOption = option }
                    )
                    Text(option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    },
    confirmButton = {
        TextButton(onClick = {
            applyOption(selectedOption)
            showDialog = false
        }) {
            Text("적용")
        }
    }
)
```

### 5. Custom Dialog
```kotlin
Dialog(onDismissRequest = { showDialog = false }) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("프로필 정보", style = MaterialTheme.typography.headlineSmall)
            // ... 커스텀 UI
        }
    }
}
```

---

## AlertDialog vs Dialog 비교

| 항목 | AlertDialog | Dialog |
|------|-------------|--------|
| **용도** | Material Design 규격 다이얼로그 | 완전 커스텀 다이얼로그 |
| **구조** | title, text, buttons 정해진 슬롯 | 완전히 자유로운 컨텐츠 |
| **스타일** | Material 테마 자동 적용 | 직접 Surface, Card 등 설정 |
| **크기** | 자동 설정 | 직접 설정 필요 |
| **권장 상황** | 간단한 확인/취소, 알림 | 복잡한 폼, 커스텀 레이아웃 |

### 언제 무엇을 사용할까?

```
간단한 확인/취소 → AlertDialog
    ↓
복잡한 입력 폼? → Dialog + Card/Surface
    ↓
Material 규격 따르기? → AlertDialog
    ↓
완전 커스텀 UI? → Dialog
```

---

## Dialog vs BottomSheet 선택 기준

| 상황 | 권장 UI |
|------|---------|
| 간단한 확인/경고 | AlertDialog |
| 2-3개 버튼 선택 | AlertDialog |
| 많은 옵션 선택 (5개 이상) | BottomSheet |
| 긴 콘텐츠 스크롤 | BottomSheet |
| 반복적인 작업 | BottomSheet |
| 중요한 결정/경고 | AlertDialog |

---

## DialogProperties

Dialog의 동작을 세부적으로 제어할 수 있는 속성입니다.

```kotlin
import androidx.compose.ui.window.DialogProperties

AlertDialog(
    onDismissRequest = { showDialog = false },
    properties = DialogProperties(
        dismissOnBackPress = true,      // 뒤로가기로 닫기 허용 (기본값: true)
        dismissOnClickOutside = true,   // 외부 탭으로 닫기 허용 (기본값: true)
        usePlatformDefaultWidth = true  // 플랫폼 기본 너비 사용 (기본값: true)
    ),
    title = { Text("제목") },
    // ...
)
```

| 속성 | 설명 | 기본값 |
|------|------|--------|
| `dismissOnBackPress` | 뒤로가기 버튼으로 닫기 허용 | `true` |
| `dismissOnClickOutside` | 외부 영역 탭으로 닫기 허용 | `true` |
| `usePlatformDefaultWidth` | 플랫폼 기본 너비 사용 | `true` |
| `decorFitsSystemWindows` | 시스템 윈도우 장식 적용 | `true` |

### 닫기 방지 다이얼로그

중요한 작업 중에는 사용자가 실수로 닫지 못하게 할 수 있습니다:

```kotlin
AlertDialog(
    onDismissRequest = { /* 아무것도 하지 않음 */ },
    properties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
    title = { Text("처리 중...") },
    text = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text("잠시만 기다려주세요")
        }
    },
    confirmButton = { }  // 버튼 없음
)
```

---

## 주의사항

1. **상태 관리 필수**
   - `showDialog` 상태 없이는 다이얼로그 제어 불가
   - `onDismissRequest`와 모든 버튼에서 상태 업데이트

2. **onDismissRequest 구현**
   - 외부 탭, 뒤로가기 처리를 위해 필수
   - 비워두면 다이얼로그가 닫히지 않음
   - `dismissOnBackPress`나 `dismissOnClickOutside`가 true여도 `onDismissRequest`에서 상태를 변경해야 함

3. **확인 버튼 로직**
   - 작업 수행 후 반드시 `showDialog = false`
   - 순서: 작업 실행 -> 다이얼로그 닫기

4. **다이얼로그 내 상태**
   - 입력 다이얼로그의 텍스트 상태는 외부에서 관리
   - 다이얼로그가 닫히면 내부 상태도 초기화됨

5. **임시 선택 상태 패턴**
   - 선택 다이얼로그에서는 임시 상태(`tempSelection`)를 사용
   - 확인 시에만 실제 값 업데이트, 취소 시 원래 값 유지

---

## 연습 문제

### 연습 1: 기본 확인/취소 다이얼로그
카운터 리셋 확인 다이얼로그를 구현하세요.

### 연습 2: 입력 다이얼로그
이름 변경 다이얼로그를 구현하세요.

### 연습 3: Custom Dialog
프로필 카드 형태의 커스텀 다이얼로그를 구현하세요.

---

## 다음 학습

- **BottomSheet**: 많은 옵션이나 긴 콘텐츠 표시
- **Snackbar**: 간단한 피드백 메시지
- **BackHandler**: 다이얼로그와 뒤로가기 처리
