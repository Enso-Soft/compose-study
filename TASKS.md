# 통합 런처 구현 태스크

> 이 문서를 읽고 한 단계씩 진행하며 업데이트합니다.

---

## 현재 상태

**Phase**: 완료 ✅
**진행률**: 100%
**마지막 업데이트**: 2025-12-28

---

## Phase 0: 사전 준비 (필수)

### 0-1. Study 모듈 Library 변환 ✅ 완료
- [x] 변환 스크립트 작성 (build.gradle.kts 수정)
- [x] 변환 스크립트 작성 (AndroidManifest.xml 수정)
- [x] 스크립트 실행 및 검증 (113개 모듈 변환)
- [x] 빌드 테스트

**완료 노트:**
- `scripts/convert_to_library.sh` 스크립트 생성
- 114개 모듈 중 113개 Library로 변환
- `hilt_viewmodel` 모듈은 @HiltAndroidApp 필요로 Application 유지
- `libs.versions.toml`에 `android-library` 플러그인 추가
- 루트 `build.gradle.kts`에 플러그인 등록

### 0-2. App 모듈 의존성 설정 ✅ 완료
- [x] app/build.gradle.kts에 모든 study 모듈 의존성 추가 (112개)
- [x] AndroidManifest.xml에 tools:replace 속성 추가
- [x] 코드 버그 수정 (search_bar, search_bar_advanced, stability 모듈)
- [x] 빌드 테스트 성공

**완료 노트:**
- 112개 study 모듈을 app 모듈의 의존성으로 추가
- `hilt_viewmodel` 제외 (Application 모듈로 유지)
- 매니페스트 병합 충돌 해결: `tools:replace` 속성 추가
- 빌드 오류 수정:
  - `search_bar`: 테마 import 경로 수정
  - `search_bar_advanced`: 테마 import 경로 수정
  - `stability`: ColumnScope 확장 함수로 변경

---

## Phase 1: 데이터 레이어

### 1-1. 데이터 모델 정의 ✅ 완료
- [x] `StudyModule` data class 작성
- [x] `Level` data class 작성
- [x] `Category` enum class 작성

**완료 노트:**
- `app/src/main/java/com/example/compose_study/model/` 디렉토리 생성
- `Category.kt`: 15개 카테고리 (BASICS, LAYOUT, STATE, COMPONENT 등)
  - displayName, description, emoji 속성 포함
  - fromDirectoryName() 헬퍼 함수
- `StudyModule.kt`: 모듈 데이터 클래스
  - id, name, description, detailDescription, level, category, prerequisites, activityClass
  - matchesQuery() 검색 함수, searchKeywords 속성
  - getLevelName(), getLevelDescription() companion 함수
- `Level.kt`: 레벨 데이터 클래스
  - number, name, description, modules
  - createDefaultLevels(), fromModules() 팩토리 함수
  - getModulesByCategory() 필터 함수

### 1-2. 모듈 레지스트리 작성 ✅ 완료
- [x] `ModuleRegistry` object 작성
- [x] 모든 study 모듈 등록 (113개)
- [x] Level별 그룹화 함수
- [x] Category별 그룹화 함수
- [x] 검색 함수

**완료 노트:**
- `app/src/main/java/com/example/compose_study/data/ModuleRegistry.kt` 생성
- `Category.kt` 업데이트: MULTIPLATFORM 카테고리 추가 (16개 카테고리)
- 113개 모듈 등록 (hilt_viewmodel 제외)
- Level별 분포:
  - Level 1 (입문): 6개
  - Level 2 (기초): 22개
  - Level 3 (중급): 40개
  - Level 4 (고급): 30개
  - Level 5 (심화): 15개
- 제공 함수:
  - `getByLevel(level: Int)`: Level별 필터링
  - `getByCategory(category: Category)`: Category별 필터링
  - `search(query: String)`: 검색 기능
  - `getById(id: String)`: ID로 조회
  - `getPrerequisites(module: StudyModule)`: 선행 모듈 조회
  - `moduleCount`, `moduleCountByLevel`, `moduleCountByCategory`: 통계

