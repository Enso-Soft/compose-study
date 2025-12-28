package com.example.autofill

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * 기본 사용법 화면
 *
 * Autofill의 기본적인 사용 방법을 학습합니다.
 * - ContentType 설정
 * - 다양한 ContentType 종류
 * - 기본 로그인 폼 예제
 */
@Composable
fun BasicUsageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 개요 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Autofill(자동완성)이란?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "시스템이 저장된 사용자 정보(이메일, 비밀번호 등)를 " +
                            "TextField에 자동으로 채워주는 기능입니다. " +
                            "Modifier.semantics { contentType = ... }으로 설정합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // 기능 1: 단일 ContentType 설정
        FeatureCard(
            title = "기능 1: ContentType 설정",
            description = "TextField에 contentType을 설정하면 시스템 자동완성이 활성화됩니다."
        ) {
            SingleContentTypeDemo()
        }

        // 기능 2: ContentType 종류
        FeatureCard(
            title = "기능 2: ContentType 종류",
            description = "다양한 ContentType을 탐색해보세요. 각 타입에 맞는 자동완성 제안이 표시됩니다."
        ) {
            ContentTypesDemo()
        }

        // 기능 3: 기본 로그인 폼
        FeatureCard(
            title = "기능 3: 기본 로그인 폼",
            description = "실제 로그인 화면에서 사용되는 Autofill 설정 예제입니다."
        ) {
            BasicLoginFormDemo()
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    description: String,
    demo: @Composable () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            demo()
        }
    }
}

/**
 * 단일 ContentType 설정 데모
 */
@Composable
private fun SingleContentTypeDemo() {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "아래 필드에 ContentType.EmailAddress가 설정되어 있습니다:",
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.EmailAddress
                },
            singleLine = true
        )

        Text(
            text = "코드: Modifier.semantics { contentType = ContentType.EmailAddress }",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/**
 * ContentType 종류 데모
 */
@Composable
private fun ContentTypesDemo() {
    var selectedType by remember { mutableIntStateOf(0) }
    var inputValue by remember { mutableStateOf("") }

    val contentTypes = listOf(
        Triple("Username", ContentType.Username, Icons.Default.Person),
        Triple("Password", ContentType.Password, Icons.Default.Lock),
        Triple("Email", ContentType.EmailAddress, Icons.Default.Email),
        Triple("Phone", ContentType.PhoneNumber, Icons.Default.Phone)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ContentType 선택 칩
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            contentTypes.forEachIndexed { index, (name, _, _) ->
                FilterChip(
                    selected = selectedType == index,
                    onClick = {
                        selectedType = index
                        inputValue = ""
                    },
                    label = { Text(name) }
                )
            }
        }

        // 선택된 ContentType에 맞는 TextField
        val (name, type, icon) = contentTypes[selectedType]

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text(name) },
            leadingIcon = { Icon(icon, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = type
                },
            visualTransformation = if (name == "Password") {
                PasswordVisualTransformation()
            } else {
                androidx.compose.ui.text.input.VisualTransformation.None
            },
            singleLine = true
        )

        Text(
            text = "현재 설정: ContentType.$name",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 기본 로그인 폼 데모
 */
@Composable
private fun BasicLoginFormDemo() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 사용자 이름 필드
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("사용자 이름") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.Username
                },
            singleLine = true
        )

        // 비밀번호 필드
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentType = ContentType.Password
                },
            singleLine = true
        )

        // 로그인 버튼
        Button(
            onClick = {
                loginResult = if (username.isNotEmpty() && password.isNotEmpty()) {
                    "로그인 성공! (사용자: $username)"
                } else {
                    "사용자 이름과 비밀번호를 입력하세요"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인")
        }

        // 결과 표시
        if (loginResult.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (loginResult.contains("성공")) {
                        MaterialTheme.colorScheme.tertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (loginResult.contains("성공")) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.Warning
                        },
                        contentDescription = null
                    )
                    Text(loginResult)
                }
            }
        }
    }
}
