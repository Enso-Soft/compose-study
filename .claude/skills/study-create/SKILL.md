---
name: study-create
description: A+ 등급 품질의 Compose 학습 모듈을 생성합니다. "고품질 모듈 만들어줘", "XXX 스터디 생성해줘" 등의 요청 시 사용합니다. 틀 선택 → 연구 → 품질 검증 → 구현의 통합 워크플로우로 재작업 없이 고품질 결과물을 생성합니다.
---

# Study Create Skill

처음부터 A+ 등급 품질을 보장하는 Compose 학습 모듈 생성 스킬입니다.

## 핵심 차별점

| 기존 방식 | study-create |
|----------|--------------|
| 생성 → 검토 → 수정 | 틀 선택 → 품질 내장 생성 |
| Sequential Thinking 21회 | 15회 (최적화) |
| 재작업 가능성 높음 | 재작업 없음 |

---

## 사용 시점

- "고품질 XXX 모듈 만들어줘" 요청 시
- "A+ 등급으로 생성해줘" 요청 시
- 새로운 Compose 기술 학습 자료 생성 시
- `/study-create {주제}` 명령 시

---

## 참조 문서 (필수로 읽어보기)

- `frameworks.md`: 교육 틀 패턴 (A/B/C/D)
- `criteria.md`: 품질 체크리스트
- `templates/pattern-*.md`: 패턴별 코드 템플릿

---

## 필수 워크플로우 (5단계)

### Phase 1: 틀 결정 (Sequential Thinking 3회 필수!)

**목적**: 주제에 최적인 교육 패턴을 먼저 결정

#### Step 1.1: 주제 조사
```
WebSearch: "{주제} Android Compose 2025 best practices"
Context7: get-library-docs(topic="{주제}")
```

#### Step 1.2: Sequential Thinking 3회

```
필수 사고 주제:
1회: 주제 유형 분석 - 이 기술은 어떤 카테고리인가?
     (Side Effect / Modifier / Layout / Architecture)
     + 디렉토리 카테고리 결정 (아래 카테고리 분류 가이드 참조)
2회: 의사결정 트리 적용 - frameworks.md 참조하여 패턴 A/B/C/D 결정
3회: 구조 확정 - 선택된 패턴의 README/파일 구조 확정
```

#### 카테고리 분류 가이드

**Step 1: 주제 → 카테고리 매핑**

| 주제 키워드 | 카테고리 | 경로 예시 |
|------------|----------|----------|
| Kotlin 문법, Compose 입문 | `basics/` | `study/basics/kotlin/` |
| Row, Column, Box, Modifier, Layout | `layout/` | `study/layout/custom_layout/` |
| remember, State, Flow, ViewModel | `state/` | `study/state/remember/` |
| LaunchedEffect, DisposableEffect, SideEffect | `effect/` | `study/effect/launched_effect/` |
| LazyColumn, LazyGrid, Pager, 스크롤 | `list/` | `study/list/lazy_list/` |
| SearchBar | `search/` | `study/search/search_bar/` |
| Scaffold, TopAppBar, NavigationBar | `structure/` | `study/structure/scaffold/` |
| NavHost, Navigation, DeepLink | `navigation/` | `study/navigation/navigation_basics/` |
| animate*, Transition, AnimatedVisibility | `animation/` | `study/animation/animation_basics/` |
| MVVM, Clean Architecture, State Hoisting | `architecture/` | `study/architecture/hilt_viewmodel/` |
| Gesture, Drag, Click, Focus | `interaction/` | `study/interaction/gesture/` |
| Coil, CameraX, Media3, Room | `integration/` | `study/integration/image_loading/` |
| Permission, Notification, DeepLink | `system/` | `study/system/permission_handling/` |
| Test, Benchmark, Profiling | `testing/` | `study/testing/compose_testing/` |

**Step 2: UI 컴포넌트는 하위 분류**

`component/` 카테고리는 4개 하위 그룹으로 세분화:

