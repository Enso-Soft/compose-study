package com.example.material_expressive.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/**
 * Material 3 Expressive 테마
 *
 * 기존 MaterialTheme 대신 MaterialExpressiveTheme을 사용하여
 * 스프링 물리 기반의 생동감 있는 애니메이션을 제공합니다.
 *
 * @param darkTheme 다크 모드 여부
 * @param dynamicColor Android 12+ 다이나믹 컬러 사용 여부
 * @param content 테마가 적용될 콘텐츠
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MaterialExpressiveStudyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // MaterialExpressiveTheme 사용 - 스프링 물리 기반 모션 자동 적용
    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(), // 핵심! 스프링 물리 모션
        typography = Typography,
        content = content
    )
}

/**
 * 기존 Material 3 테마 (비교용)
 *
 * Problem.kt에서 사용되는 기존 방식의 테마입니다.
 * MaterialExpressiveTheme과 비교하여 차이점을 확인하세요.
 */
@Composable
fun StandardMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 기존 MaterialTheme - expressive 모션 없음
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
