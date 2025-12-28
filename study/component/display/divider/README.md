# Divider (HorizontalDivider / VerticalDivider) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | Text, Button 등 기본 UI 컴포넌트 사용법 | [📚 학습하기](../../../layout/basic_ui_components/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Divider는 콘텐츠를 시각적으로 구분하는 얇은 선입니다. Material 3에서는 `HorizontalDivider`(수평)와 `VerticalDivider`(수직)로 분리되어 제공됩니다.

> "구분선은 글에서 문단을 나누는 빈 줄과 같습니다. UI에서 섹션을 명확히 구분해줍니다."

## 핵심 특징

1. **간편한 사용**: 한 줄 코드로 깔끔한 구분선 추가
2. **쉬운 커스터마이징**: `thickness`, `color` 파라미터로 두께와 색상 조절
3. **Material Design 준수**: 테마 색상 자동 적용

---

## 문제 상황: Box로 직접 구분선 그리기

### 시나리오
설정 화면에서 "계정", "알림", "일반" 섹션을 구분하는 선을 추가하려고 합니다. 전용 컴포넌트를 모르는 상태에서 Box로 직접 구현을 시도합니다.

### 잘못된 코드 예시

```kotlin
// 수평 구분선을 Box로 직접 구현
@Composable
fun ManualHorizontalDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.LightGray)
    )
}

// 수직 구분선을 Box로 직접 구현 (더 복잡!)
@Composable
fun ManualVerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()  // 부모가 높이를 정해줘야 동작!
            .background(Color.LightGray)
    )
}

// 사용 예시
@Composable
fun SettingsWithManualDividers() {
    Column {
        SettingItem("계정")
        ManualHorizontalDivider()  // 매번 반복!
        SettingItem("알림")
        ManualHorizontalDivider()  // 또 반복!
        SettingItem("보안")
    }
}
```

### 발생하는 문제점

1. **코드 중복**: 매번 같은 Box 코드 반복
2. **두께 변경 어려움**: `height(1.dp)`를 모든 곳에서 수정해야 함
3. **색상 관리 어려움**: 테마 색상이 아닌 직접 지정, 다크 모드 대응 어려움
4. **수직 구분선 구현 복잡**: `fillMaxHeight()`는 부모가 높이를 정해줘야 동작
5. **Material Design 미준수**: 표준 divider 스타일과 다를 수 있음

---

## 해결책: HorizontalDivider / VerticalDivider 사용

### 기본 사용법

```kotlin
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider

// 수평 구분선 (Column 내에서 사용)
HorizontalDivider()

// 수직 구분선 (Row 내에서 사용)
VerticalDivider()
```

### 파라미터 커스터마이징

```kotlin
// 두께 조절
HorizontalDivider(thickness = 2.dp)

// 색상 변경
HorizontalDivider(color = MaterialTheme.colorScheme.outline)

// 두께 + 색상 조합
HorizontalDivider(
    thickness = 2.dp,
    color = MaterialTheme.colorScheme.primary
)
```

### Inset Divider (들여쓰기된 구분선)

```kotlin
// 왼쪽에 여백을 둔 구분선
HorizontalDivider(
    modifier = Modifier.padding(start = 16.dp)
)

// 양쪽에 여백
HorizontalDivider(
    modifier = Modifier.padding(horizontal = 16.dp)
)
```

### VerticalDivider 핵심 주의사항

VerticalDivider를 Row 안에서 사용할 때는 **IntrinsicSize.Min**이 필수입니다!

```kotlin
Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)  // 필수! 없으면 구분선이 안 보임
) {
    Text("왼쪽")
    VerticalDivider(color = MaterialTheme.colorScheme.secondary)
    Text("오른쪽")
}
```

> **왜 IntrinsicSize.Min이 필요한가?**
>
> Row에게 "내 안의 내용물 높이를 확인해서 그에 맞춰 높이를 정해!"라고 알려주는 것입니다.
> 없으면 Row가 VerticalDivider의 높이를 0으로 만들어버려서 보이지 않습니다.

### 해결되는 이유

1. **코드 간결**: 한 줄로 구분선 완성
2. **일관된 스타일**: Material Design 가이드라인 준수
3. **테마 자동 적용**: 다크 모드에서도 적절한 색상 자동 사용
4. **쉬운 커스터마이징**: 파라미터로 두께, 색상 즉시 변경

---

## API 레퍼런스

### HorizontalDivider

```kotlin
@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,      // 두께 (기본 1dp)
    color: Color = /* 테마 기본 색상 */
)
```

### VerticalDivider

```kotlin
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,      // 두께 (기본 1dp)
    color: Color = /* 테마 기본 색상 */
)
```

---

## 사용 시나리오

### 1. 리스트 아이템 구분

```kotlin
LazyColumn {
    itemsIndexed(items) { index, item ->
        ListItem(item)
        if (index < items.lastIndex) {
            HorizontalDivider()  // 마지막 아이템 제외
        }
    }
}
```

### 2. 설정 화면 섹션 구분

```kotlin
Column {
    // 계정 섹션
    SettingItem("프로필")
    SettingItem("비밀번호")

    HorizontalDivider(
        thickness = 2.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    // 알림 섹션
    SettingItem("알림 소리")
    SettingItem("진동")
}
```

### 3. Row 내 버튼 그룹 구분

```kotlin
Row(
    modifier = Modifier.height(IntrinsicSize.Min)
) {
    TextButton(onClick = {}) { Text("취소") }
    VerticalDivider()
    TextButton(onClick = {}) { Text("확인") }
}
```

---

## 주의사항

1. **VerticalDivider는 Row에 `height(IntrinsicSize.Min)` 필수**
   - 없으면 높이가 0이 되어 보이지 않음

2. **deprecated된 Divider 사용 금지**
   - Material 2의 `Divider`는 deprecated
   - Material 3의 `HorizontalDivider`, `VerticalDivider` 사용

3. **Inset Divider는 Modifier.padding 사용**
   - 시작점에 여백을 주려면 `padding(start = 16.dp)`

---

## 연습 문제

### 연습 1: 기본 구분선 추가하기 (쉬움)

프로필 카드 아래에 수평 구분선을 추가하세요.

**요구사항:**
- ProfileCard 아래에 HorizontalDivider 추가
- 기본 두께와 색상 사용

### 연습 2: Row에서 아이템 구분하기 (중간)

통계 대시보드에서 3개의 숫자 사이에 수직 구분선을 추가하세요.

**요구사항:**
- Row 안의 3개 통계 아이템 사이에 VerticalDivider 추가
- 구분선이 제대로 보이도록 설정 (힌트: IntrinsicSize 사용)
- 구분선 색상은 secondary 색상 사용

### 연습 3: 설정 화면 구분선 시스템 (어려움)

설정 앱에서 섹션별로 다른 스타일의 구분선을 적용하세요.

**요구사항:**
- 같은 섹션 내 아이템: Inset Divider (왼쪽 16dp 패딩)
- 섹션 간: Full-width Divider (두께 2dp, outline 색상)
- LazyColumn 사용
- 마지막 아이템 뒤에는 구분선 없음

---

## 다음 학습

- [Card](../../../component/display/card/README.md) - 카드 컴포넌트
- [LazyColumn/LazyRow](../../../list/lazy_layouts/README.md) - 효율적인 리스트 구현
- [Scaffold](../../../structure/scaffold_and_theming/README.md) - 화면 구조 잡기

---

## 참고 자료

- [Divider - Android Developers](https://developer.android.com/develop/ui/compose/components/divider)
- [Material Design 3 - Dividers](https://m3.material.io/components/divider)
