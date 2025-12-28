package com.example.compose_study.model

import androidx.activity.ComponentActivity

/**
 * 학습 모듈을 나타내는 data class
 *
 * 각 학습 모듈은 독립 실행 가능한 Activity를 가지며,
 * 레벨, 카테고리, 선행 모듈 정보를 포함합니다.
 *
 * @property id 고유 식별자 (예: "launched_effect", "button")
 * @property name 표시 이름 (예: "LaunchedEffect", "Button")
 * @property description 짧은 설명 (목록에 표시)
 * @property detailDescription 상세 설명 (카드 펼침 시 표시)
 * @property level 난이도 레벨 (0-17, README.md 학습 로드맵 기준)
 * @property category 카테고리
 * @property prerequisites 선행 학습이 필요한 모듈 ID 목록
 * @property activityClass 실행할 Activity 클래스
 */
data class StudyModule(
    val id: String,
    val name: String,
    val description: String,
    val detailDescription: String,
    val level: Int,
    val category: Category,
    val prerequisites: List<String> = emptyList(),
    val activityClass: Class<out ComponentActivity>
) {
    /**
     * 검색 시 사용할 키워드 목록
     */
    val searchKeywords: List<String>
        get() = listOf(
            id,
            name,
            description,
            category.displayName
        ).map { it.lowercase() }

    /**
     * 주어진 쿼리로 이 모듈을 검색할 수 있는지 확인
     */
    fun matchesQuery(query: String): Boolean {
        if (query.isBlank()) return true
        val lowercaseQuery = query.lowercase().trim()
        return searchKeywords.any { it.contains(lowercaseQuery) } ||
                detailDescription.lowercase().contains(lowercaseQuery)
    }

    /**
     * 선행 모듈이 있는지 확인
     */
    val hasPrerequisites: Boolean
        get() = prerequisites.isNotEmpty()

    /**
     * 표시용 레벨 문자열
     */
    val levelText: String
        get() = "Level $level"

    companion object {
        /**
         * 레벨 범위 (0-17, README.md 학습 로드맵 기준)
         */
        val LEVEL_RANGE = 0..17

        /**
         * 레벨별 이름
         */
        fun getLevelName(level: Int): String = when (level) {
            0 -> "사전 준비"
            1 -> "Compose 입문"
            2 -> "레이아웃 기초"
            3 -> "상태 기초"
            4 -> "UI 컴포넌트"
            5 -> "리스트 & 검색"
            6 -> "앱 구조"
            7 -> "Side Effects"
            8 -> "상태 심화"
            9 -> "네비게이션"
            10 -> "애니메이션"
            11 -> "커스텀 레이아웃"
            12 -> "아키텍처"
            13 -> "제스처 & 인터랙션"
            14 -> "외부 통합"
            15 -> "시스템 통합"
            16 -> "테스트 & 성능"
            17 -> "멀티플랫폼"
            else -> "기타"
        }

        /**
         * 레벨별 설명
         */
        fun getLevelDescription(level: Int): String = when (level) {
            0 -> "Compose를 위한 Kotlin 기초"
            1 -> "Compose 소개, @Composable, Preview"
            2 -> "Layout, Modifier, 기본 컴포넌트"
            3 -> "remember, recomposition으로 상태 관리"
            4 -> "Button, Selection, Input, Display 컴포넌트"
            5 -> "LazyList, Pager, SearchBar"
            6 -> "Scaffold, AppBar, Navigation Bar"
            7 -> "LaunchedEffect, DisposableEffect"
            8 -> "State Hoisting, derivedStateOf"
            9 -> "Navigation, Deep Link"
            10 -> "animate*AsState, Transition"
            11 -> "Custom Layout, Canvas"
            12 -> "ViewModel, UDF 패턴"
            13 -> "Gesture, Focus, Haptic"
            14 -> "Lifecycle, Image Loading, Camera"
            15 -> "Permission, Notification"
            16 -> "Testing, Baseline Profiles"
            17 -> "Compose Multiplatform"
            else -> ""
        }
    }
}
