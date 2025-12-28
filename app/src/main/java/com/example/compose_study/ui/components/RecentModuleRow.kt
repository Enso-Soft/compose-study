package com.example.compose_study.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.compose_study.model.StudyModule

/**
 * 최근 학습 모듈을 가로 스크롤로 표시하는 컴포넌트
 *
 * @param modules 최근 학습한 모듈 목록
 * @param onModuleClick 모듈 클릭 콜백
 * @param modifier Modifier
 */
@Composable
fun RecentModuleRow(
    modules: List<StudyModule>,
    onModuleClick: (StudyModule) -> Unit,
    modifier: Modifier = Modifier
) {
    if (modules.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 헤더
        Text(
            text = "📖 최근 학습",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 가로 스크롤 모듈 카드
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = modules,
                key = { it.id }
            ) { module ->
                RecentModuleCard(
                    module = module,
                    onClick = { onModuleClick(module) }
                )
            }
        }
    }
}

/**
 * 최근 학습 모듈을 표시하는 간단한 카드
 */
@Composable
private fun RecentModuleCard(
    module: StudyModule,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(140.dp)
            .semantics {
                contentDescription = "${module.name} 모듈, ${module.category.displayName} 카테고리. 클릭하여 학습 시작"
            }
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 카테고리 이모지
            Text(
                text = module.category.emoji,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 모듈 이름
            Text(
                text = module.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 카테고리 이름
            Text(
                text = module.category.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
