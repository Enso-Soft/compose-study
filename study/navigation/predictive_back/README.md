# Predictive Back 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `BackHandler` | Compose에서 뒤로가기 이벤트 처리 | [📚 학습하기](../back_handler/README.md) |
| `Flow` | Kotlin 비동기 스트림 처리 | [📚 학습하기](../../basics/kotlin_flow/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Predictive Back**은 Android 14+에서 도입된 **예측 뒤로가기 제스처**입니다.
사용자가 뒤로 스와이프할 때 **이전 화면을 미리 보여주어**, 의도치 않은 화면 이탈을 방지하고 부드러운 전환 경험을 제공합니다.

> **비유**: Predictive Back은 "문 미리보기"와 같습니다.
> - 기존 뒤로가기: 문을 확 열어버림 (뭐가 있는지 모름)
> - Predictive Back: 문을 살짝 열어 안을 엿봄 (맞으면 열고, 아니면 닫음)

## 핵심 특징

1. **미리보기 제공**: 뒤로가기 완료 전 이전 화면 확인 가능
2. **취소 가능**: 제스처 도중 손을 떼면 취소 (원래 화면 유지)
3. **실시간 애니메이션**: progress 값(0~1)으로 커스텀 애니메이션 적용

---

## 문제 상황: 기존 BackHandler의 갑작스러운 화면 전환

### 시나리오

```
1. 사용자가 노트 편집 화면에서 글을 작성 중
2. 실수로 화면 왼쪽 가장자리를 스와이프
3. 갑자기! 이전 화면(목록)으로 전환됨
4. 작성 중이던 내용이 저장되었는지 불안함
5. 뒤로가기를 취소하고 싶었으나 이미 늦음
```

### 잘못된 코드 예시

```kotlin
// 기존 BackHandler - 애니메이션 없이 즉시 전환
BackHandler(enabled = showDetail) {
    showDetail = false  // 갑자기 화면 전환!
}
```

### 발생하는 문제점

1. **갑작스러운 전환**: 애니메이션 없이 화면이 즉시 바뀜
2. **되돌릴 수 없음**: 제스처 도중 취소할 방법이 없음
3. **목적지 불확실**: 어디로 돌아가는지 미리 알 수 없음

---

## 해결책: PredictiveBackHandler 사용

### 핵심 API

```kotlin
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.BackEventCompat
import kotlinx.coroutines.flow.Flow

PredictiveBackHandler { backEvent: Flow<BackEventCompat> ->
    try {
        backEvent.collect { event ->
            // event.progress: 0.0 ~ 1.0 (제스처 진행률)
            // event.touchX, event.touchY: 터치 좌표
            // event.swipeEdge: EDGE_LEFT 또는 EDGE_RIGHT
        }
        // 제스처 완료: 뒤로가기 실행
        onBack()
    } catch (e: CancellationException) {
        // 제스처 취소: 원래 상태 복원
    }
}
```

### 올바른 코드

```kotlin
@Composable
fun PredictiveBackDemo(onBack: () -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Text("상세 화면 - 스와이프해보세요!")

        PredictiveBackHandler { backEvent ->
            try {
                backEvent.collect { event ->
                    // 제스처 진행에 따라 화면 축소 (1.0 -> 0.8)
                    scale = 1f - (event.progress * 0.2f)
                }
                onBack()
            } catch (e: CancellationException) {
                scale = 1f  // 취소 시 원래 크기로 복원
            }
        }
    }
}
```

### 해결되는 이유

| 문제 | 해결 |
|------|------|
| 갑작스러운 전환 | progress로 부드러운 애니메이션 |
| 취소 불가 | CancellationException으로 취소 처리 |
| 목적지 불확실 | 시스템이 이전 화면 미리보기 제공 |

---

## 설정 방법

### 1. AndroidManifest.xml

```xml
<application
    android:enableOnBackInvokedCallback="true"
    ...>
```

> **참고**: Android 15+에서는 기본 활성화되어 있어 이 설정이 필요 없습니다.

### 2. Dependencies (build.gradle.kts)

```kotlin
dependencies {
    implementation("androidx.activity:activity-compose:1.9.0")
    // Navigation Compose 사용 시
    implementation("androidx.navigation:navigation-compose:2.8.0")
}
```

---

## BackEventCompat 속성

| 속성 | 타입 | 설명 |
|------|------|------|
| `progress` | Float | 제스처 진행률 (0.0 ~ 1.0) |
| `touchX` | Float | 터치 X 좌표 |
| `touchY` | Float | 터치 Y 좌표 |
| `swipeEdge` | Int | 스와이프 시작 위치 (EDGE_LEFT / EDGE_RIGHT) |

```kotlin
backEvent.collect { event ->
    when (event.swipeEdge) {
        BackEventCompat.EDGE_LEFT -> {
            // 왼쪽에서 스와이프 시작
            translationX = event.progress * 100f
        }
        BackEventCompat.EDGE_RIGHT -> {
            // 오른쪽에서 스와이프 시작
            translationX = -event.progress * 100f
        }
    }
}
```

---

## 기존 BackHandler와 비교

| 항목 | BackHandler | PredictiveBackHandler |
|------|-------------|----------------------|
| 콜백 시점 | 뒤로가기 완료 후 | 제스처 진행 중 실시간 |
| 애니메이션 | 지원 안함 | progress로 커스텀 가능 |
| 취소 지원 | 없음 | CancellationException |
| API 레벨 | 모든 버전 | Android 14+ (하위 호환) |

---

## 지원되는 Material3 컴포넌트

다음 컴포넌트들은 **자동으로 Predictive Back을 지원**합니다:

- `ModalBottomSheet`
- `ModalNavigationDrawer` / `DismissibleNavigationDrawer`
- `SearchBar`

```kotlin
// Material3 ModalBottomSheet - 자동 지원!
ModalBottomSheet(
    onDismissRequest = { showSheet = false }
) {
    // 컨텐츠
}
```

---

## 주의사항

1. **try-catch 필수**: `CancellationException`을 처리하지 않으면 앱 크래시
2. **Root Activity 주의**: MainActivity에서 back event를 가로채면 back-to-home 애니메이션 비활성화
3. **finally 블록**: 상태 복원에 사용하면 완료/취소 모두 처리 가능

```kotlin
PredictiveBackHandler { backEvent ->
    try {
        backEvent.collect { event ->
            scale = 1f - (event.progress * 0.2f)
        }
        onBack()
    } catch (e: CancellationException) {
        // 취소 시에만 실행
        throw e  // 반드시 다시 던져야 함!
    } finally {
        // 완료/취소 모두 실행
        scale = 1f
    }
}
```

---

## 연습 문제

### 연습 1: 기본 스케일 애니메이션 (쉬움)

카드 화면에서 뒤로가기 제스처 시 카드가 축소되는 애니메이션을 구현하세요.

**목표**:
- PredictiveBackHandler 기본 사용법 익히기
- try-catch-finally 패턴 적용

### 연습 2: 스와이프 방향별 애니메이션 (중간)

왼쪽에서 스와이프하면 오른쪽으로 밀리고, 오른쪽에서 스와이프하면 왼쪽으로 밀리는 애니메이션을 구현하세요.

**목표**:
- BackEventCompat.swipeEdge 활용
- 방향에 따른 translationX 적용

### 연습 3: 커스텀 BottomSheet 통합 (어려움)

커스텀 BottomSheet 컴포넌트에 Predictive Back을 적용하여 부드럽게 닫히도록 구현하세요.

**목표**:
- 컴포넌트 상태와 Predictive Back 연동
- offset 기반 애니메이션

---

## 다음 학습

- [Navigation 3](../navigation_3/) - 새로운 Navigation Compose API
- [BackHandler](../back_handler/) - 기본 뒤로가기 처리

---

## 참고 자료

- [Android Developers - Predictive Back](https://developer.android.com/develop/ui/compose/system/predictive-back)
- [Set up Predictive Back](https://developer.android.com/develop/ui/compose/system/predictive-back-setup)
- [Predictive Back Codelab](https://developer.android.com/codelabs/predictive-back)
