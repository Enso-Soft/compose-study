# Jetpack Compose 학습 바이블

> 처음부터 끝까지, 순서대로 배우는 Jetpack Compose

---

## 학습 로드맵

### Level 0: 사전 준비

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [kotlin](study/basics/kotlin/README.md) | 람다, 확장 함수, 후행 람다, 널 안전성 | - |

### Level 1: Compose 입문

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [compose_introduction](study/basics/compose_introduction/README.md) | Compose 소개, 선언적 UI vs 명령형 UI | kotlin |
| [composable_function](study/basics/composable_function/README.md) | @Composable 함수의 동작 원리 | compose_introduction |
| [preview](study/basics/preview/README.md) | @Preview로 UI 미리보기 | composable_function |

### Level 2: 레이아웃 기초

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [layout_and_modifier](study/layout/layout_and_modifier/README.md) | Column, Row, Box, Modifier 체이닝 | composable_function |
| [basic_ui_components](study/layout/basic_ui_components/README.md) | Text, Button, TextField, Image 기초 | layout_and_modifier |
| [text_typography](study/layout/text_typography/README.md) | 텍스트 스타일링, AnnotatedString | basic_ui_components |
| [rich_text](study/layout/rich_text/README.md) | AnnotatedString 복합 스타일, 클릭 가능 텍스트 | text_typography |
| [auto_sizing_text](study/layout/auto_sizing_text/README.md) | 컨테이너에 맞는 텍스트 크기 자동 조절 | basic_ui_components |
| [flow_layout](study/layout/flow_layout/README.md) | FlowRow, FlowColumn 동적 래핑 | layout_and_modifier |

### Level 3: 상태 기초

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [remember](study/state/remember/README.md) | remember, mutableStateOf로 상태 저장 | basic_ui_components |
| [recomposition](study/state/recomposition/README.md) | Recomposition 동작 이해 | remember |
| [remember_saveable](study/state/remember_saveable/README.md) | 화면 회전에도 상태 유지 | recomposition |

### Level 4: UI 컴포넌트

#### 4-A: 버튼 & 액션

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [button](study/component/action/button/README.md) | Button, IconButton, FloatingActionButton | remember |
| [menu](study/component/action/menu/README.md) | DropdownMenu, 컨텍스트 메뉴 | remember |

#### 4-B: 선택

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [checkbox](study/component/selection/checkbox/README.md) | 다중 선택 체크박스 | remember |
| [radio_button](study/component/selection/radio_button/README.md) | 단일 선택 라디오 버튼 | remember |
| [switch_component](study/component/selection/switch_component/README.md) | 온/오프 스위치 | remember |
| [chip](study/component/selection/chip/README.md) | 필터/선택 칩 | remember |
| [segmented_button](study/component/selection/segmented_button/README.md) | 세그먼트 버튼 그룹 | remember |

#### 4-C: 입력

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [textfield_state](study/component/input/textfield_state/README.md) | TextField 상태 관리, 유효성 검증 | remember |
| [secure_textfield](study/component/input/secure_textfield/README.md) | 비밀번호 입력 필드 (SecureTextField) | textfield_state |
| [autofill](study/component/input/autofill/README.md) | 시스템 자동완성 통합 (로그인, 회원가입) | textfield_state |
| [slider](study/component/input/slider/README.md) | 범위 값 선택 슬라이더 | remember |
| [date_picker](study/component/input/date_picker/README.md) | 날짜 선택기 | remember |
| [time_picker](study/component/input/time_picker/README.md) | 시간 선택기 | remember |

#### 4-D: 표시 & 피드백

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [card](study/component/display/card/README.md) | Card로 콘텐츠 그룹화 | button |
| [divider](study/component/display/divider/README.md) | 구분선 | basic_ui_components |
| [badge](study/component/display/badge/README.md) | 알림 배지 | basic_ui_components |
| [tooltip](study/component/display/tooltip/README.md) | 도움말 툴팁 | basic_ui_components |
| [progress_indicator](study/component/display/progress_indicator/README.md) | 로딩 인디케이터 | basic_ui_components |
| [dialog](study/component/display/dialog/README.md) | AlertDialog, 커스텀 Dialog | remember |

### Level 5: 리스트 & 검색

