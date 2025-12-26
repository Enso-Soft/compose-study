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
include(":study:launched_effect")
include(":study:disposable_effect")
include(":study:remember_coroutine_scope")
include(":study:side_effect")
include(":study:derived_state_of")
include(":study:kotlin_basics")
include(":study:composable_function")
include(":study:basic_ui_components")
include(":study:layout_and_modifier")
include(":study:remember")
include(":study:state_hoisting")
include(":study:recomposition")
include(":study:remember_saveable")
include(":study:navigation")
include(":study:view_model")
include(":study:stability")
include(":study:lifecycle_integration")
include(":study:preview")
include(":study:animation_basics")
include(":study:scaffold_and_theming")
include(":study:compose_testing")
include(":study:interoperability")
include(":study:custom_layout")
include(":study:constraint_layout")
include(":study:animation_advanced")
include(":study:hilt_viewmodel")
include(":study:deep_link")
include(":study:lazy_layouts")
include(":study:composition_local")
include(":study:window_insets")
include(":study:gesture")
include(":study:paging_compose")
include(":study:pager")
include(":study:back_handler")
include(":study:produce_state")
include(":study:pull_to_refresh")
