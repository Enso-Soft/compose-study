package com.example.navigation_3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem Screen: Navigation 2의 한계점 시연
 *
 * Navigation 2 (기존 방식)의 문제점:
 * 1. 문자열 기반 라우팅으로 인한 타입 불안전
 * 2. NavController가 블랙박스처럼 동작
 * 3. 백스택 상태를 직접 제어하기 어려움
 * 4. 컴파일 타임에 오류를 잡지 못함
 */
@Composable
fun ProblemScreen() {
    // 참조 객체로 카운트 (State가 아니므로 Recomposition 트리거 안 함)
    val recompositionRef = remember { object { var count = 0 } }

    // 화면에 표시할 State는 별도로 관리
    var displayCount by remember { mutableIntStateOf(0) }

    // Recomposition 횟수 추적 (State 변경 없이)
    SideEffect {
        recompositionRef.count++
    }

    // 초기 1회만 displayCount 동기화
    LaunchedEffect(Unit) {
        displayCount = recompositionRef.count
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Navigation 2의 문제점",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Text(
            text = "Recomposition 횟수: $displayCount",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 문제 1: 문자열 기반 라우팅
        ProblemCard(
            title = "문제 1: 문자열 기반 라우팅",
            code = """
// Navigation 2 - 문자열 라우트 (오타 가능!)
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen() }
    composable("product/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        ProductScreen(id ?: "")
    }
}

// 런타임에서야 발견되는 오타!
navController.navigate("prodcut/123") // 오타: product -> prodcut
// 컴파일은 성공하지만 런타임 크래시!
            """.trimIndent(),
            problems = listOf(
                "라우트 이름에 오타가 있어도 컴파일 성공",
                "런타임에서야 오류 발견 (앱 크래시)",
                "인자 타입도 모두 String으로 처리해야 함",
                "IDE 자동완성 지원 제한적"
            )
        )

        // 문제 2: 불투명한 백스택 관리
        ProblemCard(
            title = "문제 2: 불투명한 백스택 관리",
            code = """
// Navigation 2 - NavController가 상태를 숨김
val navController = rememberNavController()

// 현재 백스택을 직접 확인하기 어려움
// navController.backQueue는 internal!

// 백스택 상태와 UI 상태가 불일치할 수 있음
navController.navigate("settings")
// 이 시점에서 실제로 어떤 화면들이 스택에 있는지?
            """.trimIndent(),
            problems = listOf(
                "NavController 내부 상태가 블랙박스",
                "백스택을 직접 관찰하기 어려움",
                "두 개의 진실의 원천 발생 가능",
                "디버깅이 복잡함"
            )
        )

        // 문제 3: 인자 전달의 번거로움
        ProblemCard(
            title = "문제 3: 인자 전달의 번거로움",
            code = """
// Navigation 2 - 복잡한 인자 전달
composable(
    route = "product/{productId}?name={name}&price={price}",
    arguments = listOf(
        navArgument("productId") { type = NavType.IntType },
        navArgument("name") {
            type = NavType.StringType
            nullable = true
        },
        navArgument("price") {
            type = NavType.FloatType
            defaultValue = 0f
        }
    )
) { backStackEntry ->
    // 모든 인자를 수동으로 추출해야 함
    val productId = backStackEntry.arguments?.getInt("productId") ?: 0
    val name = backStackEntry.arguments?.getString("name") ?: ""
    val price = backStackEntry.arguments?.getFloat("price") ?: 0f
}

// 네비게이션 시에도 문자열 조합 필요
navController.navigate("product/123?name=Kotlin&price=29.99")
            """.trimIndent(),
            problems = listOf(
                "라우트 패턴 문법 학습 필요",
                "타입 캐스팅 수동 처리",
                "nullable, defaultValue 등 보일러플레이트",
                "복잡한 객체 전달 어려움"
            )
        )

        // 문제 4: ViewModel 스코핑 제한
        ProblemCard(
            title = "문제 4: ViewModel 스코핑 제한",
            code = """
// Navigation 2 - ViewModel이 Navigation Graph 전체에 스코핑
NavHost(navController, startDestination = "home") {
    composable("productList") {
        val viewModel: ProductListViewModel = hiltViewModel()
        // 이 화면이 백스택에서 제거되어도
        // ViewModel이 계속 살아있을 수 있음
    }
    composable("productDetail/{id}") {
        // 화면별로 세밀하게 ViewModel 스코핑하기 어려움
    }
}
            """.trimIndent(),
            problems = listOf(
                "ViewModel이 화면별로 스코핑되지 않음",
                "메모리 낭비 가능성",
                "화면 제거 후에도 상태 유지되는 버그 발생 가능",
                "shared ViewModel 패턴 복잡"
            )
        )

        // 문제 5: 단일 화면 제약
        ProblemCard(
            title = "문제 5: 단일 화면 제약",
            code = """
// Navigation 2 - NavHost는 한 번에 하나의 화면만 표시
NavHost(navController, startDestination = "list") {
    composable("list") { ListScreen() }
    composable("detail/{id}") { DetailScreen() }
}

// 태블릿에서 List-Detail 레이아웃을 구현하려면?
// 별도의 복잡한 로직이 필요!
// - WindowSizeClass 확인
// - 조건부 네비게이션 로직
// - 상태 동기화 문제
            """.trimIndent(),
            problems = listOf(
                "NavHost는 하나의 목적지만 표시",
                "태블릿/폴더블 대응이 복잡",
                "List-Detail 레이아웃 구현 해킹 필요",
                "Scenes API 없음"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "결론: Navigation 2의 근본적 한계",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Navigation 2는 원래 XML View 시스템을 위해 2018년에 설계되었습니다.
Compose 어댑터가 추가되었지만, 근본적으로 "사각형 구멍에 원형 막대를 끼우는" 느낌이었습니다.

Nav3는 이런 문제들을 해결하기 위해 Compose를 위해 처음부터 새로 설계되었습니다.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProblemCard(
    title: String,
    code: String,
    problems: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = code,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "발생하는 문제점:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(4.dp))

            problems.forEach { problem ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "X",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = problem,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
