# Image Loading (Coil) 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `basic_ui_components` | 기본 UI 컴포넌트 (Text, Button, Image 등) | [📚 학습하기](../../layout/basic_ui_components/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Coil**(Coroutine Image Loader)은 Kotlin 코루틴 기반의 Android 이미지 로딩 라이브러리입니다. Jetpack Compose와 네이티브하게 통합되며, 비동기 이미지 로딩, 캐싱, 변환을 간단한 API로 제공합니다.

> "Compose에서 네트워크 이미지를 로드하려면 Coil의 AsyncImage를 사용하세요."

## 핵심 특징

1. **빠름**: 메모리/디스크 캐싱, 다운샘플링, 자동 요청 취소로 최적화 (최신 버전에서 25-40% 성능 향상)
2. **가벼움**: Kotlin, Coroutines, Okio만 의존 (~2,000 메소드)
3. **Compose 네이티브**: AsyncImage로 선언적 이미지 로딩 지원
4. **Multiplatform 지원**: Android, iOS, Desktop, Web 모두 지원 (Coil 3.x)

---

## 왜 Coil인가?

### 라이브러리 비교

| 특성 | Coil | Glide | Picasso |
|------|------|-------|---------|
| Compose 지원 | **네이티브** | 어댑터 필요 | 미지원 |
| Kotlin 우선 | O | X (Java) | X (Java) |
| 코루틴 기반 | O | X | X |
| 라이브러리 크기 | ~2,000 메소드 | ~8,000 메소드 | ~3,000 메소드 |
| Multiplatform | O (3.x) | X | X |

### 2025년 권장사항

- **Compose 프로젝트**: Coil 3.x 사용 권장 (공식 문서에서도 Coil 사용)
- **기존 View 프로젝트**: Glide 또는 Coil 선택

---

## 문제 상황: Compose에서 네트워크 이미지 로딩의 어려움

### 시나리오

앱에서 사용자 프로필 이미지를 URL로 받아와 표시해야 합니다. Compose의 기본 `Image` 컴포저블을 사용하려고 하는데...

### 1. 기본 Image 컴포저블의 한계

```kotlin
// 문제: URL에서 직접 이미지를 로드할 수 없음
Image(
    painter = ???, // URL을 어떻게 Painter로 변환하지?
    contentDescription = null
)
```

`Image`는 `Painter` 타입만 받습니다:
- `painterResource(R.drawable.xxx)` - 로컬 리소스
- `BitmapPainter(bitmap)` - 메모리 비트맵
- `VectorPainter(vector)` - 벡터 이미지

**URL은 지원하지 않습니다!**

### 2. 수동 비트맵 로딩의 문제

```kotlin
// 절대 이렇게 하지 마세요!
@Composable
fun BadImageLoading(url: String) {
    // 1. 메인 스레드에서 네트워크 호출 -> ANR!
    // 2. NetworkOnMainThreadException 발생
    val bitmap = URL(url).openStream().use {
        BitmapFactory.decodeStream(it)
    }

    // 3. 캐싱 없음 -> 매번 네트워크 요청
    // 4. 메모리 관리 없음 -> OOM 위험
}
```

### 발생하는 문제점

| 문제 | 설명 | 영향 |
|------|------|------|
| **ANR** | 메인 스레드 블로킹 | 앱이 응답하지 않음 |
| **OOM** | 큰 이미지 메모리 부족 | 앱 크래시 |
| **네트워크 낭비** | 캐싱 없이 매번 다운로드 | 데이터/배터리 낭비 |
| **UX 저하** | 로딩/에러 상태 처리 없음 | 사용자 경험 악화 |

---

## 해결책: Coil 사용

### 설정

```kotlin
// build.gradle.kts
dependencies {
    // Coil 3.x (2025년 1월 기준 최신 버전: 3.3.0)
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
}

// 중요: Coil 3.x는 Java 11 bytecode를 요구합니다
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
```

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
```

### 1. 기본 AsyncImage (권장)

```kotlin
AsyncImage(
    model = "https://example.com/image.jpg",
    contentDescription = "이미지 설명",
    modifier = Modifier.size(200.dp),
    contentScale = ContentScale.Crop
)
```

**AsyncImage가 해결하는 것**:
- 비동기 네트워크 로딩 (UI 블로킹 없음)
- 자동 메모리/디스크 캐싱
- 화면 크기에 맞게 다운샘플링
- Composable 생명주기에 맞는 자동 취소

### 2. Placeholder와 Error 처리

```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)  // 페이드 인 애니메이션
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

