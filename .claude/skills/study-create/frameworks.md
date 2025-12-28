# 교육 틀 패턴 (Educational Frameworks)

Compose 주제 유형에 따른 최적의 교육 구조 패턴입니다.

---

## 틀 선택 의사결정 트리

```
주제 분석
    │
    ├─── "없으면 문제 발생" 가능한가? ──Yes──► 패턴 A (문제-해결)
    │         │
    │         └── Side Effect, State, Lifecycle 관련
    │
    ├─── "필수 구성 요소"인가? ────Yes──► 패턴 B (구성요소 탐구)
    │         │
    │         └── Modifier, 기본 Composable, Theme
    │
    ├─── "여러 옵션 중 선택"인가? ─Yes──► 패턴 C (비교-선택)
    │         │
    │         └── Layout, Navigation, Animation
    │
    └─── "설계/아키텍처"인가? ────Yes──► 패턴 D (아키텍처 가이드)
              │
              └── ViewModel, State hoisting, MVVM
```

---

## 주제별 패턴 매핑

### 패턴 A 대상 (문제-해결)
| 주제 | 이유 |
|------|------|
| LaunchedEffect | Composable 내 코루틴 실행 문제 |
| DisposableEffect | 리소스 정리 누락 문제 |
| SideEffect | Composition 외부 코드 실행 문제 |
| rememberCoroutineScope | 이벤트 핸들러 코루틴 문제 |
| rememberSaveable | Configuration 변경 시 상태 손실 |
| derivedStateOf | 불필요한 Recomposition 문제 |
| snapshotFlow | State를 Flow로 변환 문제 |
| produceState | 외부 데이터 → State 변환 문제 |

### 패턴 B 대상 (구성요소 탐구)
| 주제 | 이유 |
|------|------|
| Modifier | Compose의 핵심 구성 요소 |
| Text, Button, Image | 기본 UI 컴포넌트 |
| Slot API | 컴포넌트 설계 패턴 |
| CompositionLocal | 암시적 데이터 전달 |
| Theme/Material Design | 디자인 시스템 |

### 패턴 C 대상 (비교-선택)
| 주제 | 이유 |
|------|------|
| Row vs Column vs Box | 레이아웃 선택 |
| LazyColumn vs LazyRow vs LazyGrid | 리스트 선택 |
| Navigation 방식들 | 네비게이션 아키텍처 선택 |
| Animation 종류들 | 애니메이션 API 선택 |

### 패턴 D 대상 (아키텍처 가이드)
| 주제 | 이유 |
|------|------|
| ViewModel + Compose | UI 아키텍처 |
| State hoisting | 상태 관리 패턴 |
| Unidirectional Data Flow | 데이터 흐름 설계 |
| MVVM with Compose | 전체 아키텍처 |
| Repository 패턴 | 데이터 계층 설계 |

---

## 패턴 A: 문제-해결 (Problem-Solution)

### 적용 대상
특정 문제를 해결하기 위해 도입된 API

### 파일 구조
```
study/{category}/{module_name}/
├── README.md                    # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt          # 탭: Problem | Solution | Practice
│   ├── Problem.kt               # 문제 상황 시연
│   ├── Solution.kt              # 해결책 시연
│   ├── Practice.kt              # 3개 연습문제
│   ├── README.md                # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/effect/launched_effect/`

### README.md 구조
```markdown
# {기술명} 학습

## 개념
{기술이 무엇인지 한 문장으로}

## 핵심 특징
1. {특징 1}
2. {특징 2}
3. {특징 3}

---

## 문제 상황: {기술 없이 발생하는 문제}

### 시나리오
{현실적인 상황 설명}

### 잘못된 코드 예시
```kotlin
// 이렇게 하면 문제가 발생합니다
```

### 발생하는 문제점
1. {문제 1}
2. {문제 2}

---

## 해결책: {기술} 사용

### 올바른 코드
```kotlin
// 이렇게 하면 해결됩니다
```

### 해결되는 이유
{왜 이 기술이 문제를 해결하는지}

---

## 사용 시나리오
1. {시나리오 1}
2. {시나리오 2}
3. {시나리오 3}

## 주의사항
- {주의 1}
- {주의 2}

## 연습 문제
### 연습 1: {제목} - 쉬움
### 연습 2: {제목} - 중간
### 연습 3: {제목} - 어려움

## 다음 학습
{연관된 다음 주제}
```

