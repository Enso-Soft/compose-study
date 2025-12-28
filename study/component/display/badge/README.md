# Badge 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | Text, Button 등 기본 UI 컴포넌트 사용법 | [📚 학습하기](../../../layout/basic_ui_components/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Badge는 알림 개수나 새 항목을 표시하기 위한 Material 3 컴포넌트입니다. 앱스토어의 업데이트 알림, 카카오톡의 읽지 않은 메시지 표시, 장바구니의 상품 개수 등 일상에서 자주 보는 UI 요소입니다.

## 핵심 특징

1. **BadgedBox**: Badge를 아이콘 위에 자동으로 배치해주는 컨테이너
2. **두 가지 Badge 유형**: 숫자 Badge (개수 표시) vs 점 Badge (새 항목 표시)
3. **Material 3 디자인**: 표준 디자인 가이드라인 준수 및 접근성 자동 처리

---

## 문제 상황: Badge 없이 직접 구현할 때

### 시나리오

개발자 김철수는 앱의 메일 아이콘 위에 읽지 않은 메일 개수를 빨간 Badge로 표시하고 싶습니다.
Badge 컴포넌트를 모르는 그는 Box와 Canvas를 사용해서 직접 구현하려고 합니다.

### 잘못된 코드 예시

```kotlin
// 이렇게 하면 문제가 발생합니다!
@Composable
fun ManualBadgeAttempt(count: Int) {
    Box {
        Icon(
            imageVector = Icons.Filled.Mail,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        // 수동으로 Badge 배치 시도
        if (count > 0) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .offset(x = 12.dp, y = (-4).dp)  // offset 하드코딩 필요
                    .background(Color.Red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    color = Color.White,
                    fontSize = 10.sp  // 크기 수동 조절 필요
                )
            }
        }
    }
}
```

### 발생하는 문제점

1. **정렬이 복잡함**: offset 값을 하드코딩해야 하고, 아이콘 크기가 바뀌면 다시 조정 필요
2. **크기 조절이 어려움**: 숫자가 2자리, 3자리일 때 Badge 크기를 수동으로 조절해야 함
3. **접근성 처리 누락**: 스크린 리더가 Badge 정보를 읽을 수 없음
4. **Material 디자인 불일치**: 공식 디자인 가이드라인과 다른 모양새

---

## 해결책: Badge & BadgedBox 사용

### 올바른 코드

```kotlin
@Composable
fun ProperBadgeImplementation(count: Int) {
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge {
                    Text(
                        text = if (count > 99) "99+" else count.toString()
                    )
                }
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Mail,
            contentDescription = "메일"
        )
    }
}
```

### 해결되는 이유

1. **자동 정렬**: BadgedBox가 Badge를 아이콘 우상단에 자동 배치
2. **자동 크기 조절**: Badge가 내용에 맞게 자동으로 크기 조절
3. **접근성 내장**: Material 3가 접근성 속성 자동 처리
4. **Material 디자인 일치**: 공식 Material 3 디자인 가이드라인 준수

---

## 사용 시나리오

### 1. 점(Dot) Badge - 새 알림 표시

```kotlin
// 새로운 알림이 있음을 간단히 표시
BadgedBox(badge = { Badge() }) {
    Icon(Icons.Filled.Notifications, contentDescription = "알림")
}
```

### 2. 숫자 Badge - 정확한 개수 표시

```kotlin
// 읽지 않은 메일 개수 표시
BadgedBox(
    badge = {
        Badge { Text("5") }
    }
) {
    Icon(Icons.Filled.Mail, contentDescription = "메일")
}
```

### 3. 99+ 오버플로우 처리

```kotlin
// 많은 숫자를 99+로 표시
BadgedBox(
    badge = {
        Badge {
            val displayText = if (count > 99) "99+" else count.toString()
            Text(displayText)
        }
    }
) {
    Icon(Icons.Filled.Email, contentDescription = "이메일")
}
```

### 4. 색상 커스터마이징

```kotlin
// 브랜드 색상 적용
BadgedBox(
    badge = {
        Badge(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ) {
            Text("3")
        }
    }
) {
    Icon(Icons.Filled.ShoppingCart, contentDescription = "장바구니")
}
```

### 5. NavigationBar와 통합

```kotlin
NavigationBarItem(
    selected = true,
    onClick = { },
    icon = {
        BadgedBox(badge = { Badge { Text("3") } }) {
            Icon(Icons.Filled.Home, contentDescription = "홈")
        }
    },
    label = { Text("홈") }
)
```

---

## 주의사항

- **0일 때 숨기기**: `if (count > 0)` 조건으로 Badge 표시 여부 결정
- **99+ 처리**: 큰 숫자는 사용자 경험을 위해 "99+"로 표시
- **접근성**: 필요시 semantics modifier로 추가 설명 제공
- **색상 대비**: containerColor와 contentColor의 대비 확인

---

## 연습 문제

### 연습 1: 기본 Badge 표시 - 쉬움

알림 아이콘 위에 빨간 점(Dot Badge)을 표시하세요.

**요구사항:**
- `Icons.Filled.Notifications` 아이콘 사용
- 아이콘 위에 빨간 점 Badge 표시

### 연습 2: 동적 카운트 Badge - 중간

버튼을 눌러 메시지 개수를 증가/감소하고, Badge에 표시하세요.

**요구사항:**
- +/- 버튼으로 카운트 조절
- 카운트가 0이면 Badge 숨기기
- 카운트가 99보다 크면 "99+" 표시

### 연습 3: 장바구니 아이콘 Badge - 어려움

장바구니 아이콘에 상품 개수를 표시하는 완전한 UI를 구현하세요.

**요구사항:**
- `Icons.Filled.ShoppingCart` 아이콘 사용
- 커스텀 색상 적용 (primary 색상)
- "장바구니에 담기" 버튼으로 개수 증가
- "비우기" 버튼으로 개수 리셋
- 0일 때 Badge 숨기기, 99보다 크면 "99+" 표시

---

## 다음 학습

- **NavigationBar**: Badge를 활용한 하단 네비게이션 구성
- **BottomSheet**: 장바구니 상세 보기 구현
- **Animation**: Badge 나타나는 애니메이션 추가
