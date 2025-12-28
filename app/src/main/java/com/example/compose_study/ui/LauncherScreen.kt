package com.example.compose_study.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.compose_study.model.StudyModule
import com.example.compose_study.ui.components.LauncherSearchBar
import com.example.compose_study.ui.components.LevelSection
import com.example.compose_study.ui.components.RecentModuleRow
import com.example.compose_study.ui.components.RecentSearchList

/**
 * 통합 런처 메인 화면
 *
 * 모든 학습 모듈을 레벨별로 탐색하고 검색할 수 있는 화면입니다.
 * Material3 디자인을 따르며, 검색바, 최근 기록, 레벨 목록을 포함합니다.
 *
 * @param uiState 현재 UI 상태
 * @param onSearchQueryChange 검색어 변경 콜백
 * @param onSearchActiveChange 검색바 활성화 상태 변경 콜백
 * @param onRecentSearchClick 최근 검색어 클릭 콜백
 * @param onRecentSearchRemove 최근 검색어 삭제 콜백
 * @param onClearRecentSearches 전체 최근 검색어 삭제 콜백
 * @param onModuleClick 모듈 클릭 (학습 시작) 콜백
 * @param onModuleToggle 모듈 펼침/접힘 토글 콜백
 * @param onModuleCompleteToggle 모듈 완료 상태 토글 콜백
 * @param onLevelToggle 레벨 펼침/접힘 토글 콜백
 * @param getPrerequisites 모듈의 선행 모듈 목록을 가져오는 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherScreen(
    uiState: LauncherUiState,
    onSearchQueryChange: (String) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onRecentSearchRemove: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onModuleClick: (StudyModule) -> Unit,
    onModuleToggle: (String) -> Unit,
    onModuleCompleteToggle: (String) -> Unit,
    onLevelToggle: (Int) -> Unit,
    getPrerequisites: (StudyModule) -> List<StudyModule>,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                // 검색바가 활성화되지 않았을 때만 TopAppBar 표시
                AnimatedVisibility(
                    visible = !uiState.isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Compose Study",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = "모듈을 탐색하고 학습을 시작하세요",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        ),
                        scrollBehavior = scrollBehavior
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 검색바 (항상 표시)
                LauncherSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    isActive = uiState.isSearchActive,
                    onActiveChange = onSearchActiveChange,
                    searchResults = uiState.filteredModules,
                    recentSearches = uiState.recentSearches,
                    recentModules = uiState.recentModules,
                    expandedModules = uiState.expandedModules,
                    completedModules = uiState.completedModules,
                    onModuleClick = onModuleClick,
                    onModuleToggle = onModuleToggle,
                    onModuleCompleteToggle = onModuleCompleteToggle,
                    onRecentSearchClick = onRecentSearchClick,
                    onRecentSearchRemove = onRecentSearchRemove,
                    onClearRecentSearches = onClearRecentSearches,
                    getPrerequisites = getPrerequisites,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // 검색바가 비활성화 상태일 때만 메인 콘텐츠 표시
                AnimatedVisibility(
                    visible = !uiState.isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    when {
                        uiState.isLoading -> {
                            LoadingContent()
                        }
                        uiState.hasError -> {
                            ErrorContent(message = uiState.error ?: "알 수 없는 오류")
                        }
                        uiState.allLevels.isEmpty() -> {
                            EmptyContent()
                        }
                        else -> {
                            MainContent(
                                uiState = uiState,
                                onRecentSearchClick = onRecentSearchClick,
                                onRecentSearchRemove = onRecentSearchRemove,
                                onClearRecentSearches = onClearRecentSearches,
                                onModuleClick = onModuleClick,
                                onModuleToggle = onModuleToggle,
                                onModuleCompleteToggle = onModuleCompleteToggle,
                                onLevelToggle = onLevelToggle,
                                getPrerequisites = getPrerequisites
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 메인 콘텐츠 영역
 *
 * 최근 검색어, 최근 모듈, 레벨 목록을 표시합니다.
 */
@Composable
private fun MainContent(
    uiState: LauncherUiState,
    onRecentSearchClick: (String) -> Unit,
    onRecentSearchRemove: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onModuleClick: (StudyModule) -> Unit,
    onModuleToggle: (String) -> Unit,
    onModuleCompleteToggle: (String) -> Unit,
    onLevelToggle: (Int) -> Unit,
    getPrerequisites: (StudyModule) -> List<StudyModule>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 최근 검색어 섹션
        if (uiState.hasRecentSearches) {
            item(key = "recent_searches") {
                RecentSearchList(
                    searches = uiState.recentSearches,
                    onSearchClick = onRecentSearchClick,
                    onSearchRemove = onRecentSearchRemove,
                    onClearAll = onClearRecentSearches,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // 최근 모듈 섹션
        if (uiState.hasRecentModules) {
            item(key = "recent_modules") {
                RecentModuleRow(
                    modules = uiState.recentModules,
                    onModuleClick = onModuleClick,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        // 레벨 섹션 헤더
        item(key = "levels_header") {
            Text(
                text = "학습 모듈",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        // 레벨 목록
        items(
            items = uiState.allLevels,
            key = { "level_${it.number}" }
        ) { level ->
            LevelSection(
                level = level,
                isExpanded = uiState.isLevelExpanded(level.number),
                expandedModules = uiState.expandedModules,
                completedModules = uiState.completedModules,
                onToggle = { onLevelToggle(level.number) },
                onModuleToggle = onModuleToggle,
                onModuleLaunch = onModuleClick,
                onModuleCompleteToggle = onModuleCompleteToggle,
                getPrerequisites = getPrerequisites
            )
        }

        // 하단 여백
        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * 로딩 상태 UI
 */
@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "모듈 불러오는 중...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 에러 상태 UI
 */
@Composable
private fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "오류 발생",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 빈 상태 UI (모듈이 없을 때)
 */
@Composable
private fun EmptyContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.School,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "등록된 모듈이 없습니다",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "학습 모듈을 추가해 주세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