### Problem.kt 구조
```kotlin
@Composable
fun ProblemScreen() {
    Column {
        // 1. 문제 상황 설명 Card
        Card { Text("이 기술 없이 발생하는 문제...") }

        // 2. 문제가 발생하는 코드 (위험한 부분 주석)
        // 실제 문제 코드는 주석 처리하여 앱 크래시 방지

        // 3. 문제 발생 횟수/상태 표시
        Text("Recomposition 횟수: $count")
    }
}
```

### Solution.kt 구조
```kotlin
@Composable
fun SolutionScreen() {
    Column {
        // 1. 해결책 설명 Card
        Card { Text("이렇게 하면 해결됩니다...") }

        // 2. 올바른 코드 (실제 동작)
        LaunchedEffect(key) {
            // 올바른 구현
        }

        // 3. 해결됨을 보여주는 UI
        Text("정상 동작 중!")
    }
}
```

---

## 패턴 B: 구성요소 탐구 (Component Exploration)

### 적용 대상
Compose의 필수 구성 요소 (없으면 사용 자체가 불가능)

### 파일 구조
```
study/{category}/{module_name}/
├── README.md                    # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt          # 탭: 기본 | 고급 | Practice
│   ├── BasicUsage.kt            # 기본 사용법 데모
│   ├── AdvancedUsage.kt         # 고급 활용 + 조합 패턴
│   ├── Practice.kt              # 3개 연습문제
│   ├── README.md                # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/component/action/button/`

### README.md 구조
```markdown
# {구성요소} 완전 가이드

## 개요
{이 구성요소가 무엇이고 왜 중요한지}

## 기본 사용법
```kotlin
// 가장 기본적인 사용 예시
```

---

## 핵심 기능

### 기능 1: {이름}
{설명}
```kotlin
// 예시 코드
```

### 기능 2: {이름}
{설명}
```kotlin
// 예시 코드
```

---

## 다양한 사용 방법

### 방법 1: {유형}
### 방법 2: {유형}

---

## 조합 패턴

### 패턴 1: {조합명}
{언제 이 조합을 사용하는지}

### 패턴 2: {조합명}

---

## 베스트 프랙티스
1. {권장사항 1}
2. {권장사항 2}

## 안티패턴
1. {피해야 할 것 1}
2. {피해야 할 것 2}

## 연습 문제
### 연습 1: {제목} - 쉬움
### 연습 2: {제목} - 중간
### 연습 3: {제목} - 어려움

## 다음 학습
```

### BasicUsage.kt 구조
```kotlin
@Composable
fun BasicUsageScreen() {
    Column {
        // 1. 기본 사용법 설명
        Text("기본 사용법을 알아봅시다")

        // 2. 각 기능별 데모
        BasicDemo1()
        BasicDemo2()
        BasicDemo3()
    }
}
```

### AdvancedUsage.kt 구조
```kotlin
@Composable
fun AdvancedUsageScreen() {
    Column {
        // 1. 고급 활용 설명
        Text("고급 활용법을 알아봅시다")

        // 2. 조합 패턴 데모
        CombinationPattern1()
        CombinationPattern2()

        // 3. 베스트 프랙티스 vs 안티패턴
        BestPracticeDemo()
        AntiPatternDemo()  // 주석 처리된 나쁜 예
    }
}
```

---

## 패턴 C: 비교-선택 (Compare-Choose)

### 적용 대상
유사한 여러 옵션 중 상황에 맞게 선택이 필요한 주제

### 파일 구조
```
study/{category}/{module_name}/
├── README.md                    # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt          # 탭: 비교 | 선택가이드 | Practice
│   ├── OptionsComparison.kt     # 옵션별 데모 + 비교표
│   ├── SelectionGuide.kt        # 상황별 추천 + 의사결정 플로우
│   ├── Practice.kt              # 3개 연습문제
│   ├── README.md                # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/list/lazy_layouts/`