| 하위 카테고리 | 포함 컴포넌트 | 경로 예시 |
|--------------|--------------|----------|
| `component/action/` | Button, FAB, IconButton, Menu | `study/component/action/button/` |
| `component/selection/` | Checkbox, RadioButton, Switch, Chip, SegmentedButton | `study/component/selection/checkbox/` |
| `component/input/` | TextField, Slider, DatePicker, TimePicker | `study/component/input/text_field/` |
| `component/display/` | Card, Dialog, Badge, Tooltip, BottomSheet, Divider | `study/component/display/card/` |

**Step 3: 신규 카테고리 생성 조건**

기존 카테고리에 맞지 않는 경우, 아래 조건 충족 시 **신규 카테고리 생성 가능**:

```
신규 카테고리 생성 체크리스트:
□ 기존 15개 카테고리 중 적합한 곳이 없는가?
□ 해당 주제로 3개 이상 모듈이 예상되는가?
□ 명확한 공통 테마가 있는가?
□ 영문 단수형 snake_case로 표현 가능한가?
```

**신규 카테고리 생성 시 필수 작업:**
1. `study/{new_category}/` 디렉토리 생성
2. `settings.gradle.kts`에 카테고리 섹션 추가
3. 프로젝트 `README.md` 로드맵에 반영
4. 이 스킬 문서의 카테고리 목록 업데이트

**예시 - 신규 카테고리 판단:**
```
주제: "Wear OS Compose"
→ 기존 카테고리에 없음
→ Wear용 모듈 3개 이상 예상 (Scaffold, Button, Navigation...)
→ 공통 테마: Wearable 디바이스
→ 카테고리명: wearable/
→ 결정: 신규 카테고리 생성!
```

#### 의사결정 트리 (frameworks.md 참조)

```
주제 분석
    │
    ├── "없으면 문제 발생" 가능? ──Yes──► 패턴 A (문제-해결)
    │         └── LaunchedEffect, DisposableEffect, derivedStateOf...
    │
    ├── "필수 구성 요소"인가? ────Yes──► 패턴 B (구성요소 탐구)
    │         └── Modifier, Text, Button, CompositionLocal...
    │
    ├── "여러 옵션 중 선택"인가? ─Yes──► 패턴 C (비교-선택)
    │         └── Layout 비교, Navigation, Animation...
    │
    └── "설계/아키텍처"인가? ────Yes──► 패턴 D (아키텍처 가이드)
              └── ViewModel, State hoisting, MVVM...
```

**3회 미만 시 다음 단계로 진행할 수 없습니다!**

---

### Phase 2: 연구 (Sequential Thinking 5회 필수!)

**목적**: 선택된 틀에 맞는 콘텐츠 설계

#### Step 2.1: 심층 조사
```
WebSearch: "{주제} Compose 2025 examples tutorials"
Context7: get-library-docs(topic="{주제}", mode="code")
Context7: get-library-docs(topic="{주제}", mode="info")
```

#### Step 2.2: Sequential Thinking 5회

```
필수 사고 주제:
4회: 검색 결과 분석 및 핵심 개념 정리
5회: 틀에 맞는 시나리오 도출
     - 패턴 A: 문제 상황 → 해결책
     - 패턴 B: 기본 사용법 → 고급 활용
     - 패턴 C: 옵션별 특징 → 선택 기준
     - 패턴 D: 원칙 → 구현 단계
6회: 코드 예제 시나리오 설계
7회: 연습문제 3개 구체화 (쉬움/중간/어려움 균형)
8회: 전체 흐름 검증 - 논리적 연결 확인
```

**5회 미만 시 다음 단계로 진행할 수 없습니다!**

---

### Phase 3: 설계 + 품질 검증 (Sequential Thinking 4회 필수!)

**목적**: 구현 전 품질 보장

#### Step 3.1: Sequential Thinking 4회 (criteria.md 기준)

