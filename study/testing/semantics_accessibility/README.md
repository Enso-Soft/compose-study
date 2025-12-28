# Semantics & Accessibility (접근성) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | 기본 UI 컴포넌트 (Text, Button, Image 등) | [📚 학습하기](../../layout/basic_ui_components/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Semantics**는 Compose UI 요소에 "의미 정보"를 부여하는 시스템으로, 접근성 서비스와 테스트 프레임워크가 화면을 이해할 수 있게 합니다.

## 핵심 특징

1. **Semantics Tree**: UI Tree와 병렬로 존재하며, 각 요소의 역할과 상태를 담고 있음
2. **접근성 서비스 통합**: TalkBack, Switch Access 등이 자동으로 Semantics 정보를 활용
3. **테스트 지원**: ComposeTestRule에서 `onNodeWithContentDescription()` 등으로 UI 테스트 가능

## 왜 중요한가?

> **법적 요구사항**: 2025년 6월부터 시행되는 **유럽 접근성 법(EAA)**에 따라, EU 시장을 대상으로 하는 앱은 접근성 준수가 법적 의무입니다. 접근성을 무시하면 법적 제재를 받을 수 있습니다.

- 시각 장애인 사용자가 앱을 사용할 수 있도록 함
- 자동화된 접근성 테스트 통과
- 더 나은 사용자 경험 제공

```
UI Tree (화면)           Semantics Tree (의미)
    Column                  [Container]
      |                         |
    +-+-+                   +---+---+
    |   |                   |       |
  Image Text             "상품      "무선
                          이미지"    이어폰"
```

---

## Semantics Tree 구조

### Merged vs Unmerged Tree

```kotlin
// Merged Tree: mergeDescendants=true인 노드가 자식들을 병합
Row(
    modifier = Modifier.semantics(mergeDescendants = true) { }
) {
    Image(...)  // 병합됨
    Text(...)   // 병합됨
}
// TalkBack: 하나의 요소로 인식

// Unmerged Tree: 모든 노드가 개별적으로 존재
// 테스트 시 useUnmergedTree = true로 접근 가능
```

---

## 핵심 API

### 1. Modifier.semantics { }

시맨틱 속성을 추가하거나 수정합니다.

```kotlin
Icon(
    imageVector = Icons.Default.Favorite,
    contentDescription = null,  // 기본값
    modifier = Modifier.semantics {
        contentDescription = "좋아요 추가"  // 덮어쓰기
    }
)
```

### 2. contentDescription

스크린 리더가 읽어줄 설명을 제공합니다.

```kotlin
// 좋은 예: 동작이나 의미를 설명
Image(
    painter = painterResource(R.drawable.product),
    contentDescription = "무선 이어폰 상품 이미지"
)

// 나쁜 예: null (스크린 리더가 무시)
Image(
    painter = painterResource(R.drawable.product),
    contentDescription = null  // 접근성 문제!
)
```

### 3. mergeDescendants

자식 요소들을 하나의 논리적 단위로 병합합니다.

```kotlin
// 프로필 카드 - 하나의 요소로 인식됨
Row(
    modifier = Modifier.semantics(mergeDescendants = true) { }
) {
    Image(contentDescription = "프로필")
    Column {
        Text("홍길동")
        Text("개발자")
    }
}
// TalkBack: "프로필, 홍길동, 개발자" (한 번에)
```

### 4. clearAndSetSemantics { }

기존 시맨틱을 모두 지우고 새로 설정합니다.

```kotlin
// 자식들의 시맨틱을 무시하고 커스텀 설명 제공
Row(
    modifier = Modifier.clearAndSetSemantics {
        contentDescription = "홍길동 프로필, 개발자"
    }
) {
    Image(...)  // 이 시맨틱은 무시됨
    Text(...)   // 이 시맨틱도 무시됨
}
```

### 5. traversalIndex (탐색 순서)

TalkBack 탐색 순서를 조정합니다.

```kotlin
Box {
    // 시각적으로 아래에 있지만 먼저 탐색
    Button(
        modifier = Modifier.semantics { traversalIndex = 0f }
    ) { Text("먼저 읽힘") }

    // 시각적으로 위에 있지만 나중에 탐색
    Text(
        modifier = Modifier.semantics { traversalIndex = 1f },
        text = "나중에 읽힘"
    )
}
```

### 6. liveRegion (동적 콘텐츠 알림)

콘텐츠가 변경될 때 자동으로 알려줍니다.

```kotlin
// Polite: 현재 읽기가 끝난 후 알림
Text(
    text = "알림: ${count}개",
    modifier = Modifier.semantics {
        liveRegion = LiveRegionMode.Polite
    }
)

// Assertive: 즉시 중단하고 알림 (긴급 상황용)
Text(
    text = "경고: 연결 끊김!",
    modifier = Modifier.semantics {
        liveRegion = LiveRegionMode.Assertive
    }
)
```

### 7. hideFromAccessibility (접근성에서 숨김)

장식용 요소를 접근성 서비스에서 숨기되, 테스트에서는 접근 가능하게 유지합니다.

