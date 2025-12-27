package com.example.adaptive_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Problem: 화면 크기를 무시한 단일 레이아웃
 *
 * 이 코드의 문제점:
 * 1. 폰, 태블릿, 폴더블 관계없이 동일한 레이아웃 사용
 * 2. 태블릿에서 화면의 절반 이상이 비어있음
 * 3. 리스트와 디테일을 동시에 볼 수 없어 생산성 저하
 * 4. 불필요한 전체 화면 전환으로 UX 저하
 *
 * 테스트 방법:
 * - Android Studio에서 Resizable Emulator 사용
 * - 또는 태블릿 에뮬레이터로 실행하여 화면 낭비 확인
 */

// 샘플 이메일 데이터
data class Email(
    val id: Int,
    val sender: String,
    val subject: String,
    val preview: String,
    val content: String,
    val time: String
)

val sampleEmails = listOf(
    Email(1, "Google", "계정 보안 알림", "새로운 기기에서 로그인이 감지...", "안녕하세요,\n\n새로운 기기에서 Google 계정 로그인이 감지되었습니다.\n\n기기: Pixel 8\n위치: 서울, 대한민국\n시간: 2025년 1월 15일 오후 3:24\n\n본인이 아닌 경우 즉시 비밀번호를 변경하세요.", "오후 3:24"),
    Email(2, "GitHub", "[compose-study] PR #42 merged", "Your pull request has been...", "Your pull request #42 \"Add Adaptive Layout module\" has been successfully merged into main.\n\nChanges:\n- Added WindowSizeClass support\n- Implemented ListDetailPaneScaffold\n- Added practice exercises\n\nThank you for your contribution!", "오후 2:15"),
    Email(3, "Netflix", "새로운 콘텐츠 알림", "이번 주 신작을 확인하세요...", "안녕하세요,\n\n이번 주 Netflix 신작을 소개합니다:\n\n1. 오징어 게임 시즌 3\n2. 더 글로리 특별판\n3. 이상한 변호사 우영우 2\n\n지금 바로 시청하세요!", "오후 1:30"),
    Email(4, "Slack", "5개의 읽지 않은 메시지", "#android-team 채널에 새 메시지가...", "[#android-team]\n\n@김개발: 오늘 Compose 스터디 참석 가능하신 분?\n@이안드: 저요! 3시에 뵐게요\n@박코틀: 저도 참석합니다\n@최컴포: 오늘 주제가 뭐에요?\n@김개발: Adaptive Layout입니다!", "오전 11:45"),
    Email(5, "배달의민족", "주문이 완료되었습니다", "맛있는 점심 되세요!...", "주문 번호: 2025011512345\n\n주문 내역:\n- 김치찌개 1인분 8,000원\n- 공기밥 1,000원\n- 계란말이 5,000원\n\n합계: 14,000원\n\n예상 도착 시간: 30-40분", "오전 11:30")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemScreen() {
    var selectedEmail by remember { mutableStateOf<Email?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // 문제점 설명 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: 화면 크기를 무시한 단일 레이아웃",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildString {
                        appendLine("1. 태블릿에서 화면의 절반 이상이 비어있음")
                        appendLine("2. 이메일 내용을 보려면 매번 전체 화면 전환")
                        appendLine("3. 리스트와 디테일을 동시에 볼 수 없음")
                        append("4. 폴더블 기기에서도 폰과 동일한 UX")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // 문제가 되는 코드: 화면 크기와 관계없이 항상 단일 화면 방식
        // 태블릿에서 실행하면 화면의 많은 부분이 낭비됨
        if (selectedEmail == null) {
            ProblemEmailList(
                emails = sampleEmails,
                onEmailClick = { selectedEmail = it }
            )
        } else {
            ProblemEmailDetail(
                email = selectedEmail!!,
                onBack = { selectedEmail = null }
            )
        }
    }
}

@Composable
private fun ProblemEmailList(
    emails: List<Email>,
    onEmailClick: (Email) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(emails) { email ->
            ProblemEmailItem(
                email = email,
                onClick = { onEmailClick(email) }
            )
        }
    }
}

@Composable
private fun ProblemEmailItem(
    email: Email,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = email.sender,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email.subject,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = email.preview,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProblemEmailDetail(
    email: Email,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 뒤로가기 버튼
        TopAppBar(
            title = { Text("이메일 상세") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
            }
        )

        // 이메일 내용
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = email.subject,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = email.sender.first().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = email.sender,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = email.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = email.content,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
