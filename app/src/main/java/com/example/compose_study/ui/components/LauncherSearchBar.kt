package com.example.compose_study.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.example.compose_study.model.StudyModule

/**
 * 런처 검색바 컴포넌트
 *
 * Material3 DockedSearchBar를 사용하여 모듈 검색 기능을 제공합니다.
 * 활성화 시 최근 검색어, 최근 모듈, 검색 결과를 표시합니다.
 *
 * @param query 현재 검색어
 * @param onQueryChange 검색어 변경 콜백
 * @param isActive 검색바 활성화 상태
 * @param onActiveChange 활성화 상태 변경 콜백
 * @param searchResults 검색 결과 모듈 목록
 * @param recentSearches 최근 검색어 목록
 * @param recentModules 최근 학습 모듈 목록
 * @param expandedModules 펼쳐진 모듈 ID Set
 * @param completedModules 완료된 모듈 ID Set
 * @param onModuleClick 모듈 클릭 콜백
 * @param onModuleToggle 모듈 펼침/접힘 토글 콜백
 * @param onModuleCompleteToggle 모듈 완료 상태 토글 콜백
 * @param onRecentSearchClick 최근 검색어 클릭 콜백
 * @param onRecentSearchRemove 최근 검색어 삭제 콜백
 * @param onClearRecentSearches 전체 검색어 삭제 콜백
 * @param getPrerequisites 모듈의 선행 모듈 목록을 가져오는 함수
 * @param modifier Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LauncherSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchResults: List<StudyModule>,
    recentSearches: List<String>,
    recentModules: List<StudyModule>,
    expandedModules: Set<String>,
    completedModules: Set<String>,
    onModuleClick: (StudyModule) -> Unit,
    onModuleToggle: (String) -> Unit,
    onModuleCompleteToggle: (String) -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onRecentSearchRemove: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    getPrerequisites: (StudyModule) -> List<StudyModule>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        DockedSearchBar(
            shape = androidx.compose.material3.MaterialTheme.shapes.large,
            colors = SearchBarDefaults.colors(
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                dividerColor = Color.Transparent
            ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = { /* 검색 실행 - 이미 실시간 필터링 중 */ },
                    expanded = isActive,
                    onExpandedChange = onActiveChange,
                    placeholder = {
                        Text(
                            text = "모듈 검색...",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        if (isActive) {
                            IconButton(onClick = { onActiveChange(false) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "닫기"
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "검색"
                            )
                        }
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "검색어 지우기"
                                )
                            }
                        }
                    }
                )
            },
            expanded = isActive,
            onExpandedChange = onActiveChange,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { traversalIndex = 0f }
        ) {
            // 검색바 확장 시 표시되는 콘텐츠
            SearchBarContent(
                query = query,
                searchResults = searchResults,
                recentSearches = recentSearches,
                recentModules = recentModules,
                expandedModules = expandedModules,
                completedModules = completedModules,
                onModuleClick = onModuleClick,
                onModuleToggle = onModuleToggle,
                onModuleCompleteToggle = onModuleCompleteToggle,
                onRecentSearchClick = onRecentSearchClick,
                onRecentSearchRemove = onRecentSearchRemove,
                onClearRecentSearches = onClearRecentSearches,
                getPrerequisites = getPrerequisites
            )
        }
    }
}

/**
 * 검색바 확장 시 표시되는 콘텐츠
 *
 * 검색어가 비어있으면 최근 검색어/모듈을 표시하고,
 * 검색어가 있으면 검색 결과를 표시합니다.
 */
@Composable
private fun SearchBarContent(
    query: String,
    searchResults: List<StudyModule>,
    recentSearches: List<String>,
    recentModules: List<StudyModule>,
    expandedModules: Set<String>,
    completedModules: Set<String>,
    onModuleClick: (StudyModule) -> Unit,
    onModuleToggle: (String) -> Unit,
    onModuleCompleteToggle: (String) -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onRecentSearchRemove: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    getPrerequisites: (StudyModule) -> List<StudyModule>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (query.isEmpty()) {
            // 검색어가 비어있을 때: 최근 검색어 + 최근 모듈
            RecentSearchList(
                searches = recentSearches,
                onSearchClick = onRecentSearchClick,
                onSearchRemove = onRecentSearchRemove,
                onClearAll = onClearRecentSearches
            )

            RecentModuleRow(
                modules = recentModules,
                onModuleClick = onModuleClick
            )
        } else {
            // 검색어가 있을 때: 검색 결과
            SearchResultList(
                modules = searchResults,
                query = query,
                expandedModules = expandedModules,
                completedModules = completedModules,
                onModuleToggle = onModuleToggle,
                onModuleLaunch = onModuleClick,
                onModuleCompleteToggle = onModuleCompleteToggle,
                getPrerequisites = getPrerequisites
            )
        }
    }
}
