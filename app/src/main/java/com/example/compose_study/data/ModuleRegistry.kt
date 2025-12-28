package com.example.compose_study.data

import com.example.compose_study.model.Category
import com.example.compose_study.model.StudyModule

/**
 * 모든 학습 모듈을 등록하는 레지스트리
 *
 * 113개의 study 모듈을 등록합니다.
 * (hilt_viewmodel 제외 - Application 모듈로 유지)
 *
 * Level 체계는 README.md 학습 로드맵 기준 (Level 0-17)
 */
object ModuleRegistry {

    /**
     * 등록된 모든 모듈 목록
     */
    val modules: List<StudyModule> by lazy {
        listOf(
            // ============================================================
            // Level 0: 사전 준비
            // ============================================================
            StudyModule(
                id = "kotlin",
                name = "Kotlin 기초",
                description = "람다, 확장 함수, 후행 람다, 널 안전성",
                detailDescription = "Compose 개발에 필요한 Kotlin 기초 문법을 학습합니다. 람다 표현식, 확장 함수, 후행 람다, 널 안전성 등을 다룹니다.",
                level = 0,
                category = Category.BASICS,
                activityClass = com.example.kotlin.MainActivity::class.java
            ),

            // ============================================================
            // Level 1: Compose 입문
            // ============================================================
            StudyModule(
                id = "compose_introduction",
                name = "Compose 소개",
                description = "Compose 소개, 선언적 UI vs 명령형 UI",
                detailDescription = "Jetpack Compose의 기본 개념과 선언적 UI 패러다임을 이해합니다.",
                level = 1,
                category = Category.BASICS,
                prerequisites = listOf("kotlin"),
                activityClass = com.example.compose_introduction.MainActivity::class.java
            ),
            StudyModule(
                id = "composable_function",
                name = "Composable 함수",
                description = "@Composable 함수의 동작 원리",
                detailDescription = "@Composable 어노테이션과 Composable 함수의 특성을 학습합니다.",
                level = 1,
                category = Category.BASICS,
                prerequisites = listOf("compose_introduction"),
                activityClass = com.example.composable_function.MainActivity::class.java
            ),
            StudyModule(
                id = "preview",
                name = "Preview",
                description = "@Preview로 UI 미리보기",
                detailDescription = "@Preview 어노테이션을 사용하여 Composable을 Android Studio에서 미리 확인하는 방법을 배웁니다.",
                level = 1,
                category = Category.BASICS,
                prerequisites = listOf("composable_function"),
                activityClass = com.example.preview.MainActivity::class.java
            ),

            // ============================================================
            // Level 2: 레이아웃 기초
            // ============================================================
            StudyModule(
                id = "layout_and_modifier",
                name = "Layout & Modifier",
                description = "Column, Row, Box, Modifier 체이닝",
                detailDescription = "Row, Column, Box 레이아웃과 Modifier 체인을 학습합니다.",
                level = 2,
                category = Category.LAYOUT,
                prerequisites = listOf("composable_function"),
                activityClass = com.example.layout_and_modifier.MainActivity::class.java
            ),
            StudyModule(
                id = "basic_ui_components",
                name = "Basic UI Components",
                description = "Text, Button, TextField, Image 기초",
                detailDescription = "Text, Button, TextField, Image 등 기본 UI 컴포넌트의 사용법을 학습합니다.",
                level = 2,
                category = Category.LAYOUT,
                prerequisites = listOf("layout_and_modifier"),
                activityClass = com.example.basic_ui_components.MainActivity::class.java
            ),
            StudyModule(
                id = "text_typography",
                name = "Text & Typography",
                description = "텍스트 스타일링, AnnotatedString",
                detailDescription = "Text 컴포넌트의 다양한 스타일링 옵션과 Typography 시스템을 학습합니다.",
                level = 2,
                category = Category.LAYOUT,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.text_typography.MainActivity::class.java
            ),
            StudyModule(
                id = "rich_text",
                name = "Rich Text",
                description = "AnnotatedString 복합 스타일, 클릭 가능 텍스트",
                detailDescription = "buildAnnotatedString, SpanStyle, ClickableText 등으로 복잡한 텍스트 스타일링을 구현합니다.",
                level = 2,
                category = Category.LAYOUT,
                prerequisites = listOf("text_typography"),
                activityClass = com.example.rich_text.MainActivity::class.java
            ),
            StudyModule(
                id = "auto_sizing_text",
                name = "Auto Sizing Text",
                description = "컨테이너에 맞는 텍스트 크기 자동 조절",
                detailDescription = "텍스트가 컨테이너에 맞게 자동으로 크기를 조절하는 방법을 학습합니다.",
                level = 2,
                category = Category.LAYOUT,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.auto_sizing_text.MainActivity::class.java
            ),
            StudyModule(
                id = "flow_layout",
                name = "Flow Layout",
                description = "FlowRow, FlowColumn 동적 래핑",
                detailDescription = "자동으로 줄바꿈되는 FlowRow와 FlowColumn 레이아웃을 학습합니다.",
                level = 2,
                category = Category.LAYOUT,
                prerequisites = listOf("layout_and_modifier"),
                activityClass = com.example.flow_layout.MainActivity::class.java
            ),

            // ============================================================
            // Level 3: 상태 기초
            // ============================================================
            StudyModule(
                id = "remember",
                name = "remember",
                description = "remember, mutableStateOf로 상태 저장",
                detailDescription = "remember를 사용하여 Recomposition 사이에 상태를 유지하는 방법을 학습합니다.",
                level = 3,
                category = Category.STATE,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.remember.MainActivity::class.java
            ),
            StudyModule(
                id = "recomposition",
                name = "Recomposition",
                description = "Recomposition 동작 이해",
                detailDescription = "상태가 변경될 때 Compose가 UI를 다시 그리는 Recomposition 과정을 이해합니다.",
                level = 3,
                category = Category.STATE,
                prerequisites = listOf("remember"),
                activityClass = com.example.recomposition.MainActivity::class.java
            ),
            StudyModule(
                id = "remember_saveable",
                name = "rememberSaveable",
                description = "화면 회전에도 상태 유지",
                detailDescription = "화면 회전 등 구성 변경 시에도 상태를 유지하는 rememberSaveable을 학습합니다.",
                level = 3,
                category = Category.STATE,
                prerequisites = listOf("recomposition"),
                activityClass = com.example.remember_saveable.MainActivity::class.java
            ),

            // ============================================================
            // Level 4: UI 컴포넌트
            // ============================================================
            // 4-A: 버튼 & 액션
            StudyModule(
                id = "button",
                name = "Button",
                description = "Button, IconButton, FloatingActionButton",
                detailDescription = "Button, TextButton, OutlinedButton, IconButton, FAB 등 다양한 버튼을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.button.MainActivity::class.java
            ),
            StudyModule(
                id = "menu",
                name = "Menu",
                description = "DropdownMenu, 컨텍스트 메뉴",
                detailDescription = "DropdownMenu와 메뉴 아이템 구성을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.menu.MainActivity::class.java
            ),
            // 4-B: 선택
            StudyModule(
                id = "checkbox",
                name = "Checkbox",
                description = "다중 선택 체크박스",
                detailDescription = "Checkbox와 TriStateCheckbox 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.checkbox.MainActivity::class.java
            ),
            StudyModule(
                id = "radio_button",
                name = "RadioButton",
                description = "단일 선택 라디오 버튼",
                detailDescription = "RadioButton으로 단일 선택 옵션을 구현하는 방법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.radio_button.MainActivity::class.java
            ),
            StudyModule(
                id = "switch_component",
                name = "Switch",
                description = "온/오프 스위치",
                detailDescription = "Switch 컴포넌트로 on/off 토글을 구현합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.switch_component.MainActivity::class.java
            ),
            StudyModule(
                id = "chip",
                name = "Chip",
                description = "필터/선택 칩",
                detailDescription = "InputChip, FilterChip, SuggestionChip 등 다양한 칩을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.chip.MainActivity::class.java
            ),
            StudyModule(
                id = "segmented_button",
                name = "Segmented Button",
                description = "세그먼트 버튼 그룹",
                detailDescription = "SingleChoiceSegmentedButtonRow와 MultiChoiceSegmentedButtonRow 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.segmented_button.MainActivity::class.java
            ),
            // 4-C: 입력
            StudyModule(
                id = "textfield_state",
                name = "TextField State",
                description = "TextField 상태 관리, 유효성 검증",
                detailDescription = "TextField와 TextFieldState를 사용한 텍스트 입력을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.textfield_state.MainActivity::class.java
            ),
            StudyModule(
                id = "secure_textfield",
                name = "Secure TextField",
                description = "비밀번호 입력 필드 (SecureTextField)",
                detailDescription = "비밀번호 등 보안이 필요한 텍스트 입력을 처리하는 방법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("textfield_state"),
                activityClass = com.example.secure_textfield.MainActivity::class.java
            ),
            StudyModule(
                id = "autofill",
                name = "Autofill",
                description = "시스템 자동완성 통합 (로그인, 회원가입)",
                detailDescription = "Android Autofill 프레임워크와 Compose 통합을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("textfield_state"),
                activityClass = com.example.autofill.MainActivity::class.java
            ),
            StudyModule(
                id = "slider",
                name = "Slider",
                description = "범위 값 선택 슬라이더",
                detailDescription = "Slider와 RangeSlider 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.slider.MainActivity::class.java
            ),
            StudyModule(
                id = "date_picker",
                name = "DatePicker",
                description = "날짜 선택기",
                detailDescription = "DatePicker와 DateRangePicker 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.date_picker.MainActivity::class.java
            ),
            StudyModule(
                id = "time_picker",
                name = "TimePicker",
                description = "시간 선택기",
                detailDescription = "TimePicker 컴포넌트 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.time_picker.MainActivity::class.java
            ),
            // 4-D: 표시 & 피드백
            StudyModule(
                id = "card",
                name = "Card",
                description = "Card로 콘텐츠 그룹화",
                detailDescription = "Card, ElevatedCard, OutlinedCard 등 카드 컴포넌트를 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("button"),
                activityClass = com.example.card.MainActivity::class.java
            ),
            StudyModule(
                id = "divider",
                name = "Divider",
                description = "구분선",
                detailDescription = "HorizontalDivider와 VerticalDivider 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.divider.MainActivity::class.java
            ),
            StudyModule(
                id = "badge",
                name = "Badge",
                description = "알림 배지",
                detailDescription = "알림 개수 등을 표시하는 Badge 컴포넌트를 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.badge.MainActivity::class.java
            ),
            StudyModule(
                id = "tooltip",
                name = "Tooltip",
                description = "도움말 툴팁",
                detailDescription = "PlainTooltip과 RichTooltip 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.tooltip.MainActivity::class.java
            ),
            StudyModule(
                id = "progress_indicator",
                name = "Progress Indicator",
                description = "로딩 인디케이터",
                detailDescription = "LinearProgressIndicator와 CircularProgressIndicator 사용법을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.progress_indicator.MainActivity::class.java
            ),
            StudyModule(
                id = "dialog",
                name = "Dialog",
                description = "AlertDialog, 커스텀 Dialog",
                detailDescription = "AlertDialog와 커스텀 Dialog 구현을 학습합니다.",
                level = 4,
                category = Category.COMPONENT,
                prerequisites = listOf("remember"),
                activityClass = com.example.dialog.MainActivity::class.java
            ),

            // ============================================================
            // Level 5: 리스트 & 검색
            // ============================================================
            // 5-A: 리스트
            StudyModule(
                id = "lazy_list",
                name = "LazyColumn & LazyRow",
                description = "LazyColumn, LazyRow 기초",
                detailDescription = "대량의 아이템을 효율적으로 표시하는 LazyColumn과 LazyRow를 학습합니다.",
                level = 5,
                category = Category.LIST,
                prerequisites = listOf("remember"),
                activityClass = com.example.lazy_list.MainActivity::class.java
            ),
            StudyModule(
                id = "lazy_grid",
                name = "LazyGrid",
                description = "LazyVerticalGrid, LazyHorizontalGrid, StaggeredGrid",
                detailDescription = "LazyVerticalGrid와 LazyHorizontalGrid 사용법을 학습합니다.",
                level = 5,
                category = Category.LIST,
                prerequisites = listOf("lazy_list"),
                activityClass = com.example.lazy_grid.MainActivity::class.java
            ),
            StudyModule(
                id = "lazy_layouts",
                name = "Lazy Layouts 심화",
                description = "key, contentType, 성능 최적화",
                detailDescription = "LazyListState, 스크롤 위치 복원 등 Lazy Layout 심화 기법을 학습합니다.",
                level = 5,
                category = Category.LIST,
                prerequisites = listOf("lazy_list"),
                activityClass = com.example.lazy_layouts.MainActivity::class.java
            ),
            StudyModule(
                id = "pull_to_refresh",
                name = "Pull to Refresh",
                description = "당겨서 새로고침",
                detailDescription = "PullToRefreshBox를 사용한 새로고침 기능을 학습합니다.",
                level = 5,
                category = Category.LIST,
                prerequisites = listOf("lazy_list"),
                activityClass = com.example.pull_to_refresh.MainActivity::class.java
            ),
            StudyModule(
                id = "paging_compose",
                name = "Paging 3 with Compose",
                description = "Paging 3 무한 스크롤",
                detailDescription = "Paging 3 라이브러리와 Compose를 통합하여 대용량 데이터를 처리합니다.",
                level = 5,
                category = Category.LIST,
                prerequisites = listOf("lazy_layouts"),
                activityClass = com.example.paging_compose.MainActivity::class.java
            ),
            // 5-B: 스와이프 & 갤러리
            StudyModule(
                id = "pager",
                name = "Pager",
                description = "HorizontalPager, VerticalPager",
                detailDescription = "HorizontalPager와 VerticalPager 사용법을 학습합니다.",
                level = 5,
                category = Category.LIST,
                prerequisites = listOf("lazy_list"),
                activityClass = com.example.pager.MainActivity::class.java
            ),
            StudyModule(
                id = "carousel",
                name = "Carousel",
                description = "가로 스와이프 갤러리",
                detailDescription = "이미지 갤러리 등에 사용되는 Carousel 구현을 학습합니다.",
                level = 5,
                category = Category.LIST,
                prerequisites = listOf("pager"),
                activityClass = com.example.carousel.MainActivity::class.java
            ),
            // 5-C: 검색
            StudyModule(
                id = "search_bar",
                name = "SearchBar",
                description = "SearchBar 기초",
                detailDescription = "Material3 SearchBar 컴포넌트 사용법을 학습합니다.",
                level = 5,
                category = Category.SEARCH,
                prerequisites = listOf("textfield_state"),
                activityClass = com.example.search_bar.MainActivity::class.java
            ),
            StudyModule(
                id = "search_bar_advanced",
                name = "SearchBar 심화",
                description = "검색 자동완성, 디바운스",
                detailDescription = "검색 히스토리, 자동 완성 등 SearchBar 고급 기능을 학습합니다.",
                level = 5,
                category = Category.SEARCH,
                prerequisites = listOf("search_bar"),
                activityClass = com.example.search_bar_advanced.MainActivity::class.java
            ),

            // ============================================================
            // Level 6: 앱 구조
            // ============================================================
            StudyModule(
                id = "material_symbols",
                name = "Material Symbols",
                description = "Material Symbols 아이콘 시스템",
                detailDescription = "Material Symbols 아이콘 사용법을 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("composable_function"),
                activityClass = com.example.material_symbols.MainActivity::class.java
            ),
            StudyModule(
                id = "scaffold",
                name = "Scaffold",
                description = "Scaffold 기본 구조",
                detailDescription = "Scaffold를 사용하여 앱의 기본 레이아웃 구조를 잡는 방법을 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("layout_and_modifier"),
                activityClass = com.example.scaffold.MainActivity::class.java
            ),
            StudyModule(
                id = "window_insets",
                name = "Window Insets",
                description = "시스템 바, Edge-to-Edge",
                detailDescription = "Status Bar, Navigation Bar 영역을 처리하는 Window Insets를 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("scaffold"),
                activityClass = com.example.window_insets.MainActivity::class.java
            ),
            StudyModule(
                id = "app_bar",
                name = "App Bar",
                description = "TopAppBar, BottomAppBar",
                detailDescription = "TopAppBar, CenterAlignedTopAppBar 등 앱 바 컴포넌트를 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("window_insets"),
                activityClass = com.example.app_bar.MainActivity::class.java
            ),
            StudyModule(
                id = "navigation_bar",
                name = "Navigation Bar",
                description = "하단 네비게이션 바",
                detailDescription = "NavigationBar 컴포넌트로 하단 탭 네비게이션을 구현합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("scaffold"),
                activityClass = com.example.navigation_bar.MainActivity::class.java
            ),
            StudyModule(
                id = "tabs",
                name = "Tabs",
                description = "TabRow, 탭 전환",
                detailDescription = "TabRow와 Tab으로 탭 네비게이션을 구현합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("scaffold"),
                activityClass = com.example.tabs.MainActivity::class.java
            ),
            StudyModule(
                id = "scaffold_and_theming",
                name = "Scaffold & Theming",
                description = "MaterialTheme, 다크모드",
                detailDescription = "Scaffold와 Material Theme을 함께 활용하는 방법을 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("scaffold"),
                activityClass = com.example.scaffold_and_theming.MainActivity::class.java
            ),
            StudyModule(
                id = "snackbar",
                name = "Snackbar",
                description = "Snackbar 메시지",
                detailDescription = "SnackbarHost와 Snackbar로 사용자에게 피드백을 제공합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("scaffold"),
                activityClass = com.example.snackbar.MainActivity::class.java
            ),
            StudyModule(
                id = "bottom_sheet",
                name = "Bottom Sheet",
                description = "ModalBottomSheet 기초",
                detailDescription = "ModalBottomSheet 사용법을 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("scaffold"),
                activityClass = com.example.bottom_sheet.MainActivity::class.java
            ),
            StudyModule(
                id = "bottom_sheet_advanced",
                name = "Bottom Sheet 심화",
                description = "SheetState, 중첩 시트",
                detailDescription = "BottomSheetScaffold와 고급 바텀 시트 패턴을 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("bottom_sheet"),
                activityClass = com.example.bottom_sheet_advanced.MainActivity::class.java
            ),
            StudyModule(
                id = "adaptive_layout",
                name = "Adaptive Layout",
                description = "태블릿/폴더블 반응형 레이아웃",
                detailDescription = "다양한 화면 크기에 대응하는 적응형 레이아웃을 구현합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("window_insets"),
                activityClass = com.example.adaptive_layout.MainActivity::class.java
            ),
            StudyModule(
                id = "material_expressive",
                name = "Material Expressive",
                description = "Material 3 Expressive, 스프링 물리 모션",
                detailDescription = "Material Design 3 Expressive 스타일을 적용하는 방법을 학습합니다.",
                level = 6,
                category = Category.STRUCTURE,
                prerequisites = listOf("scaffold_and_theming"),
                activityClass = com.example.material_expressive.MainActivity::class.java
            ),

            // ============================================================
            // Level 7: Side Effects
            // ============================================================
            StudyModule(
                id = "side_effect",
                name = "SideEffect",
                description = "Side Effect 개념 이해",
                detailDescription = "Compose의 Side Effect 개념과 SideEffect 함수를 학습합니다.",
                level = 7,
                category = Category.EFFECT,
                prerequisites = listOf("recomposition"),
                activityClass = com.example.side_effect.MainActivity::class.java
            ),
            StudyModule(
                id = "launched_effect",
                name = "LaunchedEffect",
                description = "비동기 작업 실행 (API 호출 등)",
                detailDescription = "LaunchedEffect로 Composable 내에서 코루틴을 실행하는 방법을 학습합니다.",
                level = 7,
                category = Category.EFFECT,
                prerequisites = listOf("side_effect"),
                activityClass = com.example.launched_effect.MainActivity::class.java
            ),
            StudyModule(
                id = "disposable_effect",
                name = "DisposableEffect",
                description = "리소스 정리 (리스너 해제 등)",
                detailDescription = "리소스 해제가 필요한 Side Effect를 DisposableEffect로 처리합니다.",
                level = 7,
                category = Category.EFFECT,
                prerequisites = listOf("launched_effect"),
                activityClass = com.example.disposable_effect.MainActivity::class.java
            ),
            StudyModule(
                id = "remember_coroutine_scope",
                name = "rememberCoroutineScope",
                description = "이벤트 핸들러에서 코루틴 실행",
                detailDescription = "이벤트 핸들러에서 코루틴을 실행하는 rememberCoroutineScope를 학습합니다.",
                level = 7,
                category = Category.EFFECT,
                prerequisites = listOf("launched_effect"),
                activityClass = com.example.remember_coroutine_scope.MainActivity::class.java
            ),
            StudyModule(
                id = "produce_state",
                name = "produceState",
                description = "비동기 데이터를 State로 변환",
                detailDescription = "비동기 데이터 소스를 Compose State로 변환하는 produceState를 학습합니다.",
                level = 7,
                category = Category.EFFECT,
                prerequisites = listOf("launched_effect"),
                activityClass = com.example.produce_state.MainActivity::class.java
            ),
            StudyModule(
                id = "effect_handlers_advanced",
                name = "Effect Handlers 심화",
                description = "snapshotFlow, rememberUpdatedState",
                detailDescription = "snapshotFlow, derivedStateOf 등 고급 Effect 패턴을 학습합니다.",
                level = 7,
                category = Category.EFFECT,
                prerequisites = listOf("disposable_effect"),
                activityClass = com.example.effect_handlers_advanced.MainActivity::class.java
            ),

            // ============================================================
            // Level 8: 상태 심화
            // ============================================================
            StudyModule(
                id = "state_hoisting",
                name = "State Hoisting",
                description = "상태 끌어올리기, Stateless 컴포넌트",
                detailDescription = "상태를 상위 Composable로 끌어올려 Stateless Composable을 만드는 패턴을 학습합니다.",
                level = 8,
                category = Category.STATE,
                prerequisites = listOf("remember_saveable"),
                activityClass = com.example.state_hoisting.MainActivity::class.java
            ),
            StudyModule(
                id = "derived_state_of",
                name = "derivedStateOf",
                description = "파생 상태로 불필요한 Recomposition 방지",
                detailDescription = "다른 상태로부터 파생된 상태를 효율적으로 계산하는 derivedStateOf를 학습합니다.",
                level = 8,
                category = Category.STATE,
                prerequisites = listOf("state_hoisting"),
                activityClass = com.example.derived_state_of.MainActivity::class.java
            ),
            StudyModule(
                id = "snapshot_system",
                name = "Snapshot System",
                description = "snapshotFlow로 State를 Flow로 변환",
                detailDescription = "Compose의 상태 관리 기반인 Snapshot System의 내부 동작을 이해합니다.",
                level = 8,
                category = Category.STATE,
                prerequisites = listOf("derived_state_of"),
                activityClass = com.example.snapshot_system.MainActivity::class.java
            ),
            StudyModule(
                id = "stability",
                name = "Stability",
                description = "@Stable, @Immutable로 성능 최적화",
                detailDescription = "@Stable, @Immutable 어노테이션으로 불필요한 Recomposition을 방지하는 방법을 학습합니다.",
                level = 8,
                category = Category.STATE,
                prerequisites = listOf("derived_state_of"),
                activityClass = com.example.stability.MainActivity::class.java
            ),
            StudyModule(
                id = "state_management_advanced",
                name = "State Management 심화",
                description = "StateFlow vs SharedFlow vs Channel",
                detailDescription = "복잡한 앱에서의 상태 관리 전략과 패턴을 학습합니다.",
                level = 8,
                category = Category.STATE,
                prerequisites = listOf("stability"),
                activityClass = com.example.state_management_advanced.MainActivity::class.java
            ),
            StudyModule(
                id = "state_restoration_advanced",
                name = "State Restoration 심화",
                description = "프로세스 종료 후 상태 복원",
                detailDescription = "복잡한 상태의 저장과 복원을 다루는 고급 기법을 학습합니다.",
                level = 8,
                category = Category.STATE,
                prerequisites = listOf("state_management_advanced"),
                activityClass = com.example.state_restoration_advanced.MainActivity::class.java
            ),
            StudyModule(
                id = "retain",
                name = "Retain",
                description = "직렬화 없이 Config Change 상태 유지",
                detailDescription = "앱 프로세스가 종료되어도 상태를 복원하는 방법을 학습합니다.",
                level = 8,
                category = Category.STATE,
                prerequisites = listOf("remember_saveable"),
                activityClass = com.example.retain.MainActivity::class.java
            ),

            // ============================================================
            // Level 9: 네비게이션
            // ============================================================
            StudyModule(
                id = "navigation_basics",
                name = "Navigation 기초",
                description = "Type-Safe Navigation 기초",
                detailDescription = "NavController와 NavHost를 사용한 화면 전환을 학습합니다.",
                level = 9,
                category = Category.NAVIGATION,
                prerequisites = listOf("state_hoisting"),
                activityClass = com.example.navigation.MainActivity::class.java
            ),
            StudyModule(
                id = "navigation_3",
                name = "Navigation 3",
                description = "Navigation 3 (2025 최신)",
                detailDescription = "새로운 Navigation 3 API를 학습합니다.",
                level = 9,
                category = Category.NAVIGATION,
                prerequisites = listOf("navigation_basics"),
                activityClass = com.example.navigation_3.MainActivity::class.java
            ),
            StudyModule(
                id = "deep_link",
                name = "Deep Link",
                description = "딥링크로 특정 화면 열기",
                detailDescription = "Navigation에서 딥링크를 설정하고 처리하는 방법을 학습합니다.",
                level = 9,
                category = Category.NAVIGATION,
                prerequisites = listOf("navigation_basics"),
                activityClass = com.example.deep_link.MainActivity::class.java
            ),
            StudyModule(
                id = "back_handler",
                name = "BackHandler",
                description = "뒤로가기 커스텀 처리",
                detailDescription = "BackHandler로 시스템 뒤로 가기 버튼을 처리합니다.",
                level = 9,
                category = Category.NAVIGATION,
                prerequisites = listOf("navigation_basics"),
                activityClass = com.example.back_handler.MainActivity::class.java
            ),
            StudyModule(
                id = "predictive_back",
                name = "Predictive Back",
                description = "예측 뒤로가기 제스처 (Android 14+)",
                detailDescription = "Android 14+ Predictive Back 제스처를 지원하는 방법을 학습합니다.",
                level = 9,
                category = Category.NAVIGATION,
                prerequisites = listOf("back_handler"),
                activityClass = com.example.predictive_back.MainActivity::class.java
            ),
            StudyModule(
                id = "navigation_drawer",
                name = "Navigation Drawer",
                description = "사이드 드로어 메뉴",
                detailDescription = "ModalNavigationDrawer 사용법을 학습합니다.",
                level = 9,
                category = Category.NAVIGATION,
                prerequisites = listOf("navigation_basics"),
                activityClass = com.example.navigation_drawer.MainActivity::class.java
            ),
            StudyModule(
                id = "navigation_rail",
                name = "Navigation Rail",
                description = "태블릿용 레일 네비게이션",
                detailDescription = "태블릿/데스크톱용 NavigationRail 컴포넌트를 학습합니다.",
                level = 9,
                category = Category.NAVIGATION,
                prerequisites = listOf("navigation_basics"),
                activityClass = com.example.navigation_rail.MainActivity::class.java
            ),

            // ============================================================
            // Level 10: 애니메이션
            // ============================================================
            StudyModule(
                id = "animation_basics",
                name = "Animation 기초",
                description = "animate*AsState, AnimatedVisibility",
                detailDescription = "animate*AsState를 사용한 기본 애니메이션을 학습합니다.",
                level = 10,
                category = Category.ANIMATION,
                prerequisites = listOf("remember"),
                activityClass = com.example.animation.MainActivity::class.java
            ),
            StudyModule(
                id = "animation_advanced",
                name = "Animation 심화",
                description = "updateTransition, Animatable",
                detailDescription = "Animatable, updateTransition, infiniteTransition 등을 학습합니다.",
                level = 10,
                category = Category.ANIMATION,
                prerequisites = listOf("animation_basics"),
                activityClass = com.example.animation_advanced.MainActivity::class.java
            ),
            StudyModule(
                id = "animation_physics",
                name = "Physics Animation",
                description = "spring/decay 물리 기반 애니메이션",
                detailDescription = "spring, decay 등 물리 기반 애니메이션을 학습합니다.",
                level = 10,
                category = Category.ANIMATION,
                prerequisites = listOf("animation_advanced"),
                activityClass = com.example.animation_physics.MainActivity::class.java
            ),
            StudyModule(
                id = "animate_bounds",
                name = "Animate Bounds",
                description = "LookaheadScope, 레이아웃 애니메이션",
                detailDescription = "animateBounds를 사용한 레이아웃 전환 애니메이션을 학습합니다.",
                level = 10,
                category = Category.ANIMATION,
                prerequisites = listOf("animation_advanced"),
                activityClass = com.example.animate_bounds.MainActivity::class.java
            ),
            StudyModule(
                id = "shared_element_transition",
                name = "Shared Element Transition",
                description = "화면 전환 시 공유 요소 애니메이션",
                detailDescription = "화면 간 공유 요소 전환 애니메이션을 구현합니다.",
                level = 10,
                category = Category.ANIMATION,
                prerequisites = listOf("animation_advanced", "navigation_basics"),
                activityClass = com.example.shared_element_transition.MainActivity::class.java
            ),

            // ============================================================
            // Level 11: 커스텀 레이아웃
            // ============================================================
            StudyModule(
                id = "intrinsic_measurements",
                name = "Intrinsic Measurements",
                description = "IntrinsicSize로 자식 크기 동기화",
                detailDescription = "IntrinsicSize를 사용하여 자식의 크기를 미리 측정하는 방법을 학습합니다.",
                level = 11,
                category = Category.LAYOUT,
                prerequisites = listOf("layout_and_modifier"),
                activityClass = com.example.intrinsic_measurements.MainActivity::class.java
            ),
            StudyModule(
                id = "constraint_layout",
                name = "ConstraintLayout",
                description = "제약 조건 기반 복잡한 레이아웃",
                detailDescription = "복잡한 레이아웃을 구성하는 ConstraintLayout의 Compose 버전을 학습합니다.",
                level = 11,
                category = Category.LAYOUT,
                prerequisites = listOf("layout_and_modifier"),
                activityClass = com.example.constraint_layout.MainActivity::class.java
            ),
            StudyModule(
                id = "custom_layout",
                name = "Custom Layout",
                description = "Layout() 커스텀 레이아웃 작성",
                detailDescription = "Layout Composable을 사용하여 완전히 새로운 레이아웃을 만드는 방법을 학습합니다.",
                level = 11,
                category = Category.LAYOUT,
                prerequisites = listOf("constraint_layout"),
                activityClass = com.example.custom_layout.MainActivity::class.java
            ),
            StudyModule(
                id = "custom_modifier",
                name = "Custom Modifier",
                description = "Modifier.Node로 커스텀 Modifier 생성",
                detailDescription = "재사용 가능한 커스텀 Modifier를 만드는 방법을 학습합니다.",
                level = 11,
                category = Category.LAYOUT,
                prerequisites = listOf("custom_layout"),
                activityClass = com.example.custom_modifier.MainActivity::class.java
            ),
            StudyModule(
                id = "canvas_drawing",
                name = "Canvas Drawing",
                description = "Canvas로 커스텀 그래픽",
                detailDescription = "Canvas를 사용하여 커스텀 그래픽을 그리는 방법을 학습합니다.",
                level = 11,
                category = Category.LAYOUT,
                prerequisites = listOf("layout_and_modifier"),
                activityClass = com.example.canvas_drawing.MainActivity::class.java
            ),

            // ============================================================
            // Level 12: 아키텍처
            // ============================================================
            StudyModule(
                id = "view_model",
                name = "ViewModel",
                description = "ViewModel + Compose 통합",
                detailDescription = "ViewModel을 Compose에서 사용하는 방법을 학습합니다.",
                level = 12,
                category = Category.ARCHITECTURE,
                prerequisites = listOf("state_hoisting"),
                activityClass = com.example.view_model.MainActivity::class.java
            ),
            // Note: hilt_viewmodel은 Application 모듈이므로 제외
            StudyModule(
                id = "screen_and_component",
                name = "Screen & Component",
                description = "Screen vs Component 분리 패턴",
                detailDescription = "Screen과 Component의 역할을 분리하는 패턴을 학습합니다.",
                level = 12,
                category = Category.ARCHITECTURE,
                prerequisites = listOf("state_hoisting"),
                activityClass = com.example.screen_and_component.MainActivity::class.java
            ),
            StudyModule(
                id = "slot_api_pattern",
                name = "Slot API Pattern",
                description = "Slot API로 유연한 컴포넌트 설계",
                detailDescription = "Composable 람다를 사용한 Slot API 패턴을 학습합니다.",
                level = 12,
                category = Category.ARCHITECTURE,
                prerequisites = listOf("screen_and_component"),
                activityClass = com.example.slot_api_pattern.MainActivity::class.java
            ),
            StudyModule(
                id = "composition_local",
                name = "CompositionLocal",
                description = "암시적 데이터 전달",
                detailDescription = "CompositionLocal로 트리 전체에 데이터를 전달하는 방법을 학습합니다.",
                level = 12,
                category = Category.ARCHITECTURE,
                prerequisites = listOf("slot_api_pattern"),
                activityClass = com.example.composition_local.MainActivity::class.java
            ),
            StudyModule(
                id = "unidirectional_data_flow",
                name = "Unidirectional Data Flow",
                description = "단방향 데이터 흐름 (UDF), MVI 패턴",
                detailDescription = "UDF 패턴으로 상태를 관리하는 아키텍처를 학습합니다.",
                level = 12,
                category = Category.ARCHITECTURE,
                prerequisites = listOf("view_model"),
                activityClass = com.example.unidirectional_data_flow.MainActivity::class.java
            ),

            // ============================================================
            // Level 13: 제스처 & 인터랙션
            // ============================================================
            StudyModule(
                id = "gesture",
                name = "Gesture 기초",
                description = "탭, 드래그, 스와이프 제스처",
                detailDescription = "clickable, pointerInput 등 기본 제스처 처리를 학습합니다.",
                level = 13,
                category = Category.INTERACTION,
                prerequisites = listOf("remember"),
                activityClass = com.example.gesture.MainActivity::class.java
            ),
            StudyModule(
                id = "gesture_advanced",
                name = "Gesture 심화",
                description = "pointerInput 고급 제스처, 멀티터치",
                detailDescription = "멀티터치, 드래그, 스와이프 등 복잡한 제스처를 학습합니다.",
                level = 13,
                category = Category.INTERACTION,
                prerequisites = listOf("gesture"),
                activityClass = com.example.gesture_advanced.MainActivity::class.java
            ),
            StudyModule(
                id = "focus_management",
                name = "Focus Management",
                description = "포커스 순서, 키보드 처리",
                detailDescription = "FocusRequester, FocusManager를 사용한 포커스 제어를 학습합니다.",
                level = 13,
                category = Category.INTERACTION,
                prerequisites = listOf("gesture"),
                activityClass = com.example.focus_management.MainActivity::class.java
            ),
            StudyModule(
                id = "drag_and_drop",
                name = "Drag & Drop",
                description = "드래그 앤 드롭 기능",
                detailDescription = "Compose에서 드래그 앤 드롭을 구현하는 방법을 학습합니다.",
                level = 13,
                category = Category.INTERACTION,
                prerequisites = listOf("gesture"),
                activityClass = com.example.drag_and_drop.MainActivity::class.java
            ),
            StudyModule(
                id = "haptic_feedback",
                name = "Haptic Feedback",
                description = "진동/햅틱 피드백",
                detailDescription = "터치 시 진동 피드백을 제공하는 방법을 학습합니다.",
                level = 13,
                category = Category.INTERACTION,
                prerequisites = listOf("gesture"),
                activityClass = com.example.haptic_feedback.MainActivity::class.java
            ),

            // ============================================================
            // Level 14: 외부 통합
            // ============================================================
            StudyModule(
                id = "lifecycle_integration",
                name = "Lifecycle Integration",
                description = "LifecycleOwner 연동",
                detailDescription = "Compose에서 Android 생명주기를 처리하는 방법을 학습합니다.",
                level = 14,
                category = Category.INTEGRATION,
                prerequisites = listOf("disposable_effect"),
                activityClass = com.example.lifecycle_integration.MainActivity::class.java
            ),
            StudyModule(
                id = "image_loading",
                name = "Image Loading",
                description = "Coil로 이미지 로딩",
                detailDescription = "Coil 등 이미지 로딩 라이브러리와 Compose 통합을 학습합니다.",
                level = 14,
                category = Category.INTEGRATION,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.image_loading.MainActivity::class.java
            ),
            StudyModule(
                id = "media3_player",
                name = "Media3 Player",
                description = "ExoPlayer + Compose",
                detailDescription = "Media3 ExoPlayer를 Compose에서 사용하는 방법을 학습합니다.",
                level = 14,
                category = Category.INTEGRATION,
                prerequisites = listOf("lifecycle_integration"),
                activityClass = com.example.media3_player.MainActivity::class.java
            ),
            StudyModule(
                id = "interoperability",
                name = "View Interoperability",
                description = "View ↔ Compose 상호운용",
                detailDescription = "AndroidView, ComposeView를 통한 View-Compose 상호 운용을 학습합니다.",
                level = 14,
                category = Category.INTEGRATION,
                prerequisites = listOf("lifecycle_integration"),
                activityClass = com.example.interoperability.MainActivity::class.java
            ),
            StudyModule(
                id = "camerax_compose",
                name = "CameraX with Compose",
                description = "CameraX + Compose",
                detailDescription = "CameraX 라이브러리를 Compose에서 사용하는 방법을 학습합니다.",
                level = 14,
                category = Category.INTEGRATION,
                prerequisites = listOf("interoperability"),
                activityClass = com.example.camerax_compose.MainActivity::class.java
            ),

            // ============================================================
            // Level 15: 시스템 통합
            // ============================================================
            StudyModule(
                id = "permission_handling",
                name = "Permission Handling",
                description = "런타임 권한 요청",
                detailDescription = "Accompanist 또는 직접 권한 요청을 처리하는 방법을 학습합니다.",
                level = 15,
                category = Category.SYSTEM,
                prerequisites = listOf("launched_effect"),
                activityClass = com.example.permission_handling.MainActivity::class.java
            ),
            StudyModule(
                id = "notification_integration",
                name = "Notification Integration",
                description = "알림 생성 및 처리",
                detailDescription = "Compose 앱에서 알림을 생성하고 처리하는 방법을 학습합니다.",
                level = 15,
                category = Category.SYSTEM,
                prerequisites = listOf("permission_handling"),
                activityClass = com.example.notification_integration.MainActivity::class.java
            ),
            StudyModule(
                id = "audio_recording",
                name = "Audio Recording",
                description = "오디오 녹음",
                detailDescription = "Compose 앱에서 오디오를 녹음하는 방법을 학습합니다.",
                level = 15,
                category = Category.SYSTEM,
                prerequisites = listOf("permission_handling"),
                activityClass = com.example.audio_recording.MainActivity::class.java
            ),

            // ============================================================
            // Level 16: 테스트 & 성능
            // ============================================================
            StudyModule(
                id = "semantics_accessibility",
                name = "Semantics & Accessibility",
                description = "접근성, 스크린 리더 지원",
                detailDescription = "Compose의 Semantics 시스템과 접근성 지원을 학습합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("basic_ui_components"),
                activityClass = com.example.semantics_accessibility.MainActivity::class.java
            ),
            StudyModule(
                id = "compose_testing",
                name = "Compose Testing",
                description = "UI 테스트, Semantics",
                detailDescription = "Compose UI 테스트 기본을 학습합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("semantics_accessibility"),
                activityClass = com.example.compose_testing.MainActivity::class.java
            ),
            StudyModule(
                id = "compose_preview_testing",
                name = "Preview Testing",
                description = "@PreviewParameter로 다중 Preview 테스트",
                detailDescription = "@Preview를 활용한 테스트 방법을 학습합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("compose_testing"),
                activityClass = com.example.compose_preview_testing.MainActivity::class.java
            ),
            StudyModule(
                id = "screenshot_testing",
                name = "Screenshot Testing",
                description = "스크린샷 테스트, Paparazzi",
                detailDescription = "Compose 스크린샷 테스트를 구현합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("compose_testing"),
                activityClass = com.example.screenshot_testing.MainActivity::class.java
            ),
            StudyModule(
                id = "compose_compiler_metrics",
                name = "Compiler Metrics",
                description = "컴파일러 리포트 분석",
                detailDescription = "Compose 컴파일러 메트릭스를 분석하여 성능을 최적화합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("stability"),
                activityClass = com.example.compose_compiler_metrics.MainActivity::class.java
            ),
            StudyModule(
                id = "baseline_profiles",
                name = "Baseline Profiles",
                description = "앱 시작 성능 최적화",
                detailDescription = "Baseline Profile을 생성하여 앱 시작 성능을 개선합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("compose_compiler_metrics"),
                activityClass = com.example.baseline_profiles.MainActivity::class.java
            ),
            StudyModule(
                id = "pausable_composition",
                name = "Pausable Composition",
                description = "Jank 방지, Composition 일시중단",
                detailDescription = "Pausable Composition의 개념과 활용을 학습합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("baseline_profiles"),
                activityClass = com.example.pausable_composition.MainActivity::class.java
            ),
            StudyModule(
                id = "visibility_tracking",
                name = "Visibility Tracking",
                description = "화면 노출 추적 (광고, 분석)",
                detailDescription = "Composable의 화면 가시성을 추적하는 방법을 학습합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("lazy_layouts"),
                activityClass = com.example.visibility_tracking.MainActivity::class.java
            ),
            StudyModule(
                id = "strong_skipping_mode",
                name = "Strong Skipping Mode",
                description = "Strong Skipping Mode 성능 최적화",
                detailDescription = "Compose 1.5+ Strong Skipping Mode의 동작을 이해합니다.",
                level = 16,
                category = Category.TESTING,
                prerequisites = listOf("stability"),
                activityClass = com.example.strong_skipping_mode.MainActivity::class.java
            ),

            // ============================================================
            // Level 17: 멀티플랫폼
            // ============================================================
            StudyModule(
                id = "compose_multiplatform_intro",
                name = "Compose Multiplatform 소개",
                description = "Compose Multiplatform 입문, expect/actual 패턴",
                detailDescription = "Compose Multiplatform의 개념과 시작 방법을 학습합니다.",
                level = 17,
                category = Category.MULTIPLATFORM,
                prerequisites = listOf("composable_function"),
                activityClass = com.example.compose_multiplatform_intro.MainActivity::class.java
            ),
            StudyModule(
                id = "desktop_extensions",
                name = "Desktop Extensions",
                description = "Desktop 전용 API (MenuBar, Tray, KeyShortcut)",
                detailDescription = "데스크톱 전용 기능과 API를 학습합니다.",
                level = 17,
                category = Category.MULTIPLATFORM,
                prerequisites = listOf("compose_multiplatform_intro"),
                activityClass = com.example.desktop_extensions.MainActivity::class.java
            ),
            StudyModule(
                id = "web_wasm",
                name = "Web with WASM",
                description = "Kotlin/Wasm으로 웹에서 Compose 실행",
                detailDescription = "WebAssembly를 사용한 웹 타겟 배포를 학습합니다.",
                level = 17,
                category = Category.MULTIPLATFORM,
                prerequisites = listOf("compose_multiplatform_intro"),
                activityClass = com.example.web_wasm.MainActivity::class.java
            )
        )
    }