#### 5-A: 리스트

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [lazy_list](study/list/lazy_list/README.md) | LazyColumn, LazyRow 기초 | remember |
| [lazy_grid](study/list/lazy_grid/README.md) | LazyVerticalGrid, LazyHorizontalGrid, StaggeredGrid | lazy_list |
| [lazy_layouts](study/list/lazy_layouts/README.md) | key, contentType, 성능 최적화 | lazy_list |
| [pull_to_refresh](study/list/pull_to_refresh/README.md) | 당겨서 새로고침 | lazy_list |
| [paging_compose](study/list/paging_compose/README.md) | Paging 3 무한 스크롤 | lazy_layouts |

#### 5-B: 스와이프 & 갤러리

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [pager](study/list/pager/README.md) | HorizontalPager, VerticalPager | lazy_list |
| [carousel](study/list/carousel/README.md) | 가로 스와이프 갤러리 | pager |

#### 5-C: 검색

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [search_bar](study/search/search_bar/README.md) | SearchBar 기초 | textfield_state |
| [search_bar_advanced](study/search/search_bar_advanced/README.md) | 검색 자동완성, 디바운스 | search_bar |

### Level 6: 앱 구조

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [material_symbols](study/structure/material_symbols/README.md) | Material Symbols 아이콘 시스템 | composable_function |
| [scaffold](study/structure/scaffold/README.md) | Scaffold 기본 구조 | layout_and_modifier |
| [window_insets](study/structure/window_insets/README.md) | 시스템 바, Edge-to-Edge | scaffold |
| [app_bar](study/structure/app_bar/README.md) | TopAppBar, BottomAppBar | window_insets |
| [navigation_bar](study/structure/navigation_bar/README.md) | 하단 네비게이션 바 | scaffold |
| [tabs](study/structure/tabs/README.md) | TabRow, 탭 전환 | scaffold |
| [scaffold_and_theming](study/structure/scaffold_and_theming/README.md) | MaterialTheme, 다크모드 | scaffold |
| [snackbar](study/structure/snackbar/README.md) | Snackbar 메시지 | scaffold |
| [bottom_sheet](study/structure/bottom_sheet/README.md) | ModalBottomSheet 기초 | scaffold |
| [bottom_sheet_advanced](study/structure/bottom_sheet_advanced/README.md) | SheetState, 중첩 시트 | bottom_sheet |
| [adaptive_layout](study/structure/adaptive_layout/README.md) | 태블릿/폴더블 반응형 레이아웃 | window_insets |
| [material_expressive](study/structure/material_expressive/README.md) | Material 3 Expressive, 스프링 물리 모션 | scaffold_and_theming |

### Level 7: Side Effects

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [side_effect](study/effect/side_effect/README.md) | Side Effect 개념 이해 | recomposition |
| [launched_effect](study/effect/launched_effect/README.md) | 비동기 작업 실행 (API 호출 등) | side_effect |
| [disposable_effect](study/effect/disposable_effect/README.md) | 리소스 정리 (리스너 해제 등) | launched_effect |
| [remember_coroutine_scope](study/effect/remember_coroutine_scope/README.md) | 이벤트 핸들러에서 코루틴 실행 | launched_effect |
| [produce_state](study/effect/produce_state/README.md) | 비동기 데이터를 State로 변환 | launched_effect |
| [effect_handlers_advanced](study/effect/effect_handlers_advanced/README.md) | snapshotFlow, rememberUpdatedState | disposable_effect |

### Level 8: 상태 심화

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [state_hoisting](study/state/state_hoisting/README.md) | 상태 끌어올리기, Stateless 컴포넌트 | remember_saveable |
| [derived_state_of](study/state/derived_state_of/README.md) | 파생 상태로 불필요한 Recomposition 방지 | state_hoisting |
| [snapshot_system](study/state/snapshot_system/README.md) | snapshotFlow로 State를 Flow로 변환 | derived_state_of |
| [stability](study/state/stability/README.md) | @Stable, @Immutable로 성능 최적화 | derived_state_of |
| [state_management_advanced](study/state/state_management_advanced/README.md) | StateFlow vs SharedFlow vs Channel | stability |
| [state_restoration_advanced](study/state/state_restoration_advanced/README.md) | 프로세스 종료 후 상태 복원 | state_management_advanced |
| [retain](study/state/retain/README.md) | 직렬화 없이 Config Change 상태 유지 | remember_saveable |

