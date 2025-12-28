# 기본 UI 컴포넌트 완전 가이드

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기                                                                                   |
|----------|------|----------------------------------------------------------------------------------------|
| `layout_and_modifier` | Column, Row, Box 레이아웃과 Modifier 체이닝 | [📚 학습하기](../../layout/layout_and_modifier/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개요

Compose의 기본 UI 컴포넌트인 **Text, Button, TextField, Icon**은 모든 화면의 **빌딩 블록**입니다. 이 네 가지 컴포넌트를 마스터하면 대부분의 UI를 구성할 수 있습니다.

| 컴포넌트 | 역할 | View 시스템 대응 |
|---------|------|-----------------|
| Text | 텍스트 표시 | TextView |
| Button | 클릭 가능한 버튼 | Button |
| TextField | 텍스트 입력 | EditText |
| Icon | 아이콘 표시 | ImageView |

---

## 기본 사용법

### Text - 가장 간단한 형태

```kotlin
Text("Hello, World!")
```

### Button - onClick 필수

```kotlin
Button(onClick = { /* 클릭 시 동작 */ }) {
    Text("클릭")
}
```

### TextField - 상태 연결 필수

```kotlin
var text by remember { mutableStateOf("") }
TextField(
    value = text,
    onValueChange = { text = it }
)
```

### Icon - contentDescription 권장

```kotlin
Icon(
    imageVector = Icons.Default.Home,
    contentDescription = "홈"  // 접근성!
)
```

---

## 핵심 기능

### 1. Text 컴포넌트

텍스트를 화면에 표시하는 가장 기본적인 컴포넌트입니다.

#### MaterialTheme Typography 활용

Material Design 3에서 제공하는 미리 정의된 텍스트 스타일을 사용합니다.

```kotlin
// 제목 스타일
Text(
    text = "제목",
    style = MaterialTheme.typography.headlineLarge
)

// 본문 스타일
Text(
    text = "본문 내용입니다.",
    style = MaterialTheme.typography.bodyLarge
)

// 캡션 스타일
Text(
    text = "작은 설명",
    style = MaterialTheme.typography.labelSmall
)
```

**Typography 종류**:
- `displayLarge/Medium/Small`: 가장 큰 제목
- `headlineLarge/Medium/Small`: 섹션 제목
- `titleLarge/Medium/Small`: 작은 제목
- `bodyLarge/Medium/Small`: 본문
- `labelLarge/Medium/Small`: 캡션, 버튼 텍스트

#### 커스텀 스타일링

```kotlin
Text(
    text = "커스텀 스타일",
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    color = Color.Red,
    textAlign = TextAlign.Center,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis
)
```

#### 주요 파라미터

| 파라미터 | 설명 | 예시 |
|---------|------|------|
| `text` | 표시할 문자열 | `"Hello"` |
| `style` | Typography 스타일 | `MaterialTheme.typography.bodyLarge` |
| `color` | 텍스트 색상 | `Color.Red`, `MaterialTheme.colorScheme.primary` |
| `fontSize` | 글꼴 크기 | `16.sp` |
| `fontWeight` | 글꼴 두께 | `FontWeight.Bold` |
| `textAlign` | 정렬 | `TextAlign.Center` |
| `maxLines` | 최대 줄 수 | `2` |
| `overflow` | 넘침 처리 | `TextOverflow.Ellipsis` |

---

### 2. Button 컴포넌트

사용자 클릭을 받는 인터랙티브 컴포넌트입니다.

#### Material 3 버튼 종류 (5가지)

```kotlin
// 1. Filled Button (기본, 가장 강조)
Button(onClick = { }) {
    Text("Primary Action")
}

// 2. Filled Tonal Button (중간 강조)
FilledTonalButton(onClick = { }) {
    Text("Secondary Action")
}

// 3. Outlined Button (테두리만)
OutlinedButton(onClick = { }) {
    Text("Outlined")
}

// 4. Text Button (텍스트만)
TextButton(onClick = { }) {
    Text("Text Only")
}

// 5. Elevated Button (그림자 효과)
ElevatedButton(onClick = { }) {
    Text("Elevated")
}
```

**언제 어떤 버튼을 쓸까?**
- `Button`: 가장 중요한 액션 (저장, 확인)
- `FilledTonalButton`: 보조 액션
- `OutlinedButton`: 대안 액션 (취소)
- `TextButton`: 덜 중요한 액션 (건너뛰기)
- `ElevatedButton`: 배경과 구분 필요 시

#### IconButton

아이콘만 있는 버튼입니다.

```kotlin
IconButton(onClick = { }) {
    Icon(Icons.Default.Favorite, contentDescription = "좋아요")
}
```

#### 아이콘 + 텍스트 버튼

```kotlin
Button(onClick = { }) {
    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
    Spacer(Modifier.width(8.dp))
    Text("전송")
}
```

#### 비활성화 상태

```kotlin
Button(
    onClick = { },
    enabled = false  // 비활성화
) {
    Text("비활성")
}
```

#### 주요 파라미터

| 파라미터 | 설명 | 예시 |
|---------|------|------|
| `onClick` | 클릭 시 실행 (필수!) | `{ count++ }` |
| `enabled` | 활성화 여부 | `true` / `false` |
| `colors` | 버튼 색상 | `ButtonDefaults.buttonColors()` |
| `content` | 버튼 내용 (후행 람다) | `{ Text("클릭") }` |

---

### 3. TextField 컴포넌트

사용자로부터 텍스트 입력을 받는 컴포넌트입니다.

#### 필수 패턴: 상태 연결

TextField는 **value**와 **onValueChange**를 통해 상태와 연결해야 합니다.

```kotlin
var text by remember { mutableStateOf("") }

TextField(
    value = text,                    // 현재 값
    onValueChange = { text = it },   // 값 변경 시 상태 업데이트
    label = { Text("이름") }
)
```

#### TextField vs OutlinedTextField

```kotlin
// Filled 스타일 (배경색 있음)
TextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("Filled") }
)

// Outlined 스타일 (테두리만)
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("Outlined") }
)
```

#### 아이콘 추가

```kotlin
OutlinedTextField(
    value = email,
    onValueChange = { email = it },
    label = { Text("이메일") },
    leadingIcon = { Icon(Icons.Default.Email, null) },  // 앞 아이콘
    trailingIcon = { Icon(Icons.Default.Clear, null) }  // 뒤 아이콘
)
```

#### 한 줄 입력 제한

```kotlin
TextField(
    value = text,
    onValueChange = { text = it },
    singleLine = true  // 한 줄만 입력 가능
)
```

#### 주요 파라미터

| 파라미터 | 설명 | 예시 |
|---------|------|------|
| `value` | 현재 입력값 (필수!) | `text` |
| `onValueChange` | 입력 변경 콜백 (필수!) | `{ text = it }` |
| `label` | 필드 레이블 | `{ Text("이름") }` |
| `placeholder` | 빈 상태 힌트 | `{ Text("입력하세요") }` |
| `leadingIcon` | 앞 아이콘 | `{ Icon(...) }` |
| `trailingIcon` | 뒤 아이콘 | `{ Icon(...) }` |
| `singleLine` | 한 줄 제한 | `true` |
| `enabled` | 활성화 여부 | `true` |
| `isError` | 에러 상태 | `false` |

> **2025년 업데이트**: Material 3 버전 1.4.0부터 `rememberTextFieldState()` 기반의 새로운 API가 도입되었습니다. 이 모듈에서는 기존 `value + onValueChange` 패턴을 다루며, 새 API는 별도 고급 모듈에서 학습합니다.

---

### 4. Icon 컴포넌트

아이콘을 표시하는 컴포넌트입니다.

#### Material Icons 사용

```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

Icon(
    imageVector = Icons.Default.Home,
    contentDescription = "홈"
)
```

#### 자주 쓰는 아이콘

| 아이콘 | 이름 | 용도 |
|--------|------|------|
| Icons.Default.Home | 홈 | 홈 화면 |
| Icons.Default.Search | 검색 | 검색 기능 |
| Icons.Default.Settings | 설정 | 설정 화면 |
| Icons.Default.Person | 사람 | 프로필 |
| Icons.Default.Favorite | 하트 | 좋아요 |
| Icons.Default.Add | 더하기 | 추가 |
| Icons.Default.Delete | 휴지통 | 삭제 |
| Icons.Default.Edit | 연필 | 편집 |
| Icons.Default.Email | 이메일 | 메일 |
| Icons.Default.Lock | 자물쇠 | 비밀번호 |

#### 색상 변경 (Tint)

```kotlin
Icon(
    imageVector = Icons.Default.Favorite,
    contentDescription = "좋아요",
    tint = Color.Red  // 빨간 하트
)
```

#### 크기 조절

```kotlin
Icon(
    imageVector = Icons.Default.Star,
    contentDescription = "별",
    modifier = Modifier.size(48.dp)  // 48dp 크기
)
```

#### contentDescription (접근성)

```kotlin
// 기능이 있는 아이콘: 설명 필수
Icon(Icons.Default.Delete, contentDescription = "삭제")

// 순수 장식용: null 가능
Icon(Icons.Default.Star, contentDescription = null)
```

---

## 조합 패턴

### 패턴 1: 로그인 폼

```kotlin
@Composable
fun LoginForm(onLogin: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // 이메일 입력
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 비밀번호 입력
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 로그인 버튼
        Button(
            onClick = { onLogin(email, password) },
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인")
        }
    }
}
```

### 패턴 2: 카드 헤더

```kotlin
@Composable
fun CardHeader(title: String, onMoreClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onMoreClick) {
            Icon(Icons.Default.MoreVert, contentDescription = "더보기")
        }
    }
}
```

### 패턴 3: 검색 바

```kotlin
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("검색어를 입력하세요") },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, "지우기")
                }
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}
```

---

## 베스트 프랙티스

### 1. MaterialTheme 활용하기

```kotlin
// 권장: 테마 색상 사용
Text(
    text = "제목",
    color = MaterialTheme.colorScheme.primary
)

// 비권장: 하드코딩
Text(
    text = "제목",
    color = Color(0xFF6200EE)  // 다크모드 대응 불가
)
```

### 2. contentDescription 제공하기

```kotlin
// 권장: 접근성 설명 제공
Icon(Icons.Default.Delete, contentDescription = "삭제")

// 장식용 아이콘은 null
Icon(Icons.Default.Star, contentDescription = null)
```

### 3. 상태는 remember로 관리

```kotlin
// 권장
var count by remember { mutableIntStateOf(0) }

// 비권장: Recomposition마다 초기화됨
var count = 0
```

### 4. 버튼에 적절한 enabled 상태

```kotlin
Button(
    onClick = { submit() },
    enabled = isFormValid  // 폼이 유효할 때만 활성화
) {
    Text("제출")
}
```

---

## 안티패턴

### 1. TextField 상태 미연결

```kotlin
// 잘못된 코드 - 입력이 보이지 않음!
TextField(
    value = "",           // 항상 빈 문자열
    onValueChange = { }   // 아무것도 안 함
)

// 올바른 코드
var text by remember { mutableStateOf("") }
TextField(
    value = text,
    onValueChange = { text = it }
)
```

### 2. Button onClick 누락

```kotlin
// 컴파일 에러!
Button {
    Text("클릭")
}

// 올바른 코드
Button(onClick = { }) {
    Text("클릭")
}
```

### 3. Icon contentDescription 누락

```kotlin
// 경고: 접근성 문제
Icon(Icons.Default.Home)

// 올바른 코드
Icon(Icons.Default.Home, contentDescription = "홈")
```

### 4. 스타일 없는 텍스트 나열

```kotlin
// 문제: 시각적 계층 없음
Text("제목")
Text("본문")
Text("캡션")

// 해결: Typography로 계층 표현
Text("제목", style = MaterialTheme.typography.headlineMedium)
Text("본문", style = MaterialTheme.typography.bodyLarge)
Text("캡션", style = MaterialTheme.typography.labelSmall)
```

---

## 연습 문제

### 연습 1: Text 스타일링
다양한 Typography 스타일을 적용하여 시각적 계층을 만들어보세요.

### 연습 2: Button과 카운터
여러 종류의 Button을 사용하여 카운터를 구현해보세요.

### 연습 3: TextField 입력 폼
이름, 이메일, 메시지를 입력받는 폼을 만들고, 모든 필드가 채워져야 전송 버튼이 활성화되도록 구현해보세요.

---

## 다음 학습

- **Layout & Modifier**: Column, Row, Box로 컴포넌트 배치하기
- **Modifier 심화**: padding, background, size 등 스타일링
- **State 관리**: remember, rememberSaveable 심화

---

## 참고 자료

- [Android Developers - Text](https://developer.android.com/develop/ui/compose/text)
- [Android Developers - Button](https://developer.android.com/develop/ui/compose/components/button)
- [Android Developers - TextField](https://developer.android.com/develop/ui/compose/text/user-input)
- [Material Design 3 - Components](https://m3.material.io/components)
