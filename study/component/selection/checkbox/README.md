# Checkbox 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Checkbox는 사용자가 목록에서 하나 이상의 항목을 선택하거나 해제할 수 있게 하는 UI 컴포넌트입니다.
종이 설문지의 체크박스와 같은 역할을 하며, 폼 UI에서 가장 기본적인 선택 컴포넌트입니다.

## 핵심 특징

1. **상태 기반 UI**: `checked` 상태에 따라 체크 표시가 자동으로 변경됩니다
2. **콜백 패턴**: `onCheckedChange`를 통해 사용자 입력을 처리합니다
3. **3가지 상태 지원**: TriStateCheckbox로 체크/해제/불확정 상태를 표현할 수 있습니다

## 사전 지식

- Compose 기본 (`@Composable` 함수)
- `remember`와 `mutableStateOf` 사용법
- 클릭 이벤트 처리

---

## 문제 상황: 직접 체크박스를 구현하면?

### 시나리오
쇼핑 카트에서 여러 상품을 선택하는 UI를 만들려고 합니다.
Box와 Icon으로 직접 체크박스를 구현해봅니다.

### 잘못된 코드 예시

```kotlin
// 직접 구현한 체크박스 - 문제가 많음!
@Composable
fun CustomCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(Icons.Default.Check, contentDescription = null)
        }
    }
}

// 각 항목마다 개별 상태 변수 - 확장성 없음!
var item1Checked by remember { mutableStateOf(false) }
var item2Checked by remember { mutableStateOf(false) }
var item3Checked by remember { mutableStateOf(false) }
```

### 발생하는 문제점

1. **상태 관리 복잡성**: 항목이 늘어날수록 상태 변수도 늘어남
2. **전체 선택 로직 복잡**: 모든 항목 상태를 일일이 확인해야 함
3. **Material Design 미준수**: 표준 디자인과 다른 모양
4. **접근성 문제**: 스크린 리더가 체크박스로 인식 못함
5. **코드 중복**: 각 항목마다 유사한 코드 반복

---

## 해결책: Checkbox 컴포넌트 사용

### 올바른 코드

```kotlin
// Material Checkbox 사용 - 깔끔하고 접근성 지원!
val items = listOf("상품 A", "상품 B", "상품 C")
val checkedStates = remember { mutableStateListOf(false, false, false) }

items.forEachIndexed { index, item ->
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checkedStates[index],
            onCheckedChange = { checkedStates[index] = it }
        )
        Text(item)
    }
}
```

### 해결되는 이유

- `mutableStateListOf`로 여러 상태를 효율적으로 관리
- Material Design 가이드라인 자동 준수
- 접근성 자동 지원 (스크린 리더 호환)
- 애니메이션, 터치 피드백 등 자동 제공

---

## 기본 사용법

### Checkbox

```kotlin
@Composable
fun BasicCheckboxExample() {
    // 1. 체크 상태를 저장할 변수
    var isChecked by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        // 2. Checkbox 컴포넌트
        Checkbox(
            checked = isChecked,                    // 현재 체크 상태
            onCheckedChange = { isChecked = it }    // 상태 변경 시 호출
        )
        Text("동의합니다")
    }
}
```

### 주요 파라미터

| 파라미터 | 타입 | 설명 |
|---------|------|------|
| `checked` | Boolean | 체크 상태 (true/false) |
| `onCheckedChange` | (Boolean) -> Unit | 상태 변경 콜백 |
| `enabled` | Boolean | 활성화 여부 (기본값: true) |
| `colors` | CheckboxColors | 색상 커스터마이징 |

---

## 레이블과 함께 사용하기 (접근성)

```kotlin
// 권장 패턴: Row 전체를 클릭 가능하게
Row(
    modifier = Modifier
        .fillMaxWidth()
        .toggleable(
            value = isChecked,
            onValueChange = { isChecked = it },
            role = Role.Checkbox    // 스크린 리더용
        )
        .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Checkbox(
        checked = isChecked,
        onCheckedChange = null  // Row에서 처리하므로 null
    )
    Spacer(Modifier.width(8.dp))
    Text("이용약관에 동의합니다")
}
```