### Level 9: 네비게이션

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [navigation_basics](study/navigation/navigation_basics/README.md) | Type-Safe Navigation 기초 | state_hoisting |
| [navigation_3](study/navigation/navigation_3/README.md) | Navigation 3 (2025 최신) | navigation_basics |
| [deep_link](study/navigation/deep_link/README.md) | 딥링크로 특정 화면 열기 | navigation_basics |
| [back_handler](study/navigation/back_handler/README.md) | 뒤로가기 커스텀 처리 | navigation_basics |
| [predictive_back](study/navigation/predictive_back/README.md) | 예측 뒤로가기 제스처 (Android 14+) | back_handler |
| [navigation_drawer](study/navigation/navigation_drawer/README.md) | 사이드 드로어 메뉴 | navigation_basics |
| [navigation_rail](study/navigation/navigation_rail/README.md) | 태블릿용 레일 네비게이션 | navigation_basics |

### Level 10: 애니메이션

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [animation_basics](study/animation/animation_basics/README.md) | animate*AsState, AnimatedVisibility | remember |
| [animation_advanced](study/animation/animation_advanced/README.md) | updateTransition, Animatable | animation_basics |
| [animation_physics](study/animation/animation_physics/README.md) | spring/decay 물리 기반 애니메이션 | animation_advanced |
| [animate_bounds](study/animation/animate_bounds/README.md) | LookaheadScope, 레이아웃 애니메이션 | animation_advanced |
| [shared_element_transition](study/animation/shared_element_transition/README.md) | 화면 전환 시 공유 요소 애니메이션 | animation_advanced, navigation_basics |

### Level 11: 커스텀 레이아웃

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [intrinsic_measurements](study/layout/intrinsic_measurements/README.md) | IntrinsicSize로 자식 크기 동기화 | layout_and_modifier |
| [constraint_layout](study/layout/constraint_layout/README.md) | 제약 조건 기반 복잡한 레이아웃 | layout_and_modifier |
| [custom_layout](study/layout/custom_layout/README.md) | Layout() 커스텀 레이아웃 작성 | constraint_layout |
| [custom_modifier](study/layout/custom_modifier/README.md) | Modifier.Node로 커스텀 Modifier 생성 | custom_layout |
| [canvas_drawing](study/layout/canvas_drawing/README.md) | Canvas로 커스텀 그래픽 | layout_and_modifier |

### Level 12: 아키텍처

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [view_model](study/architecture/view_model/README.md) | ViewModel + Compose 통합 | state_hoisting |
| [hilt_viewmodel](study/architecture/hilt_viewmodel/README.md) | @HiltViewModel, 의존성 주입 | view_model |
| [screen_and_component](study/architecture/screen_and_component/README.md) | Screen vs Component 분리 패턴 | state_hoisting |
| [slot_api_pattern](study/architecture/slot_api_pattern/README.md) | Slot API로 유연한 컴포넌트 설계 | screen_and_component |
| [composition_local](study/architecture/composition_local/README.md) | 암시적 데이터 전달 | slot_api_pattern |
| [unidirectional_data_flow](study/architecture/unidirectional_data_flow/README.md) | 단방향 데이터 흐름 (UDF), MVI 패턴 | view_model |

### Level 13: 제스처 & 인터랙션

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [gesture](study/interaction/gesture/README.md) | 탭, 드래그, 스와이프 제스처 | remember |
| [gesture_advanced](study/interaction/gesture_advanced/README.md) | pointerInput 고급 제스처, 멀티터치 | gesture |
| [focus_management](study/interaction/focus_management/README.md) | 포커스 순서, 키보드 처리 | gesture |
| [drag_and_drop](study/interaction/drag_and_drop/README.md) | 드래그 앤 드롭 기능 | gesture |
| [haptic_feedback](study/interaction/haptic_feedback/README.md) | 진동/햅틱 피드백 | gesture |

