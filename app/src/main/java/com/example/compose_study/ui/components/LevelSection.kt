package com.example.compose_study.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.compose_study.model.Level
import com.example.compose_study.model.StudyModule

/**
 * 레벨 섹션 컴포넌트
 *
 * 레벨 헤더를 클릭하면 해당 레벨의 모듈 목록이 펼쳐지거나 접힙니다.
 * 각 모듈은 ModuleCard로 표시됩니다.
 *
 * @param level 표시할 레벨 정보 (모듈 목록 포함)
 * @param isExpanded 레벨이 펼쳐져 있는지 여부
 * @param expandedModules 펼쳐진 모듈 ID Set
 * @param completedModules 완료된 모듈 ID Set
 * @param onToggle 레벨 펼침/접힘 토글 콜백
 * @param onModuleToggle 모듈 펼침/접힘 토글 콜백
 * @param onModuleLaunch 모듈 학습 시작 콜백
 * @param onModuleCompleteToggle 모듈 완료 상태 토글 콜백
 * @param getPrerequisites 모듈의 선행 모듈 목록을 가져오는 함수
 * @param modifier Modifier
 */
@Composable
fun LevelSection(
    level: Level,
    isExpanded: Boolean,
    expandedModules: Set<String>,
    completedModules: Set<String>,
    onToggle: () -> Unit,
    onModuleToggle: (String) -> Unit,
    onModuleLaunch: (StudyModule) -> Unit,
    onModuleCompleteToggle: (String) -> Unit,
    getPrerequisites: (StudyModule) -> List<StudyModule>,
    modifier: Modifier = Modifier
) {
    // 이 레벨의 완료된 모듈 수 계산
    val completedCount = level.modules.count { it.id in completedModules }
    // 화살표 회전 애니메이션
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "arrow_rotation"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        // 레벨 헤더
        LevelHeader(
            level = level,
            completedCount = completedCount,
            isExpanded = isExpanded,
            arrowRotation = arrowRotation,
            onClick = onToggle,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // 모듈 목록 (AnimatedVisibility)
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(
                animationSpec = tween(durationMillis = 300)
            ),
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                level.modules.forEach { module ->
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

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * 레벨 헤더 컴포넌트
 *
 * 레벨 정보와 펼침/접힘 화살표, 완료 진행률을 표시합니다.
 */
@Composable
private fun LevelHeader(
    level: Level,
    completedCount: Int,
    isExpanded: Boolean,
    arrowRotation: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCount = level.modules.size
    val progressText = "$completedCount/$totalCount"
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        color = if (isExpanded) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        shape = MaterialTheme.shapes.large,
        tonalElevation = 0.dp,
        shadowElevation = if (isExpanded) 4.dp else 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // 레벨 헤더 텍스트
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = level.headerWithCount,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isExpanded) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )

                    // 완료 진행률 표시
                    if (completedCount > 0) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = if (completedCount == totalCount) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                            }
                        ) {
                            Text(
                                text = progressText,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                // 레벨 설명 (접혀있을 때만)
                if (!isExpanded && level.description.isNotBlank()) {
                    Text(
                        text = level.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 펼침/접힘 화살표
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "접기" else "펼치기",
                modifier = Modifier.rotate(arrowRotation),
                tint = if (isExpanded) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}
