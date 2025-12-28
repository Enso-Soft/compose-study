package com.example.compose_study.model

/**
 * 학습 모듈의 카테고리를 정의하는 enum class
 *
 * study/ 디렉토리의 하위 폴더 구조와 1:1 매핑됩니다.
 */
enum class Category(
    val displayName: String,
    val description: String,
    val emoji: String
) {
    BASICS(
        displayName = "기초",
        description = "Kotlin과 Compose 입문",
        emoji = "📚"
    ),
    LAYOUT(
        displayName = "레이아웃",
        description = "화면 배치와 Modifier",
        emoji = "📐"
    ),
    STATE(
        displayName = "상태 관리",
        description = "상태와 Recomposition",
        emoji = "🔄"
    ),
    COMPONENT(
        displayName = "컴포넌트",
        description = "UI 컴포넌트",
        emoji = "🧩"
    ),
    LIST(
        displayName = "리스트",
        description = "LazyColumn과 LazyRow",
        emoji = "📋"
    ),
    SEARCH(
        displayName = "검색",
        description = "SearchBar 구현",
        emoji = "🔍"
    ),
    STRUCTURE(
        displayName = "구조",
        description = "Scaffold와 앱 구조",
        emoji = "🏗️"
    ),
    EFFECT(
        displayName = "Side Effect",
        description = "LaunchedEffect, DisposableEffect 등",
        emoji = "⚡"
    ),
    NAVIGATION(
        displayName = "네비게이션",
        description = "화면 전환과 라우팅",
        emoji = "🧭"
    ),
    ANIMATION(
        displayName = "애니메이션",
        description = "움직이는 UI 효과",
        emoji = "🎬"
    ),
    ARCHITECTURE(
        displayName = "아키텍처",
        description = "MVVM, ViewModel, DI",
        emoji = "🏛️"
    ),
    INTERACTION(
        displayName = "인터랙션",
        description = "제스처와 터치 처리",
        emoji = "👆"
    ),
    INTEGRATION(
        displayName = "통합",
        description = "외부 라이브러리 연동",
        emoji = "🔌"
    ),
    SYSTEM(
        displayName = "시스템",
        description = "권한, 알림 등 시스템 기능",
        emoji = "⚙️"
    ),
    TESTING(
        displayName = "테스트",
        description = "테스트와 성능 측정",
        emoji = "🧪"
    ),
    MULTIPLATFORM(
        displayName = "멀티플랫폼",
        description = "Compose Multiplatform",
        emoji = "🌐"
    );

    /**
     * 카테고리의 표시용 문자열 (이모지 + 이름)
     */
    val label: String
        get() = "$emoji $displayName"

    companion object {
        /**
         * 디렉토리 이름으로 Category 찾기
         * @param dirName study/ 하위의 디렉토리 이름 (예: "basics", "effect")
         * @return 매칭되는 Category, 없으면 null
         */
        fun fromDirectoryName(dirName: String): Category? {
            return entries.find { it.name.equals(dirName, ignoreCase = true) }
        }
    }
}
