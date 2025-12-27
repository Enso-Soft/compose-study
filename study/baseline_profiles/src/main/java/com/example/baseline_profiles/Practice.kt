package com.example.baseline_profiles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 연습 문제 1: 프로파일 규칙 이해 및 작성
 *
 * baseline-prof.txt 형식을 이해하고 규칙을 작성하는 연습
 */
@Composable
fun Practice1_ProfileRules() {
    var showHint by remember { mutableStateOf(false) }
    var showAnswer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 프로파일 규칙 작성",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "baseline-prof.txt 형식을 이해하고 규칙을 작성해보세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 시나리오 설명
        PracticeCard(
            title = "시나리오: 쇼핑 앱",
            content = """
앱 구조:
- MainActivity: 앱 진입점
- HomeScreen: 메인 화면 (상품 목록)
- ProductListScreen: 카테고리별 상품 (LazyColumn)
- ProductDetailScreen: 상품 상세
- CartScreen: 장바구니
- CheckoutScreen: 결제

패키지: com.example.shop
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 플래그 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "프로파일 플래그 의미",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProfileFlagExplanation("H", "Hot", "자주 호출되는 메서드")
                ProfileFlagExplanation("S", "Startup", "앱 시작 시 필요")
                ProfileFlagExplanation("P", "Post-startup", "시작 직후 필요")
                ProfileFlagExplanation("L", "Class", "클래스 타입 지시자")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제
        QuestionCard(
            question = "다음 질문에 답하세요:",
            items = listOf(
                "1. MainActivity와 HomeScreen을 Startup에 포함하려면?",
                "2. ProductListScreen을 Hot + Post-startup으로 추가하려면?",
                "3. 모든 ViewModel 클래스를 와일드카드로 포함하려면?",
                "4. onCreate 메서드를 Startup으로 포함하려면?"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트/정답 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { showHint = !showHint },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (showHint) "힌트 숨기기" else "힌트 보기")
            }
            Button(
                onClick = { showAnswer = !showAnswer },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (showAnswer) "정답 숨기기" else "정답 보기")
            }
        }

        // 힌트
        AnimatedVisibility(visible = showHint) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("힌트:", fontWeight = FontWeight.Bold)
                    Text("- 클래스: L + 패키지/클래스명 + ;")
                    Text("- 메서드: 플래그 + L + 패키지/클래스명 + ; + -> + 메서드시그니처")
                    Text("- 와일드카드: ** 사용")
                }
            }
        }

        // 정답
        AnimatedVisibility(visible = showAnswer) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("정답:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    CodeBlock(
                        code = """
# 1. Startup 클래스
HSLcom/example/shop/MainActivity;
HSLcom/example/shop/ui/HomeScreen;

# 2. Hot + Post-startup
HPLcom/example/shop/ui/ProductListScreen;

# 3. 와일드카드 ViewModel
Lcom/example/shop/viewmodel/**;

# 4. onCreate 메서드 (Startup)
HSPLcom/example/shop/MainActivity;->onCreate(Landroid/os/Bundle;)V
                        """.trimIndent()
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 2: BaselineProfileGenerator 작성
 *
 * Macrobenchmark를 사용한 프로필 생성 테스트 작성
 */
@Composable
fun Practice2_ProfileGenerator() {
    var showHint by remember { mutableStateOf(false) }
    var showAnswer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: BaselineProfileGenerator 작성",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "뉴스 앱의 Critical User Journey를 정의하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 시나리오 설명
        PracticeCard(
            title = "시나리오: 뉴스 앱",
            content = """
Critical User Journey:
1. 앱 시작 -> 뉴스 피드 표시
2. 첫 번째 뉴스 아이템 클릭 -> 상세 페이지
3. 뒤로 가기 -> 피드로 돌아오기
4. 스크롤하여 더 많은 뉴스 확인

UI 요소:
- 뉴스 아이템 ID: "news_item_0", "news_item_1", ...
- 뉴스 피드 ID: "news_feed"
- 패키지명: com.example.news
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 문제
        QuestionCard(
            question = "BaselineProfileGenerator 테스트를 완성하세요:",
            items = listOf(
                "1. 앱 시작 코드 작성",
                "2. 첫 번째 뉴스 아이템 클릭",
                "3. 뒤로 가기 후 스크롤",
                "4. includeInStartupProfile 설정"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 스켈레톤 코드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("완성해야 할 코드:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                CodeBlock(
                    code = """
@RunWith(AndroidJUnit4::class)
class NewsAppProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateProfile() {
        rule.collect(
            packageName = "com.example.news",
            // TODO: Startup Profile에 포함
        ) {
            // TODO: 1. 앱 시작


            // TODO: 2. 첫 번째 뉴스 클릭


            // TODO: 3. 뒤로 가기 후 스크롤

        }
    }
}
                    """.trimIndent()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트/정답 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { showHint = !showHint },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (showHint) "힌트 숨기기" else "힌트 보기")
            }
            Button(
                onClick = { showAnswer = !showAnswer },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (showAnswer) "정답 숨기기" else "정답 보기")
            }
        }

        // 힌트
        AnimatedVisibility(visible = showHint) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("힌트:", fontWeight = FontWeight.Bold)
                    Text("- startActivityAndWait(): 앱 시작")
                    Text("- device.findObject(By.res(\"id\")): ID로 요소 찾기")
                    Text("- .click(): 클릭")
                    Text("- device.pressBack(): 뒤로 가기")
                    Text("- .scroll(Direction.DOWN, 2f): 스크롤")
                    Text("- device.waitForIdle(): 안정화 대기")
                }
            }
        }

        // 정답
        AnimatedVisibility(visible = showAnswer) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("정답:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    CodeBlock(
                        code = """
@RunWith(AndroidJUnit4::class)
class NewsAppProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateProfile() {
        rule.collect(
            packageName = "com.example.news",
            includeInStartupProfile = true  // Startup Profile 포함
        ) {
            // 1. 앱 시작
            startActivityAndWait()

            // 2. 첫 번째 뉴스 클릭
            device.findObject(By.res("news_item_0")).click()
            device.waitForIdle()

            // 3. 뒤로 가기 후 스크롤
            device.pressBack()
            device.waitForIdle()

            device.findObject(By.res("news_feed"))
                .scroll(Direction.DOWN, 2f)
            device.waitForIdle()
        }
    }
}
                        """.trimIndent()
                    )
                }
            }
        }
    }
}

/**
 * 연습 문제 3: Compose ReportDrawn API 활용
 *
 * 비동기 데이터 로드 완료 시점을 정확히 보고하기
 */
@Composable
fun Practice3_ReportDrawn() {
    var showHint by remember { mutableStateOf(false) }
    var showAnswer by remember { mutableStateOf(false) }

    // 데모용 상태
    var isLoading by remember { mutableStateOf(true) }
    var articles by remember { mutableStateOf<List<String>>(emptyList()) }

    // 데이터 로드 시뮬레이션
    LaunchedEffect(Unit) {
        delay(2000)
        articles = listOf("Article 1", "Article 2", "Article 3")
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: ReportDrawn API 활용",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "비동기 데이터 로드 완료 시점을 정확히 보고하세요",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 데모 영역
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("데모: 비동기 데이터 로드", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                    Text("데이터 로딩 중...")
                    Text(
                        text = "(ReportDrawn 호출 전)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    articles.forEach { article ->
                        Text("- $article")
                    }
                    Text(
                        text = "(ReportDrawn 호출됨!)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4CAF50)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        isLoading = true
                        articles = emptyList()
                    }
                ) {
                    Text("다시 로드")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 문제
        QuestionCard(
            question = "다음 코드를 완성하세요:",
            items = listOf(
                "1. ReportDrawnWhen을 사용하여 데이터 로드 완료 시점 보고",
                "2. ReportDrawnAfter를 사용하여 suspend 함수 완료 후 보고"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 스켈레톤 코드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("완성해야 할 코드 1:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                CodeBlock(
                    code = """
@Composable
fun ArticleListScreen(
    loadArticles: suspend () -> List<Article>
) {
    var articles by remember {
        mutableStateOf<List<Article>>(emptyList())
    }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        articles = loadArticles()
        isLoading = false
    }

    // TODO: 데이터 로드 완료 시점에 ReportDrawn 호출
    // 힌트: ReportDrawnWhen { 조건 }


    if (isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(articles) { article ->
                ArticleCard(article)
            }
        }
    }
}
                    """.trimIndent()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("완성해야 할 코드 2:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                CodeBlock(
                    code = """
