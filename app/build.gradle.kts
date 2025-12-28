plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.compose_study"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.compose_study"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // ========================================
    // Study Modules - basics
    // ========================================
    implementation(project(":study:basics:kotlin"))
    implementation(project(":study:basics:compose_introduction"))
    implementation(project(":study:basics:composable_function"))
    implementation(project(":study:basics:preview"))

    // ========================================
    // Study Modules - layout
    // ========================================
    implementation(project(":study:layout:layout_and_modifier"))
    implementation(project(":study:layout:basic_ui_components"))
    implementation(project(":study:layout:text_typography"))
    implementation(project(":study:layout:flow_layout"))
    implementation(project(":study:layout:constraint_layout"))
    implementation(project(":study:layout:custom_layout"))
    implementation(project(":study:layout:custom_modifier"))
    implementation(project(":study:layout:canvas_drawing"))
    implementation(project(":study:layout:auto_sizing_text"))
    implementation(project(":study:layout:intrinsic_measurements"))
    implementation(project(":study:layout:rich_text"))

    // ========================================
    // Study Modules - state
    // ========================================
    implementation(project(":study:state:remember"))
    implementation(project(":study:state:recomposition"))
    implementation(project(":study:state:remember_saveable"))
    implementation(project(":study:state:state_hoisting"))
    implementation(project(":study:state:derived_state_of"))
    implementation(project(":study:state:stability"))
    implementation(project(":study:state:state_management_advanced"))
    implementation(project(":study:state:state_restoration_advanced"))
    implementation(project(":study:state:retain"))
    implementation(project(":study:state:snapshot_system"))

    // ========================================
    // Study Modules - component/action
    // ========================================
    implementation(project(":study:component:action:button"))
    implementation(project(":study:component:action:menu"))

    // ========================================
    // Study Modules - component/selection
    // ========================================
    implementation(project(":study:component:selection:checkbox"))
    implementation(project(":study:component:selection:radio_button"))
    implementation(project(":study:component:selection:switch_component"))
    implementation(project(":study:component:selection:chip"))
    implementation(project(":study:component:selection:segmented_button"))

    // ========================================
    // Study Modules - component/input
    // ========================================
    implementation(project(":study:component:input:textfield_state"))
    implementation(project(":study:component:input:secure_textfield"))
    implementation(project(":study:component:input:slider"))
    implementation(project(":study:component:input:date_picker"))
    implementation(project(":study:component:input:time_picker"))
    implementation(project(":study:component:input:autofill"))

    // ========================================
    // Study Modules - component/display
    // ========================================
    implementation(project(":study:component:display:card"))
    implementation(project(":study:component:display:divider"))
    implementation(project(":study:component:display:badge"))
    implementation(project(":study:component:display:tooltip"))
    implementation(project(":study:component:display:progress_indicator"))
    implementation(project(":study:component:display:dialog"))

    // ========================================
    // Study Modules - list
    // ========================================
    implementation(project(":study:list:lazy_list"))
    implementation(project(":study:list:lazy_grid"))
    implementation(project(":study:list:lazy_layouts"))
    implementation(project(":study:list:pager"))
    implementation(project(":study:list:carousel"))
    implementation(project(":study:list:pull_to_refresh"))
    implementation(project(":study:list:paging_compose"))

    // ========================================
    // Study Modules - search
    // ========================================
    implementation(project(":study:search:search_bar"))
    implementation(project(":study:search:search_bar_advanced"))

    // ========================================
    // Study Modules - structure
    // ========================================
    implementation(project(":study:structure:scaffold"))
    implementation(project(":study:structure:window_insets"))
    implementation(project(":study:structure:app_bar"))
    implementation(project(":study:structure:navigation_bar"))
    implementation(project(":study:structure:tabs"))
    implementation(project(":study:structure:scaffold_and_theming"))
    implementation(project(":study:structure:snackbar"))
    implementation(project(":study:structure:bottom_sheet"))
    implementation(project(":study:structure:bottom_sheet_advanced"))
    implementation(project(":study:structure:adaptive_layout"))
    implementation(project(":study:structure:material_expressive"))
    implementation(project(":study:structure:material_symbols"))

    // ========================================
    // Study Modules - effect
    // ========================================
    implementation(project(":study:effect:side_effect"))
    implementation(project(":study:effect:launched_effect"))
    implementation(project(":study:effect:disposable_effect"))
    implementation(project(":study:effect:remember_coroutine_scope"))
    implementation(project(":study:effect:produce_state"))
    implementation(project(":study:effect:effect_handlers_advanced"))

    // ========================================
    // Study Modules - navigation
    // ========================================
    implementation(project(":study:navigation:navigation_basics"))
    implementation(project(":study:navigation:navigation_3"))
    implementation(project(":study:navigation:deep_link"))
    implementation(project(":study:navigation:back_handler"))
    implementation(project(":study:navigation:predictive_back"))
    implementation(project(":study:navigation:navigation_drawer"))
    implementation(project(":study:navigation:navigation_rail"))

    // ========================================
    // Study Modules - animation
    // ========================================
    implementation(project(":study:animation:animation_basics"))
    implementation(project(":study:animation:animation_advanced"))
    implementation(project(":study:animation:animation_physics"))
    implementation(project(":study:animation:animate_bounds"))
    implementation(project(":study:animation:shared_element_transition"))

    // ========================================
    // Study Modules - architecture
    // ========================================
    implementation(project(":study:architecture:view_model"))
    // hilt_viewmodel 제외 - Application 모듈로 유지 (@HiltAndroidApp 필요)
    implementation(project(":study:architecture:screen_and_component"))
    implementation(project(":study:architecture:slot_api_pattern"))
    implementation(project(":study:architecture:composition_local"))
    implementation(project(":study:architecture:unidirectional_data_flow"))

    // ========================================
    // Study Modules - interaction
    // ========================================
    implementation(project(":study:interaction:gesture"))
    implementation(project(":study:interaction:gesture_advanced"))
    implementation(project(":study:interaction:focus_management"))
    implementation(project(":study:interaction:drag_and_drop"))
    implementation(project(":study:interaction:haptic_feedback"))

    // ========================================
    // Study Modules - integration
    // ========================================
    implementation(project(":study:integration:lifecycle_integration"))
    implementation(project(":study:integration:image_loading"))
    implementation(project(":study:integration:media3_player"))
    implementation(project(":study:integration:interoperability"))
    implementation(project(":study:integration:camerax_compose"))

    // ========================================
    // Study Modules - system
    // ========================================
    implementation(project(":study:system:permission_handling"))
    implementation(project(":study:system:notification_integration"))
    implementation(project(":study:system:audio_recording"))

    // ========================================
    // Study Modules - testing
    // ========================================
    implementation(project(":study:testing:compose_testing"))
    implementation(project(":study:testing:screenshot_testing"))
    implementation(project(":study:testing:compose_compiler_metrics"))
    implementation(project(":study:testing:baseline_profiles"))
    implementation(project(":study:testing:semantics_accessibility"))
    implementation(project(":study:testing:visibility_tracking"))
    implementation(project(":study:testing:pausable_composition"))
    implementation(project(":study:testing:strong_skipping_mode"))
    implementation(project(":study:testing:compose_preview_testing"))

    // ========================================
    // Study Modules - multiplatform
    // ========================================
    implementation(project(":study:multiplatform:compose_multiplatform_intro"))
    implementation(project(":study:multiplatform:desktop_extensions"))
    implementation(project(":study:multiplatform:web_wasm"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}