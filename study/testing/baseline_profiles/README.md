# Baseline Profiles & Startup Profiles 학습

## 사전 지식

이 모듈을 학습하기 전에 다음 개념을 이해하고 있어야 합니다:

| 사전 지식 | 설명 | 바로가기 |
|----------|------|---------|
| `compose_compiler_metrics` | Compose Compiler Metrics를 통한 성능 분석 | [📚 학습하기](../../testing/compose_compiler_metrics/README.md) |

> 💡 선행 학습이 완료되지 않았다면 위 링크를 먼저 학습하세요.

---

## 개념

**Baseline Profiles**는 앱의 코드 실행 속도를 **약 30% 향상**시키는 프로필 기반 최적화(PGO, Profile-Guided Optimization) 기술입니다.

앱 설치 시 Android Runtime(ART)이 프로필에 정의된 코드 경로를 **AOT(Ahead-of-Time) 컴파일**하여, 첫 실행부터 JIT 컴파일 단계를 건너뛰고 최적화된 성능을 제공합니다.

**Startup Profiles**는 Baseline Profiles의 보완 기술로, d8 덱서에게 어떤 클래스/메서드를 첫 번째 classes.dex 파일에 배치할지 지시하여 앱 시작 시간을 단축합니다.

> **2025년 업데이트**: Compose 1.9부터 내부 최적화로 스크롤 jank가 0.2%로 감소했습니다. 하지만 앱 고유 코드의 최적화를 위해 Baseline Profiles는 여전히 권장됩니다.

## 핵심 특징

1. **AOT 컴파일**: 앱 설치 시점에 미리 네이티브 코드로 컴파일
2. **JIT 컴파일 제거**: 첫 실행부터 해석(interpretation) 모드 불필요
3. **Cloud Profiles 보완**: Google Play의 Cloud Profiles와 독립적으로 동작
4. **30%+ 성능 향상**: Meta, Trello 등 실제 사례에서 입증
5. **Compose 필수**: Compose는 라이브러리로 배포되어 AOT 컴파일 혜택 필요

## 왜 Baseline Profiles가 필요한가?

### Android Runtime(ART) 동작 방식

```
[앱 설치]
    |
    v
[첫 번째 실행]
    |
    +-- 해석(Interpretation) 모드로 코드 실행
    |   (느림 - 바이트코드를 한 줄씩 해석)
    |
    +-- JIT(Just-in-Time) 컴파일러가 자주 실행되는 코드 컴파일
    |   (CPU 리소스 소모 -> UI 버벅거림)
    |
    v
[여러 번 실행 후]
    |
    +-- ART가 프로필 데이터 수집
    |
    +-- 백그라운드에서 AOT 컴파일
    |
    v
[최적화 완료] (며칠 후...)
```

**문제점**: 사용자의 첫 경험이 최악의 성능!

### Baseline Profiles 적용 후

```
[개발자가 Baseline Profile 생성]
    |
    v
[앱 빌드 시 APK/AAB에 포함]
    |
    v
[앱 설치]
    |
    +-- ART가 프로필을 읽고 즉시 AOT 컴파일
    |
    v
[첫 번째 실행부터 최적화된 성능!]
```

## Compose와 Baseline Profiles

### 왜 Compose 앱에 특히 중요한가?

Compose는 **플랫폼 코드가 아닌 라이브러리**로 배포됩니다:

| 코드 유형 | 컴파일 상태 | 첫 실행 성능 |
|----------|------------|-------------|
| Android Platform Code | 이미 AOT 컴파일됨 | 빠름 |
| Compose Library | JIT 컴파일 필요 | 느림 |
| 앱 고유 코드 | JIT 컴파일 필요 | 느림 |

Compose 팀은 라이브러리에 기본 Baseline Profile을 포함하지만, **앱 고유의 코드 경로**는 개발자가 직접 프로필을 생성해야 합니다.

## 성능 향상 사례

| 회사/앱 | 성능 향상 | 적용 영역 |
|---------|----------|----------|
| **Meta (Facebook, Instagram)** | 최대 40% | 앱 시작, 렌더링 |
| **Trello** | 25% | 앱 시작 시간 |
| **Google Calendar** | 20% | 콜드 런치, 느린 프레임 50% 감소 |
| **일반적인 앱** | ~30% | 시작 및 런타임 |

## Baseline Profile 생성 방법

### 1. AGP 8.2 이상: Module Wizard 사용

Android Studio Iguana 이상에서:

1. **File > New > New Module**
2. **Baseline Profile Generator** 템플릿 선택
3. Target application 지정

### 2. build.gradle.kts 설정

```kotlin
// baselineprofile 모듈의 build.gradle.kts
plugins {
    id("com.android.test")
    id("androidx.baselineprofile")
}

android {
    namespace = "com.example.baselineprofile"
    targetProjectPath = ":app"

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6api31") {
            device = "Pixel 6"
            apiLevel = 31
            systemImageSource = "aosp"
        }
    }
}

baselineProfile {
    managedDevices += "pixel6api31"
    useConnectedDevices = false
}

dependencies {
    implementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.benchmark:benchmark-macro-junit4:1.4.1")
}
```

### 3. App 모듈 설정

```kotlin
// app의 build.gradle.kts
plugins {
    id("com.android.application")
    id("androidx.baselineprofile")
}

dependencies {
    implementation("androidx.profileinstaller:profileinstaller:1.4.1")
    baselineProfile(project(":baselineprofile"))
}

baselineProfile {
    automaticGenerationDuringBuild = true
}
```

