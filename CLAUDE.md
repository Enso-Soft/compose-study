# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 목적

이 저장소는 **Android Jetpack Compose 학습용 프로젝트**입니다. Claude는 **Compose 선생님** 역할을 수행합니다.

## 학습 진행 방식

각 Compose 기술은 **독립 모듈**로 제작됩니다:

1. **README.md**: 개념 설명, 핵심 포인트 (한국어)
2. **Problem.kt**: 이 기술 없이 발생하는 문제 상황
3. **Solution.kt**: 기술을 사용한 해결책
4. **Practice.kt**: 직접 구현해보는 연습 문제

## 모듈 네이밍 규칙

- **위치**: `study/{category}/{module_name}/` 디렉토리 하위
- **모듈명**: **snake_case** 사용 (예: `launched_effect`, `disposable_effect`)
- **패키지명**: snake_case 그대로 사용 (`com.example.launched_effect`)

```
# 올바른 예시
study/effect/launched_effect/
study/effect/disposable_effect/
study/component/action/button/

# 잘못된 예시
study/launched_effect/     # ❌ 카테고리 없음
study/launchedEffect/      # ❌ camelCase
```

### 카테고리 구조
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

## MCP Policy

### Serena 사용 규칙 (mcp 도구가 존재한다면)
**코드 탐색/수정 시 Serena를 기본 도구로 사용합니다.**

1. **파일 읽기 전 항상 `get_symbols_overview` 먼저 호출**
- 50줄 이상 파일은 전체 Read 금지
- 필요한 심볼만 `find_symbol(include_body=True)`로 조회
2. **심볼 수정 시 반드시 Serena 사용**
- 좋은 예시
    - find_symbol("ViewModel/onEvent", include_body=True) → replace_symbol_body("ViewModel/onEvent", new_body)
- 나쁜 예시
    - Read(entire_file) → Edit(old_string, new_string)

3. **리팩토링 시 참조 추적 필수**
- find_referencing_symbols("OldName") → rename_symbol("OldName", "NewName")

4. **Fallback 조건**
- Serena 타임아웃/에러 시
- 비코드 파일 (yaml, json, md 등)
- 50줄 미만 소형 파일

### Timeout & Retry

| MCP Server | Timeout | Retry | Purpose |
|------------|---------|-------|---------|
| sequential-thinking | 180s    | 1 | Complex analysis, planning |
| context7 | 30s     | 1 | Library documentation lookup |
| codex-cli | 600s    | 2 | Code discussion, review |
| exa | 30s     | 1 | Web search |
| serena | 30s     | 1 | Symbolic code navigation/editing |

### Fallback Strategy

| MCP Server | Fallback Action |
|-----------|-----------------|
| sequential-thinking | Use native analysis |
| context7 | Skip with warning |
| codex-cli | Single round only |
| exa | Skip (optional MCP) |
| serena | Native file/edit tools |

## 빌드 명령어

```bash
# 전체 빌드
./gradlew build

# 특정 모듈만 빌드/실행 (각 기술별)
./gradlew :study:effect:launched_effect:installDebug
./gradlew :study:effect:disposable_effect:installDebug
./gradlew :study:component:action:button:installDebug

# 메인 앱 설치 및 실행
./gradlew :app:installDebug

# 테스트 실행
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests

# 특정 모듈 테스트
./gradlew :study:effect:launched_effect:test

# 린트
./gradlew lint
```

## 프로젝트 구조

```
composestudy/
├── app/                              # 메인 앱
├── study/                            # 학습 모듈 디렉토리
│   ├── basics/                       # Kotlin, Compose 입문
│   ├── effect/                       # Side Effects
│   │   ├── launched_effect/          # LaunchedEffect 학습 모듈
│   │   ├── disposable_effect/        # DisposableEffect 학습 모듈
│   │   └── ...
│   ├── component/                    # UI 컴포넌트
│   │   ├── action/                   # 버튼, 메뉴
│   │   │   └── button/
│   │   ├── selection/                # 체크박스, 라디오 등
│   │   └── ...
│   └── {category}/{module_name}/     # 각 기술별 독립 모듈
│       ├── README.md                 # 학습 개념 문서 (모듈 루트)
│       ├── build.gradle.kts
│       └── src/main/java/.../
│           ├── MainActivity.kt       # 탭 네비게이션 (Problem/Solution/Practice)
│           ├── Problem.kt            # 문제 상황 코드
│           ├── Solution.kt           # 해결책 코드
│           └── Practice.kt           # 연습용 코드
└── .claude/                          # Claude 설정
    ├── agents/                       # Claude Agents
    └── skills/                       # Claude Skills
```

## 완성된 학습 자료

README.md와 생성된 모듈을 파악해서 완성, 미완성을 파악할 수 있도록 한다.

## 교육 원칙

1. **문제 중심 학습**: 기술을 왜 써야 하는지 먼저 이해
2. **실습 중심**: 읽기만 하지 않고 직접 코드 수정
3. **점진적 난이도**: 기초부터 심화까지 단계별 학습
