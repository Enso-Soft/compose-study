package com.example.screen_and_component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.screen_and_component.ui.theme.ScreenAndComponentTheme

/**
 * Solution: Screen과 Component를 올바르게 분리하기
 *
 * 이 화면에서는 Screen + Content + Component 패턴을 사용하여
 * 재사용 가능하고, Preview 가능하고, 테스트 용이한 코드를 만드는 방법을 보여줍니다.
 */

@Composable
fun SolutionScreen() {
    var selectedDemo by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 데모 선택 탭
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedDemo == 0,
                onClick = { selectedDemo = 0 },
                label = { Text("분리 패턴") }
            )
            FilterChip(
                selected = selectedDemo == 1,
                onClick = { selectedDemo = 1 },
                label = { Text("Component") }
            )
            FilterChip(
                selected = selectedDemo == 2,
                onClick = { selectedDemo = 2 },
                label = { Text("전체 구조") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedDemo) {
            0 -> Solution1_ScreenContentPattern()
            1 -> Solution2_ReusableComponents()
            2 -> Solution3_FullStructure()
        }
    }
}

// ============================================================
// 솔루션 1: Screen + Content 분리 패턴
// ============================================================

/**
 * 솔루션 1: Screen + Content 분리 패턴
 */
@Composable
fun Solution1_ScreenContentPattern() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: Screen + Content 분리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Screen: ViewModel 연결, 상태 수집\n" +
                            "Content: 순수 UI, Preview 가능, 테스트 용이",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 실제 Content 예시 (Preview 가능!)
        DemoProfileContent(
            state = DemoProfileState(
                userName = "홍길동",
                userEmail = "hong@example.com",
                postCount = 89,
                followerCount = 1234,
                followingCount = 567,
                isEditing = false
            ),
            onEditClick = {},
            onLogoutClick = {},
            onBackClick = {}
        )

        // 코드 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Screen + Content 코드 구조",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// ===== Screen (Stateful) =====
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileContent(
        state = state,
        onEditClick = viewModel::onEditClick,
        onLogoutClick = viewModel::onLogout,
        onBackClick = onNavigateBack
    )
}

// ===== Content (Stateless) =====
@Composable
fun ProfileContent(
    state: ProfileState,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 순수 UI 구현
}