```kotlin
// 장식용 구분선 - TalkBack에서는 무시되지만 테스트에서는 접근 가능
Text(
    text = "•",
    modifier = Modifier.semantics {
        hideFromAccessibility()
    }
)
```

> **contentDescription = null과의 차이**: `hideFromAccessibility()`는 요소를 Semantics Tree에 유지하므로 테스트가 가능합니다. 반면 `contentDescription = null`은 해당 요소에 대한 설명만 제거합니다.

---

## 문제 상황: 접근성을 고려하지 않은 UI

### 잘못된 코드 예시

```kotlin
@Composable
fun BadProductCard() {
    Row {
        // 문제 1: contentDescription 누락
        Image(
            painter = painterResource(R.drawable.product),
            contentDescription = null
        )
        Column {
            Text("무선 이어폰")  // 개별 포커스
            Text("89,000원")     // 개별 포커스
        }
        // 문제 2: 아이콘만 있는 버튼
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null  // 무슨 버튼인지 모름!
            )
        }
    }
}
```

### 발생하는 문제점

1. **TalkBack 탐색 결과:**
   - "이미지" (무슨 이미지?)
   - "무선 이어폰"
   - "89,000원"
   - "버튼" (무슨 버튼?)

2. **사용자 경험:**
   - 상품 이미지 내용 불명
   - 각 요소를 개별적으로 탐색해야 함
   - 장바구니 버튼의 기능을 알 수 없음
   - 탐색에 시간이 오래 걸림

---

## 해결책: Semantics 적용

### 올바른 코드

```kotlin
@Composable
fun GoodProductCard() {
    Row(
        modifier = Modifier
            .semantics(mergeDescendants = true) { }
            .clearAndSetSemantics {
                contentDescription = "무선 이어폰, 89,000원"
            }
    ) {
        Image(
            painter = painterResource(R.drawable.product),
            contentDescription = "무선 이어폰 상품 이미지"
        )
        Column {
            Text("무선 이어폰")
            Text("89,000원")
        }
    }

    IconButton(
        onClick = { addToCart() },
        modifier = Modifier.semantics {
            contentDescription = "장바구니에 추가"
        }
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null  // 부모에서 설정했으므로 OK
        )
    }
}
```

### 해결되는 이유

1. **TalkBack 탐색 결과:**
   - "무선 이어폰, 89,000원" (한 번에)
   - "장바구니에 추가, 버튼"

2. **사용자 경험:**
   - 카드 전체 정보를 한 번에 파악
   - 버튼 기능이 명확함
   - 탐색 시간 단축

---

## 색상 대비율 가이드 (WCAG)

### 최소 요구사항 (Level AA)

| 텍스트 크기 | 대비율 |
|------------|--------|
| 일반 텍스트 (< 18pt) | 4.5:1 |
| 대형 텍스트 (>= 18pt 또는 14pt 굵게) | 3:1 |
| UI 컴포넌트/그래픽 | 3:1 |

### 향상된 요구사항 (Level AAA)

| 텍스트 크기 | 대비율 |
|------------|--------|
| 일반 텍스트 | 7:1 |
| 대형 텍스트 | 4.5:1 |

### 색상만으로 정보를 전달하지 마세요

```kotlin
// 나쁜 예: 색상만으로 상태 표시
Text(
    text = "재고",
    color = if (inStock) Color.Green else Color.Red
)

// 좋은 예: 색상 + 텍스트/아이콘
Row {
    Icon(
        imageVector = if (inStock) Icons.Default.Check else Icons.Default.Close,
        contentDescription = null
    )
    Text(
        text = if (inStock) "재고 있음" else "품절",
        color = if (inStock) Color.Green else Color.Red
    )
}
```

---

## Compose 1.8+ 자동화 접근성 테스트

### 의존성 추가

```kotlin
// build.gradle.kts
// Compose 1.8+부터 별도 패키지로 분리됨
androidTestImplementation("androidx.compose.ui:ui-test-junit4-accessibility:$compose_version")
```

### 테스트 코드

```kotlin
@RunWith(AndroidJUnit4::class)
class AccessibilityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // 접근성 검사 활성화
        composeTestRule.enableAccessibilityChecks()
    }

    @Test
    fun productCard_meetsAccessibilityGuidelines() {
        composeTestRule.setContent {
            ProductCard()
        }

        // 접근성 검사 수행
        composeTestRule.onRoot().tryPerformAccessibilityChecks()
    }
}
```

### 검사 항목

- **콘텐츠 설명 누락**: contentDescription이 없는 이미지/아이콘
- **색상 대비**: 텍스트 대비율이 4.5:1 미만
- **터치 타겟 크기**: 48dp x 48dp 미만
- **탐색 순서**: 논리적이지 않은 순서

---

## TalkBack / Switch Access 테스트

### TalkBack 테스트 방법

1. **설정 > 접근성 > TalkBack** 활성화
2. 앱 실행 후 화면을 스와이프하며 탐색
3. 확인 사항:
   - 모든 요소가 읽히는가?
   - 읽는 순서가 논리적인가?
   - 버튼/링크의 동작이 명확한가?

