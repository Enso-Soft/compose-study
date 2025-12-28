package com.example.semantics_accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 연습 문제 1: 프로필 카드 접근성 개선 (초급)
 *
 * 요구사항:
 * - 프로필 사진, 이름, 상태 메시지를 하나의 논리적 단위로 병합
 * - 팔로우 버튼에 적절한 설명 추가
 * - 온라인/오프라인 상태를 색상 외의 방법으로도 표시
 *
 * TODO: Modifier.semantics를 사용해서 접근성을 개선하세요!
 */
@Composable
fun Practice1_ProfileCard() {
    var isFollowing by remember { mutableStateOf(false) }
    val isOnline = true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 1: 프로필 카드 접근성 개선",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TODO: 아래 프로필 카드의 접근성을 개선하세요",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "1. 프로필 정보를 하나로 병합",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "2. 팔로우 버튼 설명 추가",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "3. 온라인 상태를 텍스트로도 표시",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: 이 카드의 접근성을 개선하세요
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                // TODO: semantics(mergeDescendants = true) 추가
                // TODO: clearAndSetSemantics로 전체 설명 제공
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 프로필 이미지 + 온라인 상태
                Box {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            // TODO: contentDescription 추가
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // 온라인 상태 표시 (색상만 사용 - 문제!)
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (isOnline) Color.Green else Color.Gray)
                            .align(Alignment.BottomEnd)
                    )
                    // TODO: 색상 외에 텍스트/아이콘으로도 상태 표시
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "김철수",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "안녕하세요! 반갑습니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // TODO: 온라인 상태 텍스트 추가
                }

                Button(
                    onClick = { isFollowing = !isFollowing },
                    // TODO: semantics { contentDescription = ... } 추가
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFollowing)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (isFollowing) "팔로잉" else "팔로우")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Modifier.semantics(mergeDescendants = true) { }
Modifier.clearAndSetSemantics {
    contentDescription = "설명"
}
contentDescription = "프로필 이미지"
Text(if (isOnline) "온라인" else "오프라인")
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        /*
        // 정답 예시
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .semantics(mergeDescendants = true) {
                        contentDescription = "김철수, 안녕하세요! 반갑습니다. ${if (isOnline) "온라인" else "오프라인"}"
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "프로필 이미지",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (isOnline) Color.Green else Color.Gray)
                            .align(Alignment.BottomEnd)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "김철수",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "안녕하세요! 반갑습니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // 온라인 상태 텍스트 추가
                    Text(
                        text = if (isOnline) "온라인" else "오프라인",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isOnline) Color.Green else Color.Gray
                    )
                }
            }

            Button(
                onClick = { isFollowing = !isFollowing },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
                    .semantics {
                        contentDescription = if (isFollowing) "팔로우 취소" else "팔로우하기"
                    }
            ) {
                Text(if (isFollowing) "팔로잉" else "팔로우")
            }
        }
        */
    }
}

/**
 * 연습 문제 2: 실시간 경매 타이머 (중급)
 *
 * 요구사항:
 * - 가격 변동 시 liveRegion으로 알림
 * - 종료 임박 시 Assertive 모드로 긴급 알림
 * - 색상 외 텍스트/아이콘으로 상태 표시
 *
 * TODO: liveRegion을 사용해서 동적 알림을 구현하세요!
 */
