# RadioButton 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**RadioButton**은 여러 옵션 중에서 **하나만** 선택할 때 사용하는 UI 컴포넌트입니다.

> 옛날 라디오의 채널 버튼을 떠올려보세요:
> - 버튼 하나를 누르면 기존에 눌린 버튼이 튀어나옵니다
> - 동시에 두 개를 누를 수 없습니다
> - 이렇게 "하나만 선택 가능"한 UI가 RadioButton입니다

### 언제 사용하나요?

| 사용 O | 사용 X |
|--------|--------|
| 결제 수단 선택 (카드/계좌이체/간편결제) | 토핑 선택 (여러 개 가능) |
| 배송 옵션 (일반/빠른/새벽) | 알림 설정 (개별 ON/OFF) |
| 언어 선택 | 관심사 선택 |
| 성별 선택 | 약관 동의 (개별 항목) |

---

## 기본 사용법

### 가장 간단한 RadioButton

```kotlin
@Composable
fun SimpleRadioButton() {
    var isSelected by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = isSelected,       // 선택 여부
            onClick = { isSelected = true }  // 클릭 시 동작
        )
        Text("옵션")
    }
}
```

### 핵심 파라미터

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `selected` | Boolean | 이 버튼이 선택되었는지 여부 |
| `onClick` | (() -> Unit)? | 클릭 시 실행할 동작 (null 가능) |
| `enabled` | Boolean | 활성화 여부 (기본값: true) |
| `interactionSource` | MutableInteractionSource? | 상호작용 상태 관찰 |

---

## 핵심 기능

### 기능 1: RadioButton 그룹

여러 옵션 중 하나를 선택하는 가장 일반적인 패턴입니다.

```kotlin
@Composable
fun RadioGroupDemo() {
    val options = listOf("한국어", "English", "日本語")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selectedOption),  // 현재 옵션이 선택된 것인지 비교
                    onClick = { selectedOption = option }   // 클릭 시 이 옵션을 선택
                )
                Text(option)
            }
        }
    }
}
```

**핵심 포인트**:
- `remember { mutableStateOf(...) }`로 선택 상태 저장
- `forEach`로 각 옵션에 대해 RadioButton 생성
- `selected = (option == selectedOption)`으로 현재 선택 여부 판단

### 기능 2: 레이블과 함께 사용

RadioButton만 클릭 가능하면 터치 영역이 작습니다. Row 전체를 클릭 가능하게 만들면 사용성이 향상됩니다.

```kotlin
@Composable
fun LabeledRadioButton() {
    val options = listOf("소리", "진동", "무음")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedOption = option }  // Row 전체 클릭 가능
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null  // Row가 클릭을 처리하므로 null
                )
                Text(option)
            }
        }
    }
}
```

**핵심 포인트**:
- `RadioButton`의 `onClick = null`로 설정
- `Row`에 `Modifier.clickable { ... }` 추가

### 기능 3: enabled/disabled 상태

특정 조건에서 옵션을 비활성화할 수 있습니다.

```kotlin
@Composable
fun EnabledDisabledDemo() {
    val options = listOf(
        "무료 플랜" to true,
        "프리미엄 플랜 (준비 중)" to false  // 비활성화
    )
    var selectedOption by remember { mutableStateOf("무료 플랜") }

    Column {
        options.forEach { (option, enabled) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { if (enabled) selectedOption = option },
                    enabled = enabled  // 활성화 여부
                )
                Text(
                    text = option,
                    color = if (enabled) Color.Unspecified else Color.Gray
                )
            }
        }
    }
}
```

---

## 고급 활용

### 패턴 1: 접근성 최적화 (권장 패턴)

스크린 리더(TalkBack)가 RadioButton 그룹을 올바르게 인식하도록 합니다.

```kotlin
@Composable
fun AccessibleRadioGroup() {
    val options = listOf("신용카드", "계좌이체", "간편결제")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column(Modifier.selectableGroup()) {  // 1. 그룹으로 묶음
        options.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)  // 터치 영역 확보
                    .selectable(  // 2. 선택 가능하게 설정
                        selected = (option == selectedOption),
                        onClick = { selectedOption = option },
                        role = Role.RadioButton  // 3. 역할 명시
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null  // 4. Row가 처리하므로 null
                )
                Text(option, Modifier.padding(start = 16.dp))
            }
        }
    }
}
```