---

## 고급 활용: TriStateCheckbox

### ToggleableState란?

3가지 상태를 표현하는 enum입니다:
- `ToggleableState.On`: 체크됨 (모두 선택)
- `ToggleableState.Off`: 해제됨 (모두 해제)
- `ToggleableState.Indeterminate`: 불확정 (일부만 선택) - 대시(-) 표시

### 전체 선택 구현

```kotlin
@Composable
fun SelectAllExample() {
    val items = listOf("옵션 1", "옵션 2", "옵션 3")
    val checkedStates = remember { mutableStateListOf(false, false, false) }

    // 부모 상태 자동 계산
    val parentState = when {
        checkedStates.all { it } -> ToggleableState.On
        checkedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    Column {
        // 전체 선택 체크박스
        Row(verticalAlignment = Alignment.CenterVertically) {
            TriStateCheckbox(
                state = parentState,
                onClick = {
                    // On이 아니면 모두 선택, On이면 모두 해제
                    val newState = parentState != ToggleableState.On
                    checkedStates.indices.forEach { checkedStates[it] = newState }
                }
            )
            Text("전체 선택")
        }

        // 개별 항목
        items.forEachIndexed { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedStates[index],
                    onCheckedChange = { checkedStates[index] = it }
                )
                Text(item)
            }
        }
    }
}
```

---

## 색상 커스터마이징

```kotlin
Checkbox(
    checked = isChecked,
    onCheckedChange = { isChecked = it },
    colors = CheckboxDefaults.colors(
        checkedColor = Color.Green,           // 체크됐을 때 배경색
        uncheckedColor = Color.Gray,          // 체크 안됐을 때 테두리색
        checkmarkColor = Color.White,         // 체크마크 색상
        disabledCheckedColor = Color.LightGray,
        disabledUncheckedColor = Color.LightGray
    )
)
```

---

## 사용 시나리오

1. **이용약관 동의**: 회원가입 시 필수 동의 체크
2. **할 일 목록**: ToDo 앱에서 완료 표시
3. **쇼핑 카트**: 여러 상품 선택 및 전체 선택
4. **설정 화면**: 옵션 켜기/끄기
5. **필터 UI**: 여러 조건 중 선택

---

## 주의사항

- `onCheckedChange`에 null을 전달하면 클릭해도 상태가 변경되지 않습니다
- 여러 체크박스 상태 관리 시 `mutableStateListOf` 또는 `mutableStateMapOf` 사용을 권장합니다
- 레이블 텍스트 클릭으로도 체크되게 하려면 `toggleable` modifier를 사용하세요
- 스위치(Switch)와 다른 점: 체크박스는 다중 선택, 스위치는 단일 on/off에 적합

---

## 연습 문제

### 연습 1: 이용약관 동의 - 쉬움

이용약관 동의 체크박스를 구현하세요.
- "이용약관에 동의합니다" 텍스트와 체크박스
- 체크해야만 "가입하기" 버튼이 활성화됨

**힌트**: `Button(enabled = isAgreed)`

### 연습 2: 할 일 목록 - 중간

할 일 목록을 구현하세요.
- 5개의 할 일 항목 표시
- 완료된 항목은 취소선 표시
- 상단에 "3/5 완료" 형식으로 진행률 표시
- 완료된 항목은 초록색 체크박스

**힌트**: `mutableStateListOf`, `TextDecoration.LineThrough`, `CheckboxDefaults.colors()`

### 연습 3: 쇼핑 카트 전체 선택 - 어려움

쇼핑 카트 UI를 구현하세요.
- 상품 목록 (이름, 가격)
- 각 상품에 체크박스
- "전체 선택" TriStateCheckbox
- 선택된 상품의 총 가격 표시
- 선택된 항목 없으면 "주문하기" 버튼 비활성화

**힌트**: `TriStateCheckbox`, `ToggleableState`, `sumOf`

---

## 다음 학습

- **Switch**: 단일 on/off 토글에 적합한 컴포넌트
- **RadioButton**: 여러 옵션 중 하나만 선택
- **Slider**: 범위 값 선택
