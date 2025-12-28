# Chip 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념
Chip은 사용자가 정보를 입력하거나, 선택하거나, 콘텐츠를 필터링하거나, 액션을 수행할 수 있게 해주는 **작고 컴팩트한 UI 요소**입니다.

Material Design 3에서는 4가지 유형의 Chip을 제공합니다.

## 핵심 특징
1. **4가지 전문 유형**: 용도별로 최적화된 AssistChip, FilterChip, InputChip, SuggestionChip
2. **내장된 상태 관리**: FilterChip의 선택 상태, InputChip의 삭제 기능 등이 기본 제공
3. **Material 3 디자인 준수**: 일관된 스타일, 애니메이션, 접근성 자동 지원

---

## 4가지 Chip 유형 한눈에 보기

Chip 유형을 일상 생활에 비유하면:

- **AssistChip (도우미 칩)**: 스마트폰의 "빠른 설정" 버튼
  - 예: "Wi-Fi 켜기", "캘린더에 추가" - 클릭하면 바로 동작

- **FilterChip (필터 칩)**: 쇼핑몰의 "필터" 버튼
  - 예: "무료배송만", "할인중" - 선택하면 체크 표시, 다시 누르면 해제

- **InputChip (입력 칩)**: 이메일의 "받는 사람" 태그
  - 예: "홍길동 <hong@mail.com>" - X 버튼으로 삭제 가능

- **SuggestionChip (제안 칩)**: AI 챗봇의 "추천 질문"
  - 예: "오늘 날씨 알려줘" - 클릭하면 해당 질문 전송

### 비교표

| 유형 | 용도 | 선택 상태 | 삭제 가능 | 대표 예시 |
|------|------|----------|----------|----------|
| AssistChip | 스마트 액션 실행 | X | X | 캘린더 추가, 알람 설정 |
| FilterChip | 필터링/선택 | O | X | 카테고리 필터, 태그 선택 |
| InputChip | 사용자 입력 표시 | O | O | 이메일 수신자, 검색 태그 |
| SuggestionChip | 동적 제안 | X | X | AI 추천, 자동완성 |

---

## 사전 지식
이 모듈을 학습하기 전에 다음 개념을 알아야 합니다:
- `remember` / `mutableStateOf` (상태 관리 기초)
- Composable 함수 기본 개념
- Icon, Text 컴포넌트 사용법

---

## 용어 정리

| 용어 | 의미 |
|------|------|
| `selected` | 선택 상태 - 칩이 현재 선택되어 있는지 나타내는 Boolean 값 |
| `leadingIcon` | 왼쪽 아이콘 - 텍스트 왼쪽에 표시되는 아이콘 |
| `trailingIcon` | 오른쪽 아이콘 - 텍스트 오른쪽에 표시되는 아이콘 (보통 삭제 버튼) |
| `avatar` | 아바타 - 프로필 사진처럼 둥근 형태의 이미지/아이콘 |
| `FlowRow` | 자동 줄바꿈 가로 배치 - 공간이 부족하면 다음 줄로 넘어감 |

---

## 문제 상황: 직접 구현의 어려움

### 시나리오
쇼핑몰 앱에서 상품 필터링을 위한 태그 UI를 만들려고 합니다.
"할인중", "무료배송", "당일배송" 같은 필터를 선택 가능하게 만들어야 합니다.

### Surface + Text로 직접 구현하면?

```kotlin
// 직접 구현한 커스텀 태그 (약 35줄)
@Composable
fun CustomTag(
    text: String,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier.clickable { onSelectionChange(!isSelected) },
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text)
            if (onDelete != null) {
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Close, "삭제")
                }
            }
        }
    }
}
```

### 발생하는 문제점
1. **코드가 길고 복잡함**: 간단한 태그 하나에 30줄 이상
2. **스타일 수동 관리**: 배경색, 아이콘 크기, 패딩 직접 계산
3. **Material 3 스펙 미준수**: 표준 ripple 효과, 애니메이션 없음
4. **접근성 누락 가능**: contentDescription 등 직접 추가 필요
5. **유지보수 어려움**: 여러 화면에서 같은 코드 반복

