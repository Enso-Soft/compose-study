package com.example.web_wasm

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
import androidx.compose.ui.unit.dp

/**
 * 연습 문제 1: 프로젝트 구조 이해 (쉬움)
 *
 * 목표: wasmJs 타겟 설정을 위한 build.gradle.kts 코드를 완성하세요.
 */
@Composable
fun Practice1_StructureScreen() {
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
                    text = "연습 1: 프로젝트 구조 이해",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "wasmJs 타겟을 설정하기 위한 build.gradle.kts 코드에서 " +
                            "빈칸을 채워 완성하세요."
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
                Spacer(modifier = Modifier.height(4.dp))
                Text("- wasmJs 블록 안에 browser 블록이 있습니다")
                Text("- browser 블록 안에 binaries.executable() 호출이 필요합니다")
                Text("- 실행 명령어는 ./gradlew wasmJsBrowserRun 입니다")
            }
        }

        // 연습 영역
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF3E5F5)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Practice1_Exercise()
            }
        }

        // 정답 카드
        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Text(
                            text = """
kotlin {
    wasmJs {
        browser {
            binaries.executable()
        }
    }
}

// 실행 명령어:
// ./gradlew wasmJsBrowserRun
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD4D4D4),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice1_Exercise() {
    Text(
        text = "빈칸을 채우세요:",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(12.dp))

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // TODO: 학습자가 빈칸을 채워야 합니다
            Text(
                text = "kotlin {",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4FC1FF),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "    _____ {  // TODO: 웹 타겟 설정",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFD700),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "        _____ {  // TODO: 브라우저 환경",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFD700),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "            _____  // TODO: 실행 파일 생성",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFD700),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "        }",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4FC1FF),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "    }",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4FC1FF),
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4FC1FF),
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "// 실행 명령어: ./gradlew _____",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFD700),
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// ============================================================

/**
 * 연습 문제 2: 코드 변환 (중간)
 *
 * 목표: Android Compose 코드에서 웹에서 사용할 수 없는 API를 식별하고
 * 대안을 제시하세요.
 */
@Composable
fun Practice2_ConversionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 2: 코드 변환",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "아래 Android Compose 코드에서 웹에서 사용할 수 없는 API를 " +
                            "찾고, 웹 호환 가능한 대안을 제시하세요."
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- Android 전용 API: Toast, Context, Intent")
                Text("- 웹 대안: window.alert(), localStorage, window.open()")
                Text("- 순수 Compose UI 코드는 그대로 사용 가능합니다")
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Practice2_Exercise()
            }
        }

        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "문제가 되는 코드:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("1. Toast.makeText(context, ...) - Android 전용")
                    Text("2. LocalContext.current - Android 전용")
                    Text("3. startActivity(Intent) - Android 전용")

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "웹 대안:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Text(
                            text = """
// Toast 대안
external fun alert(message: String)
alert("저장되었습니다!")

// Context 대안
// 웹에서는 Context가 필요 없음

// Intent 대안
external val window: Window
window.open("https://example.com", "_blank")
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD4D4D4),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice2_Exercise() {
    Text(
        text = "문제가 있는 Android 코드:",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(12.dp))

    // TODO: 학습자가 문제를 찾아야 합니다
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Text(
            text = """
@Composable
fun SaveButton() {
    val context = LocalContext.current  // 문제?

    Button(onClick = {
        // 데이터 저장 로직...

        Toast.makeText(  // 문제?
            context,
            "저장되었습니다!",
            Toast.LENGTH_SHORT
        ).show()
    }) {
        Text("저장")
    }
}

@Composable
fun OpenBrowserButton(url: String) {
    val context = LocalContext.current  // 문제?

    Button(onClick = {
        val intent = Intent(Intent.ACTION_VIEW)  // 문제?
        intent.data = Uri.parse(url)
        context.startActivity(intent)  // 문제?
    }) {
        Text("브라우저 열기")
    }
}
            """.trimIndent(),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFD4D4D4),
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(12.dp)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "질문:",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
    Text("1. 웹에서 사용할 수 없는 코드는 무엇인가요?")
    Text("2. 각각의 웹 대안은 무엇인가요?")
}

// ============================================================

/**
 * 연습 문제 3: JavaScript Interop 설계 (어려움)
 *
 * 목표: localStorage를 사용하는 데이터 저장 유틸리티를 설계하세요.
 */
@Composable
fun Practice3_InteropScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "연습 3: JavaScript Interop 설계",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "브라우저의 localStorage API를 Kotlin에서 사용할 수 있도록 " +
                            "external 선언과 유틸리티 함수를 설계하세요."
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "힌트",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("- external 키워드로 JavaScript 함수/객체 선언")
                Text("- localStorage.getItem(key), setItem(key, value), removeItem(key)")
                Text("- 반환값이 null일 수 있으므로 String? 타입 사용")
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFCE4EC)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Practice3_Exercise()
            }
        }

        var showAnswer by remember { mutableStateOf(false) }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "정답 보기",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showAnswer = !showAnswer }) {
                        Text(if (showAnswer) "숨기기" else "펼치기")
                    }
                }
                if (showAnswer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Text(
                            text = """
// 1. localStorage external 선언
external object localStorage {
    fun getItem(key: String): String?
    fun setItem(key: String, value: String)
    fun removeItem(key: String)
    fun clear()
}

// 2. 유틸리티 객체
object WebStorage {
    fun save(key: String, value: String) {
        localStorage.setItem(key, value)
    }

    fun load(key: String): String? {
        return localStorage.getItem(key)
    }

    fun remove(key: String) {
        localStorage.removeItem(key)
    }

    fun loadOrDefault(
        key: String,
        default: String
    ): String {
        return localStorage.getItem(key) ?: default
    }
}

// 3. 사용 예시
fun saveUserName(name: String) {
    WebStorage.save("user_name", name)
}

fun loadUserName(): String {
    return WebStorage.loadOrDefault(
        "user_name",
        "Guest"
    )
}
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD4D4D4),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Practice3_Exercise() {
    Text(
        text = "요구사항:",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text("1. localStorage를 external로 선언하세요")
    Text("2. 편리하게 사용할 수 있는 WebStorage 유틸리티 객체를 만드세요")
    Text("3. 값이 없을 때 기본값을 반환하는 함수를 추가하세요")

    Spacer(modifier = Modifier.height(12.dp))

    // TODO: 학습자가 직접 구현해야 합니다
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Text(
            text = """
// TODO: localStorage external 선언
external object localStorage {
    // TODO: getItem 함수 선언
    // TODO: setItem 함수 선언
    // TODO: removeItem 함수 선언
}

// TODO: WebStorage 유틸리티 객체
object WebStorage {
    // TODO: save(key, value) 함수

    // TODO: load(key) 함수 - null 가능

    // TODO: remove(key) 함수

    // TODO: loadOrDefault(key, default) 함수
}

// 사용 예시
fun example() {
    WebStorage.save("theme", "dark")
    val theme = WebStorage.loadOrDefault("theme", "light")
    // theme = "dark"
}
            """.trimIndent(),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFD4D4D4),
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(12.dp)
        )
    }
}
