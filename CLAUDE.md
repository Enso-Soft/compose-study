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

- **위치**: `study/{module_name}/` 디렉토리 하위
- **모듈명**: **snake_case** 사용 (예: `launched_effect`, `disposable_effect`)
- **패키지명**: snake_case 그대로 사용 (`com.example.launched_effect`)

```
# 올바른 예시
study/launched_effect/
study/disposable_effect/
study/remember_coroutine_scope/

# 잘못된 예시
launchedeffect/          # ❌ study/ 없음, snake_case 아님
study/launchedEffect/    # ❌ camelCase
```

## 빌드 명령어

```bash
# 전체 빌드
./gradlew build

# 특정 모듈만 빌드/실행 (각 기술별)
./gradlew :study:launched_effect:installDebug
./gradlew :study:disposable_effect:installDebug

# 메인 앱 설치 및 실행
./gradlew :app:installDebug

# 테스트 실행
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests

# 특정 모듈 테스트
./gradlew :study:launched_effect:test

# 린트
./gradlew lint
```

## 프로젝트 구조

```
composestudy/
├── app/                              # 메인 앱
├── study/                            # 학습 모듈 디렉토리
│   ├── launched_effect/              # LaunchedEffect 학습 모듈
│   ├── disposable_effect/            # DisposableEffect 학습 모듈
│   ├── remember_coroutine_scope/     # (예정)
│   └── {module_name}/                # 각 기술별 독립 모듈
│       ├── build.gradle.kts
│       └── src/main/java/.../
│           ├── MainActivity.kt       # 탭 네비게이션 (Problem/Solution/Practice)
│           ├── README.md             # 학습 개념 문서
│           ├── Problem.kt            # 문제 상황 코드
│           ├── Solution.kt           # 해결책 코드
│           └── Practice.kt           # 연습용 코드
└── .claude/skills/                   # Claude Skills
    └── compose-module/               # 모듈 생성 자동화 스킬
```

## 완성된 학습 자료

README.md와 생성된 모듈을 파악해서 완성, 미완성을 파악할 수 있도록 한다.

## 교육 원칙

1. **문제 중심 학습**: 기술을 왜 써야 하는지 먼저 이해
2. **실습 중심**: 읽기만 하지 않고 직접 코드 수정
3. **점진적 난이도**: 기초부터 심화까지 단계별 학습
