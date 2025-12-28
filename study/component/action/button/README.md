# Button - Compose UI 구성요소

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

Button은 사용자가 탭하여 액션을 실행하는 가장 기본적인 상호작용 컴포넌트입니다. Material 3에서는 다양한 강조 수준과 용도에 맞는 7가지 버튼 유형을 제공합니다.

> "버튼의 외형은 그 중요도를 반영해야 합니다."
> 신호등처럼 - Filled(빨강, 멈춰!), Tonal(노랑, 주의), Text(초록, 괜찮아)

---

## Button 유형 비교표

| 유형 | 강조 수준 | 용도 | 예시 |
|------|----------|------|------|
| **Button (Filled)** | 최고 | 주요 액션 | 저장, 확인, 제출 |
| **FilledTonalButton** | 높음 | 보조 액션 | 초기화, 다음 |
| **ElevatedButton** | 중간 | 분리된 액션 | 내보내기, 공유 |
| **OutlinedButton** | 낮음 | 취소/대안 | 취소, 나중에 |
| **TextButton** | 최소 | 부가 액션 | 더보기, 건너뛰기 |
| **IconButton** | - | 공간 절약 | 설정, 좋아요 |
| **IconToggleButton** | - | 토글 상태 | 북마크, 음소거 |
| **FAB** | 특별 | 화면 대표 액션 | 새 항목 추가 |

---

## 기본 사용법

### 1. Button (Filled) - 주요 액션
```kotlin
Button(onClick = { /* 처리 로직 */ }) {
    Text("저장")
}
```

### 2. FilledTonalButton - 보조 액션
```kotlin
FilledTonalButton(onClick = { /* 처리 로직 */ }) {
    Text("초기화")
}
```

### 3. ElevatedButton - 그림자 효과
```kotlin
ElevatedButton(onClick = { /* 처리 로직 */ }) {
    Text("내보내기")
}
```

### 4. OutlinedButton - 테두리만
```kotlin
OutlinedButton(onClick = { /* 처리 로직 */ }) {
    Text("취소")
}
```

### 5. TextButton - 텍스트만
```kotlin
TextButton(onClick = { /* 처리 로직 */ }) {
    Text("더보기")
}
```

### 6. IconButton - 아이콘만
```kotlin
IconButton(onClick = { /* 처리 로직 */ }) {
    Icon(
        Icons.Filled.Settings,
        contentDescription = "설정" // 접근성 필수!
    )
}
```

### 7. IconToggleButton - 토글 상태
```kotlin
var isBookmarked by remember { mutableStateOf(false) }

IconToggleButton(
    checked = isBookmarked,
    onCheckedChange = { isBookmarked = it }
) {
    Icon(
        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
        contentDescription = if (isBookmarked) "북마크 해제" else "북마크 추가"
    )
}
```

### 8. FloatingActionButton - 화면 대표 액션
```kotlin
FloatingActionButton(onClick = { /* 처리 로직 */ }) {
    Icon(Icons.Filled.Add, contentDescription = "추가")
}

// 확장 FAB
ExtendedFloatingActionButton(
    onClick = { /* 처리 로직 */ },
    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
    text = { Text("작성") }
)
```

---

## 언제 어떤 Button을 선택할까?

```
화면에서 가장 중요한 액션인가?
    └── Yes → Button (Filled) 또는 FAB
    └── No → 보조 액션인가?
              └── Yes → FilledTonalButton
              └── No → 취소/대안 액션인가?
                        └── Yes → OutlinedButton
                        └── No → TextButton
```

### 실무 가이드

| 상황 | 권장 버튼 |
|------|----------|
| 폼 제출 (저장, 확인) | Button (Filled) |
| 폼 취소 | OutlinedButton |
| 다음/이전 네비게이션 | FilledTonalButton |
| "더보기", "자세히" 링크 | TextButton |
| 툴바 아이콘 | IconButton |
| 새 항목 추가 (화면 우하단) | FAB |

---

## 고급 활용

### 커스텀 색상
```kotlin
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF1DA1F2), // 버튼 배경색
        contentColor = Color.White          // 텍스트/아이콘 색상
    )
) {
    Text("Twitter 스타일")
}
```

### 커스텀 모양
```kotlin
Button(
    onClick = { },
    shape = RoundedCornerShape(50) // Pill 모양
) {
    Text("둥근 버튼")
}
```

### 로딩 상태 버튼
```kotlin
var isLoading by remember { mutableStateOf(false) }

Button(
    onClick = { isLoading = true },
    enabled = !isLoading // 로딩 중 비활성화
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp
        )
        Spacer(Modifier.width(8.dp))
        Text("처리 중...")
    } else {
        Text("제출하기")
    }
}
```

### 아이콘 + 텍스트 조합
```kotlin
Button(onClick = { }) {
    Icon(Icons.Filled.ShoppingCart, contentDescription = null)
    Spacer(Modifier.width(8.dp))
    Text("장바구니 담기")
}
```

### 전체 너비 버튼
```kotlin
Button(
    onClick = { },
    modifier = Modifier.fillMaxWidth()
) {
    Text("결제하기")
}
```

### 비활성화 상태
```kotlin
Button(
    onClick = { },
    enabled = isFormValid // 조건에 따라 활성화
) {
    Text("가입하기")
}
```

