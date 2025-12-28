# Material 3 Expressive 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `MaterialTheme` | Compose의 기본 테마 시스템 | [📚 학습하기](../scaffold_and_theming/README.md) |
| `animation_basics` | Compose 애니메이션 기본 개념 | [📚 학습하기](../../animation/animation_basics/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

**Material 3 Expressive**는 2025년 Google I/O에서 발표된 Material Design 3의 새로운 확장입니다.
기존 Material 3에 **스프링 물리 기반 모션**, **35개의 새로운 Shape**, **15개의 새로운 컴포넌트**를 추가하여
더욱 생동감 있고 감성적인 사용자 경험을 제공합니다.

> 마치 기존 Material 3가 "정장"이라면, Material 3 Expressive는 "편안한 운동복"과 같습니다.
> 더 자유롭고 생동감 있게 움직일 수 있죠!

## 핵심 특징

### 1. MaterialExpressiveTheme
기존 `MaterialTheme`을 대체하는 새로운 테마 함수입니다.
기본적으로 expressive 모션 스킴이 적용되어 모든 Material 컴포넌트에 스프링 물리 애니메이션이 자동으로 적용됩니다.

### 2. MotionScheme.expressive()
스프링 물리 기반의 애니메이션 시스템입니다:
- **Spatial Springs (공간 스프링)**: 위치, 크기, 회전 등 "공간적" 변화에 사용
- **Effects Springs (효과 스프링)**: 색상, 투명도 등 "시각 효과" 변화에 사용

> 비유: 춤을 출 때 발걸음(위치 이동)은 Spatial, 표정 변화(시각 효과)는 Effects입니다!

### 3. 35개의 새로운 Shape
기본 둥근 모서리를 넘어 다양한 형태의 Shape를 제공합니다:
- `MaterialShapes.Cookie9Sided` - 쿠키 모양
- 그 외 다양한 기하학적 형태
- **Shape morphing** - 도형 간 부드러운 전환 애니메이션

### 4. 15개의 새로운/업데이트된 컴포넌트
- `LargeExtendedFloatingActionButton`
- `CircularWavyProgressIndicator`
- `LinearWavyProgressIndicator`
- `FloatingActionButtonMenu`
- `ToggleFloatingActionButton`
- `HorizontalFloatingToolbar`
- `SplitButtonLayout`
- 등등...

---

## 의존성 설정

```kotlin
// build.gradle.kts
dependencies {
    // Material 3 Expressive (1.4.0-alpha17 이상)
    implementation("androidx.compose.material3:material3:1.4.0-alpha17")

    // Graphics Shapes (MaterialShapes 사용 시)
    implementation("androidx.graphics:graphics-shapes:1.0.1")
}
```

---

## 기존 Material 3 vs Material Expressive 비교

### 기존 Material 3의 한계

```kotlin
// 기존 방식: tween 기반의 선형 애니메이션
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.9f else 1f,
    animationSpec = tween(300) // 딱딱한 선형 전환
)
```

**문제점**:
- 로봇처럼 딱딱한 움직임
- 자연스러운 바운스 효과 없음
- 사용자에게 감성적 반응 전달 어려움

### Material Expressive 해결책

```kotlin
// Material Expressive: 스프링 물리 기반
val animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.9f else 1f,
    animationSpec = animationSpec // 탄력있는 스프링!
)
```

**장점**:
- 실제 스프링처럼 탄력있게 튕기는 움직임
- 자연스럽고 생동감 있는 인터랙션
- 사용자에게 즐거운 경험 제공

---

## 기본 사용법

### 1. MaterialExpressiveTheme 설정

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyApp() {
    MaterialExpressiveTheme(
        colorScheme = dynamicLightColorScheme(LocalContext.current),
        motionScheme = MotionScheme.expressive(),
        typography = Typography
    ) {
        // 앱 콘텐츠
    }
}
```

### 2. Spatial Spec으로 애니메이션 적용

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SpringButton() {
    val animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = animationSpec
    )

    Button(
        modifier = Modifier.scale(scale),
        onClick = { isPressed = !isPressed }
    ) {
        Text("스프링 버튼")
    }
}
```

### 3. 새로운 컴포넌트 사용

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveComponents() {
    // 큰 확장 FAB
    LargeExtendedFloatingActionButton(
        onClick = { /* ... */ },
        icon = { Icon(Icons.Filled.Add, contentDescription = null) },
        text = { Text("Large FAB") }
    )

    // 물결 프로그레스
    CircularWavyProgressIndicator()
}
```

---

## Spatial Springs vs Effects Springs

| 구분 | Spatial Springs | Effects Springs |
|------|-----------------|-----------------|
| 용도 | 위치, 크기, 회전 | 색상, 투명도 |
| API | `defaultSpatialSpec<T>()` | `defaultEffectsSpec<T>()` |
| 예시 | 버튼 스케일, 카드 이동 | 페이드 인/아웃, 색상 전환 |

```kotlin
// Spatial: 버튼 스케일 애니메이션
val spatialSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()

// Effects: 투명도 전환 애니메이션
val effectsSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
```

---

## 베스트 프랙티스

1. **테마 레벨에서 적용**: 앱 최상위에서 `MaterialExpressiveTheme` 사용
2. **일관된 모션**: 같은 유형의 애니메이션에는 같은 Spec 사용
3. **적절한 Spec 선택**:
   - 이동/크기 변화 → Spatial
   - 색상/투명도 변화 → Effects
4. **OptIn 어노테이션 필수**: `@OptIn(ExperimentalMaterial3ExpressiveApi::class)`

---

## 안티패턴

1. **기존 tween과 혼용하지 않기**
   ```kotlin
   // BAD: Expressive 테마에서 tween 사용
   animationSpec = tween(300)

   // GOOD: MotionScheme 사용
   animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
   ```

2. **모든 곳에 expressive 적용하지 않기**
   - 로딩 인디케이터 등 지속적 애니메이션은 기존 방식 유지 가능

---

## 연습 문제

### 연습 1: MaterialExpressiveTheme 적용하기 (쉬움)
기존 `MaterialTheme`을 `MaterialExpressiveTheme`으로 교체하세요.

### 연습 2: Spatial Spec 버튼 애니메이션 (중간)
`defaultSpatialSpec`을 사용하여 버튼 클릭 시 스프링 스케일 애니메이션을 구현하세요.

### 연습 3: FAB 메뉴 구현 (어려움)
`FloatingActionButtonMenu`와 `ToggleFloatingActionButton`을 조합하여
확장 가능한 FAB 메뉴를 구현하세요.

---

## 다음 학습

- [Scaffold와 테마](../scaffold_and_theming/) - 기본 테마 시스템 이해
- [애니메이션 기초](../../animation/animation_basics/) - Compose 애니메이션 기본
- [애니메이션 심화](../../animation/animation_advanced/) - 고급 애니메이션 기법

---

## 참고 자료

- [Material 3 Expressive 공식 문서](https://m3.material.io/)
- [Android Developers Blog - Androidify](https://android-developers.googleblog.com/2025/05/androidify-building-delightful-ui-with-compose.html)
- [Compose Material 3 릴리즈 노트](https://developer.android.com/jetpack/androidx/releases/compose-material3)
