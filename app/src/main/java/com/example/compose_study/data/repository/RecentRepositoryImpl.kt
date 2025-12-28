package com.example.compose_study.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Context extension으로 DataStore 인스턴스 생성
 *
 * 앱 전체에서 단일 인스턴스로 사용됩니다.
 */
val Context.recentDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "recent_preferences"
)

/**
 * RecentRepository의 구현체
 *
 * DataStore Preferences를 사용하여 최근 검색어와 모듈을 저장합니다.
 * 리스트는 구분자(|)로 연결된 문자열로 저장되어 순서가 보존됩니다.
 *
 * @param dataStore DataStore 인스턴스 (Context.recentDataStore 사용)
 */
class RecentRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : RecentRepository {

    companion object {
        // DataStore Keys
        private val RECENT_SEARCHES_KEY = stringPreferencesKey("recent_searches")
        private val RECENT_MODULES_KEY = stringPreferencesKey("recent_modules")
        private val COMPLETED_MODULES_KEY = stringPreferencesKey("completed_modules")
        private val EXPANDED_LEVELS_KEY = stringPreferencesKey("expanded_levels")

        // 리스트 구분자
        private const val SEPARATOR = "|"
    }

    // ========================================
    // 최근 검색어
    // ========================================

    override fun getRecentSearches(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[RECENT_SEARCHES_KEY]?.toList() ?: emptyList()
        }
    }

    override suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        dataStore.edit { preferences ->
            val currentList = preferences[RECENT_SEARCHES_KEY]?.toList()?.toMutableList()
                ?: mutableListOf()

            // 이미 있으면 제거 (나중에 맨 앞에 추가)
            currentList.remove(query)

            // 맨 앞에 추가
            currentList.add(0, query)

            // 최대 개수 유지
            val trimmedList = currentList.take(RecentRepository.MAX_RECENT_SEARCHES)

            preferences[RECENT_SEARCHES_KEY] = trimmedList.toStorageString()
        }
    }

    override suspend fun removeRecentSearch(query: String) {
        dataStore.edit { preferences ->
            val currentList = preferences[RECENT_SEARCHES_KEY]?.toList()?.toMutableList()
                ?: mutableListOf()

            currentList.remove(query)

            preferences[RECENT_SEARCHES_KEY] = currentList.toStorageString()
        }
    }

    override suspend fun clearRecentSearches() {
        dataStore.edit { preferences ->
            preferences.remove(RECENT_SEARCHES_KEY)
        }
    }

    // ========================================
    // 최근 모듈
    // ========================================

    override fun getRecentModules(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[RECENT_MODULES_KEY]?.toList() ?: emptyList()
        }
    }

    override suspend fun addRecentModule(moduleId: String) {
        if (moduleId.isBlank()) return

        dataStore.edit { preferences ->
            val currentList = preferences[RECENT_MODULES_KEY]?.toList()?.toMutableList()
                ?: mutableListOf()

            // 이미 있으면 제거 (나중에 맨 앞에 추가)
            currentList.remove(moduleId)

            // 맨 앞에 추가
            currentList.add(0, moduleId)

            // 최대 개수 유지
            val trimmedList = currentList.take(RecentRepository.MAX_RECENT_MODULES)

            preferences[RECENT_MODULES_KEY] = trimmedList.toStorageString()
        }
    }

    override suspend fun clearRecentModules() {
        dataStore.edit { preferences ->
            preferences.remove(RECENT_MODULES_KEY)
        }
    }

    // ========================================
    // 모듈 완료 상태
    // ========================================

    override fun getCompletedModules(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[COMPLETED_MODULES_KEY]?.toList()?.toSet() ?: emptySet()
        }
    }

    override suspend fun toggleModuleCompletion(moduleId: String) {
        if (moduleId.isBlank()) return

        dataStore.edit { preferences ->
            val currentSet = preferences[COMPLETED_MODULES_KEY]?.toList()?.toMutableSet()
                ?: mutableSetOf()

            if (currentSet.contains(moduleId)) {
                currentSet.remove(moduleId)
            } else {
                currentSet.add(moduleId)
            }

            preferences[COMPLETED_MODULES_KEY] = currentSet.toList().toStorageString()
        }
    }

    override suspend fun markModuleCompleted(moduleId: String) {
        if (moduleId.isBlank()) return

        dataStore.edit { preferences ->
            val currentSet = preferences[COMPLETED_MODULES_KEY]?.toList()?.toMutableSet()
                ?: mutableSetOf()

            currentSet.add(moduleId)

            preferences[COMPLETED_MODULES_KEY] = currentSet.toList().toStorageString()
        }
    }

    override suspend fun markModuleIncomplete(moduleId: String) {
        if (moduleId.isBlank()) return

        dataStore.edit { preferences ->
            val currentSet = preferences[COMPLETED_MODULES_KEY]?.toList()?.toMutableSet()
                ?: mutableSetOf()

            currentSet.remove(moduleId)

            preferences[COMPLETED_MODULES_KEY] = currentSet.toList().toStorageString()
        }
    }

    override suspend fun clearCompletedModules() {
        dataStore.edit { preferences ->
            preferences.remove(COMPLETED_MODULES_KEY)
        }
    }

    // ========================================
    // 레벨 펼침 상태
    // ========================================

    override fun getExpandedLevels(): Flow<Set<Int>> {
        return dataStore.data.map { preferences ->
            preferences[EXPANDED_LEVELS_KEY]?.toIntSet() ?: emptySet()
        }
    }

    override suspend fun saveExpandedLevels(levels: Set<Int>) {
        dataStore.edit { preferences ->
            preferences[EXPANDED_LEVELS_KEY] = levels.toStorageString()
        }
    }

    // ========================================
    // Helper Extensions
    // ========================================

    /**
     * 저장된 문자열을 리스트로 변환
     */
    private fun String.toList(): List<String> {
        return if (this.isEmpty()) {
            emptyList()
        } else {
            this.split(SEPARATOR).filter { it.isNotEmpty() }
        }
    }

    /**
     * 리스트를 저장용 문자열로 변환
     */
    private fun List<String>.toStorageString(): String {
        return this.joinToString(SEPARATOR)
    }

    /**
     * 저장된 문자열을 Int Set으로 변환
     */
    private fun String.toIntSet(): Set<Int> {
        return if (this.isEmpty()) {
            emptySet()
        } else {
            this.split(SEPARATOR)
                .filter { it.isNotEmpty() }
                .mapNotNull { it.toIntOrNull() }
                .toSet()
        }
    }

    /**
     * Int Set을 저장용 문자열로 변환
     */
    private fun Set<Int>.toStorageString(): String {
        return this.joinToString(SEPARATOR) { it.toString() }
    }
}
