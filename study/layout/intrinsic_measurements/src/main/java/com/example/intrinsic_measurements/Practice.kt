package com.example.intrinsic_measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 프로필 비교 UI (쉬움)
 *
 * 목표: IntrinsicSize.Min을 사용하여 Divider 높이 동기화
 *
 * 요구사항:
 * 두 사용자 프로필을 Row로 나란히 배치하고,
 * 가운데 VerticalDivider로 구분하세요.
 * 왼쪽 프로필은 4줄, 오른쪽은 2줄입니다.
 * Divider가 왼쪽 프로필 높이에 맞춰야 합니다.
 */
@Composable
fun Practice1_ProfileComparison() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 프로필 비교 UI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "두 사용자 프로필을 비교하는 UI를 만드세요.\n" +
                            "- 왼쪽: 이름, 나이, 직업, 취미 (4줄)\n" +
                            "- 오른쪽: 이름, 나이 (2줄)\n" +
                            "- 가운데 VerticalDivider가 왼쪽 높이에 맞춰야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. Row에 height(IntrinsicSize.Min) 적용")
                Text("2. VerticalDivider에 fillMaxHeight() 적용")
                Text("3. 각 Column에 weight(1f) 적용")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "구현 결과:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Practice1_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }
        Button(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice1_Answer()
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    // TODO: 여기에 IntrinsicSize.Min을 사용하여 구현하세요

    // 아래는 "문제 상황" 코드입니다.
    // height(IntrinsicSize.Min)을 추가하여 Divider가 정상 동작하게 만드세요!

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // TODO: .height(IntrinsicSize.Min) 추가
            .border(1.dp, Color.Gray)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFE3F2FD))
                .padding(8.dp)
        ) {
            Text("사용자 A", fontWeight = FontWeight.Bold)
            Text("나이: 28세")
            Text("직업: 디자이너")
            Text("취미: 독서, 요가")
        }

        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFFCE4EC))
                .padding(8.dp)
        ) {
            Text("사용자 B", fontWeight = FontWeight.Bold)
            Text("나이: 32세")
        }
    }
}

@Composable
private fun Practice1_Answer() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "정답",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 정답 코드 실행
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)  // 정답!
                    .border(2.dp, Color(0xFF2E7D32))
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFE3F2FD))
                        .padding(8.dp)
                ) {
                    Text("사용자 A", fontWeight = FontWeight.Bold)
                    Text("나이: 28세")
                    Text("직업: 디자이너")
                    Text("취미: 독서, 요가")
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFFCE4EC))
                        .padding(8.dp)
                ) {
                    Text("사용자 B", fontWeight = FontWeight.Bold)
                    Text("나이: 32세")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)  // 추가!
        ...
)
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            )
        }
    }
}

/**
 * 연습 문제 2: 동일 높이 상품 카드 (중간)
 *
 * 목표: IntrinsicSize.Min + fillMaxHeight 조합으로 동일 높이 카드 구현
 *
 * 요구사항:
 * 3개의 상품 카드를 Row로 배치하세요.
 * 각 카드는 상품명, 가격, 설명을 포함합니다.
 * 설명의 길이가 다르더라도 모든 카드가 동일한 높이를 가져야 합니다.
 * "구매" 버튼은 각 카드의 하단에 고정되어야 합니다.
 */
@Composable
fun Practice2_ProductCards() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 동일 높이 상품 카드",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "3개의 상품 카드를 동일한 높이로 만드세요.\n" +
                            "- 각 카드: 상품명, 가격, 설명, 구매 버튼\n" +
                            "- 설명 길이가 달라도 카드 높이는 동일\n" +
                            "- 구매 버튼은 각 카드 하단에 고정",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. Row에 height(IntrinsicSize.Min) 적용")
                Text("2. 각 Card에 fillMaxHeight() 적용")
                Text("3. Column 내 Spacer(Modifier.weight(1f))로 버튼을 하단에 밀기")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "구현 결과:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Practice2_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }
        Button(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice2_Answer()
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    // TODO: 여기에 IntrinsicSize.Min + fillMaxHeight를 사용하여 구현하세요

    val products = listOf(
        Triple("노트북", "1,200,000원", "고성능 프로세서"),
        Triple("모니터", "450,000원", "27인치 4K 해상도, HDR 지원, 색재현율 99%"),
        Triple("키보드", "150,000원", "기계식, RGB 백라이트, 무선")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // TODO: .height(IntrinsicSize.Min) 추가
            .border(1.dp, Color.Gray),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        products.forEach { (name, price, desc) ->
            Card(
                modifier = Modifier
                    .weight(1f),
                    // TODO: .fillMaxHeight() 추가
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(name, fontWeight = FontWeight.Bold)
                    Text(price, color = MaterialTheme.colorScheme.primary)
                    Text(desc, style = MaterialTheme.typography.bodySmall)
                    // TODO: Spacer(Modifier.weight(1f)) 추가하여 버튼을 하단으로
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("구매")
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice2_Answer() {
    val products = listOf(
        Triple("노트북", "1,200,000원", "고성능 프로세서"),
        Triple("모니터", "450,000원", "27인치 4K 해상도, HDR 지원, 색재현율 99%"),
        Triple("키보드", "150,000원", "기계식, RGB 백라이트, 무선")
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "정답",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 정답 코드 실행
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)  // 정답!
                    .border(2.dp, Color(0xFF2E7D32)),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                products.forEach { (name, price, desc) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),  // 정답!
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text(price, color = MaterialTheme.colorScheme.primary)
                            Text(desc, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.weight(1f))  // 정답!
                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("구매")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
Row(
    modifier = Modifier
        .height(IntrinsicSize.Min)  // 추가!
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()  // 추가!
    ) {
        Column {
            // ... 내용 ...
            Spacer(Modifier.weight(1f))  // 추가!
            Button { }
        }
    }
}
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            )
        }
    }
}