### 1-3. Repository 작성 ✅ 완료
- [x] `ModuleRepository` interface 정의
- [x] `ModuleRepositoryImpl` 구현
- [x] `RecentRepository` interface 정의
- [x] `RecentRepositoryImpl` 구현 (DataStore 사용)

**완료 노트:**
- `libs.versions.toml`에 DataStore 의존성 추가 (1.1.1)
- `app/build.gradle.kts`에 DataStore 의존성 추가
- `app/src/main/java/com/example/compose_study/data/repository/` 디렉토리 생성
- `ModuleRepository.kt`: 모듈 데이터 접근 인터페이스
  - getAllModules(), getModulesByLevel(), getModulesByCategory()
  - searchModules(), getModuleById(), getPrerequisites()
  - getLevels(), moduleCount, moduleCountByLevel, moduleCountByCategory
- `ModuleRepositoryImpl.kt`: ModuleRegistry 위임 구현
- `RecentRepository.kt`: 최근 검색/모듈 저장 인터페이스
  - getRecentSearches(), addRecentSearch(), removeRecentSearch(), clearRecentSearches()
  - getRecentModules(), addRecentModule(), clearRecentModules()
  - MAX_RECENT_SEARCHES = 10, MAX_RECENT_MODULES = 10
- `RecentRepositoryImpl.kt`: DataStore Preferences 구현
  - Context.recentDataStore extension
  - 구분자(|)로 연결된 문자열로 순서 보존

---

## Phase 2: UI 상태 관리 ✅ 완료

### 2-1. UI State 정의 ✅ 완료
- [x] `LauncherUiState` data class 작성

### 2-2. ViewModel 작성 ✅ 완료
- [x] `LauncherViewModel` 작성
- [x] 검색 로직 구현 (debounce 300ms)
- [x] 최근 검색/모듈 관리 로직
- [x] Expand/Collapse 로직

**완료 노트:**
- `app/src/main/java/com/example/compose_study/ui/` 디렉토리 생성
- `LauncherUiState.kt`: UI 상태 data class
  - searchQuery, isSearchActive, filteredModules
  - recentSearches, recentModules
  - expandedLevels (기본 Level 1 펼침), expandedModules
  - allLevels, isLoading, error
  - 헬퍼 함수: isLevelExpanded(), isModuleExpanded(), hasSearchResults 등
- `LauncherViewModel.kt`: 상태 관리 ViewModel
  - ModuleRepository, RecentRepository 의존성 주입
  - 검색: onSearchQueryChange(), onSearchActivate/Deactivate(), onSearchSubmit()
  - 최근 검색어: onRecentSearchClick(), onRecentSearchRemove(), onClearRecentSearches()
  - 모듈: onModuleLaunch(), onClearRecentModules()
  - 펼침/접힘: onLevelToggle(), onModuleToggle(), onExpandAllLevels(), onCollapseAllLevels()
  - Flow 기반 상태 관찰 (debounce, distinctUntilChanged)

---

## Phase 3: UI 컴포넌트

### 3-1. 기본 컴포넌트 ✅ 완료
- [x] `ModuleCard` Composable (Expandable)
- [x] `LevelSection` Composable (Expandable)
- [x] `RecentSearchList` Composable (세로 스크롤)
- [x] `RecentModuleRow` Composable (가로 스크롤)

**완료 노트:**
- `app/src/main/java/com/example/compose_study/ui/components/` 디렉토리 생성
- `ModuleCard.kt`: 확장 가능한 모듈 카드 컴포넌트
  - animateContentSize로 펼침/접힘 애니메이션
  - 카테고리 이모지, 모듈명, 설명 표시
  - 펼침 시: 상세 설명, 선행 모듈 (FlowRow + SuggestionChip), 학습 시작 버튼