### Level 14: 외부 통합

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [lifecycle_integration](study/integration/lifecycle_integration/README.md) | LifecycleOwner 연동 | disposable_effect |
| [image_loading](study/integration/image_loading/README.md) | Coil로 이미지 로딩 | basic_ui_components |
| [media3_player](study/integration/media3_player/README.md) | ExoPlayer + Compose | lifecycle_integration |
| [interoperability](study/integration/interoperability/README.md) | View ↔ Compose 상호운용 | lifecycle_integration |
| [camerax_compose](study/integration/camerax_compose/README.md) | CameraX + Compose | interoperability |

### Level 15: 시스템 통합

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [permission_handling](study/system/permission_handling/README.md) | 런타임 권한 요청 | launched_effect |
| [notification_integration](study/system/notification_integration/README.md) | 알림 생성 및 처리 | permission_handling |
| [audio_recording](study/system/audio_recording/README.md) | 오디오 녹음 | permission_handling |

### Level 16: 테스트 & 성능

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [semantics_accessibility](study/testing/semantics_accessibility/README.md) | 접근성, 스크린 리더 지원 | basic_ui_components |
| [compose_testing](study/testing/compose_testing/README.md) | UI 테스트, Semantics | semantics_accessibility |
| [compose_preview_testing](study/testing/compose_preview_testing/README.md) | @PreviewParameter로 다중 Preview 테스트 | compose_testing |
| [screenshot_testing](study/testing/screenshot_testing/README.md) | 스크린샷 테스트, Paparazzi | compose_testing |
| [compose_compiler_metrics](study/testing/compose_compiler_metrics/README.md) | 컴파일러 리포트 분석 | stability |
| [baseline_profiles](study/testing/baseline_profiles/README.md) | 앱 시작 성능 최적화 | compose_compiler_metrics |
| [pausable_composition](study/testing/pausable_composition/README.md) | Jank 방지, Composition 일시중단 | baseline_profiles |
| [visibility_tracking](study/testing/visibility_tracking/README.md) | 화면 노출 추적 (광고, 분석) | lazy_layouts |
| [strong_skipping_mode](study/testing/strong_skipping_mode/README.md) | Strong Skipping Mode 성능 최적화 | stability |

### Level 17: 멀티플랫폼

| 모듈 | 설명 | 선행 학습 |
|------|------|----------|
| [compose_multiplatform_intro](study/multiplatform/compose_multiplatform_intro/README.md) | Compose Multiplatform 입문, expect/actual 패턴 | composable_function |
| [desktop_extensions](study/multiplatform/desktop_extensions/README.md) | Desktop 전용 API (MenuBar, Tray, KeyShortcut) | compose_multiplatform_intro |
| [web_wasm](study/multiplatform/web_wasm/README.md) | Kotlin/Wasm으로 웹에서 Compose 실행 | compose_multiplatform_intro |

---

## 카테고리별 인덱스

> 특정 컴포넌트를 찾고 있다면 여기서 검색하세요

### Actions (액션)

| 모듈 | 언제 사용? |
|------|-----------|
| [button](study/component/action/button/README.md) | 클릭 가능한 버튼이 필요할 때 |
| [menu](study/component/action/menu/README.md) | 드롭다운 메뉴가 필요할 때 |
| [segmented_button](study/component/selection/segmented_button/README.md) | 여러 옵션 중 하나를 선택할 때 |

### Communication (알림/피드백)

| 모듈 | 언제 사용? |
|------|-----------|
| [badge](study/component/display/badge/README.md) | 아이콘에 알림 숫자를 표시할 때 |
| [progress_indicator](study/component/display/progress_indicator/README.md) | 로딩 상태를 보여줄 때 |
| [snackbar](study/structure/snackbar/README.md) | 짧은 메시지를 하단에 표시할 때 |
| [tooltip](study/component/display/tooltip/README.md) | 요소에 대한 추가 설명이 필요할 때 |

### Containment (컨테이너)

| 모듈 | 언제 사용? |
|------|-----------|
| [app_bar](study/structure/app_bar/README.md) | 화면 상단에 제목과 액션을 배치할 때 |
| [bottom_sheet](study/structure/bottom_sheet/README.md) | 하단에서 올라오는 추가 콘텐츠가 필요할 때 |
| [card](study/component/display/card/README.md) | 관련 정보를 그룹화할 때 |
| [carousel](study/list/carousel/README.md) | 가로 스와이프 갤러리가 필요할 때 |
| [dialog](study/component/display/dialog/README.md) | 사용자 확인이나 입력이 필요할 때 |
| [divider](study/component/display/divider/README.md) | 콘텐츠 영역을 구분할 때 |
| [lazy_list](study/list/lazy_list/README.md) | 긴 목록을 효율적으로 표시할 때 |
| [scaffold](study/structure/scaffold/README.md) | 앱 화면의 기본 구조를 잡을 때 |

