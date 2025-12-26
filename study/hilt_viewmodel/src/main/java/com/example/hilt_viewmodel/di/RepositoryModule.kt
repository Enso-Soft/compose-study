package com.example.hilt_viewmodel.di

import com.example.hilt_viewmodel.TodoRepository
import com.example.hilt_viewmodel.TodoRepositoryImpl
import com.example.hilt_viewmodel.UserRepository
import com.example.hilt_viewmodel.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module: 의존성 제공 방법 정의
 *
 * @Module: 이 클래스가 Hilt 모듈임을 선언
 * @InstallIn: 어떤 컴포넌트에 이 모듈을 설치할지 지정
 *
 * 주요 컴포넌트:
 * - SingletonComponent: 앱 전체 수명 (Application 수명)
 * - ActivityRetainedComponent: Activity 수명 (Configuration Change 생존)
 * - ViewModelComponent: ViewModel 수명
 * - ActivityComponent: Activity 인스턴스 수명
 * - FragmentComponent: Fragment 수명
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * @Binds: 인터페이스와 구현체를 연결
     * - abstract 함수로 선언
     * - 파라미터: 구현체 (Hilt가 자동 생성)
     * - 반환 타입: 인터페이스
     *
     * @Singleton: 앱 전체에서 하나의 인스턴스만 생성
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository
}

/**
 * 참고: @Provides를 사용하는 경우
 *
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object NetworkModule {
 *
 *     // @Provides: 인스턴스를 직접 생성하여 제공
 *     // - object 클래스 안에 일반 함수로 선언
 *     // - 외부 라이브러리 클래스(Retrofit 등)에 사용
 *     @Provides
 *     @Singleton
 *     fun provideRetrofit(): Retrofit {
 *         return Retrofit.Builder()
 *             .baseUrl("https://api.example.com")
 *             .addConverterFactory(GsonConverterFactory.create())
 *             .build()
 *     }
 *
 *     @Provides
 *     @Singleton
 *     fun provideApiService(retrofit: Retrofit): ApiService {
 *         return retrofit.create(ApiService::class.java)
 *     }
 * }
 */
