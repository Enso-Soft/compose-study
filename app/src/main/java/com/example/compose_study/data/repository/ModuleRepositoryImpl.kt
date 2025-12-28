package com.example.compose_study.data.repository

import com.example.compose_study.data.ModuleRegistry
import com.example.compose_study.model.Category
import com.example.compose_study.model.Level
import com.example.compose_study.model.StudyModule

/**
 * ModuleRepository의 구현체
 *
 * ModuleRegistry에 위임하여 모듈 데이터를 제공합니다.
 * Repository 패턴을 통해 데이터 소스 변경 시 이 클래스만 수정하면 됩니다.
 */
class ModuleRepositoryImpl : ModuleRepository {

    override fun getAllModules(): List<StudyModule> {
        return ModuleRegistry.modules
    }

    override fun getModulesByLevel(level: Int): List<StudyModule> {
        return ModuleRegistry.getByLevel(level)
    }

    override fun getModulesByCategory(category: Category): List<StudyModule> {
        return ModuleRegistry.getByCategory(category)
    }

    override fun searchModules(query: String): List<StudyModule> {
        return ModuleRegistry.search(query)
    }

    override fun getModuleById(id: String): StudyModule? {
        return ModuleRegistry.getById(id)
    }

    override fun getPrerequisites(module: StudyModule): List<StudyModule> {
        return ModuleRegistry.getPrerequisites(module)
    }

    override fun getLevels(): List<Level> {
        return Level.fromModules(ModuleRegistry.modules)
    }

    override val moduleCount: Int
        get() = ModuleRegistry.moduleCount

    override val moduleCountByLevel: Map<Int, Int>
        get() = ModuleRegistry.moduleCountByLevel

    override val moduleCountByCategory: Map<Category, Int>
        get() = ModuleRegistry.moduleCountByCategory
}
