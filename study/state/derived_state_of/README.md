# derivedStateOf 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `state_hoisting` | 상태를 상위 컴포저블로 끌어올려 관리하는 패턴 | [📚 학습하기](../../state/state_hoisting/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

`derivedStateOf`는 **다른 State 객체들로부터 파생된 상태**를 생성하는 함수입니다.
핵심은 **입력이 자주 변해도 출력이 같으면 Recomposition을 트리거하지 않는다**는 점입니다.

```kotlin
val showButton by remember {
    derivedStateOf {
        listState.firstVisibleItemIndex > 0
    }
}
```

## 핵심 특징

1. **자동 의존성 추적**: 내부에서 읽는 Compose State 객체들을 자동으로 추적
2. **지능적 캐싱**: 계산 결과가 이전과 같으면 Recomposition 스킵
3. **Composition 최적화**: 불필요한 UI 업데이트 방지
4. **Snapshot 시스템 통합**: Compose의 상태 관리 시스템과 완벽 호환

## 언제 사용하나?

> **입력 변경 횟수 > 출력 변경 횟수** 일 때 사용!

| 시나리오 | 입력 변경 | 출력 변경 | derivedStateOf 필요? |
|---------|----------|----------|---------------------|
| 스크롤 -> "맨 위로" 버튼 | 수백 번 | 2번 (true/false) | 필수 |
| 검색어 -> 필터링 결과 | 글자마다 | 결과 다를 수 있음 | 상황에 따라 |
| 체크박스 -> 선택 상태 | 클릭마다 | 클릭마다 변경 | 불필요 |

## 문제 상황: derivedStateOf 없이 스크롤 처리

### 잘못된 코드 예시

```kotlin
@Composable
fun ScrollableList() {
    val listState = rememberLazyListState()

    // 문제: 스크롤할 때마다 showButton을 다시 계산하고 Recomposition 발생!
    val showButton = listState.firstVisibleItemIndex > 0

    // showButton을 사용하는 UI -> 매번 Recomposition
    if (showButton) {
        ScrollToTopButton()
    }
}
```

### 발생하는 문제점

1. `firstVisibleItemIndex`가 0->1->2->3->... 계속 변경
2. 매번 `showButton` 조건문을 다시 평가
3. 값이 `true`로 동일해도 **불필요한 Recomposition 발생**
4. 스크롤 성능 저하, 배터리 낭비

## 해결책: derivedStateOf 사용

### 올바른 코드

```kotlin
@Composable
fun ScrollableList() {
    val listState = rememberLazyListState()

    // derivedStateOf로 래핑
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    // showButton이 실제로 변경될 때만 Recomposition!
    if (showButton) {
        ScrollToTopButton()
    }
}
```

### 해결되는 이유

1. `derivedStateOf`는 **계산 결과를 캐싱**
2. `firstVisibleItemIndex`가 5->6->7 변해도 `showButton`은 계속 `true`
3. **결과값이 동일하면 Recomposition을 트리거하지 않음**
4. `false->true` 또는 `true->false`로 실제 변경될 때만 UI 업데이트

## remember key 주의사항

`derivedStateOf`는 **Compose State 객체만** 자동으로 추적합니다.
일반 변수를 사용할 경우 `remember`의 key로 전달해야 합니다.

```kotlin
// 잘못된 코드 - threshold 변경을 감지 못함
val threshold = 5
val showButton by remember {
    derivedStateOf {
        listState.firstVisibleItemIndex > threshold
    }
}

// 올바른 코드 - threshold를 key로 전달
val threshold = 5
val showButton by remember(threshold) {
    derivedStateOf {
        listState.firstVisibleItemIndex > threshold
    }
}
```

## 사용 시나리오

### 1. 스크롤 기반 UI 표시
```kotlin
val showFloatingButton by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 0 }
}
```

### 2. 리스트 필터링 최적화
```kotlin
val filteredItems by remember(items) {
    derivedStateOf {
        items.filter { it.isActive }
    }
}
```

### 3. 폼 유효성 검사
```kotlin
val isFormValid by remember {
    derivedStateOf {
        email.isNotEmpty() && password.length >= 8
    }
}
```

### 4. 페이지네이션 상태
```kotlin
val isAtEnd by remember {
    derivedStateOf {
        listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ==
            listState.layoutInfo.totalItemsCount - 1
    }
}
```

## 주의사항

1. **과도한 사용 금지**: 입력과 출력 변경 빈도가 비슷하면 오히려 오버헤드
2. **remember와 함께 사용**: 항상 `remember { derivedStateOf { } }` 패턴 사용
3. **비-State 변수**: 일반 변수는 remember의 key로 전달
4. **복잡한 계산 주의**: 매 스냅샷마다 계산이 실행되므로 무거운 연산은 피함

## 안티패턴: 잘못된 사용 예시

> 공식 문서에서 명시적으로 경고하는 패턴입니다!

### 잘못된 코드 (사용하지 마세요!)

```kotlin
// DO NOT USE. Incorrect usage of derivedStateOf.
var firstName by remember { mutableStateOf("") }
var lastName by remember { mutableStateOf("") }

// 이렇게 하면 안 됩니다!
val fullNameBad by remember {
    derivedStateOf {
        "$firstName $lastName"
    }
}
```

### 왜 잘못되었나?

1. `firstName`이 변경되면 -> `fullName`도 항상 변경됨
2. `lastName`이 변경되면 -> `fullName`도 항상 변경됨
3. **입력 변경 빈도 = 출력 변경 빈도** -> derivedStateOf의 이점이 없음!
4. derivedStateOf의 추적/비교 로직만 오버헤드로 추가됨

### 올바른 사용

이 경우 그냥 직접 계산하면 됩니다:

```kotlin
var firstName by remember { mutableStateOf("") }
var lastName by remember { mutableStateOf("") }

// 단순히 조합하면 됨 - derivedStateOf 불필요!
val fullName = "$firstName $lastName"
```

### 핵심 원칙

```
derivedStateOf 사용 조건:
입력 변경 횟수 >> 출력 변경 횟수

예: 스크롤 인덱스(0,1,2,3,4,5...) -> 버튼 표시 여부(false, true)
    수백 번 변경                  ->    2번만 변경
```

## derivedStateOf vs remember(key)

| 특성 | derivedStateOf | remember(key) |
|------|---------------|---------------|
| 재계산 시점 | 의존성 변경 시 | key 변경 시 |
| Recomposition | 결과가 달라야 트리거 | key 변경 시 항상 트리거 |
| 사용 시나리오 | 입력>출력 변경 빈도 | 입력=출력 변경 빈도 |

## 연습 문제

1. **스크롤 헤더**: 스크롤 위치에 따라 헤더 축소/확대
2. **장바구니 총액**: 상품 수량 변경 시 총액 계산 최적화
3. **폼 유효성**: 여러 입력 필드의 유효성을 하나의 상태로 통합

## 다음 학습

- `snapshotFlow`: State를 Flow로 변환
- `produceState`: Flow를 State로 변환
- `Stability`: Recomposition 최적화의 근본 원리

## 참고 자료

- [Android Developers - derivedStateOf](https://developer.android.com/develop/ui/compose/side-effects#derivedstateof)
- [When should I use derivedStateOf?](https://medium.com/androiddevelopers/jetpack-compose-when-should-i-use-derivedstateof-63ce7954c11b)
