# Shared Element Transition 학습

## 개념

**Shared Element Transition(공유 요소 전환)**은 두 화면 사이에서 동일한 콘텐츠(이미지, 텍스트 등)가 시각적으로 연결되어 자연스럽게 전환되는 애니메이션입니다.

예를 들어, 리스트에서 아이템을 클릭하면 해당 아이템의 이미지가 상세 화면의 큰 이미지로 "이동"하듯이 전환됩니다. 이를 통해 사용자는 "같은 아이템을 보고 있다"는 맥락을 유지할 수 있습니다.

### 실생활 비유

영화에서 장면 전환 시 특정 물체가 화면에 남아 있으면서 다음 장면으로 자연스럽게 이어지는 것과 같습니다. 예를 들어, 카메라가 사진 한 장에 집중했다가, 그 사진이 점점 커지면서 사진 속 장면으로 들어가는 연출과 비슷합니다.

---

## 핵심 API

### 1. SharedTransitionLayout

가장 바깥쪽에 감싸야 하는 레이아웃입니다. `SharedTransitionScope`를 제공합니다.

```kotlin
SharedTransitionLayout {
    NavHost(navController, startDestination = "list") {
        composable("list") { ... }
        composable("detail/{id}") { ... }
    }
}
```

### 2. Modifier.sharedElement()

두 화면에서 동일한 요소를 연결하는 modifier입니다.

```kotlin
Image(
    painter = painterResource(R.drawable.image),
    modifier = Modifier
        .sharedElement(
            state = rememberSharedContentState(key = "image-${item.id}"),
            animatedVisibilityScope = animatedVisibilityScope
        )
)
```

**사용 시점**: 시작과 끝 composable이 **완전히 동일**할 때

### 3. Modifier.sharedBounds()

시작과 끝 composable의 **경계(bounds)**만 공유할 때 사용합니다.

```kotlin
Text(
    text = item.name,
    fontSize = 24.sp,  // 리스트에서는 18sp, 상세에서는 24sp
    modifier = Modifier
        .sharedBounds(
            sharedContentState = rememberSharedContentState(key = "text-${item.id}"),
            animatedVisibilityScope = animatedVisibilityScope
        )
)
```

**사용 시점**: 텍스트처럼 폰트 크기가 달라지는 경우

### 4. AnimatedVisibilityScope

`NavHost.composable {}` 블록에서 제공됩니다. 공유 요소의 전환 타이밍을 결정합니다.

```kotlin
composable("detail/{id}") { backStackEntry ->
    // this@composable이 AnimatedVisibilityScope
    DetailScreen(
        animatedVisibilityScope = this@composable
    )
}
```

---

## 문제 상황: Shared Element Transition 없이 화면 전환

### 잘못된 코드 예시

```kotlin
// 일반 Navigation만 사용
@Composable
fun ListScreen(navController: NavController) {
    LazyColumn {
        items(snacks) { snack ->
            SnackCard(
                snack = snack,
                onClick = { navController.navigate("detail/${snack.id}") }
            )
        }
    }
}
```

### 발생하는 문제점

1. **시각적 단절**: 리스트 아이템을 클릭하면 화면이 갑자기 바뀜
2. **맥락 손실**: 어떤 아이템을 클릭했는지 시각적 연결이 없음
3. **부자연스러운 UX**: 이미지가 "점프"하듯이 갑자기 나타남

---

## 해결책: SharedTransitionLayout 사용

### 올바른 코드

```kotlin
@Composable
fun SnackGalleryApp() {
    SharedTransitionLayout {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "list") {
            composable("list") {
                SnackListScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    onSnackClick = { id -> navController.navigate("detail/$id") }
                )
            }
            composable("detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                SnackDetailScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    snackId = id,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

// 리스트 화면에서 공유 요소 정의
@Composable
fun SnackCard(
    snack: Snack,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit
) {
    with(sharedTransitionScope) {
        Card(onClick = onClick) {
            Row {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(snack.color)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
                Text(
                    text = snack.name,
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "text-${snack.id}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
            }
        }
    }
}
```

### 해결되는 이유

1. **시각적 연속성**: 색상 박스가 리스트에서 상세로 부드럽게 이동
2. **맥락 유지**: 텍스트도 자연스럽게 전환되어 동일한 아이템임을 인지
3. **전문적인 UX**: 모던 앱의 표준 전환 효과 구현

---

## sharedElement vs sharedBounds 비교

| 특성 | sharedElement | sharedBounds |
|------|---------------|--------------|
| 사용 시점 | 콘텐츠가 동일할 때 | 콘텐츠가 다를 때 |
| 예시 | 이미지, 아이콘, 색상 박스 | 텍스트 (폰트 크기 변경) |
| 동작 | 요소가 그대로 이동 | 경계만 전환, 내용은 페이드 |
| 텍스트 권장 | X | O (폰트 스케일링 지원) |

---

## 사용 시나리오

1. **리스트 -> 상세 화면**: 가장 일반적인 패턴
2. **그리드 갤러리**: 이미지 썸네일을 클릭하면 전체 화면으로 확대
3. **카드 확장**: FAB나 카드가 확장되어 새 화면으로 전환
4. **탭 전환**: 탭 간 공유 요소 애니메이션

---

## 고급 기능

### boundsTransform으로 애니메이션 커스터마이징

```kotlin
sharedElement(
    state = rememberSharedContentState(key = "image-$id"),
    animatedVisibilityScope = animatedVisibilityScope,
    boundsTransform = { initialBounds, targetBounds ->
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    }
)
```

### renderInSharedTransitionScopeOverlay()

FAB나 BottomBar를 전환 중에도 항상 위에 표시:

```kotlin
FloatingActionButton(
    modifier = Modifier.renderInSharedTransitionScopeOverlay()
) { ... }
```

---

## 주의사항

1. **키 일치 필수**: 두 화면에서 동일한 `key`를 사용해야 연결됨
2. **SharedTransitionLayout 필수**: NavHost를 반드시 감싸야 함
3. **Scope 전달**: `sharedTransitionScope`와 `animatedVisibilityScope` 모두 전달 필요
4. **with(sharedTransitionScope) 패턴**: `sharedElement` modifier는 `SharedTransitionScope` 내에서만 사용 가능
5. **Preview 제한**: Preview에서는 공유 요소 전환이 동작하지 않음

---

## 연습 문제

1. **연습 1**: 기본 `SharedTransitionLayout` 설정하기
2. **연습 2**: 텍스트에 `sharedBounds` 적용하기
3. **연습 3**: `boundsTransform`으로 애니메이션 커스터마이징하기

---

## 다음 학습

- [Animation Basics](../animation_basics/src/main/java/com/example/animation_basics/README.md) - 기본 애니메이션
- [Animation Advanced](../animation_advanced/src/main/java/com/example/animation_advanced/README.md) - 고급 애니메이션
- [Navigation](../navigation/src/main/java/com/example/navigation/README.md) - Navigation Compose

---

## 참고 자료

- [Shared element transitions in Compose - Android Developers](https://developer.android.com/develop/ui/compose/animation/shared-elements)
- [Shared elements with Navigation Compose - Android Developers](https://developer.android.com/develop/ui/compose/animation/shared-elements/navigation)
- [Customize shared element transition - Android Developers](https://developer.android.com/develop/ui/compose/animation/shared-elements/customize)