    /**
     * Level별 모듈 조회
     */
    fun getByLevel(level: Int): List<StudyModule> {
        return modules.filter { it.level == level }
    }

    /**
     * Category별 모듈 조회
     */
    fun getByCategory(category: Category): List<StudyModule> {
        return modules.filter { it.category == category }
    }

    /**
     * 검색 쿼리로 모듈 검색
     */
    fun search(query: String): List<StudyModule> {
        if (query.isBlank()) return modules
        return modules.filter { it.matchesQuery(query) }
    }

    /**
     * ID로 모듈 조회
     */
    fun getById(id: String): StudyModule? {
        return modules.find { it.id == id }
    }

    /**
     * 선행 모듈 조회
     */
    fun getPrerequisites(module: StudyModule): List<StudyModule> {
        return module.prerequisites.mapNotNull { getById(it) }
    }

    /**
     * 등록된 모듈 수
     */
    val moduleCount: Int
        get() = modules.size

    /**
     * Level별 모듈 수 맵
     */
    val moduleCountByLevel: Map<Int, Int>
        get() = modules.groupBy { it.level }.mapValues { it.value.size }

    /**
     * Category별 모듈 수 맵
     */
    val moduleCountByCategory: Map<Category, Int>
        get() = modules.groupBy { it.category }.mapValues { it.value.size }
}