```
필수 검증 주제:
9회: 정확성 검증
     □ 2025년 공식 문서와 일치하는가?
     □ deprecated API를 사용하지 않는가?
     □ API 시그니처가 정확한가?

10회: 교육적 구조 검증
     □ 주제에 맞는 패턴을 사용하는가?
     □ 논리적 흐름이 자연스러운가?
     □ 쉬운 것에서 어려운 것으로 진행하는가?

11회: 학습자 친화성 검증
     □ 전문 용어에 설명이 있는가?
     □ 비유/예시가 충분한가?
     □ "Recomposition이 트리거되면" 대신
       "화면이 다시 그려질 때(Recomposition)" 사용하는가?

12회: 실용성 검증
     □ 실제 앱에서 사용되는 시나리오인가?
     □ 코드를 바로 복사-붙여넣기 가능한가?
     □ 연습문제 난이도가 균형있는가?
```

#### Step 3.2: 문제 발견 시
- 경미한 문제: 설계 수정 후 계속
- 심각한 문제: **Phase 1로 회귀** (틀 재검토)

**4회 미만 시 다음 단계로 진행할 수 없습니다!**

---

### Phase 4: 구현

**목적**: 품질이 검증된 설계를 코드로 구현

#### Step 4.1: 모듈 디렉토리 생성
```bash
mkdir -p study/{category}/{module_name}/src/main/java/com/example/{module_name}/ui/theme
mkdir -p study/{category}/{module_name}/src/main/res/values
mkdir -p study/{category}/{module_name}/src/main/res/drawable
mkdir -p study/{category}/{module_name}/src/main/res/mipmap-{hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi}
```

**카테고리 구조:**
```
study/
├── basics/          # Kotlin, Compose 입문
├── layout/          # 레이아웃 & Modifier
├── state/           # 상태 관리
├── component/       # UI 컴포넌트
│   ├── action/      # 버튼, 메뉴
│   ├── selection/   # 체크박스, 라디오, 스위치 등
│   ├── input/       # 텍스트필드, 슬라이더, 날짜/시간 선택
│   └── display/     # 카드, 다이얼로그, 배지 등
├── list/            # 리스트 & 스크롤
├── search/          # 검색
├── structure/       # 앱 구조 (Scaffold 등)
├── effect/          # Side Effects
├── navigation/      # 네비게이션
├── animation/       # 애니메이션
├── architecture/    # 아키텍처 패턴
├── interaction/     # 제스처 & 인터랙션
├── integration/     # 외부 통합
├── system/          # 시스템 기능
└── testing/         # 테스트 & 성능
```

#### Step 4.2: 패턴별 파일 생성

**패턴 A (문제-해결)**: `templates/pattern-a.md` 참고
- README.md (문제-해결 구조)
- Problem.kt
- Solution.kt
- Practice.kt
- MainActivity.kt (Problem | Solution | Practice 탭)

**패턴 B (구성요소 탐구)**: `templates/pattern-b.md` 참고
- README.md (구성요소 탐구 구조)
- BasicUsage.kt
- AdvancedUsage.kt
- Practice.kt
- MainActivity.kt (기본 | 고급 | Practice 탭)

**패턴 C (비교-선택)**: `templates/pattern-c.md` 참고
- README.md (비교-선택 구조)
- OptionsComparison.kt
- SelectionGuide.kt
- Practice.kt
- MainActivity.kt (비교 | 선택가이드 | Practice 탭)

**패턴 D (아키텍처 가이드)**: `templates/pattern-d.md` 참고
- README.md (아키텍처 구조)
- Principles.kt
- Implementation.kt
- Practice.kt
- MainActivity.kt (원칙 | 구현 | Practice 탭)

#### Step 4.3: 공통 파일 생성
- build.gradle.kts
- AndroidManifest.xml
- ui/theme/ (Color.kt, Type.kt, Theme.kt)
- res/ (themes.xml, colors.xml, strings.xml)

#### Step 4.4: 프로젝트 등록
```kotlin
// settings.gradle.kts - 해당 카테고리 섹션에 추가
include(":study:{category}:{module_name}")

// 예시
include(":study:effect:launched_effect")
include(":study:component:action:button")
```

#### Step 4.5: README.md 하이퍼링크 추가
프로젝트 최상단 README.md의 해당 Level 표에 추가:
```markdown
| [module_name](study/{category}/{module_name}/README.md) | 설명 | 선행 학습 |
```