- `LevelSection.kt`: 레벨 섹션 컴포넌트
  - AnimatedVisibility로 모듈 목록 표시/숨김
  - 화살표 회전 애니메이션 (animateFloatAsState)
  - 레벨 헤더: 이름, 설명, 모듈 개수
  - 내부에 ModuleCard 목록 포함
- `RecentSearchList.kt`: 최근 검색어 목록
  - 섹션 헤더 + 전체 삭제 버튼
  - 각 항목: 검색어 텍스트 + 개별 삭제 버튼
- `RecentModuleRow.kt`: 최근 학습 모듈 가로 스크롤
  - LazyRow로 가로 스크롤 구현
  - 간단한 카드: 이모지, 모듈명, 카테고리
- `app/build.gradle.kts`에 material-icons-extended 의존성 추가

### 3-2. 검색 컴포넌트 ✅ 완료
- [x] `LauncherSearchBar` Composable
- [x] 검색 결과 리스트

**완료 노트:**
- `LauncherSearchBar.kt`: Material3 DockedSearchBar 기반 검색 컴포넌트
  - SearchBarDefaults.InputField로 검색 입력 필드 구성
  - 활성화 시 뒤로가기 아이콘, 비활성화 시 검색 아이콘
  - 검색어 있을 때 지우기 버튼 표시
  - 검색어 비어있을 때: RecentSearchList, RecentModuleRow 표시
  - 검색어 있을 때: SearchResultList로 결과 표시
- `SearchResultList.kt`: 검색 결과 목록 컴포넌트
  - LazyColumn으로 ModuleCard 목록 표시
  - 결과 개수 표시 헤더
  - EmptySearchResult: 검색 결과 없을 때 빈 상태 UI
  - 검색 힌트 제공

### 3-3. 애니메이션 ✅ 완료 (Phase 3-1에서 구현)
- [x] Level 펼치기/접기 애니메이션
- [x] 모듈 카드 상세 펼침 애니메이션
- [x] 화살표 회전 애니메이션

**완료 노트:**
- 모든 애니메이션은 Phase 3-1의 기본 컴포넌트에서 이미 구현됨:
  - `LevelSection.kt`: AnimatedVisibility (expandVertically/shrinkVertically, tween 300ms)
  - `ModuleCard.kt`: animateContentSize (spring animation)
  - `LevelSection.kt`: animateFloatAsState로 화살표 180도 회전

---

## Phase 4: 메인 화면 조립 ✅ 완료

### 4-1. LauncherScreen 작성 ✅ 완료
- [x] 전체 화면 레이아웃 구성
- [x] SearchBar 통합
- [x] 최근 검색/모듈 섹션
- [x] Level 목록 섹션

**완료 노트:**
- `app/src/main/java/com/example/compose_study/ui/LauncherScreen.kt` 생성
- Scaffold + TopAppBar 기반 레이아웃
- LauncherSearchBar 통합 (검색바 활성화 시 TopAppBar 숨김)
- AnimatedVisibility로 검색 모드/일반 모드 전환
- LazyColumn으로 최근 검색어, 최근 모듈, Level 목록 표시
- LoadingContent, ErrorContent, EmptyContent 상태 UI

### 4-2. MainActivity 수정 ✅ 완료
- [x] LauncherScreen 적용
- [x] ViewModel 연결

**완료 노트:**
- `MainActivity.kt` 수정: LauncherApp Composable 추가
- `LauncherViewModelFactory.kt` 생성: Repository 주입
- viewModel() 함수로 ViewModel 생성
- 모듈 클릭 시 해당 모듈의 MainActivity 실행 (Intent)
- `lifecycle-viewmodel-compose` 의존성 추가

---

## Phase 5: 테스트 & 마무리 ✅ 완료

### 5-1. 기능 테스트 ✅ 완료
- [x] 빌드 테스트 (BUILD SUCCESSFUL)
- [x] 앱 구조 및 런처 로직 검증 (코드 리뷰)
- [x] 모듈 검색 로직 검증 (ViewModel debounce 300ms)
- [x] 모듈 실행 로직 검증 (Intent 기반 Activity 실행)
- [x] 최근 검색 저장/삭제 로직 검증 (DataStore Preferences)
- [x] Expand/Collapse 상태 관리 검증 (Set 기반)

