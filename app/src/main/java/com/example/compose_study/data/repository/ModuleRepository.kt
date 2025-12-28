package com.example.compose_study.data.repository

import com.example.compose_study.model.Category
import com.example.compose_study.model.Level
import com.example.compose_study.model.StudyModule

/**
 * 학습 모듈 데이터에 접근하는 Repository 인터페이스
 *
 * ModuleRegistry를 추상화하여 ViewModel에서 인터페이스에만 의존하도록 합니다.
 * 이를 통해 테스트 시 Mock 구현체를 주입할 수 있습니다.
 */
interface ModuleRepository {

    /**
     * 모든 학습 모듈 조회
     */
    fun getAllModules(): List<StudyModule>

    /**
     * 특정 레벨의 모듈만 조회
     * @param level 레벨 번호 (1-5)
     */
    fun getModulesByLevel(level: Int): List<StudyModule>

    /**
     * 특정 카테고리의 모듈만 조회
     * @param category 카테고리
     */
    fun getModulesByCategory(category: Category): List<StudyModule>

    /**
     * 검색어로 모듈 검색
     * @param query 검색어 (id, name, description, category 등에서 검색)
     */
    fun searchModules(query: String): List<StudyModule>

    /**
     * ID로 모듈 조회
     * @param id 모듈 ID
     * @return 모듈, 없으면 null
     */
    fun getModuleById(id: String): StudyModule?

    /**
     * 모듈의 선행 학습 모듈 목록 조회
     * @param module 대상 모듈
     */
    fun getPrerequisites(module: StudyModule): List<StudyModule>

    /**
     * 모든 레벨 정보 조회 (모듈 포함)
     */
    fun getLevels(): List<Level>

    /**
     * 전체 모듈 개수
     */
    val moduleCount: Int

    /**
     * 레벨별 모듈 개수
     */
    val moduleCountByLevel: Map<Int, Int>

    /**
     * 카테고리별 모듈 개수
     */
    val moduleCountByCategory: Map<Category, Int>
}
