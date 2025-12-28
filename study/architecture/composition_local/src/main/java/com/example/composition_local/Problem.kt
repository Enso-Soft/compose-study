package com.example.composition_local

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 문제가 있는 코드 - CompositionLocal 없이 매개변수 드릴링
 *
 * 이 코드의 문제점:
 * 1. 모든 중간 컴포넌트가 settings를 전달해야 함 (Parameter Drilling)
 * 2. 새로운 설정 추가 시 모든 계층 수정 필요
 * 3. 중간 컴포넌트가 불필요한 의존성 가짐
 * 4. 코드 중복과 가독성 저하
 */

/**
 * 앱 설정 데이터 클래스
 */
data class AppSettingsProblem(
    val primaryColor: Color = Color(0xFF6200EE),
    val secondaryColor: Color = Color(0xFF03DAC5),
    val spacing: Dp = 8.dp,
    val fontSize: Int = 16
)

@Composable
fun ProblemScreen() {
    val scrollState = rememberScrollState()

    // 앱 설정
    val settings = AppSettingsProblem(
        primaryColor = Color(0xFF6200EE),
        secondaryColor = Color(0xFF03DAC5),
        spacing = 12.dp,
        fontSize = 18
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Problem: 매개변수 드릴링",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제점",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 모든 중간 컴포넌트가 settings를 전달해야 함")
                Text("2. 새 설정 추가 시 모든 계층 수정 필요")
                Text("3. 중간 컴포넌트가 불필요한 의존성 가짐")
                Text("4. 코드 중복과 가독성 저하")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 컴포넌트 계층 구조 표시
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "컴포넌트 계층 구조",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("App (settings 생성)")
                Text("  ↓ settings 전달")
                Text("MainScreen (settings 전달만)")
                Text("  ↓ settings 전달")
                Text("ContentSection (settings 전달만)")
                Text("  ↓ settings 전달")
                Text("ContentCard (settings 전달만)")
                Text("  ↓ settings 전달")
                Text("ThemedText (settings 실제 사용)")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 실제 문제 시연
        Text(
            text = "실제 코드 시연 (5단계 깊이)",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 매개변수 드릴링 시연
        Level1_MainScreen_Problem(settings = settings)
    }
}

/**
 * Level 1: 메인 화면
 * settings를 받아서 하위 컴포넌트에 전달만 함
 */
@Composable
fun Level1_MainScreen_Problem(settings: AppSettingsProblem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8E8E8)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 1: MainScreen",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = "settings를 받아서 전달만 함",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            // Level 2로 전달
            Level2_ContentSection_Problem(settings = settings)
        }
    }
}

/**
 * Level 2: 콘텐츠 섹션
 * settings를 받아서 하위 컴포넌트에 전달만 함
 */
@Composable
fun Level2_ContentSection_Problem(settings: AppSettingsProblem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0D0D0)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 2: ContentSection",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = "settings를 받아서 전달만 함",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            // Level 3으로 전달
            Level3_ContentCard_Problem(settings = settings)
        }
    }
}

/**
 * Level 3: 콘텐츠 카드
 * settings를 받아서 하위 컴포넌트에 전달만 함
 */
@Composable
fun Level3_ContentCard_Problem(settings: AppSettingsProblem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB8B8B8)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 3: ContentCard",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = "settings를 받아서 전달만 함",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            // Level 4로 전달
            Level4_ContentBody_Problem(settings = settings)
        }
    }
}

/**
 * Level 4: 콘텐츠 본문
 * settings를 받아서 하위 컴포넌트에 전달만 함
 */
@Composable
fun Level4_ContentBody_Problem(settings: AppSettingsProblem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFA0A0A0)
        )
    ) {
        Column(modifier = Modifier.padding(settings.spacing)) {
            Text(
                text = "Level 4: ContentBody",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = "settings를 받아서 전달만 함",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            // Level 5로 전달 - 여기서야 실제 사용!
            Level5_ThemedText_Problem(settings = settings)
        }
    }
}

/**
 * Level 5: 테마가 적용된 텍스트
 * 드디어 settings를 실제로 사용!
 */
@Composable
fun Level5_ThemedText_Problem(settings: AppSettingsProblem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = settings.primaryColor
        )
    ) {
        Column(
            modifier = Modifier.padding(settings.spacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Level 5: ThemedText",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = "드디어 settings를 실제로 사용!",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green
            )

            Spacer(modifier = Modifier.height(settings.spacing))

            Text(
                text = "이 텍스트가 settings를 사용합니다",
                fontSize = settings.fontSize.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .background(settings.secondaryColor)
                    .padding(8.dp)
            ) {
                Text(
                    text = "보조 색상 배경",
                    color = Color.Black
                )
            }
        }
    }
}
