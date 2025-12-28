# Adaptive Layout 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기                                        |
|----------|------|---------------------------------------------|
| `window_insets` | WindowInsets를 통한 시스템 UI 처리 | [📚 학습하기](../../structure/window_insets/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Adaptive Layout**은 다양한 화면 크기(폰, 태블릿, 폴더블, 데스크톱)에서 최적의 사용자 경험을 제공하기 위해 UI를 동적으로 조정하는 기법입니다.

단순히 UI를 늘리거나 줄이는 **반응형(Responsive)**과 달리, **적응형(Adaptive)**은 화면 크기에 따라 완전히 다른 레이아웃을 제공합니다.

```
반응형 (Responsive)          적응형 (Adaptive)
┌─────────┐  ┌─────────────┐   ┌─────────┐  ┌──────┬──────┐
│ ▣ ▣ ▣ ▣│→│ ▣  ▣  ▣  ▣  │   │ List   │→│ List │Detail│
│         │  │              │   │  ↓     │  │      │      │
│         │  │              │   │ Detail │  │      │      │
└─────────┘  └─────────────┘   └─────────┘  └──────┴──────┘
  작은 화면      큰 화면         작은 화면      큰 화면
  (늘어남)                       (다른 레이아웃)
```

---

## WindowSizeClass

**WindowSizeClass**는 화면 크기를 분류하는 표준 브레이크포인트입니다.

### 너비 기준 (WindowWidthSizeClass)

| 클래스 | 너비 | 대표 기기 |
|--------|------|-----------|
| **Compact** | < 600dp | 세로 모드 폰 |
| **Medium** | 600dp ~ 840dp | 폴더블 언폴드, 세로 태블릿 |
| **Expanded** | > 840dp | 가로 태블릿, 데스크톱 |

### 높이 기준 (WindowHeightSizeClass)

| 클래스 | 높이 | 대표 기기 |
|--------|------|-----------|
| **Compact** | < 480dp | 가로 모드 폰 |
| **Medium** | 480dp ~ 900dp | 세로 모드 폰, 가로 태블릿 |
| **Expanded** | > 900dp | 세로 태블릿 |

---

## WindowSizeClass 사용법 (2025년 권장)

```kotlin
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun AdaptiveScreen() {
    // 현재 윈도우 정보 가져오기
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowAdaptiveInfo.windowSizeClass

    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> {
            // 폰 레이아웃
            PhoneLayout()
        }
        WindowWidthSizeClass.MEDIUM -> {
            // 폴더블/작은 태블릿 레이아웃
            MediumLayout()
        }
        WindowWidthSizeClass.EXPANDED -> {
            // 태블릿/데스크톱 레이아웃
            TabletLayout()
        }
    }
}
```

---

## 핵심 라이브러리 (2025년)

```kotlin
// build.gradle.kts
dependencies {
    // 기본 adaptive 빌딩 블록 (WindowSizeClass, currentWindowAdaptiveInfo 포함)
    implementation("androidx.compose.material3.adaptive:adaptive:1.1.0")

    // 멀티 패인 레이아웃 (ListDetailPaneScaffold, SupportingPaneScaffold)
    implementation("androidx.compose.material3.adaptive:adaptive-layout:1.1.0")

    // 적응형 네비게이터 (NavigableListDetailPaneScaffold)
    implementation("androidx.compose.material3.adaptive:adaptive-navigation:1.1.0")

    // 적응형 네비게이션 UI (NavigationSuiteScaffold)
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha15")
}
```

### 라이브러리 역할

| 라이브러리 | 주요 컴포넌트 | 용도 |
|------------|---------------|------|
| `adaptive` | `currentWindowAdaptiveInfo()` | WindowSizeClass 계산 |
| `adaptive-layout` | `ListDetailPaneScaffold` | 리스트-디테일 레이아웃 |
| `adaptive-navigation` | `NavigableListDetailPaneScaffold` | 뒤로가기 자동 처리 |
| `material3-adaptive-navigation-suite` | `NavigationSuiteScaffold` | 네비게이션 UI 자동 전환 |

---

## 적응형 레이아웃 구현 방법

화면 크기에 따라 레이아웃을 변경하는 **3가지 접근법**이 있습니다.

---

### 방법 1: 수동 when 분기

가장 기본적인 방법으로, `WindowSizeClass`에 따라 직접 레이아웃을 분기합니다.

```kotlin
@Composable
fun AdaptiveScreen() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> PhoneLayout()
        WindowWidthSizeClass.MEDIUM -> FoldableLayout()
        WindowWidthSizeClass.EXPANDED -> TabletLayout()
    }
}
```

**장점**: 완전한 커스텀 레이아웃 가능
**단점**: 네비게이션 로직을 직접 구현해야 함

---

### 방법 2: NavigationSuiteScaffold

화면 크기에 따라 네비게이션 UI를 **자동으로 변경**합니다.

```kotlin
NavigationSuiteScaffold(
    navigationSuiteItems = {
        items.forEach { item ->
            item(
                icon = { Icon(item.icon, null) },
                label = { Text(item.label) },
                selected = selectedItem == item,
                onClick = { selectedItem = item }
            )
        }
    }
) {
    // 컨텐츠
}
```

| 화면 크기 | 네비게이션 UI |
|-----------|---------------|
| Compact | BottomNavigationBar |
| Medium | NavigationRail |
| Expanded | NavigationDrawer |

**장점**: 네비게이션 UI 전환 자동화
**단점**: 네비게이션 패턴에만 적용 가능

---

### 방법 3: ListDetailPaneScaffold (권장)

리스트-디테일 패턴을 화면 크기에 맞게 **자동 조정**합니다.

```kotlin
val navigator = rememberListDetailPaneScaffoldNavigator<Item>()

ListDetailPaneScaffold(
    directive = navigator.scaffoldDirective,
    value = navigator.scaffoldValue,
    listPane = {
        ItemList(onItemClick = { item ->
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)
        })
    },
    detailPane = {
        navigator.currentDestination?.content?.let { item ->
            ItemDetail(item = item)
        }
    }
)
```

| 화면 크기 | 동작 |
|-----------|------|
| Compact | 리스트만 표시, 클릭 시 디테일로 전환 |
| Expanded | 리스트와 디테일 동시 표시 |

**2025년 권장**: `NavigableListDetailPaneScaffold`를 사용하면 뒤로가기 애니메이션까지 자동 처리됩니다.

```kotlin
val navigator = rememberListDetailPaneScaffoldNavigator<Item>()

NavigableListDetailPaneScaffold(
    navigator = navigator,
    listPane = { ... },
    detailPane = { ... }
)
```

---

## 접근법 비교 표

| 기준 | 수동 when 분기 | NavigationSuiteScaffold | ListDetailPaneScaffold |
|------|----------------|-------------------------|------------------------|
| **유연성** | 높음 | 중간 | 중간 |
| **구현 난이도** | 높음 | 낮음 | 중간 |
| **네비게이션 처리** | 수동 | 자동 | 자동 |
| **뒤로가기 처리** | 수동 | 수동 | 자동 (Navigable 버전) |
| **적합한 패턴** | 커스텀 레이아웃 | 탭/섹션 네비게이션 | 리스트-디테일 |
| **학습 곡선** | 낮음 | 낮음 | 중간 |

---

## 상황별 선택 가이드

```
시작
  │
  ├── 리스트에서 항목 선택 → 디테일 표시? ──Yes──► ListDetailPaneScaffold
  │
  ├── 하단 탭/섹션 네비게이션이 필요? ────Yes──► NavigationSuiteScaffold
  │
  ├── 완전히 다른 레이아웃이 필요? ──────Yes──► 수동 when 분기
  │
  └── 위 패턴들의 조합? ─────────────────────► NavigationSuiteScaffold
                                              + ListDetailPaneScaffold
```

### 조합 사용 예시

```kotlin
// NavigationSuiteScaffold 안에 ListDetailPaneScaffold 사용
NavigationSuiteScaffold(
    navigationSuiteItems = { /* 네비게이션 아이템 */ }
) {
    when (selectedTab) {
        Tab.Inbox -> {
            // 받은편지함: 리스트-디테일 패턴
            NavigableListDetailPaneScaffold(...)
        }
        Tab.Settings -> {
            // 설정: 단일 화면
            SettingsScreen()
        }
    }
}

---

## 문제 상황: 단일 레이아웃의 한계

화면 크기를 무시하고 모든 기기에서 동일한 레이아웃을 사용하면:

### 잘못된 코드 예시

```kotlin
@Composable
fun EmailApp() {
    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    // 문제: 화면 크기와 관계없이 항상 전체 화면 전환
    if (selectedEmail == null) {
        EmailList(onEmailClick = { selectedEmail = it })
    } else {
        EmailDetail(
            email = selectedEmail!!,
            onBack = { selectedEmail = null }
        )
    }
}
```

### 발생하는 문제점

1. **태블릿에서 화면 낭비**: 넓은 화면에서 리스트만 표시되어 절반이 비어있음
2. **불필요한 네비게이션**: 디테일을 볼 때마다 전체 화면 전환 필요
3. **폴더블 최적화 불가**: 펼친 상태에서 폰과 동일한 UX 제공
4. **생산성 저하**: 리스트와 디테일을 동시에 볼 수 없음

---

## 해결책: WindowSizeClass 기반 적응형 레이아웃

```kotlin
@Composable
fun AdaptiveEmailApp() {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> {
            // 폰: 기존과 동일한 단일 화면 방식
            if (selectedEmail == null) {
                EmailList(onEmailClick = { selectedEmail = it })
            } else {
                EmailDetail(email = selectedEmail!!, onBack = { selectedEmail = null })
            }
        }
        else -> {
            // 태블릿/폴더블: 리스트와 디테일 동시 표시
            Row(modifier = Modifier.fillMaxSize()) {
                EmailList(
                    modifier = Modifier.weight(0.4f),
                    onEmailClick = { selectedEmail = it }
                )
                EmailDetail(
                    modifier = Modifier.weight(0.6f),
                    email = selectedEmail
                )
            }
        }
    }
}
```

---

## 폴더블 디바이스 지원

폴더블 기기에서는 힌지 위치를 고려해야 합니다.

```kotlin
val windowAdaptiveInfo = currentWindowAdaptiveInfo()
val foldingFeature = windowAdaptiveInfo.windowPosture.foldingFeatures.firstOrNull()