---

## 해결책: Chip 컴포넌트 사용

### 1. FilterChip - 필터링 UI

```kotlin
// 단 5줄로 동일한 기능 구현!
var selected by remember { mutableStateOf(false) }

FilterChip(
    onClick = { selected = !selected },
    label = { Text("무료배송") },
    selected = selected,
    leadingIcon = if (selected) {
        { Icon(Icons.Default.Done, null, Modifier.size(FilterChipDefaults.IconSize)) }
    } else null
)
```

**장점:**
- `selected` 파라미터만으로 선택 상태 관리
- 자동으로 체크마크, 배경색 변경
- Material 3 스펙 자동 준수

### 2. InputChip - 삭제 가능한 태그

```kotlin
InputChip(
    onClick = { /* 상세보기 */ },
    label = { Text("검색어") },
    selected = true,
    avatar = {
        Icon(Icons.Default.Search, null, Modifier.size(InputChipDefaults.AvatarSize))
    },
    trailingIcon = {
        Icon(Icons.Default.Close, "삭제", Modifier.size(InputChipDefaults.AvatarSize))
    }
)
```

**장점:**
- 아바타, 삭제 버튼 내장
- 일관된 크기와 스타일

### 3. AssistChip - 스마트 액션

```kotlin
AssistChip(
    onClick = { /* 캘린더 추가 */ },
    label = { Text("캘린더에 추가") },
    leadingIcon = {
        Icon(Icons.Default.Event, null, Modifier.size(AssistChipDefaults.IconSize))
    }
)
```

### 4. SuggestionChip - 동적 제안

```kotlin
SuggestionChip(
    onClick = { /* 제안 선택 */ },
    label = { Text("오늘 날씨 알려줘") }
)
```

---

## FlowRow + Chip 조합

태그가 많을 때 자동으로 줄바꿈되는 레이아웃:

```kotlin
val categories = listOf("할인중", "무료배송", "당일배송", "베스트", "신상품")
var selectedCategories by remember { mutableStateOf(setOf<String>()) }

FlowRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    categories.forEach { category ->
        val isSelected = category in selectedCategories
        FilterChip(
            onClick = {
                selectedCategories = if (isSelected) {
                    selectedCategories - category
                } else {
                    selectedCategories + category
                }
            },
            label = { Text(category) },
            selected = isSelected,
            leadingIcon = if (isSelected) {
                { Icon(Icons.Default.Done, null) }
            } else null
        )
    }
}
```

---

## 주의사항

1. **ElevatedInputChip은 없다**: AssistChip, FilterChip, SuggestionChip만 Elevated 버전 제공
2. **IconSize 사용**: 각 Chip 유형별로 `XxxChipDefaults.IconSize` 사용 권장
3. **상태 호이스팅**: 실제 앱에서는 selected 상태를 ViewModel로 관리

---

## 연습 문제

### 연습 1: 기본 FilterChip (쉬움)
"알림 받기" FilterChip 하나를 구현하세요.
- 선택 시 체크마크 표시
- 선택 상태 토글 기능

### 연습 2: InputChip 태그 삭제 (중간)
태그 목록 ["Kotlin", "Android", "Compose"]를 InputChip으로 표시하고,
X 버튼 클릭 시 해당 태그가 삭제되도록 구현하세요.

### 연습 3: 카테고리 필터 시스템 (어려움)
다중 선택 가능한 카테고리 필터를 구현하세요.
- 카테고리: "전자기기", "의류", "식품", "가구", "도서", "스포츠"
- FlowRow로 자동 줄바꿈
- 선택된 카테고리 개수 표시
- "선택 초기화" 버튼

---

## 다음 학습
- FlowRow/FlowColumn 레이아웃
- Chip 커스터마이징 (ChipColors, ChipElevation)
- 필터 시스템 아키텍처 설계