### Navigation (네비게이션)

| 모듈 | 언제 사용? |
|------|-----------|
| [navigation_basics](study/navigation/navigation_basics/README.md) | 화면 간 이동이 필요할 때 |
| [navigation_bar](study/structure/navigation_bar/README.md) | 하단 탭 네비게이션이 필요할 때 |
| [navigation_drawer](study/navigation/navigation_drawer/README.md) | 사이드 메뉴가 필요할 때 |
| [navigation_rail](study/navigation/navigation_rail/README.md) | 태블릿에서 좌측 네비게이션이 필요할 때 |
| [back_handler](study/navigation/back_handler/README.md) | 뒤로가기 커스텀 처리가 필요할 때 |
| [predictive_back](study/navigation/predictive_back/README.md) | Android 14+ 예측 뒤로가기가 필요할 때 |
| [search_bar](study/search/search_bar/README.md) | 기본 검색 기능이 필요할 때 |
| [search_bar_advanced](study/search/search_bar_advanced/README.md) | 검색 기록, 디바운스 등 고급 기능이 필요할 때 |
| [tabs](study/structure/tabs/README.md) | 상단 탭으로 콘텐츠를 전환할 때 |

### Selection (선택)

| 모듈 | 언제 사용? |
|------|-----------|
| [checkbox](study/component/selection/checkbox/README.md) | 여러 항목을 다중 선택할 때 |
| [chip](study/component/selection/chip/README.md) | 필터나 태그를 선택할 때 |
| [date_picker](study/component/input/date_picker/README.md) | 날짜를 선택할 때 |
| [radio_button](study/component/selection/radio_button/README.md) | 여러 항목 중 하나만 선택할 때 |
| [slider](study/component/input/slider/README.md) | 범위 내 값을 선택할 때 |
| [switch_component](study/component/selection/switch_component/README.md) | 온/오프 토글이 필요할 때 |
| [time_picker](study/component/input/time_picker/README.md) | 시간을 선택할 때 |

### Text Input (텍스트 입력)

| 모듈 | 언제 사용? |
|------|-----------|
| [textfield_state](study/component/input/textfield_state/README.md) | 텍스트 입력 필드가 필요할 때 |
| [secure_textfield](study/component/input/secure_textfield/README.md) | 비밀번호 입력 필드가 필요할 때 |
| [autofill](study/component/input/autofill/README.md) | 로그인/회원가입 자동완성이 필요할 때 |

### Text & Typography (텍스트 스타일링)

| 모듈 | 언제 사용? |
|------|-----------|
| [text_typography](study/layout/text_typography/README.md) | 텍스트 기본 스타일링이 필요할 때 |
| [rich_text](study/layout/rich_text/README.md) | 부분 스타일, 클릭 가능한 텍스트가 필요할 때 |
| [auto_sizing_text](study/layout/auto_sizing_text/README.md) | 컨테이너에 맞게 텍스트 크기를 자동 조절할 때 |

### Gestures (제스처)

| 모듈 | 언제 사용? |
|------|-----------|
| [gesture](study/interaction/gesture/README.md) | 기본 탭, 드래그, 스와이프가 필요할 때 |
| [gesture_advanced](study/interaction/gesture_advanced/README.md) | 멀티터치, 복잡한 제스처가 필요할 때 |
| [drag_and_drop](study/interaction/drag_and_drop/README.md) | 드래그 앤 드롭이 필요할 때 |
| [haptic_feedback](study/interaction/haptic_feedback/README.md) | 터치 진동 피드백이 필요할 때 |

### Icons (아이콘)

| 모듈 | 언제 사용? |
|------|-----------|
| [material_symbols](study/structure/material_symbols/README.md) | Material Symbols 아이콘 시스템이 필요할 때 |