### Switch Access 테스트 방법

1. **설정 > 접근성 > Switch Access** 활성화
2. 스위치(볼륨 버튼 등)로 탐색
3. 확인 사항:
   - 모든 인터랙티브 요소에 접근 가능한가?
   - 포커스 순서가 적절한가?

---

## 사용 시나리오

### 1. 리스트 아이템

```kotlin
@Composable
fun ContactItem(contact: Contact) {
    Row(
        modifier = Modifier
            .clickable { onContactClick(contact) }
            .semantics {
                // clickable이 자동으로 mergeDescendants = true 적용
                contentDescription = "${contact.name}, ${contact.role}"
            }
    ) {
        Avatar(contact.imageUrl)
        Column {
            Text(contact.name)
            Text(contact.role)
        }
    }
}
```

### 2. 동적 알림 (카운터/타이머)

```kotlin
@Composable
fun NotificationBadge(count: Int) {
    Box(
        modifier = Modifier.semantics {
            contentDescription = "읽지 않은 알림 ${count}개"
            liveRegion = LiveRegionMode.Polite
        }
    ) {
        Text("$count")
    }
}
```

### 3. 폼 필드 탐색 순서

```kotlin
@Composable
fun LoginForm() {
    Column(
        modifier = Modifier.semantics { isTraversalGroup = true }
    ) {
        TextField(
            value = email,
            onValueChange = { },
            modifier = Modifier.semantics { traversalIndex = 0f }
        )
        TextField(
            value = password,
            onValueChange = { },
            modifier = Modifier.semantics { traversalIndex = 1f }
        )
        Button(
            onClick = { },
            modifier = Modifier.semantics { traversalIndex = 2f }
        ) {
            Text("로그인")
        }
    }
}
```

---

## 주의사항

### 1. clearAndSetSemantics 남용 금지

```kotlin
// 나쁜 예: 모든 정보를 숨김
Modifier.clearAndSetSemantics { }  // 빈 람다 = 완전히 숨김

// 좋은 예: 필요한 정보 제공
Modifier.clearAndSetSemantics {
    contentDescription = "의미 있는 설명"
}
```

> **주의**: `clearAndSetSemantics`를 사용하면 접근성 서비스뿐만 아니라 **AI 에이전트**나 자동화 도구도 해당 요소의 정보를 얻을 수 없게 됩니다. 꼭 필요한 경우에만 사용하세요.

### 2. 장식용 요소 처리

```kotlin
// 순수 장식용 이미지는 숨김 가능
Image(
    painter = painterResource(R.drawable.decoration),
    contentDescription = null  // 의도적으로 숨김
)

// 또는 명시적으로 접근성에서 숨김 (테스트에서는 여전히 접근 가능)
Modifier.semantics { hideFromAccessibility() }
```

### 3. 중복 알림 방지

```kotlin
// liveRegion을 자주 업데이트되는 요소에 사용하면
// 사용자가 피로해짐
var seconds by remember { mutableStateOf(0) }

// 나쁜 예: 매초 알림
Text(
    text = "$seconds",
    modifier = Modifier.semantics {
        liveRegion = LiveRegionMode.Polite  // 너무 많은 알림!
    }
)

// 좋은 예: 중요한 변경만 알림
Text(
    text = "$seconds",
    modifier = Modifier.semantics {
        if (seconds % 30 == 0) {  // 30초마다만
            liveRegion = LiveRegionMode.Polite
        }
    }
)
```

---

## 연습 문제

### 연습 1: 프로필 카드 접근성 개선 (초급)
- 프로필 사진, 이름, 상태 메시지를 하나의 논리적 단위로 병합
- 팔로우 버튼에 적절한 설명 추가

### 연습 2: 실시간 경매 타이머 (중급)
- liveRegion으로 가격 변동 알림
- 종료 임박 시 Assertive 모드 사용
- 색상 외 텍스트/아이콘으로 상태 표시

### 연습 3: 회원가입 폼 탐색 순서 (고급)
- traversalIndex로 폼 필드 순서 조정
- 도움말 툴팁은 마지막에 탐색되도록 설정

---

## 다음 학습

접근성을 구현한 후에는 다음 주제를 학습하세요:

1. **compose_testing** - UI 테스트에서 Semantics 활용
2. **screenshot_testing** - 시각적 회귀 테스트
3. **focus_management** - 키보드/포커스 접근성

---

## 참고 자료

- [Android Developers: Semantics](https://developer.android.com/develop/ui/compose/accessibility/semantics)
- [Merging and clearing](https://developer.android.com/develop/ui/compose/accessibility/merging-clearing)
- [Modify traversal order](https://developer.android.com/develop/ui/compose/accessibility/traversal)
- [Accessibility testing](https://developer.android.com/develop/ui/compose/accessibility/testing)
- [WCAG Color Contrast Guidelines](https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html)
- [CVS Health Compose Accessibility Techniques](https://github.com/cvs-health/android-compose-accessibility-techniques)
