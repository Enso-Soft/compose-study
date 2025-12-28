# Progress Indicator 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | Text, Button 등 기본 UI 컴포넌트 사용법 | [📚 학습하기](../../../layout/basic_ui_components/README.md) |

> 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

Progress Indicator는 **작업이 진행 중임을 사용자에게 시각적으로 알려주는 컴포넌트**입니다.
데이터를 불러오거나 파일을 다운로드하는 동안 사용자에게 "잠시만 기다려 주세요"라는 메시지를 전달합니다.

> Progress Indicator는 '신호등'과 같습니다:
> - 빨간불(로딩 중): "잠시만 기다리세요"
> - 초록불(완료): "이제 사용할 수 있습니다"

## 핵심 특징

1. **두 가지 형태**
   - `CircularProgressIndicator`: 원형 로딩 표시
   - `LinearProgressIndicator`: 가로 막대 형태의 진행바

2. **두 가지 모드**
   - `Indeterminate` (무한 로딩): 언제 끝날지 모를 때 사용
   - `Determinate` (진행률 표시): 몇 퍼센트 완료되었는지 표시

3. **간단한 사용법**
   - 파라미터 없이 호출하면 무한 로딩
   - `progress` 파라미터로 진행률 표시 (0.0 ~ 1.0)

---

## 문제 상황: 로딩 중 빈 화면

### 시나리오

뉴스 앱을 만들었습니다. 앱을 열면 서버에서 뉴스 목록을 가져옵니다.
네트워크 요청은 2-3초 정도 걸립니다.

**문제**: 그 동안 화면에 아무것도 표시되지 않습니다.

### 잘못된 코드 예시

```kotlin
@Composable
fun NewsScreen() {
    var newsList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000) // 서버 응답 시뮬레이션
        newsList = listOf("뉴스 1", "뉴스 2", "뉴스 3")
        isLoading = false
    }

    // 문제: 로딩 중에 아무것도 표시하지 않음!
    Column {
        newsList.forEach { news ->
            Text(news)
        }
        // isLoading == true 일 때 빈 화면...
    }
}
```

### 발생하는 문제점

1. **사용자 혼란**: "앱이 멈춘 건가?" 하고 생각합니다
2. **앱 종료**: 답답해서 앱을 강제 종료할 수 있습니다
3. **나쁜 사용자 경험**: 전문적이지 않은 앱처럼 보입니다

---

## 해결책: Progress Indicator 사용

### 올바른 코드

```kotlin
@Composable
fun NewsScreen() {
    var newsList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        newsList = listOf("뉴스 1", "뉴스 2", "뉴스 3")
        isLoading = false
    }

    // 해결: 로딩 중에 Progress Indicator 표시!
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            newsList.isNotEmpty() -> NewsList(newsList)
            else -> Text("데이터가 없습니다")
        }
    }
}
```

### 해결되는 이유

- 사용자가 "아, 지금 데이터를 불러오고 있구나"라고 인식합니다
- 앱이 정상적으로 동작하고 있다는 시각적 피드백을 제공합니다
- 전문적인 앱처럼 보입니다

---

## 기본 사용법

### 1. Indeterminate (무한 로딩) - 가장 간단!

```kotlin
// 원형 로딩
CircularProgressIndicator()

// 가로 막대 로딩
LinearProgressIndicator()
```

> 언제 사용하나요?
> - API 호출 대기 (언제 끝날지 모름)
> - 검색 결과 로딩
> - 페이지 이동 중

### 2. Determinate (진행률 표시)

```kotlin
// 70% 완료 표시 (원형)
CircularProgressIndicator(
    progress = { 0.7f }  // 0.0 ~ 1.0 사이 값
)

// 70% 완료 표시 (가로 막대)
LinearProgressIndicator(
    progress = { 0.7f }
)
```

> 언제 사용하나요?
> - 파일 다운로드 (45% 완료)
> - 파일 업로드
> - 긴 작업의 단계별 진행

### 3. 커스터마이징

```kotlin
CircularProgressIndicator(
    progress = { 0.7f },
    modifier = Modifier.size(64.dp),           // 크기
    color = MaterialTheme.colorScheme.primary, // 진행바 색상
    trackColor = Color.LightGray,              // 배경 트랙 색상
    strokeWidth = 6.dp                         // 두께
)

LinearProgressIndicator(
    progress = { 0.7f },
    modifier = Modifier.fillMaxWidth(),
    color = Color.Green,
    trackColor = Color.Gray
)
```

---

## 사용 시나리오

1. **API 호출 대기** - Indeterminate 사용
   ```kotlin
   if (isLoading) CircularProgressIndicator()
   ```

2. **파일 다운로드** - Determinate 사용
   ```kotlin
   LinearProgressIndicator(progress = { downloadProgress })
   Text("${(downloadProgress * 100).toInt()}%")
   ```

3. **버튼 내부 로딩** - 작은 크기로
   ```kotlin
   Button(onClick = { }, enabled = !isLoading) {
       if (isLoading) {
           CircularProgressIndicator(
               modifier = Modifier.size(16.dp),
               strokeWidth = 2.dp
           )
       } else {
           Text("제출")
       }
   }
   ```

---

## 주의사항

- **Indeterminate**: 진행률을 알 수 없을 때 (API 호출, 검색 등)
- **Determinate**: 진행률을 알 때 (파일 다운로드, 단계별 작업 등)
- **progress 값**: 반드시 0.0 ~ 1.0 사이여야 합니다
- **람다 형태**: `progress = { value }` 형태로 전달합니다 (최신 API)

---

## 연습 문제

### 연습 1: 간단한 로딩 표시 - 쉬움

버튼을 누르면 3초 동안 로딩 표시 후 "로딩 완료!" 메시지를 보여주세요.

**힌트**:
1. `isLoading` 상태를 `remember { mutableStateOf(false) }`로 관리
2. 버튼 클릭 시 `isLoading = true`
3. `LaunchedEffect`로 3초 후 `isLoading = false`
4. `if (isLoading) CircularProgressIndicator()`

### 연습 2: 진행률 표시 - 중간

버튼을 누르면 0%에서 100%까지 진행률을 표시하세요.

**힌트**:
1. `progress` 상태를 `remember { mutableFloatStateOf(0f) }`로 관리
2. `rememberCoroutineScope()`로 코루틴 실행
3. 반복문에서 progress를 조금씩 증가
4. `LinearProgressIndicator(progress = { progress })`
5. `Text("${(progress * 100).toInt()}%")`로 퍼센트 표시

### 연습 3: 파일 다운로드 시뮬레이션 - 어려움

파일 다운로드 UI를 만들어보세요:
- "다운로드" 버튼 클릭 시 다운로드 시작
- 진행률 바 + 퍼센트 + 파일 크기 표시 (예: "4.5MB / 10MB")
- 완료 시 체크 아이콘과 "다운로드 완료!" 메시지

**힌트**:
1. 상태 enum 만들기: `Idle`, `Downloading`, `Completed`
2. `when(state)` 문으로 UI 분기
3. 완료 시 `Icons.Default.CheckCircle` 아이콘 사용

---

## 다음 학습

- `Button`: 다양한 버튼 스타일
- `LaunchedEffect`: 비동기 작업 실행
- `rememberCoroutineScope`: 이벤트에서 코루틴 실행