## BaselineProfileGenerator 작성

```kotlin
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateProfile() {
        rule.collect(
            packageName = "com.example.app",
            includeInStartupProfile = true,  // Startup Profile에도 포함
            profileBlock = {
                // 앱 시작
                startActivityAndWait()

                // Critical User Journeys
                device.findObject(By.text("Login")).click()
                device.waitForIdle()

                // 스크롤
                device.findObject(By.res("content_list"))
                    .scroll(Direction.DOWN, 2f)
                device.waitForIdle()
            }
        )
    }
}
```

## Startup Profiles

Startup Profiles는 DEX 파일 레이아웃을 최적화합니다:

```
[일반 DEX 파일]
classes.dex: 모든 클래스가 섞여 있음
    → 시작 시 여러 DEX 파일 로드 필요

[Startup Profile 적용]
classes.dex: 시작에 필요한 클래스 우선 배치
classes2.dex: 나머지 클래스
    → 첫 번째 DEX만 로드하면 앱 시작 가능
```

```kotlin
rule.collect(
    packageName = "com.example.app",
    includeInStartupProfile = true  // 이 옵션이 핵심!
) {
    startActivityAndWait()
}
```

## Compose에서 ReportDrawn 사용

비동기 데이터 로드 완료 시점을 정확히 보고:

```kotlin
@Composable
fun MainScreen(data: List<Item>) {
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(data) {
        if (data.isNotEmpty()) {
            isReady = true
        }
    }

    // 데이터 로드 완료 시점 보고
    ReportDrawnWhen { isReady }

    LazyColumn {
        items(data) { item ->
            ItemCard(item)
        }
    }
}

// 또는 suspend 함수 완료 후
ReportDrawnAfter {
    repository.loadInitialData()
}
```

## 프로파일 규칙 형식

baseline-prof.txt 파일 형식:

```
# 클래스 포함 (L = 클래스 타입)
Lcom/example/app/MainActivity;
Lcom/example/app/ui/HomeScreen;

# 메서드 포함
# H = Hot (자주 호출)
# S = Startup (시작 시 필요)
# P = Post-startup (시작 직후)
HSPLcom/example/app/MainActivity;->onCreate(Landroid/os/Bundle;)V
HSPLcom/example/app/viewmodel/MainViewModel;-><init>()V

# 와일드카드
Lcom/example/app/ui/**;
```

## 프로파일 생성 명령어

```bash
# Baseline Profile 생성
./gradlew :app:generateBaselineProfile

# 특정 빌드 변형
./gradlew :app:generateReleaseBaselineProfile

# 벤치마크 실행
./gradlew :benchmark:connectedAndroidTest
```

## CI/CD 통합

### GitHub Actions 예시

```yaml
name: Generate Baseline Profile

on:
  push:
    branches: [main]
  workflow_dispatch:

jobs:
  baseline-profile:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Accept Android licenses
        run: yes | $ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager --licenses

      - name: Generate Baseline Profile
        run: ./gradlew :app:generateBaselineProfile

      - name: Upload Profile
        uses: actions/upload-artifact@v4
        with:
          name: baseline-profile
          path: app/src/main/baseline-prof.txt
```

## 벤치마크 결과 해석

```
StartupBenchmarks_withBaselineProfile
timeToInitialDisplayMs   min 229,   median 235,   max 248

StartupBenchmarks_withoutProfile
timeToInitialDisplayMs   min 324,   median 342,   max 358

개선율: (342 - 235) / 342 = 31% 향상
```

## 핵심 지표: TTID와 TTFD

Baseline Profiles의 효과를 측정할 때 두 가지 핵심 지표를 이해해야 합니다:

| 지표 | 의미 | 측정 시점 |
|------|------|----------|
| **TTID** (Time To Initial Display) | 첫 프레임이 그려지는 시간 | 시스템이 자동 측정 |
| **TTFD** (Time To Full Display) | 완전히 상호작용 가능한 상태 | 앱이 `reportFullyDrawn()` 또는 `ReportDrawn` 호출 시 |

Compose에서는 `ReportDrawnWhen`과 `ReportDrawnAfter`를 사용하여 TTFD를 정확히 보고합니다.

## 주의사항

1. **에뮬레이터 제한**: 프로파일 생성은 userdebug/eng 빌드 기기 또는 API 34+ 에뮬레이터 필요
2. **Release 빌드**: 벤치마크는 반드시 Release 빌드로 측정
3. **ProfileInstaller**: Google Play 외 설치(사이드로드, 사내 배포)에서도 프로필 적용을 위해 필수
4. **최소 API**: Baseline Profiles는 Android 7.0 (API 24) 이상 지원
5. **Compose 버전**: Compose 자체에 기본 프로필 포함 (1.6.0+)
6. **Gradle 버전**: AGP 8.0.0 이상 권장, AGP 8.2+ 사용 시 Module Wizard 지원

## 연습 문제

1. **연습 1**: baseline-prof.txt 규칙 작성 (HSP 플래그 이해)
2. **연습 2**: BaselineProfileGenerator 테스트 작성 (Critical User Journey)
3. **연습 3**: Compose에서 ReportDrawn API 활용

## 다음 학습

- Macrobenchmark를 사용한 성능 측정
- R8/ProGuard 최적화
- App Startup 라이브러리
