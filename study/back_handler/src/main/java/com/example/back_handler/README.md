# BackHandler 학습

## 개념

`BackHandler`는 Jetpack Compose에서 **시스템 뒤로가기 버튼(또는 제스처)을 가로채서** 커스텀 동작을 수행할 수 있게 해주는 Composable입니다.

```kotlin
import androidx.activity.compose.BackHandler

@Composable
fun EditScreen() {
    var hasChanges by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // 변경사항이 있을 때만 뒤로가기 가로채기
    BackHandler(enabled = hasChanges) {
        showDialog = true  // 확인 다이얼로그 표시
    }
}
```

## 핵심 특징

### 1. enabled 파라미터
```kotlin
BackHandler(enabled = condition) {
    // 뒤로가기 시 실행할 코드
}
```
- `enabled = true`: 핸들러 활성화 (기본값)
- `enabled = false`: 핸들러 비활성화, 시스템 기본 동작 수행
- **조건부 활성화**로 필요할 때만 가로채기

### 2. Innermost Wins (가장 안쪽 우선)
```kotlin
BackHandler { /* 외부 핸들러 */ }

if (showModal) {
    BackHandler { /* 내부 핸들러 - 이게 먼저! */ }
}
```
- 중첩된 경우 **가장 안쪽**의 enabled된 BackHandler가 우선
- 동시에 **하나의 BackHandler만** 활성 상태

### 3. 내부 동작
- 내부적으로 `OnBackPressedCallback` 사용
- Composable의 생명주기에 맞게 자동 등록/해제
- `LocalOnBackPressedDispatcherOwner`와 연결

## 문제 상황: BackHandler 없이 발생하는 문제

### 시나리오: 메모 작성 중 실수로 뒤로가기

```kotlin
// 문제가 있는 코드
@Composable
fun MemoScreen() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it }
    )

    // 뒤로가기 처리 없음!
    // → 사용자가 긴 메모 작성 중 뒤로가기 누르면
    // → 확인 없이 바로 화면 이탈
    // → 작성 내용 전부 손실
}
```

### 발생하는 문제점

1. **데이터 손실**: 저장하지 않은 작성 내용이 사라짐
2. **사용자 불만**: 실수로 뒤로가기 했을 때 복구 불가
3. **나쁜 UX**: 중요 작업 중 확인 없이 이탈

## 해결책: BackHandler 사용

### 올바른 코드

```kotlin
@Composable
fun MemoScreen() {
    var text by remember { mutableStateOf("") }
    var showExitDialog by remember { mutableStateOf(false) }

    val hasChanges = text.isNotEmpty()

    // 변경사항이 있을 때만 활성화
    BackHandler(enabled = hasChanges) {
        showExitDialog = true
    }

    TextField(
        value = text,
        onValueChange = { text = it }
    )

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("저장하지 않고 나가시겠습니까?") },
            text = { Text("작성 중인 내용이 삭제됩니다.") },
            confirmButton = {
                TextButton(onClick = { /* 나가기 */ }) {
                    Text("나가기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("계속 작성")
                }
            }
        )
    }
}
```

### 해결되는 이유

1. **조건부 활성화**: `enabled = hasChanges`로 필요할 때만 동작
2. **확인 다이얼로그**: 사용자에게 선택권 제공
3. **데이터 보호**: 실수로 인한 손실 방지

## 사용 시나리오

### 1. 저장되지 않은 변경사항 확인
```kotlin
val hasUnsavedChanges = currentData != savedData
BackHandler(enabled = hasUnsavedChanges) {
    showConfirmDialog = true
}
```

### 2. 중요 작업 중 이탈 방지
```kotlin
// 결제 진행 중
BackHandler(enabled = isProcessing) {
    showWarningDialog = true
}
```

### 3. 모달/바텀시트 닫기
```kotlin
if (showBottomSheet) {
    BackHandler {
        showBottomSheet = false  // 모달만 닫고 화면은 유지
    }
}
```

### 4. 커스텀 네비게이션
```kotlin
BackHandler {
    if (canGoBack) {
        navigateBack()
    } else {
        showExitConfirmation()
    }
}
```

## Android 13+ Predictive Back Gesture

### 기본 BackHandler
- 기존 방식으로 뒤로가기 이벤트 처리
- Predictive Back 애니메이션 미리보기 차단

### PredictiveBackHandler (고급)
```kotlin
import androidx.activity.compose.PredictiveBackHandler

PredictiveBackHandler { progress: Flow<BackEventCompat> ->
    progress.collect { backEvent ->
        // 제스처 진행 상태에 따른 애니메이션
        val scale = 1f - (backEvent.progress * 0.1f)
        // ...
    }
}
```
- 제스처 진행 상태 접근 가능
- 커스텀 애니메이션 구현

## 주의사항

### 1. 남용 금지
```kotlin
// 나쁜 예: 모든 화면에서 무조건 가로채기
BackHandler {
    // 항상 다이얼로그 표시 → 사용자 짜증
}

// 좋은 예: 필요할 때만 가로채기
BackHandler(enabled = hasUnsavedChanges) {
    showDialog = true
}
```

### 2. 탈출 경로 제공
```kotlin
// 항상 "나가기" 옵션 제공
AlertDialog(
    confirmButton = { /* 나가기 */ },
    dismissButton = { /* 계속 작성 */ }
)
```

### 3. 명확한 이유 설명
```kotlin
AlertDialog(
    title = { Text("저장하지 않고 나가시겠습니까?") },
    text = { Text("작성 중인 내용이 삭제됩니다.") }  // 왜 가로챘는지 설명
)
```

### 4. Lifecycle 고려
- BackHandler는 Composable이 Composition에 있을 때만 활성
- 화면을 떠나면 자동으로 해제됨

## 연습 문제

### 연습 1: 기본 BackHandler (쉬움)
카운터가 0이 아닐 때 뒤로가기 시 리셋 확인 다이얼로그를 표시하세요.

### 연습 2: 폼 변경 감지 (중간)
이름과 이메일 입력 폼에서 초기값과 다르면 뒤로가기를 가로채세요.

### 연습 3: 중첩 BackHandler (어려움)
모달이 열려있으면 모달 닫기, 닫혀있으면 종료 확인 다이얼로그를 표시하세요.

## 다음 학습

- **produceState**: 외부 상태를 Compose State로 변환
- **snapshotFlow**: Compose State를 Flow로 변환
- **PredictiveBackHandler**: 제스처 진행 상태 접근
