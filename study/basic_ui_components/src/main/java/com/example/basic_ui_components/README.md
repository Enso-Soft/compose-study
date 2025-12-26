# 기본 UI 컴포넌트 학습

## 개념

Compose의 기본 UI 컴포넌트는 모든 화면의 **빌딩 블록**입니다. Text, Button, TextField, Icon을 조합하여 복잡한 UI를 구성합니다.

---

## 핵심 컴포넌트

### 1. Text - 텍스트 표시

```kotlin
// 기본 사용
Text("Hello, World!")

// 스타일 적용
Text(
    text = "제목",
    style = MaterialTheme.typography.headlineMedium,
    color = MaterialTheme.colorScheme.primary,
    fontWeight = FontWeight.Bold
)

// 커스텀 스타일
Text(
    text = "커스텀",
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    color = Color.Red,
    textAlign = TextAlign.Center
)
```

**주요 파라미터:**
- `text`: 표시할 문자열
- `style`: MaterialTheme.typography에서 선택
- `color`: 텍스트 색상
- `fontSize`, `fontWeight`: 글꼴 크기와 두께
- `textAlign`: 정렬 (Start, Center, End)
- `maxLines`, `overflow`: 긴 텍스트 처리

### 2. Button - 클릭 가능한 버튼

```kotlin
// 기본 버튼
Button(onClick = { /* 클릭 시 동작 */ }) {
    Text("클릭")
}

// 버튼 종류
Button(onClick = { })       { Text("Filled") }
OutlinedButton(onClick = { }) { Text("Outlined") }
TextButton(onClick = { })     { Text("Text") }

// 아이콘 버튼
IconButton(onClick = { }) {
    Icon(Icons.Default.Favorite, contentDescription = "좋아요")
}

// 비활성화
Button(
    onClick = { },
    enabled = false
) {
    Text("비활성")
}
```

**주요 파라미터:**
- `onClick`: 클릭 시 실행할 람다 (필수!)
- `enabled`: 활성화 여부
- `colors`: ButtonDefaults.buttonColors()로 커스텀
- `content`: 버튼 내부 컨텐츠 (후행 람다)

### 3. TextField - 텍스트 입력

```kotlin
// 필수 패턴: 상태 + value + onValueChange
var text by remember { mutableStateOf("") }

TextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("이름") },
    placeholder = { Text("이름을 입력하세요") }
)

// Outlined 스타일
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("이메일") },
    leadingIcon = { Icon(Icons.Default.Email, null) }
)
```

**주요 파라미터:**
- `value`: 현재 입력값 (상태에서 읽음)
- `onValueChange`: 입력 변경 시 호출 (상태 업데이트)
- `label`: 필드 레이블
- `placeholder`: 빈 상태일 때 힌트
- `leadingIcon`, `trailingIcon`: 앞/뒤 아이콘
- `singleLine`: 한 줄 입력 제한
- `keyboardOptions`: 키보드 타입 설정

### 4. Icon - 아이콘 표시

```kotlin
// Material Icons
Icon(
    imageVector = Icons.Default.Home,
    contentDescription = "홈",  // 접근성 필수!
    tint = Color.Blue
)

// 다양한 아이콘
Icon(Icons.Default.Favorite, "좋아요")
Icon(Icons.Default.Settings, "설정")
Icon(Icons.Default.Search, "검색")
Icon(Icons.Default.Add, "추가")
```

**주요 파라미터:**
- `imageVector`: Icons.Default.XXX 또는 커스텀 벡터
- `contentDescription`: 접근성 설명 (필수!)
- `tint`: 아이콘 색상
- `modifier`: 크기, 패딩 등

---

## 문제 상황: 컴포넌트 사용 실수

### 잘못된 코드 예시

```kotlin
// ❌ 에러 1: TextField에 상태 연결 안 함
TextField(
    value = "",  // 항상 빈 문자열!
    onValueChange = { }  // 아무것도 안 함!
)
// 결과: 타이핑해도 아무 글자도 안 보임

// ❌ 에러 2: Button에 onClick 누락
Button {  // onClick 없음!
    Text("클릭")
}
// 결과: 컴파일 에러

// ❌ 에러 3: Icon에 contentDescription 누락
Icon(Icons.Default.Home)  // 접근성 문제!
// 결과: 경고 및 접근성 이슈
```

---

## 해결책: 올바른 컴포넌트 사용

### 올바른 코드

```kotlin
// ✅ TextField: 상태 연결
var text by remember { mutableStateOf("") }
TextField(
    value = text,
    onValueChange = { text = it }  // 상태 업데이트!
)

// ✅ Button: onClick 명시
Button(onClick = { count++ }) {
    Text("클릭")
}

// ✅ Icon: contentDescription 제공
Icon(
    imageVector = Icons.Default.Home,
    contentDescription = "홈으로 이동"
)
```

---

## 사용 시나리오

### 로그인 폼 예제

```kotlin
@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, null) }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { login(email, password) },
            enabled = email.isNotEmpty() && password.isNotEmpty()
        ) {
            Text("로그인")
        }
    }
}
```

---

## 주의사항

1. **TextField 상태 필수**
   - value와 onValueChange 없이는 입력 불가
   - remember로 상태 관리 필수

2. **Icon contentDescription**
   - 접근성을 위해 항상 제공
   - 순수 장식용이면 null 가능

3. **Button onClick 필수**
   - onClick 파라미터는 필수
   - 빈 람다라도 제공해야 함

4. **MaterialTheme 활용**
   - 직접 색상/폰트 지정보다 MaterialTheme 사용 권장
   - 다크모드 등 테마 대응 자동화

---

## 연습 문제

### 연습 1: Text 스타일링
다양한 Text 스타일을 적용해보세요.

### 연습 2: Button과 카운터
Button 클릭으로 카운터를 구현해보세요.

### 연습 3: TextField 입력 폼
입력한 텍스트를 실시간으로 표시해보세요.

---

## 다음 학습

- **Layout & Modifier**: Column, Row, Box로 컴포넌트 배치
- **Modifier**: padding, background, size 등 스타일링
