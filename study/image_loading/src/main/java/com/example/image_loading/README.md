# Image Loading (Coil) 학습

## 개념

**Coil**(Coroutine Image Loader)은 Kotlin 코루틴 기반의 Android 이미지 로딩 라이브러리입니다. Jetpack Compose와 완벽하게 통합되며, 비동기 이미지 로딩, 캐싱, 변환을 간단한 API로 제공합니다.

> "Compose에서 네트워크 이미지를 로드하려면 Coil의 AsyncImage를 사용하세요."

### Coil의 핵심 특징
- **빠름**: 메모리/디스크 캐싱, 다운샘플링, 자동 요청 취소
- **가벼움**: Kotlin, Coroutines, Okio만 의존
- **사용 용이**: Kotlin DSL로 간결한 API
- **현대적**: Compose, Coroutines, OkHttp와 자연스럽게 통합

---

## 왜 Coil인가?

### 다른 라이브러리와 비교

| 특성 | Coil | Glide | Picasso |
|------|------|-------|---------|
| Compose 지원 | 네이티브 | 어댑터 필요 | 미지원 |
| Kotlin 우선 | O | X (Java) | X (Java) |
| 코루틴 기반 | O | X | X |
| 라이브러리 크기 | ~2,000 메소드 | ~8,000 메소드 | ~3,000 메소드 |
| Multiplatform | O (3.x) | X | X |

### 2025년 기준 권장사항
- **Compose 프로젝트**: Coil 3.x 사용 권장
- **기존 View 프로젝트**: Glide 또는 Coil 선택

---

## 핵심 컴포넌트

### 1. AsyncImage (기본, 권장)

```kotlin
AsyncImage(
    model = "https://example.com/image.jpg",
    contentDescription = "이미지 설명",
    modifier = Modifier.size(200.dp)
)
```

**특징**:
- 대부분의 경우에 권장
- 화면 크기에 맞게 자동 다운샘플링
- 메모리/디스크 캐싱 자동 적용

### 2. SubcomposeAsyncImage (상태별 UI)

```kotlin
SubcomposeAsyncImage(
    model = imageUrl,
    contentDescription = null
) {
    val state by painter.state.collectAsState()
    when (state) {
        is AsyncImagePainter.State.Loading -> CircularProgressIndicator()
        is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
        is AsyncImagePainter.State.Error -> Icon(Icons.Default.Error, null)
        else -> {}
    }
}
```

**특징**:
- 로딩/성공/에러 상태별 커스텀 UI 가능
- 서브컴포지션 사용으로 **성능 민감한 UI(LazyList)에서는 비권장**

### 3. rememberAsyncImagePainter

```kotlin
val painter = rememberAsyncImagePainter(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build()
)

Image(
    painter = painter,
    contentDescription = null
)
```

**특징**:
- Painter가 필요할 때 사용
- AsyncImagePainter.state 관찰 가능

---

## 문제 상황: 이미지 로딩 없이 발생하는 문제

### 1. 기본 Image 컴포저블의 한계

```kotlin
// 문제: URL에서 직접 이미지를 로드할 수 없음
Image(
    painter = ???, // URL을 어떻게 Painter로 변환?
    contentDescription = null
)
```

### 2. 수동 비트맵 로딩의 문제

```kotlin
// 절대 하지 마세요!
@Composable
fun BadImageLoading(url: String) {
    // 1. 메인 스레드에서 네트워크 호출 → ANR
    // 2. NetworkOnMainThreadException 발생
    val bitmap = URL(url).openStream().use {
        BitmapFactory.decodeStream(it)
    }

    // 3. 캐싱 없음 → 매번 네트워크 요청
    // 4. 메모리 관리 없음 → OOM 위험
}
```

### 발생하는 문제점

1. **ANR (Application Not Responding)**: 메인 스레드 블로킹
2. **메모리 부족 (OOM)**: 큰 이미지 처리 시 크래시
3. **네트워크 낭비**: 캐싱 없이 매번 다운로드
4. **UX 저하**: 로딩 상태, 에러 처리 없음

---

## 해결책: Coil 사용

### 1. 기본 AsyncImage

```kotlin
AsyncImage(
    model = "https://picsum.photos/400/300",
    contentDescription = "샘플 이미지",
    modifier = Modifier.size(200.dp),
    contentScale = ContentScale.Crop
)
```

### 2. Placeholder와 Error 처리

```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build(),
    placeholder = painterResource(R.drawable.placeholder),
    error = painterResource(R.drawable.error_image),
    contentDescription = null,
    contentScale = ContentScale.Crop
)
```

### 3. 이미지 변환 (Compose 권장 방식)

```kotlin
// 원형 크롭 - Modifier.clip 사용 (권장)
AsyncImage(
    model = imageUrl,
    contentDescription = null,
    modifier = Modifier
        .size(100.dp)
        .clip(CircleShape),
    contentScale = ContentScale.Crop
)

// 둥근 모서리
AsyncImage(
    model = imageUrl,
    contentDescription = null,
    modifier = Modifier
        .size(200.dp)
        .clip(RoundedCornerShape(16.dp)),
    contentScale = ContentScale.Crop
)
```

