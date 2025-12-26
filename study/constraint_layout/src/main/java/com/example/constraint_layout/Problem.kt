package com.example.constraint_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 문제 상황: Row/Column 중첩으로 복잡해진 레이아웃
 *
 * 이 코드는 Row와 Column을 여러 단계로 중첩하여 프로필 카드를 구현합니다.
 * 코드가 복잡하고, 요소 간 관계를 파악하기 어렵습니다.
 */
@Composable
fun ProblemScreen() {
    var recompositionCount by remember { mutableIntStateOf(0) }

    SideEffect {
        recompositionCount++
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 문제 설명 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "문제: Row/Column 중첩의 복잡성",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        • 3단계 이상 중첩으로 코드 구조 파악 어려움
                        • Spacer와 weight를 과도하게 사용
                        • 요소 간 상대적 정렬이 복잡함
                        • 레이블 길이에 따른 동적 정렬 불가
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Row/Column 중첩 예시",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 문제가 되는 코드: Row/Column 3단계 중첩
        NestedProfileCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 설정 화면 예시 - Barrier 없이 구현
        Text(
            text = "레이블/값 정렬 문제",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        NestedSettingsCard()

        Spacer(modifier = Modifier.height(16.dp))

        // 코드 구조 설명
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "중첩 구조 분석",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
Row (1단계)
├── Image
└── Column (2단계)
    ├── Row (3단계) ← 여기서 이미 복잡
    │   ├── Text(name)
    │   ├── Spacer(weight)
    │   └── Text(time)
    ├── Text(description)
    └── Row (3단계)
        ├── Button
        ├── Spacer(weight)
        └── Button
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Recomposition 횟수: ${recompositionCount}회",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * Row/Column 3단계 중첩으로 구현한 프로필 카드
 * 코드가 복잡하고 가독성이 떨어짐
 */
@Composable
private fun NestedProfileCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        // 1단계: Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 아바타 이미지
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 2단계: Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 3단계: Row (이름과 시간)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "홍길동",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "오후 3:42",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "오늘 날씨가 정말 좋네요! 공원에서 산책하기 딱 좋은 날씨입니다. 다들 즐거운 하루 보내세요!",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3단계: Row (버튼들)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("좋아요")
                    }

                    TextButton(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Comment,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("댓글")
                    }

                    TextButton(
                        onClick = { },
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("공유")
                    }
                }
            }
        }
    }
}

/**
 * Row/Column으로 구현한 설정 화면
 * 레이블 길이가 다르면 값들이 정렬되지 않음
 */
@Composable
private fun NestedSettingsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "사용자 정보",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Row로 레이블/값 배치 - 고정 너비 사용해야 정렬됨
            SettingRow(label = "이름", value = "홍길동")
            SettingRow(label = "이메일", value = "hong@example.com")
            SettingRow(label = "전화번호", value = "010-1234-5678")
            SettingRow(label = "주소", value = "서울시 강남구")

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "문제: 레이블에 고정 너비(100.dp)를 지정해야 정렬됨",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun SettingRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 문제: 고정 너비를 사용해야 정렬됨
        // 레이블이 길어지면 이 값을 수동으로 조정해야 함
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.width(100.dp)  // 하드코딩된 너비
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
