---
name: compose-study
description: Jetpack Compose 학습 모듈을 생성합니다. "모듈 만들어줘", "LaunchedEffect 학습", "Side Effect 모듈", "다음 스터디 진행해줘" 등의 요청 시 사용합니다.
---

# Compose 학습 모듈 생성기

Jetpack Compose 기술별 학습 모듈을 표준화된 구조로 생성하는 스킬입니다.

## 사용 시점

- 사용자가 "XXX 모듈 만들어줘" 요청 시
- 새로운 Compose 기술 학습 자료 생성 시
- "다음 스터디 진행해줘" 요청 시

---

## 로드맵 확인 방법 (Single Source of Truth)

**별도 roadmap.md 파일 대신 프로젝트 README.md를 활용합니다.**

### 진행 상황 파악 절차

1. **README.md 읽기**
   - 프로젝트 최상단 `README.md`의 "이 저장소의 학습 모듈" 섹션 확인
   - 로드맵 순서 파악

2. **실제 모듈 확인**
   ```bash
   ls study/*/build.gradle.kts
   ```
   - 존재하는 모듈 목록 확인

3. **미구현 모듈 선택**
   - README.md 로드맵 순서에서 아직 생성되지 않은 **다음 모듈** 선택
   - **한 번에 하나의 모듈만** 생성

### 예시
```
README.md 로드맵: LaunchedEffect → rememberCoroutineScope → DisposableEffect → SideEffect
실제 모듈: study/launched_effect, study/disposable_effect

→ 다음 생성 모듈: remember_coroutine_scope (순서상 다음)
```

---

## 필수 워크플로우 (반드시 준수!)

모듈 생성 시 아래 단계를 **반드시** 순서대로 수행해야 합니다.

### Phase 1: 연구 (Research)

#### Step 1: 웹 검색
- 해당 기술에 대해 **2025년 최신 기준**으로 웹 검색 수행
- 공식 문서 (developer.android.com) 우선 참조
- 최신 베스트 프랙티스 확인

#### Step 2: Sequential Thinking (최소 5회 필수!)
`mcp__sequential-thinking__sequentialthinking` 도구를 사용하여 **최소 5번 이상** 사고 과정을 거쳐야 합니다.

```
필수 사고 주제:
1. 검색 결과 분석 및 핵심 개념 정리
2. 이 기술이 해결하는 문제 상황 도출
3. 올바른 해결책 구조화
4. 실습 예제 시나리오 설계
5. 연습 문제 3개 구체화
```

**5회 미만 시 다음 단계로 진행할 수 없습니다!**

### Phase 2: 설계 & 계획 (Design & Plan)

#### Step 3: 모듈 설계
- README.md 구조 설계
- Problem.kt / Solution.kt 시나리오 확정
- Practice.kt 연습 문제 확정

#### Step 4: 구현 계획 수립
- 파일 생성 순서 결정
- 의존성 확인

### Phase 3: 구현 (Implementation)

#### Step 5: 스터디 모듈 구현
- 계획에 따라 파일 생성
- templates.md 참조하여 표준 구조 준수

#### Step 6: README.md 하이퍼링크 추가
- 프로젝트 최상단 README.md의 모듈 표 업데이트
- 형식: `📁 [기술명](study/module_name)`

### Phase 4: 검증 (Validation)

#### Step 7: Sequential Thinking 퀄리티 체크 (최소 3회 필수!)
`mcp__sequential-thinking__sequentialthinking` 도구를 사용하여 **최소 3번 이상** 검증해야 합니다.

```
필수 검증 주제:
1. 개념 설명의 정확성 검증
2. 코드 예제 동작 확인
3. 연습 문제 난이도 및 힌트 적절성
```

**3회 미만 시 완료 처리할 수 없습니다!**

#### Step 8: 수정
- 검증 과정에서 문제 발견 시 **즉시 수정**
- 수정 후 재검증

---

## 네이밍 규칙 (중요!)

### 모듈 위치 및 이름
- **위치**: `study/{module_name}/` 디렉토리 하위
- **모듈명**: **snake_case** 사용
- **패키지명**: snake_case 그대로 사용

