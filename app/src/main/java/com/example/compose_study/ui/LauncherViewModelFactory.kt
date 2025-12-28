package com.example.compose_study.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.compose_study.data.repository.ModuleRepositoryImpl
import com.example.compose_study.data.repository.RecentRepositoryImpl
import com.example.compose_study.data.repository.recentDataStore

/**
 * LauncherViewModel을 생성하기 위한 Factory
 *
 * Hilt를 사용하지 않는 경우 수동으로 의존성을 주입합니다.
 *
 * @param context Application 또는 Activity Context
 */
class LauncherViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LauncherViewModel::class.java)) {
            val moduleRepository = ModuleRepositoryImpl()
            val recentRepository = RecentRepositoryImpl(context.recentDataStore)
            return LauncherViewModel(moduleRepository, recentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
