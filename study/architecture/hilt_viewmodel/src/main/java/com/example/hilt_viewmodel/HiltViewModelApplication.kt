package com.example.hilt_viewmodel

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt를 사용하는 앱의 Application 클래스
 *
 * @HiltAndroidApp 어노테이션:
 * - Hilt의 코드 생성을 트리거하는 필수 어노테이션
 * - Application 클래스에 반드시 추가해야 함
 * - 앱 수준의 의존성 컨테이너 역할
 */
@HiltAndroidApp
class HiltViewModelApplication : Application()
