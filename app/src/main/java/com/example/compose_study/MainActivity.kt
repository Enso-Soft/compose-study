package com.example.compose_study

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose_study.model.StudyModule
import com.example.compose_study.ui.LauncherScreen
import com.example.compose_study.ui.LauncherViewModel
import com.example.compose_study.ui.LauncherViewModelFactory
import com.example.compose_study.ui.theme.ComposestudyTheme

/**
 * 통합 런처 메인 Activity
 *
 * 모든 학습 모듈을 탐색하고 검색하여 실행할 수 있는 런처 앱입니다.
 * LauncherScreen과 LauncherViewModel을 사용하여 UI와 상태를 관리합니다.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposestudyTheme {
                LauncherApp(
                    onModuleLaunch = { module ->
                        launchModule(module)
                    }
                )
            }
        }
    }

    /**
     * 모듈 Activity 실행
     *
     * 등록된 activityClass를 사용하여 해당 모듈의 MainActivity를 시작합니다.
     */
    private fun launchModule(module: StudyModule) {
        try {
            val intent = Intent(this, module.activityClass)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO: 에러 처리 (Snackbar 등)
        }
    }
}

/**
 * 런처 앱 Root Composable
 *
 * ViewModel을 생성하고 LauncherScreen에 연결합니다.
 *
 * @param onModuleLaunch 모듈 실행 콜백 (Activity 시작)
 */
@Composable
fun LauncherApp(
    onModuleLaunch: (StudyModule) -> Unit
) {
    val viewModel: LauncherViewModel = viewModel(
        factory = LauncherViewModelFactory(
            context = androidx.compose.ui.platform.LocalContext.current.applicationContext
        )
    )
    val uiState by viewModel.uiState.collectAsState()

        LauncherScreen(
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onSearchActiveChange = { active ->
                if (active) {
                    viewModel.onSearchActivate()
                } else {
                    viewModel.onSearchSubmit()
                    viewModel.onSearchDeactivate()
                }
            },
            onSearchSubmit = viewModel::onSearchSubmit,
            onRecentSearchClick = viewModel::onRecentSearchClick,
            onRecentSearchRemove = viewModel::onRecentSearchRemove,
            onClearRecentSearches = viewModel::onClearRecentSearches,
        onModuleClick = { module ->
            viewModel.onModuleLaunch(module)
            onModuleLaunch(module)
        },
        onModuleToggle = viewModel::onModuleToggle,
        onModuleCompleteToggle = viewModel::onModuleCompleteToggle,
        onLevelToggle = viewModel::onLevelToggle,
        getPrerequisites = { module ->
            viewModel.uiState.value.allLevels
                .flatMap { it.modules }
                .filter { it.id in module.prerequisites }
        }
    )
}
