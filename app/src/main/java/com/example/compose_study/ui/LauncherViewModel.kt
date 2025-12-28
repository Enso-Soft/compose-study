package com.example.compose_study.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose_study.data.repository.ModuleRepository
import com.example.compose_study.data.repository.RecentRepository
import com.example.compose_study.model.StudyModule
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 런처 화면의 ViewModel
 *
 * Repository를 통해 데이터를 조회하고, UI 상태를 관리합니다.
 * 검색, 최근 항목 관리, 레벨/모듈 펼침 상태를 처리합니다.
 *
 * @param moduleRepository 모듈 데이터 접근 Repository
 * @param recentRepository 최근 검색/모듈 저장 Repository
 */
@OptIn(FlowPreview::class)
class LauncherViewModel(
    private val moduleRepository: ModuleRepository,
    private val recentRepository: RecentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    // 검색어 Flow (debounce 적용을 위해 별도 관리)
    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadInitialData()
        observeSearchQuery()
        observeRecentData()
    }

    // ========================================
    // 초기화
    // ========================================

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val levels = moduleRepository.getLevels()
                _uiState.update {
                    it.copy(
                        allLevels = levels,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "모듈 로드 실패: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300) // 300ms debounce
                .distinctUntilChanged()
                .collectLatest { query ->
                    val filtered = if (query.isBlank()) {
                        emptyList()
                    } else {
                        moduleRepository.searchModules(query)
                    }
                    _uiState.update { it.copy(filteredModules = filtered) }
                }
        }
    }

    private fun observeRecentData() {
        viewModelScope.launch {
            // 최근 검색어 관찰
            recentRepository.getRecentSearches().collectLatest { searches ->
                _uiState.update { it.copy(recentSearches = searches) }
            }
        }

        viewModelScope.launch {
            // 최근 모듈 관찰 (ID → StudyModule 변환)
            recentRepository.getRecentModules()
                .map { moduleIds ->
                    moduleIds.mapNotNull { id ->
                        moduleRepository.getModuleById(id)
                    }
                }
                .collectLatest { modules ->
                    _uiState.update { it.copy(recentModules = modules) }
                }
        }

        viewModelScope.launch {
            // 완료된 모듈 관찰
            recentRepository.getCompletedModules().collectLatest { completedModules ->
                _uiState.update { it.copy(completedModules = completedModules) }
            }
        }

        viewModelScope.launch {
            // 펼쳐진 레벨 관찰
            recentRepository.getExpandedLevels().collectLatest { expandedLevels ->
                _uiState.update { it.copy(expandedLevels = expandedLevels) }
            }
        }
    }

    // ========================================
    // 검색 관련
    // ========================================

    /**
     * 검색어 변경
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    /**
     * 검색바 활성화
     */
    fun onSearchActivate() {
        _uiState.update { it.copy(isSearchActive = true) }
    }

    /**
     * 검색바 비활성화
     */
    fun onSearchDeactivate() {
        _uiState.update {
            it.copy(
                isSearchActive = false,
                searchQuery = "",
                filteredModules = emptyList()
            )
        }
        searchQueryFlow.value = ""
    }

    /**
     * 검색 실행 (엔터 키 등)
     * 검색어를 최근 검색어에 저장
     */
    fun onSearchSubmit() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isNotBlank()) {
            viewModelScope.launch {
                recentRepository.addRecentSearch(query)
            }
        }
    }

    // ========================================
    // 최근 검색어 관련
    // ========================================

    /**
     * 최근 검색어 클릭
     * 해당 검색어로 검색 실행
     */
    fun onRecentSearchClick(query: String) {
        onSearchQueryChange(query)
        onSearchActivate()
    }

    /**
     * 최근 검색어 삭제
     */
    fun onRecentSearchRemove(query: String) {
        viewModelScope.launch {
            recentRepository.removeRecentSearch(query)
        }
    }

    /**
     * 모든 최근 검색어 삭제
     */
    fun onClearRecentSearches() {
        viewModelScope.launch {
            recentRepository.clearRecentSearches()
        }
    }

    // ========================================
    // 모듈 관련
    // ========================================

    /**
     * 모듈 실행 (학습 시작)
     * 최근 모듈에 저장
     */
    fun onModuleLaunch(module: StudyModule) {
        viewModelScope.launch {
            recentRepository.addRecentModule(module.id)
        }
    }

    /**
     * 최근 모듈 전체 삭제
     */
    fun onClearRecentModules() {
        viewModelScope.launch {
            recentRepository.clearRecentModules()
        }
    }

    // ========================================
    // 완료 상태 관련
    // ========================================

    /**
     * 모듈 완료 상태 토글
     */
    fun onModuleCompleteToggle(moduleId: String) {
        viewModelScope.launch {
            recentRepository.toggleModuleCompletion(moduleId)
        }
    }

    /**
     * 모든 완료 상태 초기화
     */
    fun onClearCompletedModules() {
        viewModelScope.launch {
            recentRepository.clearCompletedModules()
        }
    }

    // ========================================
    // 펼침/접힘 관련
    // ========================================

    /**
     * 레벨 펼침/접힘 토글
     */
    fun onLevelToggle(level: Int) {
        val newExpandedLevels = _uiState.value.expandedLevels.let { current ->
            if (level in current) current - level else current + level
        }
        _uiState.update { it.copy(expandedLevels = newExpandedLevels) }
        viewModelScope.launch {
            recentRepository.saveExpandedLevels(newExpandedLevels)
        }
    }

    /**
     * 모듈 상세 펼침/접힘 토글
     */
    fun onModuleToggle(moduleId: String) {
        _uiState.update { state ->
            val newExpandedModules = if (moduleId in state.expandedModules) {
                state.expandedModules - moduleId
            } else {
                state.expandedModules + moduleId
            }
            state.copy(expandedModules = newExpandedModules)
        }
    }

    /**
     * 모든 레벨 펼치기
     */
    fun onExpandAllLevels() {
        val allLevels = (0..17).toSet()
        _uiState.update { it.copy(expandedLevels = allLevels) }
        viewModelScope.launch {
            recentRepository.saveExpandedLevels(allLevels)
        }
    }

    /**
     * 모든 레벨 접기
     */
    fun onCollapseAllLevels() {
        _uiState.update { it.copy(expandedLevels = emptySet()) }
        viewModelScope.launch {
            recentRepository.saveExpandedLevels(emptySet())
        }
    }

    // ========================================
    // 에러 처리
    // ========================================

    /**
     * 에러 메시지 확인 (dismiss)
     */
    fun onErrorDismiss() {
        _uiState.update { it.copy(error = null) }
    }
}
