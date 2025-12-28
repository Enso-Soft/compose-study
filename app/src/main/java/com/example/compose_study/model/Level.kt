package com.example.compose_study.model

/**
 * 학습 레벨을 나타내는 data class
 *
 * 각 레벨은 여러 학습 모듈을 포함하며,
 * 난이도에 따라 1부터 5까지 구분됩니다.
 *
 * @property number 레벨 번호 (1-5)
 * @property name 레벨 이름 (예: "입문", "기초")
 * @property description 레벨 설명
 * @property modules 해당 레벨에 속하는 모듈 목록
 */
data class Level(
    val number: Int,
    val name: String,
    val description: String,
    val modules: List<StudyModule> = emptyList()
) {
    /**
     * 모듈 개수
     */
    val moduleCount: Int
        get() = modules.size

    /**
     * 표시용 헤더 텍스트 (예: "Level 1: 입문")
     */
    val headerText: String
        get() = "Level $number: $name"

    /**
     * 모듈 개수를 포함한 표시용 텍스트
     */
    val headerWithCount: String
        get() = "$headerText ($moduleCount)"

    /**
     * 이 레벨에 모듈이 있는지 확인
     */
    val hasModules: Boolean
        get() = modules.isNotEmpty()

    /**
     * 특정 카테고리의 모듈만 필터링
     */
    fun getModulesByCategory(category: Category): List<StudyModule> {
        return modules.filter { it.category == category }
    }

    /**
     * 카테고리별로 그룹화된 모듈 맵
     */
    val modulesByCategory: Map<Category, List<StudyModule>>
        get() = modules.groupBy { it.category }

    companion object {
        /**
         * 미리 정의된 레벨 목록 생성 (모듈 없이)
         * README.md 학습 로드맵 기준 (Level 0-17)
         */
        fun createDefaultLevels(): List<Level> = listOf(
            Level(
                number = 0,
                name = "사전 준비",
                description = "Compose를 위한 Kotlin 기초"
            ),
            Level(
                number = 1,
                name = "Compose 입문",
                description = "Compose 소개, @Composable, Preview"
            ),
            Level(
                number = 2,
                name = "레이아웃 기초",
                description = "Layout, Modifier, 기본 컴포넌트"
            ),
            Level(
                number = 3,
                name = "상태 기초",
                description = "remember, recomposition으로 상태 관리"
            ),
            Level(
                number = 4,
                name = "UI 컴포넌트",
                description = "Button, Selection, Input, Display 컴포넌트"
            ),
            Level(
                number = 5,
                name = "리스트 & 검색",
                description = "LazyList, Pager, SearchBar"
            ),
            Level(
                number = 6,
                name = "앱 구조",
                description = "Scaffold, AppBar, Navigation Bar"
            ),
            Level(
                number = 7,
                name = "Side Effects",
                description = "LaunchedEffect, DisposableEffect"
            ),
            Level(
                number = 8,
                name = "상태 심화",
                description = "State Hoisting, derivedStateOf"
            ),
            Level(
                number = 9,
                name = "네비게이션",
                description = "Navigation, Deep Link"
            ),
            Level(
                number = 10,
                name = "애니메이션",
                description = "animate*AsState, Transition"
            ),
            Level(
                number = 11,
                name = "커스텀 레이아웃",
                description = "Custom Layout, Canvas"
            ),
            Level(
                number = 12,
                name = "아키텍처",
                description = "ViewModel, UDF 패턴"
            ),
            Level(
                number = 13,
                name = "제스처 & 인터랙션",
                description = "Gesture, Focus, Haptic"
            ),
            Level(
                number = 14,
                name = "외부 통합",
                description = "Lifecycle, Image Loading, Camera"
            ),
            Level(
                number = 15,
                name = "시스템 통합",
                description = "Permission, Notification"
            ),
            Level(
                number = 16,
                name = "테스트 & 성능",
                description = "Testing, Baseline Profiles"
            ),
            Level(
                number = 17,
                name = "멀티플랫폼",
                description = "Compose Multiplatform"
            )
        )

        /**
         * 모듈 목록에서 레벨별로 그룹화하여 Level 목록 생성
         */
        fun fromModules(modules: List<StudyModule>): List<Level> {
            val modulesByLevel = modules.groupBy { it.level }
            return createDefaultLevels().map { level ->
                level.copy(modules = modulesByLevel[level.number] ?: emptyList())
            }
        }
    }
}