@Composable
fun Practice2_AuctionTimer() {
    var currentPrice by remember { mutableIntStateOf(50000) }
    var remainingSeconds by remember { mutableIntStateOf(120) }
    var bidCount by remember { mutableIntStateOf(5) }

    // 타이머 시뮬레이션
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--

            // 랜덤하게 가격 상승
            if (remainingSeconds % 10 == 0 && remainingSeconds > 0) {
                currentPrice += (1000..5000).random()
                bidCount++
            }
        }
    }

    val isUrgent = remainingSeconds <= 30
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 2: 실시간 경매 타이머",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TODO: liveRegion으로 동적 알림 구현",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "1. 가격 변동 시 Polite로 알림",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "2. 30초 이하 시 Assertive로 긴급 알림",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "3. 상태를 색상 외 텍스트로도 표시",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 경매 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isUrgent)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 상품명
                Text(
                    text = "빈티지 시계",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 현재 가격
                // TODO: liveRegion = LiveRegionMode.Polite 추가
                Text(
                    text = "${"%,d".format(currentPrice)}원",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                    // TODO: Modifier.semantics { liveRegion = ... }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "입찰 ${bidCount}회",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 남은 시간
                // TODO: 30초 이하일 때 liveRegion = LiveRegionMode.Assertive
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    // TODO: Modifier.semantics { ... }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = if (isUrgent) Color.Red else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "%02d:%02d".format(minutes, seconds),
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (isUrgent) Color.Red else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                // TODO: 색상 외에 텍스트로 긴급 상태 표시
                if (isUrgent) {
                    Spacer(modifier = Modifier.height(8.dp))
                    // TODO: 긴급 상태 텍스트 추가
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        currentPrice += 5000
                        bidCount++
                    }
                ) {
                    Text("5,000원 입찰하기")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 일반 알림
Modifier.semantics {
    contentDescription = "현재 가격 ${"%,d".format(currentPrice)}원"
    liveRegion = LiveRegionMode.Polite
}

// 긴급 알림
Modifier.semantics {
    contentDescription = "경고: 30초 남음!"
    liveRegion = LiveRegionMode.Assertive
}

// 상태 텍스트
Text(
    text = "경매 종료 임박!",
    color = Color.Red
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        /*
        // 정답 예시
        // 현재 가격
        Text(
            text = "${"%,d".format(currentPrice)}원",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics {
                contentDescription = "현재 가격 ${"%,d".format(currentPrice)}원"
                liveRegion = LiveRegionMode.Polite
            }
        )

        // 남은 시간 (긴급 시 Assertive)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.semantics {
                contentDescription = if (isUrgent)
                    "경고: ${seconds}초 남음! 경매 종료 임박!"
                else
                    "남은 시간 ${minutes}분 ${seconds}초"
                liveRegion = if (isUrgent)
                    LiveRegionMode.Assertive
                else
                    LiveRegionMode.Polite
            }
        ) { ... }

        // 긴급 상태 텍스트
        if (isUrgent) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "경매 종료 임박!",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        */
    }
}

/**
 * 연습 문제 3: 회원가입 폼 탐색 순서 (고급)
 *
 * 요구사항:
 * - isTraversalGroup으로 폼 영역 구분
 * - traversalIndex로 필드 순서 조정
 * - 도움말 툴팁은 마지막에 탐색되도록 설정
 *
 * TODO: traversalIndex를 사용해서 탐색 순서를 최적화하세요!
 */
@Composable
fun Practice3_FormTraversal() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "연습 3: 폼 탐색 순서 최적화",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TODO: traversalIndex로 탐색 순서 조정",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "1. 폼 필드: 이름 -> 이메일 -> 비밀번호",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "2. 약관 동의 -> 가입 버튼 순서",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "3. 도움말 아이콘은 맨 마지막에",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회원가입 폼
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                // TODO: semantics { isTraversalGroup = true } 추가
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "회원가입",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 이름 필드 + 도움말
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("이름") },
                        // TODO: traversalIndex = 0f
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { /* 도움말 표시 */ },
                        // TODO: traversalIndex = 10f (마지막에 탐색)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "이름 입력 도움말"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 이메일 필드 + 도움말
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("이메일") },
                        // TODO: traversalIndex = 1f
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { /* 도움말 표시 */ },
                        // TODO: traversalIndex = 11f
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "이메일 입력 도움말"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 비밀번호 필드 + 도움말
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("비밀번호") },
                        // TODO: traversalIndex = 2f
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { /* 도움말 표시 */ },
                        // TODO: traversalIndex = 12f
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "비밀번호 입력 도움말"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 약관 동의
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    // TODO: traversalIndex = 3f
                    modifier = Modifier.clickable { agreedToTerms = !agreedToTerms }
                ) {
                    Checkbox(
                        checked = agreedToTerms,
                        onCheckedChange = { agreedToTerms = it }
                    )
                    Text("이용약관에 동의합니다")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 가입 버튼
                Button(
                    onClick = { /* 가입 처리 */ },
                    // TODO: traversalIndex = 4f
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotEmpty() && email.isNotEmpty() &&
                            password.isNotEmpty() && agreedToTerms
                ) {
                    Text("가입하기")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 힌트 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
// 그룹 설정
Column(
    modifier = Modifier.semantics {
        isTraversalGroup = true
    }
)

// 필드 순서 (낮은 값이 먼저)
OutlinedTextField(
    modifier = Modifier.semantics {
        traversalIndex = 0f
    }
)

// 도움말은 높은 값 (나중에 탐색)
IconButton(
    modifier = Modifier.semantics {
        traversalIndex = 10f
    }
)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 예상 탐색 순서
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "예상 탐색 순서 (TalkBack)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. 이름 필드")
                Text("2. 이메일 필드")
                Text("3. 비밀번호 필드")
                Text("4. 약관 동의 체크박스")
                Text("5. 가입하기 버튼")
                Text("6. 이름 도움말")
                Text("7. 이메일 도움말")
                Text("8. 비밀번호 도움말")
            }
        }

        /*
        // 정답 예시
        Column(
            modifier = Modifier
                .padding(16.dp)
                .semantics { isTraversalGroup = true }
        ) {
            // 이름 필드
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("이름") },
                    modifier = Modifier
                        .weight(1f)
                        .semantics { traversalIndex = 0f }
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier.semantics { traversalIndex = 10f }
                ) {
                    Icon(Icons.Default.Info, contentDescription = "이름 입력 도움말")
                }
            }

            // 이메일 필드
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("이메일") },
                    modifier = Modifier
                        .weight(1f)
                        .semantics { traversalIndex = 1f }
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier.semantics { traversalIndex = 11f }
                ) {
                    Icon(Icons.Default.Info, contentDescription = "이메일 입력 도움말")
                }
            }

            // 비밀번호 필드
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("비밀번호") },
                    modifier = Modifier
                        .weight(1f)
                        .semantics { traversalIndex = 2f }
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier.semantics { traversalIndex = 12f }
                ) {
                    Icon(Icons.Default.Info, contentDescription = "비밀번호 입력 도움말")
                }
            }

            // 약관 동의
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { agreedToTerms = !agreedToTerms }
                    .semantics { traversalIndex = 3f }
            ) {
                Checkbox(checked = agreedToTerms, onCheckedChange = { agreedToTerms = it })
                Text("이용약관에 동의합니다")
            }

            // 가입 버튼
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { traversalIndex = 4f },
                enabled = name.isNotEmpty() && email.isNotEmpty() &&
                         password.isNotEmpty() && agreedToTerms
            ) {
                Text("가입하기")
            }
        }
        */
    }
}