---

### Phase 5: 최종 검증 (Sequential Thinking 3회 필수!)

**목적**: 완성된 모듈의 최종 품질 확인

#### Step 5.1: 빌드 확인
```bash
./gradlew :study:{category}:{module_name}:compileDebugKotlin

# 예시
./gradlew :study:effect:launched_effect:compileDebugKotlin
```

#### Step 5.2: Sequential Thinking 3회

```
필수 검증 주제:
13회: 전체 일관성 검증
      - 모든 파일이 같은 시나리오를 다루는가?
      - README ↔ 코드 내용이 일치하는가?

14회: 코드 동작 검증
      - 빌드가 성공했는가?
      - 예제 코드가 실제로 동작하는가?

15회: A+ 등급 판정
      □ 정확성: ✅ 우수
      □ 교육적 구조: ✅ 우수
      □ 학습자 친화성: ✅ 우수
      □ 실용성: ✅ 우수
      → 4가지 모두 ✅면 A+ 등급!
```

**3회 미만 시 완료 처리할 수 없습니다!**

#### Step 5.3: 문제 발견 시
- 빌드 오류: 즉시 수정
- 품질 미달: **Phase 3으로 회귀**

---

## 진행 상황 표시

```
🎯 [1/5] 틀 결정 중... (Sequential Thinking 3회)
📚 [2/5] 연구 중... (Sequential Thinking 5회)
✅ [3/5] 품질 검증 중... (Sequential Thinking 4회)
🔨 [4/5] 구현 중...
🏆 [5/5] 최종 검증 중... (Sequential Thinking 3회)
```

---

## 네이밍 규칙

| 항목 | 형식 | 예시 |
|------|------|------|
| 모듈 디렉토리 | `study/{category}/{snake_case}/` | `study/effect/launched_effect/` |
| 패키지명 | `com.example.{snake_case}` | `com.example.launched_effect` |
| 테마 함수명 | `{PascalCase}Theme` | `LaunchedEffectTheme` |
| Gradle 등록 | `:study:{category}:{module_name}` | `:study:effect:launched_effect` |

---

## 주의사항

1. **Sequential Thinking 횟수 필수 충족**
   - Phase 1: 3회 (틀 결정)
   - Phase 2: 5회 (연구)
   - Phase 3: 4회 (품질 검증)
   - Phase 5: 3회 (최종 검증)
   - **총 15회**

2. **틀 선택 우선**
   - 연구 전에 반드시 패턴 결정
   - 잘못된 틀로 시작하면 Phase 1로 회귀

3. **품질 내장 생성**
   - criteria.md 기준을 Phase 3에서 적용
   - 구현 전에 품질 보장

4. **회귀 허용**
   - 문제 발견 시 이전 Phase로 돌아가기
   - 품질을 위해 시간 투자

---

## 예시

### 요청
```
/study-create LaunchedEffect
```

### 응답 흐름
```
🎯 [1/5] 틀 결정 중...
   - WebSearch: "LaunchedEffect Compose 2025"
   - Sequential Thinking 3회
   - 결과: 패턴 A (문제-해결) 선택
     → LaunchedEffect는 "없으면 문제 발생"하는 Side Effect API

📚 [2/5] 연구 중...
   - Context7: LaunchedEffect 공식 문서
   - Sequential Thinking 5회
   - 결과: API 호출 시나리오, 연습문제 3개 설계

✅ [3/5] 품질 검증 중...
   - Sequential Thinking 4회 (4가지 기준)
   - 결과: 모든 기준 통과

🔨 [4/5] 구현 중...
   - study/effect/launched_effect/ 디렉토리 생성
   - Problem.kt, Solution.kt, Practice.kt 생성
   - settings.gradle.kts에 `:study:effect:launched_effect` 등록

🏆 [5/5] 최종 검증 중...
   - 빌드 확인: ✅
   - Sequential Thinking 3회
   - 결과: A+ 등급 달성!

✨ 완료: A+ 등급 LaunchedEffect 학습 모듈 생성됨
```