> **팁**: Coil의 `CircleCropTransformation` 대신 Compose의 `Modifier.clip(CircleShape)`을 사용하는 것이 더 효율적입니다.

### 4. SubcomposeAsyncImage (상태별 UI)

```kotlin
SubcomposeAsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build(),
    contentDescription = "이미지"
) {
    val state by painter.state.collectAsState()

    when (state) {
        is AsyncImagePainter.State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text("로딩 중...")
            }
        }
        is AsyncImagePainter.State.Success -> {
            SubcomposeAsyncImageContent()
        }
        is AsyncImagePainter.State.Error -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Error, null, tint = Color.Red)
                Text("로드 실패")
            }
        }
        else -> {}
    }
}
```

**주의**: SubcomposeAsyncImage는 서브컴포지션을 사용하므로 **LazyList에서는 성능상 비권장**입니다. LazyColumn/LazyGrid에서는 `AsyncImage + placeholder`를 사용하세요.

### 5. rememberAsyncImagePainter

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

**사용 시점**: Painter가 직접 필요하거나, AsyncImagePainter.state를 관찰해야 할 때

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

## 주의사항

### 1. 인터넷 권한 필수
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 2. Coil 3.x 의존성 구조
```kotlin
// Coil 3.x는 네트워킹 라이브러리가 분리됨
implementation("io.coil-kt.coil3:coil-compose:3.3.0")
implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
// Compose Multiplatform은 Ktor 사용:
// implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")
```

### 3. Java 11 bytecode 요구사항
```kotlin
// Coil 3.x + Compose 1.8.0 이상은 Java 11 필수
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
```

### 4. Compose에서 이미지 변환
- `Modifier.clip(CircleShape)` - 원형 (권장)
- `Modifier.clip(RoundedCornerShape(dp))` - 둥근 모서리 (권장)
- Coil Transformation 클래스 - Compose에서는 비권장

### 5. ContentScale 옵션
```kotlin
ContentScale.Crop       // 비율 유지, 잘림 허용
ContentScale.Fit        // 비율 유지, 전체 표시
ContentScale.FillBounds // 비율 무시, 영역 채움
ContentScale.Inside     // 축소만 허용
ContentScale.None       // 변환 없음
```

### 6. LazyList 성능 최적화
```kotlin
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

## 연습 문제

### 연습 1: 기본 AsyncImage (초급)

**목표**: URL에서 이미지를 로드하는 기본 AsyncImage 구현

**요구사항**:
1. AsyncImage 사용
2. placeholder로 회색 배경 표시
3. crossfade 애니메이션 적용
4. ContentScale.Crop 사용

**힌트**: `ImageRequest.Builder(context).data(url).crossfade(true).build()`

### 연습 2: 이미지 갤러리 (중급)

**목표**: LazyVerticalGrid로 이미지 갤러리 구현

**요구사항**:
1. 2열 그리드 레이아웃
2. 각 이미지에 둥근 모서리 (8.dp) 적용
3. placeholder와 error 처리
4. 정사각형 비율 유지 (`aspectRatio(1f)`)

**힌트**: `LazyVerticalGrid(columns = GridCells.Fixed(2))`

### 연습 3: 상태별 UI 처리 (고급)

**목표**: SubcomposeAsyncImage로 커스텀 상태 UI 구현

**요구사항**:
1. Loading: CircularProgressIndicator + "로딩 중..." 텍스트
2. Success: 이미지 표시
3. Error: 에러 아이콘 + "로드 실패" + 재시도 버튼

**힌트**: `painter.state.collectAsState()`로 상태 관찰

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
- [Android Developers - 이미지 로딩](https://developer.android.com/develop/ui/compose/graphics/images/loading)
