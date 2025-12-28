package com.example.compose_study.ui.components

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
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.compose_study.model.StudyModule

/**
 * 검색 결과 목록 컴포넌트
 *
 * 검색된 모듈을 ModuleCard 목록으로 표시합니다.
 * 검색 결과가 없으면 빈 상태를 표시합니다.
 *
 * @param modules 검색 결과 모듈 목록
 * @param query 현재 검색어 (빈 상태 메시지에 사용)
 * @param expandedModules 펼쳐진 모듈 ID Set
 * @param completedModules 완료된 모듈 ID Set
 * @param onModuleToggle 모듈 펼침/접힘 토글 콜백
 * @param onModuleLaunch 모듈 학습 시작 콜백
 * @param onModuleCompleteToggle 모듈 완료 상태 토글 콜백
 * @param getPrerequisites 모듈의 선행 모듈 목록을 가져오는 함수
 * @param modifier Modifier
 */
@Composable
fun SearchResultList(
    modules: List<StudyModule>,
    query: String,
    expandedModules: Set<String>,
    completedModules: Set<String>,
    onModuleToggle: (String) -> Unit,
    onModuleLaunch: (StudyModule) -> Unit,
    onModuleCompleteToggle: (String) -> Unit,
    getPrerequisites: (StudyModule) -> List<StudyModule>,
    modifier: Modifier = Modifier
) {
    if (modules.isEmpty()) {
        EmptySearchResult(
            query = query,
            modifier = modifier
        )
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            // 검색 결과 개수 헤더
            Text(
                text = "검색 결과 ${modules.size}개",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // 모듈 카드 목록
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = modules,
                    key = { it.id }
                ) { module ->
                    ModuleCard(
                        module = module,
                        isExpanded = module.id in expandedModules,
                        isCompleted = module.id in completedModules,
                        prerequisites = getPrerequisites(module),
                        onToggle = { onModuleToggle(module.id) },
                        onLaunch = { onModuleLaunch(module) },
                        onCompleteToggle = { onModuleCompleteToggle(module.id) }
                    )
                }
            }
        }
    }
}

/**
 * 검색 결과가 없을 때 표시하는 빈 상태 컴포넌트
 */
@Composable
private fun EmptySearchResult(
    query: String,
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
                imageVector = Icons.Outlined.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "검색 결과가 없습니다",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"$query\"에 대한 결과를 찾을 수 없습니다.\n다른 검색어를 입력해 보세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 검색 힌트
            Text(
                text = "💡 Tip: 모듈 이름, 카테고리, 설명으로 검색할 수 있습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}
