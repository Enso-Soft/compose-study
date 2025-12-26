package com.example.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황: @Preview 없이 개발할 때의 불편함
 *
 * 이 화면에서는 Preview 없이 개발할 때 발생하는 문제점을 보여줍니다:
 * 1. UI를 확인하려면 매번 앱을 빌드/실행해야 함
 * 2. 다양한 상태를 테스트하려면 코드가 중복됨
 * 3. 다크모드/다양한 화면 크기 테스트가 번거로움
 */
@Composable
fun ProblemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제 상황",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "@Preview 없이 개발하면 어떤 불편함이 있는지 확인해보세요.",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        HorizontalDivider()

        // 문제 1: 반복적인 빌드/실행
        ProblemCard1()

        HorizontalDivider()

        // 문제 2: 코드 중복
        ProblemCard2()

        HorizontalDivider()

        // 문제 3: 다양한 환경 테스트
        ProblemCard3()

        HorizontalDivider()

        // 실제 컴포넌트 예시
        Text(
            text = "테스트할 컴포넌트",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        UserCardExample()
    }
}

@Composable
private fun ProblemCard1() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 1: 반복적인 빌드/실행",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "UI를 확인하는 전통적인 방법:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            val steps = listOf(
                "1. 코드 수정",
                "2. 앱 빌드 (1-2분 소요)",
                "3. 에뮬레이터/기기에서 실행",
                "4. 해당 화면까지 네비게이션",
                "5. 결과 확인",
                "6. 문제 발견 시 1번으로..."
            )

            steps.forEach { step ->
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF5D4037)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "→ 사소한 UI 수정에도 많은 시간 소요!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C)
            )
        }
    }
}

@Composable
private fun ProblemCard2() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 2: 코드 중복",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "여러 상태를 테스트하려면 Preview 함수 반복:",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFEFEF)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = """
@Preview
fun UserPreview1() = UserCard(User("Alice", 25))

@Preview
fun UserPreview2() = UserCard(User("Bob", 30))

@Preview
fun UserPreview3() = UserCard(User("Charlie", 35))

// 10개 상태 = 10개 함수...
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "→ 유지보수 어려움, 코드 양 증가!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
            )
        }
    }
}

@Composable
private fun ProblemCard3() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "문제 3: 다양한 환경 테스트",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Light/Dark × Phone/Tablet = 4개 조합",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFEFEF)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = """
@Preview fun LightPhone() { ... }
@Preview fun DarkPhone() { ... }
@Preview fun LightTablet() { ... }
@Preview fun DarkTablet() { ... }

// 폰트 크기까지 추가하면?
// 4 × 3 = 12개 함수...
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "→ 조합이 늘어날수록 관리 불가능!",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
        }
    }
}

/**
 * 테스트용 컴포넌트 예시
 */
@Composable
fun UserCardExample() {
    val sampleUser = SampleUser("홍길동", 28, "hong@email.com")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "이 컴포넌트를 Preview 없이 확인하려면?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 실제 UserCard
            SampleUserCard(user = sampleUser)

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "→ 앱을 빌드하고 실행해야 합니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 샘플 데이터 클래스
 */
data class SampleUser(
    val name: String,
    val age: Int,
    val email: String
)

/**
 * 샘플 UserCard 컴포넌트
 */
@Composable
fun SampleUserCard(user: SampleUser) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${user.age}세",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}
