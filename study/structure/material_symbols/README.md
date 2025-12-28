# Material Symbols 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| Compose 기본 | Composable 함수와 기본 UI 구성 | [📚 학습하기](../../basics/composable_function/README.md) |
| Modifier | 크기, 색상 등 UI 수정자 | [📚 학습하기](../../layout/layout_and_modifier/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Material Symbols**는 Google이 제공하는 새로운 아이콘 시스템으로, 기존 Material Icons 라이브러리를 대체합니다.

> Material Symbols는 '맞춤 정장'과 같습니다.
> - 기존 Material Icons: 기성복 - 정해진 디자인만 사용 가능
> - Material Symbols: 맞춤 정장 - 두께(Weight), 채움(Fill), 광택(Grade) 등을 자유롭게 조절 가능

---

## 핵심 특징

### 1. 3가지 스타일

| 스타일 | 특징 | 적합한 상황 |
|--------|------|-------------|
| **Outlined** | 외곽선만 있는 깔끔한 스타일 | 일반적인 앱, 미니멀 디자인 |
| **Rounded** | 모서리가 둥근 부드러운 스타일 | 친근한 느낌의 앱, 어린이용 앱 |
| **Sharp** | 날카로운 모서리의 현대적 스타일 | 전문적인 앱, 비즈니스 앱 |

### 2. 가변 폰트 축 (Variable Font Axes)

Material Symbols는 4가지 축으로 아이콘을 커스터마이징할 수 있습니다:

| 축 | 범위 | 기본값 | 설명 |
|----|------|--------|------|
| **Weight** | 100 ~ 700 | 400 | 선 두께 (Thin ~ Bold) |
| **Fill** | 0 ~ 100 | 0 | 채움 정도 (빈 ~ 가득 참) |
| **Grade** | -50 ~ 200 | 0 | 시각적 무게감 |
| **Optical Size** | 20 ~ 48 | 24 | 표시 크기 최적화 (dp) |

### 3. 2,500개 이상의 아이콘

모든 종류의 앱에 필요한 아이콘을 포함합니다:
- 네비게이션: home, menu, arrow_back, close
- 액션: add, delete, edit, search, share
- 컨텐츠: favorite, bookmark, star, check
- 기타: settings, notifications, account_circle 등

---

## 문제 상황: 기존 Material Icons의 한계

### 시나리오

네비게이션 바와 설정 화면에 다양한 아이콘을 사용하는 앱을 개발하고 있습니다. 기존 `material-icons-extended` 라이브러리를 사용했더니 여러 문제가 발생했습니다.

### 문제 1: 빌드 시간 급증

```kotlin
// build.gradle.kts
dependencies {
    // 이 한 줄이 빌드 시간을 크게 증가시킵니다!
    implementation("androidx.compose.material:material-icons-extended:1.x.x")
}
```

`material-icons-extended` 라이브러리는 2,500개 이상의 모든 아이콘을 포함합니다. 실제로 앱에서 사용하는 아이콘은 10~20개인데, 나머지 2,400개도 함께 빌드되어 **빌드 시간이 크게 증가**합니다.

### 문제 2: 라이브러리 지원 중단

Google은 Material Icons 라이브러리의 업데이트를 중단했습니다:

```kotlin
// 기존 방식 - 더 이상 권장하지 않음
Icon(
    imageVector = Icons.Filled.ArrowBack,  // 이 API는 업데이트되지 않습니다
    contentDescription = "뒤로"
)

// AutoMirrored 마이그레이션 필요
Icon(
    imageVector = Icons.AutoMirrored.Filled.ArrowBack,  // RTL 지원을 위한 마이그레이션
    contentDescription = "뒤로"
)
```

### 문제 3: 커스터마이징 불가

```kotlin
// 아이콘 두께를 조절하고 싶지만... 불가능!
Icon(
    imageVector = Icons.Filled.Home,
    contentDescription = "홈"
    // weight = ??? 파라미터가 없음!
    // fill = ??? 채우기 조절 불가!
)
```

기존 Material Icons는 정적인 이미지이므로:
- 선 두께 조절 불가
- 채움/빈 스타일 전환 불가
- 애니메이션 효과 제한적

---

## 해결책: Material Symbols 사용

### 다운로드 방법

1. [Google Fonts Icons](https://fonts.google.com/icons) 접속
2. 원하는 아이콘 검색 (예: "home")
3. 스타일 선택 (Outlined, Rounded, Sharp)
4. **Android** 탭 클릭
5. XML 파일 다운로드
6. `res/drawable/` 폴더에 추가

### 기본 사용법

```kotlin
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun HomeIcon() {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_home),
        contentDescription = "홈"
    )
}

// 또는 painterResource 사용
@Composable
fun SettingsIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_settings),
        contentDescription = "설정"
    )
}
```

### 색상 적용 (Tint)

```kotlin
Icon(
    imageVector = ImageVector.vectorResource(id = R.drawable.ic_favorite),
    contentDescription = "좋아요",
    tint = MaterialTheme.colorScheme.primary  // 테마 색상 사용
)

Icon(
    imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
    contentDescription = "오류",
    tint = MaterialTheme.colorScheme.error  // 에러 색상
)
```

### 크기 조절

```kotlin
Icon(
    imageVector = ImageVector.vectorResource(id = R.drawable.ic_home),
    contentDescription = "홈",
    modifier = Modifier.size(24.dp)  // 기본 크기
)

Icon(
    imageVector = ImageVector.vectorResource(id = R.drawable.ic_home),
    contentDescription = "홈",
    modifier = Modifier.size(48.dp)  // 큰 크기
)
```

---

## 사용 시나리오

### 1. 네비게이션 바

```kotlin
@Composable
fun BottomNavBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = if (selectedItem == 0) R.drawable.ic_home_filled
                             else R.drawable.ic_home_outlined
                    ),
                    contentDescription = "홈"
                )
            },
            label = { Text("홈") }
        )
        // ... 다른 아이템들
    }
}
```

### 2. 앱바 액션

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar() {
    TopAppBar(
        title = { Text("검색") },
        navigationIcon = {
            IconButton(onClick = { /* 뒤로가기 */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    contentDescription = "뒤로"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* 검색 */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    contentDescription = "검색"
                )
            }
            IconButton(onClick = { /* 더보기 */ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_more_vert),
                    contentDescription = "더보기"
                )
            }
        }
    )
}
```

### 3. 설정 메뉴

```kotlin
@Composable
fun SettingsMenuItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

// 사용 예시
SettingsMenuItem(
    iconRes = R.drawable.ic_notifications,
    title = "알림 설정",
    onClick = { /* 알림 설정 화면으로 이동 */ }
)
```

---

## 주의사항

### 1. 아이콘 파일 네이밍

```
권장: ic_home.xml, ic_settings.xml, ic_arrow_back.xml
피하기: home.xml, Settings.xml, arrow-back.xml
```

- `ic_` 접두사 사용
- 소문자와 언더스코어만 사용
- 하이픈(-) 사용 금지

### 2. RTL (Right-to-Left) 지원

방향성이 있는 아이콘은 RTL 언어에서 미러링이 필요합니다:

```xml
<!-- 아이콘 XML에 autoMirrored 속성 추가 -->
<vector
    android:autoMirrored="true"
    ...>
</vector>
```

미러링이 필요한 아이콘:
- arrow_back, arrow_forward
- chevron_left, chevron_right
- send, reply

### 3. 접근성 (Accessibility)

```kotlin
// 장식용 아이콘 (텍스트와 함께 사용)
Icon(
    imageVector = ImageVector.vectorResource(R.drawable.ic_star),
    contentDescription = null  // null로 설정하여 TalkBack이 무시
)

// 의미 있는 아이콘 (단독 사용)
IconButton(onClick = { /* 삭제 */ }) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
        contentDescription = "삭제"  // 반드시 설명 제공
    )
}
```

### 4. 성능 최적화

```kotlin
// 자주 사용하는 아이콘은 remember로 캐싱
@Composable
fun OptimizedIcon() {
    val homeIcon = remember {
        ImageVector.vectorResource(R.drawable.ic_home)
    }

    Icon(
        imageVector = homeIcon,
        contentDescription = "홈"
    )
}
```

---

## 고급: Variable Font 사용 (선택적)

Material Symbols 가변 폰트를 사용하면 Weight, Fill, Grade, Optical Size를 동적으로 조절할 수 있습니다.

```kotlin
// res/font/에 .ttf 파일 추가 후
val symbolFont = FontFamily(
    Font(
        R.font.material_symbols_rounded,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400),
            FontVariation.Setting("FILL", 0f),
            FontVariation.Setting("GRAD", 0f),
            FontVariation.Setting("opsz", 24f)
        )
    )
)

