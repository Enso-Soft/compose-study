package com.example.screen_and_component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: Screen + Content 분리하기
 *
 * 목표: 모든 것이 섞인 Composable을 Screen + Content로 분리
 * 시나리오: 간단한 카운터 화면
 *
 * TODO: CounterScreen과 CounterContent로 분리하세요.
 */
@Composable
fun Practice1_ScreenContentSeparation() {
    // 상태 관리 (Screen 역할)
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: Screen + Content 분리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래 CounterContent를 완성하세요.\n" +
                            "상태(count)를 파라미터로 받고,\n" +
                            "이벤트(onIncrement, onDecrement)를 콜백으로 전달합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // CounterContent 사용 (연습!)
        CounterContent(
            count = count,
            onIncrement = { count++ },
            onDecrement = { count-- },
            onReset = { count = 0 }
        )

        // 힌트 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트: Content 구조",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun CounterContent(
    count: Int,              // State down
    onIncrement: () -> Unit, // Event up
    onDecrement: () -> Unit, // Event up
    onReset: () -> Unit,     // Event up
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column {
            Text("카운트: ${'$'}count")
            Row {
                Button(onClick = onDecrement) { Text("-") }
                Button(onClick = onIncrement) { Text("+") }
            }
            Button(onClick = onReset) { Text("리셋") }
        }
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * CounterContent - Stateless
 * TODO: 이 컴포넌트를 완성하세요
 */
@Composable
fun CounterContent(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    // === 정답 ===
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CounterContent (Stateless)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "$count",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledTonalButton(onClick = onDecrement) {
                    Text("-1")
                }
                Button(onClick = onIncrement) {
                    Icon(Icons.Default.Add, contentDescription = "증가")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+1")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = onReset) {
                Icon(Icons.Default.Refresh, contentDescription = "리셋")
                Spacer(modifier = Modifier.width(4.dp))
                Text("리셋")
            }
        }
    }
}

/**
 * 연습 문제 2: Component 추출하기
 *
 * 목표: 반복되는 UI 요소를 재사용 가능한 Component로 추출
 * 시나리오: 사용자 정보 카드
 *
 * TODO: UserInfoCard Component를 완성하세요.
 */
@Composable
fun Practice2_ExtractComponent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: Component 추출하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "아래에서 UserInfoCard가 3번 재사용됩니다.\n" +
                            "같은 컴포넌트에 다른 데이터를 전달하여 재사용합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // UserInfoCard 재사용 예시
        Text(
            text = "팀원 목록",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        UserInfoCard(
            name = "김철수",
            role = "개발자",
            email = "kim@example.com",
            onClick = { /* 클릭 처리 */ }
        )

        UserInfoCard(
            name = "이영희",
            role = "디자이너",
            email = "lee@example.com",
            onClick = { /* 클릭 처리 */ }
        )

        UserInfoCard(
            name = "박민수",
            role = "PM",
            email = "park@example.com",
            onClick = { /* 클릭 처리 */ }
        )

        // 힌트 코드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트: UserInfoCard 구조",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
@Composable
fun UserInfoCard(
    name: String,
    role: String,
    email: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) { /* 아이콘 */ }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(name, fontWeight = FontWeight.Bold)
                Text(role, style = MaterialTheme.typography.bodySmall)
                Text(email, color = Color.Gray)
            }
        }
    }
}
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * UserInfoCard - 재사용 가능한 Component
 * TODO: 이 컴포넌트를 완성하세요
 */
@Composable
fun UserInfoCard(
    name: String,
    role: String,
    email: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // === 정답 ===
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "상세 보기",
                tint = Color.Gray
            )
        }
    }
}

/**
 * 연습 문제 3: 프로필 화면 완성하기
 *
 * 목표: Screen + Content + Components로 완전한 화면 구성
 * 시나리오: 프로필 화면을 체계적으로 분리
 *
 * TODO: 아래 구조를 완성하세요.
 */