// ===== Preview 가능! =====
@Preview
@Composable
fun ProfileContentPreview() {
    ProfileContent(
        state = ProfileState(userName = "홍길동"),
        onEditClick = {},
        onLogoutClick = {},
        onBackClick = {}
    )
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 장점
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "이 패턴의 장점",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Preview 가능: mock 데이터로 즉시 확인\n" +
                            "2. 테스트 용이: 입력/출력만으로 검증\n" +
                            "3. 재사용 가능: 같은 Content, 다른 데이터\n" +
                            "4. 관심사 분리: Screen=상태, Content=UI",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ============================================================
// 솔루션 2: 재사용 가능한 Component
// ============================================================

/**
 * 솔루션 2: 재사용 가능한 Component
 */
@Composable
fun Solution2_ReusableComponents() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 핵심 포인트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "핵심 포인트: 재사용 가능한 Component",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "반복되는 UI 요소를 Component로 추출하면\n" +
                            "코드 중복을 줄이고 일관성을 유지할 수 있습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // StatsRow Component 예시
        Text(
            text = "StatsRow Component 재사용 예시",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("내 프로필", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                StatsRow(
                    stats = listOf(
                        StatItem("게시물", 89),
                        StatItem("팔로워", 1234),
                        StatItem("팔로잉", 567)
                    )
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("다른 사용자", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                StatsRow(
                    stats = listOf(
                        StatItem("게시물", 156),
                        StatItem("팔로워", 5678),
                        StatItem("팔로잉", 234)
                    )
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF3E0)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("상품 통계 (다른 용도)", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                StatsRow(
                    stats = listOf(
                        StatItem("판매", 42),
                        StatItem("리뷰", 128),
                        StatItem("찜", 89)
                    )
                )
            }
        }

        // 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "StatsRow Component 코드",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
data class StatItem(val label: String, val value: Int)

@Composable
fun StatsRow(
    stats: List<StatItem>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        stats.forEach { stat ->
            StatColumn(label = stat.label, value = stat.value)
        }
    }
}

@Composable
fun StatColumn(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatNumber(value),
            fontWeight = FontWeight.Bold
        )
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // ProfileAvatar Component 예시
        Text(
            text = "ProfileAvatar Component 재사용 예시",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ProfileAvatar(size = 48)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Small", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ProfileAvatar(size = 64)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Medium", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ProfileAvatar(size = 80)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Large", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// ============================================================
// 솔루션 3: 전체 구조
// ============================================================

/**
 * 솔루션 3: 전체 구조 - Screen + Content + Components
 */
@Composable
fun Solution3_FullStructure() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 구조 다이어그램
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "전체 구조 다이어그램",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
ProfileScreen (Stateful)
   |
   +-- viewModel.state.collectAsStateWithLifecycle()
   |
   +-- ProfileContent (Stateless)
          |
          +-- ProfileTopBar (Component)
          |
          +-- ProfileHeader
          |      +-- ProfileAvatar (Component)
          |      +-- userName, userEmail
          |
          +-- StatsRow (Component)
          |      +-- StatColumn x 3
          |
          +-- ActionButtons (Component)
                 +-- EditButton
                 +-- LogoutButton
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 실제 작동하는 예시
        Text(
            text = "작동하는 전체 예시",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        // 상태 시뮬레이션
        var demoState by remember {
            mutableStateOf(
                DemoProfileState(
                    userName = "홍길동",
                    userEmail = "hong@example.com",
                    postCount = 89,
                    followerCount = 1234,
                    followingCount = 567,
                    isEditing = false
                )
            )
        }

        FullProfileContent(
            state = demoState,
            onEditClick = {
                demoState = demoState.copy(isEditing = !demoState.isEditing)
            },
            onLogoutClick = { /* 로그아웃 처리 */ },
            onBackClick = { /* 뒤로가기 */ },
            onSettingsClick = { /* 설정 */ }
        )

        // 코드 구조
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "파일 구조 권장",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
feature/profile/
   |
   +-- ProfileScreen.kt      // Screen + Content
   |
   +-- ProfileState.kt       // State, Action 정의
   |
   +-- ProfileViewModel.kt   // ViewModel
   |
   +-- components/
          +-- ProfileAvatar.kt
          +-- ProfileTopBar.kt
          +-- StatsRow.kt
          +-- ActionButtons.kt

// 공통 Component는 ui/components/에 배치
ui/components/
   +-- Avatar.kt
   +-- StatsRow.kt
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // 베스트 프랙티스
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "베스트 프랙티스",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "1. Screen은 ViewModel과만 통신\n" +
                            "2. Content는 상태를 파라미터로 받음\n" +
                            "3. Component는 최소한의 파라미터만\n" +
                            "4. Modifier는 항상 첫 번째 선택적 파라미터\n" +
                            "5. Preview는 Content와 Component에 작성\n" +
                            "6. 재사용 가능한 Component는 별도 파일로",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ============================================================
// State 정의
// ============================================================

data class DemoProfileState(
    val userName: String = "",
    val userEmail: String = "",
    val postCount: Int = 0,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val isEditing: Boolean = false
)

data class StatItem(val label: String, val value: Int)

// ============================================================
// Content Composables
// ============================================================

/**
 * 데모용 ProfileContent (간단한 버전)
 */
@Composable
fun DemoProfileContent(
    state: DemoProfileState,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ProfileContent (Preview 가능!)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileAvatar(size = 80)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = state.userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = state.userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            StatsRow(
                stats = listOf(
                    StatItem("게시물", state.postCount),
                    StatItem("팔로워", state.followerCount),
                    StatItem("팔로잉", state.followingCount)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActionButtons(
                isEditing = state.isEditing,
                onEditClick = onEditClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

/**
 * 전체 구조를 보여주는 ProfileContent
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullProfileContent(
    state: DemoProfileState,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column {
            // TopAppBar Component
            TopAppBar(
                title = { Text("프로필") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "설정")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ProfileHeader
                ProfileAvatar(size = 100)
                Spacer(modifier = Modifier.height(12.dp))

                if (state.isEditing) {
                    OutlinedTextField(
                        value = state.userName,
                        onValueChange = { /* 편집 시 상태 변경 */ },
                        label = { Text("이름") },
                        singleLine = true
                    )
                } else {
                    Text(
                        text = state.userName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = state.userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // StatsRow Component
                StatsRow(
                    stats = listOf(
                        StatItem("게시물", state.postCount),
                        StatItem("팔로워", state.followerCount),
                        StatItem("팔로잉", state.followingCount)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ActionButtons Component
                ActionButtons(
                    isEditing = state.isEditing,
                    onEditClick = onEditClick,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

// ============================================================
// Reusable Components
// ============================================================

/**
 * 프로필 아바타 Component
 */
@Composable
fun ProfileAvatar(
    size: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .padding((size / 5).dp)
                .fillMaxSize(),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

/**
 * 통계 Row Component
 */
@Composable
fun StatsRow(
    stats: List<StatItem>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        stats.forEach { stat ->
            StatColumn(label = stat.label, value = stat.value)
        }
    }
}

/**
 * 통계 Column Component
 */
@Composable
fun StatColumn(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatNumber(value),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

/**
 * 액션 버튼 Component
 */
@Composable
fun ActionButtons(
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onEditClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (isEditing) "저장" else "편집")
        }
        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("로그아웃")
        }
    }
}

// ============================================================
// Utility Functions
// ============================================================

private fun formatNumber(number: Int): String {
    return when {
        number >= 10000 -> "${number / 10000}.${(number % 10000) / 1000}만"
        number >= 1000 -> "${number / 1000},${String.format("%03d", number % 1000)}"
        else -> number.toString()
    }
}

// ============================================================
// Previews (Content와 Component만 Preview 가능!)
// ============================================================

@Preview(showBackground = true)
@Composable
fun DemoProfileContentPreview() {
    ScreenAndComponentTheme {
        DemoProfileContent(
            state = DemoProfileState(
                userName = "홍길동",
                userEmail = "hong@example.com",
                postCount = 89,
                followerCount = 1234,
                followingCount = 567,
                isEditing = false
            ),
            onEditClick = {},
            onLogoutClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatsRowPreview() {
    ScreenAndComponentTheme {
        StatsRow(
            stats = listOf(
                StatItem("게시물", 89),
                StatItem("팔로워", 1234),
                StatItem("팔로잉", 567)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileAvatarPreview() {
    ScreenAndComponentTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ProfileAvatar(size = 48)
            ProfileAvatar(size = 64)
            ProfileAvatar(size = 80)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionButtonsPreview() {
    ScreenAndComponentTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ActionButtons(
                isEditing = false,
                onEditClick = {},
                onLogoutClick = {}
            )
            ActionButtons(
                isEditing = true,
                onEditClick = {},
                onLogoutClick = {}
            )
        }
    }
}
