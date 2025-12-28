package com.example.web_wasm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
 * Solution 화면: Compose for Web으로 해결
 *
 * 이 화면에서는 Compose for Web을 사용하여
 * 전통적 웹 개발의 문제점들을 어떻게 해결하는지 보여줍니다.
 */
@Composable
fun SolutionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 해결책 개요 카드
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Compose for Web 소개",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kotlin/Wasm으로 브라우저에서 Compose UI를 실행하여 모든 문제를 해결합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Compose for Web 핵심 개념
        ComposeWebOverview()

        // 해결책 1: 단일 언어
        Solution1_SingleLanguage()

        // 해결책 2: 코드 공유
        Solution2_CodeSharing()

        // 해결책 3: 타입 안전성
        Solution3_TypeSafety()

        // 프로젝트 설정 가이드
        ProjectSetupGuide()

        // 실행 명령어
        RunCommands()
    }
}

@Composable
private fun ComposeWebOverview() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Compose for Web이란?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 핵심 특징
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeatureChip("Kotlin/Wasm", "WebAssembly 기반")
                FeatureChip("Canvas 렌더링", "Skia 그래픽")
                FeatureChip("2025 Beta", "프로덕션 준비")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 아키텍처 다이어그램
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 공유 코드
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "공유 Kotlin 코드\n(UI, 비즈니스 로직)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )
                    }

                    // 화살표
                    Text(
                        text = "|\n|   |   |   |\n V   V   V   V",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // 플랫폼들
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        PlatformBadge("Android", Color(0xFF3DDC84))
                        PlatformBadge("iOS", Color(0xFFFF6B6B))
                        PlatformBadge("Desktop", Color(0xFF5C6BC0))
                        PlatformBadge("Web", Color(0xFFFFB74D))
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureChip(title: String, subtitle: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun PlatformBadge(name: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        )
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun Solution1_SingleLanguage() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier
                        .background(
                            Color(0xFFE8F5E9),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "해결책 1: 단일 언어 (Kotlin)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "HTML, CSS, JavaScript 대신 Kotlin 하나만 배우면 됩니다:",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 비교
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Before
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Before",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Red
                        )
                        Text(
                            text = "6+",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "기술 스택",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // After
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "After",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "1",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kotlin",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Solution2_CodeSharing() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier
                        .background(
                            Color(0xFFE8F5E9),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "해결책 2: 코드 공유",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "한 번 작성한 UI 코드를 모든 플랫폼에서 사용합니다:",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 공유 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "// commonMain - 모든 플랫폼에서 공유",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6A9955),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "@Composable",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFDCDCAA),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "fun Greeting(name: String) {",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4FC1FF),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "    Text(\"Hello, \$name!\")",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCE9178),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4FC1FF),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 결과
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ResultItem("개발 시간", "1/3")
                    ResultItem("버그 수정", "1회")
                    ResultItem("유지보수", "1곳")
                }
            }
        }
    }
}

@Composable
private fun ResultItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun Solution3_TypeSafety() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier
                        .background(
                            Color(0xFFE8F5E9),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "해결책 3: 타입 안전성",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Kotlin의 타입 시스템이 컴파일 타임에 에러를 검출합니다:",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Kotlin 코드 예시
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "// Kotlin - 컴파일 타임에 에러 검출",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6A9955),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "fun greet(user: User): String {",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4FC1FF),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "    return \"Hello, \${user.name}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCE9178),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4FC1FF),
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "greet(null)  // 컴파일 에러!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4CAF50),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 성공 메시지
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "앱 실행 전에 모든 타입 에러를 발견합니다!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectSetupGuide() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "프로젝트 설정",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // build.gradle.kts 예시
            Text(
                text = "build.gradle.kts:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

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

    sourceSets {
        val wasmJsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.material3)
            }
        }
    }
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

@Composable
private fun RunCommands() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "실행 명령어",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            CommandItem(
                command = "./gradlew wasmJsBrowserRun",
                description = "개발 서버 실행 (http://localhost:8080)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            CommandItem(
                command = "./gradlew wasmJsBrowserDistribution",
                description = "프로덕션 빌드 생성"
            )
        }
    }
}

@Composable
private fun CommandItem(command: String, description: String) {
    Column {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Text(
                text = command,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4EC9B0),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}