if (foldingFeature != null && foldingFeature.state == FoldingFeature.State.HALF_OPENED) {
    // 반 접힌 상태 (탁자 모드)
    TableTopLayout()
} else {
    // 일반 상태
    NormalLayout()
}
```

---

## 테스트 방법

### Android Studio Resizable Emulator

1. **Device Manager** 열기
2. **Create Device** > **Phone** > **Resizable (Experimental)** 선택
3. 에뮬레이터 실행 후 화면 크기 드래그로 조절

### 폴더블 에뮬레이터

1. **Device Manager** > **Create Device**
2. **Phone** > **7.6" Fold-in with outer display** 선택
3. 에뮬레이터 실행 후 접기/펼치기 테스트

### Configuration 변경

```kotlin
// Preview에서 다양한 화면 크기 테스트
@Preview(device = Devices.PHONE)
@Preview(device = Devices.FOLDABLE)
@Preview(device = Devices.TABLET)
@Composable
fun PreviewAdaptiveLayout() {
    AdaptiveScreen()
}
```

---

## 연습 문제

### Practice 1: WindowSizeClass 표시하기
- 현재 화면의 WindowSizeClass를 화면에 표시
- `currentWindowAdaptiveInfo()` 사용법 익히기

### Practice 2: 화면 크기별 레이아웃 분기
- Compact에서는 Column, Expanded에서는 Row 사용
- 조건부 레이아웃 구현 연습

### Practice 3: NavigationSuiteScaffold 구현
- 화면 크기에 따라 BottomNav/NavigationRail 자동 전환
- 네비게이션 아이템 추가 및 선택 상태 관리

---

## 주의사항

1. **너비 우선**: 대부분의 경우 `windowWidthSizeClass`만 고려해도 충분
2. **상태 보존**: 화면 크기 변경 시 상태가 유지되도록 `rememberSaveable` 사용
3. **테스트 필수**: 다양한 화면 크기에서 반드시 테스트
4. **점진적 적용**: 한 번에 모든 화면을 적응형으로 바꾸지 말고 핵심 화면부터 적용

---

## 다음 학습

- `ListDetailPaneScaffold`를 활용한 고급 리스트-디테일 패턴
- `SupportingPaneScaffold`로 보조 패널 구현
- `NavigableListDetailPaneScaffold`로 복잡한 네비게이션 플로우 처리

---

## 참고 자료

- [Build adaptive apps - Android Developers](https://developer.android.com/develop/ui/compose/build-adaptive-apps)
- [Use window size classes - Android Developers](https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes)
- [Compose Adaptive Layouts 1.2 Beta - Android Blog](https://android-developers.googleblog.com/2025/09/unfold-new-possibilities-with-compose-adaptive-layouts-1-2-beta.html)
- [NavigableListDetailPaneScaffold - Droidcon](https://www.droidcon.com/2025/06/16/mastering-adaptive-uis-in-jetpack-compose-a-dive-into-navigablelistdetailpanescaffold/)
