package com.example.compose_study.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * 최근 검색어와 최근 모듈을 저장/조회하는 Repository 인터페이스
 *
 * DataStore Preferences를 사용하여 로컬에 데이터를 저장합니다.
 * Flow를 통해 데이터 변경을 관찰할 수 있습니다.
 */
interface RecentRepository {

    // ========================================
    // 최근 검색어
    // ========================================

    /**
     * 최근 검색어 목록 조회 (최신순)
     * @return 검색어 목록 Flow (최대 10개)
     */
    fun getRecentSearches(): Flow<List<String>>

    /**
     * 검색어 추가
     * - 이미 있는 검색어는 최신으로 이동
     * - 최대 개수 초과 시 가장 오래된 항목 삭제
     * @param query 검색어
     */
    suspend fun addRecentSearch(query: String)

    /**
     * 특정 검색어 삭제
     * @param query 삭제할 검색어
     */
    suspend fun removeRecentSearch(query: String)

    /**
     * 모든 최근 검색어 삭제
     */
    suspend fun clearRecentSearches()

    // ========================================
    // 최근 모듈
    // ========================================

    /**
     * 최근 학습한 모듈 ID 목록 조회 (최신순)
     * @return 모듈 ID 목록 Flow (최대 10개)
     */
    fun getRecentModules(): Flow<List<String>>

    /**
     * 모듈 추가
     * - 이미 있는 모듈은 최신으로 이동
     * - 최대 개수 초과 시 가장 오래된 항목 삭제
     * @param moduleId 모듈 ID
     */
    suspend fun addRecentModule(moduleId: String)

    /**
     * 모든 최근 모듈 삭제
     */
    suspend fun clearRecentModules()

    // ========================================
    // 모듈 완료 상태
    // ========================================

    /**
     * 완료된 모듈 ID Set 조회
     * @return 완료된 모듈 ID Set Flow
     */
    fun getCompletedModules(): Flow<Set<String>>

    /**
     * 모듈 완료 상태 토글
     * @param moduleId 모듈 ID
     */
    suspend fun toggleModuleCompletion(moduleId: String)

    /**
     * 모듈을 완료로 표시
     * @param moduleId 모듈 ID
     */
    suspend fun markModuleCompleted(moduleId: String)

    /**
     * 모듈 완료 상태 해제
     * @param moduleId 모듈 ID
     */
    suspend fun markModuleIncomplete(moduleId: String)

    /**
     * 모든 완료 상태 초기화
     */
    suspend fun clearCompletedModules()

    // ========================================
    // 레벨 펼침 상태
    // ========================================

    /**
     * 펼쳐진 레벨 번호 Set 조회
     * @return 펼쳐진 레벨 번호 Set Flow (초기값: emptySet)
     */
    fun getExpandedLevels(): Flow<Set<Int>>

    /**
     * 레벨 펼침 상태 저장
     * @param levels 펼쳐진 레벨 번호 Set
     */
    suspend fun saveExpandedLevels(levels: Set<Int>)

    companion object {
        /**
         * 최근 검색어 최대 개수
         */
        const val MAX_RECENT_SEARCHES = 10

        /**
         * 최근 모듈 최대 개수
         */
        const val MAX_RECENT_MODULES = 10
    }
}