@Composable
fun Practice3_CompleteProfile() {
    // 상태 관리 (Screen 역할)
    var profileState by remember {
        mutableStateOf(
            ProfilePracticeState(
                name = "홍길동",
                email = "hong@example.com",
                bio = "안드로이드 개발자",
                posts = 42,
                followers = 1234,
                following = 567,
                isFollowing = false
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 힌트 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 프로필 화면 완성하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ProfilePracticeContent를 완성하세요.\n" +
                            "ProfileHeader, ProfileBio, ProfileStats,\n" +
                            "FollowButton 컴포넌트를 사용합니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // ProfilePracticeContent 사용
        ProfilePracticeContent(
            state = profileState,
            onFollowClick = {
                profileState = profileState.copy(
                    isFollowing = !profileState.isFollowing,
                    followers = if (profileState.isFollowing)
                        profileState.followers - 1
                    else
                        profileState.followers + 1
                )
            },
            onMessageClick = { /* 메시지 */ }
        )

        // 구조 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "구조 설명",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
ProfilePracticeContent
   |
   +-- ProfileHeader (이름, 이메일, 아바타)
   |
   +-- ProfileBio (소개글)
   |
   +-- ProfileStats (게시물, 팔로워, 팔로잉)
   |
   +-- FollowButton + MessageButton
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// ============================================================
// 연습 3을 위한 State
// ============================================================

data class ProfilePracticeState(
    val name: String,
    val email: String,
    val bio: String,
    val posts: Int,
    val followers: Int,
    val following: Int,
    val isFollowing: Boolean
)

// ============================================================
// 연습 3을 위한 Content
// ============================================================

/**
 * ProfilePracticeContent - 연습 3의 메인 Content
 * TODO: 이 컴포넌트를 완성하세요
 */
@Composable
fun ProfilePracticeContent(
    state: ProfilePracticeState,
    onFollowClick: () -> Unit,
    onMessageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // === 정답 ===
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ProfileHeader Component
            ProfilePracticeHeader(
                name = state.name,
                email = state.email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ProfileBio Component
            ProfilePracticeBio(bio = state.bio)

            Spacer(modifier = Modifier.height(16.dp))

            // ProfileStats Component
            ProfilePracticeStats(
                posts = state.posts,
                followers = state.followers,
                following = state.following
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FollowPracticeButton(
                    isFollowing = state.isFollowing,
                    onClick = onFollowClick,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = onMessageClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("메시지")
                }
            }
        }
    }
}

// ============================================================
// 연습 3을 위한 Components
// ============================================================

/**
 * ProfilePracticeHeader Component
 */
@Composable
fun ProfilePracticeHeader(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Surface(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

/**
 * ProfilePracticeBio Component
 */
@Composable
fun ProfilePracticeBio(
    bio: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = bio,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * ProfilePracticeStats Component
 */
@Composable
fun ProfilePracticeStats(
    posts: Int,
    followers: Int,
    following: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatPracticeColumn(label = "게시물", value = posts)
        StatPracticeColumn(label = "팔로워", value = followers)
        StatPracticeColumn(label = "팔로잉", value = following)
    }
}

/**
 * StatPracticeColumn Component
 */
@Composable
fun StatPracticeColumn(
    label: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatPracticeNumber(value),
            style = MaterialTheme.typography.titleLarge,
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
 * FollowPracticeButton Component
 */
@Composable
fun FollowPracticeButton(
    isFollowing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isFollowing) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("팔로잉")
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("팔로우")
        }
    }
}

// ============================================================
// Utility
// ============================================================

private fun formatPracticeNumber(number: Int): String {
    return when {
        number >= 10000 -> "${number / 10000}.${(number % 10000) / 1000}만"
        number >= 1000 -> "${number / 1000},${String.format("%03d", number % 1000)}"
        else -> number.toString()
    }
}