---

## 접근성 (Accessibility)

### IconButton에는 반드시 contentDescription 제공
```kotlin
// 좋은 예
IconButton(onClick = { }) {
    Icon(
        Icons.Filled.Favorite,
        contentDescription = "좋아요 추가" // TalkBack이 읽어줌
    )
}

// 나쁜 예
IconButton(onClick = { }) {
    Icon(
        Icons.Filled.Favorite,
        contentDescription = null // TalkBack 사용자가 이해 불가!
    )
}
```

### 터치 영역 확보
Material 3 Button은 기본적으로 48dp 이상의 터치 영역을 확보합니다. 커스텀 버튼을 만들 때도 이 규칙을 지켜야 합니다.

---

## Button API 주요 파라미터

```kotlin
@Composable
fun Button(
    onClick: () -> Unit,                    // 클릭 시 실행되는 코드
    modifier: Modifier = Modifier,          // 크기, 패딩 등 조절
    enabled: Boolean = true,                // 활성화 여부
    shape: Shape = ButtonDefaults.shape,    // 버튼 모양
    colors: ButtonColors = ButtonDefaults.buttonColors(), // 색상
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(), // 그림자
    border: BorderStroke? = null,           // 테두리 (OutlinedButton에서 사용)
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding, // 내부 패딩
    content: @Composable RowScope.() -> Unit // 버튼 내용 (가로 배치)
)
```

---

## 베스트 프랙티스

1. **강조 수준 매칭**: 버튼의 시각적 강조는 액션의 중요도와 일치해야 합니다.

2. **한 화면에 Filled Button은 1-2개**: 너무 많으면 어떤 것이 중요한지 알 수 없습니다.

3. **로딩 중 비활성화**: 중복 클릭 방지를 위해 `enabled = !isLoading` 패턴을 사용합니다.

4. **IconButton에는 contentDescription 필수**: 접근성을 위해 반드시 제공합니다.

5. **커스터마이징은 ButtonDefaults 활용**: `buttonColors()`, `buttonElevation()` 등으로 일관된 스타일 유지.

6. **48dp 최소 터치 영역**: Material 3 Button은 기본적으로 이를 보장하지만, 커스텀 버튼 제작 시 반드시 준수해야 합니다.

---

## 안티패턴

### 1. Filled Button 과다 사용
```kotlin
// 나쁜 예: 모든 버튼이 Filled
Column {
    Button(onClick = { }) { Text("저장") }
    Button(onClick = { }) { Text("취소") }  // OutlinedButton 사용해야 함
    Button(onClick = { }) { Text("삭제") }  // 위험 액션은 다른 색상 필요
}
```
> 한 화면에 Filled Button이 3개 이상이면 어떤 것이 주요 액션인지 구분할 수 없습니다.

### 2. IconButton의 contentDescription 누락
```kotlin
// 나쁜 예
IconButton(onClick = { }) {
    Icon(Icons.Filled.Delete, contentDescription = null) // TalkBack 사용 불가!
}

// 좋은 예
IconButton(onClick = { }) {
    Icon(Icons.Filled.Delete, contentDescription = "항목 삭제")
}
```

### 3. 터치 영역 부족
```kotlin
// 나쁜 예: 너무 작은 버튼
IconButton(
    onClick = { },
    modifier = Modifier.size(24.dp) // 48dp 미만!
) {
    Icon(Icons.Filled.Close, contentDescription = "닫기")
}
```
> 접근성 가이드라인에 따라 최소 48dp의 터치 영역을 확보해야 합니다.

### 4. 로딩 중 버튼 활성화 유지
```kotlin
// 나쁜 예: 중복 클릭 가능
Button(onClick = { startLoading() }) { // enabled 체크 없음!
    if (isLoading) CircularProgressIndicator() else Text("제출")
}

// 좋은 예
Button(
    onClick = { startLoading() },
    enabled = !isLoading // 로딩 중 비활성화
) {
    if (isLoading) CircularProgressIndicator() else Text("제출")
}

---

## 연습 문제

Practice 탭에서 3단계 난이도의 연습 문제를 풀어보세요:

1. **쉬움**: 기본 버튼 5종 만들기
2. **중간**: 로딩 상태가 있는 제출 버튼 구현
3. **어려움**: 재사용 가능한 커스텀 버튼 시스템 구축

---

## 다음 학습

Button을 마스터했다면 다음 주제로 넘어가세요:

| 주제 | 설명 |
|------|------|
| **IconButton 심화** | FilledIconButton, OutlinedIconButton, IconToggleButton 등 다양한 아이콘 버튼 |
| **FAB 패턴** | FAB 위치, 스크롤 시 숨기기/보이기, 애니메이션 |
| **Interaction 처리** | InteractionSource로 눌림, 포커스, 호버 상태 관찰 |
| **커스텀 디자인 시스템** | 앱 전용 버튼 시스템 구축 (Practice 3번 심화) |

---

## 참고 자료

- [Button | Android Developers](https://developer.android.com/develop/ui/compose/components/button)
- [Material Design 3 - Buttons](https://m3.material.io/components/buttons/overview)
- [Compose Material 3 API](https://developer.android.com/jetpack/androidx/releases/compose-material3)
