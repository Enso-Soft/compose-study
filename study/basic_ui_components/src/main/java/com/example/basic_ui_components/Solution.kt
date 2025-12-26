package com.example.basic_ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Solution: 기본 UI 컴포넌트 올바르게 사용하기
 *
 * 1. Text - 스타일링과 MaterialTheme 활용
 * 2. Button - onClick과 다양한 버튼 종류
 * 3. TextField - 상태 연결
 * 4. Icon - contentDescription과 tint
 */

@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 해결책 소개
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "해결책: 기본 컴포넌트 마스터하기",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Text, Button, TextField, Icon의 올바른 사용법을 알아봅니다!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 1. Text 갤러리
        TextGallery()

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Button 갤러리
        ButtonGallery()

        Spacer(modifier = Modifier.height(16.dp))

        // 3. TextField 예제
        TextFieldDemo()

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Icon 갤러리
        IconGallery()
    }
}

@Composable
private fun TextGallery() {
    SolutionCard(
        title = "1. Text 스타일링",
        explanation = "MaterialTheme.typography로 일관된 스타일을 적용합니다"
    ) {
        // MaterialTheme Typography
        Text(
            text = "displayLarge",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "headlineMedium",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "titleLarge",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "bodyLarge (기본)",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "labelSmall",
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        // 커스텀 스타일
        Text(
            text = "커스텀 스타일:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bold Text",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Colored Text",
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Large Text (24sp)",
            fontSize = 24.sp
        )
        Text(
            text = "Center Aligned",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ButtonGallery() {
    var clickCount by remember { mutableIntStateOf(0) }

    SolutionCard(
        title = "2. Button 종류",
        explanation = "상황에 맞는 버튼을 선택하세요"
    ) {
        // 클릭 카운터
        Text(
            text = "클릭 횟수: $clickCount",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 버튼 종류
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { clickCount++ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Button")
            }

            OutlinedButton(
                onClick = { clickCount++ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Outlined")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                onClick = { clickCount++ },
                modifier = Modifier.weight(1f)
            ) {
                Text("TextButton")
            }

            FilledTonalButton(
                onClick = { clickCount++ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Tonal")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 아이콘 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { clickCount++ }) {
                Icon(Icons.Default.Favorite, "좋아요")
            }
            IconButton(onClick = { clickCount++ }) {
                Icon(Icons.Default.Share, "공유")
            }
            IconButton(onClick = { clickCount++ }) {
                Icon(Icons.Default.Add, "추가")
            }
            IconButton(onClick = { clickCount = 0 }) {
                Icon(Icons.Default.Refresh, "리셋")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 비활성 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { },
                enabled = false,
                modifier = Modifier.weight(1f)
            ) {
                Text("Disabled")
            }

            Button(
                onClick = { },
                enabled = true,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Send, null)
                Spacer(Modifier.width(8.dp))
                Text("With Icon")
            }
        }
    }
}

@Composable
private fun TextFieldDemo() {
    var basicText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    SolutionCard(
        title = "3. TextField 상태 연결",
        explanation = "value와 onValueChange로 상태를 연결해야 입력이 동작합니다"
    ) {
        // 기본 TextField
        TextField(
            value = basicText,
            onValueChange = { basicText = it },
            label = { Text("기본 TextField") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Outlined TextField
        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, null) },
            placeholder = { Text("example@email.com") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 비밀번호 TextField
        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 입력값 표시
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "입력 결과:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("기본: $basicText")
                Text("이메일: $emailText")
                Text("비밀번호: ${"*".repeat(passwordText.length)}")
            }
        }
    }
}

@Composable
private fun IconGallery() {
    SolutionCard(
        title = "4. Icon 사용하기",
        explanation = "Icons.Default.XXX로 Material 아이콘을 사용합니다"
    ) {
        // 자주 쓰는 아이콘들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconWithLabel(Icons.Default.Home, "Home")
            IconWithLabel(Icons.Default.Search, "Search")
            IconWithLabel(Icons.Default.Settings, "Settings")
            IconWithLabel(Icons.Default.Person, "Person")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconWithLabel(Icons.Default.Favorite, "Favorite")
            IconWithLabel(Icons.Default.Star, "Star")
            IconWithLabel(Icons.Default.Delete, "Delete")
            IconWithLabel(Icons.Default.Edit, "Edit")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 색상 적용
        Text(
            text = "Tint 색상 적용:",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = "빨간 하트",
                tint = Color.Red,
                modifier = Modifier.size(32.dp)
            )
            Icon(
                Icons.Default.Star,
                contentDescription = "노란 별",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(32.dp)
            )
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "초록 체크",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(32.dp)
            )
            Icon(
                Icons.Default.Info,
                contentDescription = "파란 정보",
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun IconWithLabel(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun SolutionCard(
    title: String,
    explanation: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}