**완료 노트:**
- 전체 빌드 성공 (1485 tasks)
- 코드 리뷰로 모든 핵심 로직 검증 완료
- ViewModel: Flow 기반 상태 관리, debounce 적용
- Repository: DataStore로 최근 검색/모듈 저장
- 상태 관리: expandedLevels, expandedModules Set 사용

### 5-2. UI 개선 ✅ 완료
- [x] Empty State (LauncherScreen - EmptyContent)
- [x] Error State (LauncherScreen - ErrorContent)
- [x] 접근성 개선 (contentDescription)

**완료 노트:**
- Empty/Error State 이미 LauncherScreen.kt에 구현됨
- 접근성 개선 적용:
  - `RecentSearchList.kt`: 검색 아이콘에 "최근 검색어" 설명 추가
  - `RecentSearchList.kt`: 삭제 버튼에 구체적인 설명 추가 ("'검색어' 검색어 삭제")
  - `RecentModuleRow.kt`: 카드에 semantics contentDescription 추가
  - 모든 주요 아이콘에 contentDescription 있음 확인

---

## 화면 설계

### 메인 화면
```
┌─────────────────────────────────────┐
│  Compose Study                      │
├─────────────────────────────────────┤
│  🔍 모듈 검색...                    │
├─────────────────────────────────────┤
│  🕐 최근 검색           [전체 삭제] │
│  ├─ remember                    선택│
│  ├─ LaunchedEffect              선택│
│  └─ animation                   선택│
├─────────────────────────────────────┤
│  📖 최근 학습                       │
│  ┌─────┐ ┌─────┐ ┌─────┐           │
│  │Card │ │Card │ │Card │  →→→      │
│  └─────┘ └─────┘ └─────┘           │
├─────────────────────────────────────┤
│  ▼ Level 1: Compose 입문      (3)  │
│    ├─ compose_introduction     [▶] │
│    ├─ composable_function      [▶] │
│    └─ preview                  [▶] │
│                                     │
│  ▶ Level 2: 레이아웃 기초     (6)  │
└─────────────────────────────────────┘
```

### 모듈 카드 (Expanded)
```
┌─────────────────────────────────────┐
│ 📦 launched_effect                  │
│ ─────────────────────────────────── │
│ 비동기 작업 실행 (API 호출 등)       │
│ 선행: side_effect                   │
│                                     │
│            ▼ 더 보기                │
├─────────────────────────────────────┤
│ LaunchedEffect는 Composable이       │
│ 컴포지션에 진입할 때 suspend 함수를  │
│ 실행하는 Side Effect입니다.         │
│                                     │
│          [🚀 학습 시작]             │
└─────────────────────────────────────┘
```

---

## 기술 스택

| 기능 | Compose API |
|------|-------------|
| 검색 | `SearchBar` |
| 목록 | `LazyColumn`, `LazyRow` |
| 펼치기 애니메이션 | `AnimatedVisibility`, `animateContentSize` |
| 물리 애니메이션 | `spring()` |
| 상태 저장 | `rememberSaveable`, `DataStore` |
| 카드 | `ElevatedCard`, `Card` |
| 칩 | `InputChip` |

---

## 파일 구조 (완성)

