package com.example.compose_study.ui

import com.example.compose_study.model.Level
import com.example.compose_study.model.StudyModule

/**
 * 런처 화면의 UI 상태를 표현하는 data class
 *
 * 단방향 데이터 흐름(UDF)을 따르며, ViewModel에서 상태를 관리합니다.
 *
 * @property searchQuery 현재 검색어
 * @property isSearchActive 검색바가 활성화되어 있는지 여부
 * @property filteredModules 검색 결과로 필터링된 모듈 목록
 * @property recentSearches 최근 검색어 목록 (최신순)
 * @property recentModules 최근 학습한 모듈 목록 (최신순)
 * @property expandedLevels 펼쳐진 레벨 번호 Set
 * @property expandedModules 상세 정보가 펼쳐진 모듈 ID Set
 * @property completedModules 학습 완료된 모듈 ID Set
 * @property allLevels 전체 레벨 목록 (모듈 포함)
 * @property isLoading 데이터 로딩 중 여부
 * @property error 에러 메시지 (null이면 에러 없음)
 */
data class LauncherUiState(
    // 검색 관련
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val filteredModules: List<StudyModule> = emptyList(),

    // 최근 데이터
    val recentSearches: List<String> = emptyList(),
    val recentModules: List<StudyModule> = emptyList(),

    // 펼침 상태
    val expandedLevels: Set<Int> = emptySet(), // 초기 상태: 모두 접힘 (DataStore에서 로드)
    val expandedModules: Set<String> = emptySet(),

    // 완료 상태
    val completedModules: Set<String> = emptySet(),

    // 전체 데이터
    val allLevels: List<Level> = emptyList(),

    // 로딩/에러 상태
    val isLoading: Boolean = false,
    val error: String? = null
) {
    /**
     * 검색 결과가 있는지 확인
     */
    val hasSearchResults: Boolean
        get() = filteredModules.isNotEmpty()

    /**
     * 검색 중인지 확인 (검색어가 있는 경우)
     */
    val isSearching: Boolean
        get() = searchQuery.isNotBlank()

    /**
     * 최근 검색어가 있는지 확인
     */
    val hasRecentSearches: Boolean
        get() = recentSearches.isNotEmpty()

    /**
     * 최근 모듈이 있는지 확인
     */
    val hasRecentModules: Boolean
        get() = recentModules.isNotEmpty()

    /**
     * 특정 레벨이 펼쳐져 있는지 확인
     */
    fun isLevelExpanded(level: Int): Boolean = level in expandedLevels

    /**
     * 특정 모듈이 펼쳐져 있는지 확인
     */
    fun isModuleExpanded(moduleId: String): Boolean = moduleId in expandedModules

    /**
     * 특정 모듈이 완료되었는지 확인
     */
    fun isModuleCompleted(moduleId: String): Boolean = moduleId in completedModules

    /**
     * 전체 모듈 수 (allLevels 기반)
     */
    val totalModuleCount: Int
        get() = allLevels.sumOf { it.modules.size }

    /**
     * 완료된 모듈 수
     */
    val completedModuleCount: Int
        get() = completedModules.size

    /**
     * 전체 진행률 (0.0 ~ 1.0)
     */
    val completionProgress: Float
        get() = if (totalModuleCount > 0) {
            completedModuleCount.toFloat() / totalModuleCount.toFloat()
        } else {
            0f
        }

    /**
     * 에러가 있는지 확인
     */
    val hasError: Boolean
        get() = error != null
}
