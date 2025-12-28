# Tooltip 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | Text, Button 등 기본 UI 컴포넌트 사용법 | [📚 학습하기](../../../layout/basic_ui_components/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Tooltip은 UI 요소에 대한 추가 설명을 제공하는 팝업 컴포넌트입니다.
마치 만화의 말풍선처럼, 아이콘이나 버튼 위에 잠시 나타나 그 기능을 설명해줍니다.

## 핵심 특징

1. **TooltipBox**: Tooltip과 앵커(대상 요소)를 연결하는 컨테이너
2. **PlainTooltip**: 간단한 텍스트만 표시하는 기본 Tooltip
3. **RichTooltip**: 제목, 본문, 액션 버튼을 포함할 수 있는 상세 Tooltip
4. **TooltipState**: Tooltip의 표시/숨김을 프로그래밍 방식으로 제어

## 비유로 이해하기

Tooltip은 '말풍선'과 같습니다:
- 만화에서 캐릭터 위에 뜨는 말풍선처럼
- 아이콘 위에 잠시 나타나서 설명을 보여줍니다
- 손가락을 떼면 말풍선이 사라집니다

---

## 문제 상황: 아이콘 버튼의 기능을 알 수 없음

### 시나리오

문서 편집 앱을 처음 사용하는 사용자가 툴바를 봅니다.
저장, 공유, 서식, 설정 아이콘이 있지만...

### 잘못된 코드 예시

```kotlin
// Tooltip 없이 아이콘만 있는 버튼
Row {
    IconButton(onClick = { /* 저장 */ }) {
        Icon(Icons.Default.Save, contentDescription = null)  // 설명 없음!
    }
    IconButton(onClick = { /* 공유 */ }) {
        Icon(Icons.Default.Share, contentDescription = null)  // 설명 없음!
    }
    IconButton(onClick = { /* 설정 */ }) {
        Icon(Icons.Default.Settings, contentDescription = null)  // 설명 없음!
    }
}
```

### 발생하는 문제점

1. **사용자 혼란**: 아이콘만 보고는 정확한 기능을 알기 어려움
2. **학습 곡선 증가**: 새 사용자가 모든 버튼을 일일이 눌러봐야 함
3. **접근성 문제**: 스크린 리더 사용자가 버튼의 기능을 전혀 알 수 없음

---

## 해결책: Tooltip 사용

### 1. PlainTooltip - 간단한 설명

아이콘 버튼에 짧은 텍스트 설명을 추가합니다.

```kotlin
TooltipBox(
    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    tooltip = { PlainTooltip { Text("저장") } },
    state = rememberTooltipState()
) {
    IconButton(onClick = { /* 저장 */ }) {
        Icon(Icons.Default.Save, contentDescription = "저장")
    }
}
```

### 2. RichTooltip - 상세 설명

복잡한 기능에는 제목, 본문, 액션 버튼이 있는 상세 Tooltip을 사용합니다.

```kotlin
TooltipBox(
    positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
    tooltip = {
        RichTooltip(
            title = { Text("설정") },
            action = {
                TextButton(onClick = { /* 설정 열기 */ }) {
                    Text("설정 열기")
                }
            }
        ) {
            Text("글꼴, 여백, 테마 등을 변경할 수 있습니다")
        }
    },
    state = rememberTooltipState()
) {
    IconButton(onClick = { /* 설정 */ }) {
        Icon(Icons.Default.Settings, contentDescription = "설정")
    }
}
```

### 3. TooltipState - 프로그래밍 방식 제어

코드에서 Tooltip을 표시하거나 숨길 수 있습니다.

```kotlin
val tooltipState = rememberTooltipState()
val scope = rememberCoroutineScope()

// Tooltip 표시
scope.launch { tooltipState.show() }

// Tooltip 숨기기
scope.launch { tooltipState.dismiss() }
```

### 해결되는 이유

- 사용자가 버튼 기능을 즉시 이해
- 접근성 향상 (스크린 리더 지원)
- 앱 학습 곡선 완화

---

## TooltipBox 파라미터 설명

| 파라미터 | 설명 |
|---------|------|
| `positionProvider` | Tooltip이 표시될 위치를 정합니다 |
| `tooltip` | Tooltip 내용 (PlainTooltip 또는 RichTooltip) |
| `state` | Tooltip 상태 관리 |
| `content` | Tooltip이 붙을 대상 요소 (앵커) |

### 위치 제공자 (Position Provider)

- `TooltipDefaults.rememberPlainTooltipPositionProvider()`: PlainTooltip용
- `TooltipDefaults.rememberRichTooltipPositionProvider()`: RichTooltip용

---

## 트리거 방식

### 자동 트리거
- **길게 누르기** (터치 기기): 손가락으로 길게 누르면 표시
- **호버** (마우스): 마우스를 올리면 표시

### 수동 트리거
```kotlin
val tooltipState = rememberTooltipState()
val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch { tooltipState.show() }
}) {
    Text("Tooltip 표시")
}
```

---

## 사용 시나리오

1. **아이콘 버튼 설명**: 툴바, 내비게이션 등의 아이콘에 설명 추가
2. **복잡한 기능 안내**: RichTooltip으로 상세 정보 + 액션 제공
3. **앱 튜토리얼**: TooltipState로 첫 실행 시 자동 표시
4. **접근성 향상**: 시각적 요소에 텍스트 설명 제공

---

## 주의사항

- **남용 금지**: 너무 많은 Tooltip은 사용자를 피곤하게 함
- **짧고 명확하게**: PlainTooltip은 2-3단어로 간결하게
- **접근성 고려**: contentDescription도 함께 설정
- **RichTooltip 액션**: 필수 액션이 아닌 편의 기능에만 사용

---

## 연습 문제

### 연습 1: PlainTooltip 추가 (쉬움)
하트 아이콘에 "즐겨찾기에 추가" Tooltip을 추가하세요.

### 연습 2: RichTooltip 구현 (중간)
정보 아이콘에 제목, 본문, 액션 버튼이 있는 RichTooltip을 구현하세요.

### 연습 3: 이미지 편집 앱 툴바 (어려움)
5개의 도구 버튼에 적절한 Tooltip을 적용하세요:
- 단순 도구: PlainTooltip
- 복잡한 도구: RichTooltip
- 조정 도구: 첫 실행 시 자동 표시

---

## 다음 학습

- `Dialog`: 사용자와의 상호작용이 필요한 팝업
- `Snackbar`: 일시적인 메시지 표시
- `BottomSheet`: 하단에서 올라오는 추가 옵션

---

## 참고 자료

- [Android 공식 문서 - Tooltip](https://developer.android.com/develop/ui/compose/components/tooltip)
- Material Design 3 Tooltip 가이드라인