```
app/src/main/java/com/example/compose_study/
├── MainActivity.kt                    ✅ 수정
├── data/
│   ├── ModuleRegistry.kt              ✅ 생성
│   └── repository/
│       ├── ModuleRepository.kt        ✅ 생성
│       ├── ModuleRepositoryImpl.kt    ✅ 생성
│       ├── RecentRepository.kt        ✅ 생성
│       └── RecentRepositoryImpl.kt    ✅ 생성
├── model/
│   ├── Category.kt                    ✅ 생성
│   ├── StudyModule.kt                 ✅ 생성
│   └── Level.kt                       ✅ 생성
├── ui/
│   ├── LauncherScreen.kt              ✅ 생성
│   ├── LauncherViewModel.kt           ✅ 생성
│   ├── LauncherViewModelFactory.kt    ✅ 생성
│   ├── LauncherUiState.kt             ✅ 생성
│   └── components/
│       ├── ModuleCard.kt              ✅ 생성
│       ├── LevelSection.kt            ✅ 생성
│       ├── RecentSearchList.kt        ✅ 생성
│       ├── RecentModuleRow.kt         ✅ 생성
│       ├── LauncherSearchBar.kt       ✅ 생성
│       └── SearchResultList.kt        ✅ 생성
└── ui/theme/
    └── (기존 테마)
```

---

## 진행 로그

| 날짜 | Phase | 작업 내용 | 상태 |
|------|-------|----------|------|
| 2025-12-28 | 0 | 태스크 문서 작성 | 완료 |
| 2025-12-28 | 0-1 | Study 모듈 Library 변환 (113개) | 완료 |
| 2025-12-28 | 0-2 | App 모듈 의존성 설정 (112개) | 완료 |
| 2025-12-28 | 1-1 | 데이터 모델 정의 (Category, StudyModule, Level) | 완료 |
| 2025-12-28 | 1-2 | 모듈 레지스트리 작성 (113개 모듈) | 완료 |
| 2025-12-28 | 1-3 | Repository 작성 (ModuleRepository, RecentRepository) | 완료 |
| 2025-12-28 | 2 | UI 상태 관리 (LauncherUiState, LauncherViewModel) | 완료 |
| 2025-12-28 | 3-1 | 기본 컴포넌트 (ModuleCard, LevelSection, RecentSearchList, RecentModuleRow) | 완료 |
| 2025-12-28 | 3-2 | 검색 컴포넌트 (LauncherSearchBar, SearchResultList) | 완료 |
| 2025-12-28 | 3-3 | 애니메이션 (Phase 3-1에서 구현 완료) | 완료 |
| 2025-12-28 | 4-1 | LauncherScreen 작성 (Scaffold, TopAppBar, LazyColumn) | 완료 |
| 2025-12-28 | 4-2 | MainActivity 수정 (ViewModel 연결, 모듈 실행) | 완료 |
| 2025-12-28 | 5-1 | 기능 테스트 (빌드, 로직 검증) | 완료 |
| 2025-12-28 | 5-2 | UI 개선 (접근성 contentDescription) | 완료 |
| 2025-12-28 | 추가 | 모듈 완료 상태 추적 기능 구현 | 완료 |

---

## 프로젝트 완료 ✅

**통합 런처 구현이 완료되었습니다!**

### 구현된 기능
- 113개 학습 모듈 통합 런처
- 모듈 검색 (실시간 필터링, debounce 300ms)
- 레벨별 그룹화 (5단계: 입문/기초/중급/고급/심화)
- 카테고리별 분류 (16개 카테고리)
- 최근 검색어 저장 (DataStore, 최대 10개)
- 최근 학습 모듈 저장 (DataStore, 최대 10개)
- 펼침/접힘 애니메이션 (AnimatedVisibility, animateContentSize)
- 접근성 지원 (contentDescription)
- **모듈 완료 상태 추적** (2025-12-28 추가)
  - 체크 아이콘으로 완료/미완료 토글
  - 완료된 모듈 배경색 변경
  - 레벨별 완료 진행률 표시 (예: "3/6")
  - DataStore에 완료 상태 영구 저장

### 앱 실행 방법
```bash
./gradlew :app:installDebug
```

### 향후 개선 가능 사항
- 학습 진행률 시각화 (프로그레스 바)
- 즐겨찾기 기능
- 다크 모드 지원 확인

---

## 예외 모듈

다음 모듈은 Application 모듈로 유지됩니다 (통합 런처에서 제외):

| 모듈 | 이유 |
|------|------|
| `hilt_viewmodel` | @HiltAndroidApp 필요 |
