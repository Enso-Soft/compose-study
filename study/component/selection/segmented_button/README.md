# Segmented Button 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Segmented Button은 **연결된 버튼 그룹**으로, 사용자가 여러 옵션 중에서 선택할 수 있는 UI 컴포넌트입니다.
라디오 버튼이나 체크박스의 현대적인 대안으로, Material Design 3에서 도입되었습니다.

## 핵심 특징

1. **SingleChoiceSegmentedButtonRow**: 하나만 선택 가능 (라디오 버튼 대체)
2. **MultiChoiceSegmentedButtonRow**: 여러 개 선택 가능 (체크박스 대체)
3. **자동 스타일링**: 모서리 처리, 테두리, 선택 상태 색상이 자동으로 적용됨

## 선수 학습

이 모듈을 학습하기 전에 다음 개념을 알고 있어야 합니다:
- `remember`와 `mutableStateOf` 기본 사용법
- 기본 Composable 함수 작성법

---

## 문제 상황: Row + Button으로 직접 구현의 어려움

### 시나리오

정렬 방식을 선택하는 탭 UI를 만들려고 합니다.
Row와 Button을 조합해서 직접 구현하면 어떤 문제가 발생할까요?

### 잘못된 코드 예시

```kotlin
// Row + Button으로 직접 구현 시도
Row {
    options.forEachIndexed { index, label ->
        // 문제 1: 모서리 처리가 복잡함
        val shape = when (index) {
            0 -> RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
            options.lastIndex -> RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
            else -> RectangleShape
        }

        // 문제 2: 선택 상태 색상 수동 처리
        val containerColor = if (isSelected)
            MaterialTheme.colorScheme.secondaryContainer
        else Color.Transparent

        // 문제 3: 테두리 겹침 해결
        val offsetX = if (index > 0) (-1).dp else 0.dp

        OutlinedButton(
            onClick = { selectedIndex = index },
            shape = shape,
            modifier = Modifier.offset(x = offsetX)
        ) {
            Text(label)
        }
    }
}
```

### 발생하는 문제점

1. **모서리 처리가 복잡함**: 첫 번째 버튼은 왼쪽만, 마지막 버튼은 오른쪽만, 중간은 직각으로 처리해야 함
2. **선택 상태 스타일링 수동 처리**: 배경색, 글자색 등을 모두 직접 지정해야 함
3. **테두리 겹침 문제**: 버튼 사이 테두리가 2중으로 보임, offset이나 zIndex로 수동 처리 필요
4. **접근성 미지원**: 스크린 리더가 라디오 버튼 그룹으로 인식하지 못함

---

## 해결책: SegmentedButton 사용

### SingleChoiceSegmentedButtonRow (단일 선택)

```kotlin
@Composable
fun SortingSelector() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("날짜순", "이름순", "크기순")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = { selectedIndex = index },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                )
            ) {
                Text(label)
            }
        }
    }
}
```

### MultiChoiceSegmentedButtonRow (다중 선택)

```kotlin
@Composable
fun FilterSelector() {
    val selectedOptions = remember { mutableStateListOf(false, false, false) }
    val options = listOf("인기", "최신", "할인")

    MultiChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                checked = selectedOptions[index],
                onCheckedChange = { selectedOptions[index] = it },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                icon = {
                    SegmentedButtonDefaults.Icon(active = selectedOptions[index])
                }
            ) {
                Text(label)
            }
        }
    }
}
```

### 해결되는 이유

- **itemShape()**: 버튼 위치에 따라 적절한 모서리 모양을 자동으로 적용
- **자동 스타일링**: selected/checked 상태에 따라 색상이 자동으로 변경
- **테두리 처리**: 내부적으로 테두리 겹침이 해결됨
- **접근성**: Material 3 컴포넌트로 접근성이 자동 지원됨

---

## API 상세

### SegmentedButton 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `selected` | Boolean | SingleChoice: 선택 여부 |
| `checked` | Boolean | MultiChoice: 체크 여부 |
| `onClick` | () -> Unit | SingleChoice: 클릭 콜백 |
| `onCheckedChange` | (Boolean) -> Unit | MultiChoice: 상태 변경 콜백 |
| `shape` | Shape | 버튼 모양 (itemShape 사용 권장) |
| `enabled` | Boolean | 활성화 여부 (기본값: true) |
| `icon` | @Composable (() -> Unit)? | 아이콘 (선택사항) |
| `content` | @Composable () -> Unit | 버튼 내용 (label) |

### SegmentedButtonDefaults

- `itemShape(index, count)`: 버튼 위치에 따른 모양 반환
- `Icon(active)`: 선택 상태에 따른 체크 아이콘
- `colors()`: 색상 커스터마이징
- `borderStroke()`: 테두리 스타일

---

## 사용 시나리오

### 1. SingleChoice를 사용할 때
- 정렬 방식 선택 (날짜순, 이름순, 크기순)
- 보기 모드 선택 (리스트, 그리드, 갤러리)
- 탭 네비게이션
- 옵션이 상호 배타적일 때

### 2. MultiChoice를 사용할 때
- 필터 선택 (인기, 최신, 할인)
- 카테고리 다중 선택
- 태그 선택
- 옵션을 조합할 수 있을 때

### 3. 아이콘만 사용할 때
- 공간이 좁을 때
- 시각적 표현이 명확할 때 (예: 리스트/그리드 아이콘)

---

## 주의사항

- **5개 이상 옵션**: Chips 컴포넌트 사용 권장
- **itemShape 필수**: 모서리 처리를 위해 반드시 `SegmentedButtonDefaults.itemShape()` 사용
- **상태 관리**: SingleChoice는 `mutableIntStateOf`, MultiChoice는 `mutableStateListOf` 권장

---

## 연습 문제

### 연습 1: 보기 모드 선택 - 쉬움

SingleChoiceSegmentedButtonRow를 사용하여 리스트/그리드/갤러리 보기 모드를 선택하는 UI를 구현하세요.
아이콘만 사용하고, 선택된 모드명을 하단에 표시하세요.

### 연습 2: 다중 필터 선택 - 중간

MultiChoiceSegmentedButtonRow를 사용하여 "인기", "최신", "할인" 필터를 다중 선택하는 UI를 구현하세요.
선택된 필터들을 쉼표로 구분하여 하단에 표시하세요.

### 연습 3: 설정 화면 - 어려움

테마 설정(단일), 알림 설정(다중), 언어 설정(단일)을 포함한 완전한 설정 화면을 구현하세요.
설정 저장 버튼 클릭 시 현재 상태를 Snackbar로 표시하세요.

---

## 다음 학습

- [Chips](../chip/README.md): 더 많은 옵션을 다룰 때
- [RadioButton](../../../component/selection/radio_button/README.md): 전통적인 라디오 버튼
- [Checkbox](../../../component/selection/checkbox/README.md): 전통적인 체크박스

---

## 참고 자료

- [공식 문서: Segmented Button](https://developer.android.com/develop/ui/compose/components/segmented-button)
- [Material Design 3: Segmented Buttons](https://m3.material.io/components/segmented-buttons/overview)