### Multiplatform (멀티플랫폼)

| 모듈 | 언제 사용? |
|------|-----------|
| [compose_multiplatform_intro](study/multiplatform/compose_multiplatform_intro/README.md) | Android/iOS/Desktop/Web 코드 공유가 필요할 때 |
| [desktop_extensions](study/multiplatform/desktop_extensions/README.md) | Desktop 앱에 메뉴바, 트레이, 단축키가 필요할 때 |
| [web_wasm](study/multiplatform/web_wasm/README.md) | 웹 브라우저에서 Compose를 실행할 때 |

---

## 키워드로 찾기

> "이런 상황인데 뭘 써야 하지?"

### 상태 & 데이터

| 키워드 | 관련 모듈 |
|--------|-----------|
| 상태 저장 | [remember](study/state/remember/README.md), [remember_saveable](study/state/remember_saveable/README.md) |
| 화면 회전 | [remember_saveable](study/state/remember_saveable/README.md), [retain](study/state/retain/README.md), [state_restoration_advanced](study/state/state_restoration_advanced/README.md) |
| 상태 끌어올리기 | [state_hoisting](study/state/state_hoisting/README.md) |
| 파생 상태 | [derived_state_of](study/state/derived_state_of/README.md) |
| snapshotFlow | [snapshot_system](study/state/snapshot_system/README.md), [effect_handlers_advanced](study/effect/effect_handlers_advanced/README.md) |
| Flow/Channel | [state_management_advanced](study/state/state_management_advanced/README.md) |

### 비동기 & Side Effect

| 키워드 | 관련 모듈 |
|--------|-----------|
| API 호출 | [launched_effect](study/effect/launched_effect/README.md), [remember_coroutine_scope](study/effect/remember_coroutine_scope/README.md) |
| 리스너 등록/해제 | [disposable_effect](study/effect/disposable_effect/README.md) |
| 코루틴 실행 | [remember_coroutine_scope](study/effect/remember_coroutine_scope/README.md) |
| 비동기 → State | [produce_state](study/effect/produce_state/README.md) |
| snapshotFlow | [effect_handlers_advanced](study/effect/effect_handlers_advanced/README.md) |

### 리스트 & 스크롤

| 키워드 | 관련 모듈 |
|--------|-----------|
| 긴 목록 | [lazy_list](study/list/lazy_list/README.md), [lazy_grid](study/list/lazy_grid/README.md), [lazy_layouts](study/list/lazy_layouts/README.md) |
| 무한 스크롤 | [paging_compose](study/list/paging_compose/README.md) |
| 페이지 스와이프 | [pager](study/list/pager/README.md), [carousel](study/list/carousel/README.md) |
| 당겨서 새로고침 | [pull_to_refresh](study/list/pull_to_refresh/README.md) |
| 화면 노출 추적 | [visibility_tracking](study/testing/visibility_tracking/README.md) |

### 네비게이션

| 키워드 | 관련 모듈 |
|--------|-----------|
| 화면 이동 | [navigation_basics](study/navigation/navigation_basics/README.md), [navigation_3](study/navigation/navigation_3/README.md) |
| 딥링크 | [deep_link](study/navigation/deep_link/README.md) |
| 뒤로가기 | [back_handler](study/navigation/back_handler/README.md), [predictive_back](study/navigation/predictive_back/README.md) |
| 예측 뒤로가기 | [predictive_back](study/navigation/predictive_back/README.md) |
| 하단 탭 | [navigation_bar](study/structure/navigation_bar/README.md) |
| 사이드 메뉴 | [navigation_drawer](study/navigation/navigation_drawer/README.md) |

### 애니메이션

| 키워드 | 관련 모듈 |
|--------|-----------|
| 값 애니메이션 | [animation_basics](study/animation/animation_basics/README.md) |
| 복잡한 전환 | [animation_advanced](study/animation/animation_advanced/README.md) |
| 물리 애니메이션 | [animation_physics](study/animation/animation_physics/README.md) |
| spring/decay | [animation_physics](study/animation/animation_physics/README.md) |
| 레이아웃 변경 | [animate_bounds](study/animation/animate_bounds/README.md) |
| 화면 전환 효과 | [shared_element_transition](study/animation/shared_element_transition/README.md) |