**왜 이렇게 해야 하나요?**

| 요소 | 역할 |
|------|------|
| `Modifier.selectableGroup()` | 스크린 리더에게 "이 버튼들은 한 그룹"임을 알림 |
| `Modifier.selectable()` | 전체 Row를 하나의 선택 가능한 항목으로 만듦 |
| `role = Role.RadioButton` | 접근성 서비스에게 "이건 라디오 버튼"임을 알림 |
| `onClick = null` | Row가 클릭을 처리하므로 중복 방지 |

### 패턴 2: 카드 스타일 RadioButton

시각적으로 눈에 띄는 선택 UI를 만듭니다.

```kotlin
@Composable
fun CardStyleRadio() {
    data class Plan(val name: String, val price: String)
    val plans = listOf(
        Plan("Basic", "9,000원/월"),
        Plan("Pro", "19,000원/월")
    )
    var selectedPlan by remember { mutableStateOf(plans[0]) }

    Column(Modifier.selectableGroup()) {
        plans.forEach { plan ->
            val isSelected = plan == selectedPlan

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = { selectedPlan = plan }
                    ),
                border = if (isSelected)
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                else null  // 선택된 카드에만 테두리
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    RadioButton(selected = isSelected, onClick = null)
                    Column {
                        Text(plan.name, fontWeight = FontWeight.Bold)
                        Text(plan.price)
                    }
                }
            }
        }
    }
}
```

---

## RadioButton vs Checkbox 비교

| 기준 | RadioButton | Checkbox |
|------|-------------|----------|
| **선택 방식** | 단일 선택 (하나만) | 다중 선택 (여러 개) |
| **상태** | 상호 배타적 | 독립적 ON/OFF |
| **사용 사례** | 결제 수단, 크기, 언어 | 토핑, 필터, 약관 동의 |

### 피자 주문으로 이해하기

```
피자 주문
├── 크러스트 선택 → RadioButton (하나만!)
│   ├── ○ 씬
│   ├── ● 오리지널 ← 선택됨
│   └── ○ 치즈크러스트
│
└── 토핑 선택 → Checkbox (여러 개 OK)
    ├── ☑ 버섯
    ├── ☑ 피망
    └── ☐ 페퍼로니
```

---

## 베스트 프랙티스

1. **`Modifier.selectableGroup()`을 Column에 적용하세요** (접근성)
2. **RadioButton의 `onClick = null`로 설정하고, Row가 클릭을 처리하게 하세요**
3. **Row 높이를 최소 48-56dp로 유지하세요** (터치 영역)
4. **항상 Text 레이블과 함께 사용하세요**

## 안티패턴

1. **다중 선택이 필요한 곳에 RadioButton 사용** → Checkbox 사용하세요
2. **레이블 없이 RadioButton만 사용** → 의미 전달 불가
3. **각 RadioButton에 독립적인 onClick 처리** → 그룹으로 관리하세요

---

## 연습 문제

### 연습 1: 성별 선택 (쉬움)

**요구사항**:
- "남성", "여성", "선택 안 함" 세 가지 옵션
- 기본 RadioButton 그룹 구현
- 선택된 옵션을 Text로 표시

### 연습 2: 결제 수단 선택 (중간)

**요구사항**:
- "신용카드", "계좌이체", "카카오페이", "네이버페이" 옵션
- 접근성 최적화 패턴 적용
- 선택된 결제 수단으로 "결제하기" 버튼 텍스트 변경

### 연습 3: 배송 옵션 선택 (어려움)

**요구사항**:
- 배송 옵션 3가지 (일반/빠른/새벽)
- 새벽배송은 "수도권 거주" 체크 시에만 활성화
- 선택된 옵션에 따라 배송비 표시
- Card 스타일 + 선택된 카드 하이라이트

---

## 다음 학습

- [Checkbox](../../../component/selection/checkbox/README.md) - 다중 선택 UI
- [Switch](../switch_component/README.md) - ON/OFF 토글
- [Slider](../../../component/input/slider/README.md) - 범위 값 선택

---

## 참고 자료

- [Android 공식 문서 - RadioButton](https://developer.android.com/develop/ui/compose/components/radio-button)
- [Material Design 3 - Radio Button](https://m3.material.io/components/radio-button/overview)
