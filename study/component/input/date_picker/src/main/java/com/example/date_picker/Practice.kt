package com.example.date_picker

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
 * 연습 문제 1: 생년월일 선택기 (쉬움)
 *
 * DatePickerDialog를 사용하여 생년월일을 선택하는 UI를 구현합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice1_BirthdayPicker() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 1: 생년월일 선택기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        회원가입 화면의 생년월일 입력 필드를 구현하세요.

                        요구사항:
                        1. TextField 클릭 시 DatePickerDialog 표시
                        2. 미래 날짜는 선택 불가 (생년월일이므로)
                        3. 선택 후 "YYYY년 MM월 DD일" 형식으로 표시
                        4. 선택된 날짜로 현재 나이 계산하여 표시
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- rememberDatePickerState(selectableDates = ...) 사용")
                Text("- PastDatesOnly 객체 참고 (Solution.kt)")
                Text("- selectedDateMillis를 SimpleDateFormat으로 변환")
                Text("- 나이 계산: (현재년도 - 출생년도)")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "구현 영역",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice1_Exercise()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Practice1_Exercise() {
    // TODO: 여기에 생년월일 선택기를 구현하세요

    // 힌트:
    // 1. showDatePicker 상태 변수 선언
    // 2. rememberDatePickerState(selectableDates = PastDatesOnly) 생성
    // 3. selectedDateMillis를 날짜 문자열로 변환
    // 4. OutlinedTextField (readOnly, trailingIcon에 달력 아이콘)
    // 5. if (showDatePicker) DatePickerDialog { DatePicker(state) }
    // 6. 선택된 날짜로 나이 계산하여 표시

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TODO: 직접 구현해보세요!",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "힌트를 참고하여 생년월일 선택기를 구현하세요.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 연습 문제 2: 호텔 예약 날짜 선택 (중간)
 *
 * DateRangePicker를 사용하여 체크인/체크아웃 날짜를 선택합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice2_HotelReservation() {
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 호텔 예약 날짜 선택",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        호텔 예약 화면의 체크인/체크아웃 날짜 선택을 구현하세요.

                        요구사항:
                        1. "날짜 선택" 버튼 클릭 시 DateRangePicker 표시
                        2. 오늘 이전 날짜는 선택 불가
                        3. 체크인, 체크아웃 날짜를 각각 표시
                        4. 숙박 일수 계산하여 "N박 N+1일" 형식으로 표시
                        5. 예상 가격 계산 (1박당 100,000원)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- rememberDateRangePickerState(selectableDates = FutureDatesOnly)")
                Text("- selectedStartDateMillis, selectedEndDateMillis 사용")
                Text("- 일수 계산: (종료 - 시작) / (1000 * 60 * 60 * 24)")
                Text("- DateRangePicker는 height(500.dp) 이상 필요")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "구현 영역",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice2_Exercise()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Practice2_Exercise() {
    // TODO: 여기에 호텔 예약 날짜 선택기를 구현하세요

    // 힌트:
    // 1. showDateRangePicker 상태 변수 선언
    // 2. rememberDateRangePickerState(selectableDates = FutureDatesOnly)
    // 3. selectedStartDateMillis, selectedEndDateMillis 추출
    // 4. 숙박 일수 계산: (end - start) / (1000 * 60 * 60 * 24)
    // 5. Button("날짜 선택") 클릭 시 다이얼로그 표시
    // 6. DatePickerDialog { DateRangePicker(state, modifier = Modifier.height(500.dp)) }
    // 7. 체크인, 체크아웃, 숙박 일수, 예상 가격 표시

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TODO: 직접 구현해보세요!",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "힌트를 참고하여 호텔 예약 날짜 선택기를 구현하세요.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 연습 문제 3: 근무 가능일 선택 (어려움)
 *
 * 커스텀 SelectableDates를 구현하여 복잡한 날짜 제한을 적용합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Practice3_WorkdaySelection() {
    // 공휴일 목록 (밀리초) - 연습 문제에서 사용할 예시
    // 실제 구현 시 이 값들을 참고하세요
    @Suppress("UNUSED_VARIABLE")
    val holidaysExample = listOf<Long>(
        // Calendar.getInstance().apply { set(2025, 0, 1, 0, 0, 0) }.timeInMillis,  // 1월 1일
        // Calendar.getInstance().apply { set(2025, 0, 28, 0, 0, 0) }.timeInMillis, // 설날
    )

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
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: 근무 가능일 선택",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        아르바이트 근무 가능일을 선택하는 UI를 구현하세요.

                        요구사항:
                        1. 주말(토, 일)은 선택 불가
                        2. 공휴일도 선택 불가 (아래 목록 사용)
                        3. 오늘부터 30일 이내만 선택 가능
                        4. 선택한 날짜들을 리스트로 누적 표시
                        5. "초기화" 버튼으로 선택 목록 비우기

                        공휴일 목록 (2025년):
                        - 1월 1일 (신정)
                        - 1월 28~30일 (설날)
                        - 3월 1일 (삼일절)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 힌트 카드
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("- object : SelectableDates { override fun isSelectableDate() } 구현")
                Text("- Calendar.DAY_OF_WEEK로 요일 확인")
                Text("- 공휴일 목록과 비교 (utcTimeMillis in holidays)")
                Text("- 오늘 + 30일 계산: System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L")
                Text("- 선택된 날짜들: mutableStateListOf<Long>() 사용")
            }
        }

        // 연습 영역
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "구현 영역",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Practice3_Exercise()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Practice3_Exercise() {
    // TODO: 여기에 근무 가능일 선택기를 구현하세요

    // 힌트:
    // 1. 공휴일 목록 정의 (Calendar로 밀리초 변환)
    //    val holidays = listOf(
    //        Calendar.getInstance().apply {
    //            set(2025, Calendar.JANUARY, 1, 0, 0, 0)
    //        }.timeInMillis,
    //        ...
    //    )

    // 2. 커스텀 SelectableDates 구현
    //    val workdaysOnly = object : SelectableDates {
    //        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
    //            // 주말 체크
    //            val calendar = Calendar.getInstance()
    //            calendar.timeInMillis = utcTimeMillis
    //            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    //            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
    //                return false
    //            }
    //
    //            // 공휴일 체크
    //            if (utcTimeMillis in holidays) return false
    //
    //            // 30일 이내 체크
    //            val now = System.currentTimeMillis()
    //            val thirtyDaysLater = now + 30L * 24 * 60 * 60 * 1000
    //            if (utcTimeMillis < now || utcTimeMillis > thirtyDaysLater) {
    //                return false
    //            }
    //
    //            return true
    //        }
    //    }

    // 3. 선택된 날짜들을 저장할 리스트
    //    val selectedDates = remember { mutableStateListOf<Long>() }

    // 4. DatePicker 선택 시 리스트에 추가
    //    datePickerState.selectedDateMillis?.let { millis ->
    //        if (millis !in selectedDates) {
    //            selectedDates.add(millis)
    //        }
    //    }

    // 5. 선택된 날짜 목록 표시 + 초기화 버튼

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TODO: 직접 구현해보세요!",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "힌트를 참고하여 근무 가능일 선택기를 구현하세요.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "이 연습 문제는 다음을 학습합니다:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Text("- 커스텀 SelectableDates 구현")
        Text("- 다중 조건 적용 (주말 + 공휴일 + 기간 제한)")
        Text("- 선택 결과 누적 관리")
    }
}