// Text로 아이콘 표시 (유니코드 사용)
Text(
    text = "\uE88A",  // home 아이콘의 유니코드
    fontFamily = symbolFont,
    fontSize = 24.sp
)
```

> 가변 폰트 사용은 복잡하므로, 대부분의 경우 Vector Drawable 방식을 권장합니다.

---

## 연습 문제

### 연습 1: 기본 아이콘 표시 (쉬움)

설정 화면의 메뉴 아이템을 만들어보세요. 각 메뉴는 아이콘과 텍스트로 구성됩니다.

**요구사항:**
- Row 안에 Icon과 Text 배치
- ImageVector.vectorResource() 사용
- 적절한 contentDescription 제공

**힌트:**
- 제공된 R.drawable.ic_settings 리소스 사용
- Modifier.size(24.dp)로 아이콘 크기 설정
- Spacer로 아이콘과 텍스트 사이 간격 추가

### 연습 2: 스타일별 아이콘 갤러리 (중간)

Outlined, Rounded, Sharp 세 가지 스타일의 아이콘을 나란히 보여주는 비교 갤러리를 만들어보세요.

**요구사항:**
- Row로 가로 배치
- 각 스타일 아래에 라벨 표시
- 선택 가능하게 만들기 (클릭 시 테두리 표시)

**힌트:**
- remember { mutableStateOf } 로 선택 상태 관리
- Card 또는 Surface로 선택 효과 표현
- Column으로 아이콘과 라벨 수직 배치

### 연습 3: 동적 테마 아이콘 (어려움)

라이트/다크 모드에 따라 아이콘 색상이 자동으로 변경되고, Slider로 아이콘 크기를 조절할 수 있는 설정 화면을 만들어보세요.

**요구사항:**
- MaterialTheme.colorScheme과 연동
- Slider로 아이콘 크기 조절 (20dp ~ 48dp)
- 다크/라이트 모드 전환 버튼
- 여러 아이콘 동시 표시

**힌트:**
- animateDpAsState로 부드러운 크기 전환
- isSystemInDarkTheme() 또는 수동 토글
- LocalContentColor 활용

---

## 다음 학습

| 주제 | 설명 | 바로가기 |
|------|------|---------|
| **Scaffold와 테마** | Material Theme과 Scaffold 조합 | [학습하기](../scaffold_and_theming/README.md) |
| **NavigationBar** | 하단 네비게이션 바 구현 | [학습하기](../navigation_bar/README.md) |
| **TopAppBar** | 상단 앱바 구현 | [학습하기](../app_bar/README.md) |

---

## 참고 자료

- [Material Symbols - Google Fonts](https://fonts.google.com/icons)
- [Icons in Compose - Android Developers](https://developer.android.com/develop/ui/compose/graphics/images/material)
- [Material Symbols Guide - Google Developers](https://developers.google.com/fonts/docs/material_symbols)
- [Material Design Icons GitHub](https://github.com/google/material-design-icons)
