# Snackbar 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `scaffold` | Scaffold의 기본 슬롯 구조와 SnackbarHost 사용법 | [📚 학습하기](../../structure/scaffold/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Snackbar는 사용자에게 간단한 피드백을 화면 하단에 표시하는 Material Design 컴포넌트입니다.
"알림 메모"처럼 잠깐 나타났다가 사라지며, 필요하면 버튼을 눌러 응답할 수 있습니다.

## 핵심 특징

1. **Scaffold 통합**: SnackbarHost를 통해 올바른 위치에 표시
2. **액션 버튼 지원**: "실행 취소" 같은 버튼 추가 가능
3. **프로그래밍 방식 표시**: SnackbarHostState.showSnackbar()로 제어

---

## 문제 상황: Toast의 한계

### 시나리오
할일 앱에서 아이템을 삭제한 후 사용자에게 피드백을 주고 싶습니다.
하지만 Toast를 사용하면 몇 가지 문제가 발생합니다.

### 잘못된 코드 예시 (Toast 사용)

```kotlin
// Compose에서 Toast를 사용하려면 Context가 필요합니다
val context = LocalContext.current

Button(onClick = {
    items = items - item

    // Toast - 액션 버튼이 없습니다!
    Toast.makeText(
        context,
        "$item 삭제됨",
        Toast.LENGTH_SHORT
    ).show()

    // 실행 취소 방법이 없습니다...
})
```

### 발생하는 문제점

1. **Context 필요**: LocalContext.current를 사용해야 함 (Compose스럽지 않음)
2. **액션 없음**: "실행 취소" 버튼을 제공할 수 없음
3. **복구 불가**: 사용자가 실수로 삭제해도 복구할 방법이 없음
4. **생명주기 무관**: Compose 생명주기와 독립적으로 동작

---

## 해결책: Snackbar 사용

### 핵심 구성요소

| 구성요소 | 역할 |
|---------|------|
| **SnackbarHostState** | Snackbar 상태를 관리하는 객체 |
| **SnackbarHost** | Snackbar를 화면에 표시하는 컨테이너 |
| **showSnackbar()** | Snackbar를 표시하는 suspend 함수 |

### 올바른 코드

```kotlin
// Step 1: 준비
val scope = rememberCoroutineScope()
val snackbarHostState = remember { SnackbarHostState() }

// Step 2: Scaffold에 SnackbarHost 배치
Scaffold(
    snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }
) { padding ->
    // Step 3: 버튼 클릭 시 Snackbar 표시
    Button(
        modifier = Modifier.padding(padding),
        onClick = {
            val deletedItem = item
            items = items - item

            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "$deletedItem 삭제됨",
                    actionLabel = "실행 취소",
                    duration = SnackbarDuration.Short
                )

                // Step 4: 사용자 응답 처리
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        // 실행 취소! 복구
                        items = items + deletedItem
                    }
                    SnackbarResult.Dismissed -> {
                        // 완전 삭제
                    }
                }
            }
        }
    ) {
        Text("삭제")
    }
}
```

### 해결되는 이유

- **Context 불필요**: SnackbarHostState만으로 동작
- **액션 버튼**: actionLabel로 "실행 취소" 추가 가능
- **복구 가능**: SnackbarResult.ActionPerformed로 복구 로직 실행
- **Compose 통합**: Composition 생명주기에 바인딩

---

## showSnackbar() API

```kotlin
suspend fun showSnackbar(
    message: String,                              // 표시할 메시지
    actionLabel: String? = null,                  // 액션 버튼 텍스트
    withDismissAction: Boolean = false,           // X 버튼 표시 여부
    duration: SnackbarDuration = SnackbarDuration.Short  // 표시 시간
): SnackbarResult
```

### SnackbarDuration 옵션

| 옵션 | 설명 |
|------|------|
| **Short** | 약 4초 (기본값) |
| **Long** | 약 10초 |
| **Indefinite** | 수동으로 닫을 때까지 |

### SnackbarResult 값

| 값 | 의미 |
|----|------|
| **ActionPerformed** | 사용자가 액션 버튼을 클릭함 |
| **Dismissed** | 시간 만료 또는 스와이프로 닫힘 |

---

## 사용 시나리오

### 1. 삭제 후 실행 취소
```kotlin
val result = snackbarHostState.showSnackbar(
    message = "이메일이 삭제되었습니다",
    actionLabel = "실행 취소"
)
if (result == SnackbarResult.ActionPerformed) {
    restoreEmail()
}
```

### 2. 네트워크 상태 알림
```kotlin
snackbarHostState.showSnackbar(
    message = "오프라인 상태입니다",
    duration = SnackbarDuration.Indefinite,
    actionLabel = "재시도"
)
```

### 3. 폼 제출 완료
```kotlin
snackbarHostState.showSnackbar(
    message = "저장되었습니다",
    duration = SnackbarDuration.Short
)
```

---

## 주의사항

### 1. rememberCoroutineScope는 Composable 외부에서 생성

```kotlin
// 올바른 방법
val scope = rememberCoroutineScope()  // Composable 레벨에서 생성

Button(onClick = {
    scope.launch {  // onClick 안에서 사용
        snackbarHostState.showSnackbar("메시지")
    }
})
```

### 2. Scaffold 없이 사용 시 직접 배치 필요

```kotlin
Box {
    // 콘텐츠

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.align(Alignment.BottomCenter)
    )
}
```

### 3. 큐 동작 이해하기

Material Design 가이드라인에 따라 한 번에 하나의 Snackbar만 표시됩니다.
여러 showSnackbar() 호출 시 자동으로 큐에 대기합니다.

---

## 연습 문제

### 연습 1: 기본 Snackbar 표시 (쉬움)

버튼을 클릭하면 "안녕하세요!" 메시지의 Snackbar를 표시하세요.

**목표**: showSnackbar() 기본 사용법 익히기

### 연습 2: 액션 버튼 처리 (중간)

"좋아요" 버튼을 구현하세요:
- 버튼 클릭 시 좋아요 수 증가 + Snackbar 표시
- "취소" 액션으로 좋아요 취소 가능

**목표**: actionLabel과 SnackbarResult 처리하기

### 연습 3: 삭제 + 실행 취소 (어려움)

메모 목록에서 아이템을 삭제하고 "실행 취소"로 복구하세요:
- 삭제 전 인덱스와 아이템 저장
- 복구 시 원래 위치에 삽입

**목표**: 실제 앱 시나리오 구현하기

---

## 다음 학습

- **rememberCoroutineScope**: onClick에서 코루틴 실행하기
- **Scaffold**: Material Design 레이아웃 구조
- **BottomSheet**: 하단에서 올라오는 시트 UI
