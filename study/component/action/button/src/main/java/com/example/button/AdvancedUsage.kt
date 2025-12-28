package com.example.button

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * AdvancedUsage: Button 커스터마이징 및 실무 패턴
 *
 * 다루는 내용:
 * 1. 커스텀 색상 (ButtonDefaults.buttonColors)
 * 2. 커스텀 모양 (RoundedCornerShape, CutCornerShape)
 * 3. 로딩 상태 버튼
 * 4. 아이콘 + 텍스트 조합
 * 5. 전체 너비 버튼
 * 6. 비활성화 상태
 * 7. 접근성 (contentDescription)
 */
@Composable
fun AdvancedUsageScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. 커스텀 색상
        item {
            CustomColorSection()
        }

        // 2. 커스텀 모양
        item {
            CustomShapeSection()
        }

        // 3. 로딩 상태 버튼
        item {
            LoadingButtonSection()
        }

        // 4. 아이콘 + 텍스트 조합
        item {
            IconTextButtonSection()
        }

        // 5. 전체 너비 버튼
        item {
            FullWidthButtonSection()
        }

        // 6. 비활성화 상태
        item {
            DisabledButtonSection()
        }

        // 7. 접근성
        item {
            AccessibilitySection()
        }
    }
}

@Composable
private fun CustomColorSection() {
    AdvancedCard(
        title = "1. 커스텀 색상",
        description = "ButtonDefaults.buttonColors()를 사용하여 버튼의 배경색(containerColor)과 텍스트 색상(contentColor)을 변경할 수 있습니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // 브랜드 컬러 버튼
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DA1F2), // Twitter 블루
                    contentColor = Color.White
                )
            ) {
                Text("Twitter 스타일")
            }

            // 성공 버튼
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // 녹색
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("성공")
            }

            // 위험 버튼
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336), // 빨강
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.Delete, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("삭제")
            }

            // 코드 예시
            CodeSnippet(
                """
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF1DA1F2),
        contentColor = Color.White
    )
) {
    Text("커스텀 색상")
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun CustomShapeSection() {
    AdvancedCard(
        title = "2. 커스텀 모양",
        description = "shape 파라미터를 사용하여 버튼의 모서리 모양을 변경할 수 있습니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // 완전히 둥근 버튼
            Button(
                onClick = { },
                shape = RoundedCornerShape(50)
            ) {
                Text("Pill 모양")
            }

            // 약간 둥근 버튼
            Button(
                onClick = { },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("약간 둥근 모서리")
            }

            // 각진 버튼
            Button(
                onClick = { },
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("각진 모서리")
            }

            // 잘린 모서리
            Button(
                onClick = { },
                shape = CutCornerShape(8.dp)
            ) {
                Text("잘린 모서리")
            }

            CodeSnippet(
                """
Button(
    onClick = { },
    shape = RoundedCornerShape(50) // Pill 모양
) {
    Text("Pill 모양")
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun LoadingButtonSection() {
    var isLoading by remember { mutableStateOf(false) }

    // 로딩 시뮬레이션
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(3000) // 3초 대기
            isLoading = false
        }
    }

    AdvancedCard(
        title = "3. 로딩 상태 버튼",
        description = "비동기 작업 중에는 버튼을 비활성화하고 로딩 인디케이터를 표시합니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { isLoading = true },
                enabled = !isLoading,
                modifier = Modifier.animateContentSize()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("처리 중...")
                } else {
                    Text("제출하기")
                }
            }

            Text(
                text = if (isLoading) "3초 후 완료됩니다..." else "버튼을 클릭해보세요",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CodeSnippet(
                """
Button(
    onClick = { isLoading = true },
    enabled = !isLoading
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp
        )
        Spacer(Modifier.width(8.dp))
        Text("처리 중...")
    } else {
        Text("제출하기")
    }
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun IconTextButtonSection() {
    AdvancedCard(
        title = "4. 아이콘 + 텍스트 조합",
        description = "Button 안에서 Icon과 Text를 함께 사용할 수 있습니다. Spacer로 간격을 조절합니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // 왼쪽 아이콘
            Button(onClick = { }) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("장바구니 담기")
            }

            // 오른쪽 아이콘
            Button(onClick = { }) {
                Text("다음으로")
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }

            // 양쪽 아이콘
            OutlinedButton(onClick = { }) {
                Icon(Icons.Filled.Download, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("다운로드")
                Spacer(Modifier.width(8.dp))
                Text("(2MB)")
            }

            CodeSnippet(
                """
Button(onClick = { }) {
    Icon(Icons.Filled.ShoppingCart, contentDescription = null)
    Spacer(Modifier.width(8.dp))
    Text("장바구니 담기")
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun FullWidthButtonSection() {
    AdvancedCard(
        title = "5. 전체 너비 버튼",
        description = "Modifier.fillMaxWidth()를 사용하여 버튼을 화면 전체 너비로 확장합니다. 주로 하단 CTA 버튼에 사용합니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("결제하기")
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("나중에 결제하기")
            }

            // 버튼 그룹
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("취소")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("확인")
                }
            }

            CodeSnippet(
                """
Button(
    onClick = { },
    modifier = Modifier.fillMaxWidth()
) {
    Text("결제하기")
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun DisabledButtonSection() {
    var isFormValid by remember { mutableStateOf(false) }

    AdvancedCard(
        title = "6. 비활성화 상태",
        description = "enabled = false로 버튼을 비활성화합니다. 폼 검증 전이나 조건이 충족되지 않을 때 사용합니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isFormValid,
                    onCheckedChange = { isFormValid = it }
                )
                Text("약관에 동의합니다")
            }

            Button(
                onClick = { },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("가입하기")
            }

            // 비활성화 색상 커스터마이징
            Button(
                onClick = { },
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f),
                    disabledContentColor = Color.Gray
                )
            ) {
                Text("커스텀 비활성화 스타일")
            }

            CodeSnippet(
                """
Button(
    onClick = { },
    enabled = isFormValid // 조건에 따라 활성화
) {
    Text("가입하기")
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun AccessibilitySection() {
    AdvancedCard(
        title = "7. 접근성 (Accessibility)",
        description = "IconButton 등 텍스트가 없는 버튼에는 반드시 contentDescription을 제공해야 합니다."
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // 잘못된 예 (contentDescription = null)
                IconButton(
                    onClick = { },
                    modifier = Modifier.semantics {
                        contentDescription = "설정 열기" // 접근성 설명 추가
                    }
                ) {
                    Icon(Icons.Filled.Settings, contentDescription = "설정")
                }

                // Icon의 contentDescription 사용
                IconButton(onClick = { }) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "좋아요 추가" // TalkBack이 읽어줌
                    )
                }
            }

            Text(
                text = "TalkBack 사용자가 버튼의 기능을 이해할 수 있도록 설명을 제공하세요.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CodeSnippet(
                """
IconButton(onClick = { }) {
    Icon(
        Icons.Filled.Favorite,
        contentDescription = "좋아요 추가" // 접근성 설명
    )
}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun AdvancedCard(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun CodeSnippet(code: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = code,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
    }
}