### 성능 & 테스트

| 키워드 | 관련 모듈 |
|--------|-----------|
| Recomposition 최적화 | [stability](study/state/stability/README.md), [derived_state_of](study/state/derived_state_of/README.md) |
| 컴파일러 분석 | [compose_compiler_metrics](study/testing/compose_compiler_metrics/README.md) |
| 앱 시작 속도 | [baseline_profiles](study/testing/baseline_profiles/README.md) |
| Jank 방지 | [pausable_composition](study/testing/pausable_composition/README.md) |
| UI 테스트 | [compose_testing](study/testing/compose_testing/README.md) |
| Preview 테스트 | [compose_preview_testing](study/testing/compose_preview_testing/README.md) |
| 스크린샷 테스트 | [screenshot_testing](study/testing/screenshot_testing/README.md) |

### 외부 연동

| 키워드 | 관련 모듈 |
|--------|-----------|
| 이미지 로딩 | [image_loading](study/integration/image_loading/README.md) |
| 비디오 재생 | [media3_player](study/integration/media3_player/README.md) |
| 카메라 | [camerax_compose](study/integration/camerax_compose/README.md) |
| 권한 요청 | [permission_handling](study/system/permission_handling/README.md) |
| 알림 | [notification_integration](study/system/notification_integration/README.md) |
| 기존 View 사용 | [interoperability](study/integration/interoperability/README.md) |

### 텍스트 스타일링

| 키워드 | 관련 모듈 |
|--------|-----------|
| 부분 스타일 | [rich_text](study/layout/rich_text/README.md) |
| AnnotatedString | [rich_text](study/layout/rich_text/README.md), [text_typography](study/layout/text_typography/README.md) |
| 클릭 가능한 텍스트 | [rich_text](study/layout/rich_text/README.md) |
| 자동 크기 조절 | [auto_sizing_text](study/layout/auto_sizing_text/README.md) |

### 제스처 & 인터랙션

| 키워드 | 관련 모듈 |
|--------|-----------|
| 멀티터치 | [gesture_advanced](study/interaction/gesture_advanced/README.md) |
| 커스텀 제스처 | [gesture_advanced](study/interaction/gesture_advanced/README.md) |
| 진동 피드백 | [haptic_feedback](study/interaction/haptic_feedback/README.md) |
| pointerInput | [gesture_advanced](study/interaction/gesture_advanced/README.md) |

### 레이아웃 심화

| 키워드 | 관련 모듈 |
|--------|-----------|
| IntrinsicSize | [intrinsic_measurements](study/layout/intrinsic_measurements/README.md) |
| 자식 크기 동기화 | [intrinsic_measurements](study/layout/intrinsic_measurements/README.md) |
| 커스텀 레이아웃 | [custom_layout](study/layout/custom_layout/README.md) |

### 멀티플랫폼

| 키워드 | 관련 모듈 |
|--------|-----------|
| Desktop 앱 | [desktop_extensions](study/multiplatform/desktop_extensions/README.md) |
| 웹 앱 | [web_wasm](study/multiplatform/web_wasm/README.md) |
| 코드 공유 | [compose_multiplatform_intro](study/multiplatform/compose_multiplatform_intro/README.md) |
| MenuBar/Tray | [desktop_extensions](study/multiplatform/desktop_extensions/README.md) |
| Kotlin/Wasm | [web_wasm](study/multiplatform/web_wasm/README.md) |

---

## 공식 문서 링크

### Google 공식 코스
- [Android Basics with Compose](https://developer.android.com/courses/android-basics-compose/course) - 입문자용
- [Jetpack Compose for Android Developers](https://developer.android.com/courses/jetpack-compose/course) - 기존 개발자용

### 공식 문서
- [Compose Documentation](https://developer.android.com/develop/ui/compose/documentation) - 전체 문서
- [State and Jetpack Compose](https://developer.android.com/develop/ui/compose/state)
- [Side-effects in Compose](https://developer.android.com/develop/ui/compose/side-effects)
- [Compose Performance](https://developer.android.com/develop/ui/compose/performance)

### Material Design 3
- [M3 Components](https://m3.material.io/components) - 컴포넌트 가이드

---

## 기여하기

학습 모듈 추가나 개선은 언제나 환영합니다!