```
# 올바른 예시
study/launched_effect/           → com.example.launched_effect
study/disposable_effect/         → com.example.disposable_effect
study/remember_coroutine_scope/  → com.example.remember_coroutine_scope

# 잘못된 예시
launchedeffect/                  # ❌ study/ 없음, snake_case 아님
study/launchedEffect/            # ❌ camelCase
study/LaunchedEffect/            # ❌ PascalCase
```

---

## 모듈 구조

```
study/{module_name}/
├── build.gradle.kts
├── proguard-rules.pro
├── src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/example/{module_name}/
│   │   ├── MainActivity.kt      # 탭 네비게이션 (Problem/Solution/Practice)
│   │   ├── README.md            # 개념 설명 (한국어)
│   │   ├── Problem.kt           # 문제 상황 코드
│   │   ├── Solution.kt          # 해결책 코드
│   │   ├── Practice.kt          # 연습 문제 3개
│   │   └── ui/theme/            # Material3 테마
│   └── res/                     # 리소스 파일
```

---

## 생성 단계 요약

### 1. 모듈 정보 확인
- 모듈명 (snake_case)
- 핵심 문제 상황
- 해결 방법
- 연습 문제 주제 3개

### 2. 기존 모듈 패턴 참조
- `study/launched_effect` 모듈을 템플릿으로 사용
- `templates.md` 참조

### 3. 핵심 파일 생성

#### README.md 구조
```markdown
# {기술명} 학습

## 개념
{기술이 무엇인지 설명}

## 핵심 특징
1. 특징 1
2. 특징 2
...

## 문제 상황: {기술 없이 발생하는 문제}
### 잘못된 코드 예시
### 발생하는 문제점

## 해결책: {기술} 사용
### 올바른 코드
### 해결되는 이유

## 사용 시나리오
1. 시나리오 1
2. 시나리오 2
...

## 주의사항

## 연습 문제

## 다음 학습
```

#### Problem.kt 구조
- 문제 상황을 보여주는 코드
- 위험한 코드는 **주석 처리**
- `SideEffect`로 Recomposition 횟수 추적
- 문제 설명 Card 포함

#### Solution.kt 구조
- 올바른 해결책 코드
- 인터랙티브 버튼으로 동작 확인
- 핵심 포인트 Card 포함

#### Practice.kt 구조
- 3개의 연습 문제 함수
- 각 연습은 TODO 힌트 + 주석 처리된 정답
- 힌트 Card 포함

#### MainActivity.kt 구조
- `TabRow`로 Problem/Solution/Practice 탭
- `PracticeNavigator`로 연습 문제 선택

### 4. 모듈 등록
```kotlin
// settings.gradle.kts
include(":study:{module_name}")
```

### 5. README.md 업데이트
프로젝트 최상단 README.md의 모듈 표에 추가 및 내용에 해당 기술명 하이퍼링크 적용:
```markdown
| 📁 [{기술명}](study/{module_name}) | {기술명} | ✅ 완료 | `./gradlew :study:{module_name}:installDebug` |
```

### 6. 빌드 확인
```bash
./gradlew :study:{module_name}:compileDebugKotlin
```

---

## 예시

### 요청
```
DisposableEffect 모듈 만들어줘
```

### 응답 흐름
1. 웹 검색: "DisposableEffect Android Compose 2025"
2. Sequential Thinking 5회 (개념 분석, 문제 도출, 해결책, 시나리오, 연습문제)
3. 설계 및 계획 수립
4. study/disposable_effect 디렉토리 생성
5. 파일 생성 (build.gradle.kts, AndroidManifest.xml, 테마, README.md, Problem.kt, Solution.kt, Practice.kt, MainActivity.kt)
6. settings.gradle.kts 등록
7. 프로젝트 README.md에 하이퍼링크 추가
8. Sequential Thinking 3회 (퀄리티 검증)
9. 빌드 확인

---

## 주의사항

1. **모듈 위치**: 반드시 `study/` 하위에 생성
2. **snake_case**: 모듈명, 패키지명 모두 snake_case
3. **한국어 문자열 보간**: `$variable회` → `${variable}회`
4. **compileSdk**: app 모듈과 동일한 버전 사용 (현재 36)
5. **drawable 리소스**: app 모듈에서 복사 필요
6. **LocalLifecycleOwner**: `androidx.lifecycle.compose` 패키지 사용
7. **Sequential Thinking 횟수**: 연구 5회, 검증 3회 필수 충족!
