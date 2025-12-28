pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "compose-study"
include(":app")

// ========================================
// basics - Kotlin, Compose 입문
// ========================================
include(":study:basics:kotlin")
include(":study:basics:compose_introduction")
include(":study:basics:composable_function")
include(":study:basics:preview")

// ========================================
// layout - 레이아웃 & Modifier
// ========================================
include(":study:layout:layout_and_modifier")
include(":study:layout:basic_ui_components")
include(":study:layout:text_typography")
include(":study:layout:flow_layout")
include(":study:layout:constraint_layout")
include(":study:layout:custom_layout")
include(":study:layout:custom_modifier")
include(":study:layout:canvas_drawing")
include(":study:layout:auto_sizing_text")
include(":study:layout:intrinsic_measurements")
include(":study:layout:rich_text")

// ========================================
// state - 상태 관리
// ========================================
include(":study:state:remember")
include(":study:state:recomposition")
include(":study:state:remember_saveable")
include(":study:state:state_hoisting")
include(":study:state:derived_state_of")
include(":study:state:stability")
include(":study:state:state_management_advanced")
include(":study:state:state_restoration_advanced")
include(":study:state:retain")
include(":study:state:snapshot_system")

// ========================================
// component - UI 컴포넌트
// ========================================
// action
include(":study:component:action:button")
include(":study:component:action:menu")
// selection
include(":study:component:selection:checkbox")
include(":study:component:selection:radio_button")
include(":study:component:selection:switch_component")
include(":study:component:selection:chip")
include(":study:component:selection:segmented_button")
// input
include(":study:component:input:textfield_state")
include(":study:component:input:secure_textfield")
include(":study:component:input:slider")
include(":study:component:input:date_picker")
include(":study:component:input:time_picker")
include(":study:component:input:autofill")
// display
include(":study:component:display:card")
include(":study:component:display:divider")
include(":study:component:display:badge")
include(":study:component:display:tooltip")
include(":study:component:display:progress_indicator")
include(":study:component:display:dialog")

// ========================================
// list - 리스트 & 스크롤
// ========================================
include(":study:list:lazy_list")
include(":study:list:lazy_grid")
include(":study:list:lazy_layouts")
include(":study:list:pager")
include(":study:list:carousel")
include(":study:list:pull_to_refresh")
include(":study:list:paging_compose")

// ========================================
// search - 검색
// ========================================
include(":study:search:search_bar")
include(":study:search:search_bar_advanced")

// ========================================
// structure - 앱 구조
// ========================================
include(":study:structure:scaffold")
include(":study:structure:window_insets")
include(":study:structure:app_bar")
include(":study:structure:navigation_bar")
include(":study:structure:tabs")
include(":study:structure:scaffold_and_theming")
include(":study:structure:snackbar")
include(":study:structure:bottom_sheet")
include(":study:structure:bottom_sheet_advanced")
include(":study:structure:adaptive_layout")
include(":study:structure:material_expressive")
include(":study:structure:material_symbols")

// ========================================
// effect - Side Effects
// ========================================
include(":study:effect:side_effect")
include(":study:effect:launched_effect")
include(":study:effect:disposable_effect")
include(":study:effect:remember_coroutine_scope")
include(":study:effect:produce_state")
include(":study:effect:effect_handlers_advanced")

// ========================================
// navigation - 네비게이션
// ========================================
include(":study:navigation:navigation_basics")
include(":study:navigation:navigation_3")
include(":study:navigation:deep_link")
include(":study:navigation:back_handler")
include(":study:navigation:predictive_back")
include(":study:navigation:navigation_drawer")
include(":study:navigation:navigation_rail")

// ========================================
// animation - 애니메이션
// ========================================
include(":study:animation:animation_basics")
include(":study:animation:animation_advanced")
include(":study:animation:animation_physics")
include(":study:animation:animate_bounds")
include(":study:animation:shared_element_transition")

// ========================================
// architecture - 아키텍처
// ========================================
include(":study:architecture:view_model")
include(":study:architecture:hilt_viewmodel")
include(":study:architecture:screen_and_component")
include(":study:architecture:slot_api_pattern")
include(":study:architecture:composition_local")
include(":study:architecture:unidirectional_data_flow")

// ========================================
// interaction - 제스처 & 인터랙션
// ========================================
include(":study:interaction:gesture")
include(":study:interaction:gesture_advanced")
include(":study:interaction:focus_management")
include(":study:interaction:drag_and_drop")
include(":study:interaction:haptic_feedback")

// ========================================
// integration - 외부 통합
// ========================================
include(":study:integration:lifecycle_integration")
include(":study:integration:image_loading")
include(":study:integration:media3_player")
include(":study:integration:interoperability")
include(":study:integration:camerax_compose")

// ========================================
// system - 시스템 기능
// ========================================
include(":study:system:permission_handling")
include(":study:system:notification_integration")
include(":study:system:audio_recording")

// ========================================
// testing - 테스트 & 성능
// ========================================
include(":study:testing:compose_testing")
include(":study:testing:screenshot_testing")
include(":study:testing:compose_compiler_metrics")
include(":study:testing:baseline_profiles")
include(":study:testing:semantics_accessibility")
include(":study:testing:visibility_tracking")
include(":study:testing:pausable_composition")
include(":study:testing:strong_skipping_mode")
include(":study:testing:compose_preview_testing")

// ========================================
// multiplatform - 멀티플랫폼
// ========================================
include(":study:multiplatform:compose_multiplatform_intro")
include(":study:multiplatform:desktop_extensions")
include(":study:multiplatform:web_wasm")