/**
 * 연습 문제 3: 성적표 테이블 (어려움)
 *
 * 목표: IntrinsicSize를 활용한 데이터 테이블 구현
 *
 * 요구사항:
 * 학생 성적표를 테이블 형태로 구현하세요:
 * - 헤더: 이름 | 국어 | 영어 | 수학
 * - 각 행의 셀들이 동일한 높이
 * - VerticalDivider와 HorizontalDivider로 셀 구분
 */
@Composable
fun Practice3_TableLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 문제 설명
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 성적표 테이블",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "학생 성적표를 테이블로 구현하세요.\n" +
                            "- 헤더 행: 이름 | 국어 | 영어 | 수학\n" +
                            "- 데이터 행: 학생 정보\n" +
                            "- 각 셀은 Divider로 구분\n" +
                            "- 모든 행의 높이가 동일해야 합니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1. 각 Row에 height(IntrinsicSize.Min)")
                Text("2. VerticalDivider에 fillMaxHeight()")
                Text("3. HorizontalDivider로 행 구분")
                Text("4. 셀에 weight(1f)로 균등 분배")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "구현 결과:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Practice3_Exercise()
            }
        }

        // 정답 보기
        var showAnswer by remember { mutableStateOf(false) }
        Button(
            onClick = { showAnswer = !showAnswer },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showAnswer) "정답 숨기기" else "정답 보기")
        }

        if (showAnswer) {
            Practice3_Answer()
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    // TODO: 여기에 테이블 레이아웃을 구현하세요

    data class Student(val name: String, val korean: Int, val english: Int, val math: Int)

    val students = listOf(
        Student("김철수", 95, 88, 92),
        Student("이영희", 82, 95, 78),
        Student("박민수 (보충수업)", 75, 70, 85)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
    ) {
        // 헤더 행
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // TODO: .height(IntrinsicSize.Min) 추가
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            listOf("이름", "국어", "영어", "수학").forEachIndexed { index, header ->
                if (index > 0) {
                    VerticalDivider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        color = Color.Gray
                    )
                }
                Text(
                    text = header,
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        HorizontalDivider(color = Color.Gray)

        // 데이터 행들
        students.forEach { student ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // TODO: .height(IntrinsicSize.Min) 추가
            ) {
                val values = listOf(
                    student.name,
                    student.korean.toString(),
                    student.english.toString(),
                    student.math.toString()
                )
                values.forEachIndexed { index, value ->
                    if (index > 0) {
                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp),
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = value,
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        textAlign = if (index == 0) TextAlign.Start else TextAlign.Center
                    )
                }
            }
            HorizontalDivider(color = Color.Gray)
        }
    }
}

@Composable
private fun Practice3_Answer() {
    data class Student(val name: String, val korean: Int, val english: Int, val math: Int)

    val students = listOf(
        Student("김철수", 95, 88, 92),
        Student("이영희", 82, 95, 78),
        Student("박민수 (보충수업)", 75, 70, 85)
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "정답",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 정답 코드 실행
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFF2E7D32))
            ) {
                // 헤더 행
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)  // 정답!
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    listOf("이름", "국어", "영어", "수학").forEachIndexed { index, header ->
                        if (index > 0) {
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp),
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = header,
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                HorizontalDivider(color = Color.Gray)

                // 데이터 행들
                students.forEach { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)  // 정답!
                    ) {
                        val values = listOf(
                            student.name,
                            student.korean.toString(),
                            student.english.toString(),
                            student.math.toString()
                        )
                        values.forEachIndexed { index, value ->
                            if (index > 0) {
                                VerticalDivider(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(1.dp),
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = value,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(12.dp),
                                textAlign = if (index == 0) TextAlign.Start else TextAlign.Center
                            )
                        }
                    }
                    HorizontalDivider(color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = """
// 각 Row에 height(IntrinsicSize.Min) 적용
Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)  // 핵심!
) {
    // 셀들...
    VerticalDivider(
        modifier = Modifier.fillMaxHeight()
    )
    // 다음 셀들...
}
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            )
        }
    }
}