> **참고**: Coil의 `CircleCropTransformation` 대신 Compose의 `Modifier.clip(CircleShape)`을 사용하는 것이 더 효율적입니다.

---

## ImageLoader 커스터마이징

### 앱 전역 설정 (Application 클래스)

```kotlin
class MyApplication : Application(), SingletonImageLoader.Factory {
    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25) // 메모리의 25%
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02) // 디스크의 2%
                    .build()
            }
            .build()
    }
}
```

### Compose에서 설정 (Compose Multiplatform 권장)

```kotlin
@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    // 앱 컨텐츠...
}
```

---

## 캐싱 전략

### 캐시 레벨

| 레벨 | 설명 | 기본값 |
|------|------|--------|
| 메모리 캐시 | 디코딩된 Bitmap 저장 | 앱 메모리의 25% |
| 디스크 캐시 | 원본 이미지 파일 저장 | 디스크의 2% (또는 250MB) |

### 캐시 정책 제어

```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .memoryCachePolicy(CachePolicy.ENABLED)  // 메모리 캐시 사용
        .diskCachePolicy(CachePolicy.ENABLED)    // 디스크 캐시 사용
        .networkCachePolicy(CachePolicy.ENABLED) // 네트워크 캐시 사용
        .build(),
    contentDescription = null
)
```

### 캐시 무효화

```kotlin
// 특정 이미지 캐시 무효화
imageLoader.memoryCache?.remove(MemoryCache.Key(imageUrl))

// 전체 캐시 클리어
imageLoader.memoryCache?.clear()
imageLoader.diskCache?.clear()
```

---

## SubcomposeAsyncImage 활용

### 상태별 완전 커스터마이징

```kotlin
SubcomposeAsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build(),
    contentDescription = "이미지",
    modifier = Modifier.fillMaxWidth()
) {
    val state by painter.state.collectAsState()

    when (state) {
        is AsyncImagePainter.State.Empty -> {
            // 초기 상태
        }
        is AsyncImagePainter.State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text("로딩 중...", modifier = Modifier.padding(top = 48.dp))
            }
        }
        is AsyncImagePainter.State.Success -> {
            SubcomposeAsyncImageContent()
        }
        is AsyncImagePainter.State.Error -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Error, contentDescription = null)
                Text("이미지 로드 실패")
            }
        }
    }
}
```

### 성능 주의사항

```kotlin
// LazyColumn에서는 SubcomposeAsyncImage 대신 AsyncImage + placeholder 사용
LazyColumn {
    items(imageUrls) { url ->
        // 권장: AsyncImage with placeholder
        AsyncImage(
            model = url,
            placeholder = ColorPainter(Color.LightGray),
            contentDescription = null
        )

        // 비권장: SubcomposeAsyncImage (서브컴포지션 오버헤드)
        // SubcomposeAsyncImage(...)
    }
}
```

---

## 주의사항

### 1. 인터넷 권한 필수
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 2. Coil 3.x 의존성 구조
```kotlin
// Coil 3.x는 네트워킹 라이브러리가 분리됨
implementation("io.coil-kt.coil3:coil-compose:3.0.4")
implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
```

### 3. Compose에서 이미지 변환
- `Modifier.clip(CircleShape)` - 원형 (권장)
- `Modifier.clip(RoundedCornerShape(dp))` - 둥근 모서리 (권장)
- Coil Transformation 클래스 - 비권장 (Compose 외부에서만 사용)

### 4. 이미지 로드 시 contentScale
```kotlin
// ContentScale 옵션
ContentScale.Crop       // 비율 유지, 잘림 허용
ContentScale.Fit        // 비율 유지, 전체 표시
ContentScale.FillBounds // 비율 무시, 영역 채움
ContentScale.Inside     // 축소만 허용
ContentScale.None       // 변환 없음
```

---

## 연습 문제

### 연습 1: 기본 AsyncImage (초급)
- URL에서 이미지 로드
- placeholder와 crossfade 적용
- 힌트: `ImageRequest.Builder` 사용

### 연습 2: 이미지 갤러리 (중급)
- LazyVerticalGrid로 이미지 목록 표시
- 둥근 모서리와 에러 처리
- 힌트: `Modifier.clip(RoundedCornerShape())`

### 연습 3: 상태별 UI 처리 (고급)
- SubcomposeAsyncImage로 커스텀 상태 UI
- 재시도 버튼 구현
- 힌트: `painter.state.collectAsState()`

---

## 다음 학습

이 모듈을 마친 후 다음 주제를 학습하세요:

1. **Paging Compose**: 페이지네이션된 이미지 목록
2. **LazyLayouts**: 효율적인 이미지 리스트 표시
3. **Animation**: 이미지 전환 애니메이션

---

## 참고 자료

- [Coil 공식 문서](https://coil-kt.github.io/coil/)
- [Coil GitHub](https://github.com/coil-kt/coil)
- [Coil Compose 가이드](https://coil-kt.github.io/coil/compose/)
- [Android Developers - 이미지 커스터마이징](https://developer.android.com/develop/ui/compose/graphics/images/customize)