@Composable
fun InitializationScreen(
    repository: Repository
) {
    // TODO: suspend 함수 완료 후 ReportDrawn 호출
    // 힌트: ReportDrawnAfter { suspendFunction() }


    // UI 렌더링
    ContentView()
}
                    """.trimIndent()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트/정답 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { showHint = !showHint },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (showHint) "힌트 숨기기" else "힌트 보기")
            }
            Button(
                onClick = { showAnswer = !showAnswer },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (showAnswer) "정답 숨기기" else "정답 보기")
            }
        }

        // 힌트
        AnimatedVisibility(visible = showHint) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("힌트:", fontWeight = FontWeight.Bold)
                    Text("- ReportDrawnWhen { 조건 }: 조건이 true일 때 완료 보고")
                    Text("- ReportDrawnAfter { suspend 함수 }: 함수 완료 후 보고")
                    Text("- 로딩 완료 조건: !isLoading && articles.isNotEmpty()")
                }
            }
        }

        // 정답
        AnimatedVisibility(visible = showAnswer) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("정답 1:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    CodeBlock(
                        code = """
// 데이터 로드 완료 시점에 ReportDrawn 호출
ReportDrawnWhen { !isLoading && articles.isNotEmpty() }
                        """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("정답 2:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    CodeBlock(
                        code = """
// suspend 함수 완료 후 ReportDrawn 호출
ReportDrawnAfter {
    repository.loadInitialData()
}
                        """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ReportDrawn을 사용하면 Baseline Profile Generator가 " +
                                "앱의 완전 로드 시점(TTFD)을 정확히 인식하여 " +
                                "더 효과적인 프로필을 생성할 수 있습니다.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Helper Composables

@Composable
private fun PracticeCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}

@Composable
private fun QuestionCard(
    question: String,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            items.forEach { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ProfileFlagExplanation(
    flag: String,
    name: String,
    description: String
) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = flag,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$name - $description",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun CodeBlock(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        )
    }
}
