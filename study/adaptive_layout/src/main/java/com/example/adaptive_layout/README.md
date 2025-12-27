# Adaptive Layout 학습

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
    // 기본 adaptive 빌딩 블록 (WindowSizeClass 포함)
    implementation("androidx.compose.material3.adaptive:adaptive:1.1.0")

    // 멀티 패인 레이아웃 (ListDetailPaneScaffold 등)
    implementation("androidx.compose.material3.adaptive:adaptive-layout:1.1.0")

    // 적응형 네비게이터
    implementation("androidx.compose.material3.adaptive:adaptive-navigation:1.1.0")

    // 적응형 네비게이션 UI (NavigationSuiteScaffold)
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha15")
}
```

---

## 주요 컴포넌트

### 1. NavigationSuiteScaffold

화면 크기에 따라 네비게이션 UI를 자동으로 변경합니다.

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

### 2. ListDetailPaneScaffold

리스트-디테일 패턴을 화면 크기에 맞게 자동 조정합니다.

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