### README.md 구조
```markdown
# {주제}: 올바른 선택 가이드

## 개요
{이 주제에서 왜 선택이 중요한지}

---

## 옵션 1: {이름}

### 특징
- {특징 1}
- {특징 2}

### 적합한 상황
- {상황 1}
- {상황 2}

### 코드 예시
```kotlin
// 코드
```

---

## 옵션 2: {이름}
(동일 구조)

---

## 옵션 3: {이름}
(동일 구조)

---

## 비교 표

| 기준 | 옵션 1 | 옵션 2 | 옵션 3 |
|------|--------|--------|--------|
| {기준 1} | {값} | {값} | {값} |
| {기준 2} | {값} | {값} | {값} |

---

## 상황별 선택 가이드

### 상황 1: {설명}
→ **{추천 옵션}** 사용

### 상황 2: {설명}
→ **{추천 옵션}** 사용

---

## 의사결정 플로우차트

```
시작
  │
  ├── {조건 1}? ──Yes──► 옵션 1
  │
  ├── {조건 2}? ──Yes──► 옵션 2
  │
  └── 그 외 ──────────► 옵션 3
```

## 연습 문제
### 연습 1: {제목} - 옵션 1 선택 상황
### 연습 2: {제목} - 옵션 2 선택 상황
### 연습 3: {제목} - 복합 선택 상황

## 다음 학습
```

---

## 패턴 D: 아키텍처 가이드 (Architecture Guide)

### 적용 대상
설계 패턴 및 아키텍처 관련 주제

### 파일 구조
```
study/{category}/{module_name}/
├── README.md                    # 전체 학습 문서 (모듈 루트)
├── build.gradle.kts
├── src/main/java/com/example/{module_name}/
│   ├── MainActivity.kt          # 탭: 원칙 | 구현 | Practice
│   ├── Principles.kt            # 핵심 원칙 데모
│   ├── Implementation.kt        # 실제 구현 + 안티패턴
│   ├── Practice.kt              # 3개 연습문제
│   ├── README.md                # 학습 바로가기 링크
│   └── ui/theme/
└── src/main/res/
```

**예시**: `study/architecture/state_hoisting/`

### README.md 구조
```markdown
# {아키텍처} 완벽 가이드

## 왜 이 아키텍처가 필요한가?

### 문제 상황
{아키텍처 없이 발생하는 문제들}

### 해결 목표
{이 아키텍처가 달성하고자 하는 것}

---

## 핵심 원칙

### 원칙 1: {이름}
{설명}
```kotlin
// 예시
```

### 원칙 2: {이름}

### 원칙 3: {이름}

---

## 구현 방법

### Step 1: {단계}
### Step 2: {단계}
### Step 3: {단계}

---

## 실제 앱 예제

### 예제: {앱 기능명}
{전체 구현 코드 및 설명}

---

## 안티패턴

### 안티패턴 1: {이름}
{왜 나쁜지}
```kotlin
// 잘못된 코드
```

### 올바른 방법
```kotlin
// 수정된 코드
```

---

## 테스트 가이드
{이 아키텍처에서 테스트하는 방법}

## 연습 문제
### 연습 1: {제목} - 쉬움
### 연습 2: {제목} - 중간
### 연습 3: {제목} - 어려움 (전체 구현)

## 다음 학습
```

---

## 틀 변경이 필요한 경우

### 감지 신호

1. **문제-해결 틀인데 "문제"가 억지스러움**
   → 구성요소 탐구 또는 비교-선택 틀 고려

2. **구성요소 탐구인데 "왜 써야 하는지" 설명이 주가 됨**
   → 문제-해결 틀 고려

3. **비교-선택인데 옵션이 1개뿐**
   → 구성요소 탐구 또는 문제-해결 틀 고려

4. **아키텍처 가이드인데 내용이 단순**
   → 문제-해결 또는 구성요소 탐구 틀 고려

### 변경 절차

```
1. 현재 틀 파악
2. 부적합 이유 분석
3. 적합한 틀 결정
4. Phase 1로 회귀
5. 새 틀로 재시작
```
