# Switch 컴포넌트 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `remember` | Composable에서 상태를 기억하고 유지하는 방법 | [📚 학습하기](../../../state/remember/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Switch는 설정의 ON/OFF 상태를 토글하는 Material Design 컴포넌트입니다.
집에 있는 조명 스위치처럼, 토글하면 즉시 효과가 적용됩니다.

## 핵심 특징

1. **즉시 적용**: 토글 즉시 설정이 반영됩니다 (Submit 버튼 불필요)
2. **시각적 명확성**: ON/OFF 상태가 직관적으로 표현됩니다
3. **thumbContent 지원**: Material 3에서 아이콘을 추가할 수 있습니다

---

## Switch vs Checkbox: 언제 무엇을 쓸까?

| 기준 | Switch | Checkbox |
|------|--------|----------|
| **효과 발생 시점** | 즉시 적용 | Submit 후 적용 |
| **사용 예시** | 다크모드, WiFi ON/OFF | 약관 동의, 옵션 선택 |
| **적합한 화면** | 설정 화면 | 폼/양식 화면 |
| **비유** | 조명 스위치 | 투표 용지 |

### 간단한 판단 기준

```
"이 설정을 바꾸면 바로 적용되나요?"
  ├── Yes → Switch 사용
  └── No (나중에 저장/제출) → Checkbox 사용
```

---

## 문제 상황: 설정 화면에서 Checkbox 사용

### 시나리오

신입 개발자가 앱 설정 화면을 만들었습니다.
다크모드, 알림 등의 ON/OFF 설정을 Checkbox로 구현했는데...

### 발생하는 문제점

1. **UX 혼란**: 사용자가 "저장" 버튼을 찾게 됩니다
2. **즉시 적용 불명확**: 체크해도 바로 적용되는지 알 수 없습니다
3. **시각적 피드백 부족**: ON/OFF 상태가 직관적이지 않습니다
4. **모바일 터치 어려움**: Checkbox는 터치 영역이 작습니다

---

## 해결책: Switch 사용

### 기본 사용법

```kotlin
@Composable
fun BasicSwitch() {
    var checked by remember { mutableStateOf(false) }

    Switch(
        checked = checked,
        onCheckedChange = { checked = it }
    )
}
```

### 레이블과 함께 사용

```kotlin
@Composable
fun LabeledSwitch() {
    var darkMode by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("다크모드")
        Switch(
            checked = darkMode,
            onCheckedChange = { darkMode = it }
        )
    }
}
```

### thumbContent로 아이콘 추가 (Material 3)

```kotlin
@Composable
fun SwitchWithIcon() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = { checked = it },
        thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        } else null
    )
}
```

### 색상 커스터마이징

```kotlin
@Composable
fun ColoredSwitch() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = { checked = it },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}
```

### enabled/disabled 상태

```kotlin
@Composable
fun DisabledSwitch() {
    Switch(
        checked = false,
        onCheckedChange = null,  // 또는 {}
        enabled = false
    )
}
```

---

## 핵심 API 파라미터

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `checked` | Boolean | 현재 ON/OFF 상태 |
| `onCheckedChange` | (Boolean) -> Unit | 상태 변경 콜백 |
| `enabled` | Boolean | 활성화 여부 (기본값: true) |
| `thumbContent` | @Composable (() -> Unit)? | 동그란 부분에 표시할 내용 |
| `colors` | SwitchColors | 색상 커스터마이징 |

---

## 실제 앱에서의 활용

### 설정 화면 패턴

```kotlin
@Composable
fun SettingItem(
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            thumbContent = if (checked) {
                { Icon(Icons.Filled.Check, null, Modifier.size(SwitchDefaults.IconSize)) }
            } else null
        )
    }
}
```

---

## 연습 문제

### 연습 1: 기본 Switch (쉬움)

비행기 모드 스위치를 구현하세요.
- 상태에 따라 "비행기 모드: ON" / "비행기 모드: OFF" 표시

### 연습 2: 아이콘이 있는 Switch (중간)

다크모드 스위치를 구현하세요.
- ON: 달 아이콘 표시
- OFF: 아이콘 없음 또는 해 아이콘

### 연습 3: 알림 설정 화면 (어려움)

다음 설정들을 구현하세요:
1. 푸시 알림
2. 이메일 알림
3. 마케팅 수신 동의

추가 요구사항:
- 마케팅 동의가 OFF면 "프로모션 알림" 스위치는 비활성화

---

## 다음 학습

- Checkbox: 폼에서 복수 선택이 필요할 때
- RadioButton: 단일 선택이 필요할 때
- 설정 화면 구성: Preference + DataStore
