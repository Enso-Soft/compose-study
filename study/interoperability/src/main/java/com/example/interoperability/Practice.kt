package com.example.interoperability

import android.widget.CalendarView
import android.widget.RatingBar
import android.widget.SeekBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.text.SimpleDateFormat
import java.util.*

/**
 * 연습 문제 1: SeekBar로 볼륨 조절기 만들기 (초급)
 *
 * 요구사항:
 * - SeekBar를 AndroidView로 통합
 * - 볼륨 값(0~100)을 Compose State로 관리
 * - 현재 볼륨을 Text와 아이콘으로 표시
 * - "음소거" 버튼으로 0으로 설정
 */
@Composable
fun Practice1_VolumeControl() {
    // TODO: 볼륨 상태 선언 (0~100)
    var volume by remember { mutableIntStateOf(50) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "연습 1: 볼륨 조절기",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 힌트 카드
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("힌트:", style = MaterialTheme.typography.titleSmall)
                    Text("• SeekBar.setOnSeekBarChangeListener로 값 변경 감지")
                    Text("• fromUser 파라미터로 사용자 입력 구분")
                    Text("• 볼륨에 따라 다른 아이콘 표시 (🔇🔈🔉🔊)")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 볼륨 아이콘과 값 표시
            Text(
                text = when {
                    volume == 0 -> "🔇"
                    volume < 33 -> "🔈"
                    volume < 66 -> "🔉"
                    else -> "🔊"
                },
                style = MaterialTheme.typography.displayLarge
            )

            Text(
                text = "볼륨: $volume%",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: AndroidView로 SeekBar 통합
            // factory에서 SeekBar 생성, max=100 설정, 리스너 등록
            // update에서 Compose → View 동기화

            /* 정답:
            AndroidView(
                factory = { context ->
                    SeekBar(context).apply {
                        max = 100
                        progress = volume
                        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                                if (fromUser) volume = value
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })
                    }
                },
                update = { seekBar ->
                    if (seekBar.progress != volume) {
                        seekBar.progress = volume
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            */

            // 임시 Slider (AndroidView 구현 전 테스트용)
            Slider(
                value = volume.toFloat(),
                onValueChange = { volume = it.toInt() },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 컨트롤 버튼들
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { volume = 0 }
                ) {
                    Text("🔇 음소거")
                }

                Button(
                    onClick = { volume = 100 }
                ) {
                    Text("🔊 최대")
                }
            }
        }
    }
}

/**
 * 연습 문제 2: CalendarView로 날짜 선택기 만들기 (중급)
 *
 * 요구사항:
 * - CalendarView를 AndroidView로 통합
 * - 선택된 날짜를 Compose State로 관리
 * - "오늘" 버튼으로 오늘 날짜로 이동
 * - 선택된 날짜를 포맷팅하여 표시
 */
@Composable
fun Practice2_DatePicker() {
    // TODO: 선택된 날짜 상태 (밀리초)
    var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val dateFormat = remember { SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREA) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "연습 2: 날짜 선택기",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 힌트 카드
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("힌트:", style = MaterialTheme.typography.titleSmall)
                    Text("• CalendarView.setOnDateChangeListener 사용")
                    Text("• Calendar 클래스로 년/월/일 → 밀리초 변환")
                    Text("• calendarView.date로 Compose → View 동기화")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 선택된 날짜 표시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("선택된 날짜", style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = dateFormat.format(Date(selectedDate)),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: AndroidView로 CalendarView 통합
            // factory에서 CalendarView 생성, OnDateChangeListener 등록
            // update에서 calendarView.date 동기화

            /* 정답:
            AndroidView(
                factory = { context ->
                    CalendarView(context).apply {
                        date = selectedDate
                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            val calendar = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth)
                            }
                            selectedDate = calendar.timeInMillis
                        }
                    }
                },
                update = { calendarView ->
                    if (calendarView.date != selectedDate) {
                        calendarView.date = selectedDate
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            */

            // 임시 표시 (AndroidView 구현 전)
            Text(
                text = "CalendarView가 여기에 표시됩니다",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 컨트롤 버튼들
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { selectedDate = System.currentTimeMillis() }
                ) {
                    Text("📅 오늘")
                }

                Button(
                    onClick = {
                        val cal = Calendar.getInstance().apply {
                            timeInMillis = selectedDate
                            add(Calendar.DAY_OF_MONTH, 7)
                        }
                        selectedDate = cal.timeInMillis
                    }
                ) {
                    Text("➡️ +7일")
                }
            }
        }
    }
}

/**
 * 연습 문제 3: RatingBar로 리뷰 작성기 만들기 (고급)
 *
 * 요구사항:
 * - RatingBar를 AndroidView로 통합
 * - 별점(0.0~5.0)을 Compose State로 관리
 * - 리뷰 텍스트 입력 필드 (Compose TextField)
 * - 별점에 따라 다른 메시지 표시
 * - "제출" 버튼으로 리뷰 제출 (Snackbar 표시)
 */
@Composable
fun Practice3_ReviewForm() {
    // TODO: 별점 상태 (0.0~5.0)
    var rating by remember { mutableFloatStateOf(3.0f) }
    var reviewText by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Snackbar 표시
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(
                message = "리뷰가 제출되었습니다! ⭐ ${rating}점",
                duration = SnackbarDuration.Short
            )
            showSnackbar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "연습 3: 리뷰 작성기",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 힌트 카드
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("힌트:", style = MaterialTheme.typography.titleSmall)
                        Text("• RatingBar.setOnRatingBarChangeListener 사용")
                        Text("• numStars=5, stepSize=0.5f 설정")
                        Text("• ratingBar.rating으로 Compose → View 동기화")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 별점 표시
                Text(
                    text = "⭐".repeat(rating.toInt()) + if (rating % 1 >= 0.5) "✨" else "",
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "$rating / 5.0",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = when {
                        rating <= 1.0f -> "😢 매우 불만족"
                        rating <= 2.0f -> "😕 불만족"
                        rating <= 3.0f -> "😐 보통"
                        rating <= 4.0f -> "😊 만족"
                        else -> "🤩 매우 만족"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // TODO: AndroidView로 RatingBar 통합
                // factory에서 RatingBar 생성, numStars=5, stepSize=0.5f
                // OnRatingBarChangeListener로 값 변경 감지
                // update에서 rating 동기화

                /* 정답:
                AndroidView(
                    factory = { context ->
                        RatingBar(context).apply {
                            numStars = 5
                            stepSize = 0.5f
                            this.rating = rating
                            setOnRatingBarChangeListener { _, value, fromUser ->
                                if (fromUser) rating = value
                            }
                        }
                    },
                    update = { ratingBar ->
                        if (ratingBar.rating != rating) {
                            ratingBar.rating = rating
                        }
                    }
                )
                */

                // 임시 Slider (RatingBar 구현 전)
                Slider(
                    value = rating,
                    onValueChange = { rating = (it * 2).toInt() / 2f }, // 0.5 단위
                    valueRange = 0f..5f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 리뷰 텍스트 입력
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("리뷰 작성") },
                    placeholder = { Text("제품에 대한 의견을 남겨주세요") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 제출 버튼
                Button(
                    onClick = { showSnackbar = true },
                    enabled = rating > 0f && reviewText.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("리뷰 제출하기")
                }

                // 빠른 별점 선택
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1f, 2f, 3f, 4f, 5f).forEach { value ->
                        OutlinedButton(
                            onClick = { rating = value },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("${value.toInt()}⭐")
                        }
                    }
                }
            }
        }
    }
}
